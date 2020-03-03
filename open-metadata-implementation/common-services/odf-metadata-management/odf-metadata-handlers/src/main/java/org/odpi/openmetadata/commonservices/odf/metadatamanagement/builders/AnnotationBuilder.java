/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.odf.metadatamanagement.builders;

import org.odpi.openmetadata.commonservices.odf.metadatamanagement.mappers.AnnotationMapper;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Map;

/**
 * AnnotationBuilder supports the creation of the entities and
 * relationships that describe an Open Discovery Framework (ODF) Annotation.
 */
public class AnnotationBuilder
{
    protected String               annotationType;
    protected String               summary;
    protected int                  confidenceLevel;
    protected String               expression;
    protected String               explanation;
    protected String               analysisStep;
    protected String               jsonProperties;
    protected Map<String, String>  additionalProperties;
    protected OMRSRepositoryHelper repositoryHelper;
    protected String               serviceName;
    protected String               serverName;


    /**
     * Create a builder to convert the properties of the annotation bean into repository services instances.
     *
     * @param annotationType type of annotation
     * @param summary summary of the annotation
     * @param confidenceLevel how confident was discovery service that the results are correct
     * @param expression expression that summarizes the results
     * @param explanation explanation of the results
     * @param analysisStep analysis step in the discovery service that produced this annotation
     * @param jsonProperties JSON properties passed to discovery service
     * @param additionalProperties additional properties
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    public AnnotationBuilder(String               annotationType,
                             String               summary,
                             int                  confidenceLevel,
                             String               expression,
                             String               explanation,
                             String               analysisStep,
                             String               jsonProperties,
                             Map<String, String>  additionalProperties,
                             OMRSRepositoryHelper repositoryHelper,
                             String               serviceName,
                             String               serverName)
    {
        this.annotationType         = annotationType;
        this.summary                = summary;
        this.confidenceLevel        = confidenceLevel;
        this.expression             = expression;
        this.explanation            = explanation;
        this.analysisStep           = analysisStep;
        this.jsonProperties         = jsonProperties;
        this.additionalProperties   = additionalProperties;
        this.repositoryHelper       = repositoryHelper;
        this.serviceName            = serviceName;
        this.serverName             = serverName;
    }


    /**
     * Return the supplied bean properties in an InstanceProperties object for the annotation entity.
     *
     * @param methodName name of the calling method
     * @return InstanceProperties object
     * @throws InvalidParameterException there is a problem with the properties
     */
    public InstanceProperties getAnnotationInstanceProperties(String  methodName) throws InvalidParameterException
    {
        InstanceProperties properties = new InstanceProperties();

        if (annotationType != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      AnnotationMapper.ANNOTATION_TYPE_PROPERTY_NAME,
                                                                      annotationType,
                                                                      methodName);
        }

        if (summary != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      AnnotationMapper.SUMMARY_PROPERTY_NAME,
                                                                      summary,
                                                                      methodName);
        }

        properties = repositoryHelper.addIntPropertyToInstance(serviceName,
                                                               properties,
                                                               AnnotationMapper.CONFIDENCE_LEVEL_PROPERTY_NAME,
                                                               confidenceLevel,
                                                               methodName);

        if (expression != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      AnnotationMapper.EXPRESSION_PROPERTY_NAME,
                                                                      expression,
                                                                      methodName);
        }

        if (explanation != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      AnnotationMapper.EXPLANATION_PROPERTY_NAME,
                                                                      explanation,
                                                                      methodName);
        }

        if (analysisStep != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      AnnotationMapper.ANALYSIS_STEP_PROPERTY_NAME,
                                                                      analysisStep,
                                                                      methodName);
        }

        if (jsonProperties != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      AnnotationMapper.JSON_PROPERTIES_PROPERTY_NAME,
                                                                      jsonProperties,
                                                                      methodName);
        }

        if (additionalProperties != null)
        {
            properties = repositoryHelper.addStringMapPropertyToInstance(serviceName,
                                                                         properties,
                                                                         AnnotationMapper.ADDITIONAL_PROPERTIES_PROPERTY_NAME,
                                                                         additionalProperties,
                                                                         methodName);
        }

        return properties;
    }
}
