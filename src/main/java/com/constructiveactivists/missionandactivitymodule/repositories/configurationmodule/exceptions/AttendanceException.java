package com.constructiveactivists.missionandactivitymodule.repositories.configurationmodule.exceptions;

public class AttendanceException extends RuntimeException{
    public AttendanceException(String message) {
        super(message);
    }

    public AttendanceException(String message, Throwable cause) {
        super(message, cause);
    }
}