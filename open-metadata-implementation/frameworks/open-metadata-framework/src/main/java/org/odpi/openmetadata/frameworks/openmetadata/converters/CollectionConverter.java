/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.converters;

import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.CollectionElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.AgreementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.DigitalProductProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.DigitalSubscriptionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.lang.reflect.InvocationTargetException;


/**
 * CollectionConverter generates a CollectionElement from a Collection entity
 */
public class CollectionConverter<B> extends AttributedElementConverterBase<B>
{
    /**
     * Constructor
     *
     * @param propertyHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public CollectionConverter(PropertyHelper propertyHelper,
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
     * @param openMetadataElement openMetadataElement containing the properties
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    @Override
    public B getNewBean(Class<B>            beanClass,
                        OpenMetadataElement openMetadataElement,
                        String              methodName) throws PropertyServerException
    {
        try
        {
            /*
             * This is initial confirmation that the generic converter has been initialized with an appropriate bean class.
             */
            B returnBean = beanClass.getDeclaredConstructor().newInstance();

            if (returnBean instanceof CollectionElement bean)
            {
                if (openMetadataElement != null)
                {
                    bean.setElementHeader(super.getMetadataElementHeader(beanClass, openMetadataElement, methodName));
                    bean.setProperties(this.getCollectionProperties(openMetadataElement));
                }
                else
                {
                    handleMissingMetadataInstance(beanClass.getName(), OpenMetadataElement.class.getName(), methodName);
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


    /**
     * Retrieve the collection properties from the retrieved element.
     *
     * @param primaryElement element
     *
     * @return dataStructure properties
     */
    protected CollectionProperties getCollectionProperties(OpenMetadataElement primaryElement)
    {
        if (primaryElement.getElementProperties() != null)
        {
            ElementProperties    elementProperties = new ElementProperties(primaryElement.getElementProperties());
            CollectionProperties collectionProperties;

            if (propertyHelper.isTypeOf(primaryElement, OpenMetadataType.DIGITAL_PRODUCT.typeName))
            {
                collectionProperties = new DigitalProductProperties();
            }
            else if (propertyHelper.isTypeOf(primaryElement, OpenMetadataType.AGREEMENT.typeName))
            {
                if (propertyHelper.isTypeOf(primaryElement, OpenMetadataType.DIGITAL_SUBSCRIPTION.typeName))
                {
                    collectionProperties = new DigitalSubscriptionProperties();
                }
                else
                {
                    collectionProperties = new AgreementProperties();
                }
            }
            else
            {
                collectionProperties = new CollectionProperties();
            }

            collectionProperties.setQualifiedName(this.removeQualifiedName(elementProperties));
            collectionProperties.setAdditionalProperties(this.removeAdditionalProperties(elementProperties));
            collectionProperties.setName(this.removeName(elementProperties));
            collectionProperties.setDescription(this.removeDescription(elementProperties));
            collectionProperties.setCollectionType(this.removeCollectionType(elementProperties));
            collectionProperties.setEffectiveFrom(primaryElement.getEffectiveFromTime());
            collectionProperties.setEffectiveTo(primaryElement.getEffectiveToTime());

            if (collectionProperties instanceof DigitalProductProperties digitalProductProperties)
            {
                digitalProductProperties.setUserDefinedStatus(this.removeUserDefinedStatus(elementProperties));
                digitalProductProperties.setProductName(this.removeProductName(elementProperties));
                digitalProductProperties.setIdentifier(this.removeIdentifier(elementProperties));
                digitalProductProperties.setProductType(this.removeProductType(elementProperties));
                digitalProductProperties.setIntroductionDate(this.removeIntroductionDate(elementProperties));
                digitalProductProperties.setMaturity(this.removeMaturity(elementProperties));
                digitalProductProperties.setServiceLife(this.removeServiceLife(elementProperties));
                digitalProductProperties.setCurrentVersion(this.removeCurrentVersion(elementProperties));
                digitalProductProperties.setNextVersionDate(this.removeNextVersionDate(elementProperties));
                digitalProductProperties.setWithdrawDate(this.removeWithdrawDate(elementProperties));
            }
            else if (collectionProperties instanceof AgreementProperties agreementProperties)
            {
                agreementProperties.setUserDefinedStatus(this.removeUserDefinedStatus(elementProperties));
                agreementProperties.setIdentifier(this.removeIdentifier(elementProperties));

                if (agreementProperties instanceof DigitalSubscriptionProperties digitalSubscriptionProperties)
                {
                    digitalSubscriptionProperties.setServiceLevels(this.removeServiceLevels(elementProperties));
                    digitalSubscriptionProperties.setSupportLevel(this.removeSupportLevel(elementProperties));
                }
            }

            /*
             * Any remaining properties are returned in the extended properties.  They are
             * assumed to be defined in a subtype.
             */
            collectionProperties.setTypeName(primaryElement.getType().getTypeName());
            collectionProperties.setExtendedProperties(this.getRemainingExtendedProperties(elementProperties));

            return collectionProperties;
        }

        return null;
    }
}
