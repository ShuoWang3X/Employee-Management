package coursedesign;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 职工档案管理系统主类
 * 负责系统的初始化和主界面的显示
 */
public class EmployeeManagementSystem extends JFrame {
    // 系统标题
    private static final String SYSTEM_TITLE = "职工档案管理系统";
    
    // 系统版本
    private static final String SYSTEM_VERSION = "v1.0.0";
    
    // 数据文件路径（使用绝对路径）
    private static final String DATA_FILE = System.getProperty("user.dir") + File.separator + "employee_data.dat";
    
    // 职工数据存储
    private List<Employee> employees = new ArrayList<>();
    
    // 数据管理对象
    private EmployeeManager employeeManager;
    
    // 主面板
    private JPanel mainPanel;
    
    // 工具栏
    private JToolBar toolBar;
    
    // 状态条
    private JLabel statusBar;
    
    // 表格组件
    private JTable employeeTable;
    
    // 表格模型
    private EmployeeTableModel tableModel;
    
    // 构造函数
    public EmployeeManagementSystem() {
        super(SYSTEM_TITLE + " " + SYSTEM_VERSION);
        
        // 初始化数据管理器
        employeeManager = new EmployeeManager();
        
        // 先初始化界面组件（确保statusBar已创建）
        initComponents();
        
        // 再加载数据
        loadData();
        
        // 设置窗口属性
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1024, 768);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    // 初始化界面组件
    private void initComponents() {
        // 创建主面板
        mainPanel = new JPanel(new BorderLayout(10, 10));
        
        // 创建表格
        createTable();
        
        // 创建工具栏
        createToolBar();
        
        // 创建状态条
        statusBar = new JLabel("就绪");
        statusBar.setBorder(BorderFactory.createEtchedBorder());
        
        // 添加组件到主面板
        mainPanel.add(toolBar, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(employeeTable), BorderLayout.CENTER);
        mainPanel.add(statusBar, BorderLayout.SOUTH);
        
        // 添加主面板到窗口
        add(mainPanel);
        
        // 添加窗口关闭事件监听
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveData();
            }
        });
    }
    
    // 创建工具栏
    private void createToolBar() {
        toolBar = new JToolBar();
        toolBar.setFloatable(false);
        
        // 添加按钮
        JButton addButton = new JButton("新增", getIcon("/icons/add.png"));
        JButton editButton = new JButton("修改", getIcon("/icons/edit.png"));
        JButton deleteButton = new JButton("删除", getIcon("/icons/delete.png"));
        JButton searchButton = new JButton("查询", getIcon("/icons/search.png"));
        JButton refreshButton = new JButton("刷新", getIcon("/icons/refresh.png"));
        JButton exportButton = new JButton("导出", getIcon("/icons/export.png"));
        JButton importButton = new JButton("导入", getIcon("/icons/import.png"));
        
        // 添加按钮事件监听
        addButton.addActionListener(e -> {
            try {
                addEmployee();
            } catch (SQLException ex) {
                showSQLException(ex);
            }
        });
        editButton.addActionListener(e -> {
            try {
                editEmployee();
            } catch (SQLException ex) {
                showSQLException(ex);
            }
        });
        deleteButton.addActionListener(e -> {
            try {
                deleteEmployee();
            } catch (SQLException ex) {
                showSQLException(ex);
            }
        });
        searchButton.addActionListener(e -> searchEmployee());
        refreshButton.addActionListener(e -> refreshTable());
        exportButton.addActionListener(e -> exportData());
        importButton.addActionListener(e -> importData());
        
        // 设置按钮状态
        deleteButton.setEnabled(false);
        editButton.setEnabled(false);
        
        // 添加选择行事件监听，控制按钮状态
        if (employeeTable != null) {
            employeeTable.getSelectionModel().addListSelectionListener(e -> {
                boolean hasSelection = employeeTable.getSelectedRow() != -1;
                deleteButton.setEnabled(hasSelection);
                editButton.setEnabled(hasSelection);
            });
        }
        
        // 添加按钮到工具栏
        toolBar.add(addButton);
        toolBar.add(editButton);
        toolBar.add(deleteButton);
        toolBar.addSeparator();
        toolBar.add(searchButton);
        toolBar.add(refreshButton);
        toolBar.addSeparator();
        toolBar.add(exportButton);
        toolBar.add(importButton);
    }
    
 // 创建表格
    private void createTable() {
        // 创建表格模型
        tableModel = new EmployeeTableModel(employees);
        
        // 创建表格
        employeeTable = new JTable(tableModel);
        
        // 打印列数用于调试
        System.out.println("表格列数: " + employeeTable.getColumnCount());
        
        // 设置表格列宽（确保不超过实际列数）
        TableColumnModel columnModel = employeeTable.getColumnModel();
        int columnCount = columnModel.getColumnCount();
        
        if (columnCount > 0) columnModel.getColumn(0).setPreferredWidth(80);  // 工号
        if (columnCount > 1) columnModel.getColumn(1).setPreferredWidth(100); // 姓名
        if (columnCount > 2) columnModel.getColumn(2).setPreferredWidth(60);  // 性别
        if (columnCount > 3) columnModel.getColumn(3).setPreferredWidth(120); // 出生日期
        if (columnCount > 4) columnModel.getColumn(4).setPreferredWidth(120); // 入职日期
        if (columnCount > 5) columnModel.getColumn(5).setPreferredWidth(150); // 部门
        if (columnCount > 6) columnModel.getColumn(6).setPreferredWidth(150); // 职位
        if (columnCount > 7) columnModel.getColumn(7).setPreferredWidth(100); // 学历
        if (columnCount > 8) columnModel.getColumn(8).setPreferredWidth(120); // 薪资
        if (columnCount > 9) columnModel.getColumn(9).setPreferredWidth(150); // 电话
        if (columnCount > 10) columnModel.getColumn(10).setPreferredWidth(150); // 邮箱
        if (columnCount > 11) columnModel.getColumn(11).setPreferredWidth(150); // 地址
        if (columnCount > 12) columnModel.getColumn(12).setPreferredWidth(150); // 备注
        
        // 设置表格样式
        employeeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        employeeTable.setShowGrid(true);
        employeeTable.setGridColor(Color.LIGHT_GRAY);
        employeeTable.setRowHeight(25);
        
        // 设置日期列的格式
        DefaultTableCellRenderer dateRenderer = new DefaultTableCellRenderer() {
            private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, 
                                                           boolean isSelected, boolean hasFocus, 
                                                           int row, int column) {
                if (value instanceof Date) {
                    value = sdf.format((Date) value);
                }
                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        };
        
        // 设置日期列的渲染器
        if (columnCount > 3) columnModel.getColumn(3).setCellRenderer(dateRenderer);
        if (columnCount > 4) columnModel.getColumn(4).setCellRenderer(dateRenderer);
        
        // 设置表格双击事件
        employeeTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    try {
                        editEmployee();
                    } catch (SQLException ex) {
                        showSQLException(ex);
                    }
                }
            }
        });
    }
    
    // 加载数据
    private void loadData() {
        try {
            // 从数据库加载数据
            employees = employeeManager.getAllEmployees();
            tableModel.setData(employees);
            updateStatus("已加载 " + employees.size() + " 条职工数据");
        } catch (Exception e) {
            updateStatus("加载数据失败: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "加载数据失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // 保存数据
    private void saveData() {
        try {
            // 数据保存在数据库中，无需额外保存
            updateStatus("数据已自动保存到数据库");
        } catch (Exception e) {
            updateStatus("保存数据失败: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "保存数据失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // 新增职工
    private void addEmployee() throws SQLException {
        EmployeeDialog dialog = new EmployeeDialog(this, "新增职工", true);
        dialog.setEmployee(new Employee());
        dialog.setIdEditable(true);
        dialog.setVisible(true);
        
        if (dialog.isConfirmed()) {
            Employee employee = dialog.getEmployee();
            
            // 生成工号
            if (employee.getId() == null || employee.getId().trim().isEmpty()) {
                employee.setId(employeeManager.generateNextId());
            }
            
            // 添加到数据库
            employeeManager.addEmployee(employee);
            
            // 更新表格
            employees.add(employee);
            tableModel.setData(employees);
            
            updateStatus("成功添加职工: " + employee.getName());
        }
    }
    
    // 修改职工
    private void editEmployee() throws SQLException {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请先选择要修改的职工", "提示", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // 获取选中的职工
        String id = employeeTable.getValueAt(selectedRow, 0).toString();
        Employee employee = employeeManager.getEmployeeById(id);
        
        if (employee == null) {
            JOptionPane.showMessageDialog(this, "找不到选中的职工", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // 创建并显示修改对话框
        EmployeeDialog dialog = new EmployeeDialog(this, "修改职工", true);
        dialog.setEmployee(employee);
        dialog.setIdEditable(false);
        dialog.setVisible(true);
        
        if (dialog.isConfirmed()) {
            Employee updatedEmployee = dialog.getEmployee();
            
            // 更新到数据库
            employeeManager.updateEmployee(updatedEmployee);
            
            // 更新表格
            for (int i = 0; i < employees.size(); i++) {
                if (employees.get(i).getId().equals(updatedEmployee.getId())) {
                    employees.set(i, updatedEmployee);
                    break;
                }
            }
            tableModel.setData(employees);
            
            updateStatus("成功修改职工: " + updatedEmployee.getName());
        }
    }
    
    // 删除职工
    private void deleteEmployee() throws SQLException {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请先选择要删除的职工", "提示", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // 获取选中的职工
        String id = employeeTable.getValueAt(selectedRow, 0).toString();
        Employee employee = employeeManager.getEmployeeById(id);
        
        if (employee == null) {
            JOptionPane.showMessageDialog(this, "找不到选中的职工", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // 确认删除
        int confirm = JOptionPane.showConfirmDialog(
            this, 
            "确定要删除职工 " + employee.getName() + " 吗？", 
            "确认删除", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.WARNING_MESSAGE
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            // 从数据库删除
            employeeManager.deleteEmployee(id);
            
            // 更新表格
            employees.removeIf(e -> e.getId().equals(id));
            tableModel.setData(employees);
            
            updateStatus("成功删除职工: " + employee.getName());
        }
    }
    
    // 搜索职工
    private void searchEmployee() {
        SearchDialog dialog = new SearchDialog(this, "职工查询", true);
        dialog.setVisible(true);
        
        if (dialog.isConfirmed()) {
            Map<String, Object> criteria = dialog.getSearchCriteria();
            List<Employee> searchResult = employeeManager.searchEmployees(criteria);
            
            if (searchResult.isEmpty()) {
                JOptionPane.showMessageDialog(this, "没有找到符合条件的职工", "提示", JOptionPane.INFORMATION_MESSAGE);
                refreshTable();
            } else {
                employees = searchResult;
                tableModel.setData(employees);
                updateStatus("找到 " + employees.size() + " 条符合条件的记录");
            }
        }
    }
    
    // 刷新表格
    private void refreshTable() {
        try {
            // 从数据库重新加载数据
            employees = employeeManager.getAllEmployees();
            // 更新表格模型并触发刷新
            tableModel.setData(employees);
            updateStatus("数据已刷新，共 " + employees.size() + " 条记录");
        } catch (Exception e) {
            updateStatus("刷新失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // 导出数据
    private void exportData() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("导出数据");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".csv");
            }
            
            @Override
            public String getDescription() {
                return "CSV文件 (*.csv)";
            }
        });
        
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            // 确保文件扩展名是.csv
            if (!file.getName().toLowerCase().endsWith(".csv")) {
                file = new File(file.getParentFile(), file.getName() + ".csv");
            }
            
            try {
                employeeManager.exportToCSV(file, employees);
                updateStatus("成功导出 " + employees.size() + " 条记录到 " + file.getName());
                JOptionPane.showMessageDialog(this, "数据导出成功", "成功", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                updateStatus("导出数据失败: " + e.getMessage());
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "导出数据失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    // 导入数据
    private void importData() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("导入数据");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".csv");
            }
            
            @Override
            public String getDescription() {
                return "CSV文件 (*.csv)";
            }
        });
        
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            
            int confirm = JOptionPane.showConfirmDialog(
                this, 
                "导入操作将添加新记录，可能会覆盖已有数据。是否继续？", 
                "确认导入", 
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.WARNING_MESSAGE
            );
            
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    List<Employee> importedEmployees = employeeManager.importFromCSV(file);
                    employees = employeeManager.getAllEmployees();
                    tableModel.setData(employees);
                    updateStatus("成功导入 " + importedEmployees.size() + " 条记录");
                    JOptionPane.showMessageDialog(this, "成功导入 " + importedEmployees.size() + " 条记录", "成功", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException | ParseException | SQLException e) {
                    updateStatus("导入数据失败: " + e.getMessage());
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this, "导入数据失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
    // 更新状态条
    private void updateStatus(String message) {
        if (statusBar != null) {
            statusBar.setText(message);
        }
    }
    
    // 获取图标
    private ImageIcon getIcon(String path) {
        try {
            return new ImageIcon(getClass().getResource(path));
        } catch (Exception e) {
            System.err.println("无法加载图标: " + path);
            return null;
        }
    }
    
    // 显示SQL异常信息
    private void showSQLException(SQLException e) {
        String message = "数据库操作失败: " + e.getMessage();
        updateStatus(message);
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, message, "数据库错误", JOptionPane.ERROR_MESSAGE);
    }
    
    // 主方法
    public static void main(String[] args) {
        // 确保数据库表存在
        try {
            EmployeeManager manager = new EmployeeManager();
            // 调用确保表存在的方法
            manager.ensureTablesExist();
        } catch (Exception e) {
            System.err.println("初始化数据库失败: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "初始化数据库失败: " + e.getMessage(), "致命错误", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        
        // 在事件调度线程上创建和显示GUI
        SwingUtilities.invokeLater(() -> {
            try {
                // 设置系统默认外观
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                System.err.println("设置界面外观失败: " + e.getMessage());
                e.printStackTrace();
            }
            
            new EmployeeManagementSystem();
        });
    }
}