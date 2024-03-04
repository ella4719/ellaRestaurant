import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.util.Vector;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class PaymentGUI extends JFrame {
    private JTable table1, table2;
    private DefaultTableModel tableModel1, tableModel2; 
    private Restaurant restaurant; // Restaurant instance
    private Table table;
    private int tableIndex; 
	private JLabel totalAmountLabel;
	
    
    // Restaurant 객체만 받는 생성자 추가
    public PaymentGUI(Restaurant restaurant) {
        this.restaurant = restaurant;
        
    }

    public PaymentGUI(Restaurant restaurant, Table table) {
        this.restaurant = restaurant;
        this.table = table;
        this.tableIndex = restaurant.getTableIndex(table);
        setSize(1366, 1024);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);        
        
        // Panels
        JPanel headerPanel = createPanel(0, 0, 1000, 86, Color.LIGHT_GRAY);
        headerPanel.setLayout(null); 
        JPanel tableInfoPanel = createPanel(18, 10, 335, 69, 239, 224, 245);
        JPanel peopleInfoPanel = createPanel(380, 7, 126, 69, 239, 224, 245);
        JPanel totalAmountPanel = createPanel(631, 10, 263, 69, 239, 224, 245);
        
        
        // Labels
        JLabel tableLabel = createLabel("테이블: " + table.getTableName() , 39, 17, 278, 48, 40);
        JLabel peopleLabel = createLabel(table.getCurrentPeople() + "인", 404, 17, 85, 48, 40);
        //JLabel totalAmountLabel = createLabel("총액:"+String.valueOf(table.getTotal())+"원", 538, 17, 87, 48, 40);
        totalAmountLabel = createLabel("총액:"+String.valueOf(table.getTotal())+"원", 538, 17, 87, 48, 40);
        totalAmountPanel.add(totalAmountLabel);
        
        //Buttons and event handler..
        JButton plusButton = createButton("+", 348, 680, 173, 86, 36);
        plusButton.addActionListener(e -> updateOrderQuantity(1));
        JButton subButton = createButton("-", 348+174, 680, 173, 86, 36);
        subButton.addActionListener(e -> updateOrderQuantity(-1));
        JButton deletOrderRowButton = createButton("주문 취소하기", 0, 680, 347, 86, 36);
        deletOrderRowButton.addActionListener(e -> deleteSelectedOrder());
        JButton paymentButton = createButton("결제하기", 0, 770, 347, 86, 36);
        paymentButton.addActionListener(e -> performPayment());

        ImageIcon homeIcon = new ImageIcon("./Image/home-icon.png");
        JButton homeButton = new JButton(homeIcon);
        homeButton.setBounds(1279, 0, 86, 86);
        homeButton.addActionListener(e -> returnHome());
        
        // Tables
        initializeOrderTable();
        initializeMenuTable();

        // Add components to panels
        tableInfoPanel.add(tableLabel);
        peopleInfoPanel.add(peopleLabel);
        totalAmountPanel.add(totalAmountLabel);
        
        headerPanel.add(tableInfoPanel);
        headerPanel.add(peopleInfoPanel);
        headerPanel.add(totalAmountPanel);
        
        
        // Add panels and buttons to frame
        add(headerPanel);
        add(homeButton);
        add(plusButton);
        add(subButton);
        add(deletOrderRowButton);
        add(paymentButton);
    }
    
    private void initializeMenuTable() {
        JTabbedPane tabbedPane = new JTabbedPane();

        // 필드별로 메뉴 탭 생성
        Set<String> fields = restaurant.getMenus().stream()
                                        .map(Menu::getField)
                                        .collect(Collectors.toSet());
        for (String field : fields) {
            JPanel panel = new JPanel(new GridLayout(0, 4)); // 가로로 4개의 메뉴가 표시되도록 설정
            List<Menu> fieldMenus = restaurant.getMenusByField(field);

            for (Menu menu : fieldMenus) {
                if (!menu.getMenuName().equals("Dummy")) {
                    JButton button = new JButton("<html>" + menu.getMenuName() + "<br>" + menu.getPrice() + "원</html>");
                    button.addActionListener(e -> addOrderToTable(menu));
                    panel.add(button);
                }
            }

            tabbedPane.addTab(field, panel);
        }

        JScrollPane scrollPane = new JScrollPane(tabbedPane);
        scrollPane.setBounds(701, 86, 666, 800);
        add(scrollPane);
    }

    private void addOrderToTable(Menu menu) {
        if (tableIndex != -1 ) {
            // 새로운 주문 추가 또는 기존 주문 업데이트
            restaurant.getProgramManager().addOrderForGUI(tableIndex, menu.getMenuName(), 1);
            updateOrderTable();
            updateTotalAmountLabel();
        } else {
            JOptionPane.showMessageDialog(this, "오류: 선택한 테이블 또는 메뉴가 유효하지 않습니다.", "주문 오류", JOptionPane.ERROR_MESSAGE);
        }
    }




    private void initializeOrderTable() {
        String[] orderColumns = {"메뉴", "수량", "가격"}; 
        tableModel1 = new DefaultTableModel(orderColumns, 0);
        table1 = new JTable(tableModel1);
        table1.setRowHeight(40);
        updateOrderTable();
        JScrollPane scrollPane1 = new JScrollPane(table1);
        scrollPane1.setBounds(0, 86, 700, 558);
        add(scrollPane1);
    }
    private void updateOrderQuantity(int quantityChange) {
        int selectedRow = table1.getSelectedRow();
        if (selectedRow >= 0) {
            String menuName = (String) tableModel1.getValueAt(selectedRow, 0);
            
        restaurant.getProgramManager().addOrderForGUI(tableIndex, menuName, quantityChange);
        updateOrderTable();
        updateTotalAmountLabel();
            
        }
    }
    private void updateOrderTable() {
        tableModel1.setRowCount(0); 
        for (Order order : table.getOrders()) {
            Vector<Object> row = new Vector<>();
            row.add(order.getMenuName());
            row.add(order.getOrderCount());
            row.add(order.getPrice() * order.getOrderCount());
            tableModel1.addRow(row);
        }
    }

    private void deleteSelectedOrder() {
        int selectedRow = table1.getSelectedRow();
        if (selectedRow >= 0) {
            String menuName = (String) tableModel1.getValueAt(selectedRow, 0);
            int orderIndex = findOrderIndexByName(menuName);
            if (orderIndex >= 0) {
                restaurant.getProgramManager().removeOrderItem(restaurant.getTableIndex(table), orderIndex);
                updateOrderTable();
                updateTotalAmountLabel();
            }
        }
    }

    private void updateTotalAmountLabel() {
        if (totalAmountLabel != null) {
            totalAmountLabel.setText("총액:" + table.getTotal() + "원");
        }
    }


    private int findOrderIndexByName(String menuName) {
        List<Order> orders = table.getOrders();
        for (int i = 0; i < orders.size(); i++) {
            if (orders.get(i).getMenuName().equals(menuName)) {
                return i;
            }
        }
        return -1;
    }

    private void populateOrderTable() {
        for (Order order : table.getOrders()) {
            Vector<Object> row = new Vector<>();
            row.add(order.getMenuName());      
            row.add(order.getOrderCount());    
            row.add(order.getPrice());         
            tableModel1.addRow(row);
        }
    }

    private void populateMenuTable() {
        for (Menu menu : restaurant.getMenus()) {
            Vector<Object> row = new Vector<>();
            row.add(menu.getMenuName());
            row.add(menu.getPrice());
            tableModel2.addRow(row);
        }
    }

    private void performPayment() {
        int totalPaid = restaurant.pay(table);
        table.setCurrentPeople(0);

        // 결제 성공 메시지 표시
        JOptionPane.showMessageDialog(this, table.getTableName() + " 테이블 " + totalPaid + "원 결제 성공! 홈으로 돌아갑니다", 
                                      "결제 성공", JOptionPane.INFORMATION_MESSAGE);

        // 1초 후에 자동으로 홈으로 이동
        Timer timer = new Timer(1000, e -> returnHome());
        timer.setRepeats(false); // 타이머가 한 번만 실행되도록 설정
        timer.start();
    }


    private void returnHome() {
        this.dispose();
        MainFrameGUI.getInstance(restaurant).updateData(restaurant); // Update MainFrameGUI with the latest data
        MainFrameGUI.getInstance(restaurant).setVisible(true); // Show updated MainFrameGUI
    }

    private JPanel createPanel(int x, int y, int width, int height, Color color) {
        JPanel panel = new JPanel();
        panel.setBounds(x, y, width, height);
        panel.setBackground(color);
        
        return panel;
    }
    
    private JPanel createPanel(int x, int y, int width, int height, int c1, int c2, int c3) {
        JPanel panel = new JPanel();
        panel.setBounds(x, y, width, height);
        panel.setBackground(new Color(c1,c2,c3));
        
        return panel;
    }

    private JLabel createLabel(String text, int x, int y, int width, int height, int fontSize) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, width, height);
        label.setFont(new Font("Inter", Font.PLAIN, fontSize));
        return label;
    }

    private JButton createButton(String text, int x, int y, int width, int height, int fontSize) {
        JButton button = new JButton(text);
        button.setBounds(x, y, width, height);
        button.setFont(new Font("Inter", Font.PLAIN, fontSize));
        button.setBackground(new Color(239, 224, 245));
        return button;
    }
    
}
