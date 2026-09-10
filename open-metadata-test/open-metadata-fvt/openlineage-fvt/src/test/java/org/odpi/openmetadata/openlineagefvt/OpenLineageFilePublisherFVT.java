/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openlineagefvt;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageRunEvent;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Checks that the File-based Open Lineage Log Store connector writes every event that reaches the integration
 * daemon to its folder, laid out by namespace and job name, with the event JSON unchanged.  The content pack
 * configures the connector's folder as {@code logs/openlineage} relative to the platform's working directory,
 * which for this suite is the module directory.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class OpenLineageFilePublisherFVT
{
    private static final File LOG_STORE = new File("logs/openlineage");


    /**
     * Publish an event and find it in the log store.
     *
     * @throws Exception test failure
     */
    @Test
    @DisplayName("Every event reaching the daemon is written to the file-based log store")
    void eventIsWrittenToTheLogStore() throws Exception
    {
        UUID   runId = UUID.randomUUID();
        String json  = OpenLineageFvtTestSupport.publish(OpenLineageEventFactory.runEvent("START", Instant.now(), runId, OpenLineageEventFactory.JOB_NAME));

        OpenLineageFvtTestSupport.waitFor("the event for run " + runId + " to be written under " + LOG_STORE.getAbsolutePath(), () -> findEventFile(LOG_STORE, runId.toString()) != null);

        File eventFile = findEventFile(LOG_STORE, runId.toString());

        assertTrue(eventFile.getPath().contains(OpenLineageEventFactory.JOB_NAME), "The event should be filed under its job name: " + eventFile.getPath());
        assertTrue(eventFile.getName().endsWith("-START.openlineageevent"), "The file name should end with the event type: " + eventFile.getName());

        String stored = Files.readString(eventFile.toPath(), StandardCharsets.UTF_8);

        assertTrue(stored.contains(runId.toString()), "The stored event should be the raw event that was published");
        assertTrue(stored.contains(OpenLineageEventFactory.JOB_DESCRIPTION), "The stored event should keep the facets that were published");
        assertTrue(stored.length() >= json.length() - 2, "The stored event should not be truncated");
    }


    /**
     * Find the first event file below a directory whose name contains a run id.
     *
     * @param directory directory to search
     * @param runId run id
     * @return file or null
     */
    private static File findEventFile(File   directory,
                                      String runId)
    {
        File[] files = directory.listFiles();

        if (files == null)
        {
            return null;
        }

        for (File file : files)
        {
            if (file.isDirectory())
            {
                File found = findEventFile(file, runId);

                if (found != null)
                {
                    return found;
                }
            }
            else if ((file.getName().contains(runId)) && (file.getName().endsWith(".openlineageevent")))
            {
                return file;
            }
        }

        return null;
    }
}
