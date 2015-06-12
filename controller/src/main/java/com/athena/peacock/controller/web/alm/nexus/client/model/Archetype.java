//
// 이 파일은 JAXB(JavaTM Architecture for XML Binding) 참조 구현 2.2.7 버전을 통해 생성되었습니다. 
// <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>를 참조하십시오. 
// 이 파일을 수정하면 소스 스키마를 재컴파일할 때 수정 사항이 손실됩니다. 
// 생성 날짜: 2014.09.29 시간 10:31:00 AM KST 
//

package com.athena.peacock.controller.web.alm.nexus.client.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import com.sun.xml.txw2.annotation.XmlElement;

/**
 * Informations to point to an Archetype referenced in the catalog.
 * 
 * <p>
 * 
 * 
 * <pre>
 * &lt;complexType name="Archetype">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="groupId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="artifactId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="version" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="repository" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Archetype", propOrder = {

})
public class Archetype {

	protected String groupId;
	protected String artifactId;
	protected String version;
	protected String repository;
	protected String description;

	/**
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	@XmlElement(value = "groupId")
	public String getGroupId() {
		return groupId;
	}

	/**
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setGroupId(String value) {
		this.groupId = value;
	}

	/**
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getArtifactId() {
		return artifactId;
	}

	/**
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setArtifactId(String value) {
		this.artifactId = value;
	}

	/**
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setVersion(String value) {
		this.version = value;
	}

	/**
	 * @return possible object is {@link String }
	 * 
	 */
	public String getRepository() {
		return repository;
	}

	/**
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setRepository(String value) {
		this.repository = value;
	}

	/**
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setDescription(String value) {
		this.description = value;
	}

}
