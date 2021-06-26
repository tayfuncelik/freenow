package com.freenow.controller;

import com.freenow.controller.mapper.DriverMapper;
import com.freenow.datatransferobject.CarSelectDTO;
import com.freenow.datatransferobject.DriverDTO;
import com.freenow.domainobject.DriverDO;
import com.freenow.domainvalue.OnlineStatus;
import com.freenow.exception.CarAlreadyInUseException;
import com.freenow.exception.ConstraintsViolationException;
import com.freenow.exception.EntityNotFoundException;
import com.freenow.service.driver.DriverService;
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

/**
 * All operations with a driver will be routed by this controller.
 * <p/>
 */
@Api(value = "Driver Controller")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("v1/drivers")
public class DriverController
{

    private final DriverService driverService;


    @Autowired
    public DriverController(final DriverService driverService)
    {
        this.driverService = driverService;
    }


    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "getDriver", authorizations = {@Authorization(value = "JWT_LOGIN")})
    @GetMapping("/{driverId}")
    public DriverDTO getDriver(@PathVariable long driverId) throws EntityNotFoundException
    {
        return DriverMapper.makeDriverDTO(driverService.find(driverId));
    }


    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "createDriver", authorizations = {@Authorization(value = "JWT_LOGIN")})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DriverDTO createDriver(@Valid @RequestBody DriverDTO driverDTO) throws ConstraintsViolationException
    {
        DriverDO driverDO = DriverMapper.makeDriverDO(driverDTO);
        return DriverMapper.makeDriverDTO(driverService.create(driverDO));
    }


    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "deleteDriver", authorizations = {@Authorization(value = "JWT_LOGIN")})
    @DeleteMapping("/{driverId}")
    public ResponseEntity<HttpStatus> deleteDriver(@PathVariable long driverId) throws EntityNotFoundException
    {
        driverService.delete(driverId);
        return new ResponseEntity<HttpStatus>(HttpStatus.ACCEPTED);
    }


    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "dupdateLocation", authorizations = {@Authorization(value = "JWT_LOGIN")})
    @PutMapping("/{driverId}")
    public void updateLocation(
        @PathVariable long driverId, @RequestParam double longitude, @RequestParam double latitude)
        throws EntityNotFoundException
    {
        driverService.updateLocation(driverId, longitude, latitude);
    }


    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "selectCar", authorizations = {@Authorization(value = "JWT_LOGIN")})
    @GetMapping
    public List<DriverDTO> findDrivers(@RequestParam OnlineStatus onlineStatus)
    {
        return DriverMapper.makeDriverDTOList(driverService.find(onlineStatus));
    }


    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "selectCar", authorizations = {@Authorization(value = "JWT_LOGIN")})
    @PutMapping("/selectCar")
    public void selectCar(@Valid @RequestBody CarSelectDTO carSelectDTO) throws EntityNotFoundException, CarAlreadyInUseException
    {
        driverService.selectCar(carSelectDTO);
    }


    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "deSelectCar", authorizations = {@Authorization(value = "JWT_LOGIN")})
    @PutMapping("/deSelectCar")
    public void deSelectCar(@Valid @RequestBody CarSelectDTO carSelectDTO) throws EntityNotFoundException
    {
        driverService.deSelectCar(carSelectDTO);
    }


    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "findDriverByParams", authorizations = {@Authorization(value = "JWT_LOGIN")})
    @PostMapping("/findDriverByParams")
    public List<DriverDTO> findDriverByParams(@Valid @RequestBody DriverDTO driverDTO)
    {
        return DriverMapper.makeDriverDTOList(driverService.findDriverByParams(driverDTO));
    }
}
