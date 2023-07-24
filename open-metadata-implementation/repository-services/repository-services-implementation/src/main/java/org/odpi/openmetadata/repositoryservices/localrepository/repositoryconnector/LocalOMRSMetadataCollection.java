/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.localrepository.repositoryconnector;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollectionBase;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataDefaultRepositorySecurity;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OpenMetadataRepositorySecurity;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.HistorySequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchClassifications;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchProperties;
import org.odpi.openmetadata.repositoryservices.eventmanagement.OMRSRepositoryEventManager;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryValidator;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;
import org.odpi.openmetadata.repositoryservices.localrepository.OMRSLocalRepository;
import org.odpi.openmetadata.repositoryservices.localrepository.repositorycontentmanager.OMRSTypeDefManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

/**
 * LocalOMRSMetadataCollection provides a wrapper around the metadata collection for the real local repository.
 * Its role is to manage outbound repository events and audit logging/debug for the real local repository.
 */
public class LocalOMRSMetadataCollection extends OMRSMetadataCollectionBase
{
    private final OMRSMetadataCollection     realMetadataCollection;
    private final String                     localServerName;
    private final String                     localServerType;
    private final String                     localOrganizationName;
    private final boolean                    produceEventsForRealConnector;
    private final OMRSRepositoryEventManager outboundRepositoryEventProcessor;
    private final OMRSTypeDefManager         localTypeDefManager;
    private final OMRSLocalRepository        localRepository;

    /*
     * The security verifier is initialized with a null security verifier.
     */
    private OpenMetadataRepositorySecurity securityVerifier = new OMRSMetadataDefaultRepositorySecurity();


    /**
     * Constructor used by LocalOMRSRepositoryConnector
     *
     * @param parentConnector connector that this metadata collection supports.  The connector has the information
     *                        to call the metadata repository.
     * @param repositoryName name of the repository used for logging.
     * @param repositoryHelper class used to build type definitions and instances.
     * @param repositoryValidator class used to validate type definitions and instances.
     * @param metadataCollectionId unique Identifier of the metadata collection id.
     * @param localServerName name of the local server.
     * @param localServerType type of the local server.
     * @param localOrganizationName name of the organization that owns the local server.
     * @param realMetadataCollection metadata collection of the real local connector.
     * @param outboundRepositoryEventProcessor outbound event processor
     *                                         (maybe null if a repository event mapper is deployed).
     * @param produceEventsForRealConnector flag indicating whether the local connector should handle the outbound
     *                                      events for the real connector
     * @param typeDefManager manager of in-memory cache of type definitions (TypeDefs).
     */
     LocalOMRSMetadataCollection(LocalOMRSRepositoryConnector parentConnector,
                                 String                       repositoryName,
                                 OMRSRepositoryHelper         repositoryHelper,
                                 OMRSRepositoryValidator      repositoryValidator,
                                 String                       metadataCollectionId,
                                 String                       localServerName,
                                 String                       localServerType,
                                 String                       localOrganizationName,
                                 OMRSMetadataCollection       realMetadataCollection,
                                 OMRSRepositoryEventManager   outboundRepositoryEventProcessor,
                                 boolean                      produceEventsForRealConnector,
                                 OMRSTypeDefManager           typeDefManager)
    {
        /*
         * The super class manages the local metadata collection id.  This is a locally managed value.
         */
        super(parentConnector, repositoryName, repositoryHelper, repositoryValidator, metadataCollectionId);

        /*
         * Save the parent connector also as a local repository.
         */
        localRepository = parentConnector;

        /*
         * Save the metadata collection object for the real repository.  This is the metadata collection that
         * does all the work.  LocalOMRSMetadataCollection is just a wrapper for managing repository events
         * and debug and audit logging.
         */
        if (realMetadataCollection == null)
        {
            final String      actionDescription = "Local OMRS Metadata Collection Constructor";

            throw new OMRSLogicErrorException(OMRSErrorCode.NULL_LOCAL_METADATA_COLLECTION.getMessageDefinition(),
                                              this.getClass().getName(),
                                              actionDescription);
        }
        this.realMetadataCollection = realMetadataCollection;

        /*
         * Save the information needed to send repository events.
         */
        this.localServerName = localServerName;
        this.localServerType = localServerType;
        this.localOrganizationName = localOrganizationName;
        this.produceEventsForRealConnector = produceEventsForRealConnector;
        this.outboundRepositoryEventProcessor = outboundRepositoryEventProcessor;
        this.localTypeDefManager = typeDefManager;
    }


    /**
     * Set up a new security verifier (the handler runs with a default verifier until this
     * method is called).
     *
     * The security verifier provides authorization checks for access and maintenance
     * changes to open metadata.  Authorization checks are enabled through the
     * OpenMetadataServerSecurityConnector.
     *
     * @param securityVerifier new security verifier
     */
    void setSecurityVerifier(OpenMetadataRepositorySecurity securityVerifier)
    {
        if (securityVerifier != null)
        {
            this.securityVerifier = securityVerifier;
        }
    }


    /* ==============================
     * Group 2: Working with typedefs
     */


    /**
     * Verify that user is authorized to read typeDefs.  Each TypeDef is tested.  If it is not allowed, then
     * the exception is captured and the next TypeDef is tried.   A new list of TypeDefs is built up containing
     * the types that the user is allowed to see.  If this turns out to be none of them then the last exception
     * to saved is returned to the caller.
     *
     * @param userId calling user
     * @param typeDefs retrieved type definitions
     * @return list of validated types
     * @throws UserNotAuthorizedException not types can be accessed
     */
    private List<TypeDef> securityVerifyTypeDefList(String         userId,
                                                    List<TypeDef>  typeDefs) throws UserNotAuthorizedException
    {
        if (typeDefs != null)
        {
            List<TypeDef>              validatedTypeDefs = new ArrayList<>();
            UserNotAuthorizedException savedException = null;

            for (TypeDef  typeDef : typeDefs)
            {
                try
                {
                    securityVerifier.validateUserForTypeRead(userId, metadataCollectionName, typeDef);
                    validatedTypeDefs.add(typeDef);
                }
                catch (org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException  error)
                {
                    savedException = new UserNotAuthorizedException(error);
                }
            }

            if (validatedTypeDefs.isEmpty())
            {
                if (savedException != null)
                {
                    throw savedException;
                }
            }
            else
            {
                return validatedTypeDefs;
            }
        }

        return null;
    }


    /**
     * Verify that user is authorized to read attributeTypeDefs.  Each AttributeTypeDef is tested.  If it is not allowed, then
     * the exception is captured and the next AttributeTypeDef is tried.   A new list of AttributeTypeDefs is built up containing
     * the types that the user is allowed to see.  If this turns out to be none of them then the last exception
     * to saved is returned to the caller.
     *
     * @param userId calling user
     * @param attributeTypeDefs retrieved type definitions
     * @return list of validated types
     * @throws UserNotAuthorizedException not types can be accessed
     */
    private List<AttributeTypeDef> securityVerifyAttributeTypeDefList(String                  userId,
                                                                      List<AttributeTypeDef>  attributeTypeDefs) throws UserNotAuthorizedException
    {
        if (attributeTypeDefs != null)
        {
            List<AttributeTypeDef>     validatedAttributeTypeDefs = new ArrayList<>();
            UserNotAuthorizedException savedException = null;

            for (AttributeTypeDef  attributeTypeDef : attributeTypeDefs)
            {
                try
                {
                    securityVerifier.validateUserForTypeRead(userId, metadataCollectionName, attributeTypeDef);
                    validatedAttributeTypeDefs.add(attributeTypeDef);
                }
                catch (org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException  error)
                {
                    savedException = new UserNotAuthorizedException(error);
                }
            }

            if (validatedAttributeTypeDefs.isEmpty())
            {
                if (savedException != null)
                {
                    throw savedException;
                }
            }
            else
            {
                return validatedAttributeTypeDefs;
            }
        }

        return null;
    }


    /**
     * Returns the list of different types of metadata organized into two groups.  The first are the
     * attribute type definitions (AttributeTypeDefs).  These provide types for properties in full
     * type definitions.  Full type definitions (TypeDefs) describe types for entities, relationships
     * and classifications.
     *
     * @param userId unique identifier for requesting user.
     * @return TypeDefs Lists of different categories of TypeDefs.
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
         * Perform operation
         */
        TypeDefGallery  results = realMetadataCollection.getAllTypes(userId);

        if (results != null)
        {
            /*
             * Filter out types that the user is not allowed to see.
             */
            results.setTypeDefs(this.securityVerifyTypeDefList(userId, results.getTypeDefs()));
            results.setAttributeTypeDefs(this.securityVerifyAttributeTypeDefList(userId, results.getAttributeTypeDefs()));
        }

        return results;
    }


    /**
     * Returns a list of TypeDefs that have the specified name.  TypeDef names should be unique.  This
     * method allows wildcard character to be included in the name.  These are * (asterisk) for an arbitrary string of
     * characters and ampersand for an arbitrary character.
     *
     * @param userId unique identifier for requesting user.
     * @param name name of the TypeDefs to return (including wildcard characters).
     * @return TypeDefs list.
     * @throws InvalidParameterException the name of the TypeDef is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public TypeDefGallery findTypesByName(String      userId,
                                          String      name) throws InvalidParameterException,
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
        TypeDefGallery  results = realMetadataCollection.findTypesByName(userId, name);

        if (results != null)
        {
            /*
             * Filter out types that the user is not allowed to see.
             */
            results.setTypeDefs(this.securityVerifyTypeDefList(userId, results.getTypeDefs()));
            results.setAttributeTypeDefs(this.securityVerifyAttributeTypeDefList(userId, results.getAttributeTypeDefs()));
        }

        return results;
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
         * Perform operation and filter out all the types that the user is not allowed to see.
         */
        return this.securityVerifyTypeDefList(userId,
                                              realMetadataCollection.findTypeDefsByCategory(userId, category));
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
        return this.securityVerifyAttributeTypeDefList(userId,
                                                       realMetadataCollection.findAttributeTypeDefsByCategory(userId, category));
    }


    /**
     * Return the TypeDefs that have the properties matching the supplied match criteria.
     *
     * @param userId unique identifier for requesting user.
     * @param matchCriteria TypeDefProperties a list of property names.
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
        return this.securityVerifyTypeDefList(userId, realMetadataCollection.findTypeDefsByProperty(userId, matchCriteria));
    }


    /**
     * Return the types that are linked to the elements from the specified standard.
     *
     * @param userId unique identifier for requesting user.
     * @param standard name of the standard null means any.
     * @param organization name of the organization null means any.
     * @param identifier identifier of the element in the standard null means any.
     * @return TypeDefs list each entry in the list contains a typedef.  This is a structure
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
        super.typeDefExternalIDParameterValidation(userId, standard, organization, identifier, methodName);

        /*
         * Perform operation
         */
        return this.securityVerifyTypeDefList(userId, realMetadataCollection.findTypesByExternalID(userId, standard, organization, identifier));
    }


    /**
     * Return the TypeDefs that match the search criteria.
     *
     * @param userId unique identifier for requesting user.
     * @param searchCriteria String search criteria.
     * @return TypeDefs list each entry in the list contains a typedef.  This is a structure
     * describing the TypeDef's category and properties.
     * @throws InvalidParameterException the searchCriteria is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public List<TypeDef> searchForTypeDefs(String    userId,
                                           String    searchCriteria) throws InvalidParameterException,
                                                                            RepositoryErrorException,
                                                                            UserNotAuthorizedException
    {
        final String methodName                  = "searchForTypeDefs";
        final String searchCriteriaParameterName = "searchCriteria";

        /*
         * Validate parameters
         */
        super.typeDefSearchParameterValidation(userId, searchCriteria, searchCriteriaParameterName, methodName);

        /*
         * Perform operation
         */
        return this.securityVerifyTypeDefList(userId, realMetadataCollection.searchForTypeDefs(userId, searchCriteria));
    }


    /**
     * Return the TypeDef identified by the GUID.
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
        TypeDef typeDef = realMetadataCollection.getTypeDefByGUID(userId, guid);

        /*
         * Verify that this user is allowed to see this type.
         */
        if (typeDef != null)
        {
            try
            {
                securityVerifier.validateUserForTypeRead(userId, metadataCollectionName, typeDef);
            }
            catch (org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException error)
            {
                throw new UserNotAuthorizedException(error);
            }
        }

        return typeDef;
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
        AttributeTypeDef attributeTypeDef = realMetadataCollection.getAttributeTypeDefByGUID(userId, guid);

        /*
         * Verify that this user is allowed to see this type.
         */
        if (attributeTypeDef != null)
        {
            try
            {
                securityVerifier.validateUserForTypeRead(userId, metadataCollectionName, attributeTypeDef);
            }
            catch (org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException error)
            {
                throw new UserNotAuthorizedException(error);
            }
        }

        return attributeTypeDef;
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
        super.typeNameParameterValidation(userId, name, nameParameterName, methodName);

        /*
         * Perform operation
         */
        TypeDef typeDef = realMetadataCollection.getTypeDefByName(userId, name);

        /*
         * Verify that this user is allowed to see this type.
         */
        if (typeDef != null)
        {
            try
            {
                securityVerifier.validateUserForTypeRead(userId, metadataCollectionName, typeDef);
            }
            catch (org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException error)
            {
                throw new UserNotAuthorizedException(error);
            }
        }

        return typeDef;
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
                                                                              UserNotAuthorizedException
    {
        final String  methodName = "getAttributeTypeDefByName";
        final String  nameParameterName = "name";

        /*
         * Validate parameters
         */
        super.typeNameParameterValidation(userId, name, nameParameterName, methodName);

        /*
         * Perform operation
         */
        AttributeTypeDef attributeTypeDef = realMetadataCollection.getAttributeTypeDefByName(userId, name);

        /*
         * Verify that this user is allowed to see this type.
         */
        if (attributeTypeDef != null)
        {
            try
            {
                securityVerifier.validateUserForTypeRead(userId, metadataCollectionName, attributeTypeDef);
            }
            catch (org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException error)
            {
                throw new UserNotAuthorizedException(error);
            }
        }

        return attributeTypeDef;
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
    public void addTypeDef(String    userId,
                           TypeDef   newTypeDef) throws InvalidParameterException,
                                                        RepositoryErrorException,
                                                        TypeDefNotSupportedException,
                                                        TypeDefKnownException,
                                                        TypeDefConflictException,
                                                        InvalidTypeDefException,
                                                        FunctionNotSupportedException,
                                                        UserNotAuthorizedException
    {
        final String  methodName = "addTypeDef";
        final String  typeDefParameterName = "newTypeDef";

        /*
         * Validate parameters
         */
        super.newTypeDefParameterValidation(userId, newTypeDef, typeDefParameterName, methodName);

        /*
         * Check the operation is allowed.
         */
        try
        {
            securityVerifier.validateUserForTypeCreate(userId, metadataCollectionName, newTypeDef);
        }
        catch (org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException  error)
        {
            throw new UserNotAuthorizedException(error);
        }

        /*
         * Perform operation
         */
        realMetadataCollection.addTypeDef(userId, newTypeDef);

        if (localTypeDefManager != null)
        {
            localTypeDefManager.addTypeDef(repositoryName, newTypeDef);
        }

        if (produceEventsForRealConnector)
        {
            outboundRepositoryEventProcessor.processNewTypeDefEvent(repositoryName,
                                                                    metadataCollectionId,
                                                                    localServerName,
                                                                    localServerType,
                                                                    localOrganizationName,
                                                                    newTypeDef);
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
                                                                                    UserNotAuthorizedException
    {
        final String  methodName           = "addAttributeTypeDef";
        final String  typeDefParameterName = "newAttributeTypeDef";

        /*
         * Validate parameters
         */
        super.newAttributeTypeDefParameterValidation(userId, newAttributeTypeDef, typeDefParameterName, methodName);

        /*
         * Check the operation is allowed.
         */
        try
        {
            securityVerifier.validateUserForTypeCreate(userId, metadataCollectionName, newAttributeTypeDef);
        }
        catch (org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException  error)
        {
            throw new UserNotAuthorizedException(error);
        }

        /*
         * Perform operation
         */
        realMetadataCollection.addAttributeTypeDef(userId, newAttributeTypeDef);

        if (localTypeDefManager != null)
        {
            localTypeDefManager.addAttributeTypeDef(repositoryName, newAttributeTypeDef);
        }

        if (produceEventsForRealConnector)
        {
            outboundRepositoryEventProcessor.processNewAttributeTypeDefEvent(repositoryName,
                                                                             metadataCollectionId,
                                                                             localServerName,
                                                                             localServerType,
                                                                             localOrganizationName,
                                                                             newAttributeTypeDef);
        }
    }


    /**
     * Verify that a definition of a TypeDef is either new or matches the definition already stored.
     *
     * @param userId unique identifier for requesting user.
     * @param typeDef TypeDef structure describing the TypeDef to test.
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
    public boolean verifyTypeDef(String    userId,
                                 TypeDef   typeDef) throws InvalidParameterException,
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
         * Perform operation
         */
        return realMetadataCollection.verifyTypeDef(userId, typeDef);
    }


    /**
     * Verify that a definition of an AttributeTypeDef is either new or matches the definition already stored.
     *
     * @param userId unique identifier for requesting user.
     * @param attributeTypeDef TypeDef structure describing the TypeDef to test.
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
        super.attributeTypeDefParameterValidation(userId, attributeTypeDef, typeDefParameterName, methodName);

        /*
         * Perform operation
         */
        return realMetadataCollection.verifyAttributeTypeDef(userId, attributeTypeDef);
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
        final String  methodName = "updateTypeDef";

        /*
         * Validate parameters
         */
        TypeDef typeDef = super.updateTypeDefParameterValidation(userId, typeDefPatch, methodName);

        /*
         * Check the operation is allowed.
         */
        try
        {
            securityVerifier.validateUserForTypeUpdate(userId, metadataCollectionName, typeDef, typeDefPatch);
        }
        catch (org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException  error)
        {
            throw new UserNotAuthorizedException(error);
        }

        /*
         * Perform operation
         */
        TypeDef   updatedTypeDef = realMetadataCollection.updateTypeDef(userId, typeDefPatch);

        if (localTypeDefManager != null)
        {
            localTypeDefManager.updateTypeDef(repositoryName, updatedTypeDef);
        }

        if (produceEventsForRealConnector)
        {
            outboundRepositoryEventProcessor.processUpdatedTypeDefEvent(repositoryName,
                                                                        metadataCollectionId,
                                                                        localServerName,
                                                                        localServerType,
                                                                        localOrganizationName,
                                                                        typeDefPatch);
        }

        return updatedTypeDef;
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
     * @throws TypeDefInUseException the TypeDef can not be deleted because there are instances of this type in
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
        TypeDef typeDef = super.manageTypeDefParameterValidation(userId,
                                                                 guidParameterName,
                                                                 nameParameterName,
                                                                 obsoleteTypeDefGUID,
                                                                 obsoleteTypeDefName,
                                                                 methodName);

        /*
         * Check the operation is allowed.
         */
        try
        {
            securityVerifier.validateUserForTypeDelete(userId, metadataCollectionName, typeDef);
        }
        catch (org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException  error)
        {
            throw new UserNotAuthorizedException(error);
        }

        /*
         * Perform operation
         */
        realMetadataCollection.deleteTypeDef(userId,
                                             obsoleteTypeDefGUID,
                                             obsoleteTypeDefName);

        if (localTypeDefManager != null)
        {
            localTypeDefManager.deleteTypeDef(repositoryName,
                                              obsoleteTypeDefGUID,
                                              obsoleteTypeDefName);
        }

        if (produceEventsForRealConnector)
        {
            outboundRepositoryEventProcessor.processDeletedTypeDefEvent(repositoryName,
                                                                        metadataCollectionId,
                                                                        localServerName,
                                                                        localServerType,
                                                                        localOrganizationName,
                                                                        obsoleteTypeDefGUID,
                                                                        obsoleteTypeDefName);
        }
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
     * @throws TypeDefInUseException the AttributeTypeDef can not be deleted because there are instances of this type in
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
        AttributeTypeDef attributeTypeDef = super.manageAttributeTypeDefParameterValidation(userId,
                                                                                            guidParameterName,
                                                                                            nameParameterName,
                                                                                            obsoleteTypeDefGUID,
                                                                                            obsoleteTypeDefName,
                                                                                            methodName);

        /*
         * Check the operation is allowed.
         */
        try
        {
            securityVerifier.validateUserForTypeDelete(userId, metadataCollectionName, attributeTypeDef);
        }
        catch (org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException  error)
        {
            throw new UserNotAuthorizedException(error);
        }

        /*
         * Perform operation
         */
        realMetadataCollection.deleteAttributeTypeDef(userId,
                                                      obsoleteTypeDefGUID,
                                                      obsoleteTypeDefName);

        if (localTypeDefManager != null)
        {
            localTypeDefManager.deleteAttributeTypeDef(repositoryName,
                                                       obsoleteTypeDefGUID,
                                                       obsoleteTypeDefName);
        }

        if (produceEventsForRealConnector)
        {
            outboundRepositoryEventProcessor.processDeletedAttributeTypeDefEvent(repositoryName,
                                                                                 metadataCollectionId,
                                                                                 localServerName,
                                                                                 localServerType,
                                                                                 localOrganizationName,
                                                                                 obsoleteTypeDefGUID,
                                                                                 obsoleteTypeDefName);
        }
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
        TypeDef originalTypeDef = super.manageTypeDefParameterValidation(userId,
                                                                         originalGUIDParameterName,
                                                                         originalNameParameterName,
                                                                         originalTypeDefGUID,
                                                                         originalTypeDefName,
                                                                         methodName);
        super.manageTypeDefParameterValidation(userId,
                                               newGUIDParameterName,
                                               newNameParameterName,
                                               newTypeDefGUID,
                                               newTypeDefName,
                                               methodName);

        /*
         * Check the operation is allowed.
         */
        try
        {
            securityVerifier.validateUserForTypeReIdentify(userId, metadataCollectionName, originalTypeDef, newTypeDefGUID, newTypeDefName);
        }
        catch (org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException  error)
        {
            throw new UserNotAuthorizedException(error);
        }

        /*
         * Perform operation
         */
        TypeDef   newTypeDef = realMetadataCollection.reIdentifyTypeDef(userId,
                                                                        originalTypeDefGUID,
                                                                        originalTypeDefName,
                                                                        newTypeDefGUID,
                                                                        newTypeDefName);

        if (localTypeDefManager != null)
        {
            localTypeDefManager.reIdentifyTypeDef(repositoryName,
                                                  originalTypeDefGUID,
                                                  originalTypeDefName,
                                                  newTypeDef);
        }

        if (produceEventsForRealConnector)
        {
            outboundRepositoryEventProcessor.processReIdentifiedTypeDefEvent(repositoryName,
                                                                             metadataCollectionId,
                                                                             localServerName,
                                                                             localServerType,
                                                                             localOrganizationName,
                                                                             originalTypeDef,
                                                                             newTypeDef);
        }

        return newTypeDef;
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
        AttributeTypeDef originalAttributeTypeDef = super.manageAttributeTypeDefParameterValidation(userId,
                                                                                                    originalGUIDParameterName,
                                                                                                    originalNameParameterName,
                                                                                                    originalAttributeTypeDefGUID,
                                                                                                    originalAttributeTypeDefName,
                                                                                                    methodName);
        super.manageAttributeTypeDefParameterValidation(userId,
                                                        newGUIDParameterName,
                                                        newNameParameterName,
                                                        newAttributeTypeDefGUID,
                                                        newAttributeTypeDefName,
                                                        methodName);


        /*
         * Check the operation is allowed.
         */
        try
        {
            securityVerifier.validateUserForTypeReIdentify(userId,
                                                           metadataCollectionName,
                                                           originalAttributeTypeDef,
                                                           newAttributeTypeDefGUID,
                                                           newAttributeTypeDefName);
        }
        catch (org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException  error)
        {
            throw new UserNotAuthorizedException(error);
        }

        /*
         * Perform operation
         */
        AttributeTypeDef   newAttributeTypeDef = realMetadataCollection.reIdentifyAttributeTypeDef(userId,
                                                                                                   originalAttributeTypeDefGUID,
                                                                                                   originalAttributeTypeDefName,
                                                                                                   newAttributeTypeDefGUID,
                                                                                                   newAttributeTypeDefName);

        if (localTypeDefManager != null)
        {
            localTypeDefManager.reIdentifyAttributeTypeDef(repositoryName,
                                                           originalAttributeTypeDefGUID,
                                                           originalAttributeTypeDefName,
                                                           newAttributeTypeDef);
        }

        if (produceEventsForRealConnector)
        {
            outboundRepositoryEventProcessor.processReIdentifiedAttributeTypeDefEvent(repositoryName,
                                                                                      metadataCollectionId,
                                                                                      localServerName,
                                                                                      localServerType,
                                                                                      localOrganizationName,
                                                                                      originalAttributeTypeDef,
                                                                                      newAttributeTypeDef);
        }

        return newAttributeTypeDef;
    }


    /* ===================================================
     * Group 3: Locating entity and relationship instances
     */


    /**
     * Ensure the provenance of any returned instance is correctly set.  A repository may not support the storing of
     * the metadata collection id in the repository (or uses null to mean "local").  When the instance
     * is sent out, it must have its home metadata collection id set up.  So LocalOMRSMetadataCollection
     * fixes up the provenance.
     *
     * @param instance instance returned from the real repository
     */
    private void  setLocalProvenance(InstanceAuditHeader   instance)
    {
        if (instance != null)
        {
            /*
             * Ensure the provenance of any returned instance is correctly set.  A repository may not support the storing of
             * the metadata collection id in the repository (or uses null to mean "local").  When the instance
             * is sent out, it must have its home metadata collection id set up.  So LocalOMRSMetadataCollection
             * fixes up the provenance.
             */
            if (instance.getMetadataCollectionId() == null)
            {
                instance.setMetadataCollectionId(metadataCollectionId);
                instance.setMetadataCollectionName(metadataCollectionName);
                instance.setInstanceProvenanceType(InstanceProvenanceType.LOCAL_COHORT);
            }

            if (instance.getInstanceProvenanceType() == InstanceProvenanceType.EXTERNAL_SOURCE)
            {
                if (instance.getReplicatedBy() == null)
                {
                    instance.setReplicatedBy(metadataCollectionId);
                }
            }

            if (instance.getInstanceProvenanceType() == InstanceProvenanceType.LOCAL_COHORT)
            {
                if (! localRepository.isActiveCohortMember(instance.getMetadataCollectionId()))
                {
                    instance.setInstanceProvenanceType(InstanceProvenanceType.DEREGISTERED_REPOSITORY);
                }
            }

            if (instance.getMetadataCollectionName() == null)
            {
                if (metadataCollectionId.equals(instance.getMetadataCollectionId()))
                {
                    instance.setMetadataCollectionName(metadataCollectionName);
                }
            }
        }
    }


    /**
     * Ensure the provenance of any returned instance is correctly set.  A repository may not support the storing of
     * the metadata collection id in the repository (or uses null to mean "local").  When the instance
     * is sent out, it must have its home metadata collection id set up.  So LocalOMRSMetadataCollection
     * fixes up the provenance.
     *
     * @param entityClassifications instance returned from the real repository
     */
    private void  setLocalProvenanceInEntityClassifications(List<Classification>   entityClassifications)
    {
        if (entityClassifications != null)
        {
            for (Classification homeClassification : entityClassifications)
            {
                if (homeClassification != null)
                {
                    setLocalProvenance(homeClassification);
                }
            }
        }
    }


    /**
     * Ensure the provenance of any returned instance is correctly set.  A repository may not support the storing of
     * the metadata collection id in the repository (or uses null to mean "local").  When the instance
     * is sent out, it must have its home metadata collection id set up.  So LocalOMRSMetadataCollection
     * fixes up the provenance.
     *
     * @param entity instance returned from the real repository
     */
    private void  setLocalProvenanceThroughoutEntity(EntityDetail   entity)
    {
        if (entity != null)
        {
            setLocalProvenance(entity);
            setLocalProvenanceInEntityClassifications(entity.getClassifications());
        }
    }



    /**
     * Ensure the provenance of any returned instance is correctly set.  A repository may not support the storing of
     * the metadata collection id in the repository (or uses null to mean "local").  When the instance
     * is sent out, it must have its home metadata collection id set up.  So LocalOMRSMetadataCollection
     * fixes up the provenance.
     *
     * @param relationship instance returned from the real repository
     */
    private void  setLocalProvenanceThroughoutRelationship(Relationship   relationship)
    {
        if (relationship != null)
        {
            setLocalProvenance(relationship);

            /*
             * Ensure that for each EntityProxy, the home metadataCollectionId and metadataCollectionName are set.
             * Note that these proxies are clones - so we need to set back into the relationship when modified.
             */
            EntityProxy endOneProxy = relationship.getEntityOneProxy();
            setLocalProvenance(endOneProxy);
            relationship.setEntityOneProxy(endOneProxy);

            EntityProxy endTwoProxy = relationship.getEntityTwoProxy();
            setLocalProvenance(endTwoProxy);
            relationship.setEntityTwoProxy(endTwoProxy);
        }
    }


    /**
     * Review a results list and set up the local provenance information if necessary.
     *
     * @param instanceList instances returned from the real repository.
     * @return validated list (or null)
     */
    private List<EntityDetail> setLocalProvenanceInEntityList(List<EntityDetail>   instanceList)
    {
        if ((instanceList == null) || (instanceList.isEmpty()))
        {
            return null;
        }
        else
        {
            List<EntityDetail>   resultList = new ArrayList<>();

            for (EntityDetail   entity : instanceList)
            {
                if (entity != null)
                {
                    setLocalProvenanceThroughoutEntity(entity);
                    resultList.add(entity);
                }
            }

            return resultList;
        }
    }


    /**
     * Review a results list and set up the local provenance information if necessary.
     *
     * @param instanceList instances returned from the real repository.
     * @return validated list (or null)
     */
    private List<Relationship> setLocalProvenanceInRelationshipList(List<Relationship>   instanceList)
    {
        if ((instanceList == null) || (instanceList.isEmpty()))
        {
            return null;
        }
        else
        {
            List<Relationship>   resultList = new ArrayList<>();

            for (Relationship   relationship : instanceList)
            {
                if (relationship != null)
                {
                    setLocalProvenanceThroughoutRelationship(relationship);
                    resultList.add(relationship);
                }
            }

            return resultList;
        }
    }


    /**
     * Review a results list with the security verifier.
     *
     * @param userId calling user
     * @param instanceList instances returned from the real repository.
     * @return validated list (or null)
     * @throws UserNotAuthorizedException use can nt read this instance
     */
    private List<EntityDetail> securityVerifyReadEntityList(String               userId,
                                                            List<EntityDetail>   instanceList) throws UserNotAuthorizedException
    {
        if ((instanceList == null) || (instanceList.isEmpty()))
        {
            return null;
        }
        else
        {
            List<EntityDetail> resultList = new ArrayList<>();

            for (EntityDetail entity : instanceList)
            {
                EntityDetail validatedEntity = this.getValidatedEntity(userId, entity);

                if (validatedEntity != null)
                {
                    resultList.add(validatedEntity);
                }
            }

            return resultList;
        }
    }


    /**
     * Review a results list with the security verifier.
     *
     * @param userId calling user
     * @param instanceList instances returned from the real repository.
     * @return validated list (or null)
     * @throws UserNotAuthorizedException use can nt read this instance
     */
    private List<Relationship> securityVerifyReadRelationshipList(String               userId,
                                                                  List<Relationship>   instanceList) throws UserNotAuthorizedException
    {
        if ((instanceList == null) || (instanceList.isEmpty()))
        {
            return null;
        }
        else
        {
            List<Relationship> resultList = new ArrayList<>();

            for (Relationship relationship : instanceList)
            {
                Relationship validatedRelationship = this.getValidatedRelationship(userId, relationship);

                if (validatedRelationship != null)
                {
                    resultList.add(validatedRelationship);
                }
            }

            return resultList;
        }
    }


    /**
     * Set up the local provenance for an element retrieved from the local repository and call the security verifier
     * to determine if the relationship should be retrieved.
     *
     * @param userId calling user
     * @param retrievedEntity relationship retrieved from the real repository
     * @return validated relationship or null (which means pretend this was not retrieved)
     * @throws UserNotAuthorizedException the security connector prevented access to the relationship
     */
    private EntityDetail getValidatedEntity(String       userId,
                                            EntityDetail retrievedEntity) throws UserNotAuthorizedException
    {
        if (retrievedEntity != null)
        {
            setLocalProvenanceThroughoutEntity(retrievedEntity);
            setLocalProvenanceInEntityClassifications(retrievedEntity.getClassifications());

            try
            {
                return securityVerifier.validateUserForEntityRead(userId, metadataCollectionName, retrievedEntity);
            }
            catch (org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException error)
            {
                throw new UserNotAuthorizedException(error);
            }
        }
        else
        {
            return null;
        }
    }


    /**
     * Set up the local provenance for an element retrieved from the local repository and call the security verifier
     * to determine if the relationship should be retrieved.
     *
     * @param userId calling user
     * @param retrievedRelationship relationship retrieved from the real repository
     * @return validated relationship or null (which means pretend this was not retrieved)
     * @throws UserNotAuthorizedException the security connector prevented access to the relationship
     */
    private Relationship getValidatedRelationship(String       userId,
                                                  Relationship retrievedRelationship) throws UserNotAuthorizedException
    {
        if (retrievedRelationship != null)
        {
            setLocalProvenanceThroughoutRelationship(retrievedRelationship);

            try
            {
                return securityVerifier.validateUserForRelationshipRead(userId, metadataCollectionName, retrievedRelationship);
            }
            catch (org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException error)
            {
                throw new UserNotAuthorizedException(error);
            }
        }
        else
        {
            return null;
        }
    }


    /**
     * Review the contents of an instance graph returned from the real repository and set up the local provenance
     * information if necessary.
     *
     * @param instanceGraph graph from real repository
     * @return validated graph
     */
    private InstanceGraph  setLocalProvenanceInGraph(InstanceGraph   instanceGraph)
    {
        /*
         * Always return a graph, even if empty
         */
        InstanceGraph  resultGraph = new InstanceGraph();

        if (instanceGraph != null)
        {
            resultGraph.setEntities(setLocalProvenanceInEntityList(instanceGraph.getEntities()));
            resultGraph.setRelationships(setLocalProvenanceInRelationshipList(instanceGraph.getRelationships()));
        }

        return resultGraph;
    }


    /**
     * Review the contents of an instance graph returned from the real repository and set up the local provenance
     * information if necessary.
     *
     * @param userId calling userId
     * @param instanceGraph graph from real repository
     * @return validated graph
     * @throws UserNotAuthorizedException not able to retrieve types
     */
    private InstanceGraph  securityVerifyReadGraph(String          userId,
                                                   InstanceGraph   instanceGraph) throws UserNotAuthorizedException
    {
        InstanceGraph              resultGraph = new InstanceGraph();

        if (instanceGraph != null)
        {
            UserNotAuthorizedException savedException = null;

            try
            {
                resultGraph.setEntities(securityVerifyReadEntityList(userId, instanceGraph.getEntities()));
            } catch (UserNotAuthorizedException error)
            {
                savedException = error;
            }

            try
            {
                resultGraph.setRelationships(securityVerifyReadRelationshipList(userId, instanceGraph.getRelationships()));
            } catch (UserNotAuthorizedException error)
            {
                savedException = error;
            }

            if ((resultGraph.getEntities() == null) && (resultGraph.getRelationships() == null))
            {
                if (savedException != null)
                {
                    throw savedException;
                }
            }

        }
        return resultGraph;
    }


    /**
     * Returns the entity if the entity is stored in the metadata collection, otherwise null.
     * Notice that entities in DELETED state are returned by this call.
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
    public EntityDetail isEntityKnown(String userId,
                                      String guid) throws InvalidParameterException,
                                                          RepositoryErrorException,
                                                          UserNotAuthorizedException
    {
        final String methodName = "isEntityKnown";

        /*
         * Validate parameters
         */
        super.getInstanceParameterValidation(userId, guid, methodName);

        /*
         * Perform operation
         */
        EntityDetail entity = realMetadataCollection.isEntityKnown(userId, guid);

        return this.getValidatedEntity(userId, entity);
    }


    /**
     * Return the header and classifications for a specific entity.  The returned entity summary may be from
     * a full entity object or an entity proxy.
     *
     * @param userId unique identifier for requesting user.
     * @param guid String unique identifier for the entity
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
         * Perform operation
         */
        EntitySummary entity = realMetadataCollection.getEntitySummary(userId, guid);

        repositoryValidator.validateEntityFromStore(repositoryName, guid, entity, methodName);

        if (entity != null)
        {
            setLocalProvenance(entity);
            setLocalProvenanceInEntityClassifications(entity.getClassifications());

            /*
             * Check operation is allowed
             */
            try
            {
                securityVerifier.validateUserForEntitySummaryRead(userId, metadataCollectionName, entity);
            }
            catch (org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException error)
            {
                throw new UserNotAuthorizedException(error);
            }
        }

        return entity;
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
         * Perform operation
         */
        EntityDetail   entity = realMetadataCollection.getEntityDetail(userId, guid);

        repositoryValidator.validateEntityFromStore(repositoryName, guid, entity, methodName);

        return this.getValidatedEntity(userId, entity);
    }


    /**
     * Return a historical version of an entity.  This includes the header, classifications and properties of the entity.
     *
     * @param userId unique identifier for requesting user.
     * @param guid String unique identifier for the entity.
     * @param asOfTime the time used to determine which version of the entity that is desired.
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
    public  EntityDetail getEntityDetail(String    userId,
                                         String    guid,
                                         Date      asOfTime) throws InvalidParameterException,
                                                                    RepositoryErrorException,
                                                                    EntityNotKnownException,
                                                                    EntityProxyOnlyException,
                                                                    FunctionNotSupportedException,
                                                                    UserNotAuthorizedException
    {
        final String methodName = "getEntityDetail";

        /*
         * Validate parameters
         */
        super.getInstanceParameterValidation(userId, guid, asOfTime, methodName);

        /*
         * Perform operation
         */
        EntityDetail entity = realMetadataCollection.getEntityDetail(userId, guid, asOfTime);

        repositoryValidator.validateEntityFromStore(repositoryName, guid, entity, methodName);

        return this.getValidatedEntity(userId, entity);
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
     * @throws EntityNotKnownException the requested entity instance is not known in the metadata collection
     *                                   at the time requested.
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
        final String  methodName        = "getEntityDetailHistory";

        /*
         * Validate parameters
         */
        this.getInstanceHistoryParameterValidation(userId, guid, fromTime, toTime, methodName);

        /*
         * Perform operation
         */
        List<EntityDetail> history = realMetadataCollection.getEntityDetailHistory(userId, guid, fromTime, toTime, startFromElement, pageSize, sequencingOrder);

        return this.securityVerifyReadEntityList(userId, setLocalProvenanceInEntityList(history));
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
     * @param limitResultsByStatus By default, relationships in all non-DELETED statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                             status values except DELETED.
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
        List<Relationship>  resultList = realMetadataCollection.getRelationshipsForEntity(userId,
                                                                                          entityGUID,
                                                                                          relationshipTypeGUID,
                                                                                          fromRelationshipElement,
                                                                                          limitResultsByStatus,
                                                                                          asOfTime,
                                                                                          sequencingProperty,
                                                                                          sequencingOrder,
                                                                                          pageSize);

        return this.securityVerifyReadRelationshipList(userId, setLocalProvenanceInRelationshipList(resultList));
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
     * @param limitResultsByStatus By default, entities in all non-DELETED statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                             status values except DELETED.
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
    public List<EntityDetail> findEntitiesByProperty(String                    userId,
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
                                                                                                RepositoryErrorException,
                                                                                                TypeErrorException,
                                                                                                PropertyErrorException,
                                                                                                PagingErrorException,
                                                                                                FunctionNotSupportedException,
                                                                                                UserNotAuthorizedException
    {
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


        List<EntityDetail> resultList;

        /*
         * Perform operation
         */
        resultList = realMetadataCollection.findEntitiesByProperty(userId,
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


        return this.securityVerifyReadEntityList(userId, setLocalProvenanceInEntityList(resultList));
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
     * @param limitResultsByStatus By default, entities in all non-DELETED statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                             status values except DELETED.
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
    public List<EntityDetail> findEntities(String                    userId,
                                           String                    entityTypeGUID,
                                           List<String>              entitySubtypeGUIDs,
                                           SearchProperties          matchProperties,
                                           int                       fromEntityElement,
                                           List<InstanceStatus>      limitResultsByStatus,
                                           SearchClassifications     matchClassifications,
                                           Date                      asOfTime,
                                           String                    sequencingProperty,
                                           SequencingOrder           sequencingOrder,
                                           int                       pageSize) throws InvalidParameterException,
                                                                                      RepositoryErrorException,
                                                                                      TypeErrorException,
                                                                                      PropertyErrorException,
                                                                                      PagingErrorException,
                                                                                      FunctionNotSupportedException,
                                                                                      UserNotAuthorizedException
    {
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
         * Perform operation
         */
        List<EntityDetail> resultList;

        /*
         * Perform operation
         */
        resultList = realMetadataCollection.findEntities(userId,
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

        return this.securityVerifyReadEntityList(userId, setLocalProvenanceInEntityList(resultList));
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
     * @param limitResultsByStatus By default, entities in all non-DELETED statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                             status values except DELETED.
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
         * Perform operation.
         */
        List<EntityDetail> resultList;

        resultList = realMetadataCollection.findEntitiesByClassification(userId,
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

        return this.securityVerifyReadEntityList(userId, setLocalProvenanceInEntityList(resultList));
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
     * @param limitResultsByStatus By default, entities in all non-DELETED statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                             status values except DELETED.
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
         * Process operation
         */
        List<EntityDetail> resultList = realMetadataCollection.findEntitiesByPropertyValue(userId,
                                                                                           entityTypeGUID,
                                                                                           searchCriteria,
                                                                                           fromEntityElement,
                                                                                           limitResultsByStatus,
                                                                                           limitResultsByClassification,
                                                                                           asOfTime,
                                                                                           sequencingProperty,
                                                                                           sequencingOrder,
                                                                                           pageSize);

        return this.securityVerifyReadEntityList(userId, setLocalProvenanceInEntityList(resultList));
    }


    /**
     * Returns a relationship indicating if the relationship is stored in the metadata collection.
     * Notice that relationships in DELETED state are returned by this call.
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
    public Relationship  isRelationshipKnown(String    userId,
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
         * Process operation
         */

        Relationship relationship = realMetadataCollection.isRelationshipKnown(userId, guid);

        return this.getValidatedRelationship(userId, relationship);
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
         * Process operation
         */
        Relationship relationship = realMetadataCollection.getRelationship(userId, guid);

        repositoryValidator.validateRelationshipFromStore(repositoryName, guid, relationship, methodName);

        return this.getValidatedRelationship(userId, relationship);
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
    public  Relationship getRelationship(String    userId,
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
         * Perform operation
         */
        Relationship relationship = realMetadataCollection.getRelationship(userId, guid, asOfTime);

        repositoryValidator.validateRelationshipFromStore(repositoryName, guid, relationship, methodName);

        return this.getValidatedRelationship(userId, relationship);
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
         * Perform operation
         */
        List<Relationship> history = realMetadataCollection.getRelationshipHistory(userId, guid, fromTime, toTime, startFromElement, pageSize, sequencingOrder);

        return this.securityVerifyReadRelationshipList(userId, setLocalProvenanceInRelationshipList(history));
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
     * @param limitResultsByStatus By default, relationships in all non-DELETED statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                             status values except DELETED.
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
    public  List<Relationship> findRelationships(String                    userId,
                                                 String                    relationshipTypeGUID,
                                                 List<String>              relationshipSubtypeGUIDs,
                                                 SearchProperties          matchProperties,
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
         * Perform operation
         */
        List<Relationship> resultList;

        resultList = realMetadataCollection.findRelationships(userId,
                                                              relationshipTypeGUID,
                                                              relationshipSubtypeGUIDs,
                                                              matchProperties,
                                                              fromRelationshipElement,
                                                              limitResultsByStatus,
                                                              asOfTime,
                                                              sequencingProperty,
                                                              sequencingOrder,
                                                              pageSize);

        return this.securityVerifyReadRelationshipList(userId, setLocalProvenanceInRelationshipList(resultList));
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
     * @param limitResultsByStatus By default, relationships in all non-DELETED statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                             status values except DELETED.
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
    public  List<Relationship> findRelationshipsByProperty(String                    userId,
                                                           String                    relationshipTypeGUID,
                                                           InstanceProperties matchProperties,
                                                           MatchCriteria matchCriteria,
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
         * Perform operation
         */
        List<Relationship> resultList;

        resultList = realMetadataCollection.findRelationshipsByProperty(userId,
                                                                        relationshipTypeGUID,
                                                                        matchProperties,
                                                                        matchCriteria,
                                                                        fromRelationshipElement,
                                                                        limitResultsByStatus,
                                                                        asOfTime,
                                                                        sequencingProperty,
                                                                        sequencingOrder,
                                                                        pageSize);

        return this.securityVerifyReadRelationshipList(userId, setLocalProvenanceInRelationshipList(resultList));
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
     * @param limitResultsByStatus By default, relationships in all non-DELETED statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                             status values except DELETED.
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

        /*
         * Perform operation
         */
        List<Relationship> resultList = realMetadataCollection.findRelationshipsByPropertyValue(userId,
                                                                                                relationshipTypeGUID,
                                                                                                searchCriteria,
                                                                                                fromRelationshipElement,
                                                                                                limitResultsByStatus,
                                                                                                asOfTime,
                                                                                                sequencingProperty,
                                                                                                sequencingOrder,
                                                                                                pageSize);

        return this.securityVerifyReadRelationshipList(userId, setLocalProvenanceInRelationshipList(resultList));
    }


    /**
     * Return all the relationships and intermediate entities that connect the startEntity with the endEntity.
     *
     * @param userId unique identifier for requesting user.
     * @param startEntityGUID The entity that is used to anchor the query.
     * @param endEntityGUID the other entity that defines the scope of the query.
     * @param limitResultsByStatus By default, relationships in all non-DELETED statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                             status values except DELETED.
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
    public InstanceGraph getLinkingEntities(String                    userId,
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
        /*
         * Validate parameters
         */
        super.getLinkingEntitiesParameterValidation(userId,
                                                    startEntityGUID,
                                                    endEntityGUID,
                                                    limitResultsByStatus,
                                                    asOfTime);

        /*
         * Perform operation
         */
        InstanceGraph resultGraph = realMetadataCollection.getLinkingEntities(userId,
                                                                              startEntityGUID,
                                                                              endEntityGUID,
                                                                              limitResultsByStatus,
                                                                              asOfTime);

        /*
         * Check result before return
         */
        return this.securityVerifyReadGraph(userId, setLocalProvenanceInGraph(resultGraph));
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
     * @param limitResultsByStatus By default, relationships in all non-DELETED statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                             status values except DELETED.
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
        this.validateRepositoryConnector(methodName);

        /*
         * Perform operation
         */
        InstanceGraph resultGraph = realMetadataCollection.getEntityNeighborhood(userId,
                                                                                 entityGUID,
                                                                                 entityTypeGUIDs,
                                                                                 relationshipTypeGUIDs,
                                                                                 limitResultsByStatus,
                                                                                 limitResultsByClassification,
                                                                                 asOfTime,
                                                                                 level);
        /*
         * Check result before return
         */
        return this.securityVerifyReadGraph(userId, setLocalProvenanceInGraph(resultGraph));
    }


    /**
     * Return the list of entities that are of the types listed in entityTypeGUIDs and are connected, either directly or
     * indirectly to the entity identified by startEntityGUID.
     *
     * @param userId unique identifier for requesting user.
     * @param startEntityGUID unique identifier of the starting entity
     * @param entityTypeGUIDs list of guids of types to search for.  Null means any type.
     * @param fromEntityElement starting element for results list.  Used in paging.  Zero means first element.
     * @param limitResultsByStatus By default, relationships in all non-DELETED statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                             status values except DELETED.
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
     * @throws TypeErrorException one of the requested type guids passed on the request is not known by the
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

        /*
         * Validate parameters
         */
        super.getRelatedEntitiesParameterValidation(userId,
                                                    startEntityGUID,
                                                    entityTypeGUIDs,
                                                    fromEntityElement,
                                                    limitResultsByStatus,
                                                    limitResultsByClassification,
                                                    asOfTime,
                                                    sequencingProperty,
                                                    sequencingOrder,
                                                    pageSize);
        this.validateRepositoryConnector(methodName);

        /*
         * Perform operation
         */
        List<EntityDetail>  resultList = realMetadataCollection.getRelatedEntities(userId,
                                                                                   startEntityGUID,
                                                                                   entityTypeGUIDs,
                                                                                   fromEntityElement,
                                                                                   limitResultsByStatus,
                                                                                   limitResultsByClassification,
                                                                                   asOfTime,
                                                                                   sequencingProperty,
                                                                                   sequencingOrder,
                                                                                   pageSize);

        return this.securityVerifyReadEntityList(userId, setLocalProvenanceInEntityList(resultList));
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
                                                                                   FunctionNotSupportedException,
                                                                                   UserNotAuthorizedException
    {
        final String  methodName  = "addEntity";

        /*
         * Validate parameters
         */
        super.addEntityParameterValidation(userId,
                                           entityTypeGUID,
                                           initialProperties,
                                           initialClassifications,
                                           initialStatus,
                                           methodName);


        /*
         * Check operation is allowed
         */
        try
        {
            securityVerifier.validateUserForEntityCreate(userId,
                                                         metadataCollectionName,
                                                         entityTypeGUID,
                                                         initialProperties,
                                                         initialClassifications,
                                                         initialStatus);
        }
        catch (org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException  error)
        {
            throw new UserNotAuthorizedException(error);
        }


        /*
         * Validation complete, ok to create new instance
         */
        EntityDetail   entity = realMetadataCollection.addEntity(userId,
                                                                 entityTypeGUID,
                                                                 initialProperties,
                                                                 initialClassifications,
                                                                 initialStatus);

        if (entity != null)
        {
            setLocalProvenanceThroughoutEntity(entity);

            /*
             * OK to send out
             */
            if (produceEventsForRealConnector)
            {
                outboundRepositoryEventProcessor.processNewEntityEvent(repositoryName,
                                                                       metadataCollectionId,
                                                                       localServerName,
                                                                       localServerType,
                                                                       localOrganizationName,
                                                                       entity);
            }
        }

        return entity;
    }



    /**
     * Save a new entity that is sourced from an external technology.  The external
     * technology is identified by a GUID and a name.  These can be recorded in a
     * Software Server Capability (guid and qualifiedName respectively).
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
    public EntityDetail addExternalEntity(String                userId,
                                          String                entityTypeGUID,
                                          String                externalSourceGUID,
                                          String                externalSourceName,
                                          InstanceProperties    initialProperties,
                                          List<Classification>  initialClassifications,
                                          InstanceStatus        initialStatus) throws InvalidParameterException,
                                                                                      RepositoryErrorException,
                                                                                      TypeErrorException,
                                                                                      PropertyErrorException,
                                                                                      ClassificationErrorException,
                                                                                      StatusNotSupportedException,
                                                                                      FunctionNotSupportedException,
                                                                                      UserNotAuthorizedException
    {
        final String  methodName = "addExternalEntity";

        /*
         * Validate parameters
         */
        super.addExternalEntityParameterValidation(userId,
                                                   entityTypeGUID,
                                                   externalSourceGUID,
                                                   initialProperties,
                                                   initialClassifications,
                                                   initialStatus,
                                                   methodName);

        /*
         * Check operation is allowed
         */
        try
        {
            securityVerifier.validateUserForEntityCreate(userId,
                                                         externalSourceName,
                                                         entityTypeGUID,
                                                         initialProperties,
                                                         initialClassifications,
                                                         initialStatus);
        }
        catch (org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException  error)
        {
            throw new UserNotAuthorizedException(error);
        }


        /*
         * Validation complete, ok to create new instance
         */
        EntityDetail   entity = realMetadataCollection.addExternalEntity(userId,
                                                                         entityTypeGUID,
                                                                         externalSourceGUID,
                                                                         externalSourceName,
                                                                         initialProperties,
                                                                         initialClassifications,
                                                                         initialStatus);

        if (entity != null)
        {
            setLocalProvenanceThroughoutEntity(entity);

            /*
             * OK to send out
             */
            if (produceEventsForRealConnector)
            {
                outboundRepositoryEventProcessor.processNewEntityEvent(repositoryName,
                                                                       metadataCollectionId,
                                                                       localServerName,
                                                                       localServerType,
                                                                       localOrganizationName,
                                                                       entity);
            }
        }

        return entity;
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
        /*
         * Validate parameters
         */
        super.addEntityProxyParameterValidation(userId, entityProxy);

        /*
         * Validation complete - note no security check as proxy used internally
         *
         * EntityProxies are used to store a relationship where the entity at one end of the relationship is
         * not stored locally.  Its type may not be supported locally either.
         */
        realMetadataCollection.addEntityProxy(userId, entityProxy);
    }

    /**
     * Validate the requested entity is suitable for a status update by this repository.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID unique identifier (guid) for the requested entity.
     * @param methodName calling method
     * @return current entity
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by the guid is not found in the metadata collection.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    private EntityDetail validateEntityCanBeUpdated(String           userId,
                                                    String           entityGUID,
                                                    String           methodName) throws InvalidParameterException,
                                                                                        RepositoryErrorException,
                                                                                        EntityNotKnownException,
                                                                                        UserNotAuthorizedException
    {

        /*
         * This method will check:
         *
         *   that the entity can be retrieved (it exists and the entity is not a proxy) -
         *      if the entity is not found this method will throw EntityNotKnownException
         *      if the entity is found but is a proxy this method will map the underlying EntityProxyOnlyException to InvalidParameterException
         *
         *   that if the entity originated in the local cohort, this repository is the master (it is not a reference copy)
         *      if the entity is from the local cohort but not locally mastered this method will throw InvalidParameterException
         *
         *   that if the entity originated externally, this repository is the replicator
         *      if the entity is from the outside the cohort but not replicated here this method will throw InvalidParameterException
         *
         *   if something really unexpected happens this method will throw RepositoryErrorException but not in any of the above scenarios
         */

        EntityDetail entityDetail = this.validateEntityIsNotProxy(userId, entityGUID, methodName);

        this.validateEntityCanBeUpdatedByRepository(entityGUID, entityDetail, methodName);

        return entityDetail;
    }


    /**
     * Validate the requested entity exists and is not a proxy.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID unique identifier (guid) for the requested entity.
     * @param methodName calling method
     * @return current entity
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by the guid is not found in the metadata collection.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    private EntityDetail validateEntityIsNotProxy(String           userId,
                                                  String           entityGUID,
                                                  String           methodName) throws InvalidParameterException,
                                                                                      RepositoryErrorException,
                                                                                      EntityNotKnownException,
                                                                                      UserNotAuthorizedException
    {
        EntityDetail entityDetail;

        try
        {
            entityDetail = realMetadataCollection.getEntityDetail(userId, entityGUID);
        }
        catch (EntityProxyOnlyException  error)
        {
            /*
             * There is a serious logic error as the entity stored in what should be the home repository
             * is only an entity proxy.
             */
            throw new InvalidParameterException(OMRSErrorCode.ENTITY_PROXY_IN_HOME.getMessageDefinition(repositoryName,
                                                                                                        entityGUID,
                                                                                                        methodName),
                                                this.getClass().getName(),
                                                methodName,
                                                error,
                                                "entityGUID");
        }

        return entityDetail;
    }


    /**
     * Validate the requested entity is not a proxy and may be updated, classified or deleted by this repository.
     *
     * @param entityGUID unique identifier (guid) for the requested entity.
     * @param entityDetail the requested entity.
     * @param methodName calling method
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws EntityNotKnownException the entity identified by the guid is not found in the metadata collection.
     */
    private void validateEntityCanBeUpdatedByRepository(String           entityGUID,
                                                        EntityDetail     entityDetail,
                                                        String           methodName) throws InvalidParameterException,
                                                                                            EntityNotKnownException
    {
        if (entityDetail != null)
        {
            if (entityDetail.getInstanceProvenanceType() == InstanceProvenanceType.LOCAL_COHORT)
            {
                if (metadataCollectionId.equals(entityDetail.getMetadataCollectionId()))
                {
                    return;
                }
            }
            else if (entityDetail.getInstanceProvenanceType() == InstanceProvenanceType.EXTERNAL_SOURCE)
            {
                if (metadataCollectionId.equals(entityDetail.getReplicatedBy()))
                {
                    return;
                }
            }

            /*
             * There is a logic error as the caller has requested update or delete of an entity and this
             * repository does not have the right to perform the requested operation.
             */
            String parameterName = "entityGUID";
            throw new InvalidParameterException(OMRSErrorCode.ENTITY_CAN_NOT_BE_UPDATED.getMessageDefinition(repositoryName,
                                                                                                             entityGUID,
                                                                                                             methodName),
                                                this.getClass().getName(),
                                                methodName,
                                                parameterName);
        }
        else
        {
            /*
             * There is a logic error as the caller has requested update of an entity but the
             * entity cannot be found.
             */
            throw new EntityNotKnownException(OMRSErrorCode.NULL_INSTANCE.getMessageDefinition(repositoryName,
                                                                                               entityGUID,
                                                                                               methodName),
                                              this.getClass().getName(),
                                              methodName);
        }
    }


    /**
     * Validate the requested relationship is suitable for update by this repository.
     *
     * @param userId unique identifier for requesting user.
     * @param relationshipGUID unique identifier (guid) for the requested entity.
     * @param methodName calling method
     * @return current relationship
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws RelationshipNotKnownException the relationship could not be found
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    private Relationship validateRelationshipCanBeUpdated(String           userId,
                                                          String           relationshipGUID,
                                                          String           methodName) throws InvalidParameterException,
                                                                                              RepositoryErrorException,
                                                                                              RelationshipNotKnownException,
                                                                                              UserNotAuthorizedException
    {

        /*
         * This method will check:
         *
         *   that the relationship can be retrieved (it exists) -
         *      if the relationship is not found this method will throw RelationshipNotKnownException
         *
         *   that if the relationship originated in the local cohort, this repository is the master (it is not a reference copy)
         *      if the relationship is from the local cohort but not locally mastered this method will throw InvalidParameterException
         *
         *   that if the relationship originated externally, this repository is the replicator
         *      if the relationship is from the outside the cohort but not replicated here this method will throw InvalidParameterException
         *
         *   if something really unexpected happens this method will throw RepositoryErrorException but not in any of the above scenarios
         */

        Relationship relationship;

        relationship = this.validateRelationshipCanBeUpdatedByRepository(relationshipGUID,
                                                                         realMetadataCollection.getRelationship(userId, relationshipGUID),
                                                                         methodName);


        return relationship;
    }


    /**
     * Validate the requested relationship may be updated or deleted by this repository.
     *
     * @param relationshipGUID unique identifier (guid) for the requested relationship.
     * @param relationship the requested relationship.
     * @param methodName calling method
     * @return current relationship
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RelationshipNotKnownException the relationship identified by the guid is not found in the metadata collection.
     * @throws RepositoryErrorException if the relationship identified is invalid
     */
    private Relationship validateRelationshipCanBeUpdatedByRepository(String           relationshipGUID,
                                                                      Relationship     relationship,
                                                                      String           methodName) throws InvalidParameterException,
                                                                                                          RelationshipNotKnownException,
                                                                                                          RepositoryErrorException
    {
        if (relationship != null)
        {
            repositoryValidator.validateRelationshipFromStore(repositoryName, relationshipGUID, relationship, methodName);
            if (relationship.getInstanceProvenanceType() == InstanceProvenanceType.LOCAL_COHORT)
            {
                if (metadataCollectionId.equals(relationship.getMetadataCollectionId()))
                {
                    return relationship;
                }
            }
            else if (relationship.getInstanceProvenanceType() == InstanceProvenanceType.EXTERNAL_SOURCE)
            {
                if (metadataCollectionId.equals(relationship.getReplicatedBy()))
                {
                    return relationship;
                }
            }

            /*
             * There is a logic error as the caller has requested update or delete of a relationship and this
             * repository does not have the right to perform the requested operation.
             */
            String  parameterName = "relationshipGUID";

            throw new InvalidParameterException(OMRSErrorCode.RELATIONSHIP_CAN_NOT_BE_UPDATED.getMessageDefinition(repositoryName,
                                                                                                                   relationshipGUID,
                                                                                                                   methodName),
                                                this.getClass().getName(),
                                                methodName,
                                                parameterName);
        }
        else
        {
            /*
             * There is a logic error as the caller has requested update of a relationship but the
             * relationship cannot be found.
             */
            throw new RelationshipNotKnownException(OMRSErrorCode.NULL_INSTANCE.getMessageDefinition(repositoryName,
                                                                                                     relationshipGUID,
                                                                                                     methodName),
                                                    this.getClass().getName(),
                                                    methodName);
        }
    }


    /**
     * Send out details of an updated entity.
     *
     * @param oldEntity original values
     * @param newEntity updated entity
     */
    private void notifyOfUpdatedEntity(EntityDetail     oldEntity,
                                       EntityDetail     newEntity)
    {
        if (newEntity != null)
        {
            if (produceEventsForRealConnector)
            {
                outboundRepositoryEventProcessor.processUpdatedEntityEvent(repositoryName,
                                                                           metadataCollectionId,
                                                                           localServerName,
                                                                           localServerType,
                                                                           localOrganizationName,
                                                                           oldEntity,
                                                                           newEntity);
            }
        }
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
    public EntityDetail updateEntityStatus(String           userId,
                                           String           entityGUID,
                                           InstanceStatus   newStatus) throws InvalidParameterException,
                                                                              RepositoryErrorException,
                                                                              EntityNotKnownException,
                                                                              StatusNotSupportedException,
                                                                              FunctionNotSupportedException,
                                                                              UserNotAuthorizedException
    {
        final String  methodName = "updateEntityStatus";

        /*
         * Validate parameters
         */
        super.updateInstanceStatusParameterValidation(userId, entityGUID, newStatus, methodName);

        /*
         * Locate entity and check it can be updated
         */
        EntityDetail currentEntity = this.validateEntityCanBeUpdated(userId, entityGUID, methodName);

        /*
         * Check operation is allowed
         */
        try
        {
            securityVerifier.validateUserForEntityUpdate(userId, metadataCollectionName, currentEntity);
        }
        catch (org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException  error)
        {
            throw new UserNotAuthorizedException(error);
        }

        /*
         * Now do the update
         */
        EntityDetail   newEntity = realMetadataCollection.updateEntityStatus(userId, entityGUID, newStatus);

        if (newEntity != null)
        {
            setLocalProvenanceThroughoutEntity(newEntity);
            notifyOfUpdatedEntity(currentEntity, newEntity);
        }

        return newEntity;
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
    public EntityDetail updateEntityProperties(String               userId,
                                               String               entityGUID,
                                               InstanceProperties   properties) throws InvalidParameterException,
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
        super.updateInstancePropertiesPropertyValidation(userId, entityGUID, properties, methodName);

        /*
         * Locate entity and check it can be updated
         */
        EntityDetail currentEntity = this.validateEntityCanBeUpdated(userId, entityGUID, methodName);

        /*
         * Check operation is allowed
         */
        try
        {
            securityVerifier.validateUserForEntityUpdate(userId, metadataCollectionName, currentEntity);
        }
        catch (org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException  error)
        {
            throw new UserNotAuthorizedException(error);
        }

        /*
         * Now do the update
         */
        EntityDetail   newEntity = realMetadataCollection.updateEntityProperties(userId, entityGUID, properties);

        if (newEntity != null)
        {
            setLocalProvenanceThroughoutEntity(newEntity);
            notifyOfUpdatedEntity(currentEntity, newEntity);
        }

        return newEntity;
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
    public EntityDetail undoEntityUpdate(String    userId,
                                         String    entityGUID) throws InvalidParameterException,
                                                                      RepositoryErrorException,
                                                                      EntityNotKnownException,
                                                                      FunctionNotSupportedException,
                                                                      UserNotAuthorizedException
    {
        final String methodName    = "undoEntityUpdate";
        final String parameterName = "entityGUID";

        /*
         * Validate parameters
         */
        this.manageInstanceParameterValidation(userId, entityGUID, parameterName, methodName);

        /*
         * Locate entity and check it can be updated
         */
        EntityDetail currentEntity = this.validateEntityCanBeUpdated(userId, entityGUID, methodName);

        /*
         * Check operation is allowed
         */
        try
        {
            securityVerifier.validateUserForEntityUpdate(userId, metadataCollectionName, currentEntity);
        }
        catch (org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException  error)
        {
            throw new UserNotAuthorizedException(error);
        }

        /*
         * Validation complete, ok to restore entity
         */
        EntityDetail   entity = realMetadataCollection.undoEntityUpdate(userId, entityGUID);

        if (entity != null)
        {
            setLocalProvenanceThroughoutEntity(entity);

            /*
             * OK to send out
             */
            if (produceEventsForRealConnector)
            {
                outboundRepositoryEventProcessor.processUndoneEntityEvent(repositoryName,
                                                                          metadataCollectionId,
                                                                          localServerName,
                                                                          localServerType,
                                                                          localOrganizationName,
                                                                          entity);
            }
        }

        return entity;
    }


    /**
     * Delete an entity.  The entity is soft-deleted.  This means it is still in the graph, but it is no longer returned
     * on queries.  All homed relationships to the entity are also soft-deleted and will no longer be usable, while any
     * reference copy relationships to the entity will be purged (and will no longer be accessible in this repository).
     * To completely eliminate the entity from the graph requires a call to the purgeEntity() method after the delete() call.
     * The restoreEntity() method will switch an entity back to Active status to restore the entity to normal use; however,
     * this will not restore any of the relationships that were soft-deleted as part of the original deleteEntity() call.
     *
     * @param userId unique identifier for requesting user.
     * @param typeDefGUID unique identifier of the type of the entity to delete.
     * @param typeDefName unique name of the type of the entity to delete.
     * @param obsoleteEntityGUID String unique identifier (guid) for the entity
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
        final String methodName    = "deleteEntity";
        final String parameterName = "obsoleteEntityGUID";

        /*
         * Validate parameters
         */
        this.manageInstanceParameterValidation(userId,
                                               typeDefGUID,
                                               typeDefName,
                                               obsoleteEntityGUID,
                                               parameterName,
                                               methodName);


        /*
         * Locate entity and check it can be updated
         */
        EntityDetail currentEntity = this.validateEntityCanBeUpdated(userId, obsoleteEntityGUID, methodName);

        /*
         * Check operation is allowed
         */
        try
        {
            securityVerifier.validateUserForEntityDelete(userId, metadataCollectionName, currentEntity);
        }
        catch (org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException  error)
        {
            throw new UserNotAuthorizedException(error);
        }


        /*
         * Delete Entity
         */
        EntityDetail entity = realMetadataCollection.deleteEntity(userId,
                                                                  typeDefGUID,
                                                                  typeDefName,
                                                                  obsoleteEntityGUID);

        if (entity != null)
        {

            if (produceEventsForRealConnector)
            {
                setLocalProvenanceThroughoutEntity(entity);

                outboundRepositoryEventProcessor.processDeletedEntityEvent(repositoryName,
                                                                           metadataCollectionId,
                                                                           localServerName,
                                                                           localServerType,
                                                                           localOrganizationName,
                                                                           entity);
            }
        }

        return entity;
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
    public void purgeEntity(String    userId,
                            String    typeDefGUID,
                            String    typeDefName,
                            String    deletedEntityGUID) throws InvalidParameterException,
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
         * Purge entity
         */
        EntityDetail entity = realMetadataCollection.isEntityKnown(userId, deletedEntityGUID);

        if (entity != null)
        {
            /*
             * Entity not already been soft-deleted. Check operation is allowed
             */
            this.validateEntityCanBeUpdatedByRepository(deletedEntityGUID, entity, methodName);

            try
            {
                securityVerifier.validateUserForEntityDelete(userId, metadataCollectionName, entity);
            }
            catch (org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException  error)
            {
                throw new UserNotAuthorizedException(error);
            }
        }

        realMetadataCollection.purgeEntity(userId,
                                           typeDefGUID,
                                           typeDefName,
                                           deletedEntityGUID);

        if (produceEventsForRealConnector)
        {
            if (entity == null)
            {
                outboundRepositoryEventProcessor.processPurgedEntityEvent(repositoryName,
                                                                          metadataCollectionId,
                                                                          localServerName,
                                                                          localServerType,
                                                                          localOrganizationName,
                                                                          typeDefGUID,
                                                                          typeDefName,
                                                                          deletedEntityGUID);
            }
            else /* no soft delete occurred before */
            {
                outboundRepositoryEventProcessor.processPurgedEntityEvent(repositoryName,
                                                                          metadataCollectionId,
                                                                          localServerName,
                                                                          localServerType,
                                                                          localOrganizationName,
                                                                          entity);

            }
        }
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
        final String methodName    = "restoreEntity";
        final String parameterName = "deletedEntityGUID";

        /*
         * Validate parameters
         */
        super.manageInstanceParameterValidation(userId, deletedEntityGUID, parameterName, methodName);

        /*
         * Check operation is allowed
         */
        try
        {
            securityVerifier.validateUserForEntityRestore(userId, metadataCollectionName, deletedEntityGUID);
        }
        catch (org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException  error)
        {
            throw new UserNotAuthorizedException(error);
        }

        /*
         * Restore entity
         */
        EntityDetail   entity = realMetadataCollection.restoreEntity(userId, deletedEntityGUID);

        if (entity != null)
        {
            setLocalProvenanceThroughoutEntity(entity);

            /*
             * OK to send out
             */
            if (produceEventsForRealConnector)
            {
                outboundRepositoryEventProcessor.processRestoredEntityEvent(repositoryName,
                                                                            metadataCollectionId,
                                                                            localServerName,
                                                                            localServerType,
                                                                            localOrganizationName,
                                                                            entity);
            }
        }

        return entity;
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
    public EntityDetail classifyEntity(String               userId,
                                       String               entityGUID,
                                       String               classificationName,
                                       InstanceProperties   classificationProperties) throws InvalidParameterException,
                                                                                             RepositoryErrorException,
                                                                                             EntityNotKnownException,
                                                                                             ClassificationErrorException,
                                                                                             PropertyErrorException,
                                                                                             FunctionNotSupportedException,
                                                                                             UserNotAuthorizedException
    {
        final String methodName = "classifyEntity";

        /*
         * Validate parameters
         */
        TypeDef typeDef = this.classifyEntityParameterValidation(userId, entityGUID, classificationName, classificationProperties, methodName);
        if (! repositoryValidator.isActiveType(repositoryName, typeDef.getGUID(), typeDef.getName()))
        {
            throw new ClassificationErrorException(OMRSErrorCode.UNSUPPORTED_CLASSIFICATION.getMessageDefinition(repositoryName, classificationName),
                                                   this.getClass().getName(),
                                                   methodName);
        }

        /*
         * Locate entity and check it can be updated
         */
        EntitySummary  currentEntity = this.getEntitySummary(userId, entityGUID);

        /*
         * Check operation is allowed
         */
        try
        {
            securityVerifier.validateUserForEntityClassificationAdd(userId,
                                                                    metadataCollectionName,
                                                                    currentEntity,
                                                                    classificationName,
                                                                    classificationProperties);
        }
        catch (org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException  error)
        {
            throw new UserNotAuthorizedException(error);
        }

        /*
         * Update entity
         */
        EntityDetail   entity = realMetadataCollection.classifyEntity(userId,
                                                                      entityGUID,
                                                                      classificationName,
                                                                      classificationProperties);

        if (entity != null)
        {
            setLocalProvenanceThroughoutEntity(entity);

            /*
             * OK to send out
             */
            if (produceEventsForRealConnector)
            {
                Classification newClassification = repositoryHelper.getClassificationFromEntity(repositoryName,
                                                                                                entity,
                                                                                                classificationName,
                                                                                                methodName);

                outboundRepositoryEventProcessor.processClassifiedEntityEvent(repositoryName,
                                                                              metadataCollectionId,
                                                                              localServerName,
                                                                              localServerType,
                                                                              localOrganizationName,
                                                                              entity,
                                                                              newClassification);
            }
        }

        return entity;
    }


    /**
     * Add the requested classification to a specific entity.
     *
     * @param userId unique identifier for requesting user.
     * @param entityProxy identifier (proxy) for the entity.
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
    public Classification classifyEntity(String               userId,
                                         EntityProxy          entityProxy,
                                         String               classificationName,
                                         InstanceProperties   classificationProperties) throws InvalidParameterException,
                                                                                               RepositoryErrorException,
                                                                                               EntityNotKnownException,
                                                                                               ClassificationErrorException,
                                                                                               PropertyErrorException,
                                                                                               FunctionNotSupportedException,
                                                                                               UserNotAuthorizedException
    {
        final String methodName = "classifyEntity";

        /*
         * Validate parameters
         */
        TypeDef typeDef = this.classifyEntityParameterValidation(userId, entityProxy, classificationName, classificationProperties, methodName);
        if (! repositoryValidator.isActiveType(repositoryName, typeDef.getGUID(), typeDef.getName()))
        {
            throw new ClassificationErrorException(OMRSErrorCode.UNSUPPORTED_CLASSIFICATION.getMessageDefinition(repositoryName, classificationName),
                                                   this.getClass().getName(),
                                                   methodName);
        }

        /*
         * Check operation is allowed
         */
        try
        {
            securityVerifier.validateUserForEntityClassificationAdd(userId,
                                                                    metadataCollectionName,
                                                                    entityProxy,
                                                                    classificationName,
                                                                    classificationProperties);
        }
        catch (org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException  error)
        {
            throw new UserNotAuthorizedException(error);
        }

        /*
         * Update entity
         */
        Classification classification = realMetadataCollection.classifyEntity(userId,
                                                                              entityProxy,
                                                                              classificationName,
                                                                              classificationProperties);

        if (classification != null)
        {
            setLocalProvenance(classification);

            /*
             * OK to send out
             */
            if (produceEventsForRealConnector)
            {
                outboundRepositoryEventProcessor.processClassifiedEntityEvent(repositoryName,
                                                                              metadataCollectionId,
                                                                              localServerName,
                                                                              localServerType,
                                                                              localOrganizationName,
                                                                              entityProxy,
                                                                              classification);
            }
        }

        return classification;
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
    public  EntityDetail classifyEntity(String               userId,
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
        final String  methodName = "classifyEntity (detailed)";

        /*
         * Validate parameters
         */
        TypeDef typeDef = this.classifyEntityParameterValidation(userId, entityGUID, classificationName, classificationProperties, methodName);
        if (! repositoryValidator.isActiveType(repositoryName, typeDef.getGUID(), typeDef.getName()))
        {
            throw new ClassificationErrorException(OMRSErrorCode.UNSUPPORTED_CLASSIFICATION.getMessageDefinition(repositoryName, classificationName),
                                                   this.getClass().getName(),
                                                   methodName);
        }

        /*
         * Locate entity and check it can be updated
         */
        EntitySummary  currentEntity = this.getEntitySummary(userId, entityGUID);

        /*
         * Check operation is allowed
         */
        try
        {
            securityVerifier.validateUserForEntityClassificationAdd(userId,
                                                                    metadataCollectionName,
                                                                    currentEntity,
                                                                    classificationName,
                                                                    classificationProperties);
        }
        catch (org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException  error)
        {
            throw new UserNotAuthorizedException(error);
        }

        /*
         * Update entity
         */
        EntityDetail   entity = realMetadataCollection.classifyEntity(userId,
                                                                      entityGUID,
                                                                      classificationName,
                                                                      externalSourceGUID,
                                                                      externalSourceName,
                                                                      classificationOrigin,
                                                                      classificationOriginGUID,
                                                                      classificationProperties);

        if (entity != null)
        {
            setLocalProvenanceThroughoutEntity(entity);

            /*
             * OK to send out
             */
            if (produceEventsForRealConnector)
            {
                Classification newClassification = repositoryHelper.getClassificationFromEntity(repositoryName,
                                                                                                entity,
                                                                                                classificationName,
                                                                                                methodName);

                outboundRepositoryEventProcessor.processClassifiedEntityEvent(repositoryName,
                                                                              metadataCollectionId,
                                                                              localServerName,
                                                                              localServerType,
                                                                              localOrganizationName,
                                                                              entity,
                                                                              newClassification);
            }
        }

        return entity;
    }


    /**
     * Add the requested classification to a specific entity.
     *
     * @param userId unique identifier for requesting user.
     * @param entityProxy identifier (proxy) for the entity.
     * @param classificationName String name for the classification.
     * @param externalSourceGUID unique identifier (guid) for the external source.
     * @param externalSourceName unique name for the external source.
     * @param classificationOrigin source of the classification
     * @param classificationOriginGUID if the classification is propagated, this is the unique identifier of the entity where
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
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     */
    @Override
    public  Classification classifyEntity(String               userId,
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
        final String  methodName = "classifyEntity (detailed)";

        /*
         * Validate parameters
         */
        TypeDef typeDef = this.classifyEntityParameterValidation(userId, entityProxy, classificationName, classificationProperties, methodName);
        if (! repositoryValidator.isActiveType(repositoryName, typeDef.getGUID(), typeDef.getName()))
        {
            throw new ClassificationErrorException(OMRSErrorCode.UNSUPPORTED_CLASSIFICATION.getMessageDefinition(repositoryName, classificationName),
                                                   this.getClass().getName(),
                                                   methodName);
        }

        /*
         * Check operation is allowed
         */
        try
        {
            securityVerifier.validateUserForEntityClassificationAdd(userId,
                                                                    metadataCollectionName,
                                                                    entityProxy,
                                                                    classificationName,
                                                                    classificationProperties);
        }
        catch (org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException  error)
        {
            throw new UserNotAuthorizedException(error);
        }

        /*
         * Update entity
         */
        Classification classification = realMetadataCollection.classifyEntity(userId,
                                                                              entityProxy,
                                                                              classificationName,
                                                                              externalSourceGUID,
                                                                              externalSourceName,
                                                                              classificationOrigin,
                                                                              classificationOriginGUID,
                                                                              classificationProperties);

        if (classification != null)
        {
            setLocalProvenance(classification);

            /*
             * OK to send out
             */
            if (produceEventsForRealConnector)
            {
                outboundRepositoryEventProcessor.processClassifiedEntityEvent(repositoryName,
                                                                              metadataCollectionId,
                                                                              localServerName,
                                                                              localServerType,
                                                                              localOrganizationName,
                                                                              entityProxy,
                                                                              classification);
            }
        }

        return classification;
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
    public EntityDetail declassifyEntity(String    userId,
                                         String    entityGUID,
                                         String    classificationName) throws InvalidParameterException,
                                                                              RepositoryErrorException,
                                                                              EntityNotKnownException,
                                                                              ClassificationErrorException,
                                                                              FunctionNotSupportedException,
                                                                              UserNotAuthorizedException
    {
        final String methodName = "declassifyEntity";

        /*
         * Validate parameters
         */
        this.declassifyEntityParameterValidation(userId, entityGUID, classificationName, methodName);

        /*
         * Locate entity and check it can be updated
         */
        EntitySummary  currentEntity = this.getEntitySummary(userId, entityGUID);

        Classification currentClassification = repositoryHelper.getClassificationFromEntity(repositoryName,
                                                                                            currentEntity,
                                                                                            classificationName,
                                                                                            methodName);
        /*
         * Check operation is allowed
         */
        try
        {
            securityVerifier.validateUserForEntityClassificationDelete(userId,
                                                                       metadataCollectionName,
                                                                       currentEntity,
                                                                       classificationName);
        }
        catch (org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException  error)
        {
            throw new UserNotAuthorizedException(error);
        }

        /*
         * Process entity
         */
        EntityDetail   entity = realMetadataCollection.declassifyEntity(userId,
                                                                        entityGUID,
                                                                        classificationName);

        if (entity != null)
        {
            setLocalProvenanceThroughoutEntity(entity);

            /*
             * OK to send out
             */
            if (produceEventsForRealConnector)
            {
                outboundRepositoryEventProcessor.processDeclassifiedEntityEvent(repositoryName,
                                                                                metadataCollectionId,
                                                                                localServerName,
                                                                                localServerType,
                                                                                localOrganizationName,
                                                                                entity,
                                                                                currentClassification);
            }
        }

        return entity;
    }


    /**
     * Remove a specific classification from an entity.
     *
     * @param userId unique identifier for requesting user.
     * @param entityProxy String unique identifier (guid) for the entity.
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
        final String methodName = "declassifyEntity (EntityProxy)";

        /*
         * Validate parameters
         */
        this.declassifyEntityParameterValidation(userId, entityProxy, classificationName, methodName);

        /*
         * Check operation is allowed
         */
        try
        {
            securityVerifier.validateUserForEntityClassificationDelete(userId,
                                                                       metadataCollectionName,
                                                                       entityProxy,
                                                                       classificationName);
        }
        catch (org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException  error)
        {
            throw new UserNotAuthorizedException(error);
        }

        /*
         * Process entity
         */
        Classification removedClassification = realMetadataCollection.declassifyEntity(userId,
                                                                                       entityProxy,
                                                                                       classificationName);

        setLocalProvenance(removedClassification);

        /*
         * OK to send out
         */
        if (produceEventsForRealConnector)
        {
            outboundRepositoryEventProcessor.processDeclassifiedEntityEvent(repositoryName,
                                                                            metadataCollectionId,
                                                                            localServerName,
                                                                            localServerType,
                                                                            localOrganizationName,
                                                                            entityProxy,
                                                                            removedClassification);
        }

        return removedClassification;
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
    public EntityDetail updateEntityClassification(String               userId,
                                                   String               entityGUID,
                                                   String               classificationName,
                                                   InstanceProperties   properties) throws InvalidParameterException,
                                                                                           RepositoryErrorException,
                                                                                           EntityNotKnownException,
                                                                                           ClassificationErrorException,
                                                                                           PropertyErrorException,
                                                                                           FunctionNotSupportedException,
                                                                                           UserNotAuthorizedException
    {
        final String methodName = "updateEntityClassification";

        /*
         * Validate parameters
         */
        this.classifyEntityParameterValidation(userId, entityGUID, classificationName, properties, methodName);

        /*
         * Locate entity and retrieve classification.
         */
        EntitySummary  currentEntity         = this.getEntitySummary(userId, entityGUID);
        Classification currentClassification = repositoryHelper.getClassificationFromEntity(repositoryName,
                                                                                            currentEntity,
                                                                                            classificationName,
                                                                                            methodName);
        /*
         * Check operation is allowed
         */
        try
        {
            securityVerifier.validateUserForEntityClassificationUpdate(userId,
                                                                       metadataCollectionName,
                                                                       currentEntity,
                                                                       classificationName,
                                                                       properties);
        }
        catch (org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException  error)
        {
            throw new UserNotAuthorizedException(error);
        }

        /*
         * Update entity
         */
        EntityDetail   entity = realMetadataCollection.updateEntityClassification(userId,
                                                                                  entityGUID,
                                                                                  classificationName,
                                                                                  properties);

        if (entity != null)
        {
            setLocalProvenanceThroughoutEntity(entity);

            /*
             * OK to send out
             */
            if (produceEventsForRealConnector)
            {
                Classification newClassification = repositoryHelper.getClassificationFromEntity(repositoryName,
                                                                                                entity,
                                                                                                classificationName,
                                                                                                methodName);
                outboundRepositoryEventProcessor.processReclassifiedEntityEvent(repositoryName,
                                                                                metadataCollectionId,
                                                                                localServerName,
                                                                                localServerType,
                                                                                localOrganizationName,
                                                                                entity,
                                                                                currentClassification,
                                                                                newClassification);
            }
        }

        return entity;
    }


    /**
     * Update one or more properties in one of an entity's classifications.
     *
     * @param userId unique identifier for requesting user.
     * @param entityProxy identifier (proxy) for the entity.
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
    public Classification updateEntityClassification(String               userId,
                                                     EntityProxy          entityProxy,
                                                     String               classificationName,
                                                     InstanceProperties   properties) throws InvalidParameterException,
                                                                                             RepositoryErrorException,
                                                                                             EntityNotKnownException,
                                                                                             ClassificationErrorException,
                                                                                             PropertyErrorException,
                                                                                             FunctionNotSupportedException,
                                                                                             UserNotAuthorizedException
    {
        final String methodName = "updateEntityClassification (EntityProxy)";

        /*
         * Validate parameters
         */
        this.classifyEntityParameterValidation(userId, entityProxy, classificationName, properties, methodName);

        /*
         * Check operation is allowed
         */
        try
        {
            securityVerifier.validateUserForEntityClassificationUpdate(userId,
                                                                       metadataCollectionName,
                                                                       entityProxy,
                                                                       classificationName,
                                                                       properties);
        }
        catch (org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException  error)
        {
            throw new UserNotAuthorizedException(error);
        }


        /*
         * Update entity
         */
        Classification newClassification = realMetadataCollection.updateEntityClassification(userId,
                                                                                             entityProxy,
                                                                                             classificationName,
                                                                                             properties);



        setLocalProvenance(newClassification);



        /*
         * OK to send out
         */
        if (produceEventsForRealConnector)
        {
            Classification currentClassification = repositoryHelper.getClassificationFromEntity(repositoryName,
                                                                                                entityProxy,
                                                                                                classificationName,
                                                                                                methodName);

            outboundRepositoryEventProcessor.processReclassifiedEntityEvent(repositoryName,
                                                                            metadataCollectionId,
                                                                            localServerName,
                                                                            localServerType,
                                                                            localOrganizationName,
                                                                            entityProxy,
                                                                            currentClassification,
                                                                            newClassification);
        }

        return newClassification;
    }


    /**
     * Add a new relationship between two entities to the metadata collection.
     *
     * @param userId unique identifier for requesting user.
     * @param relationshipTypeGUID unique identifier (guid) for the new relationship's type.
     * @param initialProperties initial list of properties for the new entity null means no properties.
     * @param entityOneGUID the unique identifier of one of the entities that the relationship is connecting together.
     * @param entityTwoGUID the unique identifier of the other entity that the relationship is connecting together.
     * @param initialStatus initial status typically DRAFT, PREPARED or ACTIVE.
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
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
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
                                                                                   FunctionNotSupportedException,
                                                                                   UserNotAuthorizedException
    {
        final String  methodName = "addRelationship";

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

        EntitySummary end1 = realMetadataCollection.getEntitySummary(userId, entityOneGUID);
        EntitySummary end2 = realMetadataCollection.getEntitySummary(userId, entityTwoGUID);

        /*
         * Check operation is allowed
         */
        try
        {
            securityVerifier.validateUserForRelationshipCreate(userId,
                                                               metadataCollectionName,
                                                               relationshipTypeGUID,
                                                               initialProperties,
                                                               end1,
                                                               end2,
                                                               initialStatus);
        }
        catch (org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException  error)
        {
            throw new UserNotAuthorizedException(error);
        }

        /*
         * Validation complete ok to create new instance
         */
        Relationship   relationship = realMetadataCollection.addRelationship(userId,
                                                                             relationshipTypeGUID,
                                                                             initialProperties,
                                                                             entityOneGUID,
                                                                             entityTwoGUID,
                                                                             initialStatus);

        if (relationship != null)
        {
            setLocalProvenanceThroughoutRelationship(relationship);

            /*
             * OK to send out
             */
            if (produceEventsForRealConnector)
            {
                outboundRepositoryEventProcessor.processNewRelationshipEvent(repositoryName,
                                                                             metadataCollectionId,
                                                                             localServerName,
                                                                             localServerType,
                                                                             localOrganizationName,
                                                                             relationship);
            }
        }

        return relationship;
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
    public Relationship addExternalRelationship(String               userId,
                                                String               relationshipTypeGUID,
                                                String               externalSourceGUID,
                                                String               externalSourceName,
                                                InstanceProperties   initialProperties,
                                                String               entityOneGUID,
                                                String               entityTwoGUID,
                                                InstanceStatus       initialStatus) throws InvalidParameterException,
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
        super.addExternalRelationshipParameterValidation(userId,
                                                         relationshipTypeGUID,
                                                         externalSourceGUID,
                                                         initialProperties,
                                                         entityOneGUID,
                                                         entityTwoGUID,
                                                         initialStatus,
                                                         methodName);

        EntitySummary end1 = realMetadataCollection.getEntitySummary(userId, entityOneGUID);
        EntitySummary end2 = realMetadataCollection.getEntitySummary(userId, entityTwoGUID);

        /*
         * Check operation is allowed
         */
        try
        {
            securityVerifier.validateUserForRelationshipCreate(userId,
                                                               metadataCollectionName,
                                                               relationshipTypeGUID,
                                                               initialProperties,
                                                               end1,
                                                               end2,
                                                               initialStatus);
        }
        catch (org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException  error)
        {
            throw new UserNotAuthorizedException(error);
        }

        /*
         * Validation complete ok to create new instance
         */

        Relationship   relationship = realMetadataCollection.addExternalRelationship(userId,
                                                                                     relationshipTypeGUID,
                                                                                     externalSourceGUID,
                                                                                     externalSourceName,
                                                                                     initialProperties,
                                                                                     entityOneGUID,
                                                                                     entityTwoGUID,
                                                                                     initialStatus);

        if (relationship != null)
        {
            setLocalProvenanceThroughoutRelationship(relationship);

            /*
             * OK to send out
             */
            if (produceEventsForRealConnector)
            {
                outboundRepositoryEventProcessor.processNewRelationshipEvent(repositoryName,
                                                                             metadataCollectionId,
                                                                             localServerName,
                                                                             localServerType,
                                                                             localOrganizationName,
                                                                             relationship);
            }
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
     * @throws StatusNotSupportedException invalid status for instance.
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public Relationship updateRelationshipStatus(String           userId,
                                                 String           relationshipGUID,
                                                 InstanceStatus   newStatus) throws InvalidParameterException,
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
        this.updateInstanceStatusParameterValidation(userId, relationshipGUID, newStatus, methodName);

        /*
         * Locate relationship
         */
        Relationship   currentRelationship = this.validateRelationshipCanBeUpdated(userId, relationshipGUID, methodName);

        /*
         * Check operation is allowed
         */
        try
        {
            securityVerifier.validateUserForRelationshipUpdate(userId,
                                                               metadataCollectionName,
                                                               currentRelationship);
        }
        catch (org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException  error)
        {
            throw new UserNotAuthorizedException(error);
        }

        /*
         * OK to perform operation
         */
        Relationship   newRelationship     = realMetadataCollection.updateRelationshipStatus(userId,
                                                                                             relationshipGUID,
                                                                                             newStatus);

        if (newRelationship != null)
        {
            setLocalProvenanceThroughoutRelationship(newRelationship);
            notifyOfUpdatedRelationship(currentRelationship, newRelationship);
        }

        return newRelationship;
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
    public Relationship updateRelationshipProperties(String               userId,
                                                     String               relationshipGUID,
                                                     InstanceProperties   properties) throws InvalidParameterException,
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
        this.updateInstancePropertiesPropertyValidation(userId, relationshipGUID, properties, methodName);

        /*
         * Locate relationship
         */
        Relationship   currentRelationship = this.validateRelationshipCanBeUpdated(userId, relationshipGUID, methodName);

        /*
         * Check operation is allowed
         */
        try
        {
            securityVerifier.validateUserForRelationshipUpdate(userId,
                                                               metadataCollectionName,
                                                               currentRelationship);
        }
        catch (org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException  error)
        {
            throw new UserNotAuthorizedException(error);
        }

        Relationship   newRelationship = realMetadataCollection.updateRelationshipProperties(userId,
                                                                                             relationshipGUID,
                                                                                             properties);

        if (newRelationship != null)
        {

            setLocalProvenanceThroughoutRelationship(newRelationship);
            notifyOfUpdatedRelationship(currentRelationship, newRelationship);
        }

        return newRelationship;
    }


    /**
     * Send out a notification that a relationship has changed.
     *
     * @param oldRelationship original relationship
     * @param newRelationship changed relationship
     */
    private void notifyOfUpdatedRelationship(Relationship  oldRelationship,
                                             Relationship  newRelationship)
    {
        if (newRelationship != null)
        {
            if (produceEventsForRealConnector)
            {
                outboundRepositoryEventProcessor.processUpdatedRelationshipEvent(repositoryName,
                                                                                 metadataCollectionId,
                                                                                 localServerName,
                                                                                 localServerType,
                                                                                 localOrganizationName,
                                                                                 oldRelationship,
                                                                                 newRelationship);
            }
        }
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
    public Relationship undoRelationshipUpdate(String    userId,
                                               String    relationshipGUID) throws InvalidParameterException,
                                                                                  RepositoryErrorException,
                                                                                  RelationshipNotKnownException,
                                                                                  FunctionNotSupportedException,
                                                                                  UserNotAuthorizedException
    {
        final String  methodName    = "undoRelationshipUpdate";
        final String  parameterName = "relationshipGUID";

        /*
         * Validate parameters
         */
        this.manageInstanceParameterValidation(userId, relationshipGUID, parameterName, methodName);

        /*
         * Locate relationship
         */
        Relationship   currentRelationship = this.validateRelationshipCanBeUpdated(userId, relationshipGUID, methodName);

        /*
         * Check operation is allowed
         */
        try
        {
            securityVerifier.validateUserForRelationshipUpdate(userId,
                                                               metadataCollectionName,
                                                               currentRelationship);
        }
        catch (org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException  error)
        {
            throw new UserNotAuthorizedException(error);
        }

        /*
         * Restore previous version
         */
        Relationship   newRelationship = realMetadataCollection.undoRelationshipUpdate(userId,
                                                                                       relationshipGUID);

        if (newRelationship != null)
        {
            setLocalProvenanceThroughoutRelationship(newRelationship);

            /*
             * OK to send out
             */
            if (produceEventsForRealConnector)
            {
                outboundRepositoryEventProcessor.processUndoneRelationshipEvent(repositoryName,
                                                                                metadataCollectionId,
                                                                                localServerName,
                                                                                localServerType,
                                                                                localOrganizationName,
                                                                                newRelationship);
            }
        }

        return newRelationship;
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
     * @return deleted relationship
     * @throws InvalidParameterException one of the parameters is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
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
        final String  methodName    = "deleteRelationship";
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
        Relationship   currentRelationship = this.validateRelationshipCanBeUpdated(userId, obsoleteRelationshipGUID, methodName);

        /*
         * Check operation is allowed
         */
        try
        {
            securityVerifier.validateUserForRelationshipDelete(userId,
                                                               metadataCollectionName,
                                                               currentRelationship);
        }
        catch (org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException  error)
        {
            throw new UserNotAuthorizedException(error);
        }

        /*
         * Delete relationship
         */
        Relationship newRelationship = realMetadataCollection.deleteRelationship(userId,
                                                                                 typeDefGUID,
                                                                                 typeDefName,
                                                                                 obsoleteRelationshipGUID);

        if (newRelationship != null)
        {

            if (produceEventsForRealConnector)
            {
                setLocalProvenanceThroughoutRelationship(newRelationship);

                outboundRepositoryEventProcessor.processDeletedRelationshipEvent(repositoryName,
                                                                                 metadataCollectionId,
                                                                                 localServerName,
                                                                                 localServerType,
                                                                                 localOrganizationName,
                                                                                 newRelationship);
            }
        }

        return newRelationship;
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
    public void purgeRelationship(String    userId,
                                  String    typeDefGUID,
                                  String    typeDefName,
                                  String    deletedRelationshipGUID) throws InvalidParameterException,
                                                                            RepositoryErrorException,
                                                                            RelationshipNotKnownException,
                                                                            RelationshipNotDeletedException,
                                                                            FunctionNotSupportedException,
                                                                            UserNotAuthorizedException
    {
        final String  methodName    = "purgeRelationship";
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
         * Purge relationship
         */
        Relationship relationship = realMetadataCollection.isRelationshipKnown(userId, deletedRelationshipGUID);
        if (relationship != null)
        {
            /*
             * This method will check:
             *
             *   that the relationship can be retrieved (it exists) -
             *      if the relationship is not found this method will throw RelationshipNotKnownException
             *
             *   that if the relationship originated in the local cohort, this repository is the master (it is not a reference copy)
             *      if the relationship is from the local cohort but not locally mastered this method will throw InvalidParameterException
             *
             *   that if the relationship originated externally, this repository is the replicator
             *      if the relationship is from the outside the cohort but not replicated here this method will throw InvalidParameterException
             *
             *   if something really unexpected happens this method will throw RepositoryErrorException but not in any of the above scenarios
             */
            relationship = this.validateRelationshipCanBeUpdatedByRepository(relationship.getGUID(), relationship, methodName);

            /*
             * Check operation is allowed
             */
            try
            {
                securityVerifier.validateUserForRelationshipDelete(userId,
                                                                   metadataCollectionName,
                                                                   relationship);
            }
            catch (org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException  error)
            {
                throw new UserNotAuthorizedException(error);
            }
        }

        realMetadataCollection.purgeRelationship(userId, typeDefGUID, typeDefName, deletedRelationshipGUID);

        if (produceEventsForRealConnector)
        {
            if (relationship == null)
            {
                outboundRepositoryEventProcessor.processPurgedRelationshipEvent(repositoryName,
                                                                                metadataCollectionId,
                                                                                localServerName,
                                                                                localServerType,
                                                                                localOrganizationName,
                                                                                typeDefGUID,
                                                                                typeDefName,
                                                                                deletedRelationshipGUID);
            }
            else
            {
                setLocalProvenanceThroughoutRelationship(relationship);

                outboundRepositoryEventProcessor.processPurgedRelationshipEvent(repositoryName,
                                                                                metadataCollectionId,
                                                                                localServerName,
                                                                                localServerType,
                                                                                localOrganizationName,
                                                                                relationship);

            }
        }
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
        final String  methodName    = "restoreRelationship";
        final String  parameterName = "deletedRelationshipGUID";

        /*
         * Validate parameters
         */
        this.manageInstanceParameterValidation(userId, deletedRelationshipGUID, parameterName, methodName);

        /*
         * Check operation is allowed
         */
        try
        {
            securityVerifier.validateUserForRelationshipRestore(userId,
                                                                metadataCollectionName,
                                                                deletedRelationshipGUID);
        }
        catch (org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException  error)
        {
            throw new UserNotAuthorizedException(error);
        }

        /*
         * Restore relationship
         */
        Relationship   newRelationship = realMetadataCollection.restoreRelationship(userId, deletedRelationshipGUID);

        if (newRelationship != null)
        {
            setLocalProvenanceThroughoutRelationship(newRelationship);

            /*
             * OK to send out
             */
            if (produceEventsForRealConnector)
            {
                outboundRepositoryEventProcessor.processRestoredRelationshipEvent(repositoryName,
                                                                                  metadataCollectionId,
                                                                                  localServerName,
                                                                                  localServerType,
                                                                                  localOrganizationName,
                                                                                  newRelationship);
            }
        }

        return newRelationship;
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
     * @param newEntityGUID the new guid for the entity.
     * @return entity new values for this entity, including the new guid.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by the guid is not found in the metadata collection.
     * @throws FunctionNotSupportedException the repository does not support the re-identification of instances.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public EntityDetail reIdentifyEntity(String    userId,
                                         String    typeDefGUID,
                                         String    typeDefName,
                                         String    entityGUID,
                                         String    newEntityGUID) throws InvalidParameterException,
                                                                         RepositoryErrorException,
                                                                         EntityNotKnownException,
                                                                         FunctionNotSupportedException,
                                                                         UserNotAuthorizedException
    {
        final String  methodName = "reIdentifyEntity";
        final String  instanceParameterName = "entityGUID";
        final String  newInstanceParameterName = "newEntityGUID";

        /*
         * Validate parameters
         */
        super.reIdentifyInstanceParameterValidation(userId,
                                                    typeDefGUID,
                                                    typeDefName,
                                                    entityGUID,
                                                    instanceParameterName,
                                                    newEntityGUID,
                                                    newInstanceParameterName,
                                                    methodName);

        /*
         * Locate entity and check it can be updated
         */
        EntityDetail currentEntity = this.validateEntityCanBeUpdated(userId, entityGUID, methodName);

        /*
         * Check operation is allowed
         */
        try
        {
            securityVerifier.validateUserForEntityReIdentification(userId, metadataCollectionName, currentEntity, newEntityGUID);
        }
        catch (org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException  error)
        {
            throw new UserNotAuthorizedException(error);
        }

        /*
         * Update entity
         */
        EntityDetail   entity = realMetadataCollection.reIdentifyEntity(userId,
                                                                        typeDefGUID,
                                                                        typeDefName,
                                                                        entityGUID,
                                                                        newEntityGUID);

        if (entity != null)
        {
            setLocalProvenanceThroughoutEntity(entity);

            /*
             * OK to send out
             */
            if (produceEventsForRealConnector)
            {
                outboundRepositoryEventProcessor.processReIdentifiedEntityEvent(repositoryName,
                                                                                metadataCollectionId,
                                                                                localServerName,
                                                                                localServerType,
                                                                                localOrganizationName,
                                                                                entityGUID,
                                                                                entity);
            }
        }

        return entity;
    }


    /**
     * Change the type an existing entity.  Typically, this action is taken to move an entity's
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
        final String  methodName                  = "reTypeEntity";
        final String  entityParameterName         = "entityGUID";

        /*
         * Validate parameters
         */
        super.reTypeInstanceParameterValidation(userId,
                                                entityGUID,
                                                entityParameterName,
                                                TypeDefCategory.ENTITY_DEF,
                                                currentTypeDefSummary,
                                                newTypeDefSummary,
                                                methodName);

        /*
         * Locate entity and check it can be updated
         */
        EntityDetail currentEntity = this.validateEntityCanBeUpdated(userId, entityGUID, methodName);

        /*
         * Check operation is allowed
         */
        try
        {
            securityVerifier.validateUserForEntityReTyping(userId, metadataCollectionName, currentEntity, newTypeDefSummary);
        }
        catch (org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException  error)
        {
            throw new UserNotAuthorizedException(error);
        }

        /*
         * Update entity
         */
        EntityDetail   entity = realMetadataCollection.reTypeEntity(userId,
                                                                    entityGUID,
                                                                    currentTypeDefSummary,
                                                                    newTypeDefSummary);

        if (entity != null)
        {
            setLocalProvenanceThroughoutEntity(entity);

            /*
             * OK to send out
             */
            if (produceEventsForRealConnector)
            {
                outboundRepositoryEventProcessor.processReTypedEntityEvent(repositoryName,
                                                                           metadataCollectionId,
                                                                           localServerName,
                                                                           localServerType,
                                                                           localOrganizationName,
                                                                           currentTypeDefSummary,
                                                                           entity);
            }
        }

        return entity;
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
     * @param homeMetadataCollectionId the identifier of the metadata collection where this entity currently is homed.
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
    public EntityDetail reHomeEntity(String    userId,
                                     String    entityGUID,
                                     String    typeDefGUID,
                                     String    typeDefName,
                                     String    homeMetadataCollectionId,
                                     String    newHomeMetadataCollectionId,
                                     String    newHomeMetadataCollectionName) throws InvalidParameterException,
                                                                                     RepositoryErrorException,
                                                                                     EntityNotKnownException,
                                                                                     FunctionNotSupportedException,
                                                                                     UserNotAuthorizedException
    {
        final String methodName           = "reHomeEntity";
        final String entityParameterName  = "entityGUID";
        final String newHomeParameterName = "newHomeMetadataCollectionId";

        /*
         * Validate parameters
         */
        super.reHomeInstanceParameterValidation(userId,
                                                entityGUID,
                                                entityParameterName,
                                                typeDefGUID,
                                                typeDefName,
                                                homeMetadataCollectionId,
                                                newHomeMetadataCollectionId,
                                                methodName);

        if (metadataCollectionId.equals(newHomeMetadataCollectionId))
        {
            /*
             * Locate entity and check it can be updated
             */
            EntityDetail currentEntity = this.validateEntityIsNotProxy(userId, entityGUID, methodName);
            /*
             * This method will check:
             *
             *   that the entity can be retrieved (it exists and the entity is not a proxy) -
             *      if the entity is not found this method will throw EntityNotKnownException
             *      if the entity is found but is a proxy this method will map the underlying EntityProxyOnlyException to InvalidParameterException
             *
             *   that if the entity originated in the local cohort, this repository is NOT the master (it is a reference copy)
             *      if the entity is from the local cohort and locally mastered this method will throw InvalidParameterException
             *
             *   that if the entity originated externally, this repository is NOT already the replicator
             *      if the entity is from the outside the cohort and replicated here this method will throw InvalidParameterException
             *
             *   if something really unexpected happens this method will throw RepositoryErrorException but not in any of the above scenarios
             */

            repositoryValidator.validateEntityCanBeRehomed(repositoryName, metadataCollectionId, currentEntity, methodName);

            /*
             * Check operation is allowed
             */
            try
            {
                securityVerifier.validateUserForEntityReHoming(userId,
                                                               metadataCollectionName,
                                                               currentEntity,
                                                               newHomeMetadataCollectionId,
                                                               newHomeMetadataCollectionName);
            }
            catch (org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException  error)
            {
                throw new UserNotAuthorizedException(error);
            }

            /*
             * Update entity
             */
            EntityDetail entity = realMetadataCollection.reHomeEntity(userId,
                                                                      entityGUID,
                                                                      typeDefGUID,
                                                                      typeDefName,
                                                                      homeMetadataCollectionId,
                                                                      newHomeMetadataCollectionId,
                                                                      newHomeMetadataCollectionName);

            if (entity != null)
            {
                /*
                 * Ensure the provenance of the entity is correctly set.  A repository may not support the storing of
                 * the metadata collection id in the repository (or uses null to mean "local").  When the entity
                 * detail is sent out, it must have its home metadata collection id set up.  So LocalOMRSMetadataCollection
                 * fixes up the provenance.
                 */
                if (entity.getMetadataCollectionId() == null)
                {
                    entity.setMetadataCollectionId(newHomeMetadataCollectionId);
                    entity.setInstanceProvenanceType(InstanceProvenanceType.LOCAL_COHORT);
                }

                if (entity.getMetadataCollectionName() == null)
                {
                    entity.setMetadataCollectionName(newHomeMetadataCollectionName);
                }

                /*
                 * OK to send out
                 */
                if (produceEventsForRealConnector)
                {
                    outboundRepositoryEventProcessor.processReHomedEntityEvent(repositoryName,
                                                                               metadataCollectionId,
                                                                               localServerName,
                                                                               localServerType,
                                                                               localOrganizationName,
                                                                               metadataCollectionId,
                                                                               entity);
                }
            }

            return entity;
        }

       throw new InvalidParameterException(OMRSErrorCode.NOT_FOR_LOCAL_COLLECTION.getMessageDefinition(entityGUID,
                                                                                                       typeDefName,
                                                                                                       typeDefGUID,
                                                                                                       newHomeMetadataCollectionId,
                                                                                                       metadataCollectionId),
                                            this.getClass().getName(),
                                            methodName,
                                            newHomeParameterName);
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
     * @param newRelationshipGUID the new identifier for the relationship.
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
    public Relationship reIdentifyRelationship(String    userId,
                                               String    typeDefGUID,
                                               String    typeDefName,
                                               String    relationshipGUID,
                                               String    newRelationshipGUID) throws InvalidParameterException,
                                                                                     RepositoryErrorException,
                                                                                     RelationshipNotKnownException,
                                                                                     FunctionNotSupportedException,
                                                                                     UserNotAuthorizedException
    {
        final String  methodName = "reIdentifyRelationship";
        final String  instanceParameterName = "relationshipGUID";
        final String  newInstanceParameterName = "newRelationshipGUID";

        /*
         * Validate parameters
         */
        super.reIdentifyInstanceParameterValidation(userId,
                                                    typeDefGUID,
                                                    typeDefName,
                                                    relationshipGUID,
                                                    instanceParameterName,
                                                    newRelationshipGUID,
                                                    newInstanceParameterName,
                                                    methodName);

        /*
         * Locate relationship and check it can be updated
         */
        Relationship currentRelationship = this.validateRelationshipCanBeUpdated(userId, relationshipGUID, methodName);

        /*
         * Check operation is allowed
         */
        try
        {
            securityVerifier.validateUserForRelationshipReIdentification(userId, metadataCollectionName, currentRelationship, newRelationshipGUID);
        }
        catch (org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException  error)
        {
            throw new UserNotAuthorizedException(error);
        }

        /*
         * Validation complete, ok to make changes
         */
        Relationship   relationship = realMetadataCollection.reIdentifyRelationship(userId,
                                                                                    typeDefGUID,
                                                                                    typeDefName,
                                                                                    relationshipGUID,
                                                                                    newRelationshipGUID);

        if (relationship != null)
        {
            setLocalProvenanceThroughoutRelationship(relationship);

            /*
             * OK to send out
             */
            if (produceEventsForRealConnector)
            {
                outboundRepositoryEventProcessor.processReIdentifiedRelationshipEvent(repositoryName,
                                                                                      metadataCollectionId,
                                                                                      localServerName,
                                                                                      localServerType,
                                                                                      localOrganizationName,
                                                                                      relationshipGUID,
                                                                                      relationship);
            }
        }

        return relationship;
    }


    /**
     * Change the type of the existing relationship.  Typically, this action is taken to move a relationship's
     * type to either a super type (so the subtype can be deleted) or a new subtype (so additional properties can be
     * added.)  However, the type can be changed to any compatible type.
     *
     * @param userId unique identifier for requesting user.
     * @param relationshipGUID the unique identifier for the relationship.
     * @param currentTypeDefSummary the details of the TypeDef for the relationship used to verify the relationship identity.
     * @param newTypeDefSummary new details for this relationship's TypeDef.
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

        /*
         * Validate parameters
         */
        super.reTypeInstanceParameterValidation(userId,
                                                relationshipGUID,
                                                relationshipParameterName,
                                                TypeDefCategory.RELATIONSHIP_DEF,
                                                currentTypeDefSummary,
                                                newTypeDefSummary,
                                                methodName);


        /*
         * Locate relationship and check it can be updated
         */
        Relationship currentRelationship = this.validateRelationshipCanBeUpdated(userId, relationshipGUID, methodName);

        /*
         * Check operation is allowed
         */
        try
        {
            securityVerifier.validateUserForRelationshipReTyping(userId, metadataCollectionName, currentRelationship, newTypeDefSummary);
        }
        catch (org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException  error)
        {
            throw new UserNotAuthorizedException(error);
        }

        /*
         * Validation complete, ok to make changes
         */
        Relationship   relationship = realMetadataCollection.reTypeRelationship(userId,
                                                                                relationshipGUID,
                                                                                currentTypeDefSummary,
                                                                                newTypeDefSummary);

        if (relationship != null)
        {
            setLocalProvenanceThroughoutRelationship(relationship);

            /*
             * OK to send out
             */
            if (produceEventsForRealConnector)
            {
                outboundRepositoryEventProcessor.processReTypedRelationshipEvent(repositoryName,
                                                                                 metadataCollectionId,
                                                                                 localServerName,
                                                                                 localServerType,
                                                                                 localOrganizationName,
                                                                                 currentTypeDefSummary,
                                                                                 relationship);
            }
        }

        return relationship;
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
    public Relationship reHomeRelationship(String    userId,
                                           String    relationshipGUID,
                                           String    typeDefGUID,
                                           String    typeDefName,
                                           String    homeMetadataCollectionId,
                                           String    newHomeMetadataCollectionId,
                                           String    newHomeMetadataCollectionName) throws InvalidParameterException,
                                                                                           RepositoryErrorException,
                                                                                           RelationshipNotKnownException,
                                                                                           FunctionNotSupportedException,
                                                                                           UserNotAuthorizedException
    {
        final String methodName                = "reHomeRelationship";
        final String relationshipParameterName = "relationshipGUID";
        final String newHomeParameterName      = "newHomeMetadataCollectionId";

        /*
         * Validate parameters
         */
        super.reHomeInstanceParameterValidation(userId,
                                                relationshipGUID,
                                                relationshipParameterName,
                                                typeDefGUID,
                                                typeDefName,
                                                homeMetadataCollectionId,
                                                newHomeMetadataCollectionId,
                                                methodName);

        if (metadataCollectionId.equals(newHomeMetadataCollectionId))
        {
            /*
             * Locate relationship and check it can be updated
             */
            Relationship currentRelationship = realMetadataCollection.getRelationship(userId, relationshipGUID);
            /*
             * This method will check:
             *
             *   that the relationship can be retrieved (it exists) -
             *      if the relationship is not found this method will throw RelationshipNotKnownException

             *   that if the relationship originated in the local cohort, this repository is NOT the master (it is a reference copy)
             *      if the relationship is from the local cohort and locally mastered this method will throw InvalidParameterException
             *
             *   that if the relationship originated externally, this repository is NOT already the replicator
             *      if the relationship is from the outside the cohort and replicated here this method will throw InvalidParameterException
             *
             *   if something really unexpected happens this method will throw RepositoryErrorException but not in any of the above scenarios
             */
            repositoryValidator.validateRelationshipCanBeRehomed(repositoryName, metadataCollectionId, currentRelationship, methodName);

            /*
             * Check operation is allowed
             */
            try
            {
                securityVerifier.validateUserForRelationshipReHoming(userId,
                                                                     metadataCollectionName,
                                                                     currentRelationship,
                                                                     newHomeMetadataCollectionId,
                                                                     newHomeMetadataCollectionName);
            }
            catch (org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException  error)
            {
                throw new UserNotAuthorizedException(error);
            }

            /*
             * Update relationship
             */
            Relationship relationship = realMetadataCollection.reHomeRelationship(userId,
                                                                                  relationshipGUID,
                                                                                  typeDefGUID,
                                                                                  typeDefName,
                                                                                  homeMetadataCollectionId,
                                                                                  newHomeMetadataCollectionId,
                                                                                  newHomeMetadataCollectionName);

            if (relationship != null)
            {
                /*
                 * Ensure the provenance of the entity is correctly set.  A repository may not support the storing of
                 * the metadata collection id in the repository (or uses null to mean "local").  When the entity
                 * detail is sent out, it must have its home metadata collection id set up.  So LocalOMRSMetadataCollection
                 * fixes up the provenance.
                 */
                setLocalProvenanceThroughoutRelationship(relationship);

                /*
                 * OK to send out
                 */
                if (produceEventsForRealConnector)
                {
                    outboundRepositoryEventProcessor.processReHomedRelationshipEvent(repositoryName,
                                                                                     metadataCollectionId,
                                                                                     localServerName,
                                                                                     localServerType,
                                                                                     localOrganizationName,
                                                                                     homeMetadataCollectionId,
                                                                                     relationship);
                }
            }

            return relationship;
        }

        throw new InvalidParameterException(OMRSErrorCode.NOT_FOR_LOCAL_COLLECTION.getMessageDefinition(relationshipGUID,
                                                                                                        typeDefName,
                                                                                                        typeDefGUID,
                                                                                                        newHomeMetadataCollectionId,
                                                                                                        metadataCollectionId),
                                            this.getClass().getName(),
                                            methodName,
                                            newHomeParameterName);
    }



    /* ======================================================================
     * Group 6: Local house-keeping of reference metadata instances
     */


    /**
     * Save the entity as a reference copy.  The id of the home metadata collection is already set up in the
     * entity.
     *
     * @param userId unique identifier for requesting server.
     * @param entity details of the entity to save
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
    public void saveEntityReferenceCopy(String         userId,
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
        final String  instanceParameterName = "entity";

        /*
         * Validate parameters
         */
        super.referenceInstanceParameterValidation(userId, entity, instanceParameterName, methodName);

        /*
         * Validate that this instance is not from a future version of this OMRS with header values that
         * this version of the implementation does not understand.  Only save it if it is from the same or
         * past version of the OMRS.
         */
        if (entity.getHeaderVersion() <= InstanceAuditHeader.CURRENT_AUDIT_HEADER_VERSION)
        {
            /*
             * Save entity
             */
            if (securityVerifier.validateEntityReferenceCopySave(entity))
            {
                realMetadataCollection.saveEntityReferenceCopy(userId, entity);
            }
        }
    }



    /**
     * Retrieve any locally homed classifications assigned to the requested entity.  This method is implemented by repository connectors that are able
     * to store classifications for entities that are homed in another repository.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID unique identifier of the entity with classifications to retrieve
     * @return list of all the classifications for this entity that are homed in this repository
     * @throws InvalidParameterException the entity is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws EntityNotKnownException the entity is not recognized by this repository
     * @throws UserNotAuthorizedException to calling user is not authorized to retrieve this metadata
     * @throws FunctionNotSupportedException this method is not supported
     */
    @Override
    public List<Classification> getHomeClassifications(String userId,
                                                       String entityGUID) throws InvalidParameterException,
                                                                                 RepositoryErrorException,
                                                                                 EntityNotKnownException,
                                                                                 UserNotAuthorizedException,
                                                                                 FunctionNotSupportedException
    {
        final String  methodName = "getHomeClassifications";

        /*
         * Validate parameters
         */
        super.getInstanceParameterValidation(userId, entityGUID, methodName);

        /*
         * Perform operation
         */
        List<Classification>  homeClassifications = realMetadataCollection.getHomeClassifications(userId, entityGUID);

        if (homeClassifications != null)
        {
            setLocalProvenanceInEntityClassifications(homeClassifications);
        }

        return homeClassifications;
    }


    /**
     * Retrieve any locally homed classifications assigned to the requested entity.  This method is implemented by repository connectors that are able
     * to store classifications for entities that are homed in another repository.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID unique identifier of the entity with classifications to retrieve
     * @param asOfTime the time used to determine which version of the entity that is desired.
     * @return list of all the classifications for this entity that are homed in this repository
     * @throws InvalidParameterException the entity is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws EntityNotKnownException the entity is not recognized by this repository
     * @throws UserNotAuthorizedException to calling user is not authorized to retrieve this metadata
     * @throws FunctionNotSupportedException this method is not supported
     */
    @Override
    public List<Classification> getHomeClassifications(String userId,
                                                       String entityGUID,
                                                       Date   asOfTime) throws InvalidParameterException,
                                                                               RepositoryErrorException,
                                                                               EntityNotKnownException,
                                                                               UserNotAuthorizedException,
                                                                               FunctionNotSupportedException
    {
        final String  methodName = "getHomeClassifications (with history)";

        /*
         * Validate parameters
         */
        super.getInstanceParameterValidation(userId, entityGUID, methodName);

        /*
         * Perform operation
         */
        List<Classification>  homeClassifications = realMetadataCollection.getHomeClassifications(userId, entityGUID, asOfTime);

        if (homeClassifications != null)
        {
            setLocalProvenanceInEntityClassifications(homeClassifications);
        }

        return homeClassifications;
    }


    /**
     * Remove a reference copy of the entity from the local repository.  This method can be used to
     * remove reference copies from the local cohort, repositories that have left the cohort,
     * or entities that have come from open metadata archives.  It is also an opportunity to remove or
     * soft delete relationships attached to the entity.
     *
     * @param userId unique identifier for requesting server.
     * @param entity details of the entity to purge.
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
    public  void deleteEntityReferenceCopy(String         userId,
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
        final String  methodName = "deleteEntityReferenceCopy";
        final String  instanceParameterName = "entity";

        /*
         * Validate parameters
         */
        super.referenceInstanceParameterValidation(userId, entity, instanceParameterName, methodName);

        /*
         * Pass request to real repository
         */
        realMetadataCollection.deleteEntityReferenceCopy(userId, entity);
    }


    /**
     * Remove a reference copy of the entity from the local repository.  This method can be used to
     * remove reference copies from the local cohort, repositories that have left the cohort,
     * or entities that have come from open metadata archives.  It is also an opportunity to remove or
     * soft delete relationships attached to the entity
     *
     * @param userId unique identifier for requesting server.
     * @param entity details of the entity to purge.
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
    public  void purgeEntityReferenceCopy(String         userId,
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
        final String  methodName = "purgeEntityReferenceCopy";
        final String  instanceParameterName = "entity";

        /*
         * Validate parameters
         */
        this.referenceInstanceParameterValidation(userId, entity, instanceParameterName, methodName);

        /*
         * Pass request to real repository
         */
        realMetadataCollection.purgeEntityReferenceCopy(userId, entity);
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
                                         String entityGUID,
                                         String typeDefGUID,
                                         String typeDefName,
                                         String homeMetadataCollectionId) throws InvalidParameterException,
                                                                                 RepositoryErrorException,
                                                                                 EntityNotKnownException,
                                                                                 HomeEntityException,
                                                                                 FunctionNotSupportedException,
                                                                                 UserNotAuthorizedException
    {
        final String methodName               = "purgeEntityReferenceCopy";
        final String entityParameterName      = "entityGUID";
        final String homeParameterName        = "homeMetadataCollectionId";

        /*
         * Validate parameters
         */
        super.manageReferenceInstanceParameterValidation(userId,
                                                         entityGUID,
                                                         typeDefGUID,
                                                         typeDefName,
                                                         entityParameterName,
                                                         homeMetadataCollectionId,
                                                         homeParameterName,
                                                         methodName);

        /*
         * Remove entity
         */
        realMetadataCollection.purgeEntityReferenceCopy(userId,
                                                        entityGUID,
                                                        typeDefGUID,
                                                        typeDefName,
                                                        homeMetadataCollectionId);
    }


    /**
     * The local repository has requested that the repository that hosts the home metadata collection for the
     * specified entity sends out the details of this entity so the local repository can create a reference copy.
     *
     * @param userId unique identifier for requesting server.
     * @param entityGUID unique identifier of requested entity
     * @param typeDefGUID unique identifier of requested entity's TypeDef
     * @param typeDefName unique name of requested entity's TypeDef
     * @param homeMetadataCollectionId identifier of the metadata collection that is the home to this entity.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws HomeEntityException the entity belongs to the local repository so creating a reference
     *                               copy would be invalid.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public void refreshEntityReferenceCopy(String    userId,
                                           String    entityGUID,
                                           String    typeDefGUID,
                                           String    typeDefName,
                                           String    homeMetadataCollectionId) throws InvalidParameterException,
                                                                                      RepositoryErrorException,
                                                                                      HomeEntityException,
                                                                                      UserNotAuthorizedException
    {
        final String methodName                = "refreshEntityReferenceCopy";
        final String entityParameterName       = "entityGUID";
        final String homeParameterName         = "homeMetadataCollectionId";

        /*
         * Validate parameters
         */
        super.manageReferenceInstanceParameterValidation(userId,
                                                         entityGUID,
                                                         typeDefGUID,
                                                         typeDefName,
                                                         entityParameterName,
                                                         homeMetadataCollectionId,
                                                         homeParameterName,
                                                         methodName);

        /*
         * Validate that the entity GUID is ok
         */
        EntityDetail entity = this.isEntityKnown(userId, entityGUID);

        if (entity != null)
        {
            if (metadataCollectionId.equals(entity.getMetadataCollectionId()))
            {
                throw new HomeEntityException(OMRSErrorCode.HOME_REFRESH.getMessageDefinition(methodName,
                                                                                              entityGUID,
                                                                                              metadataCollectionId,
                                                                                              repositoryName),
                                              this.getClass().getName(),
                                              methodName);
            }
        }

        /*
         * Process refresh request - This event is always generated by the local metadata collection.
         * The home repository can choose whether to reply.
         */
        outboundRepositoryEventProcessor.processRefreshEntityRequested(repositoryName,
                                                                       metadataCollectionId,
                                                                       localServerName,
                                                                       localServerType,
                                                                       localOrganizationName,
                                                                       typeDefGUID,
                                                                       typeDefName,
                                                                       entityGUID,
                                                                       homeMetadataCollectionId);
    }


    /**
     * Save the classification as a reference copy.  The id of the home metadata collection is already set up in the
     * classification.  The entity may be either a locally homed entity or a reference copy.
     *
     * @param userId unique identifier for requesting user.
     * @param entity entity that the classification is attached to.
     * @param classification classification to save.
     *
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                characteristics in the TypeDef for this classification type.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     * @throws TypeErrorException the requested type is not known, or not supported in the metadata repository
     *                            hosting the metadata collection.
     * @throws EntityConflictException the new entity conflicts with an existing entity.
     * @throws InvalidEntityException the new entity has invalid contents.
     * @throws FunctionNotSupportedException the repository does not support reference copies of instances.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public void saveClassificationReferenceCopy(String         userId,
                                                EntityDetail   entity,
                                                Classification classification) throws InvalidParameterException,
                                                                                      RepositoryErrorException,
                                                                                      TypeErrorException,
                                                                                      EntityConflictException,
                                                                                      InvalidEntityException,
                                                                                      PropertyErrorException,
                                                                                      UserNotAuthorizedException,
                                                                                      FunctionNotSupportedException
    {
        final String  methodName = "saveClassificationReferenceCopy";
        final String  instanceParameterName = "entity";
        final String  classificationParameterName = "classification";

        /*
         * Validate parameters
         */
        this.basicRequestValidation(userId, methodName);
        if (entity == null)
        {
            throw new InvalidParameterException(OMRSErrorCode.NULL_REFERENCE_INSTANCE.getMessageDefinition(repositoryName, methodName, instanceParameterName),
                                                this.getClass().getName(),
                                                methodName,
                                                instanceParameterName);
        }

        if (classification == null)
        {
            throw new InvalidParameterException(OMRSErrorCode.NULL_REFERENCE_INSTANCE.getMessageDefinition(repositoryName, methodName, classificationParameterName),
                                                this.getClass().getName(),
                                                methodName,
                                                classificationParameterName);
        }

        repositoryValidator.validateInstanceType(repositoryName, entity);
        repositoryValidator.validateHomeMetadataGUID(repositoryName, instanceParameterName, entity.getMetadataCollectionId(), methodName);

        /*
         * Validate that this instance is not from a future version of this OMRS with header values that
         * this version of the implementation does not understand.  Only save it if it is from the same or
         * past version of the OMRS.
         */
        if (entity.getHeaderVersion() <= InstanceAuditHeader.CURRENT_AUDIT_HEADER_VERSION)
        {
            /*
             * Save classification
             */
            realMetadataCollection.saveClassificationReferenceCopy(userId, entity, classification);
        }
    }


    /**
     * Save the classification as a reference copy.  The id of the home metadata collection is already set up in the
     * classification.  The entity may be either a locally homed entity or a reference copy.
     *
     * @param userId unique identifier for requesting user.
     * @param entityProxy entity that the classification is attached to.
     * @param classification classification to save.
     *
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                characteristics in the TypeDef for this classification type.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     * @throws TypeErrorException the requested type is not known, or not supported in the metadata repository
     *                            hosting the metadata collection.
     * @throws EntityConflictException the new entity conflicts with an existing entity.
     * @throws InvalidEntityException the new entity has invalid contents.
     * @throws FunctionNotSupportedException the repository does not support reference copies of instances.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public void saveClassificationReferenceCopy(String         userId,
                                                EntityProxy    entityProxy,
                                                Classification classification) throws InvalidParameterException,
                                                                                      RepositoryErrorException,
                                                                                      TypeErrorException,
                                                                                      EntityConflictException,
                                                                                      InvalidEntityException,
                                                                                      PropertyErrorException,
                                                                                      UserNotAuthorizedException,
                                                                                      FunctionNotSupportedException
    {
        final String  methodName = "saveClassificationReferenceCopy";
        final String  instanceParameterName = "entityProxy";
        final String  classificationParameterName = "classification";

        /*
         * Validate parameters
         */
        this.basicRequestValidation(userId, methodName);
        if (entityProxy == null)
        {
            throw new InvalidParameterException(OMRSErrorCode.NULL_REFERENCE_INSTANCE.getMessageDefinition(repositoryName, methodName, instanceParameterName),
                                                this.getClass().getName(),
                                                methodName,
                                                instanceParameterName);
        }

        if (classification == null)
        {
            throw new InvalidParameterException(OMRSErrorCode.NULL_REFERENCE_INSTANCE.getMessageDefinition(repositoryName, methodName, classificationParameterName),
                                                this.getClass().getName(),
                                                methodName,
                                                classificationParameterName);
        }

        repositoryValidator.validateInstanceType(repositoryName, entityProxy);
        repositoryValidator.validateHomeMetadataGUID(repositoryName, instanceParameterName, entityProxy.getMetadataCollectionId(), methodName);

        /*
         * Validate that this instance is not from a future version of this OMRS with header values that
         * this version of the implementation does not understand.  Only save it if it is from the same or
         * past version of the OMRS.
         */
        if (entityProxy.getHeaderVersion() <= InstanceAuditHeader.CURRENT_AUDIT_HEADER_VERSION)
        {
            /*
             * Save classification
             */
            realMetadataCollection.saveClassificationReferenceCopy(userId, entityProxy, classification);
        }
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
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                characteristics in the TypeDef for this classification type.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws TypeErrorException the requested type is not known, or not supported in the metadata repository
     *                            hosting the metadata collection.
     * @throws EntityConflictException the new entity conflicts with an existing entity.
     * @throws InvalidEntityException the new entity has invalid contents.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     */
    @Override
    public  void purgeClassificationReferenceCopy(String         userId,
                                                  EntityDetail   entity,
                                                  Classification classification) throws InvalidParameterException,
                                                                                        TypeErrorException,
                                                                                        PropertyErrorException,
                                                                                        EntityConflictException,
                                                                                        InvalidEntityException,
                                                                                        RepositoryErrorException,
                                                                                        UserNotAuthorizedException,
                                                                                        FunctionNotSupportedException
    {
        final String  methodName = "purgeClassificationReferenceCopy";
        final String  instanceParameterName = "entity";
        final String  classificationParameterName = "classification";

        /*
         * Validate parameters
         */
        this.basicRequestValidation(userId, methodName);
        if (entity == null)
        {
            throw new InvalidParameterException(OMRSErrorCode.NULL_REFERENCE_INSTANCE.getMessageDefinition(repositoryName, methodName, instanceParameterName),
                                                this.getClass().getName(),
                                                methodName,
                                                instanceParameterName);
        }
        if (classification == null)
        {
            throw new InvalidParameterException(OMRSErrorCode.NULL_REFERENCE_INSTANCE.getMessageDefinition(repositoryName, methodName, classificationParameterName),
                                                this.getClass().getName(),
                                                methodName,
                                                classificationParameterName);
        }

        repositoryValidator.validateInstanceType(repositoryName, entity);
        repositoryValidator.validateHomeMetadataGUID(repositoryName, instanceParameterName, entity.getMetadataCollectionId(), methodName);

        /*
         * Validate that this instance is not from a future version of this OMRS with header values that
         * this version of the implementation does not understand.  Only save it if it is from the same or
         * past version of the OMRS.
         */
        if (entity.getHeaderVersion() <= InstanceAuditHeader.CURRENT_AUDIT_HEADER_VERSION)
        {
            /*
             * Remove classification
             */
            realMetadataCollection.purgeClassificationReferenceCopy(userId, entity, classification);
        }
    }


    /**
     * Remove the reference copy of the classification from the local repository. This method can be used to
     * remove reference copies from the local cohort, repositories that have left the cohort,
     * or relationships that have come from open metadata archives.
     *
     * @param userId unique identifier for requesting user.
     * @param entityProxy entity that the classification is attached to.
     * @param classification classification to purge.
     *
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                characteristics in the TypeDef for this classification type.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws TypeErrorException the requested type is not known, or not supported in the metadata repository
     *                            hosting the metadata collection.
     * @throws EntityConflictException the new entity conflicts with an existing entity.
     * @throws InvalidEntityException the new entity has invalid contents.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
     */
    @Override
    public  void purgeClassificationReferenceCopy(String         userId,
                                                  EntityProxy    entityProxy,
                                                  Classification classification) throws InvalidParameterException,
                                                                                        TypeErrorException,
                                                                                        PropertyErrorException,
                                                                                        EntityConflictException,
                                                                                        InvalidEntityException,
                                                                                        RepositoryErrorException,
                                                                                        UserNotAuthorizedException,
                                                                                        FunctionNotSupportedException
    {
        final String methodName = "purgeClassificationReferenceCopy (proxy)";
        final String instanceParameterName = "entityProxy";
        final String classificationParameterName = "classification";

        /*
         * Validate parameters
         */
        this.basicRequestValidation(userId, methodName);
        if (entityProxy == null)
        {
            throw new InvalidParameterException(OMRSErrorCode.NULL_REFERENCE_INSTANCE.getMessageDefinition(repositoryName, methodName, instanceParameterName),
                                                this.getClass().getName(),
                                                methodName,
                                                instanceParameterName);
        }
        if (classification == null)
        {
            throw new InvalidParameterException(OMRSErrorCode.NULL_REFERENCE_INSTANCE.getMessageDefinition(repositoryName, methodName, classificationParameterName),
                                                this.getClass().getName(),
                                                methodName,
                                                classificationParameterName);
        }

        repositoryValidator.validateInstanceType(repositoryName, entityProxy);
        repositoryValidator.validateHomeMetadataGUID(repositoryName, instanceParameterName, entityProxy.getMetadataCollectionId(), methodName);

        /*
         * Validate that this instance is not from a future version of this OMRS with header values that
         * this version of the implementation does not understand.  Only save it if it is from the same or
         * past version of the OMRS.
         */
        if (entityProxy.getHeaderVersion() <= InstanceAuditHeader.CURRENT_AUDIT_HEADER_VERSION)
        {
            /*
             * Remove classification
             */
            realMetadataCollection.purgeClassificationReferenceCopy(userId, entityProxy, classification);
        }
    }


    /**
     * Save the relationship as a reference copy.  The id of the home metadata collection is already set up in the
     * relationship.
     *
     * @param userId unique identifier for requesting server.
     * @param relationship relationship to save
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
    public void saveRelationshipReferenceCopy(String         userId,
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
        super.referenceInstanceParameterValidation(userId, relationship, instanceParameterName, methodName);

        /*
         * Validate that this instance is not from a future version of this OMRS with header values that
         * this version of the implementation does not understand.  Only save it if it is from the same or
         * past version of the OMRS.
         */
        if (relationship.getHeaderVersion() <= InstanceAuditHeader.CURRENT_AUDIT_HEADER_VERSION)
        {
            /*
             * Save relationship
             */
            if (securityVerifier.validateRelationshipReferenceCopySave(relationship))
            {
                realMetadataCollection.saveRelationshipReferenceCopy(userId, relationship);
            }
        }
    }


    /**
     * Remove the reference copy of the relationship from the local repository. This method can be used to
     * remove reference copies from the local cohort, repositories that have left the cohort,
     * or relationships that have come from open metadata archives.
     *
     * @param userId unique identifier for requesting server.
     * @param relationship relationship to purge.
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
    public  void deleteRelationshipReferenceCopy(String         userId,
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
        final String  methodName = "deleteRelationshipReferenceCopy";
        final String  instanceParameterName = "relationship";

        /*
         * Validate parameters
         */
        super.referenceInstanceParameterValidation(userId, relationship, instanceParameterName, methodName);

        /*
         * Delete relationship
         */
        realMetadataCollection.deleteRelationshipReferenceCopy(userId, relationship);
    }


    /**
     * Remove the reference copy of the relationship from the local repository. This method can be used to
     * remove reference copies from the local cohort, repositories that have left the cohort,
     * or relationships that have come from open metadata archives.
     *
     * @param userId unique identifier for requesting server.
     * @param relationship relationship to purge.
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
    public  void purgeRelationshipReferenceCopy(String         userId,
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
        final String methodName            = "purgeRelationshipReferenceCopy";
        final String instanceParameterName = "relationship";

        /*
         * Validate parameters
         */
        this.referenceInstanceParameterValidation(userId, relationship, instanceParameterName, methodName);

        /*
         * Purge relationship
         */
        realMetadataCollection.purgeRelationshipReferenceCopy(userId, relationship);
    }


    /**
     * Remove the reference copy of the relationship from the local repository. This method can be used to
     * remove reference copies from the local cohort, repositories that have left the cohort,
     * or relationships that have come from open metadata archives.
     *
     * @param userId unique identifier for requesting serverName.
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
    public void purgeRelationshipReferenceCopy(String    userId,
                                               String    relationshipGUID,
                                               String    typeDefGUID,
                                               String    typeDefName,
                                               String    homeMetadataCollectionId) throws InvalidParameterException,
                                                                                          RepositoryErrorException,
                                                                                          RelationshipNotKnownException,
                                                                                          HomeRelationshipException,
                                                                                          FunctionNotSupportedException,
                                                                                          UserNotAuthorizedException
    {
        final String methodName                = "purgeRelationshipReferenceCopy";
        final String relationshipParameterName = "relationshipGUID";
        final String homeParameterName         = "homeMetadataCollectionId";

        /*
         * Validate parameters
         */
        super.manageReferenceInstanceParameterValidation(userId,
                                                         relationshipGUID,
                                                         typeDefGUID,
                                                         typeDefName,
                                                         relationshipParameterName,
                                                         homeMetadataCollectionId,
                                                         homeParameterName,
                                                         methodName);

        /*
         * Purge relationship
         */
        realMetadataCollection.purgeRelationshipReferenceCopy(userId,
                                                              relationshipGUID,
                                                              typeDefGUID,
                                                              typeDefName,
                                                              homeMetadataCollectionId);
    }


    /**
     * The local server has requested that the repository that hosts the home metadata collection for the
     * specified relationship sends out the details of this relationship so the local repository can create a
     * reference copy.
     *
     * @param userId unique identifier for requesting server.
     * @param relationshipGUID unique identifier of the relationship
     * @param typeDefGUID the guid of the TypeDef for the relationship used to verify the relationship identity.
     * @param typeDefName the name of the TypeDef for the relationship used to verify the relationship identity.
     * @param homeMetadataCollectionId unique identifier for the home repository for this relationship.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws HomeRelationshipException the relationship belongs to the local repository so creating a reference
     *                                     copy would be invalid.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public void refreshRelationshipReferenceCopy(String    userId,
                                                 String    relationshipGUID,
                                                 String    typeDefGUID,
                                                 String    typeDefName,
                                                 String    homeMetadataCollectionId) throws InvalidParameterException,
                                                                                            RepositoryErrorException,
                                                                                            HomeRelationshipException,
                                                                                            UserNotAuthorizedException
    {
        final String methodName                = "refreshRelationshipReferenceCopy";
        final String relationshipParameterName = "relationshipGUID";
        final String homeParameterName         = "homeMetadataCollectionId";

        /*
         * Validate parameters
         */
        super.manageReferenceInstanceParameterValidation(userId,
                                                         relationshipGUID,
                                                         typeDefGUID,
                                                         typeDefName,
                                                         relationshipParameterName,
                                                         homeMetadataCollectionId,
                                                         homeParameterName,
                                                         methodName);

        Relationship relationship = this.isRelationshipKnown(userId, relationshipGUID);
        if (relationship != null)
        {
            if (metadataCollectionId.equals(relationship.getMetadataCollectionId()))
            {
                throw new HomeRelationshipException(OMRSErrorCode.HOME_REFRESH.getMessageDefinition(methodName,
                                                                                                    relationshipGUID,
                                                                                                    metadataCollectionId,
                                                                                                    repositoryName),
                                                    this.getClass().getName(),
                                                    methodName);
            }
        }

        /*
         * Process refresh request - This event is always generated by the local metadata collection.
         * The home repository can choose whether to reply.
         */
        outboundRepositoryEventProcessor.processRefreshRelationshipRequest(repositoryName,
                                                                           metadataCollectionId,
                                                                           localServerName,
                                                                           localServerType,
                                                                           localOrganizationName,
                                                                           typeDefGUID,
                                                                           typeDefName,
                                                                           relationshipGUID,
                                                                           homeMetadataCollectionId);
    }


    /**
     * Save the entities and relationships supplied in the instance graph as a reference copies.
     * The id of the home metadata collection is already set up in the instances.
     * Any instances from the home metadata collection are ignored.
     *
     * @param userId unique identifier for requesting user.
     * @param instances instances to save.
     * @throws InvalidParameterException the relationship is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws TypeErrorException the requested type is not known, or not supported in the metadata repository
     *                            hosting the metadata collection.
     * @throws EntityNotKnownException one of the entities identified by the relationship is not found in the
     *                                   metadata collection.
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                  characteristics in the TypeDef for this relationship's type.
     * @throws EntityConflictException the new entity conflicts with an existing entity.
     * @throws InvalidEntityException the new entity has invalid contents.
     * @throws RelationshipConflictException the new relationship conflicts with an existing relationship.
     * @throws InvalidRelationshipException the new relationship has invalid contents.
     * @throws FunctionNotSupportedException the repository does not support reference copies of instances.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public void saveInstanceReferenceCopies(String          userId,
                                            InstanceGraph   instances) throws InvalidParameterException,
                                                                              RepositoryErrorException,
                                                                              TypeErrorException,
                                                                              EntityNotKnownException,
                                                                              PropertyErrorException,
                                                                              EntityConflictException,
                                                                              RelationshipConflictException,
                                                                              InvalidEntityException,
                                                                              InvalidRelationshipException,
                                                                              UserNotAuthorizedException,
                                                                              FunctionNotSupportedException
    {
        /*
         * It is necessary to filter out all the instances that should not be saved before passing the
         * instances to the real connector.  The validated instances are packed back into an instance graph to
         * pass on the batch so that the real repository connector can benefit from the batch.
         */
        if (instances != null)
        {
            InstanceGraph validatedInstances = null;

            List<EntityDetail> entities = instances.getEntities();

            if ((entities != null) && (! entities.isEmpty()))
            {
                List<EntityDetail> validatedEntities = new ArrayList<>();

                for (EntityDetail entity : entities)
                {
                    if ((entity != null) && (securityVerifier.validateEntityReferenceCopySave(entity)))
                    {
                        validatedEntities.add(entity);
                    }
                }

                if (! validatedEntities.isEmpty())
                {
                    validatedInstances = new InstanceGraph();

                    validatedInstances.setEntities(validatedEntities);
                }
            }

            List<Relationship> relationships = instances.getRelationships();

            if ((relationships != null) && (! relationships.isEmpty()))
            {
                List<Relationship> validatedRelationships = new ArrayList<>();

                for (Relationship relationship : relationships)
                {
                    if ((relationship != null) && (securityVerifier.validateRelationshipReferenceCopySave(relationship)))
                    {
                        validatedRelationships.add(relationship);
                    }
                }

                if (! validatedRelationships.isEmpty())
                {
                    if (validatedInstances == null)
                    {
                        validatedInstances = new InstanceGraph();
                    }

                    validatedInstances.setRelationships(validatedRelationships);
                }
            }

            if (validatedInstances != null)
            {
                /*
                 * delegate processing to the real metadata collection
                 */
                realMetadataCollection.saveInstanceReferenceCopies(userId, validatedInstances);
            }
        }
    }
}
