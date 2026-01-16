/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.connectorcontext;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.PropertyFacetHandler;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.propertyfacets.PropertyFacetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.propertyfacets.ReferenceableFacetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;

import java.util.List;
import java.util.Map;

/**
 * Provides services for connectors to work with property facet elements.
 */
public class PropertyFacetClient extends ConnectorContextClientBase
{
    private final PropertyFacetHandler propertyFacetHandler;


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
    public PropertyFacetClient(ConnectorContextBase     parentContext,
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

        this.propertyFacetHandler = new PropertyFacetHandler(localServerName, auditLog, localServiceName, openMetadataClient);
    }


    /**
     * Adds a property facet to the element.
     *
     * @param elementGUID     unique identifier for the element
     * @param metadataSourceOptions options for the request
     * @param initialClassifications map of classification names to classification properties to include in the entity creation request
     * @param properties   properties of the property facet
     * @param relationshipProperties properties for the ReferenceableFacet relationship
     *
     * @return guid of new property facet.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException a problem adding the element properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String addPropertyFacetToElement(String                                elementGUID,
                                            MetadataSourceOptions                 metadataSourceOptions,
                                            Map<String, ClassificationProperties> initialClassifications,
                                            PropertyFacetProperties               properties,
                                            ReferenceableFacetProperties          relationshipProperties) throws InvalidParameterException,
                                                                                                                 PropertyServerException,
                                                                                                                 UserNotAuthorizedException
    {
        String propertyFacetGUID = propertyFacetHandler.addPropertyFacetToElement(connectorUserId, elementGUID, metadataSourceOptions, initialClassifications, properties, relationshipProperties);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementCreation(propertyFacetGUID);
        }

        return propertyFacetGUID;
    }


    /**
     * Update an existing property facet.
     *
     * @param propertyFacetGUID   unique identifier for the property facet to change.
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties   properties of the comment
     *
     * @return boolean - true if an update occurred
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException a problem adding the element properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public boolean updatePropertyFacet(String                  propertyFacetGUID,
                                       UpdateOptions           updateOptions,
                                       PropertyFacetProperties properties) throws InvalidParameterException,
                                                                                  PropertyServerException,
                                                                                  UserNotAuthorizedException
    {
        boolean updateOccurred = propertyFacetHandler.updatePropertyFacet(connectorUserId, propertyFacetGUID, updateOptions, properties);

        if ((updateOccurred) && (parentContext.getIntegrationReportWriter() != null))
        {
            parentContext.getIntegrationReportWriter().reportElementUpdate(propertyFacetGUID);
        }

        return updateOccurred;
    }


    /**
     * Removes a property facet added to the element.
     *
     * @param propertyFacetGUID  unique identifier for the property facet object.
     * @param deleteOptions options for a delete request
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException a problem updating the element properties in the property server.
     * @throws UserNotAuthorizedException the user does not have permission to perform this request.
     */
    public void deletePropertyFacet(String        propertyFacetGUID,
                                    DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException
    {
        propertyFacetHandler.deletePropertyFacet(connectorUserId, propertyFacetGUID, deleteOptions);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementDelete(propertyFacetGUID);
        }
    }


    /**
     * Return the requested property facet.
     *
     * @param propertyFacetGUID  unique identifier for the property facet object.
     * @param getOptions multiple options to control the query
     * @return property facet properties
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException a problem updating the element properties in the property server.
     * @throws UserNotAuthorizedException the user does not have permission to perform this request.
     */
    public OpenMetadataRootElement getPropertyFacetByGUID(String     propertyFacetGUID,
                                                          GetOptions getOptions) throws InvalidParameterException,
                                                                                        PropertyServerException,
                                                                                        UserNotAuthorizedException
    {
        return propertyFacetHandler.getPropertyFacetByGUID(connectorUserId, propertyFacetGUID, getOptions);
    }


    /**
     * Retrieve the list of property facet metadata elements that contain the requested keyword.
     *
     * @param name name to find
     * @param queryOptions multiple options to control the query
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> getPropertyFacetsByName(String        name,
                                                                 QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        return propertyFacetHandler.getPropertyFacetsByName(connectorUserId, name, queryOptions);
    }


    /**
     * Retrieve the list of property facet metadata elements that contain the search string.
     *
     * @param searchString string to find in the properties
     * @param searchOptions multiple options to control the query
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> findPropertyFacets(String        searchString,
                                                            SearchOptions searchOptions) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException
    {
        return propertyFacetHandler.findPropertyFacets(connectorUserId, searchString, searchOptions);
    }
}
