/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.typefvt;

import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextBase;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataClassificationDef;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataEntityDef;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRelationshipDef;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataTypeDef;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataTypeDefAttribute;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataTypeDefAttributeStatus;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataTypeDefGallery;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataTypeDefStatus;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * TypeCatalog is the list of types this suite works through, read once from the running server via the
 * connector context's {@code OpenMetadataTypesClient} and cached for the whole run.
 * <br>
 * The server's view is used in preference to reading {@code OpenMetadataTypes.omarchive} directly for one
 * important reason: a type's real shape is its {@code newTypeDefs} entry merged with every later
 * {@code typeDefPatch}, and it is the server that has already done that merging.  Reading the archive's
 * {@code newTypeDefs} alone would give a stale supertype, a stale attribute list and stale relationship
 * ends for every type that has ever been patched - which is most of the interesting ones.
 * <br>
 * Asking for {@code getInheritedAttributes = true} means each type arrives with its inherited attributes
 * already folded in, so a test can populate everything an instance of that type accepts without walking
 * the supertype chain itself.
 */
final class TypeCatalog
{
    /**
     * Types this suite does not attempt to instantiate, and why.
     * <br>
     * This list is deliberately short and every entry needs a reason.  It is not a place to park types
     * that fail: a type that cannot be created through the connector context, and is not listed here, is
     * a finding this suite exists to report.
     */
    private static final Map<String, String> UNINSTANTIABLE_TYPES = new LinkedHashMap<>();

    /**
     * Classifications this suite does not attach itself, and why.
     */
    private static final Map<String, String> UNATTACHABLE_CLASSIFICATIONS = new LinkedHashMap<>()
    {{
        put("Anchors", "the platform sets this itself from the anchor options on a create call, and its properties"
                    + " are the GUIDs of other elements - a generated value is rejected because the platform"
                    + " resolves it");
    }};

    /**
     * Relationships whose ends this suite cannot satisfy by creating a plain instance of each end type.
     * Empty today - kept so that a genuine exclusion has an obvious, documented home rather than being
     * hidden inside a test.
     */
    private static final Map<String, String> UNLINKABLE_RELATIONSHIPS = new LinkedHashMap<>();

    private static volatile List<OpenMetadataTypeDef> allTypes;

    private TypeCatalog()
    {
        // no instances
    }


    /**
     * Read every type the server knows about, once per run.
     *
     * @return all type definitions, in name order so that test reports are stable between runs
     * @throws Exception problem talking to the server - fatal, since there is nothing to test without it
     */
    private static synchronized List<OpenMetadataTypeDef> allTypes() throws Exception
    {
        if (allTypes == null)
        {
            ConnectorContextBase       connectorContext = ConnectorContextFactory.newContext();
            OpenMetadataTypeDefGallery gallery          = connectorContext.getOpenMetadataTypesClient().getAllTypes(true, false);

            List<OpenMetadataTypeDef> typeDefs = new ArrayList<>(gallery.getTypeDefs());

            typeDefs.sort(Comparator.comparing(OpenMetadataTypeDef::getName));

            allTypes = typeDefs;
        }

        return allTypes;
    }


    /**
     * Return the names of every active entity type that this suite should be able to create an instance of.
     *
     * @return type names, in name order
     * @throws Exception problem talking to the server
     */
    static List<String> entityTypeNames() throws Exception
    {
        List<String> names = new ArrayList<>();

        for (OpenMetadataTypeDef typeDef : allTypes())
        {
            if ((typeDef instanceof OpenMetadataEntityDef) && isActive(typeDef) && (! UNINSTANTIABLE_TYPES.containsKey(typeDef.getName())))
            {
                names.add(typeDef.getName());
            }
        }

        return names;
    }


    /**
     * Return the names of every active classification type.
     *
     * @return type names, in name order
     * @throws Exception problem talking to the server
     */
    static List<String> classificationTypeNames() throws Exception
    {
        List<String> names = new ArrayList<>();

        for (OpenMetadataTypeDef typeDef : allTypes())
        {
            if ((typeDef instanceof OpenMetadataClassificationDef) && isActive(typeDef)
                        && (! UNATTACHABLE_CLASSIFICATIONS.containsKey(typeDef.getName())))
            {
                names.add(typeDef.getName());
            }
        }

        return names;
    }


    /**
     * Return the names of every active relationship type whose two ends this suite can satisfy.
     *
     * @return type names, in name order
     * @throws Exception problem talking to the server
     */
    static List<String> relationshipTypeNames() throws Exception
    {
        List<String> names = new ArrayList<>();

        for (OpenMetadataTypeDef typeDef : allTypes())
        {
            if ((typeDef instanceof OpenMetadataRelationshipDef) && isActive(typeDef)
                        && (! UNLINKABLE_RELATIONSHIPS.containsKey(typeDef.getName())))
            {
                names.add(typeDef.getName());
            }
        }

        return names;
    }


    /**
     * Return every type definition, whatever its category or status - used by the checks that look at the
     * shape of the type system rather than at instances of it.
     *
     * @return all type definitions, in name order
     * @throws Exception problem talking to the server
     */
    static List<OpenMetadataTypeDef> everyTypeDefinition() throws Exception
    {
        return allTypes();
    }


    /**
     * Look one type up by name, as the server reports it.
     *
     * @param typeName name to find
     * @return type definition, or null if the server does not know it
     * @throws Exception problem talking to the server
     */
    static OpenMetadataTypeDef typeDefinition(String typeName) throws Exception
    {
        for (OpenMetadataTypeDef typeDef : allTypes())
        {
            if (typeDef.getName().equals(typeName))
            {
                return typeDef;
            }
        }

        return null;
    }


    /**
     * Return the attributes an instance of this type accepts: everything the type declares plus everything
     * it inherits (the gallery was fetched with getInheritedAttributes=true), minus any attribute that is
     * no longer active.
     * <br>
     * A deprecated or renamed attribute is skipped deliberately.  Such an attribute is still present in the
     * type definition so that existing instances keep working, but it is not something new instances should
     * be setting, and a rename pair (old name plus its replacement) would otherwise be written twice.
     *
     * @param typeDef type to describe
     * @return attributes to populate, never null
     */
    static List<OpenMetadataTypeDefAttribute> instantiableAttributes(OpenMetadataTypeDef typeDef)
    {
        List<OpenMetadataTypeDefAttribute> attributes = new ArrayList<>();

        if (typeDef.getAttributeDefinitions() != null)
        {
            for (OpenMetadataTypeDefAttribute attribute : typeDef.getAttributeDefinitions())
            {
                OpenMetadataTypeDefAttributeStatus status = attribute.getAttributeStatus();

                if ((status == null) || (status == OpenMetadataTypeDefAttributeStatus.ACTIVE_ATTRIBUTE))
                {
                    attributes.add(attribute);
                }
            }
        }

        return attributes;
    }


    /**
     * Return the reason a type is excluded from instance testing, for use in a skip message.
     *
     * @param typeName type to explain
     * @return reason, or null if the type is not excluded
     */
    static String exclusionReason(String typeName)
    {
        String reason = UNINSTANTIABLE_TYPES.get(typeName);

        if (reason == null)
        {
            reason = UNATTACHABLE_CLASSIFICATIONS.get(typeName);
        }

        return (reason != null) ? reason : UNLINKABLE_RELATIONSHIPS.get(typeName);
    }


    /**
     * Return the names of every excluded type, so a test can assert that each exclusion still names a real
     * type - an exclusion for a type that no longer exists is dead weight that hides nothing and should go.
     *
     * @return excluded type names
     */
    static Set<String> excludedTypeNames()
    {
        Set<String> names = new java.util.LinkedHashSet<>(UNINSTANTIABLE_TYPES.keySet());

        names.addAll(UNATTACHABLE_CLASSIFICATIONS.keySet());
        names.addAll(UNLINKABLE_RELATIONSHIPS.keySet());

        return names;
    }


    /**
     * A type is active unless it has been explicitly deprecated or renamed.  A null status means active -
     * that is the documented default for {@link OpenMetadataTypeDefStatus}.
     *
     * @param typeDef type to check
     * @return true if instances of this type should still be creatable
     */
    private static boolean isActive(OpenMetadataTypeDef typeDef)
    {
        return (typeDef.getStatus() == null) || (typeDef.getStatus() == OpenMetadataTypeDefStatus.ACTIVE_TYPEDEF);
    }
}
