/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.cognos.responses;

import java.util.List;

import org.odpi.openmetadata.accessservices.cognos.model.ResponseContainerDatabase;

/**
 * Response for request of the available databases. 
 *
 */
public class DatabasesResponse extends CognosOMASAPIResponse{

	/**
	 * Set list of databases as response content.
	 * @param databases set as the response.
	 */
    public void setDatabasesList(List<ResponseContainerDatabase> databases) {
        this.setData(databases);
    }

}

