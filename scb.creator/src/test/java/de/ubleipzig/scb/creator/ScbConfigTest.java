package de.ubleipzig.scb.creator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class ScbConfigTest extends CommonTests {

    @Test
    public void testScbConfiguration() {
        final ScbConfig config = getScbConfig();
        assertEquals("https://localhost:8445/", config.getBaseUrl());
        assertEquals("http://workspaces.ub.uni-leipzig.de:8182/iiif/2/", config.getImageServiceBaseUrl());
        assertEquals("http://iiif.io/api/image/2/context.json", config.getImageServiceType());
        assertEquals("collection/vp/anno/", config.getAnnotationContainer());
        assertEquals("collection/vp/target/", config.getTargetContainer());
        assertEquals("collection/vp/res/", config.getBodyContainer());
        assertEquals("collection/vp/tag/", config.getTagBodyContainer());
        assertEquals("/sk2-titles-semester.tsv", config.getMetadataFile());
        assertEquals("https://workspaces.ub.uni-leipzig.de:8445/collection/vp/meta/sk2-titles-semester.tsv",
                config.getMetadataRemoteLocation());
        assertEquals(
                "/media/christopher/OVAUBIMG/UBiMG/images/ubleipzig_sk2",
                config.getImageMetadataServiceConfig().getImageSourceDir());
        assertEquals(
                "/dimension-manifest-test-8efc742f-709e-47ea-a346-e7bdc3266b49.json",
                config.getImageMetadataServiceConfig().getDimensionManifestFilePath());
    }
}
