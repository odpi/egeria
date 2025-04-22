/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.api;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedElementStub;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.resources.ResourceListProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.StakeholderProperties;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.List;

/**
 * Defines the interface that is common to multiple element types
 */
public interface RelatedElementsManagementInterface
{
    /**
     * Create a "MoreInformation" relationship between an element that is descriptive and one that is providing the detail.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the element that is descriptive
     * @param properties properties of the relationship
     * @param detailGUID unique identifier of the element that provides the detail
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void setupMoreInformation(String                 userId,
                              String                 elementGUID,
                              RelationshipProperties properties,
                              String                 detailGUID) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException;


    /**
     * Remove a "MoreInformation" relationship between two referenceables.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the element that is descriptive
     * @param detailGUID unique identifier of the element that provides the detail
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void clearMoreInformation(String userId,
                              String elementGUID,
                              String detailGUID) throws InvalidParameterException,
                                                        UserNotAuthorizedException,
                                                        PropertyServerException;


    /**
     * Retrieve the detail elements linked via a "MoreInformation" relationship between two referenceables.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the element that is descriptive
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<RelatedElementStub> getMoreInformation(String userId,
                                                String elementGUID,
                                                int    startFrom,
                                                int    pageSize) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException;


    /**
     * Retrieve the descriptive elements linked via a "MoreInformation" relationship between two referenceables.
     *
     * @param userId calling user
     * @param detailGUID unique identifier of the element that provides the detail
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<RelatedElementStub> getDescriptiveElements(String userId,
                                                    String detailGUID,
                                                    int    startFrom,
                                                    int    pageSize) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException;


    /**
     * Create a "Stakeholder" relationship between an element and its stakeholder.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the element
     * @param properties properties of the relationship
     * @param stakeholderGUID unique identifier of the stakeholder
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void setupStakeholder(String                userId,
                          String                elementGUID,
                          StakeholderProperties properties,
                          String                stakeholderGUID) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException;


    /**
     * Remove a "Stakeholder" relationship between two referenceables.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the element
     * @param stakeholderGUID unique identifier of the stakeholder
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void clearStakeholder(String userId,
                          String elementGUID,
                          String stakeholderGUID) throws InvalidParameterException,
                                                         UserNotAuthorizedException,
                                                         PropertyServerException;


    /**
     * Retrieve the stakeholder elements linked via the "Stakeholder"  relationship between two referenceables.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the element
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<RelatedElementStub> getStakeholders(String userId,
                                             String elementGUID,
                                             int    startFrom,
                                             int    pageSize) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException;


    /**
     * Retrieve the elements commissioned by a stakeholder, linked via the "Stakeholder"  relationship between two referenceables.
     *
     * @param userId calling user
     * @param stakeholderGUID unique identifier of the stakeholder
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<RelatedElementStub> getStakeholderCommissionedElements(String userId,
                                                                String stakeholderGUID,
                                                                int    startFrom,
                                                                int    pageSize) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException;


    /**
     * Create a "ResourceList" relationship between a consuming element and an element that represents resources.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the element
     * @param properties properties of the relationship
     * @param resourceGUID unique identifier of the resource
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void setupResource(String                 userId,
                       String                 elementGUID,
                       ResourceListProperties properties,
                       String                 resourceGUID) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException;


    /**
     * Remove a "ResourceList" relationship between two referenceables.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the element
     * @param resourceGUID unique identifier of the resource
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void clearResource(String userId,
                       String elementGUID,
                       String resourceGUID) throws InvalidParameterException,
                                                   UserNotAuthorizedException,
                                                   PropertyServerException;


    /**
     * Retrieve the list of resources assigned to an element via the "ResourceList" relationship between two referenceables.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the element
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<RelatedElementStub> getResourceList(String userId,
                                             String elementGUID,
                                             int    startFrom,
                                             int    pageSize) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException;


    /**
     * Retrieve the list of elements assigned to a resource via the "ResourceList" relationship between two referenceables.
     *
     * @param userId calling user
     * @param resourceGUID unique identifier of the resource
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<RelatedElementStub> getSupportedByResource(String userId,
                                                    String resourceGUID,
                                                    int    startFrom,
                                                    int    pageSize) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException;


    /**
     * Create a "CatalogTemplate" relationship between a consuming element and a template element.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the element
     * @param templateGUID unique identifier of the template
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void setupCatalogTemplate(String userId,
                              String elementGUID,
                              String templateGUID) throws InvalidParameterException,
                                                          UserNotAuthorizedException,
                                                          PropertyServerException;


    /**
     * Remove a "CatalogTemplate" relationship between two referenceables.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the element
     * @param templateGUID unique identifier of the template
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void clearCatalogTemplate(String userId,
                              String elementGUID,
                              String templateGUID) throws InvalidParameterException,
                                                          UserNotAuthorizedException,
                                                          PropertyServerException;


    /**
     * Retrieve the list of templates assigned to an element via the "CatalogTemplate" relationship.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the element
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<RelatedElementStub> getCatalogTemplateList(String userId,
                                                    String elementGUID,
                                                    int    startFrom,
                                                    int    pageSize) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException;


    /**
     * Retrieve the list of elements assigned to a template via the "CatalogTemplate" relationship.
     *
     * @param userId calling user
     * @param templateGUID unique identifier of the template
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<RelatedElementStub> getSupportedByTemplate(String userId,
                                                    String templateGUID,
                                                    int    startFrom,
                                                    int    pageSize) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException;
}
