package chk;

import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;

/** Multi-line text components: same stock behaviour, but text is always anti-aliased. */
public final class ChkTextAreaUIs {
    private ChkTextAreaUIs() {}

    public static class TextArea extends BasicTextAreaUI {
        public static ComponentUI createUI(JComponent c) { return new TextArea(); }
        @Override protected void paintSafely(Graphics g) {
            Graphics2D g2 = Chk.text((Graphics2D) g.create()); super.paintSafely(g2); g2.dispose();
        }
    }
    public static class EditorPane extends BasicEditorPaneUI {
        public static ComponentUI createUI(JComponent c) { return new EditorPane(); }
        @Override protected void paintSafely(Graphics g) {
            Graphics2D g2 = Chk.text((Graphics2D) g.create()); super.paintSafely(g2); g2.dispose();
        }
    }
    public static class TextPane extends BasicTextPaneUI {
        public static ComponentUI createUI(JComponent c) { return new TextPane(); }
        @Override protected void paintSafely(Graphics g) {
            Graphics2D g2 = Chk.text((Graphics2D) g.create()); super.paintSafely(g2); g2.dispose();
        }
    }
}
