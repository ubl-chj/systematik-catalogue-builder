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

import static de.ubleipzig.iiif.vocabulary.ANNO.Annotation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import de.ubleipzig.iiif.vocabulary.SC;

import java.util.List;

/**
 * TemplatePaintingAnnotation.
 *
 * @author christopher-johnson
 */
@JsonPropertyOrder({"@context", "id", "type", "motivation", "body", "target"})
public class TemplatePaintingAnnotation {

    @JsonProperty("@context")
    private List<String> context;

    @JsonProperty("id")
    private String id;

    @JsonProperty("type")
    private String type = Annotation.getIRIString();

    @JsonProperty
    private String motivation = SC.painting.getIRIString();

    @JsonProperty
    private TemplateBody body;

    @JsonProperty
    private String target;

    /**
     * setContext.
     *
     * @param context a {@link List} of contexts
     */
    public void setContext(final List<String> context) {
        this.context = context;
    }

    /**
     * setId.
     *
     * @param id id
     */
    public void setId(final String id) {
        this.id = id;
    }

    /**
     * getAnnoId.
     *
     * @return id
     */
    @JsonIgnore
    public String getAnnoId() {
        return this.id;
    }

    /**
     * getBody.
     *
     * @return TemplateBody
     */
    @JsonIgnore
    public TemplateBody getBody() {
        return this.body;
    }

    /**
     * setBody.
     *
     * @param body body
     */
    public void setBody(final TemplateBody body) {
        this.body = body;
    }

    /**
     * setTarget.
     *
     * @param target target
     */
    public void setTarget(final String target) {
        this.target = target;
    }

}
