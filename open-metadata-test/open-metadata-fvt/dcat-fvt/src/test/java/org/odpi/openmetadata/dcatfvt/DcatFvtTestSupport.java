/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.dcatfvt;

import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * Shared helpers for the dcat-fvt tests: locating the sample documents, and waiting for the integration
 * daemon to finish work that happens in another server.
 */
class DcatFvtTestSupport
{
    /**
     * Page size used by every client the suite creates.
     */
    static final int MAX_PAGE_SIZE = 500;

    /**
     * Where the sample DCAT documents live, relative to the repository root.  They are the documents that the
     * Open Integration Framework's unit tests read.
     */
    static final String SAMPLE_DOCUMENTS_PATH = "open-metadata-implementation/frameworks/open-integration-framework/src/test/resources/dcat";

    /**
     * The directory the DCAT Publisher writes to - the connector's default endpoint, resolved against this
     * module's directory because that is the working directory of the test JVM.
     */
    static final File STORE_DIRECTORY = new File("distribution-hub/dcat-catalog");

    /**
     * The directory the DCAT Files Receiver monitors by default, resolved the same way.
     */
    static final File LOADING_BAY_DIRECTORY = new File("loading-bay/dcat");


    private DcatFvtTestSupport()
    {
    }


    /**
     * Locate the repository root by walking up from the working directory until a directory containing
     * "content-packs" is found.  Gradle's test worker does not always use this module's directory as its
     * working directory, so the number of levels cannot be assumed.
     *
     * @return repository root
     */
    static File findRepositoryRoot()
    {
        File candidate = new File(System.getProperty("user.dir")).getAbsoluteFile();

        for (int levelsUp = 0; levelsUp < 10; levelsUp++)
        {
            if (new File(candidate, "content-packs").isDirectory())
            {
                return candidate;
            }

            File parent = candidate.getParentFile();

            if (parent == null)
            {
                break;
            }

            candidate = parent;
        }

        throw new IllegalStateException("Could not locate the repository root by walking up from " + System.getProperty("user.dir"));
    }


    /**
     * Read one of the sample documents.
     *
     * @param relativeName path below the sample documents directory, for example "air-quality-dataset.jsonld"
     * @return document text
     * @throws Exception the file could not be read
     */
    static String readSampleDocument(String relativeName) throws Exception
    {
        File file = new File(new File(findRepositoryRoot(), SAMPLE_DOCUMENTS_PATH), relativeName);

        if (! file.isFile())
        {
            throw new IllegalStateException("Sample document " + file.getPath() + " is missing.");
        }

        return Files.readString(file.toPath(), StandardCharsets.UTF_8);
    }


    /**
     * Wait until an element with the supplied qualified name is in the repository, failing the test with the
     * supplied description if it does not appear within the configured timeout.
     *
     * @param openMetadataStore store to query
     * @param qualifiedName qualified name to look for
     * @param description what the caller was waiting for, used in the failure message
     * @return the element
     * @throws Exception problem querying the repository
     */
    static OpenMetadataElement waitForElement(OpenMetadataStore openMetadataStore,
                                              String            qualifiedName,
                                              String            description) throws Exception
    {
        long timeoutMilliseconds = OMAGPlatformExtension.getLongProperty("dcat.fvt.refresh.timeout.seconds", 120) * 1000;
        long pollMilliseconds    = OMAGPlatformExtension.getLongProperty("dcat.fvt.refresh.poll.seconds", 2) * 1000;
        long giveUpTime          = System.currentTimeMillis() + timeoutMilliseconds;

        while (true)
        {
            OpenMetadataElement element = openMetadataStore.getMetadataElementByUniqueName(qualifiedName, OpenMetadataProperty.QUALIFIED_NAME.name);

            if (element != null)
            {
                return element;
            }

            if (System.currentTimeMillis() > giveUpTime)
            {
                fail(description + ": no element with qualifiedName '" + qualifiedName + "' appeared in " + OMAGPlatformExtension.METADATA_STORE_NAME
                             + " within " + (timeoutMilliseconds / 1000) + " seconds.  Check the integration daemon's audit log in build/dcat-fvt-data/logs/audit.log.");
            }

            Thread.sleep(pollMilliseconds);
        }
    }


    /**
     * Wait until a condition holds, failing the test with the supplied description otherwise.
     *
     * @param description what the caller was waiting for
     * @param condition condition to poll
     * @throws Exception interrupted
     */
    static void waitFor(String            description,
                        Supplier<Boolean> condition) throws Exception
    {
        long timeoutMilliseconds = OMAGPlatformExtension.getLongProperty("dcat.fvt.refresh.timeout.seconds", 120) * 1000;
        long pollMilliseconds    = OMAGPlatformExtension.getLongProperty("dcat.fvt.refresh.poll.seconds", 2) * 1000;
        long giveUpTime          = System.currentTimeMillis() + timeoutMilliseconds;

        while (! condition.get())
        {
            if (System.currentTimeMillis() > giveUpTime)
            {
                fail(description + " did not happen within " + (timeoutMilliseconds / 1000) + " seconds.");
            }

            Thread.sleep(pollMilliseconds);
        }
    }


    /**
     * Delete a directory tree.
     *
     * @param directory directory to remove (ignored if absent)
     */
    static void deleteRecursively(File directory)
    {
        if (directory.isDirectory())
        {
            File[] children = directory.listFiles();

            if (children != null)
            {
                for (File child : children)
                {
                    deleteRecursively(child);
                }
            }
        }

        if (directory.exists() && (! directory.delete()))
        {
            throw new IllegalStateException("Could not delete " + directory.getPath());
        }
    }
}
