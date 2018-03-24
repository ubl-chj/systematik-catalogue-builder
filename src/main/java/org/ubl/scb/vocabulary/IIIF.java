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
 * IIIF.
 *
 * @author christopher-johnson
 */
public class IIIF extends BaseVocabulary {

    /* Namespace */
    public static final String URI = "http://iiif.io/api/image/2#";
    public static final String IMAGE_CONTEXT = "http://iiif.io/api/image/2/context.json";
    public static final String SERVICE_PROFILE = "http://iiif.io/api/image/2/level1.json";

    /* Classes */
    public static final IRI Feature = createIRI(URI + "Feature");
    public static final IRI Image = createIRI(URI + "Image");
    public static final IRI ImageProfile = createIRI(URI + "ImageProfile");
    public static final IRI Size = createIRI(URI + "Size");
    public static final IRI Tile = createIRI(URI + "Tile");

    /* Object Properties */
    public static final IRI size = createIRI(URI + "hasSize");
    public static final IRI tiles = createIRI(URI + "hasTiles");
    public static final IRI supports = createIRI(URI + "supports");

    /* Datatype Properties */
    public static final IRI format = createIRI(URI + "format");
    public static final IRI quality = createIRI(URI + "quality");
    public static final IRI scaleFactor = createIRI(URI + "scaleFactor");

    /* Named Individuals */
    public static final IRI arbitraryRotationFeature = createIRI(URI + "arbitraryRotationFeature");
    public static final IRI baseUriRedirectFeature = createIRI(URI + "baseUriRedirectFeature");
    public static final IRI canonicalLinkHeaderFeature = createIRI(URI + "canonicalLinkHeaderFeature");
    public static final IRI corsFeature = createIRI(URI + "corsFeature");
    public static final IRI jsonLdMediaTypeFeature = createIRI(URI + "jsonLdMediaTypeFeature");
    public static final IRI mirroringFeature = createIRI(URI + "mirroringFeature");
    public static final IRI profileLinkHeaderFeature = createIRI(URI + "profileLinkHeaderFeature");
    public static final IRI regionByPctFeature = createIRI(URI + "regionByPctFeature");
    public static final IRI regionByPxFeature = createIRI(URI + "regionByPxFeature");
    public static final IRI regionSquareFeature = createIRI(URI + "regionSquareFeature");
    public static final IRI rotationBy90sFeature = createIRI(URI + "rotationBy90sFeature");
    public static final IRI sizeAboveFullFeature = createIRI(URI + "sizeAboveFullFeature");
    public static final IRI sizeByHFeature = createIRI(URI + "sizeByHFeature");
    public static final IRI sizeByPctFeature = createIRI(URI + "sizeByPctFeature");
    public static final IRI sizeByWFeature = createIRI(URI + "sizeByWFeature");
    public static final IRI sizeByWHFeature = createIRI(URI + "sizeByWHFeature");
    public static final IRI sizeByWHListedFeature = createIRI(URI + "sizeByWHListedFeature");
    public static final IRI sizeByForcedWHFeature = createIRI(URI + "sizeByForcedWHFeature");

}
