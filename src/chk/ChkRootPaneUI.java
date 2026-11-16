package chk;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.beans.PropertyChangeEvent;
import java.util.List;
import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.BasicRootPaneUI;

/**
 * Window decorations for JFrame / JDialog / JOptionPane, following the mock-up: 1px #5A738E frame with 6px radius,
 * 28px gradient title bar (shield icon, bold #1E395B title), 26x19 caption buttons, flat client area.
 */
public class ChkRootPaneUI extends BasicRootPaneUI {
    static final int TITLE = 28, EDGE = 1, RESIZE = 5, CORNER = 12, ARC = 12;

    public static ComponentUI createUI(JComponent c) { return new ChkRootPaneUI(); }

    private JRootPane root;
    private TitlePane title;
    private LayoutManager oldLayout;
    private Component oldGlass;
    private HierarchyListener hier;

    @Override public void installUI(JComponent c) {
        super.installUI(c);
        root = (JRootPane) c;
        if (root.getWindowDecorationStyle() != JRootPane.NONE) installDecorations();
    }
    @Override public void uninstallUI(JComponent c) { uninstallDecorations(); super.uninstallUI(c); }
    @Override public void propertyChange(PropertyChangeEvent e) {
        super.propertyChange(e);
        if ("windowDecorationStyle".equals(e.getPropertyName())) {
            uninstallDecorations();
            if (root.getWindowDecorationStyle() != JRootPane.NONE) installDecorations();
        }
    }

    // ------------------------------------------------------------------ state helpers
    private Window window() { return SwingUtilities.getWindowAncestor(root); }
    private boolean resizable() {
        Window w = window();
        if (w instanceof Frame f) return f.isResizable();
        if (w instanceof Dialog d) return d.isResizable();
        return true;
    }
    private boolean maximized() { return window() instanceof Frame f && (f.getExtendedState() & Frame.MAXIMIZED_BOTH) == Frame.MAXIMIZED_BOTH; }
    private boolean active() { Window w = window(); return w == null || w.isActive() || w.getFocusOwner() != null; }
    private boolean rounded() {
        Window w = window();
        return !maximized() && (Boolean.TRUE.equals(root.getClientProperty("Chk.forceRounded"))
                || (w != null && w.getBackground() != null && w.getBackground().getAlpha() < 255));
    }
    private static boolean undecorated(Window w) {
        return w instanceof Frame f ? f.isUndecorated() : w instanceof Dialog d ? d.isUndecorated() : true;
    }
    private static boolean translucent() {
        try {
            return !GraphicsEnvironment.isHeadless() && GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
                    .isWindowTranslucencySupported(GraphicsDevice.WindowTranslucency.PERPIXEL_TRANSLUCENT);
        } catch (Throwable t) { return false; }
    }
    private Shape frameShape(int w, int h) { return new RoundRectangle2D.Float(0, 0, w, h, rounded() ? ARC : 0, rounded() ? ARC : 0); }

    // ------------------------------------------------------------------ install
    /** Layered pane: text anti-aliasing for everything inside, and clipping to the rounded frame. */
    private final class ContentPane extends JLayeredPane {
        @Override public void paint(Graphics g) {
            Graphics2D g2 = Chk.text((Graphics2D) g.create(), root);
            if (rounded() && root != null) {
                Shape s = frameShape(root.getWidth(), root.getHeight());
                AffineTransform t = AffineTransform.getTranslateInstance(-getX(), -getY());
                g2.clip(t.createTransformedShape(s));
            }
            super.paint(g2);
            g2.dispose();
        }
    }

    /** Glass pane: sizing zones around the frame, and the outline painted above all content. */
    private final class Glass extends JComponent {
        private int dir; private Point start; private Rectangle bounds;
        Glass() {
            setOpaque(false);
            MouseInputAdapter m = new MouseInputAdapter() {
                @Override public void mouseMoved(MouseEvent e) { setCursor(cursor(hit(e.getX(), e.getY()))); }
                @Override public void mousePressed(MouseEvent e) {
                    dir = hit(e.getX(), e.getY());
                    if (dir != 0 && window() != null) { start = e.getLocationOnScreen(); bounds = window().getBounds(); }
                }
                @Override public void mouseDragged(MouseEvent e) {
                    Window win = window();
                    if (dir == 0 || win == null) return;
                    Point p = e.getLocationOnScreen();
                    int dx = p.x - start.x, dy = p.y - start.y;
                    Rectangle r = new Rectangle(bounds);
                    Dimension min = new Dimension(Math.max(win.getMinimumSize().width, 260), Math.max(win.getMinimumSize().height, 140));
                    if ((dir & 4) != 0) { int nw = Math.max(min.width, bounds.width - dx); r.x = bounds.x + bounds.width - nw; r.width = nw; }
                    if ((dir & 8) != 0) r.width = Math.max(min.width, bounds.width + dx);
                    if ((dir & 1) != 0) { int nh = Math.max(min.height, bounds.height - dy); r.y = bounds.y + bounds.height - nh; r.height = nh; }
                    if ((dir & 2) != 0) r.height = Math.max(min.height, bounds.height + dy);
                    win.setBounds(r); win.validate();
                }
                @Override public void mouseReleased(MouseEvent e) { dir = 0; }
            };
            addMouseListener(m); addMouseMotionListener(m);
        }
        @Override public boolean contains(int x, int y) { return dir != 0 || hit(x, y) != 0; }
        @Override protected void paintComponent(Graphics g) {
            if (title == null) return;
            Graphics2D g2 = Chk.g2(g, root);
            int w = getWidth(), h = getHeight();
            boolean act = active();
            int arc = rounded() ? ARC : 0;
            if (!maximized()) {
                g2.setColor(act ? Chk.FRAME_LINE : new Color(0x8FA0B3));
                g2.draw(new RoundRectangle2D.Float(.5f, .5f, w - 1, h - 1, arc, arc));
                g2.setColor(new Color(255, 255, 255, act ? 150 : 100));
                g2.drawLine(arc / 2 + 1, 1, w - arc / 2 - 2, 1);
            }
            g2.dispose();
        }
    }

    private void installDecorations() {
        if (!(root.getLayeredPane() instanceof ContentPane)) {
            JLayeredPane old = root.getLayeredPane();
            ContentPane np = new ContentPane();
            for (Component c : old.getComponents()) { int layer = old.getLayer(c); old.remove(c); np.add(c, Integer.valueOf(layer)); }
            root.setLayeredPane(np);
        }
        title = new TitlePane();
        root.add(title);
        oldGlass = root.getGlassPane();
        Glass glass = new Glass();
        root.setGlassPane(glass);
        glass.setVisible(true);
        oldLayout = root.getLayout();
        root.setLayout(new RootLayout());
        root.setOpaque(false);
        hier = e -> {
            Window w = window();
            if (w != null && undecorated(w) && translucent()) {
                try { w.setBackground(new Color(0, 0, 0, 0)); } catch (Exception ignored) { }
            }
            if (w != null && title != null) title.attach(w);
        };
        root.addHierarchyListener(hier);
        hier.hierarchyChanged(null);
        root.revalidate(); root.repaint();
    }

    private void uninstallDecorations() {
        if (title == null) return;
        root.removeHierarchyListener(hier);
        root.remove(title);
        if (oldGlass != null) root.setGlassPane(oldGlass);
        root.setOpaque(true);
        if (oldLayout != null) root.setLayout(oldLayout);
        title = null;
        root.revalidate();
    }

    // ------------------------------------------------------------------ layout
    private final class RootLayout implements LayoutManager2 {
        public void addLayoutComponent(Component c, Object o) { }
        public void addLayoutComponent(String n, Component c) { }
        public void removeLayoutComponent(Component c) { }
        public float getLayoutAlignmentX(Container t) { return .5f; }
        public float getLayoutAlignmentY(Container t) { return .5f; }
        public void invalidateLayout(Container t) { }
        public Dimension maximumLayoutSize(Container t) { return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE); }
        private int edge() { return maximized() ? 0 : EDGE; }

        public Dimension preferredLayoutSize(Container p) {
            Dimension cp = root.getContentPane() != null ? root.getContentPane().getPreferredSize() : new Dimension(100, 100);
            Dimension mb = root.getJMenuBar() != null && root.getJMenuBar().isVisible() ? root.getJMenuBar().getPreferredSize() : new Dimension();
            return new Dimension(Math.max(cp.width, mb.width) + 2 * edge(), cp.height + mb.height + TITLE + edge());
        }
        public Dimension minimumLayoutSize(Container p) {
            Dimension cp = root.getContentPane() != null ? root.getContentPane().getMinimumSize() : new Dimension(100, 100);
            Dimension mb = root.getJMenuBar() != null && root.getJMenuBar().isVisible() ? root.getJMenuBar().getMinimumSize() : new Dimension();
            return new Dimension(Math.max(cp.width, mb.width) + 2 * edge() + 100, cp.height + mb.height + TITLE + edge());
        }
        public void layoutContainer(Container p) {
            int w = p.getWidth(), h = p.getHeight(), e = edge();
            if (root.getGlassPane() != null) root.getGlassPane().setBounds(0, 0, w, h);
            int lw = Math.max(0, w - 2 * e), lh = Math.max(0, h - TITLE - e);
            root.getLayeredPane().setBounds(e, TITLE, lw, lh);
            int mh = 0;
            JMenuBar mb = root.getJMenuBar();
            if (mb != null && mb.isVisible()) { mh = mb.getPreferredSize().height; mb.setBounds(0, 0, lw, mh); }
            if (root.getContentPane() != null) root.getContentPane().setBounds(0, mh, lw, Math.max(0, lh - mh));
            if (title != null) title.setBounds(0, 0, w, TITLE);
        }
    }

    // ------------------------------------------------------------------ frame painting
    @Override public void update(Graphics g, JComponent c) { paint(g, c); }

    @Override public void paint(Graphics g, JComponent c) {
        if (title == null) { if (c.isOpaque()) { g.setColor(c.getBackground()); g.fillRect(0, 0, c.getWidth(), c.getHeight()); } return; }
        Graphics2D g2 = Chk.g2(g, c);
        int w = c.getWidth(), h = c.getHeight();
        boolean act = active();
        Area frame = new Area(frameShape(w, h));
        g2.setColor(Chk.WINDOW_BG);
        g2.fill(frame);
        Area bar = new Area(new Rectangle2D.Float(0, 0, w, TITLE));
        bar.intersect(frame);
        Chk.titleBackground(g2, bar, 0, TITLE, act);
        g2.setColor(act ? new Color(0x95A7BD) : new Color(0xB4C0CE));
        g2.drawLine(0, TITLE - 1, w, TITLE - 1);
        g2.dispose();
    }

    // ------------------------------------------------------------------ cursor / hit
    int hit(int x, int y) {
        if (root == null || !resizable() || maximized()) return 0;
        int w = root.getWidth(), h = root.getHeight(), d = 0;
        boolean nearX = x < CORNER || x >= w - CORNER, nearY = y < CORNER || y >= h - CORNER;
        if (y < RESIZE || (x < RESIZE || x >= w - RESIZE) && nearY && y < CORNER) d |= 1;
        if (y >= h - RESIZE || (x < RESIZE || x >= w - RESIZE) && nearY && y >= h - CORNER) d |= 2;
        if (x < RESIZE || (y < RESIZE || y >= h - RESIZE) && nearX && x < CORNER) d |= 4;
        if (x >= w - RESIZE || (y < RESIZE || y >= h - RESIZE) && nearX && x >= w - CORNER) d |= 8;
        return d;
    }

    private static Cursor cursor(int d) {
        return Cursor.getPredefinedCursor(switch (d) {
            case 1 -> Cursor.N_RESIZE_CURSOR; case 2 -> Cursor.S_RESIZE_CURSOR; case 4 -> Cursor.W_RESIZE_CURSOR; case 8 -> Cursor.E_RESIZE_CURSOR;
            case 5 -> Cursor.NW_RESIZE_CURSOR; case 9 -> Cursor.NE_RESIZE_CURSOR; case 6 -> Cursor.SW_RESIZE_CURSOR; case 10 -> Cursor.SE_RESIZE_CURSOR;
            default -> Cursor.DEFAULT_CURSOR; });
    }

    // ------------------------------------------------------------------ title bar
    final class TitlePane extends JComponent {
        private int hover = -1, pressed = -1;                 // 0 min, 1 max, 2 close
        private Window win;
        private Point dragStart; private Rectangle dragBounds; private boolean dragging;
        private final Rectangle[] btn = {new Rectangle(), new Rectangle(), new Rectangle()};

        TitlePane() {
            setOpaque(false);
            MouseInputAdapter m = new MouseInputAdapter() {
                @Override public void mouseMoved(MouseEvent e) { int h = which(e.getPoint()); if (h != hover) { hover = h; repaint(); } }
                @Override public void mouseExited(MouseEvent e) { hover = -1; repaint(); }
                @Override public void mousePressed(MouseEvent e) {
                    if (SwingUtilities.isRightMouseButton(e)) { systemMenu(e); return; }
                    pressed = which(e.getPoint());
                    if (pressed >= 0) { repaint(); return; }
                    if (e.getClickCount() == 2 && win instanceof Frame f && f.isResizable()) { toggleMax(); return; }
                    dragStart = e.getLocationOnScreen(); dragBounds = win != null ? win.getBounds() : null; dragging = true;
                }
                @Override public void mouseDragged(MouseEvent e) {
                    if (!dragging || win == null || dragBounds == null) return;
                    if (maximized()) {
                        Frame f = (Frame) win;
                        double rel = e.getX() / (double) Math.max(1, getWidth());
                        f.setExtendedState(Frame.NORMAL);
                        dragBounds = f.getBounds(); dragStart = e.getLocationOnScreen();
                        dragBounds.x = dragStart.x - (int) (rel * dragBounds.width); dragBounds.y = dragStart.y - 12;
                        f.setLocation(dragBounds.x, dragBounds.y);
                        return;
                    }
                    Point p = e.getLocationOnScreen();
                    win.setLocation(dragBounds.x + p.x - dragStart.x, dragBounds.y + p.y - dragStart.y);
                }
                @Override public void mouseReleased(MouseEvent e) {
                    int r = which(e.getPoint());
                    if (pressed >= 0 && pressed == r) act(r);
                    pressed = -1; dragging = false; repaint();
                }
            };
            addMouseListener(m); addMouseMotionListener(m);
        }

        void attach(Window w) {
            if (win == w) return;
            win = w;
            WindowAdapter wa = new WindowAdapter() {
                @Override public void windowActivated(WindowEvent e) { root.repaint(); }
                @Override public void windowDeactivated(WindowEvent e) { root.repaint(); }
                @Override public void windowStateChanged(WindowEvent e) { root.revalidate(); root.repaint(); }
            };
            w.addWindowListener(wa); w.addWindowStateListener(wa);
            w.addPropertyChangeListener(e -> { if (e.getPropertyName().startsWith("title") || e.getPropertyName().contains("con")) repaint(); });
        }

        private String titleText() {
            if (win instanceof Frame f) return f.getTitle();
            if (win instanceof Dialog d) return d.getTitle();
            Object o = root.getClientProperty("Chk.title");
            return o == null ? "" : o.toString();
        }
        private boolean full() { return root.getWindowDecorationStyle() == JRootPane.FRAME; }
        private int which(Point p) { for (int i = 2; i >= 0; i--) if (btn[i].width > 0 && btn[i].contains(p)) return i; return -1; }

        private void act(int i) {
            if (win == null) return;
            switch (i) {
                case 0 -> { if (win instanceof Frame f) f.setExtendedState(f.getExtendedState() | Frame.ICONIFIED); }
                case 1 -> toggleMax();
                case 2 -> win.dispatchEvent(new WindowEvent(win, WindowEvent.WINDOW_CLOSING));
            }
        }
        private void toggleMax() {
            if (!(win instanceof Frame f) || !f.isResizable()) return;
            int st = f.getExtendedState();
            if ((st & Frame.MAXIMIZED_BOTH) != 0) f.setExtendedState(st & ~Frame.MAXIMIZED_BOTH);
            else { f.setMaximizedBounds(GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds()); f.setExtendedState(st | Frame.MAXIMIZED_BOTH); }
        }
        private void systemMenu(MouseEvent e) {
            JPopupMenu pm = new JPopupMenu();
            boolean fr = win instanceof Frame, max = maximized();
            JMenuItem restore = new JMenuItem("Restore"); restore.setEnabled(fr && max); restore.addActionListener(a -> toggleMax());
            JMenuItem min = new JMenuItem("Minimize"); min.setEnabled(fr && full()); min.addActionListener(a -> act(0));
            JMenuItem mx = new JMenuItem("Maximize"); mx.setEnabled(fr && !max && resizable() && full()); mx.addActionListener(a -> toggleMax());
            JMenuItem close = new JMenuItem("Close"); close.addActionListener(a -> act(2));
            pm.add(restore); pm.add(min); pm.add(mx); pm.addSeparator(); pm.add(close);
            pm.show(this, e.getX(), e.getY());
        }

        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = Chk.g2(g, root);
            boolean act = active(), fr = full();
            int w = getWidth(), bw = 26, bh = 19, y = (TITLE - 1 - bh) / 2, x = w - 4 - bw - (maximized() ? 0 : 1);
            btn[0].setBounds(0, 0, 0, 0); btn[1].setBounds(0, 0, 0, 0);
            btn[2].setBounds(x, y, bw, bh);
            if (fr) { btn[1].setBounds(x - 2 - bw, y, bw, bh); btn[0].setBounds(x - 4 - 2 * bw, y, bw, bh); }
            boolean canMax = win == null || (win instanceof Frame f && f.isResizable());
            for (int i = 0; i < 3; i++) {
                if (btn[i].width == 0) continue;
                Chk.Cap k = i == 0 ? Chk.Cap.MIN : i == 2 ? Chk.Cap.CLOSE : maximized() ? Chk.Cap.RESTORE : Chk.Cap.MAX;
                boolean enabled = !(i == 1 && !canMax);
                Chk.captionButton(g2, btn[i], k, hover == i && enabled, pressed == i && hover == i && enabled, enabled);
            }
            // icon + title
            int tx = 8;
            Image img = null;
            if (win != null) { List<Image> imgs = win.getIconImages(); if (!imgs.isEmpty()) img = imgs.get(0); }
            if (img != null) { g2.drawImage(img.getScaledInstance(15, 15, Image.SCALE_SMOOTH), tx, (TITLE - 1 - 15) / 2, null); tx += 21; }
            else if (Boolean.TRUE.equals(root.getClientProperty("Chk.shieldIcon"))) { ChkIcons.appShield(15).paintIcon(this, g2, tx, (TITLE - 1 - 15) / 2); tx += 21; }
            String t = titleText();
            if (t != null && !t.isEmpty()) {
                Chk.text(g2, root);
                g2.setFont(Chk.baseFont().deriveFont(Font.BOLD));
                FontMetrics fm = g2.getFontMetrics();
                int avail = (fr ? btn[0].x : btn[2].x) - tx - 10;
                String s = t;
                if (fm.stringWidth(s) > avail) { while (s.length() > 1 && fm.stringWidth(s + "\u2026") > avail) s = s.substring(0, s.length() - 1); s += "\u2026"; }
                g2.setColor(act ? Chk.TEXT_TITLE : new Color(0x7B8DA5));
                g2.drawString(s, tx, (TITLE - 1 + fm.getAscent() - fm.getDescent()) / 2);
            }
            g2.dispose();
        }
    }
}
