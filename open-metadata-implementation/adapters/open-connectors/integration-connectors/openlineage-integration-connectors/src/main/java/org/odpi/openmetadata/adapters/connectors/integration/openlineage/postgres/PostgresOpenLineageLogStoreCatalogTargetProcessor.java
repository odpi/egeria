/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.openlineage.postgres;

import org.odpi.openmetadata.adapters.connectors.integration.openlineage.ffdc.OpenLineageIntegrationConnectorAuditCode;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.JDBCResourceConnector;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.controls.JDBCConfigurationProperty;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.integration.connectors.CatalogTargetProcessorBase;
import org.odpi.openmetadata.frameworks.integration.context.CatalogTargetContext;
import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageRunEvent;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.opengovernance.properties.CatalogTarget;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * Stores open lineage events in one PostgreSQL database schema.  The schema and its tables are created, if they do
 * not exist, when the catalog target starts.  Each event is stored, with its contribution to the digest tables, in
 * one transaction.  Each refresh deletes the records that are older than the daysToCleanUp configuration property.
 */
public class PostgresOpenLineageLogStoreCatalogTargetProcessor extends CatalogTargetProcessorBase
{
    private static final long         ONE_DAY       = 24L * 60L * 60L * 1000L;

    private final Map<String, Object>               connectorConfigurationProperties;
    private       PostgresOpenLineageLogStoreSchema schema         = null;
    private       Long                              daysToCleanUp  = null;

    /*
     * Set last, once the log store is ready, by start() or a later refresh() on the refresh thread; read by
     * storeEvent() on the thread delivering open lineage events.
     */
    private volatile PostgresOpenLineageLogStoreWriter writer      = null;


    /**
     * Constructor
     *
     * @param template catalog target
     * @param catalogTargetContext specialized context for this catalog target
     * @param connectorToTarget connector to access the target resource
     * @param connectorName name of this integration connector
     * @param auditLog logging destination
     * @param connectorConfigurationProperties configuration properties of the integration connector
     */
    public PostgresOpenLineageLogStoreCatalogTargetProcessor(CatalogTarget        template,
                                                             CatalogTargetContext catalogTargetContext,
                                                             Connector            connectorToTarget,
                                                             String               connectorName,
                                                             AuditLog             auditLog,
                                                             Map<String, Object>  connectorConfigurationProperties)
    {
        super(template, catalogTargetContext, connectorToTarget, connectorName, auditLog);

        this.connectorConfigurationProperties = connectorConfigurationProperties;
    }


    /**
     * Create the schema and its tables if they do not exist.  The resource connector has been started by this point.
     * If this fails, each refresh tries again.
     *
     * @throws ConnectorCheckedException the connector detected a problem.
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    @Override
    public void start() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        super.start();

        setUpLogStore("start");
    }


    /**
     * Validate the catalog target and create the schema and its tables if they do not exist.  Problems are logged
     * and leave the writer unset, so no events are stored until a later attempt succeeds.  This means a log store
     * that could not be set up when the catalog target started (for example, because the database user had not
     * been granted permission to create the schema yet) is picked up at a later refresh without restarting the
     * connector.
     *
     * @param methodName calling method
     */
    private void setUpLogStore(String methodName)
    {
        if (! (super.getConnectorToTarget() instanceof JDBCResourceConnector jdbcResourceConnector))
        {
            logRecord(methodName,
                      OpenLineageIntegrationConnectorAuditCode.DATABASE_LOG_STORE_UNUSABLE.getMessageDefinition(connectorName,
                                                                                                                getCatalogTargetDescription(),
                                                                                                                "its connector is " + ((super.getConnectorToTarget() == null) ? "missing" : super.getConnectorToTarget().getClass().getName()) +
                                                                                                                        " rather than " + JDBCResourceConnector.class.getName()));
            return;
        }

        String schemaName = getSchemaName(jdbcResourceConnector);

        if (! PostgresOpenLineageLogStoreSchema.isValidSchemaName(schemaName))
        {
            logRecord(methodName,
                      OpenLineageIntegrationConnectorAuditCode.DATABASE_LOG_STORE_UNUSABLE.getMessageDefinition(connectorName,
                                                                                                                getCatalogTargetDescription(),
                                                                                                                (schemaName == null) ? "no database schema name is configured" :
                                                                                                                        "the database schema name '" + schemaName + "' can not be used"));
            return;
        }

        PostgresOpenLineageLogStoreSchema newSchema = new PostgresOpenLineageLogStoreSchema(schemaName);

        PostgresOpenLineageLogStoreWriter newWriter = new PostgresOpenLineageLogStoreWriter(jdbcResourceConnector.getDataSource(), newSchema);

        try
        {
            newWriter.createSchema();
        }
        catch (Exception error)
        {
            logExceptionRecord(methodName,
                               OpenLineageIntegrationConnectorAuditCode.DATABASE_LOG_STORE_UNUSABLE.getMessageDefinition(connectorName,
                                                                                                                         getCatalogTargetDescription(),
                                                                                                                         "the schema " + schemaName + " could not be set up: " +
                                                                                                                                 error.getClass().getName() + ": " + error.getMessage()),
                               error);
            return;
        }

        this.daysToCleanUp = getDaysToCleanUp();
        this.schema        = newSchema;
        this.writer        = newWriter;

        logRecord(methodName,
                  OpenLineageIntegrationConnectorAuditCode.DATABASE_LOG_STORE_READY.getMessageDefinition(connectorName,
                                                                                                         schemaName,
                                                                                                         jdbcResourceConnector.getDatabaseName(),
                                                                                                         (daysToCleanUp == null) ? "<never>" : daysToCleanUp.toString()));
    }


    /**
     * Return the name of the database schema: from the catalog target relationship, or else from the connection
     * of the database schema asset.
     *
     * @param jdbcResourceConnector connector to the database
     * @return schema name or null
     */
    private String getSchemaName(JDBCResourceConnector jdbcResourceConnector)
    {
        String schemaName = super.getStringConfigurationProperty(JDBCConfigurationProperty.DATABASE_SCHEMA.getName(),
                                                                 this.getConfigurationProperties());

        if (schemaName == null)
        {
            Connection connection = jdbcResourceConnector.getConnection();

            if (connection != null)
            {
                schemaName = super.getStringConfigurationProperty(JDBCConfigurationProperty.DATABASE_SCHEMA.getName(),
                                                                  connection.getConfigurationProperties());
            }
        }

        return schemaName;
    }


    /**
     * Return the number of days to keep the records: from the catalog target relationship, or else from the
     * connector's connection.  An invalid value is reported and ignored.
     *
     * @return days or null to keep everything
     */
    private Long getDaysToCleanUp()
    {
        final String methodName = "getDaysToCleanUp";
        final String propertyName = PostgresOpenLineageLogStoreConfigurationProperty.DAYS_TO_CLEAN_UP.getName();

        Object value = null;

        if ((this.getConfigurationProperties() != null) && (this.getConfigurationProperties().get(propertyName) != null))
        {
            value = this.getConfigurationProperties().get(propertyName);
        }
        else if (connectorConfigurationProperties != null)
        {
            value = connectorConfigurationProperties.get(propertyName);
        }

        Long days = parseDaysToCleanUp(value);

        if ((value != null) && (days == null))
        {
            logRecord(methodName,
                      OpenLineageIntegrationConnectorAuditCode.INVALID_DAYS_TO_CLEAN_UP.getMessageDefinition(connectorName, value.toString()));
        }

        return days;
    }


    /**
     * Turn the value of the daysToCleanUp configuration property into a number of days.  Values arrive as numbers
     * (Integer or Long, depending on their size, when the connection is read from JSON) or as strings.
     *
     * @param value configured value - may be null
     * @return days, or null if the value is missing or is not a whole number that is zero or more
     */
    static Long parseDaysToCleanUp(Object value)
    {
        Long days = null;

        if ((value instanceof Integer) || (value instanceof Long) || (value instanceof Short))
        {
            days = ((Number) value).longValue();
        }
        else if (value instanceof String stringValue)
        {
            try
            {
                days = Long.parseLong(stringValue.trim());
            }
            catch (NumberFormatException notANumber)
            {
                days = null;
            }
        }

        if ((days != null) && (days < 0))
        {
            return null;
        }

        return days;
    }


    /**
     * Return a description of the catalog target for messages.
     *
     * @return text
     */
    private String getCatalogTargetDescription()
    {
        if (this.getCatalogTargetElementHeader() != null)
        {
            return this.getCatalogTargetName() + " (" + this.getCatalogTargetElementHeader().getGUID() + ")";
        }

        return this.getCatalogTargetName();
    }


    /**
     * Store an open lineage event and its contribution to the digest.  An event that is already stored (because
     * it has been delivered twice) is skipped, so the digest counts it once.
     *
     * @param event event formatted using the Egeria beans
     * @param jsonEvent event as JSON - the raw event if it was supplied
     */
    public void storeEvent(OpenLineageRunEvent event,
                           String              jsonEvent)
    {
        final String methodName = "storeEvent";

        if ((writer == null) || (jsonEvent == null))
        {
            return;
        }

        PostgresOpenLineageEventDigest digest = PostgresOpenLineageEventDigest.digest(event);

        if (digest == null)
        {
            return;
        }

        try
        {
            writer.store(digest, jsonEvent);
        }
        catch (Exception error)
        {
            logExceptionRecord(methodName,
                               OpenLineageIntegrationConnectorAuditCode.DATABASE_LOG_STORE_WRITE_FAILED.getMessageDefinition(connectorName,
                                                                                                                             digest.getEventType(),
                                                                                                                             digest.getRunId(),
                                                                                                                             digest.getJobNamespace() + "/" + digest.getJobName(),
                                                                                                                             schema.getSchemaName(),
                                                                                                                             error.getClass().getName(),
                                                                                                                             error.getMessage()),
                               error);
        }
    }


    /**
     * Delete the records that are older than daysToCleanUp.
     *
     * @throws ConnectorCheckedException a problem with the connector.
     * @throws UserNotAuthorizedException the connector was disconnected so stop refresh processing
     */
    @Override
    public void refresh() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        final String methodName = "refresh";

        super.refresh();

        if (writer == null)
        {
            setUpLogStore(methodName);
        }

        if ((writer == null) || (daysToCleanUp == null))
        {
            return;
        }

        Date cutOff = new Date(System.currentTimeMillis() - (daysToCleanUp * ONE_DAY));

        try
        {
            List<Integer> deleted = writer.purge(cutOff);

            if (deleted.stream().anyMatch(count -> count > 0))
            {
                logRecord(methodName,
                          OpenLineageIntegrationConnectorAuditCode.DATABASE_LOG_STORE_PURGED.getMessageDefinition(connectorName,
                                                                                                                  Integer.toString(deleted.get(0)),
                                                                                                                  Integer.toString(deleted.get(1)),
                                                                                                                  Integer.toString(deleted.get(2)),
                                                                                                                  Integer.toString(deleted.get(3)),
                                                                                                                  cutOff.toString(),
                                                                                                                  schema.getSchemaName()));
            }
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
}
