package chk;

import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;

public class ChkSplitPaneUI extends BasicSplitPaneUI {
    public static ComponentUI createUI(JComponent c) { return new ChkSplitPaneUI(); }
    @Override public BasicSplitPaneDivider createDefaultDivider() {
        return new BasicSplitPaneDivider(this) {
            @Override public void paint(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                boolean hz = splitPane.getOrientation() == JSplitPane.HORIZONTAL_SPLIT;
                int w = getWidth(), h = getHeight();
                g2.setPaint(hz ? new GradientPaint(0, 0, new Color(0xECEFF3), w, 0, new Color(0xDCE1E7)) : new GradientPaint(0, 0, new Color(0xECEFF3), 0, h, new Color(0xDCE1E7)));
                g2.fillRect(0, 0, w, h);
                g2.setColor(new Color(0xC4CBD4));
                if (hz) { g2.drawLine(0, 0, 0, h); g2.drawLine(w - 1, 0, w - 1, h); } else { g2.drawLine(0, 0, w, 0); g2.drawLine(0, h - 1, w, h - 1); }
                g2.dispose();
                super.paintComponents(g);
            }
        };
    }
}
