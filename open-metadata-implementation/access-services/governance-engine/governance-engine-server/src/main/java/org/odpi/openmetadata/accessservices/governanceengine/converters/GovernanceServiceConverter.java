/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceengine.converters;

import org.odpi.openmetadata.accessservices.governanceengine.metadataelements.GovernanceServiceElement;
import org.odpi.openmetadata.accessservices.governanceengine.properties.GovernanceServiceProperties;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.lang.reflect.InvocationTargetException;
import java.util.List;


/**
 * GovernanceServicePropertiesConverter transfers the relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into a GovernanceServiceProperties bean.
 */
public class GovernanceServiceConverter<B> extends GovernanceEngineOMASConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity/relationship objects
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public GovernanceServiceConverter(OMRSRepositoryHelper repositoryHelper,
                                      String               serviceName,
                                      String               serverName)
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

            if (returnBean instanceof GovernanceServiceElement)
            {
                GovernanceServiceElement bean = (GovernanceServiceElement)returnBean;
                GovernanceServiceProperties properties = new GovernanceServiceProperties();

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
                    properties.setDisplayName(this.removeName(instanceProperties));
                    properties.setDescription(this.removeDescription(instanceProperties));

                    if (supplementaryEntities != null)
                    {
                        for (EntityDetail entity : supplementaryEntities)
                        {
                            if ((entity != null) && (entity.getType() != null))
                            {
                                if (repositoryHelper.isTypeOf(serviceName, entity.getType().getTypeDefName(), OpenMetadataAPIMapper.CONNECTION_TYPE_NAME))
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
                handleUnexpectedBeanClass(beanClass.getName(), GovernanceServiceProperties.class.getName(), methodName);
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
