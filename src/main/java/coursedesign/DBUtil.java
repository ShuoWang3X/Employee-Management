package coursedesign;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Arrays;
import java.util.Properties;

/**
 * 数据库连接工具类，提供数据库连接和操作的静态方法
 */
public class DBUtil {
    // 数据库连接参数
    private static String URL;
    private static String USERNAME;
    private static String PASSWORD;
    
    // 静态代码块，初始化数据库连接参数
    static {
        try {
            // 加载JDBC驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // 加载配置文件
            Properties props = loadProperties();
            
            // 获取配置参数，添加默认值防止空指针
            URL = props.getProperty("db.url", "jdbc:mysql://localhost:3306/employee_db?serverTimezone=Asia/Shanghai&characterEncoding=UTF-8");
            USERNAME = props.getProperty("db.username", "root");
            PASSWORD = props.getProperty("db.password", "123456");
            
            System.out.println("成功加载数据库配置: " + URL);
            
        } catch (ClassNotFoundException e) {
            System.err.println("错误: 未找到MySQL驱动，请添加mysql-connector-java依赖");
            e.printStackTrace();
        }
    }
    
    // 加载配置文件
    private static Properties loadProperties() {
        Properties props = new Properties();
        try {
            // 优先从外部配置文件加载
            File externalConfig = new File("db.properties");
            if (externalConfig.exists()) {
                try (FileInputStream fis = new FileInputStream(externalConfig)) {
                    props.load(fis);
                    System.out.println("成功加载外部配置文件: " + externalConfig.getAbsolutePath());
                    return props;
                }
            }
            
            // 尝试从类路径加载
            try (InputStream is = DBUtil.class.getResourceAsStream("/db.properties")) {
                if (is != null) {
                    props.load(is);
                    System.out.println("成功加载类路径配置文件");
                    return props;
                }
            }
            
            System.out.println("使用默认数据库配置");
        } catch (IOException e) {
            System.err.println("加载数据库配置失败: " + e.getMessage());
        }
        return props;
    }
    
    /**
     * 获取数据库连接（带自动重连逻辑）
     */
    public static Connection getConnection() {
        Connection con = null;
        int retryCount = 3; // 连接重试次数
        
        for (int i = 0; i < retryCount; i++) {
            try {
                con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                System.out.println("数据库连接成功: " + URL);
                return con;
            } catch (SQLException e) {
                System.err.println("数据库连接失败 (尝试 " + (i + 1) + "/" + retryCount + "): " + e.getMessage());
                
                // 最后一次尝试失败后抛出异常
                if (i == retryCount - 1) {
                    e.printStackTrace();
                    return null;
                }
                
                // 等待一段时间后重试
                try {
                    Thread.sleep(1000); // 等待1秒
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        
        return con;
    }
    
    /**
     * 关闭资源（带异常处理）
     */
    public static void close(Connection con, Statement stmt, ResultSet rs) {
        boolean error = false;
        
        try {
            if (rs != null) rs.close();
        } catch (SQLException e) {
            System.err.println("关闭 ResultSet 失败: " + e.getMessage());
            error = true;
        }
        
        try {
            if (stmt != null) stmt.close();
        } catch (SQLException e) {
            System.err.println("关闭 Statement 失败: " + e.getMessage());
            error = true;
        }
        
        try {
            if (con != null) {
                con.close();
                if (!error) {
                    System.out.println("数据库连接已正常关闭");
                }
            }
        } catch (SQLException e) {
            System.err.println("关闭 Connection 失败: " + e.getMessage());
        }
    }
    
    /**
     * 执行查询操作（返回ResultSet，使用try-with-resources）
     */
    public static ResultSet executeQuery(String sql, Object... params) {
        System.out.println("执行查询: " + sql);
        if (params != null && params.length > 0) {
            System.out.println("查询参数: " + Arrays.toString(params));
        }
        
        Connection con = getConnection();
        if (con == null) {
            System.err.println("无法获取数据库连接，查询失败");
            return null;
        }
        
        try {
            PreparedStatement pstmt = con.prepareStatement(sql);
            
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    pstmt.setObject(i + 1, params[i]);
                }
            }
            
            return pstmt.executeQuery();
        } catch (SQLException e) {
            System.err.println("查询执行失败: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 执行更新操作（INSERT/UPDATE/DELETE），返回受影响行数
     */
    public static int executeUpdate(String sql, Object... params) {
        System.out.println("执行更新: " + sql);
        if (params != null && params.length > 0) {
            System.out.println("更新参数: " + Arrays.toString(params));
        }
        
        Connection con = getConnection();
        if (con == null) {
            System.err.println("无法获取数据库连接，更新失败");
            return -1;
        }
        
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    pstmt.setObject(i + 1, params[i]);
                }
            }
            
            int result = pstmt.executeUpdate();
            System.out.println("更新成功，影响行数: " + result);
            return result;
        } catch (SQLException e) {
            System.err.println("更新执行失败: " + e.getMessage());
            e.printStackTrace();
            return -1;
        } finally {
            close(con, null, null);
        }
    }
    
    /**
     * 执行批量更新，返回更新结果数组
     */
    public static int[] executeBatch(String sql, Object[][] batchData) {
        System.out.println("执行批量更新: " + sql);
        
        Connection con = getConnection();
        if (con == null) {
            System.err.println("无法获取数据库连接，批量更新失败");
            return new int[0];
        }
        
        try {
            con.setAutoCommit(false);
            try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                if (batchData != null) {
                    for (int i = 0; i < batchData.length; i++) {
                        Object[] params = batchData[i];
                        if (params != null) {
                            for (int j = 0; j < params.length; j++) {
                                pstmt.setObject(j + 1, params[j]);
                            }
                        }
                        pstmt.addBatch();
                        System.out.println("添加批处理语句 #" + (i + 1) + " 参数: " + Arrays.toString(params));
                    }
                }
                
                int[] results = pstmt.executeBatch();
                con.commit();
                System.out.println("批量更新成功，结果: " + Arrays.toString(results));
                return results;
            }
        } catch (SQLException e) {
            System.err.println("执行批量更新失败: " + e.getMessage());
            e.printStackTrace();
            try {
                if (con != null) {
                    System.out.println("执行事务回滚");
                    con.rollback();
                }
            } catch (SQLException ex) {
                System.err.println("回滚事务失败: " + ex.getMessage());
            }
            return new int[0];
        } finally {
            try {
                if (con != null) {
                    con.setAutoCommit(true);
                }
            } catch (SQLException e) {
                System.err.println("恢复自动提交模式失败: " + e.getMessage());
            }
            close(con, null, null);
        }
    }
}