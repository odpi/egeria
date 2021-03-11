/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.glossaryauthor.initialization;

import org.odpi.openmetadata.accessservices.subjectarea.client.configs.SubjectAreaConfigClient;
import org.odpi.openmetadata.accessservices.subjectarea.client.configs.SubjectAreaConfigClients;
import org.odpi.openmetadata.accessservices.subjectarea.client.nodes.SubjectAreaNodeClients;
import org.odpi.openmetadata.accessservices.subjectarea.client.relationships.SubjectAreaGraphClient;
import org.odpi.openmetadata.accessservices.subjectarea.client.relationships.SubjectAreaRelationshipClients;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.PropertyServerException;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstanceHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.viewservices.glossaryauthor.admin.serviceinstances.GlossaryAuthorViewServicesInstance;
import org.odpi.openmetadata.viewservices.glossaryauthor.handlers.RelationshipHandler;

/**
 * GlossaryAuthorViewInstanceHandler retrieves information from the instance map for the
 * access service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the GlossaryAuthorViewAdmin class.
 */
public class GlossaryAuthorViewInstanceHandler extends OMVSServiceInstanceHandler {

    private static GlossaryAuthorViewServicesInstanceMap instanceMap = new GlossaryAuthorViewServicesInstanceMap();

    /**
     * Default constructor registers the view service
     */
    public GlossaryAuthorViewInstanceHandler() {
        super(ViewServiceDescription.GLOSSARY_AUTHOR.getViewServiceName());
        GlossaryAuthorViewRegistration.registerViewService();
    }

    public RelationshipHandler getRelationshipHandler(String serverName, String userId, String serviceOperationName) throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException {

        SubjectAreaRelationshipClients subjectAreaRelationship = this.getSubjectAreaRelationship(serverName, userId, serviceOperationName);
        return new RelationshipHandler(subjectAreaRelationship);
    }

    /**
     * This method returns the object for the tenant to use to work with the
     * subject area nodes API
     *
     * @param serverName           name of the server that the request is for
     * @param userId               local server userid
     * @param serviceOperationName service operation - usually the top level rest call
     * @return SubjectAreaNodeClients subject area nodes API objects
     */
    public SubjectAreaNodeClients getSubjectAreaNodeClients(String serverName, String userId, String serviceOperationName)
    throws
    InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        GlossaryAuthorViewServicesInstance instance = getSubjectAreaViewServicesInstance(userId, serverName, serviceOperationName);
        return instance.getNodeClients();
    }

    /**
     * This method returns the object for the tenant to use to work with the
     * subject area relationship API
     *
     * @param serverName           name of the server that the request is for
     * @param userId               local server userid
     * @param serviceOperationName service operation - usually the top level rest call
     * @return SubjectAreaRelationship subject area glossary API object
     */
    private SubjectAreaRelationshipClients getSubjectAreaRelationship(String serverName, String userId, String serviceOperationName)
    throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        GlossaryAuthorViewServicesInstance instance = getSubjectAreaViewServicesInstance(userId, serverName, serviceOperationName);
        return instance.getSubjectAreaRelationshipClients();
    }

    /**
     * Get the subject area services instance. This is an instance associated with the UI servername (tenant).
     *
     * @param userId               local server userid
     * @param serverName           name of the server that the request is for
     * @param serviceOperationName service operation - usually the top level rest call
     * @return SubjectAreaViewServicesInstance instance for this tenant to use.
     */
    private GlossaryAuthorViewServicesInstance getSubjectAreaViewServicesInstance(String userId, String serverName, String serviceOperationName)
    throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        return (GlossaryAuthorViewServicesInstance)
                super.getServerServiceInstance(userId, serverName, serviceOperationName);
    }

    /**
     * This method returns the object for the tenant to use to work with the
     * subject area config API
     *
     * @param serverName           name of the server that the request is for
     * @param userId               local server userid
     * @param serviceOperationName service operation - usually the top level rest call
     * @return SubjectAreaNodeClients subject area config API objects
     */
    public SubjectAreaConfigClient getSubjectAreaConfigClient(String serverName, String userId, String serviceOperationName)
    throws
    InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        GlossaryAuthorViewServicesInstance instance = getSubjectAreaViewServicesInstance(userId, serverName, serviceOperationName);
        return instance.getSubjectAreaConfigClient();
    }


    /**
     * This method returns the object for the tenant to use to work with the
     * subject area graph API
     *
     * @param serverName           name of the server that the request is for
     * @param userId               local server userid
     * @param serviceOperationName service operation - usually the top level rest call
     * @return SubjectAreaNodeClients subject area graph API objects
     */
    public SubjectAreaGraphClient getSubjectAreaGraphClient(String serverName, String userId, String serviceOperationName)
    throws
    InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        GlossaryAuthorViewServicesInstance instance = getSubjectAreaViewServicesInstance(userId, serverName, serviceOperationName);
        return instance.getSubjectAreaGraphClient();
    }

    public int getGlossaryViewMaxPageSize(String serverName, String userId, String serviceOperationName)
    throws
    InvalidParameterException, PropertyServerException, UserNotAuthorizedException
    {
        GlossaryAuthorViewServicesInstance instance = getSubjectAreaViewServicesInstance(userId, serverName, serviceOperationName);
        return instance.getGlossaryViewMaxPageSize();
    }

}