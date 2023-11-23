/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping;

import clojure.lang.IPersistentMap;
import clojure.lang.Keyword;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.cache.PropertyKeywords;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.cache.TypeDefCache;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefAttribute;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import xtdb.api.XtdbDocument;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.ffdc.XTDBAuditCode;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.XTDBOMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Maps the properties of EntityProxies between persistence and objects.
 */
public class EntityProxyMapping extends EntitySummaryMapping {

    private static final String ENTITY_PROXY = "EntityProxy";

    public static final String ENTITY_PROXY_ONLY_MARKER = getKeyword(EntitySummaryMapping.INSTANCE_REF_PREFIX, "proxy");

    /**
     * Construct a mapping from an EntityDetail (to map to a XTDB representation).
     * @param xtdbConnector connectivity to XTDB
     * @param entityProxy from which to map
     */
    public EntityProxyMapping(XTDBOMRSRepositoryConnector xtdbConnector,
                              EntityProxy entityProxy) {
        super(xtdbConnector, entityProxy);
    }

    /**
     * Construct a mapping from a XTDB map (to map to an Egeria representation).
     * @param xtdbConnector connectivity to XTDB
     * @param xtdbDoc from which to map
     */
    public EntityProxyMapping(XTDBOMRSRepositoryConnector xtdbConnector,
                              XtdbDocument xtdbDoc) {
        super(xtdbConnector, xtdbDoc);
    }

    /**
     * Map from XTDB to Egeria. NOTE: This method should ONLY be used when you are certain that you have
     * only a proxy that you are translating. In general, it is better to use the getFromDoc method which
     * will automatically determine whether you have a full entity or only a proxy, and translate either to
     * only the proxy representation as-needed. (This method here should really be treated as an internal
     * method of this class, and is only public given that it overrides another public method in the parent
     * class.)
     * @return EntityProxy
     * @see #getFromDoc(XTDBOMRSRepositoryConnector, XtdbDocument)
     * @see #EntityProxyMapping(XTDBOMRSRepositoryConnector, XtdbDocument)
     */
    @Override
    public EntityProxy toEgeria() {
        if (instanceHeader == null && xtdbDoc != null) {
            instanceHeader = new EntityProxy();
            fromDoc();
        }
        if (instanceHeader != null) {
            return (EntityProxy) instanceHeader;
        } else {
            return null;
        }
    }

    /**
     * Translate the provided Egeria representation into a XTDB map.
     */
    @Override
    protected XtdbDocument.Builder toDoc() {
        XtdbDocument.Builder builder = super.toDoc();
        builder.put(ENTITY_PROXY_ONLY_MARKER, true); // set an internal marker that this is only a proxy
        InstancePropertiesMapping.addToDoc(xtdbConnector, builder, instanceHeader.getType(), ((EntityProxy) instanceHeader).getUniqueProperties());
        return builder;
    }

    /**
     * Translate the provided XTDB representation into an Egeria representation.
     */
    @Override
    protected void fromDoc() {
        super.fromDoc();
        // Note: the following will ONLY work for true proxy objects, not full entities (for full
        // entities this will return a full set of properties, not only the unique properties
        InstanceProperties uniqueProperties = InstancePropertiesMapping.getFromDoc(xtdbConnector, instanceHeader.getType(), xtdbDoc);
        ((EntityProxy) instanceHeader).setUniqueProperties(uniqueProperties);
    }

    /**
     * Translate the provided XTDB representation into an Egeria representation.
     * @param doc from which to map
     * @return EntityProxy the Egeria representation of the XTDB document
     * @throws IOException on any issue deserializing values
     * @throws InvalidParameterException for any unmapped properties
     */
    public static EntityProxy fromMap(IPersistentMap doc) throws IOException, InvalidParameterException {
        if (doc == null) {
            return null;
        } else if (isOnlyAProxy(doc)) {
            // if this is only a proxy, do a direct EntityProxyMapping
            EntityProxy ep = new EntityProxy();
            EntitySummaryMapping.fromMap(ep, doc);
            InstanceType entityType = getTypeFromInstance(doc, null);
            InstanceProperties uniqueProperties = InstancePropertiesMapping.getFromMap(entityType, doc);
            ep.setUniqueProperties(uniqueProperties);
            return ep;
        } else {
            // otherwise, we'll retrieve a full EntityDetail anyway so use the helper to translate to an EntityProxy
            // (which ensures that we populate uniqueProperties accordingly)
            EntityDetail ed = EntityDetailMapping.fromMap(doc);
            return getNewEntityProxy(ed);
        }
    }

    /**
     * Translate the provided XTDB representation of an entity into an EntityProxy.
     * @param xtdbConnector connectivity to the XTDB environment
     * @param doc containing the XTDB representation of the entity
     * @return EntityProxy
     */
    public static EntityProxy getFromDoc(XTDBOMRSRepositoryConnector xtdbConnector,
                                         XtdbDocument doc) {
        final String methodName = "getFromDoc";
        if (doc == null) {
            return null;
        } else if (isOnlyAProxy(doc)) {
            // if this is only a proxy, do a direct EntityProxyMapping
            EntityProxyMapping epm = new EntityProxyMapping(xtdbConnector, doc);
            return epm.toEgeria();
        } else {
            // otherwise, we'll retrieve a full EntityDetail anyway so use the helper to translate to an EntityProxy
            // (which ensures that we populate uniqueProperties accordingly)
            EntityDetailMapping edm = new EntityDetailMapping(xtdbConnector, doc);
            try {
                return xtdbConnector.getRepositoryHelper().getNewEntityProxy(xtdbConnector.getRepositoryName(), edm.toEgeria());
            } catch (RepositoryErrorException e) {
                xtdbConnector.logProblem(EntityProxyMapping.class.getName(),
                                         methodName,
                                         XTDBAuditCode.FAILED_RETRIEVAL,
                                         e,
                                         ENTITY_PROXY,
                                         doc.getId().toString(),
                                         e.getClass().getName());
            }
            return null;
        }
    }

    /**
     * Retrieve the canonical reference to the entity summary with the specified GUID.
     * @param guid of the entity summary to reference
     * @return String giving the XTDB reference to this entity summary document
     */
    public static String getReference(String guid) {
        return EntitySummaryMapping.getReference(guid);
    }

    /**
     * Indicates whether the provided map represents only an EntityProxy (true) or a full EntityDetail (false).
     * @param doc containing the XTDB representation
     * @return boolean
     */
    public static boolean isOnlyAProxy(XtdbDocument doc) {
        Boolean only = (Boolean) doc.get(ENTITY_PROXY_ONLY_MARKER);
        return only != null && only;
    }

    /**
     * Indicates whether the provided map represents only an EntityProxy (true) or a full EntityDetail (false).
     * @param doc containing the XTDB representation
     * @return boolean
     */
    public static boolean isOnlyAProxy(IPersistentMap doc) {
        Boolean only = (Boolean) doc.valAt(Keyword.intern(ENTITY_PROXY_ONLY_MARKER));
        return only != null && only;
    }

    /**
     * Generate an entity proxy from an entity and its TypeDef.
     * @param entity entity instance
     * @return new entity proxy
     */
    private static EntityProxy getNewEntityProxy(EntityDetail entity) {

        if (entity != null) {
            InstanceType type = entity.getType();
            if (type != null) {
                    String typeDefGUID = type.getTypeDefGUID();

                    EntityProxy entityProxy = new EntityProxy(entity);
                    InstanceProperties entityProperties = entity.getProperties();

                    if (entityProperties != null) {
                        Map<String, PropertyKeywords> properties = TypeDefCache.getAllPropertyKeywordsForTypeDef(typeDefGUID);
                        InstanceProperties uniqueAttributes = new InstanceProperties();

                        uniqueAttributes.setEffectiveFromTime(entityProperties.getEffectiveFromTime());
                        uniqueAttributes.setEffectiveToTime(entityProperties.getEffectiveToTime());

                        List<TypeDefAttribute> propertiesDefinition = properties.values().stream().map(PropertyKeywords::getAttribute).collect(Collectors.toList());
                        for (TypeDefAttribute typeDefAttribute : propertiesDefinition) {
                            if (typeDefAttribute != null) {
                                String propertyName = typeDefAttribute.getAttributeName();
                                if ((typeDefAttribute.isUnique()) && (propertyName != null)) {
                                    InstancePropertyValue propertyValue = entityProperties.getPropertyValue(propertyName);
                                    if (propertyValue != null) {
                                        uniqueAttributes.setProperty(propertyName, propertyValue);
                                    }
                                }
                            }
                        }

                        if ((uniqueAttributes.getPropertyCount() > 0) || (uniqueAttributes.getEffectiveFromTime() != null) || (uniqueAttributes.getEffectiveToTime() != null)) {
                            entityProxy.setUniqueProperties(uniqueAttributes);
                        }
                    }

                    return entityProxy;
            }
        }

        return null;
    }

}
