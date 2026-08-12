<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# The CSV Lineage Import Integration Connector

The **CSVLineageImporterConnector** loads lineage relationships between existing open metadata elements from a CSV file.

Each row of the CSV file describes one relationship to create between two elements that are already catalogued in
open metadata:

* the type and unique instance name of the first element,
* the type and unique instance name of the second element,
* the orientation of the relationship (which element is at end 1 and which is at end 2), and
* optionally, a mode value.

The connector maps the short type codes used in the CSV file (for example `LIB`, `CMP`, `TB2`, `ENT`) to open metadata
type names (`Collection`, `DisplayDataSchemaType`, `RelationalTable`, `Port`, and so on), looks up each element by its
qualified name, and creates the corresponding open metadata relationship if it does not already exist.

## Deployment and configuration

The connector uses the [CSV File Store Connector](../../data-store-connectors/file-connectors/csv-file-connector) to
read its input file, identified by the endpoint's network address on the connector's connection.
It runs in the integration daemon.


----
* Return to [Integration Connectors module](..)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
