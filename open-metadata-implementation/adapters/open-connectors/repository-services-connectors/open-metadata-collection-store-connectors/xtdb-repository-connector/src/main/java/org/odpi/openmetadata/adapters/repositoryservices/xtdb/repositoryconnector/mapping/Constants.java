/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping;

import clojure.lang.Keyword;

/**
 * A set of constants to use across mappings.
 */
public class Constants {

    private Constants() {}

    // Common strings
    private static final String XTDB_API = "xtdb.api";
    public static final String ENTITY_GUID = "entityGUID";
    public static final String RELATIONSHIP_GUID = "relationshipGUID";
    public static final String HOME_METADATA_COLLECTION_ID = "homeMetadataCollectionId";
    public static final String QUERY_WITH = "Querying with: {}";
    public static final String FOUND_RESULTS = "Found results: {}";
    public static final String WRITE_RESULTS = " ... results: {}";

    // Config details
    public static final Keyword XTDB_VERSION = Keyword.intern("xtdb.version", "version");
    public static final String XTDB_LUCENE = "xtdb.lucene/lucene-store";

    // Shared properties
    public static final Keyword XTDB_PK = Keyword.intern("xt", "id");
    public static final Keyword XTDB_TX_TIME = Keyword.intern(XTDB_API, "tx-time");
    public static final Keyword XTDB_VALID_TIME = Keyword.intern(XTDB_API, "valid-time");
    public static final Keyword XTDB_DOC = Keyword.intern(XTDB_API, "doc");

    // Graph query limits
    public static final int MAX_TRAVERSAL_DEPTH = 40;

}
