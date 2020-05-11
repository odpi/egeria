/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.cognos.server;

import org.odpi.openmetadata.accessservices.cognos.assets.DatabaseContextHandler;
import org.odpi.openmetadata.accessservices.cognos.ffdc.exceptions.CognosRuntimeException;
import org.odpi.openmetadata.accessservices.cognos.ffdc.CognosErrorCode;

/**
 * CognosInstanceHandler retrieves information from the instance map for the
 * access service instances. The instance map is thread-safe. Instances are
 * added and removed by the CognosAdmin class.
 */
class CognosInstanceHandler {
	private static CognosServicesInstanceMap instanceMap = new CognosServicesInstanceMap();

	/**
	 * Default constructor registers the access service
	 */
	CognosInstanceHandler() {
		new CognosOMASRegistration();
	}

	/**
	 * Retrieve the handler for retrieving assets details for the access service.
	 *
	 * @param serverName name of the server tied to the request
	 * @return assetsHandler for exclusive use by the requested instance
	 */
	DatabaseContextHandler getAssetContextHandler(String serverName) {
		CognosServicesInstance instance = instanceMap.getInstance(serverName);

		if (instance != null) {
			return instance.getContextBuilder();
		}
		throw new CognosRuntimeException(CognosErrorCode.SERVICE_NOT_INITIALIZED, serverName);
	}

}
