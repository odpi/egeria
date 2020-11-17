/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.api;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

/**
 * The GovernanceExchangeInterface enables the exchange of governance definitions between an external asset manager and open metadata.
 * It includes the exchanges of governance definitions such as GovernancePolicy and GovernanceRule as well as classifications
 * set up by the governance teams such as SubjectArea classification.
 */
public interface GovernanceExchangeInterface
{

    /**
     * Classify the element to asset that the definitions it represents are part of a subject area definition.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryCategoryGUID unique identifier of the metadata element to update
     * @param glossaryCategoryExternalIdentifier unique identifier of the glossary category in the external asset manager
     * @param subjectAreaName qualified name of subject area
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void addElementToSubjectArea(String userId,
                                 String assetManagerGUID,
                                 String assetManagerName,
                                 String glossaryCategoryGUID,
                                 String glossaryCategoryExternalIdentifier,
                                 String subjectAreaName,
                                 String methodName) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException;


    /**
     * Remove the subject area designation from the identified element.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param openMetadataGUID unique identifier of the metadata element to update
     * @param externalElementIdentifier unique identifier of the equivalent element in the external asset manager
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void removeElementFromSubjectArea(String userId,
                                      String assetManagerGUID,
                                      String assetManagerName,
                                      String openMetadataGUID,
                                      String externalElementIdentifier,
                                      String methodName) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException;


}
