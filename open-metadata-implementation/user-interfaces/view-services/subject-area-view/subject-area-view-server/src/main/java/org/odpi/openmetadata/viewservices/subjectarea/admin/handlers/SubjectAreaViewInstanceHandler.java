/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.subjectarea.admin.handlers;

import org.odpi.openmetadata.accessservices.subjectarea.*;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.SequencingOrder;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Line;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.project.Project;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstanceHandler;
import org.odpi.openmetadata.userinterface.adminservices.configuration.registration.ViewServiceDescription;

import org.odpi.openmetadata.userinterface.common.ffdc.UserInterfaceErrorCode;
import org.odpi.openmetadata.viewservices.subjectarea.admin.registration.SubjectAreaViewRegistration;
import org.odpi.openmetadata.viewservices.subjectarea.admin.serviceinstances.SubjectAreaViewServicesInstance;

import java.util.Date;
import java.util.List;

/**
 * SubjectAreaViewInstanceHandler retrieves information from the instance map for the
 * access service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the SubjectAreaViewAdmin class.
 */
public class SubjectAreaViewInstanceHandler extends OMVSServiceInstanceHandler
{
    /**
     * Default constructor registers the view service
     */
    public SubjectAreaViewInstanceHandler() {
        super(ViewServiceDescription.SUBJECT_AREA.getViewServiceName());

        SubjectAreaViewRegistration.registerViewService();
    }
    /**
     * Return the Subject Area view's official view Service Name
     *
     * @param serverName  name of the server that the request is for
     * @param userId local server userid
     * @param serviceOperationName service operation - usually the top level rest call
     * @return String the service name
     * @throws MetadataServerUncontactableException no available instance for the requested server
     */
    private String  getViewServiceName(String serverName, String userId, String serviceOperationName) throws MetadataServerUncontactableException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        SubjectAreaViewServicesInstance instance = getSubjectAreaViewServicesInstance(userId,serverName,serviceOperationName);
        return instance.getViewServiceName();
    }

    /**
     * This serverName has an associated metadata server. This call returns that metadata servers's name.
     * @param serverName  name of the server that the request is for
     * @param userId local server userid
     * @param serviceOperationName service operation - usually the top level rest call
     * @return String Metadata server name
     * @throws MetadataServerUncontactableException Metadata server uncontactable
     *
     */
   private String getMetadataServerName(String serverName, String userId, String serviceOperationName) throws MetadataServerUncontactableException,
            InvalidParameterException,
            PropertyServerException,
            UserNotAuthorizedException {
        SubjectAreaViewServicesInstance instance = getSubjectAreaViewServicesInstance(userId,serverName,serviceOperationName);
        return instance.getMetadataServerName();
    }
    /**
     * This serverName has an associated metadata server. This call returns that metadata servers's URL.
     * @param serverName  name of the server that the request is for
     * @param userId local server userid
     * @param serviceOperationName service operation - usually the top level rest call
     * @return String Metadata server URL
     * @throws MetadataServerUncontactableException Metadata server uncontactable
     */
    private String getMetadataServerURL(String serverName, String userId, String serviceOperationName )
            throws MetadataServerUncontactableException,
            InvalidParameterException,
            PropertyServerException,
            UserNotAuthorizedException {
        SubjectAreaViewServicesInstance instance = getSubjectAreaViewServicesInstance(userId,serverName,serviceOperationName);
        return instance.getMetadataServerURL();
    }

    /**
     * This method returns the object for the tenant to use to work with the
     * subject area Term API
     * @param serverName  name of the server that the request is for
     * @param userId local server userid
     * @param serviceOperationName service operation - usually the top level rest call
     * @return SubjectAreaTerm subject area Term API object
     * @throws MetadataServerUncontactableException Metadata server uncontactable
     */
    private SubjectAreaTerm getSubjectAreaTerm(String serverName, String userId, String serviceOperationName) throws MetadataServerUncontactableException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        SubjectAreaViewServicesInstance instance = getSubjectAreaViewServicesInstance(userId,serverName,serviceOperationName);
        return instance.getSubjectAreaTerm();
    }
    /**
     * This method returns the object for the tenant to use to work with the
     * subject area Project API
     * @param serverName  name of the server that the request is for
     * @param userId local server userid
     * @param serviceOperationName service operation - usually the top level rest call
     * @return SubjectAreaTerm subject area Project API object
     * @throws MetadataServerUncontactableException Metadata server uncontactable
     */
   private SubjectAreaProject getSubjectAreaProject(String serverName, String userId, String serviceOperationName) throws MetadataServerUncontactableException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        SubjectAreaViewServicesInstance instance = getSubjectAreaViewServicesInstance(userId,serverName,serviceOperationName);
        return instance.getSubjectAreaProject();
    }

    /**
     * This method returns the object for the tenant to use to work with the
     * subject area Category API
     * @param serverName  name of the server that the request is for
     * @param userId local server userid
     * @param serviceOperationName service operation - usually the top level rest call
     * @return SubjectAreaTerm subject area Category API object
     * @throws MetadataServerUncontactableException Metadata server uncontactable
     */
    private SubjectAreaCategory getSubjectAreaCategory(String serverName, String userId, String serviceOperationName) throws MetadataServerUncontactableException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        SubjectAreaViewServicesInstance instance = getSubjectAreaViewServicesInstance(userId,serverName,serviceOperationName);
        return instance.getSubjectAreaCategory();
    }

    /**
     * This method returns the object for the tenant to use to work with the
     * subject area Graph API
     * @param serverName  name of the server that the request is for
     * @param userId local server userid
     * @param serviceOperationName service operation - usually the top level rest call
     * @return SubjectAreaTerm subject area Graph API object
     * @throws MetadataServerUncontactableException Metadata server uncontactable
     */
    private SubjectAreaGraph getSubjectAreaGraph(String serverName, String userId, String serviceOperationName) throws MetadataServerUncontactableException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        SubjectAreaViewServicesInstance instance = getSubjectAreaViewServicesInstance(userId,serverName,serviceOperationName);
        return instance.getSubjectAreaGraph();
    }

    /**
     * This method returns the object for the tenant to use to work with the
     * subject area Relationship API
     * @param serverName  name of the server that the request is for
     * @param userId local server userid
     * @param serviceOperationName service operation - usually the top level rest call
     * @return SubjectAreaTerm subject area Relationship API object
     * @throws MetadataServerUncontactableException Metadata server uncontactable
     */
    private SubjectAreaRelationship getSubjectAreaRelationship(String serverName, String userId, String serviceOperationName) throws MetadataServerUncontactableException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        SubjectAreaViewServicesInstance instance = getSubjectAreaViewServicesInstance(userId,serverName,serviceOperationName);
        return instance.getSubjectAreaRelationship();
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
            throws MetadataServerUncontactableException,
            InvalidParameterException,
            PropertyServerException,
            UserNotAuthorizedException {
        SubjectAreaViewServicesInstance instance = getSubjectAreaViewServicesInstance(userId,serverName,serviceOperationName);
        return instance.getSubjectAreaGlossary();
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
            throws InvalidParameterException,
            PropertyServerException,
            UserNotAuthorizedException,
            MetadataServerUncontactableException {
        SubjectAreaViewServicesInstance instance = null;
        try {
            instance = (SubjectAreaViewServicesInstance)
                    super.getServerServiceInstance(userId,
                            serverName,
                            serviceOperationName);
        } catch (org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException e) {
            String badParmName = e.getParameterName();
            if ("serverName".equals(badParmName)) {
                UserInterfaceErrorCode errorCode = UserInterfaceErrorCode.INVALID_SERVERNAME;
                String errorMessage = errorCode.getErrorMessageId()
                        + errorCode.getFormattedErrorMessage(serverName);
                throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                        "SubjectAreaViewInstanceHandler",
                        serviceOperationName,
                        errorMessage,
                        errorCode.getSystemAction(),
                        errorCode.getUserAction());
            } else {
                UserInterfaceErrorCode errorCode = UserInterfaceErrorCode.INVALID_PARAMETER;
                String errorMessage = errorCode.getErrorMessageId()
                        + errorCode.getFormattedErrorMessage();
                throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                        "SubjectAreaViewInstanceHandler",
                        serviceOperationName,
                        errorMessage,
                        errorCode.getSystemAction(),
                        errorCode.getUserAction());

            }

        } catch (org.odpi.openmetadata.commonservices.ffdc.exceptions.UserNotAuthorizedException e) {
            UserInterfaceErrorCode errorCode = UserInterfaceErrorCode.USER_NOT_AUTHORIZED;
            String errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage();
            throw new UserNotAuthorizedException(errorCode.getHTTPErrorCode(),
                    "SubjectAreaViewInstanceHandler",
                    serviceOperationName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction(),
                    userId);
        } catch (org.odpi.openmetadata.commonservices.ffdc.exceptions.PropertyServerException e) {
            UserInterfaceErrorCode errorCode = UserInterfaceErrorCode.SERVICE_NOT_AVAILABLE;
            String errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage(serviceName);
            throw new PropertyServerException(errorCode.getHTTPErrorCode(),
                    "SubjectAreaViewInstanceHandler",
                    serviceOperationName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }

        if (instance == null) {

            final String methodName = "getSubjectAreaViewServicesInstance";

            UserInterfaceErrorCode errorCode = UserInterfaceErrorCode.SERVICE_NOT_INITIALIZED;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverName, ViewServiceDescription.SUBJECT_AREA.getViewServiceName(), methodName);

            throw new MetadataServerUncontactableException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }
        return instance;
    }


    public Glossary createGlossary(String serverName, String userId, Glossary suppliedGlossary) throws MetadataServerUncontactableException, UnrecognizedGUIDException, ClassificationException, FunctionNotSupportedException, UnexpectedResponseException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        SubjectAreaGlossary subjectAreaGlossary = this.getSubjectAreaGlossary(serverName, userId, "createGlossary");
        return subjectAreaGlossary.createGlossary(userId, suppliedGlossary);
    }


    public Glossary getGlossaryByGuid(String serverName, String userId, String guid) throws MetadataServerUncontactableException, UnrecognizedGUIDException, FunctionNotSupportedException, UnexpectedResponseException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        SubjectAreaGlossary subjectAreaGlossary = this.getSubjectAreaGlossary(serverName, userId, "getGlossaryByGuid");
        return subjectAreaGlossary.getGlossaryByGuid(userId, guid);
    }


    public List<Line> getGlossaryRelationships(String serverName, String userId, String guid, Date asOfTime, int offset, int pageSize, SequencingOrder sequencingOrder, String sequencingProperty) throws FunctionNotSupportedException, UnexpectedResponseException, MetadataServerUncontactableException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        SubjectAreaGlossary subjectAreaGlossary = this.getSubjectAreaGlossary(serverName, userId, "getGlossaryRelationships");
        return subjectAreaGlossary.getGlossaryRelationships(userId, guid, asOfTime, offset, pageSize, sequencingOrder, sequencingProperty);
    }


    public List<Glossary> findGlossary(String serverName, String userId, String searchCriteria, Date asOfTime, int offset, int pageSize, SequencingOrder sequencingOrder, String sequencingProperty) throws MetadataServerUncontactableException, FunctionNotSupportedException, UnexpectedResponseException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        SubjectAreaGlossary subjectAreaGlossary = this.getSubjectAreaGlossary(serverName, userId, "findGlossary");
        return subjectAreaGlossary.findGlossary(userId, searchCriteria, asOfTime, offset, pageSize, sequencingOrder, sequencingProperty);
    }


    public Glossary replaceGlossary(String serverName, String userId, String guid, Glossary suppliedGlossary) throws UnexpectedResponseException, FunctionNotSupportedException, MetadataServerUncontactableException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        SubjectAreaGlossary subjectAreaGlossary = this.getSubjectAreaGlossary(serverName, userId, "replaceGlossary");
        return subjectAreaGlossary.replaceGlossary(userId, guid, suppliedGlossary);
    }


    public Glossary updateGlossary(String serverName, String userId, String guid, Glossary suppliedGlossary) throws UnexpectedResponseException, FunctionNotSupportedException, MetadataServerUncontactableException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        SubjectAreaGlossary subjectAreaGlossary = this.getSubjectAreaGlossary(serverName, userId, "updateGlossary");
        return subjectAreaGlossary.updateGlossary(userId, guid, suppliedGlossary);
    }


    public Glossary deleteGlossary(String serverName, String userId, String guid) throws  MetadataServerUncontactableException,UnrecognizedGUIDException, FunctionNotSupportedException, UnexpectedResponseException, EntityNotDeletedException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        SubjectAreaGlossary subjectAreaGlossary = this.getSubjectAreaGlossary(serverName, userId, "deleteGlossary");
        return subjectAreaGlossary.deleteGlossary(userId,guid);
    }


    public void purgeGlossary(String serverName, String userId, String guid) throws MetadataServerUncontactableException, UnrecognizedGUIDException, GUIDNotPurgedException, UnexpectedResponseException, FunctionNotSupportedException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        SubjectAreaGlossary subjectAreaGlossary = this.getSubjectAreaGlossary(serverName, userId, "purgeGlossary");
        subjectAreaGlossary.purgeGlossary(userId,guid);
    }


    public Glossary restoreGlossary(String serverName, String userId, String guid) throws MetadataServerUncontactableException, UnrecognizedGUIDException, FunctionNotSupportedException, UnexpectedResponseException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        SubjectAreaGlossary subjectAreaGlossary = this.getSubjectAreaGlossary(serverName, userId, "repstoreGlossary");
        return subjectAreaGlossary.restoreGlossary(userId, guid);
    }

    public Project createProject(String serverName, String userId, Project suppliedProject) throws MetadataServerUncontactableException, UnrecognizedGUIDException, ClassificationException, FunctionNotSupportedException, UnexpectedResponseException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {

        SubjectAreaProject subjectAreaProject = this.getSubjectAreaProject(serverName, userId, "createProject");
        return  subjectAreaProject.createProject(userId, suppliedProject);
    }


    public Project getProjectByGuid(String serverName, String userId, String guid) throws MetadataServerUncontactableException, UnrecognizedGUIDException, FunctionNotSupportedException, UnexpectedResponseException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        SubjectAreaProject subjectAreaProject = this.getSubjectAreaProject(serverName, userId, "getProjectByGuid");
        return subjectAreaProject.getProjectByGuid(userId, guid);
    }


    public List<Line> getProjectRelationships(String serverName, String userId, String guid, Date asOfTime, int offset, int pageSize, SequencingOrder sequencingOrder, String sequencingProperty) throws FunctionNotSupportedException, UnexpectedResponseException, MetadataServerUncontactableException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        SubjectAreaProject subjectAreaProject = this.getSubjectAreaProject(serverName, userId, "getProjectByGuid");
        return subjectAreaProject.getProjectRelationships(userId, guid, asOfTime, offset, pageSize, sequencingOrder, sequencingProperty);
    }


    public List<Term> getProjectTerms(String serverName, String userId, String guid, Date asOfTime) throws FunctionNotSupportedException, UnexpectedResponseException, MetadataServerUncontactableException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        SubjectAreaProject subjectAreaProject = this.getSubjectAreaProject(serverName, userId, "getProjectByGuid");
        return subjectAreaProject.getProjectTerms(userId,guid, asOfTime);
    }


    public List<Project> findProject(String serverName, String userId, String searchCriteria, Date asOfTime, int offset, int pageSize, SequencingOrder sequencingOrder, String sequencingProperty) throws MetadataServerUncontactableException, FunctionNotSupportedException, UnexpectedResponseException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        SubjectAreaProject subjectAreaProject = this.getSubjectAreaProject(serverName, userId, "findProject");
        return subjectAreaProject.findProject(userId, searchCriteria, asOfTime, offset, pageSize, sequencingOrder, sequencingProperty);
    }


    public Project replaceProject(String serverName, String userId, String guid, Project suppliedProject) throws UnexpectedResponseException, FunctionNotSupportedException, MetadataServerUncontactableException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        SubjectAreaProject subjectAreaProject = this.getSubjectAreaProject(serverName, userId, "replaceProject");
        return subjectAreaProject.replaceProject(userId,guid,suppliedProject);
    }


    public Project updateProject(String serverName, String userId, String guid, Project suppliedProject) throws UnexpectedResponseException, FunctionNotSupportedException, MetadataServerUncontactableException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        SubjectAreaProject subjectAreaProject = this.getSubjectAreaProject(serverName, userId, "updateProject");
        return subjectAreaProject.updateProject(userId, guid, suppliedProject);
    }


    public Project deleteProject(String serverName, String userId, String guid) throws  MetadataServerUncontactableException,UnrecognizedGUIDException, FunctionNotSupportedException, UnexpectedResponseException, EntityNotDeletedException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        SubjectAreaProject subjectAreaProject = this.getSubjectAreaProject(serverName, userId, "deleteProject");
        return subjectAreaProject.deleteProject(userId, guid);
    }


    public void purgeProject(String serverName, String userId, String guid) throws MetadataServerUncontactableException, UnrecognizedGUIDException, GUIDNotPurgedException, UnexpectedResponseException, FunctionNotSupportedException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        SubjectAreaProject subjectAreaProject = this.getSubjectAreaProject(serverName, userId, "purgeProject");
        subjectAreaProject.purgeProject(userId, guid);
    }


    public Project restoreProject(String serverName, String userId, String guid) throws MetadataServerUncontactableException, UnrecognizedGUIDException, FunctionNotSupportedException, UnexpectedResponseException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        SubjectAreaProject subjectAreaProject = this.getSubjectAreaProject(serverName, userId, "restoreProject");
        return subjectAreaProject.restoreProject(userId, guid);
    }


    public Term createTerm(String serverName, String userId, Term suppliedTerm) throws MetadataServerUncontactableException, ClassificationException, FunctionNotSupportedException, UnexpectedResponseException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        SubjectAreaTerm subjectAreaTerm = this.getSubjectAreaTerm(serverName, userId, "createTerm");
        return subjectAreaTerm.createTerm(userId,suppliedTerm);
    }


    public Term getTermByGuid(String serverName, String userId, String guid) throws MetadataServerUncontactableException, FunctionNotSupportedException, UnexpectedResponseException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        SubjectAreaTerm subjectAreaTerm = this.getSubjectAreaTerm(serverName, userId, "getTermByGuid");
        return subjectAreaTerm.getTermByGuid(userId, guid);
    }


    public List<Line> getTermRelationships(String serverName, String userId, String guid, Date asOfTime, int offset, int pageSize, SequencingOrder sequencingOrder, String sequencingProperty) throws MetadataServerUncontactableException, FunctionNotSupportedException, UnexpectedResponseException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        SubjectAreaTerm subjectAreaTerm = this.getSubjectAreaTerm(serverName, userId, "getTermRelationships");
        return subjectAreaTerm.getTermRelationships(userId, guid, asOfTime, offset, pageSize, sequencingOrder, sequencingProperty);
    }


    public Term replaceTerm(String serverName, String userId, String guid, Term suppliedTerm) throws UnexpectedResponseException,FunctionNotSupportedException,  MetadataServerUncontactableException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        SubjectAreaTerm subjectAreaTerm = this.getSubjectAreaTerm(serverName, userId, "replaceTerm");
        return subjectAreaTerm.replaceTerm(userId, guid, suppliedTerm);
    }


    public Term updateTerm(String serverName, String userId, String guid, Term suppliedTerm) throws UnexpectedResponseException,FunctionNotSupportedException,  MetadataServerUncontactableException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        SubjectAreaTerm subjectAreaTerm = this.getSubjectAreaTerm(serverName, userId, "updateTerm");
        return subjectAreaTerm.updateTerm(userId, guid, suppliedTerm);
    }


    public Term deleteTerm(String serverName, String userId, String guid) throws  MetadataServerUncontactableException,FunctionNotSupportedException, UnrecognizedGUIDException, UnexpectedResponseException, EntityNotDeletedException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        SubjectAreaTerm subjectAreaTerm = this.getSubjectAreaTerm(serverName, userId, "deleteTerm");
        return subjectAreaTerm.deleteTerm(userId, guid);
    }


    public void purgeTerm(String serverName, String userId, String guid) throws MetadataServerUncontactableException, GUIDNotPurgedException, FunctionNotSupportedException, UnrecognizedGUIDException, UnexpectedResponseException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        SubjectAreaTerm subjectAreaTerm = this.getSubjectAreaTerm(serverName, userId, "purgeTerm");
        subjectAreaTerm.purgeTerm(userId, guid);
    }


    public Term restoreTerm(String serverName, String userId, String guid) throws MetadataServerUncontactableException, UnrecognizedGUIDException, FunctionNotSupportedException, UnexpectedResponseException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        SubjectAreaTerm subjectAreaTerm = this.getSubjectAreaTerm(serverName, userId, "restoreTerm");
        return subjectAreaTerm.restoreTerm(userId, guid);
    }


    public List<Term> findTerm(String serverName, String userId, String searchCriteria, Date asOfTime, int offset, int pageSize, SequencingOrder sequencingOrder, String sequencingProperty) throws MetadataServerUncontactableException, FunctionNotSupportedException, UnexpectedResponseException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        SubjectAreaTerm subjectAreaTerm = this.getSubjectAreaTerm(serverName, userId, "findTerm");
        return subjectAreaTerm.findTerm(userId, searchCriteria, asOfTime, offset, pageSize, sequencingOrder, sequencingProperty);
    }

}
