/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

/**
 * The Environment Variable Secrets Store Connector retrieves secrets from environment variables.
 * Each secret is found in an environment variable named after the secret name,
 * prefixed with its secret's collection  and "_".  For example, the userId secret for the secret's collection
 * "MY_CONNECTOR" is in an environment variable called MY_CONNECTOR_userId.
 */
package org.odpi.openmetadata.adapters.connectors.secretsstore.envar;
