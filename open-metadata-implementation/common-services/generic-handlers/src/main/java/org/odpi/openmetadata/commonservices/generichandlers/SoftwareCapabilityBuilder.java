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
 * SoftwareCapabilityBuilder creates the parts for an entity that represents a software capability.
 */
public class SoftwareCapabilityBuilder extends ReferenceableBuilder
{
    private final String displayName;
    private final String description;

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
    public SoftwareCapabilityBuilder(String               qualifiedName,
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
     * Create constructor - when templating
     *
     * @param qualifiedName unique name for the connection
     * @param displayName short display name for the connection
     * @param description description of the connection
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    SoftwareCapabilityBuilder(String               qualifiedName,
                              String               displayName,
                              String               description,
                              OMRSRepositoryHelper repositoryHelper,
                              String               serviceName,
                              String               serverName)
    {
        super(qualifiedName,
              null,
              OpenMetadataAPIMapper.SOFTWARE_CAPABILITY_TYPE_GUID,
              OpenMetadataAPIMapper.SOFTWARE_CAPABILITY_TYPE_NAME,
              null,
              repositoryHelper,
              serviceName,
              serverName);

        this.displayName = displayName;
        this.description = description;
    }


    /**
     * Create constructor
     *
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    public SoftwareCapabilityBuilder(OMRSRepositoryHelper repositoryHelper,
                                     String               serviceName,
                                     String               serverName)
    {
        super(repositoryHelper,
              serviceName,
              serverName);

        this.displayName = null;
        this.description = null;
        this.type = null;
        this.version = null;
        this.patchLevel = null;
        this.source = null;
    }


    /**
     * Set up the classification that defines the type of the software capability where there are
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
            errorHandler.handleUnsupportedType(error, methodName, classificationName);
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
        InstanceProperties properties;

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      null,
                                                                      OpenMetadataAPIMapper.FORMAT_PROPERTY_NAME,
                                                                      format,
                                                                      methodName);
        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.ENCRYPTION_PROPERTY_NAME,
                                                                      encryption,
                                                                      methodName);


        /*
         * The classification is set up with the same effectivity dates as the entity so can search on classifications.
         */
        super.setEffectivityDates(properties);

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
            errorHandler.handleUnsupportedType(error, methodName, OpenMetadataAPIMapper.FILE_SYSTEM_CLASSIFICATION_TYPE_NAME);
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
                                                                  OpenMetadataAPIMapper.CAPABILITY_TYPE_PROPERTY_NAME,
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
