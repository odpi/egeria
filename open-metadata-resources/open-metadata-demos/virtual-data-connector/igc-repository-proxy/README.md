<!-- SPDX-License-Identifier: Apache-2.0 -->
  
# IGC Repository Proxy
  
The IGC Repository proxy is a server that provides an adapter for
[IBM Information Governance Catalog (IGC)](https://www.ibm.com/marketplace/information-governance-catalog) that enables it to participate in an
open metadata repository cohort.

Within the server are two connectors:

* A repository connector that translates calls from the
Open Metadata Repository Services (OMRS) into calls to IGC's
REST API.

* An event mapper connector that translates repository change events
from IGC into calls to OMRS so that OMRS can distribute the events to
other members of the cohort and the access services.

The server also contains an administration interface to configure the
event bus and the cohorts that IGC is connecting to.