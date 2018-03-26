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

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.ubl.image.metadata.ImageMetadataGeneratorConfig;
import org.ubl.scb.ScbConfig;
import org.ubl.scb.TargetBuilder;
import org.ubl.scb.templates.TemplateTarget;

/**
 * TargetBuilderTest.
 *
 * @author christopher-johnson
 */
public class TargetBuilderTest {

    private String baseUrl = "https://localhost:8445/collection/";
    private String imageSourceDir = "/media/christopher/OVAUBIMG/UBiMG/images/ubleipzig_sk2";
    private String metadataFile = "/sk2-titles-semester.tsv";

    @Test
    void getTargets() {
        final ImageMetadataGeneratorConfig imageMetadataGeneratorConfig = new ImageMetadataGeneratorConfig();
        final ScbConfig scbConfig = new ScbConfig();
        scbConfig.setBaseUrl(baseUrl);
        imageMetadataGeneratorConfig.setImageSourceDir(imageSourceDir);
        scbConfig.setMetadataFile(metadataFile);
        final TargetBuilder cb = new TargetBuilder(imageMetadataGeneratorConfig, scbConfig);
        final List<TemplateTarget> targetList = cb.buildCanvases();
        assertEquals(52218, targetList.size());
    }
}