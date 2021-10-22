package org.odpi.openmetadata.accessservices.glossaryauthor.fvt.client.glossarys;

import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.Category;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.Config;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.FindRequest;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.glossary.Glossary;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Relationship;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.List;

public interface GlossaryAuthorViewGlossary {

    /**
     * Get the config.
     * <p>
     * The result is the configuration
     *
     * @param userId       userId under which the request is performed
     *
     * @return The configuration
     *
     * @throws PropertyServerException something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     */

    Glossary create(String userId, Glossary glossary) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException;

    Glossary update(String userId, String guid, Glossary glossary, boolean action) throws PropertyServerException, InvalidParameterException, UserNotAuthorizedException;

    Glossary getByGUID(String userId, String guid) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException;

    void delete(String userId, String guid) throws PropertyServerException;

    Glossary restore(String userId, String guid) throws PropertyServerException, InvalidParameterException, UserNotAuthorizedException;

    List<Relationship> getAllRelationships(String userId, String guid) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException;

    List<Category> getCategories(String userId, String glossaryGuid, FindRequest findRequest, boolean onlyTop) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException;

    List<Term> getTerms(String userId, String glossaryGuid, FindRequest findRequest) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException;
}
