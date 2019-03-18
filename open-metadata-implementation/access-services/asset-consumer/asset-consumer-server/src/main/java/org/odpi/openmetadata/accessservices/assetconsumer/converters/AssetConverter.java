/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetconsumer.converters;


import org.odpi.openmetadata.accessservices.assetconsumer.properties.Asset;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

/**
 * AssetConverter transfers the relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into an Asset bean.
 */
public class AssetConverter extends ReferenceableHeaderConverter
{
    public static final String DISPLAY_NAME_PROPERTY_NAME = "name";
    private static final String ownerPropertyName          = "owner";
    private static final String descriptionPropertyName    = "description";
    private static final String zoneMembershipPropertyName = "zoneMembership";
    private static final String latestChangePropertyName   = "latestChange";

    private Asset  assetBean = null;


    /**
     * Constructor takes in the source of information for the bean
     *
     * @param entity properties to convert
     * @param repositoryHelper helper object to parse entity
     * @param componentName name of this component
     */
    public AssetConverter(EntityDetail         entity,
                          OMRSRepositoryHelper repositoryHelper,
                          String               componentName)
    {
        super(entity, repositoryHelper, componentName);

        if (entity != null)
        {
            this.assetBean = new Asset();
            super.setBean(assetBean);
            this.updateAssetBean(entity, repositoryHelper, componentName);
        }
    }


    /**
     * Extract the Asset specific properties from the entity.
     *
     * @param entity entity containing the relevant properties
     * @param repositoryHelper helper object to parse entity
     * @param componentName name of this component
     */
    private void updateAssetBean(EntityDetail         entity,
                                 OMRSRepositoryHelper repositoryHelper,
                                 String               componentName)
    {
        final String  methodName = "updateAssetBean";

        if (entity != null)
        {
            InstanceProperties instanceProperties = entity.getProperties();

            assetBean.setDisplayName(repositoryHelper.getStringProperty(componentName, DISPLAY_NAME_PROPERTY_NAME, instanceProperties, methodName));
            assetBean.setOwner(repositoryHelper.getStringProperty(componentName, ownerPropertyName, instanceProperties, methodName));
            assetBean.setDescription(repositoryHelper.getStringProperty(componentName, descriptionPropertyName, instanceProperties, methodName));
            assetBean.setZoneMembership(repositoryHelper.getStringArrayProperty(componentName, zoneMembershipPropertyName, instanceProperties, methodName));
            assetBean.setLastChange(repositoryHelper.getStringProperty(componentName, latestChangePropertyName, instanceProperties, methodName));
        }
    }


    /**
     * Return the converted bean.
     *
     * @return bean populated with properties from the entity supplied in the constructor
     */
    public Asset  getBean()
    {
        return assetBean;
    }
}
