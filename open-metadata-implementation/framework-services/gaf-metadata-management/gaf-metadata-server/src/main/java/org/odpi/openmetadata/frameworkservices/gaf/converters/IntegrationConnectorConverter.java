/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.gaf.converters;

import org.odpi.openmetadata.commonservices.generichandlers.OMFConverter;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.governanceaction.properties.IntegrationConnectorElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.IntegrationConnectorProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.lang.reflect.InvocationTargetException;
import java.util.List;


/**
 * IntegrationConnectorPropertiesConverter transfers the relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into a IntegrationConnectorProperties bean.
 */
public class IntegrationConnectorConverter<B> extends OMFConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity/relationship objects
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public IntegrationConnectorConverter(OMRSRepositoryHelper repositoryHelper,
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
     * @param primaryEntity entity that is the root of the collection of entities that make up the
     *                      content of the bean
     * @param supplementaryEntities entities connected to the primary entity by the relationships
     * @param relationships relationships linking the entities
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    @Override
    public B getNewComplexGraphBean(Class<B>           beanClass,
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

            if (returnBean instanceof IntegrationConnectorElement bean)
            {
                IntegrationConnectorProperties properties = new IntegrationConnectorProperties();

                if (primaryEntity != null)
                {
                    /*
                     * Check that the entity is of the correct type.
                     */
                    bean.setElementHeader(this.getMetadataElementHeader(beanClass, primaryEntity, methodName));

                    /*
                     * The initial set of values come from the entity properties.  The super class properties are removed from a copy of the entities
                     * properties, leaving any subclass properties to be stored in extended properties.
                     */
                    InstanceProperties instanceProperties = new InstanceProperties(primaryEntity.getProperties());

                    properties.setQualifiedName(this.removeQualifiedName(instanceProperties));
                    properties.setAdditionalProperties(this.removeAdditionalProperties(instanceProperties));
                    properties.setName(this.removeName(instanceProperties));
                    properties.setVersionIdentifier(this.removeVersionIdentifier(instanceProperties));
                    properties.setDescription(this.removeDescription(instanceProperties));
                    properties.setUsesBlockingCalls(this.removeUsesBlockingCalls(instanceProperties));

                    if (supplementaryEntities != null)
                    {
                        for (EntityDetail entity : supplementaryEntities)
                        {
                            if ((entity != null) && (entity.getType() != null))
                            {
                                if (repositoryHelper.isTypeOf(serviceName, entity.getType().getTypeDefName(), OpenMetadataType.CONNECTION.typeName))
                                {
                                    properties.setConnection(super.getEmbeddedConnection(beanClass,
                                                                                         entity,
                                                                                         supplementaryEntities,
                                                                                         relationships,
                                                                                         methodName));
                                }
                            }
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
                handleUnexpectedBeanClass(beanClass.getName(), IntegrationConnectorProperties.class.getName(), methodName);
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
