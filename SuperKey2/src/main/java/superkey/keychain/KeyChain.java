/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package superkey.keychain;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * a keychain with many key allEntries
 * the key chain is expected to be bound to a file, given at construction.
 * @author ico
 */
public class KeyChain {
       
    private final HashMap<String, KeyEntry> keyEntriesCollection = new HashMap<>();
    private final CipherTool cipher;
    private final File targetFile;

    public static KeyChain from( File file, CipherTool cipherModule) throws IOException {
        KeyChain chain = new KeyChain( file, cipherModule);  
        if( file.exists() ) {
           cipherModule.readProtectedKeychain( chain, file );
        }
            
        return chain ;
    }

    private KeyChain(File file, CipherTool cipher) {
        this.cipher = cipher;
        this.targetFile = file;
    }
    
   public void put( KeyEntry entry) {       
      if( keyEntriesCollection.containsKey(entry.key())) {
          throw new IllegalArgumentException( "Duplicated key: " + entry.key());
      }
      keyEntriesCollection.put( entry.key(), entry);      
   }         
   
   public KeyEntry find( String key) {
       return keyEntriesCollection.get(key);
   }

    public void save() {
        try {
            cipher.writeProtectedKeychain( this.formatAsCsv(), targetFile);
        } catch (IOException ex) {
            Logger.getLogger(KeyChain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Iterable<KeyEntry> allEntries() {
        return keyEntriesCollection.values();
    }
    
    public Iterable<KeyEntry> sortedEntries() {
        
        ArrayList<KeyEntry> list = new ArrayList<>(keyEntriesCollection.values());
        Collections.sort( list, new KeyEntryComparator());                        
        return list;
    }

    private String formatAsCsv() {
        StringBuilder builder = new StringBuilder();
        for (KeyEntry entry : keyEntriesCollection.values()) {
            builder.append(entry.formatAsCsv() );
            builder.append("\n");
        }     
        return builder.toString();
    }     
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (KeyEntry entry : keyEntriesCollection.values()) {
            builder.append(entry.toString() );
            builder.append("\n");
        }        
        return builder.toString();
    }            
}
