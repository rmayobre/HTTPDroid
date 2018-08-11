# HTTPDroid
A lightweight HTTP daemon that runs WebSocket protocol. This library is designed to be used on Android devices as a server to handle incoming commands on a LAN connection. This library will allow wireless communication for an Android device without the need of a third-party server handling sent data.

# Current Status: *pre-alpha*
This project is not yet finished and major functions of this project are not yet properly implemented.

# Road Map
## Alpha
At this point the project will have basic functions working, and focus on debugging for stability.
### Features to be included:
* HTTP requests
* WebSocket interface
* Logging system
* Easy to use callback interface to handle WebSocket responses.
## Beta
In this stage, the library will focus on adding security features.
### Features to be included:
* SQLCipher *Android edition* implementation
* Authentication System
* Encryption
  * Packet encryption
  * Database encryption
## Stable release
The library will have all listed functions fully supported, as well as any features added later in development.
### Features to be released after a stable build:
* FTP (File Transfer Protocol)
* SSH (Secure Shell)
* *Possibly more...*
