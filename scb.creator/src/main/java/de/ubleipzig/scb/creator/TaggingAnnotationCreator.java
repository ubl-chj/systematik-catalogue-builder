package de.ubleipzig.scb.creator;

import static de.ubleipzig.scb.creator.JsonSerializer.serializeToBytes;
import static org.apache.jena.arq.riot.WebContent.contentTypeNTriples;
import static org.slf4j.LoggerFactory.getLogger;

import de.ubleipzig.image.metadata.ImageMetadataServiceConfig;
import de.ubleipzig.scb.templates.TemplateTaggingAnnotation;
import de.ubleipzig.scb.templates.TemplateTarget;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
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
public final class TaggingAnnotationCreator extends AbstractResourceCreator {

    private static Logger logger = getLogger(ResourceCreator.class);
    private static Integer startIndex;
    private static Integer toIndex;
    private static JsonLdUtils jsonLdUtils = new JsonLdUtils();
    private ScbConfig scbConfig;
    private ImageMetadataServiceConfig imageMetadataServiceConfig;

    /**
     * ResourceCreator Class.
     */
    private TaggingAnnotationCreator(final ScbConfig scbConfig) {
        super();
        startIndex = 2000;
        toIndex = 3000;
        this.scbConfig = scbConfig;
        this.imageMetadataServiceConfig = scbConfig.getImageMetadataServiceConfig();;
    }

    private ScbConfig getScbConfig() {
        return this.scbConfig;
    }

    /**
     * main.
     *
     * @param args String[]
     * @throws Exception Exception
     */
    public static void main(final String[] args) throws Exception {
        final TaggingAnnotationCreator creator = new TaggingAnnotationCreator(buildScbConfig());
        final List<TemplateTarget> targetList = getTargetList(creator);
        putTaggingAnnotations(targetList);
        System.exit(1);
    }

    private static List<TemplateTarget> getTargetList(final TaggingAnnotationCreator creator) {
        final TargetBuilder tb = new TargetBuilder(creator.getScbConfig());
        return tb.buildCanvases();
    }

    private static void putTaggingAnnotations(final List<TemplateTarget> targetList) throws Exception {
        final TaggingAnnotationCreator creator = new TaggingAnnotationCreator(
                buildScbConfig());
        final TaggingAnnotationBuilder tab = new TaggingAnnotationBuilder(creator.getScbConfig());
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
