package de.scr.utils;

/**
 * Control the states between all controller-threads
 */
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
