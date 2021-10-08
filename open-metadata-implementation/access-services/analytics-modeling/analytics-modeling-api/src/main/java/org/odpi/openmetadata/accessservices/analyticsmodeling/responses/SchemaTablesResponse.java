/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */


/**
 * Response for request of the tables of the schema. 
 * 
 *
 */

package org.odpi.openmetadata.accessservices.analyticsmodeling.responses;

import java.util.Arrays;
import java.util.List;

import org.odpi.openmetadata.accessservices.analyticsmodeling.model.ResponseContainerSchemaTables;

public class SchemaTablesResponse extends AnalyticsModelingOMASAPIResponse {
	
	private List<ResponseContainerSchemaTables> data;
	/**
	 * Set list of tables of the schema.
	 * @param tables to set.
	 */
    public void setTableList(ResponseContainerSchemaTables tables) {
        data = Arrays.asList(tables);
    }
	/**
	 * Get list of tables of the schema.
	 * @return list of tables.
	 */
    public ResponseContainerSchemaTables getTableList() {
        return (data == null || data.isEmpty()) ? null : (ResponseContainerSchemaTables)data.get(0);
    }
}
