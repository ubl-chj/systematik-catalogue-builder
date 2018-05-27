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

package de.ubleipzig.scb.creator;

import static de.ubleipzig.scb.creator.internal.JsonSerializer.serialize;
import static org.junit.Assert.assertEquals;

import de.ubleipzig.image.metadata.ImageMetadataServiceConfig;
import de.ubleipzig.scb.templates.TemplateTarget;

import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import org.apache.commons.rdf.api.IRI;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.trellisldp.client.LdpClientException;

/**
 * TargetBuilderTest.
 *
 * @author christopher-johnson
 */
public class TargetBuilderTest extends CommonTests {
    @BeforeAll
    static void initAll() {
        APP.before();
        baseUrl = "http://localhost:8445/";
        h2client = getClient();
    }

    @Test
    void getTargets() {
        final ScbConfig scbConfig = getScbConfigWithAbsolutePath();
        final TargetBuilder tb = new TargetBuilder(scbConfig);
        final List<TemplateTarget> targetList = tb.buildCanvases();
        System.out.println(serialize(targetList.get(49000)).orElse(""));
        assertEquals(52218, targetList.size());
    }

    @Test
    void getTargetsFromRemoteManifest() throws LdpClientException {
        final ScbConfig scbConfig = getScbConfigWithAbsolutePath();
        final String pid = "TargetBuilderTest" + UUID.randomUUID().toString();
        final ImageMetadataServiceConfig imConfig = new ImageMetadataServiceConfig();
        imConfig.setDimensionManifestFilePath(baseUrl + pid);
        scbConfig.setImageMetadataServiceConfig(imConfig);
        final InputStream is = getDimensionManifest();
        final IRI identifier = rdf.createIRI(baseUrl + pid);
        final IRI base = rdf.createIRI(baseUrl);
        h2client.initUpgrade(base);
        h2client.put(identifier, is, "application/json");
        scbConfig.setDimensionManifestRemoteLocation(baseUrl + pid);
        final InputStream meta = ScbConfigTest.class.getResourceAsStream(
                "/data/sk2-titles.csv");
        scbConfig.setMetadata(meta);
        final TargetBuilder tb = new TargetBuilder(scbConfig);
        final List<TemplateTarget> targetList = tb.buildCanvases();
        System.out.println(serialize(targetList.get(48000)).orElse(""));
        assertEquals(52218, targetList.size());
    }
}
