/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */


/**
 * Response for request of the tables of the schema. 
 * 
 *
 */

package org.odpi.openmetadata.accessservices.analyticsmodeling.responses;

import java.util.Arrays;

import org.odpi.openmetadata.accessservices.analyticsmodeling.model.ResponseContainerSchemaTables;

public class SchemaTablesResponse extends AnalyticsModelingOMASAPIResponse {
	
	/**
	 * Set list of tables of the schema.
	 * @param tables to set.
	 */
    public void setTableList(ResponseContainerSchemaTables tables) {
        this.setData(Arrays.asList(tables));
    }
}
