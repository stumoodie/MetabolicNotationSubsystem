package uk.ac.ed.inf.Metabolic.sbmlexport;

import org.pathwayeditor.businessobjectsAPI.IMapObject;

public interface OTFilter {

	boolean accept (IMapObject imo);
}
