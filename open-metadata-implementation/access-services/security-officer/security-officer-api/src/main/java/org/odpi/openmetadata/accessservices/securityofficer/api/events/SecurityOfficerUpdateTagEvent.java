/*
 *  SPDX-License-Identifier: Apache-2.0
 *  Copyright Contributors to the ODPi Egeria project.
 */
package org.odpi.openmetadata.accessservices.securityofficer.api.events;

import org.odpi.openmetadata.accessservices.securityofficer.api.model.SecurityClassification;
import org.odpi.openmetadata.accessservices.securityofficer.api.model.SecuritySchemaElement;

public class SecurityOfficerUpdateTagEvent extends SecurityOfficerEvent {

    private SecuritySchemaElement securitySchemaElement;
    private SecurityClassification previousClassification;

    public SecuritySchemaElement getSecuritySchemaElement() {
        return securitySchemaElement;
    }

    public void setSecuritySchemaElement(SecuritySchemaElement securitySchemaElement) {
        this.securitySchemaElement = securitySchemaElement;
    }

    public SecurityClassification getPreviousClassification() {
        return previousClassification;
    }

    public void setPreviousClassification(SecurityClassification previousClassification) {
        this.previousClassification = previousClassification;
    }
}
