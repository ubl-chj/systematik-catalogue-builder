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
import org.apache.commons.rdf.jena.JenaRDF;
import org.slf4j.Logger;
import org.ubl.image.metadata.ImageMetadataGeneratorConfig;
import org.ubl.scb.templates.TemplateTarget;
import org.ubl.scb.templates.TemplateWebAnnotation;

/**
 * ResourceCreator.
 *
 * @author christopher-johnson
 */
public class ResourceCreator {

    private static Logger logger = getLogger(ResourceCreator.class);
    private final ScbConfig config;


    private static final JenaRDF rdf = new JenaRDF();
    private static final RemoteResource remote = new RemoteResource();
    private static ImageMetadataGeneratorConfig imageMetadataGeneratorConfig = new ImageMetadataGeneratorConfig();
    private static ScbConfig scbConfig = new ScbConfig();
    private static JsonLdUtils jsonLdUtils = new JsonLdUtils();

    private static String baseUrl = "https://workspaces.ub.uni-leipzig.de:8445/";
    private static String imageSourceDir = "/media/christopher/OVAUBIMG/UBiMG/images/ubleipzig_sk2";
    private static String imageServiceBaseUrl = "http://workspaces.ub.uni-leipzig.de:8182/iiif/2/";
    private static String imageServiceType = "http://iiif.io/api/image/2/context.json";
    private static String annotationContainer = "collection/vp/anno/";
    private static String targetContainer = "collection/vp/target/";
    private static String bodyContainer = "collection/vp/res/";

    //private static String metadataFile = "/sk2-titles-semester.tsv";
    private static InputStream metadataFile;
    private static String metadataFileLocation =
            "https://workspaces.ub.uni-leipzig" + ".de:8445/collection/vp/meta/sk2-titles-semester.tsv";
    private static String dimensionManifest;
    private static String dimensionManifestLocation = "https://workspaces.ub.uni-leipzig"
            + ".de:8445/collection/vp/meta/dimension-manifest-test-8efc742f-709e-47ea-a346-e7bdc3266b49";
    private static Integer startIndex;
    private static Integer toIndex;


    /**
     * ResourceCreator Class.
     */
    ResourceCreator(final ScbConfig config) {
        this.config = config;
    }

    /**
     * main.
     *
     * @param args String[]
     * @throws Exception Exception
     */
    public static void main(final String[] args) throws Exception {
        dimensionManifest = new String(remote.getRemoteBinaryResource(dimensionManifestLocation));
        metadataFile = new ByteArrayInputStream(
                new String(remote.getRemoteBinaryResource(metadataFileLocation)).getBytes());

        startIndex = 330;
        toIndex = 345;

        putImageResourceBatchFromSubList();
        final List<TemplateTarget> targetList = getTargetList();
        putCanvases(targetList);
        putAnnotations(targetList);
        System.exit(1);
    }

    private static void putImageResourceBatchFromSubList() throws Exception {
        final ImageMetadataGeneratorConfig imageMetadataGeneratorConfig = new ImageMetadataGeneratorConfig();
        imageMetadataGeneratorConfig.setImageSourceDir(imageSourceDir);
        final VorlesungImpl vi = new VorlesungImpl(imageMetadataGeneratorConfig);
        final List<File> files = vi.getFiles();
        files.sort(Comparator.naturalOrder());
        final List<File> sublist = files.subList(startIndex, toIndex);
        final Map<URI, InputStream> batch = new HashMap<>();
        for (File file : sublist) {
            final IRI identifier = rdf.createIRI(baseUrl + bodyContainer + file.getName().toLowerCase());
            final URI uri = new URI(identifier.getIRIString());
            final InputStream is = new FileInputStream(file);
            batch.put(uri, is);
        }
        remote.joiningCompletableFuturePut(batch, "image/tiff");
    }

    private static List<TemplateTarget> getTargetList() {
        scbConfig.setBaseUrl(baseUrl);
        scbConfig.setTargetContainer(targetContainer);
        imageMetadataGeneratorConfig.setDimensionManifest(dimensionManifest);
        scbConfig.setMetadata(metadataFile);
        final TargetBuilder tb = new TargetBuilder(imageMetadataGeneratorConfig, scbConfig);
        return tb.buildCanvases();
    }

    private static List<TemplateWebAnnotation> getAnnotationList(final List<TemplateTarget> targetList) {
        scbConfig.setBaseUrl(baseUrl);
        scbConfig.setMetadata(metadataFile);
        imageMetadataGeneratorConfig.setDimensionManifest(dimensionManifest);
        scbConfig.setAnnotationContainer(annotationContainer);
        scbConfig.setTargetContainer(targetContainer);
        scbConfig.setBodyContainer(bodyContainer);
        scbConfig.setImageServiceBaseUrl(imageServiceBaseUrl);
        scbConfig.setImageServiceType(imageServiceType);
        final AnnotationBuilder ab = new AnnotationBuilder(imageMetadataGeneratorConfig, scbConfig);
        return ab.getAnnotationsWithDimensionedBodies(targetList);
    }

    private static void putCanvases(final List<TemplateTarget> targetList) throws Exception {
        targetList.sort(Comparator.comparing(TemplateTarget::getCanvasLabel));
        final List<TemplateTarget> sublist = targetList.subList(startIndex, toIndex);
        final Map<URI, InputStream> batch = new HashMap<>();
        for (TemplateTarget target : sublist) {
            final IRI identifier = rdf.createIRI(target.getCanvasId());
            final URI uri = new URI(identifier.getIRIString());
            final InputStream is = new ByteArrayInputStream(
                    Objects.requireNonNull(serializeToBytes(target).orElse(null)));
            final String n3 = (String) jsonLdUtils.unmarshallToNQuads(is);
            final InputStream n3Stream = new ByteArrayInputStream(Objects.requireNonNull(n3).getBytes());
            batch.put(uri, n3Stream);
        }
        remote.joiningCompletableFuturePut(batch, contentTypeNTriples);
    }

    private static void putAnnotations(final List<TemplateTarget> targetList) throws Exception {
        final List<TemplateWebAnnotation> annoList = getAnnotationList(targetList);
        final List<TemplateWebAnnotation> sublist = annoList.subList(startIndex, toIndex);
        final Map<URI, InputStream> batch = new HashMap<>();
        for (TemplateWebAnnotation webAnno : sublist) {
            final IRI identifier = rdf.createIRI(webAnno.getAnnoId());
            final URI uri = new URI(identifier.getIRIString());
            logger.info(
                    "Annotation {} for Image Resource {} created: ", webAnno.getAnnoId(),
                    webAnno.getBody().getResourceId());
            final InputStream is = new ByteArrayInputStream(
                    Objects.requireNonNull(serializeToBytes(webAnno).orElse(null)));
            final String n3 = (String) jsonLdUtils.unmarshallToNQuads(is);
            final InputStream n3Stream = new ByteArrayInputStream(Objects.requireNonNull(n3).getBytes());
            batch.put(uri, n3Stream);
        }
        remote.joiningCompletableFuturePut(batch, contentTypeNTriples);
    }
}
