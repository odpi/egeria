/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.instances;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.MetadataServerUncontactableException;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.NewInstanceException;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

/**
 * SubjectAreaServicesInstance caches references to OMRS objects for a specific server.
 * It is also responsible for registering itself in the instance map.
 */
public class SubjectAreaServicesInstance
{
    private OMRSRepositoryConnector  repositoryConnector = null;
    private OMRSRepositoryHelper     repositoryHelper    = null;
    private OMRSMetadataCollection   metadataCollection  = null;
    private String                   serverName          = null;
    private AccessServiceDescription myDescription       = AccessServiceDescription.GOVERNANCE_ENGINE_OMAS;


    /**
     * Set up the local repository connector that will service the REST Calls.
     *
     * @param repositoryConnector link to the repository responsible for servicing the REST calls.
     * @throws NewInstanceException a problem occurred during initialization
     */
    public SubjectAreaServicesInstance(OMRSRepositoryConnector repositoryConnector) throws NewInstanceException
    {
        final String methodName = "new ServiceInstance";

        if (repositoryConnector != null) {
            try
            {
                this.repositoryConnector = repositoryConnector;
                this.serverName = repositoryConnector.getServerName();
                this.metadataCollection = repositoryConnector.getMetadataCollection();
                this.repositoryHelper = repositoryConnector.getRepositoryHelper();

                SubjectAreaServicesInstanceMap.setNewInstanceForJVM(serverName, this);
            } catch (Throwable error) {
                SubjectAreaErrorCode errorCode    = SubjectAreaErrorCode.OMRS_NOT_INITIALIZED;
                String               errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName);

                throw new NewInstanceException(errorCode.getHTTPErrorCode(),
                                               this.getClass().getName(),
                                               methodName,
                                               errorMessage,
                                               errorCode.getSystemAction(),
                                               errorCode.getUserAction());

            }
        } else {
            SubjectAreaErrorCode errorCode    = SubjectAreaErrorCode.OMRS_NOT_INITIALIZED;
            String                errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName);

            throw new NewInstanceException(errorCode.getHTTPErrorCode(),
                                           this.getClass().getName(),
                                           methodName,
                                           errorMessage,
                                           errorCode.getSystemAction(),
                                           errorCode.getUserAction());

        }
    }


    /**
     * Return the server name.
     *
     * @return serverName name of the server for this instance
     * @throws NewInstanceException a problem occurred during initialization
     */
    public String getServerName() throws NewInstanceException {
        final String methodName = "getServerName";

        if (serverName != null) {
            return serverName;
        } else {
            SubjectAreaErrorCode errorCode    = SubjectAreaErrorCode.OMRS_NOT_AVAILABLE;
            String                errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName);

            throw new NewInstanceException(errorCode.getHTTPErrorCode(),
                                           this.getClass().getName(),
                                           methodName,
                                           errorMessage,
                                           errorCode.getSystemAction(),
                                           errorCode.getUserAction());
        }
    }


    /**
     * Return the Governance Engine's official Access Service Name
     *
     * @return String name
     */
    public String  getAccessServiceName()
    {
        return myDescription.getAccessServiceName();
    }


    /**
     * Return the repository helper for this server.
     *
     * @return OMRSRepositoryHelper object
     * @throws MetadataServerUncontactableException the instance has not been initialized successfully
     */
    public OMRSRepositoryHelper getRepositoryHelper() throws MetadataServerUncontactableException {
        final String methodName = "getRepositoryHelper";

        if ((repositoryConnector != null) && (repositoryHelper != null) && (repositoryConnector.isActive())) {
            return repositoryHelper;
        } else {
            SubjectAreaErrorCode errorCode    = SubjectAreaErrorCode.OMRS_NOT_AVAILABLE;
            String                errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName);

            throw new MetadataServerUncontactableException(errorCode.getHTTPErrorCode(),
                                              this.getClass().getName(),
                                              methodName,
                                              errorMessage,
                                              errorCode.getSystemAction(),
                                              errorCode.getUserAction());
        }
    }

    /**
     * Return the local metadata collection for this server.
     *
     * @return OMRSMetadataCollection object
     * @throws MetadataServerUncontactableException the instance has not been initialized successfully
     */
    public OMRSMetadataCollection getMetadataCollection() throws MetadataServerUncontactableException {
        final String methodName = "getMetadataCollection";

        if ((repositoryConnector != null) && (metadataCollection != null) && (repositoryConnector.isActive())) {
            return metadataCollection;
        } else {
            SubjectAreaErrorCode errorCode    = SubjectAreaErrorCode.OMRS_NOT_AVAILABLE;
            String                errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName);

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
    public OMRSRepositoryConnector getRepositoryConnector() throws MetadataServerUncontactableException {
        final String methodName = "getRepositoryConnector";

        if ((repositoryConnector != null) && (metadataCollection != null) && (repositoryConnector.isActive())) {
            return repositoryConnector;
        } else {
            SubjectAreaErrorCode errorCode    = SubjectAreaErrorCode.OMRS_NOT_AVAILABLE;
            String                errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName);

            throw new MetadataServerUncontactableException(errorCode.getHTTPErrorCode(),
                                              this.getClass().getName(),
                                              methodName,
                                              errorMessage,
                                              errorCode.getSystemAction(),
                                              errorCode.getUserAction());
        }
    }


    /**
     * Unregister this instance from the instance map.
     */
    public void shutdown() {
        SubjectAreaServicesInstanceMap.removeInstanceForJVM(serverName);
    }
}
