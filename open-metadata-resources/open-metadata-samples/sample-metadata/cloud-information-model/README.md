<!-- SPDX-License-Identifier: Apache-2.0 -->
<!-- Copyright Contributors to the Egeria Project-->

# CIM archive writer

This module builds the Cloud Information Model open metadata archive from the `jsonld` model that is downloadable 
from the [Egeria GitHub Repository](https://raw.githubusercontent.com/odpi/egeria/refs/heads/main/cloud-information-model.jsonld).

The resulting open metadata archive can be found at [content-packs/CloudInformationModel.omarchive](https://raw.githubusercontent.com/odpi/egeria/refs/heads/main/content-packs/CloudInformationModel.omarchive).
It is included in the standard Egeria distribution so it can be loaded through the [Runtime Manager OMVS](https://egeria-project.org/services/omvs/runtime-manager/overview/).

## Construction process

This module has three main components:

* A Java object model that describes the contents of the `jsonld` file.
* A parser that extracts the content from the `jsonld` file and populates the object model.
* An open metadata archive writer that reads the contents from the object mode and creates:

   * A glossary describing the properties.
   * A data dictionary describing the data fields and their relationships.
   * A concept model describing the major objects.

More details on the contents of the resulting open metadata archive can be found on [Egeria's main documentation site](https://egeria-project.org/content-packs/cim-content-pack/overview/).


## Origin of the cloud information model

The `cloud-information-model.jsonld` that is part of this repository is derived from the open source [Cloud Information Model](https://github.com/cloudinformationmodel/cloudinformationmodel) project.  This original project is archived and so no further changes are being accepted.

The original file is [model.jsonld](https://raw.githubusercontent.com/cloudinformationmodel/cloudinformationmodel/refs/heads/master/dist/model.jsonld).  The Egeria community have kept the format of the contents exactly the same.  The changes made correct typos in field names and comments.



