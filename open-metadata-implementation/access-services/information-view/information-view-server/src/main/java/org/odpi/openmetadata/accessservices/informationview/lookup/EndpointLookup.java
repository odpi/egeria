/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.lookup;

import org.odpi.openmetadata.accessservices.informationview.contentmanager.OMEntityDao;
import org.odpi.openmetadata.accessservices.informationview.events.EndpointSource;
import org.odpi.openmetadata.accessservices.informationview.utils.Constants;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.Optional;

public class EndpointLookup extends EntityLookup<EndpointSource> {

    private static final Logger log = LoggerFactory.getLogger(EndpointLookup.class);

    public EndpointLookup(OMRSRepositoryConnector enterpriseConnector, OMEntityDao omEntityDao, EntityLookup chain, OMRSAuditLog auditLog) {
        super(enterpriseConnector, omEntityDao, chain, auditLog, Constants.ENDPOINT);

    }

    @Override
    public EntityDetail lookupEntity(EndpointSource source) {
        EntityDetail entity = Optional.ofNullable(super.lookupEntity(source))
                                                 .orElseGet(() -> findEndpoint(source));

        if(log.isDebugEnabled()) {
            log.debug("Endpoint found [{}]", entity);
        }
        return entity;

    }

    public EntityDetail findEndpoint(EndpointSource source){
        return findEntity(getMatchingProperties(source), Constants.ENDPOINT);
    }


    @Override
    protected InstanceProperties getMatchingProperties(EndpointSource source) {
        InstanceProperties matchProperties = new InstanceProperties();
        // GDW - each string property added to matchProperties shoudl be converted to exact match regex
        String sourceNetworkAddressRegex = enterpriseConnector.getRepositoryHelper().getExactMatchRegex(source.getNetworkAddress());
        matchProperties = enterpriseConnector.getRepositoryHelper().addStringPropertyToInstance(Constants.INFORMATION_VIEW_OMAS_NAME, matchProperties,
                Constants.NETWORK_ADDRESS, sourceNetworkAddressRegex, "findEndpoint");
        if(!StringUtils.isEmpty(source.getProtocol())){
            String sourceProtocolRegex = enterpriseConnector.getRepositoryHelper().getExactMatchRegex(source.getProtocol());
            matchProperties = enterpriseConnector.getRepositoryHelper().addStringPropertyToInstance(Constants.INFORMATION_VIEW_OMAS_NAME, matchProperties,
                    Constants.PROTOCOL, sourceProtocolRegex, "findEndpoint");
        }
        //matchProperties = enterpriseConnector.getRepositoryHelper().addStringArrayPropertyToInstance(Constants.INFORMATION_VIEW_OMAS_NAME, matchProperties, Constants.ZONE_MEMBERSHIP, supportedZones, "addEntity");

        return matchProperties;
    }

}
