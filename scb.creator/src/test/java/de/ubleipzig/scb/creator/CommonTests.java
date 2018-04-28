package de.ubleipzig.scb.creator;

import de.ubleipzig.image.metadata.ImageMetadataServiceConfig;

public abstract class CommonTests {

    String baseUrl = "https://localhost:8445/";
    String imageSourceDir = "/media/christopher/OVAUBIMG/UBiMG/images/ubleipzig_sk2";
    String metadataFile = "/sk2-titles-semester.tsv";
    String imageServiceBaseUrl = "http://workspaces.ub.uni-leipzig.de:8182/iiif/2/";
    String imageServiceType = "http://iiif.io/api/image/2/context.json";
    String annotationContainer = "collection/vp/anno/";
    String targetContainer = "collection/vp/target/";
    String bodyContainer = "collection/vp/res/";
    String tagBodyContainer = "collection/vp/tag/";
    String dimensionManifestFile = "/dimension-manifest-test-8efc742f-709e-47ea-a346-e7bdc3266b49.json";

    private final ImageMetadataServiceConfig imageMetadataServiceConfig = new ImageMetadataServiceConfig();
    private final ScbConfig scbConfig = new ScbConfig();

    ScbConfig getScbConfig() {
        scbConfig.setBaseUrl(baseUrl);
        scbConfig.setMetadata(metadataFile);
        scbConfig.setAnnotationContainer(annotationContainer);
        scbConfig.setTargetContainer(targetContainer);
        scbConfig.setBodyContainer(bodyContainer);
        scbConfig.setImageServiceBaseUrl(imageServiceBaseUrl);
        scbConfig.setImageServiceType(imageServiceType);
        scbConfig.setTagBodyContainer(tagBodyContainer);
        return this.scbConfig;
    }

    ImageMetadataServiceConfig getImageMetadataGeneratorConfig() {
        final String dimManifestPath = CommonTests.class.getResource(dimensionManifestFile).getPath();
        imageMetadataServiceConfig.setDimensionManifestFilePath(dimManifestPath);
        return this.imageMetadataServiceConfig;
    }
}
