/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.cognos.server;


import org.odpi.openmetadata.accessservices.cognos.admin.CognosAdmin;
import org.odpi.openmetadata.adminservices.configuration.OMAGAccessServiceRegistration;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceRegistration;
import org.odpi.openmetadata.adminservices.configuration.registration.ServiceOperationalStatus;

class CognosOMASRegistration {

    CognosOMASRegistration() {

        AccessServiceDescription myDescription = AccessServiceDescription.COGNOS_OMAS;
        AccessServiceRegistration myRegistration = new AccessServiceRegistration(myDescription,
        		ServiceOperationalStatus.ENABLED,
                CognosAdmin.class.getName()

        );
        OMAGAccessServiceRegistration.registerAccessService(myRegistration);
    }
}
