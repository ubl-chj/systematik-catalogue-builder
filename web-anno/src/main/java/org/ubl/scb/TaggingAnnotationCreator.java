package org.ubl.scb;

import static org.apache.jena.riot.WebContent.contentTypeNTriples;
import static org.slf4j.LoggerFactory.getLogger;
import static org.ubl.scb.JsonSerializer.serializeToBytes;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.rdf.api.IRI;
import org.slf4j.Logger;
import org.ubl.image.metadata.ImageMetadataGeneratorConfig;
import org.ubl.scb.templates.TemplateTaggingAnnotation;
import org.ubl.scb.templates.TemplateTarget;

/**
 * ResourceCreator.
 *
 * @author christopher-johnson
 */
public final class TaggingAnnotationCreator extends AbstractResourceCreator {

    private static Logger logger = getLogger(ResourceCreator.class);
    private static Integer startIndex;
    private static Integer toIndex;
    private static JsonLdUtils jsonLdUtils = new JsonLdUtils();
    private ScbConfig scbConfig;
    private ImageMetadataGeneratorConfig imageMetadataGeneratorConfig;

    /**
     * ResourceCreator Class.
     */
    private TaggingAnnotationCreator(final ScbConfig scbConfig, final ImageMetadataGeneratorConfig
            imageMetadataGeneratorConfig) {
        super();
        startIndex = 2000;
        toIndex = 3000;
        this.scbConfig = scbConfig;
        this.imageMetadataGeneratorConfig = imageMetadataGeneratorConfig;
    }

    private ScbConfig getScbConfig() {
        return this.scbConfig;
    }

    private ImageMetadataGeneratorConfig getImageMetadataGeneratorConfig() {
        return this.imageMetadataGeneratorConfig;
    }

    /**
     * main.
     *
     * @param args String[]
     * @throws Exception Exception
     */
    public static void main(final String[] args) throws Exception {
        final TaggingAnnotationCreator creator = new TaggingAnnotationCreator(buildScbConfig(), buildImageMetadataGeneratorConfig());
        final List<TemplateTarget> targetList = getTargetList(creator);
        putTaggingAnnotations(targetList);
        System.exit(1);
    }

    private static List<TemplateTarget> getTargetList(final TaggingAnnotationCreator creator) {
        final TargetBuilder tb = new TargetBuilder(creator.getImageMetadataGeneratorConfig(), creator.getScbConfig());
        return tb.buildCanvases();
    }

    private static void putTaggingAnnotations(final List<TemplateTarget> targetList) throws Exception {
        final TaggingAnnotationCreator creator = new TaggingAnnotationCreator(buildScbConfig(), buildImageMetadataGeneratorConfig());
        final TaggingAnnotationBuilder tab = new TaggingAnnotationBuilder(
                creator.getImageMetadataGeneratorConfig(), creator.getScbConfig());
        final List<TemplateTaggingAnnotation> annoList = tab.buildTaggingAnnotations(targetList);
        final List<TemplateTaggingAnnotation> sublist = annoList.subList(startIndex, toIndex);
        final Map<URI, InputStream> batch = new HashMap<>();
        for (TemplateTaggingAnnotation webAnno : sublist) {
            final IRI identifier = rdf.createIRI(webAnno.getAnnoId());
            final URI uri = new URI(identifier.getIRIString());
            logger.info(
                    "Annotation {} for Tag Body {} created", webAnno.getAnnoId(), webAnno.getBody()
                                                                                         .getTagId());
            final InputStream is = new ByteArrayInputStream(
                    Objects.requireNonNull(serializeToBytes(webAnno).orElse(null)));
            final String n3 = (String) jsonLdUtils.unmarshallToNQuads(is);
            final InputStream n3Stream = new ByteArrayInputStream(Objects.requireNonNull(n3)
                                                                         .getBytes());
            batch.put(uri, n3Stream);
        }
        remote.joiningCompletableFuturePut(batch, contentTypeNTriples);
    }

}
