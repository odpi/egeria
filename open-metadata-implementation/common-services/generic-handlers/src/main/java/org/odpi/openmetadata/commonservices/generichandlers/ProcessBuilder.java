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
 * ProcessBuilder creates the parts of a root repository entity for a process.  The default type of this process
 * is DeployedSoftwareComponent.
 */
public class ProcessBuilder extends AssetBuilder
{
    private String formula                = null;
    private String implementationLanguage = null;

    /**
     * Creation constructor used when working with classifications
     *
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    ProcessBuilder(OMRSRepositoryHelper repositoryHelper,
                   String               serviceName,
                   String               serverName)
    {
        super(OpenMetadataAPIMapper.DEPLOYED_SOFTWARE_COMPONENT_TYPE_GUID,
              OpenMetadataAPIMapper.DEPLOYED_SOFTWARE_COMPONENT_TYPE_NAME,
              repositoryHelper,
              serviceName,
              serverName);
    }


    /**
     * Constructor supporting all entity properties. (Classifications are added separately.)
     *
     * @param qualifiedName unique name
     * @param technicalName new value for the name
     * @param technicalDescription new description for the process
     * @param formula description of the logic that is implemented by this process
     * @param implementationLanguage language used to implement this process (DeployedSoftwareComponent and subtypes only)
     * @param additionalProperties additional properties
     * @param typeGUID unique identifier for the type of this process
     * @param typeName unique name for the type of this process
     * @param extendedProperties  properties from the subtype
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    ProcessBuilder(String               qualifiedName,
                   String               technicalName,
                   String               technicalDescription,
                   String               formula,
                   String               implementationLanguage,
                   Map<String, String>  additionalProperties,
                   String               typeGUID,
                   String               typeName,
                   Map<String, Object>  extendedProperties,
                   OMRSRepositoryHelper repositoryHelper,
                   String               serviceName,
                   String               serverName)
    {
        super(qualifiedName,
              technicalName,
              technicalDescription,
              additionalProperties,
              typeGUID,
              typeName,
              extendedProperties,
              repositoryHelper,
              serviceName,
              serverName);

        this.formula = formula;
        this.implementationLanguage = implementationLanguage;
    }


    /**
     * Return the bean properties describing the data flow relationship.
     *
     * @param qualifiedName unique name of this relationship
     * @param description description of this relationship
     * @param formula logic describing any filtering of data
     * @param methodName name of the calling method
     * @return InstanceProperties object
     */
    InstanceProperties getDataFlowProperties(String qualifiedName,
                                             String description,
                                             String formula,
                                             String methodName)
    {
        InstanceProperties properties = null;

        if (qualifiedName != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      null,
                                                                      OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                                                      qualifiedName,
                                                                      methodName);
        }

        if (description != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.DESCRIPTION_PROPERTY_NAME,
                                                                      description,
                                                                      methodName);
        }

        if (formula != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.FORMULA_PROPERTY_NAME,
                                                                      formula,
                                                                      methodName);
        }

        return properties;
    }


    /**
     * Return the bean properties describing the control flow relationship.
     *
     * @param qualifiedName unique name of this relationship
     * @param description description of this relationship
     * @param guard logic describing what must be true for control to pass down this control flow
     * @param methodName name of the calling method
     * @return InstanceProperties object
     */
    InstanceProperties getControlFlowProperties(String qualifiedName,
                                                String description,
                                                String guard,
                                                String methodName)
    {
        InstanceProperties properties = null;

        if (qualifiedName != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      null,
                                                                      OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                                                      qualifiedName,
                                                                      methodName);
        }

        if (description != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.DESCRIPTION_PROPERTY_NAME,
                                                                      description,
                                                                      methodName);
        }

        if (guard != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.GUARD_PROPERTY_NAME,
                                                                      guard,
                                                                      methodName);
        }

        return properties;
    }


    /**
     * Return the bean properties describing the process call relationship.
     *
     * @param qualifiedName unique name of this relationship
     * @param description description of this relationship
     * @param formula logic describing any filtering of data on the call
     * @param methodName name of the calling method
     * @return InstanceProperties object
     */
    InstanceProperties getProcessCallProperties(String qualifiedName,
                                                String description,
                                                String formula,
                                                String methodName)
    {
        InstanceProperties properties = null;

        if (qualifiedName != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      null,
                                                                      OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                                                      qualifiedName,
                                                                      methodName);
        }

        if (description != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.DESCRIPTION_PROPERTY_NAME,
                                                                      description,
                                                                      methodName);
        }

        if (formula != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.FORMULA_PROPERTY_NAME,
                                                                      formula,
                                                                      methodName);
        }

        return properties;
    }


    /**
     * Set up the AssetOwnership classification for this entity.
     * This method overrides an previously defined AssetOwnership classification for this entity.
     *
     * @param userId calling user
     * @param owner name of the owner.
     * @param ownerType Enum ordinal for type of owner - 0=userId; 1= profileId; 99=other.
     * @param methodName calling method
     * @throws InvalidParameterException AssetOwnership is not supported in the local repository, or any repository
     *                                   connected by an open metadata repository cohort
     */
    void setAssetOwnership(String userId,
                           String owner,
                           int    ownerType,
                           String methodName) throws InvalidParameterException
    {
        if (owner != null)
        {
            try
            {
                Classification classification = repositoryHelper.getNewClassification(serviceName,
                                                                                      null,
                                                                                      null,
                                                                                      InstanceProvenanceType.LOCAL_COHORT,
                                                                                      userId,
                                                                                      OpenMetadataAPIMapper.ASSET_OWNERSHIP_CLASSIFICATION_NAME,
                                                                                      typeName,
                                                                                      ClassificationOrigin.ASSIGNED,
                                                                                      null,
                                                                                      getOwnerProperties(userId,
                                                                                                         owner,
                                                                                                         ownerType,
                                                                                                         methodName));
                newClassifications.put(classification.getName(), classification);
            }
            catch (TypeErrorException error)
            {
                errorHandler.handleUnsupportedType(error, methodName, OpenMetadataAPIMapper.ASSET_OWNERSHIP_CLASSIFICATION_NAME);
            }
        }
    }


    /**
     * Return the bean properties describing the asset's owner in an InstanceProperties object.
     *
     * @param userId calling user
     * @param owner name of the owner.
     * @param ownerType Enum ordinal for type of owner - 0=userId; 1= profileId; 99=other.
     * @param methodName name of the calling method
     * @return InstanceProperties object
     * @throws InvalidParameterException the owner enum type is not supported
     */
    InstanceProperties getOwnerProperties(String userId,
                                          String owner,
                                          int    ownerType,
                                          String methodName) throws InvalidParameterException
    {
        InstanceProperties properties;

        if (owner != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      null,
                                                                      OpenMetadataAPIMapper.OWNER_PROPERTY_NAME,
                                                                      owner,
                                                                      methodName);

            try
            {
                properties = repositoryHelper.addEnumPropertyToInstance(serviceName,
                                                                        properties,
                                                                        OpenMetadataAPIMapper.OWNER_TYPE_PROPERTY_NAME,
                                                                        OpenMetadataAPIMapper.ASSET_OWNER_TYPE_ENUM_TYPE_GUID,
                                                                        OpenMetadataAPIMapper.ASSET_OWNER_TYPE_ENUM_TYPE_NAME,
                                                                        OpenMetadataAPIMapper.USER_ID_OWNER_TYPE_ORDINAL,
                                                                        methodName);
            }
            catch (TypeErrorException error)
            {
                errorHandler.handleUnsupportedType(error, methodName, OpenMetadataAPIMapper.ASSET_OWNER_TYPE_ENUM_TYPE_NAME);
            }
        }
        else
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      null,
                                                                      OpenMetadataAPIMapper.OWNER_PROPERTY_NAME,
                                                                      userId,
                                                                      methodName);

            try
            {
                properties = repositoryHelper.addEnumPropertyToInstance(serviceName,
                                                                        properties,
                                                                        OpenMetadataAPIMapper.OWNER_TYPE_PROPERTY_NAME,
                                                                        OpenMetadataAPIMapper.ASSET_OWNER_TYPE_ENUM_TYPE_GUID,
                                                                        OpenMetadataAPIMapper.ASSET_OWNER_TYPE_ENUM_TYPE_NAME,
                                                                        ownerType,
                                                                        methodName);
            }
            catch (TypeErrorException error)
            {
                errorHandler.handleUnsupportedType(error, methodName, OpenMetadataAPIMapper.ASSET_OWNER_TYPE_ENUM_TYPE_NAME);
            }
        }

        return properties;
    }


    /**
     * Add the asset origin classification to an asset.
     *
     * @param userId calling user
     * @param organizationGUID Unique identifier (GUID) of the organization where this asset originated from - or null
     * @param businessCapabilityGUID  Unique identifier (GUID) of the business capability where this asset originated from.
     * @param otherOriginValues Descriptive labels describing origin of the asset
     * @param methodName calling method
     *
     * @throws InvalidParameterException entity not known, null userId or guid
     */
    void  setAssetOrigin(String                userId,
                         String                organizationGUID,
                         String                businessCapabilityGUID,
                         Map<String, String>   otherOriginValues,
                         String                methodName) throws InvalidParameterException
    {
        if ((organizationGUID != null) || (businessCapabilityGUID != null) || ((otherOriginValues != null) && (! otherOriginValues.isEmpty())))
        {
            try
            {
                Classification classification = repositoryHelper.getNewClassification(serviceName,
                                                                                      null,
                                                                                      null,
                                                                                      InstanceProvenanceType.LOCAL_COHORT,
                                                                                      userId,
                                                                                      OpenMetadataAPIMapper.ASSET_ORIGIN_CLASSIFICATION_NAME,
                                                                                      typeName,
                                                                                      ClassificationOrigin.ASSIGNED,
                                                                                      null,
                                                                                      getOriginProperties(organizationGUID,
                                                                                                          businessCapabilityGUID,
                                                                                                          otherOriginValues,
                                                                                                          methodName));
                newClassifications.put(classification.getName(), classification);
            }
            catch (TypeErrorException error)
            {
                errorHandler.handleUnsupportedType(error, methodName, OpenMetadataAPIMapper.ASSET_ORIGIN_CLASSIFICATION_NAME);
            }
        }
    }


    /**
     * Return the bean properties describing the asset's owner in an InstanceProperties object.
     *
     * @param organizationGUID Unique identifier (GUID) of the organization where this asset originated from - or null
     * @param businessCapabilityGUID  Unique identifier (GUID) of the business capability where this asset originated from.
     * @param otherOriginValues Descriptive labels describing origin of the asset
     * @param methodName name of the calling method
     * @return InstanceProperties object
     */
    InstanceProperties getOriginProperties(String                organizationGUID,
                                           String                businessCapabilityGUID,
                                           Map<String, String>   otherOriginValues,
                                           String                methodName)
    {
        InstanceProperties properties = null;

        if (organizationGUID != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      null,
                                                                      OpenMetadataAPIMapper.ORGANIZATION_GUID_PROPERTY_NAME,
                                                                      organizationGUID,
                                                                      methodName);
        }

        if (businessCapabilityGUID != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.BUSINESS_CAPABILITY_GUID_PROPERTY_NAME,
                                                                      businessCapabilityGUID,
                                                                      methodName);

        }

        if ((otherOriginValues != null ) && (! otherOriginValues.isEmpty()))
        {
            properties = repositoryHelper.addStringMapPropertyToInstance(serviceName,
                                                                         properties,
                                                                         OpenMetadataAPIMapper.OTHER_ORIGIN_VALUES_PROPERTY_NAME,
                                                                         otherOriginValues,
                                                                         methodName);
        }

        return properties;
    }


    /**
     * Add the reference data classification to an asset.
     *
     * @param userId calling user
     * @param methodName calling method
     *
     * @throws InvalidParameterException entity not known, null userId or guid
     */
    void  setReferenceData(String userId,
                           String methodName) throws InvalidParameterException
    {
        try
        {
            Classification classification = repositoryHelper.getNewClassification(serviceName,
                                                                                  null,
                                                                                  null,
                                                                                  InstanceProvenanceType.LOCAL_COHORT,
                                                                                  userId,
                                                                                  OpenMetadataAPIMapper.REFERENCE_DATA_CLASSIFICATION_TYPE_NAME,
                                                                                  typeName,
                                                                                  ClassificationOrigin.ASSIGNED,
                                                                                  null,
                                                                                  null);
            newClassifications.put(classification.getName(), classification);
        }
        catch (TypeErrorException error)
        {
            errorHandler.handleUnsupportedType(error, methodName, OpenMetadataAPIMapper.REFERENCE_DATA_CLASSIFICATION_TYPE_NAME);
        }
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

        if (formula != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.FORMULA_PROPERTY_NAME,
                                                                      formula,
                                                                      methodName);
        }

        if (implementationLanguage != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.IMPLEMENTATION_LANGUAGE_PROPERTY_NAME,
                                                                      implementationLanguage,
                                                                      methodName);
        }

        return properties;
    }
}
