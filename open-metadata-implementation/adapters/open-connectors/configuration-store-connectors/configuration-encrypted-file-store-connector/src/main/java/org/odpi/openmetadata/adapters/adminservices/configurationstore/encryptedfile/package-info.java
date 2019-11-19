/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

/**
 * The encrypted file configuration store allows the storage of the entirety of configuration files as encrypted
 * files. Internally the mechanisms use Google Tink, and currently only leverage a relatively secure local key
 * storage technique (files and directories that are owner-accessible only, and cannot be exfiltrated as their names
 * are randomly generated). In future this may be extended to use other capabilities of Tink like external KMS, etc.
 */
package org.odpi.openmetadata.adapters.adminservices.configurationstore.encryptedfile;