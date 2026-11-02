/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.governanceactions.watchdog;

import org.odpi.openmetadata.adapters.connectors.governanceactions.ffdc.GovernanceActionConnectorsAuditCode;
import org.odpi.openmetadata.adapters.connectors.governanceactions.ffdc.GovernanceActionConnectorsErrorCode;
import org.odpi.openmetadata.frameworks.connectors.ffdc.*;
import org.odpi.openmetadata.frameworks.opengovernance.ffdc.GovernanceServiceException;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataEventType;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataOutTopicEvent;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElementList;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.OMFCheckedExceptionBase;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.opengovernance.properties.ActionTargetElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.NewActionTarget;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openwatchdog.controls.WatchdogActionGuard;
import org.odpi.openmetadata.frameworks.openwatchdog.controls.WatchdogRequestParameter;

import java.util.*;

/**
 * GenericFolderWatchdogGovernanceActionConnector listens for events relating to the files in a folder.
 */
public class GenericFolderWatchdogGovernanceActionConnector extends GenericWatchdogGovernanceActionConnector
{
    private String folderName  = null;
    private String folderGUID  = null;
    private String requestType = null;

    /**
     * Indicates that the watchdog action service is completely configured and can begin processing.
     * This is a standard method from the Open Connector Framework (OCF) so
     * be sure to call super.start() at the start of your overriding version.
     *
     * @throws ConnectorCheckedException a problem within the watchdog action service.
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    @Override
    public void start() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        final String methodName = "start";

        super.start();

        requestType = watchdogContext.getRequestType();

        /*
         * The folder name to listen to can come from multiple sources.  The configuration properties are set when the
         * watchdog action service is registered in open metadata.  The request parameters, which come either from the
         * governance engine definition or the caller, override them.  The process names and interesting type name are
         * processed in the super class start method.
         */
        folderName = getProperty(GenericFolderRequestParameter.FOLDER_NAME.getName(), null);

        /*
         * Action targets are set up by a previous governance action running in a governance action process.
         */
        List<ActionTargetElement> actionTargetElements = watchdogContext.getActionTargetElements();

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
                folderGUID = watchdogContext.getOpenMetadataStore().getMetadataElementGUIDByUniqueName(folderName, OpenMetadataProperty.PATH_NAME.name);

                if (folderGUID == null)
                {
                    throw new InvalidParameterException(GovernanceActionConnectorsErrorCode.FOLDER_ELEMENT_NOT_FOUND.getMessageDefinition(folderName),
                                                        this.getClass().getName(),
                                                        methodName,
                                                        OpenMetadataProperty.PATH_NAME.name);
                }
            }
            catch (InvalidParameterException error)
            {
                /*
                 * There is nothing to watch, so the request is invalid rather than failed.
                 */
                completeMonitoring(WatchdogActionGuard.MONITORING_INVALID, error.getMessage());
                return;
            }
            catch (OMFCheckedExceptionBase error)
            {
                throw new ConnectorCheckedException(error.getMessage(), error);
            }
        }

        /*
         * This registers the listener
         */
        super.startMonitoring(OpenMetadataType.DATA_FILE.typeName);
    }


    /**
     * This method is called each time a potentially new asset is received.  It triggers a governance action process to validate and
     * enrich the asset as required.
     *
     * @param metadataElementEvent event containing details of a change to an open metadata element.
     *
     * @throws GovernanceServiceException reports that the event can not be processed (this is logged but
     *                                    no other action is taken).  The listener will continue to be
     *                                    called until the watchdog governance action service declares it is complete
     *                                    or administrator action shuts down the service.
     */
    @Override
    public void processEvent(OpenMetadataOutTopicEvent metadataElementEvent) throws GovernanceServiceException
    {
        final String methodName = "processEvent";

        if (! completed)
        {
            try
            {
                String fileGUID = metadataElementEvent.getElementHeader().getGUID();

                if ((matchFolderToFileName(metadataElementEvent.getElementProperties())) || (fileInFolder(fileGUID)))
                {
                    /*
                     * A copy because the parameters are added to below, and the context's map is shared by every event.
                     */
                    Map<String, String> requestParameters = new HashMap<>();

                    if (watchdogContext.getRequestParameters() != null)
                    {
                        requestParameters.putAll(watchdogContext.getRequestParameters());
                    }

                    List<NewActionTarget> actionTargets = new ArrayList<>();

                    NewActionTarget actionTarget = new NewActionTarget();

                    actionTarget.setActionTargetGUID(fileGUID);
                    actionTarget.setActionTargetName(actionTargetName);
                    actionTargets.add(actionTarget);

                    if (metadataElementEvent.getEventType() == OpenMetadataEventType.NEW_ELEMENT_CREATED)
                    {
                        initiateProcess(newElementProcessName,
                                        requestParameters,
                                        actionTargets);
                    }
                    else if (metadataElementEvent.getEventType() == OpenMetadataEventType.ELEMENT_UPDATED)
                    {
                        ElementProperties previousElementProperties = metadataElementEvent.getPreviousElementProperties();

                        requestParameters.put(WatchdogRequestParameter.CHANGED_PROPERTIES.getName(), this.diffProperties(previousElementProperties,
                                                                                       metadataElementEvent.getElementProperties()));

                        initiateProcess(updatedElementProcessName,
                                        requestParameters,
                                        actionTargets);
                    }
                    else if (metadataElementEvent.getEventType() == OpenMetadataEventType.ELEMENT_DELETED)
                    {
                        /*
                         * A deleted file cannot be an action target, so it is identified through the request
                         * parameters instead.
                         */
                        requestParameters.put("ElementGUID", fileGUID);
                        requestParameters.put("ElementTypeName", metadataElementEvent.getElementHeader().getType().getTypeName());

                        initiateProcess(deletedElementProcessName,
                                        requestParameters,
                                        null);
                    }
                    else if (metadataElementEvent.getClassificationName() != null)
                    {
                        requestParameters.put("ClassificationName", metadataElementEvent.getClassificationName());

                        if (metadataElementEvent.getEventType() == OpenMetadataEventType.ELEMENT_CLASSIFIED)
                        {
                            initiateProcess(classifiedElementProcessName,
                                            requestParameters,
                                            actionTargets);
                        }
                        else if (metadataElementEvent.getEventType() == OpenMetadataEventType.ELEMENT_RECLASSIFIED)
                        {
                            requestParameters.put(WatchdogRequestParameter.CHANGED_PROPERTIES.getName(), this.diffProperties(metadataElementEvent.getPreviousClassificationProperties(),
                                                                                                            metadataElementEvent.getClassificationProperties()));


                            initiateProcess(reclassifiedElementProcessName,
                                            requestParameters,
                                            actionTargets);
                        }
                        else if (metadataElementEvent.getEventType() == OpenMetadataEventType.ELEMENT_DECLASSIFIED)
                        {
                            initiateProcess(declassifiedElementProcessName,
                                            requestParameters,
                                            actionTargets);
                        }
                    }

                }
            }
            catch (Exception error)
            {
                /*
                 * The watchdog runs until it is cancelled, so a problem with one event is logged and the
                 * monitoring carries on with the next.
                 */
                logExceptionRecord(methodName,
                                   GovernanceActionConnectorsAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(watchdogActionServiceName,
                                                                                                                 error.getClass().getName(),
                                                                                                                 methodName,
                                                                                                                 error.getMessage()),
                                   error);
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

        String fullPathName = propertyHelper.getStringProperty(watchdogActionServiceName,
                                                               OpenMetadataProperty.PATH_NAME.name,
                                                               elementProperties,
                                                               methodName);

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

        if (GenericFolderRequestType.NESTED_REQUEST_TYPE.getRequestType().equals(requestType))
        {
            /*
             * The file may be in any subdirectory under the monitored folder.
             */
            return true;
        }
        else if (GenericFolderRequestType.DIRECT_REQUEST_TYPE.getRequestType().equals(requestType))
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
            String parentFolderGUID = getFolderGUID(fileGUID, OpenMetadataType.NESTED_FILE_RELATIONSHIP.typeName);

            if (GenericFolderRequestType.DIRECT_REQUEST_TYPE.getRequestType().equals(requestType))
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

                    parentFolderGUID = getFolderGUID(parentFolderGUID, OpenMetadataType.FOLDER_HIERARCHY_RELATIONSHIP.typeName);
                }
            }

            return false;
        }
        catch (OMFCheckedExceptionBase error)
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
     * @throws PropertyServerException a problem with the metadata server(s)
     */
    private String getFolderGUID(String  fileGUID,
                                 String  relationshipName) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        String folderGUID = null;

        RelatedMetadataElementList relatedMetadataElementList = watchdogContext.getOpenMetadataStore().getRelatedMetadataElements(fileGUID,
                                                                                                                                    2,
                                                                                                                                    relationshipName,
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

            relatedMetadataElementList = watchdogContext.getOpenMetadataStore().getRelatedMetadataElements(fileGUID,
                                                                                                             2,
                                                                                                             relationshipName,
                                                                                                             0,
                                                                                                             0);
        }

        if ((relatedMetadataElementList != null) && (relatedMetadataElementList.getElementList() != null))
        {
            for (RelatedMetadataElement relatedMetadataElement : relatedMetadataElementList.getElementList())
            {
                if (relatedMetadataElement != null)
                {
                    folderGUID = relatedMetadataElement.getElement().getElementGUID();
                }
            }
        }

        return folderGUID;
    }
}
