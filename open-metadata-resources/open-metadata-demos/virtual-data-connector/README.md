<!-- SPDX-License-Identifier: Apache-2.0 -->
  
# Virtual Data Connector (VDC) Demo
  
The virtual data connector demo shows metadata exchange between two open metadata
repositories and the use of this
metadata to configure a data virtualization platform with views and a security tool to provide
metadata driven access control for the data virtualization platform.

It supports two basic use cases:

* An end user wishes to find some interesting data by searching the open metadata repositories.
* When they have found the data source they want, they wish to preview a subset of its data values
to verify that it really is the data they need.

These use cases seem simple but they raise three very important questions that creates
an explosion of requirements in Egeria.  The first question is:

* *What metadata is required to describe the data sources in such a way that the
end user can accurately locate the data sources they
need - assuming they are not familiar with the content and organization of the data sources?*

Typically the end user would want to use meaningful business terms to describe
the data they need, they may want so see related descriptions of the data and the
profile of its data values and its lineage.
Other information about the owners/stewards of the data and the organization they come
from, and any license associated with the data would also be relevant.
To provide this information, the VDC project needs to build out the
**[default types for open metadata](../../../open-metadata-publication/website/open-metadata-types/README.md)**
and **[provide a new catalog API](../../../open-metadata-implementation/access-services/asset-catalog/README.md)** and
**[interface for the understanding of data](../../../open-metadata-implementation/access-services/connected-asset/README.md)**
based on these values.

The second question is:

* *What is the security model that determines which metadata and data that each end user can see?*

Specifically, how should access be controlled - particularly
in a self-service, data exploration environment
where data is sources from many different systems and organizations need to be access
in order to discover new uses and interesting patterns in the data.
In the VDC demo we are providing a single endpoint for accessing data
(this is the virtual data connector itself) that uses an [Apache Ranger](https://ranger.apache.org/) plugin to control access.
This demo automatically configures the tag-based security access in
Apache Ranger using information from the open
metadata repositories (see [Governance Engine OMAS](../../../open-metadata-implementation/access-services/governance-engine/README.md))
in order to provide security access based on both the
confidentiality classification tags (eg PII and SPI tags) and the subject area of the data.

Finally, the third question is:

* *Where is the metadata and the data actually stored?*

Consider the case where the end user is searching for additional sources for their
project and the data that they need has not been provisioned into the data lake - it is
still on the source systems.   However, these data sources are already catalogued
in another metadata repository.  To be valuable, the open metadata catalog search needs
to be able to cast its search to reach data and metadata repositories beyond the data lake
in order to locate all available data.
Once the end user has identified interesting sources, they may then request that the
data is provisioned into the data lake for further analysis.
The VDC project will introduce the frameworks, integration and adapter capability
to allow a more enterprise view of the potential data sources, plus a metadata driven
connector framework for connecting to both data and metadata repositories.  
These frameworks are part of the open metadata and governance story and include:

* **[Open Connector Framework (OCF)](../../../open-metadata-implementation/frameworks/open-connector-framework/README.md)** that provides a plugability mechanism for repositories
to plug into the open metadata ecosystem.

* **[Open Metadata Repository Services (OMRS)](../../../open-metadata-implementation/repository-services/README.md)** provides the federated queries and metadata exchange
required to search multiple repositories using a single request.

* **[Open Connector Implementations](../../../open-metadata-implementation/adapters/open-connectors/repository-services-connectors/README.md)** to plug into the OMRS.


For more information on hos the VDC demo works see 
**[Walk-through of the VDC use case](docs/README.md)**.