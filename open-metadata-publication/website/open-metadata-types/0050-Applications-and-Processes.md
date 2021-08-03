<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# 0050 Applications and Processes

Applications provide business or management logic.
They are often custom built but may also be brought as a package.
They are deployed onto a server as a [SoftwareServerCapability](0042-Software-Server-Capabilities.md).

![UML](0050-Applications-and-Processes.png#pagewidth)

## Deprecated Types

The **RuntimeForProcess** relationship is superfluous - use [ServerAssetUse](0045-Servers-and-Assets.md) since **Application** is a [SoftwareServerCapability](0042-Software-Server-Capabilities.md).

---

* Return to [Area 0](Area-0-models.md).
* Return to [Overview](.).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.