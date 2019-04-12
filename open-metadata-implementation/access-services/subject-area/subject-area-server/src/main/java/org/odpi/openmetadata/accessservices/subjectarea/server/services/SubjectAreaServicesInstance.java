/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.services;


import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.NewInstanceException;
import org.odpi.openmetadata.accessservices.subjectarea.initialization.SubjectAreaRegistration;
import org.odpi.openmetadata.accessservices.subjectarea.initialization.SubjectAreaServicesInstanceMap;
import org.odpi.openmetadata.adminservices.OMAGAccessServiceRegistration;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceRegistration;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


/**
 * SubjectAreaRESTServicesInstance caches references to OMRS objects for a specific server.
 * It is also responsible for registering itself in the instance map.
 */

public class SubjectAreaServicesInstance
{
    private static final Logger log = LoggerFactory.getLogger(SubjectAreaServicesInstance.class);

    private static final String className = SubjectAreaServicesInstance.class.getName();
    private OMRSRepositoryConnector  repositoryConnector = null;
    private OMRSMetadataCollection   metadataCollection  = null;
    private String                   serverName          = null;

    // The OMRSAPIHelper allows the junits to mock out the omrs layer.
    //protected OMRSAPIHelper oMRSAPIHelper =null;
    //static private String accessServiceName = null;


    /**
     * Set up the local repository connector that will service the REST Calls.
     *
     * @param repositoryConnector link to the repository responsible for servicing the REST calls.
     * @throws NewInstanceException a problem occurred during initialization
     */
    public SubjectAreaServicesInstance(OMRSRepositoryConnector repositoryConnector) throws NewInstanceException
    {
        final String methodName = "new SubjectAreaRESTServicesInstance";

        if (repositoryConnector != null) {
            try
            {
                this.repositoryConnector = repositoryConnector;
                this.serverName = repositoryConnector.getServerName();
                this.metadataCollection = repositoryConnector.getMetadataCollection();

                SubjectAreaServicesInstanceMap.setNewInstanceForJVM(serverName, this);
            } catch (Throwable error) {
                SubjectAreaErrorCode errorCode    = SubjectAreaErrorCode.OMRS_NOT_INITIALIZED;
                String                errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName);

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

    // public void setOMRSAPIHelper(OMRSAPIHelper oMRSAPIHelper) {
//        this.oMRSAPIHelper = oMRSAPIHelper;
//    }


    //    /**
    //     * Provide a connector to the REST Services.
    //     *
    //     * @param accessServiceName    name of this access service
    //     * @param repositoryConnector  OMRS Repository Connector to the property org.odpi.openmetadata.accessservices.subjectarea.server.
    //     */
    //     public void setRepositoryConnector(String accessServiceName,
    //                                              OMRSRepositoryConnector repositoryConnector) {
    //        SubjectAreaRESTServicesInstance.accessServiceName = accessServiceName;
    //        SubjectAreaRESTServicesInstance.repositoryConnector = repositoryConnector;
    //    }
    public OMRSRepositoryConnector getRepositoryConnector() {
        return repositoryConnector;

    }

    /**
     * Default constructor
     */
    public SubjectAreaServicesInstance() {
        boolean initialized = false ;
        List<AccessServiceRegistration> registeredServices = OMAGAccessServiceRegistration.getAccessServiceRegistrationList();
        for (AccessServiceRegistration accessServiceRegistration:registeredServices) {
            if (AccessServiceDescription.SUBJECT_AREA_OMAS.getAccessServiceCode()== accessServiceRegistration.getAccessServiceCode()){
                initialized = true;
            }
        }
        if (!initialized) {
            SubjectAreaRegistration.registerAccessService();
        }
    }
    /**
     * Unregister this instance from the instance map.
     */
    public void shutdown() {
        SubjectAreaServicesInstanceMap.removeInstanceForJVM(serverName);
    }

    public String getAccessServiceName()
    {
        return "Subject Area Open Metadata Access Services";
    }

    /**
     *
     * @return OMRS metadata collection
     */
    public OMRSMetadataCollection getMetadataCollection()
    {
        return metadataCollection;
    }
}
