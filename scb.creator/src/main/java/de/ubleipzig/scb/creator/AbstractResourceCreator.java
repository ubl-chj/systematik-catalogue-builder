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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import de.ubleipzig.image.metadata.ImageMetadataServiceConfig;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

import org.apache.commons.rdf.jena.JenaRDF;
import org.trellisldp.client.LdpClientException;

/**
 * AbstractResourceCreator.
 *
 * @author christopher-johnson
 */
public abstract class AbstractResourceCreator {

    public static final JenaRDF rdf = new JenaRDF();
    public static final RemoteResource remote = new RemoteResource();

    /**
     * getDimensionManifestRemoteLocation.
     *
     * @param dimensionManifestRemoteLocation String
     * @return String
     */
    public static String getDimensionManifestRemoteLocation(final String dimensionManifestRemoteLocation) {
        try {
            return new String(remote.getRemoteBinaryResource(dimensionManifestRemoteLocation));
        } catch (LdpClientException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * getMetadataRemoteLocation.
     *
     * @param metadataRemoteLocation String
     * @return InputStream
     */
    public static InputStream getMetadataRemoteLocation(final String metadataRemoteLocation) {
        try {
            return new ByteArrayInputStream(
                    new String(remote.getRemoteBinaryResource(metadataRemoteLocation)).getBytes());
        } catch (LdpClientException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * buildScbConfig.
     *
     * @return ScbConfig
     */
    public static ScbConfig buildScbConfig() {
        final ScbConfig scbConfig;
        try {
            final File configFile = new File(AbstractResourceCreator.class.getResource("/scbconfig.yml").toURI());
            final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            scbConfig = mapper.readValue(configFile, ScbConfig.class);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e.getMessage());
        }
        scbConfig.setMetadata(getMetadataRemoteLocation(scbConfig.getMetadataRemoteLocation()));
        final ImageMetadataServiceConfig imageMetadataServiceConfig = scbConfig.getImageMetadataServiceConfig();
        imageMetadataServiceConfig.setDimensionManifest(getDimensionManifestRemoteLocation(
                scbConfig.getImageMetadataServiceConfig().getDimensionManifestFilePath()));
        return scbConfig;
    }
}
