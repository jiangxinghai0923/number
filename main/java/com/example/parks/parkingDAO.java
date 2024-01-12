package com.example.parks;

import java.sql.Connection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class parkingDAO implements bbc<parking,Integer>{
    static String url="jdbc:mysql://localhost:3306";

    static   String user="root";

    static  String password="123456";
    private final String insert = "INSERT INTO parkingspace  VALUES(?,?,?,?,?)";
    private  String update1 = "UPDATE parkingspace SET spaceType=?, spaceStatus=?, spaceLocation=?, licensePlate=? WHERE spaceID=?";
    private static final String delete1="DELETE FROM parkingspace WHERE spaceID=?";

    public parkingDAO(Connection coonn){

    }

    public   Connection getConnection() {
        Connection conn=null;

        {
            try {
                conn = DriverManager.getConnection(url, user, password);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return conn;
    }

    @Override
    public void insert(parking entity) throws SQLException {
        try (Connection connection = JDbc.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insert)) {
            preparedStatement.setString(1, entity.getSpaceID());
            preparedStatement.setString(2, entity.getSpaceType());
            preparedStatement.setString(3, entity.getSpaceStatus());
            preparedStatement.setString(4, entity.getSpaceLocation());
            preparedStatement.setString(5, entity.getLicensePlate());
            preparedStatement.executeUpdate();
        }
    }
    @Override
    public void updateByid(parking entity, String primaryKey) throws SQLException {
        try (Connection connection=JDbc.getConnection();
             PreparedStatement preparedStatement=connection.prepareStatement(update1)){
//            preparedStatement.setString(1, entity.getSpaceID());
            preparedStatement.setString(1, entity.getSpaceType());
            preparedStatement.setString(2, entity.getSpaceStatus());
            preparedStatement.setString(3, entity.getSpaceLocation());
            preparedStatement.setString(4, entity.getLicensePlate());
            preparedStatement.setInt(5, Integer.parseInt(primaryKey));
            preparedStatement.executeUpdate();
        }
    }
    @Override
    public void updateByCondition(String condition, Object[] params, parking entity) throws SQLException {

    }
    @Override
    public void delete(Integer primaryKey) throws SQLException {
        try (Connection connection=JDbc.getConnection();
             PreparedStatement preparedStatement= connection.prepareStatement(delete1)){
            preparedStatement.setInt(1,primaryKey);

            preparedStatement.executeUpdate();
        }
    }
    @Override
    public void insertBatch(List entities) throws SQLException {

    }


    @Override
    public void deleteBatch(List primaryKeys) throws SQLException {

    }

    @Override
    public void deleteByCondition(String condition, Object[] params) throws SQLException {

    }

    @Override
    public parking select(Integer primaryKey) throws SQLException {
        parking bp=null;
        String select1="SELECT * FROM parkingspace WHERE SpaceID = ?";
        try (Connection connection=JDbc.getConnection();
             PreparedStatement preparedStatement= connection.prepareStatement(select1)){
            preparedStatement.setInt(1,primaryKey);
            System.out.println("Executing SQL: " + preparedStatement.toString());
            try(ResultSet resultSet=preparedStatement.executeQuery()){
                if(resultSet.next()){
                    bp=mapResultSetToEntity(resultSet);
                }
            }
        }
        return bp;
    }
    @Override
    public List<parking> selectAll() throws SQLException {

        List<parking> bp = new ArrayList<>();
        String select2 = "SELECT * FROM parkingspace";
        try (Connection connection = JDbc.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(select2)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    bp.add(mapResultSetToEntity(resultSet));
                }
            }
        }
        return bp;
    }
    @Override
    public List<parking> selectByCondition(String conditions) throws SQLException {
        List<parking> bp = new ArrayList<>();

        String query = "SELECT * FROM parkingspace WHERE " + conditions;

        try (Connection connection = JDbc.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                parking bp1 = new parking(
                        resultSet.getString("SpaceID"),
                        resultSet.getString("SpaceType"),
                        resultSet.getString("SpaceStatus"),
                        resultSet.getString("SpaceLocation"),
                        resultSet.getString("LicensePlate")
                );
                bp.add(bp1);
            }
        }

        return bp;
    }
    @Override
    public List<parking> selectPaged(int page, int pagesize) throws SQLException {
        List<parking> bp = new ArrayList<>();
        String select4 = "SELECT * FROM parkingspace LIMIT ?,?";
        try(Connection connection=JDbc.getConnection();
            PreparedStatement preparedStatement=connection.prepareStatement(select4)) {
            preparedStatement.setInt(1,page*pagesize);
            preparedStatement.setInt(2,pagesize);
            try(ResultSet resultSet=preparedStatement.executeQuery()){
                while(resultSet.next()){
                    bp.add(mapResultSetToEntity(resultSet));
                }
            }
        }
        return bp;
    }
    @Override
    public int gettotalRecords() throws Exception {
        String sql="SELECT COUNT(*) FROM parkingspace";
        try(Connection connection=JDbc.getConnection();
            PreparedStatement preparedStatement= connection.prepareStatement(sql);
            ResultSet rs=preparedStatement.executeQuery()) {
            if(rs.next()){
                return rs.getInt(1);
            }
        }
        return 0;
    }
    private parking mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        parking bp = new parking(
                resultSet.getString("SpaceID"),
                resultSet.getString("SpaceType"),
                resultSet.getString("SpaceStatus"),
                resultSet.getString("SpaceLocation"),
                resultSet.getString("LicensePlate"));
        bp.setSpaceID(resultSet.getString("SpaceID"));
        bp.setSpaceType(resultSet.getString("SpaceType"));
        bp.setSpaceStatus(resultSet.getString("SpaceStatus"));
        bp.setLicensePlate(resultSet.getString("LicensePlate"));
        return bp;
    }
}

