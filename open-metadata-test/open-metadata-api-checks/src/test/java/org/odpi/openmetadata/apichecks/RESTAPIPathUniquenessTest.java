/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.apichecks;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * RESTAPIPathUniquenessTest checks that no view service resource class maps the same HTTP verb and path
 * twice.
 * <br><br>
 * Spring refuses to create its request mapping when two methods claim the same verb and path, and the
 * whole application context then fails to start.  That is invisible at compile time: the duplicate
 * mappings are just two annotations with the same value.  Without this check the first sign of trouble is
 * a platform that will not boot, which is a long way from the line that caused it.
 * <br><br>
 * The same path under different verbs is legal and is used deliberately - for example the multi-language
 * service has both a POST and a GET on /elements/&#123;elementGUID&#125;/translations - so the check keys on
 * the verb as well as the path.
 */
class RESTAPIPathUniquenessTest
{
    private static final Pattern MAPPING = Pattern.compile("@(Post|Get|Put|Delete)Mapping\\(\\s*(?:path\\s*=\\s*)?\"([^\"]*)\"");


    @Test
    @DisplayName("No view service resource maps the same verb and path twice")
    void mappingsAreUnique()
    {
        List<Path>   resources = SourceTree.viewServiceResources();
        List<String> clashes   = new ArrayList<>();

        assertTrue(resources.size() > 10,
                   "Expected to find the view service resource classes - found " + resources.size() +
                           ".  Has the source layout moved?");

        for (Path resource : resources)
        {
            Map<String, Integer> counts  = new LinkedHashMap<>();
            Matcher              matcher = MAPPING.matcher(SourceTree.read(resource));

            while (matcher.find())
            {
                String mapping = matcher.group(1).toUpperCase() + " " + matcher.group(2);

                counts.merge(mapping, 1, Integer::sum);
            }

            counts.forEach((mapping, count) ->
                           {
                               if (count > 1)
                               {
                                   clashes.add(resource.getFileName() + " maps " + mapping + " " + count + " times");
                               }
                           });
        }

        assertTrue(clashes.isEmpty(),
                   "Spring will not start with an ambiguous request mapping.  Duplicates found:\n    " +
                           String.join("\n    ", clashes));
    }
}
