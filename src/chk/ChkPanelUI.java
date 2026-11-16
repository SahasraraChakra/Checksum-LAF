package chk;

import java.beans.PropertyChangeListener;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.BasicPanelUI;

/** Panels: stock painting; a TitledBorder is swapped for one whose caption honours the AA switches. */
public class ChkPanelUI extends BasicPanelUI {
    public static ComponentUI createUI(JComponent c) { return new ChkPanelUI(); }
    private PropertyChangeListener l;

    private static void swap(JComponent c) {
        Border b = c.getBorder();
        if (b instanceof TitledBorder tb && !(b instanceof ChkBorders.Titled)) c.setBorder(new ChkBorders.Titled(tb));
    }
    @Override public void installUI(JComponent c) {
        super.installUI(c);
        l = e -> swap((JComponent) e.getSource());
        c.addPropertyChangeListener("border", l);
        swap(c);
    }
    @Override public void uninstallUI(JComponent c) { c.removePropertyChangeListener("border", l); super.uninstallUI(c); }
}
