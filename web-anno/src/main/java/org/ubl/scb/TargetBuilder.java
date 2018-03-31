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

package org.ubl.scb;

import static org.slf4j.LoggerFactory.getLogger;
import static org.ubl.scb.UUIDType5.NAMESPACE_URL;

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
import org.ubl.image.metadata.ImageMetadataGeneratorConfig;
import org.ubl.image.metadata.templates.ImageDimensions;
import org.ubl.scb.templates.TemplateMetadata;
import org.ubl.scb.templates.TemplateTarget;
import org.ubl.scb.vocabulary.ANNO;
import org.ubl.scb.vocabulary.SC;

/**
 * TargetBuilder.
 *
 * @author christopher-johnson
 */
public class TargetBuilder {

    private static Logger log = getLogger(TargetBuilder.class);
    private final ImageMetadataGeneratorConfig imageMetadataGeneratorConfig;
    private final ScbConfig scbConfig;

    /**
     * TargetBuilder.
     *
     * @param imageMetadataGeneratorConfig imageMetadataGeneratorConfig
     * @param scbConfig scbConfig
     */
    public TargetBuilder(final ImageMetadataGeneratorConfig imageMetadataGeneratorConfig, final ScbConfig scbConfig) {
        this.imageMetadataGeneratorConfig = imageMetadataGeneratorConfig;
        this.scbConfig = scbConfig;
    }

    /**
     * buildCanvases.
     *
     * @return {@link List} of {@link TemplateTarget}
     */
    public List<TemplateTarget> buildCanvases() {
        final VorlesungImpl vi = new VorlesungImpl(imageMetadataGeneratorConfig);
        final List<VPMetadata> inputList = vi.processInputFile(
                TargetBuilder.class.getResourceAsStream(scbConfig.getMetadataFile()));
        final AtomicInteger atomicInteger = new AtomicInteger(0);
        final List<TemplateTarget> groupedCanvases = new ArrayList<>();
        final List<int[]> result = inputList.stream().map(v -> new int[v.getGroupSize()]).collect(Collectors.toList());
        for (int[] r : result) {
            final Iterable<Integer> iterable = () -> Arrays.stream(r).iterator();
            atomicInteger.getAndIncrement();
            iterable.forEach(x -> {
                final TemplateTarget canvas = new TemplateTarget();
                canvas.setCanvasGroup(atomicInteger.get());
                groupedCanvases.add(canvas);
            });
        }

        final List<String> contexts = new ArrayList<>();
        final String targetContainer = scbConfig.getTargetContainer();
        contexts.add(ANNO.CONTEXT);
        contexts.add(SC.CONTEXT);
        final List<String> files = vi.getFileNames();
        final Iterator<String> i1 = Objects.requireNonNull(files).iterator();
        final Iterator<TemplateTarget> i2 = groupedCanvases.iterator();
        while (i1.hasNext() && i2.hasNext()) {
            final String label = i1.next();
            final UUID canvasUUID = UUIDType5.nameUUIDFromNamespaceAndString(NAMESPACE_URL, label);
            final String id = scbConfig.getBaseUrl() + targetContainer + canvasUUID.toString();
            final TemplateTarget canvas = i2.next();
            canvas.setCanvasId(id);
            canvas.setContext(contexts);
            canvas.setCanvasLabel(label);
            log.debug("Creating Canvas for {}", label);
        }

        final Iterator<TemplateTarget> i6 = groupedCanvases.iterator();
        final Map<Integer, VPMetadata> vpmap = vi.buildVPMap(inputList);
        while (i6.hasNext()) {
            final TemplateTarget c = i6.next();
            final int groupId = c.getCanvasGroup();
            final List<TemplateMetadata> mlist = new ArrayList<>();
            final VPMetadata vp = vpmap.get(groupId);
            final Optional<String> gt1 = Optional.ofNullable(vp.getGroupTag1()).filter(s -> !s.isEmpty());
            vi.setMetadata(gt1, mlist);
            final Optional<String> gt2 = Optional.ofNullable(vp.getGroupTag2()).filter(s -> !s.isEmpty());
            vi.setMetadata(gt2, mlist);
            final Optional<String> gt3 = Optional.ofNullable(vp.getGroupTag3()).filter(s -> !s.isEmpty());
            vi.setMetadata(gt3, mlist);
            final Optional<String> gt4 = Optional.ofNullable(vp.getGroupTag4()).filter(s -> !s.isEmpty());
            vi.setMetadata(gt4, mlist);
            final Optional<String> gt5 = Optional.ofNullable(vp.getGroupTag5()).filter(s -> !s.isEmpty());
            vi.setMetadata(gt5, mlist);
            c.setMetadata(mlist);
            log.debug("Setting Metadata for {}", c.getCanvasLabel());
        }

        final List<ImageDimensions> dims = vi.getDimensions();
        final Iterator<ImageDimensions> i3 = Objects.requireNonNull(dims).iterator();
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
