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

import de.ubleipzig.scb.templates.TemplateBody;
import de.ubleipzig.scb.templates.TemplateTarget;
import java.util.Iterator;
import java.util.List;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

/**
 * BodyBuilderTest.
 *
 * @author christopher-johnson
 */
public class BodyBuilderTest {

    private String baseUrl = "https://localhost:8445/collection/";
    private String imageSourceDir = "/media/christopher/OVAUBIMG/UBiMG/images/ubleipzig_sk2";
    private String metadataFile = "/sk2-titles-semester.tsv";
    private String imageServiceBaseUrl = "http://localhost:5000/iiif/";

    @Test
    void getBodies() {
        final Config config = new Config();
        config.setBaseUrl(baseUrl);
        config.setImageSourceDir(imageSourceDir);
        config.setMetadataFile(metadataFile);
        config.setImageServiceBaseUrl(imageServiceBaseUrl);
        final BodyBuilder bb = new BodyBuilder(config);
        final List<TemplateBody> annoList = bb.buildBodies();
        Assert.assertEquals(52218, annoList.size());
    }

    @Test
    void getBodiesWithDimensions() {
        final Config config = new Config();
        config.setBaseUrl(baseUrl);
        config.setImageSourceDir(imageSourceDir);
        config.setMetadataFile(metadataFile);
        config.setImageServiceBaseUrl(imageServiceBaseUrl);
        final BodyBuilder bb = new BodyBuilder(config);
        final TargetBuilder tb = new TargetBuilder(config);
        final List<TemplateTarget> targetList = tb.buildCanvases();
        final List<TemplateBody> bodyList = bb.buildBodies();
        final Iterator<TemplateBody> i1 = bodyList.iterator();
        final Iterator<TemplateTarget> i2 = targetList.iterator();
        while (i1.hasNext() && i2.hasNext()) {
            final TemplateBody body = i1.next();
            final TemplateTarget target = i2.next();
            body.setResourceHeight(target.getCanvasHeight());
            body.setResourceWidth(target.getCanvasWidth());
        }
        Assert.assertEquals(52218, bodyList.size());
    }
}
