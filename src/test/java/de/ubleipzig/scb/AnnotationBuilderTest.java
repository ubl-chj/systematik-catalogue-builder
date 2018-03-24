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

package de.ubleipzig.scb;

import java.util.Iterator;
import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.ubl.scb.AnnotationBuilder;
import org.ubl.scb.BodyBuilder;
import org.ubl.scb.Config;
import org.ubl.scb.templates.TemplateBody;
import org.ubl.scb.templates.TemplateWebAnnotation;

/**
 * AnnotationBuilderTest.
 *
 * @author christopher-johnson
 */
public class AnnotationBuilderTest {

    private String baseUrl = "https://localhost:8445/collection/";
    private String imageSourceDir = "/media/christopher/OVAUBIMG/UBiMG/images/ubleipzig_sk2";
    private String metadataFile = "/sk2-titles-semester.tsv";
    private String imageServiceBaseUrl = "http://localhost:5000/iiif/";

    @Test
    void getAnnotations() {
        final Config config = new Config();
        config.setBaseUrl(baseUrl);
        config.setImageSourceDir(imageSourceDir);
        config.setMetadataFile(metadataFile);
        final AnnotationBuilder ab = new AnnotationBuilder(config);
        final List<TemplateWebAnnotation> annoList = ab.buildAnnotations();
        Assert.assertEquals(52218, annoList.size());
    }

    @Test
    void getAnnotationsWithBodies() {
        final Config config = new Config();
        config.setBaseUrl(baseUrl);
        config.setImageSourceDir(imageSourceDir);
        config.setMetadataFile(metadataFile);
        final AnnotationBuilder ab = new AnnotationBuilder(config);
        final BodyBuilder bb = new BodyBuilder(config);
        final List<TemplateWebAnnotation> annoList = ab.buildAnnotations();
        final List<TemplateBody> bodyList = bb.buildBodies();
        final Iterator<TemplateWebAnnotation> i1 = annoList.iterator();
        final Iterator<TemplateBody> i2 = bodyList.iterator();
        while (i1.hasNext() && i2.hasNext()) {
            final TemplateWebAnnotation webAnno = i1.next();
            final TemplateBody body = i2.next();
            webAnno.setBody(body);
        }
        Assert.assertEquals(52218, annoList.size());
    }

    @Test
    void getAnnotationsWithDimensionedBodies() {
        final Config config = new Config();
        config.setBaseUrl(baseUrl);
        config.setImageSourceDir(imageSourceDir);
        config.setMetadataFile(metadataFile);
        config.setImageServiceBaseUrl(imageServiceBaseUrl);
        final AnnotationBuilder ab = new AnnotationBuilder(config);
        final BodyBuilder bb = new BodyBuilder(config);
        final List<TemplateWebAnnotation> annoList = ab.buildAnnotations();
        final List<TemplateBody> bodyList = bb.getBodiesWithDimensions();
        final Iterator<TemplateWebAnnotation> i1 = annoList.iterator();
        final Iterator<TemplateBody> i2 = bodyList.iterator();
        while (i1.hasNext() && i2.hasNext()) {
            final TemplateWebAnnotation webAnno = i1.next();
            final TemplateBody body = i2.next();
            webAnno.setBody(body);
        }
        Assert.assertEquals(52218, annoList.size());
    }
}
