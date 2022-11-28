/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.commonservices.generichandlers.ffdc.GenericHandlersErrorCode;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryErrorHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;

import java.util.*;

/**
 * OpenMetadataAPIGenericBuilder provides the common functions for building new entities, relationships and
 * classifications.  Subtypes push details of their classifications to this supertype.  They manage their instance properties.
 */
public class OpenMetadataAPIGenericBuilder
{
    protected RepositoryErrorHandler      errorHandler;
    protected OMRSRepositoryHelper        repositoryHelper;
    protected String                      serviceName;
    protected String                      serverName;

    protected Map<String, Object>         extendedProperties;
    protected String                      typeGUID;
    protected String                      typeName;

    protected InstanceStatus              instanceStatus;
    private   Date                        effectiveFrom;
    private   Date                        effectiveTo;
    protected List<Classification>        existingClassifications;
    protected Map<String, Classification> newClassifications = new HashMap<>();

    protected InstanceProperties templateProperties = null;


    /**
     * Constructor for working with classifications.
     *
     * @param typeGUID type GUID to use for the entity
     * @param typeName type name to use for the entity
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    protected OpenMetadataAPIGenericBuilder(String               typeGUID,
                                            String               typeName,
                                            OMRSRepositoryHelper repositoryHelper,
                                            String               serviceName,
                                            String               serverName)
    {
        this(typeGUID,
             typeName,
             null,
             null,
             null,
             repositoryHelper,
             serviceName,
             serverName);
    }


    /**
     * Constructor for type specific creates.
     *
     * @param typeGUID type GUID to use for the entity
     * @param typeName type name to use for the entity
     * @param extendedProperties properties for the sub type (if any)
     * @param instanceStatus status to use on the request
     * @param existingClassifications classifications that are currently stored
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    protected OpenMetadataAPIGenericBuilder(String               typeGUID,
                                            String               typeName,
                                            Map<String, Object>  extendedProperties,
                                            InstanceStatus       instanceStatus,
                                            List<Classification> existingClassifications,
                                            OMRSRepositoryHelper repositoryHelper,
                                            String               serviceName,
                                            String               serverName)
    {
        this(typeGUID,
             typeName,
             extendedProperties,
             instanceStatus,
             null,
             null,
             existingClassifications,
             repositoryHelper,
             serviceName,
             serverName);
    }


    /**
     * Constructor for type specific creates.
     *
     * @param typeGUID type GUID to use for the entity
     * @param typeName type name to use for the entity
     * @param extendedProperties properties for the sub type (if any)
     * @param instanceStatus status to use on the request
     * @param effectiveFrom date to make the element active in the governance program (null for now)
     * @param effectiveTo date to remove the element from the governance program (null = until deleted)
     * @param existingClassifications classifications that are currently stored
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    protected OpenMetadataAPIGenericBuilder(String               typeGUID,
                                            String               typeName,
                                            Map<String, Object>  extendedProperties,
                                            InstanceStatus       instanceStatus,
                                            Date                 effectiveFrom,
                                            Date                 effectiveTo,
                                            List<Classification> existingClassifications,
                                            OMRSRepositoryHelper repositoryHelper,
                                            String               serviceName,
                                            String               serverName)
    {
        this.repositoryHelper        = repositoryHelper;
        this.serviceName             = serviceName;
        this.serverName              = serverName;
        this.typeGUID                = typeGUID;
        this.typeName                = typeName;
        this.extendedProperties      = extendedProperties;
        this.instanceStatus          = instanceStatus;
        this.effectiveFrom           = effectiveFrom;
        this.effectiveTo             = effectiveTo;
        this.existingClassifications = existingClassifications;

        if (this.typeName == null)
        {
            this.typeName = OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME;
        }

        if (this.typeGUID == null)
        {
            TypeDef typeDef = repositoryHelper.getTypeDefByName(serviceName, this.typeName);
            if (typeDef != null)
            {
                this.typeGUID = typeDef.getGUID();
            }
        }

        this.errorHandler = new RepositoryErrorHandler(repositoryHelper, serviceName, serverName);
    }


    /**
     * Add a classification to the entity.  This overrides any existing value for this classification.
     *
     * @param newClassification classification object ready for the repository
     */
    public void setClassification(Classification  newClassification)
    {
        newClassifications.put(newClassification.getName(), newClassification);
    }



    /**
     * Set up the list of classifications from a template entity.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param templateClassifications list of classifications from the template
     * @param methodName calling method
     * @throws InvalidParameterException the type of one of the classifications is not supported
     */
    public void setTemplateClassifications(String                userId,
                                           String                externalSourceGUID,
                                           String                externalSourceName,
                                           List<Classification>  templateClassifications,
                                           String                methodName) throws InvalidParameterException
    {
        if (templateClassifications == null)
        {
            this.newClassifications = new HashMap<>();
        }
        else
        {
            InstanceProvenanceType instanceProvenanceType = InstanceProvenanceType.LOCAL_COHORT;

            if (externalSourceGUID != null)
            {
                instanceProvenanceType = InstanceProvenanceType.EXTERNAL_SOURCE;
            }

            for (Classification templateClassification : templateClassifications)
            {
                if (templateClassification != null)
                {
                    try
                    {
                        Classification classification;

                        if (templateClassification.getInstanceProvenanceType() == InstanceProvenanceType.LOCAL_COHORT)
                        {
                            classification = repositoryHelper.getNewClassification(serviceName,
                                                                                   null,
                                                                                   null,
                                                                                   InstanceProvenanceType.LOCAL_COHORT,
                                                                                   userId,
                                                                                   templateClassification.getName(),
                                                                                   typeName,
                                                                                   ClassificationOrigin.ASSIGNED,
                                                                                   null,
                                                                                   templateClassification.getProperties());
                        }
                        else
                        {
                            classification = repositoryHelper.getNewClassification(serviceName,
                                                                                   externalSourceGUID,
                                                                                   externalSourceName,
                                                                                   instanceProvenanceType,
                                                                                   userId,
                                                                                   templateClassification.getName(),
                                                                                   typeName,
                                                                                   ClassificationOrigin.ASSIGNED,
                                                                                   null,
                                                                                   templateClassification.getProperties());
                        }
                        this.newClassifications.put(classification.getName(), classification);
                    }
                    catch (TypeErrorException error)
                    {
                        errorHandler.handleUnsupportedType(error, methodName, templateClassification.getName());
                    }
                }
            }
        }
    }


    /**
     * Set up the "Anchors" classification for this entity.  This is used when a new entity is being created, and it is known to be
     * connected to a specific anchor.
     *
     * @param userId calling user
     * @param anchorGUID unique identifier of the anchor entity that this entity is linked to directly or indirectly
     * @param methodName calling method
     * @throws PropertyServerException a null anchors GUID has been supplied
     */
    public void setAnchors(String userId,
                           String anchorGUID,
                           String methodName) throws PropertyServerException
    {
        final String localMethodName = "setAnchors";

        if (anchorGUID != null)
        {
            try
            {
                /*
                 * This is an attempt to trap an intermittent error recorded in issue #4680.
                 */
                if ("<unknown>".equals(anchorGUID))
                {
                    throw new PropertyServerException(GenericHandlersErrorCode.UNKNOWN_ANCHOR_GUID.getMessageDefinition(localMethodName,
                                                                                                                        serviceName,
                                                                                                                        methodName),
                                                      this.getClass().getName(),
                                                      localMethodName);
                }

                Classification classification = repositoryHelper.getNewClassification(serviceName,
                                                                                      null,
                                                                                      null,
                                                                                      InstanceProvenanceType.LOCAL_COHORT,
                                                                                      userId,
                                                                                      OpenMetadataAPIMapper.ANCHORS_CLASSIFICATION_TYPE_NAME,
                                                                                      typeName,
                                                                                      ClassificationOrigin.ASSIGNED,
                                                                                      null,
                                                                                      getAnchorsProperties(anchorGUID, methodName));
                newClassifications.put(classification.getName(), classification);
            }
            catch (Exception error)
            {
                errorHandler.handleUnsupportedAnchorsType(error, methodName, OpenMetadataAPIMapper.ANCHORS_CLASSIFICATION_TYPE_NAME);
            }
        }
        else
        {
            throw new PropertyServerException(GenericHandlersErrorCode.NULL_ANCHOR_GUID.getMessageDefinition(localMethodName,
                                                                                                             serviceName,
                                                                                                             methodName),
                                              this.getClass().getName(),
                                              localMethodName);
        }
    }


    /**
     * Return the Anchors properties in an InstanceProperties object.
     *
     * @param methodName name of the calling method
     * @param anchorGUID unique identifier of the anchor entity that this entity is linked to directly or indirectly
     * @return InstanceProperties object
     */
    InstanceProperties getAnchorsProperties(String anchorGUID,
                                            String methodName)
    {
        InstanceProperties properties = null;

        if (anchorGUID != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      null,
                                                                      OpenMetadataAPIMapper.ANCHOR_GUID_PROPERTY_NAME,
                                                                      anchorGUID,
                                                                      methodName);
        }

        return properties;
    }


    /**
     * Set up the effective dates for the entity.  This determines
     *
     * @param effectiveFrom date that the element is effective from
     * @param effectiveTo date that the element is effective to
     */
    public void setEffectivityDates(Date effectiveFrom,
                                    Date effectiveTo)
    {
        this.effectiveFrom = effectiveFrom;
        this.effectiveTo   = effectiveTo;
    }


    /**
     * Set up the LatestChange classification for an anchor entity.  This is typically used on the create of
     * the anchor and other direct update actions on it.  Other updates are made to it through the generic handler
     * as attachments are added and changed.
     *
     * @param userId calling user
     * @param latestChangeTargetOrdinal ordinal for the LatestChangeTarget enum value
     * @param latestChangeActionOrdinal ordinal for the LatestChangeAction enum value
     * @param classificationName name of a changed classification
     * @param attachmentGUID unique identifier of an entity attached to the anchor
     * @param attachmentTypeName type name of the attached entity
     * @param relationshipTypeName relationship used to attach the entity
     * @param actionDescription human readable description of the change
     * @param methodName calling method
     * @throws InvalidParameterException this classification is not supported
     */
    public void setLatestChange(String userId,
                                int    latestChangeTargetOrdinal,
                                int    latestChangeActionOrdinal,
                                String classificationName,
                                String attachmentGUID,
                                String attachmentTypeName,
                                String relationshipTypeName,
                                String actionDescription,
                                String methodName) throws InvalidParameterException
    {
        try
        {
            Classification classification = repositoryHelper.getNewClassification(serviceName,
                                                                                  null,
                                                                                  null,
                                                                                  InstanceProvenanceType.LOCAL_COHORT,
                                                                                  userId,
                                                                                  OpenMetadataAPIMapper.LATEST_CHANGE_CLASSIFICATION_TYPE_NAME,
                                                                                  typeName,
                                                                                  ClassificationOrigin.ASSIGNED,
                                                                                  null,
                                                                                  getLatestChangeProperties(latestChangeTargetOrdinal,
                                                                                                            latestChangeActionOrdinal,
                                                                                                            classificationName,
                                                                                                            attachmentGUID,
                                                                                                            attachmentTypeName,
                                                                                                            relationshipTypeName,
                                                                                                            userId,
                                                                                                            actionDescription,
                                                                                                            methodName));
            newClassifications.put(classification.getName(), classification);
        }
        catch (TypeErrorException error)
        {
            errorHandler.handleUnsupportedType(error, methodName, OpenMetadataAPIMapper.LATEST_CHANGE_CLASSIFICATION_TYPE_NAME);
        }
    }


    /**
     * Set up the LatestChange classification for an anchor entity.  This is typically used on the create of
     * the anchor and other direct update actions on it.  Other updates are made to it through the generic handler
     * as attachments are added and changed.
     *
     * @param latestChangeTargetOrdinal ordinal for the LatestChangeTarget enum value
     * @param latestChangeActionOrdinal ordinal for the LatestChangeAction enum value
     * @param classificationName name of a changed classification
     * @param attachmentGUID unique identifier of an entity attached to the anchor
     * @param attachmentTypeName type name of the attached entity
     * @param relationshipTypeName relationship used to attach the entity
     * @param userId userId making the change
     * @param actionDescription human-readable description of the change
     * @param methodName calling method
     * @return properties for classification
     * @throws InvalidParameterException problem with the enum types
     */
    private InstanceProperties getLatestChangeProperties(int    latestChangeTargetOrdinal,
                                                         int    latestChangeActionOrdinal,
                                                         String classificationName,
                                                         String attachmentGUID,
                                                         String attachmentTypeName,
                                                         String relationshipTypeName,
                                                         String userId,
                                                         String actionDescription,
                                                         String methodName) throws InvalidParameterException
    {
        InstanceProperties properties = null;

        try
        {
            properties = repositoryHelper.addEnumPropertyToInstance(serviceName,
                                                                    null,
                                                                    OpenMetadataAPIMapper.CHANGE_TARGET_PROPERTY_NAME,
                                                                    OpenMetadataAPIMapper.LATEST_CHANGE_TARGET_ENUM_TYPE_GUID,
                                                                    OpenMetadataAPIMapper.LATEST_CHANGE_TARGET_ENUM_TYPE_NAME,
                                                                    latestChangeTargetOrdinal,
                                                                    methodName);
        }
        catch (TypeErrorException error)
        {
            errorHandler.handleUnsupportedType(error, methodName, OpenMetadataAPIMapper.LATEST_CHANGE_TARGET_ENUM_TYPE_NAME);
        }

        try
        {
            properties = repositoryHelper.addEnumPropertyToInstance(serviceName,
                                                                    properties,
                                                                    OpenMetadataAPIMapper.CHANGE_ACTION_PROPERTY_NAME,
                                                                    OpenMetadataAPIMapper.LATEST_CHANGE_ACTION_ENUM_TYPE_GUID,
                                                                    OpenMetadataAPIMapper.LATEST_CHANGE_ACTION_ENUM_TYPE_NAME,
                                                                    latestChangeActionOrdinal,
                                                                    methodName);
        }
        catch (TypeErrorException error)
        {
            errorHandler.handleUnsupportedType(error, methodName, OpenMetadataAPIMapper.LATEST_CHANGE_ACTION_ENUM_TYPE_NAME);
        }

        if (classificationName != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                           properties,
                                                                           OpenMetadataAPIMapper.CLASSIFICATION_NAME_PROPERTY_NAME,
                                                                           classificationName,
                                                                           methodName);
        }

        if (attachmentGUID != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                   properties,
                                                                   OpenMetadataAPIMapper.ATTACHMENT_GUID_PROPERTY_NAME,
                                                                   attachmentGUID,
                                                                   methodName);
        }

        if (attachmentTypeName != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.ATTACHMENT_TYPE_PROPERTY_NAME,
                                                                      attachmentTypeName,
                                                                      methodName);
        }

        if (userId != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.USER_PROPERTY_NAME,
                                                                      userId,
                                                                      methodName);
        }

        if (relationshipTypeName != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.RELATIONSHIP_TYPE_PROPERTY_NAME,
                                                                      relationshipTypeName,
                                                                      methodName);
        }

        if (actionDescription != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.ACTION_DESCRIPTION_PROPERTY_NAME,
                                                                      actionDescription,
                                                                      methodName);
        }

        return properties;
    }


    /**
     * Set up any properties that are present in the template.  These will be overridden by any properties passed on the constructor of the builder
     * when getInstanceProperties is called.
     *
     * @param templateProperties properties from template
     */
    void setTemplateProperties(InstanceProperties templateProperties)
    {
        this.templateProperties = templateProperties;
    }


    /**
     * Return the bean's type name
     *
     * @return string name
     */
    public String getTypeName()
    {
        return typeName;
    }


    /**
     * Return the bean's type id
     *
     * @return string id
     */
    public String getTypeGUID()
    {
        return typeGUID;
    }


    /**
     * Return the status that this entity should be created with.
     *
     * @return instance status enum
     */
    public InstanceStatus getInstanceStatus()
    {
        return instanceStatus;
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
        InstanceProperties properties = templateProperties;

        if (extendedProperties != null)
        {
            try
            {
                properties = repositoryHelper.addPropertyMapToInstance(serviceName,
                                                                       properties,
                                                                       extendedProperties,
                                                                       methodName);
            }
            catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException error)
            {
                final String  propertyName = "extendedProperties";

                errorHandler.handleUnsupportedProperty(error, methodName, propertyName);
            }
        }

        return setEffectivityDates(properties, effectiveFrom, effectiveTo);
    }


    /**
     * Return the properties based on the parameters supplied.  This is typically used for
     * relationship and classification properties.
     *
     * @param propertyMap map of property names to values
     * @param effectiveFrom date to make the element active in the governance program (null for now)
     * @param effectiveTo date to remove the element from the governance program (null = until deleted)
     * @return repository services properties
     * @throws InvalidParameterException problem mapping properties
     */
    public InstanceProperties getInstanceProperties(Map<String, InstancePropertyValue> propertyMap,
                                                    Date                               effectiveFrom,
                                                    Date                               effectiveTo) throws InvalidParameterException
    {
        InstanceProperties properties = null;

        if ((propertyMap != null) && (! propertyMap.isEmpty()))
        {
            properties = new InstanceProperties();

            properties.setInstanceProperties(propertyMap);
        }

        return setEffectivityDates(properties, effectiveFrom, effectiveTo);
    }


    /**
     * Add the supplied properties to the supplied instance properties object.
     *
     * @param properties current accumulated properties
     * @param propertyMap map of property names to values
     * @param methodName calling method
     * @return repository services properties
     * @throws InvalidParameterException problem mapping properties
     */
    protected InstanceProperties updateInstanceProperties(InstanceProperties  properties,
                                                          Map<String, Object> propertyMap,
                                                          String              methodName) throws InvalidParameterException
    {
        if (propertyMap != null)
        {
            try
            {
                properties = repositoryHelper.addPropertyMapToInstance(serviceName,
                                                                       properties,
                                                                       propertyMap,
                                                                       methodName);
            }
            catch (OCFCheckedExceptionBase error)
            {
                final String propertyName = "properties";

                throw new InvalidParameterException(error, propertyName);
            }
        }

        return properties;
    }



    /**
     * Add the supplied properties to the supplied instance properties object.
     *
     * @param properties current accumulated properties
     * @param propertyMap map of property names to values
     * @return repository services properties
     * @throws InvalidParameterException problem mapping properties
     */
    protected InstanceProperties updateInstanceProperties(InstanceProperties                 properties,
                                                          Map<String, InstancePropertyValue> propertyMap) throws InvalidParameterException
    {
        if (propertyMap != null)
        {
            try
            {
                if (properties == null)
                {
                    return getInstanceProperties(propertyMap, null, null);
                }

                Map<String, InstancePropertyValue> existingProperties = properties.getInstanceProperties();

                if (existingProperties == null)
                {
                    properties.setInstanceProperties(propertyMap);
                }
                else
                {
                    existingProperties.putAll(propertyMap);

                    properties.setInstanceProperties(existingProperties);
                }
            }
            catch (OCFCheckedExceptionBase error)
            {
                final String propertyName = "properties";

                throw new InvalidParameterException(error, propertyName);
            }
        }

        return properties;
    }


    /**
     * Set the supplied effectivity dates into the instance properties.
     *
     * @param properties current accumulated properties
     * @param effectiveFrom date to make the element active in the governance program (null for now)
     * @param effectiveTo date to remove the element from the governance program (null = until deleted)
     * @return augmented instance properties
     */
    InstanceProperties setEffectivityDates(InstanceProperties properties,
                                           Date effectiveFrom,
                                           Date effectiveTo)
    {
        if ((effectiveFrom != null) || (effectiveTo != null))
        {
            if (properties == null)
            {
                properties = new InstanceProperties();
            }

            properties.setEffectiveFromTime(effectiveFrom);
            properties.setEffectiveToTime(effectiveTo);
        }

        return properties;
    }


    /**
     * Set the supplied effectivity dates into the instance properties.
     *
     * @param properties current accumulated properties
     * @return augmented instance properties
     */
    protected InstanceProperties setEffectivityDates(InstanceProperties properties)
    {
        if ((effectiveFrom != null) || (effectiveTo != null))
        {
            if (properties == null)
            {
                properties = new InstanceProperties();
            }

            properties.setEffectiveFromTime(effectiveFrom);
            properties.setEffectiveToTime(effectiveTo);
        }

        return properties;
    }


    /**
     * Return a list of entity classifications that can be stored in the metadata
     * repository.
     *
     * @return list of entity classification objects
     */
    public List<Classification> getEntityClassifications()
    {
        List<Classification> entityClassifications = new ArrayList<>();

        if ((newClassifications != null) && (! newClassifications.isEmpty()))
        {
            entityClassifications = new ArrayList<>(newClassifications.values());
        }

        /*
         * Retrieve the classifications provided by the caller.  Add them to the list unless they have been overridden.
         */
        if ((existingClassifications != null) && (! existingClassifications.isEmpty()))
        {
            for (Classification classification : entityClassifications)
            {
                if ((classification != null) && (newClassifications.get(classification.getName()) == null))
                {
                    entityClassifications.add(classification);
                }
            }
        }

        if (entityClassifications.isEmpty())
        {
            return null;
        }

        return entityClassifications;
    }


    /**
     * Retrieve the requested classification.
     *
     * @param classificationName name of the classification to retrieve
     * @param methodName calling method
     * @return null if the classification is not found for an OMRS Classification bean for the named
     * classification
     * @throws InvalidParameterException the classification name parameter is null
     */
    private Classification getEntityClassification(String classificationName,
                                                   String methodName) throws InvalidParameterException
    {
        if (classificationName == null)
        {
            final String parameterName = "classificationName";

            errorHandler.handleUnsupportedParameter(methodName, parameterName, null);
            return null;
        }

        Classification result = newClassifications.get(classificationName);

        if ((result == null) && (existingClassifications != null))
        {
            for (Classification classification : existingClassifications)
            {
                if (classification != null)
                {
                    if (classificationName.equals(classification.getName()))
                    {
                        return classification;
                    }
                }
            }
        }

        return result;
    }




    /**
     * Retrieve the requested classification and convert it to the OMRS format.
     *
     * @param elementClassificationName name of the classification to retrieve/convert
     * @param methodName calling method
     * @return null if the classification is not found for an OMRS Classification bean for the named
     * classification
     * @throws InvalidParameterException the classification name parameter is null
     */
    public InstanceAuditHeader getExistingEntityClassificationHeader(String elementClassificationName,
                                                                     String methodName) throws InvalidParameterException
    {
        return this.getEntityClassification(elementClassificationName, methodName);
    }


    /**
     * Retrieve the requested classification and convert it to the OMRS format.
     *
     * @param elementClassificationName name of the classification to retrieve/convert
     * @param methodName calling method
     * @return null or the appropriate properties
     * @throws InvalidParameterException the classification name parameter is null
     */
    public InstanceProperties getEntityClassificationProperties(String elementClassificationName,
                                                                String methodName) throws InvalidParameterException
    {
        Classification entityClassification = this.getEntityClassification(elementClassificationName, methodName);

        if (entityClassification != null)
        {
            return entityClassification.getProperties();
        }

        return null;
    }
}
