/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.converters;


import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.mermaid.GovernanceDefinitionMermaidGraphBuilder;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.GovernanceDefinitionGraph;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.GovernanceDefinitionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;


/**
 * GovernanceDefinitionConverter provides common methods for transferring relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into a bean that inherits from GovernanceDefinitionGraph.
 */
public class GovernanceDefinitionGraphConverter<B> extends GovernanceDefinitionConverter<B>
{
    /**
     * Constructor
     *
     * @param propertyHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public GovernanceDefinitionGraphConverter(PropertyHelper propertyHelper,
                                              String         serviceName,
                                              String         serverName)
    {
        super(propertyHelper, serviceName, serverName);
    }


    /**
     * Using the supplied instances, return a new instance of the bean.  It is used for beans such as
     * an Annotation or To Do bean which combine knowledge from the element and its linked relationships.
     *
     * @param beanClass name of the class to create
     * @param primaryElement element that is the root of the collection of entities that make up the
     *                      content of the bean
     * @param relationships relationships linking the entities
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    @SuppressWarnings(value = "unused")
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

            if (returnBean instanceof GovernanceDefinitionGraph bean)
            {
                GovernanceDefinitionProperties governanceDefinitionProperties;

                bean.setElementHeader(super.getMetadataElementHeader(beanClass, primaryElement, methodName));

                ElementProperties elementProperties;

                if (primaryElement != null)
                {
                    bean.setElementHeader(this.getMetadataElementHeader(beanClass, primaryElement, methodName));
                    bean.setProperties(getGovernanceDefinitionProperties(beanClass, primaryElement, methodName));

                    if (relationships != null)
                    {
                        List<RelatedMetadataElementSummary> parents            = new ArrayList<>();
                        List<RelatedMetadataElementSummary> peers              = new ArrayList<>();
                        List<RelatedMetadataElementSummary> children           = new ArrayList<>();
                        List<RelatedMetadataElementSummary> metrics            = new ArrayList<>();
                        List<RelatedMetadataElementSummary> externalReferences = new ArrayList<>();
                        List<RelatedMetadataElementSummary> others             = new ArrayList<>();

                        for (RelatedMetadataElement relationship : relationships)
                        {
                            if (relationship != null)
                            {
                                RelatedMetadataElementSummary element  = super.getRelatedElementSummary(beanClass, relationship, methodName);

                                if (propertyHelper.isTypeOf(relationship, OpenMetadataType.EXTERNAL_REFERENCE_LINK_RELATIONSHIP.typeName))
                                {
                                    externalReferences.add(element);
                                }
                                else if (propertyHelper.isTypeOf(relationship, OpenMetadataType.GOVERNANCE_DEFINITION_METRIC_RELATIONSHIP.typeName))
                                {
                                    metrics.add(element);
                                }
                                else if ((propertyHelper.isTypeOf(relationship, OpenMetadataType.GOVERNANCE_DRIVER_LINK_RELATIONSHIP.typeName)) ||
                                         (propertyHelper.isTypeOf(relationship, OpenMetadataType.GOVERNANCE_POLICY_LINK_RELATIONSHIP.typeName)) ||
                                         (propertyHelper.isTypeOf(relationship, OpenMetadataType.GOVERNANCE_CONTROL_LINK_RELATIONSHIP.typeName)))
                                {

                                    peers.add(element);
                                }
                                else if ((propertyHelper.isTypeOf(relationship, OpenMetadataType.GOVERNANCE_RESPONSE_RELATIONSHIP.typeName)) ||
                                         (propertyHelper.isTypeOf(relationship, OpenMetadataType.GOVERNANCE_IMPLEMENTATION_RELATIONSHIP.typeName)))
                                {
                                    if (relationship.getElementAtEnd1())
                                    {
                                        parents.add(element);
                                    }
                                    else
                                    {
                                        children.add(element);
                                    }
                                }
                                else
                                {
                                    others.add(element);
                                }
                            }
                        }

                        if (! parents.isEmpty())
                        {
                            bean.setParents(parents);
                        }

                        if (! peers.isEmpty())
                        {
                            bean.setPeers(peers);
                        }

                        if (! children.isEmpty())
                        {
                            bean.setChildren(children);
                        }

                        if (! metrics.isEmpty())
                        {
                            bean.setMetrics(metrics);
                        }

                        if (! externalReferences.isEmpty())
                        {
                            bean.setExternalReferences(externalReferences);
                        }

                        if (! others.isEmpty())
                        {
                            bean.setOthers(others);
                        }

                        GovernanceDefinitionMermaidGraphBuilder graphBuilder = new GovernanceDefinitionMermaidGraphBuilder(bean);

                        bean.setMermaidGraph(graphBuilder.getMermaidGraph());
                    }
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
}
