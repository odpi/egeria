/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.server;


import org.odpi.openmetadata.accessservices.governanceprogram.ffdc.GovernanceProgramErrorCode;
import org.odpi.openmetadata.accessservices.governanceprogram.ffdc.exceptions.PropertyServerException;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

/**
 * GovernanceProgramInstanceHandler retrieves information from the instance map for the
 * access service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the GovernanceProgramAdmin class.
 */
class GovernanceProgramInstanceHandler
{
    private static GovernanceProgramServicesInstanceMap instanceMap   = new GovernanceProgramServicesInstanceMap();
    private static AccessServiceDescription             myDescription = AccessServiceDescription.GOVERNANCE_PROGRAM_OMAS;


    /**
     * Default constructor registers the access service
     */
    GovernanceProgramInstanceHandler() {
        GovernanceProgramRegistration.registerAccessService();
    }


    /**
     * Return the Governance Program's official Access Service Name
     *
     * @return String name
     */
    public String  getAccessServiceName()
    {
        return myDescription.getAccessServiceName();
    }



    /**
     * Retrieve the metadata collection for the access service.
     *
     * @param serverName name of the server tied to the request
     * @return metadata collection for exclusive use by the requested instance
     * @throws PropertyServerException no available instance for the requested server
     */
    OMRSMetadataCollection getMetadataCollection(String serverName) throws PropertyServerException
    {
        GovernanceProgramServicesInstance instance = instanceMap.getInstance(serverName);

        if (instance != null)
        {
            return instance.getMetadataCollection();
        }
        else
        {
            final String methodName = "getMetadataCollection";

            GovernanceProgramErrorCode errorCode    = GovernanceProgramErrorCode.SERVICE_NOT_INITIALIZED;
            String                     errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverName, methodName);

            throw new PropertyServerException(errorCode.getHTTPErrorCode(),
                                              this.getClass().getName(),
                                              methodName,
                                              errorMessage,
                                              errorCode.getSystemAction(),
                                              errorCode.getUserAction());
        }
    }


    /**
     * Retrieve the metadata collection for the access service.
     *
     * @param serverName name of the server tied to the request
     * @return repository connector for exclusive use by the requested instance
     * @throws PropertyServerException no available instance for the requested server
     */
    OMRSRepositoryConnector getRepositoryConnector(String serverName) throws PropertyServerException
    {
        GovernanceProgramServicesInstance instance = instanceMap.getInstance(serverName);

        if (instance != null)
        {
            return instance.getRepositoryConnector();
        }
        else
        {
            final String methodName = "getRepositoryConnector";

            GovernanceProgramErrorCode errorCode    = GovernanceProgramErrorCode.SERVICE_NOT_INITIALIZED;
            String                     errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverName, methodName);

            throw new PropertyServerException(errorCode.getHTTPErrorCode(),
                                              this.getClass().getName(),
                                              methodName,
                                              errorMessage,
                                              errorCode.getSystemAction(),
                                              errorCode.getUserAction());
        }
    }
}
