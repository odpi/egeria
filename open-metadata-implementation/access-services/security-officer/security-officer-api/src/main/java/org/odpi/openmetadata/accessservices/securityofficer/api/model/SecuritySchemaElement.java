/*
 *  SPDX-License-Identifier: Apache-2.0
 *  Copyright Contributors to the ODPi Egeria project.
 */
package org.odpi.openmetadata.accessservices.securityofficer.api.model;

import java.io.Serializable;

public class SecuritySchemaElement extends Entity implements Serializable {

    private static final long serialVersionUID = 1L;

    private SecurityClassification securityClassification;

    public SecurityClassification getSecurityClassification() {
        return securityClassification;
    }

    public void setSecurityClassification(SecurityClassification securityClassification) {
        this.securityClassification = securityClassification;
    }

    @Override
    public String toString() {
        return "SecuritySchemaElement{" +
                "securityClassification=" + securityClassification +
                '}';
    }
}
