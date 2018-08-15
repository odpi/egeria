/* SPDX-License-Identifier: Apache-2.0 */
package org.apache.atlas.virtualiser.gaian;

import org.apache.atlas.omas.informationview.events.ColumnContextEvent;
import org.apache.atlas.omas.informationview.events.ColumnDetails;
import org.apache.atlas.virtualiser.ffdc.VirtualiserCheckedException;
import org.apache.atlas.virtualiser.ffdc.VirtualiserErrorCode;
import org.apache.atlas.virtualiser.util.ExecuteQueryUtil;
import org.apache.atlas.virtualiser.util.PropertiesHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * GaianQueryConstructor creates or removes Logical Tables.
 */
public class GaianQueryConstructor {
    //prefix for technical Logical Table
    private static String TECHNICAL="LTT";
    //prefix for business Logical Tabble
    private static String BUSINESS="LTB";
    //general Logical Table in the back-end Gaian created by DBA
    private static String GENERAL="General";
    //Logical Table name create in the back end Gaian node
    private String LTName;
    //the table has been changed
    private String tableName;
    //relational database name, ie mysql,derby,db2 or oracle
    private String rdbName;
    //the database name
    private String databaseName;
    //the schema name
    private String schemaName;
    //where the gaian node is hosted
    private String gaianNodeName;
    //Logical Table technical view name
    private String LTTName;
    //Logical Table business view name
    private String LTBName;
    //update Logical Table business
    private String setLTB;
    //update Logical Table technical
    private String setLTT;
    //util for connection to Gaian
    private ExecuteQueryUtil executeQueryUtil;
    //columns with assigned business terms
    private List<MappedColumn> mappedColumns;
    //no mapped columns in this table
    private boolean noneMapped=false;
    //connect data source to the Logical Table technical
    private String setLTTDataSource;
    //connect data source to the Logical Table business;
    private String setLTBDataSource;
    //remove the general Logical Table created in the front-end Gaian
    private  String removeLT;
    //set the general Logical Table for the front-end Gaian
    private String setLTForNode;
    //Logical Tables in the whole network
    private List<LogicTable> logicTableList;
    //back end Gaian general Logical Table
    private LogicTable backEndLT;
    private static final Logger log = LoggerFactory.getLogger(GaianQueryConstructor.class);
    /**
     * based on the json file, create Logical Tables and update Gaian
     * @param columnContextEvent json file from Information View OMAS
     * @return whether create or remove tables correctly
     * @throws VirtualiserCheckedException when there is no Logical Table created for the table in the Gaian node
     */
    public boolean notifyGaian(ColumnContextEvent columnContextEvent) throws VirtualiserCheckedException {
        final String methodName="notifyGaian";
        if (columnContextEvent == null) {
            if(log.isDebugEnabled()){
                log.debug("Object ColumnContextEvent is null");
            }
            return false;
        }

        //set up basic information
        setUpBasicInformation(columnContextEvent);


        //connection to gaian
        executeQueryUtil = new ExecuteQueryUtil();
        try {
            executeQueryUtil.getConnection();
            if(log.isDebugEnabled()){
                log.debug("Connect to Gaian");
            }
        } catch (VirtualiserCheckedException e) {
            log.error("Exception: Not able to connect to Gaian.",e);
            e.printStackTrace();
        }
        //if all the columns have no business terms
        if (noneMapped) {
            if(log.isDebugEnabled()){
                log.debug("There are no mapped columns");
            }
            // if we have created Logical Tables for this table, then we delete all the Logical Tables
            if (hasMatchedLogicTable(PropertiesHelper.properties.getProperty("gaian_front_end_name"), false)) {
                if(log.isDebugEnabled()){
                    log.debug("Remove existed technical and business Logical Tables");
                }
                removeLTBAndLTT();
            }
            if(log.isDebugEnabled()){
                log.debug("Gaian is successfully updated");
            }
            return true;
        } else {
            // if there is a general Logical Table created
            if (hasMatchedLogicTable(gaianNodeName, true)) {
                setLTForNode();
                if(log.isDebugEnabled()){
                    log.debug("Set up Logical Table for Gaian node");
                }


                updateColumnDataType();


                //update LTB
                updateLTB();
                //connect data source to the Logical Table business
                setLTBDataSource=setLTDataSource(LTBName);


                //update LTT
                updateLTT();

                //connect data source to the Logical Table technical
                setLTTDataSource=setLTDataSource(LTTName);

                //remove Logical Table
                removeLT();


                //update Gaian
                updateGaian();
                if(log.isDebugEnabled()){
                    log.debug("Gaian is successfully updated");
                }


                //disconnect Gaian
                try {
                    executeQueryUtil.disconnect();
                    if(log.isDebugEnabled()){
                        log.debug("Disconnect from Gaian");
                    }
                } catch (VirtualiserCheckedException e) {
                    log.error("Exception: Not able to disconnect from Gaian.",e);
                    e.printStackTrace();
                }

                return true;
            } else {
                //, then throws an exception
                /*
                *  Wrap exception in the VirtualiserCheckedException with a suitable message
                *  when there is no Logical Table created for the table in this Gaian node
                */
                VirtualiserErrorCode errorCode = VirtualiserErrorCode.NO_lOGICTABLE;

                String        errorMessage = errorCode.getErrorMessageId()
                        + errorCode.getFormattedErrorMessage(tableName,gaianNodeName);

                throw new VirtualiserCheckedException(errorCode.getHTTPErrorCode(),
                        this.getClass().getName(),
                        methodName,
                        errorMessage,
                        errorCode.getSystemAction(),
                        errorCode.getUserAction(),
                        null);
            }
        }
    }


    /**
     * check whether on the given Gaian node, there is already exist Logical Table
     * @param gaianNodeName the Gaian node name
     * @param logicTableInBackend whether it checks the front-end Gaian or back-end Gaian
     * @return the Logical Table exists or not
     */

    private boolean hasMatchedLogicTable(String gaianNodeName, boolean logicTableInBackend){
         logicTableList=new ArrayList<>();
        try {
            logicTableList=executeQueryUtil.getLogicTables(PropertiesHelper.properties.getProperty("get_logic_tables"));
        } catch (VirtualiserCheckedException e) {
            log.error("Exception: Not able to execute query in Gaian.",e);
            e.printStackTrace();
        }
        if(logicTableList!=null){
            for(LogicTable logicTable:logicTableList){
                // find the Gaian node
               if(logicTable.getGDBNode().equals(gaianNodeName)){
                   //find the Logical Table stored in the back-end
                   if(logicTableInBackend){
                       if(logicTable.getLTName().equals(LTName)){
                           backEndLT=logicTable;
                           return true;
                       }
                   }else {
                       //find whether it contains the technical or business name because they always exist together(create or remove together)
                       if(logicTable.getLTName().equals(LTTName)|| logicTable.getLTName().equals(LTBName)){
                           return true;
                       }
                   }

                }
            }
        }
        return false;
    }


    /**
     * set up basic information based on the json file
     * @param columnContextEvent json file from Information View OMAS
     */
    private void setUpBasicInformation(ColumnContextEvent columnContextEvent){
        tableName=columnContextEvent.getTableName();
        databaseName=columnContextEvent.getDatabaseName();
        schemaName=columnContextEvent.getSchemaName();
        rdbName=columnContextEvent.getConnectionDetails().getProtocol().split(":")[1];

        Pattern p = Pattern.compile("(?<=\\/\\/)(.*?)(?=:)");

        //pattern to get hostname and assign to gaianNode
        Matcher m = p.matcher(columnContextEvent.getConnectionDetails().getNetworkAddress());
        if (m.find()) {
            gaianNodeName = m.group(1).replace(".","");
        }
        LTTName=getLogicTableName(TECHNICAL);
        LTBName=getLogicTableName(BUSINESS);
        LTName=getLogicTableName(GENERAL);

        //get columns which have assigned business terms
        mappedColumns=new ArrayList<>();
        List<ColumnDetails> columnDetailsList=columnContextEvent.getTableColumns();
        for(ColumnDetails columnDetails:columnDetailsList){
            if(columnDetails.getBusinessTerm()!=null){
                MappedColumn mappedColumn=new MappedColumn();
                mappedColumn.setBusinessName(columnDetails.getBusinessTerm().getName().replace(" ","_"));
                mappedColumn.setType(columnDetails.getType());
                mappedColumn.setTechnicalName(columnDetails.getAttributeName());
                //add this column to list
                mappedColumns.add(mappedColumn);
            }
        }
        //no columns have a assigned business term
        if(mappedColumns.size()==0){
            noneMapped=true;
        }
    }

    /**
     * get Logical Table name
     * @param type business or technical
     * @return Logical Table's name
     */
    private String getLogicTableName(String type){
        String name="";
        if(type.equals(GENERAL)){
            //form Logical Table's name for the back-end Gaian
            name=rdbName+"_"+ databaseName +"_"+ tableName;
        }else{
            //form Logical Table's name for the front-end Gaian
            name=type+"_"+ gaianNodeName +"_"+rdbName+"_"+ databaseName +"_"+ tableName;
        }
        return name.toUpperCase();
    }

    /**
     * Set Logical Table manually; with columns definitions and constant columns definitions.
     */
    private void updateLTB(){
        //there is at least one column has a business term
        setLTB = "call setlt('" +
                LTBName +
                "','";
        for (MappedColumn mappedColumn : mappedColumns) {
            setLTB += mappedColumn.getBusinessName() + " " + mappedColumn.getType() + ",";
        }
        setLTB = setLTB.substring(0, (setLTB.length() -1));
        setLTB += "','')";
    }

    /**
     * remove the business and technical Logical Tables created in the front-end Gaian
     */
    private void removeLTBAndLTT(){
        //there is no columns have business terms, then remove the Logical Table created for this table
        String removeLTB = "call removelt('" +
                LTBName + "')";
        String removeLTT = "call removelt('" +
                LTTName + "')";
        try {
            executeQueryUtil.executeUpdate(removeLTB);
            executeQueryUtil.executeUpdate(removeLTT);
        } catch (VirtualiserCheckedException e) {
            log.error("Exception: Not able to execute update in Gaian.",e);
            e.printStackTrace();
        }
        setLTB = "";
        setLTT="";
    }

    /**
     * connect table schema(format: technical_name datatype)
     * only mapped columns will show up in Logical Table technical
     * @return connected string
     */
    private String connectTableSchemaStrings(){
        String string = "";
        for (MappedColumn tableSchema: mappedColumns){
            string+=tableSchema.getTechnicalName()+" "+ tableSchema.getType()+" ";
            string += ",";

        }
        if (string.length()>0) {
            return string.substring(0, (string.length() -1));
        }
        return string;
    }

    /**
     * update the Logical Table technical
     */
    private void updateLTT(){
        setLTT = "call setlt('"+
                LTTName+"','"+
                connectTableSchemaStrings()+"','')";
    }

    /**
     * remove the Logical Table
     */
    private void removeLT(){
        removeLT= "call removelt('" + LTName + "')";
    }


    //update the Logical Tables
    private void updateGaian(){
        try {
            executeQueryUtil.executeUpdate(setLTT);
            System.out.println(setLTTDataSource);
            executeQueryUtil.executeUpdate(setLTTDataSource);
            executeQueryUtil.executeUpdate(setLTB);
            System.out.println(setLTBDataSource);
            executeQueryUtil.executeUpdate(setLTBDataSource);
            executeQueryUtil.executeUpdate(removeLT);
        } catch (VirtualiserCheckedException e) {
            log.error("Exception: Not able to execute update in Gaian.",e);
            e.printStackTrace();
        }

    }


    /**
     * connect the data source to the Logical Table
     * @param logicalTableName name of the Logical table
     * @return the call to Gaian
     */
    private String setLTDataSource(String logicalTableName){
        String setLTDataSource="call setdsrdbtable('" +
                logicalTableName +
                "', '', '" +
                gaianNodeName +
                "', '" +
                tableName +
                "','', '";

        for (MappedColumn mappedColumn : mappedColumns) {
            setLTDataSource += mappedColumn.getTechnicalName() + ",";
        }
        setLTDataSource = setLTDataSource.substring(0, (setLTDataSource.length() -1));
        setLTDataSource += "')";
        return setLTDataSource;
    }

    /**
     * Set Logical Table mirroring the definition for the given Logical Table Name on another GaianDB node, so its data can be queried remotely.
     */
    private void setLTForNode(){
        setLTForNode = "call setltfornode('"+
                LTName+ "','" +
                gaianNodeName +"')";
        //set Logical Table for node
        try {
            executeQueryUtil.executeUpdate(setLTForNode);
        } catch (VirtualiserCheckedException e) {
            log.error("Exception: Not able to execute update in Gaian.",e);
            e.printStackTrace();
        }
    }





    /**
     * set the real column data type to columns
     */
    private void updateColumnDataType(){
        for(MappedColumn mappedColumn:mappedColumns){
            for (Map.Entry<String, String> column : backEndLT.getLTDef().entrySet())
            {
                if(mappedColumn.getTechnicalName().equals(column.getKey())){
                    mappedColumn.setType(column.getValue());
                    break;
                }
            }






        }
    }


}
