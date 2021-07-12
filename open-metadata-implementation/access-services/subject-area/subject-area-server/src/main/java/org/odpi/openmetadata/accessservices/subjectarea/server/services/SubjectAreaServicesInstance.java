/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.services;


import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.handlers.*;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMASServiceInstance;
import org.odpi.openmetadata.commonservices.multitenant.ffdc.exceptions.NewInstanceException;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIGenericHandler;

/**
 * SubjectAreaRESTServicesInstance caches references to OMRS objects for a specific server.
 * It is also responsible for registering itself in the instance map.
 */

public class SubjectAreaServicesInstance extends OMASServiceInstance {
    private static final AccessServiceDescription myDescription = AccessServiceDescription.SUBJECT_AREA_OMAS;

    private final SubjectAreaGlossaryHandler glossaryHandler;
    private final SubjectAreaProjectHandler projectHandler;
    private final SubjectAreaTermHandler termHandler;
    private final SubjectAreaCategoryHandler categoryHandler;
    private final SubjectAreaRelationshipHandler relationshipHandler;
    private final SubjectAreaGraphHandler graphHandler;
    private final SubjectAreaConfigHandler configHandler;


    /**
     * Set up the local repository connector that will service the REST Calls.
     *
     * @param repositoryConnector link to the repository responsible for servicing the REST calls.
     * @param auditLog            logging destination
     * @param localServerUserId   userId used for server initiated actions
     * @param maxPageSize         max number of results to return on single request.
     * @throws NewInstanceException a problem occurred during initialization
     */
    public SubjectAreaServicesInstance(OMRSRepositoryConnector repositoryConnector,
                                       AuditLog auditLog,
                                       String localServerUserId,
                                       int maxPageSize) throws NewInstanceException {
        super(myDescription.getAccessServiceFullName(),
              repositoryConnector,
              auditLog,
              localServerUserId,
              maxPageSize);

        // repositoryHandler is set in the super class if OMRS is active
        if (repositoryHandler == null) {
            final String methodName = "new ServiceInstance";
            throw new NewInstanceException(SubjectAreaErrorCode.OMRS_NOT_INITIALIZED.getMessageDefinition(methodName),
                                           this.getClass().getName(),
                                           methodName);
        }
        OpenMetadataAPIGenericHandler<Glossary> genericHandler = new OpenMetadataAPIGenericHandler<>(
                null,
                Glossary.class,    // default to Glossary for now
                serviceName,
                serverName,
                invalidParameterHandler,
                repositoryHandler,
                repositoryHelper,
                localServerUserId,
                securityVerifier,
                supportedZones,
                defaultZones,
                publishZones,
                auditLog);

        this.glossaryHandler = new SubjectAreaGlossaryHandler(genericHandler, maxPageSize);

        this.termHandler = new SubjectAreaTermHandler(genericHandler, maxPageSize);

        this.categoryHandler = new SubjectAreaCategoryHandler(genericHandler, maxPageSize);

        this.projectHandler = new SubjectAreaProjectHandler(genericHandler, maxPageSize);

        this.graphHandler = new SubjectAreaGraphHandler(genericHandler, maxPageSize);

        this.relationshipHandler = new SubjectAreaRelationshipHandler(genericHandler, maxPageSize);

        this.configHandler = new SubjectAreaConfigHandler(genericHandler, maxPageSize);

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
