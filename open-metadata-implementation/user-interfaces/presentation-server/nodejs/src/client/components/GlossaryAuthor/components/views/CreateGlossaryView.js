/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React, { useState } from "react";
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
import { Button } from "react-bootstrap";
import getNodeType from "../properties/NodeTypes.js";
import Info16 from "@carbon/icons-react/lib/information/16";
import {
  issueRestCreate
} from "../RestCaller";

export default function CreateGlossaryView(props) {
  const [createBody, setCreateBody] = useState({});
  const [createdNode, setCreatedNode] = useState();
  const [errorMsg, setErrorMsg] = useState();
  const currentNodeType = getNodeType("glossary");

  console.log("CreateGlossaryView");

  /**
   * If there was an error the button has a class added to it to cause it to shake. After the animation ends, we need to remove the class.
   * @param {*} e end anomation event
   */
  const handleOnAnimationEnd = (e) => {
    document.getElementById("NodeCreateViewButton").classList.remove("shaker");
  };

  const handleClick = (e) => {
    console.log("handleClick(()");
    e.preventDefault();
    let body = createBody;
    const nodeType = currentNodeType;
    if (nodeType.typeForCreate) {
      body.nodeType = nodeType.nodeTypeForCreate;
    } else {
      body.nodeType = nodeType.typeName;
    }
    // TODO consider moving this up to a node controller as per the CRUD pattern.
    // inthe meantime this will be self contained.
    const url = currentNodeType.url;
    console.log("issueCreate " + url);
    issueRestCreate(url, body, onSuccessfulCreate, onErrorCreate);
  };
  const onSuccessfulCreate = (json) => {
    console.log("onSuccessfulCreate");
    if (json.result.length == 1) {
      const node = json.result[0];
      setCreatedNode(node);
    } else {
      onErrorGet("Error did not get a node from the server");
    }
  };
  const onErrorCreate = (msg) => {
    console.log("Error on Get " + msg);
    setErrorMsg(msg);
    setCreatedNode(undefined);
  };
  const validateForm = () => {
    //TODO consider marking name as manditory in the nodetype definition
    //return createBody.name && createBody.name.length > 0;

    return true;
  };
  const onErrorGet = (msg) => {
    console.log("Error on Get " + msg);
    setErrorMsg(msg);
  };
  const createLabelId = (labelKey) => {
    return "text-input-" + labelKey;
  };
  const setAttribute = (item, value) => {
    console.log("setAttribute " + item.key + ",value=" + value);
    let myCreateBody = createBody;
    myCreateBody[item.key] = value;
    setCreateBody(myCreateBody);
  };
  const createdTableHeaderData = [
    {
      header: "Attribute Name",
      key: "attrName",
    },
    {
      header: "Value",
      key: "value",
    },
  ];

  const getCreatedTableTitle = () => {
    return "Successfully created " + createdNode.name;
  };

  const getCreatedTableAttrRowData = () => {
    let rowData = [];
    const attributes = currentNodeType.attributes;

    for (var prop in createdNode) {
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

        let value = createdNode[prop];
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
    const systemAttributes = createdNode.systemAttributes;
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

  const createAnother = () => {
    setCreatedNode(undefined);
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
      {createdNode != undefined && (
        <div>
          <DataTable
            isSortable
            rows={getCreatedTableAttrRowData()}
            headers={createdTableHeaderData}
            render={({ rows, headers, getHeaderProps }) => (
              <TableContainer title={getCreatedTableTitle()}>
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
                  headers={createdTableHeaderData}
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
                onClick={createAnother}
                type="button"
              >
                Create Another
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

      {createdNode == undefined && (
        <div>
          <form>
            <div>
              <h4>
                Create {currentNodeType ? currentNodeType.typeName : ""}
                <Info16 />
              </h4>
            </div>

            {currentNodeType &&
              createdNode == undefined &&
              currentNodeType.attributes.map((item) => {
                return (
                  <div className="bx--form-item" key={item.key}>
                    <label
                      htmlFor={createLabelId(item.key)}
                      className="bx--label"
                    >
                      {item.label} <Info16 />
                    </label>
                    <input
                      id={createLabelId(item.key)}
                      type="text"
                      className="bx--text-input"
                      value={item.name}
                      onChange={(e) => setAttribute(item, e.target.value)}
                      placeholder={item.label}
                    ></input>
                  </div>
                );
              })}
            <Accordion>
              <AccordionItem title="Advanced options">
                <DatePicker dateFormat="m/d/Y" datePickerType="range">
                  <DatePickerInput
                    id="date-picker-range-start"
                    placeholder="mm/dd/yyyy"
                    labelText="Effective from date"
                    type="text"
                  />
                  <DatePickerInput
                    id="date-picker-range-end"
                    placeholder="mm/dd/yyyy"
                    labelText="Effective to date"
                    type="text"
                  />
                </DatePicker>
              </AccordionItem>
            </Accordion>

            <div className="bx--form-item">
              <button
                id="NodeCreateViewButton"
                className="bx--btn bx--btn--primary"
                disabled={!validateForm()}
                onClick={handleClick}
                onAnimationEnd={handleOnAnimationEnd}
                type="button"
              >
                Create
              </button>
            </div>
          </form>
        </div>
      )}
    </div>
  );
}
