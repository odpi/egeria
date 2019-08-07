/*
 *  SPDX-License-Identifier: Apache-2.0
 *  Copyright Contributors to the ODPi Egeria project.
 */

package org.odpi.openmetadata.accessservices.securityofficer.api.events;

import java.io.Serializable;

public enum SecurityOfficerEventType implements Serializable {

    NEW_SECURITY_ASSIGNMENT(0, "NewSecurityAssignment", "A Security Tags assigment should be processed."),
    UPDATED_SECURITY_ASSIGNMENT(1, "UpdatedSecurityAssigment", "A Security Tags assignment has been updated for schema element."),
    DELETED_SECURITY_ASSIGNMENT(2, "DeletedSecurityAssigment", "A Security Tags assignment has been deleted from schema element."),
    UNKNOWN_SECURITY_OFFICER_EVENT(100, "UnknownSecurityOfficerEvent", "A Security Officer event that is not recognized by the local handlers.");

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
