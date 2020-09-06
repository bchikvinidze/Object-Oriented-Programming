import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MetropolisViewer extends JFrame {
    private MetropolisModel model;
    private MetropolisDB db;
    private JPanel upper;
    private JTextField metroField;
    private JTextField contField;
    private JTextField populField;
    private JTable table;
    private JPanel eastPanel;
    private JPanel searchPanel;
    private JButton addButton;
    private JButton searchButton;
    private JComboBox populationDropdown;
    private JComboBox matchDropdown;
    private JPanel tablePanel;
    private  JScrollPane tableScroll;

    /*
     * sets up a northern part of the panel including three
     * JTextField for one line inputs and their respective labels.
     */
    private void setupNorth(){
        upper = new JPanel();
        upper.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        upper.add(new JLabel("Metropolis:"));
        metroField = new JTextField(10);
        upper.add(metroField);
        upper.add(new JLabel("Continent:"));
        contField = new JTextField(10);
        upper.add(contField);
        upper.add(new JLabel("Population:"));
        populField = new JTextField(10);
        upper.add(populField);
        add(upper, BorderLayout.NORTH);
    }

    /*
     * sets up a JTable which takes our AbstractDataModel and
     * places it on the center of the frame.
     */
    private void setupTable(){
        tablePanel = new JPanel(new BorderLayout(4,4));
        table = new JTable(model);
        tableScroll = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        //tablePanel.add(table);
        tablePanel.add(tableScroll);
        tablePanel.setBorder(BorderFactory.createLineBorder(Color.gray));
        add(tablePanel, BorderLayout.CENTER);
    }

    /*
     * sets up search option combo boxes and adds to the eastern panel
     */
    private void setupSearchOptions(){
        String populationFilters[] = {"Population Larger Than", "Population Less Than"};
        populationDropdown = new JComboBox(populationFilters);
        populationDropdown.setPreferredSize(new Dimension(400,20));
        String matchTypes[] = {"Exact Match", "Partial Match"};
        matchDropdown = new JComboBox(matchTypes);
        matchDropdown.setPreferredSize(new Dimension(400,20));
        searchPanel.add(populationDropdown);
        searchPanel.add(matchDropdown);
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search Options"));
        eastPanel.add(searchPanel);
    }

    /*
     * Sets up all the buttons of the panel: Add and search button and adds to the eastern panel
     */
    private void setupButtons(){
        searchPanel = new JPanel();
        searchPanel.setLayout(new GridLayout(10,1));
        searchPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        addButton = new JButton("Add");
        searchButton =  new JButton("Search");
        eastPanel.add(addButton);
        eastPanel.add(searchButton);
    }

    /*
     * Sets up eastern part of the frame including buttons: Add and search,
     * as well as search options box, consisting of two JComboBoxes
     */
    private void setupEast(){
        eastPanel = new JPanel();
        eastPanel.setLayout(new BoxLayout(eastPanel, BoxLayout.Y_AXIS));
        setupButtons();
        setupSearchOptions();
        add(eastPanel, BorderLayout.EAST);
    }

    /*
     * Sets up listeners for the two buttons
     */
    private void addListeners(){
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    db.insert(metroField.getText(), contField.getText(), populField.getText());
                    model.addRow(metroField.getText(), contField.getText(), populField.getText());
                    //model.fireTableDataChanged();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ResultSet rs = db.query(metroField.getText(), contField.getText(), populField.getText(),
                            populationDropdown.getSelectedItem().toString(), matchDropdown.getSelectedItem().toString());
                    model.addRows(rs);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    /*
     * Constructor sets up properties of the JFrame including title,
     * AbstractDataTable, database, northern, center and eastern panels,
     * add listeners and packs them. When closing the window, makes sure
     * to close database connection and exit program.
     */
    public MetropolisViewer(){
        super("Metropolis Viewer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        model = new MetropolisModel();
        db = new MetropolisDB();
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                try {
                    db.closeConnection();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        setupNorth();
        setupTable();
        setupEast();
        addListeners();

        pack();
        setVisible(true);
    }

    public static void main(String[] args) {
        // GUI Look And Feel
        // Do this incantation at the start of main() to tell Swing
        // to use the GUI LookAndFeel of the native platform. It's ok
        // to ignore the exception.
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) { }

        MetropolisViewer frame = new MetropolisViewer();
    }
}
