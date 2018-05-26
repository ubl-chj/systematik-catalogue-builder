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

import org.junit.jupiter.api.Test;

public class ScbConfigTest extends CommonTests {

    @Test
    public void testScbConfiguration() {
        final ScbConfig config = getScbConfig();
        assertEquals("https://localhost:8445/", config.getBaseUrl());
        assertEquals("http://workspaces.ub.uni-leipzig.de:8182/iiif/2/", config.getImageServiceBaseUrl());
        assertEquals("http://iiif.io/api/image/2/context.json", config.getImageServiceType());
        assertEquals("collection/vp/anno/", config.getAnnotationContainer());
        assertEquals("collection/vp/target/", config.getTargetContainer());
        assertEquals("collection/vp/res/", config.getBodyContainer());
        assertEquals("collection/vp/tag/", config.getTagBodyContainer());
        assertEquals("/data/sk2-titles.csv", config.getMetadataFile());
        assertEquals("https://workspaces.ub.uni-leipzig.de:8445/collection/vp/meta/sk2-titles-semester.tsv",
                config.getMetadataRemoteLocation());
        assertEquals("/images", config.getImageMetadataServiceConfig().getImageSourceDir());
        assertEquals("/dimension-manifest-test-8efc742f-709e-47ea-a346-e7bdc3266b49.json",
                config.getImageMetadataServiceConfig().getDimensionManifestFilePath());
    }
}
