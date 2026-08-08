# DuckDB Database Connectors

Catalogs and surveys a [DuckDB](https://duckdb.org/) database.  DuckDB is an embedded/in-process OLAP database
engine: a "database" is either a single `.duckdb` file on disk, or an in-memory `:memory:` session, opened
directly through DuckDB's JDBC driver.  There is no separate database server process to connect to.

## Database-tier only

Every other database connector suite in this directory (`postgres-server-connectors`, `mssql-server-connectors`,
`oracle-server-connectors`, `db2luw-server-connectors`) provides two tiers of connector: a Server tier that
catalogs the databases hosted by a database server, and a Database tier that catalogs the schemas, tables and
columns within one of those databases. Because DuckDB is embedded and has no server process, this module is
**database-tier only** - there is a single integration connector
(`org.odpi.openmetadata.adapters.connectors.duckdb.catalog.DuckDBDatabaseIntegrationConnector`) and a single
survey action service (`org.odpi.openmetadata.adapters.connectors.duckdb.survey.DuckDBDatabaseSurveyActionService`),
both of which work directly against a `DuckDBTarget.DATABASE` target - the path to a `.duckdb` file, or `:memory:`.

Cataloguing of the database's own native schemas, tables and columns is handed off to the existing generic JDBC
integration connector (the "friendship" connector, configured via the `DuckDBFriendshipGUID` configuration
property), exactly the same hand-off mechanism used by the other four database connector suites.

## Federation discovery

DuckDB can transparently query other data sources as if they were local tables, through two distinct mechanisms:

* **Attached databases** - the `ATTACH` statement lets DuckDB open another database (another DuckDB file, a
  SQLite file, or a remote PostgreSQL/MySQL database) and query it directly.  Attached databases are discovered
  via DuckDB's `duckdb_databases()` catalog table function.
* **External file scans** - views defined using functions such as `read_parquet()`, `read_csv()`,
  `read_csv_auto()`, `read_json()`, `iceberg_scan()` or `delta_scan()` let DuckDB query files sitting in local
  storage or an object store (S3, Azure Blob, GCS, etc) without importing them.  These are discovered by scanning
  the view definitions returned by DuckDB's `duckdb_views()` catalog table function.

Neither of these federation capabilities is unique to DuckDB, but none of the other four connector suites in this
repository discover or catalog them, so this is new capability for Egeria's database connectors.  It is
implemented once, in `DuckDBFederationExtractor`, and used in two places:

* the survey action service unconditionally reports what it finds as two new annotation types
  (`SurveyDuckDBAnnotationType.ATTACHED_SOURCE` and `SurveyDuckDBAnnotationType.EXTERNAL_FILE_SOURCE`), populated
  as `ResourceMeasureAnnotationProperties` with a free-form `resourceProperties` map (no new open metadata type
  was needed for this);
* the integration connector unconditionally catalogs what it finds as `RESOURCE_LIST_RELATIONSHIP` links from the
  DuckDB database asset to the asset representing the attached database or external file, matching an existing
  catalogued asset where possible, or creating a new placeholder/asset where not.

Any credentials embedded in an `ATTACH` connection string (eg `password=...`) are redacted before being stored as
an annotation/asset property.

## Reuse of the files-connector's path-masking mechanism

Because a DuckDB database is a file (or, for federation findings, may reference other files), this module reuses
the `FileClassifier`/`FileClassification` mechanism from `files-integration-connectors` rather than reimplementing
path canonicalisation.  This is the same mechanism the basic files integration connector uses to convert a local
path into the masked, deployment-independent path that gets stored in open metadata (via the `fileSystemName`,
`localMountPoint` and `canonicalMountPoint` configuration properties, reused directly from
`FileSystemConfigurationProperty` rather than being redefined here).  It is used for:

* determining the qualified name and JDBC connection address of the DuckDB database file itself;
* matching/creating the assets for file-backed federation findings (an attached DuckDB or SQLite database, or an
  external file scan), reusing `FilesTemplateType`'s per-format templates in exactly the same way
  `DataFilesMonitorForTarget.catalogFile()` does.

Its Jar file includes the DuckDB JDBC (`duckdb_jdbc`) client driver.

See [](https://egeria-project.org/connectors/databases/duckdb-database-survey-action-service/) for documentation.

----
Return to [data-manager-connectors](..)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
