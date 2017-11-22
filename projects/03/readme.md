# Project 03

Project 03 implementation as described [here](//nand2tetris.org/03.php).

- Bit
```c++
IN in, load;
OUT out;
```

```c++
if load[at t] == 1
    out[at t + 1] = in[at t]
else
    out[at t + 1] = in[at t]
```

- Register

```c++
IN in[16], load;
OUT out[16];
```

```c++
If load[at t] == 1
    out[at t + 1] = in[at t]
else
    out[at t + 1] = out[at t]
```

- Generic RAM

```c++
for ever
    out = memory[address]
    
    if load == 1
        memory[address] = in
```

- 8-register RAM

Memory of 8 registers, each 16 bit-wide. 

```c++
IN in[16], load, address[3];
OUT out[16];
```

- 64-register RAM

Memory of 64 registers, each 16 bit-wide. 

```c++
IN in[16], load, address[6];
OUT out[16];
```

- 512-register RAM

Memory of 512 registers, each 16 bit-wide. 

```c++
IN in[16], load, address[9];
OUT out[16];
```

- 4K RAM

Memory of 4096 registers, each 16 bit-wide. 

```c++
IN in[16], load, address[12];
OUT out[16];
```

- 16K RAM

Memory of 16384 registers, each 16 bit-wide. 

```c++
IN in[16], load, address[14];
OUT out[16];
```

- Program counter

```c++
IN in[16], load, inc, reset;
OUT out[16];

```

```c++
if reset[at t] == 1
    out[at t+1] = 0
else if load[at t] == 1
    out[at t + 1] = in[at t]
else if inc[t] == 1
    out[at t + 1] = out[at t] + 1 // (integer addition)
else
    out[at t + 1] = out[at t]

```
