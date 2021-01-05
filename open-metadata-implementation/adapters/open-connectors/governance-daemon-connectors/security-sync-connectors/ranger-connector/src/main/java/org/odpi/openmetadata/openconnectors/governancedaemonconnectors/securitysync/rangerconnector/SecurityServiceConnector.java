/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.securitysync.rangerconnector;

import org.odpi.openmetadata.accessservices.securityofficer.api.model.GovernanceClassification;
import org.odpi.openmetadata.accessservices.securityofficer.api.model.GovernedAsset;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.securitysync.rangerconnector.model.RangerSecurityServicePolicies;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.securitysync.rangerconnector.model.RangerServiceResource;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.securitysync.rangerconnector.model.RangerTag;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.securitysync.rangerconnector.model.ResourceTagMapper;

import java.util.List;

public interface SecurityServiceConnector {

    void importTaggedResources(List<GovernedAsset> governedAssets);

    RangerSecurityServicePolicies getSecurityServicePolicies(String serviceName, Long lastKnownVersion);

    RangerServiceResource createResource(GovernedAsset governedAsset);

    RangerServiceResource getResourceByGUID(String resourceGuid);

    void deleteResource(String resourceGuid);

    List<RangerTag> createSecurityTags(GovernanceClassification classification);

    List<ResourceTagMapper> getTagsAssociatedWithTheResource(Long id);

    ResourceTagMapper createAssociationResourceToSecurityTag(String tagGUID, String resourceGUID);

    void deleteAssociationResourceToSecurityTag(ResourceTagMapper resourceTagMapper);
}
