/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.governanceprogram.client;

import org.odpi.openmetadata.accessservices.governanceprogram.GovernancePolicyMakingInterface;


/**
 * GovernanceProgram provides the client-side interface for the Governance Program Open Metadata Access Service (OMAS).
 * This client, manages all of the interaction with an open metadata repository.  It is initialized with the URL
 * of the server that is running the Open Metadata Access Services.  This server is responsible for locating and
 * managing the governance program definitions exchanged with this client.
 */
public class GovernanceProgram
{
    private String            omasServerURL;  /* Initialized in constructor */

    /**
     * Create a new GovernanceProgram client.
     *
     * @param newServerURL - the network address of the server running the OMAS REST services
     */
    public GovernanceProgram(String     newServerURL)
    {
        omasServerURL = newServerURL;
    }
}
