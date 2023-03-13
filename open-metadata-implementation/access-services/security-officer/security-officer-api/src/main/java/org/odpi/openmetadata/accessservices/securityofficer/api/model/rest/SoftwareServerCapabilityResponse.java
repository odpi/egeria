/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.securityofficer.api.model.rest;

import org.odpi.openmetadata.accessservices.securityofficer.api.model.SoftwareServerCapability;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCResponseBase;

import java.io.Serializable;

public class SoftwareServerCapabilityResponse extends SecurityOfficerOMASAPIResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private SoftwareServerCapability serverCapability;

    public SoftwareServerCapability getServerCapability() {
        return serverCapability;
    }

    public void setServerCapability(SoftwareServerCapability serverCapability) {
        this.serverCapability = serverCapability;
    }
}
