/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AccountingCodesProperties describes the accounting codes associated with an element.  These codes are used to
 * track the financial aspects of the element.  The accountingCode attribute is used to describe a single code.
 * The accountingCodeList attribute is used to describe a set of codes - if multiple codes are used then
 * accountingCode would contain the primary/default/preferred value.  The accountingCodeMap attribute is used to
 * describe a set of name-to-code mappings.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AccountingCodesProperties extends ClassificationBeanProperties
{
    private String              accountingCode     = null;
    private String              description        = null;
    private List<String>        accountingCodeList = null;
    private Map<String, String> accountingCodeMap  = null;

    /**
     * Default constructor
     */
    public AccountingCodesProperties()
    {
        super();
        super.typeName = OpenMetadataType.ACCOUNTING_CODES_CLASSIFICATION.typeName;
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public AccountingCodesProperties(AccountingCodesProperties template)
    {
        super(template);

        if (template != null)
        {
            this.accountingCode     = template.getAccountingCode();
            this.description        = template.getDescription();
            this.accountingCodeList = template.getAccountingCodeList();
            this.accountingCodeMap  = template.getAccountingCodeMap();
        }
    }


    /**
     * Return the single accounting code for the element.
     *
     * @return string code
     */
    public String getAccountingCode()
    {
        return accountingCode;
    }


    /**
     * Set up the single accounting code for the element.
     *
     * @param accountingCode string code
     */
    public void setAccountingCode(String accountingCode)
    {
        this.accountingCode = accountingCode;
    }


    /**
     * Return the description of the accounting codes for the element.
     *
     * @return string description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the description of the accounting codes for the element.
     *
     * @param description string description
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return the list of accounting codes for the element.
     *
     * @return list of code strings
     */
    public List<String> getAccountingCodeList()
    {
        return accountingCodeList;
    }


    /**
     * Set up the list of accounting codes for the element.
     *
     * @param accountingCodeList list of code strings
     */
    public void setAccountingCodeList(List<String> accountingCodeList)
    {
        this.accountingCodeList = accountingCodeList;
    }


    /**
     * Return the map of names to accounting codes for the element.
     *
     * @return map of name to code
     */
    public Map<String, String> getAccountingCodeMap()
    {
        return accountingCodeMap;
    }


    /**
     * Set up the map of names to accounting codes for the element.
     *
     * @param accountingCodeMap map of name to code
     */
    public void setAccountingCodeMap(Map<String, String> accountingCodeMap)
    {
        this.accountingCodeMap = accountingCodeMap;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "AccountingCodesProperties{" +
                "accountingCode='" + accountingCode + '\'' +
                ", description='" + description + '\'' +
                ", accountingCodeList=" + accountingCodeList +
                ", accountingCodeMap=" + accountingCodeMap +
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
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        AccountingCodesProperties that = (AccountingCodesProperties) objectToCompare;
        return Objects.equals(accountingCode, that.accountingCode) &&
                Objects.equals(description, that.description) &&
                Objects.equals(accountingCodeList, that.accountingCodeList) &&
                Objects.equals(accountingCodeMap, that.accountingCodeMap);
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), accountingCode, description, accountingCodeList, accountingCodeMap);
    }
}
