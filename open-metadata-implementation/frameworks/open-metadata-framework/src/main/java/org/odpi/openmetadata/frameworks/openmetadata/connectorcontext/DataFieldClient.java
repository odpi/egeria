/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.connectorcontext;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.DataFieldHandler;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.DataFieldProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.MemberDataFieldProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.List;
import java.util.Map;

/**
 * Provides services for connectors to work with data field elements.
 */
public class DataFieldClient extends ConnectorContextClientBase
{
    private final DataFieldHandler dataFieldHandler;


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
    public DataFieldClient(ConnectorContextBase     parentContext,
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

        this.dataFieldHandler = new DataFieldHandler(localServerName, auditLog, localServiceName, openMetadataClient);
    }


    /**
     * Create a new data field.
     *
     * @param newElementOptions details of the element to create
     * @param initialClassifications map of classification names to classification properties to include in the entity creation request
     * @param properties                   properties for the new element.
     * @param parentRelationshipProperties properties to include in parent relationship
     * @return unique identifier of the newly created element
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String createDataField(NewElementOptions                     newElementOptions,
                                  Map<String, ClassificationProperties> initialClassifications,
                                  DataFieldProperties                    properties,
                                  RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                             PropertyServerException,
                                                                                                             UserNotAuthorizedException
    {
        String elementGUID = dataFieldHandler.createDataField(connectorUserId, newElementOptions, initialClassifications, properties, parentRelationshipProperties);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementCreation(elementGUID);
        }

        return elementGUID;
    }


    /**
     * Create a new metadata element to represent a dataField using an existing element as a template.
     * The template defines additional classifications and relationships that should be added to the new dataField.
     *
     * @param templateOptions details of the element to create
     * @param templateGUID the unique identifier of the existing dataField to copy (this will copy all the attachments such as nested content, schema
     *                     connection etc)
     * @param replacementProperties properties of the new metadata element.  These override the template values
     * @param placeholderProperties property name-to-property value map to replace any placeholder values in the
     *                              template element - and their anchored elements, which are also copied as part of this operation.
     * @param parentRelationshipProperties properties to include in parent relationship
     * @return unique identifier of the new metadata element
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createDataFieldFromTemplate(TemplateOptions        templateOptions,
                                              String                 templateGUID,
                                              ElementProperties      replacementProperties,
                                              Map<String, String>    placeholderProperties,
                                              RelationshipProperties parentRelationshipProperties) throws InvalidParameterException,
                                                                                                          UserNotAuthorizedException,
                                                                                                          PropertyServerException
    {
        String elementGUID = dataFieldHandler.createDataFieldFromTemplate(connectorUserId, templateOptions, templateGUID, replacementProperties, placeholderProperties, parentRelationshipProperties);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementCreation(elementGUID);
        }

        return elementGUID;
    }


    /**
     * Update the properties of a data field.
     *
     * @param dataFieldGUID       unique identifier of the dataField (returned from create)
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties             properties for the element.
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void updateDataField(String             dataFieldGUID,
                                UpdateOptions      updateOptions,
                                DataFieldProperties properties) throws InvalidParameterException,
                                                                       PropertyServerException,
                                                                       UserNotAuthorizedException
    {
        dataFieldHandler.updateDataField(connectorUserId, dataFieldGUID, updateOptions, properties);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementUpdate(dataFieldGUID);
        }
    }


    /**
     * Connect two data fields as parent and child.
     *
     * @param parentDataFieldGUID    unique identifier of the parent data field
     * @param nestedDataFieldGUID    unique identifier of the child data field
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkNestedDataFields(String                    parentDataFieldGUID,
                                     String                    nestedDataFieldGUID,
                                     MetadataSourceOptions     metadataSourceOptions,
                                     MemberDataFieldProperties relationshipProperties) throws InvalidParameterException,
                                                                                              PropertyServerException,
                                                                                              UserNotAuthorizedException
    {
        dataFieldHandler.linkNestedDataFields(connectorUserId, parentDataFieldGUID, nestedDataFieldGUID, metadataSourceOptions, relationshipProperties);
    }


    /**
     * Detach two data fields from one another.
     *
     * @param parentDataFieldGUID    unique identifier of the parent data field.
     * @param nestedDataFieldGUID    unique identifier of the child data field.
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachNestedDataFields(String        parentDataFieldGUID,
                                       String        nestedDataFieldGUID,
                                       DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                           PropertyServerException,
                                                                           UserNotAuthorizedException
    {
        dataFieldHandler.detachNestedDataFields(connectorUserId, parentDataFieldGUID, nestedDataFieldGUID, deleteOptions);
    }


    /**
     * Delete a data field.
     *
     * @param dataFieldGUID       unique identifier of the element
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteDataField(String        dataFieldGUID,
                                DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException
    {
        dataFieldHandler.deleteDataField(connectorUserId, dataFieldGUID, deleteOptions);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementDelete(dataFieldGUID);
        }
    }


    /**
     * Returns the list of data fields with a particular name.
     *
     * @param name                   name of the element to return - match is full text match in qualifiedName, resourceName or displayName
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getDataFieldsByName(String       name,
                                                             QueryOptions queryOptions) throws InvalidParameterException,
                                                                                               PropertyServerException,
                                                                                               UserNotAuthorizedException
    {
        return dataFieldHandler.getDataFieldsByName(connectorUserId, name, queryOptions);
    }


    /**
     * Return the properties of a specific data field.
     *
     * @param dataFieldGUID       unique identifier of the required element
     * @param getOptions multiple options to control the query
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElement getDataFieldByGUID(String     dataFieldGUID,
                                                      GetOptions getOptions) throws InvalidParameterException,
                                                                                    PropertyServerException,
                                                                                    UserNotAuthorizedException
    {
        return dataFieldHandler.getDataFieldByGUID(connectorUserId, dataFieldGUID, getOptions);
    }


    /**
     * Retrieve the list of data fields metadata elements that contain the search string and show which solution components (if any) are attached to it.
     * The returned dataFields include a list of the components that are associated with it.
     * The search string is treated as a regular expression.
     *
     * @param searchString           string to find in the properties
     * @param searchOptions multiple options to control the query
     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> findDataFields(String        searchString,
                                                        SearchOptions searchOptions) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        return dataFieldHandler.findDataFields(connectorUserId, searchString, searchOptions);
    }
}
