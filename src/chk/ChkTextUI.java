package chk;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;
import javax.swing.text.JTextComponent;

/** Text components: bordered fields repaint on hover/focus; every text component honours the AA switches. */
public final class ChkTextUI {
    private ChkTextUI() {}
    private static final String KEY = "Chk.textHandler";

    private static void hook(JTextComponent c) {
        MouseAdapter h = new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { c.putClientProperty("Chk.hover", Boolean.TRUE); c.repaint(); }
            @Override public void mouseExited(MouseEvent e) { c.putClientProperty("Chk.hover", Boolean.FALSE); c.repaint(); }
        };
        FocusAdapter f = new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) { c.repaint(); }
            @Override public void focusLost(FocusEvent e) { c.repaint(); }
        };
        c.addMouseListener(h); c.addFocusListener(f);
        c.putClientProperty(KEY, new Object[]{h, f});
    }
    private static void unhook(JTextComponent c) {
        Object[] o = (Object[]) c.getClientProperty(KEY);
        if (o != null) { c.removeMouseListener((MouseListener) o[0]); c.removeFocusListener((FocusListener) o[1]); }
    }
    private static Graphics2D aa(Graphics g, JComponent c) { return Chk.text(Chk.g2(g, c), c); }

    public static class TextField extends BasicTextFieldUI {
        public static ComponentUI createUI(JComponent c) { return new TextField(); }
        @Override protected void installListeners() { super.installListeners(); hook(getComponent()); }
        @Override protected void uninstallListeners() { unhook(getComponent()); super.uninstallListeners(); }
        @Override protected void paintSafely(Graphics g) { Graphics2D g2 = aa(g, getComponent()); super.paintSafely(g2); g2.dispose(); }
    }
    public static class PasswordField extends BasicPasswordFieldUI {
        public static ComponentUI createUI(JComponent c) { return new PasswordField(); }
        @Override protected void installListeners() { super.installListeners(); hook(getComponent()); }
        @Override protected void uninstallListeners() { unhook(getComponent()); super.uninstallListeners(); }
        @Override protected void paintSafely(Graphics g) { Graphics2D g2 = aa(g, getComponent()); super.paintSafely(g2); g2.dispose(); }
    }
    public static class FormattedTextField extends BasicFormattedTextFieldUI {
        public static ComponentUI createUI(JComponent c) { return new FormattedTextField(); }
        @Override protected void installListeners() { super.installListeners(); hook(getComponent()); }
        @Override protected void uninstallListeners() { unhook(getComponent()); super.uninstallListeners(); }
        @Override protected void paintSafely(Graphics g) { Graphics2D g2 = aa(g, getComponent()); super.paintSafely(g2); g2.dispose(); }
    }
    public static class TextArea extends BasicTextAreaUI {
        public static ComponentUI createUI(JComponent c) { return new TextArea(); }
        @Override protected void paintSafely(Graphics g) { Graphics2D g2 = aa(g, getComponent()); super.paintSafely(g2); g2.dispose(); }
    }
    public static class EditorPane extends BasicEditorPaneUI {
        public static ComponentUI createUI(JComponent c) { return new EditorPane(); }
        @Override protected void paintSafely(Graphics g) { Graphics2D g2 = aa(g, getComponent()); super.paintSafely(g2); g2.dispose(); }
    }
    public static class TextPane extends BasicTextPaneUI {
        public static ComponentUI createUI(JComponent c) { return new TextPane(); }
        @Override protected void paintSafely(Graphics g) { Graphics2D g2 = aa(g, getComponent()); super.paintSafely(g2); g2.dispose(); }
    }
}
