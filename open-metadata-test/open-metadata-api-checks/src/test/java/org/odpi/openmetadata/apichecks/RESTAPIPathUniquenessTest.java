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
 * Spring refuses to create its request mapping when two methods claim the same verb and path.  That is
 * invisible at compile time: the duplicate mappings are just two annotations with the same value.
 * <br><br>
 * Path variable names do not take part in matching, so /assets/&#123;assetGUID&#125;/connections/&#123;connectionGUID&#125;
 * and /assets/&#123;assetGUID&#125;/connections/&#123;endpointGUID&#125; are one and the same pattern to Spring.  A clash of
 * that shape is worse than an exact duplicate, because the context still starts and every call to the
 * path fails at runtime with an ambiguous-handler error instead.  The check therefore replaces each
 * &#123;variable&#125; with a placeholder before comparing.
 * <br><br>
 * The same path under different verbs is legal and is used deliberately - for example the multi-language
 * service has both a POST and a GET on /elements/&#123;elementGUID&#125;/translations - so the check keys on
 * the verb as well as the path.
 */
class RESTAPIPathUniquenessTest
{
    private static final Pattern MAPPING  = Pattern.compile("@(Post|Get|Put|Delete)Mapping\\(\\s*(?:path\\s*=\\s*)?\"([^\"]*)\"");
    private static final Pattern VARIABLE = Pattern.compile("\\{[^}]*}");


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
                String path    = VARIABLE.matcher(matcher.group(2)).replaceAll("{}");
                String mapping = matcher.group(1).toUpperCase() + " " + path;

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
