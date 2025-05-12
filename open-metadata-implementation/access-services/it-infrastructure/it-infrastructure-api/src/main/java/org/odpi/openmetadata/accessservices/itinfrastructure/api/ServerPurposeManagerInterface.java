/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.itinfrastructure.api;

import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;

import java.util.Date;
import java.util.Map;

/**
 * ServerPurposeManagerInterface manages the server purpose classifications on the IT Infrastructure assets.
 */
public interface ServerPurposeManagerInterface
{

    /**
     * Add a Server Purpose classification to an IT asset.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the infrastructure manager
     * @param infrastructureManagerName unique name of software server capability representing the infrastructure manager
     * @param infrastructureManagerIsHome ensure that only the infrastructure manager can update this asset
     * @param itAssetGUID unique identifier of the asset
     * @param classificationName name of the classification type
     * @param effectiveFrom when should relationship be effective - null means immediately
     * @param effectiveTo when should relationship no longer be effective - null means never
     * @param classificationProperties properties
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void addServerPurpose(String              userId,
                          String              infrastructureManagerGUID,
                          String              infrastructureManagerName,
                          boolean             infrastructureManagerIsHome,
                          String              itAssetGUID,
                          String              classificationName,
                          Date effectiveFrom,
                          Date                effectiveTo,
                          Map<String, Object> classificationProperties) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException;


    /**
     * Update the properties of a classification for an asset.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the infrastructure manager
     * @param infrastructureManagerName unique name of software server capability representing the infrastructure manager
     * @param assetTypeName name of type for the asset
     * @param assetGUID unique identifier of the asset
     * @param classificationName name of the classification type
     * @param effectiveFrom when should relationship be effective - null means immediately
     * @param effectiveTo when should relationship no longer be effective - null means never
     * @param isMergeUpdate   should the supplied properties be merged with existing properties (true) by replacing just the properties with
     *                                  matching names, or should the entire properties of the instance be replaced?
     * @param classificationProperties properties
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void updateServerPurpose(String              userId,
                             String              infrastructureManagerGUID,
                             String              infrastructureManagerName,
                             String              assetTypeName,
                             String              assetGUID,
                             String              classificationName,
                             Date                effectiveFrom,
                             Date                effectiveTo,
                             boolean             isMergeUpdate,
                             Map<String, Object> classificationProperties) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException;


    /**
     * Remove a server purpose classification.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the infrastructure manager
     * @param infrastructureManagerName unique name of software server capability representing the infrastructure manager
     * @param assetTypeName name of type for the asset
     * @param assetGUID unique identifier of the asset
     * @param classificationName name of the classification type
     * @param effectiveTime effective time of the classification to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void clearServerPurpose(String userId,
                            String infrastructureManagerGUID,
                            String infrastructureManagerName,
                            String assetTypeName,
                            String assetGUID,
                            String classificationName,
                            Date   effectiveTime) throws InvalidParameterException,
                                                         UserNotAuthorizedException,
                                                         PropertyServerException;
}
