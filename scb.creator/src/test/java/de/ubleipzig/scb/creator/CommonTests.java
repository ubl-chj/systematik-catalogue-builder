package de.ubleipzig.scb.creator;

import de.ubleipzig.image.metadata.ImageMetadataServiceConfig;

import io.dropwizard.configuration.ConfigurationException;
import io.dropwizard.configuration.YamlConfigurationFactory;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jersey.validation.Validators;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public abstract class CommonTests {

    static ScbConfig getScbConfigWithAbsolutePath() {
        final ScbConfig scbConfig;
        try {
            scbConfig = new YamlConfigurationFactory<>(
                    ScbConfig.class, Validators.newValidator(), Jackson.newObjectMapper(), "").build(
                    new File(CommonTests.class.getResource("/scbconfig-test.yml").toURI()));
        } catch (IOException | ConfigurationException | URISyntaxException e) {
            throw new RuntimeException(e.getMessage());
        }
        final ImageMetadataServiceConfig imageMetadataServiceConfig = scbConfig.getImageMetadataServiceConfig();
        imageMetadataServiceConfig.setDimensionManifestFilePath(
                CommonTests.class.getResource(imageMetadataServiceConfig.getDimensionManifestFilePath()).getPath());
        return scbConfig;
    }

    static ScbConfig getScbConfig() {
        final ScbConfig scbConfig;
        try {
            scbConfig = new YamlConfigurationFactory<>(
                    ScbConfig.class, Validators.newValidator(), Jackson.newObjectMapper(), "").build(
                    new File(CommonTests.class.getResource("/scbconfig-test.yml").toURI()));
        } catch (IOException | ConfigurationException | URISyntaxException e) {
            throw new RuntimeException(e.getMessage());
        }
        return scbConfig;
    }

}
