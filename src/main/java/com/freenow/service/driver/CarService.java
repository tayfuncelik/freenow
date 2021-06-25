package com.freenow.service.driver;

import com.freenow.domainobject.CarDO;
import com.freenow.exception.ConstraintsViolationException;
import com.freenow.exception.EntityNotFoundException;

import java.util.List;

public interface CarService
{
    CarDO find(Long driverId) throws EntityNotFoundException;

    CarDO create(CarDO carDO) throws ConstraintsViolationException;

    void delete(Long carId) throws EntityNotFoundException;

    void update(CarDO carDO) throws EntityNotFoundException;

    List<CarDO> findAll();

}
