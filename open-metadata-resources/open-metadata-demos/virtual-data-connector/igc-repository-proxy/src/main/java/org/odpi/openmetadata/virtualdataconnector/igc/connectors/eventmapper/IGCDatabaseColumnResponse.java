/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.virtualdataconnector.igc.connectors.eventmapper;

import java.util.List;


public class IGCDatabaseColumnResponse {

    protected String	  created_by	;
    protected int	      position	;
    protected String	  modified_on	;
    protected String   	  fraction	;
    protected boolean	  unique	;
    protected List<IGCContext> _context;
    protected String	  created_on	;
    protected boolean	  allows_null_values	;
    protected String	  _name	;
    protected String	  type	;
    protected String	  _type	;
    protected String	  _id	;
    protected String	  short_description	;
    protected String	  modified_by	;
    protected String	  _url	;
    protected String	  data_type	;
    protected String	  name	;
    protected String	  length	;
    protected String	  odbc_type	;
    protected IGCObject	  database_table_or_view	;

    public String getCreated_by() {
        return created_by;
    }

    public int getPosition() {
        return position;
    }

    public String getModified_on() {
        return modified_on;
    }

    public String getFraction() {
        return fraction;
    }

    public boolean isUnique() {
        return unique;
    }

    public List<IGCContext> get_context() {
        return _context;
    }

    public String getCreated_on() {
        return created_on;
    }

    public boolean isAllows_null_values() {
        return allows_null_values;
    }

    public String get_name() {
        return _name;
    }

    public String getType() {
        return type;
    }

    public String get_type() {
        return _type;
    }

    public String get_id() {
        return _id;
    }

    public String getShort_description() {
        return short_description;
    }

    public String getModified_by() {
        return modified_by;
    }

    public String get_url() {
        return _url;
    }

    public String getData_type() {
        return data_type;
    }

    public String getName() {
        return name;
    }

    public String getLength() {
        return length;
    }

    public String getOdbc_type() {
        return odbc_type;
    }

    public IGCObject getDatabase_table_or_view() {
        return database_table_or_view;
    }
}
