package uk.ac.ed.inf.Metabolic.parser;

import org.pathwayeditor.businessobjects.drawingprimitives.IDrawingElement;
import org.pathwayeditor.businessobjects.drawingprimitives.properties.IAnnotatedObject;
import org.pathwayeditor.businessobjects.drawingprimitives.properties.IAnnotationProperty;
import org.pathwayeditor.businessobjects.notationsubsystem.IValidationRuleDefinition;

public abstract class AbstractPropertyRule implements IParserRule {
	private final IValidationRuleDefinition ruleDef;
	private final String propertyName;
	private IAnnotatedObject imo;
	private IDrawingElement ref;

	protected AbstractPropertyRule(IValidationRuleDefinition ruleDefn, String propertyName){
		this.ruleDef = ruleDefn;
		this.propertyName = propertyName;
	}
	
	public String getPropertyName(){
		return this.propertyName;
	}
	
	/**
	 * @return rule definition object
	 */
	public IValidationRuleDefinition getRuleDef() {
		return ruleDef;
	}

	/**
	 * @return object to be tested
	 */
	public IAnnotatedObject getObject() {
		return imo;
	}

	public void setObject(IAnnotatedObject imo) {
		this.imo = imo;
	}

	protected IAnnotationProperty getCurrentProperty(){
		return this.imo.getProperty(this.propertyName);
	}
	
	public void setRefObject(IDrawingElement r) {
		ref=r;
		imo=(IAnnotatedObject) ref.getAttribute();
	}

	public IDrawingElement getRefObject() {
		return ref;
	}

	protected void checkState() {
		if(imo==null) throw new IllegalStateException("IAnnotatedObject is not set");
	}

}
