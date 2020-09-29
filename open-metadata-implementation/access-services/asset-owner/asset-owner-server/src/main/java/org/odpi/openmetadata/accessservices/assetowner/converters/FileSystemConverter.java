/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.converters;

import org.odpi.openmetadata.accessservices.assetowner.metadataelements.FileSystemElement;
import org.odpi.openmetadata.accessservices.assetowner.metadataelements.MetadataElement;
import org.odpi.openmetadata.accessservices.assetowner.properties.FileSystemProperties;
import org.odpi.openmetadata.accessservices.assetowner.properties.ReferenceableProperties;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

/**
 * FileSystemConverter transfers the relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into a FileSystemElement bean.
 */
public class FileSystemConverter<B> extends AssetOwnerOMASConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public FileSystemConverter(OMRSRepositoryHelper repositoryHelper,
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

            if ((returnBean instanceof FileSystemElement) && (entity != null))
            {
                FileSystemElement bean = (FileSystemElement) returnBean;

                /*
                 * The initial set of values come from the entity.
                 */
                InstanceProperties instanceProperties = new InstanceProperties(entity.getProperties());

                bean.setQualifiedName(this.removeQualifiedName(instanceProperties));
                bean.setAdditionalProperties(this.removeAdditionalProperties(instanceProperties));
                bean.setDisplayName(this.removeName(instanceProperties));
                bean.setDescription(this.removeDescription(instanceProperties));
                bean.setTypeDescription(this.removeCapabilityType(instanceProperties));
                bean.setVersion(this.removeVersion(instanceProperties));
                bean.setPatchLevel(this.removePatchLevel(instanceProperties));
                bean.setSource(this.removeSource(instanceProperties));

                /*
                 * Any remaining properties are returned in the extended properties.  They are
                 * assumed to be defined in a subtype.
                 */
                bean.setTypeName(typeName);
                bean.setExtendedProperties(this.getRemainingExtendedProperties(instanceProperties));

                instanceProperties = super.getClassificationProperties(OpenMetadataAPIMapper.FILE_SYSTEM_CLASSIFICATION_TYPE_NAME, entity);

                bean.setFormat(this.getFormat(instanceProperties));
                bean.setEncryption(this.getEncryption(instanceProperties));
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
