/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Date;
import java.util.Map;

/**
 * SurveyReportBuilder supports the creation of the entities and
 * relationships that describe a Survey Analysis Framework (SAF) Survey Report.
 */
public class SurveyReportBuilder extends ReferenceableBuilder
{
    private final String                 displayName;
    private final String                 description;
    private final Date                   creationDate;
    private final Map<String, String>    analysisParameters;



    /**
     * Constructor supporting all properties - used for updates.
     *
     * @param qualifiedName unique name
     * @param displayName new value for the display name.
     * @param description long description
     * @param creationDate date that the report ran
     * @param analysisParameters list of zones that this discovery service belongs to.
     * @param additionalProperties additional properties
     * @param extendedProperties  properties from the subtype.
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    SurveyReportBuilder(String                 qualifiedName,
                        String                 displayName,
                        String                 description,
                        Date                   creationDate,
                        Map<String, String>    analysisParameters,
                        Map<String, String>    additionalProperties,
                        Map<String, Object>    extendedProperties,
                        OMRSRepositoryHelper   repositoryHelper,
                        String                 serviceName,
                        String                 serverName)
    {
        super(qualifiedName,
              additionalProperties,
              OpenMetadataType.SURVEY_REPORT.typeGUID,
              OpenMetadataType.SURVEY_REPORT.typeName,
              extendedProperties,
              InstanceStatus.DRAFT,
              repositoryHelper,
              serviceName,
              serverName);

        this.displayName = displayName;
        this.description = description;
        this.creationDate = creationDate;
        this.analysisParameters = analysisParameters;
    }


    /**
     * Return the supplied bean properties in an InstanceProperties object.
     *
     * @param methodName name of the calling method
     * @return InstanceProperties object
     * @throws InvalidParameterException there is a problem with the properties
     */
    @Override
    public InstanceProperties getInstanceProperties(String  methodName) throws InvalidParameterException
    {
        InstanceProperties properties = super.getInstanceProperties(methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.DISPLAY_NAME.name,
                                                                  displayName,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.DESCRIPTION.name,
                                                                  description,
                                                                  methodName);

        properties = repositoryHelper.addDatePropertyToInstance(serviceName,
                                                                properties,
                                                                OpenMetadataType.EXECUTION_DATE_PROPERTY_NAME,
                                                                creationDate,
                                                                methodName);

        properties = repositoryHelper.addStringMapPropertyToInstance(serviceName,
                                                                     properties,
                                                                     OpenMetadataProperty.ANALYSIS_PARAMETERS.name,
                                                                     analysisParameters,
                                                                     methodName);

        return properties;
    }
}
