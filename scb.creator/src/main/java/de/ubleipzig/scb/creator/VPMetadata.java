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

import static org.slf4j.LoggerFactory.getLogger;

import de.ubleipzig.scb.templates.TemplateMetadata;

import java.util.ArrayList;

import org.slf4j.Logger;

/**
 * VPMetadata.
 *
 * @author christopher-johnson
 */
public class VPMetadata {

    private static Logger logger = getLogger(VPMetadata.class);
    private int groupNumber;
    private String groupTag1;
    private String groupTag2;
    private String groupTag3;
    private String groupTag4;
    private String groupTag5;
    private String groupTag6;
    private int size;
    private int sequenceBegin;

    /**
     * VPMetadata.
     */
    public VPMetadata() {
    }

    /**
     * getInfo.
     *
     * @return {@link ArrayList}
     */
    public ArrayList<TemplateMetadata> getInfo() {
        final ArrayList<TemplateMetadata> meta = new ArrayList<>();
        meta.add(new TemplateMetadata(null, null));
        return meta;
    }

    /**
     * getGroupNumber.
     *
     * @return {@link Integer}
     */
    public int getGroupNumber() {
        return this.groupNumber;
    }

    /**
     * setGroupNumber.
     *
     * @param groupNumber {@link Integer}
     */
    public void setGroupNumber(final int groupNumber) {
        this.groupNumber = groupNumber;
    }

    /**
     * getGroupTag1.
     *
     * @return {@link String}
     */
    public String getGroupTag1() {
        return this.groupTag1;
    }

    /**
     * setGroupTag1.
     *
     * @param groupTag1 {@link String}
     */
    public void setGroupTag1(final String groupTag1) {
        this.groupTag1 = groupTag1;
    }

    /**
     * getGroupTag2.
     *
     * @return {@link String}
     */
    public String getGroupTag2() {
        return this.groupTag2;
    }

    /**
     * setGroupTag2.
     *
     * @param groupTag2 {@link String}
     */
    public void setGroupTag2(final String groupTag2) {
        this.groupTag2 = groupTag2;
    }

    /**
     * getGroupTag3.
     *
     * @return {@link String}
     */
    public String getGroupTag3() {
        return this.groupTag3;
    }

    /**
     * setGroupTag3.
     *
     * @param groupTag3 {@link String}
     */
    public void setGroupTag3(final String groupTag3) {
        this.groupTag3 = groupTag3;
    }

    /**
     * getGroupTag4.
     *
     * @return {@link String}
     */
    public String getGroupTag4() {
        return this.groupTag4;
    }

    /**
     * setGroupTag4.
     *
     * @param groupTag4 {@link String}
     */
    public void setGroupTag4(final String groupTag4) {
        this.groupTag4 = groupTag4;
    }

    /**
     * getGroupTag5.
     *
     * @return {@link String}
     */
    public String getGroupTag5() {
        return this.groupTag5;
    }

    /**
     * setGroupTag5.
     *
     * @param groupTag5 {@link String}
     */
    public void setGroupTag5(final String groupTag5) {
        this.groupTag5 = groupTag5;
    }

    /**
     * getGroupTag6.
     *
     * @return {@link String}
     */
    public String getGroupTag6() {
        return this.groupTag6;
    }

    /**
     * setGroupTag6.
     *
     * @param groupTag6 {@link String}
     */
    public void setGroupTag6(final String groupTag6) {
        this.groupTag6 = groupTag6;
    }

    /**
     * getGroupSize.
     *
     * @return {@link Integer}
     */
    public int getGroupSize() {
        return this.size;
    }

    /**
     * setGroupSize.
     *
     * @param size {@link Integer}
     */
    public void setGroupSize(final int size) {
        this.size = size;
    }

    /**
     * getGroupImageSequenceBegin.
     *
     * @return {@link Integer}
     */
    public int getGroupImageSequenceBegin() {
        return this.sequenceBegin;
    }

    /**
     * setGroupImageSequenceBegin.
     *
     * @param sequenceBegin {@link Integer}
     */
    public void setGroupImageSequenceBegin(final int sequenceBegin) {
        this.sequenceBegin = sequenceBegin;
    }
}
