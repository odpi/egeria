/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector;

import com.fasterxml.jackson.databind.JsonNode;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.IGCRestClient;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.IGCVersionEnum;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.Reference;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.ReferenceList;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.search.IGCSearch;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.search.IGCSearchCondition;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.search.IGCSearchConditionSet;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.search.IGCSearchSorting;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.*;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.attributes.AttributeMapping;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.classifications.ClassificationMapping;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.entities.EntityMapping;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships.RelationshipMapping;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.entities.ReferenceableMapper;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.entities.PropertyMappingSet;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.model.OMRSStub;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollectionBase;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryValidator;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;

/**
 * Provides the OMRSMetadataCollection implementation for IBM InfoSphere Information Governance Catalog ("IGC").
 */
public class IGCOMRSMetadataCollection extends OMRSMetadataCollectionBase {

    private static final Logger log = LoggerFactory.getLogger(IGCOMRSMetadataCollection.class);

    public static final String MAPPING_PKG = "org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.";
    public static final String DEFAULT_IGC_TYPE = "main_object";
    public static final String DEFAULT_IGC_TYPE_DISPLAY_NAME = "Main Object";

    public static final String GENERATED_TYPE_PREFIX = "__|";
    public static final String GENERATED_TYPE_POSTFIX = "|__";

    private IGCRestClient igcRestClient;

    private HashSet<ImplementedMapping> implementedMappings;
    private HashMap<String, TypeDef> unimplementedTypeDefs;
    private HashSet<ImplementedAttribute> implementedAttributes;

    private XMLOutputFactory xmlOutputFactory;

    /**
     * @param parentConnector      connector that this metadata collection supports.
     *                             The connector has the information to call the metadata repository.
     * @param repositoryName       name of this repository.
     * @param repositoryHelper     helper that provides methods to repository connectors and repository event mappers
     *                             to build valid type definitions (TypeDefs), entities and relationships.
     * @param repositoryValidator  validator class for checking open metadata repository objects and parameters
     * @param metadataCollectionId unique identifier for the repository
     */
    public IGCOMRSMetadataCollection(IGCOMRSRepositoryConnector parentConnector,
                                     String repositoryName,
                                     OMRSRepositoryHelper repositoryHelper,
                                     OMRSRepositoryValidator repositoryValidator,
                                     String metadataCollectionId) {
        super(parentConnector, repositoryName, repositoryHelper, repositoryValidator, metadataCollectionId);
        this.igcRestClient = parentConnector.getIGCRestClient();
        this.xmlOutputFactory = XMLOutputFactory.newInstance();
        this.implementedMappings = new HashSet<>();
        this.unimplementedTypeDefs = new HashMap<>();
        this.implementedAttributes = new HashSet<>();
    }

    /**
     * Returns the list of different types of metadata organized into two groups.  The first are the
     * attribute type definitions (AttributeTypeDefs).  These provide types for properties in full
     * type definitions.  Full type definitions (TypeDefs) describe types for entities, relationships
     * and classifications.
     *
     * (Currently only full type definitions (TypeDefs) are implemented.)
     *
     * @param userId unique identifier for requesting user.
     * @return TypeDefGalleryResponse List of different categories of type definitions.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public TypeDefGallery getAllTypes(String   userId) throws RepositoryErrorException,
            UserNotAuthorizedException
    {
        final String                       methodName = "getAllTypes";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);

        /*
         * Perform operation
         */
        TypeDefGallery typeDefGallery = new TypeDefGallery();
        ArrayList<TypeDef> typeDefs = new ArrayList<>();
        for (ImplementedMapping implementedMapping : implementedMappings) {
            typeDefs.add(implementedMapping.getTypeDef());
        }
        typeDefGallery.setTypeDefs(typeDefs);

        ArrayList<AttributeTypeDef> attributeTypeDefs = new ArrayList<>();
        for (ImplementedAttribute implementedAttribute : implementedAttributes) {
            attributeTypeDefs.add(implementedAttribute.getAttributeTypeDef());
        }
        typeDefGallery.setAttributeTypeDefs(attributeTypeDefs);

        return typeDefGallery;
    }



    /**
     * Returns all of the TypeDefs for a specific category.
     *
     * @param userId unique identifier for requesting user.
     * @param category enum value for the category of TypeDef to return.
     * @return TypeDefs list.
     * @throws InvalidParameterException the TypeDefCategory is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public List<TypeDef> findTypeDefsByCategory(String          userId,
                                                TypeDefCategory category) throws InvalidParameterException,
            RepositoryErrorException,
            UserNotAuthorizedException {
        final String methodName            = "findTypeDefsByCategory";
        final String categoryParameterName = "category";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateTypeDefCategory(repositoryName, categoryParameterName, category, methodName);

        ArrayList<TypeDef> typeDefs = new ArrayList<>();
        for (ImplementedMapping implementedMapping : implementedMappings) {
            TypeDef candidate = implementedMapping.getTypeDef();
            if (candidate.getCategory().equals(category)) {
                typeDefs.add(candidate);
            }
        }
        return typeDefs;

    }


    /**
     * Returns all of the AttributeTypeDefs for a specific category.
     *
     * @param userId unique identifier for requesting user.
     * @param category enum value for the category of an AttributeTypeDef to return.
     * @return TypeDefs list.
     * @throws InvalidParameterException the TypeDefCategory is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public List<AttributeTypeDef> findAttributeTypeDefsByCategory(String                   userId,
                                                                  AttributeTypeDefCategory category) throws InvalidParameterException,
            RepositoryErrorException,
            UserNotAuthorizedException {
        final String methodName            = "findAttributeTypeDefsByCategory";
        final String categoryParameterName = "category";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateAttributeTypeDefCategory(repositoryName, categoryParameterName, category, methodName);

        ArrayList<AttributeTypeDef> attributeTypeDefs = new ArrayList<>();
        for (ImplementedAttribute implementedAttribute : implementedAttributes) {
            AttributeTypeDef candidate = implementedAttribute.getAttributeTypeDef();
            if (candidate.getCategory().equals(category)) {
                attributeTypeDefs.add(candidate);
            }
        }
        return attributeTypeDefs;

    }

    /**
     * Return the TypeDefs that have the properties matching the supplied match criteria.
     *
     * @param userId unique identifier for requesting user.
     * @param matchCriteria TypeDefProperties containing a list of property names.
     * @return TypeDefs list.
     * @throws InvalidParameterException the matchCriteria is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public List<TypeDef> findTypeDefsByProperty(String            userId,
                                                TypeDefProperties matchCriteria) throws InvalidParameterException,
            RepositoryErrorException,
            UserNotAuthorizedException
    {
        final String  methodName                 = "findTypeDefsByProperty";
        final String  matchCriteriaParameterName = "matchCriteria";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateMatchCriteria(repositoryName, matchCriteriaParameterName, matchCriteria, methodName);

        /*
         * Perform operation
         */
        // TODO: implement
        OMRSErrorCode errorCode = OMRSErrorCode.METHOD_NOT_IMPLEMENTED;

        String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                this.getClass().getName(),
                repositoryName);

        throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                this.getClass().getName(),
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction());
    }

    /**
     * Return the types that are linked to the elements from the specified standard.
     *
     * @param userId unique identifier for requesting user.
     * @param standard name of the standard; null means any.
     * @param organization name of the organization; null means any.
     * @param identifier identifier of the element in the standard; null means any.
     * @return TypeDefs list each entry in the list contains a typedef.  This is is a structure
     * describing the TypeDef's category and properties.
     * @throws InvalidParameterException all attributes of the external id are null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public List<TypeDef> findTypesByExternalID(String    userId,
                                               String    standard,
                                               String    organization,
                                               String    identifier) throws InvalidParameterException,
            RepositoryErrorException,
            UserNotAuthorizedException
    {
        final String                       methodName = "findTypesByExternalID";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateExternalId(repositoryName, standard, organization, identifier, methodName);

        /*
         * Perform operation
         */
        // TODO: implement
        OMRSErrorCode errorCode = OMRSErrorCode.METHOD_NOT_IMPLEMENTED;

        String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                this.getClass().getName(),
                repositoryName);

        throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                this.getClass().getName(),
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction());
    }

    /**
     * Return the TypeDefs that match the search criteria.
     *
     * @param userId unique identifier for requesting user.
     * @param searchCriteria String search criteria.
     * @return TypeDefs list where each entry in the list contains a typedef.  This is is a structure
     * describing the TypeDef's category and properties.
     * @throws InvalidParameterException the searchCriteria is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public List<TypeDef> searchForTypeDefs(String userId,
                                           String searchCriteria) throws InvalidParameterException,
            RepositoryErrorException,
            UserNotAuthorizedException
    {
        final String methodName                  = "searchForTypeDefs";
        final String searchCriteriaParameterName = "searchCriteria";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateSearchCriteria(repositoryName, searchCriteriaParameterName, searchCriteria, methodName);

        /*
         * Perform operation
         */
        List<TypeDef> typeDefs = new ArrayList<>();
        for (ImplementedMapping implementedMapping : implementedMappings) {

            TypeDef candidate = implementedMapping.getTypeDef();
            if (candidate.getName().matches(searchCriteria)) {
                typeDefs.add(candidate);
            }

        }

        return typeDefs;

    }

    /**
     * Return the TypeDef identified by the GUID.
     *
     * @param userId unique identifier for requesting user.
     * @param guid String unique id of the TypeDef.
     * @return TypeDef structure describing its category and properties.
     * @throws InvalidParameterException the guid is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws TypeDefNotKnownException The requested TypeDef is not known in the metadata collection.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public TypeDef getTypeDefByGUID(String    userId,
                                    String    guid) throws InvalidParameterException,
            RepositoryErrorException,
            TypeDefNotKnownException,
            UserNotAuthorizedException {
        final String methodName        = "getTypeDefByGUID";
        final String guidParameterName = "guid";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateGUID(repositoryName, guidParameterName, guid, methodName);

        TypeDef found = null;
        for (ImplementedMapping implementedMapping : implementedMappings) {
            String typeDefGUID = implementedMapping.getTypeDef().getGUID();
            if (typeDefGUID.equals(guid)) {
                found = implementedMapping.getTypeDef();
                break;
            }
        }
        if (found == null) {
            OMRSErrorCode errorCode = OMRSErrorCode.TYPEDEF_ID_NOT_KNOWN;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(
                    guid,
                    guidParameterName,
                    methodName,
                    repositoryName);
            throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }
        return found;

    }

    /**
     * Return the AttributeTypeDef identified by the GUID.
     *
     * @param userId unique identifier for requesting user.
     * @param guid String unique id of the TypeDef
     * @return TypeDef structure describing its category and properties.
     * @throws InvalidParameterException the guid is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws TypeDefNotKnownException The requested TypeDef is not known in the metadata collection.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public  AttributeTypeDef getAttributeTypeDefByGUID(String    userId,
                                                       String    guid) throws InvalidParameterException,
            RepositoryErrorException,
            TypeDefNotKnownException,
            UserNotAuthorizedException {
        final String methodName        = "getAttributeTypeDefByGUID";
        final String guidParameterName = "guid";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateGUID(repositoryName, guidParameterName, guid, methodName);

        AttributeTypeDef found = null;
        for (ImplementedAttribute implementedAttribute : implementedAttributes) {
            String attributeTypeDefGUID = implementedAttribute.getAttributeTypeDef().getGUID();
            if (attributeTypeDefGUID.equals(guid)) {
                found = implementedAttribute.getAttributeTypeDef();
                break;
            }
        }
        if (found == null) {
            OMRSErrorCode errorCode = OMRSErrorCode.ATTRIBUTE_TYPEDEF_ID_NOT_KNOWN;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(
                    "unknown",
                    guid,
                    methodName,
                    repositoryName);
            throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }
        return found;

    }

    /**
     * Return the TypeDef identified by the unique name.
     *
     * @param userId unique identifier for requesting user.
     * @param name String name of the TypeDef.
     * @return TypeDef structure describing its category and properties.
     * @throws InvalidParameterException the name is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws TypeDefNotKnownException the requested TypeDef is not found in the metadata collection.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public TypeDef getTypeDefByName(String    userId,
                                    String    name) throws InvalidParameterException,
            RepositoryErrorException,
            TypeDefNotKnownException,
            UserNotAuthorizedException
    {
        final String  methodName = "getTypeDefByName";
        final String  nameParameterName = "name";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateTypeName(repositoryName, nameParameterName, name, methodName);

        /*
         * Perform operation
         */
        TypeDef found = null;
        for (ImplementedMapping implementedMapping : implementedMappings) {
            TypeDef candidate = implementedMapping.getTypeDef();
            if (candidate.getName().equals(name)) {
                found = candidate;
                break;
            }
        }
        if (found == null) {
            OMRSErrorCode errorCode = OMRSErrorCode.TYPEDEF_NAME_NOT_KNOWN;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(
                    name,
                    methodName,
                    repositoryName);
            throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }
        return found;

    }

    /**
     * Return the AttributeTypeDef identified by the unique name.
     *
     * @param userId unique identifier for requesting user.
     * @param name String name of the TypeDef.
     * @return TypeDef structure describing its category and properties.
     * @throws InvalidParameterException the name is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws TypeDefNotKnownException the requested TypeDef is not found in the metadata collection.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public  AttributeTypeDef getAttributeTypeDefByName(String    userId,
                                                       String    name) throws InvalidParameterException,
            RepositoryErrorException,
            TypeDefNotKnownException,
            UserNotAuthorizedException {
        final String  methodName = "getAttributeTypeDefByName";
        final String  nameParameterName = "name";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateTypeName(repositoryName, nameParameterName, name, methodName);

        AttributeTypeDef found = null;
        for (ImplementedAttribute implementedAttribute : implementedAttributes) {
            AttributeTypeDef candidate = implementedAttribute.getAttributeTypeDef();
            if (candidate.getName().equals(name)) {
                found = candidate;
                break;
            }
        }
        if (found == null) {
            OMRSErrorCode errorCode = OMRSErrorCode.ATTRIBUTE_TYPEDEF_NAME_NOT_KNOWN;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(
                    name,
                    methodName,
                    repositoryName);
            throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }
        return found;
    }

    /**
     * Create a collection of related types.
     *
     * @param userId unique identifier for requesting user.
     * @param newTypes TypeDefGalleryResponse structure describing the new AttributeTypeDefs and TypeDefs.
     * @throws InvalidParameterException the new TypeDef is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws TypeDefNotSupportedException the repository is not able to support this TypeDef.
     * @throws TypeDefKnownException the TypeDef is already stored in the repository.
     * @throws TypeDefConflictException the new TypeDef conflicts with an existing TypeDef.
     * @throws InvalidTypeDefException the new TypeDef has invalid contents.
     * @throws FunctionNotSupportedException the repository does not support this call.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public  void addTypeDefGallery(String          userId,
                                   TypeDefGallery  newTypes) throws InvalidParameterException,
            RepositoryErrorException,
            TypeDefNotSupportedException,
            TypeDefKnownException,
            TypeDefConflictException,
            InvalidTypeDefException,
            FunctionNotSupportedException,
            UserNotAuthorizedException
    {
        final String  methodName = "addTypeDefGallery";
        final String  galleryParameterName = "newTypes";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateTypeDefGallery(repositoryName, galleryParameterName, newTypes, methodName);

        /*
         * Perform operation
         */
        List<AttributeTypeDef> attributeTypeDefs = newTypes.getAttributeTypeDefs();
        if (attributeTypeDefs != null) {
            for (AttributeTypeDef attributeTypeDef : attributeTypeDefs) {
                addAttributeTypeDef(userId, attributeTypeDef);
            }
        }

        List<TypeDef> typeDefs = newTypes.getTypeDefs();
        if (typeDefs != null) {
            for (TypeDef typeDef : typeDefs) {
                addTypeDef(userId, typeDef);
            }
        }

    }

    /**
     * Create a definition of a new TypeDef.
     *
     * @param userId unique identifier for requesting user.
     * @param newTypeDef TypeDef structure describing the new TypeDef.
     * @throws InvalidParameterException the new TypeDef is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws TypeDefNotSupportedException the repository is not able to support this TypeDef.
     * @throws TypeDefKnownException the TypeDef is already stored in the repository.
     * @throws TypeDefConflictException the new TypeDef conflicts with an existing TypeDef.
     * @throws InvalidTypeDefException the new TypeDef has invalid contents.
     * @throws FunctionNotSupportedException the repository does not support this call.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public void addTypeDef(String       userId,
                           TypeDef      newTypeDef) throws InvalidParameterException,
            RepositoryErrorException,
            TypeDefNotSupportedException,
            TypeDefKnownException,
            TypeDefConflictException,
            InvalidTypeDefException,
            FunctionNotSupportedException,
            UserNotAuthorizedException {
        final String  methodName = "addTypeDef";
        final String  typeDefParameterName = "newTypeDef";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateTypeDef(repositoryName, typeDefParameterName, newTypeDef, methodName);
        repositoryValidator.validateUnknownTypeDef(repositoryName, typeDefParameterName, newTypeDef, methodName);

        TypeDefCategory typeDefCategory = newTypeDef.getCategory();
        String omrsTypeDefName = newTypeDef.getName();
        log.debug("Looking for mapping for {} of type {}", omrsTypeDefName, typeDefCategory.getName());

        // See if we have a Mapper defined for the class -- if so, it's implemented
        StringBuilder sbMapperClassname = new StringBuilder();
        sbMapperClassname.append(MAPPING_PKG);
        switch(typeDefCategory) {
            case RELATIONSHIP_DEF:
                sbMapperClassname.append("relationships.");
                break;
            case CLASSIFICATION_DEF:
                sbMapperClassname.append("classifications.");
                break;
            default:
                sbMapperClassname.append("entities.");
                break;
        }
        sbMapperClassname.append(omrsTypeDefName);
        sbMapperClassname.append("Mapper");

        try {
            Class mappingClass = Class.forName(sbMapperClassname.toString());
            log.debug(" ... found mapping class: {}", mappingClass);

            ImplementedMapping implementedMapping = new ImplementedMapping(
                    newTypeDef,
                    mappingClass,
                    (IGCOMRSRepositoryConnector)parentConnector,
                    userId
            );
            implementedMapping.registerPOJOs(this.igcRestClient);
            implementedMappings.add(implementedMapping);

        } catch (ClassNotFoundException e) {
            unimplementedTypeDefs.put(newTypeDef.getGUID(), newTypeDef);
            throw new TypeDefNotSupportedException(404, IGCOMRSMetadataCollection.class.getName(), methodName, omrsTypeDefName + " is not supported.", "", "Request support through Egeria GitHub issue.");
        }
    }

    /**
     * Create a definition of a new AttributeTypeDef.
     *
     * @param userId unique identifier for requesting user.
     * @param newAttributeTypeDef TypeDef structure describing the new TypeDef.
     * @throws InvalidParameterException the new TypeDef is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws TypeDefNotSupportedException the repository is not able to support this TypeDef.
     * @throws TypeDefKnownException the TypeDef is already stored in the repository.
     * @throws TypeDefConflictException the new TypeDef conflicts with an existing TypeDef.
     * @throws InvalidTypeDefException the new TypeDef has invalid contents.
     * @throws FunctionNotSupportedException the repository does not support this call.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public  void addAttributeTypeDef(String             userId,
                                     AttributeTypeDef   newAttributeTypeDef) throws InvalidParameterException,
            RepositoryErrorException,
            TypeDefNotSupportedException,
            TypeDefKnownException,
            TypeDefConflictException,
            InvalidTypeDefException,
            FunctionNotSupportedException,
            UserNotAuthorizedException {
        final String  methodName           = "addAttributeTypeDef";
        final String  typeDefParameterName = "newAttributeTypeDef";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateAttributeTypeDef(repositoryName, typeDefParameterName, newAttributeTypeDef, methodName);
        repositoryValidator.validateUnknownAttributeTypeDef(repositoryName, typeDefParameterName, newAttributeTypeDef, methodName);

        // Note this is only implemented for Enums, support for other types is indicated directly
        // in the verifyAttributeTypeDef method
        AttributeTypeDefCategory attributeTypeDefCategory = newAttributeTypeDef.getCategory();
        String omrsTypeDefName = newAttributeTypeDef.getName();
        log.debug("Looking for mapping for {} of type {}", omrsTypeDefName, attributeTypeDefCategory.getName());

        // See if we have a Mapper defined for the class -- if so, it's implemented
        StringBuilder sbMapperClassname = new StringBuilder();
        sbMapperClassname.append(MAPPING_PKG);
        sbMapperClassname.append("attributes.");
        sbMapperClassname.append(omrsTypeDefName);
        sbMapperClassname.append("Mapper");

        try {
            Class mappingClass = Class.forName(sbMapperClassname.toString());
            log.debug(" ... found mapping class: {}", mappingClass);

            ImplementedAttribute implementedAttribute = new ImplementedAttribute(
                    newAttributeTypeDef,
                    mappingClass
            );
            implementedAttributes.add(implementedAttribute);

        } catch (ClassNotFoundException e) {
            throw new TypeDefNotSupportedException(404, IGCOMRSMetadataCollection.class.getName(), methodName, omrsTypeDefName + " is not supported.", "", "Request support through Egeria GitHub issue.");
        }

    }

    /**
     * Verify that a definition of a TypeDef is either new or matches the definition already stored.
     *
     * @param userId unique identifier for requesting user.
     * @param typeDef TypeDef structure describing the TypeDef to test.
     * @return boolean true means the TypeDef matches the local definition; false means the TypeDef is not known.
     * @throws InvalidParameterException the TypeDef is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws TypeDefNotSupportedException the repository is not able to support this TypeDef.
     * @throws TypeDefConflictException the new TypeDef conflicts with an existing TypeDef.
     * @throws InvalidTypeDefException the new TypeDef has invalid contents.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public boolean verifyTypeDef(String  userId,
                                 TypeDef typeDef) throws InvalidParameterException,
            RepositoryErrorException,
            TypeDefNotSupportedException,
            TypeDefConflictException,
            InvalidTypeDefException,
            UserNotAuthorizedException
    {
        final String  methodName           = "verifyTypeDef";
        final String  typeDefParameterName = "typeDef";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);
        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateTypeDef(repositoryName, typeDefParameterName, typeDef, methodName);

        // Validate that we support all of the valid InstanceStatus settings before deciding whether we
        // fully-support the TypeDef or not
        HashSet<InstanceStatus> validStatuses = new HashSet<>(typeDef.getValidInstanceStatusList());
        Set<String> mappedProperties = new HashSet<>();
        boolean bVerified = false;

        ImplementedMapping implementedMapping = getMappingForEntityType(typeDef.getGUID());
        if (implementedMapping != null) {
            HashSet<InstanceStatus> implementedStatuses = new HashSet<>();
            switch (implementedMapping.getType()) {
                case ENTITY:
                    EntityMapping entityMapping = implementedMapping.getEntityMapping();
                    mappedProperties = entityMapping.getPropertyMappings().getAllMappedOmrsProperties();
                    implementedStatuses = new HashSet<>(entityMapping.getSupportedStatuses());
                    break;
                case RELATIONSHIP:
                    RelationshipMapping relationshipMapping = implementedMapping.getRelationshipMapping();
                    mappedProperties = relationshipMapping.getMappedOmrsPropertyNames();
                    implementedStatuses = new HashSet<>(relationshipMapping.getSupportedStatuses());
                    break;
                case CLASSIFICATION:
                    ClassificationMapping classificationMapping = implementedMapping.getClassificationMapping();
                    mappedProperties = classificationMapping.getMappedOmrsPropertyNames();
                    implementedStatuses = new HashSet<>(classificationMapping.getSupportedStatuses());
                    break;
            }
            bVerified = validStatuses.equals(implementedStatuses);
        }

        // Validate that we support all of the possible properties before deciding whether we
        // fully-support the TypeDef or not
        if (bVerified) {
            List<TypeDefAttribute> properties = typeDef.getPropertiesDefinition();
            for (TypeDefAttribute typeDefAttribute : properties) {
                String omrsPropertyName = typeDefAttribute.getAttributeName();
                if (!mappedProperties.contains(omrsPropertyName)) {
                    bVerified = false;
                }
            }
        }

        return bVerified;

    }

    /**
     * Verify that a definition of an AttributeTypeDef is either new or matches the definition already stored.
     *
     * @param userId unique identifier for requesting user.
     * @param attributeTypeDef TypeDef structure describing the TypeDef to test.
     * @return boolean where true means the TypeDef matches the local definition where false means the TypeDef is not known.
     * @throws InvalidParameterException the TypeDef is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws TypeDefNotSupportedException the repository is not able to support this TypeDef.
     * @throws TypeDefConflictException the new TypeDef conflicts with an existing TypeDef.
     * @throws InvalidTypeDefException the new TypeDef has invalid contents.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public  boolean verifyAttributeTypeDef(String            userId,
                                           AttributeTypeDef  attributeTypeDef) throws InvalidParameterException,
            RepositoryErrorException,
            TypeDefNotSupportedException,
            TypeDefConflictException,
            InvalidTypeDefException,
            UserNotAuthorizedException
    {
        final String  methodName           = "verifyAttributeTypeDef";
        final String  typeDefParameterName = "attributeTypeDef";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateAttributeTypeDef(repositoryName, typeDefParameterName, attributeTypeDef, methodName);

        boolean bImplemented;
        switch (attributeTypeDef.getCategory()) {
            case PRIMITIVE:
                bImplemented = true;
                break;
            case UNKNOWN_DEF:
                bImplemented = false;
                break;
            case COLLECTION:
                bImplemented = true;
                break;
            case ENUM_DEF:
                bImplemented = (getMappingForAttributeType(attributeTypeDef.getGUID()) != null);
                break;
            default:
                bImplemented = false;
                break;
        }

        return bImplemented;

    }

    /**
     * Update one or more properties of the TypeDef.  The TypeDefPatch controls what types of updates
     * are safe to make to the TypeDef.
     *
     * @param userId unique identifier for requesting user.
     * @param typeDefPatch TypeDef patch describing change to TypeDef.
     * @return updated TypeDef
     * @throws InvalidParameterException the TypeDefPatch is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws TypeDefNotKnownException the requested TypeDef is not found in the metadata collection.
     * @throws PatchErrorException the TypeDef can not be updated because the supplied patch is incompatible
     *                               with the stored TypeDef.
     * @throws FunctionNotSupportedException the repository does not support this call.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public TypeDef updateTypeDef(String       userId,
                                 TypeDefPatch typeDefPatch) throws InvalidParameterException,
            RepositoryErrorException,
            TypeDefNotKnownException,
            PatchErrorException,
            FunctionNotSupportedException,
            UserNotAuthorizedException
    {
        final String  methodName           = "updateTypeDef";
        final String  typeDefParameterName = "typeDefPatch";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateTypeDefPatch(repositoryName, typeDefPatch, methodName);

        /*
         * Perform operation
         */
        // TODO: implement
        OMRSErrorCode errorCode = OMRSErrorCode.METHOD_NOT_IMPLEMENTED;

        String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                this.getClass().getName(),
                repositoryName);

        throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                this.getClass().getName(),
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction());
    }


    /**
     * Delete the TypeDef.  This is only possible if the TypeDef has never been used to create instances or any
     * instances of this TypeDef have been purged from the metadata collection.
     *
     * @param userId unique identifier for requesting user.
     * @param obsoleteTypeDefGUID String unique identifier for the TypeDef.
     * @param obsoleteTypeDefName String unique name for the TypeDef.
     * @throws InvalidParameterException the one of TypeDef identifiers is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws TypeDefNotKnownException the requested TypeDef is not found in the metadata collection.
     * @throws TypeDefInUseException the TypeDef can not be deleted because there are instances of this type in the
     *                                 the metadata collection.  These instances need to be purged before the
     *                                 TypeDef can be deleted.
     * @throws FunctionNotSupportedException the repository does not support this call.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public void deleteTypeDef(String    userId,
                              String    obsoleteTypeDefGUID,
                              String    obsoleteTypeDefName) throws InvalidParameterException,
            RepositoryErrorException,
            TypeDefNotKnownException,
            TypeDefInUseException,
            FunctionNotSupportedException,
            UserNotAuthorizedException
    {
        final String    methodName        = "deleteTypeDef";
        final String    guidParameterName = "obsoleteTypeDefGUID";
        final String    nameParameterName = "obsoleteTypeDefName";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateTypeDefIds(repositoryName,
                guidParameterName,
                nameParameterName,
                obsoleteTypeDefGUID,
                obsoleteTypeDefName,
                methodName);

        /*
         * Perform operation
         */
        // TODO: implement
        OMRSErrorCode errorCode = OMRSErrorCode.METHOD_NOT_IMPLEMENTED;

        String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                this.getClass().getName(),
                repositoryName);

        throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                this.getClass().getName(),
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction());
    }


    /**
     * Delete an AttributeTypeDef.  This is only possible if the AttributeTypeDef has never been used to create
     * instances or any instances of this AttributeTypeDef have been purged from the metadata collection.
     *
     * @param userId unique identifier for requesting user.
     * @param obsoleteTypeDefGUID String unique identifier for the AttributeTypeDef.
     * @param obsoleteTypeDefName String unique name for the AttributeTypeDef.
     * @throws InvalidParameterException the one of AttributeTypeDef identifiers is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws TypeDefNotKnownException the requested AttributeTypeDef is not found in the metadata collection.
     * @throws TypeDefInUseException the AttributeTypeDef can not be deleted because there are instances of this type in the
     *                                 the metadata collection.  These instances need to be purged before the
     *                                 AttributeTypeDef can be deleted.
     * @throws FunctionNotSupportedException the repository does not support this call.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public void deleteAttributeTypeDef(String    userId,
                                       String    obsoleteTypeDefGUID,
                                       String    obsoleteTypeDefName) throws InvalidParameterException,
            RepositoryErrorException,
            TypeDefNotKnownException,
            TypeDefInUseException,
            FunctionNotSupportedException,
            UserNotAuthorizedException
    {
        final String    methodName        = "deleteAttributeTypeDef";
        final String    guidParameterName = "obsoleteTypeDefGUID";
        final String    nameParameterName = "obsoleteTypeDefName";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateAttributeTypeDefIds(repositoryName,
                guidParameterName,
                nameParameterName,
                obsoleteTypeDefGUID,
                obsoleteTypeDefName,
                methodName);

        /*
         * Perform operation
         */
        // TODO: implement
        OMRSErrorCode errorCode = OMRSErrorCode.METHOD_NOT_IMPLEMENTED;

        String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                this.getClass().getName(),
                repositoryName);

        throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                this.getClass().getName(),
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction());
    }


    /**
     * Change the guid or name of an existing TypeDef to a new value.  This is used if two different
     * TypeDefs are discovered to have the same guid.  This is extremely unlikely but not impossible so
     * the open metadata protocol has provision for this.
     *
     * @param userId unique identifier for requesting user.
     * @param originalTypeDefGUID the original guid of the TypeDef.
     * @param originalTypeDefName the original name of the TypeDef.
     * @param newTypeDefGUID the new identifier for the TypeDef.
     * @param newTypeDefName new name for this TypeDef.
     * @return typeDef new values for this TypeDef, including the new guid/name.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws TypeDefNotKnownException the TypeDef identified by the original guid/name is not found
     *                                    in the metadata collection.
     * @throws FunctionNotSupportedException the repository does not support this call.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public  TypeDef reIdentifyTypeDef(String     userId,
                                      String     originalTypeDefGUID,
                                      String     originalTypeDefName,
                                      String     newTypeDefGUID,
                                      String     newTypeDefName) throws InvalidParameterException,
            RepositoryErrorException,
            TypeDefNotKnownException,
            FunctionNotSupportedException,
            UserNotAuthorizedException
    {
        final String    methodName                = "reIdentifyTypeDef";
        final String    originalGUIDParameterName = "originalTypeDefGUID";
        final String    originalNameParameterName = "originalTypeDefName";
        final String    newGUIDParameterName      = "newTypeDefGUID";
        final String    newNameParameterName      = "newTypeDefName";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateTypeDefIds(repositoryName,
                originalGUIDParameterName,
                originalNameParameterName,
                originalTypeDefGUID,
                originalTypeDefName,
                methodName);
        repositoryValidator.validateTypeDefIds(repositoryName,
                newGUIDParameterName,
                newNameParameterName,
                newTypeDefGUID,
                newTypeDefName,
                methodName);

        /*
         * Perform operation
         */
        // TODO: implement
        OMRSErrorCode errorCode = OMRSErrorCode.METHOD_NOT_IMPLEMENTED;

        String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                this.getClass().getName(),
                repositoryName);

        throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                this.getClass().getName(),
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction());
    }


    /**
     * Change the guid or name of an existing TypeDef to a new value.  This is used if two different
     * TypeDefs are discovered to have the same guid.  This is extremely unlikely but not impossible so
     * the open metadata protocol has provision for this.
     *
     * @param userId unique identifier for requesting user.
     * @param originalAttributeTypeDefGUID the original guid of the AttributeTypeDef.
     * @param originalAttributeTypeDefName the original name of the AttributeTypeDef.
     * @param newAttributeTypeDefGUID the new identifier for the AttributeTypeDef.
     * @param newAttributeTypeDefName new name for this AttributeTypeDef.
     * @return attributeTypeDef new values for this AttributeTypeDef, including the new guid/name.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws TypeDefNotKnownException the AttributeTypeDef identified by the original guid/name is not
     *                                    found in the metadata collection.
     * @throws FunctionNotSupportedException the repository does not support this call.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public  AttributeTypeDef reIdentifyAttributeTypeDef(String     userId,
                                                        String     originalAttributeTypeDefGUID,
                                                        String     originalAttributeTypeDefName,
                                                        String     newAttributeTypeDefGUID,
                                                        String     newAttributeTypeDefName) throws InvalidParameterException,
            RepositoryErrorException,
            TypeDefNotKnownException,
            FunctionNotSupportedException,
            UserNotAuthorizedException
    {
        final String    methodName                = "reIdentifyAttributeTypeDef";
        final String    originalGUIDParameterName = "originalAttributeTypeDefGUID";
        final String    originalNameParameterName = "originalAttributeTypeDefName";
        final String    newGUIDParameterName      = "newAttributeTypeDefGUID";
        final String    newNameParameterName      = "newAttributeTypeDefName";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateTypeDefIds(repositoryName,
                originalGUIDParameterName,
                originalNameParameterName,
                originalAttributeTypeDefGUID,
                originalAttributeTypeDefName,
                methodName);
        repositoryValidator.validateTypeDefIds(repositoryName,
                newGUIDParameterName,
                newNameParameterName,
                newAttributeTypeDefGUID,
                newAttributeTypeDefName,
                methodName);

        /*
         * Perform operation
         */
        // TODO: implement
        OMRSErrorCode errorCode = OMRSErrorCode.METHOD_NOT_IMPLEMENTED;

        String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                this.getClass().getName(),
                repositoryName);

        throw new FunctionNotSupportedException(errorCode.getHTTPErrorCode(),
                this.getClass().getName(),
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction());
    }

    /**
     * Returns the entity if the entity is stored in the metadata collection, otherwise null.
     *
     * @param userId unique identifier for requesting user.
     * @param guid String unique identifier for the entity
     * @return the entity details if the entity is found in the metadata collection; otherwise return null
     * @throws InvalidParameterException the guid is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public EntityDetail isEntityKnown(String     userId,
                                      String     guid) throws InvalidParameterException,
            RepositoryErrorException,
            UserNotAuthorizedException
    {
        final String  methodName = "isEntityKnown";
        final String  guidParameterName = "guid";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateGUID(repositoryName, guidParameterName, guid, methodName);

        /*
         * Perform operation
         */
        EntityDetail detail = null;
        try {
            detail = getEntityDetail(userId, guid);
        } catch (EntityNotKnownException | EntityProxyOnlyException e) {
            log.info("Entity {} not known to the repository, or only a proxy.", guid, e);
        }
        return detail;
    }


    /**
     * Return the header and classifications for a specific entity.
     *
     * @param userId unique identifier for requesting user.
     * @param guid String unique identifier for the entity.
     * @return EntitySummary structure
     * @throws InvalidParameterException the guid is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws EntityNotKnownException the requested entity instance is not known in the metadata collection.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public EntitySummary getEntitySummary(String     userId,
                                          String     guid) throws InvalidParameterException,
            RepositoryErrorException,
            EntityNotKnownException,
            UserNotAuthorizedException
    {
        final String  methodName        = "getEntitySummary";
        final String  guidParameterName = "guid";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateGUID(repositoryName, guidParameterName, guid, methodName);

        /*
         * Perform operation
         */
        log.debug("getEntitySummary with guid = {}", guid);

        // Lookup the basic asset based on the RID (strip off prefix (indicating a generated type), if there)
        String rid = getRidFromGeneratedId(guid);
        Reference asset = this.igcRestClient.getAssetRefById(rid);

        EntitySummary summary = null;
        String prefix = getPrefixFromGeneratedId(guid);

        // If we could not find any asset by the provided guid, throw an ENTITY_NOT_KNOWN exception
        if (asset == null) {
            OMRSErrorCode errorCode = OMRSErrorCode.ENTITY_NOT_KNOWN;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(guid,
                    methodName,
                    repositoryName);
            throw new EntityNotKnownException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        } else if (asset.getType().equals(DEFAULT_IGC_TYPE)) {
            /* If the asset type returned has an IGC-listed type of 'main_object', it isn't one that the REST API
             * of IGC supports (eg. a data rule detail object, a column analysis master object, etc)...
             * Trying to further process it will result in failed REST API requests; so we should skip these objects */
            OMRSErrorCode errorCode = OMRSErrorCode.INVALID_ENTITY_FROM_STORE;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(guid,
                    repositoryName,
                    methodName,
                    asset.toString());
            throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        } else {

            // Otherwise, retrieve the mapping dynamically based on the type of asset
            ReferenceableMapper referenceMapper = getMapperForParameters(asset, prefix, userId);

            if (referenceMapper != null) {
                // 2. Apply the mapping to the object, and retrieve the resulting EntityDetail
                summary = referenceMapper.getOMRSEntitySummary();
            } else {
                OMRSErrorCode errorCode = OMRSErrorCode.TYPEDEF_NOT_KNOWN_FOR_INSTANCE;
                String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(
                        prefix + asset.getType(),
                        "IGC asset",
                        methodName,
                        repositoryName);
                throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                        this.getClass().getName(),
                        methodName,
                        errorMessage,
                        errorCode.getSystemAction(),
                        errorCode.getUserAction());
            }

        }

        return summary;

    }

    /**
     * Return the header, classifications and properties of a specific entity.
     *
     * @param userId unique identifier for requesting user.
     * @param guid String unique identifier for the entity.
     * @return EntityDetail structure.
     * @throws InvalidParameterException the guid is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                 the metadata collection is stored.
     * @throws EntityNotKnownException the requested entity instance is not known in the metadata collection.
     * @throws EntityProxyOnlyException the requested entity instance is only a proxy in the metadata collection.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public EntityDetail getEntityDetail(String userId,
                                        String guid) throws InvalidParameterException,
            RepositoryErrorException,
            EntityNotKnownException,
            EntityProxyOnlyException,
            UserNotAuthorizedException
    {
        final String  methodName        = "getEntityDetail";
        final String  guidParameterName = "guid";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateGUID(repositoryName, guidParameterName, guid, methodName);

        /*
         * Perform operation
         */

        // Lookup the basic asset based on the RID (strip off prefix (indicating a generated type), if there)
        String rid = getRidFromGeneratedId(guid);
        Reference asset = this.igcRestClient.getAssetRefById(rid);

        return getEntityDetail(userId, guid, asset);

    }

    /**
     * Return the relationships for a specific entity. Note that currently this will only work for relationships known
     * to (originated within) IGC, and that not all parameters are (yet) implemented.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID String unique identifier for the entity.
     * @param relationshipTypeGUID String GUID of the the type of relationship required (null for all).
     * @param fromRelationshipElement the starting element number of the relationships to return.
     *                                This is used when retrieving elements
     *                                beyond the first page of results. Zero means start from the first element.
     * @param limitResultsByStatus Not implemented for IGC -- will only retrieve ACTIVE entities.
     * @param asOfTime Must be null (history not implemented for IGC).
     * @param sequencingProperty Must be null (there are no properties on IGC relationships).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param pageSize -- the maximum number of result classifications that can be returned on this request.  Zero means
     *                 unrestricted return results size.
     * @return Relationships list.  Null means no relationships associated with the entity.
     * @throws InvalidParameterException a parameter is invalid or null.
     * @throws TypeErrorException the type guid passed on the request is not known by the
     *                              metadata collection.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws EntityNotKnownException the requested entity instance is not known in the metadata collection.
     * @throws PropertyErrorException the sequencing property is not valid for the attached classifications.
     * @throws PagingErrorException the paging/sequencing parameters are set up incorrectly.
     * @throws FunctionNotSupportedException the repository does not support the asOfTime parameter.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public List<Relationship> getRelationshipsForEntity(String                     userId,
                                                        String                     entityGUID,
                                                        String                     relationshipTypeGUID,
                                                        int                        fromRelationshipElement,
                                                        List<InstanceStatus>       limitResultsByStatus,
                                                        Date                       asOfTime,
                                                        String                     sequencingProperty,
                                                        SequencingOrder            sequencingOrder,
                                                        int                        pageSize) throws InvalidParameterException,
            TypeErrorException,
            RepositoryErrorException,
            EntityNotKnownException,
            PropertyErrorException,
            PagingErrorException,
            FunctionNotSupportedException,
            UserNotAuthorizedException
    {
        final String  methodName = "getRelationshipsForEntity";
        final String  guidParameterName = "entityGUID";
        final String  typeGUIDParameter = "relationshipTypeGUID";
        final String  asOfTimeParameter = "asOfTime";
        final String  pageSizeParameter = "pageSize";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateGUID(repositoryName, guidParameterName, entityGUID, methodName);
        repositoryValidator.validateAsOfTime(repositoryName, asOfTimeParameter, asOfTime, methodName);
        repositoryValidator.validateOptionalTypeGUID(repositoryName, typeGUIDParameter, relationshipTypeGUID, methodName);
        repositoryValidator.validatePageSize(repositoryName, pageSizeParameter, pageSize, methodName);

        /*
         * Perform operation
         */
        ArrayList<Relationship> alRelationships = new ArrayList<>();

        // Immediately throw unimplemented exception if trying to retrieve historical view or sequence by property
        if (asOfTime != null) {
            OMRSErrorCode errorCode = OMRSErrorCode.METHOD_NOT_IMPLEMENTED;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                    this.getClass().getName(),
                    repositoryName);
            throw new FunctionNotSupportedException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        } else if (sequencingProperty != null
                || (sequencingOrder != null
                &&
                (sequencingOrder.equals(SequencingOrder.PROPERTY_ASCENDING)
                        || sequencingOrder.equals(SequencingOrder.PROPERTY_DESCENDING)))
        ) {
            OMRSErrorCode errorCode = OMRSErrorCode.METHOD_NOT_IMPLEMENTED;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                    this.getClass().getName(),
                    repositoryName);
            throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        } else if (limitResultsByStatus == null || limitResultsByStatus.equals(InstanceStatus.ACTIVE)) {

            // Otherwise, only bother searching if we are after ACTIVE (or "all") entities -- non-ACTIVE means we
            // will just return an empty list

            // 0. see if the entityGUID has a prefix (indicating a generated type)
            String rid = getRidFromGeneratedId(entityGUID);
            String prefix = getPrefixFromGeneratedId(entityGUID);

            // 1. retrieve entity from IGC by GUID (RID)
            Reference asset = this.igcRestClient.getAssetRefById(rid);

            // Ensure the entity actually exists (if not, throw error to that effect)
            if (asset == null) {
                OMRSErrorCode errorCode = OMRSErrorCode.ENTITY_NOT_KNOWN;
                String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                        this.getClass().getName(),
                        repositoryName);
                throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                        this.getClass().getName(),
                        methodName,
                        errorMessage,
                        errorCode.getSystemAction(),
                        errorCode.getUserAction());
            } else {

                ReferenceableMapper referenceMapper = getMapperForParameters(asset, prefix, userId);

                if (referenceMapper != null) {
                    // 2. Apply the mapping to the object, and retrieve the resulting relationships
                    alRelationships.addAll(
                            referenceMapper.getOMRSRelationships(
                                    relationshipTypeGUID,
                                    fromRelationshipElement,
                                    sequencingOrder,
                                    pageSize)
                    );
                } else {
                    OMRSErrorCode errorCode = OMRSErrorCode.TYPEDEF_NOT_KNOWN_FOR_INSTANCE;
                    String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(
                            prefix + asset.getType(),
                            "IGC asset",
                            methodName,
                            repositoryName);
                    throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                            this.getClass().getName(),
                            methodName,
                            errorMessage,
                            errorCode.getSystemAction(),
                            errorCode.getUserAction());
                }

            }

        }

        return alRelationships.isEmpty() ? null : alRelationships;

    }

    /**
     * Return a list of entities that match the supplied properties according to the match criteria.  The results
     * can be returned over many pages.
     *
     * @param userId unique identifier for requesting user.
     * @param entityTypeGUID String unique identifier for the entity type of interest (null means any entity type).
     * @param matchProperties List of entity properties to match to (null means match on entityTypeGUID only).
     * @param matchCriteria Enum defining how the properties should be matched to the entities in the repository.
     * @param fromEntityElement the starting element number of the entities to return.
     *                                This is used when retrieving elements
     *                                beyond the first page of results. Zero means start from the first element.
     * @param limitResultsByStatus Not implemented for IGC, only ACTIVE entities will be returned.
     * @param limitResultsByClassification List of classifications that must be present on all returned entities.
     * @param asOfTime Must be null (history not implemented for IGC).
     * @param sequencingProperty String name of the entity property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param pageSize the maximum number of result entities that can be returned on this request.  Zero means
     *                 unrestricted return results size.
     * @return a list of entities matching the supplied criteria where null means no matching entities in the metadata
     * collection.
     *
     * @throws InvalidParameterException a parameter is invalid or null.
     * @throws TypeErrorException the type guid passed on the request is not known by the
     *                              metadata collection.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws PropertyErrorException the properties specified are not valid for any of the requested types of
     *                                  entity.
     * @throws PagingErrorException the paging/sequencing parameters are set up incorrectly.
     * @throws FunctionNotSupportedException the repository does not support the asOfTime parameter.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public  List<EntityDetail> findEntitiesByProperty(String                    userId,
                                                      String                    entityTypeGUID,
                                                      InstanceProperties        matchProperties,
                                                      MatchCriteria             matchCriteria,
                                                      int                       fromEntityElement,
                                                      List<InstanceStatus>      limitResultsByStatus,
                                                      List<String>              limitResultsByClassification,
                                                      Date                      asOfTime,
                                                      String                    sequencingProperty,
                                                      SequencingOrder           sequencingOrder,
                                                      int                       pageSize) throws InvalidParameterException,
            TypeErrorException,
            RepositoryErrorException,
            PropertyErrorException,
            PagingErrorException,
            FunctionNotSupportedException,
            UserNotAuthorizedException
    {
        final String  methodName                   = "findEntitiesByProperty";
        final String  matchCriteriaParameterName   = "matchCriteria";
        final String  matchPropertiesParameterName = "matchProperties";
        final String  typeGUIDParameterName        = "entityTypeGUID";
        final String  asOfTimeParameter            = "asOfTime";
        final String  pageSizeParameter            = "pageSize";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateOptionalTypeGUID(repositoryName, typeGUIDParameterName, entityTypeGUID, methodName);
        repositoryValidator.validateAsOfTime(repositoryName, asOfTimeParameter, asOfTime, methodName);
        repositoryValidator.validatePageSize(repositoryName, pageSizeParameter, pageSize, methodName);
        repositoryValidator.validateMatchCriteria(repositoryName,
                matchCriteriaParameterName,
                matchPropertiesParameterName,
                matchCriteria,
                matchProperties,
                methodName);

        /*
         * Perform operation
         */
        ArrayList<EntityDetail> entityDetails = new ArrayList<>();

        // Immediately throw unimplemented exception if trying to retrieve historical view
        if (asOfTime != null) {
            OMRSErrorCode errorCode = OMRSErrorCode.METHOD_NOT_IMPLEMENTED;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                    this.getClass().getName(),
                    repositoryName);
            throw new FunctionNotSupportedException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        } else if (limitResultsByStatus == null || limitResultsByStatus.equals(InstanceStatus.ACTIVE)) {

            // Otherwise, only bother searching if we are after ACTIVE (or "all") entities -- non-ACTIVE means we
            // will just return an empty list

            List<ImplementedMapping> mappingsToSearch = getMappingsToSearch(entityTypeGUID, userId);

            // Now iterate through all of the mappings we need to search, construct and run an appropriate search
            // for each one
            for (ImplementedMapping mapping : mappingsToSearch) {

                IGCSearch igcSearch = new IGCSearch();
                igcSearch.addType(mapping.getIgcAssetType());

                /* We need to first retrieve the mapping so we know how to translate
                 * the provided OMRS property names to IGC property names */
                PropertyMappingSet propertyMappingSet = getEntityPropertiesFromMapping(mapping, userId);

                /* Provided there is a mapping, build up a list of IGC-specific properties
                 * and search criteria, based on the values of the InstanceProperties provided */
                ArrayList<String> properties = new ArrayList<>();
                IGCSearchConditionSet igcSearchConditionSet = new IGCSearchConditionSet();

                Iterator iPropertyNames = matchProperties.getPropertyNames();
                while (propertyMappingSet != null && iPropertyNames.hasNext()) {
                    String omrsPropertyName = (String) iPropertyNames.next();
                    InstancePropertyValue value = matchProperties.getPropertyValue(omrsPropertyName);
                    addSearchConditionFromValue(
                            igcSearchConditionSet,
                            omrsPropertyName,
                            properties,
                            propertyMappingSet,
                            value
                    );
                }

                IGCSearchSorting igcSearchSorting = null;
                if (sequencingProperty == null && sequencingOrder != null) {
                    igcSearchSorting = IGCSearchSorting.sortFromNonPropertySequencingOrder(sequencingOrder);
                }

                if (matchCriteria != null) {
                    switch (matchCriteria) {
                        case ALL:
                            igcSearchConditionSet.setMatchAnyCondition(false);
                            break;
                        case ANY:
                            igcSearchConditionSet.setMatchAnyCondition(true);
                            break;
                        case NONE:
                            igcSearchConditionSet.setMatchAnyCondition(false);
                            igcSearchConditionSet.setNegateAll(true);
                            break;
                    }
                }

                igcSearch.addProperties(properties);
                igcSearch.addConditions(igcSearchConditionSet);

                setPagingForSearch(igcSearch, fromEntityElement, pageSize);

                if (igcSearchSorting != null) {
                    igcSearch.addSortingCriteria(igcSearchSorting);
                }

                processResults(this.igcRestClient.search(igcSearch), entityDetails, pageSize, userId);

            }

        }

        return entityDetails.isEmpty() ? null : entityDetails;

    }

    /**
     * Return a list of entities that have the requested type of classification attached.
     *
     * @param userId unique identifier for requesting user.
     * @param entityTypeGUID unique identifier for the type of entity requested.  Null mans any type of entity.
     * @param classificationName name of the classification a null is not valid.
     * @param matchClassificationProperties list of classification properties used to narrow the search.
     * @param matchCriteria Enum defining how the properties should be matched to the classifications in the repository.
     * @param fromEntityElement the starting element number of the entities to return.
     *                                This is used when retrieving elements
     *                                beyond the first page of results. Zero means start from the first element.
     * @param limitResultsByStatus Not implemented for IGC, only ACTIVE entities will be returned.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values. (not implemented
     *                 for IGC, must be null)
     * @param sequencingProperty String name of the entity property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param pageSize the maximum number of result entities that can be returned on this request.  Zero means
     *                 unrestricted return results size.
     * @return a list of entities matching the supplied criteria where null means no matching entities in the metadata
     * collection.
     * @throws InvalidParameterException a parameter is invalid or null.
     * @throws TypeErrorException the type guid passed on the request is not known by the
     *                              metadata collection.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws ClassificationErrorException the classification request is not known to the metadata collection.
     * @throws PropertyErrorException the properties specified are not valid for the requested type of
     *                                  classification.
     * @throws PagingErrorException the paging/sequencing parameters are set up incorrectly.
     * @throws FunctionNotSupportedException the repository does not support the asOfTime parameter.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public  List<EntityDetail> findEntitiesByClassification(String                    userId,
                                                            String                    entityTypeGUID,
                                                            String                    classificationName,
                                                            InstanceProperties        matchClassificationProperties,
                                                            MatchCriteria             matchCriteria,
                                                            int                       fromEntityElement,
                                                            List<InstanceStatus>      limitResultsByStatus,
                                                            Date                      asOfTime,
                                                            String                    sequencingProperty,
                                                            SequencingOrder           sequencingOrder,
                                                            int                       pageSize) throws InvalidParameterException,
            TypeErrorException,
            RepositoryErrorException,
            ClassificationErrorException,
            PropertyErrorException,
            PagingErrorException,
            FunctionNotSupportedException,
            UserNotAuthorizedException
    {
        final String  methodName                   = "findEntitiesByClassification";
        final String  classificationParameterName  = "classificationName";
        final String  entityTypeGUIDParameterName  = "entityTypeGUID";

        final String  matchCriteriaParameterName   = "matchCriteria";
        final String  matchPropertiesParameterName = "matchClassificationProperties";
        final String  asOfTimeParameter            = "asOfTime";
        final String  pageSizeParameter            = "pageSize";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateOptionalTypeGUID(repositoryName, entityTypeGUIDParameterName, entityTypeGUID, methodName);
        repositoryValidator.validateAsOfTime(repositoryName, asOfTimeParameter, asOfTime, methodName);
        repositoryValidator.validatePageSize(repositoryName, pageSizeParameter, pageSize, methodName);

        /*
         * Validate TypeDef
         */
        if (entityTypeGUID != null)
        {
            TypeDef entityTypeDef = repositoryHelper.getTypeDef(repositoryName,
                    entityTypeGUIDParameterName,
                    entityTypeGUID,
                    methodName);

            repositoryValidator.validateTypeDefForInstance(repositoryName,
                    entityTypeGUIDParameterName,
                    entityTypeDef,
                    methodName);

            repositoryValidator.validateClassification(repositoryName,
                    classificationParameterName,
                    classificationName,
                    entityTypeDef.getName(),
                    methodName);
        }

        repositoryValidator.validateMatchCriteria(repositoryName,
                matchCriteriaParameterName,
                matchPropertiesParameterName,
                matchCriteria,
                matchClassificationProperties,
                methodName);

        ArrayList<EntityDetail> entityDetails = new ArrayList<>();

        // Immediately throw unimplemented exception if trying to retrieve historical view
        if (asOfTime != null) {
            OMRSErrorCode errorCode = OMRSErrorCode.METHOD_NOT_IMPLEMENTED;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                    this.getClass().getName(),
                    repositoryName);
            throw new FunctionNotSupportedException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        } else if (limitResultsByStatus == null || limitResultsByStatus.equals(InstanceStatus.ACTIVE)) {

            // Otherwise, only bother searching if we are after ACTIVE (or "all") entities -- non-ACTIVE means we
            // will just return an empty list

            List<ImplementedMapping> mappingsToSearch = getMappingsToSearch(entityTypeGUID, userId);

            // Now iterate through all of the mappings we need to search, construct and run an appropriate search
            // for each one
            for (ImplementedMapping mapping : mappingsToSearch) {

                ClassificationMapping foundMapping = null;

                // Check which classifications (if any) are implemented for the entity mapping
                List<ClassificationMapping> classificationMappings = mapping.getEntityMapping().getClassificationMappers();
                for (ClassificationMapping classificationMapping : classificationMappings) {

                    // Check whether the implemented classification matches the one we're searching based on
                    String candidateName = classificationMapping.getOmrsClassificationType();
                    if (candidateName.equals(classificationName)) {
                        foundMapping = classificationMapping;
                        break;
                    }

                }

                // Only proceed if we have found a classification mapping for this entity that matches the search
                // criteria provided
                if (foundMapping != null) {

                    IGCSearch igcSearch = new IGCSearch();
                    igcSearch.addType(mapping.getIgcAssetType());
                    IGCSearchConditionSet igcSearchConditionSet = new IGCSearchConditionSet();

                    // Retrieve the list of property names we need for the classification
                    List<String> igcClassificationProperties = foundMapping.getIgcRelationshipProperties();

                    // Compose the search criteria for the classification as a set of nested conditions, so that
                    // matchCriteria does not change the meaning of what we're searching
                    IGCSearchConditionSet baseCriteria = foundMapping.getIGCSearchCriteria(matchClassificationProperties);
                    igcSearchConditionSet.addNestedConditionSet(baseCriteria);

                    IGCSearchSorting igcSearchSorting = null;
                    if (sequencingProperty == null && sequencingOrder != null) {
                        igcSearchSorting = IGCSearchSorting.sortFromNonPropertySequencingOrder(sequencingOrder);
                    }

                    if (matchCriteria != null) {
                        switch (matchCriteria) {
                            case ALL:
                                igcSearchConditionSet.setMatchAnyCondition(false);
                                break;
                            case ANY:
                                igcSearchConditionSet.setMatchAnyCondition(true);
                                break;
                            case NONE:
                                igcSearchConditionSet.setMatchAnyCondition(false);
                                igcSearchConditionSet.setNegateAll(true);
                                break;
                        }
                    }

                    igcSearch.addProperties(igcClassificationProperties);
                    igcSearch.addConditions(igcSearchConditionSet);

                    setPagingForSearch(igcSearch, fromEntityElement, pageSize);

                    if (igcSearchSorting != null) {
                        igcSearch.addSortingCriteria(igcSearchSorting);
                    }

                    processResults(this.igcRestClient.search(igcSearch), entityDetails, pageSize, userId);

                } else {
                    log.info("No classification mapping has been implemented for {} on entity {} -- skipping from search.", classificationName, mapping.getTypeDef().getName());
                }

            }

        }

        return entityDetails.isEmpty() ? null : entityDetails;

    }

    /**
     * Return a list of entities whose string based property values match the search criteria.  The
     * search criteria may include regex style wild cards.
     *
     * @param userId unique identifier for requesting user.
     * @param entityTypeGUID GUID of the type of entity to search for. Null means all types will
     *                       be searched (could be slow so not recommended).
     * @param searchCriteria String expression contained in any of the property values within the entities
     *                       of the supplied type.
     * @param fromEntityElement the starting element number of the entities to return.
     *                                This is used when retrieving elements
     *                                beyond the first page of results. Zero means start from the first element.
     * @param limitResultsByStatus Not implemented for IGC, only ACTIVE entities will be returned.
     * @param limitResultsByClassification List of classifications that must be present on all returned entities.
     *                                     (currently not implemented for IGC)
     * @param asOfTime Must be null (history not implemented for IGC).
     * @param sequencingProperty String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     *                           (currently not implemented for IGC)
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param pageSize the maximum number of result entities that can be returned on this request.  Zero means
     *                 unrestricted return results size.
     * @return a list of entities matching the supplied criteria; null means no matching entities in the metadata
     * collection.
     * @throws InvalidParameterException a parameter is invalid or null.
     * @throws TypeErrorException the type guid passed on the request is not known by the
     *                              metadata collection.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws PropertyErrorException the sequencing property specified is not valid for any of the requested types of
     *                                  entity.
     * @throws PagingErrorException the paging/sequencing parameters are set up incorrectly.
     * @throws FunctionNotSupportedException the repository does not support the asOfTime parameter.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public  List<EntityDetail> findEntitiesByPropertyValue(String                userId,
                                                           String                entityTypeGUID,
                                                           String                searchCriteria,
                                                           int                   fromEntityElement,
                                                           List<InstanceStatus>  limitResultsByStatus,
                                                           List<String>          limitResultsByClassification,
                                                           Date                  asOfTime,
                                                           String                sequencingProperty,
                                                           SequencingOrder       sequencingOrder,
                                                           int                   pageSize) throws InvalidParameterException,
            TypeErrorException,
            RepositoryErrorException,
            PropertyErrorException,
            PagingErrorException,
            FunctionNotSupportedException,
            UserNotAuthorizedException
    {
        final String  methodName = "findEntitiesByPropertyValue";
        final String  searchCriteriaParameterName = "searchCriteria";
        final String  typeGUIDParameter = "entityTypeGUID";
        final String  asOfTimeParameter = "asOfTime";
        final String  pageSizeParameter = "pageSize";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateSearchCriteria(repositoryName, searchCriteriaParameterName, searchCriteria, methodName);
        repositoryValidator.validateOptionalTypeGUID(repositoryName, typeGUIDParameter, entityTypeGUID, methodName);
        repositoryValidator.validateAsOfTime(repositoryName, asOfTimeParameter, asOfTime, methodName);
        repositoryValidator.validatePageSize(repositoryName, pageSizeParameter, pageSize, methodName);

        /*
         * Process operation
         */
        ArrayList<EntityDetail> entityDetails = new ArrayList<>();

        // Immediately throw unimplemented exception if trying to retrieve historical view
        if (asOfTime != null) {
            OMRSErrorCode errorCode = OMRSErrorCode.METHOD_NOT_IMPLEMENTED;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                    this.getClass().getName(),
                    repositoryName);
            throw new FunctionNotSupportedException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        } else if (limitResultsByStatus == null || limitResultsByStatus.equals(InstanceStatus.ACTIVE)) {

            // Otherwise, only bother searching if we are after ACTIVE (or "all") entities -- non-ACTIVE means we
            // will just return an empty list

            List<ImplementedMapping> mappingsToSearch = getMappingsToSearch(entityTypeGUID, userId);

            // Now iterate through all of the mappings we need to search, construct and run an appropriate search
            // for each one
            for (ImplementedMapping mapping : mappingsToSearch) {

                IGCSearch igcSearch = new IGCSearch();
                String igcAssetType = addTypeToSearch(mapping, igcSearch);

                // Get POJO from the asset type, and use this to retrieve a listing of all string properties
                // for that asset type -- these are the list of properties we should use for the search
                Class pojo = igcRestClient.getPOJOForType(igcAssetType);

                if (pojo != null) {

                    List<String> properties = Reference.getStringPropertiesFromPOJO(pojo);
                    // POST'd search to IGC doesn't work on v11.7.0.2 using long_description
                    // Using "searchText" requires using "searchProperties" (no "where" conditions) -- but does not
                    // work with 'main_object', must be used with a specific asset type
                    // Therefore for v11.7.0.2 we will simply drop long_description from the fields we search
                    if (igcRestClient.getIgcVersion().isEqualTo(IGCVersionEnum.V11702)) {
                        ArrayList<String> propertiesWithoutLongDescription = new ArrayList<>();
                        for (String property : properties) {
                            if (!property.equals("long_description")) {
                                propertiesWithoutLongDescription.add(property);
                            }
                        }
                        properties = propertiesWithoutLongDescription;
                    }

                    IGCSearchSorting igcSearchSorting = null;
                    if (sequencingProperty == null && sequencingOrder != null) {
                        igcSearchSorting = IGCSearchSorting.sortFromNonPropertySequencingOrder(sequencingOrder);
                    }

                    IGCSearchConditionSet igcSearchConditionSet = new IGCSearchConditionSet();
                    igcSearchConditionSet.setMatchAnyCondition(true);
                    for (String property : properties) {
                        igcSearchConditionSet.addCondition(new IGCSearchCondition(
                                property,
                                "like %{0}%",
                                searchCriteria
                        ));
                    }

                    igcSearch.addConditions(igcSearchConditionSet);

                    setPagingForSearch(igcSearch, fromEntityElement, pageSize);

                    if (igcSearchSorting != null) {
                        igcSearch.addSortingCriteria(igcSearchSorting);
                    }

                    processResults(this.igcRestClient.search(igcSearch), entityDetails, pageSize, userId);

                } else {
                    log.warn("Unable to find POJO to handle IGC asset type '{}' -- skipping search against this asset type.", igcAssetType);
                }

            }

        }

        return entityDetails.isEmpty() ? null : entityDetails;

    }

    /**
     * Returns a boolean indicating if the relationship is stored in the metadata collection.
     *
     * @param userId unique identifier for requesting user.
     * @param guid String unique identifier for the relationship.
     * @return relationship details if the relationship is found in the metadata collection; otherwise return null.
     * @throws InvalidParameterException the guid is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public Relationship  isRelationshipKnown(String     userId,
                                             String     guid) throws InvalidParameterException,
            RepositoryErrorException,
            UserNotAuthorizedException
    {
        final String  methodName = "isRelationshipKnown";
        final String  guidParameterName = "guid";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateGUID(repositoryName, guidParameterName, guid, methodName);

        /*
         * Process operation
         */
        Relationship relationship = null;
        try {
            relationship = getRelationship(userId, guid);
        } catch (RelationshipNotKnownException e) {
            log.info("Could not find relationship {} in repository.", guid, e);
        }
        return relationship;
    }

    /**
     * Return a requested relationship. Note that currently this will only work for relationships known to
     * (originated within) IGC.
     *
     * @param userId unique identifier for requesting user.
     * @param guid String unique identifier for the relationship.
     * @return a relationship structure.
     * @throws InvalidParameterException the guid is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws RelationshipNotKnownException the metadata collection does not have a relationship with
     *                                         the requested GUID stored.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public Relationship getRelationship(String    userId,
                                        String    guid) throws InvalidParameterException,
            RepositoryErrorException,
            RelationshipNotKnownException,
            UserNotAuthorizedException
    {
        final String  methodName = "getRelationship";
        final String  guidParameterName = "guid";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateGUID(repositoryName, guidParameterName, guid, methodName);

        /*
         * Process operation
         */

        // Translate the key properties of the GUID into IGC-retrievables
        String proxyOneRid = RelationshipMapping.getProxyOneGUIDFromRelationshipGUID(guid);
        String proxyTwoRid = RelationshipMapping.getProxyTwoGUIDFromRelationshipGUID(guid);
        String omrsRelationshipName = RelationshipMapping.getRelationshipTypeFromRelationshipGUID(guid);

        String proxyOneIgcRid = proxyOneRid;
        String proxyTwoIgcRid = proxyTwoRid;

        if (isGeneratedGUID(proxyOneRid)) {
            proxyOneIgcRid = getRidFromGeneratedId(proxyOneRid);
        }
        if (isGeneratedGUID(proxyTwoRid)) {
            proxyTwoIgcRid = getRidFromGeneratedId(proxyTwoRid);
        }

        log.debug("Looking up relationship: {}", guid);

        // Should not need to translate from proxyone / proxytwo to alternative assets, as the RIDs provided
        // in the relationship GUID should already be pointing to the correct assets
        String relationshipLevelRid = (proxyOneRid.equals(proxyTwoRid)) ? proxyOneRid : null;
        Reference proxyOne;
        Reference proxyTwo;
        RelationshipMapping relationshipMapping;
        if (relationshipLevelRid != null) {
            Reference relationshipAsset = igcRestClient.getAssetRefById(proxyOneIgcRid);
            String relationshipAssetType = relationshipAsset.getType();
            relationshipMapping = getRelationshipMapper(
                    omrsRelationshipName,
                    relationshipAssetType,
                    relationshipAssetType
            );
            // Only need to translate if the RIDs are relationship-level RIDs
            proxyOne = relationshipMapping.getProxyOneAssetFromAsset(relationshipAsset, igcRestClient).get(0);
            proxyTwo = relationshipMapping.getProxyTwoAssetFromAsset(relationshipAsset, igcRestClient).get(0);
        } else {
            proxyOne = igcRestClient.getAssetRefById(proxyOneIgcRid);
            proxyTwo = igcRestClient.getAssetRefById(proxyTwoIgcRid);
            relationshipMapping = getRelationshipMapper(
                    omrsRelationshipName,
                    proxyOne.getType(),
                    proxyTwo.getType()
            );
        }

        Relationship found = null;

        if (relationshipMapping != null) {
            try {

                RelationshipDef omrsRelationshipDef = (RelationshipDef) getTypeDefByName(userId, omrsRelationshipName);
                // Since the ordering should be set by the GUID we're lookup up anyway, we'll simply set the property
                // to one of the proxyOne properties
                String igcPropertyName = relationshipMapping.getProxyOneMapping().getIgcRelationshipProperties().get(0);
                log.debug(" ... using property: {}", igcPropertyName);
                found = RelationshipMapping.getMappedRelationship(
                        (IGCOMRSRepositoryConnector)parentConnector,
                        relationshipMapping,
                        omrsRelationshipDef,
                        proxyOne,
                        proxyTwo,
                        igcPropertyName,
                        userId,
                        relationshipLevelRid
                );

            } catch (TypeDefNotKnownException e) {
                OMRSErrorCode errorCode = OMRSErrorCode.TYPEDEF_NOT_KNOWN;
                String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(
                        omrsRelationshipName,
                        guid,
                        guidParameterName,
                        methodName,
                        repositoryName);
                throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                        this.getClass().getName(),
                        methodName,
                        errorMessage,
                        errorCode.getSystemAction(),
                        errorCode.getUserAction());
            }
        } else {
            OMRSErrorCode errorCode = OMRSErrorCode.TYPEDEF_NOT_KNOWN;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(
                    omrsRelationshipName,
                    guid,
                    guidParameterName,
                    methodName,
                    repositoryName);
            throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }

        return found;

    }

    /**
     * Return a list of relationships that match the requested properties by the matching criteria.   The results
     * can be broken into pages.
     *
     * @param userId unique identifier for requesting user
     * @param relationshipTypeGUID unique identifier (guid) for the new relationship's type.
     * @param matchProperties list of  properties used to narrow the search.
     * @param matchCriteria Enum defining how the properties should be matched to the relationships in the repository.
     * @param fromRelationshipElement the starting element number of the entities to return.
     *                                This is used when retrieving elements
     *                                beyond the first page of results. Zero means start from the first element.
     * @param limitResultsByStatus By default, relationships in all statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                             status values.
     * @param asOfTime Requests a historical query of the relationships for the entity.  Null means return the
     *                 present values.
     * @param sequencingProperty String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param pageSize the maximum number of result relationships that can be returned on this request.  Zero means
     *                 unrestricted return results size.
     * @return a list of relationships.  Null means no matching relationships.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws TypeErrorException the type guid passed on the request is not known by the
     *                              metadata collection.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws PropertyErrorException the properties specified are not valid for any of the requested types of
     *                                  relationships.
     * @throws PagingErrorException the paging/sequencing parameters are set up incorrectly.
     * @throws FunctionNotSupportedException the repository does not support the asOfTime parameter.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public  List<Relationship> findRelationshipsByProperty(String                    userId,
                                                           String                    relationshipTypeGUID,
                                                           InstanceProperties        matchProperties,
                                                           MatchCriteria             matchCriteria,
                                                           int                       fromRelationshipElement,
                                                           List<InstanceStatus>      limitResultsByStatus,
                                                           Date                      asOfTime,
                                                           String                    sequencingProperty,
                                                           SequencingOrder           sequencingOrder,
                                                           int                       pageSize) throws InvalidParameterException,
            TypeErrorException,
            RepositoryErrorException,
            PropertyErrorException,
            PagingErrorException,
            FunctionNotSupportedException,
            UserNotAuthorizedException
    {
        final String  methodName = "findRelationshipsByProperty";
        final String  matchCriteriaParameterName = "matchCriteria";
        final String  matchPropertiesParameterName = "matchProperties";
        final String  guidParameterName = "relationshipTypeGUID";
        final String  asOfTimeParameter = "asOfTime";
        final String  pageSizeParameter = "pageSize";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateOptionalTypeGUID(repositoryName, guidParameterName, relationshipTypeGUID, methodName);
        repositoryValidator.validateAsOfTime(repositoryName, asOfTimeParameter, asOfTime, methodName);
        repositoryValidator.validatePageSize(repositoryName, pageSizeParameter, pageSize, methodName);
        repositoryValidator.validateMatchCriteria(repositoryName,
                matchCriteriaParameterName,
                matchPropertiesParameterName,
                matchCriteria,
                matchProperties,
                methodName);

        /*
         * Perform operation
         */
        // TODO: implement
        OMRSErrorCode errorCode = OMRSErrorCode.METHOD_NOT_IMPLEMENTED;

        String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                this.getClass().getName(),
                repositoryName);

        if (asOfTime == null)
        {
            throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }
        else
        {
            throw new FunctionNotSupportedException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }
    }

    /**
     * Return a list of relationships whose string based property values match the search criteria.  The
     * search criteria may include regex style wild cards.
     *
     * @param userId unique identifier for requesting user.
     * @param relationshipTypeGUID GUID of the type of entity to search for. Null means all types will
     *                       be searched (could be slow so not recommended).
     * @param searchCriteria String expression contained in any of the property values within the entities
     *                       of the supplied type.
     * @param fromRelationshipElement Element number of the results to skip to when building the results list
     *                                to return.  Zero means begin at the start of the results.  This is used
     *                                to retrieve the results over a number of pages.
     * @param limitResultsByStatus By default, relationships in all statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                             status values.
     * @param asOfTime Requests a historical query of the relationships for the entity.  Null means return the
     *                 present values.
     * @param sequencingProperty String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param pageSize the maximum number of result relationships that can be returned on this request.  Zero means
     *                 unrestricted return results size.
     * @return a list of relationships.  Null means no matching relationships.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws TypeErrorException the type guid passed on the request is not known by the
     *                              metadata collection.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws PropertyErrorException there is a problem with one of the other parameters.
     * @throws PagingErrorException the paging/sequencing parameters are set up incorrectly.
     * @throws FunctionNotSupportedException the repository does not support the asOfTime parameter.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public  List<Relationship> findRelationshipsByPropertyValue(String                    userId,
                                                                String                    relationshipTypeGUID,
                                                                String                    searchCriteria,
                                                                int                       fromRelationshipElement,
                                                                List<InstanceStatus>      limitResultsByStatus,
                                                                Date                      asOfTime,
                                                                String                    sequencingProperty,
                                                                SequencingOrder           sequencingOrder,
                                                                int                       pageSize) throws InvalidParameterException,
            TypeErrorException,
            RepositoryErrorException,
            PropertyErrorException,
            PagingErrorException,
            FunctionNotSupportedException,
            UserNotAuthorizedException
    {
        final String  methodName = "findRelationshipsByPropertyName";
        final String  asOfTimeParameter = "asOfTime";
        final String  typeGUIDParameter = "relationshipTypeGUID";
        final String  pageSizeParameter = "pageSize";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateOptionalTypeGUID(repositoryName, typeGUIDParameter, relationshipTypeGUID, methodName);
        repositoryValidator.validateAsOfTime(repositoryName, asOfTimeParameter, asOfTime, methodName);
        repositoryValidator.validatePageSize(repositoryName, pageSizeParameter, pageSize, methodName);

        /*
         * Perform operation
         */
        // TODO: implement
        OMRSErrorCode errorCode = OMRSErrorCode.METHOD_NOT_IMPLEMENTED;

        String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                this.getClass().getName(),
                repositoryName);

        if (asOfTime == null)
        {
            throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }
        else
        {
            throw new FunctionNotSupportedException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }
    }


    /**
     * Return all of the relationships and intermediate entities that connect the startEntity with the endEntity.
     *
     * @param userId unique identifier for requesting user.
     * @param startEntityGUID The entity that is used to anchor the query.
     * @param endEntityGUID the other entity that defines the scope of the query.
     * @param limitResultsByStatus By default, relationships in all statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                             status values.
     * @param asOfTime Requests a historical query of the relationships for the entity.  Null means return the
     *                 present values.
     * @return InstanceGraph the sub-graph that represents the returned linked entities and their relationships.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by either the startEntityGUID or the endEntityGUID
     *                                   is not found in the metadata collection.
     * @throws PropertyErrorException there is a problem with one of the other parameters.
     * @throws FunctionNotSupportedException the repository does not support the asOfTime parameter.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public  InstanceGraph getLinkingEntities(String                    userId,
                                             String                    startEntityGUID,
                                             String                    endEntityGUID,
                                             List<InstanceStatus>      limitResultsByStatus,
                                             Date                      asOfTime) throws InvalidParameterException,
            RepositoryErrorException,
            EntityNotKnownException,
            PropertyErrorException,
            FunctionNotSupportedException,
            UserNotAuthorizedException
    {
        final String methodName                   = "getLinkingEntities";
        final String startEntityGUIDParameterName = "startEntityGUID";
        final String endEntityGUIDParameterName   = "entityGUID";
        final String asOfTimeParameter            = "asOfTime";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateGUID(repositoryName, startEntityGUIDParameterName, startEntityGUID, methodName);
        repositoryValidator.validateGUID(repositoryName, endEntityGUIDParameterName, endEntityGUID, methodName);
        repositoryValidator.validateAsOfTime(repositoryName, asOfTimeParameter, asOfTime, methodName);

        /*
         * Perform operation
         */
        // TODO: implement
        OMRSErrorCode errorCode = OMRSErrorCode.METHOD_NOT_IMPLEMENTED;

        String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                this.getClass().getName(),
                repositoryName);

        throw new FunctionNotSupportedException(errorCode.getHTTPErrorCode(),
                this.getClass().getName(),
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction());
    }


    /**
     * Return the entities and relationships that radiate out from the supplied entity GUID.
     * The results are scoped both the instance type guids and the level.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID the starting point of the query.
     * @param entityTypeGUIDs list of entity types to include in the query results.  Null means include
     *                          all entities found, irrespective of their type.
     * @param relationshipTypeGUIDs list of relationship types to include in the query results.  Null means include
     *                                all relationships found, irrespective of their type.
     * @param limitResultsByStatus By default, relationships in all statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                             status values.
     * @param limitResultsByClassification List of classifications that must be present on all returned entities.
     * @param asOfTime Requests a historical query of the relationships for the entity.  Null means return the
     *                 present values.
     * @param level the number of the relationships out from the starting entity that the query will traverse to
     *              gather results.
     * @return InstanceGraph the sub-graph that represents the returned linked entities and their relationships.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws TypeErrorException the type guid passed on the request is not known by the
     *                              metadata collection.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by the entityGUID is not found in the metadata collection.
     * @throws PropertyErrorException there is a problem with one of the other parameters.
     * @throws FunctionNotSupportedException the repository does not support the asOfTime parameter.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public  InstanceGraph getEntityNeighborhood(String               userId,
                                                String               entityGUID,
                                                List<String>         entityTypeGUIDs,
                                                List<String>         relationshipTypeGUIDs,
                                                List<InstanceStatus> limitResultsByStatus,
                                                List<String>         limitResultsByClassification,
                                                Date                 asOfTime,
                                                int                  level) throws InvalidParameterException,
            TypeErrorException,
            RepositoryErrorException,
            EntityNotKnownException,
            PropertyErrorException,
            FunctionNotSupportedException,
            UserNotAuthorizedException
    {
        final String methodName                                  = "getEntityNeighborhood";
        final String entityGUIDParameterName                     = "entityGUID";
        final String entityTypeGUIDParameterName                 = "entityTypeGUIDs";
        final String relationshipTypeGUIDParameterName           = "relationshipTypeGUIDs";
        final String limitedResultsByClassificationParameterName = "limitResultsByClassification";
        final String asOfTimeParameter                           = "asOfTime";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateGUID(repositoryName, entityGUIDParameterName, entityGUID, methodName);
        repositoryValidator.validateAsOfTime(repositoryName, asOfTimeParameter, asOfTime, methodName);

        if (entityTypeGUIDs != null)
        {
            for (String guid : entityTypeGUIDs)
            {
                repositoryValidator.validateTypeGUID(repositoryName, entityTypeGUIDParameterName, guid, methodName);
            }
        }

        if (relationshipTypeGUIDs != null)
        {
            for (String guid : relationshipTypeGUIDs)
            {
                repositoryValidator.validateTypeGUID(repositoryName, relationshipTypeGUIDParameterName, guid, methodName);
            }
        }

        if (limitResultsByClassification != null)
        {
            for (String classificationName : limitResultsByClassification)
            {
                repositoryValidator.validateClassificationName(repositoryName,
                        limitedResultsByClassificationParameterName,
                        classificationName,
                        methodName);
            }
        }

        /*
         * Perform operation
         */
        // TODO: implement
        OMRSErrorCode errorCode = OMRSErrorCode.METHOD_NOT_IMPLEMENTED;

        String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                this.getClass().getName(),
                repositoryName);

        throw new FunctionNotSupportedException(errorCode.getHTTPErrorCode(),
                this.getClass().getName(),
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction());
    }


    /**
     * Return the list of entities that are of the types listed in entityTypeGUIDs and are connected, either directly or
     * indirectly to the entity identified by startEntityGUID.
     *
     * @param userId unique identifier for requesting user.
     * @param startEntityGUID unique identifier of the starting entity.
     * @param entityTypeGUIDs list of types to search for.  Null means any type.
     * @param fromEntityElement starting element for results list.  Used in paging.  Zero means first element.
     * @param limitResultsByStatus By default, relationships in all statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                             status values.
     * @param limitResultsByClassification List of classifications that must be present on all returned entities.
     * @param asOfTime Requests a historical query of the relationships for the entity.  Null means return the
     *                 present values.
     * @param sequencingProperty String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param pageSize the maximum number of result entities that can be returned on this request.  Zero means
     *                 unrestricted return results size.
     * @return list of entities either directly or indirectly connected to the start entity
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws TypeErrorException one of the type guids passed on the request is not known by the
     *                              metadata collection.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by the startEntityGUID
     *                                   is not found in the metadata collection.
     * @throws PropertyErrorException the sequencing property specified is not valid for any of the requested types of
     *                                  entity.
     * @throws PagingErrorException the paging/sequencing parameters are set up incorrectly.
     * @throws FunctionNotSupportedException the repository does not support the asOfTime parameter.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public  List<EntityDetail> getRelatedEntities(String               userId,
                                                  String               startEntityGUID,
                                                  List<String>         entityTypeGUIDs,
                                                  int                  fromEntityElement,
                                                  List<InstanceStatus> limitResultsByStatus,
                                                  List<String>         limitResultsByClassification,
                                                  Date                 asOfTime,
                                                  String               sequencingProperty,
                                                  SequencingOrder      sequencingOrder,
                                                  int                  pageSize) throws InvalidParameterException,
            TypeErrorException,
            RepositoryErrorException,
            EntityNotKnownException,
            PropertyErrorException,
            PagingErrorException,
            FunctionNotSupportedException,
            UserNotAuthorizedException
    {
        final String  methodName = "getRelatedEntities";
        final String  entityGUIDParameterName  = "startEntityGUID";
        final String  instanceTypesParameter = "instanceTypes";
        final String  asOfTimeParameter = "asOfTime";
        final String  pageSizeParameter = "pageSize";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateGUID(repositoryName, entityGUIDParameterName, startEntityGUID, methodName);
        repositoryValidator.validateAsOfTime(repositoryName, asOfTimeParameter, asOfTime, methodName);
        repositoryValidator.validatePageSize(repositoryName, pageSizeParameter, pageSize, methodName);

        if (entityTypeGUIDs != null)
        {
            for (String guid : entityTypeGUIDs)
            {
                repositoryValidator.validateTypeGUID(repositoryName, instanceTypesParameter, guid, methodName);
            }
        }

        /*
         * Perform operation
         */
        // TODO: implement
        OMRSErrorCode errorCode = OMRSErrorCode.METHOD_NOT_IMPLEMENTED;

        String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                this.getClass().getName(),
                repositoryName);

        throw new FunctionNotSupportedException(errorCode.getHTTPErrorCode(),
                this.getClass().getName(),
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction());
    }

    /**
     * Create a new entity and put it in the requested state.  The new entity is returned.
     *
     * @param userId unique identifier for requesting user.
     * @param entityTypeGUID unique identifier (guid) for the new entity's type.
     * @param initialProperties initial list of properties for the new entity; null means no properties.
     * @param initialClassifications initial list of classifications for the new entity; null means no classifications.
     * @param initialStatus initial status typically set to DRAFT, PREPARED or ACTIVE.
     * @return EntityDetail showing the new header plus the requested properties and classifications.  The entity will
     * not have any relationships at this stage.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws TypeErrorException the requested type is not known, or not supported in the metadata repository
     *                              hosting the metadata collection.
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                  characteristics in the TypeDef for this entity's type.
     * @throws ClassificationErrorException one or more of the requested classifications are either not known or
     *                                           not defined for this entity type.
     * @throws StatusNotSupportedException the metadata repository hosting the metadata collection does not support
     *                                       the requested status.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public EntityDetail addEntity(String                     userId,
                                  String                     entityTypeGUID,
                                  InstanceProperties         initialProperties,
                                  List<Classification>       initialClassifications,
                                  InstanceStatus             initialStatus) throws InvalidParameterException,
            RepositoryErrorException,
            TypeErrorException,
            PropertyErrorException,
            ClassificationErrorException,
            StatusNotSupportedException,
            UserNotAuthorizedException
    {
        final String  methodName                    = "addEntity";
        final String  entityGUIDParameterName       = "entityTypeGUID";
        final String  propertiesParameterName       = "initialProperties";
        final String  classificationsParameterName  = "initialClassifications";
        final String  initialStatusParameterName    = "initialStatus";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateTypeGUID(repositoryName, entityGUIDParameterName, entityTypeGUID, methodName);

        TypeDef  typeDef = repositoryHelper.getTypeDef(repositoryName, entityGUIDParameterName, entityTypeGUID, methodName);

        repositoryValidator.validateTypeDefForInstance(repositoryName, entityGUIDParameterName, typeDef, methodName);
        repositoryValidator.validateClassificationList(repositoryName,
                classificationsParameterName,
                initialClassifications,
                typeDef.getName(),
                methodName);

        repositoryValidator.validatePropertiesForType(repositoryName,
                propertiesParameterName,
                typeDef,
                initialProperties,
                methodName);

        repositoryValidator.validateInstanceStatus(repositoryName,
                initialStatusParameterName,
                initialStatus,
                typeDef,
                methodName);

        /*
         * Validation complete; ok to create new instance
         */
        // TODO: implement
        OMRSErrorCode errorCode = OMRSErrorCode.METHOD_NOT_IMPLEMENTED;

        String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                this.getClass().getName(),
                repositoryName);

        throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                this.getClass().getName(),
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction());
    }


    /**
     * Create an entity proxy in the metadata collection.  This is used to store relationships that span metadata
     * repositories.
     *
     * @param userId unique identifier for requesting user.
     * @param entityProxy details of entity to add.
     * @throws InvalidParameterException the entity proxy is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws FunctionNotSupportedException the repository does not support entity proxies as first class elements.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public void addEntityProxy(String       userId,
                               EntityProxy  entityProxy) throws InvalidParameterException,
            RepositoryErrorException,
            FunctionNotSupportedException,
            UserNotAuthorizedException
    {
        final String  methodName         = "addEntityProxy";
        final String  proxyParameterName = "entityProxy";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);

        repositoryValidator.validateEntityProxy(repositoryName,
                metadataCollectionId,
                proxyParameterName,
                entityProxy,
                methodName);

        /*
         * Validation complete
         */
        // TODO: implement
        OMRSErrorCode errorCode = OMRSErrorCode.METHOD_NOT_IMPLEMENTED;

        String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                this.getClass().getName(),
                repositoryName);

        throw new FunctionNotSupportedException(errorCode.getHTTPErrorCode(),
                this.getClass().getName(),
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction());
    }


    /**
     * Update the status for a specific entity.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID unique identifier (guid) for the requested entity.
     * @param newStatus new InstanceStatus for the entity.
     * @return EntityDetail showing the current entity header, properties and classifications.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by the guid is not found in the metadata collection.
     * @throws StatusNotSupportedException the metadata repository hosting the metadata collection does not support
     *                                      the requested status.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public EntityDetail updateEntityStatus(String           userId,
                                           String           entityGUID,
                                           InstanceStatus   newStatus) throws InvalidParameterException,
            RepositoryErrorException,
            EntityNotKnownException,
            StatusNotSupportedException,
            UserNotAuthorizedException
    {
        final String  methodName               = "updateEntityStatus";
        final String  entityGUIDParameterName  = "entityGUID";
        final String  statusParameterName      = "newStatus";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateGUID(repositoryName, entityGUIDParameterName, entityGUID, methodName);

        /*
         * Locate entity
         */
        // TODO: implement
        OMRSErrorCode errorCode = OMRSErrorCode.METHOD_NOT_IMPLEMENTED;

        String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                this.getClass().getName(),
                repositoryName);

        throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                this.getClass().getName(),
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction());
    }


    /**
     * Update selected properties in an entity.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID String unique identifier (guid) for the entity.
     * @param properties a list of properties to change.
     * @return EntityDetail showing the resulting entity header, properties and classifications.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by the guid is not found in the metadata collection
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                characteristics in the TypeDef for this entity's type
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public EntityDetail updateEntityProperties(String               userId,
                                               String               entityGUID,
                                               InstanceProperties   properties) throws InvalidParameterException,
            RepositoryErrorException,
            EntityNotKnownException,
            PropertyErrorException,
            UserNotAuthorizedException
    {
        final String  methodName = "updateEntityProperties";
        final String  entityGUIDParameterName  = "entityGUID";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateGUID(repositoryName, entityGUIDParameterName, entityGUID, methodName);

        /*
         * Locate entity
         */
        // TODO: implement
        OMRSErrorCode errorCode = OMRSErrorCode.METHOD_NOT_IMPLEMENTED;

        String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                this.getClass().getName(),
                repositoryName);

        throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                this.getClass().getName(),
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction());
    }


    /**
     * Undo the last update to an entity and return the previous content.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID String unique identifier (guid) for the entity.
     * @return EntityDetail showing the resulting entity header, properties and classifications.
     * @throws InvalidParameterException the guid is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by the guid is not found in the metadata collection.
     * @throws FunctionNotSupportedException the repository does not support undo.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public EntityDetail undoEntityUpdate(String  userId,
                                         String  entityGUID) throws InvalidParameterException,
            RepositoryErrorException,
            EntityNotKnownException,
            FunctionNotSupportedException,
            UserNotAuthorizedException
    {
        final String  methodName = "undoEntityUpdate";
        final String  entityGUIDParameterName  = "entityGUID";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateGUID(repositoryName, entityGUIDParameterName, entityGUID, methodName);

        /*
         * Validation complete; ok to restore entity
         */
        // TODO: implement
        OMRSErrorCode errorCode = OMRSErrorCode.METHOD_NOT_IMPLEMENTED;

        String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                this.getClass().getName(),
                repositoryName);

        throw new FunctionNotSupportedException(errorCode.getHTTPErrorCode(),
                this.getClass().getName(),
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction());
    }


    /**
     * Delete an entity.  The entity is soft deleted.  This means it is still in the graph but it is no longer returned
     * on queries.  All relationships to the entity are also soft-deleted and will no longer be usable.
     * To completely eliminate the entity from the graph requires a call to the purgeEntity() method after the delete call.
     * The restoreEntity() method will switch an entity back to Active status to restore the entity to normal use.
     *
     * @param userId unique identifier for requesting user.
     * @param typeDefGUID unique identifier of the type of the entity to delete.
     * @param typeDefName unique name of the type of the entity to delete.
     * @param obsoleteEntityGUID String unique identifier (guid) for the entity.
     * @return deleted entity
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by the guid is not found in the metadata collection.
     * @throws FunctionNotSupportedException the metadata repository hosting the metadata collection does not support
     *                                       soft-deletes (use purgeEntity() to remove the entity permanently).
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public EntityDetail   deleteEntity(String userId,
                                       String typeDefGUID,
                                       String typeDefName,
                                       String obsoleteEntityGUID) throws InvalidParameterException,
            RepositoryErrorException,
            EntityNotKnownException,
            FunctionNotSupportedException,
            UserNotAuthorizedException
    {
        final String  methodName               = "deleteEntity";
        final String  typeDefGUIDParameterName = "typeDefGUID";
        final String  typeDefNameParameterName = "typeDefName";
        final String  entityGUIDParameterName  = "obsoleteEntityGUID";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateTypeDefIds(repositoryName,
                typeDefGUIDParameterName,
                typeDefNameParameterName,
                typeDefGUID,
                typeDefName,
                methodName);
        repositoryValidator.validateGUID(repositoryName, entityGUIDParameterName, obsoleteEntityGUID, methodName);

        /*
         * Locate Entity
         */
        // TODO: implement
        OMRSErrorCode errorCode = OMRSErrorCode.METHOD_NOT_IMPLEMENTED;

        String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                this.getClass().getName(),
                repositoryName);

        throw new FunctionNotSupportedException(errorCode.getHTTPErrorCode(),
                this.getClass().getName(),
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction());
    }


    /**
     * Permanently removes a deleted entity from the metadata collection.  This request can not be undone.
     *
     * @param userId unique identifier for requesting user.
     * @param typeDefGUID unique identifier of the type of the entity to purge.
     * @param typeDefName unique name of the type of the entity to purge.
     * @param deletedEntityGUID String unique identifier (guid) for the entity.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by the guid is not found in the metadata collection
     * @throws EntityNotDeletedException the entity is not in DELETED status and so can not be purged
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public void purgeEntity(String    userId,
                            String    typeDefGUID,
                            String    typeDefName,
                            String    deletedEntityGUID) throws InvalidParameterException,
            RepositoryErrorException,
            EntityNotKnownException,
            EntityNotDeletedException,
            UserNotAuthorizedException
    {
        final String  methodName               = "purgeEntity";
        final String  typeDefGUIDParameterName = "typeDefGUID";
        final String  typeDefNameParameterName = "typeDefName";
        final String  entityGUIDParameterName  = "deletedEntityGUID";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateTypeDefIds(repositoryName,
                typeDefGUIDParameterName,
                typeDefNameParameterName,
                typeDefGUID,
                typeDefName,
                methodName);
        repositoryValidator.validateGUID(repositoryName, entityGUIDParameterName, deletedEntityGUID, methodName);

        /*
         * Locate entity
         */
        // TODO: implement
        OMRSErrorCode errorCode = OMRSErrorCode.METHOD_NOT_IMPLEMENTED;

        String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                this.getClass().getName(),
                repositoryName);

        throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                this.getClass().getName(),
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction());
    }


    /**
     * Restore the requested entity to the state it was before it was deleted.
     *
     * @param userId unique identifier for requesting user.
     * @param deletedEntityGUID String unique identifier (guid) for the entity.
     * @return EntityDetail showing the restored entity header, properties and classifications.
     * @throws InvalidParameterException the guid is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     * the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by the guid is not found in the metadata collection
     * @throws EntityNotDeletedException the entity is currently not in DELETED status and so it can not be restored
     * @throws FunctionNotSupportedException the repository does not support soft-deletes.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public EntityDetail restoreEntity(String    userId,
                                      String    deletedEntityGUID) throws InvalidParameterException,
            RepositoryErrorException,
            EntityNotKnownException,
            EntityNotDeletedException,
            FunctionNotSupportedException,
            UserNotAuthorizedException
    {
        final String  methodName              = "restoreEntity";
        final String  entityGUIDParameterName = "deletedEntityGUID";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateGUID(repositoryName, entityGUIDParameterName, deletedEntityGUID, methodName);

        /*
         * Locate entity
         */
        // TODO: implement
        OMRSErrorCode errorCode = OMRSErrorCode.METHOD_NOT_IMPLEMENTED;

        String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                this.getClass().getName(),
                repositoryName);

        throw new FunctionNotSupportedException(errorCode.getHTTPErrorCode(),
                this.getClass().getName(),
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction());
    }


    /**
     * Add the requested classification to a specific entity.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID String unique identifier (guid) for the entity.
     * @param classificationName String name for the classification.
     * @param classificationProperties list of properties to set in the classification.
     * @return EntityDetail showing the resulting entity header, properties and classifications.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by the guid is not found in the metadata collection
     * @throws ClassificationErrorException the requested classification is either not known or not valid
     *                                         for the entity.
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                characteristics in the TypeDef for this classification type
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public EntityDetail classifyEntity(String               userId,
                                       String               entityGUID,
                                       String               classificationName,
                                       InstanceProperties   classificationProperties) throws InvalidParameterException,
            RepositoryErrorException,
            EntityNotKnownException,
            ClassificationErrorException,
            PropertyErrorException,
            UserNotAuthorizedException
    {
        final String  methodName                  = "classifyEntity";
        final String  entityGUIDParameterName     = "entityGUID";
        final String  classificationParameterName = "classificationName";
        final String  propertiesParameterName     = "classificationProperties";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateGUID(repositoryName, entityGUIDParameterName, entityGUID, methodName);

        /*
         * Locate entity
         */
        // TODO: implement
        OMRSErrorCode errorCode = OMRSErrorCode.METHOD_NOT_IMPLEMENTED;

        String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                this.getClass().getName(),
                repositoryName);

        throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                this.getClass().getName(),
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction());
    }


    /**
     * Remove a specific classification from an entity.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID String unique identifier (guid) for the entity.
     * @param classificationName String name for the classification.
     * @return EntityDetail showing the resulting entity header, properties and classifications.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by the guid is not found in the metadata collection
     * @throws ClassificationErrorException the requested classification is not set on the entity.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public EntityDetail declassifyEntity(String  userId,
                                         String  entityGUID,
                                         String  classificationName) throws InvalidParameterException,
            RepositoryErrorException,
            EntityNotKnownException,
            ClassificationErrorException,
            UserNotAuthorizedException
    {
        final String  methodName                  = "declassifyEntity";
        final String  entityGUIDParameterName     = "entityGUID";
        final String  classificationParameterName = "classificationName";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateGUID(repositoryName, entityGUIDParameterName, entityGUID, methodName);
        repositoryValidator.validateClassificationName(repositoryName,
                classificationParameterName,
                classificationName,
                methodName);

        /*
         * Locate entity
         */
        // TODO: implement
        OMRSErrorCode errorCode = OMRSErrorCode.METHOD_NOT_IMPLEMENTED;

        String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                this.getClass().getName(),
                repositoryName);

        throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                this.getClass().getName(),
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction());
    }


    /**
     * Update one or more properties in one of an entity's classifications.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID String unique identifier (guid) for the entity.
     * @param classificationName String name for the classification.
     * @param properties list of properties for the classification.
     * @return EntityDetail showing the resulting entity header, properties and classifications.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by the guid is not found in the metadata collection
     * @throws ClassificationErrorException the requested classification is not attached to the classification.
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                characteristics in the TypeDef for this classification type
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public EntityDetail updateEntityClassification(String               userId,
                                                   String               entityGUID,
                                                   String               classificationName,
                                                   InstanceProperties   properties) throws InvalidParameterException,
            RepositoryErrorException,
            EntityNotKnownException,
            ClassificationErrorException,
            PropertyErrorException,
            UserNotAuthorizedException
    {
        final String  methodName = "updateEntityClassification";
        final String  entityGUIDParameterName     = "entityGUID";
        final String  classificationParameterName = "classificationName";
        final String  propertiesParameterName = "properties";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateGUID(repositoryName, entityGUIDParameterName, entityGUID, methodName);
        repositoryValidator.validateClassificationName(repositoryName, classificationParameterName, classificationName, methodName);


        try
        {
            repositoryValidator.validateClassificationProperties(repositoryName,
                    classificationName,
                    propertiesParameterName,
                    properties,
                    methodName);
        }
        catch (PropertyErrorException  error)
        {
            throw error;
        }
        catch (Throwable   error)
        {
            OMRSErrorCode errorCode = OMRSErrorCode.UNKNOWN_CLASSIFICATION;

            throw new ClassificationErrorException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    error.getMessage(),
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }


        /*
         * Locate entity
         */
        // TODO: implement
        OMRSErrorCode errorCode = OMRSErrorCode.METHOD_NOT_IMPLEMENTED;

        String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                this.getClass().getName(),
                repositoryName);

        throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                this.getClass().getName(),
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction());
    }



    /**
     * Add a new relationship between two entities to the metadata collection.
     *
     * @param userId unique identifier for requesting user.
     * @param relationshipTypeGUID unique identifier (guid) for the new relationship's type.
     * @param initialProperties initial list of properties for the new entity; null means no properties.
     * @param entityOneGUID the unique identifier of one of the entities that the relationship is connecting together.
     * @param entityTwoGUID the unique identifier of the other entity that the relationship is connecting together.
     * @param initialStatus initial status typically set to DRAFT, PREPARED or ACTIVE.
     * @return Relationship structure with the new header, requested entities and properties.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                 the metadata collection is stored.
     * @throws TypeErrorException the requested type is not known, or not supported in the metadata repository
     *                            hosting the metadata collection.
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                  characteristics in the TypeDef for this relationship's type.
     * @throws EntityNotKnownException one of the requested entities is not known in the metadata collection.
     * @throws StatusNotSupportedException the metadata repository hosting the metadata collection does not support
     *                                     the requested status.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public Relationship addRelationship(String               userId,
                                        String               relationshipTypeGUID,
                                        InstanceProperties   initialProperties,
                                        String               entityOneGUID,
                                        String               entityTwoGUID,
                                        InstanceStatus       initialStatus) throws InvalidParameterException,
            RepositoryErrorException,
            TypeErrorException,
            PropertyErrorException,
            EntityNotKnownException,
            StatusNotSupportedException,
            UserNotAuthorizedException
    {
        final String  methodName = "addRelationship";
        final String  guidParameterName = "relationshipTypeGUID";
        final String  propertiesParameterName       = "initialProperties";
        final String  initialStatusParameterName    = "initialStatus";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateTypeGUID(repositoryName, guidParameterName, relationshipTypeGUID, methodName);

        TypeDef  typeDef = repositoryHelper.getTypeDef(repositoryName, guidParameterName, relationshipTypeGUID, methodName);

        repositoryValidator.validateTypeDefForInstance(repositoryName, guidParameterName, typeDef, methodName);


        repositoryValidator.validatePropertiesForType(repositoryName,
                propertiesParameterName,
                typeDef,
                initialProperties,
                methodName);

        repositoryValidator.validateInstanceStatus(repositoryName,
                initialStatusParameterName,
                initialStatus,
                typeDef,
                methodName);

        /*
         * Validation complete, ok to create new instance
         */
        Relationship relationship = null;

        try {
            TypeDef relationshipTypeDef = getTypeDefByGUID(userId, relationshipTypeGUID);
            if (relationshipTypeDef != null) {

                String relationshipTypeName = relationshipTypeDef.getName();
                Reference entityOne = this.igcRestClient.getAssetRefById(entityOneGUID);
                Reference entityTwo = this.igcRestClient.getAssetRefById(entityTwoGUID);

                if (entityOne == null) {
                    OMRSErrorCode errorCode = OMRSErrorCode.ENTITY_NOT_KNOWN;
                    String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(
                            entityOneGUID,
                            methodName,
                            repositoryName
                    );
                    throw new EntityNotKnownException(errorCode.getHTTPErrorCode(),
                            this.getClass().getName(),
                            methodName,
                            errorMessage,
                            errorCode.getSystemAction(),
                            errorCode.getUserAction());
                }
                if (entityTwo == null) {
                    OMRSErrorCode errorCode = OMRSErrorCode.ENTITY_NOT_KNOWN;
                    String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(
                            entityTwoGUID,
                            methodName,
                            repositoryName
                    );
                    throw new EntityNotKnownException(errorCode.getHTTPErrorCode(),
                            this.getClass().getName(),
                            methodName,
                            errorMessage,
                            errorCode.getSystemAction(),
                            errorCode.getUserAction());
                }

                RelationshipMapping relationshipMapping = getRelationshipMapper(
                        relationshipTypeName,
                        entityOne.getType(),
                        entityTwo.getType()
                );

                if (relationshipMapping != null) {

                    // If there is a relationship-level asset, these cannot be created, so we need to simply fail
                    if (relationshipMapping.hasRelationshipLevelAsset()) {
                        String relationshipLevelAssetType = relationshipMapping.getRelationshipLevelIgcAsset();
                        Class pojo = igcRestClient.getPOJOForType(relationshipLevelAssetType);
                        if (!Reference.isCreatableFromPOJO(pojo)) {
                            OMRSErrorCode errorCode = OMRSErrorCode.REPOSITORY_LOGIC_ERROR;
                            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(
                                    repositoryName,
                                    methodName,
                                    "Cannot create relationship for IGC asset type: " + relationshipLevelAssetType
                            );
                            throw new RepositoryErrorException(
                                    errorCode.getHTTPErrorCode(),
                                    this.getClass().getName(),
                                    methodName,
                                    errorMessage,
                                    errorCode.getSystemAction(),
                                    errorCode.getUserAction()
                            );
                        } else {
                            // TODO: for creatable relationship-level assets, create a new one to represent this relationship
                            log.info("Creating a relationship-level asset for IGC type {} is not yet implemented.", relationshipLevelAssetType);
                        }
                    } else {
                        // TODO: implement relationship update based on provided parameters
                        //  Start from the optimal end in order to define the relationship in the most simple way
                        //relationshipMapping.getOptimalStart()
                        log.info("Creating a new relationship between {} and {} is not yet implemented.", entityOne.getType(), entityTwo.getType());
                    }

                } else {
                    OMRSErrorCode errorCode = OMRSErrorCode.TYPEDEF_NAME_NOT_KNOWN;
                    String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(
                            relationshipTypeName,
                            methodName,
                            repositoryName
                    );
                    throw new TypeErrorException(
                            errorCode.getHTTPErrorCode(),
                            this.getClass().getName(),
                            methodName,
                            errorMessage,
                            errorCode.getSystemAction(),
                            errorCode.getUserAction()
                    );
                }

            } else {
                OMRSErrorCode errorCode = OMRSErrorCode.TYPEDEF_ID_NOT_KNOWN;
                String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(
                        relationshipTypeGUID,
                        guidParameterName,
                        methodName,
                        repositoryName
                );
                throw new TypeErrorException(
                        errorCode.getHTTPErrorCode(),
                        this.getClass().getName(),
                        methodName,
                        errorMessage,
                        errorCode.getSystemAction(),
                        errorCode.getUserAction()
                );
            }
        } catch (TypeDefNotKnownException e) {
            OMRSErrorCode errorCode = OMRSErrorCode.TYPEDEF_ID_NOT_KNOWN;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(
                    relationshipTypeGUID,
                    guidParameterName,
                    methodName,
                    repositoryName
            );
            throw new TypeErrorException(
                    errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction()
            );
        }

        return relationship;

    }


    /**
     * Update the status of a specific relationship.
     *
     * @param userId unique identifier for requesting user.
     * @param relationshipGUID String unique identifier (guid) for the relationship.
     * @param newStatus new InstanceStatus for the relationship.
     * @return Resulting relationship structure with the new status set.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws RelationshipNotKnownException the requested relationship is not known in the metadata collection.
     * @throws StatusNotSupportedException the metadata repository hosting the metadata collection does not support
     *                                     the requested status.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public Relationship updateRelationshipStatus(String           userId,
                                                 String           relationshipGUID,
                                                 InstanceStatus   newStatus) throws InvalidParameterException,
            RepositoryErrorException,
            RelationshipNotKnownException,
            StatusNotSupportedException,
            UserNotAuthorizedException
    {
        final String  methodName          = "updateRelationshipStatus";
        final String  guidParameterName   = "relationshipGUID";
        final String  statusParameterName = "newStatus";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateGUID(repositoryName, guidParameterName, relationshipGUID, methodName);

        /*
         * Locate relationship
         */
        // TODO: implement
        OMRSErrorCode errorCode = OMRSErrorCode.METHOD_NOT_IMPLEMENTED;

        String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                this.getClass().getName(),
                repositoryName);

        throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                this.getClass().getName(),
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction());
    }


    /**
     * Update the properties of a specific relationship.
     *
     * @param userId unique identifier for requesting user.
     * @param relationshipGUID String unique identifier (guid) for the relationship.
     * @param properties list of the properties to update.
     * @return Resulting relationship structure with the new properties set.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws RelationshipNotKnownException the requested relationship is not known in the metadata collection.
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                characteristics in the TypeDef for this relationship's type.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public Relationship updateRelationshipProperties(String               userId,
                                                     String               relationshipGUID,
                                                     InstanceProperties   properties) throws InvalidParameterException,
            RepositoryErrorException,
            RelationshipNotKnownException,
            PropertyErrorException,
            UserNotAuthorizedException
    {
        final String  methodName = "updateRelationshipProperties";
        final String  guidParameterName = "relationshipGUID";
        final String  propertiesParameterName = "properties";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateGUID(repositoryName, guidParameterName, relationshipGUID, methodName);

        /*
         * Locate relationship
         */
        // TODO: implement
        OMRSErrorCode errorCode = OMRSErrorCode.METHOD_NOT_IMPLEMENTED;

        String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                this.getClass().getName(),
                repositoryName);

        throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                this.getClass().getName(),
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction());
    }


    /**
     * Undo the latest change to a relationship (either a change of properties or status).
     *
     * @param userId unique identifier for requesting user.
     * @param relationshipGUID String unique identifier (guid) for the relationship.
     * @return Relationship structure with the new current header, requested entities and properties.
     * @throws InvalidParameterException the guid is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws RelationshipNotKnownException the requested relationship is not known in the metadata collection.
     * @throws FunctionNotSupportedException the repository does not support undo.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public Relationship undoRelationshipUpdate(String  userId,
                                               String  relationshipGUID) throws InvalidParameterException,
            RepositoryErrorException,
            RelationshipNotKnownException,
            FunctionNotSupportedException,
            UserNotAuthorizedException
    {
        final String  methodName = "undoRelationshipUpdate";
        final String  guidParameterName = "relationshipGUID";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateGUID(repositoryName, guidParameterName, relationshipGUID, methodName);

        /*
         * Restore previous version
         */
        // TODO: implement
        OMRSErrorCode errorCode = OMRSErrorCode.METHOD_NOT_IMPLEMENTED;

        String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                this.getClass().getName(),
                repositoryName);

        throw new FunctionNotSupportedException(errorCode.getHTTPErrorCode(),
                this.getClass().getName(),
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction());
    }


    /**
     * Delete a specific relationship.  This is a soft-delete which means the relationship's status is updated to
     * DELETED and it is no longer available for queries.  To remove the relationship permanently from the
     * metadata collection, use purgeRelationship().
     *
     * @param userId unique identifier for requesting user.
     * @param typeDefGUID unique identifier of the type of the relationship to delete.
     * @param typeDefName unique name of the type of the relationship to delete.
     * @param obsoleteRelationshipGUID String unique identifier (guid) for the relationship.
     * @return deleted relationship
     * @throws InvalidParameterException one of the parameters is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     * the metadata collection is stored.
     * @throws RelationshipNotKnownException the requested relationship is not known in the metadata collection.
     * @throws FunctionNotSupportedException the metadata repository hosting the metadata collection does not support
     *                                     soft-deletes.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public Relationship deleteRelationship(String userId,
                                           String typeDefGUID,
                                           String typeDefName,
                                           String obsoleteRelationshipGUID) throws InvalidParameterException,
            RepositoryErrorException,
            RelationshipNotKnownException,
            FunctionNotSupportedException,
            UserNotAuthorizedException
    {
        final String  methodName = "deleteRelationship";
        final String  guidParameterName = "typeDefGUID";
        final String  nameParameterName = "typeDefName";
        final String  relationshipParameterName = "obsoleteRelationshipGUID";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateGUID(repositoryName, relationshipParameterName, obsoleteRelationshipGUID, methodName);
        repositoryValidator.validateTypeDefIds(repositoryName,
                guidParameterName,
                nameParameterName,
                typeDefGUID,
                typeDefName,
                methodName);

        /*
         * Locate relationship
         */
        // TODO: implement
        OMRSErrorCode errorCode = OMRSErrorCode.METHOD_NOT_IMPLEMENTED;

        String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                this.getClass().getName(),
                repositoryName);

        throw new FunctionNotSupportedException(errorCode.getHTTPErrorCode(),
                this.getClass().getName(),
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction());
    }


    /**
     * Permanently delete the relationship from the repository.  There is no means to undo this request.
     *
     * @param userId unique identifier for requesting user.
     * @param typeDefGUID unique identifier of the type of the relationship to purge.
     * @param typeDefName unique name of the type of the relationship to purge.
     * @param deletedRelationshipGUID String unique identifier (guid) for the relationship.
     * @throws InvalidParameterException one of the parameters is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws RelationshipNotKnownException the requested relationship is not known in the metadata collection.
     * @throws RelationshipNotDeletedException the requested relationship is not in DELETED status.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public void purgeRelationship(String    userId,
                                  String    typeDefGUID,
                                  String    typeDefName,
                                  String    deletedRelationshipGUID) throws InvalidParameterException,
            RepositoryErrorException,
            RelationshipNotKnownException,
            RelationshipNotDeletedException,
            UserNotAuthorizedException
    {
        final String  methodName = "purgeRelationship";
        final String  guidParameterName = "typeDefGUID";
        final String  nameParameterName = "typeDefName";
        final String  relationshipParameterName = "deletedRelationshipGUID";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateGUID(repositoryName, relationshipParameterName, deletedRelationshipGUID, methodName);
        repositoryValidator.validateTypeDefIds(repositoryName,
                guidParameterName,
                nameParameterName,
                typeDefGUID,
                typeDefName,
                methodName);

        /*
         * Locate relationship
         */
        // TODO: implement
        OMRSErrorCode errorCode = OMRSErrorCode.METHOD_NOT_IMPLEMENTED;

        String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                this.getClass().getName(),
                repositoryName);

        throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                this.getClass().getName(),
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction());
    }


    /**
     * Restore a deleted relationship into the metadata collection.  The new status will be ACTIVE and the
     * restored details of the relationship are returned to the caller.
     *
     * @param userId unique identifier for requesting user.
     * @param deletedRelationshipGUID String unique identifier (guid) for the relationship.
     * @return Relationship structure with the restored header, requested entities and properties.
     * @throws InvalidParameterException the guid is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     * the metadata collection is stored.
     * @throws RelationshipNotKnownException the requested relationship is not known in the metadata collection.
     * @throws RelationshipNotDeletedException the requested relationship is not in DELETED status.
     * @throws FunctionNotSupportedException the repository does not support soft-deletes.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public Relationship restoreRelationship(String    userId,
                                            String    deletedRelationshipGUID) throws InvalidParameterException,
            RepositoryErrorException,
            RelationshipNotKnownException,
            RelationshipNotDeletedException,
            FunctionNotSupportedException,
            UserNotAuthorizedException
    {
        final String  methodName = "restoreRelationship";
        final String  guidParameterName = "deletedRelationshipGUID";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateGUID(repositoryName, guidParameterName, deletedRelationshipGUID, methodName);

        /*
         * Locate relationship
         */
        // TODO: implement
        OMRSErrorCode errorCode = OMRSErrorCode.METHOD_NOT_IMPLEMENTED;

        String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                this.getClass().getName(),
                repositoryName);

        throw new FunctionNotSupportedException(errorCode.getHTTPErrorCode(),
                this.getClass().getName(),
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction());
    }

    /**
     * Change the guid of an existing entity to a new value.  This is used if two different
     * entities are discovered to have the same guid.  This is extremely unlikely but not impossible so
     * the open metadata protocol has provision for this.
     *
     * @param userId unique identifier for requesting user.
     * @param typeDefGUID the guid of the TypeDef for the entity used to verify the entity identity.
     * @param typeDefName the name of the TypeDef for the entity used to verify the entity identity.
     * @param entityGUID the existing identifier for the entity.
     * @param newEntityGUID new unique identifier for the entity.
     * @return entity new values for this entity, including the new guid.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by the guid is not found in the metadata collection.
     * @throws FunctionNotSupportedException the repository does not support the re-identification of instances.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public EntityDetail reIdentifyEntity(String     userId,
                                         String     typeDefGUID,
                                         String     typeDefName,
                                         String     entityGUID,
                                         String     newEntityGUID) throws InvalidParameterException,
            RepositoryErrorException,
            EntityNotKnownException,
            FunctionNotSupportedException,
            UserNotAuthorizedException
    {
        final String  methodName = "reIdentifyEntity";
        final String  guidParameterName = "typeDefGUID";
        final String  nameParameterName = "typeDefName";
        final String  instanceParameterName = "deletedRelationshipGUID";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        parentConnector.validateRepositoryIsActive(methodName);
        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateGUID(repositoryName, instanceParameterName, newEntityGUID, methodName);
        repositoryValidator.validateTypeDefIds(repositoryName,
                guidParameterName,
                nameParameterName,
                typeDefGUID,
                typeDefName,
                methodName);

        /*
         * Locate entity
         */
        // TODO: implement
        OMRSErrorCode errorCode = OMRSErrorCode.METHOD_NOT_IMPLEMENTED;

        String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                this.getClass().getName(),
                repositoryName);

        throw new FunctionNotSupportedException(errorCode.getHTTPErrorCode(),
                this.getClass().getName(),
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction());
    }


    /**
     * Change the type of an existing entity.  Typically this action is taken to move an entity's
     * type to either a super type (so the subtype can be deleted) or a new subtype (so additional properties can be
     * added.)  However, the type can be changed to any compatible type and the properties adjusted.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID the unique identifier for the entity to change.
     * @param currentTypeDefSummary the current details of the TypeDef for the entity used to verify the entity identity
     * @param newTypeDefSummary details of this entity's new TypeDef.
     * @return entity new values for this entity, including the new type information.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws TypeErrorException the requested type is not known, or not supported in the metadata repository
     *                            hosting the metadata collection.
     * @throws PropertyErrorException The properties in the instance are incompatible with the requested type.
     * @throws ClassificationErrorException the entity's classifications are not valid for the new type.
     * @throws EntityNotKnownException the entity identified by the guid is not found in the metadata collection.
     * @throws FunctionNotSupportedException the repository does not support the re-typing of instances.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public EntityDetail reTypeEntity(String         userId,
                                     String         entityGUID,
                                     TypeDefSummary currentTypeDefSummary,
                                     TypeDefSummary newTypeDefSummary) throws InvalidParameterException,
            RepositoryErrorException,
            TypeErrorException,
            PropertyErrorException,
            ClassificationErrorException,
            EntityNotKnownException,
            FunctionNotSupportedException,
            UserNotAuthorizedException
    {
        final String  methodName = "reTypeEntity";
        final String  entityParameterName = "entityGUID";
        final String  currentTypeDefParameterName = "currentTypeDefSummary";
        final String  newTypeDefParameterName = "newTypeDefSummary";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateGUID(repositoryName, entityParameterName, entityGUID, methodName);

        /*
         * Locate entity
         */
        // TODO: implement
        OMRSErrorCode errorCode = OMRSErrorCode.METHOD_NOT_IMPLEMENTED;

        String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                this.getClass().getName(),
                repositoryName);

        throw new FunctionNotSupportedException(errorCode.getHTTPErrorCode(),
                this.getClass().getName(),
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction());
    }


    /**
     * Change the home of an existing entity.  This action is taken for example, if the original home repository
     * becomes permanently unavailable, or if the user community updating this entity move to working
     * from a different repository in the open metadata repository cohort.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID the unique identifier for the entity to change.
     * @param typeDefGUID the guid of the TypeDef for the entity used to verify the entity identity.
     * @param typeDefName the name of the TypeDef for the entity used to verify the entity identity.
     * @param homeMetadataCollectionId the existing identifier for this entity's home.
     * @param newHomeMetadataCollectionId unique identifier for the new home metadata collection/repository.
     * @param newHomeMetadataCollectionName display name for the new home metadata collection/repository.
     * @return entity new values for this entity, including the new home information.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by the guid is not found in the metadata collection.
     * @throws FunctionNotSupportedException the repository does not support the re-homing of instances.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public EntityDetail reHomeEntity(String         userId,
                                     String         entityGUID,
                                     String         typeDefGUID,
                                     String         typeDefName,
                                     String         homeMetadataCollectionId,
                                     String         newHomeMetadataCollectionId,
                                     String         newHomeMetadataCollectionName) throws InvalidParameterException,
            RepositoryErrorException,
            EntityNotKnownException,
            FunctionNotSupportedException,
            UserNotAuthorizedException
    {
        final String methodName                = "reHomeEntity";
        final String guidParameterName         = "typeDefGUID";
        final String nameParameterName         = "typeDefName";
        final String entityParameterName       = "entityGUID";
        final String homeParameterName         = "homeMetadataCollectionId";
        final String newHomeParameterName      = "newHomeMetadataCollectionId";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateGUID(repositoryName, entityParameterName, entityGUID, methodName);
        repositoryValidator.validateTypeDefIds(repositoryName,
                guidParameterName,
                nameParameterName,
                typeDefGUID,
                typeDefName,
                methodName);
        repositoryValidator.validateHomeMetadataGUID(repositoryName, homeParameterName, homeMetadataCollectionId, methodName);
        repositoryValidator.validateHomeMetadataGUID(repositoryName, newHomeParameterName, newHomeMetadataCollectionId, methodName);

        /*
         * Locate entity
         */
        // TODO: implement
        OMRSErrorCode errorCode = OMRSErrorCode.METHOD_NOT_IMPLEMENTED;

        String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                this.getClass().getName(),
                repositoryName);

        throw new FunctionNotSupportedException(errorCode.getHTTPErrorCode(),
                this.getClass().getName(),
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction());
    }


    /**
     * Change the guid of an existing relationship.  This is used if two different
     * relationships are discovered to have the same guid.  This is extremely unlikely but not impossible so
     * the open metadata protocol has provision for this.
     *
     * @param userId unique identifier for requesting user.
     * @param typeDefGUID the guid of the TypeDef for the relationship used to verify the relationship identity.
     * @param typeDefName the name of the TypeDef for the relationship used to verify the relationship identity.
     * @param relationshipGUID the existing identifier for the relationship.
     * @param newRelationshipGUID  the new unique identifier for the relationship.
     * @return relationship new values for this relationship, including the new guid.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws RelationshipNotKnownException the relationship identified by the guid is not found in the
     *                                         metadata collection.
     * @throws FunctionNotSupportedException the repository does not support the re-identification of instances.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public Relationship reIdentifyRelationship(String     userId,
                                               String     typeDefGUID,
                                               String     typeDefName,
                                               String     relationshipGUID,
                                               String     newRelationshipGUID) throws InvalidParameterException,
            RepositoryErrorException,
            RelationshipNotKnownException,
            FunctionNotSupportedException,
            UserNotAuthorizedException
    {
        final String methodName                   = "reIdentifyRelationship";
        final String guidParameterName            = "typeDefGUID";
        final String nameParameterName            = "typeDefName";
        final String relationshipParameterName    = "relationshipGUID";
        final String newRelationshipParameterName = "newHomeMetadataCollectionId";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateGUID(repositoryName, relationshipParameterName, relationshipGUID, methodName);
        repositoryValidator.validateTypeDefIds(repositoryName,
                guidParameterName,
                nameParameterName,
                typeDefGUID,
                typeDefName,
                methodName);
        repositoryValidator.validateGUID(repositoryName, newRelationshipParameterName, newRelationshipGUID, methodName);

        /*
         * Locate relationship
         */
        Relationship  relationship  = this.getRelationship(userId, relationshipGUID);

        /*
         * Validation complete, ok to make changes
         */
        // TODO: implement
        OMRSErrorCode errorCode = OMRSErrorCode.METHOD_NOT_IMPLEMENTED;

        String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                this.getClass().getName(),
                repositoryName);

        throw new FunctionNotSupportedException(errorCode.getHTTPErrorCode(),
                this.getClass().getName(),
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction());
    }


    /**
     * Change the type of an existing relationship.  Typically this action is taken to move a relationship's
     * type to either a super type (so the subtype can be deleted) or a new subtype (so additional properties can be
     * added.)  However, the type can be changed to any compatible type.
     *
     * @param userId unique identifier for requesting user.
     * @param relationshipGUID the unique identifier for the relationship.
     * @param currentTypeDefSummary the details of the TypeDef for the relationship used to verify the relationship identity.
     * @param newTypeDefSummary details of this relationship's new TypeDef.
     * @return relationship new values for this relationship, including the new type information.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws TypeErrorException the requested type is not known, or not supported in the metadata repository
     *                            hosting the metadata collection.
     * @throws PropertyErrorException The properties in the instance are incompatible with the requested type.
     * @throws RelationshipNotKnownException the relationship identified by the guid is not found in the
     *                                         metadata collection.
     * @throws FunctionNotSupportedException the repository does not support the re-typing of instances.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public Relationship reTypeRelationship(String         userId,
                                           String         relationshipGUID,
                                           TypeDefSummary currentTypeDefSummary,
                                           TypeDefSummary newTypeDefSummary) throws InvalidParameterException,
            RepositoryErrorException,
            TypeErrorException,
            PropertyErrorException,
            RelationshipNotKnownException,
            FunctionNotSupportedException,
            UserNotAuthorizedException
    {
        final String methodName = "reTypeRelationship";
        final String relationshipParameterName = "relationshipGUID";
        final String currentTypeDefParameterName = "currentTypeDefSummary";
        final String newTypeDefParameterName = "newTypeDefSummary";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateGUID(repositoryName, relationshipParameterName, relationshipGUID, methodName);
        repositoryValidator.validateType(repositoryName, currentTypeDefParameterName, currentTypeDefSummary, TypeDefCategory.RELATIONSHIP_DEF, methodName);
        repositoryValidator.validateType(repositoryName, currentTypeDefParameterName, newTypeDefSummary, TypeDefCategory.RELATIONSHIP_DEF, methodName);

        /*
         * Locate relationship
         */
        Relationship  relationship  = this.getRelationship(userId, relationshipGUID);

        repositoryValidator.validateInstanceType(repositoryName,
                relationship,
                currentTypeDefParameterName,
                currentTypeDefParameterName,
                currentTypeDefSummary.getGUID(),
                currentTypeDefSummary.getName());


        repositoryValidator.validatePropertiesForType(repositoryName,
                newTypeDefParameterName,
                newTypeDefSummary,
                relationship.getProperties(),
                methodName);

        /*
         * Validation complete; ok to make changes
         */
        // TODO: implement
        OMRSErrorCode errorCode = OMRSErrorCode.METHOD_NOT_IMPLEMENTED;

        String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                this.getClass().getName(),
                repositoryName);

        throw new FunctionNotSupportedException(errorCode.getHTTPErrorCode(),
                this.getClass().getName(),
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction());
    }


    /**
     * Change the home of an existing relationship.  This action is taken for example, if the original home repository
     * becomes permanently unavailable, or if the user community updating this relationship move to working
     * from a different repository in the open metadata repository cohort.
     *
     * @param userId unique identifier for requesting user.
     * @param relationshipGUID the unique identifier for the relationship.
     * @param typeDefGUID the guid of the TypeDef for the relationship used to verify the relationship identity.
     * @param typeDefName the name of the TypeDef for the relationship used to verify the relationship identity.
     * @param homeMetadataCollectionId the existing identifier for this relationship's home.
     * @param newHomeMetadataCollectionId unique identifier for the new home metadata collection/repository.
     * @param newHomeMetadataCollectionName display name for the new home metadata collection/repository.
     * @return relationship new values for this relationship, including the new home information.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws RelationshipNotKnownException the relationship identified by the guid is not found in the
     *                                         metadata collection.
     * @throws FunctionNotSupportedException the repository does not support the re-homing of instances.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public Relationship reHomeRelationship(String   userId,
                                           String   relationshipGUID,
                                           String   typeDefGUID,
                                           String   typeDefName,
                                           String   homeMetadataCollectionId,
                                           String   newHomeMetadataCollectionId,
                                           String   newHomeMetadataCollectionName) throws InvalidParameterException,
            RepositoryErrorException,
            RelationshipNotKnownException,
            FunctionNotSupportedException,
            UserNotAuthorizedException
    {
        final String  methodName               = "reHomeRelationship";
        final String guidParameterName         = "typeDefGUID";
        final String nameParameterName         = "typeDefName";
        final String relationshipParameterName = "relationshipGUID";
        final String homeParameterName         = "homeMetadataCollectionId";
        final String newHomeParameterName      = "newHomeMetadataCollectionId";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateGUID(repositoryName, relationshipParameterName, relationshipGUID, methodName);
        repositoryValidator.validateTypeDefIds(repositoryName,
                guidParameterName,
                nameParameterName,
                typeDefGUID,
                typeDefName,
                methodName);
        repositoryValidator.validateHomeMetadataGUID(repositoryName, homeParameterName, homeMetadataCollectionId, methodName);
        repositoryValidator.validateHomeMetadataGUID(repositoryName, newHomeParameterName, newHomeMetadataCollectionId, methodName);

        /*
         * Locate relationship
         */
        // TODO: implement
        OMRSErrorCode errorCode = OMRSErrorCode.METHOD_NOT_IMPLEMENTED;

        String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                this.getClass().getName(),
                repositoryName);

        throw new FunctionNotSupportedException(errorCode.getHTTPErrorCode(),
                this.getClass().getName(),
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction());
    }

    /**
     * Save the entity as a reference copy.  The id of the home metadata collection is already set up in the
     * entity.
     *
     * @param userId unique identifier for requesting server.
     * @param entity details of the entity to save.
     * @throws InvalidParameterException the entity is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws TypeErrorException the requested type is not known, or not supported in the metadata repository
     *                            hosting the metadata collection.
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                  characteristics in the TypeDef for this entity's type.
     * @throws HomeEntityException the entity belongs to the local repository so creating a reference
     *                               copy would be invalid.
     * @throws EntityConflictException the new entity conflicts with an existing entity.
     * @throws InvalidEntityException the new entity has invalid contents.
     * @throws FunctionNotSupportedException the repository does not support reference copies of instances.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public void saveEntityReferenceCopy(String userId,
                                        EntityDetail   entity) throws InvalidParameterException,
            RepositoryErrorException,
            TypeErrorException,
            PropertyErrorException,
            HomeEntityException,
            EntityConflictException,
            InvalidEntityException,
            FunctionNotSupportedException,
            UserNotAuthorizedException
    {
        final String  methodName = "saveEntityReferenceCopy";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        // TODO: implement
        OMRSErrorCode errorCode = OMRSErrorCode.METHOD_NOT_IMPLEMENTED;

        String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                this.getClass().getName(),
                repositoryName);

        throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                this.getClass().getName(),
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction());
    }


    /**
     * Remove a reference copy of the the entity from the local repository.  This method can be used to
     * remove reference copies from the local cohort, repositories that have left the cohort,
     * or entities that have come from open metadata archives.
     *
     * @param userId unique identifier for requesting server.
     * @param entityGUID the unique identifier for the entity.
     * @param typeDefGUID the guid of the TypeDef for the relationship used to verify the relationship identity.
     * @param typeDefName the name of the TypeDef for the relationship used to verify the relationship identity.
     * @param homeMetadataCollectionId identifier of the metadata collection that is the home to this entity.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by the guid is not found in the metadata collection.
     * @throws HomeEntityException the entity belongs to the local repository so creating a reference
     *                               copy would be invalid.
     * @throws FunctionNotSupportedException the repository does not support reference copies of instances.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public void purgeEntityReferenceCopy(String userId,
                                         String   entityGUID,
                                         String   typeDefGUID,
                                         String   typeDefName,
                                         String   homeMetadataCollectionId) throws InvalidParameterException,
            RepositoryErrorException,
            EntityNotKnownException,
            HomeEntityException,
            FunctionNotSupportedException,
            UserNotAuthorizedException
    {
        final String  methodName               = "purgeEntityReferenceCopy";
        final String guidParameterName         = "typeDefGUID";
        final String nameParameterName         = "typeDefName";
        final String entityParameterName       = "entityGUID";
        final String homeParameterName         = "homeMetadataCollectionId";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateGUID(repositoryName, entityParameterName, entityGUID, methodName);
        repositoryValidator.validateTypeDefIds(repositoryName,
                guidParameterName,
                nameParameterName,
                typeDefGUID,
                typeDefName,
                methodName);
        repositoryValidator.validateHomeMetadataGUID(repositoryName, homeParameterName, homeMetadataCollectionId, methodName);

        /*
         * Remove entity
         */
        // TODO: implement
        OMRSErrorCode errorCode = OMRSErrorCode.METHOD_NOT_IMPLEMENTED;

        String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                this.getClass().getName(),
                repositoryName);

        throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                this.getClass().getName(),
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction());
    }


    /**
     * The local repository has requested that the repository that hosts the home metadata collection for the
     * specified entity sends out the details of this entity so the local repository can create a reference copy.
     *
     * @param userId unique identifier for requesting server.
     * @param entityGUID unique identifier of requested entity.
     * @param typeDefGUID unique identifier of requested entity's TypeDef.
     * @param typeDefName unique name of requested entity's TypeDef.
     * @param homeMetadataCollectionId identifier of the metadata collection that is the home to this entity.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by the guid is not found in the metadata collection.
     * @throws HomeEntityException the entity belongs to the local repository so creating a reference
     *                               copy would be invalid.
     * @throws FunctionNotSupportedException the repository does not support reference copies of instances.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public void refreshEntityReferenceCopy(String userId,
                                           String   entityGUID,
                                           String   typeDefGUID,
                                           String   typeDefName,
                                           String   homeMetadataCollectionId) throws InvalidParameterException,
            RepositoryErrorException,
            EntityNotKnownException,
            HomeEntityException,
            FunctionNotSupportedException,
            UserNotAuthorizedException
    {
        final String methodName                = "refreshEntityReferenceCopy";
        final String guidParameterName         = "typeDefGUID";
        final String nameParameterName         = "typeDefName";
        final String entityParameterName       = "entityGUID";
        final String homeParameterName         = "homeMetadataCollectionId";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateGUID(repositoryName, entityParameterName, entityGUID, methodName);
        repositoryValidator.validateTypeDefIds(repositoryName,
                guidParameterName,
                nameParameterName,
                typeDefGUID,
                typeDefName,
                methodName);
        repositoryValidator.validateHomeMetadataGUID(repositoryName, homeParameterName, homeMetadataCollectionId, methodName);

        /*
         * Send refresh message
         */
        // TODO: implement
        OMRSErrorCode errorCode = OMRSErrorCode.METHOD_NOT_IMPLEMENTED;

        String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                this.getClass().getName(),
                repositoryName);

        throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                this.getClass().getName(),
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction());
    }


    /**
     * Save the relationship as a reference copy.  The id of the home metadata collection is already set up in the
     * relationship.
     *
     * @param userId unique identifier for requesting server.
     * @param relationship relationship to save.
     * @throws InvalidParameterException the relationship is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws TypeErrorException the requested type is not known, or not supported in the metadata repository
     *                            hosting the metadata collection.
     * @throws EntityNotKnownException one of the entities identified by the relationship is not found in the
     *                                   metadata collection.
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                  characteristics in the TypeDef for this relationship's type.
     * @throws HomeRelationshipException the relationship belongs to the local repository so creating a reference
     *                                     copy would be invalid.
     * @throws RelationshipConflictException the new relationship conflicts with an existing relationship.
     * @throws InvalidRelationshipException the new relationship has invalid contents.
     * @throws FunctionNotSupportedException the repository does not support reference copies of instances.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public void saveRelationshipReferenceCopy(String userId,
                                              Relationship   relationship) throws InvalidParameterException,
            RepositoryErrorException,
            TypeErrorException,
            EntityNotKnownException,
            PropertyErrorException,
            HomeRelationshipException,
            RelationshipConflictException,
            InvalidRelationshipException,
            FunctionNotSupportedException,
            UserNotAuthorizedException
    {
        final String  methodName = "saveRelationshipReferenceCopy";
        final String  instanceParameterName = "relationship";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateReferenceInstanceHeader(repositoryName, metadataCollectionId, instanceParameterName, relationship, methodName);

        /*
         * Locate relationship
         */
        // TODO: implement
        OMRSErrorCode errorCode = OMRSErrorCode.METHOD_NOT_IMPLEMENTED;

        String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                this.getClass().getName(),
                repositoryName);

        throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                this.getClass().getName(),
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction());
    }


    /**
     * Remove the reference copy of the relationship from the local repository. This method can be used to
     * remove reference copies from the local cohort, repositories that have left the cohort,
     * or relationships that have come from open metadata archives.
     *
     * @param userId unique identifier for requesting server.
     * @param relationshipGUID the unique identifier for the relationship.
     * @param typeDefGUID the guid of the TypeDef for the relationship used to verify the relationship identity.
     * @param typeDefName the name of the TypeDef for the relationship used to verify the relationship identity.
     * @param homeMetadataCollectionId unique identifier for the home repository for this relationship.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws RelationshipNotKnownException the relationship identifier is not recognized.
     * @throws HomeRelationshipException the relationship belongs to the local repository so creating a reference
     *                                     copy would be invalid.
     * @throws FunctionNotSupportedException the repository does not support reference copies of instances.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public void purgeRelationshipReferenceCopy(String userId,
                                               String   relationshipGUID,
                                               String   typeDefGUID,
                                               String   typeDefName,
                                               String   homeMetadataCollectionId) throws InvalidParameterException,
            RepositoryErrorException,
            RelationshipNotKnownException,
            HomeRelationshipException,
            FunctionNotSupportedException,
            UserNotAuthorizedException
    {
        final String methodName                = "purgeRelationshipReferenceCopy";
        final String guidParameterName         = "typeDefGUID";
        final String nameParameterName         = "typeDefName";
        final String relationshipParameterName = "relationshipGUID";
        final String homeParameterName         = "homeMetadataCollectionId";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateGUID(repositoryName, relationshipParameterName, relationshipGUID, methodName);
        repositoryValidator.validateTypeDefIds(repositoryName,
                guidParameterName,
                nameParameterName,
                typeDefGUID,
                typeDefName,
                methodName);
        repositoryValidator.validateHomeMetadataGUID(repositoryName, homeParameterName, homeMetadataCollectionId, methodName);


        /*
         * Purge relationship
         */
        // TODO: implement
        OMRSErrorCode errorCode = OMRSErrorCode.METHOD_NOT_IMPLEMENTED;

        String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                this.getClass().getName(),
                repositoryName);

        throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                this.getClass().getName(),
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction());
    }


    /**
     * The local repository has requested that the repository that hosts the home metadata collection for the
     * specified relationship sends out the details of this relationship so the local repository can create a
     * reference copy.
     *
     * @param userId unique identifier for requesting server.
     * @param relationshipGUID unique identifier of the relationship.
     * @param typeDefGUID the guid of the TypeDef for the relationship used to verify the relationship identity.
     * @param typeDefName the name of the TypeDef for the relationship used to verify the relationship identity.
     * @param homeMetadataCollectionId unique identifier for the home repository for this relationship.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws RelationshipNotKnownException the relationship identifier is not recognized.
     * @throws HomeRelationshipException the relationship belongs to the local repository so creating a reference
     *                                     copy would be invalid.
     * @throws FunctionNotSupportedException the repository does not support reference copies of instances.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public void refreshRelationshipReferenceCopy(String userId,
                                                 String   relationshipGUID,
                                                 String   typeDefGUID,
                                                 String   typeDefName,
                                                 String   homeMetadataCollectionId) throws InvalidParameterException,
            RepositoryErrorException,
            RelationshipNotKnownException,
            HomeRelationshipException,
            FunctionNotSupportedException,
            UserNotAuthorizedException
    {
        final String methodName                = "refreshRelationshipReferenceCopy";
        final String guidParameterName         = "typeDefGUID";
        final String nameParameterName         = "typeDefName";
        final String relationshipParameterName = "relationshipGUID";
        final String homeParameterName         = "homeMetadataCollectionId";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateGUID(repositoryName, relationshipParameterName, relationshipGUID, methodName);
        repositoryValidator.validateTypeDefIds(repositoryName,
                guidParameterName,
                nameParameterName,
                typeDefGUID,
                typeDefName,
                methodName);
        repositoryValidator.validateHomeMetadataGUID(repositoryName, homeParameterName, homeMetadataCollectionId, methodName);

        /*
         * Process refresh request
         */
        // TODO: implement
        OMRSErrorCode errorCode = OMRSErrorCode.METHOD_NOT_IMPLEMENTED;

        String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                this.getClass().getName(),
                repositoryName);

        throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                this.getClass().getName(),
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction());
    }

    /**
     * Retrieve any mapping that exists for the provided entityTypeGUID (or null if there are none).
     *
     * @param entityTypeGUID the OMRS entityTypeGUID for which to find a mapping
     * @return EntityMapping
     */
    private ImplementedMapping getMappingForEntityType(String entityTypeGUID) {
        ImplementedMapping mapping = null;
        for (ImplementedMapping implementedMapping : implementedMappings) {
            if (entityTypeGUID.equals(implementedMapping.getTypeDef().getGUID())) {
                mapping = implementedMapping;
            }
        }
        return mapping;
    }

    /**
     * Retrieve any mapping that exists for the provided attributeTypeGUID (or null if there are none).
     *
     * @param attributeTypeGUID the GUID of the OMRS attribute type for which to find a mapping
     * @return
     */
    private AttributeMapping getMappingForAttributeType(String attributeTypeGUID) {
        AttributeMapping mapping = null;
        for (ImplementedAttribute implementedAttribute : implementedAttributes) {
            if (attributeTypeGUID.equals(implementedAttribute.getAttributeTypeDef().getGUID())) {
                mapping = implementedAttribute.getAttributeMapping();
            }
        }
        return mapping;
    }

    /**
     * Retrieve a mapping from IGC property name to the OMRS relationship type it represents.
     *
     * @param assetType the IGC asset type for which to find mappings
     * @param userId the userId making the request
     * @return Map<String, RelationshipMapping> - keyed by IGC asset type with values of the RelationshipMappings
     */
    public Map<String, List<RelationshipMapping>> getIgcPropertiesToRelationshipMappings(String assetType, String userId) {

        HashMap<String, List<RelationshipMapping>> map = new HashMap<>();

        List<ReferenceableMapper> mappers = getMappers(assetType, userId);
        for (ReferenceableMapper mapper : mappers) {
            List<RelationshipMapping> relationshipMappings = mapper.getRelationshipMappers();
            for (RelationshipMapping relationshipMapping : relationshipMappings) {
                if (relationshipMapping.getProxyOneMapping().matchesAssetType(assetType)) {
                    List<String> relationshipNamesOne = relationshipMapping.getProxyOneMapping().getIgcRelationshipProperties();
                    for (String relationshipName : relationshipNamesOne) {
                        if (!map.containsKey(relationshipName)) {
                            map.put(relationshipName, new ArrayList<>());
                        }
                        if (!map.get(relationshipName).contains(relationshipMapping)) {
                            map.get(relationshipName).add(relationshipMapping);
                        }
                    }
                }
                if (relationshipMapping.getProxyTwoMapping().matchesAssetType(assetType)) {
                    List<String> relationshipNamesTwo = relationshipMapping.getProxyTwoMapping().getIgcRelationshipProperties();
                    for (String relationshipName : relationshipNamesTwo) {
                        if (!map.containsKey(relationshipName)) {
                            map.put(relationshipName, new ArrayList<>());
                        }
                        if (!map.get(relationshipName).contains(relationshipMapping)) {
                            map.get(relationshipName).add(relationshipMapping);
                        }
                    }
                }
            }
        }

        return map;

    }

    /**
     * Add the type to search based on the provided mapping.
     *
     * @param mapping the mapping on which to base the search
     * @param igcSearch the IGC search object to which to add the criteria
     * @return String - the IGC asset type that will be used for the search
     */
    private String addTypeToSearch(ImplementedMapping mapping, IGCSearch igcSearch) {
        String igcType = DEFAULT_IGC_TYPE;
        if (mapping == null) {
            // If no TypeDef was provided, run against all types
            igcSearch.addType(igcType);
        } else {
            igcType = mapping.getIgcAssetType();
            igcSearch.addType(igcType);
        }
        return igcType;
    }

    /**
     * Retrieve the property mappings from the mapping.
     *
     * @param mapping the mapping from which to retrieve property mappings
     * @param userId the userId making the request
     * @return PropertyMappingSet
     */
    private PropertyMappingSet getEntityPropertiesFromMapping(ImplementedMapping mapping, String userId) {
        PropertyMappingSet propertyMappingSet = null;
        if (mapping != null) {
            EntityMapping entityMapping = mapping.getEntityMapping();
            if (entityMapping != null) {
                propertyMappingSet = entityMapping.getPropertyMappings();
            }
        }
        return propertyMappingSet;
    }

    /**
     * Setup paging properties of the IGC search.
     *
     * @param igcSearch the IGC search object to which to add the criteria
     * @param beginAt the starting index for results
     * @param pageSize the number of results to include in each page
     */
    private void setPagingForSearch(IGCSearch igcSearch, int beginAt, int pageSize) {
        if (pageSize > 0) {
            /* Only set pageSize if it has been provided; otherwise we'll end up defaulting to IGC's
             * minimal pageSize of 10 (so will need to make many calls to get all pages) */
            igcSearch.setPageSize(pageSize);
        } else {
            /* So if none has been specified, we'll set a large pageSize to be able to more efficiently
             * retrieve all pages of results */
            igcSearch.setPageSize(parentConnector.getMaxPageSize());
        }
        igcSearch.setBeginAt(beginAt);
    }

    /**
     * Process the search results into the provided list of EntityDetail objects.
     *
     * @param results the IGC search results
     * @param entityDetails the list of EntityDetails to append
     * @param pageSize the number of results per page (0 for all results)
     * @param userId the user making the request
     */
    private void processResults(ReferenceList results,
                                List<EntityDetail> entityDetails,
                                int pageSize,
                                String userId) throws RepositoryErrorException {

        if (pageSize == 0) {
            // If the provided pageSize was 0, we need to retrieve ALL pages of results...
            results.getAllPages(this.igcRestClient);
        }

        for (Reference reference : results.getItems()) {
            /* Only proceed with retrieving the EntityDetail if the type from IGC is not explicitly
             * a 'main_object' (as these are non-API-accessible asset types in IGC like column analysis master,
             * etc and will simply result in 400-code Bad Request messages from the API) */
            if (!reference.getType().equals(DEFAULT_IGC_TYPE)) {
                EntityDetail ed = null;

                List<ReferenceableMapper> mappers = getMappers(reference.getType(), userId);
                for (ReferenceableMapper mapper : mappers) {
                    log.debug("processResults with mapper: {}", mapper.getClass().getCanonicalName());
                    String idToLookup;
                    if (mapper.igcRidNeedsPrefix()) {
                        log.debug(" ... prefix required, getEntityDetail with: {}", mapper.getIgcRidPrefix() + reference.getId());
                        idToLookup = mapper.getIgcRidPrefix() + reference.getId();
                    } else {
                        log.debug(" ... no prefix required, getEntityDetail with: {}", reference.getId());
                        idToLookup = reference.getId();
                    }
                    try {
                        ed = getEntityDetail(userId, idToLookup, reference);
                    } catch (EntityNotKnownException e) {
                        log.error("Unable to find entity: {}", idToLookup);
                    }
                    if (ed != null) {
                        entityDetails.add(ed);
                    }
                }
            }
        }

        // If we haven't filled a page of results (because we needed to skip some above), recurse...
        if (results.hasMorePages() && entityDetails.size() < pageSize) {
            results.getNextPage(this.igcRestClient);
            processResults(results, entityDetails, pageSize, userId);
        }

    }

    /**
     * Retrieve the listing of implemented mappings that should be used for an entity search, including navigating
     * subtypes when a supertype is the entity type provided.
     *
     * @param entityTypeGUID the GUID of the OMRS entity type for which to search
     * @param userId the userId through which to search
     * @return List<ImplementedMapping>
     */
    private List<ImplementedMapping> getMappingsToSearch(String entityTypeGUID, String userId) throws
            InvalidParameterException,
            RepositoryErrorException,
            UserNotAuthorizedException {

        List<ImplementedMapping> mappingsToSearch = new ArrayList<>();

        // If no entityType was provided, add all implemented types (except Referenceable, as that could itself
        // include many objects that are not implemented)
        if (entityTypeGUID == null) {
            for (ImplementedMapping candidate : implementedMappings) {
                if (candidate.getTypeDef().getCategory().equals(TypeDefCategory.ENTITY_DEF)) {
                    if (!candidate.getTypeDef().getName().equals("Referenceable")) {
                        mappingsToSearch.add(candidate);
                    }
                }
            }
        } else {

            ImplementedMapping mappingExact = getMappingForEntityType(entityTypeGUID);
            String requestedTypeName;
            // If no implemented mapping could be found, at least retrieve the TypeDef for further introspection
            // (so that if it has any implemented subtypes, we can still search for those)
            if (mappingExact == null) {
                TypeDef unimplemented = unimplementedTypeDefs.get(entityTypeGUID);
                requestedTypeName = unimplemented.getName();
            } else {
                requestedTypeName = mappingExact.getTypeDef().getName();
            }

            // Walk the hierarchy of types to ensure we search across all subtypes of the requested TypeDef as well
            List<TypeDef> allEntityTypes = findTypeDefsByCategory(userId, TypeDefCategory.ENTITY_DEF);

            for (TypeDef typeDef : allEntityTypes) {
                ImplementedMapping implementedMapping = getMappingForEntityType(typeDef.getGUID());
                if (implementedMapping != null) {
                    if (repositoryHelper.isTypeOf(metadataCollectionId, typeDef.getName(), requestedTypeName)) {
                        // Add any subtypes of the requested type into the search
                        mappingsToSearch.add(implementedMapping);
                    }
                }
            }

        }

        return mappingsToSearch;

    }

    /**
     * Return the header, classifications and properties of a specific entity, using the provided IGC asset.
     *
     * @param userId unique identifier for requesting user.
     * @param guid String unique identifier for the entity.
     * @param asset the IGC asset for which an EntityDetail should be constructed.
     * @return EntityDetail structure.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     */
    public EntityDetail getEntityDetail(String userId, String guid, Reference asset)
            throws RepositoryErrorException, EntityNotKnownException {

        final String  methodName        = "getEntityDetail";

        log.debug("getEntityDetail with guid = {}", guid);

        EntityDetail detail = null;
        String prefix = getPrefixFromGeneratedId(guid);

        // If we could not find any asset by the provided guid, throw an ENTITY_NOT_KNOWN exception
        if (asset == null) {
            OMRSErrorCode errorCode = OMRSErrorCode.ENTITY_NOT_KNOWN;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(guid,
                    methodName,
                    repositoryName);
            throw new EntityNotKnownException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        } else if (asset.getType().equals(DEFAULT_IGC_TYPE)) {
            /* If the asset type returned has an IGC-listed type of 'main_object', it isn't one that the REST API
             * of IGC supports (eg. a data rule detail object, a column analysis master object, etc)...
             * Trying to further process it will result in failed REST API requests; so we should skip these objects */
            OMRSErrorCode errorCode = OMRSErrorCode.INVALID_ENTITY_FROM_STORE;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(guid,
                    repositoryName,
                    methodName,
                    asset.toString());
            throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        } else {

            // Otherwise, retrieve the mapping dynamically based on the type of asset
            ReferenceableMapper referenceMapper = getMapperForParameters(asset, prefix, userId);

            if (referenceMapper != null) {
                // 2. Apply the mapping to the object, and retrieve the resulting EntityDetail
                detail = referenceMapper.getOMRSEntityDetail();
            } else {
                OMRSErrorCode errorCode = OMRSErrorCode.TYPEDEF_NOT_KNOWN_FOR_INSTANCE;
                String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(
                        prefix + asset.getType(),
                        "IGC asset",
                        methodName,
                        repositoryName);
                throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                        this.getClass().getName(),
                        methodName,
                        errorMessage,
                        errorCode.getSystemAction(),
                        errorCode.getUserAction());
            }

        }

        return detail;

    }

    /**
     * Retrieves the Mapper that can be used for the provided parameters (or null if none exists).
     *
     * @param igcObject the IGC asset
     * @param prefix the prefix used for the asset (if any; null otherwise)
     * @param userId the user making the request
     * @return
     */
    public ReferenceableMapper getMapperForParameters(Reference igcObject, String prefix, String userId) {

        String igcAssetType = igcObject.getType();

        log.debug("Looking for mapper for type {} with prefix {}", igcAssetType, prefix);

        List<ReferenceableMapper> mappers = getMappers(igcAssetType, userId);

        ReferenceableMapper candidateMapper = null;
        for (int i = 0; i < mappers.size(); i++) {
            candidateMapper = mappers.get(i);
            String mapperPrefix = candidateMapper.getIgcRidPrefix();
            if ( (prefix == null && mapperPrefix == null)
                    || (prefix != null && mapperPrefix != null && mapperPrefix.equals(prefix)) ) {
                // If we didn't receive any prefix and this Mapper doesn't use a prefix, use it
                // or if we did receive a prefix and it matches this Mapper's prefix, use it
                break;
            }
            // Otherwise keep looping until we find one that meets above criteria (or we run out of options)
        }

        ReferenceableMapper referenceMapper = null;
        if (candidateMapper != null) {
            log.debug("Found mapper class: {}", candidateMapper.getClass().getCanonicalName());
            // Translate the provided asset to a base asset type for the mapper, if needed
            // (if not needed the 'getBaseIgcAssetFromAlternative' is effecitively a NOOP and gives back same object)
            referenceMapper = candidateMapper.initialize(
                    candidateMapper.getBaseIgcAssetFromAlternative(igcObject),
                    igcObject);
        } else {
            log.debug("No mapper class found!");
        }
        return referenceMapper;

    }

    /**
     * Retrieves the classes to use for mapping the provided IGC asset type to an OMRS entity.
     *
     * @param igcAssetType the name of the IGC asset type
     * @return List<Class>
     */
    public List<ReferenceableMapper> getMappers(String igcAssetType, String userId) {
        ArrayList<ReferenceableMapper> mappers = new ArrayList<>();
        for (ImplementedMapping mapping : implementedMappings) {
            String mappingAssetType = mapping.getIgcAssetType();
            // For now skip over the default Referenceable mapping, if no other candidate is found we will add
            // this default mapping back in below
            if (mappingAssetType != null && !mappingAssetType.equals(IGCOMRSMetadataCollection.DEFAULT_IGC_TYPE)) {
                if (mapping.matchesAssetType(igcAssetType)) {
                    log.debug(" ... adding candidate: {}", mapping.getEntityMapping().getClass().getCanonicalName());
                    mappers.add((ReferenceableMapper) mapping.getEntityMapping());
                } else {
                    for (String otherType : mapping.getOtherIgcAssetTypes()) {
                        if (igcAssetType.equals(otherType)) {
                            log.debug(" ... adding other candidate: {}", mapping.getEntityMapping().getClass().getCanonicalName());
                            mappers.add((ReferenceableMapper) mapping.getEntityMapping());
                        }
                    }
                }
            }
        }
        if (mappers.isEmpty()) {
            try {
                ReferenceableMapper defaultMapper = (ReferenceableMapper) ImplementedMapping.getEntityMapper(
                        Class.forName(MAPPING_PKG + "entities.ReferenceableMapper"),
                        (IGCOMRSRepositoryConnector)parentConnector,
                        userId
                );
                mappers.add(defaultMapper);
            } catch (ClassNotFoundException e) {
                log.error("Unable to find default ReferenceableMapper class: " + MAPPING_PKG + "entities.ReferenceableMapper", e);
            }
        }
        return mappers;
    }

    /**
     * Retrieves a RelationshipMapping by OMRS relationship type from those that are listed as implemented.
     *
     * @param omrsRelationshipType the name of the OMRS relationship type for which to retrieve a mapping
     * @return RelationshipMapping
     */
    public RelationshipMapping getRelationshipMapper(String omrsRelationshipType,
                                                     String proxyOneType,
                                                     String proxyTwoType) {
        RelationshipMapping found = null;
        for (ImplementedMapping mapping : implementedMappings) {
            RelationshipMapping candidate = mapping.getRelationshipMapping();
            // If the mapping has a relationship mapping, and its type equals the one we're looking for,
            // set it to found and short-circuit out of the loop
            if (candidate != null) {
                if (matchingRelationshipMapper(candidate, omrsRelationshipType, proxyOneType, proxyTwoType)) {
                    found = candidate;
                    break;
                } else if (candidate.hasSubTypes()) {
                    for (RelationshipMapping subMapping : candidate.getSubTypes()) {
                        if (matchingRelationshipMapper(subMapping, omrsRelationshipType, proxyOneType, proxyTwoType)) {
                            found = subMapping;
                            break;
                        }
                    }
                } else if (candidate.hasRelationshipLevelAsset()) {
                    String relationshipLevelAssetType = candidate.getRelationshipLevelIgcAsset();
                    if (proxyOneType.equals(relationshipLevelAssetType) && proxyTwoType.equals(relationshipLevelAssetType)) {
                        found = candidate;
                        break;
                    }
                }

            }
        }
        return found;
    }

    /**
     * Indicates whether the provided relationship mapping matches the provided criteria.
     *
     * @param candidate the relationship mapping to check
     * @param omrsRelationshipType the OMRS relationship type to confirm
     * @param proxyOneType the asset type of endpoint 1 of the relationship to confirm
     * @param proxyTwoType the asset type of endpoint 2 of the relationship to confirm
     * @return
     */
    private boolean matchingRelationshipMapper(RelationshipMapping candidate,
                                               String omrsRelationshipType,
                                               String proxyOneType,
                                               String proxyTwoType) {
        return (candidate != null
                && candidate.getOmrsRelationshipType().equals(omrsRelationshipType)
                && candidate.getProxyOneMapping().matchesAssetType(proxyOneType)
                && candidate.getProxyTwoMapping().matchesAssetType(proxyTwoType));
    }

    /**
     * Retrieves the OMRS TypeDefs that are mapped to by the provided IGC asset display name.
     *
     * @param igcAssetName the display name of the IGC asset type
     * @return List<TypeDef>
     */
    public List<TypeDef> getTypeDefsForAssetName(String igcAssetName) {
        ArrayList<TypeDef> typeDefs = new ArrayList<>();
        for (ImplementedMapping mapping : implementedMappings) {
            if (igcAssetName.equals(mapping.getIgcAssetTypeDisplayName())) {
                typeDefs.add(mapping.getTypeDef());
            }
        }
        return typeDefs;
    }

    /**
     * Retrieves the IGC asset type from the provided IGC asset display name (only for those assets that have
     * a mapping implemented). If none is found, will return null.
     *
     * @param igcAssetName the display name of the IGC asset type
     * @return String
     */
    public String getIgcAssetTypeForAssetName(String igcAssetName) {
        String assetType = null;
        for (ImplementedMapping mapping : implementedMappings) {
            if (igcAssetName.equals(mapping.getIgcAssetTypeDisplayName())) {
                assetType = mapping.getIgcAssetType();
                break;
            }
        }
        return assetType;
    }

    /**
     * Adds the provided value to the search criteria for IGC.
     *
     * @param igcSearchConditionSet the search conditions to which to add the criteria
     * @param omrsPropertyName the OMRS property name to search
     * @param igcProperties the list of IGC properties to which to add for inclusion in the IGC search
     * @param propertyMappingSet the property mappings
     * @param value the value for which to search
     */
    public void addSearchConditionFromValue(IGCSearchConditionSet igcSearchConditionSet,
                                            String omrsPropertyName,
                                            List<String> igcProperties,
                                            PropertyMappingSet propertyMappingSet,
                                            InstancePropertyValue value) {

        String igcPropertyName = propertyMappingSet.getIgcPropertyName(omrsPropertyName);
        if (igcPropertyName != null) {
            igcProperties.add(igcPropertyName);
            InstancePropertyCategory category = value.getInstancePropertyCategory();
            switch (category) {
                case PRIMITIVE:
                    PrimitivePropertyValue actualValue = (PrimitivePropertyValue) value;
                    PrimitiveDefCategory primitiveType = actualValue.getPrimitiveDefCategory();
                    switch (primitiveType) {
                        case OM_PRIMITIVE_TYPE_BOOLEAN:
                        case OM_PRIMITIVE_TYPE_BYTE:
                        case OM_PRIMITIVE_TYPE_CHAR:
                            igcSearchConditionSet.addCondition(new IGCSearchCondition(
                                    igcPropertyName,
                                    "=",
                                    actualValue.getPrimitiveValue().toString()
                            ));
                            break;
                        case OM_PRIMITIVE_TYPE_SHORT:
                        case OM_PRIMITIVE_TYPE_INT:
                        case OM_PRIMITIVE_TYPE_LONG:
                        case OM_PRIMITIVE_TYPE_FLOAT:
                        case OM_PRIMITIVE_TYPE_DOUBLE:
                        case OM_PRIMITIVE_TYPE_BIGINTEGER:
                        case OM_PRIMITIVE_TYPE_BIGDECIMAL:
                            igcSearchConditionSet.addCondition(new IGCSearchCondition(
                                    igcPropertyName,
                                    "=",
                                    actualValue.getPrimitiveValue().toString()
                            ));
                            break;
                        case OM_PRIMITIVE_TYPE_DATE:
                            Date date = (Date) actualValue.getPrimitiveValue();
                            igcSearchConditionSet.addCondition(new IGCSearchCondition(
                                    igcPropertyName,
                                    "=",
                                    "" + date.getTime()
                            ));
                            break;
                        case OM_PRIMITIVE_TYPE_STRING:
                        default:
                            igcSearchConditionSet.addCondition(new IGCSearchCondition(
                                    igcPropertyName,
                                    "like %{0}%",
                                    actualValue.getPrimitiveValue().toString()
                            ));
                            break;
                    }
                    break;
                case ENUM:
                    igcSearchConditionSet.addCondition(new IGCSearchCondition(
                            igcPropertyName,
                            "=",
                            ((EnumPropertyValue) value).getSymbolicName()
                    ));
                    break;
                case STRUCT:
                    Map<String, InstancePropertyValue> structValues = ((StructPropertyValue) value).getAttributes().getInstanceProperties();
                    for (Map.Entry<String, InstancePropertyValue> nextEntry : structValues.entrySet()) {
                        addSearchConditionFromValue(
                                igcSearchConditionSet,
                                nextEntry.getKey(),
                                igcProperties,
                                propertyMappingSet,
                                nextEntry.getValue()
                        );
                    }
                    break;
                case MAP:
                    Map<String, InstancePropertyValue> mapValues = ((MapPropertyValue) value).getMapValues().getInstanceProperties();
                    for (Map.Entry<String, InstancePropertyValue> nextEntry : mapValues.entrySet()) {
                        addSearchConditionFromValue(
                                igcSearchConditionSet,
                                nextEntry.getKey(),
                                igcProperties,
                                propertyMappingSet,
                                nextEntry.getValue()
                        );
                    }
                    break;
                case ARRAY:
                    Map<String, InstancePropertyValue> arrayValues = ((ArrayPropertyValue) value).getArrayValues().getInstanceProperties();
                    for (Map.Entry<String, InstancePropertyValue> nextEntry : arrayValues.entrySet()) {
                        addSearchConditionFromValue(
                                igcSearchConditionSet,
                                igcPropertyName,
                                igcProperties,
                                propertyMappingSet,
                                nextEntry.getValue()
                        );
                    }
                    break;
                default:
                    // Do nothing
                    break;
            }
        }

    }

    /**
     * Retrieves the RID from a generated GUID (or the GUID if it is not generated).
     *
     * @param guid the guid to translate
     * @return String
     */
    public static final String getRidFromGeneratedId(String guid) {
        if (isGeneratedGUID(guid)) {
            return guid
                    .substring(guid.indexOf(GENERATED_TYPE_POSTFIX) + GENERATED_TYPE_POSTFIX.length());
        } else {
            return guid;
        }
    }

    /**
     * Retrieves the generated prefix from a generated GUID (or null if the GUID is not generated).
     *
     * @param guid the guid from which to retrieve the prefix
     * @return String
     */
    public static final String getPrefixFromGeneratedId(String guid) {
        if (isGeneratedGUID(guid)) {
            return guid
                    .substring(0, guid.indexOf(GENERATED_TYPE_POSTFIX) + GENERATED_TYPE_POSTFIX.length());
        } else {
            return null;
        }
    }

    /**
     * Indicates whether the provided GUID was generated (true) or not (false).
     *
     * @param guid the guid to test
     * @return boolean
     */
    public static final boolean isGeneratedGUID(String guid) {
        return guid.startsWith(GENERATED_TYPE_PREFIX);
    }

    /**
     * Generates a unique type prefix for RIDs based on the provided moniker.
     *
     * @param moniker a repeatable way by which to refer to the type
     * @return String
     */
    public static final String generateTypePrefix(String moniker) {
        return GENERATED_TYPE_PREFIX + moniker + GENERATED_TYPE_POSTFIX;
    }

    /**
     * Retrieve an OMRS asset stub (shadow copy of last version of an asset) for the provided asset.
     * If there is no existing stub, will return null.
     *
     * @param asset the asset for which to retrieve the OMRS stub
     * @return OMRSStub
     */
    public OMRSStub getOMRSStubForAsset(Reference asset) {

        // We need to translate the provided asset into a unique name for the stub
        String stubName = getStubNameFromAsset(asset);
        IGCSearchCondition condition = new IGCSearchCondition(
                "name",
                "=",
                stubName
        );
        String[] properties = new String[]{ "$sourceRID", "$sourceType", "$payload" };
        IGCSearchConditionSet conditionSet = new IGCSearchConditionSet(condition);
        IGCSearch igcSearch = new IGCSearch("$OMRS-Stub", properties, conditionSet);
        ReferenceList results = igcRestClient.search(igcSearch);
        OMRSStub stub = null;
        if (results.getPaging().getNumTotal() > 0) {
            if (results.getPaging().getNumTotal() > 1) {
                log.warn("Found multiple stubs for asset, taking only the first: {}", stubName);
            }
            stub = (OMRSStub) results.getItems().get(0);
        } else {
            log.info("No stub found for asset: {}", stubName);
        }
        return stub;

    }

    /**
     * Update (or create if it does not already exist) the OMRS asset stub for the provided asset.
     * (Note that this method assumes you have already retrieved the full asset being provided.)
     *
     * @param asset the asset for which to upsert the OMRS stub
     * @return String the Repository ID (RID) of the OMRS stub
     */
    public String upsertOMRSStubForAsset(Reference asset) {

        String stubName = getStubNameFromAsset(asset);

        // Get the full asset details as a singular JSON payload
        String payload = igcRestClient.getValueAsJSON(asset);

        // Construct the asset XML document, including the full asset payload
        StringWriter stringWriter = new StringWriter();
        try {

            XMLStreamWriter xmlStreamWriter = xmlOutputFactory.createXMLStreamWriter(stringWriter);
            xmlStreamWriter.writeStartDocument("UTF-8", "1.0");

            xmlStreamWriter.writeStartElement("doc");
            xmlStreamWriter.writeNamespace("xmlns", "http://www.ibm.com/iis/flow-doc");

            xmlStreamWriter.writeStartElement("assets");
            xmlStreamWriter.writeStartElement("asset");

            xmlStreamWriter.writeAttribute("class", "$OMRS-Stub");
            xmlStreamWriter.writeAttribute("repr", stubName);
            xmlStreamWriter.writeAttribute("ID", "stub1");

            addAttributeToAssetXML(xmlStreamWriter, "name", stubName);
            addAttributeToAssetXML(xmlStreamWriter, "$sourceType", asset.getType());
            addAttributeToAssetXML(xmlStreamWriter, "$sourceRID", asset.getId());
            addAttributeToAssetXML(xmlStreamWriter, "$payload", payload);

            xmlStreamWriter.writeEndElement(); // </asset>
            xmlStreamWriter.writeEndElement(); // </assets>

            xmlStreamWriter.writeStartElement("importAction");
            xmlStreamWriter.writeAttribute("completeAssetIDs", "stub1");
            xmlStreamWriter.writeEndElement(); // </importAction>

            xmlStreamWriter.writeEndElement(); // </doc>

            xmlStreamWriter.writeEndDocument();
            xmlStreamWriter.flush();
            xmlStreamWriter.close();

        } catch (XMLStreamException e) {
            log.error("Unable to write XML stream: {}", asset, e);
        }

        String stubXML = stringWriter.getBuffer().toString();
        log.debug("Constructed XML for stub: {}", stubXML);

        // Upsert using the constructed asset XML
        JsonNode results = igcRestClient.upsertOpenIgcAsset(stubXML);

        return results.path("stub1").asText();

    }

    /**
     * Adds the provided attribute to the asset XML being constructed.
     *
     * @param xmlStreamWriter the asset XML being constructed
     * @param attrName the name of the attribute to add
     * @param attrValue the value of the attribute
     * @throws XMLStreamException
     */
    private void addAttributeToAssetXML(XMLStreamWriter xmlStreamWriter, String attrName, String attrValue) throws XMLStreamException {
        xmlStreamWriter.writeStartElement("attribute");
        xmlStreamWriter.writeAttribute("name", attrName);
        xmlStreamWriter.writeAttribute("value", attrValue);
        xmlStreamWriter.writeEndElement(); // </attribute>
    }

    /**
     * Delete the OMRS asset stub for the provided asset details (cannot require the asset itself since it has
     * already been removed).
     *
     * @param rid the Repository ID (RID) of the asset for which to delete the OMRS stub
     * @param assetType the IGC asset type of the asset for which to delete the OMRS stub
     * @return boolean - true on successful deletion, false otherwise
     */
    public boolean deleteOMRSStubForAsset(String rid, String assetType) {

        String stubName = getStubNameForAsset(rid, assetType);

        // Construct the asset XML document, including the full asset payload
        StringWriter stringWriter = new StringWriter();
        try {

            XMLStreamWriter xmlStreamWriter = xmlOutputFactory.createXMLStreamWriter(stringWriter);
            xmlStreamWriter.writeStartDocument("UTF-8", "1.0");

            xmlStreamWriter.writeStartElement("doc");
            xmlStreamWriter.writeNamespace("xmlns", "http://www.ibm.com/iis/flow-doc");

            xmlStreamWriter.writeStartElement("assets");
            xmlStreamWriter.writeStartElement("asset");

            xmlStreamWriter.writeAttribute("class", "$OMRS-Stub");
            xmlStreamWriter.writeAttribute("repr", stubName);
            xmlStreamWriter.writeAttribute("ID", "stub1");

            addAttributeToAssetXML(xmlStreamWriter, "name", stubName);

            xmlStreamWriter.writeEndElement(); // </asset>
            xmlStreamWriter.writeEndElement(); // </assets>

            xmlStreamWriter.writeStartElement("assetsToDelete");
            xmlStreamWriter.writeCharacters("stub1");
            xmlStreamWriter.writeEndElement(); // </assetsToDelete>

            xmlStreamWriter.writeEndElement(); // </doc>

            xmlStreamWriter.writeEndDocument();
            xmlStreamWriter.flush();
            xmlStreamWriter.close();

        } catch (XMLStreamException e) {
            log.error("Unable to write XML stream.", e);
        }

        String stubXML = stringWriter.getBuffer().toString();
        log.debug("Constructed XML for stub deletion: {}", stubXML);

        // Delete using the constructed asset XML
        return igcRestClient.deleteOpenIgcAsset(stubXML);

    }

    /**
     * Construct the unique name for the OMRS stub based on the provided asset.
     *
     * @param asset the asset for which to construct the unique OMRS stub name
     * @return String
     */
    public static String getStubNameFromAsset(Reference asset) {
        return getStubNameForAsset(asset.getId(), asset.getType());
    }

    /**
     * Construct the unique name for the OMRS stub based on the provided asset information.
     *
     * @param rid the Repository ID (RID) of the asset for which to construct the unique OMRS stub name
     * @param assetType the asset type (REST form) of the asset for which to construct the unique OMRS stub name
     * @return String
     */
    public static String getStubNameForAsset(String rid, String assetType) {
        return assetType + "_" + rid;
    }

    /**
     * Retrieve all of the asset details, including all relationships, from the RID.
     * <br><br>
     * Note that this is quite a heavy operation, relying on multiple REST calls, to build up what could be a very
     * large object; to simply retrieve the details without all relationships, see getAssetDetails.
     *
     * @param rid the Repository ID (RID) of the asset for which to retrieve all details
     * @return Reference - the object including all of its details and relationships
     */
    public Reference getFullAssetDetails(String rid) {

        // Start by retrieving the asset header, so we can introspect the class itself
        Reference assetRef = igcRestClient.getAssetRefById(rid);
        Reference fullAsset = null;

        if (assetRef != null) {

            // Introspect the full list of properties from the POJO of the asset
            Class pojoClass = igcRestClient.getPOJOForType(assetRef.getType());
            if (pojoClass != null) {
                List<String> allProps = Reference.getAllPropertiesFromPOJO(pojoClass);

                // Retrieve all asset properties, via search, as this will allow larger page
                // retrievals (and therefore be overall more efficient) than going by the GET of the asset
                fullAsset = assetRef.getAssetWithSubsetOfProperties(
                        igcRestClient,
                        allProps.toArray(new String[0]),
                        igcRestClient.getDefaultPageSize()
                );

                if (fullAsset != null) {

                    // Iterate through all the paged properties and retrieve all pages for each
                    List<String> allPaged = Reference.getPagedRelationalPropertiesFromPOJO(pojoClass);
                    for (String pagedProperty : allPaged) {
                        ReferenceList pagedValue = (ReferenceList) fullAsset.getPropertyByName(pagedProperty);
                        if (pagedValue != null) {
                            pagedValue.getAllPages(igcRestClient);
                        }
                    }

                    // Set the asset as fully retrieved, so we do not attempt to retrieve parts of it again
                    fullAsset.setFullyRetrieved();

                }
            } else {
                log.debug("No registered POJO for asset type {} -- returning basic reference.", assetRef.getType());
                fullAsset = assetRef;
            }

        } else {
            log.info("Unable to retrieve any asset with RID {} -- assume it was deleted.", rid);
        }

        return fullAsset;

    }

}
