package coursedesign;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 职工信息对话框
 * 用于新增和修改职工信息
 */
public class EmployeeDialog extends JDialog {
    // 对话框确认状态
    private boolean confirmed = false;
    
    // 职工对象
    private Employee employee;
    
    // 组件
    private JTextField idField;
    private JTextField nameField;
    private JComboBox<String> genderCombo;
    private JTextField birthDateField;
    private JTextField hireDateField;
    private JTextField departmentField;
    private JTextField positionField;
    private JComboBox<String> educationCombo;
    private JTextField salaryField;
    private JTextField phoneField;
    private JTextField emailField;
    private JTextField addressField;
    private JTextArea remarksArea;
    
    // 日期格式
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    
    // 构造函数
    public EmployeeDialog(Frame parent, String title, boolean modal) {
        super(parent, title, modal);
        initComponents();
        setSize(600, 550);
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
        JLabel genderLabel = new JLabel("性别:");
        JLabel birthDateLabel = new JLabel("出生日期:");
        JLabel hireDateLabel = new JLabel("入职日期:");
        JLabel departmentLabel = new JLabel("部门:");
        JLabel positionLabel = new JLabel("职位:");
        JLabel educationLabel = new JLabel("学历:");
        JLabel salaryLabel = new JLabel("薪资:");
        JLabel phoneLabel = new JLabel("电话:");
        JLabel emailLabel = new JLabel("邮箱:");
        JLabel addressLabel = new JLabel("地址:");
        JLabel remarksLabel = new JLabel("备注:");
        
        idField = new JTextField(20);
        nameField = new JTextField(20);
        
        String[] genders = {"男", "女", "其他"};
        genderCombo = new JComboBox<>(genders);
        
        birthDateField = new JTextField(20);
        hireDateField = new JTextField(20);
        
        departmentField = new JTextField(20);
        positionField = new JTextField(20);
        
        String[] educations = {"高中", "大专", "本科", "硕士", "博士", "其他"};
        educationCombo = new JComboBox<>(educations);
        
        salaryField = new JTextField(20);
        phoneField = new JTextField(20);
        emailField = new JTextField(20);
        addressField = new JTextField(20);
        remarksArea = new JTextArea(4, 20);
        remarksArea.setLineWrap(true);
        remarksArea.setWrapStyleWord(true);
        
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
        panel.add(genderLabel, gbc);
        
        gbc.gridx = 1;
        panel.add(genderCombo, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(birthDateLabel, gbc);
        
        gbc.gridx = 1;
        panel.add(birthDateField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(hireDateLabel, gbc);
        
        gbc.gridx = 1;
        panel.add(hireDateField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(departmentLabel, gbc);
        
        gbc.gridx = 1;
        panel.add(departmentField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 6;
        panel.add(positionLabel, gbc);
        
        gbc.gridx = 1;
        panel.add(positionField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 7;
        panel.add(educationLabel, gbc);
        
        gbc.gridx = 1;
        panel.add(educationCombo, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 8;
        panel.add(salaryLabel, gbc);
        
        gbc.gridx = 1;
        panel.add(salaryField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 9;
        panel.add(phoneLabel, gbc);
        
        gbc.gridx = 1;
        panel.add(phoneField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 10;
        panel.add(emailLabel, gbc);
        
        gbc.gridx = 1;
        panel.add(emailField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 11;
        panel.add(addressLabel, gbc);
        
        gbc.gridx = 1;
        panel.add(addressField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 12;
        panel.add(remarksLabel, gbc);
        
        gbc.gridx = 1;
        panel.add(new JScrollPane(remarksArea), gbc);
        
        // 创建按钮面板
        JPanel buttonPanel = new JPanel();
        JButton okButton = new JButton("确定");
        JButton cancelButton = new JButton("取消");
        
        okButton.addActionListener(e -> {
            if (validateInput()) {
                confirmed = true;
                dispose();
            }
        });
        
        cancelButton.addActionListener(e -> dispose());
        
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        
        // 添加主面板和按钮面板到对话框
        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    // 验证输入
    private boolean validateInput() {
        // 验证姓名
        if (nameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入姓名", "输入错误", JOptionPane.ERROR_MESSAGE);
            nameField.requestFocus();
            return false;
        }
        
        // 验证出生日期
        if (!birthDateField.getText().trim().isEmpty()) {
            try {
                dateFormat.parse(birthDateField.getText());
            } catch (ParseException e) {
                JOptionPane.showMessageDialog(this, "出生日期格式不正确，应为yyyy-MM-dd", "输入错误", JOptionPane.ERROR_MESSAGE);
                birthDateField.requestFocus();
                return false;
            }
        }
        
        // 验证入职日期
        if (!hireDateField.getText().trim().isEmpty()) {
            try {
                dateFormat.parse(hireDateField.getText());
            } catch (ParseException e) {
                JOptionPane.showMessageDialog(this, "入职日期格式不正确，应为yyyy-MM-dd", "输入错误", JOptionPane.ERROR_MESSAGE);
                hireDateField.requestFocus();
                return false;
            }
        }
        
        // 验证薪资
        if (!salaryField.getText().trim().isEmpty()) {
            try {
                double salary = Double.parseDouble(salaryField.getText());
                if (salary < 0) {
                    JOptionPane.showMessageDialog(this, "薪资不能为负数", "输入错误", JOptionPane.ERROR_MESSAGE);
                    salaryField.requestFocus();
                    return false;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "薪资格式不正确", "输入错误", JOptionPane.ERROR_MESSAGE);
                salaryField.requestFocus();
                return false;
            }
        }
        
        // 验证邮箱格式
        if (!emailField.getText().trim().isEmpty()) {
            String email = emailField.getText().trim();
            if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                JOptionPane.showMessageDialog(this, "邮箱格式不正确", "输入错误", JOptionPane.ERROR_MESSAGE);
                emailField.requestFocus();
                return false;
            }
        }
        
        // 将输入数据保存到employee对象
        if (employee == null) {
            employee = new Employee();
        }
        
        employee.setId(idField.getText().trim());
        employee.setName(nameField.getText().trim());
        employee.setGender((String) genderCombo.getSelectedItem());
        
        if (!birthDateField.getText().trim().isEmpty()) {
            try {
                employee.setBirthDate(dateFormat.parse(birthDateField.getText()));
            } catch (ParseException e) {
                // 已经验证过格式，不会执行到这里
            }
        } else {
            employee.setBirthDate(null);
        }
        
        if (!hireDateField.getText().trim().isEmpty()) {
            try {
                employee.setHireDate(dateFormat.parse(hireDateField.getText()));
            } catch (ParseException e) {
                // 已经验证过格式，不会执行到这里
            }
        } else {
            employee.setHireDate(null);
        }
        
        employee.setDepartment(departmentField.getText().trim());
        employee.setPosition(positionField.getText().trim());
        employee.setEducation((String) educationCombo.getSelectedItem());
        
        if (!salaryField.getText().trim().isEmpty()) {
            employee.setSalary(Double.parseDouble(salaryField.getText()));
        } else {
            employee.setSalary(0);
        }
        
        employee.setPhone(phoneField.getText().trim());
        employee.setEmail(emailField.getText().trim());
        employee.setAddress(addressField.getText().trim());
        employee.setRemarks(remarksArea.getText().trim());
        
        return true;
    }
    
    // 设置职工对象
    public void setEmployee(Employee employee) {
        this.employee = employee;
        
        if (employee != null) {
            idField.setText(employee.getId());
            nameField.setText(employee.getName());
            genderCombo.setSelectedItem(employee.getGender());
            
            if (employee.getBirthDate() != null) {
                birthDateField.setText(dateFormat.format(employee.getBirthDate()));
            }
            
            if (employee.getHireDate() != null) {
                hireDateField.setText(dateFormat.format(employee.getHireDate()));
            }
            
            departmentField.setText(employee.getDepartment());
            positionField.setText(employee.getPosition());
            educationCombo.setSelectedItem(employee.getEducation());
            salaryField.setText(String.valueOf(employee.getSalary()));
            phoneField.setText(employee.getPhone());
            emailField.setText(employee.getEmail());
            addressField.setText(employee.getAddress());
            remarksArea.setText(employee.getRemarks());
        }
    }
    
    // 获取职工对象
    public Employee getEmployee() {
        return employee;
    }
    
    // 设置工号是否可编辑
    public void setIdEditable(boolean editable) {
        idField.setEditable(editable);
    }
    
    // 判断是否点击了确认按钮
    public boolean isConfirmed() {
        return confirmed;
    }
}