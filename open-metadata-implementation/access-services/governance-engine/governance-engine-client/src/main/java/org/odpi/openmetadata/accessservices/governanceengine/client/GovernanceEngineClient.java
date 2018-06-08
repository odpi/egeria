/* SPDX-License-Identifier: Apache-2.0 */

package org.odpi.openmetadata.accessservices.governanceengine.client;


import org.odpi.openmetadata.accessservices.governanceengine.common.ffdc.exceptions.PropertyServerException;
import org.odpi.openmetadata.accessservices.governanceengine.common.ffdc.exceptions.RootClassificationNotFoundException;
import org.odpi.openmetadata.accessservices.governanceengine.common.ffdc.exceptions.UserNotAuthorizedException;
import org.odpi.openmetadata.accessservices.governanceengine.common.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.governanceengine.common.objects.GovernanceClassificationDefinition;
import org.odpi.openmetadata.accessservices.governanceengine.common.objects.GovernedAssetComponent;

import java.util.List;

/**
 * The Governance Engine Open Metadata Access Service (OMAS) is used by enforcement engines to retrieve
 * asset and classification information in a suitable form for implementing policies
 */
public interface GovernanceEngineClient {
    /**
     * @param userId                 - String - userId of user making request.
     * @param rootClassificationType - String - name of base classification type
     * @param rootType - String - root type of asset
     * @return AssetTagMap - map of classification
     * @throws InvalidParameterException  - one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    List<GovernedAssetComponent> getGovernedAssetComponentList(String userId, String rootClassificationType, String rootType) throws InvalidParameterException,
            UserNotAuthorizedException, RootClassificationNotFoundException, PropertyServerException;

    /**
     * @param userId                 - String - userId of user making request.
     * @param assetComponentGuid - String - guid of asset component
     * @return AssetTagMap - map of classification
     * @throws InvalidParameterException  - one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    GovernedAssetComponent getGovernedAssetComponent(String userId, String assetComponentGuid) throws InvalidParameterException,
            UserNotAuthorizedException, RootClassificationNotFoundException, PropertyServerException;
    /**
     * @param userId                 - String - userId of user making request.
     * @param rootClassificationType - String - name of base classification type
     * @return AssetTagDefinitions - Tag definitions
     * @throws InvalidParameterException  - one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    List<GovernanceClassificationDefinition> getGovernanceClassificationDefinitionList(String userId, String rootClassificationType) throws InvalidParameterException,
            UserNotAuthorizedException, RootClassificationNotFoundException, PropertyServerException;

    /**
     * @param userId                 - String - userId of user making request.
     * @param govClassGuid - String - classification guid
     * @return AssetTagDefinitions - Tag definitions
     * @throws InvalidParameterException  - one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    GovernanceClassificationDefinition getGovernanceClassificationDefinition(String userId, String govClassGuid) throws InvalidParameterException,
            UserNotAuthorizedException, RootClassificationNotFoundException, PropertyServerException;


}
