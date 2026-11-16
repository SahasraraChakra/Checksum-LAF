package chk;

import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.plaf.UIResource;

/** Every icon is drawn with Java2D; nothing needs image files. */
public final class ChkIcons {
    private ChkIcons() {}

    private abstract static class VIcon implements Icon, UIResource {
        final int w, h;
        VIcon(int w, int h) { this.w = w; this.h = h; }
        public int getIconWidth() { return w; }
        public int getIconHeight() { return h; }
        public final void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = Chk.g2(g, c);
            g2.translate(x, y);
            draw(c, g2);
            g2.dispose();
        }
        abstract void draw(Component c, Graphics2D g);
    }

    private static boolean[] state(Component c) {   // enabled, hot, pressed, selected
        if (c instanceof AbstractButton b) {
            ButtonModel m = b.getModel();
            return new boolean[]{b.isEnabled(), m.isRollover(), m.isArmed() && m.isPressed(), m.isSelected()};
        }
        return new boolean[]{c == null || c.isEnabled(), false, false, false};
    }

    private static final Color BOX = new Color(0x6B7280), BLUE = new Color(0x0055EA);

    // ---------------------------------------------------------------- check box / radio (12 px, Tailwind-forms look)
    public static Icon checkBox() {
        return new VIcon(14, 14) {
            void draw(Component c, Graphics2D g) {
                boolean[] s = state(c);
                boolean mixed = c instanceof JComponent jc && Boolean.TRUE.equals(jc.getClientProperty("Chk.indeterminate"));
                boolean on = s[3] || mixed;
                Color fill = !s[0] ? (on ? new Color(0x9DB6DD) : new Color(0xE5E8EC)) : on ? BLUE : (s[2] ? new Color(0xDCE8F8) : Color.WHITE);
                Color line = !s[0] ? new Color(0xB4BAC2) : on ? BLUE : (s[1] || s[2]) ? BLUE : BOX;
                if (s[0] && s[1] && !s[2]) { g.setColor(Chk.alpha(new Color(0x2563EB), 60)); g.fill(new Rectangle2D.Float(0, 0, 14, 14)); }
                g.setColor(fill); g.fillRect(1, 1, 12, 12);
                g.setColor(line); g.drawRect(1, 1, 11, 11);
                g.setColor(Color.WHITE);
                if (mixed) { g.fillRect(4, 6, 6, 2); }
                else if (s[3]) {
                    Path2D p = new Path2D.Float();
                    p.moveTo(3.8, 7.2); p.lineTo(6, 9.4); p.lineTo(10.4, 4.4);
                    g.setStroke(new BasicStroke(1.7f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    g.draw(p);
                }
            }
        };
    }

    public static Icon radio() {
        return new VIcon(14, 14) {
            void draw(Component c, Graphics2D g) {
                boolean[] s = state(c);
                Color fill = !s[0] ? (s[3] ? new Color(0x9DB6DD) : new Color(0xE5E8EC)) : s[3] ? BLUE : (s[2] ? new Color(0xDCE8F8) : Color.WHITE);
                Color line = !s[0] ? new Color(0xB4BAC2) : s[3] || s[1] || s[2] ? BLUE : BOX;
                if (s[0] && s[1] && !s[2]) { g.setColor(Chk.alpha(new Color(0x2563EB), 60)); g.fill(new Ellipse2D.Float(0, 0, 14, 14)); }
                Ellipse2D e = new Ellipse2D.Float(1, 1, 12, 12);
                g.setColor(fill); g.fill(e);
                g.setColor(line); g.draw(new Ellipse2D.Float(1.5f, 1.5f, 11, 11));
                if (s[3]) { g.setColor(Color.WHITE); g.fill(new Ellipse2D.Float(5, 5, 4, 4)); }
            }
        };
    }

    // ---------------------------------------------------------------- menus
    public static Icon menuCheck() {
        return new VIcon(14, 14) {
            void draw(Component c, Graphics2D g) {
                if (!(c instanceof AbstractButton b)) return;
                boolean armed = b.getModel().isArmed();
                g.setColor(b.isSelected() ? (b.isEnabled() ? BLUE : new Color(0x9DB6DD)) : Color.WHITE);
                g.fillRect(1, 1, 11, 11);
                g.setColor(armed ? Color.WHITE : (b.isSelected() ? BLUE : BOX)); g.drawRect(1, 1, 10, 10);
                if (b.isSelected()) {
                    Path2D p = new Path2D.Float();
                    p.moveTo(3.4, 6.6); p.lineTo(5.3, 8.5); p.lineTo(9.2, 4);
                    g.setColor(Color.WHITE); g.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND)); g.draw(p);
                }
            }
        };
    }

    public static Icon menuRadio() {
        return new VIcon(14, 14) {
            void draw(Component c, Graphics2D g) {
                if (!(c instanceof AbstractButton b)) return;
                boolean armed = b.getModel().isArmed();
                g.setColor(b.isSelected() ? (b.isEnabled() ? BLUE : new Color(0x9DB6DD)) : Color.WHITE);
                g.fill(new Ellipse2D.Float(1, 1, 11, 11));
                g.setColor(armed ? Color.WHITE : (b.isSelected() ? BLUE : BOX)); g.draw(new Ellipse2D.Float(1.5f, 1.5f, 10, 10));
                if (b.isSelected()) { g.setColor(Color.WHITE); g.fill(new Ellipse2D.Float(4.5f, 4.5f, 4, 4)); }
            }
        };
    }

    public static Icon menuArrow() {
        return new VIcon(8, 10) {
            void draw(Component c, Graphics2D g) {
                boolean on = !(c instanceof AbstractButton b) || b.isEnabled();
                boolean armed = c instanceof AbstractButton b2 && b2.getModel().isArmed();
                Chk.arrow(g, 4, 5, 3.4f, SwingConstants.EAST, !on ? Chk.TEXT_OFF : armed ? Color.WHITE : new Color(0x404B5A));
            }
        };
    }

    public static Icon nothing(int w, int h) { return new VIcon(w, h) { void draw(Component c, Graphics2D g) { } }; }

    // ---------------------------------------------------------------- tree / files
    public static Icon treeNode(boolean expanded) {
        return new VIcon(16, 16) {
            void draw(Component c, Graphics2D g) {
                Path2D p = new Path2D.Float();
                if (expanded) { p.moveTo(4, 6); p.lineTo(12, 6); p.lineTo(8, 11); p.closePath(); g.setColor(new Color(0x4A5568)); g.fill(p); }
                else { p.moveTo(6, 4); p.lineTo(11, 8); p.lineTo(6, 12); p.closePath(); g.setColor(new Color(0x8B95A3)); g.fill(p); }
            }
        };
    }

    public static Icon folder(boolean open) {
        return new VIcon(16, 16) {
            void draw(Component c, Graphics2D g) {
                Path2D back = new Path2D.Float();
                back.moveTo(1.5, 3.5); back.lineTo(6, 3.5); back.lineTo(7.5, 5); back.lineTo(14, 5);
                back.lineTo(14, 13); back.lineTo(1.5, 13); back.closePath();
                g.setPaint(new GradientPaint(0, 3, new Color(0xE9BE4F), 0, 13, new Color(0xD9A726)));
                g.fill(back); g.setColor(new Color(0xB98A12)); g.draw(back);
                Path2D front = new Path2D.Float();
                if (open) { front.moveTo(3, 7); front.lineTo(15.5, 7); front.lineTo(13, 13); front.lineTo(1.5, 13); }
                else { front.moveTo(1.5, 6.5); front.lineTo(14, 6.5); front.lineTo(14, 13); front.lineTo(1.5, 13); }
                front.closePath();
                g.setPaint(new GradientPaint(0, 6, new Color(0xF6D36B), 0, 13, new Color(0xE2B33A)));
                g.fill(front); g.setColor(new Color(0xB98A12)); g.draw(front);
            }
        };
    }

    public static Icon file() {
        return new VIcon(16, 16) {
            void draw(Component c, Graphics2D g) {
                Path2D p = new Path2D.Float();
                p.moveTo(3.5, 1.5); p.lineTo(10, 1.5); p.lineTo(13.5, 5); p.lineTo(13.5, 14.5); p.lineTo(3.5, 14.5); p.closePath();
                g.setPaint(new GradientPaint(0, 1, Color.WHITE, 0, 15, new Color(0xE2E8F0)));
                g.fill(p); g.setColor(new Color(0x8895A8)); g.draw(p);
                g.setColor(new Color(0xC1CAD6)); g.drawLine(10, 1, 10, 5); g.drawLine(10, 5, 13, 5);
                for (int i = 0; i < 3; i++) g.drawLine(5, 7 + i * 2, 11, 7 + i * 2);
            }
        };
    }

    public static Icon computer() {
        return new VIcon(16, 16) {
            void draw(Component c, Graphics2D g) {
                g.setPaint(new GradientPaint(0, 2, new Color(0xDFE6EE), 0, 11, new Color(0xA9B6C6)));
                g.fill(Chk.rr(1.5, 2.5, 13, 9, 2)); g.setColor(new Color(0x5B6B80)); g.draw(Chk.rr(1.5, 2.5, 13, 9, 2));
                g.setPaint(new GradientPaint(0, 3.5f, new Color(0x7EC4F4), 0, 10, new Color(0x2F7FC9))); g.fillRect(3, 4, 10, 6);
                g.setColor(new Color(0x7D8A9B)); g.fillRect(6, 12, 4, 1); g.fillRect(4, 13, 8, 1);
            }
        };
    }

    public static Icon drive(boolean floppy) {
        return new VIcon(16, 16) {
            void draw(Component c, Graphics2D g) {
                if (floppy) {
                    g.setColor(new Color(0x2E4E8C)); g.fill(Chk.rr(2.5, 1.5, 11, 13, 2));
                    g.setColor(new Color(0xE9EEF5)); g.fillRect(5, 2, 6, 4); g.setColor(new Color(0xC9D3E2)); g.fillRect(4, 9, 8, 5);
                } else {
                    g.setPaint(new GradientPaint(0, 4, new Color(0xE9EEF4), 0, 13, new Color(0xA2AFC0)));
                    g.fill(Chk.rr(1.5, 4.5, 13, 8, 2)); g.setColor(new Color(0x5F6F84)); g.draw(Chk.rr(1.5, 4.5, 13, 8, 2));
                    g.setColor(new Color(0x15B63A)); g.fillRect(10, 9, 3, 2);
                }
            }
        };
    }

    public static Icon upFolder() { return new VIcon(18, 16) { void draw(Component c, Graphics2D g) {
        folder(false).paintIcon(c, g, 1, 1);
        Path2D p = new Path2D.Float();
        p.moveTo(11, 11); p.lineTo(11, 7); p.lineTo(8.5, 7); p.lineTo(12, 2.5); p.lineTo(15.5, 7); p.lineTo(13, 7); p.lineTo(13, 11); p.closePath();
        g.setPaint(new GradientPaint(0, 2, new Color(0x4AE668), 0, 11, new Color(0x0CA130))); g.fill(p); g.setColor(new Color(0x088C26)); g.draw(p); } }; }

    public static Icon newFolder() { return new VIcon(18, 16) { void draw(Component c, Graphics2D g) {
        folder(false).paintIcon(c, g, 0, 1);
        g.setColor(new Color(0x0055EA)); g.fillOval(11, 1, 7, 7); g.setColor(Color.WHITE); g.fillRect(14, 2, 1, 5); g.fillRect(12, 4, 5, 1); } }; }

    public static Icon home() { return new VIcon(18, 16) { void draw(Component c, Graphics2D g) {
        Path2D roof = new Path2D.Float(); roof.moveTo(1, 8); roof.lineTo(9, 1.5); roof.lineTo(17, 8); roof.closePath();
        g.setColor(new Color(0xB83838)); g.fill(roof);
        g.setPaint(new GradientPaint(0, 8, new Color(0xF8F1DC), 0, 15, new Color(0xE2CC93))); g.fillRect(3, 8, 12, 7);
        g.setColor(new Color(0x7A5A2A)); g.drawRect(3, 8, 12, 7); g.fillRect(8, 10, 3, 5); } }; }

    public static Icon listView() { return new VIcon(18, 16) { void draw(Component c, Graphics2D g) {
        for (int col = 0; col < 2; col++) for (int r = 0; r < 4; r++) {
            g.setColor(new Color(0x0055EA)); g.fillRect(2 + col * 8, 2 + r * 3, 2, 2);
            g.setColor(new Color(0x7D8A9B)); g.fillRect(5 + col * 8, 2 + r * 3, 4, 2); } } }; }

    public static Icon detailsView() { return new VIcon(18, 16) { void draw(Component c, Graphics2D g) {
        g.setColor(new Color(0xC5D5EB)); g.fillRect(2, 2, 14, 3);
        for (int r = 0; r < 3; r++) { g.setColor(new Color(0x0055EA)); g.fillRect(2, 7 + r * 3, 2, 2); g.setColor(new Color(0x7D8A9B)); g.fillRect(5, 7 + r * 3, 11, 2); } } }; }

    // ---------------------------------------------------------------- option pane
    public enum Message { INFO, WARNING, ERROR, QUESTION }

    public static Icon message(Message kind) {
        return new VIcon(32, 32) {
            void draw(Component c, Graphics2D g) {
                g.setFont(Chk.baseFont().deriveFont(Font.BOLD, 20f));
                Chk.text(g, c);
                if (kind == Message.WARNING) {
                    Path2D t = new Path2D.Float(); t.moveTo(16, 2.5); t.lineTo(30, 28.5); t.lineTo(2, 28.5); t.closePath();
                    g.setPaint(new GradientPaint(0, 3, new Color(0xFFD866), 0, 29, new Color(0xE9A100))); g.fill(t);
                    g.setColor(new Color(0x9A6B00)); g.setStroke(new BasicStroke(1.4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND)); g.draw(t);
                    g.setColor(new Color(0x4A3600)); g.drawString("!", 16 - g.getFontMetrics().stringWidth("!") / 2f, 26);
                    return;
                }
                Color a = kind == Message.ERROR ? new Color(0xF8B4B4) : new Color(0x7FB2F5);
                Color b = kind == Message.ERROR ? new Color(0xC51E1E) : new Color(0x1D4ED8);
                Ellipse2D e = new Ellipse2D.Float(2, 2, 28, 28);
                g.setPaint(new GradientPaint(0, 2, a, 0, 30, b)); g.fill(e);
                g.setColor(Chk.alpha(Color.WHITE, 90)); g.fill(new Ellipse2D.Float(5, 3.5f, 22, 12));
                g.setColor(b.darker()); g.setStroke(new BasicStroke(1.4f)); g.draw(e);
                g.setColor(Color.WHITE);
                if (kind == Message.ERROR) {
                    g.setStroke(new BasicStroke(3.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    g.drawLine(11, 11, 21, 21); g.drawLine(21, 11, 11, 21);
                } else {
                    String s = kind == Message.QUESTION ? "?" : "i";
                    g.drawString(s, 16 - g.getFontMetrics().stringWidth(s) / 2f, 23.5f);
                }
            }
        };
    }

    public static Icon sortArrow(boolean ascending) {
        return new VIcon(9, 6) { void draw(Component c, Graphics2D g) {
            Chk.arrow(g, 4.5f, 3, 3.5f, ascending ? SwingConstants.NORTH : SwingConstants.SOUTH, new Color(0x6B7C93)); } };
    }

    /** Small shield used as the default window icon in the title bar. */
    public static Icon appShield(int size) {
        return new VIcon(size, size) { void draw(Component c, Graphics2D g) {
            float k = size / 16f; g.scale(k, k);
            Path2D p = new Path2D.Float(); p.moveTo(8, 1); p.lineTo(14, 3.5); p.lineTo(14, 8); p.curveTo(14, 11.5, 11, 14, 8, 15); p.curveTo(5, 14, 2, 11.5, 2, 8); p.lineTo(2, 3.5); p.closePath();
            g.setPaint(new GradientPaint(0, 1, new Color(0x4D8EF5), 0, 15, new Color(0x0055EA))); g.fill(p);
            g.setColor(Color.WHITE); g.setStroke(new BasicStroke(1.6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            Path2D t = new Path2D.Float(); t.moveTo(5, 8); t.lineTo(7.2, 10.2); t.lineTo(11, 5.8); g.draw(t); } };
    }
}
