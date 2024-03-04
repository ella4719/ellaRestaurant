import java.io.*;
import java.util.Objects;

public class Order extends Menu implements Serializable{
	int orderCount;
	Order(String menuName, int price, int orderCount) {
		super(menuName, price);
		this.orderCount = orderCount; // orderCount를 초기화
	}
	
	// 기본 생성자
    public Order() {
        super("", 0);
        this.orderCount = 0;
    }
	
	// 메뉴 주문량 접근자
	int getOrderCount() {
		return orderCount;
	}
	
	// 메뉴 주문량 설정자
	void setOrderCount(int orderCount) {
		this.orderCount = orderCount;
	}
	
	// 주문량 추가 함수
	void addOrderCount(int n) {
		orderCount += n;
	}
		
	// 주문량 감소 함수
	void minusOrderCount(int n) {
		if (orderCount > n) {
			orderCount -= n;
		}
	}
	
	int getOrderPay() {
		return price * orderCount;
	}
	
	protected void writeObject(ObjectOutputStream out) throws IOException {
        super.writeObject(out); // 부모 클래스의 writeObject 호출
        out.writeInt(orderCount); // 주문 수량 저장
    }

    protected void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        super.readObject(in); // 부모 클래스의 readObject 호출
        orderCount = in.readInt(); // 주문 수량 읽기
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Order other = (Order) obj;
        return Objects.equals(menuName, other.menuName);
    }
    
    public String getOrderInfo() {
        return menuName + "," + price + "," + orderCount;
    }
    
	// toString() 함수 재정의
	public String toString() {
		return menuName + " : " + price + " / " +orderCount;
	}
}
