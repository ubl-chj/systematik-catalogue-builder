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

import de.ubleipzig.scb.creator.internal.SimpleSSLContext;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.apache.commons.rdf.api.IRI;
import org.apache.commons.rdf.jena.JenaRDF;
import org.trellisldp.client.LdpClient;
import org.trellisldp.client.LdpClientException;
import org.trellisldp.client.LdpClientImpl;

/**
 * RemoteResource.
 *
 * @author christopher-johnson
 */
public class RemoteResource {

    private static final JenaRDF rdf = new JenaRDF();
    private static LdpClient h2client;

    public RemoteResource() {
        h2client = getClient();
    }

    private static LdpClient getClient() {

        final SimpleSSLContext sslct;
        try {
            sslct = new SimpleSSLContext();
            final SSLContext sslContext = sslct.get();
            return h2client = new LdpClientImpl(sslContext);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * getRemoteBinaryResource.
     *
     * @param resource resource
     * @return byte[]
     * @throws LdpClientException LdpClientException
     */
    public byte[] getRemoteBinaryResource(final String resource) throws LdpClientException {
        final IRI identifier = rdf.createIRI(resource);
        try {
            return h2client.getBinary(identifier);
        } catch (Exception ex) {
            throw new LdpClientException(ex.toString(), ex.getCause());
        }
    }

    /**
     * joiningCompletableFuturePut.
     *
     * @param batch       batch
     * @param contentType contentType
     */
    public void joiningCompletableFuturePut(final Map<URI, InputStream> batch, final String contentType, final String
            baseUrl) throws LdpClientException {
        final IRI base = rdf.createIRI(baseUrl);
        try {
            h2client.initUpgrade(base);
        } catch (Exception ex) {
            throw new LdpClientException(ex.toString(), ex.getCause());
        }
        h2client.joiningCompletableFuturePut(batch, contentType);
    }
}
