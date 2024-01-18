/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.surveyaction.server;


import org.odpi.openmetadata.adminservices.configuration.registration.EngineServiceRegistrationEntry;
import org.odpi.openmetadata.engineservices.surveyaction.admin.SurveyActionAdmin;
import org.odpi.openmetadata.governanceservers.enginehostservices.registration.OMAGEngineServiceRegistration;
import org.odpi.openmetadata.adminservices.configuration.registration.EngineServiceDescription;
import org.odpi.openmetadata.adminservices.configuration.registration.ServiceOperationalStatus;

/**
 * SurveyActionRegistration registers the engine service with the OMAG Server administration services.
 * This registration must be driven once at server start up.  The OMAG Server administration services
 * then use this registration information as confirmation that there is an implementation of this
 * engine service in the server, and it can be configured and used.
 */
class SurveyActionRegistration
{
    /**
     * Pass information about this engine service to the OMAG Server administration services.
     */
    static void registerEngineService()
    {
        EngineServiceDescription myDescription = EngineServiceDescription.SURVEY_ACTION_OMES;

        EngineServiceRegistrationEntry myRegistration = new EngineServiceRegistrationEntry(myDescription,
                                                                                           ServiceOperationalStatus.ENABLED,
                                                                                           SurveyActionAdmin.class.getName());
        OMAGEngineServiceRegistration.registerEngineService(myRegistration);
    }
}