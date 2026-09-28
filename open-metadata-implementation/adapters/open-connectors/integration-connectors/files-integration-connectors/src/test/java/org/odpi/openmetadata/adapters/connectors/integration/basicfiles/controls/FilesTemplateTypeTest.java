/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.basicfiles.controls;

import org.testng.annotations.Test;

import java.util.Map;

import static org.testng.Assert.assertEquals;


/**
 * Verify the default template map is keyed the way the file cataloguers look it up.
 */
public class FilesTemplateTypeTest
{
    /**
     * DataFilesMonitorForTarget looks up the template using the deployed implementation type from the file
     * classifier.  If the map is keyed any other way, every lookup misses and each file is catalogued with a
     * basic file connection - which, for example, the CSV survey rejects.
     */
    @Test public void testDefaultTemplatesKeyedByDeployedImplementationType()
    {
        Map<String, String> defaultTemplates = FilesTemplateType.getDefaultFileTemplates();

        for (FilesTemplateType filesTemplateType : FilesTemplateType.values())
        {
            assertEquals(defaultTemplates.get(filesTemplateType.getDeployedImplementationType().getDeployedImplementationType()),
                         filesTemplateType.getTemplateGUID(),
                         filesTemplateType.name());
        }

        assertEquals(defaultTemplates.size(), FilesTemplateType.values().length, "Duplicate template keys");
    }
}
