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

	TreeItem root = new TreeItem("");


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
		FolderHandler.buildTreeStructureFromList(root, readables, TreeItem::setReadable, true);
		FolderHandler.buildTreeStructureFromList(root, writables, TreeItem::setWritable, true);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCalculateLeastReadableAttribute() {
		root.calculateLeastReadableAttribute(true);
		
		System.out.format("leastreadables: %s%n", root);
	}
	
	//@Test
	public void testClone() throws CloneNotSupportedException {
		TreeItem cloned = root.clone();
		
		System.out.format("cloned: %s%n", cloned);
	}
	
	@Test
	public void testGetWritablesSimple() throws CloneNotSupportedException {
		TreeItem result = FolderHandler.getWritablesSimple(root);

		System.out.format("result tree (simple): %s%n", result);
		System.out.format("result (simple): %s%n", result.getWritablePaths(null, true));
	}

	//@Test
	public void testGetWritablesComplex() throws CloneNotSupportedException {
		TreeItem result = FolderHandler.getWritablesComplex(root);

		System.out.format("result tree (complex): %s%n", result);
		System.out.format("result (complex): %s%n", result.getWritablePaths(null, true));
	}
}
