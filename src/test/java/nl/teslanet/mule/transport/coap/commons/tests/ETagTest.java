/*******************************************************************************
 * Copyright (c) 2017, 2018 (teslanet.nl) Rogier Cobben.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Public License - v 2.0 which accompany this distribution.
 * 
 * The Eclipse Public License is available at
 *    http://www.eclipse.org/legal/epl-v20.html
 * 
 * Contributors:
 *    (teslanet.nl) Rogier Cobben - initial creation
 ******************************************************************************/

package nl.teslanet.mule.transport.coap.commons.tests;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import nl.teslanet.mule.transport.coap.commons.options.ETag;

/**
 * @author Rogier Cobben
 *
 */
public class ETagTest {

	@Rule 
	public ExpectedException exception = ExpectedException.none();

	@Test
	public void testConstructor() {
		String etagValue1 = "afb990";
		byte[] etagValue2 = { (byte) 0xAF, (byte) 0xB9, (byte) 0x90 };

		ETag etag1 = new ETag(etagValue1);
		ETag etag2 = new ETag(etagValue2);

		assertTrue("ETag contruction from String failed", etag1.toString().equals(etagValue1));
		assertTrue("ETag contruction from Byte[] failed", etag2.toString().equals(etagValue1));
	}

	@Test
	public void testEquals()
	{
		String etagValue1 = "ffb990";
		byte[] etagValue2 = { (byte) 0xFF, (byte) 0xB9, (byte) 0x90 };
		String etagValue3 = "afb991";
		String etagValue4 = "afb99100112233";

		ETag etag1 = new ETag(etagValue1);
		ETag etag2 = new ETag(etagValue2);
		ETag etag3 = new ETag(etagValue3);
		ETag etag4 = new ETag(etagValue4);

		assertTrue("ETag.equals failed to compare to equal etags", etag1.equals(etag1));

		assertTrue("ETag.equals failed to compare to equal etags", etag1.equals(etag2));
		assertTrue("ETag.equals failed to compare to equal etags", etag2.equals(etag1));

		assertFalse("ETag.equals failed to compare to unequal etags", etag1.equals(etag3));
		assertFalse("ETag.equals failed to compare to unequal etags", etag3.equals(etag1));
		assertFalse("ETag.equals failed to compare to unequal etags", etag1.equals(etag4));
		assertFalse("ETag.equals failed to compare to unequal etags", etag4.equals(etag1));
	}
	
	@Ignore // should Etag class validate or californium?
	@Test
	public void tooLongString() throws Exception 
	{
		String etagValue1 = "112233445566778899";
		ETag etag1 = null;
	    exception.expect(Exception.class);
	    //exception.expectMessage(containsString("exception message"));
		etag1 = new ETag(etagValue1);
		org.junit.Assert.assertNotNull(etag1);
	}
}
