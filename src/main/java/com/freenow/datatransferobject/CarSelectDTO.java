package com.freenow.datatransferobject;

import com.freenow.domainvalue.GeoCoordinate;
import com.freenow.domainvalue.OnlineStatus;
import io.swagger.annotations.ApiModelProperty;

public class CarSelectDTO
{
    @ApiModelProperty(name = "driverId", dataType = "long", example = "4")
    private long driverId;
    @ApiModelProperty(name = "carId", dataType = "long", example = "1")
    private long carId;


    public CarSelectDTO()
    {
    }


    public CarSelectDTO(long driverId, long carId)
    {
        this.driverId = driverId;
        this.carId = carId;
    }


    public long getDriverId()
    {
        return driverId;
    }


    public void setDriverId(long driverId)
    {
        this.driverId = driverId;
    }


    public long getCarId()
    {
        return carId;
    }


    public void setCarId(long carId)
    {
        this.carId = carId;
    }


//    public static class CarDTOBuilder
//    {
//        private long driverId;
//        private long carId;
//
//
//        public CarSelectDTO createCarDTO()
//        {
//            return new CarSelectDTO(carId, driverId);
//        }
//
//
//        public CarSelectDTO.CarDTOBuilder setCarId(Long carId)
//        {
//            this.carId = carId;
//            return this;
//        }
//
//
//        public CarSelectDTO.CarDTOBuilder setDriverId(Long driverId)
//        {
//            this.driverId = driverId;
//            return this;
//        }
//
//    }
}
