/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.api;

import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.GovernanceDefinitionElement;
import org.odpi.openmetadata.accessservices.assetmanager.properties.GovernanceDefinitionProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.KeyPattern;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.Map;

/**
 * The GovernanceExchangeInterface enables the exchange of governance definitions between an external asset manager and open metadata.
 * It includes the exchanges of governance definitions such as GovernancePolicy and GovernanceRule as well as classifications
 * set up by the governance teams such as SubjectArea classification.
 */
public interface GovernanceExchangeInterface
{
    /**
     * Create a new definition.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param definitionExternalIdentifier unique identifier of the definition in the external asset manager
     * @param definitionExternalIdentifierName name of property for the external identifier in the external asset manager
     * @param definitionExternalIdentifierUsage optional usage description for the external identifier when calling the external asset manager
     * @param definitionExternalIdentifierSource component that issuing this request.
     * @param definitionExternalIdentifierKeyPattern  pattern for the external identifier within the external asset manager (default is LOCAL_KEY)
     * @param mappingProperties additional properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param typeName type of definition
     * @param definitionProperties properties of the definition
     *
     * @return unique identifier of the definition
     *
     * @throws InvalidParameterException typeName, documentIdentifier or userId is null; documentIdentifier is not unique; typeName is not valid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    String createGovernanceDefinition(String                         userId,
                                      String                         assetManagerGUID,
                                      String                         assetManagerName,
                                      String                         typeName,
                                      String                         definitionExternalIdentifier,
                                      String                         definitionExternalIdentifierName,
                                      String                         definitionExternalIdentifierUsage,
                                      String                         definitionExternalIdentifierSource,
                                      KeyPattern                     definitionExternalIdentifierKeyPattern,
                                      Map<String, String>            mappingProperties,
                                      GovernanceDefinitionProperties definitionProperties) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException;

    /**
     * Update an existing definition.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param definitionGUID unique identifier of the definition to update
     * @param isMergeUpdate are unspecified properties unchanged (true) or replaced with null?
     * @param definitionProperties properties to update
     *
     * @throws InvalidParameterException guid, documentIdentifier or userId is null; documentIdentifier is not unique; guid is not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void  updateGovernanceDefinition(String                         userId,
                                     String                         assetManagerGUID,
                                     String                         assetManagerName,
                                     String                         definitionGUID,
                                     boolean                        isMergeUpdate,
                                     GovernanceDefinitionProperties definitionProperties) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException;


    /**
     * Delete a specific governance definition.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param definitionGUID unique identifier of the definition to remove
     *
     * @throws InvalidParameterException guid is null or not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void  deleteGovernanceDefinition(String userId,
                                     String assetManagerGUID,
                                     String assetManagerName,
                                     String definitionGUID) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException;


    /**
     * Link two related definitions together.
     * If the link already exists the description is updated.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param definitionOneGUID unique identifier of the first definition
     * @param definitionTwoGUID unique identifier of the second definition
     * @param description description of their relationship
     *
     * @throws InvalidParameterException one of the guids is null or not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void linkDefinitions(String userId,
                         String assetManagerGUID,
                         String assetManagerName,
                         String definitionOneGUID,
                         String definitionTwoGUID,
                         String description) throws InvalidParameterException,
                                                    UserNotAuthorizedException,
                                                    PropertyServerException;


    /**
     * Remove the link between two definitions.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param definitionOneGUID unique identifier of the first definition
     * @param definitionTwoGUID unique identifier of the second definition
     *
     * @throws InvalidParameterException one of the guids is null or not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void unlinkDefinitions(String userId,
                           String assetManagerGUID,
                           String assetManagerName,
                           String definitionOneGUID,
                           String definitionTwoGUID) throws InvalidParameterException,
                                                            UserNotAuthorizedException,
                                                            PropertyServerException;


    /**
     * Create a link to show that a governance definition supports the requirements of one of the governance drivers.
     * If the link already exists the rationale is updated.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param definitionGUID unique identifier of the governance definition
     * @param delegatedToDefinitionGUID unique identifier of the governance definition that is delegated to
     * @param rationale description of how the delegation supports the definition
     *
     * @throws InvalidParameterException one of the guids is null or not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void setupGovernanceDelegation(String userId,
                                   String assetManagerGUID,
                                   String assetManagerName,
                                   String definitionGUID,
                                   String delegatedToDefinitionGUID,
                                   String rationale) throws InvalidParameterException,
                                                            UserNotAuthorizedException,
                                                            PropertyServerException;


    /**
     * Remove the link between a governance definition and a governance definition that is delegated to (ie provides an implementation of).
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param definitionGUID unique identifier of the governance definition
     * @param delegatedToDefinitionGUID unique identifier of the governance definition that is delegated to
     *
     * @throws InvalidParameterException one of the guids is null or not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void clearGovernanceDelegation(String userId,
                                   String assetManagerGUID,
                                   String assetManagerName,
                                   String definitionGUID,
                                   String delegatedToDefinitionGUID) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException;


    /**
     * Retrieve the governance definition by the unique identifier assigned by this service when it was created.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param definitionGUID identifier of the governance definition to retrieve
     *
     * @return properties of the matching definition
     *
     * @throws InvalidParameterException documentIdentifier or userId is null; guid is not recognized
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    GovernanceDefinitionElement getGovernanceDefinitionByGUID(String userId,
                                                              String assetManagerGUID,
                                                              String assetManagerName,
                                                              String definitionGUID) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException;



    /**
     * Retrieve the governance definition by its assigned unique document identifier.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param documentIdentifier identifier to search for
     *
     * @return properties of the matching definition
     *
     * @throws InvalidParameterException documentIdentifier or userId is null; documentIdentifier is not recognized
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    GovernanceDefinitionElement getGovernanceDefinitionByDocId(String userId,
                                                               String assetManagerGUID,
                                                               String assetManagerName,
                                                               String documentIdentifier) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException;


    /**
     * Link a governance definition to an element using the GovernedBy relationship.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param definitionGUID identifier of the governance definition to link
     * @param elementGUID unique identifier of the metadata element to link
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void addGovernanceDefinitionToElement(String userId,
                                          String assetManagerGUID,
                                          String assetManagerName,
                                          String definitionGUID,
                                          String elementGUID,
                                          String methodName) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException;


    /**
     * Remove the GovernedBy relationship between a governance definition and an element.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param definitionGUID identifier of the governance definition to link
     * @param elementGUID unique identifier of the metadata element to update
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void removeGovernanceDefinitionFromElement(String userId,
                                               String assetManagerGUID,
                                               String assetManagerName,
                                               String definitionGUID,
                                               String elementGUID,
                                               String methodName) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException;


    /**
     * Classify the element to assert that it is part of a subject area definition.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param elementGUID unique identifier of the metadata element to update
     * @param elementExternalIdentifier unique identifier of the element in the external asset manager
     * @param subjectAreaName qualified name of subject area
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void addElementToSubjectArea(String userId,
                                 String assetManagerGUID,
                                 String assetManagerName,
                                 String elementGUID,
                                 String elementExternalIdentifier,
                                 String subjectAreaName,
                                 String methodName) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException;


    /**
     * Remove the subject area designation from the identified element.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param elementGUID unique identifier of the metadata element to update
     * @param externalElementIdentifier unique identifier of the equivalent element in the external asset manager
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void removeElementFromSubjectArea(String userId,
                                      String assetManagerGUID,
                                      String assetManagerName,
                                      String elementGUID,
                                      String externalElementIdentifier,
                                      String methodName) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException;
}
