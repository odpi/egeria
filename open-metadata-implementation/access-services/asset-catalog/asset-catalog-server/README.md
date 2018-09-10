<!-- SPDX-License-Identifier: Apache-2.0 -->

# Asset Catalog Open Metadata Access Service (OMAS) server-side implementation

The Asset Catalog OMAS server-side support is organized as follows:
* admin package that it is used to initialize and terminate the Asset Catalog OMAS. 
The initialization call provides this OMAS with resources from the Open Metadata Repository Services.

* audit log used to define the message content for the OMRS Audit Log.
* service package that provides the server-side implementation of the Asset Catalog Open Metadata Assess Service (OMAS).
These services provide the functionality to fetch asset's header, classification, properties and relationships