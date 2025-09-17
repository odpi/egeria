/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.converters;

import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionComponentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionLinkingWireProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;


/**
 * SolutionComponentConverter generates a SolutionComponentElement from an "SolutionComponent" entity and a list of its
 * related elements
 */
public class SolutionComponentConverter<B> extends OpenMetadataConverterBase<B>
{
    private final List<SolutionComponentElement> subComponents; // SolutionComposition
    private final List<SolutionPortElement>      ports;
    /**
     * Constructor
     *
     * @param propertyHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public SolutionComponentConverter(PropertyHelper                 propertyHelper,
                                      String                         serviceName,
                                      String                         serverName,
                                      List<SolutionComponentElement> subComponents,
                                      List<SolutionPortElement>      ports)
    {
        super(propertyHelper, serviceName, serverName);

        this.subComponents = subComponents;
        this.ports = ports;
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

            if (returnBean instanceof SolutionComponentElement bean)
            {
                SolutionComponentProperties solutionComponentProperties = new SolutionComponentProperties();

                bean.setElementHeader(super.getMetadataElementHeader(beanClass, primaryElement, methodName));
                bean.setSubComponents(subComponents);
                bean.setPorts(ports);

                ElementProperties elementProperties;

                /*
                 * The initial set of values come from the metadata element.
                 */
                if (primaryElement != null)
                {
                    elementProperties = new ElementProperties(primaryElement.getElementProperties());

                    solutionComponentProperties.setQualifiedName(this.removeQualifiedName(elementProperties));
                    solutionComponentProperties.setAdditionalProperties(this.removeAdditionalProperties(elementProperties));
                    solutionComponentProperties.setDisplayName(this.removeDisplayName(elementProperties));
                    solutionComponentProperties.setDescription(this.removeDescription(elementProperties));
                    solutionComponentProperties.setVersionIdentifier(this.removeVersionIdentifier(elementProperties));
                    solutionComponentProperties.setSolutionComponentType(this.removeSolutionComponentType(elementProperties));
                    solutionComponentProperties.setPlannedDeployedImplementationType(this.removePlannedDeployedImplementationType(elementProperties));
                    solutionComponentProperties.setUserDefinedStatus(this.removeUserDefinedStatus(elementProperties));
                    solutionComponentProperties.setEffectiveFrom(primaryElement.getEffectiveFromTime());
                    solutionComponentProperties.setEffectiveTo(primaryElement.getEffectiveToTime());

                    /*
                     * Any remaining properties are returned in the extended properties.  They are
                     * assumed to be defined in a subtype.
                     */
                    solutionComponentProperties.setTypeName(bean.getElementHeader().getType().getTypeName());
                    solutionComponentProperties.setExtendedProperties(this.getRemainingExtendedProperties(elementProperties));

                    bean.setProperties(solutionComponentProperties);

                    if (relationships != null)
                    {
                        List<WiredSolutionComponent>        wiredFrom       = new ArrayList<>();
                        List<WiredSolutionComponent>        wiredTo         = new ArrayList<>();
                        List<RelatedMetadataElementSummary> actors          = new ArrayList<>();
                        List<RelatedMetadataElementSummary> blueprints      = new ArrayList<>();
                        List<RelatedMetadataElementSummary> implementations = new ArrayList<>();
                        List<RelatedMetadataElementSummary> otherElements   = new ArrayList<>();

                        for (RelatedMetadataElement relatedMetadataElement : relationships)
                        {
                            if (relatedMetadataElement != null)
                            {
                                if (propertyHelper.isTypeOf(relatedMetadataElement, OpenMetadataType.SOLUTION_COMPONENT_ACTOR_RELATIONSHIP.typeName))
                                {
                                    actors.add(super.getRelatedElementSummary(relatedMetadataElement));
                                }
                                else if (propertyHelper.isTypeOf(relatedMetadataElement, OpenMetadataType.SOLUTION_BLUEPRINT_COMPOSITION_RELATIONSHIP.typeName))
                                {
                                    blueprints.add(super.getRelatedElementSummary(relatedMetadataElement));
                                }
                                else if (propertyHelper.isTypeOf(relatedMetadataElement, OpenMetadataType.IMPLEMENTED_BY_RELATIONSHIP.typeName))
                                {
                                    implementations.add(super.getRelatedElementSummary(relatedMetadataElement));
                                }
                                else if (propertyHelper.isTypeOf(relatedMetadataElement, OpenMetadataType.SOLUTION_LINKING_WIRE_RELATIONSHIP.typeName))
                                {
                                    WiredSolutionComponent        link                          = new WiredSolutionComponent();
                                    SolutionLinkingWireProperties solutionLinkingWireProperties = new SolutionLinkingWireProperties();

                                    link.setElementHeader(super.getMetadataElementHeader(beanClass,
                                                                                         relatedMetadataElement,
                                                                                         relatedMetadataElement.getRelationshipGUID(),
                                                                                         null,
                                                                                         methodName));

                                    elementProperties = new ElementProperties(relatedMetadataElement.getRelationshipProperties());

                                    solutionLinkingWireProperties.setLabel(this.removeLabel(elementProperties));
                                    solutionLinkingWireProperties.setDescription(this.removeDescription(elementProperties));
                                    solutionLinkingWireProperties.setISCQualifiedNames(this.removeISCQualifiedNames(elementProperties));
                                    solutionLinkingWireProperties.setEffectiveFrom(primaryElement.getEffectiveFromTime());
                                    solutionLinkingWireProperties.setEffectiveTo(primaryElement.getEffectiveToTime());
                                    solutionLinkingWireProperties.setExtendedProperties(this.getRemainingExtendedProperties(elementProperties));

                                    link.setProperties(solutionLinkingWireProperties);

                                    link.setLinkedElement(super.getElementSummary(beanClass, relatedMetadataElement.getElement(), methodName));

                                    if (relatedMetadataElement.getElementAtEnd1())
                                    {
                                        wiredTo.add(link);
                                    }
                                    else
                                    {
                                        wiredFrom.add(link);
                                    }
                                }
                                else
                                {
                                    otherElements.add(super.getRelatedElementSummary(relatedMetadataElement));
                                }
                            }
                        }

                        if (! wiredFrom.isEmpty())
                        {
                            bean.setWiredFromLinks(wiredFrom);
                        }

                        if (! wiredTo.isEmpty())
                        {
                            bean.setWiredToLinks(wiredTo);
                        }

                        if (! actors.isEmpty())
                        {
                            bean.setActors(actors);
                        }

                        if (! blueprints.isEmpty())
                        {
                            bean.setBlueprints(blueprints);
                        }

                        if (! implementations.isEmpty())
                        {
                            bean.setImplementations(implementations);
                        }

                        if (! otherElements.isEmpty())
                        {
                            bean.setOtherElements(otherElements);
                        }
                    }
                }
                else
                {
                    handleMissingMetadataInstance(beanClass.getName(), OpenMetadataElement.class.getName(), methodName);
                }

                bean.setProperties(solutionComponentProperties);
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
