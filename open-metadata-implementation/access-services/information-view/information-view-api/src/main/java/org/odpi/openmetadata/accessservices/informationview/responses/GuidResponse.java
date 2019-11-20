/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.responses;

public class GuidResponse extends InformationViewOMASAPIResponse{

    private String guid;

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    @Override
    public String toString() {
        return "GuidResponse{" +
                "guid='" + guid + '\'' +
                '}';
    }
}
