/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.analyticsmodeling.server;

import org.odpi.openmetadata.accessservices.analyticsmodeling.admin.AnalyticsModelingAdmin;
import org.odpi.openmetadata.adminservices.configuration.OMAGAccessServiceRegistration;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceRegistration;
import org.odpi.openmetadata.adminservices.configuration.registration.ServiceOperationalStatus;

/**
 * AnalyticsModelingOMASRegistration registers the access service with the OMAG Server
 * administration services. This registration must be driven once at server
 * start up. The OMAG Server administration services then use this registration
 * information as confirmation that there is an implementation of this access
 * service in the server and it can be configured and used.
 */
class AnalyticsModelingOMASRegistration {

	/**
	 * Pass information about this access service to the OMAG Server administration
	 * services.
	 */
	static void registerAccessService() {
		AccessServiceDescription myDescription = AccessServiceDescription.ANALYTICS_MODELING_OMAS;
		AccessServiceRegistration myRegistration = new AccessServiceRegistration(myDescription,
				ServiceOperationalStatus.ENABLED, AnalyticsModelingAdmin.class.getName());

		OMAGAccessServiceRegistration.registerAccessService(myRegistration);
	}

}
