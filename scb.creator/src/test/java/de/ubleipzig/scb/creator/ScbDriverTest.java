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

import de.ubleipzig.scb.creator.internal.ScbDriver;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ScbDriverTest extends CommonTests {

    @AfterAll
    static void tearDownAll() {
        APP.after();
    }

    @BeforeEach
    public void setUp() {
        APP.before();
        baseUrl = "http://localhost:8445/";
    }

    @Test
    void testRunDriver() {
        baseUrl = "http://localhost:8445/";
        final String configFilePath = ScbDriverTest.class.getResource("/scbconfig-test.yml").getPath();
        final String[] args;
        args = new String[]{"-b", "resources", "-f", "0", "-t", "3", "-c", configFilePath, "-i", ArgParserTest.class
                .getResource(
                "/images").getPath(), "-d", ArgParserTest.class.getResource(
                "/dimension-manifest-test-8efc742f-709e-47ea-a346-e7bdc3266b49.json").getPath()};
        ScbDriver.main(args);
    }
}
