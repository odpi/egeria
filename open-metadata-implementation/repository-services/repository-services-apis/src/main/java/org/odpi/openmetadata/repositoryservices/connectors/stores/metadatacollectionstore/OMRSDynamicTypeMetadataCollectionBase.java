/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore;


import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.AttributeTypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefPatch;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryValidator;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;

import java.util.Date;
import java.util.List;


/**
 * OMRSDynamicTypeMetadataCollectionBase provides a base class for an open metadata repository that
 * has a dynamic type system.  It begins with no types defined and builds up the knowledge of the types
 * as they are added through the API.
 */
public class OMRSDynamicTypeMetadataCollectionBase extends OMRSMetadataCollectionBase
{
    /**
     * Constructor ensures the metadata collection is linked to its connector and knows its metadata collection Id.
     *
     * @param parentConnector connector that this metadata collection supports.  The connector has the information
     *                        to call the metadata repository.
     * @param repositoryName name of this repository.
     * @param repositoryHelper helper class for building types and instances
     * @param repositoryValidator validator class for checking open metadata repository objects and parameters.
     * @param metadataCollectionId unique identifier of the metadata collection Id.
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
     * Create a definition of a new TypeDef.
     *
     * @param userId unique identifier for requesting user.
     * @param newTypeDef TypeDef structure describing the new TypeDef.
     * @throws InvalidParameterException the new TypeDef is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
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
    }


    /**
     * Create a definition of a new AttributeTypeDef.
     *
     * @param userId unique identifier for requesting user.
     * @param newAttributeTypeDef TypeDef structure describing the new TypeDef.
     * @throws InvalidParameterException the new TypeDef is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
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
    }


    /**
     * Verify that a definition of a TypeDef is either new - or matches the definition already stored.
     *
     * @param userId unique identifier for requesting user.
     * @param typeDef TypeDef structure describing the TypeDef to test.
     * @return boolean - true means the TypeDef matches the local definition - false means the TypeDef is not known.
     * @throws InvalidParameterException the TypeDef is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
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
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
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
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
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
        return repositoryHelper.applyPatch(repositoryName, typeDef, typeDefPatch);
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

        if (typeDef == null)
        {
            super.reportUnknownTypeGUID(obsoleteTypeDefGUID, obsoleteTypeDefName, methodName);
        }

        super.reportTypeDefInUse(obsoleteTypeDefGUID, obsoleteTypeDefName, methodName);
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

        if (attributeTypeDef == null)
        {
            super.reportUnknownTypeGUID(obsoleteTypeDefGUID, obsoleteTypeDefName, methodName);
        }

        super.reportTypeDefInUse(obsoleteTypeDefGUID, obsoleteTypeDefName, methodName);
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
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
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
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
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
     * @return list of all of the classifications for this entity that are homed in this repository
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
     * @return list of all of the classifications for this entity that are homed in this repository
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
}
