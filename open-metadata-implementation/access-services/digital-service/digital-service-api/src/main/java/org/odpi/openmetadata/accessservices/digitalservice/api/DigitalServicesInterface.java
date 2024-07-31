/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.digitalservice.api;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.DigitalServiceElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.DigitalServiceProperties;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DigitalServiceStatus;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.DigitalServiceDependencyProperties;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.List;

/**
 * The DigitalServicesInterface supports the management of digital services. The <i>DigitalRolesInterface</i> supports
 * the appointment of digital service managers. The <i>BusinessCapabilityManagement</i> associates business capabilities
 * with the digital services.
 */
public interface DigitalServicesInterface
{
    /**
     * Create a new digital service.
     *
     * @param userId calling user
     * @param properties properties of the digital service
     * @param initialStatus what is the initial status for the digital service - default value is DRAFT
     *
     * @return unique identifier of the digital service
     *
     * @throws InvalidParameterException one of the properties is null or invalid
     * @throws PropertyServerException problem accessing the metadata service
     * @throws UserNotAuthorizedException security access problem
     */
    String createDigitalService(String                   userId,
                                DigitalServiceProperties properties,
                                DigitalServiceStatus     initialStatus) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException;

    /**
     * Update an existing digital service.
     *
     * @param userId calling user
     * @param digitalServiceGUID unique identifier of the digital service to update
     * @param isMergeUpdate are unspecified properties unchanged (true) or removed?
     * @param properties properties to update
     *
     * @throws InvalidParameterException one of the properties is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void  updateDigitalService(String                   userId,
                               String                   digitalServiceGUID,
                               boolean                  isMergeUpdate,
                               DigitalServiceProperties properties) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException;


    /**
     * Update the status of a digital service
     *
     * @param userId calling user
     * @param digitalServiceGUID unique identifier
     * @param newStatus new status
     *
     * @throws InvalidParameterException guid, status or userId is null; guid is not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void setDigitalServiceStatus(String               userId,
                                 String               digitalServiceGUID,
                                 DigitalServiceStatus newStatus) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException;


    /**
     * Delete a specific digital service.
     *
     * @param userId calling user
     * @param digitalServiceGUID unique identifier of the digital service to remove
     *
     * @throws InvalidParameterException guid is null or not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void  removeDigitalService(String userId,
                               String digitalServiceGUID) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException;


    /**
     * Create a link to show that a digital service supports the requirements of another digital service.
     * If the link already exists the properties are updated.
     *
     * @param userId calling user
     * @param digitalServiceGUID unique identifier of the digital service
     * @param dependentDigitalServiceGUID unique identifier of the supporting digital service
     * @param properties description of how the supporting digital service provides support
     *
     * @throws InvalidParameterException one of the guids is null or not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void setupDigitalServiceDependency(String                             userId,
                                       String                             digitalServiceGUID,
                                       String                             dependentDigitalServiceGUID,
                                       DigitalServiceDependencyProperties properties) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException;


    /**
     * Remove the dependency link between two digital services.
     *
     * @param userId calling user
     * @param digitalServiceGUID unique identifier of the digital service
     * @param dependentDigitalServiceGUID unique identifier of the supporting digital service
     *
     * @throws InvalidParameterException one of the guids is null or not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void clearDigitalServiceDependency(String userId,
                                       String digitalServiceGUID,
                                       String dependentDigitalServiceGUID) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException;


    /**
     * Create a link to show that an organization has responsibility in operating a digital service.
     * If the link already exists the properties are updated.
     *
     * @param userId calling user
     * @param digitalServiceGUID unique identifier of the digital service
     * @param organizationGUID unique identifier of the organization operating the digital service
     * @param properties description of the scope of responsibilities that the organization has in the operation of the digital service
     *
     * @throws InvalidParameterException one of the guids is null or not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void setupDigitalServiceOperator(String                             userId,
                                     String                             digitalServiceGUID,
                                     String                             organizationGUID,
                                     DigitalServiceDependencyProperties properties) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException;


    /**
     * Remove the dependency link between two digital services.
     *
     * @param userId calling user
     * @param digitalServiceGUID unique identifier of the digital service
     * @param organizationGUID unique identifier of the organization operating the digital service
     *
     * @throws InvalidParameterException one of the guids is null or not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void clearDigitalServiceOperator(String userId,
                                     String digitalServiceGUID,
                                     String organizationGUID) throws InvalidParameterException,
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
    List<DigitalServiceElement> findDigitalServices(String userId,
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
    List<DigitalServiceElement> getDigitalServicesByName(String userId,
                                                         String name,
                                                         int    startFrom,
                                                         int    pageSize) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException;


    /**
     * Retrieve the list of digital services.
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
    List<DigitalServiceElement> getDigitalServices(String userId,
                                                   int    startFrom,
                                                   int    pageSize) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException;


    /**
     * Return information about the dependent digital services.
     *
     * @param userId calling user
     * @param digitalServiceGUID unique identifier for the digital service
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of matching actor profiles (hopefully only one)
     *
     * @throws InvalidParameterException name or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    List<RelatedElement> getDependentDigitalServices(String userId,
                                                     String digitalServiceGUID,
                                                     int    startFrom,
                                                     int    pageSize) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException;


    /**
     * Return information about the digital services that this digital service is dependent on.
     *
     * @param userId calling user
     * @param digitalServiceGUID unique identifier for the digital service
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of matching actor profiles (hopefully only one)
     *
     * @throws InvalidParameterException name or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    List<RelatedElement> getDigitalServiceDependencies(String userId,
                                                       String digitalServiceGUID,
                                                       int    startFrom,
                                                       int    pageSize) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException;


    /**
     * Return information about the business capabilities supported by a digital service.
     *
     * @param userId calling user
     * @param digitalServiceGUID unique identifier for the digital service
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of matching actor profiles (hopefully only one)
     *
     * @throws InvalidParameterException name or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    List<RelatedElement> getSupportedBusinessCapabilities(String userId,
                                                          String digitalServiceGUID,
                                                          int    startFrom,
                                                          int    pageSize) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException;


    /**
     * Return information about the digital products supported by a digital service.
     *
     * @param userId calling user
     * @param digitalServiceGUID unique identifier for the digital service
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of matching actor profiles (hopefully only one)
     *
     * @throws InvalidParameterException name or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    List<RelatedElement> getSupportedDigitalProducts(String userId,
                                                     String digitalServiceGUID,
                                                     int    startFrom,
                                                     int    pageSize) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException;


    /**
     * Retrieve the digital service metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param digitalServiceGUID unique identifier of the requested metadata element
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    DigitalServiceElement getDigitalServiceByGUID(String userId,
                                                  String digitalServiceGUID) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException;
}
