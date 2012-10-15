package asi.beans;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class SplitOutputStreamTest {
	
	ByteArrayOutputStream a;
	ByteArrayOutputStream b;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {

		a = new ByteArrayOutputStream();
		b = new ByteArrayOutputStream();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testWriteInt() throws IOException {
		OutputStream output = new SplitOutputStream(a, b);
		output.write(42);
		assertEquals(a.toByteArray()[0], 42);
		assertEquals(b.toByteArray()[0], 42);
	}
	
	@Test
	public void testWrite_BigInt() throws IOException {
		OutputStream output = new SplitOutputStream(a, b);
		output.write(256 + 42);
		assertEquals(a.toByteArray()[0], 42);
		assertEquals(b.toByteArray()[0], 42);
	}

	@Test
		public void testWriteByteArray() throws IOException {
		OutputStream output = new SplitOutputStream(a, b);
		byte[] bytes = {1, 2, 3, 4};
		output.write(bytes);
		assertArrayEquals(a.toByteArray(), bytes);
		assertArrayEquals(b.toByteArray(), bytes);
	}

	@Test
	public void testWriteByteArrayIntInt() throws IOException {
		OutputStream output = new SplitOutputStream(a, b);
		byte[] bytes = {1, 2, 3, 4};
		output.write(bytes, 1, 2);
		assertArrayEquals(a.toByteArray(), new byte[] {2, 3});
		assertArrayEquals(b.toByteArray(), new byte[] {2, 3});
	}

}
