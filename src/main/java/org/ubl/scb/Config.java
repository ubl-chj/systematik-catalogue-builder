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

package org.ubl.scb;

/**
 * Config.
 *
 * @author christopher-johnson
 */
public class Config {

    private String baseUrl;
    private String imageSourceDir;
    private String metadataFile;
    private String imageServiceBaseUrl;

    /**
     * @return {@link String}
     */
    public String getBaseUrl() {
        return this.baseUrl;
    }

    /**
     * @param baseUrl baseUrl
     */
    public final void setBaseUrl(final String baseUrl) {
        this.baseUrl = baseUrl;
    }

    /**
     * @return {@link String}
     */
    public String getImageSourceDir() {
        return this.imageSourceDir;
    }

    /**
     * @param imageSourceDir imageSourceDir
     */
    public final void setImageSourceDir(final String imageSourceDir) {
        this.imageSourceDir = imageSourceDir;
    }

    /**
     * @return {@link String}
     */
    public String getMetadataFile() {
        return this.metadataFile;
    }

    /**
     * @param metadataFile metadataFile
     */
    public final void setMetadataFile(final String metadataFile) {
        this.metadataFile = metadataFile;
    }

    /**
     * @return {@link String}
     */
    public String getImageServiceBaseUrl() {
        return this.imageServiceBaseUrl;
    }

    /**
     * @param imageServiceBaseUrl imageServiceBaseUrl
     */
    public final void setImageServiceBaseUrl(final String imageServiceBaseUrl) {
        this.imageServiceBaseUrl = imageServiceBaseUrl;
    }
}
