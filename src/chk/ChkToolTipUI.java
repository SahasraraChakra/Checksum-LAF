package chk;

import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;

public class ChkToolTipUI extends BasicToolTipUI {
    public static ComponentUI createUI(JComponent c) { return new ChkToolTipUI(); }
    @Override public void installUI(JComponent c) { super.installUI(c); c.setOpaque(false); }
    @Override public void paint(Graphics g, JComponent c) {
        g.setColor(new Color(0xF4F6F9)); g.fillRect(0, 0, c.getWidth(), c.getHeight());
        g.setColor(new Color(0x9BAAB9)); g.drawRect(0, 0, c.getWidth() - 1, c.getHeight() - 1);
        Graphics2D t = Chk.text((Graphics2D) g.create(), c); super.paint(t, c); t.dispose();
    }
}
