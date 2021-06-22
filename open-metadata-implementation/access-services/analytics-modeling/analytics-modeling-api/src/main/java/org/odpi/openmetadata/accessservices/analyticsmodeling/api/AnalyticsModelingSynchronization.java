/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */


package org.odpi.openmetadata.accessservices.analyticsmodeling.api;

import org.odpi.openmetadata.accessservices.analyticsmodeling.ffdc.exceptions.AnalyticsModelingCheckedException;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.ResponseContainerAssets;
import org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.model.AnalyticsAsset;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;

public interface AnalyticsModelingSynchronization {

	/**
	 * Create analytics artifact defined as input.
	 * @param userId requested the operation.
	 * @param serverCapability source where artifact persist.
	 * @param artifact definition.
	 * @return response with artifact or error description.
	 * @throws PropertyServerException in case REST call failed.
	 * @throws AnalyticsModelingCheckedException error executing request.
	 */
	public ResponseContainerAssets createArtifact(String userId, String serverCapability, AnalyticsAsset artifact)
			throws PropertyServerException, AnalyticsModelingCheckedException;

	/**
	 * Update analytics artifact defined as input.
	 * @param userId requested the operation.
	 * @param serverCapability source where artifact persist.
	 * @param artifact definition.
	 * @return response with artifact or error description.
	 * @throws PropertyServerException in case REST call failed.
	 * @throws AnalyticsModelingCheckedException error executing request.
	 */
	public ResponseContainerAssets updateArtifact(String userId, String serverCapability, AnalyticsAsset artifact)
			throws PropertyServerException, AnalyticsModelingCheckedException;
	
    /**
	 * Delete assets in repository defined by artifact unique identifier.
     * @param userId      request user
	 * @param serverCapability where the artifact is stored.
	 * @param identifier of the artifact in 3rd party system.
	 * @return errors or list of created assets.
	 * @throws PropertyServerException in case REST call failed.
	 * @throws AnalyticsModelingCheckedException error executing request.
	 */
	public ResponseContainerAssets deleteArtifact(String userId, String serverCapability, String identifier)
			throws PropertyServerException, AnalyticsModelingCheckedException;

}
