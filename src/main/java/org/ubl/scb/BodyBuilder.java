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

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.activation.FileTypeMap;

import org.slf4j.Logger;
import org.ubl.scb.templates.TemplateBody;
import org.ubl.scb.templates.TemplateService;
import org.ubl.scb.templates.TemplateTarget;
import org.ubl.scb.vocabulary.DCTypes;

/**
 * BodyBuilder.
 *
 * @author christopher-johnson
 */
public class BodyBuilder {

    private static Logger log = getLogger(TargetBuilder.class);
    private final Config config;

    /**
     *
     * @param config config
     */
    public BodyBuilder(final Config config) {
        this.config = config;
    }

    /**
     * @return a {@link List} of {@link TemplateBody}
     */
    public List<TemplateBody> buildBodies() {
        final String bodyContext = "vp/res/";
        final FileTypeMap mimes = FileTypeMap.getDefaultFileTypeMap();
        final VorlesungImpl vi = new VorlesungImpl(config.getImageSourceDir());
        final List<File> files = vi.getFiles();
        final List<TemplateBody> bodyList = new ArrayList<>();
        for (File file : files) {
            final TemplateBody body = new TemplateBody();
            final String identifier = config.getBaseUrl() + bodyContext + file.getName();
            body.setResourceId(identifier);
            body.setResourceType(DCTypes.Image.getIRIString());
            final String contentType = mimes.getContentType(file);
            body.setResourceFormat(contentType);
            final TemplateService service = new TemplateService();
            final UUID imageUUID = UUIDType5.nameUUIDFromNamespaceAndString(NAMESPACE_URL, bodyContext + file.getName
                    ());
            final String serviceId = config.getImageServiceBaseUrl() + imageUUID;
            final String serviceType = "ImageService3";
            final String serviceProfile = "level2";
            service.setServiceId(serviceId);
            service.setServiceType(serviceType);
            service.setServiceProfile(serviceProfile);
            body.setService(service);
            bodyList.add(body);
        }
        return bodyList;
    }

    /**
     * @return a {@link List} of {@link TemplateBody}
     */
    public List<TemplateBody> getBodiesWithDimensions() {
        final BodyBuilder bb = new BodyBuilder(config);
        final TargetBuilder tb = new TargetBuilder(config);
        final List<TemplateTarget> targetList = tb.buildCanvases();
        final List<TemplateBody> bodyList = bb.buildBodies();
        final Iterator<TemplateBody> i1 = bodyList.iterator();
        final Iterator<TemplateTarget> i2 = targetList.iterator();
        while (i1.hasNext() && i2.hasNext()) {
            final TemplateBody body = i1.next();
            final TemplateTarget target = i2.next();
            final Optional<Integer> height = Optional.ofNullable(target.getCanvasHeight())
                    .filter(s -> !s.toString()
                            .isEmpty());
            final Optional<Integer> width = Optional.ofNullable(target.getCanvasWidth())
                    .filter(s -> !s.toString()
                            .isEmpty());
            height.ifPresent(body::setResourceHeight);
            width.ifPresent(body::setResourceWidth);
        }
        return bodyList;
    }
}
