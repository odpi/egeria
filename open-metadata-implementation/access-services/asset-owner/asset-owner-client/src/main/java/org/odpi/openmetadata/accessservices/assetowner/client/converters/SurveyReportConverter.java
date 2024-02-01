/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.client.converters;

import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;
import org.odpi.openmetadata.frameworks.governanceaction.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.search.ElementProperties;
import org.odpi.openmetadata.frameworks.governanceaction.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.surveyaction.properties.SurveyReport;

import java.util.List;


/**
 * SurveyReportConverter converts GAF elements into a survey report bean.
 */
public class SurveyReportConverter<B> extends AssetOwnerConverterBase<B>
{
    /**
     * Constructor
     *
     * @param propertyHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public SurveyReportConverter(PropertyHelper propertyHelper,
                                 String         serviceName,
                                 String         serverName)
    {
        super(propertyHelper, serviceName, serverName);
    }


    /**
     * Using the supplied instances, return a new instance of the SurveyReport bean.
     *
     * @param beanClass class of bean that has been requested
     * @param primaryElement entity that is the root of the collection of entities that make up the
     *                      content of the bean
     * @param relationships relationships linking the entities
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied in the constructor
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    public SurveyReport getSurveyReport(Class<B>                     beanClass,
                                        OpenMetadataElement          primaryElement,
                                        List<RelatedMetadataElement> relationships,
                                        String                       methodName) throws PropertyServerException
    {
        if (propertyHelper.isTypeOf(primaryElement, OpenMetadataType.SURVEY_REPORT.typeName))
        {
            SurveyReport surveyReport = new SurveyReport();
            surveyReport.setElementHeader(this.getMetadataElementHeader(beanClass, primaryElement, methodName));


            ElementProperties elementProperties = null;
            if (primaryElement.getElementProperties() != null)
            {
                elementProperties = new ElementProperties(primaryElement.getElementProperties());
            }

            surveyReport.setQualifiedName(super.removeQualifiedName(elementProperties));
            surveyReport.setDisplayName(super.removeDisplayName(elementProperties));
            surveyReport.setDescription(super.removeDescription(elementProperties));
            surveyReport.setPurpose(super.removePurpose(elementProperties));
            surveyReport.setUser(super.removeUser(elementProperties));
            surveyReport.setAnalysisParameters(super.removeAnalysisParameters(elementProperties));
            surveyReport.setAssetGUID(super.removeAssetGUID(elementProperties));
            surveyReport.setAnalysisStep(super.removeAnalysisStep(elementProperties));
            surveyReport.setStartDate(super.removeStartDate(elementProperties));
            surveyReport.setCompletionDate(super.removeCompletionDate(elementProperties));
            surveyReport.setCompletionMessage(super.removeCompletionMessage(elementProperties));
            surveyReport.setAdditionalProperties(super.removeAdditionalProperties(elementProperties));

            surveyReport.setExtendedProperties(this.getRemainingExtendedProperties(elementProperties));

            if (relationships != null)
            {
                for (RelatedMetadataElement relationship : relationships)
                {
                    if (relationship != null)
                    {
                        if (propertyHelper.isTypeOf(relationship, OpenMetadataType.ENGINE_ACTION_SURVEY_REPORT_RELATIONSHIP.typeName))
                        {
                            surveyReport.setEngineActionGUID(relationship.getElement().getElementGUID());
                            break;
                        }
                    }
                }
            }
        }

        return null;
    }
}
