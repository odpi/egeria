/* SPDX-License-Identifier: Apache 2.0 */
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
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;

import java.util.ArrayList;
import java.util.List;

/**
 * SchemaExchangeHandler is the server side handler for managing schema types and attributes.
 */
public class SchemaExchangeHandler extends ExchangeHandlerBase
{
    private SchemaTypeHandler<SchemaTypeElement>                              schemaTypeHandler;
    private SchemaAttributeHandler<SchemaAttributeElement, SchemaTypeElement> schemaAttributeHandler;

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
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    private void addCorrelationPropertiesToSchemaTypes(String                userId,
                                                       String                  assetManagerGUID,
                                                       String                  assetManagerName,
                                                       List<SchemaTypeElement> results,
                                                       String                  methodName) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        if ((results != null) && (assetManagerGUID != null))
        {
            for (MetadataElement glossary : results)
            {
                if ((glossary != null) && (glossary.getElementHeader() != null) && (glossary.getElementHeader().getGUID() != null))
                {
                    glossary.setCorrelationHeaders(this.getCorrelationProperties(userId,
                                                                                 glossary.getElementHeader().getGUID(),
                                                                                 schemaTypeGUIDParameterName,
                                                                                 OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME,
                                                                                 assetManagerGUID,
                                                                                 assetManagerName,
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
                                                            String                       methodName) throws InvalidParameterException,
                                                                                                               UserNotAuthorizedException,
                                                                                                               PropertyServerException
    {
        if ((results != null) && (assetManagerGUID != null))
        {
            for (MetadataElement schemaAttribute : results)
            {
                if ((schemaAttribute != null) && (schemaAttribute.getElementHeader() != null) && (schemaAttribute.getElementHeader().getGUID() != null))
                {
                    schemaAttribute.setCorrelationHeaders(this.getCorrelationProperties(userId,
                                                                                         schemaAttribute.getElementHeader().getGUID(),
                                                                                         schemaAttributeGUIDParameterName,
                                                                                         OpenMetadataAPIMapper.SCHEMA_ATTRIBUTE_TYPE_NAME,
                                                                                         assetManagerGUID,
                                                                                         assetManagerName,
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
     * @param schemaTypeProperties properties about the schema type to store
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
                                   SchemaTypeProperties          schemaTypeProperties,
                                   String                        methodName) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
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

        String schemaTypeGUID = schemaTypeHandler.addSchemaType(userId,
                                                                this.getExternalSourceGUID(correlationProperties, assetManagerIsHome),
                                                                this.getExternalSourceName(correlationProperties, assetManagerIsHome),
                                                                builder,
                                                                methodName);

        if (schemaTypeGUID != null)
        {
            this.maintainSupplementaryProperties(userId,
                                                 schemaTypeGUID,
                                                 schemaTypeProperties.getQualifiedName(),
                                                 schemaTypeProperties,
                                                 false,
                                                 methodName);

            this.createExternalIdentifier(userId,
                                          schemaTypeGUID,
                                          schemaTypeGUIDParameterName,
                                          OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME,
                                          correlationProperties,
                                          methodName);
        }

        return schemaTypeGUID;
    }


    /**
     * Recursively navigate through the schema type loading up the a new schema type builder to pass to the
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
        String typeName = OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME;

        if (schemaType.getTypeName() != null)
        {
            typeName = schemaType.getTypeName();
        }

        String typeGUID = invalidParameterHandler.validateTypeName(typeName,
                                                                   OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME,
                                                                   serviceName,
                                                                   methodName,
                                                                   repositoryHelper);

        SchemaTypeBuilder schemaTypeBuilder = new SchemaTypeBuilder(schemaType.getQualifiedName(),
                                                                    schemaType.getDisplayName(),
                                                                    schemaType.getDescription(),
                                                                    schemaType.getVersionNumber(),
                                                                    schemaType.getIsDeprecated(),
                                                                    schemaType.getAuthor(),
                                                                    schemaType.getUsage(),
                                                                    schemaType.getEncodingStandard(),
                                                                    schemaType.getNamespace(),
                                                                    schemaType.getAdditionalProperties(),
                                                                    typeGUID,
                                                                    typeName,
                                                                    schemaType.getExtendedProperties(),
                                                                    repositoryHelper,
                                                                    serviceName,
                                                                    serverName);

        if (schemaType instanceof LiteralSchemaTypeProperties)
        {
            LiteralSchemaTypeProperties literalSchemaTypeProperties = (LiteralSchemaTypeProperties)schemaType;

            schemaTypeBuilder.setDataType(literalSchemaTypeProperties.getDataType());
            schemaTypeBuilder.setFixedValue(literalSchemaTypeProperties.getFixedValue());
        }
        else if (schemaType instanceof SimpleSchemaTypeProperties)
        {
            SimpleSchemaTypeProperties simpleSchemaTypeProperties = (SimpleSchemaTypeProperties)schemaType;

            schemaTypeBuilder.setDataType(simpleSchemaTypeProperties.getDataType());
            schemaTypeBuilder.setDefaultValue(simpleSchemaTypeProperties.getDefaultValue());

            if (schemaType instanceof EnumSchemaTypeProperties)
            {
                EnumSchemaTypeProperties enumSchemaTypeProperties = (EnumSchemaTypeProperties)schemaType;

                schemaTypeBuilder.setValidValuesSetGUID(enumSchemaTypeProperties.getValidValueSetGUID());
            }
            else if (schemaType instanceof ExternalSchemaTypeProperties)
            {
                ExternalSchemaTypeProperties externalSchemaTypeProperties = (ExternalSchemaTypeProperties)schemaType;

                schemaTypeBuilder.setExternalSchemaTypeGUID(externalSchemaTypeProperties.getExternalSchemaTypeGUID());
            }
        }
        else if (schemaType instanceof SchemaTypeChoiceProperties)
        {
            SchemaTypeChoiceProperties schemaTypeChoiceProperties = (SchemaTypeChoiceProperties)schemaType;

            if (schemaTypeChoiceProperties.getSchemaOptions() != null)
            {
                List<SchemaTypeBuilder> schemaOptionBuilders = new ArrayList<>();

                for (SchemaTypeProperties schemaOption : schemaTypeChoiceProperties.getSchemaOptions())
                {
                    if (schemaOption != null)
                    {
                        schemaOptionBuilders.add(this.getSchemaTypeBuilder(schemaOption,
                                                                           repositoryHelper,
                                                                           serviceName,
                                                                           serverName,
                                                                           methodName));
                    }
                }

                if (! schemaOptionBuilders.isEmpty())
                {
                    schemaTypeBuilder.setSchemaOptions(schemaOptionBuilders);
                }
            }
        }
        else if (schemaType instanceof MapSchemaTypeProperties)
        {
            MapSchemaTypeProperties mapSchemaTypeProperties = (MapSchemaTypeProperties)schemaType;

            SchemaTypeBuilder mapFromBuilder = null;
            SchemaTypeBuilder mapToBuilder = null;

            if (mapSchemaTypeProperties.getMapFromElement() != null)
            {
                mapFromBuilder = this.getSchemaTypeBuilder(mapSchemaTypeProperties.getMapFromElement(),
                                                           repositoryHelper,
                                                           serviceName,
                                                           serverName,
                                                           methodName);
            }

            if (mapSchemaTypeProperties.getMapToElement() != null)
            {
                mapToBuilder = this.getSchemaTypeBuilder(mapSchemaTypeProperties.getMapToElement(),
                                                         repositoryHelper,
                                                         serviceName,
                                                         serverName,
                                                         methodName);
            }

            schemaTypeBuilder.setMapTypes(mapFromBuilder, mapToBuilder);
        }

        return schemaTypeBuilder;
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
                                          OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME,
                                          correlationProperties,
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
     * @param schemaTypeProperties new properties for the metadata element
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
                                 SchemaTypeProperties          schemaTypeProperties,
                                 String                        methodName) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        final String propertiesParameterName     = "schemaTypeProperties";
        final String qualifiedNameParameterName  = "schemaTypeProperties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(schemaTypeGUID, schemaTypeGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(schemaTypeProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(schemaTypeProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        this.validateExternalIdentifier(userId,
                                        schemaTypeGUID,
                                        schemaTypeGUIDParameterName,
                                        OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME,
                                        correlationProperties,
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
                                      String  methodName) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        final String schemaTypeGUIDParameterName    = "schemaTypeGUID";
        final String parentElementGUIDParameterName = "parentElementGUID";
        final String parentElementTypeParameterName = "parentElementTypeName";

        invalidParameterHandler.validateName(parentElementTypeName, parentElementTypeParameterName, methodName);

        if ((parentElementTypeName != null) && repositoryHelper.isTypeOf(serviceName,
                                                                         parentElementTypeName,
                                                                         OpenMetadataAPIMapper.PORT_TYPE_NAME))
        {
            schemaTypeHandler.linkElementToElement(userId,
                                                   this.getExternalSourceGUID(assetManagerGUID, assetManagerIsHome),
                                                   this.getExternalSourceName(assetManagerName, assetManagerIsHome),
                                                   parentElementGUID,
                                                   parentElementGUIDParameterName,
                                                   parentElementTypeName,
                                                   schemaTypeGUID,
                                                   schemaTypeGUIDParameterName,
                                                   OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME,
                                                   OpenMetadataAPIMapper.PORT_SCHEMA_RELATIONSHIP_TYPE_GUID,
                                                   OpenMetadataAPIMapper.PORT_SCHEMA_RELATIONSHIP_TYPE_NAME,
                                                   null,
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
                                                   OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME,
                                                   OpenMetadataAPIMapper.ASSET_TO_SCHEMA_TYPE_TYPE_GUID,
                                                   OpenMetadataAPIMapper.ASSET_TO_SCHEMA_TYPE_TYPE_NAME,
                                                   null,
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
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearSchemaTypeParent(String userId,
                                      String assetManagerGUID,
                                      String assetManagerName,
                                      String schemaTypeGUID,
                                      String parentElementGUID,
                                      String parentElementTypeName,
                                      String methodName) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        final String schemaTypeGUIDParameterName    = "schemaTypeGUID";
        final String parentElementGUIDParameterName = "parentElementGUID";
        final String parentElementTypeParameterName = "parentElementTypeName";

        invalidParameterHandler.validateName(parentElementTypeName, parentElementTypeParameterName, methodName);

        if ((parentElementTypeName != null) && repositoryHelper.isTypeOf(serviceName,
                                                                         parentElementTypeName,
                                                                         OpenMetadataAPIMapper.PORT_TYPE_NAME))
        {
            schemaTypeHandler.unlinkElementFromElement(userId,
                                                       false,
                                                       assetManagerGUID,
                                                       assetManagerName,
                                                       parentElementGUID,
                                                       parentElementGUIDParameterName,
                                                       parentElementTypeName,
                                                       schemaTypeGUID,
                                                       schemaTypeGUIDParameterName,
                                                       OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_GUID,
                                                       OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME,
                                                       OpenMetadataAPIMapper.PORT_SCHEMA_RELATIONSHIP_TYPE_GUID,
                                                       OpenMetadataAPIMapper.PORT_SCHEMA_RELATIONSHIP_TYPE_NAME,
                                                       methodName);
        }
        else
        {
            schemaTypeHandler.unlinkElementFromElement(userId,
                                                       false,
                                                       assetManagerGUID,
                                                       assetManagerName,
                                                       parentElementGUID,
                                                       parentElementGUIDParameterName,
                                                       parentElementTypeName,
                                                       schemaTypeGUID,
                                                       schemaTypeGUIDParameterName,
                                                       OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_GUID,
                                                       OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME,
                                                       OpenMetadataAPIMapper.ASSET_TO_SCHEMA_TYPE_TYPE_GUID,
                                                       OpenMetadataAPIMapper.ASSET_TO_SCHEMA_TYPE_TYPE_NAME,
                                                       methodName);
        }
    }


    /**
     * Remove the metadata element representing a schema type.
     *
     * @param userId calling user
     * @param correlationProperties  properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param schemaTypeGUID unique identifier of the metadata element to remove
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeSchemaType(String                        userId,
                                 MetadataCorrelationProperties correlationProperties,
                                 String                        schemaTypeGUID,
                                 String                        methodName) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(schemaTypeGUID, schemaTypeGUIDParameterName, methodName);

        this.validateExternalIdentifier(userId,
                                        schemaTypeGUID,
                                        schemaTypeGUIDParameterName,
                                        OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME,
                                        correlationProperties,
                                        methodName);

        schemaAttributeHandler.deleteBeanInRepository(userId,
                                                      getExternalSourceGUID(correlationProperties),
                                                      getExternalSourceName(correlationProperties),
                                                      schemaTypeGUID,
                                                      schemaTypeGUIDParameterName,
                                                      OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_GUID,
                                                      OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME,
                                                      null,
                                                      null,
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
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<SchemaTypeElement> findSchemaType(String userId,
                                                  String assetManagerGUID,
                                                  String assetManagerName,
                                                  String searchString,
                                                  int    startFrom,
                                                  int    pageSize,
                                                  String methodName) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        List<SchemaTypeElement> results = schemaTypeHandler.findSchemaTypes(userId,
                                                                            searchString,
                                                                            startFrom,
                                                                            pageSize,
                                                                            methodName);

        addCorrelationPropertiesToSchemaTypes(userId, assetManagerGUID, assetManagerName, results, methodName);

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
     * @param methodName     calling method
     *
     * @return metadata element describing the schema type associated with the requested parent element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public SchemaTypeElement getSchemaTypeForElement(String userId,
                                                     String assetManagerGUID,
                                                     String assetManagerName,
                                                     String parentElementGUID,
                                                     String parentElementTypeName,
                                                     String methodName) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        final String guidParameterName = "parentElementGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(parentElementGUID, guidParameterName, methodName);

        SchemaTypeElement schemaTypeElement;

        if ((parentElementTypeName != null) && repositoryHelper.isTypeOf(serviceName,
                                                                         parentElementTypeName,
                                                                         OpenMetadataAPIMapper.PORT_TYPE_NAME))
        {
            schemaTypeElement = schemaTypeHandler.getSchemaTypeForPort(userId,
                                                                       parentElementGUID,
                                                                       guidParameterName,
                                                                       methodName);
        }
        else
        {
            schemaTypeElement = schemaTypeHandler.getSchemaTypeForAsset(userId,
                                                                        parentElementGUID,
                                                                        guidParameterName,
                                                                        methodName);
        }

        if (schemaTypeElement != null)
        {
            schemaTypeElement.setCorrelationHeaders(this.getCorrelationProperties(userId,
                                                                                  schemaTypeElement.getElementHeader().getGUID(),
                                                                                  schemaTypeGUIDParameterName,
                                                                                  OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME,
                                                                                  assetManagerGUID,
                                                                                  assetManagerName,
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
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<SchemaTypeElement>   getSchemaTypeByName(String userId,
                                                         String assetManagerGUID,
                                                         String assetManagerName,
                                                         String name,
                                                         int    startFrom,
                                                         int    pageSize,
                                                         String methodName) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        List<SchemaTypeElement> results = schemaTypeHandler.getSchemaTypeByName(userId, name, startFrom, pageSize, methodName);

        addCorrelationPropertiesToSchemaTypes(userId, assetManagerGUID, assetManagerName, results, methodName);

        return results;
    }


    /**
     * Retrieve the schema type metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param schemaTypeGUID unique identifier of the requested metadata element
     * @param methodName calling method
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public SchemaTypeElement getSchemaTypeByGUID(String userId,
                                                 String assetManagerGUID,
                                                 String assetManagerName,
                                                 String schemaTypeGUID,
                                                 String methodName) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        final String guidParameterName = "schemaTypeGUID";

        SchemaTypeElement schemaTypeElement = schemaTypeHandler.getSchemaType(userId,
                                                                              schemaTypeGUID,
                                                                              guidParameterName,
                                                                              methodName);

        if (schemaTypeElement != null)
        {
            schemaTypeElement.setCorrelationHeaders(this.getCorrelationProperties(userId,
                                                                                  schemaTypeGUID,
                                                                                  guidParameterName,
                                                                                  OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME,
                                                                                  assetManagerGUID,
                                                                                  assetManagerName,
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
     * @param methodName     calling method
     *
     * @return header for parent element (data asset, process, port)
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ElementHeader getSchemaTypeParent(String userId,
                                             String assetManagerGUID,
                                             String assetManagerName,
                                             String schemaTypeGUID,
                                             String methodName) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        final String guidParameterName = "schemaTypeGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(schemaTypeGUID, guidParameterName, methodName);

        RepositoryRelationshipsIterator iterator = new RepositoryRelationshipsIterator(repositoryHandler,
                                                                                       userId,
                                                                                       schemaTypeGUID,
                                                                                       OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME,
                                                                                       null,
                                                                                       null,
                                                                                       0,
                                                                                       invalidParameterHandler.getMaxPagingSize(),
                                                                                       methodName);

        while (iterator.moreToReceive())
        {
            Relationship relationship = iterator.getNext();

            if ((relationship != null) && (relationship.getType() != null) &&
                        ((repositoryHelper.isTypeOf(serviceName,
                                                    relationship.getType().getTypeDefName(),
                                                    OpenMetadataAPIMapper.ASSET_TO_SCHEMA_TYPE_TYPE_NAME)) ||
                         (repositoryHelper.isTypeOf(serviceName,
                                                    relationship.getType().getTypeDefName(),
                                                    OpenMetadataAPIMapper.PORT_SCHEMA_RELATIONSHIP_TYPE_NAME))))
            {
                final String parentGUIDParameterName = "relationship.getEntityOneProxy().getGUID()";

                EntityDetail parentEntity = schemaTypeHandler.getEntityFromRepository(userId,
                                                                                      relationship.getEntityOneProxy().getGUID(),
                                                                                      parentGUIDParameterName,
                                                                                      OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
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
                                                                                        methodName);

        if (schemaAttributeGUID != null)
        {
            this.createExternalIdentifier(userId,
                                          schemaAttributeGUID,
                                          schemaAttributeGUIDParameterName,
                                          OpenMetadataAPIMapper.SCHEMA_ATTRIBUTE_TYPE_NAME,
                                          correlationProperties,
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
        String typeName = OpenMetadataAPIMapper.SCHEMA_ATTRIBUTE_TYPE_NAME;

        if (schemaAttributeProperties.getTypeName() != null)
        {
            typeName = schemaAttributeProperties.getTypeName();
        }

        String typeGUID = invalidParameterHandler.validateTypeName(typeName,
                                                                   OpenMetadataAPIMapper.SCHEMA_ATTRIBUTE_TYPE_NAME,
                                                                   serviceName,
                                                                   methodName,
                                                                   repositoryHelper);

        int sortOrder = 0;
        if (schemaAttributeProperties.getSortOrder() != null)
        {
            sortOrder = schemaAttributeProperties.getSortOrder().getOpenTypeOrdinal();
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
                                                    String                        methodName) throws InvalidParameterException,
                                                                                                     UserNotAuthorizedException,
                                                                                                     PropertyServerException
    {
        final String scehmaElementGUIDParameterName = "schemaElementGUID";
        final String templateGUIDParameterName   = "templateGUID";
        final String propertiesParameterName     = "templateProperties";
        final String qualifiedNameParameterName  = "templateProperties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(schemaElementGUID, scehmaElementGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(templateProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(templateProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        String schemaAttributeGUID = schemaAttributeHandler.createSchemaAttributeFromTemplate(userId,
                                                                                              getExternalSourceGUID(correlationProperties, assetManagerIsHome),
                                                                                              getExternalSourceName(correlationProperties, assetManagerIsHome),
                                                                                              schemaElementGUID,
                                                                                              scehmaElementGUIDParameterName,
                                                                                              templateGUID,
                                                                                              templateProperties.getQualifiedName(),
                                                                                              templateProperties.getDisplayName(),
                                                                                              templateProperties.getDescription(),
                                                                                              methodName);

        if (schemaAttributeGUID != null)
        {
            this.createExternalIdentifier(userId,
                                          schemaAttributeGUID,
                                          schemaAttributeGUIDParameterName,
                                          OpenMetadataAPIMapper.SCHEMA_ATTRIBUTE_TYPE_NAME,
                                          correlationProperties,
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
                                      String                        methodName) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        final String propertiesParameterName          = "schemaAttributeProperties";
        final String qualifiedNameParameterName       = "schemaAttributeProperties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(schemaAttributeGUID, schemaAttributeGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(schemaAttributeProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(schemaAttributeProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        this.validateExternalIdentifier(userId,
                                        schemaAttributeGUID,
                                        schemaAttributeGUIDParameterName,
                                        OpenMetadataAPIMapper.SCHEMA_ATTRIBUTE_TYPE_NAME,
                                        correlationProperties,
                                        methodName);

        SchemaAttributeBuilder schemaAttributeBuilder = this.getSchemaAttributeBuilder(userId,
                                                                                       schemaAttributeProperties,
                                                                                       methodName);

        schemaAttributeHandler.updateSchemaAttribute(userId,
                                                     getExternalSourceGUID(correlationProperties),
                                                     getExternalSourceName(correlationProperties),
                                                     schemaAttributeGUID,
                                                     schemaAttributeGUIDParameterName,
                                                     schemaAttributeBuilder.getInstanceProperties(methodName),
                                                     isMergeUpdate,
                                                     methodName);
    }


    /**
     * Classify the schema type (or attribute if type is embedded) to indicate that it is a calculated value.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param schemaElementGUID unique identifier of the metadata element to update
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
                                                  String  methodName) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        final String schemaElementGUIDParameterName = "schemaElementGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(schemaElementGUID, schemaElementGUIDParameterName, methodName);

        InstanceProperties properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                     null,
                                                                                     OpenMetadataAPIMapper.FORMULA_PROPERTY_NAME,
                                                                                     formula,
                                                                                     methodName);

        schemaAttributeHandler.setClassificationInRepository(userId,
                                                             getExternalSourceGUID(assetManagerGUID, assetManagerIsHome),
                                                             getExternalSourceName(assetManagerName, assetManagerIsHome),
                                                             schemaElementGUID,
                                                             schemaElementGUIDParameterName,
                                                             OpenMetadataAPIMapper.SCHEMA_ELEMENT_TYPE_NAME,
                                                             OpenMetadataAPIMapper.CALCULATED_VALUE_CLASSIFICATION_TYPE_NAME,
                                                             OpenMetadataAPIMapper.CALCULATED_VALUE_CLASSIFICATION_TYPE_GUID,
                                                             properties,
                                                             false,
                                                             methodName);
    }


    /**
     * Remove the calculated value designation from the schema element.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param schemaElementGUID unique identifier of the metadata element to update
     * @param methodName     calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearSchemaElementAsCalculatedValue(String userId,
                                                    String assetManagerGUID,
                                                    String assetManagerName,
                                                    String schemaElementGUID,
                                                    String methodName) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        final String schemaElementGUIDParameterName = "schemaElementGUID";

        schemaAttributeHandler.removeClassificationFromRepository(userId,
                                                                  assetManagerGUID,
                                                                  assetManagerName,
                                                                  schemaElementGUID,
                                                                  schemaElementGUIDParameterName,
                                                                  OpenMetadataAPIMapper.SCHEMA_ELEMENT_TYPE_NAME,
                                                                  OpenMetadataAPIMapper.CALCULATED_VALUE_CLASSIFICATION_TYPE_GUID,
                                                                  OpenMetadataAPIMapper.CALCULATED_VALUE_CLASSIFICATION_TYPE_NAME,
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
                                        String     methodName) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(schemaAttributeGUID, schemaAttributeGUIDParameterName, methodName);

        InstanceProperties properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                     null,
                                                                                     OpenMetadataAPIMapper.PRIMARY_KEY_NAME_PROPERTY_NAME,
                                                                                     primaryKeyName,
                                                                                     methodName);

        int keyPatternOrdinal = 0;
        if (primaryKeyPattern != null)
        {
            keyPatternOrdinal = primaryKeyPattern.getOpenTypeOrdinal();
        }

        try
        {
            properties = repositoryHelper.addEnumPropertyToInstance(serviceName,
                                                                    properties,
                                                                    OpenMetadataAPIMapper.PRIMARY_KEY_PATTERN_PROPERTY_NAME,
                                                                    OpenMetadataAPIMapper.KEY_PATTERN_ENUM_TYPE_GUID,
                                                                    OpenMetadataAPIMapper.KEY_PATTERN_ENUM_TYPE_NAME,
                                                                    keyPatternOrdinal,
                                                                    methodName);
        }
        catch (TypeErrorException classificationNotSupported)
        {
            throw new InvalidParameterException(classificationNotSupported, OpenMetadataAPIMapper.PRIMARY_KEY_PATTERN_PROPERTY_NAME);
        }

        schemaAttributeHandler.setClassificationInRepository(userId,
                                                             getExternalSourceGUID(assetManagerGUID, assetManagerIsHome),
                                                             getExternalSourceName(assetManagerName, assetManagerIsHome),
                                                             schemaAttributeGUID,
                                                             schemaAttributeGUIDParameterName,
                                                             OpenMetadataAPIMapper.SCHEMA_ATTRIBUTE_TYPE_NAME,
                                                             OpenMetadataAPIMapper.PRIMARY_KEY_CLASSIFICATION_TYPE_GUID,
                                                             OpenMetadataAPIMapper.PRIMARY_KEY_CLASSIFICATION_TYPE_NAME,
                                                             properties,
                                                             false,
                                                             methodName);
    }


    /**
     * Remove the primary key designation from the schema attribute.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param schemaAttributeGUID unique identifier of the metadata element to update
     * @param methodName     calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearColumnAsPrimaryKey(String userId,
                                        String assetManagerGUID,
                                        String assetManagerName,
                                        String schemaAttributeGUID,
                                        String methodName) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        schemaAttributeHandler.removeClassificationFromRepository(userId,
                                                                  assetManagerGUID,
                                                                  assetManagerName,
                                                                  schemaAttributeGUID,
                                                                  schemaAttributeGUIDParameterName,
                                                                  OpenMetadataAPIMapper.SCHEMA_ATTRIBUTE_TYPE_NAME,
                                                                  OpenMetadataAPIMapper.PRIMARY_KEY_CLASSIFICATION_TYPE_GUID,
                                                                  OpenMetadataAPIMapper.PRIMARY_KEY_CLASSIFICATION_TYPE_NAME,
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
     * @param methodName     calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupForeignKeyRelationship(String               userId,
                                            String               assetManagerGUID,
                                            String               assetManagerName,
                                            boolean              assetManagerIsHome,
                                            String               primaryKeyGUID,
                                            String               foreignKeyGUID,
                                            ForeignKeyProperties foreignKeyProperties,
                                            String               methodName) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        final String primaryKeyGUIDParameterName = "primaryKeyGUID";
        final String foreignKeyGUIDParameterName = "foreignKeyGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(primaryKeyGUID, primaryKeyGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(foreignKeyGUID, foreignKeyGUIDParameterName, methodName);

        schemaAttributeHandler.linkElementToElement(userId,
                                                    getExternalSourceGUID(assetManagerGUID, assetManagerIsHome),
                                                    getExternalSourceName(assetManagerName, assetManagerIsHome),
                                                    primaryKeyGUID,
                                                    primaryKeyGUIDParameterName,
                                                    OpenMetadataAPIMapper.SCHEMA_ATTRIBUTE_TYPE_NAME,
                                                    foreignKeyGUID,
                                                    foreignKeyGUIDParameterName,
                                                    OpenMetadataAPIMapper.SCHEMA_ATTRIBUTE_TYPE_NAME,
                                                    OpenMetadataAPIMapper.FOREIGN_KEY_RELATIONSHIP_TYPE_GUID,
                                                    OpenMetadataAPIMapper.FOREIGN_KEY_RELATIONSHIP_TYPE_NAME,
                                                    this.getForeignKeyProperties(foreignKeyProperties, methodName),
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
                                                                      OpenMetadataAPIMapper.FOREIGN_KEY_NAME_PROPERTY_NAME,
                                                                      foreignKeyProperties.getName(),
                                                                      methodName);
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.FOREIGN_KEY_DESCRIPTION_PROPERTY_NAME,
                                                                      foreignKeyProperties.getDescription(),
                                                                      methodName);
            properties = repositoryHelper.addIntPropertyToInstance(serviceName,
                                                                   properties,
                                                                   OpenMetadataAPIMapper.FOREIGN_KEY_CONFIDENCE_PROPERTY_NAME,
                                                                   foreignKeyProperties.getConfidence(),
                                                                   methodName);
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.FOREIGN_KEY_STEWARD_PROPERTY_NAME,
                                                                      foreignKeyProperties.getSteward(),
                                                                      methodName);
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.FOREIGN_KEY_SOURCE_PROPERTY_NAME,
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
     * @param methodName     calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateForeignKeyRelationship(String               userId,
                                             String               assetManagerGUID,
                                             String               assetManagerName,
                                             String               primaryKeyGUID,
                                             String               foreignKeyGUID,
                                             ForeignKeyProperties foreignKeyProperties,
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
                                                          OpenMetadataAPIMapper.SCHEMA_ATTRIBUTE_TYPE_NAME,
                                                          foreignKeyGUID,
                                                          foreignKeyGUIDParameterName,
                                                          OpenMetadataAPIMapper.SCHEMA_ATTRIBUTE_TYPE_NAME,
                                                          OpenMetadataAPIMapper.FOREIGN_KEY_RELATIONSHIP_TYPE_GUID,
                                                          OpenMetadataAPIMapper.FOREIGN_KEY_RELATIONSHIP_TYPE_NAME,
                                                          this.getForeignKeyProperties(foreignKeyProperties, methodName),
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
     * @param methodName     calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearForeignKeyRelationship(String userId,
                                            String assetManagerGUID,
                                            String assetManagerName,
                                            String primaryKeyGUID,
                                            String foreignKeyGUID,
                                            String methodName) throws InvalidParameterException,
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
                                                        OpenMetadataAPIMapper.SCHEMA_ATTRIBUTE_TYPE_NAME,
                                                        foreignKeyGUID,
                                                        foreignKeyGUIDParameterName,
                                                        OpenMetadataAPIMapper.SCHEMA_ATTRIBUTE_TYPE_GUID,
                                                        OpenMetadataAPIMapper.SCHEMA_ATTRIBUTE_TYPE_NAME,
                                                        OpenMetadataAPIMapper.FOREIGN_KEY_RELATIONSHIP_TYPE_GUID,
                                                        OpenMetadataAPIMapper.FOREIGN_KEY_RELATIONSHIP_TYPE_NAME,
                                                        methodName);
    }


    /**
     * Remove the metadata element representing a schema attribute.
     *
     * @param userId calling user
     * @param correlationProperties  properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param schemaAttributeGUID unique identifier of the metadata element to remove
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeSchemaAttribute(String                        userId,
                                      MetadataCorrelationProperties correlationProperties,
                                      String                        schemaAttributeGUID,
                                      String                        methodName) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(schemaAttributeGUID, schemaAttributeGUIDParameterName, methodName);

        this.validateExternalIdentifier(userId,
                                        schemaAttributeGUID,
                                        schemaAttributeGUIDParameterName,
                                        OpenMetadataAPIMapper.SCHEMA_ATTRIBUTE_TYPE_NAME,
                                        correlationProperties,
                                        methodName);

        schemaAttributeHandler.deleteBeanInRepository(userId,
                                                      getExternalSourceGUID(correlationProperties),
                                                      getExternalSourceName(correlationProperties),
                                                      schemaAttributeGUID,
                                                      schemaAttributeGUIDParameterName,
                                                      OpenMetadataAPIMapper.SCHEMA_ATTRIBUTE_TYPE_GUID,
                                                      OpenMetadataAPIMapper.SCHEMA_ATTRIBUTE_TYPE_NAME,
                                                      null,
                                                      null,
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
     * @param methodName     calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<SchemaAttributeElement>   findSchemaAttributes(String userId,
                                                               String assetManagerGUID,
                                                               String assetManagerName,
                                                               String searchString,
                                                               int    startFrom,
                                                               int    pageSize,
                                                               String methodName) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        final String searchStringParameterName = "searchString";

        List<SchemaAttributeElement> results = schemaAttributeHandler.findSchemaAttributes(userId,
                                                                                           searchString,
                                                                                           searchStringParameterName,
                                                                                           OpenMetadataAPIMapper.SCHEMA_ATTRIBUTE_TYPE_GUID,
                                                                                           OpenMetadataAPIMapper.SCHEMA_ATTRIBUTE_TYPE_NAME,
                                                                                           null,
                                                                                           null,
                                                                                           startFrom,
                                                                                           pageSize,
                                                                                           methodName);

        addCorrelationPropertiesToSchemaAttributes(userId, assetManagerGUID, assetManagerName, results, methodName);

        return results;
    }


    /**
     * Retrieve the list of schema attributes associated with a schemaType.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param schemaTypeGUID unique identifier of the schemaType of interest
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param methodName calling method
     *
     * @return list of associated metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<SchemaAttributeElement>    getAttributesForSchemaType(String userId,
                                                                      String assetManagerGUID,
                                                                      String assetManagerName,
                                                                      String schemaTypeGUID,
                                                                      int    startFrom,
                                                                      int    pageSize,
                                                                      String methodName) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException
    {
        List<SchemaAttributeElement> results = schemaAttributeHandler.getSchemaAttributesForComplexSchemaType(userId,
                                                                                                              schemaTypeGUID,
                                                                                                              schemaTypeGUIDParameterName,
                                                                                                              null,
                                                                                                              null,
                                                                                                              startFrom,
                                                                                                              pageSize,
                                                                                                              methodName);

        addCorrelationPropertiesToSchemaAttributes(userId, assetManagerGUID, assetManagerName, results, methodName);

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
     * @param methodName     calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<SchemaAttributeElement>   getSchemaAttributesByName(String userId,
                                                                    String assetManagerGUID,
                                                                    String assetManagerName,
                                                                    String name,
                                                                    int    startFrom,
                                                                    int    pageSize,
                                                                    String methodName) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        List<SchemaAttributeElement> results = schemaAttributeHandler.getSchemaAttributesByName(userId,
                                                                                                OpenMetadataAPIMapper.RELATIONAL_TABLE_TYPE_GUID,
                                                                                                OpenMetadataAPIMapper.RELATIONAL_TABLE_TYPE_NAME,
                                                                                                name,
                                                                                                null,
                                                                                                null,
                                                                                                startFrom,
                                                                                                pageSize,
                                                                                                methodName);

        addCorrelationPropertiesToSchemaAttributes(userId, assetManagerGUID, assetManagerName, results, methodName);

        return results;
    }


    /**
     * Retrieve the schema attribute metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param schemaAttributeGUID unique identifier of the requested metadata element
     * @param methodName     calling method
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public SchemaAttributeElement getSchemaAttributeByGUID(String userId,
                                                           String assetManagerGUID,
                                                           String assetManagerName,
                                                           String schemaAttributeGUID,
                                                           String methodName) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String guidParameterName = "schemaAttributeGUID";

        SchemaAttributeElement schemaAttributeElement = schemaAttributeHandler.getBeanFromRepository(userId,
                                                                                                     schemaAttributeGUID,
                                                                                                     guidParameterName,
                                                                                                     OpenMetadataAPIMapper.SCHEMA_ATTRIBUTE_TYPE_NAME,
                                                                                                     methodName);

        if (schemaAttributeElement != null)
        {
            schemaAttributeElement.setCorrelationHeaders(this.getCorrelationProperties(userId,
                                                                                       schemaAttributeGUID,
                                                                                       guidParameterName,
                                                                                       OpenMetadataAPIMapper.SCHEMA_ATTRIBUTE_TYPE_NAME,
                                                                                       assetManagerGUID,
                                                                                       assetManagerName,
                                                                                       methodName));
        }

        return schemaAttributeElement;
    }
}
