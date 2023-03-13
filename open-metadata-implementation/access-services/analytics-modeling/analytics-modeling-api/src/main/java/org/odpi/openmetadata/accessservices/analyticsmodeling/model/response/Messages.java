/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */


package org.odpi.openmetadata.accessservices.analyticsmodeling.model.response;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

import java.util.ArrayList;
import java.util.List;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Container of the messages.
 * It contain non critical errors, warning and info messages
 * while successfully executing the request.
 * 
 * <code>
 * <br>{<br>
 * &emsp;	"messages" : [&nbsp;
 * &emsp;			{@link org.odpi.openmetadata.accessservices.analyticsmodeling.model.response.AnalyticsMessage}<br>
 * &emsp;	&nbsp;]<br>
 * }<br>
 * </code>
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Messages {
	
	private List<AnalyticsMessage> messages;

	public List<AnalyticsMessage> getMessages() {
		return messages;
	}

	public void setMessages(List<AnalyticsMessage> messages) {
		this.messages = messages;
	}

	public void addWarning(ExceptionMessageDefinition message) {
		if (messages == null) {
			messages = new ArrayList<>();
		}
		messages.add(AnalyticsMessage.createWarning(message));
	}
	
	public void addWarning(ExceptionMessageDefinition  message, String detail) {
		if (messages == null) {
			messages = new ArrayList<>();
		}
		AnalyticsMessage error = AnalyticsMessage.createWarning(message);
		error.setDetail(detail);
		messages.add(error);
	}

	public void addInfo(ExceptionMessageDefinition message) {
		if (messages == null) {
			messages = new ArrayList<>();
		}
		messages.add(AnalyticsMessage.createInfo(message));
	}

	public void addError(ExceptionMessageDefinition message) {
		if (messages == null) {
			messages = new ArrayList<>();
		}
		messages.add(AnalyticsMessage.createError(message));
	}

}
