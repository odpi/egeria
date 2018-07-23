/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.informationview.server.spring;


import org.odpi.openmetadata.accessservices.informationview.admin.InformationViewAdmin;
import org.odpi.openmetadata.accessservices.informationview.connectors.InformationViewTopicConnectorProvider;
import org.odpi.openmetadata.adminservices.OMAGAccessServiceRegistration;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceOperationalStatus;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceRegistration;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class InformationViewOMASResource {

    public InformationViewOMASResource() {

        AccessServiceDescription myDescription = AccessServiceDescription.INFORMATION_VIEW_OMAS;
        AccessServiceRegistration myRegistration = new AccessServiceRegistration(myDescription.getAccessServiceCode(),
                myDescription.getAccessServiceName(),
                myDescription.getAccessServiceDescription(),
                myDescription.getAccessServiceWiki(),
                AccessServiceOperationalStatus.ENABLED,
//                InformationViewTopicConnectorProvider.class.getName(),
                InformationViewAdmin.class.getName()

        );
        OMAGAccessServiceRegistration.registerAccessService(myRegistration);

    }
}
