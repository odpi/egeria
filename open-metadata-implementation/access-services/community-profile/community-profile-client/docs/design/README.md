<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Community Profile OMAS Client-side Design

The Community Profile OMAS has a simple client-side design
where a Java API maps one-to-one with the REST API.

The REST calls are made using the Spring RestTemplate class.
The Java beans are translated to JSON using Jackson databind
annotations.


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.