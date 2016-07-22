import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

/**
 * Created by yksenofontov on 08.07.2016.
 */

final class ClipboardAccess {

    private static ClipboardAccess instance = null;

    private ClipboardAccess() {
    }

    static ClipboardAccess getInstance() {
        if (instance == null)
            instance = new ClipboardAccess();
        return instance;
    }

    void copyToClipboard(String data){
        StringSelection stringSelection = new StringSelection(data);
        Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
        clpbrd.setContents(stringSelection, null);
    }

}
