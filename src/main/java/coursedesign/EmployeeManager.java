package coursedesign;

import java.io.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 职工管理类
 * 负责职工数据的增删改查、数据库操作和文件操作
 */
public class EmployeeManager {
    // 数据库连接工具
    private DBUtil dbUtil = new DBUtil();
    
    // 职工数据存储（内存缓存）
    private List<Employee> employees = new ArrayList<>();
    
    // 数据文件路径
    private String dataFile;
    
    // 构造函数
    public EmployeeManager() {
        // 确保数据库表存在
        ensureTablesExist();
        
        // 初始化时从数据库加载数据
        loadFromDatabase();
    }
    
    // 确保数据库表存在
    public void ensureTablesExist() {
        String sql = 
            "CREATE TABLE IF NOT EXISTS employees (" +
            "id VARCHAR(20) PRIMARY KEY, " +
            "name VARCHAR(100) NOT NULL, " +
            "gender VARCHAR(10), " +
            "birth_date DATE, " +
            "hire_date DATE, " +
            "department VARCHAR(100), " +
            "position VARCHAR(100), " +
            "education VARCHAR(20), " +
            "salary DECIMAL(10, 2), " +
            "phone VARCHAR(50), " +
            "email VARCHAR(100), " +
            "address VARCHAR(200), " +
            "remarks TEXT" +
            ")";
            
        try (Connection conn = dbUtil.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("数据库表检查完成");
        } catch (SQLException e) {
            System.err.println("创建数据库表失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // 从数据库加载所有职工
    private void loadFromDatabase() {
        employees.clear();
        String sql = "SELECT * FROM employees";
        
        try (ResultSet rs = dbUtil.executeQuery(sql)) {
            if (rs != null) {
                while (rs.next()) {
                    Employee employee = new Employee();
                    employee.setId(rs.getString("id"));
                    employee.setName(rs.getString("name"));
                    employee.setGender(rs.getString("gender"));
                    
                    // 处理日期类型
                    java.sql.Date sqlBirthDate = rs.getDate("birth_date");
                    if (sqlBirthDate != null) {
                        employee.setBirthDate(new java.util.Date(sqlBirthDate.getTime()));
                    }
                    
                    java.sql.Date sqlHireDate = rs.getDate("hire_date");
                    if (sqlHireDate != null) {
                        employee.setHireDate(new java.util.Date(sqlHireDate.getTime()));
                    }
                    
                    employee.setDepartment(rs.getString("department"));
                    employee.setPosition(rs.getString("position"));
                    employee.setEducation(rs.getString("education"));
                    employee.setSalary(rs.getDouble("salary"));
                    employee.setPhone(rs.getString("phone"));
                    employee.setEmail(rs.getString("email"));
                    employee.setAddress(rs.getString("address"));
                    employee.setRemarks(rs.getString("remarks"));
                    
                    employees.add(employee);
                }
            }
        } catch (Exception e) {
            System.err.println("从数据库加载职工数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // 添加职工
    public void addEmployee(Employee employee) throws SQLException {
        // 检查ID是否已存在
        if (getEmployeeById(employee.getId()) != null) {
            throw new IllegalArgumentException("职工ID已存在: " + employee.getId());
        }
        
        String sql = "INSERT INTO employees " +
                     "(id, name, gender, birth_date, hire_date, department, position, education, " +
                     "salary, phone, email, address, remarks) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        // 转换日期类型
        java.sql.Date sqlBirthDate = employee.getBirthDate() != null ? 
                new java.sql.Date(employee.getBirthDate().getTime()) : null;
        java.sql.Date sqlHireDate = employee.getHireDate() != null ? 
                new java.sql.Date(employee.getHireDate().getTime()) : null;
        
        int result = dbUtil.executeUpdate(sql, 
            employee.getId(),
            employee.getName(),
            employee.getGender(),
            sqlBirthDate,
            sqlHireDate,
            employee.getDepartment(),
            employee.getPosition(),
            employee.getEducation(),
            employee.getSalary(),
            employee.getPhone(),
            employee.getEmail(),
            employee.getAddress(),
            employee.getRemarks()
        );
        
        if (result > 0) {
            employees.add(employee); // 更新内存缓存
        } else {
            throw new SQLException("添加职工到数据库失败");
        }
    }
    
    // 更新职工信息
    public void updateEmployee(Employee employee) throws SQLException {
        String sql = "UPDATE employees SET " +
                     "name = ?, gender = ?, birth_date = ?, hire_date = ?, " +
                     "department = ?, position = ?, education = ?, salary = ?, " +
                     "phone = ?, email = ?, address = ?, remarks = ? " +
                     "WHERE id = ?";
        
        // 转换日期类型
        java.sql.Date sqlBirthDate = employee.getBirthDate() != null ? 
                new java.sql.Date(employee.getBirthDate().getTime()) : null;
        java.sql.Date sqlHireDate = employee.getHireDate() != null ? 
                new java.sql.Date(employee.getHireDate().getTime()) : null;
        
        int result = dbUtil.executeUpdate(sql, 
            employee.getName(),
            employee.getGender(),
            sqlBirthDate,
            sqlHireDate,
            employee.getDepartment(),
            employee.getPosition(),
            employee.getEducation(),
            employee.getSalary(),
            employee.getPhone(),
            employee.getEmail(),
            employee.getAddress(),
            employee.getRemarks(),
            employee.getId()
        );
        
        if (result > 0) {
            for (int i = 0; i < employees.size(); i++) {
                if (employees.get(i).getId().equals(employee.getId())) {
                    employees.set(i, employee); // 更新内存缓存
                    return;
                }
            }
            employees.add(employee); // 防止内存中不存在该记录
        } else {
            throw new SQLException("更新职工到数据库失败");
        }
    }
    
    // 删除职工
    public void deleteEmployee(String id) throws SQLException {
        String sql = "DELETE FROM employees WHERE id = ?";
        int result = dbUtil.executeUpdate(sql, id);
        
        if (result > 0) {
            employees.removeIf(employee -> employee.getId().equals(id)); // 更新内存缓存
        } else {
            throw new SQLException("从数据库删除职工失败");
        }
    }
    
    // 根据ID查找职工
    public Employee getEmployeeById(String id) {
        for (Employee employee : employees) {
            if (employee.getId().equals(id)) {
                return employee;
            }
        }
        return null;
    }
    
    // 获取所有职工
    public List<Employee> getAllEmployees() {
        return new ArrayList<>(employees);
    }
    
    // 按部门分组获取职工
    public Map<String, List<Employee>> getEmployeesByDepartment() {
        Map<String, List<Employee>> result = new HashMap<>();
        for (Employee employee : employees) {
            String department = employee.getDepartment();
            result.computeIfAbsent(department, k -> new ArrayList<>()).add(employee);
        }
        return result;
    }
    
    // 按职位分组获取职工
    public Map<String, List<Employee>> getEmployeesByPosition() {
        Map<String, List<Employee>> result = new HashMap<>();
        for (Employee employee : employees) {
            String position = employee.getPosition();
            result.computeIfAbsent(position, k -> new ArrayList<>()).add(employee);
        }
        return result;
    }
    
    // 按学历分组获取职工
    public Map<String, List<Employee>> getEmployeesByEducation() {
        Map<String, List<Employee>> result = new HashMap<>();
        for (Employee employee : employees) {
            String education = employee.getEducation();
            result.computeIfAbsent(education, k -> new ArrayList<>()).add(employee);
        }
        return result;
    }
    
    // 按薪资范围获取职工
    public List<Employee> getEmployeesBySalaryRange(double min, double max) {
        return employees.stream()
                .filter(e -> e.getSalary() >= min && e.getSalary() <= max)
                .collect(Collectors.toList());
    }
    
    // 按入职日期范围获取职工
    public List<Employee> getEmployeesByHireDateRange(java.util.Date startDate, java.util.Date endDate) {
        return employees.stream()
                .filter(e -> e.getHireDate() != null && 
                        e.getHireDate().compareTo(startDate) >= 0 && 
                        e.getHireDate().compareTo(endDate) <= 0)
                .collect(Collectors.toList());
    }
    
    // 按条件搜索职工
    public List<Employee> searchEmployees(Map<String, Object> criteria) {
        List<Employee> result = new ArrayList<>(employees);
        
        if (criteria == null || criteria.isEmpty()) {
            return result;
        }
        
        // 应用搜索条件
        for (Map.Entry<String, Object> entry : criteria.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            
            if (value == null) {
                continue;
            }
            
            if (key.equals("id") && value instanceof String) {
                String id = (String) value;
                if (!id.isEmpty()) {
                    result = result.stream()
                            .filter(e -> e.getId().toLowerCase().contains(id.toLowerCase()))
                            .collect(Collectors.toList());
                }
            } else if (key.equals("name") && value instanceof String) {
                String name = (String) value;
                if (!name.isEmpty()) {
                    result = result.stream()
                            .filter(e -> e.getName().toLowerCase().contains(name.toLowerCase()))
                            .collect(Collectors.toList());
                }
            } else if (key.equals("department") && value instanceof String) {
                String department = (String) value;
                if (!department.isEmpty()) {
                    result = result.stream()
                            .filter(e -> e.getDepartment().toLowerCase().contains(department.toLowerCase()))
                            .collect(Collectors.toList());
                }
            } else if (key.equals("position") && value instanceof String) {
                String position = (String) value;
                if (!position.isEmpty()) {
                    result = result.stream()
                            .filter(e -> e.getPosition().toLowerCase().contains(position.toLowerCase()))
                            .collect(Collectors.toList());
                }
            } else if (key.equals("education") && value instanceof String) {
                String education = (String) value;
                if (!education.isEmpty()) {
                    result = result.stream()
                            .filter(e -> e.getEducation().equals(education))
                            .collect(Collectors.toList());
                }
            } else if (key.equals("gender") && value instanceof String) {
                String gender = (String) value;
                if (!gender.isEmpty()) {
                    result = result.stream()
                            .filter(e -> e.getGender().equals(gender))
                            .collect(Collectors.toList());
                }
            } else if (key.equals("minSalary") && value instanceof Double) {
                double minSalary = (Double) value;
                result = result.stream()
                        .filter(e -> e.getSalary() >= minSalary)
                        .collect(Collectors.toList());
            } else if (key.equals("maxSalary") && value instanceof Double) {
                double maxSalary = (Double) value;
                result = result.stream()
                        .filter(e -> e.getSalary() <= maxSalary)
                        .collect(Collectors.toList());
            } else if (key.equals("startHireDate") && value instanceof java.util.Date) {
                java.util.Date startDate = (java.util.Date) value;
                result = result.stream()
                        .filter(e -> e.getHireDate() != null && e.getHireDate().compareTo(startDate) >= 0)
                        .collect(Collectors.toList());
            } else if (key.equals("endHireDate") && value instanceof java.util.Date) {
                java.util.Date endDate = (java.util.Date) value;
                result = result.stream()
                        .filter(e -> e.getHireDate() != null && e.getHireDate().compareTo(endDate) <= 0)
                        .collect(Collectors.toList());
            }
        }
        
        return result;
    }
    
    // 按指定字段排序职工
    public List<Employee> sortEmployees(String field, boolean ascending) {
        List<Employee> sortedList = new ArrayList<>(employees);
        
        switch (field.toLowerCase()) {
            case "id":
                sortedList.sort(Comparator.comparing(Employee::getId));
                break;
            case "name":
                sortedList.sort(Comparator.comparing(Employee::getName));
                break;
            case "gender":
                sortedList.sort(Comparator.comparing(Employee::getGender));
                break;
            case "birthdate":
                sortedList.sort(Comparator.comparing(Employee::getBirthDate));
                break;
            case "hiredate":
                sortedList.sort(Comparator.comparing(Employee::getHireDate));
                break;
            case "department":
                sortedList.sort(Comparator.comparing(Employee::getDepartment));
                break;
            case "position":
                sortedList.sort(Comparator.comparing(Employee::getPosition));
                break;
            case "education":
                sortedList.sort(Comparator.comparing(Employee::getEducation));
                break;
            case "salary":
                sortedList.sort(Comparator.comparingDouble(Employee::getSalary));
                break;
            default:
                // 默认按ID排序
                sortedList.sort(Comparator.comparing(Employee::getId));
        }
        
        if (!ascending) {
            Collections.reverse(sortedList);
        }
        
        return sortedList;
    }
    
    // 导出数据到CSV文件
    public void exportToCSV(File file, List<Employee> employees) throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            // 写入表头
            writer.write("工号,姓名,性别,出生日期,入职日期,部门,职位,学历,薪资,电话,邮箱,地址,备注");
            writer.newLine();
            
            // 写入数据
            for (Employee employee : employees) {
                writer.write(employee.getId() + ",");
                writer.write(employee.getName() + ",");
                writer.write(employee.getGender() + ",");
                writer.write(employee.getBirthDate() != null ? sdf.format(employee.getBirthDate()) : "" + ",");
                writer.write(employee.getHireDate() != null ? sdf.format(employee.getHireDate()) : "" + ",");
                writer.write(employee.getDepartment() + ",");
                writer.write(employee.getPosition() + ",");
                writer.write(employee.getEducation() + ",");
                writer.write(employee.getSalary() + ",");
                writer.write(employee.getPhone() + ",");
                writer.write(employee.getEmail() + ",");
                writer.write(employee.getAddress() + ",");
                writer.write(employee.getRemarks());
                writer.newLine();
            }
        }
    }
    
    // 从CSV文件导入数据
    public List<Employee> importFromCSV(File file) throws IOException, ParseException, SQLException {
        List<Employee> importedEmployees = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            // 跳过表头
            String line = reader.readLine();
            
            // 读取数据行
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 13) {
                    Employee employee = new Employee();
                    employee.setId(parts[0]);
                    employee.setName(parts[1]);
                    employee.setGender(parts[2]);
                    
                    // 转换日期
                    if (!parts[3].isEmpty()) {
                        employee.setBirthDate(sdf.parse(parts[3]));
                    }
                    if (!parts[4].isEmpty()) {
                        employee.setHireDate(sdf.parse(parts[4]));
                    }
                    
                    employee.setDepartment(parts[5]);
                    employee.setPosition(parts[6]);
                    employee.setEducation(parts[7]);
                    employee.setSalary(parts[8].isEmpty() ? 0 : Double.parseDouble(parts[8]));
                    employee.setPhone(parts[9]);
                    employee.setEmail(parts[10]);
                    employee.setAddress(parts[11]);
                    employee.setRemarks(parts[12]);
                    
                    importedEmployees.add(employee);
                }
            }
        }
        
        // 将导入的数据添加到数据库
        for (Employee emp : importedEmployees) {
            addEmployee(emp);
        }
        
        return importedEmployees;
    }
    
    // 保存数据到文件
    public void saveData(List<Employee> employees, String fileName) throws IOException {
        this.employees = employees;
        
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(employees);
        }
    }
    
    // 从文件加载数据
    @SuppressWarnings("unchecked")
    public List<Employee> loadData(String fileName) throws IOException, ClassNotFoundException {
        this.dataFile = fileName;
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            employees = (List<Employee>) ois.readObject();
            return employees;
        }
    }
    
    // 生成下一个工号（修复版本）
    public String generateNextId() {
        if (employees.isEmpty()) {
            return "EMP0001";
        }
        
        // 筛选出符合"EMP+数字"格式的ID
        List<String> validIds = employees.stream()
                .map(Employee::getId)
                .filter(id -> id != null && id.matches("EMP\\d+"))
                .collect(Collectors.toList());
        
        if (validIds.isEmpty()) {
            // 没有找到符合格式的ID，使用默认起始值
            return "EMP0001";
        }
        
        // 找到最大的工号
        String maxId = validIds.stream()
                .max(Comparator.naturalOrder())
                .orElse("EMP0000");
        
        try {
            // 提取数字部分并加1
            int number = Integer.parseInt(maxId.substring(3));
            number++;
            // 格式化为4位数字
            return String.format("EMP%04d", number);
        } catch (NumberFormatException e) {
            // 处理无法解析的ID
            System.err.println("无法解析员工ID: " + maxId);
            return "EMP0001";
        }
    }
}