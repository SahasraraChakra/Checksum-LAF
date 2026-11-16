package chk;

import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

/** Tabs from the mock-up: #E3E9F0 strip, gradient tabs (3px top radius, 2px gap), white bold active tab. */
public class ChkTabbedPaneUI extends BasicTabbedPaneUI {
    public static ComponentUI createUI(JComponent c) { return new ChkTabbedPaneUI(); }
    private static final Color LINE = Chk.TAB_LINE, TAB_LINE = new Color(0x9BB1C7), STRIP = new Color(0xE3E9F0);
    private int hotTab = -1;

    @Override protected void installListeners() {
        super.installListeners();
        java.awt.event.MouseAdapter m = new java.awt.event.MouseAdapter() {
            @Override public void mouseMoved(java.awt.event.MouseEvent e) { setHot(tabForCoordinate(tabPane, e.getX(), e.getY())); }
            @Override public void mouseExited(java.awt.event.MouseEvent e) { setHot(-1); }
        };
        tabPane.addMouseMotionListener(m); tabPane.addMouseListener(m);
    }
    private void setHot(int i) { if (i != hotTab) { hotTab = i; tabPane.repaint(); } }

    @Override protected void installDefaults() {
        super.installDefaults();
        tabInsets = new Insets(3, 10, 3, 10);
        selectedTabPadInsets = new Insets(1, 0, 0, 0);
        tabAreaInsets = new Insets(3, 6, 0, 6);
        contentBorderInsets = new Insets(1, 1, 1, 1);
        tabPane.setOpaque(false);
    }

    @Override protected int getTabLabelShiftY(int p, int i, boolean sel) { return 0; }
    @Override protected int getTabLabelShiftX(int p, int i, boolean sel) { return 0; }

    private Shape shape(int p, int x, int y, int w, int h) {
        int r = 6;
        w -= 2;                                       // 2px gap between tabs
        Area a;
        switch (p) {
            case BOTTOM -> a = new Area(Chk.rr(x + .5, y - r, w - 1, h + r - .5, r));
            case LEFT -> a = new Area(Chk.rr(x + .5, y + .5, w + r, h - 3, r));
            case RIGHT -> a = new Area(Chk.rr(x - r, y + .5, w + r - .5, h - 3, r));
            default -> a = new Area(Chk.rr(x + .5, y + .5, w - 1, h + r, r));
        }
        a.intersect(new Area(new Rectangle(x, y, w + 1, h)));
        return a;
    }

    @Override public void paint(Graphics g, JComponent c) {
        // tab strip background
        Graphics2D g2 = (Graphics2D) g.create();
        int p = tabPane.getTabPlacement();
        Insets in = tabPane.getInsets();
        int w = tabPane.getWidth() - in.left - in.right, h = tabPane.getHeight() - in.top - in.bottom;
        g2.setColor(STRIP);
        switch (p) {
            case BOTTOM -> g2.fillRect(in.left, in.top + h - calculateTabAreaHeight(p, runCount, maxTabHeight), w, calculateTabAreaHeight(p, runCount, maxTabHeight));
            case LEFT -> g2.fillRect(in.left, in.top, calculateTabAreaWidth(p, runCount, maxTabWidth), h);
            case RIGHT -> g2.fillRect(in.left + w - calculateTabAreaWidth(p, runCount, maxTabWidth), in.top, calculateTabAreaWidth(p, runCount, maxTabWidth), h);
            default -> g2.fillRect(in.left, in.top, w, calculateTabAreaHeight(p, runCount, maxTabHeight));
        }
        g2.dispose();
        super.paint(g, c);
    }

    @Override protected void paintTabBackground(Graphics g, int p, int i, int x, int y, int w, int h, boolean sel) {
        Graphics2D g2 = Chk.g2(g, tabPane);
        Shape s = shape(p, x, y, w, h);
        boolean hot = i == hotTab && tabPane.isEnabledAt(i);
        Color t = sel ? Color.WHITE : hot ? new Color(0xF4FAFE) : new Color(0xEFF3F7);
        Color b = sel ? Color.WHITE : hot ? new Color(0xDCEEFB) : new Color(0xD8E2EC);
        boolean vertical = p == LEFT || p == RIGHT;
        g2.setPaint(vertical ? new GradientPaint(x, 0, t, x + w, 0, b) : new GradientPaint(0, y, t, 0, y + h, b));
        g2.fill(s);
        g2.dispose();
    }

    @Override protected void paintTabBorder(Graphics g, int p, int i, int x, int y, int w, int h, boolean sel) {
        Graphics2D g2 = Chk.g2(g, tabPane);
        boolean hot = i == hotTab && tabPane.isEnabledAt(i);
        g2.setColor(sel ? LINE : hot ? new Color(0x72B8E5) : TAB_LINE);
        g2.draw(shape(p, x, y, w, h));
        g2.dispose();
    }

    @Override protected void paintFocusIndicator(Graphics g, int p, Rectangle[] rects, int i, Rectangle iconRect, Rectangle textRect, boolean sel) {
        if (tabPane.hasFocus() && sel) Chk.dottedRect((Graphics2D) g, textRect.x - 3, textRect.y - 1, textRect.width + 6, textRect.height + 2);
    }

    @Override protected void paintText(Graphics g, int p, Font font, FontMetrics fm, int i, String title, Rectangle tr, boolean sel) {
        Graphics2D g2 = Chk.text((Graphics2D) g.create(), tabPane);
        Font f = sel ? font.deriveFont(Font.BOLD) : font;
        g2.setFont(f);
        fm = g2.getFontMetrics();
        g2.setColor(!tabPane.isEnabledAt(i) ? Chk.TEXT_OFF : sel ? new Color(0x0B4A9E) : new Color(0x334155));
        javax.swing.plaf.basic.BasicGraphicsUtils.drawStringUnderlineCharAt(g2, title, tabPane.getDisplayedMnemonicIndexAt(i), tr.x, tr.y + fm.getAscent());
        g2.dispose();
    }

    @Override protected int calculateTabWidth(int p, int i, FontMetrics fm) {
        // active tab is bold -> reserve the wider width so tabs do not jump
        FontMetrics bold = tabPane.getFontMetrics(tabPane.getFont().deriveFont(Font.BOLD));
        return super.calculateTabWidth(p, i, bold) + 2;
    }

    @Override protected void paintContentBorder(Graphics g, int p, int sel) {
        int w = tabPane.getWidth(), h = tabPane.getHeight();
        Insets in = tabPane.getInsets();
        int x = in.left, y = in.top, cw = w - in.right - in.left, ch = h - in.top - in.bottom;
        switch (p) {
            case LEFT -> { x += calculateTabAreaWidth(p, runCount, maxTabWidth); cw -= (x - in.left); }
            case RIGHT -> cw -= calculateTabAreaWidth(p, runCount, maxTabWidth);
            case BOTTOM -> ch -= calculateTabAreaHeight(p, runCount, maxTabHeight);
            default -> { y += calculateTabAreaHeight(p, runCount, maxTabHeight); ch -= (y - in.top); }
        }
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(Color.WHITE); g2.fillRect(x, y, cw, ch);
        g2.setColor(LINE); g2.drawRect(x, y, cw - 1, ch - 1);
        if (sel >= 0 && sel < rects.length) {          // open the line under the active tab
            Rectangle r = rects[sel];
            g2.setColor(Color.WHITE);
            if (p == TOP) g2.drawLine(r.x + 1, y, r.x + r.width - 3, y);
            else if (p == BOTTOM) g2.drawLine(r.x + 1, y + ch - 1, r.x + r.width - 3, y + ch - 1);
        }
        g2.dispose();
    }

    @Override protected void paintContentBorderTopEdge(Graphics g, int p, int s, int x, int y, int w, int h) { }
    @Override protected void paintContentBorderBottomEdge(Graphics g, int p, int s, int x, int y, int w, int h) { }
    @Override protected void paintContentBorderLeftEdge(Graphics g, int p, int s, int x, int y, int w, int h) { }
    @Override protected void paintContentBorderRightEdge(Graphics g, int p, int s, int x, int y, int w, int h) { }
}
