/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.analyticsmodeling.model.response;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

import java.util.TreeMap;

import org.odpi.openmetadata.frameworks.auditlog.MessageFormatter;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Generic error structure.
 * <code>
 * <br>{
 * <br>&emsp;   "code": error code,
 * <br>&emsp;   "detail": explanation,
 * <br>&emsp;   "meta": {
 * <br>&emsp;&emsp;	"severity": "error" || "info" || "warning"
 * <br>&emsp;   },
 * <br>&emsp;   "status": HTTP status code,
 * <br>&emsp;   "title": short description.
 * <br> }
 * </code>
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AnalyticsMessage {

    private static final String ERROR = "error";
	private static final String SEVERITY = "severity";

	private static final MessageFormatter messageFormatter = new MessageFormatter();
	
	String status;
	String code;
	String title;
	String detail;

	TreeMap<String, String> meta;
	
	public AnalyticsMessage() {
		setSeverity(ERROR);
	}

	private AnalyticsMessage(String severity, ExceptionMessageDefinition message) {
		this.status = Integer.toString(message.getHttpErrorCode());
		this.code = message.getMessageId();
		this.title = messageFormatter.getFormattedMessage(message);
		this.detail = message.getSystemAction() + " " + message.getUserAction();
		setSeverity(severity);
	}
	
	public static AnalyticsMessage createError(String status, String code, String title, String detail) {
		AnalyticsMessage ret = new AnalyticsMessage();
		ret.status = status;
		ret.code = code;
		ret.title = title;
		ret.detail = detail;
		ret.setSeverity(ERROR);
		return ret;
	}
	
	public static AnalyticsMessage createError(ExceptionMessageDefinition message) {
		return new AnalyticsMessage(ERROR, message);
	}
	
	public static AnalyticsMessage createWarning(ExceptionMessageDefinition message) {
		return new AnalyticsMessage("warning", message);
	}
	
	public static AnalyticsMessage createInfo(ExceptionMessageDefinition message) {
		return new AnalyticsMessage("info", message);
	}
	
	/**
	 * Get error status.
	 * @return error status.
	 */
	public String getStatus() {
		return status;
	}
	
	/**
	 * Set error status.
	 * @param value of error status.
	 */
	public void setStatus(String value) {
		status = value;
	}

	/**
	 * Get error code.
	 * @return error code.
	 */
	public String getCode() {
		return code;
	}
	
	/**
	 * Set error code.
	 * @param value of error code.
	 */
	public void setCode(String value) {
		code = value;
	}
	
	/**
	 * Get error title.
	 * @return error title.
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * Set error title.
	 * @param value to set.
	 */

	public void setTitle(String value) {
		title = value;
	}
	
	/**
	 * Get error detail.
	 * @return  error detail.
	 */
	public String getDetail() {
		return detail;
	}
	
	/**
	 * Set  error detail.
	 * @param value of error detail.
	 */
	public void setDetail(String value) {
		 detail = value;
	}
	
	/**
	 * Get error meta.
	 * @return  error meta.
	 */
	public TreeMap<String, String> getMeta() {
		return meta;
	}
	
	/**
	 * Set  error meta.
	 * @param value of error detail.
	 */
	public void setMeta(TreeMap<String, String> value) {
		meta = value;
	}

	public void setSeverity(String value) {
		if (meta == null) {
			meta = new TreeMap<>();
		}
		meta.put(SEVERITY, value);
		
	}
}
