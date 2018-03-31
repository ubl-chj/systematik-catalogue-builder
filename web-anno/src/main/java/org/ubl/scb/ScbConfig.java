package org.ubl.scb;

public class ScbConfig {
    private String metadataFile;
    private String baseUrl;
    private String imageServiceBaseUrl;
    private String imageServiceType;
    private String annotationContainer;
    private String bodyContainer;
    private String targetContainer;

    /**
     * getMetadataFile.
     *
     * @return {@link String}
     */
    public String getMetadataFile() {
        return this.metadataFile;
    }

    /**
     * setMetadataFile.
     *
     * @param metadataFile metadataFile
     */
    public final void setMetadataFile(final String metadataFile) {
        this.metadataFile = metadataFile;
    }

    /**
     * getBaseUrl.
     *
     * @return {@link String}
     */
    public String getBaseUrl() {
        return this.baseUrl;
    }

    /**
     * setBaseUrl.
     *
     * @param baseUrl baseUrl
     */
    public final void setBaseUrl(final String baseUrl) {
        this.baseUrl = baseUrl;
    }

    /**
     * getImageServiceBaseUrl.
     *
     * @return {@link String}
     */
    public String getImageServiceBaseUrl() {
        return this.imageServiceBaseUrl;
    }

    /**
     * setImageServiceBaseUrl.
     *
     * @param imageServiceBaseUrl imageServiceBaseUrl
     */
    public final void setImageServiceBaseUrl(final String imageServiceBaseUrl) {
        this.imageServiceBaseUrl = imageServiceBaseUrl;
    }

    /**
     * getAnnotationContainer.
     *
     * @return {@link String}
     */
    public String getAnnotationContainer() {
        return this.annotationContainer;
    }

    /**
     * setAnnotationContainer.
     *
     * @param annotationContainer annotationContainer
     */
    public final void setAnnotationContainer(final String annotationContainer) {
        this.annotationContainer = annotationContainer;
    }

    /**
     * getBodyContainer.
     *
     * @return {@link String}
     */
    public String getBodyContainer() {
        return this.bodyContainer;
    }

    /**
     * setBodyContainer.
     *
     * @param bodyContainer annotationContainer
     */
    public final void setBodyContainer(final String bodyContainer) {
        this.bodyContainer = bodyContainer;
    }

    /**
     * getTargetContainer.
     *
     * @return {@link String}
     */
    public String getTargetContainer() {
        return this.targetContainer;
    }

    /**
     * setTargeContext.
     *
     * @param targetContainer targetContainer
     */
    public final void setTargetContainer(final String targetContainer) {
        this.targetContainer = targetContainer;
    }

    /**
     * getTargetContainer.
     *
     * @return {@link String}
     */
    public String getImageServiceType() {
        return this.imageServiceType;
    }

    /**
     * setTargeContext.
     *
     * @param imageServiceType imageServiceType
     */
    public final void setImageServiceType(final String imageServiceType) {
        this.imageServiceType = imageServiceType;
    }
}
