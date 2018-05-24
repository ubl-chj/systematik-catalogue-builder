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

import static de.ubleipzig.image.metadata.JsonSerializer.writeToFile;
import static de.ubleipzig.scb.creator.internal.JsonSerializer.serialize;

import de.ubleipzig.scb.templates.TemplateMetadataMap;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

public class MetadataMapBuilderTest {

    @Test
    void testBuildMetadataMap() {
        List<VPMetadata> inputList = new ArrayList<>();
        InputStream is = MetadataMapBuilderTest.class.getResourceAsStream("/data/sk2-titles.csv");
        try {
            final BufferedReader br = new BufferedReader(new InputStreamReader(is));
            inputList = br.lines().map(mapToItem).collect(Collectors.toList());
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        final int LOOPS = 52218;
        final AtomicInteger ai = new AtomicInteger(0);
        final List<TemplateMetadataMap> mlist = new ArrayList<>();
        for (int i = 0; i < LOOPS; i++) {
            final TemplateMetadataMap tp = new TemplateMetadataMap();
            tp.setImageIndex(ai.get());
            ai.getAndIncrement();
            mlist.add(tp);
        }
        final List<int[]> result = inputList.stream().map(v -> new int[v.getGroupSize()]).collect(
                Collectors.toList());
        final AtomicInteger ai2 = new AtomicInteger(0);
        final List<TemplateMetadataMap> groupedMaps = new ArrayList<>();
        for (int[] r : result) {
            final Iterable<Integer> it = () -> Arrays.stream(r).iterator();
            ai2.getAndIncrement();
            it.forEach(x -> {
                final TemplateMetadataMap map = new TemplateMetadataMap();
                map.setGroupId(ai2.get());
                groupedMaps.add(map);
            });
        }
        final Iterator<TemplateMetadataMap> i1 = mlist.iterator();
        final Iterator<TemplateMetadataMap> i2 = groupedMaps.iterator();
        final Map<Integer, VPMetadata> vpmap = buildVPMap(inputList);

        while (i1.hasNext() && i2.hasNext()) {
            final TemplateMetadataMap meta = i1.next();
            final TemplateMetadataMap c = i2.next();
            final int groupId = c.getGroupId();
            final Map<String, String> tags = new TreeMap<>();
            final VPMetadata vp = vpmap.get(groupId);
            final Optional<String> t1 = Optional.ofNullable(vp.getGroupTag1()).filter(s -> !s.isEmpty());
            t1.ifPresent(s -> tags.put("tag1", s));
            final Optional<String> t2 = Optional.ofNullable(vp.getGroupTag2()).filter(s -> !s.isEmpty());
            t2.ifPresent(s -> tags.put("tag2", s));
            final Optional<String> t3 = Optional.ofNullable(vp.getGroupTag3()).filter(s -> !s.isEmpty());
            t3.ifPresent(s -> tags.put("tag3", s));
            final Optional<String> t4 = Optional.ofNullable(vp.getGroupTag4()).filter(s -> !s.isEmpty());
            t4.ifPresent(s -> tags.put("tag4", s));
            final Optional<String> t5 = Optional.ofNullable(vp.getGroupTag5()).filter(s -> !s.isEmpty());
            t5.ifPresent(s -> tags.put("tag5", s));
            final Optional<String> t6 = Optional.ofNullable(vp.getGroupTag6()).filter(s -> !s.isEmpty());
            t6.ifPresent(s -> tags.put("tag6", s));
            final Optional<String> t7 = Optional.ofNullable(vp.getGroupTag7()).filter(s -> !s.isEmpty());
            t7.ifPresent(s -> tags.put("tag7", s));
            meta.setMetadataMap(tags);
            //System.out.println("setting metadata for sequence beginning " + vp.getGroupImageSequenceBegin());
        }
        String json = serialize(mlist).orElse("");
        ///writeToFile(json, new File("/tmp/vp-metadata.json"));
    }

    private Function<String, VPMetadata> mapToItem = (line) -> {
        final String[] p = line.split("\t", -1);
        final VPMetadata item = new VPMetadata();
        item.setGroupNumber(Integer.parseInt(p[0]));
        item.setGroupTag1(p[1]);
        item.setGroupTag2(p[2]);
        item.setGroupTag3(p[3]);
        item.setGroupTag4(p[4]);
        item.setGroupTag5(p[5]);
        item.setGroupTag6(p[6]);
        item.setGroupTag7(p[7]);
        item.setGroupImageSequenceBegin(Integer.parseInt(p[8]));
        item.setGroupSize(Integer.parseInt(p[9]));
        return item;
    };

    /**
     * buildVPMap.
     *
     * @param inputList a list of {@link VPMetadata}
     * @return {@link Map}
     */
    public Map<Integer, VPMetadata> buildVPMap(final List<VPMetadata> inputList) {
        final Map<Integer, VPMetadata> vpmap = new HashMap<>();
        inputList.forEach(vp -> {
            final int gid = vp.getGroupNumber();
            vpmap.put(gid, vp);
        });
        return vpmap;
    }
}
