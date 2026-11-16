package chk;

import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;

/** Track #E2E5E8 with #A8B2BC frame; fill is the green gradient from the mock-up. */
public class ChkProgressBarUI extends BasicProgressBarUI {
    public static ComponentUI createUI(JComponent c) { return new ChkProgressBarUI(); }

    @Override public Dimension getPreferredSize(JComponent c) {
        Dimension d = super.getPreferredSize(c);
        int t = progressBar.isStringPainted() ? 17 : 13;
        if (progressBar.getOrientation() == JProgressBar.HORIZONTAL) d.height = Math.max(d.height, t); else d.width = Math.max(d.width, t);
        return d;
    }

    private void track(Graphics2D g, int w, int h) {
        g.setColor(new Color(0xE2E5E8)); g.fillRect(0, 0, w, h);
        g.setColor(new Color(0xA8B2BC)); g.drawRect(0, 0, w - 1, h - 1);
        g.setColor(new Color(0, 0, 0, 28)); g.drawLine(1, 1, w - 2, 1); g.drawLine(1, 1, 1, h - 2);
    }

    private void bar(Graphics2D g, int x, int y, int w, int h) {
        if (w < 2 || h < 2) return;
        Chk.Skin s = progressBar.isEnabled() ? Chk.GREEN : Chk.DISABLED;
        Chk.glossy(g, x, y, w, h, 0, s, progressBar.getOrientation() != JProgressBar.HORIZONTAL, false);
    }

    @Override protected void paintDeterminate(Graphics g, JComponent c) {
        Graphics2D g2 = Chk.g2(g, c);
        Insets b = progressBar.getInsets();
        int w = progressBar.getWidth(), h = progressBar.getHeight();
        track(g2, w, h);
        int bw = w - b.left - b.right, bh = h - b.top - b.bottom;
        int full = getAmountFull(b, bw, bh);
        if (progressBar.getOrientation() == JProgressBar.HORIZONTAL) bar(g2, b.left, b.top, full, bh);
        else bar(g2, b.left, h - b.bottom - full, bw, full);
        g2.dispose();
        if (progressBar.isStringPainted()) { Graphics2D t = Chk.text((Graphics2D) g.create(), c); paintString(t, b.left, b.top, bw, bh, full, b); t.dispose(); }
    }

    @Override protected void paintIndeterminate(Graphics g, JComponent c) {
        Graphics2D g2 = Chk.g2(g, c);
        Insets b = progressBar.getInsets();
        int w = progressBar.getWidth(), h = progressBar.getHeight();
        track(g2, w, h);
        Rectangle box = getBox(null);
        if (box != null) bar(g2, box.x, box.y, box.width, box.height);
        g2.dispose();
        if (progressBar.isStringPainted()) { Graphics2D t = Chk.text((Graphics2D) g.create(), c); paintString(t, b.left, b.top, w - b.left - b.right, h - b.top - b.bottom, 0, b); t.dispose(); }
    }
}
