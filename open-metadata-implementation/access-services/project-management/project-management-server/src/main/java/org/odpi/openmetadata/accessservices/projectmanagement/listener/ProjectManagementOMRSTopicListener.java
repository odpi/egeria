/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.projectmanagement.listener;

import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicListenerBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryValidator;

import java.util.List;


/**
 * ProjectManagementOMRSTopicListener received details of each OMRS event from the cohorts that the local server
 * is connected to.  It passes NEW_ENTITY_EVENTs to the publisher.
 */
public class ProjectManagementOMRSTopicListener extends OMRSTopicListenerBase
{
    private static final Logger log = LoggerFactory.getLogger(ProjectManagementOMRSTopicListener.class);

    private OMRSRepositoryHelper    repositoryHelper;
    private OMRSRepositoryValidator repositoryValidator;
    private String                  componentName;
    private List<String>            supportedZones;


    /**
     * The constructor is given the connection to the out topic for Asset Consumer OMAS
     * along with classes for testing and manipulating instances.
     *
     * @param projectManagementOutTopic  connection to the out topic
     * @param repositoryHelper  provides methods for working with metadata instances
     * @param repositoryValidator  provides validation of metadata instance
     * @param componentName  name of component
     * @param supportedZones list of zones covered by this instance of the access service.
     * @param auditLog log for errors and information messages
     * @throws OMAGConfigurationErrorException problems creating the connector for the outTopic
     */
    public ProjectManagementOMRSTopicListener(Connection              projectManagementOutTopic,
                                              OMRSRepositoryHelper    repositoryHelper,
                                              OMRSRepositoryValidator repositoryValidator,
                                              String                  componentName,
                                              List<String>            supportedZones,
                                              AuditLog                auditLog) throws OMAGConfigurationErrorException
    {
        super(componentName, auditLog);

        this.repositoryHelper = repositoryHelper;
        this.repositoryValidator = repositoryValidator;
        this.componentName = componentName;
        this.supportedZones = supportedZones;
    }

}
