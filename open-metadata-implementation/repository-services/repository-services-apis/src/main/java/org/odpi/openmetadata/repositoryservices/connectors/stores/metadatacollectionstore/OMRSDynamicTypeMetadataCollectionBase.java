/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore;


import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryValidator;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * OMRSDynamicTypeMetadataCollectionBase provides a base class for an open metadata repository that
 * has a dynamic type system.  It begins with no types defined and builds up the knowledge of the types
 * as they are added through the API.
 * <br><br>
 * The types it is given fall into two groups.  Most come from open metadata archives and from other members of
 * the cohort, and are supplied again by their originators each time the server starts.  The others are homed in
 * this repository - their origin is this repository's metadata collection id - because they were defined through
 * the API.  Those are validated against the types already known, since nothing else will have checked them, and
 * are kept in the repository's type store (see getDynamicTypeStore()) so they can be restored when the server
 * restarts.
 */
public class OMRSDynamicTypeMetadataCollectionBase extends OMRSMetadataCollectionBase
{
    /**
     * Constructor ensures the metadata collection is linked to its connector and knows its metadata collection id.
     *
     * @param parentConnector connector that this metadata collection supports.  The connector has the information
     *                        to call the metadata repository.
     * @param repositoryName name of this repository.
     * @param repositoryHelper helper class for building types and instances
     * @param repositoryValidator validator class for checking open metadata repository objects and parameters.
     * @param metadataCollectionId unique identifier of the metadata collection id.
     */
    public OMRSDynamicTypeMetadataCollectionBase(OMRSRepositoryConnector parentConnector,
                                                 String                  repositoryName,
                                                 OMRSRepositoryHelper    repositoryHelper,
                                                 OMRSRepositoryValidator repositoryValidator,
                                                 String                  metadataCollectionId)
    {
        super(parentConnector,
              repositoryName,
              repositoryHelper,
              repositoryValidator,
              metadataCollectionId);
    }


    /**
     * Return the store that keeps the types homed in this repository and knows which types have instances.
     * A repository that returns null (the default) keeps no types of its own - the types added through the API
     * are lost when the server stops - and cannot delete a type definition because it cannot show that the type
     * is unused.
     *
     * @return type store or null
     */
    protected OMRSDynamicTypeStore getDynamicTypeStore()
    {
        return null;
    }


    /**
     * Return the type definitions that this repository originated and has stored itself.
     *
     * @param userId unique identifier for requesting server
     * @return gallery of stored types, or null if there are none
     * @throws RepositoryErrorException there is a problem reading the stored types
     */
    @Override
    public TypeDefGallery getStoredTypes(String userId) throws RepositoryErrorException
    {
        OMRSDynamicTypeStore typeStore = this.getDynamicTypeStore();

        if (typeStore != null)
        {
            return typeStore.getStoredTypes();
        }

        return null;
    }


    /**
     * Create a definition of a new TypeDef.
     *
     * @param userId unique identifier for requesting user.
     * @param newTypeDef TypeDef structure describing the new TypeDef.
     * @throws InvalidParameterException the new TypeDef is null.
     * @throws RepositoryErrorException a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws TypeDefKnownException the TypeDef is already stored in the repository.
     * @throws TypeDefConflictException the new TypeDef conflicts with an existing TypeDef.
     * @throws InvalidTypeDefException the new TypeDef has invalid contents.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public void addTypeDef(String  userId,
                           TypeDef newTypeDef) throws InvalidParameterException,
                                                      RepositoryErrorException,
                                                      TypeDefKnownException,
                                                      TypeDefConflictException,
                                                      InvalidTypeDefException,
                                                      UserNotAuthorizedException
    {
        final String  methodName = "addTypeDef";
        final String  typeDefParameterName = "newTypeDef";

        /*
         * Verify the incoming parameters
         */
        super.newTypeDefParameterValidation(userId, newTypeDef, typeDefParameterName, methodName);


        /*
         * Check this is not a duplicate
         */
        TypeDef existingTypeDef = repositoryHelper.getTypeDefByName(repositoryName, newTypeDef.getName());

        if (existingTypeDef != null)
        {
            reportTypeDefAlreadyDefined(newTypeDef.getGUID(),
                                        newTypeDef.getName(),
                                        methodName);
        }

        /*
         * A type homed here was defined through the API, so it is checked against the rest of the type system
         * and stored.
         */
        if (this.isHomedType(newTypeDef.getOrigin()))
        {
            this.validateDynamicTypeDef(newTypeDef, methodName);

            OMRSDynamicTypeStore typeStore = this.getDynamicTypeStore();

            if (typeStore != null)
            {
                typeStore.saveTypeDef(newTypeDef);
            }
        }
    }


    /**
     * Create a definition of a new AttributeTypeDef.
     *
     * @param userId unique identifier for requesting user.
     * @param newAttributeTypeDef TypeDef structure describing the new TypeDef.
     * @throws InvalidParameterException the new TypeDef is null.
     * @throws RepositoryErrorException a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws TypeDefKnownException the TypeDef is already stored in the repository.
     * @throws TypeDefConflictException the new TypeDef conflicts with an existing TypeDef.
     * @throws InvalidTypeDefException the new TypeDef has invalid contents.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public  void addAttributeTypeDef(String             userId,
                                     AttributeTypeDef newAttributeTypeDef) throws InvalidParameterException,
                                                                                  RepositoryErrorException,
                                                                                  TypeDefKnownException,
                                                                                  TypeDefConflictException,
                                                                                  InvalidTypeDefException,
                                                                                  UserNotAuthorizedException
    {
        final String  methodName           = "addAttributeTypeDef";
        final String  typeDefParameterName = "newAttributeTypeDef";

        /*
         * Validate parameters
         */
        super.newAttributeTypeDefParameterValidation(userId, newAttributeTypeDef, typeDefParameterName, methodName);

        /*
         * Check this is not a duplicate
         */
        AttributeTypeDef existingAttributeTypeDef = repositoryHelper.getAttributeTypeDefByName(repositoryName,
                                                                                               newAttributeTypeDef.getName());

        if (existingAttributeTypeDef != null)
        {
            reportTypeDefAlreadyDefined(newAttributeTypeDef.getGUID(),
                                        newAttributeTypeDef.getName(),
                                        methodName);
        }

        /*
         * An attribute type homed here is an enum defined through the API, so it is checked and stored.
         */
        if (this.isHomedType(newAttributeTypeDef.getOrigin()))
        {
            this.validateDynamicEnumDef(newAttributeTypeDef, methodName);

            OMRSDynamicTypeStore typeStore = this.getDynamicTypeStore();

            if (typeStore != null)
            {
                typeStore.saveAttributeTypeDef(newAttributeTypeDef);
            }
        }
    }


    /**
     * Verify that a definition of a TypeDef is either new - or matches the definition already stored.
     *
     * @param userId unique identifier for requesting user.
     * @param typeDef TypeDef structure describing the TypeDef to test.
     * @return boolean - true means the TypeDef matches the local definition - false means the TypeDef is not known.
     * @throws InvalidParameterException the TypeDef is null.
     * @throws RepositoryErrorException a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws TypeDefConflictException the new TypeDef conflicts with an existing TypeDef.
     * @throws InvalidTypeDefException the new TypeDef has invalid contents.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public boolean verifyTypeDef(String       userId,
                                 TypeDef      typeDef) throws InvalidParameterException,
                                                              RepositoryErrorException,
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
        TypeDef  existingTypeDef = repositoryHelper.getTypeDefByName(repositoryName, typeDef.getName());

        if (existingTypeDef == null)
        {
            return false;
        }
        else if ((existingTypeDef.getName().equals(typeDef.getName())) &&
                 (existingTypeDef.getGUID().equals(typeDef.getGUID())) &&
                 (existingTypeDef.getVersionName().equals(typeDef.getVersionName())))
        {
            return true;
        }
        else
        {
            super.reportTypeDefConflict(typeDef.getGUID(), typeDef.getName(), methodName);
            return false;
        }
    }


    /**
     * Verify that a definition of an AttributeTypeDef is either new - or matches the definition already stored.
     *
     * @param userId unique identifier for requesting user.
     * @param attributeTypeDef TypeDef structure describing the TypeDef to test.
     * @return boolean - true means the TypeDef matches the local definition - false means the TypeDef is not known.
     * @throws InvalidParameterException the TypeDef is null.
     * @throws RepositoryErrorException a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws TypeDefConflictException the new TypeDef conflicts with an existing TypeDef.
     * @throws InvalidTypeDefException the new TypeDef has invalid contents.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public  boolean verifyAttributeTypeDef(String            userId,
                                           AttributeTypeDef  attributeTypeDef) throws InvalidParameterException,
                                                                                      RepositoryErrorException,
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
        AttributeTypeDef  existingAttributeTypeDef = repositoryHelper.getAttributeTypeDefByName(repositoryName, attributeTypeDef.getName());

        if (existingAttributeTypeDef == null)
        {
            return false;
        }
        else if (attributeTypeDef.equals(existingAttributeTypeDef))
        {
            return true;
        }
        else
        {
            super.reportTypeDefConflict(attributeTypeDef.getGUID(), attributeTypeDef.getName(), methodName);
            return false;
        }
    }


    /**
     * Update one or more properties of the TypeDef.  The TypeDefPatch controls what types of updates
     * are safe to make to the TypeDef.
     *
     * @param userId unique identifier for requesting user.
     * @param typeDefPatch TypeDef patch describing change to TypeDef.
     * @return updated TypeDef
     * @throws InvalidParameterException the TypeDefPatch is null.
     * @throws RepositoryErrorException a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws TypeDefNotKnownException the requested TypeDef is not found in the metadata collection.
     * @throws PatchErrorException the TypeDef can not be updated because the supplied patch is incompatible
     *                               with the stored TypeDef.
     */
    @Override
    public TypeDef updateTypeDef(String       userId,
                                 TypeDefPatch typeDefPatch) throws InvalidParameterException,
                                                                   RepositoryErrorException,
                                                                   TypeDefNotKnownException,
                                                                   PatchErrorException
    {
        final String  methodName           = "updateTypeDef";

        /*
         * Validate parameters - this method returns the existing TypeDef if needed.
         */
        TypeDef typeDef = super.updateTypeDefParameterValidation(userId, typeDefPatch, methodName);

        /*
         * Perform operation - this function will validate the patch (throwing exceptions if errors found
         * and apply it to the existing typedef, returning the updated one.  If the real repository implementation
         * needs to work with the patched TypeDef, it can use the result from applyPatch().
         */
        TypeDef updatedTypeDef = repositoryHelper.applyPatch(repositoryName, typeDef, typeDefPatch);

        /*
         * A type homed here is checked again as a whole, since the patch may have changed what it depends on,
         * and the stored copy is replaced.
         */
        if ((updatedTypeDef != null) && (this.isHomedType(updatedTypeDef.getOrigin())))
        {
            this.validateDynamicTypeDef(updatedTypeDef, methodName);

            OMRSDynamicTypeStore typeStore = this.getDynamicTypeStore();

            if (typeStore != null)
            {
                typeStore.saveTypeDef(updatedTypeDef);
            }
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
     * @throws RepositoryErrorException a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws TypeDefNotKnownException the requested TypeDef is not found in the metadata collection.
     * @throws TypeDefInUseException the TypeDef can not be deleted because there are instances of this type in the
     *                                 the metadata collection.  These instances need to be purged before the
     *                                 TypeDef can be deleted.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public void deleteTypeDef(String    userId,
                              String    obsoleteTypeDefGUID,
                              String    obsoleteTypeDefName) throws InvalidParameterException,
                                                                    RepositoryErrorException,
                                                                    TypeDefNotKnownException,
                                                                    TypeDefInUseException,
                                                                    UserNotAuthorizedException
    {
        final String    methodName        = "deleteTypeDef";
        final String    guidParameterName = "obsoleteTypeDefGUID";
        final String    nameParameterName = "obsoleteTypeDefName";

        /*
         * Validate parameters
         */
        super.manageTypeDefParameterValidation(userId,
                                               guidParameterName,
                                               nameParameterName,
                                               obsoleteTypeDefGUID,
                                               obsoleteTypeDefName,
                                               methodName);

        /*
         * Perform operation
         */
        TypeDef  typeDef = repositoryHelper.getTypeDefByName(repositoryName, obsoleteTypeDefName);

        if ((typeDef == null) || (! obsoleteTypeDefGUID.equals(typeDef.getGUID())))
        {
            super.reportUnknownTypeGUID(obsoleteTypeDefGUID, guidParameterName, methodName);
            return;
        }

        /*
         * Another type that depends on this one would be left incomplete.  This includes the subtypes, which
         * means the instance check below does not need to look for instances of subtypes.
         */
        this.validateTypeDefNotReferenced(typeDef, methodName);

        /*
         * Without a type store the repository cannot show that there are no instances of the type.
         */
        OMRSDynamicTypeStore typeStore = this.getDynamicTypeStore();

        if ((typeStore == null) || (typeStore.isTypeDefInstantiated(obsoleteTypeDefGUID, obsoleteTypeDefName)))
        {
            super.reportTypeDefInUse(obsoleteTypeDefGUID, obsoleteTypeDefName, methodName);
        }
        else
        {
            typeStore.removeTypeDef(obsoleteTypeDefGUID, obsoleteTypeDefName);
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
     * @throws RepositoryErrorException a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws TypeDefNotKnownException the requested AttributeTypeDef is not found in the metadata collection.
     * @throws TypeDefInUseException the AttributeTypeDef can not be deleted because there are instances of this type in the
     *                                 the metadata collection.  These instances need to be purged before the
     *                                 AttributeTypeDef can be deleted.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public void deleteAttributeTypeDef(String    userId,
                                       String    obsoleteTypeDefGUID,
                                       String    obsoleteTypeDefName) throws InvalidParameterException,
                                                                             RepositoryErrorException,
                                                                             TypeDefNotKnownException,
                                                                             TypeDefInUseException,
                                                                             UserNotAuthorizedException
    {
        final String    methodName        = "deleteAttributeTypeDef";
        final String    guidParameterName = "obsoleteTypeDefGUID";
        final String    nameParameterName = "obsoleteTypeDefName";

        /*
         * Validate parameters
         */
        super.manageAttributeTypeDefParameterValidation(userId,
                                                        guidParameterName,
                                                        nameParameterName,
                                                        obsoleteTypeDefGUID,
                                                        obsoleteTypeDefName,
                                                        methodName);

        /*
         * Perform operation
         */
        AttributeTypeDef  attributeTypeDef = repositoryHelper.getAttributeTypeDefByName(repositoryName, obsoleteTypeDefName);

        if ((attributeTypeDef == null) || (! obsoleteTypeDefGUID.equals(attributeTypeDef.getGUID())))
        {
            super.reportUnknownTypeGUID(obsoleteTypeDefGUID, guidParameterName, methodName);
            return;
        }

        /*
         * The primitive and collection types are part of the fabric of the type system and stay, whether any
         * type uses them or not.
         */
        if (attributeTypeDef.getCategory() != AttributeTypeDefCategory.ENUM_DEF)
        {
            super.reportTypeDefInUse(obsoleteTypeDefGUID, obsoleteTypeDefName, methodName);
        }

        /*
         * An enum is in use if any type definition has an attribute of that type.  If none does then no
         * instance can have a property of that type either - an attribute cannot be removed from a type
         * definition, and a type definition can only be deleted once it has no instances - so this check is
         * enough without looking at the instances.
         */
        this.validateAttributeTypeDefNotUsed(attributeTypeDef, methodName);

        OMRSDynamicTypeStore typeStore = this.getDynamicTypeStore();

        if (typeStore != null)
        {
            typeStore.removeAttributeTypeDef(obsoleteTypeDefGUID, obsoleteTypeDefName);
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
     * @return typeDef - new values for this TypeDef, including the new guid/name.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws TypeDefNotKnownException the TypeDef identified by the original guid/name is not found
     *                                    in the metadata collection.
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
        super.manageTypeDefParameterValidation(userId,
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
         * Perform operation
         */
        TypeDef  existingTypeDef = repositoryHelper.getTypeDefByName(repositoryName, originalTypeDefName);

        if (existingTypeDef == null)
        {
            super.reportUnknownTypeGUID(originalTypeDefGUID, originalGUIDParameterName, methodName);
        }
        else
        {
            existingTypeDef.setGUID(newTypeDefGUID);
            existingTypeDef.setName(newTypeDefName);

            existingTypeDef.setVersion(existingTypeDef.getVersion() + 1);
        }

        return existingTypeDef;
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
     * @return attributeTypeDef - new values for this AttributeTypeDef, including the new guid/name.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws RepositoryErrorException a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws TypeDefNotKnownException the AttributeTypeDef identified by the original guid/name is not
     *                                    found in the metadata collection.
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
        super.manageAttributeTypeDefParameterValidation(userId,
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
         * Perform operation
         */
        AttributeTypeDef  existingAttributeTypeDef = repositoryHelper.getAttributeTypeDefByName(repositoryName, originalAttributeTypeDefName);

        if (existingAttributeTypeDef == null)
        {
            super.reportUnknownTypeGUID(originalAttributeTypeDefGUID, originalGUIDParameterName, methodName);
        }
        else
        {
            existingAttributeTypeDef.setGUID(newAttributeTypeDefGUID);
            existingAttributeTypeDef.setName(newAttributeTypeDefName);

            existingAttributeTypeDef.setVersion(existingAttributeTypeDef.getVersion() + 1);
        }

        return existingAttributeTypeDef;
    }


    /**
     * Retrieve any locally homed classifications assigned to the requested entity.  This method is implemented by repository connectors that are able
     * to store classifications for entities that are homed in another repository.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID unique identifier of the entity with classifications to retrieve
     * @return list of all the classifications for this entity that are homed in this repository
     * @throws InvalidParameterException the entity is null.
     * @throws RepositoryErrorException a problem communicating with the metadata repository where
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
        try
        {
            EntityDetail entityDetail = this.getEntityDetail(userId, entityGUID);

            return repositoryHelper.getHomeClassificationsFromEntity(repositoryName, entityDetail, metadataCollectionId, methodName);
        }
        catch (EntityProxyOnlyException  error)
        {
            return null;
        }
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
     * @throws RepositoryErrorException a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws EntityNotKnownException the entity is not recognized by this repository
     * @throws UserNotAuthorizedException to calling user is not authorized to retrieve this metadata
     * @throws FunctionNotSupportedException this method is not supported
     */
    @Override
    public List<Classification> getHomeClassifications(String userId,
                                                       String entityGUID,
                                                       Date asOfTime) throws InvalidParameterException,
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
        try
        {
            EntityDetail entityDetail = this.getEntityDetail(userId, entityGUID, asOfTime);

            return repositoryHelper.getHomeClassificationsFromEntity(repositoryName, entityDetail, metadataCollectionId, methodName);
        }
        catch (EntityProxyOnlyException  error)
        {
            return null;
        }
    }


    /* =================================================
     * Validation of the types homed in this repository
     */


    /**
     * Return whether a type originated in this repository - in which case it was defined through the API and
     * this repository is responsible for checking and storing it.
     *
     * @param origin origin of the type
     * @return boolean
     */
    private boolean isHomedType(String origin)
    {
        return (origin != null) && (origin.equals(super.metadataCollectionId));
    }


    /**
     * Check that a type definition defined through the API is consistent with the types already known.  The
     * archives and cohort members that supply the other types are trusted to have done this for themselves.
     *
     * @param typeDef type definition to check
     * @param methodName calling method
     * @throws InvalidTypeDefException the type definition is not consistent with the known types
     */
    private void validateDynamicTypeDef(TypeDef typeDef,
                                        String  methodName) throws InvalidTypeDefException
    {
        TypeDefCategory category = typeDef.getCategory();

        boolean isCorrectClass = switch (category)
        {
            case ENTITY_DEF         -> typeDef instanceof EntityDef;
            case RELATIONSHIP_DEF   -> typeDef instanceof RelationshipDef;
            case CLASSIFICATION_DEF -> typeDef instanceof ClassificationDef;
            default                 -> false;
        };

        if (! isCorrectClass)
        {
            this.throwInvalidDynamicTypeDef(typeDef.getName(),
                                            typeDef.getGUID(),
                                            methodName,
                                            "its category " + category + " does not match its structure " + typeDef.getClass().getSimpleName());
        }

        /*
         * The supertype must be of the same category, and must not lead back to this type.
         */
        Set<String> inheritedAttributeNames = new HashSet<>();

        if (typeDef.getSuperType() != null)
        {
            TypeDef superTypeDef = this.getReferencedTypeDef(typeDef, typeDef.getSuperType(), "supertype", methodName);

            if (superTypeDef.getCategory() != category)
            {
                this.throwInvalidDynamicTypeDef(typeDef.getName(),
                                                typeDef.getGUID(),
                                                methodName,
                                                "its supertype " + superTypeDef.getName() + " is a " + superTypeDef.getCategory() +
                                                        " rather than a " + category);
            }

            Set<String> ancestorNames = new HashSet<>();
            TypeDef     ancestor      = superTypeDef;

            while ((ancestor != null) && (ancestorNames.add(ancestor.getName())))
            {
                if (typeDef.getName().equals(ancestor.getName()))
                {
                    this.throwInvalidDynamicTypeDef(typeDef.getName(),
                                                    typeDef.getGUID(),
                                                    methodName,
                                                    "it would become its own supertype through " + superTypeDef.getName());
                }

                TypeDefLink ancestorSuperType = ancestor.getSuperType();

                if ((ancestorSuperType == null) || (ancestorSuperType.getName() == null))
                {
                    ancestor = null;
                }
                else
                {
                    ancestor = repositoryHelper.getTypeDefByName(repositoryName, ancestorSuperType.getName());
                }
            }

            List<TypeDefAttribute> inheritedAttributes = repositoryHelper.getAllPropertiesForTypeDef(repositoryName, superTypeDef, methodName);

            if (inheritedAttributes != null)
            {
                for (TypeDefAttribute inheritedAttribute : inheritedAttributes)
                {
                    if (inheritedAttribute != null)
                    {
                        inheritedAttributeNames.add(inheritedAttribute.getAttributeName());
                    }
                }
            }
        }

        /*
         * Each attribute must have a unique name that is not inherited, and a type that is already known.
         */
        if (typeDef.getPropertiesDefinition() != null)
        {
            Set<String> attributeNames = new HashSet<>();

            for (TypeDefAttribute attribute : typeDef.getPropertiesDefinition())
            {
                if ((attribute == null) || (attribute.getAttributeName() == null) || (attribute.getAttributeName().isBlank()))
                {
                    this.throwInvalidDynamicTypeDef(typeDef.getName(), typeDef.getGUID(), methodName, "it has an attribute with no name");
                    return;
                }

                String attributeName = attribute.getAttributeName();

                if (! attributeNames.add(attributeName))
                {
                    this.throwInvalidDynamicTypeDef(typeDef.getName(),
                                                    typeDef.getGUID(),
                                                    methodName,
                                                    "attribute " + attributeName + " is defined more than once");
                }

                if (inheritedAttributeNames.contains(attributeName))
                {
                    this.throwInvalidDynamicTypeDef(typeDef.getName(),
                                                    typeDef.getGUID(),
                                                    methodName,
                                                    "attribute " + attributeName + " is already defined by one of its supertypes");
                }

                AttributeTypeDef attributeType = attribute.getAttributeType();

                if ((attributeType == null) || (attributeType.getName() == null))
                {
                    this.throwInvalidDynamicTypeDef(typeDef.getName(),
                                                    typeDef.getGUID(),
                                                    methodName,
                                                    "attribute " + attributeName + " has no type");
                    return;
                }

                AttributeTypeDef knownAttributeType = repositoryHelper.getAttributeTypeDefByName(repositoryName, attributeType.getName());

                if (knownAttributeType == null)
                {
                    this.throwInvalidDynamicTypeDef(typeDef.getName(),
                                                    typeDef.getGUID(),
                                                    methodName,
                                                    "attribute " + attributeName + " is of type " + attributeType.getName() +
                                                            ", which is not a known attribute type");
                }
                else if ((attributeType.getGUID() != null) && (! attributeType.getGUID().equals(knownAttributeType.getGUID())))
                {
                    this.throwInvalidDynamicTypeDef(typeDef.getName(),
                                                    typeDef.getGUID(),
                                                    methodName,
                                                    "attribute " + attributeName + " is of type " + attributeType.getName() +
                                                            " with guid " + attributeType.getGUID() + " but the known type of that name has guid " +
                                                            knownAttributeType.getGUID());
                }
            }
        }

        /*
         * The category-specific links must all be to known entity types.
         */
        if (typeDef instanceof RelationshipDef relationshipDef)
        {
            this.validateRelationshipEnd(relationshipDef, relationshipDef.getEndDef1(), "end 1", methodName);
            this.validateRelationshipEnd(relationshipDef, relationshipDef.getEndDef2(), "end 2", methodName);
        }
        else if ((typeDef instanceof ClassificationDef classificationDef) && (classificationDef.getValidEntityDefs() != null))
        {
            for (TypeDefLink validEntityDef : classificationDef.getValidEntityDefs())
            {
                if (validEntityDef != null)
                {
                    this.validateEntityTypeLink(typeDef, validEntityDef, "valid entity type", methodName);
                }
            }
        }

        if ((typeDef.getInitialStatus() != null) &&
            (typeDef.getValidInstanceStatusList() != null) &&
            (! typeDef.getValidInstanceStatusList().contains(typeDef.getInitialStatus())))
        {
            this.throwInvalidDynamicTypeDef(typeDef.getName(),
                                            typeDef.getGUID(),
                                            methodName,
                                            "its initial status " + typeDef.getInitialStatus() + " is not one of its valid statuses " +
                                                    typeDef.getValidInstanceStatusList());
        }
    }


    /**
     * Check one end of a relationship type defined through the API.
     *
     * @param relationshipDef relationship type
     * @param endDef end to check
     * @param endName name of the end for the message
     * @param methodName calling method
     * @throws InvalidTypeDefException the end is not valid
     */
    private void validateRelationshipEnd(RelationshipDef    relationshipDef,
                                         RelationshipEndDef endDef,
                                         String             endName,
                                         String             methodName) throws InvalidTypeDefException
    {
        if ((endDef == null) || (endDef.getEntityType() == null))
        {
            this.throwInvalidDynamicTypeDef(relationshipDef.getName(),
                                            relationshipDef.getGUID(),
                                            methodName,
                                            "it has no entity type for " + endName);
            return;
        }

        if ((endDef.getAttributeName() == null) || (endDef.getAttributeName().isBlank()))
        {
            this.throwInvalidDynamicTypeDef(relationshipDef.getName(),
                                            relationshipDef.getGUID(),
                                            methodName,
                                            "it has no attribute name for " + endName);
        }

        this.validateEntityTypeLink(relationshipDef, endDef.getEntityType(), endName + " entity type", methodName);
    }


    /**
     * Check that a link from a type defined through the API is to a known entity type.
     *
     * @param typeDef type holding the link
     * @param entityTypeLink link to check
     * @param role what the link is for, for the message
     * @param methodName calling method
     * @throws InvalidTypeDefException the link is not to a known entity type
     */
    private void validateEntityTypeLink(TypeDef     typeDef,
                                        TypeDefLink entityTypeLink,
                                        String      role,
                                        String      methodName) throws InvalidTypeDefException
    {
        TypeDef entityDef = this.getReferencedTypeDef(typeDef, entityTypeLink, role, methodName);

        if (entityDef.getCategory() != TypeDefCategory.ENTITY_DEF)
        {
            this.throwInvalidDynamicTypeDef(typeDef.getName(),
                                            typeDef.getGUID(),
                                            methodName,
                                            "its " + role + " " + entityDef.getName() + " is a " + entityDef.getCategory() +
                                                    " rather than an entity type");
        }
    }


    /**
     * Return the known type that a link from a type defined through the API refers to.  The link is matched by
     * name, and its guid, if supplied, must agree.
     *
     * @param typeDef type holding the link
     * @param typeDefLink link to resolve
     * @param role what the link is for, for the message
     * @param methodName calling method
     * @return known type
     * @throws InvalidTypeDefException the link does not refer to a known type
     */
    private TypeDef getReferencedTypeDef(TypeDef     typeDef,
                                         TypeDefLink typeDefLink,
                                         String      role,
                                         String      methodName) throws InvalidTypeDefException
    {
        TypeDef referencedTypeDef = null;

        if (typeDefLink.getName() != null)
        {
            referencedTypeDef = repositoryHelper.getTypeDefByName(repositoryName, typeDefLink.getName());
        }

        if (referencedTypeDef == null)
        {
            this.throwInvalidDynamicTypeDef(typeDef.getName(),
                                            typeDef.getGUID(),
                                            methodName,
                                            "its " + role + " " + typeDefLink.getName() + " is not a known type");
        }
        else if ((typeDefLink.getGUID() != null) && (! typeDefLink.getGUID().equals(referencedTypeDef.getGUID())))
        {
            this.throwInvalidDynamicTypeDef(typeDef.getName(),
                                            typeDef.getGUID(),
                                            methodName,
                                            "its " + role + " " + typeDefLink.getName() + " has guid " + typeDefLink.getGUID() +
                                                    " but the known type of that name has guid " + referencedTypeDef.getGUID());
        }

        return referencedTypeDef;
    }


    /**
     * Check that an attribute type defined through the API is a well-formed enum.  Primitive and collection
     * types are fixed by the type system.
     *
     * @param attributeTypeDef attribute type to check
     * @param methodName calling method
     * @throws InvalidTypeDefException the attribute type is not a well-formed enum
     */
    private void validateDynamicEnumDef(AttributeTypeDef attributeTypeDef,
                                        String           methodName) throws InvalidTypeDefException
    {
        if ((attributeTypeDef.getCategory() != AttributeTypeDefCategory.ENUM_DEF) || (! (attributeTypeDef instanceof EnumDef enumDef)))
        {
            throw new InvalidTypeDefException(OMRSErrorCode.UNSUPPORTED_DYNAMIC_ATTRIBUTE_TYPEDEF.getMessageDefinition(attributeTypeDef.getName(),
                                                                                                                      attributeTypeDef.getGUID(),
                                                                                                                      methodName,
                                                                                                                      String.valueOf(attributeTypeDef.getCategory())),
                                              this.getClass().getName(),
                                              methodName);
        }

        List<EnumElementDef> elementDefs = enumDef.getElementDefs();

        if (elementDefs == null)
        {
            this.throwInvalidDynamicTypeDef(enumDef.getName(), enumDef.getGUID(), methodName, "it has no valid values");
            return;
        }

        Set<Integer> ordinals = new HashSet<>();
        Set<String>  values   = new HashSet<>();

        for (EnumElementDef elementDef : elementDefs)
        {
            if ((elementDef == null) || (elementDef.getValue() == null) || (elementDef.getValue().isBlank()))
            {
                this.throwInvalidDynamicTypeDef(enumDef.getName(), enumDef.getGUID(), methodName, "it has a valid value with no name");
                return;
            }

            if (! ordinals.add(elementDef.getOrdinal()))
            {
                this.throwInvalidDynamicTypeDef(enumDef.getName(),
                                                enumDef.getGUID(),
                                                methodName,
                                                "ordinal " + elementDef.getOrdinal() + " is used by more than one valid value");
            }

            if (! values.add(elementDef.getValue()))
            {
                this.throwInvalidDynamicTypeDef(enumDef.getName(),
                                                enumDef.getGUID(),
                                                methodName,
                                                "valid value " + elementDef.getValue() + " is defined more than once");
            }
        }

        EnumElementDef defaultValue = enumDef.getDefaultValue();

        if ((defaultValue != null) && (defaultValue.getValue() != null))
        {
            boolean isDefaultListed = false;

            for (EnumElementDef elementDef : elementDefs)
            {
                if ((elementDef.getOrdinal() == defaultValue.getOrdinal()) && (elementDef.getValue().equals(defaultValue.getValue())))
                {
                    isDefaultListed = true;
                    break;
                }
            }

            if (! isDefaultListed)
            {
                this.throwInvalidDynamicTypeDef(enumDef.getName(),
                                                enumDef.getGUID(),
                                                methodName,
                                                "its default value " + defaultValue.getValue() + " (ordinal " + defaultValue.getOrdinal() +
                                                        ") is not one of its valid values");
            }
        }
    }


    /**
     * Throw an exception for a type definition defined through the API that is inconsistent with the known types.
     *
     * @param typeName name of the type
     * @param typeGUID unique identifier of the type
     * @param methodName calling method
     * @param reason what is wrong with it
     * @throws InvalidTypeDefException the resulting exception
     */
    private void throwInvalidDynamicTypeDef(String typeName,
                                            String typeGUID,
                                            String methodName,
                                            String reason) throws InvalidTypeDefException
    {
        throw new InvalidTypeDefException(OMRSErrorCode.INVALID_DYNAMIC_TYPEDEF.getMessageDefinition(typeName,
                                                                                                    typeGUID,
                                                                                                    methodName,
                                                                                                    repositoryName,
                                                                                                    reason),
                                          this.getClass().getName(),
                                          methodName);
    }


    /**
     * Check that no other type definition depends on a type definition that is to be deleted.
     *
     * @param typeDef type definition to be deleted
     * @param methodName calling method
     * @throws TypeDefInUseException another type definition refers to it
     */
    private void validateTypeDefNotReferenced(TypeDef typeDef,
                                              String  methodName) throws TypeDefInUseException
    {
        List<TypeDef> knownTypeDefs = repositoryHelper.getKnownTypeDefs();

        if (knownTypeDefs != null)
        {
            String typeName = typeDef.getName();

            for (TypeDef knownTypeDef : knownTypeDefs)
            {
                if ((knownTypeDef == null) || (typeName.equals(knownTypeDef.getName())))
                {
                    continue;
                }

                if ((knownTypeDef.getSuperType() != null) && (typeName.equals(knownTypeDef.getSuperType().getName())))
                {
                    this.throwTypeDefReferenced(typeDef, knownTypeDef, "supertype", methodName);
                }

                if (knownTypeDef instanceof RelationshipDef relationshipDef)
                {
                    if (this.isRelationshipEndType(relationshipDef.getEndDef1(), typeName))
                    {
                        this.throwTypeDefReferenced(typeDef, knownTypeDef, "end 1 entity type", methodName);
                    }

                    if (this.isRelationshipEndType(relationshipDef.getEndDef2(), typeName))
                    {
                        this.throwTypeDefReferenced(typeDef, knownTypeDef, "end 2 entity type", methodName);
                    }
                }
                else if ((knownTypeDef instanceof ClassificationDef classificationDef) && (classificationDef.getValidEntityDefs() != null))
                {
                    for (TypeDefLink validEntityDef : classificationDef.getValidEntityDefs())
                    {
                        if ((validEntityDef != null) && (typeName.equals(validEntityDef.getName())))
                        {
                            this.throwTypeDefReferenced(typeDef, knownTypeDef, "valid entity type", methodName);
                        }
                    }
                }
            }
        }
    }


    /**
     * Return whether a relationship end is for the named entity type.
     *
     * @param endDef relationship end
     * @param typeName entity type name
     * @return boolean
     */
    private boolean isRelationshipEndType(RelationshipEndDef endDef,
                                          String             typeName)
    {
        return (endDef != null) && (endDef.getEntityType() != null) && (typeName.equals(endDef.getEntityType().getName()));
    }


    /**
     * Throw an exception for a type definition that cannot be deleted because another type definition refers to it.
     *
     * @param typeDef type definition to be deleted
     * @param referencingTypeDef type definition that refers to it
     * @param role what the reference is for
     * @param methodName calling method
     * @throws TypeDefInUseException the resulting exception
     */
    private void throwTypeDefReferenced(TypeDef typeDef,
                                        TypeDef referencingTypeDef,
                                        String  role,
                                        String  methodName) throws TypeDefInUseException
    {
        throw new TypeDefInUseException(OMRSErrorCode.TYPEDEF_REFERENCED.getMessageDefinition(typeDef.getName(),
                                                                                              typeDef.getGUID(),
                                                                                              repositoryName,
                                                                                              referencingTypeDef.getName(),
                                                                                              role),
                                        this.getClass().getName(),
                                        methodName);
    }


    /**
     * Check that no type definition has an attribute of an attribute type that is to be deleted.
     *
     * @param attributeTypeDef attribute type to be deleted
     * @param methodName calling method
     * @throws TypeDefInUseException a type definition uses it
     */
    private void validateAttributeTypeDefNotUsed(AttributeTypeDef attributeTypeDef,
                                                 String           methodName) throws TypeDefInUseException
    {
        List<TypeDef> knownTypeDefs = repositoryHelper.getKnownTypeDefs();

        if (knownTypeDefs != null)
        {
            for (TypeDef knownTypeDef : knownTypeDefs)
            {
                if ((knownTypeDef != null) && (knownTypeDef.getPropertiesDefinition() != null))
                {
                    for (TypeDefAttribute attribute : knownTypeDef.getPropertiesDefinition())
                    {
                        if ((attribute != null) &&
                            (attribute.getAttributeType() != null) &&
                            (attributeTypeDef.getName().equals(attribute.getAttributeType().getName())))
                        {
                            throw new TypeDefInUseException(OMRSErrorCode.ATTRIBUTE_TYPEDEF_IN_USE.getMessageDefinition(attributeTypeDef.getName(),
                                                                                                                        attributeTypeDef.getGUID(),
                                                                                                                        repositoryName,
                                                                                                                        attribute.getAttributeName(),
                                                                                                                        knownTypeDef.getName()),
                                                            this.getClass().getName(),
                                                            methodName);
                        }
                    }
                }
            }
        }
    }
}
