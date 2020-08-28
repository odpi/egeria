/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React, { useState, useEffect } from "react";
import { InfoSection, InfoCard } from "../../Info/Info";
import GlossaryImage from "../../../images/Egeria_glossary_32";
import getNodeType from "./properties/NodeTypes.js";
import { issueRestGet } from "./RestCaller";

export default function GlossaryAuthorNavigation() {
  const [errorMsg, setErrorMsg] = useState();
  const [exceptionUserAction, setExceptionUserAction] = useState();
  const [responseCategory, setResponseCategory] = useState();
  const [exceptionErrorMessage, setExceptionErrorMessage] = useState();
  const [systemAction, setSystemAction] = useState();
  const [fullResponse, setFullResponse] = useState();
  const [glossaries, setGlossaries] = useState([]);
  const nodeType = getNodeType("glossary");

  useEffect(() => {
    getGlossaries();
  }, []);
  // issue search for first page of glossaries
  const getGlossaries = () => {

    // encode the URI. Be aware the more recent RFC3986 for URLs makes use of square brackets which are reserved (for IPv6)
    const url = encodeURI(
      nodeType.url + "?offset=0&pageSize=1000&searchCriteria=.*"
    );
    issueRestGet(url, onSuccessfulSearch, onErrorSearch);
  };
  const onErrorSearch = (msg) => {
    console.log("Error on Get " + msg);
    setErrorMsg(msg);
    setGlossaries([]);
  };
  const onSuccessfulSearch = (json) => {
    setGlossaries(json.result);
  };

  return (
    <div>
      <InfoSection heading="Glossaries" className="landing-page__r3">
        {glossaries.map((glossary) => (
          <InfoCard
            heading={glossary.name}
            body={glossary.description}
            icon={<GlossaryImage />}
          />
        ))}
      </InfoSection>
    </div>
  );
}