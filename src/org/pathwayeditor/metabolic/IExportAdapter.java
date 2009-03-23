package org.pathwayeditor.metabolic;

import java.io.IOException;
import java.io.OutputStream;

/**
 * An interface that defines the behaviour of the export adapter used by a validationService adapter's
 * export service. Typical implementations of this class will implement a mapping from the NDOM
 * to an intermediate representation of the export format that can then be written to an output stream.
 * Future development of the API may include output by some other mechanism than a stream, hence
 * the separation of creation and writing.    
 * @author smoodie
 * @param N root class of the Notation Domain Object Model for the validationService adapter.
 */
public interface IExportAdapter<N> {

	/**
	 * Creates an intermediate representation of the export format ready to be 
	 * written out in some form.
	 * @param model the NDOM model, which cannot be null.
	 * @throws ExportAdapterCreationException if there are any exceptions 
	 * 
	 * @throw IllegalArgumentException if <code>model == null</code>.
	 */
	void createTarget(N model) throws ExportAdapterCreationException;
	
	/**
	 * Tests if a target has been created.
	 * @return true if create target was successful, false otherwise.
	 */
	boolean isTargetCreated();
	
	/**
	 * Writes the export format to an output stream provided by the client.
	 * @param oStream The output stream to write to. Cannot be null.
	 * @throws IOException
	 * @throws IllegalArgumentException if <code>oStream == null</code>.
	 * @throws IllegalStateException if <code>isTargetCreated() == false</code>.
	 * @throws IOException if there is any underlying i/o error while writing the export format.
	 */
	void writeTarget(OutputStream oStream) throws IOException;
	
}
