<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->
![Technical Preview](../../../images/egeria-content-status-tech-preview.png)

# Design Model Archives

The Design Model Archives provide utilities for loading common/standard design models from third parties.

The [OWL Canonical Glossary model](glossary-canonical-model/docs/owlcanonicalglossarymodel/README.md) provides the ability to
create an Egeria Archive from a Canonical Glossary defined using [OWL](https://www.w3.org/OWL/).
This is an experimental archive builder using a restricted dialect of OWL.  There is an example in `test/resources`.

If the design model is being maintained and developed, the writer
class may be scheduled to periodically recreate the open metadata archive
with the latest content.  This can then be reloaded once the server is running,
or at next server restart if the archive is configured in the server's configuration
document.

The same archive can be loaded multiple times in the server without ill-effect because
the writer records the unique identifiers ([GUIDs](https://egeria-project.org/concepts/guid))
for each element in the archive in a file.  As long as that file is available
to the writer, it will use the same GUID for each element and the
open metadata repository will only add new content to the repository
each time the archive is loaded.



----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
