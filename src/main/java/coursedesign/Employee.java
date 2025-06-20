package coursedesign;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.text.SimpleDateFormat;

/**
 * 职工实体类
 * 用于存储职工的基本信息
 */
public class Employee implements Serializable, Comparable<Employee> {
    // 序列化版本号
    private static final long serialVersionUID = 1L;
    
    // 职工ID
    private String id;
    
    // 姓名
    private String name;
    
    // 性别
    private String gender;
    
    // 出生日期
    private Date birthDate;
    
    // 入职日期
    private Date hireDate;
    
    // 部门
    private String department;
    
    // 职位
    private String position;
    
    // 学历
    private String education;
    
    // 薪资
    private double salary;
    
    // 联系电话
    private String phone;
    
    // 电子邮箱
    private String email;
    
    // 家庭住址
    private String address;
    
    // 备注信息
    private String remarks;
    
    // 无参构造函数
    public Employee() {
    }
    
    // 带参构造函数
    public Employee(String id, String name, String gender, Date birthDate, Date hireDate, 
                   String department, String position, String education, double salary, 
                   String phone, String email, String address, String remarks) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.birthDate = birthDate;
        this.hireDate = hireDate;
        this.department = department;
        this.position = position;
        this.education = education;
        this.salary = salary;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.remarks = remarks;
    }
    
    // Getter和Setter方法
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getGender() {
        return gender;
    }
    
    public void setGender(String gender) {
        this.gender = gender;
    }
    
    public Date getBirthDate() {
        return birthDate;
    }
    
    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }
    
    public Date getHireDate() {
        return hireDate;
    }
    
    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }
    
    public String getDepartment() {
        return department;
    }
    
    public void setDepartment(String department) {
        this.department = department;
    }
    
    public String getPosition() {
        return position;
    }
    
    public void setPosition(String position) {
        this.position = position;
    }
    
    public String getEducation() {
        return education;
    }
    
    public void setEducation(String education) {
        this.education = education;
    }
    
    public double getSalary() {
        return salary;
    }
    
    public void setSalary(double salary) {
        this.salary = salary;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getRemarks() {
        return remarks;
    }
    
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    
    // 获取格式化的出生日期
    public String getFormattedBirthDate() {
        if (birthDate == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(birthDate);
    }
    
    // 获取格式化的入职日期
    public String getFormattedHireDate() {
        if (hireDate == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(hireDate);
    }
    
    // 获取格式化的薪资
    public String getFormattedSalary() {
        return String.format("%.2f", salary);
    }
    
    // 重写equals方法
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Employee employee = (Employee) obj;
        return id.equals(employee.id);
    }
    
    // 重写hashCode方法
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    // 重写toString方法
    @Override
    public String toString() {
        return "Employee{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", department='" + department + '\'' +
                ", position='" + position + '\'' +
                '}';
    }
    
    // 实现Comparable接口的compareTo方法，按工号排序
    @Override
    public int compareTo(Employee other) {
        return this.id.compareTo(other.id);
    }
}

