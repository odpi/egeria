/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.connectorcontext;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.DataClassHandler;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.EntityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.DataClassCompositionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.DataClassHierarchyProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.DataClassProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;

import java.util.List;
import java.util.Map;

/**
 * Provides services for connectors to work with data class elements.
 */
public class DataClassClient extends ConnectorContextClientBase
{
    private final DataClassHandler dataClassHandler;


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
    public DataClassClient(ConnectorContextBase     parentContext,
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

        this.dataClassHandler = new DataClassHandler(localServerName, auditLog, localServiceName, openMetadataClient);
    }


    /**
     * Create a new data class.
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
    public String createDataClass(NewElementOptions                     newElementOptions,
                                  Map<String, ClassificationProperties> initialClassifications,
                                  DataClassProperties                   properties,
                                  RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                             PropertyServerException,
                                                                                                             UserNotAuthorizedException
    {
        String elementGUID = dataClassHandler.createDataClass(connectorUserId, newElementOptions, initialClassifications, properties, parentRelationshipProperties);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementCreation(elementGUID);
        }

        return elementGUID;
    }


    /**
     * Create a new metadata element to represent a dataClass using an existing element as a template.
     * The template defines additional classifications and relationships that should be added to the new dataClass.
     *
     * @param templateOptions details of the element to create
     * @param templateGUID the unique identifier of the existing dataClass to copy (this will copy all the attachments such as nested content, schema
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
    public String createDataClassFromTemplate(TemplateOptions        templateOptions,
                                              String                 templateGUID,
                                              EntityProperties       replacementProperties,
                                              Map<String, String>    placeholderProperties,
                                              RelationshipProperties parentRelationshipProperties) throws InvalidParameterException,
                                                                                                          UserNotAuthorizedException,
                                                                                                          PropertyServerException
    {
        String elementGUID = dataClassHandler.createDataClassFromTemplate(connectorUserId, templateOptions, templateGUID, replacementProperties, placeholderProperties, parentRelationshipProperties);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementCreation(elementGUID);
        }

        return elementGUID;
    }


    /**
     * Update the properties of a data class.
     *
     * @param dataClassGUID       unique identifier of the dataClass (returned from create)
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties             properties for the element.
     * @return boolean - true if an update occurred
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public boolean updateDataClass(String              dataClassGUID,
                                   UpdateOptions       updateOptions,
                                   DataClassProperties properties) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        boolean updateOccurred = dataClassHandler.updateDataClass(connectorUserId, dataClassGUID, updateOptions, properties);

        if ((updateOccurred) && (parentContext.getIntegrationReportWriter() != null))
        {
            parentContext.getIntegrationReportWriter().reportElementUpdate(dataClassGUID);
        }

        return updateOccurred;
    }


    /**
     * Connect two data classes to show that one is used by the other when it is validating (typically a complex data item).
     *
     * @param parentDataClassGUID    unique identifier of the parent data class
     * @param childDataClassGUID     unique identifier of the chile data class
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkNestedDataClass(String                         parentDataClassGUID,
                                    String                         childDataClassGUID,
                                    MakeAnchorOptions              makeAnchorOptions,
                                    DataClassCompositionProperties relationshipProperties) throws InvalidParameterException,
                                                                                                  PropertyServerException,
                                                                                                  UserNotAuthorizedException
    {
        dataClassHandler.linkNestedDataClass(connectorUserId, parentDataClassGUID, childDataClassGUID, makeAnchorOptions, relationshipProperties);
    }


    /**
     * Detach two nested data classes from one another.
     *
     * @param parentDataClassGUID    unique identifier of the  parent data class.
     * @param childDataClassGUID     unique identifier of the child data class.
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachNestedDataClass(String        parentDataClassGUID,
                                      String        childDataClassGUID,
                                      DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        dataClassHandler.detachNestedDataClass(connectorUserId, parentDataClassGUID, childDataClassGUID, deleteOptions);
    }


    /**
     * Connect two data classes to show that one provides a more specialist evaluation.
     *
     * @param parentDataClassGUID    unique identifier of the more generic data class
     * @param childDataClassGUID     unique identifier of the more specialized data class
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkSpecializedDataClass(String                       parentDataClassGUID,
                                         String                       childDataClassGUID,
                                         MakeAnchorOptions            makeAnchorOptions,
                                         DataClassHierarchyProperties relationshipProperties) throws InvalidParameterException,
                                                                                                     PropertyServerException,
                                                                                                     UserNotAuthorizedException
    {
        dataClassHandler.linkSpecializedDataClass(connectorUserId, parentDataClassGUID, childDataClassGUID, makeAnchorOptions, relationshipProperties);
    }


    /**
     * Detach two data classes from one another.
     *
     * @param parentDataClassGUID    unique identifier of the more generic data class
     * @param childDataClassGUID     unique identifier of the more specialized
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachSpecializedDataClass(String        parentDataClassGUID,
                                           String        childDataClassGUID,
                                           DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                               PropertyServerException,
                                                                               UserNotAuthorizedException
    {
        dataClassHandler.detachSpecializedDataClass(connectorUserId, parentDataClassGUID, childDataClassGUID, deleteOptions);
    }


    /**
     * Delete a dataClass.
     *
     * @param dataClassGUID       unique identifier of the element
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteDataClass(String        dataClassGUID,
                                DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException
    {
        dataClassHandler.deleteDataClass(connectorUserId, dataClassGUID, deleteOptions);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementDelete(dataClassGUID);
        }
    }


    /**
     * Returns the list of data classes with a particular name.
     *
     * @param name                   name of the element to return - match is full text match in qualifiedName, resourceName or displayName
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getDataClassesByName(String       name,
                                                              QueryOptions queryOptions) throws InvalidParameterException,
                                                                                               PropertyServerException,
                                                                                               UserNotAuthorizedException
    {
        return dataClassHandler.getDataClassesByName(connectorUserId, name, queryOptions);
    }


    /**
     * Return the properties of a specific data class.
     *
     * @param dataClassGUID       unique identifier of the required element
     * @param getOptions multiple options to control the query
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElement getDataClassByGUID(String     dataClassGUID,
                                                      GetOptions getOptions) throws InvalidParameterException,
                                                                                    PropertyServerException,
                                                                                    UserNotAuthorizedException
    {
        return dataClassHandler.getDataClassByGUID(connectorUserId, dataClassGUID, getOptions);
    }


    /**
     * Retrieve the list of dataClasss metadata elements that contain the search string and show which solution components (if any) are attached to it.
     * The returned dataClasss include a list of the components that are associated with it.
     * The search string is treated as a regular expression.
     *
     * @param searchString           string to find in the properties
     * @param searchOptions multiple options to control the query
     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> findDataClasses(String        searchString,
                                                        SearchOptions searchOptions) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        return dataClassHandler.findDataClasses(connectorUserId, searchString, searchOptions);
    }
}
