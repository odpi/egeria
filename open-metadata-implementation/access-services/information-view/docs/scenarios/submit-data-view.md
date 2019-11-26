<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Submit a data view

An external tool can submit a data view as a view on top of other sources. The request must contain also the registration guid of the external tool, obtained when the tool initially registered.
For each data view column, the metadata  contains:
* basic properties of the column like name, formula, comment, description
* the list of business terms associated, if any. This requires the list of guids of the business terms. 
* a list of sources for the column. This can be a table column, a view column, a report column or a file column. It can be resolved in 3 ways: through guid (preferred as it is the global unique identifier,
through qualifiedName or by passing the details used for lookup( such as networkAddress, database name, schema name, table name and column name for a database column). 
First 2 options should be preferred as the source is clearly indicated.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.