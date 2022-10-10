/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.digitalservice.api;

import org.odpi.openmetadata.accessservices.digitalservice.metadataelements.SubjectAreaElement;
import org.odpi.openmetadata.accessservices.digitalservice.properties.SubjectAreaClassificationProperties;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementStub;

import java.util.List;

/**
 * Subject areas group data into topic areas.  This makes it easier to manage common definitions such as
 * glossary terms, reference data and governance definitions that are specific to a certain type of data.
 * The <i>SubjectArea</i> classification can be added to digital services and digital products.
 */
public interface SubjectAreasInterface
{
    /**
     * Return information about a specific subject area.
     *
     * @param userId calling user
     * @param subjectAreaName unique name for the subject area
     *
     * @return properties of the subject area
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    SubjectAreaElement getSubjectAreaByName(String userId,
                                            String subjectAreaName) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException;


    /**
     * Return information about the defined subject areas.
     *
     * @param userId calling user
     * @param domainIdentifier identifier for the desired governance domain - if 0 then all subject areas are returned
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return properties of the subject areas
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    List<SubjectAreaElement> getSubjectAreas(String userId,
                                             int    domainIdentifier,
                                             int    startFrom,
                                             int    pageSize) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException;


    /**
     * Add a subject area classification to a referenceable element.
     *
     * @param userId calling user
     * @param elementGUID unique identifier for the element
     * @param properties identifier for a subject area
     *
     * @throws InvalidParameterException qualifiedName or userId is null; qualifiedName is not unique
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void addSubjectAreaMemberClassification(String                              userId,
                                            String                              elementGUID,
                                            SubjectAreaClassificationProperties properties) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException;


    /**
     * Remove a subject area classification from a referenceable.
     *
     * @param userId calling user
     * @param elementGUID unique identifier for the element
     *
     * @throws InvalidParameterException guid or userId is null; guid is not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void deleteSubjectAreaMemberClassification(String userId,
                                               String elementGUID) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException;


    /**
     * Return information about the contents of a subject area such as the digital services, digital products,
     * glossaries, reference data sets and quality definitions.
     *
     * @param userId calling user
     * @param subjectAreaName unique identifier for the subject area
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return properties of the subject area members
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    List<ElementStub> getMembersOfSubjectArea(String userId,
                                              String subjectAreaName,
                                              int    startFrom,
                                              int    pageSize) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException;
}
