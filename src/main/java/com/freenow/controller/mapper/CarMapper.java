package com.freenow.controller.mapper;

import com.freenow.datatransferobject.CarDTO;
import com.freenow.domainobject.CarDO;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CarMapper
{
    public static CarDO makeCarDO(CarDTO dto)
    {
        return new CarDO(dto.getId(), dto.getLicensePlate(), dto.getSeatCount(), dto.getConvertible(), dto.getRating(), dto.getEngineType());
    }


    public static CarDTO makeCarDTO(CarDO carDO)
    {
        CarDTO.CarDTOBuilder carDTOBuilder = CarDTO.newBuilder()
            .setId(carDO.getId())
            .setConvertible(carDO.getConvertible())
            .setSeatCount(carDO.getSeatCount())
            .setRating(carDO.getRating())
            .setLicensePlate(carDO.getLicensePlate())
            .setEngineType(carDO.getEngineType());
        return carDTOBuilder.createCarDTO();
    }


    public static List<CarDTO> makeCarDTOList(Collection<CarDO> carList)
    {
        return carList.stream()
            .map(CarMapper::makeCarDTO)
            .collect(Collectors.toList());
    }
}
