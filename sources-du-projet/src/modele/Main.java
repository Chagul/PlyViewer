package modele;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class Main {

	private static final String NODE_NAME = "recent_files";
    public static void main(String[] args) {
        Preferences prefs = Preferences.userRoot().node(NODE_NAME);
        int val = prefs.getInt("TEST", 10);
        val++;
        prefs.putInt("TEST", val);
        System.out.println(val);
        System.out.println(prefs.absolutePath());
        try {
			prefs.clear();
		} catch (BackingStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.out.println(val);
        System.out.println(prefs.absolutePath());
    }

}
