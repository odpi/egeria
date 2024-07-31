/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.itinfrastructure.api;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.DeploymentElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.DeploymentProperties;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.Date;
import java.util.List;

/**
 * DeploymentManagementInterface describes the deployment of IT infrastructure assets.
 * An IT infrastructure asset can be deployed to any type of asset.
 */
public interface DeploymentManagementInterface
{
    /**
     * Create a relationship that represents the deployment of an IT infrastructure asset to a specific deployment destination (another asset).
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the infrastructure manager
     * @param infrastructureManagerName unique name of software server capability representing the infrastructure manager
     * @param infrastructureManagerIsHome should the relationship be marked as owned by the infrastructure manager so others can not update?
     * @param itAssetGUID unique identifier of the IT infrastructure asset
     * @param destinationGUID unique identifier of the destination where the asset is being deployed to
     * @param deploymentProperties relationship properties
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void deployITAsset(String               userId,
                       String               infrastructureManagerGUID,
                       String               infrastructureManagerName,
                       boolean              infrastructureManagerIsHome,
                       String               itAssetGUID,
                       String               destinationGUID,
                       DeploymentProperties deploymentProperties) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException;


    /**
     * Update a deployment relationship.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the infrastructure manager
     * @param infrastructureManagerName unique name of software server capability representing the infrastructure manager
     * @param deploymentGUID unique identifier of the relationship
     * @param isMergeUpdate             should the supplied properties be merged with existing properties (true) by replacing just the properties with
     *                                  matching names, or should the entire properties of the instance be replaced?
     * @param deploymentProperties properties for the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void updateITAssetDeployment(String               userId,
                                 String               infrastructureManagerGUID,
                                 String               infrastructureManagerName,
                                 String               deploymentGUID,
                                 boolean              isMergeUpdate,
                                 DeploymentProperties deploymentProperties) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException;


    /**
     * Remove a deployment relationship.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the infrastructure manager
     * @param infrastructureManagerName unique name of software server capability representing the infrastructure manager
     * @param itAssetGUID unique identifier of the IT infrastructure asset
     * @param destinationGUID unique identifier of the destination where the asset is being deployed to
     * @param effectiveTime time when the deployment is effective
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void clearDeployment(String userId,
                         String infrastructureManagerGUID,
                         String infrastructureManagerName,
                         String itAssetGUID,
                         String destinationGUID,
                         Date   effectiveTime) throws InvalidParameterException,
                                                      UserNotAuthorizedException,
                                                      PropertyServerException;


    /**
     * Return the list of assets deployed on a particular destination.
     *
     * @param userId calling user
     * @param destinationGUID unique identifier of the destination asset to query
     * @param effectiveTime effective time for the query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<DeploymentElement> getDeployedITAssets(String userId,
                                                String destinationGUID,
                                                Date   effectiveTime,
                                                int    startFrom,
                                                int    pageSize) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException;



    /**
     * Return the list of destinations that a particular IT infrastructure asset is deployed to.
     *
     * @param userId calling user
     * @param itAssetGUID unique identifier of the IT infrastructure asset to query
     * @param effectiveTime effective time for the query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<DeploymentElement> getDeploymentDestinations(String userId,
                                                      String itAssetGUID,
                                                      Date   effectiveTime,
                                                      int    startFrom,
                                                      int    pageSize) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException;
}
