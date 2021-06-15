/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */


package org.odpi.openmetadata.accessservices.analyticsmodeling.api;

import org.odpi.openmetadata.accessservices.analyticsmodeling.responses.AnalyticsModelingOMASAPIResponse;
import org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.model.AnalyticsAsset;

public interface AnalyticsModelingSynchronization {

	/**
	 * Create analytics artifact defined as json input.
	 * @param userId requested the operation.
	 * @param serverCapability source where artifact persist.
	 * @param artifact definition.
	 * @return response with artifact or error description.
	 */
	public AnalyticsModelingOMASAPIResponse createArtifact(String userId, String serverCapability, String artifact);

	/**
	 * Update analytics artifact defined as json input.
	 * @param userId requested the operation.
	 * @param serverCapability source where artifact persist.
	 * @param artifact definition.
	 * @return response with artifact or error description.
	 */
	public AnalyticsModelingOMASAPIResponse updateArtifact(String userId, String serverCapability, AnalyticsAsset artifact);
	
    /**
	 * Delete assets in repository defined by artifact unique identifier.
     * @param userId      request user
	 * @param serverCapability where the artifact is stored.
	 * @param identifier of the artifact in 3rd party system.
	 * @return errors or list of created assets.
	 */
	public AnalyticsModelingOMASAPIResponse deleteArtifact(String userId, String serverCapability, String identifier);

}
