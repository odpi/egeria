/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.bitol.mapping;

import org.odpi.openmetadata.frameworks.integration.bitol.common.BitolCustomProperty;
import org.odpi.openmetadata.frameworks.integration.bitol.common.BitolDocument;
import org.odpi.openmetadata.frameworks.integration.bitol.odps.DataProduct;
import org.odpi.openmetadata.frameworks.integration.bitol.odps.DataProductType;
import org.odpi.openmetadata.frameworks.integration.bitol.odps.DataProductInputContract;
import org.odpi.openmetadata.frameworks.integration.bitol.odps.DataProductInputPort;
import org.odpi.openmetadata.frameworks.integration.bitol.odps.DataProductManagementPort;
import org.odpi.openmetadata.frameworks.integration.bitol.odps.DataProductOutputPort;
import org.odpi.openmetadata.frameworks.integration.bitol.odps.DataProductSBOM;
import org.odpi.openmetadata.frameworks.integration.ffdc.OIFErrorCode;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextBase;
import org.odpi.openmetadata.frameworks.openmetadata.enums.SolutionPortDirection;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.EndpointProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.AgreementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.DigitalProductProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionPortProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * DataProductGenerator builds an Open Data Product Standard (ODPS) document from a DigitalProduct in open metadata.
 * It is the reverse of DataProductMapper: the product's solution component ports become input and output ports (with
 * the data contract identifier taken from the agreement each port is an item of, or from the identifier recorded when
 * the port was catalogued), each process that the lineage shows loading the product's data becomes an input port too,
 * the endpoints listed as its resources become management ports, and the catalog,
 * organization, keywords, contact details, external references and scoped roles become the domain, tenant, tags,
 * support, authoritative definitions and team.
 */
public class DataProductGenerator extends BitolGeneratorBase
{
    /*
     * How many DataSetContent and CollectionMembership relationships are followed from a product's member asset when
     * looking for the processes that assemble its data - enough to reach the tables beneath the tabular data sets of
     * a tabular data set collection.
     */
    private static final int maxContentDepth = 2;

    /**
     * Constructor.
     *
     * @param context connector context providing access to the open metadata clients
     */
    public DataProductGenerator(ConnectorContextBase context)
    {
        super(context);
    }


    /**
     * Generate the ODPS document for a digital product.
     *
     * @param digitalProductGUID unique identifier of the DigitalProduct element
     * @return document
     * @throws InvalidParameterException the element is not a digital product
     * @throws PropertyServerException problem with the metadata store
     * @throws UserNotAuthorizedException security problem
     */
    public DataProduct generateDataProduct(String digitalProductGUID) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        OpenMetadataRootElement product = context.getCollectionClient().getCollectionByGUID(digitalProductGUID, context.getCollectionClient().getGetOptions());

        return generateDataProduct(product);
    }


    /**
     * Generate the ODPS document for a digital product that has already been retrieved with its related elements.
     *
     * @param product DigitalProduct element
     * @return document
     * @throws InvalidParameterException the element is not a digital product
     * @throws PropertyServerException problem with the metadata store
     * @throws UserNotAuthorizedException security problem
     */
    public DataProduct generateDataProduct(OpenMetadataRootElement product) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException
    {
        final String methodName = "generateDataProduct";

        if ((product == null) || (! propertyHelper.isTypeOf(product.getElementHeader(), OpenMetadataType.DIGITAL_PRODUCT.typeName)))
        {
            throw new InvalidParameterException(OIFErrorCode.WRONG_ELEMENT_TYPE_FOR_BITOL_DOCUMENT.getMessageDefinition(getGUID(product),
                                                                                                                methodName,
                                                                                                                getTypeName(product),
                                                                                                                BitolDocument.DATA_PRODUCT_KIND,
                                                                                                                OpenMetadataType.DIGITAL_PRODUCT.typeName),
                                                this.getClass().getName(),
                                                methodName,
                                                "product");
        }

        DataProduct dataProduct = new DataProduct();

        fillFundamentals(product, dataProduct);

        if (product.getProperties() instanceof DigitalProductProperties properties)
        {
            if (properties.getProductName() != null)
            {
                dataProduct.setName(properties.getProductName());
            }

            if ((properties.getPurpose() != null) && (dataProduct.getDescription() != null))
            {
                dataProduct.getDescription().setPurpose(properties.getPurpose());
            }

            /*
             * A product created in Egeria has no stored creation timestamp or type, so its introduction date is used
             * for the timestamp, and its category when it is one of the ODPS product types.
             */
            String productCreatedTs = getBitolValue(properties.getAdditionalProperties(), "productCreatedTs");

            if ((productCreatedTs == null) && (properties.getIntroductionDate() != null))
            {
                productCreatedTs = properties.getIntroductionDate().toInstant().toString();
            }

            String type = getBitolValue(properties.getAdditionalProperties(), "type");

            if ((type == null) && (DataProductType.fromValue(properties.getCategory()) != null))
            {
                type = DataProductType.fromValue(properties.getCategory()).getValue();
            }

            dataProduct.setProductCreatedTs(productCreatedTs);
            dataProduct.setType(type);
            dataProduct.setDeprecated(getDeprecated(properties.getAdditionalProperties()));
            dataProduct.setSynonyms(getSynonyms(properties.getAdditionalProperties()));
            dataProduct.setCustomProperties(addEgeriaProperties(product, properties, dataProduct.getCustomProperties()));
        }

        if (dataProduct.getDomain() == null)
        {
            dataProduct.setDomain(getDomain(product, new ArrayList<>()));
        }

        dataProduct.setSupport(getSupportChannels(product));
        dataProduct.setTeam(getTeam(product));
        dataProduct.setManagementPorts(getManagementPorts(product));

        fillPorts(product, dataProduct);

        return dataProduct;
    }


    /**
     * Add the properties of the product that ODPS has no place for as custom properties attributed to Egeria.  The
     * lifecycle properties are restored if the document is catalogued again; the product families, guiding
     * questions and license types describe relationships in open metadata and are written for information.
     *
     * @param product product element
     * @param properties product properties
     * @param customProperties custom properties already in the document (may be null)
     * @return custom properties (may be null)
     */
    private List<BitolCustomProperty> addEgeriaProperties(OpenMetadataRootElement   product,
                                                          DigitalProductProperties  properties,
                                                          List<BitolCustomProperty> customProperties)
    {
        List<BitolCustomProperty> results = customProperties;

        String deploymentStatus = (properties.getDeploymentStatus() == null) ? null : properties.getDeploymentStatus().name();

        results = addEgeriaProperty(results, BitolMapperBase.EGERIA_CATEGORY, properties.getCategory(), "Category of the product in Egeria's digital product catalog.");
        results = addEgeriaProperty(results, BitolMapperBase.EGERIA_DEPLOYMENT_STATUS, deploymentStatus, "Status of the product's implementation, separate from the status of its description.");
        results = addEgeriaProperty(results, BitolMapperBase.EGERIA_USER_DEPLOYMENT_STATUS, properties.getUserDefinedDeploymentStatus(), "Locally defined deployment status.");
        results = addEgeriaProperty(results, BitolMapperBase.EGERIA_MATURITY, properties.getMaturity(), "Level of maturity of the product.");
        results = addEgeriaProperty(results, BitolMapperBase.EGERIA_SERVICE_LIFE, properties.getServiceLife(), "Length of time that the product is expected to be in service.");
        results = addEgeriaProperty(results, BitolMapperBase.EGERIA_INTRODUCTION_DATE, properties.getIntroductionDate(), "Date that the product was made available.");
        results = addEgeriaProperty(results, BitolMapperBase.EGERIA_NEXT_VERSION_DATE, properties.getNextVersionDate(), "Date when the next version is expected to be released.");
        results = addEgeriaProperty(results, BitolMapperBase.EGERIA_WITHDRAW_DATE, properties.getWithdrawDate(), "Date when the product is expected to be (or was) withdrawn.");
        results = addEgeriaProperty(results,
                                    BitolMapperBase.EGERIA_PRODUCT_FAMILIES,
                                    getRelatedNames(product.getMemberOfCollections(), OpenMetadataType.DIGITAL_PRODUCT_FAMILY.typeName),
                                    "Product families that include this product; subscribing to a family delivers the data from all of its products.");
        results = addEgeriaProperty(results,
                                    BitolMapperBase.EGERIA_QUESTIONS,
                                    getRelatedNames(product.getSupplementaryProperties(), OpenMetadataType.REFERENCEABLE.typeName),
                                    "Questions that the product is designed to answer.");
        results = addEgeriaProperty(results,
                                    BitolMapperBase.EGERIA_LICENSE_TYPES,
                                    getRelatedNames(product.getGovernedBy(), OpenMetadataType.LICENSE_TYPE.typeName),
                                    "License types granted to the subscribers' copies of the product data.");

        return results;
    }


    /**
     * Return the domain from the digital product catalog that the product is a member of.  A product that is a
     * member of a product family takes the domain of the catalog that the family belongs to.
     *
     * @param product product (or product family) element
     * @param visitedGUIDs elements already visited, to stop a loop in the collection memberships
     * @return catalog name or null
     */
    private String getDomain(OpenMetadataRootElement product,
                             List<String>            visitedGUIDs)
    {
        if (product.getMemberOfCollections() != null)
        {
            visitedGUIDs.add(product.getElementHeader().getGUID());

            for (RelatedMetadataElementSummary collection : product.getMemberOfCollections())
            {
                if ((collection != null) && (collection.getRelatedElement() != null) &&
                    (propertyHelper.isTypeOf(collection.getRelatedElement().getElementHeader(), OpenMetadataType.DIGITAL_PRODUCT_CATALOG.typeName)))
                {
                    return getDisplayName(collection.getRelatedElement());
                }
            }

            for (RelatedMetadataElementSummary collection : product.getMemberOfCollections())
            {
                if ((collection != null) && (collection.getRelatedElement() != null) &&
                    (propertyHelper.isTypeOf(collection.getRelatedElement().getElementHeader(), OpenMetadataType.DIGITAL_PRODUCT_FAMILY.typeName)) &&
                    (! visitedGUIDs.contains(collection.getRelatedElement().getElementHeader().getGUID())))
                {
                    try
                    {
                        OpenMetadataRootElement family = context.getCollectionClient().getCollectionByGUID(collection.getRelatedElement().getElementHeader().getGUID(),
                                                                                                           context.getCollectionClient().getGetOptions());

                        if (family != null)
                        {
                            String domain = getDomain(family, visitedGUIDs);

                            if (domain != null)
                            {
                                return domain;
                            }
                        }
                    }
                    catch (Exception error)
                    {
                        /*
                         * The domain is optional so a family that can not be retrieved is skipped.
                         */
                    }
                }
            }
        }

        return null;
    }


    /**
     * Return the endpoints listed as resources of the product as management ports.
     *
     * @param product product element
     * @return list of management ports or null
     */
    private List<DataProductManagementPort> getManagementPorts(OpenMetadataRootElement product)
    {
        if (product.getResourceList() == null)
        {
            return null;
        }

        List<DataProductManagementPort> managementPorts = new ArrayList<>();

        for (RelatedMetadataElementSummary resource : product.getResourceList())
        {
            if ((resource != null) && (resource.getRelatedElement() != null) &&
                (resource.getRelatedElement().getProperties() instanceof EndpointProperties endpoint))
            {
                DataProductManagementPort managementPort = new DataProductManagementPort();

                managementPort.setName(endpoint.getDisplayName());
                managementPort.setDescription(endpoint.getDescription());
                managementPort.setUrl(endpoint.getNetworkAddress());
                managementPort.setType(endpoint.getProtocol());
                managementPort.setContent((getBitolValue(endpoint.getAdditionalProperties(), "content") != null) ? getBitolValue(endpoint.getAdditionalProperties(), "content") : endpoint.getCategory());
                managementPort.setChannel(getBitolValue(endpoint.getAdditionalProperties(), "channel"));
                managementPort.setId(getBitolValue(endpoint.getAdditionalProperties(), "id"));
                managementPort.setDeprecated(getDeprecated(endpoint.getAdditionalProperties()));
                managementPort.setCustomProperties(getCustomProperties(endpoint.getAdditionalProperties()));

                if (managementPort.getName() != null)
                {
                    managementPorts.add(managementPort);
                }
            }
        }

        return managementPorts.isEmpty() ? null : managementPorts;
    }


    /**
     * Fill the input and output ports from the solution ports of the solution components that are members of the
     * product, then add an input port for each process that the lineage shows assembling the product's data.
     *
     * @param product product element
     * @param dataProduct document to fill
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException problem with the metadata store
     * @throws UserNotAuthorizedException security problem
     */
    private void fillPorts(OpenMetadataRootElement product,
                           DataProduct             dataProduct) throws InvalidParameterException,
                                                                       PropertyServerException,
                                                                       UserNotAuthorizedException
    {
        if (product.getCollectionMembers() == null)
        {
            return;
        }

        List<DataProductInputPort>  inputPorts  = new ArrayList<>();
        List<DataProductOutputPort> outputPorts = new ArrayList<>();

        for (RelatedMetadataElementSummary member : product.getCollectionMembers())
        {
            if ((member != null) && (member.getRelatedElement() != null) &&
                (propertyHelper.isTypeOf(member.getRelatedElement().getElementHeader(), OpenMetadataType.SOLUTION_COMPONENT.typeName)))
            {
                OpenMetadataRootElement component = context.getSolutionComponentClient().getSolutionComponentByGUID(member.getRelatedElement().getElementHeader().getGUID(),
                                                                                                                    context.getSolutionComponentClient().getGetOptions());

                if ((component != null) && (component.getSolutionPorts() != null))
                {
                    for (RelatedMetadataElementSummary portSummary : component.getSolutionPorts())
                    {
                        if ((portSummary != null) && (portSummary.getRelatedElement() != null))
                        {
                            OpenMetadataRootElement port = context.getSolutionPortClient().getSolutionPortByGUID(portSummary.getRelatedElement().getElementHeader().getGUID(),
                                                                                                                 context.getSolutionPortClient().getGetOptions());

                            if ((port != null) && (port.getProperties() instanceof SolutionPortProperties portProperties))
                            {
                                if (portProperties.getDirection() == SolutionPortDirection.INPUT)
                                {
                                    inputPorts.add(getInputPort(port, portProperties));
                                }
                                else
                                {
                                    outputPorts.add(getOutputPort(port, portProperties));
                                }
                            }
                        }
                    }
                }
            }
        }

        addAssemblingProcessPorts(product, inputPorts);

        dataProduct.setInputPorts(inputPorts.isEmpty() ? null : inputPorts);
        dataProduct.setOutputPorts(outputPorts.isEmpty() ? null : outputPorts);
    }


    /**
     * Build an input port from a solution port.
     *
     * @param port port element
     * @param portProperties port properties
     * @return input port
     */
    private DataProductInputPort getInputPort(OpenMetadataRootElement port,
                                              SolutionPortProperties  portProperties)
    {
        DataProductInputPort inputPort = new DataProductInputPort();

        inputPort.setId(getBitolValue(portProperties.getAdditionalProperties(), "id"));
        inputPort.setDeprecated(getDeprecated(portProperties.getAdditionalProperties()));
        inputPort.setName(portProperties.getDisplayName());
        inputPort.setVersion((portProperties.getVersionIdentifier() != null) ? portProperties.getVersionIdentifier() : "1.0.0");
        inputPort.setContractId(getContractId(port, portProperties.getAdditionalProperties()));
        inputPort.setTags(getTags(port));
        inputPort.setAuthoritativeDefinitions(getAuthoritativeDefinitions(port));
        inputPort.setCustomProperties(getCustomProperties(portProperties.getAdditionalProperties()));

        return inputPort;
    }


    /**
     * Add an input port for each process that assembles the product's data: a process with data lineage (DataFlow,
     * DataMapping or LineageMapping) into one of the product's member assets, or into the content of a member data
     * set.  The members of a product are often abstract data sets, such as a TabularDataSetCollection, linked by
     * DataSetContent to the physical elements beneath them, and the lineage from the processes that load the data -
     * for example the tasks of a pipeline catalogued from OpenLineage events - is attached to those physical
     * elements.  A process that already has an input port of the same name is skipped, so a product catalogued from
     * a document generated here does not gain a second port for it.
     *
     * @param product product element
     * @param inputPorts input ports found so far - added to
     */
    private void addAssemblingProcessPorts(OpenMetadataRootElement    product,
                                           List<DataProductInputPort> inputPorts)
    {
        Map<String, RelatedMetadataElementSummary> processes = new LinkedHashMap<>();
        Map<String, List<String>>                  assembled = new HashMap<>();

        findAssemblingProcesses(product, processes, assembled);

        Set<String> portNames = new HashSet<>();

        for (DataProductInputPort inputPort : inputPorts)
        {
            portNames.add(inputPort.getName());
        }

        for (String processGUID : processes.keySet())
        {
            DataProductInputPort inputPort = getProcessInputPort(processes.get(processGUID), assembled.get(processGUID));

            if ((inputPort != null) && (portNames.add(inputPort.getName())))
            {
                inputPorts.add(inputPort);
            }
        }
    }


    /**
     * Return the unique identifiers of the processes that assemble a product's data: those with data lineage into one
     * of the product's member assets, or into the content of a member data set.  Each becomes an input port of the
     * product's ODPS document.
     *
     * @param product product element
     * @return list of process GUIDs (empty if none)
     */
    public List<String> getAssemblingProcessGUIDs(OpenMetadataRootElement product)
    {
        Map<String, RelatedMetadataElementSummary> processes = new LinkedHashMap<>();

        findAssemblingProcesses(product, processes, new HashMap<>());

        return new ArrayList<>(processes.keySet());
    }


    /**
     * Find the processes with data lineage into the product's member assets.
     *
     * @param product product element
     * @param processes processes found, by GUID - added to
     * @param assembled qualified names of the elements each process supplies, by process GUID - added to
     */
    private void findAssemblingProcesses(OpenMetadataRootElement                    product,
                                         Map<String, RelatedMetadataElementSummary> processes,
                                         Map<String, List<String>>                  assembled)
    {
        if ((product == null) || (product.getCollectionMembers() == null))
        {
            return;
        }

        Set<String> visited = new HashSet<>();

        for (RelatedMetadataElementSummary member : product.getCollectionMembers())
        {
            if ((member != null) && (member.getRelatedElement() != null) &&
                (propertyHelper.isTypeOf(member.getRelatedElement().getElementHeader(), OpenMetadataType.ASSET.typeName)))
            {
                findAssemblingProcesses(member.getRelatedElement().getElementHeader(), 0, visited, processes, assembled);
            }
        }
    }


    /**
     * Find the processes with data lineage into an element, then repeat for its content (if it is a data set) and
     * its members (if it is a collection of data sets).
     *
     * @param elementHeader header of the element
     * @param depth number of DataSetContent or CollectionMembership relationships followed to reach the element
     * @param visited GUIDs of the elements already examined - added to
     * @param processes processes found, by GUID - added to
     * @param assembled qualified names of the elements each process supplies, by process GUID - added to
     */
    private void findAssemblingProcesses(ElementHeader                              elementHeader,
                                         int                                        depth,
                                         Set<String>                                visited,
                                         Map<String, RelatedMetadataElementSummary> processes,
                                         Map<String, List<String>>                  assembled)
    {
        if ((elementHeader == null) || (depth > maxContentDepth) || (! visited.add(elementHeader.getGUID())))
        {
            return;
        }

        OpenMetadataRootElement element;

        try
        {
            if (propertyHelper.isTypeOf(elementHeader, OpenMetadataType.SCHEMA_ELEMENT.typeName))
            {
                element = context.getSchemaAttributeClient().getSchemaAttributeByGUID(elementHeader.getGUID(),
                                                                                      context.getSchemaAttributeClient().getGetOptions());
            }
            else
            {
                element = context.getAssetClient().getAssetByGUID(elementHeader.getGUID(),
                                                                  context.getAssetClient().getGetOptions());
            }
        }
        catch (Exception error)
        {
            /*
             * The element cannot be read with this user's security; its lineage is left out.
             */
            return;
        }

        if (element == null)
        {
            return;
        }

        if (element.getLineageLinkage() != null)
        {
            String elementName = (element.getProperties() instanceof ReferenceableProperties properties) ? properties.getQualifiedName() : elementHeader.getGUID();

            for (RelatedMetadataElementSummary lineage : element.getLineageLinkage())
            {
                /*
                 * Data flows from end 1 to end 2 of these relationships, so the supplier is at end 1.
                 */
                if ((lineage != null) && (lineage.getRelatedElement() != null) && (lineage.getRelatedElementAtEnd1()) &&
                    (lineage.getRelationshipHeader() != null) &&
                    (BitolMapperBase.DATA_LINEAGE_TYPE_NAMES.contains(lineage.getRelationshipHeader().getType().getTypeName())) &&
                    (propertyHelper.isTypeOf(lineage.getRelatedElement().getElementHeader(), OpenMetadataType.PROCESS.typeName)))
                {
                    String processGUID = lineage.getRelatedElement().getElementHeader().getGUID();

                    processes.putIfAbsent(processGUID, lineage);

                    List<String> elementNames = assembled.computeIfAbsent(processGUID, guid -> new ArrayList<>());

                    if (! elementNames.contains(elementName))
                    {
                        elementNames.add(elementName);
                    }
                }
            }
        }

        if (element.getDataSetContent() != null)
        {
            for (RelatedMetadataElementSummary content : element.getDataSetContent())
            {
                if ((content != null) && (content.getRelatedElement() != null))
                {
                    findAssemblingProcesses(content.getRelatedElement().getElementHeader(), depth + 1, visited, processes, assembled);
                }
            }
        }

        if (element.getCollectionMembers() != null)
        {
            for (RelatedMetadataElementSummary member : element.getCollectionMembers())
            {
                if ((member != null) && (member.getRelatedElement() != null) &&
                    (propertyHelper.isTypeOf(member.getRelatedElement().getElementHeader(), OpenMetadataType.DATA_SET.typeName)))
                {
                    findAssemblingProcesses(member.getRelatedElement().getElementHeader(), depth + 1, visited, processes, assembled);
                }
            }
        }
    }


    /**
     * Build an input port for a process that assembles the product's data.  The data contract identifier comes from
     * an agreement that the process is an item of, or from the identifier recorded in its additional properties;
     * the process's qualified name and type, and the elements it supplies, are recorded as Egeria custom properties.
     *
     * @param processSummary the lineage relationship to the process
     * @param assembled qualified names of the elements the process supplies
     * @return input port, or null if the process cannot be read
     */
    private DataProductInputPort getProcessInputPort(RelatedMetadataElementSummary processSummary,
                                                     List<String>                  assembled)
    {
        ElementHeader           processHeader = processSummary.getRelatedElement().getElementHeader();
        OpenMetadataRootElement process;

        try
        {
            process = context.getAssetClient().getAssetByGUID(processHeader.getGUID(), context.getAssetClient().getGetOptions());
        }
        catch (Exception error)
        {
            return null;
        }

        if ((process == null) || (! (process.getProperties() instanceof ReferenceableProperties processProperties)))
        {
            return null;
        }

        DataProductInputPort inputPort            = new DataProductInputPort();
        Map<String, String>  additionalProperties = processProperties.getAdditionalProperties();

        inputPort.setId(getBitolValue(additionalProperties, "id"));
        inputPort.setName((processProperties.getDisplayName() != null) ? processProperties.getDisplayName() : processProperties.getQualifiedName());
        inputPort.setVersion((processProperties.getVersionIdentifier() != null) ? processProperties.getVersionIdentifier() : "1.0.0");
        inputPort.setContractId(getContractId(process, additionalProperties));
        inputPort.setTags(getTags(process));
        inputPort.setAuthoritativeDefinitions(getAuthoritativeDefinitions(process));

        List<BitolCustomProperty> customProperties = getCustomProperties(additionalProperties);

        customProperties = addEgeriaProperty(customProperties,
                                             BitolMapperBase.EGERIA_PROCESS_QUALIFIED_NAME,
                                             processProperties.getQualifiedName(),
                                             "The process in open metadata that assembles the data received through this port.");
        customProperties = addEgeriaProperty(customProperties,
                                             BitolMapperBase.EGERIA_PROCESS_TYPE,
                                             processHeader.getType().getTypeName(),
                                             "The open metadata type of the process.");
        customProperties = addEgeriaProperty(customProperties,
                                             BitolMapperBase.EGERIA_ASSEMBLES,
                                             assembled,
                                             "The product's data sets, or the physical elements beneath them, that the process supplies.");

        inputPort.setCustomProperties(customProperties);

        return inputPort;
    }


    /**
     * Build an output port from a solution port.
     *
     * @param port port element
     * @param portProperties port properties
     * @return output port
     */
    private DataProductOutputPort getOutputPort(OpenMetadataRootElement port,
                                                SolutionPortProperties  portProperties)
    {
        DataProductOutputPort outputPort           = new DataProductOutputPort();
        Map<String, String>   additionalProperties = portProperties.getAdditionalProperties();

        outputPort.setId(getBitolValue(additionalProperties, "id"));
        outputPort.setDeprecated(getDeprecated(additionalProperties));
        outputPort.setSynonyms(getSynonyms(additionalProperties));
        outputPort.setContext(getContext(additionalProperties));
        outputPort.setName(portProperties.getDisplayName());
        outputPort.setDescription(portProperties.getDescription());
        outputPort.setVersion((portProperties.getVersionIdentifier() != null) ? portProperties.getVersionIdentifier() : "1.0.0");
        outputPort.setType(getBitolValue(additionalProperties, "type"));
        outputPort.setContractId(getContractId(port, additionalProperties));
        outputPort.setTags(getTags(port));
        outputPort.setAuthoritativeDefinitions(getAuthoritativeDefinitions(port));
        outputPort.setCustomProperties(getCustomProperties(additionalProperties));

        String sbomJSON = getBitolValue(additionalProperties, "sbom");

        if (sbomJSON != null)
        {
            List<DataProductSBOM> sboms = fromJSONList(sbomJSON, DataProductSBOM.class);

            if (sboms == null)
            {
                /*
                 * Products catalogued before the SBOM list was kept as JSON hold a comma-separated list of URLs.
                 */
                sboms = new ArrayList<>();

                for (String url : sbomJSON.split(",\\s*"))
                {
                    if (! url.isBlank())
                    {
                        DataProductSBOM sbom = new DataProductSBOM();

                        sbom.setType("external");
                        sbom.setUrl(url.trim());
                        sboms.add(sbom);
                    }
                }
            }

            outputPort.setSbom(sboms.isEmpty() ? null : sboms);
        }

        String inputContracts = getBitolValue(additionalProperties, "inputContracts");

        if (inputContracts != null)
        {
            List<DataProductInputContract> contracts = new ArrayList<>();

            for (String reference : inputContracts.split(",\\s*"))
            {
                String[] parts = reference.trim().split("@", 2);

                if (! parts[0].isBlank())
                {
                    DataProductInputContract contract = new DataProductInputContract();

                    contract.setId(parts[0]);
                    contract.setVersion((parts.length > 1) ? parts[1] : null);
                    contracts.add(contract);
                }
            }

            outputPort.setInputContracts(contracts.isEmpty() ? null : contracts);
        }

        return outputPort;
    }


    /**
     * Return the identifier of the data contract for a port: the identifier of the agreement the port (or the
     * process behind it) is an item of, falling back to the identifier recorded when the port was catalogued.
     *
     * @param port port or process element
     * @param additionalProperties additional properties of the element
     * @return contract identifier or null
     */
    private String getContractId(OpenMetadataRootElement port,
                                 Map<String, String>     additionalProperties)
    {
        if (port.getAgreementContents() != null)
        {
            for (RelatedMetadataElementSummary agreement : port.getAgreementContents())
            {
                if ((agreement != null) && (agreement.getRelatedElement() != null) &&
                    (agreement.getRelatedElement().getProperties() instanceof AgreementProperties agreementProperties))
                {
                    if (agreementProperties.getIdentifier() != null)
                    {
                        return agreementProperties.getIdentifier();
                    }

                    return agreement.getRelatedElement().getElementHeader().getGUID();
                }
            }
        }

        if (port.getInvolvedInAgreements() != null)
        {
            for (RelatedMetadataElementSummary agreement : port.getInvolvedInAgreements())
            {
                if ((agreement != null) && (agreement.getRelatedElement() != null) &&
                    (agreement.getRelatedElement().getProperties() instanceof ReferenceableProperties agreementProperties) &&
                    (agreementProperties.getIdentifier() != null))
                {
                    return agreementProperties.getIdentifier();
                }
            }
        }

        return getBitolValue(additionalProperties, "contractId");
    }
}
