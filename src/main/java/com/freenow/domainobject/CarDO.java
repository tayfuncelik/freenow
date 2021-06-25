package com.freenow.domainobject;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "car")
//, uniqueConstraints = @UniqueConstraint(name = "uc_driverDO", columnNames = {"driverDO"})
public class CarDO
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime dateCreated = ZonedDateTime.now();

    private String licensePlate;
    private Integer seatCount;
    private Boolean convertible;
    private String rating;
    private String engineType;//electric,gas

    @OneToOne(mappedBy = "carDO")
    private DriverDO driverDO;


    public CarDO()
    {

    }


    public CarDO(Long id, String licensePlate, Integer seatCount, Boolean convertible, String rating, String engineType)
    {
        this.id = id;
        this.licensePlate = licensePlate;
        this.seatCount = seatCount;
        this.convertible = convertible;
        this.rating = rating;
        this.engineType = engineType;
    }


    public Long getId()
    {
        return id;
    }


    public String getLicensePlate()
    {
        return licensePlate;
    }


    public void setLicensePlate(String licensePlate)
    {
        this.licensePlate = licensePlate;
    }


    public Integer getSeatCount()
    {
        return seatCount;
    }


    public void setSeatCount(Integer seatCount)
    {
        this.seatCount = seatCount;
    }


    public Boolean getConvertible()
    {
        return convertible;
    }


    public void setConvertible(Boolean convertible)
    {
        this.convertible = convertible;
    }


    public String getRating()
    {
        return rating;
    }


    public void setRating(String rating)
    {
        this.rating = rating;
    }


    public String getEngineType()
    {
        return engineType;
    }


    public void setEngineType(String engineType)
    {
        this.engineType = engineType;
    }
}
