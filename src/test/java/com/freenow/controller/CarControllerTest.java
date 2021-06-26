package com.freenow.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.freenow.FreeNowServerApplicantTestApplication;
import com.freenow.controller.mapper.CarMapper;
import com.freenow.controller.payload.AuthRequest;
import com.freenow.domainobject.CarDO;
import com.freenow.service.car.CarService;
import org.aspectj.lang.annotation.Before;
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

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = FreeNowServerApplicantTestApplication.class)
@AutoConfigureMockMvc
public class CarControllerTest extends Auth
{
    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    protected CarService carService;


    @Before("carService")
    public void setUp()
    {
        Mockito.reset(carService);
    }


    @Test
    public void register() throws Exception
    {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setPassword("pass");
        this.mockMvc.perform(MockMvcRequestBuilders
            .post("/auth/register")
            .header("Authorization", getJWT())
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(authRequest))
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }


    @Test
    public void nonRegisteredRequest() throws Exception
    {
        // given
        CarDO carDO = new CarDO(1l, null, null, false, null, null);
        // when
        when(carService.create(Mockito.any())).thenReturn(carDO);
        // then

        mockMvc.perform(MockMvcRequestBuilders
            .post("/v1/cars")
            .content(asJsonString(CarMapper.makeCarDTO(carDO)))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized());
    }


    @Test
    public void testCreateCar() throws Exception
    {
        // given
        CarDO carDO = new CarDO(1l, null, null, false, null, null);
        // when
        when(carService.create(Mockito.any())).thenReturn(carDO);
        // then
        mockMvc.perform(MockMvcRequestBuilders
            .post("/v1/cars")
            .header("Authorization", getJWT())
            .content(asJsonString(CarMapper.makeCarDTO(carDO)))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated());
    }


    @Test
    public void testGetCar() throws Exception
    {
        // given
        CarDO carDO = new CarDO(1l, "plateNo", null, false, null, null);
        // when
        when(carService.find(carDO.getId())).thenReturn(carDO);
        // then
        mockMvc.perform(MockMvcRequestBuilders
            .get("/v1/cars/{driverId}", carDO.getId())
            .header("Authorization", getJWT())
            .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(carDO.getId()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.licensePlate").value(carDO.getLicensePlate()));
    }


    @Test
    public void testDelete() throws Exception
    {
        mockMvc.perform(MockMvcRequestBuilders
            .delete("/v1/cars/{driverId}", 1)
            .header("Authorization", getJWT()))
            .andExpect(status().isAccepted());
    }


    @Test
    public void testGetAllCars() throws Exception
    {
        List<CarDO> carDOList = new ArrayList<>();
        CarDO carDO = new CarDO(1l, "new Plate", null, false, null, null);
        carDOList.add(carDO);
        when(carService.findAll()).thenReturn(carDOList);

        mockMvc.perform(MockMvcRequestBuilders
            .get("/v1/cars")
            .header("Authorization", getJWT())
            .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(carDO.getId()))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].licensePlate").value(carDO.getLicensePlate()));
    }


    @Test
    public void testPutCar() throws Exception
    {
        List<CarDO> carDOList = new ArrayList<>();
        CarDO carDO = new CarDO(1l, "new Plate", null, false, null, null);
        carDOList.add(carDO);
        doNothing().when(carService).update(carDO);

        mockMvc.perform(MockMvcRequestBuilders
            .put("/v1/cars/{carId}", carDO.getId())
            .header("Authorization", getJWT())
            .content(asJsonString(CarMapper.makeCarDTO(carDO)))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(carDO.getId()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.licensePlate").value(carDO.getLicensePlate()));
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
