package chk;

import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;

public class ChkToolBarUI extends BasicToolBarUI {
    public static ComponentUI createUI(JComponent c) { return new ChkToolBarUI(); }
    @Override public void update(Graphics g, JComponent c) {
        Graphics2D g2 = (Graphics2D) g.create();
        boolean h = ((JToolBar) c).getOrientation() == SwingConstants.HORIZONTAL;
        g2.setPaint(h ? new GradientPaint(0, 0, new Color(0xFDFDFE), 0, c.getHeight(), new Color(0xECEEF2))
                      : new GradientPaint(0, 0, new Color(0xFDFDFE), c.getWidth(), 0, new Color(0xECEEF2)));
        g2.fillRect(0, 0, c.getWidth(), c.getHeight());
        g2.dispose();
        paint(g, c);
    }

    /** 1px #CBD3DC divider, 14px tall like the mock-up. */
    public static class Separator extends BasicToolBarSeparatorUI {
        public static ComponentUI createUI(JComponent c) { return new Separator(); }
        @Override public void paint(Graphics g, JComponent c) {
            Dimension s = c.getSize();
            boolean vertical = !(c.getParent() instanceof JToolBar tb) || tb.getOrientation() == SwingConstants.HORIZONTAL;
            g.setColor(new Color(0xCBD3DC));
            if (vertical) { int x = s.width / 2; g.drawLine(x, (s.height - 14) / 2, x, (s.height + 14) / 2); }
            else { int y = s.height / 2; g.drawLine((s.width - 14) / 2, y, (s.width + 14) / 2, y); }
        }
    }
}
