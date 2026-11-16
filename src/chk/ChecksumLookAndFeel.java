package chk;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.BasicLookAndFeel;

/**
 * "Checksum" look and feel: the light Windows Vista/7 style of the hash-checker mock-ups.
 *
 * <pre>
 *   ChecksumLookAndFeel.install();                       // before creating any window
 *   ChecksumLookAndFeel.setAntialiasing(true, true);     // shapes, text (both default to true)
 *   -Dchk.aa=false                                       // or switch off from the command line
 *   button.putClientProperty("Chk.aa", Boolean.FALSE);   // or per component
 * </pre>
 * Fonts: Product Sans, else Open Sans (UI) and JetBrains Mono (code). Drop .ttf/.otf files into ./fonts to register them.
 */
public class ChecksumLookAndFeel extends BasicLookAndFeel {

    public static void install() {
        if (Chk.isTextAntialiasing()) {
            System.setProperty("swing.aatext", "true");
            System.setProperty("awt.useSystemAAFontSettings", "on");
        }
        try { UIManager.setLookAndFeel(new ChecksumLookAndFeel()); } catch (UnsupportedLookAndFeelException e) { throw new RuntimeException(e); }
        JFrame.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);
    }

    /** Switch anti-aliasing of shapes and of text, globally. Components can override with the "Chk.aa" client property. */
    public static void setAntialiasing(boolean shapes, boolean text) { Chk.setAntialiasing(shapes, text); }

    @Override public String getName() { return "Checksum"; }
    @Override public String getID() { return "Checksum"; }
    @Override public String getDescription() { return "Hash-checker (Windows Vista/7) look and feel"; }
    @Override public boolean isNativeLookAndFeel() { return false; }
    @Override public boolean isSupportedLookAndFeel() { return true; }
    @Override public boolean getSupportsWindowDecorations() { return true; }

    @Override public UIDefaults getDefaults() {
        UIDefaults d = super.getDefaults();
        if (Chk.isTextAntialiasing()) {
            try {
                UIDefaults m = new javax.swing.plaf.metal.MetalLookAndFeel().getDefaults();
                for (Object k : m.keySet())
                    if ("AATextInfoPropertyKey".equals(String.valueOf(k)) && m.get(k) != null) { d.put(k, m.get(k)); break; }
            } catch (Throwable ignored) { }
        }
        return d;
    }

    @Override protected void initClassDefaults(UIDefaults t) {
        super.initClassDefaults(t);
        String p = "chk.";
        t.putDefaults(new Object[]{
            "LabelUI", p + "ChkLabelUI", "PanelUI", p + "ChkPanelUI",
            "ButtonUI", p + "ChkButtonUI", "ToggleButtonUI", p + "ChkButtonUI",
            "CheckBoxUI", p + "ChkCheckUIs$CheckBox", "RadioButtonUI", p + "ChkCheckUIs$RadioButton",
            "ComboBoxUI", p + "ChkComboBoxUI", "TabbedPaneUI", p + "ChkTabbedPaneUI",
            "TextFieldUI", p + "ChkTextUI$TextField", "PasswordFieldUI", p + "ChkTextUI$PasswordField",
            "FormattedTextFieldUI", p + "ChkTextUI$FormattedTextField", "TextAreaUI", p + "ChkTextUI$TextArea",
            "EditorPaneUI", p + "ChkTextUI$EditorPane", "TextPaneUI", p + "ChkTextUI$TextPane",
            "MenuBarUI", p + "ChkMenus$MenuBar", "MenuUI", p + "ChkMenus$Menu", "MenuItemUI", p + "ChkMenus$MenuItem",
            "CheckBoxMenuItemUI", p + "ChkMenus$CheckBoxMenuItem", "RadioButtonMenuItemUI", p + "ChkMenus$RadioButtonMenuItem",
            "PopupMenuUI", p + "ChkMenus$PopupMenu", "PopupMenuSeparatorUI", p + "ChkMenus$PopupSeparator",
            "ToolBarUI", p + "ChkToolBarUI", "ToolBarSeparatorUI", p + "ChkToolBarUI$Separator",
            "ScrollBarUI", p + "ChkScrollBarUI", "SliderUI", p + "ChkSliderUI", "ProgressBarUI", p + "ChkProgressBarUI",
            "SpinnerUI", p + "ChkSpinnerUI", "SplitPaneUI", p + "ChkSplitPaneUI",
            "TableHeaderUI", p + "ChkTableHeaderUI", "TableUI", p + "ChkTableUI", "ToolTipUI", p + "ChkToolTipUI",
            "InternalFrameUI", p + "ChkInternalFrameUI", "DesktopIconUI", p + "ChkDesktopIconUI", "DesktopPaneUI", p + "ChkDesktopPaneUI",
            "RootPaneUI", p + "ChkRootPaneUI",
        });
    }

    @Override protected void initSystemColorDefaults(UIDefaults t) {
        loadSystemColors(t, new String[]{
            "desktop", "#26558E", "activeCaption", "#D8E3F2", "activeCaptionText", "#1E395B", "activeCaptionBorder", "#5A738E",
            "inactiveCaption", "#E3E9F0", "inactiveCaptionText", "#7B8DA5", "inactiveCaptionBorder", "#8FA0B3",
            "window", "#FFFFFF", "windowBorder", "#9CB0C4", "windowText", "#1A202C",
            "menu", "#F4F6F9", "menuText", "#1A202C", "text", "#FFFFFF", "textText", "#1A202C",
            "textHighlight", "#3399FF", "textHighlightText", "#FFFFFF", "textInactiveText", "#9AA3AD",
            "control", "#F0F2F5", "controlText", "#1E293B", "controlHighlight", "#FFFFFF", "controlLtHighlight", "#FFFFFF",
            "controlShadow", "#9AA3AC", "controlDkShadow", "#5A738E", "scrollbar", "#ECEFF3",
            "info", "#F4F6F9", "infoText", "#111111",
        }, false);
    }

    private static ColorUIResource col(int v) { return new ColorUIResource(v); }

    @Override protected void initComponentDefaults(UIDefaults t) {
        super.initComponentDefaults(t);
        FontUIResource ui = new FontUIResource(Chk.baseFont());
        FontUIResource small = new FontUIResource(Chk.baseFont().deriveFont(11f));
        FontUIResource bold = new FontUIResource(Chk.baseFont().deriveFont(Font.BOLD));
        FontUIResource mono = new FontUIResource(Chk.monoFont());
        for (Object k : new java.util.ArrayList<>(t.keySet()))
            if (k instanceof String ks && (ks.endsWith(".font") || ks.endsWith("Font"))) t.put(ks, ui);

        ColorUIResource text = col(0x1A202C), off = col(0x9AA3AD), white = col(0xFFFFFF), face = col(0xF0F2F5);
        ColorUIResource sel = col(0xDDEEFE), selFg = col(0x003380);
        Border field = new BorderUIResource(new ChkBorders.TextBorder());
        Border scroll = new BorderUIResource(new ChkBorders.ScrollBorder());
        Insets none = new InsetsUIResource(0, 0, 0, 0);
        Border btn = new BorderUIResource(new ChkBorders.ButtonBorder());

        t.putDefaults(new Object[]{
            // general
            "Panel.background", white, "Viewport.background", white, "OptionPane.background", white,
            "Label.foreground", text, "Label.disabledForeground", off, "Label.disabledShadow", white,
            "TitledBorder.border", new BorderUIResource(new ChkBorders.GroupBorder()), "TitledBorder.titleColor", col(0x0E489E), "TitledBorder.font", bold,
            "ScrollPane.border", scroll, "ScrollPane.background", white, "ScrollPane.viewportBorder", null,
            "Separator.foreground", col(0xD0D6DE), "Separator.background", white,
            "ToolTip.background", col(0xF4F6F9), "ToolTip.foreground", col(0x111111), "ToolTip.font", small,
            "ToolTip.border", new BorderUIResource(new EmptyBorder(3, 7, 3, 7)),
            // buttons
            "Button.border", btn, "Button.margin", new InsetsUIResource(3, 10, 3, 10), "Button.foreground", col(0x1E293B), "Button.background", face,
            "Button.disabledText", off, "Button.textShiftOffset", 0, "Button.focus", col(0x404040), "Button.select", face,
            "ToggleButton.border", btn, "ToggleButton.margin", new InsetsUIResource(3, 10, 3, 10), "ToggleButton.foreground", col(0x1E293B),
            "ToggleButton.disabledText", off, "ToggleButton.textShiftOffset", 0,
            "CheckBox.icon", ChkIcons.checkBox(), "CheckBox.foreground", col(0x334155), "CheckBox.disabledText", off, "CheckBox.focus", col(0x404040),
            "CheckBox.textIconGap", 5, "CheckBox.background", white, "CheckBox.font", small,
            "RadioButton.icon", ChkIcons.radio(), "RadioButton.foreground", col(0x334155), "RadioButton.disabledText", off, "RadioButton.focus", col(0x404040),
            "RadioButton.textIconGap", 5, "RadioButton.background", white, "RadioButton.font", small,
            // text
            "TextField.border", field, "PasswordField.border", field, "FormattedTextField.border", field, "Spinner.border", field,
            "TextField.background", white, "PasswordField.background", white, "FormattedTextField.background", white,
            "TextField.inactiveBackground", face, "PasswordField.inactiveBackground", face, "FormattedTextField.inactiveBackground", face,
            "TextField.foreground", col(0x0F172A), "PasswordField.foreground", col(0x0F172A), "FormattedTextField.foreground", col(0x0F172A),
            "TextArea.background", white, "TextPane.background", white, "EditorPane.background", white,
            "TextField.margin", none, "PasswordField.margin", none, "FormattedTextField.margin", none,
            "TextArea.margin", new InsetsUIResource(3, 6, 3, 6), "TextPane.margin", new InsetsUIResource(3, 6, 3, 6), "EditorPane.margin", new InsetsUIResource(3, 6, 3, 6),
            "TextArea.font", mono, "TextArea.foreground", col(0x0F172A),
            "TextField.inactiveForeground", off, "TextArea.inactiveForeground", off,
            "TextField.selectionBackground", col(0x3399FF), "TextField.selectionForeground", white, "TextArea.selectionBackground", col(0x3399FF), "TextArea.selectionForeground", white,
            "TextPane.selectionBackground", col(0x3399FF), "EditorPane.selectionBackground", col(0x3399FF), "PasswordField.selectionBackground", col(0x3399FF),
            "FormattedTextField.selectionBackground", col(0x3399FF), "TextField.caretForeground", text, "TextArea.caretForeground", text,
            // combo / list
            "ComboBox.background", white, "ComboBox.foreground", col(0x0F172A), "ComboBox.disabledForeground", off,
            "ComboBox.border", new BorderUIResource(new EmptyBorder(1, 1, 1, 1)), "ComboBox.squareButton", Boolean.FALSE,
            "ComboBox.selectionBackground", col(0xCCE8FF), "ComboBox.selectionForeground", text,
            "List.background", white, "List.foreground", text, "List.selectionBackground", sel, "List.selectionForeground", selFg, "List.font", small,
            "List.focusCellHighlightBorder", new BorderUIResource(new EmptyBorder(1, 1, 1, 1)),
            "List.cellRenderer", new UIDefaults.ProxyLazyValue("chk.ChkListCellRenderer$UI"),
            // tabs
            "TabbedPane.background", face, "TabbedPane.foreground", col(0x334155), "TabbedPane.font", small, "TabbedPane.contentOpaque", Boolean.TRUE,
            "TabbedPane.tabInsets", new InsetsUIResource(3, 10, 3, 10), "TabbedPane.tabAreaInsets", new InsetsUIResource(3, 6, 0, 6),
            "TabbedPane.contentBorderInsets", new InsetsUIResource(1, 1, 1, 1), "TabbedPane.selectedTabPadInsets", new InsetsUIResource(1, 0, 0, 0),
            "TabbedPane.tabsOverlapBorder", Boolean.TRUE, "TabbedPane.textIconGap", 5, "TabbedPane.tabRunOverlay", 0,
            // menus
            "MenuBar.border", new BorderUIResource(new EmptyBorder(1, 4, 1, 4)), "MenuBar.background", face,
            "Menu.border", new BorderUIResource(new EmptyBorder(3, 8, 3, 8)), "Menu.foreground", text, "Menu.selectionForeground", text, "Menu.selectionBackground", col(0xE5EFF9),
            "Menu.arrowIcon", ChkIcons.menuArrow(), "Menu.checkIcon", ChkIcons.nothing(1, 1), "Menu.margin", none, "Menu.disabledForeground", off,
            "Menu.acceleratorForeground", col(0x777777), "Menu.acceleratorSelectionForeground", col(0xDFE9FB), "Menu.font", ui,
            "MenuItem.border", new BorderUIResource(new EmptyBorder(3, 8, 3, 10)), "MenuItem.foreground", col(0x111111), "MenuItem.font", small,
            "MenuItem.selectionForeground", white, "MenuItem.selectionBackground", col(0x2D7BE5),
            "MenuItem.acceleratorForeground", col(0x777777), "MenuItem.acceleratorSelectionForeground", col(0xDFE9FB), "MenuItem.disabledForeground", off,
            "MenuItem.checkIcon", ChkIcons.nothing(1, 1), "MenuItem.arrowIcon", ChkIcons.nothing(1, 1), "MenuItem.margin", none,
            "MenuItem.minimumTextOffset", 0, "MenuItem.afterCheckIconGap", 6, "MenuItem.textIconGap", 8,
            "CheckBoxMenuItem.checkIcon", ChkIcons.menuCheck(), "CheckBoxMenuItem.border", new BorderUIResource(new EmptyBorder(3, 8, 3, 10)), "CheckBoxMenuItem.margin", none,
            "CheckBoxMenuItem.selectionBackground", col(0x2D7BE5), "CheckBoxMenuItem.selectionForeground", white, "CheckBoxMenuItem.acceleratorForeground", col(0x777777),
            "CheckBoxMenuItem.foreground", col(0x111111), "CheckBoxMenuItem.arrowIcon", ChkIcons.nothing(1, 1), "CheckBoxMenuItem.font", small,
            "RadioButtonMenuItem.checkIcon", ChkIcons.menuRadio(), "RadioButtonMenuItem.border", new BorderUIResource(new EmptyBorder(3, 8, 3, 10)), "RadioButtonMenuItem.margin", none,
            "RadioButtonMenuItem.selectionBackground", col(0x2D7BE5), "RadioButtonMenuItem.selectionForeground", white, "RadioButtonMenuItem.acceleratorForeground", col(0x777777),
            "RadioButtonMenuItem.foreground", col(0x111111), "RadioButtonMenuItem.arrowIcon", ChkIcons.nothing(1, 1), "RadioButtonMenuItem.font", small,
            "PopupMenu.border", new BorderUIResource(new ChkBorders.PopupBorder(new Insets(3, 3, 3, 3), new Color(0x9BAAB9))),
            "PopupMenu.background", col(0xF4F6F9), "PopupMenu.foreground", text,
            // toolbar
            "ToolBar.border", new BorderUIResource(new ChkBorders.ToolBarBorder()), "ToolBar.background", face, "ToolBar.isRollover", Boolean.TRUE, "ToolBar.font", small,
            "ToolBar.rolloverBorder", btn, "ToolBar.nonrolloverBorder", btn, "ToolBar.separatorSize", new DimensionUIResource(9, 9), "ToolBar.foreground", col(0x1E293B),
            // scroll / slider / progress / spinner / split
            "ScrollBar.width", 17, "ScrollBar.background", col(0xECEFF3),
            "Slider.background", white, "Slider.focus", white, "Slider.tickColor", col(0x8D939B), "Slider.font", small,
            "ProgressBar.border", new BorderUIResource(new EmptyBorder(1, 1, 1, 1)), "ProgressBar.foreground", col(0x1A202C), "ProgressBar.background", col(0xE2E5E8),
            "ProgressBar.selectionForeground", col(0x1A202C), "ProgressBar.selectionBackground", col(0x1A202C), "ProgressBar.font", small,
            "ProgressBar.horizontalSize", new DimensionUIResource(146, 13), "ProgressBar.verticalSize", new DimensionUIResource(13, 146),
            "ProgressBar.repaintInterval", 30, "ProgressBar.cycleTime", 1600,
            "Spinner.background", white, "Spinner.foreground", col(0x0F172A),
            "SplitPane.border", null, "SplitPane.dividerSize", 6, "SplitPaneDivider.border", null, "SplitPane.background", face,
            // table / tree
            "Table.background", white, "Table.foreground", text, "Table.selectionBackground", sel, "Table.selectionForeground", selFg, "Table.font", small,
            "Table.gridColor", col(0xF0F2F4), "Table.rowHeight", 20, "Table.scrollPaneBorder", scroll,
            "Table.focusCellHighlightBorder", new BorderUIResource(new EmptyBorder(1, 6, 1, 6)), "Table.focusSelectedCellHighlightBorder", new BorderUIResource(new EmptyBorder(1, 6, 1, 6)),
            "Table.cellNoFocusBorder", new BorderUIResource(new EmptyBorder(1, 6, 1, 6)),
            "TableHeader.foreground", col(0x222222), "TableHeader.background", white, "TableHeader.font", bold,
            "Tree.background", white, "Tree.foreground", text, "Tree.textBackground", white, "Tree.textForeground", text, "Tree.font", small,
            "Tree.selectionBackground", sel, "Tree.selectionForeground", selFg, "Tree.selectionBorderColor", sel,
            "Tree.expandedIcon", ChkIcons.treeNode(true), "Tree.collapsedIcon", ChkIcons.treeNode(false),
            "Tree.openIcon", ChkIcons.folder(true), "Tree.closedIcon", ChkIcons.folder(false), "Tree.leafIcon", ChkIcons.file(),
            "Tree.paintLines", Boolean.FALSE, "Tree.rowHeight", 20, "Tree.leftChildIndent", 6, "Tree.rightChildIndent", 12,
            // dialogs
            "OptionPane.errorIcon", ChkIcons.message(ChkIcons.Message.ERROR), "OptionPane.informationIcon", ChkIcons.message(ChkIcons.Message.INFO),
            "OptionPane.warningIcon", ChkIcons.message(ChkIcons.Message.WARNING), "OptionPane.questionIcon", ChkIcons.message(ChkIcons.Message.QUESTION),
            "OptionPane.messageForeground", col(0x1A202C), "OptionPane.border", new BorderUIResource(new EmptyBorder(0, 0, 0, 0)),
            "OptionPane.messageAreaBorder", new BorderUIResource(new EmptyBorder(16, 16, 12, 16)),
            "OptionPane.buttonAreaBorder", new BorderUIResource(new ChkBorders.CommandBarBorder()),
            "FileChooser.upFolderIcon", ChkIcons.upFolder(), "FileChooser.newFolderIcon", ChkIcons.newFolder(), "FileChooser.homeFolderIcon", ChkIcons.home(),
            "FileChooser.listViewIcon", ChkIcons.listView(), "FileChooser.detailsViewIcon", ChkIcons.detailsView(),
            "FileChooser.computerIcon", ChkIcons.computer(), "FileChooser.directoryIcon", ChkIcons.folder(false), "FileChooser.fileIcon", ChkIcons.file(),
            "FileView.directoryIcon", ChkIcons.folder(false), "FileView.fileIcon", ChkIcons.file(), "FileView.computerIcon", ChkIcons.computer(),
            "FileView.hardDriveIcon", ChkIcons.drive(false), "FileView.floppyDriveIcon", ChkIcons.drive(true),
            // internal frames / desktop
            "DesktopPane.background", col(0x26558E), "Desktop.background", col(0x26558E),
            "InternalFrame.border", new BorderUIResource(new ChkBorders.InternalFrameBorder()),
            "DesktopIcon.border", new BorderUIResource(new ChkBorders.InternalFrameBorder()),
            "InternalFrame.iconifyIcon", ChkIcons.nothing(1, 1), "InternalFrame.maximizeIcon", ChkIcons.nothing(1, 1),
            "InternalFrame.minimizeIcon", ChkIcons.nothing(1, 1), "InternalFrame.closeIcon", ChkIcons.nothing(1, 1),
            "InternalFrame.activeTitleBackground", col(0xD8E3F2), "InternalFrame.inactiveTitleBackground", col(0xE3E9F0),
            "InternalFrame.activeTitleForeground", col(0x1E395B), "InternalFrame.inactiveTitleForeground", col(0x7B8DA5), "InternalFrame.titleFont", bold, "InternalFrame.icon", ChkIcons.appShield(14),
        });
    }
}
