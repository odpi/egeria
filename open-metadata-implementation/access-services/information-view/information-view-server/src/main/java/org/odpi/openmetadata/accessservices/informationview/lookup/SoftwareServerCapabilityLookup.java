/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.lookup;

import org.odpi.openmetadata.accessservices.informationview.contentmanager.OMEntityDao;
import org.odpi.openmetadata.accessservices.informationview.events.SoftwareServerCapabilitySource;
import org.odpi.openmetadata.accessservices.informationview.utils.Constants;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static org.odpi.openmetadata.accessservices.informationview.ffdc.ExceptionHandler.buildEntityNotFoundException;

public class SoftwareServerCapabilityLookup extends EntityLookup<SoftwareServerCapabilitySource>{

    private static final Logger log = LoggerFactory.getLogger(EndpointLookup.class);

    public SoftwareServerCapabilityLookup(OMRSRepositoryConnector enterpriseConnector, OMEntityDao omEntityDao,
                                          EntityLookup parentChain, OMRSAuditLog auditLog) {
        super(enterpriseConnector, omEntityDao, parentChain, auditLog, Constants.SOFTWARE_SERVER_CAPABILITY);
    }

    @Override
    public EntityDetail lookupEntity(SoftwareServerCapabilitySource source) {
       EntityDetail entity = Optional.ofNullable(super.lookupEntity(source))
                                                .orElseThrow(() -> buildEntityNotFoundException(Constants.QUALIFIED_NAME,
                                                                                                source.getQualifiedName(),
                                                                                                Constants.SOFTWARE_SERVER_CAPABILITY,
                                                                                                SoftwareServerCapabilityLookup.class.getName()));
        if (log.isDebugEnabled()) {
            log.debug("SoftwareServerCapability found [{}]", entity);
        }
        return entity;
    }

    @Override
    protected InstanceProperties getMatchingProperties(SoftwareServerCapabilitySource softwareServerCapabilitySource) {
        InstanceProperties matchProperties = new InstanceProperties();
        // GDW - each string property added to matchProperties shoudl be converted to exact match regex
        String softwareServerCapabilitySourceQualifiedNameRegex =
                enterpriseConnector.getRepositoryHelper().getExactMatchRegex(softwareServerCapabilitySource.getQualifiedName());
        matchProperties = enterpriseConnector.getRepositoryHelper().addStringPropertyToInstance(Constants.INFORMATION_VIEW_OMAS_NAME, matchProperties,
                Constants.QUALIFIED_NAME, softwareServerCapabilitySourceQualifiedNameRegex, "getMatchingProperties");
        return matchProperties;
    }
}
