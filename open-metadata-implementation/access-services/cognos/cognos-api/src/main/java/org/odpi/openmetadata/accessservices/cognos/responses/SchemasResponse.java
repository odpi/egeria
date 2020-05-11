/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.cognos.responses;

import java.util.List;

import org.odpi.openmetadata.accessservices.cognos.model.ResponseContainerDatabaseSchema;

public class SchemasResponse extends CognosOMASAPIResponse{

    public void setSchemaList(List<ResponseContainerDatabaseSchema> databasesSchemas) {
        this.setData(databasesSchemas);
    }

}

