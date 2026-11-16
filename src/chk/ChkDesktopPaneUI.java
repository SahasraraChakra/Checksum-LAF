package chk;

import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.BasicDesktopPaneUI;

/** JDesktopPane backdrop: the blue radial wallpaper of the mock-up (#3D79B8 -> #26558E -> #1A3F6E). */
public class ChkDesktopPaneUI extends BasicDesktopPaneUI {
    public static ComponentUI createUI(JComponent c) { return new ChkDesktopPaneUI(); }
    @Override public void update(Graphics g, JComponent c) {
        Graphics2D g2 = (Graphics2D) g.create();
        int w = c.getWidth(), h = c.getHeight();
        float r = Math.max(w, h) * .75f;
        g2.setPaint(new RadialGradientPaint(new java.awt.geom.Point2D.Float(w / 2f, h * .3f), r, new float[]{0f, .6f, 1f},
                new Color[]{new Color(0x3D79B8), new Color(0x26558E), new Color(0x1A3F6E)}));
        g2.fillRect(0, 0, w, h);
        g2.dispose();
        paint(g, c);
    }
}
