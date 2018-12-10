/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.lookup;

import org.odpi.openmetadata.accessservices.informationview.contentmanager.EntitiesCreatorHelper;
import org.odpi.openmetadata.accessservices.informationview.events.Source;
import org.odpi.openmetadata.accessservices.informationview.utils.Constants;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EndpointLookup extends EntityLookup<Source> {

    private static final Logger log = LoggerFactory.getLogger(EndpointLookup.class);

    public EndpointLookup(OMRSRepositoryConnector enterpriseConnector, EntitiesCreatorHelper entitiesCreatorHelper, EntityLookup chain, OMRSAuditLog auditLog) {
        super(enterpriseConnector, entitiesCreatorHelper, chain, auditLog);

    }

    @Override
    public EntityDetail lookupEntity(Source source) throws Exception {
        return findEndpoint(source);//TODO check

    }

    @Override
    protected InstanceProperties getMatchingProperties(Source source) {
        InstanceProperties matchProperties = new InstanceProperties();
        matchProperties = enterpriseConnector.getRepositoryHelper().addStringPropertyToInstance("", matchProperties, Constants.NETWORK_ADDRESS, source.getNetworkAddress(), "findEndpoint");
        matchProperties = enterpriseConnector.getRepositoryHelper().addStringPropertyToInstance("", matchProperties, Constants.PROTOCOL, source.getProtocol(), "findEndpoint");
        return matchProperties;
    }


    public EntityDetail findEndpoint(Source source) {
        EntityDetail entity = findEntity(getMatchingProperties(source), Constants.ENDPOINT);
        log.info("Endpoint found [{}]", entity);
        return entity;
    }


}
