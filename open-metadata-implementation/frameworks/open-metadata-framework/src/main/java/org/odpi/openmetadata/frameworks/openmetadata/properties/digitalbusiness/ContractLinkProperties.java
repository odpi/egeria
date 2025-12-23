/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AgreementActorProperties describes a relationship between an actor and an agreement.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContractLinkProperties extends RelationshipBeanProperties
{
    private String contractId                  = null;
    private String contractLiaison             = null;
    private String contractLiaisonTypeName     = null;
    private String contractLiaisonPropertyName = null;

    /**
     * Default constructor
     */
    public ContractLinkProperties()
    {
        super();
        super.typeName = OpenMetadataType.CONTRACT_LINK_RELATIONSHIP.typeName;
    }


    /**
     * Copy/clone constructor.
     *
     * @param template template object to copy.
     */
    public ContractLinkProperties(ContractLinkProperties template)
    {
        super(template);

        if (template != null)
        {
            this.contractId                  = template.getContractId();
            this.contractLiaison             = template.getContractLiaison();
            this.contractLiaisonTypeName     = template.getContractLiaisonTypeName();
            this.contractLiaisonPropertyName = template.getContractLiaisonPropertyName();
        }
    }


    /**
     * Set up the unique identifier for the contract.
     *
     * @param contractId String name
     */
    public void setContractId(String contractId)
    {
        this.contractId = contractId;
    }


    /**
     * Returns the unique identifier for the contract.
     *
     * @return String name
     */
    public String getContractId()
    {
        return contractId;
    }



    /**
     * Return the name of the person responsible for this contract.
     *
     * @return string name/id
     */
    public String getContractLiaison()
    {
        return contractLiaison;
    }


    /**
     * Set up the name of the person responsible for this contract.
     *
     * @param contractLiaison string name/id
     */
    public void setContractLiaison(String contractLiaison)
    {
        this.contractLiaison = contractLiaison;
    }


    /**
     * Return the name of the type of the element supplying the contractLiaison property.
     *
     * @return string type name
     */
    public String getContractLiaisonTypeName()
    {
        return contractLiaisonTypeName;
    }


    /**
     * Set up the name of the type of the element supplying the contractLiaison property.
     *
     * @param contractLiaisonTypeName string type name
     */
    public void setContractLiaisonTypeName(String contractLiaisonTypeName)
    {
        this.contractLiaisonTypeName = contractLiaisonTypeName;
    }


    /**
     * Return the name of the property from the element supplying the contractLiaison property.
     *
     * @return string property name
     */
    public String getContractLiaisonPropertyName()
    {
        return contractLiaisonPropertyName;
    }


    /**
     * Set up the name of the property from the element supplying the contractLiaison property.
     *
     * @param contractLiaisonPropertyName string property name
     */
    public void setContractLiaisonPropertyName(String contractLiaisonPropertyName)
    {
        this.contractLiaisonPropertyName = contractLiaisonPropertyName;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ContractLinkProperties{" +
                "contractId='" + contractId + '\'' +
                ", contractLiaison='" + contractLiaison + '\'' +
                ", contractLiaisonTypeName='" + contractLiaisonTypeName + '\'' +
                ", contractLiaisonPropertyName='" + contractLiaisonPropertyName + '\'' +
                "} " + super.toString();
    }


    /**
     * Compare the values of the supplied object with those stored in the current object.
     *
     * @param objectToCompare supplied object
     * @return boolean result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (! (objectToCompare instanceof ContractLinkProperties that))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        return Objects.equals(contractLiaison, that.contractLiaison) &&
                Objects.equals(contractLiaisonTypeName, that.contractLiaisonTypeName) &&
                Objects.equals(contractLiaisonPropertyName, that.contractLiaisonPropertyName) &&
                Objects.equals(contractId, that.contractId);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), contractId, contractLiaison, contractLiaisonTypeName, contractLiaisonPropertyName);
    }
}
