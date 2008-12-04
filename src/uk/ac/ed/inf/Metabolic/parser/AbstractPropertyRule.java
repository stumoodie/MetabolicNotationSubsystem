package uk.ac.ed.inf.Metabolic.parser;

import org.pathwayeditor.businessobjects.drawingprimitives.IDrawingElement;
import org.pathwayeditor.businessobjects.drawingprimitives.properties.IAnnotatedObject;
import org.pathwayeditor.businessobjects.notationsubsystem.IValidationRuleDefinition;

public abstract class AbstractPropertyRule implements IParserRule {

	protected IValidationRuleDefinition ruleDef;
	protected IAnnotatedObject imo;
	protected IDrawingElement ref;

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

	public void setRuleDef(IValidationRuleDefinition ruleDef) {
		this.ruleDef = ruleDef;
	}

	public void setObject(IAnnotatedObject imo) {
		this.imo = imo;
	}

	public void setRefObject(IDrawingElement r) {
		ref=r;
		imo=(IAnnotatedObject) ref.getAttribute();
	}

	public IDrawingElement getRefObject() {
		return ref;
	}

	protected void checkState() {
		if(ruleDef==null) throw new NullPointerException("Rule definition is not set");
		if(imo==null) throw new NullPointerException("IAnnotatedObject is not set");
	}

}
