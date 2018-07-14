<!-- SPDX-License-Identifier: Apache-2.0 -->

# Project Management Open Metadata Access Service (OMAS)

The Project Management OMAS provides APIs and events for tools and applications
focused on managing information about the governance projects in progress
and the metadata they are managing.  It supports the planning and execution
of metadata changes as well as providing an overview of the status of
the current governance project.

The module structure for the Project Management OMAS is as follows:

* [project-management-client](project-management-client) supports the client library.
* [project-management-api](project-management-api) supports the common Java classes that are used both by the client and the server.
* [project-management-server](project-management-server) supports in implementation of the access service and its related event management.
* [project-management-spring](project-management-spring) supports the REST API using the [Spring](../../../developer-resources/Spring.md) libraries.
