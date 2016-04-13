/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package superkey.keychain;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ico
 */
public class KeyEntryComparatorTest {

    private KeyEntryComparator comparator = new KeyEntryComparator();
    private KeyEntry entryA, entryB;

    public KeyEntryComparatorTest() {
    }

    @Before
    public void setUp() {
        entryA = new KeyEntry();
        entryA.setApplicationName("appx");
        entryA.setUsername("xx");
        entryB = new KeyEntry();
        entryB.setApplicationName("appy");
        entryB.setUsername("yy");

    }

    @After
    public void tearDown() {
    }

    @Test
    public void testCompare() {

        assertEquals("Comparing equal objects fails", 0, comparator.compare(entryA, entryA));
        assertEquals("Comparing equal objects fails", 0, comparator.compare(entryB, entryB));

        assertThat("faile to identify less than case", comparator.compare(entryA, entryB), Matchers.lessThan(0));
        assertThat("faile to identify greater than case", comparator.compare(entryB, entryA), Matchers.greaterThan(0));

    }

}
