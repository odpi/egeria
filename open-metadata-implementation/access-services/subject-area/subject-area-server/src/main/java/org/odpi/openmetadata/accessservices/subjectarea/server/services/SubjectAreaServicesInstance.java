/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.services;


import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.handlers.*;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OCFOMASServiceInstance;
import org.odpi.openmetadata.commonservices.multitenant.ffdc.exceptions.NewInstanceException;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

/**
 * SubjectAreaRESTServicesInstance caches references to OMRS objects for a specific server.
 * It is also responsible for registering itself in the instance map.
 */

public class SubjectAreaServicesInstance extends OCFOMASServiceInstance
{
    private static AccessServiceDescription myDescription = AccessServiceDescription.SUBJECT_AREA_OMAS;
    private OMRSAPIHelper oMRSAPIHelper;
    private SubjectAreaGlossaryHandler glossaryHandler;
    private SubjectAreaProjectHandler projectHandler;
    private SubjectAreaTermHandler termHandler;
    private SubjectAreaCategoryHandler categoryHandler;
    private SubjectAreaRelationshipHandler relationshipHandler;
    private SubjectAreaGraphHandler graphHandler;

    /**
     * Set up the local repository connector that will service the REST Calls.
     *
     * @param repositoryConnector link to the repository responsible for servicing the REST calls.
     * @param auditLog logging destination
     * @param localServerUserId userId used for server initiated actions
     * @param maxPageSize max number of results to return on single request.
     *
     * @throws NewInstanceException a problem occurred during initialization
     */
    public SubjectAreaServicesInstance(OMRSRepositoryConnector repositoryConnector,
                                       AuditLog                auditLog,
                                       String                  localServerUserId,
                                       int                     maxPageSize) throws NewInstanceException
    {
        super(myDescription.getAccessServiceFullName(),
              repositoryConnector,
              auditLog,
              localServerUserId,
              maxPageSize);

        final String methodName = "new ServiceInstance";

        if (repositoryHandler != null)
        {
            if (this.oMRSAPIHelper == null ) {
                this.oMRSAPIHelper = new OMRSAPIHelper(serviceName);
            }
            this.glossaryHandler= new SubjectAreaGlossaryHandler(serviceName,
                                                                     serverName,
                                                                     invalidParameterHandler,
                                                                     repositoryHelper,
                                                                     repositoryHandler,
                                                                     oMRSAPIHelper,
                                                                     errorHandler);
            this.termHandler= new SubjectAreaTermHandler(serviceName,
                                                                 serverName,
                                                                 invalidParameterHandler,
                                                                 repositoryHelper,
                                                                 repositoryHandler,
                                                                 oMRSAPIHelper,
                                                                 errorHandler);

            this.categoryHandler= new SubjectAreaCategoryHandler(serviceName,
                                                                         serverName,
                                                                         invalidParameterHandler,
                                                                         repositoryHelper,
                                                                         repositoryHandler,
                                                                         oMRSAPIHelper,
                                                                         errorHandler);

            this.projectHandler= new SubjectAreaProjectHandler(serviceName,
                                                                     serverName,
                                                                     invalidParameterHandler,
                                                                     repositoryHelper,
                                                                     repositoryHandler,
                                                                     oMRSAPIHelper,
                                                                     errorHandler);

            this.graphHandler= new SubjectAreaGraphHandler(serviceName,
                                                               serverName,
                                                               invalidParameterHandler,
                                                               repositoryHelper,
                                                               repositoryHandler,
                                                               oMRSAPIHelper,
                                                               errorHandler);

            this.relationshipHandler= new SubjectAreaRelationshipHandler(serviceName,
                                                           serverName,
                                                           invalidParameterHandler,
                                                           repositoryHelper,
                                                           repositoryHandler,
                                                           oMRSAPIHelper,
                                                           errorHandler);


        }
        else
        {
//            throw new NewInstanceException(SubjectAreaErrorCode.OMRS_NOT_INITIALIZED.getMessageDefinition(methodName),
//                                           this.getClass().getName(),
//                                           methodName);

        }
    }


    /**
     * Return the handler for glossary requests.
     *
     * @return handler object
     */
    public SubjectAreaGlossaryHandler getGlossaryHandler() {
        return glossaryHandler;
    }
    /**
     * Return the handler for project requests.
     *
     * @return handler object
     */
    public SubjectAreaProjectHandler getProjectHandler() {
        return projectHandler;
    }
    /**
     * Return the handler for term requests.
     *
     * @return handler object
     */
    public SubjectAreaTermHandler getTermHandler() {
        return termHandler;
    }
    /**
     * Return the handler for category requests.
     *
     * @return handler object
     */
    public SubjectAreaCategoryHandler getCategoryHandler() {
        return categoryHandler;
    }
    /**
     * Return the handler for relationship requests.
     *
     * @return handler object
     */
    public SubjectAreaRelationshipHandler getRelationshipHandler() {
        return relationshipHandler;
    }
    /**
     * Return the handler for graph requests.
     *
     * @return handler object
     */
    public SubjectAreaGraphHandler getGraphHandler() {
        return graphHandler;
    }
}
