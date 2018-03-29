/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.ubleipzig.scb;

import static io.dropwizard.testing.ConfigOverride.config;
import static io.dropwizard.testing.ResourceHelpers.resourceFilePath;
import static javax.ws.rs.core.HttpHeaders.LINK;
import static org.apache.jena.riot.WebContent.contentTypeJSONLD;
import static org.apache.jena.riot.WebContent.contentTypeNTriples;
import static org.ubl.scb.JSONSerializer.serialize;

import cool.pandora.ldpclient.ACLStatement;
import cool.pandora.ldpclient.LdpClient;
import cool.pandora.ldpclient.LdpClientException;
import cool.pandora.ldpclient.LdpClientImpl;
import cool.pandora.ldpclient.SimpleSSLContext;

import io.dropwizard.testing.DropwizardTestSupport;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import javax.net.ssl.SSLContext;

import org.apache.commons.rdf.api.IRI;
import org.apache.commons.rdf.jena.JenaRDF;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.trellisldp.app.TrellisApplication;
import org.trellisldp.app.config.TrellisConfiguration;
import org.trellisldp.vocabulary.ACL;
import org.trellisldp.vocabulary.LDP;
import org.ubl.image.metadata.ImageMetadataGeneratorConfig;
import org.ubl.scb.AnnotationBuilder;
import org.ubl.scb.ScbConfig;
import org.ubl.scb.TargetBuilder;
import org.ubl.scb.VorlesungImpl;
import org.ubl.scb.templates.TemplateTarget;
import org.ubl.scb.templates.TemplateWebAnnotation;


/**
 * ResourceCreatorTest.
 *
 * @author christopher-johnson
 */
public class ResourceCreatorTest {

    private static final DropwizardTestSupport<TrellisConfiguration> APP = new DropwizardTestSupport<>(
            TrellisApplication.class, resourceFilePath("trellis-config.yml"),
            config("server" + "" + "" + "" + "" + ".applicationConnectors[1].port", "8445"),
            config("binaries", resourceFilePath("data") + "/binaries"),
            config("mementos", resourceFilePath("data") + "/mementos"),
            config("namespaces", resourceFilePath("data/namespaces.json")),
            config("server.applicationConnectors[1].keyStorePath", resourceFilePath("keystore/trellis.jks")));
    private static final JenaRDF rdf = new JenaRDF();
    private static String baseUrl;
    private static String pid;
    private static LdpClient h2client = null;
    private String imageSourceDir = "/media/christopher/OVAUBIMG/UBiMG/images/ubleipzig_sk2";
    private String metadataFile = "/sk2-titles-semester.tsv";
    private String imageServiceBase = "http://localhost:5000/iiif/";
    private String dimensionManifestFile = "/dimension-manifest-test-8efc742f-709e-47ea-a346-e7bdc3266b49";

    @BeforeAll
    static void initAll() {
        //APP.before();
        baseUrl = "https://localhost:8445/";
        try {
            final SimpleSSLContext sslct = new SimpleSSLContext();
            final SSLContext sslContext = sslct.get();
            h2client = new LdpClientImpl(sslContext);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @AfterAll
    static void tearDownAll() {

        //APP.after();
    }

    private static InputStream getTestResource() {
        return ResourceCreatorTest.class.getResourceAsStream("/data/empty.ttl");
    }

    @BeforeEach
    void init() {
        pid = "ldp-test-" + UUID.randomUUID().toString();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createDefaultContainers(){
        final IRI identifier = rdf.createIRI(baseUrl);
        final IRI collectionBase = rdf.createIRI(baseUrl + "collection");
        final IRI collectionId = rdf.createIRI(baseUrl + "collection/vp");
        try {
            h2client.newLdpDc(identifier, "collection", identifier);
            h2client.newLdpDc(collectionBase, "vp", collectionBase);
            h2client.newLdpDc(collectionId, "res", collectionId);
            h2client.newLdpDc(collectionId, "target", collectionId);
            h2client.newLdpDc(collectionId, "body", collectionId);
            h2client.newLdpDc(collectionId, "anno", collectionId);
        } catch (LdpClientException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testPutImageResourceBatchFromSubList() throws Exception {
        final Integer STARTINDEX = 110;
        final Integer TOINDEX = 120;
        final ImageMetadataGeneratorConfig imageMetadataGeneratorConfig = new ImageMetadataGeneratorConfig();
        imageMetadataGeneratorConfig.setImageSourceDir(imageSourceDir);
        final VorlesungImpl vi = new VorlesungImpl(imageMetadataGeneratorConfig);
        final List<File> files = vi.getFiles();
        files.sort(Comparator.naturalOrder());
        List<File> sublist = files.subList(STARTINDEX, TOINDEX);
        final Map<URI, InputStream> batch = new HashMap<>();
        for (File file : sublist) {
            final IRI identifier = rdf.createIRI(baseUrl + "collection/vp/res/" + file.getName().toLowerCase());
            final URI uri = new URI(identifier.getIRIString());
            final InputStream is = new FileInputStream(file);
            batch.put(uri, is);
        }
        h2client.joiningCompletableFuturePut(batch, "image/tiff");
    }

    @Test
    void testPutImageResourcewithAsync() throws Exception {
        final ImageMetadataGeneratorConfig imageMetadataGeneratorConfig = new ImageMetadataGeneratorConfig();
        imageMetadataGeneratorConfig.setImageSourceDir(imageSourceDir);
        final VorlesungImpl vi = new VorlesungImpl(imageMetadataGeneratorConfig);
        final List<File> files = vi.getFiles();
        for (File file : files) {
            final IRI identifier = rdf.createIRI(baseUrl + "vp/resources/" + file.getName());
            final InputStream is = new FileInputStream(file);
            h2client.put(identifier, is, "image/tiff");
        }
    }

    @Test
    void deleteContainer() {
        try {
            final IRI identifier = rdf.createIRI("http://localhost:8080/collection/vp/res3/00000037.tif");
            h2client.delete(identifier);
        } catch (LdpClientException e) {
            e.printStackTrace();
        }
    }

    @Test
    void createDirectContainer() {
        try {
            final IRI identifier = rdf.createIRI("http://localhost:8080/collection/vp");
            h2client.newLdpDc(identifier, "resources", identifier);
        } catch (LdpClientException e) {
            e.printStackTrace();
        }
    }

    @Test
    void createBasicContainer() {
        try {
            final IRI identifier = rdf.createIRI("http://localhost:8080/test4");
            final Map<String, String> metadata = new HashMap<>();
            metadata.put(LINK, LDP.BasicContainer + "; rel=\"type\"");
            h2client.putWithMetadata(identifier, getTestResource(), metadata);
        } catch (LdpClientException e) {
            e.printStackTrace();
        }
    }

    @Test
    void createContainerACL() {
        final Set<IRI> modes = new HashSet<>();
        modes.add(ACL.Read);
        modes.add(ACL.Write);
        modes.add(ACL.Control);
        final IRI agent = rdf.createIRI("http://xmlns.com/foaf/0.1/Agent");
        final IRI accessTo = rdf.createIRI("http://localhost:8080/test6");
        try {
            final IRI identifier = rdf.createIRI("http://localhost:8080/test6?ext=acl");
            final ACLStatement acl = new ACLStatement(modes, agent, accessTo);
            h2client.put(identifier, new ByteArrayInputStream(acl.getACL().toByteArray()), contentTypeNTriples);
        } catch (LdpClientException e) {
            e.printStackTrace();
        }
    }

    @Test
    void putCanvases() {
        final ImageMetadataGeneratorConfig imageMetadataGeneratorConfig = new ImageMetadataGeneratorConfig();
        final ScbConfig scbConfig = new ScbConfig();
        scbConfig.setBaseUrl(baseUrl);
        imageMetadataGeneratorConfig.setImageSourceDir(imageSourceDir);
        scbConfig.setMetadataFile(metadataFile);
        imageMetadataGeneratorConfig.setDimensionManifestFilePath(DeserializeDimensionManifestTest.class.getResource(
                dimensionManifestFile).getPath());
        final TargetBuilder cb = new TargetBuilder(imageMetadataGeneratorConfig, scbConfig);
        final List<TemplateTarget> canvaslist = cb.buildCanvases();
        for (TemplateTarget canvas : canvaslist) {
            final IRI identifier = rdf.createIRI(canvas.getCanvasId());
            final Optional<String> json = serialize(canvas);
            final String output = json.orElse(null);
            final InputStream is = new ByteArrayInputStream(Objects.requireNonNull(output).getBytes());
            try {
                h2client.put(identifier, is, contentTypeJSONLD);
            } catch (LdpClientException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    void putAnnotations() {
        final ImageMetadataGeneratorConfig imageMetadataGeneratorConfig = new ImageMetadataGeneratorConfig();
        final ScbConfig scbConfig = new ScbConfig();
        scbConfig.setBaseUrl(baseUrl);
        imageMetadataGeneratorConfig.setImageSourceDir(imageSourceDir);
        scbConfig.setMetadataFile(metadataFile);
        imageMetadataGeneratorConfig.setDimensionManifestFilePath(DeserializeDimensionManifestTest.class.getResource(
                dimensionManifestFile).getPath());
        scbConfig.setImageServiceBaseUrl(imageServiceBase);
        scbConfig.setAnnotationContext("vp/anno/");
        scbConfig.setBodyContext("vp/anno/");
        final AnnotationBuilder ab = new AnnotationBuilder(imageMetadataGeneratorConfig, scbConfig);
        final List<TemplateWebAnnotation> annolist = ab.getAnnotationsWithDimensionedBodies();
        for (TemplateWebAnnotation anno : annolist) {
            final IRI identifier = rdf.createIRI(anno.getAnnoId());
            final Optional<String> json = serialize(anno);
            final String output = json.orElse(null);
            final InputStream is = new ByteArrayInputStream(Objects.requireNonNull(output).getBytes());
            try {
                h2client.put(identifier, is, contentTypeJSONLD);
            } catch (LdpClientException e) {
                e.printStackTrace();
            }
        }
    }

}
