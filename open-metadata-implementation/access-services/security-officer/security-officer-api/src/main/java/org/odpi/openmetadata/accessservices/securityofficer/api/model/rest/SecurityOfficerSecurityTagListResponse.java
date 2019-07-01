/*
 *  SPDX-License-Identifier: Apache-2.0
 *  Copyright Contributors to the ODPi Egeria project.
 */
package org.odpi.openmetadata.accessservices.securityofficer.api.model.rest;

import org.odpi.openmetadata.accessservices.securityofficer.api.model.SecurityClassification;

import java.util.List;

public class SecurityOfficerSecurityTagListResponse extends SecurityOfficerOMASAPIResponse {

    private List<SecurityClassification> securityTags;

    public List<SecurityClassification> getSecurityTags() {
        return securityTags;
    }

    public void setSecurityTags(List<SecurityClassification> securityTags) {
        this.securityTags = securityTags;
    }
}