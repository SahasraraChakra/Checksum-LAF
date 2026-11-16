package chk;

import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.LayerUI;
import javax.swing.table.*;
import javax.swing.text.*;
import javax.swing.tree.*;

/** Showcase of every Swing component under the Chk look and feel. Run: java -cp aero.jar chk.ChkDemo */
public class ChkDemo {

    public static void main(String[] a) {
        ChecksumLookAndFeel.install();
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("Checksum Look & Feel \u2013 Component Gallery".replace("&&", "&"));
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setJMenuBar(menuBar());
            JPanel c = new JPanel(new BorderLayout());
            c.add(toolBar(), BorderLayout.NORTH);
            c.add(tabs(), BorderLayout.CENTER);
            c.add(statusBar(), BorderLayout.SOUTH);
            f.setContentPane(c);
            f.setSize(1100, 720);
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        });
    }

    // ------------------------------------------------------------------ shell
    public static JMenuBar menuBar() {
        JMenuBar mb = new JMenuBar();
        JMenu file = new JMenu("File"); file.setMnemonic('F');
        file.add(item("New", "ctrl N")); file.add(item("Open\u2026", "ctrl O"));
        JMenu recent = new JMenu("Open Recent"); recent.add(new JMenuItem("report.xlsx")); recent.add(new JMenuItem("notes.txt")); file.add(recent);
        JMenuItem save = item("Save", "ctrl S"); file.add(save);
        JMenuItem saveAs = item("Save As\u2026", "ctrl shift S"); saveAs.setEnabled(false); file.add(saveAs);
        file.addSeparator();
        JCheckBoxMenuItem auto = new JCheckBoxMenuItem("Auto-save", true); file.add(auto);
        JCheckBoxMenuItem wrap = new JCheckBoxMenuItem("Word wrap"); file.add(wrap);
        file.addSeparator();
        ButtonGroup g = new ButtonGroup();
        for (String s : new String[]{"Small icons", "Large icons"}) { JRadioButtonMenuItem r = new JRadioButtonMenuItem(s, s.startsWith("Small")); g.add(r); file.add(r); }
        file.addSeparator(); file.add(item("Exit", "alt F4"));
        JMenu edit = new JMenu("Edit"); edit.setMnemonic('E');
        edit.add(item("Undo", "ctrl Z")); edit.add(item("Redo", "ctrl Y")); edit.addSeparator();
        edit.add(item("Cut", "ctrl X")); edit.add(item("Copy", "ctrl C")); edit.add(item("Paste", "ctrl V"));
        JMenu view = new JMenu("View"); view.add(new JMenuItem("Zoom In")); view.add(new JMenuItem("Zoom Out"));
        JMenu help = new JMenu("Help"); help.add(new JMenuItem("About Chk\u2026"));
        mb.add(file); mb.add(edit); mb.add(view); mb.add(help);
        return mb;
    }

    private static JMenuItem item(String t, String key) {
        JMenuItem i = new JMenuItem(t);
        i.setAccelerator(KeyStroke.getKeyStroke(key));
        return i;
    }

    public static JToolBar toolBar() {
        JToolBar tb = new JToolBar();
        tb.add(new JButton("New", ChkIcons.file()));
        tb.add(new JButton("Open", ChkIcons.folder(false)));
        tb.addSeparator();
        JToggleButton t1 = new JToggleButton("Bold"); t1.setSelected(true); tb.add(t1);
        tb.add(new JToggleButton("Italic"));
        tb.addSeparator();
        tb.add(new JButton("Home", ChkIcons.home()));
        tb.add(Box.createHorizontalGlue());
        JTextField search = new JTextField("Search", 12); tb.add(search);
        for (Component c : tb.getComponents()) if (c instanceof AbstractButton b) { b.setFocusable(false); }
        tb.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        search.setMaximumSize(new Dimension(160, 24));
        return tb;
    }

    public static JComponent statusBar() {
        JPanel p = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setPaint(new GradientPaint(0, 0, new Color(0xF4F6FA), 0, getHeight(), new Color(0xDDE4EE)));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(new Color(0xB9C4D4)); g2.drawLine(0, 0, getWidth(), 0);
            }
        };
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(3, 8, 3, 8));
        p.add(new JLabel("Ready"), BorderLayout.WEST);
        JProgressBar pb = new JProgressBar(0, 100); pb.setValue(62); pb.setPreferredSize(new Dimension(140, 15));
        p.add(pb, BorderLayout.EAST);
        return p;
    }

    public static JTabbedPane tabs() {
        JTabbedPane t = new JTabbedPane();
        t.addTab("Controls", controls());
        t.addTab("Text", text());
        t.addTab("Data", data());
        t.addTab("Desktop", desktop());
        t.addTab("Choosers", choosers());
        t.setBorder(new EmptyBorder(6, 6, 6, 6));
        return t;
    }

    private static JPanel group(String title, JComponent c) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createTitledBorder(title));
        p.add(c);
        return p;
    }

    private static JPanel flow(Component... cs) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        for (Component c : cs) p.add(c);
        return p;
    }

    // ------------------------------------------------------------------ tab: controls
    public static JPanel controls() {
        JPanel root = new JPanel(new GridLayout(1, 3, 10, 0));
        root.setBorder(new EmptyBorder(10, 10, 10, 10));
        ColumnPanel col1 = new ColumnPanel(), col2 = new ColumnPanel(), col3 = new ColumnPanel();

        // buttons
        JButton normal = new JButton("Button"); normal.setName("normal");
        JButton def = new JButton("Default"); def.setName("default");
        JButton hot = new JButton("Hover"); hot.setName("hot");
        JButton press = new JButton("Pressed"); press.setName("pressed");
        JButton dis = new JButton("Disabled"); dis.setEnabled(false);
        JToggleButton tgl = new JToggleButton("Toggle", true);
        JButton icon = new JButton("With icon", ChkIcons.folder(false));
        normal.setToolTipText("A tooltip in Chk style");
        JPanel buttons = new JPanel(new GridLayout(0, 1));
        buttons.add(flow(normal, def, hot)); buttons.add(flow(press, dis)); buttons.add(flow(tgl, new JToggleButton("Off"), icon));
        col1.add(group("Buttons", buttons));

        // checks / radios
        JPanel cr = new JPanel(new GridLayout(0, 2, 8, 2));
        JCheckBox c1 = new JCheckBox("Checked", true), c2 = new JCheckBox("Unchecked"), c3 = new JCheckBox("Disabled", true);
        c3.setEnabled(false); c2.setName("hotcheck");
        JRadioButton r1 = new JRadioButton("Option A", true), r2 = new JRadioButton("Option B"), r3 = new JRadioButton("Disabled"); r3.setEnabled(false);
        ButtonGroup bg = new ButtonGroup(); bg.add(r1); bg.add(r2); bg.add(r3);
        cr.add(c1); cr.add(r1); cr.add(c2); cr.add(r2); cr.add(c3); cr.add(r3);
        col1.add(group("Check boxes & radio buttons", cr));

        // sliders / progress / scrollbar
        JSlider s1 = new JSlider(0, 100, 35); s1.setPaintTicks(true); s1.setMajorTickSpacing(25); s1.setMinorTickSpacing(5); s1.setPaintLabels(true);
        JSlider s2 = new JSlider(0, 100, 60);
        JProgressBar p1 = new JProgressBar(0, 100); p1.setValue(65); p1.setStringPainted(true);
        JProgressBar p2 = new JProgressBar(); p2.setIndeterminate(true);
        JScrollBar sb = new JScrollBar(JScrollBar.HORIZONTAL, 20, 30, 0, 100);
        JPanel sl = new JPanel(); sl.setLayout(new BoxLayout(sl, BoxLayout.Y_AXIS));
        for (JComponent c : new JComponent[]{s1, s2, p1, p2, sb}) { sl.add(c); sl.add(Box.createVerticalStrut(8)); }
        col1.add(group("Slider, progress & scroll bar", sl));

        // combos
        JComboBox<String> cb = new JComboBox<>(new String[]{"Windows 7", "Windows Vista", "Windows XP"}); cb.setName("combo");
        JComboBox<String> ce = new JComboBox<>(new String[]{"Editable", "Second", "Third"}); ce.setEditable(true);
        JComboBox<String> cd = new JComboBox<>(new String[]{"Disabled"}); cd.setEnabled(false);
        JSpinner sp = new JSpinner(new SpinnerNumberModel(42, 0, 100, 1));
        JSpinner sd = new JSpinner(new SpinnerDateModel());
        JPanel in = new JPanel(new GridLayout(0, 1, 0, 6));
        in.add(cb); in.add(ce); in.add(cd); in.add(sp); in.add(sd);
        col2.add(group("Combo boxes & spinners", in));

        // list + scrollbar demo
        JList<String> lst = new JList<>(new String[]{"One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight"});
        lst.setSelectedIndex(1); lst.setVisibleRowCount(5);
        col2.add(group("JList in JScrollPane", new JScrollPane(lst)));

        JPanel lay = new JPanel(new BorderLayout());
        JLayeredPane lp = new JLayeredPane(); lp.setPreferredSize(new Dimension(200, 70));
        JButton lb1 = new JButton("Layer 0"), lb2 = new JButton("Layer 1");
        lb1.setBounds(10, 8, 100, 28); lb2.setBounds(60, 28, 100, 28);
        lp.add(lb1, JLayeredPane.DEFAULT_LAYER); lp.add(lb2, JLayeredPane.PALETTE_LAYER);
        col2.add(group("JLayeredPane", lp));

        // labels, separator, box, JLayer
        JLabel l1 = new JLabel("Label with text"), l2 = new JLabel("Disabled label"); l2.setEnabled(false);
        JLabel l3 = new JLabel("Label with icon", ChkIcons.message(ChkIcons.Message.INFO), SwingConstants.LEFT);
        JPanel box = new JPanel(); box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        box.add(l1); box.add(Box.createVerticalStrut(4)); box.add(new JSeparator()); box.add(Box.createVerticalStrut(4)); box.add(l2);
        box.add(Box.createVerticalStrut(6)); box.add(l3);
        col3.add(group("JLabel, JSeparator, Box", box));
        JPanel layerTarget = flow(new JButton("JLayer"), new JCheckBox("wrapped"));
        col3.add(group("JLayer", new JLayer<>(layerTarget, new LayerUI<>() { })));
        JToolBar mini = new JToolBar(); mini.setFloatable(false);
        mini.add(new JButton(ChkIcons.upFolder())); mini.add(new JButton(ChkIcons.newFolder())); mini.addSeparator(); mini.add(new JToggleButton(ChkIcons.listView(), true)); mini.add(new JToggleButton(ChkIcons.detailsView()));
        col3.add(group("JToolBar (icon buttons)", mini));
        JPopupMenu pop = new JPopupMenu(); pop.add("Context item");
        JLabel tip = new JLabel("Hover me for a JToolTip"); tip.setName("tip"); tip.setToolTipText("<html><b>JToolTip</b><br>Checksum-style tooltip</html>");
        col3.add(group("Tooltips", flow(tip)));

        col1.filler(); col2.filler(); col3.filler();
        root.add(col1); root.add(col2); root.add(col3);
        return root;
    }

    /** Vertical stack that keeps each group at its preferred height. */
    static final class ColumnPanel extends JPanel {
        ColumnPanel() { super(new GridBagLayout()); setOpaque(false); }
        @Override public Component add(Component c) {
            GridBagConstraints g = new GridBagConstraints();
            g.gridx = 0; g.gridy = getComponentCount(); g.weightx = 1; g.fill = GridBagConstraints.HORIZONTAL; g.anchor = GridBagConstraints.NORTH;
            g.insets = new Insets(0, 0, 8, 0);
            super.add(c, g);
            return c;
        }
        void filler() {
            GridBagConstraints g = new GridBagConstraints();
            g.gridx = 0; g.gridy = getComponentCount(); g.weighty = 1;
            super.add(Box.createGlue(), g);
        }
    }

    // ------------------------------------------------------------------ tab: text
    public static JPanel text() {
        JPanel root = new JPanel(new GridBagLayout());
        root.setBorder(new EmptyBorder(10, 10, 10, 10));
        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.BOTH; g.weightx = 1; g.weighty = 0; g.insets = new Insets(4, 4, 4, 4);

        JPanel fields = new JPanel(new GridBagLayout());
        GridBagConstraints f = new GridBagConstraints(); f.insets = new Insets(3, 4, 3, 4); f.fill = GridBagConstraints.HORIZONTAL;
        String[] names = {"JTextField", "JPasswordField", "JFormattedTextField", "Disabled field"};
        JComponent[] cs = {new JTextField("Plain text"), new JPasswordField("secret"),
                new JFormattedTextField(new DecimalFormat("#,##0.00")), new JTextField("Can't edit")};
        ((JFormattedTextField) cs[2]).setValue(1234.5); cs[3].setEnabled(false);
        for (int i = 0; i < names.length; i++) {
            f.gridy = i; f.gridx = 0; f.weightx = 0; fields.add(new JLabel(names[i]), f);
            f.gridx = 1; f.weightx = 1; fields.add(cs[i], f);
        }
        g.gridx = 0; g.gridy = 0; root.add(group("Fields", fields), g);

        JTextArea ta = new JTextArea("JTextArea \u2013 multi-line plain text.\nSelect some of this text to see the\nclassic Windows highlight.\n\nInter 11 is the default font.", 6, 28);
        ta.select(15, 40);
        g.gridx = 1; g.gridy = 0; g.weighty = 0; root.add(group("JTextArea in JScrollPane", new JScrollPane(ta)), g);

        JEditorPane ep = new JEditorPane("text/html",
                "<html><body style='font-family:Inter,sans-serif;font-size:11pt'><h3 style='color:#1E395B'>JEditorPane</h3>"
                + "<p>HTML with <b>bold</b>, <i>italic</i> and <a href='#'>links</a>.</p><ul><li>Lists</li><li>Tables</li></ul></body></html>");
        ep.setEditable(false);
        g.gridx = 0; g.gridy = 1; g.weighty = 1; root.add(group("JEditorPane", new JScrollPane(ep)), g);

        JTextPane tp = new JTextPane();
        StyledDocument d = tp.getStyledDocument();
        try {
            Style bold = tp.addStyle("b", null); StyleConstants.setBold(bold, true); StyleConstants.setForeground(bold, new Color(0x1E395B));
            Style red = tp.addStyle("r", null); StyleConstants.setForeground(red, new Color(0xC0392B)); StyleConstants.setItalic(red, true);
            d.insertString(0, "JTextPane ", bold); d.insertString(d.getLength(), "supports mixed styles ", null); d.insertString(d.getLength(), "like this.", red);
        } catch (BadLocationException ignored) { }
        g.gridx = 1; g.gridy = 1; root.add(group("JTextPane", new JScrollPane(tp)), g);
        return root;
    }

    // ------------------------------------------------------------------ tab: data
    public static JPanel data() {
        DefaultMutableTreeNode rootN = new DefaultMutableTreeNode("Computer");
        DefaultMutableTreeNode docs = new DefaultMutableTreeNode("Documents");
        docs.add(new DefaultMutableTreeNode("Reports")); docs.add(new DefaultMutableTreeNode("Letters")); docs.add(new DefaultMutableTreeNode("todo.txt"));
        DefaultMutableTreeNode pics = new DefaultMutableTreeNode("Pictures");
        pics.add(new DefaultMutableTreeNode("holiday.jpg")); pics.add(new DefaultMutableTreeNode("logo.png"));
        rootN.add(docs); rootN.add(pics); rootN.add(new DefaultMutableTreeNode("Music"));
        JTree tree = new JTree(rootN);
        tree.expandRow(1); tree.expandRow(0); tree.setSelectionRow(2);
        tree.setName("tree");

        String[] cols = {"Name", "Type", "Size", "Modified"};
        Object[][] rows = {
                {"Chk.java", "Java source", 12480, "06/11/2026"}, {"ChkButtonUI.java", "Java source", 4310, "06/11/2026"},
                {"ChkRootPaneUI.java", "Java source", 18992, "05/11/2026"}, {"inter.otf", "Font", 605092, "04/11/2026"},
                {"README.md", "Markdown", 2210, "04/11/2026"}, {"build.sh", "Shell script", 312, "03/11/2026"}, {"icon.png", "Image", 8120, "02/11/2026"}};
        DefaultTableModel m = new DefaultTableModel(rows, cols) {
            @Override public Class<?> getColumnClass(int c) { return c == 2 ? Integer.class : String.class; }
        };
        JTable table = new JTable(m);
        table.setAutoCreateRowSorter(true);
        table.getRowSorter().setSortKeys(java.util.List.of(new RowSorter.SortKey(0, SortOrder.ASCENDING)));
        table.setRowSelectionInterval(1, 1);
        table.setFillsViewportHeight(true);

        JList<String> list = new JList<>(new String[]{"Desktop", "Downloads", "Documents", "Music", "Pictures", "Videos"});
        list.setSelectedIndex(2);

        JScrollPane tsp = new JScrollPane(table); tsp.setColumnHeaderView(table.getTableHeader());
        JSplitPane right = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tsp, new JScrollPane(list));
        right.setResizeWeight(.65); right.setDividerLocation(230);
        JSplitPane main = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(tree), right);
        main.setDividerLocation(230);
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(new EmptyBorder(8, 8, 8, 8));
        p.add(main);
        return p;
    }

    // ------------------------------------------------------------------ tab: desktop
    public static JPanel desktop() {
        JDesktopPane dp = new JDesktopPane();
        JInternalFrame f1 = new JInternalFrame("Document 1", true, true, true, true);
        f1.setFrameIcon(ChkIcons.file());
        f1.add(new JScrollPane(new JTextArea("An internal frame on a JDesktopPane.\nIt has the same glass title bar.")));
        f1.setBounds(30, 30, 360, 220); f1.setVisible(true);
        JInternalFrame f2 = new JInternalFrame("Inactive frame", true, true, true, true);
        f2.setFrameIcon(ChkIcons.folder(false));
        JPanel pp = flow(new JButton("OK"), new JButton("Cancel")); f2.add(pp);
        f2.setBounds(260, 120, 340, 180); f2.setVisible(true);
        JInternalFrame f3 = new JInternalFrame("Minimised", true, true, true, true);
        f3.setBounds(500, 20, 220, 120); f3.setVisible(true);
        dp.add(f1); dp.add(f3); dp.add(f2);
        f1.putClientProperty("Chk.active", true);
        try { f1.setSelected(true); f3.setIcon(true); } catch (Exception ignored) { }
        JPanel p = new JPanel(new BorderLayout()); p.setBorder(new EmptyBorder(8, 8, 8, 8)); p.add(dp);
        return p;
    }

    // ------------------------------------------------------------------ tab: choosers
    public static JPanel choosers() {
        JColorChooser cc = new JColorChooser(new Color(0x3399FF));
        JFileChooser fc = new JFileChooser(new java.io.File("."));
        fc.setControlButtonsAreShown(true);
        JTabbedPane t = new JTabbedPane(JTabbedPane.BOTTOM);
        t.addTab("JColorChooser", cc); t.addTab("JFileChooser", fc);
        JPanel buttons = flow(
                action("Info", () -> JOptionPane.showMessageDialog(null, "Everything is Chk.", "Information", JOptionPane.INFORMATION_MESSAGE)),
                action("Question", () -> JOptionPane.showConfirmDialog(null, "Save changes before closing?", "Question", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE)),
                action("Warning", () -> JOptionPane.showMessageDialog(null, "Disk space is low.", "Warning", JOptionPane.WARNING_MESSAGE)),
                action("Error", () -> JOptionPane.showMessageDialog(null, "Could not open the file.", "Error", JOptionPane.ERROR_MESSAGE)),
                action("Input", () -> JOptionPane.showInputDialog(null, "Your name:", "Input", JOptionPane.PLAIN_MESSAGE)));
        JPanel p = new JPanel(new BorderLayout()); p.setBorder(new EmptyBorder(8, 8, 8, 8));
        p.add(t); p.add(buttons, BorderLayout.SOUTH);
        return p;
    }

    private static JButton action(String t, Runnable r) { JButton b = new JButton(t); b.addActionListener(e -> r.run()); return b; }
}
