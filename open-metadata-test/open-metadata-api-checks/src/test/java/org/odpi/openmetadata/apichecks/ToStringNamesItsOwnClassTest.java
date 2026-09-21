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
 * ToStringNamesItsOwnClassTest checks that a {@code toString} method names the class it belongs to, and
 * that each of its labels names the field it prints.
 * <br><br>
 * A {@code toString} is written by copying a sibling's and editing it, which is why both halves of it go
 * wrong the same way.  Neither is caught by anything: the method compiles, returns a string, and says
 * something false.
 * <ul>
 *     <li><b>The wrong class.</b>  A {@code ClassificationProperties} announced itself as a
 *     {@code ClassificationBeanProperties}, an {@code EntityProperties} as a
 *     {@code RelationshipProperties}, a {@code DataManagerProperties} as an {@code EngineProperties}.
 *     That does not merely fail to help - it sends whoever is reading the log to the wrong class.  This was
 *     true of 230 types across the implementation.</li>
 *     <li><b>The wrong label.</b>  {@code ", governanceEngineGUID='" + executorEngineGUID} prints one field
 *     under another's name, usually because the field was renamed and the label was not.  140 of those.</li>
 * </ul>
 * The rule this enforces is the simple one: the literal at the front names this class, and a label names
 * the field it prints.
 * <br><br>
 * <b>What it does not judge.</b>  Only a label whose printed expression is a field of the same class is
 * checked, so a label over a getter call, a local, or an expression is left alone.  And where a label
 * disagrees with its field <i>deliberately</i> - {@code StarRating} swaps two of them and says so in a
 * comment - the comment is the signal to leave it be, so a line carrying a trailing comment is skipped.
 * That is a narrow escape hatch on purpose: a deliberate mismatch is rare enough to be worth a sentence
 * explaining itself.
 */
class ToStringNamesItsOwnClassTest
{
    /**
     * The opening literal of a {@code toString} built the usual way.
     */
    private static final Pattern OPENING_LITERAL = Pattern.compile("return\\s+\"([A-Za-z0-9_]+)\\{");

    /**
     * {@code "label='" + field} or {@code ", label=" + field}, capturing the label and the expression.
     */
    private static final Pattern LABELLED_VALUE = Pattern.compile("\"(?:,\\s*)?([a-z][A-Za-z0-9_]*)='?\"\\s*\\+\\s*([A-Za-z_][A-Za-z0-9_]*)\\b");

    /**
     * A field declaration, capturing its name.  Protected fields count: a subclass's {@code toString} prints
     * them as readily as its own.
     */
    private static final Pattern FIELD = Pattern.compile("\\n\\s+p(?:rivate|rotected)\\s+(?!static)(?:final\\s+)?[A-Za-z0-9_<>,\\[\\]\\.\\? ]+?\\s+([a-z][A-Za-z0-9_]*)\\s*(?:=|;)");


    @Test
    @DisplayName("A toString names its own class and labels its fields correctly")
    void toStringDescribesWhatItActuallyIs()
    {
        List<Path>   classFiles = SourceTree.implementationClasses();
        List<String> offenders  = new ArrayList<>();

        assertTrue(classFiles.size() > 1000,
                   "Expected to find the implementation's source files - found " + classFiles.size() +
                           ".  Has the source layout moved?");

        for (JavaClasses.JavaClass javaClass : JavaClasses.topLevelClasses(classFiles))
        {
            checkOneClass(javaClass, offenders);
        }

        assertTrue(offenders.isEmpty(),
                   "These toString methods describe something other than what they are, so a log line or a" +
                           " diagnostic built from one is misleading:\n    " + String.join("\n    ", offenders));
    }


    /**
     * Check one class's {@code toString}, if it has one.
     *
     * @param javaClass class to check
     * @param offenders collected findings
     */
    private void checkOneClass(JavaClasses.JavaClass javaClass, List<String> offenders)
    {
        String toString = toStringBody(javaClass.source());

        if (toString == null)
        {
            return;
        }

        Matcher opening = OPENING_LITERAL.matcher(toString);

        if (opening.find() && (! javaClass.name().equals(opening.group(1))))
        {
            offenders.add(javaClass.name() + ".toString() announces itself as " + opening.group(1) +
                                  " (" + javaClass.file().getFileName() + ")");
        }

        Map<String, String> fields  = fieldNames(javaClass.source());
        Matcher             labels  = LABELLED_VALUE.matcher(toString);

        while (labels.find())
        {
            String label = labels.group(1);
            String value = labels.group(2);

            if (label.equals(value) || (! fields.containsKey(value)))
            {
                continue;
            }

            if (lineCarriesAComment(toString, labels.start()))
            {
                continue;
            }

            offenders.add(javaClass.name() + ".toString() prints " + value + " under the label '" + label +
                                  "' (" + javaClass.file().getFileName() + ")");
        }
    }


    /**
     * Return the body of the class's {@code toString}, or null when it has none.
     * <br><br>
     * {@link JavaMethods} is not used here because it slices a method from its declaration to the start of
     * the next public one, which would carry the following method's javadoc - and a javadoc mentioning a
     * field name would be read as a label.
     *
     * @param source contents of a java file
     * @return the method body, or null
     */
    private String toStringBody(String source)
    {
        Matcher declaration = Pattern.compile("public\\s+String\\s+toString\\s*\\(").matcher(source);

        if (! declaration.find())
        {
            return null;
        }

        int open  = source.indexOf('{', declaration.end());
        int depth = 0;

        if (open < 0)
        {
            return null;
        }

        for (int index = open; index < source.length(); index++)
        {
            if (source.charAt(index) == '{')
            {
                depth++;
            }
            else if (source.charAt(index) == '}')
            {
                depth--;

                if (depth == 0)
                {
                    return source.substring(open, index + 1);
                }
            }
        }

        return null;
    }


    /**
     * Return the instance fields the supplied source declares.
     *
     * @param source contents of a java file
     * @return field names, mapped to themselves so that lookup reads naturally
     */
    private Map<String, String> fieldNames(String source)
    {
        Map<String, String> fields  = new LinkedHashMap<>();
        Matcher             matcher = FIELD.matcher(source);

        while (matcher.find())
        {
            fields.put(matcher.group(1), matcher.group(1));
        }

        return fields;
    }


    /**
     * Does the line holding the supplied offset end with a comment?  A deliberate mismatch explains itself
     * in one, and this check takes that as its answer.
     *
     * @param body the method body
     * @param offset where the label was found
     * @return true when the line carries a trailing comment
     */
    private boolean lineCarriesAComment(String body, int offset)
    {
        int lineStart = body.lastIndexOf('\n', offset) + 1;
        int lineEnd   = body.indexOf('\n', offset);

        if (lineEnd < 0)
        {
            lineEnd = body.length();
        }

        return body.substring(lineStart, lineEnd).contains("//");
    }
}
