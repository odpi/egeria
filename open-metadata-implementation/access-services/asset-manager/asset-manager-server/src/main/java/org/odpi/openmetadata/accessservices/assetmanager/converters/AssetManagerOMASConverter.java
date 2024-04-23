/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.converters;

import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.RelatedElement;
import org.odpi.openmetadata.accessservices.assetmanager.properties.*;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIGenericConverter;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DataItemSortOrder;
import org.odpi.openmetadata.frameworks.openmetadata.enums.KeyPattern;
import org.odpi.openmetadata.frameworks.openmetadata.enums.PortType;
import org.odpi.openmetadata.frameworks.openmetadata.enums.SynchronizationDirection;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementStub;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;


/**
 * AssetManagerOMASConverter provides the generic methods for the Asset Manager beans' converters.  Generic classes
 * have limited knowledge of the classes these are working on and this means creating a new instance of a
 * class from within a generic is a little involved.  This class provides the generic method for creating
 * and initializing an Asset Manager bean.
 */
public abstract class AssetManagerOMASConverter<B> extends OpenMetadataAPIGenericConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName name of this server
     */
    public AssetManagerOMASConverter(OMRSRepositoryHelper   repositoryHelper,
                                     String                 serviceName,
                                     String                 serverName)
    {
        super (repositoryHelper, serviceName, serverName);
    }


    /*===============================
     * Methods to fill out headers and enums
     */

    /**
     * Using the supplied instances, return a new instance of a relatedElement bean. This is used for beans that
     * contain a combination of the properties from an entity and that of a connected relationship.
     *
     * @param beanClass name of the class to create
     * @param entity entity containing the properties
     * @param relationship relationship containing the properties
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    public RelatedElement getRelatedElement(Class<B>     beanClass,
                                            EntityDetail entity,
                                            Relationship relationship,
                                            String       methodName) throws PropertyServerException
    {
        RelatedElement relatedElement = new RelatedElement();

        relatedElement.setRelationshipHeader(this.getMetadataElementHeader(beanClass, relationship, null, methodName));

        if (relationship != null)
        {
            InstanceProperties instanceProperties = new InstanceProperties(relationship.getProperties());

            RelationshipProperties relationshipProperties = new RelationshipProperties();

            relationshipProperties.setEffectiveFrom(instanceProperties.getEffectiveFromTime());
            relationshipProperties.setEffectiveTo(instanceProperties.getEffectiveToTime());
            relationshipProperties.setExtendedProperties(this.getRemainingExtendedProperties(instanceProperties));

            relatedElement.setRelationshipProperties(relationshipProperties);
        }
        else
        {
            handleMissingMetadataInstance(beanClass.getName(), TypeDefCategory.RELATIONSHIP_DEF, methodName);
        }


        if (entity != null)
        {
            ElementStub elementStub = this.getElementStub(beanClass, entity, methodName);

            relatedElement.setRelatedElement(elementStub);
        }
        else
        {
            handleMissingMetadataInstance(beanClass.getName(), TypeDefCategory.ENTITY_DEF, methodName);
        }

        return relatedElement;
    }



    /**
     * Extract and delete the sortOrder property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return DataItemSortOrder enum
     */
    DataItemSortOrder removeSortOrder(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeSortOrder";

        if (instanceProperties != null)
        {
            int ordinal = repositoryHelper.removeEnumPropertyOrdinal(serviceName,
                                                                     OpenMetadataProperty.SORT_ORDER.name,
                                                                     instanceProperties,
                                                                     methodName);

            for (DataItemSortOrder dataItemSortOrder : DataItemSortOrder.values())
            {
                if (dataItemSortOrder.getOrdinal() == ordinal)
                {
                    return dataItemSortOrder;
                }
            }
        }

        return DataItemSortOrder.UNSORTED;
    }


    /**
     * Extract and delete the keyPattern property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return KeyPattern enum
     */
    KeyPattern removeKeyPattern(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeKeyPattern";

        if (instanceProperties != null)
        {
            int ordinal = repositoryHelper.removeEnumPropertyOrdinal(serviceName,
                                                                     OpenMetadataProperty.KEY_PATTERN.name,
                                                                     instanceProperties,
                                                                     methodName);

            for (KeyPattern keyPattern : KeyPattern.values())
            {
                if (keyPattern.getOpenTypeOrdinal() == ordinal)
                {
                    return keyPattern;
                }
            }
        }

        return KeyPattern.LOCAL_KEY;
    }


    /**
     * Extract and delete the permittedSynchronization property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return SynchronizationDirection enum
     */
    SynchronizationDirection removePermittedSynchronization(InstanceProperties  instanceProperties)
    {
        final String methodName = "removePermittedSynchronization";

        if (instanceProperties != null)
        {
            int ordinal = repositoryHelper.removeEnumPropertyOrdinal(serviceName,
                                                                     OpenMetadataProperty.PERMITTED_SYNCHRONIZATION.name,
                                                                     instanceProperties,
                                                                     methodName);

            for (SynchronizationDirection synchronizationDirection : SynchronizationDirection.values())
            {
                if (synchronizationDirection.getOpenTypeOrdinal() == ordinal)
                {
                    return synchronizationDirection;
                }
            }
        }

        return SynchronizationDirection.BOTH_DIRECTIONS;
    }


    /**
     * Extract and delete the portType property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return PortType enum
     */
    PortType removePortType(InstanceProperties  instanceProperties)
    {
        final String methodName = "removePortType";

        if (instanceProperties != null)
        {
            int ordinal = repositoryHelper.removeEnumPropertyOrdinal(serviceName,
                                                                     OpenMetadataType.PORT_TYPE_PROPERTY_NAME,
                                                                     instanceProperties,
                                                                     methodName);

            for (PortType portType : PortType.values())
            {
                if (portType.getOpenTypeOrdinal() == ordinal)
                {
                    return portType;
                }
            }
        }

        return PortType.OTHER;
    }
}
