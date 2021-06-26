package com.freenow.controller.payload;

public class UserVO
{
    private String id;
    private String userName;
    private String password;
    private String role;


    public UserVO(String id, String userName, String password, String role)
    {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.role = role;
    }


    public UserVO()
    {
    }


    public String getId()
    {
        return id;
    }


    public void setId(String id)
    {
        this.id = id;
    }


    public String getUserName()
    {
        return userName;
    }


    public void setUserName(String userName)
    {
        this.userName = userName;
    }


    public String getPassword()
    {
        return password;
    }


    public void setPassword(String password)
    {
        this.password = password;
    }


    public String getRole()
    {
        return role;
    }


    public void setRole(String role)
    {
        this.role = role;
    }
}
