/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.localrepository.repositorycontentmanager;


import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

/**
 * OMRSRepositoryContentHelper provides methods to repository connectors and repository event mappers to help
 * them build valid type definitions (TypeDefs), entities and relationships.  It is a facade to the
 * repository content manager which holds an in memory cache of all the active TypeDefs in the local server.
 * OMRSRepositoryContentHelper's purpose is to create an object that the repository connectors and event mappers can
 * create, use and discard without needing to know how to connect to the repository content manager.
 */
public class OMRSRepositoryContentHelper implements OMRSRepositoryHelper
{
    private static final Logger log = LoggerFactory.getLogger(OMRSRepositoryContentHelper.class);

    private OMRSRepositoryContentManager repositoryContentManager;


    /**
     * Creates a repository helper linked to the supplied repository content manager.
     *
     * @param repositoryContentManager object associated with the local repository.
     */
    public OMRSRepositoryContentHelper(OMRSRepositoryContentManager repositoryContentManager)
    {
        this.repositoryContentManager = repositoryContentManager;
    }


    /**
     * Return the list of typedefs active in the local repository.
     *
     * @return TypeDef gallery
     */
    public TypeDefGallery getActiveTypeDefGallery()
    {
        final String methodName = "getActiveTypeDefGallery";

        validateRepositoryContentManager(methodName);

        return repositoryContentManager.getActiveTypeDefGallery();
    }


    /**
     * Return the list of typedefs known by the local repository.
     *
     * @return TypeDef gallery
     */
    public TypeDefGallery getKnownTypeDefGallery()
    {
        final String methodName = "getKnownTypeDefGallery";

        validateRepositoryContentManager(methodName);

        return repositoryContentManager.getKnownTypeDefGallery();
    }


    /**
     * Return the TypeDef identified by the name supplied by the caller.  This is used in the connectors when
     * validating the actual types of the repository with the known open metadata types. It is looking specifically
     * for types of the same name but with different content.
     *
     * @param sourceName  source of the request (used for logging)
     * @param typeDefName unique name for the TypeDef
     * @return TypeDef object or null if TypeDef is not known.
     */
    public TypeDef getTypeDefByName(String sourceName,
                                    String typeDefName)
    {
        final String methodName = "getTypeDefByName";

        validateRepositoryContentManager(methodName);

        return repositoryContentManager.getTypeDefByName(sourceName, typeDefName);
    }


    /**
     * Return the AttributeTypeDef identified by the name supplied by the caller.  This is used in the connectors when
     * validating the actual types of the repository with the known open metadata types.  It is looking specifically
     * for types of the same name but with different content.
     *
     * @param sourceName           source of the request (used for logging)
     * @param attributeTypeDefName unique name for the TypeDef
     * @return AttributeTypeDef object or null if AttributeTypeDef is not known.
     */
    public AttributeTypeDef getAttributeTypeDefByName(String sourceName,
                                                      String attributeTypeDefName)
    {
        final String methodName = "getAttributeTypeDefByName";

        validateRepositoryContentManager(methodName);

        return repositoryContentManager.getAttributeTypeDefByName(sourceName, attributeTypeDefName);
    }


    /**
     * Return the TypeDefs identified by the name supplied by the caller.  The TypeDef name may have wild
     * card characters in it which is why the results are returned in a list.
     *
     * @param sourceName  source of the request (used for logging)
     * @param typeDefName unique name for the TypeDef
     * @return TypeDef object or null if TypeDef is not known.
     */
    public TypeDefGallery getActiveTypesByWildCardName(String sourceName,
                                                       String typeDefName)
    {
        final String methodName = "getActiveTypesByWildCardName";

        validateRepositoryContentManager(methodName);

        return repositoryContentManager.getActiveTypesByWildCardName(sourceName, typeDefName);
    }


    /**
     * Return the TypeDef identified by the guid supplied by the caller.  This call is used when
     * retrieving a type that only the guid is known.
     *
     * @param sourceName  source of the request (used for logging)
     * @param parameterName name of guid parameter
     * @param typeDefGUID unique identifier for the TypeDef
     * @param methodName calling method
     * @return TypeDef object
     * @throws TypeErrorException unknown or invalid type
     */
    public TypeDef getTypeDef(String sourceName,
                              String parameterName,
                              String typeDefGUID,
                              String methodName) throws TypeErrorException
    {
        validateRepositoryContentManager(methodName);

        return repositoryContentManager.getTypeDef(sourceName, parameterName, typeDefGUID, methodName);
    }


    /**
     * Return the AttributeTypeDef identified by the guid and name supplied by the caller.  This call is used when
     * retrieving a type that only the guid is known.
     *
     * @param sourceName           source of the request (used for logging)
     * @param attributeTypeDefGUID unique identifier for the AttributeTypeDef
     * @return TypeDef object
     * @throws TypeErrorException unknown or invalid type
     */
    public AttributeTypeDef getAttributeTypeDef(String sourceName,
                                                String attributeTypeDefGUID,
                                                String methodName) throws TypeErrorException
    {
        validateRepositoryContentManager(methodName);

        return repositoryContentManager.getAttributeTypeDef(sourceName, attributeTypeDefGUID, methodName);
    }


    /**
     * Return the TypeDef identified by the guid and name supplied by the caller.  This call is used when
     * retrieving a type that should exist.  For example, retrieving the type of a metadata instance.
     *
     * @param sourceName  source of the request (used for logging)
     * @param guidParameterName name of guid parameter
     * @param nameParameterName name of type name parameter
     * @param typeDefGUID unique identifier for the TypeDef
     * @param typeDefName unique name for the TypeDef
     * @param methodName  calling method
     * @return TypeDef object
     * @throws TypeErrorException unknown or invalid type
     */
    public TypeDef getTypeDef(String sourceName,
                              String guidParameterName,
                              String nameParameterName,
                              String typeDefGUID,
                              String typeDefName,
                              String methodName) throws TypeErrorException
    {
        validateRepositoryContentManager(methodName);

        return repositoryContentManager.getTypeDef(sourceName,
                                                   guidParameterName,
                                                   nameParameterName,
                                                   typeDefGUID,
                                                   typeDefName,
                                                   methodName);
    }


    /**
     * Return the AttributeTypeDef identified by the guid and name supplied by the caller.  This call is used when
     * retrieving a type that should exist.  For example, retrieving the type definition of a metadata instance's
     * property.
     *
     * @param sourceName           source of the request (used for logging)
     * @param attributeTypeDefGUID unique identifier for the AttributeTypeDef
     * @param attributeTypeDefName unique name for the AttributeTypeDef
     * @param methodName calling method
     * @return TypeDef object
     * @throws TypeErrorException unknown or invalid type
     */
    public AttributeTypeDef getAttributeTypeDef(String sourceName,
                                                String attributeTypeDefGUID,
                                                String attributeTypeDefName,
                                                String methodName) throws TypeErrorException
    {
        validateRepositoryContentManager(methodName);

        return repositoryContentManager.getAttributeTypeDef(sourceName,
                                                            attributeTypeDefGUID,
                                                            attributeTypeDefName,
                                                            methodName);
    }


    /**
     * Returns an updated TypeDef that has had the supplied patch applied.  It throws an exception if any part of
     * the patch is incompatible with the original TypeDef.  For example, if there is a mismatch between
     * the type or version that either represents.
     *
     * @param sourceName      source of the TypeDef (used for logging)
     * @param typeDefPatch    patch to apply
     * @param originalTypeDef typeDef to patch
     * @return updated TypeDef
     * @throws PatchErrorException       the patch is either badly formatted, or does not apply to the supplied TypeDef
     * @throws InvalidParameterException the TypeDefPatch is null.
     */
    public TypeDef applyPatch(String       sourceName,
                              TypeDef      originalTypeDef,
                              TypeDefPatch typeDefPatch) throws PatchErrorException,
                                                                InvalidParameterException
    {
        final String  methodName = "applyPatch";

        validateRepositoryContentManager(methodName);

        TypeDef clonedTypeDef  = null;
        TypeDef updatedTypeDef = null;

        /*
         * Begin with simple validation of the typeDef patch.
         */
        if (typeDefPatch != null)
        {
            // TODO invalid parameter exception
        }

        long newVersion = typeDefPatch.getUpdateToVersion();
        if (newVersion <= typeDefPatch.getApplyToVersion())
        {
            // TODO PatchError
        }

        TypeDefPatchAction patchAction = typeDefPatch.getAction();
        if (patchAction == null)
        {
            // TODO patch error
        }


        /*
         * Is the version compatible?
         */
        if (originalTypeDef.getVersion() != typeDefPatch.getApplyToVersion())
        {
            // TODO throw PatchException incompatible versions
        }

        /*
         * OK to perform the update.  Need to create a new TypeDef object.  TypeDef is an abstract class
         * so need to use the TypeDefCategory to create a new object of the correct type.
         */
        TypeDefCategory category = originalTypeDef.getCategory();
        if (category == null)
        {
            // TODO Throw PatchError as base type is messed up
        }

        try
        {
            switch (category)
            {
                case ENTITY_DEF:
                    clonedTypeDef = new EntityDef((EntityDef) originalTypeDef);
                    break;

                case RELATIONSHIP_DEF:
                    clonedTypeDef = new RelationshipDef((RelationshipDef) originalTypeDef);
                    break;

                case CLASSIFICATION_DEF:
                    clonedTypeDef = new ClassificationDef((ClassificationDef) originalTypeDef);
                    break;
            }
        }
        catch (ClassCastException castError)
        {
            // TODO Throw PatchError as base type is messed up
        }

        /*
         * Now we have a new TypeDef, just need to make the changes.  The Action
         */
        if (clonedTypeDef != null)
        {
            switch (patchAction)
            {
                case ADD_ATTRIBUTES:
                    updatedTypeDef = this.patchTypeDefAttributes(clonedTypeDef, typeDefPatch.getTypeDefAttributes());
                    break;

                case ADD_OPTIONS:
                    updatedTypeDef = this.patchTypeDefNewOptions(clonedTypeDef, typeDefPatch.getTypeDefOptions());
                    break;

                case UPDATE_OPTIONS:
                    updatedTypeDef = this.patchTypeDefUpdateOptions(clonedTypeDef, typeDefPatch.getTypeDefOptions());
                    break;

                case DELETE_OPTIONS:
                    updatedTypeDef = this.patchTypeDefDeleteOptions(clonedTypeDef, typeDefPatch.getTypeDefOptions());
                    break;

                case ADD_EXTERNAL_STANDARDS:
                    updatedTypeDef = this.patchTypeDefAddExternalStandards(clonedTypeDef,
                                                                           typeDefPatch.getExternalStandardMappings(),
                                                                           typeDefPatch.getTypeDefAttributes());
                    break;

                case UPDATE_EXTERNAL_STANDARDS:
                    updatedTypeDef = this.patchTypeDefUpdateExternalStandards(clonedTypeDef,
                                                                              typeDefPatch.getExternalStandardMappings(),
                                                                              typeDefPatch.getTypeDefAttributes());
                    break;

                case DELETE_EXTERNAL_STANDARDS:
                    updatedTypeDef = this.patchTypeDefDeleteExternalStandards(clonedTypeDef,
                                                                              typeDefPatch.getExternalStandardMappings(),
                                                                              typeDefPatch.getTypeDefAttributes());
                    break;

                case UPDATE_DESCRIPTIONS:
                    updatedTypeDef = this.patchTypeDefNewDescriptions(clonedTypeDef,
                                                                      typeDefPatch.getDescription(),
                                                                      typeDefPatch.getDescriptionGUID(),
                                                                      typeDefPatch.getTypeDefAttributes());
                    break;
            }
        }


        if (updatedTypeDef != null)
        {
            updatedTypeDef.setVersion(typeDefPatch.getUpdateToVersion());
            updatedTypeDef.setVersionName(typeDefPatch.getNewVersionName());
        }

        return updatedTypeDef;
    }


    /**
     * Add the supplied attributes to the properties definition for the cloned typedef.
     *
     * @param clonedTypeDef     TypeDef object to update
     * @param typeDefAttributes new attributes to add.
     * @return updated TypeDef
     * @throws PatchErrorException problem adding attributes
     */
    private TypeDef patchTypeDefAttributes(TypeDef clonedTypeDef,
                                           List<TypeDefAttribute> typeDefAttributes) throws PatchErrorException
    {
        List<TypeDefAttribute> propertyDefinitions = clonedTypeDef.getPropertiesDefinition();

        if (propertyDefinitions == null)
        {
            propertyDefinitions = new ArrayList<>();
        }

        for (TypeDefAttribute newAttribute : typeDefAttributes)
        {
            if (newAttribute != null)
            {
                String           attributeName = newAttribute.getAttributeName();
                AttributeTypeDef attributeType = newAttribute.getAttributeType();

                if ((attributeName != null) && (attributeType != null))
                {
                    if (propertyDefinitions.contains(newAttribute))
                    {
                        // TODO Patch error as Duplicate Attribute
                    }
                    else
                    {
                        propertyDefinitions.add(newAttribute);
                    }
                }
                else
                {
                    // TODO Patch Error as Invalid Attribute in patch
                }
            }
        }

        if (propertyDefinitions.size() > 0)
        {
            clonedTypeDef.setPropertiesDefinition(propertyDefinitions);
        }
        else
        {
            clonedTypeDef.setPropertiesDefinition(null);
        }

        return clonedTypeDef;
    }


    /**
     * @param clonedTypeDef  TypeDef object to update
     * @param typeDefOptions new options to add
     * @return updated TypeDef
     * @throws PatchErrorException problem adding options
     */
    private TypeDef patchTypeDefNewOptions(TypeDef clonedTypeDef,
                                           Map<String, String> typeDefOptions) throws PatchErrorException
    {
        // TODO
        return null;
    }


    /**
     * @param clonedTypeDef  TypeDef object to update
     * @param typeDefOptions options to update
     * @return updated TypeDef
     * @throws PatchErrorException problem updating options
     */
    private TypeDef patchTypeDefUpdateOptions(TypeDef clonedTypeDef,
                                              Map<String, String> typeDefOptions) throws PatchErrorException
    {
        // TODO
        return null;
    }


    /**
     * @param clonedTypeDef  TypeDef object to update
     * @param typeDefOptions options to delete
     * @return updated TypeDef
     * @throws PatchErrorException problem deleting options
     */
    private TypeDef patchTypeDefDeleteOptions(TypeDef clonedTypeDef,
                                              Map<String, String> typeDefOptions) throws PatchErrorException
    {
        // TODO
        return null;
    }


    /**
     * Add new mappings to external standards to the TypeDef.
     *
     * @param clonedTypeDef            TypeDef object to update
     * @param externalStandardMappings new mappings to add
     * @return updated TypeDef
     * @throws PatchErrorException problem adding mapping(s)
     */
    private TypeDef patchTypeDefAddExternalStandards(TypeDef clonedTypeDef,
                                                     List<ExternalStandardMapping> externalStandardMappings,
                                                     List<TypeDefAttribute> typeDefAttributes) throws PatchErrorException
    {
        // TODO
        return null;
    }


    /**
     * Update the supplied mappings from the TypeDef.
     *
     * @param clonedTypeDef            TypeDef object to update
     * @param externalStandardMappings mappings to update
     * @return updated TypeDef
     * @throws PatchErrorException problem updating mapping(s)
     */
    private TypeDef patchTypeDefUpdateExternalStandards(TypeDef clonedTypeDef,
                                                        List<ExternalStandardMapping> externalStandardMappings,
                                                        List<TypeDefAttribute> typeDefAttributes) throws PatchErrorException
    {
        // TODO
        return null;
    }


    /**
     * Delete the supplied mappings from the TypeDef.
     *
     * @param clonedTypeDef            TypeDef object to update
     * @param externalStandardMappings list of mappings to delete
     * @return updated TypeDef
     * @throws PatchErrorException problem deleting mapping(s)
     */
    private TypeDef patchTypeDefDeleteExternalStandards(TypeDef clonedTypeDef,
                                                        List<ExternalStandardMapping> externalStandardMappings,
                                                        List<TypeDefAttribute> typeDefAttributes) throws PatchErrorException
    {
        // TODO
        return null;
    }


    /**
     * Update the descriptions for the TypeDef or any of its attributes.  If the description values are null, they are
     * not changes in the TypeDef.  This means there is no way to clear a description, just update it for a better one.
     *
     * @param clonedTypeDef   TypeDef object to update
     * @param description     new description
     * @param descriptionGUID new unique identifier for glossary term that provides detailed description of TypeDef
     * @return updated TypeDef
     * @throws PatchErrorException problem adding new description
     */
    private TypeDef patchTypeDefNewDescriptions(TypeDef clonedTypeDef,
                                                String description,
                                                String descriptionGUID,
                                                List<TypeDefAttribute> typeDefAttributes) throws PatchErrorException
    {
        if (description != null)
        {
            clonedTypeDef.setDescription(description);
        }
        if (descriptionGUID != null)
        {
            clonedTypeDef.setDescriptionGUID(descriptionGUID);
        }

        if (typeDefAttributes != null)
        {
            List<TypeDefAttribute> propertiesDefinition = clonedTypeDef.getPropertiesDefinition();

            if (propertiesDefinition == null)
            {
                // TODO throw patch error as attempting to Patch TypeDef with no properties
            }

            for (TypeDefAttribute patchTypeDefAttribute : typeDefAttributes)
            {
                if (patchTypeDefAttribute != null)
                {
                    String patchTypeDefAttributeName = patchTypeDefAttribute.getAttributeName();

                    if (patchTypeDefAttributeName != null)
                    {
                        for (TypeDefAttribute existingProperty : propertiesDefinition)
                        {
                            if (existingProperty != null)
                            {
                                if (patchTypeDefAttributeName.equals(existingProperty.getAttributeName()))
                                {

                                }
                            }
                            else
                            {
                                // TODO throw Patch Error because basic Type is messed up
                            }
                        }
                    }
                    else
                    {
                        //  TODO throw Patch Error null attribute name
                    }
                }
                else
                {
                    // TODO throw Patch Error null attribute included
                }
            }
        }

        return clonedTypeDef;
    }


    /**
     * Return the names of all of the properties in the supplied TypeDef and all of its super-types.
     *
     * @param sourceName name of caller.
     * @param typeDef TypeDef to query.
     * @param methodName calling method.
     * @return list of property names.
     */
    public List<TypeDefAttribute> getAllPropertiesForTypeDef(String  sourceName,
                                                             TypeDef typeDef,
                                                             String  methodName)
    {
        validateRepositoryContentManager(methodName);

        return repositoryContentManager.getAllPropertiesForTypeDef(sourceName, typeDef, methodName);
    }


    /**
     * Validate that the type of an instance is of the expected/desired type.  The actual instnace may be a subtype
     * of the expected type of course.
     *
     * @param sourceName source of the request (used for logging)
     * @param actualTypeName name of the entity type
     * @param expectedTypeName name of the expected type
     * @return boolean if they match (a null in either results in false)
     */
    public boolean  isTypeOf(String   sourceName,
                             String   actualTypeName,
                             String   expectedTypeName)
    {
        final String  methodName = "isTypeOf";

        validateRepositoryContentManager(methodName);

        return repositoryContentManager.isTypeOf(sourceName, actualTypeName, expectedTypeName);
    }


    /**
     * Match the supplied external standard identifiers against the active types for this repository.
     *
     * @param sourceName source of the request (used for logging)
     * @param standard name of the standard, null means any.
     * @param organization name of the organization, null means any.
     * @param identifier identifier of the element in the standard, null means any.
     * @param methodName method receiving the call
     * @return list of typeDefs
     */
    public  List<TypeDef> getMatchingActiveTypes(String sourceName,
                                                 String standard,
                                                 String organization,
                                                 String identifier,
                                                 String methodName)
    {
        List<TypeDef>  matchingTypes = new ArrayList<>();
        TypeDefGallery typeDefGallery = this.getActiveTypeDefGallery();

        if (typeDefGallery != null)
        {
            for (TypeDef activeTypeDef : typeDefGallery.getTypeDefs())
            {
                /*
                 * Extract all of the external standards mappings from the TypeDef.  They are located in the TypeDef
                 * itself and in the TypeDefAttributes.
                 */
                List<ExternalStandardMapping>  externalStandardMappings = new ArrayList<>();

                if (activeTypeDef.getExternalStandardMappings() != null)
                {
                    externalStandardMappings.addAll(activeTypeDef.getExternalStandardMappings());
                }

                List<TypeDefAttribute>  typeDefAttributes = activeTypeDef.getPropertiesDefinition();

                if (typeDefAttributes != null)
                {
                    for (TypeDefAttribute  typeDefAttribute : typeDefAttributes)
                    {
                        if ((typeDefAttribute != null) && (typeDefAttribute.getExternalStandardMappings() != null))
                        {
                            externalStandardMappings.addAll(activeTypeDef.getExternalStandardMappings());
                        }
                    }
                }

                /*
                 * Look for matching standards
                 */
                for (ExternalStandardMapping externalStandardMapping : externalStandardMappings)
                {
                    String activeTypeDefStandardName = externalStandardMapping.getStandardName();
                    String activeTypeDefStandardOrgName = externalStandardMapping.getStandardOrganization();
                    String activeTypeDefStandardIdentifier = externalStandardMapping.getStandardTypeName();

                    if ((activeTypeDefStandardName != null) && (activeTypeDefStandardName.equals(standard)))
                    {
                        matchingTypes.add(activeTypeDef);
                    }
                    else if ((activeTypeDefStandardOrgName != null) && (activeTypeDefStandardOrgName.equals(organization)))
                    {
                        matchingTypes.add(activeTypeDef);
                    }
                    else if ((activeTypeDefStandardIdentifier != null) && (activeTypeDefStandardIdentifier.equals(identifier)))
                    {
                        matchingTypes.add(activeTypeDef);
                    }
                }
            }
        }

        if (matchingTypes.isEmpty())
        {
            return null;
        }
        else
        {
            return matchingTypes;
        }
    }

    /**
     * Remember the metadata collection name for this metadata collection Id. If the metadata collection id
     * is null, it is ignored.
     *
     * @param metadataCollectionId unique identifier (guid) for the metadata collection.
     * @param metadataCollectionName display name for the metadata collection (can be null).
     */
    public void registerMetadataCollection(String    metadataCollectionId,
                                                        String    metadataCollectionName)
    {
        final String methodName = "registerMetadataCollection";

        validateRepositoryContentManager(methodName);

        repositoryContentManager.registerMetadataCollection(metadataCollectionId, metadataCollectionName);
    }


    /**
     * Return the metadata collection name (or null) for a metadata collection id.
     *
     * @param metadataCollectionId unique identifier (guid) for the metadata collection.
     * @return display name
     */
    public String getMetadataCollectionName(String    metadataCollectionId)
    {
        final String methodName = "getMetadataCollectionName";

        validateRepositoryContentManager(methodName);

        return repositoryContentManager.getMetadataCollectionName(metadataCollectionId);
    }


    /**
     * Return an entity with the header and type information filled out.  The caller only needs to add properties
     * and classifications to complete the set up of the entity.
     *
     * @param sourceName           source of the request (used for logging)
     * @param metadataCollectionId unique identifier for the home metadata collection
     * @param provenanceType       origin of the entity
     * @param userName             name of the creator
     * @param typeName             name of the type
     * @return partially filled out entity needs classifications and properties
     * @throws TypeErrorException the type name is not recognized.
     */
    public EntityDetail getSkeletonEntity(String sourceName,
                                          String metadataCollectionId,
                                          InstanceProvenanceType provenanceType,
                                          String userName,
                                          String typeName) throws TypeErrorException
    {
        final String methodName = "getSkeletonEntity";

        validateRepositoryContentManager(methodName);

        EntityDetail entity = new EntityDetail();
        String       guid   = UUID.randomUUID().toString();

        entity.setInstanceProvenanceType(provenanceType);
        entity.setMetadataCollectionId(metadataCollectionId);
        entity.setCreateTime(new Date());
        entity.setGUID(guid);
        entity.setVersion(1L);

        entity.setType(repositoryContentManager.getInstanceType(sourceName, TypeDefCategory.ENTITY_DEF, typeName, methodName));
        entity.setStatus(repositoryContentManager.getInitialStatus(sourceName, typeName, methodName));
        entity.setCreatedBy(userName);
        entity.setInstanceURL(repositoryContentManager.getEntityURL(sourceName, guid));

        return entity;
    }


    /**
     * Return a classification with the header and type information filled out.  The caller only needs to add properties
     * and possibility origin information if it is propagated to complete the set up of the classification.
     *
     * @param sourceName             source of the request (used for logging)
     * @param userName               name of the creator
     * @param classificationTypeName name of the classification type
     * @param entityTypeName         name of the type for the entity that this classification is to be attached to.
     * @return partially filled out classification needs properties and possibly origin information
     * @throws TypeErrorException the type name is not recognized as a classification type.
     */
    public Classification getSkeletonClassification(String sourceName,
                                                    String userName,
                                                    String classificationTypeName,
                                                    String entityTypeName) throws TypeErrorException
    {
        final String methodName = "getSkeletonClassification";

        validateRepositoryContentManager(methodName);


        if (repositoryContentManager.isValidTypeCategory(sourceName,
                                                         TypeDefCategory.CLASSIFICATION_DEF,
                                                         classificationTypeName,
                                                         methodName))
        {
            if (repositoryContentManager.isValidClassificationForEntity(sourceName,
                                                                        classificationTypeName,
                                                                        entityTypeName,
                                                                        methodName))
            {
                Classification classification = new Classification();

                classification.setName(classificationTypeName);
                classification.setCreateTime(new Date());
                classification.setCreatedBy(userName);
                classification.setVersion(1L);
                classification.setType(repositoryContentManager.getInstanceType(sourceName,
                                                                                TypeDefCategory.CLASSIFICATION_DEF,
                                                                                classificationTypeName,
                                                                                methodName));
                classification.setStatus(repositoryContentManager.getInitialStatus(sourceName,
                                                                                   classificationTypeName,
                                                                                   methodName));

                return classification;
            }
            else
            {
                OMRSErrorCode errorCode = OMRSErrorCode.INVALID_CLASSIFICATION_FOR_ENTITY;
                String errorMessage = errorCode.getErrorMessageId()
                        + errorCode.getFormattedErrorMessage(classificationTypeName, entityTypeName);

                throw new TypeErrorException(errorCode.getHTTPErrorCode(),
                                             this.getClass().getName(),
                                             methodName,
                                             errorMessage,
                                             errorCode.getSystemAction(),
                                             errorCode.getUserAction());
            }
        }
        else
        {
            OMRSErrorCode errorCode = OMRSErrorCode.UNKNOWN_CLASSIFICATION;
            String errorMessage = errorCode.getErrorMessageId()
                                + errorCode.getFormattedErrorMessage(classificationTypeName);

            throw new TypeErrorException(errorCode.getHTTPErrorCode(),
                                         this.getClass().getName(),
                                         methodName,
                                         errorMessage,
                                         errorCode.getSystemAction(),
                                         errorCode.getUserAction());
        }
    }


    /**
     * Return a relationship with the header and type information filled out.  The caller only needs to add properties
     * to complete the set up of the relationship.
     *
     * @param sourceName           source of the request (used for logging)
     * @param metadataCollectionId unique identifier for the home metadata collection
     * @param provenanceType       origin type of the relationship
     * @param userName             name of the creator
     * @param typeName             name of the relationship's type
     * @return partially filled out relationship needs properties
     * @throws TypeErrorException the type name is not recognized as a relationship type.
     */
    public Relationship getSkeletonRelationship(String sourceName,
                                                String metadataCollectionId,
                                                InstanceProvenanceType provenanceType,
                                                String userName,
                                                String typeName) throws TypeErrorException
    {
        final String methodName = "getSkeletonRelationship";

        validateRepositoryContentManager(methodName);

        Relationship relationship = new Relationship();
        String       guid         = UUID.randomUUID().toString();

        relationship.setInstanceProvenanceType(provenanceType);
        relationship.setMetadataCollectionId(metadataCollectionId);
        relationship.setCreateTime(new Date());
        relationship.setGUID(guid);
        relationship.setVersion(1L);

        relationship.setType(repositoryContentManager.getInstanceType(sourceName,
                                                                      TypeDefCategory.RELATIONSHIP_DEF,
                                                                      typeName,
                                                                      methodName));
        relationship.setStatus(repositoryContentManager.getInitialStatus(sourceName, typeName, methodName));
        relationship.setCreatedBy(userName);
        relationship.setInstanceURL(repositoryContentManager.getRelationshipURL(sourceName, guid));

        return relationship;
    }


    /**
     * Return a relationship with the header and type information filled out.  The caller only needs to add properties
     * to complete the set up of the relationship.
     *
     * @param sourceName     source of the request (used for logging)
     * @param typeDefSummary details of the new type
     * @return instance type
     * @throws TypeErrorException the type name is not recognized as a relationship type.
     */
    public InstanceType getNewInstanceType(String sourceName,
                                           TypeDefSummary typeDefSummary) throws TypeErrorException
    {
        final String methodName = "getNewInstanceType";

        validateRepositoryContentManager(methodName);

        return repositoryContentManager.getInstanceType(sourceName,
                                                        typeDefSummary.getCategory(),
                                                        typeDefSummary.getName(),
                                                        methodName);
    }


    /**
     * Return a filled out entity.  It just needs to add the classifications.
     *
     * @param sourceName           source of the request (used for logging)
     * @param metadataCollectionId unique identifier for the home metadata collection
     * @param provenanceType       origin of the entity
     * @param userName             name of the creator
     * @param typeName             name of the type
     * @param properties           properties for the entity
     * @param classifications      list of classifications for the entity
     * @return an entity that is filled out
     * @throws TypeErrorException the type name is not recognized as an entity type
     */
    public EntityDetail getNewEntity(String sourceName,
                                     String metadataCollectionId,
                                     InstanceProvenanceType provenanceType,
                                     String userName,
                                     String typeName,
                                     InstanceProperties properties,
                                     List<Classification> classifications) throws TypeErrorException
    {
        EntityDetail entity = this.getSkeletonEntity(sourceName,
                                                     metadataCollectionId,
                                                     provenanceType,
                                                     userName,
                                                     typeName);

        entity.setProperties(properties);
        entity.setClassifications(classifications);

        return entity;
    }


    /**
     * Return a filled out relationship, caller just needs the entity proxies added.
     *
     * @param sourceName           source of the request (used for logging)
     * @param metadataCollectionId unique identifier for the home metadata collection
     * @param provenanceType       origin of the relationship
     * @param userName             name of the creator
     * @param typeName             name of the type
     * @param properties           properties for the relationship
     * @return a relationship that is filled out
     * @throws TypeErrorException the type name is not recognized as a relationship type
     */
    public Relationship getNewRelationship(String sourceName,
                                           String metadataCollectionId,
                                           InstanceProvenanceType provenanceType,
                                           String userName,
                                           String typeName,
                                           InstanceProperties properties) throws TypeErrorException
    {
        Relationship relationship = this.getSkeletonRelationship(sourceName,
                                                                 metadataCollectionId,
                                                                 provenanceType,
                                                                 userName,
                                                                 typeName);

        relationship.setProperties(properties);

        return relationship;
    }


    /**
     * Return a classification with the header and type information filled out.  The caller only needs to add properties
     * to complete the set up of the classification.
     *
     * @param sourceName     source of the request (used for logging)
     * @param userName       name of the creator
     * @param typeName       name of the type
     * @param entityTypeName name of the type for the entity that this classification is to be attached to.
     * @param properties     properties for the classification
     * @return partially filled out classification needs properties and possibly origin information
     * @throws TypeErrorException the type name is not recognized as a classification type.
     */
    public Classification getNewClassification(String sourceName,
                                               String userName,
                                               String typeName,
                                               String entityTypeName,
                                               ClassificationOrigin classificationOrigin,
                                               String classificationOriginGUID,
                                               InstanceProperties properties) throws TypeErrorException
    {
        Classification classification = this.getSkeletonClassification(sourceName,
                                                                       userName,
                                                                       typeName,
                                                                       entityTypeName);

        classification.setClassificationOrigin(classificationOrigin);
        classification.setClassificationOriginGUID(classificationOriginGUID);
        classification.setProperties(properties);

        return classification;
    }


    /**
     * Add a classification to an existing entity.
     *
     * @param sourceName        source of the request (used for logging)
     * @param entity            entity to update
     * @param newClassification classification to update
     * @param methodName        calling method
     * @return updated entity
     */
    public EntityDetail addClassificationToEntity(String sourceName,
                                                  EntityDetail entity,
                                                  Classification newClassification,
                                                  String methodName)
    {
        EntityDetail updatedEntity = new EntityDetail(entity);

        if (newClassification != null)
        {
            /*
             * Duplicate classifications are not allowed so a hash map is used to remove duplicates.
             */
            Map<String, Classification> entityClassificationsMap = new HashMap<>();
            List<Classification>        entityClassifications    = updatedEntity.getClassifications();

            if (entityClassifications != null)
            {
                for (Classification existingClassification : entityClassifications)
                {
                    if (existingClassification != null)
                    {
                        entityClassificationsMap.put(existingClassification.getName(), existingClassification);
                    }
                }
            }

            entityClassificationsMap.put(newClassification.getName(), newClassification);

            if (entityClassificationsMap.isEmpty())
            {
                updatedEntity.setClassifications(null);
            }
            else
            {
                entityClassifications = new ArrayList<>(entityClassificationsMap.values());

                updatedEntity.setClassifications(entityClassifications);
            }

            return updatedEntity;
        }
        else
        {
            final String thisMethodName = "addClassificationToEntity";

            OMRSErrorCode errorCode = OMRSErrorCode.NULL_CLASSIFICATION_CREATED;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(sourceName,
                                                                                                     thisMethodName,
                                                                                                     methodName);

            throw new OMRSLogicErrorException(errorCode.getHTTPErrorCode(),
                                              this.getClass().getName(),
                                              methodName,
                                              errorMessage,
                                              errorCode.getSystemAction(),
                                              errorCode.getUserAction());
        }
    }


    /**
     * Return the names classification from an existing entity.
     *
     * @param sourceName         source of the request (used for logging)
     * @param entity             entity to update
     * @param classificationName classification to retrieve
     * @param methodName         calling method
     * @return located classification
     * @throws ClassificationErrorException the classification is not attached to the entity
     */
    public Classification getClassificationFromEntity(String sourceName,
                                                      EntityDetail entity,
                                                      String classificationName,
                                                      String methodName) throws ClassificationErrorException
    {
        final String thisMethodName = "getClassificationFromEntity";

        if ((entity == null) || (classificationName == null))
        {
            OMRSErrorCode errorCode = OMRSErrorCode.VALIDATION_LOGIC_ERROR;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(sourceName,
                                                                                                     thisMethodName,
                                                                                                     methodName);

            throw new OMRSLogicErrorException(errorCode.getHTTPErrorCode(),
                                              this.getClass().getName(),
                                              methodName,
                                              errorMessage,
                                              errorCode.getSystemAction(),
                                              errorCode.getUserAction());
        }

        List<Classification> entityClassifications = entity.getClassifications();

        if (entityClassifications != null)
        {
            for (Classification entityClassification : entityClassifications)
            {
                if (classificationName.equals(entityClassification.getName()))
                {
                    return entityClassification;
                }
            }
        }

        OMRSErrorCode errorCode = OMRSErrorCode.ENTITY_NOT_CLASSIFIED;
        String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                                                                                                 sourceName,
                                                                                                 classificationName,
                                                                                                 entity.getGUID());
        throw new ClassificationErrorException(errorCode.getHTTPErrorCode(),
                                               this.getClass().getName(),
                                               methodName,
                                               errorMessage,
                                               errorCode.getSystemAction(),
                                               errorCode.getUserAction());
    }


    /**
     * Replace an existing classification with a new one
     *
     * @param sourceName        source of the request (used for logging)
     * @param userName          name of the editor
     * @param entity            entity to update
     * @param newClassification classification to update
     * @param methodName        calling method
     * @return updated entity
     */
    public EntityDetail updateClassificationInEntity(String sourceName,
                                                     String userName,
                                                     EntityDetail entity,
                                                     Classification newClassification,
                                                     String methodName)
    {
        if (newClassification != null)
        {
            Classification updatedClassification = new Classification(newClassification);

            updatedClassification = incrementVersion(userName, newClassification, updatedClassification);

            return this.addClassificationToEntity(sourceName, entity, updatedClassification, methodName);
        }
        else
        {
            final String thisMethodName = "updateClassificationInEntity";

            OMRSErrorCode errorCode = OMRSErrorCode.NULL_CLASSIFICATION_CREATED;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(sourceName,
                                                                                                     thisMethodName,
                                                                                                     methodName);

            throw new OMRSLogicErrorException(errorCode.getHTTPErrorCode(),
                                              this.getClass().getName(),
                                              methodName,
                                              errorMessage,
                                              errorCode.getSystemAction(),
                                              errorCode.getUserAction());
        }
    }


    /**
     * Return a oldClassification with the header and type information filled out.  The caller only needs to add properties
     * to complete the set up of the oldClassification.
     *
     * @param sourceName            source of the request (used for logging)
     * @param entity                entity to update
     * @param oldClassificationName classification to remove
     * @param methodName            calling method
     * @return updated entity
     * @throws ClassificationErrorException the entity was not classified with this classification
     */
    public EntityDetail deleteClassificationFromEntity(String sourceName,
                                                       EntityDetail entity,
                                                       String oldClassificationName,
                                                       String methodName) throws ClassificationErrorException
    {
        EntityDetail updatedEntity = new EntityDetail(entity);

        if (oldClassificationName != null)
        {
            /*
             * Duplicate classifications are not allowed so a hash map is used to remove duplicates.
             */
            Map<String, Classification> entityClassificationsMap = new HashMap<>();
            List<Classification>        entityClassifications    = updatedEntity.getClassifications();

            if (entityClassifications != null)
            {
                for (Classification existingClassification : entityClassifications)
                {
                    if (existingClassification != null)
                    {
                        entityClassificationsMap.put(existingClassification.getName(), existingClassification);
                    }
                }
            }

            Classification oldClassification = entityClassificationsMap.remove(oldClassificationName);

            if (oldClassification == null)
            {
                OMRSErrorCode errorCode = OMRSErrorCode.ENTITY_NOT_CLASSIFIED;
                String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                                                                                                         sourceName,
                                                                                                         oldClassificationName,
                                                                                                         entity.getGUID());
                throw new ClassificationErrorException(errorCode.getHTTPErrorCode(),
                                                       this.getClass().getName(),
                                                       methodName,
                                                       errorMessage,
                                                       errorCode.getSystemAction(),
                                                       errorCode.getUserAction());
            }

            if (entityClassificationsMap.isEmpty())
            {
                updatedEntity.setClassifications(null);
            }
            else
            {
                entityClassifications = new ArrayList<>(entityClassificationsMap.values());

                updatedEntity.setClassifications(entityClassifications);
            }

            return updatedEntity;
        }
        else
        {
            final String thisMethodName = "deleteClassificationFromEntity";

            OMRSErrorCode errorCode = OMRSErrorCode.NULL_CLASSIFICATION_NAME;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(sourceName,
                                                                                                     thisMethodName,
                                                                                                     methodName);

            throw new OMRSLogicErrorException(errorCode.getHTTPErrorCode(),
                                              this.getClass().getName(),
                                              methodName,
                                              errorMessage,
                                              errorCode.getSystemAction(),
                                              errorCode.getUserAction());
        }
    }


    /**
     * Merge two sets of instance properties.
     *
     * @param sourceName         source of the request (used for logging)
     * @param existingProperties current set of properties
     * @param newProperties      properties to add/update
     * @return merged properties
     */
    public InstanceProperties mergeInstanceProperties(String             sourceName,
                                                      InstanceProperties existingProperties,
                                                      InstanceProperties newProperties)
    {
        InstanceProperties mergedProperties;

        if (existingProperties == null)
        {
            mergedProperties = newProperties;
        }
        else
        {
            mergedProperties = existingProperties;

            if (newProperties != null)
            {
                Iterator<String> newPropertyNames = newProperties.getPropertyNames();

                while (newPropertyNames.hasNext())
                {
                    String newPropertyName = newPropertyNames.next();

                    mergedProperties.setProperty(newPropertyName, newProperties.getPropertyValue(newPropertyName));
                }
            }
        }

        return mergedProperties;
    }


    /**
     * Changes the control information to reflect an update in an instance.
     *
     * @param userId           user making the change.
     * @param originalInstance original instance before the change
     * @param updatedInstance  new version of the instance that needs updating
     * @return updated instance
     */
    public Relationship incrementVersion(String              userId,
                                         InstanceAuditHeader originalInstance,
                                         Relationship        updatedInstance)
    {
        updatedInstance.setUpdatedBy(userId);
        updatedInstance.setUpdateTime(new Date());

        long currentVersion = originalInstance.getVersion();
        updatedInstance.setVersion(currentVersion+1);

        return updatedInstance;
    }


    /**
     * Changes the control information to reflect an update in an instance.
     *
     * @param userId           user making the change.
     * @param originalInstance original instance before the change
     * @param updatedInstance  new version of the instance that needs updating
     * @return updated instance
     */
    public Classification incrementVersion(String              userId,
                                           InstanceAuditHeader originalInstance,
                                           Classification      updatedInstance)
    {
        updatedInstance.setUpdatedBy(userId);
        updatedInstance.setUpdateTime(new Date());

        long currentVersion = originalInstance.getVersion();
        updatedInstance.setVersion(currentVersion+1);

        return updatedInstance;
    }


    /**
     * Changes the control information to reflect an update in an instance.
     *
     * @param userId           user making the change.
     * @param originalInstance original instance before the change
     * @param updatedInstance  new version of the instance that needs updating
     * @return updated instance
     */
    public EntityDetail incrementVersion(String              userId,
                                         InstanceAuditHeader originalInstance,
                                         EntityDetail        updatedInstance)
    {
        updatedInstance.setUpdatedBy(userId);
        updatedInstance.setUpdateTime(new Date());

        long currentVersion = originalInstance.getVersion();
        updatedInstance.setVersion(currentVersion+1);

        return updatedInstance;
    }




    /**
     * Generate an entity proxy from an entity and its TypeDef.
     *
     * @param sourceName source of the request (used for logging)
     * @param entity     entity instance
     * @return new entity proxy
     * @throws RepositoryErrorException logic error in the repository corrupted entity
     */
    public EntityProxy getNewEntityProxy(String       sourceName,
                                         EntityDetail entity) throws RepositoryErrorException
    {
        final String  methodName = "getNewEntityProxy";
        final String  parameterName = "entity";

        validateRepositoryContentManager(methodName);

        if (entity != null)
        {
            InstanceType type = entity.getType();

            if (type != null)
            {
                try
                {
                    TypeDef typeDef = repositoryContentManager.getTypeDef(sourceName,
                                                                          parameterName,
                                                                          parameterName,
                                                                          type.getTypeDefGUID(),
                                                                          type.getTypeDefName(),
                                                                          methodName);

                    EntityProxy            entityProxy          = new EntityProxy(entity);
                    InstanceProperties     entityProperties     = entity.getProperties();

                    if (entityProperties != null)
                    {
                        List<TypeDefAttribute> propertiesDefinition =
                                repositoryContentManager.getAllPropertiesForTypeDef(sourceName,
                                                                                    typeDef,
                                                                                    methodName);
                        InstanceProperties uniqueAttributes = new InstanceProperties();

                        if (propertiesDefinition != null)
                        {
                            for (TypeDefAttribute typeDefAttribute : propertiesDefinition)
                            {
                                if (typeDefAttribute != null)
                                {
                                    String propertyName = typeDefAttribute.getAttributeName();

                                    if ((typeDefAttribute.isUnique()) && (propertyName != null))
                                    {
                                        InstancePropertyValue propertyValue = entityProperties.getPropertyValue(
                                                propertyName);

                                        if (propertyValue != null)
                                        {
                                            uniqueAttributes.setProperty(propertyName, propertyValue);
                                        }
                                    }
                                }
                            }
                        }

                        if (uniqueAttributes.getPropertyCount() > 0)
                        {
                            entityProxy.setUniqueProperties(uniqueAttributes);
                        }
                    }

                    return entityProxy;
                }
                catch (TypeErrorException error)
                {
                    OMRSErrorCode errorCode = OMRSErrorCode.REPOSITORY_LOGIC_ERROR;
                    String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(sourceName,
                                                                                                             methodName,
                                                                                                             error.getErrorMessage());

                    throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                                                       this.getClass().getName(),
                                                       methodName,
                                                       errorMessage,
                                                       errorCode.getSystemAction(),
                                                       errorCode.getUserAction());
                }
            }
        }

        return null;
    }


    /**
     * Return a filled out entity.
     *
     * @param sourceName            source of the request (used for logging)
     * @param metadataCollectionId  unique identifier for the home metadata collection
     * @param provenanceType        origin of the entity
     * @param userName              name of the creator
     * @param typeName              name of the type
     * @param properties            properties for the entity
     * @param classifications       list of classifications for the entity
     * @return                      an entity that is filled out
     * @throws TypeErrorException   the type name is not recognized as an entity type
     */
    public EntityProxy getNewEntityProxy(String                    sourceName,
                                         String                    metadataCollectionId,
                                         InstanceProvenanceType    provenanceType,
                                         String                    userName,
                                         String                    typeName,
                                         InstanceProperties        properties,
                                         List<Classification>      classifications) throws TypeErrorException
    {
        EntityProxy entity = this.getSkeletonEntityProxy(sourceName,
                                                         metadataCollectionId,
                                                         provenanceType,
                                                         userName,
                                                         typeName);

        entity.setUniqueProperties(properties);
        entity.setClassifications(classifications);

        return entity;
    }

    /**
     * Return an entity with the header and type information filled out.  The caller only needs to add properties
     * and classifications to complete the set up of the entity.
     *
     * @param sourceName                source of the request (used for logging)
     * @param metadataCollectionId      unique identifier for the home metadata collection
     * @param provenanceType            origin of the entity
     * @param userName                  name of the creator
     * @param typeName                  name of the type
     * @return                          partially filled out entity  needs classifications and properties
     * @throws TypeErrorException       the type name is not recognized.
     */
    public EntityProxy getSkeletonEntityProxy(String                  sourceName,
                                              String                  metadataCollectionId,
                                              InstanceProvenanceType  provenanceType,
                                              String                  userName,
                                              String                  typeName) throws TypeErrorException
    {
        final String methodName = "getSkeletonEntityProxy";

        validateRepositoryContentManager(methodName);

        EntityProxy entity = new EntityProxy();
        String       guid   = UUID.randomUUID().toString();

        entity.setInstanceProvenanceType(provenanceType);
        entity.setMetadataCollectionId(metadataCollectionId);
        entity.setCreateTime(new Date());
        entity.setGUID(guid);
        entity.setVersion(1L);

        entity.setType(repositoryContentManager.getInstanceType(sourceName, TypeDefCategory.ENTITY_DEF, typeName, methodName));
        entity.setStatus(repositoryContentManager.getInitialStatus(sourceName, typeName, methodName));
        entity.setCreatedBy(userName);
        entity.setInstanceURL(repositoryContentManager.getEntityURL(sourceName, guid));

        return entity;
    }


    /**
     * Return boolean true if entity is linked by this relationship.
     *
     * @param sourceName   name of source requesting help
     * @param entityGUID   unique identifier of entity
     * @param relationship relationship to test
     * @return boolean indicating whether the entity is mentioned in the relationship
     */
    public boolean relatedEntity(String sourceName,
                                 String entityGUID,
                                 Relationship relationship)
    {
        if (relationship != null)
        {
            EntityProxy entityOneProxy = relationship.getEntityOneProxy();
            EntityProxy entityTwoProxy = relationship.getEntityTwoProxy();

            if (entityOneProxy != null)
            {
                if (entityGUID.equals(entityOneProxy.getGUID()))
                {
                    return true;
                }
            }

            if (entityTwoProxy != null)
            {
                if (entityGUID.equals(entityTwoProxy.getGUID()))
                {
                    return true;
                }
            }
        }

        return false;
    }


    /**
     * Remove the named property from the instance properties object.
     *
     * @param propertyName name of property to remove
     * @param properties instance properties object to work on
     */
    private void removeProperty(String    propertyName, InstanceProperties  properties)
    {
        if (properties != null)
        {
            Map<String, InstancePropertyValue> instancePropertyValueMap = properties.getInstanceProperties();

            if (instancePropertyValueMap != null)
            {
                instancePropertyValueMap.remove(propertyName);
                properties.setInstanceProperties(instancePropertyValueMap);
            }
        }
    }



    /**
     * Return the requested property or null if property is not found.  If the property is not
     * a string property then a logic exception is thrown
     *
     * @param sourceName source of call
     * @param propertyName name of requested property
     * @param properties properties from the instance.
     * @param methodName method of caller
     * @return string property value or null
     */
    public String getStringProperty(String             sourceName,
                                    String             propertyName,
                                    InstanceProperties properties,
                                    String             methodName)
    {
        final String  thisMethodName = "getStringProperty";

        if (properties != null)
        {
            InstancePropertyValue instancePropertyValue = properties.getPropertyValue(propertyName);

            if (instancePropertyValue != null)
            {
                try
                {
                    if (instancePropertyValue.getInstancePropertyCategory() == InstancePropertyCategory.PRIMITIVE)
                    {
                        PrimitivePropertyValue primitivePropertyValue = (PrimitivePropertyValue) instancePropertyValue;

                        if (primitivePropertyValue.getPrimitiveDefCategory() == PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING)
                        {
                            if (primitivePropertyValue.getPrimitiveValue() != null)
                            {
                                String retrievedProperty = primitivePropertyValue.getPrimitiveValue().toString();
                                log.debug("Retrieved " + propertyName + " property: " + retrievedProperty);

                                return retrievedProperty;
                            }
                        }
                    }
                }
                catch (Throwable error)
                {
                    throwHelperLogicError(sourceName, methodName, thisMethodName);
                }
            }
        }

        log.debug("No " + propertyName + " property");
        return null;
    }


    /**
     * Return the requested property or null if property is not found.  If the property is found, it is removed from
     * the InstanceProperties structure.  If the property is not a string property then a logic exception is thrown.
     *
     * @param sourceName  source of call
     * @param propertyName  name of requested property
     * @param properties  properties from the instance.
     * @param methodName  method of caller
     * @return string property value or null
     */
    public String removeStringProperty(String             sourceName,
                                       String             propertyName,
                                       InstanceProperties properties,
                                       String             methodName)
    {
        String  retrievedProperty = null;

        if (properties != null)
        {
            retrievedProperty = this.getStringProperty(sourceName, propertyName, properties, methodName);

            if (retrievedProperty != null)
            {
                this.removeProperty(propertyName, properties);
                log.debug("Properties left: " + properties.toString());
            }
        }

        log.debug("Retrieved " + propertyName + " property: " + retrievedProperty);
        return retrievedProperty;
    }


    /**
     * Return the requested property or null if property is not found.  If the property is not
     * a map property then a logic exception is thrown
     *
     * @param sourceName source of call
     * @param propertyName name of requested property
     * @param properties properties from the instance.
     * @param methodName method of caller
     * @return string property value or null
     */
    public InstanceProperties getMapProperty(String             sourceName,
                                             String             propertyName,
                                             InstanceProperties properties,
                                             String             methodName)
    {
        final String  thisMethodName = "getMapProperty";

        if (properties != null)
        {
            InstancePropertyValue instancePropertyValue = properties.getPropertyValue(propertyName);

            if (instancePropertyValue != null)
            {
                try
                {
                    if (instancePropertyValue.getInstancePropertyCategory() == InstancePropertyCategory.MAP)
                    {
                        MapPropertyValue mapPropertyValue = (MapPropertyValue) instancePropertyValue;

                        log.debug("Retrieved map property " + propertyName);

                        return mapPropertyValue.getMapValues();
                    }
                }
                catch (Throwable error)
                {
                    throwHelperLogicError(sourceName, methodName, thisMethodName);
                }
            }
        }

        log.debug("Map property " + propertyName + " not present");
        return null;
    }


    /**
     * Locates and extracts a string array property and extracts its values.
     *
     * @param sourceName source of call
     * @param propertyName name of requested map property
     * @param properties all of the properties of the instance
     * @param callingMethodName method of caller
     * @return array property value or null
     */
    public List<String> getStringArrayProperty(String             sourceName,
                                               String             propertyName,
                                               InstanceProperties properties,
                                               String             callingMethodName)
    {
        final String  thisMethodName = "getStringArrayProperty";

        if (properties != null)
        {
            InstancePropertyValue instancePropertyValue = properties.getPropertyValue(propertyName);

            if (instancePropertyValue != null)
            {
                /*
                 * The property exists in the supplied properties.   It should be of category ARRAY.
                 * If it is then it can be cast to an ArrayPropertyValue in order to extract the
                 * array size and the values.
                 */
                log.debug(thisMethodName + "retrieved array property " + propertyName + " for " + callingMethodName);

                try
                {
                    if (instancePropertyValue.getInstancePropertyCategory() == InstancePropertyCategory.ARRAY)
                    {
                        ArrayPropertyValue arrayPropertyValue = (ArrayPropertyValue) instancePropertyValue;

                        if ((arrayPropertyValue != null) && (arrayPropertyValue.getArrayCount() > 0))
                        {
                            /*
                             * There are values to extract
                             */
                            log.debug(thisMethodName + " found that array property " + propertyName + " has " + arrayPropertyValue.getArrayCount() + " elements.");

                            return getInstancePropertiesAsArray(arrayPropertyValue.getArrayValues(), callingMethodName);
                        }
                    }
                }
                catch (Throwable error)
                {
                    throwHelperLogicError(sourceName, callingMethodName, thisMethodName);
                }
            }
        }

        log.debug(propertyName + " not present in " + properties);
        return null;
    }


    /**
     * Locates and extracts a string array property and extracts its values.
     * If the property is found, it is removed from the InstanceProperties structure.
     * If the property is not an array property then a logic exception is thrown.
     *
     * @param sourceName source of call
     * @param propertyName name of requested map property
     * @param properties all of the properties of the instance
     * @param methodName method of caller
     * @return array property value or null
     */
    public List<String> removeStringArrayProperty(String             sourceName,
                                                  String             propertyName,
                                                  InstanceProperties properties,
                                                  String             methodName)
    {
        List<String>  retrievedProperty = null;

        if (properties != null)
        {
            retrievedProperty = this.getStringArrayProperty(sourceName, propertyName, properties, methodName);

            if (retrievedProperty != null)
            {
                this.removeProperty(propertyName, properties);
                log.debug("Properties left: " + properties.toString());
            }
        }

        log.debug("Retrieved " + propertyName + " property: " + retrievedProperty);
        return retrievedProperty;
    }


    /**
     * Convert the values in the instance properties into a String Array.  It assumes all of the elements are primitives.
     *
     * @param instanceProperties instance properties containing the values.  They should all be primitive Strings.
     * @param callingMethodName method of caller
     * @return list of strings
     */
    public List<String> getInstancePropertiesAsArray(InstanceProperties     instanceProperties,
                                                     String                 callingMethodName)
    {
        final String  thisMethodName = "getInstancePropertiesAsArray";

        if (instanceProperties != null)
        {
            Map<String, InstancePropertyValue> instancePropertyValues = instanceProperties.getInstanceProperties();
            List<String>                       resultingArray = new ArrayList<>();

            for (String arrayOrdinalName : instancePropertyValues.keySet())
            {
                if (arrayOrdinalName != null)
                {
                    log.debug(thisMethodName + " processing array element: " + arrayOrdinalName);

                    int                   arrayOrdinalNumber  = Integer.decode(arrayOrdinalName);
                    InstancePropertyValue actualPropertyValue = instanceProperties.getPropertyValue(arrayOrdinalName);

                    if (actualPropertyValue != null)
                    {
                        if (actualPropertyValue.getInstancePropertyCategory() == InstancePropertyCategory.PRIMITIVE)
                        {
                            PrimitivePropertyValue primitivePropertyValue = (PrimitivePropertyValue) actualPropertyValue;
                            resultingArray.add(arrayOrdinalNumber, primitivePropertyValue.getPrimitiveValue().toString());
                        }
                        else
                        {
                            log.error(thisMethodName + " skipping collection value: " + actualPropertyValue + " from method " + callingMethodName);
                        }
                    }
                    else
                    {
                        log.error(thisMethodName + " skipping null value" + " from method " + callingMethodName);
                    }
                }
                else
                {
                    log.error(thisMethodName + " skipping null ordinal" + " from method " + callingMethodName);
                }
            }

            log.debug(thisMethodName + " returning array: " + resultingArray + " to method " + callingMethodName);
            return resultingArray;
        }

        log.debug(thisMethodName + " has no property values to extract for method " + callingMethodName);
        return null;
    }


    /**
     * Locates and extracts a property from an instance that is of type map and then converts its values into a Java map.
     *
     * @param sourceName source of call
     * @param propertyName name of requested map property
     * @param properties values of the property
     * @param methodName method of caller
     * @return map property value or null
     */
    public Map<String, String> getStringMapFromProperty(String             sourceName,
                                                        String             propertyName,
                                                        InstanceProperties properties,
                                                        String             methodName)
    {
        Map<String, Object>   mapFromProperty = this.getMapFromProperty(sourceName, propertyName, properties, methodName);

        if (mapFromProperty != null)
        {
            Map<String, String>  stringMapFromProperty = new HashMap<>();

            for (String mapPropertyName : mapFromProperty.keySet())
            {
                Object actualPropertyValue = mapFromProperty.get(mapPropertyName);

                if (actualPropertyValue != null)
                {
                    stringMapFromProperty.put(mapPropertyName, actualPropertyValue.toString());
                }
            }

            if (! stringMapFromProperty.isEmpty())
            {
                return stringMapFromProperty;
            }
        }

        return null;
    }


    /**
     * Locates and extracts a property from an instance that is of type map and then converts its values into a Java map.
     * If the property is found, it is removed from the InstanceProperties structure.
     * If the property is not a map property then a logic exception is thrown.
     *
     * @param sourceName source of call
     * @param propertyName name of requested map property
     * @param properties values of the property
     * @param methodName method of caller
     * @return map property value or null
     */
    public Map<String, String> removeStringMapFromProperty(String             sourceName,
                                                           String             propertyName,
                                                           InstanceProperties properties,
                                                           String             methodName)
    {
        Map<String, String>  retrievedProperty = null;

        if (properties != null)
        {
            retrievedProperty = this.getStringMapFromProperty(sourceName, propertyName, properties, methodName);

            if (retrievedProperty != null)
            {
                this.removeProperty(propertyName, properties);
                log.debug("Properties left: " + properties.toString());
            }
        }

        log.debug("Retrieved " + propertyName + " property: " + retrievedProperty);
        return retrievedProperty;
    }


    /**
     * Locates and extracts a property from an instance that is of type map and then converts its values into a Java map.
     *
     * @param sourceName source of call
     * @param propertyName name of requested map property
     * @param properties all of the properties of the instance
     * @param methodName method of caller
     * @return map property value or null
     */
    public Map<String, Object> getMapFromProperty(String             sourceName,
                                                  String             propertyName,
                                                  InstanceProperties properties,
                                                  String             methodName)
    {
        final String  thisMethodName = "getMapFromProperty";

        if (properties != null)
        {
            InstancePropertyValue instancePropertyValue = properties.getPropertyValue(propertyName);

            if (instancePropertyValue != null)
            {
                try
                {
                    if (instancePropertyValue.getInstancePropertyCategory() == InstancePropertyCategory.MAP)
                    {
                        MapPropertyValue mapInstancePropertyValue = (MapPropertyValue) instancePropertyValue;

                        log.debug("Retrieved map property " + propertyName);

                        return this.getInstancePropertiesAsMap(mapInstancePropertyValue.getMapValues());
                    }
                }
                catch (Throwable error)
                {
                    throwHelperLogicError(sourceName, methodName, thisMethodName);
                }
            }
        }

        log.debug("Map property " + propertyName + " not present");
        return null;
    }


    /**
     * Convert an instance properties object into a map.
     *
     * @param instanceProperties packed properties
     * @return properties stored in Java map
     */
    public Map<String, Object> getInstancePropertiesAsMap(InstanceProperties    instanceProperties)
    {
        if (instanceProperties != null)
        {
            Map<String, InstancePropertyValue> instancePropertyValues = instanceProperties.getInstanceProperties();
            Map<String, Object>                resultingMap      = new HashMap<>();

            for (String mapPropertyName : instancePropertyValues.keySet())
            {
                InstancePropertyValue actualPropertyValue = instanceProperties.getPropertyValue(mapPropertyName);

                if (actualPropertyValue != null)
                {
                    if (actualPropertyValue.getInstancePropertyCategory() == InstancePropertyCategory.PRIMITIVE)
                    {
                        PrimitivePropertyValue primitivePropertyValue = (PrimitivePropertyValue) actualPropertyValue;
                        resultingMap.put(mapPropertyName, primitivePropertyValue.getPrimitiveValue());
                    }
                    else if (actualPropertyValue.getInstancePropertyCategory() == InstancePropertyCategory.ENUM)
                    {
                        EnumPropertyValue  enumPropertyValue = (EnumPropertyValue) actualPropertyValue;
                        resultingMap.put(mapPropertyName, enumPropertyValue.getSymbolicName());
                    }
                    else
                    {
                        resultingMap.put(mapPropertyName, actualPropertyValue);
                    }
                }
            }

            log.debug("Returning map: " + resultingMap);
            return resultingMap;
        }

        log.debug("No Properties present");
        return null;
    }



    /**
     * Return the requested property or 0 if property is not found.  If the property is not
     * an int property then a logic exception is thrown.
     *
     * @param sourceName source of call
     * @param propertyName name of requested property
     * @param properties properties from the instance.
     * @param methodName method of caller
     * @return string property value or null
     */
    public int    getIntProperty(String             sourceName,
                                 String             propertyName,
                                 InstanceProperties properties,
                                 String             methodName)
    {
        final String  thisMethodName = "getIntProperty";

        if (properties != null)
        {
            InstancePropertyValue instancePropertyValue = properties.getPropertyValue(propertyName);

            if (instancePropertyValue != null)
            {
                try
                {
                    if (instancePropertyValue.getInstancePropertyCategory() == InstancePropertyCategory.PRIMITIVE)
                    {
                        PrimitivePropertyValue primitivePropertyValue = (PrimitivePropertyValue) instancePropertyValue;

                        if (primitivePropertyValue.getPrimitiveDefCategory() == PrimitiveDefCategory.OM_PRIMITIVE_TYPE_INT)
                        {
                            log.debug("Retrieved integer property " + propertyName);

                            if (primitivePropertyValue.getPrimitiveValue() != null)
                            {
                                return Integer.valueOf(primitivePropertyValue.getPrimitiveValue().toString());
                            }
                        }
                    }
                }
                catch (Throwable error)
                {
                    throwHelperLogicError(sourceName, methodName, thisMethodName);
                }
            }
        }

        log.debug("Integer property " + propertyName + " not present");

        return 0;
    }


    /**
     * Return the requested property or 0 if property is not found.
     * If the property is found, it is removed from the InstanceProperties structure.
     * If the property is not an int property then a logic exception is thrown.
     *
     * @param sourceName  source of call
     * @param propertyName  name of requested property
     * @param properties  properties from the instance.
     * @param methodName  method of caller
     * @return string property value or null
     */
    public int    removeIntProperty(String             sourceName,
                                    String             propertyName,
                                    InstanceProperties properties,
                                    String             methodName)
    {
        int  retrievedProperty = 0;

        if (properties != null)
        {
            retrievedProperty = this.getIntProperty(sourceName, propertyName, properties, methodName);

            this.removeProperty(propertyName, properties);
            log.debug("Properties left: " + properties.toString());
        }

        log.debug("Retrieved " + propertyName + " property: " + retrievedProperty);
        return retrievedProperty;
    }


    /**
     * Return the requested property or null if property is not found.  If the property is not
     * a date property then a logic exception is thrown.
     *
     * @param sourceName source of call
     * @param propertyName name of requested property
     * @param properties properties from the instance.
     * @param methodName method of caller
     * @return string property value or null
     */
    public Date    getDateProperty(String             sourceName,
                                   String             propertyName,
                                   InstanceProperties properties,
                                   String             methodName)
    {
        final String  thisMethodName = "getDateProperty";

        if (properties != null)
        {
            InstancePropertyValue instancePropertyValue = properties.getPropertyValue(propertyName);

            if (instancePropertyValue != null)
            {
                try
                {
                    if (instancePropertyValue.getInstancePropertyCategory() == InstancePropertyCategory.PRIMITIVE)
                    {
                        PrimitivePropertyValue primitivePropertyValue = (PrimitivePropertyValue) instancePropertyValue;

                        if (primitivePropertyValue.getPrimitiveDefCategory() == PrimitiveDefCategory.OM_PRIMITIVE_TYPE_DATE)
                        {
                            log.debug("Retrieved date property " + propertyName);

                            if (primitivePropertyValue.getPrimitiveValue() != null)
                            {
                                return (Date)primitivePropertyValue.getPrimitiveValue();
                            }
                        }
                    }
                }
                catch (Throwable error)
                {
                    throwHelperLogicError(sourceName, methodName, thisMethodName);
                }
            }
        }

        log.debug("Date property " + propertyName + " not present");

        return null;
    }


    /**
     * Return the requested property or null if property is not found.
     * If the property is found, it is removed from the InstanceProperties structure.
     * If the property is not a date property then a logic exception is thrown.
     *
     * @param sourceName  source of call
     * @param propertyName  name of requested property
     * @param properties  properties from the instance.
     * @param methodName  method of caller
     * @return string property value or null
     */
    public Date    removeDateProperty(String             sourceName,
                                     String             propertyName,
                                     InstanceProperties properties,
                                     String             methodName)
    {
        Date  retrievedProperty = null;

        if (properties != null)
        {
            retrievedProperty = this.getDateProperty(sourceName, propertyName, properties, methodName);

            this.removeProperty(propertyName, properties);
            log.debug("Properties left: " + properties.toString());
        }

        log.debug("Retrieved " + propertyName + " property: " + retrievedProperty);
        return retrievedProperty;
    }



    /**
     * Return the requested property or false if property is not found.  If the property is not
     * a boolean property then a logic exception is thrown
     *
     * @param sourceName source of call
     * @param propertyName name of requested property
     * @param properties properties from the instance.
     * @param methodName method of caller
     * @return string property value or null
     */
    public boolean getBooleanProperty(String             sourceName,
                                      String             propertyName,
                                      InstanceProperties properties,
                                      String             methodName)
    {
        final String  thisMethodName = "getBooleanProperty";

        if (properties != null)
        {
            InstancePropertyValue instancePropertyValue = properties.getPropertyValue(propertyName);

            if (instancePropertyValue != null)
            {
                try
                {
                    if (instancePropertyValue.getInstancePropertyCategory() == InstancePropertyCategory.PRIMITIVE)
                    {
                        PrimitivePropertyValue primitivePropertyValue = (PrimitivePropertyValue) instancePropertyValue;

                        if (primitivePropertyValue.getPrimitiveDefCategory() == PrimitiveDefCategory.OM_PRIMITIVE_TYPE_BOOLEAN)
                        {
                            log.debug("Retrieved boolean property " + propertyName);

                            if (primitivePropertyValue.getPrimitiveValue() != null)
                            {
                                return Boolean.valueOf(primitivePropertyValue.getPrimitiveValue().toString());
                            }
                        }
                    }
                }
                catch (Throwable error)
                {
                    throwHelperLogicError(sourceName, methodName, thisMethodName);
                }
            }
        }

        log.debug("Boolean property " + propertyName + " not present");

        return false;
    }


    /**
     * Return the requested property or false if property is not found.
     * If the property is found, it is removed from the InstanceProperties structure.
     * If the property is not a boolean property then a logic exception is thrown.
     *
     * @param sourceName  source of call
     * @param propertyName  name of requested property
     * @param properties  properties from the instance.
     * @param methodName  method of caller
     * @return string property value or null
     */
    public boolean removeBooleanProperty(String             sourceName,
                                         String             propertyName,
                                         InstanceProperties properties,
                                         String             methodName)
    {
        boolean  retrievedProperty = false;

        if (properties != null)
        {
            retrievedProperty = this.getBooleanProperty(sourceName, propertyName, properties, methodName);

            this.removeProperty(propertyName, properties);
            log.debug("Properties left: " + properties.toString());
        }

        log.debug("Retrieved " + propertyName + " property: " + retrievedProperty);
        return retrievedProperty;
    }


    /**
     * Add the supplied property to an instance properties object.  If the instance property object
     * supplied is null, a new instance properties object is created.
     *
     * @param sourceName name of caller
     * @param properties properties object to add property to, may be null.
     * @param propertyName name of property
     * @param propertyValue value of property
     * @param methodName calling method name
     * @return instance properties object.
     */
    public InstanceProperties addStringPropertyToInstance(String             sourceName,
                                                          InstanceProperties properties,
                                                          String             propertyName,
                                                          String             propertyValue,
                                                          String             methodName)
    {
        InstanceProperties  resultingProperties;

        if (propertyValue != null)
        {
            log.debug("Adding property " + propertyName + " for " + methodName);

            if (properties == null)
            {
                log.debug("First property");

                resultingProperties = new InstanceProperties();
            }
            else
            {
                resultingProperties = properties;
            }


            PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();

            primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
            primitivePropertyValue.setPrimitiveValue(propertyValue);
            primitivePropertyValue.setTypeName(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING.getName());
            primitivePropertyValue.setTypeGUID(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING.getGUID());

            resultingProperties.setProperty(propertyName, primitivePropertyValue);

            return resultingProperties;
        }
        else
        {
            log.debug("Null property");
            return properties;
        }
    }


    /**
     * Add the supplied property to an instance properties object.  If the instance property object
     * supplied is null, a new instance properties object is created.
     *
     * @param sourceName name of caller
     * @param properties properties object to add property to, may be null.
     * @param propertyName name of property
     * @param propertyValue value of property
     * @param methodName calling method name
     * @return instance properties object.
     */
    public InstanceProperties addIntPropertyToInstance(String             sourceName,
                                                       InstanceProperties properties,
                                                       String             propertyName,
                                                       int                propertyValue,
                                                       String             methodName)
    {
        InstanceProperties  resultingProperties;

        log.debug("Adding property " + propertyName + " for " + methodName);

        if (properties == null)
        {
            log.debug("First property");

            resultingProperties = new InstanceProperties();
        }
        else
        {
            resultingProperties = properties;
        }


        PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();

        primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_INT);
        primitivePropertyValue.setPrimitiveValue(propertyValue);
        primitivePropertyValue.setTypeName(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_INT.getName());
        primitivePropertyValue.setTypeGUID(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_INT.getGUID());

        resultingProperties.setProperty(propertyName, primitivePropertyValue);

        return resultingProperties;
    }


    /**
     * Add the supplied property to an instance properties object.  If the instance property object
     * supplied is null, a new instance properties object is created.
     *
     * @param sourceName  name of caller
     * @param properties  properties object to add property to may be null.
     * @param propertyName  name of property
     * @param propertyValue  value of property
     * @param methodName  calling method name
     * @return instance properties object.
     */
    public InstanceProperties addLongPropertyToInstance(String             sourceName,
                                                        InstanceProperties properties,
                                                        String             propertyName,
                                                        long               propertyValue,
                                                        String             methodName)
    {
        InstanceProperties  resultingProperties;

        log.debug("Adding property " + propertyName + " for " + methodName);

        if (properties == null)
        {
            log.debug("First property");

            resultingProperties = new InstanceProperties();
        }
        else
        {
            resultingProperties = properties;
        }


        PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();

        primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_LONG);
        primitivePropertyValue.setPrimitiveValue(propertyValue);

        resultingProperties.setProperty(propertyName, primitivePropertyValue);

        return resultingProperties;
    }


    /**
     * Add the supplied property to an instance properties object.  If the instance property object
     * supplied is null, a new instance properties object is created.
     *
     * @param sourceName  name of caller
     * @param properties  properties object to add property to may be null.
     * @param propertyName  name of property
     * @param propertyValue  value of property
     * @param methodName  calling method name
     * @return instance properties object.
     */
    public InstanceProperties addFloatPropertyToInstance(String             sourceName,
                                                         InstanceProperties properties,
                                                         String             propertyName,
                                                         float              propertyValue,
                                                         String             methodName)
    {
        InstanceProperties  resultingProperties;

        log.debug("Adding property " + propertyName + " for " + methodName);

        if (properties == null)
        {
            log.debug("First property");

            resultingProperties = new InstanceProperties();
        }
        else
        {
            resultingProperties = properties;
        }


        PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();

        primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_FLOAT);
        primitivePropertyValue.setPrimitiveValue(propertyValue);

        resultingProperties.setProperty(propertyName, primitivePropertyValue);

        return resultingProperties;
    }


    /**
     * Add the supplied property to an instance properties object.  If the instance property object
     * supplied is null, a new instance properties object is created.
     *
     * @param sourceName  name of caller
     * @param properties  properties object to add property to may be null.
     * @param propertyName  name of property
     * @param propertyValue  value of property
     * @param methodName  calling method name
     * @return instance properties object.
     */
    public InstanceProperties addDatePropertyToInstance(String             sourceName,
                                                        InstanceProperties properties,
                                                        String             propertyName,
                                                        Date               propertyValue,
                                                        String             methodName)
    {
        InstanceProperties  resultingProperties;

        log.debug("Adding property " + propertyName + " for " + methodName);

        if (properties == null)
        {
            log.debug("First property");

            resultingProperties = new InstanceProperties();
        }
        else
        {
            resultingProperties = properties;
        }


        PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();

        primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_DATE);
        primitivePropertyValue.setPrimitiveValue(propertyValue);

        resultingProperties.setProperty(propertyName, primitivePropertyValue);

        return resultingProperties;
    }


    /**
     * Add the supplied property to an instance properties object.  If the instance property object
     * supplied is null, a new instance properties object is created.
     *
     * @param sourceName name of caller
     * @param properties properties object to add property to, may be null.
     * @param propertyName name of property
     * @param propertyValue value of property
     * @param methodName calling method name
     * @return instance properties object.
     */
    public InstanceProperties addBooleanPropertyToInstance(String             sourceName,
                                                           InstanceProperties properties,
                                                           String             propertyName,
                                                           boolean            propertyValue,
                                                           String             methodName)
    {
        InstanceProperties  resultingProperties;

        log.debug("Adding property " + propertyName + " for " + methodName);

        if (properties == null)
        {
            log.debug("First property");

            resultingProperties = new InstanceProperties();
        }
        else
        {
            resultingProperties = properties;
        }


        PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();

        primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_BOOLEAN);
        primitivePropertyValue.setPrimitiveValue(propertyValue);
        primitivePropertyValue.setTypeName(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_BOOLEAN.getName());
        primitivePropertyValue.setTypeGUID(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_BOOLEAN.getGUID());

        resultingProperties.setProperty(propertyName, primitivePropertyValue);

        return resultingProperties;
    }


    /**
     * Add the supplied property to an instance properties object.  If the instance property object
     * supplied is null, a new instance properties object is created.
     *
     * @param sourceName name of caller
     * @param properties properties object to add property to, may be null.
     * @param propertyName name of property
     * @param ordinal numeric value of property
     * @param symbolicName String value of property
     * @param description String description of property value
     * @param methodName calling method name
     * @return instance properties object.
     */
    public InstanceProperties addEnumPropertyToInstance(String             sourceName,
                                                        InstanceProperties properties,
                                                        String             propertyName,
                                                        int                ordinal,
                                                        String             symbolicName,
                                                        String             description,
                                                        String             methodName)
    {
        InstanceProperties  resultingProperties;

        log.debug("Adding property " + propertyName + " for " + methodName);

        if (properties == null)
        {
            log.debug("First property");

            resultingProperties = new InstanceProperties();
        }
        else
        {
            resultingProperties = properties;
        }


        EnumPropertyValue enumPropertyValue = new EnumPropertyValue();

        enumPropertyValue.setOrdinal(ordinal);
        enumPropertyValue.setSymbolicName(symbolicName);
        enumPropertyValue.setDescription(description);

        resultingProperties.setProperty(propertyName, enumPropertyValue);

        return resultingProperties;
    }



    public InstanceProperties addStringArrayPropertyToInstance(String sourceName, InstanceProperties properties, String propertyName, List<String> propertyValues, String methodName) {
        if (propertyValues != null) {
            log.debug("Adding property " + propertyName + " for " + methodName);
            InstanceProperties resultingProperties;
            if (properties == null) {
                log.debug("First property");
                resultingProperties = new InstanceProperties();
            } else {
                resultingProperties = properties;
            }

            ArrayPropertyValue arrayPropertyValue = new ArrayPropertyValue();

            int count = 0;
            for(String propertyValue : propertyValues){
                PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                primitivePropertyValue.setPrimitiveValue(propertyValue);
                primitivePropertyValue.setTypeName(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING.getName());
                primitivePropertyValue.setTypeGUID(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING.getGUID());
                arrayPropertyValue.setArrayValue(count++, primitivePropertyValue);
            }
            arrayPropertyValue.setArrayCount(propertyValues.size());
            resultingProperties.setProperty(propertyName, arrayPropertyValue);
            return resultingProperties;
        } else {
            log.debug("Null property");
            return properties;
        }
    }




    /**
     * Add the supplied map property to an instance properties object.  The supplied map is stored as a single
     * property in the instances properties.   If the instance properties object
     * supplied is null, a new instance properties object is created.
     *
     * @param sourceName name of caller
     * @param properties properties object to add property to, may be null.
     * @param propertyName name of property
     * @param mapValues contents of the map
     * @param methodName calling method name
     * @return instance properties object.
     */
    public InstanceProperties addMapPropertyToInstance(String              sourceName,
                                                       InstanceProperties  properties,
                                                       String              propertyName,
                                                       Map<String, String> mapValues,
                                                       String              methodName)
    {
        if (mapValues != null)
        {
            log.debug("Adding property " + propertyName + " for " + methodName);

            if ((mapValues != null) && (! mapValues.isEmpty()))
            {
                InstanceProperties  resultingProperties;

                if (properties == null)
                {
                    resultingProperties = new InstanceProperties();
                }
                else
                {
                    resultingProperties = properties;
                }


                /*
                 * The values of a map property are stored as an embedded InstanceProperties object.
                 */
                InstanceProperties  mapInstanceProperties  = this.addPropertyMapToInstance(sourceName,
                                                                                           null,
                                                                                           propertyName,
                                                                                           mapValues,
                                                                                           methodName);

                /*
                 * If there was content in the map then the resulting InstanceProperties are added as
                 * a property to the resulting properties.
                 */
                if (mapInstanceProperties != null)
                {
                    MapPropertyValue mapPropertyValue = new MapPropertyValue();
                    mapPropertyValue.setMapValues(mapInstanceProperties);
                    resultingProperties.setProperty(propertyName, mapPropertyValue);

                    log.debug("Returning instanceProperty: " + resultingProperties.toString());

                    return resultingProperties;
                }
            }
        }

        log.debug("Null property");
        return properties;
    }


    /**
     * Add the supplied property map to an instance properties object.  Each of the entries in the map is added
     * as a separate property in instance properties.  If the instance properties object
     * supplied is null, a new instance properties object is created.
     *
     * @param sourceName name of caller
     * @param properties properties object to add property to, may be null.
     * @param propertyName name of property
     * @param mapValues contents of the map
     * @param methodName calling method name
     * @return instance properties object.
     */
    public InstanceProperties addPropertyMapToInstance(String              sourceName,
                                                       InstanceProperties  properties,
                                                       String              propertyName,
                                                       Map<String, String> mapValues,
                                                       String              methodName)
    {
        if ((mapValues != null) && (! mapValues.isEmpty()))
        {
            log.debug("Adding property " + propertyName + " for " + methodName);

            InstanceProperties  resultingProperties;

            if (properties == null)
            {
                resultingProperties = new InstanceProperties();
            }
            else
            {
                resultingProperties = properties;
            }

            int propertyCount = 0;

            for (String mapPropertyName : mapValues.keySet())
            {
                Object mapPropertyValue = mapValues.get(mapPropertyName);

                if (mapPropertyValue instanceof String)
                {
                    PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                    primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
                    primitivePropertyValue.setPrimitiveValue(mapPropertyValue);
                    primitivePropertyValue.setTypeName(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING.getName());
                    primitivePropertyValue.setTypeGUID(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING.getGUID());
                    resultingProperties.setProperty(mapPropertyName, primitivePropertyValue);
                    propertyCount++;
                }
                else if (mapPropertyValue instanceof Integer)
                {
                    PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                    primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_INT);
                    primitivePropertyValue.setTypeName(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_INT.getName());
                    primitivePropertyValue.setTypeGUID(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_INT.getGUID());
                    primitivePropertyValue.setPrimitiveValue(mapPropertyValue);
                    resultingProperties.setProperty(mapPropertyName, primitivePropertyValue);
                    propertyCount++;
                }
                else if (mapPropertyValue instanceof Long)
                {
                    PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                    primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_LONG);
                    primitivePropertyValue.setTypeName(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_LONG.getName());
                    primitivePropertyValue.setTypeGUID(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_LONG.getGUID());
                    primitivePropertyValue.setPrimitiveValue(mapPropertyValue);
                    resultingProperties.setProperty(mapPropertyName, primitivePropertyValue);
                    propertyCount++;
                }
                else if (mapPropertyValue instanceof Short)
                {
                    PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                    primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_SHORT);
                    primitivePropertyValue.setTypeName(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_SHORT.getName());
                    primitivePropertyValue.setTypeGUID(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_SHORT.getGUID());
                    primitivePropertyValue.setPrimitiveValue(mapPropertyValue);
                    resultingProperties.setProperty(mapPropertyName, primitivePropertyValue);
                    propertyCount++;
                }
                else if (mapPropertyValue instanceof Date)
                {
                    PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                    primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_DATE);
                    primitivePropertyValue.setTypeName(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_DATE.getName());
                    primitivePropertyValue.setTypeGUID(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_DATE.getGUID());
                    primitivePropertyValue.setPrimitiveValue(mapPropertyValue);
                    resultingProperties.setProperty(mapPropertyName, primitivePropertyValue);
                    propertyCount++;
                }
                else if (mapPropertyValue instanceof Character)
                {
                    PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                    primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_CHAR);
                    primitivePropertyValue.setTypeName(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_CHAR.getName());
                    primitivePropertyValue.setTypeGUID(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_CHAR.getGUID());
                    primitivePropertyValue.setPrimitiveValue(mapPropertyValue);
                    resultingProperties.setProperty(mapPropertyName, primitivePropertyValue);
                    propertyCount++;
                }
                else if (mapPropertyValue instanceof Byte)
                {
                    PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                    primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_BYTE);
                    primitivePropertyValue.setTypeName(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_BYTE.getName());
                    primitivePropertyValue.setTypeGUID(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_BYTE.getGUID());
                    primitivePropertyValue.setPrimitiveValue(mapPropertyValue);
                    resultingProperties.setProperty(mapPropertyName, primitivePropertyValue);
                    propertyCount++;
                }
                else if (mapPropertyValue instanceof Boolean)
                {
                    PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                    primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_BOOLEAN);
                    primitivePropertyValue.setTypeName(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_BOOLEAN.getName());
                    primitivePropertyValue.setTypeGUID(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_BOOLEAN.getGUID());
                    primitivePropertyValue.setPrimitiveValue(mapPropertyValue);
                    resultingProperties.setProperty(mapPropertyName, primitivePropertyValue);
                    propertyCount++;
                }
                else if (mapPropertyValue instanceof Float)
                {
                    PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                    primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_FLOAT);
                    primitivePropertyValue.setTypeName(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_FLOAT.getName());
                    primitivePropertyValue.setTypeGUID(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_FLOAT.getGUID());
                    primitivePropertyValue.setPrimitiveValue(mapPropertyValue);
                    resultingProperties.setProperty(mapPropertyName, primitivePropertyValue);
                    propertyCount++;
                }
                else if (mapPropertyValue instanceof BigDecimal)
                {
                    PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                    primitivePropertyValue.setPrimitiveDefCategory(
                            PrimitiveDefCategory.OM_PRIMITIVE_TYPE_BIGDECIMAL);
                    primitivePropertyValue.setTypeName(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_BIGDECIMAL.getName());
                    primitivePropertyValue.setTypeGUID(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_BIGDECIMAL.getGUID());
                    primitivePropertyValue.setPrimitiveValue(mapPropertyValue);
                    resultingProperties.setProperty(mapPropertyName, primitivePropertyValue);
                    propertyCount++;
                }
                else if (mapPropertyValue instanceof BigInteger)
                {
                    PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                    primitivePropertyValue.setPrimitiveDefCategory(
                            PrimitiveDefCategory.OM_PRIMITIVE_TYPE_BIGINTEGER);
                    primitivePropertyValue.setTypeName(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_BIGINTEGER.getName());
                    primitivePropertyValue.setTypeGUID(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_BIGINTEGER.getGUID());
                    primitivePropertyValue.setPrimitiveValue(mapPropertyValue);
                    resultingProperties.setProperty(mapPropertyName, primitivePropertyValue);
                    propertyCount++;
                }
                else if (mapPropertyValue instanceof Double)
                {
                    PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                    primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_DOUBLE);
                    primitivePropertyValue.setTypeName(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_DOUBLE.getName());
                    primitivePropertyValue.setTypeGUID(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_DOUBLE.getGUID());
                    primitivePropertyValue.setPrimitiveValue(mapPropertyValue);
                    resultingProperties.setProperty(mapPropertyName, primitivePropertyValue);
                    propertyCount++;
                }
                else
                {
                    PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
                    primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_UNKNOWN);
                    primitivePropertyValue.setTypeName(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_UNKNOWN.getName());
                    primitivePropertyValue.setTypeGUID(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_UNKNOWN.getGUID());
                    primitivePropertyValue.setPrimitiveValue(mapPropertyValue);
                    resultingProperties.setProperty(mapPropertyName, primitivePropertyValue);
                    propertyCount++;
                }
            }

            if (propertyCount > 0)
            {
                log.debug("Returning instanceProperty: " + resultingProperties.toString());

                return resultingProperties;
            }
        }

        log.debug("Null property");
        return properties;
    }


    /**
     * Returns the type name from an instance (entity, relationship or classification).
     *
     * @param instance instance to read
     * @return String type name
     * @throws InvalidParameterException if the parameters are null or invalid
     * @throws RepositoryErrorException if the instance does not have a type name
     */
    public String   getTypeName(InstanceAuditHeader      instance) throws RepositoryErrorException,
                                                                          InvalidParameterException
    {
        final String methodName = "getTypeName";

        if (instance != null)
        {
            InstanceType type = instance.getType();

            if (type != null)
            {
                if (type.getTypeDefName() != null)
                {
                    return type.getTypeDefName();
                }
            }

            throwRepositoryContentError(methodName, instance);
        }

        throwParameterError(methodName);
        return null;
    }


    /**
     * Return the guid of an entity linked to end 1 of the relationship.
     *
     * @param relationship relationship to parse
     * @return String unique identifier
     */
    public String  getEnd1EntityGUID(Relationship   relationship)
    {
        if (relationship != null)
        {
            EntityProxy entityProxy = relationship.getEntityOneProxy();

            if (entityProxy != null)
            {
                if (entityProxy.getGUID() != null)
                {
                    return entityProxy.getGUID();
                }
            }
        }

        return null;
    }


    /**
     * Return the guid of an entity linked to end 2 of the relationship.
     *
     * @param relationship relationship to parse
     * @return String unique identifier
     */
    public String  getEnd2EntityGUID(Relationship   relationship)
    {
        if (relationship != null)
        {
            EntityProxy entityProxy = relationship.getEntityTwoProxy();

            if (entityProxy != null)
            {
                if (entityProxy.getGUID() != null)
                {
                    return entityProxy.getGUID();
                }
            }
        }

        return null;
    }
    /**
     * Use the paging and sequencing parameters to format the results for a repository call that returns a list of
     * entity instances.
     *
     * @param fullResults - the full list of results in an arbitrary order
     * @param fromElement - the starting element number of the instances to return. This is used when retrieving elements
     *                    beyond the first page of results. Zero means start from the first element.
     * @param sequencingProperty - String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder - Enum defining how the results should be ordered.
     * @param pageSize - the maximum number of result entities that can be returned on this request.  Zero means
     *                 unrestricted return results size.
     * @return results array as requested
     * @throws PropertyErrorException the sequencing property specified is not valid for any of the requested types of
     *                                  entity.
     * @throws PagingErrorException the paging/sequencing parameters are set up incorrectly.
     */
    public List<EntityDetail>  formatEntityResults(List<EntityDetail>   fullResults,
                                                   int                  fromElement,
                                                   String               sequencingProperty,
                                                   SequencingOrder      sequencingOrder,
                                                   int                  pageSize) throws PagingErrorException,
                                                                                         PropertyErrorException
    {
        if (fullResults == null)
        {
            return null;
        }

        if (fullResults.isEmpty())
        {
            return null;
        }

        int fullResultsSize =fullResults.size();

        List<EntityDetail>  sortedResults = fullResults;
        // todo sort list according to properties

        if ((pageSize == 0) || (pageSize > sortedResults.size()))
        {
            return sortedResults;
        }

        int toIndex = getToIndex(fromElement, pageSize, fullResultsSize);
        return new ArrayList<>(fullResults.subList(fromElement, toIndex));
    }


    /**
     * Use the paging and sequencing parameters to format the results for a repository call that returns a list of
     * relationship instances.
     *
     * @param fullResults - the full list of results in an arbitrary order. This is supplied not empty.
     * @param fromElement - the starting element number of the instances to return. This is used when retrieving elements
     *                    beyond the first page of results. Zero means start from the first element.
     * @param sequencingProperty - String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder - Enum defining how the results should be ordered.
     * @param pageSize - the maximum number of result entities that can be returned on this request.  Zero means
     *                 unrestricted return results size.
     * @return results array as requested
     * @throws PropertyErrorException the sequencing property specified is not valid for any of the requested types of
     *                                  relationship.
     * @throws PagingErrorException the paging/sequencing parameters are set up incorrectly.
     */
    public List<Relationship>  formatRelationshipResults(List<Relationship>   fullResults,
                                                         int                  fromElement,
                                                         String               sequencingProperty,
                                                         SequencingOrder      sequencingOrder,
                                                         int                  pageSize) throws PagingErrorException,
                                                                                               PropertyErrorException
    {
        if (fullResults == null)
        {
            return null;
        }

        if (fullResults.isEmpty())
        {
            return null;
        }
        int fullResultsSize =fullResults.size();

        if (fromElement > fullResultsSize)
        {
            return null;
        }


        List<Relationship>  sortedResults = fullResults;
        // todo sort list according to properties

        if ((pageSize == 0) || (pageSize > sortedResults.size()))
        {
            return sortedResults;
        }

        int toIndex = getToIndex(fromElement, pageSize, fullResultsSize);
        return new ArrayList<>(fullResults.subList(fromElement, toIndex));
    }

    /**
     * When issuing find requests with paging, it can be that we have all the data, but need to only return
     * a subset of the data based on the page size. This method is given the from index and a pageSize and calculates
     * the to index.
     * @param fromIndex the index into the data to start from.
     * @param pageSize the page size to use. 0 means no paging.
     * @param totalSize the total size of the data.
     * @return the to index.
     */
    private int getToIndex(int fromIndex, int pageSize, int totalSize) {
        int toIndex = 0;
        if (totalSize < fromIndex + pageSize)
        {
            toIndex = totalSize;
        } else
        {
            toIndex = fromIndex + pageSize;
        }
        return toIndex;
    }


    /**
     * Throws a logic error exception when the repository helper is called with invalid parameters.
     * Normally this means the repository helper methods have been called in the wrong order.
     *
     * @param sourceName name of the calling repository or service
     * @param originatingMethodName method that called the repository validator
     * @param localMethodName local method that deleted the error
     */
    private void throwHelperLogicError(String     sourceName,
                                       String     originatingMethodName,
                                       String     localMethodName)
    {
        OMRSErrorCode errorCode = OMRSErrorCode.HELPER_LOGIC_ERROR;
        String errorMessage     = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(sourceName,
                                                                                                     localMethodName,
                                                                                                     originatingMethodName);

        throw new OMRSLogicErrorException(errorCode.getHTTPErrorCode(),
                                          this.getClass().getName(),
                                          localMethodName,
                                          errorMessage,
                                          errorCode.getSystemAction(),
                                          errorCode.getUserAction());
    }


    /**
     * Throws a logic error exception when the repository validator is called with invalid parameters.
     * Normally this means the repository validator methods have been called in the wrong order.
     *
     * @param methodName local method that detected the error
     * @throws InvalidParameterException for an invalid parameter - this is typically a logic error
     */
    private void throwParameterError(String     methodName) throws InvalidParameterException
    {
        OMRSErrorCode errorCode = OMRSErrorCode.NULL_PARAMETER;
        String errorMessage     = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName);

        throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                                          this.getClass().getName(),
                                          methodName,
                                          errorMessage,
                                          errorCode.getSystemAction(),
                                          errorCode.getUserAction());
    }


    /**
     * Throws a logic error exception when the repository validator is called with invalid parameters.
     * Normally this means the repository validator methods have been called in the wrong order.
     *
     * @param methodName local method that detected the error
     * @param instance instance in error
     * @throws RepositoryErrorException there is an invalid instance
     */
    private void throwRepositoryContentError(String              methodName,
                                             InstanceAuditHeader instance) throws RepositoryErrorException
    {
        OMRSErrorCode errorCode = OMRSErrorCode.INVALID_INSTANCE;
        String errorMessage     = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                                                                                                     instance.toString());

        throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                                          this.getClass().getName(),
                                          methodName,
                                          errorMessage,
                                          errorCode.getSystemAction(),
                                          errorCode.getUserAction());
    }



    /**
     * Throw a logic error exception if this object does not have a repository content manager.
     * This would occur if if is being used in an environment where the OMRS has not been properly
     * initialized.
     *
     * @param methodName name of calling method.
     */
    private void validateRepositoryContentManager(String   methodName)
    {
        if (repositoryContentManager == null)
        {
            OMRSErrorCode errorCode = OMRSErrorCode.LOCAL_REPOSITORY_CONFIGURATION_ERROR;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage();

            throw new OMRSLogicErrorException(errorCode.getHTTPErrorCode(),
                                              this.getClass().getName(),
                                              methodName,
                                              errorMessage,
                                              errorCode.getSystemAction(),
                                              errorCode.getUserAction());
        }
    }
}
