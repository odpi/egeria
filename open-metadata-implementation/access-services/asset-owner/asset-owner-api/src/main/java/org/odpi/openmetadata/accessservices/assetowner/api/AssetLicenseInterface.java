/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.api;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.LicenseElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.LicenseTypeElement;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedElementStub;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.LicenseProperties;

import java.util.List;

/**
 * The AssetLicenseInterface supports the management of the types of licenses (terms and conditions) associated with elements.
 */
public interface AssetLicenseInterface
{
    /* ========================================================
     * Management of the different types of licenses
     */

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
     * @param title identifier of license
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
    List<RelatedElementStub> getLicensedElements(String userId,
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