/*
 *  SPDX-License-Identifier: Apache-2.0
 *  Copyright Contributors to the ODPi Egeria project.
 */
package org.odpi.openmetadata.accessservices.securityofficer.api.events;

import org.odpi.openmetadata.accessservices.securityofficer.api.model.SchemaElementEntity;

public class SecurityOfficerNewTagEvent extends SecurityOfficerEvent {

    private SchemaElementEntity schemaElementEntity;

    public SchemaElementEntity getSchemaElementEntity() {
        return schemaElementEntity;
    }

    public void setSchemaElementEntity(SchemaElementEntity schemaElementEntity) {
        this.schemaElementEntity = schemaElementEntity;
    }
}
