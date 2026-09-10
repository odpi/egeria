/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.bitol.mapping;

import org.odpi.openmetadata.frameworks.integration.bitol.odps.DataProductManagementPortContent;
import org.odpi.openmetadata.frameworks.integration.bitol.odps.DataProductManagementPortType;
import org.odpi.openmetadata.frameworks.integration.bitol.odps.DataProductType;
import org.odpi.openmetadata.frameworks.integration.bitol.common.BitolDocument;
import org.odpi.openmetadata.frameworks.integration.bitol.odps.DataProduct;
import org.odpi.openmetadata.frameworks.integration.bitol.odps.DataProductInputContract;
import org.odpi.openmetadata.frameworks.integration.bitol.odps.DataProductInputPort;
import org.odpi.openmetadata.frameworks.integration.bitol.odps.DataProductManagementPort;
import org.odpi.openmetadata.frameworks.integration.bitol.odps.DataProductOutputPort;
import org.odpi.openmetadata.frameworks.integration.bitol.odps.DataProductSBOM;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.CollectionClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextBase;
import org.odpi.openmetadata.frameworks.openmetadata.enums.SolutionPortDirection;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionMembershipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.EndpointProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.AgreementItemProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.DigitalProductCatalogProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.DigitalProductProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.resources.ResourceListProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionComponentPortProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionComponentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionPortProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DataProductMapper catalogues an Open Data Product Standard (ODPS) document as a DigitalProduct in open metadata.
 * <ul>
 *     <li>The document becomes a DigitalProduct (one element per version, qualifiedName DataProduct::{id}::{version})
 *         whose identifier is also recorded as an ExternalId shared by all versions.</li>
 *     <li>The document's status maps onto the product's content status; a retired product is deleted.</li>
 *     <li>The domain becomes a DigitalProductCatalog that the product is a member of; the tenant becomes an
 *         Organization that the product is scoped by; tags become search keywords.</li>
 *     <li>The input and output ports become SolutionPorts of a SolutionComponent that describes the product's design.
 *         Where an output port names a data contract that is already catalogued (as an Agreement), the port becomes an
 *         item of that agreement.</li>
 *     <li>Management ports become Endpoints listed as resources of the product.</li>
 *     <li>The team becomes a Team profile and scoped PersonRoles, support channels become ContactDetails and
 *         authoritative definitions become ExternalReferences (see BitolMapperBase).</li>
 * </ul>
 * Everything that has no home in the open metadata types is kept in additional properties with the prefix "bitol.".
 */
public class DataProductMapper extends BitolMapperBase
{
    private static final String SOLUTION_COMPONENT_TYPE = "Data Product";

    /**
     * Constructor.
     *
     * @param context connector context providing access to the open metadata clients
     */
    public DataProductMapper(ConnectorContextBase context)
    {
        super(context);
    }


    /**
     * Catalogue a data product.  Republishing the same version updates the existing elements; a new version creates
     * new elements linked to the same ExternalId.
     *
     * @param dataProduct document to catalogue
     * @param source description of where the document came from (recorded on the ExternalId link)
     * @return result describing what was done and anything that could not be fully represented
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException problem with the metadata store
     * @throws UserNotAuthorizedException security problem
     */
    public BitolMappingResult catalogueDataProduct(DataProduct dataProduct,
                                                   String      source) throws InvalidParameterException,
                                                                              PropertyServerException,
                                                                              UserNotAuthorizedException
    {
        if ((dataProduct == null) || (dataProduct.getId() == null))
        {
            BitolMappingResult skipped = new BitolMappingResult(BitolDocument.DATA_PRODUCT_KIND, null, null, null);

            skipped.addWarning("The data product has no identifier so it can not be catalogued");

            return skipped;
        }

        String             qualifiedName = getDocumentQualifiedName(BitolDocument.DATA_PRODUCT_KIND, dataProduct.getId(), dataProduct.getVersion());
        BitolMappingResult result        = new BitolMappingResult(BitolDocument.DATA_PRODUCT_KIND, dataProduct.getId(), dataProduct.getVersion(), qualifiedName);
        CollectionClient   productClient = context.getCollectionClient(OpenMetadataType.DIGITAL_PRODUCT.typeName);
        String             productGUID   = findElementGUID(qualifiedName);

        /*
         * A retired product is removed (soft delete - the element can be restored).
         */
        if (isRetired(dataProduct))
        {
            if (productGUID != null)
            {
                productClient.deleteCollection(productGUID, productClient.getDeleteOptions(false));

                result.setElementGUID(productGUID);
                result.setAction(BitolMappingResult.Action.DELETED);
            }
            else
            {
                result.addWarning("The product is retired and was never catalogued, so there is nothing to remove");
            }

            return result;
        }

        /*
         * The product itself.
         */
        DigitalProductProperties properties = getDigitalProductProperties(dataProduct, qualifiedName);

        if (productGUID == null)
        {
            productGUID = productClient.createCollection(new NewElementOptions(productClient.getMetadataSourceOptions()),
                                                         null,
                                                         properties,
                                                         null);
            result.setAction(BitolMappingResult.Action.CREATED);
        }
        else
        {
            productClient.updateCollection(productGUID, productClient.getUpdateOptions(false), properties);
            result.setAction(BitolMappingResult.Action.UPDATED);
        }

        result.setElementGUID(productGUID);

        /*
         * The fundamentals that become separate elements.
         */
        linkExternalId(BitolDocument.DATA_PRODUCT_KIND, dataProduct.getId(), productGUID, source);
        addSearchKeywords(dataProduct.getTags(), productGUID);
        linkDomainCatalog(dataProduct.getDomain(), productGUID);
        linkTenantOrganization(dataProduct.getTenant(), productGUID);
        linkAuthoritativeDefinitions(dataProduct.getAuthoritativeDefinitions(), productGUID);

        if (dataProduct.getDescription() != null)
        {
            linkAuthoritativeDefinitions(dataProduct.getDescription().getAuthoritativeDefinitions(), productGUID);
        }

        /*
         * The sections.
         */
        linkPorts(dataProduct, productGUID, qualifiedName, result);
        linkManagementPorts(dataProduct.getManagementPorts(), productGUID, qualifiedName);
        linkSupportChannels(dataProduct.getSupport(), productGUID, qualifiedName);
        linkTeam(dataProduct.getTeam(), productGUID, qualifiedName, result);

        return result;
    }


    /**
     * Build the properties of the DigitalProduct from the document's fundamentals.
     *
     * @param dataProduct document
     * @param qualifiedName qualified name for this version
     * @return properties
     */
    private DigitalProductProperties getDigitalProductProperties(DataProduct dataProduct,
                                                                 String      qualifiedName)
    {
        DigitalProductProperties properties = new DigitalProductProperties();

        fillFundamentals(dataProduct, properties, qualifiedName);

        properties.setProductName(dataProduct.getName());

        if (dataProduct.getDescription() != null)
        {
            properties.setPurpose(dataProduct.getDescription().getPurpose());
        }

        if (dataProduct.getProductCreatedTs() != null)
        {
            properties.getAdditionalProperties().put(ADDITIONAL_PROPERTY_PREFIX + "productCreatedTs", dataProduct.getProductCreatedTs());
        }

        putIfPresent(properties.getAdditionalProperties(), "type", canonical(dataProduct.getType(), DataProductType.fromValue(dataProduct.getType())));
        addPortExtensions(null, dataProduct.getDeprecated(), properties.getAdditionalProperties());
        putIfPresent(properties.getAdditionalProperties(), SYNONYMS_JSON, toJSON(dataProduct.getSynonyms()));

        return properties;
    }


    /**
     * Add the product to the digital product catalog that represents its domain, creating the catalog if needed.
     *
     * @param domain domain from the document (may be null)
     * @param productGUID product
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException problem with the metadata store
     * @throws UserNotAuthorizedException security problem
     */
    private void linkDomainCatalog(String domain,
                                   String productGUID) throws InvalidParameterException,
                                                              PropertyServerException,
                                                              UserNotAuthorizedException
    {
        if ((domain != null) && (! domain.isBlank()))
        {
            CollectionClient catalogClient = context.getCollectionClient(OpenMetadataType.DIGITAL_PRODUCT_CATALOG.typeName);
            String           qualifiedName = OpenMetadataType.DIGITAL_PRODUCT_CATALOG.typeName + SEPARATOR + domain;
            String           catalogGUID   = findElementGUID(qualifiedName);

            if (catalogGUID == null)
            {
                DigitalProductCatalogProperties properties = new DigitalProductCatalogProperties();

                properties.setQualifiedName(qualifiedName);
                properties.setDisplayName(domain);
                properties.setDescription("Digital products in the " + domain + " domain.");

                catalogGUID = catalogClient.createCollection(new NewElementOptions(catalogClient.getMetadataSourceOptions()),
                                                             null,
                                                             properties,
                                                             null);
            }

            CollectionMembershipProperties membership = new CollectionMembershipProperties();

            membership.setMembershipRationale("Domain named in the data product document");

            catalogClient.addToCollection(catalogGUID, productGUID, catalogClient.getMakeAnchorOptions(false), membership);
        }
    }


    /**
     * Represent the input and output ports as SolutionPorts of a SolutionComponent that describes the product's design.
     * The component is a member of the product collection and anchored to it.
     *
     * @param dataProduct document
     * @param productGUID product
     * @param productQualifiedName product qualified name
     * @param result result to record warnings in
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException problem with the metadata store
     * @throws UserNotAuthorizedException security problem
     */
    private void linkPorts(DataProduct        dataProduct,
                           String             productGUID,
                           String             productQualifiedName,
                           BitolMappingResult result) throws InvalidParameterException,
                                                             PropertyServerException,
                                                             UserNotAuthorizedException
    {
        boolean hasInputPorts  = (dataProduct.getInputPorts() != null) && (! dataProduct.getInputPorts().isEmpty());
        boolean hasOutputPorts = (dataProduct.getOutputPorts() != null) && (! dataProduct.getOutputPorts().isEmpty());

        if ((! hasInputPorts) && (! hasOutputPorts))
        {
            return;
        }

        String componentQualifiedName = productQualifiedName + SEPARATOR + OpenMetadataType.SOLUTION_COMPONENT.typeName;
        String componentGUID          = findElementGUID(componentQualifiedName);

        SolutionComponentProperties componentProperties = new SolutionComponentProperties();

        componentProperties.setQualifiedName(componentQualifiedName);
        componentProperties.setDisplayName((dataProduct.getName() != null) ? dataProduct.getName() : dataProduct.getId());
        componentProperties.setDescription("Design of the data product showing the data it consumes (input ports) and provides (output ports).");
        componentProperties.setVersionIdentifier(dataProduct.getVersion());
        componentProperties.setSolutionComponentType(SOLUTION_COMPONENT_TYPE);

        if (componentGUID == null)
        {
            NewElementOptions newElementOptions = new NewElementOptions(context.getSolutionComponentClient().getMetadataSourceOptions());

            newElementOptions.setAnchorGUID(productGUID);
            newElementOptions.setIsOwnAnchor(false);
            newElementOptions.setParentGUID(productGUID);
            newElementOptions.setParentRelationshipTypeName(OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName);
            newElementOptions.setParentAtEnd1(true);

            CollectionMembershipProperties membership = new CollectionMembershipProperties();

            membership.setMembershipRationale("Design of the data product from its ODPS document");

            componentGUID = context.getSolutionComponentClient().createSolutionComponent(newElementOptions, null, componentProperties, membership);
        }
        else
        {
            context.getSolutionComponentClient().updateSolutionComponent(componentGUID,
                                                                         context.getSolutionComponentClient().getUpdateOptions(false),
                                                                         componentProperties);
        }

        if (hasInputPorts)
        {
            for (DataProductInputPort port : dataProduct.getInputPorts())
            {
                if ((port != null) && (port.getName() != null))
                {
                    Map<String, String> additionalProperties = new HashMap<>();

                    addContractId(port.getContractId(), additionalProperties);
                    addPortExtensions(port.getId(), port.getDeprecated(), additionalProperties);
                    addCustomProperties(port.getCustomProperties(), additionalProperties);

                    String portGUID = createOrUpdatePort(productGUID,
                                                         componentGUID,
                                                         productQualifiedName + SEPARATOR + "InputPort" + SEPARATOR + port.getName() + SEPARATOR + port.getVersion(),
                                                         port.getName(),
                                                         null,
                                                         port.getVersion(),
                                                         SolutionPortDirection.INPUT,
                                                         additionalProperties);

                    addSearchKeywords(port.getTags(), portGUID);
                    linkAuthoritativeDefinitions(port.getAuthoritativeDefinitions(), portGUID);
                    linkContract(port.getContractId(), portGUID, port.getName(), result);
                }
            }
        }

        if (hasOutputPorts)
        {
            for (DataProductOutputPort port : dataProduct.getOutputPorts())
            {
                if ((port != null) && (port.getName() != null))
                {
                    Map<String, String> additionalProperties = new HashMap<>();

                    addContractId(port.getContractId(), additionalProperties);
                    addPortExtensions(port.getId(), port.getDeprecated(), additionalProperties);
                    putIfPresent(additionalProperties, SYNONYMS_JSON, toJSON(port.getSynonyms()));
                    putIfPresent(additionalProperties, CONTEXT_JSON, toJSON(port.getContext()));

                    if (port.getType() != null)
                    {
                        additionalProperties.put(ADDITIONAL_PROPERTY_PREFIX + "type", port.getType());
                    }

                    putIfPresent(additionalProperties, "sbom", toJSON(port.getSbom()));

                    if (port.getInputContracts() != null)
                    {
                        List<String> inputContracts = new ArrayList<>();

                        for (DataProductInputContract inputContract : port.getInputContracts())
                        {
                            if ((inputContract != null) && (inputContract.getId() != null))
                            {
                                inputContracts.add(inputContract.getId() + "@" + inputContract.getVersion());
                            }
                        }

                        if (! inputContracts.isEmpty())
                        {
                            additionalProperties.put(ADDITIONAL_PROPERTY_PREFIX + "inputContracts", String.join(", ", inputContracts));
                        }
                    }

                    addCustomProperties(port.getCustomProperties(), additionalProperties);

                    String portGUID = createOrUpdatePort(productGUID,
                                                         componentGUID,
                                                         productQualifiedName + SEPARATOR + "OutputPort" + SEPARATOR + port.getName() + SEPARATOR + port.getVersion(),
                                                         port.getName(),
                                                         port.getDescription(),
                                                         port.getVersion(),
                                                         SolutionPortDirection.OUTPUT,
                                                         additionalProperties);

                    addSearchKeywords(port.getTags(), portGUID);
                    linkAuthoritativeDefinitions(port.getAuthoritativeDefinitions(), portGUID);
                    linkContract(port.getContractId(), portGUID, port.getName(), result);
                }
            }
        }
    }


    /**
     * Record a contract identifier in additional properties.
     *
     * @param contractId identifier (may be null)
     * @param additionalProperties map to add to
     */
    private void addContractId(String              contractId,
                               Map<String, String> additionalProperties)
    {
        if (contractId != null)
        {
            additionalProperties.put(ADDITIONAL_PROPERTY_PREFIX + "contractId", contractId);
        }
    }


    /**
     * Create or update a SolutionPort attached to the solution component and anchored to the product.
     *
     * @param productGUID anchor
     * @param componentGUID solution component that owns the port
     * @param qualifiedName qualified name of the port
     * @param name display name
     * @param description description (may be null)
     * @param version version identifier (may be null)
     * @param direction direction of the port
     * @param additionalProperties additional properties
     * @return unique identifier of the port
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException problem with the metadata store
     * @throws UserNotAuthorizedException security problem
     */
    private String createOrUpdatePort(String                productGUID,
                                      String                componentGUID,
                                      String                qualifiedName,
                                      String                name,
                                      String                description,
                                      String                version,
                                      SolutionPortDirection direction,
                                      Map<String, String>   additionalProperties) throws InvalidParameterException,
                                                                                         PropertyServerException,
                                                                                         UserNotAuthorizedException
    {
        SolutionPortProperties properties = new SolutionPortProperties();

        properties.setQualifiedName(qualifiedName);
        properties.setDisplayName(name);
        properties.setDescription(description);
        properties.setVersionIdentifier(version);
        properties.setDirection(direction);
        properties.setAdditionalProperties(additionalProperties);

        String portGUID = findElementGUID(qualifiedName);

        if (portGUID == null)
        {
            NewElementOptions newElementOptions = new NewElementOptions(context.getSolutionPortClient().getMetadataSourceOptions());

            newElementOptions.setAnchorGUID(productGUID);
            newElementOptions.setIsOwnAnchor(false);
            newElementOptions.setParentGUID(componentGUID);
            newElementOptions.setParentRelationshipTypeName(OpenMetadataType.SOLUTION_COMPONENT_PORT_RELATIONSHIP.typeName);
            newElementOptions.setParentAtEnd1(true);

            portGUID = context.getSolutionPortClient().createSolutionPort(newElementOptions, null, properties, new SolutionComponentPortProperties());
        }
        else
        {
            context.getSolutionPortClient().updateSolutionPort(portGUID, context.getSolutionPortClient().getUpdateOptions(false), properties);
        }

        return portGUID;
    }


    /**
     * Link a port to the agreement that represents the data contract it names, if that contract has been catalogued.
     * The latest catalogued version of the contract is used since the port does not name a contract version.
     *
     * @param contractId contract identifier from the port (may be null)
     * @param portGUID port
     * @param portName name of the port (recorded as the agreement item identifier)
     * @param result result to record warnings in
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException problem with the metadata store
     * @throws UserNotAuthorizedException security problem
     */
    private void linkContract(String             contractId,
                              String             portGUID,
                              String             portName,
                              BitolMappingResult result) throws InvalidParameterException,
                                                                PropertyServerException,
                                                                UserNotAuthorizedException
    {
        if (contractId == null)
        {
            return;
        }

        OpenMetadataRootElement agreement = getLatestVersion(findElementsByDocumentId(BitolDocument.DATA_CONTRACT_KIND, contractId));

        if ((agreement != null) && (agreement.getElementHeader() != null))
        {
            AgreementItemProperties itemProperties = new AgreementItemProperties();

            itemProperties.setAgreementItemId(portName);

            context.getCollectionClient().linkAgreementItem(agreement.getElementHeader().getGUID(),
                                                            portGUID,
                                                            context.getCollectionClient().getMakeAnchorOptions(false),
                                                            itemProperties);
        }
        else
        {
            result.addWarning("Port " + portName + " names data contract " + contractId + " which is not yet catalogued; the identifier is recorded on the port");
        }
    }


    /**
     * Represent the management ports as Endpoints listed as resources of the product.
     *
     * @param managementPorts management ports from the document (may be null)
     * @param productGUID product
     * @param productQualifiedName product qualified name
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException problem with the metadata store
     * @throws UserNotAuthorizedException security problem
     */
    private void linkManagementPorts(List<DataProductManagementPort> managementPorts,
                                     String                          productGUID,
                                     String                          productQualifiedName) throws InvalidParameterException,
                                                                                                  PropertyServerException,
                                                                                                  UserNotAuthorizedException
    {
        if (managementPorts != null)
        {
            for (DataProductManagementPort managementPort : managementPorts)
            {
                if ((managementPort != null) && (managementPort.getName() != null))
                {
                    String qualifiedName = productQualifiedName + SEPARATOR + "ManagementPort" + SEPARATOR + managementPort.getName();
                    String endpointGUID  = findElementGUID(qualifiedName);

                    EndpointProperties properties = new EndpointProperties();

                    properties.setQualifiedName(qualifiedName);
                    properties.setDisplayName(managementPort.getName());
                    properties.setDescription(managementPort.getDescription());
                    properties.setNetworkAddress(managementPort.getUrl());
                    properties.setProtocol(canonical(managementPort.getType(), DataProductManagementPortType.fromValue(managementPort.getType())));
                    properties.setCategory(canonical(managementPort.getContent(), DataProductManagementPortContent.fromValue(managementPort.getContent())));

                    Map<String, String> additionalProperties = new HashMap<>();

                    if (managementPort.getContent() != null)
                    {
                        additionalProperties.put(ADDITIONAL_PROPERTY_PREFIX + "content", managementPort.getContent());
                    }
                    if (managementPort.getChannel() != null)
                    {
                        additionalProperties.put(ADDITIONAL_PROPERTY_PREFIX + "channel", managementPort.getChannel());
                    }
                    addPortExtensions(managementPort.getId(), managementPort.getDeprecated(), additionalProperties);
                    addCustomProperties(managementPort.getCustomProperties(), additionalProperties);
                    properties.setAdditionalProperties(additionalProperties);

                    if (endpointGUID == null)
                    {
                        NewElementOptions newElementOptions = new NewElementOptions(context.getEndpointClient().getMetadataSourceOptions());

                        newElementOptions.setAnchorGUID(productGUID);
                        newElementOptions.setIsOwnAnchor(false);
                        newElementOptions.setParentGUID(productGUID);
                        newElementOptions.setParentRelationshipTypeName(OpenMetadataType.RESOURCE_LIST_RELATIONSHIP.typeName);
                        newElementOptions.setParentAtEnd1(true);

                        endpointGUID = context.getEndpointClient().createEndpoint(newElementOptions, null, properties, new ResourceListProperties());
                    }
                    else
                    {
                        context.getEndpointClient().updateEndpoint(endpointGUID, context.getEndpointClient().getUpdateOptions(false), properties);
                    }

                    addSearchKeywords(managementPort.getTags(), endpointGUID);
                    linkAuthoritativeDefinitions(managementPort.getAuthoritativeDefinitions(), endpointGUID);
                }
            }
        }
    }

    /**
     * Record the stable identifier and the deprecated flag of a port (or the product itself) in its additional
     * properties.
     *
     * @param id stable identifier (may be null)
     * @param deprecated deprecated flag (may be null)
     * @param additionalProperties map to add to
     */
    private void addPortExtensions(String              id,
                                   Boolean             deprecated,
                                   Map<String, String> additionalProperties)
    {
        putIfPresent(additionalProperties, "id", id);

        if (Boolean.TRUE.equals(deprecated))
        {
            additionalProperties.put(ADDITIONAL_PROPERTY_PREFIX + DEPRECATED, "true");
        }
    }
}
