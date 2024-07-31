/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.commonservices.generichandlers.OMFConverter;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.APIOperationElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.apis.APIOperationProperties;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.lang.reflect.InvocationTargetException;


/**
 * EventTypeConverter provides common methods for transferring relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into a bean that inherits from EventTypeElement.  It is working with a ComplexSchemaType.
 */
public class APIOperationConverter<B> extends OMFConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public APIOperationConverter(OMRSRepositoryHelper repositoryHelper,
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

            if (returnBean instanceof APIOperationElement bean)
            {
                if ((entity != null) && (entity.getProperties() != null))
                {
                    /*
                     * The schema type has many different subtypes.
                     * This next piece of logic sorts out which type of schema bean to create.
                     */
                    bean.setElementHeader(this.getMetadataElementHeader(beanClass, entity, entity.getClassifications(), methodName));

                    APIOperationProperties apiOperationProperties = new APIOperationProperties();

                    InstanceProperties propertiesCopy = new InstanceProperties(entity.getProperties());

                    apiOperationProperties.setQualifiedName(this.removeQualifiedName(propertiesCopy));
                    apiOperationProperties.setDisplayName(this.removeDisplayName(propertiesCopy));
                    apiOperationProperties.setDescription(this.removeDescription(propertiesCopy));
                    apiOperationProperties.setIsDeprecated(this.removeIsDeprecated(propertiesCopy));
                    apiOperationProperties.setVersionNumber(this.removeVersionNumber(propertiesCopy));
                    apiOperationProperties.setAuthor(this.removeAuthor(propertiesCopy));
                    apiOperationProperties.setUsage(this.removeUsage(propertiesCopy));
                    apiOperationProperties.setEncodingStandard(this.removeEncodingStandard(propertiesCopy));
                    apiOperationProperties.setNamespace(this.removeNamespace(propertiesCopy));
                    apiOperationProperties.setAdditionalProperties(this.removeAdditionalProperties(propertiesCopy));

                    /*
                     * Any remaining properties are returned in the extended properties.  They are
                     * assumed to be defined in a subtype.
                     */
                    apiOperationProperties.setExtendedProperties(this.getRemainingExtendedProperties(propertiesCopy));

                    bean.setProperties(apiOperationProperties);

                    return returnBean;
                }
                else
                {
                    handleMissingMetadataInstance(beanClass.getName(), TypeDefCategory.ENTITY_DEF, methodName);
                }
            }
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
    @SuppressWarnings(value = "unused")
    @Override
    public B getNewBean(Class<B>     beanClass,
                        EntityDetail entity,
                        Relationship relationship,
                        String       methodName) throws PropertyServerException
    {
        return this.getNewBean(beanClass, entity, methodName);
    }
}
