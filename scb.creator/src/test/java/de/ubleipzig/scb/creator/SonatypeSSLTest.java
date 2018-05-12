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

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;

import org.apache.commons.rdf.api.IRI;
import org.apache.commons.rdf.jena.JenaRDF;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.trellisldp.client.LdpClient;
import org.trellisldp.client.LdpClientException;
import org.trellisldp.client.LdpClientImpl;

public class SonatypeSSLTest extends CommonTests {

    private static final JenaRDF rdf = new JenaRDF();
    private static LdpClient h2client = null;

    @BeforeAll
    static void initAll() {
        try {
            final SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(
                    null, (x509CertChain, authType) -> true).build();
            h2client = new LdpClientImpl(sslContext);
        } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
            e.printStackTrace();
        }
    }

    @Test
    void connectToSonatype() throws LdpClientException {
        final IRI identifier = rdf.createIRI("https://oss.sonatype.org/content/repositories/snapshots/");
        final String res = h2client.getWithContentType(identifier, "text/html");
        //System.out.println(res);
    }

    @Test
    void connectToSonatypeWithApacheClient() throws IOException, KeyStoreException, NoSuchAlgorithmException,
            KeyManagementException {
        final SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(
                null, (x509CertChain, authType) -> true).build();
        final CloseableHttpClient httpClient = HttpClients.custom().setSSLHostnameVerifier(
                NoopHostnameVerifier.INSTANCE).setSSLContext(sslContext).build();
        final CloseableHttpResponse response = httpClient.execute(
                new HttpGet("https://oss.sonatype.org/content/repositories/snapshots/"));
        final String bodyAsString = EntityUtils.toString(response.getEntity());
        // System.out.println(bodyAsString);
    }
}
