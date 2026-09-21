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

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * BeanEqualityChainTest checks that a bean deferring part of its equality to its parent has a parent that
 * implements it.
 * <br><br>
 * This is the other half of {@link BeanEqualityContractTest}.  That one catches a bean calling
 * {@code super.equals} with no superclass at all, where {@code super} is plainly {@link Object}.  This one
 * catches the case that looks correct: the bean <i>does</i> have a superclass, and calls
 * {@code super.equals} on it quite legitimately - but no class anywhere up the chain implements
 * {@code equals}, so the call resolves to {@link Object#equals} all the same.  The effect is identical and
 * rather worse hidden:
 * <ul>
 *     <li>{@code equals} becomes identity, so the bean never equals a separate bean with the same content -
 *     not a copy of itself, not one that came back over JSON.</li>
 *     <li>{@code hashCode} becomes the identity hash, so equal beans hash differently.</li>
 *     <li>Every field declared by the classes in between takes part in no comparison at all, which is
 *     usually the first thing anybody notices and the last thing they suspect.</li>
 * </ul>
 * It found exactly this in the automated curation view service: {@code CatalogTemplate} and
 * {@code ResourceDescription} both deferred to {@code RefDataElementBase}, which declared three fields and
 * implemented neither method.
 * <br><br>
 * The fix is to implement {@code equals} and {@code hashCode} on the class that is missing them - which is
 * usually the base, and fixes every subclass at once - not to delete the {@code super} call from the
 * subclasses.
 * <br><br>
 * A chain that leaves the implementation is not judged.  Where an ancestor is not among the files scanned
 * here - a class from the JDK or from a dependency - whether it implements {@code equals} cannot be read
 * from this repository's source, and a guess either way would be worse than silence.
 */
class BeanEqualityChainTest
{
    /**
     * What a scanned class does about equality: the class it extends, whether it implements each method,
     * and whether it defers to its parent.
     *
     * @param superClassName the class it extends, or null
     * @param implementsEquals declares {@code equals}
     * @param implementsHashCode declares {@code hashCode}
     * @param defersEquals its {@code equals} calls {@code super.equals}
     * @param defersHashCode its {@code hashCode} calls {@code super.hashCode}
     * @param fileName where it came from, for the error message
     */
    private record Equality(String  superClassName,
                            boolean implementsEquals,
                            boolean implementsHashCode,
                            boolean defersEquals,
                            boolean defersHashCode,
                            String  fileName) { }


    @Test
    @DisplayName("A bean deferring equality to its parent has a parent that implements it")
    void deferredEqualityReachesAnImplementation()
    {
        List<Path>            classFiles = SourceTree.implementationClasses();
        Map<String, Equality> classes    = new LinkedHashMap<>();
        List<String>          offenders  = new ArrayList<>();

        assertTrue(classFiles.size() > 1000,
                   "Expected to find the implementation's source files - found " + classFiles.size() +
                           ".  Has the source layout moved?");

        for (JavaClasses.JavaClass javaClass : JavaClasses.topLevelClasses(classFiles))
        {
            boolean implementsEquals   = false;
            boolean implementsHashCode = false;
            boolean defersEquals       = false;
            boolean defersHashCode     = false;

            for (JavaMethods.Method method : JavaMethods.publicMethods(javaClass.source()))
            {
                if ("equals".equals(method.name()))
                {
                    implementsEquals = true;
                    defersEquals     = method.body().contains("super.equals(");
                }

                if ("hashCode".equals(method.name()))
                {
                    implementsHashCode = true;
                    defersHashCode     = method.body().contains("super.hashCode(");
                }
            }

            classes.put(javaClass.name(),
                        new Equality(javaClass.superClassName(),
                                     implementsEquals,
                                     implementsHashCode,
                                     defersEquals,
                                     defersHashCode,
                                     javaClass.file().getFileName().toString()));
        }

        for (Map.Entry<String, Equality> entry : classes.entrySet())
        {
            Equality equality = entry.getValue();

            /*
             * A class with no superclass at all is BeanEqualityContractTest's business, not this check's.
             */
            if ((equality.superClassName() == null) || (! chainIsWithinTheImplementation(equality, classes)))
            {
                continue;
            }

            if (equality.defersEquals() && (ancestorImplementing(equality, classes, true) == null))
            {
                offenders.add(entry.getKey() + ".equals() calls super.equals(), but no class above it" +
                                      " implements equals() - so the call reaches Object and compares by" +
                                      " identity. Implement equals() on " + topOfChain(equality, classes) +
                                      " (" + equality.fileName() + ")");
            }

            if (equality.defersHashCode() && (ancestorImplementing(equality, classes, false) == null))
            {
                offenders.add(entry.getKey() + ".hashCode() calls super.hashCode(), but no class above it" +
                                      " implements hashCode() - so the call reaches Object and returns the" +
                                      " identity hash. Implement hashCode() on " + topOfChain(equality, classes) +
                                      " (" + equality.fileName() + ")");
            }
        }

        assertTrue(offenders.isEmpty(),
                   "These beans defer part of their equality to a parent that does not implement it, so the" +
                           " deferral silently becomes an identity comparison:\n    " +
                           String.join("\n    ", offenders));
    }


    /**
     * Can the whole superclass chain be read from the files scanned here?  If any ancestor is outside them
     * there is no way to tell whether it implements equality, so the class is not judged.
     *
     * @param equality the class's equality shape
     * @param classes every scanned class
     * @return true when every ancestor was scanned
     */
    private boolean chainIsWithinTheImplementation(Equality equality, Map<String, Equality> classes)
    {
        String ancestor = equality.superClassName();

        while (ancestor != null)
        {
            Equality ancestorEquality = classes.get(ancestor);

            if (ancestorEquality == null)
            {
                return false;
            }

            ancestor = ancestorEquality.superClassName();
        }

        return true;
    }


    /**
     * Return the nearest ancestor that implements the method in question, or null when none does.
     *
     * @param equality the class's equality shape
     * @param classes every scanned class
     * @param forEquals true for equals, false for hashCode
     * @return ancestor name, or null
     */
    private String ancestorImplementing(Equality equality, Map<String, Equality> classes, boolean forEquals)
    {
        String ancestor = equality.superClassName();

        while (ancestor != null)
        {
            Equality ancestorEquality = classes.get(ancestor);

            if (ancestorEquality == null)
            {
                return null;
            }

            if (forEquals ? ancestorEquality.implementsEquals() : ancestorEquality.implementsHashCode())
            {
                return ancestor;
            }

            ancestor = ancestorEquality.superClassName();
        }

        return null;
    }


    /**
     * Return the topmost class in the chain - the one the missing implementation almost always belongs on,
     * because putting it there fixes every subclass at once.
     *
     * @param equality the class's equality shape
     * @param classes every scanned class
     * @return the name of the root of the hierarchy
     */
    private String topOfChain(Equality equality, Map<String, Equality> classes)
    {
        String ancestor = equality.superClassName();

        while (true)
        {
            Equality ancestorEquality = classes.get(ancestor);

            if ((ancestorEquality == null) || (ancestorEquality.superClassName() == null))
            {
                return ancestor;
            }

            ancestor = ancestorEquality.superClassName();
        }
    }
}
