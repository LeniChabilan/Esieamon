# Esieamon


MAC : 

javac -d classes -sourcepath src $(find src -name "*.java")


java -cp $(pwd)/classes com.esiea.pootp.EsieamonExecutable 



Romain :

javac -d classes $(find | grep -E "./com/.*.java")

java -cp $(pwd)/classes com.esiea.pootp.EsieamonExecutable