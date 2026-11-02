<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Security control catalogs

These JSON files are read by `SecurityPackArchiveWriter` to build `SecurityContentPack.omarchive`.

| File | Source | Version |
|---|---|---|
| `nist-csf-2.0.json` | [`NIST_CSF_v2.0_catalog-min.json`](https://github.com/usnistgov/oscal-content/blob/main/nist.gov/CSF/v2.0/json/NIST_CSF_v2.0_catalog-min.json) | CSF 2.0 (OSCAL catalog 1.2.1) |
| `nist-sp-800-53-rev5.json` | [`NIST_SP-800-53_rev5_catalog-min.json`](https://github.com/usnistgov/oscal-content/blob/main/nist.gov/SP800-53/rev5/json/NIST_SP-800-53_rev5_catalog-min.json) | SP 800-53 Rev. 5.2.0 |

Both catalogs are works of the U.S. Government.  They are in the public domain in the United States, and NIST waives
copyright worldwide under [CC0 1.0](https://creativecommons.org/publicdomain/zero/1.0/).

The files are a compact form of NIST's OSCAL catalogs, generated on 2026-10-26:

* withdrawn controls, categories and subcategories are left out;
* each SP 800-53 control and control enhancement keeps its NIST label (for example `AC-2` and `AC-2(1)`), its
  title, its statement and its discussion;
* parameters in the statements are written the way NIST publishes them, as
  `[Assignment: organization-defined ...]` or `[Selection (one or more): ...; ...]`;
* each CSF subcategory keeps its identifier, its outcome statement and its implementation examples.

To refresh them when NIST publishes a new version, download the two OSCAL files above into a directory and run
[`scripts/convert_oscal_security_catalogs.py`](../../../../scripts/convert_oscal_security_catalogs.py), which applies
these rules (it needs only the Python standard library):

```
python3 scripts/convert_oscal_security_catalogs.py <directory holding the OSCAL files> src/main/resources/security-catalogs
```

Then rerun the content pack writer.  Elements keep their GUIDs because they are keyed by their
qualified names (for example `NIST SP 800-53 Rev 5::AC-2`) in `EgeriaContentPacksGUIDMap.json`.
