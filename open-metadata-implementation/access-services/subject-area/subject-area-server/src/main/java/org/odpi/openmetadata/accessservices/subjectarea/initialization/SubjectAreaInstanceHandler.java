/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.initialization;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.server.services.SubjectAreaServicesInstance;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.MetadataServerUncontactableException;

import java.util.List;

/**
 * SubjectAreaInstanceHandler retrieves information from the instance map for the
 * access service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the SubjectAreaAdmin class.
 */
public class SubjectAreaInstanceHandler
{
    private static SubjectAreaServicesInstanceMap instanceMap   = new SubjectAreaServicesInstanceMap();
    private static AccessServiceDescription   myDescription = AccessServiceDescription.SUBJECT_AREA_OMAS;


    /**
     * Default constructor registers the access service
     */
    public SubjectAreaInstanceHandler() {
        SubjectAreaRegistration.registerAccessService();
    }
    /**
     * Return the Subject Area's official Access Service Name
     *
     * @param serverName  name of the server that the request is for
     * @return String name
     * @throws MetadataServerUncontactableException no available instance for the requested server
     */
    public String  getAccessServiceName(String serverName) throws MetadataServerUncontactableException {
        SubjectAreaServicesInstance instance = instanceMap.getInstance(serverName);

        if (instance != null) {
            return instance.getAccessServiceName();
        } else {
            final String methodName = "getAccessServiceName";

            SubjectAreaErrorCode errorCode    = SubjectAreaErrorCode.SERVICE_NOT_INITIALIZED;
            String                    errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverName, methodName);

            throw new MetadataServerUncontactableException(errorCode.getHTTPErrorCode(),
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
     * @return metadata collection for exclusive use by the requested instance
     * @throws MetadataServerUncontactableException no available instance for the requested server
     */
    OMRSMetadataCollection getMetadataCollection(String serverName) throws MetadataServerUncontactableException {
        SubjectAreaServicesInstance instance = instanceMap.getInstance(serverName);

        if (instance != null) {
            return instance.getMetadataCollection();
        } else {
            final String methodName = "getMetadataCollection";

            SubjectAreaErrorCode errorCode    = SubjectAreaErrorCode.SERVICE_NOT_INITIALIZED;
            String                    errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverName, methodName);

            throw new MetadataServerUncontactableException(errorCode.getHTTPErrorCode(),
                                              this.getClass().getName(),
                                              methodName,
                                              errorMessage,
                                              errorCode.getSystemAction(),
                                              errorCode.getUserAction());
        }
    }


    /**
     * Return the repository connector for this server.
     *
     * @param serverName         serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @return OMRSRepositoryConnector object
     * @throws MetadataServerUncontactableException the instance has not been initialized successfully
     */
    public OMRSRepositoryConnector getRepositoryConnector(String  serverName) throws MetadataServerUncontactableException
    {
        SubjectAreaServicesInstance instance = instanceMap.getInstance(serverName);

        if (instance != null) {
            return instance.getRepositoryConnector();
        } else {
            final String methodName = "getRepositoryConnector";

            SubjectAreaErrorCode errorCode    = SubjectAreaErrorCode.SERVICE_NOT_INITIALIZED;
            String                    errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverName, methodName);

            throw new MetadataServerUncontactableException(errorCode.getHTTPErrorCode(),
                                              this.getClass().getName(),
                                              methodName,
                                              errorMessage,
                                              errorCode.getSystemAction(),
                                              errorCode.getUserAction());
        }
    }
}
