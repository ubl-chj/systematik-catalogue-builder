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

import de.ubleipzig.scb.templates.TemplateTarget;

import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.junit.jupiter.api.Test;

/**
 * ResourceCreatorTest.
 *
 * @author christopher-johnson
 */
public class ResourceCreatorTest extends CommonTests {

    @Test
    void testBuildImageResourceBatchFromSubList() {
        final ScbConfig scbConfig = getScbConfigWithAbsolutePath();
        scbConfig.setFromIndex(0);
        scbConfig.setToIndex(3);
        final ResourceCreator creator = new ResourceCreator(scbConfig);
        final Map<URI, InputStream> imageBatch = creator.buildImageResourceBatchFromSubList();
        assertEquals(3, imageBatch.size());
        assertEquals("http://localhost:8445/collection/vp/res/00000001.jpg", Objects.requireNonNull(
                imageBatch.entrySet().stream().map(Map.Entry::getKey).findFirst().orElse(null)).toString());
    }


    @Test
    void testBuildCanvasBatch() {
        final ScbConfig scbConfig = getScbConfigWithAbsolutePath();
        scbConfig.setFromIndex(0);
        scbConfig.setToIndex(50);
        final ResourceCreator creator = new ResourceCreator(scbConfig);
        final List<TemplateTarget> targetList = creator.getTargetList();
        final Map<URI, InputStream> canvasBatch = creator.buildCanvasBatch(targetList);
        assertEquals(50, canvasBatch.size());
    }


    @Test
    void testBuildAnnotationBatch() {
        final ScbConfig scbConfig = getScbConfigWithAbsolutePath();
        scbConfig.setFromIndex(0);
        scbConfig.setToIndex(50);
        final ResourceCreator creator = new ResourceCreator(scbConfig);
        final List<TemplateTarget> targetList = creator.getTargetList();
        final Map<URI, InputStream> annotationBatch = creator.buildAnnotationBatch(targetList);
        assertEquals(50, annotationBatch.size());

    }

    @Test
    void testBuildTaggingAnnotationBatch() {
        final ScbConfig scbConfig = getScbConfigWithAbsolutePath();
        scbConfig.setFromIndex(0);
        scbConfig.setToIndex(50);
        scbConfig.setBuilderType("tagging");
        final TaggingAnnotationCreator creator = new TaggingAnnotationCreator(scbConfig);
        final List<TemplateTarget> targetList = creator.getTargetList();
        final Map<URI, InputStream> annotationBatch = creator.buildAnnotationBatch(targetList);
        assertEquals(50, annotationBatch.size());
    }
}
