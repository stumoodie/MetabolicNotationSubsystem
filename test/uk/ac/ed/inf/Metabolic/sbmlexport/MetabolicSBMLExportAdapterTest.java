package uk.ac.ed.inf.Metabolic.sbmlexport;

import static org.junit.Assert.assertFalse;

import java.io.FileOutputStream;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import uk.ac.ed.inf.Metabolic.IExportAdapter;
import uk.ac.ed.inf.Metabolic.ndomAPI.IModel;

@RunWith(JMock.class)
public class MetabolicSBMLExportAdapterTest {
	Mockery mockery = new JUnit4Mockery();
	
    IExportAdapter<IModel> exportAdapter;
	@Before
	public void setUp() throws Exception {
		exportAdapter=new MetabolicSBMLExportAdapter<IModel>();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test(expected=IllegalArgumentException.class)
	public void testCreateTargetPreconditions() throws Exception {
		exportAdapter.createTarget(null);
	}

	@Test
	public void testIsTargetCreatedIsInitiallyFalse() {
	  assertFalse(exportAdapter.isTargetCreated()) ;
	}

	@Test(expected=IllegalArgumentException.class)
	public void testWriteTargetPrecondition1() throws Exception{
		exportAdapter.writeTarget(null);
		assertFalse(exportAdapter.isTargetCreated());
	}
	
	@Test(expected=IllegalStateException.class)
	public void testWriteTargetFailsIfTargetNotCreated() throws Exception{
		exportAdapter.writeTarget(new FileOutputStream("file"));
	}
	
	@Test
	public void testWriteTargetPassesIfTargetCreated() throws Exception{
		final IModel model = mockery.mock(IModel.class);
		exportAdapter.createTarget(model);
		exportAdapter.writeTarget(new FileOutputStream("file"));
	}
}
