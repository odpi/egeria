/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.graphrepository.repositoryconnector;

import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.ClassificationOrigin;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntitySummary;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceGraph;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProvenanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.AttributeTypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.AttributeTypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.ClassificationDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefAttribute;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefGallery;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefPatch;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefSummary;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollectionBase;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryValidator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;


/**
 * The GraphOMRSMetadataCollection provides a local open metadata repository that uses a graph store as its
 * persistence layer.
 */
public class GraphOMRSMetadataCollection extends OMRSMetadataCollectionBase {

    private static final Logger log = LoggerFactory.getLogger(GraphOMRSMetadataCollection.class);

    private GraphOMRSMetadataStore    graphStore  = null;
    private OMRSAuditLog              auditLog = null;

    /**
     * Constructor ensures the metadata collection is linked to its connector and knows its metadata collection Id.
     *
     * @param parentConnector      - connector that this metadata collection supports.  The connector has the information
     *                             to call the metadata repository.
     * @param repositoryName       - name of the repository - used for logging.
     * @param repositoryHelper     - class used to build type definitions and instances.
     * @param repositoryValidator  - class used to validate type definitions and instances.
     * @param metadataCollectionId - unique Identifier of the metadata collection Id.
     */
    public GraphOMRSMetadataCollection(GraphOMRSRepositoryConnector parentConnector,
                                       String                       repositoryName,
                                       OMRSRepositoryHelper         repositoryHelper,
                                       OMRSRepositoryValidator      repositoryValidator,
                                       String                       metadataCollectionId,
                                       OMRSAuditLog                 auditLog)


    {

        /*
         * The metadata collection Id is the unique Id for the metadata collection.  It is managed by the super class.
         */
        super(parentConnector, repositoryName, repositoryHelper, repositoryValidator, metadataCollectionId);

        final String methodName = "GraphOMRSMetadataCollection";
        /*
         * Save parentConnector since this has the connection information and access to the metadata about the
         * metadata cluster.
         */
        this.parentConnector = parentConnector;

        this.auditLog = auditLog;

        try {
            this.graphStore = new GraphOMRSMetadataStore(metadataCollectionId, repositoryName, repositoryHelper, auditLog);
        }
        catch(RepositoryErrorException e) {
            log.error("{} could not create graph metadata collection for repository name {}", methodName, repositoryName);
            // Little point throwing the exception any higher here - the error has been logged at all levels;
        }
    }


    // verifyTypeDef will always return true because all knowledge of types is delegated to the RCM.
    public boolean verifyTypeDef(String  userId,
                                 TypeDef typeDef)
            throws
            InvalidParameterException,
            RepositoryErrorException,
            InvalidTypeDefException,
            UserNotAuthorizedException
    {
        final String methodName = "verifyTypeDef";
        final String typeDefParameterName = "typeDef";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateTypeDef(repositoryName, typeDefParameterName, typeDef, methodName);

        TypeDefCategory typeDefCategory = typeDef.getCategory();
        switch (typeDefCategory) {
            case ENTITY_DEF:
                // Create indexes for entity primitive properties
                graphStore.createEntityIndexes(typeDef);
                break;
            case RELATIONSHIP_DEF:
                // Create indexes for relationship primitive properties
                graphStore.createRelationshipIndexes(typeDef);
                break;
            case CLASSIFICATION_DEF:
                // Create indexes for classification primitive properties
                graphStore.createClassificationIndexes(typeDef);
                break;
        }



        return true;
    }

    // verifyAttributeTypeDef will always return true because all knowledge of types is delegated to the RCM.
    public boolean verifyAttributeTypeDef(String           userId,
                                          AttributeTypeDef attributeTypeDef)
            throws
            InvalidParameterException,
            RepositoryErrorException,
            InvalidTypeDefException,
            UserNotAuthorizedException
    {
        final String methodName = "verifyAttributeTypeDef";
        final String typeDefParameterName = "attributeTypeDef";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateAttributeTypeDef(repositoryName, typeDefParameterName, attributeTypeDef, methodName);

        /*
         * Perform operation
         */
        return true;
    }

    // getAttributeTypeDef will always delegate to the RCM.
    public TypeDef getTypeDefByName(String userId,
                                    String name)
            throws
            InvalidParameterException,
            RepositoryErrorException,
            UserNotAuthorizedException
    {
        final String methodName = "getTypeDefByName";
        final String nameParameterName = "name";


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

        return repositoryHelper.getTypeDefByName(repositoryName, name);
    }

    // getAttributeTypeDef will always delegate to the RCM.
    public AttributeTypeDef getAttributeTypeDefByName(String userId,
                                                      String name)
            throws
            InvalidParameterException,
            RepositoryErrorException,
            UserNotAuthorizedException
    {
        final String methodName = "getAttributeTypeDefByName";
        final String nameParameterName = "name";


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

        return repositoryHelper.getAttributeTypeDefByName(repositoryName, name);
    }

    // getTypeDef will always delegate to the RCM.
    public TypeDef getTypeDefByGUID(String userId,
                                    String guid)
            throws
            InvalidParameterException,
            RepositoryErrorException,
            TypeDefNotKnownException,
            UserNotAuthorizedException
    {
        final String methodName = "getTypeDefByGUID";
        final String guidParameterName = "guid";


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
        try {
            return repositoryHelper.getTypeDef(repositoryName, guidParameterName, guid, methodName);
        }
        catch (TypeErrorException e) {
            OMRSErrorCode errorCode = OMRSErrorCode.TYPEDEF_NOT_KNOWN;

            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                    this.getClass().getName(),
                    repositoryName);

            throw new TypeDefNotKnownException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }
    }

    // getAttributeTypeDef will always delegate to the RCM.
    public AttributeTypeDef getAttributeTypeDefByGUID(String userId,
                                                      String guid)
            throws
            InvalidParameterException,
            RepositoryErrorException,
            TypeDefNotKnownException,
            UserNotAuthorizedException
    {
        final String methodName = "getAttributeTypeDefByGUID";
        final String guidParameterName = "guid";

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

        try {
            return repositoryHelper.getAttributeTypeDef(repositoryName, guid, methodName);
        }
        catch (TypeErrorException e) {
            OMRSErrorCode errorCode = OMRSErrorCode.ATTRIBUTE_TYPEDEF_NOT_KNOWN;

            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                    this.getClass().getName(),
                    repositoryName);

            throw new TypeDefNotKnownException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }
    }

    // findTypeDefsByCategory will always delegate to the RCM.
    public List<TypeDef> findTypeDefsByCategory(String userId,
                                                TypeDefCategory category)
            throws
            InvalidParameterException,
            RepositoryErrorException,
            UserNotAuthorizedException
    {
        final String methodName = "findTypeDefsByCategory";
        final String categoryParameterName = "category";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateTypeDefCategory(repositoryName, categoryParameterName, category, methodName);

        /*
         * Perform operation
         */

        TypeDefGallery activeTypes = repositoryHelper.getActiveTypeDefGallery();
        List<TypeDef> allTypes = activeTypes.getTypeDefs();
        if (allTypes == null)
            return null;

        List<TypeDef> typesForCategory = new ArrayList<>();

        if (!allTypes.isEmpty()) {
            for (TypeDef typeDef : allTypes) {
                if (typeDef != null) {
                    if (typeDef.getCategory() == category) {
                        typesForCategory.add(typeDef);
                    }
                }
            }
        }

        if (typesForCategory.isEmpty()) {
            typesForCategory = null;
        }

        return typesForCategory;

    }

    // findAttributeTypeDefsByCategory will always delegate to the RCM.
    public List<AttributeTypeDef> findAttributeTypeDefsByCategory(String userId,
                                                                  AttributeTypeDefCategory category)
            throws
            InvalidParameterException,
            RepositoryErrorException,
            UserNotAuthorizedException
    {
        final String methodName = "findAttributeTypeDefsByCategory";
        final String categoryParameterName = "category";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateAttributeTypeDefCategory(repositoryName, categoryParameterName, category, methodName);

        /*
         * Perform operation
         */
        TypeDefGallery activeTypes = repositoryHelper.getActiveTypeDefGallery();
        List<AttributeTypeDef> allAttributeTypes = activeTypes.getAttributeTypeDefs();
        if (allAttributeTypes == null)
            return null;
        List<AttributeTypeDef> typesForCategory = new ArrayList<>();

        if (!allAttributeTypes.isEmpty()) {
            for (AttributeTypeDef attributeTypeDef : allAttributeTypes) {
                if (attributeTypeDef != null) {
                    if (attributeTypeDef.getCategory() == category) {
                        typesForCategory.add(attributeTypeDef);
                    }
                }
            }
        }

        if (typesForCategory.isEmpty()) {
            typesForCategory = null;
        }

        return typesForCategory;
    }


    // addTypeDef - not expected but support anyway
    public void addTypeDef(String  userId,
                           TypeDef newTypeDef)
            throws
            InvalidParameterException,
            RepositoryErrorException,
            TypeDefKnownException,
            TypeDefConflictException,
            InvalidTypeDefException,
            UserNotAuthorizedException
    {
        final String methodName = "addTypeDef";
        final String typeDefParameterName = "newTypeDef";


        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateTypeDef(repositoryName, typeDefParameterName, newTypeDef, methodName);
        repositoryValidator.validateUnknownTypeDef(repositoryName, typeDefParameterName, newTypeDef, methodName);

        /*
         * Perform operation
         */
        String typeDefName = newTypeDef.getName();
        TypeDef existingTypeDef = repositoryHelper.getTypeDefByName(repositoryName, typeDefName);
        // Expecting to find type in RCM...

        if (existingTypeDef != null) {
            OMRSErrorCode errorCode = OMRSErrorCode.TYPEDEF_ALREADY_DEFINED;
            String errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage(newTypeDef.getName(),
                    newTypeDef.getGUID(),
                    repositoryName);

            throw new TypeDefKnownException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        } else {
            // Cannot add a type def to this metadataCollection
            OMRSErrorCode errorCode = OMRSErrorCode.METHOD_NOT_IMPLEMENTED;
            String errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage(newTypeDef.getName(),
                    newTypeDef.getGUID(),
                    repositoryName);

            throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }
    }

    // deleteTypeDef - not expected but support anyway
    public void deleteTypeDef(String userId,
                              String obsoleteTypeDefGUID,
                              String obsoleteTypeDefName)
            throws
            InvalidParameterException,
            RepositoryErrorException,
            UserNotAuthorizedException
    {
        final String methodName = "deleteTypeDef";
        final String guidParameterName = "obsoleteTypeDefGUID";
        final String nameParameterName = "obsoleteTypeDefName";

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
        // Cannot delete a type def via this metadataCollection
        OMRSErrorCode errorCode = OMRSErrorCode.METHOD_NOT_IMPLEMENTED;
        String errorMessage = errorCode.getErrorMessageId()
                + errorCode.getFormattedErrorMessage(obsoleteTypeDefName,
                obsoleteTypeDefGUID,
                repositoryName);

        throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                this.getClass().getName(),
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction());

    }


    // findTypeDefsByProperty
    public List<TypeDef> findTypeDefsByProperty(String userId,
                                                TypeDefProperties matchCriteria)
            throws
            InvalidParameterException,
            RepositoryErrorException,
            UserNotAuthorizedException
    {
        final String methodName = "findTypeDefsByProperty";
        final String matchCriteriaParameterName = "matchCriteria";

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
        TypeDefGallery activeTypes = repositoryHelper.getActiveTypeDefGallery();
        List<TypeDef> allTypes = activeTypes.getTypeDefs();
        if (allTypes == null)
            return null;

        List<TypeDef> typesMatchProperties = new ArrayList<>();
        Set<String> propertyNames = matchCriteria.getTypeDefProperties().keySet();

        if (!allTypes.isEmpty()) {
            for (TypeDef typeDef : allTypes) {
                if (typeDef != null) {
                    List<TypeDefAttribute> typeDefAttributes = typeDef.getPropertiesDefinition();
                    boolean allPropertiesMatch = true;

                    for (String propertyName : propertyNames) {
                        boolean thisPropertyMatches = false;

                        if (propertyName != null) {
                            for (TypeDefAttribute attribute : typeDefAttributes) {
                                if (attribute != null) {
                                    if (propertyName.equals(attribute.getAttributeName())) {
                                        thisPropertyMatches = true;
                                        break;
                                    }
                                }
                            }

                            if (!thisPropertyMatches) {
                                allPropertiesMatch = false;
                                break;
                            }
                        }
                    }

                    if (allPropertiesMatch) {
                        typesMatchProperties.add(typeDef);
                    }
                }
            }
        }

        if (typesMatchProperties.isEmpty()) {
            typesMatchProperties = null;
        }

        return typesMatchProperties;
    }


    // findTypesByExternalID
    public List<TypeDef> findTypesByExternalID(String userId,
                                               String standard,
                                               String organization,
                                               String identifier)
            throws
            InvalidParameterException,
            RepositoryErrorException,
            UserNotAuthorizedException
    {
        final String methodName = "findTypesByExternalID";

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
        return repositoryHelper.getMatchingActiveTypes(repositoryName, standard, organization, identifier, methodName);
    }


    // searchForTypeDefs
    public List<TypeDef> searchForTypeDefs(String userId,
                                           String searchCriteria)
            throws
            InvalidParameterException,
            RepositoryErrorException,
            UserNotAuthorizedException
    {
        final String methodName = "searchForTypeDefs";
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
        TypeDefGallery activeTypes = repositoryHelper.getActiveTypeDefGallery();
        List<TypeDef> allTypes = activeTypes.getTypeDefs();
        if (allTypes == null)
            return null;

        List<TypeDef> matchedTypeDefs = new ArrayList<>();

        for (TypeDef typeDef : allTypes) {
            if (typeDef != null) {
                if (typeDef.getName().matches(searchCriteria)) {
                    matchedTypeDefs.add(typeDef);
                }
            }
        }

        if (matchedTypeDefs.isEmpty()) {
            matchedTypeDefs = null;
        }

        return matchedTypeDefs;
    }


    // addAttributeTypeDef - not expected but support anyway
    public void addAttributeTypeDef(String userId,
                                    AttributeTypeDef newAttributeTypeDef)
            throws
            InvalidParameterException,
            RepositoryErrorException,
            TypeDefKnownException,
            TypeDefConflictException,
            InvalidTypeDefException,
            UserNotAuthorizedException
    {
        final String methodName = "addAttributeTypeDef";
        final String typeDefParameterName = "newAttributeTypeDef";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateAttributeTypeDef(repositoryName, typeDefParameterName, newAttributeTypeDef, methodName);
        repositoryValidator.validateUnknownAttributeTypeDef(repositoryName, typeDefParameterName, newAttributeTypeDef, methodName);

        /*
         * Perform operation
         */
        String typeDefName = newAttributeTypeDef.getName();
        AttributeTypeDef existingAttributeTypeDef = repositoryHelper.getAttributeTypeDefByName(repositoryName, typeDefName);
        // Expecting to find type in RCM...

        if (existingAttributeTypeDef != null) {
            OMRSErrorCode errorCode = OMRSErrorCode.TYPEDEF_ALREADY_DEFINED;
            String errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage(newAttributeTypeDef.getName(),
                    newAttributeTypeDef.getGUID(),
                    repositoryName);

            throw new TypeDefKnownException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        } else {
            // Cannot add a type def to this metadataCollection
            OMRSErrorCode errorCode = OMRSErrorCode.METHOD_NOT_IMPLEMENTED;
            String errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage(newAttributeTypeDef.getName(),
                    newAttributeTypeDef.getGUID(),
                    repositoryName);

            throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }

    }

    // updateTypeDef - not expected but support anyway
    public TypeDef updateTypeDef(String userId,
                                 TypeDefPatch typeDefPatch)
            throws
            InvalidParameterException,
            RepositoryErrorException,
            PatchErrorException,
            UserNotAuthorizedException
    {
        final String methodName = "updateTypeDef";
        final String typeDefParameterName = "typeDefPatch";

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
        // Cannot add a type def to this metadataCollection
        OMRSErrorCode errorCode = OMRSErrorCode.METHOD_NOT_IMPLEMENTED;
        String errorMessage = errorCode.getErrorMessageId()
                + errorCode.getFormattedErrorMessage(typeDefPatch.getTypeName(),
                typeDefPatch.getTypeDefGUID(),
                repositoryName);

        throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                this.getClass().getName(),
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction());

    }

    // deleteAttributeTypeDef - not expected but support anyway
    public void deleteAttributeTypeDef(String userId,
                                       String obsoleteTypeDefGUID,
                                       String obsoleteTypeDefName)
            throws
            InvalidParameterException,
            RepositoryErrorException,
            UserNotAuthorizedException
    {
        final String methodName = "deleteAttributeTypeDef";
        final String guidParameterName = "obsoleteTypeDefGUID";
        final String nameParameterName = "obsoleteTypeDefName";

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
        // Cannot delete a type def via this metadataCollection
        OMRSErrorCode errorCode = OMRSErrorCode.METHOD_NOT_IMPLEMENTED;
        String errorMessage = errorCode.getErrorMessageId()
                + errorCode.getFormattedErrorMessage(obsoleteTypeDefName,
                obsoleteTypeDefGUID,
                repositoryName);

        throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                this.getClass().getName(),
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction());
    }


    // reidentifyTypeDef - not expected but support anyway
    public TypeDef reIdentifyTypeDef(String userId,
                                     String originalTypeDefGUID,
                                     String originalTypeDefName,
                                     String newTypeDefGUID,
                                     String newTypeDefName)
            throws
            InvalidParameterException,
            RepositoryErrorException,
            UserNotAuthorizedException
    {
        final String methodName = "reIdentifyTypeDef";
        final String originalGUIDParameterName = "originalTypeDefGUID";
        final String originalNameParameterName = "originalTypeDefName";
        final String newGUIDParameterName = "newTypeDefGUID";
        final String newNameParameterName = "newTypeDefName";

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
        // Cannot reidentify a type def via this metadataCollection
        OMRSErrorCode errorCode = OMRSErrorCode.METHOD_NOT_IMPLEMENTED;
        String errorMessage = errorCode.getErrorMessageId()
                + errorCode.getFormattedErrorMessage(originalTypeDefName,
                originalTypeDefGUID,
                repositoryName);

        throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                this.getClass().getName(),
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction());

    }


    // reidentifyAttributeTypeDef - not expected but support anyway
    public AttributeTypeDef reIdentifyAttributeTypeDef(String userId,
                                                       String originalAttributeTypeDefGUID,
                                                       String originalAttributeTypeDefName,
                                                       String newAttributeTypeDefGUID,
                                                       String newAttributeTypeDefName)
            throws
            InvalidParameterException,
            RepositoryErrorException,
            UserNotAuthorizedException
    {
        final String methodName = "reIdentifyAttributeTypeDef";
        final String originalGUIDParameterName = "originalAttributeTypeDefGUID";
        final String originalNameParameterName = "originalAttributeTypeDefName";
        final String newGUIDParameterName = "newAttributeTypeDefGUID";
        final String newNameParameterName = "newAttributeTypeDefName";

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
        // Cannot reidentify an attribute type def via this metadataCollection
        OMRSErrorCode errorCode = OMRSErrorCode.METHOD_NOT_IMPLEMENTED;
        String errorMessage = errorCode.getErrorMessageId()
                + errorCode.getFormattedErrorMessage(originalAttributeTypeDefName,
                originalAttributeTypeDefGUID,
                repositoryName);

        throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                this.getClass().getName(),
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction());
    }





    public EntityDetail addEntity(String                userId,
                                  String                entityTypeGUID,
                                  InstanceProperties    initialProperties,
                                  List<Classification>  initialClassifications,
                                  InstanceStatus        initialStatus)
            throws
            InvalidParameterException,
            RepositoryErrorException,
            TypeErrorException,
            PropertyErrorException,
            ClassificationErrorException,
            StatusNotSupportedException,
            UserNotAuthorizedException
    {

        final String methodName = "addEntity";

        /*
         * Validate parameters
         */
        TypeDef typeDef = super.addEntityParameterValidation(userId,
                entityTypeGUID,
                initialProperties,
                initialClassifications,
                initialStatus);

        /*
         *  Ensure all classifications have valid InstanceType
         *
         */
        if (initialClassifications != null) {
            for (Classification classification : initialClassifications) {
                // Check the classification contains the type
                if (classification.getType() == null) {
                    // Retrieve the classification def and construct an InstanceType
                    ClassificationDef classificationDef;
                    try {
                        classificationDef = (ClassificationDef) repositoryHelper.getTypeDefByName(repositoryName, classification.getName());
                        classification.setType(repositoryHelper.getNewInstanceType(metadataCollectionId, classificationDef));
                    } catch (Exception e) {
                        log.error("{}: Could not find classification def with name {}", methodName, classification.getName(), e);

                        OMRSErrorCode errorCode = OMRSErrorCode.INVALID_TYPEDEF;
                        String errorMessage = errorCode.getErrorMessageId()
                                + errorCode.getFormattedErrorMessage(classification.getName(), "unknown", "initialClassifications", methodName, repositoryName, "unknown");

                        throw new TypeErrorException(errorCode.getHTTPErrorCode(),
                                this.getClass().getName(),
                                methodName,
                                errorMessage,
                                errorCode.getSystemAction(),
                                errorCode.getUserAction());
                    }
                }
            }
        }

        /*
         * Validation complete - ok to create new instance
         */
        EntityDetail newEntity = repositoryHelper.getNewEntity(repositoryName,
                null,
                InstanceProvenanceType.LOCAL_COHORT,
                userId,
                typeDef.getName(),
                initialProperties,
                initialClassifications);
        /*
         * If an initial status is supplied then override the default value.
         */
        if (initialStatus != null) {
            newEntity.setStatus(initialStatus);
        }

        newEntity = graphStore.createEntityInStore(newEntity);

        return newEntity;
    }



    // addEntityProxy
    public void addEntityProxy(String       userId,
                               EntityProxy  entityProxy)
            throws
            InvalidParameterException,
            RepositoryErrorException,
            UserNotAuthorizedException
    {
        /*
         * Validate parameters
         */
        super.addEntityProxyParameterValidation(userId, entityProxy);

        /*
         * Validation complete
         */
        EntityDetail  entity  = this.isEntityKnown(userId, entityProxy.getGUID());
        if (entity == null)
        {
            graphStore.createEntityProxyInStore(entityProxy);
        }
    }



    // isEntityKnown
    public EntityDetail isEntityKnown(String     userId,
                                      String     guid)
            throws
            InvalidParameterException,
            RepositoryErrorException,
            UserNotAuthorizedException
    {
        final String  methodName = "isEntityKnown";

        /*
         * Validate parameters
         */
        super.getInstanceParameterValidation(userId, guid, methodName);

        /*
         * Perform operation
         */

        EntityDetail entity;
        try {
            entity = graphStore.getEntityFromStore(guid);
            repositoryValidator.validateEntityFromStore(repositoryName, guid, entity, methodName);
        }
        catch (EntityNotKnownException e) {
            log.error("{} entity with GUID {} does not exist in repository {}", methodName, guid, repositoryName);
            entity = null;
        }

        return entity;
    }

    // isRelationshipKnown
    public Relationship  isRelationshipKnown(String     userId,
                                             String     guid)
            throws
            InvalidParameterException,
            RepositoryErrorException,
            UserNotAuthorizedException
    {
        final String  methodName = "isRelationshipKnown";

        /*
         * Validate parameters
         */
        super.getInstanceParameterValidation(userId, guid, methodName);

        /*
         * Process operation
         */
        return graphStore.getRelationshipFromStore(guid);
    }

    // getEntitySummary
    public EntitySummary getEntitySummary(String     userId,
                                          String     guid)
            throws
            InvalidParameterException,
            RepositoryErrorException,
            EntityNotKnownException,
            UserNotAuthorizedException
    {
        final String  methodName        = "getEntitySummary";

        /*
         * Validate parameters
         */
        super.getInstanceParameterValidation(userId, guid, methodName);

        /*
         * Perform operation
         */

        EntitySummary entity;
        try {
            entity = graphStore.getEntityFromStore(guid);

        }
        catch (EntityNotKnownException e) {
            log.info("{} entity not found wth GUID {} found - will check whether there is a proxy", methodName, guid);
            entity = graphStore.getEntityProxyFromStore(guid);

        }


        repositoryValidator.validateEntityFromStore(repositoryName, guid, entity, methodName);
        repositoryValidator.validateEntityIsNotDeleted(repositoryName, entity, methodName);

        return entity;
    }

    // getEntityDetail
    public EntityDetail getEntityDetail(String     userId,
                                        String     guid)
            throws
            InvalidParameterException,
            RepositoryErrorException,
            EntityNotKnownException,
            UserNotAuthorizedException
    {
        final String methodName = "getEntityDetail";
        final String guidParameterName = "guid";

        /*
         * Validate parameters
         */
        super.getInstanceParameterValidation(userId, guid, methodName);

        /*
         * Perform operation
         */

        EntityDetail entity = graphStore.getEntityFromStore(guid);

        repositoryValidator.validateEntityFromStore(repositoryName, guid, entity, methodName);
        repositoryValidator.validateEntityIsNotDeleted(repositoryName, entity, methodName);

        return entity;
    }


    // addRelationship
    public Relationship addRelationship(String               userId,
                                        String               relationshipTypeGUID,
                                        InstanceProperties   initialProperties,
                                        String               entityOneGUID,
                                        String               entityTwoGUID,
                                        InstanceStatus       initialStatus)
            throws
            InvalidParameterException,
            RepositoryErrorException,
            TypeErrorException,
            PropertyErrorException,
            EntityNotKnownException,
            StatusNotSupportedException,
            UserNotAuthorizedException
    {
        final String  methodName = "addRelationship";

        /*
         * Validate parameters
         */
        TypeDef typeDef = super.addRelationshipParameterValidation(userId,
                relationshipTypeGUID,
                initialProperties,
                entityOneGUID,
                entityTwoGUID,
                initialStatus);


        /*
         * Validation complete - ok to create new instance
         */
        Relationship relationship = repositoryHelper.getNewRelationship(repositoryName,
                null,
                InstanceProvenanceType.LOCAL_COHORT,
                userId,
                typeDef.getName(),
                initialProperties);

        /*
         * Retrieve a proxy for entity 1
         */
        EntityProxy entityOneProxy = graphStore.getEntityProxyFromStore(entityOneGUID);

        repositoryValidator.validateEntityFromStore(repositoryName, entityOneGUID, entityOneProxy, methodName);
        repositoryValidator.validateEntityIsNotDeleted(repositoryName, entityOneProxy, methodName);

        /*
         * Retrieve a proxy for entity 2
         */
        EntityProxy entityTwoProxy = graphStore.getEntityProxyFromStore(entityTwoGUID);

        repositoryValidator.validateEntityFromStore(repositoryName, entityTwoGUID, entityTwoProxy, methodName);
        repositoryValidator.validateEntityIsNotDeleted(repositoryName, entityTwoProxy, methodName);


        repositoryValidator.validateRelationshipEnds(repositoryName, entityOneProxy, entityTwoProxy, typeDef, methodName);

        relationship.setEntityOneProxy(entityOneProxy);
        relationship.setEntityTwoProxy(entityTwoProxy);

        /*
         * If an initial status is supplied then override the default value.
         */
        if (initialStatus != null)
        {
            relationship.setStatus(initialStatus);
        }

        graphStore.createRelationshipInStore(relationship);

        return relationship;
    }

    // getRelationship
    public Relationship getRelationship(String    userId,
                                        String    guid)
            throws
            InvalidParameterException,
            RepositoryErrorException,
            RelationshipNotKnownException,
            UserNotAuthorizedException
    {
        final String  methodName = "getRelationship";

        /*
         * Validate parameters
         */
        super.getInstanceParameterValidation(userId, guid, methodName);

        /*
         * Process operation
         */
        Relationship  relationship = graphStore.getRelationshipFromStore(guid);

        repositoryValidator.validateRelationshipFromStore(repositoryName, guid, relationship, methodName);
        repositoryValidator.validateRelationshipIsNotDeleted(repositoryName, relationship, methodName);

        return relationship;
    }


    // updateEntityStatus
    public EntityDetail updateEntityStatus(String           userId,
                                           String           entityGUID,
                                           InstanceStatus   newStatus)
            throws
            InvalidParameterException,
            RepositoryErrorException,
            EntityNotKnownException,
            StatusNotSupportedException,
            UserNotAuthorizedException
    {
        final String  methodName               = "updateEntityStatus";
        final String  statusParameterName      = "newStatus";

        /*
         * Validate parameters
         */
        this.updateInstanceStatusParameterValidation(userId, entityGUID, newStatus, methodName);

        /*
         * Locate entity - only interested in a non-proxy entity
         */
        EntityDetail entity;
        try {

            entity = graphStore.getEntityFromStore(entityGUID);

        }
        catch (EntityNotKnownException e) {
            log.error("{} entity wth GUID {} not found", methodName, entityGUID);
            OMRSErrorCode errorCode = OMRSErrorCode.ENTITY_PROXY_ONLY;

            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                    this.getClass().getName(),
                    repositoryName);

            throw new EntityNotKnownException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());

        }
        catch (RepositoryErrorException e) {
            log.error("{} repository exception during retrieval of entity wth GUID {}", methodName, entityGUID);
            throw e;
        }


        repositoryValidator.validateEntityFromStore(repositoryName, entityGUID, entity, methodName);
        repositoryValidator.validateEntityIsNotDeleted(repositoryName, entity, methodName);

        repositoryValidator.validateInstanceType(repositoryName, entity);

        String entityTypeGUID = entity.getType().getTypeDefGUID();

        TypeDef typeDef;
        try {
            typeDef = repositoryHelper.getTypeDef(repositoryName, "entityTypeGUID", entityTypeGUID, methodName);
        }
        catch (TypeErrorException e) {
            OMRSErrorCode errorCode = OMRSErrorCode.TYPEDEF_NOT_KNOWN;

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

        repositoryValidator.validateNewStatus(repositoryName, statusParameterName, newStatus, typeDef, methodName);

        /*
         * Validation complete - ok to make changes
         */
        EntityDetail   updatedEntity = new EntityDetail(entity);

        updatedEntity.setStatus(newStatus);

        updatedEntity = repositoryHelper.incrementVersion(userId, entity, updatedEntity);

        graphStore.updateEntityInStore(updatedEntity);


        return updatedEntity;
    }

    // updateEntityProperties
    public EntityDetail updateEntityProperties(String               userId,
                                               String               entityGUID,
                                               InstanceProperties   properties)
            throws
            InvalidParameterException,
            RepositoryErrorException,
            EntityNotKnownException,
            PropertyErrorException,
            UserNotAuthorizedException
    {
        final String  methodName = "updateEntityProperties";
        final String  propertiesParameterName  = "properties";

        /*
         * Validate parameters
         */
        this.updateInstancePropertiesPropertyValidation(userId, entityGUID, properties, methodName);

        /*
         * Locate entity - only interested in a non-proxy entity
         */
        EntityDetail entity;
        try {

            entity = graphStore.getEntityFromStore(entityGUID);

        }
        catch (EntityNotKnownException e) {
            log.error("{} entity wth GUID {} not found", methodName, entityGUID);
            OMRSErrorCode errorCode = OMRSErrorCode.ENTITY_PROXY_ONLY;

            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                    this.getClass().getName(),
                    repositoryName);

            throw new EntityNotKnownException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());

        }
        catch (RepositoryErrorException e) {
            log.error("{} repository exception during retrieval of entity wth GUID {}", methodName, entityGUID);
            throw e;
        }


        repositoryValidator.validateEntityFromStore(repositoryName, entityGUID, entity, methodName);
        repositoryValidator.validateEntityIsNotDeleted(repositoryName, entity, methodName);

        repositoryValidator.validateInstanceType(repositoryName, entity);

        String entityTypeGUID = entity.getType().getTypeDefGUID();

        TypeDef typeDef;
        try {
            typeDef = repositoryHelper.getTypeDef(repositoryName, "entityTypeGUID", entityTypeGUID, methodName);
        }
        catch (TypeErrorException e) {
            OMRSErrorCode errorCode = OMRSErrorCode.TYPEDEF_NOT_KNOWN;

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

        repositoryValidator.validateNewPropertiesForType(repositoryName,
                propertiesParameterName,
                typeDef,
                properties,
                methodName);

        /*
         * Validation complete - ok to make changes
         */
        EntityDetail   updatedEntity = new EntityDetail(entity);

        updatedEntity.setProperties(properties);

        updatedEntity = repositoryHelper.incrementVersion(userId, entity, updatedEntity);

        graphStore.updateEntityInStore(updatedEntity);

        ///*
        // * The repository store maintains an entity proxy for use with relationships.
        // */
        //EntityProxy entityProxy = repositoryHelper.getNewEntityProxy(repositoryName, updatedEntity);
        //
        //graphStore.updateEntityProxyInStore(entityProxy);

        return updatedEntity;
    }


    // updateRelationshipStatus
    public Relationship updateRelationshipStatus(String           userId,
                                                 String           relationshipGUID,
                                                 InstanceStatus   newStatus)
            throws
            InvalidParameterException,
            RepositoryErrorException,
            RelationshipNotKnownException,
            StatusNotSupportedException,
            UserNotAuthorizedException
    {
        final String  methodName          = "updateRelationshipStatus";
        final String  statusParameterName = "newStatus";

        /*
         * Validate parameters
         */
        this.updateInstanceStatusParameterValidation(userId, relationshipGUID, newStatus, methodName);

        /*
         * Locate relationship
         */
        Relationship  relationship = this.getRelationship(userId, relationshipGUID);

        repositoryValidator.validateInstanceType(repositoryName, relationship);

        String relationshipTypeGUID = relationship.getType().getTypeDefGUID();

        TypeDef typeDef;
        try {
            typeDef = repositoryHelper.getTypeDef(repositoryName, "relationshipTypeGUID", relationshipTypeGUID, methodName);
        }
        catch (TypeErrorException e) {
            OMRSErrorCode errorCode = OMRSErrorCode.TYPEDEF_NOT_KNOWN;

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


        repositoryValidator.validateNewStatus(repositoryName,
                statusParameterName,
                newStatus,
                typeDef,
                methodName);

        /*
         * Validation complete - ok to make changes
         */
        Relationship   updatedRelationship = new Relationship(relationship);

        updatedRelationship.setStatus(newStatus);

        updatedRelationship = repositoryHelper.incrementVersion(userId, relationship, updatedRelationship);

        graphStore.updateRelationshipInStore(updatedRelationship);

        return updatedRelationship;
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
    public Relationship updateRelationshipProperties(String               userId,
                                                     String               relationshipGUID,
                                                     InstanceProperties   properties)
            throws
            InvalidParameterException,
            RepositoryErrorException,
            RelationshipNotKnownException,
            PropertyErrorException,
            UserNotAuthorizedException
    {
        final String  methodName = "updateRelationshipProperties";
        final String  propertiesParameterName = "properties";

        /*
         * Validate parameters
         */
        this.updateInstancePropertiesPropertyValidation(userId, relationshipGUID, properties, methodName);

        /*
         * Locate relationship
         */
        Relationship  relationship = this.getRelationship(userId, relationshipGUID);

        repositoryValidator.validateInstanceType(repositoryName, relationship);

        String relationshipTypeGUID = relationship.getType().getTypeDefGUID();

        TypeDef typeDef;
        try {
            typeDef = repositoryHelper.getTypeDef(repositoryName, "relationshipTypeGUID", relationshipTypeGUID, methodName);
        }
        catch (TypeErrorException e) {
            OMRSErrorCode errorCode = OMRSErrorCode.TYPEDEF_NOT_KNOWN;

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

        repositoryValidator.validateNewPropertiesForType(repositoryName,
                propertiesParameterName,
                typeDef,
                properties,
                methodName);


        /*
         * Validation complete - ok to make changes
         */
        Relationship   updatedRelationship = new Relationship(relationship);

        updatedRelationship.setProperties(properties);
        updatedRelationship = repositoryHelper.incrementVersion(userId, relationship, updatedRelationship);

        graphStore.updateRelationshipInStore(updatedRelationship);

        return updatedRelationship;
    }


    // purgeEntity
    public void purgeEntity(String    userId,
                            String    typeDefGUID,
                            String    typeDefName,
                            String    deletedEntityGUID)
            throws
            InvalidParameterException,
            RepositoryErrorException,
            EntityNotKnownException,
            EntityNotDeletedException,
            UserNotAuthorizedException
    {
        final String  methodName  = "purgeEntity";

        /*
         * Validate parameters
         */
        this.removeInstanceParameterValidation(userId, typeDefGUID, typeDefName, deletedEntityGUID, methodName);

        /*
         * Locate entity - only interested in a non-proxy entity
         */
        EntityDetail entity;
        try {

            entity = graphStore.getEntityFromStore(deletedEntityGUID);

        }
        catch (EntityNotKnownException e) {
            log.error("{} entity wth GUID {} not found", methodName, deletedEntityGUID);
            OMRSErrorCode errorCode = OMRSErrorCode.ENTITY_PROXY_ONLY;

            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                    this.getClass().getName(),
                    repositoryName);

            throw new EntityNotKnownException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());

        }
        catch (RepositoryErrorException e) {
            log.error("{} repository exception during retrieval of entity wth GUID {}", methodName, deletedEntityGUID);
            throw e;
        }

        repositoryValidator.validateEntityFromStore(repositoryName, deletedEntityGUID, entity, methodName);

        repositoryValidator.validateTypeForInstanceDelete(repositoryName,
                typeDefGUID,
                typeDefName,
                entity,
                methodName);

        repositoryValidator.validateEntityIsDeleted(repositoryName, entity, methodName);


        /*
         * Locate/purge relationships for entity
         */
        try
        {
            List<Relationship> relationships = this.getRelationshipsForEntity(userId,
                    deletedEntityGUID,
                    null,
                    0,
                    null,
                    null,
                    null,
                    null,
                    10000);


            if (relationships != null)
            {
                for (Relationship relationship : relationships)
                {
                    if (relationship != null)
                    {
                        graphStore.removeRelationshipFromStore(relationship.getGUID());
                    }
                }
            }
        }
        catch (Throwable  error)
        {
            // nothing to do - keep going
        }

        /*
         * Validation is complete - ok to remove the entity
         */
        graphStore.removeEntityFromStore(entity.getGUID());

    }

    // purgeRelationship
    public void purgeRelationship(String    userId,
                                  String    typeDefGUID,
                                  String    typeDefName,
                                  String    deletedRelationshipGUID)
            throws
            InvalidParameterException,
            RepositoryErrorException,
            RelationshipNotKnownException,
            RelationshipNotDeletedException,
            UserNotAuthorizedException
    {
        final String  methodName = "purgeRelationship";

        /*
         * Validate parameters
         */
        this.removeInstanceParameterValidation(userId, typeDefGUID, typeDefName, deletedRelationshipGUID, methodName);

        /*
         * Locate relationship
         */
        Relationship  relationship  = graphStore.getRelationshipFromStore(deletedRelationshipGUID);

        repositoryValidator.validateRelationshipFromStore(repositoryName, deletedRelationshipGUID, relationship, methodName);
        repositoryValidator.validateTypeForInstanceDelete(repositoryName,
                typeDefGUID,
                typeDefName,
                relationship,
                methodName);

        repositoryValidator.validateRelationshipIsDeleted(repositoryName, relationship, methodName);


        /*
         * Validation is complete - ok to remove the relationship
         */
        graphStore.removeRelationshipFromStore(relationship.getGUID());
    }


    // getRelationshipsForEntity
    public List<Relationship> getRelationshipsForEntity(String                     userId,
                                                        String                     entityGUID,
                                                        String                     relationshipTypeGUID,
                                                        int                        fromRelationshipElement,
                                                        List<InstanceStatus>       limitResultsByStatus,
                                                        Date                       asOfTime,
                                                        String                     sequencingProperty,
                                                        SequencingOrder            sequencingOrder,
                                                        int                        pageSize)
            throws
            InvalidParameterException,
            TypeErrorException,
            RepositoryErrorException,
            EntityNotKnownException,
            PropertyErrorException,
            PagingErrorException,
            UserNotAuthorizedException,
            FunctionNotSupportedException
    {
        final String  methodName = "getRelationshipsForEntity";

        /*
         * Validate parameters
         */
        super.getRelationshipsForEntityParameterValidation(userId,
                entityGUID,
                relationshipTypeGUID,
                fromRelationshipElement,
                limitResultsByStatus,
                asOfTime,
                sequencingProperty,
                sequencingOrder,
                pageSize);

        /*
         * Perform operation
         */
        EntitySummary  entity = this.getEntitySummary(userId, entityGUID);

        repositoryValidator.validateEntityFromStore(repositoryName, entityGUID, entity, methodName);
        repositoryValidator.validateEntityIsNotDeleted(repositoryName, entity, methodName);

        List<Relationship> entityRelationships = new ArrayList<>();

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
        }

        List<Relationship> filteredRelationships = new ArrayList<>();
        List<Relationship> relationships = graphStore.getRelationshipsForEntity(entityGUID);

        for (Relationship  relationship : relationships) {

            if (relationship != null) {

                if (relationship.getStatus() != InstanceStatus.DELETED) {
                    // exclude DELETED relationships

                    // filter results according to status filter parameter
                    if (  limitResultsByStatus == null
                          || (   limitResultsByStatus != null
                              && !limitResultsByStatus.isEmpty()
                              && limitResultsByStatus.contains(relationship.getStatus()))) {

                        // filter by typeGUID if necessary
                        if (relationshipTypeGUID == null  || relationshipTypeGUID.equals(relationship.getType().getTypeDefGUID())) {
                            filteredRelationships.add(relationship);
                        }
                    }
                }
            }
        }

        if (filteredRelationships.isEmpty())
        {
            return null;
        }

        return repositoryHelper.formatRelationshipResults(filteredRelationships,
                fromRelationshipElement,
                sequencingProperty,
                sequencingOrder,
                pageSize);
    }


    // findEntitiesByProperty
    public List<EntityDetail> findEntitiesByProperty(String                 userId,
                                                     String                 entityTypeGUID,
                                                     InstanceProperties     matchProperties,
                                                     MatchCriteria          matchCriteria,
                                                     int                    fromEntityElement,
                                                     List<InstanceStatus>   limitResultsByStatus,
                                                     List<String>           limitResultsByClassification,
                                                     Date                   asOfTime,
                                                     String                 sequencingProperty,
                                                     SequencingOrder        sequencingOrder,
                                                     int                    pageSize)
            throws
            InvalidParameterException,
            RepositoryErrorException,
            TypeErrorException,
            PropertyErrorException,
            PagingErrorException,
            FunctionNotSupportedException,
            UserNotAuthorizedException
    {

        final String methodName = "findEntitiesByProperty";
        final String entityTypeGUIDParameterName = "entityTypeGUID";

        /*
         * Validate parameters
         */
        super.findEntitiesByPropertyParameterValidation(userId,
                entityTypeGUID,
                matchProperties,
                matchCriteria,
                fromEntityElement,
                limitResultsByStatus,
                limitResultsByClassification,
                asOfTime,
                sequencingProperty,
                sequencingOrder,
                pageSize);


        if (asOfTime != null) {
            log.error("{} does not support asOfTime searches", methodName);

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


        /*
         * Perform operation
         */

        ArrayList<EntityDetail> returnEntities = null;


        String specifiedTypeName = null;
        if (entityTypeGUID != null) {
            TypeDef typeDef = repositoryHelper.getTypeDef(repositoryName, entityTypeGUIDParameterName, entityTypeGUID, methodName);
            specifiedTypeName = typeDef.getName();
        }

        TypeDefGallery activeTypes = repositoryHelper.getActiveTypeDefGallery();
        List<TypeDef> allTypeDefs = activeTypes.getTypeDefs();

        for (TypeDef typeDef : allTypeDefs) {
            if (typeDef.getCategory() == TypeDefCategory.ENTITY_DEF) {

                log.debug("{}: checking entity type {}", methodName, typeDef.getName());

                String actualTypeName = typeDef.getName();

                // If entityTypeGUID parameter is not null there is an expected type, so check whether the
                // current type matches the expected type or is one of its sub-types.

                if (specifiedTypeName != null) {

                    boolean typeMatch = repositoryHelper.isTypeOf(metadataCollectionId, actualTypeName, specifiedTypeName);
                    if (!typeMatch) {
                        log.debug("{}: not searching entity type {} because not a subtype of {}", methodName, actualTypeName, specifiedTypeName);
                        continue;
                    }
                    log.debug("{}: continuing with search for entity type {} because it is a subtype of {}", methodName, actualTypeName, specifiedTypeName);


                }
                // Extract the type guid and invoke a type specific search...

                List<EntityDetail> entitiesForCurrentType = graphStore.findEntitiesByProperty(actualTypeName, matchProperties, matchCriteria);


                if (entitiesForCurrentType != null && !entitiesForCurrentType.isEmpty()) {
                    if (returnEntities == null) {
                        returnEntities = new ArrayList<>();
                    }
                    log.info("{}: for type {} found {} entities", methodName, typeDef.getName(), entitiesForCurrentType.size());
                    returnEntities.addAll(entitiesForCurrentType);
                } else {
                    log.info("{}: for type {} found no entities", methodName, typeDef.getName());
                }

            }
        }

        // Eliminate soft deleted entities and apply status and classification filtering if any was requested
        if (returnEntities == null) {
            return null;
        }
        else {
            List<EntityDetail> retainedEntities = new ArrayList<>();
            for (EntityDetail entity : returnEntities) {
                if (entity != null) {
                    if ((entity.getStatus() != InstanceStatus.DELETED)
                            && (repositoryValidator.verifyInstanceHasRightStatus(limitResultsByStatus, entity))
                            && (repositoryValidator.verifyEntityIsClassified(limitResultsByClassification, entity))) {

                        retainedEntities.add(entity);
                    }
                }
            }

            return repositoryHelper.formatEntityResults(retainedEntities, fromEntityElement, sequencingProperty, sequencingOrder, pageSize);
        }
    }


    // findRelationshipsByProperty
    public  List<Relationship> findRelationshipsByProperty(String                    userId,
                                                           String                    relationshipTypeGUID,
                                                           InstanceProperties        matchProperties,
                                                           MatchCriteria             matchCriteria,
                                                           int                       fromRelationshipElement,
                                                           List<InstanceStatus>      limitResultsByStatus,
                                                           Date                      asOfTime,
                                                           String                    sequencingProperty,
                                                           SequencingOrder           sequencingOrder,
                                                           int                       pageSize)
            throws
            InvalidParameterException,
            TypeErrorException,
            RepositoryErrorException,
            PropertyErrorException,
            PagingErrorException,
            FunctionNotSupportedException,
            UserNotAuthorizedException
    {
        final String  methodName = "findRelationshipsByProperty";
        final String  guidParameterName = "relationshipTypeGUID";

        /*
         * Validate parameters
         */
        super.findRelationshipsByPropertyParameterValidation(userId,
                relationshipTypeGUID,
                matchProperties,
                matchCriteria,
                fromRelationshipElement,
                limitResultsByStatus,
                asOfTime,
                sequencingProperty,
                sequencingOrder,
                pageSize);

        this.validateTypeGUID(repositoryName, guidParameterName, relationshipTypeGUID, methodName);


        if (asOfTime != null) {
            log.error("{} does not support asOfTime searches", methodName);

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

        /*
         * Perform operation
         */

        // There are no supertype/subtype hierarchies in relationship types, so only search the specified type or all types.

        List<Relationship> returnRelationships = null;

        String specifiedTypeName = null;

        if (relationshipTypeGUID != null) {
            // search the specified type (only)
            TypeDef typeDef = repositoryHelper.getTypeDef(repositoryName, guidParameterName, relationshipTypeGUID, methodName);
            specifiedTypeName = typeDef.getName();

            log.info("{}: search relationship type {}", methodName, specifiedTypeName);

            returnRelationships = graphStore.findRelationshipsByProperty(specifiedTypeName, matchProperties, matchCriteria);

        }
        else {
            // search all types

            TypeDefGallery activeTypes = repositoryHelper.getActiveTypeDefGallery();
            List<TypeDef> allTypeDefs = activeTypes.getTypeDefs();

            for (TypeDef typeDef : allTypeDefs) {

                if (typeDef.getCategory() == TypeDefCategory.RELATIONSHIP_DEF) {

                    log.info("{}: search relationship type {}", methodName, typeDef.getName());

                    String actualTypeName = typeDef.getName();

                    // For this type, invoke a type specific search...

                    List<Relationship> relationshipsForCurrentType = graphStore.findRelationshipsByProperty(actualTypeName, matchProperties, matchCriteria);

                    if (relationshipsForCurrentType != null && !relationshipsForCurrentType.isEmpty()) {
                        if (returnRelationships == null) {
                            returnRelationships = new ArrayList<>();
                        }
                        log.info("{}: for type {} found {} relationships", methodName, typeDef.getName(), relationshipsForCurrentType.size());
                        returnRelationships.addAll(relationshipsForCurrentType);
                    } else {
                        log.info("{}: for type {} found no relationships", methodName, typeDef.getName());
                    }
                }
            }
        }


        // Eliminate soft deleted relationships and apply status filtering if any was requested
        if (returnRelationships == null) {
            return null;
        }
        else {
            List<Relationship> retainedRelationships = new ArrayList<>();
            for (Relationship relationship : returnRelationships) {
                if (relationship != null) {
                    if ((relationship.getStatus() != InstanceStatus.DELETED)
                            && (repositoryValidator.verifyInstanceHasRightStatus(limitResultsByStatus, relationship))) {

                        retainedRelationships.add(relationship);
                    }
                }
            }

            return repositoryHelper.formatRelationshipResults(retainedRelationships, fromRelationshipElement, sequencingProperty, sequencingOrder, pageSize);
        }
    }





    public  void validateTypeGUID(String sourceName,
                                  String guidParameterName,
                                  String guid,
                                  String methodName)
            throws
            TypeErrorException
    {
        if  (guid != null)
        {
            TypeDef typeDef = repositoryHelper.getTypeDef(repositoryName, guidParameterName, guid, methodName);
            if (typeDef == null)
            {
                OMRSErrorCode errorCode    = OMRSErrorCode.TYPEDEF_ID_NOT_KNOWN;
                String        errorMessage = errorCode.getErrorMessageId()
                        + errorCode.getFormattedErrorMessage(guid, guidParameterName, methodName, sourceName);

                throw new TypeErrorException(errorCode.getHTTPErrorCode(),
                        this.getClass().getName(),
                        methodName,
                        errorMessage,
                        errorCode.getSystemAction(),
                        errorCode.getUserAction());
            }
        }
    }


    // findEntitiesByPropertyValue
    public  List<EntityDetail> findEntitiesByPropertyValue(String                userId,
                                                           String                entityTypeGUID,
                                                           String                searchCriteria,
                                                           int                   fromEntityElement,
                                                           List<InstanceStatus>  limitResultsByStatus,
                                                           List<String>          limitResultsByClassification,
                                                           Date                  asOfTime,
                                                           String                sequencingProperty,
                                                           SequencingOrder       sequencingOrder,
                                                           int                   pageSize)
            throws
            InvalidParameterException,
            TypeErrorException,
            RepositoryErrorException,
            PropertyErrorException,
            PagingErrorException,
            FunctionNotSupportedException,
            UserNotAuthorizedException
    {
        final String  methodName = "findEntitiesByPropertyValue";
        final String  entityTypeGUIDParameterName = "entityTypeGUID";

        /*
         * Validate parameters
         */
        super.findEntitiesByPropertyValueParameterValidation(userId,
                entityTypeGUID,
                searchCriteria,
                fromEntityElement,
                limitResultsByStatus,
                limitResultsByClassification,
                asOfTime,
                sequencingProperty,
                sequencingOrder,
                pageSize);


        if (asOfTime != null) {
            log.error("{} does not support asOfTime searches", methodName);

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


        /*
         * Perform operation
         */

        List<EntityDetail> returnEntities = null;

        // Include subtypes

        String specifiedTypeName = null;
        if (entityTypeGUID != null) {
            TypeDef typeDef = repositoryHelper.getTypeDef(repositoryName, entityTypeGUIDParameterName, entityTypeGUID, methodName);
            specifiedTypeName = typeDef.getName();
        }

        TypeDefGallery activeTypes = repositoryHelper.getActiveTypeDefGallery();
        List<TypeDef> allTypeDefs = activeTypes.getTypeDefs();

        for (TypeDef typeDef : allTypeDefs) {
            if (typeDef.getCategory() == TypeDefCategory.ENTITY_DEF) {

                log.info("{}: checking entity type {}", methodName, typeDef.getName());

                String actualTypeName = typeDef.getName();

                // If entityTypeGUID parameter is not null there is an expected type, so check whether the
                // current type matches the expected type or is one of its sub-types.

                if (specifiedTypeName != null) {

                    boolean typeMatch = repositoryHelper.isTypeOf(metadataCollectionId, actualTypeName, specifiedTypeName);
                    if (!typeMatch) {
                        log.info("{}: not searching entity type {} because not a subtype of {}", methodName, actualTypeName, specifiedTypeName);
                        continue;
                    }
                    log.info("{}: continuing with search for entity type {} because it is a subtype of {}", methodName, actualTypeName, specifiedTypeName);

                }

                InstanceProperties matchProperties = graphStore.constructMatchPropertiesForSearchCriteria(typeDef, searchCriteria, GraphOMRSConstants.ElementType.Vertex);


                List<EntityDetail> entitiesForCurrentType = graphStore.findEntitiesByProperty(actualTypeName, matchProperties, MatchCriteria.ANY);


                if (entitiesForCurrentType != null && !entitiesForCurrentType.isEmpty()) {
                    if (returnEntities == null) {
                        returnEntities = new ArrayList<>();
                    }
                    log.info("{}: for type {} found {} entities", methodName, typeDef.getName(), entitiesForCurrentType.size());
                    returnEntities.addAll(entitiesForCurrentType);
                } else {
                    log.info("{}: for type {} found no entities", methodName, typeDef.getName());
                }

            }
        }

        // Eliminate soft deleted entities and apply status and classification filtering if any was requested
        if (returnEntities ==  null) {
            return null;
        }
        else {
            List<EntityDetail> retainedEntities = new ArrayList<>();
            for (EntityDetail entity : returnEntities) {
                if (entity != null) {
                    if ((entity.getStatus() != InstanceStatus.DELETED)
                            && (repositoryValidator.verifyInstanceHasRightStatus(limitResultsByStatus, entity))
                            && (repositoryValidator.verifyEntityIsClassified(limitResultsByClassification, entity))) {

                        retainedEntities.add(entity);
                    }
                }
            }

            return repositoryHelper.formatEntityResults(retainedEntities, fromEntityElement, sequencingProperty, sequencingOrder, pageSize);
        }
    }



    // findRelationshipsByPropertyValue
    public  List<Relationship> findRelationshipsByPropertyValue(String                    userId,
                                                                String                    relationshipTypeGUID,
                                                                String                    searchCriteria,
                                                                int                       fromRelationshipElement,
                                                                List<InstanceStatus>      limitResultsByStatus,
                                                                Date                      asOfTime,
                                                                String                    sequencingProperty,
                                                                SequencingOrder           sequencingOrder,
                                                                int                       pageSize)
            throws
            InvalidParameterException,
            TypeErrorException,
            RepositoryErrorException,
            PropertyErrorException,
            PagingErrorException,
            FunctionNotSupportedException,
            UserNotAuthorizedException
    {
        final String methodName = "findRelationshipsByPropertyValue";
        final String relationshipTypeGUIDParameterName = "relationshipTypeGUID";


        /*
         * Validate parameters
         */
        super.findRelationshipsByPropertyValueParameterValidation(userId,
                relationshipTypeGUID,
                searchCriteria,
                fromRelationshipElement,
                limitResultsByStatus,
                asOfTime,
                sequencingProperty,
                sequencingOrder,
                pageSize);


        if (asOfTime != null) {
            log.error("{} does not support asOfTime searches", methodName);

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

        /*
         * Perform operation
         */
        List<Relationship> foundRelationships = new ArrayList<>();

        // There are no supertype/subtype hierarchies in relationship types, so only search the specified type or all types.

        List<Relationship> returnRelationships = null;

        String specifiedTypeName = null;

        List<TypeDef> typesToSearch = new ArrayList<>();


        if (relationshipTypeGUID != null) {

            // search the specified type (only)
            TypeDef typeDef = repositoryHelper.getTypeDef(repositoryName, relationshipTypeGUIDParameterName, relationshipTypeGUID, methodName);
            specifiedTypeName = typeDef.getName();

            log.info("{}: search relationship type {}", methodName, specifiedTypeName);
            typesToSearch.add(typeDef);
        }
        else {

            // search all types

            TypeDefGallery activeTypes = repositoryHelper.getActiveTypeDefGallery();
            List<TypeDef> allTypeDefs = activeTypes.getTypeDefs();

            for (TypeDef typeDef : allTypeDefs) {

                if (typeDef.getCategory() == TypeDefCategory.RELATIONSHIP_DEF) {

                    log.info("{}: search relationship type {}", methodName, typeDef.getName());
                    typesToSearch.add(typeDef);
                }
            }
        }


        for (TypeDef typeDef : typesToSearch) {

            String currentTypeName = typeDef.getName();

            InstanceProperties matchProperties = graphStore.constructMatchPropertiesForSearchCriteria(typeDef, searchCriteria, GraphOMRSConstants.ElementType.Edge);

            List<Relationship> relationshipsForCurrentType = graphStore.findRelationshipsByProperty(currentTypeName, matchProperties, MatchCriteria.ANY);

            if (relationshipsForCurrentType != null && !relationshipsForCurrentType.isEmpty()) {
                if (returnRelationships == null) {
                    returnRelationships = new ArrayList<>();
                }
                log.info("{}: for type {} found {} relationships", methodName, typeDef.getName(), relationshipsForCurrentType.size());
                returnRelationships.addAll(relationshipsForCurrentType);

            }
            else {
                log.info("{}: for type {} found no relationships", methodName, typeDef.getName());
            }

        }


        // Eliminate soft deleted relationships and apply status filtering if any was requested
        if (returnRelationships == null) {
            return null;
        }
        else {
            List<Relationship> retainedRelationships = new ArrayList<>();
            for (Relationship relationship : returnRelationships) {
                if (relationship != null) {
                    if ((relationship.getStatus() != InstanceStatus.DELETED)
                            && (repositoryValidator.verifyInstanceHasRightStatus(limitResultsByStatus, relationship))) {

                        retainedRelationships.add(relationship);
                    }
                }
            }

            return repositoryHelper.formatRelationshipResults(retainedRelationships, fromRelationshipElement, sequencingProperty, sequencingOrder, pageSize);
        }
    }



    // classifyEntity
    public EntityDetail classifyEntity(String               userId,
                                       String               entityGUID,
                                       String               classificationName,
                                       InstanceProperties   classificationProperties)
            throws
            InvalidParameterException,
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
         * Locate entity - only interested in a non-proxy entity
         */
        EntityDetail entity;
        try {

            entity = graphStore.getEntityFromStore(entityGUID);

        }
        catch (EntityNotKnownException e) {
            log.error("{} entity wth GUID {} not found", methodName, entityGUID);
            OMRSErrorCode errorCode = OMRSErrorCode.ENTITY_NOT_KNOWN;

            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(entityGUID,
                    methodName,
                    repositoryName);

            throw new EntityNotKnownException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());

        }
        catch (RepositoryErrorException e) {
            log.error("{} repository exception during retrieval of entity wth GUID {}", methodName, entityGUID);
            throw e;
        }

        repositoryValidator.validateEntityFromStore(repositoryName, entityGUID, entity, methodName);
        repositoryValidator.validateEntityIsNotDeleted(repositoryName, entity, methodName);

        repositoryValidator.validateInstanceType(repositoryName, entity);

        InstanceType entityType = entity.getType();

        repositoryValidator.validateClassification(repositoryName, classificationParameterName, classificationName, entityType.getTypeDefName(), methodName);

        Classification newClassification;
        try
        {
            repositoryValidator.validateClassificationProperties(repositoryName,
                    classificationName,
                    propertiesParameterName,
                    classificationProperties,
                    methodName);

            /*
             * Validation complete - build the new classification
             */
            newClassification = repositoryHelper.getNewClassification(repositoryName,
                    userId,
                    classificationName,
                    entityType.getTypeDefName(),
                    ClassificationOrigin.ASSIGNED,
                    null,
                    classificationProperties);
        }
        catch (PropertyErrorException  error)
        {
            throw error;
        }
        catch (Throwable   error)
        {
            OMRSErrorCode errorCode = OMRSErrorCode.INVALID_CLASSIFICATION_FOR_ENTITY;

            throw new ClassificationErrorException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    error.getMessage(),
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }

        /*
         * Validation complete - ok to update entity
         */

        EntityDetail updatedEntity = repositoryHelper.addClassificationToEntity(repositoryName, entity, newClassification, methodName);

        updatedEntity = repositoryHelper.incrementVersion(userId, entity, updatedEntity);

        graphStore.updateEntityInStore(updatedEntity);

        return updatedEntity;
    }


    // declassifyEntity
    public EntityDetail declassifyEntity(String  userId,
                                         String  entityGUID,
                                         String  classificationName)
            throws
            InvalidParameterException,
            RepositoryErrorException,
            EntityNotKnownException,
            ClassificationErrorException,
            UserNotAuthorizedException
    {
        final String methodName = "declassifyEntity";

        /*
         * Validate parameters
         */
        super.declassifyEntityParameterValidation(userId, entityGUID, classificationName);

        /*
         * Locate entity - only interested in a non-proxy entity
         */
        EntityDetail entity;
        try {

            entity = graphStore.getEntityFromStore(entityGUID);

        }
        catch (EntityNotKnownException e) {
            log.error("{} entity wth GUID {} not found", methodName, entityGUID);
            OMRSErrorCode errorCode = OMRSErrorCode.ENTITY_PROXY_ONLY;

            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                    this.getClass().getName(),
                    repositoryName);

            throw new EntityNotKnownException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());

        }
        catch (RepositoryErrorException e) {
            log.error("{} repository exception during retrieval of entity wth GUID {}", methodName, entityGUID);
            throw e;
        }



        repositoryValidator.validateEntityFromStore(repositoryName, entityGUID, entity, methodName);
        repositoryValidator.validateEntityIsNotDeleted(repositoryName, entity, methodName);

        EntityDetail updatedEntity = repositoryHelper.deleteClassificationFromEntity(repositoryName, entity, classificationName, methodName);

        updatedEntity = repositoryHelper.incrementVersion(userId, entity, updatedEntity);

        graphStore.updateEntityInStore(updatedEntity);

        return updatedEntity;
    }


    // findEntitiesByClassification
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
                                                            int                       pageSize)
            throws
            InvalidParameterException,
            TypeErrorException,
            RepositoryErrorException,
            ClassificationErrorException,
            FunctionNotSupportedException,
            PropertyErrorException,
            PagingErrorException,
            UserNotAuthorizedException
    {

        final String methodName = "findEntitiesByClassification";
        final String entityTypeGUIDParameterName = "entityTypeGUID";

        /*
         * Validate parameters
         */
        super.findEntitiesByClassificationParameterValidation(userId,
                entityTypeGUID,
                classificationName,
                matchClassificationProperties,
                matchCriteria,
                fromEntityElement,
                limitResultsByStatus,
                asOfTime,
                sequencingProperty,
                sequencingOrder,
                pageSize);


        if (asOfTime != null) {
            // Not supported
            log.error("{} does not support asOfTime parameter", methodName);

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


        /*
         * Perform operation
         */


        ArrayList<EntityDetail> returnEntities = null;


        String specifiedTypeName = null;
        if (entityTypeGUID != null) {
            TypeDef typeDef = repositoryHelper.getTypeDef(repositoryName, entityTypeGUIDParameterName, entityTypeGUID, methodName);
            specifiedTypeName = typeDef.getName();
        }

        TypeDefGallery activeTypes = repositoryHelper.getActiveTypeDefGallery();
        List<TypeDef> allTypeDefs = activeTypes.getTypeDefs();

        for (TypeDef typeDef : allTypeDefs) {
            if (typeDef.getCategory() == TypeDefCategory.ENTITY_DEF) {

                log.debug("{}: checking entity type {}", methodName, typeDef.getName());

                String actualTypeName = typeDef.getName();

                // If entityTypeGUID parameter is not null there is an expected type, so check whether the
                // current type matches the expected type or is one of its sub-types.

                if (specifiedTypeName != null) {

                    boolean typeMatch = repositoryHelper.isTypeOf(metadataCollectionId, actualTypeName, specifiedTypeName);
                    if (!typeMatch) {
                        log.debug("{}: not searching entity type {} because not a subtype of {}", methodName, actualTypeName, specifiedTypeName);
                        continue;
                    }
                    log.debug("{}: continuing with search for entity type {} because it is a subtype of {}", methodName, actualTypeName, specifiedTypeName);


                }

                // Find all entities of this type that have the matching classification.
                //
                List<EntityDetail> entitiesForCurrentType = graphStore.findEntitiesByClassification(classificationName, matchClassificationProperties, matchCriteria, actualTypeName);


                if (entitiesForCurrentType != null && !entitiesForCurrentType.isEmpty()) {
                    if (returnEntities == null) {
                        returnEntities = new ArrayList<>();
                    }
                    log.info("{}: for type {} found {} entities", methodName, typeDef.getName(), entitiesForCurrentType.size());
                    returnEntities.addAll(entitiesForCurrentType);
                } else {
                    log.info("{}: for type {} found no entities", methodName, typeDef.getName());
                }

            }
        }


        // Eliminate soft deleted entities and apply status filtering if any was requested
        if (returnEntities == null) {
            return null;
        } else {
            List<EntityDetail> retainedEntities = new ArrayList<>();
            for (EntityDetail entity : returnEntities) {
                if (entity != null) {
                    if (   (entity.getStatus() != InstanceStatus.DELETED)
                        && (repositoryValidator.verifyInstanceHasRightStatus(limitResultsByStatus, entity))) {

                        retainedEntities.add(entity);
                    }
                }
            }

            return repositoryHelper.formatEntityResults(retainedEntities, fromEntityElement, sequencingProperty, sequencingOrder, pageSize);
        }
    }



    // deleteEntity
     public EntityDetail deleteEntity(String    userId,
                                      String    typeDefGUID,
                                      String    typeDefName,
                                      String    obsoleteEntityGUID)
            throws
            InvalidParameterException,
            RepositoryErrorException,
            EntityNotKnownException,
            UserNotAuthorizedException
    {
        final String methodName = "deleteEntity";

        /*
         * Validate parameters
         */
        super.removeInstanceParameterValidation(userId, typeDefGUID, typeDefName, obsoleteEntityGUID, methodName);

        /*
         * Locate Entity. Doesn't matter if it is a proxy or not, so try both
         */
        EntityDetail entityDetail = null;
        try {

            entityDetail = graphStore.getEntityFromStore(obsoleteEntityGUID);
            repositoryValidator.validateEntityFromStore(repositoryName, obsoleteEntityGUID, entityDetail, methodName);

        } catch (EntityNotKnownException e) {

            log.error("{} entity wth GUID {} not found or only a proxy", methodName, obsoleteEntityGUID);
            OMRSErrorCode errorCode = OMRSErrorCode.ENTITY_NOT_KNOWN;

            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                    this.getClass().getName(),
                    repositoryName);

            throw new EntityNotKnownException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }

        repositoryValidator.validateTypeForInstanceDelete(repositoryName, typeDefGUID, typeDefName, entityDetail, methodName);

        repositoryValidator.validateInstanceStatusForDelete(repositoryName, entityDetail, methodName);

        /*
         * Locate/delete relationships for entity
         */
        try {
            List<Relationship> relationships = this.getRelationshipsForEntity(userId,
                    obsoleteEntityGUID,
                    null,
                    0,
                    null,
                    null,
                    null,
                    null,
                    10000);


            if (relationships != null) {
                for (Relationship relationship : relationships) {
                    if (relationship != null) {
                        InstanceType type = relationship.getType();
                        if (type != null) {
                            this.deleteRelationship(userId,
                                    type.getTypeDefGUID(),
                                    type.getTypeDefName(),
                                    relationship.getGUID());
                        }
                    }
                }
            }
        } catch (Throwable error) {
            // nothing to do - keep going
        }


        /*
         * A delete is a soft-delete that updates the status to DELETED.
         */

        EntityDetail updatedEntity = new EntityDetail(entityDetail);

        updatedEntity.setStatusOnDelete(entityDetail.getStatus());
        updatedEntity.setStatus(InstanceStatus.DELETED);

        updatedEntity = repositoryHelper.incrementVersion(userId, entityDetail, updatedEntity);

        graphStore.updateEntityInStore(updatedEntity);

        return updatedEntity;


    }

    // restoreEntity
    public EntityDetail restoreEntity(String    userId,
                                      String    deletedEntityGUID)
            throws
            InvalidParameterException,
            RepositoryErrorException,
            EntityNotKnownException,
            EntityNotDeletedException,
            UserNotAuthorizedException
    {
        final String methodName = "restoreEntity";

        /*
         * Validate parameters
         */
        super.manageInstanceParameterValidation(userId, deletedEntityGUID, methodName);

        /*
         * Locate entity
         */
        EntityDetail  entity  = graphStore.getEntityFromStore(deletedEntityGUID);

        repositoryValidator.validateEntityFromStore(repositoryName, deletedEntityGUID, entity, methodName);

        repositoryValidator.validateEntityIsDeleted(repositoryName, entity, methodName);

        /*
         * Validation is complete.  It is ok to restore the entity.
         */

        EntityDetail restoredEntity = new EntityDetail(entity);

        restoredEntity.setStatus(entity.getStatusOnDelete());
        restoredEntity.setStatusOnDelete(null);

        restoredEntity = repositoryHelper.incrementVersion(userId, entity, restoredEntity);

        graphStore.updateEntityInStore(restoredEntity);

        return restoredEntity;
    }


    // deleteRelationship
    public Relationship deleteRelationship(String    userId,
                                           String    typeDefGUID,
                                           String    typeDefName,
                                           String    obsoleteRelationshipGUID)
            throws
            InvalidParameterException,
            RepositoryErrorException,
            RelationshipNotKnownException,
            UserNotAuthorizedException
    {
        final String  methodName = "deleteRelationship";

        /*
         * Validate parameters
         */
        this.removeInstanceParameterValidation(userId, typeDefGUID, typeDefName, obsoleteRelationshipGUID, methodName);

        /*
         * Locate relationship
         */
        Relationship  relationship  = this.getRelationship(userId, obsoleteRelationshipGUID);

        repositoryValidator.validateTypeForInstanceDelete(repositoryName, typeDefGUID, typeDefName, relationship, methodName);

        /*
         * A delete is a soft-delete that updates the status to DELETED.
         */
        Relationship   updatedRelationship = new Relationship(relationship);

        updatedRelationship.setStatusOnDelete(relationship.getStatus());
        updatedRelationship.setStatus(InstanceStatus.DELETED);

        updatedRelationship = repositoryHelper.incrementVersion(userId, relationship, updatedRelationship);

        graphStore.updateRelationshipInStore(updatedRelationship);

        return updatedRelationship;
    }


    // restoreRelationship
    public Relationship restoreRelationship(String    userId,
                                            String    deletedRelationshipGUID)
            throws
            InvalidParameterException,
            RepositoryErrorException,
            RelationshipNotKnownException,
            RelationshipNotDeletedException,
            UserNotAuthorizedException
    {
        final String  methodName = "restoreRelationship";

        /*
         * Validate parameters
         */
        this.manageInstanceParameterValidation(userId, deletedRelationshipGUID, methodName);

        /*
         * Locate relationship
         */
        Relationship  relationship  = graphStore.getRelationshipFromStore(deletedRelationshipGUID);

        repositoryValidator.validateRelationshipFromStore(repositoryName, deletedRelationshipGUID, relationship, methodName);
        repositoryValidator.validateRelationshipIsDeleted(repositoryName, relationship, methodName);

        /*
         * Validation is complete.  It is ok to restore the relationship.
         */

        Relationship restoredRelationship = new Relationship(relationship);
        restoredRelationship.setStatus(relationship.getStatusOnDelete());
        relationship.setStatusOnDelete(null);

        restoredRelationship = repositoryHelper.incrementVersion(userId, relationship, restoredRelationship);

        graphStore.updateRelationshipInStore(restoredRelationship);

        return restoredRelationship;

    }



    // reIdentifyEntity
    public EntityDetail reIdentifyEntity(String     userId,
                                         String     typeDefGUID,
                                         String     typeDefName,
                                         String     entityGUID,
                                         String     newEntityGUID)
            throws
            InvalidParameterException,
            RepositoryErrorException,
            EntityNotKnownException,
            UserNotAuthorizedException
    {
        final String  methodName            = "reIdentifyEntity";
        final String  guidParameterName     = "typeDefGUID";
        final String  nameParameterName     = "typeDefName";
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
        EntityDetail  entity  = graphStore.getEntityFromStore(entityGUID);

        repositoryValidator.validateEntityFromStore(repositoryName, entityGUID, entity, methodName);

        /*
         * Validation complete - ok to make changes
         */
        EntityDetail   updatedEntity = new EntityDetail(entity);

        updatedEntity.setGUID(newEntityGUID);

        updatedEntity = repositoryHelper.incrementVersion(userId, entity, updatedEntity);

        graphStore.removeEntityFromStore(entityGUID);
        graphStore.createEntityInStore(updatedEntity);

        return updatedEntity;
    }



    // reHomeEntity
    public EntityDetail reHomeEntity(String         userId,
                                     String         entityGUID,
                                     String         typeDefGUID,
                                     String         typeDefName,
                                     String         homeMetadataCollectionId,
                                     String         newHomeMetadataCollectionId,
                                     String         newHomeMetadataCollectionName)
            throws
            InvalidParameterException,
            RepositoryErrorException,
            EntityNotKnownException,
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
        EntityDetail  entity  = graphStore.getEntityFromStore(entityGUID);

        repositoryValidator.validateEntityFromStore(repositoryName, entityGUID, entity, methodName);


        /*
         * Validation complete - ok to make changes
         */
        EntityDetail   updatedEntity = new EntityDetail(entity);

        updatedEntity.setMetadataCollectionId(newHomeMetadataCollectionId);
        updatedEntity.setMetadataCollectionName(newHomeMetadataCollectionName);
        updatedEntity.setInstanceProvenanceType(InstanceProvenanceType.LOCAL_COHORT);

        updatedEntity = repositoryHelper.incrementVersion(userId, entity, updatedEntity);

        graphStore.updateEntityInStore(updatedEntity);

        return updatedEntity;
    }


    // reTypeEntity
    public EntityDetail reTypeEntity(String         userId,
                                     String         entityGUID,
                                     TypeDefSummary currentTypeDefSummary,
                                     TypeDefSummary newTypeDefSummary)
            throws
            InvalidParameterException,
            RepositoryErrorException,
            TypeErrorException,
            PropertyErrorException,
            ClassificationErrorException,
            EntityNotKnownException,
            UserNotAuthorizedException
    {
        final String  methodName                  = "reTypeEntity";
        final String  entityParameterName         = "entityGUID";
        final String  currentTypeDefParameterName = "currentTypeDefSummary";
        final String  newTypeDefParameterName     = "newTypeDefSummary";

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
        EntityDetail  entity  = graphStore.getEntityFromStore(entityGUID);

        repositoryValidator.validateEntityFromStore(repositoryName, entityGUID, entity, methodName);

        repositoryValidator.validateInstanceType(repositoryName,
                entity,
                currentTypeDefParameterName,
                currentTypeDefParameterName,
                currentTypeDefSummary.getGUID(),
                currentTypeDefSummary.getName());

        repositoryValidator.validatePropertiesForType(repositoryName,
                newTypeDefParameterName,
                newTypeDefSummary,
                entity.getProperties(),
                methodName);

        repositoryValidator.validateClassificationList(repositoryName,
                entityParameterName,
                entity.getClassifications(),
                newTypeDefSummary.getName(),
                methodName);

        /*
         * Validation complete - ok to make changes
         */
        EntityDetail   updatedEntity = new EntityDetail(entity);
        InstanceType   newInstanceType = repositoryHelper.getNewInstanceType(repositoryName, newTypeDefSummary);

        updatedEntity.setType(newInstanceType);

        updatedEntity = repositoryHelper.incrementVersion(userId, entity, updatedEntity);

        graphStore.updateEntityInStore(entity);

        return updatedEntity;
    }


    // reIdentifyRelationship
    public Relationship reIdentifyRelationship(String     userId,
                                               String     typeDefGUID,
                                               String     typeDefName,
                                               String     relationshipGUID,
                                               String     newRelationshipGUID)
            throws
            InvalidParameterException,
            RepositoryErrorException,
            RelationshipNotKnownException,
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
         * Validation complete - ok to make changes
         */
        Relationship   updatedRelationship = new Relationship(relationship);

        updatedRelationship.setGUID(newRelationshipGUID);

        updatedRelationship = repositoryHelper.incrementVersion(userId, relationship, updatedRelationship);

        graphStore.removeRelationshipFromStore(relationshipGUID);
        graphStore.createRelationshipInStore(updatedRelationship);

        return updatedRelationship;
    }


    // reTypeRelationship
    public Relationship reTypeRelationship(String         userId,
                                           String         relationshipGUID,
                                           TypeDefSummary currentTypeDefSummary,
                                           TypeDefSummary newTypeDefSummary)
            throws
            InvalidParameterException,
            RepositoryErrorException,
            TypeErrorException,
            PropertyErrorException,
            RelationshipNotKnownException,
            UserNotAuthorizedException
    {

        final String methodName                   = "reTypeRelationship";
        final String relationshipParameterName    = "relationshipGUID";
        final String currentTypeDefParameterName  = "currentTypeDefSummary";
        final String newTypeDefParameterName      = "newTypeDefSummary";

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
         * Validation complete - ok to make changes
         */
        Relationship   updatedRelationship = new Relationship(relationship);
        InstanceType   newInstanceType = repositoryHelper.getNewInstanceType(repositoryName, newTypeDefSummary);

        updatedRelationship.setType(newInstanceType);

        updatedRelationship = repositoryHelper.incrementVersion(userId, relationship, updatedRelationship);

        graphStore.updateRelationshipInStore(updatedRelationship);

        return updatedRelationship;
    }


    // reHomeRelationship
    public Relationship reHomeRelationship(String   userId,
                                           String   relationshipGUID,
                                           String   typeDefGUID,
                                           String   typeDefName,
                                           String   homeMetadataCollectionId,
                                           String   newHomeMetadataCollectionId,
                                           String   newHomeMetadataCollectionName)
            throws
            InvalidParameterException,
            RepositoryErrorException,
            RelationshipNotKnownException,
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
        Relationship  relationship  = this.getRelationship(userId, relationshipGUID);

        /*
         * Validation complete - ok to make changes
         */
        Relationship   updatedRelationship = new Relationship(relationship);

        updatedRelationship.setMetadataCollectionId(newHomeMetadataCollectionId);
        updatedRelationship.setMetadataCollectionName(newHomeMetadataCollectionName);
        updatedRelationship.setInstanceProvenanceType(InstanceProvenanceType.LOCAL_COHORT);

        updatedRelationship = repositoryHelper.incrementVersion(userId, relationship, updatedRelationship);

        graphStore.updateRelationshipInStore(updatedRelationship);

        return updatedRelationship;
    }



    // updateEntityClassification
    public EntityDetail updateEntityClassification(String               userId,
                                                   String               entityGUID,
                                                   String               classificationName,
                                                   InstanceProperties   properties)
            throws
            InvalidParameterException,
            RepositoryErrorException,
            EntityNotKnownException,
            ClassificationErrorException,
            PropertyErrorException,
            UserNotAuthorizedException
    {
        final String  methodName = "updateEntityClassification";

        /*
         * Validate parameters
         */
        super.updateEntityClassificationParameterValidation(userId, entityGUID, classificationName, properties);

        /*
         * Locate entity
         */
        EntityDetail entity = graphStore.getEntityFromStore(entityGUID);

        repositoryValidator.validateEntityFromStore(repositoryName, entityGUID, entity, methodName);
        repositoryValidator.validateEntityIsNotDeleted(repositoryName, entity, methodName);

        Classification classification = repositoryHelper.getClassificationFromEntity(repositoryName,
                entity,
                classificationName,
                methodName);

        Classification  newClassification = new Classification(classification);

        newClassification.setProperties(properties);

        repositoryHelper.incrementVersion(userId, classification, newClassification);

        EntityDetail updatedEntity = repositoryHelper.updateClassificationInEntity(repositoryName,
                userId,
                entity,
                newClassification,
                methodName);

        updatedEntity = repositoryHelper.incrementVersion(userId, entity, updatedEntity);

        graphStore.updateEntityInStore(updatedEntity);


        return updatedEntity;
    }


    /*
     * Reference Copies
     */

    public void saveEntityReferenceCopy(String         userId,
                                        EntityDetail   entity)
            throws
            InvalidParameterException,
            RepositoryErrorException,
            TypeErrorException,
            PropertyErrorException,
            HomeEntityException,
            EntityConflictException,
            InvalidEntityException,
            UserNotAuthorizedException
    {
        final String  methodName            = "saveEntityReferenceCopy";
        final String  instanceParameterName = "entity";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);

        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateReferenceInstanceHeader(repositoryName,
                metadataCollectionId,
                instanceParameterName,
                entity,
                methodName);

        // There may (or not) already be a proxy for this entity:
        // - if no proxy, create the ref copy on the store
        // - if there is a proxy, it is replaced by this reference copy. Note that the proxy flag should be cleared.
        graphStore.createEntityInStore(entity);

    }


    /*
     * Removal o proxy entities: if a proxy entity existed prior to the ref copy being saved, it was replaced by the
     * ref copy - so when we now purge the ref copy there is no need to remove any proxy - it has already been subsumed.
     */
    public void purgeEntityReferenceCopy(String   userId,
                                         String   entityGUID,
                                         String   typeDefGUID,
                                         String   typeDefName,
                                         String   homeMetadataCollectionId)
            throws
            InvalidParameterException,
            RepositoryErrorException,
            EntityNotKnownException,
            HomeEntityException,
            UserNotAuthorizedException
    {
        final String methodName                = "purgeEntityReferenceCopy";
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

        EntityDetail  entity = graphStore.getEntityFromStore(entityGUID);
        if (entity != null)
        {
            graphStore.removeEntityFromStore(entityGUID);
        }
        else
        {
            OMRSErrorCode errorCode = OMRSErrorCode.ENTITY_NOT_KNOWN;
            String        errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(entityGUID,
                    methodName,
                    repositoryName);

            throw new EntityNotKnownException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }
    }




    public void refreshEntityReferenceCopy(String   userId,
                                           String   entityGUID,
                                           String   typeDefGUID,
                                           String   typeDefName,
                                           String   homeMetadataCollectionId)
            throws
            InvalidParameterException,
            RepositoryErrorException,
            EntityNotKnownException,
            HomeEntityException,
            UserNotAuthorizedException
    {
        final String methodName                = "refreshEntityReferenceCopy";

        /*
         * This method should be handled by the local repository connector since this repository connector
         * does not have event handling powers (no event mapper)
         */
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


    public void saveRelationshipReferenceCopy(String         userId,
                                              Relationship   relationship)
            throws
            InvalidParameterException,
            RepositoryErrorException,
            TypeErrorException,
            EntityNotKnownException,
            PropertyErrorException,
            HomeRelationshipException,
            RelationshipConflictException,
            InvalidRelationshipException,
            UserNotAuthorizedException
    {
        final String  methodName            = "saveRelationshipReferenceCopy";
        final String  instanceParameterName = "relationship";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateReferenceInstanceHeader(repositoryName,
                metadataCollectionId,
                instanceParameterName,
                relationship,
                methodName);

        graphStore.createEntityProxyInStore(relationship.getEntityOneProxy());
        graphStore.createEntityProxyInStore(relationship.getEntityTwoProxy());
        graphStore.createRelationshipInStore(relationship);
    }


    public void purgeRelationshipReferenceCopy(String   userId,
                                               String   relationshipGUID,
                                               String   typeDefGUID,
                                               String   typeDefName,
                                               String   homeMetadataCollectionId)
            throws
            InvalidParameterException,
            RepositoryErrorException,
            RelationshipNotKnownException,
            HomeRelationshipException,
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
        graphStore.removeRelationshipFromStore(relationshipGUID);
    }



    public void refreshRelationshipReferenceCopy(String userId,
                                                 String   relationshipGUID,
                                                 String   typeDefGUID,
                                                 String   typeDefName,
                                                 String   homeMetadataCollectionId)
            throws
            InvalidParameterException,
            RepositoryErrorException,
            RelationshipNotKnownException,
            HomeRelationshipException,
            UserNotAuthorizedException
    {
        final String methodName = "refreshRelationshipReferenceCopy";

        /*
         * This method should be handled by the local repository connector since this repository connector
         * does not have event handling powers (no event mapper)
         */
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



    // getEntityNeighborhood
    public InstanceGraph getEntityNeighborhood(String               userId,
                                               String               entityGUID,
                                               List<String>         entityTypeGUIDs,
                                               List<String>         relationshipTypeGUIDs,
                                               List<InstanceStatus> limitResultsByStatus,
                                               List<String>         limitResultsByClassification,
                                               Date                 asOfTime,
                                               int                  level)
            throws
            InvalidParameterException,
            RepositoryErrorException,
            EntityNotKnownException,
            TypeErrorException,
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
                this.validateTypeGUID(repositoryName, entityTypeGUIDParameterName, guid, methodName);
            }
        }

        if (relationshipTypeGUIDs != null)
        {
            for (String guid : relationshipTypeGUIDs)
            {
                this.validateTypeGUID(repositoryName, relationshipTypeGUIDParameterName, guid, methodName);
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


        if (asOfTime != null) {
            // Not supported
            log.error("{} does not support asOfTime parameter", methodName);

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

        /*
         * Delegate to the graph store
         */

        InstanceGraph subgraph = graphStore.getSubGraph(entityGUID, entityTypeGUIDs, relationshipTypeGUIDs, limitResultsByStatus, limitResultsByClassification, level);

        return subgraph;
    }




    // Return the list of entities that are of the types listed in entityTypeGUIDs and are connected, either directly or
    // indirectly to the entity identified by startEntityGUID.
    //
    // This is implemented by delegating to getEntityNeighbourhood with level = 1 and no relationship type filters
    // The specified entityType, status and classification filters are passed through to getEntityNeighbourhood.
    //

    public  List<EntityDetail> getRelatedEntities(String               userId,
                                                  String               startEntityGUID,
                                                  List<String>         entityTypeGUIDs,
                                                  int                  fromEntityElement,
                                                  List<InstanceStatus> limitResultsByStatus,
                                                  List<String>         limitResultsByClassification,
                                                  Date                 asOfTime,
                                                  String               sequencingProperty,
                                                  SequencingOrder      sequencingOrder,
                                                  int                  pageSize)
            throws
            InvalidParameterException,
            TypeErrorException,
            RepositoryErrorException,
            EntityNotKnownException,
            PropertyErrorException,
            PagingErrorException,
            FunctionNotSupportedException,
            UserNotAuthorizedException
    {
        final String  methodName = "getRelatedEntities";

        /*
         * Validate parameters
         */
        this.getRelatedEntitiesParameterValidation(userId,
                startEntityGUID,
                entityTypeGUIDs,
                fromEntityElement,
                limitResultsByStatus,
                limitResultsByClassification,
                asOfTime,
                sequencingProperty,
                sequencingOrder,
                pageSize);


        if (asOfTime != null) {
            // Not supported
            log.error("{} does not support asOfTime parameter", methodName);

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

        /*
         * Perform operation
         */
        InstanceGraph adjacentGraph = this.getEntityNeighborhood( userId, startEntityGUID, entityTypeGUIDs, null, limitResultsByStatus, limitResultsByClassification, null, 1);

        if (adjacentGraph != null) {

            // Pick out the entities - since these are all EntityDetail objects (this method does not return proxy objects)
            List<EntityDetail> adjacentEntities = adjacentGraph.getEntities();

            return repositoryHelper.formatEntityResults(adjacentEntities, fromEntityElement, sequencingProperty, sequencingOrder, pageSize);

        }
        return null;

    }


    // Return all of the relationships and intermediate entities that connect the startEntity with the endEntity.
    public  InstanceGraph getLinkingEntities(String                    userId,
                                             String                    startEntityGUID,
                                             String                    endEntityGUID,
                                             List<InstanceStatus>      limitResultsByStatus,
                                             Date                      asOfTime)
            throws
            InvalidParameterException,
            RepositoryErrorException,
            EntityNotKnownException,
            PropertyErrorException,
            FunctionNotSupportedException,
            UserNotAuthorizedException
    {
        final String methodName = "getLinkingEntities";

        /*
         * Validate parameters
         */
        this.getLinkingEntitiesParameterValidation(userId,
                startEntityGUID,
                endEntityGUID,
                limitResultsByStatus,
                asOfTime);


        if (asOfTime != null) {
            // Not supported
            log.error("{} does not support asOfTime parameter", methodName);

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


        /*
         * Perform operation
         */

        /*
         * Delegate to the graph store
         */


        // This method sets a couple of limits on how far or wide the graph store will traverse looking for paths.
        // The first limit is 'maxPaths' - the traversal will stop if and when this number of traversals has been found.
        // THe second limit is 'maxDepth' - the traversal will stop when any traverser reaches a path length exceeding this.
        // For now these limits are set hard here - they could be made soft/configurable.
        int maxPaths = 20;
        int maxDepth = 40;
        try {

            InstanceGraph subgraph = graphStore.getPaths(startEntityGUID, endEntityGUID, limitResultsByStatus, maxPaths, maxDepth);
            return subgraph;
        }
        catch (Exception e) {
            GraphOMRSErrorCode errorCode = GraphOMRSErrorCode.CONNECTED_ENTITIES_FAILURE;

            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(startEntityGUID, endEntityGUID, methodName,
                    this.getClass().getName(),
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