/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.governanceprogram.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.governanceprogram.rest.ClassificationRequestBody;
import org.odpi.openmetadata.accessservices.governanceprogram.rest.ExternalSourceRequestBody;
import org.odpi.openmetadata.accessservices.governanceprogram.rest.GovernanceMetricListResponse;
import org.odpi.openmetadata.accessservices.governanceprogram.rest.GovernanceMetricResponse;
import org.odpi.openmetadata.accessservices.governanceprogram.rest.ReferenceableRequestBody;
import org.odpi.openmetadata.accessservices.governanceprogram.rest.RelationshipRequestBody;
import org.odpi.openmetadata.accessservices.governanceprogram.server.GovernanceMetricsRESTServices;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * GovernanceMetricsResource sets up the governance metrics that are part of an organization governance.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/governance-program/users/{userId}")

@Tag(name="Governance Program OMAS",
     description="The Governance Program OMAS provides APIs and events for tools and applications focused on defining a data strategy, planning support for a regulation and/or developing a governance program for the data landscape.",
     externalDocs=@ExternalDocumentation(description="Governance Program Open Metadata Access Service (OMAS)",
                                         url="https://egeria-project.org/services/omas/governance-program/overview/"))

public class GovernanceMetricsResource
{
    private final GovernanceMetricsRESTServices restAPI = new GovernanceMetricsRESTServices();

    /**
     * Default constructor
     */
    public GovernanceMetricsResource()
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
    @PostMapping(path = "/governance-metrics")

    public GUIDResponse createGovernanceMetric(@PathVariable String                   serverName,
                                               @PathVariable String                   userId,
                                               @RequestBody  ReferenceableRequestBody requestBody)
    {
        return restAPI.createGovernanceMetric(serverName, userId, requestBody);
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
    @PostMapping(path = "/governance-metrics/{metricGUID}/update")

    public VoidResponse updateGovernanceMetric(@PathVariable String                   serverName,
                                               @PathVariable String                   userId,
                                               @PathVariable String                   metricGUID,
                                               @RequestParam boolean                  isMergeUpdate,
                                               @RequestBody  ReferenceableRequestBody requestBody)
    {
        return restAPI.updateGovernanceMetric(serverName, userId, metricGUID, isMergeUpdate, requestBody);
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
    @PostMapping(path = "/governance-metrics/{metricGUID}/delete")

    public VoidResponse  deleteGovernanceMetric(@PathVariable String                    serverName,
                                                @PathVariable String                    userId,
                                                @PathVariable String                    metricGUID,
                                                @RequestBody  ExternalSourceRequestBody requestBody)
    {
        return restAPI.deleteGovernanceMetric(serverName, userId, metricGUID, requestBody);
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
    @PostMapping(path = "/governance-metrics/{metricGUID}/governance-definitions/{governanceDefinitionGUID}/link")

    public VoidResponse setupGovernanceDefinitionMetric(@PathVariable String                  serverName,
                                                        @PathVariable String                  userId,
                                                        @PathVariable String                  metricGUID,
                                                        @PathVariable String                  governanceDefinitionGUID,
                                                        @RequestBody  RelationshipRequestBody requestBody)
    {
        return restAPI.setupGovernanceDefinitionMetric(serverName, userId, metricGUID, governanceDefinitionGUID, requestBody);
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
    @PostMapping(path = "/governance-metrics/{metricGUID}/governance-definitions/{governanceDefinitionGUID}/unlink")

    public VoidResponse clearGovernanceDefinitionMetric(@PathVariable String                  serverName,
                                                        @PathVariable String                  userId,
                                                        @PathVariable String                  metricGUID,
                                                        @PathVariable String                  governanceDefinitionGUID,
                                                        @RequestBody  RelationshipRequestBody requestBody)
    {
        return restAPI.clearGovernanceDefinitionMetric(serverName, userId, metricGUID, governanceDefinitionGUID, requestBody);
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
    @PostMapping(path = "/governance-metrics/{metricGUID}/governance-results/{dataSetGUID}/link")

    public VoidResponse setupGovernanceResults(@PathVariable String                  serverName,
                                               @PathVariable String                  userId,
                                               @PathVariable String                  metricGUID,
                                               @PathVariable String                  dataSetGUID,
                                               @RequestBody  RelationshipRequestBody requestBody)
    {
        return restAPI.setupGovernanceResults(serverName, userId, metricGUID, dataSetGUID, requestBody);
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
    @PostMapping(path = "/governance-metrics/{metricGUID}/governance-results/{dataSetGUID}/unlink")

    public VoidResponse clearGovernanceResults(@PathVariable String                  serverName,
                                               @PathVariable String                  userId,
                                               @PathVariable String                  metricGUID,
                                               @PathVariable String                  dataSetGUID,
                                               @RequestBody  RelationshipRequestBody requestBody)
    {
        return restAPI.clearGovernanceResults(serverName, userId, metricGUID, dataSetGUID, requestBody);
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
    @PostMapping(path = "/data-sets/{dataSetGUID}/classify-as-governance-measurements-set")

    public VoidResponse setGovernanceMeasurementsDataSet(@PathVariable String                    serverName,
                                                         @PathVariable String                    userId,
                                                         @PathVariable String                    dataSetGUID,
                                                         @RequestBody  ClassificationRequestBody requestBody)
    {
        return restAPI.setGovernanceMeasurementsDataSet(serverName, userId, dataSetGUID, requestBody);
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
    @PostMapping(path = "/data-sets/{dataSetGUID}/classify-as-governance-measurements-set/delete")

    public VoidResponse clearGovernanceMeasurementsDataSet(@PathVariable String                    serverName,
                                                           @PathVariable String                    userId,
                                                           @PathVariable String                    dataSetGUID,
                                                           @RequestBody  ExternalSourceRequestBody requestBody)
    {
        return restAPI.clearGovernanceMeasurementsDataSet(serverName, userId, dataSetGUID, requestBody);
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
    @PostMapping(path = "/elements/{elementGUID}/classify-with-governance-expectations")

    public VoidResponse setGovernanceExpectations(@PathVariable String                    serverName,
                                                  @PathVariable String                    userId,
                                                  @PathVariable String                    elementGUID,
                                                  @RequestBody  ClassificationRequestBody requestBody)
    {
        return restAPI.setGovernanceExpectations(serverName, userId, elementGUID, requestBody);
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
    @PostMapping(path = "/elements/{elementGUID}/classify-with-governance-expectations/delete")

    public VoidResponse clearGovernanceExpectations(@PathVariable String                    serverName,
                                                    @PathVariable String                    userId,
                                                    @PathVariable String                    elementGUID,
                                                    @RequestBody  ExternalSourceRequestBody requestBody)
    {
        return restAPI.clearGovernanceExpectations(serverName, userId, elementGUID, requestBody);
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
    @PostMapping(path = "/elements/{elementGUID}/classify-with-governance-measurements")

    public VoidResponse setGovernanceMeasurements(@PathVariable String                    serverName,
                                                  @PathVariable String                    userId,
                                                  @PathVariable String                    elementGUID,
                                                  @RequestBody  ClassificationRequestBody requestBody)
    {
        return restAPI.setGovernanceMeasurements(serverName, userId, elementGUID, requestBody);
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
    @PostMapping(path = "/elements/{elementGUID}/classify-with-governance-measurements/delete")

    public VoidResponse clearGovernanceMeasurements(@PathVariable String                    serverName,
                                                    @PathVariable String                    userId,
                                                    @PathVariable String                    elementGUID,
                                                    @RequestBody  ExternalSourceRequestBody requestBody)
    {
        return restAPI.clearGovernanceMeasurements(serverName, userId, elementGUID, requestBody);
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
    @GetMapping(path = "/governance-metrics/{metricGUID}")

    public GovernanceMetricResponse getGovernanceMetricByGUID(@PathVariable String serverName,
                                                              @PathVariable String userId,
                                                              @PathVariable String metricGUID)
    {
        return restAPI.getGovernanceMetricByGUID(serverName, userId, metricGUID);
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
    @PostMapping(path = "/governance-metrics/by-search-string")

    public GovernanceMetricListResponse findGovernanceMetrics(@PathVariable String                  serverName,
                                                              @PathVariable String                  userId,
                                                              @RequestParam int                     startFrom,
                                                              @RequestParam int                     pageSize,
                                                              @RequestBody  SearchStringRequestBody requestBody)
    {
        return restAPI.findGovernanceMetrics(serverName, userId, startFrom, pageSize, requestBody);
    }
}
