/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

/**
 * Mappers that catalogue Bitol documents in open metadata (and, in later phases, generate them from open metadata).
 * The mappers work through the connector context clients so that they can run inside an integration connector or a
 * view service.  DataProductMapper handles Open Data Product Standard (ODPS) documents; BitolMapperBase holds the
 * logic shared with the Open Data Contract Standard (ODCS) mapper.
 */
package org.odpi.openmetadata.frameworks.integration.bitol.mapping;
