/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.builders;

import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.LastAttachmentMapper;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;


/**
 * AssetBuilder creates the root repository entity for an asset.
 */
public class LastAttachmentBuilder extends RootBuilder
{
    private String       anchorGUID;
    private String       anchorType;
    private String       attachmentGUID;
    private String       attachmentType;
    private String       attachmentOwner;
    private String       description;


    /**
     * Creation constructor
     *
     * @param anchorGUID unique identifier of referenceable
     * @param anchorType type name of referenceable.
     * @param attachmentGUID unique identifier for attachment
     * @param attachmentType type name of attachment
     * @param attachmentOwner userId of attachment creator.
     * @param description new description for the asset.
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    public LastAttachmentBuilder(String               anchorGUID,
                                 String               anchorType,
                                 String               attachmentGUID,
                                 String               attachmentType,
                                 String               attachmentOwner,
                                 String               description,
                                 OMRSRepositoryHelper repositoryHelper,
                                 String               serviceName,
                                 String               serverName)
    {
        super(repositoryHelper, serviceName, serverName);

        this.anchorGUID = anchorGUID;
        this.anchorType = anchorType;
        this.attachmentGUID = attachmentGUID;
        this.attachmentType = attachmentType;
        this.attachmentOwner = attachmentOwner;
        this.description = description;
    }


    /**
     * Return the supplied bean properties in an InstanceProperties object.
     *
     * @param methodName name of the calling method
     * @return InstanceProperties object
     * @throws InvalidParameterException there is a problem with the properties
     */
    public InstanceProperties getInstanceProperties(String  methodName) throws InvalidParameterException
    {
        InstanceProperties properties = super.getInstanceProperties(methodName);

        if (anchorGUID != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      LastAttachmentMapper.ANCHOR_GUID_PROPERTY_NAME,
                                                                      anchorGUID,
                                                                      methodName);
        }

        if (anchorType != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      LastAttachmentMapper.ANCHOR_TYPE_PROPERTY_NAME,
                                                                      anchorType,
                                                                      methodName);
        }

        if (attachmentGUID != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      LastAttachmentMapper.ATTACHMENT_GUID_PROPERTY_NAME,
                                                                      attachmentGUID,
                                                                      methodName);
        }

        if (attachmentType != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      LastAttachmentMapper.ATTACHMENT_TYPE_PROPERTY_NAME,
                                                                      attachmentType,
                                                                      methodName);
        }

        if (attachmentOwner != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      LastAttachmentMapper.ATTACHMENT_OWNER_PROPERTY_NAME,
                                                                      attachmentOwner,
                                                                      methodName);
        }

        if (description != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      LastAttachmentMapper.DESCRIPTION_PROPERTY_NAME,
                                                                      description,
                                                                      methodName);
        }



        return properties;
    }
}
