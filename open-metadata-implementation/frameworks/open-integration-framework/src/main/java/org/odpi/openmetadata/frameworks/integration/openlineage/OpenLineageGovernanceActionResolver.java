/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.openlineage;

import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextBase;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElementList;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

/**
 * OpenLineageGovernanceActionResolver finds the open metadata elements behind a run that the governance action
 * publisher reported.  The run's egeria_governanceAction facet names the process instance that the engine action is
 * part of (processName) and the process step that it implements (processStepName) by their qualified names.
 * <ul>
 *     <li>The <b>process</b> is the GovernanceActionProcess that the process instance is governed by (or the process
 *     instance itself if it is not linked to one).  The lineage of every run of the process meets at this element,
 *     which is the same element that provisioning services such as Wedgwood connect their own lineage to.</li>
 *     <li>The <b>process step</b> is the GovernanceActionProcessStep.  Each step of a process runs separately, so the
 *     RunMetrics that describe how often and how long the runs take are kept on the step.</li>
 *     <li>The <b>process instance</b> represents this run of the process.</li>
 * </ul>
 * Neither the governance action process nor its steps are assets, so the elements are retrieved through the open
 * metadata store.
 */
public class OpenLineageGovernanceActionResolver
{
    /**
     * The elements behind a governance action run.
     *
     * @param processGUID unique identifier of the governance action process (or the process instance)
     * @param processQualifiedName qualified name of the governance action process
     * @param processStepGUID unique identifier of the process step (null if it can not be found)
     * @param processStepQualifiedName qualified name of the process step
     * @param processInstanceGUID unique identifier of the process instance for this run
     * @param processInstanceQualifiedName qualified name of the process instance
     */
    public record GovernanceActionElements(String processGUID,
                                           String processQualifiedName,
                                           String processStepGUID,
                                           String processStepQualifiedName,
                                           String processInstanceGUID,
                                           String processInstanceQualifiedName) {}


    private final ConnectorContextBase context;
    private final String               sourceName;
    private final PropertyHelper       propertyHelper = new PropertyHelper();


    /**
     * Constructor.
     *
     * @param context context of the calling connector or governance service
     * @param sourceName name of the caller (for property helper calls)
     */
    public OpenLineageGovernanceActionResolver(ConnectorContextBase context,
                                               String               sourceName)
    {
        this.context    = context;
        this.sourceName = sourceName;
    }


    /**
     * Find the elements behind a run reported by the governance action publisher.
     *
     * @param facet the run's egeria_governanceAction facet
     * @return elements, or null if the run is not part of a governance action process that can be found
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException repository problem
     * @throws UserNotAuthorizedException security problem
     */
    public GovernanceActionElements resolve(OpenLineageEgeriaGovernanceActionRunFacet facet) throws InvalidParameterException,
                                                                                                    PropertyServerException,
                                                                                                    UserNotAuthorizedException
    {
        if ((facet == null) || (facet.getProcessName() == null))
        {
            return null;
        }

        OpenMetadataStore   store           = context.getOpenMetadataStore();
        OpenMetadataElement processInstance = store.getMetadataElementByUniqueName(facet.getProcessName(), OpenMetadataProperty.QUALIFIED_NAME.name);

        if (processInstance == null)
        {
            return null;
        }

        OpenMetadataElement process = processInstance;

        if (propertyHelper.isTypeOf(processInstance, OpenMetadataType.GOVERNANCE_ACTION_PROCESS_INSTANCE.typeName))
        {
            RelatedMetadataElementList governedBy = store.getRelatedMetadataElements(processInstance.getElementGUID(),
                                                                                     0,
                                                                                     OpenMetadataType.GOVERNED_BY_RELATIONSHIP.typeName,
                                                                                     0,
                                                                                     context.getMaxPageSize());

            if ((governedBy != null) && (governedBy.getElementList() != null))
            {
                for (RelatedMetadataElement relatedElement : governedBy.getElementList())
                {
                    if ((relatedElement != null) && (relatedElement.getElement() != null) &&
                        (propertyHelper.isTypeOf(relatedElement.getElement(), OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName)))
                    {
                        process = relatedElement.getElement();
                        break;
                    }
                }
            }
        }

        OpenMetadataElement processStep = null;

        if (facet.getProcessStepName() != null)
        {
            processStep = store.getMetadataElementByUniqueName(facet.getProcessStepName(), OpenMetadataProperty.QUALIFIED_NAME.name);

            if ((processStep != null) && (! propertyHelper.isTypeOf(processStep, OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP.typeName)))
            {
                processStep = null;
            }
        }

        return new GovernanceActionElements(process.getElementGUID(),
                                            getQualifiedName(process),
                                            (processStep == null) ? null : processStep.getElementGUID(),
                                            (processStep == null) ? null : getQualifiedName(processStep),
                                            processInstance.getElementGUID(),
                                            getQualifiedName(processInstance));
    }


    /**
     * Return the qualified name of an element.
     *
     * @param element element
     * @return qualified name or null
     */
    private String getQualifiedName(OpenMetadataElement element)
    {
        final String methodName = "getQualifiedName";

        return propertyHelper.getStringProperty(sourceName, OpenMetadataProperty.QUALIFIED_NAME.name, element.getElementProperties(), methodName);
    }
}
