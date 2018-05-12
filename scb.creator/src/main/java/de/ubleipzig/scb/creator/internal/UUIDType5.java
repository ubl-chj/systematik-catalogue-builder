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

package de.ubleipzig.scb.creator.internal;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.UUID;

/**
 * UUIDType5.
 *
 * @author christopher-johnson
 */
public final class UUIDType5 {

    public static final UUID NAMESPACE_URL = UUID.fromString("6ba7b811-9dad-11d1-80b4-00c04fd430c8");
    private static final Charset UTF8 = Charset.forName("UTF-8");

    private UUIDType5() {
    }

    /**
     * nameUUIDFromNamespaceAndString.
     *
     * @param namespace namespace
     * @param name name
     * @return {@link UUID}
     */
    public static UUID nameUUIDFromNamespaceAndString(final UUID namespace, final String name) {
        return nameUUIDFromNamespaceAndBytes(namespace, Objects.requireNonNull(name, "name == null").getBytes(UTF8));
    }

    /**
     * nameUUIDFromNamespaceAndBytes.
     *
     * @param namespace namespace
     * @param name name
     * @return {@link UUID}
     */
    public static UUID nameUUIDFromNamespaceAndBytes(final UUID namespace, final byte[] name) {
        final MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException nsae) {
            throw new InternalError("SHA-1 not supported");
        }
        md.update(toBytes(Objects.requireNonNull(namespace, "namespace is null")));
        md.update(Objects.requireNonNull(name, "name is null"));
        final byte[] sha1Bytes = md.digest();
        sha1Bytes[6] &= 0x0f;  /* clear version        */
        sha1Bytes[6] |= 0x50;  /* set to version 5     */
        sha1Bytes[8] &= 0x3f;  /* clear variant        */
        sha1Bytes[8] |= 0x80;  /* set to IETF variant  */
        return fromBytes(sha1Bytes);
    }

    /**
     * fromBytes.
     *
     * @param data data
     * @return {@link UUID}
     */
    private static UUID fromBytes(final byte[] data) {
        // Based on the private UUID(bytes[]) constructor
        long msb = 0;
        long lsb = 0;
        assert data.length >= 16;
        for (int i = 0; i < 8; i++) {
            msb = (msb << 8) | (data[i] & 0xff);
        }
        for (int i = 8; i < 16; i++) {
            lsb = (lsb << 8) | (data[i] & 0xff);
        }
        return new UUID(msb, lsb);
    }

    /**
     * toBytes.
     *
     * @param uuid {@link UUID}
     * @return byte[]
     */
    private static byte[] toBytes(final UUID uuid) {
        // inverted logic of fromBytes()
        final byte[] out = new byte[16];
        final long msb = uuid.getMostSignificantBits();
        final long lsb = uuid.getLeastSignificantBits();
        for (int i = 0; i < 8; i++) {
            out[i] = (byte) ((msb >> ((7 - i) * 8)) & 0xff);
        }
        for (int i = 8; i < 16; i++) {
            out[i] = (byte) ((lsb >> ((15 - i) * 8)) & 0xff);
        }
        return out;
    }
}
