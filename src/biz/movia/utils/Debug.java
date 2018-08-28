package biz.movia.utils;

public class Debug {
    /**
     * A string with the calling filename, method name and line number.
     */
    public static String prefix() {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        if (stackTraceElements.length >= 3) {
            StackTraceElement stackTraceElement = stackTraceElements[2];
            return stackTraceElement.getFileName()+":"+stackTraceElement.getMethodName()+":"+stackTraceElement.getLineNumber()+" ";
        }
        return "?:?:? ";
    }
}
