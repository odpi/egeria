/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.connectedasset.converters;


import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementOrigin;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProvenanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;

/**
 * TypeHandler provides common methods for managing open metadata types and their conversion to types used
 * by Connected Asset OMAS.
 */
public class TypeConverter
{

    /**
     * Default constructor
     */
    public TypeConverter()
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
    public ElementType getElementType(InstanceType            instanceType,
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
        }

        elementType.setElementHomeMetadataCollectionId(metadataCollectionId);
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

                case EXTERNAL_ENGINE:
                    return ElementOrigin.EXTERNAL_ENGINE;

                case EXPORT_ARCHIVE:
                    return ElementOrigin.EXPORT_ARCHIVE;

                case EXTERNAL_TOOL:
                    return ElementOrigin.EXTERNAL_TOOL;

                case LOCAL_COHORT:
                    return ElementOrigin.LOCAL_COHORT;

                case CONTENT_PACK:
                    return ElementOrigin.CONTENT_PACK;

                case DATA_PLATFORM:
                    return ElementOrigin.DATA_PLATFORM;

                case CONFIGURATION:
                    return ElementOrigin.CONFIGURATION;

                case UNKNOWN:
                    return ElementOrigin.UNKNOWN;
            }
        }

        return ElementOrigin.UNKNOWN;
    }
}
