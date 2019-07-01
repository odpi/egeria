/*
 *  SPDX-License-Identifier: Apache-2.0
 *  Copyright Contributors to the ODPi Egeria project.
 */

package org.odpi.openmetadata.accessservices.securityofficer.api.events;

import java.io.Serializable;

public enum SecurityOfficerEventType implements Serializable {

    NEW_SECURITY_ASSIGNMENT(0, "NewSecurityAssignment", "A Security Tags assigment should be processes."),
    UPDATED_SECURITY_TAGS(1, "UpdatedSecurityTags", "A Security Tags classification has been changed at schema element level"),
    UNKNOWN_SECURITY_OFFICER_EVENT(4, "UnknownSecurityOfficerEvent", "A Security Officer event that is not recognized by the local handlers.");

    private static final long serialVersionUID = 1L;

    private int eventTypeCode;
    private String eventTypeName;
    private String eventTypeDescription;

    SecurityOfficerEventType(int eventTypeCode, String eventTypeName, String eventTypeDescription) {
        this.eventTypeCode = eventTypeCode;
        this.eventTypeName = eventTypeName;
        this.eventTypeDescription = eventTypeDescription;
    }

    public int getEventTypeCode() {
        return eventTypeCode;
    }

    public String getEventTypeName() {
        return eventTypeName;
    }

    public String getEventTypeDescription() {
        return eventTypeDescription;
    }
}
