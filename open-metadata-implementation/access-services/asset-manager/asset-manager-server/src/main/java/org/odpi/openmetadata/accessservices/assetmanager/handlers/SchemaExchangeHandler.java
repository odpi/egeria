/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.handlers;


import org.odpi.openmetadata.accessservices.assetmanager.converters.ElementHeaderConverter;
import org.odpi.openmetadata.accessservices.assetmanager.converters.SchemaAttributeConverter;
import org.odpi.openmetadata.accessservices.assetmanager.converters.SchemaTypeConverter;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.*;
import org.odpi.openmetadata.accessservices.assetmanager.properties.*;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.generichandlers.*;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryRelationshipsIterator;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementHeader;
import org.odpi.openmetadata.frameworks.governanceaction.properties.MetadataCorrelationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.enums.KeyPattern;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;

import java.util.Date;
import java.util.List;

/**
 * SchemaExchangeHandler is the server side handler for managing schema types and attributes.
 */
public class SchemaExchangeHandler extends ExchangeHandlerBase
{
    private final SchemaTypeHandler<SchemaTypeElement>                              schemaTypeHandler;
    private final SchemaAttributeHandler<SchemaAttributeElement, SchemaTypeElement> schemaAttributeHandler;

    private final static String schemaTypeGUIDParameterName      = "schemaTypeGUID";
    private final static String schemaAttributeGUIDParameterName = "schemaAttributeGUID";

    /**
     * Construct the schema exchange handler with information needed to work with schema related objects
     * for Asset Manager OMAS.
     *
     * @param serviceName      name of this service
     * @param serverName       name of the local server
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler     manages calls to the repository services
     * @param repositoryHelper provides utilities for manipulating the repository services objects
     * @param localServerUserId userId for this server
     * @param securityVerifier open metadata security services verifier
     * @param supportedZones list of zones that the access service is allowed to serve instances from.
     * @param defaultZones list of zones that the access service should set in all new instances.
     * @param publishZones list of zones that the access service sets up in published instances.
     * @param auditLog destination for audit log events.
     */
    public SchemaExchangeHandler(String                             serviceName,
                                 String                             serverName,
                                 InvalidParameterHandler            invalidParameterHandler,
                                 RepositoryHandler                  repositoryHandler,
                                 OMRSRepositoryHelper               repositoryHelper,
                                 String                             localServerUserId,
                                 OpenMetadataServerSecurityVerifier securityVerifier,
                                 List<String>                       supportedZones,
                                 List<String>                       defaultZones,
                                 List<String>                       publishZones,
                                 AuditLog                           auditLog)
    {
        super(serviceName,
              serverName,
              invalidParameterHandler,
              repositoryHandler,
              repositoryHelper,
              localServerUserId,
              securityVerifier,
              supportedZones,
              defaultZones,
              publishZones,
              auditLog);

        schemaTypeHandler = new SchemaTypeHandler<>(new SchemaTypeConverter<>(repositoryHelper, serviceName, serverName),
                                                  SchemaTypeElement.class,
                                                  serviceName,
                                                  serverName,
                                                  invalidParameterHandler,
                                                  repositoryHandler,
                                                  repositoryHelper,
                                                  localServerUserId,
                                                  securityVerifier,
                                                  supportedZones,
                                                  defaultZones,
                                                  publishZones,
                                                  auditLog);

        schemaAttributeHandler = new SchemaAttributeHandler<>(new SchemaAttributeConverter<>(repositoryHelper, serviceName, serverName),
                                                               SchemaAttributeElement.class,
                                                              new SchemaTypeConverter<>(repositoryHelper, serviceName, serverName),
                                                              SchemaTypeElement.class,
                                                               serviceName,
                                                               serverName,
                                                               invalidParameterHandler,
                                                               repositoryHandler,
                                                               repositoryHelper,
                                                               localServerUserId,
                                                               securityVerifier,
                                                               supportedZones,
                                                               defaultZones,
                                                               publishZones,
                                                               auditLog);
    }



    /* ========================================================
     * Managing the externalIds and related correlation properties.
     */



    /**
     * Update each returned element with details of the correlation properties for the supplied asset manager.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param results list of elements
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    private void addCorrelationPropertiesToSchemaTypes(String                  userId,
                                                       String                  assetManagerGUID,
                                                       String                  assetManagerName,
                                                       List<SchemaTypeElement> results,
                                                       boolean                 forLineage,
                                                       boolean                 forDuplicateProcessing,
                                                       Date                    effectiveTime,
                                                       String                  methodName) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        if (results != null)
        {
            for (SchemaTypeElement element : results)
            {
                if ((element != null) && (element.getElementHeader() != null) && (element.getElementHeader().getGUID() != null))
                {
                    element.setCorrelationHeaders(this.getCorrelationProperties(userId,
                                                                                element.getElementHeader().getGUID(),
                                                                                schemaTypeGUIDParameterName,
                                                                                OpenMetadataType.SCHEMA_TYPE_TYPE_NAME,
                                                                                assetManagerGUID,
                                                                                assetManagerName,
                                                                                forLineage,
                                                                                forDuplicateProcessing,
                                                                                effectiveTime,
                                                                                methodName));
                }
            }
        }
    }


    /**
     * Update each returned element with details of the correlation properties for the supplied asset manager.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param results list of elements
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    private void addCorrelationPropertiesToSchemaAttributes(String                       userId,
                                                            String                       assetManagerGUID,
                                                            String                       assetManagerName,
                                                            List<SchemaAttributeElement> results,
                                                            boolean                      forLineage,
                                                            boolean                      forDuplicateProcessing,
                                                            Date                         effectiveTime,
                                                            String                       methodName) throws InvalidParameterException,
                                                                                                               UserNotAuthorizedException,
                                                                                                               PropertyServerException
    {
        if ((results != null) && (assetManagerGUID != null))
        {
            for (SchemaAttributeElement element : results)
            {
                if ((element != null) && (element.getElementHeader() != null) && (element.getElementHeader().getGUID() != null))
                {
                    element.setCorrelationHeaders(this.getCorrelationProperties(userId,
                                                                                element.getElementHeader().getGUID(),
                                                                                schemaAttributeGUIDParameterName,
                                                                                OpenMetadataType.SCHEMA_ATTRIBUTE.typeName,
                                                                                assetManagerGUID,
                                                                                assetManagerName,
                                                                                forLineage,
                                                                                forDuplicateProcessing,
                                                                                effectiveTime,
                                                                                methodName));
                }
            }
        }
    }


    /* =====================================================================================================================
     * A schemaType describes the structure of a data asset, process or port
     */

    /**
     * Create a new metadata element to represent a schema type.
     *
     * @param userId calling user
     * @param correlationProperties  properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param assetManagerIsHome ensure that only the asset manager can update this schema element
     * @param anchorGUID unique identifier of the intended anchor of the schema type
     * @param schemaTypeProperties properties about the schema type to store
     * @param forLineage                the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing    the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName     calling method
     *
     * @return unique identifier of the new schema type
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createSchemaType(String                        userId,
                                   MetadataCorrelationProperties correlationProperties,
                                   boolean                       assetManagerIsHome,
                                   String                        anchorGUID,
                                   SchemaTypeProperties          schemaTypeProperties,
                                   boolean                       forLineage,
                                   boolean                       forDuplicateProcessing,
                                   Date                          effectiveTime,
                                   String                        methodName) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        final String anchorGUIDParameterName     = "anchorGUID";
        final String propertiesParameterName     = "schemaTypeProperties";
        final String qualifiedNameParameterName  = "schemaTypeProperties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(schemaTypeProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(schemaTypeProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        SchemaTypeBuilder builder = this.getSchemaTypeBuilder(schemaTypeProperties,
                                                              repositoryHelper,
                                                              serviceName,
                                                              serverName,
                                                              methodName);

        builder.setEffectivityDates(schemaTypeProperties.getEffectiveFrom(), schemaTypeProperties.getEffectiveTo());

        if (anchorGUID != null)
        {
            EntityDetail anchorEntity = repositoryHandler.getEntityByGUID(userId,
                                                                          anchorGUID,
                                                                          anchorGUIDParameterName,
                                                                          OpenMetadataType.REFERENCEABLE.typeName,
                                                                          forLineage,
                                                                          forDuplicateProcessing,
                                                                          effectiveTime,
                                                                          methodName);
            if (anchorEntity != null)
            {
                builder.setAnchors(userId,
                                   anchorGUID,
                                   anchorEntity.getType().getTypeDefName(),
                                   schemaTypeHandler.getDomainName(anchorEntity),
                                   methodName);
            }
        }

        String schemaTypeGUID = schemaTypeHandler.addSchemaType(userId,
                                                                this.getExternalSourceGUID(correlationProperties, assetManagerIsHome),
                                                                this.getExternalSourceName(correlationProperties, assetManagerIsHome),
                                                                builder,
                                                                schemaTypeProperties.getEffectiveFrom(),
                                                                schemaTypeProperties.getEffectiveTo(),
                                                                forLineage,
                                                                forDuplicateProcessing,
                                                                effectiveTime,
                                                                methodName);

        if (schemaTypeGUID != null)
        {
            this.createExternalIdentifier(userId,
                                          schemaTypeGUID,
                                          schemaTypeGUIDParameterName,
                                          OpenMetadataType.SCHEMA_TYPE_TYPE_NAME,
                                          correlationProperties,
                                          forLineage,
                                          forDuplicateProcessing,
                                          effectiveTime,
                                          methodName);
        }

        return schemaTypeGUID;
    }


    /**
     * Recursively navigate through the schema type loading up a new schema type builder to pass to the
     * schemaTypeHandler.
     *
     * @param schemaType supplied schema type
     * @param repositoryHelper repository helper for this server
     * @param serviceName calling service name
     * @param serverName this server instance
     * @param methodName calling method
     *
     * @return schema type builder
     * @throws InvalidParameterException invalid type in one of the schema types
     */
    private SchemaTypeBuilder getSchemaTypeBuilder(SchemaTypeProperties schemaType,
                                                   OMRSRepositoryHelper repositoryHelper,
                                                   String               serviceName,
                                                   String               serverName,
                                                   String               methodName) throws InvalidParameterException
    {
        String typeName = OpenMetadataType.SCHEMA_TYPE_TYPE_NAME;

        if (schemaType.getTypeName() != null)
        {
            typeName = schemaType.getTypeName();
        }

        String typeGUID = invalidParameterHandler.validateTypeName(typeName,
                                                                   OpenMetadataType.SCHEMA_TYPE_TYPE_NAME,
                                                                   serviceName,
                                                                   methodName,
                                                                   repositoryHelper);

        return new SchemaTypeBuilder(schemaType.getQualifiedName(),
                                     schemaType.getDisplayName(),
                                     schemaType.getDescription(),
                                     schemaType.getVersionNumber(),
                                     schemaType.getIsDeprecated(),
                                     schemaType.getAuthor(),
                                     schemaType.getUsage(),
                                     schemaType.getEncodingStandard(),
                                     schemaType.getNamespace(),
                                     schemaType.getFormula(),
                                     schemaType.getAdditionalProperties(),
                                     typeGUID,
                                     typeName,
                                     schemaType.getExtendedProperties(),
                                     repositoryHelper,
                                     serviceName,
                                     serverName);
    }


    /**
     * Create a new metadata element to represent a schema type using an existing metadata element as a template.
     *
     * @param userId calling user
     * @param correlationProperties  properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param assetManagerIsHome ensure that only the asset manager can update this schema element
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     * @param methodName calling method
     *
     * @return unique identifier of the new schema type
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createSchemaTypeFromTemplate(String                        userId,
                                               MetadataCorrelationProperties correlationProperties,
                                               boolean                       assetManagerIsHome,
                                               String                        templateGUID,
                                               TemplateProperties            templateProperties,
                                               String                        methodName) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException
    {
        final String templateGUIDParameterName   = "templateGUID";
        final String propertiesParameterName     = "templateProperties";
        final String qualifiedNameParameterName  = "templateProperties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(templateProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(templateProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        String schemaTypeGUID = schemaTypeHandler.createSchemaTypeFromTemplate(userId,
                                                                               this.getExternalSourceGUID(correlationProperties, assetManagerIsHome),
                                                                               this.getExternalSourceName(correlationProperties, assetManagerIsHome),
                                                                               templateGUID,
                                                                               templateProperties.getQualifiedName(),
                                                                               templateProperties.getDisplayName(),
                                                                               templateProperties.getDescription(),
                                                                               methodName);

        if (schemaTypeGUID != null)
        {
            this.createExternalIdentifier(userId,
                                          schemaTypeGUID,
                                          schemaTypeGUIDParameterName,
                                          OpenMetadataType.SCHEMA_TYPE_TYPE_NAME,
                                          correlationProperties,
                                          false,
                                          false,
                                          null,
                                          methodName);
        }

        return schemaTypeGUID;
    }


    /**
     * Update the metadata element representing a schema type.
     *
     * @param userId calling user
     * @param correlationProperties  properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param schemaTypeGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param schemaTypeProperties new properties for the metadata element
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateSchemaType(String                        userId,
                                 MetadataCorrelationProperties correlationProperties,
                                 String                        schemaTypeGUID,
                                 boolean                       isMergeUpdate,
                                 boolean                       forLineage,
                                 boolean                       forDuplicateProcessing,
                                 SchemaTypeProperties          schemaTypeProperties,
                                 Date                          effectiveTime,
                                 String                        methodName) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        final String propertiesParameterName     = "schemaTypeProperties";
        final String qualifiedNameParameterName  = "schemaTypeProperties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(schemaTypeGUID, schemaTypeGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(schemaTypeProperties, propertiesParameterName, methodName);
        if (! isMergeUpdate)
        {
            invalidParameterHandler.validateName(schemaTypeProperties.getQualifiedName(), qualifiedNameParameterName, methodName);
        }

        this.validateExternalIdentifier(userId,
                                        schemaTypeGUID,
                                        schemaTypeGUIDParameterName,
                                        OpenMetadataType.SCHEMA_TYPE_TYPE_NAME,
                                        correlationProperties,
                                        forLineage,
                                        forDuplicateProcessing,
                                        effectiveTime,
                                        methodName);

        SchemaTypeBuilder builder = this.getSchemaTypeBuilder(schemaTypeProperties,
                                                              repositoryHelper,
                                                              serviceName,
                                                              serverName,
                                                              methodName);

        schemaTypeHandler.updateSchemaType(userId,
                                           this.getExternalSourceGUID(correlationProperties),
                                           this.getExternalSourceName(correlationProperties),
                                           schemaTypeGUID,
                                           schemaTypeGUIDParameterName,
                                           builder,
                                           isMergeUpdate,
                                           forLineage,
                                           forDuplicateProcessing,
                                           effectiveTime,
                                           methodName);
    }


    /**
     * Connect a schema type to a data asset, process or port.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param assetManagerIsHome ensure that only the asset manager can update this relationship
     * @param schemaTypeGUID unique identifier of the schema type to connect
     * @param parentElementGUID unique identifier of the open metadata element that this schema type is to be connected to
     * @param parentElementTypeName unique type name of the open metadata element that this schema type is to be connected to
     * @param effectiveFrom             the date when this element is active - null for active now
     * @param effectiveTo               the date when this element becomes inactive - null for active until deleted
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName     calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupSchemaTypeParent(String  userId,
                                      String  assetManagerGUID,
                                      String  assetManagerName,
                                      boolean assetManagerIsHome,
                                      String  schemaTypeGUID,
                                      String  parentElementGUID,
                                      String  parentElementTypeName,
                                      Date    effectiveFrom,
                                      Date    effectiveTo,
                                      boolean forLineage,
                                      boolean forDuplicateProcessing,
                                      Date    effectiveTime,
                                      String  methodName) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        final String schemaTypeGUIDParameterName    = "schemaTypeGUID";
        final String parentElementGUIDParameterName = "parentElementGUID";
        final String parentElementTypeParameterName = "parentElementTypeName";

        invalidParameterHandler.validateName(parentElementTypeName, parentElementTypeParameterName, methodName);

       if (repositoryHelper.isTypeOf(serviceName, parentElementTypeName, OpenMetadataType.PORT_TYPE_NAME))
        {
            schemaTypeHandler.linkElementToElement(userId,
                                                   this.getExternalSourceGUID(assetManagerGUID, assetManagerIsHome),
                                                   this.getExternalSourceName(assetManagerName, assetManagerIsHome),
                                                   parentElementGUID,
                                                   parentElementGUIDParameterName,
                                                   parentElementTypeName,
                                                   schemaTypeGUID,
                                                   schemaTypeGUIDParameterName,
                                                   OpenMetadataType.SCHEMA_TYPE_TYPE_NAME,
                                                   forLineage,
                                                   forDuplicateProcessing,
                                                   OpenMetadataType.PORT_SCHEMA_RELATIONSHIP_TYPE_GUID,
                                                   OpenMetadataType.PORT_SCHEMA_RELATIONSHIP_TYPE_NAME,
                                                   null,
                                                   effectiveFrom,
                                                   effectiveTo,
                                                   effectiveTime,
                                                   methodName);
        }
        else
        {
            schemaTypeHandler.linkElementToElement(userId,
                                                   this.getExternalSourceGUID(assetManagerGUID, assetManagerIsHome),
                                                   this.getExternalSourceName(assetManagerName, assetManagerIsHome),
                                                   parentElementGUID,
                                                   parentElementGUIDParameterName,
                                                   parentElementTypeName,
                                                   schemaTypeGUID,
                                                   schemaTypeGUIDParameterName,
                                                   OpenMetadataType.SCHEMA_TYPE_TYPE_NAME,
                                                   forLineage,
                                                   forDuplicateProcessing,
                                                   OpenMetadataType.ASSET_TO_SCHEMA_TYPE_TYPE_GUID,
                                                   OpenMetadataType.ASSET_TO_SCHEMA_TYPE_TYPE_NAME,
                                                   null,
                                                   effectiveFrom,
                                                   effectiveTo,
                                                   effectiveTime,
                                                   methodName);
        }
    }


    /**
     * Remove the relationship between a schema type and its parent data asset, process or port.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param schemaTypeGUID unique identifier of the schema type to connect
     * @param parentElementGUID unique identifier of the open metadata element that this schema type is to be connected to
     * @param parentElementTypeName unique type name of the open metadata element that this schema type is to be connected to
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearSchemaTypeParent(String  userId,
                                      String  assetManagerGUID,
                                      String  assetManagerName,
                                      String  schemaTypeGUID,
                                      String  parentElementGUID,
                                      String  parentElementTypeName,
                                      boolean forLineage,
                                      boolean forDuplicateProcessing,
                                      Date    effectiveTime,
                                      String  methodName) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        final String parentElementTypeParameterName = "parentElementTypeName";

        invalidParameterHandler.validateName(parentElementTypeName, parentElementTypeParameterName, methodName);

        schemaTypeHandler.clearSchemaTypeParent(userId,
                                                assetManagerGUID,
                                                assetManagerName,
                                                schemaTypeGUID,
                                                parentElementGUID,
                                                parentElementTypeName,
                                                forLineage,
                                                forDuplicateProcessing,
                                                effectiveTime,
                                                methodName);
    }


    /**
     * Create a relationship between two schema elements.  The name of the desired relationship, and any properties (including effectivity dates)
     * are passed on the API.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param assetManagerIsHome ensure that only the asset manager can update this relationship
     * @param endOneGUID unique identifier of the schema element at end one of the relationship
     * @param endTwoGUID unique identifier of the schema element at end two of the relationship
     * @param relationshipTypeName type of the relationship to create
     * @param properties properties for the new relationship
     * @param effectiveFrom             the date when this element is active - null for active now
     * @param effectiveTo               the date when this element becomes inactive - null for active until deleted
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName     calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @SuppressWarnings(value = "unused")
    public void setupSchemaElementRelationship(String                 userId,
                                               String                 assetManagerGUID,
                                               String                 assetManagerName,
                                               boolean                assetManagerIsHome,
                                               String                 endOneGUID,
                                               String                 endTwoGUID,
                                               String                 relationshipTypeName,
                                               RelationshipProperties properties,
                                               Date                   effectiveFrom,
                                               Date                   effectiveTo,
                                               boolean                forLineage,
                                               boolean                forDuplicateProcessing,
                                               Date                   effectiveTime,
                                               String                 methodName) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        final String endOneParameterName           = "endOneGUID";
        final String endTwoParameterName           = "endTwoGUID";
        final String relationshipTypeParameterName = "relationshipTypeName";
        final String propertiesParameterName       = "properties";

        if (properties != null)
        {
            schemaTypeHandler.setupSchemaElementRelationship(userId,
                                                             this.getExternalSourceGUID(assetManagerGUID, assetManagerIsHome),
                                                             this.getExternalSourceName(assetManagerName, assetManagerIsHome),
                                                             endOneGUID,
                                                             endTwoGUID,
                                                             relationshipTypeName,
                                                             properties.getExtendedProperties(),
                                                             effectiveFrom,
                                                             effectiveTo,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             effectiveTime,
                                                             methodName);
        }
        else
        {
            schemaTypeHandler.setupSchemaElementRelationship(userId,
                                                             this.getExternalSourceGUID(assetManagerGUID, assetManagerIsHome),
                                                             this.getExternalSourceName(assetManagerName, assetManagerIsHome),
                                                             endOneGUID,
                                                             endTwoGUID,
                                                             relationshipTypeName,
                                                             null,
                                                             effectiveFrom,
                                                             effectiveTo,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             effectiveTime,
                                                             methodName);
        }
    }


    /**
     * Remove a relationship between two schema elements.  The name of the desired relationship is passed on the API.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param endOneGUID unique identifier of the schema element at end one of the relationship
     * @param endTwoGUID unique identifier of the schema element at end two of the relationship
     * @param relationshipTypeName type of the relationship to create
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearSchemaElementRelationship(String  userId,
                                               String  assetManagerGUID,
                                               String  assetManagerName,
                                               String  endOneGUID,
                                               String  endTwoGUID,
                                               String  relationshipTypeName,
                                               boolean forLineage,
                                               boolean forDuplicateProcessing,
                                               Date    effectiveTime,
                                               String  methodName) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        final String relationshipTypeParameterName = "relationshipTypeName";

        invalidParameterHandler.validateName(relationshipTypeName, relationshipTypeParameterName, methodName);

        schemaTypeHandler.clearSchemaElementRelationship(userId,
                                                         assetManagerGUID,
                                                         assetManagerName,
                                                         endOneGUID,
                                                         endTwoGUID,
                                                         relationshipTypeName,
                                                         forLineage,
                                                         forDuplicateProcessing,
                                                         effectiveTime,
                                                         methodName);
    }


    /**
     * Remove the metadata element representing a schema type.
     *
     * @param userId calling user
     * @param correlationProperties  properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param schemaTypeGUID unique identifier of the metadata element to remove
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeSchemaType(String                        userId,
                                 MetadataCorrelationProperties correlationProperties,
                                 String                        schemaTypeGUID,
                                 boolean                       forLineage,
                                 boolean                       forDuplicateProcessing,
                                 Date                          effectiveTime,
                                 String                        methodName) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(schemaTypeGUID, schemaTypeGUIDParameterName, methodName);

        this.validateExternalIdentifier(userId,
                                        schemaTypeGUID,
                                        schemaTypeGUIDParameterName,
                                        OpenMetadataType.SCHEMA_TYPE_TYPE_NAME,
                                        correlationProperties,
                                        forLineage,
                                        forDuplicateProcessing,
                                        effectiveTime,
                                        methodName);

        schemaAttributeHandler.deleteBeanInRepository(userId,
                                                      getExternalSourceGUID(correlationProperties),
                                                      getExternalSourceName(correlationProperties),
                                                      schemaTypeGUID,
                                                      schemaTypeGUIDParameterName,
                                                      OpenMetadataType.SCHEMA_TYPE_TYPE_GUID,
                                                      OpenMetadataType.SCHEMA_TYPE_TYPE_NAME,
                                                      null,
                                                      null,
                                                      forLineage,
                                                      forDuplicateProcessing,
                                                      effectiveTime,
                                                      methodName);
    }


    /**
     * Retrieve the list of schema type metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<SchemaTypeElement> findSchemaType(String  userId,
                                                  String  assetManagerGUID,
                                                  String  assetManagerName,
                                                  String  searchString,
                                                  int     startFrom,
                                                  int     pageSize,
                                                  boolean forLineage,
                                                  boolean forDuplicateProcessing,
                                                  Date    effectiveTime,
                                                  String  methodName) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
       List<SchemaTypeElement> results = schemaTypeHandler.findSchemaTypes(userId,
                                                                           null,
                                                                           searchString,
                                                                           startFrom,
                                                                           pageSize,
                                                                           forLineage,
                                                                           forDuplicateProcessing,
                                                                           effectiveTime,
                                                                           methodName);

        addCorrelationPropertiesToSchemaTypes(userId,
                                              assetManagerGUID,
                                              assetManagerName,
                                              results,
                                              forLineage,
                                              forDuplicateProcessing,
                                              effectiveTime,
                                              methodName);

        return results;
    }


    /**
     * Return the schema type associated with a specific open metadata element (data asset, process or port).
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param parentElementGUID unique identifier of the open metadata element that this schema type is to be connected to
     * @param parentElementTypeName unique type name of the open metadata element that this schema type is to be connected to
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName     calling method
     *
     * @return metadata element describing the schema type associated with the requested parent element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public SchemaTypeElement getSchemaTypeForElement(String  userId,
                                                     String  assetManagerGUID,
                                                     String  assetManagerName,
                                                     String  parentElementGUID,
                                                     String  parentElementTypeName,
                                                     boolean forLineage,
                                                     boolean forDuplicateProcessing,
                                                     Date    effectiveTime,
                                                     String  methodName) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        final String guidParameterName = "parentElementGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(parentElementGUID, guidParameterName, methodName);

        SchemaTypeElement schemaTypeElement;

        if ((parentElementTypeName != null) && repositoryHelper.isTypeOf(serviceName,
                                                                         parentElementTypeName,
                                                                         OpenMetadataType.PORT_TYPE_NAME))
        {
            schemaTypeElement = schemaTypeHandler.getSchemaTypeForPort(userId,
                                                                       parentElementGUID,
                                                                       guidParameterName,
                                                                       forLineage,
                                                                       forDuplicateProcessing,
                                                                       effectiveTime,
                                                                       methodName);
        }
        else
        {
            schemaTypeElement = schemaTypeHandler.getSchemaTypeForAsset(userId,
                                                                        parentElementGUID,
                                                                        guidParameterName,
                                                                        forLineage,
                                                                        forDuplicateProcessing,
                                                                        effectiveTime,
                                                                        methodName);
        }

        if (schemaTypeElement != null)
        {
            schemaTypeElement.setCorrelationHeaders(this.getCorrelationProperties(userId,
                                                                                  schemaTypeElement.getElementHeader().getGUID(),
                                                                                  schemaTypeGUIDParameterName,
                                                                                  OpenMetadataType.SCHEMA_TYPE_TYPE_NAME,
                                                                                  assetManagerGUID,
                                                                                  assetManagerName,
                                                                                  forLineage,
                                                                                  forDuplicateProcessing,
                                                                                  effectiveTime,
                                                                                  methodName));
        }

        return schemaTypeElement;
    }


    /**
     * Retrieve the list of schema type metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<SchemaTypeElement>   getSchemaTypeByName(String  userId,
                                                         String  assetManagerGUID,
                                                         String  assetManagerName,
                                                         String  name,
                                                         int     startFrom,
                                                         int     pageSize,
                                                         boolean forLineage,
                                                         boolean forDuplicateProcessing,
                                                         Date    effectiveTime,
                                                         String  methodName) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        List<SchemaTypeElement> results = schemaTypeHandler.getSchemaTypeByName(userId, null, name, startFrom, pageSize, forLineage, forDuplicateProcessing, effectiveTime, methodName);

        addCorrelationPropertiesToSchemaTypes(userId,
                                              assetManagerGUID,
                                              assetManagerName,
                                              results,
                                              forLineage,
                                              forDuplicateProcessing,
                                              effectiveTime,
                                              methodName);

        return results;
    }


    /**
     * Retrieve the schema type metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param schemaTypeGUID unique identifier of the requested metadata element
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName calling method
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public SchemaTypeElement getSchemaTypeByGUID(String  userId,
                                                 String  assetManagerGUID,
                                                 String  assetManagerName,
                                                 String  schemaTypeGUID,
                                                 boolean forLineage,
                                                 boolean forDuplicateProcessing,
                                                 Date    effectiveTime,
                                                 String  methodName) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        final String guidParameterName = "schemaTypeGUID";

        SchemaTypeElement schemaTypeElement = schemaTypeHandler.getSchemaType(userId,
                                                                              schemaTypeGUID,
                                                                              guidParameterName,
                                                                              forLineage,
                                                                              forDuplicateProcessing,
                                                                              effectiveTime,
                                                                              methodName);

        if (schemaTypeElement != null)
        {
            schemaTypeElement.setCorrelationHeaders(this.getCorrelationProperties(userId,
                                                                                  schemaTypeGUID,
                                                                                  guidParameterName,
                                                                                  OpenMetadataType.SCHEMA_TYPE_TYPE_NAME,
                                                                                  assetManagerGUID,
                                                                                  assetManagerName,
                                                                                  forLineage,
                                                                                  forDuplicateProcessing,
                                                                                  effectiveTime,
                                                                                  methodName));
        }

        return schemaTypeElement;
    }


    /**
     * Retrieve the header of the metadata element connected to a schema type.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param schemaTypeGUID unique identifier of the requested metadata element
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName     calling method
     *
     * @return header for parent element (data asset, process, port)
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @SuppressWarnings(value="unused")
    public ElementHeader getSchemaTypeParent(String  userId,
                                             String  assetManagerGUID,
                                             String  assetManagerName,
                                             String  schemaTypeGUID,
                                             boolean forLineage,
                                             boolean forDuplicateProcessing,
                                             Date    effectiveTime,
                                             String  methodName) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        final String guidParameterName = "schemaTypeGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(schemaTypeGUID, guidParameterName, methodName);

        RepositoryRelationshipsIterator iterator = new RepositoryRelationshipsIterator(repositoryHandler,
                                                                                       invalidParameterHandler,
                                                                                       userId,
                                                                                       schemaTypeGUID,
                                                                                       OpenMetadataType.SCHEMA_TYPE_TYPE_NAME,
                                                                                       null,
                                                                                       null,
                                                                                       1,
                                                                                       false,
                                                                                       false,
                                                                                       0,
                                                                                       invalidParameterHandler.getMaxPagingSize(),
                                                                                       effectiveTime,
                                                                                       methodName);

        while (iterator.moreToReceive())
        {
            Relationship relationship = iterator.getNext();

            if ((relationship != null) && (relationship.getType() != null) &&
                        ((repositoryHelper.isTypeOf(serviceName,
                                                    relationship.getType().getTypeDefName(),
                                                    OpenMetadataType.ASSET_TO_SCHEMA_TYPE_TYPE_NAME)) ||
                         (repositoryHelper.isTypeOf(serviceName,
                                                    relationship.getType().getTypeDefName(),
                                                    OpenMetadataType.PORT_SCHEMA_RELATIONSHIP_TYPE_NAME))))
            {
                final String parentGUIDParameterName = "relationship.getEntityOneProxy().getGUID()";

                EntityDetail parentEntity = schemaTypeHandler.getEntityFromRepository(userId,
                                                                                      relationship.getEntityOneProxy().getGUID(),
                                                                                      parentGUIDParameterName,
                                                                                      OpenMetadataType.REFERENCEABLE.typeName,
                                                                                      null,
                                                                                      null,
                                                                                      false,
                                                                                      false,
                                                                                      effectiveTime,
                                                                                      methodName);

                ElementHeaderConverter<ElementHeader> headerConverter = new ElementHeaderConverter<>(repositoryHelper, serviceName, serverName);
                return headerConverter.getNewBean(ElementHeader.class, parentEntity, methodName);
            }
        }

        return null;
    }


    /* ===============================================================================
     * A schemaType typically contains many schema attributes, linked with relationships.
     */

    /**
     * Create a new metadata element to represent a schema attribute.
     *
     * @param userId calling user
     * @param correlationProperties  properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param assetManagerIsHome ensure that only the asset manager can update this schema attribute
     * @param schemaElementGUID unique identifier of the schemaType or Schema Attribute where the schema attribute is connected to
     * @param schemaAttributeProperties properties for the schema attribute
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName calling method
     *
     * @return unique identifier of the new metadata element for the schema attribute
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createSchemaAttribute(String                        userId,
                                        MetadataCorrelationProperties correlationProperties,
                                        boolean                       assetManagerIsHome,
                                        String                        schemaElementGUID,
                                        SchemaAttributeProperties     schemaAttributeProperties,
                                        boolean                       forLineage,
                                        boolean                       forDuplicateProcessing,
                                        Date                          effectiveTime,
                                        String                        methodName) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        final String schemaElementGUIDParameterName  = "schemaElementGUID";
        final String propertiesParameterName     = "schemaAttributeProperties";
        final String qualifiedNameParameterName  = "schemaAttributeProperties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(schemaElementGUID, schemaElementGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(schemaAttributeProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(schemaAttributeProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        SchemaAttributeBuilder schemaAttributeBuilder = this.getSchemaAttributeBuilder(userId,
                                                                                       schemaAttributeProperties,
                                                                                       methodName);

        String schemaAttributeGUID = schemaAttributeHandler.createNestedSchemaAttribute(userId,
                                                                                        getExternalSourceGUID(correlationProperties, assetManagerIsHome),
                                                                                        getExternalSourceName(correlationProperties, assetManagerIsHome),
                                                                                        schemaElementGUID,
                                                                                        schemaElementGUIDParameterName,
                                                                                        schemaAttributeProperties.getQualifiedName(),
                                                                                        qualifiedNameParameterName,
                                                                                        schemaAttributeBuilder,
                                                                                        schemaAttributeProperties.getEffectiveFrom(),
                                                                                        schemaAttributeProperties.getEffectiveTo(),
                                                                                        forLineage,
                                                                                        forDuplicateProcessing,
                                                                                        effectiveTime,
                                                                                        methodName);

        if (schemaAttributeGUID != null)
        {
            this.createExternalIdentifier(userId,
                                          schemaAttributeGUID,
                                          schemaAttributeGUIDParameterName,
                                          OpenMetadataType.SCHEMA_ATTRIBUTE.typeName,
                                          correlationProperties,
                                          forLineage,
                                          forDuplicateProcessing,
                                          effectiveTime,
                                          methodName);
        }

        return schemaAttributeGUID;
    }


    /**
     * Return a schema attribute builder packed with the knowledge of the schema attribute from the schema attribute properties
     *
     * @param userId calling user
     * @param schemaAttributeProperties properties from the caller
     * @param methodName calling method
     *
     * @return schema attributes properties in a builder
     * @throws InvalidParameterException schema type is invalid
     */
    private SchemaAttributeBuilder getSchemaAttributeBuilder(String                    userId,
                                                             SchemaAttributeProperties schemaAttributeProperties,
                                                             String                    methodName) throws InvalidParameterException
    {
        String typeName = OpenMetadataType.SCHEMA_ATTRIBUTE.typeName;

        if (schemaAttributeProperties.getTypeName() != null)
        {
            typeName = schemaAttributeProperties.getTypeName();
        }

        String typeGUID = invalidParameterHandler.validateTypeName(typeName,
                                                                   OpenMetadataType.SCHEMA_ATTRIBUTE.typeName,
                                                                   serviceName,
                                                                   methodName,
                                                                   repositoryHelper);

        int sortOrder = 0;
        if (schemaAttributeProperties.getSortOrder() != null)
        {
            sortOrder = schemaAttributeProperties.getSortOrder().getOrdinal();
        }

        SchemaAttributeBuilder schemaAttributeBuilder = new SchemaAttributeBuilder(schemaAttributeProperties.getQualifiedName(),
                                                                                   schemaAttributeProperties.getDisplayName(),
                                                                                   schemaAttributeProperties.getDescription(),
                                                                                   schemaAttributeProperties.getElementPosition(),
                                                                                   schemaAttributeProperties.getMinCardinality(),
                                                                                   schemaAttributeProperties.getMaxCardinality(),
                                                                                   schemaAttributeProperties.getIsDeprecated(),
                                                                                   schemaAttributeProperties.getDefaultValueOverride(),
                                                                                   schemaAttributeProperties.getAllowsDuplicateValues(),
                                                                                   schemaAttributeProperties.getOrderedValues(),
                                                                                   sortOrder,
                                                                                   schemaAttributeProperties.getMinimumLength(),
                                                                                   schemaAttributeProperties.getLength(),
                                                                                   schemaAttributeProperties.getPrecision(),
                                                                                   schemaAttributeProperties.getIsNullable(),
                                                                                   schemaAttributeProperties.getNativeJavaClass(),
                                                                                   schemaAttributeProperties.getAliases(),
                                                                                   schemaAttributeProperties.getAdditionalProperties(),
                                                                                   typeGUID,
                                                                                   typeName,
                                                                                   schemaAttributeProperties.getExtendedProperties(),
                                                                                   repositoryHelper,
                                                                                   serviceName,
                                                                                   serverName);

        if (schemaAttributeProperties.getSchemaType() != null)
        {
            SchemaTypeBuilder schemaTypeBuilder = this.getSchemaTypeBuilder(schemaAttributeProperties.getSchemaType(),
                                                                            repositoryHelper,
                                                                            serviceName,
                                                                            serverName,
                                                                            methodName);

            schemaAttributeBuilder.setSchemaType(userId, schemaTypeBuilder, methodName);
        }

        return schemaAttributeBuilder;
    }


    /**
     * Create a new metadata element to represent a schema attribute using an existing metadata element as a template.
     *
     * @param userId calling user
     * @param correlationProperties  properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param assetManagerIsHome ensure that only the asset manager can update this schema attribute
     * @param schemaElementGUID unique identifier of the schemaType or Schema Attribute where the schema attribute is connected to
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName calling method
     *
     * @return unique identifier of the new metadata element for the schema attribute
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createSchemaAttributeFromTemplate(String                        userId,
                                                    MetadataCorrelationProperties correlationProperties,
                                                    boolean                       assetManagerIsHome,
                                                    String                        schemaElementGUID,
                                                    String                        templateGUID,
                                                    TemplateProperties            templateProperties,
                                                    boolean                       forLineage,
                                                    boolean                       forDuplicateProcessing,
                                                    Date                          effectiveTime,
                                                    String                        methodName) throws InvalidParameterException,
                                                                                                     UserNotAuthorizedException,
                                                                                                     PropertyServerException
    {
        final String schemaElementGUIDParameterName = "schemaElementGUID";
        final String templateGUIDParameterName   = "templateGUID";
        final String propertiesParameterName     = "templateProperties";
        final String qualifiedNameParameterName  = "templateProperties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(schemaElementGUID, schemaElementGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(templateProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(templateProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        String schemaAttributeGUID = schemaAttributeHandler.createSchemaAttributeFromTemplate(userId,
                                                                                              getExternalSourceGUID(correlationProperties, assetManagerIsHome),
                                                                                              getExternalSourceName(correlationProperties, assetManagerIsHome),
                                                                                              schemaElementGUID,
                                                                                              schemaElementGUIDParameterName,
                                                                                              templateGUID,
                                                                                              templateProperties.getQualifiedName(),
                                                                                              templateProperties.getDisplayName(),
                                                                                              templateProperties.getDescription(),
                                                                                              forLineage,
                                                                                              forDuplicateProcessing,
                                                                                              effectiveTime,
                                                                                              methodName);

        if (schemaAttributeGUID != null)
        {
            this.createExternalIdentifier(userId,
                                          schemaAttributeGUID,
                                          schemaAttributeGUIDParameterName,
                                          OpenMetadataType.SCHEMA_ATTRIBUTE.typeName,
                                          correlationProperties,
                                          forLineage,
                                          forDuplicateProcessing,
                                          effectiveTime,
                                          methodName);
        }

        return schemaAttributeGUID;
    }


    /**
     * Update the properties of the metadata element representing a schema attribute.
     *
     * @param userId calling user
     * @param correlationProperties  properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param schemaAttributeGUID unique identifier of the schema attribute to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param schemaAttributeProperties new properties for the schema attribute
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateSchemaAttribute(String                        userId,
                                      MetadataCorrelationProperties correlationProperties,
                                      String                        schemaAttributeGUID,
                                      boolean                       isMergeUpdate,
                                      SchemaAttributeProperties     schemaAttributeProperties,
                                      boolean                       forLineage,
                                      boolean                       forDuplicateProcessing,
                                      Date                          effectiveTime,
                                      String                        methodName) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        final String propertiesParameterName          = "schemaAttributeProperties";
        final String qualifiedNameParameterName       = "schemaAttributeProperties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(schemaAttributeGUID, schemaAttributeGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(schemaAttributeProperties, propertiesParameterName, methodName);
        if (! isMergeUpdate)
        {
            invalidParameterHandler.validateName(schemaAttributeProperties.getQualifiedName(), qualifiedNameParameterName, methodName);
        }

        this.validateExternalIdentifier(userId,
                                        schemaAttributeGUID,
                                        schemaAttributeGUIDParameterName,
                                        OpenMetadataType.SCHEMA_ATTRIBUTE.typeName,
                                        correlationProperties,
                                        forLineage,
                                        forDuplicateProcessing,
                                        effectiveTime,
                                        methodName);

        SchemaAttributeBuilder schemaAttributeBuilder = this.getSchemaAttributeBuilder(userId,
                                                                                       schemaAttributeProperties,
                                                                                       methodName);

        schemaAttributeHandler.updateSchemaAttribute(userId,
                                                     getExternalSourceGUID(correlationProperties),
                                                     getExternalSourceName(correlationProperties),
                                                     schemaAttributeGUID,
                                                     schemaAttributeGUIDParameterName,
                                                     schemaAttributeProperties.getQualifiedName(),
                                                     qualifiedNameParameterName,
                                                     schemaAttributeBuilder,
                                                     schemaAttributeProperties.getTypeName(),
                                                     isMergeUpdate,
                                                     forLineage,
                                                     forDuplicateProcessing,
                                                     effectiveTime,
                                                     methodName);
    }


    /**
     * Classify the schema type (or attribute if type is embedded) to indicate that it is a calculated value.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param assetManagerIsHome ensure that only the asset manager can update this relationship
     * @param schemaElementGUID unique identifier of the metadata element to update
     * @param formula formula used to calculate the value
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName     calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setSchemaElementAsCalculatedValue(String  userId,
                                                  String  assetManagerGUID,
                                                  String  assetManagerName,
                                                  boolean assetManagerIsHome,
                                                  String  schemaElementGUID,
                                                  String  formula,
                                                  boolean forLineage,
                                                  boolean forDuplicateProcessing,
                                                  Date    effectiveTime,
                                                  String  methodName) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        final String schemaElementGUIDParameterName = "schemaElementGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(schemaElementGUID, schemaElementGUIDParameterName, methodName);

        InstanceProperties properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                     null,
                                                                                     OpenMetadataProperty.FORMULA.name,
                                                                                     formula,
                                                                                     methodName);

        schemaAttributeHandler.setClassificationInRepository(userId,
                                                             getExternalSourceGUID(assetManagerGUID, assetManagerIsHome),
                                                             getExternalSourceName(assetManagerName, assetManagerIsHome),
                                                             schemaElementGUID,
                                                             schemaElementGUIDParameterName,
                                                             OpenMetadataType.SCHEMA_ELEMENT_TYPE_NAME,
                                                             OpenMetadataType.CALCULATED_VALUE_CLASSIFICATION_TYPE_NAME,
                                                             OpenMetadataType.CALCULATED_VALUE_CLASSIFICATION_TYPE_GUID,
                                                             properties,
                                                             false,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             effectiveTime,
                                                             methodName);
    }


    /**
     * Remove the calculated value designation from the schema element.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param schemaElementGUID unique identifier of the metadata element to update
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName     calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearSchemaElementAsCalculatedValue(String  userId,
                                                    String  assetManagerGUID,
                                                    String  assetManagerName,
                                                    String  schemaElementGUID,
                                                    boolean forLineage,
                                                    boolean forDuplicateProcessing,
                                                    Date    effectiveTime,
                                                    String  methodName) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        final String schemaElementGUIDParameterName = "schemaElementGUID";

        schemaAttributeHandler.removeClassificationFromRepository(userId,
                                                                  assetManagerGUID,
                                                                  assetManagerName,
                                                                  schemaElementGUID,
                                                                  schemaElementGUIDParameterName,
                                                                  OpenMetadataType.SCHEMA_ELEMENT_TYPE_NAME,
                                                                  OpenMetadataType.CALCULATED_VALUE_CLASSIFICATION_TYPE_GUID,
                                                                  OpenMetadataType.CALCULATED_VALUE_CLASSIFICATION_TYPE_NAME,
                                                                  forLineage,
                                                                  forDuplicateProcessing,
                                                                  effectiveTime,
                                                                  methodName);
    }


    /**
     * Classify the column schema attribute to indicate that it describes a primary key.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param assetManagerIsHome ensure that only the asset manager can update this classification
     * @param schemaAttributeGUID unique identifier of the metadata element to update
     * @param primaryKeyName name of the primary key (if different from the column name)
     * @param primaryKeyPattern key pattern used to maintain the primary key
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName     calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupColumnAsPrimaryKey(String     userId,
                                        String     assetManagerGUID,
                                        String     assetManagerName,
                                        boolean    assetManagerIsHome,
                                        String     schemaAttributeGUID,
                                        String     primaryKeyName,
                                        KeyPattern primaryKeyPattern,
                                        boolean    forLineage,
                                        boolean    forDuplicateProcessing,
                                        Date       effectiveTime,
                                        String     methodName) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(schemaAttributeGUID, schemaAttributeGUIDParameterName, methodName);

        InstanceProperties properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                     null,
                                                                                     OpenMetadataType.PRIMARY_KEY_NAME_PROPERTY_NAME,
                                                                                     primaryKeyName,
                                                                                     methodName);

        int keyPatternOrdinal = 0;
        if (primaryKeyPattern != null)
        {
            keyPatternOrdinal = primaryKeyPattern.getOrdinal();
        }

        try
        {
            properties = repositoryHelper.addEnumPropertyToInstance(serviceName,
                                                                    properties,
                                                                    OpenMetadataType.PRIMARY_KEY_PATTERN_PROPERTY_NAME,
                                                                    OpenMetadataType.KEY_PATTERN_ENUM_TYPE_GUID,
                                                                    OpenMetadataType.KEY_PATTERN_ENUM_TYPE_NAME,
                                                                    keyPatternOrdinal,
                                                                    methodName);
        }
        catch (TypeErrorException classificationNotSupported)
        {
            throw new InvalidParameterException(classificationNotSupported, OpenMetadataType.PRIMARY_KEY_PATTERN_PROPERTY_NAME);
        }

        schemaAttributeHandler.setClassificationInRepository(userId,
                                                             getExternalSourceGUID(assetManagerGUID, assetManagerIsHome),
                                                             getExternalSourceName(assetManagerName, assetManagerIsHome),
                                                             schemaAttributeGUID,
                                                             schemaAttributeGUIDParameterName,
                                                             OpenMetadataType.SCHEMA_ATTRIBUTE.typeName,
                                                             OpenMetadataType.PRIMARY_KEY_CLASSIFICATION_TYPE_GUID,
                                                             OpenMetadataType.PRIMARY_KEY_CLASSIFICATION_TYPE_NAME,
                                                             properties,
                                                             false,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             effectiveTime,
                                                             methodName);
    }


    /**
     * Remove the primary key designation from the schema attribute.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param schemaAttributeGUID unique identifier of the metadata element to update
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName     calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearColumnAsPrimaryKey(String  userId,
                                        String  assetManagerGUID,
                                        String  assetManagerName,
                                        String  schemaAttributeGUID,
                                        boolean forLineage,
                                        boolean forDuplicateProcessing,
                                        Date    effectiveTime,
                                        String  methodName) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        schemaAttributeHandler.removeClassificationFromRepository(userId,
                                                                  assetManagerGUID,
                                                                  assetManagerName,
                                                                  schemaAttributeGUID,
                                                                  schemaAttributeGUIDParameterName,
                                                                  OpenMetadataType.SCHEMA_ATTRIBUTE.typeName,
                                                                  OpenMetadataType.PRIMARY_KEY_CLASSIFICATION_TYPE_GUID,
                                                                  OpenMetadataType.PRIMARY_KEY_CLASSIFICATION_TYPE_NAME,
                                                                  forLineage,
                                                                  forDuplicateProcessing,
                                                                  effectiveTime,
                                                                  methodName);
    }


    /**
     * Link two schema attributes together to show a foreign key relationship.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param assetManagerIsHome ensure that only the asset manager can update this relationship
     * @param primaryKeyGUID unique identifier of the derived schema element
     * @param foreignKeyGUID unique identifier of the query target schema element
     * @param foreignKeyProperties properties for the foreign key relationship
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName     calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @SuppressWarnings(value = "unused")
    public void setupForeignKeyRelationship(String               userId,
                                            String               assetManagerGUID,
                                            String               assetManagerName,
                                            boolean              assetManagerIsHome,
                                            String               primaryKeyGUID,
                                            String               foreignKeyGUID,
                                            ForeignKeyProperties foreignKeyProperties,
                                            boolean              forLineage,
                                            boolean              forDuplicateProcessing,
                                            Date                 effectiveTime,
                                            String               methodName) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        final String primaryKeyGUIDParameterName = "primaryKeyGUID";
        final String foreignKeyGUIDParameterName = "foreignKeyGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(primaryKeyGUID, primaryKeyGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(foreignKeyGUID, foreignKeyGUIDParameterName, methodName);

        Date effectiveFrom = null;
        Date effectiveTo = null;

        if (foreignKeyProperties != null)
        {
            effectiveFrom = foreignKeyProperties.getEffectiveFrom();
            effectiveTo = foreignKeyProperties.getEffectiveTo();
        }

        schemaAttributeHandler.linkElementToElement(userId,
                                                    getExternalSourceGUID(assetManagerGUID, assetManagerIsHome),
                                                    getExternalSourceName(assetManagerName, assetManagerIsHome),
                                                    primaryKeyGUID,
                                                    primaryKeyGUIDParameterName,
                                                    OpenMetadataType.SCHEMA_ATTRIBUTE.typeName,
                                                    foreignKeyGUID,
                                                    foreignKeyGUIDParameterName,
                                                    OpenMetadataType.SCHEMA_ATTRIBUTE.typeName,
                                                    forLineage,
                                                    forDuplicateProcessing,
                                                    OpenMetadataType.FOREIGN_KEY_RELATIONSHIP_TYPE_GUID,
                                                    OpenMetadataType.FOREIGN_KEY_RELATIONSHIP_TYPE_NAME,
                                                    this.getForeignKeyProperties(foreignKeyProperties, methodName),
                                                    effectiveFrom,
                                                    effectiveTo,
                                                    effectiveTime,
                                                    methodName);
    }


    /**
     * Set up the foreign key properties in an InstanceProperties object.
     *
     * @param foreignKeyProperties caller's properties
     * @param methodName calling method
     * @return instance properties
     */
    private InstanceProperties getForeignKeyProperties(ForeignKeyProperties foreignKeyProperties,
                                                       String               methodName)
    {
        InstanceProperties properties = null;

        if (foreignKeyProperties != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      null,
                                                                      OpenMetadataType.FOREIGN_KEY_NAME_PROPERTY_NAME,
                                                                      foreignKeyProperties.getName(),
                                                                      methodName);
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataType.FOREIGN_KEY_DESCRIPTION_PROPERTY_NAME,
                                                                      foreignKeyProperties.getDescription(),
                                                                      methodName);
            properties = repositoryHelper.addIntPropertyToInstance(serviceName,
                                                                   properties,
                                                                   OpenMetadataType.FOREIGN_KEY_CONFIDENCE_PROPERTY_NAME,
                                                                   foreignKeyProperties.getConfidence(),
                                                                   methodName);
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataType.FOREIGN_KEY_STEWARD_PROPERTY_NAME,
                                                                      foreignKeyProperties.getSteward(),
                                                                      methodName);
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataType.FOREIGN_KEY_SOURCE_PROPERTY_NAME,
                                                                      foreignKeyProperties.getSource(),
                                                                      methodName);
        }

        return properties;
    }


    /**
     * Update the relationship properties for the query target.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param primaryKeyGUID unique identifier of the derived schema element
     * @param foreignKeyGUID unique identifier of the query target schema element
     * @param foreignKeyProperties properties for the foreign key relationship
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName     calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @SuppressWarnings(value = "unused")
    public void updateForeignKeyRelationship(String               userId,
                                             String               assetManagerGUID,
                                             String               assetManagerName,
                                             String               primaryKeyGUID,
                                             String               foreignKeyGUID,
                                             ForeignKeyProperties foreignKeyProperties,
                                             boolean              forLineage,
                                             boolean              forDuplicateProcessing,
                                             Date                 effectiveTime,
                                             String               methodName) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String primaryKeyGUIDParameterName = "primaryKeyGUID";
        final String foreignKeyGUIDParameterName = "foreignKeyGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(primaryKeyGUID, primaryKeyGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(foreignKeyGUID, foreignKeyGUIDParameterName, methodName);

        schemaAttributeHandler.updateElementToElementLink(userId,
                                                          assetManagerGUID,
                                                          assetManagerName,
                                                          primaryKeyGUID,
                                                          primaryKeyGUIDParameterName,
                                                          OpenMetadataType.SCHEMA_ATTRIBUTE.typeName,
                                                          foreignKeyGUID,
                                                          foreignKeyGUIDParameterName,
                                                          OpenMetadataType.SCHEMA_ATTRIBUTE.typeName,
                                                          forLineage,
                                                          forDuplicateProcessing,
                                                          supportedZones,
                                                          OpenMetadataType.FOREIGN_KEY_RELATIONSHIP_TYPE_GUID,
                                                          OpenMetadataType.FOREIGN_KEY_RELATIONSHIP_TYPE_NAME,
                                                          false,
                                                          this.getForeignKeyProperties(foreignKeyProperties, methodName),
                                                          effectiveTime,
                                                          methodName);
    }


    /**
     * Remove the foreign key relationship between two schema elements.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param primaryKeyGUID unique identifier of the derived schema element
     * @param foreignKeyGUID unique identifier of the query target schema element
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName     calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearForeignKeyRelationship(String  userId,
                                            String  assetManagerGUID,
                                            String  assetManagerName,
                                            String  primaryKeyGUID,
                                            String  foreignKeyGUID,
                                            boolean forLineage,
                                            boolean forDuplicateProcessing,
                                            Date    effectiveTime,
                                            String  methodName) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        final String primaryKeyGUIDParameterName = "primaryKeyGUID";
        final String foreignKeyGUIDParameterName = "foreignKeyGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(primaryKeyGUID, primaryKeyGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(foreignKeyGUID, foreignKeyGUIDParameterName, methodName);

        schemaAttributeHandler.unlinkElementFromElement(userId,
                                                        false,
                                                        assetManagerGUID,
                                                        assetManagerName,
                                                        primaryKeyGUID,
                                                        primaryKeyGUIDParameterName,
                                                        OpenMetadataType.SCHEMA_ATTRIBUTE.typeName,
                                                        foreignKeyGUID,
                                                        foreignKeyGUIDParameterName,
                                                        OpenMetadataType.SCHEMA_ATTRIBUTE.typeGUID,
                                                        OpenMetadataType.SCHEMA_ATTRIBUTE.typeName,
                                                        forLineage,
                                                        forDuplicateProcessing,
                                                        OpenMetadataType.FOREIGN_KEY_RELATIONSHIP_TYPE_GUID,
                                                        OpenMetadataType.FOREIGN_KEY_RELATIONSHIP_TYPE_NAME,
                                                        effectiveTime,
                                                        methodName);
    }


    /**
     * Remove the metadata element representing a schema attribute.
     *
     * @param userId calling user
     * @param correlationProperties  properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param schemaAttributeGUID unique identifier of the metadata element to remove
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeSchemaAttribute(String                        userId,
                                      MetadataCorrelationProperties correlationProperties,
                                      String                        schemaAttributeGUID,
                                      boolean                       forLineage,
                                      boolean                       forDuplicateProcessing,
                                      Date                          effectiveTime,
                                      String                        methodName) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(schemaAttributeGUID, schemaAttributeGUIDParameterName, methodName);

        this.validateExternalIdentifier(userId,
                                        schemaAttributeGUID,
                                        schemaAttributeGUIDParameterName,
                                        OpenMetadataType.SCHEMA_ATTRIBUTE.typeName,
                                        correlationProperties,
                                        forLineage,
                                        forDuplicateProcessing,
                                        effectiveTime,
                                        methodName);

        schemaAttributeHandler.deleteBeanInRepository(userId,
                                                      getExternalSourceGUID(correlationProperties),
                                                      getExternalSourceName(correlationProperties),
                                                      schemaAttributeGUID,
                                                      schemaAttributeGUIDParameterName,
                                                      OpenMetadataType.SCHEMA_ATTRIBUTE.typeGUID,
                                                      OpenMetadataType.SCHEMA_ATTRIBUTE.typeName,
                                                      null,
                                                      null,
                                                      forLineage,
                                                      forDuplicateProcessing,
                                                      effectiveTime,
                                                      methodName);
    }


    /**
     * Retrieve the list of schema attribute metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName     calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<SchemaAttributeElement>   findSchemaAttributes(String  userId,
                                                               String  assetManagerGUID,
                                                               String  assetManagerName,
                                                               String  searchString,
                                                               int     startFrom,
                                                               int     pageSize,
                                                               boolean forLineage,
                                                               boolean forDuplicateProcessing,
                                                               Date    effectiveTime,
                                                               String  methodName) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        final String searchStringParameterName = "searchString";

        List<SchemaAttributeElement> results = schemaAttributeHandler.findSchemaAttributes(userId,
                                                                                           searchString,
                                                                                           searchStringParameterName,
                                                                                           OpenMetadataType.SCHEMA_ATTRIBUTE.typeGUID,
                                                                                           OpenMetadataType.SCHEMA_ATTRIBUTE.typeName,
                                                                                           null,
                                                                                           null,
                                                                                           startFrom,
                                                                                           pageSize,
                                                                                           forLineage,
                                                                                           forDuplicateProcessing,
                                                                                           effectiveTime,
                                                                                           methodName);

        addCorrelationPropertiesToSchemaAttributes(userId, assetManagerGUID, assetManagerName, results, forLineage, forDuplicateProcessing, effectiveTime, methodName);

        return results;
    }


    /**
     * Retrieve the list of schema attributes associated with a StructSchemaType or nested underneath a schema attribute.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param parentSchemaElementGUID unique identifier of the schema element of interest
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName calling method
     *
     * @return list of associated metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<SchemaAttributeElement> getNestedAttributes(String  userId,
                                                            String  assetManagerGUID,
                                                            String  assetManagerName,
                                                            String  parentSchemaElementGUID,
                                                            int     startFrom,
                                                            int     pageSize,
                                                            boolean forLineage,
                                                            boolean forDuplicateProcessing,
                                                            Date    effectiveTime,
                                                            String  methodName) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        final String elementGUIDParameterName = "parentSchemaElementGUID";

        List<SchemaAttributeElement> results = schemaAttributeHandler.getAttachedSchemaAttributes(userId,
                                                                                                  parentSchemaElementGUID,
                                                                                                  elementGUIDParameterName,
                                                                                                  OpenMetadataType.SCHEMA_ATTRIBUTE.typeName,
                                                                                                  startFrom,
                                                                                                  pageSize,
                                                                                                  forLineage,
                                                                                                  forDuplicateProcessing,
                                                                                                  effectiveTime,
                                                                                                  methodName);


        addCorrelationPropertiesToSchemaAttributes(userId, assetManagerGUID, assetManagerName, results, forLineage, forDuplicateProcessing, effectiveTime, methodName);

        return results;
    }


    /**
     * Retrieve the list of schema attribute metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName     calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<SchemaAttributeElement>   getSchemaAttributesByName(String  userId,
                                                                    String  assetManagerGUID,
                                                                    String  assetManagerName,
                                                                    String  name,
                                                                    int     startFrom,
                                                                    int     pageSize,
                                                                    boolean forLineage,
                                                                    boolean forDuplicateProcessing,
                                                                    Date    effectiveTime,
                                                                    String  methodName) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        List<SchemaAttributeElement> results = schemaAttributeHandler.getSchemaAttributesByName(userId,
                                                                                                OpenMetadataType.SCHEMA_ATTRIBUTE.typeGUID,
                                                                                                OpenMetadataType.SCHEMA_ATTRIBUTE.typeName,
                                                                                                name,
                                                                                                null,
                                                                                                null,
                                                                                                supportedZones,
                                                                                                startFrom,
                                                                                                pageSize,
                                                                                                forLineage,
                                                                                                forDuplicateProcessing,
                                                                                                effectiveTime,
                                                                                                methodName);

        addCorrelationPropertiesToSchemaAttributes(userId, assetManagerGUID, assetManagerName, results, forLineage, forDuplicateProcessing, effectiveTime, methodName);

        return results;
    }


    /**
     * Retrieve the schema attribute metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param schemaAttributeGUID unique identifier of the requested metadata element
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName     calling method
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public SchemaAttributeElement getSchemaAttributeByGUID(String  userId,
                                                           String  assetManagerGUID,
                                                           String  assetManagerName,
                                                           String  schemaAttributeGUID,
                                                           boolean forLineage,
                                                           boolean forDuplicateProcessing,
                                                           Date    effectiveTime,
                                                           String  methodName) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        final String guidParameterName = "schemaAttributeGUID";

        SchemaAttributeElement schemaAttributeElement = schemaAttributeHandler.getSchemaAttribute(userId,
                                                                                                  schemaAttributeGUID,
                                                                                                  guidParameterName,
                                                                                                  OpenMetadataType.SCHEMA_ATTRIBUTE.typeName,
                                                                                                  null,
                                                                                                  null,
                                                                                                  false,
                                                                                                  false,
                                                                                                  effectiveTime,
                                                                                                  methodName);

        if (schemaAttributeElement != null)
        {
            schemaAttributeElement.setCorrelationHeaders(this.getCorrelationProperties(userId,
                                                                                       schemaAttributeGUID,
                                                                                       guidParameterName,
                                                                                       OpenMetadataType.SCHEMA_ATTRIBUTE.typeName,
                                                                                       assetManagerGUID,
                                                                                       assetManagerName,
                                                                                       forLineage,
                                                                                       forDuplicateProcessing,
                                                                                       effectiveTime,
                                                                                       methodName));
        }

        return schemaAttributeElement;
    }
}
