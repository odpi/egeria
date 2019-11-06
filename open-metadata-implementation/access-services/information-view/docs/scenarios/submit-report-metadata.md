<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Submit a report

An external tool can submit a report metadata representation. The request must contain also the registration guid of the external tool, obtained when the tool initially registered.
For each report column, the metadata  contains:
* basic properties of the column like name, formula
* the list of business terms associated, if any. This requires the list of guids of the business terms. 
* a list of sources for the column. This can be a table column, a view column, a report column or a file column. It can be resolved in 3 ways: through guid (preferred as it is the global unique identifier,
through qualifiedName or by passing the details used for lookup( such as networkAddress, database name, schema name, table name and column name for a database column). 
First 2 options should be preferred as the source is clearly indicated.
The report metadata representation can only be updated by the owning tool, meaning the registration guid must match the one used at initial creation step.
An example of a metadata representation of the report can be seen in [reportDiagram](ReportDiagramExample.png) 

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.