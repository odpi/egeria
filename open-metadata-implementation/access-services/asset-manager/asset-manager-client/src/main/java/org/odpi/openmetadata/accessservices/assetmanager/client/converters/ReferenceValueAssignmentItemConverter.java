/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.client.converters;

import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.*;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.validvalues.ReferenceValueAssignmentProperties;

import java.lang.reflect.InvocationTargetException;


/**
 * ReferenceValueAssignmentItemConverter generates a ReferenceValueAssignmentItemElement bean from a RelatedMetadataElement.
 */
public class ReferenceValueAssignmentItemConverter<B> extends AssetManagerConverterBase<B>
{
    /**
     * Constructor
     *
     * @param propertyHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public ReferenceValueAssignmentItemConverter(PropertyHelper propertyHelper,
                                                 String         serviceName,
                                                 String         serverName)
    {
        super(propertyHelper, serviceName, serverName);
    }


    /**
     * Using the supplied openMetadataElement, return a new instance of the bean. This is used for most beans that have
     * a one to one correspondence with the repository instances.
     *
     * @param beanClass name of the class to create
     * @param relatedMetadataElement the properties of an open metadata element plus details of the relationship used to navigate to it
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    @Override
    public B getNewBean(Class<B>               beanClass,
                        RelatedMetadataElement relatedMetadataElement,
                        String                 methodName) throws PropertyServerException
    {
        try
        {
            /*
             * This is initial confirmation that the generic converter has been initialized with an appropriate bean class.
             */
            B returnBean = beanClass.getDeclaredConstructor().newInstance();

            if (returnBean instanceof ReferenceValueAssignmentItemElement bean)
            {
                bean.setRelationshipHeader(super.getMetadataElementHeader(beanClass,
                                                                          relatedMetadataElement,
                                                                          relatedMetadataElement.getRelationshipGUID(),
                                                                          null,
                                                                          methodName));

                ElementProperties elementProperties;

                /*
                 * The initial set of values come from the openMetadataElement.
                 */
                if (relatedMetadataElement.getRelationshipProperties() != null)
                {
                    ReferenceValueAssignmentProperties properties = new ReferenceValueAssignmentProperties();

                    elementProperties = new ElementProperties(relatedMetadataElement.getRelationshipProperties());

                    properties.setAttributeName(this.removeAttributeName(elementProperties));
                    properties.setConfidence(this.removeConfidence(elementProperties));
                    properties.setSteward(this.removeSteward(elementProperties));
                    properties.setStewardTypeName(this.removeStewardTypeName(elementProperties));
                    properties.setStewardPropertyName(this.removeStewardPropertyName(elementProperties));
                    properties.setNotes(this.removeNotes(elementProperties));
                    properties.setEffectiveFrom(relatedMetadataElement.getEffectiveFromTime());
                    properties.setEffectiveTo(relatedMetadataElement.getEffectiveToTime());

                    /*
                     * Any remaining properties are returned in the extended properties.  They are
                     * assumed to be defined in a subtype.
                     */
                    properties.setExtendedProperties(this.getRemainingExtendedProperties(elementProperties));

                    bean.setRelationshipProperties(properties);
                }
                else
                {
                    handleMissingMetadataInstance(beanClass.getName(), OpenMetadataElement.class.getName(), methodName);
                }

                if (relatedMetadataElement.getElement() != null)
                {
                    ReferenceableElement referenceableElement = new ReferenceableElement();

                    referenceableElement.setElementHeader(super.getMetadataElementHeader(beanClass,
                                                                                         relatedMetadataElement.getElement(),
                                                                                         relatedMetadataElement.getElement().getElementGUID(),
                                                                                         relatedMetadataElement.getElement().getClassifications(),
                                                                                         methodName));

                    ReferenceableProperties referenceableProperties = new ReferenceableProperties();

                    elementProperties = new ElementProperties(relatedMetadataElement.getElement().getElementProperties());

                    referenceableProperties.setQualifiedName(this.removeQualifiedName(elementProperties));
                    referenceableProperties.setAdditionalProperties(this.removeAdditionalProperties(elementProperties));
                    /*
                     * Any remaining properties are returned in the extended properties.  They are
                     * assumed to be defined in a subtype.
                     */
                    referenceableProperties.setExtendedProperties(this.getRemainingExtendedProperties(elementProperties));
                    referenceableProperties.setEffectiveFrom(relatedMetadataElement.getElement().getEffectiveFromTime());
                    referenceableProperties.setEffectiveTo(relatedMetadataElement.getElement().getEffectiveToTime());
                    referenceableProperties.setTypeName(relatedMetadataElement.getElement().getType().getTypeName());

                    referenceableElement.setReferenceableProperties(referenceableProperties);

                    bean.setAssignedItem(referenceableElement);
                }
            }

            return returnBean;
        }
        catch (IllegalAccessException | InstantiationException | ClassCastException | NoSuchMethodException | InvocationTargetException error)
        {
            super.handleInvalidBeanClass(beanClass.getName(), error, methodName);
        }

        return null;
    }
}
