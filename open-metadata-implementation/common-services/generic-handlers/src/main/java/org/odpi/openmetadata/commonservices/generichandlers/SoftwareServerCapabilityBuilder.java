/* SPDX-License-Identifier: Apache 2.0 */
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
 * SoftwareServerCapabilityBuilder creates the parts for an entity that represents a software server capability.
 */
public class SoftwareServerCapabilityBuilder extends ReferenceableBuilder
{
    private String displayName;
    private String description;
    private String type;
    private String version;
    private String patchLevel;
    private String source;


    /**
     * Create constructor
     *
     * @param qualifiedName qualified name for the file system
     * @param displayName short display name
     * @param description description of the file system
     * @param type type of file system
     * @param version version of file system
     * @param patchLevel patchLevel of software supporting the file system
     * @param source supplier of the software for this file system
     * @param additionalProperties additional properties for a governance zone
     * @param typeName name of the type for this element
     * @param typeId unique identifier of the type for this element
     * @param extendedProperties  properties for a governance zone subtype
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    public SoftwareServerCapabilityBuilder(String               qualifiedName,
                                           String               displayName,
                                           String               description,
                                           String               type,
                                           String               version,
                                           String               patchLevel,
                                           String               source,
                                           Map<String, String>  additionalProperties,
                                           String               typeId,
                                           String               typeName,
                                           Map<String, Object>  extendedProperties,
                                           OMRSRepositoryHelper repositoryHelper,
                                           String               serviceName,
                                           String               serverName)
    {
        super(qualifiedName,
              additionalProperties,
              typeId,
              typeName,
              extendedProperties,
              repositoryHelper,
              serviceName,
              serverName);

        this.displayName = displayName;
        this.description = description;
        this.type = type;
        this.version = version;
        this.patchLevel = patchLevel;
        this.source = source;
    }


    /**
     * Set up the classification that defines the type of the software server capability where there are
     * no associated properties.
     *
     * @param userId calling user
     * @param classificationName name of a changed classification
     * @param methodName calling method
     * @throws InvalidParameterException no support for his open metadata type
     */
    void setCapabilityTypeClassification(String userId,
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
            errorHandler.handleUnsupportedType(error, methodName, OpenMetadataAPIMapper.LATEST_CHANGE_CLASSIFICATION_TYPE_NAME);
        }
    }


    /**
     * Return the supplied bean properties in an InstanceProperties object.
     *
     * @param userId calling user
     * @param format format used by the file system
     * @param encryption type of encryption
     * @param methodName name of the calling method
     * @throws InvalidParameterException classification not supported
     */
    void setFileSystemClassification(String userId,
                                     String format,
                                     String encryption,
                                     String methodName) throws InvalidParameterException
    {
        InstanceProperties properties = null;

        if (format != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      null,
                                                                      OpenMetadataAPIMapper.FORMAT_PROPERTY_NAME,
                                                                      format,
                                                                      methodName);
        }

        if (encryption != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.ENCRYPTION_PROPERTY_NAME,
                                                                      encryption,
                                                                      methodName);
        }

        try
        {
            Classification classification = repositoryHelper.getNewClassification(serviceName,
                                                                                  null,
                                                                                  null,
                                                                                  InstanceProvenanceType.LOCAL_COHORT,
                                                                                  userId,
                                                                                  OpenMetadataAPIMapper.FILE_SYSTEM_CLASSIFICATION_TYPE_NAME,
                                                                                  typeName,
                                                                                  ClassificationOrigin.ASSIGNED,
                                                                                  null,
                                                                                  properties);
            newClassifications.put(classification.getName(), classification);
        }
        catch (TypeErrorException error)
        {
            errorHandler.handleUnsupportedType(error, methodName, OpenMetadataAPIMapper.LATEST_CHANGE_CLASSIFICATION_TYPE_NAME);
        }
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

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.DEPLOYED_IMPLEMENTATION_TYPE_PROPERTY_NAME,
                                                                  type,
                                                                  methodName);
        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.CAPABILITY_VERSION_PROPERTY_NAME,
                                                                  version,
                                                                  methodName);
        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.PATCH_LEVEL_PROPERTY_NAME,
                                                                  patchLevel,
                                                                  methodName);
        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.SOURCE_PROPERTY_NAME,
                                                                  source,
                                                                  methodName);

        return properties;
    }
}
