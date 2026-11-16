package chk;

import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.basic.BasicGraphicsUtils;

/**
 * JButton / JToggleButton. Looks (picked automatically):
 * push button, toolbar button (parent is a JToolBar), primary ("Chk.style"="primary"),
 * caption button ("Chk.style"="caption", with "Chk.cap" = Chk.Cap) used by internal frames.
 */
public class ChkButtonUI extends BasicButtonUI {
    public static ComponentUI createUI(JComponent c) { return new ChkButtonUI(); }

    @Override protected void installDefaults(AbstractButton b) {
        super.installDefaults(b);
        b.setOpaque(false);
        b.setRolloverEnabled(true);
    }

    @Override public void update(Graphics g, JComponent c) { paint(g, c); }

    @Override public void paint(Graphics g, JComponent c) {
        AbstractButton b = (AbstractButton) c;
        if (b.isContentAreaFilled() || b.isBorderPainted() || "caption".equals(b.getClientProperty("Chk.style"))) paintBackground(g, b);
        super.paint(g, c);
    }

    private static boolean isToolbar(AbstractButton b) {
        return b.getParent() instanceof JToolBar || "toolbar".equals(b.getClientProperty("Chk.style"));
    }
    private static boolean isPrimary(AbstractButton b) { return "primary".equals(b.getClientProperty("Chk.style")); }

    protected void paintBackground(Graphics g, AbstractButton b) {
        ButtonModel m = b.getModel();
        int w = b.getWidth(), h = b.getHeight();
        boolean enabled = b.isEnabled();
        boolean hot = enabled && m.isRollover();
        boolean pressed = enabled && m.isArmed() && m.isPressed();
        boolean selected = m.isSelected();
        Object style = b.getClientProperty("Chk.style");
        Graphics2D g2 = Chk.g2(g, b);
        try {
            if ("caption".equals(style)) {
                Chk.Cap k = b.getClientProperty("Chk.cap") instanceof Chk.Cap cc ? cc : Chk.Cap.CLOSE;
                Chk.captionButton(g2, new Rectangle(0, 0, w, h), k, hot, pressed, enabled);
            } else if (isToolbar(b)) {
                if (!(hot || pressed || selected)) return;
                Chk.glossy(g2, 0, 0, w, h, 4, (pressed || selected) ? Chk.TOOL_PRESSED : Chk.TOOL_HOT, false);
            } else if (isPrimary(b)) {
                Chk.Skin s = !enabled ? Chk.DISABLED : pressed ? new Chk.Skin(new Color(0x0A4BB0), new Color(0x083F96), new Color(0x063381), new Color(0x07388B), Chk.PRIMARY.border())
                        : hot ? Chk.PRIMARY_HOT : Chk.PRIMARY;
                Chk.glossy(g2, 0, 0, w, h, 4, s, false);
            } else {
                Chk.Skin s = Chk.skin(enabled, hot, pressed || (selected && b instanceof JToggleButton));
                Chk.glossy(g2, 0, 0, w, h, 4, s, false);
                if (enabled && !pressed && b instanceof JButton jb && jb.isDefaultButton()) {
                    g2.setColor(new Color(0x8FCAF0));
                    g2.draw(Chk.rr(1.5, 1.5, w - 3, h - 3, 3));
                    g2.setColor(new Color(0x3886BC));
                    g2.draw(Chk.rr(.5, .5, w - 1, h - 1, 4));
                } else if (selected && enabled && b instanceof JToggleButton && !pressed) {
                    g2.setColor(new Color(0x8FCAF0));
                    g2.draw(Chk.rr(1.5, 1.5, w - 3, h - 3, 3));
                }
            }
        } finally { g2.dispose(); }
    }

    @Override protected void paintText(Graphics g, AbstractButton b, Rectangle tr, String text) {
        if ("caption".equals(b.getClientProperty("Chk.style"))) return;
        Graphics2D g2 = Chk.text((Graphics2D) g.create(), b);
        FontMetrics fm = g2.getFontMetrics();
        boolean primary = isPrimary(b) && b.isEnabled();
        if (primary) {
            g2.setColor(new Color(0, 0, 0, 110));
            BasicGraphicsUtils.drawStringUnderlineCharAt(g2, text, b.getDisplayedMnemonicIndex(), tr.x, tr.y + fm.getAscent() + 1);
        }
        g2.setColor(!b.isEnabled() ? Chk.TEXT_OFF : primary ? Color.WHITE : b.getForeground());
        BasicGraphicsUtils.drawStringUnderlineCharAt(g2, text, b.getDisplayedMnemonicIndex(), tr.x, tr.y + fm.getAscent());
        g2.dispose();
    }

    @Override protected void paintFocus(Graphics g, AbstractButton b, Rectangle viewRect, Rectangle textRect, Rectangle iconRect) {
        if (isToolbar(b) || b.getClientProperty("Chk.style") != null) return;
        Chk.dottedRect((Graphics2D) g, 3, 3, b.getWidth() - 6, b.getHeight() - 6);
    }

    @Override protected void paintButtonPressed(Graphics g, AbstractButton b) { }
}
