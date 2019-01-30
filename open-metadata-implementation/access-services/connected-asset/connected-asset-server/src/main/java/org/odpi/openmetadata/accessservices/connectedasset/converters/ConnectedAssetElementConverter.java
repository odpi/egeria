/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.connectedasset.converters;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.Classification;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementHeader;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.List;


/**
 * ConnectedAssetElementConverter provides the root converter for the Open Connector Framework (OCF) beans.
 */
public class ConnectedAssetElementConverter
{
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
    ConnectedAssetElementConverter(EntityDetail         entity,
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
    ConnectedAssetElementConverter(EntityDetail         entity,
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
    void updateBean(ElementHeader bean)
    {
        if (entity != null)
        {
            TypeConverter typeConverter = new TypeConverter();

            bean.setType(typeConverter.getElementType(entity.getType(),
                                                      entity.getInstanceProvenanceType(),
                                                      entity.getMetadataCollectionId(),
                                                      entity.getMetadataCollectionName(),
                                                      entity.getInstanceLicense()));
            bean.setGUID(entity.getGUID());
            bean.setURL(entity.getInstanceURL());
            bean.setClassifications(this.getClassificationsFromEntity());
        }
    }


    /**
     * Extract the classifications from the entity.
     *
     * @return list of bean classifications
     */
    private List<Classification> getClassificationsFromEntity()
    {
        List<Classification> classifications = null;

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

                        beanClassification.setClassificationName(entityClassification.getName());
                        beanClassification.setClassificationProperties(repositoryHelper.getInstancePropertiesAsMap(entityClassification.getProperties()));
                    }
                }
            }
        }

        return classifications;
    }



}
