<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Coco Pharmaceuticals clinical trial Bitol documents

These sample documents describe the data shared during Coco Pharmaceuticals' *Teddy Bear Drop Foot* clinical trial
(project `PROJ-CT-TBDF`) using the [Bitol](https://bitol.io) open standards:

* `DataProduct/teddy-bear-drop-foot-weekly-measurements.odps.yaml` is an **Open Data Product Standard (ODPS)** document
  published by the clinical trial team.  It describes the data product that gathers the weekly patient measurements
  from the participating hospitals (its input ports) and provides the validated measurements to the research team
  (its output port).
* `DataContract/*.odcs.yaml` are **Open Data Contract Standard (ODCS)** documents.  There is one for each hospital
  taking part in the trial - Hampton Hospital, Oak Dene Hospital and Old Market Hospital - describing the data sharing
  agreement under which the hospital sends its weekly measurements, and one for the validated data set the trial
  provides to its researchers.  The weekly measurement files themselves are the `*-drop-foot-weekly-measurements`
  directories alongside this one.

The documents can be catalogued by placing them in the `loading-bay/bitol` directory of a platform running the
[Bitol Content Pack](https://egeria-project.org/content-packs/bitol-content-pack/overview), or by publishing them to an
integration daemon's `publish-data-product` and `publish-data-contract` endpoints.  The `bitol-fvt` functional
verification test suite uses them to validate the Bitol connectors.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
