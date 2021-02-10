package com.project.mega.triplus.entity;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "item")
@XmlAccessorType(XmlAccessType.PROPERTY)
@Getter @Setter
public class XMLResponseItem {
    private String code;
    private String name;
    private String rnum;
}
