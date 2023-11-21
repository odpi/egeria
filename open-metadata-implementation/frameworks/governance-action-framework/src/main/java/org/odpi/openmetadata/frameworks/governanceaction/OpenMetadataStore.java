/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementStatus;
import org.odpi.openmetadata.frameworks.governanceaction.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.governanceaction.properties.OpenMetadataAttributeTypeDef;
import org.odpi.openmetadata.frameworks.governanceaction.properties.OpenMetadataAttributeTypeDefCategory;
import org.odpi.openmetadata.frameworks.governanceaction.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.OpenMetadataTypeDef;
import org.odpi.openmetadata.frameworks.governanceaction.properties.OpenMetadataTypeDefCategory;
import org.odpi.openmetadata.frameworks.governanceaction.properties.OpenMetadataTypeDefGallery;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RelatedMetadataElements;
import org.odpi.openmetadata.frameworks.governanceaction.search.SearchClassifications;
import org.odpi.openmetadata.frameworks.governanceaction.search.SearchProperties;
import org.odpi.openmetadata.frameworks.governanceaction.search.SequencingOrder;

import java.util.Date;
import java.util.List;

/**
 * OpenMetadataStore provides access to metadata elements stored in the metadata repositories.  It is implemented by the
 * abstract class OpenMetadataClient and used by all the governance action services to retrieve metadata.
 * <p>
 * The concrete class for OpenMetadataClient is implemented by a metadata repository provider. In Egeria, this class is
 * implemented in the GAF Metadata Management Services.
 */
public class OpenMetadataStore
{
    private final OpenMetadataClient openMetadataClient;
    private final String             userId;

    /**
     * Construct the open metadata store wrapper around the open metadata client.
     *
     * @param openMetadataClient client to retrieve values from the
     * @param userId userId for the governance service
     */
    public OpenMetadataStore(OpenMetadataClient openMetadataClient, String userId)
    {
        this.openMetadataClient = openMetadataClient;
        this.userId             = userId;
    }


    /**
     * Returns the list of different types of metadata organized into two groups.  The first are the
     * attribute type definitions (AttributeTypeDefs).  These provide types for properties in full
     * type definitions.  Full type definitions (TypeDefs) describe types for entities, relationships
     * and classifications.
     *
     * @param userId unique identifier for requesting user.
     *
     * @return TypeDefGallery  List of different categories of type definitions.
     *
     * @throws InvalidParameterException  the userId is null
     * @throws PropertyServerException    there is a problem communicating with the metadata repository.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public OpenMetadataTypeDefGallery getAllTypes(String userId) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException
    {
        return openMetadataClient.getAllTypes(userId);
    }


    /**
     * Returns all the TypeDefs for a specific category.
     *
     * @param userId   unique identifier for requesting user.
     * @param category enum value for the category of TypeDef to return.
     *
     * @return TypeDefs list.
     *
     * @throws InvalidParameterException  the TypeDefCategory is null.
     * @throws PropertyServerException    there is a problem communicating with the metadata repository.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public List<OpenMetadataTypeDef> findTypeDefsByCategory(String                      userId,
                                                            OpenMetadataTypeDefCategory category) throws InvalidParameterException,
                                                                                                         PropertyServerException,
                                                                                                         UserNotAuthorizedException
    {
        return openMetadataClient.findTypeDefsByCategory(userId, category);
    }


    /**
     * Returns all the AttributeTypeDefs for a specific category.
     *
     * @param userId   unique identifier for requesting user.
     * @param category enum value for the category of an AttributeTypeDef to return.
     *
     * @return AttributeTypeDefs list.
     *
     * @throws InvalidParameterException  the TypeDefCategory is null.
     * @throws PropertyServerException    there is a problem communicating with the metadata repository.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public List<OpenMetadataAttributeTypeDef> findAttributeTypeDefsByCategory(String                               userId,
                                                                              OpenMetadataAttributeTypeDefCategory category) throws InvalidParameterException,
                                                                                                                                    PropertyServerException,
                                                                                                                                    UserNotAuthorizedException
    {
        return openMetadataClient.findAttributeTypeDefsByCategory(userId, category);
    }


    /**
     * Return the types that are linked to the elements from the specified standard.
     *
     * @param userId       unique identifier for requesting user.
     * @param standard     name of the standard null means any.
     * @param organization name of the organization null means any.
     * @param identifier   identifier of the element in the standard null means any.
     *
     * @return TypeDefs list  each entry in the list contains a TypeDef.  This is a structure
     * describing the TypeDef's category and properties.
     *
     * @throws InvalidParameterException  all attributes of the external id are null.
     * @throws PropertyServerException    there is a problem communicating with the metadata repository.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public List<OpenMetadataTypeDef> findTypesByExternalID(String userId,
                                                           String standard,
                                                           String organization,
                                                           String identifier) throws InvalidParameterException,
                                                                                     PropertyServerException,
                                                                                     UserNotAuthorizedException
    {
        return openMetadataClient.findTypesByExternalId(userId, standard, organization, identifier);
    }


    /**
     * Return the TypeDef identified by the GUID.
     *
     * @param userId unique identifier for requesting user.
     * @param guid   String unique id of the TypeDef
     *
     * @return TypeDef structure describing its category and properties.
     *
     * @throws InvalidParameterException  the guid is null.
     * @throws PropertyServerException    there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public OpenMetadataTypeDef getTypeDefByGUID(String userId,
                                                String guid) throws InvalidParameterException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException
    {
        return openMetadataClient.getTypeDefByGUID(userId, guid);
    }


    /**
     * Return the AttributeTypeDef identified by the GUID.
     *
     * @param userId unique identifier for requesting user.
     * @param guid   String unique id of the TypeDef
     *
     * @return TypeDef structure describing its category and properties.
     *
     * @throws InvalidParameterException  the guid is null.
     * @throws PropertyServerException    there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public OpenMetadataAttributeTypeDef getAttributeTypeDefByGUID(String userId,
                                                                  String guid) throws InvalidParameterException,
                                                                                      PropertyServerException,
                                                                                      UserNotAuthorizedException
    {
        return openMetadataClient.getAttributeTypeDefByGUID(userId, guid);
    }


    /**
     * Return the TypeDef identified by the unique name.
     *
     * @param userId unique identifier for requesting user.
     * @param name   String name of the TypeDef.
     *
     * @return TypeDef structure describing its category and properties.
     *
     * @throws InvalidParameterException  the name is null.
     * @throws PropertyServerException    there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public OpenMetadataTypeDef getTypeDefByName(String userId,
                                                String name) throws InvalidParameterException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException
    {
        return openMetadataClient.getTypeDefByName(userId, name);
    }


    /**
     * Return the AttributeTypeDef identified by the unique name.
     *
     * @param userId unique identifier for requesting user.
     * @param name   String name of the TypeDef.
     *
     * @return TypeDef structure describing its category and properties.
     *
     * @throws InvalidParameterException  the name is null.
     * @throws PropertyServerException    there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public OpenMetadataAttributeTypeDef getAttributeTypeDefByName(String userId,
                                                                  String name) throws InvalidParameterException,
                                                                                      PropertyServerException,
                                                                                      UserNotAuthorizedException
    {
        return openMetadataClient.getAttributeTypeDefByName(userId, name);
    }


    /**
     * Retrieve the metadata element using its unique identifier.
     *
     * @param elementGUID unique identifier for the metadata element
     * @param forLineage the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     *
     * @return metadata element properties
     * @throws InvalidParameterException the unique identifier is null or not known.
     * @throws UserNotAuthorizedException the governance action service is not able to access the element
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    public OpenMetadataElement getMetadataElementByGUID(String  elementGUID,
                                                        boolean forLineage,
                                                        boolean forDuplicateProcessing,
                                                        Date    effectiveTime) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        return openMetadataClient.getMetadataElementByGUID(userId,
                                                           elementGUID,
                                                           forLineage,
                                                           forDuplicateProcessing,
                                                           effectiveTime);
    }


    /**
     * Retrieve the metadata element using its unique name (typically the qualified name).
     *
     * @param uniqueName unique name for the metadata element
     * @param uniquePropertyName name of property name to test in the open metadata element - if null "qualifiedName" is used
     * @param forLineage the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     *
     * @return metadata element properties or null if not found
     * @throws InvalidParameterException the unique identifier is null.
     * @throws UserNotAuthorizedException the governance action service is not able to access the element
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    public OpenMetadataElement getMetadataElementByUniqueName(String  uniqueName,
                                                              String  uniquePropertyName,
                                                              boolean forLineage,
                                                              boolean forDuplicateProcessing,
                                                              Date    effectiveTime) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        return openMetadataClient.getMetadataElementByUniqueName(userId,
                                                                 uniqueName,
                                                                 uniquePropertyName,
                                                                 forLineage,
                                                                 forDuplicateProcessing,
                                                                 effectiveTime);
    }


    /**
     * Retrieve the unique identifier of a metadata element using its unique name (typically the qualified name).
     *
     * @param uniqueName unique name for the metadata element
     * @param uniquePropertyName name of property name to test in the open metadata element - if null "qualifiedName" is used
     * @param forLineage the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     *
     * @return metadata element unique identifier (guid)
     * @throws InvalidParameterException the unique identifier is null or not known.
     * @throws UserNotAuthorizedException the governance action service is not able to access the element
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    public String getMetadataElementGUIDByUniqueName(String  uniqueName,
                                                     String  uniquePropertyName,
                                                     boolean forLineage,
                                                     boolean forDuplicateProcessing,
                                                     Date    effectiveTime) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        return openMetadataClient.getMetadataElementGUIDByUniqueName(userId,
                                                                     uniqueName,
                                                                     uniquePropertyName,
                                                                     forLineage,
                                                                     forDuplicateProcessing,
                                                                     effectiveTime);
    }


    /**
     * Retrieve the metadata elements that contain the requested string.
     *
     * @param searchString name to retrieve
     * @param forLineage the retrieved elements are for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved elements are for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime only return an element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements (or null if no elements match the name)
     * @throws InvalidParameterException the qualified name is null
     * @throws UserNotAuthorizedException the governance action service is not able to access the element
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    public List<OpenMetadataElement> findMetadataElementsWithString(String  searchString,
                                                                    boolean forLineage,
                                                                    boolean forDuplicateProcessing,
                                                                    Date    effectiveTime,
                                                                    int     startFrom,
                                                                    int     pageSize) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        return openMetadataClient.findMetadataElementsWithString(userId,
                                                                 searchString,
                                                                 forLineage,
                                                                 forDuplicateProcessing,
                                                                 effectiveTime,
                                                                 startFrom,
                                                                 pageSize);
    }


    /**
     * Retrieve the metadata elements of the requested type that contain the requested string.
     *
     * @param searchString name to retrieve
     * @param typeName    name of the type to limit the results to (maybe null to mean all types)
     * @param forLineage the retrieved elements are for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved elements are for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime only return an element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements (or null if no elements match the name)
     * @throws InvalidParameterException the qualified name is null
     * @throws UserNotAuthorizedException the governance action service is not able to access the element
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    public List<OpenMetadataElement> findMetadataElementsWithString(String  searchString,
                                                                    String  typeName,
                                                                    boolean forLineage,
                                                                    boolean forDuplicateProcessing,
                                                                    Date    effectiveTime,
                                                                    int     startFrom,
                                                                    int     pageSize) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        return openMetadataClient.findMetadataElementsWithString(userId,
                                                                 searchString,
                                                                 typeName,
                                                                 forLineage,
                                                                 forDuplicateProcessing,
                                                                 effectiveTime,
                                                                 startFrom,
                                                                 pageSize);
    }


    /**
     * Retrieve the metadata elements connected to the supplied element.
     *
     * @param elementGUID unique identifier for the starting metadata element
     * @param startingAtEnd indicates which end to retrieve from (0 is "either end"; 1 is end1; 2 is end 2)
     * @param relationshipTypeName type name of relationships to follow (or null for all)
     * @param forLineage the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved elements are for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime only return an element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of related elements
     * @throws InvalidParameterException the unique identifier is null or not known; the relationship type is invalid
     * @throws UserNotAuthorizedException the governance action service is not able to access the elements
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    public List<RelatedMetadataElement> getRelatedMetadataElements(String  elementGUID,
                                                                   int     startingAtEnd,
                                                                   String  relationshipTypeName,
                                                                   boolean forLineage,
                                                                   boolean forDuplicateProcessing,
                                                                   Date    effectiveTime,
                                                                   int     startFrom,
                                                                   int     pageSize) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        return openMetadataClient.getRelatedMetadataElements(userId,
                                                             elementGUID,
                                                             startingAtEnd,
                                                             relationshipTypeName,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             effectiveTime,
                                                             startFrom,
                                                             pageSize);
    }


    /**
     * Retrieve the relationships linking to the supplied elements.
     *
     * @param metadataElementAtEnd1GUID unique identifier of the metadata element at end 1 of the relationship
     * @param metadataElementAtEnd2GUID unique identifier of the metadata element at end 2 of the relationship
     * @param relationshipTypeName type name of relationships to follow (or null for all)
     * @param forLineage the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved elements are for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime only return an element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of related elements
     * @throws InvalidParameterException the unique identifier is null or not known; the relationship type is invalid
     * @throws UserNotAuthorizedException the governance action service is not able to access the elements
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    public List<RelatedMetadataElements> getMetadataElementRelationships(String  metadataElementAtEnd1GUID,
                                                                         String  metadataElementAtEnd2GUID,
                                                                         String  relationshipTypeName,
                                                                         boolean forLineage,
                                                                         boolean forDuplicateProcessing,
                                                                         Date    effectiveTime,
                                                                         int     startFrom,
                                                                         int     pageSize) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        return openMetadataClient.getMetadataElementRelationships(userId,
                                                                  metadataElementAtEnd1GUID,
                                                                  metadataElementAtEnd2GUID,
                                                                  relationshipTypeName,
                                                                  forLineage,
                                                                  forDuplicateProcessing,
                                                                  effectiveTime,
                                                                  startFrom,
                                                                  pageSize);
    }


    /**
     * Return a list of metadata elements that match the supplied criteria.  The results can be returned over many pages.
     *
     * @param metadataElementTypeName type of interest (null means any element type)
     * @param metadataElementSubtypeName optional list of the subtypes of the metadataElementTypeName to
     *                           include in the search results. Null means all subtypes.
     * @param searchProperties Optional list of entity property conditions to match.
     * @param limitResultsByStatus By default, entities in all statuses (other than DELETE) are returned.  However, it is possible
     *                             to specify a list of statuses (e.g. ACTIVE) to restrict the results to.  Null means all status values.
     * @param matchClassifications Optional list of classifications to match.
     * @param sequencingProperty String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param forLineage the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return a list of elements matching the supplied criteria; null means no matching elements in the metadata store.
     * @throws InvalidParameterException one of the search parameters are is invalid
     * @throws UserNotAuthorizedException the governance action service is not able to access the elements
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    public List<OpenMetadataElement> findMetadataElements(String                metadataElementTypeName,
                                                          List<String>          metadataElementSubtypeName,
                                                          SearchProperties      searchProperties,
                                                          List<ElementStatus>   limitResultsByStatus,
                                                          SearchClassifications matchClassifications,
                                                          String                sequencingProperty,
                                                          SequencingOrder       sequencingOrder,
                                                          boolean               forLineage,
                                                          boolean               forDuplicateProcessing,
                                                          Date                  effectiveTime,
                                                          int                   startFrom,
                                                          int                   pageSize) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        return openMetadataClient.findMetadataElements(userId,
                                                       metadataElementTypeName,
                                                       metadataElementSubtypeName,
                                                       searchProperties,
                                                       limitResultsByStatus,
                                                       matchClassifications,
                                                       sequencingProperty,
                                                       sequencingOrder,
                                                       forLineage,
                                                       forDuplicateProcessing,
                                                       effectiveTime,
                                                       startFrom,
                                                       pageSize);
    }


    /**
     * Return a list of relationships that match the requested conditions.  The results can be received as a series of pages.
     *
     * @param relationshipTypeName relationship's type.  Null means all types
     *                             (but may be slow so not recommended).
     * @param searchProperties Optional list of relationship property conditions to match.
     * @param sequencingProperty String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param forLineage the retrieved elements are for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime only return an element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return a list of relationships.  Null means no matching relationships.
     * @throws InvalidParameterException one of the search parameters are is invalid
     * @throws UserNotAuthorizedException the governance action service is not able to access the elements
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    public List<RelatedMetadataElements> findRelationshipsBetweenMetadataElements(String           relationshipTypeName,
                                                                                  SearchProperties searchProperties,
                                                                                  String           sequencingProperty,
                                                                                  SequencingOrder  sequencingOrder,
                                                                                  boolean          forLineage,
                                                                                  boolean          forDuplicateProcessing,
                                                                                  Date             effectiveTime,
                                                                                  int              startFrom,
                                                                                  int              pageSize) throws InvalidParameterException,
                                                                                                                    UserNotAuthorizedException,
                                                                                                                    PropertyServerException
    {
        return openMetadataClient.findRelationshipsBetweenMetadataElements(userId,
                                                                           relationshipTypeName,
                                                                           searchProperties,
                                                                           sequencingProperty,
                                                                           sequencingOrder,
                                                                           forLineage,
                                                                           forDuplicateProcessing,
                                                                           effectiveTime,
                                                                           startFrom,
                                                                           pageSize);
    }
}
