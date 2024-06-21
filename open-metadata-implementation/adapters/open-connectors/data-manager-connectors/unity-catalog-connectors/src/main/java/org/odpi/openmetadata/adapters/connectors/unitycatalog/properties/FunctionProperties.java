/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.unitycatalog.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The properties of a function - maps to CreateFunction.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class FunctionProperties extends SchemaNamespaceProperties
{
    private FunctionParameterInfos input_params = null;
    private ColumnTypeName         data_type = null;
    private String                 full_data_type = null;
    private FunctionParameterInfos return_params = null;
    private RoutineBody            routine_body = null;
    private String                 routine_definition = null;
    private DependencyList         routine_dependencies = null;
    private ParameterStyle         parameter_style = null;
    private boolean                is_deterministic = true;
    private SQLDataAccess          sql_data_access = null;
    private boolean                is_null_call = false;
    private SecurityType           security_type = null;
    private String                 specific_name = null;
    private String                 external_language = null;

    /**
     * Constructor
     */
    public FunctionProperties()
    {
    }

    /**
     * Return the description of the input parameters.
     *
     * @return list
     */
    public FunctionParameterInfos getInput_params()
    {
        return input_params;
    }


    /**
     * Set up the description of the input parameters,
     *
     * @param input_params list
     */
    public void setInput_params(FunctionParameterInfos input_params)
    {
        this.input_params = input_params;
    }


    /**
     * Return the data type of the function ?
     *
     * @return enum
     */
    public ColumnTypeName getData_type()
    {
        return data_type;
    }


    /**
     * Set up the data type of the function.
     *
     * @param data_type enum
     */
    public void setData_type(ColumnTypeName data_type)
    {
        this.data_type = data_type;
    }

    /**
     * Return the "pretty-printed" function data type.
     *
     * @return string
     */
    public String getFull_data_type()
    {
        return full_data_type;
    }


    /**
     * Set up the "pretty-printed" function data type.
     *
     * @param full_data_type string
     */
    public void setFull_data_type(String full_data_type)
    {
        this.full_data_type = full_data_type;
    }


    /**
     * Return the description of the function's return parameters.
     *
     * @return parameter list
     */
    public FunctionParameterInfos getReturn_params()
    {
        return return_params;
    }


    /**
     * Set up the description of the function's return parameters.
     *
     * @param return_params parameter list
     */
    public void setReturn_params(FunctionParameterInfos return_params)
    {
        this.return_params = return_params;
    }


    /**
     * Return the language of implementation.
     * When **EXTERNAL** is used, the language of the routine function should be specified in the __external_language__ field,  and the __return_params__ of the function cannot be used (as **TABLE** return type is not supported), and the __sql_data_access__ field must be **NO_SQL**.
     *
     * @return enum
     */
    public RoutineBody getRoutine_body()
    {
        return routine_body;
    }


    /**
     * Set up the language of implementation.
     * When **EXTERNAL** is used, the language of the routine function should be specified in the __external_language__ field,  and the __return_params__ of the function cannot be used (as **TABLE** return type is not supported), and the __sql_data_access__ field must be **NO_SQL**.
     *
     * @param routine_body enum
     */
    public void setRoutine_body(RoutineBody routine_body)
    {
        this.routine_body = routine_body;
    }


    /**
     * Return the function body.
     *
     * @return string
     */
    public String getRoutine_definition()
    {
        return routine_definition;
    }


    /**
     * Set up the function body.
     *
     * @param routine_definition string
     */
    public void setRoutine_definition(String routine_definition)
    {
        this.routine_definition = routine_definition;
    }


    /**
     * Return te list of dependencies.
     *
     * @return list
     */
    public DependencyList getRoutine_dependencies()
    {
        return routine_dependencies;
    }


    /**
     * Set up the list of dependencies.
     *
     * @param routine_dependencies list
     */
    public void setRoutine_dependencies(DependencyList routine_dependencies)
    {
        this.routine_dependencies = routine_dependencies;
    }


    /**
     * Return the function parameter style.
     * **S** is the value for SQL.
     *
     * @return enum
     */
    public ParameterStyle getParameter_style()
    {
        return parameter_style;
    }


    /**
     * Set up the function parameter style.
     * **S** is the value for SQL.
     *
     * @param parameter_style enum
     */
    public void setParameter_style(ParameterStyle parameter_style)
    {
        this.parameter_style = parameter_style;
    }


    /**
     * Return whether this function is deterministic.
     *
     * @return boolean
     */
    public boolean getIs_deterministic()
    {
        return is_deterministic;
    }


    /**
     * Set up whether this function is deterministic.
     *
     * @param is_deterministic boolean
     */
    public void setIs_deterministic(boolean is_deterministic)
    {
        this.is_deterministic = is_deterministic;
    }


    /**
     * Return the type of SQL data access used by the function.
     *
     * @return enum
     */
    public SQLDataAccess getSql_data_access()
    {
        return sql_data_access;
    }


    /**
     * Set up the type of SQL data access used by the function.
     *
     * @param sql_data_access enum
     */
    public void setSql_data_access(SQLDataAccess sql_data_access)
    {
        this.sql_data_access = sql_data_access;
    }


    /**
     * Return whether thi function is a null call,
     *
     * @return boolean
     */
    public boolean getIs_null_call()
    {
        return is_null_call;
    }


    /**
     * Set up whether this function is a null call.
     *
     * @param is_null_call boolean
     */
    public void setIs_null_call(boolean is_null_call)
    {
        this.is_null_call = is_null_call;
    }


    /**
     * Return the function security type.
     *
     * @return enum
     */
    public SecurityType getSecurity_type()
    {
        return security_type;
    }


    /**
     * Set up the function security type.
     *
     * @param security_type enum
     */
    public void setSecurity_type(SecurityType security_type)
    {
        this.security_type = security_type;
    }


    /**
     * Return the specific name of the function; Reserved for future use.
     *
     * @return string
     */
    public String getSpecific_name()
    {
        return specific_name;
    }


    /**
     * Set up the specific name of the function; Reserved for future use.
     *
     * @param specific_name string
     */
    public void setSpecific_name(String specific_name)
    {
        this.specific_name = specific_name;
    }


    /**
     * Return the name of the external language of the function (if relevant).
     *
     * @return string
     */
    public String getExternal_language()
    {
        return external_language;
    }


    /**
     * Set up the name of the external language of the function (if relevant).
     *
     * @param external_language string
     */
    public void setExternal_language(String external_language)
    {
        this.external_language = external_language;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "FunctionProperties{" +
                "input_params=" + input_params +
                ", data_type=" + data_type +
                ", full_data_type='" + full_data_type + '\'' +
                ", return_params=" + return_params +
                ", routine_body=" + routine_body +
                ", routine_definition='" + routine_definition + '\'' +
                ", routine_dependencies=" + routine_dependencies +
                ", parameter_style=" + parameter_style +
                ", is_deterministic=" + is_deterministic +
                ", sql_data_access=" + sql_data_access +
                ", is_null_call=" + is_null_call +
                ", security_type=" + security_type +
                ", specific_name='" + specific_name + '\'' +
                ", external_language='" + external_language + '\'' +
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
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        FunctionProperties that = (FunctionProperties) objectToCompare;
        return is_deterministic == that.is_deterministic && is_null_call == that.is_null_call &&
                Objects.equals(input_params, that.input_params) && data_type == that.data_type &&
                Objects.equals(full_data_type, that.full_data_type) &&
                Objects.equals(return_params, that.return_params) && routine_body == that.routine_body &&
                Objects.equals(routine_definition, that.routine_definition) &&
                Objects.equals(routine_dependencies, that.routine_dependencies) &&
                Objects.equals(security_type, that.security_type) &&
                Objects.equals(specific_name, that.specific_name) &&
                Objects.equals(external_language, that.external_language) &&
                parameter_style == that.parameter_style && sql_data_access == that.sql_data_access;
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), input_params, data_type, full_data_type, return_params, routine_body, routine_definition, routine_dependencies, parameter_style, is_deterministic, sql_data_access, is_null_call, security_type, specific_name, external_language);
    }
}
