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
 * hands out must appear here, either in {@link #LIFECYCLE_CLIENTS} or {@link #BESPOKE_CLIENTS} - naming
 * the element type or the test class that exercises it - or in {@link #NOT_YET_COVERED} with a reason.
 * {@link ClientCoverageFVT} fails the run if a client
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
     * One lifecycle test case: the client to drive, and the element stem used in its method names.
     *
     * @param clientName simple class name of the client
     * @param elementStem the stem in {@code create<Stem>}, {@code get<Stem>ByGUID} and the rest
     */
    private record LifecycleClient(String clientName, String elementStem) { }


    /**
     * Clients with the standard lifecycle surface - {@code create<Stem>}, {@code get<Stem>ByGUID},
     * {@code get<Stem>sByName}, {@code find<Stem>s}, {@code update<Stem>}, {@code delete<Stem>} - paired
     * with the element stem used in those method names.  {@link ClientLifecycleFVT} drives each pair
     * through the whole sequence.
     * <br>
     * A client appears once per element type it creates, which is why {@code NetworkClient} is here twice.
     * That is also why this is a list rather than a map keyed on the client name: a map silently kept only
     * the last element type for a client that maintains more than one, so {@code Network} was named here
     * and never driven.
     */
    private static final List<LifecycleClient> LIFECYCLE_CLIENTS = new ArrayList<>()
    {{
        add(new LifecycleClient("ActorProfileClient", "ActorProfile"));
        add(new LifecycleClient("ActorRoleClient", "ActorRole"));
        add(new LifecycleClient("AnnotationClient", "Annotation"));
        add(new LifecycleClient("AssetClient", "Asset"));
        add(new LifecycleClient("CollectionClient", "Collection"));
        add(new LifecycleClient("CommunityClient", "Community"));
        add(new LifecycleClient("ConceptModelElementClient", "ConceptModelElement"));
        add(new LifecycleClient("ConnectionClient", "Connection"));
        add(new LifecycleClient("ConnectorTypeClient", "ConnectorType"));
        add(new LifecycleClient("ContactDetailsClient", "ContactDetails"));
        add(new LifecycleClient("ContextEventClient", "ContextEvent"));
        add(new LifecycleClient("DataFieldClient", "DataField"));
        add(new LifecycleClient("DataStructureClient", "DataStructure"));
        add(new LifecycleClient("DataValueSpecificationClient", "DataValueSpecification"));
        add(new LifecycleClient("DesignPatternClient", "DesignPattern"));
        add(new LifecycleClient("EndpointClient", "Endpoint"));
        add(new LifecycleClient("ExternalIdClient", "ExternalId"));
        add(new LifecycleClient("ExternalReferenceClient", "ExternalReference"));
        add(new LifecycleClient("GlossaryTermClient", "GlossaryTerm"));
        add(new LifecycleClient("GovernanceDefinitionClient", "GovernanceDefinition"));
        add(new LifecycleClient("InformalTagClient", "InformalTag"));
        add(new LifecycleClient("LocationClient", "Location"));
        add(new LifecycleClient("MetadataRepositoryCohortClient", "MetadataRepositoryCohort"));
        add(new LifecycleClient("NetworkClient", "Network"));
        add(new LifecycleClient("NetworkClient", "NetworkGateway"));
        add(new LifecycleClient("OperatingPlatformClient", "OperatingPlatform"));
        add(new LifecycleClient("PerspectiveClient", "Perspective"));
        add(new LifecycleClient("ProjectClient", "Project"));
        add(new LifecycleClient("SchemaAttributeClient", "SchemaAttribute"));
        add(new LifecycleClient("SchemaTypeClient", "SchemaType"));
        add(new LifecycleClient("SkillClient", "Skill"));
        add(new LifecycleClient("SoftwareCapabilityClient", "SoftwareCapability"));
        add(new LifecycleClient("SolutionComponentClient", "SolutionComponent"));
        add(new LifecycleClient("SolutionPortClient", "SolutionPort"));
        add(new LifecycleClient("StorageVolumeClient", "StorageVolume"));
        add(new LifecycleClient("UserIdentityClient", "UserIdentity"));
        add(new LifecycleClient("ValidValueDefinitionClient", "ValidValueDefinition"));
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
        put("OpenMetadataStore",             "ReadAndReferenceClientFVT + InstanceControlClientFVT");
        put("OpenMetadataTypesClient",       "ReadAndReferenceClientFVT");
        put("ClassificationExplorerClient",  "ReadAndReferenceClientFVT");
        put("LineageClient",                 "ReadAndReferenceClientFVT");
        put("InformationSupplyChainClient",  "ReadAndReferenceClientFVT");
        put("SpecificationPropertyClient",   "ReadAndReferenceClientFVT");
        put("ValidMetadataValuesClient",     "ReadAndReferenceClientFVT");
        put("ProductManagerClient",          "ProductManagerClientFVT");
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
        return isLifecycleClient(clientName)
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
        return isLifecycleClient(clientName) || BESPOKE_CLIENTS.containsKey(clientName);
    }


    /**
     * The lifecycle test cases, as "ClientName:Stem" pairs.
     *
     * @return test case names, in catalog order
     */
    static List<String> lifecycleClients()
    {
        List<String> cases = new ArrayList<>();

        for (LifecycleClient lifecycleClient : LIFECYCLE_CLIENTS)
        {
            cases.add(lifecycleClient.clientName() + ":" + lifecycleClient.elementStem());
        }

        return cases;
    }


    /**
     * Is this client one of the lifecycle clients?  It may appear more than once, one entry per element type
     * it maintains.
     *
     * @param clientName simple class name of the client
     * @return true if the catalog drives it through the lifecycle sequence
     */
    private static boolean isLifecycleClient(String clientName)
    {
        for (LifecycleClient lifecycleClient : LIFECYCLE_CLIENTS)
        {
            if (lifecycleClient.clientName().equals(clientName))
            {
                return true;
            }
        }

        return false;
    }


    /**
     * Every client name this catalog mentions, for the check that none of them has been removed from the
     * connector context.
     *
     * @return client names
     */
    static Set<String> allNamedClients()
    {
        Set<String> names = new LinkedHashSet<>();

        for (LifecycleClient lifecycleClient : LIFECYCLE_CLIENTS)
        {
            names.add(lifecycleClient.clientName());
        }

        names.addAll(BESPOKE_CLIENTS.keySet());
        names.addAll(NOT_YET_COVERED.keySet());

        return names;
    }
}
