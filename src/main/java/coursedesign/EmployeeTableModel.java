package coursedesign;

import javax.swing.table.AbstractTableModel;

import java.util.List;

/**
 * 职工表格模型
 * 用于在JTable中显示职工数据
 */
public class EmployeeTableModel extends AbstractTableModel {
    // 列名
    private String[] columnNames = {"工号", "姓名", "性别", "出生日期", "入职日期", "部门", "职位", "学历", "薪资"};
    
    // 数据
    private List<Employee> data;
    
    // 构造函数
    public EmployeeTableModel(List<Employee> data) {
        this.data = data;
    }
    
    // 设置数据
    public void setData(List<Employee> data) {
        this.data = data;
        fireTableDataChanged();
    }
    
    // 获取列数
    @Override
    public int getColumnCount() {
        return columnNames.length;
    }
    
    // 获取行数
    @Override
    public int getRowCount() {
        return data.size();
    }
    
    // 获取列名
    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }
    
    // 获取单元格值
    @Override
    public Object getValueAt(int row, int col) {
        Employee employee = data.get(row);
        
        switch (col) {
            case 0: return employee.getId();
            case 1: return employee.getName();
            case 2: return employee.getGender();
            case 3: return employee.getFormattedBirthDate();
            case 4: return employee.getFormattedHireDate();
            case 5: return employee.getDepartment();
            case 6: return employee.getPosition();
            case 7: return employee.getEducation();
            case 8: return employee.getFormattedSalary();
            default: return null;
        }
    }
    
    // 获取列的数据类型
    @Override
    public Class<?> getColumnClass(int col) {
        switch (col) {
            case 0: return String.class;
            case 1: return String.class;
            case 2: return String.class;
            case 3: return String.class;
            case 4: return String.class;
            case 5: return String.class;
            case 6: return String.class;
            case 7: return String.class;
            case 8: return String.class;
            default: return Object.class;
        }
    }
    
    // 设置单元格是否可编辑
    @Override
    public boolean isCellEditable(int row, int col) {
        return false;
    }
    
    // 设置单元格值
    @Override
    public void setValueAt(Object value, int row, int col) {
        // 不允许直接在表格中编辑，通过编辑对话框进行编辑
    }
}

