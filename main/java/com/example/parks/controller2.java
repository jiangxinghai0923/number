package com.example.parks;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;


public class controller2 {

    @FXML
    private TextField SpaceID1;

    @FXML
    private TextField SpaceType1;

    @FXML
    private TextField SpaceLocation1;

    @FXML
    private TextField Plate1;

    @FXML
    private TextField Status;

    @FXML
    private TextField spaceStatus;

    @FXML
    private TableView<parking> biao;

    @FXML
    private TableColumn<parking, String> spaceID;

    @FXML
    private TableColumn<parking, String> spaceType;

    @FXML
    private TableColumn<parking, String> spaceLocation;

    @FXML
    private TableColumn<parking, String> licensePlate;

    @FXML
    private TableColumn<parking, String> spaceStats;

    @FXML
    private Pagination pages;

    private int itempage=100;

    @FXML
    void add(ActionEvent event) {//增加数据
        String spaceID = SpaceID1.getText();
        String spaceType = SpaceType1.getText();
        String spaceStatus = Status.getText();
        String spaceLocation = SpaceLocation1.getText();
        String licensePlate= Plate1.getText();
        parking newbp = new parking(spaceID, spaceType, spaceLocation, licensePlate,spaceStatus);
        System.out.println(newbp);
        try (Connection connection=JDbc.getConnection()){
            parkingDAO bpDao=new parkingDAO(connection);
            bpDao.insert(newbp);
        }catch (Exception e){
            e.printStackTrace();
        }
        clearInputFields();
        loadPageData(0);

    }
    @FXML
    void DELETE(ActionEvent event) {
        parking debp = biao.getSelectionModel().getSelectedItem();
        if (debp != null) {
            try (Connection connection = JDbc.getConnection()) {
                parkingDAO bpDao = new parkingDAO(connection);
                // 在数据库中删除选定的美容品
                bpDao.delete(Integer.valueOf(debp.getSpaceID()));
                // 重新加载当前页的数据
                loadPageData(pages.getCurrentPageIndex());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // 如果没有选择车辆信息，显示警告或通知用户选择一个美容品
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("警告");
            alert.setHeaderText(null);
            alert.setContentText("请选择一个车辆信息以执行删除操作。");
            alert.showAndWait();
        }
    }
    @FXML
    void Search(ActionEvent event) {//查询操作，根据ID和车位查询
        String searchId = SpaceID1.getText().trim();
        String searchName = SpaceLocation1.getText().trim();

        if (!searchId.isEmpty() && !searchName.isEmpty()) {
            // 同时输入了 ID 和 name
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("搜索提示");
            alert.setHeaderText(null);
            alert.setContentText("请输入 ID 进行搜索。");
            alert.showAndWait();
        } else if (!searchId.isEmpty()) {
            // 执行 ID 搜索
            try (Connection connection = JDbc.getConnection()) {
                parkingDAO bpDAO = new parkingDAO(connection);
                int bpId = Integer.parseInt(searchId);
                parking foundbp = bpDAO.select(bpId);

                if (foundbp != null) {
                    ObservableList<parking> searchResult = FXCollections.observableArrayList(foundbp);
                    updateTableView(searchResult);
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("搜索结果");
                    alert.setHeaderText(null);
                    alert.setContentText("未找到 ID 为：" + bpId + " 的成员");
                    alert.showAndWait();
                }
            } catch (SQLException | NumberFormatException e) {
                e.printStackTrace();
            }
        } else if (!searchName.isEmpty()) {
            // 执行姓名搜索
            try (Connection connection = JDbc.getConnection()) {
                parkingDAO bpDAO = new parkingDAO(connection);
                List<parking> searchResult = bpDAO.selectByCondition("spaceLocation='" + searchName + "'");
                ObservableList<parking> observableList = FXCollections.observableArrayList(searchResult);
                updateTableView(observableList);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            // 未输入 ID 或名字
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("搜索提示");
            alert.setHeaderText(null);
            alert.setContentText("请输入要搜索的 ID 或车位。");
            alert.showAndWait();
        }
    }

    @FXML
    void Return(ActionEvent event){
        clearInputFields();
        biao.refresh();
        loadPageData(pages.getCurrentPageIndex());
    }

    @FXML
    void EDIT(ActionEvent event) {//执行修改操作
        parking selectedBp = biao.getSelectionModel().getSelectedItem();

        if (selectedBp != null) {
            // 将选定的美容品信息显示在左边输入区域
            SpaceID1.setText(selectedBp.getSpaceID());
            SpaceType1.setText(selectedBp.getSpaceType());
            SpaceLocation1.setText(selectedBp.getSpaceLocation());
            Plate1.setText(selectedBp.getLicensePlate());
            Status.setText(selectedBp.getSpaceStatus());;
            SpaceID1.setDisable(true);
        } else {
            // 如果没有选择车辆信息，显示警告或通知用户选择一个美容品
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("警告");
            alert.setHeaderText(null);
            alert.setContentText("请选择一个车辆信息以执行编辑操作。");
            alert.showAndWait();
        }
    }
    @FXML
    void saveEdit(ActionEvent event) {//保存编辑后的操作
// 获取编辑后的信息
        String editedSapceID=SpaceID1.getText();
        String editedType = SpaceType1.getText();
        String editedSpaceLocation = SpaceLocation1.getText();
        String editedPlate = Plate1.getText();
       String editedStatus= Status.getText();
        // 更新选定用户的信息
        parking selectedBp = biao.getSelectionModel().getSelectedItem();
        selectedBp.setSpaceType(editedType);
        selectedBp.setSpaceID(editedSapceID);
        selectedBp.setSpaceLocation(editedSpaceLocation);
        selectedBp.setLicensePlate(editedPlate);
        selectedBp.setSpaceStatus(editedStatus);

        try (Connection connection = JDbc.getConnection()) {
            parkingDAO bpDao = new parkingDAO(connection);
            // 将更改保存到数据库
            bpDao.updateByid(selectedBp,editedSapceID);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 刷新表格视图
        biao.refresh();
        // 清空输入区域
        clearInputFields();
        SpaceID1.setDisable(false);
        // 重新加载当前页的数据
        loadPageData(pages.getCurrentPageIndex());
    }

    @FXML
    private void initialize() throws Exception{
// 使用用户数据更新TableView
        initializepage();
        biao.setRowFactory(tv -> {
            TableRow<parking> row = new TableRow<>();
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
        pages.setPageCount(CalculatepageCount());
        pages.currentPageIndexProperty().addListener((observable, oldValue, newValue) -> {
            loadPageData(newValue.intValue());
        });
    }
    private int CalculatepageCount(){
        try (Connection connection=JDbc.getConnection()){
            parkingDAO bpDao=new parkingDAO(connection);
            return(int)Math.ceil((double) bpDao.gettotalRecords()/itempage);

        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    private void clearInputFields() {
        // 清空文本框
        SpaceID1.clear();
        SpaceType1.clear();
        SpaceLocation1.clear();
        Plate1.clear();
        Status.clear();
    }
    private void loadPageData(int pageindex) {
        try (Connection connection = JDbc.getConnection()) {
            parkingDAO bpDAO = new parkingDAO(connection);
            List<parking> pageData = bpDAO.selectPaged(pageindex, itempage);
            updateTableView(pageData);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void updateTableView(List<parking> parking) {

        biao.getItems().setAll(parking);
// 为每列设置单元格值工厂，以从用户对象中获取属性值显示。
        spaceID.setCellValueFactory(new PropertyValueFactory<>
                ("spaceID"));
        spaceType.setCellValueFactory(new PropertyValueFactory<>
                ("spaceType"));
        spaceLocation.setCellValueFactory(new PropertyValueFactory<>
                ("spaceLocation"));
        licensePlate.setCellValueFactory(new PropertyValueFactory<>
                ("licensePlate"));
        spaceStats.setCellValueFactory(new PropertyValueFactory<>
                ("spaceStatus"));
    }


}
