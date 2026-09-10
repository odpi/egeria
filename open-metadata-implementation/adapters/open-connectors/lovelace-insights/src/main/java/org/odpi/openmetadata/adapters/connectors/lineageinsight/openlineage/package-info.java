/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

/**
 * Lovelace services that analyse a historical store of OpenLineage events (as written by the file-based
 * OpenLineage log store integration connector) and refine the metadata that the OpenLineage cataloguer
 * maintains as events arrive: the run profile of each process, the data scope of each data asset and a
 * summary of the data quality results reported for each data asset.
 */
package org.odpi.openmetadata.adapters.connectors.lineageinsight.openlineage;
