/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping;

import clojure.lang.IPersistentMap;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.XTDBOMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import xtdb.api.XtdbDocument;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;

import java.io.IOException;

/**
 * Maps the properties of EntityDetails between persistence and objects.
 */
public class EntityDetailMapping extends EntitySummaryMapping {

    public static final String ENTITY_PROPERTIES_NS = "entityProperties";

    /**
     * Construct a mapping from an EntityDetail (to map to a XTDB representation).
     * @param xtdbConnector connectivity to XTDB
     * @param entityDetail from which to map
     */
    public EntityDetailMapping(XTDBOMRSRepositoryConnector xtdbConnector,
                               EntityDetail entityDetail) {
        super(xtdbConnector, entityDetail);
    }

    /**
     * Construct a mapping from a XTDB map (to map to an Egeria representation).
     * @param xtdbConnector connectivity to XTDB
     * @param xtdbDoc from which to map
     */
    public EntityDetailMapping(XTDBOMRSRepositoryConnector xtdbConnector,
                               XtdbDocument xtdbDoc) {
        super(xtdbConnector, xtdbDoc);
    }

    /**
     * Map from XTDB to Egeria.
     * @return EntityDetail
     * @see #EntityDetailMapping(XTDBOMRSRepositoryConnector, XtdbDocument)
     */
    @Override
    public EntityDetail toEgeria() {
        if (instanceHeader == null && xtdbDoc != null) {
            instanceHeader = new EntityDetail();
            fromDoc();
        }
        if (instanceHeader != null) {
            return (EntityDetail) instanceHeader;
        } else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected XtdbDocument.Builder toDoc() {
        XtdbDocument.Builder builder = super.toDoc();
        // overwrite any internal marker that this is only a proxy
        builder.put(EntityProxyMapping.ENTITY_PROXY_ONLY_MARKER, false);
        InstancePropertiesMapping.addToDoc(xtdbConnector, builder, instanceHeader.getType(), ((EntityDetail) instanceHeader).getProperties());
        return builder;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void fromDoc() {
        super.fromDoc();
        InstanceProperties ip = InstancePropertiesMapping.getFromDoc(xtdbConnector, instanceHeader.getType(), xtdbDoc);
        ((EntityDetail) instanceHeader).setProperties(ip);
    }

    /**
     * Translate the provided XTDB representation into an Egeria representation.
     * @param doc from which to map
     * @return EntityDetail the Egeria representation of the XTDB document
     * @throws IOException on any issue deserializing values
     * @throws InvalidParameterException for any unmapped properties
     */
    public static EntityDetail fromMap(IPersistentMap doc) throws IOException, InvalidParameterException {
        if (doc == null) {
            return null;
        } else {
            EntityDetail ed = new EntityDetail();
            EntitySummaryMapping.fromMap(ed, doc);
            InstanceType entityType = getTypeFromInstance(doc, null);
            InstanceProperties ip = InstancePropertiesMapping.getFromMap(entityType, doc);
            ed.setProperties(ip);
            return ed;
        }
    }

    /**
     * Translate the provided GUID into a XTDB reference.
     * @param guid to translate
     * @return String reference for XTDB
     */
    public static String getReference(String guid) {
        return EntitySummaryMapping.getReference(guid);
    }

}
