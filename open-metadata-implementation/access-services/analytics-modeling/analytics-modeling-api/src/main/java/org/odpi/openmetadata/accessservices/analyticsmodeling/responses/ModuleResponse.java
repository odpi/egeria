/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.analyticsmodeling.responses;

import java.util.Arrays;

import org.odpi.openmetadata.accessservices.analyticsmodeling.model.ResponseContainerModule;

/**
 * Response for request of the Analytics Modeling module. 
 *
 */

public class ModuleResponse extends AnalyticsModelingOMASAPIResponse {

	/**
	 * Set module definition.
	 * @param module definition.
	 */
	public void setModule(ResponseContainerModule module) {
        this.setData(Arrays.asList(module));
    }

}
