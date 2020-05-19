/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.cognos.server;

import org.odpi.openmetadata.accessservices.cognos.assets.DatabaseContextHandler;
import org.odpi.openmetadata.accessservices.cognos.ffdc.exceptions.CognosCheckedException;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.PropertyServerException;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.UserNotAuthorizedException;
import org.odpi.openmetadata.commonservices.multitenant.OCFOMASServiceInstanceHandler;
import org.odpi.openmetadata.accessservices.cognos.ffdc.CognosErrorCode;

/**
 * CognosInstanceHandler retrieves information from the instance map for the
 * access service instances. The instance map is thread-safe. Instances are
 * added and removed by the CognosAdmin class.
 */
class CognosInstanceHandler extends OCFOMASServiceInstanceHandler {

	/**
	 * Default constructor registers the access service
	 */
	CognosInstanceHandler() {
        super(AccessServiceDescription.COGNOS_OMAS.getAccessServiceFullName());

        CognosOMASRegistration.registerAccessService();
	}

	/**
	 * Retrieve the handler for retrieving assets details for the access service.
	 *
	 * @param serverName name of the server tied to the request
	 * @param userId of the request
	 * @param serviceOperationName context
	 * @return database handler for exclusive use by the requested instance
	 * @throws CognosCheckedException if server is not initialized.
	 */

	DatabaseContextHandler getDatabaseContextHandler(String serverName, String userId, String serviceOperationName)
			throws CognosCheckedException {
		
		try {
			CognosServicesInstance instance = (CognosServicesInstance)
					super.getServerServiceInstance(userId, serverName, serviceOperationName);
			if (instance != null) {
				return instance.getContextBuilder();
			}
		} catch (InvalidParameterException | UserNotAuthorizedException | PropertyServerException error) {
			throw new CognosCheckedException(
					CognosErrorCode.SERVICE_INSTANCE_FAILURE.getMessageDefinition(serverName, userId, serviceOperationName),
					this.getClass().getSimpleName(),
					serviceOperationName, 
					error);
		}

		throw new CognosCheckedException(
				CognosErrorCode.SERVICE_NOT_INITIALIZED.getMessageDefinition(serverName),
				this.getClass().getSimpleName(),
				serviceOperationName);
	}

}
