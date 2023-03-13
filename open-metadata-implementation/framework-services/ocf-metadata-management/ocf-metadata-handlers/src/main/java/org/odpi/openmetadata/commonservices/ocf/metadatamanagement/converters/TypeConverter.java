/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.converters;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementOriginCategory;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProvenanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefLink;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * TypeHandler provides common methods for managing open metadata types and their conversion to types used
 * by Connected Asset OMAS.
 */
class TypeConverter
{
    private final OMRSRepositoryHelper repositoryHelper;
    private final String sourceName;

    TypeConverter(OMRSRepositoryHelper repositoryHelper, String sourceName)
    {
        this.repositoryHelper = repositoryHelper;
        this.sourceName = sourceName;
    }


    /**
     * Convert information from a repository instance into an Open Connector Framework ElementType.
     *
     * @param instanceType type information from the instance
     * @return OCF ElementType object
     */
    ElementType getElementType(InstanceType            instanceType)
    {
        ElementType  elementType = new ElementType();

        if (instanceType != null)
        {
            String typeName = instanceType.getTypeDefName();

            elementType.setTypeId(instanceType.getTypeDefGUID());
            elementType.setTypeName(typeName);
            elementType.setTypeVersion(instanceType.getTypeDefVersion());

            String description = repositoryHelper.getTypeDefByName(sourceName, typeName).getDescription();

            elementType.setTypeDescription(description);

            List<TypeDefLink> typeDefSuperTypes = repositoryHelper.getSuperTypes(sourceName, typeName);

            if ((typeDefSuperTypes != null) && (! typeDefSuperTypes.isEmpty()))
            {
                List<String>   superTypes = new ArrayList<>();

                for (TypeDefLink typeDefLink : typeDefSuperTypes)
                {
                    if (typeDefLink != null)
                    {
                        superTypes.add(typeDefLink.getName());
                    }
                }

                if (! superTypes.isEmpty())
                {
                    elementType.setSuperTypeNames(superTypes);
                }
            }
        }

        return elementType;
    }


    /**
     * Translate the repository services' InstanceProvenanceType to an ElementOrigin.
     *
     * @param instanceProvenanceType value from the repository services
     * @return ElementOrigin enum
     */
    public ElementOriginCategory getElementOrigin(InstanceProvenanceType   instanceProvenanceType)
    {
        if (instanceProvenanceType != null)
        {
            switch (instanceProvenanceType)
            {
                case DEREGISTERED_REPOSITORY:
                    return ElementOriginCategory.DEREGISTERED_REPOSITORY;

                case EXTERNAL_SOURCE:
                    return ElementOriginCategory.EXTERNAL_SOURCE;

                case EXPORT_ARCHIVE:
                    return ElementOriginCategory.EXPORT_ARCHIVE;

                case LOCAL_COHORT:
                    return ElementOriginCategory.LOCAL_COHORT;

                case CONTENT_PACK:
                    return ElementOriginCategory.CONTENT_PACK;

                case CONFIGURATION:
                    return ElementOriginCategory.CONFIGURATION;

                case UNKNOWN:
                    return ElementOriginCategory.UNKNOWN;
            }
        }

        return ElementOriginCategory.UNKNOWN;
    }
}
