/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React, { useState, useEffect } from "react";
import {
  Accordion,
  AccordionItem,
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
import getNodeType from "./properties/NodeTypes.js";
import Info16 from "@carbon/icons-react/lib/information/16";
import {
  issueRestUpdate,
  issueRestGet
} from "./RestCaller";

export default function UpdateGlossary(props) {
  const [updateBody, setUpdateBody] = useState({});
  const [updatedNode, setUpdatedNode] = useState();
  const currentNodeType = getNodeType("glossary");
  const [errorMsg, setErrorMsg] = useState();
  console.log("UpdateGlossary");

  const initialise = () => {
    issueRestGet(getUrl(), onSuccessfulGet, onErrorGet)
    return "Getting details";
  }
  const getUrl = () => {
    const {guid} = useParams();
    return currentNodeType.url + "/" + guid;
  }

  /**
   * If there was an error the button has a class added to it to cause it to shake. After the animation ends, we need to remove the class.
   * @param {*} e end anomation event
   */
  const handleOnAnimationEnd = (e) => {
    document.getElementById("NodeUpdateViewButton").classList.remove("shaker");
  };

  function issueGet() {
    console.log("issueGet " + getUrl());
    issueRestGet(getUrl(), onSuccessfulGet, onErrorGet);
  }

  const handleClick = (e) => {
    console.log("handleClick(()");
    e.preventDefault();
    let body = updateBody;
 
    // TODO consider moving this up to a node controller as per the CRUD pattern.
    // in the meantime this will be self contained.

    console.log("issueUpdate " + getUrl());
    issueRestUpdate(getUrl(), body, onSuccessfulUpdate, onErrorUpdate);
  };
  const onSuccessfulGet = (json) => {
    console.log("onSuccessfulUpdate");
    if (json.result.length == 1) {
      const node = json.result[0];
      setUpdatedNode(node);
    } else {
      onErrorGet("Error did not get a node from the server");
    }
  };
  const onErrorGet = (msg) => {
    console.log("Error on Get " + msg);
    setErrorMsg(msg);
    setUpdatedNode(undefined);
  };
  const onSuccessfulUpdate = (json) => {
    console.log("onSuccessfulUpdate");
    if (json.result.length == 1) {
      const node = json.result[0];
      setUpdatedNode(node);
    } else {
      onErrorGet("Error did not get a node from the server");
    }
  };
  const onErrorUpdate = (msg) => {
    console.log("Error on Update " + msg);
    setErrorMsg(msg);
    setUpdatedNode(undefined);
  };
  const validateForm = () => {
    //TODO consider marking name as manditory in the nodetype definition
    //return updateBody.name && updateBody.name.length > 0;

    return true;
  };
  const updateLabelId = (labelKey) => {
    return "text-input-" + labelKey;
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
    return "Successfully updated " + updatedNode.name;
  };

  const getUpdatedTableAttrRowData = () => {
    let rowData = [];
    const attributes = currentNodeType.attributes;

    for (var prop in updatedNode) {
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

        let value = updatedNode[prop];
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
    const systemAttributes = updatedNode.systemAttributes;
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
    setupdatedNode(undefined);
  }
  const onClickBack = () => {
    console.log("Back clicked");
    // use props.history, as there is another window history object in scope in the event listener  
    console.log(props.history);
    // go  back 
    props.history.goBack();
  }
  return (
    <div>
       {updatedNode == undefined && initialise() } 
      {updatedNode != undefined && (
        <div>
          <DataTable
            isSortable
            rows={getUpdatedTableAttrRowData()}
            headers={updatedTableHeaderData}
            render={({ rows, headers, getHeaderProps }) => (
              <TableContainer title={getUpdatedTableTitle()}>
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
                                <TableCell key={cell.id}>
                                  {cell.value}
                                </TableCell>
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
          <button
                className="bx--btn bx--btn--primary"
                onClick={updateAnother}
                type="button"
              >
                Update Again
              </button>
              <button
                className="bx--btn bx--btn--primary"
                onClick={onClickBack}
                type="button"
              >
                Back
              </button>
        </div>
      )}
    </div>
  );
}
