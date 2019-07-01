package org.odpi.openmetadata.accessservices.securityofficer.api.events;

import org.odpi.openmetadata.accessservices.securityofficer.api.model.SecurityClassification;

public class SecurityOfficerUpdateTagEvent extends SecurityOfficerEvent {

    private SecurityClassification previousClassification;

    public SecurityClassification getPreviousClassification() {
        return previousClassification;
    }

    public void setPreviousClassification(SecurityClassification previousClassification) {
        this.previousClassification = previousClassification;
    }
}
