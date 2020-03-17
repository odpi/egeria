/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceengine.client;

import org.odpi.openmetadata.accessservices.governanceengine.api.objects.GovernedAsset;
import org.odpi.openmetadata.accessservices.governanceengine.api.objects.SoftwareServerCapability;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.List;

/**
 * The Governance Engine Open Metadata Access Service (OMAS) is used by enforcement engines to retrieve
 * asset and classification information in a suitable form for implementing policies
 * <p>
 * This OMAS is mostly concerned with:
 * - A Classification Definition - this tells us what the classification type is, what parms it may have - and can be useful
 * for authoring and validation
 * - An asset , or part (hence we call it an asset component) that has a classification
 * <p>
 * Furthermore in order to scope queries we restrict by
 * - root classification - the node in the classification tree of where we are interested (for example all governance classifications)
 * - root type           - the base type that we are interested in
 */
public interface GovernanceEngineInterface {
    /**
     * @param userId         - userId of user making request.
     * @param classification - name of base classification type (can be null)
     * @param entityTypes    - String - root type of asset (can be null)
     * @return GovernedAsset - list of the governed assets
     */
    List<GovernedAsset> getGovernedAssetList(String userId, String classification, List<String> entityTypes, Integer offset, Integer pageSize)
            throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException;

    /**
     * @param userId    - String - userId of user making request.
     * @param assetGuid - String - guid of asset component
     * @return AssetTagMap                  - map of classification
     * @throws InvalidParameterException  - one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    GovernedAsset getGovernedAsset(String userId, String assetGuid)
            throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException;


    String createSoftwareServerCapability(String userId, SoftwareServerCapability softwareServerCapability)
            throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException;

    SoftwareServerCapability getSoftwareServerCapabilityByGUID(String userId, String guid)
            throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException;
}
