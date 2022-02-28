/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.api;

import org.odpi.openmetadata.accessservices.governanceprogram.metadataelements.CertificationTypeElement;
import org.odpi.openmetadata.accessservices.governanceprogram.metadataelements.GovernanceRoleElement;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.CertificationProperties;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.CertificationTypeProperties;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.GovernanceDefinitionStatus;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.Date;
import java.util.List;

/**
 * The CertificationManagementInterface provides the ability to manage the certification types that can be associated with elements.
 */
public interface CertificationManagementInterface
{
    /* ========================================
     * Certification Types
     */

    /**
     * Create a description of the certification type.
     *
     * @param userId calling user
     * @param properties certification properties
     * @param initialStatus what is the initial status for the certification type - default value is DRAFT
     *
     * @return unique identifier of new definition
     *
     * @throws InvalidParameterException documentIdentifier or userId is null; documentIdentifier is not unique
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    String createCertificationType(String                      userId,
                                   CertificationTypeProperties properties,
                                   GovernanceDefinitionStatus  initialStatus) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException;


    /**
     * Update the properties of the certification type.
     *
     * @param userId calling user
     * @param certificationTypeGUID identifier of the governance definition to change
     * @param isMergeUpdate are unspecified properties unchanged (true) or replaced with null?
     * @param properties certification properties
     *
     * @throws InvalidParameterException guid, documentIdentifier or userId is null; documentIdentifier is not unique
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void updateCertificationType(String                      userId,
                                 String                      certificationTypeGUID,
                                 boolean                     isMergeUpdate,
                                 CertificationTypeProperties properties) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException;


    /**
     * Delete the properties of the certification type.
     *
     * @param userId calling user
     * @param certificationTypeGUID identifier of the governance definition to delete
     *
     * @throws InvalidParameterException guid or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void deleteCertificationType(String userId,
                                 String certificationTypeGUID) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException;


    /**
     * Retrieve the certification type by the unique identifier assigned by this service when it was created.
     *
     * @param userId calling user
     * @param certificationTypeGUID identifier of the governance definition to retrieve
     *
     * @return properties of the certification type
     *
     * @throws InvalidParameterException guid or userId is null; guid is not recognized
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    CertificationTypeElement getCertificationTypeByGUID(String userId,
                                                        String certificationTypeGUID) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException;


    /**
     * Retrieve the certification type by its assigned unique document identifier.
     *
     * @param userId calling user
     * @param documentIdentifier identifier to search for
     *
     * @return properties of the matching certification type
     *
     * @throws InvalidParameterException documentIdentifier or userId is null; documentIdentifier is not recognized
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    CertificationTypeElement getCertificationTypeByDocId(String userId,
                                                         String documentIdentifier) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException;


    /**
     * Retrieve all of the certification types for a particular title.  The title can include regEx wildcards.
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
    List<CertificationTypeElement> getCertificationTypesByTitle(String userId,
                                                                String title,
                                                                int    startFrom,
                                                                int    pageSize) throws UserNotAuthorizedException,
                                                                                        InvalidParameterException,
                                                                                        PropertyServerException;


    /**
     * Retrieve all of the certification type definitions for a specific governance domain.
     *
     * @param userId calling user
     * @param domainIdentifier identifier to search for
     * @param startFrom where to start from in the list of definitions
     * @param pageSize max number of results to return in one call
     *
     * @return properties of the matching certification type definitions
     *
     * @throws InvalidParameterException domainIdentifier or userId is null; domainIdentifier is not recognized
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    List<CertificationTypeElement> getCertificationTypeByDomainId(String userId,
                                                                  int    domainIdentifier,
                                                                  int    startFrom,
                                                                  int    pageSize) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException;

    /* =======================================
     * Certifications
     */

    /**
     * Link an element to a certification type and include details of the certification in the relationship properties.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the element being certified
     * @param certificationTypeGUID unique identifier for the certification type
     * @param properties the properties of the certification
     *
     * @throws InvalidParameterException one of the properties is invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void certifyElement(String                  userId,
                        String                  elementGUID,
                        String                  certificationTypeGUID,
                        CertificationProperties properties) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException;


    /**
     * Update the properties of a certification.  Remember to include the certificationId in the properties if the element has multiple
     * certifications for the same certification type.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the element being certified
     * @param certificationTypeGUID unique identifier for the certification type
     * @param isMergeUpdate should the supplied properties overlay the existing properties or replace them
     * @param properties the properties of the certification
     *
     * @throws InvalidParameterException one of the properties is invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void updateCertification(String                  userId,
                             String                  elementGUID,
                             String                  certificationTypeGUID,
                             boolean                 isMergeUpdate,
                             CertificationProperties properties)  throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException;


    /**
     * Remove the certification for an element.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the element being certified
     * @param certificationTypeGUID unique identifier for the certification type
     * @param certificateId optional unique identifier from the certification authority - it is used to disambiguate the certifications for the element.
     *
     * @throws InvalidParameterException one of the properties is invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void decertifyElement(String userId,
                          String elementGUID,
                          String certificationTypeGUID,
                          String certificateId)  throws InvalidParameterException,
                                                        UserNotAuthorizedException,
                                                        PropertyServerException;
}
