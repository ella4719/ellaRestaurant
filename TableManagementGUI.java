
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import java.awt.*;

import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class TableManagementGUI extends JFrame implements TableInteraction , RestaurantEventListener{
	private static TableManagementGUI instance;
    private JTable tableTable;
    private DefaultTableModel tableTableModel;
    private Restaurant restaurant;
    private JPanel topPanel;
    private Set<Integer> editModeRows;
    private JPanel panel_3;
    
	
    // 생성자를 private으로 선언하여 외부에서 인스턴스 생성을 방지
    private TableManagementGUI(Restaurant restaurant) {
        this.restaurant = restaurant;
        this.editModeRows = new HashSet<>();
        initializeUI();
        initializePanel3();
        restaurant.addEventListener(this);
        
    }
    // 싱글톤 인스턴스를 얻는 public static 메소드
    public static TableManagementGUI getInstance(Restaurant restaurant) {
        if (instance == null) {
            instance = new TableManagementGUI(restaurant);
        }
        return instance;
    }

    private void initializeUI() {
        setTitle("Table Management");
        setSize(1366, 1024); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        initializeTable();
        initializeTopPanel();
       
        add(topPanel, BorderLayout.SOUTH);
        
        JPanel panel = new JPanel();
		panel.setBounds(17, 0, 1145, 60);
		add(panel);
		panel.setLayout(null);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(16, 6, 328, 49);
		panel.add(panel_1);
		panel_1.setBackground(new Color(230, 230, 250));
		
		JLabel lblNewLabel = new JLabel("Ella Restaurant");
		lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 30));
		panel_1.add(lblNewLabel);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBounds(374, 6, 423, 48);
		panel.add(panel_2);
		panel_2.setBackground(new Color(230, 230, 250));
		
		JLabel lblNewLabel_1 = new JLabel("Table Edit");
		lblNewLabel_1.setFont(new Font("Times New Roman", Font.BOLD, 30));
		panel_2.add(lblNewLabel_1);
        
		JPanel panel_4 = new JPanel();
		panel_4.setBounds(702, 128, 641, 690);
		add(panel_4);
		
		JPanel panel_5 = new JPanel();
		panel_5.setBounds(27, 845, 1313, 52);
		add(panel_5);
		
		ImageIcon homeIcon = new ImageIcon("./Image/home-icon.png");

        JButton homeButton = new JButton(homeIcon);
        homeButton.setBounds(1279, 0, 86, 74);
        homeButton.setPreferredSize(new Dimension(86,86));
        homeButton.setBorderPainted(true);
        homeButton.setContentAreaFilled(false);
        homeButton.setFocusPainted(false);
       
        
        homeButton.addActionListener(e -> returnHome());
        add(homeButton);
        

        populateTable();
        initializePanel3();
    }

    private void initializeTable() {
        String[] columnNames = {"Table Name", "Capacity", "Available", "Edit", "Save", "Delete"};
        tableTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
            	// "Edit", "Save", "Delete" 버튼 열은 항상 편집 가능
                if (column >= 3) {
                    return true;
                }
                return editModeRows.contains(row) && (column < 3 || column == 4);
            }
        };

        tableTable = new JTable(tableTableModel);
        JScrollPane scrollPane = new JScrollPane(tableTable);
        scrollPane.setBounds(702, 72, 641, 690);
        getContentPane().add(scrollPane);

        // 버튼 렌더러 및 에디터 설정
        ButtonRenderer buttonRenderer = new ButtonRenderer();
        tableTable.getColumn("Edit").setCellRenderer(buttonRenderer);
        tableTable.getColumn("Edit").setCellEditor(new ButtonEditor(new JCheckBox(), this, "Edit", "TableManagement"));
        tableTable.getColumn("Save").setCellRenderer(buttonRenderer);
        tableTable.getColumn("Save").setCellEditor(new ButtonEditor(new JCheckBox(), this, "Save", "TableManagement"));
        tableTable.getColumn("Delete").setCellRenderer(buttonRenderer);
        tableTable.getColumn("Delete").setCellEditor(new ButtonEditor(new JCheckBox(), this, "Delete", "TableManagement"));
    }

    private void returnHome() {
        this.setVisible(false); // 현재 창을 숨김
        MainFrameGUI.getInstance(restaurant).setVisible(true); // 기존 MainFrameGUI 인스턴스를 화면에 표시
    }




    private void initializeTopPanel() {
        topPanel = new JPanel();
        topPanel.setBounds(27, 805, 1313, 52);
        JButton addButton = new JButton("Add");
        JButton saveAllButton = new JButton("Save All");
        
        topPanel.add(addButton);
        topPanel.add(saveAllButton);

        addButton.addActionListener(e -> addTableItem());
        saveAllButton.addActionListener(e -> saveAllTables());
    }

    private void addTableItem() {
        // Add a new row in edit mode
        int newRowIndex = tableTableModel.getRowCount();
        tableTableModel.addRow(new Object[]{"", "", Boolean.TRUE, "Edit", "Save", "Delete"});
        editModeRows.add(newRowIndex);
        tableTable.setRowSelectionInterval(newRowIndex, newRowIndex);
        tableTable.editCellAt(newRowIndex, 0);
    }
    
    public void toggleEditMode(int row, String mode) {
        if ("Edit".equals(mode)) {
            editModeRows.add(row);
            // 필요한 경우 추가적인 UI 업데이트 로직
        } else if ("Save".equals(mode)) {
            saveTableItem(row);
            editModeRows.remove(row);
            // 필요한 경우 추가적인 UI 업데이트 로직
        }
        tableTableModel.fireTableRowsUpdated(row, row); // 행 업데이트
    }

    public void saveTableItem(int row) {
        // 테이블 정보 추출
    	
        String tableName = (String) tableTableModel.getValueAt(row, 0);
        int capacity = Integer.parseInt((String) tableTableModel.getValueAt(row, 1));
        boolean isAvailable = (Boolean) tableTableModel.getValueAt(row, 2);

        try {
            // 테이블 정보 업데이트
            Table updatedTable = new Table(tableName, capacity, isAvailable);
            restaurant.getProgramManager().addOrUpdateTable(row, updatedTable);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    public void deleteTableItem(int row) {
        String tableName = (String) tableTableModel.getValueAt(row, 0);
        try {
            // 테이블 삭제
            restaurant.getProgramManager().deleteTable(tableName);
            tableTableModel.removeRow(row);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void saveAllTables() {
        Restaurant.ProgramManager programManager = restaurant.new ProgramManager();

        for (int i = 0; i < tableTableModel.getRowCount(); i++) {
            String tableName = (String) tableTableModel.getValueAt(i, 0);
            int numberOfPeople = Integer.parseInt((String) tableTableModel.getValueAt(i, 1));
            boolean isAvailable = Boolean.TRUE.equals(tableTableModel.getValueAt(i, 2)); // 'Available' 컬럼 처리

            try {
                Table newTable = new Table(tableName, numberOfPeople, isAvailable, 0);
                programManager.addOrUpdateTable(i,newTable);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }

        editModeRows.clear(); // 편집 모드 종료
        tableTableModel.fireTableDataChanged(); // 테이블 모델 업데이트
    }

    private void populateTable() {
        // 기존 테이블 모델 데이터 삭제
        tableTableModel.setRowCount(0);

        // Restaurant 객체에서 테이블 정보 가져오기
        ArrayList<Table> tables = restaurant.getTables();
        for (Table table : tables) {
            // 테이블 모델에 행 추가
            Object[] rowData = new Object[] {
                table.getTableName(),
                String.valueOf(table.getMember()),
                table.getAvailable(), 
                "Edit",
                "Save",
                "Delete"
            };
            tableTableModel.addRow(rowData);
        }
    }
    
 // panel_6 초기화
    private void initializePanel3() {
    	panel_3 = new JPanel();
        panel_3.setBackground(Color.WHITE);
        panel_3.setLayout(new GridLayout(0, 4, 10, 10));

        JScrollPane scrollPane = new JScrollPane(panel_3);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBounds(27, 72, 648, 690);
        getContentPane().add(scrollPane);


        updateUI(); // UI를 업데이트하여 panel_6에 컴포넌트들을 추가합니다.
    }
	
	// 데이터 업데이트
    public void updateData(Restaurant restaurant) {
        this.restaurant = restaurant;
        System.out.println("Updating data - Restaurant tables count: " + restaurant.getTables().size());
        updateUI();
    }


    private void updateUI() {
        panel_3.removeAll();
        for (Table table : restaurant.getTables()) {
            JPanel tablePanel = createTablePanel(table);
            panel_3.add(tablePanel);

        }
        panel_3.revalidate();
        panel_3.repaint();
    }
    // 테이블 패널 생성
    protected JPanel createTablePanel(Table table) {
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(null);
        tablePanel.setSize(new Dimension(120, 120));
        tablePanel.setPreferredSize(new Dimension(120, 120));
        
        // 각 테이블 패널에 이름 설정
        tablePanel.setName(table.getTableName());

        // 라벨 패널 생성 및 라벨 추가
        
        JLabel tableNameLabel = new JLabel("Table: " + table.getTableName());
        tableNameLabel.setBounds(10, 2, 90, 40);
        tablePanel.add(tableNameLabel);
        
        

        // 인원수 라벨-'Capacity: member명' 
        JLabel tableCapacityLabel = new JLabel("Capacity: " + table.getMember() + "명");
        tableCapacityLabel.setBounds(10, 42, 90, 40);
        tablePanel.add(tableCapacityLabel);


        return tablePanel;
    }
    @Override
    public void onDataChanged() {
        updateUI();
    }



    
   
}     