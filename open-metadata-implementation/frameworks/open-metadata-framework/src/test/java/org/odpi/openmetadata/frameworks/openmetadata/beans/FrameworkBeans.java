/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.beans;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * FrameworkBeans finds the framework's beans so that a test can cover all of them instead of the ones
 * somebody remembered to list.
 * <br><br>
 * The beans are found from the source tree rather than from a hand-kept list, which is the point: a bean
 * added tomorrow is covered tomorrow, with nothing to remember.  Source is walked rather than the classpath
 * scanned because the framework has no classpath scanner on it and does not need one for this.
 */
class FrameworkBeans
{
    private static final String SOURCE_ROOT = "open-metadata-implementation/frameworks/open-metadata-framework/src/main/java";
    private static final String BEAN_ROOT   = "org/odpi/openmetadata/frameworks/openmetadata";

    /**
     * The packages whose classes are beans that travel between the platform and its callers.
     */
    private static final List<String> BEAN_PACKAGES = List.of("properties", "metadataelements", "search");

    /**
     * Classes in those packages that are not beans, each with the reason.  Anything else found there is
     * covered, so a genuine future exception has an obvious home rather than being hidden in a filter.
     */
    private static final Map<String, String> NOT_BEANS = new LinkedHashMap<>()
    {{
        put("PropertyHelper", "a helper that lives in the search package, not a bean - it has its own tests");
    }};


    /**
     * Return the root of the repository.  A test's working directory is its own module, so walk up until
     * the file that marks the root of the build is found.
     *
     * @return repository root
     */
    private static Path repositoryRoot()
    {
        Path candidate = Paths.get("").toAbsolutePath();

        while (candidate != null)
        {
            if (Files.exists(candidate.resolve("settings.gradle")))
            {
                return candidate;
            }

            candidate = candidate.getParent();
        }

        throw new IllegalStateException("Unable to locate the repository root - no settings.gradle found above " +
                                                Paths.get("").toAbsolutePath());
    }


    /**
     * Return every concrete bean class in the framework's bean packages.  Enums, interfaces, records and
     * abstract classes are left out - none of them is a bean with a lifecycle to test - along with the
     * classes named in {@link #NOT_BEANS}.
     *
     * @return bean classes
     */
    static List<Class<?>> beanClasses()
    {
        Path           sourceRoot = repositoryRoot().resolve(SOURCE_ROOT);
        List<Class<?>> beans      = new ArrayList<>();

        for (String beanPackage : BEAN_PACKAGES)
        {
            Path directory = sourceRoot.resolve(BEAN_ROOT).resolve(beanPackage);

            if (! Files.isDirectory(directory))
            {
                throw new IllegalStateException("Bean package " + beanPackage + " not found at " + directory +
                                                        " - has the source layout moved?");
            }

            try (Stream<Path> walk = Files.walk(directory))
            {
                walk.filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().endsWith(".java"))
                    .filter(path -> ! path.getFileName().toString().equals("package-info.java"))
                    .sorted()
                    .forEach(path -> addBean(beans, sourceRoot, path));
            }
            catch (IOException error)
            {
                throw new UncheckedIOException(error);
            }
        }

        return beans;
    }


    /**
     * Add the class the supplied source file declares, if it is a bean.
     *
     * @param beans collected beans
     * @param sourceRoot the source root the file's package is relative to
     * @param sourceFile the java file
     */
    private static void addBean(List<Class<?>> beans, Path sourceRoot, Path sourceFile)
    {
        String relativePath = sourceRoot.relativize(sourceFile).toString();
        String className    = relativePath.substring(0, relativePath.length() - ".java".length())
                                          .replace('/', '.');
        String simpleName   = className.substring(className.lastIndexOf('.') + 1);

        if (NOT_BEANS.containsKey(simpleName))
        {
            return;
        }

        Class<?> candidate;

        try
        {
            candidate = Class.forName(className);
        }
        catch (ClassNotFoundException error)
        {
            throw new IllegalStateException("Source file " + sourceFile + " has no compiled class " + className, error);
        }

        if (candidate.isEnum() || candidate.isInterface() || candidate.isRecord() || candidate.isAnnotation())
        {
            return;
        }

        if (Modifier.isAbstract(candidate.getModifiers()))
        {
            return;
        }

        if (! hasPublicNoArgConstructor(candidate))
        {
            return;
        }

        beans.add(candidate);
    }


    /**
     * Does this class have the public no-argument constructor every bean needs, for Jackson as much as for
     * this test?
     *
     * @param beanClass class to check
     * @return true when it has one
     */
    private static boolean hasPublicNoArgConstructor(Class<?> beanClass)
    {
        try
        {
            return Modifier.isPublic(beanClass.getDeclaredConstructor().getModifiers());
        }
        catch (NoSuchMethodException error)
        {
            return false;
        }
    }


    /**
     * Return the bean's copy constructor, or null when it has none.
     * <br><br>
     * A bean may have more than one single argument constructor, and only one of them is the copy
     * constructor.  The others take a <b>supertype</b> and build this bean out of a plainer one -
     * {@code ActorProperties(ReferenceableProperties)} is one - and they are deliberately partial: they
     * cannot carry the fields the supertype does not have, and they reset the type name to this bean's own
     * type.  Asserting that one of those copies the whole bean would be asserting the opposite of what it
     * is for.
     * <br><br>
     * So only the constructor taking the bean's own class counts, and a bean whose sole single argument
     * constructor is a widened one is reported as having no copy constructor rather than being held to a
     * contract that constructor does not claim to meet.  Choosing by {@link Class#getConstructors()} order
     * instead would be worse than wrong: that order is unspecified, so the test would pass or fail
     * depending on how the class happened to be loaded.
     *
     * @param beanClass class to look at
     * @return copy constructor, or null
     */
    static Constructor<?> copyConstructor(Class<?> beanClass)
    {
        for (Constructor<?> constructor : beanClass.getConstructors())
        {
            if ((constructor.getParameterCount() == 1) && (constructor.getParameterTypes()[0] == beanClass))
            {
                return constructor;
            }
        }

        return null;
    }
}
