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

import static de.ubleipzig.scb.creator.internal.UUIDType5.NAMESPACE_URL;
import static org.slf4j.LoggerFactory.getLogger;

import de.ubleipzig.iiif.vocabulary.DCTypes;
import de.ubleipzig.iiif.vocabulary.IIIFEnum;
import de.ubleipzig.image.metadata.ImageMetadataServiceConfig;
import de.ubleipzig.image.metadata.templates.ImageDimensions;
import de.ubleipzig.scb.creator.internal.UUIDType5;
import de.ubleipzig.scb.templates.TemplateBody;
import de.ubleipzig.scb.templates.TemplateService;
import de.ubleipzig.scb.templates.TemplateTarget;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.activation.FileTypeMap;

import org.slf4j.Logger;

/**
 * BodyBuilder.
 *
 * @author christopher-johnson
 */
public class BodyBuilder {

    private static Logger logger = getLogger(BodyBuilder.class);
    private final ImageMetadataServiceConfig imageMetadataServiceConfig;
    private final ScbConfig scbConfig;

    /**
     * BodyBuilder.
     *
     * @param scbConfig scbConfig
     */
    public BodyBuilder(final ScbConfig scbConfig) {
        this.imageMetadataServiceConfig = scbConfig.getImageMetadataServiceConfig();
        this.scbConfig = scbConfig;
    }

    /**
     * buildBodies.
     *
     * @return a {@link List} of {@link TemplateBody}
     */
    public List<TemplateBody> buildBodies() {
        final String bodyContainer = scbConfig.getBodyContainer();
        final FileTypeMap mimes = FileTypeMap.getDefaultFileTypeMap();
        final VorlesungImpl vi = new VorlesungImpl(imageMetadataServiceConfig);
        final List<ImageDimensions> files = vi.getDimensions();
        final List<TemplateBody> bodyList = new ArrayList<>();
        for (ImageDimensions file : files) {
            final String filename = file.getFilename().toLowerCase();
            final TemplateBody body = new TemplateBody();
            final String identifier = scbConfig.getBaseUrl() + bodyContainer + filename;
            body.setResourceId(identifier);
            body.setResourceType(DCTypes.Image.getIRIString());
            final String contentType = mimes.getContentType(filename);
            body.setResourceFormat(contentType);
            final TemplateService service = new TemplateService();
            final UUID imageUUID = UUIDType5.nameUUIDFromNamespaceAndString(
                    NAMESPACE_URL, bodyContainer + filename);
            final String serviceId = scbConfig.getImageServiceBaseUrl() + imageUUID;
            final String serviceType = scbConfig.getImageServiceType();
            final String serviceProfile = IIIFEnum.SERVICE_PROFILE.IRIString();
            service.setServiceId(serviceId);
            service.setServiceType(serviceType);
            service.setServiceProfile(serviceProfile);
            body.setService(service);
            bodyList.add(body);
            logger.debug("Adding Body {} to list", identifier);
        }
        return bodyList;
    }

    /**
     * getBodiesWithDimensions.
     *
     * @param targetList targetList
     * @return a {@link List} of {@link TemplateBody}
     */
    public List<TemplateBody> getBodiesWithDimensions(final List<TemplateTarget> targetList) {
        final List<TemplateBody> bodyList = buildBodies();
        final Iterator<TemplateBody> i1 = bodyList.iterator();
        final Iterator<TemplateTarget> i2 = targetList.iterator();
        while (i1.hasNext() && i2.hasNext()) {
            final TemplateBody body = i1.next();
            final TemplateTarget target = i2.next();
            final Optional<Integer> height = Optional.ofNullable(target.getCanvasHeight()).filter(
                    s -> !s.toString().isEmpty());
            final Optional<Integer> width = Optional.ofNullable(target.getCanvasWidth()).filter(
                    s -> !s.toString().isEmpty());
            height.ifPresent(body::setResourceHeight);
            width.ifPresent(body::setResourceWidth);
        }
        return bodyList;
    }
}
