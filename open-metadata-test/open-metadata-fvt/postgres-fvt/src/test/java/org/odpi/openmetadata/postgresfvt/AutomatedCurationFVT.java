/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.postgresfvt;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.adapters.connectors.controls.PostgresDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.postgres.controls.PostgreSQLTemplateType;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeleteMethod;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.viewservices.automatedcuration.properties.CatalogTemplate;
import org.odpi.openmetadata.viewservices.automatedcuration.properties.ResourceDescription;
import org.odpi.openmetadata.viewservices.automatedcuration.properties.TechnologyTypeReport;
import org.odpi.openmetadata.viewservices.automatedcuration.properties.TechnologyTypeSummary;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Exercises the Automated Curation OMVS against the PostgreSQL content pack - the read half of the API, plus
 * the direct "create from template" call.
 * <br>
 * This is the part of the API that makes a content pack usable by someone who did not write it.  A curator
 * does not begin by knowing that a governance action process called
 * "PostgreSQLServer::CreateAsCatalogTargetGovernanceActionProcess" exists; they begin by asking what Egeria
 * knows about PostgreSQL and being told what it can do with one.  So these tests ask the questions in that
 * order, and check that the answers are complete enough to act on: the technology type is findable by search,
 * its report names the catalog templates and governance action processes that the rest of this suite goes on to
 * run, and a template named in that report can be used exactly as the report describes it.
 * <br>
 * The tests that <em>run</em> those processes are in {@link PostgresServerSurveyFVT} and
 * {@link PostgresServerCatalogFVT}.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class AutomatedCurationFVT
{
    /**
     * The technology type this suite works with throughout: "PostgreSQL Server".
     */
    private static final String POSTGRES_SERVER_TECHNOLOGY_TYPE =
            PostgresDeployedImplementationType.POSTGRESQL_SERVER.getDeployedImplementationType();


    /**
     * A search for "PostgreSQL" finds the technology types the content pack defines.
     * <br>
     * Every deployed implementation type in the pack is expected back, not just some of them: this is the call
     * a curator uses to find out what is available, and a type that is defined but not findable is one nobody
     * will ever ask for.
     *
     * @throws Exception the view service reported a problem
     */
    @Test
    @DisplayName("Searching for PostgreSQL technology types finds the ones the content pack defines")
    public void testFindTechnologyTypes() throws Exception
    {
        List<TechnologyTypeSummary> found = new AutomatedCurationClient().findTechnologyTypes("PostgreSQL");

        assertFalse(found.isEmpty(),
                    "The Automated Curation service found no technology types matching 'PostgreSQL' - the PostgreSQL content"
                            + " pack defines several, so either it did not load or the search is not reaching it.");

        List<String> foundNames = new ArrayList<>();

        for (TechnologyTypeSummary summary : found)
        {
            foundNames.add(summary.getDisplayName());
        }

        for (PostgresDeployedImplementationType technologyType : PostgresDeployedImplementationType.values())
        {
            assertTrue(foundNames.contains(technologyType.getDeployedImplementationType()),
                       "Technology type '" + technologyType.getDeployedImplementationType()
                               + "' is defined by the PostgreSQL content pack but a search for 'PostgreSQL' did not find it."
                               + "  The search found: " + foundNames);
        }
    }


    /**
     * The report for "PostgreSQL Server" names everything a curator would need in order to catalogue one.
     * <br>
     * Three things are checked, and each is a different kind of "the pack is wired up":
     * <ul>
     *     <li>a <b>catalog template</b> for the server, so the asset can be created without hand-building its
     *     connection and endpoint;</li>
     *     <li>the <b>governance action processes</b> that act on it - create-and-survey, create-as-catalog-target
     *     and delete - because those are the automation the pack exists to offer;</li>
     *     <li>a <b>resource list</b> naming the connectors and services that support this technology, which is
     *     how the pack says "this is what will do the work".</li>
     * </ul>
     *
     * @throws Exception the view service reported a problem
     */
    @Test
    @DisplayName("The PostgreSQL Server technology type report names its templates, processes and connectors")
    public void testTechnologyTypeReport() throws Exception
    {
        TechnologyTypeReport report = new AutomatedCurationClient().getTechnologyTypeDetail(POSTGRES_SERVER_TECHNOLOGY_TYPE);

        assertEquals(POSTGRES_SERVER_TECHNOLOGY_TYPE,
                     report.getDisplayName(),
                     "The Automated Curation service returned a report for a different technology type.");

        /*
         * The catalog template - identified by the GUID the connector module itself declares, so that this
         * check fails if the pack and the connector ever disagree about which template is the server template.
         */
        assertNotNull(report.getCatalogTemplates(),
                      "The PostgreSQL Server technology type has no catalog templates, so there is no supported way to"
                              + " catalogue one.");

        List<String> templateGUIDs = new ArrayList<>();

        for (CatalogTemplate catalogTemplate : report.getCatalogTemplates())
        {
            templateGUIDs.add(catalogTemplate.getTemplateGUID());
        }

        assertTrue(templateGUIDs.contains(PostgreSQLTemplateType.POSTGRES_SERVER_TEMPLATE.getTemplateGUID()),
                   "The PostgreSQL Server technology type does not offer the catalog template that"
                           + " PostgreSQLTemplateType.POSTGRES_SERVER_TEMPLATE declares.  It offers: " + templateGUIDs);

        /*
         * The governance action processes.  These are matched by GUID rather than by name: the report gives
         * each process as a related element, and its GUID is the only identifier that is certainly there.  The
         * GUIDs are looked up from the qualified names the rest of this suite uses to run them, so this check
         * says exactly what it means - "the processes this suite runs are the ones a curator would be offered".
         */
        assertNotNull(report.getGovernanceActionProcesses(),
                      "The PostgreSQL Server technology type has no governance action processes, so nothing about it can be"
                              + " automated.");

        List<String> offeredProcessGUIDs = new ArrayList<>();

        for (ResourceDescription resourceDescription : report.getGovernanceActionProcesses())
        {
            if ((resourceDescription.getRelatedElement() != null)
                        && (resourceDescription.getRelatedElement().getElementHeader() != null))
            {
                offeredProcessGUIDs.add(resourceDescription.getRelatedElement().getElementHeader().getGUID());
            }
        }

        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();

        for (String processQualifiedName : List.of(PostgresServerSurveyFVT.CREATE_AND_SURVEY_PROCESS,
                                                   PostgresServerCatalogFVT.CREATE_AS_CATALOG_TARGET_PROCESS,
                                                   PostgresServerCatalogFVT.DELETE_ASSET_PROCESS))
        {
            OpenMetadataElement process = openMetadataStore.getMetadataElementByUniqueName(processQualifiedName,
                                                                                           OpenMetadataProperty.QUALIFIED_NAME.name);

            assertNotNull(process, "Governance action process " + processQualifiedName + " is not in the repository.");

            assertTrue(offeredProcessGUIDs.contains(process.getElementGUID()),
                       "Governance action process " + processQualifiedName + " exists but is not offered against technology type '"
                               + POSTGRES_SERVER_TECHNOLOGY_TYPE + "', so a curator looking at PostgreSQL servers would never"
                               + " be shown it.");
        }

        /*
         * The resource list - the connectors and services that support this technology.
         */
        assertNotNull(report.getResourceList(),
                      "The PostgreSQL Server technology type has an empty resource list, so nothing declares that it can work"
                              + " with one.");

        assertFalse(report.getResourceList().isEmpty(),
                    "The PostgreSQL Server technology type has an empty resource list, so nothing declares that it can work"
                            + " with one.");
    }


    /**
     * A catalog template named in the report can be used through the same API, and produces an asset with
     * every placeholder substituted.
     * <br>
     * This is the one place in the suite where an asset is created directly rather than by a governance action.
     * It is worth separating because it isolates the template from everything else: if a governance action
     * process that creates a server fails, this test says whether the template or the process was at fault.
     * <br>
     * The check that matters is the placeholder one.  A template whose placeholders are not substituted does
     * not fail - it produces an asset carrying "~{hostIdentifier}~" where a host name belongs, which is worse
     * than an empty property because it looks like data.
     *
     * @throws Exception the view service reported a problem, or the asset did not arrive as expected
     */
    @Test
    @DisplayName("A PostgreSQL Server can be catalogued directly from its catalog template")
    public void testCreateElementFromTemplate() throws Exception
    {
        String serverName        = PostgresFvtTestSupport.serverUnderTestName("template");
        String qualifiedName     = PostgresFvtTestSupport.serverAssetQualifiedName(serverName);
        String newElementGUID    = null;

        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext(DeleteMethod.PURGE).getOpenMetadataStore();

        try
        {
            Map<String, String> placeholderValues = PostgresFvtTestSupport.serverTemplatePlaceholders(serverName);

            newElementGUID = new AutomatedCurationClient()
                                     .createElementFromTemplate(PostgreSQLTemplateType.POSTGRES_SERVER_TEMPLATE.getTemplateGUID(),
                                                                placeholderValues);

            assertNotNull(newElementGUID, "The Automated Curation service created no element from the PostgreSQL Server template.");

            OpenMetadataElement newElement = openMetadataStore.getMetadataElementByGUID(newElementGUID);

            assertNotNull(newElement, "The element the template call returned cannot be read back from the repository.");

            assertEquals(qualifiedName,
                         PostgresFvtTestSupport.getStringProperty(newElement, OpenMetadataProperty.QUALIFIED_NAME.name),
                         "The element created from the PostgreSQL Server template does not carry the qualified name the"
                                 + " template builds from the serverName placeholder.");

            List<String> survivingPlaceholders = PostgresFvtTestSupport.findPlaceholders("the new " + newElement.getType().getTypeName(),
                                                                                          newElement.getElementProperties());

            assertTrue(survivingPlaceholders.isEmpty(),
                       "The element created from the PostgreSQL Server template still carries unsubstituted placeholders: "
                               + survivingPlaceholders);

            /*
             * The same element should now be visible as an instance of the technology type - which is the call
             * a curator makes to see what has actually been catalogued, as opposed to what could be.
             */
            List<OpenMetadataRootElement> elements = new AutomatedCurationClient()
                                                             .getTechnologyTypeElements(POSTGRES_SERVER_TECHNOLOGY_TYPE);

            boolean found = false;

            for (OpenMetadataRootElement element : elements)
            {
                if ((element.getElementHeader() != null) && newElementGUID.equals(element.getElementHeader().getGUID()))
                {
                    found = true;
                    break;
                }
            }

            assertTrue(found,
                       "The newly catalogued PostgreSQL server is not listed among the elements of technology type '"
                               + POSTGRES_SERVER_TECHNOLOGY_TYPE + "', so it was created without the deployedImplementationType"
                               + " that makes it discoverable.");
        }
        finally
        {
            if (newElementGUID != null)
            {
                PostgresFvtTestSupport.purgeElement(openMetadataStore, newElementGUID);
            }
        }
    }
}
