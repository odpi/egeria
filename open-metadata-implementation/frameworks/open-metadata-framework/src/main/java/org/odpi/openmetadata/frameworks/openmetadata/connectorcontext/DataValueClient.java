/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.connectorcontext;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.DataValueHandler;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.EntityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datavalues.DataValueProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datavalues.DataValueSourceProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datavalues.EmbeddedDataValueProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datavalues.ProcessingVariableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;

import java.util.List;
import java.util.Map;

/**
 * Provides services for connectors to work with data value elements.
 */
public class DataValueClient extends ConnectorContextClientBase
{
    private final DataValueHandler dataValueHandler;


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
    public DataValueClient(ConnectorContextBase parentContext,
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

        this.dataValueHandler = new DataValueHandler(localServerName, auditLog, localServiceName, openMetadataClient);
    }


    /**
     * Create a data value.
     *
     * @param newElementOptions            details of the element to create
     * @param initialClassifications       map of classification names to classification properties to include in the entity creation request
     * @param properties                   properties for the new element
     * @param parentRelationshipProperties properties to include in parent relationship
     * @return unique identifier of the newly created element
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     */
    public String createDataValue(NewElementOptions                     newElementOptions,
                                  Map<String, ClassificationProperties> initialClassifications,
                                  DataValueProperties                   properties,
                                  RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                             PropertyServerException,
                                                                                                             UserNotAuthorizedException
    {
        return dataValueHandler.createDataValue(connectorUserId, newElementOptions, initialClassifications, properties, parentRelationshipProperties);
    }


    /**
     * Create a new metadata element to represent a data value using an existing metadata element as a template.  The
     * template defines additional classifications and relationships that should be added to the new element.
     *
     * @param templateOptions              details of the element to create
     * @param templateGUID                 the unique identifier of the existing element to copy
     * @param replacementProperties        properties of the new metadata element.  These override the template values
     * @param replacementClassifications   map of classification names to classification properties to include in the entity creation request. These override the template values
     * @param placeholderProperties        property name-to-property value map to replace any placeholder values in the template element - and their anchored elements, which are also copied as part of this operation
     * @param parentRelationshipProperties properties to include in parent relationship
     * @return unique identifier of the new metadata element
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     */
    public String createDataValueFromTemplate(TemplateOptions                       templateOptions,
                                              String                                templateGUID,
                                              EntityProperties                      replacementProperties,
                                              Map<String, ClassificationProperties> replacementClassifications,
                                              Map<String, String>                   placeholderProperties,
                                              RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                                         PropertyServerException,
                                                                                                                         UserNotAuthorizedException
    {
        return dataValueHandler.createDataValueFromTemplate(connectorUserId, templateOptions, templateGUID, replacementProperties, replacementClassifications, placeholderProperties, parentRelationshipProperties);
    }


    /**
     * Update the properties of a data value.
     *
     * @param dataValueGUID unique identifier of the data value
     * @param updateOptions provides a structure for the additional options when updating an element
     * @param properties    properties for the element
     * @return boolean - true if an update occurred
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     */
    public boolean updateDataValue(String              dataValueGUID,
                                   UpdateOptions       updateOptions,
                                   DataValueProperties properties) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        return dataValueHandler.updateDataValue(connectorUserId, dataValueGUID, updateOptions, properties);
    }


    /**
     * Delete a data value.
     *
     * @param dataValueGUID unique identifier of the data value
     * @param deleteOptions options for a delete request
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     */
    public void deleteDataValue(String        dataValueGUID,
                                DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException
    {
        dataValueHandler.deleteDataValue(connectorUserId, dataValueGUID, deleteOptions);
    }


    /**
     * Returns the list of data values with a particular name.
     *
     * @param name         name of the element to return - match is full text match in qualifiedName, identifier or displayName
     * @param queryOptions multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     */
    public List<OpenMetadataRootElement> getDataValuesByName(String       name,
                                                             QueryOptions queryOptions) throws InvalidParameterException,
                                                                                               PropertyServerException,
                                                                                               UserNotAuthorizedException
    {
        return dataValueHandler.getDataValuesByName(connectorUserId, name, queryOptions);
    }


    /**
     * Return the properties of a specific data value.
     *
     * @param dataValueGUID unique identifier of the data value
     * @param getOptions    multiple options to control the query
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     */
    public OpenMetadataRootElement getDataValueByGUID(String     dataValueGUID,
                                                      GetOptions getOptions) throws InvalidParameterException,
                                                                                    PropertyServerException,
                                                                                    UserNotAuthorizedException
    {
        return dataValueHandler.getDataValueByGUID(connectorUserId, dataValueGUID, getOptions);
    }


    /**
     * Retrieve the list of data value metadata elements that contain the search string.
     *
     * @param searchString  string to find in the properties
     * @param searchOptions multiple options to control the query
     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     */
    public List<OpenMetadataRootElement> findDataValues(String        searchString,
                                                        SearchOptions searchOptions) throws InvalidParameterException,
                                                                                            PropertyServerException,
                                                                                            UserNotAuthorizedException
    {
        return dataValueHandler.findDataValues(connectorUserId, searchString, searchOptions);
    }


    /**
     * Attach a data value to the asset that describes the digital resource where the data is stored.
     *
     * @param dataValueGUID          unique identifier of the data value
     * @param assetGUID              unique identifier of the asset for the digital resource where the data is stored
     * @param makeAnchorOptions      options to control access to open metadata
     * @param relationshipProperties description of the relationship
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     */
    public void linkDataValueSource(String                    dataValueGUID,
                                    String                    assetGUID,
                                    MakeAnchorOptions         makeAnchorOptions,
                                    DataValueSourceProperties relationshipProperties) throws InvalidParameterException,
                                                                                             PropertyServerException,
                                                                                             UserNotAuthorizedException
    {
        dataValueHandler.linkDataValueSource(connectorUserId, dataValueGUID, assetGUID, makeAnchorOptions, relationshipProperties);
    }


    /**
     * Detach a data value from the asset that describes the digital resource where the data is stored.
     *
     * @param dataValueGUID unique identifier of the data value
     * @param assetGUID     unique identifier of the asset for the digital resource where the data is stored
     * @param deleteOptions options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     */
    public void detachDataValueSource(String        dataValueGUID,
                                      String        assetGUID,
                                      DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        dataValueHandler.detachDataValueSource(connectorUserId, dataValueGUID, assetGUID, deleteOptions);
    }


    /**
     * Attach an element (such as a process) to a data value that is used to configure its processing.
     *
     * @param elementGUID            unique identifier of the element that uses the data value
     * @param dataValueGUID          unique identifier of the data value
     * @param makeAnchorOptions      options to control access to open metadata
     * @param relationshipProperties description of the relationship
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     */
    public void linkProcessingVariable(String                       elementGUID,
                                       String                       dataValueGUID,
                                       MakeAnchorOptions            makeAnchorOptions,
                                       ProcessingVariableProperties relationshipProperties) throws InvalidParameterException,
                                                                                                   PropertyServerException,
                                                                                                   UserNotAuthorizedException
    {
        dataValueHandler.linkProcessingVariable(connectorUserId, elementGUID, dataValueGUID, makeAnchorOptions, relationshipProperties);
    }


    /**
     * Detach an element (such as a process) from a data value that is used to configure its processing.
     *
     * @param elementGUID   unique identifier of the element that uses the data value
     * @param dataValueGUID unique identifier of the data value
     * @param deleteOptions options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     */
    public void detachProcessingVariable(String        elementGUID,
                                         String        dataValueGUID,
                                         DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        dataValueHandler.detachProcessingVariable(connectorUserId, elementGUID, dataValueGUID, deleteOptions);
    }


    /**
     * Embed a data value in another data value.
     *
     * @param parentDataValueGUID    unique identifier of the parent data value
     * @param embeddedDataValueGUID  unique identifier of the embedded data value
     * @param makeAnchorOptions      options to control access to open metadata
     * @param relationshipProperties description of the relationship
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     */
    public void linkEmbeddedDataValue(String                      parentDataValueGUID,
                                      String                      embeddedDataValueGUID,
                                      MakeAnchorOptions           makeAnchorOptions,
                                      EmbeddedDataValueProperties relationshipProperties) throws InvalidParameterException,
                                                                                                 PropertyServerException,
                                                                                                 UserNotAuthorizedException
    {
        dataValueHandler.linkEmbeddedDataValue(connectorUserId, parentDataValueGUID, embeddedDataValueGUID, makeAnchorOptions, relationshipProperties);
    }


    /**
     * Remove an embedded data value from its parent data value.
     *
     * @param parentDataValueGUID   unique identifier of the parent data value
     * @param embeddedDataValueGUID unique identifier of the embedded data value
     * @param deleteOptions         options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     */
    public void detachEmbeddedDataValue(String        parentDataValueGUID,
                                        String        embeddedDataValueGUID,
                                        DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                            PropertyServerException,
                                                                            UserNotAuthorizedException
    {
        dataValueHandler.detachEmbeddedDataValue(connectorUserId, parentDataValueGUID, embeddedDataValueGUID, deleteOptions);
    }
}
