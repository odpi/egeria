<!-- SPDX-License-Identifier: Apache-2.0 -->

# Repository origin test case

Validate the retrieval of the server origin descriptor from the open metadata repository.

## Operation

This test uses the ../open-metadata/admin-services/users/{userId}/servers/{serverName}/server-origin
operation to test that the repository knows its origin descriptor.

## Assertions

* **repository-origin-01** Origin descriptor retrieved from repository.

   The origin descriptor has successfully been retrieved from the server.
If this assertion fails, check that the server is started and the
open metadata services are activated.