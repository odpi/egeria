/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.converters;

import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.EndpointElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.EndpointProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;

import java.lang.reflect.InvocationTargetException;


/**
 * EndpointConverter generates a EndpointElement from a Endpoint entity
 */
public class EndpointConverter<B> extends SupplementaryPropertiesConverterBase<B>
{
    /**
     * Constructor
     *
     * @param propertyHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public EndpointConverter(PropertyHelper propertyHelper,
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

            if (returnBean instanceof EndpointElement bean)
            {
                EndpointProperties properties = new EndpointProperties();

                bean.setElementHeader(super.getMetadataElementHeader(beanClass, openMetadataElement, methodName));

                ElementProperties elementProperties;

                /*
                 * The initial set of values come from the openMetadataElement.
                 */
                if (openMetadataElement != null)
                {
                    elementProperties = new ElementProperties(openMetadataElement.getElementProperties());

                    properties.setQualifiedName(this.removeQualifiedName(elementProperties));
                    properties.setAdditionalProperties(this.removeAdditionalProperties(elementProperties));
                    properties.setName(this.removeName(elementProperties));
                    properties.setResourceDescription(this.removeDescription(elementProperties));
                    properties.setAddress(this.removeNetworkAddress(elementProperties));
                    properties.setProtocol(this.removeProtocol(elementProperties));
                    properties.setEncryptionMethod(this.removeEncryptionMethod(elementProperties));
                    properties.setEffectiveFrom(openMetadataElement.getEffectiveFromTime());
                    properties.setEffectiveTo(openMetadataElement.getEffectiveToTime());

                    /*
                     * Any remaining properties are returned in the extended properties.  They are
                     * assumed to be defined in a subtype.
                     */
                    properties.setTypeName(bean.getElementHeader().getType().getTypeName());
                    properties.setExtendedProperties(this.getRemainingExtendedProperties(elementProperties));
                }
                else
                {
                    handleMissingMetadataInstance(beanClass.getName(), OpenMetadataElement.class.getName(), methodName);
                }

                bean.setProperties(properties);
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
