/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.subjectarea.initialization;

import org.odpi.openmetadata.accessservices.subjectarea.SubjectAreaGlossary;
import org.odpi.openmetadata.accessservices.subjectarea.SubjectAreaProject;
import org.odpi.openmetadata.accessservices.subjectarea.SubjectAreaTerm;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.MetadataServerUncontactableException;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.PropertyServerException;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.UserNotAuthorizedException;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstanceHandler;
import org.odpi.openmetadata.viewservices.subjectarea.admin.serviceinstances.SubjectAreaViewServicesInstance;
import org.odpi.openmetadata.viewservices.subjectarea.handlers.GlossaryHandler;
import org.odpi.openmetadata.viewservices.subjectarea.handlers.ProjectHandler;
import org.odpi.openmetadata.viewservices.subjectarea.handlers.TermHandler;

/**
 * SubjectAreaViewInstanceHandler retrieves information from the instance map for the
 * access service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the SubjectAreaViewAdmin class.
 */
public class SubjectAreaViewInstanceHandler extends OMVSServiceInstanceHandler
{

    private static SubjectAreaViewServicesInstanceMap instanceMap   = new SubjectAreaViewServicesInstanceMap();
   // private static ViewServiceDescription   myDescription = ViewServiceDescription.SUBJECT_AREA;

    /**
     * Default constructor registers the view service
     */
    public SubjectAreaViewInstanceHandler() {
        super(ViewServiceDescription.SUBJECT_AREA.getViewServiceName());
        SubjectAreaViewRegistration.registerViewService();
    }
    /**
     * Return the Subject Area's official Access Service Name
     *
     * @param serverName  name of the server that the request is for
     * @return String the service name
     * @throws MetadataServerUncontactableException no available instance for the requested server
     */
    public String  getViewServiceName(String serverName) throws MetadataServerUncontactableException {
        org.odpi.openmetadata.viewservices.subjectarea.services.SubjectAreaViewServicesInstance instance = instanceMap.getInstance(serverName);

        if (instance != null) {
            return instance.getServiceName();
        } else {
            final String methodName = "getViewServiceName";
            //TODO create a view specific error
            SubjectAreaErrorCode errorCode    = SubjectAreaErrorCode.SERVICE_NOT_INITIALIZED;
            String                    errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverName, methodName);

            throw new MetadataServerUncontactableException(errorCode.getHTTPErrorCode(),
                                                           this.getClass().getName(),
                                                           methodName,
                                                           errorMessage,
                                                           errorCode.getSystemAction(),
                                                           errorCode.getUserAction());
        }
    }

    public GlossaryHandler getGlossaryHandler(String serverName,String userId,String serviceOperationName) throws org.odpi.openmetadata.commonservices.ffdc.exceptions.UserNotAuthorizedException, org.odpi.openmetadata.commonservices.ffdc.exceptions.PropertyServerException, org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException, MetadataServerUncontactableException {

        SubjectAreaGlossary subjectAreaGlossary = this.getSubjectAreaGlossary(serverName,userId,serviceOperationName);
        return new GlossaryHandler(subjectAreaGlossary);
    }

    public ProjectHandler getProjectHandler(String serverName,String userId,String serviceOperationName) throws org.odpi.openmetadata.commonservices.ffdc.exceptions.UserNotAuthorizedException, org.odpi.openmetadata.commonservices.ffdc.exceptions.PropertyServerException, org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException, MetadataServerUncontactableException {

        SubjectAreaProject subjectAreaProject = this.getSubjectAreaProject(serverName,userId,serviceOperationName);
        return new ProjectHandler(subjectAreaProject);
    }

    public TermHandler getTermHandler(String serverName, String userId, String serviceOperationName) throws org.odpi.openmetadata.commonservices.ffdc.exceptions.UserNotAuthorizedException, org.odpi.openmetadata.commonservices.ffdc.exceptions.PropertyServerException, org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException, MetadataServerUncontactableException {

        SubjectAreaTerm subjectAreaTerm = this.getSubjectAreaTerm(serverName,userId,serviceOperationName);
        return new TermHandler(subjectAreaTerm);
    }

    /**
     * This method returns the object for the tenant to use to work with the
     * subject area glossary API
     * @param serverName  name of the server that the request is for
     * @param userId local server userid
     * @param serviceOperationName service operation - usually the top level rest call
     * @return SubjectAreaGlossary subject area glossary API object
     * @throws MetadataServerUncontactableException Metadata server uncontactable
     */
    private SubjectAreaGlossary getSubjectAreaGlossary(String serverName, String userId, String serviceOperationName)
            throws
            org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException, org.odpi.openmetadata.commonservices.ffdc.exceptions.PropertyServerException, org.odpi.openmetadata.commonservices.ffdc.exceptions.UserNotAuthorizedException {
        SubjectAreaViewServicesInstance instance  = getSubjectAreaViewServicesInstance(userId,serverName,serviceOperationName);
        return instance.getSubjectAreaGlossary();
    }

    /**
     * This method returns the object for the tenant to use to work with the
     * subject area term API
     * @param serverName  name of the server that the request is for
     * @param userId local server userid
     * @param serviceOperationName service operation - usually the top level rest call
     * @return SubjectAreaGlossary subject area glossary API object
     */
    private SubjectAreaTerm getSubjectAreaTerm(String serverName, String userId, String serviceOperationName)
            throws
            org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException, org.odpi.openmetadata.commonservices.ffdc.exceptions.PropertyServerException, org.odpi.openmetadata.commonservices.ffdc.exceptions.UserNotAuthorizedException {
        SubjectAreaViewServicesInstance instance  = getSubjectAreaViewServicesInstance(userId,serverName,serviceOperationName);
        return instance.getSubjectAreaTerm();
    }
    /**
     * This method returns the object for the tenant to use to work with the
     * subject area project API
     * @param serverName  name of the server that the request is for
     * @param userId local server userid
     * @param serviceOperationName service operation - usually the top level rest call
     * @return SubjectAreaGlossary subject area glossary API object
     */
    private SubjectAreaProject getSubjectAreaProject(String serverName, String userId, String serviceOperationName)
            throws
            org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException, org.odpi.openmetadata.commonservices.ffdc.exceptions.PropertyServerException, org.odpi.openmetadata.commonservices.ffdc.exceptions.UserNotAuthorizedException {
        SubjectAreaViewServicesInstance instance  = getSubjectAreaViewServicesInstance(userId,serverName,serviceOperationName);
        return instance.getSubjectAreaProject();
    }

    /**
     * Get the subject area services instance. This is an instance associated with the UI servername (tenant).
     * @param userId local server userid
     * @param serverName name of the server that the request is for
     * @param serviceOperationName service operation - usually the top level rest call
     * @return SubjectAreaViewServicesInstance instance for this tenant to use.
     * @throws InvalidParameterException
     * @throws PropertyServerException
     * @throws UserNotAuthorizedException
     * @throws MetadataServerUncontactableException
     */
    private SubjectAreaViewServicesInstance getSubjectAreaViewServicesInstance(String userId, String serverName, String serviceOperationName)
            throws org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException, org.odpi.openmetadata.commonservices.ffdc.exceptions.PropertyServerException, org.odpi.openmetadata.commonservices.ffdc.exceptions.UserNotAuthorizedException {
        return (SubjectAreaViewServicesInstance)
                    super.getServerServiceInstance(userId,
                            serverName,
                            serviceOperationName);

    }

}
