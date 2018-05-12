/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
        imageMetadataServiceConfig.setImageSourceDir(
                CommonTests.class.getResource(imageMetadataServiceConfig.getImageSourceDir()).getPath());
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
