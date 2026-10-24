/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.darwinfvt;

import org.odpi.openmetadata.contentpacks.core.IntegrationConnectorDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementControlHeader;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRelationship;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRelationshipList;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElementList;
import org.odpi.openmetadata.frameworks.openmetadata.search.DeleteOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.EndMatchCriteria;
import org.odpi.openmetadata.frameworks.openmetadata.search.GetOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.search.QueryOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.List;

/**
 * DarwinFvtTestSupport holds the identities of the fixture elements that {@link DarwinArchiveWriter} lays
 * down, and the helpers the tests use to read the repository back.
 * <p>
 * The fixture is arranged in sets, each aimed at one thing Darwin does.  Every element has a qualified name
 * starting with {@link #QUALIFIED_NAME_PREFIX}, and every element and relationship has a fixed unique
 * identifier, so that the tests can refer to them directly and the suite can purge its own debris.
 * <ul>
 *     <li><b>Set 1 - mapped</b>: two data assets whose columns are joined by a DataMapping that names an
 *     information supply chain.  Each asset is owned by a software server's capability and is the member of a
 *     digital product.  A DigitalProductDependency between the products, asserted by the archive, names no
 *     supply chain.  Darwin derives the asset-level data flow from the mapping, the server-level data flow from
 *     that, and fills the supply chain in on the products' dependency.</li>
 *     <li><b>Set 2 - indirect</b>: a third asset, server and product, reached from the mapped source asset
 *     through a process along a second supply chain.  Darwin follows the path through the process at both the
 *     server and the product level.</li>
 *     <li><b>Set 3 - broken chain</b>: a fourth asset and product reached from the same process, but along a
 *     different supply chain from the one that reaches the process.  Darwin must not follow the path.</li>
 *     <li><b>Set 4 - unproven</b>: a product whose asset has no lineage at all, with a dependency on the mapped
 *     source product asserted by the archive.  Darwin records an exception against it.</li>
 *     <li><b>Set 5 - stale</b>: two more assets joined by a mapping, and nothing else.  The test removes the
 *     mapping after the first refresh so that the data flow Darwin derived from it has to be withdrawn.</li>
 *     <li><b>Set 6 - present</b>: two assets joined by a mapping, where the archive already asserts the data
 *     flow between the assets for the same supply chain.  Darwin must not add a second one.</li>
 *     <li><b>Set 7 - process use</b>: a process used (not owned) by one server's capability, with data flowing
 *     from it to an asset owned by a second server and to an asset that a third server's capability only uses.
 *     A process counts for its server whatever the use type, so Darwin derives the data flow from the first server
 *     to the second; a data asset only counts for a server that owns it, so there is none to the third.</li>
 *     <li><b>Set 8 - control flow</b>: two processes, each used by its own server and each the member of its own
 *     product, joined by a ControlFlow.  Darwin derives a ControlFlow between the servers, but no DataFlow, and no
 *     DigitalProductDependency between the products - control flow is not data.</li>
 *     <li><b>Set 9 - process call</b>: two processes, each used by its own server, joined by a ProcessCall.  Darwin
 *     derives a ProcessCall between the servers.</li>
 *     <li><b>Set 10 - table to process</b>: a column of an asset mapped by a DataMapping to a process - the shape the
 *     Open Lineage Cataloguer produces.  Darwin derives a DataFlow from the asset to the process.</li>
 *     <li><b>Set 11 - unproven data flow</b>: two assets whose columns each take part in a DataMapping with other
 *     assets, but not with each other, and a DataFlow between them asserted by the archive.  The schema detail is
 *     catalogued for both ends and does not prove the data flow, so Darwin records an UnprovenDataFlow exception
 *     against the asset the data reaches.</li>
 *     <li><b>Set 12 - unchecked data flow</b>: two assets with no schema detail and a DataFlow between them asserted by
 *     the archive.  The missing detail is no evidence against the data flow, so Darwin records no exception.</li>
 *     <li><b>Set 13 - unproven control flow</b>: two servers, each using a process, and a ControlFlow between the
 *     servers asserted by the archive with no control flow between the processes.  Darwin records an
 *     UnprovenControlFlow exception against the server control passes to.</li>
 *     <li><b>Set 14 - unchecked dependency</b>: a product with no members and a dependency on the mapped source product
 *     asserted by the archive.  No lineage could prove or disprove it, so Darwin records no exception.</li>
 *     <li><b>Set 15 - supply chain within a path</b>: two products' assets joined through a process by an untagged
 *     DataFlow and then a tagged one.  The path's supply chain is known from the tagged relationship, so Darwin
 *     fills it in on the untagged one.</li>
 *     <li><b>Set 16 - supply chain from above</b>: two products' assets joined by an untagged DataFlow, with a
 *     dependency between the products asserted by the archive for a supply chain.  The untagged path proves the
 *     dependency, so Darwin records no exception, and fills the dependency's supply chain in on the DataFlow.</li>
 *     <li><b>Set 17 - ambiguous supply chain</b>: an untagged DataFlow from a product's asset to a process, which feeds
 *     two other products' assets along two supply chains.  Darwin leaves the DataFlow untagged and records a copy of
 *     it for each supply chain.</li>
 *     <li><b>Set 18 - data sets as views</b>: two products whose members are data sets over physical assets
 *     (DataSetContent), with lineage between the physical assets through a process.  Darwin treats each data set as
 *     its content and derives the dependency between the products.  A third product's data set covers the second's
 *     content, but for a different supply chain from the lineage's, so it gets no dependency.</li>
 *     <li><b>Set 19 - data sets over tables</b>: two products whose members are data sets over relational tables, with
 *     the tables joined through a process by DataMapping relationships and no lineage between the assets the tables
 *     belong to - the shape the Open Lineage Cataloguer produces.  Darwin follows the table-to-process mappings and
 *     derives the dependency between the products.</li>
 *     <li><b>Set 20 - adopted data flow</b>: two assets whose columns are joined by a mapping, and each of which has
 *     another column mapped to a third asset, so that both stay catalogued when the first mapping goes.  The test
 *     edits the data flow Darwin derives, then removes the mapping: Darwin keeps the adopted data flow and records it
 *     as unproven instead of removing it.</li>
 * </ul>
 */
final class DarwinFvtTestSupport
{
    static final String QUALIFIED_NAME_PREFIX = "darwin-fvt:";

    static final int MAX_PAGE_SIZE = 100;

    /**
     * The userId Darwin runs under, which is what it records in the createdBy of everything it creates.  It
     * comes from the content pack definition so that a change there cannot leave this suite looking for the
     * wrong name.
     */
    static final String DARWIN_USER_ID = IntegrationConnectorDefinition.DARWIN_PRODUCT_DEPENDENCY_MANAGER.getConnectorUserId();

    /**
     * The qualified name Darwin gives its own exception type.  Mirrors the connector's private constant.
     */
    static final String EXCEPTION_TYPE_QUALIFIED_NAME = OpenMetadataType.EXCEPTION_TYPE.typeName + "::UnprovenDigitalProductDependency";

    /**
     * The qualified names of the exception types Darwin records unproven lineage relationships against.
     */
    static final String DATA_FLOW_EXCEPTION_TYPE_QUALIFIED_NAME    = OpenMetadataType.EXCEPTION_TYPE.typeName + "::UnprovenDataFlow";
    static final String CONTROL_FLOW_EXCEPTION_TYPE_QUALIFIED_NAME = OpenMetadataType.EXCEPTION_TYPE.typeName + "::UnprovenControlFlow";

    private static final PropertyHelper propertyHelper = new PropertyHelper();

    /*
     * The information supply chains that the fixture's lineage belongs to.
     */
    static final String ISC_MAPPED   = QUALIFIED_NAME_PREFIX + "isc:mapped";
    static final String ISC_INDIRECT = QUALIFIED_NAME_PREFIX + "isc:indirect";
    static final String ISC_OTHER    = QUALIFIED_NAME_PREFIX + "isc:other";
    static final String ISC_UNPROVEN = QUALIFIED_NAME_PREFIX + "isc:unproven";
    static final String ISC_STALE    = QUALIFIED_NAME_PREFIX + "isc:stale";
    static final String ISC_PRESENT  = QUALIFIED_NAME_PREFIX + "isc:present";
    static final String ISC_PROCESS_USE   = QUALIFIED_NAME_PREFIX + "isc:process-use";
    static final String ISC_CONTROL       = QUALIFIED_NAME_PREFIX + "isc:control";
    static final String ISC_CALL          = QUALIFIED_NAME_PREFIX + "isc:call";
    static final String ISC_TABLE_PROCESS = QUALIFIED_NAME_PREFIX + "isc:table-process";
    static final String ISC_UNPROVEN_FLOW = QUALIFIED_NAME_PREFIX + "isc:unproven-flow";
    static final String ISC_UNCHECKED     = QUALIFIED_NAME_PREFIX + "isc:unchecked";
    static final String ISC_WITHIN_PATH   = QUALIFIED_NAME_PREFIX + "isc:within-path";
    static final String ISC_FROM_ABOVE    = QUALIFIED_NAME_PREFIX + "isc:from-above";
    static final String ISC_AMBIGUOUS_1   = QUALIFIED_NAME_PREFIX + "isc:ambiguous-1";
    static final String ISC_AMBIGUOUS_2   = QUALIFIED_NAME_PREFIX + "isc:ambiguous-2";
    static final String ISC_VIEW          = QUALIFIED_NAME_PREFIX + "isc:view";
    static final String ISC_VIEW_OTHER    = QUALIFIED_NAME_PREFIX + "isc:view-other";
    static final String ISC_TABLE_VIEW    = QUALIFIED_NAME_PREFIX + "isc:table-view";
    static final String ISC_ADOPTED       = QUALIFIED_NAME_PREFIX + "isc:adopted";

    /**
     * The derivations Darwin records on the relationships it creates - one per derivation rule.
     */
    static final String ASSET_LINEAGE_DERIVATION       = "derived from data mappings";
    static final String SERVER_CONTROL_FLOW_DERIVATION = "derived from process control flow";
    static final String PRODUCT_DEPENDENCY_DERIVATION  = "derived from lineage";
    static final String SUPPLY_CHAIN_COPY_DERIVATION   = "supply chain inferred";

    /*
     * Set 1 - mapped.
     */
    static final String ASSET_SOURCE_GUID             = "9af96432-c5c1-4921-bc47-62e33565a317";
    static final String ASSET_TARGET_GUID             = "896d6c25-0ca6-43ff-a52c-52bc9e66ea84";
    static final String COLUMN_SOURCE_GUID            = "35c94f0d-70df-4f90-8713-bfa094abb85c";
    static final String COLUMN_TARGET_GUID            = "86e9c7a6-6341-4371-8d2d-303661c0e730";
    static final String MAPPING_SOURCE_TO_TARGET_GUID = "140f1e17-5d87-4a19-ad40-8e54f262b156";
    static final String SERVER_SOURCE_GUID            = "88572f49-f72c-43f9-9dfc-6a2ac7dc6527";
    static final String CAPABILITY_SOURCE_GUID        = "563e7273-f95e-42fe-9e7e-87b1c2256d7f";
    static final String SERVER_TARGET_GUID            = "b15c6085-f976-452b-bbee-f0e1c43fcbdc";
    static final String CAPABILITY_TARGET_GUID        = "2c2942c0-d3dc-45e6-ae03-017de0d665b4";
    static final String PRODUCT_SOURCE_GUID           = "4332837e-244e-4dd2-88a2-4624a0b1558a";
    static final String PRODUCT_TARGET_GUID           = "c29ba962-7815-4c3e-babd-11e17c1ffdd4";
    static final String DEPENDENCY_UNSET_ISC_GUID     = "bc9548c3-2e02-4eb5-b561-eca6798217c8";
    static final String SUPPORTED_CAPABILITY_SOURCE_GUID = "1efdec85-4d65-4f85-8bc8-38cbdea903ff";
    static final String ASSET_USE_SOURCE_GUID            = "16dc60a4-445b-4b65-b125-fc67693710de";
    static final String SUPPORTED_CAPABILITY_TARGET_GUID = "b8398be6-3e67-461a-97d0-d969c1d6b816";
    static final String ASSET_USE_TARGET_GUID            = "306dc60e-d2fb-4160-bd58-4e6f23557fc1";
    static final String MEMBERSHIP_SOURCE_GUID           = "757a2d47-5b0b-4580-9375-daa57f4b1c0f";
    static final String MEMBERSHIP_TARGET_GUID           = "04ada230-7c60-4ec1-a5ea-b7fefc1bf8de";

    /*
     * Set 2 - indirect.
     */
    static final String ASSET_INDIRECT_GUID              = "0e17ac33-1769-463f-a198-87cca3fc8190";
    static final String PROCESS_INDIRECT_GUID            = "41186b54-252b-45e9-a68f-07121f2dae6a";
    static final String SERVER_INDIRECT_GUID             = "c1256211-bd69-4a09-9bf8-fbffe56f1725";
    static final String CAPABILITY_INDIRECT_GUID         = "a2c440cf-80d7-4021-bb86-54beda53273a";
    static final String PRODUCT_INDIRECT_GUID            = "236779f0-7bbc-47a4-a7ad-798bc62282e8";
    static final String FLOW_SOURCE_TO_PROCESS_GUID      = "5749deb0-6874-477a-8c8f-7e62a5bc2740";
    static final String FLOW_PROCESS_TO_INDIRECT_GUID    = "b58a2803-90bb-4d87-9933-f4c1f9e9bfbc";
    static final String SUPPORTED_CAPABILITY_INDIRECT_GUID = "9a36fd38-9268-4664-b6fe-14ba6f5e378b";
    static final String ASSET_USE_INDIRECT_GUID            = "c5d2c728-402c-403f-bf6b-6249ac011d97";
    static final String MEMBERSHIP_INDIRECT_GUID           = "25524264-5876-4d87-9a49-1ced1aea0f75";

    /*
     * Set 3 - broken chain.
     */
    static final String ASSET_BROKEN_CHAIN_GUID           = "f5eae1ac-6a43-47eb-a299-570d559c57c4";
    static final String PRODUCT_BROKEN_CHAIN_GUID         = "6bfefb99-ccc4-4a47-ba75-caa4f36f9200";
    static final String FLOW_PROCESS_TO_BROKEN_CHAIN_GUID = "1544ff72-176b-4ae7-aa52-9e04506c48ad";
    static final String MEMBERSHIP_BROKEN_CHAIN_GUID      = "8f2746fa-4a2b-49d6-96f2-1d950b6377c5";

    /*
     * Set 4 - unproven.
     */
    static final String ASSET_UNPROVEN_GUID      = "52e61f4f-1b9f-47c7-a7f6-6e7e5a5d49aa";
    static final String PRODUCT_UNPROVEN_GUID    = "2898d591-c9e4-40c0-bf94-3e0f9d599dee";
    static final String DEPENDENCY_UNPROVEN_GUID = "fd626d86-6b24-4f20-aae6-15692fb0f6c6";
    static final String MEMBERSHIP_UNPROVEN_GUID = "643964a3-036f-40da-893f-5f4c7fabc0b9";

    /*
     * Set 5 - stale.
     */
    static final String ASSET_STALE_SOURCE_GUID  = "418a0809-4e1e-4a11-8d35-034e46806bf0";
    static final String ASSET_STALE_TARGET_GUID  = "865b3ac3-2645-4a55-8792-d9470416908f";
    static final String COLUMN_STALE_SOURCE_GUID = "426f6620-3266-4fd1-a1f8-1fc5b97cd9e1";
    static final String COLUMN_STALE_TARGET_GUID = "a1f427e8-7d8f-4f43-b817-6df62558c9cf";
    static final String MAPPING_STALE_GUID       = "9b263d6e-f59e-4337-b447-418aa921aab3";

    /*
     * Set 6 - present.
     */
    static final String ASSET_PRESENT_SOURCE_GUID  = "8ceae3c5-d4cd-41d7-8f91-971ca33f74ce";
    static final String ASSET_PRESENT_TARGET_GUID  = "addd8231-f2b9-48b2-b513-475591eff52d";
    static final String COLUMN_PRESENT_SOURCE_GUID = "fab64c5c-ca4b-437a-857c-cbfa92f23da9";
    static final String COLUMN_PRESENT_TARGET_GUID = "c3dee673-2cb5-402c-9025-f408e0c4d022";
    static final String MAPPING_PRESENT_GUID       = "4deeb697-075c-4a8b-bce4-073ccb5cb55d";
    static final String FLOW_PRESENT_GUID          = "f9e2c4de-be1b-4d98-b759-8aeea2a07d07";

    /*
     * Set 7 - process use.
     */
    static final String PROCESS_USED_GUID                        = "b2e9a057-3bf1-4080-8c34-35210829c918";
    static final String SERVER_PROCESS_USE_GUID                  = "779bd569-f3f7-4620-b5b0-551494732558";
    static final String CAPABILITY_PROCESS_USE_GUID              = "dbf207e5-365f-4529-b556-7f4477204ac2";
    static final String SUPPORTED_CAPABILITY_PROCESS_USE_GUID    = "62713574-e641-41c2-b304-7890b6c421df";
    static final String ASSET_USE_PROCESS_USE_GUID               = "bef02275-5880-4830-9d70-d690f57d2702";
    static final String ASSET_PROCESS_OUTPUT_GUID                = "a69730f6-2d07-41cb-95a1-d90f11419142";
    static final String SERVER_PROCESS_OUTPUT_GUID               = "adac4d8b-fe92-4334-a062-a6b5e3c03f43";
    static final String CAPABILITY_PROCESS_OUTPUT_GUID           = "a8d6347c-7960-4b03-a07e-9f7bd0652fd9";
    static final String SUPPORTED_CAPABILITY_PROCESS_OUTPUT_GUID = "da37d898-f937-4f1e-a827-a36d8e64cec5";
    static final String ASSET_USE_PROCESS_OUTPUT_GUID            = "6631ef10-004e-49c1-bfac-3542bb7fd34d";
    static final String ASSET_ONLY_USED_GUID                     = "608eb953-d103-429b-91cf-70cc54bfcb80";
    static final String SERVER_ONLY_USES_GUID                    = "f493555e-fb93-47a7-8c13-a3158b81c4a9";
    static final String CAPABILITY_ONLY_USES_GUID                = "7da6f471-bf95-4b00-9d7e-977cfd5437c9";
    static final String SUPPORTED_CAPABILITY_ONLY_USES_GUID      = "e7bd97b1-2c13-4f64-b556-66690ae18d92";
    static final String ASSET_USE_ONLY_USES_GUID                 = "0387db53-27bc-4a8d-93bb-ce4e24873947";
    static final String FLOW_PROCESS_TO_OUTPUT_GUID              = "4ca19a9f-396a-4c12-a710-df3b8b7ce4c5";
    static final String FLOW_PROCESS_TO_ONLY_USED_GUID           = "4f033d7f-7e75-4360-b73d-b1d15b04676d";

    /*
     * Set 8 - control flow.
     */
    static final String PROCESS_CONTROL_FROM_GUID              = "49c7f72e-3519-42bd-b077-1a4ec24b226a";
    static final String SERVER_CONTROL_FROM_GUID               = "cc576d8d-4cc9-43fd-bc29-f630b1287612";
    static final String CAPABILITY_CONTROL_FROM_GUID           = "bb0d7707-b4cb-41da-aa4d-e5b5196cdb60";
    static final String SUPPORTED_CAPABILITY_CONTROL_FROM_GUID = "a11fef8d-a4fd-46e0-a995-2994690d0dbd";
    static final String ASSET_USE_CONTROL_FROM_GUID            = "5f7e98d7-204a-4713-b28d-553afd8a0aba";
    static final String PROCESS_CONTROL_TO_GUID                = "987d18a0-cd1a-4144-a817-d53a99085e3f";
    static final String SERVER_CONTROL_TO_GUID                 = "92aa0f5e-a13a-436d-877d-15c8ce4fcef7";
    static final String CAPABILITY_CONTROL_TO_GUID             = "3dd65bee-a065-408c-8692-864544068858";
    static final String SUPPORTED_CAPABILITY_CONTROL_TO_GUID   = "61b0296a-032d-4da4-af49-130697ef1277";
    static final String ASSET_USE_CONTROL_TO_GUID              = "e80dd4ad-054d-43b1-913f-559155627e4b";
    static final String CONTROL_FLOW_GUID                      = "1c90a282-1cbc-48e7-8abc-071b9b044b6b";
    static final String PRODUCT_CONTROL_FROM_GUID              = "59490fa2-8c5b-4698-82cb-132982cfa339";
    static final String MEMBERSHIP_CONTROL_FROM_GUID           = "dd718923-9b05-41d6-81fa-3fa0ff269085";
    static final String PRODUCT_CONTROL_TO_GUID                = "f3ce89d9-e6f3-40fd-8ff2-34c90efc86b1";
    static final String MEMBERSHIP_CONTROL_TO_GUID             = "f90a9f9b-9bdf-4587-ace5-3075e9fdc494";

    /*
     * Set 9 - process call.
     */
    static final String PROCESS_CALLER_GUID              = "1503fc92-ec14-4f03-8394-6ddda76381b2";
    static final String SERVER_CALLER_GUID               = "5eec748f-b167-4f7b-ab62-ad76c8c86432";
    static final String CAPABILITY_CALLER_GUID           = "c1f01d27-4c62-454e-9a95-a53eace638f4";
    static final String SUPPORTED_CAPABILITY_CALLER_GUID = "314dfb16-08cf-4609-8a1a-bde2ff340bc3";
    static final String ASSET_USE_CALLER_GUID            = "4f89e44b-49a2-4eb0-9c34-8c7654fc6f04";
    static final String PROCESS_CALLED_GUID              = "cc325ad3-c439-4831-8a91-3cac42f6813f";
    static final String SERVER_CALLED_GUID               = "52b386d5-5ef6-4426-8ff2-d88cb8340073";
    static final String CAPABILITY_CALLED_GUID           = "c10489be-71c5-4fbc-a40e-33692663c42f";
    static final String SUPPORTED_CAPABILITY_CALLED_GUID = "984befcb-8136-4243-a604-c0f8f775c215";
    static final String ASSET_USE_CALLED_GUID            = "2bbdae50-43a7-4b8f-b184-ec3fe92c6f69";
    static final String PROCESS_CALL_GUID                = "59ab3dd7-85ae-48a9-89ca-4bb17b52cdf2";

    /*
     * Set 10 - table to process.
     */
    static final String ASSET_TABLE_GUID              = "1fe12e92-fc5f-4e30-a829-17845c6a6356";
    static final String COLUMN_TABLE_GUID             = "ba7f418f-121b-4da9-a5e4-cc4aabc2b516";
    static final String PROCESS_MAPPED_GUID           = "ffe37b0e-8dcf-4c23-b579-55d6821453b0";
    static final String MAPPING_TABLE_TO_PROCESS_GUID = "1ef98d65-401b-426d-bcb6-8005229f6890";

    /*
     * Set 11 - unproven data flow.
     */
    static final String ASSET_UNPROVEN_FLOW_SOURCE_GUID   = "d77d8405-04de-4908-b772-4d157f96383b";
    static final String ASSET_UNPROVEN_FLOW_TARGET_GUID   = "d71bdac2-a004-4036-8c2f-dbddde66f535";
    static final String COLUMN_UNPROVEN_FLOW_SOURCE_GUID  = "704a9544-7d77-44cb-836a-530395be0009";
    static final String COLUMN_UNPROVEN_FLOW_TARGET_GUID  = "86eb3dbb-e63a-4517-b4e0-a70ac5d5041d";
    static final String ASSET_UNPROVEN_FLOW_OTHER_1_GUID  = "62deba1e-709b-4c52-8aa2-d2e9d995b95f";
    static final String ASSET_UNPROVEN_FLOW_OTHER_2_GUID  = "088b44dc-e240-4ec9-bcd3-6fce72042ba8";
    static final String COLUMN_UNPROVEN_FLOW_OTHER_1_GUID = "73985090-f5b5-4ac7-a6c3-2b7b75712dd2";
    static final String COLUMN_UNPROVEN_FLOW_OTHER_2_GUID = "c407a4fa-1cdd-4d0f-be69-04ecdf0bcd60";
    static final String MAPPING_UNPROVEN_FLOW_OUT_GUID    = "1be4f225-6fa9-413c-94bb-31c09ad16d10";
    static final String MAPPING_UNPROVEN_FLOW_IN_GUID     = "7d6a524d-81d9-480b-b995-34b6dce779f1";
    static final String FLOW_UNPROVEN_GUID                = "9c43bda5-1570-447b-b6f3-4fb92fc566f0";

    /*
     * Set 12 - unchecked data flow.
     */
    static final String ASSET_UNCHECKED_SOURCE_GUID = "1d42a063-f30d-4db5-a009-183bbdd3f3f5";
    static final String ASSET_UNCHECKED_TARGET_GUID = "246ba24f-fc5e-488a-9633-d2bd20d2d237";
    static final String FLOW_UNCHECKED_GUID         = "76261d4a-78ef-4e65-8a8d-d40cf164f3c1";

    /*
     * Set 13 - unproven control flow.
     */
    static final String PROCESS_UNPROVEN_CONTROL_FROM_GUID              = "e0df2fad-07ce-49c3-910c-1276af6d7ed0";
    static final String PROCESS_UNPROVEN_CONTROL_TO_GUID                = "c09f8cd1-1f49-431a-a0d5-226aa15feaa2";
    static final String SERVER_UNPROVEN_CONTROL_FROM_GUID               = "a3690e7d-8db3-40e3-a6f6-074fde17d86d";
    static final String CAPABILITY_UNPROVEN_CONTROL_FROM_GUID           = "fc9507bc-30bd-4d08-95f1-0ebe1057e859";
    static final String SUPPORTED_CAPABILITY_UNPROVEN_CONTROL_FROM_GUID = "f775e635-2d63-4bee-be10-6ba8329c5491";
    static final String ASSET_USE_UNPROVEN_CONTROL_FROM_GUID            = "73c455f8-7adc-4805-a175-87618be0bcb0";
    static final String SERVER_UNPROVEN_CONTROL_TO_GUID                 = "9918d078-b58b-4a80-b4d9-ce5722356f8e";
    static final String CAPABILITY_UNPROVEN_CONTROL_TO_GUID             = "6ec6984b-381c-4e7a-9a57-44d4480ab73a";
    static final String SUPPORTED_CAPABILITY_UNPROVEN_CONTROL_TO_GUID   = "cd5f05ce-1786-4229-b955-9b43e2fedf23";
    static final String ASSET_USE_UNPROVEN_CONTROL_TO_GUID              = "8bc23545-2828-4d96-9fe6-6a01f14695c1";
    static final String CONTROL_FLOW_UNPROVEN_GUID                      = "c6b07f14-aa7b-46ed-ba95-a87d606632f3";

    /*
     * Set 14 - unchecked dependency.
     */
    static final String PRODUCT_NO_MEMBERS_GUID   = "f6a15ea0-85a1-49eb-a3ef-70bddb55b065";
    static final String DEPENDENCY_UNCHECKED_GUID = "043c766a-9c8a-4aa6-870b-7ffaec58527d";

    /*
     * Set 15 - supply chain within a path.
     */
    static final String ASSET_WITHIN_SOURCE_GUID           = "55d6c506-4c0f-4976-bc40-c9243763dfaf";
    static final String ASSET_WITHIN_TARGET_GUID           = "cd297a6a-38c2-4da5-ba46-6d213533a2d9";
    static final String PROCESS_WITHIN_GUID                = "58d94d12-15be-417c-adfb-937e5fbdce71";
    static final String PRODUCT_WITHIN_SOURCE_GUID         = "84b9071c-8804-4dde-b776-784e3db80b99";
    static final String MEMBERSHIP_WITHIN_SOURCE_GUID      = "cf467bf9-9489-4ad1-8507-33c5312b6b09";
    static final String PRODUCT_WITHIN_TARGET_GUID         = "bb18911f-ad4c-437a-92fc-87d74748c2a9";
    static final String MEMBERSHIP_WITHIN_TARGET_GUID      = "dcb272ce-eaf3-427b-aeac-eb01434669f7";
    static final String FLOW_WITHIN_UNTAGGED_GUID          = "854033c5-0171-4b5f-8d2f-bb2baa026457";
    static final String FLOW_WITHIN_TAGGED_GUID            = "7978764d-d202-46f6-be7f-5139868ad462";

    /*
     * Set 16 - supply chain from above.
     */
    static final String ASSET_ABOVE_SOURCE_GUID            = "8e5a0334-3803-4075-a87b-637728e0431e";
    static final String ASSET_ABOVE_TARGET_GUID            = "25d8c656-7df0-4236-8fd4-14dd5cda9383";
    static final String PRODUCT_ABOVE_SOURCE_GUID          = "2311f1a3-fc74-4a53-9c55-4aa9f0730723";
    static final String MEMBERSHIP_ABOVE_SOURCE_GUID       = "b1824117-9400-4e65-98d5-23dc2e2f2730";
    static final String PRODUCT_ABOVE_TARGET_GUID          = "2975d932-c56e-4e13-90fa-5150d641ecf9";
    static final String MEMBERSHIP_ABOVE_TARGET_GUID       = "4689b9ea-e009-4a8f-9621-a8c6ba95b8a1";
    static final String FLOW_ABOVE_UNTAGGED_GUID           = "8bb1d128-33e2-499f-b90e-4c322b7723a6";
    static final String DEPENDENCY_ABOVE_GUID              = "75f32e92-2e88-4594-a840-1fba183981bd";

    /*
     * Set 17 - ambiguous supply chain.
     */
    static final String ASSET_AMBIGUOUS_SOURCE_GUID        = "1bd9caa4-45fa-4aa2-bae8-00bb88bf5281";
    static final String ASSET_AMBIGUOUS_TARGET_1_GUID      = "bfaf7e04-7702-4d92-b67d-f32bc24b9772";
    static final String ASSET_AMBIGUOUS_TARGET_2_GUID      = "6bd8f58a-2414-4bbf-ab92-0854de2ee008";
    static final String PROCESS_AMBIGUOUS_GUID             = "b8cfe909-e01a-4687-98f1-fdcc5446abbc";
    static final String PRODUCT_AMBIGUOUS_SOURCE_GUID      = "1062db24-8ef0-4c51-b8f9-b71aee93300a";
    static final String MEMBERSHIP_AMBIGUOUS_SOURCE_GUID   = "b399cedb-7e1f-41ab-8508-78b25cc66a90";
    static final String PRODUCT_AMBIGUOUS_TARGET_1_GUID    = "bcdbf9f0-b9a1-42ee-8c3d-e095ecfaf7da";
    static final String MEMBERSHIP_AMBIGUOUS_TARGET_1_GUID = "69a8c6d7-b46b-445d-aa80-0eb628d08d22";
    static final String PRODUCT_AMBIGUOUS_TARGET_2_GUID    = "bf7a45f0-d7f7-4f44-8ce4-4dacd89307cb";
    static final String MEMBERSHIP_AMBIGUOUS_TARGET_2_GUID = "b61aeae0-179b-4c62-baf6-db1b9af55866";
    static final String FLOW_AMBIGUOUS_UNTAGGED_GUID       = "4217b669-0b9e-4ea4-9d99-3f2f70339b3f";
    static final String FLOW_AMBIGUOUS_1_GUID              = "48e05880-64ff-4e19-8990-58b7b9523593";
    static final String FLOW_AMBIGUOUS_2_GUID              = "60ae8686-08d0-428b-aa7d-1485ca8e8279";

    /*
     * Set 18 - data sets as views.
     */
    static final String DATA_SET_VIEW_SOURCE_GUID   = "41a4f7af-2662-49f8-976c-4e5daabf4bbb";
    static final String ASSET_VIEW_SOURCE_GUID      = "d97d3f74-e410-4345-ae3e-83463b0166f1";
    static final String PRODUCT_VIEW_SOURCE_GUID    = "0269e45c-0700-4178-aff8-db64493f1371";
    static final String MEMBERSHIP_VIEW_SOURCE_GUID = "20319da8-b7a4-42bc-a35a-32bfafde3616";
    static final String CONTENT_VIEW_SOURCE_GUID    = "52f0db1e-f717-4ce1-a4e9-bd932a0d71b5";
    static final String PROCESS_VIEW_GUID           = "a8acd2fb-b153-417e-ab35-ca65d6c0fd0f";
    static final String FLOW_VIEW_IN_GUID           = "4f167285-55d4-4c6d-bf84-15df39e8b733";
    static final String FLOW_VIEW_OUT_GUID          = "9be795fd-bbac-4192-85fe-978398bbc508";
    static final String DATA_SET_VIEW_TARGET_GUID   = "f45462d4-059f-4707-9a7e-ee87a97a0b9d";
    static final String ASSET_VIEW_TARGET_GUID      = "2bf913aa-7a9c-4fb6-b99c-a42648cb9e37";
    static final String PRODUCT_VIEW_TARGET_GUID    = "b4ee06f0-ff93-457f-888b-1deb1d0d7b9b";
    static final String MEMBERSHIP_VIEW_TARGET_GUID = "149a6832-07a6-44a2-88a0-36520b29c5b0";
    static final String CONTENT_VIEW_TARGET_GUID    = "56722dcd-867c-49c5-81f7-afddc6c6ebf9";
    static final String DATA_SET_VIEW_OTHER_GUID    = "6e3dbc4e-271d-4b91-acf7-2b13e069f858";
    static final String PRODUCT_VIEW_OTHER_GUID     = "3680b4b2-97ba-4c50-b9ee-f6c939a8c8a2";
    static final String MEMBERSHIP_VIEW_OTHER_GUID  = "559449b7-0631-440e-be28-cde0191ce9f1";
    static final String CONTENT_VIEW_OTHER_GUID     = "405115d7-bb46-4fc6-bd55-cb544767ffd8";

    /*
     * Set 19 - data sets over tables.
     */
    static final String ASSET_TABLE_VIEW_SOURCE_GUID      = "349c9460-3548-4d28-b098-7bd1261d4af9";
    static final String ASSET_TABLE_VIEW_TARGET_GUID      = "6789dff9-e7fc-46dc-bd26-9c555de489bd";
    static final String TABLE_VIEW_SOURCE_GUID            = "d6e01a2b-1908-4829-9a3c-4c084e507daa";
    static final String TABLE_VIEW_TARGET_GUID            = "b306d4f1-bcf3-485f-a209-e10a00381683";
    static final String DATA_SET_TABLE_VIEW_SOURCE_GUID   = "69f4b503-c8d5-4162-8909-a46ad35aa369";
    static final String DATA_SET_TABLE_VIEW_TARGET_GUID   = "71900e02-31ce-4f34-bf97-3f069cb9b278";
    static final String PROCESS_TABLE_VIEW_GUID           = "c9766946-8451-4e15-9a20-b4d3ec7ba9d0";
    static final String PRODUCT_TABLE_VIEW_SOURCE_GUID    = "13e9a21a-ebb8-4a15-a82a-5b325a0ad99b";
    static final String MEMBERSHIP_TABLE_VIEW_SOURCE_GUID = "5cb4f6b5-cb4b-4739-be8b-7dc55860569d";
    static final String PRODUCT_TABLE_VIEW_TARGET_GUID    = "e4c72858-a7b3-4be8-9f0b-8d8515c2ebd5";
    static final String MEMBERSHIP_TABLE_VIEW_TARGET_GUID = "20616f51-f5af-40ce-8ad5-a9fca0e08413";
    static final String CONTENT_TABLE_VIEW_SOURCE_GUID    = "69fb673e-b0d7-415a-9faa-813702a642e3";
    static final String CONTENT_TABLE_VIEW_TARGET_GUID    = "e10bdee8-aee2-476e-abd9-a36550c00e50";
    static final String MAPPING_TABLE_VIEW_IN_GUID        = "1085b033-b585-47df-9f5a-d5ce79aec160";
    static final String MAPPING_TABLE_VIEW_OUT_GUID       = "0a9f688a-7413-4a4f-aa07-a9a95a6538e6";

    /*
     * Set 20 - adopted data flow.
     */
    static final String ASSET_ADOPTED_SOURCE_GUID       = "915c516f-4d9a-44f7-a50a-c49222830936";
    static final String ASSET_ADOPTED_TARGET_GUID       = "d2ba3f54-7b19-4fb3-bb06-54c7feb948e7";
    static final String ASSET_ADOPTED_OTHER_1_GUID      = "189d8a1f-6195-4aa4-b6f4-7ab635010f26";
    static final String ASSET_ADOPTED_OTHER_2_GUID      = "64e0ae3b-b5bf-40f1-aeef-45ca281aa434";
    static final String COLUMN_ADOPTED_SOURCE_GUID      = "6d6d749a-aa5d-477a-8624-ef1701972649";
    static final String COLUMN_ADOPTED_SOURCE_2_GUID    = "3f84d1f7-1614-4eb6-a5c2-6e1d318c1a54";
    static final String COLUMN_ADOPTED_TARGET_GUID      = "548c0ff4-1515-41db-a1a6-24b31ed8922e";
    static final String COLUMN_ADOPTED_TARGET_2_GUID    = "7d2767ce-4b29-4e5e-b042-8127e32e2096";
    static final String COLUMN_ADOPTED_OTHER_1_GUID     = "c7b8cdad-ebe8-4bab-892f-1d85a3987e43";
    static final String COLUMN_ADOPTED_OTHER_2_GUID     = "daad5dd8-f5a5-4969-af44-72c4d3f731d6";
    static final String MAPPING_ADOPTED_GUID            = "242431c3-a98e-4b1f-ab62-e4f0949902b7";
    static final String MAPPING_ADOPTED_SOURCE_OUT_GUID = "639d3990-0b7f-44ce-b940-fe104ae5d1ac";
    static final String MAPPING_ADOPTED_TARGET_IN_GUID  = "6d778db3-deda-46bf-8929-dbd77a9ae89a";

    /**
     * Every entity the fixture creates.  Purging these takes their relationships with them - the archive's
     * own and the ones Darwin created between them on an earlier run.
     */
    static final List<String> FIXTURE_ELEMENT_GUIDS = List.of(ASSET_SOURCE_GUID,
                                                               ASSET_TARGET_GUID,
                                                               COLUMN_SOURCE_GUID,
                                                               COLUMN_TARGET_GUID,
                                                               SERVER_SOURCE_GUID,
                                                               CAPABILITY_SOURCE_GUID,
                                                               SERVER_TARGET_GUID,
                                                               CAPABILITY_TARGET_GUID,
                                                               PRODUCT_SOURCE_GUID,
                                                               PRODUCT_TARGET_GUID,
                                                               ASSET_INDIRECT_GUID,
                                                               PROCESS_INDIRECT_GUID,
                                                               SERVER_INDIRECT_GUID,
                                                               CAPABILITY_INDIRECT_GUID,
                                                               PRODUCT_INDIRECT_GUID,
                                                               ASSET_BROKEN_CHAIN_GUID,
                                                               PRODUCT_BROKEN_CHAIN_GUID,
                                                               ASSET_UNPROVEN_GUID,
                                                               PRODUCT_UNPROVEN_GUID,
                                                               ASSET_STALE_SOURCE_GUID,
                                                               ASSET_STALE_TARGET_GUID,
                                                               COLUMN_STALE_SOURCE_GUID,
                                                               COLUMN_STALE_TARGET_GUID,
                                                               ASSET_PRESENT_SOURCE_GUID,
                                                               ASSET_PRESENT_TARGET_GUID,
                                                               COLUMN_PRESENT_SOURCE_GUID,
                                                               COLUMN_PRESENT_TARGET_GUID,
                                                               PROCESS_USED_GUID,
                                                               SERVER_PROCESS_USE_GUID,
                                                               CAPABILITY_PROCESS_USE_GUID,
                                                               ASSET_PROCESS_OUTPUT_GUID,
                                                               SERVER_PROCESS_OUTPUT_GUID,
                                                               CAPABILITY_PROCESS_OUTPUT_GUID,
                                                               ASSET_ONLY_USED_GUID,
                                                               SERVER_ONLY_USES_GUID,
                                                               CAPABILITY_ONLY_USES_GUID,
                                                               PROCESS_CONTROL_FROM_GUID,
                                                               SERVER_CONTROL_FROM_GUID,
                                                               CAPABILITY_CONTROL_FROM_GUID,
                                                               PROCESS_CONTROL_TO_GUID,
                                                               SERVER_CONTROL_TO_GUID,
                                                               CAPABILITY_CONTROL_TO_GUID,
                                                               PRODUCT_CONTROL_FROM_GUID,
                                                               PRODUCT_CONTROL_TO_GUID,
                                                               PROCESS_CALLER_GUID,
                                                               SERVER_CALLER_GUID,
                                                               CAPABILITY_CALLER_GUID,
                                                               PROCESS_CALLED_GUID,
                                                               SERVER_CALLED_GUID,
                                                               CAPABILITY_CALLED_GUID,
                                                               ASSET_TABLE_GUID,
                                                               COLUMN_TABLE_GUID,
                                                               PROCESS_MAPPED_GUID,
                                                               ASSET_UNPROVEN_FLOW_SOURCE_GUID,
                                                               ASSET_UNPROVEN_FLOW_TARGET_GUID,
                                                               COLUMN_UNPROVEN_FLOW_SOURCE_GUID,
                                                               COLUMN_UNPROVEN_FLOW_TARGET_GUID,
                                                               ASSET_UNPROVEN_FLOW_OTHER_1_GUID,
                                                               ASSET_UNPROVEN_FLOW_OTHER_2_GUID,
                                                               COLUMN_UNPROVEN_FLOW_OTHER_1_GUID,
                                                               COLUMN_UNPROVEN_FLOW_OTHER_2_GUID,
                                                               ASSET_UNCHECKED_SOURCE_GUID,
                                                               ASSET_UNCHECKED_TARGET_GUID,
                                                               PROCESS_UNPROVEN_CONTROL_FROM_GUID,
                                                               PROCESS_UNPROVEN_CONTROL_TO_GUID,
                                                               SERVER_UNPROVEN_CONTROL_FROM_GUID,
                                                               CAPABILITY_UNPROVEN_CONTROL_FROM_GUID,
                                                               SERVER_UNPROVEN_CONTROL_TO_GUID,
                                                               CAPABILITY_UNPROVEN_CONTROL_TO_GUID,
                                                               PRODUCT_NO_MEMBERS_GUID,
                                                               ASSET_WITHIN_SOURCE_GUID,
                                                               ASSET_WITHIN_TARGET_GUID,
                                                               PROCESS_WITHIN_GUID,
                                                               PRODUCT_WITHIN_SOURCE_GUID,
                                                               PRODUCT_WITHIN_TARGET_GUID,
                                                               ASSET_ABOVE_SOURCE_GUID,
                                                               ASSET_ABOVE_TARGET_GUID,
                                                               PRODUCT_ABOVE_SOURCE_GUID,
                                                               PRODUCT_ABOVE_TARGET_GUID,
                                                               ASSET_AMBIGUOUS_SOURCE_GUID,
                                                               ASSET_AMBIGUOUS_TARGET_1_GUID,
                                                               ASSET_AMBIGUOUS_TARGET_2_GUID,
                                                               PROCESS_AMBIGUOUS_GUID,
                                                               PRODUCT_AMBIGUOUS_SOURCE_GUID,
                                                               PRODUCT_AMBIGUOUS_TARGET_1_GUID,
                                                               PRODUCT_AMBIGUOUS_TARGET_2_GUID,
                                                               DATA_SET_VIEW_SOURCE_GUID,
                                                               ASSET_VIEW_SOURCE_GUID,
                                                               PRODUCT_VIEW_SOURCE_GUID,
                                                               PROCESS_VIEW_GUID,
                                                               DATA_SET_VIEW_TARGET_GUID,
                                                               ASSET_VIEW_TARGET_GUID,
                                                               PRODUCT_VIEW_TARGET_GUID,
                                                               DATA_SET_VIEW_OTHER_GUID,
                                                               PRODUCT_VIEW_OTHER_GUID,
                                                               ASSET_TABLE_VIEW_SOURCE_GUID,
                                                               ASSET_TABLE_VIEW_TARGET_GUID,
                                                               TABLE_VIEW_SOURCE_GUID,
                                                               TABLE_VIEW_TARGET_GUID,
                                                               DATA_SET_TABLE_VIEW_SOURCE_GUID,
                                                               DATA_SET_TABLE_VIEW_TARGET_GUID,
                                                               PROCESS_TABLE_VIEW_GUID,
                                                               PRODUCT_TABLE_VIEW_SOURCE_GUID,
                                                               PRODUCT_TABLE_VIEW_TARGET_GUID,
                                                               ASSET_ADOPTED_SOURCE_GUID,
                                                               ASSET_ADOPTED_TARGET_GUID,
                                                               ASSET_ADOPTED_OTHER_1_GUID,
                                                               ASSET_ADOPTED_OTHER_2_GUID,
                                                               COLUMN_ADOPTED_SOURCE_GUID,
                                                               COLUMN_ADOPTED_SOURCE_2_GUID,
                                                               COLUMN_ADOPTED_TARGET_GUID,
                                                               COLUMN_ADOPTED_TARGET_2_GUID,
                                                               COLUMN_ADOPTED_OTHER_1_GUID,
                                                               COLUMN_ADOPTED_OTHER_2_GUID);


    private DarwinFvtTestSupport()
    {
        // no instances
    }


    /**
     * Query options for reading lineage, which also returns the elements only visible to lineage.
     *
     * @return query options
     */
    static QueryOptions lineageQueryOptions()
    {
        QueryOptions queryOptions = new QueryOptions();

        queryOptions.setForLineage(true);
        queryOptions.setPageSize(MAX_PAGE_SIZE);

        return queryOptions;
    }


    /**
     * Get options for reading an element that may only be visible to lineage.
     *
     * @return get options
     */
    static GetOptions lineageGetOptions()
    {
        GetOptions getOptions = new GetOptions();

        getOptions.setForLineage(true);

        return getOptions;
    }


    /**
     * Delete options for removing one of the fixture's own relationships.  The fixture is owned by the archive
     * that supplied it, so it can only be changed on behalf of the archive - the same rule Darwin follows
     * when it fills in an information supply chain on one.
     *
     * @return delete options
     */
    static DeleteOptions archiveOwnedDeleteOptions()
    {
        DeleteOptions deleteOptions = new DeleteOptions();

        deleteOptions.setForLineage(true);
        deleteOptions.setExternalSourceGUID(DarwinArchiveWriter.ARCHIVE_GUID);
        deleteOptions.setExternalSourceName(DarwinArchiveWriter.ARCHIVE_NAME);

        return deleteOptions;
    }


    /**
     * Retrieve the relationships of a type running from one element to another - in that direction, so a
     * DataFlow from the second to the first is not returned.
     *
     * @param openMetadataStore store to read through
     * @param relationshipTypeName type of relationship
     * @param end1GUID element at end 1
     * @param end2GUID element at end 2
     * @return list - empty if there are none
     * @throws Exception the retrieval failed
     */
    static List<OpenMetadataRelationship> getRelationships(OpenMetadataStore openMetadataStore,
                                                           String            relationshipTypeName,
                                                           String            end1GUID,
                                                           String            end2GUID) throws Exception
    {
        List<OpenMetadataRelationship> relationships = new ArrayList<>();

        OpenMetadataRelationshipList retrievedRelationships =
                openMetadataStore.findRelationshipsBetweenMetadataElements(relationshipTypeName,
                                                                            null,
                                                                            List.of(end1GUID),
                                                                            List.of(end2GUID),
                                                                            EndMatchCriteria.BOTH,
                                                                            null,
                                                                            lineageQueryOptions());

        if ((retrievedRelationships != null) && (retrievedRelationships.getRelationships() != null))
        {
            for (OpenMetadataRelationship relationship : retrievedRelationships.getRelationships())
            {
                /*
                 * The direction is checked here as well, so that the assertions do not depend on how the
                 * repository interprets the end criteria.
                 */
                if ((relationship != null)
                            && (end1GUID.equals(relationship.getElementGUIDAtEnd1()))
                            && (end2GUID.equals(relationship.getElementGUIDAtEnd2())))
                {
                    relationships.add(relationship);
                }
            }
        }

        return relationships;
    }


    /**
     * Retrieve the single relationship of a type running from one element to another for a particular
     * information supply chain.
     *
     * @param openMetadataStore store to read through
     * @param relationshipTypeName type of relationship
     * @param end1GUID element at end 1
     * @param end2GUID element at end 2
     * @param iscQualifiedName information supply chain - may be null
     * @return the relationship, or null if there is none
     * @throws Exception the retrieval failed
     */
    static OpenMetadataRelationship getRelationshipForSupplyChain(OpenMetadataStore openMetadataStore,
                                                                  String            relationshipTypeName,
                                                                  String            end1GUID,
                                                                  String            end2GUID,
                                                                  String            iscQualifiedName) throws Exception
    {
        for (OpenMetadataRelationship relationship : getRelationships(openMetadataStore, relationshipTypeName, end1GUID, end2GUID))
        {
            String relationshipSupplyChain = getISCQualifiedName(relationship);

            if ((iscQualifiedName == null) ? (relationshipSupplyChain == null) : iscQualifiedName.equals(relationshipSupplyChain))
            {
                return relationship;
            }
        }

        return null;
    }


    /**
     * Return the information supply chain named on a relationship.
     *
     * @param relationship relationship to read
     * @return qualified name or null
     */
    static String getISCQualifiedName(OpenMetadataRelationship relationship)
    {
        final String methodName = "getISCQualifiedName";

        return propertyHelper.getStringProperty(QUALIFIED_NAME_PREFIX,
                                                OpenMetadataProperty.ISC_QUALIFIED_NAME.name,
                                                relationship.getRelationshipProperties(),
                                                methodName);
    }


    /**
     * Return the derivation of a relationship - the rule that derived it.
     *
     * @param relationship relationship to read
     * @return derivation or null
     */
    static String getDerivation(OpenMetadataRelationship relationship)
    {
        final String methodName = "getDerivation";

        return propertyHelper.getStringProperty(QUALIFIED_NAME_PREFIX,
                                                OpenMetadataProperty.DERIVATION.name,
                                                relationship.getRelationshipProperties(),
                                                methodName);
    }


    /**
     * Return the label of a relationship - the summary of the data that flows.
     *
     * @param relationship relationship to read
     * @return label or null
     */
    static String getLabel(OpenMetadataRelationship relationship)
    {
        final String methodName = "getLabel";

        return propertyHelper.getStringProperty(QUALIFIED_NAME_PREFIX,
                                                OpenMetadataProperty.LABEL.name,
                                                relationship.getRelationshipProperties(),
                                                methodName);
    }


    /**
     * Return who created an instance.
     *
     * @param header instance header
     * @return userId or null
     */
    static String getCreatedBy(ElementControlHeader header)
    {
        if ((header != null) && (header.getVersions() != null))
        {
            return header.getVersions().getCreatedBy();
        }

        return null;
    }


    /**
     * Was this instance created by Darwin?  Darwin runs under its own userId, and that is how its own work is
     * told apart from the fixture's assertions.
     *
     * @param header instance header
     * @return boolean
     */
    static boolean isCreatedByDarwin(ElementControlHeader header)
    {
        return DARWIN_USER_ID.equals(getCreatedBy(header));
    }


    /**
     * Retrieve the Exception relationships attached to a digital product.  The product is at end 1; the
     * exception type is at end 2 and is the related element.
     *
     * @param openMetadataStore store to read through
     * @param productGUID product to look at
     * @return list - empty if there are none
     * @throws Exception the retrieval failed
     */
    static List<RelatedMetadataElement> getExceptions(OpenMetadataStore openMetadataStore,
                                                      String            productGUID) throws Exception
    {
        List<RelatedMetadataElement> exceptions = new ArrayList<>();

        RelatedMetadataElementList retrievedExceptions = openMetadataStore.getRelatedMetadataElements(productGUID,
                                                                                                      1,
                                                                                                      OpenMetadataType.EXCEPTION_RELATIONSHIP.typeName,
                                                                                                      lineageQueryOptions());

        if ((retrievedExceptions != null) && (retrievedExceptions.getElementList() != null))
        {
            for (RelatedMetadataElement exception : retrievedExceptions.getElementList())
            {
                if (exception != null)
                {
                    exceptions.add(exception);
                }
            }
        }

        return exceptions;
    }


    /**
     * Return the relationships that an Exception relationship names as affected.
     *
     * @param exception the exception relationship and its exception type
     * @return list of relationship GUIDs or null
     */
    static List<String> getAffectedRelationships(RelatedMetadataElement exception)
    {
        final String methodName = "getAffectedRelationships";

        ElementProperties properties = exception.getRelationshipProperties();

        return propertyHelper.getStringArrayProperty(QUALIFIED_NAME_PREFIX,
                                                     OpenMetadataProperty.AFFECTED_RELATIONSHIPS.name,
                                                     properties,
                                                     methodName);
    }


    /**
     * Return the qualified name of an element.
     *
     * @param element element to read
     * @return qualified name or null
     */
    static String getQualifiedName(OpenMetadataElement element)
    {
        final String methodName = "getQualifiedName";

        if (element == null)
        {
            return null;
        }

        return propertyHelper.getStringProperty(QUALIFIED_NAME_PREFIX,
                                                OpenMetadataProperty.QUALIFIED_NAME.name,
                                                element.getElementProperties(),
                                                methodName);
    }
}
