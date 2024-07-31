/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.oif.converters;

import org.odpi.openmetadata.commonservices.generichandlers.OMFConverter;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.governanceaction.properties.IntegrationReport;
import org.odpi.openmetadata.frameworks.governanceaction.properties.IntegrationReportProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.lang.reflect.InvocationTargetException;
import java.util.List;


/**
 * AssetConverter provides common methods for transferring relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into an DataAssetElement bean.
 */
public class IntegrationReportConverter<B> extends OMFConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public IntegrationReportConverter(OMRSRepositoryHelper repositoryHelper,
                                      String               serviceName,
                                      String               serverName)
    {
        super(repositoryHelper, serviceName, serverName);
    }


    /**
     * Using the supplied instances, return a new instance of the bean. This is used for beans that
     * contain a combination of the properties from an entity and that of a connected relationship.
     *
     * @param beanClass name of the class to create
     * @param entity entity containing the properties
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    @Override
    public B getNewBean(Class<B>     beanClass,
                        EntityDetail entity,
                        String       methodName) throws PropertyServerException
    {
        try
        {
            /*
             * This is initial confirmation that the generic converter has been initialized with an appropriate bean class.
             */
            B returnBean = beanClass.getDeclaredConstructor().newInstance();

            if (returnBean instanceof IntegrationReport bean)
            {
                IntegrationReportProperties reportProperties = new IntegrationReportProperties();

                if (entity != null)
                {
                    bean.setElementHeader(super.getMetadataElementHeader(beanClass, entity, methodName));

                    /*
                     * The initial set of values come from the entity.
                     */
                    InstanceProperties instanceProperties = new InstanceProperties(entity.getProperties());

                    reportProperties.setConnectorId(repositoryHelper.getStringProperty(serviceName,
                                                                                       OpenMetadataType.CONNECTOR_ID_PROPERTY_NAME,
                                                                                       instanceProperties,
                                                                                       methodName));
                    reportProperties.setConnectorName(repositoryHelper.getStringProperty(serviceName,
                                                                                         OpenMetadataType.CONNECTOR_NAME_PROPERTY_NAME,
                                                                                         instanceProperties,
                                                                                         methodName));
                    reportProperties.setServerName(repositoryHelper.getStringProperty(serviceName,
                                                                                      OpenMetadataType.SERVER_NAME_PROPERTY_NAME,
                                                                                      instanceProperties,
                                                                                      methodName));
                    reportProperties.setRefreshStartDate(repositoryHelper.getDateProperty(serviceName,
                                                                                          OpenMetadataType.REFRESH_START_DATE_PROPERTY_NAME,
                                                                                          instanceProperties,
                                                                                          methodName));
                    reportProperties.setRefreshCompletionDate(repositoryHelper.getDateProperty(serviceName,
                                                                                               OpenMetadataType.REFRESH_COMPLETION_DATE_PROPERTY_NAME,
                                                                                               instanceProperties,
                                                                                               methodName));
                    reportProperties.setCreatedElements(repositoryHelper.getStringArrayProperty(serviceName,
                                                                                                OpenMetadataType.CREATED_ELEMENTS_PROPERTY_NAME,
                                                                                                instanceProperties,
                                                                                                methodName));
                    reportProperties.setUpdatedElements(repositoryHelper.getStringArrayProperty(serviceName,
                                                                                                OpenMetadataType.UPDATED_ELEMENTS_PROPERTY_NAME,
                                                                                                instanceProperties,
                                                                                                methodName));
                    reportProperties.setDeletedElements(repositoryHelper.getStringArrayProperty(serviceName,
                                                                                                OpenMetadataType.DELETED_ELEMENTS_PROPERTY_NAME,
                                                                                                instanceProperties,
                                                                                                methodName));
                    reportProperties.setAdditionalProperties(repositoryHelper.getStringMapFromProperty(serviceName,
                                                                                                       OpenMetadataProperty.ADDITIONAL_PROPERTIES.name,
                                                                                                       instanceProperties,
                                                                                                       methodName));

                    bean.setProperties(reportProperties);
                }
                else
                {
                    handleMissingMetadataInstance(beanClass.getName(), TypeDefCategory.ENTITY_DEF, methodName);
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
     * Using the supplied instances, return a new instance of the bean. This is used for beans that
     * contain a combination of the properties from an entity and that of a connected relationship.
     *
     * @param beanClass name of the class to create
     * @param entity entity containing the properties
     * @param relationship relationship containing the properties
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    @Override
    public B getNewBean(Class<B>     beanClass,
                        EntityDetail entity,
                        Relationship relationship,
                        String       methodName) throws PropertyServerException
    {
        return getNewBean(beanClass, entity, methodName);
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
        // todo process supplement properties
        return getNewBean(beanClass, primaryEntity, methodName);
    }
}
