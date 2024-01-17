/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.governanceengine.outtopic;

import org.odpi.openmetadata.accessservices.governanceengine.converters.GovernanceEngineOMASConverter;
import org.odpi.openmetadata.accessservices.governanceengine.ffdc.GovernanceEngineAuditCode;
import org.odpi.openmetadata.accessservices.governanceengine.handlers.MetadataElementHandler;
import org.odpi.openmetadata.accessservices.governanceengine.metadataelements.GovernanceActionElement;
import org.odpi.openmetadata.accessservices.governanceengine.metadataelements.MetadataElement;
import org.odpi.openmetadata.commonservices.generichandlers.EngineActionHandler;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementControlHeader;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementOrigin;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementOriginCategory;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementStatus;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementStub;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementVersions;
import org.odpi.openmetadata.frameworks.governanceaction.events.WatchdogClassificationEvent;
import org.odpi.openmetadata.frameworks.governanceaction.events.WatchdogEventType;
import org.odpi.openmetadata.frameworks.governanceaction.events.WatchdogMetadataElementEvent;
import org.odpi.openmetadata.frameworks.governanceaction.events.WatchdogRelatedElementsEvent;
import org.odpi.openmetadata.frameworks.governanceaction.properties.AttachedClassification;
import org.odpi.openmetadata.frameworks.governanceaction.properties.EngineActionStatus;
import org.odpi.openmetadata.frameworks.governanceaction.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RelatedMetadataElements;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicListenerBase;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntitySummary;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceAuditHeader;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProvenanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.RelationshipDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefLink;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefSummary;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.List;


/**
 * GovernanceEngineOMRSTopicListener is the listener that registers with the repository services (OMRS)
 * to monitor changes to the metadata.  This class is looking for changes to the governance engines
 * and governance services configuration.  When it detects an appropriate change it passes the
 * update to the GovernanceEngineOutTopicPublisher to publish a GovernanceEngineEvent describing the
 * change.  These events are picked up by the Engine Host Server (See engine-host-services) to
 * control the configuration of the governance engines it is hosting.
 */
public class GovernanceEngineOMRSTopicListener extends OMRSTopicListenerBase
{
    private final GovernanceEngineOutTopicPublisher                eventPublisher;
    private final OMRSRepositoryHelper                             repositoryHelper;
    private final MetadataElementHandler<OpenMetadataElement>  metadataElementHandler;
    private final EngineActionHandler<GovernanceActionElement> engineActionHandler;

    private final String                                           userId;
    private final String                                           serverName;

    private final EntityDetail                                     nullEntity = null;
    private final Relationship                                     nullRelationship = null;
    private final GovernanceEngineOMASConverter<MetadataElement>   converter;

    /**
     * Initialize the topic listener.
     *
     * @param serviceName this is the full name of the service - used for error logging in base class
     * @param serverName name of this server
     * @param userId local server userId for issuing requests to the repository services
     * @param metadataElementHandler handler for working with GAF objects
     * @param engineActionHandler handler for working with governance actions
     * @param eventPublisher this is the out topic publisher.
     * @param repositoryHelper repository helper
     * @param auditLog logging destination
     */
    public GovernanceEngineOMRSTopicListener(String                                           serviceName,
                                             String                                           serverName,
                                             String                                           userId,
                                             MetadataElementHandler<OpenMetadataElement>      metadataElementHandler,
                                             EngineActionHandler<GovernanceActionElement> engineActionHandler,
                                             GovernanceEngineOutTopicPublisher                eventPublisher,
                                             OMRSRepositoryHelper                             repositoryHelper,
                                             AuditLog                                         auditLog)
    {
        super(serviceName, auditLog);

        this.metadataElementHandler = metadataElementHandler;
        this.engineActionHandler = engineActionHandler;

        this.userId = userId;
        this.serverName = serverName;

        this.eventPublisher   = eventPublisher;
        this.repositoryHelper = repositoryHelper;

        this.converter = new GovernanceEngineOMASConverter<>(repositoryHelper, serviceName, metadataElementHandler.getServerName());
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
     * @return boolean flag indicating that the event is processed
     */
    private boolean excludeGovernanceEngineEvent(String         sourceName,
                                                 EntitySummary  entity)
    {
        if (entity != null)
        {
            InstanceType type = entity.getType();

            if (type != null)
            {
                return (repositoryHelper.isTypeOf(sourceName,
                                                  type.getTypeDefName(),
                                                  OpenMetadataType.GOVERNANCE_ENGINE.typeName));
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
     * @return boolean flag indicating that the event is processed
     */
    private boolean excludeSupportedGovernanceService(String       sourceName,
                                                      Relationship relationship)
    {
        if (relationship != null)
        {
            InstanceType type = relationship.getType();

            if (type != null)
            {
                 return repositoryHelper.isTypeOf(sourceName,
                                                  type.getTypeDefName(),
                                                  OpenMetadataType.SUPPORTED_GOVERNANCE_SERVICE_RELATIONSHIP.typeName);
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
    private boolean processGovernanceActionEvent(String       sourceName,
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

                        eventPublisher.publishNewGovernanceAction(governanceEngineGUID,
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
     * Process an entity extracted from an event.
     *
     * @param sourceName source of the event
     * @param entity entity from the event
     * @return boolean flag indicating that the event should be ignored
     */
    private boolean excludeGovernanceActionEvent(String        sourceName,
                                                 EntitySummary entity)
    {
        if (entity != null)
        {
            InstanceType type = entity.getType();

            if (type != null)
            {
                return repositoryHelper.isTypeOf(sourceName,
                                                 type.getTypeDefName(),
                                                 OpenMetadataType.ENGINE_ACTION_TYPE_NAME);
            }
        }

        return false;
    }



    /**
     * Ignore events from entities that are part of the governance action processing.
     * (Called after the GovernanceAction events are handled.)
     * May not need this.
     *
     * @param sourceName source of the event
     * @param entity entity from the event
     * @return boolean flag indicating that the event should be ignored
     */
    private boolean excludeGovernanceManagementEvents(String        sourceName,
                                                      EntitySummary entity)
    {
        if (entity != null)
        {
            InstanceType type = entity.getType();

            if (type != null)
            {
                return repositoryHelper.isTypeOf(sourceName,
                                                 type.getTypeDefName(),
                                                 OpenMetadataType.GOVERNANCE_ACTION_TYPE_TYPE_NAME);
            }
        }

        return false;
    }


    /**
     * Ignore events from relationships that are part of the governance action processing.
     *
     * @param sourceName source of the event
     * @param relationship relationship from the event
     * @return boolean flag indicating that the event should be ignored
     */
    private boolean excludeGovernanceManagementEvents(String            sourceName,
                                                      Relationship      relationship)
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
                    return true;
                }

                if (repositoryHelper.isTypeOf(sourceName,
                                              type.getTypeDefName(),
                                              OpenMetadataType.TARGET_FOR_ACTION_TYPE_NAME))
                {
                    return true;
                }

                if (repositoryHelper.isTypeOf(sourceName,
                                              type.getTypeDefName(),
                                              OpenMetadataType.ENGINE_ACTION_REQUEST_SOURCE_TYPE_NAME))
                {
                    return true;
                }

                return (repositoryHelper.isTypeOf(sourceName,
                                                  type.getTypeDefName(),
                                                  OpenMetadataType.ENGINE_ACTION_EXECUTOR_TYPE_NAME));
            }
        }

        return false;
    }


    /**
     * Using the content of the classification, create an element classification.
     *
     * @param classification from the repository services
     * @return open metadata element object
     */
    private AttachedClassification getClassification(Classification classification)
    {
        if (classification != null)
        {
            AttachedClassification beanClassification = new AttachedClassification();

            converter.fillElementControlHeader(beanClassification, classification);

            beanClassification.setClassificationName(classification.getName());

            if (classification.getProperties() != null)
            {
                beanClassification.setClassificationProperties(converter.mapElementProperties(classification.getProperties()));
                beanClassification.setEffectiveFromTime(classification.getProperties().getEffectiveFromTime());
                beanClassification.setEffectiveToTime(classification.getProperties().getEffectiveToTime());
            }

            return beanClassification;
        }

        return null;
    }


    /**
     * Using the content of the relationship, create a related metadata elements object.
     *
     * @param relationship relationship from the repository
     * @return related metadata elements object
     */
    private RelatedMetadataElements getRelatedElements(Relationship relationship)
    {
        if (relationship != null)
        {
            RelatedMetadataElements relatedMetadataElements = new RelatedMetadataElements();

            converter.fillElementControlHeader(relatedMetadataElements, relationship);

            relatedMetadataElements.setRelationshipGUID(relationship.getGUID());
            relatedMetadataElements.setRelationshipType(converter.getElementType(relationship));

            if (relationship.getProperties() != null)
            {
                relatedMetadataElements.setRelationshipProperties(converter.mapElementProperties(relationship.getProperties()));
                relatedMetadataElements.setEffectiveFromTime(relationship.getProperties().getEffectiveFromTime());
                relatedMetadataElements.setEffectiveToTime(relationship.getProperties().getEffectiveToTime());
            }

            if (relationship.getEntityOneProxy() != null)
            {
                relatedMetadataElements.setElementGUIDAtEnd1(relationship.getEntityOneProxy().getGUID());

                ElementStub elementStub = new ElementStub();
                fillElementControlHeader(elementStub, relationship.getEntityOneProxy());
                elementStub.setUniqueName(getQualifiedName(relationship.getEntityOneProxy().getUniqueProperties()));
                relatedMetadataElements.setElementAtEnd1(elementStub);
            }

            if (relationship.getEntityTwoProxy() != null)
            {
                relatedMetadataElements.setElementGUIDAtEnd2(relationship.getEntityTwoProxy().getGUID());
            }

            if (repositoryHelper.getTypeDefByName(serviceName, relationship.getType().getTypeDefName()) instanceof RelationshipDef relationshipDef)
            {
                relatedMetadataElements.setLabelAtEnd1(relationshipDef.getEndDef1().getAttributeName());
                relatedMetadataElements.setLabelAtEnd2(relationshipDef.getEndDef2().getAttributeName());
            }

            return relatedMetadataElements;
        }

        return null;
    }


    /**
     * Fill a GAF control header from the information in a repository services element header.
     *
     * @param elementControlHeader GAF object control header
     * @param header OMRS element header
     */
    public void fillElementControlHeader(ElementControlHeader elementControlHeader,
                                         InstanceAuditHeader header)
    {
        if (header != null)
        {
            elementControlHeader.setStatus(this.getElementStatus(header.getStatus()));
            elementControlHeader.setType(this.getElementType(header));

            ElementOrigin elementOrigin = new ElementOrigin();

            elementOrigin.setSourceServer(serverName);
            elementOrigin.setOriginCategory(this.getElementOriginCategory(header.getInstanceProvenanceType()));
            elementOrigin.setHomeMetadataCollectionId(header.getMetadataCollectionId());
            elementOrigin.setHomeMetadataCollectionName(header.getMetadataCollectionName());
            elementOrigin.setLicense(header.getInstanceLicense());

            elementControlHeader.setOrigin(elementOrigin);

            elementControlHeader.setVersions(this.getElementVersions(header));
        }
    }



    /**
     * Translate the repository services' InstanceStatus to an ElementStatus.
     *
     * @param instanceStatus value from the repository services
     * @return ElementStatus enum
     */
    protected ElementStatus getElementStatus(InstanceStatus instanceStatus)
    {
        if (instanceStatus != null)
        {
            switch (instanceStatus)
            {
                case UNKNOWN:
                    return ElementStatus.UNKNOWN;

                case DRAFT:
                    return ElementStatus.DRAFT;

                case PREPARED:
                    return ElementStatus.PREPARED;

                case PROPOSED:
                    return ElementStatus.PROPOSED;

                case APPROVED:
                    return ElementStatus.APPROVED;

                case REJECTED:
                    return ElementStatus.REJECTED;

                case APPROVED_CONCEPT:
                    return ElementStatus.APPROVED_CONCEPT;

                case UNDER_DEVELOPMENT:
                    return ElementStatus.UNDER_DEVELOPMENT;

                case DEVELOPMENT_COMPLETE:
                    return ElementStatus.DEVELOPMENT_COMPLETE;

                case APPROVED_FOR_DEPLOYMENT:
                    return ElementStatus.APPROVED_FOR_DEPLOYMENT;

                case STANDBY:
                    return ElementStatus.STANDBY;

                case ACTIVE:
                    return ElementStatus.ACTIVE;

                case FAILED:
                    return ElementStatus.FAILED;

                case DISABLED:
                    return ElementStatus.DISABLED;

                case COMPLETE:
                    return ElementStatus.COMPLETE;

                case DEPRECATED:
                    return ElementStatus.DEPRECATED;

                case OTHER:
                    return ElementStatus.OTHER;
            }
        }

        return ElementStatus.UNKNOWN;
    }



    /**
     * Convert information from a repository instance into an Open Connector Framework ElementType.
     *
     * @param instanceHeader values from the server
     * @return OCF ElementType object
     */
    public ElementType getElementType(InstanceAuditHeader instanceHeader)
    {
        ElementType elementType = new ElementType();

        InstanceType instanceType = instanceHeader.getType();

        if (instanceType != null)
        {
            String  typeDefName = instanceType.getTypeDefName();
            TypeDef typeDef     = repositoryHelper.getTypeDefByName(serviceName, typeDefName);

            elementType.setTypeId(instanceType.getTypeDefGUID());
            elementType.setTypeName(typeDefName);
            elementType.setTypeVersion(instanceType.getTypeDefVersion());
            elementType.setTypeDescription(typeDef.getDescription());

            List<TypeDefLink> typeDefSuperTypes = repositoryHelper.getSuperTypes(serviceName, typeDefName);

            if ((typeDefSuperTypes != null) && (! typeDefSuperTypes.isEmpty()))
            {
                List<String>   superTypes = new ArrayList<>();

                for (TypeDefLink typeDefLink : typeDefSuperTypes)
                {
                    if (typeDefLink != null)
                    {
                        superTypes.add(typeDefLink.getName());
                    }
                }

                if (! superTypes.isEmpty())
                {
                    elementType.setSuperTypeNames(superTypes);
                }
            }
        }

        return elementType;
    }




    /**
     * Translate the repository services' InstanceProvenanceType to an ElementOrigin.
     *
     * @param instanceProvenanceType value from the repository services
     * @return ElementOrigin enum
     */
    protected ElementOriginCategory getElementOriginCategory(InstanceProvenanceType instanceProvenanceType)
    {
        if (instanceProvenanceType != null)
        {
            switch (instanceProvenanceType)
            {
                case DEREGISTERED_REPOSITORY:
                    return ElementOriginCategory.DEREGISTERED_REPOSITORY;

                case EXTERNAL_SOURCE:
                    return ElementOriginCategory.EXTERNAL_SOURCE;

                case EXPORT_ARCHIVE:
                    return ElementOriginCategory.EXPORT_ARCHIVE;

                case LOCAL_COHORT:
                    return ElementOriginCategory.LOCAL_COHORT;

                case CONTENT_PACK:
                    return ElementOriginCategory.CONTENT_PACK;

                case CONFIGURATION:
                    return ElementOriginCategory.CONFIGURATION;

                case UNKNOWN:
                    return ElementOriginCategory.UNKNOWN;
            }
        }

        return ElementOriginCategory.UNKNOWN;
    }


    /**
     * Extract detail of the version of the element and the user's maintaining it.
     *
     * @param header audit header from the repository
     * @return ElementVersions object
     */
    protected ElementVersions getElementVersions(InstanceAuditHeader header)
    {
        ElementVersions elementVersions = new ElementVersions();

        elementVersions.setCreatedBy(header.getCreatedBy());
        elementVersions.setCreateTime(header.getCreateTime());
        elementVersions.setUpdatedBy(header.getUpdatedBy());
        elementVersions.setUpdateTime(header.getUpdateTime());
        elementVersions.setMaintainedBy(header.getMaintainedBy());
        elementVersions.setVersion(header.getVersion());

        return elementVersions;
    }


    /**
     * Extract the qualifiedName property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return string name or null
     */
    protected String getQualifiedName(InstanceProperties instanceProperties)
    {
        final String methodName = "getQualifiedName";

        if (instanceProperties != null)
        {
            return repositoryHelper.getStringProperty(serviceName,
                                                      OpenMetadataProperty.QUALIFIED_NAME.name,
                                                      instanceProperties,
                                                      methodName);
        }

        return null;
    }


    /**
     * Process an entity extracted from an event.
     *
     * @param sourceName source of the event
     * @param eventType watchdog event type
     * @param entity entity from the event
     * @param previousEntity set up for update events
     * @param methodName calling method (indicates type of event action)
     */
    private void processWatchdogEvent(String            sourceName,
                                      WatchdogEventType eventType,
                                      EntityDetail      entity,
                                      EntityDetail      previousEntity,
                                      String            methodName)
    {
        if (entity != null)
        {
            try
            {
                WatchdogMetadataElementEvent watchdogEvent = new WatchdogMetadataElementEvent();

                watchdogEvent.setEventType(eventType);
                watchdogEvent.setMetadataElement(metadataElementHandler.getMetadataElementByGUID(userId,
                                                                                                 entity.getGUID(),
                                                                                                 true,
                                                                                                 false,
                                                                                                 null,
                                                                                                 methodName));

                if (previousEntity != null)
                {
                    watchdogEvent.setPreviousMetadataElement(metadataElementHandler.getMetadataElementByGUID(userId,
                                                                                                             previousEntity.getGUID(),
                                                                                                             true,
                                                                                                             false,
                                                                                                             null,
                                                                                                             methodName));
                }

                eventPublisher.publishWatchdogEvent(watchdogEvent);
            }
            catch (InvalidParameterException error)
            {
                auditLog.logMessage(methodName,
                                      GovernanceEngineAuditCode.SKIPPING_INSTANCE.getMessageDefinition(sourceName,
                                                                                                       methodName,
                                                                                                       entity.getGUID(),
                                                                                                       error.getMessage()));
            }
            catch (Exception error)
            {
                auditLog.logException(methodName,
                                      GovernanceEngineAuditCode.EVENT_PROCESSING_ERROR.getMessageDefinition(error.getClass().getName(),
                                                                                                            sourceName,
                                                                                                            methodName,
                                                                                                            entity.getGUID(),
                                                                                                            error.getMessage()),
                                      error);
            }
        }
    }


    /**
     * Process a classification extracted from an event.
     *
     * @param sourceName source of the event
     * @param eventType watchdog event type
     * @param entity entity from the event
     * @param classification from the event
     * @param previousClassification set up for update events
     * @param methodName calling method (indicates type of event action)
     */
    private void processWatchdogEvent(String            sourceName,
                                      WatchdogEventType eventType,
                                      EntitySummary     entity,
                                      Classification    classification,
                                      Classification    previousClassification,
                                      String            methodName)
    {
        if (entity != null)
        {
            try
            {
                WatchdogClassificationEvent watchdogEvent = new WatchdogClassificationEvent();

                watchdogEvent.setEventType(eventType);
                watchdogEvent.setMetadataElement(metadataElementHandler.getMetadataElementByGUID(userId, entity.getGUID(), true, false, null, methodName));
                watchdogEvent.setChangedClassification(this.getClassification(classification));

                if (previousClassification != null)
                {
                    watchdogEvent.setChangedClassification(this.getClassification(previousClassification));
                }

                eventPublisher.publishWatchdogEvent(watchdogEvent);
            }
            catch (InvalidParameterException error)
            {
                auditLog.logMessage(methodName,
                                    GovernanceEngineAuditCode.SKIPPING_INSTANCE.getMessageDefinition(sourceName,
                                                                                                     methodName,
                                                                                                     entity.getGUID(),
                                                                                                     error.getMessage()));
            }
            catch (Exception error)
            {
                auditLog.logException(methodName,
                                      GovernanceEngineAuditCode.EVENT_PROCESSING_ERROR.getMessageDefinition(error.getClass().getName(),
                                                                                                            sourceName,
                                                                                                            methodName,
                                                                                                            entity.getGUID(),
                                                                                                            error.getMessage()),
                                      error);
            }
        }
    }


    /**
     * Process a relationship extracted from an event.
     *
     * @param sourceName source of the event
     * @param eventType watchdog event type
     * @param relationship relationship from the event
     * @param previousRelationship previous value of the relationship
     * @param methodName calling method (indicates type of event action)
     */
    private void processWatchdogEvent(String            sourceName,
                                      WatchdogEventType eventType,
                                      Relationship      relationship,
                                      Relationship      previousRelationship,
                                      String            methodName)
    {
        if (relationship != null)
        {
            try
            {
                /*
                 * Validate that the relationship is visible to this service
                 */
                metadataElementHandler.getMetadataElementByGUID(userId, relationship.getEntityOneProxy().getGUID(), true, false, null, methodName);
                metadataElementHandler.getMetadataElementByGUID(userId, relationship.getEntityTwoProxy().getGUID(), true, false, null, methodName);

                /*
                 * OK to publish relationship
                 */
                WatchdogRelatedElementsEvent watchdogEvent = new WatchdogRelatedElementsEvent();

                watchdogEvent.setEventType(eventType);
                watchdogEvent.setRelatedMetadataElements(this.getRelatedElements(relationship));

                if (previousRelationship != null)
                {
                    watchdogEvent.setPreviousRelatedMetadataElements(this.getRelatedElements(previousRelationship));
                }

                eventPublisher.publishWatchdogEvent(watchdogEvent);
            }
            catch (InvalidParameterException error)
            {
                auditLog.logMessage(methodName,
                                    GovernanceEngineAuditCode.SKIPPING_INSTANCE.getMessageDefinition(sourceName,
                                                                                                     methodName,
                                                                                                     relationship.getGUID(),
                                                                                                     error.getMessage()));
            }
            catch (Exception error)
            {
                auditLog.logException(methodName,
                                      GovernanceEngineAuditCode.EVENT_PROCESSING_ERROR.getMessageDefinition(error.getClass().getName(),
                                                                                                            sourceName,
                                                                                                            methodName,
                                                                                                            relationship.getGUID(),
                                                                                                            error.getMessage()),
                                      error);
            }
        }
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

        if ((! processGovernanceEngineEvent(sourceName, entity, methodName)) &&
                    (! processGovernanceActionEvent(sourceName, entity, methodName)) &&
                    (! excludeGovernanceManagementEvents(sourceName, entity)))
        {
            processWatchdogEvent(sourceName, WatchdogEventType.NEW_ELEMENT, entity, nullEntity, methodName);
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

        if ((! processGovernanceEngineEvent(sourceName, newEntity, methodName)) &&
                    (! processGovernanceActionEvent(sourceName, newEntity, methodName)) &&
                    (! excludeGovernanceManagementEvents(sourceName, newEntity)))
        {
            processWatchdogEvent(sourceName, WatchdogEventType.UPDATED_ELEMENT_PROPERTIES, newEntity, oldEntity, methodName);
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

        if ((! processGovernanceEngineEvent(sourceName, entity, methodName)) &&
                    (! processGovernanceActionEvent(sourceName, entity, methodName)) &&
                    (! excludeGovernanceManagementEvents(sourceName, entity)))
        {
            processWatchdogEvent(sourceName, WatchdogEventType.UPDATED_ELEMENT_PROPERTIES, entity, nullEntity, methodName);
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

        if ((! excludeGovernanceEngineEvent(sourceName, entity)) &&
                    (! excludeGovernanceActionEvent(sourceName, entity)) &&
                    (! excludeGovernanceManagementEvents(sourceName, entity)))
        {
            processWatchdogEvent(sourceName, WatchdogEventType.NEW_CLASSIFICATION, entity, classification, null, methodName);
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

        if ((! excludeGovernanceEngineEvent(sourceName, entity)) &&
                    (! excludeGovernanceActionEvent(sourceName, entity)) &&
                    (! excludeGovernanceManagementEvents(sourceName, entity)))
        {
            processWatchdogEvent(sourceName, WatchdogEventType.NEW_CLASSIFICATION, entity, classification, null, methodName);
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

        if ((! excludeGovernanceEngineEvent(sourceName, entity)) &&
                    (! excludeGovernanceActionEvent(sourceName, entity)) &&
                    (! excludeGovernanceManagementEvents(sourceName, entity)))
        {
            processWatchdogEvent(sourceName, WatchdogEventType.DELETED_CLASSIFICATION, entity, originalClassification, null, methodName);
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

        if ((! excludeGovernanceEngineEvent(sourceName, entity)) &&
                    (! excludeGovernanceActionEvent(sourceName, entity)) &&
                    (! excludeGovernanceManagementEvents(sourceName, entity)))
        {
            processWatchdogEvent(sourceName, WatchdogEventType.DELETED_CLASSIFICATION, entity, originalClassification, null, methodName);
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

        if ((! excludeGovernanceEngineEvent(sourceName, entity)) &&
                    (! excludeGovernanceActionEvent(sourceName, entity)) &&
                    (! excludeGovernanceManagementEvents(sourceName, entity)))
        {
            processWatchdogEvent(sourceName, WatchdogEventType.UPDATED_CLASSIFICATION_PROPERTIES, entity, classification, originalClassification, methodName);
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

        if ((! excludeGovernanceEngineEvent(sourceName, entity)) &&
                    (! excludeGovernanceActionEvent(sourceName, entity)) &&
                    (! excludeGovernanceManagementEvents(sourceName, entity)))
        {
            processWatchdogEvent(sourceName, WatchdogEventType.UPDATED_CLASSIFICATION_PROPERTIES, entity, classification, originalClassification, methodName);
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

        if ((! processGovernanceEngineEvent(sourceName, entity, methodName)) &&
                    (! processGovernanceActionEvent(sourceName, entity, methodName)) &&
                    (! excludeGovernanceManagementEvents(sourceName, entity)))
        {
            processWatchdogEvent(sourceName, WatchdogEventType.DELETED_ELEMENT, entity, nullEntity, methodName);
        }
    }


    /**
     * An existing entity has been deleted and purged in a single action.
     *
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

        if ((! processGovernanceEngineEvent(sourceName, entity, methodName)) &&
                    (! processGovernanceActionEvent(sourceName, entity, methodName)) &&
                    (! excludeGovernanceManagementEvents(sourceName, entity)))
        {
            processWatchdogEvent(sourceName, WatchdogEventType.DELETED_ELEMENT, entity, nullEntity, methodName);
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

        if ((! processGovernanceEngineEvent(sourceName, entity, methodName)) &&
                    (! processGovernanceActionEvent(sourceName, entity, methodName)) &&
                    (! excludeGovernanceManagementEvents(sourceName, entity)))
        {
            processWatchdogEvent(sourceName, WatchdogEventType.REFRESHED_ELEMENT, entity, nullEntity, methodName);
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

        if ((! processGovernanceEngineEvent(sourceName, entity, methodName)) &&
                    (! processGovernanceActionEvent(sourceName, entity, methodName)) &&
                    (! excludeGovernanceManagementEvents(sourceName, entity)))
        {
            processWatchdogEvent(sourceName, WatchdogEventType.REFRESHED_ELEMENT, entity, nullEntity, methodName);
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

        if ((! processGovernanceEngineEvent(sourceName, entity, methodName)) &&
                    (! processGovernanceActionEvent(sourceName, entity, methodName)) &&
                    (! excludeGovernanceManagementEvents(sourceName, entity)))
        {
            processWatchdogEvent(sourceName, WatchdogEventType.REFRESHED_ELEMENT, entity, nullEntity, methodName);
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

        if ((! processGovernanceEngineEvent(sourceName, entity, methodName)) &&
                    (! processGovernanceActionEvent(sourceName, entity, methodName)) &&
                    (! excludeGovernanceManagementEvents(sourceName, entity)))
        {
            processWatchdogEvent(sourceName, WatchdogEventType.REFRESHED_ELEMENT, entity, nullEntity, methodName);
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

        if ((! excludeGovernanceEngineEvent(sourceName, entity)) &&
                    (! excludeGovernanceActionEvent(sourceName, entity)) &&
                    (! excludeGovernanceManagementEvents(sourceName, entity)))
        {
            processWatchdogEvent(sourceName, WatchdogEventType.REFRESHED_ELEMENT, entity, nullEntity, methodName);
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

        if ((! processSupportedGovernanceService(sourceName, relationship, methodName)) &&
                    (! excludeGovernanceManagementEvents(sourceName, relationship)))
        {
            processWatchdogEvent(sourceName, WatchdogEventType.NEW_RELATIONSHIP, relationship, nullRelationship, methodName);
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

        if ((! processSupportedGovernanceService(sourceName, newRelationship, methodName)) &&
                    (! excludeGovernanceManagementEvents(sourceName, newRelationship)))
        {
            processWatchdogEvent(sourceName, WatchdogEventType.UPDATED_RELATIONSHIP_PROPERTIES, newRelationship, oldRelationship, methodName);
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

        if ((! processSupportedGovernanceService(sourceName, relationship, methodName)) &&
                    (! excludeGovernanceManagementEvents(sourceName, relationship)))
        {
            processWatchdogEvent(sourceName, WatchdogEventType.REFRESHED_RELATIONSHIP, relationship, nullRelationship, methodName);
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

        if ((! processSupportedGovernanceService(sourceName, relationship, methodName)) &&
                    (! excludeGovernanceManagementEvents(sourceName, relationship)))
        {
            processWatchdogEvent(sourceName, WatchdogEventType.DELETED_RELATIONSHIP, relationship, nullRelationship, methodName);
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

        if ((! processSupportedGovernanceService(sourceName, relationship, methodName)) &&
                    (! excludeGovernanceManagementEvents(sourceName, relationship)))
        {
            processWatchdogEvent(sourceName, WatchdogEventType.DELETED_RELATIONSHIP, relationship, nullRelationship, methodName);
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

        if ((! processSupportedGovernanceService(sourceName, relationship, methodName)) &&
                    (! excludeGovernanceManagementEvents(sourceName, relationship)))
        {
            processWatchdogEvent(sourceName, WatchdogEventType.REFRESHED_RELATIONSHIP, relationship, nullRelationship, methodName);
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

        if ((! processSupportedGovernanceService(sourceName, relationship, methodName)) &&
                    (! excludeGovernanceManagementEvents(sourceName, relationship)))
        {
            processWatchdogEvent(sourceName, WatchdogEventType.REFRESHED_RELATIONSHIP, relationship, nullRelationship, methodName);
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

        if ((! processSupportedGovernanceService(sourceName, relationship, methodName)) &&
                    (! excludeGovernanceManagementEvents(sourceName, relationship)))
        {
            processWatchdogEvent(sourceName, WatchdogEventType.REFRESHED_RELATIONSHIP, relationship, nullRelationship, methodName);
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

        if ((! processSupportedGovernanceService(sourceName, relationship, methodName)) &&
                    (! excludeGovernanceManagementEvents(sourceName, relationship)))
        {
            processWatchdogEvent(sourceName, WatchdogEventType.REFRESHED_RELATIONSHIP, relationship, nullRelationship, methodName);
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

        if ((! excludeSupportedGovernanceService(sourceName, relationship)) &&
                    (! excludeGovernanceManagementEvents(sourceName, relationship)))
        {
            processWatchdogEvent(sourceName, WatchdogEventType.REFRESHED_RELATIONSHIP, relationship, nullRelationship, methodName);
        }
    }
}
