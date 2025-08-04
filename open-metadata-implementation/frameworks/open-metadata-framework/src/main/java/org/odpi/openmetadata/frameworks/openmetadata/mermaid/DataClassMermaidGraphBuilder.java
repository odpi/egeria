/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.DataClassElement;


/**
 * Creates a mermaid graph rendering of the Open Metadata Framework's data class graph.
 */
public class DataClassMermaidGraphBuilder extends OpenMetadataRootMermaidGraphBuilder
{
    /**
     * Construct a mermaid markdown graph.
     *
     * @param dataClassElement content
     */
    public DataClassMermaidGraphBuilder(DataClassElement dataClassElement)
    {
        super(dataClassElement);

        super.addRelatedElementSummaries(dataClassElement.getNestedDataClasses(), VisualStyle.DATA_CLASS, dataClassElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(dataClassElement.getSpecializedDataClasses(), VisualStyle.DATA_CLASS, dataClassElement.getElementHeader().getGUID());
        super.addRelatedElementSummaries(dataClassElement.getAssignedMeanings(), VisualStyle.LINKED_ELEMENT, dataClassElement.getElementHeader().getGUID());
    }
}
