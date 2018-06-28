/* SPDX-License-Identifier: Apache-2.0 */

package org.odpi.openmetadata.accessservices.governanceengine.client;

import org.odpi.openmetadata.accessservices.governanceengine.common.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.governanceengine.common.objects.GovernanceClassificationDefinition;
import org.odpi.openmetadata.accessservices.governanceengine.common.objects.GovernedAssetComponent;

import java.util.List;

/**
 * The Governance Engine Open Metadata Access Service (OMAS) is used by enforcement engines to retrieve
 * asset and classification information in a suitable form for implementing policies
 *
 * This OMAS is mostly concerned with:
 *   -> A Classification Definition - this tells us what the classification type is, what parms it may have - and can be useful
 *      for authoring and validation
 *   -> An asset , or part (hence we call it an asset component) that has a classification
 *
 *   Furthermore in order to scope queries we restrict by
 *   -> root classification - the node in the classification tree of where we are interested (for example all governance classifications)
 *   -> root type           - the base type that we are interested in
 */

//TODO Propose to extend the rootType to a list so we could get multiple types of asset(components) in a single call

public interface GovernanceEngineClient {
    /**
     * @param userId                                - String - userId of user making request.
     * @param rootClassificationType                - String - name of base classification type (can be null)
     * @param rootType                              - String - root type of asset (can be null)
     * @return AssetTagMap                          - map of classification
     * @throws InvalidParameterException            - one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException           - the requesting user is not authorized to issue this request.
     * @throws RootClassificationNotFoundException  - the classification to scope search is not found
     * @throws RootAssetTypeNotFoundException       - the classification to scope search is not found
     * @throws MetadataServerException              - A failure occurred communicating with the metadata repository
     */
    List<GovernedAssetComponent> getGovernedAssetComponentList(String userId, String rootClassificationType, String rootType)
            throws InvalidParameterException, UserNotAuthorizedException, RootClassificationNotFoundException,
            MetadataServerException, RootAssetTypeNotFoundException;

    /**
     * @param userId                        - String - userId of user making request.
     * @param assetComponentGuid            - String - guid of asset component
     * @return AssetTagMap                  - map of classification
     * @throws InvalidParameterException    - one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException   - the requesting user is not authorized to issue this request.
     * @throws MetadataServerException      - A failure occurred communicating with the metadata repository
     * @throws GuidNotFoundException        - the guid is not found
     */
    GovernedAssetComponent getGovernedAssetComponent(String userId, String assetComponentGuid) throws InvalidParameterException,
            UserNotAuthorizedException, MetadataServerException, GuidNotFoundException;
    /**
     * @param userId                                - String - userId of user making request.
     * @param rootClassificationType                - String - name of base classification type
     * @return AssetTagDefinitions                  - Tag definitions
     * @throws InvalidParameterException            - one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException           - the requesting user is not authorized to issue this request.
     * @throws MetadataServerException              - A failure occurred communicating with the metadata repository
     * @throws RootClassificationNotFoundException  - the classification to scope search is not found
     */
    List<GovernanceClassificationDefinition> getGovernanceClassificationDefinitionList(String userId, String rootClassificationType) throws InvalidParameterException,
            UserNotAuthorizedException, RootClassificationNotFoundException, MetadataServerException;

    /**
     * @param userId                        - String - userId of user making request.
     * @param govClassGuid                  - String - classification guid
     * @return AssetTagDefinitions          - Tag definitions
     * @throws InvalidParameterException    - one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException   - the requesting user is not authorized to issue this request.
     * @throws MetadataServerException      - A failure occurred communicating with the metadata repository
     * @throws GuidNotFoundException        - the guid is not found
     */
    GovernanceClassificationDefinition getGovernanceClassificationDefinition(String userId, String govClassGuid) throws InvalidParameterException,
            UserNotAuthorizedException, MetadataServerException, GuidNotFoundException;


}
