/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.converters;

import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.InformationSupplyChainComponent;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.InformationSupplyChainElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.InformationSupplyChainSegment;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRelationship;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.informationsupplychains.InformationSupplyChainProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;


/**
 * InformationSupplyChainConverter generates an InformationSupplyChainElement from an "InformationSupplyChain" entity and a list of its
 * related segments.
 */
public class InformationSupplyChainConverter<B> extends OpenMetadataConverterBase<B>
{
    private final List<InformationSupplyChainSegment>   segments;       // InformationSupplyChainComposition (end 2)
    private final List<InformationSupplyChainComponent> implementedBy;  // ImplementedBy relationship and nested elements/wires

    private final List<OpenMetadataRelationship>        implementation; // Relationships with the ISC's qualifiedName in properties

    /**
     * Constructor
     *
     * @param propertyHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     * @param segments segments for this element
     * @param implementedBy components linked via ImplementedBy relationship
     * @param implementation components with lineage relationships identifying this information supply chain
     */
    public InformationSupplyChainConverter(PropertyHelper                             propertyHelper,
                                           String                                     serviceName,
                                           String                                     serverName,
                                           List<InformationSupplyChainSegment>        segments,
                                           List<InformationSupplyChainComponent>      implementedBy,
                                           List<OpenMetadataRelationship>             implementation)
    {
        super(propertyHelper, serviceName, serverName);

        this.segments = segments;
        this.implementedBy = implementedBy;
        this.implementation = implementation;
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
                bean.setImplementedByList(implementedBy);
                bean.setImplementation(super.getRelationshipElements(beanClass, implementation, methodName));

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
                    informationSupplyChainProperties.setIntegrationStyle(this.removeIntegrationStyle(elementProperties));
                    informationSupplyChainProperties.setEstimatedVolumetrics(this.removeEstimatedVolumetrics(elementProperties));
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
            B returnBean = this.getNewBean(beanClass, primaryElement, methodName);

            if (returnBean instanceof InformationSupplyChainElement bean)
            {
                if (relationships != null)
                {
                    /*
                     * All segments should have already been removed - but if a mistake has been made, the segments will appear as parents rather than be ignored.
                     * Similarly, there should not be any ImplementedBy relationships.  If there are, they will appear in other related elements.
                     */
                    bean.setParents(super.getRelatedElements(beanClass,  OpenMetadataType.INFORMATION_SUPPLY_CHAIN_COMPOSITION_RELATIONSHIP.typeName, relationships));
                    bean.setLinks(super.getRelatedElements(beanClass,  OpenMetadataType.INFORMATION_SUPPLY_CHAIN_LINK_RELATIONSHIP.typeName, relationships));
                    bean.setExternalReferences(super.getRelatedElements(beanClass,  OpenMetadataType.EXTERNAL_REFERENCE_LINK_RELATIONSHIP.typeName, relationships));
                    bean.setOtherRelatedElements(super.getOtherRelatedElements(beanClass,
                                                                               relationships,
                                                                               Arrays.asList(OpenMetadataType.INFORMATION_SUPPLY_CHAIN_COMPOSITION_RELATIONSHIP.typeName,
                                                                                             OpenMetadataType.INFORMATION_SUPPLY_CHAIN_LINK_RELATIONSHIP.typeName,
                                                                                             OpenMetadataType.EXTERNAL_REFERENCE_LINK_RELATIONSHIP.typeName)));
                }
            }

            return returnBean;
        }
        catch (ClassCastException error)
        {
            super.handleInvalidBeanClass(beanClass.getName(), error, methodName);
        }

        return null;
    }
}
