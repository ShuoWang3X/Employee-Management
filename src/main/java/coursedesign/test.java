package coursedesign;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 测试数据库操作的类
 */
public class test {
    public static void main(String[] args) {
        // 测试连接
        Connection con = DBUtil.getConnection();
        if (con != null) {
            System.out.println("数据库连接测试成功!");
            DBUtil.close(con, null, null);
        } else {
            System.out.println("数据库连接测试失败!");
            return;
        }
        
        // 测试查询操作
        System.out.println("测试查询操作:");
        ResultSet rs = DBUtil.executeQuery("SELECT 1");
        try {
            if (rs != null && rs.next()) {
                System.out.println("查询结果: " + rs.getInt(1));
            } else {
                System.out.println("查询无结果");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(null, null, rs);
        }
        
        // 测试更新操作（使用数据库中已存在的表，如user表）
        System.out.println("\n测试更新操作:");
        int result = DBUtil.executeUpdate("INSERT INTO user (id, name) VALUES ('TEST001', '测试用户') ON DUPLICATE KEY UPDATE name = '测试用户'");
        System.out.println("更新返回: " + result);
    }
}