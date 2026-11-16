package chk;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;

/** Selects look like the mock-up inputs: white field, #84A1BF frame, chevron on the right. Editable combos get a glossy arrow cell. */
public class ChkComboBoxUI extends BasicComboBoxUI {
    public static ComponentUI createUI(JComponent c) { return new ChkComboBoxUI(); }
    private MouseAdapter hover;

    @Override protected void installDefaults() { super.installDefaults(); comboBox.setOpaque(false); }

    @Override protected void installListeners() {
        super.installListeners();
        hover = new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { comboBox.putClientProperty("Chk.hover", Boolean.TRUE); comboBox.repaint(); }
            @Override public void mouseExited(MouseEvent e) { comboBox.putClientProperty("Chk.hover", Boolean.FALSE); comboBox.repaint(); }
        };
        comboBox.addMouseListener(hover);
    }

    @Override protected void uninstallListeners() { comboBox.removeMouseListener(hover); super.uninstallListeners(); }

    @Override protected void installComponents() {
        super.installComponents();
        if (arrowButton != null && hover != null) arrowButton.addMouseListener(hover);
    }

    @Override protected JButton createArrowButton() {
        JButton b = new JButton() {
            @Override public Dimension getPreferredSize() { return new Dimension(19, 19); }
            @Override public void paint(Graphics g) {
                Graphics2D g2 = Chk.g2(g, comboBox);
                boolean en = comboBox.isEnabled();
                int w = getWidth(), h = getHeight();
                Color ink = en ? new Color(0x4A5568) : Chk.TEXT_OFF;
                if (comboBox.isEditable()) {
                    boolean hot = Boolean.TRUE.equals(comboBox.getClientProperty("Chk.hover"));
                    Chk.Skin s = Chk.skin(en, hot, comboBox.isPopupVisible());
                    g2.setPaint(new GradientPaint(0, 1, s.t1(), 0, h - 1, s.b2()));
                    g2.fillRect(0, 1, w - 1, h - 2);
                    g2.setColor(Chk.FIELD_BORDER); g2.drawLine(0, 1, 0, h - 2);
                    Chk.arrow(g2, w / 2f, h / 2f, 3.3f, SwingConstants.SOUTH, ink);
                } else {
                    g2.setColor(ink);
                    g2.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    float cx = w / 2f + 1, cy = h / 2f;
                    g2.draw(new java.awt.geom.Path2D.Float() {{ moveTo(cx - 3.2, cy - 1.6); lineTo(cx, cy + 1.6); lineTo(cx + 3.2, cy - 1.6); }});
                }
                g2.dispose();
            }
        };
        b.setFocusable(false); b.setOpaque(false); b.setContentAreaFilled(false); b.setBorderPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder());
        b.setName("ComboBox.arrowButton");
        return b;
    }

    @Override public void paint(Graphics g, JComponent c) {
        Graphics2D g2 = Chk.g2(g, c);
        int w = c.getWidth(), h = c.getHeight();
        boolean en = c.isEnabled();
        boolean hot = Boolean.TRUE.equals(comboBox.getClientProperty("Chk.hover")) && en;
        g2.setColor(!en ? Chk.WINDOW_BG : (hot && !comboBox.isEditable()) ? new Color(0xE9F4FD) : Color.WHITE);
        g2.fillRect(0, 0, w, h);
        new ChkBorders.TextBorder().paintBorder(c, g2, 0, 0, w, h);
        if (!comboBox.isEditable() && comboBox.hasFocus() && !comboBox.isPopupVisible()) Chk.dottedRect(g2, 4, 4, w - 22, h - 8);
        g2.dispose();
        if (!comboBox.isEditable()) paintCurrentValue(g, rectangleForCurrentValue(), false);
    }

    @Override public void paintCurrentValue(Graphics g, Rectangle bounds, boolean hasFocus) {
        ListCellRenderer<Object> r = comboBox.getRenderer();
        Component c = r.getListCellRendererComponent(listBox, comboBox.getSelectedItem(), -1, false, false);
        c.setFont(comboBox.getFont());
        c.setForeground(comboBox.isEnabled() ? comboBox.getForeground() : Chk.TEXT_OFF);
        if (c instanceof JComponent jc) jc.setOpaque(false);
        Graphics2D tg = Chk.text((Graphics2D) g.create(), comboBox);
        currentValuePane.paintComponent(tg, c, comboBox, bounds.x, bounds.y, bounds.width, bounds.height, c instanceof JPanel);
        tg.dispose();
    }

    @Override public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) { }
    @Override protected ListCellRenderer<Object> createRenderer() { return new ChkListCellRenderer.UI(); }

    @Override protected ComboPopup createPopup() {
        return new BasicComboPopup(comboBox) {
            @Override protected void configurePopup() {
                super.configurePopup();
                setBorder(new ChkBorders.PopupBorder(new Insets(1, 1, 1, 1), Chk.FIELD_BORDER));
            }
        };
    }

    @Override protected Insets getInsets() { return new Insets(1, 2, 1, 1); }
}
