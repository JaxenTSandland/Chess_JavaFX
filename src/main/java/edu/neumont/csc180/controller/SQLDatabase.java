/**
 * @author jsandland
 * @createdOn 8/23/2024 at 10:14 PM
 * @projectName JaxenS_ChessJavaFX
 * @packageName edu.neumont.csc180.controller;
 */
package edu.neumont.csc180.controller;

import java.sql.*;

public class SQLDatabase {
    static String sqlUrl = "jdbc:mysql://localhost:3306/csc180?allowPublicKeyRetrieval=true&useSSL=false";
    static String sqlUser = "root";
    static String sqlPassword = "test";

    public static String getColumnValue(String getColumn, String username) {
        return getColumnValue(getColumn, "username", username);
    }

    public static String getColumnValue(String getColumn, String whereColumn, String whereValue){
        String sql = "select " + getColumn + " from chessData where " + whereColumn +" =(?)";

        try {
            Connection con = DriverManager.getConnection(sqlUrl, sqlUser, sqlPassword);
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, whereValue);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                return rs.getNString(1);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static void setColumnValue(String setColumn, String setValue, String username) {
        String sql = "update chessData set " + setColumn + " =('" + setValue + "') where username = ('?')";
        try {
            Connection con = DriverManager.getConnection(sqlUrl, sqlUser, sqlPassword);
            PreparedStatement pst = con.prepareStatement(sql);

            pst.setString(1, username);

            pst.executeUpdate();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static boolean createAccount(String username, String password){
        String sql = "insert into chessData (username, password) values (?, ?)";

        try {
            Connection con = DriverManager.getConnection(sqlUrl, sqlUser, sqlPassword);
            PreparedStatement pst = con.prepareStatement(sql);


            pst.setString(1, username);
            pst.setString(2, password);

            pst.executeUpdate();
            return true;
        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public static boolean saveBoard(String username,
                                    String wKing, String wQueen, String wBishop, String wRook, String wKnight, String wPawn,
                                    String bKing, String bQueen, String bBishop, String bRook, String bKnight, String bPawn) {

        String sql = "UPDATE chessData SET " +
                "wKing = ?, wQueen = ?, wBishop = ?, wRook = ?, wKnight = ?, wPawn = ?, " +
                "bKing = ?, bQueen = ?, bBishop = ?, bRook = ?, bKnight = ?, bPawn = ? " +
                "WHERE username = ?";

        // Use a PreparedStatement to safely inject parameters into the SQL query
        try (Connection conn = DriverManager.getConnection(sqlUrl, sqlUser, sqlPassword);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Set the parameters for the PreparedStatement
            pstmt.setString(1, wKing);
            pstmt.setString(2, wQueen);
            pstmt.setString(3, wBishop);
            pstmt.setString(4, wRook);
            pstmt.setString(5, wKnight);
            pstmt.setString(6, wPawn);
            pstmt.setString(7, bKing);
            pstmt.setString(8, bQueen);
            pstmt.setString(9, bBishop);
            pstmt.setString(10, bRook);
            pstmt.setString(11, bKnight);
            pstmt.setString(12, bPawn);
            pstmt.setString(13, username);


            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void clearSave(String username) {
        String sql = "UPDATE chessData SET wKing = '' WHERE username = (?)";

        try (Connection conn = DriverManager.getConnection(sqlUrl, sqlUser, sqlPassword);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);


            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean usernameExists(String username) {
        String sql = "select 1 from chessData where username = (?)";

        try (Connection conn = DriverManager.getConnection(sqlUrl, sqlUser, sqlPassword);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next(); // If there is a result, the username exists
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
