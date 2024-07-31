/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ValidValueElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.validvalues.ValidValueProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.lang.reflect.InvocationTargetException;


/**
 * ValidValueConverter provides common methods for transferring relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into a bean that inherits from ValidValueElement.
 */
public class ValidValueConverter<B> extends OMFConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public ValidValueConverter(OMRSRepositoryHelper repositoryHelper,
                               String serviceName,
                               String serverName)
    {
        super(repositoryHelper, serviceName, serverName);
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
        try
        {
            /*
             * This is initial confirmation that the generic converter has been initialized with an appropriate bean class.
             */
            B returnBean = beanClass.getDeclaredConstructor().newInstance();

            if (returnBean instanceof ValidValueElement bean)
            {
                this.updateSimpleMetadataElement(beanClass, bean, entity, methodName);

                if (relationship != null)
                {
                    if (repositoryHelper.isTypeOf(serviceName, relationship.getType().getTypeDefName(), OpenMetadataType.VALID_VALUE_MEMBER_RELATIONSHIP.typeName))
                    {
                        bean.setSetGUID(relationship.getEntityOneProxy().getGUID());
                        bean.setIsDefaultValue(this.removeIsDefaultValue(relationship.getProperties()));
                    }

                    bean.setRelatedElement(super.getRelatedElement(beanClass, entity, relationship, methodName));
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

            if (returnBean instanceof ValidValueElement)
            {
                this.updateSimpleMetadataElement(beanClass, (ValidValueElement) returnBean, entity, methodName);
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
     * Extract the properties from the entity.  Each concrete DataManager OMAS converter implements this method.
     * The top level fills in the header
     *
     * @param beanClass name of the class to create
     * @param bean output bean
     * @param entity entity containing the properties
     * @param methodName calling method
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    void updateSimpleMetadataElement(Class<B>          beanClass,
                                     ValidValueElement bean,
                                     EntityDetail      entity,
                                     String            methodName)  throws PropertyServerException
    {
        if (entity != null)
        {
            bean.setElementHeader(this.getMetadataElementHeader(beanClass, entity, methodName));
            ValidValueProperties validValueProperties = new ValidValueProperties();

            InstanceProperties instanceProperties = new InstanceProperties(entity.getProperties());

            validValueProperties.setQualifiedName(this.removeQualifiedName(instanceProperties));
            validValueProperties.setAdditionalProperties(this.removeAdditionalProperties(instanceProperties));
            validValueProperties.setDisplayName(this.removeName(instanceProperties));
            validValueProperties.setDescription(this.removeDescription(instanceProperties));
            validValueProperties.setCategory(this.removeCategory(instanceProperties));
            validValueProperties.setUsage(this.removeUsage(instanceProperties));
            validValueProperties.setScope(this.removeScope(instanceProperties));
            validValueProperties.setPreferredValue(this.removePreferredValue(instanceProperties));
            validValueProperties.setDataType(this.removeDataType(instanceProperties));
            validValueProperties.setIsDeprecated(this.removeIsDeprecated(instanceProperties));
            validValueProperties.setIsCaseSensitive(this.removeIsCaseSensitive(instanceProperties));

            /*
             * Any remaining properties are returned in the extended properties.  They are
             * assumed to be defined in a subtype.
             */
            validValueProperties.setTypeName(bean.getElementHeader().getType().getTypeName());
            validValueProperties.setExtendedProperties(this.getRemainingExtendedProperties(instanceProperties));

            bean.setValidValueProperties(validValueProperties);
        }
        else
        {
            handleMissingMetadataInstance(beanClass.getName(), TypeDefCategory.ENTITY_DEF, methodName);
        }
    }
}
