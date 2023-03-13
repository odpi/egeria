/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.ClassificationOrigin;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProvenanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;

import java.util.Map;

/**
 * CollectionBuilder creates the parts for an entity that represents a collection.
 */
public class CollectionBuilder extends ReferenceableBuilder
{
    private String displayName = null;
    private String description = null;


    /**
     * Create constructor
     *
     * @param qualifiedName unique name for the collection
     * @param displayName short display name for the collection
     * @param description description of the collection
     * @param additionalProperties additional properties for a collection
     * @param typeGUID unique identifier of this element's type
     * @param typeName unique name of this element's type
     * @param extendedProperties  properties for a collection subtype
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    CollectionBuilder(String               qualifiedName,
                      String               displayName,
                      String               description,
                      Map<String, String>  additionalProperties,
                      String               typeGUID,
                      String               typeName,
                      Map<String, Object>  extendedProperties,
                      OMRSRepositoryHelper repositoryHelper,
                      String               serviceName,
                      String               serverName)
    {
        super(qualifiedName,
              additionalProperties,
              typeGUID,
              typeName,
              extendedProperties,
              repositoryHelper,
              serviceName,
              serverName);

        this.displayName = displayName;
        this.description = description;
    }


    /**
     * Create constructor
     *
     * @param qualifiedName unique name for the collection
     * @param displayName short display name for the collection
     * @param description description of the collection
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    CollectionBuilder(String               qualifiedName,
                      String               displayName,
                      String               description,
                      OMRSRepositoryHelper repositoryHelper,
                      String               serviceName,
                      String               serverName)
    {
        super(qualifiedName,
              null,
              OpenMetadataAPIMapper.COLLECTION_TYPE_GUID,
              OpenMetadataAPIMapper.COLLECTION_TYPE_NAME,
              null,
              repositoryHelper,
              serviceName,
              serverName);

        this.displayName = displayName;
        this.description = description;
    }


    /**
     * Classification constructor
     *
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    CollectionBuilder(OMRSRepositoryHelper repositoryHelper,
                      String               serviceName,
                      String               serverName)
    {
        super(OpenMetadataAPIMapper.COLLECTION_TYPE_GUID,
              OpenMetadataAPIMapper.COLLECTION_TYPE_NAME,
              repositoryHelper,
              serviceName,
              serverName);
    }


    /**
     * Return the supplied bean properties in an InstanceProperties object.
     *
     * @param methodName name of the calling method
     * @return InstanceProperties object
     * @throws InvalidParameterException there is a problem with the properties
     */
    @Override
    public InstanceProperties getInstanceProperties(String  methodName) throws InvalidParameterException
    {
        InstanceProperties properties = super.getInstanceProperties(methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.NAME_PROPERTY_NAME,
                                                                  displayName,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.DESCRIPTION_PROPERTY_NAME,
                                                                  description,
                                                                  methodName);

        return properties;
    }


    /**
     * Set up the classification for this collection - assuming it has no properties.
     *
     * @param userId calling user
     * @param classificationName name of the classification
     * @param methodName name of the calling method
     */
    void setupCollectionClassification(String userId,
                                       String classificationName,
                                       String methodName) throws InvalidParameterException
    {
        try
        {
            Classification classification = repositoryHelper.getNewClassification(serviceName,
                                                                                  null,
                                                                                  null,
                                                                                  InstanceProvenanceType.LOCAL_COHORT,
                                                                                  userId,
                                                                                  classificationName,
                                                                                  typeName,
                                                                                  ClassificationOrigin.ASSIGNED,
                                                                                  null,
                                                                                  null);
            newClassifications.put(classification.getName(), classification);
        }
        catch (TypeErrorException error)
        {
            errorHandler.handleUnsupportedType(error, methodName, OpenMetadataAPIMapper.FOLDER_TYPE_NAME);
        }
    }


    /**
     * Set up the classification to show that this collection is a folder.
     *
     * @param userId calling user
     * @param orderBy the factor used to organize the members
     * @param orderPropertyName name of property of OrderBy is 99 (OTHER)
     * @param methodName name of the calling method
     */
    void setupFolderClassification(String userId,
                                   int    orderBy,
                                   String orderPropertyName,
                                   String methodName) throws InvalidParameterException
    {
        try
        {
            Classification classification = repositoryHelper.getNewClassification(serviceName,
                                                                                  null,
                                                                                  null,
                                                                                  InstanceProvenanceType.LOCAL_COHORT,
                                                                                  userId,
                                                                                  OpenMetadataAPIMapper.FOLDER_TYPE_NAME,
                                                                                  typeName,
                                                                                  ClassificationOrigin.ASSIGNED,
                                                                                  null,
                                                                                  getFolderProperties(orderBy, orderPropertyName, methodName));
            newClassifications.put(classification.getName(), classification);
        }
        catch (TypeErrorException error)
        {
            errorHandler.handleUnsupportedType(error, methodName, OpenMetadataAPIMapper.FOLDER_TYPE_NAME);
        }
    }



    /**
     * Return the bean properties describing a taxonomy in an InstanceProperties object.
     *
     * @param orderBy the factor used to organize the members
     * @param orderPropertyName name of property of OrderBy is 99 (OTHER)
     * @param methodName name of the calling method
     * @return InstanceProperties object
     */
    InstanceProperties getFolderProperties(int    orderBy,
                                           String orderPropertyName,
                                           String methodName)
    {
        InstanceProperties properties = repositoryHelper.addIntPropertyToInstance(serviceName,
                                                                                  null,
                                                                                  OpenMetadataAPIMapper.ORDER_BY_PROPERTY_NAME,
                                                                                  orderBy,
                                                                                  methodName);


        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.ORDER_PROPERTY_NAME_PROPERTY_NAME,
                                                                  orderPropertyName,
                                                                  methodName);

        setEffectivityDates(properties);

        return properties;
    }
}
