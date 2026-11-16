package chk;

import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;

public class ChkScrollBarUI extends BasicScrollBarUI {
    public static ComponentUI createUI(JComponent c) { return new ChkScrollBarUI(); }

    private JButton arrow(int dir) {
        JButton b = new BasicArrowButton(dir) {
            @Override public Dimension getPreferredSize() { return new Dimension(17, 17); }
            @Override public void paint(Graphics g) {
                Graphics2D g2 = Chk.g2(g, scrollbar);
                ButtonModel m = getModel();
                boolean en = isEnabled();
                Chk.glossy(g2, 0, 0, getWidth(), getHeight(), 3, Chk.skin(en, m.isRollover(), m.isArmed() && m.isPressed()), false);
                Chk.arrow(g2, getWidth() / 2f, getHeight() / 2f, 3.2f, getDirection(), en ? new Color(0x4A5568) : new Color(0xB4BAC2));
                g2.dispose();
            }
        };
        b.setRolloverEnabled(true);
        return b;
    }
    @Override protected JButton createDecreaseButton(int o) { return arrow(o); }
    @Override protected JButton createIncreaseButton(int o) { return arrow(o); }

    @Override protected void paintTrack(Graphics g, JComponent c, Rectangle r) {
        g.setColor(new Color(0xECEFF3)); g.fillRect(r.x, r.y, r.width, r.height);
        g.setColor(new Color(0xD5DAE1));
        if (scrollbar.getOrientation() == VERTICAL) { g.drawLine(r.x, r.y, r.x, r.y + r.height); }
        else { g.drawLine(r.x, r.y, r.x + r.width, r.y); }
    }

    @Override protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
        if (r.isEmpty() || !scrollbar.isEnabled()) return;
        Graphics2D g2 = Chk.g2(g, scrollbar);
        boolean v = scrollbar.getOrientation() == VERTICAL;
        Chk.Skin s = isDragging ? Chk.PRESSED : isThumbRollover() ? Chk.HOT : Chk.NORMAL;
        Chk.glossy(g2, r.x, r.y, r.width, r.height, 3, s, v);
        int cx = r.x + r.width / 2, cy = r.y + r.height / 2;
        g2.setStroke(new BasicStroke(1f));
        for (int i = -1; i <= 1; i++) {
            if (v) { if (r.height < 22) break; g2.setColor(new Color(0x9AA3AE)); g2.drawLine(cx - 4, cy + i * 3, cx + 3, cy + i * 3);
                     g2.setColor(Color.WHITE); g2.drawLine(cx - 4, cy + i * 3 + 1, cx + 3, cy + i * 3 + 1); }
            else { if (r.width < 22) break; g2.setColor(new Color(0x9AA3AE)); g2.drawLine(cx + i * 3, cy - 4, cx + i * 3, cy + 3);
                   g2.setColor(Color.WHITE); g2.drawLine(cx + i * 3 + 1, cy - 4, cx + i * 3 + 1, cy + 3); }
        }
        g2.dispose();
    }

    @Override protected Dimension getMinimumThumbSize() { return new Dimension(17, 17); }
}
