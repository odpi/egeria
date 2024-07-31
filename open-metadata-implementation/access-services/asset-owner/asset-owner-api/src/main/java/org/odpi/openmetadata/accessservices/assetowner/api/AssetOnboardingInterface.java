/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.api;


import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.SchemaAttributeElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.SchemaTypeElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.AssetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.SchemaAttributeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.SchemaTypeProperties;
import org.odpi.openmetadata.accessservices.assetowner.properties.TemplateProperties;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;

import java.util.List;
import java.util.Map;

/**
 * AssetOnboardingInterface provides the client-side interface for an asset owner to set up the metadata about their
 * asset.  This includes defining its name, source and license.
 */
public interface AssetOnboardingInterface
{
    /**
     * Add a simple asset description to the catalog.
     *
     * @param userId calling user (assumed to be the owner)
     * @param typeName specific type of the asset - this must match a defined subtype
     * @param qualifiedName unique name for the asset in the catalog
     * @param displayName display name for the asset in the catalog
     * @param description description of the asset in the catalog
     * @param additionalProperties additional properties added by the caller
     * @param extendedProperties properties from the subtypes
     *
     * @return unique identifier (guid) of the asset
     *
     * @throws InvalidParameterException full path or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    String  addAssetToCatalog(String               userId,
                              String               typeName,
                              String               qualifiedName,
                              String               displayName,
                              String               description,
                              Map<String, String>  additionalProperties,
                              Map<String, Object>  extendedProperties) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException;


    /**
     * Add a comprehensive asset description to the catalog.
     *
     * @param userId calling user
     * @param assetProperties properties for the asset
     *
     * @return unique identifier (guid) of the asset
     *
     * @throws InvalidParameterException full path or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    String  addAssetToCatalog(String          userId,
                              AssetProperties assetProperties) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException;


    /**
     * Create a new metadata element to represent an asset using an existing asset as a template.
     *
     * @param userId calling user
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    String addAssetToCatalogUsingTemplate(String             userId,
                                          String             templateGUID,
                                          TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException;


    /**
     * Update the metadata element representing an asset.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param assetProperties new properties for this element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void updateAsset(String              userId,
                     String              assetGUID,
                     boolean             isMergeUpdate,
                     AssetProperties     assetProperties) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException;




    /**
     * Link two asset together.
     * Use information from the relationship type definition to ensure the fromAssetGUID and toAssetGUID are the right way around.
     *
     * @param userId calling user
     * @param relationshipTypeName type name of relationship to create
     * @param fromAssetGUID unique identifier of the asset at end 1 of the relationship
     * @param toAssetGUID unique identifier of the asset at end 2 of the relationship
     * @param relationshipProperties unique identifier for this relationship
     *
     * @return unique identifier of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    String setupRelatedAsset(String                 userId,
                             String                 relationshipTypeName,
                             String                 fromAssetGUID,
                             String                 toAssetGUID,
                             RelationshipProperties relationshipProperties) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException;


    /**
     * Update relationship between two elements.
     *
     * @param userId calling user
     * @param relationshipTypeName type name of relationship to update
     * @param relationshipGUID unique identifier of the relationship
     * @param relationshipProperties description and/or purpose of the relationship
     * @param isMergeUpdate should the new properties be merged with the existing properties, or replace them entirely
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void   updateAssetRelationship(String                 userId,
                                   String                 relationshipTypeName,
                                   String                 relationshipGUID,
                                   boolean                isMergeUpdate,
                                   RelationshipProperties relationshipProperties) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException;


    /**
     * Remove the relationship between two elements.
     *
     * @param userId calling user
     * @param relationshipTypeName type name of relationship to delete
     * @param relationshipGUID unique identifier of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void clearAssetRelationship(String  userId,
                                String  relationshipTypeName,
                                String  relationshipGUID) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException;


    /**
     * Stores the supplied schema details in the catalog and attaches it to the asset.  If another schema is currently
     * attached to the asset, it is unlinked and deleted.  If more attributes need to be added in addition to the
     * ones supplied then this can be done with addSchemaAttributesToSchemaType().
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the asset that the schema is to be attached to
     * @param schemaType schema type to create and attach directly to the asset.
     * @param schemaAttributes optional schema attributes.
     *
     * @return guid of the schema type or
     *
     * @throws InvalidParameterException full path or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    String   addCombinedSchemaToAsset(String                          userId,
                                      String                          assetGUID,
                                      SchemaTypeProperties            schemaType,
                                      List<SchemaAttributeProperties> schemaAttributes) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException;


    /**
     * Stores the supplied schema type in the catalog and attaches it to the asset.  If another schema is currently
     * attached to the asset, it is unlinked and deleted.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the asset that the schema is to be attached to
     * @param schemaType schema type to create and attach directly to the asset.
     *
     * @return guid of the new schema type or
     *
     * @throws InvalidParameterException full path or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    String   addSchemaTypeToAsset(String               userId,
                                  String               assetGUID,
                                  SchemaTypeProperties schemaType) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException;


    /**
     * Links the supplied schema type directly to the asset.  If this schema is either not found, or
     * already attached to an asset, then an error occurs.  If another schema is currently
     * attached to the asset, it is unlinked and deleted.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the asset that the schema is to be attached to
     * @param schemaTypeGUID unique identifier of the schema type to attach
     *
     * @throws InvalidParameterException full path or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void   attachSchemaTypeToAsset(String            userId,
                                   String            assetGUID,
                                   String            schemaTypeGUID) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException;


    /**
     * Unlinks the schema from the asset but does not delete it.  This means it can be reattached to a different asset.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the asset that the schema is to be attached to
     * @return guid of detached schema type
     *
     * @throws InvalidParameterException full path or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    String detachSchemaTypeFromAsset(String          userId,
                                     String          assetGUID) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException;


    /**
     * Detaches and deletes an asset's schema.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the asset that the schema is to be attached to
     *
     * @throws InvalidParameterException full path or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void  deleteAssetSchemaType(String userId,
                                String assetGUID) throws InvalidParameterException,
                                                         UserNotAuthorizedException,
                                                         PropertyServerException;



    /* =====================================================================================================================
     * A schemaType describes the structure of a data asset, process or port
     */

    /**
     * Create a new metadata element to represent a schema type.
     *
     * @param userId calling user
     * @param schemaTypeProperties properties about the schema type to store
     *
     * @return unique identifier of the new schema type
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    String createSchemaType(String               userId,
                            SchemaTypeProperties schemaTypeProperties) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException;


    /**
     * Create a new metadata element to represent a schema type.
     *
     * @param userId calling user
     * @param anchorGUID unique identifier of the intended anchor of the schema type
     * @param schemaTypeProperties properties about the schema type to store
     *
     * @return unique identifier of the new schema type
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    String createAnchoredSchemaType(String                       userId,
                                    String                       anchorGUID,
                                    SchemaTypeProperties         schemaTypeProperties) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException;


    /**
     * Create a new metadata element to represent a schema type using an existing metadata element as a template.
     *
     * @param userId calling user
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new schema type
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    String createSchemaTypeFromTemplate(String             userId,
                                        String             templateGUID,
                                        TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException;


    /**
     * Update the metadata element representing a schema type.
     *
     * @param userId calling user
     * @param schemaTypeGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param schemaTypeProperties new properties for the metadata element
    *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void updateSchemaType(String               userId,
                          String               schemaTypeGUID,
                          boolean              isMergeUpdate,
                          SchemaTypeProperties schemaTypeProperties) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException;


    /**
     * Connect a schema type to a data asset, process or port.
     *
     * @param userId calling user
     * @param schemaTypeGUID unique identifier of the schema type to connect
     * @param parentElementGUID unique identifier of the open metadata element that this schema type is to be connected to
     * @param parentElementTypeName unique type name of the open metadata element that this schema type is to be connected to
     * @param properties properties for the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void setupSchemaTypeParent(String                 userId,
                               String                 schemaTypeGUID,
                               String                 parentElementGUID,
                               String                 parentElementTypeName,
                               RelationshipProperties properties) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException;


    /**
     * Remove the relationship between a schema type and its parent data asset, process or port.
     *
     * @param userId calling user
     * @param schemaTypeGUID unique identifier of the schema type to connect
     * @param parentElementGUID unique identifier of the open metadata element that this schema type is to be connected to
     * @param parentElementTypeName unique type name of the open metadata element that this schema type is to be connected to
    *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void clearSchemaTypeParent(String userId,
                               String schemaTypeGUID,
                               String parentElementGUID,
                               String parentElementTypeName) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException;


    /**
     * Create a relationship between two schema elements.  The name of the desired relationship, and any properties (including effectivity dates)
     * are passed on the API.
     *
     * @param userId calling user
     * @param endOneGUID unique identifier of the schema element at end one of the relationship
     * @param endTwoGUID unique identifier of the schema element at end two of the relationship
     * @param relationshipTypeName type of the relationship to create
     * @param properties relationship properties
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void setupSchemaElementRelationship(String                 userId,
                                        String                 endOneGUID,
                                        String                 endTwoGUID,
                                        String                 relationshipTypeName,
                                        RelationshipProperties properties) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException;


    /**
     * Remove a relationship between two schema elements.  The name of the desired relationship is passed on the API.
     *
     * @param userId calling user
     * @param endOneGUID unique identifier of the schema element at end one of the relationship
     * @param endTwoGUID unique identifier of the schema element at end two of the relationship
     * @param relationshipTypeName type of the relationship to delete
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void clearSchemaElementRelationship(String  userId,
                                        String  endOneGUID,
                                        String  endTwoGUID,
                                        String  relationshipTypeName) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException;


    /**
     * Remove the metadata element representing a schema type.
     *
     * @param userId calling user
     * @param schemaTypeGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void removeSchemaType(String  userId,
                          String  schemaTypeGUID) throws InvalidParameterException,
                                                         UserNotAuthorizedException,
                                                         PropertyServerException;


    /**
     * Retrieve the list of schema type metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
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
    List<SchemaTypeElement> findSchemaType(String  userId,
                                           String  searchString,
                                           int     startFrom,
                                           int     pageSize) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException;


    /**
     * Return the schema type associated with a specific open metadata element (data asset, process or port).
     *
     * @param userId calling user
     * @param parentElementGUID unique identifier of the open metadata element that this schema type is to be connected to
     * @param parentElementTypeName unique type name of the open metadata element that this schema type is to be connected to
    *
     * @return metadata element describing the schema type associated with the requested parent element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    SchemaTypeElement getSchemaTypeForElement(String  userId,
                                              String  parentElementGUID,
                                              String  parentElementTypeName) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException;


    /**
     * Retrieve the list of schema type metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
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
    List<SchemaTypeElement>   getSchemaTypeByName(String  userId,
                                                  String  name,
                                                  int     startFrom,
                                                  int     pageSize) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException;


    /**
     * Retrieve the schema type metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param schemaTypeGUID unique identifier of the requested metadata element
    *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    SchemaTypeElement getSchemaTypeByGUID(String  userId,
                                          String  schemaTypeGUID) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException;


    /**
     * Retrieve the header of the metadata element connected to a schema type.
     *
     * @param userId calling user
     * @param schemaTypeGUID unique identifier of the requested metadata element
    *
     * @return header for parent element (data asset, process, port)
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    ElementHeader getSchemaTypeParent(String  userId,
                                      String  schemaTypeGUID) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException;


    /* ===============================================================================
     * A schemaType typically contains many schema attributes, linked with relationships.
     */

    /**
     * Adds attributes to a complex schema type like a relational table, avro schema or a structured document.
     * This method can be called repeatedly to add many attributes to a schema.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the asset that the schema is to be attached to
     * @param parentGUID unique identifier of the schema element to anchor these attributes to.
     * @param schemaAttributes list of schema attribute objects.
     *
     * @throws InvalidParameterException full path or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void addSchemaAttributes(String                          userId,
                             String                          assetGUID,
                             String                          parentGUID,
                             List<SchemaAttributeProperties> schemaAttributes) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException;


    /**
     * Adds an attribute to a complex schema type like a relational table, avro schema or a structured document.
     * This method can be called repeatedly to add many attributes to a schema.  Since the GUID is returned, it is possible
     * to add nested schema attributes to another schema attribute.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the asset that the schema is to be attached to
     * @param parentGUID unique identifier of the schema element to anchor these attributes to.
     * @param schemaAttribute schema attribute object to add to the schema.
     *
     * @return list of unique identifiers for the new schema attributes returned in the same order as the supplied attribute or
     *
     * @throws InvalidParameterException full path or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    String addSchemaAttribute(String                    userId,
                              String                    assetGUID,
                              String                    parentGUID,
                              SchemaAttributeProperties schemaAttribute) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException;



    /**
     * Create a new metadata element to represent a schema attribute using an existing metadata element as a template.
     *
     * @param userId calling user
     * @param schemaElementGUID unique identifier of the schemaType or Schema Attribute where the schema attribute is connected to
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new metadata element for the schema attribute
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    String createSchemaAttributeFromTemplate(String                       userId,
                                             String                       schemaElementGUID,
                                             String                       templateGUID,
                                             TemplateProperties           templateProperties) throws InvalidParameterException,
                                                                                                     UserNotAuthorizedException,
                                                                                                     PropertyServerException;


    /**
     * Update the properties of the metadata element representing a schema attribute.
     *
     * @param userId calling user
     * @param schemaAttributeGUID unique identifier of the schema attribute to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param schemaAttributeProperties new properties for the schema attribute
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void updateSchemaAttribute(String                    userId,
                               String                    schemaAttributeGUID,
                               boolean                   isMergeUpdate,
                               SchemaAttributeProperties schemaAttributeProperties) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException;


    /**
     * Classify the schema type (or attribute if type is embedded) to indicate that it is a calculated value.
     *
     * @param userId calling user
     * @param schemaElementGUID unique identifier of the metadata element to update
     * @param formula description of the logic that maps data values to
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void setSchemaElementAsCalculatedValue(String  userId,
                                           String  schemaElementGUID,
                                           String  formula) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException;


    /**
     * Remove the calculated value designation from the schema element.
     *
     * @param userId calling user
     * @param schemaElementGUID unique identifier of the metadata element to update
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void clearSchemaElementAsCalculatedValue(String  userId,
                                             String  schemaElementGUID) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException;


    /**
     * Remove the metadata element representing a schema attribute.
     *
     * @param userId calling user
     * @param schemaAttributeGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void removeSchemaAttribute(String  userId,
                               String  schemaAttributeGUID) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException;


    /**
     * Retrieve the list of schema attribute metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
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
    List<SchemaAttributeElement>   findSchemaAttributes(String  userId,
                                                        String  searchString,
                                                        int     startFrom,
                                                        int     pageSize) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException;


    /**
     * Retrieve the list of schema attributes associated with a schema element.
     *
     * @param userId calling user
     * @param parentSchemaElementGUID unique identifier of the schema element of interest
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of associated metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<SchemaAttributeElement> getNestedSchemaAttributes(String  userId,
                                                           String  parentSchemaElementGUID,
                                                           int     startFrom,
                                                           int     pageSize) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException;


    /**
     * Retrieve the list of schema attribute metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
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
    List<SchemaAttributeElement>   getSchemaAttributesByName(String  userId,
                                                             String  name,
                                                             int     startFrom,
                                                             int     pageSize) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException;


    /**
     * Retrieve the schema attribute metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param schemaAttributeGUID unique identifier of the requested metadata element
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    SchemaAttributeElement getSchemaAttributeByGUID(String  userId,
                                                    String  schemaAttributeGUID) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException;

    /**
     * Adds a connection to an asset.  Assets can have multiple connections attached.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the asset to attach the connection to
     * @param assetSummary summary of the asset that is stored in the relationship between the asset and the connection.
     * @param connection connection object.  If the connection is already stored (matching guid)
     *                   then the existing connection is used.
     * @throws InvalidParameterException full path or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Deprecated
    void addConnectionToAsset(String        userId,
                              String        assetGUID,
                              String        assetSummary,
                              Connection    connection) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException;
}
