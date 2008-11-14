package uk.ac.ed.inf.Metabolic;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.pathwayeditor.businessobjects.drawingprimitives.attributes.ConnectionRouter;
import org.pathwayeditor.businessobjects.drawingprimitives.attributes.LineStyle;
import org.pathwayeditor.businessobjects.drawingprimitives.attributes.RGB;
import org.pathwayeditor.businessobjects.drawingprimitives.properties.IPropertyDefinition;
import org.pathwayeditor.businessobjects.typedefn.ILinkAttributeDefaults;

public class MetabolicFallbackLinkAttributeDefaults implements ILinkAttributeDefaults{

	private String description;
	private String detailedDescription;
	private RGB lineColour;
	private LineStyle lineStyle;
	private int lineWidth;
	private String name;
	private ConnectionRouter router;
	private String url;
	private Set <IPropertyDefinition> propertyDefinitions = new HashSet<IPropertyDefinition>();

	public String getDescription() {
		return description;
	}

	public String getDetailedDescription() {
		return detailedDescription;
	}

	public RGB getLineColour() {
		return lineColour;
	}

	public LineStyle getLineStyle() {
		return lineStyle;
	}

	public int getLineWidth() {
		return lineWidth;
	}

	public String getName() {
		return name;
	}

	public ConnectionRouter getRouter() {
		return router;
	}

	public String getUrl() {
		return url;
	}

	public Iterator<IPropertyDefinition> propertyDefinitionIterator() {
		return propertyDefinitions .iterator();
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setDetailedDescription(String detailedDescription) {
		this.detailedDescription = detailedDescription;
	}

	public void setLineColour(RGB lineColour) {
		this.lineColour = lineColour;
	}

	public void setLineStyle(LineStyle lineStyle) {
		this.lineStyle = lineStyle;
	}

	public void setLineWidth(int lineWidth) {
		this.lineWidth = lineWidth;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setRouter(ConnectionRouter router) {
		this.router = router;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setPropertyDefinitions(Set<IPropertyDefinition> propertyDefinitions) {
		this.propertyDefinitions = propertyDefinitions;
	}

}
