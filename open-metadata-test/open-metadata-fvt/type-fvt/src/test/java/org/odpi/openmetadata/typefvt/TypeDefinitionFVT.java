/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.typefvt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextBase;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataTypesClient;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataClassificationDef;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataEntityDef;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRelationshipDef;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataTypeDef;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataTypeDefLink;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * TypeDefinitionFVT checks the shape of the type system the server is running with, before any of the other
 * classes start creating instances against it.
 * <br>
 * These are the checks that catch a type system which is internally inconsistent - a supertype that was
 * never defined, a relationship end pointing at a type that does not exist, an {@code OpenMetadataType} enum
 * constant that names a type the archive does not contain, or the same GUID issued twice.  Every one of
 * those compiles perfectly well and only shows up as a confusing runtime failure much later, which is
 * exactly the kind of thing an FVT should be finding first.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class TypeDefinitionFVT
{
    /**
     * Every supertype named by a type must itself be a type the server knows.
     *
     * @throws Exception problem talking to the server
     */
    @Test
    void everySuperTypeResolves() throws Exception
    {
        Set<String>  knownTypeNames = new HashSet<>();
        List<String> failures       = new ArrayList<>();

        for (OpenMetadataTypeDef typeDef : TypeCatalog.everyTypeDefinition())
        {
            knownTypeNames.add(typeDef.getName());
        }

        for (OpenMetadataTypeDef typeDef : TypeCatalog.everyTypeDefinition())
        {
            OpenMetadataTypeDefLink superType = typeDef.getSuperType();

            if ((superType != null) && (! knownTypeNames.contains(superType.getName())))
            {
                failures.add(typeDef.getName() + " has supertype " + superType.getName() + ", which is not a known type");
            }
        }

        assertTrue(failures.isEmpty(), "Unresolved supertypes: " + failures);
    }


    /**
     * Both ends of every relationship, and every entity a classification says it can be attached to, must
     * name a type the server knows.
     *
     * @throws Exception problem talking to the server
     */
    @Test
    void everyEndAndValidEntityResolves() throws Exception
    {
        Set<String>  entityTypeNames = new HashSet<>();
        List<String> failures        = new ArrayList<>();

        for (OpenMetadataTypeDef typeDef : TypeCatalog.everyTypeDefinition())
        {
            if (typeDef instanceof OpenMetadataEntityDef)
            {
                entityTypeNames.add(typeDef.getName());
            }
        }

        for (OpenMetadataTypeDef typeDef : TypeCatalog.everyTypeDefinition())
        {
            if (typeDef instanceof OpenMetadataRelationshipDef relationshipDef)
            {
                checkEnd(relationshipDef.getName(), "endDef1", relationshipDef.getEndDef1(), entityTypeNames, failures);
                checkEnd(relationshipDef.getName(), "endDef2", relationshipDef.getEndDef2(), entityTypeNames, failures);
            }
            else if (typeDef instanceof OpenMetadataClassificationDef classificationDef)
            {
                if (classificationDef.getValidEntityDefs() != null)
                {
                    for (OpenMetadataTypeDefLink validEntityDef : classificationDef.getValidEntityDefs())
                    {
                        if ((validEntityDef != null) && (! entityTypeNames.contains(validEntityDef.getName())))
                        {
                            failures.add(classificationDef.getName() + " can be attached to " + validEntityDef.getName()
                                                 + ", which is not a known entity type");
                        }
                    }
                }
            }
        }

        assertTrue(failures.isEmpty(), "Unresolved relationship ends / classification targets: " + failures);
    }


    /**
     * No two types may share a GUID.  A copy-pasted GUID is easy to introduce, survives every build, and
     * then makes one of the two types unreachable by GUID at runtime.
     *
     * @throws Exception problem talking to the server
     */
    @Test
    void everyTypeGUIDIsUnique() throws Exception
    {
        Map<String, String> guidOwners = new HashMap<>();
        List<String>        failures   = new ArrayList<>();

        for (OpenMetadataTypeDef typeDef : TypeCatalog.everyTypeDefinition())
        {
            String previousOwner = guidOwners.put(typeDef.getGUID(), typeDef.getName());

            if (previousOwner != null)
            {
                failures.add(typeDef.getGUID() + " is used by both " + previousOwner + " and " + typeDef.getName());
            }
        }

        assertTrue(failures.isEmpty(), "Duplicate type GUIDs: " + failures);
    }


    /**
     * Every {@link OpenMetadataType} constant must name a type the server actually has, with the same GUID.
     * <br>
     * This is the check that keeps the Java view of the model and the archive from drifting apart.  Code
     * all over the codebase reaches for {@code OpenMetadataType.SOMETHING.typeName}; if the archive never
     * defines that type, or defines it under a different GUID, nothing complains until a call fails in
     * production.
     *
     * @throws Exception problem talking to the server
     */
    @Test
    void everyOpenMetadataTypeConstantExistsOnTheServer() throws Exception
    {
        Map<String, OpenMetadataTypeDef> typesByName = new HashMap<>();
        List<String>                     failures    = new ArrayList<>();

        for (OpenMetadataTypeDef typeDef : TypeCatalog.everyTypeDefinition())
        {
            typesByName.put(typeDef.getName(), typeDef);
        }

        for (OpenMetadataType openMetadataType : OpenMetadataType.values())
        {
            OpenMetadataTypeDef typeDef = typesByName.get(openMetadataType.typeName);

            if (typeDef == null)
            {
                failures.add(openMetadataType.name() + " names type '" + openMetadataType.typeName
                                     + "', which the server does not have");
            }
            else if ((openMetadataType.typeGUID != null) && (! openMetadataType.typeGUID.equals(typeDef.getGUID())))
            {
                failures.add(openMetadataType.name() + " (" + openMetadataType.typeName + ") has GUID "
                                     + openMetadataType.typeGUID + " but the server has " + typeDef.getGUID());
            }
        }

        assertTrue(failures.isEmpty(), "OpenMetadataType constants that disagree with the server: " + failures);
    }


    /**
     * Looking a type up by name and by GUID must give the same answer, through the same client the rest of
     * this suite uses.
     *
     * @throws Exception problem talking to the server
     */
    @Test
    void lookupByNameAndByGUIDAgree() throws Exception
    {
        ConnectorContextBase    connectorContext = ConnectorContextFactory.newContext();
        OpenMetadataTypesClient typesClient      = connectorContext.getOpenMetadataTypesClient();
        List<String>            failures         = new ArrayList<>();

        for (OpenMetadataTypeDef typeDef : TypeCatalog.everyTypeDefinition())
        {
            OpenMetadataTypeDef byName = typesClient.getTypeDefByName(false, false, typeDef.getName());
            OpenMetadataTypeDef byGUID = typesClient.getTypeDefByGUID(false, false, typeDef.getGUID());

            if (byName == null)
            {
                failures.add(typeDef.getName() + " could not be looked up by name");
            }
            else if (byGUID == null)
            {
                failures.add(typeDef.getName() + " could not be looked up by GUID " + typeDef.getGUID());
            }
            else if (! byName.getGUID().equals(byGUID.getGUID()))
            {
                failures.add(typeDef.getName() + " resolves to " + byName.getGUID() + " by name but "
                                     + byGUID.getGUID() + " by GUID");
            }
        }

        assertTrue(failures.isEmpty(), "Type lookups that disagree: " + failures);
    }


    /**
     * Every type this suite excludes from instance testing must still be a real type.  An exclusion for a
     * type that has since been removed or renamed hides nothing and should be deleted, but it is invisible
     * unless something checks.
     *
     * @throws Exception problem talking to the server
     */
    @Test
    void everyExclusionStillNamesARealType() throws Exception
    {
        Set<String>  knownTypeNames = new HashSet<>();
        List<String> failures       = new ArrayList<>();

        for (OpenMetadataTypeDef typeDef : TypeCatalog.everyTypeDefinition())
        {
            knownTypeNames.add(typeDef.getName());
        }

        for (String excludedTypeName : TypeCatalog.excludedTypeNames())
        {
            if (! knownTypeNames.contains(excludedTypeName))
            {
                failures.add(excludedTypeName + " is excluded by TypeCatalog but is no longer a type - remove the exclusion");
            }
        }

        assertTrue(failures.isEmpty(), "Stale exclusions: " + failures);
    }


    /**
     * Check one relationship end.
     *
     * @param relationshipTypeName relationship being checked
     * @param endName which end
     * @param endDef the end definition
     * @param entityTypeNames every known entity type name
     * @param failures collected failures
     */
    private void checkEnd(String                                                                              relationshipTypeName,
                          String                                                                              endName,
                          org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRelationshipEndDef endDef,
                          Set<String>                                                                         entityTypeNames,
                          List<String>                                                                        failures)
    {
        if (endDef == null)
        {
            failures.add(relationshipTypeName + " has no " + endName);
        }
        else if ((endDef.getEntityType() == null) || (! entityTypeNames.contains(endDef.getEntityType().getName())))
        {
            failures.add(relationshipTypeName + "." + endName + " points at "
                                 + ((endDef.getEntityType() == null) ? "nothing" : endDef.getEntityType().getName())
                                 + ", which is not a known entity type");
        }
    }
}
