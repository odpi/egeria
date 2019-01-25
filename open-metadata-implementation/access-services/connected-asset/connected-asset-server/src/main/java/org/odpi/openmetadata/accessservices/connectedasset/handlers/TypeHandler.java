/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.connectedasset.handlers;


import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementOrigin;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProvenanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;

/**
 * TypeHandler provides common methods for managing open metadata types and their conversion to types used
 * vy Connected Asset OMAS.
 */
public class TypeHandler
{

    /**
     * Default constructor
     */
    public TypeHandler()
    {
    }


    /**
     * Convert information from a repository instance into an Open Connector Framework ElementType.
     *
     * @param instanceType type information from the instance
     * @param instanceProvenanceType provenance information from the instance
     * @param metadataCollectionId home of the instance
     * @param serverName name of this server
     * @return OCF ElementType object
     */
    ElementType getElementType(InstanceType            instanceType,
                               InstanceProvenanceType  instanceProvenanceType,
                               String                  metadataCollectionId,
                               String                  serverName)
    {
        if (instanceType != null)
        {
            ElementType  elementType = new ElementType();

            elementType.setElementTypeId(instanceType.getTypeDefGUID());
            elementType.setElementTypeName(instanceType.getTypeDefName());
            elementType.setElementTypeVersion(instanceType.getTypeDefVersion());
            elementType.setElementTypeDescription(instanceType.getTypeDefDescription());
            elementType.setElementSourceServer(serverName);
            elementType.setElementHomeMetadataCollectionId(metadataCollectionId);

            switch (instanceProvenanceType)
            {
                case UNKNOWN:
                    elementType.setElementOrigin(ElementOrigin.UNKNOWN);
                    break;
                case CONTENT_PACK:
                    elementType.setElementOrigin(ElementOrigin.CONTENT_PACK);
                    break;
                case EXPORT_ARCHIVE:
                    elementType.setElementOrigin(ElementOrigin.EXPORT_ARCHIVE);
                    break;
                case LOCAL_COHORT:
                    elementType.setElementOrigin(ElementOrigin.LOCAL_COHORT);
                    break;
                case EXTERNAL_TOOL:
                    elementType.setElementOrigin(ElementOrigin.EXTERNAL_TOOL);
                    break;
                case DATA_PLATFORM:
                    elementType.setElementOrigin(ElementOrigin.DATA_PLATFORM);
                    break;
                case EXTERNAL_ENGINE:
                    elementType.setElementOrigin(ElementOrigin.EXTERNAL_ENGINE);
                    break;
                case DEREGISTERED_REPOSITORY:
                    elementType.setElementOrigin(ElementOrigin.DEREGISTERED_REPOSITORY);
                    break;
                case CONFIGURATION:
                    elementType.setElementOrigin(ElementOrigin.CONFIGURATION);
                    break;
            }

            return elementType;
        }
        else
        {
            return null;
        }
    }
}
