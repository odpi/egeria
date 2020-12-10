/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

const integrationServices = [
  {
    "serviceName": "Files Integrator OMIS",
    "serviceURLMarker": "files-integrator",
    "serviceDescription": "Extract metadata about files stored in a file system or file manager.",
    "serviceWiki": "https://egeria.odpi.org/open-metadata-implementation/integration-services/files-integrator/"
  },
  {
    "serviceName": "Database Integrator OMIS",
    "serviceURLMarker": "database-integrator",
    "serviceDescription": "Extract metadata such as schema, tables and columns from database managers.",
    "serviceWiki": "https://egeria.odpi.org/open-metadata-implementation/integration-services/database-integrator/"
  },
  {
    "serviceName": "Lineage Integrator OMIS",
    "serviceURLMarker": "lineage-integrator",
    "serviceDescription": "Manage capture of lineage from a third party tool.",
    "serviceWiki": "https://egeria.odpi.org/open-metadata-implementation/integration-services/lineage-integrator/"
  },
  {
    "serviceName": "Catalog Integrator OMIS",
    "serviceURLMarker": "catalog-integrator",
    "serviceDescription": "Exchange metadata with third party data catalogs.",
    "serviceWiki": "https://egeria.odpi.org/open-metadata-implementation/integration-services/catalog-integrator/"
  },
  {
    "serviceName": "Organization Integrator OMIS",
    "serviceURLMarker": "organization-integrator",
    "serviceDescription": "Load information about the teams and people in an organization and return collaboration activity.",
    "serviceWiki": "https://egeria.odpi.org/open-metadata-implementation/integration-services/organization-integrator/"
  }
]

export default integrationServices;