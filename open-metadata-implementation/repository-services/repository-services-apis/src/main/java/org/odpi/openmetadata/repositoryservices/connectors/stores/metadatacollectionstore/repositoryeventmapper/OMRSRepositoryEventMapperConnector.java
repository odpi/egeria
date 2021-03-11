/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryeventmapper;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.auditlog.AuditLoggingComponent;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBase;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSAuditCode;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryValidator;


import java.util.ArrayList;
import java.util.List;

/**
 * OMRSRepositoryEventMapperBase provides a base class for implementors of OMRSRepositoryEventMapper.
 */
public abstract class OMRSRepositoryEventMapperConnector extends ConnectorBase implements OMRSRepositoryEventMapper,
                                                                                          AuditLoggingComponent
{
    protected OMRSRepositoryEventProcessor repositoryEventProcessor  = null;
    protected String                       repositoryEventMapperName = null;
    protected OMRSRepositoryConnector      repositoryConnector       = null;
    protected OMRSRepositoryHelper         repositoryHelper          = null;
    protected OMRSRepositoryValidator      repositoryValidator       = null;
    protected String                       localMetadataCollectionId = null;
    protected String                       localServerName           = null;
    protected String                       localServerType           = null;
    protected String                       localOrganizationName     = null;
    protected String                       localServerUserId         = null;
    protected AuditLog                     auditLog                  = null;

    private List<OpenMetadataTopicConnector> eventBusConnectors = new ArrayList<>();


    /**
     * Default constructor for OCF ConnectorBase.
     */
    OMRSRepositoryEventMapperConnector()
    {
        super();
    }


    /**
     * Receive an audit log object that can be used to record audit log messages.  The caller has initialized it
     * with the correct component description and log destinations.
     *
     * @param auditLog audit log object
     */
    @Override
    public void setAuditLog(AuditLog   auditLog)
    {
        this.auditLog = auditLog;
    }


    /**
     * Pass additional information to the connector needed to process events.
     *
     * @param repositoryEventMapperName repository event mapper name used for the source of the OMRS events.
     * @param repositoryConnector ths is the connector to the local repository that the event mapper is processing
     *                            events from.  The repository connector is used to retrieve additional information
     *                            necessary to fill out the OMRS Events.
     */
    @Override
    public void initialize(String                      repositoryEventMapperName,
                           OMRSRepositoryConnector     repositoryConnector)
    {
        this.repositoryEventMapperName = repositoryEventMapperName;
        this.repositoryConnector = repositoryConnector;
    }


    /**
     * Set up a repository helper object for the repository connector to use.
     *
     * @param repositoryHelper helper object for building TypeDefs and metadata instances.
     */
    @Override
    public void setRepositoryHelper(OMRSRepositoryHelper repositoryHelper)
    {
        this.repositoryHelper = repositoryHelper;
    }


    /**
     * Set up a repository validator for the repository connector to use.
     *
     * @param repositoryValidator validator object to check the validity of TypeDefs and metadata instances.
     */
    @Override
    public void setRepositoryValidator(OMRSRepositoryValidator repositoryValidator)
    {
        this.repositoryValidator = repositoryValidator;
    }


    /**
     * Set up the name of the server where the metadata collection resides.
     *
     * @param serverName String name
     */
    @Override
    public void  setServerName(String      serverName)
    {
        this.localServerName = serverName;
    }


    /**
     * Set up the descriptive string describing the type of the server.  This might be the
     * name of the product, or similar identifier.
     *
     * @param serverType String server type
     */
    @Override
    public void setServerType(String serverType)
    {
        this.localServerType = serverType;
    }


    /**
     * Set up the name of the organization that runs/owns the server.
     *
     * @param organizationName String organization name
     */
    @Override
    public void setOrganizationName(String organizationName)
    {
        this.localOrganizationName = organizationName;
    }


    /**
     * Set up the userId that the local server should use when processing events and there is no external user
     * driving the operation.
     *
     * @param localServerUserId string user id
     */
    @Override
    public void setServerUserId(String localServerUserId)
    {
        this.localServerUserId = localServerUserId;
    }


    /**
     * Set up the unique Id for this metadata collection.
     *
     * @param metadataCollectionId String unique Id
     */
    @Override
    public void setMetadataCollectionId(String         metadataCollectionId)
    {
        this.localMetadataCollectionId = metadataCollectionId;
    }


    /**
     * Set up the repository event listener for this connector to use.  The connector should pass
     * each type or instance metadata change reported by its metadata repository's metadata on to the
     * repository event listener.
     *
     * @param repositoryEventProcessor listener responsible for distributing notifications of local
     *                                changes to metadata types and instances to the rest of the
     *                                open metadata repository cluster.
     */
    @Override
    public void setRepositoryEventProcessor(OMRSRepositoryEventProcessor repositoryEventProcessor)
    {
        this.repositoryEventProcessor = repositoryEventProcessor;
    }


    /**
     * Indicates that the connector is completely configured and can begin processing.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    @Override
    public void start() throws ConnectorCheckedException
    {
        super.start();

        final String            methodName = "Start Event Mapper";

        /*
         * If the event mapper is not using embedded connectors then this list is empty.
         */
        for (OpenMetadataTopicConnector eventBusConnector : eventBusConnectors)
        {
            eventBusConnector.start();
        }

        if (auditLog != null)
        {
            auditLog.logMessage(methodName,
                                OMRSAuditCode.EVENT_MAPPER_LISTENER_STARTED.getMessageDefinition(repositoryEventMapperName),
                               this.getConnection().toString());
        }
    }


    /**
     * Free up any resources held since the connector is no longer needed.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    @Override
    public  void disconnect() throws ConnectorCheckedException
    {
        final String            methodName = "Disconnect Event Mapper";

        super.disconnect();

        /*
         * If the event mapper is not using embedded connectors then this list is empty.
         */
        for (OpenMetadataTopicConnector eventBusConnector : eventBusConnectors)
        {
            eventBusConnector.disconnect();
        }

        if (auditLog != null)
        {
            auditLog.logMessage(methodName,
                                OMRSAuditCode.EVENT_MAPPER_LISTENER_DISCONNECTED.getMessageDefinition(repositoryEventMapperName),
                               this.getConnection().toString());
        }
    }
}
