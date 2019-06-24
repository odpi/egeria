/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.responses;

import org.odpi.openmetadata.accessservices.informationview.events.SoftwareServerCapabilitySource;

public class RegistrationResponse extends InformationViewOMASAPIResponse {

  private SoftwareServerCapabilitySource softwareServerCapabilitySource;

    public SoftwareServerCapabilitySource getSoftwareServerCapabilitySource() {
        return softwareServerCapabilitySource;
    }

    public void setSoftwareServerCapabilitySource(SoftwareServerCapabilitySource softwareServerCapabilitySource) {
        this.softwareServerCapabilitySource = softwareServerCapabilitySource;
    }

    @Override
    public String toString() {
        return "{" +
                "softwareServerCapabilitySource=" + softwareServerCapabilitySource +
                '}';
    }
}
