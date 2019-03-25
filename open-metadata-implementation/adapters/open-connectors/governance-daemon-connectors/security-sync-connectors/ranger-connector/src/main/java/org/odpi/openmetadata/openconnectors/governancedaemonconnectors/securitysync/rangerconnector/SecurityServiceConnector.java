/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.securitysync.rangerconnector;

import org.odpi.openmetadata.accessservices.governanceengine.api.objects.GovernanceClassification;
import org.odpi.openmetadata.accessservices.governanceengine.api.objects.GovernedAsset;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.securitysync.rangerconnector.model.RangerTag;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.securitysync.rangerconnector.model.ResourceTagMapper;

import java.util.List;

public interface SecurityServiceConnector {

    void importTaggedResources(List<GovernedAsset> governedAssets);

    void createAssociationResourceToSecurityTag(GovernedAsset governedAsset);

    void deleteAssociationResourceToSecurityTag(ResourceTagMapper resourceTagMapper);

    void createSecurityTag(GovernanceClassification classification);

    List<RangerTag> getSecurityTags();

    ResourceTagMapper getTagAssociatedWithTheResource(Long id);

    ResourceTagMapper createAssociationResourceToSecurityTag(String tagGUID, String resourceGUID);
}
