// Runs an infinite loop that listens to the keyboard input. 
// When a key is pressed (any key), the program blackens the screen,
// i.e. writes "black" in every pixel. When no key is pressed, the
// program clears the screen, i.e. writes "white" in every pixel.

(INF)

@j
M=0

@i
M=0

@KBD
D=M

@WHITE
D; JEQ

(BLACK)

@8192
D=A

@i
D=D-M

@INF
D; JEQ

@SCREEN
D=A

@i
A=D+M
M=-1

@i
M=M+1

@BLACK
0; JMP

(WHITE)

@8192
D=A

@j
D=D-M

@INF
D; JEQ

@SCREEN
D=A

@j
A=D+M
M=0

@j
M=M+1

@WHITE
0; JMP
