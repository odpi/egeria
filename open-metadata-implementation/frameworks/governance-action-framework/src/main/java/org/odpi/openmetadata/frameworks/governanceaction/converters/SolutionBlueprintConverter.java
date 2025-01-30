/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction.converters;

import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.governanceaction.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.search.ElementProperties;
import org.odpi.openmetadata.frameworks.governanceaction.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionBlueprintProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionComponentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionLinkingWireProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;


/**
 * SolutionBlueprintConverter generates a SolutionBlueprintElement from an "SolutionBlueprint" entity and a list of its
 * related solution components.
 */
public class SolutionBlueprintConverter<B> extends OpenMetadataConverterBase<B>
{
    private final List<SolutionBlueprintComponent> solutionComponentElements; // SolutionComposition
    /**
     * Constructor
     *
     * @param propertyHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public SolutionBlueprintConverter(PropertyHelper                   propertyHelper,
                                      String                           serviceName,
                                      String                           serverName,
                                      List<SolutionBlueprintComponent> solutionComponentElements)
    {
        super(propertyHelper, serviceName, serverName);

        this.solutionComponentElements = solutionComponentElements;
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
    public B getNewBean(Class<B>            beanClass,
                        OpenMetadataElement primaryElement,
                        String              methodName) throws PropertyServerException

    {
        try
        {
            /*
             * This is initial confirmation that the generic converter has been initialized with an appropriate bean class.
             */
            B returnBean = beanClass.getDeclaredConstructor().newInstance();

            if (returnBean instanceof SolutionBlueprintElement bean)
            {
                SolutionBlueprintProperties solutionBlueprintProperties = new SolutionBlueprintProperties();

                bean.setElementHeader(super.getMetadataElementHeader(beanClass, primaryElement, methodName));
                bean.setSolutionComponents(solutionComponentElements);

                ElementProperties elementProperties;

                /*
                 * The initial set of values come from the metadata element.
                 */
                if (primaryElement != null)
                {
                    elementProperties = new ElementProperties(primaryElement.getElementProperties());

                    solutionBlueprintProperties.setQualifiedName(this.removeQualifiedName(elementProperties));
                    solutionBlueprintProperties.setAdditionalProperties(this.removeAdditionalProperties(elementProperties));
                    solutionBlueprintProperties.setDisplayName(this.removeDisplayName(elementProperties));
                    solutionBlueprintProperties.setDescription(this.removeDescription(elementProperties));
                    solutionBlueprintProperties.setVersion(this.removeVersionIdentifier(elementProperties));
                    solutionBlueprintProperties.setEffectiveFrom(primaryElement.getEffectiveFromTime());
                    solutionBlueprintProperties.setEffectiveTo(primaryElement.getEffectiveToTime());

                    /*
                     * Any remaining properties are returned in the extended properties.  They are
                     * assumed to be defined in a subtype.
                     */
                    solutionBlueprintProperties.setTypeName(bean.getElementHeader().getType().getTypeName());
                    solutionBlueprintProperties.setExtendedProperties(this.getRemainingExtendedProperties(elementProperties));

                    bean.setProperties(solutionBlueprintProperties);
                }
                else
                {
                    handleMissingMetadataInstance(beanClass.getName(), OpenMetadataElement.class.getName(), methodName);
                }

                bean.setProperties(solutionBlueprintProperties);
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
