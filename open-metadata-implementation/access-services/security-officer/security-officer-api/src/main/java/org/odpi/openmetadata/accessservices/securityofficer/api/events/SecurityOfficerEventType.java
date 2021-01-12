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
    NEW_CLASSIFIED_ASSET(3, "ClassifiedAsset", "A governed classification has been assigned to an asset."),
    RE_CLASSIFIED_ASSET(4, "ReClassifiedAsset", "A classification has been changed for governed asset."),
    DE_CLASSIFIED_ASSET(5, "DeClassifiedAsset", "A classification has been deleted for governed asset."),
    DELETED_ASSET(6, "DeletedAsset", "A governed asset has been deleted."),
    UNKNOWN_SECURITY_OFFICER_EVENT(100, "UnknownSecurityOfficerEvent", "A Security Officer event that is not recognized by the local handlers.");

    private static final long serialVersionUID = 1L;

    private int eventTypeCode;
    private String eventTypeName;
    private String eventTypeDescription;

    /**
     * Default Constructor - sets up the specific values for this instance of the enum.
     *
     * @param eventTypeCode        int identifier used for indexing based on the enum.
     * @param eventTypeName        string name used for messages that include the enum.
     * @param eventTypeDescription default description for the enum value - used when natural resource
     *                             bundle is not available.
     */
    SecurityOfficerEventType(int eventTypeCode, String eventTypeName, String eventTypeDescription) {
        this.eventTypeCode = eventTypeCode;
        this.eventTypeName = eventTypeName;
        this.eventTypeDescription = eventTypeDescription;
    }


    /**
     * Return the int identifier used for indexing based on the enum.
     *
     * @return int identifier code
     */
    public int getEventTypeCode() {
        return eventTypeCode;
    }


    /**
     * Return the string name used for messages that include the enum.
     *
     * @return String name
     */
    public String getEventTypeName() {
        return eventTypeName;
    }


    /**
     * Return the default description for the enum value - used when natural resource
     * bundle is not available.
     *
     * @return String default description
     */
    public String getEventTypeDescription() {
        return eventTypeDescription;
    }
}
