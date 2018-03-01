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

import static de.ubleipzig.scb.JAI.getImageDimensions;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.apache.commons.io.FilenameUtils;

/**
 * VorlesungImpl.
 *
 * @author christopher-johnson
 */
public class VorlesungImpl {

    private String imageSourceDir;
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
        item.setGroupImageSequenceBegin(Integer.parseInt(p[7]));
        item.setGroupSize(Integer.parseInt(p[8]));
        return item;
    };

    /**
     * @param imageSourceDir the local filesystem path to the image resources
     */
    public VorlesungImpl(final String imageSourceDir) {
        this.imageSourceDir = imageSourceDir;
    }

    /**
     * @param keys keys
     * @param values values
     * @param <K> K
     * @param <V> V
     * @return {@link IntStream}
     */
    public static <K, V> Map<K, V> zipToMap(final List<K> keys, final List<V> values) {
        return IntStream.range(0, keys.size()).boxed().collect(Collectors.toMap(keys::get, values::get));
    }

    /**
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
     * @param tag an {@link Optional} {@link String}
     * @param mlist a list of {@link TemplateMetadata}
     */
    public void setMetadata(@SuppressWarnings("OptionalUsedAsFieldOrParameterType") final Optional<String> tag, final
    List<TemplateMetadata> mlist) {
        if (tag.isPresent()) {
            final TemplateMetadata meta = new TemplateMetadata("tag", tag.get());
            mlist.add(meta);
        }
    }

    /**
     * @return a list of {@link String}
     */
    public List<String> getFileNames() {
        final List<String> names = new ArrayList<>();
        try (Stream<Path> paths = Files.walk(Paths.get(imageSourceDir)).filter(Files::isRegularFile)) {
            paths.forEach(p -> {
                final String name = FilenameUtils.removeExtension(p.getFileName().toString());
                names.add(name);
            });
            return names;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @return a list of {@link Path}
     */
    public List<File> getFiles() {
        final List<File> files = new ArrayList<>();
        try (Stream<Path> paths = Files.walk(Paths.get(imageSourceDir)).filter(Files::isRegularFile)) {
            paths.forEach(p -> {
                final File file = new File(String.valueOf(p.toAbsolutePath()));
                files.add(file);
            });
            return files;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @return a list of {@link Dimension}
     */
    public List<Dimension> getDimensions() {
        final List<Dimension> dims = new ArrayList<>();
        try (Stream<Path> paths = Files.walk(Paths.get(imageSourceDir)).filter(Files::isRegularFile)) {
            paths.forEach(p -> {
                final File imageFile = new File(String.valueOf(p.toAbsolutePath()));
                dims.add(getImageDimensions(imageFile));
            });
            return dims;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
