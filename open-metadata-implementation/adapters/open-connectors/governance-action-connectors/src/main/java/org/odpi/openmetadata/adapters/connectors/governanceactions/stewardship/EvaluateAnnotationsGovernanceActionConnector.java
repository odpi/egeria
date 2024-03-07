/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.governanceactions.stewardship;

import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.governanceaction.GeneralGovernanceActionService;
import org.odpi.openmetadata.frameworks.governanceaction.VerificationGovernanceActionService;
import org.odpi.openmetadata.frameworks.governanceaction.properties.CompletionStatus;
import org.odpi.openmetadata.frameworks.governanceaction.properties.NewActionTarget;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * EvaluateAnnotationsGovernanceActionConnector is currently a placeholder for a governance action service
 * that will look through the annotations from a survey report and set up guards to drive actions that
 * process the different types.
 */
public class EvaluateAnnotationsGovernanceActionConnector extends GeneralGovernanceActionService
{
    /**
     * Default constructor
     */
    public EvaluateAnnotationsGovernanceActionConnector()
    {
    }


    /**
     * Indicates that the governance action service is completely configured and can begin processing.
     * This is a standard method from the Open Connector Framework (OCF) so
     * be sure to call super.start() at the start of your overriding version.
     *
     * @throws ConnectorCheckedException there is a problem within the governance action service.
     */
    @Override
    public void start() throws ConnectorCheckedException
    {
        final String methodName = "start";

        super.start();

        List<String>          outputGuards         = new ArrayList<>();
        CompletionStatus      completionStatus     = null;
    }
}
