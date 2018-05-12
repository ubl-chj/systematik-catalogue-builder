package de.ubleipzig.scb.creator;

import de.ubleipzig.image.metadata.ImageMetadataServiceConfig;

import io.dropwizard.configuration.ConfigurationException;
import io.dropwizard.configuration.YamlConfigurationFactory;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jersey.validation.Validators;

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
            scbConfig = new YamlConfigurationFactory<>(
                    ScbConfig.class, Validators.newValidator(), Jackson.newObjectMapper(), "").build(
                    new File(AbstractResourceCreator.class.getResource("/scbconfig.yml").toURI()));
        } catch (IOException | ConfigurationException | URISyntaxException e) {
            throw new RuntimeException(e.getMessage());
        }
        scbConfig.setMetadata(getMetadataRemoteLocation(scbConfig.getMetadataRemoteLocation()));
        final ImageMetadataServiceConfig imageMetadataServiceConfig = scbConfig.getImageMetadataServiceConfig();
        imageMetadataServiceConfig.setDimensionManifest(getDimensionManifestRemoteLocation(
                scbConfig.getImageMetadataServiceConfig().getDimensionManifestFilePath()));
        return scbConfig;
    }
}
