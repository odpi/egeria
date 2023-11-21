<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2019. -->

# Asset Management Samples

The asset management samples illustrate the use of the Java APIs associated with the
[Asset Owner OMAS](https://egeria-project.org/services/omas/asset-owner/overview) and the
[Asset Consumer OMAS](https://egeria-project.org/services/omas/asset-consumer/overview).

In simple terms, the Asset Owner OMAS sets up descriptions of
[Assets](https://egeria-project.org/concepts/asset) in the
open metadata repositories.  The owner of the asset is defining what the asset is about and how it should
be governed and used.

The Asset Consumer OMAS is used by tools that support people wanting to locate and use the assets.
So essentially it is a query and read interface.  The exception is that the asset consumers can provide feedback
to the asset owner and other consumers on the assets themselves and also tag them with labels that make them
easier to find and understand.

Since the Asset Consumer OMAS is using metadata set up by the Asset Owner OMAS it is helpful to learn about the
two interfaces together, which is why the samples are combined.


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.