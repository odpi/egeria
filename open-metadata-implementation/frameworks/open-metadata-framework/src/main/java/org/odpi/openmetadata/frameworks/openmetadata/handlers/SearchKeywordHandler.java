/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.handlers;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ElementStatus;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.feedback.SearchKeywordProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Collections;
import java.util.List;
import java.util.Map;


/**
 * SearchKeywordHandler is the handler for managing search keywords.  These elements are anchored to their
 * parent entity so that they are included in an anchor based search.
 */
public class SearchKeywordHandler extends OpenMetadataHandlerBase
{
    /**
     * Create a new handler.
     *
     * @param localServerName        name of this server (view server)
     * @param auditLog               logging destination
     * @param serviceName            local service name
     * @param openMetadataClient     access to open metadata
     */
    public SearchKeywordHandler(String             localServerName,
                                AuditLog           auditLog,
                                String             serviceName,
                                OpenMetadataClient openMetadataClient)
    {
        super(localServerName, auditLog, serviceName, openMetadataClient, OpenMetadataType.SEARCH_KEYWORD.typeName);
    }


    /**
     * Adds a search keyword to the element.
     *
     * @param userId        userId of user making request
     * @param elementGUID     unique identifier for the element
     * @param metadataSourceOptions options for the request
     * @param initialClassifications map of classification names to classification properties to include in the entity creation request
     * @param properties   properties of the search keyword
     *
     * @return guid of new search keyword.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the element properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String addSearchKeywordToElement(String                                userId,
                                            String                                elementGUID,
                                            MetadataSourceOptions                 metadataSourceOptions,
                                            Map<String, ClassificationProperties> initialClassifications,
                                            SearchKeywordProperties                     properties) throws InvalidParameterException,
                                                                                                           PropertyServerException,
                                                                                                           UserNotAuthorizedException
    {
        final String methodName = "addSearchKeywordToElement";
        final String propertiesParameterName   = "properties";
        final String searchKeywordQNameParameterName = "properties.keyword";

        propertyHelper.validateObject(properties, propertiesParameterName, methodName);
        propertyHelper.validateMandatoryName(properties.getDisplayName(), searchKeywordQNameParameterName, methodName);

        NewElementOptions newElementOptions = new NewElementOptions(metadataSourceOptions);

        newElementOptions.setInitialStatus(ElementStatus.ACTIVE);
        newElementOptions.setAnchorGUID(elementGUID);
        newElementOptions.setIsOwnAnchor(false);
        newElementOptions.setParentGUID(elementGUID);
        newElementOptions.setParentRelationshipTypeName(OpenMetadataType.SEARCH_KEYWORD_LINK_RELATIONSHIP.typeName);
        newElementOptions.setParentAtEnd1(true);

        return openMetadataClient.createMetadataElementInStore(userId,
                                                               OpenMetadataType.SEARCH_KEYWORD.typeName,
                                                               newElementOptions,
                                                               classificationBuilder.getInitialClassifications(initialClassifications),
                                                               elementBuilder.getNewElementProperties(properties),
                                                               null);
    }


    /**
     * Update an existing search keyword.
     *
     * @param userId        userId of user making request.
     * @param searchKeywordGUID   unique identifier for the search keyword to change.
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties   properties of the searchKeyword
     *
     * @return boolean - true if an update occurred
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the element properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public boolean updateSearchKeyword(String                  userId,
                                       String                  searchKeywordGUID,
                                       UpdateOptions           updateOptions,
                                       SearchKeywordProperties properties) throws InvalidParameterException,
                                                                                  PropertyServerException,
                                                                                  UserNotAuthorizedException
    {
        final String methodName = "updateSearchKeyword";
        final String propertiesParameterName   = "properties";

        propertyHelper.validateObject(properties, propertiesParameterName, methodName);

        return openMetadataClient.updateMetadataElementInStore(userId, searchKeywordGUID, updateOptions, elementBuilder.getNewElementProperties(properties));
    }


    /**
     * Removes a search keyword added to the element by this user.
     *
     * @param userId       userId of user making request.
     * @param searchKeywordGUID  unique identifier for the search keyword object.
     * @param deleteOptions options for a delete request
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the element properties in the property server.
     * @throws UserNotAuthorizedException the user does not have permission to perform this request.
     */
    public void deleteSearchKeyword(String        userId,
                                    String        searchKeywordGUID,
                                    DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException
    {
        openMetadataClient.deleteMetadataElementInStore(userId, searchKeywordGUID, deleteOptions);
    }


    /**
     * Return the requested search keyword.
     *
     * @param userId       userId of user making request.
     * @param searchKeywordGUID  unique identifier for the search keyword object.
     * @param getOptions multiple options to control the query
     * @return search keyword properties
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the element properties in the property server.
     * @throws UserNotAuthorizedException the user does not have permission to perform this request.
     */
    public OpenMetadataRootElement getSearchKeywordByGUID(String     userId,
                                                          String     searchKeywordGUID,
                                                          GetOptions getOptions) throws InvalidParameterException,
                                                                                        PropertyServerException,
                                                                                        UserNotAuthorizedException
    {
        final String methodName = "getSearchKeywordByGUID";

        return super.getRootElementByGUID(userId, searchKeywordGUID, getOptions, methodName);
    }


    /**
     * Return the list of keywords exactly matching the supplied name.
     *
     * @param userId the name of the calling user
     * @param name name to search for
     * @param queryOptions multiple options to control the query
     *
     * @return name list
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getSearchKeywordsByName(String       userId,
                                                                 String       name,
                                                                 QueryOptions queryOptions) throws InvalidParameterException,
                                                                                             PropertyServerException,
                                                                                             UserNotAuthorizedException
    {
        final String methodName = "getKeywordsByName";
        final String nameParameterName = "name";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateMandatoryName(name, nameParameterName, methodName);
        propertyHelper.validatePaging(queryOptions, openMetadataClient.getMaxPagingSize(), methodName);

        List<String> propertyNames = Collections.singletonList(OpenMetadataProperty.DISPLAY_NAME.name);

        return super.getRootElementsByName(userId,
                                           name,
                                           propertyNames,
                                           queryOptions,
                                           methodName);
    }



    /**
     * Retrieve the list of search keyword metadata elements that contain the search string.
     *
     * @param userId calling user
     * @param searchString string to find in the properties
     * @param searchOptions multiple options to control the query
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> findSearchKeywords(String        userId,
                                                            String        searchString,
                                                            SearchOptions searchOptions) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException
    {
        final String methodName = "findSearchKeywords";

        return super.findRootElements(userId, searchString, searchOptions,methodName);
    }
}
