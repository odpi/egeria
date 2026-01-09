/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.validmetadatavalues;

import org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.DynamicOpenMetadataDataSetConnectorBase;
import org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.ffdc.TabularDataAuditCode;
import org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.ffdc.TabularDataErrorCode;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ValidValueDefinitionClient;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.List;


/**
 * ValidValueDataSetConnector enables interaction with a valid metadata value set as if it is a tabular data set.
 */
public class ValidMetadataValueDataSetConnector extends DynamicOpenMetadataDataSetConnectorBase
{
    private static final String myConnectorName = ValidMetadataValueDataSetConnector.class.getName();


    /**
     * Default constructor
     */
    public ValidMetadataValueDataSetConnector()
    {
        super(myConnectorName);
    }


    /**
     * Call made by the ConnectorProvider to initialize the Connector with the base services.
     *
     * @param connectorInstanceId   unique id for the connector instance   useful for messages etc
     * @param connectionDetails   POJO for the configuration used to create the connector
     */
    public void initialize(String                                 connectorInstanceId,
                           Connection                             connectionDetails) throws ConnectorCheckedException
    {
        super.initialize(connectorInstanceId, connectionDetails);

        super.productDefinition = new ValidMetadataValueDataSetProvider().getProductDefinition(identifierPropertyValue, canonicalName, description);
    }


    /**
     * Refresh any cached values.
     *
     * @throws ConnectorCheckedException unable to refresh
     */
    public void refreshCache() throws ConnectorCheckedException
    {
        final String methodName = "refreshCache";

        /*
         * Retrieve the valid values for the valid metadata value set and store them in the records map.
         */
        try
        {
            List<String> knownGUIDs = new ArrayList<>();

            for (long recordNumber : records.keySet())
            {
                OpenMetadataRootElement validValue = records.get(recordNumber);

                if (validValue != null)
                {
                    knownGUIDs.add(validValue.getElementHeader().getGUID());
                }
            }

            ValidValueDefinitionClient validValueDefinitionClient = connectorContext.getValidValueDefinitionClient(OpenMetadataType.VALID_METADATA_VALUE.typeName);
            int startFrom = 0;
            int pageSize = connectorContext.getMaxPageSize();

            if (pageSize == 0)
            {
                pageSize = 100;
            }

            List<OpenMetadataRootElement> validValueSet = validValueDefinitionClient.getValidValueDefinitionsByIdentifier(identifierPropertyValue,
                                                                                                                          validValueDefinitionClient.getQueryOptions(startFrom, pageSize));

            while (validValueSet != null)
            {
                for (OpenMetadataRootElement member : validValueSet)
                {
                    /*
                     * The record is added if it is not already in the record map.
                     */
                    if ((member != null) && (!knownGUIDs.contains(member.getElementHeader().getGUID())))
                    {
                        records.put((long) records.size(), member);
                    }
                }

                startFrom += pageSize;
                validValueSet = validValueDefinitionClient.getValidValueDefinitionsByIdentifier(identifierPropertyValue,
                                                                                                validValueDefinitionClient.getQueryOptions(startFrom, pageSize));
            }
        }
        catch (Exception error)
        {
            super.logRecord(methodName, TabularDataAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                       error.getClass().getName(),
                                                                                                       methodName,
                                                                                                       error.getMessage()));

            throw new ConnectorCheckedException(TabularDataErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                               error.getClass().getName(),
                                                                                                               methodName,
                                                                                                               error.getMessage()),
                                                this.getClass().getName(),
                                                methodName,
                                                error);
        }
    }
}