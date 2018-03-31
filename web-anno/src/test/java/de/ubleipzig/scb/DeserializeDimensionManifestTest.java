package de.ubleipzig.scb;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.ubl.scb.JsonSerializer.serialize;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.ubl.image.metadata.ImageMetadataGenerator;
import org.ubl.image.metadata.ImageMetadataGeneratorConfig;
import org.ubl.image.metadata.templates.ImageDimensions;

public class DeserializeDimensionManifestTest extends CommonTests {

    @Test
    void testDeserializeDimensionManifest() {
        final ImageMetadataGeneratorConfig imageMetadataGeneratorConfig = new ImageMetadataGeneratorConfig();
        imageMetadataGeneratorConfig.setDimensionManifestFilePath(
                DeserializeDimensionManifestTest.class.getResource(dimensionManifestFile).getPath());
        final ImageMetadataGenerator generator = new ImageMetadataGenerator(imageMetadataGeneratorConfig);
        final List<ImageDimensions> dimList = generator.buildDimensionManifestFromFile();
        System.out.println(serialize(dimList.get(1)).orElse(""));
        assertEquals(52218, dimList.size());
   }

}
