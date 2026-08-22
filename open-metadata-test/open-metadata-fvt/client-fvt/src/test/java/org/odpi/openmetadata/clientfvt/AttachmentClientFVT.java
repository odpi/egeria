/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.clientfvt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ActorProfileClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextBase;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ContributionRecordClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.MultiLanguageClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.TemplateClient;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.ActorProfileProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.templates.TemplateProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.translations.TranslationDetailProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.ContributionRecordProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.DeleteOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.MetadataSourceOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.search.QueryOptions;

import java.util.List;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * AttachmentClientFVT covers the remaining clients that decorate an existing element rather than creating one
 * of their own: a contribution record on an actor profile, a translation on any element, and the template
 * classification.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class AttachmentClientFVT
{
    /**
     * Attach a contribution record to an actor profile, read it back, then remove it.
     *
     * @throws Exception any failure - which is the finding
     */
    @Test
    void contributionRecordClientAttachesAndDetaches() throws Exception
    {
        ConnectorContextBase     connectorContext = ConnectorContextFactory.newContext();
        ActorProfileClient       profileClient    = connectorContext.getActorProfileClient();
        ContributionRecordClient recordClient     = connectorContext.getContributionRecordClient();

        NewElementOptions newElementOptions = new NewElementOptions();

        newElementOptions.setIsOwnAnchor(true);

        ActorProfileProperties profileProperties = new ActorProfileProperties();

        profileProperties.setQualifiedName(ClientFvtTestSupport.newQualifiedName("ContributionHost"));
        profileProperties.setDisplayName("client-fvt contribution host");

        String profileGUID = profileClient.createActorProfile(newElementOptions, null, profileProperties, null);
        String recordGUID  = null;

        try
        {
            ContributionRecordProperties recordProperties = new ContributionRecordProperties();

            recordProperties.setQualifiedName(ClientFvtTestSupport.newQualifiedName("ContributionRecord"));

            recordGUID = recordClient.addContributionRecordToElement(profileGUID, new MetadataSourceOptions(),
                                                                       null, recordProperties, null);

            assertNotNull(recordGUID, "addContributionRecordToElement returned no GUID");
            assertNotNull(recordClient.getContributionRecordByGUID(recordGUID, null),
                          "The contribution record could not be read back after being added");
        }
        finally
        {
            if (recordGUID != null)
            {
                recordClient.deleteContributionRecord(recordGUID, new DeleteOptions());
            }

            FeedbackClientFVT.cleanUpHost(connectorContext, profileGUID);
        }
    }


    /**
     * Set a translation on an element, read it back in that language, then clear it and check it is gone.
     *
     * @throws Exception any failure - which is the finding
     */
    @Test
    void multiLanguageClientSetsAndClearsATranslation() throws Exception
    {
        ConnectorContextBase connectorContext    = ConnectorContextFactory.newContext();
        MultiLanguageClient  multiLanguageClient = connectorContext.getMultiLanguageClient();

        String hostGUID = FeedbackClientFVT.createHostCollection(connectorContext, "Translation");
        String language = "fr";
        String locale   = "FR";

        try
        {
            TranslationDetailProperties translation = new TranslationDetailProperties();

            translation.setLanguage(language);
            translation.setLanguageCode(language);
            translation.setLocale(locale);
            translation.setDisplayName("collection client-fvt en francais");

            multiLanguageClient.setTranslation(hostGUID, null, translation);

            TranslationDetailProperties retrieved = multiLanguageClient.getTranslation(hostGUID, language, locale);

            assertNotNull(retrieved, "getTranslation found nothing after a translation was set");
            assertEquals(language, retrieved.getLanguage(), "The translation came back in a different language");

            // A second translation in the same language but a different locale must be stored and retrieved
            // independently - that is what the locale is for.
            TranslationDetailProperties canadian = new TranslationDetailProperties();

            canadian.setLanguage(language);
            canadian.setLanguageCode(language);
            canadian.setLocale("CA");
            canadian.setDisplayName("collection client-fvt en francais canadien");

            multiLanguageClient.setTranslation(hostGUID, null, canadian);

            assertEquals("collection client-fvt en francais canadien",
                         multiLanguageClient.getTranslation(hostGUID, language, "CA").getDisplayName(),
                         "The Canadian translation did not come back");
            assertEquals("collection client-fvt en francais",
                         multiLanguageClient.getTranslation(hostGUID, language, locale).getDisplayName(),
                         "Adding a second locale changed which translation the first locale returns");

            List<TranslationDetailProperties> allTranslations = multiLanguageClient.getTranslations(hostGUID, 0, 0);

            assertNotNull(allTranslations, "getTranslations returned nothing for an element with two translations");
            assertEquals(2, allTranslations.size(), "getTranslations did not return both translations");

            // Setting the same language and locale again must update in place rather than add a duplicate.
            translation.setDisplayName("collection client-fvt en francais (revise)");
            multiLanguageClient.setTranslation(hostGUID, null, translation);

            assertEquals(2, multiLanguageClient.getTranslations(hostGUID, 0, 0).size(),
                         "Re-setting an existing language/locale added a duplicate instead of updating it");
            assertEquals("collection client-fvt en francais (revise)",
                         multiLanguageClient.getTranslation(hostGUID, language, locale).getDisplayName(),
                         "Re-setting an existing language/locale did not update the stored translation");

            multiLanguageClient.clearTranslation(hostGUID, language, locale);

            assertNull(multiLanguageClient.getTranslation(hostGUID, language, locale),
                       "getTranslation still returns a translation after it was cleared");
            assertNotNull(multiLanguageClient.getTranslation(hostGUID, language, "CA"),
                          "Clearing one locale also removed the translation for another locale");

            multiLanguageClient.clearTranslation(hostGUID, language, "CA");
        }
        finally
        {
            FeedbackClientFVT.cleanUpHost(connectorContext, hostGUID);
        }
    }


    /**
     * Classify an element as a template, find it among the templates, then remove the classification.
     *
     * @throws Exception any failure - which is the finding
     */
    @Test
    void templateClientClassifiesAndDeclassifies() throws Exception
    {
        ConnectorContextBase connectorContext = ConnectorContextFactory.newContext();
        TemplateClient       templateClient   = connectorContext.getTemplateClient();

        String hostGUID     = FeedbackClientFVT.createHostCollection(connectorContext, "Template");
        String templateName = "client-fvt template " + hostGUID;

        try
        {
            TemplateProperties templateProperties = new TemplateProperties();

            templateProperties.setDisplayName(templateName);

            templateClient.addTemplateClassification(hostGUID, templateProperties, new MetadataSourceOptions());

            // The template is found by the displayName on the Template *classification*, not by the entity's
            // own display name - a template's entity properties describe what it will produce and are
            // typically placeholders.
            QueryOptions queryOptions = new QueryOptions();

            queryOptions.setMetadataElementTypeName(OpenMetadataType.COLLECTION.typeName);

            List<OpenMetadataRootElement> templates = templateClient.getTemplatesByName(templateName, queryOptions);

            assertTrue(containsGUID(templates, hostGUID),
                       "getTemplatesByName did not find the element just classified as a template");

            templateClient.removeTemplateClassification(hostGUID, new MetadataSourceOptions());

            assertFalse(containsGUID(templateClient.getTemplatesByName(templateName, queryOptions), hostGUID),
                        "getTemplatesByName still returns the element after its Template classification was removed");
        }
        finally
        {
            FeedbackClientFVT.cleanUpHost(connectorContext, hostGUID);
        }
    }


    /**
     * Does this result list contain the element with this GUID?
     *
     * @param elements results
     * @param elementGUID GUID to look for
     * @return true if present
     */
    private static boolean containsGUID(List<OpenMetadataRootElement> elements, String elementGUID)
    {
        if (elements == null) return false;

        for (OpenMetadataRootElement element : elements)
        {
            if ((element.getElementHeader() != null) && elementGUID.equals(element.getElementHeader().getGUID()))
            {
                return true;
            }
        }

        return false;
    }
}
