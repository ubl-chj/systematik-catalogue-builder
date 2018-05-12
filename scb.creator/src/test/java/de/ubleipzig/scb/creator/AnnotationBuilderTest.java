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

import de.ubleipzig.scb.templates.TemplatePaintingAnnotation;
import de.ubleipzig.scb.templates.TemplateTarget;

import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

/**
 * AnnotationBuilderTest.
 *
 * @author christopher-johnson
 */
public class AnnotationBuilderTest extends CommonTests {

    @Test
    void getAnnotationsWithDimensionedBodies() {
        final ScbConfig scbConfig = getScbConfigWithAbsolutePath();
        final AnnotationBuilder ab = new AnnotationBuilder(scbConfig);
        final List<TemplateTarget> targetList = getTargetList();
        final List<TemplatePaintingAnnotation> annoList = ab.getAnnotationsWithDimensionedBodies(targetList);
        System.out.println(serialize(annoList.get(1)).orElse(""));
        Assert.assertEquals(52218, annoList.size());
    }

    private List<TemplateTarget> getTargetList() {
        final ScbConfig scbConfig = getScbConfigWithAbsolutePath();
        final TargetBuilder tb = new TargetBuilder(scbConfig);
        return tb.buildCanvases();
    }
}
