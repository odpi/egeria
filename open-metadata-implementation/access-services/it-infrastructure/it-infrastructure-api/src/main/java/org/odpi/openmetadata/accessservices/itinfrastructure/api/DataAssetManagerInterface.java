/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.itinfrastructure.api;



import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.DataAssetElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.DataAssetProperties;
import org.odpi.openmetadata.accessservices.itinfrastructure.properties.TemplateProperties;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.Date;
import java.util.List;

/**
 * DataAssetExchangeInterface provides methods to define data assets.
 */
public interface DataAssetManagerInterface
{

    /*
     * The Data Asset entity is the top level element to describe a data source such as a data store or data set.
     */

    /**
     * Create a new metadata element to represent the root of an asset.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the caller
     * @param infrastructureManagerName unique name of software server capability representing the caller
     * @param infrastructureManagerIsHome ensure that only the infrastructure manager can update this asset
     * @param assetProperties properties to store
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    String createDataAsset(String              userId,
                           String              infrastructureManagerGUID,
                           String              infrastructureManagerName,
                           boolean             infrastructureManagerIsHome,
                           DataAssetProperties assetProperties) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException;


    /**
     * Create a new metadata element to represent an asset using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new asset.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the caller
     * @param infrastructureManagerName unique name of software server capability representing the caller
     * @param infrastructureManagerIsHome ensure that only the infrastructure manager can update this asset
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    String createDataAssetFromTemplate(String             userId,
                                       String             infrastructureManagerGUID,
                                       String             infrastructureManagerName,
                                       boolean            infrastructureManagerIsHome,
                                       String             templateGUID,
                                       TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException;


    /**
     * Update the metadata element representing an asset.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the caller
     * @param infrastructureManagerName unique name of software server capability representing the caller
     * @param assetGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param assetProperties new properties for this element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void updateDataAsset(String              userId,
                         String              infrastructureManagerGUID,
                         String              infrastructureManagerName,
                         String              assetGUID,
                         boolean             isMergeUpdate,
                         DataAssetProperties assetProperties) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException;


    /**
     * Classify the asset to indicate that it can be used as reference data.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the caller
     * @param infrastructureManagerName unique name of software server capability representing the caller
     * @param assetGUID unique identifier of the metadata element to update
     * @param effectiveFrom time when this hosting is effective - null means immediately
     * @param effectiveTo time when this hosting is no longer effective - null means forever
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void setDataAssetAsReferenceData(String userId,
                                     String infrastructureManagerGUID,
                                     String infrastructureManagerName,
                                     String assetGUID,
                                     Date   effectiveFrom,
                                     Date   effectiveTo) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException;


    /**
     * Remove the reference data designation from the asset.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the caller
     * @param infrastructureManagerName unique name of software server capability representing the caller
     * @param assetGUID unique identifier of the metadata element to update
     * @param effectiveTime time when the classification is effective
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void clearDataAssetAsReferenceData(String userId,
                                       String infrastructureManagerGUID,
                                       String infrastructureManagerName,
                                       String assetGUID,
                                       Date   effectiveTime) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException;


    /**
     * Update the zones for the asset so that it becomes visible to consumers.
     * (The zones are set to the list of zones in the publishedZones option configured for each
     * instance of the IT Infrastructure OMAS).
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the metadata element to publish
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void publishDataAsset(String userId,
                          String assetGUID) throws InvalidParameterException,
                                                   UserNotAuthorizedException,
                                                   PropertyServerException;


    /**
     * Update the zones for the asset so that it is no longer visible to consumers.
     * (The zones are set to the list of zones in the defaultZones option configured for each
     * instance of the IT Infrastructure OMAS.  This is the setting when the database is first created).
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the metadata element to withdraw
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void withdrawDataAsset(String userId,
                           String assetGUID) throws InvalidParameterException,
                                                    UserNotAuthorizedException,
                                                    PropertyServerException;


    /**
     * Remove the metadata element representing an asset.  This will delete the asset and all anchored
     * elements such as schema and comments.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the caller
     * @param infrastructureManagerName unique name of software server capability representing the caller
     * @param assetGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void removeDataAsset(String userId,
                         String infrastructureManagerGUID,
                         String infrastructureManagerName,
                         String assetGUID) throws InvalidParameterException,
                                                  UserNotAuthorizedException,
                                                  PropertyServerException;


    /**
     * Retrieve the list of asset metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param searchString string to find in the properties
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
    List<DataAssetElement> findDataAssets(String userId,
                                          String searchString,
                                          Date   effectiveTime,
                                          int    startFrom,
                                          int    pageSize) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException;



    /**
     * Retrieve the list of asset metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param name name to search for
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
    List<DataAssetElement> getDataAssetsByName(String userId,
                                               String name,
                                               Date   effectiveTime,
                                               int    startFrom,
                                               int    pageSize) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException;


    /**
     * Retrieve the list of assets created on behalf of the named infrastructure manager.
     *
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the caller
     * @param infrastructureManagerName unique name of software server capability representing the caller
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
    List<DataAssetElement> getDataAssetsForInfrastructureManager(String userId,
                                                                 String infrastructureManagerGUID,
                                                                 String infrastructureManagerName,
                                                                 Date   effectiveTime,
                                                                 int    startFrom,
                                                                 int    pageSize) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException;


    /**
     * Retrieve the asset metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param guid unique identifier of the requested metadata element
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    DataAssetElement getDataAssetByGUID(String userId,
                                        String guid) throws InvalidParameterException,
                                                            UserNotAuthorizedException,
                                                            PropertyServerException;
}
