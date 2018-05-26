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

import static javax.ws.rs.core.HttpHeaders.LINK;
import static org.apache.jena.arq.riot.WebContent.contentTypeNTriples;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import org.apache.commons.rdf.api.IRI;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.trellisldp.client.ACLStatement;
import org.trellisldp.client.LdpClientException;
import org.trellisldp.vocabulary.ACL;
import org.trellisldp.vocabulary.LDP;

public class ResourceTransportTest extends CommonTests {

    private static Integer fromIndex;
    private static Integer toIndex;

    @BeforeAll
    static void initAll() {
        APP.before();
        baseUrl = "http://localhost:" + APP.getLocalPort() + "/";
        //baseUrl = "http://localhost:8000/";
        fromIndex = 0;
        toIndex = 10;
        h2client = getClient();
    }

    @AfterAll
    static void tearDownAll() {
        APP.after();
    }

    private static InputStream getTestResource() {
        return ResourceCreatorTest.class.getResourceAsStream("/data/empty.ttl");
    }

    private static InputStream getTestN3Resource() {
        return ResourceCreatorTest.class.getResourceAsStream("/data/webanno.complete.nt");
    }

    @BeforeEach()
    void init() {
        pid = "ldp-test-" + UUID.randomUUID().toString();
    }

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

    @Test
    void testPutMetadataResource() throws Exception {
        final ScbConfig scbConfig = getScbConfig();
        final String metadataFile = scbConfig.getMetadataFile();
        final IRI identifier = rdf.createIRI(baseUrl + "collection/vp/meta" + metadataFile);
        final InputStream is = ResourceCreatorTest.class.getResourceAsStream(metadataFile);
        h2client.put(identifier, is, "text/tab-separated-values");
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
            h2client.createDirectContainer(identifier, "resources", identifier);
        } catch (LdpClientException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testCreateBasicContainer() {
        try {
            final IRI identifier = rdf.createIRI(baseUrl + pid);
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
        final IRI accessTo = rdf.createIRI(baseUrl + pid);
        try {
            final IRI identifier = rdf.createIRI(baseUrl + pid + "?ext=acl");
            final ACLStatement acl = new ACLStatement(modes, agent, accessTo);
            h2client.put(identifier, new ByteArrayInputStream(acl.getACL().toByteArray()), contentTypeNTriples);
        } catch (LdpClientException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testJoiningCompletableFuturePut() throws Exception {
        try {
            final Map<URI, InputStream> map = new HashMap<>();
            final int loops = 100;
            for (int i = 0; i < loops; i++) {
                final String pid = "ldp-test-" + UUID.randomUUID().toString();
                final IRI identifier = rdf.createIRI(baseUrl + pid);
                final URI uri = new URI(identifier.getIRIString());
                final InputStream is = getTestN3Resource();
                map.put(uri, is);
            }
            h2client.joiningCompletableFuturePut(map, contentTypeNTriples);
        } catch (Exception ex) {
            throw new LdpClientException(ex.toString(), ex.getCause());
        }
    }
}
