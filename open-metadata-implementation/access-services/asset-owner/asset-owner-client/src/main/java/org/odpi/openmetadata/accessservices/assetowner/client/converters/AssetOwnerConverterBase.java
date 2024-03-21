/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.client.converters;

import org.odpi.openmetadata.accessservices.assetowner.metadataelements.RelatedElement;
import org.odpi.openmetadata.accessservices.assetowner.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementStub;
import org.odpi.openmetadata.frameworks.governanceaction.converters.OpenMetadataConverterBase;
import org.odpi.openmetadata.frameworks.governanceaction.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RelatedMetadataElements;
import org.odpi.openmetadata.frameworks.governanceaction.search.ElementProperties;
import org.odpi.openmetadata.frameworks.governanceaction.search.PropertyHelper;

/**
 * Base class converter for Asset Owner OMAS.
 *
 * @param <B> bean class
 */
public abstract class AssetOwnerConverterBase<B> extends OpenMetadataConverterBase<B>
{
    /**
     * Constructor
     *
     * @param propertyHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public AssetOwnerConverterBase(PropertyHelper propertyHelper,
                                   String               serviceName,
                                   String               serverName)
    {
        super(propertyHelper, serviceName, serverName);
    }


    /**
     * Using the supplied instances, return a new instance of a relatedElement bean. This is used for beans that
     * contain a combination of the properties from an entity and that of a connected relationship.
     *
     * @param beanClass name of the class to create
     * @param element entity containing the properties
     * @param relationship relationship containing the properties
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    public RelatedElement getRelatedElement(Class<B>                beanClass,
                                            OpenMetadataElement     element,
                                            RelatedMetadataElements relationship,
                                            String                  methodName) throws PropertyServerException
    {
        RelatedElement  relatedElement = new RelatedElement();

        relatedElement.setRelationshipHeader(this.getMetadataElementHeader(beanClass, relationship, relationship.getRelationshipGUID(), null, methodName));

        if (relationship != null)
        {
            ElementProperties instanceProperties = new ElementProperties(relationship.getRelationshipProperties());

            RelationshipProperties relationshipProperties = new RelationshipProperties();

            relationshipProperties.setEffectiveFrom(relationship.getEffectiveFromTime());
            relationshipProperties.setEffectiveTo(relationship.getEffectiveToTime());
            relationshipProperties.setExtendedProperties(this.getRemainingExtendedProperties(instanceProperties));

            relatedElement.setRelationshipProperties(relationshipProperties);
        }
        else
        {
            handleMissingMetadataInstance(beanClass.getName(), RelatedMetadataElements.class.getName(), methodName);
        }

        if (element != null)
        {
            ElementStub elementStub = this.getElementStub(beanClass, element, methodName);

            relatedElement.setRelatedElement(elementStub);
        }
        else
        {
            handleMissingMetadataInstance(beanClass.getName(), OpenMetadataElement.class.getName(), methodName);
        }

        return relatedElement;
    }


    /**
     * Using the supplied instances, return a new instance of a relatedElement bean. This is used for beans that
     * contain a combination of the properties from an entity and that of a connected relationship.
     *
     * @param beanClass name of the class to create
     * @param relatedMetadataElement results containing the properties
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    public RelatedElement getRelatedElement(Class<B>               beanClass,
                                            RelatedMetadataElement relatedMetadataElement,
                                            String                 methodName) throws PropertyServerException
    {
        RelatedElement  relatedElement = new RelatedElement();

        relatedElement.setRelationshipHeader(this.getMetadataElementHeader(beanClass, relatedMetadataElement, relatedMetadataElement.getRelationshipGUID(), null, methodName));

        if (relatedMetadataElement != null)
        {
            ElementProperties instanceProperties = new ElementProperties(relatedMetadataElement.getRelationshipProperties());

            RelationshipProperties relationshipProperties = new RelationshipProperties();

            relationshipProperties.setEffectiveFrom(relatedMetadataElement.getEffectiveFromTime());
            relationshipProperties.setEffectiveTo(relatedMetadataElement.getEffectiveToTime());
            relationshipProperties.setExtendedProperties(this.getRemainingExtendedProperties(instanceProperties));

            relatedElement.setRelationshipProperties(relationshipProperties);
        }
        else
        {
            handleMissingMetadataInstance(beanClass.getName(), RelatedMetadataElements.class.getName(), methodName);
        }

        return relatedElement;
    }
}
