/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.apichecks;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * JavaMethods splits a source file into its top level public methods so that a check can look at one
 * method at a time.  This is a deliberately simple split on the declaration line rather than a parse -
 * the checks here only need to know which lines belong to which method.
 */
class JavaMethods
{
    private static final Pattern PUBLIC_METHOD = Pattern.compile("\\n {4}public\\s+[A-Za-z0-9_<>,.\\[\\] ]+?\\s+(\\w+)\\s*\\(");


    /**
     * One public method: its name and the source that makes it up.
     *
     * @param name method name
     * @param body source of the method, from its declaration to the start of the next one
     */
    record Method(String name, String body) { }


    /**
     * Split the supplied source into its public methods.
     *
     * @param source contents of a java file
     * @return the public methods it declares
     */
    static List<Method> publicMethods(String source)
    {
        List<Method>  methods = new ArrayList<>();
        List<Integer> starts  = new ArrayList<>();
        List<String>  names   = new ArrayList<>();

        Matcher matcher = PUBLIC_METHOD.matcher(source);

        while (matcher.find())
        {
            starts.add(matcher.start());
            names.add(matcher.group(1));
        }

        for (int i = 0; i < starts.size(); i++)
        {
            int end = (i + 1 < starts.size()) ? starts.get(i + 1) : source.length();

            methods.add(new Method(names.get(i), source.substring(starts.get(i), end)));
        }

        return methods;
    }


    /**
     * Return the declaration of a method - everything up to the opening brace of its body.  Used to look
     * at a method's parameters without matching text in the body.
     *
     * @param method method to inspect
     * @return the declaration
     */
    static String declaration(Method method)
    {
        int brace = method.body().indexOf('{');

        return (brace < 0) ? method.body() : method.body().substring(0, brace);
    }
}
