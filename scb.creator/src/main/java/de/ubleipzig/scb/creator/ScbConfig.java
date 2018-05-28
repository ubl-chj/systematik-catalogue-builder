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

import com.fasterxml.jackson.annotation.JsonProperty;

import de.ubleipzig.image.metadata.ImageMetadataServiceConfig;

public class ScbConfig {

    private String metadataLocation;
    private String baseUrl;
    private String imageServiceBaseUrl;
    private String imageServiceType;
    private String annotationContainer;
    private String bodyContainer;
    private String tagBodyContainer;
    private String targetContainer;
    private ImageMetadataServiceConfig imageMetadataServiceConfig = new ImageMetadataServiceConfig();
    private String dimensionManifestRemoteLocation;

    private Integer fromIndex;
    private Integer toIndex;
    private String builderType;
    private Boolean useH2c;


    /**
     *
     * @return Boolean
     */
    @JsonProperty
    public Boolean getUseH2c() {
        return useH2c;
    }

    /**
     *
     * @param useH2c Boolean
     */
    @JsonProperty
    public void setUseH2c(Boolean useH2c) {
        this.useH2c = useH2c;
    }

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
    public String getMetadataLocation() {
        return metadataLocation;
    }

    /**
     * setMetadataFile.
     *
     * @param metadataLocation metadataLocation
     */
    @JsonProperty
    public final void setMetadataLocation(final String metadataLocation) {
        this.metadataLocation = metadataLocation;
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

    public final Integer getFromIndex() {
        return fromIndex;
    }

    public final void setFromIndex(final Integer fromIndex) {
        this.fromIndex = fromIndex;
    }

    public final Integer getToIndex() {
        return toIndex;
    }

    public final void setToIndex(final Integer toIndex) {
        this.toIndex = toIndex;
    }

    public final String getBuilderType() {
        return builderType;
    }

    public final void setBuilderType(final String builderType) {
        this.builderType = builderType;
    }
}
