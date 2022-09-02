/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.api;

import org.odpi.openmetadata.accessservices.governanceprogram.metadataelements.LicenseElement;
import org.odpi.openmetadata.accessservices.governanceprogram.metadataelements.LicenseTypeElement;
import org.odpi.openmetadata.accessservices.governanceprogram.metadataelements.RelatedElement;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.GovernanceDefinitionStatus;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.LicenseProperties;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.LicenseTypeProperties;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.List;

/**
 * The RightsManagementInterface supports the management of the types of licenses (terms and conditions) associated with elements.
 */
public interface RightsManagementInterface
{
    /* ========================================================
     * Management of the different types of licenses
     */

    /**
     * Create a description of the license type.
     *
     * @param userId calling user
     * @param properties license properties
     * @param initialStatus what is the initial status for the license type - default value is DRAFT
     *
     * @return unique identifier of new definition
     *
     * @throws InvalidParameterException documentIdentifier or userId is null; documentIdentifier is not unique
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    String createLicenseType(String                     userId,
                             LicenseTypeProperties      properties,
                             GovernanceDefinitionStatus initialStatus) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException;


    /**
     * Update the properties of the license type.
     *
     * @param userId calling user
     * @param licenseGUID identifier of the governance definition to change
     * @param isMergeUpdate are unspecified properties unchanged (true) or replaced with null?
     * @param properties license properties
     *
     * @throws InvalidParameterException guid, documentIdentifier or userId is null; documentIdentifier is not unique
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void updateLicenseType(String                userId,
                           String                licenseGUID,
                           boolean               isMergeUpdate,
                           LicenseTypeProperties properties) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException;


    /**
     * Update the status of a license type.
     *
     * @param userId calling user
     * @param licenseTypeGUID identifier of the governance definition to change
     * @param newStatus new status
     *
     * @throws InvalidParameterException guid, documentIdentifier or userId is null; documentIdentifier is not unique
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void setLicenseTypeStatus(String                     userId,
                              String                     licenseTypeGUID,
                              GovernanceDefinitionStatus newStatus) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException;


    /**
     * Delete the properties of the license type.
     *
     * @param userId calling user
     * @param licenseGUID identifier of the governance definition to delete
     *
     * @throws InvalidParameterException guid or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void deleteLicenseType(String userId,
                           String licenseGUID) throws InvalidParameterException,
                                                      UserNotAuthorizedException,
                                                      PropertyServerException;


    /**
     * Retrieve the license type by the unique identifier assigned by this service when it was created.
     *
     * @param userId calling user
     * @param licenseGUID identifier of the governance definition to retrieve
     *
     * @return properties of the license type
     *
     * @throws InvalidParameterException guid or userId is null; guid is not recognized
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    LicenseTypeElement getLicenseTypeByGUID(String userId,
                                            String licenseGUID) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException;


    /**
     * Retrieve the license type by its assigned unique document identifier.
     *
     * @param userId calling user
     * @param documentIdentifier identifier to search for
     *
     * @return properties of the matching license type
     *
     * @throws InvalidParameterException documentIdentifier or userId is null; documentIdentifier is not recognized
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    LicenseTypeElement getLicenseTypeByDocId(String userId,
                                             String documentIdentifier) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException;


    /**
     * Retrieve all the license types for a particular title.  The title can include regEx wildcards.
     *
     * @param userId calling user
     * @param title identifier of role
     * @param startFrom where to start from in the list of definitions
     * @param pageSize max number of results to return in one call
     *
     * @return list of matching roles (null if no matching elements)
     *
     * @throws InvalidParameterException title or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    List<LicenseTypeElement> getLicenseTypesByTitle(String userId,
                                                    String title,
                                                    int    startFrom,
                                                    int    pageSize) throws UserNotAuthorizedException,
                                                                            InvalidParameterException,
                                                                            PropertyServerException;


    /**
     * Retrieve all the license type definitions for a specific governance domain.
     *
     * @param userId calling user
     * @param domainIdentifier identifier to search for
     * @param startFrom where to start from in the list of definitions
     * @param pageSize max number of results to return in one call
     *
     * @return properties of the matching license type definitions
     *
     * @throws InvalidParameterException domainIdentifier or userId is null; domainIdentifier is not recognized
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    List<LicenseTypeElement> getLicenseTypeByDomainId(String userId,
                                                      int    domainIdentifier,
                                                      int    startFrom,
                                                      int    pageSize) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException;


    /* =======================================
     * Licensing
     */

    /**
     * Link an element to a license type and include details of the license in the relationship properties.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the element being licensed
     * @param licenseTypeGUID unique identifier for the license type
     * @param properties the properties of the license
     *
     * @return unique identifier of the new relationship
     *
     * @throws InvalidParameterException one of the properties is invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    String licenseElement(String            userId,
                          String            elementGUID,
                          String            licenseTypeGUID,
                          LicenseProperties properties) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException;


    /**
     * Update the properties of a license.
     *
     * @param userId calling user
     * @param licenseGUID unique identifier for the license relationship
     * @param isMergeUpdate should the supplied properties overlay the existing properties or replace them
     * @param properties the properties of the license
     *
     * @throws InvalidParameterException one of the properties is invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void updateLicense(String            userId,
                       String            licenseGUID,
                       boolean           isMergeUpdate,
                       LicenseProperties properties) throws InvalidParameterException,
                                                            UserNotAuthorizedException,
                                                            PropertyServerException;


    /**
     * Remove the license for an element.
     *
     * @param userId calling user
     * @param licenseGUID unique identifier for the license relationship
     *
     * @throws InvalidParameterException one of the properties is invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void unlicenseElement(String userId,
                          String licenseGUID)  throws InvalidParameterException,
                                                      UserNotAuthorizedException,
                                                      PropertyServerException;



    /**
     * Return information about the elements linked to a license.
     *
     * @param userId calling user
     * @param licenseGUID unique identifier for the license relationship
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return properties of the subject area
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    List<RelatedElement> getLicensedElements(String userId,
                                             String licenseGUID,
                                             int    startFrom,
                                             int    pageSize) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException;


    /**
     * Return information about the licenses linked to an element.
     *
     * @param userId calling user
     * @param elementGUID unique identifier for the license relationship
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return properties of the subject area
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    List<LicenseElement> getLicenses(String userId,
                                     String elementGUID,
                                     int    startFrom,
                                     int    pageSize) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException;
}