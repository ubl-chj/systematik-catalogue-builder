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

import static de.ubleipzig.scb.creator.internal.JsonSerializer.serializeToBytes;
import static org.apache.jena.arq.riot.WebContent.contentTypeNTriples;
import static org.slf4j.LoggerFactory.getLogger;

import de.ubleipzig.scb.creator.internal.JsonLdUtils;
import de.ubleipzig.scb.templates.TemplatePaintingAnnotation;
import de.ubleipzig.scb.templates.TemplateTaggingAnnotation;
import de.ubleipzig.scb.templates.TemplateTarget;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.rdf.api.IRI;
import org.slf4j.Logger;

/**
 * ResourceCreator.
 *
 * @author christopher-johnson
 */
public final class TaggingAnnotationCreator extends AbstractResourceCreator implements SystematikCatalogueBuilder {

    private static Logger logger = getLogger(ResourceCreator.class);
    private Integer fromIndex;
    private Integer toIndex;
    private static JsonLdUtils jsonLdUtils = new JsonLdUtils();
    private ScbConfig scbConfig;

    /**
     * TaggingAnnotationCreator Class.
     *
     * @param scbConfig scbConfig
     */
    public TaggingAnnotationCreator(final ScbConfig scbConfig) {
        super();
        this.scbConfig = scbConfig;
        this.fromIndex = scbConfig.getFromIndex();
        this.toIndex = scbConfig.getToIndex();
    }

    @Override
    public void run() {
        logger.info("Running TaggingAnnotationCreator...");
        final List<TemplateTarget> targetList = getTargetList();
        final Map<URI, InputStream> annotationBatch = buildAnnotationBatch(targetList);
        remote.joiningCompletableFuturePut(annotationBatch, contentTypeNTriples);
    }

    @Override
    public List<TemplateTarget> getTargetList() {
        final TargetBuilder tb = new TargetBuilder(scbConfig);
        return tb.buildCanvases();
    }

    @Override
    public List<TemplatePaintingAnnotation> getAnnotationList(final List<TemplateTarget> targetList) {
        return null;
    }

    @Override
    public Map<URI, InputStream> buildAnnotationBatch(final List<TemplateTarget> targetList) {
        final TaggingAnnotationBuilder tab = new TaggingAnnotationBuilder(scbConfig);
        final List<TemplateTaggingAnnotation> annoList = tab.buildTaggingAnnotations(targetList);
        final List<TemplateTaggingAnnotation> sublist = annoList.subList(fromIndex, toIndex);
        final Map<URI, InputStream> batch = new HashMap<>();
        try {
            for (TemplateTaggingAnnotation webAnno : sublist) {
                final IRI identifier = rdf.createIRI(webAnno.getAnnoId());
                final URI uri = new URI(identifier.getIRIString());
                logger.info("Annotation {} for Tag Body {} created", webAnno.getAnnoId(), webAnno.getBody().getTagId());
                final InputStream is = new ByteArrayInputStream(
                        Objects.requireNonNull(serializeToBytes(webAnno).orElse(null)));
                final String n3 = (String) jsonLdUtils.unmarshallToNQuads(is);
                final InputStream n3Stream = new ByteArrayInputStream(Objects.requireNonNull(n3).getBytes());
                batch.put(uri, n3Stream);
            }
            return batch;
        } catch (URISyntaxException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
