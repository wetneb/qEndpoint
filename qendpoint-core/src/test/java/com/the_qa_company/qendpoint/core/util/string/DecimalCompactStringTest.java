package com.the_qa_company.qendpoint.core.util.string;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class DecimalCompactStringTest {
	@Test
	public void doubleCompactTest() {
		DecimalCompactString cs = new DecimalCompactString(new BigDecimal(0));
		for (double vld : new double[] {42, 806.46789, 23456789.345, -2345678.234, -1.097, 0, 8900.2}) {
			// test set
			BigDecimal vl = new BigDecimal(vld);
			cs.setValue(vl);
			assertNull("buffer init", cs.buffer);
			// test using value itself
			assertEquals("bad val (long)", (long)vl.longValue(), cs.longValue());
			assertEquals("bad val (decimal)", vl, cs.decimalValue());
			assertEquals("bad val (double)", vl.doubleValue(), cs.doubleValue(), 0.001);
			assertEquals("bad clone", cs, cs.copy());
			// no init
			assertNull("buffer init", cs.buffer);
			assertEquals("bad val str", String.valueOf(vl), cs.toString());
			assertNotNull("buffer not init", cs.buffer);
			// test buffer
			byte[] buff = cs.getBuffer();
			assertArrayEquals("bad buff", String.valueOf(vl).getBytes(ByteStringUtil.STRING_ENCODING), buff);
			// test copy
			assertEquals("bad clone", cs, cs.copy());
			assertEquals("bad clone", 0, cs.compareTo(cs.copyAppend("")));
			CompactString cs2 = new CompactString(buff);
			assertEquals("bad copy", 0, cs.compareTo(cs2));
			assertEquals("bad hash", cs.hashCode(), cs2.hashCode());
			//assertEquals("bad long", cs.longValue(), cs2.longValue()); // can't format double
			assertEquals("bad double", cs.doubleValue(), cs2.doubleValue(), 0.001);
			assertEquals("bad decimal", cs.decimalValue().doubleValue(), cs2.decimalValue().doubleValue(), 0.001);
		}
	}

}