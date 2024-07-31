/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.client;

import org.odpi.openmetadata.accessservices.governanceprogram.api.GovernanceMetricsInterface;
import org.odpi.openmetadata.accessservices.governanceprogram.client.rest.GovernanceProgramRESTClient;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.GovernanceMetricElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.GovernanceDefinitionMetricProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.GovernanceExpectationsProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.GovernanceMeasurementsDataSetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.GovernanceMeasurementsProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.GovernanceMetricProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.GovernanceResultsProperties;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.List;

/**
 * GovernanceMetricsManager is the java client for managing governance metrics and their links to all types of governance definitions.
 */
public class GovernanceMetricsManager extends GovernanceProgramBaseClient implements GovernanceMetricsInterface
{
    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     *
     * @throws InvalidParameterException bad input parameters
     */
    public GovernanceMetricsManager(String serverName,
                                    String serverPlatformURLRoot) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     *
     * @throws InvalidParameterException bad input parameters
     */
    public GovernanceMetricsManager(String     serverName,
                                    String     serverPlatformURLRoot,
                                    String     userId,
                                    String     password) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password);
    }



    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param maxPageSize pre-initialized parameter limit
     * @param auditLog logging destination
     *
     * @throws InvalidParameterException bad input parameters
     */
    public GovernanceMetricsManager(String   serverName,
                                    String   serverPlatformURLRoot,
                                    int      maxPageSize,
                                    AuditLog auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, maxPageSize, auditLog);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @param maxPageSize pre-initialized parameter limit
     * @param auditLog logging destination
     *
     * @throws InvalidParameterException bad input parameters
     */
    public GovernanceMetricsManager(String     serverName,
                                    String     serverPlatformURLRoot,
                                    String     userId,
                                    String     password,
                                    int        maxPageSize,
                                    AuditLog   auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password, maxPageSize, auditLog);
    }


    /**
     * Create a new client that uses the supplied rest client.  This is typically used when called from another OMAG Server.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param restClient internal client for rest calls
     * @param maxPageSize pre-initialized parameter limit
     *
     * @throws InvalidParameterException bad input parameters
     */
    public GovernanceMetricsManager(String                      serverName,
                                    String                      serverPlatformURLRoot,
                                    GovernanceProgramRESTClient restClient,
                                    int                         maxPageSize) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, restClient, maxPageSize);
    }


    /**
     * Create a new governance metric.
     *
     * @param userId calling user
     * @param metricProperties properties of the metric
     *
     * @return unique identifier of the metric
     *
     * @throws InvalidParameterException typeName, documentIdentifier or userId is null; documentIdentifier is not unique; typeName is not valid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public String createGovernanceMetric(String                     userId,
                                         GovernanceMetricProperties metricProperties) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        final String methodName = "createGovernanceMetric";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/governance-metrics";
        final String propertiesParameterName = "metricProperties";

        return super.createReferenceable(userId, metricProperties, propertiesParameterName, urlTemplate, methodName);
    }


    /**
     * Update an existing governance metric.
     *
     * @param userId calling user
     * @param metricGUID unique identifier of the metric to update
     * @param isMergeUpdate are unspecified properties unchanged (true) or replaced with null?
     * @param metricProperties properties to update
     *
     * @throws InvalidParameterException guid, documentIdentifier or userId is null; documentIdentifier is not unique; guid is not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void  updateGovernanceMetric(String                     userId,
                                        String                     metricGUID,
                                        boolean                    isMergeUpdate,
                                        GovernanceMetricProperties metricProperties) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        final String methodName = "updateGovernanceMetric";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/governance-metrics/{2}/update?isMergeUpdate={4}";

        final String guidParameterName = "metricGUID";
        final String propertiesParameterName = "metricProperties";

        super.updateReferenceable(userId, metricGUID, guidParameterName, isMergeUpdate, metricProperties, propertiesParameterName, urlTemplate, methodName);
    }


    /**
     * Delete a specific governance metric.
     *
     * @param userId calling user
     * @param metricGUID unique identifier of the metric to remove
     *
     * @throws InvalidParameterException guid is null or not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void  deleteGovernanceMetric(String userId,
                                        String metricGUID) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        final String methodName = "deleteExternalReference";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/governance-metrics/{2}/delete";
        final String guidParameterName = "metricGUID";

        super.removeReferenceable(userId, metricGUID, guidParameterName, urlTemplate, methodName);
    }


    /**
     * Create a link to show that a governance metric supports the requirements of one of the governance policies.
     * If the link already exists the rationale is updated.
     *
     * @param userId calling user
     * @param metricGUID unique identifier of the governance metric
     * @param governanceDefinitionGUID unique identifier of the governance definition
     * @param rationale description of how the metric supports the metric
     *
     * @throws InvalidParameterException one of the guids is null or not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void setupGovernanceDefinitionMetric(String                               userId,
                                                String                               metricGUID,
                                                String                               governanceDefinitionGUID,
                                                GovernanceDefinitionMetricProperties rationale) throws InvalidParameterException,
                                                                                                       UserNotAuthorizedException,
                                                                                                       PropertyServerException
    {
        final String methodName = "setupGovernanceDefinitionMetric";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/governance-metrics/{2}/governance-definitions/{3}/link";

        final String metricGUIDParameterName = "metricGUID";
        final String governanceDefinitionGUIDParameterName = "governanceDefinitionGUID";

        super.setupRelationship(userId, metricGUID, metricGUIDParameterName, null, rationale, governanceDefinitionGUID,governanceDefinitionGUIDParameterName, urlTemplate, methodName);
    }


    /**
     * Remove the link between a governance metric and a governance definition.
     *
     * @param userId calling user
     * @param metricGUID unique identifier of the governance metric
     * @param governanceDefinitionGUID unique identifier of the governance definition
     *
     * @throws InvalidParameterException one of the guids is null or not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void clearGovernanceDefinitionMetric(String userId,
                                                String metricGUID,
                                                String governanceDefinitionGUID) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        final String methodName = "clearGovernanceDefinitionMetric";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/governance-metrics/{2}/governance-definitions/{3}/unlink";

        final String metricGUIDParameterName = "metricGUID";
        final String governanceDefinitionGUIDParameterName = "governanceDefinitionGUID";

        super.clearRelationship(userId, metricGUID, metricGUIDParameterName, null, governanceDefinitionGUID, governanceDefinitionGUIDParameterName, urlTemplate, methodName);
    }


    /**
     * Create a link to show which data set holds the measurements for a data set.
     *
     * @param userId calling user
     * @param metricGUID unique identifier of the governance metric
     * @param dataSetGUID unique identifier of the governance definition
     * @param properties description of how the data set supports the metric
     *
     * @throws InvalidParameterException one of the guids is null or not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void setupGovernanceResults(String                      userId,
                                       String                      metricGUID,
                                       String                      dataSetGUID,
                                       GovernanceResultsProperties properties) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        final String methodName = "setupGovernanceResults";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/governance-metrics/{2}/governance-results/{3}/link";

        final String metricGUIDParameterName = "metricGUID";
        final String dataSetGUIDParameterName = "dataSetGUID";

        super.setupRelationship(userId, metricGUID, metricGUIDParameterName, null, properties, dataSetGUID, dataSetGUIDParameterName, urlTemplate, methodName);
    }


    /**
     * Remove the link between a governance metric and a data set.
     *
     * @param userId calling user
     * @param metricGUID unique identifier of the governance metric
     * @param dataSetGUID unique identifier of the data set
     *
     * @throws InvalidParameterException one of the guids is null or not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void clearGovernanceResults(String userId,
                                       String metricGUID,
                                       String dataSetGUID) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        final String methodName = "clearGovernanceResults";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/governance-metrics/{2}/governance-results/{3}/unlink";

        final String metricGUIDParameterName = "metricGUID";
        final String governanceDefinitionGUIDParameterName = "dataSetGUID";

        clearRelationship(userId, metricGUID, metricGUIDParameterName, null, dataSetGUID, governanceDefinitionGUIDParameterName, urlTemplate, methodName);
    }


    /**
     * Classify the data set to indicate that contains governance measurements.
     *
     * @param userId        calling user
     * @param dataSetGUID  unique identifier of the metadata element to classify
     * @param properties    properties of the data set's measurements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void setGovernanceMeasurementsDataSet(String                                  userId,
                                                 String                                  dataSetGUID,
                                                 GovernanceMeasurementsDataSetProperties properties) throws InvalidParameterException,
                                                                                                            UserNotAuthorizedException,
                                                                                                            PropertyServerException
    {
        final String methodName = "setGovernanceMeasurementsDataSet";
        final String guidParameter = "dataSetGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/data-sets/{2}/classify-as-governance-measurements-set";

        super.setReferenceableClassification(userId, dataSetGUID, guidParameter, properties, urlTemplate, methodName);
    }


    /**
     * Remove the governance data designation from the data set.
     *
     * @param userId       calling user
     * @param dataSetGUID  unique identifier of the metadata element to classify
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearGovernanceMeasurementsDataSet(String userId,
                                                   String dataSetGUID) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        final String methodName = "clearGovernanceMeasurementsDataSet";
        final String guidParameter = "dataSetGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/data-sets/{2}/classify-as-governance-measurements-set/delete";

        super.removeReferenceableClassification(userId, dataSetGUID, guidParameter, urlTemplate, methodName);
    }


    /**
     * Classify the element to indicate the expected values of the governance measurements. Can be used to create or update the values.
     *
     * @param userId        calling user
     * @param elementGUID  unique identifier of the metadata element to classify
     * @param properties    properties of the data set's measurements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void setGovernanceExpectations(String                           userId,
                                          String                           elementGUID,
                                          GovernanceExpectationsProperties properties) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException
    {
        final String methodName = "setGovernanceExpectations";
        final String guidParameter = "elementGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/elements/{2}/classify-with-governance-expectations";

        super.setReferenceableClassification(userId, elementGUID, guidParameter, properties, urlTemplate, methodName);
    }


    /**
     * Remove the governance expectations classification from the element.
     *
     * @param userId       calling user
     * @param elementGUID  unique identifier of the metadata element to classify
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearGovernanceExpectations(String userId,
                                            String elementGUID) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        final String methodName = "clearGovernanceExpectations";
        final String guidParameter = "elementGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/elements/{2}/classify-with-governance-expectations/delete";

        super.removeReferenceableClassification(userId, elementGUID, guidParameter, urlTemplate, methodName);
    }



    /**
     * Classify the element with relevant governance measurements. Can be used to create or update the values.
     *
     * @param userId        calling user
     * @param elementGUID  unique identifier of the metadata element to classify
     * @param properties    properties of the data set's measurements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void setGovernanceMeasurements(String                           userId,
                                          String                           elementGUID,
                                          GovernanceMeasurementsProperties properties) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException
    {
        final String methodName = "setGovernanceMeasurements";
        final String guidParameter = "elementGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/elements/{2}/classify-with-governance-measurements";

        super.setReferenceableClassification(userId, elementGUID, guidParameter, properties, urlTemplate, methodName);
    }


    /**
     * Remove the measurements from the element.
     *
     * @param userId       calling user
     * @param elementGUID  unique identifier of the metadata element to classify
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearGovernanceMeasurements(String userId,
                                            String elementGUID) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        final String methodName = "clearGovernanceMeasurements";
        final String guidParameter = "elementGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/elements/{2}/classify-with-governance-measurements/delete";

        super.removeReferenceableClassification(userId, elementGUID, guidParameter, urlTemplate, methodName);
    }


    /**
     * Return information about a specific governance metric.
     *
     * @param userId calling user
     * @param metricGUID unique identifier for the governance metric
     *
     * @return properties of the governance metric
     *
     * @throws InvalidParameterException metricGUID or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public GovernanceMetricElement getGovernanceMetricByGUID(String userId,
                                                             String metricGUID) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        final String methodName = "getGovernanceMetricByGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/governance-metrics/{2}";

        final String guidParameterName = "metricGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(metricGUID, guidParameterName, methodName);

        GovernanceMetricResponse restResult = restClient.callGovernanceMetricGetRESTCall(methodName,
                                                                                         urlTemplate,
                                                                                         serverName,
                                                                                         userId,
                                                                                         metricGUID);

        return restResult.getElement();
    }


    /**
     * Retrieve the list of governance metrics for this search string.
     *
     * @param userId the name of the calling user.
     * @param searchString value to search for (supports wildcards).
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of metrics
     *
     * @throws InvalidParameterException guid invalid or the search parameter is not correctly specified, or is null.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @Override
    public List<GovernanceMetricElement> findGovernanceMetrics(String userId,
                                                               String searchString,
                                                               int    startFrom,
                                                               int    pageSize) throws InvalidParameterException,
                                                                                       PropertyServerException,
                                                                                       UserNotAuthorizedException
    {
        final String methodName = "findGovernanceMetrics";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/governance-metrics/by-search-string?startFrom={2}&pageSize={3}";
        final String searchStringParameterName = "searchString";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, searchStringParameterName, methodName);

        int queryPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        SearchStringRequestBody requestBody = new SearchStringRequestBody();
        requestBody.setSearchString(searchString);
        requestBody.setSearchStringParameterName(searchStringParameterName);

        GovernanceMetricsResponse restResult = restClient.callGovernanceMetricListPostRESTCall(methodName,
                                                                                               urlTemplate,
                                                                                               requestBody,
                                                                                               serverName,
                                                                                               userId,
                                                                                               startFrom,
                                                                                               queryPageSize);

        return restResult.getElements();
    }
}
