/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping;

import clojure.lang.IPersistentMap;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.XTDBOMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import xtdb.api.XtdbDocument;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntitySummary;

import java.io.IOException;
import java.util.List;

/**
 * Maps the properties of EntitySummaries between persistence and objects.
 */
public class EntitySummaryMapping extends InstanceHeaderMapping {

    public static final String INSTANCE_REF_PREFIX = "e";
    public static final String N_CLASSIFICATIONS = "classifications";

    /**
     * Construct a mapping from an EntityDetail (to map to a XTDB representation).
     * @param xtdbConnector connectivity to XTDB
     * @param entitySummary from which to map
     */
    public EntitySummaryMapping(XTDBOMRSRepositoryConnector xtdbConnector,
                                EntitySummary entitySummary) {
        super(xtdbConnector, entitySummary);
    }

    /**
     * Construct a mapping from a XTDB map (to map to an Egeria representation).
     * @param xtdbConnector connectivity to XTDB
     * @param xtdbDoc from which to map
     */
    public EntitySummaryMapping(XTDBOMRSRepositoryConnector xtdbConnector,
                                XtdbDocument xtdbDoc) {
        super(xtdbConnector, xtdbDoc);
    }

    /**
     * Map from XTDB to Egeria.
     * @return EntityDetail
     * @see #EntitySummaryMapping(XTDBOMRSRepositoryConnector, XtdbDocument)
     */
    public EntitySummary toEgeria() {
        if (instanceHeader == null && xtdbDoc != null) {
            instanceHeader = new EntitySummary();
            fromDoc();
        }
        if (instanceHeader != null) {
            return (EntitySummary) instanceHeader;
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
        List<Classification> classifications = ((EntitySummary) instanceHeader).getClassifications();
        ClassificationMapping cm = new ClassificationMapping(xtdbConnector, classifications);
        cm.addToXtdbDoc(builder);
        return builder;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void fromDoc() {
        super.fromDoc();
        ClassificationMapping cm = new ClassificationMapping(xtdbConnector, xtdbDoc);
        List<Classification> classifications = cm.toEgeria();
        ((EntitySummary) instanceHeader).setClassifications(classifications);
    }

    /**
     * Translate the provided XTDB representation into an Egeria representation.
     * @param es into which to map
     * @param doc from which to map
     * @throws IOException on any issue deserializing values
     * @throws InvalidParameterException for any unmapped properties
     */
    protected static void fromMap(EntitySummary es,
                                  IPersistentMap doc) throws IOException, InvalidParameterException {
        InstanceHeaderMapping.fromMap(es, doc);
        List<Classification> classifications = ClassificationMapping.fromMap(doc);
        es.setClassifications(classifications);
    }

    /**
     * Retrieve the canonical reference to the entity summary with the specified GUID.
     * @param guid of the entity summary to reference
     * @return String giving the XTDB reference to this entity summary document
     */
    public static String getReference(String guid) {
        return getReference(INSTANCE_REF_PREFIX, guid);
    }

}
