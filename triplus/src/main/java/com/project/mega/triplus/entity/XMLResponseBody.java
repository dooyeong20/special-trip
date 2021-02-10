package com.project.mega.triplus.entity;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "body")
@XmlAccessorType(XmlAccessType.PROPERTY)
@Getter @Setter
public class XMLResponseBody {
    private List<XMLResponseItem> items;
    private String numOfRows;
    private String pageNo;
    private String totalCount;
}
