/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
/* Copyright Contributors to the ODPi Egeria category. */
package org.odpi.openmetadata.viewservices.automatedcuration.server;

import org.odpi.openmetadata.accessservices.assetowner.client.*;
import org.odpi.openmetadata.accessservices.assetowner.metadataelements.ExternalReferenceElement;
import org.odpi.openmetadata.accessservices.assetowner.metadataelements.RelatedElement;
import org.odpi.openmetadata.accessservices.assetowner.metadataelements.ValidValueElement;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementClassification;
import org.odpi.openmetadata.frameworks.integration.properties.CatalogTargetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworkservices.gaf.rest.*;
import org.odpi.openmetadata.frameworkservices.oif.rest.CatalogTargetResponse;
import org.odpi.openmetadata.frameworkservices.oif.rest.CatalogTargetsResponse;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.odpi.openmetadata.viewservices.automatedcuration.properties.CatalogTemplate;
import org.odpi.openmetadata.viewservices.automatedcuration.properties.ResourceDescription;
import org.odpi.openmetadata.viewservices.automatedcuration.properties.TechnologyTypeReport;
import org.odpi.openmetadata.viewservices.automatedcuration.properties.TechnologyTypeSummary;
import org.odpi.openmetadata.viewservices.automatedcuration.rest.TechnologyTypeReportResponse;
import org.odpi.openmetadata.viewservices.automatedcuration.rest.TechnologyTypeSummaryListResponse;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * The AutomatedCurationRESTServices provides the implementation of the Automated Curation Open Metadata View Service (OMVS).
 */

public class AutomatedCurationRESTServices extends TokenController
{
    private static final AutomatedCurationInstanceHandler instanceHandler = new AutomatedCurationInstanceHandler();

    private static final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(AutomatedCurationRESTServices.class),
                                                                            instanceHandler.getServiceName());

    /**
     * Default constructor
     */
    public AutomatedCurationRESTServices()
    {
    }

    /* =====================================================================================================================
     * The technology types provide the reference data to guide the curator.  They are extracted from the valid
     * values for deployedImplementationType
     */

    /**
     * Retrieve the list of deployed implementation type metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public TechnologyTypeSummaryListResponse findTechnologyTypes(String            serverName,
                                                                 boolean           startsWith,
                                                                 boolean           endsWith,
                                                                 boolean           ignoreCase,
                                                                 int               startFrom,
                                                                 int               pageSize,
                                                                 FilterRequestBody requestBody)
    {
        final String methodName = "findTechnologyTypes";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        TechnologyTypeSummaryListResponse response = new TechnologyTypeSummaryListResponse();
        AuditLog                          auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ValidValuesAssetOwner handler = instanceHandler.getValidValuesAssetOwner(userId, serverName, methodName);

            Date effectiveTime = new Date();

            if (requestBody != null)
            {
                effectiveTime = requestBody.getEffectiveTime();
            }

            List<ValidValueElement> validValues = handler.findValidValues(userId,
                                                                          "Egeria:ValidMetadataValue:.*:deployedImplementationType-.*",
                                                                          startFrom,
                                                                          pageSize,
                                                                          effectiveTime);

            if ((requestBody != null) && (requestBody.getFilter() != null) && (! requestBody.getFilter().isBlank()) && (validValues != null))
            {
                List<ValidValueElement> filteredValidValues = new ArrayList<>();
                String searchString = instanceHandler.getSearchString(requestBody.getFilter(), startsWith, endsWith, ignoreCase);

                for (ValidValueElement validValue : validValues)
                {
                    if ((validValue != null) && (validValue.getValidValueProperties() != null) && (validValue.getValidValueProperties().getPreferredValue() != null))
                    {
                        if (validValue.getValidValueProperties().getPreferredValue().matches(searchString))
                        {
                            filteredValidValues.add(validValue);
                        }
                    }
                }

                response.setElements(this.getTechnologySummaries(filteredValidValues));
            }
            else
            {
                response.setElements(this.getTechnologySummaries(validValues));
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
     * Concert valid values in to technology types.
     *
     * @param validValues valid values retrieved from the open metadata ecosystem.
     * @return list of technology type summaries
     */
    private List<TechnologyTypeSummary> getTechnologySummaries(List<ValidValueElement> validValues)
    {
        if (validValues != null)
        {
            List<TechnologyTypeSummary> technologySummaries = new ArrayList<>();

            for (ValidValueElement validValueElement : validValues)
            {
                if ((validValueElement != null) && (validValueElement.getValidValueProperties().getQualifiedName().endsWith(")")))
                {
                    TechnologyTypeSummary technologyTypeSummary = new TechnologyTypeSummary();

                    technologyTypeSummary.setTechnologyTypeGUID(validValueElement.getElementHeader().getGUID());
                    technologyTypeSummary.setQualifiedName(validValueElement.getValidValueProperties().getQualifiedName());
                    technologyTypeSummary.setName(validValueElement.getValidValueProperties().getPreferredValue());
                    technologyTypeSummary.setDescription(validValueElement.getValidValueProperties().getDescription());
                    technologyTypeSummary.setCategory(validValueElement.getValidValueProperties().getCategory());

                    technologySummaries.add(technologyTypeSummary);
                }
            }

            if (! technologySummaries.isEmpty())
            {
                return technologySummaries;
            }
        }

        return null;
    }


    /**
     * Retrieve the list of deployed implementation type metadata elements linked to a particular open metadata type.
     *
     * @param serverName name of the service to route the request to
     * @param typeName does the value start with the supplied string?
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody does the value start with the supplied string?
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */

    public TechnologyTypeSummaryListResponse getTechnologyTypesForOpenMetadataType(String                   serverName,
                                                                                   String                   typeName,
                                                                                   int                      startFrom,
                                                                                   int                      pageSize,
                                                                                   EffectiveTimeRequestBody requestBody)
    {
        final String methodName = "getTechnologyTypesForOpenMetadataType";
        final String parameterName = "typeName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        TechnologyTypeSummaryListResponse response = new TechnologyTypeSummaryListResponse();
        AuditLog                          auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (typeName != null)
            {
                ValidValuesAssetOwner handler = instanceHandler.getValidValuesAssetOwner(userId, serverName, methodName);

                if (requestBody != null)
                {
                    List<ValidValueElement> validValues = handler.findValidValues(userId,
                                                                                  "Egeria:ValidMetadataValue:" + typeName + ":deployedImplementationType-.*",
                                                                                  startFrom,
                                                                                  pageSize,
                                                                                  requestBody.getEffectiveTime());
                    response.setElements(this.getTechnologySummaries(validValues));
                }
                else
                {
                    List<ValidValueElement> validValues = handler.findValidValues(userId,
                                                                                  "Egeria:ValidMetadataValue:" + typeName + ":deployedImplementationType-.*",
                                                                                  startFrom,
                                                                                  pageSize,
                                                                                  new Date());
                    response.setElements(this.getTechnologySummaries(validValues));
                }
            }
            else
            {
                restExceptionHandler.handleMissingValue(parameterName, methodName);
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
     * Retrieve the requested deployed implementation type metadata element. There are no wildcards allowed in the name.
     *
     * @param serverName name of the service to route the request to
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public TechnologyTypeReportResponse getTechnologyTypeDetail(String            serverName,
                                                                FilterRequestBody requestBody)
    {
        final String methodName = "getTechnologyTypeDetail";
        final String parameterName = "requestBody.filter";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        TechnologyTypeReportResponse response = new TechnologyTypeReportResponse();
        AuditLog                     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getFilter() != null)
                {
                    ValidValuesAssetOwner    validValuesHandler = instanceHandler.getValidValuesAssetOwner(userId, serverName, methodName);
                    ExternalReferenceManager externalRefHandler = instanceHandler.getExternalReferenceManager(userId, serverName, methodName);
                    OpenMetadataStoreClient  openHandler        = instanceHandler.getOpenMetadataStoreClient(userId, serverName, methodName);

                    List<ValidValueElement> validValues = validValuesHandler.findValidValues(userId,
                                                                                             "Egeria:ValidMetadataValue:.*:deployedImplementationType-.*",
                                                                                             0,
                                                                                             0,
                                                                                             requestBody.getEffectiveTime());

                    if (validValues != null)
                    {
                        TechnologyTypeReport report = new TechnologyTypeReport();

                        for (ValidValueElement validValueElement : validValues)
                        {
                            if ((validValueElement != null) &&
                                    (validValueElement.getValidValueProperties() != null) &&
                                    (requestBody.getFilter().equals(validValueElement.getValidValueProperties().getPreferredValue())))
                            {
                                report.setTechnologyTypeGUID(validValueElement.getElementHeader().getGUID());
                                report.setQualifiedName(validValueElement.getValidValueProperties().getQualifiedName());
                                report.setName(validValueElement.getValidValueProperties().getPreferredValue());
                                report.setDescription(validValueElement.getValidValueProperties().getDescription());
                                report.setCategory(validValueElement.getValidValueProperties().getCategory());

                                if (validValueElement.getSetGUID() != null)
                                {
                                    ValidValueElement superElement = validValuesHandler.getValidValueByGUID(userId, validValueElement.getSetGUID());

                                    if (superElement != null)
                                    {
                                        report.setTechnologySuperType(superElement.getValidValueProperties().getPreferredValue());
                                    }
                                }

                                List<ValidValueElement> subElements = validValuesHandler.getValidValueSetMembers(userId, validValueElement.getElementHeader().getGUID(), 0, 0);

                                if (subElements != null)
                                {
                                    List<String> subElementNames = new ArrayList<>();

                                    for (ValidValueElement subElement : subElements)
                                    {
                                        if (subElement != null)
                                        {
                                            subElementNames.add(subElement.getValidValueProperties().getPreferredValue());
                                        }
                                    }

                                    report.setTechnologySubtypes(subElementNames);
                                }

                                List<RelatedElement> resourceList = externalRefHandler.getResourceList(userId,
                                                                                                       validValueElement.getElementHeader().getGUID(),
                                                                                                       0, 0);

                                if (resourceList != null)
                                {
                                    List<ResourceDescription> resources = new ArrayList<>();

                                    for (RelatedElement resource : resourceList)
                                    {
                                        if (resource != null)
                                        {
                                            if ((resource.getRelationshipProperties() != null) && (resource.getRelationshipProperties().getExtendedProperties() != null))
                                            {
                                                Map<String, Object> extendedProperties = resource.getRelationshipProperties().getExtendedProperties();

                                                ResourceDescription resourceDescription = new ResourceDescription();

                                                if (extendedProperties.get(OpenMetadataProperty.RESOURCE_USE.name) != null)
                                                {
                                                    resourceDescription.setResourceUse(extendedProperties.get(OpenMetadataProperty.RESOURCE_USE.name).toString());
                                                }
                                                if (extendedProperties.get(OpenMetadataProperty.RESOURCE_USE_DESCRIPTION.name) != null)
                                                {
                                                    resourceDescription.setResourceUseDescription(extendedProperties.get(OpenMetadataProperty.RESOURCE_USE_DESCRIPTION.name).toString());
                                                }

                                                resourceDescription.setRelatedElement(resource.getRelatedElement());
                                                resourceDescription.setSpecification(openHandler.getSpecification(userId, resource.getRelatedElement().getGUID()));

                                                resources.add(resourceDescription);
                                            }
                                        }
                                    }

                                    if (!resources.isEmpty())
                                    {
                                        report.setResourceList(resources);
                                    }
                                }

                                List<RelatedElement> catalogTemplateList = externalRefHandler.getCatalogTemplateList(userId,
                                                                                                                     validValueElement.getElementHeader().getGUID(),
                                                                                                                     0, 0);

                                if (catalogTemplateList != null)
                                {
                                    List<CatalogTemplate> catalogTemplates = new ArrayList<>();

                                    for (RelatedElement templateElement : catalogTemplateList)
                                    {
                                        if (templateElement != null)
                                        {
                                            CatalogTemplate catalogTemplate = new CatalogTemplate();

                                            catalogTemplate.setRelatedElement(templateElement.getRelatedElement());

                                            List<ElementClassification> classifications = templateElement.getRelatedElement().getClassifications();

                                            if (classifications != null)
                                            {
                                                for (ElementClassification classification : classifications)
                                                {
                                                    if (classification != null)
                                                    {
                                                        if (classification.getClassificationName().equals(OpenMetadataType.TEMPLATE_CLASSIFICATION.typeName))
                                                        {
                                                            if (classification.getClassificationProperties() != null)
                                                            {
                                                                if (classification.getClassificationProperties().get(OpenMetadataProperty.NAME.name) != null)
                                                                {
                                                                    catalogTemplate.setName(classification.getClassificationProperties().get(OpenMetadataProperty.NAME.name).toString());
                                                                }
                                                                else if (classification.getClassificationProperties().get(OpenMetadataProperty.DESCRIPTION.name) != null)
                                                                {
                                                                    catalogTemplate.setDescription(classification.getClassificationProperties().get(OpenMetadataProperty.DESCRIPTION.name).toString());
                                                                }
                                                                else if (classification.getClassificationProperties().get(OpenMetadataProperty.VERSION_IDENTIFIER.name) != null)
                                                                {
                                                                    catalogTemplate.setVersionIdentifier(classification.getClassificationProperties().get(OpenMetadataProperty.VERSION_IDENTIFIER.name).toString());
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }

                                            catalogTemplate.setSpecification(openHandler.getSpecification(userId, templateElement.getRelatedElement().getGUID()));

                                            catalogTemplates.add(catalogTemplate);
                                        }
                                    }

                                    if (! catalogTemplates.isEmpty())
                                    {
                                        report.setCatalogTemplates(catalogTemplates);
                                    }
                                }

                                List<ExternalReferenceElement> externalReferenceElements = externalRefHandler.retrieveAttachedExternalReferences(userId,
                                                                                                                                                 validValueElement.getElementHeader().getGUID(),
                                                                                                                                                 0, 0);

                                report.setExternalReferences(externalReferenceElements);

                                break;
                            }
                        }

                        response.setElement(report);
                    }
                }
                else
                {
                    restExceptionHandler.handleMissingValue(parameterName, methodName);
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


    /* =====================================================================================================================
     * Catalog templates make it easy to create new complex object such as assets.
     */

    /**
     * Create a new element from a template.
     *
     * @param serverName name of the service to route the request to
     * @param requestBody information about the template
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createElementFromTemplate(String              serverName,
                                                  TemplateRequestBody requestBody)
    {
        final String methodName = "createElementFromTemplate";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog                     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                OpenMetadataStoreClient openHandler = instanceHandler.getOpenMetadataStoreClient(userId, serverName, methodName);

                response.setGUID(openHandler.createMetadataElementFromTemplate(userId,
                                                                               requestBody.getTypeName(),
                                                                               requestBody.getAnchorGUID(),
                                                                               requestBody.getIsOwnAnchor(),
                                                                               requestBody.getEffectiveFrom(),
                                                                               requestBody.getEffectiveTo(),
                                                                               requestBody.getTemplateGUID(),
                                                                               requestBody.getTemplateProperties(),
                                                                               requestBody.getPlaceholderPropertyValues(),
                                                                               requestBody.getParentGUID(),
                                                                               requestBody.getParentRelationshipTypeName(),
                                                                               requestBody.getParentRelationshipProperties(),
                                                                               requestBody.getParentAtEnd1()));
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


    /* =====================================================================================================================
     * A catalog target links an element (typically an asset, to an integration connector for processing.
     */

    /**
     * Add a catalog target to an integration connector.
     *
     * @param serverName name of the service to route the request to.
     * @param integrationConnectorGUID unique identifier of the integration service.
     * @param metadataElementGUID unique identifier of the metadata element that is a catalog target.
     * @param requestBody properties for the relationship.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the catalog target definition.
     */
    public VoidResponse addCatalogTarget(String                  serverName,
                                         String                  integrationConnectorGUID,
                                         String                  metadataElementGUID,
                                         CatalogTargetProperties requestBody)
    {
        final String methodName = "addCatalogTarget";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog                    auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                OpenIntegrationServiceClient handler = instanceHandler.getOpenIntegrationServiceClient(userId, serverName, methodName);

                handler.addCatalogTarget(userId,
                                         integrationConnectorGUID,
                                         metadataElementGUID,
                                         requestBody);
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
     * Retrieve a specific catalog target associated with an integration connector.
     *
     * @param serverName name of the service to route the request to.
     * @param integrationConnectorGUID unique identifier of the integration service.
     * @param metadataElementGUID unique identifier of the metadata element that is a catalog target.
     *
     * @return details of the governance service and the asset types it is registered for or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the integration connector definition.
     */
    public CatalogTargetResponse getCatalogTarget(String serverName,
                                                  String integrationConnectorGUID,
                                                  String metadataElementGUID)
    {
        final String methodName = "getCatalogTarget";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        CatalogTargetResponse response = new CatalogTargetResponse();
        AuditLog              auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OpenIntegrationServiceClient handler = instanceHandler.getOpenIntegrationServiceClient(userId, serverName, methodName);

            response.setElement(handler.getCatalogTarget(userId, integrationConnectorGUID, metadataElementGUID));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the details of the metadata elements identified as catalog targets with an integration connector.
     *
     * @param serverName name of the service to route the request to.
     * @param integrationConnectorGUID unique identifier of the integration connector.
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of unique identifiers or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the integration connector definition.
     */
    public CatalogTargetsResponse getCatalogTargets(String serverName,
                                                    String integrationConnectorGUID,
                                                    int    startFrom,
                                                    int    pageSize)
    {
        final String methodName = "getCatalogTargets";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        CatalogTargetsResponse response = new CatalogTargetsResponse();
        AuditLog               auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OpenIntegrationServiceClient handler = instanceHandler.getOpenIntegrationServiceClient(userId, serverName, methodName);

            response.setElements(handler.getCatalogTargets(userId, integrationConnectorGUID, startFrom, pageSize));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Unregister a catalog target from the integration connector.
     *
     * @param serverName name of the service to route the request to.
     * @param integrationConnectorGUID unique identifier of the integration connector.
     * @param metadataElementGUID unique identifier of the governance service.
     * @param requestBody null request body.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the integration connector definition.
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse removeCatalogTarget(String          serverName,
                                            String          integrationConnectorGUID,
                                            String          metadataElementGUID,
                                            NullRequestBody requestBody)
    {
        final String methodName = "removeCatalogTarget";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog                    auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OpenIntegrationServiceClient handler = instanceHandler.getOpenIntegrationServiceClient(userId, serverName, methodName);

            handler.removeCatalogTarget(userId, integrationConnectorGUID, metadataElementGUID);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /* =====================================================================================================================
     * A governance action type describes a template to call a single engine action.
     */

    /**
     * Retrieve the list of governance action type metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param startsWith does the value start with the supplied string?
     * @param endsWith does the value end with the supplied string?
     * @param ignoreCase should the search ignore case?
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GovernanceActionTypesResponse findGovernanceActionTypes(String                  serverName,
                                                                   boolean                 startsWith,
                                                                   boolean                 endsWith,
                                                                   boolean                 ignoreCase,
                                                                   int                     startFrom,
                                                                   int                     pageSize,
                                                                   FilterRequestBody       requestBody)
    {
        final String methodName = "findGovernanceActionTypes";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GovernanceActionTypesResponse response = new GovernanceActionTypesResponse();
        AuditLog                      auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            OpenGovernanceClient handler = instanceHandler.getOpenGovernanceClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.findGovernanceActionTypes(userId,
                                                                       instanceHandler.getSearchString(requestBody.getFilter(), startsWith, endsWith, ignoreCase),
                                                                       startFrom,
                                                                       pageSize,
                                                                       requestBody.getEffectiveTime()));
            }
            else
            {
                response.setElements(handler.findGovernanceActionTypes(userId,
                                                                       instanceHandler.getSearchString(null, startsWith, endsWith, ignoreCase),
                                                                       startFrom,
                                                                       pageSize,
                                                                       new Date()));
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
     * Retrieve the list of governance action type metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the service to route the request to
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody name to search for
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GovernanceActionTypesResponse getGovernanceActionTypesByName(String            serverName,
                                                                        int               startFrom,
                                                                        int               pageSize,
                                                                        FilterRequestBody requestBody)
    {
        final String methodName = "getGovernanceActionTypesByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GovernanceActionTypesResponse response = new GovernanceActionTypesResponse();
        AuditLog                             auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            if (requestBody != null)
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                OpenGovernanceClient handler = instanceHandler.getOpenGovernanceClient(userId, serverName, methodName);

                response.setElements(handler.getGovernanceActionTypesByName(userId,
                                                                            requestBody.getFilter(),
                                                                            startFrom,
                                                                            pageSize,
                                                                            requestBody.getEffectiveTime()));
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
     * Retrieve the governance action type metadata element with the supplied unique identifier.
     *
     * @param serverName name of the service to route the request to
     * @param governanceActionTypeGUID unique identifier of the governance action type
     *
     * @return requested metadata element or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GovernanceActionTypeResponse getGovernanceActionTypeByGUID(String serverName,
                                                                      String governanceActionTypeGUID)
    {
        final String methodName = "getGovernanceActionTypeByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GovernanceActionTypeResponse response = new GovernanceActionTypeResponse();
        AuditLog                            auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            OpenGovernanceClient handler = instanceHandler.getOpenGovernanceClient(userId, serverName, methodName);

            response.setElement(handler.getGovernanceActionTypeByGUID(userId, governanceActionTypeGUID));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }



    /* =====================================================================================================================
     * A governance action process describes a well-defined series of steps that gets something done.
     * The steps are defined using GovernanceActionProcessSteps.
     */

    /**
     * Retrieve the list of governance action process metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param startsWith does the value start with the supplied string?
     * @param endsWith does the value end with the supplied string?
     * @param ignoreCase should the search ignore case?
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GovernanceActionProcessElementsResponse findGovernanceActionProcesses(String                  serverName,
                                                                                 boolean                 startsWith,
                                                                                 boolean                 endsWith,
                                                                                 boolean                 ignoreCase,
                                                                                 int                     startFrom,
                                                                                 int                     pageSize,
                                                                                 FilterRequestBody       requestBody)
    {
        final String methodName = "findGovernanceActionProcesses";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GovernanceActionProcessElementsResponse response = new GovernanceActionProcessElementsResponse();
        AuditLog                                auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            OpenGovernanceClient handler = instanceHandler.getOpenGovernanceClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.findGovernanceActionProcesses(userId,
                                                                           instanceHandler.getSearchString(requestBody.getFilter(), startsWith, endsWith, ignoreCase),
                                                                           startFrom,
                                                                           pageSize,
                                                                           requestBody.getEffectiveTime()));
            }
            else
            {
                response.setElements(handler.findGovernanceActionProcesses(userId,
                                                                           instanceHandler.getSearchString(null, startsWith, endsWith, ignoreCase),
                                                                           startFrom,
                                                                           pageSize,
                                                                           new Date()));
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
     * Retrieve the list of governance action process metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the service to route the request to
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody name to search for
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GovernanceActionProcessElementsResponse getGovernanceActionProcessesByName(String            serverName,
                                                                                      int               startFrom,
                                                                                      int               pageSize,
                                                                                      FilterRequestBody requestBody)
    {
        final String methodName = "getGovernanceActionProcessesByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GovernanceActionProcessElementsResponse response = new GovernanceActionProcessElementsResponse();
        AuditLog                                auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            if (requestBody != null)
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                OpenGovernanceClient handler = instanceHandler.getOpenGovernanceClient(userId, serverName, methodName);

                response.setElements(handler.getGovernanceActionProcessesByName(userId,
                                                                                requestBody.getFilter(),
                                                                                startFrom,
                                                                                pageSize,
                                                                                requestBody.getEffectiveTime()));
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
     * Retrieve the governance action process metadata element with the supplied unique identifier.
     *
     * @param serverName name of the service to route the request to
     * @param processGUID unique identifier of the requested metadata element
     *
     * @return requested metadata element or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GovernanceActionProcessElementResponse getGovernanceActionProcessByGUID(String serverName,
                                                                                   String processGUID)
    {
        final String methodName = "getGovernanceActionProcessByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GovernanceActionProcessElementResponse response = new GovernanceActionProcessElementResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            OpenGovernanceClient handler = instanceHandler.getOpenGovernanceClient(userId, serverName, methodName);

            response.setElement(handler.getGovernanceActionProcessByGUID(userId, processGUID));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the governance action process metadata element with the supplied unique identifier
     * along with the flow definition describing its implementation.
     *
     * @param serverName name of the service to route the request to
     * @param processGUID unique identifier of the requested metadata element
     * @param requestBody effectiveTime
     *
     * @return requested metadata element or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GovernanceActionProcessGraphResponse getGovernanceActionProcessGraph(String                   serverName,
                                                                                String                   processGUID,
                                                                                EffectiveTimeRequestBody requestBody)
    {
        final String methodName = "getGovernanceActionProcessGraph";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GovernanceActionProcessGraphResponse response = new GovernanceActionProcessGraphResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            OpenGovernanceClient handler = instanceHandler.getOpenGovernanceClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElement(handler.getGovernanceActionProcessGraph(userId, processGUID, requestBody.getEffectiveTime()));
            }
            else
            {
                response.setElement(handler.getGovernanceActionProcessGraph(userId, processGUID, new Date()));
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /*
     * Engine Actions
     */

    /**
     * Request the status and properties of an executing engine action request.
     *
     * @param serverName     name of server instance to route request to
     * @param engineActionGUID identifier of the engine action request.
     *
     * @return engine action properties and status or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException there was a problem detected by the metadata store.
     */
    public EngineActionElementResponse getEngineAction(String serverName,
                                                       String engineActionGUID)
    {
        final String methodName = "getEngineAction";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        AuditLog auditLog = null;
        EngineActionElementResponse response = new EngineActionElementResponse();

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OpenGovernanceClient handler = instanceHandler.getOpenGovernanceClient(userId, serverName, methodName);

            response.setElement(handler.getEngineAction(userId, engineActionGUID));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Request that an engine action is stopped.
     *
     * @param serverName     name of server instance to route request to
     * @param engineActionGUID identifier of the engine action request.
     *
     * @return engine action properties and status or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException there was a problem detected by the metadata store.
     */
    public VoidResponse cancelEngineAction(String serverName,
                                           String engineActionGUID)
    {
        final String methodName = "cancelEngineAction";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        AuditLog auditLog = null;
        VoidResponse response = new VoidResponse();

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OpenGovernanceClient handler = instanceHandler.getOpenGovernanceClient(userId, serverName, methodName);

            handler.cancelEngineAction(userId, engineActionGUID);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the engine actions that are known to the server.
     *
     * @param serverName     name of server instance to route request to
     * @param startFrom starting from element
     * @param pageSize maximum elements to return
     *
     * @return list of engine action elements or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException there was a problem detected by the metadata store.
     */
    public EngineActionElementsResponse getEngineActions(String serverName,
                                                         int    startFrom,
                                                         int    pageSize)
    {
        final String methodName = "getEngineActions";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        AuditLog auditLog = null;
        EngineActionElementsResponse response = new EngineActionElementsResponse();

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OpenGovernanceClient handler = instanceHandler.getOpenGovernanceClient(userId, serverName, methodName);

            response.setElements(handler.getEngineActions(userId,
                                                          startFrom,
                                                          pageSize));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the engine actions that are still in process.
     *
     * @param serverName     name of server instance to route request to
     * @param startFrom starting from element
     * @param pageSize maximum elements to return
     *
     * @return list of engine action elements or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException there was a problem detected by the metadata store.
     */
    public EngineActionElementsResponse getActiveEngineActions(String serverName,
                                                               int    startFrom,
                                                               int    pageSize)
    {
        final String methodName = "getActiveEngineActions";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        AuditLog auditLog = null;
        EngineActionElementsResponse response = new EngineActionElementsResponse();

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OpenGovernanceClient handler = instanceHandler.getOpenGovernanceClient(userId, serverName, methodName);

            response.setElements(handler.getActiveEngineActions(userId, startFrom, pageSize));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the list of engine action metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param startsWith does the value start with the supplied string?
     * @param endsWith does the value end with the supplied string?
     * @param ignoreCase should the search ignore case?
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public EngineActionElementsResponse findEngineActions(String            serverName,
                                                          boolean           startsWith,
                                                          boolean           endsWith,
                                                          boolean           ignoreCase,
                                                          int               startFrom,
                                                          int               pageSize,
                                                          FilterRequestBody requestBody)
    {
        final String methodName = "findEngineActions";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        EngineActionElementsResponse response = new EngineActionElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            OpenGovernanceClient handler = instanceHandler.getOpenGovernanceClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.findEngineActions(userId,
                                                               instanceHandler.getSearchString(requestBody.getFilter(), startsWith, endsWith, ignoreCase),
                                                               startFrom,
                                                               pageSize));
            }
            else
            {
                response.setElements(handler.findEngineActions(userId,
                                                               instanceHandler.getSearchString(null, startsWith, endsWith, ignoreCase),
                                                               startFrom,
                                                               pageSize));
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
     * Retrieve the list of engine action  metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the service to route the request to
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody name to search for
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public EngineActionElementsResponse getEngineActionsByName(String            serverName,
                                                               int               startFrom,
                                                               int               pageSize,
                                                               FilterRequestBody requestBody)
    {
        final String methodName = "getEngineActionsByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        EngineActionElementsResponse response = new EngineActionElementsResponse();
        AuditLog                     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            if (requestBody != null)
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                OpenGovernanceClient handler = instanceHandler.getOpenGovernanceClient(userId, serverName, methodName);

                response.setElements(handler.getEngineActionsByName(userId,
                                                                    requestBody.getFilter(),
                                                                    startFrom,
                                                                    pageSize));
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


    /* =====================================================================================================================
     * Execute governance actions
     */

    /**
     * Create a governance action in the metadata store which will trigger the governance action service
     * associated with the supplied request type.  The governance action remains to act as a record
     * of the actions taken for auditing.
     *
     * @param serverName     name of server instance to route request to
     * @param governanceEngineName name of the governance engine that should execute the request
     * @param requestBody properties for the governance action and to pass to the governance action service
     *
     * @return unique identifier of the governance action or
     *  InvalidParameterException null qualified name
     *  UserNotAuthorizedException this governance action service is not authorized to create a governance action
     *  PropertyServerException there is a problem with the metadata store
     */
    public GUIDResponse initiateEngineAction(String                          serverName,
                                             String                          governanceEngineName,
                                             InitiateEngineActionRequestBody requestBody)
    {
        final String methodName = "initiateEngineAction";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        AuditLog auditLog = null;
        GUIDResponse response = new GUIDResponse();

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                OpenGovernanceClient handler = instanceHandler.getOpenGovernanceClient(userId, serverName, methodName);

                response.setGUID(handler.initiateEngineAction(userId,
                                                              requestBody.getQualifiedName(),
                                                              requestBody.getDomainIdentifier(),
                                                              requestBody.getDisplayName(),
                                                              requestBody.getDescription(),
                                                              requestBody.getRequestSourceGUIDs(),
                                                              requestBody.getActionTargets(),
                                                              requestBody.getReceivedGuards(),
                                                              requestBody.getStartDate(),
                                                              governanceEngineName,
                                                              requestBody.getRequestType(),
                                                              requestBody.getRequestParameters(),
                                                              requestBody.getProcessName(),
                                                              requestBody.getRequestSourceName(),
                                                              requestBody.getOriginatorServiceName(),
                                                              requestBody.getOriginatorEngineName()));
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
     * Using the named governance action type as a template, initiate an engine action.
     *
     * @param serverName     name of server instance to route request to
     * @param requestBody properties to initiate the new instance of the engine action
     *
     * @return unique identifier of the first governance action of the process or
     *  InvalidParameterException null or unrecognized qualified name of the process
     *  UserNotAuthorizedException this governance action service is not authorized to create a governance action process
     *  PropertyServerException there is a problem with the metadata store
     */
    public GUIDResponse initiateGovernanceActionType(String                          serverName,
                                                     InitiateGovernanceActionTypeRequestBody requestBody)
    {
        final String methodName = "initiateGovernanceActionType";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        AuditLog auditLog = null;
        GUIDResponse response = new GUIDResponse();

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                OpenGovernanceClient handler = instanceHandler.getOpenGovernanceClient(userId, serverName, methodName);

                response.setGUID(handler.initiateGovernanceActionType(userId,
                                                                      requestBody.getGovernanceActionTypeQualifiedName(),
                                                                      requestBody.getRequestSourceGUIDs(),
                                                                      requestBody.getActionTargets(),
                                                                      requestBody.getStartDate(),
                                                                      requestBody.getRequestParameters(),
                                                                      requestBody.getOriginatorServiceName(),
                                                                      requestBody.getOriginatorEngineName()));
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
     * Using the named governance action process as a template, initiate a chain of governance actions.
     *
     * @param serverName     name of server instance to route request to
     * @param requestBody properties to initiate the new instance of the process
     *
     * @return unique identifier of the first governance action of the process or
     *  InvalidParameterException null or unrecognized qualified name of the process
     *  UserNotAuthorizedException this governance action service is not authorized to create a governance action process
     *  PropertyServerException there is a problem with the metadata store
     */
    public GUIDResponse initiateGovernanceActionProcess(String                             serverName,
                                                        InitiateGovernanceActionProcessRequestBody requestBody)
    {
        final String methodName = "initiateGovernanceActionProcess";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        AuditLog auditLog = null;
        GUIDResponse response = new GUIDResponse();

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                OpenGovernanceClient handler = instanceHandler.getOpenGovernanceClient(userId, serverName, methodName);

                response.setGUID(handler.initiateGovernanceActionProcess(userId,
                                                                         requestBody.getProcessQualifiedName(),
                                                                         requestBody.getRequestSourceGUIDs(),
                                                                         requestBody.getActionTargets(),
                                                                         requestBody.getStartDate(),
                                                                         requestBody.getRequestParameters(),
                                                                         requestBody.getOriginatorServiceName(),
                                                                         requestBody.getOriginatorEngineName()));
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
