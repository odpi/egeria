/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.repositorygovernance.server;

import org.odpi.openmetadata.adminservices.configuration.registration.EngineServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMESServiceInstanceHandler;

/**
 * RepositoryGovernanceInstanceHandler retrieves information from the instance map for the
 * repository governance engine service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the RepositoryGovernanceAdmin class.
 */
class RepositoryGovernanceInstanceHandler extends OMESServiceInstanceHandler
{
    /**
     * Default constructor registers the access service
     */
    RepositoryGovernanceInstanceHandler()
    {
        super(EngineServiceDescription.REPOSITORY_GOVERNANCE_OMES.getEngineServiceName());

        RepositoryGovernanceRegistration.registerEngineService();
    }
}
