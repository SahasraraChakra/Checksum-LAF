package chk;

import java.awt.*;
import java.awt.geom.*;
import java.io.File;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.SwingConstants;

/** Palette (taken from tab1.html / tab2.html), gradients, anti-aliasing switches and fonts. */
public final class Chk {
    private Chk() {}

    /** Four-stop glossy fill (top half t1->t2, bottom half b1->b2) plus outline. */
    public record Skin(Color t1, Color t2, Color b1, Color b2, Color border) {}

    private static Color c(int v) { return new Color(v); }
    public static Color alpha(Color c, int a) { return new Color(c.getRed(), c.getGreen(), c.getBlue(), a); }

    // ------------------------------------------------------------------ colours
    public static final Color WINDOW_BG   = c(0xF0F2F5);
    public static final Color CLIENT_BG   = c(0xFFFFFF);
    public static final Color BAR_BG      = c(0xECEFF3);          // command bar / status bar
    public static final Color FRAME_LINE  = c(0x5A738E);
    public static final Color TEXT        = c(0x1A202C);
    public static final Color TEXT_BTN    = c(0x1E293B);
    public static final Color TEXT_LABEL  = c(0x334155);
    public static final Color TEXT_DIM    = c(0x64748B);
    public static final Color TEXT_OFF    = c(0x9AA3AD);
    public static final Color TEXT_TITLE  = c(0x1E395B);
    public static final Color TEXT_GROUP  = c(0x0E489E);
    public static final Color TEXT_LINK   = c(0x0055EA);
    public static final Color TEXT_SEL    = c(0x003380);
    public static final Color FIELD_BORDER = c(0x84A1BF);
    public static final Color FIELD_FOCUS  = c(0x2563EB);
    public static final Color FIELD_OFF    = c(0xC5CDD7);
    public static final Color SELECTION    = c(0xDDEEFE);
    public static final Color SELECTION_COMBO = c(0xCCE8FF);
    public static final Color ZEBRA        = c(0xFAFCFE);
    public static final Color GRID         = c(0xF0F2F4);
    public static final Color SEPARATOR    = c(0xD0D6DE);
    public static final Color PANEL_LINE   = c(0xC5CDD7);          // group box
    public static final Color TAB_LINE     = c(0x7F9DB9);

    // ------------------------------------------------------------------ skins
    public static final Skin NORMAL   = new Skin(c(0xF8F9FA), c(0xECEEF1), c(0xDFE2E6), c(0xD3D7DD), c(0x8B98A6));
    public static final Skin HOT      = new Skin(c(0xECF6FD), c(0xDAEFFC), c(0xBEE5FC), c(0xA9DBF7), c(0x3886BC));
    public static final Skin PRESSED  = new Skin(c(0xCDE9FB), c(0xBFE5FC), c(0xA1D8F8), c(0x87C6F1), c(0x276896));
    public static final Skin DISABLED = new Skin(c(0xF4F5F7), c(0xF4F5F7), c(0xF4F5F7), c(0xF4F5F7), c(0xC5CDD7));
    public static final Skin PRIMARY  = new Skin(c(0x2A79E8), c(0x155DC6), c(0x094CB2), c(0x08439E), c(0x06347A));
    public static final Skin PRIMARY_HOT = new Skin(c(0x3D89F3), c(0x206CD9), c(0x1057C2), c(0x0F4EB0), c(0x063070));
    public static final Skin TOOL_HOT     = new Skin(c(0xF2F7FD), c(0xE6F1FB), c(0xD8EBF9), c(0xD8EBF9), c(0x72B8E5));
    public static final Skin TOOL_PRESSED = new Skin(c(0xD3E8F8), c(0xCCE3F6), c(0xC0DDF3), c(0xC0DDF3), c(0x72B8E5));
    public static final Skin GREEN    = new Skin(c(0x4DE469), c(0x17B83D), c(0x0CA130), c(0x16BA3C), c(0x088C26));
    // caption buttons
    public static final Skin CAP      = new Skin(c(0xFFFFFF), c(0xE3E9F2), c(0xCBD6E6), c(0xE1E8F3), c(0x7B8B9E));
    public static final Skin CAP_HOT  = new Skin(c(0xFFFFFF), c(0xF5F8FC), c(0xE1ECF9), c(0xEEF4FD), c(0x557AA9));
    public static final Skin CAP_DOWN = new Skin(c(0xDCEBF8), c(0xCFE3F5), c(0xB5D2EC), c(0xC4DBF1), c(0x3F6592));
    public static final Skin CLOSE    = new Skin(c(0xFDE2E2), c(0xF8B4B4), c(0xEE7878), c(0xF6A3A3), c(0xB87272));
    public static final Skin CLOSE_HOT  = new Skin(c(0xFDBABA), c(0xF77676), c(0xE83232), c(0xF46C6C), c(0xB87272));
    public static final Skin CLOSE_DOWN = new Skin(c(0xF29A9A), c(0xE05A5A), c(0xC51E1E), c(0xD94A4A), c(0x8F4A4A));

    public static Skin skin(boolean enabled, boolean hot, boolean pressed) {
        if (!enabled) return DISABLED;
        if (pressed) return PRESSED;
        return hot ? HOT : NORMAL;
    }

    // ------------------------------------------------------------------ anti-aliasing switches
    /** Global switches. Defaults: on. System properties -Dchk.aa=false, -Dchk.aa.shapes=false, -Dchk.aa.text=false. */
    private static volatile boolean aaShapes = bool("chk.aa.shapes", bool("chk.aa", true));
    private static volatile boolean aaText   = bool("chk.aa.text",   bool("chk.aa", true));
    private static boolean bool(String k, boolean d) {
        String v = System.getProperty(k);
        return v == null ? d : Boolean.parseBoolean(v);
    }
    public static void setAntialiasing(boolean shapes, boolean text) { aaShapes = shapes; aaText = text; }
    public static boolean isShapeAntialiasing() { return aaShapes; }
    public static boolean isTextAntialiasing() { return aaText; }

    /** Per-component override: component.putClientProperty("Chk.aa", Boolean.FALSE) turns it off for that component. */
    public static boolean shapesOn(Component c) { return override(c, aaShapes); }
    public static boolean textOn(Component c) { return override(c, aaText); }
    private static boolean override(Component c, boolean def) {
        if (c instanceof JComponent jc) {
            Object o = jc.getClientProperty("Chk.aa");
            if (o instanceof Boolean b) return b;
        }
        return def;
    }

    /** Copy of g with shape anti-aliasing applied (unless switched off). */
    public static Graphics2D g2(Graphics g) { return g2(g, null); }
    public static Graphics2D g2(Graphics g, Component c) {
        Graphics2D g2 = (Graphics2D) g.create();
        if (shapesOn(c)) {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        }
        return g2;
    }

    /** Applies desktop text anti-aliasing (ClearType where available) unless switched off. */
    public static Graphics2D text(Graphics2D g) { return text(g, null); }
    public static Graphics2D text(Graphics2D g, Component c) {
        if (!textOn(c)) return g;
        Object hints = Toolkit.getDefaultToolkit().getDesktopProperty("awt.font.desktophints");
        if (hints instanceof Map<?, ?> m) {
            for (Map.Entry<?, ?> e : m.entrySet()) g.setRenderingHint((RenderingHints.Key) e.getKey(), e.getValue());
        } else {
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        }
        return g;
    }

    // ------------------------------------------------------------------ painting helpers
    public static RoundRectangle2D rr(double x, double y, double w, double h, double arc) {
        return new RoundRectangle2D.Double(x, y, w, h, arc, arc);
    }

    public static void glossy(Graphics2D g, float x, float y, float w, float h, float arc, Skin s, boolean leftToRight) {
        glossy(g, x, y, w, h, arc, s, leftToRight, true);
    }

    /** Win-style button: 2-tone gradient, 1px outline and a 1px white top highlight (inset 0 1px 0). */
    public static void glossy(Graphics2D g, float x, float y, float w, float h, float arc, Skin s,
                              boolean leftToRight, boolean topHighlight) {
        RoundRectangle2D shape = new RoundRectangle2D.Float(x + .5f, y + .5f, w - 1, h - 1, arc, arc);
        Area a1 = new Area(shape), a2 = new Area(shape);
        if (!leftToRight) {
            float mid = y + h / 2f;
            a1.intersect(new Area(new Rectangle2D.Float(x, y, w, mid - y)));
            a2.intersect(new Area(new Rectangle2D.Float(x, mid, w, y + h - mid)));
            g.setPaint(new GradientPaint(0, y, s.t1, 0, mid, s.t2)); g.fill(a1);
            g.setPaint(new GradientPaint(0, mid, s.b1, 0, y + h, s.b2)); g.fill(a2);
        } else {
            float mid = x + w / 2f;
            a1.intersect(new Area(new Rectangle2D.Float(x, y, mid - x, h)));
            a2.intersect(new Area(new Rectangle2D.Float(mid, y, x + w - mid, h)));
            g.setPaint(new GradientPaint(x, 0, s.t1, mid, 0, s.t2)); g.fill(a1);
            g.setPaint(new GradientPaint(mid, 0, s.b1, x + w, 0, s.b2)); g.fill(a2);
        }
        if (topHighlight && w > 6 && h > 6) {
            g.setColor(new Color(255, 255, 255, 215));
            g.fill(new Rectangle2D.Float(x + 1.5f, y + 1f, w - 3f, 1f));
        }
        if (s.border != null) { g.setColor(s.border); g.draw(shape); }
    }

    public static void arrow(Graphics2D g, float cx, float cy, float size, int dir, Color col) {
        Path2D p = new Path2D.Float();
        float h = size;
        switch (dir) {
            case SwingConstants.NORTH -> { p.moveTo(cx - size, cy + h / 2); p.lineTo(cx + size, cy + h / 2); p.lineTo(cx, cy - h / 2); }
            case SwingConstants.SOUTH -> { p.moveTo(cx - size, cy - h / 2); p.lineTo(cx + size, cy - h / 2); p.lineTo(cx, cy + h / 2); }
            case SwingConstants.WEST  -> { p.moveTo(cx + h / 2, cy - size); p.lineTo(cx + h / 2, cy + size); p.lineTo(cx - h / 2, cy); }
            default                   -> { p.moveTo(cx - h / 2, cy - size); p.lineTo(cx - h / 2, cy + size); p.lineTo(cx + h / 2, cy); }
        }
        p.closePath();
        g.setColor(col);
        g.fill(p);
    }

    public static void dottedRect(Graphics2D g, int x, int y, int w, int h) {
        Graphics2D d = (Graphics2D) g.create();
        d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        d.setColor(new Color(0x404040));
        d.setStroke(new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1f, new float[]{1f, 1f}, 0f));
        d.drawRect(x, y, w - 1, h - 1);
        d.dispose();
    }

    // ------------------------------------------------------------------ title-bar painting shared by windows and internal frames
    public enum Cap { MIN, MAX, RESTORE, CLOSE }

    /** 4-stop title bar gradient from the mock-up: #EBF1F8 / #D8E3F2 / #C5D5EB / #D6E3F4. */
    public static void titleBackground(Graphics2D g, Shape s, float y, float h, boolean active) {
        Color a = active ? c(0xEBF1F8) : c(0xF1F4F8), b = active ? c(0xD8E3F2) : c(0xE3E9F0);
        Color d = active ? c(0xC5D5EB) : c(0xD5DEE9), e = active ? c(0xD6E3F4) : c(0xDDE5EF);
        g.setPaint(new LinearGradientPaint(0, y, 0, y + h, new float[]{0f, .48f, .5f, 1f}, new Color[]{a, b, d, e}));
        g.fill(s);
    }

    /** One caption button (min / max / close) with the mock-up gradients; glyphs are drawn as vectors. */
    public static void captionButton(Graphics2D g, Rectangle r, Cap kind, boolean hot, boolean down, boolean enabled) {
        boolean close = kind == Cap.CLOSE;
        Skin s = close ? (down ? CLOSE_DOWN : hot ? CLOSE_HOT : CLOSE) : (down ? CAP_DOWN : hot ? CAP_HOT : CAP);
        if (!enabled) s = DISABLED;
        glossy(g, r.x, r.y, r.width, r.height, 4, s, false, false);
        float cx = r.x + r.width / 2f, cy = r.y + r.height / 2f;
        float k = r.height / 19f;                       // scale glyphs with the button
        Color col = !enabled ? c(0xA8B0BA) : close ? (hot || down ? Color.WHITE : c(0x4C0000)) : c(0x333333);
        g.setColor(col);
        g.setStroke(new BasicStroke(1.4f * Math.max(.8f, k), BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
        switch (kind) {
            case MIN -> g.fill(new Rectangle2D.Float(cx - 3.5f * k, cy + 2f * k, 7f * k, 2f * k));
            case MAX -> g.draw(new Rectangle2D.Float(cx - 3.5f * k, cy - 3f * k, 7f * k, 6f * k));
            case RESTORE -> {
                g.draw(new Rectangle2D.Float(cx - 1.5f * k, cy - 3.5f * k, 5f * k, 4.5f * k));
                g.draw(new Rectangle2D.Float(cx - 3.5f * k, cy - 1.2f * k, 5f * k, 4.5f * k));
            }
            case CLOSE -> {
                g.setStroke(new BasicStroke(1.5f * Math.max(.8f, k), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g.draw(new Line2D.Float(cx - 3.2f * k, cy - 3.2f * k, cx + 3.2f * k, cy + 3.2f * k));
                g.draw(new Line2D.Float(cx + 3.2f * k, cy - 3.2f * k, cx - 3.2f * k, cy + 3.2f * k));
            }
        }
    }

    // ------------------------------------------------------------------ fonts
    private static Font base, mono;
    private static boolean fontsRegistered;

    public static float fontSize() { return Float.parseFloat(System.getProperty("chk.fontSize", "11.5")); }

    /** Register every .ttf/.otf in ./fonts (or -Dchk.fontDir) and the bundled Inter fallback. */
    private static synchronized void registerFonts() {
        if (fontsRegistered) return;
        fontsRegistered = true;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        File dir = new File(System.getProperty("chk.fontDir", "fonts"));
        File[] fs = dir.listFiles((d, n) -> n.toLowerCase().endsWith(".ttf") || n.toLowerCase().endsWith(".otf"));
        if (fs != null) for (File f : fs) { try { ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, f)); } catch (Exception ignored) { } }
        for (String n : new String[]{"Inter-Regular", "Inter-Bold", "Inter-Italic", "Inter-BoldItalic"}) {
            try (java.io.InputStream in = Chk.class.getResourceAsStream("/chk/fonts/" + n + ".otf")) {
                if (in != null) ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, in));
            } catch (Exception ignored) { }
        }
    }

    private static String pick(String[] wanted, String fallback) {
        Set<String> have = new HashSet<>(java.util.List.of(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()));
        for (String f : wanted) if (have.contains(f)) return f;
        return fallback;
    }

    /** Product Sans, then Open Sans (as in the mock-up), then sensible fallbacks. */
    public static synchronized Font baseFont() {
        if (base == null) {
            registerFonts();
            String fam = pick(new String[]{"Product Sans", "Open Sans", "Segoe UI Variable Text", "Segoe UI", "Inter", "Arial"}, Font.SANS_SERIF);
            base = new Font(fam, Font.PLAIN, 12).deriveFont(fontSize());
        }
        return base;
    }

    /** JetBrains Mono for digests, paths and numbers; Consolas / DejaVu Sans Mono fallback. */
    public static synchronized Font monoFont() {
        if (mono == null) {
            registerFonts();
            String fam = pick(new String[]{"JetBrains Mono", "Cascadia Mono", "Consolas", "DejaVu Sans Mono", "Courier New"}, Font.MONOSPACED);
            mono = new Font(fam, Font.PLAIN, 12).deriveFont(11f);
        }
        return mono;
    }
}
