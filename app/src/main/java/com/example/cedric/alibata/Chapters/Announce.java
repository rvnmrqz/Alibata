package com.example.cedric.alibata.Chapters;

/**
 * Created by Cedric on 21 Jan 2018.
 */

public class Announce {
    private int AnId;
    private String Teacher;
    private String Title;
    private String Date;
    private String Message;


    public void setpAnId(int pAnId){this.AnId=pAnId;}
    public void setpProf(String pProf){this.Teacher=pProf;}
    public void setpSubject(String pTitle){this.Title=pTitle;}
    public void setpMessage(String pMessage){this.Message=pMessage;}
    public void setpDate(String pDate){this.Date=pDate;}

    public int getpAnId(){return AnId;}
    public String getpProf(){return Teacher;}
    public String getpSubject(){return Title;}
    public String getpMessage(){return Message;}
    public String getpDate(){return Date;}



}