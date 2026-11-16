# Component coverage (java_swing_components.md)
Own UI delegate + painting: JButton, JToggleButton, JCheckBox, JRadioButton, JMenuBar, JMenu, JMenuItem, JCheckBoxMenuItem,
JRadioButtonMenuItem, JPopupMenu (+separator), JLabel, JTextField, JPasswordField, JFormattedTextField, JTextArea, JEditorPane,
JTextPane, JComboBox (+popup, editable), JSpinner, JSlider, JProgressBar, JScrollBar, JSplitPane, JTabbedPane, JToolBar (+separator),
JTable, JTableHeader (zebra rows, sort arrows), JList, JTree, JToolTip, JPanel (group-box titles), JInternalFrame,
JInternalFrame.JDesktopIcon, JDesktopPane, JRootPane (JFrame / JDialog title bar, frame, resize, JOptionPane dialogs).
Skinned through borders, colours, fonts, icons: JScrollPane, JViewport, JSeparator, JColorChooser, JFileChooser, JOptionPane,
JLayeredPane, Box / Box.Filler, JLayer, TitledBorder / BorderFactory borders.
Nothing visual to skin: JWindow (undecorated by design), JApplet (removed from modern Java), ButtonGroup, Action, Timer,
SwingWorker, UIManager, layout managers.
Custom borders: ButtonBorder, TextBorder, ScrollBorder, PopupBorder, GroupBorder, Titled, ToolBarBorder, InternalFrameBorder,
CommandBarBorder, StatusPaneBorder, AccentBorder.
