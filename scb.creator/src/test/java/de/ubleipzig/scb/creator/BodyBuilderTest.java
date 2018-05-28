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

import de.ubleipzig.scb.templates.TemplateBody;
import de.ubleipzig.scb.templates.TemplateTarget;

import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

/**
 * BodyBuilderTest.
 */
public class BodyBuilderTest extends CommonTests {

    @Test
    void getBodiesWithDimensions() {
        final ScbConfig scbConfig = getScbConfigWithAbsolutePath("/scbconfig-test.yml");
        final BodyBuilder bb = new BodyBuilder(scbConfig);
        final List<TemplateTarget> targetList = getTargetList();
        final List<TemplateBody> bodyList = bb.getBodiesWithDimensions(targetList);
        System.out.println(serialize(bodyList.get(1)).orElse(""));
        Assert.assertEquals(52218, bodyList.size());
    }

    private List<TemplateTarget> getTargetList() {
        final ScbConfig scbConfig = getScbConfigWithAbsolutePath("/scbconfig-test.yml");
        final TargetBuilder tb = new TargetBuilder(scbConfig);
        return tb.buildCanvases();
    }
}
