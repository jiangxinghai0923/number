package com.example.parks;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.poi.ss.formula.functions.T;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class controller3 {

    @FXML
    private TextField SpaceID2;

    @FXML
    private TextField Plate2;

    @FXML
    private TextField tel;

    @FXML
    private TextField Name;

    @FXML
    private TableView<Message> vehicle;

    @FXML
    private TableColumn<Message, String> VehicleID;

    @FXML
    private TableColumn<Message, String> LicensePlate;

    @FXML
    private TableColumn<Message, String> VehicleType;

    @FXML
    private TableColumn<Message, String> OwnerName;

    @FXML
    private TableColumn<Message, String> OwnerContact;

    @FXML
    private TableColumn<Message, String> VehicleColor;

    @FXML
    private Button search2;

    @FXML
    private Button fix;

    @FXML
    private Button add2;

    @FXML
    private TextField Color;

    @FXML
    private Pagination pages2;

    @FXML
    private TextField Type2;

    private int itempage=100;

    @FXML
    void change(ActionEvent event) {

    }

    @FXML
    void secadd(ActionEvent event) {
        String VehicleID=SpaceID2.getText();
        String LicensePlate = Plate2.getText();
        String VehicleType = Type2.getText();
        String VehicleColor = Color.getText();
        String OwnerName= Name.getText();
        String OwnerContact= tel.getText();
        Message newmm=new Message(VehicleID,LicensePlate,VehicleType,VehicleColor,OwnerName,OwnerContact);
        System.out.println(newmm);
        try (Connection connection=JDbc.getConnection()){
            MessageDAO mmDao=new MessageDAO(connection);
            mmDao.insert(newmm);
        }catch (Exception e){
            e.printStackTrace();
        }
        clearInputFields();
        loadPageData(0);
    }

    @FXML
    void DELETE(ActionEvent event) {
        Message demember = vehicle.getSelectionModel().getSelectedItem();
        if (demember != null) {
            try (Connection connection = JDbc.getConnection()) {
                MessageDAO mmDao = new MessageDAO(connection);
                // 在数据库中删除选定的信息
                mmDao.delete(Integer.valueOf(demember.getVehicleID()));
                // 重新加载当前页的数据
                loadPageData(pages2.getCurrentPageIndex());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // 如果没有选择数据，显示警告或通知用户选择一个数据
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("警告");
            alert.setHeaderText(null);
            alert.setContentText("请选择一个用户以执行删除操作。");
            alert.showAndWait();
        }
    }
    @FXML
    void scosearch(ActionEvent event) {//执行查询操作，根据车位编号和车牌
        String searchId = SpaceID2.getText().trim();
        String searchName = Plate2.getText().trim();

        if (!searchId.isEmpty() && !searchName.isEmpty()) {
            // 同时输入了 ID 和姓名
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("搜索提示");
            alert.setHeaderText(null);
            alert.setContentText("请只输入 ID 或车牌进行搜索。");
            alert.showAndWait();
        } else if (!searchId.isEmpty()) {
            // 执行 ID 搜索
            try (Connection connection = JDbc.getConnection()) {
                MessageDAO mmDAO = new MessageDAO(connection);
                int memberId = Integer.parseInt(searchId);
                Message foundMember = mmDAO.select(memberId);

                if (foundMember != null) {
                    ObservableList<Message> searchResult = FXCollections.observableArrayList(foundMember);
                    updateTableView(searchResult);
//                    button1.setVisible(false);
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("搜索结果");
                    alert.setHeaderText(null);
                    alert.setContentText("未找到 ID 为：" + memberId + " 的成员");
                    alert.showAndWait();
                }
            } catch (SQLException | NumberFormatException e) {
                e.printStackTrace();
            }
        } else if (!searchName.isEmpty()) {
            // 执行姓名搜索
            try (Connection connection = JDbc.getConnection()) {
                MessageDAO mmDAO = new MessageDAO(connection);
                List<Message> searchResult = mmDAO.selectByCondition("LicensePlate='" + searchName + "'");
                ObservableList<Message> observableList = FXCollections.observableArrayList(searchResult);
                updateTableView(observableList);
            } catch (SQLException e) {
                e.printStackTrace();
            }
//            button1.setVisible(false);
        } else {
            // 未输入 ID 或姓名
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("搜索提示");
            alert.setHeaderText(null);
            alert.setContentText("请输入要搜索的 ID 或姓名。");
            alert.showAndWait();
        }

    }

    @FXML
    void Return(ActionEvent event){
        clearInputFields();
        vehicle.refresh();
        loadPageData(pages2.getCurrentPageIndex());
    }

    @FXML
    void EDIT(ActionEvent event) {
        Message selectedMember = vehicle.getSelectionModel().getSelectedItem();

        if (selectedMember != null) {
            // 将选定的用户信息显示在左边输入区域
            SpaceID2.setText(selectedMember.getVehicleID());
            Plate2.setText(selectedMember.getLicensePlate());
            Type2.setText(selectedMember.getVehicleType());
            Color.setText(selectedMember.getVehicleColor());;
            Name.setText(selectedMember.getOwnerName());;
            tel.setText(selectedMember.getOwnerContact());
        } else {
            // 如果没有选择用户，显示警告或通知用户选择一个用户
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("警告");
            alert.setHeaderText(null);
            alert.setContentText("请选择一个用户以执行编辑操作。");
            alert.showAndWait();
        }
    }
    @FXML
    void saveEdit(ActionEvent event) {
// 获取编辑后的信息
        String editedVehicleID =SpaceID2.getText();
        String editedVehicleType = Type2.getText();
        String editedOwnerContact = tel.getText();
        String editedVehicleColor = Color.getText();
        String editedOwnerName = Name.getText();
        String editedLicensePlate= Plate2.getText();

        // 更新选定用户的信息
        Message selectedMember = new Message();
        selectedMember.setVehicleType(editedVehicleType);
        selectedMember.setVehicleID(editedVehicleID);
        selectedMember.setOwnerContact(editedOwnerContact);
        selectedMember.setVehicleColor(editedVehicleColor);
        selectedMember.setOwnerName(editedOwnerName);
        selectedMember.setLicensePlate(editedLicensePlate);
        try (Connection connection = JDbc.getConnection()) {
            MessageDAO mmDao = new MessageDAO(connection);
            // 将更改保存到数据库
            mmDao.updateByid(selectedMember,editedVehicleID);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 刷新表格视图
        vehicle.refresh();
        // 清空输入区域
        clearInputFields();
        SpaceID2.setDisable(false);
        // 重新加载当前页的数据
        loadPageData(pages2.getCurrentPageIndex());
    }

    @FXML
    private void initialize() throws Exception{
// 使用用户数据更新TableView
        initializepage();
        vehicle.setRowFactory(tv -> {
            TableRow<Message> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    // 处理双击事件（调用 EDIT 方法）
                    EDIT(new ActionEvent());
                }
            });
            return row;
        });
        loadPageData(0);
    }
    private void initializepage(){
        pages2.setPageCount(CalculatepageCount());
        pages2.currentPageIndexProperty().addListener((observable, oldValue, newValue) -> {
            loadPageData(newValue.intValue());
        });
    }
    private int CalculatepageCount(){
        try (Connection connection=JDbc.getConnection()){
            MessageDAO mmDao=new MessageDAO(connection);
            return(int)Math.ceil((double) mmDao.gettotalRecords()/itempage);

        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    private void clearInputFields() {
        // 清空文本框
        SpaceID2.clear();
        Type2.clear();
        tel.clear();
        Plate2.clear();
        Name.clear();
        Color.clear();
    }

    private void loadPageData(int pageindex) {
        try (Connection connection = JDbc.getConnection()) {
            MessageDAO mmDAO = new MessageDAO(connection);
            List<Message> pageData = mmDAO.selectPaged(pageindex, itempage);
            updateTableView(pageData);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void updateTableView(List<Message> message) {

        vehicle.getItems().setAll(message);
// 为每列设置单元格值工厂，以从用户对象中获取属性值显示。
        VehicleID.setCellValueFactory(new PropertyValueFactory<>
                ("VehicleID"));
        LicensePlate.setCellValueFactory(new PropertyValueFactory<>
                ("LicensePlate"));
        VehicleType.setCellValueFactory(new PropertyValueFactory<>
                ("VehicleType"));
        VehicleColor.setCellValueFactory(new PropertyValueFactory<>
                ("VehicleColor"));
        OwnerName.setCellValueFactory(new PropertyValueFactory<>
                ("OwnerName"));
        OwnerContact.setCellValueFactory(new PropertyValueFactory<>
                ("OwnerContact"));
    }

//    private List<Members> loadData()  {
//        try (Connection conn = DruidDataSourceUtil.getConnection()) {// 获取
//
//// 创建用户数据访问对象
//            memberDao = new MembersDAO(conn);
//// 从数据库中查询所有用户数据
//            return memberDao.selectAll();
//        } catch (Exception e) {
//// 异常处理，打印异常信息
//            e.printStackTrace();
//// 返回空列表表示加载数据失败
//            return null;
//        }
//    }

}
