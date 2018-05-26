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

import static de.ubleipzig.scb.creator.AbstractResourceCreator.getDimensionManifestRemoteLocation;

import de.ubleipzig.image.metadata.ImageMetadataService;
import de.ubleipzig.image.metadata.ImageMetadataServiceConfig;
import de.ubleipzig.image.metadata.ImageMetadataServiceImpl;
import de.ubleipzig.image.metadata.templates.ImageDimensions;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ImageMetadataImpl {
    private final ImageMetadataServiceConfig imageMetadataServiceConfig;

    public ImageMetadataImpl(final ImageMetadataServiceConfig imageMetadataServiceConfig) {
        this.imageMetadataServiceConfig = imageMetadataServiceConfig;
    }


    /**
     * getFileNames.
     *
     * @return a list of {@link String}
     */
    public List<String> getFileNames() {
        final List<String> names = new ArrayList<>();
        final ImageMetadataService service = new ImageMetadataServiceImpl(imageMetadataServiceConfig);
        final List<ImageDimensions> dimensionManifest = service.unmarshallDimensionManifestFromFile();
        dimensionManifest.forEach(p -> {
            final String name = p.getFilename();
            names.add(name);
        });
        return names;
    }

    /**
     * getFileNames.
     *
     * @return a list of {@link String}
     */
    public List<String> getFileNamesFromRemote() {
        final List<String> names = new ArrayList<>();
        final String remoteDimManifest = getDimensionManifestRemoteLocation(
                imageMetadataServiceConfig.getDimensionManifestFilePath());
        imageMetadataServiceConfig.setDimensionManifest(remoteDimManifest);
        final ImageMetadataService service = new ImageMetadataServiceImpl(imageMetadataServiceConfig);
        final List<ImageDimensions> dimensionManifest = service.unmarshallDimensionManifestFromRemote();
        dimensionManifest.forEach(p -> {
            final String name = p.getFilename();
            names.add(name);
        });
        return names;
    }

    /**
     * getFiles.
     *
     * @return a list of {@link Path}
     */
    public List<File> getFiles() {
        final List<File> files = new ArrayList<>();
        try (Stream<Path> paths = Files.walk(Paths.get(imageMetadataServiceConfig.getImageSourceDir())).filter(
                Files::isRegularFile)) {
            paths.forEach(p -> {
                final File file = new File(String.valueOf(p.toAbsolutePath()));
                files.add(file);
            });
            return files;
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * getDimensions.
     *
     * @return a list of {@link ImageDimensions}
     */
    public List<ImageDimensions> getDimensions() {
        final ImageMetadataService service = new ImageMetadataServiceImpl(imageMetadataServiceConfig);
        final List<ImageDimensions> dims;
        if (imageMetadataServiceConfig.getDimensionManifest() != null) {
            dims = service.unmarshallDimensionManifestFromRemote();
        } else {
            dims = service.unmarshallDimensionManifestFromFile();
        }
        return dims;
    }
}
