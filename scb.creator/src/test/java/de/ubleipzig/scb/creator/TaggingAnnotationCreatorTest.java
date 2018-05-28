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

import de.ubleipzig.scb.templates.TemplateTaggingAnnotation;
import de.ubleipzig.scb.templates.TemplateTarget;

import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class TaggingAnnotationCreatorTest extends CommonTests {

    @Test
    void testRuntimeException() {
        final ScbConfig scbConfig = getScbConfigWithAbsolutePath("/scbconfig-test.yml");
        scbConfig.setFromIndex(0);
        scbConfig.setToIndex(2);
        final TaggingAnnotationCreator creator = new TaggingAnnotationCreator(scbConfig);
        final List<TemplateTarget> targetList = new ArrayList<>();
        final TemplateTarget target = new TemplateTarget();
        target.setTargetId("http://an.illegal.uri? blah");
        target.setCanvasLabel("whatever");
        for (int i = 0; i < 3; i++) {
            targetList.add(target);
        }
        scbConfig.setBaseUrl("http://an.illegal.uri? blah");
        assertThrows(RuntimeException.class, () -> creator.buildAnnotationBatch(targetList));
    }

    @Test
    void testException() {
        final ScbConfig scbConfig = getScbConfigWithAbsolutePath("/scbconfig-test.yml");
        scbConfig.setFromIndex(0);
        scbConfig.setToIndex(2);
        scbConfig.setBaseUrl("http://an.illegal.uri? blah");
        final TaggingAnnotationCreator creator = new TaggingAnnotationCreator(scbConfig);
        final Map<URI, InputStream> annotationBatch = new HashMap<>();
        assertThrows(RuntimeException.class, () -> creator.putToRemote(annotationBatch));
    }

    @Test
    void testGetAnnotations() {
        final ScbConfig scbConfig = getScbConfigWithAbsolutePath("/scbconfig-test.yml");
        scbConfig.setFromIndex(0);
        scbConfig.setToIndex(2);
        final TaggingAnnotationCreator creator = new TaggingAnnotationCreator(scbConfig);
        final List<TemplateTarget> targetList = creator.getTargetList();
        final List<TemplateTaggingAnnotation> taglist = creator.getAnnotationList(targetList);
        assertEquals(305688, taglist.size());
    }

}
