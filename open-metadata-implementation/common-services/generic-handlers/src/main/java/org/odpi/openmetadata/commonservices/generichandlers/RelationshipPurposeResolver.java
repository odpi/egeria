/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.openmetadata.enums.OpenMetadataRelationshipPurpose;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.RelationshipDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * RelationshipPurposeResolver works out the purpose of a relationship type.  The purpose recorded in the
 * RelationshipDef is used first.  If it is missing (for example, the type came from a cohort member running
 * an older types archive), the purpose defined in OpenMetadataType is used.  A type that has neither
 * (such as a type added by an extension archive) inherits the purpose of its nearest super type.
 */
public class RelationshipPurposeResolver
{
    private static final Map<String, OpenMetadataRelationshipPurpose> openMetadataTypePurposes = new HashMap<>();

    static
    {
        for (OpenMetadataType openMetadataType : OpenMetadataType.values())
        {
            if (openMetadataType.relationshipPurpose != null)
            {
                openMetadataTypePurposes.put(openMetadataType.typeName, openMetadataType.relationshipPurpose);
            }
        }
    }


    /**
     * Return the purpose of the named relationship type.
     *
     * @param sourceName calling service
     * @param typeName name of the relationship type
     * @param repositoryHelper helper for retrieving the type definitions
     * @return purpose, or null if none can be found
     */
    public static OpenMetadataRelationshipPurpose getRelationshipPurpose(String               sourceName,
                                                                         String               typeName,
                                                                         OMRSRepositoryHelper repositoryHelper)
    {
        String currentTypeName = typeName;

        while (currentTypeName != null)
        {
            TypeDef typeDef = null;

            if (repositoryHelper != null)
            {
                typeDef = repositoryHelper.getTypeDefByName(sourceName, currentTypeName);
            }

            if (typeDef instanceof RelationshipDef relationshipDef)
            {
                OpenMetadataRelationshipPurpose purpose = OpenMetadataRelationshipPurpose.fromName(relationshipDef.getRelationshipPurpose());

                if (purpose != null)
                {
                    return purpose;
                }
            }

            OpenMetadataRelationshipPurpose purpose = openMetadataTypePurposes.get(currentTypeName);

            if (purpose != null)
            {
                return purpose;
            }

            if ((typeDef != null) && (typeDef.getSuperType() != null))
            {
                currentTypeName = typeDef.getSuperType().getName();
            }
            else
            {
                currentTypeName = null;
            }
        }

        return null;
    }


    /**
     * Return the purpose of the supplied relationship type.
     *
     * @param sourceName calling service
     * @param relationshipDef relationship type
     * @param repositoryHelper helper for retrieving super types
     * @return purpose, or null if none can be found
     */
    public static OpenMetadataRelationshipPurpose getRelationshipPurpose(String               sourceName,
                                                                         RelationshipDef      relationshipDef,
                                                                         OMRSRepositoryHelper repositoryHelper)
    {
        if (relationshipDef != null)
        {
            OpenMetadataRelationshipPurpose purpose = OpenMetadataRelationshipPurpose.fromName(relationshipDef.getRelationshipPurpose());

            if (purpose != null)
            {
                return purpose;
            }

            return getRelationshipPurpose(sourceName, relationshipDef.getName(), repositoryHelper);
        }

        return null;
    }
}
