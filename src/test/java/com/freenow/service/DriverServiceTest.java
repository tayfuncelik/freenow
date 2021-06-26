package com.freenow.service;

import com.freenow.FreeNowServerApplicantTestApplication;
import com.freenow.controller.mapper.DriverMapper;
import com.freenow.dataaccessobject.CarRepository;
import com.freenow.dataaccessobject.DriverRepository;
import com.freenow.datatransferobject.CarSelectDTO;
import com.freenow.domainobject.CarDO;
import com.freenow.domainobject.DriverDO;
import com.freenow.domainvalue.GeoCoordinate;
import com.freenow.domainvalue.OnlineStatus;
import com.freenow.exception.CarAlreadyInUseException;
import com.freenow.exception.ConstraintsViolationException;
import com.freenow.exception.EntityNotFoundException;
import com.freenow.service.car.CarService;
import com.freenow.service.driver.DriverService;
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
public class DriverServiceTest
{

    @MockBean
    private DriverRepository driverRepository;

    @MockBean
    private CarRepository carRepository;

    @Autowired
    private DriverService driverService;

    @Autowired
    private CarService carService;


    @Test
    public void create() throws ConstraintsViolationException
    {
        DriverDO driver = driverFactory();
        Mockito.when(driverRepository.save(driver)).thenReturn(driver);
        DriverDO returnedDriver = driverService.create(driver);
        Assertions.assertEquals(returnedDriver.getId(), driver.getId());
    }


    @Test
    public void createException()
    {
        DriverDO driver = driverFactory();
        Mockito.when(driverRepository.save(driver)).thenThrow(DataIntegrityViolationException.class);
        Exception exception = assertThrows(ConstraintsViolationException.class, () -> {
            driverService.create(driver);
        });
        Assertions.assertEquals(exception.getClass(), ConstraintsViolationException.class);

    }


    @Test
    public void delete() throws EntityNotFoundException
    {
        DriverDO driver = driverFactory();
        Mockito.when(driverRepository.findById(driver.getId())).thenReturn(Optional.of(driver));
        Mockito.doNothing().when(driverRepository).delete(driver);
        driverService.delete(driver.getId());
        DriverDO returnedDriver = driverService.find(driver.getId());
        Assertions.assertEquals(returnedDriver.getDeleted(), true);

    }


    @Test
    public void deleteNotExistDataError()
    {
        DriverDO driver = driverFactory();
        assertAll(() -> Optional.ofNullable(driverRepository.findById(Mockito.any()))
            .orElseThrow(NullPointerException::new));
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            driverService.delete(driver.getId());
        });
        Assertions.assertEquals(exception.getClass(), EntityNotFoundException.class);
        Assertions.assertEquals(exception.getMessage(), "Could not find entity with id: " + driver.getId());
    }


    @Test
    public void updateLocation() throws EntityNotFoundException
    {
        DriverDO driver = driverFactory();
        Mockito.when(driverRepository.findById(driver.getId())).thenReturn(Optional.of(driver));
        driverService.updateLocation(driver.getId(), 55d, 66d);
        DriverDO returnedDO = driverService.find(driver.getId());
        Assertions.assertEquals(returnedDO.getCoordinate().getLatitude(), 66);
        Assertions.assertEquals(returnedDO.getCoordinate().getLongitude(), 55);
    }


    @Test
    public void find() throws EntityNotFoundException
    {
        DriverDO driver = driverFactory();
        List<DriverDO> driverDOList = new ArrayList<>();
        driverDOList.add(driver);

        Mockito.when(driverRepository.findById(driver.getId())).thenReturn(Optional.of(driver));
        DriverDO returnedDO = driverService.find(driver.getId());
        Assertions.assertEquals(returnedDO.getId(), driver.getId());
        Assertions.assertEquals(returnedDO.getOnlineStatus(), OnlineStatus.ONLINE);
    }


    @Test
    public void findAll()
    {
        DriverDO driver = driverFactory();
        List<DriverDO> driverDOList = new ArrayList<>();
        driverDOList.add(driver);
        Mockito.when(driverRepository.findByOnlineStatus(OnlineStatus.ONLINE)).thenReturn(driverDOList);

        Mockito.when(driverRepository.findById(driver.getId())).thenReturn(Optional.of(driver));
        List<DriverDO> returnedDOList = driverService.find(OnlineStatus.ONLINE);
        Assertions.assertEquals(returnedDOList.get(0).getOnlineStatus(), OnlineStatus.ONLINE);
    }


    @Test
    public void selectCar() throws EntityNotFoundException, CarAlreadyInUseException
    {
        DriverDO driver = driverFactory();
        CarDO car = new CarDO();
        car.setId(4l);
        driver.setCarDO(car);

        Mockito.when(driverRepository.findById(driver.getId())).thenReturn(Optional.of(driver));
        Mockito.when(carRepository.findById(driver.getCarDO().getId())).thenReturn(Optional.of(car));

        CarSelectDTO carSelectDTO = new CarSelectDTO();
        carSelectDTO.setDriverId(driver.getId());
        carSelectDTO.setCarId(car.getId());
        driverService.selectCar(carSelectDTO);

        DriverDO returnedDriver = driverService.find(driver.getId());
        Assertions.assertEquals(returnedDriver.getCarDO(), car);
    }


    @Test
    public void selectCarAlreadyInUse()
    {
        DriverDO driver = driverFactory();
        CarDO car = new CarDO();
        car.setId(4l);
        driver.setCarDO(car);
        Mockito.when(driverRepository.findByOnlineStatusIsAndCarDOId(OnlineStatus.ONLINE, driver.getCarDO().getId())).thenReturn(driver);

        Mockito.when(driverRepository.findById(driver.getId())).thenReturn(Optional.of(driver));
        Mockito.when(carRepository.findById(driver.getCarDO().getId())).thenReturn(Optional.of(car));

        CarSelectDTO carSelectDTO = new CarSelectDTO();
        carSelectDTO.setDriverId(driver.getId());
        carSelectDTO.setCarId(car.getId());
        Exception exception = assertThrows(CarAlreadyInUseException.class, () -> {
            driverService.selectCar(carSelectDTO);
        });
        Assertions.assertEquals(exception.getClass(), CarAlreadyInUseException.class);

    }


    @Test
    public void deSelectCar() throws EntityNotFoundException, CarAlreadyInUseException
    {
        DriverDO driver = driverFactory();
        CarDO car = new CarDO();
        car.setId(4l);
        driver.setCarDO(car);

        Mockito.when(driverRepository.findById(driver.getId())).thenReturn(Optional.of(driver));

        CarSelectDTO carSelectDTO = new CarSelectDTO();
        carSelectDTO.setDriverId(driver.getId());
        driverService.deSelectCar(carSelectDTO);

        DriverDO returnedDriver = driverService.find(driver.getId());
        Assertions.assertEquals(returnedDriver.getCarDO(), null);
    }


    @Test
    public void findDriverByParams()
    {
        DriverDO driver = driverFactory();
        CarDO car = new CarDO();
        car.setId(4l);
        car.setEngineType("electric");
        driver.setCarDO(car);
        List<DriverDO> requestDriverList = new ArrayList<>();
        requestDriverList.add(driver);

        Mockito.when(driverRepository.findByParams(Mockito.any())).thenReturn(requestDriverList);
        List<DriverDO> returnedDriverList = driverService.findDriverByParams(DriverMapper.makeDriverDTO(driver));
        Assertions.assertEquals(returnedDriverList.get(0).getId(), driver.getId());
        Assertions.assertEquals(returnedDriverList.get(0).getOnlineStatus(), OnlineStatus.ONLINE);
        Assertions.assertEquals(returnedDriverList.get(0).getCarDO().getId(), car.getId());
        Assertions.assertEquals(returnedDriverList.get(0).getCarDO().getEngineType(), car.getEngineType());
        //It might be extend with other field ...
    }


    private DriverDO driverFactory()
    {
        DriverDO driverDO = new DriverDO("username", "password");
        driverDO.setId(1L);
        GeoCoordinate coordinate = new GeoCoordinate(11l, 22l);
        driverDO.setCoordinate(coordinate);
        driverDO.setOnlineStatus(OnlineStatus.ONLINE);
        driverDO.setDeleted(false);
        //TODO
        driverDO.setCarDO(null);
        return driverDO;
    }
}
