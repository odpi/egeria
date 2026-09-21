/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.apichecks;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * JavaClasses picks the top level class out of a source file - its name, whether it is abstract, and the
 * class it extends.  Like {@link JavaMethods} this is a simple match on the declaration rather than a
 * parse; the checks here only need the declaration, and a file's top level class is the one named after
 * the file.
 * <br><br>
 * Matching on the file name is what keeps nested and inner classes out of the way.  A declaration such as
 * {@code class Foo<T> extends Bar<T>} is matched up to its opening brace, so the type arguments do not
 * hide the superclass.
 */
class JavaClasses
{
    /**
     * One top level class.
     *
     * @param name class name, which is also the file name
     * @param superClassName the class it extends, or null when it extends nothing but Object.  A class that
     *                       only implements interfaces has no superclass and so reports null here.
     * @param isAbstract whether the class is abstract
     * @param source the whole file
     * @param file where it came from, for error messages
     */
    record JavaClass(String name, String superClassName, boolean isAbstract, String source, Path file)
    {
        /**
         * Does this class extend something other than Object?
         *
         * @return true when it declares a superclass
         */
        boolean hasSuperClass()
        {
            return superClassName != null;
        }
    }


    private static final Pattern EXTENDS = Pattern.compile("\\bextends\\s+([A-Za-z0-9_]+)");


    /**
     * Return the top level class declared in the supplied file, or null when its top level type is not a
     * class - an enum, an interface, a record or an annotation.
     *
     * @param file file to read
     * @return the class, or null
     */
    static JavaClass topLevelClass(Path file)
    {
        String fileName = file.getFileName().toString();

        if (! fileName.endsWith(".java"))
        {
            return null;
        }

        String  className   = fileName.substring(0, fileName.length() - ".java".length());
        String  source      = SourceTree.read(file);
        Matcher declaration = Pattern.compile("(?:public\\s+)?(abstract\\s+|final\\s+)*class\\s+" +
                                                      Pattern.quote(className) + "\\b([^{]*)\\{")
                                     .matcher(source);

        if (! declaration.find())
        {
            return null;
        }

        String  modifiers      = (declaration.group(1) == null) ? "" : declaration.group(1);
        Matcher superClass     = EXTENDS.matcher(declaration.group(2));
        String  superClassName = superClass.find() ? superClass.group(1) : null;

        return new JavaClass(className, superClassName, modifiers.contains("abstract"), source, file);
    }


    /**
     * Return the top level class of every supplied file, skipping the files whose top level type is not a
     * class.
     *
     * @param files files to read
     * @return the classes among them
     */
    static List<JavaClass> topLevelClasses(List<Path> files)
    {
        List<JavaClass> classes = new ArrayList<>();

        for (Path file : files)
        {
            JavaClass javaClass = topLevelClass(file);

            if (javaClass != null)
            {
                classes.add(javaClass);
            }
        }

        return classes;
    }
}
