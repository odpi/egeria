/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.api;


import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.GovernanceStatusIdentifierElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.GovernanceStatusIdentifierSetElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.GovernanceStatusIdentifierProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.GovernanceStatusIdentifierSetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;

import java.util.List;

/**
 * GovernanceStatusLevelInterface defines the status levels of status for governance elements.
 */
public interface GovernanceStatusLevelInterface
{
    /* =====================================================================================================================
     * Egeria defines a default set of classification levels for each governance status.  The method below sets them up.
     */

    /**
     * Create a governance status identifier set using the standard values defined in GovernanceStatus enum
     *
     * @param userId calling user
     *
     * @return unique identifier of the governance level identifier set
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    String createStandardGovernanceStatusLevels(String userId) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException;


    /* =====================================================================================================================
     * The GovernanceStatusIdentifierSet entity is the top level element in a collection of related governance status level identifiers.
     */


    /**
     * Create a new metadata element to represent the root of a Governance Status Level Identifier Sets.
     *
     * @param userId calling user
     * @param properties properties to store
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    String createGovernanceStatusIdentifierSet(String                                  userId,
                                               GovernanceStatusIdentifierSetProperties properties) throws InvalidParameterException,
                                                                                                          UserNotAuthorizedException,
                                                                                                          PropertyServerException;


    /**
     * Update the metadata element representing a Governance Status Level Identifier Sets.
     *
     * @param userId calling user
     * @param setGUID unique identifier of the metadata element to remove
     * @param properties new properties for this element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void updateGovernanceStatusIdentifierSet(String                                  userId,
                                             String                                  setGUID,
                                             GovernanceStatusIdentifierSetProperties properties) throws InvalidParameterException,
                                                                                                        UserNotAuthorizedException,
                                                                                                        PropertyServerException;


    /**
     * Remove the metadata element representing a Governance Status Level Identifier Sets and all linked level identifiers.
     *
     * @param userId calling user
     * @param setGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void removeGovernanceStatusIdentifierSet(String userId,
                                             String setGUID) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException;


    /**
     * Retrieve the list of Governance Status Level Identifier Sets.
     *
     * @param userId calling user
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<GovernanceStatusIdentifierSetElement> getGovernanceStatusIdentifierSets(String userId) throws InvalidParameterException,
                                                                                                       UserNotAuthorizedException,
                                                                                                       PropertyServerException;




    /**
     * Retrieve the Governance Level Identifier Set metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param setGUID unique identifier of the requested metadata element
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    GovernanceStatusIdentifierSetElement getGovernanceStatusIdentifierSetByGUID(String userId,
                                                                                String setGUID) throws InvalidParameterException,
                                                                                                       UserNotAuthorizedException,
                                                                                                       PropertyServerException;


    /* =====================================================================================================================
     * A GovernanceStatusIdentifier describes an area of focus in the governance program.
     */

    /**
     * Create a new metadata element to represent a governance status level identifier.
     *
     * @param userId calling user
     * @param properties properties about the Governance LevelIdentifier to store
     * @param setGUID unique identifier of the set that this identifier belongs
     *
     * @return unique identifier of the new Governance LevelIdentifier
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    String createGovernanceStatusIdentifier(String                               userId,
                                            String                               setGUID,
                                            GovernanceStatusIdentifierProperties properties) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException;


    /**
     * Update the metadata element representing a governance status level identifier.
     *
     * @param userId calling user
     * @param identifierGUID unique identifier of the metadata element to update
     * @param properties new properties for the metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void updateGovernanceStatusIdentifier(String                               userId,
                                          String                               identifierGUID,
                                          GovernanceStatusIdentifierProperties properties) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException;


    /**
     * Remove the metadata element representing a governance status level identifier.
     *
     * @param userId calling user
     * @param identifierGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void removeGovernanceStatusIdentifier(String userId,
                                          String identifierGUID) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException;


    /**
     * Retrieve the governance status level identifier metadata element for the requested level within a specific governance status.
     *
     * @param userId calling user
     * @param statusIdentifier level value to retrieve
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    GovernanceStatusIdentifierElement getGovernanceStatusIdentifier(String userId,
                                                                    int    statusIdentifier) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException;
}
