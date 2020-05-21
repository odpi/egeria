/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.analyticsmodeling.responses;

import java.util.List;

import org.odpi.openmetadata.accessservices.analyticsmodeling.model.ResponseContainerDatabaseSchema;


/**
 * Response for request of the available database schemas. 
 *
 */
public class SchemasResponse extends AnalyticsModelingOMASAPIResponse{

	/**
	 * Set list of schemas available for database.
	 * @param databaseSchemas set as response.
	 */
    public void setSchemaList(List<ResponseContainerDatabaseSchema> databaseSchemas) {
        this.setData(databaseSchemas);
    }

}

