import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

/**
 * Created by yksenofontov on 08.07.2016.
 */

public final class ClipboardAccess {

    private static ClipboardAccess instance = null;

    private ClipboardAccess() {
    }

    public static ClipboardAccess getInstance() {
        if (instance == null)
            instance = new ClipboardAccess();
        return instance;
    }

    public void copyToClipboard(String data){
        String myString = data;
        StringSelection stringSelection = new StringSelection(myString);
        Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
        clpbrd.setContents(stringSelection, null);
    }

}
