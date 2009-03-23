package org.pathwayeditor.metabolic.parser;

import org.pathwayeditor.businessobjects.drawingprimitives.IDrawingNode;
import org.pathwayeditor.businessobjects.drawingprimitives.ILinkAttribute;
import org.pathwayeditor.businessobjects.drawingprimitives.ILinkEdge;
import org.pathwayeditor.businessobjects.drawingprimitives.IShapeAttribute;
import org.pathwayeditor.businessobjects.drawingprimitives.properties.IAnnotatedObject;
import org.pathwayeditor.notationsubsystem.toolkit.ndom.INdomModel;

/**
 * Basic Abstract class for Notation Domain Object Model. Defines ID, name,
 * description and detailed description of them object.
 * 
 * $Id: ModelObject.java 4642 2009-02-03 11:50:42Z smoodie $
 * 
 * @author Anatoly Sorokin
 * @date 30 May 2008
 * 
 */
public abstract class ModelObject implements INdomModel {

	public static String getStringProperty(String propName, IDrawingNode o) {
		String name = ((IAnnotatedObject) o.getAttribute()).getProperty(
				propName).getValue().toString();
		return name;
	}

	private String description = "";
	private String detailedDescription = "";
	private final String id;
	private final String name;
	private final String aSCIIName;

	/**
	 * Create object from ID, Name and ASCII name.
	 * 
	 * @param id
	 *            object ID ("(_|[a-z]|[A-Z])(_|[a-z]|[A-Z]|[0-9])*")
	 * @param name
	 *            non-null name string, could contains any markup
	 * @param asciiName
	 *            non-null simplified name string
	 * @throws IllegalArgumentException
	 *             if preconditions are not hold
	 */
	public ModelObject(String id, String name, String asciiName)
			throws IllegalArgumentException {
		if (!id.matches("(_|[a-z]|[A-Z])(_|[a-z]|[A-Z]|[0-9])*"))
			throw new IllegalArgumentException("Invalid ID: " + id);
		if (name == null)
			throw new IllegalArgumentException("Name should not be null");
		if (asciiName == null)
			throw new IllegalArgumentException("ASCIIName should not be null");
		this.id = id;
		this.name = name;
		// TODO add validation for ASCIIname as well
		aSCIIName = asciiName;
	}

	/**
	 * Create object from id and visual node element.
	 * 
	 * @see #ModelObject(String, String, String)
	 * @param id
	 *            object ID ("(_|[a-z]|[A-Z])(_|[a-z]|[A-Z]|[0-9])*")
	 * @param mapObject
	 *            non-null business object
	 * @throws IllegalArgumentException
	 *             if preconditions are not hold
	 */
	public ModelObject(String id, IDrawingNode mapObject)
			throws IllegalArgumentException {
		this(id, ((IShapeAttribute) mapObject.getAttribute()).getName(),
				AbstractNDOMParser.getASCIIName(mapObject));
		IShapeAttribute isa = (IShapeAttribute) mapObject.getAttribute();
		setDescription(isa.getDescription());
		setDetailedDescription(isa.getDetailedDescription());
	}

	public ModelObject(String id, ILinkEdge mapObject)
			throws IllegalArgumentException {
		this(id, ((ILinkAttribute) mapObject.getAttribute()).getName(),
				AbstractNDOMParser.getASCIIName(mapObject));
		ILinkAttribute isa = (ILinkAttribute) mapObject.getAttribute();
		setDescription(isa.getDescription());
		setDetailedDescription(isa.getDetailedDescription());
	}

	public String getDescription() {
		return description;
	}

	public String getDetailedDescription() {
		return detailedDescription;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getASCIIName() {
		return aSCIIName;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setDetailedDescription(String detailedDescription) {
		this.detailedDescription = detailedDescription;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("ModelObject:[");
		sb.append("\nID:").append(getId());
		sb.append("\nname:").append(getASCIIName());
		sb.append("\nDescription:").append(getDescription());
		sb.append("\nDetailedDescription:").append(getDetailedDescription());
		sb.append("\n]\n");
		return sb.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj instanceof ModelObject) {
			ModelObject mo = (ModelObject) obj;
			return id.equals(mo.id);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

}

/*
 * $Log: ModelObject.java,v $ Revision 1.1 2008/07/10 12:06:38 nhanlon
 * extraction of Toolkit project
 * 
 * Revision 1.2 2008/07/07 08:46:50 nhanlon interface changes
 * 
 * Revision 1.1 2008/06/02 10:25:43 asorokin NDOM facility
 */