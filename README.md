
# Terminal

A high-performance Java library providing low-level, native control over terminal environments. This library enables sophisticated terminal interactions, robust input handling, and dynamic layout rendering by bridging JVM logic with native system calls via JNI.


## Features

*   **Raw Terminal Access:** Direct control over terminal modes (Raw vs. Canonical), cursor positioning, and screen/line clearing via native C implementation.
*   **Advanced Input Processing:** High-level input handling supporting password masking, escape sequence interpretation (Arrow keys, Home/End, Delete), and input length constraints.
*   **Dynamic Table Engine:** A structured data visualization component that automatically calculates column widths and truncates data to fit the current terminal window size.
*   **Event-Driven Resizing:** Reactive terminal resize monitoring using background polling threads and listener callbacks.
*   **Command Line Utilities:** Robust argument parsing supporting quoted strings, escape characters, and size validation for CLI-based applications.



## Demo

```java
package com.zen.workspace;

import com.zen.lib.terminal.driver.TerminalDriver;
import com.zen.lib.terminal.input.TerminalInput;
import com.zen.lib.terminal.table.Table;
import com.zen.lib.terminal.table.TableRow;

import java.util.List;

import static com.zen.lib.terminal.Terminal.*;

public class Application {

    static {
        System.loadLibrary("zenterminal");
    }

    public static void main(String[] args) {
        var columnMaxSize = TerminalDriver.getTerminalWindowSize().width();
        var table = new Table(columnMaxSize);

        table.getHeaders().addAll(List.of("Id", "Name", "Phone number"));
        table.getRows().addAll(List.of(
                new TableRow("101", "Mehdi", "0903 - 2782516"),
                new TableRow("102", "Ahmad", "0912 - 3556386"),
                new TableRow("103", "Reza", "0905 - 5721169")
        ));

        println(table.render());
        printCharacters('-', 50, true);

        var name = new TerminalInput(false, true).read("name: ", 50, true);
        printName(name);
    }

    private static void printName(String name) {
        if (name.isEmpty()) {
            printMessage(MessageType.ERROR, "Nothing to show\n");
        } else {
            printMessage(MessageType.INFORMATION, "Hello ", name, "\n");
        }
    }
}

```
## Optimizations

Re-write entire codebase with better and simpler algorithms and lots of things happening in direct access to the system and native-code.


## Deployment

You have 2 options to install this library.

```bash
  cp -v native/out /lib /lib64
  cp -v out/*.jars 'project_folder'
```

2. You can use Deployment script to install them.

```bash
  sudo python3 deployment.py 'project_folder'
```

## Tech Stack

**Core:** Java +21

**Native access:** C / C++

**Deployment**: Python


## 🚀 About Me
Like to build architecture systems and high-performance stuff

* Machine learning
* CLI tools
* Automation
* Low-level programming


## Authors

- [Mehdi Lavasani](https://github.com/mehdiprgm)

