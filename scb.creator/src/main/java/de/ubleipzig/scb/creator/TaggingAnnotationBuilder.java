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
import de.ubleipzig.image.metadata.ImageMetadataServiceConfig;
import de.ubleipzig.scb.creator.internal.UUIDType5;
import de.ubleipzig.scb.templates.TemplateMetadata;
import de.ubleipzig.scb.templates.TemplateTagBody;
import de.ubleipzig.scb.templates.TemplateTaggingAnnotation;
import de.ubleipzig.scb.templates.TemplateTarget;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;

/**
 * TaggingAnnotationBuilder.
 *
 * @author christopher-johnson
 */
public class TaggingAnnotationBuilder {

    private static Logger logger = getLogger(TaggingAnnotationBuilder.class);
    private final ImageMetadataServiceConfig imageMetadataServiceConfig;
    private final ScbConfig scbConfig;

    /**
     * TargetBuilder.
     *
     * @param scbConfig scbConfig
     */
    public TaggingAnnotationBuilder(final ScbConfig scbConfig) {
        this.imageMetadataServiceConfig = scbConfig.getImageMetadataServiceConfig();
        this.scbConfig = scbConfig;
    }

    /**
     * buildTaggingAnnotations.
     *
     * @param targetList targetList
     * @return a {@link List} of {@link TemplateTaggingAnnotation}
     */
    public List<TemplateTaggingAnnotation> buildTaggingAnnotations(final List<TemplateTarget> targetList) {
        final String annoContext = scbConfig.getAnnotationContainer();
        final String bodyContext = scbConfig.getTagBodyContainer();
        final String baseUrl = scbConfig.getBaseUrl();
        final List<String> contexts = new ArrayList<>();
        contexts.add(ANNO.CONTEXT);
        contexts.add(SC.CONTEXT);
        final List<TemplateTaggingAnnotation> annoList = new ArrayList<>();
        targetList.forEach(t -> {
            final List<TemplateMetadata> metadataList = t.getMetadata();
            metadataList.forEach(m -> {
                final String annoId = annoContext + t.getTargetId() + m.getMetadataValue();
                final UUID annoUUID = UUIDType5.nameUUIDFromNamespaceAndString(NAMESPACE_URL, annoId);
                final String identifier = baseUrl + annoContext + annoUUID;
                final TemplateTaggingAnnotation ta = new TemplateTaggingAnnotation();
                ta.setAnnoId(identifier);
                ta.setContext(contexts);
                final TemplateTagBody body = new TemplateTagBody();
                final String tagUUID = UUID.randomUUID().toString();
                final String tagIdentifier = baseUrl + bodyContext + tagUUID;
                body.setTagId(tagIdentifier);
                body.setTagPurpose(ANNO.tagging.getIRIString());
                body.setTagType(ANNO.TextualBody.getIRIString());
                body.setTagValue(m.getMetadataValue());
                ta.setTargetGroup(t.getTargetGroup());
                ta.setBody(body);
                ta.setTarget(t.getTargetId());
                annoList.add(ta);
                logger.debug("Adding Annotation {} to list", identifier);
            });
        });
        return annoList;
    }
}
