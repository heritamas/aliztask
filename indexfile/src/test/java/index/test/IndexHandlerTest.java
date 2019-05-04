package index.test;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import index.IndexHandler;
import index.helper.TestHelper;

public class IndexHandlerTest {
	
	IndexHandler idxh;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		idxh = new IndexHandler(TestHelper.getPathToResource("/test.txt"));
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() throws IOException {
		idxh.indexAFile();
	}

}
