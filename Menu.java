import java.io.*;
import java.util.Objects;

public class Menu implements Serializable {
	protected String menuName = "";
	protected int price = 0;
	 protected String field = ""; // 메뉴 필드 추가
	
	// 기본 생성자
    Menu() {
        
    }
	//menu constructor : name only
	Menu(String menuName){
		this.menuName = menuName;
	}
	//menu constructor : name, price
	Menu(String menuName, int price){
		this.menuName = menuName;
		this.price = price;
	}
	// Menu constructor with field
    Menu(String menuName, int price, String field){
        this.menuName = menuName;
        this.price = price;
        this.field = field; // 필드 초기화
    }

    // Field getter and setter
    void setField(String field) {
        this.field = field;
    }
    String getField() {
        return field;
    }
	//menu name setter
	void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	//menu price setter
	void setPrice(int price) {
		this.price= price;
	}
	
	//menu name getter
	String getMenuName() {
		return menuName;
	}
	//menu price getter
	int getPrice() {
		return price;
	}
	
	 protected void writeObject(ObjectOutputStream out) throws IOException {
	        out.writeUTF(menuName); // Write menu name as UTF
	        out.writeInt(price); // Write price as int
	        out.writeUTF(field); // 필드 값 쓰기
	    }

	    protected void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
	        menuName = in.readUTF(); // Read menu name
	        price = in.readInt(); // Read price
	        field = in.readUTF(); // 필드 값 읽기
	    }
	
	    @Override
	    public boolean equals(Object obj) {
	        if (this == obj) return true;
	        if (obj == null || getClass() != obj.getClass()) return false;
	        Menu other = (Menu) obj;
	        return Objects.equals(menuName, other.menuName) &&
	               Objects.equals(field, other.field);
	    }

	//toString( method overriding 
	public String toString() {
		return menuName + " : " + price;
	}

}
