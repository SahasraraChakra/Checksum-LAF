package chk;

import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.BasicLabelUI;

/** JLabel (also list / table / tree cell renderers) with switchable text anti-aliasing. */
public class ChkLabelUI extends BasicLabelUI {
    private static final ChkLabelUI INSTANCE = new ChkLabelUI();
    public static ComponentUI createUI(JComponent c) { return INSTANCE; }
    @Override public void paint(Graphics g, JComponent c) {
        Graphics2D g2 = Chk.text((Graphics2D) g.create(), c);
        super.paint(g2, c);
        g2.dispose();
    }
}
