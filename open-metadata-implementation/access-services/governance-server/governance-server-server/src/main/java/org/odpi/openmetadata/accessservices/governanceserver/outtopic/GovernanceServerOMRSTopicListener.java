/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.governanceserver.outtopic;

import org.odpi.openmetadata.accessservices.governanceserver.ffdc.GovernanceServerAuditCode;
import org.odpi.openmetadata.commonservices.generichandlers.EngineActionHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;
import org.odpi.openmetadata.frameworks.governanceaction.properties.EngineActionElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.EngineActionStatus;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicListenerBase;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefSummary;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;


/**
 * GovernanceEngineOMRSTopicListener is the listener that registers with the repository services (OMRS)
 * to monitor changes to the metadata.  This class is looking for changes to the Governance Servers
 * and governance services configuration.  When it detects an appropriate change it passes the
 * update to the GovernanceEngineOutTopicPublisher to publish a GovernanceEngineEvent describing the
 * change.  These events are picked up by the Engine Host Server (See engine-host-services) to
 * control the configuration of the Governance Servers it is hosting.
 */
public class GovernanceServerOMRSTopicListener extends OMRSTopicListenerBase
{
    private final GovernanceServerOutTopicPublisher              eventPublisher;
    private final OMRSRepositoryHelper                           repositoryHelper;
    private final EngineActionHandler<EngineActionElement>        engineActionHandler;


    /**
     * Initialize the topic listener.
     *
     * @param serviceName this is the full name of the service - used for error logging in base class
     * @param engineActionHandler handler for working with governance actions
     * @param eventPublisher this is the out topic publisher.
     * @param repositoryHelper repository helper
     * @param auditLog logging destination
     */
    public GovernanceServerOMRSTopicListener(String                                           serviceName,
                                             EngineActionHandler<EngineActionElement>         engineActionHandler,
                                             GovernanceServerOutTopicPublisher                eventPublisher,
                                             OMRSRepositoryHelper                             repositoryHelper,
                                             AuditLog                                         auditLog)
    {
        super(serviceName, auditLog);

        this.engineActionHandler = engineActionHandler;
        this.eventPublisher   = eventPublisher;
        this.repositoryHelper = repositoryHelper;
    }


    /**
     * Process an entity extracted from an event.
     *
     * @param sourceName source of the event
     * @param entity entity from the event
     * @param methodName calling method (indicates type of event action)
     * @return boolean flag indicating that the event is processed
     */
    private boolean processGovernanceEngineEvent(String         sourceName,
                                                 EntityDetail   entity,
                                                 String         methodName)
    {
        if (entity != null)
        {
            InstanceType type = entity.getType();

            if (type != null)
            {
                if (repositoryHelper.isTypeOf(sourceName,
                                              type.getTypeDefName(),
                                              OpenMetadataType.GOVERNANCE_ENGINE.typeName))
                {
                    eventPublisher.publishRefreshGovernanceEngineEvent(entity.getGUID(),
                                                                      repositoryHelper.getStringProperty(sourceName,
                                                                                                         OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                                         entity.getProperties(),
                                                                                                         methodName));
                    return true;
                }
            }
        }

        return false;
    }


    /**
     * Process an entity extracted from an event.
     *
     * @param sourceName source of the event
     * @param entity entity from the event
     * @param methodName calling method (indicates type of event action)
     * @return boolean flag indicating that the event is processed
     */
    private boolean processIntegrationGroupEvent(String         sourceName,
                                                 EntityDetail   entity,
                                                 String         methodName)
    {
        if (entity != null)
        {
            InstanceType type = entity.getType();

            if (type != null)
            {
                if (repositoryHelper.isTypeOf(sourceName,
                                              type.getTypeDefName(),
                                              OpenMetadataType.INTEGRATION_GROUP_TYPE_NAME))
                {
                    eventPublisher.publishRefreshIntegrationGroupEvent(entity.getGUID(),
                                                                       repositoryHelper.getStringProperty(sourceName,
                                                                                                          OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                                          entity.getProperties(),
                                                                                                          methodName));
                    return true;
                }
            }
        }

        return false;
    }


    /**
     * Process an entity extracted from an event.
     *
     * @param sourceName source of the event
     * @param entity entity from the event
     * @param methodName calling method (indicates type of event action)
     * @return boolean flag indicating that the event is processed
     */
    private boolean processIntegrationConnectorEvent(String         sourceName,
                                                     EntityDetail   entity,
                                                     String         methodName)
    {
        if (entity != null)
        {
            InstanceType type = entity.getType();

            if (type != null)
            {
                if (repositoryHelper.isTypeOf(sourceName,
                                              type.getTypeDefName(),
                                              OpenMetadataType.INTEGRATION_CONNECTOR_TYPE_NAME))
                {
                    eventPublisher.publishRefreshIntegrationConnectorEvent(entity.getGUID(),
                                                                           repositoryHelper.getStringProperty(sourceName,
                                                                                                              OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                                              entity.getProperties(),
                                                                                                              methodName));
                    return true;
                }
            }
        }

        return false;
    }




    /**
     * Process an entity extracted from an event.
     *
     * @param sourceName source of the event
     * @param entity entity from the event
     * @param methodName calling method (indicates type of event action)
     * @return boolean flag indicating that the event is processed
     */
    private boolean processGovernanceEngineEvent(String         sourceName,
                                                 EntityProxy    entity,
                                                 String         methodName)
    {
        if (entity != null)
        {
            InstanceType type = entity.getType();

            if (type != null)
            {
                if (repositoryHelper.isTypeOf(sourceName,
                                              type.getTypeDefName(),
                                              OpenMetadataType.GOVERNANCE_ENGINE.typeName))
                {
                    eventPublisher.publishRefreshGovernanceEngineEvent(entity.getGUID(),
                                                                       repositoryHelper.getStringProperty(sourceName,
                                                                                                          OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                                          entity.getUniqueProperties(),
                                                                                                          methodName));
                    return true;
                }
            }
        }

        return false;
    }


    /**
     * Process an entity extracted from an event.
     *
     * @param sourceName source of the event
     * @param entity entity from the event
     * @param methodName calling method (indicates type of event action)
     * @return boolean flag indicating that the event is processed
     */
    private boolean processIntegrationGroupEvent(String        sourceName,
                                                 EntityProxy   entity,
                                                 String        methodName)
    {
        if (entity != null)
        {
            InstanceType type = entity.getType();

            if (type != null)
            {
                if (repositoryHelper.isTypeOf(sourceName,
                                              type.getTypeDefName(),
                                              OpenMetadataType.INTEGRATION_GROUP_TYPE_NAME))
                {
                    eventPublisher.publishRefreshIntegrationGroupEvent(entity.getGUID(),
                                                                       repositoryHelper.getStringProperty(sourceName,
                                                                                                          OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                                          entity.getUniqueProperties(),
                                                                                                          methodName));
                    return true;
                }
            }
        }

        return false;
    }


    /**
     * Process an entity extracted from an event.
     *
     * @param sourceName source of the event
     * @param entity entity from the event
     * @param methodName calling method (indicates type of event action)
     * @return boolean flag indicating that the event is processed
     */
    private boolean processIntegrationConnectorEvent(String         sourceName,
                                                     EntityProxy    entity,
                                                     String         methodName)
    {
        if (entity != null)
        {
            InstanceType type = entity.getType();

            if (type != null)
            {
                if (repositoryHelper.isTypeOf(sourceName,
                                              type.getTypeDefName(),
                                              OpenMetadataType.INTEGRATION_CONNECTOR_TYPE_NAME))
                {
                    eventPublisher.publishRefreshIntegrationConnectorEvent(entity.getGUID(),
                                                                           repositoryHelper.getStringProperty(sourceName,
                                                                                                              OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                                              entity.getUniqueProperties(),
                                                                                                              methodName));
                    return true;
                }
            }
        }

        return false;
    }




    /**
     * Process a relationship extracted from an event.
     *
     * @param sourceName source of the event
     * @param relationship relationship from the event
     * @param methodName calling method (indicates type of event action)
     * @return boolean flag indicating that the event is processed
     */
    private boolean processSupportedGovernanceService(String       sourceName,
                                                      Relationship relationship,
                                                      String       methodName)
    {
        if (relationship != null)
        {
            InstanceType type = relationship.getType();

            if (type != null)
            {
                if (repositoryHelper.isTypeOf(sourceName,
                                              type.getTypeDefName(),
                                              OpenMetadataType.SUPPORTED_GOVERNANCE_SERVICE_RELATIONSHIP.typeName))
                {
                    EntityProxy governanceEngineEntityProxy = relationship.getEntityOneProxy();
                    EntityProxy governanceServiceEntityProxy = relationship.getEntityTwoProxy();

                    if (governanceEngineEntityProxy != null)
                    {
                        eventPublisher.publishRefreshGovernanceServiceEvent(governanceEngineEntityProxy.getGUID(),
                                                                            repositoryHelper.getStringProperty(sourceName,
                                                                                                               OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                                               governanceEngineEntityProxy.getUniqueProperties(),
                                                                                                               methodName),
                                                                            governanceServiceEntityProxy.getGUID(),
                                                                            repositoryHelper.getStringProperty(sourceName,
                                                                                                               OpenMetadataProperty.REQUEST_TYPE.name,
                                                                                                               relationship.getProperties(),
                                                                                                               methodName));
                        return true;
                    }
                }
            }
        }

        return false;
    }


    /**
     * Process a relationship extracted from an event.
     *
     * @param sourceName source of the event
     * @param relationship relationship from the event
     * @param methodName calling method (indicates type of event action)
     * @return boolean flag indicating that the event is processed
     */
    private boolean processRegisteredIntegrationConnector(String       sourceName,
                                                          Relationship relationship,
                                                          String       methodName)
    {
        if (relationship != null)
        {
            InstanceType type = relationship.getType();

            if (type != null)
            {
                if (repositoryHelper.isTypeOf(sourceName,
                                              type.getTypeDefName(),
                                              OpenMetadataType.REGISTERED_INTEGRATION_CONNECTOR_TYPE_NAME))
                {
                    EntityProxy integrationConnectorEntityProxy = relationship.getEntityTwoProxy();

                    if (integrationConnectorEntityProxy != null)
                    {
                        eventPublisher.publishRefreshIntegrationConnectorEvent(integrationConnectorEntityProxy.getGUID(),
                                                                               repositoryHelper.getStringProperty(sourceName,
                                                                                                                  OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                                                  integrationConnectorEntityProxy.getUniqueProperties(),
                                                                                                                  methodName));
                        return true;
                    }
                }
            }
        }

        return false;
    }


    /**
     * Process an entity extracted from an event.
     *
     * @param sourceName source of the event
     * @param entity entity from the event
     * @param methodName calling method (indicates type of event action)
     * @return boolean flag indicating that the event should be ignored
     */
    private boolean processEngineActionEvent(String       sourceName,
                                             EntityDetail entity,
                                             String       methodName)
    {
        if (entity != null)
        {
            InstanceType type = entity.getType();

            if (type != null)
            {
                if (repositoryHelper.isTypeOf(sourceName,
                                              type.getTypeDefName(),
                                              OpenMetadataType.ENGINE_ACTION_TYPE_NAME))
                {
                    EngineActionStatus status = engineActionHandler.getActionStatus(OpenMetadataType.ACTION_STATUS_PROPERTY_NAME,
                                                                                    entity.getProperties());

                    if (status == EngineActionStatus.APPROVED)
                    {
                        String governanceEngineGUID = repositoryHelper.getStringProperty(sourceName,
                                                                                         OpenMetadataType.EXECUTOR_ENGINE_GUID_PROPERTY_NAME,
                                                                                         entity.getProperties(),
                                                                                         methodName);
                        String governanceEngineName = repositoryHelper.getStringProperty(sourceName,
                                                                                         OpenMetadataType.EXECUTOR_ENGINE_NAME_PROPERTY_NAME,
                                                                                         entity.getProperties(),
                                                                                         methodName);

                        eventPublisher.publishNewEngineAction(governanceEngineGUID,
                                                              governanceEngineName,
                                                              entity.getGUID());
                    }
                    else if (status == EngineActionStatus.CANCELLED)
                    {
                        String governanceEngineGUID = repositoryHelper.getStringProperty(sourceName,
                                                                                         OpenMetadataType.EXECUTOR_ENGINE_GUID_PROPERTY_NAME,
                                                                                         entity.getProperties(),
                                                                                         methodName);
                        String governanceEngineName = repositoryHelper.getStringProperty(sourceName,
                                                                                         OpenMetadataType.EXECUTOR_ENGINE_NAME_PROPERTY_NAME,
                                                                                         entity.getProperties(),
                                                                                         methodName);

                        eventPublisher.publishCancelledEngineAction(governanceEngineGUID,
                                                                    governanceEngineName,
                                                                    entity.getGUID());
                    }

                    return true;
                }
            }
        }

        return false;
    }


    /**
     * A new entity has been created.
     *
     * @param sourceName                     name of the source of the event.  It may be the cohort name for incoming events or the
     *                                       local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName           name of the server that the event came from.
     * @param originatorServerType           type of server that the event came from.
     * @param originatorOrganizationName     name of the organization that owns the server that sent the event.
     * @param entity                         details of the new entity
     */
    @Override
    public void processNewEntityEvent(String       sourceName,
                                      String       originatorMetadataCollectionId,
                                      String       originatorServerName,
                                      String       originatorServerType,
                                      String       originatorOrganizationName,
                                      EntityDetail entity)
    {
        final String methodName = "processNewEntityEvent";

        if ((processGovernanceEngineEvent(sourceName, entity, methodName)) ||
            (processIntegrationGroupEvent(sourceName, entity, methodName)) ||
            (processEngineActionEvent(sourceName, entity, methodName)))
        {
            auditLog.logMessage(methodName, GovernanceServerAuditCode.INTERESTING_EVENT.getMessageDefinition(entity.getGUID()));
        }
    }


    /**
     * An existing entity has been updated.
     *
     * @param sourceName                     name of the source of the event.  It may be the cohort name for incoming events or the
     *                                       local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName           name of the server that the event came from.
     * @param originatorServerType           type of server that the event came from.
     * @param originatorOrganizationName     name of the organization that owns the server that sent the event.
     * @param oldEntity                      original values for the entity.
     * @param newEntity                      details of the new version of the entity.
     */
    @Override
    public void processUpdatedEntityEvent(String       sourceName,
                                          String       originatorMetadataCollectionId,
                                          String       originatorServerName,
                                          String       originatorServerType,
                                          String       originatorOrganizationName,
                                          EntityDetail oldEntity,
                                          EntityDetail newEntity)
    {
        final String methodName = "processUpdatedEntityEvent";

        if ((processGovernanceEngineEvent(sourceName, newEntity, methodName)) ||
            (processIntegrationGroupEvent(sourceName, newEntity, methodName)) ||
            (processIntegrationConnectorEvent(sourceName, newEntity, methodName)) ||
            (processEngineActionEvent(sourceName, newEntity, methodName)))
        {
            auditLog.logMessage(methodName, GovernanceServerAuditCode.INTERESTING_EVENT.getMessageDefinition(newEntity.getGUID()));
        }
    }


    /**
     * An update to an entity has been undone.
     *
     * @param sourceName                     name of the source of the event.  It may be the cohort name for incoming events or the
     *                                       local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName           name of the server that the event came from.
     * @param originatorServerType           type of server that the event came from.
     * @param originatorOrganizationName     name of the organization that owns the server that sent the event.
     * @param entity                         details of the version of the entity that has been restored.
     */
    @Override
    public void processUndoneEntityEvent(String       sourceName,
                                         String       originatorMetadataCollectionId,
                                         String       originatorServerName,
                                         String       originatorServerType,
                                         String       originatorOrganizationName,
                                         EntityDetail entity)
    {
        final String methodName = "processUndoneEntityEvent";

        if ((processGovernanceEngineEvent(sourceName, entity, methodName)) ||
                (processIntegrationGroupEvent(sourceName, entity, methodName)) ||
                (processIntegrationConnectorEvent(sourceName, entity, methodName)) ||
                (processEngineActionEvent(sourceName, entity, methodName)))
        {
            auditLog.logMessage(methodName, GovernanceServerAuditCode.INTERESTING_EVENT.getMessageDefinition(entity.getGUID()));
        }
    }


    /**
     * A new classification has been added to an entity.
     *
     * @param sourceName  name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId  unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName  name of the server that the event came from.
     * @param originatorServerType  type of server that the event came from.
     * @param originatorOrganizationName  name of the organization that owns the server that sent the event.
     * @param entity  details of the entity with the new classification added. No guarantee this is all the classifications.
     * @param classification new classification
     */
    @Override
    public void processClassifiedEntityEvent(String         sourceName,
                                             String         originatorMetadataCollectionId,
                                             String         originatorServerName,
                                             String         originatorServerType,
                                             String         originatorOrganizationName,
                                             EntityDetail   entity,
                                             Classification classification)
    {
        final String methodName = "processClassifiedEntityEvent";

        if ((processGovernanceEngineEvent(sourceName, entity, methodName)) ||
                (processIntegrationGroupEvent(sourceName, entity, methodName)) ||
                (processIntegrationConnectorEvent(sourceName, entity, methodName)) ||
                (processEngineActionEvent(sourceName, entity, methodName)))
        {
            auditLog.logMessage(methodName, GovernanceServerAuditCode.INTERESTING_EVENT.getMessageDefinition(entity.getGUID()));
        }
    }


    /**
     * A new classification has been added to an entity.
     *
     * @param sourceName  name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId  unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName  name of the server that the event came from.
     * @param originatorServerType  type of server that the event came from.
     * @param originatorOrganizationName  name of the organization that owns the server that sent the event.
     * @param entity  details of the entity with the new classification added. No guarantee this is all the classifications.
     * @param classification new classification
     */
    @Override
    public void processClassifiedEntityEvent(String         sourceName,
                                             String         originatorMetadataCollectionId,
                                             String         originatorServerName,
                                             String         originatorServerType,
                                             String         originatorOrganizationName,
                                             EntityProxy    entity,
                                             Classification classification)
    {
        final String methodName = "processClassifiedEntityEvent(proxy)";

        if ((processGovernanceEngineEvent(sourceName, entity, methodName)) ||
            (processIntegrationGroupEvent(sourceName, entity, methodName)) ||
            (processIntegrationConnectorEvent(sourceName, entity, methodName)))
        {
            auditLog.logMessage(methodName, GovernanceServerAuditCode.INTERESTING_EVENT.getMessageDefinition(entity.getGUID()));
        }
    }


    /**
     * A classification has been removed from an entity.
     *
     * @param sourceName  name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId  unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName  name of the server that the event came from.
     * @param originatorServerType  type of server that the event came from.
     * @param originatorOrganizationName  name of the organization that owns the server that sent the event.
     * @param entity  details of the entity after the classification has been removed. No guarantee this is all the classifications.
     * @param originalClassification classification that was removed
     */
    @Override
    public void processDeclassifiedEntityEvent(String         sourceName,
                                               String         originatorMetadataCollectionId,
                                               String         originatorServerName,
                                               String         originatorServerType,
                                               String         originatorOrganizationName,
                                               EntityDetail   entity,
                                               Classification originalClassification)
    {
        final String methodName = "processDeclassifiedEntityEvent";

        if ((processGovernanceEngineEvent(sourceName, entity, methodName)) ||
                (processIntegrationGroupEvent(sourceName, entity, methodName)) ||
                (processIntegrationConnectorEvent(sourceName, entity, methodName)) ||
                (processEngineActionEvent(sourceName, entity, methodName)))
        {
            auditLog.logMessage(methodName, GovernanceServerAuditCode.INTERESTING_EVENT.getMessageDefinition(entity.getGUID()));
        }
    }


    /**
     * A classification has been removed from an entity.
     *
     * @param sourceName  name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId  unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName  name of the server that the event came from.
     * @param originatorServerType  type of server that the event came from.
     * @param originatorOrganizationName  name of the organization that owns the server that sent the event.
     * @param entity  details of the entity after the classification has been removed. No guarantee this is all the classifications.
     * @param originalClassification classification that was removed
     */
    @Override
    public void processDeclassifiedEntityEvent(String         sourceName,
                                               String         originatorMetadataCollectionId,
                                               String         originatorServerName,
                                               String         originatorServerType,
                                               String         originatorOrganizationName,
                                               EntityProxy    entity,
                                               Classification originalClassification)
    {
        final String methodName = "processDeclassifiedEntityEvent";

        if ((processGovernanceEngineEvent(sourceName, entity, methodName)) ||
                (processIntegrationGroupEvent(sourceName, entity, methodName)) ||
                (processIntegrationConnectorEvent(sourceName, entity, methodName)))
        {
            auditLog.logMessage(methodName, GovernanceServerAuditCode.INTERESTING_EVENT.getMessageDefinition(entity.getGUID()));
        }
    }


    /**
     * An existing classification has been changed on an entity.
     *
     * @param sourceName  name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId  unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName  name of the server that the event came from.
     * @param originatorServerType  type of server that the event came from.
     * @param originatorOrganizationName  name of the organization that owns the server that sent the event.
     * @param entity  details of the entity after the classification has been changed. No guarantee this is all the classifications.
     * @param originalClassification classification that was removed
     * @param classification new classification
     */
    @Override
    public void processReclassifiedEntityEvent(String         sourceName,
                                               String         originatorMetadataCollectionId,
                                               String         originatorServerName,
                                               String         originatorServerType,
                                               String         originatorOrganizationName,
                                               EntityDetail   entity,
                                               Classification originalClassification,
                                               Classification classification)
    {
        final String methodName = "processReclassifiedEntityEvent";

        if ((processGovernanceEngineEvent(sourceName, entity, methodName)) ||
                (processIntegrationGroupEvent(sourceName, entity, methodName)) ||
                (processIntegrationConnectorEvent(sourceName, entity, methodName)) ||
                (processEngineActionEvent(sourceName, entity, methodName)))
        {
            auditLog.logMessage(methodName, GovernanceServerAuditCode.INTERESTING_EVENT.getMessageDefinition(entity.getGUID()));
        }
    }



    /**
     * An existing classification has been changed on an entity.
     *
     * @param sourceName  name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId  unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName  name of the server that the event came from.
     * @param originatorServerType  type of server that the event came from.
     * @param originatorOrganizationName  name of the organization that owns the server that sent the event.
     * @param entity  details of the entity after the classification has been changed. No guarantee this is all the classifications.
     * @param originalClassification classification that was removed
     * @param classification new classification
     */
    @Override
    public void processReclassifiedEntityEvent(String         sourceName,
                                               String         originatorMetadataCollectionId,
                                               String         originatorServerName,
                                               String         originatorServerType,
                                               String         originatorOrganizationName,
                                               EntityProxy    entity,
                                               Classification originalClassification,
                                               Classification classification)
    {
        final String methodName = "processReclassifiedEntityEvent";

        if ((processGovernanceEngineEvent(sourceName, entity, methodName)) ||
                (processIntegrationGroupEvent(sourceName, entity, methodName)) ||
                (processIntegrationConnectorEvent(sourceName, entity, methodName)))
        {
            auditLog.logMessage(methodName, GovernanceServerAuditCode.INTERESTING_EVENT.getMessageDefinition(entity.getGUID()));
        }
    }


    /**
     * An existing entity has been deleted.  This is a soft delete. This means it is still in the repository,
     * but it is no longer returned on queries.
     * <p>
     * All relationships to the entity are also soft-deleted and will no longer be usable.  These deleted relationships
     * will be notified through separate events.
     * <p>
     * Details of the TypeDef are included with the entity's unique id (guid) to ensure the right entity is deleted in
     * the remote repositories.
     *
     * @param sourceName                     name of the source of the event.  It may be the cohort name for incoming events or the
     *                                       local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName           name of the server that the event came from.
     * @param originatorServerType           type of server that the event came from.
     * @param originatorOrganizationName     name of the organization that owns the server that sent the event.
     * @param entity                         deleted entity
     */
    @Override
    public void processDeletedEntityEvent(String       sourceName,
                                          String       originatorMetadataCollectionId,
                                          String       originatorServerName,
                                          String       originatorServerType,
                                          String       originatorOrganizationName,
                                          EntityDetail entity)
    {
        final String methodName = "processDeletedEntityEvent";

        if ((processGovernanceEngineEvent(sourceName, entity, methodName)) ||
                (processIntegrationGroupEvent(sourceName, entity, methodName)) ||
                (processIntegrationConnectorEvent(sourceName, entity, methodName)) ||
                (processEngineActionEvent(sourceName, entity, methodName)))
        {
            auditLog.logMessage(methodName, GovernanceServerAuditCode.INTERESTING_EVENT.getMessageDefinition(entity.getGUID()));
        }
    }


    /**
     * An existing entity has been deleted and purged in a single action.
     * All relationships to the entity are also deleted and purged and will no longer be usable.  These deleted relationships
     * will be notified through separate events.
     *
     *
     * @param sourceName  name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId  unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName  name of the server that the event came from.
     * @param originatorServerType  type of server that the event came from.
     * @param originatorOrganizationName  name of the organization that owns the server that sent the event.
     * @param entity  deleted entity
     */
    @Override
    public void processDeletePurgedEntityEvent(String       sourceName,
                                               String       originatorMetadataCollectionId,
                                               String       originatorServerName,
                                               String       originatorServerType,
                                               String       originatorOrganizationName,
                                               EntityDetail entity)
    {
        final String methodName = "processDeletePurgedEntityEvent";

        if ((processGovernanceEngineEvent(sourceName, entity, methodName)) ||
                (processIntegrationGroupEvent(sourceName, entity, methodName)) ||
                (processIntegrationConnectorEvent(sourceName, entity, methodName)) ||
                (processEngineActionEvent(sourceName, entity, methodName)))
        {
            auditLog.logMessage(methodName, GovernanceServerAuditCode.INTERESTING_EVENT.getMessageDefinition(entity.getGUID()));
        }
    }


    /**
     * A deleted entity has been restored to the state it was before it was deleted.
     *
     * @param sourceName                     name of the source of the event.  It may be the cohort name for incoming events or the
     *                                       local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName           name of the server that the event came from.
     * @param originatorServerType           type of server that the event came from.
     * @param originatorOrganizationName     name of the organization that owns the server that sent the event.
     * @param entity                         details of the version of the entity that has been restored.
     */
    @Override
    public void processRestoredEntityEvent(String       sourceName,
                                           String       originatorMetadataCollectionId,
                                           String       originatorServerName,
                                           String       originatorServerType,
                                           String       originatorOrganizationName,
                                           EntityDetail entity)
    {
        final String methodName = "processRestoredEntityEvent";

        if ((processGovernanceEngineEvent(sourceName, entity, methodName)) ||
                (processIntegrationGroupEvent(sourceName, entity, methodName)) ||
                (processIntegrationConnectorEvent(sourceName, entity, methodName)) ||
                (processEngineActionEvent(sourceName, entity, methodName)))
        {
            auditLog.logMessage(methodName, GovernanceServerAuditCode.INTERESTING_EVENT.getMessageDefinition(entity.getGUID()));
        }
    }


    /**
     * The guid of an existing entity has been changed to a new value.  This is used if two different
     * entities are discovered to have the same guid.  This is extremely unlikely but not impossible so
     * the open metadata protocol has provision for this.
     *
     * @param sourceName                     name of the source of the event.  It may be the cohort name for incoming events or the
     *                                       local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName           name of the server that the event came from.
     * @param originatorServerType           type of server that the event came from.
     * @param originatorOrganizationName     name of the organization that owns the server that sent the event.
     * @param originalEntityGUID             the existing identifier for the entity.
     * @param entity                         new values for this entity, including the new guid.
     */
    @Override
    public void processReIdentifiedEntityEvent(String       sourceName,
                                               String       originatorMetadataCollectionId,
                                               String       originatorServerName,
                                               String       originatorServerType,
                                               String       originatorOrganizationName,
                                               String       originalEntityGUID,
                                               EntityDetail entity)
    {
        final String methodName = "processReIdentifiedEntityEvent";

        if ((processGovernanceEngineEvent(sourceName, entity, methodName)) ||
                (processIntegrationGroupEvent(sourceName, entity, methodName)) ||
                (processIntegrationConnectorEvent(sourceName, entity, methodName)) ||
                (processEngineActionEvent(sourceName, entity, methodName)))
        {
            auditLog.logMessage(methodName, GovernanceServerAuditCode.INTERESTING_EVENT.getMessageDefinition(entity.getGUID()));
        }
    }


    /**
     * An existing entity has had its type changed.  Typically, this action is taken to move an entity's
     * type to either a super type (so the subtype can be deleted) or a new subtype (so additional properties can be
     * added.)  However, the type can be changed to any compatible type.
     *
     * @param sourceName                     name of the source of the event.  It may be the cohort name for incoming events or the
     *                                       local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName           name of the server that the event came from.
     * @param originatorServerType           type of server that the event came from.
     * @param originatorOrganizationName     name of the organization that owns the server that sent the event.
     * @param originalTypeDefSummary         original details of this entity's TypeDef.
     * @param entity                         new values for this entity, including the new type information.
     */
    @Override
    public void processReTypedEntityEvent(String         sourceName,
                                          String         originatorMetadataCollectionId,
                                          String         originatorServerName,
                                          String         originatorServerType,
                                          String         originatorOrganizationName,
                                          TypeDefSummary originalTypeDefSummary,
                                          EntityDetail   entity)
    {
        final String methodName = "processReTypedEntityEvent";

        if ((processGovernanceEngineEvent(sourceName, entity, methodName)) ||
                (processIntegrationGroupEvent(sourceName, entity, methodName)) ||
                (processIntegrationConnectorEvent(sourceName, entity, methodName)) ||
                (processEngineActionEvent(sourceName, entity, methodName)))
        {
            auditLog.logMessage(methodName, GovernanceServerAuditCode.INTERESTING_EVENT.getMessageDefinition(entity.getGUID()));
        }
    }


    /**
     * An existing entity has changed home repository.  This action is taken for example, if a repository
     * becomes permanently unavailable, or if the user community updating this entity move to working
     * from a different repository in the open metadata repository cohort.
     *
     * @param sourceName                       name of the source of the event.  It may be the cohort name for incoming events or the
     *                                         local repository, or event mapper name.
     * @param originatorMetadataCollectionId   unique identifier for the metadata collection hosted by the server that
     *                                         sent the event.
     * @param originatorServerName             name of the server that the event came from.
     * @param originatorServerType             type of server that the event came from.
     * @param originatorOrganizationName       name of the organization that owns the server that sent the event.
     * @param originalHomeMetadataCollectionId unique identifier for the original home repository.
     * @param entity                           new values for this entity, including the new home information.
     */
    @Override
    public void processReHomedEntityEvent(String       sourceName,
                                          String       originatorMetadataCollectionId,
                                          String       originatorServerName,
                                          String       originatorServerType,
                                          String       originatorOrganizationName,
                                          String       originalHomeMetadataCollectionId,
                                          EntityDetail entity)
    {
        final String methodName = "processReHomedEntityEvent";

        if ((processGovernanceEngineEvent(sourceName, entity, methodName)) ||
                (processIntegrationGroupEvent(sourceName, entity, methodName)) ||
                (processIntegrationConnectorEvent(sourceName, entity, methodName)) ||
                (processEngineActionEvent(sourceName, entity, methodName)))
        {
            auditLog.logMessage(methodName, GovernanceServerAuditCode.INTERESTING_EVENT.getMessageDefinition(entity.getGUID()));
        }
    }


    /**
     * A remote repository in the cohort has sent entity details in response to a refresh request.
     *
     * @param sourceName                     name of the source of the event.  It may be the cohort name for incoming events or the
     *                                       local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName           name of the server that the event came from.
     * @param originatorServerType           type of server that the event came from.
     * @param originatorOrganizationName     name of the organization that owns the server that sent the event.
     * @param entity                         details of the requested entity
     */
    @Override
    public void processRefreshEntityEvent(String       sourceName,
                                          String       originatorMetadataCollectionId,
                                          String       originatorServerName,
                                          String       originatorServerType,
                                          String       originatorOrganizationName,
                                          EntityDetail entity)
    {
        final String methodName = "processRefreshEntityEvent";

        if ((processGovernanceEngineEvent(sourceName, entity, methodName)) ||
                (processIntegrationGroupEvent(sourceName, entity, methodName)) ||
                (processIntegrationConnectorEvent(sourceName, entity, methodName)) ||
                (processEngineActionEvent(sourceName, entity, methodName)))
        {
            auditLog.logMessage(methodName, GovernanceServerAuditCode.INTERESTING_EVENT.getMessageDefinition(entity.getGUID()));
        }
    }


    /**
     * A new relationship has been created.
     *
     * @param sourceName                     name of the source of the event.  It may be the cohort name for incoming events or the
     *                                       local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName           name of the server that the event came from.
     * @param originatorServerType           type of server that the event came from.
     * @param originatorOrganizationName     name of the organization that owns the server that sent the event.
     * @param relationship                   details of the new relationship
     */
    @Override
    public void processNewRelationshipEvent(String       sourceName,
                                            String       originatorMetadataCollectionId,
                                            String       originatorServerName,
                                            String       originatorServerType,
                                            String       originatorOrganizationName,
                                            Relationship relationship)
    {
        final String methodName = "processNewRelationshipEvent";

        if ((processSupportedGovernanceService(sourceName, relationship, methodName)) ||
            (processRegisteredIntegrationConnector(sourceName, relationship, methodName)))
        {
            auditLog.logMessage(methodName, GovernanceServerAuditCode.INTERESTING_EVENT.getMessageDefinition(relationship.getGUID()));
        }
    }


    /**
     * An existing relationship has been updated.
     *
     * @param sourceName                     name of the source of the event.  It may be the cohort name for incoming events or the
     *                                       local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName           name of the server that the event came from.
     * @param originatorServerType           type of server that the event came from.
     * @param originatorOrganizationName     name of the organization that owns the server that sent the event.
     * @param oldRelationship                original details of the relationship.
     * @param newRelationship                details of the new version of the relationship.
     */
    @Override
    public void processUpdatedRelationshipEvent(String       sourceName,
                                                String       originatorMetadataCollectionId,
                                                String       originatorServerName,
                                                String       originatorServerType,
                                                String       originatorOrganizationName,
                                                Relationship oldRelationship,
                                                Relationship newRelationship)
    {
        final String methodName = "processUpdatedRelationshipEvent";

        if ((processSupportedGovernanceService(sourceName, newRelationship, methodName)) ||
                (processRegisteredIntegrationConnector(sourceName, newRelationship, methodName)))
        {
            auditLog.logMessage(methodName, GovernanceServerAuditCode.INTERESTING_EVENT.getMessageDefinition(newRelationship.getGUID()));
        }
    }


    /**
     * An update to a relationship has been undone.
     *
     * @param sourceName                     name of the source of the event.  It may be the cohort name for incoming events or the
     *                                       local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName           name of the server that the event came from.
     * @param originatorServerType           type of server that the event came from.
     * @param originatorOrganizationName     name of the organization that owns the server that sent the event.
     * @param relationship                   details of the version of the relationship that has been restored.
     */
    @Override
    public void processUndoneRelationshipEvent(String       sourceName,
                                               String       originatorMetadataCollectionId,
                                               String       originatorServerName,
                                               String       originatorServerType,
                                               String       originatorOrganizationName,
                                               Relationship relationship)
    {
        final String methodName = "processUndoneRelationshipEvent";

        if ((processSupportedGovernanceService(sourceName, relationship, methodName)) ||
                (processRegisteredIntegrationConnector(sourceName, relationship, methodName)))
        {
            auditLog.logMessage(methodName, GovernanceServerAuditCode.INTERESTING_EVENT.getMessageDefinition(relationship.getGUID()));
        }
    }


    /**
     * An existing relationship has been deleted.  This is a soft delete. This means it is still in the repository,
     * but it is no longer returned on queries.
     * <p>
     * Details of the TypeDef are included with the relationship's unique id (guid) to ensure the right
     * relationship is deleted in the remote repositories.
     *
     * @param sourceName                     name of the source of the event.  It may be the cohort name for incoming events or the
     *                                       local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName           name of the server that the event came from.
     * @param originatorServerType           type of server that the event came from.
     * @param originatorOrganizationName     name of the organization that owns the server that sent the event.
     * @param relationship                   deleted relationship
     */
    @Override
    public void processDeletedRelationshipEvent(String       sourceName,
                                                String       originatorMetadataCollectionId,
                                                String       originatorServerName,
                                                String       originatorServerType,
                                                String       originatorOrganizationName,
                                                Relationship relationship)
    {
        final String methodName = "processDeletedRelationshipEvent";

        if ((processSupportedGovernanceService(sourceName, relationship, methodName)) ||
                (processRegisteredIntegrationConnector(sourceName, relationship, methodName)))
        {
            auditLog.logMessage(methodName, GovernanceServerAuditCode.INTERESTING_EVENT.getMessageDefinition(relationship.getGUID()));
        }
    }


    /**
     * An active relationship has been deleted and purged from the repository.  This request can not be undone.
     *
     * @param sourceName  name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId  unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName  name of the server that the event came from.
     * @param originatorServerType type of server that the event came from.
     * @param originatorOrganizationName  name of the organization that owns the server that sent the event.
     * @param relationship  deleted relationship
     */
    @Override
    public void processDeletePurgedRelationshipEvent(String       sourceName,
                                                     String       originatorMetadataCollectionId,
                                                     String       originatorServerName,
                                                     String       originatorServerType,
                                                     String       originatorOrganizationName,
                                                     Relationship relationship)
    {
        final String methodName = "processDeletePurgedRelationshipEvent";

        if ((processSupportedGovernanceService(sourceName, relationship, methodName)) ||
                (processRegisteredIntegrationConnector(sourceName, relationship, methodName)))
        {
            auditLog.logMessage(methodName, GovernanceServerAuditCode.INTERESTING_EVENT.getMessageDefinition(relationship.getGUID()));
        }
    }


    /**
     * A deleted relationship has been restored to the state it was before it was deleted.
     *
     * @param sourceName                     name of the source of the event.  It may be the cohort name for incoming events or the
     *                                       local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName           name of the server that the event came from.
     * @param originatorServerType           type of server that the event came from.
     * @param originatorOrganizationName     name of the organization that owns the server that sent the event.
     * @param relationship                   details of the version of the relationship that has been restored.
     */
    @Override
    public void processRestoredRelationshipEvent(String       sourceName,
                                                 String       originatorMetadataCollectionId,
                                                 String       originatorServerName,
                                                 String       originatorServerType,
                                                 String       originatorOrganizationName,
                                                 Relationship relationship)
    {
        final String methodName = "processRestoredRelationshipEvent";

        if ((processSupportedGovernanceService(sourceName, relationship, methodName)) ||
                (processRegisteredIntegrationConnector(sourceName, relationship, methodName)))
        {
            auditLog.logMessage(methodName, GovernanceServerAuditCode.INTERESTING_EVENT.getMessageDefinition(relationship.getGUID()));
        }
    }


    /**
     * The guid of an existing relationship has changed.  This is used if two different
     * relationships are discovered to have the same guid.  This is extremely unlikely but not impossible so
     * the open metadata protocol has provision for this.
     *
     * @param sourceName                     name of the source of the event.  It may be the cohort name for incoming events or the
     *                                       local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName           name of the server that the event came from.
     * @param originatorServerType           type of server that the event came from.
     * @param originatorOrganizationName     name of the organization that owns the server that sent the event.
     * @param originalRelationshipGUID       the existing identifier for the relationship.
     * @param relationship                   new values for this relationship, including the new guid.
     */
    @Override
    public void processReIdentifiedRelationshipEvent(String       sourceName,
                                                     String       originatorMetadataCollectionId,
                                                     String       originatorServerName,
                                                     String       originatorServerType,
                                                     String       originatorOrganizationName,
                                                     String       originalRelationshipGUID,
                                                     Relationship relationship)
    {
        final String methodName = "processReIdentifiedRelationshipEvent";

        if ((processSupportedGovernanceService(sourceName, relationship, methodName)) ||
                (processRegisteredIntegrationConnector(sourceName, relationship, methodName)))
        {
            auditLog.logMessage(methodName, GovernanceServerAuditCode.INTERESTING_EVENT.getMessageDefinition(relationship.getGUID()));
        }
    }


    /**
     * An existing relationship has had its type changed.  Typically, this action is taken to move a relationship's
     * type to either a super type (so the subtype can be deleted) or a new subtype (so additional properties can be
     * added.)  However, the type can be changed to any compatible type.
     *
     * @param sourceName                     name of the source of the event.  It may be the cohort name for incoming events or the
     *                                       local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName           name of the server that the event came from.
     * @param originatorServerType           type of server that the event came from.
     * @param originatorOrganizationName     name of the organization that owns the server that sent the event.
     * @param originalTypeDefSummary         original details of this relationship's TypeDef.
     * @param relationship                   new values for this relationship, including the new type information.
     */
    @Override
    public void processReTypedRelationshipEvent(String         sourceName,
                                                String         originatorMetadataCollectionId,
                                                String         originatorServerName,
                                                String         originatorServerType,
                                                String         originatorOrganizationName,
                                                TypeDefSummary originalTypeDefSummary,
                                                Relationship   relationship)
    {
        final String methodName = "processReTypedRelationshipEvent";

        if ((processSupportedGovernanceService(sourceName, relationship, methodName)) ||
                (processRegisteredIntegrationConnector(sourceName, relationship, methodName)))
        {
            auditLog.logMessage(methodName, GovernanceServerAuditCode.INTERESTING_EVENT.getMessageDefinition(relationship.getGUID()));
        }
    }


    /**
     * An existing relationship has changed home repository.  This action is taken for example, if a repository
     * becomes permanently unavailable, or if the user community updating this relationship move to working
     * from a different repository in the open metadata repository cohort.
     *
     * @param sourceName                     name of the source of the event.  It may be the cohort name for incoming events or the
     *                                       local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName           name of the server that the event came from.
     * @param originatorServerType           type of server that the event came from.
     * @param originatorOrganizationName     name of the organization that owns the server that sent the event.
     * @param originalHomeMetadataCollection unique identifier for the original home repository.
     * @param relationship                   new values for this relationship, including the new home information.
     */
    @Override
    public void processReHomedRelationshipEvent(String       sourceName,
                                                String       originatorMetadataCollectionId,
                                                String       originatorServerName,
                                                String       originatorServerType,
                                                String       originatorOrganizationName,
                                                String       originalHomeMetadataCollection,
                                                Relationship relationship)
    {
        final String methodName = "processReHomedRelationshipEvent";

        if ((processSupportedGovernanceService(sourceName, relationship, methodName)) ||
                (processRegisteredIntegrationConnector(sourceName, relationship, methodName)))
        {
            auditLog.logMessage(methodName, GovernanceServerAuditCode.INTERESTING_EVENT.getMessageDefinition(relationship.getGUID()));
        }
    }


    /**
     * The local repository is refreshing the information about a relationship for the other
     * repositories in the cohort.
     *
     * @param sourceName                     name of the source of the event.  It may be the cohort name for incoming events or the
     *                                       local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName           name of the server that the event came from.
     * @param originatorServerType           type of server that the event came from.
     * @param originatorOrganizationName     name of the organization that owns the server that sent the event.
     * @param relationship                   relationship details
     */
    @Override
    public void processRefreshRelationshipEvent(String       sourceName,
                                                String       originatorMetadataCollectionId,
                                                String       originatorServerName,
                                                String       originatorServerType,
                                                String       originatorOrganizationName,
                                                Relationship relationship)
    {
        final String methodName = "processRefreshRelationshipEvent";

        if ((processSupportedGovernanceService(sourceName, relationship, methodName)) ||
                (processRegisteredIntegrationConnector(sourceName, relationship, methodName)))
        {
            auditLog.logMessage(methodName, GovernanceServerAuditCode.INTERESTING_EVENT.getMessageDefinition(relationship.getGUID()));
        }
    }
}
