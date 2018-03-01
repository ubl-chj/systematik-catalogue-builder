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

package de.ubleipzig.vocabulary;

import static java.util.Collections.unmodifiableMap;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.of;
import static java.util.Optional.ofNullable;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * JSONReader.
 *
 * @author christopher-johnson
 */
public class JSONReader implements JSONReaderService {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private final Map<String, String> data;
    private final String filePath;

    /**
     * Create a JSON-based Namespace service.
     *
     * @param filePath the file path
     */
    public JSONReader(final String filePath) {
        requireNonNull(filePath, "The filePath may not be null!");

        this.filePath = filePath;
        this.data = read(filePath);
        init();
    }

    /**
     * @param filePath {@link String}
     * @return {@link Map}
     */
    public static Map<String, String> read(final String filePath) {
        final File file = new File(filePath);
        final Map<String, String> namespaces = new HashMap<>();
        if (file.exists()) {
            try {
                of(MAPPER.readTree(new File(filePath))).filter(JsonNode::isObject).ifPresent(
                        json -> json.fields().forEachRemaining(node -> {
                            if (node.getValue().isTextual()) {
                                namespaces.put(node.getKey(), node.getValue().textValue());
                            }
                        }));
            } catch (final IOException ex) {
                throw new UncheckedIOException(ex);
            }
        }
        return namespaces;
    }

    @Override
    public Map<String, String> getNamespaces() {
        return unmodifiableMap(data);
    }

    @Override
    public Optional<String> getNamespace(final String prefix) {
        return ofNullable(data.get(prefix));
    }

    @Override
    public Optional<String> getPrefix(final String namespace) {
        return null;
    }

    @Override
    public Boolean setPrefix(final String prefix, final String namespace) {
        return null;
    }

    private void init() {
        if (data.isEmpty()) {
            data.putAll(read(getClass().getResource("/defaultNamespaces.json").getPath()));
        }
    }
}
