/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceengine.converters;

import org.odpi.openmetadata.accessservices.governanceengine.metadataelements.GovernanceActionElement;
import org.odpi.openmetadata.accessservices.governanceengine.properties.GovernanceActionProperties;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.governanceaction.properties.ActionTargetElement;
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
 * GovernanceActionConverter transfers the relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into a Governance Action bean.
 */
public class GovernanceActionConverter<B> extends GovernanceEngineOMASConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity/relationship objects
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public GovernanceActionConverter(OMRSRepositoryHelper repositoryHelper,
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
     * @param primaryEntity entity that is the root of the cluster of entities that make up the content of the bean
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

            if (returnBean instanceof GovernanceActionElement)
            {
                GovernanceActionElement    bean       = (GovernanceActionElement)returnBean;
                GovernanceActionProperties properties = new GovernanceActionProperties();

                if (primaryEntity != null)
                {
                    /*
                     * Check that the entity is of the correct type.
                     */
                    bean.setElementHeader(this.getMetadataElementHeader(beanClass, primaryEntity, methodName));

                    properties.setRequestedTime(primaryEntity.getCreateTime());

                    /*
                     * The initial set of values come from the entity properties.  The super class properties are removed from a copy of the entities
                     * properties, leaving any subclass properties to be stored in extended properties.
                     */
                    InstanceProperties instanceProperties = new InstanceProperties(primaryEntity.getProperties());

                    properties.setQualifiedName(this.removeQualifiedName(instanceProperties));
                    properties.setDomainIdentifier(this.removeDomainIdentifier(instanceProperties));
                    properties.setDisplayName(this.removeDisplayName(instanceProperties));
                    properties.setDescription(this.removeDescription(instanceProperties));
                    properties.setRequestType(this.removeRequestType(instanceProperties));
                    properties.setRequestParameters(this.removeRequestParameters(instanceProperties));
                    properties.setGovernanceEngineGUID(this.removeExecutorEngineGUID(instanceProperties));
                    properties.setGovernanceEngineName(this.removeExecutorEngineName(instanceProperties));
                    properties.setProcessName(this.removeProcessName(instanceProperties));
                    properties.setGovernanceActionTypeGUID(this.removeGovernanceActionTypeGUID(instanceProperties));
                    properties.setGovernanceActionTypeName(this.removeGovernanceActionTypeName(instanceProperties));
                    properties.setMandatoryGuards(this.removeMandatoryGuards(instanceProperties));
                    properties.setReceivedGuards(this.removeReceivedGuards(instanceProperties));
                    properties.setActionStatus(this.removeActionStatus(OpenMetadataAPIMapper.ACTION_STATUS_PROPERTY_NAME, instanceProperties));
                    properties.setStartTime(this.removeStartDate(instanceProperties));
                    properties.setProcessingEngineUserId(this.removeProcessingEngineUserId(instanceProperties));
                    properties.setCompletionTime(this.removeCompletionDate(instanceProperties));
                    properties.setCompletionGuards(this.removeCompletionGuards(instanceProperties));
                    properties.setAdditionalProperties(this.removeAdditionalProperties(instanceProperties));

                    if (relationships != null)
                    {
                        List<RequestSourceElement> requestSourceElements = new ArrayList<>();
                        List<ActionTargetElement>  actionTargetElements  = new ArrayList<>();

                        for (Relationship relationship : relationships)
                        {
                            if ((relationship != null) && (relationship.getType() != null))
                            {
                                String actualTypeName = relationship.getType().getTypeDefName();
                                instanceProperties = new InstanceProperties(relationship.getProperties());

                                if (repositoryHelper.isTypeOf(serviceName, actualTypeName, OpenMetadataAPIMapper.GOVERNANCE_ACTION_EXECUTOR_TYPE_NAME))
                                {
                                    if (properties.getRequestType() == null)
                                    {
                                        properties.setRequestType(this.removeRequestType(instanceProperties));
                                        properties.setRequestParameters(this.removeRequestParameters(instanceProperties));
                                    }

                                    if (properties.getGovernanceEngineGUID() == null)
                                    {
                                        EntityProxy entityProxy = relationship.getEntityTwoProxy();

                                        properties.setGovernanceEngineGUID(entityProxy.getGUID());

                                        if (entityProxy.getUniqueProperties() != null)
                                        {
                                            properties.setGovernanceEngineName(this.getQualifiedName(entityProxy.getUniqueProperties()));
                                        }
                                    }
                                }
                                else if (repositoryHelper.isTypeOf(serviceName, actualTypeName, OpenMetadataAPIMapper.TARGET_FOR_ACTION_TYPE_NAME))
                                {
                                    ActionTargetElement actionTargetElement = new ActionTargetElement();

                                    actionTargetElement.setActionTargetName(this.removeActionTargetName(instanceProperties));
                                    actionTargetElement.setStatus(this.removeActionStatus(OpenMetadataAPIMapper.STATUS_PROPERTY_NAME, instanceProperties));
                                    actionTargetElement.setStartDate(this.removeStartDate(instanceProperties));
                                    actionTargetElement.setCompletionDate(this.removeCompletionDate(instanceProperties));

                                    String actionTargetGUID = relationship.getEntityTwoProxy().getGUID();

                                    if (actionTargetGUID != null)
                                    {
                                        actionTargetElement.setActionTargetGUID(actionTargetGUID);
                                        actionTargetElement.setTargetElement(this.getOpenMetadataElement(actionTargetGUID, supplementaryEntities));
                                    }

                                    actionTargetElements.add(actionTargetElement);
                                }
                                else if (repositoryHelper.isTypeOf(serviceName, actualTypeName, OpenMetadataAPIMapper.GOVERNANCE_ACTION_REQUEST_SOURCE_TYPE_NAME))
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
                            }
                        }

                        if (! requestSourceElements.isEmpty())
                        {
                            properties.setRequestSourceElements(requestSourceElements);
                        }

                        if (! actionTargetElements.isEmpty())
                        {
                            properties.setActionTargetElements(actionTargetElements);
                        }
                    }

                    bean.setProperties(properties);
                }
                else
                {
                    handleMissingMetadataInstance(beanClass.getName(), TypeDefCategory.ENTITY_DEF, methodName);
                }
            }
            else
            {
                handleUnexpectedBeanClass(beanClass.getName(), GovernanceActionProperties.class.getName(), methodName);
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
