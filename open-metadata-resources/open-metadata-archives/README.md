<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->
  
# Open Metadata Archives
  
The [open metadata archives](https://egeria-project.org/concepts/open-metadata-archive) provide pre-canned content (open metadata types and instances) to load into an open metadata
repository.  

## Supported utilities for open metadata archives

Egeria supports the following open metadata archives.  Associated with each archive
are utilities that help you build additional archives of your own content.

* [Content Pack Helpers](content-pack-helpers) provide common functions for building archives based on the open metadata types.

* [Open Metadata Types](open-metadata-types) - the Egeria Open Metadata Type Definitions.
This archive is always loaded by each OMAG metadata repository server at start-up.
This is to reduce the chance that new types developed by a third party have names that conflict with the open metadata types.
There is also a utility to create the archive file for these open metadata types.
The find out more about the Open Metadata Types [click here](https://egeria-project.org/types).

* [Core Content Pack](core-content-pack) - provides utilities for building the
open metadata archive containing reference information information about one or more connectors that
follow the [Open Connector Framework (OCF)](../../open-metadata-implementation/frameworks/open-connector-framework).

* [Design Model Archives](design-model-archives) - provides utilities to manage common/standard model content from third parties.  

----
* Return to [open-metadata-resources](..)
* Return to [Home Page](https://egeria-project.org/)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
