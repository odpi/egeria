/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.api;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.CertificationTypeElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedElementStub;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.CertificationProperties;

import java.util.List;

/**
 * The AssetCertificationInterface provides the ability to manage the certification types that can be associated with elements.
 */
public interface AssetCertificationInterface
{
    /* ========================================
     * Certification Types
     */

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
     * Retrieve all the certification types for a particular title.  The title can include regEx wildcards.
     *
     * @param userId calling user
     * @param title identifier of certification
     * @param startFrom where to start from in the list of definitions
     * @param pageSize max number of results to return in one call
     *
     * @return list of matching certification types (null if no matching elements)
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
     * Retrieve all the certification type definitions for a specific governance domain.
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
     * @return unique identifier of the new relationship
     *
     * @throws InvalidParameterException one of the properties is invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    String certifyElement(String                  userId,
                          String                  elementGUID,
                          String                  certificationTypeGUID,
                          CertificationProperties properties) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException;


    /**
     * Update the properties of a certification.
     *
     * @param userId calling user
     * @param certificationGUID unique identifier of the certification relationship being updated
     * @param isMergeUpdate should the supplied properties overlay the existing properties or replace them
     * @param properties the properties of the certification
     *
     * @throws InvalidParameterException one of the properties is invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void updateCertification(String                  userId,
                             String                  certificationGUID,
                             boolean                 isMergeUpdate,
                             CertificationProperties properties)  throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException;


    /**
     * Remove the certification for an element.
     *
     * @param userId calling user
     * @param certificationGUID unique identifier of the certification relationship
     *
     * @throws InvalidParameterException one of the properties is invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void decertifyElement(String userId,
                          String certificationGUID) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException;


    /**
     * Return information about the elements linked to a certification.
     *
     * @param userId calling user
     * @param certificationTypeGUID unique identifier for the certification type
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return properties of the certification
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    List<RelatedElementStub> getCertifiedElements(String userId,
                                                  String certificationTypeGUID,
                                                  int    startFrom,
                                                  int    pageSize) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException;


    /**
     * Return information about the certifications linked to an element.
     *
     * @param userId calling user
     * @param elementGUID unique identifier for the certification
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return properties of the certification
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    List<RelatedElementStub> getCertifications(String userId,
                                               String elementGUID,
                                               int    startFrom,
                                               int    pageSize) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException;
}
