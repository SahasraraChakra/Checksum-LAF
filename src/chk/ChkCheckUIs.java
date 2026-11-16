package chk;

import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;

/** Check box / radio button delegates (icons come from ChkIcons); text honours the AA switches. */
public final class ChkCheckUIs {
    private ChkCheckUIs() {}
    public static class CheckBox extends BasicCheckBoxUI {
        public static ComponentUI createUI(JComponent c) { return new CheckBox(); }
        @Override public void paint(Graphics g, JComponent c) { Graphics2D g2 = Chk.text(Chk.g2(g, c), c); super.paint(g2, c); g2.dispose(); }
    }
    public static class RadioButton extends BasicRadioButtonUI {
        public static ComponentUI createUI(JComponent c) { return new RadioButton(); }
        @Override public void paint(Graphics g, JComponent c) { Graphics2D g2 = Chk.text(Chk.g2(g, c), c); super.paint(g2, c); g2.dispose(); }
    }
}
