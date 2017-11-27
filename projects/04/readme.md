# Project 04

Project 06 implementation as described [here](//nand2tetris.org/05.php).

Write `Mult.asm` and `Fill.asm` to make yourself familiar with the Hack assembly language.

`Mult.asm`: Computes the value `R0×R1` and stores the result in `R2`.
`Fill.asm`: The program runs an infinite loop that listens to the keyboard input. When a key is pressed (any key), the program blackens the screen, i.e., it writes "black" in every pixel; the screen should remain fully black as long as the key is pressed. 

When no key is pressed, the program clears the screen, i.e., it writes "white" in every pixel; the screen should remain fully clear as long as no key is pressed.

The rest of this document outlines the Hack assembly language specification.

## Assembly Language (.asm) Files

An assembly language file is composed of text lines, each representing either an instruction or a symbol declaration.

- Instruction: An A-instruction or a C-instruction.

- `(SYMBOL)`: This pseudo-command binds the `SYMBOL` to the memory location into which the next command in the program will be stored. It is called "pseudocommand" since it generates no machine code.

### Conventions

- Constants and Symbols: Constants must be non-negative and are written in decimal notation. A user-defined symbol can be any sequence of letters, digits, underscore (`_`), dot (`.`), dollar sign (`$`), and colon (`:`) that does not begin with a digit.

- Comments: Text beginning with two slashes (`//`) and ending at the end of the line is considered a comment and is ignored.

- White Space: Space characters are ignored. Empty lines are ignored.

- Case Conventions: All the assembly mnemonics must be written in uppercase. The rest (user-defined labels and variable names) is case sensitive. The convention is to use uppercase for labels and lowercase for variable names.

## Instructions

The hack machine language consists of two instruction types called addressing instruction (A-instruction) and compute instruction (C-instruction). The instruction format is as follows.

- A-instruction: `@value`, where `value` is either a non-negative decimal number or a symbol referring to such a number.

- C-instruction: `dest=comp;jump`. If `dest` is omitted, the `=` must also be omitted. If `jump` is omitted, the `;` must also be omitted.

## Symbols

Hack assembly commands can refer to memory locations (addresses) using either constants or symbols. Symbols in assembly programs arise from three sources.

### Predefined symbols

Any Hack assembly program is allowed to use the following predefined symbols.

Label    | RAM Address
-------- | -----------
`SP`     |           0
`LCL`    |           1
`ARG`    |           2
`THIS`   |           3
`THAT`   |           4
`R0—R15` |        0—15
`SCREEN` |       16384
`KBD`    |       24576

### Label symbols

The pseudo-command `(XXX)` defines the symbol `XXX` to refer to the instruction memory location holding the next command in the program. A label can be defined only once and can be used anywhere in the assembly program, even before the line in which it is defined.

### Variable symbols

Any symbol `xxx` appearing in an assembly program that is not predefined and is not defined elsewhere using the `(xxx)` command is treated as a variable. Variables are mapped to consecutive memory locations as they are first encountered, starting at RAM address 16.
