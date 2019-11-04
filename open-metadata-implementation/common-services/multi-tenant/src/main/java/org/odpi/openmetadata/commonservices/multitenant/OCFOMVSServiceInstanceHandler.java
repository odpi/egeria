/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.multitenant;

import org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.PropertyServerException;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.UserNotAuthorizedException;

import org.odpi.openmetadata.userinterface.adminservices.configuration.registration.ViewServiceDescription;
/**
 * Provide access to the common handlers for OMVS's that use the OCF beans.
 */
public abstract class OCFOMVSServiceInstanceHandler extends OMVSServiceInstanceHandler
{

    private static final String OMVS = " OMVS";

    /**
     * Constructor
     *
     * @param serviceName name of the service
     */
    public OCFOMVSServiceInstanceHandler(String serviceName)
    {
        super(serviceName);
    }


    /**
     * Return the service's official name
     *
     * @param callingServiceURLName url fragment that indicates the service name
     * @return String name
     */
    public String  getServiceName(String callingServiceURLName)
    {
        final String subjectAreaURLName      = "subject-area";
        final String assetSearchURLName      = "asset-search";
        final String openLineageURLName      = "open-lineage";
        final String typeExplorerURLName     = "type-explorer";

        String callingServiceName;

        if (assetSearchURLName.equals(callingServiceURLName))
        {
            callingServiceName = ViewServiceDescription.ASSET_SEARCH.getViewServiceName() + OMVS;
        }
        else if (subjectAreaURLName.equals(callingServiceURLName))
        {
            callingServiceName = ViewServiceDescription.SUBJECT_AREA.getViewServiceName() + OMVS;
        }
        else if (openLineageURLName.equals(callingServiceURLName))
        {
            callingServiceName = ViewServiceDescription.TYPE_EXPLORER.getViewServiceName() + OMVS;
        }
        else if (typeExplorerURLName.equals(callingServiceURLName))
        {
            callingServiceName = ViewServiceDescription.TYPE_EXPLORER.getViewServiceName() + OMVS;
        }
        else
        {
            callingServiceName = callingServiceURLName;
        }

        return callingServiceName;
    }


    /**
     * Get the instance for a specific service.
     *
     * @param userId calling user
     * @param serverName name of this server
     * @param callingServiceURLName url fragment that indicates the service name
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return specific service instance
     * @throws InvalidParameterException the server name is not known
     * @throws UserNotAuthorizedException the user is not authorized to issue the request.
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    private OMASServiceInstance getCallingServiceInstance(String  userId,
                                                          String  serverName,
                                                          String  callingServiceURLName,
                                                          String  serviceOperationName) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        return (OMASServiceInstance)platformInstanceMap.getServiceInstance(userId,
                                                                           serverName,
                                                                           this.getServiceName(callingServiceURLName),
                                                                           serviceOperationName);
    }
}
