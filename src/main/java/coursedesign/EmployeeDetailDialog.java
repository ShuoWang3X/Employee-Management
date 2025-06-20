package coursedesign;

import javax.swing.*;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 职工详情对话框
 * 用于查看职工详细信息
 */
public class EmployeeDetailDialog extends JDialog {
    // 构造函数
    public EmployeeDetailDialog(Frame owner, String title, boolean modal, Employee employee) {
        super(owner, title, modal);
        
        // 初始化界面
        initComponents(employee);
        
        // 设置窗口属性
        setSize(500, 600);
        setLocationRelativeTo(owner);
        setResizable(false);
    }
    
    // 初始化界面组件
    private void initComponents(Employee employee) {
        // 创建面板
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // 创建表单面板
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // 日期格式化器
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        
        // 工号
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("工号:"), gbc);
        
        gbc.gridx = 1;
        formPanel.add(new JLabel(employee.getId()), gbc);
        
        // 姓名
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("姓名:"), gbc);
        
        gbc.gridx = 1;
        formPanel.add(new JLabel(employee.getName()), gbc);
        
        // 性别
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("性别:"), gbc);
        
        gbc.gridx = 1;
        formPanel.add(new JLabel(employee.getGender()), gbc);
        
        // 出生日期
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("出生日期:"), gbc);
        
        gbc.gridx = 1;
        Date birthDate = employee.getBirthDate();
        formPanel.add(new JLabel(birthDate != null ? dateFormat.format(birthDate) : ""), gbc);
        
        // 入职日期
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("入职日期:"), gbc);
        
        gbc.gridx = 1;
        Date hireDate = employee.getHireDate();
        formPanel.add(new JLabel(hireDate != null ? dateFormat.format(hireDate) : ""), gbc);
        
        // 部门
        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("部门:"), gbc);
        
        gbc.gridx = 1;
        formPanel.add(new JLabel(employee.getDepartment()), gbc);
        
        // 职位
        gbc.gridx = 0;
        gbc.gridy = 6;
        formPanel.add(new JLabel("职位:"), gbc);
        
        gbc.gridx = 1;
        formPanel.add(new JLabel(employee.getPosition()), gbc);
        
        // 学历
        gbc.gridx = 0;
        gbc.gridy = 7;
        formPanel.add(new JLabel("学历:"), gbc);
        
        gbc.gridx = 1;
        formPanel.add(new JLabel(employee.getEducation()), gbc);
        
        // 薪资
        gbc.gridx = 0;
        gbc.gridy = 8;
        formPanel.add(new JLabel("薪资:"), gbc);
        
        gbc.gridx = 1;
        formPanel.add(new JLabel(String.format("%.2f", employee.getSalary())), gbc);
        
        // 电话
        gbc.gridx = 0;
        gbc.gridy = 9;
        formPanel.add(new JLabel("电话:"), gbc);
        
        gbc.gridx = 1;
        formPanel.add(new JLabel(employee.getPhone()), gbc);
        
        // 邮箱
        gbc.gridx = 0;
        gbc.gridy = 10;
        formPanel.add(new JLabel("邮箱:"), gbc);
        
        gbc.gridx = 1;
        formPanel.add(new JLabel(employee.getEmail()), gbc);
        
        // 地址
        gbc.gridx = 0;
        gbc.gridy = 11;
        formPanel.add(new JLabel("地址:"), gbc);
        
        gbc.gridx = 1;
        formPanel.add(new JLabel(employee.getAddress()), gbc);
        
        // 备注
        gbc.gridx = 0;
        gbc.gridy = 12;
        formPanel.add(new JLabel("备注:"), gbc);
        
        gbc.gridx = 1;
        JTextArea remarksArea = new JTextArea(3, 20);
        remarksArea.setEditable(false);
        remarksArea.setLineWrap(true);
        remarksArea.setText(employee.getRemarks());
        JScrollPane scrollPane = new JScrollPane(remarksArea);
        formPanel.add(scrollPane, gbc);
        
        // 创建按钮面板
        JPanel buttonPanel = new JPanel();
        JButton closeButton = new JButton("关闭");
        
        closeButton.addActionListener(e -> dispose());
        
        buttonPanel.add(closeButton);
        
        // 添加组件到主面板
        panel.add(new JLabel("职工详细信息:"), BorderLayout.NORTH);
        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        // 添加主面板到对话框
        add(panel);
    }
}
