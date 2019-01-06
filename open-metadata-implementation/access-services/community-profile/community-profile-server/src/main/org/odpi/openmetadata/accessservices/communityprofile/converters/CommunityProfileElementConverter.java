/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.converters;

import org.odpi.openmetadata.accessservices.communityprofile.properties.Classification;
import org.odpi.openmetadata.accessservices.communityprofile.properties.CommunityProfileElementHeader;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.List;


/**
 * CommunityProfileElementConverter provides the root converter for the Community Profile OMAS beans.
 */
public class CommunityProfileElementConverter
{
    private static final String unknownProvenance                = "<Unknown>";

    EntityDetail         entity;
    Relationship         relationship = null;
    OMRSRepositoryHelper repositoryHelper;
    String               serviceName;

    /**
     * Constructor captures the initial content
     *
     * @param entity properties to convert
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     */
    CommunityProfileElementConverter(EntityDetail         entity,
                                     OMRSRepositoryHelper repositoryHelper,
                                     String               serviceName)
    {
        this.entity = entity;
        this.repositoryHelper = repositoryHelper;
        this.serviceName = serviceName;
    }


    /**
     * Constructor captures the initial content with relationship
     *
     * @param entity properties to convert
     * @param relationship properties to convert
     * @param repositoryHelper helper object to parse entity/relationship
     * @param serviceName name of this component
     */
    CommunityProfileElementConverter(EntityDetail         entity,
                                     Relationship         relationship,
                                     OMRSRepositoryHelper repositoryHelper,
                                     String               serviceName)
    {
        this.entity = entity;
        this.relationship = relationship;
        this.repositoryHelper = repositoryHelper;
        this.serviceName = serviceName;
    }


    /**
     * Extract the properties from the entity.
     */
    void updateBean(CommunityProfileElementHeader  bean)
    {
        if (entity != null)
        {
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
        }
    }


    /**
     * Extract the classifications from the entity.
     *
     * @return list of bean classifications
     */
    List<Classification> getClassificationsFromEntity()
    {
        List<Classification> classifications       = null;

        if (entity != null)
        {
            List<org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification> entityClassifications = entity.getClassifications();

            if (entityClassifications != null)
            {
                classifications = new ArrayList<>();

                for (org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification entityClassification : entityClassifications)
                {
                    if (entityClassification != null)
                    {
                        Classification beanClassification = new Classification();

                        beanClassification.setName(entityClassification.getName());
                        beanClassification.setProperties(repositoryHelper.getInstancePropertiesAsMap(entityClassification.getProperties()));
                    }
                }
            }
        }

        return classifications;
    }
}
