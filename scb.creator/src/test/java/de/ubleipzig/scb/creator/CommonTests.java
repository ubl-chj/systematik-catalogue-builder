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

import static io.dropwizard.testing.ConfigOverride.config;
import static io.dropwizard.testing.ResourceHelpers.resourceFilePath;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import de.ubleipzig.image.metadata.ImageMetadataServiceConfig;

import io.dropwizard.testing.DropwizardTestSupport;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.SSLContext;

import org.apache.commons.rdf.jena.JenaRDF;
import org.trellisldp.app.config.TrellisConfiguration;
import org.trellisldp.app.triplestore.TrellisApplication;
import org.trellisldp.client.LdpClient;
import org.trellisldp.client.LdpClientImpl;

public abstract class CommonTests {
    static final DropwizardTestSupport<TrellisConfiguration> APP = new DropwizardTestSupport<>(TrellisApplication.class,
            resourceFilePath("trellis-config.yml"), config("server.applicationConnectors[0].port", "0"),
            config("binaries", resourceFilePath("data") + "/binaries"),
            config("mementos", resourceFilePath("data") + "/mementos"),
            config("namespaces", resourceFilePath("data/namespaces.json")),
            config("server.applicationConnectors[1].keyStorePath", resourceFilePath("keystore/trellis.jks")));
    static final JenaRDF rdf = new JenaRDF();
    static String baseUrl;
    static String pid;
    static LdpClient h2client = null;

    static LdpClient getClient() {
        try {
            final SimpleSSLContext sslct = new SimpleSSLContext();
            final SSLContext sslContext = sslct.get();
            return new LdpClientImpl(sslContext);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    static InputStream getDimensionManifest() {
        return CommonTests.class.getResourceAsStream(
                "/dimension-manifest-test-8efc742f-709e-47ea-a346-e7bdc3266b49.json");
    }

    static ScbConfig getScbConfigWithAbsolutePath() {
        final ScbConfig scbConfig;
        try {
            final File configFile = new File(CommonTests.class.getResource("/scbconfig-test.yml").toURI());
            final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            scbConfig = mapper.readValue(configFile, ScbConfig.class);
        } catch (IOException | URISyntaxException e) {
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
        try {
            final File configFile = new File(CommonTests.class.getResource("/scbconfig-test.yml").toURI());
            final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            return mapper.readValue(configFile, ScbConfig.class);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    static String read(final InputStream is) {
        final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nread;
        final byte[] data = new byte[1024];
        try {
            while ((nread = is.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nread);
                buffer.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        final byte[] byteArray = buffer.toByteArray();
        return new String(byteArray, StandardCharsets.UTF_8);
    }

}
