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

package org.ubl.scb;

import static org.slf4j.LoggerFactory.getLogger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.ubl.image.metadata.ImageMetadataGeneratorConfig;
import org.ubl.scb.templates.TemplateBody;
import org.ubl.scb.templates.TemplateTarget;
import org.ubl.scb.templates.TemplateWebAnnotation;
import org.ubl.scb.vocabulary.ANNO;
import org.ubl.scb.vocabulary.SC;

/**
 * AnnotationBuilder.
 *
 * @author christopher-johnson
 */
public class AnnotationBuilder {

    private static Logger log = getLogger(TargetBuilder.class);
    private final ImageMetadataGeneratorConfig imageMetadataGeneratorConfig;
    private final ScbConfig scbConfig;

    /**
     * AnnotationBuilder.
     *
     * @param imageMetadataGeneratorConfig imageMetadataGeneratorConfig
     * @param scbConfig scbConfig
     */
    public AnnotationBuilder(final ImageMetadataGeneratorConfig imageMetadataGeneratorConfig, final ScbConfig
            scbConfig) {
        this.imageMetadataGeneratorConfig = imageMetadataGeneratorConfig;
        this.scbConfig = scbConfig;
    }

    /**
     * buildAnnotations.
     *
     * @param targetList targetList
     * @return a {@link List} of {@link TemplateWebAnnotation}
     */
    public List<TemplateWebAnnotation> buildAnnotations(final List<TemplateTarget> targetList) {
        final String annoContext = scbConfig.getAnnotationContainer();
        final List<String> contexts = new ArrayList<>();
        contexts.add(ANNO.CONTEXT);
        contexts.add(SC.CONTEXT);
        final List<TemplateWebAnnotation> annoList = new ArrayList<>();
        for (TemplateTarget target : targetList) {
            final String annoUUID = UUID.randomUUID().toString();
            final String identifier = scbConfig.getBaseUrl() + annoContext + annoUUID;
            final TemplateWebAnnotation ta = new TemplateWebAnnotation();
            ta.setId(identifier);
            ta.setContext(contexts);
            ta.setTarget(target.getCanvasId());
            annoList.add(ta);
            log.debug("Adding Annotation {} to list", identifier);
        }
        return annoList;
    }

    /**
     * getAnnotationsWithDimensionedBodies.
     *
     * @param targetList targetList
     * @return a {@link List} of {@link TemplateWebAnnotation}
     */
    public List<TemplateWebAnnotation> getAnnotationsWithDimensionedBodies(final List<TemplateTarget> targetList) {
        final BodyBuilder bb = new BodyBuilder(imageMetadataGeneratorConfig, scbConfig);
        final List<TemplateWebAnnotation> annoList = buildAnnotations(targetList);
        final List<TemplateBody> bodyList = bb.getBodiesWithDimensions(targetList);
        final Iterator<TemplateWebAnnotation> i1 = annoList.iterator();
        final Iterator<TemplateBody> i2 = bodyList.iterator();
        final List<TemplateWebAnnotation> webAnnoList = new ArrayList<>();
        while (i1.hasNext() && i2.hasNext()) {
            final TemplateWebAnnotation webAnno = i1.next();
            final TemplateBody body = i2.next();
            webAnno.setBody(body);
            webAnnoList.add(webAnno);
        }
        return webAnnoList;
    }
}
