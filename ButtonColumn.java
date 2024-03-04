import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonColumn extends AbstractCellEditor implements TableCellRenderer, TableCellEditor, ActionListener {
    private JTable table;
    private JButton renderButton;
    private JButton editButton;
    private Action action;
    private int column;

    public ButtonColumn(JTable table, Action action, int column) {
        super();
        this.table = table;
        this.action = action;
        this.column = column;
        renderButton = new JButton();
        editButton = new JButton();
        editButton.setFocusPainted(false);
        editButton.addActionListener(this);
        renderButton.addActionListener(this);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (isSelected) {
            renderButton.setForeground(table.getSelectionForeground());
            renderButton.setBackground(table.getSelectionBackground());
        } else {
            renderButton.setForeground(table.getForeground());
            renderButton.setBackground(table.getBackground());
        }
        renderButton.setText((value == null) ? "" : value.toString());
        return renderButton;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        editButton.setText((value == null) ? "" : value.toString());
        return editButton;
    }


    @Override
    public Object getCellEditorValue() {
        return editButton.getText();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        fireEditingStopped();
        action.actionPerformed(new ActionEvent(table, ActionEvent.ACTION_PERFORMED, "" + table.getSelectedRow()));
    }
}
