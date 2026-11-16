package chk;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;

public class ChkSpinnerUI extends BasicSpinnerUI {
    public static ComponentUI createUI(JComponent c) { return new ChkSpinnerUI(); }

    @Override protected void installDefaults() {
        super.installDefaults();
        fixEditor();
        spinner.addPropertyChangeListener("editor", e -> fixEditor());
    }

    private void fixEditor() {
        if (spinner.getEditor() instanceof JSpinner.DefaultEditor de) {
            JFormattedTextField f = de.getTextField();
            f.setBorder(new EmptyBorder(2, 3, 2, 3));
            f.addFocusListener(new FocusAdapter() {
                @Override public void focusGained(FocusEvent e) { spinner.repaint(); }
                @Override public void focusLost(FocusEvent e) { spinner.repaint(); }
            });
            MouseAdapter m = new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) { spinner.putClientProperty("Chk.hover", true); spinner.repaint(); }
                @Override public void mouseExited(MouseEvent e) { spinner.putClientProperty("Chk.hover", false); spinner.repaint(); }
            };
            f.addMouseListener(m); spinner.addMouseListener(m);
        }
    }

    private JButton button(int dir, String name) {
        JButton b = new JButton() {
            @Override public Dimension getPreferredSize() { return new Dimension(16, 10); }
            @Override public void paint(Graphics g) {
                Graphics2D g2 = Chk.g2(g, spinner);
                ButtonModel m = getModel();
                Chk.Skin s = Chk.skin(spinner.isEnabled(), m.isRollover(), m.isArmed() && m.isPressed());
                g2.setPaint(new GradientPaint(0, 0, s.t1(), 0, getHeight(), s.b2())); g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(s.border()); g2.drawLine(0, 0, 0, getHeight()); if (dir == SwingConstants.NORTH) g2.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
                Chk.arrow(g2, getWidth() / 2f + .5f, getHeight() / 2f, 2.7f, dir, spinner.isEnabled() ? new Color(0x4A5568) : Chk.TEXT_OFF);
                g2.dispose();
            }
        };
        b.setName(name); b.setFocusable(false); b.setBorder(BorderFactory.createEmptyBorder()); b.setRolloverEnabled(true);
        return b;
    }

    @Override protected Component createNextButton() { JComponent c = button(SwingConstants.NORTH, "Spinner.nextButton"); installNextButtonListeners(c); return c; }
    @Override protected Component createPreviousButton() { JComponent c = button(SwingConstants.SOUTH, "Spinner.previousButton"); installPreviousButtonListeners(c); return c; }
}
