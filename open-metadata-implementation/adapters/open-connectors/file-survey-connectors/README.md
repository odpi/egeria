<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2019. -->

![Stable](../../../../images/egeria-content-status-released.png#pagewidth)

# File Survey Connectors

The file survey connectors contain the implementation of four
[survey action services](https://egeria-project.org/concepts/survey-action-service) that extract and report on files and folders.
The connectors are provided by the Egeria community.

----

Return to [open-connectors](..) module.

----

## Folder Survey Service

*FolderSurveyService* is a survey action service implementation for analysing the files nested in a folder. The depth of the survey is controlled by the analysisLevel request parameter.

    * The default value is 'TOP_LEVEL_ONLY' which produces summary statistics for the top-level directory only.
    * If it is set to 'ALL_FOLDERS' then there are statistics created for the top level directory, and all subdirectories.
    * If it set to 'TOP_LEVEL_AND_FILES' then statistics are created for the top-level directory and all files that are encountered.
    * Finally, if it is set to 'ALL_FOLDERS_AND_FILES' then statistics are created for all directories and files encountered.", "string", "myFile.csv".

![Architecture](docs/folder-survey-action-service.png)
> Figure 1: Folder Survey Service

![Report Structure](docs/folder-survey-action-survey-report.png)
> Figure 2: report structure for Folder Survey Service

## File Survey Service

*File Survey Service* extracts properties about a file.  It then maps these properties to Egeria's file reference data to enable summary statistics to be calculated.

![Architecture](docs/any-file-survey-action-service.png)
> Figure 3: File Survey Service

![Report Structure](docs/any-file-survey-action-survey-report.png)
> Figure 4: report structure for File Survey Service

## CSV File Survey Service

*CSV File Survey* analyses the internals of a CSV file to determine the schema (column structure) and to profile the data in each column.
![Architecture](docs/csv-file-survey-action-service.png)
> Figure 5: CSV File Survey Service

![Report Structure](docs/csv-file-survey-action-survey-report.png)
> Figure 6: report structure for CSV File Survey Service

## Parquet File Survey Service

*Parquet File Survey* analyses the footer of an [Apache Parquet](https://parquet.apache.org/) file to determine the
schema (column structure) and to profile the data in each column.  It uses the
[Parquet File Connector](../data-store-connectors/file-connectors/parquet-file-connector) to read the file.

The schema, the measurements and the range of values in each column come from the file's footer.  This part of the survey
takes the same time whatever the size of the file, and it works with all versions of the Parquet format, whichever
encodings and compression algorithms are used.  The last step of the survey also reads the values in the file,
to count how many times each value occurs.  This relies on the DuckDB JDBC driver, which is included in the OMAG Server Platform.

The survey has four analysis steps.  The `finalAnalysisStep` request parameter is used to stop the survey early.

| Analysis Step | Annotation | Attached to | Description |
|---------------|------------|-------------|-------------|
| Check Asset | | | Check that the asset is a `ParquetFile` and that it has a connection to the Parquet File Connector. |
| Measure Resource | *Extract File Properties* | Asset | The properties of the file that are visible through the file system - this is the same annotation that the File Survey Service produces. |
| Measure Resource | *Extract Parquet File Properties* | Asset | The characteristics of the Parquet file: the number of rows, row groups and columns; the compressed and uncompressed size of the data; the software that wrote the file; and the features of the Parquet format that the file uses (declared format version, data page versions, encodings, compression algorithms and the style of type annotation).  These show what a reader needs to support in order to read the file. |
| Schema Extraction | *Extract Schema From Footer* | Schema type | The number of columns and their names.  In addition, a `TabularColumn` is attached to the asset's schema type for each top-level column in the file.  It records the column's name, position, data type and whether it is nullable.  A column that contains nested data is described using `array`, `map` and `struct` in its data type.  If the asset already has a schema, the columns are matched by name: matching columns are updated, new columns are added and the columns that are no longer in the file are archived. |
| Profile Data | *Profile Column From Statistics* | Column | One annotation for each column that holds values (so there are several for a column that contains nested data).  It records the smallest and largest values, the number of values, nulls and (if the writer recorded it) distinct values, the compressed and uncompressed size, and how the values are stored.  These are calculated by combining the statistics that the writer saved for each row group.  In addition, the values of each top-level column that holds a single value are read, and how many times each value occurs is recorded if there are no more than 100 different values.  This is intended for columns that hold codes or categories. |

The profile of a column is only as good as the statistics that the writer of the file chose to save.
The smallest and largest values are omitted if they are missing from any of the row groups,
or if they were written by an older writer for a type of column where the values are known to be unreliable.

The counts of each value need the DuckDB JDBC driver.  If it is not available, the survey logs
message `SURVEY-ACTION-SERVICE-0007` and completes without them.  Stopping the survey at an earlier step, using
`finalAnalysisStep`, avoids reading the values in a very large file.

The survey fails with message `PARQUET-FILE-CONNECTOR-400-001` if the file is not a Parquet file.

----
----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.