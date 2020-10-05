/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React, { useState } from "react";
import Add32 from "../../../images/Egeria_add_32";
import getNodeType from "./properties/NodeTypes.js";
import { Form, FormGroup, TextInput, Button } from "carbon-components-react";
import { issueRestCreate } from "./RestCaller";
import { useParams } from "react-router-dom";

export default function QuickTerms() {
  const glossaryNodeType = getNodeType("glossary");
  const termNodeType = getNodeType("term");
  const [terms, setTerms] = useState([]);
  const [termResponses, setTermResponses] = useState([]);

  const url = getUrl();
  function getUrl() {
    const { guid } = useParams();
    return glossaryNodeType.url + "/" + guid + "/terms";
  }
  const validateForm = () => {
    return true;
  };
  const onAdd = (e) => {
    console.log("onAdd");
    let newTerm = {};
    newTerm.name = "";
    newTerm.description = "";
    const workingTerms = [...terms, newTerm];
    setTerms(workingTerms);
  };

  const onSubmit = (e) => {
    console.log("onSubmit");
    e.preventDefault();

    if (terms.length > 0) {
      console.log("issueUpdate " + url);
      issueRestCreate(url, terms, onSuccessfulCreate, onErrorCreate);
    } else {
      alert("Nothing to create");
    }
  };
  const onSuccessfulCreate = (json) => {
    setTermResponses(json.result);
  };
  //return next term to create
  function onErrorCreate(msg) {
    //TODO;
  }

  // properties that will be displayed be default for a node
  const mainProperties = [
    {
      key: "name",
      text: "Name",
    },
    {
      key: "description",
      text: "Description",
    },
    {
      key: "qualifiedName",
      text: "Qualified Name",
    },
  ];

  const [headerData, setHeaderData] = useState(mainProperties);
  const additionalProperties = calculateAdditionalProperties();
  let selectedAdditionalProperties = [];

  // calculate the results table header - this will be the default columns plus any additional coliumns the user has specified
  function calculateHeaderData() {
    let allProperties = mainProperties;
    if (
      selectedAdditionalProperties !== undefined &&
      selectedAdditionalProperties &&
      selectedAdditionalProperties.length > 0
    ) {
      allProperties = mainProperties.concat(selectedAdditionalProperties);
    }
    console.log(allProperties);
    setHeaderData(allProperties);
  }

  // Additional attributes can be selected so more columns can be shown
  // the additional attriniutes are in selectedAdditionalProperties
  const onAdditionalAttributesChanged = (items) => {
    console.log("onAdditionalAttributesChanged");
    console.log(items.selectedItems);
    selectedAdditionalProperties = [];
    const selectedItems = items.selectedItems;
    for (let i = 0; i < selectedItems.length; i++) {
      let item = {};
      item.key = selectedItems[i].id;
      item.text = selectedItems[i].text;
      selectedAdditionalProperties.push(item);
    }
    // render the table by recalculating the header state based on the new values
    calculateHeaderData();
  };
  // calculate the columns from the main attributes and the additional attributes.
  function calculateAdditionalProperties() {
    let items = [];
    termNodeType.attributes.map(function (attribute) {
      if (
        attribute.key != "name" &&
        attribute.key != "qualifiedName" &&
        attribute.key != "description"
      ) {
        let item = {};
        item.id = attribute.key;
        item.text = attribute.label;
        items.push(item);
      }
    });
    return items;
  }

  function setTermName(index, name) {
    let workingTerms = terms;
    workingTerms[index].name = name;
    setTerms(workingTerms);
  }
  function setTermDescription(index, description) {
    let workingTerms = terms;
    workingTerms[index].description = description;
    setTerms(workingTerms);
  }

  return (
    <div>
      {termResponses.length == 0 && (
        <div>
          <h3>Quickly add terms.</h3>
          <Add32 kind="primary" onClick={() => onAdd()} />
          <Form>
            <FormGroup>
              {terms.map((item, index) => {
                return (
                  <div className="bx--form-item" key={index}>
                    <div className="row-container">
                      <TextInput
                        type="text"
                        placeholder="Term name"
                        defaultValue={item.name}
                        onChange={(e) => {
                          setTermName(index, e.target.value);
                        }}
                      />
                      <TextInput
                        type="text"
                        placeholder="Term description"
                        defaultValue={item.description}
                        onChange={(e) => {
                          setTermDescription(index, e.target.value);
                        }}
                      />
                      <div>{item.result}</div>
                    </div>
                  </div>
                );
              })}
              <Button
                type="submit"
                onClick={onSubmit}
                disabled={!validateForm()}
              >
                Create Terms on Server
              </Button>
            </FormGroup>
          </Form>
        </div>
      )}
      {termResponses.length > 0 && (
        <div>
          {termResponses.map((item, index) => {
            return (
              <div key={index}>
                Result {index} {item.result.name}  {item.relatedHTTPCode}
              </div>
            );
          })}
        </div>
      )}
    </div>
  );
}
