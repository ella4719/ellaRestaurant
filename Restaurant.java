import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;



public class Restaurant implements Serializable {
	private static final long serialVersionUID = 1L; 
	// Menu 객체들로 이루어진 리스트
	protected ArrayList<Menu> menus;
	// Table 객체들로 이루어진 리스트
	protected ArrayList<Table> tables;
	private transient List<RestaurantEventListener> listeners = new ArrayList<>();
	private static final Logger LOGGER = Logger.getLogger(Restaurant.class.getName());
	
	
	// 매출액 변수
	protected int amount=0;
	
	// Restaurant 기본 생성자 함수
	public Restaurant() {
		 this.tables = new ArrayList<Table>();	// 테이블 배열 생성
		 this.menus = new ArrayList<Menu>();		// 메뉴 배열 생성
	}
		
	public Restaurant(String defaultFilename) {
		File file = new File(defaultFilename);
        if (file.exists()) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(defaultFilename))) {
                Restaurant loadedRestaurant = (Restaurant) in.readObject();
                this.menus = loadedRestaurant.getMenus();
                this.tables = loadedRestaurant.getTables();
                this.amount = loadedRestaurant.getAmount();
                // 다른 필요한 상태도 여기서 설정
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                // 여기서는 예외 처리를 적절하게 
            }
        } else {
            // 파일이 없으면 기본 상태로 초기화
            this.menus = new ArrayList<>();
            this.tables = new ArrayList<>();
            this.amount = 0;
           
        }
    }

	// menus 접근자
	ArrayList<Menu> getMenus() {
		return menus;
	}
	// tables 접근자
	ArrayList<Table> getTables() {
		return tables;
	}
	// 특정 테이블 객체의 인덱스를 반환하는 메소드
    public int getTableIndex(Table table) {
        return tables.indexOf(table);
    }
	
	// amount 접근자
	int getAmount() {
		return amount;
	}
	
	
	public void addEventListener(RestaurantEventListener listener) {
        listeners.add(listener);
    }
    public void removeEventListener(RestaurantEventListener listener) {
        listeners.remove(listener);
    }
    private void notifyListeners() {
        for (RestaurantEventListener listener : listeners) {
            listener.onDataChanged();
        }
    }
	
	
	// 메뉴 객체 추가
	public void addMenu(Menu m) throws Exception {
        if (menus.contains(m)) {
            throw new Exception("The menu already exists");
        }
        menus.add(m);
        notifyListeners();
  
    }
	

	// 테이블 객체 추가
	public void addTable(Table t) throws Exception {
        if (tables.contains(t)) {
            throw new Exception("The table already exists. Please enter a different name.");
        }
        tables.add(t);
        notifyListeners();
    }

	// 메뉴 객체 삭제
	public void deleteMenu(Menu m) throws Exception {
        if (!menus.remove(m)) {
	        throw new Exception("The menu does not exist");  // 삭제하려는 메뉴가 없는 경우 예외 발생
	    }
        notifyListeners();
	}
	//메뉴 전체 삭제
	public void clearMenus() {
	    menus.clear(); // 모든 메뉴 삭제
	    notifyListeners(); // 리스너에게 변경 사항 알림
	}


	// 테이블 객체 삭제
	public void deleteTable(Table t) throws Exception {
        if (!tables.remove(t)) {
	        throw new Exception("The table does not exist");  // 삭제하려는 테이블이 없는 경우 예외 발생
	    }
        notifyListeners();
	}
	
	


	
	public void addAmount(int paid) {
		amount += paid;
	}
 	
	
	
	public Order menuToOrder(Menu m) {
		return new Order(m.getMenuName(), m.getPrice(), 0);
	}
	
	
	// 계산
	public int pay(Table t) {
		int paid = t.outTable();
		addAmount(paid);
		return paid;
	}
	// 메뉴 이름을 기반으로 메뉴 인덱스 찾기
    public int findMenuIndexByName(String menuName) {
        for (int i = 0; i < menus.size(); i++) {
            if (menus.get(i).getMenuName().equals(menuName)) {
                return i;
            }
        }
        return -1; // 메뉴가 목록에 없으면 -1 반환
    }
	
	// 유저 인터페이스에서 메뉴,테이블 리스트를 출력할 때, 필요한 문자열을 반환하는 매소드. 출력은 인터페이스에서
	public String getMenuList() {
	    StringBuilder menuList = new StringBuilder();
	    for (int i = 0; i < menus.size(); i++) {
	        menuList.append(i).append(". ").append(menus.get(i)).append("\n");
	    }
	    return menuList.toString();
	}
	//테이블 리스트 문자열 반
	public String getTableList() {
	    StringBuilder tableList = new StringBuilder();
	    for (int i = 0; i < tables.size(); i++) {
	    	tableList.append(i).append(". ").append(tables.get(i)).append("\n");
	    }
	    return tableList.toString();
	}


	//테이블-주문 리스트 문자열 반환 
	public String getTableOrders(int tableIndex) throws Exception {
	    if (tableIndex >= 0 && tableIndex < tables.size()) {
	        Table table = tables.get(tableIndex);
	        StringBuilder sb = new StringBuilder();
	        sb.append("Orders for ").append(table.getTableName()).append(":\n");
	        for (Order order : table.getOrders()) {
	            sb.append(order).append("\n");
	        }
	        return sb.toString();
	    } else {
	        throw new Exception("Invalid table index.");
	    }
	}
	


	public void saveRestaurant(ObjectOutputStream out) throws IOException {
	    // 메뉴 정보 저장
	    out.writeInt(menus.size());
	    for(Menu menu: menus) {
	        out.writeObject(menu);
	    }

	    // 테이블 정보 저장
	    out.writeInt(tables.size());
	    for(Table table: tables) {
	        out.writeObject(table);
	    }
	}

    
	public void loadRestaurant(ObjectInputStream in) throws IOException, ClassNotFoundException {
	    // 메뉴 정보 불러오기
	    int menuSize = in.readInt();
	    for(int i = 0; i < menuSize; i++) {
	        Menu menu = (Menu) in.readObject();
	        try {
	            addMenu(menu); // 메뉴 추가
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

	    // 테이블 정보 불러오기
	    int tableSize = in.readInt();
	    for(int i = 0; i < tableSize; i++) {
	        Table table = (Table) in.readObject();
	        try {
	            addTable(table); // 테이블 추가
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	}
	
	public void savingRestaurantData(String filename) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(this);
            LOGGER.info("Data has been saved successfully to " + filename);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "An IO error occurred while saving data to " + filename, e);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An error occurred while saving data to " + filename, e);
        }
    }
	
	public void loadRestaurantData(String filename) {
	    try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
	        // 파일로부터 객체 읽어오기
	        Restaurant loadedRestaurant = (Restaurant) in.readObject();

	        // 현재 객체의 상태를 읽어온 객체의 상태로 업데이트
	        this.menus = loadedRestaurant.getMenus();
	        this.tables = loadedRestaurant.getTables();
	        this.amount = loadedRestaurant.getAmount();

	        System.out.println("Data has been loaded successfully from " + filename + ".");
	    } catch (IOException e) {
	        System.err.println("An IO error occurred while loading data from " + filename + ".");
	        e.printStackTrace();
	    } catch (ClassNotFoundException e) {
	        System.err.println("A ClassNotFound error occurred while loading data from " + filename + ".");
	        e.printStackTrace();
	    } catch (Exception e) {
	        System.err.println("An error occurred while loading data from " + filename + ".");
	        e.printStackTrace();
	    }
	}


	
	
	public void addMenuToField(String fieldName, Menu menu) throws Exception {
	    if (menus.contains(menu)) {
	        throw new Exception("The menu already exists");
	    }
	    menu.setField(fieldName); // 필드 이름 설정
	    menus.add(menu); // 메뉴 추가
	    notifyListeners();
	}

	public void deleteMenuFromField(String fieldName, String menuName) throws Exception {
	    Menu toRemove = null;
	    for (Menu menu : menus) {
	        if (menu.getField().equals(fieldName) && menu.getMenuName().equals(menuName)) {
	            toRemove = menu;
	            break;
	        }
	    }
	    if (toRemove != null) {
	        menus.remove(toRemove);
	        notifyListeners();
	    } else {
	        throw new Exception("Menu not found in the field");
	    }
	}
	

	public List<Menu> getMenusByField(String fieldName) {
	    List<Menu> fieldMenus = new ArrayList<>();
	    for (Menu menu : menus) {
	        if (menu.getField().equals(fieldName)) {
	            fieldMenus.add(menu);
	        }
	    }
	    return fieldMenus;
	}
	public String getMyFileNameWithMenuName(String menuName) {
		String myFieldName= "";
		for (Menu menu : menus) {
	        if (menu.getMenuName().equals(menuName)) {       	
	        	myFieldName = menu.getField();
	        	
	            break;
	        }
	    }
		return myFieldName;
	}
	
	 private transient ProgramManager programManager = new ProgramManager();

	    public ProgramManager getProgramManager() {
	        return programManager;
	    }
	
	// 내부 클래스로 ProgramManager 추가
    public class ProgramManager implements Serializable  {
    	private transient ProgramManager programManager;
    	 // 테이블 추가 또는 업데이트
    	public void addOrUpdateTable(int rowIndex, Table table) throws Exception {
    	    if (rowIndex >= 0 && rowIndex < tables.size()) {
    	        Table existingTable = tables.get(rowIndex);
    	        if (!existingTable.getTableName().equals(table.getTableName())) {
    	            // 이름이 변경 확인
    	            if (findTableIndex(table.getTableName()) != -1) {
    	                throw new Exception("Table name already exists. Please use a different name.");
    	            }
    	        }
    	        // 테이블 정보 업데이트
    	        tables.set(rowIndex, table);
    	    } else {
    	        // 신규 테이블 추가
    	        if (findTableIndex(table.getTableName()) != -1) {
    	            throw new Exception("Table name already exists. Please use a different name.");
    	        }
    	        tables.add(table);
    	    }
    	    notifyListeners();
    	}

    	// 테이블 삭제
        public void deleteTable(String tableName) throws Exception {
            int existingIndex = findTableIndex(tableName);
            if (existingIndex < 0) {
                throw new Exception("Table not found: " + tableName);
            }
            tables.remove(existingIndex);
            notifyListeners();
        }
        // 메뉴 추가 또는 업데이트
        public void addOrUpdateMenu(Menu menu) throws Exception {
            int existingIndex = findMenuIndex(menu.getMenuName());
            if (existingIndex >= 0) {
                menus.set(existingIndex, menu);
            } else {
                menus.add(menu);
            }
            notifyListeners();
        }
    	// 메뉴 삭제
        public void deleteMenu(String menuName) throws Exception {
            int existingIndex = findMenuIndex(menuName);
            if (existingIndex < 0) {
                throw new Exception("Menu not found: " + menuName);
            }
            menus.remove(existingIndex);
            notifyListeners();
        }
        // 테이블 인덱스 찾기
        private int findTableIndex(String tableName) {
            for (int i = 0; i < tables.size(); i++) {
                if (tables.get(i).getTableName().equals(tableName)) {
                    return i;
                }
            }
            return -1;
        }

        // 메뉴 인덱스 찾기
        private int findMenuIndex(String menuName) {
            for (int i = 0; i < menus.size(); i++) {
                if (menus.get(i).getMenuName().equals(menuName)) {
                    return i;
                }
            }
            return -1;
        }

        


        // 주문 추가,수량 변경-paymentGUI
    	public boolean addOrderForGUI(int tableIndex, String menuName, int quantity) {
    		int menuIndex=findMenuIndexByName(menuName);
    	    if (tableIndex < 0 || tableIndex >= tables.size() || menuIndex < 0 || menuIndex >= menus.size()) {
    	        return false; // 유효하지 않은 인덱스 처리
    	    }
    	    Table table = tables.get(tableIndex);
    	    
    	    Menu menu = menus.get(menuIndex);
    	    
    	    // 이미 존재하는 주문 찾기
    	    int orderIndex = table.searchOrder(new Order(menu.getMenuName(), menu.getPrice(),1));
    	    if (orderIndex >= 0) {
    	        // 주문이 이미 있는 경우, 수량 업데이트
    	        table.getOrders().get(orderIndex).addOrderCount(quantity);
    	    } else {
    	        // 새 주문 추가
    	        Order newOrder = new Order(menu.getMenuName(), menu.getPrice(), quantity);
    	        table.addOrder(newOrder);
    	    }
    	    notifyListeners();
    	    return true;
    	}


        // 기존 주문서에 올라온 메뉴 수량 추가 및 감소
        public boolean updateOrderQuantity(int tableIndex, int orderIndex, int quantityChange) {
        	
            if (tableIndex < 0 || tableIndex >= tables.size()) {
                return false; // 유효하지 않은 인덱스 처리
            }
            Table table = tables.get(tableIndex);
            if (orderIndex < 0 || orderIndex >= table.getOrders().size()) {
                return false; // 유효하지 않은 인덱스 처리
            }
            Order order = table.getOrders().get(orderIndex);
            int newQuantity = order.getOrderCount() + quantityChange;
            if (newQuantity < 0) {
                return false; // 수량이 0보다 작아지는 경우 처리
            }
            order.setOrderCount(newQuantity);
            notifyListeners();
            return true;
        }

        // 기존 주문 메뉴들 중 특정 메뉴 삭제
        public boolean removeOrderItem(int tableIndex, int orderIndex) {
            if (tableIndex < 0 || tableIndex >= tables.size()) {
                return false; // 유효하지 않은 인덱스 처리
            }
            Table table = tables.get(tableIndex);
            if (orderIndex < 0 || orderIndex >= table.getOrders().size()) {
                return false; // 유효하지 않은 인덱스 처리
            }
            table.getOrders().remove(orderIndex);
            notifyListeners();
            return true;
        }

        // 주문 전체 삭제
        public boolean clearOrders(int tableIndex) {
            if (tableIndex < 0 || tableIndex >= tables.size()) {
                return false; // 유효하지 않은 인덱스 처리
            }
            Table table = tables.get(tableIndex);
            table.clearOrders();
            notifyListeners();
            return true;
        }
        public void updateMenu(String menuName, Menu updatedMenu) throws Exception {
            // menus 리스트에서 menuName과 일치하는 메뉴를 찾습니다.
            for (int i = 0; i < menus.size(); i++) {
                Menu menu = menus.get(i);
                if (menu.getMenuName().equals(menuName)) {
                    // 일치하는 메뉴를 찾았으면, 새 정보로 업데이트합니다.
                    menus.set(i, updatedMenu);
                    notifyListeners(); // 변경 사항 리스너에게 알림
                    return;
                }
            }
            throw new Exception("Menu not found: " + menuName); // 메뉴가 목록에 없는 경우 예외 발생
        }

        //지정 필드 속 모든 메뉴 menus에서 삭제 
        public void deleteMenusByField(String selectedField) {
            if (selectedField == null || selectedField.trim().isEmpty()) {
                throw new IllegalArgumentException("Field name cannot be null or empty.");
            }

            menus.removeIf(menu -> selectedField.equals(menu.getField())); // 지정된 필드의 메뉴 삭제
            notifyListeners(); // 리스너에게 변경 사항 알림
        }

     
    }
}
	
	
	