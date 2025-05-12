/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.digitalarchitecture.api;


import org.odpi.openmetadata.accessservices.digitalarchitecture.metadataelements.TemplateElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.templates.TemplateClassificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * The ManageTemplates is used to create and maintain templates.
 */
public interface ManageTemplates
{
    /**
     * Classify an asset as suitable to be used as a template for cataloguing assets of a similar types.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the element to classify as a template
     * @param properties properties of the template
     * @param specification values required to use the template
     *
     * @throws InvalidParameterException element not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void addTemplateClassification(String                                 userId,
                                   String                                 elementGUID,
                                   TemplateClassificationProperties       properties,
                                   Map<String, List<Map<String, String>>> specification) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException;


    /**
     * Remove the classification that indicates that this asset can be used as a template.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the element to declassify
     *
     * @throws InvalidParameterException  element not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void removeTemplateClassification(String userId,
                                      String elementGUID) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException;


    /**
     * Return a list of templates for a particular open metadata type of element.
     *
     * @param userId calling user
     * @param typeName type name to query
     * @param effectiveTime optional effective time
     * @param startFrom starting element
     * @param pageSize maximum number of elements
     *
     * @return list of templates
     *
     * @throws InvalidParameterException  element not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    List<TemplateElement> getTemplatesForType(String userId,
                                              String typeName,
                                              Date   effectiveTime,
                                              int    startFrom,
                                              int    pageSize) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException;


    /**
     * Find templates with the requested RegEx in the string properties of the template properties.
     *
     * @param userId calling user
     * @param searchString string to search for (regEx)
     * @param typeName      type name to query (if null, any type is returned)
     * @param effectiveTime optional effective time
     * @param startFrom starting element
     * @param pageSize maximum number of elements
     *
     * @return list of templates
     *
     * @throws InvalidParameterException  element not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    List<TemplateElement> findTemplates(String userId,
                                        String searchString,
                                        String typeName,
                                        Date   effectiveTime,
                                        int    startFrom,
                                        int    pageSize) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException;


    /**
     * Return the templates with the requested name.
     *
     * @param userId calling user
     * @param name name to search for
     * @param typeName      type name to query (if null, any type is returned)
     * @param effectiveTime optional effective time
     * @param startFrom starting element
     * @param pageSize maximum number of elements
     *
     * @return list of templates
     *
     * @throws InvalidParameterException  element not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    List<TemplateElement> getTemplatesByName(String userId,
                                             String name,
                                             String typeName,
                                             Date   effectiveTime,
                                             int    startFrom,
                                             int    pageSize) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException;
}
