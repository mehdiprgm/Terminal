#ifndef TERMINAL_H
#define TERMINAL_H

#include <sys/ioctl.h>
#include <termios.h>
#include <unistd.h>

#include <cstdlib>
#include <iostream>
#include <sstream>
#include <string>

using std::cin, std::cout, std::cerr, std::endl;

struct Coord {
  int x;
  int y;
};

struct WindowSize {
  int width;
  int height;
};

void enableRawMode();
void disableRawMode();

void gotoxy(int x, int y);

void clearScreen();
void clearLine();

char getch(bool echo = false);

Coord getTerminalCoordinate();
WindowSize getWindowSize();
#endif