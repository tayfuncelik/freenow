package com.freenow.service;

import com.freenow.FreeNowServerApplicantTestApplication;
import com.freenow.dataaccessobject.CarRepository;
import com.freenow.domainobject.CarDO;
import com.freenow.exception.ConstraintsViolationException;
import com.freenow.exception.EntityNotFoundException;
import com.freenow.service.car.CarService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = FreeNowServerApplicantTestApplication.class)
@AutoConfigureMockMvc
public class CarServiceTest
{
    @Autowired
    protected CarService carService;

    @MockBean
    protected CarRepository carRepository;


    @Test
    public void create() throws ConstraintsViolationException
    {
        CarDO car = carFactory();
        Mockito.when(carRepository.save(car)).thenReturn(car);
        CarDO returnedCar = carService.create(car);
        Assertions.assertEquals(returnedCar.getId(), car.getId());
        Assertions.assertEquals(returnedCar.getLicensePlate(), car.getLicensePlate());
        Assertions.assertEquals(returnedCar.getConvertible(), car.getConvertible());
        Assertions.assertEquals(returnedCar.getRating(), car.getRating());
        Assertions.assertEquals(returnedCar.getSeatCount(), car.getSeatCount());
        Assertions.assertEquals(returnedCar.getEngineType(), car.getEngineType());
    }


    @Test
    public void createDuplicateError()
    {
        CarDO car = carFactory();
        Mockito.when(carRepository.save(car)).thenThrow(DataIntegrityViolationException.class);
        Exception exception = assertThrows(ConstraintsViolationException.class, () -> {
            carService.create(car);
        });
        Assertions.assertEquals(exception.getClass(), ConstraintsViolationException.class);
    }


    @Test
    public void find() throws EntityNotFoundException
    {
        CarDO car = carFactory();
        Mockito.when(carRepository.findById(car.getId())).thenReturn(Optional.of(car));
        CarDO returnedCar = carService.find(car.getId());
        Assertions.assertEquals(returnedCar.getId(), car.getId());
        Assertions.assertEquals(returnedCar.getLicensePlate(), car.getLicensePlate());
        Assertions.assertEquals(returnedCar.getConvertible(), car.getConvertible());
        Assertions.assertEquals(returnedCar.getRating(), car.getRating());
        Assertions.assertEquals(returnedCar.getSeatCount(), car.getSeatCount());
        Assertions.assertEquals(returnedCar.getEngineType(), car.getEngineType());
    }


    @Test
    public void delete() throws EntityNotFoundException
    {
        CarDO car = carFactory();
        Mockito.when(carRepository.findById(car.getId())).thenReturn(Optional.of(car));
        Mockito.doNothing().when(carRepository).delete(car);

        carService.delete(car.getId());
        Mockito.when(carRepository.findById(car.getId())).thenReturn(null);
        Exception exception = assertThrows(NullPointerException.class, () -> {
            carService.find(car.getId());
        });
        Assertions.assertEquals(exception.getClass(), NullPointerException.class);
    }


    @Test
    public void deleteNonExistData()
    {
        CarDO car = carFactory();
        assertAll(() -> Optional.ofNullable(carRepository.findById(Mockito.any()))
            .orElseThrow(NullPointerException::new));
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            carService.delete(car.getId());
        });
        Assertions.assertEquals(exception.getClass(), EntityNotFoundException.class);
    }


    @Test
    public void update() throws EntityNotFoundException
    {
        CarDO car = carFactory();
        CarDO carNew = carFactory();
        carNew.setSeatCount(44);
        Mockito.when(carRepository.findById(car.getId())).thenReturn(Optional.of(carNew));
        Mockito.when(carRepository.save(car)).thenReturn(carNew);
        carService.update(car);
        CarDO returnedCar = carService.find(car.getId());
        Assertions.assertEquals(returnedCar.getSeatCount(), carNew.getSeatCount());
    }


    @Test
    public void findAll()
    {
        CarDO car = carFactory();
        List<CarDO> carDOList = new ArrayList<>();
        carDOList.add(car);

        Mockito.when(carRepository.findAll()).thenReturn(carDOList);
        List<CarDO> returnedCarDoList = carService.findAll();
        Assertions.assertEquals(returnedCarDoList.size(), 1);
        Assertions.assertEquals(returnedCarDoList.get(0), car);
    }


    private CarDO carFactory()
    {
        CarDO car = new CarDO();
        car.setId(1L);
        car.setConvertible(false);
        car.setEngineType("electric");
        car.setRating("12");
        car.setLicensePlate("abc");
        car.setSeatCount(4);
        return car;
    }
}
