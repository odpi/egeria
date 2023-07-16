/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore;


import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.AttributeTypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefPatch;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryValidator;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;

import java.util.ArrayList;
import java.util.List;

/**
 * OMRSFixedTypeMetadataCollectionBase provides a base class for an open metadata repository that
 * has a fixed type system.  Its constructor requires the fixed list of types and attribute types
 * that its implementing repository supports.  Once this is in place,
 * OMRSFixedTypeMetadataCollectionBase can support all the type management methods.
 */
public abstract class OMRSFixedTypeMetadataCollectionBase extends OMRSMetadataCollectionBase
{
    private List<String> supportedAttributeTypeNames = new ArrayList<>();
    private List<String> supportedTypeNames          = new ArrayList<>();

    /**
     * Constructor ensures the metadata collection is linked to its connector and knows its metadata collection id.
     *
     * @param parentConnector connector that this metadata collection supports.  The connector has the information
     *                        to call the metadata repository.
     * @param repositoryName name of this repository.
     * @param repositoryHelper helper class for building types and instances
     * @param repositoryValidator validator class for checking open metadata repository objects and parameters.
     * @param metadataCollectionId unique identifier of the metadata collection id.
     * @param supportedAttributeTypeNames list of attribute type names supported by the implementing repository
     * @param supportedTypeNames list of type names supported by the implementing repository
     */
    public OMRSFixedTypeMetadataCollectionBase(OMRSRepositoryConnector parentConnector,
                                               String                  repositoryName,
                                               OMRSRepositoryHelper    repositoryHelper,
                                               OMRSRepositoryValidator repositoryValidator,
                                               String                  metadataCollectionId,
                                               List<String>            supportedAttributeTypeNames,
                                               List<String>            supportedTypeNames)
    {
        super(parentConnector,
              repositoryName,
              repositoryHelper,
              repositoryValidator,
              metadataCollectionId);

        if (supportedAttributeTypeNames != null)
        {
            this.supportedAttributeTypeNames = supportedAttributeTypeNames;
        }

        if (supportedTypeNames != null)
        {
            this.supportedTypeNames = supportedTypeNames;
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
        this.basicRequestValidation(userId, methodName);
        repositoryValidator.validateTypeDef(repositoryName, typeDefParameterName, typeDef, methodName);

        /*
         * Test type
         */
        if (supportedTypeNames.contains(typeDef.getName()))
        {
            return true;
        }

        super.reportTypeDefNotSupported(typeDef.getName(), methodName);
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
        this.basicRequestValidation(userId, methodName);
        repositoryValidator.validateAttributeTypeDef(repositoryName, typeDefParameterName, attributeTypeDef, methodName);

        /*
         * Test type
         */
        if (supportedAttributeTypeNames.contains(attributeTypeDef.getName()))
        {
            return true;
        }

        super.reportTypeDefNotSupported(attributeTypeDef.getName(), methodName);
        return false;
    }


    /**
     * Create a definition of a new TypeDef.
     *
     * @param userId unique identifier for requesting user.
     * @param newTypeDef TypeDef structure describing the new TypeDef.
     * @throws FunctionNotSupportedException the repository does not support this call.
     */
    public void addTypeDef(String       userId,
                           TypeDef      newTypeDef) throws FunctionNotSupportedException
    {
        final String  methodName = "addTypeDef";

        reportUnsupportedOptionalFunction(methodName);
    }


    /**
     * Create a definition of a new AttributeTypeDef.
     *
     * @param userId unique identifier for requesting user.
     * @param newAttributeTypeDef TypeDef structure describing the new TypeDef.
     * @throws FunctionNotSupportedException the repository does not support this call.
     */
    public  void addAttributeTypeDef(String             userId,
                                     AttributeTypeDef   newAttributeTypeDef) throws FunctionNotSupportedException
    {
        final String  methodName           = "addAttributeTypeDef";

        reportUnsupportedOptionalFunction(methodName);
    }


    /**
     * Update one or more properties of the TypeDef.  The TypeDefPatch controls what types of updates
     * are safe to make to the TypeDef.
     *
     * @param userId unique identifier for requesting user.
     * @param typeDefPatch TypeDef patch describing change to TypeDef.
     * @return updated TypeDef
     * @throws FunctionNotSupportedException the repository does not support this call.
     */
    public TypeDef updateTypeDef(String       userId,
                                 TypeDefPatch typeDefPatch) throws FunctionNotSupportedException
    {
        final String  methodName           = "updateTypeDef";

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
     * @throws FunctionNotSupportedException the repository does not support this call.
     */
    public void deleteTypeDef(String    userId,
                              String    obsoleteTypeDefGUID,
                              String    obsoleteTypeDefName) throws FunctionNotSupportedException
    {
        final String    methodName        = "deleteTypeDef";

        reportUnsupportedOptionalFunction(methodName);
    }


    /**
     * Delete an AttributeTypeDef.  This is only possible if the AttributeTypeDef has never been used to create
     * instances or any instances of this AttributeTypeDef have been purged from the metadata collection.
     *
     * @param userId unique identifier for requesting user.
     * @param obsoleteTypeDefGUID String unique identifier for the AttributeTypeDef.
     * @param obsoleteTypeDefName String unique name for the AttributeTypeDef.
     * @throws FunctionNotSupportedException the repository does not support this call.
     */
    public void deleteAttributeTypeDef(String    userId,
                                       String    obsoleteTypeDefGUID,
                                       String    obsoleteTypeDefName) throws FunctionNotSupportedException
    {
        final String    methodName        = "deleteAttributeTypeDef";

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
     * @throws FunctionNotSupportedException the repository does not support this call.
     */
    public  TypeDef reIdentifyTypeDef(String     userId,
                                      String     originalTypeDefGUID,
                                      String     originalTypeDefName,
                                      String     newTypeDefGUID,
                                      String     newTypeDefName) throws FunctionNotSupportedException
    {
        final String    methodName                = "reIdentifyTypeDef";

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
     * @throws FunctionNotSupportedException the repository does not support this call.
     */
    public  AttributeTypeDef reIdentifyAttributeTypeDef(String     userId,
                                                        String     originalAttributeTypeDefGUID,
                                                        String     originalAttributeTypeDefName,
                                                        String     newAttributeTypeDefGUID,
                                                        String     newAttributeTypeDefName) throws FunctionNotSupportedException
    {
        final String    methodName                = "reIdentifyAttributeTypeDef";

        reportUnsupportedOptionalFunction(methodName);
        return null;
    }
}
