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
 * TemplateService.
 *
 * @author christopher-johnson
 */
@JsonPropertyOrder({"@context", "id", "type", "profile"})
public class TemplateService {

    @JsonProperty("id")
    private String id;

    @JsonProperty("type")
    private String type;

    @JsonProperty
    private String profile;

    /**
     * TemplateService.
     *
     */
    public TemplateService() {
    }

    /**
     * setServiceId.
     *
     * @param id id
     */
    public void setServiceId(final String id) {
        this.id = id;
    }

    /**
     * setServiceType.
     *
     * @param type type
     */
    public void setServiceType(final String type) {
        this.type = type;
    }

    /**
     * setServiceProfile.
     *
     * @param profile profile
     */
    public void setServiceProfile(final String profile) {
        this.profile = profile;
    }

}
