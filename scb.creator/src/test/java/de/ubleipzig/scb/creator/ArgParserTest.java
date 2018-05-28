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

import static org.junit.jupiter.api.Assertions.assertThrows;

import de.ubleipzig.scb.creator.internal.ArgParser;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ArgParserTest extends CommonTests {
    private ArgParser parser;
    private String configFile = "/scbconfig-test.yml";

    @BeforeAll
    static void initAll() {
        APP.before();
        baseUrl = "http://localhost:8445/";
    }

    @AfterAll
    static void stop() {
        APP.after();
    }

    @Test
    void testRequiredArgs1() {
        parser = new ArgParser();
        final String configFilePath = ArgParserTest.class.getResource(configFile).getPath();
        final String[] args;
        args = new String[]{"-b", "resources", "-f", "0", "-t", "3", "-c", configFilePath, "-i", ArgParserTest.class
                .getResource("/images").getPath(), "-d", ArgParserTest.class.getResource(
                "/dimension-manifest-test-8efc742f-709e-47ea-a346-e7bdc3266b49.json").getPath(),
        "-m", ArgParserTest.class.getResource("/data/sk2-titles.csv").getPath()};
        final SystematikCatalogueBuilder builder = parser.init(args);
        builder.run();
    }

    @Test
    void testTaggingBuilderArgs() {
        parser = new ArgParser();
        final String configFilePath = ArgParserTest.class.getResource(configFile).getPath();
        final String[] args;
        args = new String[]{"-b", "tagging", "-f", "0", "-t", "3", "-c", configFilePath, "-i", ArgParserTest.class
                .getResource(
                "/images").getPath(), "-d", ArgParserTest.class.getResource(
                "/dimension-manifest-test-8efc742f-709e-47ea-a346-e7bdc3266b49.json").getPath(),
                "-m", ArgParserTest.class.getResource("/data/sk2-titles.csv").getPath()};
        final SystematikCatalogueBuilder builder = parser.init(args);
        builder.run();
    }

    @Test
    void testInvalidConfig() {
        parser = new ArgParser();
        final String[] args;
        args = new String[]{"-b", "tagging", "-f", "0", "-t", "3", "-c", "no-config.yml", "-i", ArgParserTest.class
                .getResource(
                "/images").getPath(), "-d", ArgParserTest.class.getResource(
                "/dimension-manifest-test-8efc742f-709e-47ea-a346-e7bdc3266b49.json").getPath()};
        assertThrows(RuntimeException.class, () -> parser.init(args));
    }

    @Test
    void testInvalidArgs() {
        parser = new ArgParser();
        final String[] args;
        args = new String[]{"-y", "tagging"};
        assertThrows(RuntimeException.class, () -> parser.init(args));
    }
}
