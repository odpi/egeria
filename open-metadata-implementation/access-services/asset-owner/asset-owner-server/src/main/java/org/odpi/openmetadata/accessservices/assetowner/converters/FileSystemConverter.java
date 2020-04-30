/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.converters;

import org.odpi.openmetadata.accessservices.assetowner.mappers.FileSystemMapper;
import org.odpi.openmetadata.accessservices.assetowner.properties.FileSystem;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.converters.ReferenceableConverter;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.ReferenceableMapper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;


/**
 * FileSystemConverter transfers the relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into a FileSystem bean.
 */
public class FileSystemConverter extends ReferenceableConverter
{
    /**
     * Constructor captures the initial content with connectionToAssetRelationship
     *
     * @param zoneEntity properties to convert
     * @param repositoryHelper helper object to parse entity/relationship objects
     * @param serviceName name of this component
     */
    public FileSystemConverter(EntityDetail         zoneEntity,
                               OMRSRepositoryHelper repositoryHelper,
                               String               serviceName)
    {
        super(zoneEntity, repositoryHelper, serviceName);
    }


    /**
     * Request the bean is extracted from the repository objects
     *
     * @return output bean
     */
    public FileSystem getFileSystemBean()
    {
        FileSystem  bean = null;

        if (entity != null)
        {
            bean = new FileSystem();

            updateBean(bean);
        }

        return bean;
    }


    /**
     * Request the bean is extracted from the repository objects
     *
     * @param bean output bean
     */
    private void updateBean(FileSystem bean)
    {
        final String  methodName = "updateBean";

        if (entity != null)
        {
            super.updateBean(bean);

            /*
             * The properties are removed from the instance properties and stowed in the bean.
             * Any remaining properties are stored in extendedProperties.
             */
            InstanceProperties instanceProperties = entity.getProperties();

            if (instanceProperties != null)
            {
                bean.setQualifiedName(repositoryHelper.removeStringProperty(serviceName,
                                                                            ReferenceableMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                                                            instanceProperties,
                                                                            methodName));
                bean.setDisplayName(repositoryHelper.removeStringProperty(serviceName,
                                                                          FileSystemMapper.DISPLAY_NAME_PROPERTY_NAME,
                                                                          instanceProperties,
                                                                          methodName));
                bean.setDescription(repositoryHelper.removeStringProperty(serviceName,
                                                                          FileSystemMapper.DESCRIPTION_PROPERTY_NAME,
                                                                          instanceProperties,
                                                                          methodName));
                bean.setFileSystemType(repositoryHelper.removeStringProperty(serviceName,
                                                                       FileSystemMapper.TYPE_PROPERTY_NAME,
                                                                       instanceProperties,
                                                                       methodName));
                bean.setVersion(repositoryHelper.removeStringProperty(serviceName,
                                                                      FileSystemMapper.VERSION_PROPERTY_NAME,
                                                                      instanceProperties,
                                                                      methodName));
                bean.setPatchLevel(repositoryHelper.removeStringProperty(serviceName,
                                                                      FileSystemMapper.PATCH_LEVEL_PROPERTY_NAME,
                                                                      instanceProperties,
                                                                      methodName));
                bean.setSource(repositoryHelper.removeStringProperty(serviceName,
                                                                     FileSystemMapper.SOURCE_PROPERTY_NAME,
                                                                     instanceProperties,
                                                                     methodName));
                bean.setAdditionalProperties(repositoryHelper.removeStringMapFromProperty(serviceName,
                                                                                          ReferenceableMapper.ADDITIONAL_PROPERTIES_PROPERTY_NAME,
                                                                                          instanceProperties,
                                                                                          methodName));

                bean.setExtendedProperties(repositoryHelper.getInstancePropertiesAsMap(instanceProperties));
            }

            instanceProperties = super.getClassificationProperties(FileSystemMapper.FILE_SYSTEM_CLASSIFICATION_TYPE_NAME);

            if (instanceProperties != null)
            {
                bean.setFormat(repositoryHelper.getStringProperty(serviceName,
                                                                  FileSystemMapper.FORMAT_PROPERTY_NAME,
                                                                  instanceProperties,
                                                                  methodName));
                bean.setDisplayName(repositoryHelper.getStringProperty(serviceName,
                                                                       FileSystemMapper.ENCRYPTION_PROPERTY_NAME,
                                                                       instanceProperties,
                                                                       methodName));

            }
        }
    }
}
