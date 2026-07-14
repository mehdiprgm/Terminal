#include "headers/TerminalDriverInterface.h"

#include "headers/Terminal.h"

extern "C" {

JNIEXPORT void JNICALL
Java_com_zen_lib_terminal_driver_TerminalDriver_enableRawMode(JNIEnv* env,
                                                              jclass clazz) {
  enableRawMode();
}

JNIEXPORT void JNICALL
Java_com_zen_lib_terminal_driver_TerminalDriver_disableRawMode(JNIEnv* env,
                                                               jclass clazz) {
  disableRawMode();
}

JNIEXPORT void JNICALL
Java_com_zen_lib_terminal_driver_TerminalDriver_clearScreen(JNIEnv* env,
                                                            jclass clazz) {
  clearScreen();
}

JNIEXPORT void JNICALL
Java_com_zen_lib_terminal_driver_TerminalDriver_clearLine(JNIEnv* env,
                                                          jclass clazz) {
  clearLine();
}

JNIEXPORT void JNICALL Java_com_zen_lib_terminal_driver_TerminalDriver_gotoxy(
    JNIEnv* env, jclass clazz, jint x, jint y) {
  gotoxy(static_cast<int>(x), static_cast<int>(y));
}

JNIEXPORT jchar JNICALL Java_com_zen_lib_terminal_driver_TerminalDriver_getch(
    JNIEnv* env, jclass clazz, jboolean echo) {
  return static_cast<jchar>(getch(echo == JNI_TRUE));
}

JNIEXPORT jobject JNICALL
Java_com_zen_lib_terminal_driver_TerminalDriver_getTerminalCursorCoordinate(
    JNIEnv* env, jclass clazz) {
  Coord coord = getTerminalCoordinate();

  jclass coordClass =
      env->FindClass("com/zen/lib/terminal/driver/TerminalCursorCoordinate");

  jmethodID constructor = env->GetMethodID(coordClass, "<init>", "(II)V");

  return env->NewObject(coordClass, constructor, static_cast<jint>(coord.x),
                        static_cast<jint>(coord.y));
}

JNIEXPORT jobject JNICALL
Java_com_zen_lib_terminal_driver_TerminalDriver_getTerminalWindowSize(
    JNIEnv* env, jclass clazz) {
  WindowSize windowSize = getWindowSize();

  jclass sizeClass =
      env->FindClass("com/zen/lib/terminal/driver/TerminalWindowSize");

  jmethodID constructor = env->GetMethodID(sizeClass, "<init>", "(II)V");

  return env->NewObject(sizeClass, constructor,
                        static_cast<jint>(windowSize.width),
                        static_cast<jint>(windowSize.height));
}

}  // extern "C"