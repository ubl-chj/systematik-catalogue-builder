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

package de.ubleipzig.scb.templates;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * TemplateTagBody.
 *
 * @author christopher-johnson
 */
@JsonPropertyOrder({"id", "type", "label", "purpose", "value"})
public class TemplateTagBody {

    @JsonProperty("id")
    private String id;

    @JsonProperty("type")
    private String type;

    @JsonProperty
    private String purpose;

    @JsonProperty
    private String value;

    /**
     * TemplateBody.
     */
    public TemplateTagBody() {
    }

    /**
     * setTagId.
     *
     * @param id id
     */
    public void setTagId(final String id) {
        this.id = id;
    }

    /**
     * getResourceId.
     *
     * @return String
     */
    @JsonIgnore
    public String getTagId() {
        return this.id;
    }

    /**
     * setResourceType.
     *
     * @param type label
     */
    public void setTagType(final String type) {
        this.type = type;
    }

    /**
     * setResourceFormat.
     *
     * @param purpose purpose
     */
    public void setTagPurpose(final String purpose) {
        this.purpose = purpose;
    }

    /**
     * setResourceFormat.
     *
     * @param value value
     */
    public void setTagValue(final String value) {
        this.value = value;
    }
}
