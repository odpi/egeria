<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2019. -->

![TechPreview](../../../../images/egeria-content-status-released.png#pagewidth)

# File Survey Connectors

The file survey connectors contain the implementation of three
[survey action services](https://egeria-project.org/concepts/survey-action-service) that extract and report on files and folders.
The connectors are provided by the Egeria community.

* Folder Survey Service -  *FolderSurveyService* is a survey action service implementation for analysing the files nested in a folder. The depth of the survey is controlled by the analysisLevel request parameter.

    * The default value is 'TOP_LEVEL_ONLY' which produces summary statistics for the top-level directory only.
    * If it is set to 'ALL_FOLDERS' then there are statistics created for the top level directory, and all subdirectories.
    * If it set to 'TOP_LEVEL_AND_FILES' then statistics are created for the top-level directory and all files that are encountered.
    * Finally, if it is set to 'ALL_FOLDERS_AND_FILES' then statistics are created for all directories and files encountered.", "string", "myFile.csv".

* File Survey Service - *File Survey Service* extracts properties about a file.  It then maps these properties to Egeria's file reference data to enable summary statistics to be calculated.

* CSV Survey Service - *CSV File Survey* analyses the internals of a CSV file to determine the schema (column structure) and to profile the data in each column.

Return to [open-connectors](..) module.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.