<!-- SPDX-License-Identifier: Apache-2.0 -->
  
# Open Metadata Collection Store Connectors

The open metadata collection store connectors contain connectors that
support mappings to different vendor's metadata repositories.
Each repository needs an open metadata repository connector and
and an optional open metadata repository event mapper.

These connectors are then installed as the local repository in the
Open Metadata Repository Services (OMRS).

* **[graph-repository-connector](graph-repository-connector)** -
provides a local repository that uses a graph store as its persistence store.

* **[inmemory-repository-connector](inmemory-repository-connector)** -
provides a local repository that is entirely in memory.  Its is useful for
testing/developing OMASs and demos.

* **[omrs-rest-repository-connector](omrs-rest-repository-connector)** -
uses the OMRS REST API to call an open metadata compliant repository.