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
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.glossaries.*;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * GlossaryTermHandler provides methods to define glossary terms
 */
public class GlossaryTermHandler extends OpenMetadataHandlerBase
{

    /**
     * Create a new handler.
     *
     * @param localServerName        name of this server (view server)
     * @param auditLog               logging destination
     * @param localServiceName       local service name
     * @param openMetadataClient     access to open metadata
     */
    public GlossaryTermHandler(String             localServerName,
                               AuditLog           auditLog,
                               String             localServiceName,
                               OpenMetadataClient openMetadataClient)
    {
        super(localServerName,
              auditLog,
              localServiceName,
              openMetadataClient,
              OpenMetadataType.GLOSSARY_TERM.typeName);
    }


    /**
     * Create a new glossary term.
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
    public String createGlossaryTerm(String                                userId,
                                     NewElementOptions                     newElementOptions,
                                     Map<String, ClassificationProperties> initialClassifications,
                                     GlossaryTermProperties                properties,
                                     RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                                PropertyServerException,
                                                                                                                UserNotAuthorizedException
    {
        final String methodName = "createGlossaryTerm";

        return super.createNewElement(userId,
                                      newElementOptions,
                                      initialClassifications,
                                      properties,
                                      parentRelationshipProperties,
                                      methodName);
    }


    /**
     * Create a new metadata element to represent a glossary term using an existing element as a template.
     * The template defines additional classifications and relationships that should be added to the new glossary term.
     *
     * @param userId                       calling user
     * @param templateOptions details of the element to create
     * @param templateGUID the unique identifier of the existing asset to copy (this will copy all the attachments such as nested content, schema
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
    public String createGlossaryTermFromTemplate(String                 userId,
                                                 TemplateOptions        templateOptions,
                                                 String                 templateGUID,
                                                 ElementProperties      replacementProperties,
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
     * Update the properties of a glossary term.
     *
     * @param userId                 userId of user making request.
     * @param glossaryTermGUID       unique identifier of the glossary term (returned from create)
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties             properties for the element.
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void updateGlossaryTerm(String               userId,
                                   String               glossaryTermGUID,
                                   UpdateOptions        updateOptions,
                                   GlossaryTermProperties properties) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        final String methodName        = "updateGlossaryTerm";
        final String guidParameterName = "glossaryTermGUID";

        super.updateElement(userId,
                            glossaryTermGUID,
                            guidParameterName,
                            updateOptions,
                            properties,
                            methodName);
    }


    /**
     * Update the glossary term using the properties and classifications from the parentGUID stored in the request body.
     *
     * @param userId calling user
     * @param glossaryTermGUID unique identifier of the glossary term to update
     * @param templateGUID identifier for the template glossary term
     * @param updateWithTemplateOptions options for the change to the term
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateGlossaryTermFromTemplate(String                    userId,
                                               String                    glossaryTermGUID,
                                               String                    templateGUID,
                                               UpdateWithTemplateOptions updateWithTemplateOptions) throws InvalidParameterException,
                                                                                                           UserNotAuthorizedException,
                                                                                                           PropertyServerException
    {
        final String methodName                    = "updateGlossaryTermFromTemplate";
        final String glossaryGUIDParameterName     = "templateGUID";
        final String glossaryTermGUIDParameterName = "glossaryTermGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(glossaryTermGUID, glossaryGUIDParameterName, methodName);
        propertyHelper.validateGUID(templateGUID, glossaryTermGUIDParameterName, methodName);
    }


    /**
     * Move a glossary term from one glossary to another.  This involves anchoring to the new glossary and
     * changing the term anchor relationship
     *
     * @param userId                 userId of user making request
     * @param glossaryTermGUID       unique identifier of the glossary term
     * @param glossaryGUID           unique identifier of the location
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void moveGlossaryTerm(String            userId,
                                 String            glossaryTermGUID,
                                 String            glossaryGUID,
                                 DeleteOptions     deleteOptions) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        final String methodName            = "moveGlossaryTerm";
        final String end1GUIDParameterName = "glossaryTermGUID";
        final String end2GUIDParameterName = "glossaryGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(glossaryTermGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(glossaryGUID, end2GUIDParameterName, methodName);
    }


    /**
     * Return the list of term-to-term relationship names.
     *
     * @return list of type names that are subtypes of asset
     */
    public List<String> getTermRelationshipTypeNames()
    {
        return Arrays.asList(OpenMetadataType.RELATED_TERM_RELATIONSHIP.typeName,
                             OpenMetadataType.SYNONYM_RELATIONSHIP.typeName,
                             OpenMetadataType.ANTONYM_RELATIONSHIP.typeName,
                             OpenMetadataType.PREFERRED_TERM_RELATIONSHIP.typeName,
                             OpenMetadataType.REPLACEMENT_TERM_RELATIONSHIP.typeName,
                             OpenMetadataType.ISA_RELATIONSHIP.typeName);
    }


    /**
     * Link two terms together using a specialist relationship.
     *
     * @param userId                 userId of user making request
     * @param relationshipTypeName name of the type of relationship to create
     * @param glossaryTermOneGUID unique identifier of the glossary term at end 1
     * @param glossaryTermTwoGUID unique identifier of the glossary term at end 2
     * @param metadataSourceOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void setupTermRelationship(String                   userId,
                                      String                   relationshipTypeName,
                                      String                   glossaryTermOneGUID,
                                      String                   glossaryTermTwoGUID,
                                      MetadataSourceOptions    metadataSourceOptions,
                                      GlossaryTermRelationship relationshipProperties) throws InvalidParameterException,
                                                                                              PropertyServerException,
                                                                                              UserNotAuthorizedException
    {
        final String methodName                = "setupTermRelationship";
        final String end1GUIDParameterName     = "glossaryTermOneGUID";
        final String end2GUIDParameterName     = "glossaryTermTwoGUID";
        final String glossaryTypeParameterName = "relationshipTypeName";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(glossaryTermOneGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(glossaryTermTwoGUID, end2GUIDParameterName, methodName);
        propertyHelper.validateMandatoryName(relationshipTypeName, glossaryTypeParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        relationshipTypeName,
                                                        glossaryTermOneGUID,
                                                        glossaryTermTwoGUID,
                                                        metadataSourceOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Update the relationship properties for the two terms.
     *
     * @param userId                 userId of user making request
     * @param relationshipTypeName name of the type of relationship to create
     * @param glossaryTermOneGUID unique identifier of the glossary term at end 1
     * @param glossaryTermTwoGUID unique identifier of the glossary term at end 2
     * @param updateOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void updateTermRelationship(String                   userId,
                                      String                   relationshipTypeName,
                                      String                   glossaryTermOneGUID,
                                      String                   glossaryTermTwoGUID,
                                      UpdateOptions    updateOptions,
                                      GlossaryTermRelationship relationshipProperties) throws InvalidParameterException,
                                                                                              PropertyServerException,
                                                                                              UserNotAuthorizedException
    {
        final String methodName                = "updateTermRelationship";
        final String end1GUIDParameterName     = "glossaryTermOneGUID";
        final String end2GUIDParameterName     = "glossaryTermTwoGUID";
        final String glossaryTypeParameterName = "relationshipTypeName";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(glossaryTermOneGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(glossaryTermTwoGUID, end2GUIDParameterName, methodName);
        propertyHelper.validateMandatoryName(relationshipTypeName, glossaryTypeParameterName, methodName);

        openMetadataClient.updateRelatedElementsInStore(userId,
                                                        relationshipTypeName,
                                                        glossaryTermOneGUID,
                                                        glossaryTermTwoGUID,
                                                        updateOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Remove the relationship between two terms.
     *
     * @param userId                 userId of user making request.
     * @param relationshipTypeName name of the type of relationship to create
     * @param glossaryTermOneGUID unique identifier of the glossary term at end 1
     * @param glossaryTermTwoGUID unique identifier of the glossary term at end 2
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void clearTermRelationship(String        userId,
                                      String        relationshipTypeName,
                                      String        glossaryTermOneGUID,
                                      String        glossaryTermTwoGUID,
                                      DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        final String methodName = "clearTermRelationship";

        final String end1GUIDParameterName     = "glossaryTermOneGUID";
        final String end2GUIDParameterName     = "glossaryTermTwoGUID";
        final String glossaryTypeParameterName = "relationshipTypeName";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(glossaryTermOneGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(glossaryTermTwoGUID, end2GUIDParameterName, methodName);
        propertyHelper.validateMandatoryName(relationshipTypeName, glossaryTypeParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        relationshipTypeName,
                                                        glossaryTermOneGUID,
                                                        glossaryTermTwoGUID,
                                                        deleteOptions);
    }


    /**
     * Classify the glossary term to indicate that it describes an abstract concept.
     *
     * @param userId                 userId of user making request.
     * @param glossaryTermGUID    unique identifier of the term.
     * @param properties            properties for the classification
     * @param metadataSourceOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void setTermAsAbstractConcept(String                    userId,
                                         String                    glossaryTermGUID,
                                         AbstractConceptProperties properties,
                                         MetadataSourceOptions     metadataSourceOptions) throws InvalidParameterException,
                                                                                                 PropertyServerException,
                                                                                                 UserNotAuthorizedException
    {
        final String methodName = "setTermAsAbstractConcept";
        final String guidParameterName = "glossaryTermGUID";
        final String propertiesParameterName = "properties";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(glossaryTermGUID, guidParameterName, methodName);
        propertyHelper.validateObject(properties, propertiesParameterName, methodName);

        openMetadataClient.classifyMetadataElementInStore(userId,
                                                          glossaryTermGUID,
                                                          OpenMetadataType.ABSTRACT_CONCEPT_CLASSIFICATION.typeName,
                                                          metadataSourceOptions,
                                                          classificationBuilder.getNewElementProperties(properties));
    }


    /**
     * Remove the abstract concept designation from the glossary term.
     *
     * @param userId                 userId of user making request.
     * @param glossaryTermGUID    unique identifier of the term.
     * @param metadataSourceOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void clearTermAsAbstractConcept(String                userId,
                                               String                glossaryTermGUID,
                                               MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                                   PropertyServerException,
                                                                                                   UserNotAuthorizedException
    {
        final String methodName = "clearTermAsAbstractConcept";
        final String guidParameterName = "glossaryTermGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(glossaryTermGUID, guidParameterName, methodName);

        openMetadataClient.declassifyMetadataElementInStore(userId,
                                                            glossaryTermGUID,
                                                            OpenMetadataType.ABSTRACT_CONCEPT_CLASSIFICATION.typeName,
                                                            metadataSourceOptions);
    }


    /**
     * Classify the glossary term to indicate that it describes a data value.
     *
     * @param userId                 userId of user making request.
     * @param glossaryTermGUID    unique identifier of the term.
     * @param properties            properties for the classification
     * @param metadataSourceOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void setTermAsDataValue(String                    userId,
                                   String                    glossaryTermGUID,
                                   DataValueProperties       properties,
                                   MetadataSourceOptions     metadataSourceOptions) throws InvalidParameterException,
                                                                                           PropertyServerException,
                                                                                           UserNotAuthorizedException
    {
        final String methodName = "setTermAsDataValue";
        final String guidParameterName = "glossaryTermGUID";
        final String propertiesParameterName = "properties";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(glossaryTermGUID, guidParameterName, methodName);
        propertyHelper.validateObject(properties, propertiesParameterName, methodName);

        openMetadataClient.classifyMetadataElementInStore(userId,
                                                          glossaryTermGUID,
                                                          OpenMetadataType.DATA_VALUE_CLASSIFICATION.typeName,
                                                          metadataSourceOptions,
                                                          classificationBuilder.getNewElementProperties(properties));
    }


    /**
     * Remove the data value designation from the glossary term.
     *
     * @param userId                 userId of user making request.
     * @param glossaryTermGUID    unique identifier of the term.
     * @param metadataSourceOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void clearTermAsDataValue(String                userId,
                                     String                glossaryTermGUID,
                                     MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                         PropertyServerException,
                                                                                         UserNotAuthorizedException
    {
        final String methodName = "clearTermAsDataValue";
        final String guidParameterName = "glossaryTermGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(glossaryTermGUID, guidParameterName, methodName);

        openMetadataClient.declassifyMetadataElementInStore(userId,
                                                            glossaryTermGUID,
                                                            OpenMetadataType.DATA_VALUE_CLASSIFICATION.typeName,
                                                            metadataSourceOptions);
    }


    /**
     * Classify the glossary term to indicate that it describes an activity.
     *
     * @param userId                 userId of user making request.
     * @param glossaryTermGUID    unique identifier of the term.
     * @param properties            properties for the classification
     * @param metadataSourceOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void setTermAsActivity(String                    userId,
                                  String                    glossaryTermGUID,
                                  ActivityDescriptionProperties properties,
                                  MetadataSourceOptions     metadataSourceOptions) throws InvalidParameterException,
                                                                                          PropertyServerException,
                                                                                          UserNotAuthorizedException
    {
        final String methodName = "setTermAsActivity";
        final String guidParameterName = "glossaryTermGUID";
        final String propertiesParameterName = "properties";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(glossaryTermGUID, guidParameterName, methodName);
        propertyHelper.validateObject(properties, propertiesParameterName, methodName);

        openMetadataClient.classifyMetadataElementInStore(userId,
                                                          glossaryTermGUID,
                                                          OpenMetadataType.ACTIVITY_DESCRIPTION_CLASSIFICATION.typeName,
                                                          metadataSourceOptions,
                                                          classificationBuilder.getNewElementProperties(properties));
    }


    /**
     * Remove the activity designation from the glossary term.
     *
     * @param userId                 userId of user making request.
     * @param glossaryTermGUID    unique identifier of the term.
     * @param metadataSourceOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void clearTermAsActivity(String                userId,
                                    String                glossaryTermGUID,
                                    MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                        PropertyServerException,
                                                                                        UserNotAuthorizedException
    {
        final String methodName = "clearTermAsActivity";
        final String guidParameterName = "glossaryTermGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(glossaryTermGUID, guidParameterName, methodName);

        openMetadataClient.declassifyMetadataElementInStore(userId,
                                                            glossaryTermGUID,
                                                            OpenMetadataType.ACTIVITY_DESCRIPTION_CLASSIFICATION.typeName,
                                                            metadataSourceOptions);
    }


    /**
     * Classify the glossary term to indicate that it describes a context.
     *
     * @param userId                 userId of user making request.
     * @param glossaryTermGUID    unique identifier of the term.
     * @param properties            properties for the classification
     * @param metadataSourceOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void setTermAsContext(String                    userId,
                                  String                    glossaryTermGUID,
                                  ContextDefinitionProperties properties,
                                  MetadataSourceOptions     metadataSourceOptions) throws InvalidParameterException,
                                                                                          PropertyServerException,
                                                                                          UserNotAuthorizedException
    {
        final String methodName = "setTermAsContext";
        final String guidParameterName = "glossaryTermGUID";
        final String propertiesParameterName = "properties";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(glossaryTermGUID, guidParameterName, methodName);
        propertyHelper.validateObject(properties, propertiesParameterName, methodName);

        openMetadataClient.classifyMetadataElementInStore(userId,
                                                          glossaryTermGUID,
                                                          OpenMetadataType.CONTEXT_DEFINITION_CLASSIFICATION.typeName,
                                                          metadataSourceOptions,
                                                          classificationBuilder.getNewElementProperties(properties));
    }


    /**
     * Remove the context definition designation from the glossary term.
     *
     * @param userId                 userId of user making request.
     * @param glossaryTermGUID    unique identifier of the term.
     * @param metadataSourceOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void clearTermAsContext(String                userId,
                                    String                glossaryTermGUID,
                                    MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                        PropertyServerException,
                                                                                        UserNotAuthorizedException
    {
        final String methodName = "clearTermAsContext";
        final String guidParameterName = "glossaryTermGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(glossaryTermGUID, guidParameterName, methodName);

        openMetadataClient.declassifyMetadataElementInStore(userId,
                                                            glossaryTermGUID,
                                                            OpenMetadataType.CONTEXT_DEFINITION_CLASSIFICATION.typeName,
                                                            metadataSourceOptions);
    }


    /**
     * Delete a glossary term.
     *
     * @param userId                 userId of user making request.
     * @param glossaryTermGUID       unique identifier of the element
     * @param deleteOptions options for a delete request
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteGlossaryTerm(String        userId,
                                   String        glossaryTermGUID,
                                   DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                       PropertyServerException,
                                                                       UserNotAuthorizedException
    {
        final String methodName        = "deleteGlossaryTerm";
        final String guidParameterName = "glossaryTermGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(glossaryTermGUID, guidParameterName, methodName);

        openMetadataClient.deleteMetadataElementInStore(userId, glossaryTermGUID, deleteOptions);
    }


    /**
     * Returns the list of glossary terms with a particular name.
     *
     * @param userId                 userId of user making request
     * @param name                   name of the element to return - match is full text match in qualifiedName or name
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getGlossaryTermsByName(String       userId,
                                                                String       name,
                                                                QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                  PropertyServerException,
                                                                                                  UserNotAuthorizedException
    {
        final String methodName = "getGlossaryTermsByName";

        List<String> propertyNames = Arrays.asList(OpenMetadataProperty.QUALIFIED_NAME.name,
                                                   OpenMetadataProperty.DISPLAY_NAME.name);

        return super.getRootElementsByName(userId, name, propertyNames, queryOptions, methodName);
    }


    /**
     * Return the properties of a specific glossary term.
     *
     * @param userId                 userId of user making request
     * @param glossaryTermGUID       unique identifier of the required element
     * @param getOptions multiple options to control the query
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElement getGlossaryTermByGUID(String     userId,
                                                         String     glossaryTermGUID,
                                                         GetOptions getOptions) throws InvalidParameterException,
                                                                                       PropertyServerException,
                                                                                       UserNotAuthorizedException
    {
        final String methodName = "getGlossaryTermByGUID";

        return super.getRootElementByGUID(userId, glossaryTermGUID, getOptions, methodName);
    }


    /**
     * Retrieve the list of glossary terms metadata elements that contain the search string and show which solution components (if any) are attached to it.
     * The returned glossary terms include a list of the components that are associated with it.
     * The search string is treated as a regular expression.
     *
     * @param userId                 calling user
     * @param searchString           string to find in the properties
     * @param queryOptions multiple options to control the query
     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> findGlossaryTerms(String        userId,
                                                           String        searchString,
                                                           SearchOptions queryOptions) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        final String methodName  = "findGlossaryTerms";

        // todo include glossary GUID in search
        return super.findRootElements(userId, searchString, queryOptions, methodName);
    }


    /**
     * Returns the list of glossary terms for a glossary with a particular glossaryGUID.
     *
     * @param userId                 userId of user making request
     * @param glossaryGUID           unique identifier of the starting glossary
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getTermsForGlossary(String       userId,
                                                             String       glossaryGUID,
                                                             QueryOptions queryOptions) throws InvalidParameterException,
                                                                                               PropertyServerException,
                                                                                               UserNotAuthorizedException
    {
        final String methodName = "getTermsForGlossary";
        final String guidParameterName = "glossaryGUID";

        return super.getRelatedRootElements(userId,
                                            glossaryGUID,
                                            guidParameterName,
                                            1,
                                            OpenMetadataType.PARENT_GLOSSARY_RELATIONSHIP.typeName,
                                            queryOptions,
                                            methodName);
    }
}
