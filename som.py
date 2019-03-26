import numpy as np
from tkinter import *
from PIL import ImageTk, Image
import os
import random as rand
import math
from time import sleep

iterations = 1000
width = 200
height = 200
displayWidth = width*2
displayHeight = height*2   

# turning rbg to hex
_NUMERALS = '0123456789abcdefABCDEF'
_HEXDEC = {v: int(v, 16) for v in (x+y for x in _NUMERALS for y in _NUMERALS)}
LOWERCASE = 'x'

def rgb(triplet):
    return _HEXDEC[triplet[0:2]], _HEXDEC[triplet[2:4]], _HEXDEC[triplet[4:6]]

def triplet(rgb, lettercase=LOWERCASE):
    return format(rgb[0]<<16 | rgb[1]<<8 | rgb[2], '06'+lettercase)

def toHex(rgb):
   # turn weights to colors
   intRgb = [0,0,0]
   for i in range(len(rgb)):
      intRgb[i] = int(rgb[i] * 255)
   return '#{}'.format(triplet(intRgb))

def _iterator(x, y):
      for i in range(x):
         for j in range(y):
               yield np.array([i, j])

def euclidianDistance(bmu, chosen):
   summ = 0
   for i in range(len(bmu)):
      # current_nodes_weight - input_vector
      summ += math.pow(bmu[i] - chosen[i],2) # replace chosen with weight values
   
   return math.sqrt(summ)

# get the best matching unit
def getBMU(chosen, c):
   bmu = c[0,0]
   bestDistance = euclidianDistance(bmu, chosen)
   xCoord = 0
   yCoord = 0
   for i in range(len(c)):
      for j in range(len(c[0])):
         curDistance = euclidianDistance(c[i,j], chosen)

         # if current distance is better than bmu change bmu to current best
         if curDistance < bestDistance:
            bestDistance = curDistance
            bmu = colors[i,j]
            xCoord = j
            yCoord = i
   
   return np.array([bmu, xCoord, yCoord])

# gets the neighbourhood size of the current iteration
def getNeighbourhoodRadius(rad, itern, lam):
   return rad * math.exp(-itern/lam)

#calculates distance between 2 vectors
def distance(x1, y1, x2, y2):
   x = math.pow(x1 - x2, 2)
   y = math.pow(y1 - y2, 2)
   return math.sqrt(x + y)

def getWeightChange(dis, rad):
   rad2 = 2 * (rad*rad)
   return math.exp(-(dis**2)/rad2)

def changeWeight(color, inputVec, learning, change):
   for i in range(3):
      color[i] += change * learning * (inputVec[i] - color[i])
   return color

# START ASSIGNMENT
root = Tk()
w = Canvas(root, width=displayWidth, height=displayHeight)
w.pack()
# Create the random color array
colors = np.random.random_sample(size=(width,height,3))

# DRAW GUI
x1 = 0
y1 = 0
x2 = 2
y2 = 2
for i in range(height):
   for j in range(width):
      w.create_rectangle(x1, y1, x2, y2, fill=''+toHex(colors[i,j]), outline="")
      x1 += 2
      x2 += 2
   y1 += 2
   y2 += 2
   x1 = 0
   x2 = 2
## TRAINING THE NETWORK
# start competition

# Choose 4 random colors from colors array
inputVec = np.random.randint(width/10, size=(200, 2)) # 4 values, 2 (x,y)

# TRAINING

inputColor = np.random.random_sample(size=(200, 3)) # 4 values 3 (r,g,b)
# # get color from coords in colors
# for i in range(len(inputVec)):
#    inputColor[i] = colors[inputVec[i,0], inputVec[i,1]] # get color from colors

# inputColor = [
#    [1, 0, 0],
#    [0, 1, 0],
#    [0, 0, 1],
#    [1, 1, 0]
# ]
# setting up kohenan learning values
radius = 50
lamda = iterations / math.log(radius)
start_learning_rate = 0.2

# weights are small random values
#def learn(iteration):
for iteration in range(iterations+1):

   nRadius = radius * math.exp(-iteration/lamda)
   # learning rate decaying over time
   learningRate = start_learning_rate * math.exp(-iteration/lamda)
   for i in range(len(inputColor)):
      color = inputColor[i] # get current input
      bmuInfo = getBMU(color, colors)
      bmu = bmuInfo[0]
      x = bmuInfo[1]
      y = bmuInfo[2]

      # get radius
      startX = int(x - nRadius)
      startY = int(y - nRadius)
      endX = int(x + nRadius)
      endY = int(y + nRadius)

      # make sure radius is within bounds
      if startX < 0:
         startX = 0
      if startY < 0:
         startY = 0
      if endX >= width:
         endX = width
      if endY >= height:
         endY = height
      
      # start changing
      for y2 in range(len(colors)): # startY, endY
         for x2 in range(len(colors[0])): # startX, endX
            curr = colors[y2, x2] # get the color
            dist = distance(x, y, x2, y2)
            #if (dist <= nRadius):
               # change weights
            weightChange = getWeightChange(dist, nRadius)
            colors[y2,x2] = changeWeight(colors[y2,x2], color, learningRate, weightChange)

      # w = Canvas(root, width=displayWidth, height=displayHeight)
      # w.pack()
      if iteration % 50 == 0:
         x1 = 0
         y1 = 0
         x2 = 2
         y2 = 2
         for i in range(height):
            for j in range(width):
               w.create_rectangle(x1, y1, x2, y2, fill=''+toHex(colors[i,j]), outline="")
               x1 += 2
               x2 += 2
            y1 += 2
            y2 += 2
            x1 = 0
            x2 = 2
         root.update_idletasks()
         root.update()
            #sleep(0.5)
# val = 0
# for i in range(50):
#    root.after(500, learn(i))
#root.mainloop()
