/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.digitalarchitecture.api;


import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ElementStatus;
import org.odpi.openmetadata.frameworks.openmetadata.enums.SequencingOrder;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;

import java.util.Date;
import java.util.List;

/**
 * ManageSolutions provides methods to define information supply chains, solution components and their supporting objects
 * The interface supports the following types of objects
 *
 * <ul>
 *     <li>InformationSupplyChains</li>
 *     <li>InformationSupplyChainSegments</li>
 *     <li>SolutionBlueprints</li>
 *     <li>SolutionComponents</li>
 *     <li>SolutionPorts</li>
 *     <li>SolutionRoles</li>
 * </ul>
 */
public interface ManageSolutions
{
    /**
     * Retrieve the list of information supply chains metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param searchString string to find in the properties
     * @param limitResultsByStatus control the status of the elements to retrieve - default is everything but Deleted
     * @param asOfTime repository time to use
     * @param sequencingProperty property to use for sequencing order
     * @param sequencingOrder order to retrieve results
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime effectivity dating for elements
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<InformationSupplyChainElement> findInformationSupplyChains(String              userId,
                                                                    String              searchString,
                                                                    List<ElementStatus> limitResultsByStatus,
                                                                    Date                asOfTime,
                                                                    SequencingOrder     sequencingOrder,
                                                                    String              sequencingProperty,
                                                                    int                 startFrom,
                                                                    int                 pageSize,
                                                                    Date                effectiveTime) throws InvalidParameterException,
                                                                                                              UserNotAuthorizedException,
                                                                                                              PropertyServerException;

    /**
     * Retrieve the list of solution blueprint metadata elements that contain the search string.
     * The returned blueprints include a list of the components that are associated with it.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param searchString string to find in the properties
     * @param limitResultsByStatus control the status of the elements to retrieve - default is everything but Deleted
     * @param asOfTime repository time to use
     * @param sequencingProperty property to use for sequencing order
     * @param sequencingOrder order to retrieve results
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime effectivity dating for elements
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<SolutionBlueprintElement> findSolutionBlueprints(String              userId,
                                                          String              searchString,
                                                          List<ElementStatus> limitResultsByStatus,
                                                          Date                asOfTime,
                                                          SequencingOrder     sequencingOrder,
                                                          String              sequencingProperty,
                                                          int                 startFrom,
                                                          int                 pageSize,
                                                          Date                effectiveTime) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException;

    /**
     * Retrieve the list of actor roles metadata elements that contain the search string and show which solution components (if any) are attached to it.
     * The returned solution roles include a list of the components that are associated with it.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param searchString string to find in the properties
     * @param limitResultsByStatus control the status of the elements to retrieve - default is everything but Deleted
     * @param asOfTime repository time to use
     * @param sequencingProperty property to use for sequencing order
     * @param sequencingOrder order to retrieve results
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime effectivity dating for elements
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<SolutionRoleElement> findSolutionRoles(String              userId,
                                                String              searchString,
                                                List<ElementStatus> limitResultsByStatus,
                                                Date                asOfTime,
                                                SequencingOrder     sequencingOrder,
                                                String              sequencingProperty,
                                                int                 startFrom,
                                                int                 pageSize,
                                                Date                effectiveTime) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException;
    /**
     * Retrieve the list of solution component metadata elements that contain the search string.  The solutions components returned include information about the consumers, actors and other solution components that are associated with them.
     * The returned solution components include a list of the subcomponents, peer components and solution roles that are associated with it.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param searchString string to find in the properties
     * @param limitResultsByStatus control the status of the elements to retrieve - default is everything but Deleted
     * @param asOfTime repository time to use
     * @param sequencingProperty property to use for sequencing order
     * @param sequencingOrder order to retrieve results
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime effectivity dating for elements
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<SolutionComponentElement> findSolutionComponents(String              userId,
                                                          String              searchString,
                                                          List<ElementStatus> limitResultsByStatus,
                                                          Date                asOfTime,
                                                          SequencingOrder     sequencingOrder,
                                                          String              sequencingProperty,
                                                          int                 startFrom,
                                                          int                 pageSize,
                                                          Date                effectiveTime) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException;

    /**
     * Retrieve the list of  metadata elements that are associated with the solution component via the ImplementedBy relationship.
     *
     * @param userId calling user
     * @param solutionComponentGUID unique identifier of the solution component to query
     * @param limitResultsByStatus control the status of the elements to retrieve - default is everything but Deleted
     * @param asOfTime repository time to use
     * @param sequencingProperty property to use for sequencing order
     * @param sequencingOrder order to retrieve results
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime effectivity dating for elements
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<RelatedMetadataElementSummary> getSolutionComponentImplementations(String              userId,
                                                                            String             solutionComponentGUID,
                                                                            List<ElementStatus> limitResultsByStatus,
                                                                            Date                asOfTime,
                                                                            SequencingOrder     sequencingOrder,
                                                                            String              sequencingProperty,
                                                                            int                 startFrom,
                                                                            int                 pageSize,
                                                                            Date                effectiveTime) throws InvalidParameterException,
                                                                                                                      UserNotAuthorizedException,
                                                                                                                      PropertyServerException;


}
