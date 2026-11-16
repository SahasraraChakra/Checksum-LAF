package chk;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.*;

/** All borders of the look and feel. Several are public so applications can reuse them (status bar, command bar, result banner). */
public final class ChkBorders {
    private ChkBorders() {}

    /** Space around the label of a push button; the outline is painted by the UI. */
    public static class ButtonBorder extends AbstractBorder implements UIResource {
        @Override public Insets getBorderInsets(Component c, Insets i) {
            if (c.getParent() instanceof JToolBar || "toolbar".equals(((JComponent) c).getClientProperty("Chk.style"))) {
                i.set(3, 7, 3, 7);
            } else {
                Insets m = (c instanceof AbstractButton b && b.getMargin() != null) ? b.getMargin() : new Insets(3, 10, 3, 10);
                i.set(m.top + 1, m.left + 1, m.bottom + 1, m.right + 1);
            }
            return i;
        }
        @Override public Insets getBorderInsets(Component c) { return getBorderInsets(c, new Insets(0, 0, 0, 0)); }
    }

    /** Input frame: 1px #84A1BF, inset shadow, blue on focus (with the 1px halo drawn inside). */
    public static class TextBorder extends AbstractBorder implements UIResource {
        private final Insets in;
        public TextBorder() { this(new Insets(4, 7, 4, 7)); }
        public TextBorder(Insets in) { this.in = in; }
        @Override public Insets getBorderInsets(Component c, Insets i) { i.set(in.top, in.left, in.bottom, in.right); return i; }
        @Override public Insets getBorderInsets(Component c) { return (Insets) in.clone(); }
        @Override public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g.create();
            boolean enabled = c.isEnabled();
            boolean hot = c instanceof JComponent jc && Boolean.TRUE.equals(jc.getClientProperty("Chk.hover"));
            Component fo = KeyboardFocusManager.getCurrentKeyboardFocusManager().getPermanentFocusOwner();
            boolean focus = fo != null && SwingUtilities.isDescendingFrom(fo, c);
            Color outer = !enabled ? Chk.FIELD_OFF : focus ? Chk.FIELD_FOCUS : hot ? new Color(0x3886BC) : Chk.FIELD_BORDER;
            g2.setColor(outer);
            g2.drawRect(x, y, w - 1, h - 1);
            if (enabled && focus) {                       // box-shadow: 0 0 0 1px rgba(37,99,235,.4)
                g2.setColor(Chk.alpha(Chk.FIELD_FOCUS, 100));
                g2.drawRect(x + 1, y + 1, w - 3, h - 3);
            } else if (enabled) {                         // inset 1px 1px 2px rgba(0,0,0,.08)
                g2.setColor(new Color(0, 0, 0, 22));
                g2.drawLine(x + 1, y + 1, x + w - 2, y + 1);
                g2.drawLine(x + 1, y + 1, x + 1, y + h - 2);
                g2.setColor(new Color(0, 0, 0, 9));
                g2.drawLine(x + 2, y + 2, x + w - 3, y + 2);
                g2.drawLine(x + 2, y + 2, x + 2, y + h - 3);
            }
            g2.dispose();
        }
    }

    /** Frame of list views, tables, scroll panes (#9CB0C4). */
    public static class ScrollBorder extends AbstractBorder implements UIResource {
        @Override public Insets getBorderInsets(Component c, Insets i) { i.set(1, 1, 1, 1); return i; }
        @Override public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            g.setColor(c.isEnabled() ? new Color(0x9CB0C4) : Chk.FIELD_OFF);
            g.drawRect(x, y, w - 1, h - 1);
        }
    }

    /** Menu / combo popup / tooltip frame: 1px #9BAAB9 with 2px padding. */
    public static class PopupBorder extends AbstractBorder implements UIResource {
        private final Insets in; private final Color line;
        public PopupBorder() { this(new Insets(3, 3, 3, 3), new Color(0x9BAAB9)); }
        public PopupBorder(Insets in, Color line) { this.in = in; this.line = line; }
        @Override public Insets getBorderInsets(Component c, Insets i) { i.set(in.top, in.left, in.bottom, in.right); return i; }
        @Override public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            g.setColor(line);
            g.drawRect(x, y, w - 1, h - 1);
        }
    }

    /** Group box: 1px #C5CDD7, 3px radius. */
    public static class GroupBorder extends AbstractBorder implements UIResource {
        @Override public Insets getBorderInsets(Component c, Insets i) { i.set(6, 8, 6, 8); return i; }
        @Override public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = Chk.g2(g, c);
            g2.setColor(Chk.PANEL_LINE);
            g2.draw(Chk.rr(x + .5, y + .5, w - 1, h - 1, 6));
            g2.dispose();
        }
    }

    /** Titled border whose caption honours the anti-aliasing switches. */
    public static final class Titled extends TitledBorder implements UIResource {
        Titled(TitledBorder t) {
            super(t.getBorder(), t.getTitle(), t.getTitleJustification(), t.getTitlePosition(), t.getTitleFont(), t.getTitleColor());
        }
        @Override public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = Chk.text(Chk.g2(g, c), c);
            super.paintBorder(c, g2, x, y, w, h);
            g2.dispose();
        }
    }

    /** Toolbar: flat; 1px #C4CBD5 rule underneath. No grip (matches the mock-up). */
    public static class ToolBarBorder extends AbstractBorder implements UIResource, SwingConstants {
        @Override public Insets getBorderInsets(Component c, Insets i) {
            boolean horiz = !(c instanceof JToolBar tb) || tb.getOrientation() == HORIZONTAL;
            if (horiz) i.set(2, 6, 3, 6); else i.set(6, 2, 6, 3);
            return i;
        }
        @Override public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            g.setColor(new Color(0xC4CBD5));
            g.drawLine(x, y + h - 1, x + w - 1, y + h - 1);
        }
    }

    /** Frame of a JInternalFrame: 1px #5A738E, 3px sizing band, 1px #95A7BD edge around the client area. */
    public static class InternalFrameBorder extends AbstractBorder implements UIResource {
        public static final int BAND = 4;
        @Override public Insets getBorderInsets(Component c, Insets i) { i.set(1, BAND, BAND, BAND); return i; }
        @Override public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = Chk.g2(g, c);
            boolean active = !(c instanceof JInternalFrame f) || f.isSelected() || Boolean.TRUE.equals(f.getClientProperty("Chk.active"));
            Shape outer = Chk.rr(x, y, w, h, 8);
            g2.setPaint(new GradientPaint(0, y, active ? new Color(0xD8E3F2) : new Color(0xE3E9F0), 0, y + h, active ? new Color(0xC5D5EB) : new Color(0xD5DEE9)));
            g2.fill(outer);
            g2.setColor(Chk.FRAME_LINE);
            g2.draw(Chk.rr(x + .5, y + .5, w - 1, h - 1, 8));
            g2.setColor(new Color(0x95A7BD));
            g2.drawRect(x + BAND - 1, y + 22, w - 2 * BAND + 1, h - 22 - BAND + 1);
            g2.dispose();
        }
    }

    /** Bottom command bar (#ECEFF3 with 1px #C4CBD4 top line); use for button panels. */
    public static class CommandBarBorder extends AbstractBorder implements UIResource {
        private final Insets in;
        public CommandBarBorder() { this(new Insets(7, 10, 7, 10)); }
        public CommandBarBorder(Insets in) { this.in = in; }
        @Override public Insets getBorderInsets(Component c, Insets i) { i.set(in.top, in.left, in.bottom, in.right); return i; }
        @Override public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            g.setColor(Chk.BAR_BG); g.fillRect(x, y, w, h);
            g.setColor(new Color(0xC4CBD4)); g.drawLine(x, y, x + w - 1, y);
        }
    }

    /** One sunken compartment of the status bar (3D inset like the classic Win32 look). */
    public static class StatusPaneBorder extends AbstractBorder implements UIResource {
        @Override public Insets getBorderInsets(Component c, Insets i) { i.set(2, 7, 2, 7); return i; }
        @Override public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            g.setColor(new Color(0x9AA3AC)); g.drawLine(x, y, x + w - 1, y); g.drawLine(x, y, x, y + h - 1);
            g.setColor(Color.WHITE); g.drawLine(x + w - 1, y, x + w - 1, y + h - 1); g.drawLine(x, y + h - 1, x + w - 1, y + h - 1);
        }
    }

    /** Coloured frame for result banners (blue match / red mismatch). */
    public static class AccentBorder extends AbstractBorder implements UIResource {
        private final Color color; private final int thick;
        public AccentBorder(Color color, int thick) { this.color = color; this.thick = thick; }
        @Override public Insets getBorderInsets(Component c, Insets i) { i.set(thick + 3, thick + 7, thick + 3, thick + 7); return i; }
        @Override public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = Chk.g2(g, c);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(thick));
            g2.draw(Chk.rr(x + thick / 2.0, y + thick / 2.0, w - thick, h - thick, 6));
            g2.dispose();
        }
    }
}
