/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryErrorHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataProperty;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;


import java.util.ArrayList;
import java.util.List;

/**
 * OpenMetadataAPIRootHandler provides the common properties for the generic handlers.
 *
 * @param <B> bean class
 */
public class OpenMetadataAPIRootHandler<B>
{
    protected OpenMetadataAPIGenericConverter<B> converter;
    protected Class<B>                           beanClass;

    protected String                             serviceName;
    protected String                  serverName;
    protected OMRSRepositoryHelper    repositoryHelper;
    protected RepositoryHandler       repositoryHandler;
    protected InvalidParameterHandler invalidParameterHandler;

    protected String                             localServerUserId;
    protected OpenMetadataServerSecurityVerifier securityVerifier = new OpenMetadataServerSecurityVerifier();


    protected List<String> supportedZones;
    protected List<String> publishZones;
    protected List<String>                       defaultZones;

    protected RepositoryErrorHandler errorHandler;

    protected final List<String> qualifiedNamePropertyNamesList;

    protected static final String assetActionDescription = "userAssetMonitoring";

    protected AuditLog                           auditLog;


    /**
     * Construct the handler information needed to interact with the repository services
     *
     * @param converter specific converter for this bean class
     * @param beanClass name of bean class that is represented by the generic class B
     * @param serviceName      name of this service
     * @param serverName       name of the local server
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler     manages calls to the repository services
     * @param repositoryHelper provides utilities for manipulating the repository services objects
     * @param localServerUserId userId for this server
     * @param securityVerifier open metadata security services verifier
     * @param supportedZones list of zones that the access service is allowed to serve Asset instances from.
     * @param defaultZones list of zones that the access service should set in all new Asset instances.
     * @param publishZones list of zones that the access service sets up in published Asset instances.
     * @param auditLog destination for audit log events.
     */
    public OpenMetadataAPIRootHandler(OpenMetadataAPIGenericConverter<B> converter,
                                      Class<B>                           beanClass,
                                      String                             serviceName,
                                      String                             serverName,
                                      InvalidParameterHandler            invalidParameterHandler,
                                      RepositoryHandler                  repositoryHandler,
                                      OMRSRepositoryHelper               repositoryHelper,
                                      String                             localServerUserId,
                                      OpenMetadataServerSecurityVerifier securityVerifier,
                                      List<String>                       supportedZones,
                                      List<String>                       defaultZones,
                                      List<String>                       publishZones,
                                      AuditLog                           auditLog)
    {
        this.converter = converter;
        this.beanClass = beanClass;

        this.serviceName             = serviceName;
        this.serverName              = serverName;
        this.invalidParameterHandler = invalidParameterHandler;
        this.repositoryHandler       = repositoryHandler;
        this.repositoryHelper        = repositoryHelper;
        this.localServerUserId       = localServerUserId;

        if (securityVerifier != null)
        {
                this.securityVerifier = securityVerifier;
        }

        this.supportedZones = supportedZones;
        this.defaultZones   = defaultZones;
        this.publishZones   = publishZones;

        this.auditLog = auditLog;

        this.errorHandler = new RepositoryErrorHandler(repositoryHelper, serviceName, serverName, auditLog);

        this.qualifiedNamePropertyNamesList = new ArrayList<>();
        this.qualifiedNamePropertyNamesList.add(OpenMetadataProperty.QUALIFIED_NAME.name);
    }




    /**
     * Return this handler's converter.
     *
     * @return converter
     */
    public OpenMetadataAPIGenericConverter<B> getConverter()
    {
        return converter;
    }


    /**
     * Set up a new security verifier (the handler runs with a default verifier until this method is called).
     * <br><br>
     * The security verifier provides authorization checks for access and maintenance
     * changes to open metadata.  Authorization checks are enabled through the
     * OpenMetadataServerSecurityConnector.
     *
     * @param securityVerifier new security verifier
     */
    public void setSecurityVerifier(OpenMetadataServerSecurityVerifier securityVerifier)
    {
        if (securityVerifier != null)
        {
                this.securityVerifier = securityVerifier;
        }
    }


    /**
     * Return the repository helper for this server.
     *
     * @return repository helper object
     */
    public OMRSRepositoryHelper getRepositoryHelper()
    {
        return repositoryHelper;
    }


    /**
     * Return the repository handler for this server.
     *
     * @return repository handler object
     */
    public RepositoryHandler getRepositoryHandler()
    {
        return repositoryHandler;
    }


    /**
     * Return the list of zones to use for retrieving assets.
     *
     * @return list of zone names
     */
    public List<String> getSupportedZones()
    {
        return supportedZones;
    }


    /**
     * Return the type definition for the named type.
     *
     * @param suppliedTypeName caller's subtype (or null)
     * @param defaultTypeName common super type
     *
     * @return type definition
     */
    public TypeDef getTypeDefByName(String suppliedTypeName,
                                    String defaultTypeName)
    {
        String resultsTypeName = defaultTypeName;

        if (suppliedTypeName != null)
        {
                resultsTypeName = suppliedTypeName;
        }

        return repositoryHelper.getTypeDefByName(serviceName, resultsTypeName);
    }


    /**
     * Return the name of this service.
     *
     * @return string name
     */
    public String getServiceName()
    {
        return serviceName;
    }

    /**
     * Return the name of this server.
     *
     * @return string name
     */
    public String getServerName()
    {
        return serverName;
    }
}
