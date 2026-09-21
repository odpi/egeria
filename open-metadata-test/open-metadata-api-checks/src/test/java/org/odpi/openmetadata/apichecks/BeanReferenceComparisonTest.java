/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.apichecks;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * BeanReferenceComparisonTest checks that no {@code equals} method compares a reference field with
 * {@code ==}.
 * <br><br>
 * Inside an {@code equals} method, {@code field == that.field} reads exactly like a comparison of two
 * values, and for a primitive or an enum constant it is one.  For anything else it asks whether the two
 * beans happen to hold the <i>same object</i>, which two separately built beans never do.  A bean whose
 * {@code equals} does this can therefore never equal a separate bean with the same content - not a copy of
 * itself, not one that has come back over JSON - while its {@code hashCode}, written the usual way with
 * {@code Objects.hash(...)}, goes on hashing by value.  Equal hash codes and unequal beans is the
 * equals/hashCode contract broken in the direction that is hardest to notice: nothing throws, nothing logs,
 * and a de-duplication step simply stops finding anything.
 * <br><br>
 * It is also inherited.  The first of these found in this repository was in {@code DataAssetProperties},
 * whose {@code authors} list was compared this way - and since more than fifty beans extend it, every
 * {@code CSVFileProperties}, {@code DatabaseProperties} and {@code TopicProperties} in the model had the
 * same defect, from one line.  Seven more turned up elsewhere on the same scan, including a {@code String}
 * compared with {@code ==}, which works for interned literals and for nothing a caller would send.
 * <br><br>
 * A primitive or an enum compared this way is correct and idiomatic, so both are left alone.  Every enum in
 * the implementation is found first, which is why the check reads the whole tree and not only the beans:
 * knowing that {@code contentStatus} is a {@code ContentStatus} is no use without knowing that
 * {@code ContentStatus} is an enum.  A boxed primitive - an {@code Integer} or a {@code Boolean} - is
 * <b>not</b> left alone: comparing those with {@code ==} works only inside the cache the JVM happens to
 * keep, which is the worst kind of working.
 */
class BeanReferenceComparisonTest
{
    /**
     * {@code field == someVariable.field} - the same name on both sides, which is the shape a field
     * comparison takes and which {@code this == objectToCompare} and {@code getClass() != ...} do not.
     */
    private static final Pattern SELF_COMPARISON = Pattern.compile("\\b([a-z][A-Za-z0-9_]*)\\s*==\\s*[A-Za-z_][A-Za-z0-9_]*\\.\\1\\b");

    /**
     * A field declaration, capturing its type and its name.
     */
    private static final Pattern FIELD = Pattern.compile("\\n\\s+private\\s+(?!static)(?:final\\s+)?([A-Za-z0-9_<>,\\[\\]\\.\\? ]+?)\\s+([a-z][A-Za-z0-9_]*)\\s*(?:=|;)");

    /**
     * An enum declaration named after its file.
     */
    private static final Pattern ENUM = Pattern.compile("\\n(?:public\\s+)?enum\\s+(\\w+)\\b");

    /**
     * The primitives, for which {@code ==} is the right comparison.
     */
    private static final Set<String> PRIMITIVES = Set.of("int", "long", "short", "byte", "float", "double",
                                                         "boolean", "char");


    @Test
    @DisplayName("No equals() method compares a reference field with ==")
    void equalsComparesValuesNotReferences()
    {
        List<Path>   classFiles = SourceTree.implementationClasses();
        Set<String>  enumNames  = enumNames(classFiles);
        List<String> offenders  = new ArrayList<>();

        assertTrue(classFiles.size() > 1000,
                   "Expected to find the implementation's source files - found " + classFiles.size() +
                           ".  Has the source layout moved?");

        assertTrue(enumNames.size() > 100,
                   "Expected to find the implementation's enums - found " + enumNames.size() +
                           ".  Without them every enum comparison would be reported as a defect.");

        for (JavaClasses.JavaClass javaClass : JavaClasses.topLevelClasses(classFiles))
        {
            Map<String, String> fieldTypes = fieldTypes(javaClass.source());

            for (JavaMethods.Method method : JavaMethods.publicMethods(javaClass.source()))
            {
                if (! "equals".equals(method.name()))
                {
                    continue;
                }

                Matcher matcher = SELF_COMPARISON.matcher(method.body());

                while (matcher.find())
                {
                    String fieldName = matcher.group(1);
                    String fieldType = fieldTypes.get(fieldName);

                    /*
                     * A name that is not a field of this class is something else - a local, or a field of a
                     * class this one nests - and is not this check's business.
                     */
                    if (fieldType == null)
                    {
                        continue;
                    }

                    String rawType = fieldType.split("<")[0].trim();

                    if (PRIMITIVES.contains(rawType) || enumNames.contains(rawType))
                    {
                        continue;
                    }

                    offenders.add(javaClass.name() + ".equals() compares " + fieldName + " (a " + fieldType +
                                          ") with == rather than Objects.equals(), so the bean only equals" +
                                          " another that holds the very same object (" +
                                          javaClass.file().getFileName() + ")");
                }
            }
        }

        assertTrue(offenders.isEmpty(),
                   "These beans compare a reference field by identity inside equals(), so they never equal a" +
                           " separate bean with the same content while their hashCode still hashes by value:\n    " +
                           String.join("\n    ", offenders));
    }


    /**
     * Return the name of every enum in the supplied files, so that an enum compared with {@code ==} is not
     * reported.
     *
     * @param classFiles files to read
     * @return enum names
     */
    private Set<String> enumNames(List<Path> classFiles)
    {
        Set<String> enumNames = new HashSet<>();

        for (Path classFile : classFiles)
        {
            String  fileName = classFile.getFileName().toString();
            String  expected = fileName.substring(0, fileName.length() - ".java".length());
            Matcher matcher  = ENUM.matcher(SourceTree.read(classFile));

            while (matcher.find())
            {
                if (expected.equals(matcher.group(1)))
                {
                    enumNames.add(matcher.group(1));
                }
            }
        }

        return enumNames;
    }


    /**
     * Return the instance fields the supplied source declares, mapped to their declared types.
     *
     * @param source contents of a java file
     * @return field name to type
     */
    private Map<String, String> fieldTypes(String source)
    {
        Map<String, String> fieldTypes = new java.util.LinkedHashMap<>();
        Matcher             matcher    = FIELD.matcher(source);

        while (matcher.find())
        {
            fieldTypes.put(matcher.group(2), matcher.group(1).trim());
        }

        return fieldTypes;
    }
}
