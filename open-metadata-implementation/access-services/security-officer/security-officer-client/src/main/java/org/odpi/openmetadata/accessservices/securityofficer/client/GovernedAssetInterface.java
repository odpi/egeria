/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.securityofficer.client;

import org.odpi.openmetadata.accessservices.securityofficer.api.model.GovernedAsset;
import org.odpi.openmetadata.accessservices.securityofficer.api.model.SoftwareServerCapability;
import org.odpi.openmetadata.accessservices.securityofficer.api.model.rest.SoftwareServerCapabilityRequestBody;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.List;

/**
 * The Security Officer Open Metadata Access Service (OMAS) is used by enforcement engines to retrieve
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
public interface GovernedAssetInterface
{

    /**
     * Returns a list of the entities filtered by types that have the searched governed classification.
     *
     * @param userId         - userId of user making request.
     * @param classification - name of base classification type (can be null)
     * @param entityTypes    - String - root type of asset (can be null)
     * @param offset         - offset of full collection to begin the return results
     * @param pageSize       - limit the number of the results returned
     * @return a list of the governed entities that have the Governed Classification Searched.
     * @throws UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     * @throws PropertyServerException    - there is a problem retrieving information from the property server(s).
     * @throws InvalidParameterException  - one of the parameters is null or invalid.
     */
    List<GovernedAsset> getGovernedAssetList(String userId, String classification, List<String> entityTypes, Integer offset, Integer pageSize)
            throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException;

    /**
     * @param userId    - String - userId of user making request.
     * @param assetGuid - String - guid of asset component
     * @return the entity if this has a governed classification assigned
     * @throws UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     * @throws PropertyServerException    - there is a problem retrieving information from the property server(s).
     * @throws InvalidParameterException  - one of the parameters is null or invalid.
     */
    GovernedAsset getGovernedAsset(String userId, String assetGuid)
            throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException;

    /**
     * Create a Software Server Capability entity
     *
     * @param userId                   - String - userId of user making request.
     * @param softwareServerCapability - SoftwareServerCapabilityRequestBody
     * @return the GUID of the Software Server entity created
     * @throws UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     * @throws PropertyServerException    - there is a problem retrieving information from the property server(s).
     * @throws InvalidParameterException  - one of the parameters is null or invalid.
     */
    String createSoftwareServerCapability(String userId, SoftwareServerCapabilityRequestBody softwareServerCapability)
            throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException;

    /**
     * Returns the Software Server Capability entity by global identifier
     *
     * @param userId - the name of the calling user
     * @param guid   - guid of the software server
     * @return the Software Server Capability entity associated with the provided GUID
     * @throws UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     * @throws PropertyServerException    - there is a problem retrieving information from the property server(s).
     * @throws InvalidParameterException  - one of the parameters is null or invalid.
     */
    SoftwareServerCapability getSoftwareServerCapabilityByGUID(String userId, String guid)
            throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException;
}
