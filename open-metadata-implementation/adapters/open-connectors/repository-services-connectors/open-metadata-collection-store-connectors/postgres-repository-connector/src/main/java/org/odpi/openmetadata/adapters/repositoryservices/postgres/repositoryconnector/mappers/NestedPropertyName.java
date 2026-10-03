/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.repositoryservices.postgres.repositoryconnector.mappers;

import java.util.ArrayList;
import java.util.List;

/**
 * Encodes the names of nested properties - the keys of a map, the attributes of a struct and the
 * indexes of an array - in the property name column of the attribute value tables.
 * <p>
 * A nested property is stored with a property name made of its parent's property name, a colon, and
 * its own name, such as {@code additionalProperties:jobType}.  A map key may itself contain a colon
 * (for example {@code tag:coco}), so each name added to the path is escaped: a backslash becomes
 * {@code \\} and a colon becomes {@code \:}.  The path is then split only on unescaped colons, and a
 * name is unescaped only when it becomes a property name again.
 */
public final class NestedPropertyName
{
    private static final char SEPARATOR = ':';
    private static final char ESCAPE    = '\\';


    /**
     * Private constructor for a utility class.
     */
    private NestedPropertyName()
    {
    }


    /**
     * Escape a name so that it can be added to a property name path.
     *
     * @param name name of a nested property (map key, struct attribute or array index)
     * @return escaped name
     */
    public static String escape(String name)
    {
        if (name == null)
        {
            return null;
        }

        StringBuilder escapedName = new StringBuilder(name.length());

        for (int i = 0; i < name.length(); i++)
        {
            char c = name.charAt(i);

            if ((c == SEPARATOR) || (c == ESCAPE))
            {
                escapedName.append(ESCAPE);
            }
            escapedName.append(c);
        }

        return escapedName.toString();
    }


    /**
     * Reverse {@link #escape(String)}.
     *
     * @param escapedName escaped name
     * @return original name
     */
    public static String unescape(String escapedName)
    {
        if ((escapedName == null) || (escapedName.indexOf(ESCAPE) < 0))
        {
            return escapedName;
        }

        StringBuilder name = new StringBuilder(escapedName.length());

        for (int i = 0; i < escapedName.length(); i++)
        {
            char c = escapedName.charAt(i);

            if ((c == ESCAPE) && (i + 1 < escapedName.length()))
            {
                i++;
                c = escapedName.charAt(i);
            }
            name.append(c);
        }

        return name.toString();
    }


    /**
     * Split a property name path on its unescaped colons.  The parts keep their escaping.
     *
     * @param propertyName property name path, such as {@code additionalProperties:tag\:coco}
     * @return parts of the path, such as {@code additionalProperties} and {@code tag\:coco}
     */
    public static List<String> split(String propertyName)
    {
        List<String> parts = new ArrayList<>();

        if (propertyName == null)
        {
            return parts;
        }

        StringBuilder part = new StringBuilder();

        for (int i = 0; i < propertyName.length(); i++)
        {
            char c = propertyName.charAt(i);

            if ((c == ESCAPE) && (i + 1 < propertyName.length()))
            {
                part.append(c).append(propertyName.charAt(i + 1));
                i++;
            }
            else if (c == SEPARATOR)
            {
                parts.add(part.toString());
                part.setLength(0);
            }
            else
            {
                part.append(c);
            }
        }

        parts.add(part.toString());

        return parts;
    }
}
