
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;
public class ButtonEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
	//테이블의 특정 열에 배치된 버튼을 누를때 실행되는로직 관리 
    private JButton button;
    private JTable table;
    private JFrame guiInstance;
    private int editingRow, editingCol;
    private String actionType;
    private String context;

    public ButtonEditor(JCheckBox checkBox, JFrame guiInstance, String actionType, String context) {
        this.guiInstance = guiInstance;
        this.actionType = actionType;
        this.context = context;

        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(this);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.table = table;
        this.editingRow = row;
        button.setText((value != null) ? value.toString() : "");
        return button;
    }
    /*
    @Override
    public void actionPerformed(ActionEvent e) {
        if ("MenuManagement".equals(context)) {
            handleMenuManagementActions();
        } else if ("TableManagement".equals(context)) {
            handleTableManagementActions();
        }
        fireEditingStopped();
    }*/
    @Override
    public void actionPerformed(ActionEvent e) {
        // 로그 추가
        System.out.println("Button clicked: " + actionType + " in row " + editingRow);

        if ("TableManagement".equals(context)) {
            handleTableManagementActions();
        } else if ("MenuManagement".equals(context)) {
            handleMenuManagementActions();
        }
        fireEditingStopped(); // Notify the cell editor that editing has stopped
    }


    private void handleMenuManagementActions() {
        if (guiInstance instanceof MenuManagementGUI) {
            MenuManagementGUI menuGUI = (MenuManagementGUI) guiInstance;
            switch (actionType) {
                case "Edit":
                    menuGUI.toggleEditMode(editingRow, "Edit");
                    break;
                case "Save":
                    menuGUI.toggleEditMode(editingRow, "Save");
                    break;
                case "Delete":
                    menuGUI.deleteMenuItem(editingRow);
                    break;
            }
        }
    }

    private void handleTableManagementActions() {
        if (guiInstance instanceof TableManagementGUI) {
            TableManagementGUI tableGUI = (TableManagementGUI) guiInstance;
            switch (actionType) {
                case "Edit":
                    tableGUI.toggleEditMode(editingRow, "Edit");
                    break;
                case "Save":
                    tableGUI.toggleEditMode(editingRow, "Save");
                    break;
                case "Delete":
                    tableGUI.deleteTableItem(editingRow);
                    break;
            }
        }
    }

    @Override
    public Object getCellEditorValue() {
        return button.getText();
    }
}