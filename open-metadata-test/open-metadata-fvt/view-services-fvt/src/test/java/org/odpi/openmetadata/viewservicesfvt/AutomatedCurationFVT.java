/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservicesfvt;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteElementRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.FilterRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.GetRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NameRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewElementRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.ResultsRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.SecretsCollectionRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.TemplateRequestBody;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ActivityStatus;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.NewActionTarget;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.AssetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.filesandfolders.DataFileProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.actions.EngineActionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.ConnectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.EndpointProperties;
import org.odpi.openmetadata.frameworks.opengovernance.properties.EngineActionElement;
import org.odpi.openmetadata.frameworkservices.gaf.rest.EngineActionElementsResponse;
import org.odpi.openmetadata.frameworkservices.gaf.rest.InitiateEngineActionRequestBody;
import org.odpi.openmetadata.metadatasecurity.properties.OpenMetadataSecretsCollection;
import org.odpi.openmetadata.viewservices.automatedcuration.properties.CatalogTemplate;
import org.odpi.openmetadata.viewservices.automatedcuration.properties.TechnologyTypeHierarchy;
import org.odpi.openmetadata.viewservices.automatedcuration.properties.TechnologyTypeReport;
import org.odpi.openmetadata.viewservices.automatedcuration.properties.TechnologyTypeSummary;
import org.odpi.openmetadata.viewservices.automatedcuration.rest.TechnologyTypeHierarchyResponse;
import org.odpi.openmetadata.viewservices.automatedcuration.rest.TechnologyTypeReportResponse;
import org.odpi.openmetadata.viewservices.automatedcuration.rest.TechnologyTypeSummaryListResponse;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * AutomatedCurationFVT covers the Automated Curation OMVS on its own URL marker: the technology type requests,
 * which describe the Core Content Pack's "Data File" technology type and its catalog template; creating an
 * element from that template; saving and deleting client-side secrets in a YAML secrets file that the view
 * server writes; and initiating, listing and cancelling an engine action.
 * <br>
 * None of these needs a running engine host: initiating an engine action only records it, APPROVED, in the
 * metadata store for an engine host to claim, and cancelling it only changes its status.  The engine action
 * runs on the Core Content Pack's Stewardship engine, whose "write-to-audit-log" request type the engine-name
 * validation requires.
 * <br>
 * The two other initiate requests (governance action types and processes) and the process instance cancel
 * need governance action definitions linked by the Action Author, so ActionAuthorFVT covers them.  The engine
 * action lifecycle after it is claimed is in GovernanceEngineFVT.
 */
@ExtendWith(OMAGPlatformExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AutomatedCurationFVT
{
    private static final ViewServiceClient automatedCuration = new ViewServiceClient(ViewServiceDescription.AUTOMATED_CURATION);
    private static final ViewServiceClient assetMaker        = new ViewServiceClient(ViewServiceDescription.ASSET_MAKER);
    private static final ViewServiceClient connectionMaker   = new ViewServiceClient(ViewServiceDescription.CONNECTION_MAKER);

    /*
     * Content from the Core Content Pack.
     */
    static final String DATA_FILE_TECHNOLOGY_TYPE    = "Data File";
    static final String DATA_FILE_TECHNOLOGY_TYPE_QN = "Egeria:ValidMetadataValue:DataFile:deployedImplementationType-(Data File)";
    static final String DATA_FILE_TEMPLATE_GUID      = "ff2a4de1-d8e0-40ab-a5e2-212f06c6a6dd";
    static final String YAML_SECRETS_FILE_CONNECTOR_TYPE_GUID = "178aace3-6286-42dc-9dce-505f6c8b0e76";
    static final String STEWARDSHIP_ENGINE_NAME      = "Stewardship";
    static final String WRITE_TO_AUDIT_LOG           = "write-to-audit-log";

    private static String templatedFileGUID = null;


    /**
     * The "Data File" technology type is found by a search, is listed for its open metadata type, and its
     * report lists the Core Content Pack's catalog template.  The hierarchy request returns it as the root.
     */
    @Test
    @Order(1)
    void technologyTypes()
    {
        SearchStringRequestBody searchRequest = new SearchStringRequestBody();

        searchRequest.setSearchString(DATA_FILE_TECHNOLOGY_TYPE);

        assertTrue(hasTechnologyType(automatedCuration.post("/technology-types/by-search-string",
                                                            searchRequest,
                                                            TechnologyTypeSummaryListResponse.class).getElements()),
                   "findTechnologyTypes did not find " + DATA_FILE_TECHNOLOGY_TYPE_QN);

        /*
         * The request succeeds, but what it returns is checked in technologyTypesForOpenMetadataType, which is
         * disabled by an Egeria bug.
         */
        automatedCuration.post("/open-metadata-types/DataFile/technology-types", new ResultsRequestBody(), TechnologyTypeSummaryListResponse.class);

        TechnologyTypeReport report = automatedCuration.post("/technology-types/by-name",
                                                             filter(DATA_FILE_TECHNOLOGY_TYPE),
                                                             TechnologyTypeReportResponse.class).getElement();

        assertNotNull(report, "getTechnologyTypeDetail returned nothing for " + DATA_FILE_TECHNOLOGY_TYPE);
        assertEquals(DATA_FILE_TECHNOLOGY_TYPE_QN, report.getQualifiedName());
        assertNotNull(report.getCatalogTemplates(), "the Data File technology type report has no catalog templates");

        boolean templateListed = false;

        for (CatalogTemplate catalogTemplate : report.getCatalogTemplates())
        {
            if (DATA_FILE_TEMPLATE_GUID.equals(catalogTemplate.getTemplateGUID()) ||
                    ((catalogTemplate.getRelatedElement() != null) && (catalogTemplate.getRelatedElement().getElementHeader() != null) &&
                             DATA_FILE_TEMPLATE_GUID.equals(catalogTemplate.getRelatedElement().getElementHeader().getGUID())))
            {
                templateListed = true;
            }
        }

        assertTrue(templateListed, "the Data File technology type report does not list template " + DATA_FILE_TEMPLATE_GUID + ": " + report.getCatalogTemplates());

        TechnologyTypeHierarchyResponse hierarchyResponse = automatedCuration.post("/technology-types/hierarchy",
                                                                                   filter(DATA_FILE_TECHNOLOGY_TYPE),
                                                                                   TechnologyTypeHierarchyResponse.class);
        TechnologyTypeHierarchy hierarchy = hierarchyResponse.getElement();

        assertNotNull(hierarchy, "getTechnologyTypeHierarchy returned nothing for " + DATA_FILE_TECHNOLOGY_TYPE);
        assertEquals(DATA_FILE_TECHNOLOGY_TYPE_QN, hierarchy.getQualifiedName());
        assertNotNull(hierarchyResponse.getMermaidGraph(), "getTechnologyTypeHierarchy returned no mermaid graph");
    }


    /**
     * The technology types for the DataFile open metadata type include "Data File".
     * <br>
     * TechnologyTypeHandler.getTechnologyTypesForOpenMetadataType searches for the qualified names with the
     * regular expression "Egeria:ValidMetadataValue:DataFile:deployedImplementationType-.*" and the LIKE
     * operator, but LIKE is a literal "contains" test, so ".*" matches nothing and the request always returns
     * an empty list.
     */
    @Test
    @Order(2)
    void technologyTypesForOpenMetadataType()
    {
        assertTrue(hasTechnologyType(automatedCuration.post("/open-metadata-types/DataFile/technology-types",
                                                            new ResultsRequestBody(),
                                                            TechnologyTypeSummaryListResponse.class).getElements()),
                   "getTechnologyTypesForOpenMetadataType(DataFile) did not list " + DATA_FILE_TECHNOLOGY_TYPE_QN);
    }


    /**
     * An element created from the Data File catalog template takes its placeholder values, carries the
     * technology type as its deployed implementation type and so is listed as one of the technology type's
     * elements.
     */
    @Test
    @Order(3)
    void createElementFromTemplate()
    {
        String namespacePath = ViewServicesFvtTestSupport.runSearchString() + "::automated-curation";
        String resourceName  = "templated-file.csv";

        Map<String, String> placeholders = new HashMap<>();

        placeholders.put("namespacePath", namespacePath);
        placeholders.put("resourceName", resourceName);
        placeholders.put("displayName", "Templated file");
        placeholders.put("description", "Created from a template by " + AutomatedCurationFVT.class.getSimpleName());
        placeholders.put("filePathName", "/data/" + resourceName);
        placeholders.put("versionIdentifier", "V1.0");

        TemplateRequestBody requestBody = new TemplateRequestBody();

        requestBody.setTemplateGUID(DATA_FILE_TEMPLATE_GUID);
        requestBody.setIsOwnAnchor(true);
        requestBody.setPlaceholderPropertyValues(placeholders);

        templatedFileGUID = automatedCuration.create("/catalog-templates/new-element", requestBody);

        OpenMetadataRootElement file = assetMaker.element("/assets/" + templatedFileGUID + "/retrieve", new GetRequestBody());

        assertNotNull(file, "the element created from the template is missing");

        DataFileProperties properties = assertInstanceOf(DataFileProperties.class, file.getProperties());

        assertEquals("Data File::" + namespacePath + "::" + resourceName, properties.getQualifiedName());
        assertEquals("Templated file", properties.getDisplayName());
        assertEquals(DATA_FILE_TECHNOLOGY_TYPE, properties.getDeployedImplementationType());

        List<OpenMetadataRootElement> technologyTypeElements = automatedCuration.elements("/technology-types/elements",
                                                                                          filter(DATA_FILE_TECHNOLOGY_TYPE));

        assertTrue(technologyTypeElements.stream().anyMatch(element -> templatedFileGUID.equals(element.getElementHeader().getGUID())),
                   "getTechnologyTypeElements(Data File) does not include the element created from the template");

        assetMaker.call("/assets/" + templatedFileGUID + "/delete", new DeleteElementRequestBody());
        ViewServicesFvtTestSupport.assertGone(assetMaker, "/assets/" + templatedFileGUID + "/retrieve");
    }


    /**
     * Client-side secrets are written to the secrets store asset's YAML file through its connector, and
     * deleting one removes it from the file while the others stay.
     * <br>
     * The connection names a secretsCollectionName configuration property only to get round the bug that
     * clientSideSecretsWithoutCollectionName shows: the YAML secrets file connector holds many collections and
     * ignores the property, but cannot start without it.
     *
     * @throws IOException problem reading the secrets file
     */
    @Test
    @Order(4)
    void clientSideSecrets() throws IOException
    {
        saveAndDeleteClientSideSecrets("client-side", Map.of("secretsCollectionName", "System1Secret"));
    }


    /**
     * The same as clientSideSecrets, with the connection configured as the YAML secrets file connector expects:
     * its provider recognizes no configuration properties.
     * <br>
     * YAMLSecretsFileConnector.start() calls SecretsStoreConnector.start(), which throws OCF-CONNECTOR-400-009
     * when the connection has no secretsCollectionName configuration property, and only then sets
     * secretsCollectionName back to null because the file connector serves every collection in the file.
     *
     * @throws IOException problem reading the secrets file
     */
    @Test
    @Order(5)
    void clientSideSecretsWithoutCollectionName() throws IOException
    {
        saveAndDeleteClientSideSecrets("no-collection-name", null);
    }


    private void saveAndDeleteClientSideSecrets(String              name,
                                                Map<String, Object> configurationProperties) throws IOException
    {
        File secretsFile = new File("build/view-services-fvt-data/" + OMAGPlatformExtension.RUN_ID + "-" + name + ".omsecrets").getAbsoluteFile();

        Files.createDirectories(secretsFile.getParentFile().toPath());
        Files.deleteIfExists(secretsFile.toPath());

        String secretsStoreGUID = createSecretsStoreFile(name, secretsFile);
        String connectionGUID   = null;
        String endpointGUID     = null;

        try
        {
            ConnectionProperties connectionProperties = new ConnectionProperties();

            connectionProperties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("Connection", name));
            connectionProperties.setDisplayName("Client-side secrets store connection");
            connectionProperties.setConfigurationProperties(configurationProperties);

            connectionGUID = connectionMaker.create("/connections", newElement(connectionProperties));

            EndpointProperties endpointProperties = new EndpointProperties();

            endpointProperties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("Endpoint", name));
            endpointProperties.setNetworkAddress(secretsFile.getAbsolutePath());

            endpointGUID = connectionMaker.create("/endpoints", newElement(endpointProperties));

            connectionMaker.call("/connections/" + connectionGUID + "/connector-types/" + YAML_SECRETS_FILE_CONNECTOR_TYPE_GUID + "/attach", new NewRelationshipRequestBody());
            connectionMaker.call("/connections/" + connectionGUID + "/endpoints/" + endpointGUID + "/attach", new NewRelationshipRequestBody());
            connectionMaker.call("/elements/" + secretsStoreGUID + "/connections/" + connectionGUID + "/attach", new NewRelationshipRequestBody());

            automatedCuration.call("/secrets-stores/" + secretsStoreGUID + "/client-side-secret/save", secretsCollection("System1Secret", "sys1User"));
            automatedCuration.call("/secrets-stores/" + secretsStoreGUID + "/client-side-secret/save", secretsCollection("System2Secret", "sys2User"));

            assertTrue(secretsFile.exists(), "saveClientSideSecret did not create the secrets file " + secretsFile);

            String contents = Files.readString(secretsFile.toPath());

            assertTrue(contents.contains("System1Secret") && contents.contains("sys1User"), "System1Secret was not saved: " + contents);
            assertTrue(contents.contains("System2Secret") && contents.contains("sys2User"), "System2Secret was not saved: " + contents);

            NameRequestBody deleteRequest = new NameRequestBody();

            deleteRequest.setName("System1Secret");

            automatedCuration.call("/secrets-stores/" + secretsStoreGUID + "/client-side-secret/delete", deleteRequest);

            contents = Files.readString(secretsFile.toPath());

            assertFalse(contents.contains("System1Secret"), "deleteClientSideSecret left System1Secret in the file: " + contents);
            assertTrue(contents.contains("System2Secret"), "deleteClientSideSecret removed the wrong secrets collection: " + contents);
        }
        finally
        {
            if (connectionGUID != null)
            {
                connectionMaker.call("/connections/" + connectionGUID + "/delete", new DeleteElementRequestBody());
            }

            if (endpointGUID != null)
            {
                connectionMaker.call("/endpoints/" + endpointGUID + "/delete", new DeleteElementRequestBody());
            }

            assetMaker.call("/assets/" + secretsStoreGUID + "/delete", new DeleteElementRequestBody());
            Files.deleteIfExists(secretsFile.toPath());
        }

        ViewServicesFvtTestSupport.assertGone(assetMaker, "/assets/" + secretsStoreGUID + "/retrieve");
    }


    /**
     * An initiated engine action is APPROVED for the Stewardship engine, with its action target, and is listed
     * among the active engine actions.  Once cancelled it is CANCELLED and no longer active.
     */
    @Test
    @Order(6)
    void initiateAndCancelEngineAction()
    {
        String targetGUID       = createTargetAsset("engine-action-target");
        String engineActionGUID = initiateEngineAction("automated-curation", targetGUID);

        EngineActionProperties engineAction = readEngineAction(engineActionGUID);

        assertEquals(ActivityStatus.APPROVED, engineAction.getActivityStatus());
        assertEquals(WRITE_TO_AUDIT_LOG, engineAction.getRequestType());
        assertEquals(STEWARDSHIP_ENGINE_NAME, engineAction.getExecutorEngineName());

        EngineActionElement active = findActiveEngineAction(engineActionGUID);

        assertNotNull(active, "getActiveEngineActions does not list the new engine action " + engineActionGUID);
        assertNotNull(active.getActionTargetElements(), "the engine action has no action targets");
        assertTrue(active.getActionTargetElements().stream().anyMatch(target -> targetGUID.equals(target.getActionTargetGUID())),
                   "the engine action does not have its action target");

        automatedCuration.call("/engine-actions/" + engineActionGUID + "/cancel", null);

        assertEquals(ActivityStatus.CANCELLED, readEngineAction(engineActionGUID).getActivityStatus());
        assertEquals(null, findActiveEngineAction(engineActionGUID), "a cancelled engine action is still listed as active");

        assetMaker.call("/assets/" + engineActionGUID + "/delete", new DeleteElementRequestBody());
        assetMaker.call("/assets/" + targetGUID + "/delete", new DeleteElementRequestBody());

        ViewServicesFvtTestSupport.assertGone(assetMaker, "/assets/" + engineActionGUID + "/retrieve");
        ViewServicesFvtTestSupport.assertGone(assetMaker, "/assets/" + targetGUID + "/retrieve");
    }


    /*
     * Helpers shared with GovernanceEngineFVT and ActionAuthorFVT.
     */


    /**
     * Create an asset to act as an action target.
     *
     * @param name distinguishing name
     * @return asset GUID
     */
    static String createTargetAsset(String name)
    {
        AssetProperties properties = new AssetProperties();

        properties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("Asset", name));
        properties.setDisplayName("Action target " + name);

        return assetMaker.create("/assets", newElement(properties));
    }


    /**
     * Initiate an engine action for the Stewardship engine's write-to-audit-log request type.
     *
     * @param name distinguishing name
     * @param targetGUID action target
     * @return engine action GUID
     */
    static String initiateEngineAction(String name,
                                       String targetGUID)
    {
        NewActionTarget actionTarget = new NewActionTarget();

        actionTarget.setActionTargetName("auditedElement");
        actionTarget.setActionTargetGUID(targetGUID);

        InitiateEngineActionRequestBody requestBody = new InitiateEngineActionRequestBody();

        requestBody.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("EngineAction", name));
        requestBody.setDisplayName("Engine action " + name);
        requestBody.setRequestType(WRITE_TO_AUDIT_LOG);
        requestBody.setRequestParameters(Map.of("message", "written by view-services-fvt"));
        requestBody.setActionTargets(List.of(actionTarget));

        return automatedCuration.create("/governance-engines/" + STEWARDSHIP_ENGINE_NAME + "/engine-actions/initiate", requestBody);
    }


    /**
     * Read an engine action as an asset.
     *
     * @param engineActionGUID engine action
     * @return its properties
     */
    static EngineActionProperties readEngineAction(String engineActionGUID)
    {
        OpenMetadataRootElement element = assetMaker.element("/assets/" + engineActionGUID + "/retrieve", new GetRequestBody());

        assertNotNull(element, "engine action " + engineActionGUID + " is missing");

        return assertInstanceOf(EngineActionProperties.class, element.getProperties());
    }


    /**
     * Look for an engine action in the active engine actions.
     *
     * @param engineActionGUID engine action
     * @return the element, or null if it is not active
     */
    static EngineActionElement findActiveEngineAction(String engineActionGUID)
    {
        List<EngineActionElement> activeEngineActions = automatedCuration.get("/engine-actions/active?startFrom=0&pageSize=0",
                                                                              EngineActionElementsResponse.class).getElements();

        if (activeEngineActions != null)
        {
            for (EngineActionElement engineAction : activeEngineActions)
            {
                if (engineActionGUID.equals(engineAction.getElementHeader().getGUID()))
                {
                    return engineAction;
                }
            }
        }

        return null;
    }


    private static String createSecretsStoreFile(String name,
                                                 File   secretsFile)
    {
        DataFileProperties properties = new DataFileProperties();

        properties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("DataFile", name));
        properties.setDisplayName("Client-side secrets store");
        properties.setPathName(secretsFile.getAbsolutePath());
        properties.setFileName(secretsFile.getName());
        properties.setFileExtension("omsecrets");

        return assetMaker.create("/assets", newElement(properties));
    }


    private static SecretsCollectionRequestBody secretsCollection(String collectionName,
                                                                  String userId)
    {
        OpenMetadataSecretsCollection secretsCollection = new OpenMetadataSecretsCollection();

        secretsCollection.setCollectionName(collectionName);
        secretsCollection.setDisplayName("Secret for " + collectionName);
        secretsCollection.setRefreshTimeInterval(60L);
        secretsCollection.setSecrets(Map.of("userId", userId, "clearPassword", "secret"));

        SecretsCollectionRequestBody requestBody = new SecretsCollectionRequestBody();

        requestBody.setSecretsCollection(secretsCollection);

        return requestBody;
    }


    private static NewElementRequestBody newElement(org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRootProperties properties)
    {
        NewElementRequestBody requestBody = new NewElementRequestBody();

        requestBody.setIsOwnAnchor(true);
        requestBody.setProperties(properties);

        return requestBody;
    }


    private static FilterRequestBody filter(String filter)
    {
        FilterRequestBody requestBody = new FilterRequestBody();

        requestBody.setFilter(filter);

        return requestBody;
    }


    private static boolean hasTechnologyType(List<TechnologyTypeSummary> technologyTypes)
    {
        return (technologyTypes != null) &&
                technologyTypes.stream().anyMatch(technologyType -> DATA_FILE_TECHNOLOGY_TYPE_QN.equals(technologyType.getQualifiedName()));
    }
}
