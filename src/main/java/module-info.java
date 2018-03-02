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
module de.ubleipzig.scb {
    requires jackson.annotations;
    requires org.apache.commons.rdf.api;
    requires slf4j.api;
    requires java.desktop;
    requires jai.imageio.core;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.core;
    requires org.apache.commons.io;
    requires cool.pandora.ldpclient;
    requires jdk.incubator.httpclient;
    //noinspection removal
    requires java.activation;
}