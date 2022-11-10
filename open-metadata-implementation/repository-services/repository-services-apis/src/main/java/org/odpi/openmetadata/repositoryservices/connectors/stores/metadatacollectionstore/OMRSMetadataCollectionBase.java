/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore;

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
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;

import java.util.*;

/**
 * <p>
 *     OMRSMetadataInstanceStore is the common interface for working with the contents of a metadata repository.
 *     Within a metadata collection are the type definitions (TypeDefs) and metadata instances (Entities and
 *     Relationships).  OMRSMetadataCollectionBase provides empty implementation of the abstract methods of
 *     OMRSMetadataInstanceStore.
 *
 *     The methods on OMRSMetadataInstanceStore are in the following major groups:
 * </p>
 * <ul>
 *     <li><b>Methods to retrieve information about the metadata repository</b> -
 *         Used to retrieve or confirm the identity of the metadata repository
 *     </li>
 *     <li><b>Methods for working with typedefs</b> -
 *         Typedefs are used to define the type model for open metadata.
 *         The open metadata support had a comprehensive set of typedefs implemented, and these can be augmented by
 *         different vendors or applications.  The typedefs can be queried, created, updated and deleted though the
 *         metadata collection.
 *     </li>
 *
 *     <li><b>Methods for querying Entities and Relationships</b> -
 *         The metadata repository stores instances of the typedefs as metadata instances.
 *         Principally these are entities (nodes in the graph) and relationships (links between nodes).
 *         Both the entities and relationships can have properties.
 *         The entity may also have structured properties called structs and classifications attached.
 *         This second group of methods supports a range of queries to retrieve these instances.
 *     </li>
 *
 *     <li><b>Methods for maintaining the instances</b> -
 *         The fourth group of methods supports the maintenance of the metadata instances.  Each instance as a status
 *         (see InstanceStatus) that allows an instance to be proposed, drafted and approved before it becomes
 *         active.  The instances can also be soft-deleted and restored or purged from the metadata
 *         collection.
 *     </li>
 *     <li>
 *         <b>Methods for repairing the metadata collections of the cohort</b> -
 *         The fifth group of methods are for editing the control information of entities and relationships to
 *         manage changes in the cohort.  These methods are advanced methods and are rarely used.
 *     </li>
 *     <li>
 *         <b>Methods for local maintenance of a metadata collection</b>
 *         The final group of methods are for removing reference copies of the metadata instances.  These updates
 *         are not broadcast to the rest of the Cohort as events.
 *     </li>
 * </ul>
 */
public abstract class OMRSMetadataCollectionBase extends OMRSMetadataCollection
{
    /**
     * Constructor ensures the metadata collection is linked to its connector and knows its metadata collection id.
     *
     * @param parentConnector connector that this metadata collection supports.  The connector has the information
     *                        to call the metadata repository.
     * @param repositoryName name of this repository.
     * @param repositoryHelper helper class for building types and instances
     * @param repositoryValidator validator class for checking open metadata repository objects and parameters.
     * @param metadataCollectionId unique identifier of the metadata collection Id.
     */
    public OMRSMetadataCollectionBase(OMRSRepositoryConnector parentConnector,
                                      String                  repositoryName,
                                      OMRSRepositoryHelper    repositoryHelper,
                                      OMRSRepositoryValidator repositoryValidator,
                                      String                  metadataCollectionId)
    {
        super(parentConnector, repositoryName, metadataCollectionId, repositoryHelper, repositoryValidator);
    }


    /* ======================================================================
     * Group 1: Confirm the identity of the metadata repository being called.
     */


    /**
     * Returns the identifier of the metadata repository.  This is the identifier used to register the
     * metadata repository with the metadata repository cohort.  It is also the identifier used to
     * identify the home repository of a metadata instance.
     *
     * @param userId calling user
     * @return String  metadata collection id.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository.
     */
    @Override
    public String      getMetadataCollectionId(String   userId) throws RepositoryErrorException
    {
        final String methodName = "getMetadataCollectionId";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        /*
         * Perform operation
         */
        return super.metadataCollectionId;
    }


    /* ==============================
     * Group 2: Working with typedefs
     */


    /**
     * Validate the properties of a request that accesses types by guid.
     *
     * @param userId calling user
     * @param guid guid of type
     * @param guidParameterName parameter that the guid was passed in on
     * @param methodName calling method
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws RepositoryErrorException there is a problem with the repository connector
     */
    protected void typeGUIDParameterValidation(String userId,
                                               String guid,
                                               String guidParameterName,
                                               String methodName) throws InvalidParameterException,
                                                                         RepositoryErrorException
    {
        super.basicRequestValidation(userId, methodName);

        repositoryValidator.validateGUID(repositoryName, guidParameterName, guid, methodName);
    }


    /**
     * Validate the properties of a request that accesses types by name.
     *
     * @param userId calling user
     * @param name name of type
     * @param nameParameterName parameter that the name was passed in on
     * @param methodName calling method
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws RepositoryErrorException there is a problem with the repository connector
     */
    protected void typeNameParameterValidation(String userId,
                                               String name,
                                               String nameParameterName,
                                               String methodName) throws InvalidParameterException,
                                                                         RepositoryErrorException
    {
        super.basicRequestValidation(userId, methodName);

        repositoryValidator.validateTypeName(repositoryName, nameParameterName, name, methodName);
    }


    /**
     * Validate the properties of a request that accesses types by category.
     *
     * @param userId calling user
     * @param category category of type
     * @param categoryParameterName parameter that the category was passed in on
     * @param methodName calling method
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws RepositoryErrorException there is a problem with the repository connector
     */
    protected void typeDefCategoryParameterValidation(String          userId,
                                                      TypeDefCategory category,
                                                      String          categoryParameterName,
                                                      String          methodName) throws InvalidParameterException,
                                                                                         RepositoryErrorException
    {
        super.basicRequestValidation(userId, methodName);

        repositoryValidator.validateTypeDefCategory(repositoryName, categoryParameterName, category, methodName);
    }


    /**
     * Validate the properties of a request that accesses types by category.
     *
     * @param userId calling user
     * @param category category of type
     * @param categoryParameterName parameter that the category was passed in on
     * @param methodName calling method
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws RepositoryErrorException there is a problem with the repository connector
     */
    protected void attributeTypeDefCategoryParameterValidation(String                   userId,
                                                               AttributeTypeDefCategory category,
                                                               String                   categoryParameterName,
                                                               String                   methodName) throws InvalidParameterException,
                                                                                                           RepositoryErrorException
    {
        super.basicRequestValidation(userId, methodName);

        repositoryValidator.validateAttributeTypeDefCategory(repositoryName, categoryParameterName, category, methodName);
    }


    /**
     * Validate the properties used to locate types.
     *
     * @param userId unique identifier for requesting user.
     * @param matchCriteria TypeDefProperties containing a list of property names.
     * @param matchCriteriaParameterName parameter that the match criteria was passed in on
     * @param methodName calling method
     * @throws InvalidParameterException the matchCriteria is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository.
     */
    protected void typeDefPropertyParameterValidation(String            userId,
                                                      TypeDefProperties matchCriteria,
                                                      String            matchCriteriaParameterName,
                                                      String            methodName) throws InvalidParameterException,
                                                                                           RepositoryErrorException
    {
        super.basicRequestValidation(userId, methodName);
        repositoryValidator.validateMatchCriteria(repositoryName, matchCriteriaParameterName, matchCriteria, methodName);
    }


    /**
     * Validate the properties used to locate types.
     *
     * @param userId unique identifier for requesting user.
     * @param searchCriteria string containing the search criteria.
     * @param searchCriteriaParameterName parameter that the search criteria was passed in on
     * @param methodName calling method
     * @throws InvalidParameterException the searchCriteria is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository.
     */
    protected void typeDefSearchParameterValidation(String       userId,
                                                    String       searchCriteria,
                                                    String       searchCriteriaParameterName,
                                                    String       methodName) throws InvalidParameterException,
                                                                                    RepositoryErrorException
    {
        super.basicRequestValidation(userId, methodName);
        repositoryValidator.validateSearchCriteria(repositoryName, searchCriteriaParameterName, searchCriteria, methodName);
    }


    /**
     * Validate the properties used to locate types.
     *
     * @param userId unique identifier for requesting user.
     * @param standard name of the standard - null means any.
     * @param organization name of the organization - null means any.
     * @param identifier identifier of the element in the standard - null means any.
     * @param methodName calling method
     * @throws InvalidParameterException the matchCriteria is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository.
     */
    protected void typeDefExternalIDParameterValidation(String    userId,
                                                        String    standard,
                                                        String    organization,
                                                        String    identifier,
                                                        String    methodName) throws InvalidParameterException,
                                                                                     RepositoryErrorException
    {
        super.basicRequestValidation(userId, methodName);
        repositoryValidator.validateExternalId(repositoryName, standard, organization, identifier, methodName);
    }


    /**
     * Validate that a supplied typeDef is valid.
     *
     * @param userId unique identifier for requesting user.
     * @param typeDef supplied type definition
     * @param typeDefParameterName name of the parameter for the type definition
     * @param methodName calling method
     * @throws InvalidParameterException a property is null or invalid
     * @throws InvalidTypeDefException the type is invalid
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository.
     */
    protected void typeDefParameterValidation(String  userId,
                                              TypeDef typeDef,
                                              String  typeDefParameterName,
                                              String  methodName) throws InvalidParameterException,
                                                                         InvalidTypeDefException,
                                                                         RepositoryErrorException
    {
        super.basicRequestValidation(userId, methodName);

        repositoryValidator.validateTypeDef(repositoryName, typeDefParameterName, typeDef, methodName);
    }


    /**
     * Validate that a supplied attributeTypeDef is valid.
     *
     * @param userId unique identifier for requesting user.
     * @param attributeTypeDef supplied type definition
     * @param typeDefParameterName name of the parameter for the type definition
     * @param methodName calling method
     * @throws InvalidParameterException a property is null or invalid
     * @throws InvalidTypeDefException the type is invalid
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository.
     */
    protected void attributeTypeDefParameterValidation(String           userId,
                                                       AttributeTypeDef attributeTypeDef,
                                                       String           typeDefParameterName,
                                                       String           methodName) throws InvalidParameterException,
                                                                                           InvalidTypeDefException,
                                                                                           RepositoryErrorException
    {
        super.basicRequestValidation(userId, methodName);
        repositoryValidator.validateAttributeTypeDef(repositoryName, typeDefParameterName, attributeTypeDef, methodName);
    }


    /**
     * Validate that a supplied typeDef is valid and unknown.
     *
     * @param userId unique identifier for requesting user.
     * @param typeDef supplied type definition
     * @param typeDefParameterName name of the parameter for the type definition
     * @param methodName calling method
     * @throws InvalidParameterException a property is null or invalid
     * @throws InvalidTypeDefException the type is invalid
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository.
     * @throws TypeDefConflictException incompatible TypeDefs
     * @throws TypeDefKnownException duplicate TypeDefs
     */
    protected void newTypeDefParameterValidation(String  userId,
                                                 TypeDef typeDef,
                                                 String  typeDefParameterName,
                                                 String  methodName) throws InvalidParameterException,
                                                                            InvalidTypeDefException,
                                                                            TypeDefConflictException,
                                                                            TypeDefKnownException,
                                                                            RepositoryErrorException
    {
        this.typeDefParameterValidation(userId, typeDef, typeDefParameterName, methodName);
        repositoryValidator.validateUnknownTypeDef(repositoryName, typeDefParameterName, typeDef, methodName);
    }


    /**
     * Validate that a supplied attributeTypeDef is valid and unknown.
     *
     * @param userId unique identifier for requesting user.
     * @param attributeTypeDef supplied type definition
     * @param typeDefParameterName name of the parameter for the type definition
     * @param methodName calling method
     * @throws InvalidParameterException a property is null or invalid
     * @throws InvalidTypeDefException the type is invalid
     * @throws TypeDefConflictException incompatible TypeDefs
     * @throws TypeDefKnownException duplicate TypeDefs
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository.
     */
    protected void newAttributeTypeDefParameterValidation(String           userId,
                                                          AttributeTypeDef attributeTypeDef,
                                                          String           typeDefParameterName,
                                                          String           methodName) throws InvalidParameterException,
                                                                                              InvalidTypeDefException,
                                                                                              TypeDefConflictException,
                                                                                              TypeDefKnownException,
                                                                                              RepositoryErrorException
    {
        this.attributeTypeDefParameterValidation(userId, attributeTypeDef, typeDefParameterName, methodName);
        repositoryValidator.validateUnknownAttributeTypeDef(repositoryName, typeDefParameterName, attributeTypeDef, methodName);
    }


    /**
     * Validate that a patch to a type definition is valid.
     *
     * @param userId unique identifier for requesting user.
     * @param typeDefPatch update to the type definition
     * @param methodName calling method
     * @return type definition
     * @throws InvalidParameterException a property is null or invalid
     * @throws PatchErrorException the type update is invalid
     * @throws TypeDefNotKnownException the type is not known
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository.
     */
    protected TypeDef updateTypeDefParameterValidation(String       userId,
                                                       TypeDefPatch typeDefPatch,
                                                       String       methodName) throws InvalidParameterException,
                                                                                       PatchErrorException,
                                                                                       TypeDefNotKnownException,
                                                                                       RepositoryErrorException
    {
        super.basicRequestValidation(userId, methodName);
        return repositoryValidator.validateTypeDefPatch(repositoryName, typeDefPatch, methodName);
    }


    /**
     * Validate the type information for a management change to a type.
     *
     * @param userId unique identifier for requesting user.
     * @param guidParameterName name of parameter for type identifier
     * @param nameParameterName name of parameter for type name
     * @param typeDefGUID unique identifier of type
     * @param typeDefName unique name of type
     * @param methodName calling method
     * @return typeDef
     * @throws InvalidParameterException a property is null or invalid
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository.
     */
    protected TypeDef manageTypeDefParameterValidation(String  userId,
                                                       String  guidParameterName,
                                                       String  nameParameterName,
                                                       String  typeDefGUID,
                                                       String  typeDefName,
                                                       String  methodName) throws InvalidParameterException,
                                                                                  RepositoryErrorException
    {
        super.basicRequestValidation(userId, methodName);
        return repositoryValidator.getValidTypeDefFromIds(repositoryName,
                                                          guidParameterName,
                                                          nameParameterName,
                                                          typeDefGUID,
                                                          typeDefName,
                                                          methodName);
    }


    /**
     * Validate the type information for a management change to an attributeTypeDef.
     *
     * @param userId unique identifier for requesting user.
     * @param guidParameterName name of parameter for type identifier
     * @param nameParameterName name of parameter for type name
     * @param attributeTypeDefGUID unique identifier of type
     * @param attributeTypeDefName unique name of type
     * @param methodName calling method
     * @return retrieved type definition
     * @throws InvalidParameterException a property is null or invalid
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository.
     */
    protected AttributeTypeDef manageAttributeTypeDefParameterValidation(String  userId,
                                                                         String  guidParameterName,
                                                                         String  nameParameterName,
                                                                         String  attributeTypeDefGUID,
                                                                         String  attributeTypeDefName,
                                                                         String  methodName) throws InvalidParameterException,
                                                                                                    RepositoryErrorException
    {
        super.basicRequestValidation(userId, methodName);
        return repositoryValidator.getValidAttributeTypeDefFromIds(repositoryName,
                                                                   guidParameterName,
                                                                   nameParameterName,
                                                                   attributeTypeDefGUID,
                                                                   attributeTypeDefName,
                                                                   methodName);
    }



    /**
     * Returns the list of different types of metadata organized into two groups.  The first are the
     * attribute type definitions (AttributeTypeDefs).  These provide types for properties in full
     * type definitions.  Full type definitions (TypeDefs) describe types for entities, relationships
     * and classifications.
     *
     * @param userId unique identifier for requesting user.
     * @return TypeDefGallery List of different categories of type definitions.
     * @throws InvalidParameterException the userId is null
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public TypeDefGallery getAllTypes(String   userId) throws InvalidParameterException,
                                                              RepositoryErrorException,
                                                              UserNotAuthorizedException
    {
        final String  methodName = "getAllTypes";

        /*
         * Validate parameters
         */
        super.basicRequestValidation(userId, methodName);

        /*
         * Perform operation
         */
        return repositoryHelper.getActiveTypeDefGallery();
    }


    /**
     * Return the types identified by the name supplied by the caller.  The name may have wild
     * card characters in it which is why the results are returned in a list.
     *
     * @param allTypeDefs list of types to filter.
     * @param allAttributeTypeDefs list of types to filter.
     * @param typeDefName unique name for the TypeDef
     * @return TypeDef object or null if TypeDef is not known.
     */
    public TypeDefGallery filterTypesByWildCardName (List<TypeDef>           allTypeDefs,
                                                     List<AttributeTypeDef>  allAttributeTypeDefs,
                                                     String                  typeDefName)
    {
        if (typeDefName != null)
        {
            List<TypeDef>         matchedTypeDefs = new ArrayList<>();

            if (allTypeDefs != null)
            {
                for (TypeDef typeDef : allTypeDefs)
                {
                    if (typeDef != null)
                    {
                        if (typeDef.getName().matches(typeDefName))
                        {
                            matchedTypeDefs.add(typeDef);
                        }
                    }
                }
            }

            List<AttributeTypeDef>         matchedAttributeTypeDefs = new ArrayList<>();

            if (allAttributeTypeDefs != null)
            {
                for (AttributeTypeDef attributeTypeDef : allAttributeTypeDefs)
                {
                    if (attributeTypeDef != null)
                    {
                        if (attributeTypeDef.getName().matches(typeDefName))
                        {
                            matchedAttributeTypeDefs.add(attributeTypeDef);
                        }
                    }
                }
            }

            if ((! matchedTypeDefs.isEmpty()) || (! matchedAttributeTypeDefs.isEmpty()))
            {
                TypeDefGallery        typeDefGallery = new TypeDefGallery();

                if (! matchedTypeDefs.isEmpty())
                {
                    typeDefGallery.setTypeDefs(matchedTypeDefs);
                }
                else
                {
                    typeDefGallery.setTypeDefs(null);
                }

                if (! matchedAttributeTypeDefs.isEmpty())
                {
                    typeDefGallery.setAttributeTypeDefs(matchedAttributeTypeDefs);
                }
                else
                {
                    typeDefGallery.setAttributeTypeDefs(null);
                }

                return typeDefGallery;
            }
        }

        return null;
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
    public TypeDefGallery findTypesByName(String userId,
                                          String name) throws InvalidParameterException,
                                                              RepositoryErrorException,
                                                              UserNotAuthorizedException
    {
        final String   methodName        = "findTypesByName";
        final String   nameParameterName = "name";

        /*
         * Validate parameters
         */
        this.typeNameParameterValidation(userId, name, nameParameterName, methodName);

        /*
         * Retrieve types
         */
        return this.filterTypesByWildCardName(repositoryHelper.getActiveTypeDefs(),
                                              repositoryHelper.getActiveAttributeTypeDefs(),
                                              name);
    }


    /**
     * Returns all the types for a specific category.
     *
     * @param allTypes list of types to filter.
     * @param category enum value for the category of types to return.
     * @return type list.
     */
    protected List<TypeDef> filterTypeDefsByCategory(List<TypeDef>   allTypes,
                                                     TypeDefCategory category)
    {
        if (allTypes != null)
        {
            List<TypeDef> typesForCategory = new ArrayList<>();

            for (TypeDef typeDef : allTypes)
            {
                if (typeDef != null)
                {
                    if (typeDef.getCategory() == category)
                    {
                        typesForCategory.add(typeDef);
                    }
                }
            }


            if (typesForCategory.isEmpty())
            {
                typesForCategory = null;
            }

            return typesForCategory;
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
        this.typeDefCategoryParameterValidation(userId, category, categoryParameterName, methodName);

        /*
         * Perform operation
         */
        return this.filterTypeDefsByCategory(repositoryHelper.getActiveTypeDefs(), category);
    }



    /**
     * Returns all the types for a specific category.
     *
     * @param allTypes list of types to filter.
     * @param category enum value for the category of types to return.
     * @return type list.
     */
    protected List<AttributeTypeDef> filterAttributeTypeDefsByCategory(List<AttributeTypeDef>   allTypes,
                                                                       AttributeTypeDefCategory category)
    {
        if (allTypes != null)
        {
            List<AttributeTypeDef> typesForCategory = new ArrayList<>();

            for (AttributeTypeDef attributeTypeDef : allTypes)
            {
                if (attributeTypeDef != null)
                {
                    if (attributeTypeDef.getCategory() == category)
                    {
                        typesForCategory.add(attributeTypeDef);
                    }
                }
            }


            if (typesForCategory.isEmpty())
            {
                typesForCategory = null;
            }

            return typesForCategory;
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
        this.attributeTypeDefCategoryParameterValidation(userId, category, categoryParameterName, methodName);

        /*
         * Perform operation
         */
        return this.filterAttributeTypeDefsByCategory(repositoryHelper.getActiveAttributeTypeDefs(), category);
    }


    /**
     * Return the TypeDefs that have the properties matching the supplied match criteria.
     *
     * @param allTypes list of types to filter.
     * @param matchCriteria TypeDefProperties - a list of property names.
     * @return TypeDefs list.
     */
    protected List<TypeDef> filterTypeDefsByProperty(List<TypeDef>     allTypes,
                                                     TypeDefProperties matchCriteria)
    {
        if (allTypes != null)
        {
            List<TypeDef> typesMatchProperties = new ArrayList<>();
            Set<String>   propertyNames        = matchCriteria.getTypeDefProperties().keySet();

            for (TypeDef typeDef : allTypes)
            {
                if (typeDef != null)
                {
                    List<TypeDefAttribute> typeDefAttributes  = typeDef.getPropertiesDefinition();
                    boolean                allPropertiesMatch = true;

                    for (String propertyName : propertyNames)
                    {
                        boolean thisPropertyMatches = false;

                        if (propertyName != null)
                        {
                            for (TypeDefAttribute attribute : typeDefAttributes)
                            {
                                if (attribute != null)
                                {
                                    if (propertyName.equals(attribute.getAttributeName()))
                                    {
                                        thisPropertyMatches = true;
                                        break;
                                    }
                                }
                            }

                            if (!thisPropertyMatches)
                            {
                                allPropertiesMatch = false;
                                break;
                            }
                        }
                    }

                    if (allPropertiesMatch)
                    {
                        typesMatchProperties.add(typeDef);
                    }
                }
            }

            if (typesMatchProperties.isEmpty())
            {
                typesMatchProperties = null;
            }

            return typesMatchProperties;
        }

        return null;
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
        this.typeDefPropertyParameterValidation(userId, matchCriteria, matchCriteriaParameterName, methodName);

        /*
         * Perform operation
         */
        return this.filterTypeDefsByProperty(repositoryHelper.getActiveTypeDefs(), matchCriteria);
    }


    /**
     * Match the supplied external standard identifiers against the active types for this repository.
     *
     * @param allTypes list of types to filter
     * @param standard name of the standard, null means any.
     * @param organization name of the organization, null means any.
     * @param identifier identifier of the element in the standard, null means any.
     * @return list of typeDefs
     */
    public  List<TypeDef> filterTypesByExternalID(List<TypeDef> allTypes,
                                                  String        standard,
                                                  String        organization,
                                                  String        identifier)
    {
        if (allTypes != null)
        {
            List<TypeDef>  matchingTypes = new ArrayList<>();

            for (TypeDef activeTypeDef : allTypes)
            {
                /*
                 * Extract all the external standards mappings from the TypeDef.  They are located in the TypeDef
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

            if (matchingTypes.isEmpty())
            {
                return null;
            }
            else
            {
                return matchingTypes;
            }
        }

        return null;
    }


    /**
     * Return the types that are linked to the elements from the specified standard.
     *
     * @param userId unique identifier for requesting user.
     * @param standard name of the standard; null means any.
     * @param organization name of the organization; null means any.
     * @param identifier identifier of the element in the standard; null means any.
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
        this.typeDefExternalIDParameterValidation(userId, standard, organization, identifier, methodName);

        /*
         * Perform operation
         */
        return this.filterTypesByExternalID(repositoryHelper.getActiveTypeDefs(), standard, organization, identifier);
    }


    /**
     * Return the TypeDefs that match the search criteria.
     *
     * @param allTypes list of types to filter.
     * @param searchCriteria String - search criteria.
     * @return TypeDefs list - each entry in the list contains a typedef.  This is a structure
     * describing the TypeDef's category and properties.
     */
    public List<TypeDef> filterTypeDefsBySearchCriteria(List<TypeDef>     allTypes,
                                                        String            searchCriteria)
    {
        if (allTypes != null)
        {
            List<TypeDef> matchedTypeDefs = new ArrayList<>();

            for (TypeDef typeDef : allTypes)
            {
                if (typeDef != null)
                {
                    if (typeDef.getName().matches(searchCriteria))
                    {
                        matchedTypeDefs.add(typeDef);
                    }
                }
            }

            if (matchedTypeDefs.isEmpty())
            {
                matchedTypeDefs = null;
            }

            return matchedTypeDefs;
        }

        return null;
    }


    /**
     * Return the TypeDefs that match the search criteria.
     *
     * @param userId unique identifier for requesting user.
     * @param searchCriteria String search criteria.
     * @return TypeDefs list where each entry in the list contains a typedef.  This is a structure
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
        return this.filterTypeDefsBySearchCriteria(repositoryHelper.getActiveTypeDefs(), searchCriteria);
    }


    /**
     * Return the TypeDef that matches the guid.
     *
     * @param allTypes list of types to filter.
     * @param guid unique identifier for type
     * @return Matching type
     */
    public TypeDef filterTypeDefsByGUID(List<TypeDef>     allTypes,
                                        String            guid)
    {
        if (allTypes != null)
        {
            for (TypeDef typeDef : allTypes)
            {
                if (typeDef != null)
                {
                    if (typeDef.getGUID().equals(guid))
                    {
                         return typeDef;
                    }
                }
            }
        }

        return null;
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
                                                           UserNotAuthorizedException
    {
        final String methodName        = "getTypeDefByGUID";
        final String guidParameterName = "guid";

        /*
         * Validate parameters
         */
        this.typeGUIDParameterValidation(userId, guid, guidParameterName, methodName);

        /*
         * Perform operation
         */
        TypeDef  result = this.filterTypeDefsByGUID(repositoryHelper.getActiveTypeDefs(), guid);

        if (result == null)
        {
            reportUnknownTypeGUID(guid, guidParameterName, methodName);
        }

        return result;
    }


    /**
     * Return the AttributeTypeDef that matches the guid.
     *
     * @param allTypes list of types to filter.
     * @param guid unique identifier for type
     * @return Matching type
     */
    public AttributeTypeDef filterAttributeTypeDefsByGUID(List<AttributeTypeDef>  allTypes,
                                                          String                  guid)
    {
        if (allTypes != null)
        {
            for (AttributeTypeDef attributeTypeDef : allTypes)
            {
                if (attributeTypeDef != null)
                {
                    if (attributeTypeDef.getGUID().equals(guid))
                    {
                        return attributeTypeDef;
                    }
                }
            }
        }

        return null;
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
                                                                              UserNotAuthorizedException
    {
        final String methodName        = "getAttributeTypeDefByGUID";
        final String guidParameterName = "guid";

        /*
         * Validate parameters
         */
        this.typeGUIDParameterValidation(userId, guid, guidParameterName, methodName);

        /*
         * Perform operation
         */
        AttributeTypeDef  result = this.filterAttributeTypeDefsByGUID(repositoryHelper.getActiveAttributeTypeDefs(), guid);

        if (result == null)
        {
            reportUnknownTypeGUID(guid, guidParameterName, methodName);
        }

        return result;
    }



    /**
     * Return the TypeDef that matches the name.
     *
     * @param allTypes list of types to filter.
     * @param name unique identifier for type
     * @return Matching type
     */
    public TypeDef filterTypeDefsByName(List<TypeDef>     allTypes,
                                        String            name)
    {
        if (allTypes != null)
        {
            for (TypeDef typeDef : allTypes)
            {
                if (typeDef != null)
                {
                    if (typeDef.getName().equals(name))
                    {
                        return typeDef;
                    }
                }
            }
        }

        return null;
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
        this.typeNameParameterValidation(userId, name, nameParameterName, methodName);

        /*
         * Perform operation
         */
        TypeDef  result = this.filterTypeDefsByName(repositoryHelper.getActiveTypeDefs(), name);

        if (result == null)
        {
            reportUnknownTypeName(name, methodName);
        }

        return result;
    }


    /**
     * Return the TypeDef that matches the name.
     *
     * @param allTypes list of types to filter.
     * @param name unique identifier for type
     * @return Matching type
     */
    public AttributeTypeDef filterAttributeTypeDefsByName(List<AttributeTypeDef>     allTypes,
                                                          String            name)
    {
        if (allTypes != null)
        {
            for (AttributeTypeDef attributeTypeDef : allTypes)
            {
                if (attributeTypeDef != null)
                {
                    if (attributeTypeDef.getName().equals(name))
                    {
                        return attributeTypeDef;
                    }
                }
            }
        }

        return null;
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
        this.typeNameParameterValidation(userId, name, nameParameterName, methodName);

        /*
         * Perform operation
         */
        AttributeTypeDef  result = this.filterAttributeTypeDefsByName(repositoryHelper.getActiveAttributeTypeDefs(), name);

        if (result == null)
        {
            reportUnknownTypeName(name, methodName);
        }

        return result;
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
        super.basicRequestValidation(userId, methodName);
        repositoryValidator.validateTypeDefGallery(repositoryName, galleryParameterName, newTypes, methodName);

        /*
         * Perform operation
         */
        List<AttributeTypeDef>   attributeTypeDefs = newTypes.getAttributeTypeDefs();
        List<TypeDef>            typeDefs          = newTypes.getTypeDefs();

        if (attributeTypeDefs != null)
        {
            for (AttributeTypeDef attributeTypeDef : attributeTypeDefs)
            {
                this.addAttributeTypeDef(userId, attributeTypeDef);
            }
        }

        if (typeDefs != null)
        {
            for (TypeDef typeDef : typeDefs)
            {
                this.addTypeDef(userId, typeDef);
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
                                                           UserNotAuthorizedException
    {
        final String  methodName = "addTypeDef";
        final String  typeDefParameterName = "newTypeDef";

        /*
         * Validate parameters
         */
        this.newTypeDefParameterValidation(userId, newTypeDef, typeDefParameterName, methodName);

        /*
         * Perform operation
         */
        reportUnsupportedOptionalFunction(methodName);
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
        this.newAttributeTypeDefParameterValidation(userId, newAttributeTypeDef, typeDefParameterName, methodName);

        /*
         * Perform operation
         */
        reportUnsupportedOptionalFunction(methodName);
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
    public boolean verifyTypeDef(String       userId,
                                 TypeDef      typeDef) throws InvalidParameterException,
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
        this.typeDefParameterValidation(userId, typeDef, typeDefParameterName, methodName);

        /*
         * Perform operation
         */
        reportUnsupportedMandatoryFunction(methodName);
        return false;
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
        this.attributeTypeDefParameterValidation(userId, attributeTypeDef, typeDefParameterName, methodName);

        /*
         * Perform operation
         */
        reportUnsupportedMandatoryFunction(methodName);
        return false;
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

        /*
         * Validate parameters
         */
        this.updateTypeDefParameterValidation(userId, typeDefPatch, methodName);

        /*
         * Perform operation
         */
        reportUnsupportedOptionalFunction(methodName);
        return null;
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
        this.manageTypeDefParameterValidation(userId,
                                              guidParameterName,
                                              nameParameterName,
                                              obsoleteTypeDefGUID,
                                              obsoleteTypeDefName,
                                              methodName);

        /*
         * Perform operation
         */
        reportUnsupportedOptionalFunction(methodName);
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
        this.manageAttributeTypeDefParameterValidation(userId,
                                                       guidParameterName,
                                                       nameParameterName,
                                                       obsoleteTypeDefGUID,
                                                       obsoleteTypeDefName,
                                                       methodName);

        /*
         * Perform operation
         */
        reportUnsupportedOptionalFunction(methodName);
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
        this.manageTypeDefParameterValidation(userId,
                                              originalGUIDParameterName,
                                              originalNameParameterName,
                                              originalTypeDefGUID,
                                              originalTypeDefName,
                                              methodName);
        this.manageTypeDefParameterValidation(userId,
                                              newGUIDParameterName,
                                              newNameParameterName,
                                              newTypeDefGUID,
                                              newTypeDefName,
                                              methodName);

        /*
         * Perform operation
         */
        reportUnsupportedOptionalFunction(methodName);
        return null;
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
        this.manageAttributeTypeDefParameterValidation(userId,
                                                       originalGUIDParameterName,
                                                       originalNameParameterName,
                                                       originalAttributeTypeDefGUID,
                                                       originalAttributeTypeDefName,
                                                       methodName);
        this.manageAttributeTypeDefParameterValidation(userId,
                                                       newGUIDParameterName,
                                                       newNameParameterName,
                                                       newAttributeTypeDefGUID,
                                                       newAttributeTypeDefName,
                                                       methodName);

        /*
         * Perform operation
         */
        reportUnsupportedOptionalFunction(methodName);
        return null;
    }


    /* ===================================================
     * Group 3: Locating entity and relationship instances
     */

    /**
     * Validate the parameters for a retrieve of an instance.
     *
     * @param userId unique identifier for requesting user.
     * @param guid String unique identifier for the relationship.
     * @param methodName name of calling method.
     * @throws InvalidParameterException the guid or date is null or the date is for a future time
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                 the metadata collection is stored.
     */
    protected  void getInstanceParameterValidation(String    userId,
                                                   String    guid,
                                                   String    methodName) throws InvalidParameterException,
                                                                                RepositoryErrorException
    {
        final String  guidParameterName = "guid";

        /*
         * Validate parameters
         */
        super.basicRequestValidation(userId, methodName);
        repositoryValidator.validateGUID(repositoryName, guidParameterName, guid, methodName);
    }


    /**
     * Validate the parameters for a retrieve of a historical version of an instance.
     *
     * @param userId unique identifier for requesting user.
     * @param guid String unique identifier for the relationship.
     * @param asOfTime the time used to determine which version of the entity that is desired.
     * @param methodName name of calling method.
     * @throws InvalidParameterException the guid or date is null or the date is for a future time
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                 the metadata collection is stored.
     */
    protected  void getInstanceParameterValidation(String    userId,
                                                   String    guid,
                                                   Date      asOfTime,
                                                   String    methodName) throws InvalidParameterException,
                                                                                RepositoryErrorException
    {
        final String  guidParameterName = "guid";
        final String  asOfTimeParameter = "asOfTime";

        /*
         * Validate parameters
         */
        super.basicRequestValidation(userId, methodName);
        repositoryValidator.validateGUID(repositoryName, guidParameterName, guid, methodName);
        repositoryValidator.validateAsOfTimeNotNull(repositoryName, asOfTimeParameter, asOfTime, methodName);
    }


    /**
     * Validate the parameters for a retrieve multiple historical versions of an instance.
     *
     * @param userId unique identifier for requesting user.
     * @param guid String unique identifier for the relationship.
     * @param fromTime the earliest point in time from which to retrieve historical versions of the entity (inclusive)
     * @param toTime the latest point in time from which to retrieve historical versions of the entity (exclusive)
     * @param methodName name of calling method.
     * @throws InvalidParameterException the guid is null or the 'fromTime' is after the 'toTime'
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                 the metadata collection is stored.
     */
    protected  void getInstanceHistoryParameterValidation(String    userId,
                                                          String    guid,
                                                          Date      fromTime,
                                                          Date      toTime,
                                                          String    methodName) throws InvalidParameterException,
                                                                                       RepositoryErrorException
    {
        final String  guidParameterName = "guid";
        final String  timeRangeParameterNames = "fromTime/toTime";

        /*
         * Validate parameters
         */
        super.basicRequestValidation(userId, methodName);
        repositoryValidator.validateGUID(repositoryName, guidParameterName, guid, methodName);
        repositoryValidator.validateDateRange(repositoryName, timeRangeParameterNames, fromTime, toTime, methodName);
    }


    /**
     * Validate the parameters for getRelationshipsForEntity.
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
     * @throws InvalidParameterException a parameter is invalid or null.
     * @throws TypeErrorException the type guid passed on the request is not known by the
     *                              metadata collection.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws PagingErrorException the paging/sequencing parameters are set up incorrectly.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @SuppressWarnings(value = "unused")
    protected void getRelationshipsForEntityParameterValidation(String                     userId,
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
                                                                                                            PagingErrorException,
                                                                                                            UserNotAuthorizedException
    {
        final String methodName        = "getRelationshipsForEntity";
        final String guidParameterName = "entityGUID";
        final String typeGUIDParameter = "relationshipTypeGUID";
        final String asOfTimeParameter = "asOfTime";
        final String pageSizeParameter = "pageSize";

        /*
         * Validate parameters
         */
        super.basicRequestValidation(userId, methodName);
        repositoryValidator.validateGUID(repositoryName, guidParameterName, entityGUID, methodName);
        repositoryValidator.validateAsOfTime(repositoryName, asOfTimeParameter, asOfTime, methodName);
        repositoryValidator.validateOptionalTypeGUID(repositoryName, typeGUIDParameter, relationshipTypeGUID,
                                                     methodName);
        repositoryValidator.validatePageSize(repositoryName, pageSizeParameter, pageSize, methodName);
    }


    /**
     * Validate the parameters for findEntitiesByProperty.
     *
     * @param userId unique identifier for requesting user.
     * @param entityTypeGUID String unique identifier for the entity type of interest (null means any entity type).
     * @param matchProperties Optional list of entity properties to match (contains wildcards).
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
     * @throws InvalidParameterException a parameter is invalid or null.
     * @throws TypeErrorException the type guid passed on the request is not known by the
     *                              metadata collection.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws PropertyErrorException the properties specified are not valid for any of the requested types of
     *                                  entity.
     * @throws PagingErrorException the paging/sequencing parameters are set up incorrectly.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @SuppressWarnings(value = "unused")
    protected void findEntitiesByPropertyParameterValidation(String                    userId,
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
                                                                                                        UserNotAuthorizedException
    {
        final String methodName                   = "findEntitiesByProperty";
        final String matchCriteriaParameterName   = "matchCriteria";
        final String matchPropertiesParameterName = "matchProperties";
        final String typeGUIDParameterName        = "entityTypeGUID";
        final String asOfTimeParameter            = "asOfTime";
        final String pageSizeParameter            = "pageSize";


        super.basicRequestValidation(userId, methodName);
        repositoryValidator.validateOptionalTypeGUID(repositoryName, typeGUIDParameterName, entityTypeGUID, methodName);
        repositoryValidator.validateAsOfTime(repositoryName, asOfTimeParameter, asOfTime, methodName);
        repositoryValidator.validatePageSize(repositoryName, pageSizeParameter, pageSize, methodName);
        repositoryValidator.validateMatchCriteria(repositoryName,
                                                  matchCriteriaParameterName,
                                                  matchPropertiesParameterName,
                                                  matchCriteria,
                                                  matchProperties,
                                                  methodName);
    }


    /**
     * Validate the parameters for findEntities.
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
     * @throws InvalidParameterException a parameter is invalid or null.
     * @throws TypeErrorException the type guid passed on the request is not known by the
     *                              metadata collection.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws PagingErrorException the paging/sequencing parameters are set up incorrectly.
     * @see #findEntities(String, String, List, SearchProperties, int, List, SearchClassifications, Date, String, SequencingOrder, int)
     */
    @SuppressWarnings(value = "unused")
    protected void findEntitiesParameterValidation(String                    userId,
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
                                                                                              PagingErrorException
    {
        final String methodName                        = "findEntities";
        final String typeGUIDParameterName             = "entityTypeGUID";
        final String subtypeGUIDsParameterName         = "entitySubtypeGUIDs";
        final String asOfTimeParameter                 = "asOfTime";
        final String pageSizeParameter                 = "pageSize";
        final String matchPropertiesParameterName      = "matchProperties";
        final String matchClassificationsParameterName = "matchClassifications";

        super.basicRequestValidation(userId, methodName);
        repositoryValidator.validateOptionalTypeGUIDs(repositoryName, typeGUIDParameterName, entityTypeGUID, subtypeGUIDsParameterName, entitySubtypeGUIDs, methodName);
        repositoryValidator.validateAsOfTime(repositoryName, asOfTimeParameter, asOfTime, methodName);
        repositoryValidator.validatePageSize(repositoryName, pageSizeParameter, pageSize, methodName);
        repositoryValidator.validateSearchProperties(repositoryName, matchPropertiesParameterName, matchProperties, methodName);
        repositoryValidator.validateSearchClassifications(repositoryName, matchClassificationsParameterName, matchClassifications, methodName);
    }


    /**
     * Validate the parameters for findEntitiesByClassification.
     *
     * @param userId unique identifier for requesting user.
     * @param entityTypeGUID unique identifier for the type of entity requested.  Null means any type of entity.
     * @param classificationName name of the classification a null is not valid.
     * @param matchClassificationProperties Optional list of entity properties to match (contains wildcards).
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
     * @throws InvalidParameterException a parameter is invalid or null.
     * @throws TypeErrorException the type guid passed on the request is not known by the
     *                              metadata collection.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws ClassificationErrorException the classification request is not known to the metadata collection.
     * @throws PropertyErrorException the properties specified are not valid for the requested type of
     *                                  classification.
     * @throws PagingErrorException the paging/sequencing parameters are set up incorrectly.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @SuppressWarnings(value = "unused")
    protected void findEntitiesByClassificationParameterValidation(String                    userId,
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
                                                                                                              UserNotAuthorizedException
    {
        final String methodName                  = "findEntitiesByClassification";
        final String classificationParameterName = "classificationName";
        final String entityTypeGUIDParameterName = "entityTypeGUID";

        final String matchCriteriaParameterName   = "matchCriteria";
        final String matchPropertiesParameterName = "matchClassificationProperties";
        final String asOfTimeParameter            = "asOfTime";
        final String pageSizeParameter            = "pageSize";

        /*
         * Validate parameters
         */
        super.basicRequestValidation(userId, methodName);
        repositoryValidator.validateOptionalTypeGUID(repositoryName,
                                                     entityTypeGUIDParameterName,
                                                     entityTypeGUID,
                                                     methodName);
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
    }


    /**
     * Validate parameters passed to findEntitiesByPropertyValue.
     *
     * @param userId unique identifier for requesting user.
     * @param entityTypeGUID GUID of the type of entity to search for. Null means all types will
     *                       be searched (could be slow so not recommended).
     * @param searchCriteria String expression contained in any of the property values within the entities
     *                       of the supplied type.
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
     * @throws InvalidParameterException a parameter is invalid or null.
     * @throws TypeErrorException the type guid passed on the request is not known by the
     *                              metadata collection.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws PropertyErrorException the sequencing property specified is not valid for any of the requested types of
     *                                  entity.
     * @throws PagingErrorException the paging/sequencing parameters are set up incorrectly.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @SuppressWarnings(value = "unused")
    protected void findEntitiesByPropertyValueParameterValidation(String                userId,
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
                                                                                                         UserNotAuthorizedException
    {
        final String  methodName = "findEntitiesByPropertyValue";
        final String  searchCriteriaParameterName = "searchCriteria";
        final String  typeGUIDParameter = "entityTypeGUID";
        final String  asOfTimeParameter = "asOfTime";
        final String  pageSizeParameter = "pageSize";

        super.basicRequestValidation(userId, methodName);
        repositoryValidator.validateSearchCriteria(repositoryName, searchCriteriaParameterName, searchCriteria, methodName);
        repositoryValidator.validateOptionalTypeGUID(repositoryName, typeGUIDParameter, entityTypeGUID, methodName);
        repositoryValidator.validateAsOfTime(repositoryName, asOfTimeParameter, asOfTime, methodName);
        repositoryValidator.validatePageSize(repositoryName, pageSizeParameter, pageSize, methodName);
    }


    /**
     * Validate the parameters passed to findRelationships.
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
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws TypeErrorException the type guid passed on the request is not known by the
     *                              metadata collection.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws PagingErrorException the paging/sequencing parameters are set up incorrectly.
     * @see #findRelationships(String, String, List, SearchProperties, int, List, Date, String, SequencingOrder, int)
     */
    @SuppressWarnings(value = "unused")
    protected void findRelationshipsParameterValidation(String                    userId,
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
                                                                                                   PagingErrorException
    {
        final String methodName                   = "findRelationships";
        final String matchPropertiesParameterName = "matchProperties";
        final String guidParameterName            = "relationshipTypeGUID";
        final String subtypeGuidsParameterName    = "relationshipSubtypeGUIDs";
        final String asOfTimeParameter            = "asOfTime";
        final String pageSizeParameter            = "pageSize";

        /*
         * Validate parameters
         */
        super.basicRequestValidation(userId, methodName);
        repositoryValidator.validateOptionalTypeGUIDs(repositoryName, guidParameterName, relationshipTypeGUID, subtypeGuidsParameterName, relationshipSubtypeGUIDs, methodName);
        repositoryValidator.validateAsOfTime(repositoryName, asOfTimeParameter, asOfTime, methodName);
        repositoryValidator.validatePageSize(repositoryName, pageSizeParameter, pageSize, methodName);
        repositoryValidator.validateSearchProperties(repositoryName, matchPropertiesParameterName, matchProperties, methodName);
    }


    /**
     * Validate the parameters passed to findRelationshipsByProperty.
     *
     * @param userId unique identifier for requesting user.
     * @param relationshipTypeGUID unique identifier (guid) for the new relationship's type.  Null means all types
     *                             (but may be slow so not recommended).
     * @param matchProperties list of properties used to narrow the search.  The property values may include
     *                        regex style wild cards.
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
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws TypeErrorException the type guid passed on the request is not known by the
     *                              metadata collection.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws PropertyErrorException the properties specified are not valid for any of the requested types of
     *                                  relationships.
     * @throws PagingErrorException the paging/sequencing parameters are set up incorrectly.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @SuppressWarnings(value = "unused")
    protected void findRelationshipsByPropertyParameterValidation(String                    userId,
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
                                                                                                             UserNotAuthorizedException
    {
        final String methodName                   = "findRelationshipsByProperty";
        final String matchCriteriaParameterName   = "matchCriteria";
        final String matchPropertiesParameterName = "matchProperties";
        final String guidParameterName            = "relationshipTypeGUID";
        final String asOfTimeParameter            = "asOfTime";
        final String pageSizeParameter            = "pageSize";

        /*
         * Validate parameters
         */
        super.basicRequestValidation(userId, methodName);
        repositoryValidator.validateOptionalTypeGUID(repositoryName, guidParameterName, relationshipTypeGUID, methodName);
        repositoryValidator.validateAsOfTime(repositoryName, asOfTimeParameter, asOfTime, methodName);
        repositoryValidator.validatePageSize(repositoryName, pageSizeParameter, pageSize, methodName);
        repositoryValidator.validateMatchCriteria(repositoryName,
                                                  matchCriteriaParameterName,
                                                  matchPropertiesParameterName,
                                                  matchCriteria,
                                                  matchProperties,
                                                  methodName);
    }


    /**
     * Validate the parameters passed to findRelationshipsByPropertyName.
     *
     * @param userId unique identifier for requesting user.
     * @param relationshipTypeGUID GUID of the type of entity to search for. Null means all types will
     *                       be searched (could be slow so not recommended).
     * @param searchCriteria String expression contained in any of the property values within the entities
     *                       of the supplied type.
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
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws TypeErrorException the type guid passed on the request is not known by the
     *                              metadata collection.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws PropertyErrorException there is a problem with one of the other parameters.
     * @throws PagingErrorException the paging/sequencing parameters are set up incorrectly.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @SuppressWarnings(value = "unused")
    protected void findRelationshipsByPropertyValueParameterValidation(String                    userId,
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
                                                                                                                  UserNotAuthorizedException
    {
        final String methodName                  = "findRelationshipsByPropertyName";
        final String searchCriteriaParameterName = "searchCriteria";
        final String asOfTimeParameter           = "asOfTime";
        final String typeGUIDParameter           = "relationshipTypeGUID";
        final String pageSizeParameter           = "pageSize";

        /*
         * Validate parameters
         */
        super.basicRequestValidation(userId, methodName);
        repositoryValidator.validateOptionalTypeGUID(repositoryName,
                                                     typeGUIDParameter,
                                                     relationshipTypeGUID,
                                                     methodName);
        repositoryValidator.validateSearchCriteria(repositoryName, searchCriteriaParameterName, searchCriteria, methodName);
        repositoryValidator.validateAsOfTime(repositoryName, asOfTimeParameter, asOfTime, methodName);
        repositoryValidator.validatePageSize(repositoryName, pageSizeParameter, pageSize, methodName);
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
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws TypeErrorException the type guid passed on the request is not known by the
     *                              metadata collection.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by the entityGUID is not found in the metadata collection.
     * @throws PropertyErrorException there is a problem with one of the other parameters.
     * @throws FunctionNotSupportedException the repository does not support this call.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @SuppressWarnings(value = "unused")
    protected void getEntityNeighborhoodParameterValidation(String               userId,
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
        super.basicRequestValidation(userId, methodName);
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
    }


    /**
     * Validate the parameters passed to getLinkingEntities.
     *
     * @param userId unique identifier for requesting user.
     * @param startEntityGUID The entity that is used to anchor the query.
     * @param endEntityGUID the other entity that defines the scope of the query.
     * @param limitResultsByStatus By default, relationships in all non-DELETED statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                             status values except DELETED.
     * @param asOfTime Requests a historical query of the relationships for the entity.  Null means return the
     *                 present values.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws PropertyErrorException there is a problem with one of the other parameters.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @SuppressWarnings(value = "unused")
    protected void getLinkingEntitiesParameterValidation(String                    userId,
                                                         String                    startEntityGUID,
                                                         String                    endEntityGUID,
                                                         List<InstanceStatus>      limitResultsByStatus,
                                                         Date                      asOfTime) throws InvalidParameterException,
                                                                                                    RepositoryErrorException,
                                                                                                    PropertyErrorException,
                                                                                                    UserNotAuthorizedException
    {
        final String methodName                   = "getLinkingEntities";
        final String startEntityGUIDParameterName = "startEntityGUID";
        final String endEntityGUIDParameterName   = "entityGUID";
        final String asOfTimeParameter            = "asOfTime";

        super.basicRequestValidation(userId, methodName);
        repositoryValidator.validateGUID(repositoryName, startEntityGUIDParameterName, startEntityGUID, methodName);
        repositoryValidator.validateGUID(repositoryName, endEntityGUIDParameterName, endEntityGUID, methodName);
        repositoryValidator.validateAsOfTime(repositoryName, asOfTimeParameter, asOfTime, methodName);
    }


    /**
     * Validate the parameters passed to the getRelatedEntities.
     *
     * @param userId unique identifier for requesting user.
     * @param startEntityGUID unique identifier of the starting entity.
     * @param entityTypeGUIDs list of types to search for.  Null means any type.
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
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @SuppressWarnings(value = "unused")
    protected void getRelatedEntitiesParameterValidation(String               userId,
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
        super.basicRequestValidation(userId, methodName);
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

        /*
         * Validate parameters
         */
        this.getInstanceParameterValidation(userId, guid, methodName);

        /*
         * Perform operation
         */
        reportUnsupportedMandatoryFunction(methodName);
        return null;
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

        /*
         * Validate parameters
         */
        this.getInstanceParameterValidation(userId, guid, methodName);

        /*
         * Perform operation
         */
        reportUnsupportedMandatoryFunction(methodName);
        return null;
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
    public EntityDetail getEntityDetail(String     userId,
                                        String     guid) throws InvalidParameterException,
                                                                RepositoryErrorException,
                                                                EntityNotKnownException,
                                                                EntityProxyOnlyException,
                                                                UserNotAuthorizedException
    {
        final String  methodName        = "getEntityDetail";

        /*
         * Validate parameters
         */
        this.getInstanceParameterValidation(userId, guid, methodName);

        /*
         * Perform operation
         */
        reportUnsupportedMandatoryFunction(methodName);
        return null;
    }


    /**
     * Return a historical version of an entity. This includes the header, classifications and properties of the entity.
     *
     * @param userId unique identifier for requesting user.
     * @param guid String unique identifier for the entity.
     * @param asOfTime the time used to determine which version of the entity that is desired.
     * @return EntityDetail structure.
     * @throws InvalidParameterException the guid or date is null or date is for a future time.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                 the metadata collection is stored.
     * @throws EntityNotKnownException the requested entity instance is not known in the metadata collection
     *                                   at the time requested.
     * @throws EntityProxyOnlyException the requested entity instance is only a proxy in the metadata collection.
     * @throws FunctionNotSupportedException the repository does not support the asOfTime parameter.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public  EntityDetail getEntityDetail(String     userId,
                                         String     guid,
                                         Date       asOfTime) throws InvalidParameterException,
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
        this.getInstanceParameterValidation(userId, guid, asOfTime, methodName);

        /*
         * Perform operation
         */
        reportUnsupportedAsOfTimeFunction(methodName, asOfTime);
        return null;
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
        reportUnsupportedOptionalFunction(methodName);
        return null;
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
     * @throws PropertyErrorException the sequencing property is not valid for the retrieved relationships.
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

        /*
         * Validate parameters
         */
        this.getRelationshipsForEntityParameterValidation(userId,
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
        reportUnsupportedAsOfTimeFunction(methodName, asOfTime);

        return null;
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
        final String  methodName                   = "findEntities";

        /*
         * Validate parameters
         */
        this.findEntitiesParameterValidation(userId,
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
        reportUnsupportedOptionalFunction(methodName);
        return null;
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
        final String  methodName                   = "findEntitiesByProperty";

        /*
         * Validate parameters
         */
        this.findEntitiesByPropertyParameterValidation(userId,
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
         * Perform operation
         */
        reportUnsupportedAsOfTimeFunction(methodName, asOfTime);

        return null;
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
        final String  methodName  = "findEntitiesByClassification";

        /*
         * Validate parameters
         */
        this.findEntitiesByClassificationParameterValidation(userId,
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
         * Perform operation
         */
        reportUnsupportedAsOfTimeFunction(methodName, asOfTime);

        return null;
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
        final String  methodName = "findEntitiesByPropertyValue";

        /*
         * Validate parameters
         */
        this.findEntitiesByPropertyValueParameterValidation(userId,
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
         * Perform operation
         */
        reportUnsupportedAsOfTimeFunction(methodName, asOfTime);

        return null;
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

        /*
         * Validate parameters
         */
        this.getInstanceParameterValidation(userId, guid, methodName);

        /*
         * Retrieve relationship
         */
        reportUnsupportedMandatoryFunction(methodName);
        return null;
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
        this.getInstanceParameterValidation(userId, guid, methodName);

        /*
         * Retrieve relationship
         */
        reportUnsupportedMandatoryFunction(methodName);
        return null;
    }



    /**
     * Return a historical version of a relationship.
     *
     * @param userId unique identifier for requesting user.
     * @param guid String unique identifier for the relationship.
     * @param asOfTime the time used to determine which version of the entity that is desired.
     * @return Relationship structure.
     * @throws InvalidParameterException the guid or date is null or the date is for a future time
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

        this.getInstanceParameterValidation(userId, guid, asOfTime, methodName);

        /*
         * Perform operation
         */
        reportUnsupportedAsOfTimeFunction(methodName, asOfTime);

        return null;
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

        this.getInstanceHistoryParameterValidation(userId, guid, fromTime, toTime, methodName);

        /*
         * Perform operation
         */
        reportUnsupportedOptionalFunction(methodName);

        return null;
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
        final String  methodName = "findRelationships";

        this.findRelationshipsParameterValidation(userId,
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
        reportUnsupportedOptionalFunction(methodName);
        return null;
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
     * @throws FunctionNotSupportedException the repository does not support this call.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     * @see OMRSRepositoryHelper#getExactMatchRegex(String)
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

        this.findRelationshipsByPropertyParameterValidation(userId,
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
        reportUnsupportedOptionalFunction(methodName);
        return null;
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
     * @throws FunctionNotSupportedException the repository does not support this call.
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
        final String  methodName = "findRelationshipsByPropertyName";

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
         * Perform operation
         */
        reportUnsupportedOptionalFunction(methodName);
        return null;
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
     * @throws FunctionNotSupportedException the repository does not support this call.
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
        final String methodName  = "getLinkingEntities";

        /*
         * Validate parameters
         */
        this.getLinkingEntitiesParameterValidation(userId,
                                                   startEntityGUID,
                                                   endEntityGUID,
                                                   limitResultsByStatus,
                                                   asOfTime);

        /*
         * Perform operation
         */
        reportUnsupportedOptionalFunction(methodName);
        return null;
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
     * @throws TypeErrorException the type guid passed on the request is not known by the
     *                              metadata collection.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by the entityGUID is not found in the metadata collection.
     * @throws PropertyErrorException there is a problem with one of the other parameters.
     * @throws FunctionNotSupportedException the repository does not support this call.
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
        final String methodName  = "getEntityNeighborhood";

        /*
         * Validate parameters
         */
        this.getEntityNeighborhoodParameterValidation(userId,
                                                      entityGUID,
                                                      entityTypeGUIDs,
                                                      relationshipTypeGUIDs,
                                                      limitResultsByStatus,
                                                      limitResultsByClassification,
                                                      asOfTime,
                                                      level);

        /*
         * Perform operation
         */
        reportUnsupportedOptionalFunction(methodName);
        return null;
    }




    /**
     * Return the list of entities that are of the types listed in entityTypeGUIDs and are connected, either directly or
     * indirectly to the entity identified by startEntityGUID.
     *
     * @param userId unique identifier for requesting user.
     * @param startEntityGUID unique identifier of the starting entity.
     * @param entityTypeGUIDs list of types to search for.  Null means any type.
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
     * @throws TypeErrorException one of the type guids passed on the request is not known by the
     *                              metadata collection.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws EntityNotKnownException the entity identified by the startEntityGUID
     *                                   is not found in the metadata collection.
     * @throws PropertyErrorException the sequencing property specified is not valid for any of the requested types of
     *                                  entity.
     * @throws PagingErrorException the paging/sequencing parameters are set up incorrectly.
     * @throws FunctionNotSupportedException the repository does not support this call.
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

        /*
         * Perform operation
         */
        reportUnsupportedOptionalFunction(methodName);
        return null;
    }


    /* ======================================================
     * Group 4: Maintaining entity and relationship instances
     */


    /**
     * Validate that the parameters passed to addEntity are valid.
     *
     * @param userId unique identifier for requesting user.
     * @param entityTypeGUID unique identifier (guid) for the new entity's type.
     * @param initialProperties initial list of properties for the new entity; null means no properties.
     * @param initialClassifications initial list of classifications for the new entity; null means no classifications.
     * @param initialStatus initial status typically set to DRAFT, PREPARED or ACTIVE.
     * not have any relationships at this stage.
     * @param methodName calling method
     * @return typeDef of the entity's type
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
     */
    protected TypeDef addEntityParameterValidation(String                     userId,
                                                   String                     entityTypeGUID,
                                                   InstanceProperties         initialProperties,
                                                   List<Classification>       initialClassifications,
                                                   InstanceStatus             initialStatus,
                                                   String                     methodName) throws InvalidParameterException,
                                                                                                 RepositoryErrorException,
                                                                                                 TypeErrorException,
                                                                                                 PropertyErrorException,
                                                                                                 ClassificationErrorException,
                                                                                                 StatusNotSupportedException
    {
        final String  entityGUIDParameterName       = "entityTypeGUID";
        final String  propertiesParameterName       = "initialProperties";
        final String  classificationsParameterName  = "initialClassifications";
        final String  initialStatusParameterName    = "initialStatus";

        /*
         * Validate parameters
         */
        super.basicRequestValidation(userId, methodName);
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

        return typeDef;
    }


    /**
     * Validate that the parameters passed to addEntity are valid.
     *
     * @param userId unique identifier for requesting user.
     * @param entityTypeGUID unique identifier (guid) for the new entity's type.
     * @param initialProperties initial list of properties for the new entity; null means no properties.
     * @param initialClassifications initial list of classifications for the new entity; null means no classifications.
     * @param initialStatus initial status typically set to DRAFT, PREPARED or ACTIVE.
     * not have any relationships at this stage.
     * @return typeDef of the entity's type
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
    @Deprecated
    protected TypeDef addEntityParameterValidation(String                     userId,
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
        final String  methodName    = "addEntity";

        return this.addEntityParameterValidation(userId,
                                                 entityTypeGUID,
                                                 initialProperties,
                                                 initialClassifications,
                                                 initialStatus,
                                                 methodName);
    }


    /**
     * Validate parameters needed to save a new entity that is sourced from an external technology.
     *
     * @param userId unique identifier for requesting user.
     * @param entityTypeGUID unique identifier (guid) for the new entity's type.
     * @param externalSourceGUID unique identifier (guid) for the external source.
     * @param initialProperties initial list of properties for the new entity; null means no properties.
     * @param initialClassifications initial list of classifications for the new entity null means no classifications.
     * @param initialStatus initial status typically DRAFT, PREPARED or ACTIVE.
     * @param methodName calling method
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
    protected TypeDef addExternalEntityParameterValidation(String                     userId,
                                                           String                     entityTypeGUID,
                                                           String                     externalSourceGUID,
                                                           InstanceProperties         initialProperties,
                                                           List<Classification>       initialClassifications,
                                                           InstanceStatus             initialStatus,
                                                           String                     methodName) throws InvalidParameterException,
                                                                                                         RepositoryErrorException,
                                                                                                         TypeErrorException,
                                                                                                         PropertyErrorException,
                                                                                                         ClassificationErrorException,
                                                                                                         StatusNotSupportedException,
                                                                                                         UserNotAuthorizedException
    {
        final String  guidParameterName       = "entityTypeGUID";

        /*
         * Validate parameters
         */
        repositoryValidator.validateGUID(repositoryName, guidParameterName, externalSourceGUID, methodName);

        return this.addEntityParameterValidation(userId,
                                                 entityTypeGUID,
                                                 initialProperties,
                                                 initialClassifications,
                                                 initialStatus,
                                                 methodName);
    }


    /**
     * Validate the parameters for addEntityProxy.
     *
     * @param userId unique identifier for requesting user.
     * @param entityProxy details of entity to add.
     * @throws InvalidParameterException the entity proxy is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     */
    protected void addEntityProxyParameterValidation(String       userId,
                                                     EntityProxy  entityProxy) throws InvalidParameterException,
                                                                                      RepositoryErrorException
    {

        final String  methodName         = "addEntityProxy";
        final String  proxyParameterName = "entityProxy";

        /*
         * Validate parameters
         */
        super.basicRequestValidation(userId, methodName);

        repositoryValidator.validateEntityProxy(repositoryName,
                                                metadataCollectionId,
                                                proxyParameterName,
                                                entityProxy,
                                                methodName);
    }


    /**
     * Validate parameters for updateEntityStatus.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID unique identifier (guid) for the requested entity.
     * @param newStatus new InstanceStatus for the entity.
     * @param methodName calling method
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     */
    protected void updateInstanceStatusParameterValidation(String           userId,
                                                           String           entityGUID,
                                                           InstanceStatus   newStatus,
                                                           String           methodName) throws InvalidParameterException,
                                                                                               RepositoryErrorException
    {
        final String  entityGUIDParameterName  = "entityGUID";
        final String  statusParameterName      = "newStatus";

        super.basicRequestValidation(userId, methodName);
        repositoryValidator.validateGUID(repositoryName, entityGUIDParameterName, entityGUID, methodName);

        if (newStatus == null)
        {
            throw new InvalidParameterException(OMRSErrorCode.NULL_INSTANCE_STATUS.getMessageDefinition(statusParameterName,
                                                                                                        methodName,
                                                                                                        repositoryName),
                                                this.getClass().getName(),
                                                methodName,
                                                statusParameterName);
        }
    }


    /**
     * Validate the parameters for update the properties of an instance.
     *
     * @param userId unique identifier for requesting user.
     * @param instanceGUID String unique identifier (guid) for the entity.
     * @param properties a list of properties to change.
     * @param methodName calling method
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     */
    @SuppressWarnings(value = "unused")
    protected void updateInstancePropertiesPropertyValidation(String               userId,
                                                              String               instanceGUID,
                                                              InstanceProperties   properties,
                                                              String               methodName) throws InvalidParameterException,
                                                                                                      RepositoryErrorException
    {
        final String  guidParameterName  = "instanceGUID";

        super.basicRequestValidation(userId, methodName);
        repositoryValidator.validateGUID(repositoryName, guidParameterName, instanceGUID, methodName);
    }


    /**
     * Validate the parameters for calls that manage an existing instance.
     *
     * @param userId unique identifier for requesting user.
     * @param instanceGUID String unique identifier (guid) for the instance.
     * @param instanceGUIDParameterName name of parameter for instance's GUID
     * @param methodName name of the calling method.
     * @throws InvalidParameterException the guid is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     */
    protected void manageInstanceParameterValidation(String  userId,
                                                     String  instanceGUID,
                                                     String  instanceGUIDParameterName,
                                                     String  methodName) throws InvalidParameterException,
                                                                                RepositoryErrorException
    {

        super.basicRequestValidation(userId, methodName);
        repositoryValidator.validateGUID(repositoryName, instanceGUIDParameterName, instanceGUID, methodName);
    }


    /**
     * Validate the parameters for calls that manage an existing instance.
     *
     * @param userId unique identifier for requesting user.
     * @param instanceGUID String unique identifier (guid) for the instance.
     * @param methodName name of the calling method.
     * @throws InvalidParameterException the guid is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Deprecated
    protected void manageInstanceParameterValidation(String  userId,
                                                     String  instanceGUID,
                                                     String  methodName) throws InvalidParameterException,
                                                                                RepositoryErrorException,
                                                                                UserNotAuthorizedException
    {
        final String  instanceGUIDParameterName  = "instanceGUID";

        this.manageInstanceParameterValidation(userId, instanceGUID, instanceGUIDParameterName, methodName);
    }


    /**
     * Validate the parameters passed to delete() or purge() instance method.
     *
     * @param userId unique identifier for requesting user.
     * @param typeDefGUID unique identifier of the type of the instance to delete.
     * @param typeDefName unique name of the type of the instance to delete.
     * @param instanceGUID String unique identifier (guid) for the instance.
     * @param instanceGUIDParameterName name of parameter for instance's GUID
     * @param methodName calling method
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     */
    protected void manageInstanceParameterValidation(String userId,
                                                     String typeDefGUID,
                                                     String typeDefName,
                                                     String instanceGUID,
                                                     String instanceGUIDParameterName,
                                                     String methodName) throws InvalidParameterException,
                                                                               RepositoryErrorException
    {
        final String  typeDefGUIDParameterName   = "typeDefGUID";
        final String  typeDefNameParameterName   = "typeDefName";

        super.basicRequestValidation(userId, methodName);
        repositoryValidator.validateTypeDefIds(repositoryName,
                                               typeDefGUIDParameterName,
                                               typeDefNameParameterName,
                                               typeDefGUID,
                                               typeDefName,
                                               methodName);
        repositoryValidator.validateGUID(repositoryName, instanceGUIDParameterName, instanceGUID, methodName);
    }





    /**
     * Validate the parameters passed to delete() or purge() instance method.
     *
     * @param userId unique identifier for requesting user.
     * @param typeDefGUID unique identifier of the type of the instance to delete.
     * @param typeDefName unique name of the type of the instance to delete.
     * @param instanceGUID String unique identifier (guid) for the instance.
     * @param methodName calling method
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Deprecated
    protected void  removeInstanceParameterValidation(String userId,
                                                      String typeDefGUID,
                                                      String typeDefName,
                                                      String instanceGUID,
                                                      String methodName) throws InvalidParameterException,
                                                                                RepositoryErrorException,
                                                                                UserNotAuthorizedException
    {
        final String  instanceGUIDParameterName  = "instanceGUID";

        this.manageInstanceParameterValidation(userId, typeDefGUID, typeDefName, instanceGUID, instanceGUIDParameterName, methodName);
    }


    /**
     * Validate the parameters passed to classifyEntity.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID String unique identifier (guid) for the entity.
     * @param classificationName String name for the classification.
     * @param classificationProperties list of properties to set in the classification.
     * @param methodName calling method
     * @return typeDef for the classification
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                characteristics in the TypeDef for this classification type
     */
    protected TypeDef  classifyEntityParameterValidation(String               userId,
                                                         String               entityGUID,
                                                         String               classificationName,
                                                         InstanceProperties   classificationProperties,
                                                         String               methodName) throws InvalidParameterException,
                                                                                                 RepositoryErrorException,
                                                                                                 PropertyErrorException
    {

        final String entityGUIDParameterName     = "entityGUID";
        final String classificationParameterName = "classificationName";
        final String propertiesParameterName     = "classificationProperties";

        super.basicRequestValidation(userId, methodName);
        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateGUID(repositoryName, entityGUIDParameterName, entityGUID, methodName);
        TypeDef typeDef = repositoryValidator.validateClassificationName(repositoryName, classificationParameterName, classificationName, methodName);
        repositoryValidator.validateClassificationProperties(repositoryName,
                                                             classificationName,
                                                             propertiesParameterName,
                                                             classificationProperties,
                                                             methodName);

        return typeDef;
    }


    /**
     * Validate the parameters passed to classifyEntity.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID String unique identifier (guid) for the entity.
     * @param classificationName String name for the classification.
     * @param classificationProperties list of properties to set in the classification.
     * @return typeDef for the classification
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                characteristics in the TypeDef for this classification type
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Deprecated
    protected TypeDef  classifyEntityParameterValidation(String               userId,
                                                         String               entityGUID,
                                                         String               classificationName,
                                                         InstanceProperties   classificationProperties) throws InvalidParameterException,
                                                                                                               RepositoryErrorException,
                                                                                                               PropertyErrorException,
                                                                                                               UserNotAuthorizedException
    {
        final String  methodName   = "classifyEntity";

        return classifyEntityParameterValidation(userId, entityGUID, classificationName, classificationProperties, methodName);
    }

    /**
     * Validate the parameters passed to classifyEntity.
     *
     * @param userId unique identifier for requesting user.
     * @param entityProxy identifier (proxy) for the entity.
     * @param classificationName String name for the classification.
     * @param classificationProperties list of properties to set in the classification.
     * @param methodName calling method
     *
     * @return typeDef for the classification
     *
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                characteristics in the TypeDef for this classification type
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    protected TypeDef  classifyEntityParameterValidation(String               userId,
                                                         EntityProxy          entityProxy,
                                                         String               classificationName,
                                                         InstanceProperties   classificationProperties,
                                                         String               methodName) throws InvalidParameterException,
                                                                                                 RepositoryErrorException,
                                                                                                 PropertyErrorException,
                                                                                                 UserNotAuthorizedException
    {
        if (entityProxy == null)
        {
            throw new InvalidParameterException(OMRSErrorCode.NULL_ENTITY_PROXY.getMessageDefinition(repositoryName, "entityProxy", methodName),
                    this.getClass().getName(),
                    methodName,
                    "entityProxy");
        }

        return classifyEntityParameterValidation(userId, entityProxy.getGUID(), classificationName, classificationProperties, methodName);
    }


    /**
     * Validate the parameters passed to declassifyEntity.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID String unique identifier (guid) for the entity.
     * @param classificationName String name for the classification.
     * @param methodName calling method
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     */
    protected void declassifyEntityParameterValidation(String    userId,
                                                       String    entityGUID,
                                                       String    classificationName,
                                                       String    methodName) throws InvalidParameterException,
                                                                                    RepositoryErrorException
    {
        final String  entityGUIDParameterName     = "entityGUID";
        final String  classificationParameterName = "classificationName";

        super.basicRequestValidation(userId, methodName);
        repositoryValidator.validateGUID(repositoryName, entityGUIDParameterName, entityGUID, methodName);
        repositoryValidator.validateClassificationName(repositoryName,
                                                       classificationParameterName,
                                                       classificationName,
                                                       methodName);
    }


    /**
     * Validate the parameters passed to declassifyEntity.
     *
     * @param userId unique identifier for requesting user.
     * @param entityProxy proxy for the entity.
     * @param classificationName String name for the classification.
     * @param methodName calling method
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     */
    protected void declassifyEntityParameterValidation(String      userId,
                                                       EntityProxy entityProxy,
                                                       String      classificationName,
                                                       String      methodName) throws InvalidParameterException,
                                                                                      RepositoryErrorException
    {
        final String  entityGUIDParameterName     = "entityGUID";
        final String  classificationParameterName = "classificationName";

        super.basicRequestValidation(userId, methodName);

        if (entityProxy == null)
        {
            repositoryValidator.validateGUID(repositoryName, entityGUIDParameterName, null, methodName);
        }
        else
        {
            repositoryValidator.validateGUID(repositoryName, entityGUIDParameterName, entityProxy.getGUID(), methodName);
        }

        repositoryValidator.validateClassificationName(repositoryName,
                                                       classificationParameterName,
                                                       classificationName,
                                                       methodName);
    }


    /**
     * Validate the parameters passed to declassifyEntity.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID String unique identifier (guid) for the entity.
     * @param classificationName String name for the classification.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     */
    @Deprecated
    protected void declassifyEntityParameterValidation(String    userId,
                                                       String    entityGUID,
                                                       String    classificationName) throws InvalidParameterException,
                                                                                            RepositoryErrorException
    {

        final String  methodName     = "declassifyEntity";

        this.declassifyEntityParameterValidation(userId, entityGUID, classificationName, methodName);
    }


    /**
     * Validate the parameters passed to updateEntityClassification.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID String unique identifier (guid) for the entity.
     * @param classificationName String name for the classification.
     * @param properties list of properties for the classification.
     * @return typeDef for the classification
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                characteristics in the TypeDef for this classification type
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Deprecated
    protected TypeDef updateEntityClassificationParameterValidation(String               userId,
                                                                    String               entityGUID,
                                                                    String               classificationName,
                                                                    InstanceProperties   properties) throws InvalidParameterException,
                                                                                                            RepositoryErrorException,
                                                                                                            PropertyErrorException,
                                                                                                            UserNotAuthorizedException
    {
        final String  methodName = "updateEntityClassification";

        return classifyEntityParameterValidation(userId, entityGUID, classificationName, properties, methodName);
    }



    /**
     * Validate the parameters passed to updateEntityClassification.
     *
     * @param userId unique identifier for requesting user.
     * @param entityProxy proxy for the entity.
     * @param classificationName String name for the classification.
     * @param properties list of properties for the classification.
     * @return typeDef for the classification
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                characteristics in the TypeDef for this classification type
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Deprecated
    protected TypeDef updateEntityClassificationParameterValidation(String               userId,
                                                                    EntityProxy          entityProxy,
                                                                    String               classificationName,
                                                                    InstanceProperties   properties) throws InvalidParameterException,
                                                                                                            RepositoryErrorException,
                                                                                                            PropertyErrorException,
                                                                                                            UserNotAuthorizedException
    {
        final String  methodName = "updateEntityClassification";

        return classifyEntityParameterValidation(userId, entityProxy, classificationName, properties, methodName);
    }


    /**
     * Validate the parameters for adding a new relationship between two entities to the metadata collection.
     *
     * @param userId unique identifier for requesting user.
     * @param relationshipTypeGUID unique identifier (guid) for the new relationship's type.
     * @param initialProperties initial list of properties for the new entity; null means no properties.
     * @param entityOneGUID the unique identifier of one of the entities that the relationship is connecting together.
     * @param entityTwoGUID the unique identifier of the other entity that the relationship is connecting together.
     * @param initialStatus initial status typically set to DRAFT, PREPARED or ACTIVE.
     * @param methodName calling method
     * @return type definition
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                 the metadata collection is stored.
     * @throws TypeErrorException the requested type is not known, or not supported in the metadata repository
     *                            hosting the metadata collection.
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                  characteristics in the TypeDef for this relationship's type.
     * @throws StatusNotSupportedException the metadata repository hosting the metadata collection does not support
     *                                     the requested status.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    protected TypeDef addRelationshipParameterValidation(String               userId,
                                                         String               relationshipTypeGUID,
                                                         InstanceProperties   initialProperties,
                                                         String               entityOneGUID,
                                                         String               entityTwoGUID,
                                                         InstanceStatus       initialStatus,
                                                         String               methodName) throws InvalidParameterException,
                                                                                                 RepositoryErrorException,
                                                                                                 TypeErrorException,
                                                                                                 PropertyErrorException,
                                                                                                 StatusNotSupportedException,
                                                                                                 UserNotAuthorizedException
    {
        final String guidParameterName          = "relationshipTypeGUID";
        final String entityOneParameterName     = "entityOneGUID";
        final String entityTwoParameterName     = "entityTwoGUID";
        final String propertiesParameterName    = "initialProperties";
        final String initialStatusParameterName = "initialStatus";

        /*
         * Validate parameters
         */
        super.basicRequestValidation(userId, methodName);
        repositoryValidator.validateTypeGUID(repositoryName, guidParameterName, relationshipTypeGUID, methodName);
        repositoryValidator.validateGUID(repositoryName, entityOneParameterName, entityOneGUID, methodName);
        repositoryValidator.validateGUID(repositoryName, entityTwoParameterName, entityTwoGUID, methodName);

        TypeDef typeDef = repositoryHelper.getTypeDef(repositoryName, guidParameterName, relationshipTypeGUID, methodName);

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

        return typeDef;
    }


    /**
     * Validate the parameters for adding a new relationship between two entities to the metadata collection.
     *
     * @param userId unique identifier for requesting user.
     * @param relationshipTypeGUID unique identifier (guid) for the new relationship's type.
     * @param initialProperties initial list of properties for the new entity; null means no properties.
     * @param entityOneGUID the unique identifier of one of the entities that the relationship is connecting together.
     * @param entityTwoGUID the unique identifier of the other entity that the relationship is connecting together.
     * @param initialStatus initial status typically set to DRAFT, PREPARED or ACTIVE.
     * @return type definition
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                 the metadata collection is stored.
     * @throws TypeErrorException the requested type is not known, or not supported in the metadata repository
     *                            hosting the metadata collection.
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                  characteristics in the TypeDef for this relationship's type.
     * @throws StatusNotSupportedException the metadata repository hosting the metadata collection does not support
     *                                     the requested status.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Deprecated
    protected TypeDef addRelationshipParameterValidation(String               userId,
                                                         String               relationshipTypeGUID,
                                                         InstanceProperties   initialProperties,
                                                         String               entityOneGUID,
                                                         String               entityTwoGUID,
                                                         InstanceStatus       initialStatus) throws InvalidParameterException,
                                                                                                    RepositoryErrorException,
                                                                                                    TypeErrorException,
                                                                                                    PropertyErrorException,
                                                                                                    StatusNotSupportedException,
                                                                                                    UserNotAuthorizedException
    {
        final String methodName          = "addRelationship";

        return this.addRelationshipParameterValidation(userId,
                                                       relationshipTypeGUID,
                                                       initialProperties,
                                                       entityOneGUID,
                                                       entityTwoGUID,
                                                       initialStatus,
                                                       methodName);
    }


    /**
     * Validate the parameters needed to save a new relationship that is sourced from an external technology.
     *
     * @param userId unique identifier for requesting user.
     * @param relationshipTypeGUID unique identifier (guid) for the new relationship's type.
     * @param externalSourceGUID unique identifier (guid) for the external source.
     * @param initialProperties initial list of properties for the new entity; null means no properties.
     * @param entityOneGUID the unique identifier of one of the entities that the relationship is connecting together.
     * @param entityTwoGUID the unique identifier of the other entity that the relationship is connecting together.
     * @param initialStatus initial status; typically DRAFT, PREPARED or ACTIVE.
     * @param methodName calling method
     * @return Relationship structure with the new header, requested entities and properties.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                 the metadata collection is stored.
     * @throws TypeErrorException the requested type is not known, or not supported in the metadata repository
     *                            hosting the metadata collection.
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different
     *                                  characteristics in the TypeDef for this relationship's type.
     * @throws StatusNotSupportedException the metadata repository hosting the metadata collection does not support
     *                                     the requested status.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    protected TypeDef addExternalRelationshipParameterValidation(String               userId,
                                                                 String               relationshipTypeGUID,
                                                                 String               externalSourceGUID,
                                                                 InstanceProperties   initialProperties,
                                                                 String               entityOneGUID,
                                                                 String               entityTwoGUID,
                                                                 InstanceStatus       initialStatus,
                                                                 String               methodName) throws InvalidParameterException,
                                                                                                         RepositoryErrorException,
                                                                                                         TypeErrorException,
                                                                                                         PropertyErrorException,
                                                                                                         StatusNotSupportedException,
                                                                                                         UserNotAuthorizedException
    {
        final String guidParameterName          = "relationshipTypeGUID";

        /*
         * Validate additional parameters
         */
        repositoryValidator.validateGUID(repositoryName, guidParameterName, externalSourceGUID, methodName);

        /*
         * Validate parameters for any add relationship
         */
        return this.addRelationshipParameterValidation(userId,
                                                       relationshipTypeGUID,
                                                       initialProperties,
                                                       entityOneGUID,
                                                       entityTwoGUID,
                                                       initialStatus,
                                                       methodName);
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
        final String  methodName    = "addEntity";

        this.addEntityParameterValidation(userId,
                                          entityTypeGUID,
                                          initialProperties,
                                          initialClassifications,
                                          initialStatus,
                                          methodName);

        /*
         * Validation complete; ok to create new instance
         */
        reportUnsupportedOptionalFunction(methodName);
        return null;
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
    public EntityDetail addExternalEntity(String             userId,
                                          String             entityTypeGUID,
                                          String             externalSourceGUID,
                                          String             externalSourceName,
                                          InstanceProperties initialProperties,
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

        this.addExternalEntityParameterValidation(userId,
                                                  entityTypeGUID,
                                                  externalSourceGUID,
                                                  initialProperties,
                                                  initialClassifications,
                                                  initialStatus,
                                                  methodName);

        /*
         * Add entity
         */
        reportUnsupportedOptionalFunction(methodName);
        return null;
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

        /*
         * Validate parameters
         */
        this.addEntityProxyParameterValidation(userId, entityProxy);

        /*
         * Validation complete
         */
        reportUnsupportedOptionalFunction(methodName);
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
        final String  methodName   = "updateEntityStatus";

        /*
         * Validate parameters
         */
        this.updateInstanceStatusParameterValidation(userId, entityGUID, newStatus, methodName);

        /*
         * Locate entity
         */
        reportUnsupportedOptionalFunction(methodName);
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
        this.updateInstancePropertiesPropertyValidation(userId, entityGUID, properties, methodName);

        /*
         * Locate entity
         */
        reportUnsupportedOptionalFunction(methodName);
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
    public EntityDetail undoEntityUpdate(String  userId,
                                         String  entityGUID) throws InvalidParameterException,
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
        this.manageInstanceParameterValidation(userId, entityGUID, parameterName, methodName);

        /*
         * Validation complete; ok to restore entity
         */
        reportUnsupportedOptionalFunction(methodName);
        return null;
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
        final String methodName               = "deleteEntity";
        final String entityParameterName      = "obsoleteEntityGUID";

        /*
         * Validate parameters
         */
        this.manageInstanceParameterValidation(userId,
                                               typeDefGUID,
                                               typeDefName,
                                               obsoleteEntityGUID,
                                               entityParameterName,
                                               methodName);

        /*
         * Delete Entity
         */
        reportUnsupportedOptionalFunction(methodName);
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
        final String  methodName               = "purgeEntity";
        final String  guidParameterName        = "deletedEntityGUID";

        /*
         * Validate parameters
         */
        this.manageInstanceParameterValidation(userId,
                                               typeDefGUID,
                                               typeDefName,
                                               deletedEntityGUID,
                                               guidParameterName,
                                               methodName);

        /*
         * Purge entity
         */
        reportUnsupportedOptionalFunction(methodName);
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
        final String  guidParameterName       = "deletedEntityGUID";

        /*
         * Validate parameters
         */
        this.manageInstanceParameterValidation(userId, deletedEntityGUID, guidParameterName, methodName);

        /*
         * Restore entity
         */
        reportUnsupportedOptionalFunction(methodName);
        return null;
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
        final String  methodName   = "classifyEntity";

        /*
         * Validate parameters
         */
        this.classifyEntityParameterValidation(userId,
                                               entityGUID,
                                               classificationName,
                                               classificationProperties,
                                               methodName);

        /*
         * Update entity
         */
        reportUnsupportedOptionalFunction(methodName);
        return null;
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
        this.classifyEntityParameterValidation(userId,
                                               entityGUID,
                                               classificationName,
                                               classificationProperties,
                                               methodName);

        /*
         * Update entity
         */
        reportUnsupportedOptionalFunction(methodName);
        return null;
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
    public EntityDetail declassifyEntity(String  userId,
                                         String  entityGUID,
                                         String  classificationName) throws InvalidParameterException,
                                                                            RepositoryErrorException,
                                                                            EntityNotKnownException,
                                                                            ClassificationErrorException,
                                                                            FunctionNotSupportedException,
                                                                            UserNotAuthorizedException
    {
        final String  methodName                  = "declassifyEntity";

        /*
         * Validate parameters
         */
        this.declassifyEntityParameterValidation(userId, entityGUID, classificationName, methodName);

        /*
         * Update entity
         */
        reportUnsupportedOptionalFunction(methodName);
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
        final String  methodName = "updateEntityClassification";

        /*
         * Validate parameters
         */
        this.classifyEntityParameterValidation(userId, entityGUID, classificationName, properties, methodName);

        /*
         * Update entity
         */
        reportUnsupportedOptionalFunction(methodName);
        return null;
    }


    /**
     * Retrieve an entity proxy
     * @param userId calling user
     * @param entityGUID entity guid
     * @param methodName calling method
     * @return entity proxy
     * @throws InvalidParameterException one of the parameters is null
     * @throws RepositoryErrorException there is a problem communicating with the remote repository
     * @throws EntityNotKnownException the guid is not recognised
     * @throws UserNotAuthorizedException the calling user can not access this entity.
     */
    protected  EntityProxy getEntityProxy(String   userId,
                                          String   entityGUID,
                                          String   methodName) throws InvalidParameterException,
                                                                      RepositoryErrorException,
                                                                      EntityNotKnownException,
                                                                      UserNotAuthorizedException
    {
        EntitySummary entitySummary = this.getEntitySummary(userId, entityGUID);

        if (entitySummary != null)
        {
            return new EntityProxy(entitySummary);
        }

        reportEntityNotKnown(entityGUID, methodName);
        return null;
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
        this.addRelationshipParameterValidation(userId,
                                                relationshipTypeGUID,
                                                initialProperties,
                                                entityOneGUID,
                                                entityTwoGUID,
                                                initialStatus,
                                                methodName);

        /*
         * Validation complete, ok to create new instance
         */
        reportUnsupportedOptionalFunction(methodName);
        return null;
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
        this.addExternalRelationshipParameterValidation(userId,
                                                        relationshipTypeGUID,
                                                        externalSourceGUID,
                                                        initialProperties,
                                                        entityOneGUID,
                                                        entityTwoGUID,
                                                        initialStatus,
                                                        methodName);

        /*
         * Validation complete, ok to create new instance
         */
        reportUnsupportedOptionalFunction(methodName);
        return null;
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
        final String  methodName     = "updateRelationshipStatus";

        /*
         * Validate parameters
         */
        this.updateInstanceStatusParameterValidation(userId, relationshipGUID, newStatus, methodName);

        /*
         * Update relationship
         */
        reportUnsupportedOptionalFunction(methodName);
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
         * Update relationship
         */
        reportUnsupportedOptionalFunction(methodName);
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
    public Relationship undoRelationshipUpdate(String  userId,
                                               String  relationshipGUID) throws InvalidParameterException,
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
         * Restore previous version
         */
        reportUnsupportedOptionalFunction(methodName);
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
        final String  relationshipParameterName = "obsoleteRelationshipGUID";

        /*
         * Validate parameters
         */
        this.manageInstanceParameterValidation(userId,
                                               typeDefGUID,
                                               typeDefName,
                                               obsoleteRelationshipGUID,
                                               relationshipParameterName,
                                               methodName);

        /*
         * Delete relationship
         */
        reportUnsupportedOptionalFunction(methodName);
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
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     * @throws FunctionNotSupportedException the repository does not support maintenance of metadata.
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
        final String  methodName                = "purgeRelationship";
        final String  relationshipParameterName = "deletedRelationshipGUID";

        /*
         * Validate parameters
         */
        this.manageInstanceParameterValidation(userId,
                                               typeDefGUID,
                                               typeDefName,
                                               deletedRelationshipGUID,
                                               relationshipParameterName,
                                               methodName);

        /*
         * Delete relationship
         */
        reportUnsupportedOptionalFunction(methodName);
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
         * Restore relationship
         */
        reportUnsupportedOptionalFunction(methodName);
        return null;
    }


    /* ======================================================================
     * Group 5: Change the control information in entities and relationships
     */


    /**
     *
     * @param userId unique identifier for requesting user.
     * @param typeDefGUID the guid of the TypeDef for the instance - used to verify the entity identity.
     * @param typeDefName the name of the TypeDef for the instance - used to verify the entity identity.
     * @param instanceGUID unique identifier of the instance
     * @param instanceParameterName name of original instance GUID parameter
     * @param newInstanceGUID new unique identifier for the instance
     * @param newInstanceParameterName name of new instance GUID's parameter
     * @param methodName calling method
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    protected void reIdentifyInstanceParameterValidation(String     userId,
                                                         String     typeDefGUID,
                                                         String     typeDefName,
                                                         String     instanceGUID,
                                                         String     instanceParameterName,
                                                         String     newInstanceGUID,
                                                         String     newInstanceParameterName,
                                                         String     methodName) throws InvalidParameterException,
                                                                                       RepositoryErrorException,
                                                                                       UserNotAuthorizedException
    {
        final String  guidParameterName = "typeDefGUID";
        final String  nameParameterName = "typeDefName";

        super.basicRequestValidation(userId, methodName);
        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateGUID(repositoryName, instanceParameterName, instanceGUID, methodName);
        repositoryValidator.validateGUID(repositoryName, newInstanceParameterName, newInstanceGUID, methodName);
        repositoryValidator.validateTypeDefIds(repositoryName,
                                               guidParameterName,
                                               nameParameterName,
                                               typeDefGUID,
                                               typeDefName,
                                               methodName);
    }


    /**
     * Validate the parameters needed to reType an instance.
     *
     * @param userId unique identifier for requesting user.
     * @param instanceGUID unique identifier of the instance
     * @param instanceParameterName name of instance GUID parameter
     * @param expectedTypeDefCategory the category of the instance
     * @param currentTypeDefSummary the current type
     * @param newTypeDefSummary the new type
     * @param methodName calling method
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository.
     * @throws TypeErrorException the requested type is not known, or not supported in the metadata repository
     *                            hosting the metadata collection.
     */
    protected void reTypeInstanceParameterValidation(String          userId,
                                                     String          instanceGUID,
                                                     String          instanceParameterName,
                                                     TypeDefCategory expectedTypeDefCategory,
                                                     TypeDefSummary  currentTypeDefSummary,
                                                     TypeDefSummary  newTypeDefSummary,
                                                     String          methodName) throws InvalidParameterException,
                                                                                        RepositoryErrorException,
                                                                                        TypeErrorException
    {
        final String currentTypeDefParameterName = "currentTypeDefSummary";
        final String newTypeDefParameterName = "newTypeDefSummary";

        this.manageInstanceParameterValidation(userId, instanceGUID, instanceParameterName, methodName);

        repositoryValidator.validateActiveType(repositoryName,
                                               currentTypeDefParameterName,
                                               currentTypeDefSummary,
                                               expectedTypeDefCategory,
                                               methodName);
        repositoryValidator.validateActiveType(repositoryName,
                                               newTypeDefParameterName,
                                               newTypeDefSummary,
                                               expectedTypeDefCategory,
                                               methodName);
    }


    /**
     * Validate that the parameters passed to a reHome request are valid.
     *
     * @param userId unique identifier for requesting user.
     * @param instanceGUID unique identifier of the instance
     * @param instanceParameterName name of instance GUID parameter
     * @param typeDefGUID unique identifier of type
     * @param typeDefName unique name of type
     * @param homeMetadataCollectionId existing home
     * @param newHomeMetadataCollectionId new home
     * @param methodName calling method
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository.
     */
    protected void reHomeInstanceParameterValidation(String          userId,
                                                     String          instanceGUID,
                                                     String          instanceParameterName,
                                                     String          typeDefGUID,
                                                     String          typeDefName,
                                                     String          homeMetadataCollectionId,
                                                     String          newHomeMetadataCollectionId,
                                                     String          methodName) throws InvalidParameterException,
                                                                                        RepositoryErrorException
    {
        final String guidParameterName    = "typeDefGUID";
        final String nameParameterName    = "typeDefName";
        final String homeParameterName    = "homeMetadataCollectionId";
        final String newHomeParameterName = "newHomeMetadataCollectionId";

        this.manageInstanceParameterValidation(userId, instanceGUID, instanceParameterName, methodName);

        repositoryValidator.validateTypeDefIds(repositoryName,
                                               guidParameterName,
                                               nameParameterName,
                                               typeDefGUID,
                                               typeDefName,
                                               methodName);
        repositoryValidator.validateHomeMetadataGUID(repositoryName, homeParameterName, homeMetadataCollectionId, methodName);
        repositoryValidator.validateHomeMetadataGUID(repositoryName, newHomeParameterName, newHomeMetadataCollectionId, methodName);
    }


    /**
     * Change the guid of an existing entity to a new value.  This is used if two different
     * entities are discovered to have the same guid.  This is extremely unlikely but not impossible so
     * the open metadata protocol has provision for this.
     *
     * @param userId unique identifier for requesting user.
     * @param typeDefGUID the guid of the TypeDef for the entity - used to verify the entity identity.
     * @param typeDefName the name of the TypeDef for the entity - used to verify the entity identity.
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
        final String  instanceParameterName = "entityGUID";
        final String  newInstanceParameterName = "newEntityGUID";

        /*
         * Validate parameters
         */
        this.reIdentifyInstanceParameterValidation(userId,
                                                   typeDefGUID,
                                                   typeDefName,
                                                   entityGUID,
                                                   instanceParameterName,
                                                   newEntityGUID,
                                                   newInstanceParameterName,
                                                   methodName);


        /*
         * Update entity
         */
        reportUnsupportedOptionalFunction(methodName);
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

        /*
         * Validate parameters
         */
        this.reTypeInstanceParameterValidation(userId,
                                                entityGUID,
                                                entityParameterName,
                                                TypeDefCategory.ENTITY_DEF,
                                                currentTypeDefSummary,
                                                newTypeDefSummary,
                                                methodName);

        /*
         * Update entity
         */
        reportUnsupportedOptionalFunction(methodName);
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
        final String entityParameterName       = "entityGUID";

        /*
         * Validate parameters
         */
        this.reHomeInstanceParameterValidation(userId,
                                               entityGUID,
                                               entityParameterName,
                                               typeDefGUID,
                                               typeDefName,
                                               homeMetadataCollectionId,
                                               newHomeMetadataCollectionId,
                                               methodName);

        /*
         * Update entity
         */
        reportUnsupportedOptionalFunction(methodName);
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
        final String  methodName = "reIdentifyRelationship";
        final String  instanceParameterName = "relationshipGUID";
        final String  newInstanceParameterName = "newRelationshipGUID";

        /*
         * Validate parameters
         */
        this.reIdentifyInstanceParameterValidation(userId,
                                                   typeDefGUID,
                                                   typeDefName,
                                                   relationshipGUID,
                                                   instanceParameterName,
                                                   newRelationshipGUID,
                                                   newInstanceParameterName,
                                                   methodName);

        /*
         * Validation complete, ok to make changes
         */
        reportUnsupportedOptionalFunction(methodName);
        return null;
    }


    /**
     * Change the existing relationship's type.  Typically, this action is taken to move a relationship's
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

        /*
         * Validate parameters
         */
        this.reTypeInstanceParameterValidation(userId,
                                               relationshipGUID,
                                               relationshipParameterName,
                                               TypeDefCategory.RELATIONSHIP_DEF,
                                               currentTypeDefSummary,
                                               newTypeDefSummary,
                                               methodName);

        /*
         * Update relationship
         */
        reportUnsupportedOptionalFunction(methodName);
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
        final String methodName                = "reHomeRelationship";
        final String relationshipParameterName = "relationshipGUID";

        /*
         * Validate parameters
         */
        this.reHomeInstanceParameterValidation(userId,
                                               relationshipGUID,
                                               relationshipParameterName,
                                               typeDefGUID,
                                               typeDefName,
                                               homeMetadataCollectionId,
                                               newHomeMetadataCollectionId,
                                               methodName);

        /*
         * Locate relationship
         */
        reportUnsupportedOptionalFunction(methodName);
        return null;
    }



    /* ======================================================================
     * Group 6: Local house-keeping of reference metadata instances
     */


    /**
     * Validate the parameters passed to a reference instance method.
     *
     * @param userId unique identifier for requesting user.
     * @param typeDefGUID unique identifier of the type of the instance to delete.
     * @param typeDefName unique name of the type of the instance to delete.
     * @param instanceGUID String unique identifier (guid) for the instance.
     * @param instanceGUIDParameterName name of parameter for instance's GUID
     * @param homeMetadataCollectionId identifier of the metadata collection that is the home to this instance.
     * @param homeParameterName name of homeMetadataCollectionId parameter
     * @param methodName calling method
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    protected void  manageReferenceInstanceParameterValidation(String userId,
                                                               String typeDefGUID,
                                                               String typeDefName,
                                                               String instanceGUID,
                                                               String instanceGUIDParameterName,
                                                               String homeMetadataCollectionId,
                                                               String homeParameterName,
                                                               String methodName) throws InvalidParameterException,
                                                                                         RepositoryErrorException,
                                                                                         UserNotAuthorizedException
    {
        this.manageInstanceParameterValidation(userId, typeDefGUID, typeDefName, instanceGUID, instanceGUIDParameterName, methodName);

        repositoryValidator.validateHomeMetadataGUID(repositoryName, homeParameterName, homeMetadataCollectionId, methodName);
    }


    /**
     * Validate the header instance passed to delete() or purge() instance method.
     *
     * @param userId unique identifier for requesting user.
     * @param instanceHeader header for the instance.
     * @param instanceParameterName name of parameter for instance's GUID
     * @param methodName calling method
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    protected void referenceInstanceParameterValidation(String         userId,
                                                        InstanceHeader instanceHeader,
                                                        String         instanceParameterName,
                                                        String         methodName) throws InvalidParameterException,
                                                                                          RepositoryErrorException,
                                                                                          UserNotAuthorizedException
    {
        this.basicRequestValidation(userId, methodName);

        repositoryValidator.validateReferenceInstanceHeader(repositoryName,
                                                            metadataCollectionId,
                                                            instanceParameterName,
                                                            instanceHeader,
                                                            auditLog,
                                                            methodName);
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
        this.referenceInstanceParameterValidation(userId, entity, instanceParameterName, methodName);

        /*
         * Save entity
         */
        reportUnsupportedOptionalFunction(methodName);
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

        reportUnsupportedOptionalFunction(methodName);
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

        reportUnsupportedOptionalFunction(methodName);
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
        this.referenceInstanceParameterValidation(userId, entity, instanceParameterName, methodName);

        /*
         * Pass request to real repository
         */
        this.saveEntityReferenceCopy(userId, entity);
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
        super.purgeEntityReferenceCopy(userId, entity);
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
     * @throws EntityNotKnownException the entity identified by the guid is either a proxy or not found in the metadata collection.
     * @throws HomeEntityException the entity belongs to the local repository so creating a reference
     *                               copy would be invalid.
     * @throws FunctionNotSupportedException the repository does not support reference copies of instances.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public void purgeEntityReferenceCopy(String   userId,
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
        final String methodName                = "purgeEntityReferenceCopy";
        final String entityParameterName       = "entityGUID";
        final String homeParameterName         = "homeMetadataCollectionId";

        /*
         * Validate parameters
         */
        this.manageReferenceInstanceParameterValidation(userId,
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
        reportUnsupportedOptionalFunction(methodName);
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
    public void refreshEntityReferenceCopy(String   userId,
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
        final String entityParameterName       = "entityGUID";
        final String homeParameterName         = "homeMetadataCollectionId";

        /*
         * Validate parameters
         */
        this.manageReferenceInstanceParameterValidation(userId,
                                                        entityGUID,
                                                        typeDefGUID,
                                                        typeDefName,
                                                        entityParameterName,
                                                        homeMetadataCollectionId,
                                                        homeParameterName,
                                                        methodName);

        /*
         * Send refresh message
         */
        reportUnsupportedOptionalFunction(methodName);
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
        /*
         * This is a new method - if this method is not overridden in the implementing repository connector,
         * the logic below is executed.
         */
        super.saveClassificationReferenceCopy(userId, entity, classification);
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
                                                EntityProxy    entity,
                                                Classification classification) throws InvalidParameterException,
                                                                                      RepositoryErrorException,
                                                                                      TypeErrorException,
                                                                                      EntityConflictException,
                                                                                      InvalidEntityException,
                                                                                      PropertyErrorException,
                                                                                      UserNotAuthorizedException,
                                                                                      FunctionNotSupportedException
    {
        /*
         * This is a new method - if this method is not overridden in the implementing repository connector,
         * the logic below is executed.
         */
        super.saveClassificationReferenceCopy(userId, entity, classification);
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
        /*
         * This is a new method - if this method is not overridden in the implementing repository connector,
         * the logic below is executed.
         */
        super.purgeClassificationReferenceCopy(userId, entity, classification);
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
        this.referenceInstanceParameterValidation(userId, relationship, instanceParameterName, methodName);

        /*
         * Save relationship
         */
        reportUnsupportedOptionalFunction(methodName);
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
        this.referenceInstanceParameterValidation(userId, relationship, instanceParameterName, methodName);

        /*
         * Delete relationship
         */
        super.deleteRelationshipReferenceCopy(userId, relationship);
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
        super.purgeRelationshipReferenceCopy(userId, relationship);
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
     * @throws RelationshipNotKnownException the relationship is not know in the metadata collection.
     * @throws HomeRelationshipException the relationship belongs to the local repository so creating a reference
     *                                     copy would be invalid.
     * @throws FunctionNotSupportedException the repository does not support reference copies of instances.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public void purgeRelationshipReferenceCopy(String   userId,
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
        final String relationshipParameterName = "relationshipGUID";
        final String homeParameterName         = "homeMetadataCollectionId";

        /*
         * Validate parameters
         */
        this.manageReferenceInstanceParameterValidation(userId,
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
        reportUnsupportedOptionalFunction(methodName);
    }


    /**
     * The local server has requested that the repository that hosts the home metadata collection for the
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
    public void refreshRelationshipReferenceCopy(String   userId,
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
        final String relationshipParameterName = "relationshipGUID";
        final String homeParameterName         = "homeMetadataCollectionId";

        /*
         * Validate parameters
         */
        this.manageReferenceInstanceParameterValidation(userId,
                                                        relationshipGUID,
                                                        typeDefGUID,
                                                        typeDefName,
                                                        relationshipParameterName,
                                                        homeMetadataCollectionId,
                                                        homeParameterName,
                                                        methodName);

        /*
         * Process refresh request
         */
        reportUnsupportedOptionalFunction(methodName);
    }


    /**
     * Extract the typeDef for an instance
     *
     * @param header this is the header of the instance
     * @param methodName calling method
     * @return TypeDef
     * @throws RepositoryErrorException logic error - this call should not fail
     */
    protected TypeDef  getTypeDefForInstance(InstanceHeader   header,
                                             String           methodName) throws RepositoryErrorException
    {
        TypeDef  typeDef;
        try
        {
            final String typeGUIDParameterName = "getType.getTypeDefGUID";

            String instanceTypeGUID = header.getType().getTypeDefGUID();

            typeDef = repositoryHelper.getTypeDef(repositoryName, typeGUIDParameterName, instanceTypeGUID, methodName);
        }
        catch (TypeErrorException error)
        {
            throw new RepositoryErrorException(OMRSErrorCode.TYPEDEF_NOT_KNOWN.getMessageDefinition(methodName,
                                                                                                    this.getClass().getName(),
                                                                                                    repositoryName),
                                               this.getClass().getName(),
                                               methodName);
        }
        catch (Throwable  error)
        {
            throw new RepositoryErrorException(OMRSErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(this.getClass().getName(),
                                                                                                       methodName,
                                                                                                       repositoryName),
                                               this.getClass().getName(),
                                               methodName);
        }

        return typeDef;
    }

    /**
     * Return an exception to indicate that the method is unsupported but this is ok because it is optional.
     *
     * @param methodName calling method
     * @param asOfTime time warp value
     * @throws FunctionNotSupportedException optional function not supported
     * @throws RepositoryErrorException mandatory function not supported
     */
    protected void reportUnsupportedAsOfTimeFunction(String methodName,
                                                     Date   asOfTime) throws FunctionNotSupportedException,
                                                                             RepositoryErrorException
    {
        if (asOfTime == null)
        {
            reportUnsupportedMandatoryFunction(methodName);
        }
        else
        {
            reportUnsupportedOptionalFunction(methodName);
        }
    }


    /**
     * Return an exception to indicate that the method is unsupported.  This is a repository error because the function
     * is mandatory.
     *
     * @param methodName calling method
     * @throws RepositoryErrorException mandatory function not supported
     */
    private void reportUnsupportedMandatoryFunction(String methodName) throws RepositoryErrorException
    {
        throw new RepositoryErrorException(OMRSErrorCode.METHOD_NOT_IMPLEMENTED.getMessageDefinition(methodName,
                                                                                                     this.getClass().getName(),
                                                                                                     repositoryName),
                                           this.getClass().getName(),
                                           methodName);
    }


    /**
     * Throw exception to indicate that a type unique identifier is not recognized.
     *
     * @param guid guid of type
     * @param guidParameterName parameter that the guid was passed in on
     * @param methodName calling method
     * @throws TypeDefNotKnownException unknown type definition
     */
    protected void reportUnknownTypeGUID(String guid,
                                         String guidParameterName,
                                         String methodName) throws TypeDefNotKnownException
    {
        throw new TypeDefNotKnownException(OMRSErrorCode.TYPEDEF_ID_NOT_KNOWN.getMessageDefinition(guid,
                                                                                                   guidParameterName,
                                                                                                   methodName,
                                                                                                   repositoryName),
                                           this.getClass().getName(),
                                           methodName);
    }


    /**
     * Throw exception to indicate that a type unique identifier is not recognized.
     *
     * @param name name of type
     * @param methodName calling method
     * @throws TypeDefNotKnownException unknown type definition
     */
    protected void reportUnknownTypeName(String name,
                                         String methodName) throws TypeDefNotKnownException
    {
        throw new TypeDefNotKnownException(OMRSErrorCode.TYPEDEF_NAME_NOT_KNOWN.getMessageDefinition(name,
                                                                                                     methodName,
                                                                                                     repositoryName),
                                           this.getClass().getName(),
                                           methodName);
    }


    /**
     * Throw exception to indicate that an entity unique identifier is not recognized.
     *
     * @param entityGUID unknown unique identifier
     * @param methodName calling method
     *
     * @throws EntityNotKnownException unknown entity
     */
    protected void reportEntityNotKnown(String    entityGUID,
                                        String    methodName) throws EntityNotKnownException
    {
        throw new EntityNotKnownException(OMRSErrorCode.ENTITY_NOT_KNOWN.getMessageDefinition(entityGUID,
                                                                                              methodName,
                                                                                              repositoryName),
                                          this.getClass().getName(),
                                          methodName);
    }


    /**
     * Throw exception to indicate that a relationship unique identifier is not recognized.
     *
     * @param relationshipGUID unknown unique identifier
     * @param methodName calling method
     *
     * @throws RelationshipNotKnownException unknown relationship
     */
    protected void reportRelationshipNotKnown(String    relationshipGUID,
                                              String    methodName) throws RelationshipNotKnownException
    {
        throw new RelationshipNotKnownException(OMRSErrorCode.RELATIONSHIP_NOT_KNOWN.getMessageDefinition(relationshipGUID,
                                                                                                          methodName,
                                                                                                          repositoryName),
                                                this.getClass().getName(),
                                                methodName);
    }


    /**
     * Throw exception to indicate that a relationship unique identifier is not recognized.
     *
     * @param typeName type name not supported
     * @param methodName calling method
     *
     * @throws TypeDefNotSupportedException unsupported type
     */
    protected void reportTypeDefNotSupported(String    typeName,
                                             String    methodName) throws TypeDefNotSupportedException
    {
        throw new TypeDefNotSupportedException(OMRSErrorCode.TYPE_NOT_IMPLEMENTED.getMessageDefinition(repositoryName,
                                                                                                       typeName),
                                                this.getClass().getName(),
                                                methodName);
    }


    /**
     * Report that the type can not be created because it is already defined.
     *
     * @param typeGUID unique identifier of type
     * @param typeName unique name of type
     * @param methodName calling method
     * @throws TypeDefKnownException the type is already defined.
     */
    protected void reportTypeDefAlreadyDefined(String typeGUID,
                                               String typeName,
                                               String methodName) throws TypeDefKnownException
    {
        throw new TypeDefKnownException(OMRSErrorCode.TYPEDEF_ALREADY_DEFINED.getMessageDefinition(typeName,
                                                                                                   typeGUID,
                                                                                                   repositoryName),
                                        this.getClass().getName(),
                                        methodName);
    }


    /**
     * Report a type conflict error.
     *
     * @param typeGUID unique identifier of type
     * @param typeName unique name of type
     * @param methodName calling method
     * @throws TypeDefConflictException more than one definition for the same type name/guid
     */
    protected void reportTypeDefConflict(String typeGUID,
                                         String typeName,
                                         String methodName) throws TypeDefConflictException
    {
        throw new TypeDefConflictException(OMRSErrorCode.VERIFY_CONFLICT_DETECTED.getMessageDefinition(typeGUID,
                                                                                                       typeName,
                                                                                                       repositoryName),
                                           this.getClass().getName(),
                                           methodName);
    }


    /**
     * Report that a type can not be deleted.
     *
     * @param typeGUID unique identifier of type
     * @param typeName unique name of type
     * @param methodName calling method
     * @throws TypeDefInUseException type in use
     */
    protected void reportTypeDefInUse(String typeGUID,
                                      String typeName,
                                      String methodName) throws TypeDefInUseException
    {
        throw new TypeDefInUseException(OMRSErrorCode.TYPEDEF_IN_USE.getMessageDefinition(typeName,
                                                                                          typeGUID,
                                                                                          repositoryName),
                                        this.getClass().getName(),
                                        methodName);
    }


    /**
     * Throw an exception for the situation where there is only a proxy and the caller wants an entity details.
     *
     * @param guid unique identity of the entity
     * @param guidParameterName parameter name of the entity guid
     * @param methodName calling method
     * @throws EntityProxyOnlyException only a proxy
     */
    protected void reportEntityProxyOnly(String guid,
                                         String guidParameterName,
                                         String methodName) throws EntityProxyOnlyException
    {
        throw new EntityProxyOnlyException(OMRSErrorCode.ENTITY_PROXY_ONLY.getMessageDefinition(guid,
                                                                                                repositoryName,
                                                                                                guidParameterName,
                                                                                                methodName),
                                           this.getClass().getName(),
                                           methodName);
    }
}
