/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.handlers;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.EntityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.validvalues.*;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * ValidValueDefinitionHandler provides methods to define valid values.
 */
public class ValidValueDefinitionHandler extends OpenMetadataHandlerBase
{
    /**
     * Create a new handler.
     *
     * @param localServerName        name of this server (view server)
     * @param auditLog               logging destination
     * @param localServiceName       local service name
     * @param openMetadataClient     access to open metadata
     */
    public ValidValueDefinitionHandler(String             localServerName,
                                       AuditLog           auditLog,
                                       String             localServiceName,
                                       OpenMetadataClient openMetadataClient)
    {
        super(localServerName,
              auditLog,
              localServiceName,
              openMetadataClient,
              OpenMetadataType.VALID_VALUE_DEFINITION.typeName);
    }


    /**
     * Create a new handler.
     *
     * @param localServerName        name of this server (view server)
     * @param auditLog               logging destination
     * @param localServiceName       local service name
     * @param openMetadataClient     access to open metadata
     * @param metadataElementTypeName type of principle element
     */
    public ValidValueDefinitionHandler(String             localServerName,
                                       AuditLog           auditLog,
                                       String             localServiceName,
                                       OpenMetadataClient openMetadataClient,
                                       String             metadataElementTypeName)
    {
        super(localServerName,
              auditLog,
              localServiceName,
              openMetadataClient,
              metadataElementTypeName);
    }


    /**
     * Create a new handler.
     *
     * @param template        properties to copy
     * @param specificTypeName   subtype  to control handler
     */
    public ValidValueDefinitionHandler(ValidValueDefinitionHandler template,
                                       String                      specificTypeName)
    {
        super(template, specificTypeName);
    }


    /**
     * Create a new validValueDefinition.
     *
     * @param userId                       userId of user making request.
     * @param newElementOptions details of the element to create
     * @param initialClassifications map of classification names to classification properties to include in the entity creation request
     * @param properties                   properties for the new element.
     * @param parentRelationshipProperties properties to include in parent relationship
     * @return unique identifier of the newly created element
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String createValidValueDefinition(String                                userId,
                                             NewElementOptions                     newElementOptions,
                                             Map<String, ClassificationProperties> initialClassifications,
                                             ValidValueDefinitionProperties properties,
                                             RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                                        PropertyServerException,
                                                                                                                        UserNotAuthorizedException
    {
        final String methodName = "createValidValueDefinition";

        return super.createNewElement(userId,
                                      newElementOptions,
                                      initialClassifications,
                                      properties,
                                      parentRelationshipProperties,
                                      methodName);
    }


    /**
     * Create a new metadata element to represent a validValueDefinition using an existing element as a template.
     * The template defines additional classifications and relationships that should be added to the new validValueDefinition.
     *
     * @param userId                       calling user
     * @param templateOptions details of the element to create
     * @param templateGUID the unique identifier of the existing referenceable to copy (this will copy all the attachments such as nested content, schema
     *                     connection etc)
     * @param replacementProperties properties of the new metadata element.  These override the template values
     * @param placeholderProperties property name-to-property value map to replace any placeholder values in the
     *                              template element - and their anchored elements, which are also copied as part of this operation.
     * @param parentRelationshipProperties properties to include in parent relationship
     *
     * @return unique identifier of the new metadata element
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createValidValueDefinitionFromTemplate(String                 userId,
                                                         TemplateOptions        templateOptions,
                                                         String                 templateGUID,
                                                         EntityProperties       replacementProperties,
                                                         Map<String, String>    placeholderProperties,
                                                         RelationshipProperties parentRelationshipProperties) throws InvalidParameterException,
                                                                                                                     UserNotAuthorizedException,
                                                                                                                     PropertyServerException
    {
        return super.createElementFromTemplate(userId,
                                               templateOptions,
                                               templateGUID,
                                               replacementProperties,
                                               placeholderProperties,
                                               parentRelationshipProperties);
    }


    /**
     * Update the properties of a validValueDefinition.
     *
     * @param userId                 userId of user making request.
     * @param validValueDefinitionGUID       unique identifier of the validValueDefinition (returned from create)
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties             properties for the element.
     * @return boolean - true if an update occurred
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public boolean updateValidValueDefinition(String                         userId,
                                              String                         validValueDefinitionGUID,
                                              UpdateOptions                  updateOptions,
                                              ValidValueDefinitionProperties properties) throws InvalidParameterException,
                                                                                                PropertyServerException,
                                                                                                UserNotAuthorizedException
    {
        final String methodName        = "updateValidValueDefinition";
        final String guidParameterName = "validValueDefinitionGUID";

        return super.updateElement(userId,
                                   validValueDefinitionGUID,
                                   guidParameterName,
                                   updateOptions,
                                   properties,
                                   methodName);
    }


    /**
     * Attach a valid value to an implementation - probably a referenceable.
     *
     * @param userId                 userId of user making request
     * @param validValueDefinitionGUID       unique identifier of the validValueDefinition
     * @param elementGUID           unique identifier of the location
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkValidValueImplementation(String                              userId,
                                             String                              validValueDefinitionGUID,
                                             String                              elementGUID,
                                             MakeAnchorOptions               makeAnchorOptions,
                                             ValidValuesImplementationProperties relationshipProperties) throws InvalidParameterException,
                                                                                                                PropertyServerException,
                                                                                                                UserNotAuthorizedException
    {
        final String methodName            = "linkLocationToProfile";
        final String end1GUIDParameterName = "validValueDefinitionGUID";
        final String end2GUIDParameterName = "elementGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(validValueDefinitionGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(elementGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.VALID_VALUES_IMPL_RELATIONSHIP.typeName,
                                                        validValueDefinitionGUID,
                                                        elementGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach a valid value from an implementation - probably a referenceable.
     *
     * @param userId                 userId of user making request.
     * @param validValueDefinitionGUID       unique identifier of the validValueDefinition
     * @param elementGUID           unique identifier of the location
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachValidValueImplementation(String        userId,
                                               String        validValueDefinitionGUID,
                                               String        elementGUID,
                                               DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException
    {
        final String methodName = "detachValidValueImplementation";

        final String end1GUIDParameterName = "validValueDefinitionGUID";
        final String end2GUIDParameterName = "elementGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(validValueDefinitionGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(elementGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.VALID_VALUES_IMPL_RELATIONSHIP.typeName,
                                                        validValueDefinitionGUID,
                                                        elementGUID,
                                                        deleteOptions);
    }


    /**
     * Attach a valid value to a consumer - probably a schema element or data set.
     *
     * @param userId                 userId of user making request
     * @param elementGUID           unique identifier of the location
     * @param validValueDefinitionGUID       unique identifier of the validValueDefinition
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkValidValuesAssignment(String                          userId,
                                          String                          elementGUID,
                                          String                          validValueDefinitionGUID,
                                          MakeAnchorOptions               makeAnchorOptions,
                                          ValidValuesAssignmentProperties relationshipProperties) throws InvalidParameterException,
                                                                                                         PropertyServerException,
                                                                                                         UserNotAuthorizedException
    {
        final String methodName            = "linkValidValuesAssignment";
        final String end1GUIDParameterName = "elementGUID";
        final String end2GUIDParameterName = "validValueDefinitionGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(validValueDefinitionGUID, end2GUIDParameterName, methodName);
        propertyHelper.validateGUID(elementGUID, end1GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.VALID_VALUES_ASSIGNMENT_RELATIONSHIP.typeName,
                                                        elementGUID,
                                                        validValueDefinitionGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach a valid value from a consumer - probably a schema element or data set.
     *
     * @param userId                 userId of user making request.
     * @param elementGUID           unique identifier of the location
     * @param validValueDefinitionGUID       unique identifier of the validValueDefinition
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachValidValuesAssignment(String        userId,
                                            String        elementGUID,
                                            String        validValueDefinitionGUID,
                                            DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        final String methodName = "detachValidValuesAssignment";

        final String end1GUIDParameterName = "elementGUID";
        final String end2GUIDParameterName = "validValueDefinitionGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(elementGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(validValueDefinitionGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.VALID_VALUES_ASSIGNMENT_RELATIONSHIP.typeName,
                                                        elementGUID,
                                                        validValueDefinitionGUID,
                                                        deleteOptions);
    }


    /**
     * Attach a valid value to a tagged element.
     *
     * @param userId                 userId of user making request
     * @param elementGUID           unique identifier of the location
     * @param validValueDefinitionGUID       unique identifier of the validValueDefinition
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkReferenceValueAssignment(String                             userId,
                                             String                             elementGUID,
                                             String                             validValueDefinitionGUID,
                                             MakeAnchorOptions                  makeAnchorOptions,
                                             ReferenceValueAssignmentProperties relationshipProperties) throws InvalidParameterException,
                                                                                                               PropertyServerException,
                                                                                                               UserNotAuthorizedException
    {
        final String methodName            = "linkReferenceValueAssignment";
        final String end1GUIDParameterName = "elementGUID";
        final String end2GUIDParameterName = "validValueDefinitionGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(validValueDefinitionGUID, end2GUIDParameterName, methodName);
        propertyHelper.validateGUID(elementGUID, end1GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.REFERENCE_VALUE_ASSIGNMENT_RELATIONSHIP.typeName,
                                                        elementGUID,
                                                        validValueDefinitionGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach a valid value from a tagged element.
     *
     * @param userId                 userId of user making request.
     * @param elementGUID           unique identifier of the location
     * @param validValueDefinitionGUID       unique identifier of the validValueDefinition
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachReferenceValueAssignment(String        userId,
                                               String        elementGUID,
                                               String        validValueDefinitionGUID,
                                               DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException
    {
        final String methodName = "detachReferenceValueAssignment";

        final String end1GUIDParameterName = "elementGUID";
        final String end2GUIDParameterName = "validValueDefinitionGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(elementGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(validValueDefinitionGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.REFERENCE_VALUE_ASSIGNMENT_RELATIONSHIP.typeName,
                                                        elementGUID,
                                                        validValueDefinitionGUID,
                                                        deleteOptions);
    }


    /**
     * Attach a valid value to one of its peers.
     *
     * @param userId                 userId of user making request
     * @param validValueOneGUID          unique identifier of the first valid value
     * @param validValueTwoGUID          unique identifier of the second valid value
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkAssociatedValidValues(String                          userId,
                                          String                          validValueOneGUID,
                                          String                          validValueTwoGUID,
                                          MakeAnchorOptions               makeAnchorOptions,
                                          ValidValueAssociationProperties relationshipProperties) throws InvalidParameterException,
                                                                                                         PropertyServerException,
                                                                                                         UserNotAuthorizedException
    {
        final String methodName            = "linkAssociatedValidValues";
        final String end1GUIDParameterName = "validValueOneGUID";
        final String end2GUIDParameterName = "validValueTwoGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(validValueOneGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(validValueTwoGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.VALID_VALUE_ASSOCIATION_RELATIONSHIP.typeName,
                                                        validValueOneGUID,
                                                        validValueTwoGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach a valid value from one of its peers.
     *
     * @param userId                 userId of user making request.
     * @param validValueOneGUID          unique identifier of the first valid value
     * @param validValueTwoGUID          unique identifier of the second valid value
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachAssociatedValidValues(String        userId,
                                            String        validValueOneGUID,
                                            String        validValueTwoGUID,
                                            DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        final String methodName = "detachAssociatedValidValues";
        final String end1GUIDParameterName = "validValueOneGUID";
        final String end2GUIDParameterName = "validValueTwoGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(validValueOneGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(validValueTwoGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.VALID_VALUE_ASSOCIATION_RELATIONSHIP.typeName,
                                                        validValueOneGUID,
                                                        validValueTwoGUID,
                                                        deleteOptions);
    }


    /**
     * Attach a valid value to one of its peers.
     *
     * @param userId                 userId of user making request
     * @param validValueOneGUID          unique identifier of the first valid value
     * @param validValueTwoGUID          unique identifier of the second valid value
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkConsistentValidValues(String                          userId,
                                          String                          validValueOneGUID,
                                          String                          validValueTwoGUID,
                                          MakeAnchorOptions               makeAnchorOptions,
                                          ConsistentValidValuesProperties relationshipProperties) throws InvalidParameterException,
                                                                                                         PropertyServerException,
                                                                                                         UserNotAuthorizedException
    {
        final String methodName            = "linkConsistentValidValues";
        final String end1GUIDParameterName = "validValueOneGUID";
        final String end2GUIDParameterName = "validValueTwoGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(validValueOneGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(validValueTwoGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.CONSISTENT_VALID_VALUES_RELATIONSHIP.typeName,
                                                        validValueOneGUID,
                                                        validValueTwoGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach a valid value from one of its peers.
     *
     * @param userId                 userId of user making request.
     * @param validValueOneGUID          unique identifier of the first valid value
     * @param validValueTwoGUID          unique identifier of the second valid value
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachConsistentValidValues(String        userId,
                                            String        validValueOneGUID,
                                            String        validValueTwoGUID,
                                            DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        final String methodName = "detachConsistentValidValues";
        final String end1GUIDParameterName = "validValueOneGUID";
        final String end2GUIDParameterName = "validValueTwoGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(validValueOneGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(validValueTwoGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.CONSISTENT_VALID_VALUES_RELATIONSHIP.typeName,
                                                        validValueOneGUID,
                                                        validValueTwoGUID,
                                                        deleteOptions);
    }


    /**
     * Attach a valid value to one of its peers.
     *
     * @param userId                 userId of user making request
     * @param validValueOneGUID          unique identifier of the first valid value
     * @param validValueTwoGUID          unique identifier of the second valid value
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkMappedValidValues(String                       userId,
                                      String                       validValueOneGUID,
                                      String                       validValueTwoGUID,
                                      MakeAnchorOptions            makeAnchorOptions,
                                      ValidValuesMappingProperties relationshipProperties) throws InvalidParameterException,
                                                                                                  PropertyServerException,
                                                                                                  UserNotAuthorizedException
    {
        final String methodName            = "linkMappedValidValues";
        final String end1GUIDParameterName = "validValueOneGUID";
        final String end2GUIDParameterName = "validValueTwoGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(validValueOneGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(validValueTwoGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.VALID_VALUES_MAPPING_RELATIONSHIP.typeName,
                                                        validValueOneGUID,
                                                        validValueTwoGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach a valid value from one of its peers.
     *
     * @param userId                 userId of user making request.
     * @param validValueOneGUID          unique identifier of the first valid value
     * @param validValueTwoGUID          unique identifier of the second valid value
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachMappedValidValues(String        userId,
                                        String        validValueOneGUID,
                                        String        validValueTwoGUID,
                                        DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                            PropertyServerException,
                                                                            UserNotAuthorizedException
    {
        final String methodName = "detachMappedValidValues";
        final String end1GUIDParameterName = "validValueOneGUID";
        final String end2GUIDParameterName = "validValueTwoGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(validValueOneGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(validValueTwoGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.VALID_VALUES_MAPPING_RELATIONSHIP.typeName,
                                                        validValueOneGUID,
                                                        validValueTwoGUID,
                                                        deleteOptions);
    }


    /**
     * Attach a valid value to a valid value set.
     *
     * @param userId                 userId of user making request
     * @param valueValueSetGUID          unique identifier of the super team
     * @param valueValueMemberGUID            unique identifier of the valueValueMember
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkValidValueMember(String                     userId,
                                     String                     valueValueSetGUID,
                                     String                     valueValueMemberGUID,
                                     MakeAnchorOptions          makeAnchorOptions,
                                     ValidValueMemberProperties relationshipProperties) throws InvalidParameterException,
                                                                                               PropertyServerException,
                                                                                               UserNotAuthorizedException
    {
        final String methodName            = "linkValidValueMember";
        final String end1GUIDParameterName = "valueValueSetGUID";
        final String end2GUIDParameterName = "valueValueMemberGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(valueValueSetGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(valueValueMemberGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.VALID_VALUE_MEMBER_RELATIONSHIP.typeName,
                                                        valueValueSetGUID,
                                                        valueValueMemberGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach a valid value from a valid value set.
     *
     * @param userId                 userId of user making request.
     * @param valueValueSetGUID          unique identifier of the super team
     * @param valueValueMemberGUID            unique identifier of the valueValueMember
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachValidValueMember(String        userId,
                                       String        valueValueSetGUID,
                                       String        valueValueMemberGUID,
                                       DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                           PropertyServerException,
                                                                           UserNotAuthorizedException
    {
        final String methodName = "detachValidValueMember";
        final String end1GUIDParameterName = "valueValueSetGUID";
        final String end2GUIDParameterName = "valueValueMemberGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(valueValueSetGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(valueValueMemberGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.VALID_VALUE_MEMBER_RELATIONSHIP.typeName,
                                                        valueValueSetGUID,
                                                        valueValueMemberGUID,
                                                        deleteOptions);
    }


    /**
     * Attach a referenceable to a specification property valid value.
     *
     * @param userId                 userId of user making request
     * @param referenceableGUID       unique identifier of the referenceable
     * @param validValueDefinitionGUID            unique identifier of the IT profile
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkSpecificationProperty(String                                    userId,
                                          String                                    referenceableGUID,
                                          String                                    validValueDefinitionGUID,
                                          MakeAnchorOptions                         makeAnchorOptions,
                                          SpecificationPropertyAssignmentProperties relationshipProperties) throws InvalidParameterException,
                                                                                                   PropertyServerException,
                                                                                                   UserNotAuthorizedException
    {
        final String methodName            = "linkSpecificationProperty";
        final String end1GUIDParameterName = "referenceableGUID";
        final String end2GUIDParameterName = "validValueDefinitionGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(referenceableGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(validValueDefinitionGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.SPECIFICATION_PROPERTY_ASSIGNMENT_RELATIONSHIP.typeName,
                                                        referenceableGUID,
                                                        validValueDefinitionGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach a referenceable to a specification property valid value.
     *
     * @param userId                 userId of user making request.
     * @param referenceableGUID              unique identifier of the referenceable
     * @param validValueDefinitionGUID          unique identifier of the IT profile
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachSpecificationProperty(String        userId,
                                            String        referenceableGUID,
                                            String        validValueDefinitionGUID,
                                            DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        final String methodName = "detachSpecificationProperty";
        final String end1GUIDParameterName = "referenceableGUID";
        final String end2GUIDParameterName = "validValueDefinitionGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(referenceableGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(validValueDefinitionGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.SPECIFICATION_PROPERTY_ASSIGNMENT_RELATIONSHIP.typeName,
                                                        referenceableGUID,
                                                        validValueDefinitionGUID,
                                                        deleteOptions);
    }


    /**
     * Delete a validValueDefinition.
     *
     * @param userId                 userId of user making request.
     * @param validValueDefinitionGUID       unique identifier of the element
     * @param deleteOptions options for a delete request
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteValidValueDefinition(String        userId,
                                           String        validValueDefinitionGUID,
                                           DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                               PropertyServerException,
                                                                               UserNotAuthorizedException
    {
        final String methodName        = "deleteValidValueDefinition";
        final String guidParameterName = "validValueDefinitionGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(validValueDefinitionGUID, guidParameterName, methodName);

        openMetadataClient.deleteMetadataElementInStore(userId, validValueDefinitionGUID, deleteOptions);
    }


    /**
     * Returns the list of validValueDefinitions with a particular name.
     *
     * @param userId                 userId of user making request
     * @param name                   name of the element to return - match is full text match in qualifiedName or name
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getValidValueDefinitionsByName(String       userId,
                                                                        String       name,
                                                                        QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                          PropertyServerException,
                                                                                                          UserNotAuthorizedException
    {
        final String methodName = "getValidValueDefinitionsByName";

        List<String> propertyNames = Arrays.asList(OpenMetadataProperty.QUALIFIED_NAME.name,
                                                   OpenMetadataProperty.IDENTIFIER.name,
                                                   OpenMetadataProperty.PREFERRED_VALUE.name,
                                                   OpenMetadataProperty.DISPLAY_NAME.name);

        return super.getRootElementsByName(userId,
                                           name,
                                           propertyNames,
                                           queryOptions,
                                           methodName);
    }


    /**
     * Return the properties of a specific validValueDefinition.
     *
     * @param userId                 userId of user making request
     * @param validValueDefinitionGUID       unique identifier of the required element
     * @param getOptions multiple options to control the query
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElement getValidValueDefinitionByGUID(String     userId,
                                                                 String     validValueDefinitionGUID,
                                                                 GetOptions getOptions) throws InvalidParameterException,
                                                                                               PropertyServerException,
                                                                                               UserNotAuthorizedException
    {
        final String methodName = "getValidValueDefinitionByGUID";

        return super.getRootElementByGUID(userId,
                                          validValueDefinitionGUID,
                                          getOptions,
                                          methodName);
    }


    /**
     * Retrieve the list of validValueDefinitions metadata elements that contain the search string.
     *
     * @param userId                 calling user
     * @param searchString           string to find in the properties
     * @param searchOptions multiple options to control the query
     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> findValidValueDefinitions(String        userId,
                                                                   String        searchString,
                                                                   SearchOptions searchOptions) throws InvalidParameterException,
                                                                                                       UserNotAuthorizedException,
                                                                                                       PropertyServerException
    {
        final String methodName  = "findValidValueDefinitions";

        return super.findRootElements(userId, searchString, searchOptions, methodName);
    }
}
