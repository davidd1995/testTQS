package superkey.ui;

import superkey.keychain.KeyEntry;
import superkey.keychain.KeyChain;
import superkey.keychain.CipherTool;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import static superkey_old.SuperKey.nextSessionPass;

/**
 *
 * @author ico
 */
public class UserMenu {

    private static final String KEYCHAIN_FILE = "Keychain.txt";
    private static final String KEYCHAIN_MASTER_KEY = "#wisper"; // "#wisper";

    private static Logger logger = Logger.getLogger(UserMenu.class.getName());
    private static Scanner currentScanner = new Scanner(System.in);

    public static void main(String[] args) {

        currentScanner.useDelimiter("\\n");
        File userKeychainFile = new File(KEYCHAIN_FILE);
        
        try {
            logger.info("using file " + userKeychainFile.getCanonicalPath());
            

            KeyChain keyChain = KeyChain.from(userKeychainFile, new CipherTool(KEYCHAIN_MASTER_KEY));

            int option;
            while ((option = askUserForSelection()) != 0) {
                switch (option) {
                    case 1:
                        createNewEntry(keyChain);
                        break;
                    case 2:
                        listAllEntries(keyChain);
                        break;
                    case 3:
                        findEntryByKey(keyChain);
                        break;
                }
            }

            if (currentScanner != null) {
                currentScanner.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(UserMenu.class.getName()).log(Level.SEVERE, "Errors handling the keychain: " + ex.getMessage(), ex);
        }
    }

    private static void findEntryByKey(KeyChain keychain) throws FileNotFoundException {

        String searchKey = promptForString("Nome da aplicação/site? ");
        KeyEntry found = keychain.find(searchKey);
        if (null != found) {
            System.out.println("Your existing key: " + found.toString());
        } else {
            System.out.println("No match for key >" + searchKey + "<");
        }

    }

    private static void listAllEntries(KeyChain keychain) throws FileNotFoundException {

        for (KeyEntry entry : keychain.sortedEntries()) {
            System.out.println(entry.toString());
        }
    }

    /**
     * prompts for a new entry and adds it to the current keychain
     *
     * @param stdin
     * @param users
     * @return
     * @throws IOException
     */
    private static void createNewEntry(KeyChain keychain) throws IOException {
        KeyEntry entry = new KeyEntry();

        String appName = promptForString("Aplicação/site?");
        String userName = promptForString("Utilizador?");

        if (appName.isEmpty() || userName.isEmpty()) {
            // nothing to do here
        } else {
            String yn = promptForString("Gerar password? (y/n)?");
            if ("y".equalsIgnoreCase(yn)) {
                entry.setPassword(nextSessionPass());
                System.out.println("Nova Pass > " + entry.getPassword());
            } else {
                entry.setPassword(promptForString("Password?"));
            }
            entry.setApplicationName(appName);
            entry.setUsername(userName);
            
            keychain.put(entry);
            keychain.save();
        }
    }

    /**
     * prints the menu and reads the user option
     *
     * @return selected option
     */
    private static int askUserForSelection() {
        int op = 0;
        do {
            System.out.println();
            System.out.println(
                    "1- Criar nova entrada\n"
                    + "2- Listar tudo\n"
                    + "3- Pesquisar credenciais p/ aplicação\n"
                    + "0- Sair");
            System.out.print("Opção? ");
            op = currentScanner.nextInt();
           
        } while (op < 0 || op > 3);
        return op;
    }

    private static String promptForString(String message) {
        String input;               
        System.out.println(message);
        input = currentScanner.next();
        return input;
    }

}
