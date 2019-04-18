<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Validate incoming type definition event test case

This test case is generated for each of the incoming type definition events from the technology under test.
It is looking to ensure that the event header is correctly filled out.

As part of its processing, it extracts the type definition itself.  This may be an
AttributeTypeDef or a TypeDef.  Depending on what was retrieved, the repository workbench
will kick of additional test cases to validate this type definition is consistent with
the definition supported by the repository and is usable.

## Operation

This test case steps through the header fields of the event and reports the values found as discovered properties.

## Assertions



----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.