import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Set;
import java.util.HashSet;


public class MenuManagementGUI extends JFrame {
    private JTable menuTable;
    private DefaultTableModel menuTableModel;
    private Set<Integer> editModeRows;
    private JComboBox<String> menuFieldSelector;
    private Restaurant restaurant;
    private JPanel fieldManagementPanel;
    private JPanel topPanel;
    
    
  
    public MenuManagementGUI(Restaurant restaurant) {
        this.restaurant = restaurant;
        editModeRows = new HashSet<>();
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("Menu Management");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        initializeTable(); //메뉴 표시할 표 작성 

        initializeFieldSelector(); //메뉴들의 필드를 표시하는 콤보박스 색터 작성 
        add(menuFieldSelector, BorderLayout.NORTH);

        // Create a panel to hold both field management and top panels
        JPanel southPanel = new JPanel(new BorderLayout());
        initializeFieldManagementPanel(); // 이 메서드 내에서 fieldManagementPanel 초기화
        initializeTopPanel(); // 이 메서드 내에서 topPanel 초기화
        southPanel.add(fieldManagementPanel, BorderLayout.NORTH);
        southPanel.add(topPanel, BorderLayout.SOUTH);
        add(southPanel, BorderLayout.SOUTH);

        // Populate table with data
        populateTable();
    }
    
    private void initializeTable() {
        menuTableModel = new DefaultTableModel(new Object[]{"Name", "Price", "Field", "Edit", "Save", "Delete"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
            	// "Edit", "Save", "Delete" 버튼 열은 항상 편집 가능
                if (column >= 3) {
                    return true;
                }
                // "Name"과 "Price" 열은 편집 모드일 때만 편집 가능
                return editModeRows.contains(row) && (column == 0 || column == 1);
            }
        };
        

        menuTable = new JTable(menuTableModel);
        JScrollPane scrollPane = new JScrollPane(menuTable);
        scrollPane.setBounds(702, 72, 641, 690);
        getContentPane().add(scrollPane);

        // 버튼 렌더러 및 에디터 설정
        ButtonRenderer buttonRenderer = new ButtonRenderer();
        menuTable.getColumn("Edit").setCellRenderer(buttonRenderer);
        menuTable.getColumn("Edit").setCellEditor(new ButtonEditor(new JCheckBox(), this, "Edit", "MenuManagement"));
        menuTable.getColumn("Save").setCellRenderer(buttonRenderer);
        menuTable.getColumn("Save").setCellEditor(new ButtonEditor(new JCheckBox(), this, "Save", "MenuManagement"));
        menuTable.getColumn("Delete").setCellRenderer(buttonRenderer);
        menuTable.getColumn("Delete").setCellEditor(new ButtonEditor(new JCheckBox(), this, "Delete", "MenuManagement"));
    }

    //최상단 콤보박스 메뉴필드 색터 초기화 
    private void initializeFieldSelector() {
        menuFieldSelector = new JComboBox<>();
        updateFieldSelector();
        menuFieldSelector.addActionListener(e -> {
            if (menuFieldSelector.getSelectedItem() != null) {
                filterMenuItems(menuFieldSelector.getSelectedItem().toString());
            }
        });
    }
    private void updateFieldSelector() {
        menuFieldSelector.removeAllItems();
        menuFieldSelector.addItem("All");
        
        if(restaurant.menus.size()>0) {
        restaurant.getMenus().stream()
            .map(Menu::getField)
            .distinct()
            .forEach(field -> menuFieldSelector.addItem(field));
        }
        
        
        // 콤보 박스에 항목이 있으면 첫 번째 항목을 기본적으로 선택
        if (menuFieldSelector.getItemCount() > 0) {
            menuFieldSelector.setSelectedIndex(0);
        }
    }
    private void filterMenuItems(String field) { // 필드에 속하는 메뉴 디스플레이 
    	if (field == null) {
            return; // 선택된 항목이 없으면 함수 종료
        }
        menuTableModel.setRowCount(0); // 테이블 초기화
        if (field.equals("All")) {
        	populateTable();
            } else {
            for (Menu menu : restaurant.getMenusByField(field)) {   
            	
                    addMenuToTable(menu);
            }
        }
    }
    

    private void addMenuToTable(Menu menu) {
    	if (!"Dummy".equals(menu.getMenuName())) {
        menuTableModel.addRow(new Object[]{
            menu.getMenuName(),
            menu.getPrice(),
            menu.getField(),
            "Edit",
            "Save",
            "Delete"
        });
    	}
    }
    
    //패널 관련 메소드 
    private void initializeFieldManagementPanel() {
        fieldManagementPanel = new JPanel();
        JButton addFieldButton = new JButton("Add Field");
        JButton deleteFieldButton = new JButton("Delete Field");
        fieldManagementPanel.add(addFieldButton);
        fieldManagementPanel.add(deleteFieldButton);
        add(fieldManagementPanel, BorderLayout.SOUTH);
        
        // Add Field 버튼에 ActionListener 추가
        addFieldButton.addActionListener(e -> addField());
        // Delete Field 버튼에 ActionListener 추가
        deleteFieldButton.addActionListener(e -> deleteField());


        fieldManagementPanel.add(addFieldButton);
        fieldManagementPanel.add(deleteFieldButton);
        add(fieldManagementPanel, BorderLayout.SOUTH);

    
    }
    private void initializeTopPanel() {
        // Add and Home buttons at the top
    	topPanel = new JPanel();
        JButton addButton = new JButton("Add");
        JButton homeButton = new JButton("Home");
        topPanel.add(addButton);
        topPanel.add(homeButton);
        add(topPanel, BorderLayout.SOUTH);

        // Button action listeners
        addButton.addActionListener(e -> addMenuItem());
        homeButton.addActionListener(e -> returnHome());
    }
    
    // Populate the table with menu items
    private void populateTable() {
        menuTableModel.setRowCount(0);
        for(Menu menu : restaurant.getMenus()) {
        	if (!"Dummy".equals(menu.getMenuName())) {
                Object[] rowData = new Object[]{
                    menu.getMenuName(),
                    menu.getPrice(),
                    menu.getField(),
                    "Edit",
                    "Save",
                    "Delete"
                };
                menuTableModel.addRow(rowData);
            }
        }
    }
   


    
    private void addField() {
        String field = JOptionPane.showInputDialog(this, "Enter new field name:");
        if (field != null && !field.trim().isEmpty()) {
            try {
                Menu dummyMenu = new Menu("Dummy", 0, field.trim()); // 임시 메뉴 객체 생성
                restaurant.addMenu(dummyMenu); //실제 데이터에 추가 
                updateFieldSelector(); // 필드 셀렉터 업데이트
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }
    
    private void deleteField() {
        String selectedField = (String) menuFieldSelector.getSelectedItem();
        if (selectedField == null) {
            JOptionPane.showMessageDialog(this, "필드를 선택해주세요.", "필드 삭제 오류", JOptionPane.ERROR_MESSAGE);
            return; // 선택된 필드가 없으면 함수 종료
        }

        try {
            if ("All".equals(selectedField)) {
                int confirm = JOptionPane.showConfirmDialog(this, 
                        "모든 필드와 메뉴를 삭제하게 됩니다. 정말로 삭제하겠습니까?",
                        "전체 삭제 확인", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    restaurant.clearMenus(); // 모든 메뉴 삭제
                }
            } else {
                restaurant.getProgramManager().deleteMenusByField(selectedField); // 선택된 필드의 메뉴 삭제
            }
            updateFieldSelector(); // 필드 셀렉터 업데이트
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "삭제 오류", JOptionPane.ERROR_MESSAGE);
        }
    }



    




 // Adds a new menu item
    private void addMenuItem() {
        JTextField menuNameField = new JTextField();
        JTextField priceField = new JTextField();
        JTextField itemField = new JTextField();
        Menu menu;
        String[] menuFields = restaurant.getMenus().stream()
    .map(Menu::getField)
    .distinct()
    .toArray(String[]::new);
JComboBox<String> fieldComboBox = new JComboBox<>(menuFields);
        Object[] message = {
            "Menu Name:", menuNameField,
            "Price:", priceField,
            "Field:", fieldComboBox,
            
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Add New Menu Item", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String menuName = menuNameField.getText();
            String priceText = priceField.getText();
            String field = fieldComboBox.getSelectedItem().toString();
            String item = itemField.getText();
            try {
            	int price = Integer.parseInt(priceText);
                Menu newMenu = new Menu(menuName, price, field);
                restaurant.addMenu(newMenu);
                menuTableModel.addRow(new Object[]{menuName, price,field, "Edit", "Save", "Delete"});
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number for the price.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "An error occurred: " + ex.getMessage());
            }
        }
    }
    
    
    private void returnHome() {
        this.dispose();
        MainFrameGUI.getInstance(restaurant).updateData(restaurant); // Update MainFrameGUI with the latest data
        MainFrameGUI.getInstance(restaurant).setVisible(true); // Show updated MainFrameGUI
    }
    
    // 메뉴 항목 편집 시작
    public void toggleEditMode(int row, String mode) {
        if ("Edit".equals(mode)) {
            editModeRows.add(row);
        } else if ("Save".equals(mode)) {
            saveMenuItem(row);
            editModeRows.remove(row);
        }
        menuTableModel.fireTableRowsUpdated(row, row);
    }
    // 메뉴 항목 저장
    public void saveMenuItem(int row) {
        String menuName = (String) menuTableModel.getValueAt(row, 0);
        String priceText = (String) menuTableModel.getValueAt(row, 1);
        String field = (String) menuTableModel.getValueAt(row, 2);

        try {
            int price = Integer.parseInt(priceText); // 문자열을 정수로 변환
            Menu updatedMenu = new Menu(menuName, price, field);
            restaurant.getProgramManager().addOrUpdateMenu(updatedMenu);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "가격은 숫자로 입력해야 합니다.", "입력 오류", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "업데이트 오류", JOptionPane.ERROR_MESSAGE);
        }
    }

    // 메뉴 항목 삭제
    public void deleteMenuItem(int row) {
    	System.out.println("Deleting menu item in row " + row);
        String menuName = (String) menuTableModel.getValueAt(row, 0);
        try {
            restaurant.getProgramManager().deleteMenu(menuName);
            menuTableModel.removeRow(row);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
  
    
    
    
}
