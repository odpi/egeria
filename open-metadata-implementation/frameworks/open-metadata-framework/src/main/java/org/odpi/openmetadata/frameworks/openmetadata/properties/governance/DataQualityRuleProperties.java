/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.governance;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DataQualityRuleProperties describes a rule that checks the quality of data, for example that a data field has no
 * null values or that a data set has an expected number of rows.  It captures the check in the terms used by data
 * contract standards such as the Bitol Open Data Contract Standard (ODCS): the quality dimension being checked, the
 * type of check, the metric or expression evaluated and the comparison against the threshold values.  The rule is a
 * governance control and is linked to the data structures, data fields or assets it governs using the GovernedBy
 * relationship.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DataQualityRuleProperties extends GovernanceRuleProperties
{
    private String       qualityDimension   = null;
    private String       checkType          = null;
    private String       metric             = null;
    private String       severity           = null;
    private String       businessImpact     = null;
    private String       method             = null;
    private String       units              = null;
    private String       schedule           = null;
    private String       scheduler          = null;
    private String       expression         = null;
    private String       qualityEngine      = null;
    private String       comparisonOperator = null;
    private List<String> thresholdValues    = null;


    /**
     * Default Constructor
     */
    public DataQualityRuleProperties()
    {
        super();
        super.typeName = OpenMetadataType.DATA_QUALITY_RULE.typeName;
    }


    /**
     * Copy/Clone Constructor
     *
     * @param template object to copy
     */
    public DataQualityRuleProperties(DataQualityRuleProperties template)
    {
        super(template);

        if (template != null)
        {
            qualityDimension   = template.getQualityDimension();
            checkType          = template.getCheckType();
            metric             = template.getMetric();
            severity           = template.getSeverity();
            businessImpact     = template.getBusinessImpact();
            method             = template.getMethod();
            units              = template.getUnits();
            schedule           = template.getSchedule();
            scheduler          = template.getScheduler();
            expression         = template.getExpression();
            qualityEngine      = template.getQualityEngine();
            comparisonOperator = template.getComparisonOperator();
            thresholdValues    = template.getThresholdValues();
        }
    }


    /**
     * Return the data quality dimension that the rule addresses, for example accuracy, completeness, conformity,
     * consistency, coverage, timeliness or uniqueness.
     *
     * @return string name
     */
    public String getQualityDimension()
    {
        return qualityDimension;
    }


    /**
     * Set up the data quality dimension that the rule addresses.
     *
     * @param qualityDimension string name
     */
    public void setQualityDimension(String qualityDimension)
    {
        this.qualityDimension = qualityDimension;
    }


    /**
     * Return the type of check: text (described for humans), library (a standard metric), sql (a query) or
     * custom (run by a named engine).
     *
     * @return string name
     */
    public String getCheckType()
    {
        return checkType;
    }


    /**
     * Set up the type of check.
     *
     * @param checkType string name
     */
    public void setCheckType(String checkType)
    {
        this.checkType = checkType;
    }


    /**
     * Return the name of the standard metric evaluated by a library check, for example nullValues, missingValues,
     * invalidValues, duplicateValues or rowCount.
     *
     * @return string name
     */
    public String getMetric()
    {
        return metric;
    }


    /**
     * Set up the name of the standard metric evaluated by a library check.
     *
     * @param metric string name
     */
    public void setMetric(String metric)
    {
        this.metric = metric;
    }


    /**
     * Return the severity of a failure of the rule, for example info, warning or error.
     *
     * @return string
     */
    public String getSeverity()
    {
        return severity;
    }


    /**
     * Set up the severity of a failure of the rule.
     *
     * @param severity string
     */
    public void setSeverity(String severity)
    {
        this.severity = severity;
    }


    /**
     * Return the business impact of a failure of the rule, for example operational or regulatory.
     *
     * @return string
     */
    public String getBusinessImpact()
    {
        return businessImpact;
    }


    /**
     * Set up the business impact of a failure of the rule.
     *
     * @param businessImpact string
     */
    public void setBusinessImpact(String businessImpact)
    {
        this.businessImpact = businessImpact;
    }


    /**
     * Return the method used to perform the check, for example reconciliation.
     *
     * @return string
     */
    public String getMethod()
    {
        return method;
    }


    /**
     * Set up the method used to perform the check.
     *
     * @param method string
     */
    public void setMethod(String method)
    {
        this.method = method;
    }


    /**
     * Return the units of the values being compared, for example rows or percent.
     *
     * @return string
     */
    public String getUnits()
    {
        return units;
    }


    /**
     * Set up the units of the values being compared.
     *
     * @param units string
     */
    public void setUnits(String units)
    {
        this.units = units;
    }


    /**
     * Return the schedule configuration for the scheduler, for example a cron expression.
     *
     * @return string
     */
    public String getSchedule()
    {
        return schedule;
    }


    /**
     * Set up the schedule configuration for the scheduler.
     *
     * @param schedule string
     */
    public void setSchedule(String schedule)
    {
        this.schedule = schedule;
    }


    /**
     * Return the name of the scheduler that runs the check, for example cron.
     *
     * @return string
     */
    public String getScheduler()
    {
        return scheduler;
    }


    /**
     * Set up the name of the scheduler that runs the check.
     *
     * @param scheduler string
     */
    public void setScheduler(String scheduler)
    {
        this.scheduler = scheduler;
    }


    /**
     * Return the expression evaluated by the check: the SQL query of a sql check, or the engine specific
     * implementation of a custom check.
     *
     * @return string
     */
    public String getExpression()
    {
        return expression;
    }


    /**
     * Set up the expression evaluated by the check.
     *
     * @param expression string
     */
    public void setExpression(String expression)
    {
        this.expression = expression;
    }


    /**
     * Return the name of the engine that runs a custom check, for example soda or great-expectations.
     *
     * @return string
     */
    public String getQualityEngine()
    {
        return qualityEngine;
    }


    /**
     * Set up the name of the engine that runs a custom check.
     *
     * @param qualityEngine string
     */
    public void setQualityEngine(String qualityEngine)
    {
        this.qualityEngine = qualityEngine;
    }


    /**
     * Return the operator used to compare the measured value with the threshold values, for example mustBe,
     * mustNotBe, mustBeGreaterThan, mustBeGreaterOrEqualTo, mustBeLessThan, mustBeLessOrEqualTo, mustBeBetween
     * or mustNotBeBetween.
     *
     * @return string
     */
    public String getComparisonOperator()
    {
        return comparisonOperator;
    }


    /**
     * Set up the operator used to compare the measured value with the threshold values.
     *
     * @param comparisonOperator string
     */
    public void setComparisonOperator(String comparisonOperator)
    {
        this.comparisonOperator = comparisonOperator;
    }


    /**
     * Return the threshold values that the measured value is compared with.  Most operators take one value; the
     * between operators take two.
     *
     * @return list of values expressed as strings
     */
    public List<String> getThresholdValues()
    {
        return thresholdValues;
    }


    /**
     * Set up the threshold values that the measured value is compared with.
     *
     * @param thresholdValues list of values expressed as strings
     */
    public void setThresholdValues(List<String> thresholdValues)
    {
        this.thresholdValues = thresholdValues;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "DataQualityRuleProperties{" +
                "qualityDimension='" + qualityDimension + '\'' +
                ", checkType='" + checkType + '\'' +
                ", metric='" + metric + '\'' +
                ", severity='" + severity + '\'' +
                ", businessImpact='" + businessImpact + '\'' +
                ", method='" + method + '\'' +
                ", units='" + units + '\'' +
                ", schedule='" + schedule + '\'' +
                ", scheduler='" + scheduler + '\'' +
                ", expression='" + expression + '\'' +
                ", qualityEngine='" + qualityEngine + '\'' +
                ", comparisonOperator='" + comparisonOperator + '\'' +
                ", thresholdValues=" + thresholdValues +
                "} " + super.toString();
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
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        DataQualityRuleProperties that = (DataQualityRuleProperties) objectToCompare;
        return Objects.equals(qualityDimension, that.qualityDimension) &&
                Objects.equals(checkType, that.checkType) &&
                Objects.equals(metric, that.metric) &&
                Objects.equals(severity, that.severity) &&
                Objects.equals(businessImpact, that.businessImpact) &&
                Objects.equals(method, that.method) &&
                Objects.equals(units, that.units) &&
                Objects.equals(schedule, that.schedule) &&
                Objects.equals(scheduler, that.scheduler) &&
                Objects.equals(expression, that.expression) &&
                Objects.equals(qualityEngine, that.qualityEngine) &&
                Objects.equals(comparisonOperator, that.comparisonOperator) &&
                Objects.equals(thresholdValues, that.thresholdValues);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), qualityDimension, checkType, metric, severity, businessImpact, method,
                            units, schedule, scheduler, expression, qualityEngine, comparisonOperator, thresholdValues);
    }
}
