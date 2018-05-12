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

import static io.dropwizard.testing.ConfigOverride.config;
import static io.dropwizard.testing.ResourceHelpers.resourceFilePath;
import static javax.ws.rs.core.HttpHeaders.LINK;
import static org.apache.jena.arq.riot.WebContent.contentTypeNTriples;
import static org.junit.jupiter.api.Assertions.assertEquals;

import de.ubleipzig.scb.templates.TemplateTarget;

import io.dropwizard.testing.DropwizardTestSupport;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    private static Integer fromIndex;
    private static Integer toIndex;

    @BeforeAll
    static void initAll() {
        APP.before();
        fromIndex = 0;
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

    @Disabled
    @Test
    void testCreateDefaultContainers() {
        final IRI identifier = rdf.createIRI(baseUrl);
        final IRI collectionBase = rdf.createIRI(baseUrl + "collection");
        final IRI collectionId = rdf.createIRI(baseUrl + "collection/vp");
        try {
            h2client.createDirectContainer(identifier, "collection", identifier);
            h2client.createDirectContainer(collectionBase, "vp", collectionBase);
            h2client.createDirectContainer(collectionId, "res", collectionId);
            h2client.createDirectContainer(collectionId, "target", collectionId);
            h2client.createDirectContainer(collectionId, "body", collectionId);
            h2client.createDirectContainer(collectionId, "tag", collectionId);
            h2client.createDirectContainer(collectionId, "anno", collectionId);
            h2client.createDirectContainer(collectionId, "meta", collectionId);
        } catch (LdpClientException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testPutDimensionManifestResource() throws Exception {
        final ScbConfig scbConfig = getScbConfig();
        final String dimensionManifestFilePath = scbConfig.getImageMetadataServiceConfig()
                .getDimensionManifestFilePath();
        final IRI identifier = rdf.createIRI(baseUrl + "collection/vp/meta" + dimensionManifestFilePath);
        final InputStream is = ResourceCreatorTest.class.getResourceAsStream(dimensionManifestFilePath);
        h2client.put(identifier, is, "application/json");
    }

    @Disabled
    @Test
    void testPutMetadataResource() throws Exception {
        final ScbConfig scbConfig = getScbConfig();
        final String metadataFile = scbConfig.getMetadataFile();
        final IRI identifier = rdf.createIRI(baseUrl + "collection/vp/meta" + metadataFile);
        final InputStream is = ResourceCreatorTest.class.getResourceAsStream(metadataFile);
        h2client.put(identifier, is, "text/tab-separated-values");
    }

    @Test
    void testBuildImageResourceBatchFromSubList() {
        final ScbConfig scbConfig = getScbConfigWithAbsolutePath();
        scbConfig.setFromIndex(0);
        scbConfig.setToIndex(3);
        final ResourceCreator creator = new ResourceCreator(scbConfig);
        final Map<URI, InputStream> imageBatch = creator.buildImageResourceBatchFromSubList();
        assertEquals(3, imageBatch.size());
        assertEquals(
                "https://localhost:8445/collection/vp/res/00000003.jpg", Objects.requireNonNull(
                        imageBatch.entrySet().stream().map(Map.Entry::getKey).findFirst().orElse(null)).toString());
    }


    @Test
    void testBuildCanvasBatch() {
        final ScbConfig scbConfig = getScbConfigWithAbsolutePath();
        scbConfig.setFromIndex(0);
        scbConfig.setToIndex(50);
        final ResourceCreator creator = new ResourceCreator(scbConfig);
        final List<TemplateTarget> targetList = creator.getTargetList();
        final Map<URI, InputStream> canvasBatch = creator.buildCanvasBatch(targetList);
        assertEquals(50, canvasBatch.size());
    }


    @Test
    void testBuildAnnotationBatch() {
        final ScbConfig scbConfig = getScbConfigWithAbsolutePath();
        scbConfig.setFromIndex(0);
        scbConfig.setToIndex(50);
        final ResourceCreator creator = new ResourceCreator(scbConfig);
        final List<TemplateTarget> targetList = creator.getTargetList();
        final Map<URI, InputStream> annotationBatch = creator.buildAnnotationBatch(targetList);
        assertEquals(50, annotationBatch.size());

    }

    @Test
    void testPutTaggingAnnotations() {
        final ScbConfig scbConfig = getScbConfigWithAbsolutePath();
        scbConfig.setFromIndex(0);
        scbConfig.setToIndex(50);
        scbConfig.setBuilderType("tagging");
        final TaggingAnnotationCreator creator = new TaggingAnnotationCreator(scbConfig);
        final List<TemplateTarget> targetList = creator.getTargetList();
        final Map<URI, InputStream> annotationBatch = creator.buildAnnotationBatch(targetList);
        assertEquals(50, annotationBatch.size());
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

    @Disabled
    @Test
    void testCreateDirectContainer() {
        try {
            final IRI identifier = rdf.createIRI(baseUrl + "collection/vp");
            h2client.createDirectContainer(identifier, "resources", identifier);
        } catch (LdpClientException e) {
            e.printStackTrace();
        }
    }

    @Disabled
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

    @Disabled
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
}
