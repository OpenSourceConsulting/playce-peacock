//
// �� ������ JAXB(JavaTM Architecture for XML Binding) ���� ���� 2.2.7 ������ ���� ��Ǿ���ϴ�. 
// <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>�� �����Ͻʽÿ�. 
// �� ������ �����ϸ� �ҽ� ��Ű���� ���������� �� ���� ������ �սǵ˴ϴ�. 
// �� ��¥: 2014.09.29 �ð� 10:31:00 AM KST 
//

package com.athena.peacock.controller.web.alm.nexus.client.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.sun.xml.txw2.annotation.XmlElement;

/**
 * 0.0.0+
 * 
 * <p>
 * ArchetypeCatalog complex type�� ���� Java Ŭ�����Դϴ�.
 * 
 * <p>
 * 
 * <pre>
 * &lt;complexType name="ArchetypeCatalog">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="archetypes" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="archetype" type="{http://maven.apache.org/plugins/maven-archetype-plugin/archetype-catalog/1.0.0}Archetype" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArchetypeCatalog", propOrder = {

})
@XmlRootElement(name = "archetype-catalog", namespace = "http://maven.apache.org/plugins/maven-archetype-plugin/archetype-catalog/1.0.0")
public class ArchetypeCatalog {

	protected ArchetypeCatalog.Archetypes archetypes;

	/**
	 * archetypes �Ӽ��� ���� �����ɴϴ�.
	 * 
	 * @return possible object is {@link ArchetypeCatalog.Archetypes }
	 * 
	 */
	public ArchetypeCatalog.Archetypes getArchetypes() {
		return archetypes;
	}

	/**
	 * archetypes �Ӽ��� ���� �����մϴ�.
	 * 
	 * @param value
	 *            allowed object is {@link ArchetypeCatalog.Archetypes }
	 * 
	 */
	public void setArchetypes(ArchetypeCatalog.Archetypes value) {
		this.archetypes = value;
	}

	/**
	 * <p>
	 * anonymous complex type�� ���� Java Ŭ�����Դϴ�.
	 * 
	 * <p>
	 * ���� ��Ű�� ������ �� Ŭ������ ���ԵǴ� �ʿ��� �������� �����մϴ�.
	 * 
	 * <pre>
	 * &lt;complexType>
	 *   &lt;complexContent>
	 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
	 *       &lt;sequence>
	 *         &lt;element name="archetype" type="{http://maven.apache.org/plugins/maven-archetype-plugin/archetype-catalog/1.0.0}Archetype" maxOccurs="unbounded" minOccurs="0"/>
	 *       &lt;/sequence>
	 *     &lt;/restriction>
	 *   &lt;/complexContent>
	 * &lt;/complexType>
	 * </pre>
	 * 
	 * 
	 */
	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "", propOrder = { "archetype" })
	public static class Archetypes {

		protected List<Archetype> archetype;

		/**
		 * Gets the value of the archetype property.
		 * 
		 * <p>
		 * This accessor method returns a reference to the live list, not a
		 * snapshot. Therefore any modification you make to the returned list
		 * will be present inside the JAXB object. This is why there is not a
		 * <CODE>set</CODE> method for the archetype property.
		 * 
		 * <p>
		 * For example, to add a new item, do as follows:
		 * 
		 * <pre>
		 * getArchetype().add(newItem);
		 * </pre>
		 * 
		 * 
		 * <p>
		 * Objects of the following type(s) are allowed in the list
		 * {@link Archetype }
		 * 
		 * 
		 */
		public List<Archetype> getArchetype() {
			if (archetype == null) {
				archetype = new ArrayList<Archetype>();
			}
			return this.archetype;
		}

	}

}
