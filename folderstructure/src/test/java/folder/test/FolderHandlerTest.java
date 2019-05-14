package folder.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import folder.FolderHandler;
import folder.helper.TestHelper;
import folder.model.TreeItem;

public class FolderHandlerTest {
	
	static List<String> readables;
	static List<String> writables;
	
	FolderHandler handler;

	//TreeItem root = new TreeItem("");


	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		readables = TestHelper.readStringListFromResource("/readable");
		writables = TestHelper.readStringListFromResource("/writable");
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		handler = new FolderHandler("");
		
		handler.buildTreeStructureFromList(readables, TreeItem::setReadable, true);
		handler.buildTreeStructureFromList(writables, TreeItem::setWritable, true);
	}

	@After
	public void tearDown() throws Exception {
	}

	//@Test
	public void testBuildTree() {
		System.out.format("tree: %s%n", handler.getTree());
	}

	//@Test
	public void testCalculateLeastReadableAttribute() {
		handler.getTree().calculateLeastReadableAttribute(true);
		
		System.out.format("leastreadables: %s%n", handler.getTree());
	}
	
	//@Test
	public void testClone() throws CloneNotSupportedException {
		TreeItem cloned = handler.getTree().clone();
		
		System.out.format("cloned: %s%n", cloned);
	}
	
	@Test
	public void testGetWritablesSimple() throws CloneNotSupportedException {
		TreeItem result = handler.getWritablesSimple();

		System.out.format("result tree (simple): %s%n", result);
		System.out.format("result (simple): %s%n", result.getWritablePaths(null, true));
	}

	//@Test
	public void testGetWritablesComplex() throws CloneNotSupportedException {
		TreeItem result = handler.getWritablesComplex();

		System.out.format("result tree (complex): %s%n", result);
		System.out.format("result (complex): %s%n", result.getWritablePaths(null, true));
	}
}
