/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.api;

import org.odpi.openmetadata.accessservices.governanceprogram.metadataelements.GovernanceDomainElement;
import org.odpi.openmetadata.accessservices.governanceprogram.metadataelements.GovernanceDomainSetElement;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.GovernanceDomainProperties;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.GovernanceDomainSetProperties;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.List;


/**
 * GovernanceDomainInterface sets up the governance domains that are part of an organization governance.
 * Each governance domain describes a focus for governance.  The governance domain typically focuses on a particular set of activity
 * within the organization.  There is often overlap in the resources (assets) that each domain governs.  As a result, there is
 * often linkage between the governance definitions from different governance domains.
 */
public interface GovernanceDomainInterface
{
    /* =====================================================================================================================
     * Egeria defines a default set of governance domains for the governance program.  The method below sets them up.
     */

    /**
     * Create a governance domain set called "EgeriaStandardDomains" containing governance domain definitions for the following governance domains.
     *
     * <ul>
     *     <li>Unclassified - The governance domain is not specified - that is the definition applies to all domains - this is the default value for governance definitions within the governance program.</li>
     *     <li>Data - The data (information) governance domain</li>
     *     <li>Privacy - The data privacy governance domain</li>
     *     <li>Security - The security governance domain.</li>
     *     <li>IT Infrastructure - The IT infrastructure management governance domain.</li>
     *     <li>Software Development - The software development lifecycle (SDLC) governance domain.</li>
     *     <li>Corporate - The corporate governance domain.</li>
     *     <li>Asset Management - The physical asset management governance domain.</li>
     * </ul>
     *
     * @param userId calling user
     * @return unique identifier of the governance domain set
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    String createStandardGovernanceDomains(String userId) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException;


    /* =====================================================================================================================
     * The GovernanceDomainSet entity is the top level element in a collection of related governance domains.
     */


    /**
     * Create a new metadata element to represent the root of a Governance Domain Set.
     *
     * @param userId calling user
     * @param governanceDomainSetProperties properties to store
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    String createGovernanceDomainSet(String                        userId,
                                     GovernanceDomainSetProperties governanceDomainSetProperties) throws InvalidParameterException,
                                                                                                         UserNotAuthorizedException,
                                                                                                         PropertyServerException;


    /**
     * Update the metadata element representing a Governance Domain Set.
     *
     * @param userId calling user
     * @param governanceDomainSetGUID unique identifier of the metadata element to remove
     * @param governanceDomainSetProperties new properties for this element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void updateGovernanceDomainSet(String                        userId,
                                   String                        governanceDomainSetGUID,
                                   GovernanceDomainSetProperties governanceDomainSetProperties) throws InvalidParameterException,
                                                                                                       UserNotAuthorizedException,
                                                                                                       PropertyServerException;


    /**
     * Remove the metadata element representing a governanceDomainSet.  The governance domains are not deleted.
     *
     * @param userId calling user
     * @param governanceDomainSetGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void removeGovernanceDomainSet(String userId,
                                   String governanceDomainSetGUID) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException;


    /**
     * Retrieve the list of governanceDomainSet metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<GovernanceDomainSetElement>   findGovernanceDomainSets(String userId,
                                                                String searchString,
                                                                int    startFrom,
                                                                int    pageSize) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException;


    /**
     * Retrieve the list of governanceDomainSet metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<GovernanceDomainSetElement> getGovernanceDomainSetsByName(String userId,
                                                                   String name,
                                                                   int    startFrom,
                                                                   int    pageSize) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException;


   

    /**
     * Retrieve the governanceDomainSet metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param governanceDomainSetGUID unique identifier of the requested metadata element
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    GovernanceDomainSetElement getGovernanceDomainSetByGUID(String userId,
                                                            String governanceDomainSetGUID) throws InvalidParameterException, 
                                                                                                   UserNotAuthorizedException, 
                                                                                                   PropertyServerException;


    /* =====================================================================================================================
     * A Governance Domain describes an area of focus in the governance program.
     */

    /**
     * Create a new metadata element to represent a governance domain.
     *
     * @param userId calling user
     * @param setGUID unique identifier of the set that this identifier belongs
     * @param governanceDomainProperties properties about the Governance Domain to store
     *
     * @return unique identifier of the new Governance Domain
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    String createGovernanceDomain(String                     userId,
                                  String                     setGUID,
                                  GovernanceDomainProperties governanceDomainProperties) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException;
    

    /**
     * Update the metadata element representing a Governance Domain.
     *
     * @param userId calling user
     * @param governanceDomainGUID unique identifier of the metadata element to update
     * @param governanceDomainProperties new properties for the metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void updateGovernanceDomain(String                     userId,
                                String                     governanceDomainGUID,
                                GovernanceDomainProperties governanceDomainProperties) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException;


    /**
     * Remove the metadata element representing a Governance Domain.
     *
     * @param userId calling user
     * @param governanceDomainGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void deleteGovernanceDomain(String userId,
                                String governanceDomainGUID) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException;


    /**
     * Create a parent-child relationship between a governance domain set and a governance domain.
     *
     * @param userId calling user
     * @param governanceDomainSetGUID unique identifier of the governance domain set
     * @param governanceDomainGUID unique identifier of the governance domain
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void addDomainToSet(String userId,
                        String governanceDomainSetGUID,
                        String governanceDomainGUID) throws InvalidParameterException,
                                                            UserNotAuthorizedException,
                                                            PropertyServerException;


    /**
     * Remove a parent-child relationship between a governance domain set and a governance domain.
     *
     * @param userId calling user
     * @param governanceDomainSetGUID unique identifier of the governance domain set
     * @param governanceDomainGUID unique identifier of the governance domain
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void removeDomainFromSet(String userId,
                             String governanceDomainSetGUID,
                             String governanceDomainGUID) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException;


    /**
     * Retrieve the list of Governance Domain metadata elements defined for the governance program.
     *
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<GovernanceDomainElement> getGovernanceDomains(String userId,
                                                       int    startFrom,
                                                       int    pageSize) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException;


    /**
     * Retrieve the list of Governance Domain metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<GovernanceDomainElement> findGovernanceDomains(String userId,
                                                        String searchString,
                                                        int    startFrom,
                                                        int    pageSize) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException;


    /**
     * Return the list of governance domain sets that a governance domain belong.
     *
     * @param userId calling user
     * @param governanceDomainGUID unique identifier of the governance domain to query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of metadata elements describing the sets associated with the requested governanceDomainSet
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<GovernanceDomainSetElement> getSetsForGovernanceDomain(String userId,
                                                                String governanceDomainGUID,
                                                                int    startFrom,
                                                                int    pageSize) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException;


    /**
     * Retrieve the list of Governance Domain metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<GovernanceDomainElement> getGovernanceDomainsByName(String userId,
                                                             String name,
                                                             int    startFrom,
                                                             int    pageSize) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException;


    /**
     * Retrieve the Governance Domain metadata element with the supplied unique identifier assigned when the domain description was stored in
     * the metadata repository.
     *
     * @param userId calling user
     * @param governanceDomainGUID unique identifier of the requested metadata element
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    GovernanceDomainElement getGovernanceDomainByGUID(String userId,
                                                      String governanceDomainGUID) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException;


    /**
     * Retrieve the Governance Domain metadata element with the supplied domain identifier.
     *
     * @param userId calling user
     * @param domainIdentifier identifier used to identify the domain
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    GovernanceDomainElement getGovernanceDomainByIdentifier(String userId,
                                                            int    domainIdentifier) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException;

}
