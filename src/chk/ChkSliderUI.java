package chk;

import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;

public class ChkSliderUI extends BasicSliderUI {
    public ChkSliderUI(JSlider s) { super(s); }
    public static ComponentUI createUI(JComponent c) { return new ChkSliderUI((JSlider) c); }
    private Point hotPoint = new Point(-10, -10);

    @Override protected Dimension getThumbSize() {
        return slider.getOrientation() == JSlider.HORIZONTAL ? new Dimension(11, 19) : new Dimension(19, 11);
    }
    @Override public void paintFocus(Graphics g) { }

    @Override public void paintTrack(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        Rectangle t = trackRect;
        boolean hz = slider.getOrientation() == JSlider.HORIZONTAL;
        int th = 5;
        Rectangle r = hz ? new Rectangle(t.x, t.y + (t.height - th) / 2, t.width, th) : new Rectangle(t.x + (t.width - th) / 2, t.y, th, t.height);
        g2.setColor(new Color(0xE2E6EA)); g2.fillRect(r.x, r.y, r.width, r.height);
        g2.setColor(new Color(0xB4BAC2)); g2.drawRect(r.x, r.y, r.width - 1, r.height - 1);
        g2.setColor(new Color(0, 0, 0, 30)); if (hz) g2.drawLine(r.x + 1, r.y + 1, r.x + r.width - 2, r.y + 1); else g2.drawLine(r.x + 1, r.y + 1, r.x + 1, r.y + r.height - 2);
        g2.dispose();
    }

    @Override public void paintThumb(Graphics g) {
        Graphics2D g2 = Chk.g2(g, slider);
        Rectangle r = thumbRect;
        Chk.Skin s = !slider.isEnabled() ? Chk.DISABLED : slider.getValueIsAdjusting() ? Chk.PRESSED : r.contains(hotPoint) ? Chk.HOT : Chk.NORMAL;
        Chk.glossy(g2, r.x, r.y, r.width, r.height, 4, s, slider.getOrientation() != JSlider.HORIZONTAL);
        g2.dispose();
    }

    @Override protected TrackListener createTrackListener(JSlider s) {
        return new TrackListener() {
            @Override public void mouseMoved(java.awt.event.MouseEvent e) { hotPoint = e.getPoint(); slider.repaint(thumbRect); }
            @Override public void mouseExited(java.awt.event.MouseEvent e) { hotPoint = new Point(-10, -10); slider.repaint(thumbRect); }
        };
    }

    @Override public void paintTicks(Graphics g) { g.setColor(new Color(0x8D939B)); super.paintTicks(g); }
    @Override public void paintLabels(Graphics g) { Graphics2D t = Chk.text((Graphics2D) g.create(), slider); super.paintLabels(t); t.dispose(); }
}
