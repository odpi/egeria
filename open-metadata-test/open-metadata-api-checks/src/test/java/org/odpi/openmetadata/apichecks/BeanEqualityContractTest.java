/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.apichecks;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * BeanEqualityContractTest checks that no class calls {@code super.equals} or {@code super.hashCode} when
 * it has no superclass to call.
 * <br><br>
 * Egeria's beans are written as a hierarchy, and the house style for a bean part way up one is to compare
 * its own fields and then defer the rest to its parent:
 * <pre>
 *     if (! super.equals(objectToCompare))
 *     {
 *         return false;
 *     }
 * </pre>
 * That is correct where there is a parent.  Where there is not - a bean at the root of its own hierarchy,
 * or one that only implements an interface - {@code super} is {@link Object}, and the two calls mean
 * something quite different from what they look like:
 * <ul>
 *     <li>{@code Object.equals} is identity. The {@code this == objectToCompare} test at the top of the
 *     method has already dealt with that case, so the guard can only ever return false, and the bean
 *     never compares equal to a separate bean holding the same content.</li>
 *     <li>{@code Object.hashCode} is the identity hash. Mixing it into {@code Objects.hash(...)} gives
 *     every instance a different hash code, so a bean whose {@code equals} is value based breaks the
 *     equals/hashCode contract: two equal beans hash differently and both survive in a {@link java.util.HashSet}
 *     or as separate keys in a {@link java.util.HashMap}.</li>
 * </ul>
 * Neither fails at compile time - {@code super.equals} and {@code super.hashCode} resolve perfectly well
 * against {@code Object} - and neither shows up as an error at runtime. What shows up instead is
 * duplicates in a collection, a de-duplication step that finds nothing, or a comparison that says two
 * identical beans differ. This check makes it fail the build.
 * <br><br>
 * The fix is to drop the call: remove the {@code super.equals} guard, and remove {@code super.hashCode()}
 * from the {@code Objects.hash(...)} argument list. It is not to add a superclass.
 * <br><br>
 * The check reads the whole implementation rather than a chosen set of bean packages, because the pattern
 * appears wherever beans are written - the frameworks, the REST request and response bodies, the view
 * service request bodies and the server status beans. It looks at the top level class only: a nested class
 * has its own declaration and its own superclass, and this check would have to parse the file to tell the
 * two apart.
 */
class BeanEqualityContractTest
{
    @Test
    @DisplayName("No class calls super.equals or super.hashCode when it has no superclass")
    void superCallsHaveASuperclassToCall()
    {
        List<Path>   classFiles = SourceTree.implementationClasses();
        List<String> offenders  = new ArrayList<>();

        assertTrue(classFiles.size() > 1000,
                   "Expected to find the implementation's source files - found " + classFiles.size() +
                           ".  Has the source layout moved?");

        for (JavaClasses.JavaClass javaClass : JavaClasses.topLevelClasses(classFiles))
        {
            if (javaClass.hasSuperClass())
            {
                continue;
            }

            for (JavaMethods.Method method : JavaMethods.publicMethods(javaClass.source()))
            {
                if ("equals".equals(method.name()) && method.body().contains("super.equals("))
                {
                    offenders.add(javaClass.name() + ".equals() calls super.equals(), which is Object's" +
                                          " identity comparison - the bean can never equal a separate bean" +
                                          " with the same content (" + javaClass.file().getFileName() + ")");
                }

                if ("hashCode".equals(method.name()) && method.body().contains("super.hashCode("))
                {
                    offenders.add(javaClass.name() + ".hashCode() calls super.hashCode(), which is Object's" +
                                          " identity hash - equal beans get different hash codes (" +
                                          javaClass.file().getFileName() + ")");
                }
            }
        }

        assertTrue(offenders.isEmpty(),
                   "These classes have no superclass, so super.equals()/super.hashCode() reach Object and" +
                           " quietly break value equality.  Remove the call rather than adding a" +
                           " superclass:\n    " + String.join("\n    ", offenders));
    }
}
