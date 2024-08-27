/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.samples.archiveutilities;

import org.odpi.openmetadata.frameworks.connectors.controls.SupportedTechnologyType;
import org.odpi.openmetadata.frameworks.governanceaction.controls.ActionTargetType;
import org.odpi.openmetadata.frameworks.governanceaction.controls.GuardType;
import org.odpi.openmetadata.frameworks.governanceaction.controls.RequestParameterType;
import org.odpi.openmetadata.frameworks.governanceaction.controls.RequestTypeType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.ResourceUse;
import org.odpi.openmetadata.frameworks.surveyaction.controls.AnalysisStepType;
import org.odpi.openmetadata.frameworks.surveyaction.controls.AnnotationTypeType;

import java.util.List;

/**
 * GovernanceActionDescription provides details for calling a governance service.
 */
public class GovernanceActionDescription
{
    public String                        governanceServiceGUID        = null;
    public String                        governanceServiceDescription = null;
    public List<SupportedTechnologyType> supportedTechnologies        = null;
    public List<RequestTypeType>         supportedRequestTypes        = null;
    public List<RequestParameterType>    supportedRequestParameters   = null;
    public List<ActionTargetType>        supportedActionTargets       = null;
    public List<AnalysisStepType>        supportedAnalysisSteps       = null;
    public List<AnnotationTypeType>      supportedAnnotationTypes     = null;
    public List<RequestParameterType>    producedRequestParameters    = null;
    public List<ActionTargetType>        producedActionTargets        = null;
    public List<GuardType>               producedGuards               = null;
    public ResourceUse                   resourceUse                  = null;
}
