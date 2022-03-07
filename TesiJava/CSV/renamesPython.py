import os

path = '.\\'

files = os.listdir(path)

for f in files:
    if f.split("_")[0] == "ScannerPythonTesi2" :
        #print(f)
        os.rename(f,"ScannerPythonTesi"+f.split(".csv")[0][18:]+"_2.csv")
