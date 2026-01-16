/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.converters;

import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.reports.ConnectorActivityReport;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.reports.ConnectorActivityReportProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;

import java.lang.reflect.InvocationTargetException;


/**
 * ConnectorActivityReportConverter provides common methods for transferring relevant properties from an
 * Open Metadata Element object into an Integration Report bean.
 */
public class ConnectorActivityReportConverter<B> extends OpenMetadataConverterBase<B>
{
    /**
     * Constructor
     *
     * @param propertyHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public ConnectorActivityReportConverter(PropertyHelper propertyHelper,
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
     * @throws PropertyServerException a problem instantiating the bean
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

            if (returnBean instanceof ConnectorActivityReport bean)
            {
                ConnectorActivityReportProperties properties = new ConnectorActivityReportProperties();

                bean.setElementHeader(super.getMetadataElementHeader(beanClass, openMetadataElement, methodName));

                ElementProperties elementProperties;

                if (openMetadataElement != null)
                {
                    elementProperties = new ElementProperties(openMetadataElement.getElementProperties());

                    properties.setServerName(this.removeServerName(elementProperties));
                    properties.setConnectorId(this.removeConnectorId(elementProperties));
                    properties.setConnectorName(this.removeConnectorName(elementProperties));
                    properties.setStartTime(this.removeStartTime(elementProperties));
                    properties.setCompletionTime(this.removeCompletionTime(elementProperties));
                    properties.setCreatedElements(this.removeCreatedElements(elementProperties));
                    properties.setUpdatedElements(this.removeUpdatedElements(elementProperties));
                    properties.setDeletedElements(this.removeDeletedElements(elementProperties));
                    properties.setAdditionalProperties(this.removeAdditionalProperties(elementProperties));
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
