package org.ubl.scb;

import cool.pandora.ldpclient.LdpClientException;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.apache.commons.rdf.jena.JenaRDF;
import org.ubl.image.metadata.ImageMetadataGeneratorConfig;

/**
 * AbstractResourceCreator.
 *
 * @author christopher-johnson
 */
public abstract class AbstractResourceCreator {

    public static final JenaRDF rdf = new JenaRDF();
    public static final RemoteResource remote = new RemoteResource();

    public static String baseUrl = "https://workspaces.ub.uni-leipzig.de:8445/";

    public static String imageServiceBaseUrl = "http://workspaces.ub.uni-leipzig.de:8182/iiif/2/";
    public static String imageServiceType = "http://iiif.io/api/image/2/context.json";
    public static String annotationContainer = "collection/vp/anno/";
    public static String targetContainer = "collection/vp/target/";
    public static String bodyContainer = "collection/vp/res/";
    public static String tagBodyContainer = "collection/vp/tag/";

    public static String imageSourceDir = "/media/christopher/OVAUBIMG/UBiMG/images/ubleipzig_sk2";
    private String metadataFileString = "/sk2-titles-semester.tsv";

    public static String metadataRemoteLocation =
            "https://workspaces.ub.uni-leipzig.de:8445/collection/vp/meta/sk2-titles-semester.tsv";
    public static String dimensionManifestRemoteLocation = "https://workspaces.ub.uni-leipzig"
            + ".de:8445/collection/vp/meta/dimension-manifest-test-8efc742f-709e-47ea-a346-e7bdc3266b49";


    /**
     *
     * @return String
     */
    public static String getDimensionManifestRemoteLocation() {
        try {
            return new String(remote.getRemoteBinaryResource(dimensionManifestRemoteLocation));
        } catch (LdpClientException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @return InputStream
     */
    public static InputStream getMetadataRemoteLocation() {
        try {
            return new ByteArrayInputStream(
                    new String(remote.getRemoteBinaryResource(metadataRemoteLocation)).getBytes());
        } catch (LdpClientException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ScbConfig buildScbConfig() {
        final ScbConfig scbConfig = new ScbConfig();
        scbConfig.setBaseUrl(baseUrl);
        scbConfig.setMetadata(getMetadataRemoteLocation());
        scbConfig.setAnnotationContainer(annotationContainer);
        scbConfig.setTargetContainer(targetContainer);
        scbConfig.setBodyContainer(bodyContainer);
        scbConfig.setTagBodyContainer(tagBodyContainer);
        scbConfig.setImageServiceBaseUrl(imageServiceBaseUrl);
        scbConfig.setImageServiceType(imageServiceType);

        return scbConfig;
    }

    public static ImageMetadataGeneratorConfig buildImageMetadataGeneratorConfig() {
        final ImageMetadataGeneratorConfig imageMetadataGeneratorConfig = new ImageMetadataGeneratorConfig();
        imageMetadataGeneratorConfig.setDimensionManifestFilePath(dimensionManifestRemoteLocation);
        imageMetadataGeneratorConfig.setImageSourceDir(imageSourceDir);
        imageMetadataGeneratorConfig.setDimensionManifest(getDimensionManifestRemoteLocation());
        return imageMetadataGeneratorConfig;
    }
}
