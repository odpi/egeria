<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# 0615 Schema Extraction

One of the simplest discovery processes for relational
data is to extract the schema details from the asset
through the JDBC connector getMetadata() API.
Other connectors or data sources may also provide APIs for schema extraction.
The schema is first added as an annotation.
This is then either matched with an existing schema or
a new schema is created (see [Area 5](Area-5-models.md)).
This may be completely automated, or with stewards assistance.

![UML](0615-Schema-Extraction.png)