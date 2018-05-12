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

import static de.ubleipzig.scb.creator.internal.JsonSerializer.serialize;
import static org.junit.jupiter.api.Assertions.assertEquals;

import de.ubleipzig.image.metadata.ImageMetadataService;
import de.ubleipzig.image.metadata.ImageMetadataServiceConfig;
import de.ubleipzig.image.metadata.ImageMetadataServiceImpl;
import de.ubleipzig.image.metadata.templates.ImageDimensions;

import java.util.List;

import org.junit.jupiter.api.Test;

public class DeserializeDimensionManifestTest extends CommonTests {

    @Test
    void testDeserializeDimensionManifest() {
        final ScbConfig scbConfig = getScbConfigWithAbsolutePath();
        final ImageMetadataServiceConfig imageMetadataServiceConfig = scbConfig.getImageMetadataServiceConfig();
        final ImageMetadataService generator = new ImageMetadataServiceImpl(imageMetadataServiceConfig);
        final List<ImageDimensions> dimList = generator.unmarshallDimensionManifestFromFile();
        System.out.println(serialize(dimList.get(1)).orElse(""));
        assertEquals(52218, dimList.size());
    }

}
