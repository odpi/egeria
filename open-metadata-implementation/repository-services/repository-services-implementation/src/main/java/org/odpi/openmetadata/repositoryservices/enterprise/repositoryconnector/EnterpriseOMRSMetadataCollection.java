/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollectionBase;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.HistorySequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchClassifications;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryValidator;
import org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.control.FederationControl;
import org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.control.ParallelFederationControl;
import org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.control.SequentialFederationControl;
import org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.executors.*;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSAuditCode;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;

import java.util.*;


/**
 * EnterpriseOMRSMetadataCollection executes the calls to the open metadata repositories registered
 * with the OMRSEnterpriseConnectorManager.  The effect is a federated view over these open metadata
 * repositories.
 * <p>
 *     EnterpriseOMRSMetadataCollection is part of an EnterpriseOMRSRepositoryConnector.  The EnterpriseOMRSRepositoryConnector
 *     holds the list of OMRS Connectors, one for each of the metadata repositories.  This list may change
 *     over time as metadata repositories register and deregister with the connected cohorts.
 *     The EnterpriseOMRSRepositoryConnector is responsible for keeping the list of connectors up-to-date through
 *     contact with the OMRSEnterpriseConnectorManager.
 * </p>
 * <p>
 *     When a request is made to the EnterpriseOMRSMetadataCollection, it calls the EnterpriseOMRSRepositoryConnector
 *     to request the appropriate list of metadata collection for the request.  Then the EnterpriseOMRSConnector
 *     calls the appropriate remote connectors.
 * </p>
 * <p>
 *     The first OMRS Connector in the list is the OMRS Repository Connector for the "local" repository.
 *     The local repository is favoured when new metadata is to be created, unless the type of metadata
 *     is not supported by the local repository. In which case, the EnterpriseOMRSMetadataCollection searches its
 *     list looking for the first metadata repository that supports the metadata type and stores it there.
 * </p>
 * <p>
 *     Updates and deletes are routed to the owning (home) repository.  Searches are made to each repository in turn
 *     and the duplicates are removed.  Queries are directed to the local repository and then the remote repositories
 *     until all the requested metadata is assembled.
 * </p>
 */
class EnterpriseOMRSMetadataCollection extends OMRSMetadataCollectionBase
{
    /*
     * Private variables for a metadata collection instance
     */
    private final EnterpriseOMRSRepositoryConnector enterpriseParentConnector;
    private final String                            localMetadataCollectionId;
    private final AuditLog                          auditLog;


    /**
     * Constructor ensures the metadata collection is linked to its connector and knows its metadata collection id.
     *
     * @param enterpriseParentConnector connector that this metadata collection supports.  The connector has the information
     *                        to call the metadata repository.
     * @param repositoryName name of the repository used for logging.
     * @param repositoryHelper class used to build type definitions and instances.
     * @param repositoryValidator class used to validate type definitions and instances.
     * @param metadataCollectionId unique Identifier of the enterprise metadata collection id.
     * @param localMetadataCollectionId unique Identifier of the local repository's metadata collection id (will be null if no local repository).
     */
    EnterpriseOMRSMetadataCollection(EnterpriseOMRSRepositoryConnector enterpriseParentConnector,
                                     String                            repositoryName,
                                     OMRSRepositoryHelper              repositoryHelper,
                                     OMRSRepositoryValidator           repositoryValidator,
                                     String                            metadataCollectionId,
                                     String                            localMetadataCollectionId,
                                     AuditLog auditLog)
    {
        /*
         * The metadata collection id is the unique identifier for the metadata collection.  It is managed by the super class.
         */
        super(enterpriseParentConnector, repositoryName, repositoryHelper, repositoryValidator, metadataCollectionId);

        /*
         * Save enterpriseParentConnector since this has the connection information and
         * access to the metadata about the open metadata repository cohort.
         */
        this.enterpriseParentConnector = enterpriseParentConnector;
        this.localMetadataCollectionId = localMetadataCollectionId;
        this.auditLog                  = auditLog;
    }


    /* ==============================
     * Group 2: Working with typedefs
     */

    /**
     * Returns the list of different types of metadata organized into two groups.  The first are the
     * attribute type definitions (AttributeTypeDefs).  These provide types for properties in full
     * type definitions.  Full type definitions (TypeDefs) describe types for entities, relationships
     * and classifications.
     *
     * @param userId  unique identifier for requesting user.
     * @return TypeDefGallery  List of different categories of TypeDefs.
     * @throws InvalidParameterException the userId is null
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public TypeDefGallery getAllTypes(String userId) throws InvalidParameterException,
                                                            RepositoryErrorException,
                                                            UserNotAuthorizedException
    {
        final String methodName = "getAllTypes";

        /*
         * Validate parameters
         */
        super.basicRequestValidation(userId, methodName);

        /*
         * Validation complete, ok to continue with request
         *
         * The list of cohort connectors are retrieved for each request to ensure that any changes in
         * the shape of the cohort are reflected immediately.
         */
        List<OMRSRepositoryConnector> cohortConnectors = enterpriseParentConnector.getCohortConnectors(methodName);

        FederationControl federationControl = new ParallelFederationControl(userId, cohortConnectors, auditLog, methodName);
        GetAllTypesExecutor executor = new GetAllTypesExecutor(userId,
                                                               methodName,
                                                               localMetadataCollectionId,
                                                               auditLog,
                                                               repositoryValidator);

        /*
         * Ready to process the request.  Create requests occur in the first repository that accepts the call.
         * Some repositories may produce exceptions.  These exceptions are saved and will be returned if
         * there are no positive results from any repository.
         */
        federationControl.executeCommand(executor);

        return executor.getResults();
    }


    /**
     * Returns a list of type definitions that have the specified name.  Type names should be unique.  This
     * method allows wildcard character to be included in the name.  These are * (asterisk) for an
     * arbitrary string of characters and ampersand for an arbitrary character.
     *
     * @param userId unique identifier for requesting user.
     * @param name name of the TypeDefs to return (including wildcard characters).
     * @return TypeDefs list.
     * @throws InvalidParameterException the name of the TypeDef is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public TypeDefGallery findTypesByName(String   userId,
                                          String   name) throws InvalidParameterException,
                                                                RepositoryErrorException,
                                                                UserNotAuthorizedException
    {
        final String   methodName        = "findTypesByName";
        final String   nameParameterName = "name";

        /*
         * Validate parameters
         */
        super.typeNameParameterValidation(userId, name, nameParameterName, methodName);

        /*
         * Retrieve types
         */
        TypeDefGallery allTypes = this.getAllTypes(userId);

        if (allTypes != null)
        {
            return super.filterTypesByWildCardName(repositoryHelper.getActiveTypeDefs(),
                                                   repositoryHelper.getActiveAttributeTypeDefs(),
                                                   name);
        }

        return null;
    }


    /**
     * Returns all the TypeDefs for a specific category.
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
                                                                                 UserNotAuthorizedException
    {
        final String methodName            = "findTypeDefsByCategory";
        final String categoryParameterName = "category";

        /*
         * Validate parameters
         */
        super.typeDefCategoryParameterValidation(userId, category, categoryParameterName, methodName);

        /*
         * Perform operation
         */
        TypeDefGallery allTypes = this.getAllTypes(userId);

        if (allTypes != null)
        {
            return super.filterTypeDefsByCategory(allTypes.getTypeDefs(), category);
        }

        return null;
    }


    /**
     * Returns all the AttributeTypeDefs for a specific category.
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
                                                                                                            UserNotAuthorizedException
    {
        final String methodName            = "findAttributeTypeDefsByCategory";
        final String categoryParameterName = "category";

        /*
         * Validate parameters
         */
        super.attributeTypeDefCategoryParameterValidation(userId, category, categoryParameterName, methodName);

        /*
         * Perform operation
         */
        TypeDefGallery allTypes = this.getAllTypes(userId);

        if (allTypes != null)
        {
            return super.filterAttributeTypeDefsByCategory(allTypes.getAttributeTypeDefs(), category);
        }

        return null;
    }


    /**
     * Return the TypeDefs that have the properties matching the supplied match criteria.
     *
     * @param userId  unique identifier for requesting user.
     * @param matchCriteria  TypeDefProperties a list of property names.
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
        super.typeDefPropertyParameterValidation(userId, matchCriteria, matchCriteriaParameterName, methodName);

        /*
         * Perform operation
         */
        TypeDefGallery allTypes = this.getAllTypes(userId);

        if (allTypes != null)
        {
            return this.filterTypeDefsByProperty(allTypes.getTypeDefs(), matchCriteria);
        }

        return null;
    }


    /**
     * Return the types that are linked to the elements from the specified standard.
     *
     * @param userId  unique identifier for requesting user.
     * @param standard  name of the standard null means any.
     * @param organization  name of the organization null means any.
     * @param identifier  identifier of the element in the standard null means any.
     * @return TypeDefs list each entry in the list contains a typedef.  This is a structure
     * describing the TypeDef's category and properties.
     * @throws InvalidParameterException all attributes of the external id are null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public  List<TypeDef> findTypesByExternalID(String    userId,
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
        super.typeDefExternalIDParameterValidation(userId, standard, organization, identifier, methodName);

        /*
         * Perform operation
         */
        TypeDefGallery allTypes = this.getAllTypes(userId);

        if (allTypes != null)
        {
            return super.filterTypesByExternalID(allTypes.getTypeDefs(), standard, organization, identifier);
        }

        return null;
    }


    /**
     * Return the TypeDefs that match the search criteria.
     *
     * @param userId  unique identifier for requesting user.
     * @param searchCriteria  String search criteria.
     * @return TypeDefs list each entry in the list contains a typedef.  This is a structure
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
        this.typeDefSearchParameterValidation(userId, searchCriteria, searchCriteriaParameterName, methodName);

        /*
         * Perform operation
         */
        TypeDefGallery allTypes = this.getAllTypes(userId);

        if (allTypes != null)
        {
            return super.filterTypeDefsBySearchCriteria(allTypes.getTypeDefs(), searchCriteria);
        }

        return null;
    }


    /**
     * Return the TypeDef identified by the GUID.
     *
     * @param userId  unique identifier for requesting user.
     * @param guid  String unique id of the TypeDef
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
                                                           UserNotAuthorizedException
    {
        final String methodName        = "getTypeDefByGUID";
        final String guidParameterName = "guid";

        /*
         * Validate parameters
         */
        super.typeGUIDParameterValidation(userId, guid, guidParameterName, methodName);

        /*
         * Perform operation
         */
        TypeDefGallery allTypes = this.getAllTypes(userId);

        if (allTypes != null)
        {
            TypeDef result = super.filterTypeDefsByGUID(allTypes.getTypeDefs(), guid);

            if (result == null)
            {
                super.reportUnknownTypeGUID(guid, guidParameterName, methodName);
            }

            return result;
        }

        return null;
    }


    /**
     * Return the AttributeTypeDef identified by the GUID.
     *
     * @param userId  unique identifier for requesting user.
     * @param guid  String unique id of the TypeDef
     * @return TypeDef structure describing its category and properties.
     * @throws InvalidParameterException the guid is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws TypeDefNotKnownException The requested TypeDef is not known in the metadata collection.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public AttributeTypeDef getAttributeTypeDefByGUID(String    userId,
                                                      String    guid) throws InvalidParameterException,
                                                                             RepositoryErrorException,
                                                                             TypeDefNotKnownException,
                                                                             UserNotAuthorizedException
    {
        final String methodName        = "getAttributeTypeDefByGUID";
        final String guidParameterName = "guid";

        /*
         * Validate parameters
         */
        super.typeGUIDParameterValidation(userId, guid, guidParameterName, methodName);

        /*
         * Perform operation
         */
        TypeDefGallery allTypes = this.getAllTypes(userId);

        if (allTypes != null)
        {
            AttributeTypeDef result = super.filterAttributeTypeDefsByGUID(allTypes.getAttributeTypeDefs(), guid);

            if (result == null)
            {
                super.reportUnknownTypeGUID(guid, guidParameterName, methodName);
            }

            return result;
        }

        return null;
    }


    /**
     * Return the TypeDef identified by the unique name.
     *
     * @param userId  unique identifier for requesting user.
     * @param name  String name of the TypeDef.
     * @return TypeDef structure describing its category and properties.
     * @throws InvalidParameterException the name is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws TypeDefNotKnownException the requested TypeDef is not found in the metadata collection.
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
        this.typeNameParameterValidation(userId, name, nameParameterName, methodName);

        /*
         * Perform operation
         */
        TypeDefGallery allTypes = this.getAllTypes(userId);

        if (allTypes != null)
        {
            TypeDef result = super.filterTypeDefsByName(allTypes.getTypeDefs(), name);

            if (result == null)
            {
                super.reportUnknownTypeName(name, methodName);
            }

            return result;
        }

        return null;
    }


    /**
     * Return the AttributeTypeDef identified by the unique name.
     *
     * @param userId  unique identifier for requesting user.
     * @param name  String name of the TypeDef.
     * @return TypeDef structure describing its category and properties.
     * @throws InvalidParameterException the name is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws TypeDefNotKnownException the requested TypeDef is not found in the metadata collection.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public AttributeTypeDef getAttributeTypeDefByName(String    userId,
                                                      String    name) throws InvalidParameterException,
                                                                             RepositoryErrorException,
                                                                             TypeDefNotKnownException,
                                                                             UserNotAuthorizedException
    {
        final String  methodName = "getAttributeTypeDefByName";
        final String  nameParameterName = "name";

        /*
         * Validate parameters
         */
        this.typeNameParameterValidation(userId, name, nameParameterName, methodName);

        /*
         * Perform operation
         */
        TypeDefGallery allTypes = this.getAllTypes(userId);

        if (allTypes != null)
        {
            AttributeTypeDef result = super.filterAttributeTypeDefsByName(repositoryHelper.getKnownAttributeTypeDefs(), name);

            if (result == null)
            {
                super.reportUnknownTypeName(name, methodName);
            }

            return result;
        }

        return null;
    }


    /**
     * Create a collection of related types.
     *
     * @param userId  unique identifier for requesting user.
     * @param newTypes  TypeDefGalleryResponse structure describing the new AttributeTypeDefs and TypeDefs.
     * @throws FunctionNotSupportedException the repository does not support this call.
     */
    @Override
    public  void addTypeDefGallery(String          userId,
                                   TypeDefGallery newTypes) throws FunctionNotSupportedException

    {
        final String    methodName = "addTypeDefGallery";

        throwNotEnterpriseFunction(methodName);
    }


    /**
     * Create a definition of a new TypeDef.   This new TypeDef is pushed to each repository that will accept it.
     * An exception is passed to the caller if the TypeDef is invalid, or if none of the repositories accept it.
     *
     * @param userId  unique identifier for requesting user.
     * @param newTypeDef  TypeDef structure describing the new TypeDef.
     * @throws FunctionNotSupportedException the repository does not support this call.
     */
    @Override
    public void addTypeDef(String  userId,
                           TypeDef newTypeDef) throws FunctionNotSupportedException
    {
        final String    methodName = "addTypeDef";

        throwNotEnterpriseFunction(methodName);
    }


    /**
     * Create a definition of a new AttributeTypeDef.
     *
     * @param userId  unique identifier for requesting user.
     * @param newAttributeTypeDef  TypeDef structure describing the new TypeDef.
     * @throws FunctionNotSupportedException the repository does not support this call.
     */
    @Override
    public  void addAttributeTypeDef(String           userId,
                                     AttributeTypeDef newAttributeTypeDef) throws FunctionNotSupportedException
    {
        final String    methodName = "addAttributeTypeDef";

        throwNotEnterpriseFunction(methodName);
    }


    /**
     * Verify that a definition of a TypeDef is either new or matches the definition already stored.
     *
     * @param userId  unique identifier for requesting user.
     * @param typeDef  TypeDef structure describing the TypeDef to test.
     * @return boolean true means the TypeDef matches the local definition false means the TypeDef is not known.
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
        super.typeDefParameterValidation(userId, typeDef, typeDefParameterName, methodName);

        /*
         * Validation complete, ok to continue with request
         *
         * The list of cohort connectors are retrieved for each request to ensure that any changes in
         * the shape of the cohort are reflected immediately.
         */
        List<OMRSRepositoryConnector> cohortConnectors = enterpriseParentConnector.getCohortConnectors(methodName);

        FederationControl     federationControl = new SequentialFederationControl(userId, cohortConnectors, auditLog, methodName);
        VerifyTypeDefExecutor executor          = new VerifyTypeDefExecutor(userId, typeDef, auditLog, methodName);

        /*
         * Ready to process the request.  Create requests occur in the first repository that accepts the call.
         * Some repositories may produce exceptions.  These exceptions are saved and will be returned if
         * there are no positive results from any repository.
         */
        federationControl.executeCommand(executor);

        return executor.getResult();
    }


    /**
     * Verify that a definition of an AttributeTypeDef is either new or matches the definition already stored.
     *
     * @param userId  unique identifier for requesting user.
     * @param attributeTypeDef  TypeDef structure describing the TypeDef to test.
     * @return boolean true means the TypeDef matches the local definition false means the TypeDef is not known.
     * @throws InvalidParameterException the TypeDef is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws TypeDefNotSupportedException the repository is not able to support this TypeDef.
     * @throws TypeDefConflictException the new TypeDef conflicts with an existing TypeDef.
     * @throws InvalidTypeDefException the new TypeDef has invalid contents.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public  boolean verifyAttributeTypeDef(String           userId,
                                           AttributeTypeDef attributeTypeDef) throws InvalidParameterException,
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
        super.attributeTypeDefParameterValidation(userId, attributeTypeDef, typeDefParameterName, methodName);

        /*
         * Validation complete, ok to continue with request
         *
         * The list of cohort connectors are retrieved for each request to ensure that any changes in
         * the shape of the cohort are reflected immediately.
         */
        List<OMRSRepositoryConnector> cohortConnectors = enterpriseParentConnector.getCohortConnectors(methodName);

        FederationControl              federationControl = new SequentialFederationControl(userId, cohortConnectors, auditLog, methodName);
        VerifyAttributeTypeDefExecutor executor          = new VerifyAttributeTypeDefExecutor(userId, attributeTypeDef, auditLog, methodName);

        /*
         * Ready to process the request.  Create requests occur in the first repository that accepts the call.
         * Some repositories may produce exceptions.  These exceptions are saved and will be returned if
         * there are no positive results from any repository.
         */
        federationControl.executeCommand(executor);

        return executor.getResult();
    }


    /**
     * Update one or more properties of the TypeDef.  The TypeDefPatch controls what types of updates
     * are safe to make to the TypeDef.
     *
     * @param userId  unique identifier for requesting user.
     * @param typeDefPatch  TypeDef patch describing change to TypeDef.
     * @return updated TypeDef
     * @throws FunctionNotSupportedException the repository does not support this call.
     */
    @Override
    public TypeDef updateTypeDef(String       userId,
                                 TypeDefPatch typeDefPatch) throws FunctionNotSupportedException
    {
        final String                       methodName = "updateTypeDef";

        throwNotEnterpriseFunction(methodName);

        return null;
    }


    /**
     * Delete the TypeDef.  This is only possible if the TypeDef has never been used to create instances or any
     * instances of this TypeDef have been purged from the metadata collection.
     *
     * @param userId  unique identifier for requesting user.
     * @param obsoleteTypeDefGUID  String unique identifier for the TypeDef.
     * @param obsoleteTypeDefName  String unique name for the TypeDef.
     * @throws FunctionNotSupportedException the repository does not support this call.
     */
    @Override
    public void deleteTypeDef(String    userId,
                              String    obsoleteTypeDefGUID,
                              String    obsoleteTypeDefName) throws FunctionNotSupportedException
    {
        final String methodName = "deleteTypeDef";

        throwNotEnterpriseFunction(methodName);
    }


    /**
     * Delete an AttributeTypeDef.  This is only possible if the AttributeTypeDef has never been used to create
     * instances or any instances of this AttributeTypeDef have been purged from the metadata collection.
     *
     * @param userId  unique identifier for requesting user.
     * @param obsoleteTypeDefGUID  String unique identifier for the AttributeTypeDef.
     * @param obsoleteTypeDefName  String unique name for the AttributeTypeDef.
     * @throws FunctionNotSupportedException the repository does not support this call.
     */
    @Override
    public void deleteAttributeTypeDef(String    userId,
                                       String    obsoleteTypeDefGUID,
                                       String    obsoleteTypeDefName) throws FunctionNotSupportedException
    {
        final String methodName = "deleteAttributeTypeDef";

        throwNotEnterpriseFunction(methodName);
    }


    /**
     * Change the guid or name of an existing TypeDef to a new value.  This is used if two different
     * TypeDefs are discovered to have the same guid.  This is extremely unlikely but not impossible so
     * the open metadata protocol has provision for this.
     *
     * @param userId  unique identifier for requesting user.
     * @param originalTypeDefGUID  the original guid of the TypeDef.
     * @param originalTypeDefName  the original name of the TypeDef.
     * @param newTypeDefGUID  the new identifier for the TypeDef.
     * @param newTypeDefName  new name for this TypeDef.
     * @return typeDef  new values for this TypeDef, including the new guid/name.
     * @throws FunctionNotSupportedException the repository does not support this call.
     */
    @Override
    public TypeDef reIdentifyTypeDef(String     userId,
                                     String     originalTypeDefGUID,
                                     String     originalTypeDefName,
                                     String     newTypeDefGUID,
                                     String     newTypeDefName) throws FunctionNotSupportedException
    {
        final String methodName = "reIdentifyTypeDef";

        throwNotEnterpriseFunction(methodName);

        return null;
    }


    /**
     * Change the guid or name of an existing TypeDef to a new value.  This is used if two different
     * TypeDefs are discovered to have the same guid.  This is extremely unlikely but not impossible so
     * the open metadata protocol has provision for this.
     *
     * @param userId  unique identifier for requesting user.
     * @param originalAttributeTypeDefGUID  the original guid of the AttributeTypeDef.
     * @param originalAttributeTypeDefName  the original name of the AttributeTypeDef.
     * @param newAttributeTypeDefGUID  the new identifier for the AttributeTypeDef.
     * @param newAttributeTypeDefName  new name for this AttributeTypeDef.
     * @return attributeTypeDef  new values for this AttributeTypeDef, including the new guid/name.
     * @throws FunctionNotSupportedException the repository does not support this call.
     */
    @Override
    public AttributeTypeDef reIdentifyAttributeTypeDef(String     userId,
                                                       String     originalAttributeTypeDefGUID,
                                                       String     originalAttributeTypeDefName,
                                                       String     newAttributeTypeDefGUID,
                                                       String     newAttributeTypeDefName) throws FunctionNotSupportedException
    {
        final String                       methodName = "reIdentifyAttributeTypeDef";

        throwNotEnterpriseFunction(methodName);

        return null;
    }


    /* ===================================================
     * Group 3: Locating entity and relationship instances
     */


    /**
     * Returns the entity if the entity is stored in the metadata collection, otherwise null.
     * Notice that entities in DELETED state are returned by this call.
     *
     * @param userId  unique identifier for requesting user.
     * @param guid  String unique identifier for the entity
     * @return the entity details if the entity is found in the metadata collection; otherwise return null
     * @throws InvalidParameterException the guid is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public EntityDetail isEntityKnown(String    userId,
                                      String    guid) throws InvalidParameterException,
                                                             RepositoryErrorException,
                                                             UserNotAuthorizedException
    {
        final String  methodName = "isEntityKnown";

        /*
         * Validate parameters
         */
        super.getInstanceParameterValidation(userId, guid, methodName);

        /*
         * Validation complete, ok to continue with request
         *
         * The list of cohort connectors are retrieved for each request to ensure that any changes in
         * the shape of the cohort are reflected immediately.
         */
        List<OMRSRepositoryConnector> cohortConnectors = enterpriseParentConnector.getCohortConnectors(methodName);

        FederationControl       federationControl = new ParallelFederationControl(userId, cohortConnectors, auditLog, methodName);
        GetEntityDetailExecutor executor          = new GetEntityDetailExecutor(userId, guid, auditLog, methodName);

        /*
         * Ready to process the request.  Create requests occur in the first repository that accepts the call.
         * Some repositories may produce exceptions.  These exceptions are saved and will be returned if
         * there are no positive results from any repository.
         */
        federationControl.executeCommand(executor);

        return executor.isEntityKnown(true);
    }


    /**
     * Return the header and classifications for a specific entity.  The returned entity summary may be from
     * a full entity object or an entity proxy.
     *
     * @param userId  unique identifier for requesting user.
     * @param guid  String unique identifier for the entity
     * @return EntitySummary structure
     * @throws InvalidParameterException the guid is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws EntityNotKnownException the requested entity instance is not known in the metadata collection.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public EntitySummary getEntitySummary(String    userId,
                                          String    guid) throws InvalidParameterException,
                                                                 RepositoryErrorException,
                                                                 EntityNotKnownException,
                                                                 UserNotAuthorizedException
    {
        final String  methodName = "getEntitySummary";

        /*
         * Validate parameters
         */
        super.getInstanceParameterValidation(userId, guid, methodName);

        /*
         * Validation complete, ok to continue with request
         *
         * The list of cohort connectors are retrieved for each request to ensure that any changes in
         * the shape of the cohort are reflected immediately.
         */
        List<OMRSRepositoryConnector> cohortConnectors = enterpriseParentConnector.getCohortConnectors(methodName);

        FederationControl        federationControl = new ParallelFederationControl(userId, cohortConnectors, auditLog, methodName);
        GetEntitySummaryExecutor executor          = new GetEntitySummaryExecutor(userId, guid, auditLog, methodName);

        /*
         * Ready to process the request.  Get requests occur in the first repository that accepts the call.
         * Some repositories may produce exceptions.  These exceptions are saved and will be returned if
         * there are no positive results from any repository.
         */
        federationControl.executeCommand(executor);

        return executor.getEntitySummary();
    }


    /**
     * Return the header, classifications and properties of a specific entity.
     *
     * @param userId  unique identifier for requesting user.
     * @param guid  String unique identifier for the entity.
     * @return EntityDetail structure.
     * @throws InvalidParameterException the guid is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                 the metadata collection is stored.
     * @throws EntityNotKnownException the requested entity instance is not known in the metadata collection.
     * @throws EntityProxyOnlyException the requested entity instance is only a proxy in the metadata collection.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public EntityDetail getEntityDetail(String    userId,
                                        String    guid) throws InvalidParameterException,
                                                               RepositoryErrorException,
                                                               EntityNotKnownException,
                                                               EntityProxyOnlyException,
                                                               UserNotAuthorizedException
    {
        final String  methodName        = "getEntityDetail";

        /*
         * Validate parameters
         */
        super.getInstanceParameterValidation(userId, guid, methodName);

        /*
         * Validation complete, ok to continue with request
         *
         * The list of cohort connectors are retrieved for each request to ensure that any changes in
         * the shape of the cohort are reflected immediately.
         */
        List<OMRSRepositoryConnector> cohortConnectors = enterpriseParentConnector.getCohortConnectors(methodName);

        FederationControl       federationControl = new ParallelFederationControl(userId, cohortConnectors, auditLog, methodName);
        GetEntityDetailExecutor executor          = new GetEntityDetailExecutor(userId, guid, auditLog, methodName);

        federationControl.executeCommand(executor);

        return executor.getEntityDetail();
    }


    /**
     * Return a historical version of an entity.  This includes the header, classifications and properties of the entity.
     *
     * @param userId unique identifier for requesting user.
     * @param guid  String unique identifier for the entity.
     * @param asOfTime  the time used to determine which version of the entity that is desired.
     * @return EntityDetail structure.
     * @throws InvalidParameterException the guid or date is null or the date is for a future time.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                 the metadata collection is stored.
     * @throws EntityNotKnownException the requested entity instance is not known in the metadata collection
     *                                   at the time requested.
     * @throws EntityProxyOnlyException the requested entity instance is only a proxy in the metadata collection.
     * @throws FunctionNotSupportedException the repository does not support the asOfTime parameter.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public EntityDetail getEntityDetail(String    userId,
                                        String    guid,
                                        Date      asOfTime) throws InvalidParameterException,
                                                                   RepositoryErrorException,
                                                                   EntityNotKnownException,
                                                                   EntityProxyOnlyException,
                                                                   FunctionNotSupportedException,
                                                                   UserNotAuthorizedException
    {
        final String  methodName        = "getEntityDetail";

        /*
         * Validate parameters
         */
        super.getInstanceParameterValidation(userId, guid, asOfTime, methodName);

        /*
         * Validation complete, ok to continue with request
         *
         * The list of cohort connectors are retrieved for each request to ensure that any changes in
         * the shape of the cohort are reflected immediately.
         */
        List<OMRSRepositoryConnector> cohortConnectors = enterpriseParentConnector.getCohortConnectors(methodName);

        FederationControl       federationControl = new ParallelFederationControl(userId, cohortConnectors, auditLog, methodName);
        GetEntityDetailExecutor executor          = new GetEntityDetailExecutor(userId, guid, asOfTime, auditLog, methodName);

        /*
         * Ready to process the request.  Callers to the enterprise repository are typically well-defined and only request entities that
         * are known.  The loop below assumes that the entity is not returned because a repository is not currently registered.
         * Therefore, the enterprise connector will retry the request five times to give the owning repository time to register.
         */
        int retryCount = 0;

        while (retryCount < 4)
        {
            try
            {
                federationControl.executeCommand(executor);

                return executor.getEntityDetail();
            }
            catch (EntityProxyOnlyException | EntityNotKnownException proxyException)
            {
                cohortConnectors = enterpriseParentConnector.getCohortConnectors(methodName);

                federationControl = new ParallelFederationControl(userId, cohortConnectors, auditLog, methodName);
                executor          = new GetEntityDetailExecutor(userId, guid, asOfTime, auditLog, methodName);

                retryCount ++;
                auditLog.logMessage(methodName, OMRSAuditCode.RETRY_FOR_PROXY.getMessageDefinition(guid, userId, Integer.toString(retryCount)));
            }
        }

        federationControl.executeCommand(executor);

        return executor.getEntityDetailAsOfTime();
    }


    /**
     * Return all historical versions of an entity within the bounds of the provided timestamps. To retrieve all historical
     * versions of an entity, set both the 'fromTime' and 'toTime' to null.
     *
     * @param userId unique identifier for requesting user.
     * @param guid String unique identifier for the entity.
     * @param fromTime the earliest point in time from which to retrieve historical versions of the entity (inclusive)
     * @param toTime the latest point in time from which to retrieve historical versions of the entity (exclusive)
     * @param startFromElement the starting element number of the historical versions to return. This is used when retrieving
     *                         versions beyond the first page of results. Zero means start from the first element.
     * @param pageSize the maximum number of result versions that can be returned on this request. Zero means unrestricted
     *                 return results size.
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @return {@code List<EntityDetail>} of each historical version of the entity detail within the bounds, and in the order requested.
     * @throws InvalidParameterException the guid or date is null or fromTime is after the toTime
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                 the metadata collection is stored.
     * @throws EntityNotKnownException the requested entity instance is not known in the metadata collection at the time requested.
     * @throws EntityProxyOnlyException the requested entity instance is only a proxy in the metadata collection.
     * @throws FunctionNotSupportedException the repository does not support history.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public List<EntityDetail> getEntityDetailHistory(String                 userId,
                                                     String                 guid,
                                                     Date                   fromTime,
                                                     Date                   toTime,
                                                     int                    startFromElement,
                                                     int                    pageSize,
                                                     HistorySequencingOrder sequencingOrder) throws InvalidParameterException,
                                                                                                    RepositoryErrorException,
                                                                                                    EntityNotKnownException,
                                                                                                    EntityProxyOnlyException,
                                                                                                    FunctionNotSupportedException,
                                                                                                    UserNotAuthorizedException
    {
        final String  methodName = "getEntityDetailHistory";

        /*
         * Validate parameters
         */
        this.getInstanceHistoryParameterValidation(userId, guid, fromTime, toTime, methodName);

        /*
         * Validation complete, ok to continue with request
         *
         * The list of cohort connectors are retrieved for each request to ensure that any changes in
         * the shape of the cohort are reflected immediately.
         */
        List<OMRSRepositoryConnector> cohortConnectors = enterpriseParentConnector.getCohortConnectors(methodName);

        FederationControl              federationControl = new ParallelFederationControl(userId, cohortConnectors, auditLog, methodName);
        GetEntityDetailHistoryExecutor executor          = new GetEntityDetailHistoryExecutor(userId,
                                                                                              guid,
                                                                                              fromTime,
                                                                                              toTime,
                                                                                              startFromElement,
                                                                                              pageSize,
                                                                                              sequencingOrder,
                                                                                              localMetadataCollectionId,
                                                                                              auditLog,
                                                                                              repositoryValidator,
                                                                                              methodName);

        /*
         * Ready to process the request.  Create requests occur in the first repository that accepts the call.
         * Some repositories may produce exceptions.  These exceptions are saved and will be returned if
         * there are no positive results from any repository.
         */
        federationControl.executeCommand(executor);

        return executor.getHistoryResults(enterpriseParentConnector, this);
    }


    /**
     * Return all historical versions of an entity's classification within the bounds of the provided timestamps.
     * To retrieve all historical versions of an entity's classification, set both the 'fromTime' and 'toTime' to null.
     *
     * @param userId unique identifier for requesting user.
     * @param guid String unique identifier for the entity.
     * @param classificationName name of the classification within entity
     * @param fromTime the earliest point in time from which to retrieve historical versions of the entity (inclusive)
     * @param toTime the latest point in time from which to retrieve historical versions of the entity (exclusive)
     * @param startFromElement the starting element number of the historical versions to return. This is used when retrieving
     *                         versions beyond the first page of results. Zero means start from the first element.
     * @param pageSize the maximum number of result versions that can be returned on this request. Zero means unrestricted
     *                 return results size.
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @return {@code List<Classification>} of each historical version of the entity's classification within the bounds, and in the order requested.
     * @throws InvalidParameterException the guid or date is null or fromTime is after the toTime
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where the metadata collection is stored.
     * @throws EntityNotKnownException the requested entity instance is not active in the metadata collection at the time requested.
     * @throws EntityProxyOnlyException the requested entity instance is only a proxy in the metadata collection.
     * @throws FunctionNotSupportedException the repository does not support history.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public List<Classification> getClassificationHistory(String                 userId,
                                                         String                 guid,
                                                         String                 classificationName,
                                                         Date                   fromTime,
                                                         Date                   toTime,
                                                         int                    startFromElement,
                                                         int                    pageSize,
                                                         HistorySequencingOrder sequencingOrder) throws InvalidParameterException,
                                                                                                        RepositoryErrorException,
                                                                                                        EntityNotKnownException,
                                                                                                        EntityProxyOnlyException,
                                                                                                        FunctionNotSupportedException,
                                                                                                        UserNotAuthorizedException
    {
        final String  methodName = "getClassificationHistory";

        /*
         * Validate parameters
         */
        this.getInstanceHistoryParameterValidation(userId, classificationName, guid, fromTime, toTime, methodName);

        /*
         * Validation complete, ok to continue with request
         *
         * The list of cohort connectors are retrieved for each request to ensure that any changes in
         * the shape of the cohort are reflected immediately.
         */
        List<OMRSRepositoryConnector> cohortConnectors = enterpriseParentConnector.getCohortConnectors(methodName);

        FederationControl                federationControl = new ParallelFederationControl(userId, cohortConnectors, auditLog, methodName);
        GetClassificationHistoryExecutor executor          = new GetClassificationHistoryExecutor(userId,
                                                                                                  guid,
                                                                                                  classificationName,
                                                                                                  fromTime,
                                                                                                  toTime,
                                                                                                  startFromElement,
                                                                                                  pageSize,
                                                                                                  sequencingOrder,
                                                                                                  localMetadataCollectionId,
                                                                                                  auditLog,
                                                                                                  repositoryValidator,
                                                                                                  methodName);

        /*
         * Ready to process the request.  Create requests occur in the first repository that accepts the call.
         * Some repositories may produce exceptions.  These exceptions are saved and will be returned if
         * there are no positive results from any repository.
         */
        federationControl.executeCommand(executor);

        return executor.getHistoryResults(this);
    }


    /**
     * Return the relationships for a specific entity.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID String unique identifier for the entity.
     * @param relationshipTypeGUID String GUID of the type of relationship required (null for all).
     * @param fromRelationshipElement the starting element number of the relationships to return.
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
    public List<Relationship> getRelationshipsForEntity(String               userId,
                                                        String               entityGUID,
                                                        String               relationshipTypeGUID,
                                                        int                  fromRelationshipElement,
                                                        List<InstanceStatus> limitResultsByStatus,
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
        final String  methodName        = "getRelationshipsForEntity";

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
         * Validation complete, ok to continue with request
         *
         * The list of cohort connectors are retrieved for each request to ensure that any changes in
         * the shape of the cohort are reflected immediately.
         */
        List<OMRSRepositoryConnector> cohortConnectors = enterpriseParentConnector.getCohortConnectors(methodName);

        FederationControl                 federationControl = new ParallelFederationControl(userId, cohortConnectors, auditLog, methodName);
        GetRelationshipsForEntityExecutor executor          = new GetRelationshipsForEntityExecutor(userId,
                                                                                                    entityGUID,
                                                                                                    relationshipTypeGUID,
                                                                                                    fromRelationshipElement,
                                                                                                    limitResultsByStatus,
                                                                                                    asOfTime,
                                                                                                    sequencingProperty,
                                                                                                    sequencingOrder,
                                                                                                    pageSize,
                                                                                                    localMetadataCollectionId,
                                                                                                    auditLog,
                                                                                                    repositoryValidator,
                                                                                                    methodName);

        /*
         * Ready to process the request.  Create requests occur in the first repository that accepts the call.
         * Some repositories may produce exceptions.  These exceptions are saved and will be returned if
         * there are no positive results from any repository.
         */
        federationControl.executeCommand(executor);

        List<Relationship> results = executor.getResults(enterpriseParentConnector);

        if ((results == null) || (results.isEmpty()))
        {
            /*
             * This could be either that the entity exists with no relationships, or the entity GUID is invalid.
             * The call below checks that the entityGUID is valid.  The check is done at the end rather than before
             * retrieving relationships so that it is avoided if there are relationships to return.
             */
            this.isEntityKnown(userId, entityGUID);
            results = null;
        }

        return results;
    }


    /**
     * Return a list of entities that match the supplied properties according to the match criteria.  The results
     * can be returned over many pages.
     *
     * @param userId unique identifier for requesting user.
     * @param entityTypeGUID String unique identifier for the entity type of interest (null means any entity type).
     * @param matchProperties Optional list of entity properties to match (where any String property's value should
     *                        be defined as a Java regular expression, even if it should be an exact match).
     * @param matchCriteria Enum defining how the match properties should be matched to the entities in the repository.
     * @param fromEntityElement the starting element number of the entities to return.
     *                                This is used when retrieving elements
     *                                beyond the first page of results. Zero means start from the first element.
     * @param limitResultsByStatus By default, entities in all statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                             status values.
     * @param limitResultsByClassification List of classifications that must be present on all returned entities.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty String name of the entity property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
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
     * @throws PropertyErrorException the properties specified are not valid for any of the requested types of
     *                                  entity.
     * @throws PagingErrorException the paging/sequencing parameters are set up incorrectly.
     * @throws FunctionNotSupportedException the repository does not support one of the provided parameters.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     * @see OMRSRepositoryHelper#getExactMatchRegex(String)
     */
    @Override
    public List<EntityDetail> findEntitiesByProperty(String               userId,
                                                     String               entityTypeGUID,
                                                     InstanceProperties   matchProperties,
                                                     MatchCriteria        matchCriteria,
                                                     int                  fromEntityElement,
                                                     List<InstanceStatus> limitResultsByStatus,
                                                     List<String>         limitResultsByClassification,
                                                     Date                 asOfTime,
                                                     String               sequencingProperty,
                                                     SequencingOrder      sequencingOrder,
                                                     int                  pageSize) throws InvalidParameterException,
                                                                                           RepositoryErrorException,
                                                                                           TypeErrorException,
                                                                                           PropertyErrorException,
                                                                                           PagingErrorException,
                                                                                           FunctionNotSupportedException,
                                                                                           UserNotAuthorizedException
    {
        final String methodName = "findEntitiesByProperty";

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

        /*
         * Validation complete, ok to continue with request
         *
         * The list of cohort connectors are retrieved for each request to ensure that any changes in
         * the shape of the cohort are reflected immediately.
         */
        List<OMRSRepositoryConnector> cohortConnectors = enterpriseParentConnector.getCohortConnectors(methodName);

        FederationControl              federationControl = new ParallelFederationControl(userId, cohortConnectors, auditLog, methodName);
        FindEntitiesByPropertyExecutor executor          = new FindEntitiesByPropertyExecutor(userId,
                                                                                              entityTypeGUID,
                                                                                              matchProperties,
                                                                                              matchCriteria,
                                                                                              fromEntityElement,
                                                                                              limitResultsByStatus,
                                                                                              limitResultsByClassification,
                                                                                              asOfTime,
                                                                                              sequencingProperty,
                                                                                              sequencingOrder,
                                                                                              pageSize,
                                                                                              localMetadataCollectionId,
                                                                                              auditLog,
                                                                                              repositoryValidator,
                                                                                              methodName);

        /*
         * Ready to process the request.  Create requests occur in the first repository that accepts the call.
         * Some repositories may produce exceptions.  These exceptions are saved and will be returned if
         * there are no positive results from any repository.
         */
        federationControl.executeCommand(executor);

        return executor.getResults(enterpriseParentConnector, this);
    }


    /**
     * Return a list of entities that match the supplied criteria.  The results can be returned over many pages.
     *
     * @param userId unique identifier for requesting user.
     * @param entityTypeGUID String unique identifier for the entity type of interest (null means any entity type).
     * @param entitySubtypeGUIDs optional list of the unique identifiers (guids) for subtypes of the entityTypeGUID to
     *                           include in the search results. Null means all subtypes.
     * @param matchProperties Optional list of entity property conditions to match.
     * @param fromEntityElement the starting element number of the entities to return.
     *                                This is used when retrieving elements
     *                                beyond the first page of results. Zero means start from the first element.
     * @param limitResultsByStatus By default, entities in all statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                             status values.
     * @param matchClassifications Optional list of entity classifications to match.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty String name of the entity property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
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
     * @throws PropertyErrorException the properties specified are not valid for any of the requested types of
     *                                  entity.
     * @throws PagingErrorException the paging/sequencing parameters are set up incorrectly.
     * @throws FunctionNotSupportedException the repository does not support this optional method.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public List<EntityDetail> findEntities(String                userId,
                                           String                entityTypeGUID,
                                           List<String>          entitySubtypeGUIDs,
                                           SearchProperties      matchProperties,
                                           int                   fromEntityElement,
                                           List<InstanceStatus>  limitResultsByStatus,
                                           SearchClassifications matchClassifications,
                                           Date                  asOfTime,
                                           String                sequencingProperty,
                                           SequencingOrder       sequencingOrder,
                                           int                   pageSize) throws InvalidParameterException,
                                                                                  RepositoryErrorException,
                                                                                  TypeErrorException,
                                                                                  PropertyErrorException,
                                                                                  PagingErrorException,
                                                                                  FunctionNotSupportedException,
                                                                                  UserNotAuthorizedException
    {
        final String  methodName = "findEntities";

        /*
         * Validate parameters
         */
        super.findEntitiesParameterValidation(userId,
                                              entityTypeGUID,
                                              entitySubtypeGUIDs,
                                              matchProperties,
                                              fromEntityElement,
                                              limitResultsByStatus,
                                              matchClassifications,
                                              asOfTime,
                                              sequencingProperty,
                                              sequencingOrder,
                                              pageSize);

        /*
         * Validation complete, ok to continue with request
         *
         * The list of cohort connectors are retrieved for each request to ensure that any changes in
         * the shape of the cohort are reflected immediately.
         */
        List<OMRSRepositoryConnector> cohortConnectors = enterpriseParentConnector.getCohortConnectors(methodName);

        FederationControl    federationControl = new ParallelFederationControl(userId, cohortConnectors, auditLog, methodName);
        FindEntitiesExecutor executor          = new FindEntitiesExecutor(userId,
                                                                          entityTypeGUID,
                                                                          entitySubtypeGUIDs,
                                                                          matchProperties,
                                                                          fromEntityElement,
                                                                          limitResultsByStatus,
                                                                          matchClassifications,
                                                                          asOfTime,
                                                                          sequencingProperty,
                                                                          sequencingOrder,
                                                                          pageSize,
                                                                          localMetadataCollectionId,
                                                                          auditLog,
                                                                          repositoryValidator,
                                                                          methodName);

        /*
         * Ready to process the request.  Create requests occur in the first repository that accepts the call.
         * Some repositories may produce exceptions.  These exceptions are saved and will be returned if
         * there are no positive results from any repository.
         */
        federationControl.executeCommand(executor);

        return executor.getResults(enterpriseParentConnector, this);
    }


    /**
     * Return a list of entities that have the requested type of classifications attached.
     *
     * @param userId unique identifier for requesting user.
     * @param entityTypeGUID unique identifier for the type of entity requested.  Null means any type of entity
     *                       (but could be slow so not recommended).
     * @param classificationName name of the classification, note a null is not valid.
     * @param matchClassificationProperties list of classification properties used to narrow the search (where any String
     *                                      property's value should be defined as a Java regular expression, even if it
     *                                      should be an exact match).
     * @param matchCriteria Enum defining how the match properties should be matched to the classifications in the repository.
     * @param fromEntityElement the starting element number of the entities to return.
     *                                This is used when retrieving elements
     *                                beyond the first page of results. Zero means start from the first element.
     * @param limitResultsByStatus By default, entities in all statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                             status values.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty String name of the entity property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
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
     * @throws ClassificationErrorException the classification request is not known to the metadata collection.
     * @throws PropertyErrorException the properties specified are not valid for the requested type of
     *                                  classification.
     * @throws PagingErrorException the paging/sequencing parameters are set up incorrectly.
     * @throws FunctionNotSupportedException the repository does not support one of the provided parameters.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     * @see OMRSRepositoryHelper#getExactMatchRegex(String)
     */
    @Override
    public  List<EntityDetail> findEntitiesByClassification(String               userId,
                                                            String               entityTypeGUID,
                                                            String               classificationName,
                                                            InstanceProperties   matchClassificationProperties,
                                                            MatchCriteria        matchCriteria,
                                                            int                  fromEntityElement,
                                                            List<InstanceStatus> limitResultsByStatus,
                                                            Date                 asOfTime,
                                                            String               sequencingProperty,
                                                            SequencingOrder      sequencingOrder,
                                                            int                  pageSize) throws InvalidParameterException,
                                                                                                  TypeErrorException,
                                                                                                  RepositoryErrorException,
                                                                                                  ClassificationErrorException,
                                                                                                  PropertyErrorException,
                                                                                                  PagingErrorException,
                                                                                                  FunctionNotSupportedException,
                                                                                                  UserNotAuthorizedException
    {
        final String methodName = "findEntitiesByClassification";

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


        /*
         * Validation complete, ok to continue with request
         *
         * The list of cohort connectors are retrieved for each request to ensure that any changes in
         * the shape of the cohort are reflected immediately.
         */
        List<OMRSRepositoryConnector> cohortConnectors = enterpriseParentConnector.getCohortConnectors(methodName);

        FederationControl                    federationControl = new ParallelFederationControl(userId, cohortConnectors, auditLog, methodName);
        FindEntitiesByClassificationExecutor executor          = new FindEntitiesByClassificationExecutor(userId,
                                                                                                          entityTypeGUID,
                                                                                                          classificationName,
                                                                                                          matchClassificationProperties,
                                                                                                          matchCriteria,
                                                                                                          fromEntityElement,
                                                                                                          limitResultsByStatus,
                                                                                                          asOfTime,
                                                                                                          sequencingProperty,
                                                                                                          sequencingOrder,
                                                                                                          pageSize,
                                                                                                          localMetadataCollectionId,
                                                                                                          auditLog,
                                                                                                          repositoryValidator,
                                                                                                          methodName);

        /*
         * Ready to process the request.  Create requests occur in the first repository that accepts the call.
         * Some repositories may produce exceptions.  These exceptions are saved and will be returned if
         * there are no positive results from any repository.
         */
        federationControl.executeCommand(executor);

        return executor.getResults(enterpriseParentConnector, this);
    }


    /**
     * Return a list of entities whose string based property values match the search criteria.  The
     * search criteria may include regex style wild cards.
     *
     * @param userId unique identifier for requesting user.
     * @param entityTypeGUID GUID of the type of entity to search for. Null means all types will
     *                       be searched (could be slow so not recommended).
     * @param searchCriteria String Java regular expression used to match against any of the String property values
     *                       within entity instances of the specified type(s).
     *                       This parameter must not be null.
     * @param fromEntityElement the starting element number of the entities to return.
     *                                This is used when retrieving elements
     *                                beyond the first page of results. Zero means start from the first element.
     * @param limitResultsByStatus By default, entities in all statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                             status values.
     * @param limitResultsByClassification List of classifications that must be present on all returned entities.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param pageSize the maximum number of result entities that can be returned on this request.  Zero means
     *                 unrestricted return results size.
     * @return a list of entities matching the supplied criteria null means no matching entities in the metadata
     * collection.
     * @throws InvalidParameterException a parameter is invalid or null.
     * @throws TypeErrorException the type guid passed on the request is not known by the
     *                              metadata collection.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws PropertyErrorException the sequencing property specified is not valid for any of the requested types of
     *                                  entity.
     * @throws PagingErrorException the paging/sequencing parameters are set up incorrectly.
     * @throws FunctionNotSupportedException the repository does not support one of the provided parameters.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     * @see OMRSRepositoryHelper#getExactMatchRegex(String)
     * @see OMRSRepositoryHelper#getContainsRegex(String)
     */
    @Override
    public List<EntityDetail> findEntitiesByPropertyValue(String               userId,
                                                          String               entityTypeGUID,
                                                          String               searchCriteria,
                                                          int                  fromEntityElement,
                                                          List<InstanceStatus> limitResultsByStatus,
                                                          List<String>         limitResultsByClassification,
                                                          Date                 asOfTime,
                                                          String               sequencingProperty,
                                                          SequencingOrder      sequencingOrder,
                                                          int                  pageSize) throws InvalidParameterException,
                                                                                                TypeErrorException,
                                                                                                RepositoryErrorException,
                                                                                                PropertyErrorException,
                                                                                                PagingErrorException,
                                                                                                FunctionNotSupportedException,
                                                                                                UserNotAuthorizedException
    {
        final String  methodName = "findEntitiesByPropertyValue";

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

        /*
         * Validation complete, ok to continue with request
         *
         * The list of cohort connectors are retrieved for each request to ensure that any changes in
         * the shape of the cohort are reflected immediately.
         */
        List<OMRSRepositoryConnector> cohortConnectors = enterpriseParentConnector.getCohortConnectors(methodName);

        FederationControl                   federationControl = new ParallelFederationControl(userId, cohortConnectors, auditLog, methodName);
        FindEntitiesByPropertyValueExecutor executor          = new FindEntitiesByPropertyValueExecutor(userId,
                                                                                                        entityTypeGUID,
                                                                                                        searchCriteria,
                                                                                                        fromEntityElement,
                                                                                                        limitResultsByStatus,
                                                                                                        limitResultsByClassification,
                                                                                                        asOfTime,
                                                                                                        sequencingProperty,
                                                                                                        sequencingOrder,
                                                                                                        pageSize,
                                                                                                        localMetadataCollectionId,
                                                                                                        auditLog,
                                                                                                        repositoryValidator,
                                                                                                        methodName);

        /*
         * Ready to process the request.  Create requests occur in the first repository that accepts the call.
         * Some repositories may produce exceptions.  These exceptions are saved and will be returned if
         * there are no positive results from any repository.
         */
        federationControl.executeCommand(executor);

        return executor.getResults(enterpriseParentConnector, this);
    }


    /**
     * Returns a relationship indicating if the relationship is stored in the metadata collection.
     * Notice that relationships in DELETED state are returned by this call.
     *
     * @param userId unique identifier for requesting user.
     * @param guid String unique identifier for the relationship
     * @return relationship details if the relationship is found in the metadata collection; otherwise return null
     * @throws InvalidParameterException the guid is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public Relationship isRelationshipKnown(String    userId,
                                            String    guid) throws InvalidParameterException,
                                                                   RepositoryErrorException,
                                                                   UserNotAuthorizedException
    {
        final String  methodName = "isRelationshipKnown";

        /*
         * Validate parameters
         */
        super.getInstanceParameterValidation(userId, guid, methodName);

        /*
         * Validation complete, ok to continue with request
         *
         * The list of cohort connectors are retrieved for each request to ensure that any changes in
         * the shape of the cohort are reflected immediately.
         */
        List<OMRSRepositoryConnector> cohortConnectors = enterpriseParentConnector.getCohortConnectors(methodName);

        FederationControl       federationControl = new SequentialFederationControl(userId, cohortConnectors, auditLog, methodName);
        GetRelationshipExecutor executor          = new GetRelationshipExecutor(userId, guid, auditLog, methodName);

        /*
         * Ready to process the request.  Create requests occur in the first repository that accepts the call.
         * Some repositories may produce exceptions.  These exceptions are saved and will be returned if
         * there are no positive results from any repository.
         */
        federationControl.executeCommand(executor);

        return executor.isRelationshipKnown();
    }


    /**
     * Return a requested relationship.
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

        /*
         * Validate parameters
         */
        super.getInstanceParameterValidation(userId, guid, methodName);

        /*
         * Validation complete, ok to continue with request
         *
         * The list of cohort connectors are retrieved for each request to ensure that any changes in
         * the shape of the cohort are reflected immediately.
         */
        List<OMRSRepositoryConnector> cohortConnectors = enterpriseParentConnector.getCohortConnectors(methodName);

        FederationControl       federationControl = new SequentialFederationControl(userId, cohortConnectors, auditLog, methodName);
        GetRelationshipExecutor executor          = new GetRelationshipExecutor(userId, guid, auditLog, methodName);

        /*
         * Ready to process the request.  Create requests occur in the first repository that accepts the call.
         * Some repositories may produce exceptions.  These exceptions are saved and will be returned if
         * there are no positive results from any repository.
         */
        federationControl.executeCommand(executor);

        return executor.getRelationship();
    }


    /**
     * Return a historical version of a relationship.
     *
     * @param userId unique identifier for requesting user.
     * @param guid String unique identifier for the relationship.
     * @param asOfTime the time used to determine which version of the entity that is desired.
     * @return Relationship structure.
     * @throws InvalidParameterException the guid or date is null or the date is for a future time.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                 the metadata collection is stored.
     * @throws RelationshipNotKnownException the requested entity instance is not known in the metadata collection
     *                                   at the time requested.
     * @throws FunctionNotSupportedException the repository does not support the asOfTime parameter.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public Relationship getRelationship(String    userId,
                                        String    guid,
                                        Date      asOfTime) throws InvalidParameterException,
                                                                   RepositoryErrorException,
                                                                   RelationshipNotKnownException,
                                                                   FunctionNotSupportedException,
                                                                   UserNotAuthorizedException
    {
        final String  methodName = "getRelationship";

        /*
         * Validate parameters
         */
        super.getInstanceParameterValidation(userId, guid, asOfTime, methodName);

        /*
         * Validation complete, ok to continue with request
         *
         * The list of cohort connectors are retrieved for each request to ensure that any changes in
         * the shape of the cohort are reflected immediately.
         */
        List<OMRSRepositoryConnector> cohortConnectors = enterpriseParentConnector.getCohortConnectors(methodName);

        FederationControl       federationControl = new SequentialFederationControl(userId, cohortConnectors, auditLog, methodName);
        GetRelationshipExecutor executor          = new GetRelationshipExecutor(userId, guid, asOfTime, auditLog, methodName);

        /*
         * Ready to process the request.  Create requests occur in the first repository that accepts the call.
         * Some repositories may produce exceptions.  These exceptions are saved and will be returned if
         * there are no positive results from any repository.
         */
        federationControl.executeCommand(executor);

        return executor.getRelationshipAsOfTime();
    }


    /**
     * Return all historical versions of a relationship within the bounds of the provided timestamps. To retrieve all
     * historical versions of a relationship, set both the 'fromTime' and 'toTime' to null.
     *
     * @param userId unique identifier for requesting user.
     * @param guid String unique identifier for the entity.
     * @param fromTime the earliest point in time from which to retrieve historical versions of the entity (inclusive)
     * @param toTime the latest point in time from which to retrieve historical versions of the entity (exclusive)
     * @param startFromElement the starting element number of the historical versions to return. This is used when retrieving
     *                         versions beyond the first page of results. Zero means start from the first element.
     * @param pageSize the maximum number of result versions that can be returned on this request. Zero means unrestricted
     *                 return results size.
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @return {@code List<Relationship>} of each historical version of the relationship within the bounds, and in the order requested.
     * @throws InvalidParameterException the guid or date is null or fromTime is after the toTime
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                 the metadata collection is stored.
     * @throws RelationshipNotKnownException the requested relationship instance is not known in the metadata collection
     *                                       at the time requested.
     * @throws FunctionNotSupportedException the repository does not support history.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public List<Relationship> getRelationshipHistory(String                 userId,
                                                     String                 guid,
                                                     Date                   fromTime,
                                                     Date                   toTime,
                                                     int                    startFromElement,
                                                     int                    pageSize,
                                                     HistorySequencingOrder sequencingOrder) throws InvalidParameterException,
                                                                                                    RepositoryErrorException,
                                                                                                    RelationshipNotKnownException,
                                                                                                    FunctionNotSupportedException,
                                                                                                    UserNotAuthorizedException
    {
        final String  methodName = "getRelationshipHistory";

        /*
         * Validate parameters
         */
        this.getInstanceHistoryParameterValidation(userId, guid, fromTime, toTime, methodName);

        /*
         * Validation complete, ok to continue with request
         *
         * The list of cohort connectors are retrieved for each request to ensure that any changes in
         * the shape of the cohort are reflected immediately.
         */
        List<OMRSRepositoryConnector> cohortConnectors = enterpriseParentConnector.getCohortConnectors(methodName);

        FederationControl              federationControl = new ParallelFederationControl(userId, cohortConnectors, auditLog, methodName);
        GetRelationshipHistoryExecutor executor          = new GetRelationshipHistoryExecutor(userId,
                                                                                              guid,
                                                                                              fromTime,
                                                                                              toTime,
                                                                                              startFromElement,
                                                                                              pageSize,
                                                                                              sequencingOrder,
                                                                                              localMetadataCollectionId,
                                                                                              auditLog,
                                                                                              repositoryValidator,
                                                                                              methodName);

        /*
         * Ready to process the request.  Create requests occur in the first repository that accepts the call.
         * Some repositories may produce exceptions.  These exceptions are saved and will be returned if
         * there are no positive results from any repository.
         */
        federationControl.executeCommand(executor);

        return executor.getHistoryResults(enterpriseParentConnector);
    }


    /**
     * Return a list of relationships that match the requested conditions.  The results can be received as a series of
     * pages.
     *
     * @param userId unique identifier for requesting user.
     * @param relationshipTypeGUID unique identifier (guid) for the relationship's type.  Null means all types
     *                             (but may be slow so not recommended).
     * @param relationshipSubtypeGUIDs optional list of the unique identifiers (guids) for subtypes of the
     *                                 relationshipTypeGUID to include in the search results. Null means all subtypes.
     * @param matchProperties Optional list of relationship property conditions to match.
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
     * @throws FunctionNotSupportedException the repository does not support one of the provided parameters.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     * @see OMRSRepositoryHelper#getExactMatchRegex(String)
     */
    @Override
    public  List<Relationship> findRelationships(String               userId,
                                                 String               relationshipTypeGUID,
                                                 List<String>         relationshipSubtypeGUIDs,
                                                 SearchProperties     matchProperties,
                                                 int                  fromRelationshipElement,
                                                 List<InstanceStatus> limitResultsByStatus,
                                                 Date                 asOfTime,
                                                 String               sequencingProperty,
                                                 SequencingOrder      sequencingOrder,
                                                 int                  pageSize) throws InvalidParameterException,
                                                                                       TypeErrorException,
                                                                                       RepositoryErrorException,
                                                                                       PropertyErrorException,
                                                                                       PagingErrorException,
                                                                                       FunctionNotSupportedException,
                                                                                       UserNotAuthorizedException
    {
        final String  methodName = "findRelationships";

        /*
         * Validate parameters
         */
        super.findRelationshipsParameterValidation(userId,
                                                   relationshipTypeGUID,
                                                   relationshipSubtypeGUIDs,
                                                   matchProperties,
                                                   fromRelationshipElement,
                                                   limitResultsByStatus,
                                                   asOfTime,
                                                   sequencingProperty,
                                                   sequencingOrder,
                                                   pageSize);

        /*
         * Validation complete, ok to continue with request
         *
         * The list of cohort connectors are retrieved for each request to ensure that any changes in
         * the shape of the cohort are reflected immediately.
         */
        List<OMRSRepositoryConnector> cohortConnectors = enterpriseParentConnector.getCohortConnectors(methodName);

        FederationControl         federationControl = new ParallelFederationControl(userId, cohortConnectors, auditLog, methodName);
        FindRelationshipsExecutor executor          = new FindRelationshipsExecutor(userId,
                                                                                    relationshipTypeGUID,
                                                                                    relationshipSubtypeGUIDs,
                                                                                    matchProperties,
                                                                                    fromRelationshipElement,
                                                                                    limitResultsByStatus,
                                                                                    asOfTime,
                                                                                    sequencingProperty,
                                                                                    sequencingOrder,
                                                                                    pageSize,
                                                                                    localMetadataCollectionId,
                                                                                    auditLog,
                                                                                    repositoryValidator,
                                                                                    methodName);

        /*
         * Ready to process the request.  Create requests occur in the first repository that accepts the call.
         * Some repositories may produce exceptions.  These exceptions are saved and will be returned if
         * there are no positive results from any repository.
         */
        federationControl.executeCommand(executor);

        return executor.getResults(enterpriseParentConnector);
    }


    /**
     * Return a list of relationships that match the requested properties by the matching criteria.   The results
     * can be received as a series of pages.
     *
     * @param userId unique identifier for requesting user.
     * @param relationshipTypeGUID unique identifier (guid) for the new relationship's type.  Null means all types
     *                             (but may be slow so not recommended).
     * @param matchProperties Optional list of relationship properties to match (where any String property's value should
     *                        be defined as a Java regular expression, even if it should be an exact match).
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
     * @throws FunctionNotSupportedException the repository does not support one of the provided parameters.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     * @see OMRSRepositoryHelper#getExactMatchRegex(String)
     */
    @Override
    public  List<Relationship> findRelationshipsByProperty(String               userId,
                                                           String               relationshipTypeGUID,
                                                           InstanceProperties   matchProperties,
                                                           MatchCriteria        matchCriteria,
                                                           int                  fromRelationshipElement,
                                                           List<InstanceStatus> limitResultsByStatus,
                                                           Date                 asOfTime,
                                                           String               sequencingProperty,
                                                           SequencingOrder      sequencingOrder,
                                                           int                  pageSize) throws InvalidParameterException,
                                                                                                 TypeErrorException,
                                                                                                 RepositoryErrorException,
                                                                                                 PropertyErrorException,
                                                                                                 PagingErrorException,
                                                                                                 FunctionNotSupportedException,
                                                                                                 UserNotAuthorizedException
    {
        final String  methodName = "findRelationshipsByProperty";

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

        /*
         * Validation complete, ok to continue with request
         *
         * The list of cohort connectors are retrieved for each request to ensure that any changes in
         * the shape of the cohort are reflected immediately.
         */
        List<OMRSRepositoryConnector> cohortConnectors = enterpriseParentConnector.getCohortConnectors(methodName);

        FederationControl                   federationControl = new ParallelFederationControl(userId, cohortConnectors, auditLog, methodName);
        FindRelationshipsByPropertyExecutor executor          = new FindRelationshipsByPropertyExecutor(userId,
                                                                                                        relationshipTypeGUID,
                                                                                                        matchProperties,
                                                                                                        matchCriteria,
                                                                                                        fromRelationshipElement,
                                                                                                        limitResultsByStatus,
                                                                                                        asOfTime,
                                                                                                        sequencingProperty,
                                                                                                        sequencingOrder,
                                                                                                        pageSize,
                                                                                                        localMetadataCollectionId,
                                                                                                        auditLog,
                                                                                                        repositoryValidator,
                                                                                                        methodName);

        /*
         * Ready to process the request.  Create requests occur in the first repository that accepts the call.
         * Some repositories may produce exceptions.  These exceptions are saved and will be returned if
         * there are no positive results from any repository.
         */
        federationControl.executeCommand(executor);

        return executor.getResults(enterpriseParentConnector);
    }



    /**
     * Return a list of relationships whose string based property values match the search criteria.  The
     * search criteria may include regex style wild cards.
     *
     * @param userId unique identifier for requesting user.
     * @param relationshipTypeGUID GUID of the type of entity to search for. Null means all types will
     *                       be searched (could be slow so not recommended).
     * @param searchCriteria String Java regular expression used to match against any of the String property values
     *                       within the relationship instances of the specified type(s).
     *                       This parameter must not be null.
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
     * @throws FunctionNotSupportedException the repository does not support one of the provided parameters.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     * @see OMRSRepositoryHelper#getExactMatchRegex(String)
     * @see OMRSRepositoryHelper#getContainsRegex(String)
     */
    @Override
    public  List<Relationship> findRelationshipsByPropertyValue(String               userId,
                                                                String               relationshipTypeGUID,
                                                                String               searchCriteria,
                                                                int                  fromRelationshipElement,
                                                                List<InstanceStatus> limitResultsByStatus,
                                                                Date                 asOfTime,
                                                                String               sequencingProperty,
                                                                SequencingOrder      sequencingOrder,
                                                                int                  pageSize) throws InvalidParameterException,
                                                                                                      TypeErrorException,
                                                                                                      RepositoryErrorException,
                                                                                                      PropertyErrorException,
                                                                                                      PagingErrorException,
                                                                                                      FunctionNotSupportedException,
                                                                                                      UserNotAuthorizedException
    {
        final String  methodName = "findRelationshipsByPropertyValue";

        /*
         * Validate parameters
         */
        this.findRelationshipsByPropertyValueParameterValidation(userId,
                                                                 relationshipTypeGUID,
                                                                 searchCriteria,
                                                                 fromRelationshipElement,
                                                                 limitResultsByStatus,
                                                                 asOfTime,
                                                                 sequencingProperty,
                                                                 sequencingOrder,
                                                                 pageSize);

        /*
         * Validation complete, ok to continue with request
         *
         * The list of cohort connectors are retrieved for each request to ensure that any changes in
         * the shape of the cohort are reflected immediately.
         */
        List<OMRSRepositoryConnector> cohortConnectors = enterpriseParentConnector.getCohortConnectors(methodName);

        FederationControl                        federationControl = new ParallelFederationControl(userId, cohortConnectors, auditLog, methodName);
        FindRelationshipsByPropertyValueExecutor executor          = new FindRelationshipsByPropertyValueExecutor(userId,
                                                                                                                  relationshipTypeGUID,
                                                                                                                  searchCriteria,
                                                                                                                  fromRelationshipElement,
                                                                                                                  limitResultsByStatus,
                                                                                                                  asOfTime,
                                                                                                                  sequencingProperty,
                                                                                                                  sequencingOrder,
                                                                                                                  pageSize,
                                                                                                                  localMetadataCollectionId,
                                                                                                                  auditLog,
                                                                                                                  repositoryValidator,
                                                                                                                  methodName);

        /*
         * Ready to process the request.  Create requests occur in the first repository that accepts the call.
         * Some repositories may produce exceptions.  These exceptions are saved and will be returned if
         * there are no positive results from any repository.
         */
        federationControl.executeCommand(executor);

        return executor.getResults(enterpriseParentConnector);
    }


    /**
     * Return all the relationships and intermediate entities that connect the startEntity with the endEntity.
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
    public InstanceGraph getLinkingEntities(String               userId,
                                            String               startEntityGUID,
                                            String               endEntityGUID,
                                            List<InstanceStatus> limitResultsByStatus,
                                            Date                 asOfTime) throws InvalidParameterException,
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
        super.getLinkingEntitiesParameterValidation(userId,
                                                    startEntityGUID,
                                                    endEntityGUID,
                                                    limitResultsByStatus,
                                                    asOfTime);

        /*
         * The list of cohort connectors are retrieved for each request to ensure that any changes in
         * the shape of the cohort are reflected immediately.
         */
        List<OMRSRepositoryConnector> cohortConnectors = enterpriseParentConnector.getCohortConnectors(methodName);


        /*
         * Ready to process the request.  Search results need to come from all members of the cohort.
         * They need to be combined and then duplicates removed to create the final list of results.
         * Some repositories may produce exceptions.  These exceptions are saved and one selected to
         * be returned if there are no results from any repository.
         */
        Map<String, EntityDetail> combinedEntityResults       = new HashMap<>();
        Map<String, Relationship> combinedRelationshipResults = new HashMap<>();

        EntityNotKnownException       entityNotKnownException       = null;
        FunctionNotSupportedException functionNotSupportedException = null;
        PropertyErrorException        propertyErrorException        = null;
        UserNotAuthorizedException    userNotAuthorizedException    = null;
        RepositoryErrorException      repositoryErrorException      = null;
        Exception                     anotherException              = null;

        /*
         * Loop through the metadata collections extracting the typedefs from each repository.
         */
        for (OMRSRepositoryConnector cohortConnector : cohortConnectors)
        {
            if (cohortConnector != null)
            {
                OMRSMetadataCollection metadataCollection = cohortConnector.getMetadataCollection();

                validateMetadataCollection(metadataCollection, methodName);

                try
                {
                    /*
                     * Issue the request
                     */
                    InstanceGraph results = metadataCollection.getLinkingEntities(userId,
                                                                                  startEntityGUID,
                                                                                  endEntityGUID,
                                                                                  limitResultsByStatus,
                                                                                  asOfTime);

                    /*
                     * Step through the list of returned TypeDefs and consolidate.
                     */
                    if (results != null)
                    {
                        combinedRelationshipResults = this.addUniqueRelationships(combinedRelationshipResults,
                                                                                  results.getRelationships(),
                                                                                  cohortConnector.getServerName(),
                                                                                  cohortConnector.getMetadataCollectionId(),
                                                                                  methodName);
                        combinedEntityResults = this.addUniqueEntities(combinedEntityResults,
                                                                       results.getEntities(),
                                                                       cohortConnector.getServerName(),
                                                                       cohortConnector.getMetadataCollectionId(),
                                                                       methodName);
                    }
                }
                catch (RepositoryErrorException error)
                {
                    repositoryErrorException = error;
                }
                catch (PropertyErrorException error)
                {
                    propertyErrorException = error;
                }
                catch (EntityNotKnownException error)
                {
                    entityNotKnownException = error;
                }
                catch (FunctionNotSupportedException error)
                {
                    functionNotSupportedException = error;
                }
                catch (UserNotAuthorizedException error)
                {
                    userNotAuthorizedException = error;
                }
                catch (Exception error)
                {
                    anotherException = error;
                }
            }
        }

        return validatedInstanceGraphResults(repositoryName,
                                             combinedEntityResults,
                                             combinedRelationshipResults,
                                             userNotAuthorizedException,
                                             propertyErrorException,
                                             functionNotSupportedException,
                                             entityNotKnownException,
                                             repositoryErrorException,
                                             anotherException,
                                             methodName);
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
     * @throws TypeErrorException one or more of the type guids passed on the request is not known by the
     *                              metadata collection.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by the entityGUID is not found in the metadata collection.
     * @throws PropertyErrorException there is a problem with one of the other parameters.
     * @throws FunctionNotSupportedException the repository does not support the asOfTime parameter.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public InstanceGraph getEntityNeighborhood(String               userId,
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
        final String methodName = "getEntityNeighborhood";

        /*
         * Validate parameters
         */
        super.getEntityNeighborhoodParameterValidation(userId,
                                                       entityGUID,
                                                       entityTypeGUIDs,
                                                       relationshipTypeGUIDs,
                                                       limitResultsByStatus,
                                                       limitResultsByClassification,
                                                       asOfTime,
                                                       level);

        /*
         * The list of cohort connectors are retrieved for each request to ensure that any changes in
         * the shape of the cohort are reflected immediately.
         */
        List<OMRSRepositoryConnector> cohortConnectors = enterpriseParentConnector.getCohortConnectors(methodName);

        /*
         * Ready to process the request.  Search results need to come from all members of the cohort.
         * They need to be combined and then duplicates removed to create the final list of results.
         * Some repositories may produce exceptions.  These exceptions are saved and one selected to
         * be returned if there are no results from any repository.
         */
        Map<String, EntityDetail> combinedEntityResults       = new HashMap<>();
        Map<String, Relationship> combinedRelationshipResults = new HashMap<>();

        EntityNotKnownException       entityNotKnownException       = null;
        FunctionNotSupportedException functionNotSupportedException = null;
        PropertyErrorException        propertyErrorException        = null;
        UserNotAuthorizedException    userNotAuthorizedException    = null;
        RepositoryErrorException      repositoryErrorException      = null;
        Exception                     anotherException              = null;

        /*
         * Loop through the metadata collections extracting the typedefs from each repository.
         */
        for (OMRSRepositoryConnector cohortConnector : cohortConnectors)
        {
            if (cohortConnector != null)
            {
                OMRSMetadataCollection metadataCollection = cohortConnector.getMetadataCollection();

                validateMetadataCollection(metadataCollection, methodName);

                try
                {
                    /*
                     * Issue the request
                     */
                    InstanceGraph results = metadataCollection.getEntityNeighborhood(userId,
                                                                                     entityGUID,
                                                                                     entityTypeGUIDs,
                                                                                     relationshipTypeGUIDs,
                                                                                     limitResultsByStatus,
                                                                                     limitResultsByClassification,
                                                                                     asOfTime,
                                                                                     level);

                    /*
                     * Step through the list of returned TypeDefs and consolidate.
                     */
                    if (results != null)
                    {
                        combinedRelationshipResults = this.addUniqueRelationships(combinedRelationshipResults,
                                                                                  results.getRelationships(),
                                                                                  cohortConnector.getServerName(),
                                                                                  cohortConnector.getMetadataCollectionId(),
                                                                                  methodName);
                        combinedEntityResults = this.addUniqueEntities(combinedEntityResults,
                                                                       results.getEntities(),
                                                                       cohortConnector.getServerName(),
                                                                       cohortConnector.getMetadataCollectionId(),
                                                                       methodName);
                    }
                }
                catch (RepositoryErrorException error)
                {
                    repositoryErrorException = error;
                }
                catch (PropertyErrorException error)
                {
                    propertyErrorException = error;
                }
                catch (EntityNotKnownException error)
                {
                    entityNotKnownException = error;
                }
                catch (FunctionNotSupportedException error)
                {
                    functionNotSupportedException = error;
                }
                catch (UserNotAuthorizedException error)
                {
                    userNotAuthorizedException = error;
                }
                catch (Exception error)
                {
                    anotherException = error;
                }
            }
        }

        return validatedInstanceGraphResults(repositoryName,
                                             combinedEntityResults,
                                             combinedRelationshipResults,
                                             userNotAuthorizedException,
                                             propertyErrorException,
                                             functionNotSupportedException,
                                             entityNotKnownException,
                                             repositoryErrorException,
                                             anotherException,
                                             methodName);
    }


    /**
     * Return the list of entities that are of the types listed in entityTypeGUIDs and are connected, either directly or
     * indirectly to the entity identified by startEntityGUID.
     *
     * @param userId unique identifier for requesting user.
     * @param startEntityGUID unique identifier of the starting entity
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
     * @throws TypeErrorException one or more of the type guids passed on the request is not known by the
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
        final String  typeGUIDParameterName  = "instanceTypes";
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
                repositoryValidator.validateTypeGUID(repositoryName, typeGUIDParameterName, guid, methodName);
            }
        }

        /*
         * Perform operation
         *
         * The list of cohort connectors are retrieved for each request to ensure that any changes in
         * the shape of the cohort are reflected immediately.
         */
        List<OMRSRepositoryConnector> cohortConnectors = enterpriseParentConnector.getCohortConnectors(methodName);

        /*
         * Ready to process the request.  Search results need to come from all members of the cohort.
         * They need to be combined and then duplicates removed to create the final list of results.
         * Some repositories may produce exceptions.  These exceptions are saved and one selected to
         * be returned if there are no results from any repository.
         */
        Map<String, EntityDetail> combinedResults = new HashMap<>();

        InvalidParameterException     invalidParameterException     = null;
        EntityNotKnownException       entityNotKnownException       = null;
        FunctionNotSupportedException functionNotSupportedException = null;
        TypeErrorException            typeErrorException            = null;
        PropertyErrorException        propertyErrorException        = null;
        UserNotAuthorizedException    userNotAuthorizedException    = null;
        RepositoryErrorException      repositoryErrorException      = null;
        Exception                     anotherException              = null;

        /*
         * Loop through the metadata collections extracting the typedefs from each repository.
         */
        for (OMRSRepositoryConnector cohortConnector : cohortConnectors)
        {
            if (cohortConnector != null)
            {
                OMRSMetadataCollection metadataCollection = cohortConnector.getMetadataCollection();

                validateMetadataCollection(metadataCollection, methodName);

                try
                {
                    /*
                     * Issue the request
                     */
                    List<EntityDetail> results = metadataCollection.getRelatedEntities(userId,
                                                                                       startEntityGUID,
                                                                                       entityTypeGUIDs,
                                                                                       fromEntityElement,
                                                                                       limitResultsByStatus,
                                                                                       limitResultsByClassification,
                                                                                       asOfTime,
                                                                                       sequencingProperty,
                                                                                       sequencingOrder,
                                                                                       pageSize);

                    /*
                     * Step through the list of returned TypeDefs and remove duplicates.
                     */
                    combinedResults = this.addUniqueEntities(combinedResults,
                                                             results,
                                                             cohortConnector.getServerName(),
                                                             cohortConnector.getMetadataCollectionId(),
                                                             methodName);
                }
                catch (EntityNotKnownException error)
                {
                    entityNotKnownException = error;
                }
                catch (FunctionNotSupportedException error)
                {
                    functionNotSupportedException = error;
                }
                catch (TypeErrorException error)
                {
                    typeErrorException = error;
                }
                catch (PropertyErrorException error)
                {
                    propertyErrorException = error;
                }
                catch (RepositoryErrorException error)
                {
                    repositoryErrorException = error;
                }
                catch (UserNotAuthorizedException error)
                {
                    userNotAuthorizedException = error;
                }
                catch (InvalidParameterException error)
                {
                    invalidParameterException = error;
                }
                catch (Exception error)
                {
                    anotherException = error;
                }
            }
        }


        if (combinedResults.isEmpty())
        {
            throwCapturedRepositoryErrorException(repositoryErrorException);
            throwCapturedUserNotAuthorizedException(userNotAuthorizedException);
            throwCapturedGenericException(anotherException, methodName);
            throwCapturedTypeErrorException(typeErrorException);
            throwCapturedPropertyErrorException(propertyErrorException);
            throwCapturedInvalidParameterException(invalidParameterException);
            throwCapturedFunctionNotSupportedException(functionNotSupportedException);
            throwCapturedEntityNotKnownException(entityNotKnownException);

            return null;
        }

        return new ArrayList<>(combinedResults.values());
    }



    /* ======================================================
     * Group 4: Maintaining entity and relationship instances
     */

    /**
     * Create a new entity and put it in the requested state.  The new entity is returned.
     *
     * @param userId unique identifier for requesting user.
     * @param entityTypeGUID unique identifier (guid) for the new entity's type.
     * @param initialProperties initial list of properties for the new entity null means no properties.
     * @param initialClassifications initial list of classifications for the new entity null means no classifications.
     * @param initialStatus initial status typically DRAFT, PREPARED or ACTIVE.
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
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public EntityDetail addEntity(String               userId,
                                  String               entityTypeGUID,
                                  InstanceProperties   initialProperties,
                                  List<Classification> initialClassifications,
                                  InstanceStatus       initialStatus) throws InvalidParameterException,
                                                                             RepositoryErrorException,
                                                                             TypeErrorException,
                                                                             PropertyErrorException,
                                                                             ClassificationErrorException,
                                                                             StatusNotSupportedException,
                                                                             FunctionNotSupportedException,
                                                                             UserNotAuthorizedException
    {
        final String  methodName  = "addEntity";

        super.addEntityParameterValidation(userId,
                                           entityTypeGUID,
                                           initialProperties,
                                           initialClassifications,
                                           initialStatus,
                                           methodName);

        /*
         * Validation complete, ok to continue with request
         *
         * The list of cohort connectors are retrieved for each request to ensure that any changes in
         * the shape of the cohort are reflected immediately.
         */
        List<OMRSRepositoryConnector> cohortConnectors = enterpriseParentConnector.getCohortConnectors(methodName);

        FederationControl federationControl = new SequentialFederationControl(userId, cohortConnectors, auditLog, methodName);
        AddEntityExecutor executor = new AddEntityExecutor(userId,
                                                           entityTypeGUID,
                                                           initialProperties,
                                                           initialClassifications,
                                                           initialStatus,
                                                           auditLog,
                                                           methodName);
        /*
         * Ready to process the request.  Create requests occur in the first repository that accepts the call.
         * Some repositories may produce exceptions.  These exceptions are saved and will be returned if
         * there are no positive results from any repository.
         */
        federationControl.executeCommand(executor);

        return executor.getNewEntity();
    }


    /**
     * Save a new entity that is sourced from an external technology.  The external
     * technology is identified by a GUID and a name.  These can be recorded in a
     * Software Capability (guid and qualifiedName respectively).
     * The new entity is assigned a new GUID and put
     * in the requested state.  The new entity is returned.
     *
     * @param userId unique identifier for requesting user.
     * @param entityTypeGUID unique identifier (guid) for the new entity's type.
     * @param externalSourceGUID unique identifier (guid) for the external source.
     * @param externalSourceName unique name for the external source.
     * @param initialProperties initial list of properties for the new entity; null means no properties.
     * @param initialClassifications initial list of classifications for the new entity null means no classifications.
     * @param initialStatus initial status typically DRAFT, PREPARED or ACTIVE.
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
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public EntityDetail addExternalEntity(String               userId,
                                          String               entityTypeGUID,
                                          String               externalSourceGUID,
                                          String               externalSourceName,
                                          InstanceProperties   initialProperties,
                                          List<Classification> initialClassifications,
                                          InstanceStatus       initialStatus) throws InvalidParameterException,
                                                                                     RepositoryErrorException,
                                                                                     TypeErrorException,
                                                                                     PropertyErrorException,
                                                                                     ClassificationErrorException,
                                                                                     StatusNotSupportedException,
                                                                                     FunctionNotSupportedException,
                                                                                     UserNotAuthorizedException
    {
        final String  methodName = "addExternalEntity";

        this.addExternalEntityParameterValidation(userId,
                                                  entityTypeGUID,
                                                  externalSourceGUID,
                                                  initialProperties,
                                                  initialClassifications,
                                                  initialStatus,
                                                  methodName);

        /*
         * Validation complete, ok to continue with request
         *
         * The list of cohort connectors are retrieved for each request to ensure that any changes in
         * the shape of the cohort are reflected immediately.
         */
        List<OMRSRepositoryConnector> cohortConnectors = enterpriseParentConnector.getCohortConnectors(methodName);

        FederationControl federationControl = new SequentialFederationControl(userId, cohortConnectors, auditLog, methodName);
        AddEntityExecutor executor = new AddEntityExecutor(userId,
                                                           entityTypeGUID,
                                                           externalSourceGUID,
                                                           externalSourceName,
                                                           initialProperties,
                                                           initialClassifications,
                                                           initialStatus,
                                                           auditLog,
                                                           methodName);

        /*
         * Ready to process the request.  Create requests occur in the first repository that accepts the call.
         * Some repositories may produce exceptions.  These exceptions are saved and will be returned if
         * there are no positive results from any repository.
         */
        federationControl.executeCommand(executor);

        return executor.getNewEntity();
    }


    /**
     * Create an entity proxy in the metadata collection.  This is used to store relationships that span metadata
     * repositories.
     *
     * @param userId unique identifier for requesting user.
     * @param entityProxy details of entity to add.
     * @throws FunctionNotSupportedException the repository does not support entity proxies as first class elements.
     */
    @Override
    public void addEntityProxy(String      userId,
                               EntityProxy entityProxy) throws FunctionNotSupportedException
    {
        final String  methodName = "addEntityProxy";

        throwNotEnterpriseFunction(methodName);
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
     * @throws StatusNotSupportedException invalid status for instance.
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public EntityDetail updateEntityStatus(String         userId,
                                           String         entityGUID,
                                           InstanceStatus newStatus) throws InvalidParameterException,
                                                                            RepositoryErrorException,
                                                                            EntityNotKnownException,
                                                                            StatusNotSupportedException,
                                                                            FunctionNotSupportedException,
                                                                            UserNotAuthorizedException
    {
        final String  methodName   = "updateEntityStatus";

        /*
         * Validate parameters
         */
        super.updateInstanceStatusParameterValidation(userId, entityGUID, newStatus, methodName);

        /*
         * Locate entity
         */
        EntitySummary entity = this.getEntitySummary(userId, entityGUID);

        /*
         * Validation complete, ok to make changes
         */
        OMRSMetadataCollection metadataCollection = enterpriseParentConnector.getHomeMetadataCollection(entity,
                                                                                                        methodName);
        if (metadataCollection != null)
        {
            return metadataCollection.updateEntityStatus(userId, entityGUID, newStatus);
        }

        return null;
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
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public EntityDetail updateEntityProperties(String             userId,
                                               String             entityGUID,
                                               InstanceProperties properties) throws InvalidParameterException,
                                                                                     RepositoryErrorException,
                                                                                     EntityNotKnownException,
                                                                                     PropertyErrorException,
                                                                                     FunctionNotSupportedException,
                                                                                     UserNotAuthorizedException
    {
        final String  methodName = "updateEntityProperties";

        /*
         * Validate parameters
         */
        this.updateInstancePropertiesPropertyValidation(userId, entityGUID, properties, methodName);

        /*
         * Locate entity
         */
        EntitySummary entity = this.getEntitySummary(userId, entityGUID);

        /*
         * Validation complete, ok to make changes
         */
        OMRSMetadataCollection metadataCollection = enterpriseParentConnector.getHomeMetadataCollection(entity,
                                                                                                        methodName);
        if (metadataCollection != null)
        {
            return metadataCollection.updateEntityProperties(userId, entityGUID, properties);
        }

        return null;
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
    public EntityDetail undoEntityUpdate(String userId,
                                         String entityGUID) throws InvalidParameterException,
                                                                   RepositoryErrorException,
                                                                   EntityNotKnownException,
                                                                   FunctionNotSupportedException,
                                                                   UserNotAuthorizedException
    {
        final String  methodName = "undoEntityUpdate";
        final String  parameterName = "entityGUID";

        /*
         * Validate parameters
         */
        super.manageInstanceParameterValidation(userId, entityGUID, parameterName, methodName);

        /*
         * Locate entity
         */
        EntitySummary entity = this.getEntitySummary(userId, entityGUID);

        /*
         * Validation complete, ok to make changes
         */
        OMRSMetadataCollection metadataCollection = enterpriseParentConnector.getHomeMetadataCollection(entity,
                                                                                                        methodName);
        if (metadataCollection != null)
        {
            return metadataCollection.undoEntityUpdate(userId, entityGUID);
        }

        return null;
    }


    /**
     * Delete an entity.  The entity is soft-deleted.  This means it is still in the graph, but it is no longer returned
     * on queries.  All homed relationships to the entity are also soft-deleted and will no longer be usable, while any
     * reference copy relationships to the entity will be purged (and will no longer be accessible in this repository).
     * To completely eliminate the entity from the graph requires a call to the purgeEntity() method after the delete call.
     * The restoreEntity() method will switch an entity back to Active status to restore the entity to normal use; however,
     * this will not restore any of the relationships that were soft-deleted as part of the original deleteEntity() call.
     *
     * @param userId unique identifier for requesting user.
     * @param typeDefGUID unique identifier of the type of the entity to delete.
     * @param typeDefName unique name of the type of the entity to delete.
     * @param obsoleteEntityGUID String unique identifier (guid) for the entity
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by the guid is not found in the metadata collection.
     * @throws FunctionNotSupportedException the metadata repository hosting the metadata collection does not support
     *                                       soft-deletes (use purgeEntity() to remove the entity permanently).
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public EntityDetail deleteEntity(String userId,
                                     String typeDefGUID,
                                     String typeDefName,
                                     String obsoleteEntityGUID) throws InvalidParameterException,
                                                                       RepositoryErrorException,
                                                                       EntityNotKnownException,
                                                                       FunctionNotSupportedException,
                                                                       UserNotAuthorizedException
    {
        final String methodName    = "deleteEntity";
        final String parameterName = "obsoleteEntityGUID";

        /*
         * Validate parameters
         */
        super.manageInstanceParameterValidation(userId,
                                                typeDefGUID,
                                                typeDefName,
                                                obsoleteEntityGUID,
                                                parameterName,
                                                methodName);

        /*
         * Locate entity
         */
        EntitySummary entity = this.getEntitySummary(userId, obsoleteEntityGUID);

        /*
         * Validation complete, ok to make changes
         */
        OMRSMetadataCollection metadataCollection = enterpriseParentConnector.getHomeMetadataCollection(entity,
                                                                                                        methodName);
        if (metadataCollection != null)
        {
            return metadataCollection.deleteEntity(userId, typeDefGUID, typeDefName, obsoleteEntityGUID);
        }

        return null;
    }


    /**
     * Permanently removes a deleted entity from the metadata collection. All relationships to the entity -- both homed
     * and reference copies -- will also be purged to maintain referential integrity within the repository. This request
     * can not be undone.
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
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public void purgeEntity(String userId,
                            String typeDefGUID,
                            String typeDefName,
                            String deletedEntityGUID) throws InvalidParameterException,
                                                             RepositoryErrorException,
                                                             EntityNotKnownException,
                                                             EntityNotDeletedException,
                                                             FunctionNotSupportedException,
                                                             UserNotAuthorizedException
    {
        final String methodName    = "purgeEntity";
        final String parameterName = "deletedEntityGUID";

        /*
         * Validate parameters
         */
        this.manageInstanceParameterValidation(userId,
                                               typeDefGUID,
                                               typeDefName,
                                               deletedEntityGUID,
                                               parameterName,
                                               methodName);

        /*
         * Validation complete, ok to continue with request
         *
         * The list of cohort connectors are retrieved for each request to ensure that any changes in
         * the shape of the cohort are reflected immediately.
         */
        List<OMRSRepositoryConnector> cohortConnectors = enterpriseParentConnector.getCohortConnectors(methodName);

        FederationControl federationControl = new SequentialFederationControl(userId, cohortConnectors, auditLog, methodName);
        PurgeEntityExecutor executor = new PurgeEntityExecutor(userId,
                                                               typeDefGUID,
                                                               typeDefName,
                                                               deletedEntityGUID,
                                                               auditLog,
                                                               methodName);

        /*
         * Ready to process the request.  Create requests occur in the first repository that accepts the call.
         * Some repositories may produce exceptions.  These exceptions are saved and will be returned if
         * there are no positive results from any repository.
         */
        federationControl.executeCommand(executor);

        executor.getResult();
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
    public EntityDetail restoreEntity(String userId,
                                      String deletedEntityGUID) throws InvalidParameterException,
                                                                       RepositoryErrorException,
                                                                       EntityNotKnownException,
                                                                       EntityNotDeletedException,
                                                                       FunctionNotSupportedException,
                                                                       UserNotAuthorizedException
    {
        final String methodName    = "restoreEntity";
        final String parameterName = "deletedEntityGUID";

        /*
         * Validate parameters
         */
        super.manageInstanceParameterValidation(userId, deletedEntityGUID, parameterName, methodName);

        /*
         * Validation complete, ok to continue with request
         *
         * The list of cohort connectors are retrieved for each request to ensure that any changes in
         * the shape of the cohort are reflected immediately.
         */
        List<OMRSRepositoryConnector> cohortConnectors = enterpriseParentConnector.getCohortConnectors(methodName);

        FederationControl federationControl = new SequentialFederationControl(userId, cohortConnectors, auditLog, methodName);
        RestoreEntityExecutor executor = new RestoreEntityExecutor(userId,
                                                                   deletedEntityGUID,
                                                                   auditLog,
                                                                   methodName);

        /*
         * Ready to process the request.  Create requests occur in the first repository that accepts the call.
         * Some repositories may produce exceptions.  These exceptions are saved and will be returned if
         * there are no positive results from any repository.
         */
        federationControl.executeCommand(executor);

        return executor.getRestoredEntity();
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
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public EntityDetail classifyEntity(String             userId,
                                       String             entityGUID,
                                       String             classificationName,
                                       InstanceProperties classificationProperties) throws InvalidParameterException,
                                                                                           RepositoryErrorException,
                                                                                           EntityNotKnownException,
                                                                                           ClassificationErrorException,
                                                                                           PropertyErrorException,
                                                                                           FunctionNotSupportedException,
                                                                                           UserNotAuthorizedException
    {
        final String  methodName = "classifyEntity";

        /*
         * Validate parameters
         */
        this.classifyEntityParameterValidation(userId,
                                               entityGUID,
                                               classificationName,
                                               classificationProperties,
                                               methodName);

        /*
         * Locate entity and check classification is not already present.
         */
        EntitySummary entity = this.getEntitySummary(userId, entityGUID);

        if (repositoryHelper.checkEntityNotClassifiedEntity(repositoryName, entity, classificationName, classificationProperties, auditLog, methodName) == null)
        {
            /*
             * Validation complete, ok to continue with request
             *
             * The list of cohort connectors are retrieved for each request to ensure that any changes in
             * the shape of the cohort are reflected immediately.
             */
            List<OMRSRepositoryConnector> cohortConnectors = enterpriseParentConnector.getHomeLocalRemoteConnectors(entity, methodName);

            FederationControl federationControl = new SequentialFederationControl(userId, cohortConnectors, auditLog, methodName);
            ClassifyEntityExecutor executor = new ClassifyEntityExecutor(userId,
                                                                         entityGUID,
                                                                         null,
                                                                         classificationName,
                                                                         null,
                                                                         null,
                                                                         null,
                                                                         null,
                                                                         classificationProperties,
                                                                         auditLog,
                                                                         methodName);

            /*
             * Ready to process the request.  Create requests occur in the first repository that accepts the call.
             * Some repositories may produce exceptions.  These exceptions are saved and will be returned if
             * there are no positive results from any repository.
             */
            federationControl.executeCommand(executor);

            return executor.getUpdatedEntity();
        }
        else
        {
            return this.isEntityKnown(userId, entityGUID);
        }
    }


    /**
     * Add the requested classification to a specific entity.
     *
     * @param userId unique identifier for requesting user.
     * @param entityProxy identifier (proxy) for the entity.
     * @param classificationName String name for the classification.
     * @param classificationProperties list of properties to set in the classification.
     * @return Classification showing the resulting entity header, properties and classifications.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by the guid is not found in the metadata collection
     * @throws ClassificationErrorException the requested classification is either not known or not valid
     *                                         for the entity.
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                characteristics in the TypeDef for this classification type
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public Classification classifyEntity(String             userId,
                                         EntityProxy        entityProxy,
                                         String             classificationName,
                                         InstanceProperties classificationProperties) throws InvalidParameterException,
                                                                                             RepositoryErrorException,
                                                                                             EntityNotKnownException,
                                                                                             ClassificationErrorException,
                                                                                             PropertyErrorException,
                                                                                             FunctionNotSupportedException,
                                                                                             UserNotAuthorizedException
    {
        final String  methodName = "classifyEntity";

        /*
         * Validate parameters
         */
        this.classifyEntityParameterValidation(userId,
                                               entityProxy,
                                               classificationName,
                                               classificationProperties,
                                               methodName);

        /*
         * Locate entity and check classification is not already present.
         */
        EntitySummary entity = this.getEntitySummary(userId, entityProxy.getGUID());

        Classification duplicateClassification = repositoryHelper.checkEntityNotClassifiedEntity(repositoryName, entity, classificationName, classificationProperties, auditLog, methodName);

        if (duplicateClassification == null)
        {
            /*
             * Validation complete, ok to continue with request
             *
             * The list of cohort connectors are retrieved for each request to ensure that any changes in
             * the shape of the cohort are reflected immediately.
             */
            List<OMRSRepositoryConnector> cohortConnectors = enterpriseParentConnector.getHomeLocalRemoteConnectors(entity, methodName);

            FederationControl federationControl = new SequentialFederationControl(userId, cohortConnectors, auditLog, methodName);
            ClassifyEntityExecutor executor = new ClassifyEntityExecutor(userId,
                                                                         entityProxy.getGUID(),
                                                                         entityProxy,
                                                                         classificationName,
                                                                         null,
                                                                         null,
                                                                         null,
                                                                         null,
                                                                         classificationProperties,
                                                                         auditLog,
                                                                         methodName);

            /*
             * Ready to process the request.  Create requests occur in the first repository that accepts the call.
             * Some repositories may produce exceptions.  These exceptions are saved and will be returned if
             * there are no positive results from any repository.
             */
            federationControl.executeCommand(executor);

            return executor.getAddedClassification();
        }
        else
        {
            return duplicateClassification;
        }
    }


    /**
     * Add the requested classification to a specific entity.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID String unique identifier (guid) for the entity.
     * @param classificationName String name for the classification.
     * @param externalSourceGUID unique identifier (guid) for the external source.
     * @param externalSourceName unique name for the external source.
     * @param classificationOrigin source of the classification
     * @param classificationOriginGUID if the classification is propagated, this is the unique identifier of the entity where
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
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     */
    @Override
    public EntityDetail classifyEntity(String               userId,
                                       String               entityGUID,
                                       String               classificationName,
                                       String               externalSourceGUID,
                                       String               externalSourceName,
                                       ClassificationOrigin classificationOrigin,
                                       String               classificationOriginGUID,
                                       InstanceProperties   classificationProperties) throws InvalidParameterException,
                                                                                             RepositoryErrorException,
                                                                                             EntityNotKnownException,
                                                                                             ClassificationErrorException,
                                                                                             PropertyErrorException,
                                                                                             UserNotAuthorizedException,
                                                                                             FunctionNotSupportedException
    {
        final String  methodName = "classifyEntity (Detailed)";

        /*
         * Validate parameters
         */
        this.classifyEntityParameterValidation(userId,
                                               entityGUID,
                                               classificationName,
                                               classificationProperties,
                                               methodName);

        /*
         * Locate entity and check classification is not already present.
         */
        EntitySummary entity = this.getEntitySummary(userId, entityGUID);

        if (repositoryHelper.checkEntityNotClassifiedEntity(repositoryName, entity, classificationName, classificationProperties, auditLog, methodName) == null)
        {
            /*
             * Validation complete, ok to continue with request
             *
             * The list of cohort connectors are retrieved for each request to ensure that any changes in
             * the shape of the cohort are reflected immediately.
             */
            List<OMRSRepositoryConnector> cohortConnectors = enterpriseParentConnector.getHomeLocalRemoteConnectors(entity, methodName);

            FederationControl federationControl = new SequentialFederationControl(userId, cohortConnectors, auditLog, methodName);
            ClassifyEntityExecutor executor = new ClassifyEntityExecutor(userId,
                                                                         entityGUID,
                                                                         null,
                                                                         classificationName,
                                                                         externalSourceGUID,
                                                                         externalSourceName,
                                                                         classificationOrigin,
                                                                         classificationOriginGUID,
                                                                         classificationProperties,
                                                                         auditLog,
                                                                         methodName);

            /*
             * Ready to process the request.  Create requests occur in the first repository that accepts the call.
             * Some repositories may produce exceptions.  These exceptions are saved and will be returned if
             * there are no positive results from any repository.
             */
            federationControl.executeCommand(executor);

            return executor.getUpdatedEntity();
        }
        else
        {
            return this.isEntityKnown(userId, entityGUID);
        }
    }


    /**
     * Add the requested classification to a specific entity.
     *
     * @param userId unique identifier for requesting user.
     * @param entityProxy entity as a proxy.
     * @param classificationName String name for the classification.
     * @param externalSourceGUID unique identifier (guid) for the external source.
     * @param externalSourceName unique name for the external source.
     * @param classificationOrigin source of the classification
     * @param classificationOriginGUID if the classification is propagated, this is the unique identifier of the entity where
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
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     */
    @Override
    public Classification classifyEntity(String               userId,
                                         EntityProxy          entityProxy,
                                         String               classificationName,
                                         String               externalSourceGUID,
                                         String               externalSourceName,
                                         ClassificationOrigin classificationOrigin,
                                         String               classificationOriginGUID,
                                         InstanceProperties   classificationProperties) throws InvalidParameterException,
                                                                                               RepositoryErrorException,
                                                                                               EntityNotKnownException,
                                                                                               ClassificationErrorException,
                                                                                               PropertyErrorException,
                                                                                               UserNotAuthorizedException,
                                                                                               FunctionNotSupportedException
    {
        final String  methodName = "classifyEntity (Detailed)";

        /*
         * Validate parameters
         */
        this.classifyEntityParameterValidation(userId, entityProxy, classificationName, classificationProperties, methodName);

        /*
         * Locate entity and check classification is not already present.
         */
        EntitySummary entity = this.getEntitySummary(userId, entityProxy.getGUID());

        Classification duplicateClassification = repositoryHelper.checkEntityNotClassifiedEntity(repositoryName, entity, classificationName, classificationProperties, auditLog, methodName);

        if (duplicateClassification == null)
        {
            /*
             * Validation complete, ok to continue with request
             *
             * The list of cohort connectors are retrieved for each request to ensure that any changes in
             * the shape of the cohort are reflected immediately.
             */
            List<OMRSRepositoryConnector> cohortConnectors = enterpriseParentConnector.getHomeLocalRemoteConnectors(entity, methodName);

            FederationControl federationControl = new SequentialFederationControl(userId, cohortConnectors, auditLog, methodName);
            ClassifyEntityExecutor executor = new ClassifyEntityExecutor(userId,
                                                                         entityProxy.getGUID(),
                                                                         entityProxy,
                                                                         classificationName,
                                                                         externalSourceGUID,
                                                                         externalSourceName,
                                                                         classificationOrigin,
                                                                         classificationOriginGUID,
                                                                         classificationProperties,
                                                                         auditLog,
                                                                         methodName);

            /*
             * Ready to process the request.  Create requests occur in the first repository that accepts the call.
             * Some repositories may produce exceptions.  These exceptions are saved and will be returned if
             * there are no positive results from any repository.
             */
            federationControl.executeCommand(executor);

            return executor.getAddedClassification();
        }
        else
        {
            return duplicateClassification;
        }
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
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public EntityDetail declassifyEntity(String userId,
                                         String entityGUID,
                                         String classificationName) throws InvalidParameterException,
                                                                           RepositoryErrorException,
                                                                           EntityNotKnownException,
                                                                           ClassificationErrorException,
                                                                           FunctionNotSupportedException,
                                                                           UserNotAuthorizedException
    {
        final String  methodName = "declassifyEntity";

        /*
         * Validate parameters
         */
        super.declassifyEntityParameterValidation(userId, entityGUID, classificationName, methodName);

        /*
         * Locate entity and retrieve classification.
         */
        EntitySummary  entity         = this.getEntitySummary(userId, entityGUID);
        Classification classification = repositoryHelper.getClassificationFromEntity(repositoryName, entity, classificationName, methodName);

        /*
         * Validation complete, ok to make changes
         */
        OMRSMetadataCollection metadataCollection = enterpriseParentConnector.getHomeMetadataCollection(classification, methodName);
        if (metadataCollection != null)
        {
            return metadataCollection.declassifyEntity(userId, entityGUID, classificationName);
        }

        return null;
    }

    /**
     * Remove a specific classification from an entity.
     *
     * @param userId unique identifier for requesting user.
     * @param entityProxy identifier (proxy) for the entity.
     * @param classificationName String name for the classification.
     * @return Classification showing the resulting entity header, properties and classifications.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by the guid is not found in the metadata collection
     * @throws ClassificationErrorException the requested classification is not set on the entity.
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public Classification declassifyEntity(String      userId,
                                           EntityProxy entityProxy,
                                           String      classificationName) throws InvalidParameterException,
                                                                                  RepositoryErrorException,
                                                                                  EntityNotKnownException,
                                                                                  ClassificationErrorException,
                                                                                  FunctionNotSupportedException,
                                                                                  UserNotAuthorizedException
    {
        final String  methodName = "declassifyEntity (EntityProxy)";

        /*
         * Validate parameters
         */
        super.declassifyEntityParameterValidation(userId, entityProxy, classificationName, methodName);

        /*
         * Locate entity and retrieve classification.
         */
        EntitySummary  entity         = this.getEntitySummary(userId, entityProxy.getGUID());
        Classification classification = repositoryHelper.getClassificationFromEntity(repositoryName, entity, classificationName, methodName);

        /*
         * Validation complete, ok to make changes
         */
        OMRSMetadataCollection metadataCollection = enterpriseParentConnector.getHomeMetadataCollection(classification, methodName);
        if (metadataCollection != null)
        {
            return metadataCollection.declassifyEntity(userId, entityProxy, classificationName);
        }

        return null;
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
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public EntityDetail updateEntityClassification(String             userId,
                                                   String             entityGUID,
                                                   String             classificationName,
                                                   InstanceProperties properties) throws InvalidParameterException,
                                                                                         RepositoryErrorException,
                                                                                         EntityNotKnownException,
                                                                                         ClassificationErrorException,
                                                                                         PropertyErrorException,
                                                                                         FunctionNotSupportedException,
                                                                                         UserNotAuthorizedException
    {
        final String  methodName = "updateEntityClassification";

        /*
         * Validate parameters
         */
        classifyEntityParameterValidation(userId, entityGUID, classificationName, properties, methodName);

        /*
         * Locate entity and retrieve classification.
         */
        EntitySummary  entity         = this.getEntitySummary(userId, entityGUID);
        Classification classification = repositoryHelper.getClassificationFromEntity(repositoryName, entity, classificationName, methodName);

        /*
         * Validation complete, ok to make changes
         */
        OMRSMetadataCollection metadataCollection = enterpriseParentConnector.getHomeMetadataCollection(classification, methodName);
        if (metadataCollection != null)
        {
            return metadataCollection.updateEntityClassification(userId,
                                                                 entityGUID,
                                                                 classificationName,
                                                                 properties);
        }

        return null;
    }


    /**
     * Update one or more properties in one of an entity's classifications.
     *
     * @param userId unique identifier for requesting user.
     * @param entityProxy identifier (proxy) for the entity.
     * @param classificationName String name for the classification.
     * @param properties list of properties for the classification.
     * @return Classification showing the resulting entity header, properties and classifications.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by the guid is not found in the metadata collection
     * @throws ClassificationErrorException the requested classification is not attached to the classification.
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                characteristics in the TypeDef for this classification type
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public Classification updateEntityClassification(String             userId,
                                                     EntityProxy        entityProxy,
                                                     String             classificationName,
                                                     InstanceProperties properties) throws InvalidParameterException,
                                                                                           RepositoryErrorException,
                                                                                           EntityNotKnownException,
                                                                                           ClassificationErrorException,
                                                                                           PropertyErrorException,
                                                                                           FunctionNotSupportedException,
                                                                                           UserNotAuthorizedException
    {
        final String  methodName = "updateEntityClassification (EntityProxy)";

        /*
         * Validate parameters
         */
        classifyEntityParameterValidation(userId, entityProxy, classificationName, properties, methodName);

        /*
         * Locate entity and retrieve classification.
         */
        EntitySummary  entity         = this.getEntitySummary(userId, entityProxy.getGUID());
        Classification classification = repositoryHelper.getClassificationFromEntity(repositoryName, entity, classificationName, methodName);

        /*
         * Validation complete, ok to make changes
         */
        OMRSMetadataCollection metadataCollection = enterpriseParentConnector.getHomeMetadataCollection(classification, methodName);
        if (metadataCollection != null)
        {
            return metadataCollection.updateEntityClassification(userId,
                                                                 entityProxy,
                                                                 classificationName,
                                                                 properties);
        }

        return null;
    }

    /**
     * Add a new relationship between two entities to the metadata collection.
     * <p>
     * There are 3 parts to this method
     * <ol>
     *     <li>Validation of the associated typedefs and supplied properties </li>
     *     <li>Validate that the requested relationship ends exist, by checking each server (each cohortConnector)
     *     looking for the entity at each end of the relationship. If either end entities are not found then an
     *     EntityNotKnownException is thrown.</li>
     *     <li>For each server, check it has the entity for each end, if not then attempt to create proxies.
     *     If we now have 2 valid ends on this server, then  add the relationship. Do this for each server in turn until
     *     it adds successfully. If it does not add to any server then throw an error.</li>
     * </ol>
     *
     * @param userId               unique identifier for requesting user.
     * @param relationshipTypeGUID unique identifier (guid) for the new relationship's type.
     * @param initialProperties    initial list of properties for the new entity, null means no properties.
     * @param entityOneGUID        the unique identifier of one of the entities that the relationship is connecting together.
     * @param entityTwoGUID        the unique identifier of the other entity that the relationship is connecting together.
     * @param initialStatus        initial status. This is typically DRAFT, PREPARED or ACTIVE.
     * @return Relationship structure with the new header, requested entities and properties.
     * @throws InvalidParameterException   one of the parameters is invalid or null.
     * @throws RepositoryErrorException    there is a problem communicating with the metadata repository where
     *                                     the metadata collection is stored.
     * @throws TypeErrorException          the requested type is not known, or not supported in the metadata repository
     *                                     hosting the metadata collection.
     * @throws PropertyErrorException      one or more of the requested properties are not defined, or have different
     *                                     characteristics in the TypeDef for this relationship's type.
     * @throws EntityNotKnownException     one of the requested entities is not known in the metadata collection.
     * @throws StatusNotSupportedException the metadata repository hosting the metadata collection does not support
     *                                     the requested status.
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     * @throws UserNotAuthorizedException  the userId is not permitted to perform this operation.
     */
    @Override
    public Relationship addRelationship(String             userId,
                                        String             relationshipTypeGUID,
                                        InstanceProperties initialProperties,
                                        String             entityOneGUID,
                                        String             entityTwoGUID,
                                        InstanceStatus     initialStatus) throws InvalidParameterException,
                                                                                 RepositoryErrorException,
                                                                                 TypeErrorException,
                                                                                 PropertyErrorException,
                                                                                 EntityNotKnownException,
                                                                                 StatusNotSupportedException,
                                                                                 FunctionNotSupportedException,
                                                                                 UserNotAuthorizedException
    {
        final String methodName = "addRelationship";


        /*
         * Validate parameters
         */
        super.addRelationshipParameterValidation(userId,
                                                 relationshipTypeGUID,
                                                 initialProperties,
                                                 entityOneGUID,
                                                 entityTwoGUID,
                                                 initialStatus,
                                                 methodName);

        EntityProxy entityOneProxy = this.getEntityProxy(userId, entityOneGUID, methodName);
        EntityProxy entityTwoProxy = this.getEntityProxy(userId, entityTwoGUID, methodName);

        /*
         * Validation complete, ok to continue with request
         *
         * The list of cohort connectors are retrieved for each request to ensure that any changes in
         * the shape of the cohort are reflected immediately.
         */
        List<OMRSRepositoryConnector> cohortConnectors = enterpriseParentConnector.getCohortConnectors(methodName);

        FederationControl federationControl = new SequentialFederationControl(userId, cohortConnectors, auditLog, methodName);
        AddRelationshipExecutor executor = new AddRelationshipExecutor(userId,
                                                                       relationshipTypeGUID,
                                                                       initialProperties,
                                                                       entityOneProxy,
                                                                       entityTwoProxy,
                                                                       initialStatus,
                                                                       auditLog,
                                                                       methodName);

        /*
         * Ready to process the request.  Create requests occur in the first repository that accepts the call.
         * Some repositories may produce exceptions.  These exceptions are saved and will be returned if
         * there are no positive results from any repository.
         */
        federationControl.executeCommand(executor);

        return executor.getNewRelationship();
    }


    /**
     * Save a new relationship that is sourced from an external technology.  The external
     * technology is identified by a GUID and a name.  These can be recorded in a
     * Software Server Capability (guid and qualifiedName respectively).
     * The new relationship is assigned a new GUID and put
     * in the requested state.  The new relationship is returned.
     *
     * @param userId unique identifier for requesting user.
     * @param relationshipTypeGUID unique identifier (guid) for the new relationship's type.
     * @param externalSourceGUID unique identifier (guid) for the external source.
     * @param externalSourceName unique name for the external source.
     * @param initialProperties initial list of properties for the new entity; null means no properties.
     * @param entityOneGUID the unique identifier of one of the entities that the relationship is connecting together.
     * @param entityTwoGUID the unique identifier of the other entity that the relationship is connecting together.
     * @param initialStatus initial status; typically DRAFT, PREPARED or ACTIVE.
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
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     */
    @Override
    public Relationship addExternalRelationship(String             userId,
                                                String             relationshipTypeGUID,
                                                String             externalSourceGUID,
                                                String             externalSourceName,
                                                InstanceProperties initialProperties,
                                                String             entityOneGUID,
                                                String             entityTwoGUID,
                                                InstanceStatus     initialStatus) throws InvalidParameterException,
                                                                                         RepositoryErrorException,
                                                                                         TypeErrorException,
                                                                                         PropertyErrorException,
                                                                                         EntityNotKnownException,
                                                                                         StatusNotSupportedException,
                                                                                         UserNotAuthorizedException,
                                                                                         FunctionNotSupportedException
    {
        final String  methodName = "addExternalRelationship";

        /*
         * Validate parameters
         */
        this.addExternalRelationshipParameterValidation(userId,
                                                        relationshipTypeGUID,
                                                        externalSourceGUID,
                                                        initialProperties,
                                                        entityOneGUID,
                                                        entityTwoGUID,
                                                        initialStatus,
                                                        methodName);

        EntityProxy entityOneProxy = this.getEntityProxy(userId, entityOneGUID, methodName);
        EntityProxy entityTwoProxy = this.getEntityProxy(userId, entityTwoGUID, methodName);

        /*
         * Validation complete, ok to continue with request
         *
         * The list of cohort connectors are retrieved for each request to ensure that any changes in
         * the shape of the cohort are reflected immediately.
         */
        List<OMRSRepositoryConnector> cohortConnectors = enterpriseParentConnector.getCohortConnectors(methodName);

        FederationControl federationControl = new SequentialFederationControl(userId, cohortConnectors, auditLog, methodName);
        AddRelationshipExecutor executor = new AddRelationshipExecutor(userId,
                                                                       relationshipTypeGUID,
                                                                       externalSourceGUID,
                                                                       externalSourceName,
                                                                       initialProperties,
                                                                       entityOneProxy,
                                                                       entityTwoProxy,
                                                                       initialStatus,
                                                                       auditLog,
                                                                       methodName);

        /*
         * Ready to process the request.  Create requests occur in the first repository that accepts the call.
         * Some repositories may produce exceptions.  These exceptions are saved and will be returned if
         * there are no positive results from any repository.
         */
        federationControl.executeCommand(executor);

        return executor.getNewRelationship();
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
     * @throws StatusNotSupportedException invalid status for instance.
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public Relationship updateRelationshipStatus(String         userId,
                                                 String         relationshipGUID,
                                                 InstanceStatus newStatus) throws InvalidParameterException,
                                                                                  RepositoryErrorException,
                                                                                  RelationshipNotKnownException,
                                                                                  StatusNotSupportedException,
                                                                                  FunctionNotSupportedException,
                                                                                  UserNotAuthorizedException
    {
        final String  methodName          = "updateRelationshipStatus";

        /*
         * Validate parameters
         */
        super.updateInstanceStatusParameterValidation(userId, relationshipGUID, newStatus, methodName);

        /*
         * Locate relationship
         */
        Relationship relationship = this.getRelationship(userId, relationshipGUID);

        /*
         * Validation complete, ok to make changes
         */
        OMRSMetadataCollection metadataCollection = enterpriseParentConnector.getHomeMetadataCollection(relationship,
                                                                                                        methodName);
        if (metadataCollection != null)
        {
            return metadataCollection.updateRelationshipStatus(userId, relationshipGUID, newStatus);
        }

        return null;
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
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public Relationship updateRelationshipProperties(String             userId,
                                                     String             relationshipGUID,
                                                     InstanceProperties properties) throws InvalidParameterException,
                                                                                           RepositoryErrorException,
                                                                                           RelationshipNotKnownException,
                                                                                           PropertyErrorException,
                                                                                           FunctionNotSupportedException,
                                                                                           UserNotAuthorizedException
    {
        final String  methodName = "updateRelationshipProperties";

        /*
         * Validate parameters
         */
        super.updateInstancePropertiesPropertyValidation(userId, relationshipGUID, properties, methodName);

        /*
         * Locate relationship
         */
        Relationship relationship = this.getRelationship(userId, relationshipGUID);

        /*
         * Validation complete, ok to make changes
         */
        OMRSMetadataCollection metadataCollection = enterpriseParentConnector.getHomeMetadataCollection(relationship,
                                                                                                        methodName);
        if (metadataCollection != null)
        {
            return metadataCollection.updateRelationshipProperties(userId, relationshipGUID, properties);
        }

        return null;
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
    public Relationship undoRelationshipUpdate(String userId,
                                               String relationshipGUID) throws InvalidParameterException,
                                                                               RepositoryErrorException,
                                                                               RelationshipNotKnownException,
                                                                               FunctionNotSupportedException,
                                                                               UserNotAuthorizedException
    {
        final String  methodName = "undoRelationshipUpdate";
        final String  parameterName = "relationshipGUID";

        /*
         * Validate parameters
         */
        super.manageInstanceParameterValidation(userId, relationshipGUID, parameterName, methodName);

        /*
         * Locate relationship
         */
        Relationship relationship = this.getRelationship(userId, relationshipGUID);

        /*
         * Validation complete, ok to make changes
         */
        OMRSMetadataCollection metadataCollection = enterpriseParentConnector.getHomeMetadataCollection(relationship,
                                                                                                        methodName);
        if (metadataCollection != null)
        {
            return metadataCollection.undoRelationshipUpdate(userId, relationshipGUID);
        }

        return null;
    }


    /**
     * Delete a specific relationship.  This is a soft-delete which means the relationship's status is updated to
     * DELETED, and it is no longer available for queries.  To remove the relationship permanently from the
     * metadata collection, use purgeRelationship().
     *
     * @param userId unique identifier for requesting user.
     * @param typeDefGUID unique identifier of the type of the relationship to delete.
     * @param typeDefName unique name of the type of the relationship to delete.
     * @param obsoleteRelationshipGUID String unique identifier (guid) for the relationship.
     * @return delete relationship
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
        final String  parameterName = "obsoleteRelationshipGUID";

        /*
         * Validate parameters
         */
        this.manageInstanceParameterValidation(userId,
                                               typeDefGUID,
                                               typeDefName,
                                               obsoleteRelationshipGUID,
                                               parameterName,
                                               methodName);

        /*
         * Locate relationship
         */
        Relationship relationship = this.getRelationship(userId, obsoleteRelationshipGUID);

        /*
         * Validation complete, ok to make changes
         */
        OMRSMetadataCollection metadataCollection = enterpriseParentConnector.getHomeMetadataCollection(relationship,
                                                                                                        methodName);
        if (metadataCollection != null)
        {
            return metadataCollection.deleteRelationship(userId,
                                                         typeDefGUID,
                                                         typeDefName,
                                                         obsoleteRelationshipGUID);
        }

        return null;
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
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public void purgeRelationship(String userId,
                                  String typeDefGUID,
                                  String typeDefName,
                                  String deletedRelationshipGUID) throws InvalidParameterException,
                                                                         RepositoryErrorException,
                                                                         RelationshipNotKnownException,
                                                                         RelationshipNotDeletedException,
                                                                         FunctionNotSupportedException,
                                                                         UserNotAuthorizedException
    {
        final String  methodName = "purgeRelationship";
        final String  parameterName = "deletedRelationshipGUID";

        /*
         * Validate parameters
         */
        this.manageInstanceParameterValidation(userId,
                                               typeDefGUID,
                                               typeDefName,
                                               deletedRelationshipGUID,
                                               parameterName,
                                               methodName);

        /*
         * Validation complete, ok to continue with request
         *
         * The list of cohort connectors are retrieved for each request to ensure that any changes in
         * the shape of the cohort are reflected immediately.
         */
        List<OMRSRepositoryConnector> cohortConnectors = enterpriseParentConnector.getCohortConnectors(methodName);

        FederationControl federationControl = new SequentialFederationControl(userId, cohortConnectors, auditLog, methodName);
        PurgeRelationshipExecutor executor = new PurgeRelationshipExecutor(userId,
                                                                           typeDefGUID,
                                                                           typeDefName,
                                                                           deletedRelationshipGUID,
                                                                           auditLog,
                                                                           methodName);

        /*
         * Ready to process the request.  Create requests occur in the first repository that accepts the call.
         * Some repositories may produce exceptions.  These exceptions are saved and will be returned if
         * there are no positive results from any repository.
         */
        federationControl.executeCommand(executor);

        executor.getResult();
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
    public Relationship restoreRelationship(String userId,
                                            String deletedRelationshipGUID) throws InvalidParameterException,
                                                                                   RepositoryErrorException,
                                                                                   RelationshipNotKnownException,
                                                                                   RelationshipNotDeletedException,
                                                                                   FunctionNotSupportedException,
                                                                                   UserNotAuthorizedException
    {
        final String  methodName = "restoreRelationship";
        final String  parameterName = "deletedRelationshipGUID";

        /*
         * Validate parameters
         */
        this.manageInstanceParameterValidation(userId, deletedRelationshipGUID, parameterName, methodName);

        /*
         * Validation complete, ok to continue with request
         *
         * The list of cohort connectors are retrieved for each request to ensure that any changes in
         * the shape of the cohort are reflected immediately.
         */
        List<OMRSRepositoryConnector> cohortConnectors = enterpriseParentConnector.getCohortConnectors(methodName);

        FederationControl federationControl = new SequentialFederationControl(userId, cohortConnectors, auditLog, methodName);
        RestoreRelationshipExecutor executor = new RestoreRelationshipExecutor(userId,
                                                                               deletedRelationshipGUID,
                                                                               auditLog,
                                                                               methodName);

        /*
         * Ready to process the request.  Create requests occur in the first repository that accepts the call.
         * Some repositories may produce exceptions.  These exceptions are saved and will be returned if
         * there are no positive results from any repository.
         */
        federationControl.executeCommand(executor);

        return executor.getRestoredRelationship();
    }


    /* ======================================================================
     * Group 5: Change the control information in entities and relationships
     */


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
     * @throws FunctionNotSupportedException the repository does not support the re-identification of instances.
     */
    @Override
    public EntityDetail reIdentifyEntity(String userId,
                                         String typeDefGUID,
                                         String typeDefName,
                                         String entityGUID,
                                         String newEntityGUID) throws FunctionNotSupportedException
    {
        final String  methodName = "reIdentifyEntity";

        throwNotEnterpriseFunction(methodName);

        return null;
    }


    /**
     * Change an existing entity's type.  Typically, this action is taken to move an entity's
     * type to either a super type (so the subtype can be deleted) or a new subtype (so additional properties can be
     * added.)  However, the type can be changed to any compatible type and the properties adjusted.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID the unique identifier for the entity to change.
     * @param currentTypeDefSummary the current details of the TypeDef for the entity used to verify the entity identity
     * @param newTypeDefSummary details of this entity's new TypeDef.
     * @return entity new values for this entity, including the new type information.
     * @throws FunctionNotSupportedException the repository does not support the re-typing of instances.
     */
    @Override
    public EntityDetail reTypeEntity(String         userId,
                                     String         entityGUID,
                                     TypeDefSummary currentTypeDefSummary,
                                     TypeDefSummary newTypeDefSummary) throws FunctionNotSupportedException
    {
        final String  methodName = "reTypeEntity";

        throwNotEnterpriseFunction(methodName);

        return null;
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
     * @throws FunctionNotSupportedException the repository does not support the re-homing of instances.
     */
    @Override
    public EntityDetail reHomeEntity(String userId,
                                     String entityGUID,
                                     String typeDefGUID,
                                     String typeDefName,
                                     String homeMetadataCollectionId,
                                     String newHomeMetadataCollectionId,
                                     String newHomeMetadataCollectionName) throws FunctionNotSupportedException
    {
        final String  methodName = "reHomeEntity";

        throwNotEnterpriseFunction(methodName);

        return null;
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
     * @throws FunctionNotSupportedException the repository does not support the re-identification of instances.
     */
    @Override
    public Relationship reIdentifyRelationship(String userId,
                                               String typeDefGUID,
                                               String typeDefName,
                                               String relationshipGUID,
                                               String newRelationshipGUID) throws FunctionNotSupportedException
    {
        final String  methodName = "reIdentifyRelationship";

        throwNotEnterpriseFunction(methodName);

        return null;
    }


    /**
     * Change an existing relationship's type.  Typically, this action is taken to move a relationship's
     * type to either a super type (so the subtype can be deleted) or a new subtype (so additional properties can be
     * added.)  However, the type can be changed to any compatible type.
     *
     * @param userId unique identifier for requesting user.
     * @param relationshipGUID the unique identifier for the relationship.
     * @param currentTypeDefSummary the details of the TypeDef for the relationship used to verify the relationship identity.
     * @param newTypeDefSummary details of this relationship's new TypeDef.
     * @return relationship new values for this relationship, including the new type information.
     * @throws FunctionNotSupportedException the repository does not support the re-typing of instances.
     */
    @Override
    public Relationship reTypeRelationship(String         userId,
                                           String         relationshipGUID,
                                           TypeDefSummary currentTypeDefSummary,
                                           TypeDefSummary newTypeDefSummary) throws FunctionNotSupportedException
    {
        final String methodName = "reTypeRelationship";

        throwNotEnterpriseFunction(methodName);

        return null;
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
     * @throws FunctionNotSupportedException the repository does not support the re-homing of instances.
     */
    @Override
    public Relationship reHomeRelationship(String userId,
                                           String relationshipGUID,
                                           String typeDefGUID,
                                           String typeDefName,
                                           String homeMetadataCollectionId,
                                           String newHomeMetadataCollectionId,
                                           String newHomeMetadataCollectionName) throws FunctionNotSupportedException
    {
        final String    methodName = "reHomeRelationship";

        throwNotEnterpriseFunction(methodName);

        return null;
    }



    /* ======================================================================
     * Group 6: Local house-keeping of reference metadata instances
     * These methods are not supported by the EnterpriseOMRSRepositoryConnector
     */


    /**
     * Save the entity as a reference copy.  The id of the home metadata collection is already set up in the
     * entity.
     *
     * @param userId unique identifier for requesting server.
     * @param entity details of the entity to save
     * @throws FunctionNotSupportedException the repository does not support reference copies of instances.
     */
    @Override
    public void saveEntityReferenceCopy(String       userId,
                                        EntityDetail entity) throws FunctionNotSupportedException
    {
        final String    methodName = "saveEntityReferenceCopy";

        throwNotEnterpriseFunction(methodName);
    }



    /**
     * Retrieve any locally homed classifications assigned to the requested entity.  This method is implemented by repository connectors that are able
     * to store classifications for entities that are homed in another repository.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID unique identifier of the entity with classifications to retrieve
     * @return list of all the classifications for this entity that are homed in this repository
     * @throws FunctionNotSupportedException this method is not supported
     */
    @Override
    public List<Classification> getHomeClassifications(String userId,
                                                       String entityGUID) throws FunctionNotSupportedException
    {
        final String  methodName = "getHomeClassifications";

        throwNotEnterpriseFunction(methodName);
        return null;
    }


    /**
     * Retrieve any locally homed classifications assigned to the requested entity.  This method is implemented by repository connectors that are able
     * to store classifications for entities that are homed in another repository.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID unique identifier of the entity with classifications to retrieve
     * @param asOfTime the time used to determine which version of the entity that is desired.
     * @return list of all the classifications for this entity that are homed in this repository
     * @throws FunctionNotSupportedException this method is not supported
     */
    @Override
    public List<Classification> getHomeClassifications(String userId,
                                                       String entityGUID,
                                                       Date   asOfTime) throws FunctionNotSupportedException
    {
        final String  methodName = "getHomeClassifications (with history)";

        throwNotEnterpriseFunction(methodName);
        return null;
    }

    /**
     * Remove a reference copy of the entity from the local repository.  This method can be used to
     * remove reference copies from the local cohort, repositories that have left the cohort,
     * or entities that have come from open metadata archives.  It is also an opportunity to remove or
     * soft delete relationships attached to the entity
     *
     * @param userId unique identifier for requesting server.
     * @param entity details of the entity to purge.
     * @throws FunctionNotSupportedException the repository does not support reference copies of instances.
     */
    @Override
    public  void deleteEntityReferenceCopy(String       userId,
                                           EntityDetail entity) throws FunctionNotSupportedException
    {
        final String  methodName = "deleteEntityReferenceCopy";

        throwNotEnterpriseFunction(methodName);
    }


    /**
     * Remove a reference copy of the entity from the local repository.  This method can be used to
     * remove reference copies from the local cohort, repositories that have left the cohort,
     * or entities that have come from open metadata archives.  It is also an opportunity to remove
     * relationships attached to the entity.
     * This method is called when a remote repository calls the variant of purgeEntity that
     * passes the EntityDetail object.  This is typically used if purge is called without a previous soft-delete.
     * However, it may also be used to purge after a soft-delete.
     *
     * @param userId unique identifier for requesting server.
     * @param entity details of the entity to purge.
     * @throws FunctionNotSupportedException the repository does not support reference copies of instances.
     */
    @Override
    public  void purgeEntityReferenceCopy(String       userId,
                                          EntityDetail entity) throws FunctionNotSupportedException
    {
        final String    methodName = "purgeEntityReferenceCopy";

        throwNotEnterpriseFunction(methodName);
    }


    /**
     * Remove a reference copy of the entity from the local repository.  This method can be used to
     * remove reference copies from the local cohort, repositories that have left the cohort,
     * or entities that have come from open metadata archives.
     *
     * @param userId unique identifier for requesting server.
     * @param entityGUID the unique identifier for the entity.
     * @param typeDefGUID the guid of the TypeDef for the relationship used to verify the relationship identity.
     * @param typeDefName the name of the TypeDef for the relationship used to verify the relationship identity.
     * @param homeMetadataCollectionId identifier of the metadata collection that is the home to this entity.
     * @throws FunctionNotSupportedException the repository does not support reference copies of instances.
     */
    @Override
    public void purgeEntityReferenceCopy(String userId,
                                         String entityGUID,
                                         String typeDefGUID,
                                         String typeDefName,
                                         String homeMetadataCollectionId) throws FunctionNotSupportedException
    {
        final String    methodName = "purgeEntityReferenceCopy";

        throwNotEnterpriseFunction(methodName);
    }


    /**
     * The local server has requested that the repository that hosts the home metadata collection for the
     * specified entity sends out the details of this entity so the local repository can create a reference copy.
     *
     * @param userId unique identifier for requesting server.
     * @param entityGUID unique identifier of requested entity
     * @param typeDefGUID unique identifier of requested entity's TypeDef
     * @param typeDefName unique name of requested entity's TypeDef
     * @param homeMetadataCollectionId identifier of the metadata collection that is the home to this entity.
     * @throws FunctionNotSupportedException the repository does not support reference copies of instances.
     */
    @Override
    public void refreshEntityReferenceCopy(String userId,
                                           String entityGUID,
                                           String typeDefGUID,
                                           String typeDefName,
                                           String homeMetadataCollectionId) throws FunctionNotSupportedException
    {
        final String    methodName = "refreshEntityReferenceCopy";

        throwNotEnterpriseFunction(methodName);
    }


    /**
     * Save the classification as a reference copy.  The id of the home metadata collection is already set up in the
     * classification.  The entity may be either a locally homed entity or a reference copy.
     *
     * @param userId unique identifier for requesting user.
     * @param entity entity that the classification is attached to.
     * @param classification classification to save.
     *
     * @throws FunctionNotSupportedException the repository does not support reference copies of instances.
     */
    @Override
    public void saveClassificationReferenceCopy(String         userId,
                                                EntityDetail   entity,
                                                Classification classification) throws FunctionNotSupportedException
    {
        final String methodName = "saveClassificationReferenceCopy";

        throwNotEnterpriseFunction(methodName);
    }


    /**
     * Save the classification as a reference copy.  The id of the home metadata collection is already set up in the
     * classification.  The entity may be either a locally homed entity or a reference copy.
     *
     * @param userId unique identifier for requesting user.
     * @param entity entity that the classification is attached to.
     * @param classification classification to save.
     *
     * @throws FunctionNotSupportedException the repository does not support reference copies of instances.
     */
    @Override
    public void saveClassificationReferenceCopy(String         userId,
                                                EntityProxy    entity,
                                                Classification classification) throws FunctionNotSupportedException
    {
        final String methodName = "saveClassificationReferenceCopy";

        throwNotEnterpriseFunction(methodName);
    }


    /**
     * Remove the reference copy of the classification from the local repository. This method can be used to
     * remove reference copies from the local cohort, repositories that have left the cohort,
     * or relationships that have come from open metadata archives.
     *
     * @param userId unique identifier for requesting user.
     * @param entity entity that the classification is attached to.
     * @param classification classification to purge.
     *
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     */
    @Override
    public  void purgeClassificationReferenceCopy(String         userId,
                                                  EntityDetail   entity,
                                                  Classification classification) throws FunctionNotSupportedException
    {
        final String methodName = "purgeClassificationReferenceCopy";

        throwNotEnterpriseFunction(methodName);
    }


    /**
     * Save the relationship as a reference copy.  The id of the home metadata collection is already set up in the
     * relationship.
     *
     * @param userId unique identifier for requesting server.
     * @param relationship relationship to save
     * @throws FunctionNotSupportedException the repository does not support reference copies of instances.
     */
    @Override
    public void saveRelationshipReferenceCopy(String       userId,
                                              Relationship relationship) throws FunctionNotSupportedException
    {
        final String    methodName = "saveRelationshipReferenceCopy";

        throwNotEnterpriseFunction(methodName);
    }


    /**
     * Remove the reference copy of the relationship from the local repository. This method can be used to
     * remove reference copies from the local cohort, repositories that have left the cohort,
     * or relationships that have come from open metadata archives.
     *
     * @param userId unique identifier for requesting server.
     * @param relationship relationship to purge.
     * @throws FunctionNotSupportedException the repository does not support reference copies of instances.
     */
    @Override
    public  void deleteRelationshipReferenceCopy(String       userId,
                                                 Relationship relationship) throws FunctionNotSupportedException
    {
        final String  methodName = "deleteRelationshipReferenceCopy";

        throwNotEnterpriseFunction(methodName);
    }


    /**
     * This method is called when a remote repository calls the variant of purgeRelationship that
     * passes the relationship object.  This is typically used if purge is called without a previous soft-delete.
     * However, it may also be used to purge after a soft-delete.
     * Remove the reference copy of the relationship from the local repository. This method can be used to
     * remove reference copies from the local cohort, repositories that have left the cohort,
     * or relationships that have come from open metadata archives.
     *
     * @param userId unique identifier for requesting server.
     * @param relationship the purged relationship.
     * @throws FunctionNotSupportedException the repository does not support reference copies of instances.
     */
    @Override
    public  void purgeRelationshipReferenceCopy(String       userId,
                                                Relationship relationship) throws FunctionNotSupportedException
    {
        final String  methodName = "purgeRelationshipReferenceCopy";

        throwNotEnterpriseFunction(methodName);
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
     * @throws FunctionNotSupportedException the repository does not support reference copies of instances.
     */
    @Override
    public void purgeRelationshipReferenceCopy(String userId,
                                               String relationshipGUID,
                                               String typeDefGUID,
                                               String typeDefName,
                                               String homeMetadataCollectionId) throws FunctionNotSupportedException
    {
        final String    methodName = "purgeRelationshipReferenceCopy";

        throwNotEnterpriseFunction(methodName);
    }


    /**
     * The local server has requested that the repository that hosts the home metadata collection for the
     * specified relationship sends out the details of this relationship so the local repository can create a
     * reference copy.
     *
     * @param userId unique identifier for requesting user.
     * @param relationshipGUID unique identifier of the relationship
     * @param typeDefGUID the guid of the TypeDef for the relationship used to verify the relationship identity.
     * @param typeDefName the name of the TypeDef for the relationship used to verify the relationship identity.
     * @param homeMetadataCollectionId unique identifier for the home repository for this relationship.
     * @throws FunctionNotSupportedException the repository does not support reference copies of instances.
     */
    @Override
    public void refreshRelationshipReferenceCopy(String userId,
                                                 String relationshipGUID,
                                                 String typeDefGUID,
                                                 String typeDefName,
                                                 String homeMetadataCollectionId) throws FunctionNotSupportedException
    {
        final String    methodName = "refreshRelationshipReferenceCopy";

        throwNotEnterpriseFunction(methodName);
    }


    /*
     * =================================================
     * Private validation and processing methods
     */


    /**
     * Build a combined list of entities.
     *
     * @param accumulatedResults current accumulated entities
     * @param results newly received list of entities
     * @param serverName name of the server that provided the new entity
     * @param metadataCollectionId unique identifier for metadata collection that provided the new entity
     * @param methodName method name that returned the new entity
     * @return combined results
     */
    private Map<String, EntityDetail> addUniqueEntities(Map<String, EntityDetail> accumulatedResults,
                                                        List<EntityDetail>        results,
                                                        String                    serverName,
                                                        String                    metadataCollectionId,
                                                        String                    methodName)
    {
        Map<String, EntityDetail> combinedResults = new HashMap<>(accumulatedResults);

        if (results != null)
        {
            for (EntityDetail returnedEntity : results)
            {
                combinedResults = this.addUniqueEntity(combinedResults,
                                                       returnedEntity,
                                                       serverName,
                                                       metadataCollectionId,
                                                       methodName);
            }
        }

        return combinedResults;
    }


    /**
     * Build a combined list of entities.
     *
     * @param accumulatedResults current accumulated entities
     * @param entity newly received entity
     * @param serverName name of the server that provided the new entity
     * @param metadataCollectionId unique identifier for metadata collection that provided the new entity
     * @param methodName method name that returned the new entity
     * @return combined results
     */
    private Map<String, EntityDetail> addUniqueEntity(Map<String, EntityDetail> accumulatedResults,
                                                      EntityDetail              entity,
                                                      String                    serverName,
                                                      String                    metadataCollectionId,
                                                      String                    methodName)
    {
        Map<String, EntityDetail> combinedResults = new HashMap<>(accumulatedResults);

        if (entity != null)
        {
            EntityDetail existingEntity = combinedResults.put(entity.getGUID(), entity);

            // todo validate that existing entity and the new one are copies
        }

        return combinedResults;
    }


    /**
     * Build a combined list of relationships.
     *
     * @param accumulatedResults current accumulated relationships
     * @param results newly received list of relationships
     * @param serverName name of the server that provided the new relationship
     * @param metadataCollectionId unique identifier for metadata collection that provided the new relationship
     * @param methodName method name that returned the new relationship
     * @return combined results
     */
    private Map<String, Relationship> addUniqueRelationships(Map<String, Relationship> accumulatedResults,
                                                             List<Relationship>        results,
                                                             String                    serverName,
                                                             String                    metadataCollectionId,
                                                             String                    methodName)
    {
        Map<String, Relationship> combinedResults = new HashMap<>(accumulatedResults);

        if (results != null)
        {
            for (Relationship returnedRelationship : results)
            {
                combinedResults = this.addUniqueRelationship(combinedResults,
                                                             returnedRelationship,
                                                             serverName,
                                                             metadataCollectionId,
                                                             methodName);
            }
        }

        return combinedResults;
    }


    /**
     * Build a combined list of relationships.
     *
     * @param accumulatedResults current accumulated relationships
     * @param relationship newly received relationship
     * @param serverName name of the server that provided the new relationship
     * @param metadataCollectionId unique identifier for metadata collection that provided the new relationship
     * @param methodName method name that returned the new relationship
     * @return combined results
     */
    private Map<String, Relationship> addUniqueRelationship(Map<String, Relationship> accumulatedResults,
                                                            Relationship              relationship,
                                                            String                    serverName,
                                                            String                    metadataCollectionId,
                                                            String                    methodName)
    {
        Map<String, Relationship> combinedResults = new HashMap<>(accumulatedResults);

        if (relationship != null)
        {
            Relationship existingRelationship = combinedResults.put(relationship.getGUID(), relationship);

            // todo validate that existing relationship and the new one are copies
        }

        return combinedResults;
    }


    /**
     * Verify that a cohort member's metadata collection is not null.
     *
     * @param cohortMetadataCollection metadata collection
     * @param methodName name of method
     * @throws RepositoryErrorException null metadata collection
     */
    private void validateMetadataCollection(OMRSMetadataCollection cohortMetadataCollection,
                                            String                 methodName) throws RepositoryErrorException
    {
        /*
         * The cohort metadata collection should not be null.  It is in a real mess if this fails.
         */
        if (cohortMetadataCollection == null)
        {
            /*
             * A problem in the set-up of the metadata collection list.  Repository connectors implemented
             * with no metadata collection are tested for in the OMRSEnterpriseConnectorManager so something
             * else has gone wrong.
             */
            throw new RepositoryErrorException(OMRSErrorCode.NULL_ENTERPRISE_METADATA_COLLECTION.getMessageDefinition(),
                                               this.getClass().getName(),
                                               methodName);
        }
    }


    /**
     * Indicates to the caller that the method called is not supported by the enterprise connector.
     *
     * @param methodName name of the method that was called
     * @throws FunctionNotSupportedException resulting exception
     */
    private void throwNotEnterpriseFunction(String methodName) throws FunctionNotSupportedException
    {
        throw new FunctionNotSupportedException(OMRSErrorCode.ENTERPRISE_NOT_SUPPORTED.getMessageDefinition(methodName),
                                                this.getClass().getName(),
                                                methodName);
    }


    /**
     * Throw a InvalidParameterException if it was returned by one of the calls to a cohort connector.
     *
     * @param exception captured exception
     * @throws InvalidParameterException one of the parameters is invalid
     */
    private void throwCapturedInvalidParameterException(InvalidParameterException exception) throws InvalidParameterException
    {
        if (exception != null)
        {
            throw exception;
        }
    }


    /**
     * Throw a TypeErrorException if it was returned by one of the calls to a cohort connector.
     *
     * @param exception captured exception
     * @throws TypeErrorException the type definition of the instance is not known in any of the federated repositories
     */
    private void throwCapturedTypeErrorException(TypeErrorException exception) throws TypeErrorException
    {
        if (exception != null)
        {
            throw exception;
        }
    }


    /**
     * Throw a PropertyErrorException if it was returned by one of the calls to a cohort connector.
     *
     * @param exception captured exception
     * @throws PropertyErrorException the properties are not valid for the call
     */
    private void throwCapturedPropertyErrorException(PropertyErrorException exception) throws PropertyErrorException
    {
        if (exception != null)
        {
            throw exception;
        }
    }


    /**
     * Throw a EntityNotKnownException if it was returned by one of the calls to a cohort connector.
     *
     * @param exception captured exception
     * @throws EntityNotKnownException the entity is not known in any of the federated repositories
     */
    private void throwCapturedEntityNotKnownException(EntityNotKnownException exception) throws EntityNotKnownException
    {
        if (exception != null)
        {
            throw exception;
        }
    }


    /**
     * Throw a FunctionNotSupportedException if it was returned by all the calls to the cohort connectors.
     *
     * @param exception captured exception
     * @throws FunctionNotSupportedException the requested function is not supported in any of the federated repositories
     */
    private void throwCapturedFunctionNotSupportedException(FunctionNotSupportedException exception) throws FunctionNotSupportedException
    {
        if (exception != null)
        {
            throw exception;
        }
    }


    /**
     * Throw a UserNotAuthorizedException if it was returned by one of the calls to a cohort connector.
     *
     * @param exception captured exception
     * @throws UserNotAuthorizedException the userId is not authorized in the server
     */
    private void throwCapturedUserNotAuthorizedException(UserNotAuthorizedException exception) throws UserNotAuthorizedException
    {
        if (exception != null)
        {
            throw exception;
        }
    }


    /**
     * Throw a RepositoryErrorException if it was returned by one of the calls to a cohort connector.
     *
     * @param exception captured exception
     * @throws RepositoryErrorException there was an error in the repository
     */
    private void throwCapturedRepositoryErrorException(RepositoryErrorException exception) throws RepositoryErrorException
    {
        if (exception != null)
        {
            throw exception;
        }
    }


    /**
     * Throw a RepositoryErrorException if an unexpected exception was returned by one of the calls
     * to a cohort connector.
     *
     * @param exception captured exception
     * @param methodName calling method
     * @throws RepositoryErrorException there was an unexpected error in the repository
     */
    private void throwCapturedGenericException(Exception exception,
                                               String    methodName) throws RepositoryErrorException
    {
        if (exception != null)
        {
            throw new RepositoryErrorException(OMRSErrorCode.UNEXPECTED_EXCEPTION_FROM_COHORT.getMessageDefinition(exception.getClass().getName(),
                                                                                                                   methodName,
                                                                                                                   exception.getMessage()),
                                               this.getClass().getName(),
                                               methodName);
        }
    }


    /**
     * Return a validated InstanceGraph.
     *
     * @param repositoryName name of this repository
     * @param accumulatedEntityResults list of returned entities
     * @param accumulatedRelationshipResults list of returned relationships
     * @param userNotAuthorizedException captured exception
     * @param propertyErrorException captured exception
     * @param functionNotSupportedException captured exception
     * @param entityNotKnownException captured exception
     * @param repositoryErrorException captured exception
     * @param anotherException captured generic exception
     * @param methodName name of calling method
     * @return InstanceGraph
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     * @throws PropertyErrorException issue with a property value
     * @throws FunctionNotSupportedException the repository does not support the requested method
     * @throws EntityNotKnownException the requested entity is not known in the metadata collection
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     * the metadata collection is stored.
     */
    private InstanceGraph validatedInstanceGraphResults(String                        repositoryName,
                                                        Map<String, EntityDetail>     accumulatedEntityResults,
                                                        Map<String, Relationship>     accumulatedRelationshipResults,
                                                        UserNotAuthorizedException    userNotAuthorizedException,
                                                        PropertyErrorException        propertyErrorException,
                                                        FunctionNotSupportedException functionNotSupportedException,
                                                        EntityNotKnownException       entityNotKnownException,
                                                        RepositoryErrorException      repositoryErrorException,
                                                        Exception                     anotherException,
                                                        String                        methodName) throws UserNotAuthorizedException,
                                                                                                         PropertyErrorException,
                                                                                                         FunctionNotSupportedException,
                                                                                                         EntityNotKnownException,
                                                                                                         RepositoryErrorException
    {
        int  resultCount = (accumulatedEntityResults.size() + accumulatedRelationshipResults.size());

        /*
         * Return any results, or exception if nothing is found.
         */
        if (resultCount > 0)
        {
            InstanceGraph      instanceGraph = new InstanceGraph();
            List<EntityDetail> entityDetails = new ArrayList<>(accumulatedEntityResults.values());
            List<Relationship> relationships = new ArrayList<>(accumulatedRelationshipResults.values());

            // todo Validate the entities and relationships

            instanceGraph.setEntities(entityDetails);
            instanceGraph.setRelationships(relationships);

            return instanceGraph;
        }
        else
        {
            throwCapturedUserNotAuthorizedException(userNotAuthorizedException);
            throwCapturedRepositoryErrorException(repositoryErrorException);
            throwCapturedPropertyErrorException(propertyErrorException);
            throwCapturedGenericException(anotherException, methodName);
            throwCapturedFunctionNotSupportedException(functionNotSupportedException);
            throwCapturedEntityNotKnownException(entityNotKnownException);

            /*
             * Nothing went wrong, there are just no results.
             */
            return null;
        }
    }
}