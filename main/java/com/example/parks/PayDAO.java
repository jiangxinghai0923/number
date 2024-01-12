package com.example.parks;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PayDAO implements bbc<Pay,Integer>{
    static String url="jdbc:mysql://localhost:3306";

    static   String user="root";

    static  String password="123456";
    private final String insert = "INSERT INTO  pay_money (RecordID, LicensePlate, EntryTime, ExitTime, ParkingLocation, ChargeAmount) VALUES (?, ?, ?, ?, ?, ?)";
    private final String update1 ="UPDATE pay_money SET LicensePlate=?,EntryTime=? ,ExitTime=?, ParkingLocation=? ,ChargeAmount=? WHERE RecordID=?";
    private final String delete="DELETE FROM pay_money WHERE RecordID=?";

    public PayDAO(Connection conn){

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
    public void insert(Pay entity) throws SQLException {
        try (Connection connection = JDbc.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insert)) {
            preparedStatement.setString(1, entity.getRecordID());
            preparedStatement.setString(2, entity.getLicensePlate());
            preparedStatement.setString(3, entity.getExitTime());
            preparedStatement.setString(4, entity.getEntryTime());
            preparedStatement.setString(5, entity.getParkingLocation());
            preparedStatement.setString(6, entity.getChargeAmount());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void updateByid(Pay entity, String primaryKey) throws SQLException {
        try (Connection connection=JDbc.getConnection();
             PreparedStatement preparedStatement=connection.prepareStatement(update1)){
            preparedStatement.setString(1, entity.getLicensePlate());
            preparedStatement.setString(2, entity.getEntryTime());
            preparedStatement.setString(3, entity.getExitTime());
            preparedStatement.setString(4, entity.getParkingLocation());
            preparedStatement.setString(5, entity.getChargeAmount());
            preparedStatement.setString(6, primaryKey);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void updateByCondition(String condition, Object[] params, Pay entity) throws SQLException {

    }

    @Override
    public void delete(Integer primaryKey) throws SQLException {
        try (Connection connection=JDbc.getConnection();
             PreparedStatement preparedStatement= connection.prepareStatement(delete)){
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
    public Pay select(Integer primaryKey) throws SQLException {
        Pay pa=null;
        String select1="SELECT * FROM pay_money WHERE RecordID = ?";
        try (Connection connection=JDbc.getConnection();
             PreparedStatement preparedStatement= connection.prepareStatement(select1)){
            preparedStatement.setInt(1,primaryKey);
            System.out.println("Executing SQL: " + preparedStatement.toString());
            try(ResultSet resultSet=preparedStatement.executeQuery()){
                if(resultSet.next()){
                    pa=mapResultSetToEntity(resultSet);
                }
            }
        }
        return pa;
    }

    @Override
    public List<Pay> selectAll() throws SQLException {

        List<Pay> sl = new ArrayList<>();
        String select2 = "SELECT * FROM pay";
        try (Connection connection = JDbc.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(select2)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    sl.add(mapResultSetToEntity(resultSet));
                }
            }
        }
        return sl;
    }

    @Override
    public List<Pay> selectByCondition(String conditions) throws SQLException {
        List<Pay> sl = new ArrayList<>();

        String query = "SELECT * FROM pay_money WHERE " + conditions;

        try (Connection connection = JDbc.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Pay sl1 = new Pay(
                        resultSet.getString("RecordID"),
                        resultSet.getString("LicensePlate"),
                        resultSet.getString("EntryTime"),
                        resultSet.getString("ExitTime"),
                        resultSet.getString("ParkingLocation"),
                        resultSet.getString("ChargeAmount")
                );
                sl.add(sl1);
            }
        }

        return sl;
    }


    @Override
    public List<Pay> selectPaged(int page, int pagesize) throws SQLException {
        List<Pay> sl = new ArrayList<>();
        String select4 = "SELECT * FROM pay_money LIMIT ?,?";
        try(Connection connection=JDbc.getConnection();
            PreparedStatement preparedStatement=connection.prepareStatement(select4)) {
            preparedStatement.setInt(1,page*pagesize);
            preparedStatement.setInt(2,pagesize);
            try(ResultSet resultSet=preparedStatement.executeQuery()){
                while(resultSet.next()){
                    sl.add(mapResultSetToEntity(resultSet));
                }
            }
        }
        return sl;
    }

    @Override
    public int gettotalRecords() throws Exception {
        String sql="SELECT COUNT(*) FROM pay_money";
        try(Connection connection=JDbc.getConnection();
            PreparedStatement preparedStatement= connection.prepareStatement(sql);
            ResultSet rs=preparedStatement.executeQuery()) {
            if(rs.next()){
                return rs.getInt(1);
            }
        }
        return 0;
    }

    private Pay mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        Pay sl = new Pay( resultSet.getString("RecordID"),
                resultSet.getString("LicensePlate"),
                resultSet.getString("EntryTime"),
                resultSet.getString("ExitTime"),
                resultSet.getString("ParkingLocation"),
                resultSet.getString("ChargeAmount"));
        sl.setRecordID(resultSet.getString("RecordID"));
        sl.setLicensePlate(resultSet.getString("LicensePlate"));
        sl.setEntryTime(resultSet.getString("EntryTime"));
        sl.setExitTime(resultSet.getString("ExitTime"));
        sl.setParkingLocation(resultSet.getString("ParkingLocation"));
        sl.setChargeAmount(resultSet.getString("ChargeAmount"));
        return sl;
    }

    public double getPriceByProductID(String productID) throws SQLException {
        double ChargeAmount = 0.0;
        String query = "SELECT ChargeAmount FROM pay_money WHERE RecordID = ?";
        try (Connection connection = JDbc.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, productID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    ChargeAmount = resultSet.getDouble("Price");
                }
            }
        }
        return ChargeAmount;
    }
}
