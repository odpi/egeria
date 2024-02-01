/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.stewardshipaction.api;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.surveyaction.properties.Annotation;
import org.odpi.openmetadata.frameworks.surveyaction.properties.AnnotationStatus;
import org.odpi.openmetadata.frameworks.surveyaction.properties.SurveyReport;

import java.util.List;

/**
 * The SurveyReportInterface is used by the steward to review the survey reports associated with an asset.
 */
public interface SurveyReportInterface
{
    /**
     * Return the survey reports linked to the asset.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the asset
     * @param startFrom position in the list (used when there are so many reports that paging is needed)
     * @param pageSize maximum number of elements to return an this call
     * @return list of discovery analysis reports
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem that occurred within the property server.
     */
    List<SurveyReport> getSurveyReports(String  userId,
                                        String  assetGUID,
                                        int     startFrom,
                                        int     pageSize) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException;


    /**
     * Return the annotations linked directly to the report.
     *
     * @param userId identifier of calling user
     * @param reportGUID identifier of the discovery request.
     * @param startFrom initial position in the stored list.
     * @param pageSize maximum number of definitions to return on this call.
     *
     * @return list of annotations
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem that occurred within the property server.
     */
    List<Annotation> getSurveyReportAnnotations(String           userId,
                                                String           reportGUID,
                                                int              startFrom,
                                                int              pageSize) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException;


    /**
     * Return any annotations attached to this annotation.
     *
     * @param userId identifier of calling user
     * @param annotationGUID anchor annotation
     * @param startFrom starting position in the list
     * @param pageSize maximum number of annotations that can be returned.
     *
     * @return list of Annotation objects
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem that occurred within the property server.
     */
    List<Annotation>  getExtendedAnnotations(String           userId,
                                             String           annotationGUID,
                                             int              startFrom,
                                             int              pageSize) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException;
}
