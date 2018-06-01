import time

def mandelbrot(size):
  my_sum = 0

  byte_acc = 0
  bit_num  = 0

  y = 0

  while (y < size):
    ci = (2.0 * y / size) - 1.0

    x = 0
    
    while (x < size):
      zr   = 0.0
      zrzr = 0.0
      zi   = 0.0
      zizi = 0.0
      cr = (2.0 * x / size) - 1.5

      z = 0
      escape = 1

      while (z < 50):
        tr = zrzr - zizi + cr
        ti = 2.0 * zr * zi + ci

        zr = tr
        zi = ti

        zrzr = zr*zr
        zizi = zi*zi
        if (zrzr + zizi > 4.0):
          escape = 0
          break

        z = z + 1

      byte_acc = (byte_acc << 1) | escape
      bit_num = bit_num + 1

      if (bit_num == 8):
        my_sum = my_sum ^ byte_acc
        byte_acc = 0
        bit_num  = 0
      elif (x == size - 1):
        byte_acc = byte_acc << (8 - bit_num)
        my_sum = my_sum ^ byte_acc
        byte_acc = 0
        bit_num  = 0

      x = x + 1
    
    y = y + 1
  
  return my_sum


def warmup():
  n = 0
  while n < 10000:
    mandelbrot(10);
    n = n + 1


def sample():
  return mandelbrot(750) == 192


iterations = 20
warmup = 0
problemSize = 1000

if not sample():
  print "Value Error!"

print "Overall iterations: ", iterations
print "Warmup  iterations: ", warmup
print "Problem size:       ", problemSize

while (warmup > 0):
  mandelbrot(problemSize)
  warmup = warmup - 1

while (iterations > 0):
  start = time.time()
  mandelbrot(problemSize)
  elapsed = (time.time() - start) * 1000000
  iterations = iterations - 1

  print "Mandelbrot: iterations=1 runtime: ", elapsed, "us"
