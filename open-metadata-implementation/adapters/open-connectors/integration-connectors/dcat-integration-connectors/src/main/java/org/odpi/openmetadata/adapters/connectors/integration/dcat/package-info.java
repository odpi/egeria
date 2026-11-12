/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

/**
 * Integration connectors that exchange descriptions of data assets using the W3C Data Catalog Vocabulary (DCAT).
 * The receivers publish DCAT documents from file directories and event topics to the integration daemon's DCAT
 * listeners; the cataloguer turns each document into data set, distribution and data service assets; and the
 * publisher writes the DCAT descriptions of the assets in a publishing catalog to the file system.
 */
package org.odpi.openmetadata.adapters.connectors.integration.dcat;
