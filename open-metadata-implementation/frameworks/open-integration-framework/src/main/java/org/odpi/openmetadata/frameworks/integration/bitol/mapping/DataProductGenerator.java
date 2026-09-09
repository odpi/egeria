/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.bitol.mapping;

import org.odpi.openmetadata.frameworks.integration.bitol.common.BitolDocument;
import org.odpi.openmetadata.frameworks.integration.bitol.odps.DataProduct;
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
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.EndpointProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.AgreementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.DigitalProductProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionPortProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * DataProductGenerator builds an Open Data Product Standard (ODPS) document from a DigitalProduct in open metadata.
 * It is the reverse of DataProductMapper: the product's solution component ports become input and output ports (with
 * the data contract identifier taken from the agreement each port is an item of, or from the identifier recorded when
 * the port was catalogued), the endpoints listed as its resources become management ports, and the catalog,
 * organization, keywords, contact details, external references and scoped roles become the domain, tenant, tags,
 * support, authoritative definitions and team.
 */
public class DataProductGenerator extends BitolGeneratorBase
{
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

            dataProduct.setProductCreatedTs(getBitolValue(properties.getAdditionalProperties(), "productCreatedTs"));
        }

        if (dataProduct.getDomain() == null)
        {
            dataProduct.setDomain(getDomain(product));
        }

        dataProduct.setSupport(getSupportChannels(product));
        dataProduct.setTeam(getTeam(product));
        dataProduct.setManagementPorts(getManagementPorts(product));

        fillPorts(product, dataProduct);

        return dataProduct;
    }


    /**
     * Return the domain from the digital product catalog that the product is a member of.
     *
     * @param product product element
     * @return catalog name or null
     */
    private String getDomain(OpenMetadataRootElement product)
    {
        if (product.getMemberOfCollections() != null)
        {
            for (RelatedMetadataElementSummary collection : product.getMemberOfCollections())
            {
                if ((collection != null) && (collection.getRelatedElement() != null) &&
                    (propertyHelper.isTypeOf(collection.getRelatedElement().getElementHeader(), OpenMetadataType.DIGITAL_PRODUCT_CATALOG.typeName)))
                {
                    return getDisplayName(collection.getRelatedElement());
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
     * Fill the input and output ports from the solution ports of the solution components that are members of the product.
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

        inputPort.setName(portProperties.getDisplayName());
        inputPort.setVersion((portProperties.getVersionIdentifier() != null) ? portProperties.getVersionIdentifier() : "1.0.0");
        inputPort.setContractId(getContractId(port, portProperties));
        inputPort.setTags(getTags(port));
        inputPort.setAuthoritativeDefinitions(getAuthoritativeDefinitions(port));
        inputPort.setCustomProperties(getCustomProperties(portProperties.getAdditionalProperties()));

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

        outputPort.setName(portProperties.getDisplayName());
        outputPort.setDescription(portProperties.getDescription());
        outputPort.setVersion((portProperties.getVersionIdentifier() != null) ? portProperties.getVersionIdentifier() : "1.0.0");
        outputPort.setType(getBitolValue(additionalProperties, "type"));
        outputPort.setContractId(getContractId(port, portProperties));
        outputPort.setTags(getTags(port));
        outputPort.setAuthoritativeDefinitions(getAuthoritativeDefinitions(port));
        outputPort.setCustomProperties(getCustomProperties(additionalProperties));

        String sbomURLs = getBitolValue(additionalProperties, "sbom");

        if (sbomURLs != null)
        {
            List<DataProductSBOM> sboms = new ArrayList<>();

            for (String url : sbomURLs.split(",\\s*"))
            {
                if (! url.isBlank())
                {
                    DataProductSBOM sbom = new DataProductSBOM();

                    sbom.setType("external");
                    sbom.setUrl(url.trim());
                    sboms.add(sbom);
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
     * Return the identifier of the data contract for a port: the identifier of the agreement the port is an item of,
     * falling back to the identifier recorded when the port was catalogued.
     *
     * @param port port element
     * @param portProperties port properties
     * @return contract identifier or null
     */
    private String getContractId(OpenMetadataRootElement port,
                                 SolutionPortProperties  portProperties)
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

        return getBitolValue(portProperties.getAdditionalProperties(), "contractId");
    }
}
