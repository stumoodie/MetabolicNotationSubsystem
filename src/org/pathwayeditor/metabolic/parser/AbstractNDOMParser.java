package org.pathwayeditor.metabolic.parser;

import java.util.ArrayList;
import java.util.List;

import org.pathwayeditor.businessobjects.drawingprimitives.IDrawingElement;
import org.pathwayeditor.businessobjects.drawingprimitives.IDrawingNode;
import org.pathwayeditor.businessobjects.drawingprimitives.ILinkAttribute;
import org.pathwayeditor.businessobjects.drawingprimitives.ILinkEdge;
import org.pathwayeditor.businessobjects.drawingprimitives.IRootNode;
import org.pathwayeditor.businessobjects.drawingprimitives.IShapeAttribute;
import org.pathwayeditor.notationsubsystem.toolkit.ndom.NdomException;

public abstract class AbstractNDOMParser {

//	private static final String PROP_NAME = "Name";
	private IRootNode rmo;
	private boolean parsed = false;
	private boolean hasWarnings = false;
	private boolean valid = false;
	private List<String> report = new ArrayList<String>();
	private int counter = 0;
	

	/**
	 * Generate ID for ndom description of map element. ID is based upon name of objects <code>ObjectType</code> and serial counter.
	 * @param o non-null map object. Object type should be non-null value
	 * @return
	 */
	protected String getId(IDrawingElement o) {
		StringBuffer id = new StringBuffer();
		id.append(o.getAttribute().getObjectType().getName().replaceAll("[^_a-zA-Z0-9]",
				""));
		id.append(counter++);
		return id.toString();
	}
	

	/**
	 * Generate valid ASCII string from node name.
	 * @param o
	 * @return
	 */
	public static String getASCIIName(IDrawingNode o) {
		//FIXME add code for conversion to ASCII name
		String name = ((IShapeAttribute) o.getAttribute()).getName();
		return name;
	}

	public static String getASCIIName(ILinkEdge e) {
		//FIXME add code for conversion to ASCII name
		String name = ((ILinkAttribute) e.getAttribute()).getName();
		return name;
	}

	public AbstractNDOMParser() {

	}

	public AbstractNDOMParser(IRootNode rmo) {
		this.rmo = rmo;
	}

	/**
	 * Parsing diagram. The main method for parser. It sets object counter to
	 * <code>0</code>. Makes report empty, and set <code>parsed</code>
	 * value to trueat the end.<br>
	 * Contract:<br>
	 * Preconditions: <code>RootMapObject</code> is set and is not null.<br>
	 * Postconditions: <br>
	 * <ul>
	 * <li><code>parsed</code> is <code>TRUE</code>;</li>
	 * <li> if {@link #hasWarnings()} returns <code>FALSE</code> and
	 * {@link #isValid()} returns <code>TRUE</code>, then report is empty
	 * </li>
	 * <li> if {@link #hasWarnings()} returns <code>TRUE</code> and
	 * {@link #isValid()} returns <code>TRUE</code>, then report is not empty
	 * but contains only warnings</li>
	 * <li> if {@link #isValid()} returns <code>FALSE</code> , then report is
	 * not empty and contains errors </li>
	 * <li> if {@link NdomException} is thrown, then {@link #isValid()} returns
	 * <code>FALSE</code></li>
	 * </ul>
	 * 
	 * @throws NdomException
	 *             if parser is not able to recover from parsing error.
	 */
	public void parse() throws NdomException {
		if(rmo==null) throw new NdomException("No map specified for parsing");
		counter = 0;
		ndom();
		report = new ArrayList<String>();
		valid = true;
		hasWarnings = false;
		// Composition
		rmo();
		// Connectivity
		connectivity();
		// Semantic Validation
		semanticValidation();
		parsed = true;
	}

	/**
	 * Semantic validation of domain object model. The last method to invoke in
	 * {@link #parse()}.<br>
	 * Contract:<br>
	 * Preconditions: <code>RootMapObject</code> is set and is not null.<br>
	 * Postconditions: the same as {@link #parse()}.
	 */
	protected abstract void semanticValidation();

	/**
	 * Prepare notation domain object model for parsing of the diagram. This
	 * method is invokes first in parsing procedure.<br>
	 * Contract:<br>
	 * Preconditions: <code>RootMapObject</code> is set and is not null.<br>
	 * Postconditions: the same as {@link #parse()}.
	 */
	protected abstract void ndom();

	/**
	 * Analyse connectivity of the diagram. That method is intended to deal with
	 * links, their properties and graph structure analysis.<br>
	 * Contract:<br>
	 * Preconditions: <code>RootMapObject</code> is set and is not null.<br>
	 * Postconditions: the same as {@link #parse()}.
	 */
	protected abstract void connectivity();

	/**
	 * Parsing RootMapObject. Initial point for any NDOM parsing procedure. This
	 * method is responsible for analysing shape hierarchy and its
	 * representation in NDOM.<br>
	 * Contract:<br>
	 * Preconditions: <code>RootMapObject</code> is set and is not null.<br>
	 * Postconditions: the same as {@link #parse()}.
	 */
	protected abstract void rmo();

	/**
	 * Write error to the report.<br>
	 * Postconditions:<br>
	 * <ul>
	 * <li>{@link #isValid()} returns <code>FALSE</code></li>
	 * <li>report is not empty</li>
	 * </ul>
	 * 
	 * @param message
	 */
	protected void error(String message) {
		report.add("ERROR\t" + message);
		valid = false;
	}

	/**
	 * Write warnings to the report.<br>
	 * Postconditions:<br>
	 * <ul>
	 * <li>{@link #hasWarnings()()} returns <code>TRUE</code></li>
	 * <li>report is not empty</li>
	 * </ul>
	 * 
	 * @param message
	 */
	protected void warning(String message) {
		report.add("WARNING\t" + message);
		hasWarnings = true;
	}

	/**
	 * @return true if there was attempt to parse RootMapObject, false
	 *         otherwise.
	 */
	public boolean isParsed() {
		return parsed;
	}

	/**
	 * Assign new RootMapObject to be parsed. Sets {@link #isParsed()} to
	 * <code>FALSE</code>, and makes report empty.
	 * 
	 * @param rmo
	 *            RootMapObject to be parsed
	 */
	public void setRmo(IRootNode rmo) {
		this.rmo = rmo;
		parsed = false;
		report = new ArrayList<String>();
	}

	public IRootNode getRmo() {
		return rmo;
	}

	public boolean isValid() {
		return valid;
	}

	public boolean hasWarnings() {
		return hasWarnings;
	}

	public List<String> getReport() {
		return report;
	}

	/**
	 * Convert string value to integer. In the case of improperly formatted string returns writes an error with the message specified in second argument.
	 * @param st string to parse.
	 * @param message to describe the source of value in error message.
	 * @return result of string parsing or 1, if string is not valid integer.
	 */
	protected int getInt(String st, String message) {
		int stoich = 1;
		try {
			if (st != null && st.trim().length() == 0) {
				stoich = Integer.parseInt(st);
			}
		} catch (NumberFormatException nfe) {
			error(message + st);
		}
		return stoich;
	}

	/**
	 * @param st string to parse.
	 * @param message to describe the source of value in error message.
	 * @return result of string parsing or 0.0, if string is not valid double.
	 */
	protected double getDouble(String st, String message) {
		double parseDouble = 0.0;
		try {
			if (st != null && st.trim().length() == 0) {
				parseDouble = Double.parseDouble(st);
			}
		} catch (NumberFormatException nfe) {
			error(message + st);
		}
		return parseDouble;
	}

	protected void report(NdomException e) {
		String message = " Parsing Error\t" + e.getMessage();
		error(message);
	}


}

/*
 * $Log: AbstractNDOMParser.java,v $
 * Revision 1.2  2008/07/12 15:50:49  smoodie
 * Removed dependency on IRuleValidationReportBuilder
 *
 * Revision 1.1  2008/07/10 12:06:38  nhanlon
 * extraction of Toolkit project
 *
 * Revision 1.9  2008/07/07 08:46:50  nhanlon
 * interface changes
 *
 * Revision 1.8  2008/06/26 13:08:56  radams
 * check args of report builder
 *
 * Revision 1.7  2008/06/24 09:49:58  radams
 * moved report builder int o abstract ndom
 *
 * Revision 1.6  2008/06/23 14:42:01  radams
 * set report builderdirectly in abstract validation class
 *
 * Revision 1.5  2008/06/13 13:34:01  radams
 * revert to previous
 *
 * Revision 1.3  2008/06/06 11:50:28  asorokin
 * *** empty log message ***
 *
 * Revision 1.2  2008/06/03 08:52:03  asorokin
 * Size-based calculation of direction of first or last link segment
 *
 * Revision 1.1  2008/06/02 10:25:43  asorokin
 * NDOM facility
 *
 */