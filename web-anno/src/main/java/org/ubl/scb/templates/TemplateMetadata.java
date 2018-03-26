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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * TemplateMetadata.
 *
 * @author christopher-johnson
 */
@JsonPropertyOrder({"label", "value"})
public class TemplateMetadata {

    @JsonProperty("label")
    private String label = "unnamed metadata";

    @JsonProperty("value")
    private String value;

    /**
     * TemplateMetadata.
     *
     * @param label {@link String}
     * @param value {@link String}
     */
    public TemplateMetadata(final String label, final String value) {
        this.label = label;
        this.value = value;
    }
}