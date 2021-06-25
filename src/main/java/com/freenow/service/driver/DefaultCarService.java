package com.freenow.service.driver;

import com.freenow.dataaccessobject.CarRepository;
import com.freenow.dataaccessobject.DriverRepository;
import com.freenow.domainobject.CarDO;
import com.freenow.domainobject.DriverDO;
import com.freenow.domainvalue.GeoCoordinate;
import com.freenow.domainvalue.OnlineStatus;
import com.freenow.exception.ConstraintsViolationException;
import com.freenow.exception.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service to encapsulate the link between DAO and controller and to have business logic for some driver specific things.
 * <p/>
 */
@Service
public class DefaultCarService implements CarService
{

    private static final Logger LOG = LoggerFactory.getLogger(DefaultCarService.class);

    private final CarRepository carRepository;


    public DefaultCarService(final CarRepository carRepository)
    {
        this.carRepository = carRepository;
    }


    @Override
    public CarDO find(Long driverId) throws EntityNotFoundException
    {
        return findCarChecked(driverId);
    }


    @Override
    public CarDO create(CarDO carDO) throws ConstraintsViolationException
    {
        CarDO car;
        try
        {
            car = carRepository.save(carDO);
        }
        catch (DataIntegrityViolationException e)
        {
            LOG.warn("ConstraintsViolationException while creating a car: {}", carDO, e);
            throw new ConstraintsViolationException(e.getMessage());
        }
        return car;
    }


    @Override
    @Transactional
    public void delete(Long carId) throws EntityNotFoundException
    {
        CarDO carDO = findCarChecked(carId);
        carRepository.delete(carDO);
    }


    @Override
    @Transactional
    public void update(CarDO carDO) throws EntityNotFoundException
    {
        findCarChecked(carDO.getId());
        carRepository.save(carDO);
    }


    @Override
    public List<CarDO> findAll()
    {
        return carRepository.findAll();
    }


    private CarDO findCarChecked(Long carId) throws EntityNotFoundException
    {
        return carRepository.findById(carId)
            .orElseThrow(() -> new EntityNotFoundException("Could not find entity with id: " + carId));
    }

}
