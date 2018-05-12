package de.ubleipzig.scb.creator;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.ubleipzig.image.metadata.ImageMetadataServiceConfig;

import io.dropwizard.Configuration;

import java.io.InputStream;

public class ScbConfig extends Configuration {
    private String metadataFile;
    private InputStream metadataInputStream;
    private String baseUrl;
    private String imageServiceBaseUrl;
    private String imageServiceType;
    private String annotationContainer;
    private String bodyContainer;
    private String tagBodyContainer;
    private String targetContainer;
    private ImageMetadataServiceConfig imageMetadataServiceConfig = new ImageMetadataServiceConfig();
    private String metadataRemoteLocation;
    private String dimensionManifestRemoteLocation;

    /**
     * getImageMetadataServiceConfig.
     *
     * @return {@link ImageMetadataServiceConfig}
     */
    @JsonProperty
    public ImageMetadataServiceConfig getImageMetadataServiceConfig() {
        return imageMetadataServiceConfig;
    }

    /**
     * setImageMetadataServiceConfig.
     *
     * @param imageMetadataServiceConfig {@link ImageMetadataServiceConfig}
     */
    @JsonProperty
    public void setImageMetadataServiceConfig(final ImageMetadataServiceConfig imageMetadataServiceConfig) {
        this.imageMetadataServiceConfig = imageMetadataServiceConfig;
    }

    /**
     * getMetadataRemoteLocation.
     *
     * @return {@link String}
     */
    @JsonProperty
    public String getMetadataRemoteLocation() {
        return metadataRemoteLocation;
    }

    /**
     * setMetadataFile.
     *
     * @param metadataRemoteLocation metadataRemoteLocation
     */
    @JsonProperty
    public final void setMetadataRemoteLocation(final String metadataRemoteLocation) {
        this.metadataRemoteLocation = metadataRemoteLocation;
    }

    /**
     * getMetadataRemoteLocation.
     *
     * @return {@link String}
     */
    @JsonProperty
    public String getDimensionManifestRemoteLocation() {
        return dimensionManifestRemoteLocation;
    }

    /**
     * setMetadataFile.
     *
     * @param dimensionManifestRemoteLocation dimensionManifetRemoteLocation
     */
    @JsonProperty
    public final void setDimensionManifestRemoteLocation(final String dimensionManifestRemoteLocation) {
        this.dimensionManifestRemoteLocation = dimensionManifestRemoteLocation;
    }

    /**
     * getMetadataFile.
     *
     * @return {@link String}
     */
    @JsonProperty
    public String getMetadataFile() {
        return metadataFile;
    }

    /**
     * getMetadataFile.
     *
     * @return {@link String}
     */
    public InputStream getMetadataInputStream() {
        return metadataInputStream;
    }

    /**
     * setMetadataFile.
     *
     * @param metadataFile metadataFile
     */
    @JsonProperty
    public final void setMetadata(final String metadataFile) {
        this.metadataFile = metadataFile;
    }

    /**
     * setMetadataFile.
     *
     * @param metadataInputStream metadataInputStream
     */
    public final void setMetadata(final InputStream metadataInputStream) {
        this.metadataInputStream = metadataInputStream;
    }

    /**
     * getBaseUrl.
     *
     * @return {@link String}
     */
    @JsonProperty
    public String getBaseUrl() {
        return baseUrl;
    }

    /**
     * setBaseUrl.
     *
     * @param baseUrl baseUrl
     */
    @JsonProperty
    public final void setBaseUrl(final String baseUrl) {
        this.baseUrl = baseUrl;
    }

    /**
     * getImageServiceBaseUrl.
     *
     * @return {@link String}
     */
    @JsonProperty
    public String getImageServiceBaseUrl() {
        return imageServiceBaseUrl;
    }

    /**
     * setImageServiceBaseUrl.
     *
     * @param imageServiceBaseUrl imageServiceBaseUrl
     */
    @JsonProperty
    public final void setImageServiceBaseUrl(final String imageServiceBaseUrl) {
        this.imageServiceBaseUrl = imageServiceBaseUrl;
    }

    /**
     * getAnnotationContainer.
     *
     * @return {@link String}
     */
    @JsonProperty
    public String getAnnotationContainer() {
        return annotationContainer;
    }

    /**
     * setAnnotationContainer.
     *
     * @param annotationContainer annotationContainer
     */
    @JsonProperty
    public final void setAnnotationContainer(final String annotationContainer) {
        this.annotationContainer = annotationContainer;

    }

    /**
     * getBodyContainer.
     *
     * @return {@link String}
     */
    @JsonProperty
    public String getBodyContainer() {
        return bodyContainer;
    }

    /**
     * setBodyContainer.
     *
     * @param bodyContainer bodyContainer
     */
    @JsonProperty
    public final void setBodyContainer(final String bodyContainer) {
        this.bodyContainer = bodyContainer;
    }

    /**
     * getTagBodyContainer.
     *
     * @return {@link String}
     */
    @JsonProperty
    public String getTagBodyContainer() {
        return tagBodyContainer;
    }

    /**
     * setTagBodyContainer.
     *
     * @param tagBodyContainer tagBodyContainer
     */
    @JsonProperty
    public final void setTagBodyContainer(final String tagBodyContainer) {
        this.tagBodyContainer = tagBodyContainer;
    }

    /**
     * getTargetContainer.
     *
     * @return {@link String}
     */
    @JsonProperty
    public String getTargetContainer() {
        return targetContainer;
    }

    /**
     * setTargeContext.
     *
     * @param targetContainer targetContainer
     */
    @JsonProperty
    public final void setTargetContainer(final String targetContainer) {
        this.targetContainer = targetContainer;
    }

    /**
     * getTargetContainer.
     *
     * @return {@link String}
     */
    @JsonProperty
    public String getImageServiceType() {
        return imageServiceType;
    }

    /**
     * setTargeContext.
     *
     * @param imageServiceType imageServiceType
     */
    @JsonProperty
    public final void setImageServiceType(final String imageServiceType) {
        this.imageServiceType = imageServiceType;
    }
}
