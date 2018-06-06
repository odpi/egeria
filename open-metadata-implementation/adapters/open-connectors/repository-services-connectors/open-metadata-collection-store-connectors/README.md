<!-- SPDX-License-Identifier: Apache-2.0 -->
  
# Open Metadata Collection Store Connectors

The open metadata collection store connectors contain connectors that
support mappings to different vendor's metadata repositories.
Each repository needs an open metadata repository connector and
and an optional open metadata repository event mapper.

These connectors are then installed as the local repository in the
Open Metadata Repository Services (OMRS).

## Graph Repository Connector

Graph Repository Connector provides a local repository that uses a graph store as its persistence store.

## In Memory Repository Connector

In Memory Repository Connector provides a local repository that is entirely in memory.  Its is useful for
testing/developing OMASs and demos.

## OMRS REST API Connector

Uses the OMRS REST API to call an open metadata compliant repository.