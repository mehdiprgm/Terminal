#include "headers/Terminal.h"

static termios originalTermios;

void enableRawMode() { tcsetattr(STDIN_FILENO, TCSAFLUSH, &originalTermios); }

void disableRawMode() {
  if (tcgetattr(STDIN_FILENO, &originalTermios) == -1) {
    cerr << "Failed to get terminal attributes\n";
  }

  std::atexit(disableRawMode);
  termios raw = originalTermios;

  raw.c_lflag &= ~(ECHO | ICANON);
  raw.c_iflag &= ~(IXON | ICRNL);
  raw.c_oflag &= ~(OPOST);

  raw.c_cc[VMIN] = 1;
  raw.c_cc[VTIME] = 0;

  if (tcsetattr(STDIN_FILENO, TCSAFLUSH, &raw) == -1) {
    cerr << "Failed to set raw mode\n";
  }
}

void gotoxy(int x, int y) {
  cout << "\033[" << y + 1 << ";" << x + 1 << "H";
  cout.flush();
}

void clearScreen() { system("clear"); }

void clearLine() { cout << "\33[2K\r" << std::flush; }

char getch(bool echo) {
  termios oldAttr{};
  char ch = 0;

  if (tcgetattr(STDIN_FILENO, &oldAttr) == -1) {
    throw std::runtime_error("error: failed to get terminal attributes");
  }

  termios newAttr = oldAttr;
  newAttr.c_lflag &= ~ICANON;

  if (echo) {
    newAttr.c_lflag |= ECHO;
  } else {
    newAttr.c_lflag &= ~ECHO;
  }

  newAttr.c_cc[VMIN] = 1;
  newAttr.c_cc[VTIME] = 0;

  if (tcsetattr(STDIN_FILENO, TCSANOW, &newAttr) == -1) {
    throw std::runtime_error("tcsetattr failed");
  }

  if (read(STDIN_FILENO, &ch, 1) == -1) {
    tcsetattr(STDIN_FILENO, TCSANOW, &oldAttr);
    throw std::runtime_error("read failed");
  }

  tcsetattr(STDIN_FILENO, TCSANOW, &oldAttr);
  return ch;
}

Coord getTerminalCoordinate() {
  Coord coord{0, 0};

  char buf[32], ch = 0;
  int i = 0, row = 0, col = 0;

  termios oldt{}, newt{};
  tcgetattr(STDIN_FILENO, &oldt);

  newt = oldt;
  newt.c_lflag &= ~(ICANON | ECHO);
  tcsetattr(STDIN_FILENO, TCSANOW, &newt);

  cout << "\033[6n" << std::flush;
  while (i < 31 && read(STDIN_FILENO, &ch, 1) == 1) {
    buf[i++] = ch;
    if (ch == 'R') break;
  }

  buf[i] = '\0';
  tcsetattr(STDIN_FILENO, TCSANOW, &oldt);

  if (sscanf(buf, "\033[%d;%dR", &row, &col) == 2) {
    coord.y = row - 1;
    coord.x = col - 1;
  }

  return coord;
}

WindowSize getWindowSize() {
  WindowSize windowSize{0, 0};
  struct winsize w;

  if (ioctl(STDOUT_FILENO, TIOCGWINSZ, &w) == -1) {
    windowSize.width = -1;
    windowSize.height = -1;
  } else {
    windowSize.width = w.ws_col;
    windowSize.height = w.ws_row;
  }

  return windowSize;
}