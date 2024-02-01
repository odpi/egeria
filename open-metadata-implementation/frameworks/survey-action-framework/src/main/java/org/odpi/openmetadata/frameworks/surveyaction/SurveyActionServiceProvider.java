/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.surveyaction;

import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;
import org.odpi.openmetadata.frameworks.governanceaction.search.GovernanceServiceProviderBase;


/**
 * SurveyActionServiceProvider implements the base class for the connector provider for a survey action service.
 */
public abstract class SurveyActionServiceProvider extends GovernanceServiceProviderBase
{
    static
    {
        supportedAssetTypeName = OpenMetadataType.SURVEY_ACTION_SERVICE.typeName;
    }
}
