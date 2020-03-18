/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceengine.api.model;

import java.io.Serializable;

public class SoftwareServerCapabilityResponse extends GovernanceEngineOMASResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private SoftwareServerCapability serverCapability;

    public SoftwareServerCapability getServerCapability() {
        return serverCapability;
    }

    public void setServerCapability(SoftwareServerCapability serverCapability) {
        this.serverCapability = serverCapability;
    }
}
