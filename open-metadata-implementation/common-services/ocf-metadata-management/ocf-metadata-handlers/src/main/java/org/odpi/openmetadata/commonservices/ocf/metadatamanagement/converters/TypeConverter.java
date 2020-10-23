/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.converters;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementOrigin;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProvenanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefLink;

import java.util.ArrayList;
import java.util.List;

/**
 * TypeHandler provides common methods for managing open metadata types and their conversion to types used
 * by Connected Asset OMAS.
 */
class TypeConverter
{

    /**
     * Default constructor
     */
    TypeConverter()
    {
    }


    /**
     * Convert information from a repository instance into an Open Connector Framework ElementType.
     *
     * @param instanceType type information from the instance
     * @param instanceProvenanceType provenance information from the instance
     * @param metadataCollectionId home of the instance
     * @param metadataCollectionName name of the instance's home
     * @param elementLicense any attached license
     * @return OCF ElementType object
     */
    ElementType getElementType(InstanceType            instanceType,
                               InstanceProvenanceType  instanceProvenanceType,
                               String                  metadataCollectionId,
                               String                  metadataCollectionName,
                               String                  elementLicense)
    {
        ElementType  elementType = new ElementType();

        if (instanceType != null)
        {
            elementType.setElementTypeId(instanceType.getTypeDefGUID());
            elementType.setElementTypeName(instanceType.getTypeDefName());
            elementType.setElementTypeVersion(instanceType.getTypeDefVersion());
            elementType.setElementTypeDescription(instanceType.getTypeDefDescription());

            List<TypeDefLink> typeDefSuperTypes = instanceType.getTypeDefSuperTypes();

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
                    elementType.setElementSuperTypeNames(superTypes);
                }
            }
        }

        elementType.setElementMetadataCollectionId(metadataCollectionId);
        elementType.setElementMetadataCollectionName(metadataCollectionName);
        elementType.setElementOrigin(this.getElementOrigin(instanceProvenanceType));
        elementType.setElementSourceServer(metadataCollectionName);
        elementType.setElementLicense(elementLicense);

        return elementType;
    }


    /**
     * Translate the repository services' InstanceProvenanceType to an ElementOrigin.
     *
     * @param instanceProvenanceType value from the repository services
     * @return ElementOrigin enum
     */
    private ElementOrigin getElementOrigin(InstanceProvenanceType   instanceProvenanceType)
    {
        if (instanceProvenanceType != null)
        {
            switch (instanceProvenanceType)
            {
                case DEREGISTERED_REPOSITORY:
                    return ElementOrigin.DEREGISTERED_REPOSITORY;

                case EXTERNAL_SOURCE:
                    return ElementOrigin.EXTERNAL_SOURCE;

                case EXPORT_ARCHIVE:
                    return ElementOrigin.EXPORT_ARCHIVE;

                case LOCAL_COHORT:
                    return ElementOrigin.LOCAL_COHORT;

                case CONTENT_PACK:
                    return ElementOrigin.CONTENT_PACK;

                case CONFIGURATION:
                    return ElementOrigin.CONFIGURATION;

                case UNKNOWN:
                    return ElementOrigin.UNKNOWN;
            }
        }

        return ElementOrigin.UNKNOWN;
    }
}
