package chk;

import java.awt.*;
import java.util.Date;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;
import javax.swing.table.*;

/** 20px rows, #F0F2F4 grid, #FAFCFE zebra, #DDEEFE selection with #003380 text. */
public class ChkTableUI extends BasicTableUI {
    public static ComponentUI createUI(JComponent c) { return new ChkTableUI(); }

    @Override protected void installDefaults() {
        super.installDefaults();
        int rh = UIManager.getInt("Table.rowHeight");
        if (rh > 0 && table.getRowHeight() < rh) table.setRowHeight(rh);
        table.setIntercellSpacing(new Dimension(1, 1));
        table.setShowGrid(true);
        table.setGridColor(Chk.GRID);
        for (Class<?> k : new Class<?>[]{Object.class, Number.class, Float.class, Double.class, Date.class}) {
            TableCellRenderer r = table.getDefaultRenderer(k);
            if (r instanceof UIResource) table.setDefaultRenderer(k, new Zebra(Number.class.isAssignableFrom(k)));
        }
    }

    @Override public void paint(Graphics g, JComponent c) {
        Graphics2D t = Chk.text((Graphics2D) g.create(), c);
        super.paint(t, c);
        t.dispose();
    }

    static final class Zebra extends DefaultTableCellRenderer.UIResource {
        private final boolean right;
        Zebra(boolean right) { this.right = right; }
        @Override public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foc, int r, int c) {
            super.getTableCellRendererComponent(t, v, sel, false, r, c);
            if (right) setHorizontalAlignment(RIGHT);
            if (sel) { setBackground(Chk.SELECTION); setForeground(Chk.TEXT_SEL); }
            else { setBackground(r % 2 == 1 ? Chk.ZEBRA : Color.WHITE); setForeground(t.isEnabled() ? Chk.TEXT : Chk.TEXT_OFF); }
            setBorder(BorderFactory.createEmptyBorder(1, 6, 1, 6));
            return this;
        }
    }
}
