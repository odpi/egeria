/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React, { useState } from "react";
import Add32 from "../../../images/Egeria_add_32";
// import Success32 from "../../../images/Success_32";
import successIcon from '@carbon/icons/es/checkmark--filled/32/';
import { getAttributes, toSVG } from '@carbon/icon-helpers';
import getNodeType from "./properties/NodeTypes.js";
import { Button, Form, FormGroup, TextInput } from "carbon-components-react";

import { issueRestCreate } from "./RestCaller";
import { useParams } from "react-router-dom";

export default function QuickTerms(props) {
  const glossaryNodeType = getNodeType("glossary");
  const [terms, setTerms] = useState([]);
  const [termsWithStatus, setTermsWithStatus] = useState([]);
  const [errorMsg, setErrorMsg] = useState();

  const successIconNode = toSVG({
    ...successIcon,
    attrs: getAttributes(successIcon.attrs),
  });

  const url = getUrl();
  function getUrl() {
    const { guid } = useParams();
    return glossaryNodeType.url + "/" + guid + "/terms";
  }
  const validateForm = () => {
    if (terms.length == 0) {
      return false;
    } else {
      return true;
    }
  };
  const onAdd = () => {
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
    let workingTermsWithStatus = [];
    for (let i = 0; i < terms.length; i++) {
      let workingTermWithStatus = terms[i];
      if (terms[i].name.trim() == "") {
        workingTermWithStatus.status = "Error - blank name";
      } else if (json.result[i].relatedHTTPCode == "200") {
        workingTermWithStatus = json.result[i].result[0];
        workingTermWithStatus.status = "Success";
      } else {
        workingTermWithStatus.status = "Error";
      }
      workingTermsWithStatus[i] = workingTermWithStatus;
    }
    setTermsWithStatus(workingTermsWithStatus);
  };
  //return next term to create
  function onErrorCreate(msg) {
    setErrorMsg(msg);
  }
  const onClickBack = () => {
    console.log("Back clicked");
    if (termsWithStatus.length > 0) {
      setTerms([]);
      setTermsWithStatus([]);
    } else {
      // go back using the router history
      props.history.goBack();
    }
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
  const onSelectRow = (row) => {
    console.log("onSelectRow " + row);
  };

  return (
    <div>
      <div className="row-container">
        <button onClick={onClickBack} type="button">
          Back
        </button>
      </div>
      {termsWithStatus.length == 0 && (
        <div>
          <div className="row-container">
            <h3>Quickly add terms.</h3>
          </div>
          <div className="row-container">
            <Add32 kind="primary" onClick={() => onAdd()} />
          </div>
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
              <div className="row-container">
                <Button
                  type="submit"
                  onClick={onSubmit}
                  disabled={!validateForm()}
                >
                  Create Terms on Server
                </Button>
              </div>
            </FormGroup>
          </Form>
        </div>
      )}
      {termsWithStatus.length > 0 && (
        <div>
          <div className="row-container">
            <h3>Terms Added.</h3>
          </div>
          <Form>
            <FormGroup>
              <div className="bx--form-item">
                <div className="row-container">
                  <TextInput
                    type="text"
                    defaultValue="Name"
                    style={{ color: "grey" }}
                    readOnly
                  />
                  <TextInput
                    type="text"
                    defaultValue="Description"
                    style={{ color: "grey" }}
                    readOnly
                  />

                  <TextInput
                    type="text"
                    defaultValue="Status"
                    style={{ color: "grey" }}
                    readOnly
                  />
                </div>
              </div>
              {termsWithStatus.map((item, index) => {
                return (
                  <div className="bx--form-item" key={index}>
                    <div className="row-container">
                      <TextInput
                        type="text"
                        defaultValue={item.name}
                        readOnly
                      />
                      <TextInput
                        type="text"
                        defaultValue={item.description}
                        readOnly
                      />
                      {item.status == "Success" && (
                        <div className="row-container">
                          <TextInput
                            type="text"
                            defaultValue={item.status}
                            readOnly
                          />
                          <div className="left-20" alias="white_check_mark" src="https://github.githubassets.com/images/icons/emoji/unicode/2705.png">✅</div>
                        </div>
                      )}
                      {item.status != "Success" && (
                        <div>
                          <TextInput
                            type="text"
                            defaultValue={item.status}
                            readOnly
                          />
                        </div>
                      )}
                    </div>
                  </div>
                );
              })}
            </FormGroup>
          </Form>
          <article style={{ color: "red" }}>{errorMsg}</article>
        </div>
      )}
    </div>
  );
}
