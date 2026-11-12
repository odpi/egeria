/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

/**
 * A small reader for JSON-LD documents.  It expands the terms used in a document into full IRIs and flattens the nested
 * nodes into a graph of nodes that can be looked up by identifier.  It supports the features of JSON-LD that are used
 * in DCAT descriptions (embedded contexts, prefixes, term definitions, keyword aliases, value objects, language maps,
 * lists and sets).  Contexts are never retrieved from the network: a document that refers to a remote context is read
 * using the standard prefixes and term names of the DCAT vocabulary.
 */
package org.odpi.openmetadata.frameworks.integration.dcat.jsonld;
