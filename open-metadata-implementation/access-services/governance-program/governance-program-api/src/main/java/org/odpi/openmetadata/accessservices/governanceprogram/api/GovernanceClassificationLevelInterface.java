/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.api;


import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.GovernanceLevelIdentifierElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.GovernanceLevelIdentifierSetElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.GovernanceLevelIdentifierProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.GovernanceLevelIdentifierSetProperties;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;


import java.util.List;

/**
 * GovernanceClassificationLevelInterface defines the levels of the standard governance classifications of Impact, Criticality,
 * Retention, Confidence and Confidentiality.
 */
public interface GovernanceClassificationLevelInterface
{
    /* =====================================================================================================================
     * Egeria defines a default set of classification levels for each governance classification.  The method below sets them up.
     */

    /**
     * Create a governance level identifier set for a specific governance classification:
     *
     * <ul>
     *     <li>Impact - classification used to document the impact of an issue or situation.</li>
     *     <li>Criticality - classification used to document how critical an asset or activity is.</li>
     *     <li>Retention - classification used to identify the basis that an asset should be retained.</li>
     *     <li>Confidence - classification use to document an assessment of the quality of an asset or element with an asset.</li>
     *     <li>Confidentiality - classification use to define how much access to an asset should be restricted.</li>
     * </ul>
     *
     * @param userId calling user
     * @param classificationName name of the classification level to set up
     *
     * @return unique identifier of the governance level identifier set
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    String createStandardGovernanceClassificationLevels(String userId,
                                                        String classificationName) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException;


    /* =====================================================================================================================
     * The GovernanceLevelIdentifierSet entity is the top level element in a collection of related governance classification level identifiers.
     */


    /**
     * Create a new metadata element to represent the root of a Governance Classification Level Identifier Sets.
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
    String createGovernanceLevelIdentifierSet(String                                 userId,
                                              GovernanceLevelIdentifierSetProperties properties) throws InvalidParameterException,
                                                                                                        UserNotAuthorizedException,
                                                                                                        PropertyServerException;


    /**
     * Update the metadata element representing a Governance Classification Level Identifier Sets.
     *
     * @param userId calling user
     * @param setGUID unique identifier of the metadata element to remove
     * @param properties new properties for this element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void updateGovernanceLevelIdentifierSet(String                                 userId,
                                            String                                 setGUID,
                                            GovernanceLevelIdentifierSetProperties properties) throws InvalidParameterException,
                                                                                                      UserNotAuthorizedException,
                                                                                                      PropertyServerException;


    /**
     * Remove the metadata element representing a Governance Classification Level Identifier Sets and all linked level identifiers.
     *
     * @param userId calling user
     * @param setGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void removeGovernanceLevelIdentifierSet(String userId,
                                            String setGUID) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException;


    /**
     * Retrieve the list of Governance Classification Level Identifier Sets.
     *
     * @param userId calling user
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<GovernanceLevelIdentifierSetElement> getGovernanceLevelIdentifierSets(String userId) throws InvalidParameterException,
                                                                                                     UserNotAuthorizedException,
                                                                                                     PropertyServerException;


    /**
     * Retrieve the Governance Level Identifier Set for a requested governance classification.
     *
     * @param userId calling user
     * @param classificationName classificationName to search for
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    GovernanceLevelIdentifierSetElement getGovernanceLevelIdentifierSet(String userId,
                                                                        String classificationName) throws InvalidParameterException,
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
    GovernanceLevelIdentifierSetElement getGovernanceLevelIdentifierSetByGUID(String userId,
                                                                              String setGUID) throws InvalidParameterException,
                                                                                                     UserNotAuthorizedException,
                                                                                                     PropertyServerException;


    /* =====================================================================================================================
     * A GovernanceLevelIdentifier describes an area of focus in the governance program.
     */

    /**
     * Create a new metadata element to represent a governance classification level identifier.
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
    String createGovernanceLevelIdentifier(String                              userId,
                                           String                              setGUID,
                                           GovernanceLevelIdentifierProperties properties) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException;


    /**
     * Update the metadata element representing a governance classification level identifier.
     *
     * @param userId calling user
     * @param identifierGUID unique identifier of the metadata element to update
     * @param properties new properties for the metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void updateGovernanceLevelIdentifier(String                     userId,
                                         String                     identifierGUID,
                                         GovernanceLevelIdentifierProperties properties) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException;


    /**
     * Remove the metadata element representing a governance classification level identifier.
     *
     * @param userId calling user
     * @param identifierGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void removeGovernanceLevelIdentifier(String userId,
                                         String identifierGUID) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException;


    /**
     * Retrieve the governance classification level identifier metadata element for the requested level within a specific governance classification.
     *
     * @param userId calling user
     * @param classificationName string to find in the properties
     * @param levelIdentifier level value to retrieve
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    GovernanceLevelIdentifierElement getGovernanceLevelIdentifier(String userId,
                                                                  String classificationName,
                                                                  int    levelIdentifier) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException;
}
