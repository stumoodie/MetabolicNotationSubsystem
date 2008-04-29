package uk.ed.ac.inf.Metabolic.sbmlexport;

import org.sbml.libsbml.Model;
import org.sbml.libsbml.SBMLDocument;

public class TestSBML {
    static {System.loadLibrary("sbmlj");}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SBMLDocument d = new SBMLDocument();
		Model m  = d.createModel();
		System.out.println("OK");

	}

}
