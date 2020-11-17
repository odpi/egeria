<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Asset Manager Open Metadata Access Service (OMAS) Client

The Asset Manager OMAS supports a REST API for requests and an event-based
interface for asynchronous integration.  This client
package provides Java clients to make it easier to call these interfaces.
They are primarily intended to be called by the [Catalog Integrator OMIS]()

There are 4 clients:

 * **MetadataSourceClient** enables the caller to create the Software Server Capability that represents
   the source, or owner, of the metadata.  These sources are database manager, file managers and 
   file owning applications.
         
 * **GlossaryManagerClient** enables the caller to describe databases, database schemas, database tables
   and database columns.
    
 * **FilesAndFoldersClient** enables the caller to describe files and the organizing element structure
   around it.

 * **AssetManagerEventClient** enables the client to send and receive events from the Asset Manager OMAS.


----
Return to the [asset-manager](..) module.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
