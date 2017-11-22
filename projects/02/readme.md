# Project 02

Project 02 implementation as described [here](//nand2tetris.org/02.php).

- Half Adder

Compute the sum of two bits

```c++
IN a, b;    // 1-bit inputs
OUT sum,    // Right bit of a + b
    carry;  // Left bit of a + b
```

```

a, b | sum | carry
------------------
0, 0 |   0 |     0
0, 1 |   1 |     0
1, 0 |   1 |     0
1, 1 |   0 |     1
```

- Full Adder

Compute the sum of three bits

```c++
IN a, b, c;  // 1-bit inputs
OUT sum,     // Right bit of a + b + c
    carry;   // Left bit of a + b + c
```


```
a, b, c | sum | carry
---------------------
0, 0, 0 |   0 |     0
0, 0, 1 |   1 |     0
0, 1, 0 |   1 |     0
0, 1, 1 |   0 |     1
1, 0, 0 |   1 |     0
1, 0, 1 |   0 |     1
1, 1, 0 |   0 |     1
1, 1, 1 |   1 |     1
```

- 16-bit Add

```c++
IN a[16], b[16];
OUT out[16];
```

```c++
// add two 16-bit values
// ignore most significant carry bit
out = a + b
```

- 16-bit Incrementer

```c++
IN in[16];
OUT out[16];
```

```c++
out = in + 1 // (arithmetic addition)
```

- ALU

```c++
IN
    x[16], y[16],  // 16-bit inputs
    zx,            // zero the x input?
    nx,            // negate the x input?
    zy,            // zero the y input?
    ny,            // negate the y input?
    f,             // compute out = x + y (if 1) or x & y (if 0)
    no;            // negate the out output?
OUT
    out[16],       // 16-bit output
    zr,            // 1 if (out == 0), 0 otherwise
    ng;            // 1 if (out < 0),  0 otherwise
```

```
calc | zx | nx | zy | ny | f | no |
-----------------------------------
0    |  1 |  0 |  1 |  0 | 1 |  0 |
1    |  1 |  1 |  1 |  1 | 1 |  1 |
-1   |  1 |  1 |  1 |  0 | 1 |  0 |
x    |  0 |  0 |  1 |  1 | 0 |  0 |
y    |  1 |  1 |  0 |  0 | 0 |  0 |
!x   |  0 |  0 |  1 |  1 | 0 |  1 |
!y   |  1 |  1 |  0 |  0 | 0 |  1 |
-x   |  0 |  0 |  1 |  1 | 1 |  1 |
-y   |  1 |  1 |  0 |  0 | 1 |  1 |
x+1  |  0 |  1 |  1 |  1 | 1 |  1 |
y+1  |  1 |  1 |  0 |  1 | 1 |  1 |
x-1  |  0 |  0 |  1 |  1 | 1 |  0 |
y-1  |  1 |  1 |  0 |  0 | 1 |  0 |
x+y  |  0 |  0 |  0 |  0 | 1 |  0 |
x-y  |  0 |  1 |  0 |  0 | 1 |  1 |
y-x  |  0 |  0 |  0 |  1 | 1 |  1 |
x&y  |  0 |  0 |  0 |  0 | 0 |  0 |
x|y  |  0 |  1 |  0 |  1 | 0 |  1 |
```
