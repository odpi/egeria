/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.server;

import org.odpi.openmetadata.accessservices.governanceprogram.metadataelements.GovernanceMetricElement;
import org.odpi.openmetadata.accessservices.governanceprogram.metadataelements.RelatedElement;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.GovernanceDefinitionMetricProperties;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.GovernanceExpectationsProperties;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.GovernanceMeasurementsDataSetProperties;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.GovernanceMeasurementsProperties;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.GovernanceMetricProperties;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.GovernanceResultsProperties;
import org.odpi.openmetadata.accessservices.governanceprogram.rest.ClassificationRequestBody;
import org.odpi.openmetadata.accessservices.governanceprogram.rest.ExternalSourceRequestBody;
import org.odpi.openmetadata.accessservices.governanceprogram.rest.GovernanceMetricListResponse;
import org.odpi.openmetadata.accessservices.governanceprogram.rest.GovernanceMetricResponse;
import org.odpi.openmetadata.accessservices.governanceprogram.rest.ReferenceableRequestBody;
import org.odpi.openmetadata.accessservices.governanceprogram.rest.RelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.commonservices.generichandlers.AssetHandler;
import org.odpi.openmetadata.commonservices.generichandlers.GovernanceMetricHandler;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;
import org.odpi.openmetadata.commonservices.generichandlers.ReferenceableHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * GovernanceMetricsRESTServices is the server-side for managing governance metrics and their links to all types of governance definitions.
 */
public class GovernanceMetricsRESTServices
{
    private static final GovernanceProgramInstanceHandler instanceHandler = new GovernanceProgramInstanceHandler();

    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(GovernanceMetricsRESTServices.class),
                                                                            instanceHandler.getServiceName());

    private final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();


    /**
     * Default constructor
     */
    public GovernanceMetricsRESTServices()
    {
    }


    /**
     * Create a new governance metric.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param requestBody properties of the metric
     *
     * @return unique identifier of the metric or
     *  InvalidParameterException typeName, documentIdentifier or userId is null; documentIdentifier is not unique; typeName is not valid
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    public GUIDResponse createGovernanceMetric(String                   serverName,
                                               String                   userId,
                                               ReferenceableRequestBody requestBody)
    {
        final String methodName = "createGovernanceMetric";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof GovernanceMetricProperties)
                {
                    auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                    GovernanceMetricHandler<GovernanceMetricElement> handler = instanceHandler.getGovernanceMetricHandler(userId, serverName, methodName);

                    GovernanceMetricProperties properties = (GovernanceMetricProperties) requestBody.getProperties();

                    String setGUID = handler.createGovernanceMetric(userId,
                                                                    requestBody.getExternalSourceGUID(),
                                                                    requestBody.getExternalSourceName(),
                                                                    properties.getQualifiedName(),
                                                                    properties.getDisplayName(),
                                                                    properties.getDescription(),
                                                                    properties.getMeasurement(),
                                                                    properties.getTarget(),
                                                                    properties.getDomainIdentifier(),
                                                                    properties.getAdditionalProperties(),
                                                                    properties.getTypeName(),
                                                                    properties.getExtendedProperties(),
                                                                    properties.getEffectiveFrom(),
                                                                    properties.getEffectiveTo(),
                                                                    new Date(),
                                                                    methodName);

                    response.setGUID(setGUID);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(GovernanceMetricProperties.class.getName(), methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Update an existing governance metric.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param metricGUID unique identifier of the metric to update
     * @param isMergeUpdate are unspecified properties unchanged (true) or replaced with null?
     * @param requestBody properties to update
     *
     * @return void or
     *  InvalidParameterException invalid guid or properties
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    public VoidResponse updateGovernanceMetric(String                   serverName,
                                               String                   userId,
                                               String                   metricGUID,
                                               boolean                  isMergeUpdate,
                                               ReferenceableRequestBody requestBody)
    {
        final String methodName = "updateGovernanceMetric";
        final String guidParameterName = "metricGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof GovernanceMetricProperties)
                {
                    auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                    GovernanceMetricHandler<GovernanceMetricElement> handler = instanceHandler.getGovernanceMetricHandler(userId, serverName, methodName);

                    GovernanceMetricProperties properties = (GovernanceMetricProperties) requestBody.getProperties();

                    handler.updateGovernanceMetric(userId,
                                                   requestBody.getExternalSourceGUID(),
                                                   requestBody.getExternalSourceName(),
                                                   metricGUID,
                                                   guidParameterName,
                                                   properties.getQualifiedName(),
                                                   properties.getDisplayName(),
                                                   properties.getDescription(),
                                                   properties.getMeasurement(),
                                                   properties.getTarget(),
                                                   properties.getDomainIdentifier(),
                                                   properties.getAdditionalProperties(),
                                                   properties.getTypeName(),
                                                   properties.getExtendedProperties(),
                                                   properties.getEffectiveFrom(),
                                                   properties.getEffectiveTo(),
                                                   isMergeUpdate,
                                                   false,
                                                   false,
                                                   new Date(),
                                                   methodName);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(GovernanceMetricProperties.class.getName(), methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Delete a specific governance metric.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param metricGUID unique identifier of the metric to remove
     * @param requestBody external source request body
     *
     * @return void or
     *  InvalidParameterException invalid guid
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    public VoidResponse  deleteGovernanceMetric(String                    serverName,
                                                String                    userId,
                                                String                    metricGUID,
                                                ExternalSourceRequestBody requestBody)
    {
        final String methodName = "deleteExternalReference";
        final String guidParameterName = "metricGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GovernanceMetricHandler<GovernanceMetricElement> handler = instanceHandler.getGovernanceMetricHandler(userId, serverName, methodName);

            handler.removeGovernanceMetric(userId,
                                           requestBody.getExternalSourceGUID(),
                                           requestBody.getExternalSourceName(),
                                           metricGUID,
                                           guidParameterName,
                                           false,
                                           false,
                                           new Date(),
                                           methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Create a link to show that a governance metric supports the requirements of one of the governance policies.
     * If the link already exists the rationale is updated.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param metricGUID unique identifier of the governance metric
     * @param governanceDefinitionGUID unique identifier of the governance definition
     * @param requestBody description of how the metric supports the metric
     *
     * @return void or
     *  InvalidParameterException invalid parameter
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
    */
    public VoidResponse setupGovernanceDefinitionMetric(String                  serverName,
                                                        String                  userId,
                                                        String                  metricGUID,
                                                        String                  governanceDefinitionGUID,
                                                        RelationshipRequestBody requestBody)
    {
        final String methodName = "setupGovernanceDefinitionMetric";
        final String metricGUIDParameterName = "metricGUID";
        final String governanceDefinitionGUIDParameterName = "governanceDefinitionGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            GovernanceMetricHandler<GovernanceMetricElement> handler = instanceHandler.getGovernanceMetricHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof GovernanceDefinitionMetricProperties)
                {
                    GovernanceDefinitionMetricProperties properties = (GovernanceDefinitionMetricProperties)requestBody.getProperties();

                    handler.addGovernanceDefinitionMetric(userId,
                                                          requestBody.getExternalSourceGUID(),
                                                          requestBody.getExternalSourceName(),
                                                          metricGUID,
                                                          metricGUIDParameterName,
                                                          governanceDefinitionGUID,
                                                          governanceDefinitionGUIDParameterName,
                                                          properties.getRationale(),
                                                          requestBody.getProperties().getEffectiveFrom(),
                                                          requestBody.getProperties().getEffectiveTo(),
                                                          false,
                                                          false,
                                                          new Date(),
                                                          methodName);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.addGovernanceDefinitionMetric(userId,
                                                          requestBody.getExternalSourceGUID(),
                                                          requestBody.getExternalSourceName(),
                                                          metricGUID,
                                                          metricGUIDParameterName,
                                                          governanceDefinitionGUID,
                                                          governanceDefinitionGUIDParameterName,
                                                          null,
                                                          null,
                                                          null,
                                                          false,
                                                          false,
                                                          new Date(),
                                                          methodName);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(GovernanceDefinitionMetricProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.addGovernanceDefinitionMetric(userId,
                                                      null,
                                                      null,
                                                      metricGUID,
                                                      metricGUIDParameterName,
                                                      governanceDefinitionGUID,
                                                      governanceDefinitionGUIDParameterName,
                                                      null,
                                                      null,
                                                      null,
                                                      false,
                                                      false,
                                                      new Date(),
                                                      methodName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Remove the link between a governance metric and a governance definition.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param metricGUID unique identifier of the governance metric
     * @param governanceDefinitionGUID unique identifier of the governance definition
     * @param requestBody relationship properties
     *
     * @return void or
     *  InvalidParameterException invalid guid
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    public VoidResponse clearGovernanceDefinitionMetric(String                  serverName,
                                                        String                  userId,
                                                        String                  metricGUID,
                                                        String                  governanceDefinitionGUID,
                                                        RelationshipRequestBody requestBody)
    {
        final String methodName = "clearGovernanceDefinitionMetric";
        final String metricGUIDParameterName = "metricGUID";
        final String governanceDefinitionGUIDParameterName = "governanceDefinitionGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            GovernanceMetricHandler<GovernanceMetricElement> handler = instanceHandler.getGovernanceMetricHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.removeGovernanceDefinitionMetric(userId,
                                                         requestBody.getExternalSourceGUID(),
                                                         requestBody.getExternalSourceName(),
                                                         metricGUID,
                                                         metricGUIDParameterName,
                                                         governanceDefinitionGUID,
                                                         governanceDefinitionGUIDParameterName,
                                                         false,
                                                         false,
                                                         new Date(),
                                                         methodName);
            }
            else
            {
                handler.removeGovernanceDefinitionMetric(userId,
                                                         null,
                                                         null,
                                                         metricGUID,
                                                         metricGUIDParameterName,
                                                         governanceDefinitionGUID,
                                                         governanceDefinitionGUIDParameterName,
                                                         false,
                                                         false,
                                                         new Date(),
                                                         methodName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Create a link to show which data set holds the measurements for a data set.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param metricGUID unique identifier of the governance metric
     * @param dataSetGUID unique identifier of the governance definition
     * @param requestBody description of how the data set supports the metric
     *
     * @return void or
     *  InvalidParameterException invalid guid
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    public VoidResponse setupGovernanceResults(String                  serverName,
                                               String                  userId,
                                               String                  metricGUID,
                                               String                  dataSetGUID,
                                               RelationshipRequestBody requestBody)
    {
        final String methodName = "setupGovernanceResults";
        final String metricGUIDParameterName = "metricGUID";
        final String dataSetGUIDParameterName = "dataSetGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            GovernanceMetricHandler<GovernanceMetricElement> handler = instanceHandler.getGovernanceMetricHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof GovernanceResultsProperties)
                {
                    GovernanceResultsProperties properties = (GovernanceResultsProperties)requestBody.getProperties();

                    handler.addGovernanceResults(userId,
                                                   requestBody.getExternalSourceGUID(),
                                                   requestBody.getExternalSourceName(),
                                                   metricGUID,
                                                   metricGUIDParameterName,
                                                   dataSetGUID,
                                                   dataSetGUIDParameterName,
                                                   properties.getQuery(),
                                                   requestBody.getProperties().getEffectiveFrom(),
                                                   requestBody.getProperties().getEffectiveTo(),
                                                   false,
                                                   false,
                                                   new Date(),
                                                   methodName);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.addGovernanceResults(userId,
                                                   requestBody.getExternalSourceGUID(),
                                                   requestBody.getExternalSourceName(),
                                                   metricGUID,
                                                   metricGUIDParameterName,
                                                   dataSetGUID,
                                                   dataSetGUIDParameterName,
                                                   null,
                                                   null,
                                                   null,
                                                   false,
                                                   false,
                                                   new Date(),
                                                   methodName);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(GovernanceResultsProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.addGovernanceResults(userId,
                                               null,
                                               null,
                                               metricGUID,
                                               metricGUIDParameterName,
                                               dataSetGUID,
                                               dataSetGUIDParameterName,
                                               null,
                                               null,
                                               null,
                                               false,
                                               false,
                                               new Date(),
                                               methodName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Remove the link between a governance metric and a data set.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param metricGUID unique identifier of the governance metric
     * @param dataSetGUID unique identifier of the data set
     * @param requestBody relationship properties
     *
     * @return void or
     *  InvalidParameterException invalid guid
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    public VoidResponse clearGovernanceResults(String                  serverName,
                                               String                  userId,
                                               String                  metricGUID,
                                               String                  dataSetGUID,
                                               RelationshipRequestBody requestBody)
    {
        final String methodName = "clearGovernanceResults";
        final String metricGUIDParameterName = "metricGUID";
        final String dataSetGUIDParameterName = "dataSetGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            GovernanceMetricHandler<GovernanceMetricElement> handler = instanceHandler.getGovernanceMetricHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.removeGovernanceResults(userId,
                                               requestBody.getExternalSourceGUID(),
                                               requestBody.getExternalSourceName(),
                                               metricGUID,
                                               metricGUIDParameterName,
                                               dataSetGUID,
                                               dataSetGUIDParameterName,
                                               false,
                                               false,
                                               new Date(),
                                               methodName);
            }
            else
            {
                handler.removeGovernanceResults(userId,
                                               null,
                                               null,
                                               metricGUID,
                                               metricGUIDParameterName,
                                               dataSetGUID,
                                               dataSetGUIDParameterName,
                                               false,
                                               false,
                                               new Date(),
                                               methodName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;

    }


    /**
     * Classify the data set to indicate that contains governance measurements.
     *
     * @param serverName name of the server instance to connect to
     * @param userId        calling user
     * @param dataSetGUID  unique identifier of the metadata element to classify
     * @param requestBody    properties of the data set's measurements
     *
     * @return void or
     *  InvalidParameterException invalid guid
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    public VoidResponse setGovernanceMeasurementsDataSet(String                    serverName,
                                                         String                    userId,
                                                         String                    dataSetGUID,
                                                         ClassificationRequestBody requestBody)
    {
        final String methodName = "setGovernanceMeasurementsDataSet";
        final String guidParameter = "dataSetGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            AssetHandler<RelatedElement> handler = instanceHandler.getRelatedAssetHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof GovernanceMeasurementsDataSetProperties)
                {
                    GovernanceMeasurementsDataSetProperties properties = (GovernanceMeasurementsDataSetProperties)requestBody.getProperties();

                    handler.addGovernanceMeasurementsDataSetClassification(userId,
                                                                           requestBody.getExternalSourceGUID(),
                                                                           requestBody.getExternalSourceName(),
                                                                           dataSetGUID,
                                                                           guidParameter,
                                                                           OpenMetadataType.DATA_SET.typeName,
                                                                           properties.getDescription(),
                                                                           false,
                                                                           false,
                                                                           properties.getEffectiveFrom(),
                                                                           properties.getEffectiveTo(),
                                                                           null,
                                                                           methodName);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(GovernanceMeasurementsDataSetProperties.class.getName(), methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Remove the governance data designation from the data set.
     *
     * @param serverName name of the server instance to connect to
     * @param userId       calling user
     * @param dataSetGUID  unique identifier of the metadata element to classify
     * @param requestBody external source properties
     *
     * @return void or
     *  InvalidParameterException invalid guid
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    public VoidResponse clearGovernanceMeasurementsDataSet(String                    serverName,
                                                           String                    userId,
                                                           String                    dataSetGUID,
                                                           ExternalSourceRequestBody requestBody)
    {
        final String methodName = "clearGovernanceMeasurementsDataSet";
        final String guidParameter = "dataSetGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            AssetHandler<RelatedElement> handler = instanceHandler.getRelatedAssetHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.removeGovernanceMeasurementsDataSetClassification(userId,
                                                                          requestBody.getExternalSourceGUID(),
                                                                          requestBody.getExternalSourceName(),
                                                                          dataSetGUID,
                                                                          guidParameter,
                                                                          OpenMetadataType.DATA_SET.typeName,
                                                                          false,
                                                                          false,
                                                                          null,
                                                                          methodName);
            }
            else
            {
                handler.removeGovernanceMeasurementsDataSetClassification(userId,
                                                                          null,
                                                                          null,
                                                                          dataSetGUID,
                                                                          guidParameter,
                                                                          OpenMetadataType.DATA_SET.typeName,
                                                                          false,
                                                                          false,
                                                                          null,
                                                                          methodName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }

    
    /**
     * Classify the element to indicate the expected values of the governance measurements. Can be used to create or update the values.
     *
     * @param serverName name of the server instance to connect to
     * @param userId        calling user
     * @param elementGUID  unique identifier of the metadata element to classify
     * @param requestBody    expectation properties
     *
     * @return void or
     *  InvalidParameterException invalid guid
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    public VoidResponse setGovernanceExpectations(String                    serverName,
                                                  String                    userId,
                                                  String                    elementGUID,
                                                  ClassificationRequestBody requestBody)
    {
        final String methodName = "setGovernanceExpectations";
        final String elementGUIDParameterName = "elementGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ReferenceableHandler<RelatedElement> handler = instanceHandler.getRelatedElementHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof GovernanceExpectationsProperties)
                {
                    GovernanceExpectationsProperties properties = (GovernanceExpectationsProperties)requestBody.getProperties();

                    handler.addGovernanceExpectationsClassification(userId,
                                                                    requestBody.getExternalSourceGUID(),
                                                                    requestBody.getExternalSourceName(),
                                                                    elementGUID,
                                                                    elementGUIDParameterName,
                                                                    OpenMetadataType.REFERENCEABLE.typeName,
                                                                    properties.getCounts(),
                                                                    properties.getValues(),
                                                                    properties.getFlags(),
                                                                    false,
                                                                    false,
                                                                    properties.getEffectiveFrom(),
                                                                    properties.getEffectiveTo(),
                                                                    null,
                                                                    methodName);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(GovernanceExpectationsProperties.class.getName(), methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Remove the governance expectations classification from the element.
     *
     * @param serverName name of the server instance to connect to
     * @param userId       calling user
     * @param elementGUID  unique identifier of the metadata element to classify
     * @param requestBody external source properties
     *
     * @return void or
     *  InvalidParameterException invalid guid
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    public VoidResponse clearGovernanceExpectations(String                    serverName,
                                                    String                    userId,
                                                    String                    elementGUID,
                                                    ExternalSourceRequestBody requestBody)
    {
        final String methodName = "clearGovernanceExpectations";
        final String elementGUIDParameterName = "elementGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ReferenceableHandler<RelatedElement> handler = instanceHandler.getRelatedElementHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.removeGovernanceExpectationsClassification(userId,
                                                                   requestBody.getExternalSourceGUID(),
                                                                   requestBody.getExternalSourceName(),
                                                                   elementGUID,
                                                                   elementGUIDParameterName,
                                                                   OpenMetadataType.REFERENCEABLE.typeName,
                                                                   false,
                                                                   false,
                                                                   null,
                                                                   methodName);
            }
            else
            {
                handler.removeGovernanceExpectationsClassification(userId,
                                                                   null,
                                                                   null,
                                                                   elementGUID,
                                                                   elementGUIDParameterName,
                                                                   OpenMetadataType.REFERENCEABLE.typeName,
                                                                   false,
                                                                   false,
                                                                   null,
                                                                   methodName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Classify the element with relevant governance measurements. Can be used to create or update the values.
     *
     * @param serverName name of the server instance to connect to
     * @param userId        calling user
     * @param elementGUID  unique identifier of the metadata element to classify
     * @param requestBody    measurements
     *
     * @return void or
     *  InvalidParameterException invalid guid
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    public VoidResponse setGovernanceMeasurements(String                    serverName,
                                                  String                    userId,
                                                  String                    elementGUID,
                                                  ClassificationRequestBody requestBody)
    {
        final String methodName = "setGovernanceMeasurements";
        final String elementGUIDParameterName = "elementGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ReferenceableHandler<RelatedElement> handler = instanceHandler.getRelatedElementHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof GovernanceMeasurementsProperties)
                {
                    GovernanceMeasurementsProperties properties = (GovernanceMeasurementsProperties)requestBody.getProperties();

                    handler.addGovernanceMeasurementsClassification(userId,
                                                                    requestBody.getExternalSourceGUID(),
                                                                    requestBody.getExternalSourceName(),
                                                                    elementGUID,
                                                                    elementGUIDParameterName,
                                                                    OpenMetadataType.REFERENCEABLE.typeName,
                                                                    properties.getCounts(),
                                                                    properties.getValues(),
                                                                    properties.getFlags(),
                                                                    false,
                                                                    false,
                                                                    properties.getEffectiveFrom(),
                                                                    properties.getEffectiveTo(),
                                                                    null,
                                                                    methodName);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(GovernanceMeasurementsProperties.class.getName(), methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Remove the measurements from the element.
     *
     * @param serverName name of the server instance to connect to
     * @param userId       calling user
     * @param elementGUID  unique identifier of the metadata element to classify
     * @param requestBody external source properties
     *
     * @return void or
     *  InvalidParameterException invalid guid
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    public VoidResponse clearGovernanceMeasurements(String                    serverName,
                                                    String                    userId,
                                                    String                    elementGUID,
                                                    ExternalSourceRequestBody requestBody)
    {
        final String methodName = "clearGovernanceMeasurements";
        final String elementGUIDParameterName = "elementGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ReferenceableHandler<RelatedElement> handler = instanceHandler.getRelatedElementHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.removeGovernanceMeasurementsClassification(userId,
                                                                   requestBody.getExternalSourceGUID(),
                                                                   requestBody.getExternalSourceName(),
                                                                   elementGUID,
                                                                   elementGUIDParameterName,
                                                                   OpenMetadataType.REFERENCEABLE.typeName,
                                                                   false,
                                                                   false,
                                                                   null,
                                                                   methodName);
            }
            else
            {
                handler.removeGovernanceMeasurementsClassification(userId,
                                                                   null,
                                                                   null,
                                                                   elementGUID,
                                                                   elementGUIDParameterName,
                                                                   OpenMetadataType.REFERENCEABLE.typeName,
                                                                   false,
                                                                   false,
                                                                   null,
                                                                   methodName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }
    

    /**
     * Return information about a specific governance metric.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param metricGUID unique identifier for the governance metric
     *
     * @return properties of the governance metric or
     *  InvalidParameterException typeName, documentIdentifier or userId is null; documentIdentifier is not unique; typeName is not valid
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
    */
    public GovernanceMetricResponse getGovernanceMetricByGUID(String serverName,
                                                              String userId,
                                                              String metricGUID)
    {
        final String methodName = "getGovernanceMetricByGUID";
        final String guidParameterName = "metricGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GovernanceMetricResponse response = new GovernanceMetricResponse();
        AuditLog                 auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            GovernanceMetricHandler<GovernanceMetricElement> handler = instanceHandler.getGovernanceMetricHandler(userId, serverName, methodName);

            response.setElement(handler.getGovernanceMetricByGUID(userId,
                                                                  metricGUID,
                                                                  guidParameterName,
                                                                  false,
                                                                  false,
                                                                  new Date(),
                                                                  methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the list of governance metrics for this search string.
     *
     * @param serverName name of the server instance to connect to
     * @param userId the name of the calling user.
     * @param requestBody value to search for (supports wildcards).
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of metrics or
     *  InvalidParameterException typeName, documentIdentifier or userId is null; documentIdentifier is not unique; typeName is not valid
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    public GovernanceMetricListResponse findGovernanceMetrics(String                  serverName,
                                                              String                  userId,
                                                              int                     startFrom,
                                                              int                     pageSize,
                                                              SearchStringRequestBody requestBody)
    {
        final String methodName = "findGovernanceMetrics";
        final String searchStringParameterName = "searchString";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GovernanceMetricListResponse response = new GovernanceMetricListResponse();
        AuditLog                     auditLog = null;

        try
        {
            if (requestBody != null)
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                GovernanceMetricHandler<GovernanceMetricElement> handler = instanceHandler.getGovernanceMetricHandler(userId, serverName, methodName);

                response.setElements(handler.findGovernanceMetrics(userId,
                                                                   requestBody.getSearchString(),
                                                                   searchStringParameterName,
                                                                   startFrom,
                                                                   pageSize,
                                                                   false,
                                                                   false,
                                                                   new Date(),
                                                                   methodName));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }
}
