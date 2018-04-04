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
import static org.apache.jena.riot.WebContent.contentTypeNTriples;
import static org.ubl.scb.JsonSerializer.serializeToBytes;

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
import java.util.Set;
import java.util.UUID;

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
import org.ubl.scb.JsonLdUtils;
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
public class ResourceCreatorTest extends CommonTests {

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
    private JsonLdUtils jsonLdUtils = new JsonLdUtils();
    private static Integer startIndex;
    private static Integer toIndex;

    @BeforeAll
    static void initAll() {
        //APP.before();
        startIndex = 225;
        toIndex = 226;
        baseUrl = "https://workspaces.ub.uni-leipzig.de:8445/";
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
        pid = "ldp-test-" + UUID.randomUUID()
                                .toString();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createDefaultContainers() {
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
            h2client.newLdpDc(collectionId, "meta", collectionId);
        } catch (LdpClientException e) {
            e.printStackTrace();
        }
    }

    @Test
    void putDimensionManifestResource() throws Exception {
        final IRI identifier = rdf.createIRI(baseUrl + "collection/vp/meta" + dimensionManifestFile);
        final InputStream is = ResourceCreatorTest.class.getResourceAsStream(dimensionManifestFile);
        h2client.put(identifier, is, "application/json");
    }

    @Test
    void putMetadataResource() throws Exception {
        final IRI identifier = rdf.createIRI(baseUrl + "collection/vp/meta" + metadataFile);
        final InputStream is = ResourceCreatorTest.class.getResourceAsStream(metadataFile);
        h2client.put(identifier, is, "text/tab-separated-values");
    }

    @Test
    void testPutImageResourceBatchFromSubList() throws Exception {
        final ImageMetadataGeneratorConfig imageMetadataGeneratorConfig = new ImageMetadataGeneratorConfig();
        imageMetadataGeneratorConfig.setImageSourceDir(imageSourceDir);
        final VorlesungImpl vi = new VorlesungImpl(imageMetadataGeneratorConfig);
        final List<File> files = vi.getFiles();
        files.sort(Comparator.naturalOrder());
        final List<File> sublist = files.subList(startIndex, toIndex);
        final Map<URI, InputStream> batch = new HashMap<>();
        for (File file : sublist) {
            final IRI identifier = rdf.createIRI(baseUrl + bodyContainer + file.getName()
                                                                               .toLowerCase());
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
            final IRI identifier = rdf.createIRI(baseUrl + "vp/res/" + file.getName());
            final InputStream is = new FileInputStream(file);
            h2client.put(identifier, is, "image/tiff");
        }
    }

    @Test
    void deleteContainer() {
        try {
            final IRI identifier = rdf.createIRI("https://workspaces.ub.uni-leipzig"
                    + ".de:8445/collection/vp/anno/de6ccd15-5fd8-4205-93db-3b502acade6a");
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
            h2client.put(
                    identifier, new ByteArrayInputStream(acl.getACL()
                                                            .toByteArray()), contentTypeNTriples);
        } catch (LdpClientException e) {
            e.printStackTrace();
        }
    }

    @Test
    void putCanvases() throws Exception {
        final ImageMetadataGeneratorConfig imageMetadataGeneratorConfig = new ImageMetadataGeneratorConfig();
        final ScbConfig scbConfig = new ScbConfig();
        scbConfig.setBaseUrl(baseUrl);
        scbConfig.setTargetContainer(targetContainer);
        imageMetadataGeneratorConfig.setDimensionManifestFilePath(dimensionManifestFile);
        scbConfig.setMetadata(metadataFile);
        final TargetBuilder tb = new TargetBuilder(imageMetadataGeneratorConfig, scbConfig);
        final List<TemplateTarget> targetList = tb.buildCanvases();
        targetList.sort(Comparator.comparing(TemplateTarget::getCanvasLabel));
        final List<TemplateTarget> sublist = targetList.subList(startIndex, toIndex);
        final Map<URI, InputStream> batch = new HashMap<>();
        for (TemplateTarget target : sublist) {
            final IRI identifier = rdf.createIRI(target.getCanvasId());
            final URI uri = new URI(identifier.getIRIString());
            final InputStream is = new ByteArrayInputStream(
                    Objects.requireNonNull(serializeToBytes(target).orElse(null)));
            final String n3 = (String) jsonLdUtils.unmarshallToNQuads(is);
            final InputStream n3Stream = new ByteArrayInputStream(Objects.requireNonNull(n3)
                                                                         .getBytes());
            batch.put(uri, n3Stream);
        }
        h2client.joiningCompletableFuturePut(batch, contentTypeNTriples);
    }

    @Test
    void putAnnotations() throws Exception {
        final ImageMetadataGeneratorConfig imageMetadataGeneratorConfig = new ImageMetadataGeneratorConfig();
        final ScbConfig scbConfig = new ScbConfig();
        scbConfig.setBaseUrl(baseUrl);
        scbConfig.setMetadata(metadataFile);
        imageMetadataGeneratorConfig.setDimensionManifestFilePath(
                AnnotationBuilderTest.class.getResource(dimensionManifestFile)
                                           .getPath());
        scbConfig.setAnnotationContainer(annotationContainer);
        scbConfig.setTargetContainer(targetContainer);
        scbConfig.setBodyContainer(bodyContainer);
        scbConfig.setImageServiceBaseUrl(imageServiceBaseUrl);
        scbConfig.setImageServiceType(imageServiceType);
        final AnnotationBuilder ab = new AnnotationBuilder(imageMetadataGeneratorConfig, scbConfig);
        final List<TemplateTarget> targetList = getTargetList(scbConfig, imageMetadataGeneratorConfig);
        final List<TemplateWebAnnotation> annoList = ab.getAnnotationsWithDimensionedBodies(targetList);
        final List<TemplateWebAnnotation> sublist = annoList.subList(startIndex, toIndex);
        final Map<URI, InputStream> batch = new HashMap<>();
        for (TemplateWebAnnotation webAnno : sublist) {
            final IRI identifier = rdf.createIRI(webAnno.getAnnoId());
            final URI uri = new URI(identifier.getIRIString());
            System.out.println("Annotation " + webAnno.getAnnoId() + " for Image Resource " + webAnno.getBody()
                                                                                                     .getResourceId()
                    + " created");
            final InputStream is = new ByteArrayInputStream(
                    Objects.requireNonNull(serializeToBytes(webAnno).orElse(null)));
            final String n3 = (String) jsonLdUtils.unmarshallToNQuads(is);
            final InputStream n3Stream = new ByteArrayInputStream(Objects.requireNonNull(n3)
                                                                         .getBytes());
            batch.put(uri, n3Stream);
        }
        h2client.joiningCompletableFuturePut(batch, contentTypeNTriples);
    }

    private List<TemplateTarget> getTargetList(final ScbConfig scbConfig, final ImageMetadataGeneratorConfig
            imageMetadataGeneratorConfig) {
        scbConfig.setBaseUrl(baseUrl);
        scbConfig.setTargetContainer(targetContainer);
        imageMetadataGeneratorConfig.setDimensionManifestFilePath(dimensionManifestFile);
        scbConfig.setMetadata(metadataFile);
        final TargetBuilder tb = new TargetBuilder(imageMetadataGeneratorConfig, scbConfig);
        return tb.buildCanvases();
    }
}
