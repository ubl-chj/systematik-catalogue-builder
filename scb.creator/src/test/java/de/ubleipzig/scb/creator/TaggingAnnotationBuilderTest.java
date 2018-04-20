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

import static de.ubleipzig.scb.creator.JsonSerializer.serialize;

import de.ubleipzig.image.metadata.ImageMetadataServiceConfig;
import de.ubleipzig.scb.templates.TemplateTaggingAnnotation;
import de.ubleipzig.scb.templates.TemplateTarget;

import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

/**
 * AnnotationBuilderTest.
 *
 * @author christopher-johnson
 */
public class TaggingAnnotationBuilderTest extends CommonTests {


    @Test
    void testGetTaggingAnnotations() {
        final ImageMetadataServiceConfig imageMetadataServiceConfig = getImageMetadataGeneratorConfig();
        final ScbConfig scbConfig = getScbConfig();
        final TaggingAnnotationBuilder ab = new TaggingAnnotationBuilder(imageMetadataServiceConfig, scbConfig);
        final List<TemplateTarget> targetList = getTargetList();
        final List<TemplateTaggingAnnotation> annoList = ab.buildTaggingAnnotations(targetList);
        System.out.println(serialize(annoList.get(100156)).orElse(""));
        Assert.assertEquals(260512, annoList.size());
    }

    private List<TemplateTarget> getTargetList() {
        final ImageMetadataServiceConfig imageMetadataServiceConfig = getImageMetadataGeneratorConfig();
        final ScbConfig scbConfig = getScbConfig();
        final TaggingAnnotationBuilder tb = new TaggingAnnotationBuilder(imageMetadataServiceConfig, scbConfig);
        return tb.buildTaggingTargets();
    }
}