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

package org.ubl.scb.templates;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * TemplateBody.
 *
 * @author christopher-johnson
 */
@JsonPropertyOrder({"id", "type", "label", "format", "height", "width", "service"})
public class TemplateBody {

    @JsonProperty("id")
    private String id = "";

    @JsonProperty("type")
    private String type;

    @JsonProperty
    private String format;

    @JsonProperty
    private Integer height;

    @JsonProperty
    private Integer width;

    @JsonProperty
    private TemplateService service;

    /**
     * TemplateBody.
     */
    public TemplateBody() {
    }

    /**
     * setResourceId.
     *
     * @param id id
     */
    public void setResourceId(final String id) {
        this.id = id;
    }

    /**
     * getResourceId.
     *
     * @return String
     */
    @JsonIgnore
    public String getResourceId() {
        return this.id;
    }

    /**
     * setResourceType.
     *
     * @param type label
     */
    public void setResourceType(final String type) {
        this.type = type;
    }

    /**
     * setResourceFormat.
     *
     * @param format format
     */
    public void setResourceFormat(final String format) {
        this.format = format;
    }

    /**
     * setService.
     *
     * @param service service
     */
    public void setService(final TemplateService service) {
        this.service = service;
    }

    /**
     * setResourceHeight.
     *
     * @param height height
     */
    public void setResourceHeight(final Integer height) {
        this.height = height;
    }

    /**
     * setResourceWidth.
     *
     * @param width width
     */
    public void setResourceWidth(final Integer width) {
        this.width = width;
    }
}
