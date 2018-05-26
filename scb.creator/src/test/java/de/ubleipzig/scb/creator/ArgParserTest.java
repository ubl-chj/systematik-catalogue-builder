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

import de.ubleipzig.scb.creator.internal.ArgParser;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class ArgParserTest {
    private ArgParser parser;
    private String configFile = "/scbconfig-test-remote.yml";

    @Disabled
    @Test
    void testRequiredArgs1() {
        parser = new ArgParser();
        final String configFilePath = ArgParserTest.class.getResource(configFile).getPath();
        final String[] args;
        args = new String[]{"-b", "resources", "-f", "100", "-t", "120", "-c", configFilePath};
        final SystematikCatalogueBuilder builder = parser.init(args);
        builder.run();
    }
}
