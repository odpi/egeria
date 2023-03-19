/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.glossaryview.client;

import org.odpi.openmetadata.accessservices.glossaryview.exception.GlossaryViewOmasException;
import org.odpi.openmetadata.accessservices.glossaryview.rest.ExternalGlossaryLink;
import org.odpi.openmetadata.accessservices.glossaryview.rest.Glossary;
import org.odpi.openmetadata.accessservices.glossaryview.rest.GlossaryCategory;
import org.odpi.openmetadata.accessservices.glossaryview.rest.GlossaryTerm;
import org.odpi.openmetadata.accessservices.glossaryview.rest.GlossaryViewEntityDetail;
import org.odpi.openmetadata.accessservices.glossaryview.rest.GlossaryViewEntityDetailResponse;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The Glossary View Open Metadata Access Service (OMAS) provides an interface to query for glossaries, categories and terms.
 * Regarding the paged requests, one can pass null to 'from' and 'size' params in order to use their default values
 */
public class GlossaryViewClient extends OmasClient {

    private static final String PATH_ROOT = "/servers/{0}/open-metadata/access-services/glossary-view/users/{1}";
    private static final String PARAM_DELIMITER = "?";
    private static final String PAGINATION = "from={3}&size={4}";

    private static final String GET_GLOSSARY = PATH_ROOT + "/glossaries/{2}";

    private static final String GET_GLOSSARIES = PATH_ROOT + "/glossaries" + PARAM_DELIMITER + "from={2}&size={3}";
    private static final String GET_TERM_HOME_GLOSSARY = PATH_ROOT + "/terms/{2}/home-glossary";
    private static final String GET_CATEGORY_HOME_GLOSSARY = PATH_ROOT + "/categories/{2}/home-glossary";
    private static final String GET_GLOSSARY_EXTERNAL_GLOSSARY_LINKS = PATH_ROOT + "/glossaries/{2}/external-glossary-links" + PARAM_DELIMITER + PAGINATION;

    private static final String GET_CATEGORIES = PATH_ROOT + "/categories";
    private static final String GET_CATEGORY = PATH_ROOT + "/categories/{2}";
    private static final String GET_CATEGORIES_OF_GLOSSARY = PATH_ROOT + "/glossaries/{2}/categories";
    private static final String GET_SUBCATEGORIES = PATH_ROOT + "/categories/{2}/subcategories";
    private static final String GET_CATEGORY_EXTERNAL_GLOSSARY_LINKS = PATH_ROOT + "/categories/{2}/external-glossary-links" + PARAM_DELIMITER + PAGINATION;

    private static final String GET_TERMS = PATH_ROOT + "/terms" + PARAM_DELIMITER + "from={2}&size={3}";
    private static final String GET_TERM = PATH_ROOT + "/terms/{2}";
    private static final String GET_TERMS_OF_GLOSSARY = PATH_ROOT + "/glossaries/{2}/terms";
    private static final String GET_TERMS_OF_CATEGORY = PATH_ROOT + "/categories/{2}/terms";
    private static final String GET_TERM_EXTERNAL_GLOSSARY_LINKS = PATH_ROOT + "/terms/{2}/external-glossary-links" + PARAM_DELIMITER + PAGINATION;
    private static final String GET_RELATED_TERMS = PATH_ROOT + "/terms/{2}/see-also" + PARAM_DELIMITER + PAGINATION;
    private static final String GET_SYNONYMS = PATH_ROOT + "/terms/{2}/synonyms" + PARAM_DELIMITER + PAGINATION;
    private static final String GET_ANTONYMS = PATH_ROOT + "/terms/{2}/antonyms" + PARAM_DELIMITER + PAGINATION;
    private static final String GET_PREFERRED_TERMS = PATH_ROOT + "/terms/{2}/preferred-terms" + PARAM_DELIMITER + PAGINATION;
    private static final String GET_REPLACEMENT_TERMS = PATH_ROOT + "/terms/{2}/replacement-terms" + PARAM_DELIMITER + PAGINATION;
    private static final String GET_TRANSLATIONS = PATH_ROOT + "/terms/{2}/translations-terms" + PARAM_DELIMITER + PAGINATION;
    private static final String GET_IS_A = PATH_ROOT + "/terms/{2}/is-a" + PARAM_DELIMITER + PAGINATION;
    private static final String GET_VALID_VALUES = PATH_ROOT + "/terms/{2}/valid-values" + PARAM_DELIMITER + PAGINATION;
    private static final String GET_USED_IN_CONTEXTS = PATH_ROOT + "/terms/{2}/used-in-contexts" + PARAM_DELIMITER + PAGINATION;
    private static final String GET_ASSIGNED_ELEMENTS = PATH_ROOT + "/terms/{2}/assigned-elements" + PARAM_DELIMITER + PAGINATION;
    private static final String GET_ATTRIBUTES = PATH_ROOT + "/terms/{2}/attributes" + PARAM_DELIMITER + PAGINATION;
    private static final String GET_SUBTYPES = PATH_ROOT + "/terms/{2}/subtypes" + PARAM_DELIMITER + PAGINATION;
    private static final String GET_TYPES = PATH_ROOT + "/terms/{2}/types" + PARAM_DELIMITER + PAGINATION;

    /**
     * Create a new client
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST services
     *
     * @throws InvalidParameterException null URL or server name
     */
    public GlossaryViewClient(String serverName, String serverPlatformRootURL) throws
            org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException {
        super(serverName, serverPlatformRootURL);
    }

    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST services
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     *
     * @throws InvalidParameterException null URL or server name
     */
    public GlossaryViewClient(String serverName, String serverPlatformRootURL, String userId, String password) throws
            org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException {
        super(serverName, serverPlatformRootURL, userId, password);
    }

    /**
     * Extract all glossary definitions
     *
     * @param userId calling user
     * @param from starting index
     * @param size max number of returned entities
     *
     * @return EntityDetailResponse glossaries
     *
     * @throws PropertyServerException if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     * @throws GlossaryViewOmasException if a problem occurs on the omas backend
     */
    public List<Glossary> getAllGlossaries(String userId, Integer from, Integer size)
            throws PropertyServerException, InvalidParameterException, GlossaryViewOmasException {

        GlossaryViewEntityDetailResponse response = getMultipleEntitiesPagedResponse("getAllGlossaries",
                GET_GLOSSARIES, serverName, userId, from, size);

        return castOmasResult(response.getResult());
    }

    /**
     * Extract all glossaryTerms definitions
     *
     * @param userId calling user
     * @param from starting index
     * @param size max number of returned entities
     *
     * @return EntityDetailResponse glossaries
     *
     * @throws PropertyServerException if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     * @throws GlossaryViewOmasException if a problem occurs on the omas backend
     */
    public List<GlossaryTerm> getAllGlossaryTerms(String userId, Integer from, Integer size)
            throws PropertyServerException, InvalidParameterException, GlossaryViewOmasException {

        GlossaryViewEntityDetailResponse response = getMultipleEntitiesPagedResponse("getAllGlossaryTerms",
                GET_TERMS, serverName, userId, from, size);

        return castOmasResult(response.getResult());
    }

    /**
     * Extract all categories definitions
     *
     * @param userId calling user
     * @param from starting index
     * @param size max number of returned entities
     *
     * @return EntityDetailResponse glossaries
     *
     * @throws PropertyServerException if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     * @throws GlossaryViewOmasException if a problem occurs on the omas backend
     */
    public List<GlossaryCategory> getAllCategories(String userId, Integer from, Integer size)
            throws PropertyServerException, InvalidParameterException, GlossaryViewOmasException {

        GlossaryViewEntityDetailResponse response = getMultipleEntitiesPagedResponse("getAllCategories",
                GET_CATEGORIES, serverName, userId, from, size);

        return castOmasResult(response.getResult());
    }

    /**
     * Extract a glossary definition
     *
     * @param userId calling user
     * @param glossaryGUID glossary GUID
     *
     * @return EntityDetailResponse glossary
     *
     * @throws PropertyServerException if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     * @throws GlossaryViewOmasException if a problem occurs on the omas backend
     */
    public Glossary getGlossary(String userId, String glossaryGUID)
            throws PropertyServerException, InvalidParameterException, GlossaryViewOmasException {

        GlossaryViewEntityDetailResponse response =  getSingleEntityResponse("getGlossary", GET_GLOSSARY,
                serverName, userId, glossaryGUID);

        return firstEntity(castOmasResult(response.getResult()));
    }

    /**
     * Extract a term's home glossary
     *
     * @param userId calling user
     * @param termGUID term GUID
     *
     * @return EntityDetailResponse glossary
     *
     * @throws PropertyServerException if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     * @throws GlossaryViewOmasException if a problem occurs on the omas backend
     */
    public Glossary getTermHomeGlossary(String userId, String termGUID)
            throws PropertyServerException, InvalidParameterException, GlossaryViewOmasException {

        GlossaryViewEntityDetailResponse response =  getSingleEntityResponse("getTermHomeGlossary",
                GET_TERM_HOME_GLOSSARY, serverName, userId, termGUID);

        return firstEntity(castOmasResult(response.getResult()));
    }

    /**
     * Extract a category's home glossary
     *
     * @param userId calling user
     * @param categoryGUID category GUID
     *
     * @return EntityDetailResponse glossary
     *
     * @throws PropertyServerException if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     * @throws GlossaryViewOmasException if a problem occurs on the omas backend
     */
    public Glossary getCategoryHomeGlossary(String userId, String categoryGUID)
            throws PropertyServerException, InvalidParameterException, GlossaryViewOmasException {

        GlossaryViewEntityDetailResponse response = getSingleEntityResponse("getCategoryHomeGlossary",
                GET_CATEGORY_HOME_GLOSSARY, serverName, userId, categoryGUID);


        return firstEntity(castOmasResult(response.getResult()));
    }

    /**
     * Extract a glossaries's external glossary links
     *
     * @param userId calling user
     * @param glossaryGUID glossary GUID
     * @param from starting index
     * @param size max number of returned entities
     *
     * @return EntityDetailResponse external glossary links
     *
     * @throws PropertyServerException if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     * @throws GlossaryViewOmasException if a problem occurs on the omas backend
     */
    public List<ExternalGlossaryLink> getExternalGlossaryLinksOfGlossary(String userId, String glossaryGUID, Integer from, Integer size)
            throws PropertyServerException, InvalidParameterException, GlossaryViewOmasException {

        GlossaryViewEntityDetailResponse response =  getMultipleRelatedEntitiesPagedResponse("getExternalGlossaryLinksOfGlossary",
                GET_GLOSSARY_EXTERNAL_GLOSSARY_LINKS, serverName, userId, glossaryGUID, from, size);

        return castOmasResult(response.getResult());
    }

    /**
     * Extract a category definition
     *
     * @param userId calling user
     * @param categoryGUID category GUID
     *
     * @return EntityDetailResponse category
     *
     * @throws PropertyServerException if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     * @throws GlossaryViewOmasException if a problem occurs on the omas backend
     */
    public GlossaryCategory getCategory(String userId, String categoryGUID)
            throws PropertyServerException, InvalidParameterException, GlossaryViewOmasException {

        GlossaryViewEntityDetailResponse response =  getSingleEntityResponse("getCategory", GET_CATEGORY,
                serverName, userId, categoryGUID);

        return firstEntity(castOmasResult(response.getResult()));
    }

    /**
     * Extract categories within a glossary
     *
     * @param userId calling user
     * @param glossaryGUID glossary GUID
     * @param from starting index
     * @param size max number of returned entities
     *
     * @return EntityDetailResponse categories
     *
     * @throws PropertyServerException if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     * @throws GlossaryViewOmasException if a problem occurs on the omas backend
     */
    public List<GlossaryCategory> getCategories(String userId, String glossaryGUID, Integer from, Integer size)
            throws PropertyServerException, InvalidParameterException, GlossaryViewOmasException {

        GlossaryViewEntityDetailResponse response = getMultipleRelatedEntitiesPagedResponse("getCategories",
                GET_CATEGORIES_OF_GLOSSARY, serverName, userId, glossaryGUID, from, size);

        return castOmasResult(response.getResult());
    }

    /**
     * Extract subcategories of a category
     *
     * @param userId calling user
     * @param categoryGUID category GUID
     * @param from starting index
     * @param size max number of returned entities
     *
     * @return EntityDetailResponse subcategories
     *
     * @throws PropertyServerException if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     * @throws GlossaryViewOmasException if a problem occurs on the omas backend
     */
    public List<GlossaryCategory> getSubcategories(String userId, String categoryGUID, Integer from, Integer size)
            throws PropertyServerException, InvalidParameterException, GlossaryViewOmasException {

        GlossaryViewEntityDetailResponse response = getMultipleRelatedEntitiesPagedResponse("getSubcategories",
                GET_SUBCATEGORIES, serverName, userId, categoryGUID, from, size);

        return castOmasResult(response.getResult());
    }

    /**
     * Extract a category's external glossary links
     *
     * @param userId calling user
     * @param categoryGUID category GUID
     * @param from starting index
     * @param size max number of returned entities
     *
     * @return EntityDetailResponse external glossary links
     *
     * @throws PropertyServerException if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     * @throws GlossaryViewOmasException if a problem occurs on the omas backend
     */
    public List<ExternalGlossaryLink> getExternalGlossaryLinksOfCategory(String userId, String categoryGUID, Integer from, Integer size)
            throws PropertyServerException, InvalidParameterException, GlossaryViewOmasException {

        GlossaryViewEntityDetailResponse response = getMultipleRelatedEntitiesPagedResponse("getExternalGlossaryLinksOfCategory",
                GET_CATEGORY_EXTERNAL_GLOSSARY_LINKS, serverName, userId, categoryGUID, from, size);

        return castOmasResult(response.getResult());
    }

    /**
     * Extract a term definition
     *
     * @param userId calling user
     * @param termGUID term GUID
     *
     * @return EntityDetailResponse term
     *
     * @throws PropertyServerException if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     * @throws GlossaryViewOmasException if a problem occurs on the omas backend
     */
    public GlossaryTerm getTerm(String userId, String termGUID)
            throws PropertyServerException, InvalidParameterException, GlossaryViewOmasException {

        GlossaryViewEntityDetailResponse response = getSingleEntityResponse("getTerm", GET_TERM, serverName,
                userId, termGUID);
        List<GlossaryViewEntityDetail> glossaryViewEntityDetails = castOmasResult(response.getResult());
        return (GlossaryTerm) firstEntity(glossaryViewEntityDetails);
    }

    /**
     * Extract terms within a glossary
     *
     * @param userId calling user
     * @param glossaryGUID glossary GUID
     * @param from starting index
     * @param size max number of returned entities
     *
     * @return EntityDetailResponse terms
     *
     * @throws PropertyServerException if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     * @throws GlossaryViewOmasException if a problem occurs on the omas backend
     */
    public List<GlossaryTerm> getTermsOfGlossary(String userId, String glossaryGUID, Integer from, Integer size)
            throws PropertyServerException, InvalidParameterException, GlossaryViewOmasException {

        GlossaryViewEntityDetailResponse response = getMultipleRelatedEntitiesPagedResponse("getTermsOfGlossary",
                GET_TERMS_OF_GLOSSARY, serverName, userId, glossaryGUID, from, size);

        return castOmasResult(response.getResult());
    }

    /**
     * Extract terms within a category
     *
     * @param userId calling user
     * @param categoryGUID category GUID
     * @param from starting index
     * @param size max number of returned entities
     *
     * @return EntityDetailResponse terms
     *
     * @throws PropertyServerException if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     * @throws GlossaryViewOmasException if a problem occurs on the omas backend
     */
    public List<GlossaryTerm> getTermsOfCategory(String userId, String categoryGUID, Integer from, Integer size)
            throws PropertyServerException, InvalidParameterException, GlossaryViewOmasException {

        GlossaryViewEntityDetailResponse response = getMultipleRelatedEntitiesPagedResponse("getTermsOfCategory",
                GET_TERMS_OF_CATEGORY, serverName, userId, categoryGUID, from, size);

        return castOmasResult(response.getResult());
    }

    /**
     * Extract a term's external glossary links
     *
     * @param userId calling user
     * @param termGUID term GUID
     * @param from starting index
     * @param size max number of returned entities
     *
     * @return EntityDetailResponse external glossary links
     *
     * @throws PropertyServerException if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     * @throws GlossaryViewOmasException if a problem occurs on the omas backend
     */
    public List<ExternalGlossaryLink> getExternalGlossaryLinksOfTerm(String userId, String termGUID, Integer from, Integer size)
            throws PropertyServerException, InvalidParameterException, GlossaryViewOmasException {

        GlossaryViewEntityDetailResponse response = getMultipleRelatedEntitiesPagedResponse("getExternalGlossaryLinksOfTerm",
                GET_TERM_EXTERNAL_GLOSSARY_LINKS, serverName, userId, termGUID, from, size);

        return castOmasResult(response.getResult());
    }

    /**
     * Extract related terms
     *
     * @param userId calling user
     * @param termGUID term GUID
     * @param from starting index
     * @param size max number of returned entities
     *
     * @return EntityDetailResponse terms
     *
     * @throws PropertyServerException if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     * @throws GlossaryViewOmasException if a problem occurs on the omas backend
     */
    public List<GlossaryTerm> getRelatedTerms(String userId, String termGUID, Integer from, Integer size)
            throws PropertyServerException, InvalidParameterException, GlossaryViewOmasException {

        GlossaryViewEntityDetailResponse response = getMultipleRelatedEntitiesPagedResponse("getRelatedTerms",
                GET_RELATED_TERMS, serverName, userId, termGUID, from, size);

        return castOmasResult(response.getResult());
    }

    /**
     * Extract synonyms
     *
     * @param userId calling user
     * @param termGUID term GUID
     * @param from starting index
     * @param size max number of returned entities
     *
     * @return EntityDetailResponse terms
     *
     * @throws PropertyServerException if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     * @throws GlossaryViewOmasException if a problem occurs on the omas backend
     */
    public List<GlossaryTerm> getSynonyms(String userId, String termGUID, Integer from, Integer size)
            throws PropertyServerException, InvalidParameterException, GlossaryViewOmasException {

        GlossaryViewEntityDetailResponse response = getMultipleRelatedEntitiesPagedResponse("getSynonyms",
                GET_SYNONYMS, serverName, userId, termGUID, from, size);

        return castOmasResult(response.getResult());
    }

    /**
     * Extract antonyms
     *
     * @param userId calling user
     * @param termGUID term GUID
     * @param from starting index
     * @param size max number of returned entities
     *
     * @return EntityDetailResponse terms
     *
     * @throws PropertyServerException if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     * @throws GlossaryViewOmasException if a problem occurs on the omas backend
     */
    public List<GlossaryTerm> getAntonyms(String userId, String termGUID, Integer from, Integer size)
            throws PropertyServerException, InvalidParameterException, GlossaryViewOmasException {

        GlossaryViewEntityDetailResponse response = getMultipleRelatedEntitiesPagedResponse("getAntonyms",
                GET_ANTONYMS, serverName, userId, termGUID, from, size);

        return castOmasResult(response.getResult());
    }

    /**
     * Extract preferred terms
     *
     * @param userId calling user
     * @param termGUID term GUID
     * @param from starting index
     * @param size max number of returned entities
     *
     * @return EntityDetailResponse terms
     *
     * @throws PropertyServerException if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     * @throws GlossaryViewOmasException if a problem occurs on the omas backend
     */
    public List<GlossaryTerm> getPreferredTerms(String userId, String termGUID, Integer from, Integer size)
            throws PropertyServerException, InvalidParameterException, GlossaryViewOmasException {

        GlossaryViewEntityDetailResponse response = getMultipleRelatedEntitiesPagedResponse("getPreferredTerms",
                GET_PREFERRED_TERMS, serverName, userId, termGUID, from, size);

        return castOmasResult(response.getResult());
    }

    /**
     * Extract replacement terms
     *
     * @param userId calling user
     * @param termGUID term GUID
     * @param from starting index
     * @param size max number of returned entities
     *
     * @return EntityDetailResponse terms
     *
     * @throws PropertyServerException if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     * @throws GlossaryViewOmasException if a problem occurs on the omas backend
     */
    public List<GlossaryTerm> getReplacementTerms(String userId, String termGUID, Integer from, Integer size)
            throws PropertyServerException, InvalidParameterException, GlossaryViewOmasException {

        GlossaryViewEntityDetailResponse response = getMultipleRelatedEntitiesPagedResponse("getReplacementTerms",
                GET_REPLACEMENT_TERMS, serverName, userId, termGUID, from, size);

        return castOmasResult(response.getResult());
    }

    /**
     * Extract translations
     *
     * @param userId calling user
     * @param termGUID term GUID
     * @param from starting index
     * @param size max number of returned entities
     *
     * @return EntityDetailResponse terms
     *
     * @throws PropertyServerException if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     * @throws GlossaryViewOmasException if a problem occurs on the omas backend
     */
    public List<GlossaryTerm> getTranslations(String userId, String termGUID, Integer from, Integer size)
            throws PropertyServerException, InvalidParameterException, GlossaryViewOmasException {

        GlossaryViewEntityDetailResponse response = getMultipleRelatedEntitiesPagedResponse("getTranslations",
                GET_TRANSLATIONS, serverName, userId, termGUID, from, size);

        return castOmasResult(response.getResult());
    }

    /**
     * Extract "is-a" terms
     *
     * @param userId calling user
     * @param termGUID term GUID
     * @param from starting index
     * @param size max number of returned entities
     *
     * @return EntityDetailResponse terms
     *
     * @throws PropertyServerException if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     * @throws GlossaryViewOmasException if a problem occurs on the omas backend
     */
    public List<GlossaryTerm> getIsA(String userId, String termGUID, Integer from, Integer size)
            throws PropertyServerException, InvalidParameterException, GlossaryViewOmasException {

        GlossaryViewEntityDetailResponse response = getMultipleRelatedEntitiesPagedResponse("getIsA", GET_IS_A,
                serverName, userId, termGUID, from, size);

        return castOmasResult(response.getResult());
    }

    /**
     * Extract valid values
     *
     * @param userId calling user
     * @param termGUID term GUID
     * @param from starting index
     * @param size max number of returned entities
     *
     * @return EntityDetailResponse terms
     *
     * @throws PropertyServerException if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     * @throws GlossaryViewOmasException if a problem occurs on the omas backend
     */
    public List<GlossaryTerm> getValidValues(String userId, String termGUID, Integer from, Integer size)
            throws PropertyServerException, InvalidParameterException, GlossaryViewOmasException {

        GlossaryViewEntityDetailResponse response = getMultipleRelatedEntitiesPagedResponse("getValidValues",
                GET_VALID_VALUES, serverName, userId, termGUID, from, size);

        return castOmasResult(response.getResult());
    }

    /**
     * Extract "used-in-contexts" terms
     *
     * @param userId calling user
     * @param termGUID term GUID
     * @param from starting index
     * @param size max number of returned entities
     *
     * @return EntityDetailResponse terms
     *
     * @throws PropertyServerException if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     * @throws GlossaryViewOmasException if a problem occurs on the omas backend
     */
    public List<GlossaryTerm> getUsedInContexts(String userId, String termGUID, Integer from, Integer size)
            throws PropertyServerException, InvalidParameterException, GlossaryViewOmasException {

        GlossaryViewEntityDetailResponse response = getMultipleRelatedEntitiesPagedResponse("getUsedInContexts",
                GET_USED_IN_CONTEXTS, serverName, userId, termGUID, from, size);

        return castOmasResult(response.getResult());
    }

    /**
     * Extract assigned elements
     *
     * @param userId calling user
     * @param termGUID term GUID
     * @param from starting index
     * @param size max number of returned entities
     *
     * @return EntityDetailResponse terms
     *
     * @throws PropertyServerException if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     * @throws GlossaryViewOmasException if a problem occurs on the omas backend
     */
    public List<GlossaryTerm> getAssignedElements(String userId, String termGUID, Integer from, Integer size)
            throws PropertyServerException, InvalidParameterException, GlossaryViewOmasException {

        GlossaryViewEntityDetailResponse response = getMultipleRelatedEntitiesPagedResponse("getAssignedElements",
                GET_ASSIGNED_ELEMENTS, serverName, userId, termGUID, from, size);

        return castOmasResult(response.getResult());
    }

    /**
     * Extract attributes
     *
     * @param userId calling user
     * @param termGUID term GUID
     * @param from starting index
     * @param size max number of returned entities
     *
     * @return EntityDetailResponse terms
     *
     * @throws PropertyServerException if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     * @throws GlossaryViewOmasException if a problem occurs on the omas backend
     */
    public List<GlossaryTerm> getAttributes(String userId, String termGUID, Integer from, Integer size)
            throws PropertyServerException, InvalidParameterException, GlossaryViewOmasException {

        GlossaryViewEntityDetailResponse response = getMultipleRelatedEntitiesPagedResponse("getAttributes",
                GET_ATTRIBUTES, serverName, userId, termGUID, from, size);

        return castOmasResult(response.getResult());
    }

    /**
     * Extract subtypes
     *
     * @param userId calling user
     * @param termGUID term GUID
     * @param from starting index
     * @param size max number of returned entities
     *
     * @return EntityDetailResponse terms
     *
     * @throws PropertyServerException if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     * @throws GlossaryViewOmasException if a problem occurs on the omas backend
     */
    public List<GlossaryTerm> getSubtypes(String userId, String termGUID, Integer from, Integer size)
            throws PropertyServerException, InvalidParameterException, GlossaryViewOmasException {

        GlossaryViewEntityDetailResponse response = getMultipleRelatedEntitiesPagedResponse("getSubtypes",
                GET_SUBTYPES, serverName, userId, termGUID, from, size);

        return castOmasResult(response.getResult());
    }

    /**
     * Extract types
     *
     * @param userId calling user
     * @param termGUID term GUID
     * @param from starting index
     * @param size max number of returned entities
     *
     * @return EntityDetailResponse terms
     *
     * @throws PropertyServerException if a problem occurs while serving the request
     * @throws InvalidParameterException if parameter validation fails
     * @throws GlossaryViewOmasException if a problem occurs on the omas backend
     */
    public List<GlossaryTerm> getTypes(String userId, String termGUID, Integer from, Integer size)
            throws PropertyServerException, InvalidParameterException, GlossaryViewOmasException {

        GlossaryViewEntityDetailResponse response = getMultipleRelatedEntitiesPagedResponse("getTypes",
                GET_TYPES, serverName, userId, termGUID, from, size);

        return castOmasResult(response.getResult());
    }

    /*
    * In order to have meaning, extracted into a separate method the 'casting' instructions
    */
    private <T extends GlossaryViewEntityDetail> List<T> castOmasResult(List<GlossaryViewEntityDetail> omasResult){
        return omasResult.stream().map(e -> (T)e).collect(Collectors.toList());
    }

    private <T extends GlossaryViewEntityDetail> T firstEntity(List<T> entities){
        if(entities == null){
            return null;
        }
        if(entities.size() == 1){
            return entities.get(0);
        }
        return null;
    }

}
