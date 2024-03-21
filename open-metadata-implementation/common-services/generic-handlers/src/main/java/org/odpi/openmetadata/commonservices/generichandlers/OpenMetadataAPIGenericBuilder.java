/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;
import org.odpi.openmetadata.commonservices.generichandlers.ffdc.GenericHandlersErrorCode;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryErrorHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;

import java.util.*;
import java.util.regex.Pattern;

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

    protected InstanceProperties          templateProperties = null;


    /**
     * Constructor for working with classifications.
     *
     * @param typeGUID type GUID to use for the entity
     * @param typeName type name to use for the entity
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    public OpenMetadataAPIGenericBuilder(String typeGUID,
                                         String typeName,
                                         OMRSRepositoryHelper repositoryHelper,
                                         String serviceName,
                                         String serverName)
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
     * @param extendedProperties properties for the sub ype (if any)
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
     * @param extendedProperties properties for the subtype (if any)
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
            this.typeName = OpenMetadataType.REFERENCEABLE.typeName;
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
     * Return whether a particular classification has been set up by the caller.
     *
     * @param classificationName name of the classification to test for
     * @return boolean
     */
    public boolean isClassificationSet(String classificationName)
    {
        return newClassifications.get(classificationName) != null;
    }


    /**
     * Set up the list of classifications from a template entity.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param templateClassifications list of classifications from the template
     * @param placeholderProperties map of placeholder names to placeholder values to substitute into the template properties
     * @param methodName calling method
     * @throws InvalidParameterException the type of one of the classifications is not supported
     */
    public void setTemplateClassifications(String                userId,
                                           String                externalSourceGUID,
                                           String                externalSourceName,
                                           List<Classification>  templateClassifications,
                                           Map<String, String>   placeholderProperties,
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
                /*
                 * The anchor classification from the template is skipped because the new entity may have a
                 * different anchor.  The anchor classification is therefore set up explicitly. The Template
                 * classification is also skipped because hte new element will not be a template because the
                 * placeholders have been resolved.
                 */
                if ((templateClassification != null) &&
                        (! OpenMetadataType.ANCHORS_CLASSIFICATION.typeName.equals(templateClassification.getName())) &&
                        (! OpenMetadataType.TEMPLATE_CLASSIFICATION.typeName.equals(templateClassification.getName())))
                {
                    try
                    {
                        Classification classification;

                        InstanceProperties classificationProperties = templateClassification.getProperties();

                        if (classificationProperties != null)
                        {
                            classificationProperties = replaceStringPropertiesWithPlaceholders(classificationProperties,
                                                                                               placeholderProperties);
                        }

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
                                                                                   classificationProperties);
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
                                                                                   classificationProperties);
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
     * Set up the "TemplateSubstitute" classification for this entity.  This is used when a new entity is being created, and it is known to be
     * created as a template substitute.
     *
     * @param userId calling user
     * @param methodName calling method
     * @throws PropertyServerException a null anchors GUID has been supplied
     */
    public void setTemplateSubstitute(String userId,
                                      String methodName) throws PropertyServerException
    {
        try
        {
            Classification classification = repositoryHelper.getNewClassification(serviceName,
                                                                                  null,
                                                                                  null,
                                                                                  InstanceProvenanceType.LOCAL_COHORT,
                                                                                  userId,
                                                                                  OpenMetadataType.TEMPLATE_SUBSTITUTE_CLASSIFICATION.typeName,
                                                                                  typeName,
                                                                                  ClassificationOrigin.ASSIGNED,
                                                                                  null,
                                                                                  null);
            newClassifications.put(classification.getName(), classification);
        }
        catch (Exception error)
        {
            errorHandler.handleUnsupportedAnchorsType(error, methodName, OpenMetadataType.TEMPLATE_SUBSTITUTE_CLASSIFICATION.typeName);
        }
    }


    /**
     * Set up the "Anchors" classification for this entity.  This is used when a new entity is being created, and it is known to be
     * connected to a specific anchor.
     *
     * @param userId calling user
     * @param anchorGUID unique identifier of the anchor entity that this entity is linked to directly or indirectly
     * @param anchorTypeName unique identifier of the anchor entity's type
     * @param methodName calling method
     * @throws PropertyServerException a null anchors GUID has been supplied
     */
    public void setAnchors(String userId,
                           String anchorGUID,
                           String anchorTypeName,
                           String methodName) throws PropertyServerException
    {
        final String localMethodName = "setAnchors";

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
                                                                                  OpenMetadataType.ANCHORS_CLASSIFICATION.typeName,
                                                                                  typeName,
                                                                                  ClassificationOrigin.ASSIGNED,
                                                                                  null,
                                                                                  getAnchorsProperties(anchorGUID, anchorTypeName, methodName));
            newClassifications.put(classification.getName(), classification);
        }
        catch (Exception error)
        {
            errorHandler.handleUnsupportedAnchorsType(error, methodName, OpenMetadataType.ANCHORS_CLASSIFICATION.typeName);
        }
    }


    /**
     * Return the Anchors properties in an InstanceProperties object.
     *
     * @param anchorGUID unique identifier of the anchor entity that this entity is linked to directly or indirectly
     * @param anchorTypeName unique identifier of the anchor entity's type
     * @param methodName name of the calling method
     * @return InstanceProperties object
     */
    InstanceProperties getAnchorsProperties(String anchorGUID,
                                            String anchorTypeName,
                                            String methodName)
    {
        InstanceProperties properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                     null,
                                                                                     OpenMetadataProperty.ANCHOR_GUID.name,
                                                                                     anchorGUID,
                                                                                     methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.ANCHOR_TYPE_NAME.name,
                                                                  anchorTypeName,
                                                                  methodName);

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
     * Set up any properties that are present in the template.  These will be overridden by any properties passed
     * on the constructor of the builder when getInstanceProperties is called.
     *
     * @param templateProperties properties from template
     * @param placeholderProperties map of placeholder names to placeholder values to substitute into the template
     *                              properties
     */
    void setTemplateProperties(InstanceProperties  templateProperties,
                               Map<String, String> placeholderProperties)
    {
        this.templateProperties = this.replaceStringPropertiesWithPlaceholders(templateProperties, placeholderProperties);
    }


    /**
     * Resolve placeholder in the properties that are present in the template.  These will be overridden by any properties passed
     * on the constructor of the builder when getInstanceProperties is called.
     *
     * @param templateProperties properties from template
     * @param placeholderProperties map of placeholder names to placeholder values to substitute into the template
     *                              properties
     * @return updated instance properties
     */
    InstanceProperties replaceStringPropertiesWithPlaceholders(InstanceProperties  templateProperties,
                                                               Map<String, String> placeholderProperties)
    {
        final String methodName = "replaceStringPropertiesWithPlaceholders";

        if (templateProperties != null)
        {

            if ((templateProperties.getPropertyCount() == 0) ||
                    (placeholderProperties == null) ||
                    (placeholderProperties.isEmpty()))
            {
                return new InstanceProperties(templateProperties);
            }
            else
            {
                InstanceProperties newTemplateProperties = new InstanceProperties(templateProperties);

                Map<String, InstancePropertyValue> instancePropertyValueMap = templateProperties.getInstanceProperties();
                for (String propertyName : instancePropertyValueMap.keySet())
                {
                    InstancePropertyValue instancePropertyValue = newTemplateProperties.getPropertyValue(propertyName);

                    if (instancePropertyValue instanceof PrimitivePropertyValue primitivePropertyValue)
                    {
                        if (primitivePropertyValue.getPrimitiveDefCategory() == PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING)
                        {
                            repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                         newTemplateProperties,
                                                                         propertyName,
                                                                         replacePrimitiveStringWithPlaceholders(primitivePropertyValue,
                                                                                                                placeholderProperties),
                                                                         methodName);
                        }
                    }
                    else if (instancePropertyValue instanceof ArrayPropertyValue arrayPropertyValue)
                    {
                        arrayPropertyValue.setArrayValues(this.replaceStringPropertiesWithPlaceholders(arrayPropertyValue.getArrayValues(),
                                                                                                       placeholderProperties));

                        newTemplateProperties.setProperty(propertyName, arrayPropertyValue);
                    }
                    else if (instancePropertyValue instanceof MapPropertyValue mapPropertyValue)
                    {
                        mapPropertyValue.setMapValues(this.replaceStringPropertiesWithPlaceholders(mapPropertyValue.getMapValues(),
                                                                                                   placeholderProperties));
                        newTemplateProperties.setProperty(propertyName, mapPropertyValue);
                    }
                }

                return newTemplateProperties;
            }
        }

        return null;
    }


    /**
     * Replace any placeholders found in the string property with the supplied values.
     *
     * @param primitivePropertyValue string property from the template
     * @param placeholderProperties map of placeholder names to placeholder values to substitute into the template
     *                              properties
     * @return updated property
     */
    private String replacePrimitiveStringWithPlaceholders(PrimitivePropertyValue primitivePropertyValue,
                                                          Map<String, String>    placeholderProperties)
    {
        String propertyValue = primitivePropertyValue.valueAsString();

        if ((propertyValue == null) || (! propertyValue.contains("{{")))
        {
            /*
             * No placeholders in property.
             */
            return propertyValue;
        }

        if ((placeholderProperties != null) && (! placeholderProperties.isEmpty()))
        {
            for (String placeholderName : placeholderProperties.keySet())
            {
                String placeholderMatchString = "{{"+ placeholderName + "}}";

                if (propertyValue.equals(placeholderMatchString))
                {
                    propertyValue = placeholderProperties.get(placeholderName);
                }
                else
                {
                    String regExMatchString = Pattern.quote(placeholderMatchString);
                    String[] configBits = propertyValue.split(regExMatchString);

                    if (configBits.length == 1)
                    {
                        if (! propertyValue.equals(configBits[0]))
                        {
                            propertyValue = configBits[0] + placeholderProperties.get(placeholderName);
                        }
                    }
                    else if (configBits.length > 1)
                    {
                        StringBuilder newConfigString = new StringBuilder();
                        boolean       firstPart       = true;

                        for (String configBit : configBits)
                        {
                            if (! firstPart)
                            {
                                newConfigString.append(placeholderProperties.get(placeholderName));
                            }

                            firstPart = false;

                            if (configBit != null)
                            {
                                newConfigString.append(configBit);
                            }
                        }

                        if (propertyValue.endsWith(placeholderMatchString))
                        {
                            newConfigString.append(placeholderProperties.get(placeholderName));
                        }

                        propertyValue = newConfigString.toString();
                    }
                }
            }
        }

        return propertyValue;
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
}
