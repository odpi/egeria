/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ElementStatus;
import org.odpi.openmetadata.frameworks.governanceaction.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.governanceaction.properties.*;
import org.odpi.openmetadata.frameworks.governanceaction.search.ElementProperties;
import org.odpi.openmetadata.frameworks.governanceaction.search.SearchClassifications;
import org.odpi.openmetadata.frameworks.governanceaction.search.SearchProperties;
import org.odpi.openmetadata.frameworks.openmetadata.enums.SequencingOrder;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ArchiveProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.contextevents.ContextEventImpactProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.contextevents.ContextEventProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.contextevents.DependentContextEventProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.contextevents.RelatedContextEventProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * OpenMetadataStore provides access to metadata elements stored in the metadata repositories.  It is implemented by the
 * abstract class OpenMetadataClient and used by all the governance action services to retrieve metadata.
 * <p>
 * The concrete class for OpenMetadataClient is implemented by a metadata repository provider. In Egeria, this class is
 * implemented in the GAF Metadata Management Services.
 */
public class OpenMetadataStore
{
    protected final OpenMetadataClient openMetadataClient;
    private final   String             userId;
    private         String             externalSourceGUID;
    private         String             externalSourceName;
    private final   String             originatorGUID;

    private boolean forLineage              = false;
    private boolean forDuplicateProcessing  = false;
    private boolean useCurrentEffectiveTime = false;


    /**
     * The constructor needs an implementation of the open metadata store.
     *
     * @param openMetadataClient client implementation
     * @param userId calling user
     * @param externalSourceGUID unique identifier for external source (or null)
     * @param externalSourceName unique name for external source (or null)
     * @param originatorGUID unique identifier of the source of the to do
     */
    public OpenMetadataStore(OpenMetadataClient      openMetadataClient,
                             String                  userId,
                             String                  externalSourceGUID,
                             String                  externalSourceName,
                             String                  originatorGUID)
    {
        this.openMetadataClient = openMetadataClient;
        this.userId             = userId;
        this.externalSourceGUID = externalSourceGUID;
        this.externalSourceName = externalSourceName;
        this.originatorGUID     = originatorGUID;
    }


    /**
     * Construct the open metadata store wrapper around the open metadata client.
     *
     * @param openMetadataClient client to retrieve values from the
     * @param userId userId for the governance service
     * @param originatorGUID unique identifier of the source of the to do
     */
    public OpenMetadataStore(OpenMetadataClient openMetadataClient,
                             String             userId,
                             String             originatorGUID)
    {
        this.openMetadataClient = openMetadataClient;
        this.userId             = userId;
        this.externalSourceGUID = null;
        this.externalSourceName = null;
        this.originatorGUID     = originatorGUID;
    }


    /**
     * Get the maximum paging size.
     *
     * @return maxPagingSize new value
     */
    public int getMaxPagingSize() { return openMetadataClient.getMaxPagingSize(); }


    /**
     * Return the forLineage setting.
     *
     * @return boolean
     */
    public boolean isForLineage()
    {
        return forLineage;
    }


    /**
     * Set up the forLineage setting.
     *
     * @param forLineage boolean
     */
    public void setForLineage(boolean forLineage)
    {
        this.forLineage = forLineage;
    }


    /**
     * Return the forDuplicateProcessing setting.
     *
     * @return boolean
     */
    public boolean isForDuplicateProcessing()
    {
        return forDuplicateProcessing;
    }


    /**
     * Set up the forDuplicateProcessing setting.
     *
     * @param forDuplicateProcessing boolean
     */
    public void setForDuplicateProcessing(boolean forDuplicateProcessing)
    {
        this.forDuplicateProcessing = forDuplicateProcessing;
    }


    /**
     * Return the boolean setting for whether the current time for requests or null.
     *
     * @return boolean
     */
    public boolean isUseCurrentEffectiveTime()
    {
        return useCurrentEffectiveTime;
    }


    /**
     * Set up the boolean setting for whether the current time for requests or null.
     *
     * @param useCurrentEffectiveTime boolean
     */
    public void setUseCurrentEffectiveTime(boolean useCurrentEffectiveTime)
    {
        this.useCurrentEffectiveTime = useCurrentEffectiveTime;
    }


    /**
     * Return the appropriate effectiveTime for a request.
     *
     * @return date
     */
    private Date getEffectiveTime()
    {
        if (useCurrentEffectiveTime)
        {
            return new Date();
        }

        return null;
    }


    /**
     * Set up the externalSourceGUID and Name.
     *
     * @param externalSourceGUID unique identifier of the source metadata collection
     * @param externalSourceName unique name of the source metadata collection
     */
    public void setExternalSourceIds(String externalSourceGUID,
                                     String externalSourceName)
    {
        this.externalSourceGUID = externalSourceGUID;
        this.externalSourceName = externalSourceName;
    }


    /**
     * Returns the list of different types of metadata organized into two groups.  The first are the
     * attribute type definitions (AttributeTypeDefs).  These provide types for properties in full
     * type definitions.  Full type definitions (TypeDefs) describe types for entities, relationships
     * and classifications.
     *
     *
     * @return TypeDefGallery  List of different categories of type definitions.
     *
     * @throws InvalidParameterException  the userId is null
     * @throws PropertyServerException    there is a problem communicating with the metadata repository.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public OpenMetadataTypeDefGallery getAllTypes() throws InvalidParameterException,
                                                           PropertyServerException,
                                                           UserNotAuthorizedException
    {
        return openMetadataClient.getAllTypes(userId);
    }


    /**
     * Returns all the TypeDefs for a specific category.
     *
     * @param category enum value for the category of TypeDef to return.
     *
     * @return TypeDefs list.
     *
     * @throws InvalidParameterException  the TypeDefCategory is null.
     * @throws PropertyServerException    there is a problem communicating with the metadata repository.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public List<OpenMetadataTypeDef> findTypeDefsByCategory(OpenMetadataTypeDefCategory category) throws InvalidParameterException,
                                                                                                         PropertyServerException,
                                                                                                         UserNotAuthorizedException
    {
        return openMetadataClient.findTypeDefsByCategory(userId, category);
    }


    /**
     * Returns all the AttributeTypeDefs for a specific category.
     *
     * @param category enum value for the category of an AttributeTypeDef to return.
     *
     * @return AttributeTypeDefs list.
     *
     * @throws InvalidParameterException  the TypeDefCategory is null.
     * @throws PropertyServerException    there is a problem communicating with the metadata repository.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public List<OpenMetadataAttributeTypeDef> findAttributeTypeDefsByCategory(OpenMetadataAttributeTypeDefCategory category) throws InvalidParameterException,
                                                                                                                                    PropertyServerException,
                                                                                                                                    UserNotAuthorizedException
    {
        return openMetadataClient.findAttributeTypeDefsByCategory(userId, category);
    }


    /**
     * Return the types that are linked to the elements from the specified standard.
     *
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
    public List<OpenMetadataTypeDef> findTypesByExternalID(String standard,
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
     * @param guid   String unique id of the TypeDef
     *
     * @return TypeDef structure describing its category and properties.
     *
     * @throws InvalidParameterException  the guid is null.
     * @throws PropertyServerException    there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public OpenMetadataTypeDef getTypeDefByGUID(String guid) throws InvalidParameterException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException
    {
        return openMetadataClient.getTypeDefByGUID(userId, guid);
    }


    /**
     * Return the AttributeTypeDef identified by the GUID.
     *
     * @param guid   String unique id of the TypeDef
     *
     * @return TypeDef structure describing its category and properties.
     *
     * @throws InvalidParameterException  the guid is null.
     * @throws PropertyServerException    there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public OpenMetadataAttributeTypeDef getAttributeTypeDefByGUID(String guid) throws InvalidParameterException,
                                                                                      PropertyServerException,
                                                                                      UserNotAuthorizedException
    {
        return openMetadataClient.getAttributeTypeDefByGUID(userId, guid);
    }


    /**
     * Return the TypeDef identified by the unique name.
     *
     * @param name   String name of the TypeDef.
     *
     * @return TypeDef structure describing its category and properties.
     *
     * @throws InvalidParameterException  the name is null.
     * @throws PropertyServerException    there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public OpenMetadataTypeDef getTypeDefByName(String name) throws InvalidParameterException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException
    {
        return openMetadataClient.getTypeDefByName(userId, name);
    }


    /**
     * Return the AttributeTypeDef identified by the unique name.
     *
     * @param name   String name of the TypeDef.
     *
     * @return TypeDef structure describing its category and properties.
     *
     * @throws InvalidParameterException  the name is null.
     * @throws PropertyServerException    there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public OpenMetadataAttributeTypeDef getAttributeTypeDefByName(String name) throws InvalidParameterException,
                                                                                      PropertyServerException,
                                                                                      UserNotAuthorizedException
    {
        return openMetadataClient.getAttributeTypeDefByName(userId, name);
    }


    /**
     * Retrieve the metadata element using its unique identifier.
     *
     * @param elementGUID unique identifier for the metadata element
     *
     * @return metadata element properties
     * @throws InvalidParameterException the unique identifier is null or not known.
     * @throws UserNotAuthorizedException the governance action service is not able to access the element
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    public OpenMetadataElement getMetadataElementByGUID(String  elementGUID) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        return openMetadataClient.getMetadataElementByGUID(userId,
                                                           elementGUID,
                                                           forLineage,
                                                           forDuplicateProcessing,
                                                           null,
                                                           getEffectiveTime());
    }


    /**
     * Retrieve the metadata element using its unique name (typically the qualified name).
     *
     * @param uniqueName unique name for the metadata element
     * @param uniquePropertyName name of property name to test in the open metadata element - if null "qualifiedName" is used
     *
     * @return metadata element properties or null if not found
     * @throws InvalidParameterException the unique identifier is null.
     * @throws UserNotAuthorizedException the governance action service is not able to access the element
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    public OpenMetadataElement getMetadataElementByUniqueName(String  uniqueName,
                                                              String  uniquePropertyName) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        return openMetadataClient.getMetadataElementByUniqueName(userId,
                                                                 uniqueName,
                                                                 uniquePropertyName,
                                                                 forLineage,
                                                                 forDuplicateProcessing,
                                                                 null,
                                                                 getEffectiveTime());
    }


    /**
     * Retrieve the metadata element using its unique name (typically the qualified name).
     *
     * @param uniqueName unique name for the metadata element
     * @param uniquePropertyName name of property name to test in the open metadata element - if null "qualifiedName" is used
     *
     * @return metadata element properties or null if not found
     * @throws InvalidParameterException the unique identifier is null.
     * @throws UserNotAuthorizedException the governance action service is not able to access the element
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    public OpenMetadataElement getLineageElementByUniqueName(String  uniqueName,
                                                             String  uniquePropertyName) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException
    {
        return openMetadataClient.getMetadataElementByUniqueName(userId,
                                                                 uniqueName,
                                                                 uniquePropertyName,
                                                                 true,
                                                                 forDuplicateProcessing,
                                                                 null,
                                                                 getEffectiveTime());
    }


    /**
     * Retrieve the metadata element using its unique name (typically the qualified name) and the DELETED status.
     * This method assumes all effective dates, and forLineage and forDuplicateProcessing is set to false,
     * to cast the widest net.
     *
     * @param uniqueName unique name for the metadata element
     * @param uniquePropertyName name of property name to test in the open metadata element - if null "qualifiedName" is used
     *
     * @return metadata element properties or null if not found
     * @throws InvalidParameterException the unique identifier is null.
     * @throws UserNotAuthorizedException the governance action service is not able to access the element
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    public OpenMetadataElement getDeletedElementByUniqueName(String  uniqueName,
                                                             String  uniquePropertyName) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException
    {
        return openMetadataClient.getDeletedElementByUniqueName(userId, uniqueName, uniquePropertyName);
    }


    /**
     * Retrieve the unique identifier of a metadata element using its unique name (typically the qualified name).
     *
     * @param uniqueName unique name for the metadata element
     * @param uniquePropertyName name of property name to test in the open metadata element - if null "qualifiedName" is used
     *
     * @return metadata element unique identifier (guid)
     * @throws InvalidParameterException the unique identifier is null or not known.
     * @throws UserNotAuthorizedException the governance action service is not able to access the element
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    public String getMetadataElementGUIDByUniqueName(String  uniqueName,
                                                     String  uniquePropertyName) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        return openMetadataClient.getMetadataElementGUIDByUniqueName(userId,
                                                                     uniqueName,
                                                                     uniquePropertyName,
                                                                     forLineage,
                                                                     forDuplicateProcessing,
                                                                     null,
                                                                     getEffectiveTime());
    }


    /**
     * Retrieve the metadata elements that contain the requested string.
     *
     * @param searchString name to retrieve
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements (or null if no elements match the name)
     * @throws InvalidParameterException the qualified name is null
     * @throws UserNotAuthorizedException the governance action service is not able to access the element
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    public List<OpenMetadataElement> findMetadataElementsWithString(String  searchString,
                                                                    int     startFrom,
                                                                    int     pageSize) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        return openMetadataClient.findMetadataElementsWithString(userId,
                                                                 searchString,
                                                                 null,
                                                                 null,
                                                                 null,
                                                                 null,
                                                                 forLineage,
                                                                 forDuplicateProcessing,
                                                                 getEffectiveTime(),
                                                                 startFrom,
                                                                 pageSize);
    }


    /**
     * Retrieve the metadata elements of the requested type that contain the requested string.
     *
     * @param searchString name to retrieve
     * @param typeName    name of the type to limit the results to (maybe null to mean all types)
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
                                                                    int     startFrom,
                                                                    int     pageSize) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        return openMetadataClient.findMetadataElementsWithString(userId,
                                                                 searchString,
                                                                 typeName,
                                                                 null,
                                                                 null,
                                                                 null,
                                                                 null,
                                                                 forLineage,
                                                                 forDuplicateProcessing,
                                                                 getEffectiveTime(),
                                                                 startFrom,
                                                                 pageSize);
    }


    /**
     * Return a list of elements with the requested search string in their (display, resource)name, qualified name,
     * title, text, summary, identifier or description.  The search string is interpreted as a regular expression (RegEx).
     * The breadth of the search is determined by the supplied anchorGUID.
     *
     * @param searchString           name to retrieve
     * @param anchorGUID unique identifier of anchor
     * @param typeName optional name of the type to limit the results to
     * @param limitResultsByStatus By default, relationships in all statuses (other than DELETE) are returned.  However, it is possible
     *                             to specify a list of statuses (for example ACTIVE) to restrict the results to.  Null means all status values.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param effectiveTime only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @param startFrom              paging start point
     * @param pageSize               maximum results that can be returned
     *
     * @return list of matching metadata elements (or null if no elements match the name)
     *
     * @throws InvalidParameterException  the qualified name is null
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    public AnchorSearchMatches findElementsForAnchor(String              searchString,
                                                     String              anchorGUID,
                                                     String              typeName,
                                                     List<ElementStatus> limitResultsByStatus,
                                                     Date                asOfTime,
                                                     String              sequencingProperty,
                                                     SequencingOrder     sequencingOrder,
                                                     Date                effectiveTime,
                                                     int                 startFrom,
                                                     int                 pageSize) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        return openMetadataClient.findElementsForAnchor(userId, searchString, anchorGUID, typeName, limitResultsByStatus, asOfTime, sequencingProperty, sequencingOrder, forLineage, forDuplicateProcessing, effectiveTime, startFrom, pageSize);
    }


    /**
     * Return a list of elements with the requested search string in their (display, resource)name, qualified name,
     * title, text, summary, identifier or description.  The search string is interpreted as a regular expression (RegEx).
     * The breadth of the search is determined by the supplied domain name. The results are organized by anchor element.
     *
     * @param searchString           name to retrieve
     * @param anchorDomainName name of open metadata type for the domain
     * @param typeName               name of the type to limit the results to (maybe null to mean all types)
     * @param limitResultsByStatus By default, relationships in all statuses (other than DELETE) are returned.  However, it is possible
     *                             to specify a list of statuses (for example ACTIVE) to restrict the results to.  Null means all status values.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param effectiveTime only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @param startFrom              paging start point
     * @param pageSize               maximum results that can be returned
     *
     * @return list of matching metadata elements (or null if no elements match the name)
     *
     * @throws InvalidParameterException  the qualified name is null
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    public List<AnchorSearchMatches> findElementsInAnchorDomain(String              searchString,
                                                                String              anchorDomainName,
                                                                String              typeName,
                                                                List<ElementStatus> limitResultsByStatus,
                                                                Date                asOfTime,
                                                                String              sequencingProperty,
                                                                SequencingOrder     sequencingOrder,
                                                                Date                effectiveTime,
                                                                int                 startFrom,
                                                                int                 pageSize) throws InvalidParameterException,
                                                                                                     UserNotAuthorizedException,
                                                                                                     PropertyServerException
    {
        return openMetadataClient.findElementsInAnchorDomain(userId, searchString, anchorDomainName, typeName, limitResultsByStatus, asOfTime, sequencingProperty, sequencingOrder, forLineage, forDuplicateProcessing, effectiveTime, startFrom, pageSize);
    }


    /**
     * Return a list of elements with the requested search string in their (display, resource)name, qualified name,
     * title, text, summary, identifier or description.  The search string is interpreted as a regular expression (RegEx).
     * The breadth of the search is determined by the supplied scope guid. The results are organized by anchor element.
     *
     * @param searchString           name to retrieve
     * @param anchorScopeGUID unique identifier of the scope to use
     * @param typeName               name of the type to limit the results to (maybe null to mean all types)
     * @param limitResultsByStatus By default, relationships in all statuses (other than DELETE) are returned.  However, it is possible
     *                             to specify a list of statuses (for example ACTIVE) to restrict the results to.  Null means all status values.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param effectiveTime only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @param startFrom              paging start point
     * @param pageSize               maximum results that can be returned
     *
     * @return list of matching metadata elements (or null if no elements match the name)
     *
     * @throws InvalidParameterException  the qualified name is null
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    public List<AnchorSearchMatches> findElementsInAnchorScope(String              searchString,
                                                               String              anchorScopeGUID,
                                                               String              typeName,
                                                               List<ElementStatus> limitResultsByStatus,
                                                               Date                asOfTime,
                                                               String              sequencingProperty,
                                                               SequencingOrder     sequencingOrder,
                                                               Date                effectiveTime,
                                                               int                 startFrom,
                                                               int                 pageSize) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        return openMetadataClient.findElementsInAnchorScope(userId, searchString, anchorScopeGUID, typeName, limitResultsByStatus, asOfTime, sequencingProperty, sequencingOrder, forLineage, forDuplicateProcessing, effectiveTime, startFrom, pageSize);
    }


    /**
     * Retrieve the metadata elements connected to the supplied element.
     *
     * @param elementGUID unique identifier for the starting metadata element
     * @param startingAtEnd indicates which end to retrieve from (0 is "either end"; 1 is end1; 2 is end 2)
     * @param relationshipTypeName type name of relationships to follow (or null for all)
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of related elements
     * @throws InvalidParameterException the unique identifier is null or not known; the relationship type is invalid
     * @throws UserNotAuthorizedException the governance action service is not able to access the elements
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    public RelatedMetadataElementList getRelatedMetadataElements(String  elementGUID,
                                                                 int     startingAtEnd,
                                                                 String  relationshipTypeName,
                                                                 int     startFrom,
                                                                 int     pageSize) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        return openMetadataClient.getRelatedMetadataElements(userId,
                                                             elementGUID,
                                                             startingAtEnd,
                                                             relationshipTypeName,
                                                             null,
                                                             null,
                                                             null,
                                                             SequencingOrder.CREATION_DATE_RECENT,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             getEffectiveTime(),
                                                             startFrom,
                                                             pageSize);
    }


    /**
     * Return all the elements that are anchored to an asset plus relationships between these elements and to other elements.
     *
     * @param elementGUID  unique identifier for the element
     * @param startFrom starting element (used in paging through large result sets)
     * @param pageSize maximum number of results to return
     *
     * @return graph of elements
     *
     * @throws InvalidParameterException  the unique identifier is null or not known; the relationship type is invalid
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    public OpenMetadataElementGraph getAnchoredElementsGraph(String elementGUID,
                                                             int    startFrom,
                                                             int    pageSize) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        return openMetadataClient.getAnchoredElementsGraph(userId, elementGUID, forLineage, forDuplicateProcessing, startFrom, pageSize, null, getEffectiveTime());
    }


    /**
     * Retrieve the metadata element connected to the supplied element for a relationship type that only allows one
     * relationship to be attached.
     *
     * @param elementGUID            unique identifier for the starting metadata element
     * @param startingAtEnd          indicates which end to retrieve from (0 is "either end"; 1 is end1; 2 is end 2)
     * @param relationshipTypeName   type name of relationships to follow (or null for all)
     * @param effectiveTime          only return an element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     *
     * @return list of related elements
     *
     * @throws InvalidParameterException  the unique identifier is null or not known; the relationship type is invalid
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException    there is a problem accessing the metadata store or multiple relationships have been returned
     */
    public RelatedMetadataElement getRelatedMetadataElement(String  elementGUID,
                                                            int     startingAtEnd,
                                                            String  relationshipTypeName,
                                                            Date    effectiveTime) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        return openMetadataClient.getRelatedMetadataElement(userId,
                                                            elementGUID,
                                                            startingAtEnd,
                                                            relationshipTypeName,
                                                            forLineage,
                                                            forDuplicateProcessing,
                                                            effectiveTime);
    }


    /**
     * Return each of the versions of a metadata element.
     *
     * @param elementGUID            unique identifier for the metadata element
     * @param fromTime the earliest point in time from which to retrieve historical versions of the entity (inclusive)
     * @param toTime the latest point in time from which to retrieve historical versions of the entity (exclusive)
     * @param oldestFirst  defining how the results should be ordered.
     * @param effectiveTime only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return a list of elements matching the supplied criteria; null means no matching elements in the metadata store.
     * @throws InvalidParameterException one of the search parameters are is invalid
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    public  List<OpenMetadataElement> getMetadataElementHistory(String  elementGUID,
                                                                Date    fromTime,
                                                                Date    toTime,
                                                                boolean oldestFirst,
                                                                Date    effectiveTime,
                                                                int     startFrom,
                                                                int     pageSize) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        return openMetadataClient.getMetadataElementHistory(userId, elementGUID, fromTime, toTime, oldestFirst, forLineage, forDuplicateProcessing, effectiveTime, startFrom, pageSize);
    }


    /**
     * Retrieve the relationships linking to the supplied elements.
     *
     * @param metadataElementAtEnd1GUID unique identifier of the metadata element at end 1 of the relationship
     * @param metadataElementAtEnd2GUID unique identifier of the metadata element at end 2 of the relationship
     * @param relationshipTypeName type name of relationships to follow (or null for all)
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of related elements
     * @throws InvalidParameterException the unique identifier is null or not known; the relationship type is invalid
     * @throws UserNotAuthorizedException the governance action service is not able to access the elements
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    public OpenMetadataRelationshipList getMetadataElementRelationships(String  metadataElementAtEnd1GUID,
                                                                          String  metadataElementAtEnd2GUID,
                                                                          String  relationshipTypeName,
                                                                          int     startFrom,
                                                                          int     pageSize) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        return openMetadataClient.getMetadataElementRelationships(userId,
                                                                  metadataElementAtEnd1GUID,
                                                                  metadataElementAtEnd2GUID,
                                                                  relationshipTypeName,
                                                                  null,
                                                                  null,
                                                                  null,
                                                                  null,
                                                                  forLineage,
                                                                  forDuplicateProcessing,
                                                                  getEffectiveTime(),
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
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param matchClassifications Optional list of classifications to match.
     * @param sequencingProperty String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
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
                                                          Date                  asOfTime,
                                                          SearchClassifications matchClassifications,
                                                          String                sequencingProperty,
                                                          SequencingOrder       sequencingOrder,
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
                                                       asOfTime,
                                                       matchClassifications,
                                                       sequencingProperty,
                                                       sequencingOrder,
                                                       forLineage,
                                                       forDuplicateProcessing,
                                                       getEffectiveTime(),
                                                       startFrom,
                                                       pageSize);
    }


    /**
     * Return a list of relationships that match the requested conditions.  The results can be received as a series of pages.
     *
     * @param relationshipTypeName relationship's type.  Null means all types
     *                             (but may be slow so not recommended).
     * @param searchProperties Optional list of relationship property conditions to match.
     * @param limitResultsByStatus By default, relationships in all statuses (other than DELETE) are returned.  However, it is possible
     *                             to specify a list of statuses (for example ACTIVE) to restrict the results to.  Null means all status values.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return a list of relationships.  Null means no matching relationships.
     * @throws InvalidParameterException one of the search parameters are is invalid
     * @throws UserNotAuthorizedException the governance action service is not able to access the elements
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    public OpenMetadataRelationshipList findRelationshipsBetweenMetadataElements(String              relationshipTypeName,
                                                                                   SearchProperties    searchProperties,
                                                                                   List<ElementStatus> limitResultsByStatus,
                                                                                   Date                asOfTime,
                                                                                   String              sequencingProperty,
                                                                                   SequencingOrder     sequencingOrder,
                                                                                   int                 startFrom,
                                                                                   int                 pageSize) throws InvalidParameterException,
                                                                                                                        UserNotAuthorizedException,
                                                                                                                        PropertyServerException
    {
        return openMetadataClient.findRelationshipsBetweenMetadataElements(userId,
                                                                           relationshipTypeName,
                                                                           searchProperties,
                                                                           limitResultsByStatus,
                                                                           asOfTime,
                                                                           sequencingProperty,
                                                                           sequencingOrder,
                                                                           forLineage,
                                                                           forDuplicateProcessing,
                                                                           getEffectiveTime(),
                                                                           startFrom,
                                                                           pageSize);
    }


    /**
     * Retrieve the relationship using its unique identifier.
     *
     * @param relationshipGUID unique identifier for the relationship
     *
     * @return relationship properties
     * @throws InvalidParameterException the unique identifier is null or not known.
     * @throws UserNotAuthorizedException the governance action service is not able to access the element
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    public OpenMetadataRelationship getRelationshipByGUID(String  relationshipGUID) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        return openMetadataClient.getRelationshipByGUID(userId, relationshipGUID, forLineage, forDuplicateProcessing, null, getEffectiveTime());
    }


    /**
     * Create a new metadata element in the metadata store.  The type name comes from the open metadata types.
     * The selected type also controls the names and types of the properties that are allowed.
     * This version of the method allows access to advanced features such as multiple states and
     * effectivity dates.
     *
     * @param metadataElementTypeName type name of the new metadata element
     * @param initialStatus initial status of the metadata element
     * @param properties properties of the new metadata element
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException the type name, status or one of the properties is invalid
     * @throws UserNotAuthorizedException the governance action service is not authorized to create this type of element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public String createMetadataElementInStore(String            metadataElementTypeName,
                                               ElementStatus     initialStatus,
                                               ElementProperties properties) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        return openMetadataClient.createMetadataElementInStore(userId,
                                                               externalSourceGUID,
                                                               externalSourceName,
                                                               metadataElementTypeName,
                                                               initialStatus,
                                                               null,
                                                               null,
                                                               properties);
    }


    /**
     * Create a new metadata element in the metadata store.  The type name comes from the open metadata types.
     * The selected type also controls the names and types of the properties that are allowed.
     * This version of the method allows access to advanced features such as multiple states and
     * effectivity dates.
     *
     * @param metadataElementTypeName type name of the new metadata element
     * @param initialStatus initial status of the metadata element
     * @param effectiveFrom the date when this element is active - null for active on creation
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param properties properties of the new metadata element

     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException the type name, status or one of the properties is invalid
     * @throws UserNotAuthorizedException the governance action service is not authorized to create this type of element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public String createMetadataElementInStore(String            metadataElementTypeName,
                                               ElementStatus     initialStatus,
                                               Date              effectiveFrom,
                                               Date              effectiveTo,
                                               ElementProperties properties) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        return openMetadataClient.createMetadataElementInStore(userId,
                                                               externalSourceGUID,
                                                               externalSourceName,
                                                               metadataElementTypeName,
                                                               initialStatus,
                                                               effectiveFrom,
                                                               effectiveTo,
                                                               properties);
    }


    /**
     * Create a new metadata element in the metadata store.  The type name comes from the open metadata types.
     * The selected type also controls the names and types of the properties that are allowed.
     * This version of the method allows access to advanced features such as multiple states and
     * effectivity dates.
     *
     * @param metadataElementTypeName type name of the new metadata element
     * @param initialStatus initial status of the metadata element
     * @param initialClassifications map of classification names to classification properties to include in the entity creation request
     * @param anchorGUID unique identifier of the element that should be the anchor for the new element. Set to null if no anchor,
     *                   or the Anchors classification is included in the initial classifications.
     * @param isOwnAnchor boolean flag to day that the element should be classified as its own anchor once its element
     *                    is created in the repository.
     * @param anchorScopeGUID unique identifier of the element that represents a broader scope that the anchor belongs to.
     *                        If anchorScopeGUID is null, the value is taken from the anchor element.
     * @param effectiveFrom the date when this element is active - null for active on creation
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param properties properties of the new metadata element
     * @param parentGUID unique identifier of optional parent entity
     * @param parentRelationshipTypeName type of relationship to connect the new element to the parent
     * @param parentRelationshipProperties properties to include in parent relationship
     * @param parentAtEnd1 which end should the parent GUID go in the relationship
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException the type name, status or one of the properties is invalid
     * @throws UserNotAuthorizedException the governance action service is not authorized to create this type of element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public String createMetadataElementInStore(String                         metadataElementTypeName,
                                               ElementStatus                  initialStatus,
                                               Map<String, ElementProperties> initialClassifications,
                                               String                         anchorGUID,
                                               boolean                        isOwnAnchor,
                                               String                         anchorScopeGUID,
                                               Date                           effectiveFrom,
                                               Date                           effectiveTo,
                                               ElementProperties              properties,
                                               String                         parentGUID,
                                               String                         parentRelationshipTypeName,
                                               ElementProperties              parentRelationshipProperties,
                                               boolean                        parentAtEnd1) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        return openMetadataClient.createMetadataElementInStore(userId,
                                                               externalSourceGUID,
                                                               externalSourceName,
                                                               metadataElementTypeName,
                                                               initialStatus,
                                                               initialClassifications,
                                                               anchorGUID,
                                                               isOwnAnchor,
                                                               anchorScopeGUID,
                                                               effectiveFrom,
                                                               effectiveTo,
                                                               properties,
                                                               parentGUID,
                                                               parentRelationshipTypeName,
                                                               parentRelationshipProperties,
                                                               parentAtEnd1,
                                                               forLineage,
                                                               forDuplicateProcessing,
                                                               getEffectiveTime());
    }


    /**
     * Create a new metadata element in the metadata store.  The type name comes from the open metadata types.
     * The selected type also controls the names and types of the properties that are allowed.
     * This version of the method allows access to advanced features such as multiple states and
     * effectivity dates.
     *
     * @param externalSourceGUID      unique identifier of the software capability that owns this collection
     * @param externalSourceName      unique name of the software capability that owns this collection
     * @param metadataElementTypeName type name of the new metadata element
     * @param initialStatus initial status of the metadata element
     * @param initialClassifications map of classification names to classification properties to include in the entity creation request
     * @param anchorGUID unique identifier of the element that should be the anchor for the new element. Set to null if no anchor,
     *                   or the Anchors classification is included in the initial classifications.
     * @param isOwnAnchor boolean flag to day that the element should be classified as its own anchor once its element
     *                    is created in the repository.
     * @param anchorScopeGUID unique identifier of the element that represents a broader scope that the anchor belongs to.
     *                        If anchorScopeGUID is null, the value is taken from the anchor element.
     * @param effectiveFrom the date when this element is active - null for active on creation
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param properties properties of the new metadata element
     * @param parentGUID unique identifier of optional parent entity
     * @param parentRelationshipTypeName type of relationship to connect the new element to the parent
     * @param parentRelationshipProperties properties to include in parent relationship
     * @param parentAtEnd1 which end should the parent GUID go in the relationship
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException the type name, status or one of the properties is invalid
     * @throws UserNotAuthorizedException the governance action service is not authorized to create this type of element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public String createMetadataElementInStore(String                         externalSourceGUID,
                                               String                         externalSourceName,
                                               String                         metadataElementTypeName,
                                               ElementStatus                  initialStatus,
                                               Map<String, ElementProperties> initialClassifications,
                                               String                         anchorGUID,
                                               boolean                        isOwnAnchor,
                                               String                         anchorScopeGUID,
                                               Date                           effectiveFrom,
                                               Date                           effectiveTo,
                                               ElementProperties              properties,
                                               String                         parentGUID,
                                               String                         parentRelationshipTypeName,
                                               ElementProperties              parentRelationshipProperties,
                                               boolean                        parentAtEnd1) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        return openMetadataClient.createMetadataElementInStore(userId,
                                                               externalSourceGUID,
                                                               externalSourceName,
                                                               metadataElementTypeName,
                                                               initialStatus,
                                                               initialClassifications,
                                                               anchorGUID,
                                                               isOwnAnchor,
                                                               anchorScopeGUID,
                                                               effectiveFrom,
                                                               effectiveTo,
                                                               properties,
                                                               parentGUID,
                                                               parentRelationshipTypeName,
                                                               parentRelationshipProperties,
                                                               parentAtEnd1,
                                                               forLineage,
                                                               forDuplicateProcessing,
                                                               getEffectiveTime());
    }


    /**
     * Create a new metadata element in the metadata store using the template identified by the templateGUID.
     * The type name comes from the open metadata types.
     * The selected type also controls the names and types of the properties that are allowed.
     * The template and any similar anchored objects are
     * copied in this process.
     *
     * @param metadataElementTypeName type name of the new metadata element
     * @param anchorGUID unique identifier of the element that should be the anchor for the new element. Set to null if no anchor,
     *                   or the Anchors classification is included in the initial classifications.
     * @param isOwnAnchor boolean flag to day that the element should be classified as its own anchor once its element
     *                    is created in the repository.
     * @param anchorScopeGUID unique identifier of the element that represents a broader scope that the anchor belongs to.
     *                        If anchorScopeGUID is null, the value is taken from the anchor element.
     * @param effectiveFrom the date when this element is active - null for active on creation
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param templateGUID the unique identifier of the existing asset to copy (this will copy all the attachments such as nested content, schema
     *                     connection etc)
     * @param replacementProperties properties of the new metadata element.  These override the placeholder values
     * @param placeholderProperties property name-to-property value map to replace any placeholder values in the
     *                              template element - and their anchored elements, which are also copied as part of this operation.
     * @param parentGUID unique identifier of optional parent entity
     * @param parentRelationshipTypeName type of relationship to connect the new element to the parent
     * @param parentRelationshipProperties properties to include in parent relationship
     * @param parentAtEnd1 which end should the parent GUID go in the relationship
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException the type name, status or one of the properties is invalid
     * @throws UserNotAuthorizedException the governance action service is not authorized to create this type of element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public String createMetadataElementFromTemplate(String                         metadataElementTypeName,
                                                    String                         anchorGUID,
                                                    boolean                        isOwnAnchor,
                                                    String                         anchorScopeGUID,
                                                    Date                           effectiveFrom,
                                                    Date                           effectiveTo,
                                                    String                         templateGUID,
                                                    ElementProperties              replacementProperties,
                                                    Map<String, String>            placeholderProperties,
                                                    String                         parentGUID,
                                                    String                         parentRelationshipTypeName,
                                                    ElementProperties              parentRelationshipProperties,
                                                    boolean                        parentAtEnd1) throws InvalidParameterException,
                                                                                                        UserNotAuthorizedException,
                                                                                                        PropertyServerException
    {
        return openMetadataClient.createMetadataElementFromTemplate(userId,
                                                                    externalSourceGUID,
                                                                    externalSourceName,
                                                                    metadataElementTypeName,
                                                                    anchorGUID,
                                                                    isOwnAnchor,
                                                                    anchorScopeGUID,
                                                                    effectiveFrom,
                                                                    effectiveTo,
                                                                    templateGUID,
                                                                    replacementProperties,
                                                                    placeholderProperties,
                                                                    parentGUID,
                                                                    parentRelationshipTypeName,
                                                                    parentRelationshipProperties,
                                                                    parentAtEnd1,
                                                                    forLineage,
                                                                    forDuplicateProcessing,
                                                                    getEffectiveTime());
    }


    /**
     * Create a new metadata element in the metadata store using the template identified by the templateGUID.
     * The type name comes from the open metadata types.
     * The selected type also controls the names and types of the properties that are allowed.
     * The template and any similar anchored objects are
     * copied in this process.
     *
     * @param externalSourceGUID      unique identifier of the software capability that owns this collection
     * @param externalSourceName      unique name of the software capability that owns this collection
     * @param metadataElementTypeName type name of the new metadata element
     * @param anchorGUID unique identifier of the element that should be the anchor for the new element. Set to null if no anchor,
     *                   or the Anchors classification is included in the initial classifications.
     * @param isOwnAnchor boolean flag to day that the element should be classified as its own anchor once its element
     *                    is created in the repository.
     * @param anchorScopeGUID unique identifier of the element that represents a broader scope that the anchor belongs to.
     *                        If anchorScopeGUID is null, the value is taken from the anchor element.
     * @param effectiveFrom the date when this element is active - null for active on creation
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param templateGUID the unique identifier of the existing asset to copy (this will copy all the attachments such as nested content, schema
     *                     connection etc)
     * @param replacementProperties properties of the new metadata element.  These override the placeholder values
     * @param placeholderProperties property name-to-property value map to replace any placeholder values in the
     *                              template element - and their anchored elements, which are also copied as part of this operation.
     * @param parentGUID unique identifier of optional parent entity
     * @param parentRelationshipTypeName type of relationship to connect the new element to the parent
     * @param parentRelationshipProperties properties to include in parent relationship
     * @param parentAtEnd1 which end should the parent GUID go in the relationship
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException the type name, status or one of the properties is invalid
     * @throws UserNotAuthorizedException the governance action service is not authorized to create this type of element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public String createMetadataElementFromTemplate(String                         externalSourceGUID,
                                                    String                         externalSourceName,
                                                    String                         metadataElementTypeName,
                                                    String                         anchorGUID,
                                                    boolean                        isOwnAnchor,
                                                    String                         anchorScopeGUID,
                                                    Date                           effectiveFrom,
                                                    Date                           effectiveTo,
                                                    String                         templateGUID,
                                                    ElementProperties              replacementProperties,
                                                    Map<String, String>            placeholderProperties,
                                                    String                         parentGUID,
                                                    String                         parentRelationshipTypeName,
                                                    ElementProperties              parentRelationshipProperties,
                                                    boolean                        parentAtEnd1) throws InvalidParameterException,
                                                                                                        UserNotAuthorizedException,
                                                                                                        PropertyServerException
    {
        return openMetadataClient.createMetadataElementFromTemplate(userId,
                                                                    externalSourceGUID,
                                                                    externalSourceName,
                                                                    metadataElementTypeName,
                                                                    anchorGUID,
                                                                    isOwnAnchor,
                                                                    anchorScopeGUID,
                                                                    effectiveFrom,
                                                                    effectiveTo,
                                                                    templateGUID,
                                                                    replacementProperties,
                                                                    placeholderProperties,
                                                                    parentGUID,
                                                                    parentRelationshipTypeName,
                                                                    parentRelationshipProperties,
                                                                    parentAtEnd1,
                                                                    forLineage,
                                                                    forDuplicateProcessing,
                                                                    getEffectiveTime());
    }


    /**
     * Create/get a new metadata element in the metadata store using the template identified by the templateGUID.
     * The type name comes from the open metadata types.
     * The selected type also controls the names and types of the properties that are allowed.
     * The template and any similar anchored objects are
     * copied in this process.
     *
     * @param metadataElementTypeName type name of the new metadata element
     * @param anchorGUID unique identifier of the element that should be the anchor for the new element. Set to null if no anchor,
     *                   or the Anchors classification is included in the initial classifications.
     * @param isOwnAnchor boolean flag to day that the element should be classified as its own anchor once its element
     *                    is created in the repository.
     * @param anchorScopeGUID unique identifier of the element that represents a broader scope that the anchor belongs to.
     *                        If anchorScopeGUID is null, the value is taken from the anchor element.
     * @param effectiveFrom the date when this element is active - null for active on creation
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param templateGUID the unique identifier of the existing asset to copy (this will copy all the attachments such as nested content, schema
     *                     connection etc)
     * @param replacementProperties properties of the new metadata element.  These override the placeholder values
     * @param placeholderProperties property name-to-property value map to replace any placeholder values in the
     *                              template element - and their anchored elements, which are also copied as part of this operation.
     * @param parentGUID unique identifier of optional parent entity
     * @param parentRelationshipTypeName type of relationship to connect the new element to the parent
     * @param parentRelationshipProperties properties to include in parent relationship
     * @param parentAtEnd1 which end should the parent GUID go in the relationship
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException the type name, status or one of the properties is invalid
     * @throws UserNotAuthorizedException the governance action service is not authorized to create this type of element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public String getMetadataElementFromTemplate(String                         metadataElementTypeName,
                                                 String                         anchorGUID,
                                                 boolean                        isOwnAnchor,
                                                 String                         anchorScopeGUID,
                                                 Date                           effectiveFrom,
                                                 Date                           effectiveTo,
                                                 String                         templateGUID,
                                                 ElementProperties              replacementProperties,
                                                 Map<String, String>            placeholderProperties,
                                                 String                         parentGUID,
                                                 String                         parentRelationshipTypeName,
                                                 ElementProperties              parentRelationshipProperties,
                                                 boolean                        parentAtEnd1) throws InvalidParameterException,
                                                                                                     UserNotAuthorizedException,
                                                                                                     PropertyServerException
    {
        return openMetadataClient.getMetadataElementFromTemplate(userId,
                                                                 externalSourceGUID,
                                                                 externalSourceName,
                                                                 metadataElementTypeName,
                                                                 anchorGUID,
                                                                 isOwnAnchor,
                                                                 anchorScopeGUID,
                                                                 effectiveFrom,
                                                                 effectiveTo,
                                                                 templateGUID,
                                                                 replacementProperties,
                                                                 placeholderProperties,
                                                                 parentGUID,
                                                                 parentRelationshipTypeName,
                                                                 parentRelationshipProperties,
                                                                 parentAtEnd1,
                                                                 forLineage,
                                                                 forDuplicateProcessing,
                                                                 getEffectiveTime());
    }


    /**
     * Create/get a new metadata element in the metadata store using the template identified by the templateGUID.
     * The type name comes from the open metadata types.
     * The selected type also controls the names and types of the properties that are allowed.
     * The template and any similar anchored objects are
     * copied in this process.
     *
     * @param externalSourceGUID      unique identifier of the software capability that owns this collection
     * @param externalSourceName      unique name of the software capability that owns this collection
     * @param metadataElementTypeName type name of the new metadata element
     * @param anchorGUID unique identifier of the element that should be the anchor for the new element. Set to null if no anchor,
     *                   or the Anchors classification is included in the initial classifications.
     * @param isOwnAnchor boolean flag to day that the element should be classified as its own anchor once its element
     *                    is created in the repository.
     * @param anchorScopeGUID unique identifier of the element that represents a broader scope that the anchor belongs to.
     *                        If anchorScopeGUID is null, the value is taken from the anchor element.
     * @param effectiveFrom the date when this element is active - null for active on creation
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param templateGUID the unique identifier of the existing asset to copy (this will copy all the attachments such as nested content, schema
     *                     connection etc)
     * @param replacementProperties properties of the new metadata element.  These override the placeholder values
     * @param placeholderProperties property name-to-property value map to replace any placeholder values in the
     *                              template element - and their anchored elements, which are also copied as part of this operation.
     * @param parentGUID unique identifier of optional parent entity
     * @param parentRelationshipTypeName type of relationship to connect the new element to the parent
     * @param parentRelationshipProperties properties to include in parent relationship
     * @param parentAtEnd1 which end should the parent GUID go in the relationship
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException the type name, status or one of the properties is invalid
     * @throws UserNotAuthorizedException the governance action service is not authorized to create this type of element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public String getMetadataElementFromTemplate(String                         externalSourceGUID,
                                                 String                         externalSourceName,
                                                 String                         metadataElementTypeName,
                                                 String                         anchorGUID,
                                                 boolean                        isOwnAnchor,
                                                 String                         anchorScopeGUID,
                                                 Date                           effectiveFrom,
                                                 Date                           effectiveTo,
                                                 String                         templateGUID,
                                                 ElementProperties              replacementProperties,
                                                 Map<String, String>            placeholderProperties,
                                                 String                         parentGUID,
                                                 String                         parentRelationshipTypeName,
                                                 ElementProperties              parentRelationshipProperties,
                                                 boolean                        parentAtEnd1) throws InvalidParameterException,
                                                                                                      UserNotAuthorizedException,
                                                                                                      PropertyServerException
    {
        return openMetadataClient.getMetadataElementFromTemplate(userId,
                                                                 externalSourceGUID,
                                                                 externalSourceName,
                                                                 metadataElementTypeName,
                                                                 anchorGUID,
                                                                 isOwnAnchor,
                                                                 anchorScopeGUID,
                                                                 effectiveFrom,
                                                                 effectiveTo,
                                                                 templateGUID,
                                                                 replacementProperties,
                                                                 placeholderProperties,
                                                                 parentGUID,
                                                                 parentRelationshipTypeName,
                                                                 parentRelationshipProperties,
                                                                 parentAtEnd1,
                                                                 forLineage,
                                                                 forDuplicateProcessing,
                                                                 getEffectiveTime());
    }


    /**
     * Update the properties of a specific metadata element.  The properties must match the type definition associated with the
     * metadata element when it was created.  However, it is possible to update a few properties, or replace all them by
     * the value used in the replaceProperties flag.
     *
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param replaceProperties flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                          the individual properties specified on the request.
     * @param properties new properties for the metadata element
     *
     * @throws InvalidParameterException either the unique identifier or the properties are invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void updateMetadataElementInStore(String            metadataElementGUID,
                                             boolean           replaceProperties,
                                             ElementProperties properties) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        openMetadataClient.updateMetadataElementInStore(userId,
                                                        externalSourceGUID,
                                                        externalSourceName,
                                                        metadataElementGUID,
                                                        replaceProperties,
                                                        forLineage,
                                                        forDuplicateProcessing,
                                                        properties,
                                                        getEffectiveTime());
    }


    /**
     * Update the properties of a specific metadata element.  The properties must match the type definition associated with the
     * metadata element when it was created.  However, it is possible to update a few properties, or replace all them by
     * the value used in the replaceProperties flag.
     *
     * @param externalSourceGUID      unique identifier of the software capability that owns this collection
     * @param externalSourceName      unique name of the software capability that owns this collection
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param replaceProperties flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                          the individual properties specified on the request.
     * @param properties new properties for the metadata element
     *
     * @throws InvalidParameterException either the unique identifier or the properties are invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void updateMetadataElementInStore(String            externalSourceGUID,
                                             String            externalSourceName,
                                             String            metadataElementGUID,
                                             boolean           replaceProperties,
                                             ElementProperties properties) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        openMetadataClient.updateMetadataElementInStore(userId,
                                                        externalSourceGUID,
                                                        externalSourceName,
                                                        metadataElementGUID,
                                                        replaceProperties,
                                                        forLineage,
                                                        forDuplicateProcessing,
                                                        properties,
                                                        getEffectiveTime());
    }


    /**
     * Update the status of specific metadata element. The new status must match a status value that is defined for the element's type
     * assigned when it was created.
     *
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param newElementStatus new status value - or null to leave as is
     *
     * @throws InvalidParameterException either the unique identifier or the status are invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void updateMetadataElementStatusInStore(String        metadataElementGUID,
                                                   ElementStatus newElementStatus) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        openMetadataClient.updateMetadataElementStatusInStore(userId,
                                                              externalSourceGUID,
                                                              externalSourceName,
                                                              metadataElementGUID,
                                                              forLineage,
                                                              forDuplicateProcessing,
                                                              newElementStatus,
                                                              getEffectiveTime());
    }


    /**
     * Update the status of specific metadata element. The new status must match a status value that is defined for the element's type
     * assigned when it was created.
     *
     * @param externalSourceGUID      unique identifier of the software capability that owns this collection
     * @param externalSourceName      unique name of the software capability that owns this collection
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param newElementStatus new status value - or null to leave as is
     *
     * @throws InvalidParameterException either the unique identifier or the status are invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void updateMetadataElementStatusInStore(String        externalSourceGUID,
                                                   String        externalSourceName,
                                                   String        metadataElementGUID,
                                                   ElementStatus newElementStatus) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        openMetadataClient.updateMetadataElementStatusInStore(userId,
                                                              externalSourceGUID,
                                                              externalSourceName,
                                                              metadataElementGUID,
                                                              forLineage,
                                                              forDuplicateProcessing,
                                                              newElementStatus,
                                                              getEffectiveTime());
    }


    /**
     * Update the effectivity dates control the visibility of the element through specific APIs.
     *
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     *
     * @throws InvalidParameterException either the unique identifier or the status are invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void updateMetadataElementEffectivityInStore(String        metadataElementGUID,
                                                        Date          effectiveFrom,
                                                        Date          effectiveTo) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        openMetadataClient.updateMetadataElementEffectivityInStore(userId,
                                                                   externalSourceGUID,
                                                                   externalSourceName,
                                                                   metadataElementGUID,
                                                                   forLineage,
                                                                   forDuplicateProcessing,
                                                                   effectiveFrom,
                                                                   effectiveTo,
                                                                   getEffectiveTime());
    }


    /**
     * Update the effectivity dates control the visibility of the element through specific APIs.
     *
     * @param externalSourceGUID      unique identifier of the software capability that owns this collection
     * @param externalSourceName      unique name of the software capability that owns this collection
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     *
     * @throws InvalidParameterException either the unique identifier or the status are invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void updateMetadataElementEffectivityInStore(String        externalSourceGUID,
                                                        String        externalSourceName,
                                                        String        metadataElementGUID,
                                                        Date          effectiveFrom,
                                                        Date          effectiveTo) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        openMetadataClient.updateMetadataElementEffectivityInStore(userId,
                                                                   externalSourceGUID,
                                                                   externalSourceName,
                                                                   metadataElementGUID,
                                                                   forLineage,
                                                                   forDuplicateProcessing,
                                                                   effectiveFrom,
                                                                   effectiveTo,
                                                                   getEffectiveTime());
    }


    /**
     * Delete a specific metadata element.
     *
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param cascadedDelete     boolean indicating whether the delete request can cascade to dependent elements
     *
     * @throws InvalidParameterException the unique identifier is null or invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to delete this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void deleteMetadataElementInStore(String  metadataElementGUID,
                                             boolean cascadedDelete) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        openMetadataClient.deleteMetadataElementInStore(userId,
                                                        externalSourceGUID,
                                                        externalSourceName,
                                                        metadataElementGUID,
                                                        cascadedDelete,
                                                        forLineage,
                                                        forDuplicateProcessing,
                                                        getEffectiveTime());
    }


    /**
     * Delete a specific metadata element.
     *
     * @param externalSourceGUID      unique identifier of the software capability that owns this collection
     * @param externalSourceName      unique name of the software capability that owns this collection
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param cascadedDelete     boolean indicating whether the delete request can cascade to dependent elements
     *
     * @throws InvalidParameterException the unique identifier is null or invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to delete this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void deleteMetadataElementInStore(String  externalSourceGUID,
                                             String  externalSourceName,
                                             String  metadataElementGUID,
                                             boolean cascadedDelete) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        openMetadataClient.deleteMetadataElementInStore(userId,
                                                        externalSourceGUID,
                                                        externalSourceName,
                                                        metadataElementGUID,
                                                        cascadedDelete,
                                                        forLineage,
                                                        forDuplicateProcessing,
                                                        getEffectiveTime());
    }


    /**
     * Archive a specific metadata element.
     *
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param archiveProperties description of the archiving process
     *
     * @throws InvalidParameterException the unique identifier is null or invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to delete this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void archiveMetadataElementInStore(String            metadataElementGUID,
                                              ArchiveProperties archiveProperties) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        openMetadataClient.archiveMetadataElementInStore(userId,
                                                         externalSourceGUID,
                                                         externalSourceName,
                                                         metadataElementGUID,
                                                         archiveProperties,
                                                         forLineage,
                                                         forDuplicateProcessing,
                                                         getEffectiveTime());
    }


    /**
     * Archive a specific metadata element.
     *
     * @param externalSourceGUID      unique identifier of the software capability that owns this collection
     * @param externalSourceName      unique name of the software capability that owns this collection
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param archiveProperties description of the archiving process
     *
     * @throws InvalidParameterException the unique identifier is null or invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to delete this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void archiveMetadataElementInStore(String            externalSourceGUID,
                                              String            externalSourceName,
                                              String            metadataElementGUID,
                                              ArchiveProperties archiveProperties) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        openMetadataClient.archiveMetadataElementInStore(userId,
                                                         externalSourceGUID,
                                                         externalSourceName,
                                                         metadataElementGUID,
                                                         archiveProperties,
                                                         forLineage,
                                                         forDuplicateProcessing,
                                                         getEffectiveTime());
    }


    /**
     * Add a new classification to the metadata element.  Note that only one classification with the same name can be attached to
     * a metadata element.
     *
     * @param externalSourceGUID      unique identifier of the software capability that owns this collection
     * @param externalSourceName      unique name of the software capability that owns this collection
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param classificationName name of the classification to add (if the classification is already present then use reclassify)
     * @param properties properties to store in the new classification.  These must conform to the valid properties associated with the
     *                   classification name
     *
     * @throws InvalidParameterException the unique identifier or classification name is null or invalid in some way; properties do not match the
     *                                   valid properties associated with the classification's type definition
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void classifyMetadataElementInStore(String            externalSourceGUID,
                                               String            externalSourceName,
                                               String            metadataElementGUID,
                                               String            classificationName,
                                               ElementProperties properties) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        openMetadataClient.classifyMetadataElementInStore(userId,
                                                          externalSourceGUID,
                                                          externalSourceName,
                                                          metadataElementGUID,
                                                          classificationName,
                                                          forLineage,
                                                          forDuplicateProcessing,
                                                          null,
                                                          null,
                                                          properties,
                                                          getEffectiveTime());
    }




    /**
     * Add a new classification to the metadata element.  Note that only one classification with the same name can be attached to
     * a metadata element.
     *
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param classificationName name of the classification to add (if the classification is already present then use reclassify)
     * @param properties properties to store in the new classification.  These must conform to the valid properties associated with the
     *                   classification name
     *
     * @throws InvalidParameterException the unique identifier or classification name is null or invalid in some way; properties do not match the
     *                                   valid properties associated with the classification's type definition
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void classifyMetadataElementInStore(String            metadataElementGUID,
                                               String            classificationName,
                                               ElementProperties properties) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        openMetadataClient.classifyMetadataElementInStore(userId,
                                                          externalSourceGUID,
                                                          externalSourceName,
                                                          metadataElementGUID,
                                                          classificationName,
                                                          forLineage,
                                                          forDuplicateProcessing,
                                                          null,
                                                          null,
                                                          properties,
                                                          getEffectiveTime());
    }


    /**
     * Add a new classification to the metadata element.  Note that only one classification with the same name can be attached to
     * a metadata element.
     *
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param classificationName name of the classification to add (if the classification is already present then use reclassify)
     * @param effectiveFrom the date when this classification is active - null for active now
     * @param effectiveTo the date when this classification becomes inactive - null for active until deleted
     * @param properties properties to store in the new classification.  These must conform to the valid properties associated with the
     *                   classification name
     *
     * @throws InvalidParameterException the unique identifier or classification name is null or invalid in some way; properties do not match the
     *                                   valid properties associated with the classification's type definition
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void classifyMetadataElementInStore(String            metadataElementGUID,
                                               String            classificationName,
                                               Date              effectiveFrom,
                                               Date              effectiveTo,
                                               ElementProperties properties) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        openMetadataClient.classifyMetadataElementInStore(userId,
                                                          externalSourceGUID,
                                                          externalSourceName,
                                                          metadataElementGUID,
                                                          classificationName,
                                                          forLineage,
                                                          forDuplicateProcessing,
                                                          effectiveFrom,
                                                          effectiveTo,
                                                          properties,
                                                          getEffectiveTime());
    }


    /**
     * Add a new classification to the metadata element.  Note that only one classification with the same name can be attached to
     * a metadata element.
     *
     * @param externalSourceGUID      unique identifier of the software capability that owns this collection
     * @param externalSourceName      unique name of the software capability that owns this collection
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param classificationName name of the classification to add (if the classification is already present then use reclassify)
     * @param effectiveFrom the date when this classification is active - null for active now
     * @param effectiveTo the date when this classification becomes inactive - null for active until deleted
     * @param properties properties to store in the new classification.  These must conform to the valid properties associated with the
     *                   classification name
     *
     * @throws InvalidParameterException the unique identifier or classification name is null or invalid in some way; properties do not match the
     *                                   valid properties associated with the classification's type definition
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void classifyMetadataElementInStore(String            externalSourceGUID,
                                               String            externalSourceName,
                                               String            metadataElementGUID,
                                               String            classificationName,
                                               Date              effectiveFrom,
                                               Date              effectiveTo,
                                               ElementProperties properties) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        openMetadataClient.classifyMetadataElementInStore(userId,
                                                          externalSourceGUID,
                                                          externalSourceName,
                                                          metadataElementGUID,
                                                          classificationName,
                                                          forLineage,
                                                          forDuplicateProcessing,
                                                          effectiveFrom,
                                                          effectiveTo,
                                                          properties,
                                                          getEffectiveTime());
    }


    /**
     * Update the properties of a classification that is currently attached to a specific metadata element.
     *
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param classificationName unique name of the classification to update
     * @param replaceProperties flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                          the individual properties specified on the request.
     * @param properties new properties for the classification
     *
     * @throws InvalidParameterException the unique identifier or classification name is null or invalid in some way; properties do not match the
     *                                   valid properties associated with the classification's type definition
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element/classification
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void reclassifyMetadataElementInStore(String            metadataElementGUID,
                                                 String            classificationName,
                                                 boolean           replaceProperties,
                                                 ElementProperties properties) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        openMetadataClient.reclassifyMetadataElementInStore(userId,
                                                            externalSourceGUID,
                                                            externalSourceName,
                                                            metadataElementGUID,
                                                            classificationName,
                                                            replaceProperties,
                                                            forLineage,
                                                            forDuplicateProcessing,
                                                            properties,
                                                            getEffectiveTime());
    }


    /**
     * Update the properties of a classification that is currently attached to a specific metadata element.
     *
     * @param externalSourceGUID      unique identifier of the software capability that owns this collection
     * @param externalSourceName      unique name of the software capability that owns this collection
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param classificationName unique name of the classification to update
     * @param replaceProperties flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                          the individual properties specified on the request.
     * @param properties new properties for the classification
     *
     * @throws InvalidParameterException the unique identifier or classification name is null or invalid in some way; properties do not match the
     *                                   valid properties associated with the classification's type definition
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element/classification
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void reclassifyMetadataElementInStore(String            externalSourceGUID,
                                                 String            externalSourceName,
                                                 String            metadataElementGUID,
                                                 String            classificationName,
                                                 boolean           replaceProperties,
                                                 ElementProperties properties) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        openMetadataClient.reclassifyMetadataElementInStore(userId,
                                                            externalSourceGUID,
                                                            externalSourceName,
                                                            metadataElementGUID,
                                                            classificationName,
                                                            replaceProperties,
                                                            forLineage,
                                                            forDuplicateProcessing,
                                                            properties,
                                                            getEffectiveTime());
    }


    /**
     * Update the effectivity dates of a specific classification attached to a metadata element.
     * The effectivity dates control the visibility of the classification through specific APIs.
     *
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param classificationName unique name of the classification to update
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     *
     * @throws InvalidParameterException either the unique identifier or the status are invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void updateClassificationEffectivityInStore(String  metadataElementGUID,
                                                       String  classificationName,
                                                       Date    effectiveFrom,
                                                       Date    effectiveTo) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        openMetadataClient.updateClassificationEffectivityInStore(userId,
                                                                  externalSourceGUID,
                                                                  externalSourceName,
                                                                  metadataElementGUID,
                                                                  classificationName,
                                                                  forLineage,
                                                                  forDuplicateProcessing,
                                                                  effectiveFrom,
                                                                  effectiveTo,
                                                                  getEffectiveTime());
    }


    /**
     * Update the effectivity dates of a specific classification attached to a metadata element.
     * The effectivity dates control the visibility of the classification through specific APIs.
     *
     * @param externalSourceGUID      unique identifier of the software capability that owns this collection
     * @param externalSourceName      unique name of the software capability that owns this collection
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param classificationName unique name of the classification to update
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     *
     * @throws InvalidParameterException either the unique identifier or the status are invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void updateClassificationEffectivityInStore(String  externalSourceGUID,
                                                       String  externalSourceName,
                                                       String  metadataElementGUID,
                                                       String  classificationName,
                                                       Date    effectiveFrom,
                                                       Date    effectiveTo) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        openMetadataClient.updateClassificationEffectivityInStore(userId,
                                                                  externalSourceGUID,
                                                                  externalSourceName,
                                                                  metadataElementGUID,
                                                                  classificationName,
                                                                  forLineage,
                                                                  forDuplicateProcessing,
                                                                  effectiveFrom,
                                                                  effectiveTo,
                                                                  getEffectiveTime());
    }


    /**
     * Remove the named classification from a specific metadata element.
     *
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param classificationName unique name of the classification to remove
     *
     * @throws InvalidParameterException the unique identifier or classification name is null or invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to remove this classification
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void declassifyMetadataElementInStore(String  metadataElementGUID,
                                                 String  classificationName) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        openMetadataClient.declassifyMetadataElementInStore(userId,
                                                            externalSourceGUID,
                                                            externalSourceName,
                                                            metadataElementGUID,
                                                            classificationName,
                                                            forLineage,
                                                            forDuplicateProcessing,
                                                            getEffectiveTime());
    }


    /**
     * Remove the named classification from a specific metadata element.
     *
     * @param externalSourceGUID      unique identifier of the software capability that owns this collection
     * @param externalSourceName      unique name of the software capability that owns this collection
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param classificationName unique name of the classification to remove
     *
     * @throws InvalidParameterException the unique identifier or classification name is null or invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to remove this classification
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void declassifyMetadataElementInStore(String  externalSourceGUID,
                                                 String  externalSourceName,
                                                 String  metadataElementGUID,
                                                 String  classificationName) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        openMetadataClient.declassifyMetadataElementInStore(userId,
                                                            externalSourceGUID,
                                                            externalSourceName,
                                                            metadataElementGUID,
                                                            classificationName,
                                                            forLineage,
                                                            forDuplicateProcessing,
                                                            getEffectiveTime());
    }


    /**
     * Create a relationship between two metadata elements.  It is important to put the right element at each end of the relationship
     * according to the type definition since this will affect how the relationship is interpreted.
     *
     * @param relationshipTypeName name of the type of relationship to create.  This will determine the types of metadata elements that can be
     *                             related and the properties that can be associated with this relationship.
     * @param metadataElement1GUID unique identifier of the metadata element at end 1 of the relationship
     * @param metadataElement2GUID unique identifier of the metadata element at end 2 of the relationship
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param properties the properties of the relationship
     *
     * @return unique identifier of the new relationship
     *
     * @throws InvalidParameterException the unique identifier's of the metadata elements are null or invalid in some way; the properties are
     *                                    not valid for this type of relationship
     * @throws UserNotAuthorizedException the governance action service is not authorized to create this type of relationship
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public String createRelatedElementsInStore(String            relationshipTypeName,
                                               String            metadataElement1GUID,
                                               String            metadataElement2GUID,
                                               Date              effectiveFrom,
                                               Date              effectiveTo,
                                               ElementProperties properties) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        return openMetadataClient.createRelatedElementsInStore(userId,
                                                               externalSourceGUID,
                                                               externalSourceName,
                                                               relationshipTypeName,
                                                               metadataElement1GUID,
                                                               metadataElement2GUID,
                                                               forLineage,
                                                               forDuplicateProcessing,
                                                               effectiveFrom,
                                                               effectiveTo,
                                                               properties,
                                                               getEffectiveTime());
    }


    /**
     * Create a relationship between two metadata elements.  It is important to put the right element at each end of the relationship
     * according to the type definition since this will affect how the relationship is interpreted.
     *
     * @param externalSourceGUID      unique identifier of the software capability that owns this collection
     * @param externalSourceName      unique name of the software capability that owns this collection
     * @param relationshipTypeName name of the type of relationship to create.  This will determine the types of metadata elements that can be
     *                             related and the properties that can be associated with this relationship.
     * @param metadataElement1GUID unique identifier of the metadata element at end 1 of the relationship
     * @param metadataElement2GUID unique identifier of the metadata element at end 2 of the relationship
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param properties the properties of the relationship
     *
     * @return unique identifier of the new relationship
     *
     * @throws InvalidParameterException the unique identifier's of the metadata elements are null or invalid in some way; the properties are
     *                                    not valid for this type of relationship
     * @throws UserNotAuthorizedException the governance action service is not authorized to create this type of relationship
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public String createRelatedElementsInStore(String            externalSourceGUID,
                                               String            externalSourceName,
                                               String            relationshipTypeName,
                                               String            metadataElement1GUID,
                                               String            metadataElement2GUID,
                                               Date              effectiveFrom,
                                               Date              effectiveTo,
                                               ElementProperties properties) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        return openMetadataClient.createRelatedElementsInStore(userId,
                                                               externalSourceGUID,
                                                               externalSourceName,
                                                               relationshipTypeName,
                                                               metadataElement1GUID,
                                                               metadataElement2GUID,
                                                               forLineage,
                                                               forDuplicateProcessing,
                                                               effectiveFrom,
                                                               effectiveTo,
                                                               properties,
                                                               getEffectiveTime());
    }


    /**
     * Update the properties associated with a relationship.
     *
     * @param relationshipGUID unique identifier of the relationship to update
     * @param replaceProperties flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                          the individual properties specified on the request.
     * @param properties new properties for the relationship
     *
     * @throws InvalidParameterException the unique identifier of the relationship is null or invalid in some way; the properties are
     *                                    not valid for this type of relationship
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this relationship
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void updateRelatedElementsInStore(String            relationshipGUID,
                                             boolean           replaceProperties,
                                             ElementProperties properties) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        openMetadataClient.updateRelationshipInStore(userId,
                                                     externalSourceGUID,
                                                     externalSourceName,
                                                     relationshipGUID,
                                                     replaceProperties,
                                                     forLineage,
                                                     forDuplicateProcessing,
                                                     properties,
                                                     getEffectiveTime());
    }


    /**
     * Update the properties associated with a relationship.
     *
     * @param externalSourceGUID      unique identifier of the software capability that owns this collection
     * @param externalSourceName      unique name of the software capability that owns this collection
     * @param relationshipGUID unique identifier of the relationship to update
     * @param replaceProperties flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                          the individual properties specified on the request.
     * @param properties new properties for the relationship
     *
     * @throws InvalidParameterException the unique identifier of the relationship is null or invalid in some way; the properties are
     *                                    not valid for this type of relationship
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this relationship
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void updateRelatedElementsInStore(String            externalSourceGUID,
                                             String            externalSourceName,
                                             String            relationshipGUID,
                                             boolean           replaceProperties,
                                             ElementProperties properties) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        openMetadataClient.updateRelationshipInStore(userId,
                                                     externalSourceGUID,
                                                     externalSourceName,
                                                     relationshipGUID,
                                                     replaceProperties,
                                                     forLineage,
                                                     forDuplicateProcessing,
                                                     properties,
                                                     getEffectiveTime());
    }


    /**
     * Update the effectivity dates of a specific relationship between metadata elements.
     * The effectivity dates control the visibility of the classification through specific APIs.
     *
     * @param relationshipGUID unique identifier of the relationship to update
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     *
     * @throws InvalidParameterException either the unique identifier or the status are invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void updateRelatedElementsEffectivityInStore(String  relationshipGUID,
                                                        Date    effectiveFrom,
                                                        Date    effectiveTo) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        openMetadataClient.updateRelationshipEffectivityInStore(userId,
                                                                externalSourceGUID,
                                                                externalSourceName,
                                                                relationshipGUID,
                                                                forLineage,
                                                                forDuplicateProcessing,
                                                                effectiveFrom,
                                                                effectiveTo,
                                                                getEffectiveTime());
    }


    /**
     * Update the effectivity dates of a specific relationship between metadata elements.
     * The effectivity dates control the visibility of the classification through specific APIs.
     *
     * @param externalSourceGUID      unique identifier of the software capability that owns this collection
     * @param externalSourceName      unique name of the software capability that owns this collection
     * @param relationshipGUID unique identifier of the relationship to update
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     *
     * @throws InvalidParameterException either the unique identifier or the status are invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void updateRelatedElementsEffectivityInStore(String  externalSourceGUID,
                                                        String  externalSourceName,
                                                        String  relationshipGUID,
                                                        Date    effectiveFrom,
                                                        Date    effectiveTo) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        openMetadataClient.updateRelationshipEffectivityInStore(userId,
                                                                externalSourceGUID,
                                                                externalSourceName,
                                                                relationshipGUID,
                                                                forLineage,
                                                                forDuplicateProcessing,
                                                                effectiveFrom,
                                                                effectiveTo,
                                                                getEffectiveTime());
    }


    /**
     * Delete a relationship between two metadata elements.
     *
     * @param relationshipGUID unique identifier of the relationship to delete
     *
     * @throws InvalidParameterException the unique identifier of the relationship is null or invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to delete this relationship
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void deleteRelatedElementsInStore(String  relationshipGUID) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        openMetadataClient.deleteRelationshipInStore(userId,
                                                     externalSourceGUID,
                                                     externalSourceName,
                                                     relationshipGUID,
                                                     forLineage,
                                                     forDuplicateProcessing,
                                                     getEffectiveTime());
    }


    /**
     * Delete a relationship between two metadata elements.
     *
     * @param externalSourceGUID      unique identifier of the software capability that owns this collection
     * @param externalSourceName      unique name of the software capability that owns this collection
     * @param relationshipGUID unique identifier of the relationship to delete
     *
     * @throws InvalidParameterException the unique identifier of the relationship is null or invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to delete this relationship
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void deleteRelatedElementsInStore(String  externalSourceGUID,
                                             String  externalSourceName,
                                             String  relationshipGUID) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        openMetadataClient.deleteRelationshipInStore(userId,
                                                     externalSourceGUID,
                                                     externalSourceName,
                                                     relationshipGUID,
                                                     forLineage,
                                                     forDuplicateProcessing,
                                                     getEffectiveTime());
    }


    /**
     * Create an incident report to capture the situation detected by this governance action service.
     * This incident report will be processed by other governance activities.
     *
     * @param qualifiedName unique identifier to give this new incident report
     * @param domainIdentifier governance domain associated with this action (0=ALL)
     * @param background description of the situation
     * @param impactedResources details of the resources impacted by this situation
     * @param previousIncidents links to previous incident reports covering this situation
     * @param incidentClassifiers initial classifiers for the incident report
     * @param additionalProperties additional arbitrary properties for the incident reports
     * @param originatorGUID the unique identifier of the person or process that created the incident
     *
     * @return unique identifier of the resulting incident report
     * @throws InvalidParameterException null or non-unique qualified name for the incident report
     * @throws UserNotAuthorizedException this governance action service is not authorized to create an incident report
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public  String createIncidentReport(String                        qualifiedName,
                                        int                           domainIdentifier,
                                        String                        background,
                                        List<IncidentImpactedElement> impactedResources,
                                        List<IncidentDependency>      previousIncidents,
                                        Map<String, Integer>          incidentClassifiers,
                                        Map<String, String>           additionalProperties,
                                        String                        originatorGUID) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        return openMetadataClient.createIncidentReport(userId,
                                                       qualifiedName,
                                                       domainIdentifier,
                                                       background,
                                                       impactedResources,
                                                       previousIncidents,
                                                       incidentClassifiers,
                                                       additionalProperties,
                                                       originatorGUID);
    }


    /**
     * Create a "To Do" request for someone to work on.
     *
     * @param qualifiedName unique name for the to do.  (Could be the engine name and a guid?)
     * @param title short meaningful phrase for the person receiving the request
     * @param instructions further details on what to do
     * @param todoCategory a category of to dos (for example, "data error", "access request")
     * @param priority priority value (based on organization's scale)
     * @param dueDate date/time this needs to be completed
     * @param additionalProperties additional arbitrary properties for the incident reports
     * @param assignTo qualified name of the Actor element for the recipient
     * @param sponsorGUID unique identifier of the element that describes the rule, project that this is on behalf of
     * @param actionTargets the list of elements that should be acted upon
     *
     * @return unique identifier of new to do element
     *
     * @throws InvalidParameterException either todoQualifiedName or assignedTo are null or not recognized
     * @throws UserNotAuthorizedException the governance action service is not authorized to create a "to do" entity
     * @throws PropertyServerException there is a problem connecting to (or inside) the metadata store
     */
    public String openToDo(String                qualifiedName,
                           String                title,
                           String                instructions,
                           String                todoCategory,
                           int                   priority,
                           Date                  dueDate,
                           Map<String, String>   additionalProperties,
                           String                assignTo,
                           String                sponsorGUID,
                           List<NewActionTarget> actionTargets) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        return openMetadataClient.openToDo(userId, qualifiedName, title, instructions, todoCategory, priority, dueDate, additionalProperties, assignTo, sponsorGUID, originatorGUID, actionTargets);
    }


    /**
     * Create a new context event
     *
     * @param anchorGUID unique identifier for the context event's anchor element
     * @param parentContextEvents which context events should be linked as parents (guid->relationship properties)
     * @param childContextEvents which context events should be linked as children (guid->relationship properties)
     * @param relatedContextEvents which context events should be linked as related (guid->relationship properties)
     * @param impactedElements which elements are impacted by this context event (guid->relationship properties)
     * @param effectedDataResourceGUIDs which data resources are effected by this context event (asset guid->relationship properties)
     * @param contextEventEvidenceGUIDs which elements provide evidence that the context event is happening (element GUIDs)
     * @param contextEventProperties properties for the context event itself
     * @return guid of the new context event
     * @throws InvalidParameterException one of the properties are invalid
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException there is a problem connecting to (or inside) the metadata store
     */
    public String registerContextEvent(String                                       anchorGUID,
                                       Map<String, DependentContextEventProperties> parentContextEvents,
                                       Map<String, DependentContextEventProperties> childContextEvents,
                                       Map<String, RelatedContextEventProperties>   relatedContextEvents,
                                       Map<String, ContextEventImpactProperties>    impactedElements,
                                       Map<String, RelationshipProperties>          effectedDataResourceGUIDs,
                                       Map<String, RelationshipProperties>          contextEventEvidenceGUIDs,
                                       ContextEventProperties contextEventProperties) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        return openMetadataClient.registerContextEvent(userId, anchorGUID, parentContextEvents, childContextEvents, relatedContextEvents, impactedElements, effectedDataResourceGUIDs, contextEventEvidenceGUIDs, contextEventProperties);
    }


    /**
     * Create or update the valid value for a particular open metadata property name.  If the typeName is null, this valid value
     * applies to properties of this name from all types.  The valid value is stored in the preferredValue property.  If a valid value is
     * already set up for this property (with overlapping effective dates) then the valid value is updated.
     *
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param validMetadataValue preferred value to use in the open metadata types plus additional descriptive values.
     *
     * @throws InvalidParameterException  the property name is null or not known.
     * @throws UserNotAuthorizedException the service is not able to create/access the element
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    public void setUpValidMetadataValue(String             typeName,
                                        String             propertyName,
                                        ValidMetadataValue validMetadataValue) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        openMetadataClient.setUpValidMetadataValue(userId, typeName, propertyName, validMetadataValue);
    }


    /**
     * Create or update the valid value for a name that can be stored in a particular open metadata property name.
     * This property is of type map from name to string.
     * The valid value is stored in the preferredValue property of validMetadataValue.
     * If the typeName is null, this valid value applies to properties of this name from any open metadata type.
     * If a valid value is already set up for this property (with overlapping effective dates) then the valid value is updated.
     *
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param validMetadataValue preferred value to use in the open metadata types plus additional descriptive values.
     *
     * @throws InvalidParameterException  the property name is null or not known.
     * @throws UserNotAuthorizedException the service is not able to create/access the element
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    public void setUpValidMetadataMapName(String             typeName,
                                          String             propertyName,
                                          ValidMetadataValue validMetadataValue) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        openMetadataClient.setUpValidMetadataMapName(userId, typeName, propertyName, validMetadataValue);
    }


    /**
     * Create or update the valid value for a name that can be stored in a particular open metadata property name.
     * This property is of type map from name to string.
     * The valid value is stored in the preferredValue property of validMetadataValue.
     * If the typeName is null, this valid value applies to properties of this name from any open metadata type.
     * If a valid value is already set up for this property (with overlapping effective dates) then the valid value is updated.
     *
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param mapName name in the map that this valid value applies.  If null then the value can be used for any name in the map.
     * @param validMetadataValue preferred value to use in the open metadata types plus additional descriptive values.
     *
     * @throws InvalidParameterException  the property name is null or not known.
     * @throws UserNotAuthorizedException the service is not able to create/access the element
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    public void setUpValidMetadataMapValue(String             typeName,
                                           String             propertyName,
                                           String             mapName,
                                           ValidMetadataValue validMetadataValue) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        openMetadataClient.setUpValidMetadataMapValue(userId, typeName, propertyName, mapName, validMetadataValue);
    }


    /**
     * Remove a valid value for a property.
     *
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param preferredValue specific valid value to remove
     *
     * @throws InvalidParameterException  the property name is null or not known.
     * @throws UserNotAuthorizedException the service is not able to create/access the element
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    public void clearValidMetadataValue(String typeName,
                                        String propertyName,
                                        String preferredValue) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        openMetadataClient.clearValidMetadataValue(userId, typeName, propertyName, preferredValue);
    }


    /**
     * Remove a valid map name value for a property.  The match is done on preferred name.
     *
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param preferredValue specific valid value to remove
     *
     * @throws InvalidParameterException  the property name is null or not known.
     * @throws UserNotAuthorizedException the service is not able to create/access the element
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    public void clearValidMetadataMapName(String typeName,
                                          String propertyName,
                                          String preferredValue) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        openMetadataClient.clearValidMetadataMapName(userId, typeName, propertyName, preferredValue);
    }


    /**
     * Remove a valid map name value for a property.  The match is done on preferred name.
     *
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param mapName name in the map that this valid value applies.  If null then the value can be used for any name in the map.
     * @param preferredValue specific valid value to remove
     *
     * @throws InvalidParameterException  the property name is null or not known.
     * @throws UserNotAuthorizedException the service is not able to create/access the element
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    public void clearValidMetadataMapValue(String typeName,
                                           String propertyName,
                                           String mapName,
                                           String preferredValue) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        openMetadataClient.clearValidMetadataMapValue(userId, typeName, propertyName, mapName, preferredValue);
    }


    /**
     * Validate whether the value found in an open metadata property is valid.
     *
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param actualValue value stored in the property - if this is null, true is only returned if null is set up as a valid value.
     *
     * @return boolean flag - true if the value is one of the defined valid values or there are no valid values set up for the property (and so any value is value).
     *
     * @throws InvalidParameterException  the property name is null or not known.
     * @throws UserNotAuthorizedException the service is not able to create/access the element
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    public boolean validateMetadataValue(String typeName,
                                         String propertyName,
                                         String actualValue) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        return openMetadataClient.validateMetadataValue(userId, typeName, propertyName, actualValue);
    }


    /**
     * Validate whether the name found in an open metadata map property is valid.
     *
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param actualValue value stored in the property - if this is null, true is only returned if null is set up as a valid value.
     *
     * @return boolean flag - true if the value is one of the defined valid values or there are no valid values set up for the property (and so any value is value).
     *
     * @throws InvalidParameterException  the property name is null or not known.
     * @throws UserNotAuthorizedException the service is not able to create/access the element
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    public boolean validateMetadataMapName(String typeName,
                                           String propertyName,
                                           String actualValue) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        return openMetadataClient.validateMetadataMapName(userId, typeName, propertyName, actualValue);
    }


    /**
     * Validate whether the name found in an open metadata map property is valid.
     *
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param mapName name in the map that this valid value applies.  If null then the value can be used for any name in the map.
     * @param actualValue value stored in the property - if this is null, true is only returned if null is set up as a valid value.
     *
     * @return boolean flag - true if the value is one of the defined valid values or there are no valid values set up for the property (and so any value is value).
     *
     * @throws InvalidParameterException  the property name is null or not known.
     * @throws UserNotAuthorizedException the service is not able to create/access the element
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    public boolean validateMetadataMapValue(String typeName,
                                            String propertyName,
                                            String mapName,
                                            String actualValue) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        return openMetadataClient.validateMetadataMapValue(userId, typeName, propertyName, mapName, actualValue);
    }


    /**
     * Retrieve details of a specific valid value for a property.
     *
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param preferredValue valid value to match
     *
     * @return specific valid value definition or none if there is no definition stored
     *
     * @throws InvalidParameterException  the property name is null or not known.
     * @throws UserNotAuthorizedException the service is not able to create/access the element
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    public ValidMetadataValue getValidMetadataValue(String typeName,
                                                    String propertyName,
                                                    String preferredValue) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        return openMetadataClient.getValidMetadataValue(userId, typeName, propertyName, preferredValue);
    }


    /**
     * Retrieve details of a specific valid name for a map property.
     *
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param preferredValue valid value to match
     *
     * @return specific valid value definition or none if there is no definition stored
     *
     * @throws InvalidParameterException  the property name is null or not known.
     * @throws UserNotAuthorizedException the service is not able to create/access the element
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    public ValidMetadataValue getValidMetadataMapName(String typeName,
                                                      String propertyName,
                                                      String preferredValue) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        return openMetadataClient.getValidMetadataMapName(userId, typeName, propertyName, preferredValue);
    }


    /**
     * Retrieve details of a specific valid value for a map name.
     *
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param mapName name in the map that this valid value applies.  If null then the value can be used for any name in the map.
     * @param preferredValue valid value to match
     *
     * @return specific valid value definition or none if there is no definition stored
     *
     * @throws InvalidParameterException  the property name is null or not known.
     * @throws UserNotAuthorizedException the service is not able to create/access the element
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    public ValidMetadataValue getValidMetadataMapValue(String typeName,
                                                       String propertyName,
                                                       String mapName,
                                                       String preferredValue) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        return openMetadataClient.getValidMetadataMapValue(userId, typeName, propertyName, mapName, preferredValue);
    }


    /**
     * Retrieve all the valid values for the requested property.
     *
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of valid values defined for the property
     *
     * @throws InvalidParameterException  the property name is null or not known.
     * @throws UserNotAuthorizedException the service is not able to create/access the element
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    public List<ValidMetadataValueDetail> getValidMetadataValues(String typeName,
                                                                 String propertyName,
                                                                 int    startFrom,
                                                                 int    pageSize) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        return openMetadataClient.getValidMetadataValues(userId, typeName, propertyName, startFrom, pageSize);
    }



    /**
     * Retrieve all the consistent valid values for the requested property.
     *
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param mapName optional name of map key that this valid value applies
     * @param preferredValue the value to match against
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of valid values defined for the property
     *
     * @throws InvalidParameterException  the property name is null or not known.
     * @throws UserNotAuthorizedException the service is not able to create/access the element
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    public List<ValidMetadataValue> getConsistentMetadataValues(String typeName,
                                                                String propertyName,
                                                                String mapName,
                                                                String preferredValue,
                                                                int    startFrom,
                                                                int    pageSize) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        return openMetadataClient.getConsistentMetadataValues(userId, typeName, propertyName, mapName, preferredValue, startFrom, pageSize);
    }


    /*
     * Work with external identifiers.
     */

    /**
     * Add a new external identifier to an existing open metadata element.
     *
     * @param externalScopeGUID unique identifier of software server capability representing the caller
     * @param externalScopeName unique name of software server capability representing the caller
     * @param externalScopeTypeName type name of the software capability describing the manager for the external identifier
     * @param openMetadataElementGUID unique identifier (GUID) of the element in the open metadata ecosystem
     * @param openMetadataElementTypeName type name for the open metadata element
     * @param externalIdentifierProperties optional properties used to define an external identifier
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public void addExternalIdentifier(String                       externalScopeGUID,
                                      String                       externalScopeName,
                                      String                       externalScopeTypeName,
                                      String                       openMetadataElementGUID,
                                      String                       openMetadataElementTypeName,
                                      ExternalIdentifierProperties externalIdentifierProperties) throws InvalidParameterException,
                                                                                                        UserNotAuthorizedException,
                                                                                                        PropertyServerException
    {
        openMetadataClient.addExternalIdentifier(userId, externalScopeGUID, externalScopeName, externalScopeTypeName, openMetadataElementGUID, openMetadataElementTypeName, externalIdentifierProperties, null, null, forLineage, forDuplicateProcessing, getEffectiveTime());
    }


    /**
     * Add a new external identifier to an existing open metadata element.
     *
     * @param externalScopeGUID unique identifier of software server capability representing the caller
     * @param externalScopeName unique name of software server capability representing the caller
     * @param externalScopeTypeName type name of the software capability describing the manager for the external identifier
     * @param openMetadataElementGUID unique identifier (GUID) of the element in the open metadata ecosystem
     * @param openMetadataElementTypeName type name for the open metadata element
     * @param externalIdentifierProperties optional properties used to define an external identifier
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public void addExternalIdentifier(String                       externalScopeGUID,
                                      String                       externalScopeName,
                                      String                       externalScopeTypeName,
                                      String                       openMetadataElementGUID,
                                      String                       openMetadataElementTypeName,
                                      ExternalIdentifierProperties externalIdentifierProperties,
                                      Date                         effectiveFrom,
                                      Date                         effectiveTo) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        openMetadataClient.addExternalIdentifier(userId, externalScopeGUID, externalScopeName, externalScopeTypeName, openMetadataElementGUID, openMetadataElementTypeName, externalIdentifierProperties, effectiveFrom, effectiveTo, forLineage, forDuplicateProcessing, getEffectiveTime());
    }


    /**
     * Update the description of a specific external identifier.
     *
     * @param externalScopeGUID unique identifier of software server capability representing the caller
     * @param externalScopeName unique name of software server capability representing the caller
     * @param openMetadataElementGUID unique identifier (GUID) of the element in the open metadata ecosystem
     * @param openMetadataElementTypeName type name for the open metadata element
     * @param externalIdentifierProperties optional properties used to define an external identifier
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public void updateExternalIdentifier(String                       externalScopeGUID,
                                         String                       externalScopeName,
                                         String                       openMetadataElementGUID,
                                         String                       openMetadataElementTypeName,
                                         ExternalIdentifierProperties externalIdentifierProperties) throws InvalidParameterException,
                                                                                                           UserNotAuthorizedException,
                                                                                                           PropertyServerException
    {
        openMetadataClient.updateExternalIdentifier(userId, externalScopeGUID, externalScopeName, OpenMetadataType.INVENTORY_CATALOG.typeName, openMetadataElementGUID, openMetadataElementTypeName, externalIdentifierProperties, null, null, forLineage, forDuplicateProcessing, getEffectiveTime());
    }


    /**
     * Update the description of a specific external identifier.
     *
     * @param externalScopeGUID unique identifier of software server capability representing the caller
     * @param externalScopeName unique name of software server capability representing the caller
     * @param externalScopeTypeName type name of the software capability describing the manager for the external identifier
     * @param openMetadataElementGUID unique identifier (GUID) of the element in the open metadata ecosystem
     * @param openMetadataElementTypeName type name for the open metadata element
     * @param externalIdentifierProperties optional properties used to define an external identifier
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public void updateExternalIdentifier(String                       externalScopeGUID,
                                         String                       externalScopeName,
                                         String                       externalScopeTypeName,
                                         String                       openMetadataElementGUID,
                                         String                       openMetadataElementTypeName,
                                         ExternalIdentifierProperties externalIdentifierProperties) throws InvalidParameterException,
                                                                                                           UserNotAuthorizedException,
                                                                                                           PropertyServerException
    {
        openMetadataClient.updateExternalIdentifier(userId, externalScopeGUID, externalScopeName, externalScopeTypeName, openMetadataElementGUID, openMetadataElementTypeName, externalIdentifierProperties, null, null, forLineage, forDuplicateProcessing, getEffectiveTime());
    }


    /**
     * Update the description of a specific external identifier.
     *
     * @param externalScopeGUID unique identifier of software server capability representing the caller
     * @param externalScopeName unique name of software server capability representing the caller
     * @param externalScopeTypeName type name of the software capability describing the manager for the external identifier
     * @param openMetadataElementGUID unique identifier (GUID) of the element in the open metadata ecosystem
     * @param openMetadataElementTypeName type name for the open metadata element
     * @param externalIdentifierProperties optional properties used to define an external identifier
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public void updateExternalIdentifier(String                       externalScopeGUID,
                                         String                       externalScopeName,
                                         String                       externalScopeTypeName,
                                         String                       openMetadataElementGUID,
                                         String                       openMetadataElementTypeName,
                                         ExternalIdentifierProperties externalIdentifierProperties,
                                         Date                         effectiveFrom,
                                         Date                         effectiveTo) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        openMetadataClient.updateExternalIdentifier(userId, externalScopeGUID, externalScopeName, externalScopeTypeName, openMetadataElementGUID, openMetadataElementTypeName, externalIdentifierProperties, effectiveFrom, effectiveTo, forLineage, forDuplicateProcessing, getEffectiveTime());
    }


    /**
     * Remove an external identifier from an existing open metadata element.  The open metadata element is not
     * affected.
     *
     * @param externalScopeGUID unique identifier of software server capability representing the caller
     * @param externalScopeName unique name of software server capability representing the caller
     * @param openMetadataElementGUID unique identifier (GUID) of the element in the open metadata ecosystem
     * @param openMetadataElementTypeName type name for the open metadata element
     * @param externalIdentifier unique identifier of this element in the third party asset manager
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public void removeExternalIdentifier(String                   externalScopeGUID,
                                         String                   externalScopeName,
                                         String                   openMetadataElementGUID,
                                         String                   openMetadataElementTypeName,
                                         String                   externalIdentifier) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        openMetadataClient.removeExternalIdentifier(userId, externalScopeGUID, externalScopeName, openMetadataElementGUID, openMetadataElementTypeName, externalIdentifier, forLineage, forDuplicateProcessing, getEffectiveTime());
    }


    /**
     * Confirm that the values of a particular metadata element have been synchronized.  This is important
     * from an audit point of view, and to allow bidirectional updates of metadata using optimistic locking.
     *
     * @param externalScopeGUID unique identifier of software server capability representing the caller
     * @param externalScopeName unique name of software server capability representing the caller
     * @param openMetadataElementGUID unique identifier (GUID) of this element in open metadata
     * @param openMetadataElementTypeName type name for the open metadata element
     * @param externalIdentifier unique identifier of this element in the external asset manager
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public void confirmSynchronization(String externalScopeGUID,
                                       String externalScopeName,
                                       String openMetadataElementGUID,
                                       String openMetadataElementTypeName,
                                       String externalIdentifier) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        openMetadataClient.confirmSynchronization(userId, externalScopeGUID, externalScopeName, openMetadataElementGUID, openMetadataElementTypeName, externalIdentifier);
    }


    /**
     * Return the list of headers for open metadata elements that are associated with a particular
     * external identifier.
     *
     * @param externalScopeGUID unique identifier of software server capability representing the caller
     * @param externalScopeName unique name of software server capability representing the caller
     * @param externalIdentifier unique identifier of this element in the external asset manager
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of element headers
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public List<ElementHeader> getElementsForExternalIdentifier(String externalScopeGUID,
                                                                String externalScopeName,
                                                                String externalIdentifier,
                                                                int    startFrom,
                                                                int    pageSize) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        return openMetadataClient.getElementsForExternalIdentifier(userId, externalScopeGUID, externalScopeName, externalIdentifier, startFrom, pageSize, forLineage, forDuplicateProcessing, getEffectiveTime());
    }


    /**
     * Check that the supplied external identifier matches the element GUID.
     *
     * @param externalScopeGUID unique identifier of software server capability representing the caller
     * @param externalScopeName unique name of software server capability representing the caller
     * @param openMetadataElementGUID element guid used for the lookup
     * @param openMetadataElementTypeName type name for the open metadata element
     * @param elementExternalIdentifier external identifier value
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public void validateExternalIdentifier(String  externalScopeGUID,
                                           String  externalScopeName,
                                           String  openMetadataElementGUID,
                                           String  openMetadataElementTypeName,
                                           String  elementExternalIdentifier) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        openMetadataClient.validateExternalIdentifier(userId, externalScopeGUID, externalScopeName, openMetadataElementGUID, openMetadataElementTypeName, elementExternalIdentifier, forLineage, forDuplicateProcessing, getEffectiveTime());
    }


    /**
     * Assemble the correlation headers attached to the supplied element guid.  This includes the external identifiers
     * plus information on the scope and usage.
     *
     * @param externalScopeGUID unique identifier of software server capability representing the caller
     * @param externalScopeName unique name of software server capability representing the caller
     * @param openMetadataElementGUID unique identifier (GUID) of this element in open metadata
     * @param openMetadataElementTypeName type name for the open metadata element
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of correlation headers (note if asset manager identifiers are present, only the matching correlation header is returned)
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public List<MetadataCorrelationHeader> getExternalIdentifiers(String externalScopeGUID,
                                                                  String externalScopeName,
                                                                  String openMetadataElementGUID,
                                                                  String openMetadataElementTypeName,
                                                                  int    startFrom,
                                                                  int    pageSize) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        return openMetadataClient.getExternalIdentifiers(userId, externalScopeGUID, externalScopeName, openMetadataElementGUID, openMetadataElementTypeName, startFrom, pageSize, forLineage, forDuplicateProcessing, getEffectiveTime());
    }


    /**
     * Return the vendor properties associated with an element.  The inner map holds the specific properties for each
     * vendor.  The outer maps the vendor identifier to the properties.
     *
     * @param openMetadataElementGUID unique identifier (GUID) of this element in open metadata
     * @param openMetadataElementTypeName type name for the open metadata element
     * @return map of vendor properties
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public Map<String, Map<String, String>> getVendorProperties(String openMetadataElementGUID,
                                                                String openMetadataElementTypeName) throws InvalidParameterException,
                                                                                                           UserNotAuthorizedException,
                                                                                                           PropertyServerException
    {
        return openMetadataClient.getVendorProperties(userId,  openMetadataElementGUID, openMetadataElementTypeName, forLineage, forDuplicateProcessing, getEffectiveTime());
    }
}