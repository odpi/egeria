/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.integrationservices.catalog.connector;

import org.odpi.openmetadata.accessservices.assetmanager.api.SchemaExchangeInterface;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.ElementHeader;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.SchemaAttributeElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.SchemaTypeElement;
import org.odpi.openmetadata.accessservices.assetmanager.properties.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.integrationservices.catalog.ffdc.CatalogIntegratorErrorCode;

import java.util.List;
import java.util.Map;


/**
 * LineageExchangeService is the context for managing process definitions and lineage linkage.
 */
public class SchemaExchangeService
{
    private SchemaExchangeInterface  schemaExchangeClient;

    String                   userId;
    String                   assetManagerGUID;
    String                   assetManagerName;
    String                   connectorName;
    SynchronizationDirection synchronizationDirection;
    AuditLog                 auditLog;


    /**
     * Create a new client to exchange lineage content with open metadata.
     *
     * @param schemaExchangeClient client for exchange requests
     * @param synchronizationDirection direction(s) that metadata can flow
     * @param userId integration daemon's userId
     * @param assetManagerGUID unique identifier of the software server capability for the asset manager
     * @param assetManagerName unique name of the software server capability for the asset manager
     * @param connectorName name of the connector using this context
     * @param auditLog logging destination
     */
    SchemaExchangeService(SchemaExchangeInterface  schemaExchangeClient,
                          SynchronizationDirection synchronizationDirection,
                          String                   userId,
                          String                   assetManagerGUID,
                          String                   assetManagerName,
                          String                   connectorName,
                          AuditLog                 auditLog)
    {
        this.schemaExchangeClient     = schemaExchangeClient;
        this.synchronizationDirection = synchronizationDirection;
        this.userId                   = userId;
        this.assetManagerGUID         = assetManagerGUID;
        this.assetManagerName         = assetManagerName;
        this.connectorName            = connectorName;
        this.auditLog                 = auditLog;
    }




    /* =====================================================================================================================
     * A schemaType describes the structure of a data asset, process or port
     */

    /**
     * Create a new metadata element to represent a schema type.
     *
     * @param assetManagerIsHome ensure that only the asset manager can update this schema element
     * @param schemaTypeExternalIdentifier unique identifier of the schema type in the external asset manager
     * @param schemaTypeExternalIdentifierName name of property for the external identifier in the external asset manager
     * @param schemaTypeExternalIdentifierUsage optional usage description for the external identifier when calling the external asset manager
     * @param schemaTypeExternalIdentifierKeyPattern pattern for the external identifier within the external asset manager (default is LOCAL_KEY)
     * @param mappingProperties additional properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param schemaTypeProperties properties about the schema type to store
     *
     * @return unique identifier of the new schema type
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createSchemaType(boolean              assetManagerIsHome,
                                   String               schemaTypeExternalIdentifier,
                                   String               schemaTypeExternalIdentifierName,
                                   String               schemaTypeExternalIdentifierUsage,
                                   KeyPattern           schemaTypeExternalIdentifierKeyPattern,
                                   Map<String, String>  mappingProperties,
                                   SchemaTypeProperties schemaTypeProperties) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String methodName = "createSchemaType";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            return schemaExchangeClient.createSchemaType(userId,
                                                         assetManagerGUID,
                                                         assetManagerName,
                                                         assetManagerIsHome,
                                                         schemaTypeExternalIdentifier,
                                                         schemaTypeExternalIdentifierName,
                                                         schemaTypeExternalIdentifierUsage,
                                                         connectorName,
                                                         schemaTypeExternalIdentifierKeyPattern,
                                                         mappingProperties,
                                                         schemaTypeProperties);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    synchronizationDirection.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Create a new metadata element to represent a schema type using an existing metadata element as a template.
     *
     * @param assetManagerIsHome ensure that only the asset manager can update this schema element
     * @param templateGUID unique identifier of the metadata element to copy
     * @param schemaTypeExternalIdentifier unique identifier of the schema type in the external asset manager
     * @param schemaTypeExternalIdentifierName name of property for the external identifier in the external asset manager
     * @param schemaTypeExternalIdentifierUsage optional usage description for the external identifier when calling the external asset manager
     * @param schemaTypeExternalIdentifierKeyPattern pattern for the external identifier within the external asset manager (default is LOCAL_KEY)
     * @param mappingProperties additional properties to help with the mapping of the elements in the
     *                          external asset manager and open metadata
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new schema type
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createSchemaTypeFromTemplate(boolean             assetManagerIsHome,
                                               String              templateGUID,
                                               String              schemaTypeExternalIdentifier,
                                               String              schemaTypeExternalIdentifierName,
                                               String              schemaTypeExternalIdentifierUsage,
                                               KeyPattern          schemaTypeExternalIdentifierKeyPattern,
                                               Map<String, String> mappingProperties,
                                               TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        final String methodName = "createSchemaTypeFromTemplate";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            return schemaExchangeClient.createSchemaTypeFromTemplate(userId,
                                                                     assetManagerGUID,
                                                                     assetManagerName,
                                                                     assetManagerIsHome,
                                                                     templateGUID,
                                                                     schemaTypeExternalIdentifier,
                                                                     schemaTypeExternalIdentifierName,
                                                                     schemaTypeExternalIdentifierUsage,
                                                                     connectorName,
                                                                     schemaTypeExternalIdentifierKeyPattern,
                                                                     mappingProperties,
                                                                     templateProperties);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    synchronizationDirection.getName(),
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
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateSchemaType(String               schemaTypeGUID,
                                 String               schemaTypeExternalIdentifier,
                                 boolean              isMergeUpdate,
                                 SchemaTypeProperties schemaTypeProperties) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        final String methodName = "updateSchemaType";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            schemaExchangeClient.updateSchemaType(userId,
                                                  assetManagerGUID,
                                                  assetManagerName,
                                                  schemaTypeGUID,
                                                  schemaTypeExternalIdentifier,
                                                  isMergeUpdate,
                                                  schemaTypeProperties);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    synchronizationDirection.getName(),
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
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupSchemaTypeParent(boolean assetManagerIsHome,
                                      String  schemaTypeGUID,
                                      String  parentElementGUID,
                                      String  parentElementTypeName) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        final String methodName = "setupSchemaTypeParent";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            schemaExchangeClient.setupSchemaTypeParent(userId,
                                                       assetManagerGUID,
                                                       assetManagerName,
                                                       assetManagerIsHome,
                                                       schemaTypeGUID,
                                                       parentElementGUID,
                                                       parentElementTypeName);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    synchronizationDirection.getName(),
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
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearSchemaTypeParent(String schemaTypeGUID,
                                      String parentElementGUID,
                                      String parentElementTypeName) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        final String methodName = "clearSchemaTypeParent";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            schemaExchangeClient.clearSchemaTypeParent(userId,
                                                       assetManagerGUID,
                                                       assetManagerName,
                                                       schemaTypeGUID,
                                                       parentElementGUID,
                                                       parentElementTypeName);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    synchronizationDirection.getName(),
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
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeSchemaType(String schemaTypeGUID,
                                 String schemaTypeExternalIdentifier) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        final String methodName = "removeSchemaType";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            schemaExchangeClient.removeSchemaType(userId, assetManagerGUID, assetManagerName, schemaTypeGUID, schemaTypeExternalIdentifier);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    synchronizationDirection.getName(),
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
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<SchemaTypeElement> findSchemaType(String searchString,
                                                  int    startFrom,
                                                  int    pageSize) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        return schemaExchangeClient.findSchemaType(userId, assetManagerGUID, assetManagerName, searchString, startFrom, pageSize);
    }


    /**
     * Return the schema type associated with a specific open metadata element (data asset, process or port).
     *
     * @param parentElementGUID unique identifier of the open metadata element that this schema type is to be connected to
     * @param parentElementTypeName unique type name of the open metadata element that this schema type is to be connected to
     *
     * @return metadata element describing the schema type associated with the requested parent element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public SchemaTypeElement getSchemaTypeForElement(String parentElementGUID,
                                                     String parentElementTypeName) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        return schemaExchangeClient.getSchemaTypeForElement(userId, assetManagerGUID, assetManagerName, parentElementGUID, parentElementTypeName);
    }


    /**
     * Retrieve the list of schema type metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<SchemaTypeElement>   getSchemaTypeByName(String name,
                                                         int    startFrom,
                                                         int    pageSize) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        return schemaExchangeClient.getSchemaTypeByName(userId, assetManagerGUID, assetManagerName, name, startFrom, pageSize);
    }


    /**
     * Retrieve the schema type metadata element with the supplied unique identifier.
     *
     * @param schemaTypeGUID unique identifier of the requested metadata element
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public SchemaTypeElement getSchemaTypeByGUID(String schemaTypeGUID) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        return schemaExchangeClient.getSchemaTypeByGUID(userId, assetManagerGUID, assetManagerName, schemaTypeGUID);
    }


    /**
     * Retrieve the header of the metadata element connected to a schema type.
     *
     * @param schemaTypeGUID unique identifier of the requested metadata element
     *
     * @return header for parent element (data asset, process, port)
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ElementHeader getSchemaTypeParent(String schemaTypeGUID) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        return schemaExchangeClient.getSchemaTypeParent(userId, assetManagerGUID, assetManagerName, schemaTypeGUID);
    }


    /* ===============================================================================
     * A schemaType typically contains many schema attributes, linked with relationships.
     */

    /**
     * Create a new metadata element to represent a schema attribute.
     *
     * @param assetManagerIsHome ensure that only the asset manager can update this schema attribute
     * @param schemaElementGUID unique identifier of the schemaType or Schema Attribute where the schema attribute is connected to
     * @param schemaAttributeExternalIdentifier unique identifier of the schema attribute in the external asset manager
     * @param schemaAttributeExternalIdentifierName name of property for the external identifier in the external asset manager
     * @param schemaAttributeExternalIdentifierUsage optional usage description for the external identifier when calling the external asset manager
     * @param schemaAttributeExternalIdentifierKeyPattern pattern for the external identifier within the external asset manager (default is LOCAL_KEY)
     * @param mappingProperties additional properties to help with the mapping of the elements in the
     *                          external asset manager and open metadata
     * @param schemaAttributeProperties properties for the schema attribute
     *
     * @return unique identifier of the new metadata element for the schema attribute
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createSchemaAttribute(boolean                   assetManagerIsHome,
                                        String                    schemaElementGUID,
                                        String                    schemaAttributeExternalIdentifier,
                                        String                    schemaAttributeExternalIdentifierName,
                                        String                    schemaAttributeExternalIdentifierUsage,
                                        KeyPattern                schemaAttributeExternalIdentifierKeyPattern,
                                        Map<String, String>       mappingProperties,
                                        SchemaAttributeProperties schemaAttributeProperties) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        final String methodName = "createSchemaAttribute";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            return schemaExchangeClient.createSchemaAttribute(userId,
                                                              assetManagerGUID,
                                                              assetManagerName,
                                                              assetManagerIsHome,
                                                              schemaElementGUID,
                                                              schemaAttributeExternalIdentifier,
                                                              schemaAttributeExternalIdentifierName,
                                                              schemaAttributeExternalIdentifierUsage,
                                                              connectorName,
                                                              schemaAttributeExternalIdentifierKeyPattern,
                                                              mappingProperties,
                                                              schemaAttributeProperties);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(synchronizationDirection.getName(),
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
     * @param schemaAttributeExternalIdentifier unique identifier of the schema attribute in the external asset manager
     * @param schemaAttributeExternalIdentifierName name of property for the external identifier in the external asset manager
     * @param schemaAttributeExternalIdentifierUsage optional usage description for the external identifier when calling the external asset manager
     * @param schemaAttributeExternalIdentifierKeyPattern pattern for the external identifier within the external asset manager (default is LOCAL_KEY)
     * @param mappingProperties additional properties to help with the mapping of the elements in the
     *                          external asset manager and open metadata
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new metadata element for the schema attribute
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createSchemaAttributeFromTemplate(boolean             assetManagerIsHome,
                                                    String              schemaElementGUID,
                                                    String              templateGUID,
                                                    String              schemaAttributeExternalIdentifier,
                                                    String              schemaAttributeExternalIdentifierName,
                                                    String              schemaAttributeExternalIdentifierUsage,
                                                    KeyPattern          schemaAttributeExternalIdentifierKeyPattern,
                                                    Map<String, String> mappingProperties,
                                                    TemplateProperties  templateProperties) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        final String methodName = "createSchemaAttributeFromTemplate";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            return schemaExchangeClient.createSchemaAttributeFromTemplate(userId,
                                                                          assetManagerGUID,
                                                                          assetManagerName,
                                                                          assetManagerIsHome,
                                                                          schemaElementGUID,
                                                                          templateGUID,
                                                                          schemaAttributeExternalIdentifier,
                                                                          schemaAttributeExternalIdentifierName,
                                                                          schemaAttributeExternalIdentifierUsage,
                                                                          connectorName,
                                                                          schemaAttributeExternalIdentifierKeyPattern,
                                                                          mappingProperties,
                                                                          templateProperties);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    synchronizationDirection.getName(),
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
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateSchemaAttribute(String                    schemaAttributeGUID,
                                      String                    schemaAttributeExternalIdentifier,
                                      boolean                   isMergeUpdate,
                                      SchemaAttributeProperties schemaAttributeProperties) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        final String methodName = "updateSchemaAttribute";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            schemaExchangeClient.updateSchemaAttribute(userId,
                                                       assetManagerGUID,
                                                       assetManagerName,
                                                       schemaAttributeGUID,
                                                       schemaAttributeExternalIdentifier,
                                                       isMergeUpdate,
                                                       schemaAttributeProperties);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    synchronizationDirection.getName(),
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
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setSchemaElementAsCalculatedValue(boolean assetManagerIsHome,
                                                  String  schemaElementGUID,
                                                  String  schemaElementExternalIdentifier,
                                                  String  formula) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        final String methodName = "setGlossaryAsCanonical";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            schemaExchangeClient.setSchemaElementAsCalculatedValue(userId, assetManagerGUID, assetManagerName, assetManagerIsHome, schemaElementGUID, schemaElementExternalIdentifier, formula);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    synchronizationDirection.getName(),
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
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearSchemaElementAsCalculatedValue(String schemaElementGUID,
                                                    String schemaElementExternalIdentifier) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        final String methodName = "clearSchemaElementAsCalculatedValue";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            schemaExchangeClient.clearSchemaElementAsCalculatedValue(userId, assetManagerGUID, assetManagerName, schemaElementGUID, schemaElementExternalIdentifier);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    synchronizationDirection.getName(),
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
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param assetManagerIsHome ensure that only the asset manager can update this classification
     * @param schemaAttributeGUID unique identifier of the metadata element to update
     * @param schemaAttributeExternalIdentifier unique identifier of the schema attribute in the external asset manager
     * @param primaryKeyName name of the primary key (if different from the column name)
     * @param primaryKeyPattern key pattern used to maintain the primary key
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
                                        String     schemaAttributeExternalIdentifier,
                                        String     primaryKeyName,
                                        KeyPattern primaryKeyPattern) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        final String methodName = "setupColumnAsPrimaryKey";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            schemaExchangeClient.setupColumnAsPrimaryKey(userId,
                                                         assetManagerGUID,
                                                         assetManagerName,
                                                         assetManagerIsHome,
                                                         schemaAttributeGUID,
                                                         schemaAttributeExternalIdentifier,
                                                         primaryKeyName,
                                                         primaryKeyPattern);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    synchronizationDirection.getName(),
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
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearColumnAsPrimaryKey(String schemaAttributeGUID,
                                        String schemaAttributeExternalIdentifier) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        final String methodName = "clearColumnAsPrimaryKey";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            schemaExchangeClient.clearColumnAsPrimaryKey(userId,
                                                         assetManagerGUID,
                                                         assetManagerName,
                                                         schemaAttributeGUID,
                                                         schemaAttributeExternalIdentifier);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    synchronizationDirection.getName(),
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
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupForeignKeyRelationship(boolean              assetManagerIsHome,
                                            String               primaryKeyGUID,
                                            String               foreignKeyGUID,
                                            ForeignKeyProperties foreignKeyProperties) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException
    {
        final String methodName = "setupForeignKeyRelationship";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            schemaExchangeClient.setupForeignKeyRelationship(userId,
                                                             assetManagerGUID,
                                                             assetManagerName,
                                                             assetManagerIsHome,
                                                             primaryKeyGUID,
                                                             foreignKeyGUID,
                                                             foreignKeyProperties);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    synchronizationDirection.getName(),
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
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param primaryKeyGUID unique identifier of the derived schema element
     * @param foreignKeyGUID unique identifier of the query target schema element
     * @param foreignKeyProperties properties for the foreign key relationship
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
                                             ForeignKeyProperties foreignKeyProperties) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        final String methodName = "updateForeignKeyRelationship";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            schemaExchangeClient.updateForeignKeyRelationship(userId,
                                                              assetManagerGUID,
                                                              assetManagerName,
                                                              primaryKeyGUID,
                                                              foreignKeyGUID,
                                                              foreignKeyProperties);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    synchronizationDirection.getName(),
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
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearForeignKeyRelationship(String primaryKeyGUID,
                                            String foreignKeyGUID) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        final String methodName = "clearForeignKeyRelationship";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            schemaExchangeClient.clearForeignKeyRelationship(userId,
                                                             assetManagerGUID,
                                                             assetManagerName,
                                                             primaryKeyGUID,
                                                             foreignKeyGUID);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    synchronizationDirection.getName(),
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
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeSchemaAttribute(String schemaAttributeGUID,
                                      String schemaAttributeExternalIdentifier) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        final String methodName = "removeSchemaAttribute";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            schemaExchangeClient.removeSchemaAttribute(userId, assetManagerGUID, assetManagerName, schemaAttributeGUID, schemaAttributeExternalIdentifier);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    synchronizationDirection.getName(),
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
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<SchemaAttributeElement>   findSchemaAttributes(String searchString,
                                                               int    startFrom,
                                                               int    pageSize) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        return schemaExchangeClient.findSchemaAttributes(userId, assetManagerGUID, assetManagerName, searchString, startFrom, pageSize);
    }


    /**
     * Retrieve the list of schema attributes associated with a schemaType.
     *
     * @param schemaTypeGUID unique identifier of the schemaType of interest
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of associated metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<SchemaAttributeElement>    getAttributesForSchemaType(String schemaTypeGUID,
                                                                      int    startFrom,
                                                                      int    pageSize) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException
    {
        return schemaExchangeClient.getAttributesForSchemaType(userId, assetManagerGUID, assetManagerName, schemaTypeGUID, startFrom, pageSize);
    }


    /**
     * Retrieve the list of schema attribute metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<SchemaAttributeElement>   getSchemaAttributesByName(String name,
                                                                    int    startFrom,
                                                                    int    pageSize) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        return schemaExchangeClient.getSchemaAttributesByName(userId, assetManagerGUID, assetManagerName, name, startFrom, pageSize);
    }


    /**
     * Retrieve the schema attribute metadata element with the supplied unique identifier.
     *
     * @param schemaAttributeGUID unique identifier of the requested metadata element
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public SchemaAttributeElement getSchemaAttributeByGUID(String schemaAttributeGUID) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException
    {
        return schemaExchangeClient.getSchemaAttributeByGUID(userId, assetManagerGUID, assetManagerName, schemaAttributeGUID);
    }

}
