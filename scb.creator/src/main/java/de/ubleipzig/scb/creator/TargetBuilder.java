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

import static de.ubleipzig.scb.creator.AbstractResourceCreator.getMetadataRemoteLocation;
import static de.ubleipzig.scb.creator.internal.UUIDType5.NAMESPACE_URL;
import static org.apache.commons.io.FilenameUtils.removeExtension;
import static org.slf4j.LoggerFactory.getLogger;

import de.ubleipzig.iiif.vocabulary.ANNO;
import de.ubleipzig.iiif.vocabulary.SC;
import de.ubleipzig.image.metadata.ImageMetadataServiceConfig;
import de.ubleipzig.image.metadata.templates.ImageDimensions;
import de.ubleipzig.scb.creator.internal.UUIDType5;
import de.ubleipzig.scb.templates.TemplateMetadata;
import de.ubleipzig.scb.templates.TemplateTarget;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
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
     * buildTargetList.
     *
     * @param inputList List
     * @return List
     */
    public List<TemplateTarget> buildTargetList(final List<VPMetadata> inputList) {
        final AtomicInteger atomicInteger = new AtomicInteger(0);
        final List<TemplateTarget> targetList = new ArrayList<>();
        final List<int[]> result = inputList.stream().map(v -> new int[v.getGroupSize()]).collect(Collectors.toList());
        for (int[] r : result) {
            final Iterable<Integer> iterable = () -> Arrays.stream(r).iterator();
            atomicInteger.getAndIncrement();
            iterable.forEach(x -> {
                final TemplateTarget target = new TemplateTarget();
                target.setTargetGroup(atomicInteger.get());
                targetList.add(target);
            });
        }
        return targetList;
    }

    private List<String> getContexts() {
        final List<String> contexts = new ArrayList<>();
        contexts.add(ANNO.CONTEXT);
        contexts.add(SC.CONTEXT);
        return contexts;
    }

    private List<String> getFileNames(final ImageMetadataImpl im) {
        final List<String> fileNames;
        final String dimManifest = imageMetadataServiceConfig.getDimensionManifestFilePath();
        if (dimManifest.contains("http")) {
            fileNames = im.getFileNamesFromRemote();
        } else {
            fileNames = im.getFileNames();
        }
        return fileNames;
    }

    private List<VPMetadata> getMetadata(final VorlesungImpl vi) {
        List<VPMetadata> metadata = null;
        final String metadataFile = scbConfig.getMetadataLocation();
        if (metadataFile.contains("http")) {
            final InputStream is = getMetadataRemoteLocation(metadataFile);
            metadata = vi.processInputFile(is);
        } else {
            try {
                metadata = vi.processInputFile(new FileInputStream(scbConfig.getMetadataLocation()));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return metadata;
    }

    private List<TemplateTarget> createTargetIdentifiers(final List<String> fileNames, final List<String> contexts,
                                                         final List<TemplateTarget> targetList) {
        final String targetContainer = scbConfig.getTargetContainer();
        final Iterator<String> i1 = Objects.requireNonNull(fileNames).iterator();
        final Iterator<TemplateTarget> i2 = targetList.iterator();
        while (i1.hasNext() && i2.hasNext()) {
            final String label = i1.next();
            final UUID canvasUUID = UUIDType5.nameUUIDFromNamespaceAndString(NAMESPACE_URL, label);
            final String id = scbConfig.getBaseUrl() + targetContainer + canvasUUID.toString();
            final TemplateTarget target = i2.next();
            target.setTargetId(id);
            target.setContext(contexts);
            target.setCanvasLabel(removeExtension(label));
            log.debug("Creating Target for {}", label);
        }
        return targetList;
    }

    private List<TemplateTarget> setTargetMetadata(final List<TemplateTarget> identifierTargets, final VorlesungImpl
            vi, final List<VPMetadata> metadata) {
        final Iterator<TemplateTarget> it = identifierTargets.iterator();
        final Map<Integer, VPMetadata> vpmap = vi.buildVPMap(metadata);
        while (it.hasNext()) {
            final TemplateTarget target = it.next();
            final int groupId = target.getTargetGroup();
            final List<TemplateMetadata> mlist = new ArrayList<>();
            final VPMetadata vp = vpmap.get(groupId);
            final Optional<String> gt1 = Optional.ofNullable(vp.getGroupTag1()).filter(s -> !s.isEmpty());
            gt1.ifPresent(t -> vi.setMetadata(t, mlist));
            final Optional<String> gt2 = Optional.ofNullable(vp.getGroupTag2()).filter(s -> !s.isEmpty());
            gt2.ifPresent(t -> vi.setMetadata(t, mlist));
            final Optional<String> gt3 = Optional.ofNullable(vp.getGroupTag3()).filter(s -> !s.isEmpty());
            gt3.ifPresent(t -> vi.setMetadata(t, mlist));
            final Optional<String> gt4 = Optional.ofNullable(vp.getGroupTag4()).filter(s -> !s.isEmpty());
            gt4.ifPresent(t -> vi.setMetadata(t, mlist));
            final Optional<String> gt5 = Optional.ofNullable(vp.getGroupTag5()).filter(s -> !s.isEmpty());
            gt5.ifPresent(t -> vi.setMetadata(t, mlist));
            final Optional<String> gt6 = Optional.ofNullable(vp.getGroupTag6()).filter(s -> !s.isEmpty());
            gt6.ifPresent(t -> vi.setMetadata(t, mlist));
            final Optional<String> gt7 = Optional.ofNullable(vp.getGroupTag7()).filter(s -> !s.isEmpty());
            gt7.ifPresent(t -> vi.setMetadata(t, mlist));
            final Optional<String> gt8 = Optional.ofNullable(vp.getGroupTag8()).filter(s -> !s.isEmpty());
            gt8.ifPresent(t -> vi.setMetadata(t, mlist));
            target.setMetadata(mlist);
            target.setTargetGroup(groupId);
            log.debug("Setting Metadata for {}", target.getCanvasLabel());
        }
        return identifierTargets;
    }

    private List<TemplateTarget> setImageDimensions(final List<TemplateTarget> metadataTargets, final
    ImageMetadataImpl im) {
        final List<ImageDimensions> dims = im.getDimensions();
        final Iterator<ImageDimensions> i3 = Objects.requireNonNull(dims).iterator();
        final Iterator<TemplateTarget> i4 = metadataTargets.iterator();
        while (i3.hasNext() && i4.hasNext()) {
            final ImageDimensions dim = i3.next();
            final TemplateTarget target = i4.next();
            if (dim != null) {
                target.setCanvasWidth(dim.getWidth());
                target.setCanvasHeight(dim.getHeight());
            }
            log.debug("Setting Dimensions for {}", target.getCanvasLabel());
        }
        return metadataTargets;
    }

    /**
     * buildCanvases.
     *
     * @return {@link List} of {@link TemplateTarget}
     */
    public List<TemplateTarget> buildCanvases() {
        final VorlesungImpl vi = new VorlesungImpl();
        final ImageMetadataImpl im = new ImageMetadataImpl(imageMetadataServiceConfig);
        final List<VPMetadata> metadata = getMetadata(vi);
        final List<TemplateTarget> targetList = buildTargetList(metadata);
        final List<String> contexts = getContexts();
        final List<String> fileNames = getFileNames(im);
        final List<TemplateTarget> identifierTargets = createTargetIdentifiers(fileNames, contexts, targetList);
        final List<TemplateTarget> metadataTargets = setTargetMetadata(identifierTargets, vi, metadata);
        return setImageDimensions(metadataTargets, im);
    }
}
