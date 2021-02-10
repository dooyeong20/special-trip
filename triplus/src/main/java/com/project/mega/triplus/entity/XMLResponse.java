package com.project.mega.triplus.entity;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "response")
@XmlAccessorType(XmlAccessType.PROPERTY)
@Getter @Setter
public class XMLResponse{

    private XMLResponseHeader header;
    private XMLResponseBody body;

}
