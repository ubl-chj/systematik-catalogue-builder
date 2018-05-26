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

import de.ubleipzig.scb.templates.TemplateMetadata;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * VorlesungImpl.
 *
 * @author christopher-johnson
 */
public class VorlesungImpl {

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
        item.setGroupTag8(p[8]);
        item.setGroupImageSequenceBegin(Integer.parseInt(p[9]));
        item.setGroupSize(Integer.parseInt(p[10]));
        return item;
    };

    /**
     * VorlesungImpl.
     */
    public VorlesungImpl() {
    }

    /**
     * processInputFile.
     *
     * @param is {@link InputStream}
     * @return a list of {@link VPMetadata}
     */
    public List<VPMetadata> processInputFile(final InputStream is) {
        List<VPMetadata> inputList = new ArrayList<>();
        try {
            final BufferedReader br = new BufferedReader(new InputStreamReader(is));
            inputList = br.lines().map(mapToItem).collect(Collectors.toList());
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputList;
    }

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

    /**
     * setMetadata.
     *
     * @param tag   an {@link Optional} {@link String}
     * @param mlist a list of {@link TemplateMetadata}
     */
    public void setMetadata(final String tag, final List<TemplateMetadata> mlist) {
        final TemplateMetadata meta = new TemplateMetadata("tag", tag);
        mlist.add(meta);
    }

}
