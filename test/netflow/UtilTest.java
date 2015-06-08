package netflow;

import static org.junit.Assert.*;
import static netflow.Util.bytesToUnsignedShort;
import static netflow.Util.bytesToUnsignedInt;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class UtilTest
{
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception
	{}
	
	@Before
	public void setUp() throws Exception
	{}
	
	@After
	public void tearDown() throws Exception
	{}
	
	@Test
	public void testBytesToUnsignedShort()
	{
		
		byte[] test = {
				(byte) 0x00 ,
				(byte) 0x00 ,
				(byte) 0x80 ,
				(byte) 0xFF ,
				(byte) 0x24 ,
				(byte) 0x68 ,
				(byte) 0xAC ,
				(byte) 0xE0 ,
				(byte) 0x9B ,
				(byte) 0x57 ,
				(byte) 0x13 ,
				(byte) 0x00 ,
				(byte) 0xFF ,
				(byte) 0xFF ,
				(byte) 0x00 };
		
		assertEquals(bytesToUnsignedShort(test,  0), 0x0000);
		assertEquals(bytesToUnsignedShort(test,  1), 0x0080);
		assertEquals(bytesToUnsignedShort(test,  2), 0x80FF);
		assertEquals(bytesToUnsignedShort(test,  3), 0xFF24);
		assertEquals(bytesToUnsignedShort(test,  4), 0x2468);
		assertEquals(bytesToUnsignedShort(test,  5), 0x68AC);
		assertEquals(bytesToUnsignedShort(test,  6), 0xACE0);
		assertEquals(bytesToUnsignedShort(test,  7), 0xE09B);
		assertEquals(bytesToUnsignedShort(test,  8), 0x9B57);
		assertEquals(bytesToUnsignedShort(test,  9), 0x5713);
		assertEquals(bytesToUnsignedShort(test, 10), 0x1300);
		assertEquals(bytesToUnsignedShort(test, 11), 0x00FF);
		assertEquals(bytesToUnsignedShort(test, 12), 0xFFFF);
		assertEquals(bytesToUnsignedShort(test, 13), 0xFF00);
	}
	
	@Test
	public void testBytesToUnsignedInt()
	{
		
		byte[] test = {
				(byte) 0x00 ,
				(byte) 0x00 ,
				(byte) 0x80 ,
				(byte) 0xFF ,
				(byte) 0x24 ,
				(byte) 0x68 ,
				(byte) 0xAC ,
				(byte) 0xE0 ,
				(byte) 0x9B ,
				(byte) 0x57 ,
				(byte) 0x13 ,
				(byte) 0x00 ,
				(byte) 0xFF ,
				(byte) 0xFF ,
				(byte) 0x00 };
		
		assertEquals(bytesToUnsignedInt(test,  0), 0x000080FFL);
		assertEquals(bytesToUnsignedInt(test,  1), 0x0080FF24L);
		assertEquals(bytesToUnsignedInt(test,  2), 0x80FF2468L);
		assertEquals(bytesToUnsignedInt(test,  3), 0xFF2468ACL);
		assertEquals(bytesToUnsignedInt(test,  4), 0x2468ACE0L);
		assertEquals(bytesToUnsignedInt(test,  5), 0x68ACE09BL);
		assertEquals(bytesToUnsignedInt(test,  6), 0xACE09B57L);
		assertEquals(bytesToUnsignedInt(test,  7), 0xE09B5713L);
		assertEquals(bytesToUnsignedInt(test,  8), 0x9B571300L);
		assertEquals(bytesToUnsignedInt(test,  9), 0x571300FFL);
		assertEquals(bytesToUnsignedInt(test, 10), 0x1300FFFFL);
		assertEquals(bytesToUnsignedInt(test, 11), 0x00FFFF00L);
	}
	
}
