/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.assetcatalog.converters;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementControlHeader;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementType;
import org.odpi.openmetadata.frameworks.openmetadata.converters.OpenMetadataConverterBase;
import org.odpi.openmetadata.frameworks.openmetadata.properties.AttachedClassification;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworkservices.omf.client.OpenMetadataClientBase;
import org.odpi.openmetadata.viewservices.assetcatalog.beans.Classification;
import org.odpi.openmetadata.viewservices.assetcatalog.beans.Element;
import org.odpi.openmetadata.viewservices.assetcatalog.beans.ElementOrigin;
import org.odpi.openmetadata.viewservices.assetcatalog.beans.Type;

import java.util.List;

/**
 * Provide base converter functions for the Asset Catalog OMVS.
 *
 * @param <B> bean class
 */
public abstract class AssetCatalogConverterBase<B> extends OpenMetadataConverterBase<B>
{
    /**
     * Constructor
     *
     * @param propertyHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public AssetCatalogConverterBase(PropertyHelper propertyHelper,
                                     String               serviceName,
                                     String               serverName)
    {
        super(propertyHelper, serviceName, serverName);
    }


    /**
     * Fill in the properties of an Asset Catalog element from the header of an open metadata
     * element.
     *
     * @param element asset catalog element
     * @param elementControlHeader open metadata element header
     */
    private void fillInElementControlHeader(Element              element,
                                            ElementControlHeader elementControlHeader)
    {
        element.setStatus(elementControlHeader.getStatus().getName());
        element.setOrigin(this.getOrigin(elementControlHeader));
        element.setType(this.getType(elementControlHeader.getType()));
        element.setVersion(elementControlHeader.getVersions().getVersion());
        element.setCreatedBy(elementControlHeader.getVersions().getCreatedBy());
        element.setCreateTime(elementControlHeader.getVersions().getCreateTime());
        element.setUpdatedBy(elementControlHeader.getVersions().getUpdatedBy());
        element.setUpdateTime(elementControlHeader.getVersions().getUpdateTime());
    }


    /**
     * Convert the open metadata type object into an asset catalog type object.
     *
     * @param openMetadataType open metadata type object
     * @return asset catalog type object
     */
    private Type getType(ElementType openMetadataType)
    {
        Type type = new Type();

        if (openMetadataType.getSuperTypeNames() != null)
        {
            type.setSuperType(openMetadataType.getSuperTypeNames().get(0));
        }
        type.setName(openMetadataType.getTypeName());
        type.setDescription(openMetadataType.getTypeDescription());
        type.setVersion(openMetadataType.getTypeVersion());

        return type;
    }


    /**
     * Convert the values from the open metadata origin to the asset catalog origin.
     *
     * @param elementControlHeader open metadata header
     * @return asset catalog origin
     */
    private ElementOrigin getOrigin(ElementControlHeader elementControlHeader)
    {
        ElementOrigin elementOrigin = new ElementOrigin();

        elementOrigin.setOriginCategory(elementControlHeader.getOrigin().getOriginCategory());
        elementOrigin.setInstanceLicense(elementControlHeader.getOrigin().getLicense());
        elementOrigin.setSourceServer(elementControlHeader.getOrigin().getSourceServer());
        elementOrigin.setMetadataCollectionId(elementControlHeader.getOrigin().getHomeMetadataCollectionId());
        elementOrigin.setMetadataCollectionName(elementControlHeader.getOrigin().getHomeMetadataCollectionName());

        return elementOrigin;
    }


    private Element getAnchorElement(List<AttachedClassification> classifications,
                                     String                 userId,
                                     OpenMetadataClientBase client)
    {
        if (classifications != null)
        {

        }

        return null;
    }


    private List<Classification> getClassifications(List<AttachedClassification> attachedClassifications)
    {
        // todo
        return null;
    }


    /**
     * Fill in the properties of an Asset Catalog element from the header of an open metadata
     * element.
     *
     * @param element asset catalog element
     * @param openMetadataElement open metadata element
     */
    private void fillInElement(Element                element,
                               OpenMetadataElement    openMetadataElement,
                               String                 userId,
                               OpenMetadataClientBase client)
    {
        final String methodName = "fillInElement";

        fillInElementControlHeader(element, openMetadataElement);

        element.setGuid(openMetadataElement.getElementGUID());
        element.setAnchorElement(getAnchorElement(openMetadataElement.getClassifications(), userId, client));
        element.setClassifications(getClassifications(openMetadataElement.getClassifications()));
        element.setName(propertyHelper.getStringProperty(serviceName,
                                                         OpenMetadataProperty.QUALIFIED_NAME.name,
                                                         openMetadataElement.getElementProperties(),
                                                         methodName));
        element.setOrigin(this.getOrigin(openMetadataElement));
        element.setType(this.getType(openMetadataElement.getType()));
        element.setAdditionalProperties(propertyHelper.getStringMapFromProperty(serviceName,
                                                                                OpenMetadataProperty.ADDITIONAL_PROPERTIES.name,
                                                                                openMetadataElement.getElementProperties(),
                                                                                methodName));
    }


    /**
     * Fill in the properties of an Asset Catalog element from the header of an open metadata
     * element.
     *
     * @param element asset catalog element
     * @param relatedMetadataElement open metadata element
     */
    private void fillInElement(Element                element,
                               RelatedMetadataElement relatedMetadataElement,
                               String                 userId,
                               OpenMetadataClientBase client)
    {
        fillInElement(element,
                      relatedMetadataElement.getElement(),
                      userId,
                      client);
    }
}
