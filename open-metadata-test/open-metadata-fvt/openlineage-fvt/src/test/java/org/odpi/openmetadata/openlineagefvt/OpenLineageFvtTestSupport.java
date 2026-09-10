/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openlineagefvt;

import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageRunEvent;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.AssetClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextBase;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeleteMethod;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementStatus;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElementList;
import org.odpi.openmetadata.frameworks.openmetadata.search.DeleteOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyComparisonOperator;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.search.QueryOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.SearchProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * OpenLineageFvtTestSupport holds the helpers shared by the test classes: publishing events into the integration
 * daemon, waiting for the connectors to reflect them in the repository, reading the results and cleaning up.
 */
class OpenLineageFvtTestSupport
{
    static final int MAX_PAGE_SIZE = 500;

    /**
     * Every element created from this suite's events has a qualified name containing this marker (it is part of
     * every namespace the events use), which is what the clean-up looks for.
     */
    static final String TEST_MARKER = "openlineage-fvt";


    /**
     * Where the shared secrets store is.
     *
     * @return path
     */
    static String getSecretsStoreLocation()
    {
        return OMAGPlatformExtension.getProperty("openlineage.fvt.server.secrets.store",
                                                 "../../../open-metadata-resources/open-metadata-deployment/secrets/egeria-servers.omsecrets");
    }


    /**
     * Publish a run event to the integration daemon through its REST API - the same entry point that a processing
     * engine uses when it sends events straight to Egeria.
     *
     * @param event event bean
     * @return the JSON that was sent
     * @throws Exception problem publishing
     */
    static String publish(OpenLineageRunEvent event) throws Exception
    {
        String json = OpenLineageEventFactory.toJSON(event);

        OMAGPlatformExtension.getIntegrationDaemonClient().publishOpenLineageEvent(json);

        return json;
    }


    /**
     * Build the qualified name the cataloguer uses for a job's process.
     *
     * @param namespace job namespace
     * @param name job name
     * @return qualified name
     */
    static String processQualifiedName(String namespace,
                                       String name)
    {
        return OpenMetadataType.DEPLOYED_SOFTWARE_COMPONENT.typeName + "::" + namespace + "::" + name;
    }


    /**
     * Build the qualified name the cataloguer uses for a dataset's asset.
     *
     * @param typeName open metadata type the cataloguer chooses for the dataset
     * @param namespace dataset namespace
     * @param name dataset name
     * @return qualified name
     */
    static String assetQualifiedName(String typeName,
                                     String namespace,
                                     String name)
    {
        return typeName + "::" + namespace + "::" + name;
    }


    /**
     * Retrieve an asset (or process) by qualified name through the asset client, which returns the classifications
     * and properties as beans.
     *
     * @param qualifiedName qualified name
     * @return element or null
     * @throws Exception repository problem
     */
    static OpenMetadataRootElement getAsset(String qualifiedName) throws Exception
    {
        AssetClient assetClient = ConnectorContextFactory.newContext().getAssetClient();

        return assetClient.getAssetByUniqueName(qualifiedName, OpenMetadataProperty.QUALIFIED_NAME.name, assetClient.getGetOptions());
    }


    /**
     * Wait for an asset (or process) with a qualified name to appear.
     *
     * @param qualifiedName qualified name
     * @param description what is being waited for, for the failure message
     * @return the element
     * @throws Exception timed out or repository problem
     */
    static OpenMetadataRootElement waitForAsset(String qualifiedName,
                                                String description) throws Exception
    {
        List<OpenMetadataRootElement> holder = new ArrayList<>();

        waitFor(description + " (" + qualifiedName + ")", () ->
        {
            OpenMetadataRootElement element = getAsset(qualifiedName);

            if (element != null)
            {
                holder.add(element);
                return true;
            }

            return false;
        });

        return holder.get(0);
    }


    /**
     * Wait for an asset with a qualified name to disappear (be deleted or archived).
     *
     * @param qualifiedName qualified name
     * @param description what is being waited for, for the failure message
     * @throws Exception timed out or repository problem
     */
    static void waitForAssetToGo(String qualifiedName,
                                 String description) throws Exception
    {
        waitFor(description + " (" + qualifiedName + ")", () -> getAsset(qualifiedName) == null);
    }


    /**
     * Wait for the latest version of an asset to satisfy a condition - used when the element exists but the
     * connector has still to update it.
     *
     * @param qualifiedName qualified name
     * @param description what is being waited for
     * @param condition test on the element
     * @return the element in the state that satisfied the condition
     * @throws Exception timed out or repository problem
     */
    static OpenMetadataRootElement waitForAsset(String                qualifiedName,
                                                String                description,
                                                ElementCondition      condition) throws Exception
    {
        List<OpenMetadataRootElement> holder = new ArrayList<>();

        waitFor(description + " (" + qualifiedName + ")", () ->
        {
            OpenMetadataRootElement element = getAsset(qualifiedName);

            if ((element != null) && (condition.isMet(element)))
            {
                holder.clear();
                holder.add(element);
                return true;
            }

            return false;
        });

        return holder.get(0);
    }


    /**
     * A condition on an element.
     */
    interface ElementCondition
    {
        /**
         * Is the element in the state being waited for?
         *
         * @param element element
         * @return boolean
         * @throws Exception problem examining it
         */
        boolean isMet(OpenMetadataRootElement element) throws Exception;
    }


    /**
     * Return the elements related to an element through a relationship type.
     *
     * @param elementGUID starting element
     * @param startingAtEnd 0 for either end, 1 or 2 for a specific end
     * @param relationshipTypeName relationship type
     * @return related elements (never null)
     * @throws Exception repository problem
     */
    static List<RelatedMetadataElement> getRelatedElements(String elementGUID,
                                                           int    startingAtEnd,
                                                           String relationshipTypeName) throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();

        RelatedMetadataElementList related = openMetadataStore.getRelatedMetadataElements(elementGUID, startingAtEnd, relationshipTypeName, 0, MAX_PAGE_SIZE);

        if ((related == null) || (related.getElementList() == null))
        {
            return new ArrayList<>();
        }

        return related.getElementList();
    }


    /**
     * Is there a relationship of a type between two elements (in either direction)?
     *
     * @param elementGUID one element
     * @param otherElementGUID other element
     * @param relationshipTypeName relationship type
     * @return boolean
     * @throws Exception repository problem
     */
    static boolean areLinked(String elementGUID,
                             String otherElementGUID,
                             String relationshipTypeName) throws Exception
    {
        for (RelatedMetadataElement related : getRelatedElements(elementGUID, 0, relationshipTypeName))
        {
            if ((related.getElement() != null) && (otherElementGUID.equals(related.getElement().getElementGUID())))
            {
                return true;
            }
        }

        return false;
    }


    /**
     * A condition that can be polled.
     */
    interface WaitableCondition
    {
        /**
         * Has the thing being waited for happened yet?
         *
         * @return true when it has
         * @throws Exception checking threw - treated as fatal rather than as "not yet"
         */
        boolean isMet() throws Exception;
    }


    /**
     * Poll a condition until it is met or the event timeout expires.
     *
     * @param description what is being waited for, for the failure message
     * @param condition condition
     * @throws Exception timed out (AssertionError) or the condition threw
     */
    static void waitFor(String            description,
                        WaitableCondition condition) throws Exception
    {
        long timeoutMilliseconds = OMAGPlatformExtension.getLongProperty("openlineage.fvt.event.timeout.seconds", 120) * 1000;
        long pollMilliseconds    = OMAGPlatformExtension.getLongProperty("openlineage.fvt.event.poll.seconds", 2) * 1000;
        long giveUpTime          = System.currentTimeMillis() + timeoutMilliseconds;

        while (System.currentTimeMillis() < giveUpTime)
        {
            if (condition.isMet())
            {
                return;
            }

            Thread.sleep(pollMilliseconds);
        }

        throw new AssertionError(description + " did not happen within " + (timeoutMilliseconds / 1000)
                                         + " seconds.  The audit log at build/openlineage-fvt-data/logs/audit.log says what the"
                                         + " connectors were doing while this test waited.");
    }


    /**
     * Purge everything a previous run left behind.  Every element created from the suite's events has the test
     * marker in its qualified name; the elements anchored to them (schemas, annotations, survey reports, runs) go
     * with them through the cascaded delete.
     *
     * @throws Exception repository problem
     */
    static void cleanUpLeftoverTestElements() throws Exception
    {
        if (! OMAGPlatformExtension.getBooleanProperty("openlineage.fvt.clear.down", true))
        {
            System.out.println("openlineage-fvt: leaving previous runs' metadata in place - openlineage.fvt.clear.down is false.");
            return;
        }

        ConnectorContextBase connectorContext  = ConnectorContextFactory.newContext(DeleteMethod.PURGE);
        OpenMetadataStore    openMetadataStore = connectorContext.getOpenMetadataStore();
        PropertyHelper       propertyHelper    = new PropertyHelper();

        SearchProperties searchProperties = new SearchProperties();

        searchProperties.setConditions(propertyHelper.addStringProperty(null,
                                                                        OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                        TEST_MARKER,
                                                                        PropertyComparisonOperator.LIKE));

        QueryOptions queryOptions = new QueryOptions();

        queryOptions.setLimitResultsByStatus(List.of(ElementStatus.ACTIVE, ElementStatus.DELETED));
        queryOptions.setPageSize(MAX_PAGE_SIZE);
        queryOptions.setForLineage(true);

        int purgedCount    = 0;
        int emptyPassLimit = 50;

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
            System.out.println("openlineage-fvt: purged " + purgedCount + " leftover test element(s) from a previous run before starting");
        }
    }


    /**
     * Remove the events a previous run left in the file-based log store, so that the Lovelace services analyse
     * only this run's events.  The log store is the file publisher's folder under the module directory.
     */
    static void cleanUpLogStore()
    {
        if (! OMAGPlatformExtension.getBooleanProperty("openlineage.fvt.clear.down", true))
        {
            return;
        }

        File logStore = new File("logs/openlineage");

        if (logStore.isDirectory())
        {
            int removed = deleteTree(logStore);

            System.out.println("openlineage-fvt: removed " + removed + " file(s) left in " + logStore.getAbsolutePath() + " by a previous run");
        }
    }


    /**
     * Delete a directory tree.
     *
     * @param file file or directory
     * @return number of files removed
     */
    private static int deleteTree(File file)
    {
        int removed = 0;

        File[] children = file.listFiles();

        if (children != null)
        {
            for (File child : children)
            {
                removed += deleteTree(child);
            }
        }

        if (file.delete() && (children == null))
        {
            removed++;
        }

        return removed;
    }


    /**
     * Soft-delete then purge an element - a purge alone fails on a live element, and a soft delete alone leaves
     * it in the repository.
     *
     * @param openMetadataStore store
     * @param elementGUID element
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
            // Best-effort - the element may already be soft-deleted.
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
            // Best-effort clean-up - nothing further can be done if this fails.
        }
    }
}
