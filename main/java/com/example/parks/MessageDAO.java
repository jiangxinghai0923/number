package com.example.parks;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO implements bbc<Message,Integer>{
    static String url="jdbc:mysql://localhost:3306";

    static   String user="root";

    static  String password="123456";
    private final String insert = "INSERT INTO vehicle(VehicleID,LicensePlate,VehicleType,VehicleColor,OwnerName,OwnerContact) VALUES(?,?,?,?,?,?)";
    private final String update2="UPDATE vehicle SET LicensePlate=?,VehicleType=?,VehicleColor=?,OwnerName=?,OwnerContact=? WHERE VehicleID=?";
    private final String delete="DELETE FROM vehicle WHERE VehicleID=?,";

    public MessageDAO(Connection conn){

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
    public void insert(Message entity) throws SQLException {
        try (Connection connection = JDbc.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insert)) {
            preparedStatement.setString(1, entity.getVehicleID());
            preparedStatement.setString(2, entity.getLicensePlate());
            preparedStatement.setString(3, entity.getVehicleType());
            preparedStatement.setString(4, entity.getVehicleColor());
            preparedStatement.setString(5, entity.getOwnerName());
            preparedStatement.setString(6, entity.getOwnerContact());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void updateByid(Message entity, String VehicleID) throws SQLException {
        try (Connection connection=JDbc.getConnection();
             PreparedStatement preparedStatement=connection.prepareStatement(update2)){
            preparedStatement.setString(6, entity.getVehicleID());
            preparedStatement.setString(1, entity.getLicensePlate());
            preparedStatement.setString(2,entity.getVehicleType() );
            preparedStatement.setString(3, entity.getVehicleColor());
            preparedStatement.setString(4, entity.getOwnerName());
            preparedStatement.setString(5, entity.getOwnerContact());
//            preparedStatement.setString(7, primaryKey);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void updateByCondition(String condition, Object[] params, Message entity) throws SQLException {

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

    /**
     * 
     * @param primaryKey
     * @return
     * @throws SQLException
     */
    @Override
    public Message select(Integer primaryKey) throws SQLException {
        Message mm=null;
        String select1="SELECT * FROM vehicle WHERE VehicleID = ?";
        try (Connection connection=JDbc.getConnection();
             PreparedStatement preparedStatement= connection.prepareStatement(select1)){
            preparedStatement.setInt(1,primaryKey);
            System.out.println("Executing SQL: " + preparedStatement.toString());
            try(ResultSet resultSet=preparedStatement.executeQuery()){
                if(resultSet.next()){
                    mm=mapResultSetToEntity(resultSet);
                }
            }
        }
        return mm;
    }

    /**
     *
     * @return
     * @throws SQLException
     */
    @Override
    public List<Message> selectAll() throws SQLException {

        List<Message> member = new ArrayList<>();
        String select2 = "SELECT * FROM vehicle";
        try (Connection connection = JDbc.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(select2)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    member.add(mapResultSetToEntity(resultSet));
                }
            }
        }
        return member;
    }

    /**
     *
     * @param conditions
     * @return
     * @throws SQLException
     */
    @Override
    public List<Message> selectByCondition(String conditions) throws SQLException {
        List<Message> member = new ArrayList<>();

        String query = "SELECT * FROM vehicle WHERE " + conditions;

        try (Connection connection = JDbc.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Message member1 = new Message(
                        resultSet.getString("VehicleID"),
                        resultSet.getString("LicensePlate"),
                        resultSet.getString("VehicleType"),
                        resultSet.getString("VehicleColor"),
                        resultSet.getString("OwnerName"),
                        resultSet.getString("OwnerContact")
                );
                member.add(member1);
            }
        }

        return member;
    }

    /**
     *
     * @param page
     * @param pagesize
     * @return
     * @throws SQLException
     */
    @Override
    public List<Message> selectPaged(int page, int pagesize) throws SQLException {
        List<Message> mm = new ArrayList<>();
        String select4 = "SELECT * FROM vehicle LIMIT ?,?";
        try(Connection connection=JDbc.getConnection();
            PreparedStatement preparedStatement=connection.prepareStatement(select4)) {
            preparedStatement.setInt(1,page*pagesize);
            preparedStatement.setInt(2,pagesize);
            try(ResultSet resultSet=preparedStatement.executeQuery()){
                while(resultSet.next()){
                    mm.add(mapResultSetToEntity(resultSet));
                }
            }
        }
        return mm;
    }

    /**
     *
     * @return
     * @throws Exception
     */
    @Override
    public int gettotalRecords() throws Exception {
        String sql="SELECT COUNT(*) FROM vehicle";
        try(Connection connection=JDbc.getConnection();
            PreparedStatement preparedStatement= connection.prepareStatement(sql);
            ResultSet rs=preparedStatement.executeQuery()) {
            if(rs.next()){
                return rs.getInt(1);
            }
        }
        return 0;
    }

    /**
     *
     * @param resultSet
     * @return
     * @throws SQLException
     */
    private Message mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        Message member = new Message(
                resultSet.getString("VehicleID"),
                resultSet.getString("LicensePlate"),
                resultSet.getString("VehicleType"),
                resultSet.getString("VehicleColor"),
                resultSet.getString("OwnerName"),
                resultSet.getString("OwnerContact"));
        member.setVehicleID(resultSet.getString("VehicleID"));
        member.setLicensePlate(resultSet.getString("LicensePlate"));
        member.setVehicleType(resultSet.getString("VehicleType"));
        member.setVehicleColor(resultSet.getString("VehicleColor"));
        member.setOwnerName(resultSet.getString("OwnerName"));
        member.setOwnerContact(resultSet.getString("OwnerContact"));
        return member;
    }
}


