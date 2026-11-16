package chk;

import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.UIResource;

/** List / combo cell: flat #DDEEFE selection (blue text) for lists, #CCE8FF in combo popups; 20 px rows. */
public class ChkListCellRenderer extends DefaultListCellRenderer {
    private Color selFill;

    public static class UI extends ChkListCellRenderer implements javax.swing.plaf.UIResource { }

    public ChkListCellRenderer() { setOpaque(false); setBorder(BorderFactory.createEmptyBorder(2, 6, 2, 6)); }

    @Override public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean focus) {
        super.getListCellRendererComponent(list, value, index, false, false);
        boolean combo = "ComboBox.list".equals(list.getName());
        selFill = isSelected && index >= 0 ? (combo ? Chk.SELECTION_COMBO : Chk.SELECTION) : null;
        setOpaque(false);
        setForeground(!list.isEnabled() ? Chk.TEXT_OFF : selFill != null && !combo ? Chk.TEXT_SEL : Chk.TEXT);
        return this;
    }

    @Override public Dimension getPreferredSize() {
        Dimension d = super.getPreferredSize();
        d.height = Math.max(d.height, 20);
        return d;
    }

    @Override protected void paintComponent(Graphics g) {
        if (selFill != null) { g.setColor(selFill); g.fillRect(0, 0, getWidth(), getHeight()); }
        super.paintComponent(g);
    }
}
