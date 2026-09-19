/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

/**
 * The darwin connector maintains the DigitalProductDependency relationships between digital products.  It derives
 * them from the data lineage between the assets that are members of the products, following each information
 * supply chain in turn, and records an exception against any dependency asserted by an external user that the
 * lineage does not support.
 */
package org.odpi.openmetadata.adapters.connectors.darwin;
