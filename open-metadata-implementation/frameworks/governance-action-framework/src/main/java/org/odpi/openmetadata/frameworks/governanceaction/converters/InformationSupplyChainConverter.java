/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction.converters;

import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.governanceaction.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.search.ElementProperties;
import org.odpi.openmetadata.frameworks.governanceaction.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.InformationSupplyChainElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.InformationSupplyChainSegmentElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.informationsupplychains.InformationSupplyChainProperties;

import java.lang.reflect.InvocationTargetException;
import java.util.List;


/**
 * InformationSupplyChainConverter generates an InformationSupplyChainElement from an "InformationSupplyChain" entity and a list of its
 * related segments.
 */
public class InformationSupplyChainConverter<B> extends OpenMetadataConverterBase<B>
{
    private final List<InformationSupplyChainSegmentElement> segments; // SolutionComposition
    /**
     * Constructor
     *
     * @param propertyHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public InformationSupplyChainConverter(PropertyHelper                             propertyHelper,
                                           String                                     serviceName,
                                           String                                     serverName,
                                           List<InformationSupplyChainSegmentElement> segments)
    {
        super(propertyHelper, serviceName, serverName);

        this.segments = segments;
    }


    /**
     * Using the supplied instances, return a new instance of the bean.
     *
     * @param beanClass name of the class to create
     * @param primaryElement element that is the root of the collection of entities that make up the
     *                      content of the bean
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    @Override
    public B getNewBean(Class<B>                     beanClass,
                        OpenMetadataElement          primaryElement,
                        String                       methodName) throws PropertyServerException

    {
        try
        {
            /*
             * This is initial confirmation that the generic converter has been initialized with an appropriate bean class.
             */
            B returnBean = beanClass.getDeclaredConstructor().newInstance();

            if (returnBean instanceof InformationSupplyChainElement bean)
            {
                InformationSupplyChainProperties informationSupplyChainProperties = new InformationSupplyChainProperties();

                bean.setElementHeader(super.getMetadataElementHeader(beanClass, primaryElement, methodName));
                bean.setSegments(segments);

                ElementProperties elementProperties;

                /*
                 * The initial set of values come from the metadata element.
                 */
                if (primaryElement != null)
                {
                    elementProperties = new ElementProperties(primaryElement.getElementProperties());

                    informationSupplyChainProperties.setQualifiedName(this.removeQualifiedName(elementProperties));
                    informationSupplyChainProperties.setAdditionalProperties(this.removeAdditionalProperties(elementProperties));
                    informationSupplyChainProperties.setDisplayName(this.removeDisplayName(elementProperties));
                    informationSupplyChainProperties.setDescription(this.removeDescription(elementProperties));
                    informationSupplyChainProperties.setScope(this.removeScope(elementProperties));
                    informationSupplyChainProperties.setPurposes(this.removePurposes(elementProperties));
                    informationSupplyChainProperties.setEffectiveFrom(primaryElement.getEffectiveFromTime());
                    informationSupplyChainProperties.setEffectiveTo(primaryElement.getEffectiveToTime());

                    /*
                     * Any remaining properties are returned in the extended properties.  They are
                     * assumed to be defined in a subtype.
                     */
                    informationSupplyChainProperties.setTypeName(bean.getElementHeader().getType().getTypeName());
                    informationSupplyChainProperties.setExtendedProperties(this.getRemainingExtendedProperties(elementProperties));

                    bean.setProperties(informationSupplyChainProperties);
                }
                else
                {
                    handleMissingMetadataInstance(beanClass.getName(), OpenMetadataElement.class.getName(), methodName);
                }

                bean.setProperties(informationSupplyChainProperties);
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
