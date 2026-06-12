/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.connectorcontext;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.DataValueSpecificationHandler;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.EntityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.*;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;

import java.util.List;
import java.util.Map;

/**
 * Provides services for connectors to work with data value specification elements.
 */
public class DataValueSpecificationClient extends ConnectorContextClientBase
{
    private final DataValueSpecificationHandler dataValueSpecificationHandler;


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
    public DataValueSpecificationClient(ConnectorContextBase     parentContext,
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

        this.dataValueSpecificationHandler = new DataValueSpecificationHandler(localServerName, auditLog, localServiceName, openMetadataClient);
    }


    /**
     * Create a new data value specification.
     *
     * @param newElementOptions details of the element to create
     * @param initialClassifications map of classification names to classification properties to include in the entity creation request
     * @param properties                   properties for the new element.
     * @param parentRelationshipProperties properties to include in parent relationship
     * @return unique identifier of the newly created element
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String createDataValueSpecification(NewElementOptions                     newElementOptions,
                                               Map<String, ClassificationProperties> initialClassifications,
                                               DataValueSpecificationProperties      properties,
                                               RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                                          PropertyServerException,
                                                                                                                          UserNotAuthorizedException
    {
        String elementGUID = dataValueSpecificationHandler.createDataValueSpecification(connectorUserId, newElementOptions, initialClassifications, properties, parentRelationshipProperties);

        if (parentContext.getActivityReportWriter() != null)
        {
            parentContext.getActivityReportWriter().reportElementCreation(elementGUID);
        }

        return elementGUID;
    }


    /**
     * Create a new metadata element to represent a dataValueSpecification using an existing element as a template.
     * The template defines additional classifications and relationships that should be added to the new dataValueSpecification.
     *
     * @param templateOptions details of the element to create
     * @param templateGUID the unique identifier of the existing element to copy
     * @param replacementProperties properties of the new metadata element.  These override the template values
     * @param replacementClassifications map of classification names to classification properties to include in the entity creation request. These override the template values.
     * @param placeholderProperties property name-to-property value map to replace any placeholder values in the
     *                              template element - and their anchored elements, which are also copied as part of this operation.
     * @param parentRelationshipProperties properties to include in parent relationship
     * @return unique identifier of the new metadata element
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     */
    public String createDataValueSpecificationFromTemplate(TemplateOptions                       templateOptions,
                                                           String                                templateGUID,
                                                           EntityProperties                      replacementProperties,
                                                           Map<String, ClassificationProperties> replacementClassifications,
                                                           Map<String, String>                   placeholderProperties,
                                                           RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                                                      UserNotAuthorizedException,
                                                                                                                                      PropertyServerException
    {
        String elementGUID = dataValueSpecificationHandler.createDataValueSpecificationFromTemplate(connectorUserId, templateOptions, templateGUID, replacementProperties, replacementClassifications, placeholderProperties, parentRelationshipProperties);

        if (parentContext.getActivityReportWriter() != null)
        {
            parentContext.getActivityReportWriter().reportElementCreation(elementGUID);
        }

        return elementGUID;
    }


    /**
     * Update the properties of a data value specification.
     *
     * @param dataValueSpecificationGUID       unique identifier of the dataValueSpecification (returned from create)
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties             properties for the element.
     * @return boolean - true if an update occurred
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public boolean updateDataValueSpecification(String              dataValueSpecificationGUID,
                                                UpdateOptions       updateOptions,
                                                DataValueSpecificationProperties properties) throws InvalidParameterException,
                                                                                                    PropertyServerException,
                                                                                                    UserNotAuthorizedException
    {
        boolean updateOccurred = dataValueSpecificationHandler.updateDataValueSpecification(connectorUserId, dataValueSpecificationGUID, updateOptions, properties);

        if ((updateOccurred) && (parentContext.getActivityReportWriter() != null))
        {
            parentContext.getActivityReportWriter().reportElementUpdate(dataValueSpecificationGUID);
        }

        return updateOccurred;
    }


    /**
     * Connect a data value to an element to show that it is part of its definition.
     *
     * @param elementGUID    unique identifier of the more generic data value specification
     * @param dataValueSpecificationGUID     unique identifier of the more specialized data value specification
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkDataValueDefinition(String                        elementGUID,
                                        String                        dataValueSpecificationGUID,
                                        MakeAnchorOptions             makeAnchorOptions,
                                        DataValueDefinitionProperties relationshipProperties) throws InvalidParameterException,
                                                                                                     PropertyServerException,
                                                                                                     UserNotAuthorizedException
    {
        dataValueSpecificationHandler.linkDataValueDefinition(connectorUserId, elementGUID, dataValueSpecificationGUID, makeAnchorOptions, relationshipProperties);
    }


    /**
     * Detach a data value from an element to show that it is no longer part of its definition.
     *
     * @param elementGUID    unique identifier of the more generic data value specification
     * @param dataValueSpecificationGUID     unique identifier of the more specialized
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachDataValueDefinition(String        elementGUID,
                                          String        dataValueSpecificationGUID,
                                          DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                              PropertyServerException,
                                                                              UserNotAuthorizedException
    {
        dataValueSpecificationHandler.detachDataValueDefinition(connectorUserId, elementGUID, dataValueSpecificationGUID, deleteOptions);
    }


    /**
     * Connect a data value to an element to show that it describes its data values.
     *
     * @param elementGUID    unique identifier of the more generic data value specification
     * @param dataValueSpecificationGUID     unique identifier of the more specialized data value specification
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkDataValueAssignment(String                        elementGUID,
                                        String                        dataValueSpecificationGUID,
                                        MakeAnchorOptions             makeAnchorOptions,
                                        DataValueAssignmentProperties relationshipProperties) throws InvalidParameterException,
                                                                                                     PropertyServerException,
                                                                                                     UserNotAuthorizedException
    {
        dataValueSpecificationHandler.linkDataValueAssignment(connectorUserId, elementGUID, dataValueSpecificationGUID, makeAnchorOptions, relationshipProperties);
    }


    /**
     * Detach a data value from an element to show that it no longer describes its data values.
     *
     * @param elementGUID    unique identifier of the more generic data value specification
     * @param dataValueSpecificationGUID     unique identifier of the more specialized
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachDataValueAssignment(String        elementGUID,
                                          String        dataValueSpecificationGUID,
                                          DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                              PropertyServerException,
                                                                              UserNotAuthorizedException
    {
        dataValueSpecificationHandler.detachDataValueAssignment(connectorUserId, elementGUID, dataValueSpecificationGUID, deleteOptions);
    }


    /**
     * Connect two data value specifications to show that one provides a more specialist evaluation.
     *
     * @param parentDataValueSpecificationGUID    unique identifier of the more generic data value specification
     * @param childDataValueSpecificationGUID     unique identifier of the more specialized data value specification
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkSpecializedDataValueSpecification(String                       parentDataValueSpecificationGUID,
                                                      String                       childDataValueSpecificationGUID,
                                                      MakeAnchorOptions            makeAnchorOptions,
                                                      DataValueHierarchyProperties relationshipProperties) throws InvalidParameterException,
                                                                                                                  PropertyServerException,
                                                                                                                  UserNotAuthorizedException
    {
        dataValueSpecificationHandler.linkSpecializedDataValueSpecification(connectorUserId, parentDataValueSpecificationGUID, childDataValueSpecificationGUID, makeAnchorOptions, relationshipProperties);
    }


    /**
     * Detach two data value specifications from one another.
     *
     * @param parentDataValueSpecificationGUID    unique identifier of the more generic data value specification
     * @param childDataValueSpecificationGUID     unique identifier of the more specialized
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachSpecializedDataValueSpecification(String        parentDataValueSpecificationGUID,
                                                        String        childDataValueSpecificationGUID,
                                                        DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                            PropertyServerException,
                                                                                            UserNotAuthorizedException
    {
        dataValueSpecificationHandler.detachSpecializedDataValueSpecification(connectorUserId, parentDataValueSpecificationGUID, childDataValueSpecificationGUID, deleteOptions);
    }


    /**
     * Delete a dataValueSpecification.
     *
     * @param dataValueSpecificationGUID       unique identifier of the element
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteDataValueSpecification(String        dataValueSpecificationGUID,
                                             DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException
    {
        dataValueSpecificationHandler.deleteDataValueSpecification(connectorUserId, dataValueSpecificationGUID, deleteOptions);

        if (parentContext.getActivityReportWriter() != null)
        {
            parentContext.getActivityReportWriter().reportElementDelete(dataValueSpecificationGUID);
        }
    }


    /**
     * Connect two data value specifications to show that one is used by the other when it is validating (typically a complex data item).
     *
     * @param parentDataClassGUID    unique identifier of the parent data class
     * @param childDataClassGUID     unique identifier of the child data class
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkNestedDataClass(String                         parentDataClassGUID,
                                    String                         childDataClassGUID,
                                    MakeAnchorOptions              makeAnchorOptions,
                                    DataClassCompositionProperties relationshipProperties) throws InvalidParameterException,
                                                                                                  PropertyServerException,
                                                                                                  UserNotAuthorizedException
    {
        dataValueSpecificationHandler.linkNestedDataClass(connectorUserId, parentDataClassGUID, childDataClassGUID, makeAnchorOptions, relationshipProperties);
    }


    /**
     * Detach two nested data classes from one another.
     *
     * @param parentDataClassGUID    unique identifier of the  parent data class.
     * @param childDataClassGUID     unique identifier of the child data class.
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachNestedDataClass(String        parentDataClassGUID,
                                      String        childDataClassGUID,
                                      DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        dataValueSpecificationHandler.detachNestedDataClass(connectorUserId, parentDataClassGUID, childDataClassGUID, deleteOptions);
    }


    /**
     * Returns the list of data value specifications with a particular name.
     *
     * @param name                   name of the element to return - match is full text match in qualifiedName, resourceName or displayName
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getDataValueSpecificationsByName(String       name,
                                                                          QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                            PropertyServerException,
                                                                                                            UserNotAuthorizedException
    {
        return dataValueSpecificationHandler.getDataValueSpecificationsByName(connectorUserId, name, queryOptions);
    }


    /**
     * Return the properties of a specific data value specification.
     *
     * @param dataValueSpecificationGUID       unique identifier of the required element
     * @param getOptions multiple options to control the query
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElement getDataValueSpecificationByGUID(String     dataValueSpecificationGUID,
                                                                   GetOptions getOptions) throws InvalidParameterException,
                                                                                                 PropertyServerException,
                                                                                                 UserNotAuthorizedException
    {
        return dataValueSpecificationHandler.getDataValueSpecificationByGUID(connectorUserId, dataValueSpecificationGUID, getOptions);
    }


    /**
     * Retrieve the list of dataValueSpecifications metadata elements that contain the search string and show which solution components (if any) are attached to it.
     * The returned dataValueSpecifications include a list of the components that are associated with it.
     * The search string is treated as a regular expression.
     *
     * @param searchString           string to find in the properties
     * @param searchOptions multiple options to control the query
     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> findDataValueSpecifications(String        searchString,
                                                                     SearchOptions searchOptions) throws InvalidParameterException,
                                                                                                         UserNotAuthorizedException,
                                                                                                         PropertyServerException
    {
        return dataValueSpecificationHandler.findDataValueSpecifications(connectorUserId, searchString, searchOptions);
    }
}
