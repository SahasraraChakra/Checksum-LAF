package chk;

import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.BasicDesktopIconUI;

/** Minimised JInternalFrame (JDesktopIcon): just the small title bar with its caption buttons. */
public class ChkDesktopIconUI extends BasicDesktopIconUI {
    public static ComponentUI createUI(JComponent c) { return new ChkDesktopIconUI(); }
    @Override protected void installComponents() {
        frame = desktopIcon.getInternalFrame();
        iconPane = new ChkInternalFrameUI.Title(frame);
        desktopIcon.setLayout(new BorderLayout());
        desktopIcon.add(iconPane, BorderLayout.CENTER);
    }
    @Override public Dimension getPreferredSize(JComponent c) { return new Dimension(180, 26); }
    @Override public Dimension getMinimumSize(JComponent c) { return getPreferredSize(c); }
}
