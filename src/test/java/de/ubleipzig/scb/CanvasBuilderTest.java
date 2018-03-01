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

package de.ubleipzig.scb;

import static de.ubleipzig.scb.JSONSerializer.serialize;
import static de.ubleipzig.scb.UUIDType5.NAMESPACE_URL;
import static org.junit.Assert.assertEquals;

import de.ubleipzig.vocabulary.ANNO;
import de.ubleipzig.vocabulary.SC;
import java.awt.Dimension;
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
import org.junit.jupiter.api.Test;

/**
 * CanvasBuilderTest.
 *
 * @author christopher-johnson
 */
public class CanvasBuilderTest {

    private String baseUrl = "http://workspaces.ub.uni-leipzig.de:8080/repository/node/collection/vp/canvas/";
    private String imageSourceDir = "/media/christopher/OVAUBIMG/UBiMG/images/ubleipzig_sk2";
    private String metadataFile = "/sk2-semester-tabs.csv";

    @Test
    void buildCanvasTest() {
        final VorlesungImpl vi = new VorlesungImpl(imageSourceDir);
        final List<VPMetadata> inputList = vi.processInputFile(
                CanvasBuilderTest.class.getResourceAsStream(metadataFile));
        final AtomicInteger atomicInteger = new AtomicInteger(0);
        final List<TemplateCanvas> groupedCanvases = new ArrayList<>();
        final List<int[]> result = inputList.stream().map(v -> new int[v.getGroupSize()]).collect(Collectors.toList());
        for (int[] r : result) {
            final Iterable<Integer> iterable = () -> Arrays.stream(r).iterator();
            atomicInteger.getAndIncrement();
            iterable.forEach(x -> {
                final TemplateCanvas canvas = new TemplateCanvas();
                canvas.setCanvasGroup(atomicInteger.get());
                groupedCanvases.add(canvas);
            });
        }

        final List<String> contexts = new ArrayList<>();
        contexts.add(ANNO.CONTEXT);
        contexts.add(SC.CONTEXT);

        final List<String> files = vi.getFileNames();
        final Iterator<String> i1 = Objects.requireNonNull(files).iterator();
        final Iterator<TemplateCanvas> i2 = groupedCanvases.iterator();
        while (i1.hasNext() && i2.hasNext()) {
            final String label = i1.next();
            final UUID canvasUUID = UUIDType5.nameUUIDFromNamespaceAndString(NAMESPACE_URL, label);
            final String id = baseUrl + canvasUUID.toString();
            final TemplateCanvas canvas = i2.next();
            canvas.setCanvasId(id);
            canvas.setContext(contexts);
            canvas.setCanvasLabel(label);
        }

        final Iterator<TemplateCanvas> i6 = groupedCanvases.iterator();
        final Map<Integer, VPMetadata> vpmap = vi.buildVPMap(inputList);
        while (i6.hasNext()) {
            final TemplateCanvas c = i6.next();
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
        }

        final List<Dimension> dims = vi.getDimensions();
        final Iterator<Dimension> i3 = Objects.requireNonNull(dims).iterator();
        final Iterator<TemplateCanvas> i4 = groupedCanvases.iterator();
        while (i3.hasNext() && i4.hasNext()) {
            final Dimension dim = i3.next();
            final TemplateCanvas canvas = i4.next();
            if (dim != null) {
                final double imgWidth = dim.getWidth();
                final Integer width = (int) imgWidth;
                final double imgHeight = dim.getHeight();
                final Integer height = (int) imgHeight;
                canvas.setCanvasWidth(width);
                canvas.setCanvasHeight(height);
            }
        }

        groupedCanvases.forEach(c -> {
            final Optional<String> json = serialize(c);
            System.out.println(json.orElse(null));
        });

        assertEquals(52218, groupedCanvases.size());
    }
}
