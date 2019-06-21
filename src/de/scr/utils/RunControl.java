package de.scr.utils;

public enum RunControl {
    //Start options
    STOP,
    LINE,
    LINE_CONVOY,
    LINE_EVADE,
    GUI_MODE,

    //Intern flags
    EVADING,
    LINEDETECT,
    LINEDETECT_EVADING
}
