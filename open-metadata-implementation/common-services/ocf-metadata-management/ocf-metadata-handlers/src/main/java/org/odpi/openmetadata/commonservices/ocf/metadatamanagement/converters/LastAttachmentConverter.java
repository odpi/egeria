/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.converters;


import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.LastAttachmentMapper;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.properties.LastAttachment;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

/**
 * LastAttachmentConverter transfers the relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into a LastAttachment bean.
 */
public class LastAttachmentConverter extends ElementHeaderConverter
{
    /**
     * Constructor captures the initial content
     *
     * @param entity properties to convert
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     */
    public LastAttachmentConverter(EntityDetail         entity,
                                   OMRSRepositoryHelper repositoryHelper,
                                   String               serviceName)
    {
        super(entity, repositoryHelper, serviceName);
    }


    /**
     * Request the bean is extracted from the repository entity.
     *
     * @return output bean
     */
    public LastAttachment getBean()
    {
        final String  methodName = "getBean";

        LastAttachment  bean = null;

        if (entity != null)
        {
            bean = new LastAttachment();

            super.updateBean(bean);
            bean.setUpdateTime(entity.getUpdateTime());

            InstanceProperties instanceProperties = entity.getProperties();

            if (instanceProperties != null)
            {
                bean.setAnchorGUID(repositoryHelper.removeStringProperty(serviceName,
                                                                         LastAttachmentMapper.ANCHOR_GUID_PROPERTY_NAME,
                                                                         instanceProperties,
                                                                         methodName));
                bean.setAnchorType(repositoryHelper.removeStringProperty(serviceName,
                                                                         LastAttachmentMapper.ANCHOR_TYPE_PROPERTY_NAME,
                                                                         instanceProperties,
                                                                         methodName));
                bean.setAttachmentGUID(repositoryHelper.removeStringProperty(serviceName,
                                                                             LastAttachmentMapper.ATTACHMENT_GUID_PROPERTY_NAME,
                                                                             instanceProperties,
                                                                             methodName));
                bean.setAttachmentType(repositoryHelper.removeStringProperty(serviceName,
                                                                             LastAttachmentMapper.ATTACHMENT_TYPE_PROPERTY_NAME,
                                                                             instanceProperties,
                                                                             methodName));
                bean.setAttachmentOwner(repositoryHelper.removeStringProperty(serviceName,
                                                                              LastAttachmentMapper.ATTACHMENT_OWNER_PROPERTY_NAME,
                                                                              instanceProperties,
                                                                              methodName));
                bean.setDescription(repositoryHelper.removeStringProperty(serviceName,
                                                                          LastAttachmentMapper.DESCRIPTION_PROPERTY_NAME,
                                                                          instanceProperties,
                                                                          methodName));
                bean.setExtendedProperties(repositoryHelper.getInstancePropertiesAsMap(instanceProperties));
            }
        }

        return bean;
    }
}
