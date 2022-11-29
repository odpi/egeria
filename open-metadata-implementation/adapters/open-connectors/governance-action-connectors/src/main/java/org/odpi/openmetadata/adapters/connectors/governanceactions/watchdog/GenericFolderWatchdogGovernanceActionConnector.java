/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.governanceactions.watchdog;

import org.odpi.openmetadata.adapters.connectors.governanceactions.ffdc.GovernanceActionConnectorsAuditCode;
import org.odpi.openmetadata.adapters.connectors.governanceactions.ffdc.GovernanceActionConnectorsErrorCode;
import org.odpi.openmetadata.frameworks.connectors.ffdc.*;
import org.odpi.openmetadata.frameworks.governanceaction.events.*;
import org.odpi.openmetadata.frameworks.governanceaction.ffdc.GovernanceServiceException;
import org.odpi.openmetadata.frameworks.governanceaction.properties.ActionTargetElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.CompletionStatus;
import org.odpi.openmetadata.frameworks.governanceaction.properties.NewActionTarget;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.search.ElementProperties;
import org.odpi.openmetadata.frameworks.governanceaction.search.PropertyHelper;

import java.util.*;

/**
 * GenericFolderWatchdogGovernanceActionConnector listens for events relating to the files in a folder.
 */
public class GenericFolderWatchdogGovernanceActionConnector extends GenericWatchdogGovernanceActionConnector
{
    private String folderName = null;
    private String folderGUID = null;

    private final PropertyHelper propertyHelper = new PropertyHelper();

    /**
     * Indicates that the governance action service is completely configured and can begin processing.
     *
     * This is a standard method from the Open Connector Framework (OCF) so
     * be sure to call super.start() at the start of your overriding version.
     *
     * @throws ConnectorCheckedException there is a problem within the governance action service.
     */
    @Override
    public void start() throws ConnectorCheckedException
    {
        final String methodName = "start";

        super.validateContext(governanceContext);

        Map<String, Object> configurationProperties = connectionProperties.getConfigurationProperties();

        /*
         * The folder name to listen to can come from multiple sources.  The configuration properties are set when the governance service is
         * registered in open metadata.  They are override default values coded in the connector.
         */
        if (configurationProperties != null)
        {
            Object folderNameOption = configurationProperties.get(GenericFolderWatchdogGovernanceActionProvider.FOLDER_NAME_PROPERTY);

            if (folderNameOption != null)
            {
                folderName = folderNameOption.toString();
            }
        }

        /*
         * Next the request parameters come either from the governance engine definition or the caller.
         */
        if (governanceContext.getRequestParameters() != null)
        {
            Map<String, String> requestParameters = governanceContext.getRequestParameters();

            for (String requestParameterName : requestParameters.keySet())
            {
                if (requestParameterName != null)
                {
                    /*
                     * The process names and interesting type name will be processed in the super class start method (see getProperty() method).
                     */
                    if (GenericFolderWatchdogGovernanceActionProvider.FOLDER_NAME_PROPERTY.equals(requestParameterName))
                    {
                        folderName = requestParameters.get(requestParameterName);
                    }
                }
            }
        }

        /*
         * Action targets are set up by a previous governance action running in a governance action process.
         */
        List<ActionTargetElement> actionTargetElements = governanceContext.getActionTargetElements();

        if (actionTargetElements != null)
        {
            for (ActionTargetElement actionTargetElement : actionTargetElements)
            {
                if (actionTargetElement != null)
                {
                    if (GenericFolderWatchdogGovernanceActionProvider.FOLDER_TARGET_PROPERTY.equals(actionTargetElement.getActionTargetName()))
                    {
                        folderGUID = actionTargetElement.getActionTargetGUID();
                    }
                }
            }
        }

        if ((folderGUID == null) && (folderName != null))
        {
            try
            {
                folderGUID = governanceContext.getOpenMetadataStore().getMetadataElementGUIDByUniqueName(folderName, "pathName", false, false, null);

                if (folderGUID == null)
                {
                    throw new InvalidParameterException(GovernanceActionConnectorsErrorCode.FOLDER_ELEMENT_NOT_FOUND.getMessageDefinition(folderName),
                                                        this.getClass().getName(),
                                                        methodName,
                                                        "folderName");
                }
            }
            catch (OCFCheckedExceptionBase error)
            {
                throw new ConnectorCheckedException(error.getMessage(), error);
            }
        }

        /*
         * This registers the listener
         */
        super.start("DataFile");
    }


    /**
     * This method is called each time a potentially new asset is received.  It triggers a governance action process to validate and
     * enrich the asset as required.
     *
     * @param event event containing details of a change to an open metadata element.
     *
     * @throws GovernanceServiceException reports that the event can not be processed (this is logged but
     *                                    no other action is taken).  The listener will continue to be
     *                                    called until the watchdog governance action service declares it is complete
     *                                    or administrator action shuts down the service.
     */
    void processEvent(WatchdogGovernanceEvent event) throws GovernanceServiceException
    {
        final String methodName = "processEvent";

        if (! completed)
        {
            if (event instanceof WatchdogMetadataElementEvent)
            {
                WatchdogMetadataElementEvent metadataElementEvent = (WatchdogMetadataElementEvent) event;

                String fileGUID = metadataElementEvent.getMetadataElement().getElementGUID();

                if ((matchFolderToFileName(metadataElementEvent.getMetadataElement().getElementProperties())) || (fileInFolder(fileGUID)))
                {
                    Map<String, String>   requestParameters = new HashMap<>();
                    List<NewActionTarget> actionTargets = new ArrayList<>();

                    NewActionTarget actionTarget = new NewActionTarget();

                    actionTarget.setActionTargetGUID(fileGUID);
                    actionTarget.setActionTargetName(actionTargetName);
                    actionTargets.add(actionTarget);

                    if (metadataElementEvent.getEventType() == WatchdogEventType.NEW_ELEMENT)
                    {
                        initiateProcess(newElementProcessName,
                                        null,
                                        actionTargets);
                    }
                    else if (metadataElementEvent.getEventType() == WatchdogEventType.UPDATED_ELEMENT_PROPERTIES)
                    {
                        ElementProperties previousElementProperties = null;

                        if (metadataElementEvent.getPreviousMetadataElement() != null)
                        {
                            previousElementProperties = metadataElementEvent.getPreviousMetadataElement().getElementProperties();
                        }

                        requestParameters.put("ChangedProperties", this.diffProperties(previousElementProperties,
                                                                                       metadataElementEvent.getMetadataElement().getElementProperties()));

                        initiateProcess(updatedElementProcessName,
                                        requestParameters,
                                        actionTargets);
                    }
                    else if (metadataElementEvent.getEventType() == WatchdogEventType.DELETED_ELEMENT)
                    {
                        initiateProcess(deletedElementProcessName,
                                        null,
                                        actionTargets);
                    }
                    else
                    {
                        WatchdogClassificationEvent classificationEvent = (WatchdogClassificationEvent) event;

                        requestParameters.put("ClassificationName", classificationEvent.getChangedClassification().getClassificationName());

                        if (metadataElementEvent.getEventType() == WatchdogEventType.NEW_CLASSIFICATION)
                        {
                            initiateProcess(classifiedElementProcessName,
                                            requestParameters,
                                            actionTargets);
                        }
                        else if (metadataElementEvent.getEventType() == WatchdogEventType.UPDATED_CLASSIFICATION_PROPERTIES)
                        {
                            ElementProperties previousElementProperties = null;

                            if (classificationEvent.getPreviousClassification() != null)
                            {
                                previousElementProperties = classificationEvent.getPreviousClassification().getClassificationProperties();
                            }

                            requestParameters.put("ChangedProperties", this.diffProperties(previousElementProperties,
                                                                                           classificationEvent.getChangedClassification().getClassificationProperties()));


                            initiateProcess(reclassifiedElementProcessName,
                                            requestParameters,
                                            actionTargets);
                        }
                        else if (metadataElementEvent.getEventType() == WatchdogEventType.DELETED_CLASSIFICATION)
                        {
                            initiateProcess(declassifiedElementProcessName,
                                            requestParameters,
                                            actionTargets);
                        }
                    }
                }
            }

            if (completed)
            {
                try
                {
                    List<String> outputGuards = new ArrayList<>();
                    outputGuards.add(GenericWatchdogGovernanceActionProvider.MONITORING_FAILED);

                    governanceContext.recordCompletionStatus(CompletionStatus.FAILED, outputGuards);
                }
                catch (Exception error)
                {
                    if (auditLog != null)
                    {
                        auditLog.logException(methodName,
                                              GovernanceActionConnectorsAuditCode.UNABLE_TO_SET_COMPLETION_STATUS.getMessageDefinition(governanceServiceName,
                                                                                                                                       error.getClass().getName(),
                                                                                                                                       error.getMessage()),
                                              error);
                    }
                }
            }
        }
    }


    /**
     * Use the folder name to match against path name.
     *
     * @param elementProperties properties from the element event.
     * @return flag indicating an appropriate name march
     */
    private boolean matchFolderToFileName(ElementProperties elementProperties)
    {
        final String methodName = "matchFolderToFileName";

        if (folderName == null)
        {
            /*
             * No specific folder is being monitored.
             */
            return true;
        }

        String fullPathName = propertyHelper.getStringProperty(governanceServiceName, "pathName", elementProperties, methodName);

        if (fullPathName == null)
        {
            return false;
        }

        if (! fullPathName.startsWith(folderName))
        {
            /*
             * Not in the same file structure.
             */
            return false;
        }

        if (GenericFolderWatchdogGovernanceActionProvider.NESTED_REQUEST_TYPE.equals(governanceContext.getRequestType()))
        {
            /*
             * The file may be in any subdirectory under the monitored folder.
             */
            return true;
        }
        else if (GenericFolderWatchdogGovernanceActionProvider.DIRECT_REQUEST_TYPE.equals(governanceContext.getRequestType()))
        {
            /*
             * Only looking for files directly in the requested folder. Note: this does not work with MS files.
             */
            String[] splitFileName = fullPathName.split("/");
            String[] splitHigh = folderName.split("/");

            return (splitHigh.length == splitFileName.length - 1);
        }

        return false;
    }


    /**
     * Determine if the file is in the folder.
     *
     * @param fileGUID unique identifier of file
     * @return boolean flag to indicate if the file is in the monitored folder
     * @throws GovernanceServiceException reports that the event can not be processed (this is logged but
     *                                    no other action is taken).  The listener will continue to be
     *                                    called until the watchdog governance action service declares it is complete
     *                                    or administrator action shuts down the service.
     */
    private boolean fileInFolder(String fileGUID) throws GovernanceServiceException
    {
        try
        {
            String parentFolderGUID = getFolderGUID(fileGUID, "NestedFile");

            if (GenericFolderWatchdogGovernanceActionProvider.DIRECT_REQUEST_TYPE.equals(governanceContext.getRequestType()))
            {
                /*
                 * Only looking for files directly in the requested folder.
                 */
                if (parentFolderGUID != null)
                {
                    return parentFolderGUID.equals(folderGUID);
                }
            }
            else
            {
                /*
                 * The file may be in a subdirectory under the monitored folder.
                 */
                while (parentFolderGUID != null)
                {
                    if (parentFolderGUID.equals(folderGUID))
                    {
                        return true;
                    }

                    parentFolderGUID = getFolderGUID(parentFolderGUID, "FolderHierarchy");
                }
            }

            return false;
        }
        catch (OCFCheckedExceptionBase error)
        {
            throw new GovernanceServiceException(error.getMessage(), error);
        }
    }


    /**
     * Return the unique identifier of a folder by navigating from the file.
     *
     * @param fileGUID file unique identifier
     *
     * @return unique identifier of the folder
     * @throws InvalidParameterException one of the parameters passed to open metadata is invalid (probably a bug in this code)
     * @throws UserNotAuthorizedException the userId for the connector does not have the authority it needs
     * @throws PropertyServerException there is a problem with the metadata server(s)
     */
    private String getFolderGUID(String  fileGUID,
                                 String  relationshipName) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        String folderGUID = null;

        List<RelatedMetadataElement> relatedMetadataElementList = governanceContext.getOpenMetadataStore().getRelatedMetadataElements(fileGUID,
                                                                                                                                      2,
                                                                                                                                      relationshipName,
                                                                                                                                      false,
                                                                                                                                      false,
                                                                                                                                      null,
                                                                                                                                      0,
                                                                                                                                      0);

        /*
         * It is possible that the folders have not yet been added - give the cataloguing process time to complete.
         */
        if (relatedMetadataElementList == null)
        {
            try
            {
                Thread.sleep(500);
            }
            catch (Exception interrupt)
            {
                // ignore
            }

            relatedMetadataElementList = governanceContext.getOpenMetadataStore().getRelatedMetadataElements(fileGUID,
                                                                                                             2,
                                                                                                             relationshipName,
                                                                                                             false,
                                                                                                             false,
                                                                                                             null,
                                                                                                             0,
                                                                                                             0);
        }

        if (relatedMetadataElementList != null)
        {
            for (RelatedMetadataElement relatedMetadataElement : relatedMetadataElementList)
            {
                if (relatedMetadataElement != null)
                {
                    folderGUID = relatedMetadataElement.getElementProperties().getElementGUID();
                }
            }
        }

        return folderGUID;
    }


    /**
     * Disconnect is called either because this governance action service called governanceContext.recordCompletionStatus()
     * or the administrator requested this governance action service stop running or the hosting server is shutting down.
     *
     * If disconnect completes before the governance action service records
     * its completion status then the governance action service is restarted either at the administrator's request or the next time the server starts.
     * If you do not want this governance action service restarted, be sure to record the completion status in disconnect().
     *
     * The disconnect() method is a standard method from the Open Connector Framework (OCF).  If you need to override this method
     * be sure to call super.disconnect() in your version.
     *
     * @throws ConnectorCheckedException there is a problem within the governance action service.
     */
    @Override
    public  void disconnect() throws ConnectorCheckedException
    {
        super.disconnect();
    }
}
