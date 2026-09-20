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
                                                               COLUMN_PRESENT_TARGET_GUID);


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
     * Return the label recorded on a relationship.
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
