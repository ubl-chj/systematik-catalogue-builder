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

import de.ubleipzig.scb.templates.TemplatePaintingAnnotation;
import de.ubleipzig.scb.templates.TemplateTarget;

import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.Map;

public interface SystematikCatalogueBuilder {

    /**
     * run.
     */
    void run();

    /**
     * getTargetList.
     *
     * @return List
     */
    List<TemplateTarget> getTargetList();

    /**
     * getAnnotationList.
     *
     * @param targetList List
     * @return List
     */
    List<TemplatePaintingAnnotation> getAnnotationList(final List<TemplateTarget> targetList);

    /**
     * putAnnotations.
     *
     * @param targetList List
     * @return Map
     */
    Map<URI, InputStream> buildAnnotationBatch(final List<TemplateTarget> targetList);
}