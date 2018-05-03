package de.ubleipzig.scb.creator;

import static de.ubleipzig.scb.creator.JsonSerializer.serializeToBytes;
import static org.apache.jena.arq.riot.WebContent.contentTypeNTriples;
import static org.slf4j.LoggerFactory.getLogger;

import de.ubleipzig.image.metadata.ImageMetadataServiceConfig;
import de.ubleipzig.scb.templates.TemplatePaintingAnnotation;
import de.ubleipzig.scb.templates.TemplateTarget;

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

/**
 * ResourceCreator.
 *
 * @author christopher-johnson
 */
public final class ResourceCreator extends AbstractResourceCreator {

    private static Logger logger = getLogger(ResourceCreator.class);
    private static Integer startIndex;
    private static Integer toIndex;
    private static JsonLdUtils jsonLdUtils = new JsonLdUtils();
    private ScbConfig scbConfig;
    private ImageMetadataServiceConfig imageMetadataServiceConfig;

    /**
     * ResourceCreator Class.
     */
    private ResourceCreator(final ScbConfig scbConfig, final ImageMetadataServiceConfig
            imageMetadataServiceConfig) {
        super();
        startIndex = 2210;
        toIndex = 2230;
        this.scbConfig = scbConfig;
        this.imageMetadataServiceConfig = imageMetadataServiceConfig;
    }

    private ScbConfig getScbConfig() {
        return this.scbConfig;
    }

    private ImageMetadataServiceConfig getImageMetadataGeneratorConfig() {
        return this.imageMetadataServiceConfig;
    }

    /**
     * main.
     *
     * @param args String[]
     * @throws Exception Exception
     */
    public static void main(final String[] args) throws Exception {
        final ResourceCreator creator = new ResourceCreator(buildScbConfig(), buildImageMetadataGeneratorConfig());
        putImageResourceBatchFromSubList(creator);
        final List<TemplateTarget> targetList = getTargetList(creator);
        putCanvases(targetList);
        putAnnotations(targetList);
        System.exit(1);
    }

    private static void putImageResourceBatchFromSubList(final ResourceCreator creator) throws Exception {
        final VorlesungImpl vi = new VorlesungImpl(creator.getImageMetadataGeneratorConfig());
        final ScbConfig config = creator.getScbConfig();
        final List<File> files = vi.getFiles();
        files.sort(Comparator.naturalOrder());
        final List<File> sublist = files.subList(startIndex, toIndex);
        final Map<URI, InputStream> batch = new HashMap<>();
        for (File file : sublist) {
            final IRI identifier = rdf.createIRI(config.getBaseUrl() + config.getBodyContainer() + file.getName()
                                                                                                       .toLowerCase());
            final URI uri = new URI(identifier.getIRIString());
            final InputStream is = new FileInputStream(file);
            batch.put(uri, is);
        }
        remote.joiningCompletableFuturePut(batch, "image/tiff");
    }

    private static List<TemplateTarget> getTargetList(final ResourceCreator creator) {
        final TargetBuilder tb = new TargetBuilder(creator.getImageMetadataGeneratorConfig(), creator.getScbConfig());
        return tb.buildCanvases();
    }

    private static List<TemplatePaintingAnnotation> getAnnotationList(final List<TemplateTarget> targetList) {
        final ResourceCreator creator = new ResourceCreator(buildScbConfig(), buildImageMetadataGeneratorConfig());
        final AnnotationBuilder ab = new AnnotationBuilder(
                creator.getImageMetadataGeneratorConfig(), creator.getScbConfig());
        return ab.getAnnotationsWithDimensionedBodies(targetList);
    }

    private static void putCanvases(final List<TemplateTarget> targetList) throws Exception {
        targetList.sort(Comparator.comparing(TemplateTarget::getCanvasLabel));
        final List<TemplateTarget> sublist = targetList.subList(startIndex, toIndex);
        final Map<URI, InputStream> batch = new HashMap<>();
        for (TemplateTarget target : sublist) {
            final IRI identifier = rdf.createIRI(target.getTargetId());
            final URI uri = new URI(identifier.getIRIString());
            final InputStream is = new ByteArrayInputStream(
                    Objects.requireNonNull(serializeToBytes(target).orElse(null)));
            final String n3 = (String) jsonLdUtils.unmarshallToNQuads(is);
            final InputStream n3Stream = new ByteArrayInputStream(Objects.requireNonNull(n3)
                                                                         .getBytes());
            batch.put(uri, n3Stream);
        }
        remote.joiningCompletableFuturePut(batch, contentTypeNTriples);
    }

    private static void putAnnotations(final List<TemplateTarget> targetList) throws Exception {
        final List<TemplatePaintingAnnotation> annoList = getAnnotationList(targetList);
        final List<TemplatePaintingAnnotation> sublist = annoList.subList(startIndex, toIndex);
        final Map<URI, InputStream> batch = new HashMap<>();
        for (TemplatePaintingAnnotation webAnno : sublist) {
            final IRI identifier = rdf.createIRI(webAnno.getAnnoId());
            final URI uri = new URI(identifier.getIRIString());
            logger.info(
                    "Annotation {} for Image Resource {} created: ", webAnno.getAnnoId(), webAnno.getBody()
                                                                                                 .getResourceId());
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
