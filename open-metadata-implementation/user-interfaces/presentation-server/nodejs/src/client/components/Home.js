/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React, { useContext } from "react";
import { InfoSection, InfoCard, LocalInfoCard } from "./Info/Info";
import GitHub32 from "../images/GitHub_logo_32";
import EgeriaColor64 from "../images/Egeria_logo_color_64";
import GlossaryAuthor32 from "../images/Egeria_glossary_author_32";
import { IdentificationContext } from "../contexts/IdentificationContext";

export default function Home() {
  const identificationContext = useContext(IdentificationContext);
  console.log("Home identificationContext", identificationContext);
  return (
    <div className="bx--grid">
      <InfoSection heading="Home" className="landing-page__r3">
        <InfoCard
          heading="Egeria GitHub "
          body="Develop Egeria - build and enhance."
          icon={<GitHub32 />}
          link="https://github.com/odpi/egeria"
        />
        <InfoCard
          heading="Egeria project webpage"
          body="Find out more about Egeria, which provides the Apache 2.0 licensed open metadata and governance type system, frameworks, APIs, event payloads and interchange protocols to enable tools, engines and platforms to exchange metadata in order to get the best value from data whilst ensuring it is properly governed."
          icon={<EgeriaColor64 />}
          link="https://egeria.odpi.org/"
        />
        <LocalInfoCard
          heading="Glossary Author"
          body="Author glossary content to define the meaning and the relationships between different types of terminology."
          icon={<GlossaryAuthor32 />}
          link={identificationContext.getBrowserURL("glossary-author")}
        />
      </InfoSection>

      {/* <div class="bx--aspect-ratio bx--aspect-ratio--1x1">
  <div class="bx--aspect-ratio--object">1:1a</div>
  <div class="bx--aspect-ratio--object">1:1b</div>
</div> */}
    </div>
  );
}