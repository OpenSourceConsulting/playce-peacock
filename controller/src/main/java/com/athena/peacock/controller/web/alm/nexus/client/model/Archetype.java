//
// �� ������ JAXB(JavaTM Architecture for XML Binding) ���� ���� 2.2.7 ������ ���� ��Ǿ���ϴ�. 
// <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>�� �����Ͻʽÿ�. 
// �� ������ �����ϸ� �ҽ� ��Ű���� ���������� �� ���� ������ �սǵ˴ϴ�. 
// �� ��¥: 2014.09.29 �ð� 10:31:00 AM KST 
//

package com.athena.peacock.controller.web.alm.nexus.client.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.sun.xml.txw2.annotation.XmlElement;

/**
 * Informations to point to an Archetype referenced in the catalog.
 * 
 * <p>
 * Archetype complex type�� ���� Java Ŭ�����Դϴ�.
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
	 * groupId �Ӽ��� ���� �����ɴϴ�.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	@XmlElement(value = "groupId")
	public String getGroupId() {
		return groupId;
	}

	/**
	 * groupId �Ӽ��� ���� �����մϴ�.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setGroupId(String value) {
		this.groupId = value;
	}

	/**
	 * artifactId �Ӽ��� ���� �����ɴϴ�.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getArtifactId() {
		return artifactId;
	}

	/**
	 * artifactId �Ӽ��� ���� �����մϴ�.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setArtifactId(String value) {
		this.artifactId = value;
	}

	/**
	 * version �Ӽ��� ���� �����ɴϴ�.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * version �Ӽ��� ���� �����մϴ�.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setVersion(String value) {
		this.version = value;
	}

	/**
	 * repository �Ӽ��� ���� �����ɴϴ�.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getRepository() {
		return repository;
	}

	/**
	 * repository �Ӽ��� ���� �����մϴ�.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setRepository(String value) {
		this.repository = value;
	}

	/**
	 * description �Ӽ��� ���� �����ɴϴ�.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * description �Ӽ��� ���� �����մϴ�.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setDescription(String value) {
		this.description = value;
	}

}
