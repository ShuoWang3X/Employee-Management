package coursedesign;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * 搜索对话框
 * 用于设置搜索条件
 */
public class SearchDialog extends JDialog {
    // 对话框确认状态
    private boolean confirmed = false;
    
    // 搜索条件
    private Map searchCriteria = new HashMap();
    
    // 组件
    private JTextField idField;
    private JTextField nameField;
    private JComboBox departmentCombo;
    private JComboBox positionCombo;
    private JComboBox educationCombo;
    private JComboBox genderCombo;
    private JTextField minSalaryField;
    private JTextField maxSalaryField;
    private JTextField startHireDateField;
    private JTextField endHireDateField;
    
    // 日期格式
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    
    // 构造函数
    public SearchDialog(Frame parent, String title, boolean modal) {
        super(parent, title, modal);
        initComponents();
        setSize(500, 400);
        setLocationRelativeTo(parent);
    }
    
    // 初始化组件
    private void initComponents() {
        // 创建面板
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // 创建标签和输入组件
        JLabel idLabel = new JLabel("工号:");
        JLabel nameLabel = new JLabel("姓名:");
        JLabel departmentLabel = new JLabel("部门:");
        JLabel positionLabel = new JLabel("职位:");
        JLabel educationLabel = new JLabel("学历:");
        JLabel genderLabel = new JLabel("性别:");
        JLabel salaryLabel = new JLabel("薪资范围:");
        JLabel minSalaryLabel = new JLabel("最小:");
        JLabel maxSalaryLabel = new JLabel("最大:");
        JLabel hireDateLabel = new JLabel("入职日期范围:");
        JLabel startHireDateLabel = new JLabel("开始:");
        JLabel endHireDateLabel = new JLabel("结束:");
        
        idField = new JTextField(15);
        nameField = new JTextField(15);
        
        // 获取部门列表
        EmployeeManager manager = new EmployeeManager();
        List departments = new ArrayList(manager.getEmployeesByDepartment().keySet());
        departments.add(0, ""); // 添加空选项
        
        // 获取职位列表
        List positions = new ArrayList(manager.getEmployeesByPosition().keySet());
        positions.add(0, ""); // 添加空选项
        
        String[] educations = {"", "高中", "大专", "本科", "硕士", "博士", "其他"};
        String[] genders = {"", "男", "女", "其他"};
        
        // 修改：使用Vector替代，并明确转换类型
        departmentCombo = new JComboBox(new Vector(departments));
        positionCombo = new JComboBox(new Vector(positions));
        
        educationCombo = new JComboBox(educations);
        genderCombo = new JComboBox(genders);
        
        minSalaryField = new JTextField(8);
        maxSalaryField = new JTextField(8);
        startHireDateField = new JTextField(10);
        endHireDateField = new JTextField(10);
        
        // 添加组件到面板
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(idLabel, gbc);
        
        gbc.gridx = 1;
        panel.add(idField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(nameLabel, gbc);
        
        gbc.gridx = 1;
        panel.add(nameField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(departmentLabel, gbc);
        
        gbc.gridx = 1;
        panel.add(departmentCombo, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(positionLabel, gbc);
        
        gbc.gridx = 1;
        panel.add(positionCombo, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(educationLabel, gbc);
        
        gbc.gridx = 1;
        panel.add(educationCombo, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(genderLabel, gbc);
        
        gbc.gridx = 1;
        panel.add(genderCombo, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 6;
        panel.add(salaryLabel, gbc);
        
        gbc.gridx = 1;
        JPanel salaryPanel = new JPanel();
        salaryPanel.add(minSalaryLabel);
        salaryPanel.add(minSalaryField);
        salaryPanel.add(new JLabel("至"));
        salaryPanel.add(maxSalaryLabel);
        salaryPanel.add(maxSalaryField);
        panel.add(salaryPanel, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 7;
        panel.add(hireDateLabel, gbc);
        
        gbc.gridx = 1;
        JPanel hireDatePanel = new JPanel();
        hireDatePanel.add(startHireDateLabel);
        hireDatePanel.add(startHireDateField);
        hireDatePanel.add(new JLabel("至"));
        hireDatePanel.add(endHireDateLabel);
        hireDatePanel.add(endHireDateField);
        panel.add(hireDatePanel, gbc);
        
        // 创建按钮面板
        JPanel buttonPanel = new JPanel();
        JButton searchButton = new JButton("搜索");
        JButton cancelButton = new JButton("取消");
        
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (validateInput()) {
                    confirmed = true;
                    dispose();
                }
            }
        });
        
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        
        buttonPanel.add(searchButton);
        buttonPanel.add(cancelButton);
        
        // 添加主面板和按钮面板到对话框
        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    // 验证输入
    private boolean validateInput() {
        // 清空之前的搜索条件
        searchCriteria.clear();
        
        // 添加工号条件
        if (!idField.getText().trim().isEmpty()) {
            searchCriteria.put("id", idField.getText().trim());
        }
        
        // 添加姓名条件
        if (!nameField.getText().trim().isEmpty()) {
            searchCriteria.put("name", nameField.getText().trim());
        }
        
        // 添加部门条件
        if (departmentCombo.getSelectedIndex() > 0) {
            searchCriteria.put("department", departmentCombo.getSelectedItem());
        }
        
        // 添加职位条件
        if (positionCombo.getSelectedIndex() > 0) {
            searchCriteria.put("position", positionCombo.getSelectedItem());
        }
        
        // 添加学历条件
        if (educationCombo.getSelectedIndex() > 0) {
            searchCriteria.put("education", educationCombo.getSelectedItem());
        }
        
        // 添加性别条件
        if (genderCombo.getSelectedIndex() > 0) {
            searchCriteria.put("gender", genderCombo.getSelectedItem());
        }
        
        // 添加薪资条件
        if (!minSalaryField.getText().trim().isEmpty()) {
            try {
                double minSalary = Double.parseDouble(minSalaryField.getText());
                searchCriteria.put("minSalary", new Double(minSalary));
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "最小薪资格式不正确", "输入错误", JOptionPane.ERROR_MESSAGE);
                minSalaryField.requestFocus();
                return false;
            }
        }
        
        if (!maxSalaryField.getText().trim().isEmpty()) {
            try {
                double maxSalary = Double.parseDouble(maxSalaryField.getText());
                searchCriteria.put("maxSalary", new Double(maxSalary));
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "最大薪资格式不正确", "输入错误", JOptionPane.ERROR_MESSAGE);
                maxSalaryField.requestFocus();
                return false;
            }
        }
        
        // 验证薪资范围
        if (!minSalaryField.getText().trim().isEmpty() && !maxSalaryField.getText().trim().isEmpty()) {
            double min = Double.parseDouble(minSalaryField.getText());
            double max = Double.parseDouble(maxSalaryField.getText());
            if (min > max) {
                JOptionPane.showMessageDialog(this, "最小薪资不能大于最大薪资", "输入错误", JOptionPane.ERROR_MESSAGE);
                minSalaryField.requestFocus();
                return false;
            }
        }
        
        // 添加入职日期条件
        if (!startHireDateField.getText().trim().isEmpty()) {
            try {
                Date startDate = dateFormat.parse(startHireDateField.getText());
                searchCriteria.put("startHireDate", startDate);
            } catch (ParseException e) {
                JOptionPane.showMessageDialog(this, "开始日期格式不正确，应为yyyy-MM-dd", "输入错误", JOptionPane.ERROR_MESSAGE);
                startHireDateField.requestFocus();
                return false;
            }
        }
        
        if (!endHireDateField.getText().trim().isEmpty()) {
            try {
                Date endDate = dateFormat.parse(endHireDateField.getText());
                searchCriteria.put("endHireDate", endDate);
            } catch (ParseException e) {
                JOptionPane.showMessageDialog(this, "结束日期格式不正确，应为yyyy-MM-dd", "输入错误", JOptionPane.ERROR_MESSAGE);
                endHireDateField.requestFocus();
                return false;
            }
        }
        
        // 验证日期范围
        if (!startHireDateField.getText().trim().isEmpty() && !endHireDateField.getText().trim().isEmpty()) {
            try {
                Date startDate = dateFormat.parse(startHireDateField.getText());
                Date endDate = dateFormat.parse(endHireDateField.getText());
                if (startDate.after(endDate)) {
                    JOptionPane.showMessageDialog(this, "开始日期不能晚于结束日期", "输入错误", JOptionPane.ERROR_MESSAGE);
                    startHireDateField.requestFocus();
                    return false;
                }
            } catch (ParseException e) {
                // 已经验证过格式，不会执行到这里
            }
        }
        
        // 至少有一个搜索条件
        if (searchCriteria.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请至少输入一个搜索条件", "提示", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        
        return true;
    }
    
    // 获取搜索条件
    public Map getSearchCriteria() {
        return searchCriteria;
    }
    
    // 判断是否点击了搜索按钮
    public boolean isConfirmed() {
        return confirmed;
    }
}