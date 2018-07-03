/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.governanceengine.server.spring;

import org.apache.log4j.Logger;
import org.odpi.openmetadata.accessservices.governanceengine.server.admin.GovernanceEngineAdmin;
import org.odpi.openmetadata.adminservices.OMAGAccessServiceRegistration;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceOperationalStatus;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceRegistration;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class GovernanceEngineOMASResource {
    private static final org.apache.log4j.Logger log      = Logger.getLogger(GovernanceEngineOMASResource.class);

    /**
     * Constructor
     */
    public GovernanceEngineOMASResource() {

        final String methodName = "initialize";


        log.debug(">>" + methodName);


        AccessServiceDescription myDescription = AccessServiceDescription.GOVERNANCE_ENGINE_OMAS;
        AccessServiceRegistration myRegistration = new AccessServiceRegistration(myDescription.getAccessServiceCode(),
                myDescription.getAccessServiceName(),
                myDescription.getAccessServiceDescription(),
                myDescription.getAccessServiceWiki(),
                AccessServiceOperationalStatus.ENABLED,
                GovernanceEngineAdmin.class.getName()

        );
        OMAGAccessServiceRegistration.registerAccessService(myRegistration);
        log.debug("<<" + methodName);

    }

}
