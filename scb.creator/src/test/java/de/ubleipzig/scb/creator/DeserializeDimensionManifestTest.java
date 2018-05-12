package de.ubleipzig.scb.creator;

import static de.ubleipzig.scb.creator.JsonSerializer.serialize;
import static org.junit.jupiter.api.Assertions.assertEquals;

import de.ubleipzig.image.metadata.ImageMetadataService;
import de.ubleipzig.image.metadata.ImageMetadataServiceConfig;
import de.ubleipzig.image.metadata.ImageMetadataServiceImpl;
import de.ubleipzig.image.metadata.templates.ImageDimensions;

import java.util.List;

import org.junit.jupiter.api.Test;

public class DeserializeDimensionManifestTest extends CommonTests {

    @Test
    void testDeserializeDimensionManifest() {
        ScbConfig scbConfig = getScbConfigWithAbsolutePath();
        final ImageMetadataServiceConfig imageMetadataServiceConfig = scbConfig.getImageMetadataServiceConfig();
        final ImageMetadataService generator = new ImageMetadataServiceImpl(imageMetadataServiceConfig);
        final List<ImageDimensions> dimList = generator.unmarshallDimensionManifestFromFile();
        System.out.println(serialize(dimList.get(1)).orElse(""));
        assertEquals(52218, dimList.size());
    }

}
