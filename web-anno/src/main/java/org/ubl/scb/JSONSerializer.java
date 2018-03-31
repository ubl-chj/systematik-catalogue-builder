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

package org.ubl.scb;

import static com.fasterxml.jackson.core.util.DefaultIndenter.SYSTEM_LINEFEED_INSTANCE;
import static com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.slf4j.LoggerFactory.getLogger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Optional;

import org.slf4j.Logger;

/**
 * JSONSerializer.
 *
 * @author christopher-johnson
 */
public final class JSONSerializer {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static Logger logger = getLogger(JSONSerializer.class);

    static {
        MAPPER.configure(WRITE_DATES_AS_TIMESTAMPS, false);
        MAPPER.configure(INDENT_OUTPUT, true);
    }

    private JSONSerializer() {
    }

    /**
     * Serialize the Template.
     *
     * @param template a JSON template
     * @return the Template as a JSON string
     */
    public static Optional<String> serialize(final Object template) {
        try {
            return of(MAPPER.writer(PrettyPrinter.instance).writeValueAsString(template));
        } catch (final JsonProcessingException ex) {
            return empty();
        }
    }

    /**
     * Serialize the Template.
     *
     * @param template a JSON template
     * @return the Template as a JSON string
     */
    public static Optional<byte[]> serializeToBytes(final Object template) {
        try {
            return of(MAPPER.writer(PrettyPrinter.instance).writeValueAsBytes(template));
        } catch (final JsonProcessingException ex) {
            return empty();
        }
    }

    private static class PrettyPrinter extends DefaultPrettyPrinter {

        public static final PrettyPrinter instance = new PrettyPrinter();

        public PrettyPrinter() {
            _arrayIndenter = SYSTEM_LINEFEED_INSTANCE;
        }
    }
}
