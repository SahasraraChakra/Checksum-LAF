package chk;

import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;

/** JMenuBar, JMenu, JMenuItem, JCheckBoxMenuItem, JRadioButtonMenuItem, JPopupMenu and its separator. */
public final class ChkMenus {
    private ChkMenus() {}

    static boolean top(JMenuItem mi) { return mi.getParent() instanceof JMenuBar; }

    static void paintItem(Graphics g, JMenuItem mi) {
        ButtonModel m = mi.getModel();
        int w = mi.getWidth(), h = mi.getHeight();
        boolean open = mi instanceof JMenu && m.isSelected();
        boolean armed = m.isArmed() || open;
        Chk.text((Graphics2D) g, mi);
        Graphics2D g2 = Chk.g2(g, mi);
        if (top(mi)) {
            if (armed || m.isRollover()) {                        // #E5EFF9 + #79BAE6, 2px radius
                g2.setColor(new Color(0xE5EFF9)); g2.fill(Chk.rr(0, 0, w, h, 4));
                g2.setColor(new Color(0x79BAE6)); g2.draw(Chk.rr(.5, .5, w - 1, h - 1, 4));
            }
        } else if (armed && mi.isEnabled()) {                      // solid #2D7BE5, white text
            g2.setColor(new Color(0x2D7BE5)); g2.fillRect(0, 0, w, h);
        }
        g2.dispose();
    }

    public static class MenuBar extends BasicMenuBarUI {
        public static ComponentUI createUI(JComponent c) { return new MenuBar(); }
        @Override public void update(Graphics g, JComponent c) {
            g.setColor(Chk.WINDOW_BG); g.fillRect(0, 0, c.getWidth(), c.getHeight());
            g.setColor(new Color(0xD5DBE3)); g.drawLine(0, c.getHeight() - 1, c.getWidth(), c.getHeight() - 1);
        }
    }

    public static class Menu extends BasicMenuUI {
        public static ComponentUI createUI(JComponent c) { return new Menu(); }
        @Override protected void installDefaults() { super.installDefaults(); menuItem.setOpaque(false); }
        @Override protected void paintBackground(Graphics g, JMenuItem mi, Color bg) { paintItem(g, mi); }
        @Override protected void paintMenuItem(Graphics g, JComponent c, Icon ci, Icon ai, Color bg, Color fg, int gap) {
            selectionForeground = top((JMenuItem) c) ? Chk.TEXT : Color.WHITE;
            super.paintMenuItem(g, c, ci, ai, bg, fg, gap);
        }
    }

    public static class MenuItem extends BasicMenuItemUI {
        public static ComponentUI createUI(JComponent c) { return new MenuItem(); }
        @Override protected void installDefaults() { super.installDefaults(); menuItem.setOpaque(false); }
        @Override protected void paintBackground(Graphics g, JMenuItem mi, Color bg) { paintItem(g, mi); }
        @Override protected void paintMenuItem(Graphics g, JComponent c, Icon ci, Icon ai, Color bg, Color fg, int gap) {
            selectionForeground = top((JMenuItem) c) ? Chk.TEXT : Color.WHITE;
            super.paintMenuItem(g, c, ci, ai, bg, fg, gap);
        }
    }

    public static class CheckBoxMenuItem extends BasicCheckBoxMenuItemUI {
        public static ComponentUI createUI(JComponent c) { return new CheckBoxMenuItem(); }
        @Override protected void installDefaults() { super.installDefaults(); menuItem.setOpaque(false); }
        @Override protected void paintBackground(Graphics g, JMenuItem mi, Color bg) { paintItem(g, mi); }
        @Override protected void paintMenuItem(Graphics g, JComponent c, Icon ci, Icon ai, Color bg, Color fg, int gap) {
            selectionForeground = top((JMenuItem) c) ? Chk.TEXT : Color.WHITE;
            super.paintMenuItem(g, c, ci, ai, bg, fg, gap);
        }
    }

    public static class RadioButtonMenuItem extends BasicRadioButtonMenuItemUI {
        public static ComponentUI createUI(JComponent c) { return new RadioButtonMenuItem(); }
        @Override protected void installDefaults() { super.installDefaults(); menuItem.setOpaque(false); }
        @Override protected void paintBackground(Graphics g, JMenuItem mi, Color bg) { paintItem(g, mi); }
        @Override protected void paintMenuItem(Graphics g, JComponent c, Icon ci, Icon ai, Color bg, Color fg, int gap) {
            selectionForeground = top((JMenuItem) c) ? Chk.TEXT : Color.WHITE;
            super.paintMenuItem(g, c, ci, ai, bg, fg, gap);
        }
    }

    public static class PopupMenu extends BasicPopupMenuUI {
        public static ComponentUI createUI(JComponent c) { return new PopupMenu(); }
        @Override public void update(Graphics g, JComponent c) {
            g.setColor(new Color(0xF4F6F9)); g.fillRect(0, 0, c.getWidth(), c.getHeight());
            paint(g, c);
        }
    }

    public static class PopupSeparator extends BasicPopupMenuSeparatorUI {
        public static ComponentUI createUI(JComponent c) { return new PopupSeparator(); }
        @Override public void paint(Graphics g, JComponent c) {
            g.setColor(Chk.SEPARATOR); g.drawLine(2, 2, c.getWidth() - 3, 2);
        }
        @Override public Dimension getPreferredSize(JComponent c) { return new Dimension(0, 5); }
    }
}
