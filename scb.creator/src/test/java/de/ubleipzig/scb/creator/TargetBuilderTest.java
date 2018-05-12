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
import static org.junit.Assert.assertEquals;

import de.ubleipzig.scb.templates.TemplateTarget;

import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * TargetBuilderTest.
 *
 * @author christopher-johnson
 */
public class TargetBuilderTest extends CommonTests {

    @Test
    void getTargets() {
        ScbConfig scbConfig = getScbConfigWithAbsolutePath();
        final TargetBuilder tb = new TargetBuilder(scbConfig);
        final List<TemplateTarget> targetList = tb.buildCanvases();
        System.out.println(serialize(targetList.get(49000)).orElse(""));
        assertEquals(52218, targetList.size());
    }
}
