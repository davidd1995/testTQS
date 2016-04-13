/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package superkey.keychain;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidKeyException;
import javax.crypto.BadPaddingException;
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
    private KeyChain kc;
    
    String KEYCHAIN_FILE = "Keychain.txt";
    String KEYCHAIN_MASTER_KEY = "#wisper";
    
    File userKeychainFile = new File(KEYCHAIN_FILE);
    

    
    @Before
    public void setUp() throws IOException {
        entryA = new KeyEntry();
        entryA.setApplicationName("appx");
        entryA.setUsername("xx");
        entryA.setPassword("secret@@@");
        kc = KeyChain.from(userKeychainFile, new CipherTool(KEYCHAIN_MASTER_KEY));
    }
    
    @After
    public void tearDown() {
    }
    
    @Test(expected=IOException.class )
    public void KeyEntryTest() throws IOException {
        String KEYCHAIN_MASTER = "testing";
        kc = KeyChain.from(userKeychainFile, new CipherTool(KEYCHAIN_MASTER));
    }
    
    @Test
    public void canGetAfterAdded() throws IOException{
        kc.put(entryA);
        KeyEntry entryB = kc.find(entryA.key());
        assertEquals("Both entries arent equal", entryA.key(), entryB.key());
        assertEquals("Both entries arent equal", entryA, entryB);
    }
    
    @Test
    public void testAddingNew() throws IOException{
        int count = 0, verify = 0;
        Iterable<KeyEntry> itr = kc.allEntries();
        for(KeyEntry x: itr){
            count++;
        }
        kc.put(entryA);
        Iterable<KeyEntry> itrverify = kc.allEntries();
        for(KeyEntry x: itrverify){
            verify++;
        }
        KeyEntry entryB = kc.find(entryA.key());
        assertEquals("Both entries arent equal", entryA, entryB);
        assertEquals("Entry not added", count+1, verify);
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
        assertEquals("failed to format entry to delimited string", entryA.toString(), expects);
        
    }

    @Test
    public void testParse() {
       String expects = "appx" + KeyEntry.FIELDS_DELIMITER + "xx" + KeyEntry.FIELDS_DELIMITER + "secret@@@";
       KeyEntry test = new KeyEntry();
       test = test.parse(expects);
       assertEquals("failed to format entry to delimited string", test.getApplicationName(), "appx");
       assertEquals("failed to format entry to delimited string", test.getUsername(), "xx");
       assertEquals("failed to format entry to delimited string", test.getPassword(), "secret@@@");
    }  
    

}
