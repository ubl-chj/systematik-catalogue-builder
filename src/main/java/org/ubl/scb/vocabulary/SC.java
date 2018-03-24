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
 * SC.
 *
 * @author christopher-johnson
 */
public class SC extends BaseVocabulary {

    /* Namespace */
    public static final String URI = "http://iiif.io/api/presentation/2#";
    public static final String NAMESPACE = "sc:";

    /* Context */
    public static final String CONTEXT = "http://iiif.io/api/presentation/2/context.json";

    public static final IRI base = createIRI(URI);

    /* Classes */
    public static final IRI AnnotationList = createIRI(URI + "AnnotationList");
    public static final IRI Canvas = createIRI(URI + "Canvas");
    public static final IRI Collection = createIRI(URI + "Collection");
    public static final IRI Layer = createIRI(URI + "Layer");
    public static final IRI Manifest = createIRI(URI + "Manifest");
    public static final IRI Range = createIRI(URI + "Range");
    public static final IRI Sequence = createIRI(URI + "Sequence");
    public static final IRI Zone = createIRI(URI + "Zone");

    /* Namespace Classes */
    public static final String _AnnotationList = NAMESPACE + "AnnotationList";
    public static final String _Canvas = NAMESPACE + "Canvas";
    public static final String _Collection = NAMESPACE + "Collection";
    public static final String _Layer = NAMESPACE + "Layer";
    public static final String _Manifest = NAMESPACE + "Manifest";
    public static final String _Range = NAMESPACE + "Range";
    public static final String _Sequence = NAMESPACE + "Sequence";
    public static final String _Zone = NAMESPACE + "Zone";

    /* Properties */
    public static final IRI attributionLabel = createIRI(URI + "attributionLabel");
    public static final IRI hasAnnotations = createIRI(URI + "hasAnnotations");
    public static final IRI hasCanvases = createIRI(URI + "hasCanvases");
    public static final IRI hasCollections = createIRI(URI + "hasCollections");
    public static final IRI hasContentLayer = createIRI(URI + "hasContentLayer");
    public static final IRI hasImageAnnotations = createIRI(URI + "hasImageAnnotations");
    public static final IRI hasLists = createIRI(URI + "hasLists");
    public static final IRI hasManifests = (IRI) createIRI(URI + "hasManifests");
    public static final IRI hasRanges = createIRI(URI + "hasRanges");
    public static final IRI hasSequences = createIRI(URI + "hasSequences");
    public static final IRI hasStartCanvas = createIRI(URI + "hasStartCanvas");
    public static final IRI metadataLabels = createIRI(URI + "metadataLabels");
    public static final IRI viewingDirection = createIRI(URI + "ViewingDirection");
    public static final IRI viewingHint = createIRI(URI + "ViewingHint");

    /* Named Individuals */
    public static final IRI bottomToTopDirection = createIRI(URI + "bottomToTopDirection");
    public static final IRI continuousHint = createIRI(URI + "continuousHint");
    public static final IRI facingPagesHint = createIRI(URI + "facingPagesHint");
    public static final IRI individualsHint = createIRI(URI + "individualsHint");
    public static final IRI leftToRightDirection = createIRI(URI + "leftToRightDirection");
    public static final IRI multiPartHint = createIRI(URI + "multiPartHint");
    public static final IRI nonPagedHint = createIRI(URI + "nonPagedHint");
    public static final IRI pagedHint = createIRI(URI + "pagedHint");
    public static final IRI painting = createIRI(URI + "painting");
    public static final IRI rightToLeftDirection = createIRI(URI + "rightToLeftDirection");
    public static final IRI topHint = createIRI(URI + "topHint");
    public static final IRI topToBottomDirection = createIRI(URI + "topToBottomDirection");

    /* Named Individuals */
    public static final String _bottomToTopDirection = NAMESPACE + "bottomToTopDirection";
    public static final String _continuousHint = NAMESPACE + "continuousHint";
    public static final String _facingPagesHint = NAMESPACE + "facingPagesHint";
    public static final String _individualsHint = NAMESPACE + "individualsHint";
    public static final String _leftToRightDirection = NAMESPACE + "leftToRightDirection";
    public static final String _multiPartHint = NAMESPACE + "multiPartHint";
    public static final String _nonPagedHint = NAMESPACE + "nonPagedHint";
    public static final String _pagedHint = NAMESPACE + "pagedHint";
    public static final String _painting = NAMESPACE + "painting";
    public static final String _rightToLeftDirection = NAMESPACE + "rightToLeftDirection";
    public static final String _topHint = NAMESPACE + "topHint";
    public static final String _topToBottomDirection = NAMESPACE + "topToBottomDirection";

}
