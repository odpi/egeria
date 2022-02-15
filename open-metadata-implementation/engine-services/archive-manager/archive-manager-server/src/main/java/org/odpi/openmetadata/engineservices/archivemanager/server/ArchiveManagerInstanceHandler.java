/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.archivemanager.server;

import org.odpi.openmetadata.adminservices.configuration.registration.EngineServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMESServiceInstanceHandler;
import org.odpi.openmetadata.engineservices.archivemanager.handlers.ArchiveEngineHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

/**
 * ArchiveManagerInstanceHandler retrieves information from the instance map for the
 * archive manager engine service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the ArchiveManagerAdmin class.
 */
class ArchiveManagerInstanceHandler extends OMESServiceInstanceHandler
{
    /**
     * Default constructor registers the access service
     */
    ArchiveManagerInstanceHandler()
    {
        super(EngineServiceDescription.ARCHIVE_MANAGER_OMES.getEngineServiceName());

        ArchiveManagerRegistration.registerEngineService();
    }


    /**
     * Retrieve the specific handler for the archive engine.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @param archiveEngineName unique name of the archive engine
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return handler for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    ArchiveEngineHandler getArchiveEngineHandler(String userId,
                                                     String serverName,
                                                     String archiveEngineName,
                                                     String serviceOperationName) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        ArchiveManagerInstance instance = (ArchiveManagerInstance)super.getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getArchiveEngine(archiveEngineName);
        }

        return null;
    }
}
