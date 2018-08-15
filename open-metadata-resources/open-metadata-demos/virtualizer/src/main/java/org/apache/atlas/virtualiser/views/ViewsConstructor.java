/* SPDX-License-Identifier: Apache-2.0 */
package org.apache.atlas.virtualiser.views;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.atlas.omas.informationview.events.*;
import org.apache.atlas.virtualiser.ffdc.VirtualiserCheckedException;
import org.apache.atlas.virtualiser.ffdc.VirtualiserErrorCode;
import org.apache.atlas.virtualiser.kafka.KafkaVirtualiserProducer;
import org.apache.atlas.virtualiser.util.PropertiesHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * ViewsConstructor is used to create technical and business views for Information View OMAS. And
 * two json files will be sent out to Information View In topic.
 */
public class ViewsConstructor {
    private static final Logger log = LoggerFactory.getLogger(ViewsConstructor.class);
    /**
     * this is used by events to create and read json
     */
    private static ObjectMapper mapper = new ObjectMapper();


    /**
     * notify Information View OMAS
     * @param columnContextEvent it contains the information from json which is sent from Information View
     * @return String of business and technical views
     */
    public String[] notifyIVOMAS(ColumnContextEvent columnContextEvent){
        if (columnContextEvent==null){
            if(log.isDebugEnabled()){
                log.debug("Object ColumnContextEvent is null");
            }
            return null;
        }
        String[] views=null;
        try {
            views=createViews(columnContextEvent);
            //none are mapped
            if(views==null){
                if(log.isDebugEnabled()){
                    log.debug("There are no mapped columns");
                }
                return null;
            }
            sendNotification(views);
        } catch (VirtualiserCheckedException e) {
            log.error("Exception: Not able to create views or notify Information View OMAS.",e);
            e.printStackTrace();
        }
        if(log.isDebugEnabled()){
            log.debug("Views are created and notification is sent out to Information View OMAS");
        }
        return views;
    }

    /**
     * create technical view and technical view
     * @param columnContextEvent it contains the information from json which is sent from Information View
     * @return two string which are jsons for technical view and business view
     */
    private String[] createViews(ColumnContextEvent columnContextEvent) throws VirtualiserCheckedException {
        final String methodName="createViews";
        String[] views={"",""};
        InformationViewEvent businessView=new InformationViewEvent();
        InformationViewEvent technicalView=new InformationViewEvent();

        //better way is to deep copy properties of InformationViewEvent
        //we can implement constructor contains InformationViewEvent as a parameter

        //get common part
        String tableQualifiedName=columnContextEvent.getTableQualifiedName();
        String tableTypeQualifiedName=columnContextEvent.getTableTypeQualifiedName();
        String databaseQualifiedName=columnContextEvent.getDatabaseQualifiedName();
        String schemaQualifiedName=columnContextEvent.getSchemaQualifiedName();
        String schemaTypeQualifiedName=columnContextEvent.getSchemaTypeQualifiedName();
        String tableName=columnContextEvent.getTableName();
        String databaseName=columnContextEvent.getDatabaseName();
        String schemaName=columnContextEvent.getSchemaName();
        //set up ConnectionDetails
        ConnectionDetails connectionDetails =columnContextEvent.getConnectionDetails();
        if (connectionDetails ==null){
            connectionDetails =new ConnectionDetails();
        }
        connectionDetails.setConnectorProviderName(PropertiesHelper.properties.getProperty("connector_provider_name"));


        //create business view
        businessView.setTableQualifiedName(tableQualifiedName);
        businessView.setTableTypeQualifiedName(tableTypeQualifiedName);
        businessView.setSchemaQualifiedName(schemaQualifiedName);
        businessView.setSchemaTypeQualifiedName(schemaTypeQualifiedName);
        businessView.setDatabaseQualifiedName(databaseQualifiedName);
        businessView.setTableName(tableName);
        businessView.setConnectionDetails(connectionDetails);
        businessView.setDatabaseName(databaseName);
        businessView.setSchemaName(schemaName);

        //create technical view
        technicalView.setTableQualifiedName(tableQualifiedName);
        technicalView.setTableTypeQualifiedName(tableTypeQualifiedName);
        technicalView.setSchemaQualifiedName(schemaQualifiedName);
        technicalView.setSchemaTypeQualifiedName(schemaTypeQualifiedName);
        technicalView.setDatabaseQualifiedName(databaseQualifiedName);
        technicalView.setTableName(tableName);
        technicalView.setConnectionDetails(connectionDetails);
        technicalView.setSchemaName(schemaName);
        technicalView.setDatabaseName(databaseName);
        //      views[1]=createTechnicalView(columnContextEvent,technicalView);

        List<ColumnDetails> columnDetailsList=columnContextEvent.getTableColumns();
        //add derived columns to the view
        List<DerivedColumnDetail> technicalDerivedColumns=new ArrayList<>();
        List<DerivedColumnDetail> businessDerivedColumns=new ArrayList<>();

        for(ColumnDetails columnDetails:columnDetailsList){
            if(columnDetails.getBusinessTerm()!=null){
                DerivedColumnDetail technicalCol=new DerivedColumnDetail();
                DerivedColumnDetail businessCol=new DerivedColumnDetail();

                //set business name
                businessCol.setAttributeName(columnDetails.getBusinessTerm().getName());
                //set technical name
                technicalCol.setAttributeName(columnDetails.getAttributeName());

                technicalCol.setPosition(columnDetails.getPosition());
                technicalCol.setType(columnDetails.getType());
                technicalCol.setRealColumn(columnDetails);
                //add this column to technicalDerivedColumns
                technicalDerivedColumns.add(technicalCol);

                businessCol.setPosition(columnDetails.getPosition());
                businessCol.setType(columnDetails.getType());
                businessCol.setRealColumn(columnDetails);
                //add this column to businessDerivedColumns
                businessDerivedColumns.add(businessCol);
            }
        }
        //if there is no columns then it should be set as null
        if(businessDerivedColumns.size()==0||technicalDerivedColumns.size()==0){
            businessDerivedColumns=null;
            technicalDerivedColumns=null;
            return null;
        }
        //add derived columns to technical view
        technicalView.setDerivedColumns(technicalDerivedColumns);

        //add derived columns to business view
        businessView.setDerivedColumns(businessDerivedColumns);

        try {
            views[0]= mapper.writeValueAsString(businessView);
            views[1]=mapper.writeValueAsString(technicalView);
        } catch (JsonProcessingException e) {
           /*
            *  Wrap exception in the VirtualiserCheckedException with a suitable message
            *  when the execution failed
            */
            VirtualiserErrorCode errorCode = VirtualiserErrorCode.JACKSON_PARSE_FAIL;

            String        errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage(methodName);

            throw new VirtualiserCheckedException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction(),
                    e);
        }


        return views;
    }

    /**
     * send the notification to Information View OMAS
     * @param views technical view json and business view json
     */
    private void sendNotification(String[] views){
        KafkaVirtualiserProducer kafkaVirtualiserProducer=new KafkaVirtualiserProducer();
        try {
            //update business view
            kafkaVirtualiserProducer.runProducer(views[0]);
            //update technical view
            kafkaVirtualiserProducer.runProducer(views[1]);
            //close producer after send out two views
            kafkaVirtualiserProducer.closeProducer();

        } catch (VirtualiserCheckedException e) {
            e.printStackTrace();
        }



    }


}
