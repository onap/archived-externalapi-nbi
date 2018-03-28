package org.onap.nbi.exceptions;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Error", propOrder = {"code", "message", "description", "infoURL"})
public class ApiError {
    @XmlElement(required = true)
    protected String code;
    @XmlElement(required = true)
    protected String message;
    @XmlElement(required = true)
    private String description;
    @XmlElement(required = true)
    protected String infoURL;

    public ApiError() {}

    public ApiError(String code, String message, String description, String infoURL) {
        this.code = code;
        this.message = message;
        this.description = description;
        this.infoURL = infoURL;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInfoURL() {
        return infoURL;
    }

    public void setInfoURL(String infoURL) {
        this.infoURL = infoURL;
    }
}

