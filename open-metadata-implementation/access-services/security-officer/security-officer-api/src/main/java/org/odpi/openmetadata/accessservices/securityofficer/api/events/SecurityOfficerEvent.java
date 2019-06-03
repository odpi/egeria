/*
 *  SPDX-License-Identifier: Apache-2.0
 *  Copyright Contributors to the ODPi Egeria project.
 */

package org.odpi.openmetadata.accessservices.securityofficer.api.events;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.securityofficer.api.model.SchemaElementEntity;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SecurityOfficerEvent describes the structure of the events emitted by the Security Officer OMAS.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SecurityOfficerEvent implements Serializable {

    private static final long serialVersionUID = 1L;
    private SecurityOfficerEventType eventType = SecurityOfficerEventType.UNKNOWN_SECURITY_OFFICER_EVENT;
    private SchemaElementEntity schemaElementEntity;

    public SecurityOfficerEventType getEventType() {
        return eventType;
    }

    public void setEventType(SecurityOfficerEventType eventType) {
        this.eventType = eventType;
    }

    public SchemaElementEntity getSchemaElementEntity() {
        return schemaElementEntity;
    }

    public void setSchemaElementEntity(SchemaElementEntity schemaElementEntity) {
        this.schemaElementEntity = schemaElementEntity;
    }
}