package com.freenow.datatransferobject;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CarDTO
{
    private Long id;
    @ApiModelProperty(name = "licensePlate", dataType = "String", value = "22 BC 12")
    private String licensePlate;
    @ApiModelProperty(name = "seatCount", dataType = "Integer", value = "4")
    private Integer seatCount;
    @ApiModelProperty(name = "convertible", dataType = "Boolean", value = "false")
    private Boolean convertible;
    @ApiModelProperty(name = "rating", dataType = "String", value = "5")
    private String rating;
    @ApiModelProperty(name = "engineType", dataType = "String", value = "electric")
    private String engineType;//electric,gas


    private CarDTO()
    {

    }


    private CarDTO(Long id, String licensePlate, Integer seatCount, Boolean convertible, String rating, String engineType)
    {
        this.id = id;
        this.licensePlate = licensePlate;
        this.seatCount = seatCount;
        this.convertible = convertible;
        this.rating = rating;
        this.engineType = engineType;
    }


    public static CarDTOBuilder newBuilder()
    {
        return new CarDTOBuilder();
    }


    public static class CarDTOBuilder
    {
        private Long id;
        private String licensePlate;
        private Integer seatCount;
        private Boolean convertible;
        private String rating;
        private String engineType;//electric,gas


        public CarDTO createCarDTO()
        {
            return new CarDTO(id, licensePlate, seatCount, convertible, rating, engineType);
        }


        public CarDTOBuilder setId(Long id)
        {
            this.id = id;
            return this;
        }


        public CarDTOBuilder setLicensePlate(String licensePlate)
        {
            this.licensePlate = licensePlate;
            return this;
        }


        public CarDTOBuilder setSeatCount(Integer seatCount)
        {
            this.seatCount = seatCount;
            return this;
        }


        public CarDTOBuilder setConvertible(Boolean convertible)
        {
            this.convertible = convertible;
            return this;
        }


        public CarDTOBuilder setRating(String rating)
        {
            this.rating = rating;
            return this;
        }


        public CarDTOBuilder setEngineType(String engineType)
        {
            this.engineType = engineType;
            return this;
        }
    }


    @JsonProperty
    public Long getId()
    {
        return id;
    }


    public String getLicensePlate()
    {
        return licensePlate;
    }


    public Integer getSeatCount()
    {
        return seatCount;
    }


    public Boolean getConvertible()
    {
        return convertible;
    }


    public String getRating()
    {
        return rating;
    }


    public String getEngineType()
    {
        return engineType;
    }
}
