/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer;

import org.apache.commons.lang3.StringUtils;
import org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.requests.Jdbc;
import org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.requests.Omas;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.databases.DatabaseProperties;

import java.util.List;

import static org.odpi.openmetadata.adapters.connectors.integration.jdbc.ffdc.JDBCIntegrationConnectorAuditCode.TRANSFER_COMPLETE_FOR_DB_OBJECT;


/**
 * Creates the database root of the metadata structure the follows
 */
public class DatabaseTransfer {

    private final Jdbc jdbc;
    private final String databaseManagerName;
    private final String address;
    private final String          catalog;
    private final OpenMetadataRootElement databaseElement;
    private final Omas            omas;
    private final AuditLog auditLog;

    public DatabaseTransfer(Jdbc jdbc,
                            String databaseManagerName,
                            OpenMetadataRootElement databaseElement,
                            String address,
                            String catalog,
                            Omas omas,
                            AuditLog auditLog)
    {
        this.jdbc = jdbc;
        this.databaseManagerName = databaseManagerName;
        this.databaseElement = databaseElement;
        this.address = address;
        this.catalog = catalog;
        this.omas = omas;
        this.auditLog = auditLog;
    }

    /**
     * Triggers database metadata transfer
     *
     * @return database element
     */
    public OpenMetadataRootElement execute()
    {
        /*
         * If the database element is already known, just return it.
         */
        if (databaseElement != null)
        {
            return databaseElement;
        }
        else
        {
            /*
             * Locate the database asset
             */
            DatabaseProperties databaseProperties = buildDatabaseProperties();
            String multipleDatabasesFoundMessage = "Querying for a database with qualified name "
                    + databaseProperties.getQualifiedName() + " and found multiple. Expecting only one";

            List<OpenMetadataRootElement> databasesInOmas = omas.getDatabasesByName(databaseProperties.getQualifiedName());
            if (databasesInOmas.isEmpty())
            {
                omas.createDatabase(databaseProperties);
            }
            else
            {
                if (databasesInOmas.size() > 1)
                {
                    auditLog.logMessage(multipleDatabasesFoundMessage, null);
                    return null;
                }
                omas.updateDatabase(databasesInOmas.get(0).getElementHeader().getGUID(), databaseProperties);
            }

            databasesInOmas = omas.getDatabasesByName(databaseProperties.getQualifiedName());
            if (databasesInOmas.size() == 1)
            {
                auditLog.logMessage("Transferred database with qualified name " + databaseProperties.getQualifiedName(),
                                    TRANSFER_COMPLETE_FOR_DB_OBJECT.getMessageDefinition("database " + databaseProperties.getQualifiedName()));
                return databasesInOmas.get(0);
            }

            auditLog.logMessage(multipleDatabasesFoundMessage, null);
        }
        return null;
    }

    /**
     * Builds database properties
     *
     * @return properties
     */
    private DatabaseProperties buildDatabaseProperties()
    {
        String driverName = jdbc.getDriverName();
        String databaseProductVersion = jdbc.getDatabaseProductVersion();
        String databaseProductName = jdbc.getDatabaseProductName();
        String url = jdbc.getUrl();

        DatabaseProperties databaseProperties = new DatabaseProperties();
        databaseProperties.setQualifiedName(databaseManagerName + "::" + address);
        databaseProperties.setDisplayName(StringUtils.isBlank(catalog) ? address : catalog);
        databaseProperties.setDatabaseInstance(driverName);
        databaseProperties.setVersionIdentifier(databaseProductVersion);
        databaseProperties.setDeployedImplementationType(databaseProductName);
        databaseProperties.setImportedFrom(url);

        return databaseProperties;
    }

}
