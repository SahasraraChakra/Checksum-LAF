package chk;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;

/** Headless renderer used to produce the preview PNGs (no display needed). */
public class Screenshot {
    static void layoutAll(Component c) {
        if (c instanceof Container k) {
            k.doLayout();
            for (Component ch : k.getComponents()) layoutAll(ch);
            if (c instanceof JSplitPane sp) sp.doLayout();
        }
    }

    static JRootPane window(JComponent content, JMenuBar mb, String title, int w, int h, int style) {
        JRootPane r = new JRootPane();
        r.setWindowDecorationStyle(style);
        r.putClientProperty("Chk.title", title); r.putClientProperty("Chk.shieldIcon", Boolean.TRUE);
        r.putClientProperty("Chk.forceRounded", Boolean.TRUE);
        if (mb != null) r.setJMenuBar(mb);
        r.setContentPane(content);
        r.setSize(w, h);
        for (int i = 0; i < 3; i++) layoutAll(r);
        return r;
    }

    static BufferedImage render(JRootPane r, Component... overlayPopups) { return null; }

    static void save(BufferedImage img, String name) throws Exception {
        ImageIO.write(img, "png", new File(name));
        System.out.println("wrote " + name);
    }

    static BufferedImage desk(int w, int h) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setPaint(new GradientPaint(0, 0, new Color(0x1B5E9E), w, h, new Color(0x0B2A55)));
        g.fillRect(0, 0, w, h);
        g.setPaint(new RadialGradientPaint(new Point2D.Float(w * .7f, h * .2f), w * .6f, new float[]{0, 1}, new Color[]{new Color(120, 200, 255, 140), new Color(0, 0, 0, 0)}));
        g.fillRect(0, 0, w, h);
        g.dispose();
        return img;
    }

    static void paintOn(Graphics2D g, Component c, int x, int y) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.translate(x, y);
        c.paint(g2);
        g2.dispose();
    }

    static BufferedImage compose(JRootPane r, int margin, Object... popups) {
        int w = r.getWidth() + margin * 2, h = r.getHeight() + margin * 2;
        BufferedImage img = desk(w, h);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        for (int i = 8; i >= 1; i--) {   // soft shadow
            g.setColor(new Color(0, 0, 0, 14));
            g.fill(new RoundRectangle2D.Float(margin - i + 2, margin - i + 6, r.getWidth() + i * 2, r.getHeight() + i * 2, 16 + i, 16 + i));
        }
        paintOn(g, r, margin, margin);
        for (int i = 0; i + 2 < popups.length; i += 3) {
            JComponent p = (JComponent) popups[i];
            Point loc = SwingUtilities.convertPoint((Component) popups[i + 1], 0, 0, r);
            Point off = (Point) popups[i + 2];
            p.setSize(p.getPreferredSize());
            for (int k = 0; k < 3; k++) layoutAll(p);
            paintOn(g, p, margin + loc.x + off.x, margin + loc.y + off.y);
        }
        g.dispose();
        return img;
    }

    public static void main(String[] a) throws Exception {
        ChecksumLookAndFeel.install();
        RepaintManager.currentManager(null).setDoubleBufferingEnabled(false);
        String out = a.length > 0 ? a[0] : ".";

        // ---- 1. controls tab (combo popup + tooltip open)
        JPanel content = new JPanel(new BorderLayout());
        content.add(ChkDemo.toolBar(), BorderLayout.NORTH);
        JTabbedPane tabs = ChkDemo.tabs();
        content.add(tabs, BorderLayout.CENTER);
        content.add(ChkDemo.statusBar(), BorderLayout.SOUTH);
        JMenuBar mb = ChkDemo.menuBar();
        JRootPane r = window(content, mb, "Checksum Look & Feel \u2013 Component Gallery", 1100, 720, JRootPane.FRAME);
        simulate(tabs);
        JComboBox<?> cb = (JComboBox<?>) find(tabs, "combo");
        JPopupMenu cpop = (JPopupMenu) cb.getUI().getAccessibleChild(cb, 0);
        cpop.setPreferredSize(new Dimension(cb.getWidth(), 3 * 20 + 4));
        ((JList<?>) ((JScrollPane) cpop.getComponent(0)).getViewport().getView()).setSelectedIndex(1);
        JLabel tip = (JLabel) find(tabs, "tip");
        JToolTip tt = tip.createToolTip(); tt.setTipText("<html><b>JToolTip</b><br>Checksum-style tooltip</html>");
        BufferedImage img = compose(r, 40, cpop, cb, new Point(0, cb.getHeight() - 1), tt, tip, new Point(20, tip.getHeight() + 2));
        save(img, out + "/chk_controls.png");

        // ---- 1b. menu
        JMenuBar mb2 = ChkDemo.menuBar();
        JRootPane rm = window(content, mb2, "Checksum Look & Feel \u2013 Component Gallery", 1100, 720, JRootPane.FRAME);
        JMenu file = mb2.getMenu(0);
        file.getModel().setSelected(true);
        JPopupMenu pm = file.getPopupMenu();
        pm.setSize(pm.getPreferredSize());
        ((JMenuItem) pm.getComponent(3)).getModel().setArmed(true);
        save(compose(rm, 40, pm, file, new Point(0, file.getHeight())), out + "/chk_menu.png");

        // ---- 2..4 other tabs
        for (int i = 1; i <= 4; i++) {
            tabs.setSelectedIndex(i);
            JRootPane r2 = window(content, ChkDemo.menuBar(), "Checksum Look & Feel \u2013 Component Gallery", 1100, 720, JRootPane.FRAME);
            layoutAll(r2);
            save(compose(r2, 40), out + "/chk_tab" + i + ".png");
        }

        // ---- 5. dialogs
        JOptionPane op = new JOptionPane("Could not open the file.\nCheck that it exists and try again.", JOptionPane.ERROR_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{"OK", "Details\u2026"});
        JRootPane d = window(op, null, "Error", 400, 170, JRootPane.ERROR_DIALOG);
        op.setSize(d.getWidth() - 12, d.getHeight() - 36); 
        for (int i = 0; i < 3; i++) layoutAll(d);
        save(compose(d, 30), out + "/chk_dialog.png");
    }

    static Component find(Container c, String name) {
        for (Component k : c.getComponents()) {
            if (name.equals(k.getName())) return k;
            if (k instanceof Container kc) { Component f = find(kc, name); if (f != null) return f; }
        }
        return null;
    }

    private static void simulate(Container root) {
        for (Component c : root.getComponents()) {
            if (c instanceof AbstractButton b && b.getName() != null) {
                switch (b.getName()) {
                    case "default" -> { JRootPane rp = null; }
                    case "hot" -> b.getModel().setRollover(true);
                    case "pressed" -> { b.getModel().setRollover(true); b.getModel().setArmed(true); b.getModel().setPressed(true); }
                    case "hotcheck" -> b.getModel().setRollover(true);
                }
            }
            if (c instanceof Container k) simulate(k);
        }
    }
}
