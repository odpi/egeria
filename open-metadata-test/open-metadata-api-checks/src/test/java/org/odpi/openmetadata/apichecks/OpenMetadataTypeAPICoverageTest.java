/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.apichecks;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * OpenMetadataTypeAPICoverageTest checks that every relationship type and every classification type can
 * be maintained through the open metadata handlers.
 * <br><br>
 * A type with a properties bean but no handler method is invisible to callers: the type exists, the bean
 * exists, and there is no supported way to create or remove an instance of it short of the generic
 * metadata-expert calls.  Gaps like this accumulate quietly, because adding a type does not fail
 * anything.  This check makes them fail the build instead.
 * <br><br>
 * A type counts as covered when a handler method that writes to the store also names the type.  The
 * exclusions below are types where that deliberately does not apply, each for a stated reason.
 */
class OpenMetadataTypeAPICoverageTest
{
    /**
     * Types that are maintained without a handler method naming them.  Each entry needs a reason - if a
     * type is simply missing an API, it belongs in the code rather than in here.
     */
    private static final Set<String> EXPECTED_ABSENTEES = new LinkedHashSet<>(List.of(

            /*
             * Supertypes that exist to be inherited from rather than instantiated directly.
             */
            "LabeledRelationship",
            "RoledRelationship",

            /*
             * Maintained through LineageHandler.linkLineage, which takes the relationship type name as a
             * parameter rather than naming each subtype - AssetHandler.getLineageRelationshipTypeNames()
             * lists them.  UltimateSource and UltimateDestination are normally derived by lineage tooling
             * rather than authored by hand.
             */
            "LineageRelationship",
            "DataLineageRelationship",
            "UltimateSource",
            "UltimateDestination",
            "DataFlow",
            "ControlFlow",
            "ProcessCall",
            "LineageMapping",
            "DataMapping",

            /*
             * Maintained under the covers by MultiLanguageInterface, which manages the TranslationDetail
             * elements and their links to the elements they translate.
             */
            "TranslationLink",

            /*
             * Maintained through GlossaryTermHandler.setupTermRelationship, which takes the relationship
             * type name as a parameter.  getTermRelationshipTypeNames() lists the types it accepts.
             */
            "Synonym",
            "Antonym",
            "RelatedTerm",
            "PreferredTerm",
            "ReplacementTerm",
            "ISARelationship",

            /*
             * Maintained through GovernanceDefinitionHandler.linkPeerDefinitions and
             * attachSupportingDefinition, both of which take the relationship type name as a parameter.
             */
            "GovernanceDriverLink",
            "GovernancePolicyLink",
            "GovernanceControlLink",
            "GovernanceResponse",
            "GovernanceMechanism",

            /*
             * Maintained by the framework itself.  Anchors is set by the anchor plumbing in
             * OpenMetadataHandlerBase from the anchor options supplied on create; Memento is applied by
             * the archive and delete machinery.  A typed API for either would let callers contradict the
             * process that owns them.
             */
            "Anchors",
            "Memento"));

    /*
     * Method name prefixes that mean a method only retrieves.  A type named solely inside one of these -
     * typically populateRootElement, which fills in an element's relationships on the way out - has no
     * way to be created or removed.  Everything else counts, including methods that pass the type name
     * down to a shared helper rather than calling the store directly.
     */
    private static final List<String> RETRIEVAL_PREFIXES = List.of("get", "find", "populate", "convert", "is");


    @Test
    @DisplayName("Every relationship type can be maintained through a handler")
    void relationshipTypesHaveHandlerSupport()
    {
        assertCoverage(RelationshipBeanProperties.class, "relationship");
    }


    @Test
    @DisplayName("Every classification type can be maintained through a handler")
    void classificationTypesHaveHandlerSupport()
    {
        assertCoverage(ClassificationBeanProperties.class, "classification");
    }


    @Test
    @DisplayName("Every excluded type still exists, so the exclusion list does not go stale")
    void exclusionsAreStillRealTypes()
    {
        Set<String> known = new LinkedHashSet<>();

        for (OpenMetadataType type : OpenMetadataType.values())
        {
            known.add(type.typeName);
        }

        List<String> unknown = new ArrayList<>();

        for (String excluded : EXPECTED_ABSENTEES)
        {
            if (! known.contains(excluded))
            {
                unknown.add(excluded);
            }
        }

        assertTrue(unknown.isEmpty(),
                   "These types are excluded from the coverage check but no longer exist.  Remove them from " +
                           "EXPECTED_ABSENTEES:\n    " + String.join("\n    ", unknown));
    }


    /**
     * Check every type whose bean extends the supplied base class.
     *
     * @param beanBaseClass RelationshipBeanProperties or ClassificationBeanProperties
     * @param description word for the kind of type, used in the failure message
     */
    private void assertCoverage(Class<?> beanBaseClass,
                                String   description)
    {
        List<Path>   handlers  = SourceTree.handlers();
        List<String> uncovered = new ArrayList<>();
        int          checked   = 0;

        assertTrue(handlers.size() > 20,
                   "Expected to find the open metadata handlers - found " + handlers.size() +
                           ".  Has the source layout moved?");

        List<String> writeMethods = new ArrayList<>();

        for (Path handler : handlers)
        {
            for (JavaMethods.Method method : JavaMethods.publicMethods(SourceTree.read(handler)))
            {
                if (! isRetrieval(method.name()))
                {
                    writeMethods.add(method.body());
                }
            }
        }

        String maintainedTypes = String.join("\n", writeMethods);

        for (OpenMetadataType type : OpenMetadataType.values())
        {
            if ((type.beanClass == null) || (! beanBaseClass.isAssignableFrom(type.beanClass)))
            {
                continue;
            }

            if (EXPECTED_ABSENTEES.contains(type.typeName))
            {
                continue;
            }

            checked++;

            if (! maintainedTypes.contains("OpenMetadataType." + type.name() + "."))
            {
                uncovered.add(type.typeName + " (OpenMetadataType." + type.name() + ")");
            }
        }

        assertTrue(checked > 50,
                   "Expected to find the " + description + " types - found " + checked +
                           ".  Has the bean hierarchy changed?");

        assertTrue(uncovered.isEmpty(),
                   "These " + description + " types have a properties bean but no handler method that " +
                           "maintains them, so there is no supported way to create or remove one:\n    " +
                           String.join("\n    ", uncovered));
    }


    /**
     * Is this method only retrieving?
     *
     * @param methodName name of the method
     * @return true if the method's name says it retrieves rather than maintains
     */
    private boolean isRetrieval(String methodName)
    {
        for (String prefix : RETRIEVAL_PREFIXES)
        {
            if (methodName.startsWith(prefix))
            {
                return true;
            }
        }

        return false;
    }
}
