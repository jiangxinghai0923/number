package com.example.parks;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class controller4 {
private PayDAO payDao;
    @FXML
    private AnchorPane pages;

    @FXML
    private TableView<Pay> ebtryexitcord;

    @FXML
    private TextField ID3;

    @FXML
    private TextField palte3;

    @FXML
    private TextField money;
    @FXML
    private TableColumn<Pay, String> RecordID;
    @FXML
    private TableColumn<Pay, String> LicensePlate;
    @FXML
    private TableColumn<Pay, Date> EntryTime;
    @FXML
    private TableColumn<Pay, Date> ExitTime;
    @FXML
    private TableColumn<Pay, String> ChargeAmount;
    @FXML
    private TableColumn<Pay, String> ParkingLocation;


    @FXML
    private Button addth;

    @FXML
    private Button Dete;

    @FXML
    private TextField coming;

    @FXML
    private TextField outtime;

    @FXML
    private Pagination page3;

    @FXML
    private TextField lot3;
    @FXML
    private Button excel;
    private int itempage=100;

    @FXML
    void comedate(ActionEvent event) {

    }
    @FXML
    void Importing(ActionEvent actionEvent) {
        try {
            // 使用FileChooser对话框选择文件
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("保存Excel文件");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel文件", "*.xlsx"));
            File file = fileChooser.showSaveDialog(null);

            if (file != null) {
                // 创建ExcelWriter
                ExcelWriter excelWriter = EasyExcel.write(file, Pay.class).build();

                // 创建WriteSheet
                WriteSheet writeSheet = EasyExcel.writerSheet("Sheet1").build();

                // 从TableView获取数据
                ObservableList<Pay> membersList = ebtryexitcord.getItems();

                // 将数据写入Excel
                excelWriter.write(membersList, writeSheet);

                // 关闭ExcelWriter
                excelWriter.finish();

                // 显示成功消息
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("导出成功");
                alert.setHeaderText(null);
                alert.setContentText("数据成功导出到Excel文件。");
                alert.showAndWait();
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 如果发生错误，显示错误消息
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("导出错误");
            alert.setHeaderText(null);
            alert.setContentText("导出数据到Excel时发生错误。");
            alert.showAndWait();
        }
    }


    @FXML
    void delete(ActionEvent event) {
        Pay slbp = (Pay) ebtryexitcord.getSelectionModel().getSelectedItem();
        if (slbp != null) {
            try (Connection connection = JDbc.getConnection()) {
                PayDAO slDao = new PayDAO(connection);
                // 在数据库中删除选定的商单
                slDao.delete(Integer.valueOf(slbp.getRecordID()));
                // 重新加载当前页的数据
                loadPageData(page3.getCurrentPageIndex());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // 如果没有选择商单，显示警告或通知用户选择一个商单
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("警告");
            alert.setHeaderText(null);
            alert.setContentText("请选择一条车辆信息。");
            alert.showAndWait();
        }

    }

    @FXML
    void fourth(ActionEvent event) {

    }

    @FXML
    void outdate(ActionEvent event) {

    }
    @FXML
    void search(ActionEvent event) {
        String searchId = ID3.getText();
        String searchName = palte3.getText();
        if (!searchId.isEmpty() && !searchName.isEmpty()) {
            // 同时输入了 ID 和 车牌
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("搜索提示");
            alert.setHeaderText(null);
            alert.setContentText("请输入 ID 进行搜索。");
            alert.showAndWait();
        } else if (!searchId.isEmpty()) {
            // 执行 ID 搜索
            try (Connection connection = JDbc.getConnection()) {
                PayDAO bpDAO = new PayDAO(connection);
                int bpId = Integer.parseInt(searchId);
                Pay foundbp = bpDAO.select(bpId);

                if (foundbp != null) {
                    ObservableList<Pay> searchResult = FXCollections.observableArrayList(foundbp);
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
                PayDAO bpDAO = new PayDAO(connection);
                List<Pay> searchResult = bpDAO.selectByCondition("LicensePlate='" + searchName + "'");
                ObservableList<Pay> observableList = FXCollections.observableArrayList(searchResult);
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
    void thirdadd(ActionEvent event) {
        try {
            // 获取界面输入的销售数据
            String RecordID = ID3.getText();
            String LicensePlate = palte3.getText();
            String ParkingLocation = lot3.getText();
            String EntryTime = coming.getText();
            String Outtime = outtime.getText();
            String ChargeAmount = money.getText();

            // 创建 Sales 对象
            Pay newSale = new Pay(RecordID, LicensePlate, ParkingLocation, ChargeAmount, EntryTime,Outtime);
            System.out.println((newSale));

            // 将销售数据插入数据库
            insertSale(newSale);

            // 刷新表格数据和分页控件
            ebtryexitcord.refresh();
            initializepage();

            // 清除输入框内容
            clearInputFields();
        } catch (SQLException e) {
            e.printStackTrace(); // 处理异常
        }

    }
    private void insertSale(Pay newSale) throws SQLException {
        payDao.insert(newSale);
    }
    @FXML
    void EDIT(ActionEvent event) {
        Pay selectedsl = (Pay) ebtryexitcord.getSelectionModel().getSelectedItem();

        if (selectedsl != null) {
            // 将选定的车辆信息显示在右
            ID3.setText(selectedsl.getRecordID());
            palte3.setText(selectedsl.getLicensePlate());
            lot3.setText(selectedsl.getParkingLocation());
            coming.setText(selectedsl.getEntryTime());
            outtime.setText(selectedsl.getExitTime());
            money.setText(selectedsl.getChargeAmount());
        } else {
            // 如果没有选择车辆信息，显示警告或通知用户选择一个商单
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("警告");
            alert.setHeaderText(null);
            alert.setContentText("请选择一个车辆信息以执行编辑操作。");
            alert.showAndWait();
        }
    }
    @FXML
    void saveEdit(ActionEvent event) {
// 获取编辑后的信息
        String editedRecordID=ID3.getText();
        String editedLicensePlate = palte3.getText();
        String editedParkingLocation = lot3.getText();
        String editedChargeAmount= money.getText();
        String editedEntryTime=coming.getText() ;
        String editedExitTime=outtime.getText() ;
        // 更新选定商单的信息
        Pay selectedSl = (Pay) ebtryexitcord.getSelectionModel().getSelectedItem();
        selectedSl.setRecordID(editedRecordID);
        selectedSl.setLicensePlate(editedLicensePlate);
        selectedSl.setParkingLocation(editedParkingLocation);
        selectedSl.setChargeAmount(editedChargeAmount);
        selectedSl.setEntryTime(editedEntryTime);
        selectedSl.setExitTime(editedExitTime);

        try (Connection connection = JDbc.getConnection()) {
            PayDAO slDao = new PayDAO(connection);
            // 将更改保存到数据库
            slDao.updateByid(selectedSl,editedRecordID);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 刷新表格视图
        ebtryexitcord.refresh();
        // 清空输入区域
        clearInputFields();
        // 重新加载当前页的数据
        loadPageData(page3.getCurrentPageIndex());
    }
//
    @FXML
    private void initialize() throws Exception{
// 使用用户数据更新ebtryexitcord
        initializepage();
        ebtryexitcord.setRowFactory(tv -> {
            TableRow<Pay> row = new TableRow<>();
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
        page3.setPageCount(CalculatepageCount());
        page3.currentPageIndexProperty().addListener((observable, oldValue, newValue) -> {
            loadPageData(newValue.intValue());
        });
    }
    private int CalculatepageCount(){
        try (Connection connection=JDbc.getConnection()){
            PayDAO slDao=new PayDAO(connection);
            return(int)Math.ceil((double) slDao.gettotalRecords()/itempage);

        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    private void clearInputFields() {
        // 清空文本框
        ID3.clear();
        palte3.clear();
        money.clear();
        lot3.clear();
        // 清空日期选择器
        coming.clear();
        outtime.clear();
    }

    private void loadPageData(int pageindex) {
        try (Connection connection = JDbc.getConnection()) {
            PayDAO slDAO = new PayDAO(connection);
            List<Pay> pageData = slDAO.selectPaged(pageindex, itempage);
            updateTableView(pageData);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void updateTableView(List<Pay> pay) {

        ebtryexitcord.getItems().setAll(pay);
// 为每列设置单元格值工厂，以从用户对象中获取属性值显示。
        RecordID.setCellValueFactory(new PropertyValueFactory<>
                ("RecordID"));
        LicensePlate.setCellValueFactory(new PropertyValueFactory<>
                ("LicensePlate"));
        EntryTime.setCellValueFactory(new PropertyValueFactory<>
                ("EntryTime"));
        ExitTime.setCellValueFactory(new PropertyValueFactory<>
                ("ExitTime"));
        ParkingLocation.setCellValueFactory(new PropertyValueFactory<>
                ("ParkingLocation"));
        ChargeAmount.setCellValueFactory(new PropertyValueFactory<>
                ("ChargeAmount"));
    }

}

