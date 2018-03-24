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

import static org.ubl.scb.vocabulary.SC._Canvas;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

/**
 * TemplateTarget.
 *
 * @author christopher-johnson
 */
@JsonPropertyOrder({"@context", "@id", "@type", "metadata", "label", "height", "width"})
public class TemplateTarget {

    @JsonProperty("@context")
    private List<String> context;

    @JsonProperty("@id")
    private String id;

    @JsonProperty("@type")
    private String type = _Canvas;

    @JsonProperty
    private String label;

    @JsonProperty
    private int group;

    @JsonProperty
    private Integer height;

    @JsonProperty
    private Integer width;

    @JsonProperty("metadata")
    private List<TemplateMetadata> metadata;

    /**
     *
     */
    public TemplateTarget() {
    }

    /**
     * @param context a {@link List} of contexts
     */
    public void setContext(final List<String> context) {
        this.context = context;
    }

    /**
     * @return id
     */
    @JsonIgnore
    public String getCanvasId() {
        return this.id;
    }

    /**
     * @param id id
     */
    public void setCanvasId(final String id) {
        this.id = id;
    }

    /**
     * @return label
     */
    @JsonIgnore
    public String getCanvasLabel() {
        return this.label;
    }

    /**
     * @param label label
     */
    public void setCanvasLabel(final String label) {
        this.label = label;
    }

    /**
     * @return group
     */
    @JsonIgnore
    public int getCanvasGroup() {
        return this.group;
    }

    /**
     * @param group group
     */
    public void setCanvasGroup(final int group) {
        this.group = group;
    }

    /**
     * @param metadata metadata as {@link List}
     */
    public void setMetadata(final List<TemplateMetadata> metadata) {
        this.metadata = metadata;
    }

    /**
     * @return height
     */
    @JsonIgnore
    public Integer getCanvasHeight() {
        return this.height;
    }

    /**
     * @param height height as {@link Integer}
     */
    public void setCanvasHeight(final Integer height) {
        this.height = height;
    }

    /**
     * @return width
     */
    @JsonIgnore
    public Integer getCanvasWidth() {
        return this.width;
    }

    /**
     * @param width width as {@link Integer}
     */
    public void setCanvasWidth(final Integer width) {
        this.width = width;
    }

}


