/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.assetowner.client;


import org.odpi.openmetadata.accessservices.assetowner.AssetOnboardingInterface;


/**
 * AssetOwner provides the client-side interface for the Asset Owner Open Metadata Access Service (OMAS).
 * This client, manages all of the interaction with an open metadata repository.  It is initialized with the URL
 * of the server that is running the Open Metadata Access Services.  This server is responsible for locating and
 * managing the asset owner's definitions exchanged with this client.
 */
public class AssetOwner implements AssetOnboardingInterface
{
    private String            omasServerURL;  /* Initialized in constructor */

    /**
     * Create a new AssetOwner client.
     *
     * @param newServerURL - the network address of the server running the OMAS REST services
     */
    public AssetOwner(String     newServerURL)
    {
        omasServerURL = newServerURL;
    }
}
