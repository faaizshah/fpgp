#! /bin/bash
# Script of jar exection of missing values program
echo ""
echo "Bienvenue et Bonjour !"
echo ""
read -e -p "entrez le nom de fichier avec l'extension .jar :   " file
echo ""
python resultsPythonScriptFGPG.py -i $file -s 0.1
sleep 15
python resultsPythonScriptFGPG.py -i $file -s 0.2
sleep 15
python resultsPythonScriptFGPG.py -i $file -s 0.3
sleep 15
python resultsPythonScriptFGPG.py -i $file -s 0.4
sleep 15
python resultsPythonScriptFGPG.py -i $file -s 0.5
sleep 15
python resultsPythonScriptFGPG.py -i $file -s 0.6
sleep 15
python resultsPythonScriptFGPG.py -i $file -s 0.7
sleep 15
python resultsPythonScriptFGPG.py -i $file -s 0.8

echo "Program ends"
