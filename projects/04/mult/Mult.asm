// Multiplies R0 and R1 and stores the result in R2.
// (R0, R1, R2 refer to RAM[0], RAM[1], and RAM[2], respectively.)

@R1
D=M

@n
M=D

@2
M=0

@i
M=0

(LOOP)

@n
D=M

@i
D=D-M

@END
D; JEQ

@R0
D=M

@R2
M=M+D

@i
M=M+1

@LOOP
0; JMP

(END)

@END
0;JEQ
