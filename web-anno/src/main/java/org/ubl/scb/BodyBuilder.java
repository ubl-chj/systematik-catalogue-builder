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
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.activation.FileTypeMap;

import org.slf4j.Logger;
import org.ubl.image.metadata.ImageMetadataGeneratorConfig;
import org.ubl.image.metadata.templates.ImageDimensions;
import org.ubl.scb.templates.TemplateBody;
import org.ubl.scb.templates.TemplateService;
import org.ubl.scb.templates.TemplateTarget;
import org.ubl.scb.vocabulary.DCTypes;
import org.ubl.scb.vocabulary.IIIF;

/**
 * BodyBuilder.
 *
 * @author christopher-johnson
 */
public class BodyBuilder {

    private static Logger log = getLogger(BodyBuilder.class);
    private final ImageMetadataGeneratorConfig imageMetadataGeneratorConfig;
    private final ScbConfig scbConfig;

    /**
     * BodyBuilder.
     *
     * @param imageMetadataGeneratorConfig imageMetadataGeneratorConfig
     * @param scbConfig scbConfig
     */
    public BodyBuilder(final ImageMetadataGeneratorConfig imageMetadataGeneratorConfig, final ScbConfig scbConfig) {
        this.imageMetadataGeneratorConfig = imageMetadataGeneratorConfig;
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
        final VorlesungImpl vi = new VorlesungImpl(imageMetadataGeneratorConfig);
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
            final String serviceProfile = IIIF.SERVICE_PROFILE;
            service.setServiceId(serviceId);
            service.setServiceType(serviceType);
            service.setServiceProfile(serviceProfile);
            body.setService(service);
            bodyList.add(body);
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
