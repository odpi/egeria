/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.samples.glossaryworkflow;

import org.odpi.openmetadata.accessservices.assetmanager.client.management.GlossaryManagementClient;
import org.odpi.openmetadata.accessservices.assetmanager.properties.GlossaryProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.GlossaryTermProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.GlossaryTermStatus;
import org.odpi.openmetadata.http.HttpHelper;


/**
 * GlossaryWorkflowSamples demonstrates different patterns of managing controlled updates to glossary terms.
 * There are 4 patterns:
 *
 * <ul>
 *     <li>Temporary Editing Glossary</li>
 *     <li>Rolling Editing Glossary</li>
 *     <li>Temporary Staging Glossary</li>
 *     <li>Rolling Staging Glossary</li>
 * </ul>
 */
public class GlossaryWorkflowSamples
{
    /**
     * Main program that controls the operation of the sample.  The parameters are passed space separated.
     *
     * @param args 1. server name, 2. URL root for the server, 3. client userId
     */
    public static void main(String[] args)
    {
        String  serverName = "simple-metadata-store";
        String  platformURLRoot = "https://localhost:9443";
        String  clientUserId = "peterprofile";

        if (args != null)
        {
            if (args.length > 0)
            {
                serverName = args[0];
            }

            if (args.length > 2)
            {
                platformURLRoot = args[2];
            }

            if (args.length > 3)
            {
                clientUserId = args[3];
            }
        }

        HttpHelper.noStrictSSL();

        System.out.println("===================================");
        System.out.println("Temporary Editing Glossary Sample  ");
        System.out.println("===================================");
        System.out.println("Running against server: " + serverName + " at " + platformURLRoot);
        System.out.println("Using userId: " + clientUserId);
        System.out.println();

        try
        {
            TemporaryEditingGlossary temporaryEditingGlossary = new TemporaryEditingGlossary(serverName, platformURLRoot, clientUserId);
            temporaryEditingGlossary.run();
        }
        catch (Exception  error)
        {
            System.out.println("Exception: " + error.getClass().getName() + " with message " + error.getMessage());
            System.exit(-1);
        }
    }
}
