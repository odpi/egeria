/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.localrepository.repositorycontentmanager;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.utilities.OMRSRepositoryPropertiesUtilities;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSAuditCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryValidator;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

import static org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyCategory.ENUM;
import static org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyCategory.PRIMITIVE;
import static org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory.*;

/**
 * OMRSRepositoryContentValidator provides methods to validate TypeDefs and Instances returned from
 * an open metadata repository.  It is typically used by OMRS repository connectors and
 * repository event mappers.
 */
public class OMRSRepositoryContentValidator implements OMRSRepositoryValidator
{
    private static final Logger log = LoggerFactory.getLogger(OMRSRepositoryContentValidator.class);

    private final OMRSRepositoryContentManager    repositoryContentManager;

    private enum MatchOption
    {
        RegexFullMatch,
        RegexContainsMatch,
        ExactMatch
    }


    /**
     * Typical constructor used by the OMRS to create a repository validator for a repository connector.
     *
     * @param repositoryContentManager holds details of valid types and provides the implementation of
     *                                 the repository validator methods
     */
    public OMRSRepositoryContentValidator(OMRSRepositoryContentManager repositoryContentManager)
    {
        final String methodName = "OMRSRepositoryContentValidator";

        this.repositoryContentManager = repositoryContentManager;

        validateRepositoryContentManager(methodName);
    }


    /**
     * Return a boolean flag indicating whether the list of TypeDefs passed are compatible with the
     * all known typedefs.
     * A valid TypeDef is one that matches name, GUID and version to the full list of TypeDefs.
     * If a new TypeDef is present, it is added to the enterprise list.
     *
     * @param sourceName source of the request (used for logging)
     * @param typeDefs list of TypeDefs.
     * @throws RepositoryErrorException a conflicting or invalid TypeDef has been returned
     */
    @Override
    public void   validateEnterpriseTypeDefs(String        sourceName,
                                             List<TypeDef> typeDefs,
                                             String        methodName) throws RepositoryErrorException
    {
        validateRepositoryContentManager(methodName);

        repositoryContentManager.validateEnterpriseTypeDefs(sourceName, typeDefs, methodName);
    }


    /**
     * Return a boolean flag indicating whether the list of TypeDefs passed are compatible with the
     * all known typedefs.
     * A valid TypeDef is one that matches name, GUID and version to the full list of TypeDefs.
     * If a new TypeDef is present, it is added to the enterprise list.
     *
     * @param sourceName source of the request (used for logging)
     * @param attributeTypeDefs list of AttributeTypeDefs.
     * @throws RepositoryErrorException a conflicting or invalid AttributeTypeDef has been returned
     */
    @Override
    public void   validateEnterpriseAttributeTypeDefs(String                 sourceName,
                                                      List<AttributeTypeDef> attributeTypeDefs,
                                                      String                 methodName) throws RepositoryErrorException
    {
        validateRepositoryContentManager(methodName);

        repositoryContentManager.validateEnterpriseAttributeTypeDefs(sourceName, attributeTypeDefs, methodName);
    }


    /**
     * Return boolean indicating whether the TypeDef/AttributeTypeDef is in use in the repository.
     *
     * @param sourceName source of the request (used for logging)
     * @param typeGUID unique identifier of the type
     * @param typeName unique name of the type
     * @return boolean flag
     */
    @Override
    public boolean isActiveType(String   sourceName, String typeGUID, String typeName)
    {
        final String  methodName = "isActiveType";

        validateRepositoryContentManager(methodName);

        return repositoryContentManager.isActiveType(sourceName, typeGUID, typeName);
    }


    /**
     * Return boolean indicating whether the TypeDef/AttributeTypeDef is in use in the repository.
     *
     * @param sourceName source of the request (used for logging)
     * @param typeGUID unique identifier of the type
     * @return boolean flag
     */
    @Override
    public boolean isActiveTypeId(String   sourceName, String typeGUID)
    {
        final String  methodName = "isActiveTypeId";

        validateRepositoryContentManager(methodName);

        return repositoryContentManager.isActiveTypeId(typeGUID);
    }


    /**
     * Return boolean indicating whether the TypeDef is one of the open metadata types.
     *
     * @param sourceName source of the request (used for logging)
     * @param typeGUID unique identifier of the type
     * @param typeName unique name of the type
     * @return boolean flag
     */
    @Override
    public boolean isOpenType(String   sourceName, String typeGUID, String typeName)
    {
        final String  methodName = "isOpenType";

        validateRepositoryContentManager(methodName);

        return repositoryContentManager.isOpenType(sourceName, typeGUID, typeName);
    }


    /**
     * Return boolean indicating whether the TypeDef is one of the open metadata types.
     *
     * @param sourceName source of the request (used for logging)
     * @param typeGUID unique identifier of the type
     * @return boolean flag
     */
    @Override
    public boolean isOpenTypeId(String   sourceName, String typeGUID)
    {
        final String  methodName = "isOpenTypeId";

        validateRepositoryContentManager(methodName);

        return repositoryContentManager.isOpenTypeId(typeGUID);
    }


    /**
     * Return boolean indicating whether the TypeDef/AttributeTypeDef is in use in the repository.
     *
     * @param sourceName source of the request (used for logging)
     * @param typeGUID unique identifier of the type
     * @param typeName unique name of the type
     * @return boolean flag
     */
    @Override
    public boolean isKnownType(String   sourceName, String typeGUID, String typeName)
    {
        final String  methodName = "isKnownType";

        validateRepositoryContentManager(methodName);

        return repositoryContentManager.isKnownType(sourceName, typeGUID, typeName);
    }


    /**
     * Return boolean indicating whether the TypeDef/AttributeTypeDef is in use in the repository.
     *
     * @param sourceName source of the request (used for logging)
     * @param typeGUID unique identifier of the type
     * @return boolean flag
     */
    @Override
    public boolean isKnownTypeId(String   sourceName, String typeGUID)
    {
        final String  methodName = "isKnownTypeId";

        validateRepositoryContentManager(methodName);

        return repositoryContentManager.isKnownTypeId(typeGUID);
    }


    /**
     * Return boolean indicating whether the TypeDef identifiers are from a single known type or not.
     *
     * @param sourceName source of the request (used for logging)
     * @param typeGUID unique identifier of the TypeDef
     * @param typeName unique name of the TypeDef
     * @return boolean result
     */
    @Override
    public boolean validTypeId(String sourceName,
                               String typeGUID,
                               String typeName)
    {
        final String  methodName = "validTypeId";

        validateRepositoryContentManager(methodName);

        return repositoryContentManager.validTypeId(sourceName, typeGUID, typeName);
    }


    /**
     * Return boolean indicating whether the TypeDef identifiers are from a single known type or not.
     *
     * @param sourceName source of the request (used for logging)
     * @param typeDefGUID unique identifier of the TypeDef
     * @param typeDefName unique name of the TypeDef
     * @param category category for the TypeDef
     * @return boolean result
     */
    @Override
    public boolean validTypeDefId(String          sourceName,
                                  String          typeDefGUID,
                                  String          typeDefName,
                                  TypeDefCategory category)
    {
        final String  methodName = "validTypeDefId";

        validateRepositoryContentManager(methodName);

        return repositoryContentManager.validTypeDefId(sourceName, typeDefGUID, typeDefName, category);
    }


    /**
     * Return boolean indicating whether the AttributeTypeDef identifiers are from a single known type or not.
     *
     * @param sourceName source of the request (used for logging)
     * @param attributeTypeDefGUID unique identifier of the AttributeTypeDef
     * @param attributeTypeDefName unique name of the AttributeTypeDef
     * @param category category for the AttributeTypeDef
     * @return boolean result
     */
    @Override
    public boolean validAttributeTypeDefId(String                   sourceName,
                                           String                   attributeTypeDefGUID,
                                           String                   attributeTypeDefName,
                                           AttributeTypeDefCategory category)
    {
        final String  methodName = "validAttributeTypeDefId";

        validateRepositoryContentManager(methodName);

       return repositoryContentManager.validAttributeTypeDefId(sourceName,
                                                               attributeTypeDefGUID,
                                                               attributeTypeDefName,
                                                               category);
    }



    /**
     * Return boolean indicating whether the TypeDef identifiers are from a single known type or not.
     *
     * @param sourceName source of the request (used for logging)
     * @param typeDefGUID unique identifier of the TypeDef
     * @param typeDefName unique name of the TypeDef
     * @param typeDefVersion version of the type
     * @param category category for the TypeDef
     * @return boolean result
     */
    @Override
    public boolean validTypeDefId(String          sourceName,
                                  String          typeDefGUID,
                                  String          typeDefName,
                                  String          typeDefVersion,
                                  TypeDefCategory category)
    {
        final String  methodName = "validTypeDefId";

        validateRepositoryContentManager(methodName);

        return repositoryContentManager.validTypeDefId(sourceName,
                                                       typeDefGUID,
                                                       typeDefName,
                                                       typeDefVersion,
                                                       category);
    }


    /**
     * Return boolean indicating whether the TypeDef identifiers are from a single known type or not.
     *
     * @param sourceName source of the request (used for logging)
     * @param attributeTypeDefGUID unique identifier of the TypeDef
     * @param attributeTypeDefName unique name of the TypeDef
     * @param attributeTypeDefVersion version of the type
     * @param category category for the TypeDef
     * @return boolean result
     */
    @Override
    public boolean validAttributeTypeDefId(String                   sourceName,
                                           String                   attributeTypeDefGUID,
                                           String                   attributeTypeDefName,
                                           String                   attributeTypeDefVersion,
                                           AttributeTypeDefCategory category)
    {
        final String  methodName = "validAttributeTypeDefId";

        validateRepositoryContentManager(methodName);

        return repositoryContentManager.validAttributeTypeDefId(sourceName,
                                                                attributeTypeDefGUID,
                                                                attributeTypeDefName,
                                                                attributeTypeDefVersion,
                                                                category);
    }


    /**
     * Validate that the supplied type is a valid active type.
     *
     * @param sourceName source of the request (used for logging)
     * @param typeParameterName the name of the parameter that passed the type
     * @param typeDefSummary the type to test
     * @param category the expected category of the type
     * @param methodName the name of the method that supplied the type
     * @throws InvalidParameterException the type is null or contains invalid values
     * @throws TypeErrorException the type is not active
     */
    @Override
    public void validateActiveType(String           sourceName,
                                   String           typeParameterName,
                                   TypeDefSummary   typeDefSummary,
                                   TypeDefCategory  category,
                                   String           methodName) throws TypeErrorException, InvalidParameterException
    {
        if (! this.isActiveType(sourceName, typeDefSummary.getGUID(), typeDefSummary.getName()))
        {
            throw new TypeErrorException(OMRSErrorCode.TYPEDEF_NOT_KNOWN.getMessageDefinition(typeDefSummary.getName(),
                                                                                              typeDefSummary.getGUID(),
                                                                                              typeParameterName,
                                                                                              methodName,
                                                                                              sourceName),
                                         this.getClass().getName(),
                                         methodName);
        }

        // todo check category
    }


    /**
     * Return boolean indicating whether the supplied TypeDef is valid or not.
     *
     * @param sourceName source of the TypeDef (used for logging)
     * @param typeDef TypeDef to test
     * @return boolean result
     */
    @Override
    public boolean validTypeDef(String         sourceName,
                                TypeDef        typeDef)
    {
        final String methodName = "validTypeDef";

        validateRepositoryContentManager(methodName);

        return repositoryContentManager.validTypeDef(sourceName, typeDef);
    }


    /**
     * Return boolean indicating whether the supplied AttributeTypeDef is valid or not.
     *
     * @param sourceName source of the request (used for logging)
     * @param attributeTypeDef TypeDef to test
     * @return boolean result
     */
    @Override
    public boolean validAttributeTypeDef(String           sourceName,
                                         AttributeTypeDef attributeTypeDef)
    {
        final String  methodName = "validAttributeTypeDef";

        validateRepositoryContentManager(methodName);

        return repositoryContentManager.validAttributeTypeDef(sourceName, attributeTypeDef);
    }


    /**
     * Return boolean indicating whether the supplied TypeDefSummary is valid or not.
     *
     * @param sourceName source of the request (used for logging)
     * @param typeDefSummary TypeDefSummary to test.
     * @return boolean result.
     */
    @Override
    public boolean validTypeDefSummary(String         sourceName,
                                       TypeDefSummary typeDefSummary)
    {
        final String  methodName = "validTypeDefSummary";

        validateRepositoryContentManager(methodName);

        return repositoryContentManager.validTypeDefSummary(sourceName, typeDefSummary);
    }


    /**
     * Test that the supplied entity is valid.
     *
     * @param sourceName source of the request (used for logging)
     * @param entity entity to test
     * @return boolean result
     */
    @Override
    public boolean validEntity(String        sourceName,
                               EntitySummary entity)
    {
        final String methodName = "validEntity";

        return validInstance(sourceName, entity, methodName, true) &&
                validClassifications(sourceName, entity);
    }


    /**
     * Check that the classifications for an entity are valid.
     *
     * @param sourceName source of the request (used for logging)
     * @param entity entity to test
     * @return boolean result
     */
    private boolean validClassifications(String        sourceName,
                                         EntitySummary entity)
    {
        final String methodName = "validClassifications";

        if (entity.getClassifications() != null)
        {
            for (Classification classification : entity.getClassifications())
            {
                if (classification == null)
                {
                    return false;
                }

                if (classification.getName() == null)
                {
                    repositoryContentManager.logNullInstance(sourceName, methodName);
                    return false;
                }

                if (! this.validInstanceAuditHeader(sourceName,
                                                    entity.getGUID() + ":" + classification.getName(),
                                                    classification,
                                                    methodName,
                                                    true))
                {
                    return false;
                }

                if (! classification.getName().equals(classification.getType().getTypeDefName()))
                {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Test that the supplied entity is valid.
     *
     * @param sourceName source of the request (used for logging)
     * @param entity entity to test
     * @return boolean result
     */
    @Override
    public boolean validEntity(String      sourceName,
                               EntityProxy entity)
    {
        return this.validEntity(sourceName, (EntitySummary)entity);
    }


    /**
     * Test that the supplied entity is valid.
     *
     * @param sourceName source of the request (used for logging)
     * @param entity entity to test
     * @return boolean result
     */
    @Override
    public boolean validEntity(String       sourceName,
                               EntityDetail entity)
    {
        return this.validEntity(sourceName, (EntitySummary)entity);
    }


    /**
     * Test that the supplied relationship is valid.
     *
     * @param sourceName source of the request (used for logging)
     * @param relationship relationship to test
     * @return boolean result
     */
    @Override
    public boolean validRelationship(String       sourceName,
                                     Relationship relationship)
    {
        final String methodName    = "validRelationship";
        // 1. Validate the instance
        boolean valid = validInstance(sourceName, relationship, methodName, false);
        // 2. Validate the ends of the relationship
        if (relationship.getEntityOneProxy() == null || relationship.getEntityTwoProxy() == null)
        {
            valid = false;
            repositoryContentManager.logNullInstance(sourceName, methodName);
        }
        return valid;
    }


    /**
     * Test that the supplied instance is valid.
     *
     * @param sourceName source of the request (used for logging)
     * @param instance instance to test
     * @param methodName calling method
     * @param fromStore is the entity from the store?
     * @return boolean result
     */
    private boolean validInstance(String         sourceName,
                                  InstanceHeader instance,
                                  String         methodName,
                                  boolean        fromStore)
    {
        if (instance == null)
        {
            repositoryContentManager.logNullInstance(sourceName, methodName);
            return false;
        }

        InstanceType instanceType = instance.getType();

        if (instanceType == null)
        {
            repositoryContentManager.logNullType(sourceName, methodName);
            return false;
        }

        if (! validInstanceId(sourceName,
                              instanceType.getTypeDefGUID(),
                              instanceType.getTypeDefName(),
                              instanceType.getTypeDefCategory(),
                              instance.getGUID()))
        {
            /*
             * Error already logged
             */
            return false;
        }

        if (! fromStore)
        {
            String homeMetadataCollectionId = instance.getMetadataCollectionId();

            if (homeMetadataCollectionId == null)
            {
                repositoryContentManager.logNullMetadataCollectionId(sourceName,
                                                                     instanceType.getTypeDefGUID(),
                                                                     instanceType.getTypeDefName(),
                                                                     instanceType.getTypeDefCategory().getName(),
                                                                     instance.getGUID(),
                                                                     methodName);
                return false;
            }
        }

        return true;
    }


    /**
     * Test that the supplied instance is valid.
     *
     * @param sourceName source of the request (used for logging)
     * @param identifier identifier of the instance (typically guid - but may include classification name)
     * @param instance instance to test
     * @param methodName calling method
     * @param fromStore is the entity from the store?
     * @return boolean result
     */
    private boolean validInstanceAuditHeader(String              sourceName,
                                             String              identifier,
                                             InstanceAuditHeader instance,
                                             String              methodName,
                                             boolean             fromStore)
    {
        if (instance == null)
        {
            repositoryContentManager.logNullInstance(sourceName, methodName);
            return false;
        }

        InstanceType instanceType = instance.getType();

        if (instanceType == null)
        {
            repositoryContentManager.logNullType(sourceName, methodName);
            return false;
        }

        if (! validTypeDefId(sourceName,
                             instanceType.getTypeDefGUID(),
                             instanceType.getTypeDefName(),
                             instanceType.getTypeDefCategory()))
        {
            /*
             * Error already logged
             */
            return false;
        }

        if (! fromStore)
        {
            String homeMetadataCollectionId = instance.getMetadataCollectionId();

            if (homeMetadataCollectionId == null)
            {
                repositoryContentManager.logNullMetadataCollectionId(sourceName,
                                                                     instanceType.getTypeDefGUID(),
                                                                     instanceType.getTypeDefName(),
                                                                     instanceType.getTypeDefCategory().getName(),
                                                                     identifier,
                                                                     methodName);
                return false;
            }
        }

        return true;
    }


    /**
     * Verify that the identifiers for an instance are correct.
     *
     * @param sourceName source of the request (used for logging)
     * @param typeDefGUID unique identifier for the type.
     * @param typeDefName unique name for the type.
     * @param category expected category of the instance.
     * @param instanceGUID unique identifier for the instance.
     * @return boolean indicating whether the identifiers are ok.
     */
    @Override
    public boolean validInstanceId(String           sourceName,
                                   String           typeDefGUID,
                                   String           typeDefName,
                                   TypeDefCategory  category,
                                   String           instanceGUID)
    {

        if (! validTypeDefId(sourceName,
                             typeDefGUID,
                             typeDefName,
                             category))
        {
            /*
             * Error messages already logged
             */
            return false;
        }

        if (instanceGUID == null)
        {
            repositoryContentManager.logNullInstanceGUID(sourceName, typeDefGUID, typeDefName, category);
            return false;
        }

        return true;
    }


    /* ==============================================================
     * Simple parameter validation methods needed by all repositories
     * ==============================================================
     */


    /**
     * Validate that the supplied user id is not null.
     *
     * @param sourceName name of source of request.
     * @param userId userId passed on call to this metadata collection.
     * @param methodName name of method requesting the validation.
     * @throws InvalidParameterException the userId is invalid
     */
    @Override
    public  void validateUserId(String  sourceName,
                                String  userId,
                                String  methodName) throws InvalidParameterException
    {
        final String userIdParameterName = "userId";

        if ((userId == null) || (userId.length() == 0))
        {
            throw new InvalidParameterException(OMRSErrorCode.NULL_USER_ID.getMessageDefinition(userIdParameterName, methodName, sourceName),
                                                this.getClass().getName(),
                                                methodName,
                                                userIdParameterName);
        }
    }


    /**
     * Validate that a TypeDef's identifiers are not null.
     *
     * @param sourceName source of the request (used for logging)
     * @param guidParameterName name of the parameter that passed the guid.
     * @param nameParameterName name of the parameter that passed the name.
     * @param guid unique identifier for a type or an instance passed on the request
     * @param name name of TypeDef.
     * @param methodName method receiving the call
     * @throws InvalidParameterException no guid provided
     */
    @Override
    public  void validateTypeDefIds(String sourceName,
                                    String guidParameterName,
                                    String nameParameterName,
                                    String guid,
                                    String name,
                                    String methodName) throws InvalidParameterException
    {
        if (guid == null)
        {
            throw new InvalidParameterException(OMRSErrorCode.NULL_TYPEDEF_IDENTIFIER.getMessageDefinition(guidParameterName,
                                                                                                           methodName,
                                                                                                           sourceName),
                                                this.getClass().getName(),
                                                methodName,
                                                guidParameterName);
        }
        else if (name == null)
        {
            throw new InvalidParameterException(OMRSErrorCode.NO_TYPEDEF_NAME.getMessageDefinition(nameParameterName,
                                                                                                   methodName,
                                                                                                   sourceName),
                                                this.getClass().getName(),
                                                methodName,
                                                nameParameterName);
        }
    }


    /**
     * Validate that a TypeDef's identifiers are not null and return the type.
     *
     * @param sourceName source of the request (used for logging)
     * @param guidParameterName name of the parameter that passed the guid.
     * @param nameParameterName name of the parameter that passed the name.
     * @param guid unique identifier for a type or an instance passed on the request
     * @param name name of TypeDef.
     * @param methodName method receiving the call
     * @return  typeDef
     * @throws InvalidParameterException no guid provided
     */
    @Override
    public  TypeDef getValidTypeDefFromIds(String sourceName,
                                           String guidParameterName,
                                           String nameParameterName,
                                           String guid,
                                           String name,
                                           String methodName) throws InvalidParameterException
    {
        this.validateTypeDefIds(sourceName, guidParameterName, nameParameterName, guid, name, methodName);

        try
        {
            return repositoryContentManager.getTypeDef(sourceName, guidParameterName, guid, methodName);
        }
        catch (TypeErrorException error)
        {
            throw new InvalidParameterException(error, guidParameterName + " or " + nameParameterName);
        }
    }


    /**
     * Validate that an AttributeTypeDef's identifiers are not null and are recognized.
     *
     * @param sourceName source of the request (used for logging)
     * @param guidParameterName name of the parameter that passed the guid.
     * @param nameParameterName name of the parameter that passed the name.
     * @param guid unique identifier for a type or an instance passed on the request
     * @param name name of TypeDef.
     * @param methodName method receiving the call
     * @throws InvalidParameterException no guid, or name provided
     */
    @Override
    public  void validateAttributeTypeDefIds(String sourceName,
                                                         String guidParameterName,
                                                         String nameParameterName,
                                                         String guid,
                                                         String name,
                                                         String methodName) throws InvalidParameterException
    {
        if (guid == null)
        {
            throw new InvalidParameterException(OMRSErrorCode.NULL_ATTRIBUTE_TYPEDEF_IDENTIFIER.getMessageDefinition(guidParameterName,
                                                                                                                     methodName,
                                                                                                                     sourceName),
                                                this.getClass().getName(),
                                                methodName,
                                                guidParameterName);
        }
        else if (name == null)
        {
            throw new InvalidParameterException(OMRSErrorCode.NO_ATTRIBUTE_TYPEDEF_NAME.getMessageDefinition(nameParameterName,
                                                                                                             methodName,
                                                                                                             sourceName),
                                                this.getClass().getName(),
                                                methodName,
                                                nameParameterName);
        }
    }


    /**
     * Validate that an AttributeTypeDef's identifiers are not null and are recognized.
     *
     * @param sourceName source of the request (used for logging)
     * @param guidParameterName name of the parameter that passed the guid.
     * @param nameParameterName name of the parameter that passed the name.
     * @param guid unique identifier for a type or an instance passed on the request
     * @param name name of TypeDef.
     * @param methodName method receiving the call
     * @return retrieved type definition
     * @throws InvalidParameterException no guid, or name provided
     */
    @Override
    public  AttributeTypeDef getValidAttributeTypeDefFromIds(String sourceName,
                                                             String guidParameterName,
                                                             String nameParameterName,
                                                             String guid,
                                                             String name,
                                                             String methodName) throws InvalidParameterException
    {
        this.validateAttributeTypeDefIds(sourceName, guidParameterName, nameParameterName, guid, name, methodName);

        try
        {
            return repositoryContentManager.getAttributeTypeDef(sourceName, guidParameterName, guid, methodName);
        }
        catch (TypeErrorException error)
        {
            throw new InvalidParameterException(error, guidParameterName + " or " + nameParameterName);
        }
    }


    /**
     * Validate that type's identifier is not null.
     *
     * @param sourceName source of the request (used for logging)
     * @param guidParameterName name of the parameter that passed the guid.
     * @param guid unique identifier for a type or an instance passed on the request
     * @param methodName method receiving the call
     * @throws InvalidParameterException no guid provided
     * @throws TypeErrorException guid is not for a recognized type
     */
    @Override
    public  void validateTypeGUID(String sourceName,
                                  String guidParameterName,
                                  String guid,
                                  String methodName) throws InvalidParameterException,
                                                            TypeErrorException
    {
        if (guid == null)
        {
            throw new InvalidParameterException(OMRSErrorCode.NULL_TYPEDEF_IDENTIFIER.getMessageDefinition(guidParameterName,
                                                                                                           methodName,
                                                                                                           sourceName),
                                                this.getClass().getName(),
                                                methodName,
                                                guidParameterName);
        }

        validateOptionalTypeGUID(sourceName, guidParameterName, guid, methodName);
    }


    /**
     * Validate that type's identifier is not null.
     *
     * @param sourceName source of the request (used for logging)
     * @param guidParameterName name of the parameter that passed the guid.
     * @param guid unique identifier for a type or an instance passed on the request
     * @param methodName method receiving the call
     * @throws TypeErrorException unknown type guid
     */
    @Override
    public  void validateOptionalTypeGUID(String sourceName,
                                          String guidParameterName,
                                          String guid,
                                          String methodName) throws TypeErrorException
    {
        if (guid != null)
        {
            if (! isKnownTypeId(sourceName, guid))
            {
                throw new TypeErrorException(OMRSErrorCode.TYPEDEF_ID_NOT_KNOWN.getMessageDefinition(guid,
                                                                                                     guidParameterName,
                                                                                                     methodName,
                                                                                                     sourceName),
                                             this.getClass().getName(),
                                             methodName);
            }
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public  void validateOptionalTypeGUIDs(String sourceName,
                                           String guidParameterName,
                                           String guid,
                                           String subtypeParameterName,
                                           List<String> subtypeGuids,
                                           String methodName) throws TypeErrorException
    {
        validateOptionalTypeGUID(sourceName, guidParameterName, guid, methodName);
        if (subtypeGuids != null)
        {
            List<String> invalidSubtypes = new ArrayList<>();
            for (String subtype : subtypeGuids)
            {
                if (! isKnownTypeId(sourceName, subtype))
                {
                    throw new TypeErrorException(OMRSErrorCode.TYPEDEF_ID_NOT_KNOWN.getMessageDefinition(subtype,
                                                                                                         subtypeParameterName,
                                                                                                         methodName,
                                                                                                         sourceName),
                                                 this.getClass().getName(),
                                                 methodName);
                }
                else
                {
                    validateRepositoryContentManager(methodName);
                    TypeDef subtypeDef = repositoryContentManager.getTypeDef(sourceName, subtypeParameterName, subtype, methodName);
                    if (! repositoryContentManager.isTypeOfByGUID(sourceName, subtype, subtypeDef.getName(), guid))
                    {
                        invalidSubtypes.add(subtype);
                    }
                }
            }
            if (! invalidSubtypes.isEmpty())
            {
                throw new TypeErrorException(OMRSErrorCode.TYPEDEF_NOT_SUBTYPE.getMessageDefinition(invalidSubtypes.toString(), guid),
                                             this.getClass().getName(),
                                             methodName);
            }
        }
    }


    /**
     * Verify that a TypeDefPatch is not null and is for a recognized type.
     *
     * @param sourceName source of the request (used for logging)
     * @param patch patch to test
     * @param methodName calling method
     * @return current value of the type
     * @throws InvalidParameterException the patch is null
     * @throws TypeDefNotKnownException the type is not known
     * @throws PatchErrorException the patch is invalid
     */
    @Override
    public TypeDef validateTypeDefPatch(String       sourceName,
                                        TypeDefPatch patch,
                                        String       methodName) throws InvalidParameterException,
                                                                        TypeDefNotKnownException,
                                                                        PatchErrorException
    {
        OMRSRepositoryPropertiesUtilities utilities = new OMRSRepositoryPropertiesUtilities();

        utilities.validateTypeDefPatch(sourceName, patch, methodName);

        return repositoryContentManager.getTypeDefByName(patch.getTypeDefName());
    }


    /**
     * Validate that if a type's identifier is passed then it is valid.
     *
     * @param sourceName source of the request (used for logging)
     * @param guidParameterName name of the parameter that passed the guid.
     * @param guid unique identifier for a type or an instance passed on the request
     * @param methodName method receiving the call
     * @throws TypeErrorException invalid provided
     */
    @Override
    public  void validateInstanceTypeGUID(String sourceName,
                                          String guidParameterName,
                                          String guid,
                                          String methodName) throws TypeErrorException
    {
        if (guid == null)
        {
            throw new TypeErrorException(OMRSErrorCode.BAD_TYPEDEF_ID_FOR_INSTANCE.getMessageDefinition(guidParameterName,
                                                                                                        methodName,
                                                                                                        sourceName),
                                         this.getClass().getName(),
                                         methodName);
        }
    }


    /**
     * Validate that type's name is not null.
     *
     * @param sourceName source of the request (used for logging)
     * @param nameParameterName name of the parameter that passed the name.
     * @param name unique identifier for a type or an instance passed on the request
     * @param methodName method receiving the call
     * @throws InvalidParameterException no name provided
     */
    @Override
    public  void validateTypeName(String sourceName,
                                  String nameParameterName,
                                  String name,
                                  String methodName) throws InvalidParameterException
    {
        if (name == null)
        {
            throw new InvalidParameterException(OMRSErrorCode.NO_TYPEDEF_NAME.getMessageDefinition(nameParameterName,
                                                                                                   methodName,
                                                                                                   sourceName),
                                                this.getClass().getName(),
                                                methodName,
                                                nameParameterName);
        }
    }


    /**
     * Validate that a TypeDef's category is not null.
     *
     * @param sourceName source of the request (used for logging)
     * @param nameParameterName name of the parameter that passed the name.
     * @param category category of TypeDef
     * @param methodName method receiving the call
     * @throws InvalidParameterException no name provided
     */
    @Override
    public  void validateTypeDefCategory(String          sourceName,
                                         String          nameParameterName,
                                         TypeDefCategory category,
                                         String          methodName) throws InvalidParameterException
    {
        if (category == null)
        {
            throw new InvalidParameterException(OMRSErrorCode.NO_TYPEDEF_CATEGORY.getMessageDefinition(nameParameterName,
                                                                                                       methodName,
                                                                                                       sourceName),
                                                this.getClass().getName(),
                                                methodName,
                                                nameParameterName);
        }
    }


    /**
     * Validate that a AttributeTypeDef's category is not null.
     *
     * @param sourceName source of the request (used for logging)
     * @param nameParameterName name of the parameter that passed the name.
     * @param category category of TypeDef
     * @param methodName method receiving the call
     * @throws InvalidParameterException no name provided
     */
    @Override
    public  void validateAttributeTypeDefCategory(String                   sourceName,
                                                  String                   nameParameterName,
                                                  AttributeTypeDefCategory category,
                                                  String                   methodName) throws InvalidParameterException
    {
        if (category == null)
        {
            throw new InvalidParameterException(OMRSErrorCode.NO_ATTRIBUTE_TYPEDEF_CATEGORY.getMessageDefinition(nameParameterName,
                                                                                                                 methodName,
                                                                                                                 sourceName),
                                                this.getClass().getName(),
                                                methodName,
                                                nameParameterName);
        }
    }


    /**
     * Validate the content of a new TypeDef is valid.
     *
     * @param sourceName source of the request (used for logging)
     * @param parameterName name of the parameter that passed the typeDef.
     * @param typeDef unique identifier for a type or an instance passed on the request
     * @param methodName method receiving the call
     * @throws InvalidParameterException no typeDef provided
     * @throws InvalidTypeDefException invalid typeDef provided
     */
    @Override
    public  void validateTypeDef(String  sourceName,
                                 String  parameterName,
                                 TypeDef typeDef,
                                 String  methodName) throws InvalidParameterException, InvalidTypeDefException
    {
        if (typeDef == null)
        {
            throw new InvalidParameterException(OMRSErrorCode.NULL_TYPEDEF.getMessageDefinition(parameterName,
                                                                                                methodName,
                                                                                                sourceName),
                                                this.getClass().getName(),
                                                methodName,
                                                parameterName);
        }

        if (typeDef.getHeaderVersion() > TypeDefElementHeader.CURRENT_TYPE_DEF_HEADER_VERSION)
        {
            throw new InvalidTypeDefException(OMRSErrorCode.UNSUPPORTED_TYPE_HEADER_VERSION.getMessageDefinition(methodName,
                                                                                                                 sourceName,
                                                                                                                 typeDef.getName(),
                                                                                                                 Long.toString(typeDef.getHeaderVersion()),
                                                                                                                 Long.toString(typeDef.getHeaderVersion())),
                                              this.getClass().getName(),
                                              methodName);
        }

        validateTypeDefIds(sourceName,
                           parameterName + ".getGUID",
                           parameterName + ".getName",
                           typeDef.getGUID(),
                           typeDef.getName(),
                           methodName);

        validateTypeDefCategory(sourceName,
                                parameterName + ".getCategory",
                                typeDef.getCategory(),
                                methodName);


    }


    /**
     * Validate the content of a new TypeDef is known.
     *
     * @param sourceName source of the request (used for logging)
     * @param parameterName name of the parameter that passed the typeDef.
     * @param typeDef unique identifier for a type or an instance passed on the request
     * @param methodName method receiving the call
     * @throws TypeDefNotKnownException typeDef provided not recognized
     */
    @Override
    public  void validateKnownTypeDef(String  sourceName,
                                      String  parameterName,
                                      TypeDef typeDef,
                                      String  methodName) throws TypeDefNotKnownException
    {
        final String  thisMethodName = "validateKnownTypeDef";

        validateRepositoryContentManager(thisMethodName);

        if (! repositoryContentManager.isKnownType(sourceName, typeDef.getGUID(), typeDef.getName()))
        {
            throw new TypeDefNotKnownException(OMRSErrorCode.TYPEDEF_NOT_KNOWN.getMessageDefinition(typeDef.getName(),
                                                                                                    typeDef.getGUID(),
                                                                                                    parameterName,
                                                                                                    methodName,
                                                                                                    sourceName),
                                               this.getClass().getName(),
                                               methodName);

        }
    }


    /**
     * Validate the content of a new TypeDef is known.
     *
     * @param sourceName source of the request (used for logging)
     * @param parameterName name of the parameter that passed the typeDef.
     * @param typeDef unique identifier for a type or an instance passed on the request
     * @param methodName method receiving the call
     * @throws TypeDefKnownException the TypeDef is already defined
     * @throws TypeDefConflictException the TypeDef is already defined but differently
     */
    @Override
    public  void validateUnknownTypeDef(String  sourceName,
                                        String  parameterName,
                                        TypeDef typeDef,
                                        String  methodName) throws TypeDefKnownException,
                                                                   TypeDefConflictException
    {
        final String  thisMethodName = "validateUnknownTypeDef";

        validateRepositoryContentManager(thisMethodName);

        if (repositoryContentManager.isKnownType(sourceName, typeDef.getGUID(), typeDef.getName()))
        {
            // todo validate that the existing typeDef matches the new one.

            throw new TypeDefKnownException(OMRSErrorCode.TYPEDEF_ALREADY_DEFINED.getMessageDefinition(typeDef.getName(),
                                                                                                       typeDef.getGUID(),
                                                                                                       sourceName),
                                            this.getClass().getName(),
                                            methodName);

        }
    }


    /**
     * Validate the content of a new TypeDef is known.
     *
     * @param sourceName source of the request (used for logging)
     * @param parameterName name of the parameter that passed the typeDef.
     * @param attributeTypeDef unique identifier for an attribute type or an instance passed on the request
     * @param methodName method receiving the call
     * @throws TypeDefKnownException the TypeDef is already defined
     * @throws TypeDefConflictException the TypeDef is already defined but differently
     */
    @Override
    public  void validateUnknownAttributeTypeDef(String           sourceName,
                                                 String           parameterName,
                                                 AttributeTypeDef attributeTypeDef,
                                                 String           methodName) throws TypeDefKnownException,
                                                                                     TypeDefConflictException
    {
        final String  thisMethodName = "validateUnknownTypeDef";

        validateRepositoryContentManager(thisMethodName);

        if (repositoryContentManager.isKnownType(sourceName,
                                                 attributeTypeDef.getGUID(),
                                                 attributeTypeDef.getName()))
        {
            throw new TypeDefKnownException(OMRSErrorCode.ATTRIBUTE_TYPEDEF_ALREADY_DEFINED.getMessageDefinition(attributeTypeDef.getName(),
                                                                                                                 attributeTypeDef.getGUID(),
                                                                                                                 sourceName),
                                            this.getClass().getName(),
                                            methodName);

        }
    }


    /**
     * Validate the content of a TypeDef associated with a metadata instance.
     *
     * @param sourceName source of the request (used for logging)
     * @param parameterName name of the parameter that passed the typeDef.
     * @param typeDef unique identifier for a type or an instance passed on the request
     * @param methodName method receiving the call
     * @throws TypeErrorException no typeDef provided
     * @throws RepositoryErrorException the TypeDef from the repository is in error.
     */
    @Override
    public  void validateTypeDefForInstance(String  sourceName,
                                            String  parameterName,
                                            TypeDef typeDef,
                                            String  methodName) throws TypeErrorException,
                                                                       RepositoryErrorException
    {
        final String thisMethodName = "validateTypeDefForInstance";

        if (typeDef == null)
        {
            throw new TypeErrorException(OMRSErrorCode.NULL_TYPEDEF.getMessageDefinition(parameterName,
                                                                                         methodName,
                                                                                         sourceName),
                                         this.getClass().getName(),
                                         methodName);
        }

        try
        {
            validateTypeDefIds(sourceName,
                               parameterName + ".getGUID",
                               parameterName + ".getName",
                               typeDef.getGUID(),
                               typeDef.getName(),
                               methodName);

            validateTypeDefCategory(sourceName,
                                    parameterName + ".getCategory",
                                    typeDef.getCategory(),
                                    methodName);
        }
        catch (Exception    error)
        {
           throw new RepositoryErrorException(OMRSErrorCode.BAD_TYPEDEF.getMessageDefinition(thisMethodName,
                                                                                              typeDef.getName(),
                                                                                              sourceName,
                                                                                              methodName),
                                               this.getClass().getName(),
                                               methodName);
        }
    }


    /**
     * Validate that the supplied TypeDef GUID and name matches the type associated with a metadata instance.
     *
     * @param sourceName source of the request (used for logging)
     * @param typeDefGUID the supplied typeDef GUID.
     * @param typeDefName the supplied typeDef name.
     * @param instance instance retrieved from the store with the supplied instance guid
     * @param methodName method making this call
     * @throws InvalidParameterException incompatibility detected between the TypeDef and the instance's type
     * @throws RepositoryErrorException the instance from the repository is in error.
     */
    @Override
    public  void validateTypeForInstanceDelete(String         sourceName,
                                               String         typeDefGUID,
                                               String         typeDefName,
                                               InstanceHeader instance,
                                               String         methodName) throws InvalidParameterException,
                                                                                 RepositoryErrorException
    {
        final String localMethodName = "validateTypeForInstanceDelete";

        /*
         * Just make sure the instance has a type :)
         */
        this.validateInstanceType(sourceName, instance);

        /*
         * Both the GUID and the name must match
         */
        TypeDef knownType = repositoryContentManager.getTypeDefByName(typeDefName);

        if (knownType == null)
        {
            throw new InvalidParameterException(OMRSErrorCode.BAD_TYPEDEF_IDS_FOR_DELETE.getMessageDefinition(typeDefName,
                                                                                                              typeDefGUID,
                                                                                                              methodName,
                                                                                                              instance.getGUID(),
                                                                                                              sourceName),
                                                this.getClass().getName(),
                                                methodName,
                                                "typeDefName");
        }
        else if (! typeDefGUID.equals(knownType.getGUID()))
        {
            throw new InvalidParameterException(OMRSErrorCode.BAD_TYPEDEF_IDS_FOR_DELETE.getMessageDefinition(typeDefName,
                                                                                                              typeDefGUID,
                                                                                                              methodName,
                                                                                                              instance.getGUID(),
                                                                                                              sourceName),
                                                this.getClass().getName(),
                                                methodName,
                                                "typeDefGUID");
        }
        else if (! this.isATypeOf(sourceName, instance, typeDefName, localMethodName))
        {
            throw new InvalidParameterException(OMRSErrorCode.BAD_TYPEDEF_IDS_FOR_DELETE.getMessageDefinition(typeDefName,
                                                                                                              typeDefGUID,
                                                                                                              methodName,
                                                                                                              instance.getGUID(),
                                                                                                              sourceName),
                                                this.getClass().getName(),
                                                methodName,
                                                "type");
        }
    }


    /**
     * Validate the content of a new AttributeTypeDef.
     *
     * @param sourceName source of the request (used for logging)
     * @param parameterName name of the parameter that passed the attributeTypeDef.
     * @param attributeTypeDef unique identifier for a type or an instance passed on the request
     * @param methodName method receiving the call
     * @throws InvalidParameterException no attributeTypeDef provided
     * @throws InvalidTypeDefException bad attributeTypeDef provided
     */
    @Override
    public  void validateAttributeTypeDef(String           sourceName,
                                          String           parameterName,
                                          AttributeTypeDef attributeTypeDef,
                                          String           methodName) throws InvalidParameterException,
                                                                              InvalidTypeDefException
    {
        if (attributeTypeDef == null)
        {
            throw new InvalidParameterException(OMRSErrorCode.NULL_ATTRIBUTE_TYPEDEF.getMessageDefinition(parameterName,
                                                                                                          methodName,
                                                                                                          sourceName),
                                                this.getClass().getName(),
                                                methodName,
                                                parameterName);
        }

        if (attributeTypeDef.getHeaderVersion() > TypeDefElementHeader.CURRENT_TYPE_DEF_HEADER_VERSION)
        {
            throw new InvalidTypeDefException(OMRSErrorCode.UNSUPPORTED_TYPE_HEADER_VERSION.getMessageDefinition(methodName,
                                                                                                                 sourceName,
                                                                                                                 attributeTypeDef.getName(),
                                                                                                                 Long.toString(attributeTypeDef.getHeaderVersion()),
                                                                                                                 Long.toString(attributeTypeDef.getHeaderVersion())),
                                              this.getClass().getName(),
                                              methodName);
        }


        validateAttributeTypeDefIds(sourceName,
                                    parameterName + ".getGUID",
                                    parameterName + ".getName",
                                    attributeTypeDef.getGUID(),
                                    attributeTypeDef.getName(),
                                    methodName);

        validateAttributeTypeDefCategory(sourceName,
                                         parameterName + ".getCategory",
                                         attributeTypeDef.getCategory(),
                                         methodName);
    }


    /**
     * Validate that type's name is not null.
     *
     * @param sourceName source of the request (used for logging)
     * @param parameterName name of the parameter that passed the name.
     * @param gallery typeDef gallery
     * @param methodName method receiving the call
     * @throws InvalidParameterException no name provided
     */
    @Override
    public  void validateTypeDefGallery(String         sourceName,
                                        String         parameterName,
                                        TypeDefGallery gallery,
                                        String         methodName) throws InvalidParameterException
    {
        if (gallery == null)
        {
            throw new InvalidParameterException(OMRSErrorCode.NULL_TYPEDEF_GALLERY.getMessageDefinition(parameterName,
                                                                                                        methodName,
                                                                                                        sourceName),
                                                this.getClass().getName(),
                                                methodName,
                                                parameterName);
        }
    }


    /**
     * Validate that the type's name is not null.
     *
     * @param sourceName source of the request (used for logging)
     * @param standard name of the standard, null means any.
     * @param organization name of the organization, null means any.
     * @param identifier identifier of the element in the standard, null means any.
     * @param methodName method receiving the call
     * @throws InvalidParameterException no name provided
     */
    @Override
    public  void validateExternalId(String sourceName,
                                    String standard,
                                    String organization,
                                    String identifier,
                                    String methodName) throws InvalidParameterException
    {
        if ((standard == null) && (organization == null) && (identifier == null))
        {
            throw new InvalidParameterException(OMRSErrorCode.NO_EXTERNAL_ID.getMessageDefinition(methodName,
                                                                                                  sourceName),
                                                this.getClass().getName(),
                                                methodName,
                                                "externalId");
        }
    }


    /**
     * Validate that an entity's identifier is not null.
     *
     * @param sourceName source of the request (used for logging)
     * @param guidParameterName name of the parameter that passed the guid.
     * @param guid unique identifier for a type or an instance passed on the request
     * @param methodName method receiving the call
     * @throws InvalidParameterException no guid provided
     */
    @Override
    public  void validateGUID(String sourceName,
                              String guidParameterName,
                              String guid,
                              String methodName) throws InvalidParameterException
    {
        if (guid == null)
        {
            throw new InvalidParameterException(OMRSErrorCode.NO_GUID.getMessageDefinition(guidParameterName,
                                                                                           methodName,
                                                                                           sourceName),
                                                this.getClass().getName(),
                                                methodName,
                                                guidParameterName);
        }
    }


    /**
     * Validate that a home metadata collection identifier is not null.
     *
     * @param sourceName source of the request (used for logging)
     * @param guidParameterName name of the parameter that passed the guid.
     * @param guid unique identifier for a type or an instance passed on the request
     * @param methodName method receiving the call
     * @throws InvalidParameterException no guid provided
     */
    @Override
    public  void validateHomeMetadataGUID(String sourceName,
                                          String guidParameterName,
                                          String guid,
                                          String methodName) throws InvalidParameterException
    {
        if (guid == null)
        {
            throw new InvalidParameterException(OMRSErrorCode.NULL_HOME_METADATA_COLLECTION_ID.getMessageDefinition(guidParameterName,
                                                                                                                    methodName,
                                                                                                                    sourceName),
                                                this.getClass().getName(),
                                                methodName,
                                                guidParameterName);
        }
    }


    /**
     * Validate that a home metadata collection identifier in a classification is not null.
     *
     * @param sourceName source of the request (used for logging)
     * @param classification classification to test.
     * @param methodName method receiving the call
     * @throws RepositoryErrorException no guid provided
     */
    @Override
    public  void validateHomeMetadataGUID(String           sourceName,
                                          Classification   classification,
                                          String           methodName) throws RepositoryErrorException
    {
        final String  thisMethodName = "validateHomeMetadataGUID";

        if (classification == null)
        {
            this.throwValidatorLogicError(sourceName, methodName, thisMethodName);
        }

        if (classification.getMetadataCollectionId() == null)
        {
            throw new RepositoryErrorException(OMRSErrorCode.NULL_INSTANCE_METADATA_COLLECTION_ID.getMessageDefinition(classification.getName(),
                                                                                                                       sourceName,
                                                                                                                       methodName,
                                                                                                                       classification.toString()),
                                               this.getClass().getName(),
                                               methodName);
        }
    }



    /**
     * Validate that a home metadata collection identifier in an instance is not null.
     *
     * @param sourceName source of the request (used for logging)
     * @param instance instance to test.
     * @param methodName method receiving the call
     * @throws RepositoryErrorException no guid provided
     */
    @Override
    public  void validateHomeMetadataGUID(String           sourceName,
                                          InstanceHeader   instance,
                                          String           methodName) throws RepositoryErrorException
    {
        final String  thisMethodName = "validateHomeMetadataGUID";

        if (instance == null)
        {
            this.throwValidatorLogicError(sourceName, methodName, thisMethodName);
        }

        if (instance.getMetadataCollectionId() == null)
        {
            throw new RepositoryErrorException(OMRSErrorCode.NULL_INSTANCE_METADATA_COLLECTION_ID.getMessageDefinition(instance.getGUID(),
                                                                                                                       sourceName,
                                                                                                                       methodName,
                                                                                                                       instance.toString()),
                                               this.getClass().getName(),
                                               methodName);
        }
    }


    /**
     * Validate that the asOfTime parameter is not for the future.
     *
     * @param sourceName source of the request (used for logging)
     * @param parameterName name of the parameter that passed the guid.
     * @param asOfTime unique name for a classification type
     * @param methodName method receiving the call
     * @throws InvalidParameterException asOfTime is for the future
     */
    @Override
    public  void validateAsOfTime(String sourceName,
                                  String parameterName,
                                  Date   asOfTime,
                                  String methodName) throws InvalidParameterException
    {
        if (asOfTime != null)
        {
            Date   now = new Date();

            if (asOfTime.after(now))
            {
                throw new InvalidParameterException(OMRSErrorCode.REPOSITORY_NOT_CRYSTAL_BALL.getMessageDefinition(asOfTime.toString(),
                                                                                                                   parameterName,
                                                                                                                   methodName,
                                                                                                                   sourceName),
                                                    this.getClass().getName(),
                                                    methodName,
                                                    parameterName);
            }
        }
    }


    /**
     * Validate that the asOfTime parameter is not null or for the future.
     *
     * @param sourceName source of the request (used for logging)
     * @param parameterName name of the parameter that passed the guid.
     * @param asOfTime unique name for a classification type
     * @param methodName method receiving the call
     * @throws InvalidParameterException asOfTime is for the future
     */
    @Override
    public  void validateAsOfTimeNotNull(String sourceName,
                                         String parameterName,
                                         Date   asOfTime,
                                         String methodName) throws InvalidParameterException
    {
        if (asOfTime == null)
        {
            throw new InvalidParameterException(OMRSErrorCode.NULL_AS_OF_TIME.getMessageDefinition(methodName,
                                                                                                   parameterName,
                                                                                                   sourceName),
                                                this.getClass().getName(),
                                                methodName,
                                                parameterName);
        }
        else
        {
            this.validateAsOfTime(sourceName, parameterName, asOfTime, methodName);
        }
    }


    /**
     * Validate that the time parameters are not inverted ('from' later than 'to').
     *
     * @param sourceName source of the request (used for logging)
     * @param parameterName name of the parameter that passed the guid.
     * @param fromTime the earliest point in time from which to retrieve historical versions of the instance (inclusive)
     * @param toTime the latest point in time from which to retrieve historical versions of the instance (exclusive)
     * @param methodName method receiving the call
     * @throws InvalidParameterException 'fromTime' is later than 'toTime', or either is some point in the future
     */
    @Override
    public  void validateDateRange(String sourceName,
                                   String parameterName,
                                   Date   fromTime,
                                   Date   toTime,
                                   String methodName) throws InvalidParameterException
    {
        // If either (or both) are null, then this is valid: simply extend forwards or backwards (or both) as far as we can
        if (fromTime != null && toTime != null)
        {
            if (fromTime.compareTo(toTime) > 0)
            {
                throw new InvalidParameterException(OMRSErrorCode.INVALID_TIME_RANGE.getMessageDefinition(methodName,
                                                                                                          fromTime.toString(),
                                                                                                          toTime.toString()),
                                                    this.getClass().getName(),
                                                    methodName,
                                                    parameterName);
            }
        }
        // Regardless, validate any non-null date is not in the future
        if (fromTime != null)
        {
            this.validateAsOfTime(sourceName, "fromTime", fromTime, methodName);
        }
        if (toTime != null)
        {
            this.validateAsOfTime(sourceName, "toTime", toTime, methodName);
        }
    }


    /**
     * Validate that a page size parameter is not negative.
     *
     * @param sourceName source of the request (used for logging)
     * @param parameterName name of the parameter that passed the guid.
     * @param pageSize number of elements to return on a request
     * @param methodName method receiving the call
     * @throws PagingErrorException pageSize is negative
     */
    @Override
    public  void validatePageSize(String sourceName,
                                  String parameterName,
                                  int    pageSize,
                                  String methodName) throws PagingErrorException
    {
        if (pageSize < 0)
        {
            throw new PagingErrorException(OMRSErrorCode.NEGATIVE_PAGE_SIZE.getMessageDefinition(Integer.toString(pageSize),
                                                                                                 parameterName,
                                                                                                 methodName,
                                                                                                 sourceName),
                                           this.getClass().getName(),
                                           methodName);

        }
    }


    /**
     * Validate that a classification name is not null.
     *
     * @param sourceName source of the request (used for logging)
     * @param parameterName name of the parameter that passed the guid.
     * @param classificationName unique name for a classification type
     * @param methodName method receiving the call
     * @return type definition for the classification
     * @throws InvalidParameterException  classification name is null or invalid
     */
    @Override
    public  TypeDef validateClassificationName(String sourceName,
                                               String parameterName,
                                               String classificationName,
                                               String methodName) throws InvalidParameterException
    {
        if (classificationName == null)
        {
            throw new InvalidParameterException(OMRSErrorCode.NULL_CLASSIFICATION_NAME.getMessageDefinition(parameterName,
                                                                                                            methodName,
                                                                                                            sourceName),
                                                this.getClass().getName(),
                                                methodName,
                                                parameterName);
        }

        TypeDef typeDef = repositoryContentManager.getTypeDefByName(classificationName);

        if (typeDef == null)
        {
           throw new InvalidParameterException(OMRSErrorCode.UNKNOWN_CLASSIFICATION.getMessageDefinition(classificationName,
                                                                                                          sourceName),
                                                this.getClass().getName(),
                                                methodName,
                                                parameterName);
        }

        return typeDef;
    }


    /**
     * Validate that a classification is valid for the entity.
     *
     * @param sourceName source of the request (used for logging)
     * @param classificationName unique name for a classification type
     * @param propertiesParameterName name of the parameter that passed the properties.
     * @param classificationProperties properties to test
     * @param methodName method receiving the call
     * @throws PropertyErrorException classification name is null
     */
    @Override
    public  void validateClassificationProperties(String             sourceName,
                                                  String             classificationName,
                                                  String             propertiesParameterName,
                                                  InstanceProperties classificationProperties,
                                                  String             methodName) throws PropertyErrorException
    {
        validateRepositoryContentManager(methodName);

        TypeDef   classificationTypeDef = repositoryContentManager.getTypeDefByName(classificationName);

        if (classificationTypeDef != null)
        {
            validatePropertiesForType(sourceName, propertiesParameterName, classificationTypeDef, classificationProperties, methodName);
        }
        else
        {
            /*
             * Logic error as the type should be valid
             */
            final String   thisMethodName = "validateClassificationProperties";

            throwValidatorLogicError(sourceName, methodName, thisMethodName);
        }
    }


    /**
     * Validate that a classification is valid for the entity.
     *
     * @param sourceName source of the request (used for logging)
     * @param classificationParameterName name of the parameter that passed the guid.
     * @param classificationName unique name for a classification type
     * @param entityTypeName name of entity type
     * @param methodName method receiving the call
     * @throws InvalidParameterException classification name is null
     * @throws ClassificationErrorException the classification is invalid for this entity
     */
    @Override
    public  void validateClassification(String             sourceName,
                                        String             classificationParameterName,
                                        String             classificationName,
                                        String             entityTypeName,
                                        String             methodName) throws InvalidParameterException,
                                                                              ClassificationErrorException
    {
        validateRepositoryContentManager(methodName);

        this.validateClassificationName(sourceName, classificationParameterName, classificationName, methodName);

        if (entityTypeName != null)
        {
            if (!repositoryContentManager.isValidClassificationForEntity(sourceName,
                                                                         classificationName,
                                                                         entityTypeName,
                                                                         methodName))
            {
                throw new ClassificationErrorException(OMRSErrorCode.INVALID_CLASSIFICATION_FOR_ENTITY.getMessageDefinition(sourceName,
                                                                                                                            classificationName,
                                                                                                                            entityTypeName),
                                                       this.getClass().getName(),
                                                       methodName);
            }
        }
    }


    /**
     * Validate that a classification is valid for the entity.
     *
     * @param sourceName source of the request (used for logging)
     * @param parameterName name of the parameter that passed the guid.
     * @param classifications list of classifications
     * @param entityTypeName name of entity type
     * @param methodName method receiving the call
     * @throws InvalidParameterException classification name is null
     * @throws ClassificationErrorException the classification is invalid for this entity
     * @throws PropertyErrorException the classification's properties are invalid for its type
     * @throws TypeErrorException the classification's type is invalid
     */
    @Override
    public  void validateClassificationList(String               sourceName,
                                            String               parameterName,
                                            List<Classification> classifications,
                                            String               entityTypeName,
                                            String               methodName) throws InvalidParameterException,
                                                                                    ClassificationErrorException,
                                                                                    PropertyErrorException,
                                                                                    TypeErrorException
    {
        validateRepositoryContentManager(methodName);

        if (classifications != null)
        {
            for (Classification classification : classifications)
            {
                if (classification != null)
                {

                    this.validateClassification(sourceName,
                                                parameterName,
                                                classification.getName(),
                                                entityTypeName,
                                                methodName);


                    this.validatePropertiesForType(sourceName,
                                                   parameterName,
                                                   repositoryContentManager.getTypeDefByName(classification.getName()),
                                                   classification.getProperties(),
                                                   methodName);
                }
                else
                {
                    throw new InvalidParameterException(OMRSErrorCode.NULL_CLASSIFICATION_NAME.getMessageDefinition(parameterName,
                                                                                                                    methodName,
                                                                                                                    sourceName),
                                                        this.getClass().getName(),
                                                        methodName,
                                                        parameterName);
                }
            }
        }
    }


    /**
     * Validate that a TypeDef match criteria set of properties is not null.
     *
     * @param sourceName source of the request (used for logging)
     * @param parameterName name of the parameter that passed the match criteria.
     * @param matchCriteria match criteria properties
     * @param methodName method receiving the call
     * @throws InvalidParameterException no guid provided
     */
    @Override
    public  void validateMatchCriteria(String            sourceName,
                                       String            parameterName,
                                       TypeDefProperties matchCriteria,
                                       String            methodName) throws InvalidParameterException
    {
        if (matchCriteria == null)
        {
            throw new InvalidParameterException(OMRSErrorCode.NO_MATCH_CRITERIA.getMessageDefinition(parameterName,
                                                                                                     methodName,
                                                                                                     sourceName),
                                                this.getClass().getName(),
                                                methodName,
                                                parameterName);
        }
    }


    /**
     * Validate that a metadata instance match criteria and set of properties are either both null or
     * both not null.
     *
     * @param sourceName source of the request (used for logging)
     * @param matchCriteriaParameterName name of the parameter that passed the match criteria.
     * @param matchPropertiesParameterName name of the parameter that passed the match criteria.
     * @param matchCriteria match criteria enum
     * @param matchProperties match properties
     * @param methodName method receiving the call
     * @throws InvalidParameterException no guid provided
     */
    @Override
    public  void validateMatchCriteria(String             sourceName,
                                       String             matchCriteriaParameterName,
                                       String             matchPropertiesParameterName,
                                       MatchCriteria      matchCriteria,
                                       InstanceProperties matchProperties,
                                       String             methodName) throws InvalidParameterException
    {
        if ((matchCriteria == null) && (matchProperties == null))
        {
            return;
        }

        if (matchCriteria == null)
        {
            throw new InvalidParameterException(OMRSErrorCode.NO_MATCH_CRITERIA.getMessageDefinition(matchCriteriaParameterName,
                                                                                                     methodName,
                                                                                                     sourceName),
                                                this.getClass().getName(),
                                                methodName,
                                                matchCriteriaParameterName);
        }

        if (matchProperties == null)
        {
            throw new InvalidParameterException(OMRSErrorCode.NO_MATCH_CRITERIA.getMessageDefinition(matchPropertiesParameterName,
                                                                                                     methodName,
                                                                                                     sourceName),
                                                this.getClass().getName(),
                                                methodName,
                                                matchPropertiesParameterName);
        }
    }


    /**
     * Validate that a search criteria  is not null.
     *
     * @param sourceName source of the request (used for logging)
     * @param parameterName name of the parameter that passed the search criteria.
     * @param searchCriteria match criteria properties
     * @param methodName method receiving the call
     * @throws InvalidParameterException no guid provided
     */
    @Override
    public  void validateSearchCriteria(String sourceName,
                                        String parameterName,
                                        String searchCriteria,
                                        String methodName) throws InvalidParameterException
    {
        if ((searchCriteria == null) || (searchCriteria.isEmpty()))
        {
            throw new InvalidParameterException(OMRSErrorCode.NO_SEARCH_CRITERIA.getMessageDefinition(parameterName,
                                                                                                      methodName,
                                                                                                      sourceName),
                                                this.getClass().getName(),
                                                methodName,
                                                parameterName);
        }
    }


    /**
     * Validate the property-based search conditions.
     *
     * @param sourceName  source of the request (used for logging)
     * @param parameterName  name of the parameter that passed the property-based conditions
     * @param matchProperties  property-based conditions
     * @param methodName  method receiving the call
     * @throws InvalidParameterException  property-based conditions are invalid
     */
    @Override
    public void validateSearchProperties(String           sourceName,
                                         String           parameterName,
                                         SearchProperties matchProperties,
                                         String           methodName) throws InvalidParameterException
    {
        if (matchProperties == null)
        {
            return;
        }
        for (PropertyCondition condition : matchProperties.getConditions())
        {
            SearchProperties nestedConditions = condition.getNestedConditions();
            String propertyName = condition.getProperty();
            PropertyComparisonOperator operator = condition.getOperator();
            InstancePropertyValue value = condition.getValue();
            if (nestedConditions == null)
            {
                // If there are no nested conditions, there must be at least property and operator
                if (propertyName == null || operator == null)
                {
                    throw new InvalidParameterException(OMRSErrorCode.INVALID_PROPERTY_SEARCH.getMessageDefinition(),
                                                        this.getClass().getName(),
                                                        methodName,
                                                        parameterName);
                }
                else if (value == null)
                {
                    // ... and if the operator is not a null-oriented operator, there must also be a value
                    if (!operator.equals(PropertyComparisonOperator.IS_NULL) && !operator.equals(PropertyComparisonOperator.NOT_NULL))
                    {
                        throw new InvalidParameterException(OMRSErrorCode.INVALID_PROPERTY_SEARCH.getMessageDefinition(),
                                                            this.getClass().getName(),
                                                            methodName,
                                                            parameterName);
                    }
                }
                else
                {
                    // If these are all present, ensure that the types are as expected:
                    switch(operator)
                    {
                        case IN:
                            // For the IN operator, only an ArrayTypePropertyValue is allowed
                            if (!(value instanceof ArrayPropertyValue))
                            {
                                throw new InvalidParameterException(OMRSErrorCode.INVALID_LIST_CONDITION.getMessageDefinition(),
                                                                    this.getClass().getName(),
                                                                    methodName,
                                                                    parameterName);
                            }
                            break;
                        case LIKE:
                            // For the LIKE operator, only a PrimitiveTypePropertyValue of type string is allowed
                            if (value instanceof PrimitivePropertyValue ppv)
                            {
                                if (!ppv.getPrimitiveDefCategory().equals(OM_PRIMITIVE_TYPE_STRING))
                                {
                                    throw new InvalidParameterException(OMRSErrorCode.INVALID_LIKE_CONDITION.getMessageDefinition(),
                                                                        this.getClass().getName(),
                                                                        methodName,
                                                                        parameterName);
                                }
                            }
                            else
                            {
                                throw new InvalidParameterException(OMRSErrorCode.INVALID_LIKE_CONDITION.getMessageDefinition(),
                                                                    this.getClass().getName(),
                                                                    methodName,
                                                                    parameterName);
                            }
                            break;
                        case LT:
                        case LTE:
                        case GT:
                        case GTE:
                            // For the <, <=, >=, > operators, only numeric or date types are allowed
                            if (value instanceof PrimitivePropertyValue ppv)
                            {
                                PrimitiveDefCategory pdc = ppv.getPrimitiveDefCategory();
                                if (! (pdc.equals(OM_PRIMITIVE_TYPE_DATE)
                                        || pdc.equals(OM_PRIMITIVE_TYPE_SHORT)
                                        || pdc.equals(OM_PRIMITIVE_TYPE_INT)
                                        || pdc.equals(OM_PRIMITIVE_TYPE_LONG)
                                        || pdc.equals(OM_PRIMITIVE_TYPE_FLOAT)
                                        || pdc.equals(OM_PRIMITIVE_TYPE_DOUBLE)
                                        || pdc.equals(OM_PRIMITIVE_TYPE_BIGINTEGER)
                                        || pdc.equals(OM_PRIMITIVE_TYPE_BIGDECIMAL)))
                                {
                                    throw new InvalidParameterException(OMRSErrorCode.INVALID_NUMERIC_CONDITION.getMessageDefinition(operator.getName()),
                                                                        this.getClass().getName(),
                                                                        methodName,
                                                                        parameterName);
                                }
                            }
                            else
                            {
                                throw new InvalidParameterException(OMRSErrorCode.INVALID_NUMERIC_CONDITION.getMessageDefinition(operator.getName()),
                                                                    this.getClass().getName(),
                                                                    methodName,
                                                                    parameterName);
                            }
                            break;
                    }
                }
            }
            else
            {
                // If nestedConditions is present, there cannot be any property, operator or value
                if (propertyName != null || operator != null || value != null)
                {
                    throw new InvalidParameterException(OMRSErrorCode.INVALID_PROPERTY_SEARCH.getMessageDefinition(),
                                                        this.getClass().getName(),
                                                        methodName,
                                                        parameterName);
                }
                // Recurse on the nested properties
                validateSearchProperties(sourceName, parameterName, nestedConditions, methodName);
            }
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void validateSearchClassifications(String sourceName,
                                              String parameterName,
                                              SearchClassifications matchClassifications,
                                              String methodName) throws InvalidParameterException
    {
        if (matchClassifications == null)
        {
            return;
        }
        for (ClassificationCondition condition : matchClassifications.getConditions())
        {
            String classificationName = condition.getName();
            if (classificationName == null || classificationName.isBlank())
            {
                throw new InvalidParameterException(OMRSErrorCode.INVALID_CLASSIFICATION_SEARCH.getMessageDefinition(),
                                                    this.getClass().getName(),
                                                    methodName,
                                                    parameterName);
            }
            validateSearchProperties(sourceName, parameterName, condition.getMatchProperties(), methodName);
        }
    }


    /**
     * Validate that the properties for a metadata instance match its TypeDef.
     *
     * @param sourceName source of the request (used for logging)
     * @param parameterName name of the "properties" parameter.
     * @param typeDef type information to validate against.
     * @param properties proposed properties for instance.
     * @param methodName method receiving the call.
     * @throws PropertyErrorException invalid property
     */
    @Override
    public  void validatePropertiesForType(String             sourceName,
                                           String             parameterName,
                                           TypeDef            typeDef,
                                           InstanceProperties properties,
                                           String             methodName) throws PropertyErrorException
    {
        final String   thisMethodName = "validatePropertiesForType";

        if (properties == null)
        {
            /*
             * No properties to evaluate so return
             */
            return;
        }

        if (typeDef == null)
        {
            /*
             * Logic error as the type should be valid
             */
            throwValidatorLogicError(sourceName, methodName, thisMethodName);
        }

        String  typeDefCategoryName = null;
        String  typeDefName         = typeDef.getName();

        if (typeDef.getCategory() != null)
        {
            typeDefCategoryName = typeDef.getCategory().getName();
        }

        List<TypeDefAttribute> typeDefAttributes = repositoryContentManager.getAllPropertiesForTypeDef(sourceName,
                                                                                                       typeDef,
                                                                                                       methodName);

        if (typeDefAttributes == null)
        {
            /*
             * Error is thrown because properties is not null so properties have been provided for this instance.
             */
            throw new PropertyErrorException(OMRSErrorCode.NO_PROPERTIES_FOR_TYPE.getMessageDefinition(typeDefCategoryName,
                                                                                                       typeDefName,
                                                                                                       sourceName),
                                             this.getClass().getName(),
                                             methodName);
        }

        /*
         * Need to step through each of the proposed properties and validate that the name and value are
         * present, and they match the typeDef
         */
        Iterator<?>    propertyList = properties.getPropertyNames();

        while (propertyList.hasNext())
        {
            String   propertyName = propertyList.next().toString();

            if (propertyName == null)
            {
                throw new PropertyErrorException(OMRSErrorCode.NULL_PROPERTY_NAME_FOR_INSTANCE.getMessageDefinition(parameterName,
                                                                                                                    methodName,
                                                                                                                    sourceName),
                                                 this.getClass().getName(),
                                                 methodName);
            }

            AttributeTypeDefCategory  propertyDefinitionType = null;
            AttributeTypeDef          attributeTypeDef = null;
            boolean                   recognizedProperty = false;

            for (TypeDefAttribute typeDefAttribute : typeDefAttributes)
            {
                if (typeDefAttribute != null)
                {
                    if (propertyName.equals(typeDefAttribute.getAttributeName()))
                    {
                        recognizedProperty = true;

                        attributeTypeDef = typeDefAttribute.getAttributeType();
                        if (attributeTypeDef == null)
                        {
                            propertyDefinitionType = AttributeTypeDefCategory.UNKNOWN_DEF;
                        }
                        else
                        {
                            propertyDefinitionType = attributeTypeDef.getCategory();
                        }
                    }
                }
            }

            if (! recognizedProperty)
            {
                throw new PropertyErrorException(OMRSErrorCode.BAD_PROPERTY_FOR_TYPE.getMessageDefinition(propertyName,
                                                                                                          typeDefCategoryName,
                                                                                                          typeDefName,
                                                                                                          sourceName),
                                                 this.getClass().getName(),
                                                 methodName);
            }

            InstancePropertyValue propertyValue = properties.getPropertyValue(propertyName);

            if (propertyValue == null)
            {
                throw new PropertyErrorException(OMRSErrorCode.NULL_PROPERTY_VALUE_FOR_INSTANCE.getMessageDefinition(parameterName,
                                                                                                                     methodName,
                                                                                                                     sourceName),
                                                 this.getClass().getName(),
                                                 methodName);
            }

            InstancePropertyCategory propertyType = propertyValue.getInstancePropertyCategory();

            if (propertyType == null)
            {
                throw new PropertyErrorException(OMRSErrorCode.NULL_PROPERTY_TYPE_FOR_INSTANCE.getMessageDefinition(parameterName,
                                                                                                                    methodName,
                                                                                                                    sourceName),
                                                 this.getClass().getName(),
                                                 methodName);
            }

            boolean  validPropertyType = false;
            String   actualPropertyTypeName = propertyType.getName();
            String   validPropertyTypeName  = propertyDefinitionType.getName();

            switch (propertyType)
            {
                case PRIMITIVE:
                    if (propertyDefinitionType == AttributeTypeDefCategory.PRIMITIVE)
                    {
                        /*
                         * Ensure that primitive definition category is a perfect match...
                         */
                        PrimitivePropertyValue primPropertyValue    = (PrimitivePropertyValue)propertyValue;
                        PrimitiveDefCategory   primPropertyCategory = primPropertyValue.getPrimitiveDefCategory();
                        PrimitiveDef           expectedAttributeDef = (PrimitiveDef) attributeTypeDef;
                        PrimitiveDefCategory   expectedAttributeDefCategory = expectedAttributeDef.getPrimitiveDefCategory();
                        if (primPropertyCategory == expectedAttributeDefCategory)
                        {
                            validPropertyType = true;
                        }
                        else
                        {
                            actualPropertyTypeName = primPropertyCategory.getName();
                            validPropertyTypeName  = expectedAttributeDefCategory.getName();
                        }
                    }
                    else if (propertyDefinitionType == AttributeTypeDefCategory.UNKNOWN_DEF)
                    {
                        /*
                         * This property definition type may have been adopted above due to the
                         * attributeTypeDef being null. Permit primitive definition category to
                         * be a sloppy match...
                         */
                        validPropertyType = true;
                    }
                    break;

                case ENUM:
                    if (propertyDefinitionType == AttributeTypeDefCategory.ENUM_DEF)
                    {
                        validPropertyType = true;
                    }
                    break;

                case MAP:

                case STRUCT:

                case ARRAY:
                    if (propertyDefinitionType == AttributeTypeDefCategory.COLLECTION)
                    {
                        validPropertyType = true;
                    }
                    break;
            }

            if (! validPropertyType)
            {
                throw new PropertyErrorException(OMRSErrorCode.BAD_PROPERTY_TYPE.getMessageDefinition(propertyName,
                                                                                                      actualPropertyTypeName,
                                                                                                      typeDefCategoryName,
                                                                                                      typeDefName,
                                                                                                      validPropertyTypeName,
                                                                                                      sourceName),
                                                 this.getClass().getName(),
                                                 methodName);
            }
        }
    }


    /**
     * Validate that the properties for a metadata instance match its TypeDef
     *
     * @param sourceName source of the request (used for logging)
     * @param parameterName name of the "properties" parameter.
     * @param typeDefSummary type information to validate against.
     * @param properties proposed properties
     * @param methodName method receiving the call
     * @throws TypeErrorException no typeDef provided
     * @throws PropertyErrorException invalid property
     */
    @Override
    public  void validatePropertiesForType(String             sourceName,
                                           String             parameterName,
                                           TypeDefSummary     typeDefSummary,
                                           InstanceProperties properties,
                                           String             methodName) throws PropertyErrorException,
                                                                                 TypeErrorException
    {
        validateRepositoryContentManager(methodName);

        if (typeDefSummary == null)
        {
            /*
             * Logic error as the type should be valid
             */
            final String   thisMethodName = "validatePropertiesForType";

            throwValidatorLogicError(sourceName, methodName, thisMethodName);
        }

        TypeDef typeDef = repositoryContentManager.getTypeDef(sourceName,
                                                              parameterName,
                                                              parameterName,
                                                              typeDefSummary.getGUID(),
                                                              typeDefSummary.getName(),
                                                              methodName);

        this.validatePropertiesForType(sourceName, parameterName, typeDef, properties, methodName);
    }


    /**
     * Validate that the properties for a metadata instance match its TypeDef
     *
     * @param sourceName source of the request (used for logging)
     * @param parameterName name of the "properties" parameter.
     * @param typeDef type information to validate against.
     * @param properties proposed properties
     * @param methodName method receiving the call
     * @throws PropertyErrorException invalid property
     */
    @Override
    public  void validateNewPropertiesForType(String             sourceName,
                                              String             parameterName,
                                              TypeDef            typeDef,
                                              InstanceProperties properties,
                                              String             methodName) throws PropertyErrorException
    {
        if (properties != null)
        {
            this.validatePropertiesForType(sourceName, parameterName, typeDef, properties, methodName);
        }
        else
        {
            throw new PropertyErrorException(OMRSErrorCode.NO_NEW_PROPERTIES.getMessageDefinition(parameterName,
                                                                                                  methodName,
                                                                                                  sourceName),
                                              this.getClass().getName(),
                                              methodName);
        }
    }


    /**
     * Verify whether the instance passed to this method is of the type indicated by the type guid.
     * A null type guid matches all instances (ie result is true).  A null instance returns false.
     *
     * @param sourceName name of the caller.
     * @param instanceTypeGUID unique identifier of the type (or null).
     * @param instance instance to test.
     * @return boolean
     */
    @Override
    public boolean verifyInstanceType(String           sourceName,
                                      String           instanceTypeGUID,
                                      InstanceHeader   instance)
    {
        if (instance != null)
        {
            if (instanceTypeGUID == null)
            {
                /*
                 * A null instance type matches all instances
                 */
                return true;
            }
            else
            {
                InstanceType entityType = instance.getType();

                if (entityType != null)
                {
                    return (repositoryContentManager.isTypeOfByGUID(sourceName,
                                                                entityType.getTypeDefGUID(),
                                                                entityType.getTypeDefName(),
                                                                instanceTypeGUID));
                }
            }
        }

        return false;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean verifyInstanceType(String         sourceName,
                                      String         instanceTypeGUID,
                                      List<String>   subtypeGUIDs,
                                      InstanceHeader instance)
    {
        boolean soFar = verifyInstanceType(sourceName, instanceTypeGUID, instance);
        if (soFar)
        {
            if (subtypeGUIDs != null)
            {
                boolean matchesASubtype = false;
                for (String subtype : subtypeGUIDs)
                {
                    matchesASubtype = verifyInstanceType(sourceName, subtype, instance);
                    if (matchesASubtype)
                    {
                        // Short-circuit out if we found a subtype match
                        break;
                    }
                }
                soFar = matchesASubtype;
            }
        }
        return soFar;
    }


    /**
     * Verify that an entity has been successfully retrieved from the repository and has valid contents.
     *
     * @param sourceName source of the request (used for logging)
     * @param guid unique identifier used to retrieve the entity
     * @param entity the retrieved entity (or null)
     * @param methodName method receiving the call
     * @throws EntityNotKnownException No entity found
     * @throws RepositoryErrorException logic error in the repository corrupted instance
     */
    @Override
    public void validateEntityFromStore(String           sourceName,
                                        String           guid,
                                        EntitySummary    entity,
                                        String           methodName) throws RepositoryErrorException,
                                                                            EntityNotKnownException
    {
        if (entity == null)
        {
            throw new EntityNotKnownException(OMRSErrorCode.ENTITY_NOT_KNOWN.getMessageDefinition(guid,
                                                                                                  methodName,
                                                                                                  sourceName),
                                              this.getClass().getName(),
                                              methodName);
        }

        if (! validInstance(sourceName, entity, methodName, true))
        {
            throw new RepositoryErrorException(OMRSErrorCode.INVALID_ENTITY_FROM_STORE.getMessageDefinition(guid,
                                                                                                            sourceName,
                                                                                                            methodName,
                                                                                                            entity.toString()),
                                               this.getClass().getName(),
                                               methodName);
        }
    }


    /**
     * Verify that an entity has been successfully retrieved from the repository and has valid contents.
     *
     * @param sourceName source of the request (used for logging)
     * @param guid unique identifier used to retrieve the entity
     * @param entity the retrieved entity (or null)
     * @param methodName method receiving the call
     * @throws EntityNotKnownException No entity found
     * @throws RepositoryErrorException logic error in the repository corrupted instance
     */
    @Override
    public void validateEntityFromStore(String           sourceName,
                                        String           guid,
                                        EntityDetail     entity,
                                        String           methodName) throws RepositoryErrorException,
                                                                            EntityNotKnownException
    {
        if (entity == null)
        {
            throw new EntityNotKnownException(OMRSErrorCode.ENTITY_NOT_KNOWN.getMessageDefinition(guid,
                                                                                                  methodName,
                                                                                                  sourceName),
                                              this.getClass().getName(),
                                              methodName);
        }

        if (! validEntity(sourceName, entity))
        {
            throw new RepositoryErrorException(OMRSErrorCode.INVALID_ENTITY_FROM_STORE.getMessageDefinition(guid,
                                                                                                            sourceName,
                                                                                                            methodName,
                                                                                                            entity.toString()),
                                               this.getClass().getName(),
                                               methodName);
        }
    }


    /**
     * Verify that a relationship has been successfully retrieved from the repository and has valid contents.
     *
     * @param sourceName source of the request (used for logging)
     * @param guid unique identifier used to retrieve the entity
     * @param relationship the retrieved relationship (or null)
     * @param methodName method receiving the call
     * @throws RelationshipNotKnownException No relationship found
     * @throws RepositoryErrorException logic error in the repository corrupted instance
     */
    @Override
    public void validateRelationshipFromStore(String       sourceName,
                                              String       guid,
                                              Relationship relationship,
                                              String       methodName) throws RepositoryErrorException,
                                                                              RelationshipNotKnownException
    {
        if (relationship == null)
        {
            throw new RelationshipNotKnownException(OMRSErrorCode.RELATIONSHIP_NOT_FOUND.getMessageDefinition(guid,
                                                                                                              methodName,
                                                                                                              sourceName),
                                                    this.getClass().getName(),
                                                    methodName);
        }


        if (! validRelationship(sourceName, relationship))
        {
            throw new RepositoryErrorException(OMRSErrorCode.INVALID_RELATIONSHIP_FROM_STORE.getMessageDefinition(guid,
                                                                                                                  sourceName,
                                                                                                                  methodName,
                                                                                                                  relationship.toString()),
                                               this.getClass().getName(),
                                               methodName);
        }
    }

    /**
     * Verify that the instance retrieved from the repository has a valid instance type.
     *
     * @param sourceName source of the request (used for logging)
     * @param instance the retrieved instance
     * @throws RepositoryErrorException logic error in the repository corrupted instance
     */
    @Override
    public void validateInstanceType(String           sourceName,
                                     InstanceHeader   instance) throws RepositoryErrorException
    {
        final String  methodName = "validateInstanceType";

        if (instance != null)
        {
            InstanceType instanceType = instance.getType();

            if (instanceType != null)
            {
                if (this.isKnownType(sourceName, instanceType.getTypeDefGUID(), instanceType.getTypeDefName()))
                {
                    return;
                }
                else
                {
                    throw new RepositoryErrorException(OMRSErrorCode.INACTIVE_INSTANCE_TYPE.getMessageDefinition(methodName,
                                                                                                                 sourceName,
                                                                                                                 instance.getGUID(),
                                                                                                                 instanceType.getTypeDefName(),
                                                                                                                 instanceType.getTypeDefGUID()),
                                                       this.getClass().getName(),
                                                       methodName);
                }
            }
            else
            {
                throw new RepositoryErrorException(OMRSErrorCode.NULL_INSTANCE_TYPE.getMessageDefinition(methodName, sourceName),
                                                   this.getClass().getName(),
                                                   methodName);
            }
        }
        else
        {
            /*
             * Logic error as the instance should be valid
             */
            final String   thisMethodName = "validateInstanceType";

            throwValidatorLogicError(sourceName, methodName, thisMethodName);
        }
    }


    /**
     * Verify that the instance retrieved from the repository has a valid instance type that matches the
     * expected type.
     *
     * @param sourceName source of the request (used for logging)
     * @param instance the retrieved instance
     * @param typeGUIDParameterName name of parameter for TypeDefGUID
     * @param typeNameParameterName name of parameter for TypeDefName
     * @param expectedTypeGUID expected GUID of InstanceType
     * @param expectedTypeName expected name of InstanceType
     * @throws RepositoryErrorException logic error in the repository corrupted instance
     * @throws TypeErrorException problem with type
     * @throws InvalidParameterException invalid parameter
     */
    @Override
    public void validateInstanceType(String           sourceName,
                                     InstanceHeader   instance,
                                     String           typeGUIDParameterName,
                                     String           typeNameParameterName,
                                     String           expectedTypeGUID,
                                     String           expectedTypeName) throws RepositoryErrorException,
                                                                               TypeErrorException,
                                                                               InvalidParameterException
    {
        final String  methodName = "validateInstanceType";

        this.validateInstanceType(sourceName, instance);

        if (expectedTypeGUID == null)
        {
           throw new InvalidParameterException(OMRSErrorCode.NULL_TYPEDEF_IDENTIFIER.getMessageDefinition(typeGUIDParameterName,
                                                                                                           methodName,
                                                                                                           sourceName),
                                                this.getClass().getName(),
                                                methodName,
                                                typeGUIDParameterName);
        }

        if (expectedTypeName == null)
        {
            throw new InvalidParameterException(OMRSErrorCode.NULL_TYPEDEF_NAME.getMessageDefinition(typeNameParameterName,
                                                                                                     methodName,
                                                                                                     sourceName),
                                                this.getClass().getName(),
                                                methodName,
                                                typeNameParameterName);
        }


    }


    /**
     * Verify that the supplied instance is in one of the supplied statuses. Note that if the supplied statuses are
     * null, then only statuses that are not DELETE are considered valid.
     *
     * @param validStatuses list of statuses the instance should be in any one of them
     * @param instance instance to test
     * @return boolean result
     */
    @Override
    public boolean verifyInstanceHasRightStatus(List<InstanceStatus>      validStatuses,
                                                InstanceHeader            instance)
    {
        if (instance != null)
        {
            if (validStatuses == null)
            {
                return instance.getStatus() != InstanceStatus.DELETED;
            }
            else
            {
                return validStatuses.contains(instance.getStatus());
            }
        }

        return true;
    }


    /**
     * Validates an instance status where null is permissible.
     *
     * @param sourceName source of the request (used for logging)
     * @param instanceStatusParameterName name of the initial status parameter
     * @param instanceStatus initial status value
     * @param typeDef type of the instance
     * @param methodName method called
     * @throws StatusNotSupportedException the initial status is invalid for this type
     */
    @Override
    public void validateInstanceStatus(String         sourceName,
                                       String         instanceStatusParameterName,
                                       InstanceStatus instanceStatus,
                                       TypeDef        typeDef,
                                       String         methodName) throws StatusNotSupportedException
    {
        if (instanceStatus == InstanceStatus.DELETED)
        {
            throw new StatusNotSupportedException(OMRSErrorCode.BAD_DELETE_INSTANCE_STATUS.getMessageDefinition(instanceStatus.getName(),
                                                                                                                instanceStatusParameterName,
                                                                                                                methodName,
                                                                                                                sourceName),
                                                  this.getClass().getName(),
                                                  methodName);
        }
        else if (instanceStatus != null)
        {
            if (typeDef != null)
            {
                List<InstanceStatus>   validStatuses = typeDef.getValidInstanceStatusList();

                for (InstanceStatus validStatus : validStatuses)
                {
                    if (instanceStatus == validStatus)
                    {
                        return;
                    }
                }

                throw new StatusNotSupportedException(OMRSErrorCode.BAD_INSTANCE_STATUS.getMessageDefinition(instanceStatus.getName(),
                                                                                                             instanceStatusParameterName,
                                                                                                             methodName,
                                                                                                             sourceName,
                                                                                                             typeDef.getName()),
                                                      this.getClass().getName(),
                                                      methodName);
            }
            else
            {
                throw new StatusNotSupportedException(OMRSErrorCode.NULL_TYPEDEF.getMessageDefinition("typeDef",
                                                                                                      methodName,
                                                                                                      sourceName),
                                                      this.getClass().getName(),
                                                      methodName);
            }
        }
    }


    /**
     * Validates an instance status where null is not allowed.
     *
     * @param sourceName source of the request (used for logging)
     * @param instanceStatusParameterName name of the initial status parameter
     * @param instanceStatus initial status value
     * @param typeDef type of the instance
     * @param methodName method called
     * @throws StatusNotSupportedException the initial status is invalid for this type
     * @throws InvalidParameterException invalid parameter
     */
    @Override
    public void validateNewStatus(String         sourceName,
                                  String         instanceStatusParameterName,
                                  InstanceStatus instanceStatus,
                                  TypeDef        typeDef,
                                  String         methodName) throws StatusNotSupportedException,
                                                                    InvalidParameterException
    {
        if (instanceStatus != null)
        {
           this.validateInstanceStatus(sourceName, instanceStatusParameterName, instanceStatus, typeDef, methodName);
        }
        else
        {
            throw new InvalidParameterException(OMRSErrorCode.NULL_INSTANCE_STATUS.getMessageDefinition(instanceStatusParameterName,
                                                                                                        methodName,
                                                                                                        sourceName),
                                                this.getClass().getName(),
                                                methodName,
                                                instanceStatusParameterName);
        }
    }


    /**
     * Verify that an instance is not already deleted since the repository is processing a delete request,
     * and it does not want to look stupid.
     *
     * @param sourceName source of the request (used for logging)
     * @param instance instance about to be deleted
     * @param methodName name of method called
     * @throws InvalidParameterException the instance is already deleted
     */
    @Override
    public void validateInstanceStatusForDelete(String         sourceName,
                                                InstanceHeader instance,
                                                String         methodName) throws InvalidParameterException
    {
        if (instance != null)
        {
            if (instance.getStatus() == InstanceStatus.DELETED)
            {
                /*
                 * Instance is already deleted
                 */
                throw new InvalidParameterException(OMRSErrorCode.INSTANCE_ALREADY_DELETED.getMessageDefinition(methodName,
                                                                                                                sourceName,
                                                                                                                instance.getGUID()),
                                                    this.getClass().getName(),
                                                    methodName,
                                                    "instance");
            }
        }
        else
        {
            /*
             * Logic error as the instance should be valid
             */
            final String   thisMethodName = "validateInstanceStatusForDelete";

            throwValidatorLogicError(sourceName, methodName, thisMethodName);
        }
    }


    /**
     * Simple method used to extract the type name of an instance.  It is used in error messages and audit log messages.
     *
     * @param instanceHeader instance header.
     * @return type name or "nullType"
     */
    private String getTypeNameForMessage(InstanceHeader  instanceHeader)
    {
        String       typeName = "<nullType>";
        InstanceType type = instanceHeader.getType();

        if ((type != null) && (type.getTypeDefName() != null))
        {
            typeName = type.getTypeDefName();
        }

        return typeName;
    }


    /**
     * Verify the status of an entity to check if it has been deleted.
     *
     * @param sourceName source of the request (used for logging)
     * @param instance instance to validate
     * @param methodName name of calling method
     * @throws EntityNotKnownException the entity is in deleted status
     */
    @Override
    public void validateEntityIsNotDeleted(String         sourceName,
                                           InstanceHeader instance,
                                           String         methodName) throws EntityNotKnownException
    {
        if (instance != null)
        {
            if (instance.getStatus() == InstanceStatus.DELETED)
            {
                throw new EntityNotKnownException(OMRSErrorCode.ENTITY_SOFT_DELETED.getMessageDefinition(this.getTypeNameForMessage(instance),
                                                                                                         instance.getGUID(),
                                                                                                         methodName,
                                                                                                         sourceName),
                                                  this.getClass().getName(),
                                                  methodName);
            }
        }
        else
        {
            /*
             * Logic error as the instance should be valid
             */
            final String   thisMethodName = "validateEntityIsNotDeleted";

            throwValidatorLogicError(sourceName, methodName, thisMethodName);
        }
    }


    /**
     * Verify the status of an entity to check it has been deleted.  This method is used during a purge operation
     * to ensure delete() has been called before purge.
     *
     * @param sourceName source of the request (used for logging)
     * @param instance instance to validate
     * @param methodName name of calling method
     * @throws EntityNotDeletedException the entity is not in deleted status
     */
    @Override
    public void validateEntityIsDeleted(String         sourceName,
                                        InstanceHeader instance,
                                        String         methodName) throws EntityNotDeletedException
    {
        if (instance != null)
        {
            if (instance.getStatus() != InstanceStatus.DELETED)
            {
                throw new EntityNotDeletedException(OMRSErrorCode.INSTANCE_NOT_DELETED.getMessageDefinition(methodName,
                                                                                                            sourceName,
                                                                                                            instance.getGUID()),
                                                    this.getClass().getName(),
                                                    methodName);
            }
        }
        else
        {
            /*
             * Logic error as the instance should be valid
             */
            final String   thisMethodName = "validateEntityIsDeleted";

            throwValidatorLogicError(sourceName, methodName, thisMethodName);
        }
    }


    /**
     * Verify that an entity instance can be updated by the metadataCollection. This method is used
     * when the metadataCollection is called to update the status properties or classification of an
     * entity instance.
     *
     * @param sourceName  source of the request (used for logging)
     * @param metadataCollectionId unique identifier for the metadata collection
     * @param instance  instance to validate
     * @param methodName  name of calling method
     * @throws InvalidParameterException  the entity is in deleted status
     */
    @Override
    public void validateEntityCanBeUpdated(String         sourceName,
                                           String         metadataCollectionId,
                                           InstanceHeader instance,
                                           String         methodName) throws InvalidParameterException

    {
        /*
         * Check that it is legal to update the entity.
         * The caller can update the entity provided:
         * The entity is locally mastered
         * OR
         * The entity has instanceProvenanceType set to external and replicatedBy is set to the local metadataCollectionId.
         * Any other combination suggest that this is either a reference copy of an instance from the local cohort or a reference
         * copy of an external entity (and something else is responsible for replication).
         *
         * If not throw an InvalidParameterException.
         */

        /*
         * Assume it is OK to update the entity, then inspect the conditions below
         */
        boolean updateAllowed = true;

        InstanceProvenanceType instanceProvenance = instance.getInstanceProvenanceType();
        switch (instanceProvenance)
        {
            case LOCAL_COHORT:
                String entityHome = instance.getMetadataCollectionId();
                if (entityHome != null && !entityHome.equals(metadataCollectionId))
                {
                    updateAllowed = false;
                }
                break;

            case EXTERNAL_SOURCE:
                String replicatedBy = instance.getReplicatedBy();
                if (replicatedBy != null && !replicatedBy.equals(metadataCollectionId))
                {
                    updateAllowed = false;
                }
                break;

            default:
                /*
                 * For any other instance provenance value do not allow update
                 */
                updateAllowed = false;
                break;
        }

        if (!updateAllowed)
        {
            /*
             * The instance should not be updated - throw InvalidParameterException
             */
            throw new InvalidParameterException(OMRSErrorCode.INSTANCE_HOME_NOT_LOCAL.getMessageDefinition(instance.getMetadataCollectionId(),
                                                                                                           methodName,
                                                                                                           instance.getGUID(),
                                                                                                           metadataCollectionId,
                                                                                                           sourceName),
                                                this.getClass().getName(),
                                                methodName,
                                                "instance");
        }
    }


    /**
     * Verify that an entity instance can be rehomed by the metadataCollection. This method is used
     * when the metadataCollection is called to rehome an entity instance.
     *
     * @param sourceName  source of the request (used for logging)
     * @param metadataCollectionId unique identifier for the metadata collection
     * @param instance  instance to validate
     * @param methodName  name of calling method
     * @throws InvalidParameterException  the entity is in deleted status
     */
    @Override
    public void validateEntityCanBeRehomed(String         sourceName,
                                           String         metadataCollectionId,
                                           InstanceHeader instance,
                                           String         methodName) throws InvalidParameterException
    {
        /*
         * Check that it is legal to rehome the entity.
         * The caller can rehome the entity provided:
         * The entity originates from the local cohort and is NOT locally mastered
         * OR
         * The entity has instanceProvenanceType set to external and replicatedBy is NOT the local metadataCollectionId.
         * Any other combination suggests that this is NOT a reference copy (of either an instance from the local cohort or
         * an external entity) and consequently cannot be rehomed.
         *
         * If not rehomable throw an InvalidParameterException.
         */

        /*
         * Assume it is NOT OK to rehome the entity, then inspect the conditions below
         */
        boolean updateAllowed = false;

        InstanceProvenanceType instanceProvenance = instance.getInstanceProvenanceType();
        switch (instanceProvenance)
        {
            case LOCAL_COHORT:
                String entityHome = instance.getMetadataCollectionId();
                if (entityHome != null && !entityHome.equals(metadataCollectionId))
                {
                    updateAllowed = true;
                }
                break;

            case EXTERNAL_SOURCE:
                String replicatedBy = instance.getReplicatedBy();
                if (replicatedBy != null && !replicatedBy.equals(metadataCollectionId))
                {
                    updateAllowed = true;
                }
                break;

            default:
                /*
                 * For any other instance provenance values allow the rehome action - this allows content pack information, deregistered repositories
                 * and configuration to be rehomed
                 */
                updateAllowed = true;
                break;
        }

        if (!updateAllowed)
        {
            /*
             * The instance should not be updated - throw InvalidParameterException
             */
            throw new InvalidParameterException(OMRSErrorCode.INSTANCE_HOME_NOT_LOCAL.getMessageDefinition(instance.getMetadataCollectionId(),
                                                                                                           methodName,
                                                                                                           instance.getGUID(),
                                                                                                           metadataCollectionId,
                                                                                                           sourceName),
                                                this.getClass().getName(),
                                                methodName,
                                                "instance");
        }
    }


    /**
     * Verify the status of a relationship to check it has been deleted.
     *
     * @param sourceName source of the request (used for logging)
     * @param instance instance to test
     * @param methodName name of calling method
     * @throws RelationshipNotKnownException the relationship is in deleted status
     */
    @Override
    public void validateRelationshipIsNotDeleted(String         sourceName,
                                                 InstanceHeader instance,
                                                 String         methodName) throws RelationshipNotKnownException
    {
        if (instance != null)
        {
            if (instance.getStatus() == InstanceStatus.DELETED)
            {
                 throw new RelationshipNotKnownException(OMRSErrorCode.RELATIONSHIP_SOFT_DELETED.getMessageDefinition(this.getTypeNameForMessage(instance),
                                                                                                                     instance.getGUID(),
                                                                                                                     methodName,
                                                                                                                     sourceName),
                                                         this.getClass().getName(),
                                                         methodName);
            }
        }
        else
        {
            /*
             * Logic error as the instance should be valid
             */
            final String   thisMethodName = "validateRelationshipIsNotDeleted";

            throwValidatorLogicError(sourceName, methodName, thisMethodName);
        }
    }


    /**
     * Verify the status of a relationship to check it has been deleted.
     *
     * @param sourceName source of the request (used for logging)
     * @param instance instance to test
     * @param methodName name of calling method
     * @throws RelationshipNotDeletedException the relationship is not in deleted status
     */
    @Override
    public void validateRelationshipIsDeleted(String         sourceName,
                                              InstanceHeader instance,
                                              String         methodName) throws RelationshipNotDeletedException
    {
        if (instance != null)
        {
            if (instance.getStatus() != InstanceStatus.DELETED)
            {
                throw new RelationshipNotDeletedException(OMRSErrorCode.INSTANCE_NOT_DELETED.getMessageDefinition(methodName,
                                                                                                                  sourceName,
                                                                                                                  instance.getGUID()),
                                                          this.getClass().getName(),
                                                          methodName);
            }
        }
        else
        {
            /*
             * Logic error as the instance should be valid
             */
            final String   thisMethodName = "validateRelationshipIsDeleted";

            throwValidatorLogicError(sourceName, methodName, thisMethodName);
        }
    }


    /**
     * Verify that a relationship instance can be updated by the metadataCollection. This method is used
     * when the metadataCollection is called to update the status or properties of a relationship instance.
     *
     * @param sourceName  source of the request (used for logging)
     * @param metadataCollectionId unique identifier for the metadata collection
     * @param instance  instance to validate
     * @param methodName  name of calling method
     * @throws InvalidParameterException  the entity is in deleted status
     */
    @Override
    public void validateRelationshipCanBeUpdated(String         sourceName,
                                                 String         metadataCollectionId,
                                                 InstanceHeader instance,
                                                 String         methodName) throws InvalidParameterException
    {

        /*
         * Check that it is legal to update the relationship.
         * The caller can update the relationship provided:
         * The relationship is locally mastered
         * OR
         * The relationship has instanceProvenanceType set to external and replicatedBy is set to the local metadataCollectionId.
         * Any other combination suggest that this is either a reference copy of an instance from the local cohort or a reference
         * copy of an external relationship (and something else is responsible for replication).
         *
         * If not throw an InvalidParameterException.
         */

        /*
         * Assume it is OK to update the relationship, then inspect the conditions below
         */
        boolean updateAllowed = true;

        InstanceProvenanceType instanceProvenance = instance.getInstanceProvenanceType();
        switch (instanceProvenance)
        {
            case LOCAL_COHORT:
                String relationshipHome = instance.getMetadataCollectionId();
                if (relationshipHome != null && !relationshipHome.equals(metadataCollectionId))
                {
                    updateAllowed = false;
                }
                break;

            case EXTERNAL_SOURCE:
                String replicatedBy = instance.getReplicatedBy();
                if (replicatedBy != null && !replicatedBy.equals(metadataCollectionId))
                {
                    updateAllowed = false;
                }
                break;

            default:
                /*
                 * For any other instance provenance value do not allow update
                 */
                updateAllowed = false;
                break;
        }

        if (!updateAllowed)
        {
            /*
             * The instance should not be updated - throw InvalidParameterException
             */
            throw new InvalidParameterException(OMRSErrorCode.INSTANCE_HOME_NOT_LOCAL.getMessageDefinition(instance.getMetadataCollectionId(),
                                                                                                           methodName,
                                                                                                           instance.getGUID(),
                                                                                                           metadataCollectionId,
                                                                                                           sourceName),
                                                this.getClass().getName(),
                                                methodName,
                                                "instance");
        }

    }

    /**
     * Verify that a relationship instance can be rehomed by the metadataCollection. This method is used
     * when the metadataCollection is called to rehome a relationship instance.
     *
     * @param sourceName  source of the request (used for logging)
     * @param metadataCollectionId unique identifier for the metadata collection
     * @param instance  instance to validate
     * @param methodName  name of calling method
     * @throws InvalidParameterException  the entity is in deleted status
     */
    @Override
    public void validateRelationshipCanBeRehomed(String         sourceName,
                                                 String         metadataCollectionId,
                                                 InstanceHeader instance,
                                                 String         methodName) throws InvalidParameterException
    {

        /*
         * Check that it is legal to rehome the relationship.
         * The caller can rehome the relationship provided:
         * The relationship originates from the local cohort and is NOT locally mastered
         * OR
         * The relationship has instanceProvenanceType set to external and replicatedBy is NOT the local metadataCollectionId.
         * Any other combination suggests that this is NOT a reference copy (of either an instance from the local cohort or
         * an external relationship) and consequently cannot be rehomed.
         *
         * If not rehomable throw an InvalidParameterException.
         */

        /*
         * Assume it is NOT OK to rehome the entity, then inspect the conditions below
         */
        boolean updateAllowed = false;

        InstanceProvenanceType instanceProvenance = instance.getInstanceProvenanceType();
        switch (instanceProvenance)
        {
            case LOCAL_COHORT:
                String relationshipHome = instance.getMetadataCollectionId();
                if (relationshipHome != null && !relationshipHome.equals(metadataCollectionId))
                {
                    updateAllowed = true;
                }
                break;

            case EXTERNAL_SOURCE:
                String replicatedBy = instance.getReplicatedBy();
                if (replicatedBy != null && !replicatedBy.equals(metadataCollectionId))
                {
                    updateAllowed = true;
                }
                break;

            default:
                /*
                 * For any other instance provenance values allow the rehome action - this allows content pack information, deregistered repositories
                 * and configuration to be rehomed
                 */
                updateAllowed = true;
                break;
        }

        if (!updateAllowed)
        {
            /*
             * The instance should not be updated - throw InvalidParameterException
             */
            throw new InvalidParameterException(OMRSErrorCode.INSTANCE_HOME_IS_LOCAL.getMessageDefinition(instance.getMetadataCollectionId(),
                                                                                                          methodName,
                                                                                                          instance.getGUID(),
                                                                                                          metadataCollectionId,
                                                                                                          sourceName),
                                                this.getClass().getName(),
                                                methodName,
                                                "instance");
        }
    }

    /**
     * Validate that the types of the two ends of a relationship match the relationship's TypeDef.
     *
     * @param sourceName source of the request (used for logging)
     * @param entityOneProxy content of end one
     * @param entityTwoProxy content of end two
     * @param typeDef typeDef for the relationship
     * @param methodName name of the method making the request
     * @throws InvalidParameterException types do not align
     */
    @Override
    public void validateRelationshipEnds(String        sourceName,
                                         EntityProxy   entityOneProxy,
                                         EntityProxy   entityTwoProxy,
                                         TypeDef       typeDef,
                                         String        methodName) throws InvalidParameterException
    {
        final String thisMethodName = "validateRelationshipEnds";

        if ((entityOneProxy != null) && (entityTwoProxy != null) && (typeDef != null))
        {
            try
            {
                RelationshipDef    relationshipDef      = (RelationshipDef) typeDef;
                RelationshipEndDef entityOneEndDef      = null;
                RelationshipEndDef entityTwoEndDef      = null;
                TypeDefLink        entityOneTypeDef     = null;
                TypeDefLink        entityTwoTypeDef     = null;
                String             entityOneTypeDefName = null;
                String             entityTwoTypeDefName = null;
                InstanceType       entityOneType        = null;
                InstanceType       entityTwoType        = null;
                String             entityOneTypeName    = null;
                String             entityTwoTypeName    = null;

                entityOneEndDef = relationshipDef.getEndDef1();
                entityTwoEndDef = relationshipDef.getEndDef2();
                log.debug("Got EndDefs");

                if ((entityOneEndDef != null) && (entityTwoEndDef != null))
                {
                    entityOneTypeDef = entityOneEndDef.getEntityType();
                    entityTwoTypeDef = entityTwoEndDef.getEntityType();
                    log.debug("Got EndDef Types");
                }

                if ((entityOneTypeDef != null) && (entityTwoTypeDef != null))
                {
                    entityOneTypeDefName = entityOneTypeDef.getName();
                    entityTwoTypeDefName = entityTwoTypeDef.getName();
                    log.debug("Got EndDef Types of One: " + entityOneTypeDefName + " and Two: " + entityTwoTypeDefName);

                }

                /*
                 * At this point we know what the expected types of
                 * the ends should be.  Now look at the relationship instance.
                 */

                entityOneType = entityOneProxy.getType();
                entityTwoType = entityTwoProxy.getType();

                log.debug("Got Proxy Types of One: " + entityOneType + " and Two: " + entityOneType);


                if ((entityOneType != null) && (entityTwoType != null))
                {
                    entityOneTypeName = entityOneType.getTypeDefName();
                    entityTwoTypeName = entityTwoType.getTypeDefName();
                }

                if ((repositoryContentManager.isTypeOf(sourceName, entityOneTypeName, entityOneTypeDefName) &&
                     repositoryContentManager.isTypeOf(sourceName, entityTwoTypeName, entityTwoTypeDefName)))
                {
                    return;
                }

                throw new InvalidParameterException(OMRSErrorCode.INVALID_RELATIONSHIP_ENDS.getMessageDefinition(methodName,
                                                                                                                 sourceName,
                                                                                                                 typeDef.getName(),
                                                                                                                 entityOneProxy.getGUID(),
                                                                                                                 entityOneTypeName,
                                                                                                                 entityOneTypeDefName,
                                                                                                                 entityTwoProxy.getGUID(),
                                                                                                                 entityTwoTypeName,
                                                                                                                 entityTwoTypeDefName),
                                                    this.getClass().getName(),
                                                    methodName,
                                                    "relationship.End");
            }
            catch (InvalidParameterException error)
            {
                throw error;
            }
            catch (Exception error)
            {
                /*
                 * Logic error as the instance should be valid
                 */
                throwValidatorLogicError(sourceName, methodName, thisMethodName);
            }
        }
        else
        {
            throwValidatorLogicError(sourceName,
                                     methodName,
                                     thisMethodName);
        }
    }


    /**
     * Return a boolean indicating whether the supplied entity is classified with the supplied classification.
     *
     * @param requiredClassification required classification; null means that there are no specific
     *                               classification requirements and so results in a true response.
     * @param entity entity to test.
     * @return boolean result
     */
    private boolean verifyEntityIsClassified(String        requiredClassification,
                                             EntitySummary entity)
    {
        if (requiredClassification != null)
        {
            List<Classification> entityClassifications = entity.getClassifications();
            if (entityClassifications != null)
            {
                for (Classification entityClassification : entityClassifications)
                {
                    if (entityClassification != null)
                    {
                        if (requiredClassification.equals(entityClassification.getName()))
                        {
                            return true;
                        }
                    }
                }
            }
        }
        else
        {
            return true;
        }
        return false;
    }


    /**
     * Return a boolean indicating whether the supplied entity is classified with one or more of the supplied
     * classifications.
     *
     * @param requiredClassifications list of required classification null means that there are no specific
     *                                classification requirements and so results in a true response.
     * @param entity entity to test.
     * @return boolean result
     */
    @Override
    public boolean verifyEntityIsClassified(List<String> requiredClassifications,
                                            EntitySummary entity)
    {
        if (requiredClassifications != null)
        {
            List<Classification> entityClassifications = entity.getClassifications();

            if (entityClassifications != null)
            {
                for (String requiredClassification : requiredClassifications)
                {
                    if (requiredClassification != null)
                    {
                        for (Classification entityClassification : entityClassifications)
                        {
                            if (entityClassification != null)
                            {
                                if (requiredClassification.equals(entityClassification.getName()))
                                {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        else
        {
            return true;
        }
        return false;
    }


    /**
     * Return the string form of the instance properties.  Can be used as propertyValue on find property
     * value calls.
     *
     * @param instanceProperties value to extract the string from
     * @return extracted string value.
     */
    @Override
    public String getStringValuesFromInstancePropertiesAsArray(InstanceProperties   instanceProperties)
    {
        final String  stringStart = "{ ";
        final String  stringDelimiter = ", ";
        final String  stringEnd = " }";

        String        results = null;

        if (instanceProperties != null)
        {
            Iterator<String> propertyNames = instanceProperties.getPropertyNames();

            while (propertyNames.hasNext())
            {
                String propertyName = propertyNames.next();

                if (results == null)
                {
                    results = getStringFromPropertyValue(instanceProperties.getPropertyValue(propertyName));
                }
                else
                {
                    results = results + stringDelimiter + getStringFromPropertyValue(instanceProperties.getPropertyValue(propertyName));
                }
            }
        }

        return stringStart + results + stringEnd;
    }


    /**
     * Return the string form of the instance properties.  Can be used as propertyValue on find property
     * value calls.
     *
     * @param instanceProperties value to extract the string from
     * @return extracted string value.
     */
    @Override
    public String getStringValuesFromInstancePropertiesAsMap(InstanceProperties   instanceProperties)
    {
        final String  stringStart = "{ ";
        final String  stringDelimiter = ", ";
        final String  mappingDelimiter = " -> ";
        final String  stringEnd = " }";

        String        results = null;

        if (instanceProperties != null)
        {
            Iterator<String> propertyNames = instanceProperties.getPropertyNames();

            while (propertyNames.hasNext())
            {
                String propertyName = propertyNames.next();

                if (results == null)
                {
                    results = propertyName + mappingDelimiter + getStringFromPropertyValue(instanceProperties.getPropertyValue(propertyName));
                }
                else
                {
                    results = results + stringDelimiter + propertyName + mappingDelimiter + getStringFromPropertyValue(instanceProperties.getPropertyValue(propertyName));
                }
            }
        }

        return stringStart + results + stringEnd;
    }


    /**
     * Return the string form of the instance properties.  Can be used as propertyValue on find property
     * value calls.
     *
     * @param instanceProperties value to extract the string from
     * @return extracted string value.
     */
    @Override
    public String getStringValuesFromInstancePropertiesAsStruct(InstanceProperties   instanceProperties)
    {
        final String  stringStart = "{ ";
        final String  stringDelimiter = ", ";
        final String  mappingDelimiter = " : ";
        final String  stringEnd = " }";

        String        results = null;

        if (instanceProperties != null)
        {
            Iterator<String> propertyNames = instanceProperties.getPropertyNames();

            while (propertyNames.hasNext())
            {
                String propertyName = propertyNames.next();

                if (results == null)
                {
                    results = propertyName + mappingDelimiter + getStringFromPropertyValue(instanceProperties.getPropertyValue(propertyName));
                }
                else
                {
                    results = results + stringDelimiter + propertyName + mappingDelimiter + getStringFromPropertyValue(instanceProperties.getPropertyValue(propertyName));
                }
            }
        }

        return stringStart + results + stringEnd;
    }


    /**
     * Return the string form of a property value.  Can be used as propertyValue on find property
     * value calls.
     *
     * @param instancePropertyValue value to extract the string from
     * @return extracted string value.
     */
    @Override
    public String getStringFromPropertyValue(InstancePropertyValue   instancePropertyValue)
    {
        if (instancePropertyValue != null)
        {
            if (instancePropertyValue.getInstancePropertyCategory() == PRIMITIVE)
            {
                PrimitivePropertyValue primitivePropertyValue = (PrimitivePropertyValue)instancePropertyValue;

                return primitivePropertyValue.getPrimitiveValue().toString();
            }
            else if (instancePropertyValue.getInstancePropertyCategory() == InstancePropertyCategory.ENUM)
            {
                EnumPropertyValue enumPropertyValue = (EnumPropertyValue)instancePropertyValue;

                return enumPropertyValue.getSymbolicName();
            }
            else if (instancePropertyValue.getInstancePropertyCategory() == InstancePropertyCategory.ARRAY)
            {
                ArrayPropertyValue  arrayPropertyValue = (ArrayPropertyValue)instancePropertyValue;

                if (arrayPropertyValue.getArrayCount() > 0)
                {
                    return this.getStringValuesFromInstancePropertiesAsArray(arrayPropertyValue.getArrayValues());
                }
            }
            else if (instancePropertyValue.getInstancePropertyCategory() == InstancePropertyCategory.MAP)
            {
                MapPropertyValue mapPropertyValue = (MapPropertyValue)instancePropertyValue;

                return this.getStringValuesFromInstancePropertiesAsMap(mapPropertyValue.getMapValues());
            }
            else if (instancePropertyValue.getInstancePropertyCategory() == InstancePropertyCategory.STRUCT)
            {
                StructPropertyValue structPropertyValue = (StructPropertyValue)instancePropertyValue;

                return this.getStringValuesFromInstancePropertiesAsStruct(structPropertyValue.getAttributes());
            }
        }

        return null;
    }



    /**
     * Count the number of matching property values that an instance has.  They may come from an entity,
     * classification or relationship.
     *
     * @param matchProperties the properties to match.
     * @param instanceProperties the properties from the instance.
     * @return integer count of the matching properties.
     * @throws InvalidParameterException invalid search criteria
     */
    @Override
    @SuppressWarnings(value="fallthrough")
    public int countMatchingPropertyValues(InstanceProperties       matchProperties,
                                           InstanceProperties       instanceProperties) throws InvalidParameterException
    {
        final String  methodName = "countMatchingPropertyValues";
        int           matchingProperties = 0;

        if ((matchProperties != null) && (instanceProperties != null))
        {
            Iterator<String> matchPropertyNames = matchProperties.getPropertyNames();

            while (matchPropertyNames.hasNext())
            {
                String matchPropertyName = matchPropertyNames.next();

                if (matchPropertyName != null)
                {
                    InstancePropertyValue matchPropertyValue = matchProperties.getPropertyValue(matchPropertyName);

                    if (matchPropertyValue != null)
                    {
                        String           matchPropertyValueString = this.getStringFromPropertyValue(matchPropertyValue);
                        Iterator<String> instancePropertyNames    = instanceProperties.getPropertyNames();

                        while (instancePropertyNames.hasNext())
                        {
                            String instancePropertyName = instancePropertyNames.next();

                            if (instancePropertyName != null)
                            {
                                InstancePropertyValue instancePropertyValue = instanceProperties.getPropertyValue(instancePropertyName);

                                if (instancePropertyValue != null)
                                {
                                    if (instancePropertyName.equals(matchPropertyName))
                                    {
                                        /*
                                         * The property names match - do the values?
                                         */


                                        /*
                                         * The type of match performed depends on the property category [and for primitives also
                                         * the primitive def category]. The rules are as follows:
                                         *
                                         * Primitives:
                                         *   ** String        - the match value is used as a full regex.
                                         *   ** Non-String    - the match value must be an exact match (.equals() not regex)
                                         * Non-primitives:
                                         *   ** Array         - flattened to "{ value, ... }" and matched using contains regex
                                         *   ** Map           - flattened to "{ key -> value, ... }" and matched using contains regex
                                         *   ** Struct        - flattened to "{ key : value, ... }" and matched using contains regex
                                         *   ** Enums:        - matched using exact match (not regex)
                                         *
                                         */
                                        MatchOption matchOption;

                                        InstancePropertyCategory ipCat = instancePropertyValue.getInstancePropertyCategory();

                                        if (ipCat == PRIMITIVE)
                                        {

                                            /*
                                             * Property is a primitive.
                                             * If it is a string, use a full regex match.
                                             * If not a string, use an exact match.
                                             */

                                            PrimitivePropertyValue primPropValue = (PrimitivePropertyValue) instancePropertyValue;

                                            if (primPropValue.getPrimitiveDefCategory() == OM_PRIMITIVE_TYPE_STRING)
                                            {
                                                matchOption = MatchOption.RegexFullMatch;
                                            }
                                            else
                                            {
                                                matchOption = MatchOption.ExactMatch;
                                            }
                                        }
                                        else
                                        {

                                            /*
                                             * Property is not a primitive.
                                             * If it is an Array, Struct or Map use a contains regex match (against stringified collection)
                                             * If it is an Enum use an exact match
                                             *
                                             */
                                            if (ipCat == ENUM)
                                            {
                                                matchOption = MatchOption.ExactMatch;
                                            }
                                            else
                                            {
                                                matchOption = MatchOption.RegexContainsMatch;
                                            }
                                        }

                                        /*
                                         *  Perform the appropriate comparison
                                         */
                                        switch (matchOption)
                                        {
                                            case ExactMatch:
                                                if (instancePropertyValue.equals(matchPropertyValue))
                                                {
                                                    /*
                                                     * The values match exactly.
                                                     */
                                                    matchingProperties++;
                                                }
                                                break;

                                            case RegexContainsMatch:
                                                matchPropertyValueString = ".*" + matchPropertyValueString + ".*";
                                                // deliberate no break; let this drop through with the modified match string
                                            case RegexFullMatch:
                                                /*
                                                 * Does a regex match work? It must match the complete regex...
                                                 */
                                                String instancePropertyValueString = this.getStringFromPropertyValue(instancePropertyValue);

                                                if (instancePropertyValueString != null)
                                                {
                                                    try
                                                    {
                                                        if (instancePropertyValueString.matches(matchPropertyValueString))
                                                        {
                                                            matchingProperties++;
                                                        }
                                                    }
                                                    catch (Exception error)
                                                    {
                                                        throw new InvalidParameterException(
                                                                OMRSErrorCode.INVALID_SEARCH_CRITERIA.getMessageDefinition(error.getClass().getName(),
                                                                                                                           matchPropertyValueString,
                                                                                                                           instancePropertyValueString,
                                                                                                                           error.getMessage(),
                                                                                                                           methodName),
                                                                this.getClass().getName(),
                                                                methodName,
                                                                "instanceProperties");
                                                    }
                                                }
                                                break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return matchingProperties;
    }


    /**
     * Return true if the expected value is in the property map.
     *
     * @param propertyMap map with the properties
     * @param propertyName name of the property to test
     * @param expectedValue expected value
     * @return boolean result
     */
    private  boolean  checkStringPropertyValue(Map<String, InstancePropertyValue>   propertyMap,
                                               String                               propertyName,
                                               String                               expectedValue)
    {
        boolean                 result = false;
        InstancePropertyValue   instancePropertyValue = propertyMap.get(propertyName);

        if (instancePropertyValue != null)
        {
            if (instancePropertyValue.getInstancePropertyCategory() == PRIMITIVE)
            {
                try
                {
                    PrimitivePropertyValue primitivePropertyValue = (PrimitivePropertyValue)instancePropertyValue;
                    String                 matchValue = (String)primitivePropertyValue.getPrimitiveValue();

                    if (matchValue != null)
                    {
                        if (expectedValue.matches(matchValue))
                        {
                            result = true;
                        }
                    }
                }
                catch (Exception   exc)
                {
                    /*
                     * Ignore property
                     */
                }
            }
        }

        return result;
    }


    /**
     * Return true if the expected value is in the property map.
     *
     * @param propertyMap map with the properties
     * @param propertyName name of the property to test
     * @param expectedValue expected value
     * @return boolean result
     */
    private  boolean  checkDatePropertyValue(Map<String, InstancePropertyValue>   propertyMap,
                                             String                               propertyName,
                                             Date                                 expectedValue)
    {
        boolean                 result = false;
        InstancePropertyValue   instancePropertyValue = propertyMap.get(propertyName);

        if (instancePropertyValue != null)
        {
            if (instancePropertyValue.getInstancePropertyCategory() == PRIMITIVE)
            {
                try
                {
                    PrimitivePropertyValue primitivePropertyValue = (PrimitivePropertyValue)instancePropertyValue;
                    Long timestamp = (Long)primitivePropertyValue.getPrimitiveValue();

                    if (timestamp != null)
                    {
                        Date matchValue = new Date(timestamp);
                        if (matchValue.equals(expectedValue))
                        {
                            result = true;
                        }
                    }
                }
                catch (Exception   exc)
                {
                    /*
                     * Ignore property
                     */
                }
            }
        }
        return result;
    }


    /**
     * Count the number of matching property values that an instance has.  They may come from an entity,
     * or relationship.
     *
     * @param matchProperties  the properties to match.
     * @param instanceHeader  the header properties from the instance.
     * @param instanceProperties  the effectivity dates.
     * @return integer count of the matching properties.
     */
    @Override
    public int countMatchingHeaderPropertyValues(InstanceProperties       matchProperties,
                                                 InstanceAuditHeader      instanceHeader,
                                                 InstanceProperties       instanceProperties)
    {
        final String metadataCollectionIdPropertyName = "metadataCollectionId";
        final String metadataCollectionNamePropertyName = "metadataCollectionName";
        final String typeNamePropertyName = "typeName";
        final String typeGUIDPropertyName = "typeGUID";
        final String createdByPropertyName = "createdBy";
        final String updatedByPropertyName = "updatedBy";
        final String createTimePropertyName = "createTime";
        final String updateTimePropertyName = "updateTime";
        final String effectiveFromPropertyName = "effectiveFrom";
        final String effectiveToPropertyName = "effectiveTo";

        int       matchingProperties = 0;

        if ((matchProperties != null) && (instanceHeader != null))
        {
            Map<String, InstancePropertyValue>   propertyMap = matchProperties.getInstanceProperties();

            if (propertyMap != null)
            {
                if (this.checkStringPropertyValue(propertyMap, metadataCollectionIdPropertyName, instanceHeader.getMetadataCollectionId()))
                {
                    matchingProperties ++;
                }
                if (this.checkStringPropertyValue(propertyMap, metadataCollectionNamePropertyName, instanceHeader.getMetadataCollectionName()))
                {
                    matchingProperties ++;
                }
                if (this.checkStringPropertyValue(propertyMap, typeNamePropertyName, instanceHeader.getType().getTypeDefName()))
                {
                    matchingProperties ++;
                }
                if (this.checkStringPropertyValue(propertyMap, typeGUIDPropertyName, instanceHeader.getType().getTypeDefGUID()))
                {
                    matchingProperties ++;
                }
                if (this.checkStringPropertyValue(propertyMap, createdByPropertyName, instanceHeader.getCreatedBy()))
                {
                    matchingProperties ++;
                }
                if (this.checkStringPropertyValue(propertyMap, updatedByPropertyName, instanceHeader.getUpdatedBy()))
                {
                    matchingProperties ++;
                }
                if (this.checkDatePropertyValue(propertyMap, createTimePropertyName, instanceHeader.getCreateTime()))
                {
                    matchingProperties ++;
                }
                if (this.checkDatePropertyValue(propertyMap, updateTimePropertyName, instanceHeader.getUpdateTime()))
                {
                    matchingProperties ++;
                }
                if (instanceProperties != null) {

                    if (this.checkDatePropertyValue(propertyMap, effectiveFromPropertyName, instanceProperties.getEffectiveFromTime())) {
                        matchingProperties++;
                    }
                    if (this.checkDatePropertyValue(propertyMap, effectiveToPropertyName, instanceProperties.getEffectiveToTime())) {
                        matchingProperties++;
                    }

                }
            }
        }

        return matchingProperties;
    }


    /**
     * Determine if the instance properties match the match criteria.
     *
     * @param matchProperties  the properties to match.
     * @param instanceHeader the header of the instance.
     * @param instanceProperties  the properties from the instance.
     * @param matchCriteria  rule on how the match should occur.
     * @return boolean flag indicating whether the two sets of properties match
     * @throws InvalidParameterException invalid search criteria
     */
    @Override
    public boolean verifyMatchingInstancePropertyValues(InstanceProperties   matchProperties,
                                                        InstanceAuditHeader  instanceHeader,
                                                        InstanceProperties   instanceProperties,
                                                        MatchCriteria        matchCriteria) throws InvalidParameterException
    {
        if (matchProperties != null && matchProperties.getInstanceProperties() != null)
        {
            int matchingProperties = this.countMatchingPropertyValues(matchProperties, instanceProperties) +
                                     this.countMatchingHeaderPropertyValues(matchProperties, instanceHeader, instanceProperties);

            switch (matchCriteria)
            {
                case ALL:
                    if (matchingProperties == matchProperties.getPropertyCount())
                    {
                        return true;
                    }
                    break;

                case ANY:
                    if (matchingProperties > 0)
                    {
                        return true;
                    }
                    break;

                case NONE:
                    if (matchingProperties == 0)
                    {
                        return true;
                    }
                    break;
            }
        }
        else
        {
            return true;
        }

        return false;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal getNumericRepresentation(InstancePropertyValue value)
    {
        if (value == null)
        {
            return null;
        }
        InstancePropertyCategory category = value.getInstancePropertyCategory();
        if (category.equals(PRIMITIVE))
        {
            PrimitivePropertyValue ppv = (PrimitivePropertyValue) value;
            switch (ppv.getPrimitiveDefCategory())
            {
                case OM_PRIMITIVE_TYPE_DATE:
                case OM_PRIMITIVE_TYPE_LONG:
                    return BigDecimal.valueOf((Long)ppv.getPrimitiveValue());
                case OM_PRIMITIVE_TYPE_SHORT:
                    return BigDecimal.valueOf((Short)ppv.getPrimitiveValue());
                case OM_PRIMITIVE_TYPE_INT:
                    return BigDecimal.valueOf((Integer)ppv.getPrimitiveValue());
                case OM_PRIMITIVE_TYPE_FLOAT:
                    return BigDecimal.valueOf((Float) ppv.getPrimitiveValue());
                case OM_PRIMITIVE_TYPE_DOUBLE:
                    return BigDecimal.valueOf((Double) ppv.getPrimitiveValue());
                case OM_PRIMITIVE_TYPE_BIGINTEGER:
                    return new BigDecimal((BigInteger)ppv.getPrimitiveValue());
                case OM_PRIMITIVE_TYPE_BIGDECIMAL:
                    return (BigDecimal) ppv.getPrimitiveValue();
                default:
                    return null;
            }
        }
        return null;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean verifyMatchingInstancePropertyValues(SearchProperties    matchProperties,
                                                        InstanceAuditHeader instanceHeader,
                                                        InstanceProperties  instanceProperties) throws InvalidParameterException
    {
        final String methodName = "verifyMatchingInstancePropertyValues";
        if (matchProperties == null)
        {
            return true;
        }
        List<PropertyCondition> conditions = matchProperties.getConditions();
        int conditionMatchCount = 0;
        for (PropertyCondition condition : conditions)
        {
            // Simplest way: this will also short-circuit to true immediately if nested conditions is null
            boolean matchesNested = verifyMatchingInstancePropertyValues(condition.getNestedConditions(), instanceHeader, instanceProperties);
            String propertyName = condition.getProperty();
            InstancePropertyValue testValue = condition.getValue();
            InstancePropertyValue actualValue = null;
            if (instanceProperties != null)
            {
                actualValue = instanceProperties.getPropertyValue(propertyName);
            }
            boolean matchesProperties = true;
            BigDecimal testBD = getNumericRepresentation(testValue);
            BigDecimal actualBD = getNumericRepresentation(actualValue);
            PropertyComparisonOperator operator = condition.getOperator();

            /*
             * When the nested branch is complete, operator will be null (along
             * with propertyName, testValue, etc). All that remains for this
             * (nested) property condition is to contribute its result
             * (matchesNested && matchesProperties) into the current level of
             * property condition combination.
             *
             * Do not attempt to use null operator in switch statement, it will NPE
             */

            if (operator != null)
            {
                switch (condition.getOperator())
                {
                    case EQ:
                        matchesProperties = Objects.equals(actualValue, testValue);
                        break;
                    case NEQ:
                        matchesProperties = !Objects.equals(actualValue, testValue);
                        break;
                    case LT:
                        // Should only apply to number values and dates
                        matchesProperties = (actualBD != null && testBD != null && actualBD.compareTo(testBD) < 0);
                        break;
                    case LTE:
                        // Should only apply to number values and dates
                        matchesProperties = (actualBD != null && testBD != null && actualBD.compareTo(testBD) <= 0);
                        break;
                    case GT:
                        // Should only apply to number values and dates
                        matchesProperties = (actualBD != null && testBD != null && actualBD.compareTo(testBD) > 0);
                        break;
                    case GTE:
                        // Should only apply to number values and dates
                        matchesProperties = (actualBD != null && testBD != null && actualBD.compareTo(testBD) >= 0);
                        break;
                    case IN:
                        // The value to test against must be a list (ArrayTypePropertyValue)
                        if (testValue instanceof ArrayPropertyValue)
                        {
                            ArrayPropertyValue apv = (ArrayPropertyValue) testValue;
                            InstanceProperties values = apv.getArrayValues();
                            if (values == null)
                            {
                                // Impossible to match against an empty list, so always return false
                                matchesProperties = false;
                            }
                            else
                            {
                                Iterator<String> names = values.getPropertyNames();
                                matchesProperties = false;
                                while (names.hasNext() && !matchesProperties)
                                {
                                    String index = names.next();
                                    InstancePropertyValue oneTestValue = values.getPropertyValue(index);
                                    if (oneTestValue != null)
                                    {
                                        matchesProperties = oneTestValue.equals(actualValue);
                                    }
                                }
                            }
                        }
                        else
                        {
                            throw new InvalidParameterException(OMRSErrorCode.INVALID_LIST_CONDITION.getMessageDefinition(),
                                                                this.getClass().getName(),
                                                                methodName,
                                                                "matchProperties");
                        }
                        break;
                    case IS_NULL:
                        matchesProperties = (actualValue == null);
                        break;
                    case NOT_NULL:
                        matchesProperties = (actualValue != null);
                        break;
                    case LIKE:
                        // Should only apply to strings
                        if (testValue instanceof PrimitivePropertyValue && ((PrimitivePropertyValue) testValue).getPrimitiveDefCategory().equals(OM_PRIMITIVE_TYPE_STRING))
                        {
                            String test = testValue.valueAsString();
                            if (actualValue == null)
                            {
                                matchesProperties = false;
                            }
                            else
                            {
                                String actual = actualValue.valueAsString();
                                matchesProperties = actual.matches(test);
                            }
                        }
                        else
                        {
                            throw new InvalidParameterException(OMRSErrorCode.INVALID_LIKE_CONDITION.getMessageDefinition(),
                                                                this.getClass().getName(),
                                                                methodName,
                                                                "matchProperties");
                        }
                        break;
                    default:
                        matchesProperties = true;
                        break;
                }
            }
            conditionMatchCount += (matchesNested && matchesProperties) ? 1 : 0;
        }
        // TODO: we may want to move this into the loop above to short-circuit (for performance)
        switch (matchProperties.getMatchCriteria())
        {
            case ALL:
                if (conditionMatchCount == conditions.size())
                {
                    return true;
                }
                break;
            case ANY:
                if (conditionMatchCount > 0)
                {
                    return true;
                }
                break;
            case NONE:
                if (conditionMatchCount == 0)
                {
                    return true;
                }
                break;
        }
        return false;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean verifyMatchingClassifications(SearchClassifications matchClassifications,
                                                 EntitySummary         entity) throws InvalidParameterException
    {
        if (matchClassifications == null)
        {
            return true;
        }
        List<ClassificationCondition> conditions = matchClassifications.getConditions();
        if (conditions == null)
        {
            return true;
        }
        int matchingClassificationCount = 0;
        List<Classification> classifications = entity.getClassifications();
        for (ClassificationCondition condition : conditions)
        {
            String classificationName = condition.getName();
            if (classificationName != null)
            {
                // Only attempt to match if a classification name has been provided: if not, we cannot match against
                // the requested classification (as no definition of a classification has been provided that we should
                // attempt to match against)
                boolean isClassified = verifyEntityIsClassified(classificationName, entity);
                SearchProperties properties = condition.getMatchProperties();
                boolean classificationMatches = false;
                for (Classification classification : classifications)
                {
                    if (classificationName.equals(classification.getName()))
                    {
                        classificationMatches = verifyMatchingInstancePropertyValues(properties, entity, classification.getProperties());
                    }
                }
                matchingClassificationCount += (isClassified && classificationMatches) ? 1 : 0;
            }
        }
        // TODO: we may want to move this into the loop above to short-circuit (for performance)
        switch (matchClassifications.getMatchCriteria())
        {
            case ALL:
                if (matchingClassificationCount == conditions.size())
                {
                    return true;
                }
                break;
            case ANY:
                if (matchingClassificationCount > 0)
                {
                    return true;
                }
                break;
            case NONE:
                if (matchingClassificationCount == 0)
                {
                    return true;
                }
                break;
        }
        return false;
    }


    /**
     * Validates that an instance has the correct header for it to be a reference copy.
     *
     * @param sourceName source of the request (used for logging)
     * @param localMetadataCollectionId  the unique identifier for the local repository' metadata collection.
     * @param instanceParameterName the name of the parameter that provided the instance.
     * @param instance the instance to test
     * @param methodName the name of the method that supplied the instance.
     * @throws RepositoryErrorException problem with repository
     * @throws InvalidParameterException the instance is null or linked to local metadata repository
     */
    @Override
    public void validateReferenceInstanceHeader(String         sourceName,
                                                String         localMetadataCollectionId,
                                                String         instanceParameterName,
                                                InstanceHeader instance,
                                                String         methodName) throws InvalidParameterException,
                                                                                  RepositoryErrorException
    {
        this.validateReferenceInstanceHeader(sourceName, localMetadataCollectionId, instanceParameterName, instance, null, methodName);
    }


    /**
     * Validates that an instance has the correct header for it to be a reference copy.
     *
     * @param sourceName source of the request (used for logging)
     * @param localMetadataCollectionId  the unique identifier for the local repository' metadata collection.
     * @param instanceParameterName the name of the parameter that provided the instance.
     * @param instance the instance to test
     * @param auditLog optional logging destination
     * @param methodName the name of the method that supplied the instance.
     * @throws RepositoryErrorException problem with repository
     * @throws InvalidParameterException the instance is null or linked to local metadata repository
     */
    @Override
    public void validateReferenceInstanceHeader(String         sourceName,
                                                String         localMetadataCollectionId,
                                                String         instanceParameterName,
                                                InstanceHeader instance,
                                                AuditLog       auditLog,
                                                String         methodName) throws InvalidParameterException,
                                                                                  RepositoryErrorException
    {
        if (instance == null)
        {
            throw new InvalidParameterException(OMRSErrorCode.NULL_REFERENCE_INSTANCE.getMessageDefinition(sourceName, methodName),
                                                this.getClass().getName(),
                                                methodName,
                                                instanceParameterName);
        }

        this.validateInstanceType(sourceName, instance);

        this.validateHomeMetadataGUID(sourceName, instanceParameterName, instance.getMetadataCollectionId(), methodName);

        if (localMetadataCollectionId.equals(instance.getMetadataCollectionId()))
        {
            if (auditLog != null)
            {
                auditLog.logMessage(methodName, OMRSAuditCode.LOCAL_REFERENCE_INSTANCE.getMessageDefinition(instanceParameterName,
                                                                                                            methodName,
                                                                                                            localMetadataCollectionId));
            }
        }

        if (instance instanceof EntitySummary)
        {
            if (instance.getType().getTypeDefCategory() != TypeDefCategory.ENTITY_DEF)
            {
                throw new InvalidParameterException(OMRSErrorCode.WRONG_TYPEDEF_CATEGORY.getMessageDefinition(instance.getGUID(),
                        "entity", instance.getType().getTypeDefName(), instance.getType().getTypeDefCategory().getName()),
                                                    this.getClass().getName(),
                                                    methodName,
                                                    instanceParameterName);
            }
        }
        else if (instance instanceof Relationship)
        {
            if (instance.getType().getTypeDefCategory() != TypeDefCategory.RELATIONSHIP_DEF)
            {
                throw new InvalidParameterException(OMRSErrorCode.WRONG_TYPEDEF_CATEGORY.getMessageDefinition(instance.getGUID(),
                                                                                                              "relationship", instance.getType().getTypeDefName(), instance.getType().getTypeDefCategory().getName()),
                                                    this.getClass().getName(),
                                                    methodName,
                                                    instanceParameterName);
            }
        }
    }


    /**
     * Validates an entity proxy.  It must be a reference copy (ie owned by a different repository).
     *
     * @param sourceName source of the request (used for logging)
     * @param localMetadataCollectionId unique identifier for this repository's metadata collection
     * @param proxyParameterName name of the parameter used to provide the parameter
     * @param entityProxy proxy to add
     * @param methodName name of the method that adds the proxy
     * @throws InvalidParameterException the entity proxy is null or for an entity homed in this repository
     */
    @Override
    public void validateEntityProxy (String         sourceName,
                                     String         localMetadataCollectionId,
                                     String         proxyParameterName,
                                     EntityProxy    entityProxy,
                                     String         methodName) throws InvalidParameterException
    {
        if (entityProxy == null)
        {
            throw new InvalidParameterException(OMRSErrorCode.NULL_ENTITY_PROXY.getMessageDefinition(sourceName, proxyParameterName, methodName),
                                                this.getClass().getName(),
                                                methodName,
                                                proxyParameterName);
        }

        this.validateHomeMetadataGUID(sourceName, proxyParameterName, entityProxy.getMetadataCollectionId(), methodName);

        if (localMetadataCollectionId.equals(entityProxy.getMetadataCollectionId()))
        {
            throw new InvalidParameterException(OMRSErrorCode.LOCAL_ENTITY_PROXY.getMessageDefinition(sourceName, proxyParameterName, methodName),
                                                this.getClass().getName(),
                                                methodName,
                                                proxyParameterName);
        }

        if (entityProxy.getHeaderVersion() > InstanceAuditHeader.CURRENT_AUDIT_HEADER_VERSION)
        {
            throw new InvalidParameterException(OMRSErrorCode.UNSUPPORTED_INSTANCE_HEADER_VERSION.getMessageDefinition(methodName,
                                                                                                                       "EntityProxy",
                                                                                                                       sourceName,
                                                                                                                       entityProxy.getGUID(),
                                                                                                                       entityProxy.getType().getTypeDefName(),
                                                                                                                       Long.toString(entityProxy.getHeaderVersion()),
                                                                                                                       Long.toString(InstanceAuditHeader.CURRENT_AUDIT_HEADER_VERSION)),
                                                this.getClass().getName(),
                                                methodName,
                                                proxyParameterName);
        }
    }


    /**
     * Search for property values matching the search criteria (a regular expression)
     *
     * @param sourceName source of the request (used for logging)
     * @param properties list of properties associated with the in instance
     * @param searchCriteria regular expression for testing the property values
     * @param methodName name of the method requiring the search.
     * @return boolean indicating whether the search criteria is located in any of the string parameter values.
     * @throws RepositoryErrorException the properties are not properly set up in the instance
     */
    @Override
    public boolean verifyInstancePropertiesMatchSearchCriteria(String              sourceName,
                                                               InstanceProperties  properties,
                                                               String              searchCriteria,
                                                               String              methodName) throws RepositoryErrorException
    {
        if (properties == null)
        {
            return false;
        }

        Iterator<String>  propertyNames = properties.getPropertyNames();

        try
        {
            while (propertyNames.hasNext())
            {
                InstancePropertyValue  propertyValue = properties.getPropertyValue(propertyNames.next());

                switch (propertyValue.getInstancePropertyCategory())
                {
                    case PRIMITIVE:
                        PrimitivePropertyValue primitivePropertyValue = (PrimitivePropertyValue)propertyValue;
                        if (primitivePropertyValue.getPrimitiveDefCategory() == OM_PRIMITIVE_TYPE_STRING)
                        {
                            String   stringProperty = (String)primitivePropertyValue.getPrimitiveValue();

                            if (stringProperty != null)
                            {
                                if (stringProperty.matches(searchCriteria))
                                {
                                    return true;
                                }
                            }
                        }
                        break;

                    case ENUM:
                        EnumPropertyValue enumPropertyValue = (EnumPropertyValue)propertyValue;

                        String  enumValue = enumPropertyValue.getSymbolicName();
                        if (enumValue != null)
                        {
                            if (enumValue.matches(searchCriteria))
                            {
                                return true;
                            }
                        }
                        break;

                    case STRUCT:
                        StructPropertyValue structPropertyValue = (StructPropertyValue)propertyValue;

                        if (verifyInstancePropertiesMatchSearchCriteria(sourceName,
                                                                        structPropertyValue.getAttributes(),
                                                                        searchCriteria,
                                                                        methodName))
                        {
                            return true;
                        }
                        break;

                    case ARRAY:
                        ArrayPropertyValue arrayPropertyValue = (ArrayPropertyValue)propertyValue;

                        if (verifyInstancePropertiesMatchSearchCriteria(sourceName,
                                                                        arrayPropertyValue.getArrayValues(),
                                                                        searchCriteria,
                                                                        methodName))
                        {
                            return true;
                        }
                        break;

                    case MAP:
                        MapPropertyValue mapPropertyValue = (MapPropertyValue)propertyValue;

                        if (verifyInstancePropertiesMatchSearchCriteria(sourceName,
                                                                        mapPropertyValue.getMapValues(),
                                                                        searchCriteria,
                                                                        methodName))
                        {
                            return true;
                        }
                        break;
                }
            }
        }
        catch (Exception   error)
        {
            /*
             * Probably a class cast error which should never occur.
             */
            throw new RepositoryErrorException(OMRSErrorCode.BAD_PROPERTY_FOR_INSTANCE.getMessageDefinition(error.getClass().getName(),
                                                                                                            searchCriteria,
                                                                                                            methodName,
                                                                                                            sourceName,
                                                                                                            error.getMessage()),
                                               this.getClass().getName(),
                                               methodName,
                                               error);
        }

        return false;
    }


    /**
     * Search for property values matching the supplied property value
     *
     * @param sourceName source of the request (used for logging)
     * @param properties list of properties associated with the in instance
     * @param searchPropertyValue property value as a string
     * @param methodName name of the method requiring the search.
     * @return boolean indicating whether the search criteria is located in any of the string parameter values.
     * @throws RepositoryErrorException the properties are not properly set up in the instance
     */
    @Override
    public boolean verifyInstancePropertiesMatchPropertyValue(String              sourceName,
                                                              InstanceProperties  properties,
                                                              String              searchPropertyValue,
                                                              String              methodName) throws RepositoryErrorException
    {
        if (properties == null)
        {
            return false;
        }

        Iterator<String>  propertyNames = properties.getPropertyNames();

        try
        {
            while (propertyNames.hasNext())
            {
                InstancePropertyValue  instancePropertyValue = properties.getPropertyValue(propertyNames.next());

                switch (instancePropertyValue.getInstancePropertyCategory())
                {
                    case PRIMITIVE:
                        PrimitivePropertyValue primitivePropertyValue = (PrimitivePropertyValue)instancePropertyValue;
                        Object    instanceProperty = primitivePropertyValue.getPrimitiveValue();
                        String    stringProperty = null;

                        if (instanceProperty != null)
                        {
                            stringProperty = instanceProperty.toString();
                        }

                        if (stringProperty != null)
                        {
                            if (stringProperty.equals(searchPropertyValue))
                            {
                                return true;
                            }
                        }
                        break;

                    case ENUM:
                        EnumPropertyValue enumPropertyValue = (EnumPropertyValue)instancePropertyValue;

                        String  enumValue = enumPropertyValue.getSymbolicName();
                        if (enumValue != null)
                        {
                            if (enumValue.equals(searchPropertyValue))
                            {
                                return true;
                            }
                        }
                        break;

                    case STRUCT:
                        StructPropertyValue structPropertyValue = (StructPropertyValue)instancePropertyValue;

                        if (verifyInstancePropertiesMatchPropertyValue(sourceName,
                                                                       structPropertyValue.getAttributes(),
                                                                       searchPropertyValue,
                                                                       methodName))
                        {
                            return true;
                        }
                        break;

                    case ARRAY:
                        ArrayPropertyValue arrayPropertyValue = (ArrayPropertyValue)instancePropertyValue;

                        if (verifyInstancePropertiesMatchPropertyValue(sourceName,
                                                                       arrayPropertyValue.getArrayValues(),
                                                                       searchPropertyValue,
                                                                       methodName))
                        {
                            return true;
                        }
                        break;

                    case MAP:
                        MapPropertyValue mapPropertyValue = (MapPropertyValue)instancePropertyValue;

                        if (verifyInstancePropertiesMatchPropertyValue(sourceName,
                                                                       mapPropertyValue.getMapValues(),
                                                                       searchPropertyValue,
                                                                       methodName))
                        {
                            return true;
                        }
                        break;
                }
            }
        }
        catch (Exception   error)
        {
            /*
             * Probably a class cast error which should never occur.
             */
            throw new RepositoryErrorException(OMRSErrorCode.BAD_PROPERTY_FOR_INSTANCE.getMessageDefinition(error.getClass().getName(),
                                                                                                            searchPropertyValue,
                                                                                                            methodName,
                                                                                                            sourceName,
                                                                                                            error.getMessage()),
                                               this.getClass().getName(),
                                               methodName,
                                               error);
        }

        return false;
    }


    /**
     * Throw a logic error exception if this object does not have a repository content manager.
     * This would occur if it is being used in an environment where the OMRS has not been properly
     * initialized.
     *
     * @param methodName name of calling method.
     */
    private void validateRepositoryContentManager(String   methodName)
    {
        if (repositoryContentManager == null)
        {
            throw new OMRSLogicErrorException(OMRSErrorCode.LOCAL_REPOSITORY_CONFIGURATION_ERROR.getMessageDefinition(),
                                              this.getClass().getName(),
                                              methodName);
        }
    }


    /**
     * Throws a logic error exception when the repository validator is called with invalid parameters.
     * Normally this means the repository validator methods have been called in the wrong order.
     *
     * @param sourceName source of the request (used for logging)
     * @param originatingMethodName method that called the repository validator
     * @param localMethodName local method that detected the error
     */
    private void throwValidatorLogicError(String     sourceName,
                                          String     originatingMethodName,
                                          String     localMethodName)
    {
       throw new OMRSLogicErrorException(OMRSErrorCode.VALIDATION_LOGIC_ERROR.getMessageDefinition(sourceName,
                                                                                                    localMethodName,
                                                                                                    originatingMethodName),
                                          this.getClass().getName(),
                                          localMethodName);
    }


    /**
     * Returns a boolean indicating that the instance is of the supplied type.  It tests the
     * base type and all the super types.
     *
     * @param sourceName source of the request (used for logging)
     * @param instance instance to test
     * @param typeName name of the type
     * @param localMethodName  local method that is calling isATypeOf
     * @return true if typeName is instance type or a supertype of it, otherwise false
     */
    @Override
    public boolean isATypeOf(String               sourceName,
                             InstanceAuditHeader  instance,
                             String               typeName,
                             String               localMethodName)
    {
        final String   methodName = "isATypeOf";

        if (typeName == null)
        {
            throwValidatorLogicError(sourceName, methodName, localMethodName);
        }

        if (instance == null)
        {
            throwValidatorLogicError(sourceName, methodName, localMethodName);
        }

        InstanceType   instanceType = instance.getType();

        if (instanceType != null)
        {
            String   entityTypeName = instanceType.getTypeDefName();

            if (typeName.equals(entityTypeName))
            {
                return true;
            }

            List<TypeDefLink> superTypes = repositoryContentManager.getSuperTypes(sourceName, entityTypeName, methodName);

            if (superTypes != null)
            {
                for (TypeDefLink   typeDefLink : superTypes)
                {
                    if (typeDefLink != null)
                    {
                        if (typeName.equals(typeDefLink.getName()))
                        {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }


    /**
     * Validate that either zero or one entity were returned from a find request.  This is typically when searching
     * for entities of a specific type using one of its unique properties.
     *
     * @param findResults list of entities returned from the search.
     * @param typeName name of the type of entities requested.
     * @param serviceName service that requested the entities.
     * @param methodName calling method.
     */
    @Override
    public void validateAtMostOneEntityResult(List<EntityDetail>   findResults,
                                              String               typeName,
                                              String               serviceName,
                                              String               methodName) throws RepositoryErrorException
    {
        if (findResults != null)
        {
            if (findResults.size() > 1)
            {
                throw new RepositoryErrorException(OMRSErrorCode.MULTIPLE_ENTITIES_FOUND.getMessageDefinition(typeName,
                                                                                                              serviceName,
                                                                                                              methodName,
                                                                                                              findResults.toString()),
                                                   this.getClass().getName(),
                                                   methodName);
            }
        }
    }


    /**
     * Validate that either zero or one relationship were returned from a find request.  This is typically when searching
     * for relationships of a specific type where the cardinality is set to AT_MOST_ONE in the RelationshipEndCardinality.
     *
     * @param findResults list of relationships returned from the search.
     * @param typeName name of the type of relationships requested.
     * @param serviceName service that requested the relationships.
     * @param methodName calling method.
     */
    @Override
    public void validateAtMostOneRelationshipResult(List<Relationship>   findResults,
                                                    String               typeName,
                                                    String               serviceName,
                                                    String               methodName) throws RepositoryErrorException
    {
        if (findResults != null)
        {
            if (findResults.size() > 1)
            {
                throw new RepositoryErrorException(OMRSErrorCode.MULTIPLE_RELATIONSHIPS_FOUND.getMessageDefinition(typeName,
                                                                                                                   serviceName,
                                                                                                                   methodName,
                                                                                                                   findResults.toString()),
                                                   this.getClass().getName(),
                                                   methodName);
            }
        }
    }
}
