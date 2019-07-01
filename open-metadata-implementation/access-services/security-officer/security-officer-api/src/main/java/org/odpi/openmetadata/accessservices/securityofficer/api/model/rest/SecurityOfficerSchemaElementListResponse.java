/*
 *  SPDX-License-Identifier: Apache-2.0
 *  Copyright Contributors to the ODPi Egeria project.
 */
package org.odpi.openmetadata.accessservices.securityofficer.api.model.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.securityofficer.api.model.SchemaElementEntity;

import java.io.Serializable;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SecurityOfficerSchemaElementListResponse extends SecurityOfficerOMASAPIResponse implements Serializable {

    private static final long serialVersionUID = 1L;
    private List<SchemaElementEntity> schemaElementEntityList;

    public List<SchemaElementEntity> getSchemaElementEntityList() {
        return schemaElementEntityList;
    }

    public void setSchemaElementEntityList(List<SchemaElementEntity> schemaElementEntityList) {
        this.schemaElementEntityList = schemaElementEntityList;
    }
}
