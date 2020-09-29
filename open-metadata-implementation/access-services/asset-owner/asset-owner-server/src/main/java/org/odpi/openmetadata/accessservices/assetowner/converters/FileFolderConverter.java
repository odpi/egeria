/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.converters;

import org.odpi.openmetadata.accessservices.assetowner.metadataelements.FolderElement;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

/**
 * FileFolderConverter transfers the relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into a FileFolderElement bean.
 */
public class FileFolderConverter<B> extends AssetConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity/relationship objects
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public FileFolderConverter(OMRSRepositoryHelper repositoryHelper,
                               String               serviceName,
                               String               serverName)
    {
        super(repositoryHelper, serviceName, serverName);
    }


    /**
     * Using the supplied instances, return a new instance of the bean. This is used for beans that have
     * contain a combination of the properties from an entity and a that os a connected relationship.
     *
     * @param beanClass name of the class to create
     * @param entity entity containing the properties
     * @param relationship relationship containing the properties
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
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
            B returnBean = beanClass.newInstance();

            if (returnBean instanceof FolderElement)
            {
                FolderElement bean = (FolderElement) returnBean;

                bean.setElementHeader(super.getMetadataElementHeader(beanClass, entity, methodName));

                InstanceProperties instanceProperties = null;

                /*
                 * The initial set of values come from the entity.
                 */
                if (entity != null)
                {
                    instanceProperties = new InstanceProperties(entity.getProperties());
                }

                bean.setQualifiedName(this.removeQualifiedName(instanceProperties));
                bean.setAdditionalProperties(this.removeAdditionalProperties(instanceProperties));
                bean.setDisplayName(this.removeDisplayName(instanceProperties));
                bean.setDescription(this.removeDescription(instanceProperties));

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
                bean.setTypeName(typeName);
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

                bean.setOriginOrganizationGUID(this.getOriginOrganizationGUID(instanceProperties));
                bean.setOriginBusinessCapabilityGUID(this.getOriginBusinessCapabilityGUID(instanceProperties));
                bean.setOtherOriginValues(this.getOtherOriginValues(instanceProperties));

                instanceProperties = super.getClassificationProperties(OpenMetadataAPIMapper.DATA_STORE_ENCODING_CLASSIFICATION_NAME, entity);

                bean.setEncodingType(this.getDataStoreEncodingType(instanceProperties));
                bean.setEncodingLanguage(this.getDataStoreEncodingLanguage(instanceProperties));
                bean.setEncodingDescription(this.getDataStoreEncodingDescription(instanceProperties));
                bean.setEncodingProperties(this.getEncodingProperties(instanceProperties));

                /*
                 * Any remaining properties are returned in the extended properties.  They are
                 * assumed to be defined in a subtype.
                 */
                bean.setTypeName(typeName);
                bean.setExtendedProperties(this.getRemainingExtendedProperties(instanceProperties));
            }

            return returnBean;
        }
        catch (IllegalAccessException | InstantiationException | ClassCastException error)
        {
            super.handleInvalidBeanClass(beanClass.getName(), error, methodName);
        }

        return null;
    }
}
