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

import static io.dropwizard.testing.ConfigOverride.config;
import static io.dropwizard.testing.ResourceHelpers.resourceFilePath;
import static org.apache.jena.riot.WebContent.contentTypeJSONLD;
import static org.ubl.scb.JSONSerializer.serialize;

import io.dropwizard.testing.DropwizardTestSupport;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import javax.net.ssl.SSLContext;

import org.apache.commons.rdf.api.IRI;
import org.apache.commons.rdf.jena.JenaRDF;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.trellisldp.app.TrellisApplication;
import org.trellisldp.app.config.TrellisConfiguration;
import org.ubl.scb.Config;
import org.ubl.scb.TargetBuilder;
import org.ubl.scb.VorlesungImpl;
import org.ubl.scb.templates.TemplateTarget;

import cool.pandora.ldpclient.LdpClient;
import cool.pandora.ldpclient.LdpClientException;
import cool.pandora.ldpclient.LdpClientImpl;
import cool.pandora.ldpclient.SimpleSSLContext;

/**
 * ResourceCreatorTest.
 *
 * @author christopher-johnson
 */
public class ResourceCreatorTest {

    private static final DropwizardTestSupport<TrellisConfiguration> APP = new DropwizardTestSupport<>
            (TrellisApplication.class, resourceFilePath("trellis-config.yml"), config("server" + "" + "" + "" + "" +
                    ".applicationConnectors[1].port", "8445"), config("binaries", resourceFilePath("data") +
                    "/binaries"), config("mementos", resourceFilePath("data") + "/mementos"), config("namespaces",
                    resourceFilePath("data/namespaces.json")), config("server.applicationConnectors[1].keyStorePath",
                    resourceFilePath("keystore/trellis.jks")));
    private static final JenaRDF rdf = new JenaRDF();
    private static String baseUrl;
    private static String pid;
    private static LdpClient h2client = null;
    private String imageSourceDir = "/media/christopher/OVAUBIMG/UBiMG/images/ubleipzig_sk2";
    private String metadataFile = "/sk2-titles-semester.tsv";

    @BeforeAll
    static void initAll() {
        APP.before();
        baseUrl = "https://localhost:8445/collection/";
        try {
            final SimpleSSLContext sslct = new SimpleSSLContext();
            final SSLContext sslContext = sslct.get();
            h2client = new LdpClientImpl(sslContext);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @AfterAll
    static void tearDownAll() {
        APP.after();
    }

    @BeforeEach
    void init() {
        pid = "ldp-test-" + UUID.randomUUID()
                .toString();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testPutImageResource() throws Exception {
        final VorlesungImpl vi = new VorlesungImpl(imageSourceDir);
        final List<File> files = vi.getFiles();
        for (File file : files) {
            final IRI identifier = rdf.createIRI(baseUrl + "/vp/res/" + file.getName());
            final InputStream is = new FileInputStream(file);
            h2client.put(identifier, is, "image/tiff");
        }
    }

    @Test
    void putCanvases() {
        final Config config = new Config();
        config.setBaseUrl(baseUrl);
        config.setImageSourceDir(imageSourceDir);
        config.setMetadataFile(metadataFile);
        final TargetBuilder cb = new TargetBuilder(config);
        final List<TemplateTarget> canvaslist = cb.buildCanvases();
        for (TemplateTarget canvas : canvaslist) {
            final IRI identifier = rdf.createIRI(canvas.getCanvasId());
            final Optional<String> json = serialize(canvas);
            final String output = json.orElse(null);
            final InputStream is = new ByteArrayInputStream(Objects.requireNonNull(output)
                    .getBytes());
            try {
                h2client.put(identifier, is, contentTypeJSONLD);
            } catch (LdpClientException e) {
                e.printStackTrace();
            }
        }
    }

}
