/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;


import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ActorRoleElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.PersonRoleProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;


import java.lang.reflect.InvocationTargetException;

/**
 * PersonRoleConverter generates a PersonRoleProperties bean from an PersonRoleProperties entity and the relationships connected to it.
 */
public class PersonRoleConverter<B> extends OMFConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public PersonRoleConverter(OMRSRepositoryHelper repositoryHelper,
                               String               serviceName,
                               String               serverName)
    {
        super(repositoryHelper, serviceName, serverName);
    }


    /**
     * Using the supplied entity, return a new instance of the bean. This is used for most beans that have
     * a one to one correspondence with the repository instances.
     *
     * @param beanClass name of the class to create
     * @param entity entity containing the properties
     * @param methodName calling method
     * @return bean populated with properties from the entity supplied
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

            if (returnBean instanceof ActorRoleElement bean)
            {
                PersonRoleProperties roleProperties = new PersonRoleProperties();

                if (entity != null)
                {
                    bean.setElementHeader(this.getMetadataElementHeader(beanClass, entity, methodName));

                    /*
                     * The initial set of values come from the entity.
                     */
                    InstanceProperties instanceProperties = new InstanceProperties(entity.getProperties());

                    roleProperties.setQualifiedName(this.removeQualifiedName(instanceProperties));
                    roleProperties.setRoleId(this.removeIdentifier(instanceProperties));
                    roleProperties.setTitle(this.removeName(instanceProperties));
                    roleProperties.setDescription(this.removeDescription(instanceProperties));
                    roleProperties.setScope(this.removeScope(instanceProperties));
                    roleProperties.setDomainIdentifier(this.removeDomainIdentifier(instanceProperties));
                    roleProperties.setHeadCountLimitSet(instanceProperties.getPropertyValue(OpenMetadataProperty.HEAD_COUNT.name) != null);
                    roleProperties.setHeadCount(this.removeHeadCount(instanceProperties));

                    roleProperties.setAdditionalProperties(this.removeAdditionalProperties(instanceProperties));
                    roleProperties.setEffectiveFrom(instanceProperties.getEffectiveFromTime());
                    roleProperties.setEffectiveTo(instanceProperties.getEffectiveToTime());


                    /*
                     * Any remaining properties are returned in the extended properties.  They are
                     * assumed to be defined in a subtype.
                     */
                    roleProperties.setTypeName(bean.getElementHeader().getType().getTypeName());
                    roleProperties.setExtendedProperties(this.getRemainingExtendedProperties(instanceProperties));

                    bean.setProperties(roleProperties);
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
        B returnBean = this.getNewBean(beanClass, entity, methodName);

        if (returnBean instanceof ActorRoleElement bean)
        {

            bean.setRelatedElement(super.getRelatedElement(beanClass, entity, relationship, methodName));
        }

        return returnBean;
    }
}
