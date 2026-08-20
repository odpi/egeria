/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.apichecks;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * RESTAPINullRequestBodyTest checks that every view service REST method which takes a requestBody tests
 * it for null before using it.
 * <br><br>
 * The request body of these operations is optional - the Spring resource declares
 * &#64;RequestBody(required = false) - so a caller can and does send nothing.  A method that goes straight
 * to requestBody.getProperties() answers that caller with a NullPointerException and a stack trace
 * instead of a message telling them what was wrong.
 * <br><br>
 * The obvious alternative, letting Spring reject a missing body by marking it required, is not used
 * because the message Spring produces is not helpful enough to hand to an end user.  The services do this
 * themselves, so this check makes sure they actually do.
 * <br><br>
 * What counts as handling it is deliberately loose: any test of requestBody against null in the method.
 * Some methods return an error, some treat a missing body as "no properties supplied" and carry on.  Both
 * are fine.  Only ignoring the possibility is not.
 */
class RESTAPINullRequestBodyTest
{
    private static final Pattern DEREFERENCE = Pattern.compile("\\brequestBody\\s*\\.");
    private static final Pattern NULL_TEST   = Pattern.compile("requestBody\\s*(!=|==)\\s*null");


    @Test
    @DisplayName("Every REST method that uses its requestBody first tests it for null")
    void requestBodiesAreNullChecked()
    {
        List<Path>   restServices = SourceTree.viewServiceRESTServices();
        List<String> unguarded    = new ArrayList<>();
        int          checked      = 0;

        assertTrue(restServices.size() > 10,
                   "Expected to find the view service REST services classes - found " + restServices.size() +
                           ".  Has the source layout moved?");

        for (Path restService : restServices)
        {
            for (JavaMethods.Method method : JavaMethods.publicMethods(SourceTree.read(restService)))
            {
                if (! JavaMethods.declaration(method).contains("requestBody"))
                {
                    continue;
                }

                checked++;

                if (DEREFERENCE.matcher(method.body()).find() && ! NULL_TEST.matcher(method.body()).find())
                {
                    unguarded.add(restService.getFileName() + "." + method.name() + "()");
                }
            }
        }

        assertTrue(checked > 500,
                   "Expected to find the REST methods that take a request body - found " + checked +
                           ".  Has the method declaration style changed?");

        assertTrue(unguarded.isEmpty(),
                   "These REST methods use their requestBody without testing it for null, so a caller that " +
                           "sends no body gets a NullPointerException:\n    " + String.join("\n    ", unguarded));
    }
}
