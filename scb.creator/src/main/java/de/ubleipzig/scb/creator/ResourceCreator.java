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

import de.ubleipzig.image.metadata.ImageMetadataServiceConfig;
import de.ubleipzig.scb.creator.internal.JsonLdUtils;
import de.ubleipzig.scb.templates.TemplatePaintingAnnotation;
import de.ubleipzig.scb.templates.TemplateTarget;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.rdf.api.IRI;
import org.slf4j.Logger;
import org.trellisldp.client.LdpClientException;

/**
 * ResourceCreator.
 *
 * @author christopher-johnson
 */
public final class ResourceCreator extends AbstractResourceCreator implements SystematikCatalogueBuilder {

    private static Logger logger = getLogger(ResourceCreator.class);
    private Integer fromIndex;
    private Integer toIndex;
    private JsonLdUtils jsonLdUtils = new JsonLdUtils();
    private ScbConfig scbConfig;
    private ImageMetadataServiceConfig imageMetadataServiceConfig;

    /**
     * ResourceCreator Class.
     *
     * @param scbConfig scbConfig
     */
    public ResourceCreator(final ScbConfig scbConfig) {
        super();
        this.scbConfig = scbConfig;
        this.imageMetadataServiceConfig = scbConfig.getImageMetadataServiceConfig();
        this.fromIndex = scbConfig.getFromIndex();
        this.toIndex = scbConfig.getToIndex();
    }

    @Override
    public void run() {
        logger.info("Running ResourceCreator...");
        final String baseUrl = scbConfig.getBaseUrl();
        final Map<URI, InputStream> imageBatch = buildImageResourceBatchFromSubList();
        try {
            remote.joiningCompletableFuturePut(imageBatch, "image/tiff", baseUrl);
        } catch (LdpClientException e) {
            throw new RuntimeException(e.getMessage());
        }
        final List<TemplateTarget> targetList = getTargetList();
        final Map<URI, InputStream> canvasBatch = buildCanvasBatch(targetList);
        try {
            remote.joiningCompletableFuturePut(canvasBatch, contentTypeNTriples, baseUrl);
        } catch (LdpClientException e) {
            throw new RuntimeException(e.getMessage());
        }
        final Map<URI, InputStream> annotationBatch = buildAnnotationBatch(targetList);
        try {
            remote.joiningCompletableFuturePut(annotationBatch, contentTypeNTriples, baseUrl);
        } catch (LdpClientException e) {
            throw new RuntimeException(e.getMessage());
        }
        logger.info("Process Complete: Exiting");
    }

    /**
     * putImageResourceBatchFromSubList.
     *
     * @return Map
     */
    public Map<URI, InputStream> buildImageResourceBatchFromSubList() {
        final ImageMetadataImpl im = new ImageMetadataImpl(imageMetadataServiceConfig);
        final List<File> files = im.getFiles();
        files.sort(Comparator.naturalOrder());
        final List<File> sublist = files.subList(fromIndex, toIndex);
        final Map<URI, InputStream> batch = new HashMap<>();
        try {
            for (File file : sublist) {
                final IRI identifier = rdf.createIRI(
                        scbConfig.getBaseUrl() + scbConfig.getBodyContainer() + file.getName().toLowerCase());
                final URI uri = new URI(identifier.getIRIString());
                final InputStream is = new FileInputStream(file);
                batch.put(uri, is);
            }
            return batch;
        } catch (URISyntaxException | FileNotFoundException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<TemplateTarget> getTargetList() {
        final TargetBuilder tb = new TargetBuilder(scbConfig);
        return tb.buildCanvases();
    }

    @Override
    public List<TemplatePaintingAnnotation> getAnnotationList(final List<TemplateTarget> targetList) {
        final AnnotationBuilder ab = new AnnotationBuilder(scbConfig);
        return ab.getAnnotationsWithDimensionedBodies(targetList);
    }

    /**
     * putCanvases.
     *
     * @param targetList targetList
     * @return Map
     */
    public Map<URI, InputStream> buildCanvasBatch(final List<TemplateTarget> targetList) {
        targetList.sort(Comparator.comparing(TemplateTarget::getCanvasLabel));
        final List<TemplateTarget> sublist = targetList.subList(fromIndex, toIndex);
        final Map<URI, InputStream> batch = new HashMap<>();
        try {
            for (TemplateTarget target : sublist) {
                final IRI identifier = rdf.createIRI(target.getTargetId());
                final URI uri = new URI(identifier.getIRIString());
                final InputStream is = new ByteArrayInputStream(
                        Objects.requireNonNull(serializeToBytes(target).orElse(null)));
                final String n3 = (String) jsonLdUtils.unmarshallToNQuads(is);
                final InputStream n3Stream = new ByteArrayInputStream(Objects.requireNonNull(n3).getBytes());
                batch.put(uri, n3Stream);
            }
            return batch;
        } catch (URISyntaxException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Map<URI, InputStream> buildAnnotationBatch(final List<TemplateTarget> targetList) {
        final List<TemplatePaintingAnnotation> annoList = getAnnotationList(targetList);
        final List<TemplatePaintingAnnotation> sublist = annoList.subList(fromIndex, toIndex);
        final Map<URI, InputStream> batch = new HashMap<>();
        try {
            for (TemplatePaintingAnnotation webAnno : sublist) {
                final IRI identifier = rdf.createIRI(webAnno.getAnnoId());
                final URI uri = new URI(identifier.getIRIString());
                logger.info("Annotation {} for Image Resource {} created: ", webAnno.getAnnoId(),
                        webAnno.getBody().getResourceId());
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
