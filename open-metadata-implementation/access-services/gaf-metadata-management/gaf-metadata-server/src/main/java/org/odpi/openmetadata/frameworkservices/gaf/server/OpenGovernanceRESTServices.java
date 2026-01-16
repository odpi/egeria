/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.gaf.server;


import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NullRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.ResultsRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.commonservices.generichandlers.AssetHandler;
import org.odpi.openmetadata.commonservices.generichandlers.EngineActionHandler;
import org.odpi.openmetadata.commonservices.generichandlers.GovernanceActionProcessStepHandler;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIGenericHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.opengovernance.mermaid.GovernanceActionProcessMermaidGraphBuilder;
import org.odpi.openmetadata.frameworks.opengovernance.properties.*;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ActivityStatus;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElementStub;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworkservices.gaf.rest.*;
import org.odpi.openmetadata.frameworkservices.omf.converters.OpenMetadataElementStubConverter;
import org.odpi.openmetadata.frameworkservices.omf.rest.ActionTargetStatusRequestBody;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * The OpenGovernanceRESTServices provides the server-side implementation of the services used by the governance
 * engine as it is managing requests to execute open governance services in the governance server.
 * These services align with the interface definitions from the Open Survey Framework (OGF).
 */
public class OpenGovernanceRESTServices
{
    private final static GAFServicesInstanceHandler instanceHandler = new GAFServicesInstanceHandler();

    private final        RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();
    private final static RESTCallLogger       restCallLogger       = new RESTCallLogger(LoggerFactory.getLogger(OpenGovernanceRESTServices.class),
                                                                                        instanceHandler.getServiceName());

    private final InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();

    /**
     * Default constructor
     */
    public OpenGovernanceRESTServices()
    {
    }



    /* =====================================================================================================================
     * A governance action process describes a well-defined series of steps that gets something done.
     * The steps are defined using GovernanceActionProcessSteps.
     */


    /**
     * Add the specification and predefined action targets and request parameters to the element returned.
     *
     * @param userId calling user
     * @param handler generic handler
     * @param element retrieved element.
     * @return elements plus specifications
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException repository error
     * @throws UserNotAuthorizedException security error
     */
    private GovernanceActionProcessElement completeGovernanceActionProcessElement(String                                                        userId,
                                                                                  OpenMetadataAPIGenericHandler<GovernanceActionProcessElement> handler,
                                                                                  GovernanceActionProcessElement                                element) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException
    {
        final String methodName = "completeGovernanceActionProcessElement";

        if (element != null)
        {
            final String elementGUIDParameterName = "governanceActionProcessGUID";

            if (handler.getRepositoryHelper().isTypeOf(handler.getServiceName(),
                                                       element.getElementHeader().getType().getTypeName(),
                                                       OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName))
            {
                /*
                 * Retrieve pre-defined Action Targets.
                 * The relationships are retrieved explicitly by type because it is assumed that the process may have many other relationships.
                 */
                List<Relationship> relationships = handler.getAttachmentLinks(userId,
                                                                              element.getElementHeader().getGUID(),
                                                                              elementGUIDParameterName,
                                                                              OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName,
                                                                              OpenMetadataType.TARGET_FOR_GOVERNANCE_ACTION_RELATIONSHIP.typeGUID,
                                                                              OpenMetadataType.TARGET_FOR_GOVERNANCE_ACTION_RELATIONSHIP.typeName,
                                                                              null,
                                                                              null,
                                                                              2,
                                                                              null,
                                                                              null,
                                                                              SequencingOrder.CREATION_DATE_RECENT,
                                                                              null,
                                                                              true,
                                                                              false,
                                                                              0,
                                                                              0,
                                                                              new Date(),
                                                                              methodName);

                if (relationships != null)
                {
                    List<PredefinedActionTarget> predefinedActionTargets = new ArrayList<>();
                    OpenMetadataElementStubConverter<OpenMetadataElementStub> converter = new OpenMetadataElementStubConverter<>(handler.getRepositoryHelper(),
                                                                                                                                 handler.getServiceName(),
                                                                                                                                 handler.getServerName());
                    for (Relationship relationship : relationships)
                    {
                        if (relationship != null)
                        {
                            PredefinedActionTarget predefinedActionTarget = new PredefinedActionTarget();

                            predefinedActionTarget.setActionTargetName(handler.getRepositoryHelper().getStringProperty(handler.getServiceName(),
                                                                                                                       OpenMetadataProperty.ACTION_TARGET_NAME.name,
                                                                                                                       relationship.getProperties(),
                                                                                                                       methodName));

                            predefinedActionTarget.setActionTargetElementStub(converter.getOpenMetadataElementStub(relationship.getEntityTwoProxy()));

                            predefinedActionTargets.add(predefinedActionTarget);
                        }
                    }

                    element.setPredefinedActionTargets(predefinedActionTargets);
                }

                Relationship relationship = handler.getUniqueAttachmentLink(userId,
                                                                            element.getElementHeader().getGUID(),
                                                                            elementGUIDParameterName,
                                                                            OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName,
                                                                            OpenMetadataType.GOVERNANCE_ACTION_PROCESS_FLOW_RELATIONSHIP.typeGUID,
                                                                            OpenMetadataType.GOVERNANCE_ACTION_PROCESS_FLOW_RELATIONSHIP.typeName,
                                                                            null,
                                                                            null,
                                                                            2,
                                                                            null,
                                                                            null,
                                                                            SequencingOrder.CREATION_DATE_RECENT,
                                                                            null,
                                                                            false,
                                                                            false,
                                                                            new Date(),
                                                                            methodName);
                if (relationship != null)
                {
                    element.setPredefinedRequestParameters(handler.getRepositoryHelper().getStringMapFromProperty(handler.getServiceName(),
                                                                                                                  OpenMetadataProperty.REQUEST_PARAMETERS.name,
                                                                                                                  relationship.getProperties(),
                                                                                                                  methodName));
                }
            }
        }

        return element;
    }



    /**
     * Retrieve the governance action process metadata element with the supplied unique identifier
     * along with the flow definition describing its implementation.
     *
     * @param serverName name of the service to route the request to
     * @param userId calling user
     * @param processGUID unique identifier of the requested metadata element
     *
     * @return requested metadata element or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public GovernanceActionProcessGraphResponse getGovernanceActionProcessGraph(String             serverName,
                                                                                String             userId,
                                                                                String             processGUID,
                                                                                ResultsRequestBody requestBody)
    {
        final String methodName = "getGovernanceActionProcessGraph";
        final String processGUIDParameterName = "processGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GovernanceActionProcessGraphResponse response = new GovernanceActionProcessGraphResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            AssetHandler<GovernanceActionProcessElement> processHandler = instanceHandler.getGovernanceActionProcessHandler(userId, serverName, methodName);
            EngineActionHandler<EngineActionElement> engineActionHandler = instanceHandler.getEngineActionHandler(userId, serverName, methodName);
            GovernanceActionProcessStepHandler<GovernanceActionProcessStepElement> handler = instanceHandler.getGovernanceActionProcessStepHandler(userId, serverName, methodName);

            GovernanceActionProcessGraph governanceActionProcessGraph = new GovernanceActionProcessGraph();

            if (requestBody != null)
            {
                governanceActionProcessGraph.setGovernanceActionProcess(
                        completeGovernanceActionProcessElement(userId, processHandler, processHandler.getBeanFromRepository(userId,
                                                                                                                            processGUID,
                                                                                                                            processGUIDParameterName,
                                                                                                                            OpenMetadataType.REFERENCEABLE.typeName,
                                                                                                                            requestBody.getForLineage(),
                                                                                                                            requestBody.getForDuplicateProcessing(),
                                                                                                                            requestBody.getEffectiveTime(),
                                                                                                                            methodName)));
            }
            else
            {
                governanceActionProcessGraph.setGovernanceActionProcess(
                        completeGovernanceActionProcessElement(userId, processHandler, processHandler.getBeanFromRepository(userId,
                                                                                                                            processGUID,
                                                                                                                            processGUIDParameterName,
                                                                                                                            OpenMetadataType.REFERENCEABLE.typeName,
                                                                                                                            false,
                                                                                                                            false,
                                                                                                                            new Date(),
                                                                                                                            methodName)));
            }


            String processTypeName = governanceActionProcessGraph.getGovernanceActionProcess().getElementHeader().getType().getTypeName();

            if (OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName.equals(processTypeName))
            {
                governanceActionProcessGraph.setFirstProcessStep(this.getFirstProcessStepElement(serverName,
                                                                                                 userId,
                                                                                                 processGUID,
                                                                                                 methodName));
            }
            else if (OpenMetadataType.GOVERNANCE_ACTION_PROCESS_INSTANCE.typeName.equals(processTypeName))
            {
                governanceActionProcessGraph.setFirstProcessStep(this.getFirstEngineStepElement(serverName,
                                                                                                userId,
                                                                                                processGUID,
                                                                                                methodName));
            }

            if (governanceActionProcessGraph.getFirstProcessStep() != null)
            {
                String firstProcessStepGUID = governanceActionProcessGraph.getFirstProcessStep().getElement().getElementHeader().getGUID();

                List<String>  processedGUIDs = new ArrayList<>();

                processedGUIDs.add(firstProcessStepGUID);

                if (OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName.equals(processTypeName))
                {
                    getNextProcessSteps(userId,
                                        handler,
                                        firstProcessStepGUID,
                                        governanceActionProcessGraph,
                                        processedGUIDs,
                                        invalidParameterHandler.getMaxPagingSize());
                }
                else if (OpenMetadataType.GOVERNANCE_ACTION_PROCESS_INSTANCE.typeName.equals(processTypeName))
                {
                    getNextEngineSteps(userId,
                                        engineActionHandler,
                                        firstProcessStepGUID,
                                        governanceActionProcessGraph,
                                        processedGUIDs,
                                        invalidParameterHandler.getMaxPagingSize());
                }
            }

            GovernanceActionProcessMermaidGraphBuilder graphBuilder = new GovernanceActionProcessMermaidGraphBuilder(governanceActionProcessGraph);
            governanceActionProcessGraph.setMermaidGraph(graphBuilder.getMermaidGraph());

            response.setElement(governanceActionProcessGraph);

        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the next step in the process.
     *
     * @param userId calling user
     * @param handler access to metadata
     * @param processStepGUID current step
     * @param governanceActionProcessGraph current state of the graph
     * @param processedGUIDs the guids we have processed
     * @param pageSize max page size
     * @throws InvalidParameterException bad property
     * @throws PropertyServerException repository in error
     * @throws UserNotAuthorizedException user authorization exception
     */
    private void getNextProcessSteps(String                                                                 userId,
                                     GovernanceActionProcessStepHandler<GovernanceActionProcessStepElement> handler,
                                     String                                                                 processStepGUID,
                                     GovernanceActionProcessGraph                                           governanceActionProcessGraph,
                                     List<String>                                                           processedGUIDs,
                                     int                                                                    pageSize) throws InvalidParameterException,
                                                                                                                             PropertyServerException,
                                                                                                                             UserNotAuthorizedException
    {
        final String methodName = "getNextProcessSteps";

        RepositoryHandler repositoryHandler = handler.getRepositoryHandler();

        int startFrom = 0;
        List<Relationship> nextProcessStepRelationships = repositoryHandler.getRelationshipsByType(userId,
                                                                                                   processStepGUID,
                                                                                                   OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP.typeName,
                                                                                                   OpenMetadataType.NEXT_GOVERNANCE_ACTION_PROCESS_STEP_RELATIONSHIP.typeGUID,
                                                                                                   OpenMetadataType.NEXT_GOVERNANCE_ACTION_PROCESS_STEP_RELATIONSHIP.typeName,
                                                                                                   2,
                                                                                                   null,
                                                                                                   null,
                                                                                                   SequencingOrder.CREATION_DATE_RECENT,
                                                                                                   null,
                                                                                                   false,
                                                                                                   false,
                                                                                                   startFrom,
                                                                                                   pageSize,
                                                                                                   null,
                                                                                                   methodName);

        while (nextProcessStepRelationships != null)
        {
            for (Relationship relationship : nextProcessStepRelationships)
            {
                this.addProcessStep(userId,
                                    handler,
                                    relationship.getEntityOneProxy().getGUID(),
                                    governanceActionProcessGraph,
                                    processedGUIDs);

                NextGovernanceActionProcessStepLink processStepLink = new NextGovernanceActionProcessStepLink();

                processStepLink.setPreviousProcessStep(handler.getElementStub(relationship.getEntityOneProxy()));
                processStepLink.setNextProcessStep(handler.getElementStub(relationship.getEntityTwoProxy()));
                processStepLink.setNextProcessStepLinkGUID(relationship.getGUID());
                processStepLink.setGuard(handler.getRepositoryHelper().getStringProperty(handler.getServiceName(),
                                                                                         OpenMetadataProperty.GUARD.name,
                                                                                         relationship.getProperties(),
                                                                                         methodName));
                processStepLink.setMandatoryGuard(handler.getRepositoryHelper().getBooleanProperty(handler.getServiceName(),
                                                                                                   OpenMetadataProperty.MANDATORY_GUARD.name,
                                                                                                   relationship.getProperties(),
                                                                                                   methodName));

                List<NextGovernanceActionProcessStepLink> processStepLinks = governanceActionProcessGraph.getProcessStepLinks();

                if (processStepLinks == null)
                {
                    processStepLinks = new ArrayList<>();
                }

                processStepLinks.add(processStepLink);
                governanceActionProcessGraph.setProcessStepLinks(processStepLinks);

                if (! processedGUIDs.contains(relationship.getEntityTwoProxy().getGUID()))
                {
                    this.addProcessStep(userId,
                                        handler,
                                        relationship.getEntityTwoProxy().getGUID(),
                                        governanceActionProcessGraph,
                                        processedGUIDs);

                    getNextProcessSteps(userId,
                                        handler,
                                        relationship.getEntityTwoProxy().getGUID(),
                                        governanceActionProcessGraph,
                                        processedGUIDs,
                                        pageSize);
                }
            }

            startFrom = startFrom + pageSize;
            nextProcessStepRelationships = repositoryHandler.getRelationshipsByType(userId,
                                                                                    processStepGUID,
                                                                                    OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP.typeName,
                                                                                    OpenMetadataType.NEXT_GOVERNANCE_ACTION_PROCESS_STEP_RELATIONSHIP.typeGUID,
                                                                                    OpenMetadataType.NEXT_GOVERNANCE_ACTION_PROCESS_STEP_RELATIONSHIP.typeName,
                                                                                    2,
                                                                                    null,
                                                                                    null,
                                                                                    SequencingOrder.CREATION_DATE_RECENT,
                                                                                    null,
                                                                                    false,
                                                                                    false,
                                                                                    startFrom,
                                                                                    pageSize,
                                                                                    null,
                                                                                    methodName);
        }
    }



    /**
     * Retrieve the next step in the process.
     *
     * @param userId calling user
     * @param handler access to metadata
     * @param processStepGUID current step
     * @param governanceActionProcessGraph current state of the graph
     * @param processedGUIDs the guids we have processed
     * @param pageSize max page size
     * @throws InvalidParameterException bad property
     * @throws PropertyServerException repository in error
     * @throws UserNotAuthorizedException user authorization exception
     */
    private void getNextEngineSteps(String                                    userId,
                                     EngineActionHandler<EngineActionElement> handler,
                                     String                                   processStepGUID,
                                     GovernanceActionProcessGraph             governanceActionProcessGraph,
                                     List<String>                             processedGUIDs,
                                     int                                      pageSize) throws InvalidParameterException,
                                                                                               PropertyServerException,
                                                                                               UserNotAuthorizedException
    {
        final String methodName = "getNextEngineSteps";

        RepositoryHandler repositoryHandler = handler.getRepositoryHandler();

        int startFrom = 0;
        List<Relationship> nextProcessStepRelationships = repositoryHandler.getRelationshipsByType(userId,
                                                                                                   processStepGUID,
                                                                                                   OpenMetadataType.ENGINE_ACTION.typeName,
                                                                                                   OpenMetadataType.CONTROL_FLOW_RELATIONSHIP.typeGUID,
                                                                                                   OpenMetadataType.CONTROL_FLOW_RELATIONSHIP.typeName,
                                                                                                   2,
                                                                                                   null,
                                                                                                   null,
                                                                                                   SequencingOrder.CREATION_DATE_RECENT,
                                                                                                   null,
                                                                                                   false,
                                                                                                   false,
                                                                                                   startFrom,
                                                                                                   pageSize,
                                                                                                   null,
                                                                                                   methodName);

        while (nextProcessStepRelationships != null)
        {
            for (Relationship relationship : nextProcessStepRelationships)
            {
                this.addEngineStep(userId,
                                   handler,
                                   relationship.getEntityOneProxy().getGUID(),
                                   governanceActionProcessGraph,
                                   processedGUIDs);

                NextGovernanceActionProcessStepLink processStepLink = new NextGovernanceActionProcessStepLink();

                processStepLink.setPreviousProcessStep(handler.getElementStub(relationship.getEntityOneProxy()));
                processStepLink.setNextProcessStep(handler.getElementStub(relationship.getEntityTwoProxy()));
                processStepLink.setNextProcessStepLinkGUID(relationship.getGUID());
                processStepLink.setGuard(handler.getRepositoryHelper().getStringProperty(handler.getServiceName(),
                                                                                         OpenMetadataProperty.GUARD.name,
                                                                                         relationship.getProperties(),
                                                                                         methodName));
                processStepLink.setMandatoryGuard(handler.getRepositoryHelper().getBooleanProperty(handler.getServiceName(),
                                                                                                   OpenMetadataProperty.MANDATORY_GUARD.name,
                                                                                                   relationship.getProperties(),
                                                                                                   methodName));

                List<NextGovernanceActionProcessStepLink> processStepLinks = governanceActionProcessGraph.getProcessStepLinks();

                if (processStepLinks == null)
                {
                    processStepLinks = new ArrayList<>();
                }

                processStepLinks.add(processStepLink);
                governanceActionProcessGraph.setProcessStepLinks(processStepLinks);

                if (! processedGUIDs.contains(relationship.getEntityTwoProxy().getGUID()))
                {
                    this.addEngineStep(userId,
                                       handler,
                                       relationship.getEntityTwoProxy().getGUID(),
                                       governanceActionProcessGraph,
                                       processedGUIDs);

                    getNextEngineSteps(userId,
                                       handler,
                                       relationship.getEntityTwoProxy().getGUID(),
                                       governanceActionProcessGraph,
                                       processedGUIDs,
                                       pageSize);
                }
            }

            startFrom = startFrom + pageSize;
            nextProcessStepRelationships = repositoryHandler.getRelationshipsByType(userId,
                                                                                    processStepGUID,
                                                                                    OpenMetadataType.ENGINE_ACTION.typeName,
                                                                                    OpenMetadataType.CONTROL_FLOW_RELATIONSHIP.typeGUID,
                                                                                    OpenMetadataType.CONTROL_FLOW_RELATIONSHIP.typeName,
                                                                                    2,
                                                                                    null,
                                                                                    null,
                                                                                    SequencingOrder.CREATION_DATE_RECENT,
                                                                                    null,
                                                                                    false,
                                                                                    false,
                                                                                    startFrom,
                                                                                    pageSize,
                                                                                    null,
                                                                                    methodName);
        }
    }


    /**
     * Save the next step in the process.
     *
     * @param userId calling user
     * @param handler access to metadata
     * @param processStepGUID current step
     * @param governanceActionProcessGraph current state of the graph
     * @param processedGUIDs the guids we have processed
     * @throws InvalidParameterException bad property
     * @throws PropertyServerException repository in error
     * @throws UserNotAuthorizedException user authorization exception
     */
    private void addProcessStep(String                                                                 userId,
                                GovernanceActionProcessStepHandler<GovernanceActionProcessStepElement> handler,
                                String                                                                 processStepGUID,
                                GovernanceActionProcessGraph                                           governanceActionProcessGraph,
                                List<String>                                                           processedGUIDs) throws InvalidParameterException,
                                                                                                                              PropertyServerException,
                                                                                                                              UserNotAuthorizedException
    {
        final String methodName = "addProcessStep";
        if (! processedGUIDs.contains(processStepGUID))
        {
            GovernanceActionProcessStepElement processStepElement = handler.getGovernanceActionProcessStepByGUID(userId,
                                                                                                                 processStepGUID,
                                                                                                                 null,
                                                                                                                 methodName);

            List<GovernanceActionProcessStepExecutionElement> processStepElements = governanceActionProcessGraph.getNextProcessSteps();

            if (processStepElements == null)
            {
                processStepElements = new ArrayList<>();
            }

            processStepElements.add(new GovernanceActionProcessStepExecutionElement(processStepElement));

            governanceActionProcessGraph.setNextProcessSteps(processStepElements);

            processedGUIDs.add(processStepGUID);
        }
    }


    /**
     * Save the next step in the process.
     *
     * @param userId calling user
     * @param handler access to metadata
     * @param processStepGUID current step
     * @param governanceActionProcessGraph current state of the graph
     * @param processedGUIDs the guids we have processed
     * @throws InvalidParameterException bad property
     * @throws PropertyServerException repository in error
     * @throws UserNotAuthorizedException user authorization exception
     */
    private void addEngineStep(String                                   userId,
                               EngineActionHandler<EngineActionElement> handler,
                               String                                   processStepGUID,
                               GovernanceActionProcessGraph             governanceActionProcessGraph,
                               List<String>                             processedGUIDs) throws InvalidParameterException,
                                                                                               PropertyServerException,
                                                                                               UserNotAuthorizedException
    {
        final String methodName = "addEngineStep";

        if (! processedGUIDs.contains(processStepGUID))
        {
            EngineActionElement engineActionElement = handler.getEngineAction(userId,
                                                                              processStepGUID,
                                                                              null,
                                                                              methodName);

            List<GovernanceActionProcessStepExecutionElement> processStepElements = governanceActionProcessGraph.getNextProcessSteps();

            if (processStepElements == null)
            {
                processStepElements = new ArrayList<>();
            }

            processStepElements.add(new GovernanceActionProcessStepExecutionElement(engineActionElement));

            governanceActionProcessGraph.setNextProcessSteps(processStepElements);

            processedGUIDs.add(processStepGUID);
        }
    }


    /**
     * Return the first process step element of a governance action process.
     *
     * @param serverName name of the service to route the request to
     * @param userId calling user
     * @param processGUID unique identifier of the governance action process
     * @param methodName calling method
     * @return first process step element
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws PropertyServerException the user is not authorized to issue this request
     * @throws UserNotAuthorizedException a problem reported in the open metadata server(s)
     */
    private FirstGovernanceActionProcessStepElement getFirstProcessStepElement(String serverName,
                                                                               String userId,
                                                                               String processGUID,
                                                                               String methodName) throws InvalidParameterException,
                                                                                                         PropertyServerException,
                                                                                                         UserNotAuthorizedException
    {
        GovernanceActionProcessStepHandler<GovernanceActionProcessStepElement> handler = instanceHandler.getGovernanceActionProcessStepHandler(userId, serverName, methodName);

        final String processGUIDParameterName = "processGUID";

        Relationship firstActionProcessStepLink = handler.getUniqueAttachmentLink(userId,
                                                                                  processGUID,
                                                                                  processGUIDParameterName,
                                                                                  OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName,
                                                                                  OpenMetadataType.GOVERNANCE_ACTION_PROCESS_FLOW_RELATIONSHIP.typeGUID,
                                                                                  OpenMetadataType.GOVERNANCE_ACTION_PROCESS_FLOW_RELATIONSHIP.typeName,
                                                                                  null,
                                                                                  OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP.typeName,
                                                                                  0,
                                                                                  null,
                                                                                  null,
                                                                                  SequencingOrder.CREATION_DATE_RECENT,
                                                                                  null,
                                                                                  false,
                                                                                  false,
                                                                                  null,
                                                                                  methodName);

        if ((firstActionProcessStepLink != null) && (firstActionProcessStepLink.getEntityTwoProxy() != null))
        {
            FirstGovernanceActionProcessStepElement firstProcessStep = new FirstGovernanceActionProcessStepElement();

            firstProcessStep.setLinkGUID(firstActionProcessStepLink.getGUID());
            firstProcessStep.setGuard(handler.getRepositoryHelper().getStringProperty(instanceHandler.getServiceName(),
                                                                                      OpenMetadataProperty.GUARD.name,
                                                                                      firstActionProcessStepLink.getProperties(),
                                                                                      methodName));

            GovernanceActionProcessStepElement processStepElement = handler.getGovernanceActionProcessStepByGUID(userId,
                                                                                                                 firstActionProcessStepLink.getEntityTwoProxy().getGUID(),
                                                                                                                 null,
                                                                                                                 methodName);
            firstProcessStep.setElement(new GovernanceActionProcessStepExecutionElement(processStepElement));

            return firstProcessStep;
        }

        return null;
    }


    /**
     * Return the first process step element of a governance action process.
     *
     * @param serverName name of the service to route the request to
     * @param userId calling user
     * @param processInstanceGUID unique identifier of the governance action process
     * @param methodName calling method
     * @return first process step element
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws PropertyServerException the user is not authorized to issue this request
     * @throws UserNotAuthorizedException a problem reported in the open metadata server(s)
     */
    private FirstGovernanceActionProcessStepElement getFirstEngineStepElement(String serverName,
                                                                              String userId,
                                                                              String processInstanceGUID,
                                                                              String methodName) throws InvalidParameterException,
                                                                                                        PropertyServerException,
                                                                                                        UserNotAuthorizedException
    {
        EngineActionHandler<EngineActionElement> handler = instanceHandler.getEngineActionHandler(userId, serverName, methodName);

        final String processGUIDParameterName = "processInstanceGUID";

        Relationship firstActionProcessStepLink = handler.getUniqueAttachmentLink(userId,
                                                                                  processInstanceGUID,
                                                                                  processGUIDParameterName,
                                                                                  OpenMetadataType.GOVERNANCE_ACTION_PROCESS_INSTANCE.typeName,
                                                                                  OpenMetadataType.ACTION_REQUESTER_RELATIONSHIP.typeGUID,
                                                                                  OpenMetadataType.ACTION_REQUESTER_RELATIONSHIP.typeName,
                                                                                  null,
                                                                                  OpenMetadataType.ENGINE_ACTION.typeName,
                                                                                  0,
                                                                                  null,
                                                                                  null,
                                                                                  SequencingOrder.CREATION_DATE_RECENT,
                                                                                  null,
                                                                                  false,
                                                                                  false,
                                                                                  null,
                                                                                  methodName);

        if ((firstActionProcessStepLink != null) && (firstActionProcessStepLink.getEntityTwoProxy() != null))
        {
            FirstGovernanceActionProcessStepElement firstProcessStep = new FirstGovernanceActionProcessStepElement();

            firstProcessStep.setLinkGUID(firstActionProcessStepLink.getGUID());

            EngineActionElement engineActionElement = handler.getEngineAction(userId,
                                                                              firstActionProcessStepLink.getEntityTwoProxy().getGUID(),
                                                                              null,
                                                                              methodName);

            firstProcessStep.setElement(new GovernanceActionProcessStepExecutionElement(engineActionElement));

            return firstProcessStep;
        }

        return null;
    }




    /*
     * Engine Actions
     */

    /**
     * Request the status and properties of an executing engine action request.
     *
     * @param serverName     name of server instance to route request to
     * @param userId identifier of calling user
     * @param engineActionGUID identifier of the engine action request.
     *
     * @return engine action properties and status or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException there was a problem detected by the metadata store.
     */
    public EngineActionElementResponse getEngineAction(String serverName,
                                                       String userId,
                                                       String engineActionGUID)
    {
        final String methodName = "getEngineAction";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog auditLog = null;
        EngineActionElementResponse response = new EngineActionElementResponse();

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            EngineActionHandler<EngineActionElement> handler = instanceHandler.getEngineActionHandler(userId, serverName, methodName);

            response.setElement(handler.getEngineAction(userId,
                                                        engineActionGUID,
                                                        new Date(),
                                                        methodName));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the engine actions that are still in process.
     *
     * @param serverName     name of server instance to route request to
     * @param userId userId of caller
     * @param startFrom starting from element
     * @param pageSize maximum elements to return
     *
     * @return list of engine action elements or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException there was a problem detected by the metadata store.
     */
    public EngineActionElementsResponse getActiveEngineActions(String serverName,
                                                               String userId,
                                                               int    startFrom,
                                                               int    pageSize)
    {
        final String methodName = "getActiveEngineActions";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog auditLog = null;
        EngineActionElementsResponse response = new EngineActionElementsResponse();

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            EngineActionHandler<EngineActionElement> handler = instanceHandler.getEngineActionHandler(userId, serverName, methodName);

            response.setElements(handler.getActiveEngineActions(userId,
                                                                startFrom,
                                                                pageSize,
                                                                new Date(),
                                                                methodName));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the engine actions that are still in process and that have been claimed by this caller's userId.
     * This call is used when the caller restarts.
     *
     * @param serverName     name of server instance to route request to
     * @param userId userId of caller
     * @param governanceEngineGUID unique identifier of governance engine
     * @param startFrom starting from element
     * @param pageSize maximum elements to return
     *
     * @return list of engine action elements or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException there was a problem detected by the metadata store.
     */
    public EngineActionElementsResponse getActiveClaimedEngineActions(String serverName,
                                                                      String userId,
                                                                      String governanceEngineGUID,
                                                                      int    startFrom,
                                                                      int    pageSize)
    {
        final String methodName = "getActiveClaimedEngineActions";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog auditLog = null;
        EngineActionElementsResponse response = new EngineActionElementsResponse();

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            EngineActionHandler<EngineActionElement> handler = instanceHandler.getEngineActionHandler(userId, serverName, methodName);

            response.setElements(handler.getActiveClaimedEngineActions(userId,
                                                                       governanceEngineGUID,
                                                                       startFrom,
                                                                       pageSize,
                                                                       new Date(),
                                                                       methodName));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Request that execution of an engine action is allocated to the caller.
     *
     * @param serverName     name of server instance to route request to
     * @param userId identifier of calling user
     * @param engineActionGUID identifier of the engine action request
     * @param requestBody null request body
     *
     * @return void or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException there was a problem detected by the metadata store.
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse claimEngineAction(String          serverName,
                                          String          userId,
                                          String          engineActionGUID,
                                          NullRequestBody requestBody)
    {
        final String methodName = "claimEngineAction";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog auditLog = null;
        VoidResponse response = new VoidResponse();

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            EngineActionHandler<EngineActionElement> handler = instanceHandler.getEngineActionHandler(userId, serverName, methodName);

            handler.claimEngineAction(userId,
                                      engineActionGUID,
                                      new Date(),
                                      methodName);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Request that execution of an engine action is stopped.
     *
     * @param serverName     name of server instance to route request to
     * @param userId identifier of calling user
     * @param engineActionGUID identifier of the engine action request
     * @param requestBody null request body
     *
     * @return void or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException there was a problem detected by the metadata store.
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse cancelEngineAction(String          serverName,
                                           String          userId,
                                           String          engineActionGUID,
                                           NullRequestBody requestBody)
    {
        final String methodName = "cancelEngineAction";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog auditLog = null;
        VoidResponse response = new VoidResponse();

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            EngineActionHandler<EngineActionElement> handler = instanceHandler.getEngineActionHandler(userId, serverName, methodName);

            handler.cancelEngineAction(userId,
                                       engineActionGUID,
                                       new Date(),
                                       methodName);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }



    /**
     * Update the status of the engine action - providing the caller is permitted.
     *
     * @param serverName     name of server instance to route request to
     * @param userId identifier of calling user
     * @param engineActionGUID identifier of the engine action request
     * @param requestBody new status ordinal
     *
     * @return void or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException there was a problem detected by the metadata store.
     */
    public VoidResponse updateEngineActionStatus(String                        serverName,
                                                 String                        userId,
                                                 String                        engineActionGUID,
                                                 EngineActionStatusRequestBody requestBody)
    {
        final String methodName = "updateEngineActionStatus";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog auditLog = null;
        VoidResponse response = new VoidResponse();

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                EngineActionHandler<EngineActionElement> handler = instanceHandler.getEngineActionHandler(userId, serverName, methodName);

                int statusOrdinal = ActivityStatus.COMPLETED.getOrdinal();

                if (requestBody.getStatus() != null)
                {
                    statusOrdinal = requestBody.getStatus().getOrdinal();
                }

                handler.updateEngineActionStatus(userId,
                                                 engineActionGUID,
                                                 statusOrdinal,
                                                 new Date(),
                                                 methodName);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Update the status of a specific action target. By default, these values are derived from
     * the values for the governance action service.  However, if the governance action service has to process name
     * target elements, then setting the status on each individual target will show the progress of the
     * governance action service.
     *
     * @param serverName     name of server instance to route request to
     * @param userId caller's userId
     * @param requestBody relationship properties
     *
     * @return void or
     *  InvalidParameterException the action target GUID is not recognized
     *  UserNotAuthorizedException the governance action service is not authorized to update the action target properties
     *  PropertyServerException a problem connecting to the metadata store
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse updateActionTargetStatus(String                        serverName,
                                                 String                        userId,
                                                 ActionTargetStatusRequestBody requestBody)
    {
        final String methodName = "updateActionTargetStatus";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog auditLog = null;
        VoidResponse response = new VoidResponse();

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                EngineActionHandler<EngineActionElement> handler = instanceHandler.getEngineActionHandler(userId, serverName, methodName);

                int statusOrdinal = ActivityStatus.COMPLETED.getOrdinal();

                if (requestBody.getStatus() != null)
                {
                    statusOrdinal = requestBody.getStatus().getOrdinal();
                }
                handler.updateActionTargetStatus(userId,
                                                 requestBody.getActionTargetGUID(),
                                                 statusOrdinal,
                                                 requestBody.getStartDate(),
                                                 requestBody.getCompletionDate(),
                                                 requestBody.getCompletionMessage(),
                                                 new Date(),
                                                 methodName);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Declare that all the processing for the governance action service is finished and the status of the work.
     *
     * @param serverName     name of server instance to route request to
     * @param userId caller's userId
     * @param governanceActionGUID unique identifier of the governance action to update
     * @param requestBody completion status enum value, optional guard strings for triggering subsequent action(s) plus
     *                    a list of additional elements to add to the action targets for the next phase
     *
     * @return void or
     *  InvalidParameterException the completion status is null
     *  UserNotAuthorizedException the governance action service is not authorized to update the governance action service status
     *  PropertyServerException a problem connecting to the metadata store
     */
    public VoidResponse recordCompletionStatus(String                      serverName,
                                               String                      userId,
                                               String                      governanceActionGUID,
                                               CompletionStatusRequestBody requestBody)
    {
        final String methodName = "recordCompletionStatus";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog auditLog = null;
        VoidResponse response = new VoidResponse();

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                EngineActionHandler<EngineActionElement> handler = instanceHandler.getEngineActionHandler(userId, serverName, methodName);

                int statusOrdinal = ActivityStatus.COMPLETED.getOrdinal();

                if (requestBody.getStatus() != null)
                {
                    statusOrdinal = requestBody.getStatus().getOpenTypeOrdinal();
                }

                handler.recordCompletionStatus(userId,
                                               governanceActionGUID,
                                               statusOrdinal,
                                               requestBody.getRequestParameters(),
                                               requestBody.getOutputGuards(),
                                               requestBody.getNewActionTargets(),
                                               requestBody.getCompletionMessage(),
                                               new Date(),
                                               methodName);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Create a governance action in the metadata store which will trigger the governance action service
     * associated with the supplied request type.  The governance action remains to act as a record
     * of the actions taken for auditing.
     *
     * @param serverName     name of server instance to route request to
     * @param userId caller's userId
     * @param governanceEngineName name of the governance engine that should execute the request
     * @param requestBody properties for the governance action and to pass to the governance action service
     *
     * @return unique identifier of the governance action or
     *  InvalidParameterException null qualified name
     *  UserNotAuthorizedException this governance action service is not authorized to create a governance action
     *  PropertyServerException a problem with the metadata store
     */
    public GUIDResponse initiateEngineAction(String                          serverName,
                                             String                          userId,
                                             String                          governanceEngineName,
                                             InitiateEngineActionRequestBody requestBody)
    {
        final String methodName = "initiateEngineAction";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog auditLog = null;
        GUIDResponse response = new GUIDResponse();

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                EngineActionHandler<EngineActionElement> handler = instanceHandler.getEngineActionHandler(userId, serverName, methodName);

                Date startTime = requestBody.getStartDate();

                if (startTime == null)
                {
                    startTime = new Date();
                }

                String engineActionGUID = handler.createEngineAction(userId,
                                                                     requestBody.getQualifiedName(),
                                                                     requestBody.getDomainIdentifier(),
                                                                     requestBody.getDisplayName(),
                                                                     requestBody.getDescription(),
                                                                     requestBody.getActionSourceGUIDs(),
                                                                     requestBody.getActionCauseGUIDs(),
                                                                     requestBody.getActionTargets(),
                                                                     null,
                                                                     requestBody.getReceivedGuards(),
                                                                     startTime,
                                                                     governanceEngineName,
                                                                     userId,
                                                                     requestBody.getRequestType(),
                                                                     requestBody.getRequestParameters(),
                                                                     null,
                                                                     null,
                                                                     null,
                                                                     requestBody.getProcessName(),
                                                                     null,
                                                                     null,
                                                                     methodName);

                if (engineActionGUID != null)
                {
                    /*
                     * Since there is no process control, the governance action moves immediately into APPROVED
                     * status, and it is picked up by the listening engine hosts.
                     */
                    handler.approveEngineAction(userId,
                                                engineActionGUID,
                                                requestBody.getQualifiedName(),
                                                null,
                                                requestBody.getReceivedGuards(),
                                                requestBody.getStartDate(),
                                                governanceEngineName,
                                                requestBody.getRequestType(),
                                                requestBody.getRequestParameters(),
                                                null,
                                                requestBody.getProcessName(),
                                                methodName);

                    response.setGUID(engineActionGUID);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Using the named governance action type as a template, initiate an engine action.
     *
     * @param serverName     name of server instance to route request to
     * @param userId caller's userId
     * @param requestBody properties to initiate the new instance of the engine action
     *
     * @return unique identifier of the first governance action of the process or
     *  InvalidParameterException null or unrecognized qualified name of the process
     *  UserNotAuthorizedException this governance action service is not authorized to create a governance action process
     *  PropertyServerException a problem with the metadata store
     */
    public GUIDResponse initiateGovernanceActionType(String                                  serverName,
                                                     String                                  userId,
                                                     InitiateGovernanceActionTypeRequestBody requestBody)
    {
        final String methodName = "initiateGovernanceActionType";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog auditLog = null;
        GUIDResponse response = new GUIDResponse();

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                EngineActionHandler<EngineActionElement> handler = instanceHandler.getEngineActionHandler(userId, serverName, methodName);

                response.setGUID(handler.initiateGovernanceActionType(userId,
                                                                      requestBody.getGovernanceActionTypeQualifiedName(),
                                                                      requestBody.getActionSourceGUIDs(),
                                                                      requestBody.getActionCauseGUIDs(),
                                                                      requestBody.getActionTargets(),
                                                                      requestBody.getRequestParameters(),
                                                                      requestBody.getStartDate(),
                                                                      requestBody.getOriginatorServiceName(),
                                                                      requestBody.getOriginatorEngineName(),
                                                                      methodName));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Using the named governance action process as a template, initiate a chain of governance actions.
     *
     * @param serverName     name of server instance to route request to
     * @param userId caller's userId
     * @param requestBody properties to initiate the new instance of the process
     *
     * @return unique identifier of the governance action process instance or
     *  InvalidParameterException null or unrecognized qualified name of the process
     *  UserNotAuthorizedException this governance action service is not authorized to create a governance action process
     *  PropertyServerException a problem with the metadata store
     */
    public GUIDResponse initiateGovernanceActionProcess(String                             serverName,
                                                        String                             userId,
                                                        InitiateGovernanceActionProcessRequestBody requestBody)
    {
        final String methodName = "initiateGovernanceActionProcess";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AuditLog auditLog = null;
        GUIDResponse response = new GUIDResponse();

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                EngineActionHandler<EngineActionElement> handler = instanceHandler.getEngineActionHandler(userId, serverName, methodName);

                response.setGUID(handler.initiateGovernanceActionProcess(userId,
                                                                         requestBody.getProcessQualifiedName(),
                                                                         requestBody.getActionSourceGUIDs(),
                                                                         requestBody.getActionCauseGUIDs(),
                                                                         requestBody.getActionTargets(),
                                                                         requestBody.getRequestParameters(),
                                                                         requestBody.getStartDate(),
                                                                         requestBody.getOriginatorServiceName(),
                                                                         requestBody.getOriginatorEngineName(),
                                                                         methodName));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }
}
