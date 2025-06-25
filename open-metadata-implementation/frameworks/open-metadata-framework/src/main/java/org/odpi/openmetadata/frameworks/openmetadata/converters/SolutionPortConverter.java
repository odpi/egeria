/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.converters;

import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementStub;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.SolutionPortElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionPortProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;


/**
 * SolutionPortConverter generates a SolutionPortElement from an "SolutionPort" entity and a list of its
 * related elements
 */
public class SolutionPortConverter<B> extends OpenMetadataConverterBase<B>
{
    /**
     * Constructor
     *
     * @param propertyHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public SolutionPortConverter(PropertyHelper propertyHelper,
                                 String         serviceName,
                                 String         serverName)
    {
        super(propertyHelper, serviceName, serverName);
    }


    /**
     * Using the supplied instances, return a new instance of the bean.
     *
     * @param beanClass name of the class to create
     * @param primaryElement element that is the root of the collection of entities that make up the
     *                      content of the bean
     * @param relationships relationships linking the entities
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    @Override
    public B getNewComplexBean(Class<B>                     beanClass,
                               OpenMetadataElement          primaryElement,
                               List<RelatedMetadataElement> relationships,
                               String                       methodName) throws PropertyServerException

    {
        try
        {
            /*
             * This is initial confirmation that the generic converter has been initialized with an appropriate bean class.
             */
            B returnBean = beanClass.getDeclaredConstructor().newInstance();

            if (returnBean instanceof SolutionPortElement bean)
            {
                SolutionPortProperties portProperties = new SolutionPortProperties();

                bean.setElementHeader(super.getMetadataElementHeader(beanClass, primaryElement, methodName));

                ElementProperties elementProperties;

                /*
                 * The initial set of values come from the metadata element.
                 */
                if (primaryElement != null)
                {
                    elementProperties = new ElementProperties(primaryElement.getElementProperties());

                    portProperties.setQualifiedName(this.removeQualifiedName(elementProperties));
                    portProperties.setAdditionalProperties(this.removeAdditionalProperties(elementProperties));
                    portProperties.setDisplayName(this.removeDisplayName(elementProperties));
                    portProperties.setDescription(this.removeDescription(elementProperties));
                    portProperties.setVersionIdentifier(this.removeVersionIdentifier(elementProperties));
                    portProperties.setSolutionPortDirection(this.removeSolutionPortDirection(elementProperties));
                    portProperties.setUserDefinedStatus(this.removeUserDefinedStatus(elementProperties));
                    portProperties.setEffectiveFrom(primaryElement.getEffectiveFromTime());
                    portProperties.setEffectiveTo(primaryElement.getEffectiveToTime());

                    /*
                     * Any remaining properties are returned in the extended properties.  They are
                     * assumed to be defined in a subtype.
                     */
                    portProperties.setTypeName(bean.getElementHeader().getType().getTypeName());
                    portProperties.setExtendedProperties(this.getRemainingExtendedProperties(elementProperties));

                    bean.setProperties(portProperties);

                    if (relationships != null)
                    {
                        List<ElementStub> delegationPorts = new ArrayList<>();

                        for (RelatedMetadataElement relatedMetadataElement : relationships)
                        {
                            if (relatedMetadataElement != null)
                            {
                                if (propertyHelper.isTypeOf(relatedMetadataElement, OpenMetadataType.SOLUTION_PORT_SCHEMA_RELATIONSHIP.typeName))
                                {
                                    bean.setSchemaType(super.getElementStub(beanClass, relatedMetadataElement.getElement(), methodName));
                                }
                                else if (propertyHelper.isTypeOf(relatedMetadataElement, OpenMetadataType.SOLUTION_PORT_DELEGATION_RELATIONSHIP.typeName))
                                {
                                    delegationPorts.add(super.getElementStub(beanClass, relatedMetadataElement.getElement(), methodName));
                                }
                            }
                        }

                        if (! delegationPorts.isEmpty())
                        {
                            bean.setDelegationPorts(delegationPorts);
                        }
                    }
                }
                else
                {
                    handleMissingMetadataInstance(beanClass.getName(), OpenMetadataElement.class.getName(), methodName);
                }

                bean.setProperties(portProperties);
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
