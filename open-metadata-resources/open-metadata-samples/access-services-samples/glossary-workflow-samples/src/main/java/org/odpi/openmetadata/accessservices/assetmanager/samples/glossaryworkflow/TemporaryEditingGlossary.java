/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.samples.glossaryworkflow;

import org.odpi.openmetadata.accessservices.assetmanager.client.exchange.GlossaryExchangeClient;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.GlossaryElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.GlossaryTermElement;
import org.odpi.openmetadata.accessservices.assetmanager.properties.TemplateProperties;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementClassification;
import org.odpi.openmetadata.frameworks.openmetadata.properties.glossaries.GlossaryProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.glossaries.GlossaryTermProperties;


/**
 * TemporaryEditingGlossary demonstrates how to use a temporary editing glossary to manage updates to a glossary term so that they
 * are only visible once they have been approved.
 */
public class TemporaryEditingGlossary
{
    private final String serverName;
    private final String platformURLRoot;
    private final String clientUserId;


    /**
     * Set up the parameters for the sample.
     *
     * @param serverName server to call
     * @param serverURLRoot location of server
     * @param clientUserId userId to access the server
     */
    public TemporaryEditingGlossary(String serverName,
                                    String serverURLRoot,
                                    String clientUserId)
    {
        this.serverName = serverName;
        this.platformURLRoot = serverURLRoot;
        this.clientUserId = clientUserId;
    }


    /**
     * This runs the sample.
     *
     * @throws Exception all exceptions are returned to tha caller
     */
    void run() throws Exception
    {
        try
        {
            GlossaryExchangeClient client = new GlossaryExchangeClient(serverName, platformURLRoot, 100);

            System.out.println("\nSetting up the 'live' glossary");
            GlossaryProperties glossaryProperties = new GlossaryProperties();

            glossaryProperties.setQualifiedName("Glossary:Live Glossary");
            glossaryProperties.setDisplayName("Live Glossary");
            glossaryProperties.setDescription("This is the main glossary that is visible to all.");

            String liveGlossaryGUID = client.createGlossary(clientUserId, null, null, false, null, glossaryProperties);

            printGlossary(client, liveGlossaryGUID);

            System.out.println("\nSetting up the editing glossary for the first glossary term");

            glossaryProperties = new GlossaryProperties();

            glossaryProperties.setQualifiedName("Glossary:Editing Glossary 1");
            glossaryProperties.setDisplayName("Editing Glossary 1");
            glossaryProperties.setDescription("This is the first editing glossary that contains the first version of the term.");

            String editingGlossaryGUID = client.createGlossary(clientUserId, null, null, false, null, glossaryProperties);
            client.setGlossaryAsEditingGlossary(clientUserId, null, null, editingGlossaryGUID, null, null, null, false, false);

            printGlossary(client, editingGlossaryGUID);

            System.out.println("\nCreate the glossary term in the editing glossary");
            GlossaryTermProperties glossaryTermProperties = new GlossaryTermProperties();

            glossaryTermProperties.setQualifiedName("GlossaryTerm:Customer Identifier");
            glossaryTermProperties.setDisplayName("Customer Identifier");
            glossaryTermProperties.setSummary("Unique identifier for a customer");
            glossaryTermProperties.setVersionIdentifier("V1.0");

            String glossaryTerm1GUID = client.createControlledGlossaryTerm(clientUserId,
                                                                           null,
                                                                           null,
                                                                           false,
                                                                           editingGlossaryGUID,
                                                                           null,
                                                                           glossaryTermProperties,
                                                                           GlossaryTermStatus.DRAFT,
                                                                           null,
                                                                           false,
                                                                           false);
            printGlossaryTerm(client, glossaryTerm1GUID);

            System.out.println("\nApprove the first version of the glossary term in the editing glossary");
            client.updateGlossaryTermStatus(clientUserId,
                                            null,
                                            null,
                                            glossaryTerm1GUID,
                                            null,
                                            GlossaryTermStatus.APPROVED,
                                            null,
                                            false,
                                            false);
            printGlossaryTerm(client, glossaryTerm1GUID);

            System.out.println("\nPublishing V1 of the term to the 'live' glossary using a move glossary term");
            client.moveGlossaryTerm(clientUserId, null, null, glossaryTerm1GUID, null, liveGlossaryGUID, null, false, false);
            printGlossaryTerm(client, glossaryTerm1GUID);

            System.out.println("\nRemove editing glossary");
            client.removeGlossary(clientUserId, null, null, editingGlossaryGUID, null, true, null, false, false);

            System.out.println("\nSetting up the editing glossary for V2 of the first glossary term");

            glossaryProperties = new GlossaryProperties();

            glossaryProperties.setQualifiedName("Glossary:Editing Glossary 2");
            glossaryProperties.setDisplayName("Editing Glossary 2");
            glossaryProperties.setDescription("This is the second editing glossary that contains the first version of the term.");

            editingGlossaryGUID = client.createGlossary(clientUserId, null, null, false, null, glossaryProperties);
            client.setGlossaryAsEditingGlossary(clientUserId, null, null, editingGlossaryGUID, null, null, null, false, false);
            printGlossary(client, editingGlossaryGUID);

            System.out.println("\nSetting up the private copy of the glossary term in the editing glossary.");
            TemplateProperties templateProperties = new TemplateProperties();
            templateProperties.setQualifiedName("EditingGlossaryTerm:Customer Identifier");

            String editingGlossaryTermGUID = client.createGlossaryTermFromTemplate(clientUserId, null, null, false, editingGlossaryGUID, glossaryTerm1GUID, null,  false,false,  GlossaryTermStatus.DRAFT, templateProperties);
            printGlossaryTerm(client, editingGlossaryTermGUID);

            String updateDescription = "\nAdd description to the private copy of the glossary term in the editing glossary.";
            System.out.println(updateDescription);
            glossaryTermProperties = new GlossaryTermProperties();

            glossaryTermProperties.setDescription("Every person who is a customer needs a unique identifier which is associated with their accounts and other purchased services.");
            glossaryTermProperties.setUsage("The customer identifier is used to retrieve all of the accounts and services for a person.");
            glossaryTermProperties.setAbbreviation("CustId");
            glossaryTermProperties.setVersionIdentifier("V2.0");

            client.updateGlossaryTerm(clientUserId, null, null, editingGlossaryTermGUID, null,true, glossaryTermProperties, updateDescription, null, false, false);
            printGlossaryTerm(client, editingGlossaryTermGUID);

            System.out.println("\nApprove the second version of the glossary term in the editing glossary");
            client.updateGlossaryTermStatus(clientUserId,
                                            null,
                                            null,
                                            editingGlossaryTermGUID,
                                            null,
                                            GlossaryTermStatus.APPROVED,
                                            null,
                                            false,
                                            false);
            printGlossaryTerm(client, editingGlossaryTermGUID);

            updateDescription = "\nPublishing V2 of the term to the 'live' glossary using a move glossary term";
            System.out.println(updateDescription);
            client.updateGlossaryTermFromTemplate(clientUserId, null, null, glossaryTerm1GUID, null, editingGlossaryTermGUID, updateDescription, true, true, null,false, false);
            printGlossaryTerm(client, glossaryTerm1GUID);

            System.out.println("\nRemove second editing glossary");
            client.removeGlossary(clientUserId, null, null, editingGlossaryGUID, null, true, null, false, false);

            printGlossaryTerm(client, glossaryTerm1GUID);

            System.out.println("\nRemove live glossary");
            client.removeGlossary(clientUserId, null, null, liveGlossaryGUID, null, true, null, false, false);
        }
        catch (Exception error)
        {
            System.out.println("Error in temporary editing glossary sample");
            throw error;
        }
    }

    private void printGlossary(GlossaryExchangeClient client, String glossaryGUID) throws Exception
    {
        GlossaryElement glossaryElement = client.getGlossaryByGUID(clientUserId, null, null, glossaryGUID, null, false, false);

        System.out.println("===> Glossary GUID:           " + glossaryElement.getElementHeader().getGUID());
        System.out.println("===> Glossary Qualified Name: " + glossaryElement.getGlossaryProperties().getQualifiedName());
        System.out.println("===> Glossary Display Name:   " + glossaryElement.getGlossaryProperties().getDisplayName());
        System.out.println("===> Glossary Description:    " + glossaryElement.getGlossaryProperties().getDescription());
    }

    private void printGlossaryTerm(GlossaryExchangeClient client, String glossaryTermGUID) throws Exception
    {
        GlossaryTermElement glossaryTermElement = client.getGlossaryTermByGUID(clientUserId, null, null, glossaryTermGUID, null, false, false);

        System.out.println("===> Term GUID:           " + glossaryTermElement.getElementHeader().getGUID());

        if (glossaryTermElement.getElementHeader().getOtherClassifications() != null)
        {
            for (ElementClassification classification : glossaryTermElement.getElementHeader().getOtherClassifications())
            {
                if ("Anchors".equals(classification.getClassificationName()))
                {
                    System.out.println("===> Glossary GUID:       " + classification.getClassificationProperties().get("anchorGUID"));
                }
            }
        }

        System.out.println("===> Term Status          " + glossaryTermElement.getElementHeader().getStatus());
        System.out.println("===> Term Version         " + glossaryTermElement.getGlossaryTermProperties().getVersionIdentifier());
        System.out.println("===> Term Qualified Name: " + glossaryTermElement.getGlossaryTermProperties().getQualifiedName());
        System.out.println("===> Term Display Name:   " + glossaryTermElement.getGlossaryTermProperties().getDisplayName());
        System.out.println("===> Term Summary:        " + glossaryTermElement.getGlossaryTermProperties().getSummary());
        if (glossaryTermElement.getGlossaryTermProperties().getDescription() != null)
        {
            System.out.println("===> Term Description:    " + glossaryTermElement.getGlossaryTermProperties().getDescription());
        }
        if (glossaryTermElement.getGlossaryTermProperties().getUsage() != null)
        {
            System.out.println("===> Term Usage:          " + glossaryTermElement.getGlossaryTermProperties().getUsage());
        }
        if (glossaryTermElement.getGlossaryTermProperties().getAbbreviation() != null)
        {
            System.out.println("===> Term Abbreviation:   " + glossaryTermElement.getGlossaryTermProperties().getAbbreviation());
        }
    }
}
