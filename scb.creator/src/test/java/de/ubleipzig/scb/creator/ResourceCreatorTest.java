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

package de.ubleipzig.scb.creator;

import static de.ubleipzig.scb.creator.JsonSerializer.serializeToBytes;
import static io.dropwizard.testing.ConfigOverride.config;
import static io.dropwizard.testing.ResourceHelpers.resourceFilePath;
import static javax.ws.rs.core.HttpHeaders.LINK;
import static org.apache.jena.arq.riot.WebContent.contentTypeNTriples;

import de.ubleipzig.image.metadata.ImageMetadataServiceConfig;
import de.ubleipzig.scb.templates.TemplatePaintingAnnotation;
import de.ubleipzig.scb.templates.TemplateTaggingAnnotation;
import de.ubleipzig.scb.templates.TemplateTarget;

import io.dropwizard.testing.DropwizardTestSupport;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import javax.net.ssl.SSLContext;

import org.apache.commons.rdf.api.IRI;
import org.apache.commons.rdf.jena.JenaRDF;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.trellisldp.app.config.TrellisConfiguration;
import org.trellisldp.app.triplestore.TrellisApplication;
import org.trellisldp.client.ACLStatement;
import org.trellisldp.client.LdpClient;
import org.trellisldp.client.LdpClientException;
import org.trellisldp.client.LdpClientImpl;
import org.trellisldp.vocabulary.ACL;
import org.trellisldp.vocabulary.LDP;

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
        APP.before();
        startIndex = 0;
        toIndex = 10;
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

        APP.after();
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
    void testCreateDefaultContainers() {
        final IRI identifier = rdf.createIRI(baseUrl);
        final IRI collectionBase = rdf.createIRI(baseUrl + "collection");
        final IRI collectionId = rdf.createIRI(baseUrl + "collection/vp");
        try {
            h2client.newLdpDc(identifier, "collection", identifier);
            h2client.newLdpDc(collectionBase, "vp", collectionBase);
            h2client.newLdpDc(collectionId, "res", collectionId);
            h2client.newLdpDc(collectionId, "target", collectionId);
            h2client.newLdpDc(collectionId, "body", collectionId);
            h2client.newLdpDc(collectionId, "tag", collectionId);
            h2client.newLdpDc(collectionId, "anno", collectionId);
            h2client.newLdpDc(collectionId, "meta", collectionId);
        } catch (LdpClientException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testPutDimensionManifestResource() throws Exception {
        ScbConfig scbConfig = getScbConfig();
        final String dimensionManifestFilePath = scbConfig.getImageMetadataServiceConfig()
                .getDimensionManifestFilePath();
        final IRI identifier = rdf.createIRI(baseUrl + "collection/vp/meta/" + dimensionManifestFilePath);
        final InputStream is = ResourceCreatorTest.class.getResourceAsStream(dimensionManifestFilePath);
        h2client.put(identifier, is, "application/json");
    }

    @Test
    void testPutMetadataResource() throws Exception {
        ScbConfig scbConfig = getScbConfig();
        final String metadataFile = scbConfig.getMetadataFile();
        final IRI identifier = rdf.createIRI(baseUrl + "collection/vp/meta" + metadataFile);
        final InputStream is = ResourceCreatorTest.class.getResourceAsStream(metadataFile);
        h2client.put(identifier, is, "text/tab-separated-values");
    }

    @Disabled
    @Test
    void testPutImageResourceBatchFromSubList() throws Exception {
        ScbConfig scbConfig = getScbConfig();
        final ImageMetadataServiceConfig imageMetadataServiceConfig = new ImageMetadataServiceConfig();
        imageMetadataServiceConfig.setImageSourceDir(scbConfig.getImageMetadataServiceConfig().getImageSourceDir());
        final VorlesungImpl vi = new VorlesungImpl(imageMetadataServiceConfig);
        final List<File> files = vi.getFiles();
        files.sort(Comparator.naturalOrder());
        final List<File> sublist = files.subList(startIndex, toIndex);
        final Map<URI, InputStream> batch = new HashMap<>();
        for (File file : sublist) {
            final IRI identifier = rdf.createIRI(baseUrl + scbConfig.getBodyContainer() + file.getName().toLowerCase());
            final URI uri = new URI(identifier.getIRIString());
            final InputStream is = new FileInputStream(file);
            batch.put(uri, is);
        }
        h2client.joiningCompletableFuturePut(batch, "image/tiff");
    }

    @Disabled
    @Test
    void testPutImageResourcewithAsync() throws Exception {
        ScbConfig scbConfig = getScbConfig();
        final ImageMetadataServiceConfig imageMetadataServiceConfig = new ImageMetadataServiceConfig();
        imageMetadataServiceConfig.setImageSourceDir(scbConfig.getImageMetadataServiceConfig().getImageSourceDir());
        final VorlesungImpl vi = new VorlesungImpl(imageMetadataServiceConfig);
        final List<File> files = vi.getFiles();
        for (File file : files) {
            final IRI identifier = rdf.createIRI(baseUrl + "vp/res/" + file.getName());
            final InputStream is = new FileInputStream(file);
            h2client.put(identifier, is, "image/tiff");
        }
    }

    @Disabled
    @Test
    void testDeleteContainer() {
        final Path path = Paths.get("/tmp/batch-delete.txt");
        try (Stream<String> lines = Files.lines(path)) {
            lines.forEachOrdered(line -> {
                final IRI identifier = rdf.createIRI(line);
                try {
                    h2client.delete(identifier);
                } catch (LdpClientException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testCreateDirectContainer() {
        try {
            final IRI identifier = rdf.createIRI(baseUrl + "collection/vp");
            h2client.newLdpDc(identifier, "resources", identifier);
        } catch (LdpClientException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testCreateBasicContainer() {
        try {
            final IRI identifier = rdf.createIRI(baseUrl + "test4");
            final Map<String, String> metadata = new HashMap<>();
            metadata.put(LINK, LDP.BasicContainer + "; rel=\"type\"");
            h2client.putWithMetadata(identifier, getTestResource(), metadata);
        } catch (LdpClientException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testCreateContainerACL() {
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
    void testPutCanvases() throws Exception {
        ScbConfig scbConfig = getScbConfigWithAbsolutePath();
        final TargetBuilder tb = new TargetBuilder(scbConfig);
        final List<TemplateTarget> targetList = tb.buildCanvases();
        targetList.sort(Comparator.comparing(TemplateTarget::getCanvasLabel));
        final List<TemplateTarget> sublist = targetList.subList(startIndex, toIndex);
        final Map<URI, InputStream> batch = new HashMap<>();
        for (TemplateTarget target : sublist) {
            final IRI identifier = rdf.createIRI(target.getTargetId());
            final URI uri = new URI(identifier.getIRIString());
            final InputStream is = new ByteArrayInputStream(
                    Objects.requireNonNull(serializeToBytes(target).orElse(null)));
            final String n3 = (String) jsonLdUtils.unmarshallToNQuads(is);
            final InputStream n3Stream = new ByteArrayInputStream(Objects.requireNonNull(n3).getBytes());
            batch.put(uri, n3Stream);
        }
        h2client.joiningCompletableFuturePut(batch, contentTypeNTriples);
    }

    @Test
    void testPutAnnotations() throws Exception {
        ScbConfig scbConfig = getScbConfigWithAbsolutePath();
        final AnnotationBuilder ab = new AnnotationBuilder(scbConfig);
        final List<TemplateTarget> targetList = getTargetList();
        final List<TemplatePaintingAnnotation> annoList = ab.getAnnotationsWithDimensionedBodies(targetList);
        final List<TemplatePaintingAnnotation> sublist = annoList.subList(startIndex, toIndex);
        final Map<URI, InputStream> batch = new HashMap<>();
        for (TemplatePaintingAnnotation webAnno : sublist) {
            final IRI identifier = rdf.createIRI(webAnno.getAnnoId());
            final URI uri = new URI(identifier.getIRIString());
            System.out.println(
                    "Annotation " + webAnno.getAnnoId() + " for Image Resource " + webAnno.getBody().getResourceId()
                            + " created");
            final InputStream is = new ByteArrayInputStream(
                    Objects.requireNonNull(serializeToBytes(webAnno).orElse(null)));
            final String n3 = (String) jsonLdUtils.unmarshallToNQuads(is);
            final InputStream n3Stream = new ByteArrayInputStream(Objects.requireNonNull(n3).getBytes());
            batch.put(uri, n3Stream);
        }
        h2client.joiningCompletableFuturePut(batch, contentTypeNTriples);
    }

    @Test
    void testPutTaggingAnnotations() throws Exception {
        ScbConfig scbConfig = getScbConfigWithAbsolutePath();
        final TaggingAnnotationBuilder tab = new TaggingAnnotationBuilder(scbConfig);
        final List<TemplateTarget> targetList = getTargetList();
        final List<TemplateTaggingAnnotation> annoList = tab.buildTaggingAnnotations(targetList);
        final List<TemplateTaggingAnnotation> sublist = annoList.subList(startIndex, toIndex);
        final Map<URI, InputStream> batch = new HashMap<>();
        for (TemplateTaggingAnnotation webAnno : sublist) {
            final IRI identifier = rdf.createIRI(webAnno.getAnnoId());
            final URI uri = new URI(identifier.getIRIString());
            System.out.println(
                    "Annotation " + webAnno.getAnnoId() + " for Tag Body " + webAnno.getBody().getTagId() + " created");
            final InputStream is = new ByteArrayInputStream(
                    Objects.requireNonNull(serializeToBytes(webAnno).orElse(null)));
            final String n3 = (String) jsonLdUtils.unmarshallToNQuads(is);
            final InputStream n3Stream = new ByteArrayInputStream(Objects.requireNonNull(n3).getBytes());
            batch.put(uri, n3Stream);
        }
        h2client.joiningCompletableFuturePut(batch, contentTypeNTriples);
    }

    private List<TemplateTarget> getTargetList() {
        ScbConfig scbConfig = getScbConfigWithAbsolutePath();
        final TargetBuilder tb = new TargetBuilder(scbConfig);
        return tb.buildCanvases();
    }
}
