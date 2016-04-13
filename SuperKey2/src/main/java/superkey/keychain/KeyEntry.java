package superkey.keychain;

import java.util.Comparator;


/**
 * a valid entry in a key chain
 * @author ico
 */
public class KeyEntry {
     public static final String FIELDS_DELIMITER = ";";
     
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword( String password) {
        if( null == password) throw new IllegalArgumentException( "password  can't be null");
        if(password.isEmpty()) throw new IllegalArgumentException( "password  can't be empty");
        this.password = password;
    }

    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername( String username) {
        if( null == username) throw new IllegalArgumentException( "username can't be null");
        if(username.isEmpty()) throw new IllegalArgumentException( "username can't be empty");
        this.username = username;
    }

    private String applicationName;

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName( String applicationName) {
        if( null == applicationName) throw new IllegalArgumentException( "Application name can't be null");
        if(applicationName.isEmpty()) throw new IllegalArgumentException( "Application name can't be empty");
        
        this.applicationName = applicationName;
    }

    public String key() {
        return getApplicationName();
    }

    /**
     * 
     * @return string with fields separated by FIELDS_DELIMITER)
     */
    public String formatAsCsv() {
        StringBuilder builder = new StringBuilder();
        builder.append( getApplicationName());builder.append(FIELDS_DELIMITER);
        builder.append( getUsername());builder.append(FIELDS_DELIMITER);
        builder.append( getPassword());
        return builder.toString();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append( getApplicationName());builder.append("\t");
        builder.append( getUsername());builder.append("\t");
        builder.append( getPassword());
        return builder.toString();
    }    
    
     public static KeyEntry parse(String csvLine) {
        String[] parts = csvLine.split(FIELDS_DELIMITER);
        KeyEntry retValue = new KeyEntry();
        retValue.setApplicationName( parts[0]);
        retValue.setUsername(parts[1]);
        retValue.setPassword(parts[2]);
        return retValue;
    }
}


class KeyEntryComparator implements Comparator<KeyEntry> {
    
     @Override
     public int compare(KeyEntry ke1, KeyEntry ke2){
         return ke1.getApplicationName().compareTo( ke2.getApplicationName() );     
    }
}
