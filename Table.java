// Table 클래스
import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

public class Table implements Serializable {
	private String tableName;	// 테이블 번호 변수
	private int member=0;	// 수용 가능 인원 변수
	private boolean available = true;	// 테이블 이용 가능 여부 변수
	private ArrayList<Order> orders = new ArrayList<>(); // 주문 내역 리스트 변수
	private int currentPeople; // 현재 사용 인원 필드 추가

	
	// 기본 생성자
    Table() {
        
    }
	// 테이블 생성자
	Table(String tableName){
		this.tableName = tableName;
	}
		
	// 테이블 생성자
	Table(String tableName, int member, boolean available){
		this.tableName = tableName;
		this.member = member;
		this.available = available;
	}
	// 테이블 생성자
	Table(String tableName, int member, boolean available, int currentPeople){
		this.tableName = tableName;
		this.member = member;
		this.available = available;
	}
	
			
	// 테이블명 접근자
	String getTableName() {
		return tableName;
	}
	
	// 수용 가능 인원 접근자
	int getMember() {
		return member;
	}
	
	int getCurrentPeople() {
		return currentPeople;
	}
	
	// 이용 가능 여부 접근자
	boolean getAvailable() {
		return available;
	}
	
	// 테이블명 설정자
	void setName(String tableName) {
		this.tableName = tableName;
	}
	
	// 수용 가능 인원 설정자
	void setMember(int member) {
		this.member = member;
	}
	
	// 이용 가능 여부 설정자
	void setAvailable(boolean tf) {
		this.available = tf;
	}
	
	// 현재 사용 인원 설정 메서드
	public void setCurrentPeople(int people) {
		this.currentPeople = people;
		}
			
	/*
	// 주문 추가 함수
	void addOrder(Order order, int n) {
	    int search = searchOrder(order);
	    // 이전에 주문했던 메뉴가 아닐 경우
	    if (search == -1) {
	        orders.add(order); // orders ArrayList에 새로운 주문을 추가합니다.
	        search = orders.size() - 1; // search 값을 마지막 인덱스 값으로 바꿉니다.
	    }
	    orders.get(search).addOrderCount(n); // 주문한 해당 메뉴의 주문량을 n 증가합니다.
	}
	*/
	// 주문 추가 함수
	void addOrder(Order order) {
	    int search = searchOrder(order);
	    // 이전에 주문했던 메뉴가 아닐 경우
	    if (search == -1) {
	        orders.add(order); // orders ArrayList에 새로운 주문을 추가합니다.
	    } else {
	        // 이전에 주문했던 메뉴인 경우, 주문량을 1 증가합니다.
	        orders.get(search).addOrderCount(1); // 주문한 해당 메뉴의 주문량을 1 증가합니다.
	    }
	}

	
	// 이 테이블의 모든 주문을 삭제
    public void clearOrders() {
        orders.clear();
    }
    

	// 메뉴 객체 탐색
	public int searchOrder(Order target) {
	    return orders.indexOf(target); // orders ArrayList에서 target Order의 인덱스를 반환합니다.
	}
	
	// 테이블에 손님이 들어온 경우
	int inTable() {
		if (available == true) {
			available = false;	// 이용 불가능으로 변경
			return 0;
		}
		else {
			return -1;
		}
	}
	
	// 손님이 계산을 마치고 나갈 경우
	int outTable() {
		int paid = getTotal();	// paid 변수에 total 값을 대입(total은 0으로 초기화할 것이기 때문에)
		available = true;	// 이용 가능으로 변경	 
		orders.clear();	//  clear 메서드를 사용하여 모든 주문을 제거
		return paid;
	}
	
	// 주문 내역을 보여주는 함수
	ArrayList<Order> getOrders() { // ArrayList<Order> 반환
        return orders;
    }
	
	// total 접근자
	int getTotal() {
		int total = 0;
        for (Order order : orders) { // for-each 루프를 사용하여 주문을 순회합니다.
            total += order.getOrderPay();
        }
        return total;
	}
	
	private void writeObject(ObjectOutputStream out) throws IOException {
	    out.writeUTF(tableName); // 테이블 이름 저장
	    out.writeInt(member); // 수용 인원 저장
	    out.writeBoolean(available); // 이용 가능 여부 저장
	    out.writeInt(currentPeople);
	    out.writeObject(orders); // 주문 내역 저장
	}

	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
	    tableName = in.readUTF(); // 테이블 이름 읽기
	    member = in.readInt(); // 수용 인원 읽기
	    available = in.readBoolean(); // 이용 가능 여부 읽기
	    currentPeople=in.readInt();
	    orders = (ArrayList<Order>) in.readObject(); // 주문 내역 읽기
	}


	
	@Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Table other = (Table) obj;
        return Objects.equals(tableName, other.tableName);
    }
    
	// toString() 재정의
	public String toString() {
		return tableName+"("+member+"): " + available;
	}
	
}
