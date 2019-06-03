package com.triage;

import java.sql.*;
import java.util.logging.*;
import javax.swing.table.*;

import com.common.config.MainConfig;

/**
 *  SQL 基本操作
 * 通过它,可以很轻松的使用 JDBC 来操纵数据库
 * @author Null
 */
public class SqlHelper
{

    /**
     * 驱动
     */
    public static String driver = MainConfig.map.get("sqldriver");
    /**
     * 连接字符串
     */
    public static String url =MainConfig.map.get("sqlurl");
    /**
     * 用户名
     */
    public static String user =MainConfig.map.get("sqlusername");
    /**
     * 密码
     */
    public static String password =MainConfig.map.get("sqlpassword");

    /**
     * 不允许实例化该类
     */
    private SqlHelper()
    {
    	
    	/*driver =MainConfig.map.get("sqldriver");
    	url=MainConfig.map.get("sqlurl");
    	user=MainConfig.map.get("sqlusername");
    	password=MainConfig.map.get("sqlpassword");*/
    }

    /**
     * 获取一个数据库连接
     * 通过设置类的  driver / url / user / password 这四个静态变量来 设置数据库连接属性
     * @return 数据库连接
     */
    public static Connection getConnection()
    {
        try
        {
            // 获取驱动,这里使用的是 sqljdbc_1.2.2828.100_chs.exe,不同版本的驱动,语句有所不同
            Class.forName(driver);
        } catch (ClassNotFoundException ex)
        {
            Logger.getLogger(SqlHelper.class.getName()).log(Level.SEVERE, null, ex);
        }

        try
        {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException ex)
        {
            Logger.getLogger(SqlHelper.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * 获取一个 Statement
     * 该 Statement 已经设置数据集 可以滚动,可以更新
     * @return 如果获取失败将返回 null,调用时记得检查返回值
     */
    public static Statement getStatement()
    {
        Connection conn = getConnection();
        if (conn == null)
        {
            return null;
        }
        try
        {
            return conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
        // 设置数据集可以滚动,可以更新
        } catch (SQLException ex)
        {
            Logger.getLogger(SqlHelper.class.getName()).log(Level.SEVERE, null, ex);
            close(conn);
        }
        return null;
    }

    /**
     * 获取一个 Statement
     * 该 Statement 已经设置数据集 可以滚动,可以更新
     * @param conn 数据库连接
     * @return 如果获取失败将返回 null,调用时记得检查返回值
     */
    public static Statement getStatement(Connection conn)
    {
        if (conn == null)
        {
            return null;
        }
        try
        {

            return conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        // 设置数据集可以滚动,可以更新
        } catch (SQLException ex)
        {
            Logger.getLogger(SqlHelper.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * 获取一个带参数的 PreparedStatement
     * 该 PreparedStatement 已经设置数据集 可以滚动,可以更新
     * @param cmdText 需要 ? 参数的 SQL 语句
     * @param cmdParams SQL 语句的参数表
     * @return 如果获取失败将返回 null,调用时记得检查返回值
     */
    public static PreparedStatement getPreparedStatement(String cmdText, Object... cmdParams)
    {
        Connection conn = getConnection();
        if (conn == null)
        {
            return null;
        }

        PreparedStatement pstmt = null;
        try
        {
            pstmt = conn.prepareStatement(cmdText, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            int i = 1;
            for (Object item : cmdParams)
            {
                pstmt.setObject(i, item);
                i++;
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
            close(conn);
        }
        return pstmt;
    }

    /**
     *  获取一个带参数的 PreparedStatement
     * 该 PreparedStatement 已经设置数据集 可以滚动,可以更新
     * @param conn 数据库连接
     * @param cmdText 需要 ? 参数的 SQL 语句
     * @param cmdParams SQL 语句的参数表
     * @return 如果获取失败将返回 null,调用时记得检查返回值
     */
    public static PreparedStatement getPreparedStatement(Connection conn, String cmdText, Object... cmdParams)
    {
        if (conn == null)
        {
            return null;
        }

        PreparedStatement pstmt = null;
        try
        {
            pstmt = conn.prepareStatement(cmdText, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            int i = 1;
            for (Object item : cmdParams)
            {
                pstmt.setObject(i, item);
                i++;
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
            close(pstmt);
        }
        return pstmt;
    }

    /**
     * 执行 SQL 语句,返回结果为整型
     * 主要用于执行非查询语句
     * @param cmdText SQL 语句
     * @return 非负数:正常执行; -1:执行错误; -2:连接错误
     */
    public static int ExecSql(String cmdText)
    {
        Statement stmt = getStatement();
        if (stmt == null)
        {
            return -2;
        }
        int i;
        try
        {
            i = stmt.executeUpdate(cmdText);
        } catch (SQLException ex)
        {
            Logger.getLogger(SqlHelper.class.getName()).log(Level.SEVERE, null,
                    ex);
            i = -1;
        }
        closeConnection(stmt);
        return i;
    }

    /**
     * 执行 SQL 语句,返回结果为整型
     * 主要用于执行非查询语句
     * @param cmdText SQL 语句
     * @return 非负数:正常执行; -1:执行错误; -2:连接错误
     */
    public static int ExecSql(Connection conn, String cmdText)
    {
        Statement stmt = getStatement(conn);
        if (stmt == null)
        {
            return -2;
        }
        int i;
        try
        {
            i = stmt.executeUpdate(cmdText);

        } catch (SQLException ex)
        {
            Logger.getLogger(SqlHelper.class.getName()).log(Level.SEVERE, null,
                    ex);
            i = -1;
        }
        close(stmt);
        return i;
    }

    /**
     * 执行 SQL 语句,返回结果为整型
     * 主要用于执行非查询语句
     * @param cmdText 需要 ? 参数的 SQL 语句
     * @param cmdParams SQL 语句的参数表
     * @return 非负数:正常执行; -1:执行错误; -2:连接错误
     */
    public static int ExecSql(String cmdText, Object... cmdParams)
    {
        PreparedStatement pstmt = getPreparedStatement(cmdText, cmdParams);
        if (pstmt == null)
        {
            return -2;
        }
        int i;
        try
        {
            i = pstmt.executeUpdate();
        } catch (SQLException ex)
        {
            Logger.getLogger(SqlHelper.class.getName()).log(Level.SEVERE, null,
                    ex);
            i = -1;
        }
        closeConnection(pstmt);
        return i;
    }

    /**
     * 执行 SQL 语句,返回结果为整型
     * 主要用于执行非查询语句
     * @param conn 数据库连接
     * @param cmdText 需要 ? 参数的 SQL 语句
     * @param cmdParams SQL 语句的参数表
     * @return 非负数:正常执行; -1:执行错误; -2:连接错误
     */
    public static int ExecSql(Connection conn, String cmdText, Object... cmdParams)
    {
        PreparedStatement pstmt = getPreparedStatement(conn, cmdText, cmdParams);
        if (pstmt == null)
        {
            return -2;
        }
        int i;
        try
        {
            i = pstmt.executeUpdate();

        } catch (SQLException ex)
        {
            Logger.getLogger(SqlHelper.class.getName()).log(Level.SEVERE, null, ex);
            i = -1;
        }
        close(pstmt);
        return i;
    }

    /**
     * 返回结果集的第一行的一列的值,其他忽略
     * @param cmdText SQL 语句
     * @return
     */
    public static Object ExecScalar(String cmdText)
    {
        ResultSet rs = getResultSet(cmdText);
        Object obj = buildScalar(rs);
        closeConnection(rs);
        return obj;
    }

    /**
     * 返回结果集的第一行的一列的值,其他忽略
     * @param conn 数据库连接
     * @param cmdText SQL 语句
     * @return
     */
    public static Object ExecScalar(Connection conn, String cmdText)
    {
        ResultSet rs = getResultSet(conn, cmdText);
        Object obj = buildScalar(rs);
        closeEx(rs);
        return obj;
    }

    /**
     * 返回结果集的第一行的一列的值,其他忽略
     * @param cmdText 需要 ? 参数的 SQL 语句
     * @param cmdParams SQL 语句的参数表
     * @return
     */
    public static Object ExecScalar(String cmdText, Object... cmdParams)
    {
        ResultSet rs = getResultSet(cmdText, cmdParams);
        Object obj = buildScalar(rs);
        closeConnection(rs);
        return obj;
    }

    /**
     * 返回结果集的第一行的一列的值,其他忽略
     * @param conn 数据库连接
     * @param cmdText 需要 ? 参数的 SQL 语句
     * @param cmdParams SQL 语句的参数表
     * @return
     */
    public static Object ExecScalar(Connection conn, String cmdText, Object... cmdParams)
    {
        ResultSet rs = getResultSet(conn, cmdText, cmdParams);
        Object obj = buildScalar(rs);
        closeEx(rs);
        return obj;
    }

    /**
     * 返回一个 ResultSet
     * @param cmdText SQL 语句
     * @return
     */
    public static ResultSet getResultSet(String cmdText)
    {
        Statement stmt = getStatement();
        if (stmt == null)
        {
            return null;
        }
        try
        {
            return stmt.executeQuery(cmdText);
        } catch (SQLException ex)
        {
            Logger.getLogger(SqlHelper.class.getName()).log(Level.SEVERE, null, ex);
            closeConnection(stmt);
        }
        return null;
    }

    /**
     * 返回一个 ResultSet
     * @param conn
     * @param cmdText SQL 语句
     * @return
     */
    public static ResultSet getResultSet(Connection conn, String cmdText)
    {
        Statement stmt = getStatement(conn);
        if (stmt == null)
        {
            return null;
        }
        try
        {
            return stmt.executeQuery(cmdText);
        } catch (SQLException ex)
        {
            Logger.getLogger(SqlHelper.class.getName()).log(Level.SEVERE, null, ex);
            close(stmt);
        }
        return null;
    }

    /**
     * 返回一个 ResultSet
     * @param cmdText 需要 ? 参数的 SQL 语句
     * @param cmdParams SQL 语句的参数表
     * @return
     */
    public static ResultSet getResultSet(String cmdText, Object... cmdParams)
    {
        PreparedStatement pstmt = getPreparedStatement(cmdText, cmdParams);
        if (pstmt == null)
        {
            return null;
        }
        try
        {
            return pstmt.executeQuery();
        } catch (SQLException ex)
        {
            Logger.getLogger(SqlHelper.class.getName()).log(Level.SEVERE, null, ex);
            closeConnection(pstmt);
        }
        return null;
    }

    /**
     * 返回一个 ResultSet
     * @param conn 数据库连接
     * @param cmdText 需要 ? 参数的 SQL 语句
     * @param cmdParams SQL 语句的参数表
     * @return
     */
    public static ResultSet getResultSet(Connection conn, String cmdText, Object... cmdParams)
    {
        PreparedStatement pstmt = getPreparedStatement(conn, cmdText, cmdParams);
        if (pstmt == null)
        {
            return null;
        }
        try
        {
            return pstmt.executeQuery();
        } catch (SQLException ex)
        {
            Logger.getLogger(SqlHelper.class.getName()).log(Level.SEVERE, null, ex);
            close(pstmt);
        }
        return null;
    }

    public static Object buildScalar(ResultSet rs)
    {
        if (rs == null)
        {
            return null;
        }
        Object obj = null;
        try
        {
            if (rs.next())
            {
                obj = rs.getObject(1);
            }
        } catch (SQLException ex)
        {
            Logger.getLogger(SqlHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return obj;
    }

    /**
     * 从 ResultSet 中构建 DefaultTableModel
     * @param rs ResultSet
     * @return
     */
    public static DefaultTableModel buildTableModel(ResultSet rs)
    {
        if (rs == null)
        {
            return null;
        }
        ResultSetMetaData rsm;
        String[] columnNames = null; // 列标题
        Object[][] data = null; // 数据项
        DefaultTableModel model; // 表格模型
        try
        {
            // 查询语句
            rsm = rs.getMetaData();
            // 判断时候可以实现对数据库的更新
            if (rs.getConcurrency() == ResultSet.CONCUR_UPDATABLE)
            {
                System.out.println("Can UPDATABLE");
            } else
            {
                System.out.println("Only Ready");
            }
            // 获取列标题
            columnNames = new String[rsm.getColumnCount()];
            for (int i = 0; i < rsm.getColumnCount(); i++)
            {
                columnNames[i] = rsm.getColumnName(i + 1);
            }

            int row = 0;
            int colum = 0;
            int columCount = rsm.getColumnCount();
            // 获取行数,没有直接的方法,这里先移动到纪录结尾,获取行号,即为行数,然后再移回来
            rs.last();
            int rowCount = rs.getRow();
            rs.beforeFirst();
            System.out.println("rowCount:" + rowCount);
            // 读取数据到数据项变量
            data = new Object[rowCount][columCount];
            while (rs.next())
            {
                for (colum = 0; colum < rsm.getColumnCount(); colum++)
                {
                    data[row][colum] = rs.getObject(colum + 1);
                }
                row++;
            }
        } catch (SQLException ex)
        {
            Logger.getLogger(SqlHelper.class.getName()).log(Level.SEVERE, null,
                    ex);
            return null;
        }

        // 初始化数据模型
        model = new DefaultTableModel(data, columnNames)
        {

            /**
             * 重写 getColumnClass 可以使表格自动识别数据类型
             */
            @SuppressWarnings("unchecked")
            @Override
            public Class getColumnClass(int c)
            {
                // 这里要对空数据集进行检验
                if (dataVector.isEmpty() == false && getValueAt(0, c) != null)
                {
                    return getValueAt(0, c).getClass();
                } else
                {
                    return Object.class;
                }
            }
        };

        return model;
    }

    /**
     * 获取一个数据模型 该表格模型只能用来显示数据，如果需要更新数据，请使用 DataSet getDataSet(String cmdText)
     *
     * @param conn 数据库连接
     * @param cmdText
     *                能返回一个数据集的查询SQL 语句
     * @return 表格数据模型
     */
    public static DefaultTableModel getTableModel(Connection conn, String cmdText)
    {
        ResultSet rs = getResultSet(conn, cmdText);
        DefaultTableModel tm = buildTableModel(rs);
        closeEx(rs);
        return tm;
    }

    /**
     * 获取一个数据模型 该表格模型只能用来显示数据，如果需要更新数据，请使用 DataSet getDataSet(String cmdText)
     *
     * @param cmdText 需要 ? 参数的 SQL 语句
     * @param cmdParams SQL 语句的参数表
     * @return 表格数据模型
     */
    public static DefaultTableModel getTableModel(String cmdText, Object... cmdParams)
    {
        // 查询语句
        ResultSet rs = getResultSet(cmdText, cmdParams);
        DefaultTableModel tm = buildTableModel(rs);
        closeConnection(rs);
        return tm;
    }

    /**
     * 获取一个数据模型 该表格模型只能用来显示数据，如果需要更新数据，请使用 DataSet getDataSet(String cmdText)
     * 
     * @param cmdText
     *                能返回一个数据集的查询SQL 语句
     * @return 表格数据模型
     */
    public static DefaultTableModel getTableModel(String cmdText)
    {
        // 查询语句
        ResultSet rs = getResultSet(cmdText);
        DefaultTableModel tm = buildTableModel(rs);
        closeConnection(rs);
        return tm;
    }

    /**
     * 获取一个数据模型 该表格模型只能用来显示数据，如果需要更新数据，请使用 DataSet getDataSet(String cmdText)
     * @param conn 数据库连接
     * @param cmdText 需要 ? 参数的 SQL 语句
     * @param cmdParams SQL 语句的参数表
     * @return 表格数据模型
     */
    public static DefaultTableModel getTableModel(Connection conn, String cmdText, Object... cmdParams)
    {
        ResultSet rs = getResultSet(conn, cmdText, cmdParams);
        DefaultTableModel tm = buildTableModel(rs);
        closeEx(rs);
        return tm;
    }

    private static void close(Object obj)
    {
        if (obj == null)
        {
            return;
        }
        try
        {
            if (obj instanceof Statement)
            {
                ((Statement) obj).close();
            } else if (obj instanceof PreparedStatement)
            {
                ((PreparedStatement) obj).close();
            } else if (obj instanceof ResultSet)
            {
                ((ResultSet) obj).close();
            } else if (obj instanceof Connection)
            {
                ((Connection) obj).close();
            }
        } catch (SQLException ex)
        {
            Logger.getLogger(SqlHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void closeEx(Object obj)
    {
        if (obj == null)
        {
            return;
        }

        try
        {
            if (obj instanceof Statement)
            {
                ((Statement) obj).close();
            } else if (obj instanceof PreparedStatement)
            {
                ((PreparedStatement) obj).close();
            } else if (obj instanceof ResultSet)
            {
                ((ResultSet) obj).getStatement().close();
            } else if (obj instanceof Connection)
            {
                ((Connection) obj).close();
            }
        } catch (SQLException ex)
        {
            Logger.getLogger(SqlHelper.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private static void closeConnection(Object obj)
    {
        if (obj == null)
        {
            return;
        }
        try
        {
            if (obj instanceof Statement)
            {
                ((Statement) obj).getConnection().close();
            } else if (obj instanceof PreparedStatement)
            {
                ((PreparedStatement) obj).getConnection().close();
            } else if (obj instanceof ResultSet)
            {
                ((ResultSet) obj).getStatement().getConnection().close();
            } else if (obj instanceof Connection)
            {
                ((Connection) obj).close();
            }
        } catch (SQLException ex)
        {
            Logger.getLogger(SqlHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
