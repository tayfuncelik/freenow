package com.freenow.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.freenow.FreeNowServerApplicantTestApplication;
import com.freenow.controller.mapper.DriverMapper;
import com.freenow.datatransferobject.CarSelectDTO;
import com.freenow.datatransferobject.DriverDTO;
import com.freenow.domainobject.CarDO;
import com.freenow.domainobject.DriverDO;
import com.freenow.domainvalue.GeoCoordinate;
import com.freenow.domainvalue.OnlineStatus;
import com.freenow.exception.CarAlreadyInUseException;
import com.freenow.service.driver.DriverService;
import org.aspectj.lang.annotation.Before;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.util.NestedServletException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = FreeNowServerApplicantTestApplication.class)
@AutoConfigureMockMvc
public class DriverControllerTest extends Auth
{

    private final static String endPoint = "/v1/drivers";

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    protected DriverService driverService;


    @Before("driverService")
    public void setUp()
    {
        Mockito.reset(driverService);
    }


    @Test
    public void createDriver() throws Exception
    {
        // given
        DriverDO driverDO = new DriverDO("username", "password");
        // when
        when(driverService.create(Mockito.any())).thenReturn(driverDO);
        // then
        mockMvc.perform(MockMvcRequestBuilders
            .post(endPoint)
            .header("Authorization", getJWT())
            .content(asJsonString(DriverMapper.makeDriverDTO(driverDO)))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated());
    }


    @Test
    public void getDriver() throws Exception
    {
        // given
        DriverDO driverDO = new DriverDO("username", "password");
        driverDO.setOnlineStatus(OnlineStatus.ONLINE);
        driverDO.setId(1L);
        // when
        when(driverService.find(driverDO.getId())).thenReturn(driverDO);
        // then
        mockMvc.perform(MockMvcRequestBuilders
            .get(endPoint.concat("/{driverId}"), driverDO.getId())
            .header("Authorization", getJWT())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.username").value(driverDO.getUsername()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.password").value(driverDO.getPassword()));
    }


    @Test
    public void deleteDriver() throws Exception
    {
        mockMvc.perform(MockMvcRequestBuilders
            .delete(endPoint.concat("/{driverId}"), 1)
            .header("Authorization", getJWT()))
            .andExpect(status().isAccepted());
    }


    @Test
    public void updateLocation() throws Exception
    {
        GeoCoordinate geoCoordinate = new GeoCoordinate(2.3, 4.5);

        DriverDO driverDO = new DriverDO("username", "password");
        driverDO.setCoordinate(geoCoordinate);
        doNothing().when(driverService).updateLocation(1l, 22, 33);

        mockMvc.perform(MockMvcRequestBuilders
            .put(endPoint.concat("/{driverId}"), 1)
            .header("Authorization", getJWT())
            .param("longitude", String.valueOf(geoCoordinate.getLongitude()))
            .param("latitude", String.valueOf(geoCoordinate.getLatitude()))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }


    @Test
    public void findDrivers() throws Exception
    {
        // given
        List<DriverDO> driverDOList = new ArrayList<>();
        DriverDO driverDO = new DriverDO("username", "password");
        driverDO.setId(1l);
        driverDOList.add(driverDO);
        // when
        when(driverService.find(OnlineStatus.ONLINE)).thenReturn(driverDOList);
        // then
        mockMvc.perform(MockMvcRequestBuilders
            .get(endPoint)
            .header("Authorization", getJWT())
            .param("onlineStatus", OnlineStatus.ONLINE.name())
            .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(driverDO.getId()))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].username").value(driverDO.getUsername()));
    }


    @Test
    public void selectCar() throws Exception
    {
        // given
        CarSelectDTO carSelectDTO = new CarSelectDTO(1L, 5L);
        // when
        doNothing().when(driverService).selectCar(carSelectDTO);
        // then
        mockMvc.perform(MockMvcRequestBuilders
            .put("/v1/drivers/selectCar")
            .header("Authorization", getJWT())
            .content(asJsonString(carSelectDTO))
            .contentType(MediaType.APPLICATION_JSON)//RequestBody
            .accept(MediaType.APPLICATION_JSON)).andDo(print())
            .andExpect(status().isOk());
    }


    @Test
    public void selectCarOnlineDriverOnlyOnce() throws Exception
    {
        // given
        CarSelectDTO carSelectDTO = new CarSelectDTO(1L, 5L);
        // when
        doThrow(CarAlreadyInUseException.class).when(driverService).selectCar(Mockito.any());
        // then

        try
        {
            mockMvc.perform(MockMvcRequestBuilders
                .put(endPoint.concat("/selectCar"))
                .header("Authorization", getJWT())
                .content(asJsonString(carSelectDTO))
                .contentType(MediaType.APPLICATION_JSON)//RequestBody
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
        }
        catch (NestedServletException e)
        {
            Exception exception =
                assertThrows(CarAlreadyInUseException.class, () -> {
                    throw e.getCause();
                });
            Assertions.assertThat(exception).isExactlyInstanceOf(CarAlreadyInUseException.class);
        }
    }


    @Test
    public void deSelectCar() throws Exception
    {
        // given
        CarSelectDTO carSelectDTO = new CarSelectDTO(1L, 5L);
        // when
        doNothing().when(driverService).selectCar(carSelectDTO);
        // then
        mockMvc.perform(MockMvcRequestBuilders
            .put("/v1/drivers/deSelectCar")
            .header("Authorization", getJWT())
            .content(asJsonString(carSelectDTO))
            .contentType(MediaType.APPLICATION_JSON)//RequestBody
            .accept(MediaType.APPLICATION_JSON)).andDo(print())
            .andExpect(status().isOk());
    }


    @Test
    public void findDriverByParams() throws Exception
    {
        // given
        CarDO carDO = new CarDO(1l, "new Plate", 4, false, "rate count", "electric");
        DriverDO driverDO = new DriverDO("username", "password");
        driverDO.setId(1L);
        driverDO.setCarDO(carDO);
        List<DriverDO> driverDOList = new ArrayList<>();
        driverDOList.add(driverDO);
        DriverDTO driverDTO = DriverMapper.makeDriverDTO(driverDO);

        // when
        when(driverService.findDriverByParams(Mockito.any())).thenReturn(driverDOList);
        // then
        mockMvc.perform(MockMvcRequestBuilders
            .post(endPoint.concat("/findDriverByParams"))
            .header("Authorization", getJWT())
            .content(asJsonString(driverDTO))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(driverDO.getId()))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].username").value(driverDOList.get(0).getUsername()))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].onlineStatus").value(driverDOList.get(0).getOnlineStatus().name()))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].carDTO.licensePlate").value(driverDOList.get(0).getCarDO().getLicensePlate()))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].carDTO.convertible").value(driverDOList.get(0).getCarDO().getConvertible()))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].carDTO.rating").value(driverDOList.get(0).getCarDO().getRating()))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].carDTO.engineType").value(driverDOList.get(0).getCarDO().getEngineType()))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].carDTO.seatCount").value(driverDOList.get(0).getCarDO().getSeatCount()));
    }


    public static String asJsonString(final Object obj)
    {
        try
        {
            return new ObjectMapper().writeValueAsString(obj);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}
