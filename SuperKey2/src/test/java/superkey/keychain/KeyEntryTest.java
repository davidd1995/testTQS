/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package superkey.keychain;

import java.io.File;
import java.io.IOException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ico
 */
public class KeyEntryTest {
    private KeyEntry entryA, entryEmpty;
    private KeyChain k;
    
    String KEYCHAIN_FILE = "Keychain.txt";
    String KEYCHAIN_MASTER_KEY = "#wisper";
    
    File userKeychainFile = new File(KEYCHAIN_FILE);
    
    public KeyEntryTest() {
    }
    
    @Before
    public void setUp() throws IOException {
        entryA = new KeyEntry();
        entryA.setApplicationName("appx");
        entryA.setUsername("xx");
        entryA.setPassword("secret@@@");
        k = KeyChain.from(userKeychainFile, new CipherTool(KEYCHAIN_MASTER_KEY));
        
    }
    @After
    public void tearDown() {
    }

   
    @Test(expected=IOException.class )
    public void KeyEntryTest() throws IOException {
        String KEYCHAIN_1 = "testing";
        k = KeyChain.from(userKeychainFile, new CipherTool(KEYCHAIN_1));
    }
    
    @Test
    public void canGetAfterAdded() throws IOException{
        k.put(entryA);
        KeyEntry entryB = k.find(entryA.key());
        assertEquals("Não são iguais", entryA.key(), entryB.key());
        assertEquals("Não são iguais ", entryA, entryB);
    }
    @Test
    public void testAddingNew() throws IOException{
        int count = 0, verify = 0;
        Iterable<KeyEntry> itr = k.allEntries();
        for(KeyEntry j: itr){
            count++;
        }
        k.put(entryA);
        Iterable<KeyEntry> itrverify = k.allEntries();
        for(KeyEntry j: itrverify){
            verify++;
        }
        KeyEntry entryB = k.find(entryA.key());
        assertEquals("Não são iguais", entryA, entryB);
        assertEquals("Não foi adicionada", count+1, verify);
    }
    
    @Test( expected = IllegalArgumentException.class)
    public void testSetApplicationNameWithNull() {
        entryA.setApplicationName( null);
    }

    @Test
    public void testKey() {
        // the key is the application name
        assertEquals("failed to get existing key field", entryA.getApplicationName(), "appx");
    }

    @Test
    public void testFormatAsCsv() {
       String expects = "appx" + KeyEntry.FIELDS_DELIMITER + "xx" + KeyEntry.FIELDS_DELIMITER + "secret@@@";
       assertEquals("failed to format entry to delimited string", entryA.formatAsCsv(), expects);
                   
    }

    @Test
    public void testToString() {
        String expects = "appx" + "\t" + "xx" + "\t" + "secret@@@";
        assertEquals("failed", entryA.toString(), expects);;
    }

    @Test
    public void testParse() {
       String expects = "appx" + KeyEntry.FIELDS_DELIMITER + "xx" + KeyEntry.FIELDS_DELIMITER + "secret@@@";
       KeyEntry test = new KeyEntry();
       test = test.parse(expects);
       assertEquals("failed ", test.getUsername(), "xx");
       assertEquals("failed ", test.getApplicationName(), "appx");
       assertEquals("failed ", test.getPassword(), "secret@@@");
    }
    
}
