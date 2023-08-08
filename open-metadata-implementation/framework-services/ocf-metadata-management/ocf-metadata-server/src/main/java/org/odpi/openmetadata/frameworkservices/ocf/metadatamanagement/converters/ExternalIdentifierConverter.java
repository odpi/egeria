/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.converters;

import org.odpi.openmetadata.commonservices.generichandlers.OCFConverter;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ExternalIdentifier;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.KeyPattern;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.lang.reflect.InvocationTargetException;


/**
 * ExternalIdentifierConverter transfers the relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into a Referenceable bean.
 */
public class ExternalIdentifierConverter<B> extends OCFConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public ExternalIdentifierConverter(OMRSRepositoryHelper repositoryHelper,
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

            if (returnBean instanceof ExternalIdentifier bean)
            {
                /*
                 * Check that the entity is of the correct type.
                 */
                this.setUpElementHeader(bean, entity, OpenMetadataAPIMapper.EXTERNAL_IDENTIFIER_TYPE_NAME, methodName);

                /*
                 * The initial set of values come from the entity properties.  The super class properties are removed from a copy of the entities
                 * properties, leaving any subclass properties to be stored in extended properties.
                 */
                InstanceProperties instanceProperties = new InstanceProperties(entity.getProperties());

                bean.setQualifiedName(this.removeQualifiedName(instanceProperties));
                bean.setIdentifier((this.removeIdentifier(instanceProperties)));
                bean.setKeyPattern(this.removeKeyPattern(instanceProperties));
                bean.setExternalInstanceCreatedBy(this.removeExternalInstanceCreatedBy(instanceProperties));
                bean.setExternalInstanceCreationTime(this.removeExternalInstanceCreationTime(instanceProperties));
                bean.setExternalInstanceLastUpdatedBy(this.removeExternalInstanceLastUpdatedBy(instanceProperties));
                bean.setExternalInstanceLastUpdateTime(this.removeExternalInstanceLastUpdateTime(instanceProperties));
                bean.setExternalInstanceVersion(this.removeExternalInstanceVersion(instanceProperties));
                bean.setAdditionalProperties(this.removeAdditionalProperties(instanceProperties));

                /*
                 * Any remaining properties are returned in the extended properties.  They are
                 * assumed to be defined in a subtype.
                 */
                bean.setExtendedProperties(this.getRemainingExtendedProperties(instanceProperties));
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
    @SuppressWarnings(value = "unused")
    @Override
    public B getNewBean(Class<B>     beanClass,
                        EntityDetail entity,
                        Relationship relationship,
                        String       methodName) throws PropertyServerException
    {
        return this.getNewBean(beanClass, entity, methodName);
    }


    /**
     * Extract and delete the keyPattern property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return KeyPattern enum
     */
    KeyPattern removeKeyPattern(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeKeyPattern";

        if (instanceProperties != null)
        {
            int ordinal = repositoryHelper.removeEnumPropertyOrdinal(serviceName,
                                                                     OpenMetadataAPIMapper.KEY_PATTERN_PROPERTY_NAME,
                                                                     instanceProperties,
                                                                     methodName);

            for (KeyPattern keyPattern : KeyPattern.values())
            {
                if (keyPattern.getOpenTypeOrdinal() == ordinal)
                {
                    return keyPattern;
                }
            }
        }

        return KeyPattern.LOCAL_KEY;
    }
}
