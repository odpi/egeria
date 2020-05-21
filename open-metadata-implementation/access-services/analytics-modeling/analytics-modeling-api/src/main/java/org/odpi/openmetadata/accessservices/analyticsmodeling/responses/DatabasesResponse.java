/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.analyticsmodeling.responses;

import java.util.List;

import org.odpi.openmetadata.accessservices.analyticsmodeling.model.ResponseContainerDatabase;

/**
 * Response for request of the available databases. 
 *
 */
public class DatabasesResponse extends AnalyticsModelingOMASAPIResponse{

	/**
	 * Set list of databases as response content.
	 * @param databases set as the response.
	 */
    public void setDatabasesList(List<ResponseContainerDatabase> databases) {
        this.setData(databases);
    }

}

