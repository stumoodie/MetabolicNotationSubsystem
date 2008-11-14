package uk.ac.ed.inf.Metabolic;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.pathwayeditor.businessobjects.drawingprimitives.attributes.LineStyle;
import org.pathwayeditor.businessobjects.drawingprimitives.attributes.PrimitiveShapeType;
import org.pathwayeditor.businessobjects.drawingprimitives.attributes.RGB;
import org.pathwayeditor.businessobjects.drawingprimitives.attributes.Size;
import org.pathwayeditor.businessobjects.drawingprimitives.properties.IPropertyDefinition;
import org.pathwayeditor.businessobjects.typedefn.IShapeAttributeDefaults;

public class MetabolicFallbackShapeAttributeDefaults implements IShapeAttributeDefaults{
	private PrimitiveShapeType shapeType = PrimitiveShapeType.ROUNDED_RECTANGLE;
	private LineStyle lineStyle = LineStyle.SOLID;
	private Size size = new Size(20,20);
	private RGB fillColour = new RGB(255,255,255);
	private RGB lineColour = new RGB(0,0,0);
	private String detailedDescription= "";
	private int lineWidth= 1;
	private String url="";
	private Set<IPropertyDefinition> propertyDefinitions = new HashSet();
	private String description="A shape type";
	
	public String getDescription() {
		return description;
	}
	public String getDetailedDescription() {
		return detailedDescription;
	}
	public RGB getFillColour() {
		return fillColour ;
	}
	public RGB getLineColour() {
		return lineColour;
	}
	public LineStyle getLineStyle() {
		return lineStyle ;
	}
	public int getLineWidth() {
		return lineWidth;
	}
	public String getName() {
		return "compartment";
	}
	public PrimitiveShapeType getShapeType() {
		return shapeType ;
	}
	public Size getSize() {
		return size ;
	}
	public String getURL() {
		return url;
	}
	public Iterator<IPropertyDefinition> propertyDefinitionIterator() {
		return propertyDefinitions .iterator();
	}
	public void setShapeType(PrimitiveShapeType shapeType) {
		this.shapeType = shapeType;
	}
	public void setLineStyle(LineStyle lineStyle) {
		this.lineStyle = lineStyle;
	}
	public void setSize(Size size) {
		this.size = size;
	}
	public void setFillColour(RGB fillColour) {
		this.fillColour = fillColour;
	}
	public void setLineColour(RGB lineColour) {
		this.lineColour = lineColour;
	}
	public void setDetailedDescription(String detailedDescription) {
		this.detailedDescription = detailedDescription;
	}
	public void setLineWidth(int lineWidth) {
		this.lineWidth = lineWidth;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public void setPropertyDefinitions(Set<IPropertyDefinition> propertyDefinitions) {
		this.propertyDefinitions = propertyDefinitions;
	}
	public void setDescription(String description) {
		this.description = description;
	}

}
