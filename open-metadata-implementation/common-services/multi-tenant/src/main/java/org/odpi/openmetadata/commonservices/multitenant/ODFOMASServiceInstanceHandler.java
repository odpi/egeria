/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.multitenant;

import org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.PropertyServerException;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.UserNotAuthorizedException;
import org.odpi.openmetadata.commonservices.odf.metadatamanagement.handlers.AnnotationHandler;
import org.odpi.openmetadata.commonservices.odf.metadatamanagement.handlers.DataFieldHandler;
import org.odpi.openmetadata.commonservices.odf.metadatamanagement.handlers.DiscoveryAnalysisReportHandler;

/**
 * ODFOMASServiceInstanceHandler provides access to the specialized handlers for
 * the open discovery framework (ODF)
 */
public class ODFOMASServiceInstanceHandler extends OCFOMASServiceInstanceHandler
{
    /**
     * Constructor
     *
     * @param serviceName name of the service
     */
    public ODFOMASServiceInstanceHandler(String serviceName)
    {
        super(serviceName);
    }


    /**
     * Retrieve the specific handler for the access service.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return handler for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException error in the requested server
     */
    public DiscoveryAnalysisReportHandler getDiscoveryAnalysisReportHandler(String userId,
                                                                            String serverName,
                                                                            String serviceOperationName) throws InvalidParameterException,
                                                                                                                UserNotAuthorizedException,
                                                                                                                PropertyServerException
    {

        ODFOMASServiceInstance instance = (ODFOMASServiceInstance)super.getServerServiceInstance(userId,
                                                                                                 serverName,
                                                                                                 serviceOperationName);

        if (instance != null)
        {
            return instance.getDiscoveryAnalysisReportHandler();
        }

        return null;
    }


    /**
     * Retrieve the specific handler for the access service.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return handler for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException error in the requested server
     */
    public AnnotationHandler getAnnotationHandler(String userId,
                                                  String serverName,
                                                  String serviceOperationName) throws InvalidParameterException,
                                                                                                                UserNotAuthorizedException,
                                                                                                                PropertyServerException
    {
        ODFOMASServiceInstance instance = (ODFOMASServiceInstance)super.getServerServiceInstance(userId,
                                                                                                 serverName,
                                                                                                 serviceOperationName);

        if (instance != null)
        {
            return instance.getAnnotationHandler();
        }

        return null;
    }


    /**
     * Retrieve the specific handler for the access service.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return handler for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException error in the requested server
     */
    public DataFieldHandler getDataFieldHandler(String userId,
                                                String serverName,
                                                String serviceOperationName) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        ODFOMASServiceInstance instance = (ODFOMASServiceInstance)super.getServerServiceInstance(userId,
                                                                                                 serverName,
                                                                                                 serviceOperationName);

        if (instance != null)
        {
            return instance.getDataFieldHandler();
        }

        return null;
    }
}
