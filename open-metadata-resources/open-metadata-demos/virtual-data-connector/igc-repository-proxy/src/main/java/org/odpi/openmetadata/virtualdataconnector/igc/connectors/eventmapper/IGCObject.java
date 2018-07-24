/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.virtualdataconnector.igc.connectors.eventmapper;


public class IGCObject {
    protected String _id;
    protected String _type;
    protected String _url;
    protected String _name;

    public String get_url() {
        return _url;
    }

    public String get_name() { return _name; }

    public String get_id() {
        return _id;
    }

    public String get_type() {
        return _type;
    }
}
