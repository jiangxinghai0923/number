package com.example.parks;

//import com.alibaba.excel.annotation.annotationExcelProperty;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message implements Comparable<Message> {
    @ExcelProperty("ID")
    private String VehicleID;

    @ExcelProperty("Plate")
    private String LicensePlate;
    @ExcelProperty("Type")
    private String VehicleType;
    @ExcelProperty("Color")
    private String VehicleColor;
    @ExcelProperty("Owner")
    private String OwnerName;
    @ExcelProperty("Contact")
    private String OwnerContact;

    @Override
    public int compareTo(Message o) {
        return CharSequence.compare(this.VehicleID,o.VehicleID);
    }

}