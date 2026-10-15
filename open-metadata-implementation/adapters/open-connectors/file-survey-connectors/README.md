<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2019. -->

![Stable](../../../../images/egeria-content-status-released.png#pagewidth)

# File Survey Connectors

The file survey connectors contain the implementation of three
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
>
----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.