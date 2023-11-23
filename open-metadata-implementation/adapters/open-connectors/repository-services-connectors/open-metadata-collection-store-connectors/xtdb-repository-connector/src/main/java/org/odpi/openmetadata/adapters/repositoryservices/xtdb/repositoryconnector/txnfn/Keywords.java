/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.txnfn;

import clojure.lang.Keyword;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping.*;

/**
 * Keywords for re-use across the various transaction functions.
 */
public abstract class Keywords {

    public static final Keyword VERSION = Keyword.intern(InstanceAuditHeaderMapping.VERSION);
    public static final Keyword MAINTAINED_BY = Keyword.intern(InstanceAuditHeaderMapping.MAINTAINED_BY);
    public static final Keyword UPDATED_BY = Keyword.intern(InstanceAuditHeaderMapping.UPDATED_BY);
    public static final Keyword UPDATE_TIME = Keyword.intern(InstanceAuditHeaderMapping.UPDATE_TIME);
    public static final Keyword CREATE_TIME = Keyword.intern(InstanceAuditHeaderMapping.CREATE_TIME);
    public static final Keyword CURRENT_STATUS = Keyword.intern(InstanceAuditHeaderMapping.CURRENT_STATUS);
    public static final Keyword STATUS_ON_DELETE = Keyword.intern(InstanceAuditHeaderMapping.STATUS_ON_DELETE);
    public static final Keyword INSTANCE_PROVENANCE_TYPE = Keyword.intern(InstanceAuditHeaderMapping.INSTANCE_PROVENANCE_TYPE);
    public static final Keyword METADATA_COLLECTION_ID = Keyword.intern(InstanceAuditHeaderMapping.METADATA_COLLECTION_ID);
    public static final Keyword METADATA_COLLECTION_NAME = Keyword.intern(InstanceAuditHeaderMapping.METADATA_COLLECTION_NAME);
    public static final Keyword REPLICATED_BY = Keyword.intern(InstanceAuditHeaderMapping.REPLICATED_BY);
    public static final Keyword TYPE_DEF_GUIDS = Keyword.intern(InstanceAuditHeaderMapping.TYPE_DEF_GUIDS);
    public static final Keyword RE_IDENTIFIED_FROM_GUID = Keyword.intern(InstanceHeaderMapping.RE_IDENTIFIED_FROM_GUID);

    public static final Keyword ENTITY_PROXY_ONLY_MARKER = Keyword.intern(EntityProxyMapping.ENTITY_PROXY_ONLY_MARKER);

    public static final Keyword ENTITY_PROXIES = Keyword.intern(RelationshipMapping.ENTITY_PROXIES);

    public static final Keyword LAST_CLASSIFICATION_CHANGE = Keyword.intern(ClassificationMapping.LAST_CLASSIFICATION_CHANGE);

}
