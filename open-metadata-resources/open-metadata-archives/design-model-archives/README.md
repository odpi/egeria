<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->
![Technical Preview](../../../images/egeria-content-status-tech-preview.png)

# Design Model Archives

The Design Model Archives provide utilities for loading common/standard design models from third parties.
Currently Egeria has two examples:

* [The Cloud Information Model](https://cloudinformationmodel.org) that provides an open source data model and glossary
related to commerce activities.
* [The Owl Canonical Glossary model](glossary-canonical-model/docs/owlcanonicalglossarymodel/README.md) that The Owl Canonical Glossary model that provides the ability to
create an Egeria Archive from a Canonical Glossary defined using open standards like [Owl](https://www.w3.org/OWL/). 

Each model has a `writer` class that generates an [Open Metadata Archive](..)
This can be loaded into an open metadata repository either at each server start
(by adding the archive to the configuration document) or a one time load
while the server is running.

If the design model is being maintained and developed, the writer
class may be scheduled to periodically recreate the open metadata archive
with the latest content.  This can then be reloaded once the server is running,
or at next server restart if the archive is configured in the server's configuration
document.

The same archive can be loaded multiple times in the server because
the writer records the unique identifiers ([GUIDs](https://egeria-project.org/concepts/guid))
for each element in the archive in a file.  As long as that file is available
to the writer, it will use the same GUID for each element and the
open metadata repository will only add new content to the repository
each time the archive is loaded.



----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
