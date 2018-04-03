package org.ubl.scb;

import static java.nio.file.Paths.get;
import static org.apache.jena.riot.WebContent.contentTypeNTriples;
import static org.ubl.scb.JsonSerializer.serializeToBytes;

import cool.pandora.ldpclient.LdpClient;
import cool.pandora.ldpclient.LdpClientImpl;
import cool.pandora.ldpclient.SimpleSSLContext;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.net.ssl.SSLContext;

import org.apache.commons.rdf.api.IRI;
import org.apache.commons.rdf.jena.JenaRDF;
import org.ubl.image.metadata.ImageMetadataGeneratorConfig;
import org.ubl.scb.templates.TemplateTarget;
import org.ubl.scb.templates.TemplateWebAnnotation;

/**
 * ResourceCreator.
 *
 * @author christopher-johnson
 */
public class ResourceCreator {

    private static final JenaRDF rdf = new JenaRDF();
    private static LdpClient h2client = null;
    private static JsonLdUtils jsonLdUtils = new JsonLdUtils();
    private static String baseUrl = "https://localhost:8445/";
    private static String imageSourceDir = "/media/christopher/OVAUBIMG/UBiMG/images/ubleipzig_sk2";
    private static String metadataFile = "/sk2-titles-semester.tsv";
    private static String imageServiceBaseUrl = "http://workspaces.ub.uni-leipzig.de:8182/iiif/2/";
    private static String imageServiceType = "http://iiif.io/api/image/2/context.json";
    private static String annotationContainer = "collection/vp/anno/";
    private static String targetContainer = "collection/vp/target/";
    private static String bodyContainer = "collection/vp/res/";
    private static String dimensionManifestFile;

    /**
     * main.
     *
     * @param args String[]
     * @throws Exception Exception
     */
    public static void main(final String[] args) throws Exception {
        final String path = get(".").toAbsolutePath().normalize().toString();
        //TODO Fix this ...
        dimensionManifestFile = path
                + "/src/main/resources/dimension-manifest-test-8efc742f-709e-47ea-a346-e7bdc3266b49";
        try {
            final SimpleSSLContext sslct = new SimpleSSLContext();
            final SSLContext sslContext = sslct.get();
            h2client = new LdpClientImpl(sslContext);
        } catch (IOException e) {
            e.printStackTrace();
        }
        final Integer startIndex = 100;
        final Integer toIndex = 110;
        //putImageResourceBatchFromSubList(startIndex, toIndex);
        putCanvases(startIndex, toIndex);
        putAnnotations(startIndex, toIndex);

    }

    static void putImageResourceBatchFromSubList(final Integer startIndex, final Integer toIndex) throws Exception {
        final ImageMetadataGeneratorConfig imageMetadataGeneratorConfig = new ImageMetadataGeneratorConfig();
        imageMetadataGeneratorConfig.setImageSourceDir(imageSourceDir);
        final VorlesungImpl vi = new VorlesungImpl(imageMetadataGeneratorConfig);
        final List<File> files = vi.getFiles();
        files.sort(Comparator.naturalOrder());
        final List<File> sublist = files.subList(startIndex, toIndex);
        final Map<URI, InputStream> batch = new HashMap<>();
        for (File file : sublist) {
            final IRI identifier = rdf.createIRI(baseUrl + bodyContainer + file.getName()
                                                                               .toLowerCase());
            final URI uri = new URI(identifier.getIRIString());
            final InputStream is = new FileInputStream(file);
            batch.put(uri, is);
        }
        h2client.joiningCompletableFuturePut(batch, "image/tiff");
    }

    private static void putCanvases(final Integer startIndex, final Integer toIndex) throws Exception {
        final ImageMetadataGeneratorConfig imageMetadataGeneratorConfig = new ImageMetadataGeneratorConfig();
        final ScbConfig scbConfig = new ScbConfig();
        scbConfig.setBaseUrl(baseUrl);
        scbConfig.setTargetContainer(targetContainer);

        imageMetadataGeneratorConfig.setDimensionManifestFilePath(dimensionManifestFile);
        scbConfig.setMetadataFile(metadataFile);
        final TargetBuilder tb = new TargetBuilder(imageMetadataGeneratorConfig, scbConfig);
        final List<TemplateTarget> targetList = tb.buildCanvases();
        targetList.sort(Comparator.comparing(TemplateTarget::getCanvasLabel));
        final List<TemplateTarget> sublist = targetList.subList(startIndex, toIndex);
        final Map<URI, InputStream> batch = new HashMap<>();
        for (TemplateTarget target : sublist) {
            final IRI identifier = rdf.createIRI(target.getCanvasId());
            final URI uri = new URI(identifier.getIRIString());
            final InputStream is = new ByteArrayInputStream(
                    Objects.requireNonNull(serializeToBytes(target).orElse(null)));
            final String n3 = (String) jsonLdUtils.unmarshallToNQuads(is);
            final InputStream n3Stream = new ByteArrayInputStream(Objects.requireNonNull(n3)
                                                                         .getBytes());
            batch.put(uri, n3Stream);
        }
        h2client.joiningCompletableFuturePut(batch, contentTypeNTriples);
    }

    private static void putAnnotations(final Integer startIndex, final Integer toIndex) throws Exception {
        final ImageMetadataGeneratorConfig imageMetadataGeneratorConfig = new ImageMetadataGeneratorConfig();
        final ScbConfig scbConfig = new ScbConfig();
        scbConfig.setBaseUrl(baseUrl);
        scbConfig.setMetadataFile(metadataFile);
        imageMetadataGeneratorConfig.setDimensionManifestFilePath(
                ResourceCreator.class.getResource(dimensionManifestFile)
                                     .getPath());
        scbConfig.setAnnotationContainer(annotationContainer);
        scbConfig.setTargetContainer(targetContainer);
        scbConfig.setBodyContainer(bodyContainer);
        scbConfig.setImageServiceBaseUrl(imageServiceBaseUrl);
        scbConfig.setImageServiceType(imageServiceType);
        final AnnotationBuilder ab = new AnnotationBuilder(imageMetadataGeneratorConfig, scbConfig);
        final List<TemplateWebAnnotation> annoList = ab.getAnnotationsWithDimensionedBodies();
        final List<TemplateWebAnnotation> sublist = annoList.subList(startIndex, toIndex);
        final Map<URI, InputStream> batch = new HashMap<>();
        for (TemplateWebAnnotation webAnno : sublist) {
            final IRI identifier = rdf.createIRI(webAnno.getAnnoId());
            final URI uri = new URI(identifier.getIRIString());
            System.out.println("Image Resource created: " + webAnno.getBody()
                                                                   .getResourceId());
            final InputStream is = new ByteArrayInputStream(
                    Objects.requireNonNull(serializeToBytes(webAnno).orElse(null)));
            final String n3 = (String) jsonLdUtils.unmarshallToNQuads(is);
            final InputStream n3Stream = new ByteArrayInputStream(Objects.requireNonNull(n3)
                                                                         .getBytes());
            batch.put(uri, n3Stream);
        }
        h2client.joiningCompletableFuturePut(batch, contentTypeNTriples);
    }
}
