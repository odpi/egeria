/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.governanceactions.provisioning;

import org.odpi.openmetadata.adapters.connectors.governanceactions.ffdc.GovernanceActionConnectorsAuditCode;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.ReadableTabularDataSource;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.TabularDataCollection;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.WritableTabularDataSource;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.opengovernance.GeneralGovernanceActionService;
import org.odpi.openmetadata.frameworks.opengovernance.properties.ActionTargetElement;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.CompletionStatus;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.OMFCheckedExceptionBase;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ProvisionTabularDataSetGovernanceActionConnector copies data from one tabular data set to another.
 */
public class ProvisionTabularDataSetGovernanceActionConnector extends GeneralGovernanceActionService
{
    /*
     * TODO What additional lineage support is necessary beyond the relationships supported by the engine action?
     */
    private String  topLevelProcessName                  = this.getClass().getName();
    private String  informationSupplyChainQualifiedName  = null;
    private String  topLevelProcessTemplateQualifiedName = null;


    /*
     * TODO This describes the default lineage pattern
     */
    private boolean createLineage = true;
    private boolean childProcessLineage = true;
    private boolean columnLevelLineage = true;


    /**
     * Indicates that the governance action service is completely configured and can begin processing.
     * This is a standard method from the Open Connector Framework (OCF) so
     * be sure to call super.start() at the start of your overriding version.
     *
     * @throws ConnectorCheckedException a problem within the governance action service.
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    @Override
    public void start() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        final String methodName = "start";

        super.start();

        List<String>              outputGuards = new ArrayList<>();
        CompletionStatus          completionStatus;
        AuditLogMessageDefinition completionMessage = null;


        /*
         * Retrieve the source file and destination folder from either the request parameters or the action targets.  If both
         * are specified then the action target elements take priority.
         */
        if (governanceContext.getRequestParameters() != null)
        {
            Map<String, String> requestParameters = governanceContext.getRequestParameters();

            for (String requestParameterName : requestParameters.keySet())
            {

                if (ProvisionTabularDataSetRequestParameter.INFORMATION_SUPPLY_CHAIN_QUALIFIED_NAME.getName().equals(requestParameterName))
                {
                    informationSupplyChainQualifiedName = requestParameters.get(requestParameterName);
                }
                else if (ProvisionTabularDataSetRequestParameter.NO_LINEAGE.getName().equals(requestParameterName))
                {
                    createLineage = false;
                }
                else if (ProvisionTabularDataSetRequestParameter.TOP_LEVEL_PROCESS_NAME.getName().equals(requestParameterName))
                {
                    topLevelProcessName = requestParameters.get(requestParameterName);
                }
                else if (ProvisionTabularDataSetRequestParameter.TOP_LEVEL_PROCESS_TEMPLATE_NAME.getName().equals(requestParameterName))
                {
                    topLevelProcessTemplateQualifiedName = requestParameters.get(requestParameterName);
                }
                else if (ProvisionTabularDataSetRequestParameter.TOP_LEVEL_PROCESS_ONLY_LINEAGE.getName().equals(requestParameterName))
                {
                    childProcessLineage = false;
                }
                else if (ProvisionTabularDataSetRequestParameter.IGNORE_COLUMN_LEVEL_LINEAGE.getName().equals(requestParameterName))
                {
                    columnLevelLineage = false;
                }
            }
        }

        OpenMetadataElement sourceMetadataElement = null;
        OpenMetadataElement destinationMetadataElement = null;

        if (governanceContext.getActionTargetElements() != null)
        {
            for (ActionTargetElement actionTargetElement : governanceContext.getActionTargetElements())
            {
                if (actionTargetElement != null)
                {
                    if (ProvisionTabularDatasetActionTarget.SOURCE_DATA_SET.getName().equals(actionTargetElement.getActionTargetName()))
                    {
                        sourceMetadataElement = actionTargetElement.getTargetElement();
                    }
                    else if (ProvisionTabularDatasetActionTarget.DESTINATION_DATA_SET.getName().equals(actionTargetElement.getActionTargetName()))
                    {
                        destinationMetadataElement = actionTargetElement.getTargetElement();
                    }
                }
            }
        }

        if ((sourceMetadataElement == null) || (destinationMetadataElement == null))
        {
            outputGuards.add(ProvisionTabularDataSetGuard.PROVISIONING_COMPLETE.getName());
            completionStatus = ProvisionTabularDataSetGuard.PROVISIONING_COMPLETE.getCompletionStatus();
            completionMessage = GovernanceActionConnectorsAuditCode.NO_TARGETS.getMessageDefinition(governanceServiceName);
        }
        else
        {
            try
            {
                ReadableTabularDataSource sourceConnector      = (ReadableTabularDataSource) governanceContext.getConnectorForAsset(sourceMetadataElement.getElementGUID());
                WritableTabularDataSource destinationConnector = (WritableTabularDataSource) governanceContext.getConnectorForAsset(sourceMetadataElement.getElementGUID());

                long sourceRecordCount = sourceConnector.getRecordCount();
                long destinationRecordCount = destinationConnector.getRecordCount();

                if (destinationConnector instanceof TabularDataCollection tabularDataCollection)
                {
                    tabularDataCollection.setTableName(sourceConnector.getTableName(),
                                                       sourceConnector.getTableDescription());
                }

                destinationConnector.setColumnDescriptions(sourceConnector.getColumnDescriptions());

                if (sourceRecordCount >= destinationRecordCount)
                {
                    for (long rowNumber=0; rowNumber < destinationRecordCount ; rowNumber++)
                    {
                        destinationConnector.writeRecord(rowNumber, sourceConnector.readRecord(rowNumber));
                    }

                    for (long rowNumber = destinationRecordCount; rowNumber < sourceRecordCount ; rowNumber ++)
                    {
                        destinationConnector.appendRecord(sourceConnector.readRecord(rowNumber));
                    }
                }
                else
                {
                    for (long rowNumber=0; rowNumber < sourceRecordCount ; rowNumber++)
                    {
                        destinationConnector.writeRecord(rowNumber, sourceConnector.readRecord(rowNumber));
                    }

                    for (long rowNumber = sourceRecordCount; rowNumber < destinationRecordCount ; rowNumber ++)
                    {
                        destinationConnector.deleteRecord(rowNumber);
                    }
                }


                outputGuards.add(ProvisionTabularDataSetGuard.PROVISIONING_COMPLETE.getName());
                completionStatus = ProvisionTabularDataSetGuard.PROVISIONING_COMPLETE.getCompletionStatus();
            }
            catch (Exception error)
            {
                completionMessage = GovernanceActionConnectorsAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(governanceServiceName,
                                                                                                                  error.getClass().getName(),
                                                                                                                  methodName,
                                                                                                                  error.getMessage());
                super.logExceptionRecord(methodName, completionMessage, error);

                outputGuards.add(ProvisionTabularDataSetGuard.PROVISIONING_FAILED_EXCEPTION.getName());
                completionStatus = ProvisionTabularDataSetGuard.PROVISIONING_FAILED_EXCEPTION.getCompletionStatus();
            }
        }

        try
        {
            governanceContext.recordCompletionStatus(completionStatus, outputGuards, null, null, completionMessage);
        }
        catch (OMFCheckedExceptionBase error)
        {
            throw new ConnectorCheckedException(error.getReportedErrorMessage(), error);
        }
    }
}
