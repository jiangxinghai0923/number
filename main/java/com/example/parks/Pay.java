package com.example.parks;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

/**
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pay implements Comparable<Pay>{
    @ExcelProperty("序号")
    private String RecordID;//记录序号
    @ExcelProperty("车牌号码")
    private String LicensePlate;//车牌号码
    @ExcelProperty("入场时间")
    private String EntryTime;//入场时间
    @ExcelProperty("出场时间")
    private String ExitTime;//出场时间
    @ExcelProperty("车位位置")
    private String ParkingLocation;//车位号
    @ExcelProperty("缴费金额")
    private String ChargeAmount;//缴费金额

//    public Pay(String recordID, String licensePlate, String parkingLocation, String chargeAmount, String entryTime, String outtime) {
//    }


    @Override
    public int compareTo(Pay o) {
        return CharSequence.compare(this.RecordID,o.RecordID);
    }
}
