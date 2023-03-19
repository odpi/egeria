/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.workbenches.repository.connectorconsumer;

import org.odpi.openmetadata.conformance.auditlog.ConformanceSuiteAuditCode;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceWorkPad;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.enterprise.connectormanager.OMRSConnectorConsumer;
import org.odpi.openmetadata.repositoryservices.localrepository.repositoryconnector.LocalOMRSRepositoryConnector;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * ConformanceSuiteConnectorConsumer receives connector objects for servers connected to the same cohort(s)
 * as the conformance suite OMAG server.  It is called by the enterprise connector manager.  Its responsibility
 * is to store the technology under test's connector in the work pad to allow the workbench to run the tests.
 * Most tests are run by the workbench thread to free up the inbound event processing thread that this class is
 * called on.
 */
public class ConformanceSuiteConnectorConsumer implements OMRSConnectorConsumer
{
    private Map<String, OMRSRepositoryConnector>  connectorMap  = new HashMap<>();

    private String                                tutServerName;
    private RepositoryConformanceWorkPad          workPad;


    /**
     * Constructor is given information to scope the tests.
     *
     * @param workPad place to add information received from the enterprise connector manager.
     */
    public ConformanceSuiteConnectorConsumer(RepositoryConformanceWorkPad   workPad)
    {
        this.tutServerName = workPad.getTutServerName();
        this.workPad = workPad;
    }


    /**
     * Pass the connector for the local repository to the connector consumer.
     *
     * @param metadataCollectionId Unique identifier for the metadata collection
     * @param localConnector OMRSRepositoryConnector object for the local repository.
     */
    public void setLocalConnector(String                       metadataCollectionId,
                                  LocalOMRSRepositoryConnector localConnector)
    {
        final String methodName = "setLocalConnector";

        OMRSAuditLog auditLog = workPad.getAuditLog();

        ConformanceSuiteAuditCode auditCode = ConformanceSuiteAuditCode.LOCAL_CONNECTOR_IN_COHORT;
        auditLog.logRecord(methodName,
                           auditCode.getLogMessageId(),
                           auditCode.getSeverity(),
                           auditCode.getFormattedLogMessage(metadataCollectionId),
                           null,
                           auditCode.getSystemAction(),
                           auditCode.getUserAction());

        workPad.setLocalMetadataCollectionId(metadataCollectionId);
        workPad.setLocalRepositoryConnector(localConnector);
    }


    /**
     * Pass the connector to one of the remote repositories in the metadata repository cohort.
     *
     * @param metadataCollectionId Unique identifier for the metadata collection
     * @param remoteConnector OMRSRepositoryConnector object providing access to the remote repository.
     */
    public void addRemoteConnector(String                  metadataCollectionId,
                                   OMRSRepositoryConnector remoteConnector)
    {
        final String methodName = "addRemoteConnector";

        if (remoteConnector != null)
        {
            connectorMap.put(metadataCollectionId, remoteConnector);

            if (tutServerName.equals(remoteConnector.getServerName()))
            {
                OMRSAuditLog auditLog = workPad.getAuditLog();

                ConformanceSuiteAuditCode auditCode = ConformanceSuiteAuditCode.TUT_CONNECTED_TO_COHORT;
                auditLog.logRecord(methodName,
                                   auditCode.getLogMessageId(),
                                   auditCode.getSeverity(),
                                   auditCode.getFormattedLogMessage(tutServerName,
                                                                    metadataCollectionId),
                                   null,
                                   auditCode.getSystemAction(),
                                   auditCode.getUserAction());
                workPad.setTutMetadataCollectionId(metadataCollectionId);
                workPad.setTutRepositoryConnector(remoteConnector);
            }
            else
            {
                OMRSAuditLog auditLog = workPad.getAuditLog();

                ConformanceSuiteAuditCode auditCode = ConformanceSuiteAuditCode.ANOTHER_CONNECTED_TO_COHORT;
                auditLog.logRecord(methodName,
                                   auditCode.getLogMessageId(),
                                   auditCode.getSeverity(),
                                   auditCode.getFormattedLogMessage(remoteConnector.getServerName(),
                                                                    metadataCollectionId,
                                                                    tutServerName),
                                   null,
                                   auditCode.getSystemAction(),
                                   auditCode.getUserAction());
            }
        }
    }


    /**
     * Pass the metadata collection id for a repository that has just left the metadata repository cohort.
     *
     * @param metadataCollectionId identifier of the metadata collection that is no longer available.
     */
    public void removeRemoteConnector(String  metadataCollectionId)
    {
        final String methodName = "removeRemoteConnector";

        OMRSRepositoryConnector repositoryConnector = connectorMap.remove(metadataCollectionId);

        if (repositoryConnector != null)
        {
            if (tutServerName.equals(repositoryConnector.getServerName()))
            {
                OMRSAuditLog auditLog = workPad.getAuditLog();

                ConformanceSuiteAuditCode auditCode = ConformanceSuiteAuditCode.TUT_LEFT_COHORT;
                auditLog.logRecord(methodName,
                                   auditCode.getLogMessageId(),
                                   auditCode.getSeverity(),
                                   auditCode.getFormattedLogMessage(tutServerName,
                                                                    metadataCollectionId),
                                   null,
                                   auditCode.getSystemAction(),
                                   auditCode.getUserAction());

                workPad.setTutRepositoryConnector(null);
            }
            else
            {
                OMRSAuditLog auditLog = workPad.getAuditLog();

                ConformanceSuiteAuditCode auditCode = ConformanceSuiteAuditCode.ANOTHER_LEFT_COHORT;
                auditLog.logRecord(methodName,
                                   auditCode.getLogMessageId(),
                                   auditCode.getSeverity(),
                                   auditCode.getFormattedLogMessage(repositoryConnector.getServerName(),
                                                                    metadataCollectionId,
                                                                    tutServerName),
                                   null,
                                   auditCode.getSystemAction(),
                                   auditCode.getUserAction());
            }
        }
    }


    /**
     * Call disconnect on all registered connectors and stop calling them.  The OMRS is about to shutdown.
     */
    public void disconnectAllConnectors()
    {
        Set<String> repositoryIds = connectorMap.keySet();

        for (String metadataCollectionId : repositoryIds)
        {
            OMRSRepositoryConnector   connector = connectorMap.get(metadataCollectionId);

            if (connector != null)
            {
                try
                {
                    connector.disconnect();
                }
                catch (Exception  error)
                {
                    /* don't care */
                }
            }

            removeRemoteConnector(metadataCollectionId);
        }
    }
}
