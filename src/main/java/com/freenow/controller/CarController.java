package com.freenow.controller;

import com.freenow.controller.mapper.CarMapper;
import com.freenow.datatransferobject.CarDTO;
import com.freenow.domainobject.CarDO;
import com.freenow.exception.ConstraintsViolationException;
import com.freenow.exception.EntityNotFoundException;
import com.freenow.service.car.CarService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(value = "Car Controller")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("v1/cars")
public class CarController
{
    private final CarService carService;


    @Autowired
    public CarController(CarService carService)
    {
        this.carService = carService;
    }


    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "get Car method", authorizations = {@Authorization(value = "JWT_LOGIN")})
    @GetMapping("/{carId}")
    public CarDTO getCar(@PathVariable long carId) throws EntityNotFoundException
    {
        return CarMapper.makeCarDTO(carService.find(carId));
    }


    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "create Car method", authorizations = {@Authorization(value = "JWT_LOGIN")})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CarDTO createCar(@Valid @RequestBody CarDTO carDTO) throws ConstraintsViolationException
    {
        CarDO carDO = CarMapper.makeCarDO(carDTO);
        return CarMapper.makeCarDTO(carService.create(carDO));
    }


    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "delete Car method", authorizations = {@Authorization(value = "JWT_LOGIN")})
    @DeleteMapping("/{carId}")
    public ResponseEntity<HttpStatus> deleteCar(@PathVariable long carId) throws EntityNotFoundException
    {
        carService.delete(carId);
        return new ResponseEntity<HttpStatus>(HttpStatus.ACCEPTED);
    }


    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "find Car method", authorizations = {@Authorization(value = "JWT_LOGIN")})
    @GetMapping
    public List<CarDTO> findCars()
    {
        return CarMapper.makeCarDTOList(carService.findAll());
    }


    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "update Car method", authorizations = {@Authorization(value = "JWT_LOGIN")})
    @PutMapping("/{carId}")
    @ResponseStatus(HttpStatus.OK)
    public CarDTO updateCar(@PathVariable("carId") long carId, @Valid @RequestBody CarDTO carDTO) throws EntityNotFoundException
    {
        CarDO carDO = CarMapper.makeCarDO(carDTO);
        carService.update(carDO);
        return carDTO;
    }

}
