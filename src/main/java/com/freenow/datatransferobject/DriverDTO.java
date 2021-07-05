package com.freenow.datatransferobject;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.freenow.domainvalue.GeoCoordinate;
import com.freenow.domainvalue.OnlineStatus;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DriverDTO
{
    private Long id;
//    @NotNull(message = "Username can not be null!")
    @ApiModelProperty(name = "username", dataType = "String", value = "myname")
    private String username;
//    @NotNull(message = "Password can not be null!")
    private String password;
    @ApiModelProperty(name = "coordinate", dataType = "GeoCoordinate")
    private GeoCoordinate coordinate;
    @ApiModelProperty(name = "onlineStatus", dataType = "OnlineStatus")
    private OnlineStatus onlineStatus;
    @ApiModelProperty(name = "deleted", dataType = "Boolean", value = "false")
    private Boolean deleted;

    private CarDTO carDTO;


    private DriverDTO()
    {
    }


    private DriverDTO(Long id, String username, String password, GeoCoordinate coordinate, OnlineStatus onlineStatus, Boolean deleted, CarDTO carDTO)
    {
        this.id = id;
        this.username = username;
        this.password = password;
        this.coordinate = coordinate;
        this.carDTO = carDTO;
        this.onlineStatus = onlineStatus;
        this.deleted = deleted;
    }


    public static DriverDTOBuilder newBuilder()
    {
        return new DriverDTOBuilder();
    }


    public Boolean getDeleted()
    {
        return deleted;
    }


    public OnlineStatus getOnlineStatus()
    {
        return onlineStatus;
    }


    @JsonProperty
    public Long getId()
    {
        return id;
    }


    public String getUsername()
    {
        return username;
    }


    public String getPassword()
    {
        return password;
    }


    public GeoCoordinate getCoordinate()
    {
        return coordinate;
    }


    public CarDTO getCarDTO()
    {
        return carDTO;
    }


    public static class DriverDTOBuilder
    {
        private Long id;
        private String username;
        private String password;
        private GeoCoordinate coordinate;
        private CarDTO carDTO;
        private OnlineStatus onlineStatus;
        private boolean deleted;


        public DriverDTOBuilder setDeleted(boolean deleted)
        {
            this.deleted = deleted;
            return this;
        }


        public DriverDTOBuilder setOnlineStatus(OnlineStatus onlineStatus)
        {
            this.onlineStatus = onlineStatus;
            return this;
        }


        public DriverDTOBuilder setCarDTO(CarDTO carDTO)
        {
            this.carDTO = carDTO;
            return this;
        }


        public DriverDTOBuilder setId(Long id)
        {
            this.id = id;
            return this;
        }


        public DriverDTOBuilder setUsername(String username)
        {
            this.username = username;
            return this;
        }


        public DriverDTOBuilder setPassword(String password)
        {
            this.password = password;
            return this;
        }


        public DriverDTOBuilder setCoordinate(GeoCoordinate coordinate)
        {
            this.coordinate = coordinate;
            return this;
        }


        public DriverDTO createDriverDTO()
        {
            return new DriverDTO(id, username, password, coordinate, onlineStatus, deleted, carDTO);
        }

    }
}
