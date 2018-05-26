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

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.apache.commons.rdf.jena.JenaRDF;
import org.trellisldp.client.LdpClientException;

/**
 * AbstractResourceCreator.
 *
 * @author christopher-johnson
 */
public abstract class AbstractResourceCreator {

    public static final JenaRDF rdf = new JenaRDF();
    public static final RemoteResource remote = new RemoteResource();

    /**
     * getDimensionManifestRemoteLocation.
     *
     * @param dimensionManifestRemoteLocation String
     * @return String
     */
    public static String getDimensionManifestRemoteLocation(final String dimensionManifestRemoteLocation) {
        try {
            return new String(remote.getRemoteBinaryResource(dimensionManifestRemoteLocation));
        } catch (LdpClientException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * getMetadataRemoteLocation.
     *
     * @param metadataRemoteLocation String
     * @return InputStream
     */
    public static InputStream getMetadataRemoteLocation(final String metadataRemoteLocation) {
        try {
            return new ByteArrayInputStream(
                    new String(remote.getRemoteBinaryResource(metadataRemoteLocation)).getBytes());
        } catch (LdpClientException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
