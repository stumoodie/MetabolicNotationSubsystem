package uk.ac.ed.inf.Metabolic;


import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.pathwayeditor.businessobjects.Link;
import org.pathwayeditor.businessobjects.Map;
import org.pathwayeditor.businessobjects.Port;
import org.pathwayeditor.businessobjects.Wrappable;
import org.pathwayeditor.businessobjects.constants.PortType;
import org.pathwayeditor.businessobjectsAPI.ILink;
import org.pathwayeditor.businessobjectsAPI.IMap;
import org.pathwayeditor.businessobjectsAPI.IMapObject;
import org.pathwayeditor.businessobjectsAPI.IPersistenceObjectWrappingProvider;
import org.pathwayeditor.businessobjectsAPI.IRootMapObject;
import org.pathwayeditor.businessobjectsAPI.IShape;
import org.pathwayeditor.contextadapter.GeneralContextAdapter;
import org.pathwayeditor.contextadapter.IMapServices;
import org.pathwayeditor.contextadapter.toolkit.ctxdefn.LinkObjectType;
import org.pathwayeditor.contextadapter.toolkit.ctxdefn.ShapeObjectType;
import org.pathwayeditor.contextadapter.toolkit.ndom.AbstractNDOMParser.NdomException;
import org.pathwayeditor.contextadapterAPI.ILoadMapService;
import org.pathwayeditor.contextadapterAPI.ISaveMapService;
import org.pathwayeditor.contextadapterDefault.DefaultLoadService;
import org.pathwayeditor.contextadapterDefault.DefaultSaveService;
import org.pathwayeditor.hibernate.api.IHibernateMap;
import org.pathwayeditor.hibernate.pojos.HibernateFolder;
import org.pathwayeditor.hibernate.pojos.HibernateLink;
import org.pathwayeditor.hibernate.pojos.HibernateMap;
import org.pathwayeditor.hibernate.pojos.HibernatePort;
import org.pathwayeditor.hibernate.pojos.HibernateRootMapObject;
import org.pathwayeditor.hibernate.pojos.HibernateShape;

import uk.ac.ed.inf.Metabolic.ndomAPI.IModel;
import uk.ac.ed.inf.Metabolic.parser.MetabolicNDOMFactory;
import uk.ac.ed.inf.Metabolic.parser.NDOMFactory;

public class TestParser {

	private IRootMapObject rmo;
	private IMap testMap1;
	private IHibernateMap hibMap;
	MetabolicContextAdapterServiceProvider serv=new MetabolicContextAdapterServiceProvider();
	MetabolicContextAdapterSyntaxService syntServ=serv.getSyntaxService();
	GeneralContextAdapter ica=new GeneralContextAdapter(serv,new IMapServices() {

		public ILoadMapService createLoadService(
				IPersistenceObjectWrappingProvider wrappingProvider) {
			return new DefaultLoadService(wrappingProvider);
		}

		public ISaveMapService createSaveService() {
			return new DefaultSaveService();
		}
		
	});
	
	@Before
	public void setUp() throws Exception {
		testMap1= testMap1();
		
	}

	private IMap testMap1() {
		ShapeObjectType compartment = syntServ.getCompartment();
		IMap testMap = prepMap();
		IRootMapObject rmo = testMap.getTheSingleRootMapObject();
		IMapObject com1=addChild(compartment, rmo);
		IMapObject com2=addChild(compartment, rmo);
		ShapeObjectType reaction = syntServ.getProcess();
		IMapObject re1=addChild(reaction, rmo);
		ShapeObjectType compound = syntServ.getCompound();
		IMapObject h2o=addChild(compound, com1);
		IMapObject co2=addChild(compound, com2);
		LinkObjectType consume=syntServ.getConsume();
		ILink c1=addLink(h2o, re1, consume);
		LinkObjectType prod=syntServ.getProduce();
		ILink c2=addLink(re1, co2,prod);
		return testMap;
	}

	private ILink addLink(IMapObject source,
			IMapObject target, LinkObjectType type) {
		HibernateLink l=new HibernateLink();
		HibernatePort hibS=new HibernatePort();
		hibS.setLink(l);
		HibernatePort hibT=new HibernatePort();
		hibT.setLink(l);
		Port src=new Port(hibS);
		src.setType(PortType.SRC);
		Port tgt=new Port(hibT);
		tgt.setType(PortType.TARGET);
		Wrappable wrappable = new Wrappable(l);
		wrappable.setObjectType(type);
		IMapObject child=ica.wrap(wrappable);
		Link il=(Link) child;
		il.setSrcPort(src);
		il.setTargetPort(tgt);
		il.reconnect((IShape)source, (IShape)target);
		return il;
	}

	private IMapObject addChild(ShapeObjectType type, IMapObject rmo) {
		HibernateShape compartm=new HibernateShape();
		Wrappable wrappable = new Wrappable(compartm);
		wrappable.setObjectType(type);
		IMapObject child=ica.wrap(wrappable);
		rmo.addChild(child);
		return child;
	}

	private IMap prepMap() {
		HibernateMap hibMap= new HibernateMap ();
		hibMap.setName("MAP1");
		hibMap.setContextUUID(serv.getContext().getGlobalId());
		hibMap.setFolder(new HibernateFolder());
		hibMap.setBgColour("100,100,100");
		hibMap.setText("MapText");
		hibMap.setDescription("MapText");
		hibMap.setEnabledGrid(true);
		hibMap.setGridX(5);
		hibMap.setGridY(4);
		hibMap.setEnabledSnap(false);
		HibernateRootMapObject hibRMO = new HibernateRootMapObject();
		Wrappable wrappable = new Wrappable(hibRMO);
		wrappable.setObjectType(syntServ.getRootMapObjectType());
		IMapObject child=ica.wrap(wrappable);
		IMap testMap = new Map(hibMap);
		return testMap;
	}

	@After
	public void tearDown() throws Exception {
	}

	@Ignore
	@Test
	public void testParser() throws NdomException{
		NDOMFactory f=new MetabolicNDOMFactory(testMap1.getTheSingleRootMapObject());
		f.parse();
		IModel m=f.getNdom();
	}
}


/*
 * $Log$
 * Revision 1.1  2008/06/02 10:32:56  asorokin
 * NDOM facility
 *
 */