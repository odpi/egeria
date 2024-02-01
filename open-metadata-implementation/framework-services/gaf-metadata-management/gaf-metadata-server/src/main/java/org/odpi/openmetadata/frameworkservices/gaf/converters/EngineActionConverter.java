/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.gaf.converters;

import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;
import org.odpi.openmetadata.frameworks.governanceaction.properties.ActionTargetElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.EngineActionElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RelatedEngineActionElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RequestSourceElement;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;


/**
 * EngineActionConverter transfers the relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into a Governance Action bean.
 */
public class EngineActionConverter<B> extends OpenMetadataStoreConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity/relationship objects
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public EngineActionConverter(OMRSRepositoryHelper repositoryHelper,
                                 String               serviceName,
                                 String               serverName)
    {
        super(repositoryHelper, serviceName, serverName);
    }


    /**
     * Using the supplied instances, return a new instance of the bean.  It is used for beans such as
     * a connection bean which made up of 3 entities (Connection, ConnectorType and Endpoint) plus the
     * relationships between them.  The relationships may be omitted if they do not have any properties.
     *
     * @param beanClass name of the class to create
     * @param primaryEntity entity that is the root of the collection of entities that make up the content of the bean
     * @param supplementaryEntities entities connected to the primary entity by the relationships
     * @param relationships relationships linking the entities
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    @Override
    public B getNewComplexBean(Class<B>           beanClass,
                               EntityDetail       primaryEntity,
                               List<EntityDetail> supplementaryEntities,
                               List<Relationship> relationships,
                               String             methodName) throws PropertyServerException
    {
        try
        {
            /*
             * This is initial confirmation that the generic converter has been initialized with an appropriate bean class.
             */
            B returnBean = beanClass.getDeclaredConstructor().newInstance();

            if (returnBean instanceof EngineActionElement bean)
            {
                if (primaryEntity != null)
                {
                    /*
                     * Check that the entity is of the correct type.
                     */
                    bean.setElementHeader(this.getMetadataElementHeader(beanClass, primaryEntity, methodName));

                    bean.setRequestedTime(primaryEntity.getCreateTime());

                    /*
                     * The initial set of values come from the entity properties.  The super class properties are removed from a copy of the entities
                     * properties, leaving any subclass properties to be stored in extended properties.
                     */
                    InstanceProperties instanceProperties = new InstanceProperties(primaryEntity.getProperties());

                    bean.setQualifiedName(this.removeQualifiedName(instanceProperties));
                    bean.setDomainIdentifier(this.removeDomainIdentifier(instanceProperties));
                    bean.setDisplayName(this.removeDisplayName(instanceProperties));
                    bean.setDescription(this.removeDescription(instanceProperties));
                    bean.setRequestType(this.removeRequestType(instanceProperties));
                    bean.setRequestParameters(this.removeRequestParameters(instanceProperties));
                    bean.setGovernanceEngineGUID(this.removeExecutorEngineGUID(instanceProperties));
                    bean.setGovernanceEngineName(this.removeExecutorEngineName(instanceProperties));
                    bean.setProcessName(this.removeProcessName(instanceProperties));
                    bean.setProcessStepGUID(this.removeProcessStepGUID(instanceProperties));
                    bean.setProcessStepName(this.removeProcessStepName(instanceProperties));
                    bean.setGovernanceActionTypeGUID(this.removeGovernanceActionTypeGUID(instanceProperties));
                    bean.setGovernanceActionTypeName(this.removeGovernanceActionTypeName(instanceProperties));
                    bean.setMandatoryGuards(this.removeMandatoryGuards(instanceProperties));
                    bean.setReceivedGuards(this.removeReceivedGuards(instanceProperties));
                    bean.setActionStatus(this.removeActionStatus(OpenMetadataType.ACTION_STATUS_PROPERTY_NAME, instanceProperties));
                    bean.setStartTime(this.removeStartDate(instanceProperties));
                    bean.setProcessingEngineUserId(this.removeProcessingEngineUserId(instanceProperties));
                    bean.setCompletionTime(this.removeCompletionDate(instanceProperties));
                    bean.setCompletionGuards(this.removeCompletionGuards(instanceProperties));
                    bean.setCompletionMessage(this.removeCompletionMessage(instanceProperties));
                    bean.setAdditionalProperties(this.removeAdditionalProperties(instanceProperties));

                    if (relationships != null)
                    {
                        List<RequestSourceElement> requestSourceElements          = new ArrayList<>();
                        List<ActionTargetElement>        actionTargetElements = new ArrayList<>();
                        List<RelatedEngineActionElement> previousActions      = new ArrayList<>();
                        List<RelatedEngineActionElement> followOnActions      = new ArrayList<>();

                        for (Relationship relationship : relationships)
                        {
                            if ((relationship != null) && (relationship.getType() != null))
                            {
                                String actualTypeName = relationship.getType().getTypeDefName();
                                instanceProperties = new InstanceProperties(relationship.getProperties());

                                if (repositoryHelper.isTypeOf(serviceName, actualTypeName, OpenMetadataType.ENGINE_ACTION_EXECUTOR_TYPE_NAME))
                                {
                                    if (bean.getRequestType() == null)
                                    {
                                        bean.setRequestType(this.removeRequestType(instanceProperties));
                                        bean.setRequestParameters(this.removeRequestParameters(instanceProperties));
                                    }

                                    if (bean.getGovernanceEngineGUID() == null)
                                    {
                                        EntityProxy entityProxy = relationship.getEntityTwoProxy();

                                        bean.setGovernanceEngineGUID(entityProxy.getGUID());

                                        if (entityProxy.getUniqueProperties() != null)
                                        {
                                            bean.setGovernanceEngineName(this.getQualifiedName(entityProxy.getUniqueProperties()));
                                        }
                                    }
                                }
                                else if (repositoryHelper.isTypeOf(serviceName, actualTypeName, OpenMetadataType.TARGET_FOR_ACTION_TYPE_NAME))
                                {
                                    ActionTargetElement actionTargetElement = new ActionTargetElement();

                                    actionTargetElement.setActionTargetName(this.removeActionTargetName(instanceProperties));
                                    actionTargetElement.setStatus(this.removeActionStatus(OpenMetadataType.STATUS_PROPERTY_NAME, instanceProperties));
                                    actionTargetElement.setStartDate(this.removeStartDate(instanceProperties));
                                    actionTargetElement.setCompletionDate(this.removeCompletionDate(instanceProperties));
                                    actionTargetElement.setCompletionMessage(this.removeCompletionMessage(instanceProperties));

                                    String actionTargetGUID = relationship.getEntityTwoProxy().getGUID();

                                    if (actionTargetGUID != null)
                                    {
                                        actionTargetElement.setActionTargetGUID(actionTargetGUID);
                                        actionTargetElement.setTargetElement(this.getOpenMetadataElement(actionTargetGUID, supplementaryEntities));
                                    }

                                    actionTargetElements.add(actionTargetElement);
                                }
                                else if (repositoryHelper.isTypeOf(serviceName, actualTypeName, OpenMetadataType.ENGINE_ACTION_REQUEST_SOURCE_TYPE_NAME))
                                {
                                    String requestSourceGUID = relationship.getEntityOneProxy().getGUID();

                                    if (requestSourceGUID != null)
                                    {
                                        RequestSourceElement requestSourceElement = new RequestSourceElement();

                                        requestSourceElement.setRequestSourceElement(this.getOpenMetadataElement(requestSourceGUID, supplementaryEntities));

                                        instanceProperties = new InstanceProperties(relationship.getProperties());

                                        requestSourceElement.setRequestSourceName(this.removeRequestSourceName(instanceProperties));
                                        requestSourceElement.setOriginGovernanceService(this.removeOriginGovernanceService(instanceProperties));
                                        requestSourceElement.setOriginGovernanceEngine(this.removeOriginGovernanceEngine(instanceProperties));

                                        requestSourceElements.add(requestSourceElement);
                                    }
                                }
                                else if (repositoryHelper.isTypeOf(serviceName, actualTypeName, OpenMetadataType.NEXT_ENGINE_ACTION_TYPE_NAME))
                                {
                                    RelatedEngineActionElement relatedAction = new RelatedEngineActionElement();

                                    relatedAction.setGuard(this.removeGuard(relationship.getProperties()));
                                    relatedAction.setMandatoryGuard(this.removeMandatoryGuard(relationship.getProperties()));
                                    relatedAction.setRelatedActionLinkGUID(relationship.getGUID());

                                    if (primaryEntity.getGUID().equals(relationship.getEntityTwoProxy().getGUID()))
                                    {
                                        relatedAction.setRelatedAction(this.getElementStub(beanClass, relationship.getEntityOneProxy(), methodName));
                                        previousActions.add(relatedAction);
                                    }
                                    else
                                    {
                                        relatedAction.setRelatedAction(this.getElementStub(beanClass, relationship.getEntityTwoProxy(), methodName));
                                        followOnActions.add(relatedAction);
                                    }
                                }
                            }
                        }

                        if (! requestSourceElements.isEmpty())
                        {
                            bean.setRequestSourceElements(requestSourceElements);
                        }

                        if (! actionTargetElements.isEmpty())
                        {
                            bean.setActionTargetElements(actionTargetElements);
                        }

                        if (! previousActions.isEmpty())
                        {
                            bean.setPreviousActions(previousActions);
                        }

                        if (! followOnActions.isEmpty())
                        {
                            bean.setFollowOnActions(followOnActions);
                        }
                    }
                }
                else
                {
                    handleMissingMetadataInstance(beanClass.getName(), TypeDefCategory.ENTITY_DEF, methodName);
                }
            }
            else
            {
                handleUnexpectedBeanClass(beanClass.getName(), EngineActionElement.class.getName(), methodName);
            }

            return returnBean;
        }
        catch (IllegalAccessException | InstantiationException | ClassCastException | NoSuchMethodException | InvocationTargetException error)
        {
            super.handleInvalidBeanClass(beanClass.getName(), error, methodName);
        }

        return null;
    }
}
