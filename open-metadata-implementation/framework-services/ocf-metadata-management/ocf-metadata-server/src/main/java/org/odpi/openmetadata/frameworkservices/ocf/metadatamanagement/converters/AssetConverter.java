/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.converters;

import org.odpi.openmetadata.commonservices.generichandlers.OCFConverter;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Asset;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * AssetConverter transfers the relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into an Asset bean.
 */
public class AssetConverter<B> extends OCFConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public AssetConverter(OMRSRepositoryHelper repositoryHelper,
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
    @SuppressWarnings(value="deprecation")
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

            if (returnBean instanceof Asset)
            {
                Asset bean = (Asset) returnBean;

                /*
                 * Check that the entity is of the correct type.
                 */
                this.setUpElementHeader(bean, entity, OpenMetadataAPIMapper.ASSET_TYPE_NAME, methodName);

                /*
                 * The initial set of values come from the entity properties.  The super class properties are removed from a copy of the entities
                 * properties, leaving any subclass properties to be stored in extended properties.
                 */
                InstanceProperties instanceProperties = new InstanceProperties(entity.getProperties());

                bean.setQualifiedName(this.removeQualifiedName(instanceProperties));
                bean.setAdditionalProperties(this.removeAdditionalProperties(instanceProperties));
                bean.setResourceName(this.removeName(instanceProperties));
                bean.setName(bean.getResourceName());
                bean.setVersionIdentifier(this.removeVersionIdentifier(instanceProperties));
                bean.setResourceDescription(this.removeDescription(instanceProperties));
                bean.setDescription(bean.getResourceDescription());

                /* Note this value should be in the classification */
                bean.setOwner(this.removeOwner(instanceProperties));
                /* Note this value should be in the classification */
                bean.setOwnerType(this.removeOwnerTypeFromProperties(instanceProperties));
                /* Note this value should be in the classification */
                bean.setZoneMembership(this.removeZoneMembership(instanceProperties));

                /*
                 * Any remaining properties are returned in the extended properties.  They are
                 * assumed to be defined in a subtype.
                 */
                bean.setExtendedProperties(this.getRemainingExtendedProperties(instanceProperties));

                /*
                 * The values in the classifications override the values in the main properties of the Asset's entity.
                 * Having these properties in the main entity is deprecated.
                 */
                instanceProperties = super.getClassificationProperties(OpenMetadataAPIMapper.ASSET_ZONES_CLASSIFICATION_NAME, entity);

                bean.setZoneMembership(this.getZoneMembership(instanceProperties));

                instanceProperties = super.getClassificationProperties(OpenMetadataAPIMapper.ASSET_OWNERSHIP_CLASSIFICATION_NAME, entity);

                bean.setOwner(this.getOwner(instanceProperties));
                bean.setOwnerType(this.getOwnerTypeFromProperties(instanceProperties));

                instanceProperties = super.getClassificationProperties(OpenMetadataAPIMapper.ASSET_ORIGIN_CLASSIFICATION_NAME, entity);

                Map<String, String> originMap = this.getOtherOriginValues(instanceProperties);

                String orgOriginValue = this.getOriginOrganizationGUID(instanceProperties);
                String bizOriginValue = this.getOriginBusinessCapabilityGUID(instanceProperties);

                if ((orgOriginValue != null) || (bizOriginValue != null))
                {
                    if (originMap == null)
                    {
                        originMap = new HashMap<>();
                    }

                    if (orgOriginValue != null)
                    {
                        originMap.put(OpenMetadataAPIMapper.ORGANIZATION_PROPERTY_NAME, orgOriginValue);
                    }

                    if (bizOriginValue != null)
                    {
                        originMap.put(OpenMetadataAPIMapper.BUSINESS_CAPABILITY_PROPERTY_NAME, bizOriginValue);
                    }
                }

                bean.setAssetOrigin(originMap);

                // todo set up SecurityTags and the governance classifications
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
        return this.getNewBean(beanClass, entity, methodName);
    }
}
