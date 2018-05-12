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

import com.github.jsonldjava.core.JsonLdConsts;
import com.github.jsonldjava.core.JsonLdError;
import com.github.jsonldjava.core.JsonLdOptions;
import com.github.jsonldjava.core.JsonLdProcessor;
import com.github.jsonldjava.utils.JsonUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * JsonLdUtils.
 *
 * @author christopher-johnson
 */
public class JsonLdUtils {

    /**
     * JsonLdUtils.
     */
    public JsonLdUtils() {

    }

    /**
     * unmarshallToNQuads.
     *
     * @param is InputStream
     * @return Object nquads
     */
    public Object unmarshallToNQuads(final InputStream is) {
        final JsonLdOptions options = new JsonLdOptions();
        options.format = JsonLdConsts.APPLICATION_NQUADS;
        try {
            return JsonLdProcessor.toRDF(JsonUtils.fromInputStream(is), options);
        } catch (JsonLdError | IOException jsonLdError) {
            jsonLdError.printStackTrace();
        }
        return null;
    }
}
