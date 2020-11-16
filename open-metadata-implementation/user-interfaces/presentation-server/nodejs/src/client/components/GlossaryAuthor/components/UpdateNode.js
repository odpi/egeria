/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React, { useState } from "react";
import {
  Accordion,
  AccordionItem,
  Button,
  DatePicker,
  DatePickerInput,
  DataTable,
  TableContainer,
  Table,
  TableHead,
  TableRow,
  TableCell,
  TableHeader,
  TableBody,
} from "carbon-components-react";
import { useParams } from "react-router-dom";
import Info16 from "@carbon/icons-react/lib/information/16";
import { issueRestUpdate, issueRestGet } from "./RestCaller";
import { useHistory } from "react-router-dom";

export default function UpdateNode(props) {
  const [updateBody, setUpdateBody] = useState({});
  const [currentNode, setCurrentNode] = useState();
  const [errorMsg, setErrorMsg] = useState();
  console.log("UpdateNode");
  const url = getUrl();
  let history = useHistory();

  const initialGet = () => {
    issueRestGet(url, onSuccessfulGet, onErrorGet);
    return "Getting details";
  };
  function getUrl() {
    const { guidtoedit } = useParams();
    return props.currentNodeType.url + "/" + guidtoedit;
  }

  const handleClickUpdate = (e) => {
    console.log("handleClickUpdate()");
    e.preventDefault();
    let body = updateBody;

    // TODO consider moving this up to a node controller as per the CRUD pattern.
    // in the meantime this will be self contained.

    console.log("issueUpdate " + url);
    issueRestUpdate(url, body, onSuccessfulUpdate, onErrorUpdate);
  };
  const onSuccessfulGet = (json) => {
    console.log("onSuccessfulGet");
    if (json.result.length == 1) {
      const node = json.result[0];
      setCurrentNode(node);
    } else {
      onErrorGet("Error did not get a node from the server");
    }
  };
  const onErrorGet = (msg) => {
    console.log("Error on Get " + msg);
    setErrorMsg(msg);
    setCurrentNode(undefined);
  };
  const onSuccessfulUpdate = (json) => {
    console.log("onSuccessfulUpdate");
    if (json.result.length == 1) {
      const node = json.result[0];
      setCurrentNode(node);
    } else {
      onErrorGet("Error did not get a node from the server");
    }
  };
  const onErrorUpdate = (msg) => {
    console.log("Error on Update " + msg);
    setErrorMsg(msg);
    setCurrentNode(undefined);
  };
  const validateForm = () => {
    //TODO consider marking name as manditory in the nodetype definition
    //return updateBody.name && updateBody.name.length > 0;

    return true;
  };
  const updateLabelId = (labelKey) => {
    return "text-input-update"+props.currentNodeType.name +"-"+ labelKey;
  };
  const setAttribute = (item, value) => {
    console.log("setAttribute " + item.key + ",value=" + value);
    let myUpdateBody = updateBody;
    myUpdateBody[item.key] = value;
    setUpdateBody(myUpdateBody);
  };
  const updatedTableHeaderData = [
    {
      header: "Attribute Name",
      key: "attrName",
    },
    {
      header: "Value",
      key: "value",
    },
  ];

  const getUpdatedTableTitle = () => {
    return "Successfully updated " + currentNode.name;
  };

  const getUpdatedTableAttrRowData = () => {
    let rowData = [];
    const attributes = props.currentNodeType.attributes;

    for (var prop in currentNode) {
      if (
        prop != "systemAttributes" &&
        prop != "glossary" &&
        prop != "classifications" &&
        prop != "class"
      ) {
        let row = {};
        row.id = prop;
        row.attrName = prop;
        // if we know about the attribute then use the label.
        if (prop == "nodeType") {
          row.attrName = "Node Type";
        } else {
          for (var i = 0; i < attributes.length; i++) {
            if (attributes[i].key == prop) {
              row.attrName = attributes[i].label;
            }
          }
        }

        let value = currentNode[prop];
        // TODO deal with the other types (and null? and arrays?) properly
        value = JSON.stringify(value);
        row.value = value;
        rowData.push(row);
      }
    }
    return rowData;
  };
  const getSystemDataRowData = () => {
    let rowData = [];
    const systemAttributes = currentNode.systemAttributes;
    for (var prop in systemAttributes) {
      let row = {};
      row.id = prop;
      row.attrName = prop;

      let value = systemAttributes[prop];
      // TODO deal with the other types (and null? and arrays?) properly
      value = JSON.stringify(value);
      row.value = value;
      rowData.push(row);
    }
    return rowData;
  };

  const updateAnother = () => {
    setCurrentNode(undefined);
  };
  const onClickBack = () => {
    console.log("Back clicked");
    // use history, as there is another window history object in scope in the event listener
    console.log(history);
    // go  back
    history.goBack();
  };
  return (
    <div>
      {currentNode == undefined && initialGet()}
      {currentNode != undefined &&
        props.currentNodeType.attributes.map((item) => {
          return (
            <div className="bx--form-item" key={item.key}>
              <label htmlFor={updateLabelId(item.key)} className="bx--label">
                {item.label} <Info16 />
              </label>
              <input
                id={updateLabelId(item.key)}
                type="text"
                className="bx--text-input"
                defaultValue={currentNode[item.key]}
                key={currentNode[item.key]}
                onChange={(e) => setAttribute(item, e.target.value)}
                placeholder={item.label}
              ></input>
            </div>
          );
        })}
      {currentNode != undefined && (
        <Accordion>
          <AccordionItem title="Advanced options">
            <DatePicker dateFormat="m/d/Y" datePickerType="range">
              <DatePickerInput
                // id="date-picker-range-start"
                placeholder="mm/dd/yyyy"
                labelText="Effective from date"
                type="text"
              />
              <DatePickerInput
                // id="date-picker-range-end"
                placeholder="mm/dd/yyyy"
                labelText="Effective to date"
                type="text"
              />
            </DatePicker>
          </AccordionItem>
        </Accordion>
      )}
      {currentNode != undefined && (
        <Accordion>
          <AccordionItem title="System Attributes">
            <div className="bx--form-item">
              <DataTable
                isSortable
                rows={getSystemDataRowData()}
                headers={updatedTableHeaderData}
                render={({ rows, headers, getHeaderProps }) => (
                  <TableContainer title="System Attributes">
                    <Table size="normal">
                      <TableHead>
                        <TableRow>
                          {headers.map((header) => (
                            <TableHeader
                              key={header.key}
                              {...getHeaderProps({ header })}
                            >
                              {header.header}
                            </TableHeader>
                          ))}
                        </TableRow>
                      </TableHead>
                      <TableBody>
                        {rows.map((row) => (
                          <TableRow key={row.id}>
                            {row.cells.map((cell) => (
                              <TableCell key={cell.id}>{cell.value}</TableCell>
                            ))}
                          </TableRow>
                        ))}
                      </TableBody>
                    </Table>
                  </TableContainer>
                )}
              />
            </div>
          </AccordionItem>
        </Accordion>
      )}
       <div style={{ color: "red" }}>{errorMsg}</div>
      <Button
        className="bx--btn bx--btn--primary"
        onClick={handleClickUpdate}
        type="button"
      >
        Update
      </Button>
      <Button
        kind="secondary"
        className="bx--btn bx--btn--primary"
        onClick={onClickBack}
        type="button"
      >
        Back
      </Button>
    </div>
  );
}
