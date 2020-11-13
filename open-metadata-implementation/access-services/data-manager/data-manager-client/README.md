<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Data Manager Open Metadata Access Service (OMAS) Client

The Data Manager OMAS supports a REST API for requests and an event-based
interface for asynchronous integration.  This client
package provides Java clients to make it easier
for data tools and applications to call these interfaces.

There are 4 clients:

 * **MetadataSourceClient** enables the caller to create the Software Server Capability that represents
   the source, or owner, of the metadata.  These sources are database manager, file managers and 
   file owning applications.
         
 * **DatabaseManagerClient** enables the caller to describe databases, database schemas, database tables
   and database columns.
    
 * **FilesAndFoldersClient** enables the caller to describe files and the organizing folder structure
   around it.

 * **DataManagerEventClient** enables the client to send and receive events from the Data Manager OMAS.


----
Return to the [data-manager](..) module.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
