/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.openlineage.admin.handlers;

import org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.PropertyServerException;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.UserNotAuthorizedException;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstanceHandler;
import org.odpi.openmetadata.governanceservers.openlineage.client.OpenLineageClient;
import org.odpi.openmetadata.userinterface.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.userinterface.common.ffdc.DependantServerNotAvailableException;
import org.odpi.openmetadata.userinterface.common.ffdc.UserInterfaceErrorCode;
import org.odpi.openmetadata.viewservices.openlineage.admin.registration.OpenLineageViewRegistration;
import org.odpi.openmetadata.viewservices.openlineage.admin.serviceinstances.OpenLineageViewServicesInstance;

/**
 * OpenLineageViewInstanceHandler retrieves information from the instance map for the
 * access service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the OpenLineageViewAdmin class.
 */
public class OpenLineageViewInstanceHandler extends OMVSServiceInstanceHandler
{
    /**
     * Default constructor registers the view service
     */
    public OpenLineageViewInstanceHandler() {
        super(ViewServiceDescription.OPEN_LINEAGE.getViewServiceName());
        OpenLineageViewRegistration.registerViewService();
    }
    /**
     * This serverName has an associated metadata server. This call returns that metadata servers's name.
     * @param serverName  name of the server that the request is for
     * @param userId local server userid
     * @param serviceOperationName service operation - usually the top level rest call
     * @return String Metadata server name
     * @throws DependantServerNotAvailableException Metadata server uncontactable
     *
     */
    public String getMetadataServerName(String serverName, String userId, String serviceOperationName) throws DependantServerNotAvailableException,
            InvalidParameterException,
            PropertyServerException,
            UserNotAuthorizedException {
        OpenLineageViewServicesInstance instance = getOpenLineageViewServicesInstance(userId,serverName,serviceOperationName);
        return instance.getMetadataServerName();
    }
    /**
     * This serverName has an associated metadata server. This call returns that metadata servers's URL.
     * @param serverName  name of the server that the request is for
     * @param userId local server userid
     * @param serviceOperationName service operation - usually the top level rest call
     * @return String Metadata server URL
     * @throws DependantServerNotAvailableException Metadata server uncontactable
     */
    public String getMetadataServerURL(String serverName, String userId, String serviceOperationName )
            throws DependantServerNotAvailableException,
            InvalidParameterException,
            PropertyServerException,
            UserNotAuthorizedException {
        OpenLineageViewServicesInstance instance = getOpenLineageViewServicesInstance(userId,serverName,serviceOperationName);
        return instance.getMetadataServerURL();
    }

    /**
     * Get the open lineage client
     * @param serverName local UI server name
     * @param userId user id
     * @param serviceOperationName service operation name
     * @return open lineage client
     */
    public OpenLineageClient getOpenLineageClient(String serverName, String userId, String serviceOperationName) {
        OpenLineageViewServicesInstance instance = null;
        try {
            instance = getOpenLineageViewServicesInstance(userId,serverName,serviceOperationName);
        } catch (InvalidParameterException e) {
           //TODO
        } catch (PropertyServerException e) {
          // TODO
        } catch (UserNotAuthorizedException e) {
           // TODO
        } catch (DependantServerNotAvailableException e) {
           // TODO
        }
        return instance.getOpenLineageClient();
    }

    /**
     *
     * @param userId local server userid
     * @param serverName name of the server that the request is for
     * @param serviceOperationName service operation - usually the top level rest call
     * @return OpenLineageViewServicesInstance instance for this tenant to use.
     * @throws InvalidParameterException
     * @throws PropertyServerException
     * @throws UserNotAuthorizedException
     * @throws DependantServerNotAvailableException
     */
    private OpenLineageViewServicesInstance getOpenLineageViewServicesInstance(String userId, String serverName, String serviceOperationName)
            throws InvalidParameterException,
            PropertyServerException,
            UserNotAuthorizedException,
            DependantServerNotAvailableException {
        OpenLineageViewServicesInstance instance = (OpenLineageViewServicesInstance)
                super.getServerServiceInstance(userId,
                        serverName,
                        serviceOperationName);

        if (instance == null) {

            final String methodName = "getOpenLineageViewServicesInstance";

            UserInterfaceErrorCode errorCode = UserInterfaceErrorCode.SERVICE_NOT_INITIALIZED;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverName, ViewServiceDescription.SUBJECT_AREA.getViewServiceName(), methodName);

            throw new DependantServerNotAvailableException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }
        return instance;
    }

}
