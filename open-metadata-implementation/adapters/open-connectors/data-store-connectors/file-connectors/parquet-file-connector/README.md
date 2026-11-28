<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

![Stable](../../../../../../images/egeria-content-status-released.png#pagewidth)

# Parquet File Connector

The Parquet file connector describes the contents of a file that is formatted to the
[Apache Parquet](https://parquet.apache.org/) standard.  It returns:

* the schema of the file - its columns, their types and whether they are nullable.  Nested columns
  (lists, maps and structures) are supported;
* the number of rows, and how they are divided into row groups;
* how the values are stored - the compression algorithms, the encodings and the version of the data page layout;
* a profile of each column - the number of values, the number of nulls and the smallest and largest values;
* the additional properties that the writer added to the file - which typically include the schema used by the
  technology that wrote it.

It is also able to return the rows of data in the file, provided the [DuckDB](https://duckdb.org/) JDBC driver
is available at runtime.

It is used by the [Parquet File Survey Service](../../../file-survey-connectors) to
catalogue the columns in a Parquet file and to profile the data in them.

## How it works

A Parquet file ends with a footer where the writer records the schema of the data along with a description of each
*row group* (a horizontal slice of the data) and, within that, each *column chunk* (the values of one column
in that row group).  The description of a column chunk includes statistics about the values in it.

The description of the file comes from the footer and nothing else.  This has two consequences:

* **It is fast.**  The time taken does not depend on the size of the file since none of the data is read.  The footer
  of a file is typically a few kilobytes.
* **It has no dependencies.**  The footer is a structure that is serialized using the compact protocol
  from [Apache Thrift](https://thrift.apache.org/).  The connector includes a small decoder for this protocol, and so
  it does not need the Parquet, Thrift, Hadoop or compression libraries.

### Reading the data

Reading the rows of data is a different matter.  It needs an implementation of every version of the data page layout, every
encoding and every compression algorithm.  Rather than implement these, or add the Apache Parquet libraries
(and the Apache Hadoop libraries that they depend on), the connector passes this work to DuckDB, which has a complete
implementation of the Parquet format built into its JDBC driver.

The connector only calls DuckDB through the standard `java.sql` interfaces, using a private in-memory database
that holds no data of its own.  So DuckDB is not a dependency of the connector:

* If the DuckDB JDBC driver (`org.duckdb:duckdb_jdbc`) is on the class path, the rows of data can be read.
  This is the case in the OMAG Server Platform, which already includes the driver for the DuckDB connectors.
* If it is not, the methods that describe the file from its footer all work.  The methods that read data
  fail with the `PARQUET-FILE-CONNECTOR-400-005` exception.  The `isDataAccessAvailable()` method says which situation applies.

## Support for the different versions of Parquet

The Parquet format has evolved since it was first released in 2013, and there are many technologies that write
Parquet files, each choosing which of the newer features to use.  The layout of the file - a footer that is located
using the last eight bytes of the file - has not changed.  All the changes have been made either by adding optional fields
to the footer, or by adding new ways to lay out, encode and compress the pages of values that the footer points to.

Since the connector only decodes the footer to describe a file, the description is available for files written to any version of the format, using any
encoding and any compression algorithm.  (Reading the data relies on DuckDB's support for the format, which covers
both versions of the data page, all the encodings and the Snappy, GZIP, ZStandard, LZ4 and Brotli compression algorithms.)  The differences between the versions that do show up in the footer are
handled as follows:

| Feature | Original format (1.x) | Later versions (2.x) | What the connector does |
|---------|-----------------------|----------------------|-------------------------|
| Type annotations | *Converted types* such as `UTF8` and `TIMESTAMP_MILLIS` | *Logical types* such as `STRING` and `TIMESTAMP(MICROS, UTC)`, which have parameters and cover more types (UUID, nanosecond timestamps, local timestamps, float16, variant, geospatial) | Understands both.  The logical type is used where both are present. |
| Timestamps | Often stored as `INT96` by Apache Hive, Impala and early versions of Spark | `INT64` with a logical type | Both are reported as `timestamp`.  (There are no usable statistics for `INT96` columns.) |
| Lists and maps | Several different two-level and three-level layouts | A standard three-level layout | Recognises the standard layouts and the legacy layouts using the rules from the Parquet specification. |
| Statistics | `min` and `max` fields that were calculated using a signed comparison whatever the type of the column - which is incorrect for unsigned numbers and for strings | `min_value` and `max_value` fields that use the correct ordering for the column's type | Uses the newer fields where they are present.  The original fields are used only for the types of column where they are known to be correct. |
| Data pages | `DATA_PAGE` | `DATA_PAGE_V2` | Reports which are in use.  This is taken from the footer where the writer recorded it.  Otherwise, the header of the first data page of each column is read to discover it. |
| Encodings | `PLAIN`, `PLAIN_DICTIONARY`, `RLE`, `BIT_PACKED` | `RLE_DICTIONARY`, `DELTA_BINARY_PACKED`, `DELTA_LENGTH_BYTE_ARRAY`, `DELTA_BYTE_ARRAY`, `BYTE_STREAM_SPLIT` | Reports which are in use.  The values are not decoded. |
| Compression | `UNCOMPRESSED`, `SNAPPY`, `GZIP`, `LZO` | `BROTLI`, `LZ4`, `ZSTD`, `LZ4_RAW` | Reports which are in use.  The values are not decompressed. |
| Format version | 1 | 2 | Reports the version declared in the footer.  Note that this is not a reliable guide to the features used in a file since many writers always set it to 1. |

Fields in the footer, and values of the enumerations, that the connector does not recognize are ignored.  This means
that it continues to work as new features are added to Parquet.

The connector also supports files that use [Parquet modular encryption](https://parquet.apache.org/docs/file-format/data-pages/encryption/)
with a plaintext footer.  The columns that are encrypted are identified, and no statistics are returned for them.
A file with an encrypted footer can not be described and results in the `PARQUET-FILE-CONNECTOR-400-002` exception.

## Using the connector

The location of the file is supplied in the endpoint of the connection, in the same way as the
[Basic File Connector](../basic-file-connector).  There are no configuration properties.

The connector implements the `ParquetFileStore` interface.

* `getFileMetadata()` returns a `ParquetFileMetadata` object that holds everything from the footer.
  It includes the schema (as a tree of `ParquetSchemaElement` objects), the row groups, and a `ParquetColumnProfile`
  for each column that holds values.
* `getRecordCount()` returns the number of rows in the file.
* `getColumnNames()` returns the names of the top-level columns.
* `isDataAccessAvailable()` returns whether the rows of data can be read.
* `readRecord()` returns a row of data as a list of strings - one for each top-level column.  The first row is row 0.
  A null value is returned as null, and the value of a nested column is formatted as a string.
  Rows are retrieved from the file a thousand at a time, and only the row groups that contain them are read, so
  it is efficient both to step through a file and to pick out individual rows.
* `getValueCounts()` returns how many times each value occurs in a column, for columns with a limited number
  of different values.

The connector also implements the `ReadableTabularDataSource` interface that is used for
[tabular data sets](https://egeria-project.org/concepts/tabular-data-set/).  This means that a catalogued Parquet file can be used wherever
a tabular data source is read, for example as the source when provisioning a tabular data set.
The table name is the name of the file without its file extension.

The type of each column is described using names that are familiar from programming languages and databases
(`int`, `long`, `string`, `date`, `timestamp`, `decimal(10,2)` ...) rather than the names of the Parquet types.
A nested column is described using `array<...>`, `map<...,...>` and `struct<name:type,...>`.
The Parquet physical type and annotations are also available from `ParquetSchemaElement`.

The connector reports the following problems with a file using the `FileReadException`:

* `PARQUET-FILE-CONNECTOR-400-001` - the file is not a Parquet file.  This includes a file that is truncated, which is how a
  file appears while it is being written or copied.
* `PARQUET-FILE-CONNECTOR-400-002` - the footer of the file is encrypted.
* `PARQUET-FILE-CONNECTOR-400-003` - the footer of the file can not be decoded.
* `PARQUET-FILE-CONNECTOR-400-004` - the requested row is beyond the end of the file.
* `PARQUET-FILE-CONNECTOR-400-005` - the data can not be read because the DuckDB JDBC driver is not available.
* `PARQUET-FILE-CONNECTOR-500-002` - DuckDB reported an error reading the data.

## Parquet Tabular Data Set Connector

The `ParquetTabularDataSetConnector` provides read access to a Parquet file that is catalogued as a
[tabular data set](https://egeria-project.org/concepts/tabular-data-set/) rather than as a file.
It is the equivalent of the CSV Tabular Data Set connector, and is used with assets that have a
deployed implementation type of `Parquet Tabular Data Set`.  The core content pack includes a template for
this type of asset.

The connector is configured using the same configuration properties as the CSV Tabular Data Set connector
(they are defined in `TabularFileConfigurationProperty`):

* `directoryPathName` - the directory where the file is located.
* `tableName` - the name of the table.  The connector reads the file called `<tableName>.parquet` in the directory.
* `tableDescription` - an optional description of the table.  If it is not supplied, a description is derived from the file.

It implements the `ReadableTabularDataSource` interface and needs an embedded Parquet File Store connector,
which does the work of reading the file.  It fails to start with `PARQUET-FILE-CONNECTOR-400-006` if this embedded
connector is missing.  Since Parquet files are not updated once they are written, the connector does not implement
`WritableTabularDataSource`.

## Parquet Tabular Data Set Collection Connector

The `ParquetTabularDataSetCollectionConnector` reads a directory of Parquet files as a collection of tabular data sets.
It is used with assets that have a deployed implementation type of `Parquet Tabular Data Set Collection`, and the
core content pack includes a template for this type of asset.

Only the `directoryPathName` configuration property is needed.  The connector implements `ReadableTabularDataCollection`:

* `getTableNames()` returns the name of each Parquet file in the directory, without its file extension, in alphabetical order.
* `setTableName()` brings one of these tables into focus.  The `ReadableTabularDataSource` methods then describe and
  read that table.

Like the Parquet Tabular Data Set connector, it is read-only and needs an embedded Parquet File Store connector.

## Testing

The unit tests use Parquet files written by [DuckDB](https://duckdb.org/) in both of the format versions it supports,
with different compression algorithms and with nested data.  The expected values are the values that DuckDB reports
for the same files.  These files are located in the test resources folder.  The DuckDB JDBC driver is a test dependency
so that the tests can read the rows of data from each of them.

The characteristics of files from the first generation of writers (converted types only, `INT96` timestamps, two-level lists,
the original statistics fields and no record of the data page version) can not be reproduced with a current writer.
The tests build files with these characteristics themselves.

----
Return to the [file-connectors](..) module.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
