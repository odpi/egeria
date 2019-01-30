/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.services;


import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.MetadataServerUncontactableException;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.UnrecognizedGUIDException;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.UserNotAuthorizedException;
import org.odpi.openmetadata.accessservices.subjectarea.generated.server.SubjectAreaBeansToAccessOMRS;
import org.odpi.openmetadata.accessservices.subjectarea.responses.OMASExceptionToResponse;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.SubjectAreaUtils;
import org.odpi.openmetadata.accessservices.subjectarea.validators.InputValidator;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.odpi.openmetadata.accessservices.subjectarea.initialization.SubjectAreaInstanceHandler;


/**
 * SubjectAreaRESTServicesInstance caches references to OMRS objects for a specific server.
 * It is also responsible for registering itself in the instance map.
 */

public class SubjectAreaRESTServicesInstance
{
    private static final Logger log = LoggerFactory.getLogger(SubjectAreaRESTServicesInstance.class);
    private static final String className = SubjectAreaRESTServicesInstance.class.getName();

    // The OMRSAPIHelper allows the junits to mock out the omrs layer.
    protected OMRSAPIHelper oMRSAPIHelper = null;
    static private String accessServiceName = null;
    protected static SubjectAreaInstanceHandler instanceHandler = new SubjectAreaInstanceHandler();

    /**
     * Default constructor
     */
    public SubjectAreaRESTServicesInstance()
    {
    }

//    /**
//     * Setup the OMRSAPIHelper with the right instance based on the serverName
//     *
//     * @param serverName serverName, used to identify a server in a multitenant environment.
//     * @return SubjectAreaOMASAPIResponse null if successful
//     */
//    protected void initialiseOMRSAPIHelperForInstance(String serverName) throws MetadataServerUncontactableException
//    {
//        // this is set as a mock object for junits.
//        if (oMRSAPIHelper ==null)
//        {
//            oMRSAPIHelper = new OMRSAPIHelper();
//        }
//
//        OMRSRepositoryConnector omrsConnector = instanceHandler.getRepositoryConnector(serverName);
//        oMRSAPIHelper.setOMRSRepositoryConnector(omrsConnector);
    /**
     * Each API call needs to run in the requested tenant - indicated by the serverName and validate the userid.
     * @param serverName server name used to create the instance
     * @param userId userid under which the request will be made
     * @param methodName method name for logging
     * @return the Subject Area OMAS bean to access OMRS
     * @throws MetadataServerUncontactableException not able to communicate with the metadata server.
     * @throws InvalidParameterException a parameter is null or an invalid value.
     */
    protected SubjectAreaBeansToAccessOMRS initializeAPI(String serverName, String userId, String methodName) throws MetadataServerUncontactableException,
            InvalidParameterException
    {
        return initializeAPI(serverName,userId,null,null,methodName);
    }
    /**
     * Each API call needs to run in the requested tenant - indicated by the serverName and validate the userid.
     * @param serverName server name used to create the instance
     * @param userId userid under which the request will be made
     * @param from effective from date
     * @param to effective to Date
     * @param methodName method name for logging
     * @return the Subject Area OMAS bean to access OMRS
     * @throws MetadataServerUncontactableException not able to communicate with the metadata server.
     * @throws InvalidParameterException a parameter is null or an invalid value.
     */
    protected SubjectAreaBeansToAccessOMRS initializeAPI(String serverName, String userId, Date from, Date to, String methodName) throws MetadataServerUncontactableException, InvalidParameterException
    {
        // this is set as a mock object for junits.
        if (oMRSAPIHelper ==null)
        {
            oMRSAPIHelper = new OMRSAPIHelper();
        }
        // initialise omrs API helper with the right instance based on the server name
        OMRSRepositoryConnector omrsConnector = instanceHandler.getRepositoryConnector(serverName);
        oMRSAPIHelper.setOMRSRepositoryConnector(omrsConnector);
        SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = new SubjectAreaBeansToAccessOMRS();
        subjectAreaOmasREST.setOMRSAPIHelper(this.oMRSAPIHelper);
        InputValidator.validateUserIdNotNull(className, methodName, userId);
        InputValidator.validateEffectiveDate(className,methodName,to,from);
        return subjectAreaOmasREST;
    }

    public void setOMRSAPIHelper(OMRSAPIHelper oMRSAPIHelper)
    {
        this.oMRSAPIHelper = oMRSAPIHelper;
    }

}
