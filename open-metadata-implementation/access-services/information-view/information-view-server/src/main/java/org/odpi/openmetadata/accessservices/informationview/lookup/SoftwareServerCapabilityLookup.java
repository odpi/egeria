/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.lookup;

import org.odpi.openmetadata.accessservices.informationview.contentmanager.OMEntityDao;
import org.odpi.openmetadata.accessservices.informationview.events.SoftwareServerCapabilitySource;
import org.odpi.openmetadata.accessservices.informationview.ffdc.InformationViewErrorCode;
import org.odpi.openmetadata.accessservices.informationview.ffdc.exceptions.runtime.EntityNotFoundException;
import org.odpi.openmetadata.accessservices.informationview.utils.Constants;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PagingErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Collectors;

public class SoftwareServerCapabilityLookup extends EntityLookup<SoftwareServerCapabilitySource>{

    private static final Logger log = LoggerFactory.getLogger(EndpointLookup.class);

    public SoftwareServerCapabilityLookup(OMRSRepositoryConnector enterpriseConnector, OMEntityDao omEntityDao,
                                          EntityLookup parentChain, OMRSAuditLog auditLog) {
        super(enterpriseConnector, omEntityDao, parentChain, auditLog);
    }

    @Override
    public EntityDetail lookupEntity(SoftwareServerCapabilitySource source) throws UserNotAuthorizedException,
                                                                                   FunctionNotSupportedException,
                                                                                   InvalidParameterException,
                                                                                   RepositoryErrorException,
                                                                                   PropertyErrorException,
                                                                                   TypeErrorException,
                                                                                   PagingErrorException{
        InstanceProperties matchingProperties = getMatchingProperties(source);
        EntityDetail entity = findEntity(matchingProperties, Constants.SOFTWARE_SERVER_CAPABILITY);
        if(entity == null){
            String keys = matchingProperties.getInstanceProperties().keySet().stream().collect(Collectors.joining());
            String values = matchingProperties.getInstanceProperties().values().stream().map(v -> ((PrimitivePropertyValue)v).getPrimitiveValue().toString()).collect(Collectors.joining());
            throw new EntityNotFoundException(InformationViewErrorCode.ENTITY_NOT_FOUND_EXCEPTION.getHttpErrorCode(), SoftwareServerCapabilityLookup.class.getName(), InformationViewErrorCode.ENTITY_NOT_FOUND_EXCEPTION.getFormattedErrorMessage(keys,values), InformationViewErrorCode.ENTITY_NOT_FOUND_EXCEPTION.getSystemAction(), InformationViewErrorCode.ENTITY_NOT_FOUND_EXCEPTION.getUserAction(), null);
        }
        log.debug("SoftwareServerCapability found [{}]", entity);
        return entity;
    }

    @Override
    protected InstanceProperties getMatchingProperties(SoftwareServerCapabilitySource softwareServerCapabilitySource) {
        InstanceProperties matchProperties = new InstanceProperties();
        matchProperties = enterpriseConnector.getRepositoryHelper().addStringPropertyToInstance(Constants.INFORMATION_VIEW_OMAS_NAME, matchProperties, Constants.NAME, softwareServerCapabilitySource.getName(), "getMatchingProperties");
        return matchProperties;
    }
}
