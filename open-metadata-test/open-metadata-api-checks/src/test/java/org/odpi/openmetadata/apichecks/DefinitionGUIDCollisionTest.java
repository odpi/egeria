/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.apichecks;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * DefinitionGUIDCollisionTest checks that no GUID belonging to an open metadata type is also used as the
 * identity of an element that a content pack creates.
 * <br><br>
 * Content pack writers hard code the GUID of each element they create, in definition enums such as
 * {@code EgeriaOpenConnectorDefinition}, so that regenerating a pack keeps every element's identity.  The
 * archive builder keeps <b>one</b> GUID namespace for the whole archive - type definitions and instances
 * together - and a content pack loads the open metadata types as a dependent archive.  A definition whose
 * GUID is already a type's GUID is therefore an element the builder will refuse to add.
 * <br><br>
 * This is not theoretical.  {@code SLF4J_AUDIT_LOG_DESTINATION_CONNECTOR} carried
 * {@code e8303911-ba1c-4640-974e-c4d57ee1b310}, which is the type GUID of the
 * {@code DigitalProductDependency} relationship.  The builder refused the connector type entity, the
 * refusal was swallowed by the archive helper, and {@code CoreContentPack} shipped for its whole history
 * with a collection membership relationship pointing at a connector type that was not in the pack - the
 * only dangling reference in any pack.  Nothing failed, and nothing said so.
 * <br><br>
 * Two GUIDs colliding is a coincidence that no amount of care prevents and no reviewer would spot, which is
 * exactly what a check is for.  A collision found here is fixed by giving the <i>definition</i> a fresh
 * GUID - the type's GUID is fixed by the open metadata standard and by every repository that has stored an
 * instance of it.  Whether a new GUID for the definition is safe depends on whether that element has ever
 * shipped: if it has, changing it makes a second copy of it everywhere the old one was loaded.
 */
class DefinitionGUIDCollisionTest
{
    /**
     * A GUID written as a string literal.
     */
    private static final Pattern GUID = Pattern.compile("\"([0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12})\"");

    /**
     * The class that declares the open metadata type GUIDs.
     */
    private static final String TYPE_REGISTRY = "OpenMetadataType";

    /**
     * The enums that hold the identities of the elements a content pack creates.  Matching on the name
     * rather than listing them keeps a new definition enum covered without anybody remembering to add it.
     */
    private static final Pattern DEFINITION_ENUM = Pattern.compile(".*(Definition|Enum|ContentCollection|RoleDefinition|SolutionComponent|SolutionBlueprint)$");


    @Test
    @DisplayName("No content pack definition claims an open metadata type's GUID")
    void definitionGUIDsDoNotCollideWithTypeGUIDs()
    {
        List<Path>               classFiles     = SourceTree.implementationClasses();
        Set<String>              typeGUIDs      = new HashSet<>();
        Map<String, Set<String>> definitionGUIDs = new LinkedHashMap<>();

        assertTrue(classFiles.size() > 1000,
                   "Expected to find the implementation's source files - found " + classFiles.size() +
                           ".  Has the source layout moved?");

        /*
         * The files are read directly rather than through JavaClasses, because the definitions are enums and
         * that helper only returns classes.
         */
        for (Path classFile : classFiles)
        {
            String fileName  = classFile.getFileName().toString();
            String className = fileName.substring(0, fileName.length() - ".java".length());

            collect(classFile, className, typeGUIDs, definitionGUIDs);
        }

        assertTrue(typeGUIDs.size() > 1000,
                   "Expected to find the open metadata type GUIDs in " + TYPE_REGISTRY + " - found " +
                           typeGUIDs.size() + ".  Has it moved or been renamed?");

        assertTrue(definitionGUIDs.size() > 3,
                   "Expected to find the content pack definition enums - found " + definitionGUIDs.size() +
                           ".  Have they been renamed out of the pattern this check matches?");

        List<String> collisions = new ArrayList<>();

        for (Map.Entry<String, Set<String>> definition : definitionGUIDs.entrySet())
        {
            for (String guid : new TreeSet<>(definition.getValue()))
            {
                if (typeGUIDs.contains(guid))
                {
                    collisions.add(definition.getKey() + " uses " + guid + ", which is an open metadata type's" +
                                           " GUID - the archive builder will refuse the element and the pack" +
                                           " will ship without it");
                }
            }
        }

        assertTrue(collisions.isEmpty(),
                   "A content pack element cannot share a GUID with an open metadata type - the archive" +
                           " builder keeps one GUID namespace for types and instances together. Give the" +
                           " definition a fresh GUID:\n    " + String.join("\n    ", collisions));
    }


    /**
     * Read one file, adding its GUIDs to the type registry or to a definition as appropriate.
     *
     * @param classFile file to read
     * @param className the type it declares
     * @param typeGUIDs collected open metadata type GUIDs
     * @param definitionGUIDs collected definition GUIDs, by declaring type
     */
    private void collect(Path                     classFile,
                         String                   className,
                         Set<String>              typeGUIDs,
                         Map<String, Set<String>> definitionGUIDs)
    {
        boolean isTypeRegistry = TYPE_REGISTRY.equals(className);
        boolean isDefinition   = DEFINITION_ENUM.matcher(className).matches();

        if ((! isTypeRegistry) && (! isDefinition))
        {
            return;
        }

        Set<String> found   = new HashSet<>();
        Matcher     matcher = GUID.matcher(SourceTree.read(classFile));

        while (matcher.find())
        {
            found.add(matcher.group(1));
        }

        if (found.isEmpty())
        {
            return;
        }

        if (isTypeRegistry)
        {
            typeGUIDs.addAll(found);
        }
        else
        {
            definitionGUIDs.merge(className, found, (existing, added) ->
            {
                existing.addAll(added);
                return existing;
            });
        }
    }
}
