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

import static de.ubleipzig.scb.creator.UUIDType5.NAMESPACE_URL;
import static org.apache.commons.io.FilenameUtils.removeExtension;
import static org.slf4j.LoggerFactory.getLogger;

import de.ubleipzig.iiif.vocabulary.ANNO;
import de.ubleipzig.iiif.vocabulary.SC;
import de.ubleipzig.image.metadata.ImageMetadataServiceConfig;
import de.ubleipzig.image.metadata.templates.ImageDimensions;
import de.ubleipzig.scb.templates.TemplateMetadata;
import de.ubleipzig.scb.templates.TemplateTarget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.slf4j.Logger;

/**
 * TargetBuilder.
 *
 * @author christopher-johnson
 */
public class TargetBuilder {

    private static Logger log = getLogger(TargetBuilder.class);
    private final ImageMetadataServiceConfig imageMetadataServiceConfig;
    private final ScbConfig scbConfig;

    /**
     * TargetBuilder.
     *
     * @param scbConfig scbConfig
     */
    public TargetBuilder(final ScbConfig scbConfig) {
        this.imageMetadataServiceConfig = scbConfig.getImageMetadataServiceConfig();
        this.scbConfig = scbConfig;
    }

    /**
     * buildCanvases.
     *
     * @return {@link List} of {@link TemplateTarget}
     */
    public List<TemplateTarget> buildCanvases() {
        final VorlesungImpl vi = new VorlesungImpl(imageMetadataServiceConfig);
        final List<VPMetadata> inputList;
        if (scbConfig.getMetadataInputStream() != null) {
            inputList = vi.processInputFile(scbConfig.getMetadataInputStream());
        } else {
            inputList = vi.processInputFile(
                    TargetBuilder.class.getResourceAsStream(scbConfig.getMetadataFile()));
        }
        final AtomicInteger atomicInteger = new AtomicInteger(0);
        final List<TemplateTarget> groupedCanvases = new ArrayList<>();
        final List<int[]> result = inputList.stream()
                                            .map(v -> new int[v.getGroupSize()])
                                            .collect(Collectors.toList());
        for (int[] r : result) {
            final Iterable<Integer> iterable = () -> Arrays.stream(r)
                                                           .iterator();
            atomicInteger.getAndIncrement();
            iterable.forEach(x -> {
                final TemplateTarget canvas = new TemplateTarget();
                canvas.setTargetGroup(atomicInteger.get());
                groupedCanvases.add(canvas);
            });
        }

        final List<String> contexts = new ArrayList<>();
        final String targetContainer = scbConfig.getTargetContainer();
        contexts.add(ANNO.CONTEXT);
        contexts.add(SC.CONTEXT);
        final List<String> files;
        if (imageMetadataServiceConfig.getDimensionManifest() != null) {
            files = vi.getFileNamesFromRemote();
        } else {
            files = vi.getFileNames();
        }
        final Iterator<String> i1 = Objects.requireNonNull(files)
                                           .iterator();
        final Iterator<TemplateTarget> i2 = groupedCanvases.iterator();
        while (i1.hasNext() && i2.hasNext()) {
            final String label = i1.next();
            final UUID canvasUUID = UUIDType5.nameUUIDFromNamespaceAndString(NAMESPACE_URL, label);
            final String id = scbConfig.getBaseUrl() + targetContainer + canvasUUID.toString();
            final TemplateTarget canvas = i2.next();
            canvas.setTargetId(id);
            canvas.setContext(contexts);
            canvas.setCanvasLabel(removeExtension(label));
            log.debug("Creating Canvas for {}", label);
        }

        //TODO remove this
        final Iterator<TemplateTarget> i6 = groupedCanvases.iterator();
        final Map<Integer, VPMetadata> vpmap = vi.buildVPMap(inputList);
        while (i6.hasNext()) {
            final TemplateTarget c = i6.next();
            final int groupId = c.getTargetGroup();
            final List<TemplateMetadata> mlist = new ArrayList<>();
            final VPMetadata vp = vpmap.get(groupId);
            final Optional<String> gt1 = Optional.ofNullable(vp.getGroupTag1())
                                                 .filter(s -> !s.isEmpty());
            vi.setMetadata(gt1, mlist);
            final Optional<String> gt2 = Optional.ofNullable(vp.getGroupTag2())
                                                 .filter(s -> !s.isEmpty());
            vi.setMetadata(gt2, mlist);
            final Optional<String> gt3 = Optional.ofNullable(vp.getGroupTag3())
                                                 .filter(s -> !s.isEmpty());
            vi.setMetadata(gt3, mlist);
            final Optional<String> gt4 = Optional.ofNullable(vp.getGroupTag4())
                                                 .filter(s -> !s.isEmpty());
            vi.setMetadata(gt4, mlist);
            final Optional<String> gt5 = Optional.ofNullable(vp.getGroupTag5())
                                                 .filter(s -> !s.isEmpty());
            vi.setMetadata(gt5, mlist);
            c.setMetadata(mlist);
            c.setTargetGroup(groupId);
            log.debug("Setting Metadata for {}", c.getCanvasLabel());
        }

        final List<ImageDimensions> dims = vi.getDimensions();
        final Iterator<ImageDimensions> i3 = Objects.requireNonNull(dims)
                                                    .iterator();
        final Iterator<TemplateTarget> i4 = groupedCanvases.iterator();
        while (i3.hasNext() && i4.hasNext()) {
            final ImageDimensions dim = i3.next();
            final TemplateTarget canvas = i4.next();
            if (dim != null) {
                canvas.setCanvasWidth(dim.getWidth());
                canvas.setCanvasHeight(dim.getHeight());
            }
            log.debug("Setting Dimensions for {}", canvas.getCanvasLabel());
        }
        return groupedCanvases;
    }
}
