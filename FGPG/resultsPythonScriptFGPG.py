#!/usr/bin/python
import os, errno
import sys, getopt
import time
import csv
import argparse
from fnmatch import fnmatch

 
parser = argparse.ArgumentParser(description='This is a script gradual pattern extraction program.')
parser.add_argument('-i','--input', help='Input file name',required=True)
parser.add_argument('-s','--minSupport',help='Minimum support value', required=True)
args = parser.parse_args()

## show values ##
## show values ##
print ("\n WELCOME TO GRADUAL PATTERN EXTRACTION PROGRAM \n" )
print ("Input file: %s" % args.input )
print ("Minimum support: %s" % args.minSupport+"\n" ) 

#inFile = sys.argv[1]
#support = sys.argv[2]


inFile = args.input
support = args.minSupport
path = os.getcwd()

filename = path+'/Output-Results/'+inFile[:-4]+'-SP-'+support+'/'

if not os.path.exists(os.path.dirname(filename)):
    try:
        os.makedirs(os.path.dirname(filename))
    except OSError as exc: # Guard against race condition
        if exc.errno != errno.EEXIST:
            raise

for file in os.listdir(path):
	if fnmatch(file,inFile):
		#name = path + '/results-Latest/'
		#name = filename
		file2 = open(filename +'/'+ file[:-4] +'-SP-'+ support+ '.csv', 'wb')
		l = []
		l1 = []
		writer = csv.writer(file2)
		for x in range(0,5):
			os.system('java  -Xmx10G -XX:BiasedLockingStartupDelay=0 -XX:+UseG1GC -jar '+ file +' '+ support +'  >' + filename +'/'+ file[:-4] +'-SP-'+ support+'.'+str(x) +'.txt')
			time.sleep(10)
			handle = open(filename +'/'+ file[:-4] +'-SP-'+support+'.'+ str(x) +'.txt', 'r')
			lines_list = handle.readlines()
			last = lines_list[-1]
			sec_last = lines_list[-2]
			mem = sec_last[23:-1]
			sec = last[19:-8]
			#print "time: "+sec+ " Mem: "+ mem 
			writer.writerow([sec,mem])
			l.append(float(sec))
			l1.append(float(mem))
		writer.writerow(["AvgTime:", "AvgMem:"])
		avg = sum(l)/float(len(l))
		avg2 = sum(l1)/float(len(l1))
		print "AvgTimeConsumption: " + str(avg) + "   AvgMemoryUtilization: "+ str(avg2)+"\n"
		writer.writerow([avg, avg2])
		



