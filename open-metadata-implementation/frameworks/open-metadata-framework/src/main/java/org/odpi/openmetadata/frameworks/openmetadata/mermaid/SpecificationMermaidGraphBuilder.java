/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.List;
import java.util.Map;
import java.util.UUID;


/**
 * Show the specification properties ad a mermaid graph.
 */
public class SpecificationMermaidGraphBuilder extends MermaidGraphBuilderBase
{
    /**
     * Constructor for the graph.
     *
     * @param rootElement contents
     */
    public SpecificationMermaidGraphBuilder(OpenMetadataRootElement rootElement)
    {
        if (rootElement.getSpecification() != null)
        {
            populateMermaidGraph(rootElement.getElementHeader(),
                                 super.getNodeDisplayName(rootElement),
                                 rootElement.getSpecification());
        }
    }


    /**
     * Constructor for the graph - this is used by the Open Metadata Framework
     *
     * @param elementHeader description of the owning element
     * @param elementDisplayName display name for the element
     * @param specification contents of specification
     */
    public SpecificationMermaidGraphBuilder(ElementHeader                            elementHeader,
                                            String                                   elementDisplayName,
                                            Map<String, List<SpecificationProperty>> specification)
    {
        populateMermaidGraph(elementHeader, elementDisplayName, specification);
    }


    /**
     * Constructor for the graph - this is used by the Open Metadata Framework
     *
     * @param elementHeader description of the owning element
     * @param elementDisplayName display name for the element
     * @param specification contents of specification
     */
    public void populateMermaidGraph(ElementHeader                            elementHeader,
                                     String                                   elementDisplayName,
                                     Map<String, List<SpecificationProperty>> specification)
    {
        mermaidGraph.append("---\n");
        mermaidGraph.append("title: Specification for - ");
        mermaidGraph.append(elementDisplayName);
        mermaidGraph.append(" [");
        mermaidGraph.append(elementHeader.getGUID());
        mermaidGraph.append("]\n---\nflowchart LR\n%%{init: {\"flowchart\": {\"htmlLabels\": false}} }%%\n\n");

        if (specification != null)
        {
            this.appendNewMermaidNode(elementHeader.getGUID(),
                                      elementDisplayName,
                                      elementHeader.getType().getTypeName(),
                                      super.getVisualStyleForEntity(elementHeader, VisualStyle.GOVERNED_ELEMENT));

            for (String propertyName : specification.keySet())
            {
                String folderGUID = UUID.randomUUID().toString();

                this.appendNewMermaidNode(folderGUID,
                                          propertyName,
                                          OpenMetadataType.SPECIFICATION_PROPERTY_ASSIGNMENT_RELATIONSHIP.typeName,
                                          VisualStyle.VALID_VALUE_SET);

                this.appendMermaidLine(null,
                                       elementHeader.getGUID(),
                                       null,
                                       folderGUID);

                for (SpecificationProperty specificationProperty : specification.get(propertyName))
                {
                    if (specificationProperty != null)
                    {
                        String propertyGUID = UUID.randomUUID().toString();

                        this.appendNewMermaidNode(propertyGUID,
                                                  specificationProperty.getName(),
                                                  OpenMetadataType.SPECIFICATION_PROPERTY_VALUE.typeName,
                                                  VisualStyle.SPECIFICATION_PROPERTY);

                        this.appendMermaidLine(null,
                                               folderGUID,
                                               null,
                                               propertyGUID);

                        this.startSubgraph(specificationProperty.getName() + " details", VisualStyle.SPECIFICATION_PROPERTY);

                        this.appendNewMermaidNode(propertyGUID + "::" + OpenMetadataProperty.DESCRIPTION.name,
                                                  specificationProperty.getDescription(),
                                                  OpenMetadataProperty.DESCRIPTION.name + ":",
                                                  VisualStyle.DESCRIPTION);

                        if (specificationProperty instanceof ActionTargetType actionTargetType)
                        {
                            this.appendNewMermaidNode(propertyName + "::" + specificationProperty.getName() + "::" + OpenMetadataProperty.OPEN_METADATA_TYPE_NAME.name,
                                                      actionTargetType.getOpenMetadataTypeName(),
                                                      OpenMetadataProperty.OPEN_METADATA_TYPE_NAME.name + ":",
                                                      VisualStyle.DESCRIPTION);

                            this.appendNewMermaidNode(propertyName + "::" + specificationProperty.getName() + "::" + OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name,
                                                      actionTargetType.getDeployedImplementationType(),
                                                      OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name + ":",
                                                      VisualStyle.DESCRIPTION);

                            this.appendNewMermaidNode(propertyName + "::" + specificationProperty.getName() + "::" + OpenMetadataProperty.REQUIRED.name,
                                                      Boolean.toString(actionTargetType.getRequired()),
                                                      OpenMetadataProperty.REQUIRED.name + ":",
                                                      VisualStyle.DESCRIPTION);
                        }
                        else if (specificationProperty instanceof AnnotationTypeType annotationTypeType)
                        {
                            this.appendNewMermaidNode(propertyName + "::" + specificationProperty.getName() + "::" + OpenMetadataProperty.ANALYSIS_STEP.name,
                                                      annotationTypeType.getAnalysisStepName(),
                                                      OpenMetadataProperty.ANALYSIS_STEP.name + ":",
                                                      VisualStyle.DESCRIPTION);

                            this.appendNewMermaidNode(propertyName + "::" + specificationProperty.getName() + "::" + OpenMetadataProperty.OPEN_METADATA_TYPE_NAME.name,
                                                      annotationTypeType.getOpenMetadataTypeName(),
                                                      OpenMetadataProperty.OPEN_METADATA_TYPE_NAME.name + ":",
                                                      VisualStyle.DESCRIPTION);

                            this.appendNewMermaidNode(propertyName + "::" + specificationProperty.getName() + "::" + OpenMetadataProperty.EXPLANATION.name,
                                                      annotationTypeType.getExplanation(),
                                                      OpenMetadataProperty.EXPLANATION.name,
                                                      VisualStyle.DESCRIPTION);

                            this.appendNewMermaidNode(propertyName + "::" + specificationProperty.getName() + "::" + OpenMetadataProperty.EXPRESSION.name,
                                                      annotationTypeType.getExpression(),
                                                      OpenMetadataProperty.EXPRESSION.name + ":",
                                                      VisualStyle.DESCRIPTION);
                        }
                        else if (specificationProperty instanceof ConfigurationPropertyType configurationPropertyType)
                        {
                            this.appendNewMermaidNode(propertyName + "::" + specificationProperty.getName() + "::" + OpenMetadataProperty.DATA_TYPE.name,
                                                      configurationPropertyType.getDataType(),
                                                      OpenMetadataProperty.DATA_TYPE.name + ":",
                                                      VisualStyle.DESCRIPTION);

                            this.appendNewMermaidNode(propertyName + "::" + specificationProperty.getName() + "::" + OpenMetadataProperty.EXAMPLE.name,
                                                      configurationPropertyType.getExample(),
                                                      OpenMetadataProperty.EXAMPLE.name + ":",
                                                      VisualStyle.DESCRIPTION);

                            this.appendNewMermaidNode(propertyName + "::" + specificationProperty.getName() + "::" + OpenMetadataProperty.REQUIRED.name,
                                                      Boolean.toString(configurationPropertyType.getRequired()),
                                                      OpenMetadataProperty.REQUIRED.name + ":",
                                                      VisualStyle.DESCRIPTION);
                        }
                        else if (specificationProperty instanceof GuardType guardType)
                        {
                            if (guardType.getCompletionStatus() != null)
                            {
                                this.appendNewMermaidNode(propertyName + "::" + specificationProperty.getName() + "::completionStatus",
                                                          guardType.getCompletionStatus().getName() + " (" + guardType.getCompletionStatus().getDescription() + ")",
                                                          "completionStatus:",
                                                          VisualStyle.DESCRIPTION);
                            }
                        }
                        else if (specificationProperty instanceof PlaceholderPropertyType placeholderPropertyType)
                        {
                            this.appendNewMermaidNode(propertyName + "::" + specificationProperty.getName() + "::" + OpenMetadataProperty.DATA_TYPE.name,
                                                      placeholderPropertyType.getDataType(),
                                                      OpenMetadataProperty.DATA_TYPE.name + ":",
                                                      VisualStyle.DESCRIPTION);

                            this.appendNewMermaidNode(propertyName + "::" + specificationProperty.getName() + "::" + OpenMetadataProperty.EXAMPLE.name,
                                                      placeholderPropertyType.getExample(),
                                                      OpenMetadataProperty.EXAMPLE.name + ":",
                                                      VisualStyle.DESCRIPTION);

                            this.appendNewMermaidNode(propertyName + "::" + specificationProperty.getName() + "::" + OpenMetadataProperty.REQUIRED.name,
                                                      Boolean.toString(placeholderPropertyType.getRequired()),
                                                      OpenMetadataProperty.REQUIRED.name + ":",
                                                      VisualStyle.DESCRIPTION);
                        }
                        else if (specificationProperty instanceof ReplacementAttributeType replacementAttributeType)
                        {
                            this.appendNewMermaidNode(propertyName + "::" + specificationProperty.getName() + "::" + OpenMetadataProperty.DATA_TYPE.name,
                                                      replacementAttributeType.getDataType(),
                                                      OpenMetadataProperty.DATA_TYPE.name + ":",
                                                      VisualStyle.DESCRIPTION);

                            this.appendNewMermaidNode(propertyName + "::" + specificationProperty.getName() + "::" + OpenMetadataProperty.EXAMPLE.name,
                                                      replacementAttributeType.getExample(),
                                                      OpenMetadataProperty.EXAMPLE.name + ":",
                                                      VisualStyle.DESCRIPTION);

                            this.appendNewMermaidNode(propertyName + "::" + specificationProperty.getName() + "::" + OpenMetadataProperty.REQUIRED.name,
                                                      Boolean.toString(replacementAttributeType.getRequired()),
                                                      OpenMetadataProperty.REQUIRED.name + ":",
                                                      VisualStyle.DESCRIPTION);
                        }
                        else if (specificationProperty instanceof RequestParameterType requestParameterType)
                        {
                            this.appendNewMermaidNode(propertyName + "::" + specificationProperty.getName() + "::" + OpenMetadataProperty.DATA_TYPE.name,
                                                      requestParameterType.getDataType(),
                                                      OpenMetadataProperty.DATA_TYPE.name + ":",
                                                      VisualStyle.DESCRIPTION);

                            this.appendNewMermaidNode(propertyName + "::" + specificationProperty.getName() + "::" + OpenMetadataProperty.EXAMPLE.name,
                                                      requestParameterType.getExample(),
                                                      OpenMetadataProperty.EXAMPLE.name + ":",
                                                      VisualStyle.DESCRIPTION);

                            this.appendNewMermaidNode(propertyName + "::" + specificationProperty.getName() + "::" + OpenMetadataProperty.REQUIRED.name,
                                                      Boolean.toString(requestParameterType.getRequired()),
                                                      OpenMetadataProperty.REQUIRED.name + ":",
                                                      VisualStyle.DESCRIPTION);
                        }
                        else if (specificationProperty instanceof TemplateType templateType)
                        {
                            this.appendNewMermaidNode(propertyName + "::" + specificationProperty.getName() + "::" + OpenMetadataProperty.OPEN_METADATA_TYPE_NAME.name,
                                                      templateType.getOpenMetadataTypeName(),
                                                      OpenMetadataProperty.OPEN_METADATA_TYPE_NAME.name + ":",
                                                      VisualStyle.DESCRIPTION);

                            this.appendNewMermaidNode(propertyName + "::" + specificationProperty.getName() + "::defaultTemplateGUID",
                                                      templateType.getDefaultTemplateGUID(),
                                                      "defaultTemplateGUID:",
                                                      VisualStyle.DESCRIPTION);

                            this.appendNewMermaidNode(propertyName + "::" + specificationProperty.getName() + "::" + OpenMetadataProperty.REQUIRED.name,
                                                      Boolean.toString(templateType.getRequired()),
                                                      OpenMetadataProperty.REQUIRED.name + ":",
                                                      VisualStyle.DESCRIPTION);
                        }

                        if (specificationProperty.getOtherPropertyValues() != null)
                        {
                            for (String otherPropertyName : specificationProperty.getOtherPropertyValues().keySet())
                            {
                                this.appendNewMermaidNode(propertyName + "::" + specificationProperty.getName() + "::otherPropertyValue::" + otherPropertyName,
                                                          specificationProperty.getOtherPropertyValues().get(otherPropertyName),
                                                          otherPropertyName + ":",
                                                          VisualStyle.DESCRIPTION);
                            }
                        }

                        this.endSubgraph();

                        super.appendMermaidLine(null, propertyGUID, null,specificationProperty.getName() + " details");
                    }
                }
            }
        }
    }
}
