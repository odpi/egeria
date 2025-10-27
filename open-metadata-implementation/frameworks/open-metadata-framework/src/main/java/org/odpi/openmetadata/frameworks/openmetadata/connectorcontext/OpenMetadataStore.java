/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.connectorcontext;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ElementStatus;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.*;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;

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
public class OpenMetadataStore extends ConnectorContextClientBase
{
    private final OpenMetadataClient openMetadataClient;

    /**
     * Constructor for connector context client.
     *
     * @param parentContext connector's context
     * @param localServerName local server where this client is running - used for error handling
     * @param localServiceName local service that his connector is hosted by - used for error handling
     * @param connectorUserId the userId to use with all requests for open metadata
     * @param externalSourceGUID unique identifier of the software server capability for the source of metadata
     * @param externalSourceName unique name of the software server capability for the source of metadata
     * @param openMetadataClient client to access the open metadata store
     * @param auditLog logging destination
     * @param maxPageSize max number of elements that can be returned on a query
     */
    public OpenMetadataStore(ConnectorContextBase parentContext,
                             String               localServerName,
                             String               localServiceName,
                             String               connectorUserId,
                             String               connectorGUID,
                             String               externalSourceGUID,
                             String               externalSourceName,
                             OpenMetadataClient   openMetadataClient,
                             AuditLog             auditLog,
                             int                  maxPageSize)
    {
        super(parentContext, localServerName, localServiceName, connectorUserId, connectorGUID, externalSourceGUID, externalSourceName, auditLog, maxPageSize);

        this.openMetadataClient = openMetadataClient;
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
        return openMetadataClient.getMetadataElementByGUID(connectorUserId,
                                                           elementGUID,
                                                           getGetOptions());
    }


    /**
     * Retrieve the metadata element using its unique identifier.
     *
     * @param elementGUID unique identifier for the metadata element
     * @param getOptions multiple options to control the query
     *
     * @return metadata element properties
     * @throws InvalidParameterException the unique identifier is null or not known.
     * @throws UserNotAuthorizedException the governance action service is not able to access the element
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    public OpenMetadataElement getMetadataElementByGUID(String     elementGUID,
                                                        GetOptions getOptions) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        return openMetadataClient.getMetadataElementByGUID(connectorUserId,
                                                           elementGUID,
                                                           getOptions);
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
        return openMetadataClient.getMetadataElementByUniqueName(connectorUserId,
                                                                 uniqueName,
                                                                 uniquePropertyName,
                                                                 getGetOptions());
    }


    /**
     * Retrieve the metadata element using its unique name (typically the qualified name).
     *
     * @param uniqueName unique name for the metadata element
     * @param uniquePropertyName name of property name to test in the open metadata element - if null "qualifiedName" is used
     * @param getOptions multiple options to control the query
     *
     * @return metadata element properties or null if not found
     * @throws InvalidParameterException the unique identifier is null.
     * @throws UserNotAuthorizedException the governance action service is not able to access the element
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    public OpenMetadataElement getMetadataElementByUniqueName(String     uniqueName,
                                                              String     uniquePropertyName,
                                                              GetOptions getOptions) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        return openMetadataClient.getMetadataElementByUniqueName(connectorUserId,
                                                                 uniqueName,
                                                                 uniquePropertyName,
                                                                 getOptions);
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
        GetOptions getOptions = getGetOptions();

        getOptions.setForLineage(true);

        return openMetadataClient.getMetadataElementByUniqueName(connectorUserId,
                                                                 uniqueName,
                                                                 uniquePropertyName,
                                                                 getOptions);
    }


    /**
     * Retrieve the metadata element using its unique name (typically the qualified name) and the DELETED status.
     * This method assumes all effective dates, and forLineage and forDuplicateProcessing are false,
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
        return openMetadataClient.getDeletedElementByUniqueName(connectorUserId, uniqueName, uniquePropertyName);
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
        return openMetadataClient.getMetadataElementGUIDByUniqueName(connectorUserId,
                                                                     uniqueName,
                                                                     uniquePropertyName,
                                                                     getGetOptions());
    }


    /**
     * Retrieve the unique identifier of a metadata element using its unique name (typically the qualified name).
     *
     * @param uniqueName unique name for the metadata element
     * @param uniquePropertyName name of property name to test in the open metadata element - if null "qualifiedName" is used
     * @param getOptions multiple options to control the query
     *
     * @return metadata element unique identifier (guid)
     * @throws InvalidParameterException the unique identifier is null or not known.
     * @throws UserNotAuthorizedException the governance action service is not able to access the element
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    public String getMetadataElementGUIDByUniqueName(String     uniqueName,
                                                     String     uniquePropertyName,
                                                     GetOptions getOptions) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        return openMetadataClient.getMetadataElementGUIDByUniqueName(connectorUserId,
                                                                     uniqueName,
                                                                     uniquePropertyName,
                                                                     getOptions);
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
        return openMetadataClient.findMetadataElementsWithString(connectorUserId,
                                                                 searchString,
                                                                 new SearchOptions(getQueryOptions(startFrom, pageSize)));
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
        return openMetadataClient.findMetadataElementsWithString(connectorUserId,
                                                                 searchString,
                                                                 new SearchOptions(getQueryOptions(typeName, startFrom, pageSize)));
    }


    /**
     * Retrieve the metadata elements of the requested type that contain the requested string.
     *
     * @param searchString name to retrieve
     * @param searchOptions multiple options to control the query
     *
     * @return list of matching metadata elements (or null if no elements match the name)
     * @throws InvalidParameterException the qualified name is null
     * @throws UserNotAuthorizedException the governance action service is not able to access the element
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    public List<OpenMetadataElement> findMetadataElementsWithString(String        searchString,
                                                                    SearchOptions searchOptions) throws InvalidParameterException,
                                                                                                        UserNotAuthorizedException,
                                                                                                        PropertyServerException
    {
        return openMetadataClient.findMetadataElementsWithString(connectorUserId,
                                                                 searchString,
                                                                 searchOptions);
    }


    /**
     * Return a list of elements with the requested search string in their (display, resource)name, qualified name,
     * title, text, summary, identifier or description.  The search string is interpreted as a regular expression (RegEx).
     * The breadth of the search is determined by the supplied anchorGUID.
     *
     * @param searchString           name to retrieve
     * @param anchorGUID unique identifier of anchor
     * @param queryOptions multiple options to control the query
     *
     * @return list of matching metadata elements (or null if no elements match the name)
     *
     * @throws InvalidParameterException  the qualified name is null
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    public AnchorSearchMatches findElementsForAnchor(String              searchString,
                                                     String              anchorGUID,
                                                     QueryOptions        queryOptions) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException
    {
        return openMetadataClient.findElementsForAnchor(connectorUserId, searchString, anchorGUID, queryOptions);
    }


    /**
     * Return a list of elements with the requested search string in their (display, resource)name, qualified name,
     * title, text, summary, identifier or description.  The search string is interpreted as a regular expression (RegEx).
     * The breadth of the search is determined by the supplied domain name. The results are organized by anchor element.
     *
     * @param searchString           name to retrieve
     * @param anchorDomainName name of open metadata type for the domain
     * @param searchOptions multiple options to control the query
     *
     * @return list of matching metadata elements (or null if no elements match the name)
     *
     * @throws InvalidParameterException  the qualified name is null
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    public List<AnchorSearchMatches> findElementsInAnchorDomain(String              searchString,
                                                                String              anchorDomainName,
                                                                SearchOptions       searchOptions) throws InvalidParameterException,
                                                                                                          UserNotAuthorizedException,
                                                                                                          PropertyServerException
    {
        return openMetadataClient.findElementsInAnchorDomain(connectorUserId, searchString, anchorDomainName, searchOptions);
    }


    /**
     * Return a list of elements with the requested search string in their (display, resource)name, qualified name,
     * title, text, summary, identifier or description.  The search string is interpreted as a regular expression (RegEx).
     * The breadth of the search is determined by the supplied scope guid. The results are organized by anchor element.
     *
     * @param searchString           name to retrieve
     * @param anchorScopeGUID unique identifier of the scope to use
     * @param searchOptions multiple options to control the query
     *
     * @return list of matching metadata elements (or null if no elements match the name)
     *
     * @throws InvalidParameterException  the qualified name is null
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    public List<AnchorSearchMatches> findElementsInAnchorScope(String        searchString,
                                                               String        anchorScopeGUID,
                                                               SearchOptions searchOptions) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        return openMetadataClient.findElementsInAnchorScope(connectorUserId, searchString, anchorScopeGUID, searchOptions);
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
        return openMetadataClient.getRelatedMetadataElements(connectorUserId,
                                                             elementGUID,
                                                             startingAtEnd,
                                                             relationshipTypeName,
                                                             getQueryOptions(startFrom, pageSize));
    }


    /**
     * Retrieve the metadata elements connected to the supplied element.
     *
     * @param elementGUID unique identifier for the starting metadata element
     * @param startingAtEnd indicates which end to retrieve from (0 is "either end"; 1 is end1; 2 is end 2)
     * @param relationshipTypeName type name of relationships to follow (or null for all)
     * @param queryOptions multiple options to control the query
     *
     * @return list of related elements
     * @throws InvalidParameterException the unique identifier is null or not known; the relationship type is invalid
     * @throws UserNotAuthorizedException the governance action service is not able to access the elements
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    public RelatedMetadataElementList getRelatedMetadataElements(String       elementGUID,
                                                                 int          startingAtEnd,
                                                                 String       relationshipTypeName,
                                                                 QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        return openMetadataClient.getRelatedMetadataElements(connectorUserId,
                                                             elementGUID,
                                                             startingAtEnd,
                                                             relationshipTypeName,
                                                             queryOptions);
    }



    /**
     * Retrieve the metadata element connected to the supplied element for a relationship type that only allows one
     * relationship to be attached.
     *
     * @param elementGUID            unique identifier for the starting metadata element
     * @param startingAtEnd          indicates which end to retrieve from (0 is "either end"; 1 is end1; 2 is end 2)
     * @param relationshipTypeName   type name of relationships to follow (or null for all)
     *
     * @return list of related elements
     *
     * @throws InvalidParameterException  the unique identifier is null or not known; the relationship type is invalid
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException    there is a problem accessing the metadata store or multiple relationships have been returned
     */
    public RelatedMetadataElement getRelatedMetadataElement(String  elementGUID,
                                                            int     startingAtEnd,
                                                            String  relationshipTypeName) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        return openMetadataClient.getRelatedMetadataElement(connectorUserId,
                                                            elementGUID,
                                                            startingAtEnd,
                                                            relationshipTypeName,
                                                            getGetOptions());
    }



    /**
     * Retrieve the metadata element connected to the supplied element for a relationship type that only allows one
     * relationship to be attached.
     *
     * @param elementGUID            unique identifier for the starting metadata element
     * @param startingAtEnd          indicates which end to retrieve from (0 is "either end"; 1 is end1; 2 is end 2)
     * @param relationshipTypeName   type name of relationships to follow (or null for all)
     * @param getOptions             multiple options to control the query
     *
     * @return list of related elements
     *
     * @throws InvalidParameterException  the unique identifier is null or not known; the relationship type is invalid
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException    there is a problem accessing the metadata store or multiple relationships have been returned
     */
    public RelatedMetadataElement getRelatedMetadataElement(String     elementGUID,
                                                            int        startingAtEnd,
                                                            String     relationshipTypeName,
                                                            GetOptions getOptions) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        return openMetadataClient.getRelatedMetadataElement(connectorUserId,
                                                            elementGUID,
                                                            startingAtEnd,
                                                            relationshipTypeName,
                                                            getOptions);
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
        return openMetadataClient.getAnchoredElementsGraph(connectorUserId, elementGUID, getQueryOptions(startFrom, pageSize));
    }


    /**
     * Return all the elements that are anchored to an asset plus relationships between these elements and to other elements.
     *
     * @param elementGUID  unique identifier for the element
     * @param queryOptions multiple options to control the query
     *
     * @return graph of elements
     *
     * @throws InvalidParameterException  the unique identifier is null or not known; the relationship type is invalid
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    public OpenMetadataElementGraph getAnchoredElementsGraph(String       elementGUID,
                                                             QueryOptions queryOptions) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        return openMetadataClient.getAnchoredElementsGraph(connectorUserId, elementGUID, queryOptions);
    }


    /**
     * Return each of the versions of a metadata element.
     *
     * @param elementGUID            unique identifier for the metadata element
     * @param queryOptions multiple options to control the query
     *
     * @return a list of elements matching the supplied criteria; null means no matching elements in the metadata store.
     * @throws InvalidParameterException one of the search parameters are is invalid
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    public  List<OpenMetadataElement> getMetadataElementHistory(String                 elementGUID,
                                                                HistoricalQueryOptions queryOptions) throws InvalidParameterException,
                                                                                                            UserNotAuthorizedException,
                                                                                                            PropertyServerException
    {
        return openMetadataClient.getMetadataElementHistory(connectorUserId, elementGUID, queryOptions);
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
        return openMetadataClient.getMetadataElementRelationships(connectorUserId,
                                                                  metadataElementAtEnd1GUID,
                                                                  metadataElementAtEnd2GUID,
                                                                  relationshipTypeName,
                                                                  getQueryOptions(startFrom, pageSize));
    }


    /**
     * Return a list of metadata elements that match the supplied criteria.  The results can be returned over many pages.
     *
     * @param metadataElementTypeName type of interest (null means any element type)
     * @param metadataElementSubtypeNames optional list of the subtypes of the metadataElementTypeName to
     *                           include in the search results. Null means all subtypes.
     * @param searchProperties Optional list of entity property conditions to match.
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return a list of elements matching the supplied criteria; null means no matching elements in the metadata store.
     * @throws InvalidParameterException one of the search parameters are is invalid
     * @throws UserNotAuthorizedException the governance action service is not able to access the elements
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    public List<OpenMetadataElement> findMetadataElements(String                metadataElementTypeName,
                                                          List<String>          metadataElementSubtypeNames,
                                                          SearchProperties      searchProperties,
                                                          SearchClassifications matchClassifications,
                                                          int                   startFrom,
                                                          int                   pageSize) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        return openMetadataClient.findMetadataElements(connectorUserId,
                                                       searchProperties,
                                                       matchClassifications,
                                                       getQueryOptions(metadataElementTypeName, metadataElementSubtypeNames,startFrom, pageSize));
    }


    /**
     * Return a list of metadata elements that match the supplied criteria.  The results can be returned over many pages.
     *
     * @param searchProperties Optional list of entity property conditions to match.
     * @param matchClassifications details of the classifications to match
     * @param queryOptions multiple options to control the query
     *
     * @return a list of elements matching the supplied criteria; null means no matching elements in the metadata store.
     * @throws InvalidParameterException one of the search parameters are is invalid
     * @throws UserNotAuthorizedException the governance action service is not able to access the elements
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    public List<OpenMetadataElement> findMetadataElements(SearchProperties      searchProperties,
                                                          SearchClassifications matchClassifications,
                                                          QueryOptions          queryOptions) throws InvalidParameterException,
                                                                                                     UserNotAuthorizedException,
                                                                                                     PropertyServerException
    {
        return openMetadataClient.findMetadataElements(connectorUserId,
                                                       searchProperties,
                                                       matchClassifications,
                                                       queryOptions);
    }


    /**
     * Return a list of relationships that match the requested conditions.  The results can be received as a series of pages.
     *
     * @param relationshipTypeName relationship's type.  Null means all types
     *                             (but may be slow so not recommended).
     * @param searchProperties Optional list of relationship property conditions to match.
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
                                                                                 int                 startFrom,
                                                                                 int                 pageSize) throws InvalidParameterException,
                                                                                                                      UserNotAuthorizedException,
                                                                                                                      PropertyServerException
    {
        return openMetadataClient.findRelationshipsBetweenMetadataElements(connectorUserId,
                                                                           relationshipTypeName,
                                                                           searchProperties,
                                                                           getQueryOptions(startFrom, pageSize));
    }


    /**
     * Return a list of relationships that match the requested conditions.  The results can be received as a series of pages.
     *
     * @param relationshipTypeName relationship's type.  Null means all types
     *                             (but may be slow so not recommended).
     * @param searchProperties Optional list of relationship property conditions to match.
     * @param queryOptions multiple options to control the query
     *
     * @return a list of relationships.  Null means no matching relationships.
     * @throws InvalidParameterException one of the search parameters are is invalid
     * @throws UserNotAuthorizedException the governance action service is not able to access the elements
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    public OpenMetadataRelationshipList findRelationshipsBetweenMetadataElements(String              relationshipTypeName,
                                                                                 SearchProperties    searchProperties,
                                                                                 QueryOptions        queryOptions) throws InvalidParameterException,
                                                                                                                          UserNotAuthorizedException,
                                                                                                                          PropertyServerException
    {
        return openMetadataClient.findRelationshipsBetweenMetadataElements(connectorUserId,
                                                                           relationshipTypeName,
                                                                           searchProperties,
                                                                           queryOptions);
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
    public OpenMetadataRelationship getRelationshipByGUID(String     relationshipGUID) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException
    {
        return openMetadataClient.getRelationshipByGUID(connectorUserId, relationshipGUID, getGetOptions());
    }


    /**
     * Retrieve the relationship using its unique identifier.
     *
     * @param relationshipGUID unique identifier for the relationship
     * @param getOptions multiple options to control the query
     *
     * @return relationship properties
     * @throws InvalidParameterException the unique identifier is null or not known.
     * @throws UserNotAuthorizedException the governance action service is not able to access the element
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    public OpenMetadataRelationship getRelationshipByGUID(String     relationshipGUID,
                                                          GetOptions getOptions) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        return openMetadataClient.getRelationshipByGUID(connectorUserId, relationshipGUID, getOptions);
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
    public String createMetadataElementInStore(String               metadataElementTypeName,
                                               ElementStatus        initialStatus,
                                               NewElementProperties properties) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        NewElementOptions newElementOptions = new NewElementOptions(this.getMetadataSourceOptions());

        newElementOptions.setInitialStatus(initialStatus);

        return openMetadataClient.createMetadataElementInStore(connectorUserId,
                                                               metadataElementTypeName,
                                                               newElementOptions,
                                                               null,
                                                               properties,
                                                               null);
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
        NewElementOptions    newElementOptions    = new NewElementOptions(getMetadataSourceOptions());

        newElementOptions.setInitialStatus(initialStatus);

        NewElementProperties newElementProperties = new NewElementProperties(properties);
        newElementProperties.setEffectiveFrom(effectiveFrom);
        newElementProperties.setEffectiveTo(effectiveTo);

        return openMetadataClient.createMetadataElementInStore(connectorUserId,
                                                               metadataElementTypeName,
                                                               newElementOptions,
                                                               null,
                                                               newElementProperties,
                                                               null);
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
    public String createMetadataElementInStore(String                            metadataElementTypeName,
                                               ElementStatus                     initialStatus,
                                               Map<String, NewElementProperties> initialClassifications,
                                               String                            anchorGUID,
                                               boolean                           isOwnAnchor,
                                               String                            anchorScopeGUID,
                                               NewElementProperties              properties,
                                               String                            parentGUID,
                                               String                            parentRelationshipTypeName,
                                               NewElementProperties              parentRelationshipProperties,
                                               boolean                           parentAtEnd1) throws InvalidParameterException,
                                                                                                      UserNotAuthorizedException,
                                                                                                      PropertyServerException
    {
        NewElementOptions newElementOptions = new NewElementOptions(this.getMetadataSourceOptions());

        newElementOptions.setInitialStatus(initialStatus);
        newElementOptions.setAnchorGUID(anchorGUID);
        newElementOptions.setIsOwnAnchor(isOwnAnchor);
        newElementOptions.setAnchorScopeGUID(anchorScopeGUID);
        newElementOptions.setParentGUID(parentGUID);
        newElementOptions.setParentAtEnd1(parentAtEnd1);
        newElementOptions.setParentRelationshipTypeName(parentRelationshipTypeName);

        return this.createMetadataElementInStore(metadataElementTypeName,
                                                 newElementOptions,
                                                 initialClassifications,
                                                 properties,
                                                 parentRelationshipProperties);

    }


    /**
     * Create a new metadata element in the metadata store.  The type name comes from the open metadata types.
     * The selected type also controls the names and types of the properties that are allowed.
     * This version of the method allows access to advanced features such as multiple states and
     * effectivity dates.
     *
     * @param metadataElementTypeName type name of new element
     * @param newElementOptions details of the element to create
     * @param initialClassifications map of classification names to classification properties to include in the entity creation request
     * @param properties properties of the new metadata element
     * @param parentRelationshipProperties properties to include in parent relationship
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException the type name, status or one of the properties is invalid
     * @throws UserNotAuthorizedException the governance action service is not authorized to create this type of element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public String createMetadataElementInStore(String                            metadataElementTypeName,
                                               NewElementOptions                 newElementOptions,
                                               Map<String, NewElementProperties> initialClassifications,
                                               NewElementProperties              properties,
                                               NewElementProperties              parentRelationshipProperties) throws InvalidParameterException,
                                                                                                                      UserNotAuthorizedException,
                                                                                                                      PropertyServerException
    {
        String metadataElementGUID = openMetadataClient.createMetadataElementInStore(connectorUserId,
                                                                                     metadataElementTypeName,
                                                                                     newElementOptions,
                                                                                     initialClassifications,
                                                                                     properties,
                                                                                     parentRelationshipProperties);

        if ((metadataElementGUID != null) && (parentContext.getIntegrationReportWriter() != null))
        {
            parentContext.getIntegrationReportWriter().reportElementCreation(metadataElementGUID);
        }

        return metadataElementGUID;
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
        TemplateOptions templateOptions = new TemplateOptions(this.getMetadataSourceOptions());

        templateOptions.setAnchorGUID(anchorGUID);
        templateOptions.setIsOwnAnchor(isOwnAnchor);
        templateOptions.setAnchorScopeGUID(anchorScopeGUID);
        templateOptions.setAllowRetrieve(false);
        templateOptions.setParentGUID(parentGUID);
        templateOptions.setParentAtEnd1(parentAtEnd1);
        templateOptions.setParentRelationshipTypeName(parentRelationshipTypeName);

        return this.createMetadataElementFromTemplate(metadataElementTypeName,
                                                      templateOptions,
                                                      templateGUID,
                                                      replacementProperties,
                                                      placeholderProperties,
                                                      this.getNewElementProperties(effectiveFrom,
                                                                                   effectiveTo,
                                                                                   parentRelationshipProperties));
    }


    /**
     * Create a new metadata element in the metadata store using the template identified by the templateGUID.
     * The type name comes from the open metadata types.
     * The selected type also controls the names and types of the properties that are allowed.
     * The template and any similar anchored objects are
     * copied in this process.
     *
     * @param metadataElementTypeName expected type name of the new metadata element
     * @param templateOptions details of the element to create
     * @param templateGUID the unique identifier of the existing asset to copy (this will copy all the attachments such as nested content, schema
     *                     connection etc)
     * @param replacementProperties properties of the new metadata element.  These override the template values
     * @param placeholderProperties property name-to-property value map to replace any placeholder values in the
     *                              template element - and their anchored elements, which are also copied as part of this operation.
     * @param parentRelationshipProperties properties to include in parent relationship
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException the type name, status or one of the properties is invalid
     * @throws UserNotAuthorizedException the governance action service is not authorized to create this type of element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public String createMetadataElementFromTemplate(String               metadataElementTypeName,
                                                    TemplateOptions      templateOptions,
                                                    String               templateGUID,
                                                    ElementProperties    replacementProperties,
                                                    Map<String, String>  placeholderProperties,
                                                    NewElementProperties parentRelationshipProperties) throws InvalidParameterException,
                                                                                                              UserNotAuthorizedException,
                                                                                                              PropertyServerException
    {
        String metadataElementGUID = openMetadataClient.createMetadataElementFromTemplate(connectorUserId,
                                                                                          metadataElementTypeName,
                                                                                          templateOptions,
                                                                                          templateGUID,
                                                                                          replacementProperties,
                                                                                          placeholderProperties,
                                                                                          parentRelationshipProperties);

        if ((metadataElementGUID != null) && (parentContext.getIntegrationReportWriter() != null))
        {
            parentContext.getIntegrationReportWriter().reportElementCreation(metadataElementGUID);
        }

        return metadataElementGUID;
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
        TemplateOptions templateOptions = new TemplateOptions(this.getMetadataSourceOptions());

        templateOptions.setAnchorGUID(anchorGUID);
        templateOptions.setIsOwnAnchor(isOwnAnchor);
        templateOptions.setAnchorScopeGUID(anchorScopeGUID);
        templateOptions.setAllowRetrieve(true);
        templateOptions.setParentGUID(parentGUID);
        templateOptions.setParentAtEnd1(parentAtEnd1);
        templateOptions.setParentRelationshipTypeName(parentRelationshipTypeName);

        return this.createMetadataElementFromTemplate(metadataElementTypeName,
                                                      templateOptions,
                                                      templateGUID,
                                                      replacementProperties,
                                                      placeholderProperties,
                                                      this.getNewElementProperties(effectiveFrom,
                                                                                   effectiveTo,
                                                                                   parentRelationshipProperties));
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
        this.updateMetadataElementInStore(metadataElementGUID, this.getUpdateOptions(!replaceProperties), properties);
    }


    /**
     * Update the properties of a specific metadata element.  The properties must match the type definition associated with the
     * metadata element when it was created.  However, it is possible to update a few properties, or replace all them by
     * the value used in the replaceProperties flag.
     *
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties new properties for the metadata element
     *
     * @throws InvalidParameterException either the unique identifier or the properties are invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void updateMetadataElementInStore(String            metadataElementGUID,
                                             UpdateOptions     updateOptions,
                                             ElementProperties properties) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        openMetadataClient.updateMetadataElementInStore(connectorUserId,
                                                        metadataElementGUID,
                                                        updateOptions,
                                                        properties);


        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementUpdate(metadataElementGUID);
        }
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
        this.updateMetadataElementStatusInStore(metadataElementGUID,
                                                this.getMetadataSourceOptions(),
                                                newElementStatus);
    }


    /**
     * Update the status of specific metadata element. The new status must match a status value that is defined for the element's type
     * assigned when it was created.
     *
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param metadataSourceOptions  options to control access to open metadata
     * @param newElementStatus new status value - or null to leave as is
     *
     * @throws InvalidParameterException either the unique identifier or the status are invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void updateMetadataElementStatusInStore(String                metadataElementGUID,
                                                   MetadataSourceOptions metadataSourceOptions,
                                                   ElementStatus         newElementStatus) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        openMetadataClient.updateMetadataElementStatusInStore(connectorUserId,
                                                              metadataElementGUID,
                                                              metadataSourceOptions,
                                                              newElementStatus);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementUpdate(metadataElementGUID);
        }
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
    public void updateMetadataElementEffectivityInStore(String metadataElementGUID,
                                                        Date   effectiveFrom,
                                                        Date   effectiveTo) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        this.updateMetadataElementEffectivityInStore(metadataElementGUID,
                                                     this.getMetadataSourceOptions(),
                                                     effectiveFrom,
                                                     effectiveTo);
    }


    /**
     * Update the effectivity dates control the visibility of the element through specific APIs.
     *
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param metadataSourceOptions  options to control access to open metadata
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     *
     * @throws InvalidParameterException either the unique identifier or the status are invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void updateMetadataElementEffectivityInStore(String                metadataElementGUID,
                                                        MetadataSourceOptions metadataSourceOptions,
                                                        Date                  effectiveFrom,
                                                        Date                  effectiveTo) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        openMetadataClient.updateMetadataElementEffectivityInStore(connectorUserId,
                                                                   metadataElementGUID,
                                                                   metadataSourceOptions,
                                                                   effectiveFrom,
                                                                   effectiveTo);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementUpdate(metadataElementGUID);
        }
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


        this.deleteMetadataElementInStore(metadataElementGUID, getDeleteOptions(cascadedDelete));
    }


    /**
     * Delete a specific metadata element.
     *
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param deleteOptions options for a delete request
     *
     * @throws InvalidParameterException the unique identifier is null or invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to delete this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void deleteMetadataElementInStore(String        metadataElementGUID,
                                             DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        openMetadataClient.deleteMetadataElementInStore(connectorUserId,
                                                        metadataElementGUID,
                                                        deleteOptions);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementDelete(metadataElementGUID);
        }
    }


    /**
     * Archive a specific metadata element.
     *
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param deleteOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException the unique identifier is null or invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to delete this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void archiveMetadataElementInStore(String        metadataElementGUID,
                                              DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        openMetadataClient.archiveMetadataElementInStore(connectorUserId, metadataElementGUID, deleteOptions);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementDelete(metadataElementGUID);
        }
    }


    /**
     * Add a new classification to the metadata element.  Note that only one classification with the same name can be attached to
     * a metadata element.
     *
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param classificationName name of the classification to add (if the classification is already present then use reclassify)
     * @param metadataSourceOptions  options to control access to open metadata
     * @param properties properties to store in the new classification.  These must conform to the valid properties associated with the
     *                   classification name
     *
     * @throws InvalidParameterException the unique identifier or classification name is null or invalid in some way; properties do not match the
     *                                   valid properties associated with the classification's type definition
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void classifyMetadataElementInStore(String                metadataElementGUID,
                                               String                classificationName,
                                               MetadataSourceOptions metadataSourceOptions,
                                               NewElementProperties  properties) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        openMetadataClient.classifyMetadataElementInStore(connectorUserId,
                                                          metadataElementGUID,
                                                          classificationName,
                                                          metadataSourceOptions,
                                                          properties);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementUpdate(metadataElementGUID);
        }
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
        this.reclassifyMetadataElementInStore(metadataElementGUID,
                                              classificationName,
                                              getUpdateOptions(!replaceProperties),
                                              properties);
    }


    /**
     * Update the properties of a classification that is currently attached to a specific metadata element.
     *
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param classificationName unique name of the classification to update
     * @param updateOptions provides a structure for the additional options when updating a classification.
     * @param properties new properties for the classification
     *
     * @throws InvalidParameterException the unique identifier or classification name is null or invalid in some way; properties do not match the
     *                                   valid properties associated with the classification's type definition
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element/classification
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void reclassifyMetadataElementInStore(String            metadataElementGUID,
                                                 String            classificationName,
                                                 UpdateOptions     updateOptions,
                                                 ElementProperties properties) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        openMetadataClient.reclassifyMetadataElementInStore(connectorUserId,
                                                            metadataElementGUID,
                                                            classificationName,
                                                            updateOptions,
                                                            properties);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementUpdate(metadataElementGUID);
        }
    }


    /**
     * Update the effectivity dates of a specific classification attached to a metadata element.
     * The effectivity dates control the visibility of the classification through specific APIs.
     *
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param classificationName unique name of the classification to update
     * @param metadataSourceOptions  options to control access to open metadata
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     *
     * @throws InvalidParameterException either the unique identifier or the status are invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void updateClassificationEffectivityInStore(String                metadataElementGUID,
                                                       String                classificationName,
                                                       MetadataSourceOptions metadataSourceOptions,
                                                       Date                  effectiveFrom,
                                                       Date                  effectiveTo) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        openMetadataClient.updateClassificationEffectivityInStore(connectorUserId,
                                                                  metadataElementGUID,
                                                                  classificationName,
                                                                  metadataSourceOptions,
                                                                  effectiveFrom,
                                                                  effectiveTo);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementUpdate(metadataElementGUID);
        }
    }


    /**
     * Remove the named classification from a specific metadata element.
     *
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param classificationName unique name of the classification to remove
     * @param metadataSourceOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException the unique identifier or classification name is null or invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to remove this classification
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void declassifyMetadataElementInStore(String                metadataElementGUID,
                                                 String                classificationName,
                                                 MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                                     UserNotAuthorizedException,
                                                                                                     PropertyServerException
    {
        openMetadataClient.declassifyMetadataElementInStore(connectorUserId,
                                                            metadataElementGUID,
                                                            classificationName,
                                                            metadataSourceOptions);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementUpdate(metadataElementGUID);
        }
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
        return this.createRelatedElementsInStore(relationshipTypeName,
                                                 metadataElement1GUID,
                                                 metadataElement2GUID,
                                                 this.getMetadataSourceOptions(),
                                                 this.getNewElementProperties(effectiveFrom,
                                                                              effectiveTo,
                                                                              properties));
    }


    /**
     * Create a relationship between two metadata elements.  It is important to put the right element at each end of the relationship
     * according to the type definition since this will affect how the relationship is interpreted.
     *
     * @param relationshipTypeName name of the type of relationship to create.  This will determine the types of metadata elements that can be
     *                             related and the properties that can be associated with this relationship.
     * @param metadataElement1GUID unique identifier of the metadata element at end 1 of the relationship
     * @param metadataElement2GUID unique identifier of the metadata element at end 2 of the relationship
     * @param metadataSourceOptions  options to control access to open metadata
     * @param properties the properties of the relationship
     *
     * @return unique identifier of the new relationship
     *
     * @throws InvalidParameterException the unique identifier's of the metadata elements are null or invalid in some way; the properties are
     *                                    not valid for this type of relationship
     * @throws UserNotAuthorizedException the governance action service is not authorized to create this type of relationship
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public String createRelatedElementsInStore(String                relationshipTypeName,
                                               String                metadataElement1GUID,
                                               String                metadataElement2GUID,
                                               MetadataSourceOptions metadataSourceOptions,
                                               NewElementProperties  properties) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        String relationshipGUID = openMetadataClient.createRelatedElementsInStore(connectorUserId,
                                                                                  relationshipTypeName,
                                                                                  metadataElement1GUID,
                                                                                  metadataElement2GUID,
                                                                                  metadataSourceOptions,
                                                                                  properties);

        if ((relationshipGUID != null) && (parentContext.getIntegrationReportWriter() != null))
        {
            parentContext.getIntegrationReportWriter().reportElementCreation(relationshipGUID);
        }

        return relationshipGUID;
    }


    /**
     * Update the properties associated with a relationship.
     *
     * @param relationshipGUID unique identifier of the relationship to update
     * @param updateOptions provides a structure for the additional options when updating a relationship.
     * @param properties new properties for the relationship
     *
     * @throws InvalidParameterException the unique identifier of the relationship is null or invalid in some way; the properties are
     *                                    not valid for this type of relationship
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this relationship
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void updateRelatedElementsInStore(String            relationshipGUID,
                                             UpdateOptions     updateOptions,
                                             ElementProperties properties) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        openMetadataClient.updateRelationshipInStore(connectorUserId,
                                                     relationshipGUID,
                                                     updateOptions,
                                                     properties);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementUpdate(relationshipGUID);
        }
    }


    /**
     * Update the effectivity dates of a specific relationship between metadata elements.
     * The effectivity dates control the visibility of the classification through specific APIs.
     *
     * @param relationshipGUID unique identifier of the relationship to update
     * @param metadataSourceOptions  options to control access to open metadata
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     *
     * @throws InvalidParameterException either the unique identifier or the status are invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void updateRelatedElementsEffectivityInStore(String                relationshipGUID,
                                                        MetadataSourceOptions metadataSourceOptions,
                                                        Date                  effectiveFrom,
                                                        Date                  effectiveTo) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        openMetadataClient.updateRelationshipEffectivityInStore(connectorUserId,
                                                                relationshipGUID,
                                                                metadataSourceOptions,
                                                                effectiveFrom,
                                                                effectiveTo);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementUpdate(relationshipGUID);
        }
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
    public void deleteRelationshipInStore(String  relationshipGUID) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        this.deleteRelationshipInStore(relationshipGUID, this.getDeleteOptions(false));
    }


    /**
     * Delete a relationship between two metadata elements.
     *
     * @param relationshipGUID unique identifier of the relationship to delete
     * @param deleteOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException the unique identifier of the relationship is null or invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to delete this relationship
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void deleteRelationshipInStore(String        relationshipGUID,
                                          DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        openMetadataClient.deleteRelationshipInStore(connectorUserId,
                                                     relationshipGUID,
                                                     deleteOptions);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementDelete(relationshipGUID);
        }
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
        openMetadataClient.addExternalIdentifier(connectorUserId, externalScopeGUID, externalScopeName, externalScopeTypeName, openMetadataElementGUID, openMetadataElementTypeName, externalIdentifierProperties, null, null, forLineage, forDuplicateProcessing, getEffectiveTime());
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
        openMetadataClient.confirmSynchronization(connectorUserId, externalScopeGUID, externalScopeName, openMetadataElementGUID, openMetadataElementTypeName, externalIdentifier);
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
        return openMetadataClient.getExternalIdentifiers(connectorUserId, externalScopeGUID, externalScopeName, openMetadataElementGUID, openMetadataElementTypeName, startFrom, pageSize, forLineage, forDuplicateProcessing, getEffectiveTime());
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
        return openMetadataClient.getVendorProperties(connectorUserId, openMetadataElementGUID, openMetadataElementTypeName, forLineage, forDuplicateProcessing, getEffectiveTime());
    }
}