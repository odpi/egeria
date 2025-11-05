/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.viewservices.automatedcuration.mermaid;

import org.odpi.openmetadata.frameworks.openmetadata.mermaid.MermaidGraphBuilderBase;
import org.odpi.openmetadata.frameworks.openmetadata.mermaid.VisualStyle;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.SpecificationProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.viewservices.automatedcuration.properties.RefDataElementBase;
import org.odpi.openmetadata.viewservices.automatedcuration.properties.ResourceDescription;
import org.odpi.openmetadata.viewservices.automatedcuration.properties.TechnologyTypeReport;

import java.util.List;
import java.util.UUID;


/**
 * Show the structure of a technology type
 */
public class TechnologyTypeReportMermaidGraphBuilder extends MermaidGraphBuilderBase
{
    /**
     * Constructor for the graph - this is used by the Open Metadata Framework
     *
     * @param technologyTypeReport description of the technology type to draw
     */
    public TechnologyTypeReportMermaidGraphBuilder(TechnologyTypeReport technologyTypeReport)
    {
        mermaidGraph.append("---\n");
        mermaidGraph.append("title: Technology Report for - ");
        mermaidGraph.append(technologyTypeReport.getDisplayName());
        mermaidGraph.append(" [");
        mermaidGraph.append(technologyTypeReport.getTechnologyTypeGUID());
        mermaidGraph.append("]\n---\nflowchart LR\n%%{init: {\"flowchart\": {\"htmlLabels\": false}} }%%\n\n");

        this.appendNewMermaidNode(technologyTypeReport.getTechnologyTypeGUID(),
                                  technologyTypeReport.getDisplayName(),
                                  OpenMetadataType.TECHNOLOGY_TYPE.typeName,
                                  VisualStyle.TECHNOLOGY_TYPE);

        if (technologyTypeReport.getCatalogTemplates() != null)
        {
            String folderGUID = UUID.randomUUID().toString();

            this.appendNewMermaidNode(folderGUID,
                                      "Catalog Templates",
                                      OpenMetadataType.TECHNOLOGY_TYPE.typeName,
                                      VisualStyle.TECHNOLOGY_TYPE);

            this.appendMermaidLine(null,
                                   technologyTypeReport.getTechnologyTypeGUID(),
                                   null,
                                   folderGUID);

            for (RefDataElementBase refDataElementBase : technologyTypeReport.getCatalogTemplates())
            {
                addRefDataElementToGraph(refDataElementBase,
                                         "catalog templates",
                                         folderGUID);
            }
        }

        if (technologyTypeReport.getGovernanceActionProcesses() != null)
        {
            String folderGUID = UUID.randomUUID().toString();

            this.appendNewMermaidNode(folderGUID,
                                      "Governance Action Processes",
                                      OpenMetadataType.TECHNOLOGY_TYPE.typeName,
                                      VisualStyle.TECHNOLOGY_TYPE);

            this.appendMermaidLine(null,
                                   technologyTypeReport.getTechnologyTypeGUID(),
                                   null,
                                   folderGUID);

            for (ResourceDescription resourceDescription : technologyTypeReport.getGovernanceActionProcesses())
            {
                if (resourceDescription != null)
                {
                    if (resourceDescription.getResourceUse() != null)
                    {
                        addRefDataElementToGraph(resourceDescription,
                                                 "governance action process [" + resourceDescription.getResourceUse() + "]",
                                                 folderGUID);
                    }
                    else
                    {
                        addRefDataElementToGraph(resourceDescription,
                                                 "governance action process",
                                                 folderGUID);
                    }
                }
            }
        }

        if (technologyTypeReport.getResourceList() != null)
        {
            String folderGUID = UUID.randomUUID().toString();

            this.appendNewMermaidNode(folderGUID,
                                      "Resource List",
                                      OpenMetadataType.TECHNOLOGY_TYPE.typeName,
                                      VisualStyle.TECHNOLOGY_TYPE);

            this.appendMermaidLine(null,
                                   technologyTypeReport.getTechnologyTypeGUID(),
                                   null,
                                   folderGUID);

            for (ResourceDescription resourceDescription : technologyTypeReport.getResourceList())
            {
                if (resourceDescription != null)
                {
                    if (resourceDescription.getResourceUse() != null)
                    {
                        addRefDataElementToGraph(resourceDescription,
                                                 "resource [" + resourceDescription.getResourceUse() + "]",
                                                 folderGUID);
                    }
                    else
                    {
                        addRefDataElementToGraph(resourceDescription,
                                                 "resource",
                                                 folderGUID);
                    }
                }
            }
        }

        super.addRelatedElementSummaries(technologyTypeReport.getExternalReferences(),
                                         VisualStyle.EXTERNAL_REFERENCE,
                                         technologyTypeReport.getTechnologyTypeGUID());
    }


    /**
     * Add reference data details to the graph.
     *
     * @param refDataElementBase description
     * @param label label to link ref data
     * @param technologyTypeGUID starting guid
     */
    private void addRefDataElementToGraph(RefDataElementBase refDataElementBase,
                                          String             label,
                                          String             technologyTypeGUID)
    {
        if (refDataElementBase != null)
        {
            if (refDataElementBase.getDisplayName() != null)
            {
                this.appendNewMermaidNode(refDataElementBase.getRelatedElement().getElementHeader().getGUID(),
                                          refDataElementBase.getDisplayName(),
                                          refDataElementBase.getRelatedElement().getElementHeader().getType().getTypeName(),
                                          getVisualStyleForEntity(refDataElementBase.getRelatedElement().getElementHeader(), VisualStyle.GOVERNED_ELEMENT));
            }
            else
            {
                this.appendNewMermaidNode(refDataElementBase.getRelatedElement().getElementHeader().getGUID(),
                                          super.getNodeDisplayName(refDataElementBase.getRelatedElement()),
                                          refDataElementBase.getRelatedElement().getElementHeader().getType().getTypeName(),
                                          getVisualStyleForEntity(refDataElementBase.getRelatedElement().getElementHeader(), VisualStyle.GOVERNED_ELEMENT));
            }

            this.appendMermaidLine(null,
                                   technologyTypeGUID,
                                   label,
                                   refDataElementBase.getRelatedElement().getElementHeader().getGUID());

            if (refDataElementBase.getSpecification() != null)
            {
                for (String propertyName : refDataElementBase.getSpecification().keySet())
                {
                    String propertyGUID = refDataElementBase.getRelatedElement().getElementHeader().getGUID() + "::" + propertyName;
                    super.appendNewMermaidNode(propertyGUID,
                                               propertyName,
                                               "PropertyType",
                                               VisualStyle.VALID_VALUE_SET);

                    this.appendMermaidLine(null,
                                           refDataElementBase.getRelatedElement().getElementHeader().getGUID(),
                                           OpenMetadataType.SPECIFICATION_PROPERTY_ASSIGNMENT_RELATIONSHIP.typeName,
                                           propertyGUID);

                    List<SpecificationProperty> specificationProperties = refDataElementBase.getSpecification().get(propertyName);

                    if (specificationProperties != null)
                    {
                        for (SpecificationProperty specificationProperty : specificationProperties)
                        {
                            if (specificationProperty != null)
                            {
                                String specificationPropertiesGUID = propertyGUID + "::" + specificationProperty.getName();
                                super.appendNewMermaidNode(specificationPropertiesGUID,
                                                           specificationProperty.getName(),
                                                           specificationProperty.getSpecificationPropertyType().getPropertyType(),
                                                           VisualStyle.SPECIFICATION_PROPERTY);

                                this.appendMermaidLine(null,
                                                       propertyGUID,
                                                       OpenMetadataType.SPECIFICATION_PROPERTY_ASSIGNMENT_RELATIONSHIP.typeName,
                                                       specificationPropertiesGUID);
                            }
                        }
                    }
                }
            }
        }
    }
}
