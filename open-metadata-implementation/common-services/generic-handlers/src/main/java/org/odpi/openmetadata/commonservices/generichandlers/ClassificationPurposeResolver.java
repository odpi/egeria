/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.openmetadata.enums.OpenMetadataClassificationPurpose;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.ClassificationDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * ClassificationPurposeResolver works out the purpose of a classification type.  The purpose recorded in the
 * ClassificationDef is used first.  If it is missing (for example, the type came from a cohort member running
 * an older types archive), the purpose defined in OpenMetadataType is used.  A type that has neither
 * (such as a type added by an extension archive) inherits the purpose of its nearest super type.
 */
public class ClassificationPurposeResolver
{
    private static final Map<String, OpenMetadataClassificationPurpose> openMetadataTypePurposes = new HashMap<>();

    static
    {
        for (OpenMetadataType openMetadataType : OpenMetadataType.values())
        {
            if (openMetadataType.classificationPurpose != null)
            {
                openMetadataTypePurposes.put(openMetadataType.typeName, openMetadataType.classificationPurpose);
            }
        }
    }


    /**
     * Return the purpose of the named classification type.
     *
     * @param sourceName calling service
     * @param typeName name of the classification type
     * @param repositoryHelper helper for retrieving the type definitions
     * @return purpose, or null if none can be found
     */
    public static OpenMetadataClassificationPurpose getClassificationPurpose(String               sourceName,
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

            if (typeDef instanceof ClassificationDef classificationDef)
            {
                OpenMetadataClassificationPurpose purpose = OpenMetadataClassificationPurpose.fromName(classificationDef.getClassificationPurpose());

                if (purpose != null)
                {
                    return purpose;
                }
            }

            OpenMetadataClassificationPurpose purpose = openMetadataTypePurposes.get(currentTypeName);

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
     * Return the purpose of the supplied classification type.
     *
     * @param sourceName calling service
     * @param classificationDef classification type
     * @param repositoryHelper helper for retrieving super types
     * @return purpose, or null if none can be found
     */
    public static OpenMetadataClassificationPurpose getClassificationPurpose(String               sourceName,
                                                                             ClassificationDef    classificationDef,
                                                                             OMRSRepositoryHelper repositoryHelper)
    {
        if (classificationDef != null)
        {
            OpenMetadataClassificationPurpose purpose = OpenMetadataClassificationPurpose.fromName(classificationDef.getClassificationPurpose());

            if (purpose != null)
            {
                return purpose;
            }

            return getClassificationPurpose(sourceName, classificationDef.getName(), repositoryHelper);
        }

        return null;
    }
}
