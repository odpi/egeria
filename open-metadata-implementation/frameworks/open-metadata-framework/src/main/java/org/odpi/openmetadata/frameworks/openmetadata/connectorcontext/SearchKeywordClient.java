/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.connectorcontext;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.SearchKeywordHandler;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.feedback.SearchKeywordProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;

import java.util.List;
import java.util.Map;

/**
 * Provides services for connectors to work with search keyword elements.
 */
public class SearchKeywordClient extends ConnectorContextClientBase
{
    private final SearchKeywordHandler searchKeywordHandler;


    /**
     * Constructor for connector context client.
     *
     * @param parentContext connector's context
     * @param localServerName local server where this client is running - used for error handling
     * @param localServiceName local service that his connector is hosted by - used for error handling
     * @param connectorUserId the userId to use with all requests for open metadata
     * @param connectorGUID the unique identifier that represents this connector in open metadata
     * @param externalSourceGUID unique identifier of the software server capability for the source of metadata
     * @param externalSourceName unique name of the software server capability for the source of metadata
     * @param openMetadataClient client to access the open metadata store
     * @param auditLog logging destination
     * @param maxPageSize max number of elements that can be returned on a query
     */
    public SearchKeywordClient(ConnectorContextBase     parentContext,
                               String                   localServerName,
                               String                   localServiceName,
                               String                   connectorUserId,
                               String                   connectorGUID,
                               String                   externalSourceGUID,
                               String                   externalSourceName,
                               OpenMetadataClient       openMetadataClient,
                               AuditLog                 auditLog,
                               int                      maxPageSize)
    {
        super(parentContext, localServerName, localServiceName, connectorUserId, connectorGUID, externalSourceGUID, externalSourceName, auditLog, maxPageSize);

        this.searchKeywordHandler = new SearchKeywordHandler(localServerName, auditLog, localServiceName, openMetadataClient);
    }


    /**
     * Adds a search keyword to the element.
     *
     * @param elementGUID     unique identifier for the element.
     * @param initialClassifications map of classification names to classification properties to include in the entity creation request
     * @param properties   properties of the comment
     *
     * @return guid of new search keyword.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the element properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String addSearchKeywordToElement(String                                elementGUID,
                                            MetadataSourceOptions                 metadataSourceOptions,
                                            Map<String, ClassificationProperties> initialClassifications,
                                            SearchKeywordProperties                     properties) throws InvalidParameterException,
                                                                                                           PropertyServerException,
                                                                                                           UserNotAuthorizedException
    {
        return searchKeywordHandler.addSearchKeywordToElement(connectorUserId, elementGUID, metadataSourceOptions, initialClassifications, properties);
    }


    /**
     * Update an existing search keyword.
     *
     * @param searchKeywordGUID   unique identifier for the search keyword to change.
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties   properties of the comment
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the element properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void   updateSearchKeyword(String                  searchKeywordGUID,
                                      UpdateOptions           updateOptions,
                                      SearchKeywordProperties properties) throws InvalidParameterException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException
    {
        searchKeywordHandler.updateSearchKeyword(connectorUserId, searchKeywordGUID, updateOptions, properties);
    }


    /**
     * Removes a search keyword added to the element.
     *
     * @param searchKeywordGUID  unique identifier for the search keyword object.
     * @param deleteOptions options for a delete request
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the element properties in the property server.
     * @throws UserNotAuthorizedException the user does not have permission to perform this request.
     */
    public void deleteSearchKeyword(String        searchKeywordGUID,
                                    DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException
    {
        searchKeywordHandler.deleteSearchKeyword(connectorUserId, searchKeywordGUID, deleteOptions);
    }


    /**
     * Return the requested search keyword.
     *
     * @param searchKeywordGUID  unique identifier for the search keyword object.
     * @param getOptions multiple options to control the query
     * @return search keyword properties
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the element properties in the property server.
     * @throws UserNotAuthorizedException the user does not have permission to perform this request.
     */
    public OpenMetadataRootElement getSearchKeywordByGUID(String     searchKeywordGUID,
                                                          GetOptions getOptions) throws InvalidParameterException,
                                                                                        PropertyServerException,
                                                                                        UserNotAuthorizedException
    {
        return searchKeywordHandler.getSearchKeywordByGUID(connectorUserId, searchKeywordGUID, getOptions);
    }


    /**
     * Retrieve the list of search keyword metadata elements that contain the requested keyword.
     *
     * @param name name to find
     * @param queryOptions multiple options to control the query
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> getSearchKeywordsByName(String        name,
                                                                 QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        return searchKeywordHandler.getSearchKeywordsByName(connectorUserId, name, queryOptions);
    }


    /**
     * Retrieve the list of search keyword metadata elements that contain the search string.
     *
     * @param searchString string to find in the properties
     * @param searchOptions multiple options to control the query
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> findSearchKeywords(String        searchString,
                                                            SearchOptions searchOptions) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException
    {
        return searchKeywordHandler.findSearchKeywords(connectorUserId, searchString, searchOptions);
    }
}
