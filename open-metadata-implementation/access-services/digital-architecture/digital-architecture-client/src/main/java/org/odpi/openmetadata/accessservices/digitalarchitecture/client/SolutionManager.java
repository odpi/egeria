/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.digitalarchitecture.client;

import org.odpi.openmetadata.accessservices.digitalarchitecture.api.ManageSolutions;
import org.odpi.openmetadata.accessservices.digitalarchitecture.client.rest.DigitalArchitectureRESTClient;
import org.odpi.openmetadata.accessservices.digitalarchitecture.ffdc.DigitalArchitectureAuditCode;
import org.odpi.openmetadata.commonservices.mermaid.InformationSupplyChainMermaidGraphBuilder;
import org.odpi.openmetadata.commonservices.mermaid.SolutionBlueprintMermaidGraphBuilder;
import org.odpi.openmetadata.commonservices.mermaid.SolutionRoleMermaidGraphBuilder;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.governanceaction.converters.*;
import org.odpi.openmetadata.frameworks.governanceaction.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RelatedMetadataElementList;
import org.odpi.openmetadata.frameworks.governanceaction.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ElementStatus;
import org.odpi.openmetadata.frameworks.openmetadata.enums.SequencingOrder;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionBlueprintCompositionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionLinkingWireProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ManageSolutions provides methods to define information supply chains, solution components and their supporting objects
 * The interface supports the following types of objects
 *
 * <ul>
 *     <li>InformationSupplyChains</li>
 *     <li>InformationSupplyChainSegments</li>
 *     <li>SolutionBlueprints</li>
 *     <li>SolutionComponents</li>
 *     <li>SolutionPorts</li>
 *     <li>SolutionRoles</li>
 * </ul>
 */
public class SolutionManager extends DigitalArchitectureClientBase implements ManageSolutions
{
    final boolean forLineage = true;
    final boolean forDuplicateProcessing = false;


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param maxPageSize           maximum number of results supported by this server
     * @param auditLog              logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public SolutionManager(String   serverName,
                           String   serverPlatformURLRoot,
                           int      maxPageSize,
                           AuditLog auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, maxPageSize, auditLog);
    }


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param maxPageSize           maximum number of results supported by this server
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public SolutionManager(String serverName,
                           String serverPlatformURLRoot,
                           int    maxPageSize) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, maxPageSize);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param userId                caller's userId embedded in all HTTP requests
     * @param password              caller's userId embedded in all HTTP requests
     * @param maxPageSize           maximum number of results supported by this server
     * @param auditLog              logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public SolutionManager(String   serverName,
                           String   serverPlatformURLRoot,
                           String   userId,
                           String   password,
                           int      maxPageSize,
                           AuditLog auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password, maxPageSize, auditLog);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param userId                caller's userId embedded in all HTTP requests
     * @param password              caller's userId embedded in all HTTP requests
     * @param maxPageSize           maximum number of results supported by this server
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public SolutionManager(String serverName,
                           String serverPlatformURLRoot,
                           String userId,
                           String password,
                           int    maxPageSize) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password, maxPageSize);
    }


    /**
     * Create a new client that is going to be used in an OMAG Server (view service or integration service typically).
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param restClient            client that issues the REST API calls
     * @param maxPageSize           maximum number of results supported by this server
     * @param auditLog              logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public SolutionManager(String                        serverName,
                           String                        serverPlatformURLRoot,
                           DigitalArchitectureRESTClient restClient,
                           int                           maxPageSize,
                           AuditLog                      auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, restClient, maxPageSize, auditLog);
    }


    /**
     * Retrieve the list of information supply chains metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId               calling user
     * @param searchString         string to find in the properties
     * @param limitResultsByStatus control the status of the elements to retrieve - default is everything but Deleted
     * @param asOfTime             repository time to use
     * @param sequencingOrder      order to retrieve results
     * @param sequencingProperty   property to use for sequencing order
     * @param startFrom            paging start point
     * @param pageSize             maximum results that can be returned
     * @param effectiveTime        effectivity dating for elements
     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<InformationSupplyChainElement> findInformationSupplyChains(String userId,
                                                                           String searchString,
                                                                           List<ElementStatus> limitResultsByStatus,
                                                                           Date asOfTime,
                                                                           SequencingOrder sequencingOrder,
                                                                           String sequencingProperty,
                                                                           int startFrom,
                                                                           int pageSize,
                                                                           Date effectiveTime) throws InvalidParameterException,
                                                                                                      UserNotAuthorizedException,
                                                                                                      PropertyServerException
    {
        final String methodName = "findInformationSupplyChains";
        final String searchStringParameterName = "searchString";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, searchStringParameterName, methodName);
        invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        List<OpenMetadataElement> openMetadataElements = openMetadataStoreClient.findMetadataElementsWithString(userId,
                                                                                                                searchString,
                                                                                                                OpenMetadataType.INFORMATION_SUPPLY_CHAIN.typeName,
                                                                                                                limitResultsByStatus,
                                                                                                                asOfTime,
                                                                                                                sequencingProperty,
                                                                                                                sequencingOrder,
                                                                                                                forLineage,
                                                                                                                forDuplicateProcessing,
                                                                                                                effectiveTime,
                                                                                                                startFrom,
                                                                                                                pageSize);

        return convertInformationSupplyChains(userId, openMetadataElements, asOfTime, effectiveTime, methodName);
    }


    /**
     * Retrieve the list of solution blueprint metadata elements that contain the search string.
     * The returned blueprints include a list of the components that are associated with it.
     * The search string is treated as a regular expression.
     *
     * @param userId               calling user
     * @param searchString         string to find in the properties
     * @param limitResultsByStatus control the status of the elements to retrieve - default is everything but Deleted
     * @param asOfTime             repository time to use
     * @param sequencingOrder      order to retrieve results
     * @param sequencingProperty   property to use for sequencing order
     * @param startFrom            paging start point
     * @param pageSize             maximum results that can be returned
     * @param effectiveTime        effectivity dating for elements
     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<SolutionBlueprintElement> findSolutionBlueprints(String userId, String searchString, List<ElementStatus> limitResultsByStatus, Date asOfTime, SequencingOrder sequencingOrder, String sequencingProperty, int startFrom, int pageSize, Date effectiveTime) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException
    {
        final String methodName = "findSolutionBlueprints";
        final String searchStringParameterName = "searchString";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, searchStringParameterName, methodName);
        invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        List<OpenMetadataElement> openMetadataElements = openMetadataStoreClient.findMetadataElementsWithString(userId,
                                                                                                                searchString,
                                                                                                                OpenMetadataType.SOLUTION_BLUEPRINT.typeName,
                                                                                                                limitResultsByStatus,
                                                                                                                asOfTime,
                                                                                                                sequencingProperty,
                                                                                                                sequencingOrder,
                                                                                                                forLineage,
                                                                                                                forDuplicateProcessing,
                                                                                                                effectiveTime,
                                                                                                                startFrom,
                                                                                                                pageSize);

        return convertSolutionBlueprints(userId, openMetadataElements, asOfTime, effectiveTime, methodName);
    }


    /**
     * Retrieve the list of actor roles metadata elements that contain the search string and show which solution components (if any) are attached to it.
     * The returned solution roles include a list of the components that are associated with it.
     * The search string is treated as a regular expression.
     *
     * @param userId               calling user
     * @param searchString         string to find in the properties
     * @param limitResultsByStatus control the status of the elements to retrieve - default is everything but Deleted
     * @param asOfTime             repository time to use
     * @param sequencingOrder      order to retrieve results
     * @param sequencingProperty   property to use for sequencing order
     * @param startFrom            paging start point
     * @param pageSize             maximum results that can be returned
     * @param effectiveTime        effectivity dating for elements
     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<SolutionRoleElement> findSolutionRoles(String userId, String searchString, List<ElementStatus> limitResultsByStatus, Date asOfTime, SequencingOrder sequencingOrder, String sequencingProperty, int startFrom, int pageSize, Date effectiveTime) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException
    {
        final String methodName = "findSolutionRoles";
        final String searchStringParameterName = "searchString";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, searchStringParameterName, methodName);
        invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        List<OpenMetadataElement> openMetadataElements = openMetadataStoreClient.findMetadataElementsWithString(userId,
                                                                                                                searchString,
                                                                                                                OpenMetadataType.ACTOR_ROLE.typeName,
                                                                                                                limitResultsByStatus,
                                                                                                                asOfTime,
                                                                                                                sequencingProperty,
                                                                                                                sequencingOrder,
                                                                                                                forLineage,
                                                                                                                forDuplicateProcessing,
                                                                                                                effectiveTime,
                                                                                                                startFrom,
                                                                                                                pageSize);

        return convertSolutionRoles(userId, openMetadataElements, asOfTime, effectiveTime, methodName);
    }


    /**
     * Retrieve the list of solution component metadata elements that contain the search string.  The solutions components returned include information about the consumers, actors and other solution components that are associated with them.
     * The returned solution components include a list of the subcomponents, peer components and solution roles that are associated with it.
     * The search string is treated as a regular expression.
     *
     * @param userId               calling user
     * @param searchString         string to find in the properties
     * @param limitResultsByStatus control the status of the elements to retrieve - default is everything but Deleted
     * @param asOfTime             repository time to use
     * @param sequencingOrder      order to retrieve results
     * @param sequencingProperty   property to use for sequencing order
     * @param startFrom            paging start point
     * @param pageSize             maximum results that can be returned
     * @param effectiveTime        effectivity dating for elements
     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<SolutionComponentElement> findSolutionComponents(String userId, String searchString, List<ElementStatus> limitResultsByStatus, Date asOfTime, SequencingOrder sequencingOrder, String sequencingProperty, int startFrom, int pageSize, Date effectiveTime) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException
    {
        final String methodName = "findSolutionComponents";
        final String searchStringParameterName = "searchString";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, searchStringParameterName, methodName);
        invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        List<OpenMetadataElement> openMetadataElements = openMetadataStoreClient.findMetadataElementsWithString(userId,
                                                                                                                searchString,
                                                                                                                OpenMetadataType.SOLUTION_COMPONENT.typeName,
                                                                                                                limitResultsByStatus,
                                                                                                                asOfTime,
                                                                                                                sequencingProperty,
                                                                                                                sequencingOrder,
                                                                                                                forLineage,
                                                                                                                forDuplicateProcessing,
                                                                                                                effectiveTime,
                                                                                                                startFrom,
                                                                                                                pageSize);

        return convertSolutionComponents(userId, openMetadataElements, asOfTime, effectiveTime, methodName);
    }


    /**
     * Retrieve the list of  metadata elements that are associated with the solution component via the ImplementedBy relationship.
     *
     * @param userId                calling user
     * @param solutionComponentGUID unique identifier of the solution component to query
     * @param limitResultsByStatus  control the status of the elements to retrieve - default is everything but Deleted
     * @param asOfTime              repository time to use
     * @param sequencingOrder       order to retrieve results
     * @param sequencingProperty    property to use for sequencing order
     * @param startFrom             paging start point
     * @param pageSize              maximum results that can be returned
     * @param effectiveTime         effectivity dating for elements
     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<RelatedMetadataElementSummary> getSolutionComponentImplementations(String              userId,
                                                                                   String              solutionComponentGUID,
                                                                                   List<ElementStatus> limitResultsByStatus,
                                                                                   Date                asOfTime,
                                                                                   SequencingOrder     sequencingOrder,
                                                                                   String              sequencingProperty,
                                                                                   int                 startFrom,
                                                                                   int                 pageSize,
                                                                                   Date                effectiveTime) throws InvalidParameterException,
                                                                                                                             UserNotAuthorizedException,
                                                                                                                             PropertyServerException
    {
        final String methodName = "getSolutionComponentImplementations";
        final String parameterName = "solutionComponentGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(solutionComponentGUID, parameterName, methodName);
        invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        RelatedMetadataElementList relatedMetadataElements = openMetadataStoreClient.getRelatedMetadataElements(userId,
                                                                                                                solutionComponentGUID,
                                                                                                                1,
                                                                                                                OpenMetadataType.IMPLEMENTED_BY_RELATIONSHIP.typeName,
                                                                                                                limitResultsByStatus,
                                                                                                                asOfTime,
                                                                                                                sequencingProperty,
                                                                                                                sequencingOrder,
                                                                                                                forLineage,
                                                                                                                forDuplicateProcessing,
                                                                                                                effectiveTime,
                                                                                                                startFrom,
                                                                                                                pageSize);

        return convertSolutionComponentImplementations(relatedMetadataElements, methodName);
    }


    /**
     * Convert the open metadata elements retrieve into information supply chain elements.
     *
     * @param userId calling user
     * @param openMetadataElements elements extracted from the repository
     * @param asOfTime repository time to use
     * @param effectiveTime effectivity dating for elements
     * @param methodName calling method
     * @return list of information supply chains (or null)
     */
    private List<InformationSupplyChainElement> convertInformationSupplyChains(String                    userId,
                                                                               List<OpenMetadataElement> openMetadataElements,
                                                                               Date                      asOfTime,
                                                                               Date                      effectiveTime,
                                                                               String                    methodName)
    {
        if (openMetadataElements != null)
        {
            List<InformationSupplyChainElement> informationSupplyChainElements = new ArrayList<>();

            for (OpenMetadataElement openMetadataElement : openMetadataElements)
            {
                if (openMetadataElement != null)
                {
                    informationSupplyChainElements.add(convertInformationSupplyChain(userId, openMetadataElement, asOfTime, effectiveTime, methodName));
                }
            }

            return informationSupplyChainElements;
        }

        return null;
    }


    /**
     * Convert the open metadata elements retrieve into solution blueprint elements.
     *
     * @param userId calling user
     * @param openMetadataElements elements extracted from the repository
     * @param asOfTime repository time to use
     * @param effectiveTime effectivity dating for elements
     * @param methodName calling method
     * @return list of solution blueprints (or null)
     */
    private List<SolutionBlueprintElement> convertSolutionBlueprints(String                    userId,
                                                                     List<OpenMetadataElement> openMetadataElements,
                                                                     Date                      asOfTime,
                                                                     Date                      effectiveTime,
                                                                     String                    methodName)
    {
        if (openMetadataElements != null)
        {
            List<SolutionBlueprintElement> solutionBlueprintElements = new ArrayList<>();

            for (OpenMetadataElement openMetadataElement : openMetadataElements)
            {
                if (openMetadataElement != null)
                {
                    solutionBlueprintElements.add(convertSolutionBlueprint(userId, openMetadataElement, asOfTime, effectiveTime, methodName));
                }
            }

            return solutionBlueprintElements;
        }

        return null;
    }


    /**
     * Convert the open metadata elements retrieve into solution role elements.
     *
     * @param userId calling user
     * @param openMetadataElements elements extracted from the repository
     * @param asOfTime repository time to use
     * @param effectiveTime effectivity dating for elements
     * @param methodName calling method
     * @return list of solution roles (or null)
     */
    private List<SolutionRoleElement> convertSolutionRoles(String                    userId,
                                                           List<OpenMetadataElement> openMetadataElements,
                                                           Date                      asOfTime,
                                                           Date                      effectiveTime,
                                                           String                    methodName)
    {
        if (openMetadataElements != null)
        {
            List<SolutionRoleElement> solutionRoleElements = new ArrayList<>();

            for (OpenMetadataElement openMetadataElement : openMetadataElements)
            {
                if (openMetadataElement != null)
                {
                    solutionRoleElements.add(convertSolutionRole(userId, openMetadataElement, asOfTime, effectiveTime, methodName));
                }
            }

            return solutionRoleElements;
        }

        return null;
    }


    /**
     * Convert the open metadata elements retrieve into solution component elements.
     *
     * @param userId calling user
     * @param openMetadataElements elements extracted from the repository
     * @param asOfTime repository time to use
     * @param effectiveTime effectivity dating for elements
     * @param methodName calling method
     * @return list of solution components (or null)
     */
    private List<SolutionComponentElement> convertSolutionComponents(String                    userId,
                                                                     List<OpenMetadataElement> openMetadataElements,
                                                                     Date                      asOfTime,
                                                                     Date                      effectiveTime,
                                                                     String                    methodName)
    {
        if (openMetadataElements != null)
        {
            List<SolutionComponentElement> solutionComponentElements = new ArrayList<>();

            for (OpenMetadataElement openMetadataElement : openMetadataElements)
            {
                if (openMetadataElement != null)
                {
                    solutionComponentElements.add(convertSolutionComponent(userId, openMetadataElement, asOfTime, effectiveTime, methodName));
                }
            }

            return solutionComponentElements;
        }

        return null;
    }


    /**
     * Convert the open metadata elements retrieve into summary elements.
     *
     * @param relatedMetadataElementList elements retrieved from the repository
     * @param methodName calling method
     * @return list of summary elements (or null)
     */
    private List<RelatedMetadataElementSummary> convertSolutionComponentImplementations(RelatedMetadataElementList relatedMetadataElementList,
                                                                                        String                     methodName)
    {
        if ((relatedMetadataElementList != null) && (relatedMetadataElementList.getElementList() != null))
        {
            List<RelatedMetadataElementSummary> solutionComponentImplementationElements = new ArrayList<>();

            for (RelatedMetadataElement relatedMetadataElement : relatedMetadataElementList.getElementList())
            {
                if (relatedMetadataElement != null)
                {
                    solutionComponentImplementationElements.add(convertSolutionComponentImplementation(relatedMetadataElement, methodName));
                }
            }

            return solutionComponentImplementationElements;
        }

        return null;
    }


    /**
     * Return the information supply chain extracted from the open metadata element.
     *
     * @param userId calling user
     * @param openMetadataElement element extracted from the repository
     * @param asOfTime repository time to use
     * @param effectiveTime effectivity dating for elements
     * @param methodName calling method
     * @return bean or null
     */
    private InformationSupplyChainElement convertInformationSupplyChain(String              userId,
                                                                        OpenMetadataElement openMetadataElement,
                                                                        Date                asOfTime,
                                                                        Date                effectiveTime,
                                                                        String              methodName)
    {
        try
        {
            List<InformationSupplyChainSegmentElement> relatedSegments = new ArrayList<>();

            int startFrom = 0;
            RelatedMetadataElementList relatedMetadataElementList = openMetadataStoreClient.getRelatedMetadataElements(userId,
                                                                                                                       openMetadataElement.getElementGUID(),
                                                                                                                       1,
                                                                                                                       OpenMetadataType.INFORMATION_SUPPLY_CHAIN_COMPOSITION_RELATIONSHIP.typeName,
                                                                                                                       null,
                                                                                                                       asOfTime,
                                                                                                                       null,
                                                                                                                       SequencingOrder.CREATION_DATE_RECENT,
                                                                                                                       forLineage,
                                                                                                                       forDuplicateProcessing,
                                                                                                                       effectiveTime,
                                                                                                                       startFrom,
                                                                                                                       invalidParameterHandler.getMaxPagingSize());
            while ((relatedMetadataElementList != null) && (relatedMetadataElementList.getElementList() != null))
            {
                for (RelatedMetadataElement relatedMetadataElement : relatedMetadataElementList.getElementList())
                {
                    if (relatedMetadataElement != null)
                    {
                        relatedSegments.add(this.convertInformationSupplyChainSegment(userId, relatedMetadataElement.getElement(), asOfTime, effectiveTime, methodName));
                    }
                }

                startFrom = startFrom + invalidParameterHandler.getMaxPagingSize();
                relatedMetadataElementList = openMetadataStoreClient.getRelatedMetadataElements(userId,
                                                                                                openMetadataElement.getElementGUID(),
                                                                                                1,
                                                                                                OpenMetadataType.INFORMATION_SUPPLY_CHAIN_COMPOSITION_RELATIONSHIP.typeName,
                                                                                                null,
                                                                                                asOfTime,
                                                                                                null,
                                                                                                SequencingOrder.CREATION_DATE_RECENT,
                                                                                                forLineage,
                                                                                                forDuplicateProcessing,
                                                                                                effectiveTime,
                                                                                                startFrom,
                                                                                                invalidParameterHandler.getMaxPagingSize());
            }

            InformationSupplyChainConverter<InformationSupplyChainElement> converter = new InformationSupplyChainConverter<>(propertyHelper, serviceName, serverName, relatedSegments);
            InformationSupplyChainElement informationSupplyChainElement = converter.getNewBean(InformationSupplyChainElement.class,
                                                                                                openMetadataElement,
                                                                                                methodName);
            if (informationSupplyChainElement != null)
            {
                InformationSupplyChainMermaidGraphBuilder graphBuilder = new InformationSupplyChainMermaidGraphBuilder(informationSupplyChainElement);

                informationSupplyChainElement.setMermaidGraph(graphBuilder.getMermaidGraph());
            }

            return informationSupplyChainElement;
        }
        catch (Exception error)
        {
            if (auditLog != null)
            {
                auditLog.logMessage(methodName,
                                    DigitalArchitectureAuditCode.UNEXPECTED_CONVERTER_EXCEPTION.getMessageDefinition(error.getClass().getName(),
                                                                                                                     methodName,
                                                                                                                     error.getMessage()));
            }

            return null;
        }
    }


    /**
     * Return the information supply chain extracted from the open metadata element.
     *
     * @param userId calling user
     * @param openMetadataElement element extracted from the repository
     * @param asOfTime repository time to use
     * @param effectiveTime effectivity dating for elements
     * @param methodName calling method
     * @return bean or null
     */
    private InformationSupplyChainSegmentElement convertInformationSupplyChainSegment(String              userId,
                                                                                      OpenMetadataElement openMetadataElement,
                                                                                      Date                asOfTime,
                                                                                      Date                effectiveTime,
                                                                                      String              methodName)
    {
        try
        {
            List<RelatedMetadataElement>        relatedMetadataElements      = new ArrayList<>();
            List<RelatedMetadataElementSummary> relatedPortElements          = new ArrayList<>();
            List<SolutionLinkingWireRelationship> solutionLinkingWires       = new ArrayList<>();

            int startFrom = 0;
            RelatedMetadataElementList relatedMetadataElementList = openMetadataStoreClient.getRelatedMetadataElements(userId,
                                                                                                                       openMetadataElement.getElementGUID(),
                                                                                                                       1,
                                                                                                                       null,
                                                                                                                       null,
                                                                                                                       asOfTime,
                                                                                                                       null,
                                                                                                                       SequencingOrder.CREATION_DATE_RECENT,
                                                                                                                       forLineage,
                                                                                                                       forDuplicateProcessing,
                                                                                                                       effectiveTime,
                                                                                                                       startFrom,
                                                                                                                       invalidParameterHandler.getMaxPagingSize());
            while ((relatedMetadataElementList != null) && (relatedMetadataElementList.getElementList() != null))
            {
                for (RelatedMetadataElement relatedMetadataElement : relatedMetadataElementList.getElementList())
                {
                    if (relatedMetadataElement != null)
                    {
                        if (propertyHelper.isTypeOf(relatedMetadataElement.getElement(), OpenMetadataType.SOLUTION_COMPONENT.typeName))
                        {
                            List<RelatedMetadataElementSummary> componentPorts = this.convertPortSummaries(userId,
                                                                                                           relatedMetadataElement.getElement().getElementGUID(),
                                                                                                           asOfTime,
                                                                                                           effectiveTime,
                                                                                                           methodName);

                            if (componentPorts != null)
                            {
                                relatedPortElements.addAll(componentPorts);
                            }

                            List<SolutionLinkingWireRelationship> solutionLinkingWireRelationships = this.convertSolutionLinkingWires(userId,
                                                                                                                                      relatedMetadataElement.getElement(),
                                                                                                                                      asOfTime,
                                                                                                                                      effectiveTime,
                                                                                                                                      methodName);

                            if (solutionLinkingWireRelationships != null)
                            {
                                solutionLinkingWires.addAll(solutionLinkingWireRelationships);
                            }
                        }

                        relatedMetadataElements.add(relatedMetadataElement);
                    }
                }

                startFrom = startFrom + invalidParameterHandler.getMaxPagingSize();
                relatedMetadataElementList = openMetadataStoreClient.getRelatedMetadataElements(userId,
                                                                                                openMetadataElement.getElementGUID(),
                                                                                                1,
                                                                                                null,
                                                                                                null,
                                                                                                asOfTime,
                                                                                                null,
                                                                                                SequencingOrder.CREATION_DATE_RECENT,
                                                                                                forLineage,
                                                                                                forDuplicateProcessing,
                                                                                                effectiveTime,
                                                                                                startFrom,
                                                                                                invalidParameterHandler.getMaxPagingSize());
            }

            InformationSupplyChainSegmentConverter<InformationSupplyChainSegmentElement> converter = new InformationSupplyChainSegmentConverter<>(propertyHelper, serviceName, serverName, solutionLinkingWires, relatedPortElements);
            return converter.getNewComplexBean(InformationSupplyChainSegmentElement.class,
                                               openMetadataElement,
                                               relatedMetadataElements,
                                               methodName);
        }
        catch (Exception error)
        {
            if (auditLog != null)
            {
                auditLog.logMessage(methodName,
                                    DigitalArchitectureAuditCode.UNEXPECTED_CONVERTER_EXCEPTION.getMessageDefinition(error.getClass().getName(),
                                                                                                                     methodName,
                                                                                                                     error.getMessage()));
            }

            return null;
        }
    }


    /**
     * Return the port summaries for a solution component.
     *
     * @param userId calling user
     * @param solutionComponent starting entity
     * @param asOfTime repository time to use
     * @param effectiveTime effectivity dating for elements
     * @param methodName calling method
     * @return list
     */
    private List<SolutionLinkingWireRelationship> convertSolutionLinkingWires(String              userId,
                                                                              OpenMetadataElement solutionComponent,
                                                                              Date                asOfTime,
                                                                              Date                effectiveTime,
                                                                              String              methodName)
    {
        try
        {
            OpenMetadataConverterBase<ElementStub> wireConverter = new OpenMetadataConverterBase<>(propertyHelper, serviceName, serverName);
            List<SolutionLinkingWireRelationship>  solutionLinkingWireRelationships = new ArrayList<>();

            ElementStub solutionComponentStub = wireConverter.getElementStub(ElementStub.class, solutionComponent, methodName);

            int startFrom = 0;

            RelatedMetadataElementList relatedMetadataElementList = openMetadataStoreClient.getRelatedMetadataElements(userId,
                                                                                                                       solutionComponent.getElementGUID(),
                                                                                                                       0,
                                                                                                                       OpenMetadataType.SOLUTION_LINKING_WIRE_RELATIONSHIP.typeName,
                                                                                                                       null,
                                                                                                                       asOfTime,
                                                                                                                       null,
                                                                                                                       SequencingOrder.CREATION_DATE_RECENT,
                                                                                                                       forLineage,
                                                                                                                       forDuplicateProcessing,
                                                                                                                       effectiveTime,
                                                                                                                       startFrom,
                                                                                                                       invalidParameterHandler.getMaxPagingSize());
            while ((relatedMetadataElementList != null) && (relatedMetadataElementList.getElementList() != null))
            {
                for (RelatedMetadataElement relatedMetadataElement : relatedMetadataElementList.getElementList())
                {
                    if (relatedMetadataElement != null)
                    {
                        SolutionLinkingWireRelationship wire = new SolutionLinkingWireRelationship();

                        wire.setElementHeader(wireConverter.getMetadataElementHeader(ElementStub.class,
                                                                                     relatedMetadataElement,
                                                                                     relatedMetadataElement.getRelationshipGUID(),
                                                                                     null,
                                                                                     methodName));

                        if (relatedMetadataElement.getRelationshipProperties() != null)
                        {
                            SolutionLinkingWireProperties wireProperties = new SolutionLinkingWireProperties();

                            ElementProperties elementProperties = new ElementProperties(relatedMetadataElement.getRelationshipProperties());

                            wireProperties.setInformationSupplyChainSegmentGUIDs(wireConverter.removeInformationSupplyChainSegmentGUIDs(elementProperties));
                            wireProperties.setExtendedProperties(wireConverter.getRemainingExtendedProperties(elementProperties));
                            wireProperties.setEffectiveFrom(relatedMetadataElement.getEffectiveFromTime());
                            wireProperties.setEffectiveTo(relatedMetadataElement.getEffectiveToTime());

                            wire.setProperties(wireProperties);
                        }
                        ElementStub otherEndStub = wireConverter.getElementStub(ElementStub.class, relatedMetadataElement.getElement(), methodName);

                        if (relatedMetadataElement.getElementAtEnd1())
                        {
                            wire.setEnd1Element(otherEndStub);
                            wire.setEnd2Element(solutionComponentStub);
                        }
                        else
                        {
                            wire.setEnd1Element(solutionComponentStub);
                            wire.setEnd2Element(otherEndStub);
                        }

                        solutionLinkingWireRelationships.add(wire);
                    }
                }

                startFrom                  = startFrom + invalidParameterHandler.getMaxPagingSize();
                relatedMetadataElementList = openMetadataStoreClient.getRelatedMetadataElements(userId,
                                                                                                solutionComponent.getElementGUID(),
                                                                                                0,
                                                                                                OpenMetadataType.SOLUTION_LINKING_WIRE_RELATIONSHIP.typeName,
                                                                                                null,
                                                                                                asOfTime,
                                                                                                null,
                                                                                                SequencingOrder.CREATION_DATE_RECENT,
                                                                                                forLineage,
                                                                                                forDuplicateProcessing,
                                                                                                effectiveTime,
                                                                                                startFrom,
                                                                                                invalidParameterHandler.getMaxPagingSize());
            }

            if (! solutionLinkingWireRelationships.isEmpty())
            {
                return solutionLinkingWireRelationships;
            }
        }
        catch (Exception error)
        {
            if (auditLog != null)
            {
                auditLog.logMessage(methodName,
                                    DigitalArchitectureAuditCode.UNEXPECTED_CONVERTER_EXCEPTION.getMessageDefinition(error.getClass().getName(),
                                                                                                                     methodName,
                                                                                                                     error.getMessage()));
            }
        }

        return null;
    }


    /**
     * Return the port summaries for a solution component.
     *
     * @param userId calling user
     * @param solutionComponentGUID starting guid
     * @param asOfTime repository time to use
     * @param effectiveTime effectivity dating for elements
     * @param methodName calling method
     * @return list
     */
    private List<RelatedMetadataElementSummary> convertPortSummaries(String userId,
                                                                     String solutionComponentGUID,
                                                                     Date   asOfTime,
                                                                     Date   effectiveTime,
                                                                     String methodName)
    {
        try
        {
            OpenMetadataConverterBase<RelatedMetadataElementSummary> portConverter = new OpenMetadataConverterBase<>(propertyHelper, serviceName, serverName);
            List<RelatedMetadataElementSummary>                      portSummaries = new ArrayList<>();

            int startFrom = 0;

            RelatedMetadataElementList relatedMetadataElementList = openMetadataStoreClient.getRelatedMetadataElements(userId,
                                                                                                                       solutionComponentGUID,
                                                                                                                       1,
                                                                                                                       OpenMetadataType.SOLUTION_COMPONENT_PORT_RELATIONSHIP.typeName,
                                                                                                                       null,
                                                                                                                       asOfTime,
                                                                                                                       null,
                                                                                                                       SequencingOrder.CREATION_DATE_RECENT,
                                                                                                                       forLineage,
                                                                                                                       forDuplicateProcessing,
                                                                                                                       effectiveTime,
                                                                                                                       startFrom,
                                                                                                                       invalidParameterHandler.getMaxPagingSize());
            while ((relatedMetadataElementList != null) && (relatedMetadataElementList.getElementList() != null))
            {
                for (RelatedMetadataElement relatedMetadataElement : relatedMetadataElementList.getElementList())
                {
                    if (propertyHelper.isTypeOf(relatedMetadataElement.getElement(), OpenMetadataType.SOLUTION_COMPONENT.typeName))
                    {
                        portSummaries.add(portConverter.getRelatedElementSummary(RelatedMetadataElementSummary.class, relatedMetadataElement, methodName));
                    }
                }

                startFrom                  = startFrom + invalidParameterHandler.getMaxPagingSize();
                relatedMetadataElementList = openMetadataStoreClient.getRelatedMetadataElements(userId,
                                                                                                solutionComponentGUID,
                                                                                                1,
                                                                                                OpenMetadataType.SOLUTION_COMPONENT_PORT_RELATIONSHIP.typeName,
                                                                                                null,
                                                                                                asOfTime,
                                                                                                null,
                                                                                                SequencingOrder.CREATION_DATE_RECENT,
                                                                                                forLineage,
                                                                                                forDuplicateProcessing,
                                                                                                effectiveTime,
                                                                                                startFrom,
                                                                                                invalidParameterHandler.getMaxPagingSize());
            }

            if (! portSummaries.isEmpty())
            {
                return portSummaries;
            }
        }
        catch (Exception error)
        {
            if (auditLog != null)
            {
                auditLog.logMessage(methodName,
                                    DigitalArchitectureAuditCode.UNEXPECTED_CONVERTER_EXCEPTION.getMessageDefinition(error.getClass().getName(),
                                                                                                                     methodName,
                                                                                                                     error.getMessage()));
            }
        }

        return null;
    }


    /**
     * Return the solution blueprint extracted from the open metadata element.
     *
     * @param userId calling user
     * @param openMetadataElement element extracted from the repository
     * @param asOfTime repository time to use
     * @param effectiveTime effectivity dating for elements
     * @param methodName calling method
     * @return bean or null
     */
    private SolutionBlueprintElement convertSolutionBlueprint(String              userId,
                                                              OpenMetadataElement openMetadataElement,
                                                              Date                asOfTime,
                                                              Date                effectiveTime,
                                                              String              methodName)
    {
        try
        {
            List<SolutionBlueprintComponent> solutionBlueprintComponents = new ArrayList<>();
            OpenMetadataConverterBase<SolutionBlueprintComponent> solutionBlueprintConverter = new OpenMetadataConverterBase<>(propertyHelper, serviceName, serverName);

            int startFrom = 0;
            RelatedMetadataElementList relatedMetadataElementList = openMetadataStoreClient.getRelatedMetadataElements(userId,
                                                                                                                       openMetadataElement.getElementGUID(),
                                                                                                                       1,
                                                                                                                       OpenMetadataType.SOLUTION_BLUEPRINT_COMPOSITION_RELATIONSHIP.typeName,
                                                                                                                       null,
                                                                                                                       asOfTime,
                                                                                                                       null,
                                                                                                                       SequencingOrder.CREATION_DATE_RECENT,
                                                                                                                       forLineage,
                                                                                                                       forDuplicateProcessing,
                                                                                                                       effectiveTime,
                                                                                                                       startFrom,
                                                                                                                       invalidParameterHandler.getMaxPagingSize());
            while ((relatedMetadataElementList != null) && (relatedMetadataElementList.getElementList() != null))
            {
                for (RelatedMetadataElement relatedMetadataElement : relatedMetadataElementList.getElementList())
                {
                    if (relatedMetadataElement != null)
                    {
                        SolutionBlueprintComponent solutionBlueprintComponent = new SolutionBlueprintComponent();

                        solutionBlueprintComponent.setElementHeader(solutionBlueprintConverter.getMetadataElementHeader(SolutionBlueprintComponent.class,
                                                                                                                        relatedMetadataElement,
                                                                                                                        relatedMetadataElement.getRelationshipGUID(),
                                                                                                                        null,
                                                                                                                        methodName));
                        SolutionBlueprintCompositionProperties relationshipProperties = new SolutionBlueprintCompositionProperties();
                        ElementProperties                      elementProperties      = new ElementProperties(relatedMetadataElement.getRelationshipProperties());

                        relationshipProperties.setRole(solutionBlueprintConverter.removeRole(elementProperties));
                        relationshipProperties.setDescription(solutionBlueprintConverter.removeDescription(elementProperties));
                        relationshipProperties.setEffectiveFrom(relatedMetadataElement.getEffectiveFromTime());
                        relationshipProperties.setEffectiveTo(relatedMetadataElement.getEffectiveToTime());
                        relationshipProperties.setExtendedProperties(solutionBlueprintConverter.getRemainingExtendedProperties(elementProperties));

                        solutionBlueprintComponent.setProperties(relationshipProperties);
                        solutionBlueprintComponent.setSolutionComponent(this.convertSolutionComponent(userId, relatedMetadataElement.getElement(), asOfTime, effectiveTime, methodName));
                        solutionBlueprintComponents.add(solutionBlueprintComponent);
                    }
                }

                startFrom = startFrom + invalidParameterHandler.getMaxPagingSize();
                relatedMetadataElementList = openMetadataStoreClient.getRelatedMetadataElements(userId,
                                                                                                openMetadataElement.getElementGUID(),
                                                                                                1,
                                                                                                OpenMetadataType.SOLUTION_BLUEPRINT_COMPOSITION_RELATIONSHIP.typeName,
                                                                                                null,
                                                                                                asOfTime,
                                                                                                null,
                                                                                                SequencingOrder.CREATION_DATE_RECENT,
                                                                                                forLineage,
                                                                                                forDuplicateProcessing,
                                                                                                effectiveTime,
                                                                                                startFrom,
                                                                                                invalidParameterHandler.getMaxPagingSize());
            }

            SolutionBlueprintConverter<SolutionBlueprintElement> converter = new SolutionBlueprintConverter<>(propertyHelper, serviceName, serverName, solutionBlueprintComponents);
            SolutionBlueprintElement solutionBlueprintElement =  converter.getNewBean(SolutionBlueprintElement.class, openMetadataElement, methodName);

            if (solutionBlueprintElement != null)
            {
                SolutionBlueprintMermaidGraphBuilder graphBuilder = new SolutionBlueprintMermaidGraphBuilder(solutionBlueprintElement);

                solutionBlueprintElement.setMermaidGraph(graphBuilder.getMermaidGraph());
            }

            return solutionBlueprintElement;
        }
        catch (Exception error)
        {
            if (auditLog != null)
            {
                auditLog.logMessage(methodName,
                                    DigitalArchitectureAuditCode.UNEXPECTED_CONVERTER_EXCEPTION.getMessageDefinition(error.getClass().getName(),
                                                                                                                     methodName,
                                                                                                                     error.getMessage()));
            }

            return null;
        }
    }


    /**
     * Return the solution role extracted from the open metadata element plus linked solution components.
     *
     * @param userId calling user
     * @param openMetadataElement element extracted from the repository
     * @param asOfTime repository time to use
     * @param effectiveTime effectivity dating for elements
     * @param methodName calling method
     * @return bean or null
     */
    private SolutionRoleElement convertSolutionRole(String              userId,
                                                    OpenMetadataElement openMetadataElement,
                                                    Date                asOfTime,
                                                    Date                effectiveTime,
                                                    String              methodName)
    {
        try
        {
            List<RelatedMetadataElement> relatedMetadataElements = new ArrayList<>();

            int startFrom = 0;
            RelatedMetadataElementList relatedMetadataElementList = openMetadataStoreClient.getRelatedMetadataElements(userId,
                                                                                                                       openMetadataElement.getElementGUID(),
                                                                                                                       1,
                                                                                                                       OpenMetadataType.SOLUTION_COMPONENT_ACTOR_RELATIONSHIP.typeName,
                                                                                                                       null,
                                                                                                                       asOfTime,
                                                                                                                       null,
                                                                                                                       SequencingOrder.CREATION_DATE_RECENT,
                                                                                                                       forLineage,
                                                                                                                       forDuplicateProcessing,
                                                                                                                       effectiveTime,
                                                                                                                       startFrom,
                                                                                                                       invalidParameterHandler.getMaxPagingSize());
            while ((relatedMetadataElementList != null) && (relatedMetadataElementList.getElementList() != null))
            {
                relatedMetadataElements.addAll(relatedMetadataElementList.getElementList());

                startFrom = startFrom + invalidParameterHandler.getMaxPagingSize();
                relatedMetadataElementList = openMetadataStoreClient.getRelatedMetadataElements(userId,
                                                                                                openMetadataElement.getElementGUID(),
                                                                                                1,
                                                                                                OpenMetadataType.SOLUTION_COMPONENT_ACTOR_RELATIONSHIP.typeName,
                                                                                                null,
                                                                                                asOfTime,
                                                                                                null,
                                                                                                SequencingOrder.CREATION_DATE_RECENT,
                                                                                                forLineage,
                                                                                                forDuplicateProcessing,
                                                                                                effectiveTime,
                                                                                                startFrom,
                                                                                                invalidParameterHandler.getMaxPagingSize());
            }

            SolutionRoleConverter<SolutionRoleElement> converter = new SolutionRoleConverter<>(propertyHelper, serviceName, serverName);
            SolutionRoleElement solutionRoleElement =  converter.getNewComplexBean(SolutionRoleElement.class,
                                                                                   openMetadataElement,
                                                                                   relatedMetadataElements,
                                                                                   methodName);
            if (solutionRoleElement != null)
            {
                SolutionRoleMermaidGraphBuilder graphBuilder = new SolutionRoleMermaidGraphBuilder(solutionRoleElement);

                solutionRoleElement.setMermaidGraph(graphBuilder.getMermaidGraph());
            }

            return solutionRoleElement;
        }
        catch (Exception error)
        {
            if (auditLog != null)
            {
                auditLog.logMessage(methodName,
                                    DigitalArchitectureAuditCode.UNEXPECTED_CONVERTER_EXCEPTION.getMessageDefinition(error.getClass().getName(),
                                                                                                                     methodName,
                                                                                                                     error.getMessage()));
            }

            return null;
        }
    }


    /**
     * Return the solution component extracted from the open metadata element.
     *
     * @param userId calling user
     * @param openMetadataElement element extracted from the repository
     * @param asOfTime repository time to use
     * @param effectiveTime effectivity dating for elements
     * @param methodName calling method
     * @return bean or null
     */
    private SolutionComponentElement convertSolutionComponent(String              userId,
                                                              OpenMetadataElement openMetadataElement,
                                                              Date                asOfTime,
                                                              Date                effectiveTime,
                                                              String              methodName)
    {
        try
        {
            List<RelatedMetadataElement>   relatedMetadataElements      = new ArrayList<>();
            List<SolutionPortElement>      relatedPortElements          = new ArrayList<>();
            List<SolutionComponentElement> relatedSubComponentsElements = new ArrayList<>();

            int startFrom = 0;
            RelatedMetadataElementList relatedMetadataElementList = openMetadataStoreClient.getRelatedMetadataElements(userId,
                                                                                                                       openMetadataElement.getElementGUID(),
                                                                                                                       0,
                                                                                                                       null,
                                                                                                                       null,
                                                                                                                       asOfTime,
                                                                                                                       null,
                                                                                                                       SequencingOrder.CREATION_DATE_RECENT,
                                                                                                                       forLineage,
                                                                                                                       forDuplicateProcessing,
                                                                                                                       effectiveTime,
                                                                                                                       startFrom,
                                                                                                                       invalidParameterHandler.getMaxPagingSize());
            while ((relatedMetadataElementList != null) && (relatedMetadataElementList.getElementList() != null))
            {
                for (RelatedMetadataElement relatedMetadataElement : relatedMetadataElementList.getElementList())
                {
                    if (relatedMetadataElement != null)
                    {
                        if (propertyHelper.isTypeOf(relatedMetadataElement, OpenMetadataType.SOLUTION_COMPOSITION_RELATIONSHIP.typeName))
                        {
                            if (!relatedMetadataElement.getElementAtEnd1())
                            {
                                relatedSubComponentsElements.add(this.convertSolutionComponent(userId, relatedMetadataElement.getElement(), asOfTime, effectiveTime, methodName));
                            }
                        }
                        else if (propertyHelper.isTypeOf(relatedMetadataElement, OpenMetadataType.SOLUTION_COMPONENT_PORT_RELATIONSHIP.typeName))
                        {
                            relatedPortElements.add(this.convertSolutionPort(userId, relatedMetadataElement, asOfTime, effectiveTime, methodName));
                        }
                        else
                        {
                            relatedMetadataElements.add(relatedMetadataElement);
                        }
                    }
                }

                startFrom = startFrom + invalidParameterHandler.getMaxPagingSize();
                relatedMetadataElementList = openMetadataStoreClient.getRelatedMetadataElements(userId,
                                                                                                openMetadataElement.getElementGUID(),
                                                                                                0,
                                                                                                null,
                                                                                                null,
                                                                                                asOfTime,
                                                                                                null,
                                                                                                SequencingOrder.CREATION_DATE_RECENT,
                                                                                                forLineage,
                                                                                                forDuplicateProcessing,
                                                                                                effectiveTime,
                                                                                                startFrom,
                                                                                                invalidParameterHandler.getMaxPagingSize());
            }

            SolutionComponentConverter<SolutionComponentElement> converter = new SolutionComponentConverter<>(propertyHelper, serviceName, serverName,relatedSubComponentsElements, relatedPortElements);
            return converter.getNewComplexBean(SolutionComponentElement.class,
                                               openMetadataElement,
                                               relatedMetadataElements,
                                               methodName);
        }
        catch (Exception error)
        {
            if (auditLog != null)
            {
                auditLog.logMessage(methodName,
                                    DigitalArchitectureAuditCode.UNEXPECTED_CONVERTER_EXCEPTION.getMessageDefinition(error.getClass().getName(),
                                                                                                                     methodName,
                                                                                                                     error.getMessage()));
            }

            return null;
        }
    }


    /**
     * Return the solution port extracted from the open metadata element.
     *
     * @param userId calling user
     * @param openMetadataElement element extracted from the repository
     * @param asOfTime repository time to use
     * @param effectiveTime effectivity dating for elements
     * @param methodName calling method
     * @return bean or null
     */
    private SolutionPortElement convertSolutionPort(String                 userId,
                                                    RelatedMetadataElement openMetadataElement,
                                                    Date                   asOfTime,
                                                    Date                   effectiveTime,
                                                    String                 methodName)
    {
        // todo
        return null;
    }


    /**
     * Return the solution component implementation extracted from the open metadata element.
     *
     * @param relatedMetadataElement element extracted from the repository
     * @param methodName calling method
     * @return bean or null
     */
    private RelatedMetadataElementSummary convertSolutionComponentImplementation(RelatedMetadataElement relatedMetadataElement,
                                                                                 String                 methodName)
    {
        RelatedMetadataElementSummaryConverter<RelatedMetadataElementSummary> converter = new RelatedMetadataElementSummaryConverter<>(propertyHelper,
                                                                                                                                       serviceName,
                                                                                                                                       serverName);
        try
        {
            return converter.getRelatedElementSummary(RelatedMetadataElementSummary.class,
                                                      relatedMetadataElement,
                                                      methodName);
        }
        catch (PropertyServerException error)
        {
            if (auditLog != null)
            {
                auditLog.logMessage(methodName,
                                    DigitalArchitectureAuditCode.UNEXPECTED_CONVERTER_EXCEPTION.getMessageDefinition(error.getClass().getName(),
                                                                                                                     methodName,
                                                                                                                     error.getMessage()));
            }

            return null;
        }
    }
}
