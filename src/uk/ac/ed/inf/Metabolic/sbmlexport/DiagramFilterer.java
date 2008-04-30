package uk.ac.ed.inf.Metabolic.sbmlexport;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.pathwayeditor.businessobjectsAPI.ILink;
import org.pathwayeditor.businessobjectsAPI.IMapObject;
import org.pathwayeditor.businessobjectsAPI.IRootMapObject;
import org.pathwayeditor.businessobjectsAPI.IShape;
import org.pathwayeditor.contextadapter.publicapi.ILinkObjectType;

public class DiagramFilterer {

	Set<IShape> getRequiredShapes(IRootMapObject rmo, OTFilter filter) {
		Set<IShape>rc = new HashSet<IShape>();
		for (IMapObject s : rmo.getChildren()) {
			if(filter.accept(s)){
				rc.add((IShape)s);
			}
			recurseThroughChildren(rc, (IShape)s, filter);
		}
		return rc;
		
		
	}

	private void recurseThroughChildren(Set<IShape> rc, IShape s, OTFilter filter) {
		for (IMapObject child : s.getChildren()) {
			if(filter.accept(s)){
				rc.add(s);
			}
			recurseThroughChildren(rc, (IShape)child, filter);
		}
		
	}
    /**
     * Filters for src and target links
     * @param linkedShape
     * @param filter
     * @return
     */
	public Set<ILink> getLinks(IShape linkedShape, OTFilter filter) {
		Set<ILink>rc = new HashSet<ILink>();
		for (ILink l : linkedShape.getSourceLinks()){
			if(filter.accept(l)){
				rc.add(l);
			}
		}
		for (ILink l : linkedShape.getTargetLinks()){
			if(filter.accept(l)){
				rc.add(l);
			}
		}
		return rc;
	}
}
