package chk;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;

/** JInternalFrame: the same title bar as top-level windows, with small (18x15) caption buttons like a Windows MDI child. */
public class ChkInternalFrameUI extends BasicInternalFrameUI {
    public ChkInternalFrameUI(JInternalFrame f) { super(f); }
    public static ComponentUI createUI(JComponent c) { return new ChkInternalFrameUI((JInternalFrame) c); }

    @Override protected JComponent createNorthPane(JInternalFrame w) { titlePane = new Title(w); return titlePane; }

    @Override protected void installDefaults() {
        super.installDefaults();
        frame.setOpaque(false);
        if (frame.getFrameIcon() == null || frame.getFrameIcon() instanceof UIResource) frame.setFrameIcon(ChkIcons.appShield(14));
    }

    public static final class Title extends BasicInternalFrameTitlePane {
        public static final int H = 22, BW = 18, BH = 15;
        public Title(JInternalFrame f) { super(f); }

        @Override protected void createButtons() {
            super.createButtons();
            for (JButton b : new JButton[]{iconButton, maxButton, closeButton}) {
                b.putClientProperty("Chk.style", "caption");
                b.setOpaque(false); b.setContentAreaFilled(false); b.setBorderPainted(false); b.setFocusable(false);
                b.setIcon(null); b.setPreferredSize(new Dimension(BW, BH));
            }
            iconButton.putClientProperty("Chk.cap", Chk.Cap.MIN);
            closeButton.putClientProperty("Chk.cap", Chk.Cap.CLOSE);
            maxButton.putClientProperty("Chk.cap", Chk.Cap.MAX);
        }
        @Override protected void setButtonIcons() {
            super.setButtonIcons();
            for (JButton b : new JButton[]{iconButton, maxButton, closeButton}) if (b != null) b.setIcon(null);
            if (maxButton != null) maxButton.putClientProperty("Chk.cap", frame.isMaximum() ? Chk.Cap.RESTORE : Chk.Cap.MAX);
        }
        @Override protected void installDefaults() {
            super.installDefaults();
            selectedTextColor = Chk.TEXT_TITLE; notSelectedTextColor = new Color(0x7B8DA5);
        }
        @Override protected LayoutManager createLayout() { return new Lay(); }

        final class Lay implements LayoutManager {
            public void addLayoutComponent(String n, Component c) { }
            public void removeLayoutComponent(Component c) { }
            public Dimension preferredLayoutSize(Container c) { return new Dimension(100, H); }
            public Dimension minimumLayoutSize(Container c) { return preferredLayoutSize(c); }
            public void layoutContainer(Container c) {
                int x = c.getWidth() - 3, y = (c.getHeight() - 1 - BH) / 2;
                for (JButton b : new JButton[]{closeButton, maxButton, iconButton}) {
                    if (b == null || !b.isVisible()) continue;
                    x -= BW; b.setBounds(x, y, BW, BH); x -= 2;
                }
            }
        }

        @Override protected void paintTitleBackground(Graphics g) { }

        @Override public void paintComponent(Graphics g) {
            Graphics2D g2 = Chk.g2(g, this);
            boolean sel = frame.isSelected() || Boolean.TRUE.equals(frame.getClientProperty("Chk.active"));
            int w = getWidth(), h = getHeight();
            Chk.titleBackground(g2, new java.awt.geom.Rectangle2D.Float(0, 0, w, h), 0, h, sel);
            g2.setColor(new Color(0x95A7BD)); g2.drawLine(0, h - 1, w, h - 1);
            Chk.text(g2, this);
            g2.setFont(Chk.baseFont().deriveFont(Font.BOLD));
            FontMetrics fm = g2.getFontMetrics();
            int x = 5, y = (h - 1 + fm.getAscent() - fm.getDescent()) / 2;
            Icon ic = frame.getFrameIcon();
            if (ic != null) {
                int s = Math.min(ic.getIconHeight(), 14);
                ic.paintIcon(this, g2, x, (h - 1 - ic.getIconHeight()) / 2);
                x += ic.getIconWidth() + 4;
            }
            String t = frame.getTitle();
            if (t != null) {
                int avail = (iconButton != null && iconButton.isVisible() ? iconButton.getX() : closeButton.getX()) - x - 6;
                while (t.length() > 1 && fm.stringWidth(t) > avail) t = t.substring(0, t.length() - 1);
                g2.setColor(sel ? Chk.TEXT_TITLE : new Color(0x7B8DA5));
                g2.drawString(t, x, y);
            }
            g2.dispose();
        }
    }
}
