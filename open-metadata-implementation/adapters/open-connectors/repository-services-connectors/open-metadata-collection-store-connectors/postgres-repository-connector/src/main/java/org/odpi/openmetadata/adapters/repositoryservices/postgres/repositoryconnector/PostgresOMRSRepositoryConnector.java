/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.postgres.repositoryconnector;


import org.odpi.openmetadata.adapters.connectors.resource.jdbc.JDBCResourceConnector;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.ddl.postgres.PostgreSQLSchemaDDL;
import org.odpi.openmetadata.adapters.repositoryservices.postgres.repositoryconnector.controls.PostgresConfigurationProperty;
import org.odpi.openmetadata.adapters.repositoryservices.postgres.repositoryconnector.database.DatabaseStore;
import org.odpi.openmetadata.adapters.repositoryservices.postgres.repositoryconnector.ffdc.PostgresAuditCode;
import org.odpi.openmetadata.adapters.repositoryservices.postgres.repositoryconnector.ffdc.PostgresErrorCode;
import org.odpi.openmetadata.adapters.repositoryservices.postgres.repositoryconnector.mappers.ControlMapper;
import org.odpi.openmetadata.adapters.repositoryservices.postgres.repositoryconnector.schema.RepositoryTable;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.OMRSRuntimeException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;

import java.util.Date;
import java.util.UUID;

/**
 * The PostgresOMRSRepositoryConnector is a connector to a local PostgreSQL repository.  Each repository has the same
 * table structure and is stored in its own database schema.
 */
public class PostgresOMRSRepositoryConnector extends OMRSRepositoryConnector
{
    private final static String supportedSchemaVersion = "V1.0";
    private JDBCResourceConnector jdbcResourceConnector = null;

    /**
     * Default constructor used by the OCF Connector Provider.
     */
    public PostgresOMRSRepositoryConnector()
    {
        /*
         * Nothing to do.
         */
    }


    /**
     * Set up the unique id for this metadata collection.
     *
     * @param metadataCollectionId - String unique Id
     */
    @Override
    public void setMetadataCollectionId(String     metadataCollectionId)
    {
        final String methodName = "setMetadataCollectionId";

        super.metadataCollectionId = metadataCollectionId;
        super.repositoryName = super.getStringConfigurationProperty(PostgresConfigurationProperty.DATABASE_SCHEMA.getName(), connectionBean.getConfigurationProperties());

        if (metadataCollectionId != null)
        {
            if ((embeddedConnectors != null) && (!embeddedConnectors.isEmpty()))
            {
                for (Connector embeddedConnector : embeddedConnectors)
                {
                    if (embeddedConnector instanceof JDBCResourceConnector embeddedJDBCResourceConnector)
                    {
                        this.jdbcResourceConnector = embeddedJDBCResourceConnector;

                        try
                        {
                            if (! jdbcResourceConnector.isActive())
                            {
                                jdbcResourceConnector.start();
                            }

                            validateRepositoryControlTable(jdbcResourceConnector);

                            boolean isReadOnly = this.getRepositoryMode();
                            Date    defaultAsOfTime = this.getDefaultAsOfTime();

                            /*
                             * Initialize the metadata collection only once the connector is properly set up.
                             */
                            super.metadataCollection = new PostgresOMRSMetadataCollection(this,
                                                                                          super.serverName,
                                                                                          repositoryHelper,
                                                                                          repositoryValidator,
                                                                                          isReadOnly,
                                                                                          defaultAsOfTime,
                                                                                          jdbcResourceConnector,
                                                                                          metadataCollectionId);
                            break;
                        }
                        catch (Exception exception)
                        {
                            auditLog.logException(methodName,
                                                  PostgresAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(repositoryName,
                                                                                                              exception.getClass().getName(),
                                                                                                              methodName,
                                                                                                              exception.getMessage()),
                                                  exception);

                            throw new OMRSRuntimeException(PostgresErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(repositoryName,
                                                                                                                       exception.getClass().getName(),
                                                                                                                       methodName,
                                                                                                                       exception.getMessage()),
                                                           this.getClass().getName(),
                                                           methodName,
                                                           exception);
                        }
                    }
                }
            }
        }
    }


    /**
     * Check that the schema is in place and the repository control table is correct.
     *
     * @param jdbcResourceConnector resource connector for JDBC (started)
     * @throws RepositoryErrorException problem connecting to the database
     */
    private void validateRepositoryControlTable(JDBCResourceConnector jdbcResourceConnector) throws RepositoryErrorException
    {
        final String methodName = "validateRepositoryControlTable";

        auditLog.logMessage(methodName, PostgresAuditCode.STARTING_REPOSITORY.getMessageDefinition(repositoryName, jdbcResourceConnector.getDatabaseName()));

        String schemaName = super.getStringConfigurationProperty(PostgresConfigurationProperty.DATABASE_SCHEMA.getName(),
                                                                 connectionBean.getConfigurationProperties());

        if (schemaName == null)
        {
            schemaName = "repository_" + serverName;
        }

        try
        {
            loadDDL(jdbcResourceConnector, schemaName);

            DatabaseStore databaseStore = new DatabaseStore(jdbcResourceConnector,
                                                            repositoryName,
                                                            repositoryHelper);

            ControlMapper controlMapper = databaseStore.getControlTable();

            if (controlMapper == null)
            {
                if (metadataCollectionId == null)
                {
                    metadataCollectionId = UUID.randomUUID().toString();
                }

                controlMapper = new ControlMapper(repositoryName, serverName, metadataCollectionId, supportedSchemaVersion);

                databaseStore.saveControlTable(controlMapper);
            }
            else if (! serverName.equals(controlMapper.getServerName()))
            {
                throw new RepositoryErrorException(PostgresErrorCode.CONTROL_SERVER_MISMATCH.getMessageDefinition(repositoryName,
                                                                                                                  schemaName,
                                                                                                                  controlMapper.getServerName()),
                                                   this.getClass().getName(),
                                                   methodName);
            }
            else if (! supportedSchemaVersion.equals(controlMapper.getSchemaVersion()))
            {
                throw new RepositoryErrorException(PostgresErrorCode.CONTROL_SCHEMA_VERSION_MISMATCH.getMessageDefinition(repositoryName,
                                                                                                                          schemaName,
                                                                                                                          controlMapper.getSchemaVersion()),
                                                   this.getClass().getName(),
                                                   methodName);
            }
            else if (metadataCollectionId == null)
            {
                metadataCollectionId = controlMapper.getLocalMetadataCollectionGUID();
            }
            else if (! metadataCollectionId.equals(controlMapper.getLocalMetadataCollectionGUID()))
            {
                throw new RepositoryErrorException(PostgresErrorCode.CONTROL_MC_ID_MISMATCH.getMessageDefinition(repositoryName,
                                                                                                                 schemaName,
                                                                                                                 controlMapper.getLocalMetadataCollectionGUID(),
                                                                                                                 metadataCollectionId),
                                                   this.getClass().getName(),
                                                   methodName);
            }
        }
        catch (RepositoryErrorException error)
        {
            throw error;
        }
        catch (Exception error)
        {
            auditLog.logException(methodName, PostgresAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(repositoryName,
                                                                                                          error.getClass().getName(),
                                                                                                          methodName,
                                                                                                          error.getMessage()),
                                  error);

            throw new RepositoryErrorException(PostgresErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(repositoryName,
                                                                                                           error.getClass().getName(),
                                                                                                           methodName,
                                                                                                           error.getMessage()),
                                               this.getClass().getName(),
                                               methodName,
                                               error);
        }
    }


    /**
     * Check that the tables for the repository are defined.
     *
     * @param schemaName name of the schema
     * @throws RepositoryErrorException problem with the DDL
     */
    private void loadDDL(JDBCResourceConnector jdbcResourceConnector,
                         String                schemaName) throws RepositoryErrorException
    {
        final String methodName = "loadDDL";

        auditLog.logMessage(methodName, PostgresAuditCode.CONFIRMING_REPOSITORY_SCHEMA.getMessageDefinition(repositoryName, schemaName));

        try
        {
            java.sql.Connection jdbcConnection = jdbcResourceConnector.getDataSource().getConnection();

            PostgreSQLSchemaDDL postgreSQLSchemaDDL = new PostgreSQLSchemaDDL(schemaName,
                                                                              repositoryName,
                                                                              RepositoryTable.getTables());
            jdbcResourceConnector.addDatabaseDefinitions(jdbcConnection, postgreSQLSchemaDDL.getDDLStatements());
            jdbcConnection.commit();
        }
        catch (Exception error)
        {
            auditLog.logException(methodName, PostgresAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(repositoryName,
                                                                                                          error.getClass().getName(),
                                                                                                          methodName,
                                                                                                          error.getMessage()),
                                  error);

            throw new RepositoryErrorException(PostgresErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(repositoryName,
                                                                                                           error.getClass().getName(),
                                                                                                           methodName,
                                                                                                           error.getMessage()),
                                               this.getClass().getName(),
                                               methodName,
                                               error);
        }
    }


    /**
     * Extract the value for repository mode from the configuration properties and return a boolean to indicate
     * id the repository is in read-only mode.
     *
     * @return boolean (true=read-only)
     */
    private boolean getRepositoryMode()
    {
        final String methodName = "getRepositoryMode";

        String repositoryMode = super.getStringConfigurationProperty(PostgresConfigurationProperty.REPOSITORY_MODE.getName(),
                                                                     connectionBean.getConfigurationProperties());

        if ("readOnly".equals(repositoryMode))
        {
            repositoryMode = "read-only";
        }
        else
        {
            repositoryMode = "read-write";
        }

        auditLog.logMessage(methodName, PostgresAuditCode.REPOSITORY_MODE.getMessageDefinition(repositoryName, repositoryMode));

        return "read-only".equals(repositoryMode);
    }


    /**
     * Return the defaultAsOfTime setting from the configuration property (default is null).
     *
     * @return date or null
     * @throws InvalidParameterException badly formatted date
     */
    private Date getDefaultAsOfTime() throws InvalidParameterException
    {
        final String methodName = "getDefaultAsOfTime";

        Date defaultAsOfTime = super.getDateConfigurationProperty(PostgresConfigurationProperty.REPOSITORY_MODE.getName(),
                                                                  connectionBean.getConfigurationProperties());

        if (defaultAsOfTime == null)
        {
            auditLog.logMessage(methodName, PostgresAuditCode.DEFAULT_AS_OF_TIME.getMessageDefinition(repositoryName, "'null'"));
        }
        else
        {
            auditLog.logMessage(methodName, PostgresAuditCode.DEFAULT_AS_OF_TIME.getMessageDefinition(repositoryName, defaultAsOfTime.toString()));
        }

        return defaultAsOfTime;
    }


    /**
     * Free up any resources held since the connector is no longer needed.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    @Override
    public void disconnect() throws ConnectorCheckedException
    {
        if (jdbcResourceConnector != null)
        {
            jdbcResourceConnector.disconnect();
        }
    }
}