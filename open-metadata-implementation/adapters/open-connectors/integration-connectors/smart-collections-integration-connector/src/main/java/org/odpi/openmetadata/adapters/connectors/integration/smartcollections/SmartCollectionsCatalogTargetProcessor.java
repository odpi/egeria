/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.smartcollections;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.adapters.connectors.integration.smartcollections.ffdc.SmartCollectionsAuditCode;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCRESTClientBase;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.integration.connectors.CatalogTargetProcessorBase;
import org.odpi.openmetadata.frameworks.integration.context.CatalogTargetContext;
import org.odpi.openmetadata.frameworks.opengovernance.properties.CatalogTarget;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.AssetClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.CollectionClient;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.SavedQueryProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.ResultsSetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * SmartCollectionsCatalogTargetProcessor maintains the membership of a single Results Set collection linked as a catalog target.
 * It navigates the SmartQuery relationship to find the SavedQuery that determines the results set's membership, issues the
 * query's REST call and adjusts the results set's membership to match the elements returned.
 */
public class SmartCollectionsCatalogTargetProcessor extends CatalogTargetProcessorBase
{
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final FFDCRESTClientBase restClient;


    /**
     * Primary constructor
     *
     * @param template object to copy
     * @param catalogTargetContext specialized context for this catalog target
     * @param connectorToTarget connector to access the target resource
     * @param connectorName name of this integration connector
     * @param restClient REST client used to issue the saved query's REST call
     * @param auditLog logging destination
     */
    public SmartCollectionsCatalogTargetProcessor(CatalogTarget        template,
                                                  CatalogTargetContext catalogTargetContext,
                                                  Connector            connectorToTarget,
                                                  String               connectorName,
                                                  FFDCRESTClientBase   restClient,
                                                  AuditLog             auditLog)
    {
        super(template, catalogTargetContext, connectorToTarget, connectorName, auditLog);

        this.restClient = restClient;
    }


    /**
     * Requests that the connector refreshes the membership of the results set to match the current results of its saved query.
     *
     * @throws ConnectorCheckedException the connector detected a problem.
     */
    @Override
    public void refresh() throws ConnectorCheckedException
    {
        final String methodName = "refresh";

        if (! propertyHelper.isTypeOf(super.getCatalogTargetElement().getElementHeader(), OpenMetadataType.RESULTS_SET.typeName))
        {
            super.throwWrongTypeOfCatalogTarget(OpenMetadataType.RESULTS_SET.typeName, methodName);
            return;
        }

        String           resultsSetGUID  = super.getCatalogTargetElement().getElementHeader().getGUID();
        CollectionClient collectionClient = integrationContext.getCollectionClient();

        auditLog.logMessage(methodName, SmartCollectionsAuditCode.REFRESHING_RESULTS_SET.getMessageDefinition(connectorName, resultsSetGUID));

        Date                 startTime          = new Date();
        Date                 createdTime        = null;
        String               completionMessage  = null;
        ResultsSetProperties fetchedProperties  = null;
        boolean              succeeded          = false;

        try
        {
            OpenMetadataRootElement resultsSetElement = collectionClient.getCollectionByGUID(resultsSetGUID, collectionClient.getGetOptions());

            if (resultsSetElement.getProperties() instanceof ResultsSetProperties resultsSetProperties)
            {
                fetchedProperties = resultsSetProperties;
                createdTime       = resultsSetProperties.getCreatedTime();
            }

            List<RelatedMetadataElementSummary> savedQueries = resultsSetElement.getPopulatedUsingQuery();

            if ((savedQueries == null) || (savedQueries.size() != 1))
            {
                int savedQueryCount = (savedQueries == null) ? 0 : savedQueries.size();

                auditLog.logMessage(methodName, SmartCollectionsAuditCode.WRONG_NUMBER_OF_SAVED_QUERIES.getMessageDefinition(connectorName,
                                                                                                                              Integer.toString(savedQueryCount),
                                                                                                                              resultsSetGUID));
            }
            else
            {
                String savedQueryGUID = savedQueries.get(0).getRelatedElement().getElementHeader().getGUID();

                AssetClient savedQueryClient = integrationContext.getAssetClient(OpenMetadataType.SAVED_QUERY.typeName);

                OpenMetadataRootElement savedQueryElement = savedQueryClient.getAssetByGUID(savedQueryGUID, savedQueryClient.getGetOptions());

                if ((savedQueryElement != null) && (savedQueryElement.getProperties() instanceof SavedQueryProperties savedQueryProperties) &&
                        (savedQueryProperties.getQueryURL() != null))
                {
                    refreshMembership(resultsSetGUID, savedQueryProperties, collectionClient, methodName);
                }
            }

            succeeded = true;
        }
        catch (Exception error)
        {
            completionMessage = error.getMessage();

            auditLog.logException(methodName,
                                  SmartCollectionsAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                       error.getClass().getName(),
                                                                                                       methodName,
                                                                                                       resultsSetGUID,
                                                                                                       error.getMessage()),
                                  error);
        }

        Date completionTime = new Date();

        try
        {
            if (succeeded && (fetchedProperties != null))
            {
                /*
                 * A full replace is used on success so that a completionMessage left over from a previous, failed
                 * refresh is cleared: a merge update would leave a null completionMessage untouched.
                 */
                fetchedProperties.setCreatedTime((createdTime != null) ? createdTime : startTime);
                fetchedProperties.setStartTime(startTime);
                fetchedProperties.setCompletionTime(completionTime);
                fetchedProperties.setCompletionMessage(null);

                collectionClient.updateCollection(resultsSetGUID, collectionClient.getUpdateOptions(false), fetchedProperties);
            }
            else
            {
                ResultsSetProperties statusProperties = new ResultsSetProperties();

                if (createdTime != null)
                {
                    statusProperties.setCreatedTime(createdTime);
                }

                statusProperties.setStartTime(startTime);
                statusProperties.setCompletionTime(completionTime);
                statusProperties.setCompletionMessage((completionMessage != null) ? completionMessage : "Refresh did not complete");

                collectionClient.updateCollection(resultsSetGUID, collectionClient.getUpdateOptions(true), statusProperties);
            }
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  SmartCollectionsAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                       error.getClass().getName(),
                                                                                                       methodName,
                                                                                                       resultsSetGUID,
                                                                                                       error.getMessage()),
                                  error);
        }
    }


    /**
     * Issue the saved query's REST call and adjust the results set's membership to match the elements returned.
     *
     * @param resultsSetGUID unique identifier of the results set
     * @param savedQueryProperties properties of the saved query attached to the results set
     * @param collectionClient client used to maintain the results set's membership
     * @param methodName calling method
     */
    private void refreshMembership(String               resultsSetGUID,
                                   SavedQueryProperties savedQueryProperties,
                                   CollectionClient     collectionClient,
                                   String               methodName) throws Exception
    {
        Object requestBody = null;

        if ((savedQueryProperties.getQueryRequestBody() != null) && (! savedQueryProperties.getQueryRequestBody().isBlank()))
        {
            requestBody = objectMapper.readValue(savedQueryProperties.getQueryRequestBody(), Object.class);
        }

        QueryResponse queryResponse = restClient.callPostRESTCallNoParams(methodName,
                                                                          QueryResponse.class,
                                                                          savedQueryProperties.getQueryURL(),
                                                                          requestBody);

        /*
         * The target REST API may itself return an FFDC-style error response (relatedHTTPCode != 200) with an HTTP
         * 200/4xx/5xx status that still deserializes successfully into QueryResponse (its "elements" field is simply
         * absent).  This must be treated as a failure, not as "the query legitimately returned no elements", otherwise
         * a broken saved query would silently empty out the results set's membership.
         */
        if ((queryResponse != null) && (queryResponse.getRelatedHTTPCode() != 200))
        {
            throw new Exception(queryResponse.getExceptionErrorMessage());
        }

        Set<String> queryResultGUIDs = new HashSet<>();

        if ((queryResponse != null) && (queryResponse.getElements() != null))
        {
            for (ElementSummary elementSummary : queryResponse.getElements())
            {
                if (elementSummary != null)
                {
                    String elementGUID = elementSummary.getGUID();

                    if (elementGUID != null)
                    {
                        queryResultGUIDs.add(elementGUID);
                    }
                }
            }
        }

        Set<String> currentMemberGUIDs = new HashSet<>();

        List<OpenMetadataRootElement> currentMembers = collectionClient.getCollectionMembers(resultsSetGUID, collectionClient.getQueryOptions());

        if (currentMembers != null)
        {
            for (OpenMetadataRootElement currentMember : currentMembers)
            {
                if ((currentMember != null) && (currentMember.getElementHeader() != null))
                {
                    currentMemberGUIDs.add(currentMember.getElementHeader().getGUID());
                }
            }
        }

        int membersAdded   = 0;
        int membersRemoved = 0;

        for (String newMemberGUID : queryResultGUIDs)
        {
            if (! currentMemberGUIDs.contains(newMemberGUID))
            {
                collectionClient.addToCollection(resultsSetGUID, newMemberGUID, collectionClient.getMakeAnchorOptions(false), null);
                membersAdded++;
            }
        }

        for (String oldMemberGUID : currentMemberGUIDs)
        {
            if (! queryResultGUIDs.contains(oldMemberGUID))
            {
                collectionClient.removeFromCollection(resultsSetGUID, oldMemberGUID, collectionClient.getDeleteOptions(false));
                membersRemoved++;
            }
        }

        auditLog.logMessage(methodName, SmartCollectionsAuditCode.RESULTS_SET_REFRESHED.getMessageDefinition(connectorName,
                                                                                                               resultsSetGUID,
                                                                                                               Integer.toString(membersAdded),
                                                                                                               Integer.toString(membersRemoved)));
    }


    /**
     * Minimal shape shared by the different REST APIs that can be used to run a saved query: a list of elements, each
     * with an element header, plus the two FFDC response fields needed to recognize an error response.  The full
     * response bean varies by query endpoint (and FFDCResponseBase cannot be extended here - its @JsonTypeInfo
     * only recognizes a fixed, closed set of registered response subtypes, so extending it breaks deserialization
     * of any response, such as this one, that isn't in that registry), so only these common fields are mapped.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class QueryResponse
    {
        private List<ElementSummary> elements             = null;
        private int                  relatedHTTPCode       = 200;
        private String               exceptionErrorMessage = null;

        public List<ElementSummary> getElements()
        {
            return elements;
        }

        public void setElements(List<ElementSummary> elements)
        {
            this.elements = elements;
        }

        public int getRelatedHTTPCode()
        {
            return relatedHTTPCode;
        }

        public void setRelatedHTTPCode(int relatedHTTPCode)
        {
            this.relatedHTTPCode = relatedHTTPCode;
        }

        public String getExceptionErrorMessage()
        {
            return exceptionErrorMessage;
        }

        public void setExceptionErrorMessage(String exceptionErrorMessage)
        {
            this.exceptionErrorMessage = exceptionErrorMessage;
        }
    }


    /**
     * Minimal shape of an element returned by a saved query - only its GUID is needed.  Different query endpoints
     * return different element shapes: OpenMetadataRootElement (and similar summary beans) nest the GUID inside an
     * elementHeader, whereas the lower-level OpenMetadataElement carries it directly in a flat elementGUID property.
     * Both are mapped here so either shape can be understood.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class ElementSummary
    {
        private ElementHeader elementHeader = null;
        private String        elementGUID   = null;

        public ElementHeader getElementHeader()
        {
            return elementHeader;
        }

        public void setElementHeader(ElementHeader elementHeader)
        {
            this.elementHeader = elementHeader;
        }

        public String getElementGUID()
        {
            return elementGUID;
        }

        public void setElementGUID(String elementGUID)
        {
            this.elementGUID = elementGUID;
        }

        /**
         * Return the GUID of this element, regardless of which of the two shapes it was returned in.
         *
         * @return element GUID or null
         */
        String getGUID()
        {
            if ((elementHeader != null) && (elementHeader.getGUID() != null))
            {
                return elementHeader.getGUID();
            }

            return elementGUID;
        }
    }
}
