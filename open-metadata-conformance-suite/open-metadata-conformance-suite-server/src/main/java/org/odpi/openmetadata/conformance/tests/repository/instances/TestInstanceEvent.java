/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.tests.repository.instances;

import org.odpi.openmetadata.conformance.tests.repository.RepositoryConformanceTestCase;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceProfileRequirement;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceWorkPad;
import org.odpi.openmetadata.repositoryservices.events.OMRSInstanceEvent;


/**
 * TestInstanceEvent validates that
 */
public class TestInstanceEvent extends RepositoryConformanceTestCase
{

    /**
     * Typical constructor sets up superclass and discovered information needed for tests
     *
     * @param workPad place for parameters and results
     * @param event event to test
     */
    public TestInstanceEvent(RepositoryConformanceWorkPad workPad,
                             OMRSInstanceEvent            event)
    {
        super(workPad,
              RepositoryConformanceProfileRequirement.INSTANCE_NOTIFICATIONS.getProfileId(),
              RepositoryConformanceProfileRequirement.INSTANCE_NOTIFICATIONS.getRequirementId());
    }


    /**
     * Method implemented by the actual test case.
     *
     * @throws Exception something went wrong with the test.
     */
    protected void run() throws Exception
    {
    }
}
