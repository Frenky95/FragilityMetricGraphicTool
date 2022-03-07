import sys
import operator
import csv
import os
import re
from pathlib import Path


my_file2 = Path(sys.argv[1].split(".")[0]+"_cleared.txt")
if my_file2.is_file():
    os.remove(sys.argv[1].split(".")[0]+"_cleared.txt")


with open(sys.argv[1]) as f:
    lines = f.readlines()

f1 = open(sys.argv[1].split(".")[0]+"_cleared.txt","a")
i=0
while i<len(lines):
    if len(lines[i].split(',')[0].strip().split('_')) != 2 :
        f1.write(lines[i])
        i+=1
    else:
        i+=2
        
f1.close()
