import numpy as np
from tkinter import *
from PIL import ImageTk, Image
import os
import random as rand

# turning rbg to hex
_NUMERALS = '0123456789abcdefABCDEF'
_HEXDEC = {v: int(v, 16) for v in (x+y for x in _NUMERALS for y in _NUMERALS)}
LOWERCASE = 'x'

def rgb(triplet):
    return _HEXDEC[triplet[0:2]], _HEXDEC[triplet[2:4]], _HEXDEC[triplet[4:6]]

def triplet(rgb, lettercase=LOWERCASE):
    return format(rgb[0]<<16 | rgb[1]<<8 | rgb[2], '06'+lettercase)

def toHex(rgb):
   return '#{}'.format(triplet(rgb))

# START ASSIGNMENT
root = Tk()
w = Canvas(root, width=200, height=200)
w.pack()
# Create the random color array
colors = np.random.randint(255, size=(50,50,3))

x1 = 0
y1 = 0
x2 = 4
y2 = 4
for i in range(50):
   for j in range(50):
      w.create_rectangle(x1, y1, x2, y2, fill=''+toHex(colors[i,j]), outline="")
      x1 += 4
      x2 += 4
   y1 += 4
   y2 += 4
   x1 = 0
   x2 = 4

root.mainloop()

## TRAINING THE NETWORK