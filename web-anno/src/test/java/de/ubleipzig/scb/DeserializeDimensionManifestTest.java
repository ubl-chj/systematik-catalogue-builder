package de.ubleipzig.scb;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.ubl.image.metadata.ImageMetadataGenerator;
import org.ubl.image.metadata.ImageMetadataGeneratorConfig;
import org.ubl.image.metadata.templates.ImageDimensions;

public class DeserializeDimensionManifestTest {

    private String dimensionManifestFile = "/dimension-manifest-test-8efc742f-709e-47ea-a346-e7bdc3266b49";

    @Test
    void testDeserializeDimensionManifest() {
        final ImageMetadataGeneratorConfig imageMetadataGeneratorConfig = new ImageMetadataGeneratorConfig();
        imageMetadataGeneratorConfig.setDimensionManifestFilePath(
                DeserializeDimensionManifestTest.class.getResource(dimensionManifestFile).getPath());
        final ImageMetadataGenerator generator = new ImageMetadataGenerator(imageMetadataGeneratorConfig);
        final List<ImageDimensions> dimList = generator.buildDimensionManifestFromFile();
        assertEquals(52218, dimList.size());
        System.out.println(dimList.size());
    }

}
