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

import static org.ubl.scb.JSONSerializer.serialize;

import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.ubl.image.metadata.ImageMetadataGeneratorConfig;
import org.ubl.scb.BodyBuilder;
import org.ubl.scb.ScbConfig;
import org.ubl.scb.templates.TemplateBody;

/**
 * BodyBuilderTest.
 *
 */
public class BodyBuilderTest extends CommonTests {

    @Test
    void getBodiesWithDimensions() {
        final ImageMetadataGeneratorConfig imageMetadataGeneratorConfig = new ImageMetadataGeneratorConfig();
        final ScbConfig scbConfig = new ScbConfig();
        scbConfig.setBaseUrl(baseUrl);
        imageMetadataGeneratorConfig.setImageSourceDir(imageSourceDir);
        scbConfig.setMetadataFile(metadataFile);
        scbConfig.setBodyContext(bodyContext);
        scbConfig.setImageServiceBaseUrl(imageServiceBaseUrl);
        scbConfig.setImageServiceType(imageServiceType);
        imageMetadataGeneratorConfig.setDimensionManifestFilePath(BodyBuilder.class.getResource(
                dimensionManifestFile).getPath());
        final BodyBuilder bb = new BodyBuilder(imageMetadataGeneratorConfig, scbConfig);
        final List<TemplateBody> bodyList = bb.getBodiesWithDimensions();
        System.out.println(serialize(bodyList.get(1)).orElse(""));
        Assert.assertEquals(52218, bodyList.size());
    }
}
