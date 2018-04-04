package org.ubl.scb;

import cool.pandora.ldpclient.LdpClient;
import cool.pandora.ldpclient.LdpClientException;
import cool.pandora.ldpclient.LdpClientImpl;
import cool.pandora.ldpclient.SimpleSSLContext;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.apache.commons.rdf.api.IRI;
import org.apache.commons.rdf.jena.JenaRDF;

/**
 * RemoteResource.
 *
 * @author christopher-johnson
 */
public class RemoteResource {

    private static LdpClient h2client;
    private static final JenaRDF rdf = new JenaRDF();

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
            e.printStackTrace();
        }
        return null;
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
     * @param batch batch
     * @param contentType contentType
     * @throws LdpClientException LdpClientException
     */
    public void joiningCompletableFuturePut(final Map<URI, InputStream> batch, final String contentType) throws
            LdpClientException {
        try {
            h2client.joiningCompletableFuturePut(batch, contentType);
        } catch (Exception ex) {
            throw new LdpClientException(ex.toString(), ex.getCause());
        }
    }
}
