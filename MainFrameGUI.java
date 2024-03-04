
import java.awt.*;
import java.text.SimpleDateFormat;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Date;


public class MainFrameGUI extends JFrame implements TableInteraction , RestaurantEventListener{
	private static MainFrameGUI instance;
	private JPanel panel_6; // 클래스 레벨 변수로 선언
    private Restaurant restaurant;
    
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table_4;
    
	private static final String defaultFilename = "restaurantData.ser"; 
    
	// 싱글톤 인스턴스를 반환하는 메서드
    public static MainFrameGUI getInstance(Restaurant restaurant) {
        if (instance == null) {
            instance = new MainFrameGUI(restaurant);
        }
        return instance;
    }
    
    // 생성자
    protected MainFrameGUI(Restaurant restaurant) {
    	
    	this.restaurant = restaurant; 
        initializeUI();
        restaurant.addEventListener(this);
    }
	
	private void initializeUI() { //MainFrame 내용 만들기 
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setTitle("MainFrame");
	    setBounds(100, 100, 450, 300);
	    setSize(1366, 1024);
	    contentPane = new JPanel();
	    contentPane.setBackground(new Color(255, 255, 255));
	    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
	    setContentPane(contentPane);
	    contentPane.setLayout(null);

	    // 상단 패널 생성 및 설정
	    JPanel panel = new JPanel();
	    panel.setBounds(17, 0, 1145, 60);
	    contentPane.add(panel);
	    panel.setLayout(null);

	    JPanel panel_1 = new JPanel();
		panel_1.setBounds(16, 6, 328, 49);
		panel.add(panel_1);
		panel_1.setBackground(new Color(230, 230, 250));
		
		JLabel lblNewLabel = new JLabel("Ella Restaurant");
		lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 30));
		panel_1.add(lblNewLabel);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBounds(374, 6, 325, 48);
		panel.add(panel_2);
		panel_2.setBackground(new Color(230, 230, 250));
		
		JLabel lblNewLabel_1 = new JLabel("New label");
		lblNewLabel_1.setFont(new Font("Times New Roman", Font.BOLD, 30));
		panel_2.add(lblNewLabel_1);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBounds(729, 6, 355, 49);
		panel.add(panel_3);
		panel_3.setBackground(new Color(230, 230, 250));
		panel_3.setLayout(null);
		
		JLabel lblNewLabel_2 = new JLabel("Sales:");
		lblNewLabel_2.setFont(new Font("Times New Roman", Font.BOLD, 30));
		lblNewLabel_2.setBounds(6, 5, 86, 37);
		panel_3.add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel();
		lblNewLabel_3.setFont(new Font("Times New Roman", Font.BOLD, 30));
		lblNewLabel_3.setBounds(165, 5, 61, 35);
		panel_3.add(lblNewLabel_3);
		
		// 타이머를 사용하여 라벨을 정기적으로 업데이트
		Timer timer = new Timer(1000, e -> {
		    // 현재 시간을 "HH:mm:ss" 형식으로 표시
		    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		    String currentTime = sdf.format(new Date());
		    lblNewLabel_1.setText(currentTime);
		});

		timer.start(); 
		
		//오른쪽 기능 버튼 
		JButton btnNewButton = new JButton("Menu\nEdit");
		btnNewButton.setFont(new Font("Lucida Grande", Font.PLAIN, 27));
		btnNewButton.setBounds(1188, 50, 164, 178);
		btnNewButton.setContentAreaFilled(true);
		contentPane.add(btnNewButton);
		btnNewButton.setBackground(new Color(255, 240, 245));
		// Menu Edit 버튼 이벤트 핸들러
		btnNewButton.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                MenuManagementGUI menuManagementGUI = new MenuManagementGUI(restaurant);
	                menuManagementGUI.setVisible(true);
	                
	            }
	        });
		
		JButton btnTableEdit = new JButton("Table Edit");
		btnTableEdit.setFont(new Font("Lucida Grande", Font.PLAIN, 27));
		btnTableEdit.setContentAreaFilled(true);
		btnTableEdit.setBackground(new Color(255, 240, 245));
		btnTableEdit.setBounds(1188, 254, 164, 189);
		contentPane.add(btnTableEdit);
		// Table Edit 버튼 이벤트 핸들러
		btnTableEdit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	TableManagementGUI tableManagementGUI = TableManagementGUI.getInstance(restaurant);

                tableManagementGUI.setVisible(true);
                
            }
        });
		
		JButton btnStats = new JButton("Stats");
		btnStats.setFont(new Font("Lucida Grande", Font.PLAIN, 27));
		btnStats.setBackground(new Color(255, 240, 245));
		btnStats.setBounds(1188, 462, 164, 189);
		contentPane.add(btnStats);
		
		JButton btnSaveoff = new JButton("Save&Off");
		btnSaveoff.setFont(new Font("Lucida Grande", Font.PLAIN, 27));
		btnSaveoff.setContentAreaFilled(true);
		btnSaveoff.setBackground(new Color(255, 240, 245));
		btnSaveoff.setBounds(1188, 668, 164, 189);
		contentPane.add(btnSaveoff);
		btnSaveoff.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                restaurant.savingRestaurantData(defaultFilename);
                System.exit(0);
            }
        });
		
		//JScrollPane scrollPane = new JScrollPane();
		//scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		//scrollPane.setBounds(27, 72, 1119, 794);
		//contentPane.add(scrollPane);
	
		
		//panel_6가 제대로 초기화 되었으면 좋겠다
		initializePanel6();
		
		
		
			}

	// panel_6 초기화
    private void initializePanel6() {
    	/*
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBounds(27, 72, 1119, 794);
        contentPane.add(scrollPane);
        */

        panel_6 = new JPanel();
        panel_6.setBackground(Color.WHITE);
        panel_6.setLayout(new GridLayout(0, 4, 10, 10));
        
        // 스크롤 패널에 panel_6을 추가합니다.
        JScrollPane scrollPane = new JScrollPane(panel_6);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setBounds(27, 72, 1119, 794); // 스크롤 패널의 위치와 크기를 설정합니다.
        contentPane.add(scrollPane);

        updateUI(); // UI를 업데이트하여 panel_6에 컴포넌트들을 추가합니다.
    }
	
	// 데이터 업데이트
    public void updateData(Restaurant restaurant) {
        this.restaurant = restaurant;
        System.out.println("Updating data - Restaurant tables count: " + restaurant.getTables().size());
        updateUI();
    }


    private void updateUI() {
        panel_6.removeAll();
        for (Table table : restaurant.getTables()) {
            JPanel tablePanel = createTablePanel(table);
            panel_6.add(tablePanel);

            // 테이블에 현재 인원이 있으면 핑크색으로 표시
            if (table.getCurrentPeople() > 0 || table.getAvailable()== false)  {
                tablePanel.setBackground(new Color(252,232,241));
                // 버튼의 텍스트를 '사용 인원: 실제 사용인원'으로 업데이트
                JButton actionButton = findActionButton(tablePanel);
                if (actionButton != null) {
                    actionButton.setText("<html>사용 인원: " + table.getCurrentPeople() + "명<br/>총액: " + table.getTotal() + "원</html>");
                }
            } else {
                // 인원이 없을 때는 버튼의 텍스트를 초기화
                JButton actionButton = findActionButton(tablePanel);
                if (actionButton != null) {
                    actionButton.setText("");
                }
            }
        }
        panel_6.revalidate();
        panel_6.repaint();
    }
    private JPanel findTablePanel(String tableName) {
        for (Component comp : panel_6.getComponents()) {
            if (comp instanceof JPanel && comp.getName().equals(tableName)) {
                return (JPanel) comp;
            }
        }
        return null;
    }
    private JButton findActionButton(JPanel tablePanel) {
        for (Component comp : tablePanel.getComponents()) {
            if (comp instanceof JButton) {
                return (JButton) comp;
            }
        }
        return null; // 버튼이 없으면 null 반환
    }

    // 테이블 패널 생성
    protected JPanel createTablePanel(Table table) {
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(null);
        tablePanel.setSize(new Dimension(252, 247));
        tablePanel.setPreferredSize(new Dimension(252, 247));
        
        
        // 각 테이블 패널에 이름 설정
        tablePanel.setName(table.getTableName());


        // 라벨 패널 생성 및 라벨 추가
        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(null);
        labelPanel.setBounds(10, 10, 247, 45);
        labelPanel.setBackground(Color.WHITE);
        
        JLabel tableNameLabel = new JLabel("Table: " + table.getTableName());
        tableNameLabel.setBounds(2, 2, 130, 40);
        labelPanel.add(tableNameLabel);
        
        

        // 인원수 라벨-'Capacity: member명' 
        JLabel tableCapacityLabel = new JLabel("Capacity: " + table.getMember() + "명");
        tableCapacityLabel.setBounds(132, 2, 115, 40);
        labelPanel.add(tableCapacityLabel);

        tablePanel.add(labelPanel);

        /*// 버튼 패널 생성 및 버튼 추가
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(null); // Absolute 레이아웃
        buttonPanel.setBounds(8, 64, 247, 150 ); // 위치와 크기 설정
        //buttonPanel.setBackground(new Color(230, 230, 250)); // 버튼 패널 색상 설정
         
         */

     // 테이블 선택 버튼 추가
        JButton actionButton = new JButton();
        actionButton.setBounds(8, 64, 249, 150); // 버튼 위치와 크기 설정
        actionButton.setFont(new Font("Lucida Grande", Font.PLAIN, 27));
        actionButton.addActionListener(e -> handlePanelClick(table));
        tablePanel.add(actionButton);
        
        updateButtonLabel(table, actionButton);

   

        //tablePanel.add(buttonPanel); // 버튼 패널을 배경 패널에 추가
        
        // 라벨 패널 로그 출력
        System.out.println("Label Panel - Bounds: " + labelPanel.getBounds());
        
        
        return tablePanel;
    }
    private JButton findTableButton(Table table) {
        for (Component comp : panel_6.getComponents()) {
            if (comp instanceof JButton && comp.getName().equals(table.getTableName())) {
                return (JButton) comp;
            }
        }
        return null;
    }
    private void updateButtonLabel(Table table, JButton button) {
        if (table.getCurrentPeople() > 0) {
            button.setText("<html>인원: " + table.getCurrentPeople() + "<br>총액: " + table.getTotal() + "원</html>");
        } else {
            button.setText(""); // 버튼 텍스트 제거
        }
    }
    

    private void handlePanelClick(Table table) {
        if (table.getCurrentPeople() > 0) {
            PaymentGUI paymentGUI = new PaymentGUI(restaurant, table);
            paymentGUI.setVisible(true);
        } else {
            NumberInputDialog dialog = new NumberInputDialog(this, "실사용 인원수 입력", table.getMember());
            dialog.setVisible(true);

            if (dialog.isConfirmed()) {
                int peopleCount = dialog.getNumber();
                if (peopleCount <= table.getMember()) {
                	table.setAvailable(false);
                    table.setCurrentPeople(peopleCount);
                    
                    updateTableStatus(table, peopleCount); // 테이블 상태 업데이트
                } else {
                    JOptionPane.showMessageDialog(this, "테이블 정원 초과!!!");
                }
            }
        }
    }

    
    private JLabel findLabel(JPanel panel, String prefix) {
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JLabel) {
                JLabel label = (JLabel) comp;
                if (label.getText().startsWith(prefix)) {
                    return label;
                }
            }
        }
        return null;
    }

    private void updateTableStatus(Table table, int peopleCount) {
        SwingUtilities.invokeLater(() -> {
            JPanel tablePanel = findTablePanel(table.getTableName());
            if (tablePanel != null) {
                updateTablePanelLabel(tablePanel, "Used: " + peopleCount + "/" + table.getMember() + "명");
                tablePanel.setBackground(new Color(252,232,241));

                JButton tableButton = findActionButton(tablePanel);
                if (tableButton != null) {
                    tableButton.setText("<html>사용 인원: " + peopleCount + "명<br/>총액: " + table.getTotal() + "원</html>");
                }
            }
        });
    }
    private void updateTablePanelLabel(JPanel tablePanel, String newText) {
        for (Component comp : tablePanel.getComponents()) {
            if (comp instanceof JLabel) {
                JLabel label = (JLabel) comp;
                if (label.getText().startsWith("Capacity:") || label.getText().startsWith("Used:")) {
                    label.setText(newText);
                    return;
                }
            }
        }
    }

    public void resetTablePanel(Table table) {
        SwingUtilities.invokeLater(() -> {
            for (Component comp : panel_6.getComponents()) {
                if (comp instanceof JPanel && comp.getName().equals(table.getTableName())) {
                    JPanel tablePanel = (JPanel) comp;
                    updateTablePanelLabel(tablePanel, "Capacity: " + table.getMember() + "명");
                    tablePanel.setBackground(new Color(238,238,238));
                    tablePanel.revalidate();
                    tablePanel.repaint();
                    break;
                }
            }
        });
    }



    
    class NumberInputDialog extends JDialog {
        private int number = 0;
        private boolean confirmed = false;

        public NumberInputDialog(JFrame parent, String title, int maxNumber) {
            super(parent, title, true);
            
            System.out.println("NumberInputDialog constructor called");
            setLayout(null); // 레이아웃 매니저를 absolute
            // 중앙에 표시될 숫자 필드
            JTextField numberField = new JTextField();
            numberField.setEditable(false);
            numberField.setHorizontalAlignment(JTextField.CENTER);
            numberField.setFont(new Font("SansSerif", Font.BOLD, 20));
            numberField.setBounds(10, 10, 180, 30);
            add(numberField);

            // 버튼들을 배치할 패널
            JPanel buttonPanel = new JPanel(new GridLayout(4, 3, 10, 10)); // 4x3 그리드
            buttonPanel.setBounds(10, 50, 180, 200); // 패널의 위치와 크기 설정

            // 숫자 버튼 추가
            for (int i = 1; i <= 9; i++) {
                JButton btn = new JButton(String.valueOf(i));
                btn.setFont(new Font("SansSerif", Font.BOLD, 20));
                btn.addActionListener(e -> numberField.setText(numberField.getText() + btn.getText()));
                buttonPanel.add(btn);
            }
            
            // 삭제 버튼
            JButton deleteBtn = new JButton("취소");
            deleteBtn.setFont(new Font("SansSerif", Font.BOLD, 20));
            deleteBtn.addActionListener(e -> numberField.setText(""));
            buttonPanel.add(deleteBtn);


            // 0 버튼 추가
            JButton zeroButton = new JButton("0");
            zeroButton.setFont(new Font("SansSerif", Font.BOLD, 20));
            zeroButton.addActionListener(e -> numberField.setText(numberField.getText() + zeroButton.getText()));
            buttonPanel.add(zeroButton);

           
            // 확인 버튼
            JButton confirmBtn = new JButton("확인");
            confirmBtn.setFont(new Font("SansSerif", Font.BOLD, 20));
            confirmBtn.addActionListener(e -> {
                try {
                    number = Integer.parseInt(numberField.getText());
                    confirmed = true;
                    setVisible(false);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid number format.");
                }
            });
            buttonPanel.add(confirmBtn);

            // 패널을 다이얼로그에 추가
            add(buttonPanel);

            // 다이얼로그 사이즈 설정
            setSize(210, 300); 
            setLocationRelativeTo(parent);
        }

        public boolean isConfirmed() {
            return confirmed;
        }

        public int getNumber() {
            return number;
        }
    }



	
 



	@Override
    public void onDataChanged() {
        updateUI();
    }

	@Override
	public void toggleEditMode(int row, String actionType) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteTableItem(int row) {
		// TODO Auto-generated method stub
		
	}
}
