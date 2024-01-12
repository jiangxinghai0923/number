package com.example.parks;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import lombok.Data;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class parking implements Comparable<parking> {
    @Getter
    private  String spaceID;
    private String spaceType;
    private String spaceStatus;
    private String spaceLocation;
    private String licensePlate;

    public parking(String spaceID, String spaceType, String spaceLocation, String licensePlate) {
        this.spaceID = spaceID;
        this.spaceType = spaceType;
        this.spaceLocation = spaceLocation;
        this.licensePlate = licensePlate;
        this.spaceStatus = spaceStatus;
    }

@Override
public int compareTo(parking o) {
    return CharSequence.compare(this.spaceID,o.spaceID);
}
}
