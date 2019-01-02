/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetconsumer.converters;

import org.odpi.openmetadata.accessservices.assetconsumer.properties.ReferenceableClassification;
import org.odpi.openmetadata.accessservices.assetconsumer.properties.ReferenceableHeader;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * ReferenceableHeaderConverter transfers the relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into a ReferenceableHeader bean.
 */
public class ReferenceableHeaderConverter
{
    public static final String QUALIFIED_NAME_PROPERTY_NAME        = "qualifiedName";
    public static final String ADDITIONAL_PROPERTIES_PROPERTY_NAME = "additionalProperties";

    private static final String unknownProvenance                = "<Unknown>";

    private ReferenceableHeader  bean = null;

    private EntityDetail         entity;
    private OMRSRepositoryHelper repositoryHelper;
    private String               componentName;

    /**
     * Constructor captures the initial content
     *
     * @param entity properties to convert
     * @param repositoryHelper helper object to parse entity
     * @param componentName name of this component
     */
    ReferenceableHeaderConverter(EntityDetail         entity,
                                 OMRSRepositoryHelper repositoryHelper,
                                 String               componentName)
    {
        this.entity = entity;
        this.repositoryHelper = repositoryHelper;
        this.componentName = componentName;

    }


    /**
     * Set up the bean to convert.
     *
     * @param bean output bean
     */
    void setBean(ReferenceableHeader  bean)
    {
        this.bean = bean;
        this.updateBean(entity, repositoryHelper, componentName);
    }


    /**
     * Extract the Referenceable properties from the entity.
     *
     * @param entity entity containing the relevant properties
     * @param repositoryHelper helper object to parse entity
     * @param componentName name of this component
     */
    private void updateBean(EntityDetail         entity,
                            OMRSRepositoryHelper repositoryHelper,
                            String               componentName)
    {
        final String  methodName = "updateBean";

        if (entity != null)
        {
            bean.setGUID(entity.getGUID());

            InstanceType instanceType = entity.getType();
            if (instanceType != null)
            {
                bean.setTypeName(instanceType.getTypeDefName());
                bean.setTypeDescription(instanceType.getTypeDefDescription());
            }

            bean.setOriginId(entity.getMetadataCollectionId());
            bean.setOriginName(entity.getMetadataCollectionName());

            if (entity.getInstanceProvenanceType() != null)
            {
                bean.setOriginType(entity.getInstanceProvenanceType().getName());
            }
            else
            {
                bean.setOriginType(unknownProvenance);
            }
            bean.setOriginLicense(entity.getInstanceLicense());

            bean.setClassifications(this.getEntityClassifications(entity, repositoryHelper));

            InstanceProperties instanceProperties = entity.getProperties();

            bean.setQualifiedName(repositoryHelper.getStringProperty(componentName, QUALIFIED_NAME_PROPERTY_NAME, instanceProperties, methodName));
            bean.setAdditionalProperties(repositoryHelper.getMapFromProperty(componentName, ADDITIONAL_PROPERTIES_PROPERTY_NAME, instanceProperties, methodName));
        }
    }


    /**
     * Extract the classifications from the entity.
     *
     * @param entity entity containing the relevant properties
     * @param repositoryHelper helper object to parse entity
     */
    private List<ReferenceableClassification> getEntityClassifications(EntityDetail         entity,
                                                                       OMRSRepositoryHelper repositoryHelper)
    {
        List<ReferenceableClassification> classifications = null;
        List<Classification>              entityClassifications = entity.getClassifications();

        if (entityClassifications != null)
        {
            classifications = new ArrayList<>();

            for (Classification  entityClassification : entityClassifications)
            {
                if (entityClassification != null)
                {
                    ReferenceableClassification  beanClassification = new ReferenceableClassification();

                    beanClassification.setClassificationName(entityClassification.getName());
                    beanClassification.setClassificationProperties(repositoryHelper.getInstancePropertiesAsMap(entityClassification.getProperties()));
                }
            }
        }

        return classifications;
    }
}
