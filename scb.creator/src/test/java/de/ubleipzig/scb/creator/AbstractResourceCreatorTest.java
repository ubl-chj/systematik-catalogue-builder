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

import static de.ubleipzig.scb.creator.AbstractResourceCreator.getDimensionManifestRemoteLocation;
import static de.ubleipzig.scb.creator.AbstractResourceCreator.getMetadataRemoteLocation;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.InputStream;
import java.util.UUID;

import org.apache.commons.rdf.api.IRI;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.trellisldp.client.LdpClientException;

public class AbstractResourceCreatorTest extends CommonTests {
    @BeforeAll
    static void initAll() {
        APP.before();
        baseUrl = "http://localhost:" + APP.getLocalPort() + "/";
        h2client = getClient();
    }

    @BeforeEach()
    void init() {
        pid = "ldp-test-" + UUID.randomUUID().toString();
    }

    @Test
    void testGetDimensionManifestRemoteLocation() throws LdpClientException {
        final InputStream is = getDimensionManifest();
        final IRI identifier = rdf.createIRI(baseUrl + pid);
        h2client.put(identifier, is, "application/json");
        final String dimensionManifest = getDimensionManifestRemoteLocation(identifier.getIRIString());
        assertTrue(dimensionManifest.contains("height"));
    }

    @Test
    void testGetMetadataRemoteLocation() throws LdpClientException {
        final InputStream is = getDimensionManifest();
        final IRI identifier = rdf.createIRI(baseUrl + pid);
        h2client.put(identifier, is, "application/json");
        final InputStream metadata = getMetadataRemoteLocation(identifier.getIRIString());
        final String m = read(metadata);
        assertTrue(m.contains("height"));
    }

    @Test
    void testGetRemoteException() {
        assertThrows(RuntimeException.class, () -> getDimensionManifestRemoteLocation("httq://invalid.org"));
        assertThrows(RuntimeException.class, () -> getMetadataRemoteLocation("httq://invalid.org"));
        final RemoteResource remote = new RemoteResource();
        assertThrows(LdpClientException.class, () -> remote.getRemoteBinaryResource("httq://invalid.org"));
    }
}
