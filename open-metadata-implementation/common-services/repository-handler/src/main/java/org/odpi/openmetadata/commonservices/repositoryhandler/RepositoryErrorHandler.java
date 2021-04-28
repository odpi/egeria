/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.repositoryhandler;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * RepositoryErrorHandler provides common validation routines for the other handler classes
 */
public class RepositoryErrorHandler
{
    private String               serviceName;
    private String               serverName;
    private OMRSRepositoryHelper repositoryHelper;
    private AuditLog             auditLog = null;

    /**
     * Typical constructor providing access to the repository connector for this access service.
     *
     * @param repositoryHelper  access to the repository helper.
     * @param serviceName  name of this access service
     * @param serverName  name of this server
     */
    public RepositoryErrorHandler(OMRSRepositoryHelper repositoryHelper,
                                  String               serviceName,
                                  String               serverName)
    {
        this.repositoryHelper = repositoryHelper;
        this.serviceName = serviceName;
        this.serverName = serverName;
    }


    /**
     * Typical constructor providing access to the repository connector for this access service.
     *
     * @param repositoryHelper  access to the repository helper.
     * @param serviceName  name of this access service
     * @param serverName  name of this server
     * @param auditLog logging destination
     */
    public RepositoryErrorHandler(OMRSRepositoryHelper repositoryHelper,
                                  String               serviceName,
                                  String               serverName,
                                  AuditLog             auditLog)
    {
        this.repositoryHelper = repositoryHelper;
        this.serviceName = serviceName;
        this.serverName = serverName;
        this.auditLog = auditLog;
    }

    /**
     * Check that there is a repository connector.
     *
     * @param methodName  name of the method being called
     * @param repositoryConnector  connector object
     *
     * @throws PropertyServerException exception thrown if the repository connector
     */
    public void validateRepositoryConnector(OMRSRepositoryConnector   repositoryConnector,
                                            String                    methodName) throws PropertyServerException
    {
        if (repositoryConnector == null)
        {
            throw new PropertyServerException(RepositoryHandlerErrorCode.OMRS_NOT_INITIALIZED.getMessageDefinition(methodName),
                                              this.getClass().getName(),
                                              methodName);
        }

        if (! repositoryConnector.isActive())
        {
            throw new PropertyServerException(RepositoryHandlerErrorCode.OMRS_NOT_AVAILABLE.getMessageDefinition(methodName),
                                              this.getClass().getName(),
                                              methodName);
        }

        try
        {
            repositoryConnector.getMetadataCollection();
        }
        catch (Throwable error)
        {
            throw new PropertyServerException(RepositoryHandlerErrorCode.NO_METADATA_COLLECTION.getMessageDefinition(methodName),
                                              this.getClass().getName(),
                                              methodName);
        }
    }


    /**
     * Compare the retrieved properties with the validating properties supplied by the caller.
     *
     * @param instanceGUID unique identifier of the instance where the properties came from
     * @param validatingPropertyName name of property that should be in the entity if we have the correct one.
     * @param validatingProperty value of property that should be in the entity if we have the correct one.
     * @param retrievedProperties properties retrieved from the repository
     * @param methodName calling method
     *
     * @throws InvalidParameterException mismatch on properties
     */
    void    validateProperties(String               instanceGUID,
                               String               validatingPropertyName,
                               String               validatingProperty,
                               InstanceProperties   retrievedProperties,
                               String               methodName) throws InvalidParameterException
    {
        if ((validatingPropertyName != null) && (validatingProperty != null) && (retrievedProperties != null))
        {
            Map<String, InstancePropertyValue> instancePropertyValueMap = retrievedProperties.getInstanceProperties();
            InstancePropertyValue retrievedPropertyValue = instancePropertyValueMap.get(validatingPropertyName);

            if (! validatingProperty.equals(retrievedPropertyValue.valueAsString()))
            {
                throw new InvalidParameterException(
                        RepositoryHandlerErrorCode.INVALID_PROPERTY_VALUE.getMessageDefinition(validatingPropertyName,
                                                                                               validatingProperty,
                                                                                               methodName,
                                                                                               retrievedPropertyValue.valueAsString(),
                                                                                               instanceGUID),
                        this.getClass().getName(),
                        methodName,
                        validatingPropertyName);
            }
        }
    }


    /**
     * Verify that the external source supplied is able to update/delete the instance.  This is determined by the metadata provenance of
     * the instance.
     *
     * @param userId calling user
     * @param instanceHeader header of the entity, relationship or classification
     * @param uniqueIdentifier identifier of the owning entity or relationship - ie if the header is a classification, the unique identifier is
     *                         from the entity.
     * @param externalSourceGUID unique identifier for the external source - or null for local
     * @param externalSourceName unique name for the external source - optional - supplied for error messages
     * @param methodName calling method
     *
     * @throws UserNotAuthorizedException the provenance does not allow the update
     */
    void validateProvenance(String              userId,
                            InstanceAuditHeader instanceHeader,
                            String              uniqueIdentifier,
                            String              externalSourceGUID,
                            String              externalSourceName,
                            String              methodName) throws UserNotAuthorizedException
    {
        /*
         * If no header is provided then just return.
         */
        if (instanceHeader == null)
        {
            return;
        }

        if ((instanceHeader.getInstanceProvenanceType() == null) ||
            (instanceHeader.getInstanceProvenanceType() == InstanceProvenanceType.LOCAL_COHORT))
        {
            /*
             * Local cohort instances can be updated by any caller provided they have the right security (tested elsewhere).
             */
            return;
        }

        /*
         * The instance is owned by some sort of external process.  This means we need to be fussy about the external identifiers of the caller.
         */
        if (externalSourceGUID != null)
        {
            if (! externalSourceGUID.equals(instanceHeader.getMetadataCollectionId()))
            {
                /*
                 * Subject Area OMAS manufactures entities that do not fill out the provenance information.
                 */
                InstanceProvenanceType instanceProvenanceType = InstanceProvenanceType.LOCAL_COHORT;

                if ((instanceHeader.getInstanceProvenanceType() != null))
                {
                    instanceProvenanceType = instanceHeader.getInstanceProvenanceType();
                }

                throw new UserNotAuthorizedException(
                        RepositoryHandlerErrorCode.WRONG_EXTERNAL_SOURCE.getMessageDefinition(methodName,
                                                                                              externalSourceGUID,
                                                                                              externalSourceName,
                                                                                              instanceHeader.getType().getTypeDefCategory().getName(),
                                                                                              uniqueIdentifier,
                                                                                              instanceProvenanceType.getName(),
                                                                                              instanceHeader.getMetadataCollectionId(),
                                                                                              instanceHeader.getMetadataCollectionName()),
                        this.getClass().getName(),
                        methodName,
                        userId);
            }
        }
        else /* local cohort caller updating a non-local entity */
        {
            /*
             * Subject Area OMAS manufactures entities that do not fill out the provenance information.
             */
            InstanceProvenanceType instanceProvenanceType = InstanceProvenanceType.LOCAL_COHORT;

            if ((instanceHeader.getInstanceProvenanceType() != null))
            {
                instanceProvenanceType = instanceHeader.getInstanceProvenanceType();
            }

            throw new UserNotAuthorizedException(
                    RepositoryHandlerErrorCode.LOCAL_CANNOT_CHANGE_EXTERNAL.getMessageDefinition(methodName,
                                                                                                 instanceHeader.getType().getTypeDefCategory().getName(),
                                                                                                 uniqueIdentifier,
                                                                                                 instanceProvenanceType.getName(),
                                                                                                 instanceHeader.getMetadataCollectionId(),
                                                                                                 instanceHeader.getMetadataCollectionName(),
                                                                                                 userId),
                        this.getClass().getName(),
                        methodName,
                    userId);

        }
    }


    /**
     * Verify whether an instance is of a particular type or not.
     *
     * @param instanceHeader the entity or relationship header.
     * @param expectedTypeName name of the type to test for
     * @param methodName calling method
     * @param localMethodName name of repository handler method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     */
    void validateInstanceType(InstanceHeader instanceHeader,
                              String         expectedTypeName,
                              String         methodName,
                              String         localMethodName) throws InvalidParameterException
    {
        if (instanceHeader != null)
        {
            InstanceType type = instanceHeader.getType();
            if (type != null)
            {
                if (! repositoryHelper.isTypeOf(methodName, type.getTypeDefName(), expectedTypeName))
                {
                    this.handleWrongTypeForGUIDException(instanceHeader.getGUID(),
                                                         methodName,
                                                         type.getTypeDefName(),
                                                         expectedTypeName,
                                                         localMethodName);
                }
            }
        }
    }



    /**
     * Test whether an instance is of a particular type or not.
     *
     * @param instanceHeader the entity or relationship header.
     * @param entityTypeName name of the type to test for
     * @param methodName calling method
     *
     * @return boolean flag
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the entity.
     */
    boolean isInstanceATypeOf(InstanceHeader         instanceHeader,
                              String                 entityTypeName,
                              String                 methodName) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        if (instanceHeader != null)
        {
            InstanceType type = instanceHeader.getType();
            if (type != null)
            {
                return repositoryHelper.isTypeOf(methodName, type.getTypeDefName(), entityTypeName);
            }
        }

        return false;
    }


    /**
     * Throw an exception if the supplied guid returned an entity of the wrong type
     *
     * @param guid  unique identifier of entity
     * @param methodName  name of the method making the call.
     * @param actualType  type of retrieved entity
     * @param expectedType  type the entity should be
     * @param localMethodName name of repository handler method
     * @throws InvalidParameterException the guid is for the wrong type of object
     */
    void handleWrongTypeForGUIDException(String guid,
                                         String methodName,
                                         String actualType,
                                         String expectedType,
                                         String localMethodName) throws InvalidParameterException
    {
        final String  defaultGUIDParameterName = "guid";

        throw new InvalidParameterException(RepositoryHandlerErrorCode.INSTANCE_WRONG_TYPE_FOR_GUID.getMessageDefinition(localMethodName,
                                                                                                                         guid,
                                                                                                                         actualType,
                                                                                                                         expectedType,
                                                                                                                         methodName),
                                            this.getClass().getName(),
                                            methodName,
                                            defaultGUIDParameterName);

    }


    /**
     * Report an error where an entity is being created with the same qualified name as an existing entity
     * of the same type.
     *
     * @param typeName name of the type
     * @param qualifiedName clashing qualified name
     * @param existingEntityGUID existing entity found in the repository
     * @param methodName calling method
     * @throws InvalidParameterException exception that reports this error
     */
    public void handleDuplicateCreateRequest(String typeName,
                                             String qualifiedName,
                                             String existingEntityGUID,
                                             String methodName) throws InvalidParameterException
    {
        final String  parameterName = "qualifiedName";

        throw new InvalidParameterException(RepositoryHandlerErrorCode.DUPLICATE_CREATE_REQUEST.getMessageDefinition(typeName,
                                                                                                                     parameterName,
                                                                                                                     qualifiedName,
                                                                                                                     existingEntityGUID),
                                            this.getClass().getName(),
                                            methodName,
                                            parameterName);
    }


    /**
     * Throw an exception if the supplied userId is not authorized to perform a request
     *
     * @param userId  user name to validate
     * @param methodName  name of the method making the call.
     *
     * @throws UserNotAuthorizedException the userId is unauthorised for the request
     */
    public void handleUnauthorizedUser(String userId,
                                       String methodName) throws UserNotAuthorizedException
    {
        throw new UserNotAuthorizedException(RepositoryHandlerErrorCode.USER_NOT_AUTHORIZED.getMessageDefinition(userId,
                                                                                                                 methodName,
                                                                                                                 serviceName,
                                                                                                                 serverName),
                                             this.getClass().getName(),
                                             methodName,
                                             userId);
    }


    /**
     * Throw an exception if the supplied property is not supported
     *
     * @param error  caught exception
     * @param methodName  name of the method making the call
     * @param propertyName  name of the property in error
     *
     * @throws InvalidParameterException invalid property
     */
    public void handleUnsupportedProperty(Throwable  error,
                                          String     methodName,
                                          String     propertyName) throws InvalidParameterException
    {
        throw new InvalidParameterException(RepositoryHandlerErrorCode.INVALID_PROPERTY.getMessageDefinition(propertyName,
                                                                                                             methodName,
                                                                                                             serviceName,
                                                                                                             serverName,
                                                                                                             error.getMessage()),
                                            this.getClass().getName(),
                                            methodName,
                                            error,
                                            propertyName);
    }


    /**
     * Throw an exception if the supplied parameter is invalid.
     *
     * @param methodName  name of the method making the call
     * @param parameterName  name of the parameter in error
     * @param parameterValue value of the parameter
     *
     * @throws InvalidParameterException invalid property
     */
    public void handleUnsupportedParameter(String     methodName,
                                           String     parameterName,
                                           String     parameterValue) throws InvalidParameterException
    {
        throw new InvalidParameterException(RepositoryHandlerErrorCode.INVALID_PARAMETER.getMessageDefinition(parameterName,
                                                                                                              methodName,
                                                                                                              serviceName,
                                                                                                              serverName,
                                                                                                              parameterValue),
                                            this.getClass().getName(),
                                            methodName,
                                            parameterName);
    }


    /**
     * Throw an exception if the supplied type name is not supported i nthe metadta
     *
     * @param error  caught exception
     * @param methodName  name of the method making the call
     * @param typeName  name of the property in error
     *
     * @throws InvalidParameterException invalid property
     */
    public void handleUnsupportedType(Throwable  error,
                                      String     methodName,
                                      String     typeName) throws InvalidParameterException
    {
        throw new InvalidParameterException(RepositoryHandlerErrorCode.INVALID_TYPE.getMessageDefinition(typeName,
                                                                                                         methodName,
                                                                                                         serviceName,
                                                                                                         serverName,
                                                                                                         error.getMessage()),
                                            this.getClass().getName(),
                                            methodName,
                                            error,
                                            typeName);
    }


    /**
     * Throw an exception if the supplied userId is not authorized to perform a request
     *
     * @param error  caught exception
     * @param methodName  name of the method making the call
     * @param typeName  name of the property in error
     * @throws PropertyServerException no audit log
     */
    public void handleUnsupportedAnchorsType(Exception  error,
                                             String     methodName,
                                             String     typeName) throws PropertyServerException
    {
        if (auditLog != null)
        {
            auditLog.logException(methodName, RepositoryHandlerAuditCode.UNABLE_TO_SET_ANCHORS.getMessageDefinition(serviceName,
                                                                                                                    typeName,
                                                                                                                    methodName,
                                                                                                                    error.getClass().getName(),
                                                                                                                    error.getMessage()),
                                  error);
        }
        else
        {
            throw new PropertyServerException(RepositoryHandlerErrorCode.UNABLE_TO_SET_ANCHORS.getMessageDefinition(serviceName,
                                                                                                                    typeName,
                                                                                                                    methodName,
                                                                                                                    error.getClass().getName(),
                                                                                                                    error.getMessage()),
                                              error.getClass().getName(),
                                              methodName,
                                              error);
        }
    }


    /**
     * Throw an exception if an unexpected repository error is received
     *
     * @param error  caught exception
     * @param methodName  name of the method making the call.
     *
     * @throws PropertyServerException unexpected exception from property server
     */
    public void handleRepositoryError(Throwable  error,
                                      String     methodName) throws PropertyServerException
    {
        final String localMethodName = "handleRepositoryError";

        handleRepositoryError(error, methodName, localMethodName);
    }


    /**
     * Throw an exception if an unexpected repository error is received
     *
     * @param error  caught exception
     * @param methodName  name of the method called by the external party
     * @param localMethodName name of method that called this error
     *
     * @throws PropertyServerException unexpected exception from property server
     */
    public void handleRepositoryError(Throwable  error,
                                      String     methodName,
                                      String     localMethodName) throws PropertyServerException
    {
        if (auditLog != null)
        {
            auditLog.logException(methodName,
                                  RepositoryHandlerAuditCode.PROPERTY_SERVER_ERROR.getMessageDefinition(error.getMessage(),
                                                                                                        methodName,
                                                                                                        serviceName,
                                                                                                        serverName,
                                                                                                        error.getClass().getName(),
                                                                                                        localMethodName),
                                  error);
        }

        throw new PropertyServerException(RepositoryHandlerErrorCode.PROPERTY_SERVER_ERROR.getMessageDefinition(error.getMessage(),
                                                                                                                methodName,
                                                                                                                serviceName,
                                                                                                                serverName,
                                                                                                                error.getClass().getName(),
                                                                                                                localMethodName),
                                          this.getClass().getName(),
                                          methodName);
    }





    /**
     * Throw an exception if there is a problem with the entity guid
     *
     * @param error  caught exception
     * @param entityGUID  unique identifier for the requested entity
     * @param entityTypeName expected type of asset
     * @param methodName  name of the method making the call
     * @param guidParameterName name of the parameter that passed the GUID.
     *
     * @throws InvalidParameterException unexpected exception from property server
     */
    public void handleUnknownEntity(Throwable  error,
                                    String     entityGUID,
                                    String     entityTypeName,
                                    String     methodName,
                                    String     guidParameterName) throws InvalidParameterException
    {
        throw new InvalidParameterException(RepositoryHandlerErrorCode.UNKNOWN_ENTITY.getMessageDefinition(entityTypeName,
                                                                                                           entityGUID,
                                                                                                           methodName,
                                                                                                           serviceName,
                                                                                                           serverName,
                                                                                                           error.getMessage()),
                                            this.getClass().getName(),
                                            methodName,
                                            error,
                                            guidParameterName);

    }


    /**
     * Throw an exception if there is a problem with the relationship guid
     *
     * @param error  caught exception
     * @param relationshipGUID  unique identifier for the requested entity
     * @param relationshipTypeName expected type of asset
     * @param methodName  name of the method making the call
     * @param guidParameterName name of the parameter that passed the GUID.
     *
     * @throws InvalidParameterException unexpected exception from property server
     */
    public void handleUnknownRelationship(Throwable error,
                                          String    relationshipGUID,
                                          String    relationshipTypeName,
                                          String    methodName,
                                          String    guidParameterName) throws InvalidParameterException
    {
        String typeName = "<Unknown>";

        if (relationshipTypeName != null)
        {
            typeName = relationshipTypeName;
        }

        throw new InvalidParameterException(RepositoryHandlerErrorCode.UNKNOWN_RELATIONSHIP.getMessageDefinition(typeName,
                                                                                                                 relationshipGUID,
                                                                                                                 methodName,
                                                                                                                 serviceName,
                                                                                                                 serverName,
                                                                                                                 error.getMessage()),
                                            this.getClass().getName(),
                                            methodName,
                                            error,
                                            guidParameterName);

    }



    /**
     * Throw an exception if there is a problem with the asset guid
     *
     * @param error  caught exception
     * @param entityGUID  unique identifier for the requested entity
     * @param entityTypeName expected type of asset
     * @param methodName  name of the method making the call
     * @param guidParameterName name of the parameter that passed the GUID.
     *
     * @throws InvalidParameterException unexpected exception from property server
     */
    public void handleEntityProxy(Throwable  error,
                                  String     entityGUID,
                                  String     entityTypeName,
                                  String     methodName,
                                  String     guidParameterName) throws InvalidParameterException
    {
        throw new InvalidParameterException(RepositoryHandlerErrorCode.PROXY_ENTITY_FOUND.getMessageDefinition(entityTypeName,
                                                                                                               entityGUID,
                                                                                                               serverName,
                                                                                                               error.getMessage()),
                                            this.getClass().getName(),
                                            methodName,
                                            error,
                                            guidParameterName);
    }


    /**
     * Throw an exception if multiple relationships are returned when not expected.
     *
     * @param entityGUID  unique identifier for the anchor entity
     * @param entityTypeName  name of the entity's type
     * @param relationshipTypeName expected type of relationship
     * @param returnedRelationships list of relationships returned
     * @param methodName  name of the method making the call
     *
     * @throws PropertyServerException unexpected response from property server
     */
    public void handleAmbiguousRelationships(String             entityGUID,
                                             String             entityTypeName,
                                             String             relationshipTypeName,
                                             List<Relationship> returnedRelationships,
                                             String             methodName) throws PropertyServerException
    {
        List<String>  relationshipGUIDs = new ArrayList<>();

        if (returnedRelationships != null)
        {
            for (Relationship  relationship: returnedRelationships)
            {
                if (relationship != null)
                {
                    relationshipGUIDs.add(relationship.getGUID());
                }
            }
        }

        throw new PropertyServerException(RepositoryHandlerErrorCode.MULTIPLE_RELATIONSHIPS_FOUND.getMessageDefinition(relationshipTypeName,
                                                                                                                       entityTypeName,
                                                                                                                       entityGUID,
                                                                                                                       relationshipGUIDs.toString(),
                                                                                                                       methodName,
                                                                                                                       serverName),
                                          this.getClass().getName(),
                                          methodName);
    }


    /**
     * Throw an exception if multiple entities are returned when not expected.
     *
     * @param name  requested name for the entity
     * @param nameParameterName  name of the parameter
     * @param entityTypeName  name of the entity's type
     * @param returnedEntities list of entities returned
     * @param methodName  name of the method making the call
     *
     * @throws PropertyServerException unexpected response from property server
     */
    public void handleAmbiguousEntityName(String             name,
                                          String             nameParameterName,
                                          String             entityTypeName,
                                          List<EntityDetail> returnedEntities,
                                          String             methodName) throws PropertyServerException
    {
        List<String>  entityGUIDs = new ArrayList<>();

        if (returnedEntities != null)
        {
            for (EntityDetail  entity: returnedEntities)
            {
                if (entity != null)
                {
                    entityGUIDs.add(entity.getGUID());
                }
            }
        }

        throw new PropertyServerException(RepositoryHandlerErrorCode.MULTIPLE_ENTITIES_FOUND.getMessageDefinition(entityTypeName,
                                                                                                                  name,
                                                                                                                  entityGUIDs.toString(),
                                                                                                                  methodName,
                                                                                                                  nameParameterName,
                                                                                                                  serverName),
                                          this.getClass().getName(),
                                          methodName);
    }


    /**
     * Throw an exception if multiple entities are returned when not expected.
     *
     * @param name  requested name for the entity
     * @param nameParameterName  name of the parameter
     * @param entityTypeName  name of the entity's type
     * @param returnedEntityGUIDs list of entities returned
     * @param methodName  name of the method making the call
     *
     * @throws PropertyServerException unexpected response from property server
     */
    public  void handleAmbiguousName(String             name,
                                     String             nameParameterName,
                                     String             entityTypeName,
                                     List<String>       returnedEntityGUIDs,
                                     String             methodName) throws PropertyServerException
    {
        throw new PropertyServerException(RepositoryHandlerErrorCode.MULTIPLE_ENTITIES_FOUND.getMessageDefinition(entityTypeName,
                                                                                                                name,
                                                                                                                returnedEntityGUIDs.toString(),
                                                                                                                methodName,
                                                                                                                nameParameterName,
                                                                                                                serverName),
                                          this.getClass().getName(),
                                          methodName);
    }


    /**
     * Throw an exception if no relationships are returned when not expected.
     *
     * @param entityGUID  unique identifier for the anchor entity
     * @param entityTypeName  name of the entity's type
     * @param relationshipTypeName expected type of relationship
     * @param methodName  name of the method making the call
     *
     * @throws PropertyServerException unexpected response from property server
     */
    public void handleNoRelationship(String             entityGUID,
                                     String             entityTypeName,
                                     String             relationshipTypeName,
                                     String             methodName) throws PropertyServerException
    {
       throw new PropertyServerException(RepositoryHandlerErrorCode.NO_RELATIONSHIPS_FOUND.getMessageDefinition(relationshipTypeName,
                                                                                                                 entityTypeName,
                                                                                                                 entityGUID,
                                                                                                                 methodName,
                                                                                                                 serverName),
                                          this.getClass().getName(),
                                          methodName);
    }


    /**
     * Throw an exception if it is not possible to create an entity.
     *
     * @param entityTypeGUID  unique identifier for the entity's type
     * @param entityTypeName  name of the entity's type
     * @param properties properties
     * @param methodName  name of the method making the call
     *
     * @throws PropertyServerException unexpected response from property server
     */
    public void handleNoEntity(String             entityTypeGUID,
                               String             entityTypeName,
                               InstanceProperties properties,
                               String             methodName) throws PropertyServerException
    {
        String propertiesString = "<null>";

        if (properties != null)
        {
            propertiesString = properties.toString();
        }

        throw new PropertyServerException(RepositoryHandlerErrorCode.NULL_ENTITY_RETURNED.getMessageDefinition(methodName,
                                                                                                               serverName,
                                                                                                               entityTypeName,
                                                                                                               entityTypeGUID,
                                                                                                               propertiesString),
                                          this.getClass().getName(),
                                          methodName);

    }


    /**
     * Throw an exception if it is not possible to update an entity.
     *
     * @param entityGUID unique identifier of entity
     * @param classificationTypeGUID  unique identifier for the classification's type
     * @param classificationTypeName  name of the classification's type
     * @param properties properties
     * @param methodName  name of the method making the call
     *
     * @throws PropertyServerException unexpected response from property server
     */
    public void handleNoEntityForClassification(String             entityGUID,
                                                String             classificationTypeGUID,
                                                String             classificationTypeName,
                                                InstanceProperties properties,
                                                String             methodName) throws PropertyServerException
    {
        String propertiesString = "<null>";

        if (properties != null)
        {
            propertiesString = properties.toString();
        }

        throw new PropertyServerException(RepositoryHandlerErrorCode.NULL_ENTITY_RETURNED_FOR_CLASSIFICATION.getMessageDefinition(methodName,
                                                                                                                                  serverName,
                                                                                                                                  entityGUID,
                                                                                                                                  classificationTypeName,
                                                                                                                                  classificationTypeGUID,
                                                                                                                                  propertiesString),
                                          this.getClass().getName(),
                                          methodName);
    }
}