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

import static de.ubleipzig.scb.creator.internal.UUIDType5.NAMESPACE_URL;
import static org.slf4j.LoggerFactory.getLogger;

import de.ubleipzig.iiif.vocabulary.ANNO;
import de.ubleipzig.iiif.vocabulary.SC;
import de.ubleipzig.scb.creator.internal.UUIDType5;
import de.ubleipzig.scb.templates.TemplateBody;
import de.ubleipzig.scb.templates.TemplatePaintingAnnotation;
import de.ubleipzig.scb.templates.TemplateTarget;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;

/**
 * AnnotationBuilder.
 *
 * @author christopher-johnson
 */
public class AnnotationBuilder {

    private static Logger log = getLogger(AnnotationBuilder.class);
    private final ScbConfig scbConfig;

    /**
     * AnnotationBuilder.
     *
     * @param scbConfig scbConfig
     */
    public AnnotationBuilder(final ScbConfig scbConfig) {
        this.scbConfig = scbConfig;
    }

    /**
     * buildAnnotations.
     *
     * @param targetList targetList
     * @return a {@link List} of {@link TemplatePaintingAnnotation}
     */
    public List<TemplatePaintingAnnotation> buildAnnotations(final List<TemplateTarget> targetList) {
        final String annoContext = scbConfig.getAnnotationContainer();
        final String baseUrl = scbConfig.getBaseUrl();
        final List<String> contexts = new ArrayList<>();
        contexts.add(ANNO.CONTEXT);
        contexts.add(SC.CONTEXT);
        final List<TemplatePaintingAnnotation> annoList = new ArrayList<>();
        for (TemplateTarget target : targetList) {
            final String annoId = annoContext + target.getTargetId();
            final UUID annoUUID = UUIDType5.nameUUIDFromNamespaceAndString(NAMESPACE_URL, annoId);
            final String identifier = baseUrl + annoContext + annoUUID;
            final TemplatePaintingAnnotation ta = new TemplatePaintingAnnotation();
            ta.setId(identifier);
            ta.setContext(contexts);
            ta.setTarget(target.getTargetId());
            annoList.add(ta);
            log.debug("Adding Annotation {} to list", identifier);
        }
        return annoList;
    }

    /**
     * getAnnotationsWithDimensionedBodies.
     *
     * @param targetList targetList
     * @return a {@link List} of {@link TemplatePaintingAnnotation}
     */
    public List<TemplatePaintingAnnotation> getAnnotationsWithDimensionedBodies(final List<TemplateTarget> targetList) {
        final BodyBuilder bb = new BodyBuilder(scbConfig);
        final List<TemplatePaintingAnnotation> annoList = buildAnnotations(targetList);
        final List<TemplateBody> bodyList = bb.getBodiesWithDimensions(targetList);
        final Iterator<TemplatePaintingAnnotation> i1 = annoList.iterator();
        final Iterator<TemplateBody> i2 = bodyList.iterator();
        final List<TemplatePaintingAnnotation> webAnnoList = new ArrayList<>();
        while (i1.hasNext() && i2.hasNext()) {
            final TemplatePaintingAnnotation webAnno = i1.next();
            final TemplateBody body = i2.next();
            webAnno.setBody(body);
            webAnnoList.add(webAnno);
        }
        return webAnnoList;
    }
}
