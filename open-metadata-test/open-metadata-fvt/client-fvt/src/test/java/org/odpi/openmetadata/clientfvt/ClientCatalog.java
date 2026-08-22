/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.clientfvt;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * ClientCatalog is the single place that records what this suite covers.
 * <br>
 * <b>This is the file to edit when a client is added or changes.</b>  Every client the connector context
 * hands out must appear here, either in {@link #CLIENTS_UNDER_TEST} - naming the test class that exercises
 * it - or in {@link #EXCLUDED_CLIENTS} with a reason.  {@link ClientCoverageFVT} fails the run if a client
 * exists that this file does not mention, and equally if this file names a client that no longer exists, so
 * neither side can drift quietly.
 * <br>
 * A client listed here is a promise that its create / retrieve / search / update / delete surface is
 * exercised somewhere in the suite.  Adding a <i>method</i> to a client that is already listed will not be
 * caught automatically - that is the limit of what reflection can police - so a new method still needs a new
 * assertion in the matching test class.
 */
final class ClientCatalog
{
    /**
     * Clients with the standard lifecycle surface - {@code create<Stem>}, {@code get<Stem>ByGUID},
     * {@code get<Stem>sByName}, {@code find<Stem>s}, {@code update<Stem>}, {@code delete<Stem>} - mapped to
     * the element stem used in those method names.  {@link ClientLifecycleFVT} drives each one through the
     * whole sequence.
     * <br>
     * A client appears once per element type it creates, which is why {@code NetworkClient} is here twice.
     */
    private static final Map<String, String> LIFECYCLE_CLIENTS = new LinkedHashMap<>()
    {{
        put("ActorProfileClient", "ActorProfile");
        put("ActorRoleClient", "ActorRole");
        put("AnnotationClient", "Annotation");
        put("AssetClient", "Asset");
        put("CollectionClient", "Collection");
        put("CommunityClient", "Community");
        put("ConceptModelElementClient", "ConceptModelElement");
        put("ConnectionClient", "Connection");
        put("ConnectorTypeClient", "ConnectorType");
        put("ContactDetailsClient", "ContactDetails");
        put("ContextEventClient", "ContextEvent");
        put("DataFieldClient", "DataField");
        put("DataStructureClient", "DataStructure");
        put("DataValueSpecificationClient", "DataValueSpecification");
        put("DesignPatternClient", "DesignPattern");
        put("EndpointClient", "Endpoint");
        put("ExternalIdClient", "ExternalId");
        put("ExternalReferenceClient", "ExternalReference");
        put("GlossaryTermClient", "GlossaryTerm");
        put("GovernanceDefinitionClient", "GovernanceDefinition");
        put("InformalTagClient", "InformalTag");
        put("LocationClient", "Location");
        put("MetadataRepositoryCohortClient", "MetadataRepositoryCohort");
        put("NetworkClient", "Network");
        put("NetworkClient", "NetworkGateway");
        put("OperatingPlatformClient", "OperatingPlatform");
        put("PerspectiveClient", "Perspective");
        put("ProjectClient", "Project");
        put("SchemaAttributeClient", "SchemaAttribute");
        put("SchemaTypeClient", "SchemaType");
        put("SkillClient", "Skill");
        put("SoftwareCapabilityClient", "SoftwareCapability");
        put("SolutionComponentClient", "SolutionComponent");
        put("StorageVolumeClient", "StorageVolume");
        put("UserIdentityClient", "UserIdentity");
        put("ValidValueDefinitionClient", "ValidValueDefinition");
    }};

    /**
     * Clients that do not have the standard lifecycle surface, mapped to the test class that covers them.
     * <br>
     * Most of these attach something to an element another client has to create first - there is no
     * {@code createComment}, only {@code addCommentToElement} - so they are driven against a host element
     * their test creates.  The rest read rather than create, or maintain reference data rather than elements.
     */
    private static final Map<String, String> BESPOKE_CLIENTS = new LinkedHashMap<>()
    {{
        put("CommentClient",                 "FeedbackClientFVT");
        put("LikeClient",                    "FeedbackClientFVT");
        put("RatingClient",                  "FeedbackClientFVT");
        put("SearchKeywordClient",           "FeedbackClientFVT");
        put("PropertyFacetClient",           "FeedbackClientFVT");
        put("NoteLogClient",                 "FeedbackClientFVT");
        put("ContributionRecordClient",      "AttachmentClientFVT");
        put("MultiLanguageClient",           "AttachmentClientFVT");
        put("TemplateClient",                "AttachmentClientFVT");
        put("OpenMetadataStore",             "ReadAndReferenceClientFVT");
        put("OpenMetadataTypesClient",       "ReadAndReferenceClientFVT");
        put("ClassificationExplorerClient",  "ReadAndReferenceClientFVT");
        put("LineageClient",                 "ReadAndReferenceClientFVT");
        put("InformationSupplyChainClient",  "ReadAndReferenceClientFVT");
        put("SpecificationPropertyClient",   "ReadAndReferenceClientFVT");
        put("ValidMetadataValuesClient",     "ReadAndReferenceClientFVT");
    }};

    /**
     * Clients with no coverage at all, and why.  Empty - every client the connector context hands out is
     * exercised somewhere.  Kept so that a genuine future exclusion has an obvious, documented home rather
     * than being hidden inside a test.
     */
    private static final Map<String, String> NOT_YET_COVERED = new LinkedHashMap<>();

    private ClientCatalog()
    {
        // no instances
    }


    /**
     * Is this client mentioned at all - either exercised or explicitly not yet covered?
     *
     * @param clientName simple class name of the client
     * @return true if the catalog accounts for it
     */
    static boolean accountsFor(String clientName)
    {
        return LIFECYCLE_CLIENTS.containsKey(clientName)
                       || BESPOKE_CLIENTS.containsKey(clientName)
                       || NOT_YET_COVERED.containsKey(clientName);
    }


    /**
     * Is this client one the suite actually drives?
     *
     * @param clientName simple class name of the client
     * @return true if it is under test
     */
    static boolean isUnderTest(String clientName)
    {
        return LIFECYCLE_CLIENTS.containsKey(clientName) || BESPOKE_CLIENTS.containsKey(clientName);
    }


    /**
     * The lifecycle test cases, as "ClientName:Stem" pairs.
     *
     * @return test case names, in catalog order
     */
    static List<String> lifecycleClients()
    {
        List<String> cases = new ArrayList<>();

        for (Map.Entry<String, String> entry : LIFECYCLE_CLIENTS.entrySet())
        {
            cases.add(entry.getKey() + ":" + entry.getValue());
        }

        return cases;
    }


    /**
     * Every client name this catalog mentions, for the check that none of them has been removed from the
     * connector context.
     *
     * @return client names
     */
    static Set<String> allNamedClients()
    {
        Set<String> names = new LinkedHashSet<>(LIFECYCLE_CLIENTS.keySet());

        names.addAll(BESPOKE_CLIENTS.keySet());
        names.addAll(NOT_YET_COVERED.keySet());

        return names;
    }
}
