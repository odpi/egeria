/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.samples.glossaryworkflow;

import org.odpi.openmetadata.accessservices.assetmanager.client.management.GlossaryManagementClient;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.GlossaryElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.GlossaryTermElement;
import org.odpi.openmetadata.accessservices.assetmanager.properties.TemplateProperties;
import org.odpi.openmetadata.frameworks.openmetadata.enums.GlossaryTermStatus;
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
            GlossaryManagementClient client = new GlossaryManagementClient(serverName, platformURLRoot, 100);

            System.out.println("\nSetting up the 'live' glossary");
            GlossaryProperties glossaryProperties = new GlossaryProperties();

            glossaryProperties.setQualifiedName("Glossary:Live Glossary");
            glossaryProperties.setDisplayName("Live Glossary");
            glossaryProperties.setDescription("This is the main glossary that is visible to all.");

            String liveGlossaryGUID = client.createGlossary(clientUserId, glossaryProperties);

            printGlossary(client, liveGlossaryGUID);

            System.out.println("\nSetting up the editing glossary for the first glossary term");

            glossaryProperties = new GlossaryProperties();

            glossaryProperties.setQualifiedName("Glossary:Editing Glossary 1");
            glossaryProperties.setDisplayName("Editing Glossary 1");
            glossaryProperties.setDescription("This is the first editing glossary that contains the first version of the term.");

            String editingGlossaryGUID = client.createGlossary(clientUserId, glossaryProperties);
            client.setGlossaryAsEditingGlossary(clientUserId, editingGlossaryGUID, null, null, false, false);

            printGlossary(client, editingGlossaryGUID);

            System.out.println("\nCreate the glossary term in the editing glossary");
            GlossaryTermProperties glossaryTermProperties = new GlossaryTermProperties();

            glossaryTermProperties.setQualifiedName("GlossaryTerm:Customer Identifier");
            glossaryTermProperties.setDisplayName("Customer Identifier");
            glossaryTermProperties.setSummary("Unique identifier for a customer");
            glossaryTermProperties.setPublishVersionIdentifier("V1.0");

            String glossaryTerm1GUID = client.createControlledGlossaryTerm(clientUserId,
                                                                           editingGlossaryGUID,
                                                                           glossaryTermProperties,
                                                                           GlossaryTermStatus.DRAFT,
                                                                           null,
                                                                           false,
                                                                           false);
            printGlossaryTerm(client, glossaryTerm1GUID);

            System.out.println("\nApprove the first version of the glossary term in the editing glossary");
            client.updateGlossaryTermStatus(clientUserId,
                                            glossaryTerm1GUID,
                                            GlossaryTermStatus.APPROVED,
                                            null,
                                            false,
                                            false);
            printGlossaryTerm(client, glossaryTerm1GUID);

            System.out.println("\nPublishing V1 of the term to the 'live' glossary using a move glossary term");
            client.moveGlossaryTerm(clientUserId, glossaryTerm1GUID, liveGlossaryGUID, null, false, false);
            printGlossaryTerm(client, glossaryTerm1GUID);

            System.out.println("\nRemove editing glossary");
            client.removeGlossary(clientUserId, editingGlossaryGUID, null, false, false);

            System.out.println("\nSetting up the editing glossary for V2 of the first glossary term");

            glossaryProperties = new GlossaryProperties();

            glossaryProperties.setQualifiedName("Glossary:Editing Glossary 2");
            glossaryProperties.setDisplayName("Editing Glossary 2");
            glossaryProperties.setDescription("This is the second editing glossary that contains the first version of the term.");

            editingGlossaryGUID = client.createGlossary(clientUserId, glossaryProperties);
            client.setGlossaryAsEditingGlossary(clientUserId, editingGlossaryGUID, null, null, false, false);
            printGlossary(client, editingGlossaryGUID);

            System.out.println("\nSetting up the private copy of the glossary term in the editing glossary.");
            TemplateProperties templateProperties = new TemplateProperties();
            templateProperties.setQualifiedName("EditingGlossaryTerm:Customer Identifier");

            String editingGlossaryTermGUID = client.createGlossaryTermFromTemplate(clientUserId, editingGlossaryGUID, glossaryTerm1GUID, templateProperties, false, false, GlossaryTermStatus.DRAFT);
            printGlossaryTerm(client, editingGlossaryTermGUID);

            System.out.println("\nAdd description to the private copy of the glossary term in the editing glossary.");
            glossaryTermProperties = new GlossaryTermProperties();

            glossaryTermProperties.setDescription("Every person who is a customer needs a unique identifier which is associated with their accounts and other purchased services.");
            glossaryTermProperties.setUsage("The customer identifier is used to retrieve all of the accounts and services for a person.");
            glossaryTermProperties.setAbbreviation("CustId");
            glossaryTermProperties.setPublishVersionIdentifier("V2.0");

            client.updateGlossaryTerm(clientUserId, editingGlossaryTermGUID, true, glossaryTermProperties, null, false, false);
            printGlossaryTerm(client, editingGlossaryTermGUID);

            System.out.println("\nApprove the second version of the glossary term in the editing glossary");
            client.updateGlossaryTermStatus(clientUserId,
                                            editingGlossaryTermGUID,
                                            GlossaryTermStatus.APPROVED,
                                            null,
                                            false,
                                            false);
            printGlossaryTerm(client, editingGlossaryTermGUID);

            System.out.println("\nPublishing V2 of the term to the 'live' glossary using a move glossary term");
            client.updateGlossaryTermFromTemplate(clientUserId, glossaryTerm1GUID, editingGlossaryTermGUID, true, true, null, false, false);
            printGlossaryTerm(client, glossaryTerm1GUID);

            System.out.println("\nRemove second editing glossary");
            client.removeGlossary(clientUserId, editingGlossaryGUID, null, false, false);

            printGlossaryTerm(client, glossaryTerm1GUID);

            System.out.println("\nRemove live glossary");
            client.removeGlossary(clientUserId, liveGlossaryGUID, null, false, false);
        }
        catch (Exception error)
        {
            System.out.println("Error in temporary editing glossary sample");
            throw error;
        }
    }

    private void printGlossary(GlossaryManagementClient client, String glossaryGUID) throws Exception
    {
        GlossaryElement glossaryElement = client.getGlossaryByGUID(clientUserId, glossaryGUID, null, false, false);

        System.out.println("===> Glossary GUID:           " + glossaryElement.getElementHeader().getGUID());
        System.out.println("===> Glossary Qualified Name: " + glossaryElement.getGlossaryProperties().getQualifiedName());
        System.out.println("===> Glossary Display Name:   " + glossaryElement.getGlossaryProperties().getDisplayName());
        System.out.println("===> Glossary Description:    " + glossaryElement.getGlossaryProperties().getDescription());
    }

    private void printGlossaryTerm(GlossaryManagementClient client, String glossaryTermGUID) throws Exception
    {
        GlossaryTermElement glossaryTermElement = client.getGlossaryTermByGUID(clientUserId, glossaryTermGUID, null, false, false);

        System.out.println("===> Term GUID:           " + glossaryTermElement.getElementHeader().getGUID());

        if (glossaryTermElement.getElementHeader().getClassifications() != null)
        {
            for (ElementClassification classification : glossaryTermElement.getElementHeader().getClassifications())
            {
                if ("Anchors".equals(classification.getClassificationName()))
                {
                    System.out.println("===> Glossary GUID:       " + classification.getClassificationProperties().get("anchorGUID"));
                }
            }
        }

        System.out.println("===> Term Status          " + glossaryTermElement.getElementHeader().getStatus());
        System.out.println("===> Term Version         " + glossaryTermElement.getGlossaryTermProperties().getPublishVersionIdentifier());
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
