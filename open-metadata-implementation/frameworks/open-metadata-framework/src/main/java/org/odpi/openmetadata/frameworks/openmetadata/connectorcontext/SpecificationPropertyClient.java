/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.connectorcontext;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.SpecificationPropertyHandler;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.SpecificationPropertyType;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.SpecificationProperty;

import java.util.List;

/**
 * Provides services for connectors to work with specification property elements.
 */
public class SpecificationPropertyClient extends ConnectorContextClientBase
{
    private final SpecificationPropertyHandler specificationPropertyHandler;


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
    public SpecificationPropertyClient(ConnectorContextBase     parentContext,
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

        this.specificationPropertyHandler = new SpecificationPropertyHandler(localServerName, auditLog, localServiceName, openMetadataClient);
    }


    /**
     * Retrieve/create/update the metadata element for a valid metadata value.
     *
     * @param elementGUID unique identifier of the element to connect it to
     * @param specificationProperty the property description
     * @param metadataSourceOptions query options
     *
     * @return unique identifier of new specification property
     * @throws InvalidParameterException  the property name is null or not known.
     * @throws UserNotAuthorizedException the service is not able to create/access the element
     * @throws PropertyServerException    a problem accessing the metadata store
     */
    private String setUpSpecificationProperty(String                    elementGUID,
                                              SpecificationProperty     specificationProperty,
                                              MetadataSourceOptions     metadataSourceOptions) throws InvalidParameterException,
                                                                                                      PropertyServerException,
                                                                                                      UserNotAuthorizedException
    {
        return specificationPropertyHandler.setUpSpecificationProperty(connectorUserId, elementGUID, specificationProperty, metadataSourceOptions);
    }


    /**
     * Remove a specification property.
     *
     * @param specificationPropertyGUID unique identifier of the specification property element to remove
     * @param deleteOptions options to control the delete operation
     *
     * @throws InvalidParameterException  the property name is null or not known.
     * @throws UserNotAuthorizedException the service is not able to create/access the element
     * @throws PropertyServerException    a problem accessing the metadata store
     */
    public void deleteSpecificationPropertyName(String        specificationPropertyGUID,
                                                DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        specificationPropertyHandler.deleteSpecificationProperty(connectorUserId, specificationPropertyGUID, deleteOptions);
    }


    /**
     * Return the requested specification property.
     *
     * @param specificationPropertyGUID  unique identifier for the specification property object.
     * @param getOptions multiple options to control the query
     * @return specification property properties
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException a problem updating the element properties in the property server.
     * @throws UserNotAuthorizedException the user does not have permission to perform this request.
     */
    public OpenMetadataRootElement getSpecificationPropertyByGUID(String     specificationPropertyGUID,
                                                                  GetOptions getOptions) throws InvalidParameterException,
                                                                                                PropertyServerException,
                                                                                                UserNotAuthorizedException
    {
        return specificationPropertyHandler.getSpecificationPropertyByGUID(connectorUserId, specificationPropertyGUID, getOptions);
    }


    /**
     * Return the list of keywords exactly matching the supplied specification property type.
     *
     * @param specificationPropertyType name of specificationPropertyType.
     * @param queryOptions multiple options to control the query
     *
     * @return specificationPropertyType list
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getSpecificationPropertiesByType(SpecificationPropertyType specificationPropertyType,
                                                                          QueryOptions              queryOptions) throws InvalidParameterException,
                                                                                                                         PropertyServerException,
                                                                                                                         UserNotAuthorizedException
    {
        return specificationPropertyHandler.getSpecificationPropertiesByType(connectorUserId, specificationPropertyType, queryOptions);
    }

    /**
     * Return the list of keywords exactly matching the supplied specification property type.
     *
     * @param name name of name.
     * @param queryOptions multiple options to control the query
     *
     * @return name list
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getSpecificationPropertiesByName(String       name,
                                                                          QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                            PropertyServerException,
                                                                                                            UserNotAuthorizedException
    {
        return specificationPropertyHandler.getSpecificationPropertiesByName(connectorUserId, name, queryOptions);
    }



    /**
     * Retrieve the list of specification property metadata elements that contain the search string.
     *
     * @param searchString string to find in the properties
     * @param searchOptions multiple options to control the query
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> findSpecificationProperties(String        searchString,
                                                                     SearchOptions searchOptions) throws InvalidParameterException,
                                                                                                         UserNotAuthorizedException,
                                                                                                         PropertyServerException
    {
        return specificationPropertyHandler.findSpecificationProperties(connectorUserId, searchString, searchOptions);
    }
}
