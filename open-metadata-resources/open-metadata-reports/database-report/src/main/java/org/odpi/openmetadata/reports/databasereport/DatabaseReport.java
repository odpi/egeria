/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.reports.databasereport;


import org.odpi.openmetadata.accessservices.datamanager.client.DatabaseManagerClient;
import org.odpi.openmetadata.accessservices.datamanager.client.MetadataSourceClient;

import org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities.DatabaseManagerProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.databases.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.databases.*;
import org.odpi.openmetadata.reports.EgeriaReport;
import org.odpi.openmetadata.http.HttpHelper;
import org.odpi.openmetadata.platformservices.client.PlatformServicesClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;


/**
 * DatabaseReport illustrates the use of the Data Manager OMAS API to search for and display the metadata describing a database.
 */
public class DatabaseReport
{
    private final String serverName;
    private final String platformURLRoot;
    private final String clientUserId;


    private MetadataSourceClient  metadataSourceClient  = null;
    private DatabaseManagerClient databaseManagerClient = null;

    /**
     * Set up the parameters for the sample.
     *
     * @param serverName server to call
     * @param platformURLRoot location of server's platform
     * @param clientUserId userId to access the server
     */
    private DatabaseReport(String serverName,
                           String platformURLRoot,
                           String clientUserId)
    {
        this.serverName = serverName;
        this.platformURLRoot = platformURLRoot;
        this.clientUserId = clientUserId;

        try
        {
            metadataSourceClient  = new MetadataSourceClient(serverName, platformURLRoot);
            databaseManagerClient = new DatabaseManagerClient(serverName, platformURLRoot);
        }
        catch (Exception error)
        {
            System.out.println("There was an exception when creating the Data Manager OMAS Client.  Error message is: " + error.getMessage());
        }

    }


    /**
     * Retrieve the version of the platform.  This fails if the platform is not running or the endpoint is populated by a service that is not an
     * OMAG Server Platform.
     *
     * @return platform version or null
     */
    private String getPlatformOrigin()
    {
        try
        {
            /*
             * This client is from the platform services module and queries the runtime state of the platform and the servers that are running on it.
             */
            PlatformServicesClient platformServicesClient = new PlatformServicesClient("MyPlatform", platformURLRoot);

            /*
             * This is the first call to the platform and determines the version of the software.
             * If the platform is not running, or the remote service is not an OMAG Server Platform,
             * the utility fails at this point.
             */
            return platformServicesClient.getPlatformOrigin(clientUserId);
        }
        catch (Exception error)
        {
            System.out.println("\n\nThere was an " + error.getClass().getName() + " exception when calling the platform.  Error message is: " + error.getMessage());
            System.out.println("Ensure the platform URl is correct and the platform is running");
        }

        return null;
    }


    /**
     * Request input form the user.
     *
     * @param requestText text that describes the options
     * @return text from the user broken down into an array of words
     */
    private String[] getUserInput(String requestText)
    {
        try
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println(requestText);

            String commandLine  = br.readLine();

            return commandLine.split(" ");
        }
        catch (Exception error)
        {
            System.out.println("There was a " + error.getClass().getName() + " exception when getting user input.  Error message is: " + error.getMessage());
        }

        return null;
    }


    /**
     * Locate a database to retrieve.  The user issues a number of search requests to get to a point where they are select a database by guid.
     */
    private void locateDatabase()
    {
        final String endInteractiveMode = "exit";
        final String allDatabases = "all";

        String command = null;
        int    pageSize = 10;
        int    startFrom = - pageSize;

        try
        {
            do
            {
                startFrom = startFrom + pageSize;

                List<DatabaseElement> databases = databaseManagerClient.findDatabases(clientUserId, ".*", startFrom, pageSize);

                if (databases != null)
                {
                    boolean firstDatabase = true;

                    for (DatabaseElement databaseElement : databases)
                    {
                        this.displayDatabaseSummary(databaseElement, firstDatabase);

                        firstDatabase = false;
                    }

                    String requestText = "Enter:\n - the guid of a database to generate a report for a single database\n - 'all' for a report of all listed databases\n - 'exit' to stop \nor just enter to retrieve more databases.\n";

                    String[] commandWords = getUserInput(requestText);

                    if (commandWords != null)
                    {
                        if (commandWords.length > 0)
                        {
                            command = commandWords[0];
                            command = command.strip();
                        }

                        if ((command != null) && (! "".equals(command)) && (! endInteractiveMode.equals(command)))
                        {
                            if (allDatabases.equals(command))
                            {
                                for (DatabaseElement databaseElement : databases)
                                {
                                    this.displayDatabase(databaseElement.getElementHeader().getGUID());
                                }
                            }
                            else
                            {
                                /*
                                 * The command entered is actually the GUID of a database.
                                 */
                                displayDatabase(command);
                            }
                        }
                    }
                }
                else
                {
                    if (startFrom == 0)
                    {
                        System.out.println("There are no databases in the catalog ... " );
                        break;
                    }
                    else
                    {
                        System.out.println("There are no more databases to retrieve from the catalog ... " );
                        break;
                    }
                }
            } while (! endInteractiveMode.equals(command));
        }
        catch (Exception error)
        {
            System.out.println("There was a " + error.getClass().getName() + " exception when locating an database.  Error message is: " + error.getMessage());
        }
    }


    /**
     * This method displays a summary of a retrieved database.
     *
     * @param databaseElement properties of a database
     * @param firstDatabase is the first database and so should the table heading be listed
     */
    private void displayDatabaseSummary(DatabaseElement databaseElement,
                                        boolean         firstDatabase)
    {
        if (firstDatabase)
        {
            System.out.println("| Unique identifier (GUID)         | Unique name (qualifiedName) | Display name       | Description                  |");
            System.out.println("|----------------------------------+-----------------------------+--------------------+------------------------------|");
        }

        if (databaseElement != null)
        {
            System.out.print("| " + databaseElement.getElementHeader().getGUID());
            System.out.print(" | " + databaseElement.getDatabaseProperties().getQualifiedName());
            System.out.print(" | " + databaseElement.getDatabaseProperties().getDisplayName());
            System.out.print(" | " + databaseElement.getDatabaseProperties().getDescription());
            System.out.println(" |");
        }
    }


    /**
     * If this is a database then display the contents.
     *
     * @param databaseGUID unique identifier of database
     */
    private void displayDatabase(String databaseGUID)
    {
        try
        {
            DatabaseElement databaseElement = databaseManagerClient.getDatabaseByGUID(clientUserId, databaseGUID);

            EgeriaReport report = new EgeriaReport("Database " + databaseElement.getDatabaseProperties().getDisplayName());

            final String reportTitle = "Database report for: ";
            report.printReportTitle(0, reportTitle + databaseElement.getDatabaseProperties().getDisplayName() + " on server: " + serverName);

            report.printElementInTable(0,
                                       true,
                                       databaseElement.getElementHeader().getGUID(),
                                       databaseElement.getDatabaseProperties().getQualifiedName(),
                                       databaseElement.getDatabaseProperties().getDisplayName(),
                                       databaseElement.getDatabaseProperties().getDescription());

            /*
             * The database may have its tables organized in schemas or directly listed under the database.
             */
            displayDatabaseSchemas(report, 1, databaseGUID);
            displayTables(report, 1, databaseGUID);
        }
        catch (Exception error)
        {
            System.out.println("There was a " + error.getClass().getName() + " exception when calling the Data Manager OMAS DatabaseManagerClient.  Error message is: " + error.getMessage());
        }

    }


    /**
     * Display the database schemas for a database.
     *
     * @param report report to accumulate content
     * @param indentLevel level of indent for the report
     * @param databaseGUID unique identifier of the database
     */
    private void displayDatabaseSchemas(EgeriaReport report,
                                        int          indentLevel,
                                        String       databaseGUID)
    {
        try
        {
            List<DatabaseSchemaElement> databaseSchemaElements = databaseManagerClient.getSchemasForDatabase(clientUserId, databaseGUID, 0, 0);

            if (databaseSchemaElements != null)
            {
                for (DatabaseSchemaElement databaseSchemaElement : databaseSchemaElements)
                {
                    report.printElementInTable(indentLevel,
                                               true,
                                               databaseSchemaElement.getElementHeader().getGUID(),
                                               databaseSchemaElement.getDatabaseSchemaProperties().getQualifiedName(),
                                               databaseSchemaElement.getDatabaseSchemaProperties().getDisplayName(),
                                               databaseSchemaElement.getDatabaseSchemaProperties().getDescription());

                    displayTables(report, indentLevel + 1, databaseSchemaElement.getElementHeader().getGUID());
                }
            }
            else
            {
                report.printReportLine(indentLevel, "No database schemas");
            }
        }
        catch (Exception error)
        {
            System.out.println("There was a " + error.getClass().getName() + " exception when calling the Data Manager OMAS DatabaseManagerClient for database schemas.  Error message is: " + error.getMessage());
        }
    }


    /**
     * Display the tables for either a database or a database schema.
     *
     * @param report report to accumulate content
     * @param indentLevel level of indent for the report
     * @param parentGUID unique id for database/database schema
     */
    private void displayTables(EgeriaReport report,
                               int          indentLevel,
                               String       parentGUID)
    {
        try
        {
            List<DatabaseTableElement> databaseTableElements = databaseManagerClient.getTablesForDatabaseAsset(clientUserId, parentGUID, 0, 0);

            if (databaseTableElements != null)
            {
                for (DatabaseTableElement databaseTableElement : databaseTableElements)
                {
                    report.printElementInTable(indentLevel,
                                               true,
                                               databaseTableElement.getElementHeader().getGUID(),
                                               databaseTableElement.getDatabaseTableProperties().getQualifiedName(),
                                               databaseTableElement.getDatabaseTableProperties().getDisplayName(),
                                               databaseTableElement.getDatabaseTableProperties().getDescription());

                    if (databaseTableElement.getDatabaseColumnCount() > 0)
                    {
                        report.printReportLine(indentLevel, databaseTableElement.getDatabaseColumnCount() + " database tables ...");

                        displayColumns(report, indentLevel + 1, databaseTableElement.getElementHeader().getGUID());
                    }
                }
            }
            else
            {
                report.printReportLine(indentLevel, "No database tables");
            }
        }
        catch (Exception error)
        {
            System.out.println("There was a " + error.getClass().getName() + " exception when calling the Data Manager OMAS DatabaseManagerClient for database tables.  Error message is: " + error.getMessage());
        }
    }


    /**
     * Display the columns for a database table.
     *
     * @param report report to accumulate content
     * @param indentLevel level of indent for the report
     * @param tableGUID unique id for database table
     */
    private void displayColumns(EgeriaReport report,
                                int          indentLevel,
                                String       tableGUID)
    {
        try
        {
            List<DatabaseColumnElement> databaseColumnElements = databaseManagerClient.getColumnsForDatabaseTable(clientUserId, tableGUID, 0, 0);

            if (databaseColumnElements != null)
            {
                for (DatabaseColumnElement databaseColumnElement : databaseColumnElements)
                {
                    report.printElementInTable(indentLevel,
                                               true,
                                               databaseColumnElement.getElementHeader().getGUID(),
                                               databaseColumnElement.getDatabaseColumnProperties().getQualifiedName(),
                                               databaseColumnElement.getDatabaseColumnProperties().getDisplayName(),
                                               databaseColumnElement.getDatabaseColumnProperties().getDescription());
                }
            }
            else
            {
                report.printReportLine(indentLevel, "No database columns");
            }
        }
        catch (Exception error)
        {
            System.out.println("There was a " + error.getClass().getName() + " exception when calling the Data Manager OMAS DatabaseManagerClient for database columns.  Error message is: " + error.getMessage());
        }
    }


    /**
     * Create a set of sample database to test this report
     */
    private void createSampleDatabases()
    {
        final String databaseManagerName = "SampleDatabases";
        final String databaseNamePrefix  = "SampleDatabase";
        final String databaseNamePrefix2 = "SampleDatabaseSeparateSchemaType";

        String databaseManagerGUID = createSampleDatabaseManager(databaseManagerName);

        createSampleDatabase(databaseManagerGUID, databaseManagerName, databaseNamePrefix + "A", 0, 0, 0, false);
        createSampleDatabase(databaseManagerGUID, databaseManagerName, databaseNamePrefix + "B", 1, 0, 0, false);
        createSampleDatabase(databaseManagerGUID, databaseManagerName, databaseNamePrefix + "C", 1, 1, 0, false);
        createSampleDatabase(databaseManagerGUID, databaseManagerName, databaseNamePrefix + "D", 1, 1, 1, false);
        createSampleDatabase(databaseManagerGUID, databaseManagerName, databaseNamePrefix + "E", 0, 1, 0, false);
        createSampleDatabase(databaseManagerGUID, databaseManagerName, databaseNamePrefix + "F", 0, 1, 1, false);
        createSampleDatabase(databaseManagerGUID, databaseManagerName, databaseNamePrefix + "G", 2, 25, 30, false);

        createSampleDatabase(databaseManagerGUID, databaseManagerName, databaseNamePrefix2 + "A", 0, 0, 0, false);
        createSampleDatabase(databaseManagerGUID, databaseManagerName, databaseNamePrefix2 + "B", 1, 0, 0, false);
        createSampleDatabase(databaseManagerGUID, databaseManagerName, databaseNamePrefix2 + "C", 1, 1, 0, false);
        createSampleDatabase(databaseManagerGUID, databaseManagerName, databaseNamePrefix2 + "D", 1, 1, 1, false);
        createSampleDatabase(databaseManagerGUID, databaseManagerName, databaseNamePrefix2 + "E", 0, 1, 0, false);
        createSampleDatabase(databaseManagerGUID, databaseManagerName, databaseNamePrefix2 + "F", 0, 1, 1, false);
        createSampleDatabase(databaseManagerGUID, databaseManagerName, databaseNamePrefix2 + "G", 2, 25, 30, false);
    }


    /**
     * Set up the database manager that hosts the databases.
     *
     * @param databaseManagerName name of the database manager
     * @return unique identifier of
     */
    private String createSampleDatabaseManager(String databaseManagerName)
    {
        try
        {
            DatabaseManagerProperties databaseManagerProperties = new DatabaseManagerProperties();

            databaseManagerProperties.setQualifiedName(databaseManagerName);

            return  metadataSourceClient.createDatabaseManager(clientUserId, null, null, databaseManagerProperties);
        }
        catch (Exception error)
        {
            System.out.println("There was a " + error.getClass().getName() + " exception when calling the Data Manager OMAS DatabaseManagerClient when creating sample databases.  Error message is: " + error.getMessage());
        }

        return null;
    }


    /**
     * Create a sample database using the following parameters.
     *
     * @param databaseManagerGUID unique identifier of owning data manager
     * @param databaseManagerName unique name of identifier of owning data manager
     * @param databaseName name of database
     * @param numberOfSchemas number of database schemas to create
     * @param numberOfTables number of tables to create in each database or schema
     * @param numberOfColumns number of columns for each table
     * @param useSchemaType should the tables be connected to schema type or directly to asset?
     */
    private void createSampleDatabase(String  databaseManagerGUID,
                                      String  databaseManagerName,
                                      String  databaseName,
                                      int     numberOfSchemas,
                                      int     numberOfTables,
                                      int     numberOfColumns,
                                      boolean useSchemaType)
    {
        Date startTime = new Date();

        try
        {
            DatabaseProperties databaseProperties = new DatabaseProperties();

            databaseProperties.setQualifiedName(databaseManagerName + ":Database:" + databaseName);

            String databaseGUID = databaseManagerClient.createDatabase(clientUserId, databaseManagerGUID, databaseManagerName, databaseProperties);

            databaseManagerClient.publishDatabase(clientUserId, databaseGUID);

            if (numberOfSchemas > 0)
            {
                for (int s=0; s < numberOfSchemas; s++)
                {
                    String schemaName = "Schema" + s;

                    DeployedDatabaseSchemaProperties deployedDatabaseSchemaProperties = new DeployedDatabaseSchemaProperties();

                    deployedDatabaseSchemaProperties.setQualifiedName(databaseName + "." + schemaName);
                    deployedDatabaseSchemaProperties.setDisplayName(schemaName);
                    deployedDatabaseSchemaProperties.setDescription("Database schema definition called " + schemaName + " with " + numberOfTables + " tables.");

                    String databaseSchemaGUID = databaseManagerClient.createDatabaseSchema(clientUserId,
                                                                                           databaseManagerGUID,
                                                                                           databaseManagerName,
                                                                                           databaseGUID,
                                                                                           deployedDatabaseSchemaProperties);

                    if (numberOfTables > 0)
                    {
                        createSampleDatabaseTables(databaseManagerGUID,
                                                   databaseManagerName,
                                                   databaseSchemaGUID,
                                                   schemaName,
                                                   numberOfTables,
                                                   numberOfColumns,
                                                   useSchemaType);
                    }

                    databaseManagerClient.publishDatabaseSchema(clientUserId, databaseSchemaGUID);
                }
            }
            else /* no schemas so tables are created directly under the database */
            {
                createSampleDatabaseTables(databaseManagerGUID,
                                           databaseManagerName,
                                           databaseGUID,
                                           databaseName,
                                           numberOfTables,
                                           numberOfColumns,
                                           false);
            }

            Date endTime = new Date();

            System.out.println("Created " + databaseName + " database in " + (endTime.getTime() - startTime.getTime()) + " milliseconds");
        }
        catch (Exception error)
        {
            System.out.println("There was a " + error.getClass().getName() + " exception when calling the Data Manager OMAS DatabaseManagerClient when creating sample databases.  Error message is: " + error.getMessage());
        }
    }


    /**
     * Create a sample database tables using the following parameters.
     *
     * @param databaseManagerGUID unique identifier of owning data manager
     * @param databaseManagerName unique name of identifier of owning data manager
     * @param parentGUID unique identifier of parent element
     * @param parentName unique name of parent element
     * @param numberOfTables number of tables to create
     * @param numberOfColumns number of columns for each table
     * @param useSchemaType should the tables be connected to schema type or directly to asset?
     */
    private void createSampleDatabaseTables(String  databaseManagerGUID,
                                            String  databaseManagerName,
                                            String  parentGUID,
                                            String  parentName,
                                            int     numberOfTables,
                                            int     numberOfColumns,
                                            boolean useSchemaType)
    {
        try
        {
            if (numberOfTables > 0)
            {
                String databaseSchemaTypeGUID = null;

                if (useSchemaType)
                {
                    databaseSchemaTypeGUID = databaseManagerClient.createDatabaseSchemaType(clientUserId,
                                                                                            databaseManagerGUID,
                                                                                            databaseManagerName,
                                                                                            "SchemaOf:" + parentName);
                }

                for (int t=0; t< numberOfTables; t++)
                {
                    String tableName = "Table" + t;

                    RelationalTableProperties relationalTableProperties = new RelationalTableProperties();

                    relationalTableProperties.setQualifiedName(parentName + "." + tableName);
                    relationalTableProperties.setDisplayName(tableName);
                    relationalTableProperties.setDescription("Table definition called " + tableName + " with " + numberOfColumns + " columns.");

                    String databaseTableGUID;

                    if (useSchemaType)
                    {
                        databaseTableGUID = databaseManagerClient.createDatabaseTableForSchemaType(clientUserId,
                                                                                                   databaseManagerGUID,
                                                                                                   databaseManagerName,
                                                                                                   databaseSchemaTypeGUID,
                                                                                                   relationalTableProperties);
                    }
                    else
                    {
                        databaseTableGUID = databaseManagerClient.createDatabaseTable(clientUserId,
                                                                                      databaseManagerGUID,
                                                                                      databaseManagerName,
                                                                                      parentGUID,
                                                                                      relationalTableProperties);
                    }

                    if (numberOfColumns > 0)
                    {
                        for (int c=0; c< numberOfColumns; c++)
                        {
                            String columnName = "Column" + t + "." + c;

                            RelationalColumnProperties relationalColumnProperties = new RelationalColumnProperties();

                            relationalColumnProperties.setQualifiedName(parentName + "." + tableName);
                            relationalColumnProperties.setDisplayName(tableName);
                            relationalColumnProperties.setDescription("Column definition called " + columnName + " inside " + tableName + " table.");
                            relationalColumnProperties.setDataType("string");

                            databaseManagerClient.createDatabaseColumn(clientUserId,
                                                                       databaseManagerGUID,
                                                                       databaseManagerName,
                                                                       databaseTableGUID,
                                                                       relationalColumnProperties);
                        }
                    }
                }

                if (useSchemaType)
                {
                    databaseManagerClient.attachSchemaTypeToDatabaseAsset(clientUserId,
                                                                          databaseManagerGUID,
                                                                          databaseManagerName,
                                                                          parentGUID,
                                                                          databaseSchemaTypeGUID);
                }
            }
        }
        catch (Exception error)
        {
            System.out.println("There was a " + error.getClass().getName() + " exception when calling the Data Manager OMAS DatabaseManagerClient when creating sample databases.  Error message is: " + error.getMessage());
        }
    }


    /**
     * Main program that controls the operation of the platform report.  The parameters are passed space separated.
     * The parameters are used to override the report's default values. If mode is set to "interactive"
     * the caller is prompted for a command.  Otherwise, it is assumed to be a guid.
     *
     * @param args 1. service platform URL root, 2. client userId, 3. mode/guid
     */
    public static void main(String[] args)
    {
        final String interactiveMode = "interactive";
        final String samplesMode     = "samples";

        String serverName = "cocoMDS1";
        String platformURLRoot = "https://localhost:9444";
        String clientUserId = "peterprofile";
        String  mode = samplesMode;

        if (args.length > 0)
        {
            serverName = args[0];
        }

        if (args.length > 1)
        {
            platformURLRoot = args[1];
        }

        if (args.length > 2)
        {
            clientUserId = args[2];
        }

        if (args.length > 3)
        {
            mode = args[3];
        }

        System.out.println("===============================");
        System.out.println("Database Report   " + new Date());
        System.out.println("===============================");
        System.out.print("Running against server: " + serverName + " at " + platformURLRoot);

        DatabaseReport utility = new DatabaseReport(serverName, platformURLRoot, clientUserId);

        HttpHelper.noStrictSSL();

        String platformOrigin = utility.getPlatformOrigin();

        if (platformOrigin != null)
        {
            System.out.print(" - " + platformOrigin);
        }
        else
        {
            System.out.println();
            System.exit(-1);
        }

        System.out.println("Using userId: " + clientUserId);
        System.out.println();

        try
        {
            if (interactiveMode.equals(mode))
            {
                utility.locateDatabase();
            }
            else if (samplesMode.equals(mode))
            {
                utility.createSampleDatabases();
                utility.locateDatabase();
            }
            else
            {
                utility.displayDatabase(mode);
            }
        }
        catch (Exception  error)
        {
            System.out.println("Exception: " + error.getClass().getName() + " with message " + error.getMessage());
            System.exit(-1);
        }

        System.exit(0);
    }
}
