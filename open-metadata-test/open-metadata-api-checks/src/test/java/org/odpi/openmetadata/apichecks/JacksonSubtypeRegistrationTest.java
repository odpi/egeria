/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.apichecks;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * JacksonSubtypeRegistrationTest checks that every open metadata framework bean can be deserialized as the
 * base type it is sent as.
 * <br><br>
 * These beans travel polymorphically.  A field declared as {@code OpenMetadataRootProperties} holds any of
 * the properties beans beneath it, and one declared as {@code RelationshipBeanProperties} holds any of the
 * relationship beans.  Jackson handles that with a type id written into the JSON, which it turns back into a
 * class using the {@code @JsonSubTypes} list on the base - so a bean that nobody added to that list
 * serializes perfectly and then fails to come back:
 * <pre>
 *     InvalidTypeIdException: Could not resolve type id 'AttachedClassification' as a subtype of
 *     ElementClassificationHeader: known type ids = [ElementClassification, ElementClassificationHeader]
 * </pre>
 * Nothing catches this when the bean is written.  The class compiles, the annotation on its parent is
 * valid without it, and the write half of the round trip works, so a test that only serializes passes.  The
 * first sign of trouble is a caller receiving that exception - or, worse, nothing at all until some later
 * release declares a field as the base type and every payload carrying the bean starts failing.  This check
 * makes the omission fail the build instead.
 * <br><br>
 * A bean is satisfied by a registration anywhere in the subtype graph below its polymorphic base, not only
 * on its immediate parent.  Jackson collects subtypes transitively - it reads the {@code @JsonSubTypes} of
 * each subtype it finds, and so on - so a bean registered on a class that is not its own parent still
 * resolves.  Several are: {@code GovernanceStrategyProperties} extends {@code GovernanceDefinitionProperties}
 * but is registered on {@code GovernanceDriverProperties}, a sibling.  That is untidy rather than broken, and
 * a check that insisted on the immediate parent would report beans that work.
 * <br><br>
 * Abstract classes are skipped: they are never instantiated, so no type id is ever written for one.
 * <br><br>
 * <b>Scope.</b>  The check covers the framework's bean packages - {@code properties}, {@code metadataelements}
 * and {@code search} - and deliberately stops there.  The REST request and response hierarchies also declare
 * {@code @JsonTypeInfo} on their bases, but each endpoint names its concrete response type, so those beans are
 * never resolved through a base type's subtype list and around a hundred of them are unregistered and correct
 * as they are.  Including them would mean an exclusion list longer than the findings, which is not a check.
 * If a response hierarchy ever does start travelling as its base type, add its package here.
 */
class JacksonSubtypeRegistrationTest
{
    private static final Pattern SUBTYPE = Pattern.compile("@JsonSubTypes\\.Type\\(\\s*value\\s*=\\s*([A-Za-z0-9_]+)\\.class");


    @Test
    @DisplayName("Every framework bean is registered under the base type it is sent as")
    void everyBeanIsReachableFromItsPolymorphicBase()
    {
        List<Path>                 beanFiles = SourceTree.frameworkBeans();
        List<JavaClasses.JavaClass> beans     = JavaClasses.topLevelClasses(beanFiles);

        assertTrue(beans.size() > 500,
                   "Expected to find the framework's bean packages - found " + beans.size() +
                           " classes.  Has the source layout moved?");

        Map<String, JavaClasses.JavaClass> beansByName        = new LinkedHashMap<>();
        Map<String, Set<String>>           registeredSubtypes = new HashMap<>();
        Set<String>                        polymorphicBases   = new HashSet<>();

        for (JavaClasses.JavaClass bean : beans)
        {
            beansByName.put(bean.name(), bean);

            if (bean.source().contains("@JsonTypeInfo"))
            {
                polymorphicBases.add(bean.name());
            }

            Matcher     matcher  = SUBTYPE.matcher(bean.source());
            Set<String> subtypes = new LinkedHashSet<>();

            while (matcher.find())
            {
                subtypes.add(matcher.group(1));
            }

            registeredSubtypes.put(bean.name(), subtypes);
        }

        Map<String, Set<String>> resolvableFrom = new HashMap<>();
        List<String>             unregistered   = new ArrayList<>();

        for (JavaClasses.JavaClass bean : beans)
        {
            if (bean.isAbstract() || ! bean.hasSuperClass())
            {
                continue;
            }

            String base = polymorphicBaseOf(bean, beansByName, polymorphicBases);

            if (base == null)
            {
                continue;
            }

            Set<String> resolvable = resolvableFrom.computeIfAbsent(base,
                                                                    baseName -> subtypesResolvableFrom(baseName, registeredSubtypes));

            if (! resolvable.contains(bean.name()))
            {
                unregistered.add(bean.name() + " is sent as " + base + " but is not in its @JsonSubTypes graph" +
                                         " - add @JsonSubTypes.Type(value = " + bean.name() + ".class, name = \"" +
                                         bean.name() + "\") to " + bean.superClassName());
            }
        }

        assertTrue(unregistered.isEmpty(),
                   "These beans serialize but cannot be deserialized as the base type they are sent as:\n    " +
                           String.join("\n    ", unregistered));
    }


    /**
     * Walk up from a bean to the nearest ancestor that declares {@code @JsonTypeInfo} - the base whose
     * subtype list Jackson consults for this bean.  Returns null when there is no such ancestor in scope,
     * which means the bean is not sent polymorphically and needs no registration.
     *
     * @param bean bean to start from
     * @param beansByName every bean in scope
     * @param polymorphicBases the beans that declare @JsonTypeInfo
     * @return name of the polymorphic base, or null
     */
    private String polymorphicBaseOf(JavaClasses.JavaClass              bean,
                                     Map<String, JavaClasses.JavaClass> beansByName,
                                     Set<String>                        polymorphicBases)
    {
        String ancestor = bean.superClassName();

        while (ancestor != null && beansByName.containsKey(ancestor))
        {
            if (polymorphicBases.contains(ancestor))
            {
                return ancestor;
            }

            ancestor = beansByName.get(ancestor).superClassName();
        }

        return null;
    }


    /**
     * Return every subtype Jackson can resolve from the supplied base, following the {@code @JsonSubTypes}
     * lists transitively the way Jackson does.
     *
     * @param base base type name
     * @param registeredSubtypes what each bean registers directly
     * @return the resolvable subtype names
     */
    private Set<String> subtypesResolvableFrom(String                   base,
                                               Map<String, Set<String>> registeredSubtypes)
    {
        Set<String>   resolvable = new LinkedHashSet<>();
        Deque<String> pending    = new ArrayDeque<>();

        pending.push(base);

        while (! pending.isEmpty())
        {
            for (String subtype : registeredSubtypes.getOrDefault(pending.pop(), Set.of()))
            {
                if (resolvable.add(subtype))
                {
                    pending.push(subtype);
                }
            }
        }

        return resolvable;
    }
}
