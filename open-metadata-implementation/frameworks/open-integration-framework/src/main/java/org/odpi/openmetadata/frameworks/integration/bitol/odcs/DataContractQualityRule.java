/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.bitol.odcs;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.integration.bitol.common.BitolAuthoritativeDefinition;
import org.odpi.openmetadata.frameworks.integration.bitol.common.BitolCustomProperty;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * This class represents a data quality check defined in an Open Data Contract Standard (ODCS) data contract.
 * Quality checks can be attached to a schema object or to a schema property. The "type" determines which of the
 * other properties are relevant: text (description only), library (metric with a comparison operator), sql
 * (query with a comparison operator) or custom (engine and implementation). Only one of the comparison operators
 * (mustBe, mustNotBe, mustBeGreaterThan, ...) is expected.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DataContractQualityRule
{
    private String                             id = null;
    private String                             name = null;
    private String                             description = null;
    private String                             type = null;
    private String                             dimension = null;
    private String                             metric = null;
    private String                             rule = null;
    private String                             severity = null;
    private String                             businessImpact = null;
    private String                             method = null;
    private String                             schedule = null;
    private String                             scheduler = null;
    private String                             unit = null;
    private String                             query = null;
    private String                             engine = null;
    private Object                             implementation = null;
    private Map<String, Object>                arguments = null;
    private Object                             mustBe = null;
    private Object                             mustNotBe = null;
    private Number                             mustBeGreaterThan = null;
    private Number                             mustBeGreaterOrEqualTo = null;
    private Number                             mustBeLessThan = null;
    private Number                             mustBeLessOrEqualTo = null;
    private List<Number>                       mustBeBetween = null;
    private List<Number>                       mustNotBeBetween = null;
    private List<String>                       tags = null;
    private List<BitolCustomProperty>          customProperties = null;
    private List<BitolAuthoritativeDefinition> authoritativeDefinitions = null;


    /**
     * Default constructor
     */
    public DataContractQualityRule()
    {
    }


    /**
     * Return the stable identifier of this element (alphanumeric, hyphen and underscore characters only).
     *
     * @return string identifier
     */
    public String getId()
    {
        return id;
    }


    /**
     * Set up the stable identifier of this element (alphanumeric, hyphen and underscore characters only).
     *
     * @param id string identifier
     */
    public void setId(String id)
    {
        this.id = id;
    }


    /**
     * Return the name of the quality check.
     *
     * @return string name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Set up the name of the quality check.
     *
     * @param name string name
     */
    public void setName(String name)
    {
        this.name = name;
    }


    /**
     * Return the description of the quality check.
     *
     * @return string description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the description of the quality check.
     *
     * @param description string description
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return the type of quality check: text, library, sql or custom.  Standard values are defined in DataContractQualityType.
     *
     * @return string type name
     */
    public String getType()
    {
        return type;
    }


    /**
     * Set up the type of quality check: text, library, sql or custom.  Standard values are defined in DataContractQualityType.
     *
     * @param type string type name
     */
    public void setType(String type)
    {
        this.type = type;
    }


    /**
     * Return the data quality dimension being checked.  Standard values are defined in DataContractQualityDimension.
     *
     * @return string dimension name
     */
    public String getDimension()
    {
        return dimension;
    }


    /**
     * Set up the data quality dimension being checked.  Standard values are defined in DataContractQualityDimension.
     *
     * @param dimension string dimension name
     */
    public void setDimension(String dimension)
    {
        this.dimension = dimension;
    }


    /**
     * Return the metric evaluated by a library check.  Standard values are defined in DataContractQualityMetric.
     *
     * @return string metric name
     */
    public String getMetric()
    {
        return metric;
    }


    /**
     * Set up the metric evaluated by a library check.  Standard values are defined in DataContractQualityMetric.
     *
     * @param metric string metric name
     */
    public void setMetric(String metric)
    {
        this.metric = metric;
    }


    /**
     * Return the name of the rule (deprecated in favour of metric).
     *
     * @return string rule name
     */
    public String getRule()
    {
        return rule;
    }


    /**
     * Set up the name of the rule (deprecated in favour of metric).
     *
     * @param rule string rule name
     */
    public void setRule(String rule)
    {
        this.rule = rule;
    }


    /**
     * Return the severity of a failure, for example info, warning or error.
     *
     * @return string severity
     */
    public String getSeverity()
    {
        return severity;
    }


    /**
     * Set up the severity of a failure, for example info, warning or error.
     *
     * @param severity string severity
     */
    public void setSeverity(String severity)
    {
        this.severity = severity;
    }


    /**
     * Return the business impact of a failure, for example operational or regulatory.
     *
     * @return string impact
     */
    public String getBusinessImpact()
    {
        return businessImpact;
    }


    /**
     * Set up the business impact of a failure, for example operational or regulatory.
     *
     * @param businessImpact string impact
     */
    public void setBusinessImpact(String businessImpact)
    {
        this.businessImpact = businessImpact;
    }


    /**
     * Return the method used to perform the check, for example reconciliation.
     *
     * @return string method
     */
    public String getMethod()
    {
        return method;
    }


    /**
     * Set up the method used to perform the check, for example reconciliation.
     *
     * @param method string method
     */
    public void setMethod(String method)
    {
        this.method = method;
    }


    /**
     * Return the schedule configuration for the scheduler, for example a cron expression.
     *
     * @return string schedule
     */
    public String getSchedule()
    {
        return schedule;
    }


    /**
     * Set up the schedule configuration for the scheduler, for example a cron expression.
     *
     * @param schedule string schedule
     */
    public void setSchedule(String schedule)
    {
        this.schedule = schedule;
    }


    /**
     * Return the name of the scheduler that runs the check, for example cron.
     *
     * @return string scheduler name
     */
    public String getScheduler()
    {
        return scheduler;
    }


    /**
     * Set up the name of the scheduler that runs the check, for example cron.
     *
     * @param scheduler string scheduler name
     */
    public void setScheduler(String scheduler)
    {
        this.scheduler = scheduler;
    }


    /**
     * Return the unit of the values being compared, for example rows or percent.
     *
     * @return string unit
     */
    public String getUnit()
    {
        return unit;
    }


    /**
     * Set up the unit of the values being compared, for example rows or percent.
     *
     * @param unit string unit
     */
    public void setUnit(String unit)
    {
        this.unit = unit;
    }


    /**
     * Return the SQL query of a sql check.  It may use the ${object} and ${property} placeholders.
     *
     * @return string SQL query
     */
    public String getQuery()
    {
        return query;
    }


    /**
     * Set up the SQL query of a sql check.  It may use the ${object} and ${property} placeholders.
     *
     * @param query string SQL query
     */
    public void setQuery(String query)
    {
        this.query = query;
    }


    /**
     * Return the name of the engine that runs a custom check, for example soda or great-expectations.
     *
     * @return string engine name
     */
    public String getEngine()
    {
        return engine;
    }


    /**
     * Set up the name of the engine that runs a custom check, for example soda or great-expectations.
     *
     * @param engine string engine name
     */
    public void setEngine(String engine)
    {
        this.engine = engine;
    }


    /**
     * Return the engine specific implementation of a custom check (a string or a structure).
     *
     * @return implementation string or structure
     */
    public Object getImplementation()
    {
        return implementation;
    }


    /**
     * Set up the engine specific implementation of a custom check (a string or a structure).
     *
     * @param implementation implementation string or structure
     */
    public void setImplementation(Object implementation)
    {
        this.implementation = implementation;
    }


    /**
     * Return the additional arguments for a library metric.
     *
     * @return map of argument names to values
     */
    public Map<String, Object> getArguments()
    {
        return arguments;
    }


    /**
     * Set up the additional arguments for a library metric.
     *
     * @param arguments map of argument names to values
     */
    public void setArguments(Map<String, Object> arguments)
    {
        this.arguments = arguments;
    }


    /**
     * Return the value that the checked metric must be equal to.
     *
     * @return expected value
     */
    public Object getMustBe()
    {
        return mustBe;
    }


    /**
     * Set up the value that the checked metric must be equal to.
     *
     * @param mustBe expected value
     */
    public void setMustBe(Object mustBe)
    {
        this.mustBe = mustBe;
    }


    /**
     * Return the value that the checked metric must not be equal to.
     *
     * @return unexpected value
     */
    public Object getMustNotBe()
    {
        return mustNotBe;
    }


    /**
     * Set up the value that the checked metric must not be equal to.
     *
     * @param mustNotBe unexpected value
     */
    public void setMustNotBe(Object mustNotBe)
    {
        this.mustNotBe = mustNotBe;
    }


    /**
     * Return the value that the checked metric must be greater than.
     *
     * @return numeric threshold
     */
    public Number getMustBeGreaterThan()
    {
        return mustBeGreaterThan;
    }


    /**
     * Set up the value that the checked metric must be greater than.
     *
     * @param mustBeGreaterThan numeric threshold
     */
    public void setMustBeGreaterThan(Number mustBeGreaterThan)
    {
        this.mustBeGreaterThan = mustBeGreaterThan;
    }


    /**
     * Return the value that the checked metric must be greater than or equal to.
     *
     * @return numeric threshold
     */
    public Number getMustBeGreaterOrEqualTo()
    {
        return mustBeGreaterOrEqualTo;
    }


    /**
     * Set up the value that the checked metric must be greater than or equal to.
     *
     * @param mustBeGreaterOrEqualTo numeric threshold
     */
    public void setMustBeGreaterOrEqualTo(Number mustBeGreaterOrEqualTo)
    {
        this.mustBeGreaterOrEqualTo = mustBeGreaterOrEqualTo;
    }


    /**
     * Return the value that the checked metric must be less than.
     *
     * @return numeric threshold
     */
    public Number getMustBeLessThan()
    {
        return mustBeLessThan;
    }


    /**
     * Set up the value that the checked metric must be less than.
     *
     * @param mustBeLessThan numeric threshold
     */
    public void setMustBeLessThan(Number mustBeLessThan)
    {
        this.mustBeLessThan = mustBeLessThan;
    }


    /**
     * Return the value that the checked metric must be less than or equal to.
     *
     * @return numeric threshold
     */
    public Number getMustBeLessOrEqualTo()
    {
        return mustBeLessOrEqualTo;
    }


    /**
     * Set up the value that the checked metric must be less than or equal to.
     *
     * @param mustBeLessOrEqualTo numeric threshold
     */
    public void setMustBeLessOrEqualTo(Number mustBeLessOrEqualTo)
    {
        this.mustBeLessOrEqualTo = mustBeLessOrEqualTo;
    }


    /**
     * Return the inclusive range (two values) that the checked metric must be within.
     *
     * @return list of two numbers
     */
    public List<Number> getMustBeBetween()
    {
        return mustBeBetween;
    }


    /**
     * Set up the inclusive range (two values) that the checked metric must be within.
     *
     * @param mustBeBetween list of two numbers
     */
    public void setMustBeBetween(List<Number> mustBeBetween)
    {
        this.mustBeBetween = mustBeBetween;
    }


    /**
     * Return the range (two values) that the checked metric must be outside of.
     *
     * @return list of two numbers
     */
    public List<Number> getMustNotBeBetween()
    {
        return mustNotBeBetween;
    }


    /**
     * Set up the range (two values) that the checked metric must be outside of.
     *
     * @param mustNotBeBetween list of two numbers
     */
    public void setMustNotBeBetween(List<Number> mustNotBeBetween)
    {
        this.mustNotBeBetween = mustNotBeBetween;
    }


    /**
     * Return the list of tags attached to this element.
     *
     * @return list of tag strings
     */
    public List<String> getTags()
    {
        return tags;
    }


    /**
     * Set up the list of tags attached to this element.
     *
     * @param tags list of tag strings
     */
    public void setTags(List<String> tags)
    {
        this.tags = tags;
    }


    /**
     * Return the list of custom (key/value) properties attached to this element.
     *
     * @return list of custom properties
     */
    public List<BitolCustomProperty> getCustomProperties()
    {
        return customProperties;
    }


    /**
     * Set up the list of custom (key/value) properties attached to this element.
     *
     * @param customProperties list of custom properties
     */
    public void setCustomProperties(List<BitolCustomProperty> customProperties)
    {
        this.customProperties = customProperties;
    }


    /**
     * Return the list of links to sources that provide more details about this element.
     *
     * @return list of authoritative definitions
     */
    public List<BitolAuthoritativeDefinition> getAuthoritativeDefinitions()
    {
        return authoritativeDefinitions;
    }


    /**
     * Set up the list of links to sources that provide more details about this element.
     *
     * @param authoritativeDefinitions list of authoritative definitions
     */
    public void setAuthoritativeDefinitions(List<BitolAuthoritativeDefinition> authoritativeDefinitions)
    {
        this.authoritativeDefinitions = authoritativeDefinitions;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DataContractQualityRule{" +
                       "id='" + id + '\'' +
                       ", name='" + name + '\'' +
                       ", description='" + description + '\'' +
                       ", type='" + type + '\'' +
                       ", dimension='" + dimension + '\'' +
                       ", metric='" + metric + '\'' +
                       ", rule='" + rule + '\'' +
                       ", severity='" + severity + '\'' +
                       ", businessImpact='" + businessImpact + '\'' +
                       ", method='" + method + '\'' +
                       ", schedule='" + schedule + '\'' +
                       ", scheduler='" + scheduler + '\'' +
                       ", unit='" + unit + '\'' +
                       ", query='" + query + '\'' +
                       ", engine='" + engine + '\'' +
                       ", implementation=" + implementation +
                       ", arguments=" + arguments +
                       ", mustBe=" + mustBe +
                       ", mustNotBe=" + mustNotBe +
                       ", mustBeGreaterThan=" + mustBeGreaterThan +
                       ", mustBeGreaterOrEqualTo=" + mustBeGreaterOrEqualTo +
                       ", mustBeLessThan=" + mustBeLessThan +
                       ", mustBeLessOrEqualTo=" + mustBeLessOrEqualTo +
                       ", mustBeBetween=" + mustBeBetween +
                       ", mustNotBeBetween=" + mustNotBeBetween +
                       ", tags=" + tags +
                       ", customProperties=" + customProperties +
                       ", authoritativeDefinitions=" + authoritativeDefinitions +
                       '}';
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
        DataContractQualityRule that = (DataContractQualityRule) objectToCompare;
        return Objects.equals(id, that.id) &&
                       Objects.equals(name, that.name) &&
                       Objects.equals(description, that.description) &&
                       Objects.equals(type, that.type) &&
                       Objects.equals(dimension, that.dimension) &&
                       Objects.equals(metric, that.metric) &&
                       Objects.equals(rule, that.rule) &&
                       Objects.equals(severity, that.severity) &&
                       Objects.equals(businessImpact, that.businessImpact) &&
                       Objects.equals(method, that.method) &&
                       Objects.equals(schedule, that.schedule) &&
                       Objects.equals(scheduler, that.scheduler) &&
                       Objects.equals(unit, that.unit) &&
                       Objects.equals(query, that.query) &&
                       Objects.equals(engine, that.engine) &&
                       Objects.equals(implementation, that.implementation) &&
                       Objects.equals(arguments, that.arguments) &&
                       Objects.equals(mustBe, that.mustBe) &&
                       Objects.equals(mustNotBe, that.mustNotBe) &&
                       Objects.equals(mustBeGreaterThan, that.mustBeGreaterThan) &&
                       Objects.equals(mustBeGreaterOrEqualTo, that.mustBeGreaterOrEqualTo) &&
                       Objects.equals(mustBeLessThan, that.mustBeLessThan) &&
                       Objects.equals(mustBeLessOrEqualTo, that.mustBeLessOrEqualTo) &&
                       Objects.equals(mustBeBetween, that.mustBeBetween) &&
                       Objects.equals(mustNotBeBetween, that.mustNotBeBetween) &&
                       Objects.equals(tags, that.tags) &&
                       Objects.equals(customProperties, that.customProperties) &&
                       Objects.equals(authoritativeDefinitions, that.authoritativeDefinitions);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(id, name, description, type, dimension, metric, rule, severity, businessImpact, method, schedule, scheduler, unit, query, engine, implementation, arguments, mustBe, mustNotBe, mustBeGreaterThan, mustBeGreaterOrEqualTo, mustBeLessThan, mustBeLessOrEqualTo, mustBeBetween, mustNotBeBetween, tags, customProperties, authoritativeDefinitions);
    }
}
