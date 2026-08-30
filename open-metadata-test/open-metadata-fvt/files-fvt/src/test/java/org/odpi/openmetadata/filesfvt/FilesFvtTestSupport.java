/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.filesfvt;

import org.odpi.openmetadata.contentpacks.core.RequestTypeDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextBase;
import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderProperty;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeleteMethod;
import org.odpi.openmetadata.frameworks.opengovernance.controls.ActionTarget;
import org.odpi.openmetadata.frameworks.openmetadata.properties.NewActionTarget;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElementList;
import org.odpi.openmetadata.frameworks.openmetadata.search.DeleteOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementStatus;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyComparisonOperator;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.search.QueryOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.SearchProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Shared conventions and helpers for the files-fvt suite: how this run names the things it creates, how it
 * finds them again, how it builds the directory tree it asks Egeria to catalogue, and how it waits for work
 * that is being done by another server.
 * <br>
 * Two kinds of debris have to be dealt with, and they live in different places.  In the <b>repository</b>,
 * every element this suite causes to be created carries {@link #TEST_MARKER} somewhere in its qualified name
 * - not because the suite sets those qualified names itself (the content pack's catalog templates do that)
 * but because the suite chooses the folder names the templates build them from.  On <b>disk</b>, the suite
 * builds a directory tree of its own rather than cataloguing whatever happens to be on the machine, so that
 * assertions can name exactly what should have been found.
 * <br>
 * Both are cleared at the <em>start</em> of a run rather than the end.  Clearing up afterwards leaves the
 * debris behind whenever a run is killed or crashes - which is exactly when it is most likely to be in a
 * state the next run should not inherit.
 */
final class FilesFvtTestSupport
{
    /**
     * Marker carried by everything this suite creates, directly or through a governance action.  Folder names
     * given to the catalog templates start with it, and the templates build their qualified names from those
     * folder names, so a search for this string finds the whole graph a run produced.
     */
    static final String TEST_MARKER = "files-fvt";

    /**
     * Page size used by every client this suite creates, and configured on every server it starts.
     */
    static final int MAX_PAGE_SIZE = 500;

    private FilesFvtTestSupport()
    {
        // no instances
    }

    /**
     * Return the qualified name a governance action type is registered under, which is what the Automated
     * Curation API takes - not the request type on its own.
     * <br>
     * The two are easy to confuse and the difference is not visible until a request is refused: the pack
     * defines the request type as {@code survey-folder}, and registers the action type as
     * {@code FileSurvey::survey-folder} - the engine that runs it, then the request type.  Asking for the
     * bare request type is answered with OMAG-GENERIC-HANDLERS-400-013, "the name is not recognized", which
     * says nothing about the name it wanted instead.  That has cost real time: a survey was reported as
     * broken when the request naming it was simply never accepted, and pyegeria's own
     * {@code initiate_file_folder_survey} default and the {@code .http} examples were both wrong in the same
     * way.
     * <br>
     * It is built from the pack's own definitions rather than written out, so a rename of either half is
     * picked up here rather than turning into a request the server rejects.
     *
     * @param requestType the request type definition from the content pack
     * @return the qualified name to ask for
     */
    static String governanceActionTypeQualifiedName(RequestTypeDefinition requestType)
    {
        return requestType.getGovernanceEngine().getName() + "::" + requestType.getGovernanceRequestType();
    }


    /**
     * Return the name to use for a folder this suite asks Egeria to catalogue or survey.
     * <br>
     * Each test uses a different name because the catalog template builds the asset's qualified name from the
     * path - two tests using one folder would be creating and deleting the same element, and would interfere
     * with each other in ways that depend on the order they happened to run in.  The names are stable across
     * runs rather than random, so that a run which is killed part-way leaves debris the next run recognises
     * and removes.
     *
     * @param purpose short label for what the test is doing, for example "survey"
     * @return folder name, carrying the marker every clean-up searches for
     */
    static String folderUnderTestName(String purpose)
    {
        return TEST_MARKER + "-" + purpose;
    }


    /**
     * Return the directory this suite builds its tree under, as an absolute path.
     * <br>
     * A relative setting is resolved against this module's project directory, which is Gradle's working
     * directory for the test task, so the default lands inside the module's own build directory.
     *
     * @return absolute directory
     */
    static File getDataDirectory()
    {
        String configured = OMAGPlatformExtension.getProperty("files.fvt.data.directory", "build/files-fvt-data/tree");

        File directory = new File(configured);

        return directory.isAbsolute() ? directory : directory.getAbsoluteFile();
    }


    /**
     * Return the folder one test works on, as an absolute path.
     *
     * @param purpose short label for what the test is doing
     * @return the folder, which prepareTreeUnderTest() has already created
     */
    static File folderUnderTest(String purpose)
    {
        return new File(getDataDirectory(), folderUnderTestName(purpose));
    }


    /**
     * Return the qualified name the FileFolder catalog template gives a folder asset, so that a test can look
     * the asset up by the name the template chose rather than by the GUID it happened to be given.
     * <br>
     * The shape is the template's, not this suite's - see DataAssetTemplateDefinition.FILE_FOLDER_TEMPLATE,
     * whose qualified name is the type name, the file system name and the path name joined.  A test that
     * built this string itself would keep working after the template changed, and quietly stop testing the
     * template.
     *
     * @param folder the folder on disk
     * @return qualified name to search for
     */
    static String folderAssetQualifiedName(File folder)
    {
        return OpenMetadataType.FILE_FOLDER.typeName + "::" + getFileSystemName() + ":" + folder.getAbsolutePath();
    }


    /**
     * Return the file system name this suite catalogues its folders under.
     * <br>
     * The templates take this as a placeholder and put it in the qualified name, so it has to be the same
     * every time a given folder is catalogued, and different from anything else on the machine.  The marker
     * gives both.
     *
     * @return file system name
     */
    static String getFileSystemName()
    {
        return TEST_MARKER;
    }


    /**
     * Return the placeholder values the file and folder catalog templates need.
     * <br>
     * These are the values the governance action processes pass on to the template, and they are what turns
     * one template into an asset for a particular directory.  Every placeholder the template declares has to
     * be supplied: one left out is not rejected, it is substituted as an empty string and leaves a qualified
     * name that no search will match.
     *
     * @param folder folder the asset is being created for
     * @return placeholder values
     */
    static Map<String, String> folderTemplatePlaceholders(File folder)
    {
        Map<String, String> placeholders = new HashMap<>();

        placeholders.put(PlaceholderProperty.FILE_SYSTEM_NAME.getName(), getFileSystemName());
        placeholders.put(PlaceholderProperty.DIRECTORY_PATH_NAME.getName(), folder.getAbsolutePath());
        placeholders.put(PlaceholderProperty.DIRECTORY_NAME.getName(), folder.getName());
        placeholders.put(PlaceholderProperty.DIRECTORY_ADDRESS.getName(), folder.getAbsolutePath());
        placeholders.put(PlaceholderProperty.VERSION_IDENTIFIER.getName(), "V1.0");
        placeholders.put(PlaceholderProperty.DESCRIPTION.getName(), "Folder created by the files-fvt suite.");

        return placeholders;
    }


    /**
     * Return the placeholder values the file catalog templates need.
     *
     * @param file file the asset is being created for
     * @return placeholder values
     */
    static Map<String, String> fileTemplatePlaceholders(File file)
    {
        String fileName      = file.getName();
        int    lastDot       = fileName.lastIndexOf('.');
        String fileExtension = (lastDot > 0) ? fileName.substring(lastDot + 1) : "";

        Map<String, String> placeholders = new HashMap<>();

        placeholders.put(PlaceholderProperty.FILE_SYSTEM_NAME.getName(), getFileSystemName());
        placeholders.put(PlaceholderProperty.FILE_PATH_NAME.getName(), file.getAbsolutePath());
        placeholders.put(PlaceholderProperty.FILE_NAME.getName(), fileName);
        placeholders.put(PlaceholderProperty.FILE_TYPE.getName(), fileExtension);
        placeholders.put(PlaceholderProperty.FILE_EXTENSION.getName(), fileExtension);
        placeholders.put(PlaceholderProperty.FILE_ENCODING.getName(), "UTF-8");
        placeholders.put(PlaceholderProperty.FILE_ADDRESS.getName(), file.getAbsolutePath());
        placeholders.put(PlaceholderProperty.VERSION_IDENTIFIER.getName(), "V1.0");
        placeholders.put(PlaceholderProperty.DESCRIPTION.getName(), "File created by the files-fvt suite.");
        placeholders.put(PlaceholderProperty.DEPLOYED_IMPLEMENTATION_TYPE.getName(), "");

        return placeholders;
    }


    /**
     * Build an action target that hands one element to a governance service.
     * <br>
     * The name is a parameter rather than a constant because governance services differ on how much they care
     * about it.  A survey service takes whatever asset it is given, so any name will do.  The catalog-target
     * service looks for one called {@code newAsset} specifically, and answers a target under any other name
     * with GOVERNANCE-ACTION-CONNECTORS-0033, "has not been passed a newAsset action target" - the target is
     * delivered, it is simply never found.  Passing the name in makes each caller state which it needs.
     *
     * @param actionTargetName the name the receiving service looks for - see ActionTarget
     * @param elementGUID the element to hand over
     * @return action target
     */
    static NewActionTarget newActionTarget(String actionTargetName,
                                           String elementGUID)
    {
        NewActionTarget actionTarget = new NewActionTarget();

        actionTarget.setActionTargetName(actionTargetName);
        actionTarget.setActionTargetGUID(elementGUID);

        return actionTarget;
    }


    /**
     * Return the GUID at the other end of one relationship, whichever end it is stored at.
     *
     * @param openMetadataStore store to read through
     * @param elementGUID element to start from
     * @param relationshipTypeName relationship to follow
     * @return the GUID at the far end, or null if there is no such relationship
     * @throws Exception problem reading from the repository
     */
    static String getRelatedGUID(OpenMetadataStore openMetadataStore,
                                 String            elementGUID,
                                 String            relationshipTypeName) throws Exception
    {
        RelatedMetadataElementList related = openMetadataStore.getRelatedMetadataElements(elementGUID,
                                                                                           0,
                                                                                           relationshipTypeName,
                                                                                           0,
                                                                                           MAX_PAGE_SIZE);

        if ((related != null) && (related.getElementList() != null))
        {
            for (RelatedMetadataElement relatedElement : related.getElementList())
            {
                if ((relatedElement != null) && (relatedElement.getElement() != null))
                {
                    return relatedElement.getElement().getElementGUID();
                }
            }
        }

        return null;
    }


    /**
     * Build the directory tree this suite catalogues and surveys, removing whatever an earlier run left.
     * <br>
     * The tree is this suite's equivalent of postgres-fvt's database under test, and it exists for the same
     * reason: a survey of a directory the suite built can be asserted against exactly - three data files, one
     * nested folder, these sizes - where a survey of whatever happens to be on the machine can only be
     * asserted to have produced something.  It also keeps a run from walking a tree that something else on
     * the machine is writing to.
     * <br>
     * Each folder gets the same shape, so that any test can make the same assertions about the one it was
     * given: three files directly inside it, and one nested folder holding one more.  The files are given
     * different extensions because that is what decides which catalog template a file cataloguer chooses for
     * them, and a tree of one file type would not exercise that choice at all.
     *
     * @throws Exception the tree could not be built, which is fatal to the run
     */
    static void prepareTreeUnderTest() throws Exception
    {
        File dataDirectory = getDataDirectory();

        if (getBooleanProperty("files.fvt.clear.down", true))
        {
            deleteRecursively(dataDirectory);
        }

        for (String purpose : FOLDER_PURPOSES)
        {
            File folder = folderUnderTest(purpose);

            if (! folder.mkdirs() && ! folder.isDirectory())
            {
                throw new IllegalStateException("Could not create the folder under test: " + folder.getAbsolutePath());
            }

            writeFile(new File(folder, "measurements.csv"), "id,name,reading\n1,alpha,3.5\n2,beta,4.25\n");
            writeFile(new File(folder, "notes.txt"), "Notes written by the files-fvt suite.\n");
            writeFile(new File(folder, "settings.json"), "{\"suite\": \"files-fvt\", \"purpose\": \"" + purpose + "\"}\n");

            File nestedFolder = new File(folder, NESTED_FOLDER_NAME);

            if (! nestedFolder.mkdirs() && ! nestedFolder.isDirectory())
            {
                throw new IllegalStateException("Could not create the nested folder: " + nestedFolder.getAbsolutePath());
            }

            writeFile(new File(nestedFolder, "archive.txt"), "Nested file written by the files-fvt suite.\n");
        }
    }


    /**
     * The folders this suite builds, one per test that needs one of its own.
     */
    static final List<String> FOLDER_PURPOSES = List.of("survey", "catalog", "template");

    /**
     * Name of the folder nested inside each folder under test.
     */
    static final String NESTED_FOLDER_NAME = "nested";

    /**
     * How many files sit directly inside each folder under test.
     */
    static final int FILES_IN_FOLDER = 3;

    /**
     * How many files sit inside the nested folder.
     */
    static final int FILES_IN_NESTED_FOLDER = 1;


    /**
     * Write one file, replacing anything already there.
     *
     * @param file file to write
     * @param content what to put in it
     * @throws Exception the file could not be written
     */
    private static void writeFile(File   file,
                                  String content) throws Exception
    {
        Files.writeString(file.toPath(), content);
    }


    /**
     * Remove a directory and everything under it, if it is there at all.
     *
     * @param directory directory to remove
     * @throws Exception something could not be removed
     */
    private static void deleteRecursively(File directory) throws Exception
    {
        if (! directory.exists())
        {
            return;
        }

        try (Stream<Path> paths = Files.walk(directory.toPath()))
        {
            /*
             * Deepest first, because a directory cannot be removed until it is empty.
             */
            List<Path> ordered = paths.sorted(Comparator.reverseOrder()).toList();

            for (Path path : ordered)
            {
                Files.deleteIfExists(path);
            }
        }
    }


    /**
     * Read a boolean setting, falling back to a default when it is absent or unreadable.
     *
     * @param propertyName name of the setting
     * @param defaultValue value to use when it is not set
     * @return the setting
     */
    private static boolean getBooleanProperty(String  propertyName,
                                              boolean defaultValue)
    {
        String value = OMAGPlatformExtension.getProperty(propertyName, Boolean.toString(defaultValue));

        return Boolean.parseBoolean(value);
    }


    /**
     * Permanently remove an element, whatever status it is in.  Failures are swallowed: this is best-effort
     * clean-up, not something a test should fail on.
     * <br>
     * PURGE only succeeds on an element that is already soft-deleted, so this soft-deletes first - itself
     * best-effort, since the element may already be deleted, for example by a cascade from its own anchor.
     * The delete cascades because most of what this suite creates is a graph: the catalog template creates the
     * folder asset, its connection and its endpoint in one act, all anchored to the folder.
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
            // Best-effort clean-up - nothing further can be done if this fails.
        }
    }


    /**
     * Find and permanently purge every element whose qualified name contains {@link #TEST_MARKER}, in any
     * status.  Called once, after the servers are activated and before any test runs, so that debris from an
     * earlier - possibly failed - run does not affect this run's assertions.
     * <br>
     * A contains match is used rather than a starts-with one because this suite does not choose most of these
     * qualified names.  The catalog templates do, and they put the marker in the middle: a folder asset is
     * "FileFolder::files-fvt:/.../files-fvt-survey", and a file catalogued inside it is
     * "DataFile::files-fvt:/.../files-fvt-survey/notes.txt".
     *
     * @throws Exception problem communicating with the server - fatal, since a dirty repository would
     * invalidate the whole run
     */
    static void cleanUpLeftoverTestElements() throws Exception
    {
        if (! OMAGPlatformExtension.getBooleanProperty("files.fvt.clear.down", true))
        {
            System.out.println("files-fvt: leaving previous runs' metadata in place - files.fvt.clear.down is false."
                                       + "  Tests that assert on what this run created may see an earlier run's elements too.");
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

        /*
         * Purging shrinks the result set, and a cascaded delete removes elements the current page has not
         * reached yet, so it is simplest - and safest against paging through a set that is disappearing
         * underneath the query - to keep re-querying from the start until nothing more comes back.
         */
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

        purgedCount = purgedCount + purgeLeftoverGovernanceWork(openMetadataStore);

        if (purgedCount > 0)
        {
            System.out.println("files-fvt: purged " + purgedCount + " leftover test element(s) from a previous run before starting");
        }
    }


    /**
     * Purge every engine action and governance action process instance in the repository.
     * <br>
     * These have to be removed by type rather than by qualified name, because they are the one thing this suite
     * causes to be created that does <em>not</em> carry {@link #TEST_MARKER}: their names are built from the
     * process step they run, not from the server name this suite chose.  The marker-based sweep above cannot
     * see them.
     * <br>
     * Leaving them behind is not merely untidy - it changes what the next run does.  An engine action that was
     * created but never claimed stays at REQUESTED or APPROVED for ever, and an engine host <em>sweeps for
     * exactly those</em> when its engines load their configuration.  So a run that ends with unclaimed actions
     * hands them to the next run's engine host, which dutifully carries them out: a previous run's assets get
     * created moments after start-up, and a survey whose action target this run has just purged fails with
     * "no asset action target supplied".  Both look like defects in the current run and are nothing of the
     * kind.
     * <br>
     * Purging by type is safe here because this is a dedicated FVT repository: nothing but this suite creates
     * engine actions in it.
     *
     * @param openMetadataStore store to purge through
     * @return number of elements purged
     * @throws Exception problem communicating with the server
     */
    private static int purgeLeftoverGovernanceWork(OpenMetadataStore openMetadataStore) throws Exception
    {
        int purgedCount = 0;

        for (String typeName : List.of(OpenMetadataType.ENGINE_ACTION.typeName,
                                       OpenMetadataType.GOVERNANCE_ACTION_PROCESS_INSTANCE.typeName))
        {
            QueryOptions queryOptions = new QueryOptions();

            queryOptions.setMetadataElementTypeName(typeName);
            queryOptions.setLimitResultsByStatus(List.of(ElementStatus.ACTIVE, ElementStatus.DELETED));
            queryOptions.setPageSize(MAX_PAGE_SIZE);
            queryOptions.setForLineage(true);

            int emptyPassLimit = 50;

            while (emptyPassLimit > 0)
            {
                List<OpenMetadataElement> found = openMetadataStore.findMetadataElements(null, null, queryOptions);

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
        }

        return purgedCount;
    }


    /*
     * =====================================================================================================
     * Waiting for work that is happening somewhere else
     */


    /**
     * A condition that a test is prepared to wait for.
     */
    @FunctionalInterface
    interface WaitableCondition
    {
        /**
         * Has the thing being waited for happened yet?
         *
         * @return true when it has
         * @throws Exception checking threw - treated as fatal rather than as "not yet", because a query that
         * cannot run will not start working on the next poll
         */
        boolean isMet() throws Exception;
    }


    /**
     * Wait until the supplied condition is true, or report that it never became true.
     * <br>
     * Almost everything this suite tests happens in another server, so almost every assertion has to wait for
     * it.  Waiting is free when the thing being waited for happens - the loop leaves as soon as the condition
     * holds - so the timeouts are set well above what a healthy deployment needs.
     *
     * @param description what is being waited for, used in the failure message
     * @param timeoutSecondsProperty name of the application.properties setting holding the limit
     * @param defaultTimeoutSeconds limit to use when that setting is absent
     * @param condition the thing being waited for
     * @throws Exception the condition never became true, or checking it threw
     */
    static void waitFor(String            description,
                        String            timeoutSecondsProperty,
                        long              defaultTimeoutSeconds,
                        WaitableCondition condition) throws Exception
    {
        long timeoutMilliseconds = OMAGPlatformExtension.getLongProperty(timeoutSecondsProperty, defaultTimeoutSeconds) * 1000;
        long pollMilliseconds    = OMAGPlatformExtension.getLongProperty("files.fvt.refresh.poll.seconds", 2) * 1000;
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
                                         + " seconds.  The audit log at build/files-fvt-data/logs/audit.log says what the"
                                         + " servers were doing while this test waited.");
    }


    /**
     * Wait for an element with the supplied qualified name to appear, and return it.
     *
     * @param openMetadataStore store to search
     * @param qualifiedName qualified name to wait for
     * @param description what this element is, used in the failure message
     * @return the element
     * @throws Exception it never appeared
     */
    static OpenMetadataElement waitForElement(OpenMetadataStore openMetadataStore,
                                              String            qualifiedName,
                                              String            description) throws Exception
    {
        List<OpenMetadataElement> holder = new ArrayList<>();

        waitFor(description + " (" + qualifiedName + ")",
                "files.fvt.refresh.timeout.seconds",
                180,
                () ->
                {
                    OpenMetadataElement element = openMetadataStore.getMetadataElementByUniqueName(qualifiedName,
                                                                                                   OpenMetadataProperty.QUALIFIED_NAME.name);

                    if (element != null)
                    {
                        holder.add(element);
                        return true;
                    }

                    return false;
                });

        return holder.get(0);
    }


    /*
     * =====================================================================================================
     * Assertions shared by more than one test
     */


    /**
     * Return one string property of an element, or null if it does not have one by that name.
     * <br>
     * {@code getPropertiesAsStrings} renders every property - primitives, arrays and maps alike - as a string,
     * which is what makes it usable for a check like "is the qualified name what the template should have
     * built" without the caller having to know the property's storage type.
     *
     * @param element element to read
     * @param propertyName name of the property wanted
     * @return property value as a string, or null
     */
    static String getStringProperty(OpenMetadataElement element,
                                    String              propertyName)
    {
        if ((element == null) || (element.getElementProperties() == null))
        {
            return null;
        }

        Map<String, String> properties = element.getElementProperties().getPropertiesAsStrings();

        return (properties == null) ? null : properties.get(propertyName);
    }


    /**
     * Search one set of properties for placeholder markers that were never substituted.
     * <br>
     * A placeholder appears in a catalog template as <code>~{variableName}~</code>.  Finding one in an element
     * created from that template means the substitution did not happen: the catalogued element is carrying a
     * variable name where a real value belongs, which is worse than an empty property because it looks like
     * data.  Every property is checked as its string form, which covers arrays and maps as well as plain
     * strings - a placeholder hidden in one entry of an additionalProperties map matters just as much as one
     * in a display name.
     *
     * @param location where these properties were found, for the failure message
     * @param properties properties to search, may be null
     * @return one description per property still holding a marker, empty if there are none
     */
    static List<String> findPlaceholders(String            location,
                                         ElementProperties properties)
    {
        List<String> found = new ArrayList<>();

        if (properties == null)
        {
            return found;
        }

        Map<String, String> asStrings = properties.getPropertiesAsStrings();

        if (asStrings == null)
        {
            return found;
        }

        for (Map.Entry<String, String> property : asStrings.entrySet())
        {
            String value = property.getValue();

            if ((value != null) && value.contains("~{") && value.contains("}~"))
            {
                found.add(location + " -> " + property.getKey() + " = \"" + value + "\"");
            }
        }

        return found;
    }
}
