<!-- SPDX-License-Identifier: Apache-2.0 -->

# Enterprise Repository Connector

The enterprise repository connector
can issue calls to multiple OMRS repository connectors and aggregate the
results as if the metadata was stored in a single repository.
This is how metadata queries are federated across open metadata repositories.  

Since all implementations of OMRS repository connectors have the same API,
the Enterprise Repository Connector is able to work with
a heterogeneous collection of repositories.

The **[Enterprise Connector Manager](enterprise-connector-manager.md)**
dynamically configures the enterprise repository connector with appropriate
instances of the OMRS connectors using information from the [cohort registry](cohort-registry.md).