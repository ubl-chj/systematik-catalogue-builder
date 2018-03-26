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

package org.ubl.scb.vocabulary;

import org.apache.commons.rdf.api.IRI;

/**
 * EXIF.
 *
 * @author christopher-johnson
 */
public class EXIF extends BaseVocabulary {

    /* Namespace */
    public static final String URI = "http://www.w3.org/2003/12/exif/ns#";

    public static final IRI base = createIRI(URI);

    /* Classes */
    public static final IRI width = createIRI(URI + "width");
    public static final IRI height = createIRI(URI + "height");
}
