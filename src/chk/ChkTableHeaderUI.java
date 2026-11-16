package chk;

import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;
import javax.swing.table.*;

/** Header from the mock-up: #FFF -> #EAEEF3 -> #DBE1E7 gradient, #A4B6C9 base line, #BAC7D5 column dividers, bold 11px. */
public class ChkTableHeaderUI extends BasicTableHeaderUI {
    public static ComponentUI createUI(JComponent c) { return new ChkTableHeaderUI(); }

    @Override protected void installDefaults() {
        super.installDefaults();
        header.setDefaultRenderer(new HeaderRenderer());
        header.setOpaque(false);
    }

    static void gradient(Graphics2D g, int w, int h) {
        g.setPaint(new LinearGradientPaint(0, 0, 0, h, new float[]{0f, .5f, 1f}, new Color[]{Color.WHITE, new Color(0xEAEEF3), new Color(0xDBE1E7)}));
        g.fillRect(0, 0, w, h);
    }

    @Override public void paint(Graphics g, JComponent c) {
        gradient((Graphics2D) g, c.getWidth(), c.getHeight());
        g.setColor(new Color(0xA4B6C9)); g.drawLine(0, c.getHeight() - 1, c.getWidth(), c.getHeight() - 1);
        super.paint(g, c);
    }

    static final class HeaderRenderer extends DefaultTableCellRenderer implements UIResource {
        HeaderRenderer() { setOpaque(false); setHorizontalAlignment(LEADING); setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 6)); }
        @Override public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int col) {
            super.getTableCellRendererComponent(t, v, false, false, r, col);
            setForeground(new Color(0x222222));
            setFont((t != null ? t.getTableHeader().getFont() : getFont()).deriveFont(Font.BOLD, 11f));
            Icon icon = null;
            if (t != null && t.getRowSorter() != null && !t.getRowSorter().getSortKeys().isEmpty()) {
                RowSorter.SortKey k = t.getRowSorter().getSortKeys().get(0);
                if (k.getColumn() == t.convertColumnIndexToModel(col) && k.getSortOrder() != SortOrder.UNSORTED)
                    icon = ChkIcons.sortArrow(k.getSortOrder() == SortOrder.ASCENDING);
            }
            setIcon(icon); setHorizontalTextPosition(LEADING);
            return this;
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            gradient(g2, getWidth(), getHeight());
            g2.setColor(new Color(0xA4B6C9)); g2.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
            g2.setColor(new Color(0xBAC7D5)); g2.drawLine(getWidth() - 1, 0, getWidth() - 1, getHeight() - 2);
            super.paintComponent(Chk.text((Graphics2D) g.create(), this));
        }
        @Override public Dimension getPreferredSize() { Dimension d = super.getPreferredSize(); d.height = Math.max(d.height, 22); return d; }
    }
}
