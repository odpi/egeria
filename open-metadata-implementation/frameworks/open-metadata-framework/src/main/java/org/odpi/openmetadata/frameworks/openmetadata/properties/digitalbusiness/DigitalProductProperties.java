/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DigitalProductStatus;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationProperties;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DigitalProductProperties describes the properties that describe a digital product.  It is typically attached to a solution component,
 * but it can be attached to any referenceable.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DigitalProductProperties extends ClassificationProperties
{
    private DigitalProductStatus productStatus = null;
    private String               productName   = null;
    private String               productType          = null;
    private String               description          = null;
    private Date                 introductionDate     = null;
    private String               maturity             = null;
    private String               serviceLife          = null;
    private String               currentVersion       = null;
    private Date                 nextVersionDate      = null;
    private Date                 withdrawDate         = null;
    private Map<String, String>  additionalProperties = null;


    /**
     * Default constructor
     */
    public DigitalProductProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template template object to copy.
     */
    public DigitalProductProperties(DigitalProductProperties template)
    {
        super(template);

        if (template != null)
        {
            productStatus = template.getProductStatus();
            productName = template.getProductName();
            productType = template.getProductType();
            description = template.getDescription();
            introductionDate = template.getIntroductionDate();
            maturity = template.getMaturity();
            serviceLife = template.getServiceLife();
            currentVersion  = template.getCurrentVersion();
            nextVersionDate = template.getNextVersionDate();
            withdrawDate    = template.getWithdrawDate();
            additionalProperties = template.getAdditionalProperties();
        }
    }


    /**
     * Return the status of the product.
     *
     * @return InstanceStatus enum
     */
    public DigitalProductStatus getProductStatus()
    {
        return productStatus;
    }


    /**
     * Set up the status of the product
     *
     * @param productStatus InstanceStatus enum
     */
    public void setProductStatus(DigitalProductStatus productStatus)
    {
        this.productStatus = productStatus;
    }


    /**
     * Return the product name.
     *
     * @return string name
     */
    public String getProductName()
    {
        return productName;
    }


    /**
     * Set up the product name.
     *
     * @param productName string name
     */
    public void setProductName(String productName)
    {
        this.productName = productName;
    }


    /**
     * Return the type of product.
     *
     * @return string name
     */
    public String getProductType()
    {
        return productType;
    }


    /**
     * Set up the type of product.
     *
     * @param productType string name
     */
    public void setProductType(String productType)
    {
        this.productType = productType;
    }


    /**
     * Return the description.
     *
     * @return text
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the description.
     *
     * @param description text
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return the date that the product was added to the market.
     *
     * @return date
     */
    public Date getIntroductionDate()
    {
        return introductionDate;
    }


    /**
     * Set up the date that the product was added to the market.
     *
     * @param introductionDate date
     */
    public void setIntroductionDate(Date introductionDate)
    {
        this.introductionDate = introductionDate;
    }


    /**
     * Return the maturity level of the product.
     *
     * @return string name
     */
    public String getMaturity()
    {
        return maturity;
    }


    /**
     * Set up the maturity level of the product.
     *
     * @param maturity string name
     */
    public void setMaturity(String maturity)
    {
        this.maturity = maturity;
    }


    /**
     * Return the estimated lifetime of the product.
     *
     * @return text
     */
    public String getServiceLife()
    {
        return serviceLife;
    }


    /**
     * Set up the estimated lifetime of the product.
     *
     * @param serviceLife text
     */
    public void setServiceLife(String serviceLife)
    {
        this.serviceLife = serviceLife;
    }


    /**
     * Return the identifier of the current version.
     *
     * @return string name
     */
    public String getCurrentVersion()
    {
        return currentVersion;
    }


    /**
     * Set up the identifier of the current version.
     *
     * @param currentVersion string name
     */
    public void setCurrentVersion(String currentVersion)
    {
        this.currentVersion = currentVersion;
    }


    /**
     * Return the date of the next version of the digital service.
     *
     * @return date
     */
    public Date getNextVersionDate()
    {
        return nextVersionDate;
    }


    /**
     * Set up the date of the next version of the digital service.
     *
     * @param nextVersionDate date
     */
    public void setNextVersionDate(Date nextVersionDate)
    {
        this.nextVersionDate = nextVersionDate;
    }


    /**
     * Return the date when the product is being withdrawn.
     *
     * @return date
     */
    public Date getWithdrawDate()
    {
        return withdrawDate;
    }


    /**
     * Set up the date when the product is being withdrawn.
     *
     * @param withdrawDate date
     */
    public void setWithdrawDate(Date withdrawDate)
    {
        this.withdrawDate = withdrawDate;
    }


    /**
     * Return any additional properties that describe the product.
     *
     * @return name value pairs
     */
    public Map<String, String> getAdditionalProperties()
    {
        return additionalProperties;
    }


    /**
     * Set up any additional properties that describe the product.
     *
     * @param additionalProperties name value pairs
     */
    public void setAdditionalProperties(Map<String, String> additionalProperties)
    {
        this.additionalProperties = additionalProperties;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DigitalProductProperties{" +
                       "effectiveFrom=" + getEffectiveFrom() +
                       ", effectiveTo=" + getEffectiveTo() +
                       ", extendedProperties=" + getExtendedProperties() +
                       ", productStatus='" + productStatus + '\'' +
                       ", productName='" + productName + '\'' +
                       ", productType='" + productType + '\'' +
                       ", description='" + description + '\'' +
                       ", introductionDate=" + introductionDate +
                       ", maturity='" + maturity + '\'' +
                       ", serviceLife='" + serviceLife + '\'' +
                       ", currentVersion='" + currentVersion + '\'' +
                       ", nextVersionDate=" + nextVersionDate +
                       ", withdrawDate=" + withdrawDate +
                       ", additionalProperties=" + additionalProperties +
                       '}';
    }


    /**
     * Return comparison result based on the content of the properties.
     *
     * @param objectToCompare test object
     * @return result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (! (objectToCompare instanceof DigitalProductProperties that))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        return Objects.equals(productStatus, that.productStatus) &&
                       Objects.equals(productName, that.productName) &&
                       Objects.equals(productType, that.productType) && Objects.equals(description, that.description) &&
                       Objects.equals(introductionDate, that.introductionDate) && Objects.equals(maturity, that.maturity) &&
                       Objects.equals(serviceLife, that.serviceLife) && Objects.equals(currentVersion, that.currentVersion) &&
                       Objects.equals(nextVersionDate, that.nextVersionDate) && Objects.equals(withdrawDate, that.withdrawDate) &&
                       Objects.equals(additionalProperties, that.additionalProperties);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), productStatus, productName, productType, description, introductionDate,
                            maturity, serviceLife, currentVersion, nextVersionDate, withdrawDate, additionalProperties);
    }
}
