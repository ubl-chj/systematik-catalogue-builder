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

import static javax.imageio.ImageIO.createImageInputStream;
import static javax.imageio.ImageIO.getImageReaders;
import static javax.imageio.spi.IIORegistry.getDefaultInstance;
import static org.slf4j.LoggerFactory.getLogger;

import com.github.jaiimageio.impl.plugins.tiff.TIFFImageWriterSpi;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import org.slf4j.Logger;

/**
 * JAI.
 *
 * @author christopher-johnson
 */
class JAI {

    private static Logger logger = getLogger(JAI.class);

    private JAI() {
    }

    /**
     * registerAllServicesProviders.
     */
    private static void registerAllServicesProviders() {
        getDefaultInstance().registerServiceProvider(new TIFFImageWriterSpi());
    }

    /**
     * getImageDimensions.
     *
     * @param resourceFile File
     * @return Dimension
     */
    static Dimension getImageDimensions(final File resourceFile) {
        registerAllServicesProviders();

        try (ImageInputStream in = createImageInputStream(resourceFile)) {
            final Iterator<ImageReader> readers = getImageReaders(in);
            if (readers.hasNext()) {
                final ImageReader reader = readers.next();
                try {
                    reader.setInput(in);
                    return new Dimension(reader.getWidth(0), reader.getHeight(0));
                } finally {
                    reader.dispose();
                }
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
