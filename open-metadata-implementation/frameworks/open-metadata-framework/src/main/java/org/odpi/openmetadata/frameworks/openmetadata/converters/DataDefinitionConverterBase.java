/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.converters;

import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.DataDefinitionElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * ActorProfileConverter provides common methods for transferring relevant properties from an Open Metadata Element
 * object into a bean that inherits from ActorProfileElement.
 */
public class DataDefinitionConverterBase<B> extends AttributedElementConverterBase<B>
{
    /**
     * Constructor
     *
     * @param propertyHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public DataDefinitionConverterBase(PropertyHelper propertyHelper,
                                       String         serviceName,
                                       String         serverName)
    {
        super(propertyHelper, serviceName, serverName);
    }


    /**
     * Summarize the relationships that have no special processing by the subtype.
     *
     * @param beanClass bean class
     * @param relatedMetadataElements elements to summarize
     * @throws PropertyServerException problem in converter
     */
    protected void addRelationshipsToBean(Class<B>                     beanClass,
                                          List<RelatedMetadataElement> relatedMetadataElements,
                                          DataDefinitionElement        dataDefinitionElement) throws PropertyServerException
    {
        final String methodName = "addRelationshipToBean";

        if (relatedMetadataElements != null)
        {
            /*
             * These are the relationships for the data definition element
             */
            List<RelatedMetadataElementSummary> semanticDefinitions = new ArrayList<>();
            List<RelatedMetadataElement>        others              = new ArrayList<>();

            /*
             * Step through the relationships processing those that relate directly to attributed elements
             */
            for (RelatedMetadataElement relatedMetadataElement: relatedMetadataElements)
            {
                if (relatedMetadataElement != null)
                {
                    if ((propertyHelper.isTypeOf(relatedMetadataElement, OpenMetadataType.SEMANTIC_DEFINITION_RELATIONSHIP.typeName)) && (! relatedMetadataElement.getElementAtEnd1()))
                    {
                        semanticDefinitions.add(this.getRelatedElementSummary(relatedMetadataElement, methodName));
                    }
                    else
                    {
                        others.add(relatedMetadataElement);
                    }
                }
            }

            if (! semanticDefinitions.isEmpty())
            {
                dataDefinitionElement.setAssignedMeanings(semanticDefinitions);
            }

            super.addRelationshipsToBean(others, dataDefinitionElement);
        }
    }
}
