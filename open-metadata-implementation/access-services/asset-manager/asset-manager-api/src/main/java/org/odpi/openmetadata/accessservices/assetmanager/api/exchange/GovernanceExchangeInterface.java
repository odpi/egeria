/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.api.exchange;

import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.GovernanceDefinitionElement;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ExternalIdentifierProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.GovernanceDefinitionProperties;

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
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param externalIdentifierProperties optional properties used to define an external identifier
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
                                      ExternalIdentifierProperties   externalIdentifierProperties,
                                      GovernanceDefinitionProperties definitionProperties) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException;

    /**
     * Update an existing definition.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
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
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
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
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
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
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
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
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
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
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
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
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
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
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
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

}
