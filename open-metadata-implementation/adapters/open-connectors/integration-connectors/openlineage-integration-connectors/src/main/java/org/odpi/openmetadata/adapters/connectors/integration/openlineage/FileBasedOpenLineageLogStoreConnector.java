/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.openlineage;

import org.apache.commons.io.FileUtils;
import org.odpi.openmetadata.adapters.connectors.integration.openlineage.ffdc.OpenLineageIntegrationConnectorErrorCode;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.integrationservices.lineage.properties.OpenLineageRunEvent;

import java.io.File;
import java.time.ZonedDateTime;
import java.util.*;


/**
 * FileBasedOpenLineageLogStoreConnector provides a connector implementation for a file based open lineage log.
 * The open lineage log is stored in a directory and each open lineage event record is stored as a file with a filename built
 * from the record's unique identifier (runId), time and status. The record is stored in a subdirectory that is made from the namespace and job.
 */
public class FileBasedOpenLineageLogStoreConnector extends OpenLineageLogStoreConnectorBase
{
    /**
     * Default constructor used by the connector provider.
     */
    public FileBasedOpenLineageLogStoreConnector()
    {
        super();
    }


    /**
     * Informs the subclasses that there is a new destination - in case they need to do special setup.
     *
     * @param logStoreDirectoryName new destination
     * @throws ConnectorCheckedException new destination not valid
     */
    @Override
    protected void newDestinationIdentified(String logStoreDirectoryName) throws ConnectorCheckedException
    {
        final String methodName = "newDestinationIdentified";

        try
        {
            File logStoreDirectory = new File(logStoreDirectoryName);

            FileUtils.forceMkdir(logStoreDirectory);
        }
        catch (Exception error)
        {
            throw new ConnectorCheckedException(
                    OpenLineageIntegrationConnectorErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                       error.getClass().getName(),
                                                                                                       methodName,
                                                                                                       error.getMessage()),
                    this.getClass().getName(),
                    methodName,
                    error);
        }
    }


    /**
     * Store the open lineage event in the open lineage log store.  If the raw event is null, a json version of the open lineage event is
     * generated using the Egeria beans.
     *
     * @param openLineageEvent event formatted using Egeria beans
     * @param rawEvent event in Json form from the originator - may have facets that are not known to Egeria
     *
     * @throws InvalidParameterException indicates that the openLineageEvent parameter is invalid.
     * @throws PropertyServerException  indicates that the  log store is not available or has an error.
     */
    @Override
    public void storeEvent(OpenLineageRunEvent openLineageEvent,
                           String              rawEvent,
                           String              logStoreDirectoryName) throws InvalidParameterException,
                                                                             PropertyServerException
    {
        final String methodName = "storeEvent";

        if (rawEvent != null)
        {
            String namespace = "default-namespace/";
            String jobName = "unformatted/";
            String runId = UUID.randomUUID().toString();
            String eventType = "UNKNOWN";

            ZonedDateTime zonedDateTime = ZonedDateTime.now();

            try
            {
                if (openLineageEvent != null)
                {
                    String eventTime = openLineageEvent.getEventTime();
                    zonedDateTime = ZonedDateTime.parse(eventTime);

                    eventType = openLineageEvent.getEventType();
                    if ((openLineageEvent.getRun() != null) && (openLineageEvent.getRun().getRunId() != null))
                    {
                        runId = openLineageEvent.getRun().getRunId().toString();
                    }

                    if (openLineageEvent.getJob() != null)
                    {
                        if (openLineageEvent.getJob().getName() != null)
                        {
                            jobName = openLineageEvent.getJob().getName();
                        }
                        if (openLineageEvent.getJob().getNamespace() != null)
                        {
                            namespace = openLineageEvent.getJob().getNamespace();
                        }
                    }
                }

                String timestamp = zonedDateTime.getYear() + "-" +
                                           zonedDateTime.getMonthValue() +  "-" +
                                           zonedDateTime.getDayOfMonth() +  ":" +
                                           zonedDateTime.getHour() +  "-" +
                                           zonedDateTime.getMinute() +  "-" +
                                           zonedDateTime.getSecond() +  ":" +
                                           zonedDateTime.getNano();

                File logStoreFile = new File(logStoreDirectoryName + "/" + namespace + "/" + jobName + "/" + runId + "-" + timestamp + "-" + eventType + ".openlineageevent");

                FileUtils.writeStringToFile(logStoreFile, rawEvent, (String) null, false);
            }
            catch (Exception error)
            {
                final String parameterName = "openLineageEvent";

                Map<String, Object> additionalProperties = new HashMap<>();

                if (openLineageEvent != null)
                {
                    additionalProperties.put(parameterName, openLineageEvent.toString());
                }

                throw new PropertyServerException(
                        OpenLineageIntegrationConnectorErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                           error.getClass().getName(),
                                                                                                           methodName,
                                                                                                           error.getMessage()),
                        this.getClass().getName(),
                        methodName,
                        error,
                        additionalProperties);
            }
        }
        else
        {
            super.logNoRawEvent(openLineageEvent);
        }
    }
}
