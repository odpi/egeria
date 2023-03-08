/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.discoveryengine.converters;

import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.discovery.properties.DiscoveryAnalysisReport;
import org.odpi.openmetadata.frameworks.discovery.properties.DiscoveryRequestStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * DiscoveryAnalysisReportConverter transfers the relevant properties from some Open Metadata Repository Services (OMRS)
 * EntityDetail object into an DiscoveryAnalysisReport bean.
 */
public class DiscoveryAnalysisReportConverter<B> extends DiscoveryEngineOMASConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity/relationship objects
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public DiscoveryAnalysisReportConverter(OMRSRepositoryHelper     repositoryHelper,
                                            String                   serviceName,
                                            String                   serverName)
    {
        super(repositoryHelper, serviceName, serverName);
    }


    /**
     * Using the supplied instances, return a new instance of the bean.  It is used for beans such as
     * a connection bean which made up of 3 entities (Connection, ConnectorType and Endpoint) plus the
     * relationships between them.  The relationships may be omitted if they do not have an properties.
     *
     * @param beanClass name of the class to create
     * @param primaryEntity entity that is the root of the cluster of entities that make up the
     *                      content of the bean
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

            if (returnBean instanceof DiscoveryAnalysisReport)
            {
                DiscoveryAnalysisReport bean = (DiscoveryAnalysisReport) returnBean;

                /*
                 * Check that the entity is of the correct type.
                 */
                bean.setElementHeader(this.getMetadataElementHeader(beanClass, primaryEntity, methodName));

                /*
                 * The initial set of values come from the entity properties.  The super class properties are removed from a copy of the entities
                 * properties, leaving any subclass properties to be stored in extended properties.
                 */
                InstanceProperties instanceProperties = new InstanceProperties(primaryEntity.getProperties());

                bean.setQualifiedName(this.removeQualifiedName(instanceProperties));
                bean.setAdditionalProperties(this.removeAdditionalProperties(instanceProperties));

                bean.setDisplayName(this.removeDisplayName(instanceProperties));
                bean.setDescription(this.removeDescription(instanceProperties));
                bean.setCreationDate(this.removeExecutionDate(instanceProperties));
                bean.setAnalysisParameters(this.removeAnalysisParameters(instanceProperties));
                bean.setAnalysisStep(this.removeAnalysisStep(instanceProperties));
                bean.setDiscoveryRequestStatus(this.removeRequestStatusFromProperties(instanceProperties));

                /*
                 * Any remaining properties are returned in the extended properties.  They are
                 * assumed to be defined in a subtype.
                 */
                bean.setExtendedProperties(this.getRemainingExtendedProperties(instanceProperties));

                if (relationships != null)
                {
                    for (Relationship relationship : relationships)
                    {
                        if ((relationship != null) && (relationship.getType() != null) && (relationship.getType().getTypeDefName() != null))
                        {
                            if (repositoryHelper.isTypeOf(serviceName, relationship.getType().getTypeDefName(), OpenMetadataAPIMapper.REPORT_TO_ASSET_TYPE_NAME))
                            {
                                EntityProxy endOne = relationship.getEntityOneProxy();

                                if (endOne != null)
                                {
                                    bean.setAssetGUID(endOne.getGUID());
                                }
                            }
                            else if (repositoryHelper.isTypeOf(serviceName, relationship.getType().getTypeDefName(), OpenMetadataAPIMapper.REPORT_TO_ENGINE_TYPE_NAME))
                            {
                                EntityProxy endOne = relationship.getEntityOneProxy();

                                if (endOne != null)
                                {
                                    bean.setDiscoveryEngineGUID(endOne.getGUID());
                                }
                            }
                            else if (repositoryHelper.isTypeOf(serviceName, relationship.getType().getTypeDefName(), OpenMetadataAPIMapper.REPORT_TO_SERVICE_TYPE_NAME))
                            {
                                EntityProxy endOne = relationship.getEntityOneProxy();

                                if (endOne != null)
                                {
                                    bean.setDiscoveryServiceGUID(endOne.getGUID());
                                }
                            }
                        }
                    }
                }
            }

            return returnBean;
        }
        catch (IllegalAccessException | InstantiationException | ClassCastException | NoSuchMethodException | InvocationTargetException error)
        {
            super.handleInvalidBeanClass(beanClass.getName(), error, methodName);
        }

        return null;
    }


    /**
     * Retrieve the Discovery request status enum property from the instance properties of an entity
     *
     * @param properties  entity properties
     * @return   enum value
     */
    private DiscoveryRequestStatus removeRequestStatusFromProperties(InstanceProperties   properties)
    {
        DiscoveryRequestStatus requestStatus = DiscoveryRequestStatus.UNKNOWN_STATUS;

        if (properties != null)
        {
            Map<String, InstancePropertyValue> instancePropertiesMap = properties.getInstanceProperties();

            InstancePropertyValue instancePropertyValue = instancePropertiesMap.get(OpenMetadataAPIMapper.DISCOVERY_SERVICE_STATUS_PROPERTY_NAME);

            if (instancePropertyValue instanceof EnumPropertyValue)
            {
                EnumPropertyValue enumPropertyValue = (EnumPropertyValue) instancePropertyValue;

                switch (enumPropertyValue.getOrdinal())
                {
                    case 0:
                        requestStatus = DiscoveryRequestStatus.WAITING;
                        break;

                    case 1:
                        requestStatus = DiscoveryRequestStatus.ACTIVATING;
                        break;

                    case 2:
                        requestStatus = DiscoveryRequestStatus.IN_PROGRESS;
                        break;

                    case 3:
                        requestStatus = DiscoveryRequestStatus.FAILED;
                        break;

                    case 4:
                        requestStatus = DiscoveryRequestStatus.COMPLETED;
                        break;

                    case 5:
                        requestStatus = DiscoveryRequestStatus.OTHER;
                        break;

                    default:
                        requestStatus = DiscoveryRequestStatus.UNKNOWN_STATUS;
                        break;
                }

                instancePropertiesMap.remove(OpenMetadataAPIMapper.DISCOVERY_SERVICE_STATUS_PROPERTY_NAME);

                properties.setInstanceProperties(instancePropertiesMap);
            }
        }

        return requestStatus;
    }
}
