<!-- SPDX-License-Identifier: Apache-2.0 -->

# Cohort Registry

The OMRS Cohort Registry resides in each **[open metadata repository](../open-metadata-repository.md)**.
It is responsible for registering a server with a specific **[open metadata repository cohort](../open-metadata-repository-cohort.md)**. 

An open metadata repository cohort is a group of metadata servers that need to share metadata.
Each of these servers runs their own Cohort Registry.
The Cohort Registries communicate with one another peer-to-peer,
exchanging registration information and validating that the open metadata
type definitions that the repositories in the cohort each support are not in
conflict with one another.