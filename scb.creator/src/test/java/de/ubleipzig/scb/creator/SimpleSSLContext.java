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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

/**
 * SimpleSSLContext.
 *
 * @author christopher-johnson
 */
public class SimpleSSLContext {

    SSLContext ssl;

    /**
     * loads default keystore from SimpleSSLContext source directory.
     *
     * @throws IOException ioexception
     */
    public SimpleSSLContext() throws IOException {
        init(SimpleSSLContext.class.getResourceAsStream("/keystore/trellis.jks"));
    }

    /**
     * loads default keystore from given directory.
     *
     * @param dir a directory string
     * @throws IOException ioexception
     */
    public SimpleSSLContext(final String dir) throws IOException {
        final String file = dir + "/testkeys";
        try (FileInputStream fis = new FileInputStream(file)) {
            init(fis);
        }
    }

    private void init(final InputStream i) throws IOException {
        try {
            final char[] passphrase = "changeme".toCharArray();
            final KeyStore ks = KeyStore.getInstance("JKS");
            ks.load(i, passphrase);

            final KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(ks, passphrase);

            final TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
            tmf.init(ks);

            ssl = SSLContext.getInstance("TLS");
            ssl.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
        } catch (KeyManagementException | KeyStoreException | UnrecoverableKeyException | CertificateException
                | NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * get.
     *
     * @return SSLContext
     */
    public SSLContext get() {
        return ssl;
    }
}

