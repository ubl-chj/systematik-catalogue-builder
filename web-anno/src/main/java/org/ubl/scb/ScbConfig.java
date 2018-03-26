package org.ubl.scb;

public class ScbConfig {
    private String metadataFile;
    private String baseUrl;
    private String imageServiceBaseUrl;
    private String imageServiceType;
    private String annotationContext;
    private String bodyContext;
    private String targetContext;

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
     * getAnnotationContext.
     *
     * @return {@link String}
     */
    public String getAnnotationContext() {
        return this.annotationContext;
    }

    /**
     * setAnnotationContext.
     *
     * @param annotationContext annotationContext
     */
    public final void setAnnotationContext(final String annotationContext) {
        this.annotationContext = annotationContext;
    }

    /**
     * getBodyContext.
     *
     * @return {@link String}
     */
    public String getBodyContext() {
        return this.bodyContext;
    }

    /**
     * setBodyContext.
     *
     * @param bodyContext annotationContext
     */
    public final void setBodyContext(final String bodyContext) {
        this.bodyContext = bodyContext;
    }

    /**
     * getTargetContext.
     *
     * @return {@link String}
     */
    public String getTargetContext() {
        return this.targetContext;
    }

    /**
     * setTargeContext.
     *
     * @param targetContext targetContext
     */
    public final void setTargetContext(final String targetContext) {
        this.targetContext = targetContext;
    }

    /**
     * getTargetContext.
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
