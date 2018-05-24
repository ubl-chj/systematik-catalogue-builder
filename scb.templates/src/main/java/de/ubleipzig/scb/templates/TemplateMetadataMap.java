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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TemplateMetadataMap {

    @JsonProperty
    private Integer imageIndex;

    @JsonProperty
    private Integer groupId;

    @JsonProperty
    private Map<String, String> metadataMap;

    /**
     * setMetadataMap.
     *
     * @param metadataMap Map
     */
    public void setMetadataMap(final Map<String, String> metadataMap) {
        this.metadataMap = metadataMap;
    }

    /**
     * getMetadataMap.
     *
     * @return Map
     */
    public Map<String, String> getMetadataMap() {
        return metadataMap;
    }

    /**
     *
     * @param imageIndex Integer
     */
    public void setImageIndex(final Integer imageIndex) {
        this.imageIndex = imageIndex;
    }

    /**
     *
     * @return Integer
     */
    public Integer getImageIndex() {
        return imageIndex;
    }

    /**
     *
     * @param groupId Integer
     */
    public void setGroupId(final Integer groupId) {
        this.groupId = groupId;
    }

    /**
     *
     * @return Integer
     */
    public Integer getGroupId() {
        return groupId;
    }
}
