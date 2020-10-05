/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React, { useState } from "react";
import Add32 from "../../../images/Egeria_add_32";
import getNodeType from "./properties/NodeTypes.js";
import { Form, FormGroup, TextInput, Button } from "carbon-components-react";
import { issueRestCreate } from "./RestCaller";
import { useParams } from "react-router-dom";

export default function QuickTerms() {
  const currentNodeType = getNodeType("glossary");
  const [terms, setTerms] = useState([]);
  const [termResponses, setTermResponses] = useState([]);

  const url = getUrl();
  function getUrl() {
    const { guid } = useParams();
    return currentNodeType.url + "/" + guid + "/terms";
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
  }
  //return next term to create
  function onErrorCreate(msg) {
    //TODO;
  }

  const getGlossaryGuid = () => {
    const result = window.location.pathname.split("/");
    return result[result.length - 2];
  };

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
         
          {termResponses.map((item, index ) => {
            return (
              <div key={index}>Result {index} {item.result.name}{item.relatedHTTPCode}</div>
            );
          })}
         
          <Button type="submit" onClick={onSubmit} disabled={!validateForm()}>
            Create Terms on Server
          </Button>
        </FormGroup>
      </Form>
    </div>
  );
}
