/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.converters;

import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.AssetMapper;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.ReferenceableMapper;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Asset;
import org.odpi.openmetadata.frameworks.openmetadata.enums.AssetOwnerType;
import org.odpi.openmetadata.metadatasecurity.properties.AssetAuditHeader;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * AssetConverter transfers the relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into an Asset bean.
 */
public class AssetConverter extends ReferenceableConverter
{
    /**
     * Constructor captures the initial content with connectionToAssetRelationship
     *
     * @param assetEntity properties to convert
     * @param connectionToAssetRelationship properties to convert
     * @param repositoryHelper helper object to parse entity/relationship objects
     * @param serviceName name of this component
     * @param serverName called server
     */
    public AssetConverter(EntityDetail         assetEntity,
                          Relationship         connectionToAssetRelationship,
                          OMRSRepositoryHelper repositoryHelper,
                          String               serviceName,
                          String               serverName)
    {
        super(assetEntity, connectionToAssetRelationship, repositoryHelper, serviceName, serverName);
    }

    /**
     * Constructor captures the initial content with connectionToAssetRelationship
     *
     * @param assetEntity properties to convert
     * @param connectionToAssetRelationship properties to convert
     * @param repositoryHelper helper object to parse entity/relationship objects
     * @param serviceName name of this component
     */
    public AssetConverter(EntityDetail         assetEntity,
                          Relationship         connectionToAssetRelationship,
                          OMRSRepositoryHelper repositoryHelper,
                          String               serviceName)
    {
        super(assetEntity, connectionToAssetRelationship, repositoryHelper, serviceName, null);
    }


    /**
     * This method is overridable by the subclasses.
     *
     * @return empty bean
     */
    protected Asset  getNewBean()
    {
       return new Asset();
    }


    /**
     * Request the bean is extracted from the repository objects
     *
     * @return output bean
     */
    public Asset getAssetBean()
    {
        Asset  bean = null;

        if (entity != null)
        {
            bean = getNewBean();

            updateBean(bean);
        }

        return bean;
    }


    /**
     * Request the bean is extracted from the repository objects
     *
     * @param bean  output bean
     */
    protected void updateBean(Asset bean)
    {
        final String  methodName = "getBean";

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
                bean.setResourceName(repositoryHelper.removeStringProperty(serviceName,
                                                                          AssetMapper.DISPLAY_NAME_PROPERTY_NAME,
                                                                          instanceProperties,
                                                                          methodName));
                bean.setResourceDescription(repositoryHelper.removeStringProperty(serviceName,
                                                                          AssetMapper.DESCRIPTION_PROPERTY_NAME,
                                                                          instanceProperties,
                                                                          methodName));
                bean.setAdditionalProperties(repositoryHelper.removeStringMapFromProperty(serviceName,
                                                                                          ReferenceableMapper.ADDITIONAL_PROPERTIES_PROPERTY_NAME,
                                                                                          instanceProperties,
                                                                                          methodName));
                bean.setExtendedProperties(repositoryHelper.getInstancePropertiesAsMap(instanceProperties));
            }
        }
    }
}
