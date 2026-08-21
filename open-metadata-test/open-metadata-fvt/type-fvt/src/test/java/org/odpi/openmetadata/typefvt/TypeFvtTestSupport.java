/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.typefvt;

import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataTypeDefAttribute;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataTypeDef;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextBase;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeleteMethod;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.search.DeleteOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementStatus;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyComparisonOperator;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.search.QueryOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.SearchProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;

import java.util.List;

/**
 * Shared conventions and helpers used across the type-fvt test suite.
 * <br>
 * Every element this suite creates uses a qualified name starting with {@link #QUALIFIED_NAME_PREFIX} -
 * this makes it possible to reliably find and purge test debris, both the debris a test leaves behind
 * itself (each test should clean up after itself in a {@code finally} block) and, more importantly,
 * anything left over from an earlier run that failed partway through: unlike open-metadata-bvt's
 * in-memory repository, this suite's PostgreSQL-backed repository persists across runs, so a crashed
 * previous run's half-finished elements would otherwise accumulate indefinitely and could confuse later
 * test assertions (for example, a run that failed partway through
 * leaving one instance of every entity type behind).
 */
final class TypeFvtTestSupport
{
    /**
     * Every qualified name this suite creates starts with this prefix, so leftover test elements from a
     * previous (possibly failed) run can always be found and purged before the next run's tests start.
     */
    static final String QUALIFIED_NAME_PREFIX = "type-fvt:";

    static final int MAX_PAGE_SIZE = ConnectorContextFactory.MAX_PAGE_SIZE;

    private TypeFvtTestSupport()
    {
        // no instances
    }


    /**
     * Add this suite's qualified name to the supplied properties, but only if the type actually has a
     * qualifiedName attribute to put it in.
     * <br>
     * Most types are Referenceables and do have one, which is what makes the leftover-debris sweep in
     * {@link #cleanUpLeftoverTestElements()} possible.  A handful - the feedback types Like and Rating,
     * SearchKeyword, TranslationDetail - descend straight from OpenMetadataRoot and have no qualifiedName;
     * sending one to those is rejected outright by the repository.  Elements of those types are still
     * purged by the test that created them, they just cannot be found again by a later sweep if a run dies
     * partway through.
     *
     * @param properties properties being built, may be null
     * @param typeDef type of the element being created
     * @param qualifiedName qualified name to set
     * @return properties, with qualifiedName added where the type allows it
     */
    static ElementProperties addQualifiedNameIfSupported(ElementProperties   properties,
                                                         OpenMetadataTypeDef typeDef,
                                                         String              qualifiedName)
    {
        for (OpenMetadataTypeDefAttribute attribute : TypeCatalog.instantiableAttributes(typeDef))
        {
            if (OpenMetadataProperty.QUALIFIED_NAME.name.equals(attribute.getAttributeName()))
            {
                return new PropertyHelper().addStringProperty(properties, OpenMetadataProperty.QUALIFIED_NAME.name, qualifiedName);
            }
        }

        return properties;
    }


    /**
     * Create a plain instance of a type, carrying nothing but this suite's qualified name where the type
     * supports one.  Used for the entities that classification and relationship tests need at hand - the
     * subject under test there is the classification or relationship, not the entity.
     *
     * @param openMetadataStore store to create through
     * @param entityTypeName type of entity to create
     * @param qualifiedName qualified name to give it
     * @return new element's GUID
     * @throws Exception the entity could not be created
     */
    static String createPlainElement(OpenMetadataStore openMetadataStore,
                                     String            entityTypeName,
                                     String            qualifiedName) throws Exception
    {
        ElementProperties elementProperties = addQualifiedNameIfSupported(null,
                                                                           TypeCatalog.typeDefinition(entityTypeName),
                                                                           qualifiedName);

        NewElementOptions newElementOptions = new NewElementOptions();

        newElementOptions.setIsOwnAnchor(true);

        return openMetadataStore.createMetadataElementInStore(entityTypeName,
                                                               newElementOptions,
                                                               null,
                                                               (elementProperties == null) ? null : new NewElementProperties(elementProperties),
                                                               null);
    }


    /**
     * Build a qualified name for a newly created test element that is unique to this test run and
     * recognisable as type-fvt debris if it is ever left behind.
     *
     * @param category short label for what kind of element this is (for example "Collection")
     * @return unique qualified name starting with {@link #QUALIFIED_NAME_PREFIX}
     */
    static String newQualifiedName(String category)
    {
        return QUALIFIED_NAME_PREFIX + category + ":" + java.util.UUID.randomUUID();
    }


    /**
     * Permanently remove an element, regardless of the delete method the supplied client was set up
     * with.  Used both for the leftover-element cleanup pass and for tests to tidy up after themselves
     * once they no longer need the element (including ones that were only ever soft-deleted, to check
     * status filtering, and now need to be fully removed).  Failures are swallowed - this is a
     * best-effort cleanup operation, not something a test should fail on.
     * <br><br>
     * PURGE only succeeds on an element that is already soft-deleted (the classic OMRS lifecycle enforced
     * by the repository connector), so this soft-deletes the element first - best-effort, since it may
     * already be deleted (for example by an earlier failed run, or by a cascade delete from one of its own
     * anchor/owner elements) - before purging it.
     * <br><br>
     * {@code forLineage} is set on both steps: an element left ARCHIVEd by a test (or a previous test's
     * LOOK_FOR_LINEAGE call) carries the Memento classification, and without forLineage=true it would be
     * invisible to the lookup each delete step needs to do first - the delete would silently no-op rather
     * than throw, so the element would never actually be removed.
     *
     * @param openMetadataStore store to delete through
     * @param elementGUID element to remove
     */
    static void purgeElement(OpenMetadataStore openMetadataStore,
                             String            elementGUID)
    {
        try
        {
            DeleteOptions softDeleteOptions = new DeleteOptions();

            softDeleteOptions.setDeleteMethod(DeleteMethod.SOFT_DELETE);
            softDeleteOptions.setCascadedDelete(true);
            softDeleteOptions.setForLineage(true);

            openMetadataStore.deleteMetadataElementInStore(elementGUID, softDeleteOptions);
        }
        catch (Exception ignored)
        {
            // Best-effort - see the method comment above for why this is expected to fail sometimes.
        }

        try
        {
            DeleteOptions purgeOptions = new DeleteOptions();

            purgeOptions.setDeleteMethod(DeleteMethod.PURGE);
            purgeOptions.setCascadedDelete(true);
            purgeOptions.setForLineage(true);

            openMetadataStore.deleteMetadataElementInStore(elementGUID, purgeOptions);
        }
        catch (Exception ignored)
        {
            // Best-effort cleanup - nothing further can be done if this fails.
        }
    }


    /**
     * Permanently remove a relationship.  Used by tests that create their own relationships to tidy up after
     * themselves, including ones that were only ever soft-deleted to check status filtering.  Failures are
     * swallowed - this is a best-effort cleanup operation, not something a test should fail on.
     * <br><br>
     * PURGE only succeeds on a relationship that is already soft-deleted (the classic OMRS lifecycle enforced
     * by the repository connector), so this soft-deletes the relationship first - best-effort, since it may
     * already be deleted - before purging it.
     *
     * @param openMetadataStore store to delete through
     * @param relationshipGUID relationship to remove
     */
    static void purgeRelationship(OpenMetadataStore openMetadataStore,
                                  String            relationshipGUID)
    {
        try
        {
            DeleteOptions softDeleteOptions = new DeleteOptions();

            softDeleteOptions.setDeleteMethod(DeleteMethod.SOFT_DELETE);

            openMetadataStore.deleteRelationshipInStore(relationshipGUID, softDeleteOptions);
        }
        catch (Exception ignored)
        {
            // Best-effort - see the method comment above for why this is expected to fail sometimes.
        }

        try
        {
            DeleteOptions purgeOptions = new DeleteOptions();

            purgeOptions.setDeleteMethod(DeleteMethod.PURGE);

            openMetadataStore.deleteRelationshipInStore(relationshipGUID, purgeOptions);
        }
        catch (Exception ignored)
        {
            // Best-effort cleanup - nothing further can be done if this fails.
        }
    }


    /**
     * Find and permanently purge every element whose qualified name starts with
     * {@link #QUALIFIED_NAME_PREFIX}, in any status (including elements that were only soft-deleted, and
     * ones that were ARCHIVEd - forLineage=true on the search is required to find those, since a Memento-
     * classified element is otherwise invisible to a default query).  Called once, after the server is
     * activated and before any test runs, so that debris from an earlier, possibly failed, run does not
     * affect this run's assertions.
     *
     * @throws Exception problem communicating with the server - fatal, since a dirty repository would
     * invalidate the whole run
     */
    static void cleanUpLeftoverTestElements() throws Exception
    {
        ConnectorContextBase connectorContext  = ConnectorContextFactory.newContext(DeleteMethod.PURGE);
        OpenMetadataStore     openMetadataStore = connectorContext.getOpenMetadataStore();
        PropertyHelper        propertyHelper    = new PropertyHelper();

        SearchProperties searchProperties = new SearchProperties();

        searchProperties.setConditions(propertyHelper.addStringProperty(null,
                                                                         OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                         QUALIFIED_NAME_PREFIX,
                                                                         PropertyComparisonOperator.STARTS_WITH));

        QueryOptions queryOptions = new QueryOptions();

        queryOptions.setLimitResultsByStatus(List.of(ElementStatus.ACTIVE, ElementStatus.DELETED));
        queryOptions.setPageSize(MAX_PAGE_SIZE);
        queryOptions.setForLineage(true);

        /*
         * Purging shrinks the result set, so it is simplest (and safest against paging off-by-ones while
         * elements are disappearing underneath the query) to keep re-querying from the start until
         * nothing more comes back, rather than trying to page through a moving target.
         */
        int  purgedCount    = 0;
        int  emptyPassLimit = 50;

        while (emptyPassLimit > 0)
        {
            List<OpenMetadataElement> found = openMetadataStore.findMetadataElements(searchProperties, null, queryOptions);

            if ((found == null) || found.isEmpty())
            {
                break;
            }

            for (OpenMetadataElement element : found)
            {
                purgeElement(openMetadataStore, element.getElementGUID());
                purgedCount++;
            }

            emptyPassLimit--;
        }

        if (purgedCount > 0)
        {
            System.out.println("type-fvt: purged " + purgedCount + " leftover test element(s) from a previous run before starting");
        }
    }
}
