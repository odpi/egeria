/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction.converters;

import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.governanceaction.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.search.ElementProperties;
import org.odpi.openmetadata.frameworks.governanceaction.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.informationsupplychains.InformationSupplyChainLinkProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.informationsupplychains.InformationSupplyChainSegmentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.ImplementedByProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionLinkingWireProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;


/**
 * InformationSupplyChainSegmentConverter generates an InformationSupplyChainSegmentElement from an "InformationSupplyChain" entity and a list of its
 * related segments.
 */
public class InformationSupplyChainSegmentConverter<B> extends OpenMetadataConverterBase<B>
{
    private final List<RelatedMetadataElementSummary>   solutionComponentPorts;
    private final List<SolutionLinkingWireRelationship> solutionLinkingWires;

    /**
     * Constructor
     *
     * @param propertyHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public InformationSupplyChainSegmentConverter(PropertyHelper                        propertyHelper,
                                                  String                                serviceName,
                                                  String                                serverName,
                                                  List<SolutionLinkingWireRelationship> solutionLinkingWire,
                                                  List<RelatedMetadataElementSummary>   solutionComponentPorts)
    {
        super(propertyHelper, serviceName, serverName);

        this.solutionComponentPorts = solutionComponentPorts;
        this.solutionLinkingWires   = solutionLinkingWire;
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

            if (returnBean instanceof InformationSupplyChainSegmentElement bean)
            {
                InformationSupplyChainSegmentProperties segmentProperties = new InformationSupplyChainSegmentProperties();

                bean.setElementHeader(super.getMetadataElementHeader(beanClass, primaryElement, methodName));
                bean.setSolutionComponentPorts(solutionComponentPorts);
                bean.setSolutionLinkingWires(solutionLinkingWires);

                ElementProperties elementProperties;

                /*
                 * The initial set of values come from the metadata element.
                 */
                if (primaryElement != null)
                {
                    elementProperties = new ElementProperties(primaryElement.getElementProperties());

                    segmentProperties.setQualifiedName(this.removeQualifiedName(elementProperties));
                    segmentProperties.setAdditionalProperties(this.removeAdditionalProperties(elementProperties));
                    segmentProperties.setDisplayName(this.removeDisplayName(elementProperties));
                    segmentProperties.setDescription(this.removeDescription(elementProperties));
                    segmentProperties.setScope(this.removeScope(elementProperties));
                    segmentProperties.setIntegrationStyle(this.removeIntegrationStyle(elementProperties));
                    segmentProperties.setEstimatedVolumetrics(this.removeEstimatedVolumetrics(elementProperties));
                    segmentProperties.setEffectiveFrom(primaryElement.getEffectiveFromTime());
                    segmentProperties.setEffectiveTo(primaryElement.getEffectiveToTime());

                    /*
                     * Any remaining properties are returned in the extended properties.  They are
                     * assumed to be defined in a subtype.
                     */
                    segmentProperties.setTypeName(bean.getElementHeader().getType().getTypeName());
                    segmentProperties.setExtendedProperties(this.getRemainingExtendedProperties(elementProperties));

                    bean.setProperties(segmentProperties);

                    if (relationships != null)
                    {
                        List<InformationSupplyChainLink>        links                  = new ArrayList<>(); // InformationSupplyChainLinks
                        List<ImplementedByRelationship>         implementedByList      = new ArrayList<>(); // ImplementedBy

                        ElementStub segmentStub = this.getElementStub(beanClass, primaryElement, methodName);

                        for (RelatedMetadataElement relatedMetadataElement : relationships)
                        {
                            if (relatedMetadataElement != null)
                            {
                                if (propertyHelper.isTypeOf(relatedMetadataElement, OpenMetadataType.INFORMATION_SUPPLY_CHAIN_LINK_RELATIONSHIP.typeName))
                                {
                                    links.add(this.getInformationSupplyChainLinkRelationship(beanClass, segmentStub, relatedMetadataElement));
                                }
                                else if (propertyHelper.isTypeOf(relatedMetadataElement, OpenMetadataType.IMPLEMENTED_BY_RELATIONSHIP.typeName))
                                {
                                    implementedByList.add(this.getImplementedByRelationship(beanClass, segmentStub, relatedMetadataElement));
                                }
                            }
                        }

                        if (! links.isEmpty())
                        {
                            bean.setLinks(links);
                        }
                        if (! implementedByList.isEmpty())
                        {
                            bean.setImplementedByList(implementedByList);
                        }
                        if (! solutionComponentPorts.isEmpty())
                        {
                            bean.setSolutionComponentPorts(solutionComponentPorts);
                        }
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


    /**
     * Return an ImplementedBy relationship
     *
     * @param beanClass this bean class
     * @param segmentStub stub for the segment
     * @param relatedMetadataElement relationship
     * @return bean
     * @throws PropertyServerException problem with converter
     */
    private ImplementedByRelationship getImplementedByRelationship(Class<B>               beanClass,
                                                                   ElementStub            segmentStub,
                                                                   RelatedMetadataElement relatedMetadataElement) throws PropertyServerException
    {
        final String methodName = "getImplementedByRelationship";

        ImplementedByRelationship relationship = new ImplementedByRelationship();

        relationship.setElementHeader(this.getMetadataElementHeader(beanClass,
                                                                    relatedMetadataElement,
                                                                    relatedMetadataElement.getRelationshipGUID(),
                                                                    null,
                                                                    methodName));

        ImplementedByProperties relationshipProperties = new ImplementedByProperties();

        ElementProperties elementProperties = new ElementProperties(relatedMetadataElement.getRelationshipProperties());

        relationshipProperties.setDesignStep(this.removeDesignStep(elementProperties));
        relationshipProperties.setRole(this.removeRole(elementProperties));
        relationshipProperties.setTransformation(this.removeTransformation(elementProperties));
        relationshipProperties.setDescription(this.removeDescription(elementProperties));
        relationshipProperties.setEffectiveFrom(relatedMetadataElement.getEffectiveFromTime());
        relationshipProperties.setEffectiveTo(relatedMetadataElement.getEffectiveToTime());
        relationshipProperties.setExtendedProperties(this.getRemainingExtendedProperties(elementProperties));

        relationship.setProperties(relationshipProperties);
        if (relatedMetadataElement.getElementAtEnd1())
        {
            relationship.setEnd1Element(this.getElementStub(beanClass, relatedMetadataElement.getElement(), methodName));
            relationship.setEnd2Element(segmentStub);
        }
        else
        {
            relationship.setEnd1Element(segmentStub);
            relationship.setEnd2Element(this.getElementStub(beanClass, relatedMetadataElement.getElement(), methodName));
        }

        return relationship;
    }


    /**
     * Return an InformationSupplyChainLink relationship
     *
     * @param beanClass this bean class
     * @param segmentStub stub for the segment
     * @param relatedMetadataElement relationship
     * @return bean
     * @throws PropertyServerException problem with converter
     */
    private InformationSupplyChainLink getInformationSupplyChainLinkRelationship(Class<B>               beanClass,
                                                                                 ElementStub            segmentStub,
                                                                                 RelatedMetadataElement relatedMetadataElement) throws PropertyServerException
    {
        final String methodName = "getInformationSupplyChainLinkRelationship";

        InformationSupplyChainLink relationship = new InformationSupplyChainLink();

        relationship.setElementHeader(this.getMetadataElementHeader(beanClass,
                                                                    relatedMetadataElement,
                                                                    relatedMetadataElement.getRelationshipGUID(),
                                                                    null,
                                                                    methodName));

        InformationSupplyChainLinkProperties relationshipProperties = new InformationSupplyChainLinkProperties();

        ElementProperties elementProperties = new ElementProperties(relatedMetadataElement.getRelationshipProperties());

        relationshipProperties.setDescription(this.removeDescription(elementProperties));
        relationshipProperties.setEffectiveFrom(relatedMetadataElement.getEffectiveFromTime());
        relationshipProperties.setEffectiveTo(relatedMetadataElement.getEffectiveToTime());
        relationshipProperties.setExtendedProperties(this.getRemainingExtendedProperties(elementProperties));

        relationship.setProperties(relationshipProperties);
        if (relatedMetadataElement.getElementAtEnd1())
        {
            relationship.setEnd1Element(this.getElementStub(beanClass, relatedMetadataElement.getElement(), methodName));
            relationship.setEnd2Element(segmentStub);
        }
        else
        {
            relationship.setEnd1Element(segmentStub);
            relationship.setEnd2Element(this.getElementStub(beanClass, relatedMetadataElement.getElement(), methodName));
        }

        return relationship;
    }


    /**
     * Return an SolutionLinkingWire relationship
     *
     * @param beanClass this bean class
     * @param segmentStub stub for the segment
     * @param relatedMetadataElement relationship
     * @return bean
     * @throws PropertyServerException problem with converter
     */
    private SolutionLinkingWireRelationship getSolutionLinkingWireRelationship(Class<B>               beanClass,
                                                                               ElementStub            segmentStub,
                                                                               RelatedMetadataElement relatedMetadataElement) throws PropertyServerException
    {
        final String methodName = "getISolutionLinkingWireRelationship";

        SolutionLinkingWireRelationship relationship = new SolutionLinkingWireRelationship();

        relationship.setElementHeader(this.getMetadataElementHeader(beanClass,
                                                                    relatedMetadataElement,
                                                                    relatedMetadataElement.getRelationshipGUID(),
                                                                    null,
                                                                    methodName));

        SolutionLinkingWireProperties relationshipProperties = new SolutionLinkingWireProperties();

        ElementProperties elementProperties = new ElementProperties(relatedMetadataElement.getRelationshipProperties());

        relationshipProperties.setInformationSupplyChainSegmentGUIDs(this.removeInformationSupplyChainSegmentGUIDs(elementProperties));
        relationshipProperties.setEffectiveFrom(relatedMetadataElement.getEffectiveFromTime());
        relationshipProperties.setEffectiveTo(relatedMetadataElement.getEffectiveToTime());
        relationshipProperties.setExtendedProperties(this.getRemainingExtendedProperties(elementProperties));

        relationship.setProperties(relationshipProperties);
        if (relatedMetadataElement.getElementAtEnd1())
        {
            relationship.setEnd1Element(this.getElementStub(beanClass, relatedMetadataElement.getElement(), methodName));
            relationship.setEnd2Element(segmentStub);
        }
        else
        {
            relationship.setEnd1Element(segmentStub);
            relationship.setEnd2Element(this.getElementStub(beanClass, relatedMetadataElement.getElement(), methodName));
        }

        return relationship;
    }
}
