/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.services;


import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.handlers.*;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMASServiceInstance;
import org.odpi.openmetadata.commonservices.multitenant.ffdc.exceptions.NewInstanceException;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

/**
 * SubjectAreaRESTServicesInstance caches references to OMRS objects for a specific server.
 * It is also responsible for registering itself in the instance map.
 */

public class SubjectAreaServicesInstance extends OMASServiceInstance
{
    private static AccessServiceDescription myDescription = AccessServiceDescription.SUBJECT_AREA_OMAS;
    private OMRSAPIHelper oMRSAPIHelper;
    private SubjectAreaGlossaryHandler glossaryHandler;
    private SubjectAreaProjectHandler projectHandler;
    private SubjectAreaTermHandler termHandler;
    private SubjectAreaCategoryHandler categoryHandler;
    private SubjectAreaRelationshipHandler relationshipHandler;
    private SubjectAreaGraphHandler graphHandler;
    private SubjectAreaConfigHandler configHandler;

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
                this.oMRSAPIHelper = new OMRSAPIHelper(
                        serviceName,
                        serverName,
                        repositoryHandler,
                        repositoryHelper
                );
            }

            this.glossaryHandler= new SubjectAreaGlossaryHandler(oMRSAPIHelper, maxPageSize);

            this.termHandler= new SubjectAreaTermHandler(oMRSAPIHelper, maxPageSize);

            this.categoryHandler= new SubjectAreaCategoryHandler(oMRSAPIHelper, maxPageSize);

            this.projectHandler= new SubjectAreaProjectHandler(oMRSAPIHelper, maxPageSize);

            this.graphHandler= new SubjectAreaGraphHandler(oMRSAPIHelper, maxPageSize);

            this.relationshipHandler= new SubjectAreaRelationshipHandler(oMRSAPIHelper, maxPageSize);

            this.configHandler = new SubjectAreaConfigHandler(oMRSAPIHelper, maxPageSize);
        }
        else
        {
           throw new NewInstanceException(SubjectAreaErrorCode.OMRS_NOT_INITIALIZED.getMessageDefinition(methodName),
                                           this.getClass().getName(),
                                           methodName);

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
    /**
     * Return the handler for config requests.
     *
     * @return handler object
     */
    public SubjectAreaConfigHandler getConfigHandler() {
        return configHandler;
    }
}
