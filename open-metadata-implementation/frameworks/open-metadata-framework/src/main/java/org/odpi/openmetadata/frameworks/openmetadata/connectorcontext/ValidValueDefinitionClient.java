/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.connectorcontext;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.ValidValueDefinitionHandler;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.validvalues.*;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.List;
import java.util.Map;

/**
 * Provides services for connectors to work with ValidValueDefinition objects: Person, Team and ITProfile
 * along with contact details associated with this profile.
 */
public class ValidValueDefinitionClient extends ConnectorContextClientBase
{
    private final ValidValueDefinitionHandler   validValueDefinitionHandler;


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
    public ValidValueDefinitionClient(ConnectorContextBase     parentContext,
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

        this.validValueDefinitionHandler = new ValidValueDefinitionHandler(localServerName, auditLog, localServiceName, openMetadataClient);
    }


    /**
     * Copy/clone constructor.
     *
     * @param template client to copy
     * @param specificTypeName type name override
     */
    public ValidValueDefinitionClient(ValidValueDefinitionClient template,
                                      String                specificTypeName)
    {
        super(template);

        this.validValueDefinitionHandler = new ValidValueDefinitionHandler(template.validValueDefinitionHandler, specificTypeName);
    }


    /**
     * Create a new valid value definition.
     *
     * @param validValueSetGUID parent element
     * @param isDefaultValue is this the default value for the set?
     * @param properties                   properties for the new element.
     * @return unique identifier of the newly created element
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String createValidValueDefinition(String                         validValueSetGUID,
                                             ValidValueDefinitionProperties properties,
                                             boolean                        isDefaultValue) throws InvalidParameterException,
                                                                                                   PropertyServerException,
                                                                                                   UserNotAuthorizedException
    {
        NewElementOptions newElementOptions = new NewElementOptions(this.getMetadataSourceOptions());

        newElementOptions.setIsOwnAnchor(true);
        newElementOptions.setAnchorScopeGUID(validValueSetGUID);
        newElementOptions.setParentGUID(validValueSetGUID);
        newElementOptions.setParentAtEnd1(true);
        newElementOptions.setParentRelationshipTypeName(OpenMetadataType.VALID_VALUE_MEMBER_RELATIONSHIP.typeName);

        ValidValueMemberProperties validValueMemberProperties = new ValidValueMemberProperties();
        validValueMemberProperties.setIsDefaultValue(isDefaultValue);

        return this.createValidValueDefinition(newElementOptions,
                                               null,
                                               properties,
                                               validValueMemberProperties);
    }


    /**
     * Create a new valid value definition.
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
    public String createValidValueDefinition(NewElementOptions                     newElementOptions,
                                             Map<String, ClassificationProperties> initialClassifications,
                                             ValidValueDefinitionProperties        properties,
                                             RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                                        PropertyServerException,
                                                                                                                        UserNotAuthorizedException
    {
        String validValueDefinitionGUID = validValueDefinitionHandler.createValidValueDefinition(connectorUserId, newElementOptions, initialClassifications, properties, parentRelationshipProperties);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementCreation(validValueDefinitionGUID);
        }

        return validValueDefinitionGUID;
    }


    /**
     * Create a new metadata element to represent a valid value definition using an existing element as a template.
     * The template defines additional classifications and relationships that should be added to the new valid value definition.
     *
     * @param templateOptions details of the element to create
     * @param templateGUID the unique identifier of the existing asset to copy (this will copy all the attachments such as nested content, schema
     *                     connection etc)
     * qualified name (default is false).
     * @param replacementProperties properties of the new metadata element.  These override the template values
     * @param placeholderProperties property name-to-property value map to replace any placeholder values in the
     *                              template element - and their anchored elements, which are also copied as part of this operation.
     * @param parentRelationshipProperties properties to include in parent relationship
     * @return unique identifier of the new metadata element
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createValidValueDefinitionFromTemplate(TemplateOptions        templateOptions,
                                                         String                 templateGUID,
                                                         ElementProperties      replacementProperties,
                                                         Map<String, String>    placeholderProperties,
                                                         RelationshipProperties parentRelationshipProperties) throws InvalidParameterException,
                                                                                                                     UserNotAuthorizedException,
                                                                                                                     PropertyServerException
    {
        String validValueDefinitionGUID = validValueDefinitionHandler.createValidValueDefinitionFromTemplate(connectorUserId, templateOptions, templateGUID, replacementProperties, placeholderProperties, parentRelationshipProperties);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementCreation(validValueDefinitionGUID);
        }

        return validValueDefinitionGUID;
    }


    /**
     * Update the properties of a valid value definition.
     *
     * @param validValueDefinitionGUID       unique identifier of the valid value definition (returned from create)
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties             properties for the element.
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void updateValidValueDefinition(String                 validValueDefinitionGUID,
                                           UpdateOptions          updateOptions,
                                           ValidValueDefinitionProperties properties) throws InvalidParameterException,
                                                                                             PropertyServerException,
                                                                                             UserNotAuthorizedException
    {
        validValueDefinitionHandler.updateValidValueDefinition(connectorUserId, validValueDefinitionGUID, updateOptions, properties);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementUpdate(validValueDefinitionGUID);
        }
    }


    /**
     * Attach a valid value to an implementation - probably a referenceable.
     *
     * @param validValueDefinitionGUID       unique identifier of the valid value definition
     * @param elementGUID           unique identifier of the implementation - probably an asset
     * @param metadataSourceOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkValidValueImplementation(String                              validValueDefinitionGUID,
                                             String                              elementGUID,
                                             MetadataSourceOptions               metadataSourceOptions,
                                             ValidValuesImplementationProperties relationshipProperties) throws InvalidParameterException,
                                                                                                         PropertyServerException,
                                                                                                         UserNotAuthorizedException
    {
        validValueDefinitionHandler.linkValidValueImplementation(connectorUserId, validValueDefinitionGUID, elementGUID, metadataSourceOptions, relationshipProperties);
    }


    /**
     * Detach a valid value from an implementation - probably a referenceable.
     *
     * @param validValueDefinitionGUID       unique identifier of the valid value definition
     * @param elementGUID           unique identifier of the location
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachValidValueImplementation(String        validValueDefinitionGUID,
                                               String        elementGUID,
                                               DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException
    {
        validValueDefinitionHandler.detachValidValueImplementation(connectorUserId, validValueDefinitionGUID, elementGUID, deleteOptions);
    }



    /**
     * Attach a valid value to a consumer - probably a schema element or data set.
     *
     * @param elementGUID           unique identifier of the location
     * @param validValueDefinitionGUID       unique identifier of the validValueDefinition
     * @param metadataSourceOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkValidValuesAssignment(String                         elementGUID,
                                          String                         validValueDefinitionGUID,
                                          MetadataSourceOptions          metadataSourceOptions,
                                          ValidValuesAssignmentProperties relationshipProperties) throws InvalidParameterException,
                                                                                                         PropertyServerException,
                                                                                                         UserNotAuthorizedException
    {
        validValueDefinitionHandler.linkValidValuesAssignment(connectorUserId, elementGUID, validValueDefinitionGUID, metadataSourceOptions, relationshipProperties);
    }


    /**
     * Detach a valid value from a consumer - probably a schema element or data set.
     *
     * @param elementGUID           unique identifier of the location
     * @param validValueDefinitionGUID       unique identifier of the validValueDefinition
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachValidValuesAssignment(String        elementGUID,
                                            String        validValueDefinitionGUID,
                                            DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        validValueDefinitionHandler.detachValidValuesAssignment(connectorUserId, elementGUID, validValueDefinitionGUID, deleteOptions);
    }


    /**
     * Attach a valid value to a tagged element.
     *
     * @param elementGUID           unique identifier of the location
     * @param validValueDefinitionGUID       unique identifier of the validValueDefinition
     * @param metadataSourceOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkReferenceValueAssignment(String                             elementGUID,
                                             String                             validValueDefinitionGUID,
                                             MetadataSourceOptions              metadataSourceOptions,
                                             ReferenceValueAssignmentProperties relationshipProperties) throws InvalidParameterException,
                                                                                                               PropertyServerException,
                                                                                                               UserNotAuthorizedException
    {
        validValueDefinitionHandler.linkReferenceValueAssignment(connectorUserId, elementGUID, validValueDefinitionGUID, metadataSourceOptions, relationshipProperties);
    }


    /**
     * Detach a valid value from a tagged element.
     *
     * @param elementGUID           unique identifier of the location
     * @param validValueDefinitionGUID       unique identifier of the validValueDefinition
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachReferenceValueAssignment(String        elementGUID,
                                               String        validValueDefinitionGUID,
                                               DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException
    {
        validValueDefinitionHandler.detachReferenceValueAssignment(connectorUserId, elementGUID, validValueDefinitionGUID, deleteOptions);
    }


    /**
     * Attach a valid value to one of its peers.
     *
     * @param validValueOneGUID          unique identifier of the first valid value
     * @param validValueTwoGUID          unique identifier of the second valid value
     * @param metadataSourceOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkAssociatedValidValues(String                          validValueOneGUID,
                                          String                          validValueTwoGUID,
                                          MetadataSourceOptions           metadataSourceOptions,
                                          ValidValueAssociationProperties relationshipProperties) throws InvalidParameterException,
                                                                                                         PropertyServerException,
                                                                                                         UserNotAuthorizedException
    {
        validValueDefinitionHandler.linkAssociatedValidValues(connectorUserId, validValueOneGUID, validValueTwoGUID, metadataSourceOptions, relationshipProperties);
    }


    /**
     * Detach a valid value from one of its peers.
     *
     * @param validValueOneGUID          unique identifier of the first valid value
     * @param validValueTwoGUID          unique identifier of the second valid value
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachAssociatedValidValues(String        validValueOneGUID,
                                            String        validValueTwoGUID,
                                            DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        validValueDefinitionHandler.detachAssociatedValidValues(connectorUserId, validValueOneGUID, validValueTwoGUID, deleteOptions);
    }


    /**
     * Attach a valid value to one of its peers.
     *
     * @param validValueOneGUID          unique identifier of the first valid value
     * @param validValueTwoGUID          unique identifier of the second valid value
     * @param metadataSourceOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkConsistentValidValues(String                          validValueOneGUID,
                                          String                          validValueTwoGUID,
                                          MetadataSourceOptions           metadataSourceOptions,
                                          ConsistentValidValuesProperties relationshipProperties) throws InvalidParameterException,
                                                                                                         PropertyServerException,
                                                                                                         UserNotAuthorizedException
    {
        validValueDefinitionHandler.linkConsistentValidValues(connectorUserId, validValueOneGUID, validValueTwoGUID, metadataSourceOptions, relationshipProperties);
    }


    /**
     * Detach a valid value from one of its peers.
     *
     * @param validValueOneGUID          unique identifier of the first valid value
     * @param validValueTwoGUID          unique identifier of the second valid value
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachConsistentValidValues(String        validValueOneGUID,
                                            String        validValueTwoGUID,
                                            DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
       validValueDefinitionHandler.detachConsistentValidValues(connectorUserId, validValueOneGUID, validValueTwoGUID, deleteOptions);
    }


    /**
     * Attach a valid value to one of its peers.
     *
     * @param validValueOneGUID          unique identifier of the first valid value
     * @param validValueTwoGUID          unique identifier of the second valid value
     * @param metadataSourceOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkMappedValidValues(String                       validValueOneGUID,
                                      String                       validValueTwoGUID,
                                      MetadataSourceOptions        metadataSourceOptions,
                                      ValidValuesMappingProperties relationshipProperties) throws InvalidParameterException,
                                                                                                  PropertyServerException,
                                                                                                  UserNotAuthorizedException
    {
        validValueDefinitionHandler.linkMappedValidValues(connectorUserId, validValueOneGUID, validValueTwoGUID, metadataSourceOptions, relationshipProperties);
    }


    /**
     * Detach a valid value from one of its peers.
     *
     * @param validValueOneGUID          unique identifier of the first valid value
     * @param validValueTwoGUID          unique identifier of the second valid value
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachMappedValidValues(String        validValueOneGUID,
                                        String        validValueTwoGUID,
                                        DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                            PropertyServerException,
                                                                            UserNotAuthorizedException
    {
        validValueDefinitionHandler.detachMappedValidValues(connectorUserId, validValueOneGUID, validValueTwoGUID, deleteOptions);
    }


    /**
     * Attach a valid value to a valid value set.
     *
     * @param valueValueSetGUID          unique identifier of the super team
     * @param valueValueMemberGUID            unique identifier of the valueValueMember
     * @param metadataSourceOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkValidValueMember(String                     valueValueSetGUID,
                                     String                     valueValueMemberGUID,
                                     MetadataSourceOptions      metadataSourceOptions,
                                     ValidValueMemberProperties relationshipProperties) throws InvalidParameterException,
                                                                                               PropertyServerException,
                                                                                               UserNotAuthorizedException
    {
        validValueDefinitionHandler.linkValidValueMember(connectorUserId, valueValueSetGUID, valueValueMemberGUID, metadataSourceOptions, relationshipProperties);
    }


    /**
     * Detach a super team from a valueValueMember.
     *
     * @param valueValueSetGUID          unique identifier of the super team
     * @param valueValueMemberGUID            unique identifier of the valueValueMember
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachValidValueMember(String        valueValueSetGUID,
                                       String        valueValueMemberGUID,
                                       DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                           PropertyServerException,
                                                                           UserNotAuthorizedException
    {
        validValueDefinitionHandler.detachValidValueMember(connectorUserId, valueValueSetGUID, valueValueMemberGUID, deleteOptions);
    }


    /**
     * Attach a referenceable to a specification property valid value.
     *
     * @param referenceableGUID       unique identifier of the referenceable
     * @param validValueDefinitionGUID            unique identifier of the IT profile
     * @param metadataSourceOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkSpecificationProperty(String                                    referenceableGUID,
                                          String                                    validValueDefinitionGUID,
                                          MetadataSourceOptions                     metadataSourceOptions,
                                          SpecificationPropertyAssignmentProperties relationshipProperties) throws InvalidParameterException,
                                                                                                                   PropertyServerException,
                                                                                                                   UserNotAuthorizedException
    {
        validValueDefinitionHandler.linkSpecificationProperty(connectorUserId, referenceableGUID, validValueDefinitionGUID, metadataSourceOptions, relationshipProperties);
    }


    /**
     * Detach a referenceable to a specification property valid value.
     *
     * @param referenceableGUID              unique identifier of the referenceable
     * @param validValueDefinitionGUID          unique identifier of the IT profile
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachSpecificationProperty(String        referenceableGUID,
                                            String        validValueDefinitionGUID,
                                            DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        validValueDefinitionHandler.detachSpecificationProperty(connectorUserId, referenceableGUID, validValueDefinitionGUID, deleteOptions);
    }


    /**
     * Delete a valid value definition.
     *
     * @param validValueDefinitionGUID       unique identifier of the element
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteValidValueDefinition(String        validValueDefinitionGUID,
                                           DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                               PropertyServerException,
                                                                               UserNotAuthorizedException
    {
        validValueDefinitionHandler.deleteValidValueDefinition(connectorUserId, validValueDefinitionGUID, deleteOptions);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementDelete(validValueDefinitionGUID);
        }
    }


    /**
     * Returns the list of valid value definitions with a particular name.
     *
     * @param name                   name of the element to return - match is full text match in qualifiedName or name
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getValidValueDefinitionsByName(String       name,
                                                                        QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                          PropertyServerException,
                                                                                                          UserNotAuthorizedException
    {
        return validValueDefinitionHandler.getValidValueDefinitionsByName(connectorUserId, name, queryOptions);
    }


    /**
     * Return the properties of a specific valid value definition.
     *
     * @param validValueDefinitionGUID       unique identifier of the required element
     * @param getOptions multiple options to control the query
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElement getValidValueDefinitionByGUID(String     validValueDefinitionGUID,
                                                                 GetOptions getOptions) throws InvalidParameterException,
                                                                                               PropertyServerException,
                                                                                               UserNotAuthorizedException
    {
        return validValueDefinitionHandler.getValidValueDefinitionByGUID(connectorUserId, validValueDefinitionGUID, getOptions);
    }


    /**
     * Retrieve the list of valid value definitions metadata elements that contain the search string and show which solution components (if any) are attached to it.
     * The returned valid value definitions include a list of the components that are associated with it.
     * The search string is treated as a regular expression.
     *
     * @param searchString           string to find in the properties
     * @param searchOptions multiple options to control the query

     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> findValidValueDefinitions(String        searchString,
                                                                   SearchOptions searchOptions) throws InvalidParameterException,
                                                                                                       UserNotAuthorizedException,
                                                                                                       PropertyServerException
    {
        return validValueDefinitionHandler.findValidValueDefinitions(connectorUserId, searchString, searchOptions);
    }
}
