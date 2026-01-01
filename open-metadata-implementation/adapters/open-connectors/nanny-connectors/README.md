<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

# The Nanny Connectors

The Nanny Connectors provide support for the observation, analysis and improvement of an existing metadata 
catalog deployment. The idea is to create digital products that represent collections of reference data and insights
based on the content of the open metadata repositories.

A key component is the Jacquard Integration connector that assembles the 
open metadata digital products into the Open Metadata Digital Product Catalog.

There are also the tabular data set connectors - one for each type of product.
These connectors assemble a collection of open metadata into a single table structure that can
be provisioned into a destination that supports tabular data (eg CSV file, database table or kafka topic).

Finally, there are the external harvester connectors that harvest data from external sources and
create insights in open metadata for Jacquard products.  These connectors
can also feed tabular data stores with the raw data they are processing.


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.