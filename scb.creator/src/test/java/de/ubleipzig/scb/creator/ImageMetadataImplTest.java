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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import de.ubleipzig.image.metadata.ImageMetadataServiceConfig;
import de.ubleipzig.image.metadata.templates.ImageDimensions;

import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import org.apache.commons.rdf.api.IRI;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.trellisldp.client.LdpClientException;

public class ImageMetadataImplTest extends CommonTests {
    @BeforeAll
    static void initAll() {
        APP.before();
        baseUrl = "http://localhost:" + APP.getLocalPort() + "/";
        h2client = getClient();
    }

    @Test
    void testGetFromRemote() throws LdpClientException {
        final String pid = "imTest" + UUID.randomUUID().toString();
        final ImageMetadataServiceConfig imConfig = new ImageMetadataServiceConfig();
        imConfig.setDimensionManifestFilePath(baseUrl + pid);
        final InputStream is = getDimensionManifest();
        final IRI identifier = rdf.createIRI(baseUrl + pid);
        h2client.put(identifier, is, "application/json");
        final ImageMetadataImpl im = new ImageMetadataImpl(imConfig);
        final List<String> names = im.getFileNamesFromRemote();
        final List<ImageDimensions> dims = im.getDimensions();
        assertEquals("40JOgwENmF5adEwvWeVKuDKVksY=", dims.get(2).getDigest());
        assertEquals(52218, names.size());
    }

    @Test
    void testIOException() {
        final ImageMetadataServiceConfig imConfig = new ImageMetadataServiceConfig();
        imConfig.setImageSourceDir("/a-non-existing-path");
        final ImageMetadataImpl im = new ImageMetadataImpl(imConfig);
        assertThrows(RuntimeException.class, () -> im.getFiles());
    }
}
