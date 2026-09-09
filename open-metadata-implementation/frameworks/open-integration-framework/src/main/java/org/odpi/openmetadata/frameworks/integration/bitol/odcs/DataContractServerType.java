/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.bitol.odcs;

/**
 * DataContractServerType lists the standard types of server (infrastructure) where the data described by an ODCS data contract may reside.
 * The Bitol standards carry these values as strings, and permit additional values, so the beans hold the
 * string and this enumeration documents the standard values and provides a lookup.
 */
public enum DataContractServerType
{
    /**
     * A web API.
     */
    API       ("api", "A web API."),

    /**
     * Amazon Athena.
     */
    ATHENA    ("athena", "Amazon Athena."),

    /**
     * Microsoft Azure storage.
     */
    AZURE     ("azure", "Microsoft Azure storage."),

    /**
     * Google BigQuery.
     */
    BIGQUERY  ("bigquery", "Google BigQuery."),

    /**
     * ClickHouse.
     */
    CLICKHOUSE("clickhouse", "ClickHouse."),

    /**
     * Databricks.
     */
    DATABRICKS("databricks", "Databricks."),

    /**
     * Denodo.
     */
    DENODO    ("denodo", "Denodo."),

    /**
     * Dremio.
     */
    DREMIO    ("dremio", "Dremio."),

    /**
     * DuckDB.
     */
    DUCKDB    ("duckdb", "DuckDB."),

    /**
     * AWS Glue.
     */
    GLUE      ("glue", "AWS Glue."),

    /**
     * Google Cloud SQL.
     */
    CLOUDSQL  ("cloudsql", "Google Cloud SQL."),

    /**
     * IBM Db2.
     */
    DB2       ("db2", "IBM Db2."),

    /**
     * Apache Hive.
     */
    HIVE      ("hive", "Apache Hive."),

    /**
     * Apache Impala.
     */
    IMPALA    ("impala", "Apache Impala."),

    /**
     * IBM Informix.
     */
    INFORMIX  ("informix", "IBM Informix."),

    /**
     * Apache Kafka.
     */
    KAFKA     ("kafka", "Apache Kafka."),

    /**
     * Amazon Kinesis.
     */
    KINESIS   ("kinesis", "Amazon Kinesis."),

    /**
     * Files on a local file system.
     */
    LOCAL     ("local", "Files on a local file system."),

    /**
     * MySQL.
     */
    MYSQL     ("mysql", "MySQL."),

    /**
     * Oracle Database.
     */
    ORACLE    ("oracle", "Oracle Database."),

    /**
     * PostgreSQL.
     */
    POSTGRESQL("postgresql", "PostgreSQL."),

    /**
     * PostgreSQL (alternative spelling).
     */
    POSTGRES  ("postgres", "PostgreSQL (alternative spelling)."),

    /**
     * Presto.
     */
    PRESTO    ("presto", "Presto."),

    /**
     * Google Pub/Sub.
     */
    PUBSUB    ("pubsub", "Google Pub/Sub."),

    /**
     * Amazon Redshift.
     */
    REDSHIFT  ("redshift", "Amazon Redshift."),

    /**
     * Amazon S3 (or S3 compatible) object storage.
     */
    S3        ("s3", "Amazon S3 (or S3 compatible) object storage."),

    /**
     * An SFTP server.
     */
    SFTP      ("sftp", "An SFTP server."),

    /**
     * Snowflake.
     */
    SNOWFLAKE ("snowflake", "Snowflake."),

    /**
     * Microsoft SQL Server.
     */
    SQLSERVER ("sqlserver", "Microsoft SQL Server."),

    /**
     * Azure Synapse.
     */
    SYNAPSE   ("synapse", "Azure Synapse."),

    /**
     * Trino.
     */
    TRINO     ("trino", "Trino."),

    /**
     * Vertica.
     */
    VERTICA   ("vertica", "Vertica."),

    /**
     * Actian Zen.
     */
    ZEN       ("zen", "Actian Zen."),

    /**
     * A server type not covered by the other values.
     */
    CUSTOM    ("custom", "A server type not covered by the other values."),

    /**
     * Actian Zen (synonym of zen).
     */
    BTRIEVE   ("btrieve", "Actian Zen (synonym of zen)."),


    /**
     * Exasol in-memory analytics database.
     */
    EXASOL    ("exasol", "Exasol in-memory analytics database."),

    /**
     * Actian NoSQL FastObjects (synonym of poet).
     */
    FASTOBJECTS("fastobjects", "Actian NoSQL FastObjects (synonym of poet)."),

    /**
     * SAP HANA database.
     */
    HANA      ("hana", "SAP HANA database."),

    /**
     * Apache Iceberg catalog accessed through the Iceberg REST API.
     */
    ICEBERG   ("iceberg", "Apache Iceberg catalog accessed through the Iceberg REST API."),

    /**
     * Actian Ingres OLTP database.
     */
    INGRES    ("ingres", "Actian Ingres OLTP database."),

    /**
     * Actian NoSQL FastObjects object database.
     */
    POET      ("poet", "Actian NoSQL FastObjects object database."),

    /**
     * Teradata Vantage.
     */
    TERADATA  ("teradata", "Teradata Vantage."),

    /**
     * Actian analytics engine (Vector).
     */
    VECTORWISE("vectorwise", "Actian analytics engine (Vector)."),

    /**
     * Actian NoSQL object database.
     */
    VERSANT   ("versant", "Actian NoSQL object database.");

    private final String value;
    private final String description;


    /**
     * Constructor for the enumeration.
     *
     * @param value the string used in the document
     * @param description description of the meaning of the value
     */
    DataContractServerType(String value,
                           String description)
    {
        this.value       = value;
        this.description = description;
    }


    /**
     * Return the string used in the document.
     *
     * @return string value
     */
    public String getValue()
    {
        return value;
    }


    /**
     * Return the description of the meaning of the value.
     *
     * @return string description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Return the enumeration that matches the supplied document value (case-insensitive).
     *
     * @param value string from the document
     * @return matching enumeration or null if the value is not one of the standard values
     */
    public static DataContractServerType fromValue(String value)
    {
        if (value != null)
        {
            for (DataContractServerType candidate : DataContractServerType.values())
            {
                if (candidate.value.equalsIgnoreCase(value))
                {
                    return candidate;
                }
            }
        }

        return null;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DataContractServerType{value='" + value + "'}";
    }
}
