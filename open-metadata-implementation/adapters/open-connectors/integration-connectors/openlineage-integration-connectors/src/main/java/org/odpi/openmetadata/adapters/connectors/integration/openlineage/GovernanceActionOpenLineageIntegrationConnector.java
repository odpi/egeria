/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.openlineage;


import org.odpi.openmetadata.adapters.connectors.integration.openlineage.ffdc.OpenLineageIntegrationConnectorAuditCode;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.integration.connectors.IntegrationConnectorBase;
import org.odpi.openmetadata.frameworks.integration.openlineage.*;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ActivityStatus;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataEventListener;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataEventType;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataOutTopicEvent;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.MetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.AssetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.DataStoreProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.actions.ActionTargetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.actions.EngineActionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.net.URI;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;


/**
 * GovernanceActionOpenLineageIntegrationConnector is an integration connector to listen for governance actions executing in the
 * open metadata ecosystem, generate open lineage events for them and publish them to any integration
 * connector running in the same instance of the integration daemon.
 */
public class GovernanceActionOpenLineageIntegrationConnector extends IntegrationConnectorBase implements OpenMetadataEventListener
{
    private static final URI    producer = URI.create("https://egeria-project.org/");
    private static final String defaultNameSpace = "GovernanceActions";

    /**
     * Namespace for datasets that are identified by the qualified name of their asset in open metadata.
     */
    public static final String EGERIA_NAMESPACE = OpenLineageNamespace.EGERIA_NAMESPACE;

    private static final String      FILE_NAMESPACE                   = "file";
    private static final Set<String> OBJECT_STORE_SCHEMES             = Set.of("s3", "s3a", "gs", "wasbs");
    private static final String      ACTION_TARGET_PARAMETER_PREFIX   = "actionTarget:";
    private static final String      DESTINATION_ACTION_TARGET_PREFIX = "destination";
    private static final String      OUTPUT_ACTION_TARGET_PREFIX      = "output";
    private static final String      RECEIVED_GUARDS_PARAMETER        = "receivedGuards";
    private static final String      COMPLETION_GUARDS_PARAMETER      = "completionGuards";
    private final        ZoneId zoneId   = ZoneId.systemDefault();

    private String namespace = defaultNameSpace;

    /**
     * Default constructor
     */
    public GovernanceActionOpenLineageIntegrationConnector()
    {
    }


    /**
     * Indicates that the connector is completely configured and can begin processing.
     *
     * @throws ConnectorCheckedException the connector detected a problem.
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    @Override
    public void start() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        super.start();

        final String methodName = "start";

        namespace = super.getStringConfigurationProperty("namespace", connectionBean.getConfigurationProperties());

        if (namespace == null || namespace.isBlank())
        {
            namespace = defaultNameSpace;
        }

        try
        {
            integrationContext.registerListener(this);
        }
        catch (Exception error)
        {
            logExceptionRecord(methodName,
                               OpenLineageIntegrationConnectorAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                  error.getClass().getName(),
                                                                                                                  methodName,
                                                                                                                  error.getMessage()),
                               error);
        }
    }


    /**
     * Requests that the connector does a comparison of the metadata in the third party technology and open metadata repositories.
     * Refresh is called when the integration connector first starts and then at intervals defined in the connector's configuration
     * as well as any external REST API calls to explicitly refresh the connector.
     */
    public void refresh()
    {
        // nothing to do
    }


    /**
     * Process an event that was published by the Asset Manager OMAS.
     *
     * @param event event object - call getEventType to find out what type of event.
     */
    @Override
    public void processEvent(OpenMetadataOutTopicEvent event)
    {
        final String methodName = "processEvent";

        ElementHeader elementHeader = event.getElementHeader();

        if (((event.getEventType() == OpenMetadataEventType.NEW_ELEMENT_CREATED) ||
             (event.getEventType() == OpenMetadataEventType.REFRESH_ELEMENT_EVENT) ||
             (event.getEventType() == OpenMetadataEventType.ELEMENT_UPDATED)) &&
            (propertyHelper.isTypeOf(elementHeader, OpenMetadataType.ENGINE_ACTION.typeName)))
        {
            try
            {
                String previousActionStatus = getActivityStatus(event.getPreviousElementProperties());
                String currentActionStatus = getActivityStatus(event.getElementProperties());

                /*
                 * Only output an event if the status has changed.
                 */
                if (! previousActionStatus.equals(currentActionStatus))
                {
                    if ((ActivityStatus.IN_PROGRESS.name().equals(currentActionStatus)) ||
                        (ActivityStatus.COMPLETED.name().equals(currentActionStatus)) ||
                        (ActivityStatus.FAILED.name().equals(currentActionStatus)) ||
                        (ActivityStatus.INVALID.name().equals(currentActionStatus)))
                    {
                        OpenMetadataRootElement engineAction = integrationContext.getAssetClient().getAssetByGUID(elementHeader.getGUID(),
                                                                                                                  integrationContext.getAssetClient().getGetOptions());

                        publishOpenLineageEvent(currentActionStatus, event.getEventTime(), engineAction);
                    }
                }
            }
            catch (InvalidParameterException | UserNotAuthorizedException error)
            {
                String stringEvent = event.toString();

                logRecord(methodName,
                          OpenLineageIntegrationConnectorAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                             error.getClass().getName(),
                                                                                                             methodName,
                                                                                                             error.getMessage()),
                          stringEvent);
            }
            catch (Exception error)
            {
                String stringEvent = event.toString();

                logExceptionRecord(methodName,
                                   OpenLineageIntegrationConnectorAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                      error.getClass().getName(),
                                                                                                                      methodName,
                                                                                                                      error.getMessage()),
                                   stringEvent,
                                   error);
            }
        }
    }

    /**
     * Return the action status from the event.
     *
     * @param elementProperties properties for the engine action
     * @return action status as a string
     */
    private String getActivityStatus(ElementProperties elementProperties)
    {
        final String methodName = "getActivityStatus";

        if (elementProperties != null)
        {
            return propertyHelper.getEnumPropertySymbolicName(connectorName,
                                                              OpenMetadataProperty.ACTIVITY_STATUS.name,
                                                              elementProperties,
                                                              methodName);
        }

        return "<null>";
    }


    /**
     * Add information about the governance action into an OpenLineage event and publish it.
     *
     * @param engineActionStatus the status from the entity at the time of the event
     * @param eventTime the time of the change to the entity
     * @param engineAction source information
     * @exception ConnectorCheckedException connector has been asked to stop
     */
    private void publishOpenLineageEvent(String                  engineActionStatus,
                                         Date                    eventTime,
                                         OpenMetadataRootElement engineAction) throws ConnectorCheckedException
    {
        OpenLineageRunEvent event = new OpenLineageRunEvent();

        if (engineAction.getProperties() instanceof EngineActionProperties engineActionProperties)
        {
            event.setProducer(producer);
            event.setEventTime(getTimeStamp(eventTime));

            if (ActivityStatus.IN_PROGRESS.name().equals(engineActionStatus))
            {
                event.setEventType("START");
            }
            else if (ActivityStatus.COMPLETED.name().equals(engineActionStatus))
            {
                event.setEventType("COMPLETE");
            }
            else if (ActivityStatus.FAILED.name().equals(engineActionStatus))
            {
                event.setEventType("FAIL");
            }
            else if (ActivityStatus.INVALID.name().equals(engineActionStatus))
            {
                event.setEventType("ABORT");
            }

            OpenLineageJob job = new OpenLineageJob();


            if (engineActionProperties.getProcessStepName() != null)
            {
                job.setName(engineActionProperties.getProcessStepName());
            }
            else
            {
                job.setName(engineActionProperties.getExecutorEngineName() + "::" + engineActionProperties.getRequestType());
            }


            job.setNamespace(namespace);

            event.setJob(job);

            OpenLineageRun run = new OpenLineageRun();

            run.setRunId(this.getUUIDFromGUID(engineAction.getElementHeader().getGUID()));

            String anchorGUID = propertyHelper.getAnchorGUID(engineAction.getElementHeader());

            OpenLineageRunFacets runFacets = new OpenLineageRunFacets();

            if (anchorGUID != null)
            {
                OpenLineageParentRunFacet    parentRunFacet    = new OpenLineageParentRunFacet();
                OpenLineageParentRunFacetJob parentRunFacetJob = new OpenLineageParentRunFacetJob();
                OpenLineageParentRunFacetRun parentRunFacetRun = new OpenLineageParentRunFacetRun();

                parentRunFacet.set_producer(producer);

                parentRunFacetJob.setName(engineActionProperties.getProcessName());
                parentRunFacetJob.setNamespace(namespace);

                parentRunFacet.setJob(parentRunFacetJob);

                parentRunFacetRun.setRunId(this.getUUIDFromGUID(anchorGUID));

                parentRunFacet.setRun(parentRunFacetRun);

                runFacets.setParent(parentRunFacet);
            }

            OpenLineageNominalTimeRunFacet nominalTimeRunFacet = new OpenLineageNominalTimeRunFacet();

            nominalTimeRunFacet.set_producer(producer);

            if (engineActionProperties.getStartTime() == null)
            {
                nominalTimeRunFacet.setNominalStartTime(getTimeStamp(engineActionProperties.getRequestedTime()));
            }
            else
            {
                nominalTimeRunFacet.setNominalStartTime(getTimeStamp(engineActionProperties.getStartTime()));
            }

            if (engineActionProperties.getCompletionTime() != null)
            {
                nominalTimeRunFacet.setNominalEndTime(getTimeStamp(engineActionProperties.getCompletionTime()));
            }

            runFacets.setNominalTime(nominalTimeRunFacet);
            runFacets.setEgeriaGovernanceAction(getGovernanceActionRunFacet(engineAction.getElementHeader().getGUID(), engineActionProperties));

            /*
             * The request parameters, guards and action target roles describe how the governance action was
             * invoked rather than the data it worked on, so they are passed as execution parameters.
             */
            Map<String, OpenLineageExecutionParametersRunFacetParameter> parameters    = new LinkedHashMap<>();
            List<OpenLineageInputDataSet>                                inputDataSets  = new ArrayList<>();
            List<OpenLineageOutputDataSet>                               outputDataSets = new ArrayList<>();

            if (engineActionProperties.getRequestParameters() != null)
            {
                for (Map.Entry<String, String> requestParameter : engineActionProperties.getRequestParameters().entrySet())
                {
                    addParameter(parameters, requestParameter.getKey(), requestParameter.getKey(), requestParameter.getValue());
                }
            }

            if (engineAction.getActionTargets() != null)
            {
                for (RelatedMetadataElementSummary actionTarget : engineAction.getActionTargets())
                {
                    if ((actionTarget != null) && (actionTarget.getRelatedElement() != null))
                    {
                        String actionTargetName = null;

                        if (actionTarget.getRelationshipProperties() instanceof ActionTargetProperties actionTargetProperties)
                        {
                            actionTargetName = actionTargetProperties.getActionTargetName();
                        }

                        addParameter(parameters,
                                     ACTION_TARGET_PARAMETER_PREFIX + actionTargetName,
                                     actionTargetName,
                                     getElementName(actionTarget.getRelatedElement()));

                        DataSetIdentity dataSetIdentity = getDataSetIdentity(actionTarget.getRelatedElement());

                        if (dataSetIdentity != null)
                        {
                            if (isOutputActionTarget(actionTargetName))
                            {
                                OpenLineageOutputDataSet outputDataSet = new OpenLineageOutputDataSet();

                                outputDataSet.setNamespace(dataSetIdentity.namespace());
                                outputDataSet.setName(dataSetIdentity.name());

                                outputDataSets.add(outputDataSet);
                            }
                            else
                            {
                                OpenLineageInputDataSet inputDataSet = new OpenLineageInputDataSet();

                                inputDataSet.setNamespace(dataSetIdentity.namespace());
                                inputDataSet.setName(dataSetIdentity.name());

                                inputDataSets.add(inputDataSet);
                            }
                        }
                    }
                }
            }

            if (engineActionProperties.getReceivedGuards() != null)
            {
                addParameter(parameters, RECEIVED_GUARDS_PARAMETER, RECEIVED_GUARDS_PARAMETER, String.join(", ", engineActionProperties.getReceivedGuards()));
            }

            if ((engineActionProperties.getCompletionTime() != null) && (engineActionProperties.getCompletionGuards() != null))
            {
                addParameter(parameters, COMPLETION_GUARDS_PARAMETER, COMPLETION_GUARDS_PARAMETER, String.join(", ", engineActionProperties.getCompletionGuards()));
            }

            if (! parameters.isEmpty())
            {
                OpenLineageExecutionParametersRunFacet executionParametersRunFacet = new OpenLineageExecutionParametersRunFacet();

                executionParametersRunFacet.set_producer(producer);
                executionParametersRunFacet.setParameters(new ArrayList<>(parameters.values()));

                runFacets.setExecutionParameters(executionParametersRunFacet);
            }

            run.setFacets(runFacets);

            event.setRun(run);
            event.setInputs(inputDataSets);

            if (! outputDataSets.isEmpty())
            {
                event.setOutputs(outputDataSets);
            }

            integrationContext.publishOpenLineageRunEvent(event);
        }
    }


    /**
     * Add an execution parameter, giving it a unique key if the key is already in use (for example, when
     * several action targets share the same action target name).
     *
     * @param parameters parameters accumulated so far
     * @param key preferred key for the parameter
     * @param name name of the parameter
     * @param value value of the parameter - the parameter is skipped if this is null
     */
    private void addParameter(Map<String, OpenLineageExecutionParametersRunFacetParameter> parameters,
                              String                                                       key,
                              String                                                       name,
                              String                                                       value)
    {
        if (value != null)
        {
            String uniqueKey = key;
            int    count     = 1;

            while (parameters.containsKey(uniqueKey))
            {
                count++;
                uniqueKey = key + ":" + count;
            }

            OpenLineageExecutionParametersRunFacetParameter parameter = new OpenLineageExecutionParametersRunFacetParameter();

            parameter.setKey(uniqueKey);
            parameter.setName(name);
            parameter.setValue(value);

            parameters.put(uniqueKey, parameter);
        }
    }


    /**
     * Return the name used to identify an element in the execution parameters: its qualified name, or its
     * unique identifier if it has no qualified name.
     *
     * @param element element to name
     * @return qualified name or guid
     */
    private String getElementName(MetadataElementSummary element)
    {
        if ((element.getProperties() instanceof ReferenceableProperties referenceableProperties) &&
            (referenceableProperties.getQualifiedName() != null))
        {
            return referenceableProperties.getQualifiedName();
        }

        return element.getElementHeader().getGUID();
    }


    /**
     * Return Egeria's run facet describing the governance action behind the run.
     *
     * @param engineActionGUID unique identifier of the engine action
     * @param engineActionProperties properties of the engine action
     * @return facet
     */
    private OpenLineageEgeriaGovernanceActionRunFacet getGovernanceActionRunFacet(String                 engineActionGUID,
                                                                                  EngineActionProperties engineActionProperties)
    {
        OpenLineageEgeriaGovernanceActionRunFacet governanceActionRunFacet = new OpenLineageEgeriaGovernanceActionRunFacet();

        governanceActionRunFacet.set_producer(producer);
        governanceActionRunFacet.setIscQualifiedName(engineActionProperties.getISCQualifiedName());
        governanceActionRunFacet.setEngineActionGUID(engineActionGUID);
        governanceActionRunFacet.setGovernanceEngineName(engineActionProperties.getExecutorEngineName());
        governanceActionRunFacet.setRequestType(engineActionProperties.getRequestType());
        governanceActionRunFacet.setGovernanceActionTypeName(engineActionProperties.getGovernanceActionTypeName());
        governanceActionRunFacet.setProcessName(engineActionProperties.getProcessName());
        governanceActionRunFacet.setProcessStepName(engineActionProperties.getProcessStepName());

        return governanceActionRunFacet;
    }


    /**
     * Is the action target one that the governance action writes to?  Governance services name the targets they
     * write to with a "destination" or "output" prefix (for example, destinationFolder or destinationDataSet for the
     * provisioning services); every other action target is treated as an input.
     *
     * @param actionTargetName name of the action target
     * @return boolean
     */
    private boolean isOutputActionTarget(String actionTargetName)
    {
        if (actionTargetName == null)
        {
            return false;
        }

        String lowerCaseName = actionTargetName.toLowerCase();

        return (lowerCaseName.startsWith(DESTINATION_ACTION_TARGET_PREFIX)) || (lowerCaseName.startsWith(OUTPUT_ACTION_TARGET_PREFIX));
    }


    /**
     * The namespace and name of an OpenLineage dataset.
     *
     * @param namespace dataset namespace
     * @param name dataset name
     */
    private record DataSetIdentity(String namespace, String name) {}


    /**
     * Return the OpenLineage dataset identity of an action target.  Only data assets are datasets; other action
     * targets (servers, processes, collections, ...) are reported in the execution parameters only.
     * The dataset's identity is chosen in this order:
     * <ul>
     *     <li>the asset's namespacePath and resourceName, which hold the OpenLineage namespace and name for assets
     *     catalogued from OpenLineage events or by connectors that follow the OpenLineage naming conventions (a
     *     namespacePath that is not shaped like an OpenLineage namespace, such as a Unity Catalog catalog.schema
     *     prefix, is ignored);</li>
     *     <li>the data store's pathName, split into a namespace and name as described in the OpenLineage naming
     *     conventions for file systems and object stores;</li>
     *     <li>otherwise the "egeria" namespace and the asset's qualified name, which the OpenLineage cataloguer
     *     resolves back to the asset.</li>
     * </ul>
     *
     * @param element action target element
     * @return dataset identity or null if the element is not a data asset
     */
    private DataSetIdentity getDataSetIdentity(MetadataElementSummary element)
    {
        if ((element.getElementHeader() == null) ||
            (! propertyHelper.isTypeOf(element.getElementHeader(), OpenMetadataType.DATA_ASSET.typeName)))
        {
            return null;
        }

        if ((element.getProperties() instanceof AssetProperties assetProperties) &&
            (OpenLineageNamespace.isOpenLineageNamespace(assetProperties.getNamespacePath())) && (assetProperties.getResourceName() != null))
        {
            return new DataSetIdentity(assetProperties.getNamespacePath(), assetProperties.getResourceName());
        }

        if ((element.getProperties() instanceof DataStoreProperties dataStoreProperties) &&
            (dataStoreProperties.getPathName() != null) && (! dataStoreProperties.getPathName().isBlank()))
        {
            DataSetIdentity pathIdentity = getIdentityFromPathName(dataStoreProperties.getPathName());

            if (pathIdentity != null)
            {
                return pathIdentity;
            }
        }

        return new DataSetIdentity(EGERIA_NAMESPACE, getElementName(element));
    }


    /**
     * Split a data store's path name into an OpenLineage namespace and name.  A path with a scheme
     * (for example s3://bucket/key or hdfs://namenode:8020/path) has the scheme and authority as its namespace;
     * a plain path is in the local file system, whose namespace is "file".  Object store names are object keys,
     * so they have no leading slash.
     *
     * @param pathName path name from the data store
     * @return dataset identity or null if the path could not be converted
     */
    private DataSetIdentity getIdentityFromPathName(String pathName)
    {
        int schemeSeparator = pathName.indexOf("://");

        /*
         * A single letter before the colon is a Windows drive letter rather than a scheme.
         */
        if (schemeSeparator > 1)
        {
            String scheme    = pathName.substring(0, schemeSeparator).toLowerCase();
            String remainder = pathName.substring(schemeSeparator + 3);
            int    pathStart = remainder.indexOf('/');
            String authority = (pathStart < 0) ? remainder : remainder.substring(0, pathStart);
            String path      = (pathStart < 0) ? "" : remainder.substring(pathStart);

            if ("file".equals(scheme))
            {
                if (path.isEmpty())
                {
                    return null;
                }

                return new DataSetIdentity(authority.isEmpty() ? FILE_NAMESPACE : FILE_NAMESPACE + "://" + authority, path);
            }

            if (OBJECT_STORE_SCHEMES.contains(scheme) && path.startsWith("/"))
            {
                path = path.substring(1);
            }

            if ((authority.isEmpty()) || (path.isEmpty()))
            {
                return null;
            }

            return new DataSetIdentity(scheme + "://" + authority, path);
        }

        return new DataSetIdentity(FILE_NAMESPACE, pathName);
    }


    /**
     * Convert a GUID to a UUID.
     *
     * @param guid starting guid from Egeria
     * @return UUID object
     */
    private UUID getUUIDFromGUID(String guid)
    {
        /*
         * In-memory repo prepends type name to GUID
         */
        if (guid.length() > UUID.randomUUID().toString().length())
        {
            int length             = guid.length();
            int standardUUIDLength = UUID.randomUUID().toString().length();

            return UUID.fromString(guid.substring(length - standardUUIDLength));
        }
        else
        {
            return UUID.fromString(guid);
        }
    }


    /**
     * This turns a java Date into the right string format for an open lineage event.
     *
     * @param date date to convert
     * @return string formatted date
     */
    private String getTimeStamp (Date date)
    {
        ZonedDateTime zonedDateTime   = ZonedDateTime.ofInstant(date.toInstant(), zoneId);
        String        zonedDateString = zonedDateTime.toString();

        /*
         * This is removing the time zone from the formatted date.  This should not be necessary and is hopefully temporary.
         */
        String[] dataTokens = zonedDateString.split("\\[");

        return dataTokens[0];
    }
}
