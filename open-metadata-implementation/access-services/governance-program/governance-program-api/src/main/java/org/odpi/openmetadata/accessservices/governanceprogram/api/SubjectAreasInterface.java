/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.governanceprogram.api;

import org.odpi.openmetadata.accessservices.governanceprogram.metadataelements.RelatedElement;
import org.odpi.openmetadata.accessservices.governanceprogram.metadataelements.SubjectAreaDefinition;
import org.odpi.openmetadata.accessservices.governanceprogram.metadataelements.SubjectAreaElement;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.SubjectAreaClassificationProperties;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.SubjectAreaProperties;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementStub;

import java.util.List;


/**
 * The SubjectAreasInterface is used by the governance team to define the subject area for topic related governance definitions.
 */
public interface SubjectAreasInterface
{
    /**
     * Create a definition of a subject area.
     *
     * @param userId calling user
     * @param properties properties for a subject area
     *
     * @return unique identifier of subject area
     *
     * @throws InvalidParameterException qualifiedName or userId is null; qualifiedName is not unique
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    String createSubjectArea(String                userId,
                             SubjectAreaProperties properties) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException;

    
    /**
     * Update the definition of a subject area.
     *
     * @param userId calling user
     * @param subjectAreaGUID unique identifier of subject area
     * @param isMergeUpdate are unspecified properties unchanged (true) or replaced with null?
     * @param properties properties to change
     *
     * @throws InvalidParameterException guid, qualifiedName or userId is null; qualifiedName is not unique; guid is not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void updateSubjectArea(String                userId,
                           String                subjectAreaGUID,
                           boolean               isMergeUpdate,
                           SubjectAreaProperties properties) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException;


    /**
     * Remove the definition of a subject area.
     *
     * @param userId calling user
     * @param subjectAreaGUID unique identifier of subject area
     *
     * @throws InvalidParameterException guid or userId is null; guid is not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void deleteSubjectArea(String userId,
                           String subjectAreaGUID) throws InvalidParameterException, 
                                                          UserNotAuthorizedException, 
                                                          PropertyServerException;


    /**
     * Link two related subject areas together as part of a hierarchy.
     * A subject area can only have one parent but many child subject areas.
     *
     * @param userId calling user
     * @param parentSubjectAreaGUID unique identifier of the parent subject area
     * @param childSubjectAreaGUID unique identifier of the child subject area
     *
     * @throws InvalidParameterException one of the guids is null or not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void linkSubjectAreasInHierarchy(String userId,
                                     String parentSubjectAreaGUID,
                                     String childSubjectAreaGUID) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException;


    /**
     * Remove the link between two subject areas in the subject area hierarchy.
     *
     * @param userId calling user
     * @param parentSubjectAreaGUID unique identifier of the parent subject area
     * @param childSubjectAreaGUID unique identifier of the child subject area
     *
     * @throws InvalidParameterException one of the guids is null or not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void unlinkSubjectAreasInHierarchy(String userId,
                                       String parentSubjectAreaGUID,
                                       String childSubjectAreaGUID) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException;


    /**
     * Return information about a specific subject area.
     *
     * @param userId calling user
     * @param subjectAreaGUID unique identifier for the subject area
     *
     * @return properties of the subject area
     *
     * @throws InvalidParameterException subjectAreaGUID or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    SubjectAreaElement getSubjectAreaByGUID(String userId,
                                            String subjectAreaGUID) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException;


    /**
     * Return information about a specific subject area.
     *
     * @param userId calling user
     * @param qualifiedName unique name for the subject area
     *
     * @return properties of the subject area
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    SubjectAreaElement getSubjectAreaByName(String userId,
                                            String qualifiedName) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException;


    /**
     * Return information about the defined subject areas.
     *
     * @param userId calling user
     * @param domainIdentifier identifier for the desired governance domain
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return properties of the subject area
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    List<SubjectAreaElement> getSubjectAreasForDomain(String userId,
                                                      int    domainIdentifier,
                                                      int    startFrom,
                                                      int    pageSize) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException;

    /**
     * Return information about a specific subject area and its linked governance definitions.
     *
     * @param userId calling user
     * @param subjectAreaGUID unique identifier for the subject area
     *
     * @return properties of the subject area
     *
     * @throws InvalidParameterException subjectAreaGUID or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    SubjectAreaDefinition getSubjectAreaDefinitionByGUID(String userId,
                                                         String subjectAreaGUID) throws InvalidParameterException,
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
     * Return information about the contents of a subject area such as the glossaries, reference data sets and quality definitions.
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
