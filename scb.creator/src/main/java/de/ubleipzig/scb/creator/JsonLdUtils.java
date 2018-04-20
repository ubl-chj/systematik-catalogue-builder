package de.ubleipzig.scb.creator;

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
     *
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
