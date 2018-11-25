/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.instances;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.MetadataServerUncontactableException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

/**
 * SubjectAreaInstanceHandler retrieves information from the instance map for the
 * access service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the SubjectAreaAdmin class.
 */
public class SubjectAreaInstanceHandler
{
    private static SubjectAreaServicesInstanceMap   instanceMap = new SubjectAreaServicesInstanceMap();

    /**
     * Default constructor registers the access service
     */
    public SubjectAreaInstanceHandler() {
        SubjectAreaRegistration.registerAccessService();
    }


    /**
     * Return the Subject Area's official Access Service Name
     *
     * @param serverName name of the server tied to the request
     * @return String name
     */
    public String  getAccessServiceName(String serverName) {
        SubjectAreaServicesInstance instance = instanceMap.getInstance(serverName);

        if (instance != null) {
            return instance.getAccessServiceName();
        } else {
            return "Subject Area OMAS";
        }
    }


    /**
     * Retrieve the metadata collection for the access service.
     *
     * @param serverName name of the server tied to the request
     * @return metadata collection for exclusive use by the requested instance
     * @throws MetadataServerUncontactableException no available instance for the requested server
     */
    public OMRSMetadataCollection getMetadataCollection(String serverName) throws MetadataServerUncontactableException {
        SubjectAreaServicesInstance instance = instanceMap.getInstance(serverName);

        if (instance != null) {
            return instance.getMetadataCollection();
        } else {
            final String methodName = "getMetadataCollection";

            SubjectAreaErrorCode errorCode    = SubjectAreaErrorCode.SERVICE_NOT_INITIALIZED;
            String               errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverName);

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
     * @return OMRSRepositoryConnector object
     * @throws MetadataServerUncontactableException the instance has not been initialized successfully
     */
    public OMRSRepositoryConnector getRepositoryConnector(String  serverName) throws MetadataServerUncontactableException {
        SubjectAreaServicesInstance instance = instanceMap.getInstance(serverName);

        if (instance != null) {
            return instance.getRepositoryConnector();
        } else {
            final String methodName = "getRepositoryConnector";

            SubjectAreaErrorCode errorCode    = SubjectAreaErrorCode.SERVICE_NOT_INITIALIZED;
            String               errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverName);

            throw new MetadataServerUncontactableException(errorCode.getHTTPErrorCode(),
                                              this.getClass().getName(),
                                              methodName,
                                              errorMessage,
                                              errorCode.getSystemAction(),
                                              errorCode.getUserAction());
        }
    }
}
