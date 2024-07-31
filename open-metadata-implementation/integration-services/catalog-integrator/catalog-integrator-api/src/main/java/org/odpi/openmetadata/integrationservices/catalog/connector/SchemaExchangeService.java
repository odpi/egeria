/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.integrationservices.catalog.connector;

import org.odpi.openmetadata.accessservices.assetmanager.api.exchange.SchemaExchangeInterface;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.SchemaAttributeElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.SchemaTypeElement;
import org.odpi.openmetadata.accessservices.assetmanager.properties.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;
import org.odpi.openmetadata.frameworks.governanceaction.properties.ExternalIdentifierProperties;
import org.odpi.openmetadata.frameworks.openmetadata.enums.KeyPattern;
import org.odpi.openmetadata.frameworks.openmetadata.enums.PermittedSynchronization;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.ForeignKeyProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.SchemaAttributeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.SchemaTypeProperties;
import org.odpi.openmetadata.integrationservices.catalog.ffdc.CatalogIntegratorErrorCode;

import java.util.Date;
import java.util.List;


/**
 * LineageExchangeService is the context for managing process definitions and lineage linkage.
 */
public class SchemaExchangeService
{
    private final SchemaExchangeInterface  schemaExchangeClient;

    String                   userId;
    String                   assetManagerGUID;
    String                   assetManagerName;
    String                   connectorName;
    PermittedSynchronization permittedSynchronization;
    AuditLog                 auditLog;

    boolean forLineage = false;
    boolean forDuplicateProcessing = false;


    /**
     * Create a new client to exchange lineage content with open metadata.
     *
     * @param schemaExchangeClient client for exchange requests
     * @param permittedSynchronization direction(s) that metadata can flow
     * @param userId integration daemon's userId
     * @param assetManagerGUID unique identifier of the software server capability for the asset manager
     * @param assetManagerName unique name of the software server capability for the asset manager
     * @param connectorName name of the connector using this context
     * @param auditLog logging destination
     */
    SchemaExchangeService(SchemaExchangeInterface  schemaExchangeClient,
                          PermittedSynchronization permittedSynchronization,
                          String                   userId,
                          String                   assetManagerGUID,
                          String                   assetManagerName,
                          String                   connectorName,
                          AuditLog                 auditLog)
    {
        this.schemaExchangeClient     = schemaExchangeClient;
        this.permittedSynchronization = permittedSynchronization;
        this.userId                   = userId;
        this.assetManagerGUID         = assetManagerGUID;
        this.assetManagerName         = assetManagerName;
        this.connectorName            = connectorName;
        this.auditLog                 = auditLog;
    }



    /* ========================================================
     * Set up the forLineage flag
     */

    /**
     * Return whether retrieval requests from this service are to include elements with the Memento classification attached or not.
     *
     * @return boolean flag
     */
    public boolean isForLineage()
    {
        return forLineage;
    }


    /**
     * Set up whether retrieval requests from this service are to include elements with the Memento classification attached or not.
     *
     * @param forLineage boolean flag
     */
    public void setForLineage(boolean forLineage)
    {
        this.forLineage = forLineage;
    }


    /* ========================================================
     * Set up the forDuplicateProcessing flag
     */

    /**
     * Return whether retrieval requests from this service are to avoid merging duplicates or not.
     *
     * @return boolean flag
     */
    public boolean isForDuplicateProcessing()
    {
        return forDuplicateProcessing;
    }


    /**
     * Set up whether retrieval requests from this service are to avoid merging duplicates or not.
     *
     * @param forDuplicateProcessing boolean flag
     */
    public void setForDuplicateProcessing(boolean forDuplicateProcessing)
    {
        this.forDuplicateProcessing = forDuplicateProcessing;
    }
    

    /* =====================================================================================================================
     * A schemaType describes the structure of a data asset, process or port
     */

    /**
     * Create a new metadata element to represent a schema type.
     *
     * @param assetManagerIsHome ensure that only the asset manager can update this schema element
     * @param externalIdentifierProperties optional properties used to define an external identifier
     * @param schemaTypeProperties properties about the schema type to store
     *
     * @return unique identifier of the new schema type
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createSchemaType(boolean                      assetManagerIsHome,
                                   ExternalIdentifierProperties externalIdentifierProperties,
                                   SchemaTypeProperties         schemaTypeProperties) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        final String methodName = "createSchemaType";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            return schemaExchangeClient.createSchemaType(userId,
                                                         assetManagerGUID,
                                                         assetManagerName,
                                                         assetManagerIsHome,
                                                         externalIdentifierProperties,
                                                         forLineage,
                                                         forDuplicateProcessing,
                                                         schemaTypeProperties);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Create a new metadata element to represent a schema type.
     *
     * @param assetManagerIsHome ensure that only the asset manager can update this schema element
     * @param anchorGUID unique identifier of the intended anchor of the schema type
     * @param externalIdentifierProperties optional properties used to define an external identifier
     * @param schemaTypeProperties properties about the schema type to store
     *
     * @return unique identifier of the new schema type
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createAnchoredSchemaType(boolean                      assetManagerIsHome,
                                           String                       anchorGUID,
                                           ExternalIdentifierProperties externalIdentifierProperties,
                                           SchemaTypeProperties         schemaTypeProperties) throws InvalidParameterException,
                                                                                                     UserNotAuthorizedException,
                                                                                                     PropertyServerException
    {
        return schemaExchangeClient.createAnchoredSchemaType(userId,
                                                             assetManagerGUID,
                                                             assetManagerName,
                                                             assetManagerIsHome,
                                                             anchorGUID,
                                                             externalIdentifierProperties,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             schemaTypeProperties);
    }


    /**
     * Create a new metadata element to represent a schema type using an existing metadata element as a template.
     *
     * @param assetManagerIsHome ensure that only the asset manager can update this schema element
     * @param templateGUID unique identifier of the metadata element to copy
     * @param externalIdentifierProperties optional properties used to define an external identifier
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new schema type
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createSchemaTypeFromTemplate(boolean                      assetManagerIsHome,
                                               String                       templateGUID,
                                               ExternalIdentifierProperties externalIdentifierProperties,
                                               TemplateProperties           templateProperties) throws InvalidParameterException,
                                                                                                       UserNotAuthorizedException,
                                                                                                       PropertyServerException
    {
        final String methodName = "createSchemaTypeFromTemplate";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            return schemaExchangeClient.createSchemaTypeFromTemplate(userId,
                                                                     assetManagerGUID,
                                                                     assetManagerName,
                                                                     assetManagerIsHome,
                                                                     templateGUID,
                                                                     externalIdentifierProperties,
                                                                     templateProperties);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Update the metadata element representing a schema type.
     *
     * @param schemaTypeGUID unique identifier of the metadata element to update
     * @param schemaTypeExternalIdentifier unique identifier of the schema type in the external asset manager
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param schemaTypeProperties new properties for the metadata element
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateSchemaType(String               schemaTypeGUID,
                                 String               schemaTypeExternalIdentifier,
                                 boolean              isMergeUpdate,
                                 SchemaTypeProperties schemaTypeProperties,
                                 Date                 effectiveTime) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        final String methodName = "updateSchemaType";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            schemaExchangeClient.updateSchemaType(userId,
                                                  assetManagerGUID,
                                                  assetManagerName,
                                                  schemaTypeGUID,
                                                  schemaTypeExternalIdentifier,
                                                  isMergeUpdate,
                                                  schemaTypeProperties,
                                                  effectiveTime,
                                                  forLineage,
                                                  forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Connect a schema type to a data asset, process or port.
     *
     * @param assetManagerIsHome ensure that only the asset manager can update this relationship
     * @param schemaTypeGUID unique identifier of the schema type to connect
     * @param parentElementGUID unique identifier of the open metadata element that this schema type is to be connected to
     * @param parentElementTypeName unique type name of the open metadata element that this schema type is to be connected to
     * @param properties properties for the relationship
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupSchemaTypeParent(boolean                assetManagerIsHome,
                                      String                 schemaTypeGUID,
                                      String                 parentElementGUID,
                                      String                 parentElementTypeName,
                                      RelationshipProperties properties,
                                      Date                   effectiveTime) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        final String methodName = "setupSchemaTypeParent";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            schemaExchangeClient.setupSchemaTypeParent(userId,
                                                       assetManagerGUID,
                                                       assetManagerName,
                                                       assetManagerIsHome,
                                                       schemaTypeGUID,
                                                       parentElementGUID,
                                                       parentElementTypeName,
                                                       effectiveTime,
                                                       forLineage,
                                                       forDuplicateProcessing,
                                                       properties);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Remove the relationship between a schema type and its parent data asset, process or port.
     *
     * @param schemaTypeGUID unique identifier of the schema type to connect
     * @param parentElementGUID unique identifier of the open metadata element that this schema type is to be connected to
     * @param parentElementTypeName unique type name of the open metadata element that this schema type is to be connected to
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearSchemaTypeParent(String schemaTypeGUID,
                                      String parentElementGUID,
                                      String parentElementTypeName,
                                      Date   effectiveTime) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        final String methodName = "clearSchemaTypeParent";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            schemaExchangeClient.clearSchemaTypeParent(userId,
                                                       assetManagerGUID,
                                                       assetManagerName,
                                                       schemaTypeGUID,
                                                       parentElementGUID,
                                                       parentElementTypeName,
                                                       effectiveTime,
                                                       forLineage,
                                                       forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Create a relationship between two schema elements.  The name of the desired relationship, and any properties (including effectivity dates)
     * are passed on the API.
     *
     * @param assetManagerIsHome ensure that only the asset manager can update this relationship
     * @param endOneGUID unique identifier of the schema element at end one of the relationship
     * @param endTwoGUID unique identifier of the schema element at end two of the relationship
     * @param relationshipName name of the relationship to delete
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param properties relationship properties
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupSchemaElementRelationship(boolean                assetManagerIsHome,
                                               String                 endOneGUID,
                                               String                 endTwoGUID,
                                               String                 relationshipName,
                                               Date                   effectiveTime,
                                               RelationshipProperties properties) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        final String methodName = "setupSchemaElementRelationship";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            schemaExchangeClient.setupSchemaElementRelationship(userId,
                                                                assetManagerGUID,
                                                                assetManagerName,
                                                                assetManagerIsHome,
                                                                endOneGUID,
                                                                endTwoGUID,
                                                                relationshipName,
                                                                effectiveTime,
                                                                forLineage,
                                                                forDuplicateProcessing,
                                                                properties);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Remove a relationship between two schema elements.  The name of the desired relationship is passed on the API.
     *
     * @param endOneGUID unique identifier of the schema element at end one of the relationship
     * @param endTwoGUID unique identifier of the schema element at end two of the relationship
     * @param relationshipName name of the relationship to delete
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearSchemaElementRelationship(String  endOneGUID,
                                               String  endTwoGUID,
                                               String  relationshipName,
                                               Date    effectiveTime) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        final String methodName = "clearSchemaElementRelationship";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            schemaExchangeClient.clearSchemaElementRelationship(userId,
                                                                assetManagerGUID,
                                                                assetManagerName,
                                                                endOneGUID,
                                                                endTwoGUID,
                                                                relationshipName,
                                                                effectiveTime,
                                                                forLineage,
                                                                forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Remove the metadata element representing a schema type.
     *
     * @param schemaTypeGUID unique identifier of the metadata element to remove
     * @param schemaTypeExternalIdentifier unique identifier of the schema type in the external asset manager
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeSchemaType(String schemaTypeGUID,
                                 String schemaTypeExternalIdentifier,
                                 Date   effectiveTime) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException
    {
        final String methodName = "removeSchemaType";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            schemaExchangeClient.removeSchemaType(userId,
                                                  assetManagerGUID,
                                                  assetManagerName,
                                                  schemaTypeGUID,
                                                  schemaTypeExternalIdentifier,
                                                  effectiveTime,
                                                  forLineage,
                                                  forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Retrieve the list of schema type metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<SchemaTypeElement> findSchemaType(String searchString,
                                                  int    startFrom,
                                                  int    pageSize,
                                                  Date   effectiveTime) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        return schemaExchangeClient.findSchemaType(userId, assetManagerGUID, assetManagerName, searchString, startFrom, pageSize, effectiveTime, forLineage,
                                                   forDuplicateProcessing);
    }


    /**
     * Return the schema type associated with a specific open metadata element (data asset, process or port).
     *
     * @param parentElementGUID unique identifier of the open metadata element that this schema type is to be connected to
     * @param parentElementTypeName unique type name of the open metadata element that this schema type is to be connected to
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return metadata element describing the schema type associated with the requested parent element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public SchemaTypeElement getSchemaTypeForElement(String parentElementGUID,
                                                     String parentElementTypeName,
                                                     Date   effectiveTime) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        return schemaExchangeClient.getSchemaTypeForElement(userId,
                                                            assetManagerGUID,
                                                            assetManagerName,
                                                            parentElementGUID,
                                                            parentElementTypeName,
                                                            effectiveTime,
                                                            forLineage,
                                                            forDuplicateProcessing);
    }


    /**
     * Retrieve the list of schema type metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<SchemaTypeElement>   getSchemaTypeByName(String name,
                                                         int    startFrom,
                                                         int    pageSize,
                                                         Date   effectiveTime) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        return schemaExchangeClient.getSchemaTypeByName(userId, assetManagerGUID, assetManagerName, name, startFrom, pageSize, effectiveTime, forLineage,
                                                        forDuplicateProcessing);
    }


    /**
     * Retrieve the schema type metadata element with the supplied unique identifier.
     *
     * @param schemaTypeGUID unique identifier of the requested metadata element
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public SchemaTypeElement getSchemaTypeByGUID(String schemaTypeGUID,
                                                 Date   effectiveTime) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        return schemaExchangeClient.getSchemaTypeByGUID(userId, assetManagerGUID, assetManagerName, schemaTypeGUID, effectiveTime, forLineage,
                                                        forDuplicateProcessing);
    }


    /**
     * Retrieve the header of the metadata element connected to a schema type.
     *
     * @param schemaTypeGUID unique identifier of the requested metadata element
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return header for parent element (data asset, process, port)
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ElementHeader getSchemaTypeParent(String schemaTypeGUID,
                                             Date   effectiveTime) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        return schemaExchangeClient.getSchemaTypeParent(userId, assetManagerGUID, assetManagerName, schemaTypeGUID, effectiveTime, forLineage,
                                                        forDuplicateProcessing);
    }


    /* ===============================================================================
     * A schemaType typically contains many schema attributes, linked with relationships.
     */

    /**
     * Create a new metadata element to represent a schema attribute.
     *
     * @param assetManagerIsHome ensure that only the asset manager can update this schema attribute
     * @param schemaElementGUID unique identifier of the schemaType or Schema Attribute where the schema attribute is connected to
     * @param externalIdentifierProperties optional properties used to define an external identifier
     * @param schemaAttributeProperties properties for the schema attribute
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return unique identifier of the new metadata element for the schema attribute
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createSchemaAttribute(boolean                      assetManagerIsHome,
                                        String                       schemaElementGUID,
                                        ExternalIdentifierProperties externalIdentifierProperties,
                                        SchemaAttributeProperties    schemaAttributeProperties,
                                        Date                         effectiveTime) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        final String methodName = "createSchemaAttribute";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            return schemaExchangeClient.createSchemaAttribute(userId,
                                                              assetManagerGUID,
                                                              assetManagerName,
                                                              assetManagerIsHome,
                                                              schemaElementGUID,
                                                              externalIdentifierProperties,
                                                              schemaAttributeProperties,
                                                              effectiveTime,
                                                              forLineage,
                                                              forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(permittedSynchronization.getName(),
                                                                                                                               connectorName,
                                                                                                                               methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Create a new metadata element to represent a schema attribute using an existing metadata element as a template.
     *
     * @param assetManagerIsHome ensure that only the asset manager can update this schema element
     * @param schemaElementGUID unique identifier of the schemaType or Schema Attribute where the schema attribute is connected to
     * @param templateGUID unique identifier of the metadata element to copy
     * @param externalIdentifierProperties optional properties used to define an external identifier
     * @param templateProperties properties that override the template
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return unique identifier of the new metadata element for the schema attribute
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createSchemaAttributeFromTemplate(boolean                      assetManagerIsHome,
                                                    String                       schemaElementGUID,
                                                    String                       templateGUID,
                                                    ExternalIdentifierProperties externalIdentifierProperties,
                                                    TemplateProperties           templateProperties,
                                                    Date                         effectiveTime) throws InvalidParameterException,
                                                                                                       UserNotAuthorizedException,
                                                                                                       PropertyServerException
    {
        final String methodName = "createSchemaAttributeFromTemplate";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            return schemaExchangeClient.createSchemaAttributeFromTemplate(userId,
                                                                          assetManagerGUID,
                                                                          assetManagerName,
                                                                          assetManagerIsHome,
                                                                          schemaElementGUID,
                                                                          templateGUID,
                                                                          externalIdentifierProperties,
                                                                          templateProperties,
                                                                          effectiveTime,
                                                                          forLineage,
                                                                          forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Update the properties of the metadata element representing a schema attribute.
     *
     * @param schemaAttributeGUID unique identifier of the schema attribute to update
     * @param schemaAttributeExternalIdentifier unique identifier of the schema attribute in the external asset manager
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param schemaAttributeProperties new properties for the schema attribute
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateSchemaAttribute(String                    schemaAttributeGUID,
                                      String                    schemaAttributeExternalIdentifier,
                                      boolean                   isMergeUpdate,
                                      SchemaAttributeProperties schemaAttributeProperties,
                                      Date                      effectiveTime) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        final String methodName = "updateSchemaAttribute";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            schemaExchangeClient.updateSchemaAttribute(userId,
                                                       assetManagerGUID,
                                                       assetManagerName,
                                                       schemaAttributeGUID,
                                                       schemaAttributeExternalIdentifier,
                                                       isMergeUpdate,
                                                       schemaAttributeProperties,
                                                       effectiveTime,
                                                       forLineage,
                                                       forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Classify the schema type (or attribute if type is embedded) to indicate that it is a calculated value.
     *
     * @param assetManagerIsHome ensure that only the asset manager can update this schema attribute
     * @param schemaElementGUID unique identifier of the metadata element to update
     * @param schemaElementExternalIdentifier unique identifier of the schema element in the external asset manager
     * @param formula description of how the value is calculated
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setSchemaElementAsCalculatedValue(boolean assetManagerIsHome,
                                                  String  schemaElementGUID,
                                                  String  schemaElementExternalIdentifier,
                                                  String  formula,
                                                  Date    effectiveTime) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        final String methodName = "setGlossaryAsCanonical";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            schemaExchangeClient.setSchemaElementAsCalculatedValue(userId,
                                                                   assetManagerGUID,
                                                                   assetManagerName,
                                                                   assetManagerIsHome,
                                                                   schemaElementGUID,
                                                                   schemaElementExternalIdentifier,
                                                                   formula,
                                                                   effectiveTime,
                                                                   forLineage,
                                                                   forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Remove the calculated value designation from the schema element.
     *
     * @param schemaElementGUID unique identifier of the metadata element to update
     * @param schemaElementExternalIdentifier unique identifier of the schema element in the external asset manager
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearSchemaElementAsCalculatedValue(String schemaElementGUID,
                                                    String schemaElementExternalIdentifier,
                                                    Date   effectiveTime) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        final String methodName = "clearSchemaElementAsCalculatedValue";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            schemaExchangeClient.clearSchemaElementAsCalculatedValue(userId,
                                                                     assetManagerGUID,
                                                                     assetManagerName,
                                                                     schemaElementGUID,
                                                                     schemaElementExternalIdentifier,
                                                                     effectiveTime,
                                                                     forLineage,
                                                                     forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Classify the column schema attribute to indicate that it describes a primary key.
     *
     * @param assetManagerIsHome ensure that only the asset manager can update this classification
     * @param schemaAttributeGUID unique identifier of the metadata element to update
     * @param schemaAttributeExternalIdentifier unique identifier of the schema attribute in the external asset manager
     * @param primaryKeyName name of the primary key (if different from the column name)
     * @param primaryKeyPattern key pattern used to maintain the primary key
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupColumnAsPrimaryKey(boolean    assetManagerIsHome,
                                        String     schemaAttributeGUID,
                                        String     schemaAttributeExternalIdentifier,
                                        String     primaryKeyName,
                                        KeyPattern primaryKeyPattern,
                                        Date       effectiveTime) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String methodName = "setupColumnAsPrimaryKey";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            schemaExchangeClient.setupColumnAsPrimaryKey(userId,
                                                         assetManagerGUID,
                                                         assetManagerName,
                                                         assetManagerIsHome,
                                                         schemaAttributeGUID,
                                                         schemaAttributeExternalIdentifier,
                                                         primaryKeyName,
                                                         primaryKeyPattern,
                                                         effectiveTime,
                                                         forLineage,
                                                         forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Remove the primary key designation from the schema attribute.
     *
     * @param schemaAttributeGUID unique identifier of the metadata element to update
     * @param schemaAttributeExternalIdentifier unique identifier of the schema attribute in the external asset manager
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearColumnAsPrimaryKey(String schemaAttributeGUID,
                                        String schemaAttributeExternalIdentifier,
                                        Date   effectiveTime) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        final String methodName = "clearColumnAsPrimaryKey";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            schemaExchangeClient.clearColumnAsPrimaryKey(userId,
                                                         assetManagerGUID,
                                                         assetManagerName,
                                                         schemaAttributeGUID,
                                                         schemaAttributeExternalIdentifier,
                                                         effectiveTime,
                                                         forLineage,
                                                         forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Link two schema attributes together to show a foreign key relationship.
     *
     * @param assetManagerIsHome ensure that only the asset manager can update this relationship
     * @param primaryKeyGUID unique identifier of the derived schema element
     * @param foreignKeyGUID unique identifier of the query target schema element
     * @param foreignKeyProperties properties for the foreign key relationship
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupForeignKeyRelationship(boolean              assetManagerIsHome,
                                            String               primaryKeyGUID,
                                            String               foreignKeyGUID,
                                            ForeignKeyProperties foreignKeyProperties,
                                            Date                 effectiveTime) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        final String methodName = "setupForeignKeyRelationship";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            schemaExchangeClient.setupForeignKeyRelationship(userId,
                                                             assetManagerGUID,
                                                             assetManagerName,
                                                             assetManagerIsHome,
                                                             primaryKeyGUID,
                                                             foreignKeyGUID,
                                                             foreignKeyProperties,
                                                             effectiveTime,
                                                             forLineage,
                                                             forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Update the relationship properties for the query target.
     *
     * @param primaryKeyGUID unique identifier of the derived schema element
     * @param foreignKeyGUID unique identifier of the query target schema element
     * @param foreignKeyProperties properties for the foreign key relationship
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateForeignKeyRelationship(String               primaryKeyGUID,
                                             String               foreignKeyGUID,
                                             ForeignKeyProperties foreignKeyProperties,
                                             Date                 effectiveTime) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        final String methodName = "updateForeignKeyRelationship";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            schemaExchangeClient.updateForeignKeyRelationship(userId,
                                                              assetManagerGUID,
                                                              assetManagerName,
                                                              primaryKeyGUID,
                                                              foreignKeyGUID,
                                                              foreignKeyProperties,
                                                              effectiveTime,
                                                              forLineage,
                                                              forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Remove the foreign key relationship between two schema elements.
     *
     * @param primaryKeyGUID unique identifier of the derived schema element
     * @param foreignKeyGUID unique identifier of the query target schema element
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearForeignKeyRelationship(String primaryKeyGUID,
                                            String foreignKeyGUID,
                                            Date   effectiveTime) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String methodName = "clearForeignKeyRelationship";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            schemaExchangeClient.clearForeignKeyRelationship(userId,
                                                             assetManagerGUID,
                                                             assetManagerName,
                                                             primaryKeyGUID,
                                                             foreignKeyGUID,
                                                             effectiveTime,
                                                             forLineage,
                                                             forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Remove the metadata element representing a schema attribute.
     *
     * @param schemaAttributeGUID unique identifier of the metadata element to remove
     * @param schemaAttributeExternalIdentifier unique identifier of the schema attribute in the external asset manager
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeSchemaAttribute(String schemaAttributeGUID,
                                      String schemaAttributeExternalIdentifier,
                                      Date   effectiveTime) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        final String methodName = "removeSchemaAttribute";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            schemaExchangeClient.removeSchemaAttribute(userId,
                                                       assetManagerGUID,
                                                       assetManagerName,
                                                       schemaAttributeGUID,
                                                       schemaAttributeExternalIdentifier,
                                                       effectiveTime,
                                                       forLineage,
                                                       forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Retrieve the list of schema attribute metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<SchemaAttributeElement>   findSchemaAttributes(String searchString,
                                                               int    startFrom,
                                                               int    pageSize,
                                                               Date   effectiveTime) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        return schemaExchangeClient.findSchemaAttributes(userId, assetManagerGUID, assetManagerName, searchString, startFrom, pageSize, effectiveTime, forLineage,
                                                         forDuplicateProcessing);
    }


    /**
     * Retrieve the list of schema attributes associated with a schema element.
     *
     * @param parentSchemaElementGUID unique identifier of the schemaType of interest
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return list of associated metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<SchemaAttributeElement>    getNestedAttributes(String parentSchemaElementGUID,
                                                               int    startFrom,
                                                               int    pageSize,
                                                               Date   effectiveTime) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        return schemaExchangeClient.getNestedSchemaAttributes(userId, assetManagerGUID, assetManagerName, parentSchemaElementGUID, startFrom, pageSize, effectiveTime, forLineage,
                                                              forDuplicateProcessing);
    }


    /**
     * Retrieve the list of schema attribute metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<SchemaAttributeElement>   getSchemaAttributesByName(String name,
                                                                    int    startFrom,
                                                                    int    pageSize,
                                                                    Date   effectiveTime) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        return schemaExchangeClient.getSchemaAttributesByName(userId, assetManagerGUID, assetManagerName, name, startFrom, pageSize, effectiveTime, forLineage,
                                                              forDuplicateProcessing);
    }


    /**
     * Retrieve the schema attribute metadata element with the supplied unique identifier.
     *
     * @param schemaAttributeGUID unique identifier of the requested metadata element
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public SchemaAttributeElement getSchemaAttributeByGUID(String schemaAttributeGUID,
                                                           Date   effectiveTime) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        return schemaExchangeClient.getSchemaAttributeByGUID(userId, assetManagerGUID, assetManagerName, schemaAttributeGUID, effectiveTime, forLineage,
                                                             forDuplicateProcessing);
    }

}
