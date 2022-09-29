/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.digitalservice.api;

import org.odpi.openmetadata.accessservices.digitalservice.metadataelements.BusinessCapabilityElement;
import org.odpi.openmetadata.accessservices.digitalservice.metadataelements.RelatedElement;
import org.odpi.openmetadata.accessservices.digitalservice.properties.BusinessCapabilityProperties;
import org.odpi.openmetadata.accessservices.digitalservice.properties.DigitalSupportProperties;
import org.odpi.openmetadata.accessservices.digitalservice.properties.OrganizationalCapabilityProperties;
import org.odpi.openmetadata.accessservices.digitalservice.properties.TemplateProperties;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.List;

/**
 * BusinessCapabilityManagement supports the maintenance, linking and reporting associated with business capabilities.
 */
public interface BusinessCapabilityManagement
{
    /**
     * Create a new metadata element to represent the business capability.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param businessCapabilityProperties properties to store
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    String createBusinessCapability(String                       userId,
                                    String                       externalSourceGUID,
                                    String                       externalSourceName,
                                    BusinessCapabilityProperties businessCapabilityProperties) throws InvalidParameterException,
                                                                                                      UserNotAuthorizedException,
                                                                                                      PropertyServerException;


    /**
     * Create a new metadata element to represent a business capability using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new business capability.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    String createBusinessCapabilityFromTemplate(String             userId,
                                                String             externalSourceGUID,
                                                String             externalSourceName,
                                                String             templateGUID,
                                                TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException;


    /**
     * Update the metadata element representing a business capability.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param businessCapabilityGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param businessCapabilityProperties new properties for this element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void updateBusinessCapability(String                       userId,
                                  String                       externalSourceGUID,
                                  String                       externalSourceName,
                                  String                       businessCapabilityGUID,
                                  boolean                      isMergeUpdate,
                                  BusinessCapabilityProperties businessCapabilityProperties) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException;


    /**
     * Create a relationship between a business capability and a team to show that the support provided by the team for the business capability.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param businessCapabilityGUID unique identifier of the business capability
     * @param properties describes the scope of responsibility that the team has in the business capability
     * @param teamGUID unique identifier of the team
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void setupOrganizationalCapability(String                             userId,
                                       String                             externalSourceGUID,
                                       String                             externalSourceName,
                                       String                             businessCapabilityGUID,
                                       OrganizationalCapabilityProperties properties,
                                       String                             teamGUID) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException;


    /**
     * Remove a relationship between a business capability and a team.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param businessCapabilityGUID unique identifier of the business capability
     * @param teamGUID unique identifier of the team
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void clearOrganizationalCapability(String userId,
                                       String externalSourceGUID,
                                       String externalSourceName,
                                       String businessCapabilityGUID,
                                       String teamGUID) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException;



    /**
     * Create a relationship between a business capability and a team to show that the support provided by the team for the business capability.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param businessCapabilityGUID unique identifier of the business capability
     * @param properties describes the scope of responsibility that the team has in the business capability
     * @param digitalServiceGUID unique identifier of the digital service
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void setupSupportingDigitalService(String                   userId,
                                       String                   externalSourceGUID,
                                       String                   externalSourceName,
                                       String                   businessCapabilityGUID,
                                       DigitalSupportProperties properties,
                                       String                   digitalServiceGUID) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException;


    /**
     * Remove a relationship between a business capability and a team.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param businessCapabilityGUID unique identifier of the business capability
     * @param digitalServiceGUID unique identifier of the digital service
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void clearSupportingDigitalService(String userId,
                                       String externalSourceGUID,
                                       String externalSourceName,
                                       String businessCapabilityGUID,
                                       String digitalServiceGUID) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException;


    /**
     * Remove the metadata element representing a business capability.  This will delete all anchored
     * elements such as comments.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param businessCapabilityGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void removeBusinessCapability(String userId,
                                  String externalSourceGUID,
                                  String externalSourceName,
                                  String businessCapabilityGUID) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException;




    /**
     * Retrieve the list of metadata elements that contain the search string.
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
    List<BusinessCapabilityElement> findBusinessCapabilities(String userId,
                                                             String searchString,
                                                             int    startFrom,
                                                             int    pageSize) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException;


    /**
     * Retrieve the list of metadata elements with a matching qualified or display name.
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
    List<BusinessCapabilityElement> getBusinessCapabilitiesByName(String userId,
                                                                  String name,
                                                                  int    startFrom,
                                                                  int    pageSize) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException;


    /**
     * Retrieve the list of business capabilities.
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
    List<BusinessCapabilityElement> getBusinessCapabilities(String userId,
                                                            int    startFrom,
                                                            int    pageSize) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException;


    /**
     * Return information about the teams supporting a business capability.
     *
     * @param userId calling user
     * @param businessCapabilityGUID unique identifier for the business capability
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of matching actor profiles (hopefully only one)
     *
     * @throws InvalidParameterException name or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    List<RelatedElement> getOrganizationalCapability(String userId,
                                                     String businessCapabilityGUID,
                                                     int    startFrom,
                                                     int    pageSize) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException;



    /**
     * Return information about the digital services supporting a business capability.
     *
     * @param userId calling user
     * @param businessCapabilityGUID unique identifier for the business capability
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of matching actor profiles (hopefully only one)
     *
     * @throws InvalidParameterException name or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    List<RelatedElement> getDigitalSupport(String userId,
                                           String businessCapabilityGUID,
                                           int    startFrom,
                                           int    pageSize) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException;


    /**
     * Retrieve the business capability metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param businessCapabilityGUID unique identifier of the requested metadata element
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    BusinessCapabilityElement getBusinessCapabilityByGUID(String userId,
                                                          String businessCapabilityGUID) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException;
}
